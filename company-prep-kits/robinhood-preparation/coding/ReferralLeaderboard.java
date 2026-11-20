import java.util.*;

/**
 * Robinhood — "Referral Program Leaderboard"
 * Source: 1point3acres 40184b3e-adcc-4b4b-9804-5167e4d81d01
 *
 * "Design a referral program leaderboard with relational support such that if A
 *  refers B, and B refers C, C's count adds to BOTH A and B." Provide an
 *  efficient way to query the TOP 3, and keep performance under a large number
 *  of referral relationships.
 *
 * ---------------------------------------------------------------------------
 * This is ReferralCount.java turned into an ONLINE problem. Two separate design
 * decisions, and the interviewer is grading both:
 *
 * 1. TRANSITIVE CREDIT ON INSERT — when C joins under B, every ANCESTOR of C
 *    gains one. Walk up the parent chain. O(depth) per insert.
 *    ⚠️ Worst case is a degenerate chain: O(n) per insert, O(n^2) to build.
 *    Say that out loud and offer the mitigations (part3).
 *
 * 2. TOP-K QUERY — ⭐ and this is where the real insight is:
 *    A size-K min-heap is O(1) to read but CANNOT handle a score that DECREASES.
 *    That is fine here, because referral counts only ever grow. Say precisely
 *    that: "a heap works because this score is monotonically increasing; if
 *    referrals could be revoked I'd need an indexed structure instead."
 *    Naming the condition under which your own choice breaks is the move.
 * ---------------------------------------------------------------------------
 */
public class ReferralLeaderboard {

    /* ===================================================================
     * PART 1 — Transitive credit + a sorted top-K. The baseline.
     *
     * addReferral walks the ancestor chain incrementing each one. topK sorts
     * everybody, which is O(n log n) PER QUERY — correct but the thing to
     * improve. Write it, name the cost, then improve it.
     * =================================================================== */
    static class Baseline {
        final Map<String, String> parent = new HashMap<>();     // child -> referrer
        final Map<String, Integer> count = new HashMap<>();     // transitive descendant count

        void addUser(String u) {
            count.putIfAbsent(u, 0);
        }

        /** referrer refers newUser. */
        boolean addReferral(String referrer, String newUser) {
            if (!count.containsKey(referrer)) return false;     // referrer must exist
            if (parent.containsKey(newUser)) return false;      // one referrer per user
            if (referrer.equals(newUser)) return false;
            if (isAncestor(newUser, referrer)) return false;    // would create a cycle
            parent.put(newUser, referrer);
            count.putIfAbsent(newUser, 0);

            for (String a = referrer; a != null; a = parent.get(a))   // ⭐ every ancestor
                count.merge(a, 1, Integer::sum);
            return true;
        }

        private boolean isAncestor(String maybeAncestor, String node) {
            for (String a = node; a != null; a = parent.get(a))
                if (a.equals(maybeAncestor)) return true;
            return false;
        }

        List<String> topK(int k) {
            List<String> users = new ArrayList<>(count.keySet());
            users.sort(Comparator
                    .comparingInt((String u) -> count.get(u)).reversed()   // count DESC
                    .thenComparing(Comparator.naturalOrder()));            // tie: name ASC
            return users.subList(0, Math.min(k, users.size()));
        }
    }

    /* ===================================================================
     * PART 2 — ⭐ Indexed leaderboard. O(log n) update, O(K) query.
     *
     * CHANGES FROM PART 1:
     *   - a TreeSet ordered by (count DESC, id ASC) held alongside the map
     *   - on every score change: REMOVE the old entry, then RE-INSERT the new
     *     one. You cannot mutate a key in place inside a TreeSet — the set's
     *     ordering invariant is computed at insert time, so mutating the score
     *     of a contained element silently corrupts the structure and the element
     *     becomes unfindable. Remove-then-reinsert is mandatory, not stylistic.
     *   - topK is now just "take the first K", O(K)
     *
     * This handles DECREASES too, which the heap version does not — so it is
     * the answer if referrals can ever be revoked or a user deleted.
     * =================================================================== */
    /**
     * ⚠️ IMPLEMENTATION NOTE worth saying out loud: the comparator has to READ the
     * count map, so the TreeSet cannot be a field initialiser that runs before the
     * map exists — build it in the constructor and capture the map. A comparator
     * that closes over mutable state is exactly why remove-then-reinsert is
     * mandatory below.
     */
    static class IndexedBoard {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> count = new HashMap<>();
        private final TreeSet<String> board;

