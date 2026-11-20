import java.util.*;

/**
 * Reddit — Staff SWE coding problem: "Moderator List Hierarchy"
 *
 * Parse a newline-delimited, comma-separated action log and model a Reddit-style
 * moderator hierarchy. A moderator's EFFECTIVE ACCESS TIMESTAMP determines rank
 * (earlier = higher in the list); a mod may remove only those ranked BELOW them.
 *
 * Single self-contained, COMPILABLE + RUNNABLE file solving every part.
 *   javac ModeratorListHierarchy.java && java ModeratorListHierarchy
 *
 * Parts:
 *   PART 1  ModList                        — one global mod list
 *   PART 2  CommunityModList               — independent per-community lists
 *   PART 3  ReorderableCommunityModList    — + demote (reassign timestamp, reorder)
 *   PART 3b ReorderableCommunityModListOpt — follow-up: get_mod_list in O(m), no sort
 */
public class ModeratorListHierarchy {

    /* =====================================================================
       PART 1 — SINGLE MODERATOR LIST
       ---------------------------------------------------------------------
       Log line:  target, action, actor, timestamp     (action = add | remove)
       Core model: replay the log once, store each ACTIVE moderator's effective
       access timestamp in a map. Rank = timestamp order (earlier = higher).

       - add    : target becomes active at `timestamp` (re-add => brand-new ts)
       - remove : target becomes inactive (drop from the map)
       - can_remove_mod(actor,target): both active, actor != target,
         and actor's timestamp is strictly EARLIER than target's.
       - get_mod_list(): active mods top -> bottom (ascending timestamp).

       Complexity: ctor O(n); can_remove O(1); get_mod_list O(m log m); space O(m).
       ===================================================================== */
    static class ModList {
        // moderator -> effective access timestamp (only ACTIVE mods are present)
        private final Map<String, Integer> activeSince = new HashMap<>();

        ModList(String logs) {
            for (String raw : logs.split("\n")) {
                String line = raw.trim();
                if (line.isEmpty()) continue;

                String[] f = line.split(",");
                String target = f[0].trim();
                String action = f[1].trim();
                // f[2] = actor (not needed for state — every logged action is valid)
                int ts = Integer.parseInt(f[3].trim());

                switch (action) {
                    case "add"    -> activeSince.put(target, ts);   // re-add => new ts
                    case "remove" -> activeSince.remove(target);
                    default -> throw new IllegalArgumentException("Unsupported action: " + action);
                }
            }
        }

        boolean canRemoveMod(String actor, String target) {
            if (actor.equals(target)) return false;
            Integer a = activeSince.get(actor), t = activeSince.get(target);
            if (a == null || t == null) return false;     // both must be active
            return a < t;                                 // actor strictly higher (earlier)
        }

