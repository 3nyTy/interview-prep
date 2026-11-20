import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Admin Action Log: Order & Permission   (Reddit Staff sample)
 * ------------------------------------------------------------------------
 * Replay a chronological moderator action log, then answer:
 *   - canRemoveMod(target, actor): can `actor` remove `target`?  (seniority check)
 *   - getModRanking(): active moderators, highest level -> lowest.
 *
 * SENIORITY RULE (the trap):  among active mods, the one whose MOST-RECENT
 * "added" event happened EARLIER is the HIGHER level.  So rank ascending by
 * most-recent-add timestamp: earlier add == more senior == nearer the top.
 *
 * RE-ADD RESET (the second trap): removing then re-adding a user overwrites the
 * most-recent-add timestamp, so a re-added mod becomes the MOST JUNIOR.
 *
 * This file evolves the solution through the prompt's parts, each as its own
 * class so every part is complete and runnable on its own:
 *   Part 1  ModSystemV1 : base, single community. Two maps, one-pass build.
 *   Part 2  ModSystemV2 : community scope. State keyed by (community, user).
 *   Part 3  ModSystemV3 : + demote(). Per-community DOUBLY LINKED LIST so a
 *                         one-position demotion is O(1); `pos` index kept in
 *                         sync so canRemoveMod stays O(1).
 *   Part 4  (scale)     : ModSystemV3.fromStream(...) streams the log line by
 *                         line (peak memory O(unique mods), not O(log size));
 *                         plus the sharding / O(1)-index discussion in chat.
 *
 * Log literals assumed: action is "added" / "removed" (older variants use
 * add/delete -- confirm with the interviewer). Malformed lines are ignored.
 * ------------------------------------------------------------------------
 */
public class AdminActionLog {

    /* =====================================================================
     * PART 1 -- base ModSystem, single community
     *   Line format: <targetUser>,<action>,<actorUser>,<timestamp>
     * ===================================================================== */
    static class ModSystemV1 {

        // user -> timestamp of their MOST-RECENT 'added' event (seniority key).
        // Smaller timestamp => added earlier => more senior => HIGHER level.
        private final Map<String, Long> mostRecentAdd = new HashMap<>();
        // user -> whether the user is CURRENTLY a moderator.
        private final Map<String, Boolean> active = new HashMap<>();

        public ModSystemV1(List<String> logs) {
            for (String line : logs) {                 // one pass, log is chronological
                String[] p = line.split(",");
                if (p.length < 4) continue;            // ignore malformed (clarify: ignore vs raise)
                String target = p[0].trim();
                String action = p[1].trim();
                long ts       = Long.parseLong(p[3].trim());

                if (action.equals("added")) {
                    active.put(target, Boolean.TRUE);
                    mostRecentAdd.put(target, ts);     // most-recent add; re-add resets seniority
                } else if (action.equals("removed")) {
                    active.put(target, Boolean.FALSE); // stale timestamp is fine; guarded by `active`
                }
            }
        }

        private boolean isActive(String u) { return active.getOrDefault(u, false); }

        public boolean canRemoveMod(String targetUser, String actorUser) {
            if (actorUser.equals(targetUser)) return false;               // can't act on self
            if (!isActive(actorUser) || !isActive(targetUser)) return false; // both must be mods
            // actor higher level than target  <=>  actor added earlier  <=>  smaller timestamp.
            return mostRecentAdd.get(actorUser) < mostRecentAdd.get(targetUser);
        }

        public List<String> getModRanking() {
            List<String> mods = new ArrayList<>();
            for (Map.Entry<String, Boolean> e : active.entrySet())
                if (e.getValue()) mods.add(e.getKey());
            // Highest first: earliest most-recent-add first; tie-break by name for determinism.
            mods.sort((a, b) -> {
                int c = Long.compare(mostRecentAdd.get(a), mostRecentAdd.get(b));
                return c != 0 ? c : a.compareTo(b);
            });
            return mods;
        }
    }

    /* =====================================================================
     * PART 2 -- community scope
     *   Line format now: <community>,<targetUser>,<action>,<actorUser>,<timestamp>
     *
     * WHAT CHANGED vs Part 1 (all marked // <-- CHANGED / NEW):
     *   - Every state map gains an outer `community` key.
     *   - Parser reads a leading community field.
     *   - canRemoveMod / getModRanking take a `community` and operate within it.
     * ===================================================================== */
    static class ModSystemV2 {