        IndexedBoard() {
            board = new TreeSet<>((a, b) -> {
                int ca = count.getOrDefault(a, 0), cb = count.getOrDefault(b, 0);
                if (ca != cb) return Integer.compare(cb, ca);    // count DESC
                return a.compareTo(b);                           // then id ASC
            });
        }

        void addUser(String u) {
            if (count.containsKey(u)) return;
            count.put(u, 0);
            board.add(u);
        }

        boolean addReferral(String referrer, String newUser) {
            if (!count.containsKey(referrer)) return false;
            if (parent.containsKey(newUser)) return false;
            if (referrer.equals(newUser)) return false;
            for (String a = referrer; a != null; a = parent.get(a))
                if (a.equals(newUser)) return false;             // cycle guard

            parent.put(newUser, referrer);
            addUser(newUser);

            for (String a = referrer; a != null; a = parent.get(a)) bump(a, 1);
            return true;
        }

        /** ⭐ remove BEFORE the score changes, re-insert AFTER. */
        private void bump(String user, int delta) {
            board.remove(user);                                  // uses the OLD score to locate it
            count.merge(user, delta, Integer::sum);
            board.add(user);                                     // re-sorted on the NEW score
        }

        List<String> topK(int k) {
            List<String> out = new ArrayList<>(k);
            for (String u : board) {
                if (out.size() == k) break;
                out.add(u);
            }
            return out;
        }

        int countOf(String u) { return count.getOrDefault(u, 0); }
    }

    /* ===================================================================
     * PART 3 — The scale problem, and honest mitigations.
     *
     * CHANGES FROM PART 2:
     *   - a depth cap on credited ancestors
     *
     * ⚠️ O(depth) per insert is the real weakness. Three answers, in the order
     * worth offering them:
     *
     *   1. CAP THE CREDITED DEPTH. Most real referral programs credit only 1-2
     *      levels, which makes insert O(1) and is a product decision rather than
     *      a technical compromise. Offer this FIRST — it is what a company
     *      actually does.
     *   2. If arbitrary depth must stay fast: Euler tour + BIT. Number the tree
     *      by DFS in/out times; "descendants of x" is a contiguous range, so a
     *      subtree count is a range sum, O(log n) per update AND query.
     *      ⚠️ Cost: the Euler numbering is invalidated by an insert, so this
     *      needs either a rebuild or a balanced-BST order-maintenance structure.
     *      Say that cost; do not present it as free.
     *   3. Accept amortised O(depth) and bound depth operationally — a referral
     *      tree in practice is shallow and wide, not a chain.
     * =================================================================== */
    static class DepthCapped {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> count = new HashMap<>();
        private final int maxDepth;

        DepthCapped(int maxDepth) { this.maxDepth = maxDepth; }

        void addUser(String u) { count.putIfAbsent(u, 0); }

        boolean addReferral(String referrer, String newUser) {
            if (!count.containsKey(referrer) || parent.containsKey(newUser)) return false;
            parent.put(newUser, referrer);
            count.putIfAbsent(newUser, 0);
            int levels = 0;
            for (String a = referrer; a != null && levels < maxDepth; a = parent.get(a), levels++)
                count.merge(a, 1, Integer::sum);
            return true;
        }
        int countOf(String u) { return count.getOrDefault(u, 0); }
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        System.out.println("=== the stated requirement: A->B->C credits BOTH A and B ===");
        IndexedBoard b = new IndexedBoard();
        b.addUser("A");
        b.addReferral("A", "B");
        b.addReferral("B", "C");
        System.out.println("  A=" + b.countOf("A") + " B=" + b.countOf("B") + " C=" + b.countOf("C"));
        System.out.println("  expect A=2 B=1 C=0 -> "
                + (b.countOf("A") == 2 && b.countOf("B") == 1 && b.countOf("C") == 0 ? "PASS" : "FAIL"));