        List<String> getModList() {
            // Sort by (timestamp, name). Name tie-break is harmless here (ts unique
            // in Part 1) and makes ordering deterministic if ties ever appear.
            List<Map.Entry<String, Integer>> e = new ArrayList<>(activeSince.entrySet());
            e.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                             .thenComparing(Map.Entry::getKey));
            List<String> out = new ArrayList<>(e.size());
            for (var entry : e) out.add(entry.getKey());
            return out;
        }
    }

    /* =====================================================================
       PART 2 — MULTIPLE COMMUNITIES
       ---------------------------------------------------------------------
       CHANGE FROM PART 1:
         * Log line gains a leading field:  community, target, action, actor, timestamp
         * State becomes a NESTED map: community -> (moderator -> timestamp).
           The same username can be a mod in several communities with different ts.
         * Every query is scoped to a given community; otherwise the logic is
           byte-for-byte the Part-1 logic applied to that community's inner map.

       Complexity: ctor O(n); can_remove O(1); get_mod_list(c) O(m log m); space O(total active).
       ===================================================================== */
    static class CommunityModList {
        // community -> (moderator -> effective access timestamp)
        private final Map<String, Map<String, Integer>> activeSince = new HashMap<>();

        CommunityModList(String logs) {
            for (String raw : logs.split("\n")) {
                String line = raw.trim();
                if (line.isEmpty()) continue;

                String[] f = line.split(",");
                String community = f[0].trim();               // NEW leading field
                String target    = f[1].trim();
                String action    = f[2].trim();
                // f[3] = actor
                int ts = Integer.parseInt(f[4].trim());

                Map<String, Integer> mods =
                        activeSince.computeIfAbsent(community, k -> new HashMap<>());
                switch (action) {
                    case "add"    -> mods.put(target, ts);
                    case "remove" -> mods.remove(target);
                    default -> throw new IllegalArgumentException("Unsupported action: " + action);
                }
            }
        }

        boolean canRemoveMod(String community, String actor, String target) {
            Map<String, Integer> mods = activeSince.getOrDefault(community, Map.of());
            if (actor.equals(target)) return false;
            Integer a = mods.get(actor), t = mods.get(target);
            if (a == null || t == null) return false;
            return a < t;
        }

        List<String> getModList(String community) {
            Map<String, Integer> mods = activeSince.getOrDefault(community, Map.of());
            List<Map.Entry<String, Integer>> e = new ArrayList<>(mods.entrySet());
            e.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                             .thenComparing(Map.Entry::getKey));
            List<String> out = new ArrayList<>(e.size());
            for (var entry : e) out.add(entry.getKey());
            return out;
        }
    }

    /* =====================================================================
       PART 3 — DEMOTION (reorder the current mod list)
       ---------------------------------------------------------------------
       CHANGE FROM PART 2 (the ONLY real change):
         * A third action `demote` is added. Demotion keeps the target ACTIVE
           but reassigns their effective timestamp to the demotion timestamp,
           so they slide DOWN in that community's list.
         * Because rank is derived purely from the stored timestamp, demote is
           literally "overwrite the timestamp if the mod is active" — no other
           code changes. can_remove_mod / get_mod_list are unchanged.
       ===================================================================== */
    static class ReorderableCommunityModList {
        private final Map<String, Map<String, Integer>> activeSince = new HashMap<>();

        ReorderableCommunityModList(String logs) {
            for (String raw : logs.split("\n")) {
                String line = raw.trim();
                if (line.isEmpty()) continue;

                String[] f = line.split(",");
                String community = f[0].trim();
                String target    = f[1].trim();
                String action    = f[2].trim();
                int ts = Integer.parseInt(f[4].trim());

                Map<String, Integer> mods =
                        activeSince.computeIfAbsent(community, k -> new HashMap<>());
                switch (action) {
                    case "add"    -> mods.put(target, ts);
                    case "remove" -> mods.remove(target);
                    // NEW: demote only reorders an ALREADY-active mod (no-op if absent).
                    case "demote" -> { if (mods.containsKey(target)) mods.put(target, ts); }
                    default -> throw new IllegalArgumentException("Unsupported action: " + action);
                }
            }
        }

        boolean canRemoveMod(String community, String actor, String target) {
            Map<String, Integer> mods = activeSince.getOrDefault(community, Map.of());
            if (actor.equals(target)) return false;
            Integer a = mods.get(actor), t = mods.get(target);
            if (a == null || t == null) return false;
            return a < t;
        }

        List<String> getModList(String community) {
            Map<String, Integer> mods = activeSince.getOrDefault(community, Map.of());
            List<Map.Entry<String, Integer>> e = new ArrayList<>(mods.entrySet());
            e.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                             .thenComparing(Map.Entry::getKey));
            List<String> out = new ArrayList<>(e.size());
            for (var entry : e) out.add(entry.getKey());
            return out;
        }
    }

    /* =====================================================================
       PART 3b — FOLLOW-UP: get_mod_list in O(m) WITHOUT sorting each call
       ---------------------------------------------------------------------
       Keep the list ORDERED as we build it. Timestamps are unique within a
       community, so a per-community TreeMap<timestamp, moderator> stays sorted
       by rank; a parallel moderator->timestamp map gives O(1)/O(log m) lookups.
         * add    : put in both maps.
         * remove : delete from both maps.
         * demote : erase the old (ts -> mod) entry, insert the new one.  <-- the
                    reorder that a flat HashMap can only express by re-sorting.
       Now get_mod_list = one in-order TreeMap traversal = O(m), no per-call sort.
       ===================================================================== */
    static class ReorderableCommunityModListOpt {
        // community -> ordered (timestamp -> moderator), i.e. top-to-bottom by rank
        private final Map<String, TreeMap<Integer, String>> byRank = new HashMap<>();
        // community -> (moderator -> current timestamp)
        private final Map<String, Map<String, Integer>> tsOf = new HashMap<>();

        ReorderableCommunityModListOpt(String logs) {
            for (String raw : logs.split("\n")) {
                String line = raw.trim();
                if (line.isEmpty()) continue;

                String[] f = line.split(",");
                String community = f[0].trim();
                String target    = f[1].trim();
                String action    = f[2].trim();
                int ts = Integer.parseInt(f[4].trim());

                TreeMap<Integer, String> rank = byRank.computeIfAbsent(community, k -> new TreeMap<>());
                Map<String, Integer>     tmap = tsOf.computeIfAbsent(community, k -> new HashMap<>());

                switch (action) {
                    case "add" -> {
                        Integer old = tmap.put(target, ts);      // re-add => new ts
                        if (old != null) rank.remove(old);        // drop any stale rank entry
                        rank.put(ts, target);
                    }
                    case "remove" -> {
                        Integer old = tmap.remove(target);
                        if (old != null) rank.remove(old);
                    }
                    case "demote" -> {
                        Integer old = tmap.get(target);
                        if (old != null) {                        // only reorder active mods
                            rank.remove(old);                     // pull out old rank slot
                            tmap.put(target, ts);
                            rank.put(ts, target);                 // reinsert lower down
                        }
                    }
                    default -> throw new IllegalArgumentException("Unsupported action: " + action);
                }
            }
        }

        boolean canRemoveMod(String community, String actor, String target) {
            Map<String, Integer> mods = tsOf.getOrDefault(community, Map.of());
            if (actor.equals(target)) return false;
            Integer a = mods.get(actor), t = mods.get(target);
            if (a == null || t == null) return false;
            return a < t;
        }

        List<String> getModList(String community) {
            TreeMap<Integer, String> rank = byRank.get(community);
            if (rank == null) return List.of();
            return new ArrayList<>(rank.values());                // already in rank order — O(m)
        }
    }

    /* =====================================================================
       DEMO / SELF-TEST  (uses the exact examples from the prompt)
       ===================================================================== */
    private static void check(boolean cond, String msg) {
        if (!cond) throw new AssertionError("FAILED: " + msg);
        System.out.println("  ok: " + msg);
    }

    public static void main(String[] args) {
        // ---------- PART 1 ----------
        String logs1 = String.join("\n",
                "alice,add,SYSTEM,1",
                "bob,add,alice,2",
                "carol,add,alice,3",
                "dave,add,bob,4",
                "carol,remove,alice,5");
        ModList m1 = new ModList(logs1);
        System.out.println("PART 1");
        check(m1.canRemoveMod("alice", "bob"), "alice can remove bob");
        check(!m1.canRemoveMod("bob", "alice"), "bob cannot remove alice");
        check(m1.canRemoveMod("bob", "dave"), "bob can remove dave");
        check(m1.getModList().equals(List.of("alice", "bob", "dave")), "list = [alice, bob, dave]");

        // ---------- PART 2 ----------
        String logs2 = String.join("\n",
                "python,alice,add,SYSTEM,1",
                "python,bob,add,alice,2",
                "python,carol,add,alice,3",
                "datascience,maya,add,SYSTEM,4",
                "datascience,bob,add,maya,5",
                "python,carol,remove,alice,6");
        CommunityModList m2 = new CommunityModList(logs2);
        System.out.println("PART 2");
        check(m2.getModList("python").equals(List.of("alice", "bob")), "python = [alice, bob]");
        check(m2.getModList("datascience").equals(List.of("maya", "bob")), "datascience = [maya, bob]");
        check(m2.canRemoveMod("python", "alice", "bob"), "python: alice can remove bob");
        check(!m2.canRemoveMod("datascience", "bob", "maya"), "datascience: bob cannot remove maya");

        // ---------- PART 3 ----------
        String logs3 = String.join("\n",
                "python,alice,add,SYSTEM,1",
                "python,bob,add,alice,2",
                "python,carol,add,alice,3",
                "python,bob,demote,alice,4");
        ReorderableCommunityModList m3 = new ReorderableCommunityModList(logs3);
        System.out.println("PART 3");
        check(m3.getModList("python").equals(List.of("alice", "carol", "bob")), "python = [alice, carol, bob]");
        check(m3.canRemoveMod("python", "carol", "bob"), "carol can remove bob (after demote)");
        check(!m3.canRemoveMod("python", "bob", "carol"), "bob cannot remove carol (after demote)");

        // ---------- PART 3b (O(m) optimized, same answers) ----------
        ReorderableCommunityModListOpt m3b = new ReorderableCommunityModListOpt(logs3);
        System.out.println("PART 3b (O(m) get_mod_list)");
        check(m3b.getModList("python").equals(List.of("alice", "carol", "bob")), "python = [alice, carol, bob]");
        check(m3b.canRemoveMod("python", "carol", "bob"), "carol can remove bob");
        check(!m3b.canRemoveMod("python", "bob", "carol"), "bob cannot remove carol");

        System.out.println("\nAll checks passed.");
    }
}