        // community -> (user -> most-recent-add timestamp)          <-- CHANGED: nested by community
        private final Map<String, Map<String, Long>> mostRecentAdd = new HashMap<>();
        // community -> set of users CURRENTLY moderating there       <-- CHANGED: nested by community
        private final Map<String, Set<String>> active = new HashMap<>();

        public ModSystemV2(List<String> logs) {
            for (String line : logs) {
                String[] p = line.split(",");
                if (p.length < 5) continue;
                String community = p[0].trim();        // <-- NEW field
                String target    = p[1].trim();
                String action    = p[2].trim();
                long ts          = Long.parseLong(p[4].trim());

                if (action.equals("added")) {
                    active.computeIfAbsent(community, k -> new HashSet<>()).add(target);
                    mostRecentAdd.computeIfAbsent(community, k -> new HashMap<>()).put(target, ts);
                } else if (action.equals("removed")) {
                    Set<String> s = active.get(community);
                    if (s != null) s.remove(target);
                }
            }
        }

        private boolean isActive(String community, String u) {
            Set<String> s = active.get(community);
            return s != null && s.contains(u);
        }

        public boolean canRemoveMod(String community, String targetUser, String actorUser) { // <-- community
            if (actorUser.equals(targetUser)) return false;
            if (!isActive(community, actorUser) || !isActive(community, targetUser)) return false;
            Map<String, Long> ts = mostRecentAdd.get(community);
            return ts.get(actorUser) < ts.get(targetUser);
        }

        public List<String> getModRanking(String community) {                                 // <-- community
            Set<String> s       = active.getOrDefault(community, Collections.emptySet());
            Map<String, Long> ts = mostRecentAdd.getOrDefault(community, Collections.emptyMap());
            List<String> mods = new ArrayList<>(s);
            mods.sort((a, b) -> {
                int c = Long.compare(ts.get(a), ts.get(b));
                return c != 0 ? c : a.compareTo(b);
            });
            return mods;
        }
    }

    /* =====================================================================
     * PART 3 -- add demote(community, user)
     *
     * WHY THE DATA STRUCTURE CHANGES:
     *   Parts 1-2 could "sort on read" because rank == seniority timestamp.
     *   demote() moves a user down ONE position, perturbing order beyond any
     *   timestamp. So we hold an explicit, MUTABLE order: a per-community
     *   doubly linked list (head = highest level) + a user -> node map.
     *     - demote  : O(1) adjacent-node swap.
     *     - ranking : O(m) list walk.
     *     - canRemoveMod : O(1) via an integer `pos` on each node (swapped on
     *                      demote so it always reflects current order).
     *
     * WHAT CHANGED vs Part 2 (marked // <-- NEW / CHANGED):
     *   - Node / Community types; per-community linked list built after replay.
     *   - "level" now means CURRENT list position (node.pos), not raw timestamp.
     *   - new demote(); getModRanking walks the list; canRemoveMod compares pos.
     * ===================================================================== */
    static class ModSystemV3 {

        private static class Node {                    // <-- NEW
            final String user;
            Node prev, next;
            int pos;                                   // 0 = highest level; smaller pos = higher
            Node(String user) { this.user = user; }
        }
        private static class Community {               // <-- NEW
            Node head, tail;
            final Map<String, Node> nodeOf = new HashMap<>(); // user -> node; keys == active mods
        }

        private final Map<String, Community> communities = new HashMap<>();  // <-- CHANGED: list-backed

        public ModSystemV3(List<String> logs) {
            Map<String, Map<String, Long>> ts = new HashMap<>();
            Map<String, Set<String>> active = new HashMap<>();
            for (String line : logs) applyLine(line, ts, active); // phase 1: replay to current state
            buildLists(ts, active);                               // phase 2: build ordered lists
        }

        private ModSystemV3() {}   // for the streaming factory (Part 4)