        System.out.println("\n=== top 3 ===");
        IndexedBoard lb = new IndexedBoard();
        lb.addUser("root");
        for (String u : List.of("a", "b", "c", "d")) lb.addReferral("root", u);
        lb.addReferral("a", "a1");
        lb.addReferral("a", "a2");
        lb.addReferral("b", "b1");
        // root=7, a=2, b=1, others 0
        System.out.println("  top3 = " + lb.topK(3));
        System.out.println("  counts root=" + lb.countOf("root") + " a=" + lb.countOf("a")
                + " b=" + lb.countOf("b"));
        System.out.println("  expect [root, a, b] -> "
                + (lb.topK(3).equals(List.of("root", "a", "b")) ? "PASS" : "FAIL"));

        System.out.println("\n=== ties broken by id ASC ===");
        IndexedBoard t = new IndexedBoard();
        for (String u : List.of("zed", "amy", "bob")) t.addUser(u);
        System.out.println("  all zero -> " + t.topK(3) + "  expect [amy, bob, zed] -> "
                + (t.topK(3).equals(List.of("amy", "bob", "zed")) ? "PASS" : "FAIL"));

        System.out.println("\n=== baseline and indexed agree ===");
        Baseline base = new Baseline();
        IndexedBoard idx = new IndexedBoard();
        base.addUser("r"); idx.addUser("r");
        Random rnd = new Random(9);
        List<String> pool = new ArrayList<>(List.of("r"));
        for (int i = 0; i < 400; i++) {
            String referrer = pool.get(rnd.nextInt(pool.size()));
            String child = "u" + i;
            base.addReferral(referrer, child);
            idx.addReferral(referrer, child);
            pool.add(child);
        }
        System.out.println("  baseline top3 = " + base.topK(3));
        System.out.println("  indexed  top3 = " + idx.topK(3));
        System.out.println("  agree -> " + (base.topK(3).equals(idx.topK(3)) ? "PASS" : "FAIL"));

        System.out.println("\n=== guards ===");
        IndexedBoard g = new IndexedBoard();
        g.addUser("X");
        g.addReferral("X", "Y");
        System.out.println("  unknown referrer  -> " + g.addReferral("ghost", "Z") + " (expect false)");
        System.out.println("  second referrer   -> " + g.addReferral("X", "Y") + " (expect false)");
        System.out.println("  self referral     -> " + g.addReferral("X", "X") + " (expect false)");
        System.out.println("  cycle Y refers X  -> " + g.addReferral("Y", "X") + " (expect false)");

        System.out.println("\n=== ⭐ the O(depth) weakness: a deep CHAIN ===");
        final int CHAIN = 8_000;   // 20k takes ~57s; the shape is identical and this proves it
        IndexedBoard chain = new IndexedBoard();
        chain.addUser("n0");
        long t0 = System.currentTimeMillis();
        for (int i = 1; i <= CHAIN; i++) chain.addReferral("n" + (i - 1), "n" + i);
        long elapsed = System.currentTimeMillis() - t0;
        System.out.println("  " + CHAIN + " chained inserts: " + elapsed + " ms");
        System.out.println("  work is sum(1..n) TreeSet remove/reinsert pairs = ~"
                + (long) CHAIN * CHAIN / 2 / 1_000_000 + "M ops  <-- quadratic");
        System.out.println("  (20k of these takes ~57s on this machine — measured)");
        System.out.println("  n0 count = " + chain.countOf("n0") + " (expect " + CHAIN + ") -> "
                + (chain.countOf("n0") == CHAIN ? "PASS" : "FAIL"));

        System.out.println("\n=== part3: depth cap makes insert O(1) ===");
        DepthCapped cap = new DepthCapped(2);
        cap.addUser("m0");
        long t1 = System.currentTimeMillis();
        for (int i = 1; i <= CHAIN; i++) cap.addReferral("m" + (i - 1), "m" + i);
        System.out.println("  same " + CHAIN + " chain, credit capped at 2 levels: "
                + (System.currentTimeMillis() - t1) + " ms  <-- linear");
        // m0 is credited twice: once when m1 joins (1 level up), once when m2 joins (2 levels up).
        // After that it is out of range. So the cap yields exactly maxDepth credits for the root.
        System.out.println("  m0 count = " + cap.countOf("m0") + " (expect 2 = maxDepth) -> "
                + (cap.countOf("m0") == 2 ? "PASS" : "FAIL"));
        System.out.println("  ⭐ this is the answer a real referral program ships.");
    }
}