        /* PART 4 (scale): stream line-by-line so peak memory is O(unique mods),
         * NOT O(total log size) -- we never hold the whole log at once. */
        public static ModSystemV3 fromStream(BufferedReader in) throws IOException {
            Map<String, Map<String, Long>> ts = new HashMap<>();
            Map<String, Set<String>> active = new HashMap<>();
            String line;
            while ((line = in.readLine()) != null) {
                if (!line.isEmpty()) applyLine(line, ts, active);
            }
            ModSystemV3 sys = new ModSystemV3();
            sys.buildLists(ts, active);
            return sys;
        }

        // Fold one log line into current state. (A manual comma scan avoids the
        // per-line String[] and skips the unused actor field -- a further win at
        // millions of lines; split() is used here for readability.)
        private static void applyLine(String line, Map<String, Map<String, Long>> ts,
                                      Map<String, Set<String>> active) {
            String[] p = line.split(",");
            if (p.length < 5) return;
            String community = p[0].trim();
            String target    = p[1].trim();
            String action    = p[2].trim();
            if (action.equals("added")) {
                active.computeIfAbsent(community, k -> new HashSet<>()).add(target);
                ts.computeIfAbsent(community, k -> new HashMap<>()).put(target, Long.parseLong(p[4].trim()));
            } else if (action.equals("removed")) {
                Set<String> s = active.get(community);
                if (s != null) s.remove(target);
            }
        }

        // Phase 2: initial order of each community's list = seniority (earliest add first).
        private void buildLists(Map<String, Map<String, Long>> ts, Map<String, Set<String>> active) {
            for (Map.Entry<String, Set<String>> e : active.entrySet()) {
                String community = e.getKey();
                Map<String, Long> tmap = ts.get(community);
                List<String> ordered = new ArrayList<>(e.getValue());
                ordered.sort((a, b) -> {
                    int c = Long.compare(tmap.get(a), tmap.get(b));   // earlier add = higher level
                    return c != 0 ? c : a.compareTo(b);
                });
                Community comm = new Community();
                Node prev = null;
                int pos = 0;
                for (String u : ordered) {
                    Node n = new Node(u);
                    n.pos = pos++;
                    n.prev = prev;
                    if (prev == null) comm.head = n; else prev.next = n;
                    prev = n;
                    comm.nodeOf.put(u, n);
                }
                comm.tail = prev;
                communities.put(community, comm);
            }
        }

        public boolean canRemoveMod(String community, String targetUser, String actorUser) {
            if (actorUser.equals(targetUser)) return false;
            Community comm = communities.get(community);
            if (comm == null) return false;
            Node a = comm.nodeOf.get(actorUser);
            Node t = comm.nodeOf.get(targetUser);
            if (a == null || t == null) return false;          // both must be active mods
            return a.pos < t.pos;   // <-- CHANGED: level = CURRENT list position (reflects demotes)
        }

        public List<String> getModRanking(String community) {
            List<String> out = new ArrayList<>();
            Community comm = communities.get(community);
            if (comm == null) return out;
            for (Node n = comm.head; n != null; n = n.next) out.add(n.user);  // <-- CHANGED: walk list
            return out;
        }

        // <-- NEW: move `user` down exactly one rank in O(1). No-op if already
        //          lowest, or not a current moderator in this community.
        public void demote(String community, String user) {
            Community comm = communities.get(community);
            if (comm == null) return;
            Node a = comm.nodeOf.get(user);
            if (a == null) return;              // not a current moderator -> no-op
            Node b = a.next;
            if (b == null) return;              // already lowest-ranked -> no-op
            // Swap adjacent nodes a,b:   ...p, a, b, q...  ->  ...p, b, a, q...
            Node p = a.prev, q = b.next;
            if (p != null) p.next = b; else comm.head = b;
            b.prev = p;
            b.next = a;
            a.prev = b;
            a.next = q;
            if (q != null) q.prev = a; else comm.tail = a;
            int tmp = a.pos; a.pos = b.pos; b.pos = tmp;   // keep pos in sync -> O(1) canRemoveMod
        }
    }

    /* ============================ demo / self-check ====================== */
    public static void main(String[] args) throws IOException {
        int[] score = {0, 0};   // {pass, fail}

        // ---------------- PART 1 ----------------
        List<String> logs1 = List.of(
            "alice,added,root,1",
            "bob,added,root,2",
            "carol,added,alice,3",
            "bob,removed,alice,4",
            "bob,added,carol,5"      // re-add RESETS bob's seniority -> most junior
        );
        ModSystemV1 v1 = new ModSystemV1(logs1);
        eq(score, "V1 ranking",            v1.getModRanking().toString(), "[alice, carol, bob]");
        eq(score, "V1 alice can rm carol", v1.canRemoveMod("carol", "alice"), true);
        eq(score, "V1 carol !rm alice",    v1.canRemoveMod("alice", "carol"), false);
        eq(score, "V1 carol can rm bob",   v1.canRemoveMod("bob", "carol"), true);   // reset trap
        eq(score, "V1 bob !rm carol",      v1.canRemoveMod("carol", "bob"), false);  // reset trap
        eq(score, "V1 self is no",         v1.canRemoveMod("alice", "alice"), false);
        eq(score, "V1 inactive actor no",  v1.canRemoveMod("alice", "dave"), false);

        // ---------------- PART 2 ----------------
        List<String> logs2 = List.of(
            "r/aws,alice,added,root,1",
            "r/aws,bob,added,root,2",
            "r/java,carol,added,root,3",
            "r/aws,alice,removed,bob,4",
            "r/java,dave,added,carol,5",
            "r/aws,alice,added,bob,6"    // alice re-added in r/aws -> now junior to bob
        );
        ModSystemV2 v2 = new ModSystemV2(logs2);
        eq(score, "V2 r/aws ranking",  v2.getModRanking("r/aws").toString(),  "[bob, alice]");
        eq(score, "V2 r/java ranking", v2.getModRanking("r/java").toString(), "[carol, dave]");
        eq(score, "V2 bob rm alice",   v2.canRemoveMod("r/aws", "alice", "bob"), true);
        eq(score, "V2 alice !rm bob",  v2.canRemoveMod("r/aws", "bob", "alice"), false);
        eq(score, "V2 carol rm dave",  v2.canRemoveMod("r/java", "dave", "carol"), true);
        eq(score, "V2 cross-community", v2.canRemoveMod("r/aws", "alice", "carol"), false); // carol not in r/aws

        // ---------------- PART 3 ----------------
        List<String> logs3 = List.of(
            "r/x,a,added,root,1",
            "r/x,b,added,root,2",
            "r/x,c,added,root,3",
            "r/x,d,added,root,4"
        );
        ModSystemV3 v3 = new ModSystemV3(logs3);
        eq(score, "V3 initial",        v3.getModRanking("r/x").toString(), "[a, b, c, d]");
        v3.demote("r/x", "b");                                                   // b moves below c
        eq(score, "V3 demote b",       v3.getModRanking("r/x").toString(), "[a, c, b, d]");
        eq(score, "V3 c outranks b",   v3.canRemoveMod("r/x", "b", "c"), true);  // reflects demotion
        eq(score, "V3 b !outrank c",   v3.canRemoveMod("r/x", "c", "b"), false);
        v3.demote("r/x", "d");                                                   // tail -> no-op
        eq(score, "V3 demote tail",    v3.getModRanking("r/x").toString(), "[a, c, b, d]");
        v3.demote("r/x", "a");                                                   // head moves down one
        eq(score, "V3 demote head",    v3.getModRanking("r/x").toString(), "[c, a, b, d]");
        v3.demote("r/x", "zzz");                                                 // not a mod -> no-op
        eq(score, "V3 demote non-mod", v3.getModRanking("r/x").toString(), "[c, a, b, d]");
        eq(score, "V3 c rm a",         v3.canRemoveMod("r/x", "a", "c"), true);
        eq(score, "V3 a !rm c",        v3.canRemoveMod("r/x", "c", "a"), false);

        // ---------------- PART 4 (streaming build == identical state) ----------------
        ModSystemV3 v3s = ModSystemV3.fromStream(new BufferedReader(new StringReader(String.join("\n", logs3))));
        eq(score, "V4 stream ranking", v3s.getModRanking("r/x").toString(), "[a, b, c, d]");

        System.out.println("\n==== " + score[0] + " passed, " + score[1] + " failed ====");
    }

    private static void eq(int[] score, String name, Object actual, Object expected) {
        boolean ok = Objects.equals(actual, expected);
        System.out.println((ok ? "PASS " : "FAIL ") + name + "  => " + actual
                + (ok ? "" : "   (expected " + expected + ")"));
        if (ok) score[0]++; else score[1]++;
    }
}
