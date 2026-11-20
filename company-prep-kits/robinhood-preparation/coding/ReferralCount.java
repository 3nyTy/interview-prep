import java.util.*;

/**
 * Robinhood — "Referral Count (Subtree Descendant Count in a Referral Tree)"
 * Source: 1point3acres 3a296606-9af1-431e-be45-5c7d1a2532f1
 *
 * n nodes 0..n-1, m edges "p c" (p referred c). Output n integers where position
 * i is node i's descendant count (subtree size minus 1).
 * Constraints: n <= 2e5, m <= n-1, each node has AT MOST ONE parent, graph may be
 * disconnected (a FOREST), no self-loops.
 *
 * Given example: 6 nodes, edges 0->1, 0->2, 1->3, 1->4, 2->5  =>  "5 2 1 0 0 0"
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE TWO THINGS
 * 1. n = 2e5 and the tree can be a PATH. Recursive DFS overflows the stack —
 *    reliably, at roughly 10-20k depth on a default JVM. part1 demonstrates the
 *    crash so you never write it under pressure.
 * 2. "At most one parent" is a gift: you do not need an adjacency list at all.
 *    A single parent[] array plus a reverse-topological sweep solves it in O(n)
 *    with no graph structure and no recursion. That is the answer.
 *
 * ⚠️ It is a FOREST, not a tree. Multiple roots. Do not assume node 0 is a root.
 * ⚠️ The answer is subtree size MINUS ONE — a node is not its own descendant.
 * ---------------------------------------------------------------------------
 */
public class ReferralCount {

    /* ===================================================================
     * PART 1 — The recursive version, and why it is disqualified.
     *
     * Correct on small inputs. Blows the stack at the stated constraints.
     * Keep it to demonstrate, then delete it.
     * =================================================================== */
    static int[] part1Recursive(int n, int[][] edges) {
        List<List<Integer>> kids = new ArrayList<>(n);
        for (int i = 0; i < n; i++) kids.add(new ArrayList<>());
        boolean[] hasParent = new boolean[n];
        for (int[] e : edges) { kids.get(e[0]).add(e[1]); hasParent[e[1]] = true; }

        int[] size = new int[n];
        for (int r = 0; r < n; r++) if (!hasParent[r]) dfs(r, kids, size);

        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = size[i] - 1;
        return out;
    }
    private static int dfs(int u, List<List<Integer>> kids, int[] size) {
        int s = 1;
        for (int v : kids.get(u)) s += dfs(v, kids, size);
        size[u] = s;
        return s;
    }

    /* ===================================================================
     * PART 2 — ⭐ THE ANSWER. Parent array + reverse topological sweep. O(n).
     *
     * CHANGES FROM PART 1:
     *   - no adjacency list, no recursion
     *   - one parent[] array (valid because each node has at most one parent)
     *   - process nodes in an order where every child precedes its parent, then
     *     add each node's size into its parent
     *
     * Getting that order without recursion: repeatedly peel nodes whose
     * remaining child-count is zero — Kahn's algorithm on the child counts. Same
     * template as the load-factor problems, accumulating subtree size upward.
     * =================================================================== */
    static int[] part2(int n, int[][] edges) {
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] childCount = new int[n];

        for (int[] e : edges) {
            int p = e[0], c = e[1];
            if (p < 0 || p >= n || c < 0 || c >= n) continue;   // out of range
            if (p == c) continue;                               // stated: no self-loops
            if (parent[c] != -1) continue;                      // stated: at most one parent
            parent[c] = p;
            childCount[p]++;
        }

        int[] size = new int[n];
        Arrays.fill(size, 1);                                   // every node counts itself

        // leaves first
        int[] stack = new int[n];
        int sp = 0;
        for (int v = 0; v < n; v++) if (childCount[v] == 0) stack[sp++] = v;

        int processed = 0;
        while (sp > 0) {
            int u = stack[--sp];
            processed++;
            int p = parent[u];
            if (p != -1) {
                size[p] += size[u];                             // <-- accumulate UPWARD
                if (--childCount[p] == 0) stack[sp++] = p;
            }
        }
        // A forest has no cycles by construction here (one parent + peeling), but
        // if the input violated its own contract we would notice:
        if (processed != n) throw new IllegalArgumentException("input contains a cycle");

        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = size[i] - 1;       // ⭐ minus one
        return out;
    }

    /* ===================================================================
     * PART 3 — Iterative DFS, for when they ask for a general tree.
     *
     * CHANGES FROM PART 2:
     *   - uses an explicit stack with a post-order visit, so it still works if
     *     the "at most one parent" guarantee is dropped and a node can have
     *     several parents recorded (it then counts a DAG's subtree sizes, which
     *     is a DIFFERENT question — see SystemDependencyLoadFactor.java)
     *
     * Offer this only if they relax the single-parent rule, and SAY that the
     * question changes when they do: with multiple parents, "descendants"
     * de-duplicates and simple addition double-counts.
     * =================================================================== */
    static int[] part3IterativeDfs(int n, int[][] edges) {
        List<List<Integer>> kids = new ArrayList<>(n);
        for (int i = 0; i < n; i++) kids.add(new ArrayList<>());
        boolean[] hasParent = new boolean[n];
        for (int[] e : edges) {
            if (e[0] == e[1]) continue;
            kids.get(e[0]).add(e[1]);
            hasParent[e[1]] = true;
        }
        int[] size = new int[n];
        Arrays.fill(size, 1);

        Deque<int[]> st = new ArrayDeque<>();                   // {node, childIndex}
        boolean[] seen = new boolean[n];
        for (int r = 0; r < n; r++) {
            if (hasParent[r] || seen[r]) continue;
            st.push(new int[]{r, 0});
            seen[r] = true;
            while (!st.isEmpty()) {
                int[] top = st.peek();
                List<Integer> ch = kids.get(top[0]);
                if (top[1] < ch.size()) {
                    int c = ch.get(top[1]++);
                    if (!seen[c]) { seen[c] = true; st.push(new int[]{c, 0}); }
                } else {
                    st.pop();
                    if (!st.isEmpty()) size[st.peek()[0]] += size[top[0]];
                }
            }
        }
        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = size[i] - 1;
        return out;
    }

    /* ===================================================================
     * PART 4 — I/O in the stated format, plus the follow-ups.
     *
     * CHANGES FROM PART 3:
     *   - reads "n m" then m lines of "p c", writes n space-separated ints
     *   - adds the two natural follow-ups: who has the most descendants, and
     *     the depth of each node (same sweep, opposite direction)
     * =================================================================== */
    static String part4Format(int n, int[][] edges) {
        int[] counts = part2(n, edges);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) { if (i > 0) sb.append(' '); sb.append(counts[i]); }
        return sb.toString();
    }

    /** Follow-up: top referrer. Ties broken by smallest id. */
    static int part4TopReferrer(int n, int[][] edges) {
        int[] c = part2(n, edges);
        int best = 0;
        for (int i = 1; i < n; i++) if (c[i] > c[best]) best = i;
        return best;
    }

    /** Follow-up: depth of each node (roots at 0). Downward sweep, no recursion. */
    static int[] part4Depths(int n, int[][] edges) {
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        List<List<Integer>> kids = new ArrayList<>(n);
        for (int i = 0; i < n; i++) kids.add(new ArrayList<>());
        for (int[] e : edges) {
            if (e[0] == e[1] || parent[e[1]] != -1) continue;
            parent[e[1]] = e[0];
            kids.get(e[0]).add(e[1]);
        }
        int[] depth = new int[n];
        Deque<Integer> q = new ArrayDeque<>();
        for (int v = 0; v < n; v++) if (parent[v] == -1) { depth[v] = 0; q.add(v); }
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int c : kids.get(u)) { depth[c] = depth[u] + 1; q.add(c); }
        }
        return depth;
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        int[][] ex = {{0,1},{0,2},{1,3},{1,4},{2,5}};
        String got = part4Format(6, ex);
        System.out.println("part2  = " + got + "   (expect 5 2 1 0 0 0) -> "
                + (got.equals("5 2 1 0 0 0") ? "PASS" : "FAIL"));
        System.out.println("part1  = " + Arrays.toString(part1Recursive(6, ex)));
        System.out.println("part3  = " + Arrays.toString(part3IterativeDfs(6, ex)));
        System.out.println("depths = " + Arrays.toString(part4Depths(6, ex)) + " (expect [0,1,1,2,2,2])");
        System.out.println("top    = " + part4TopReferrer(6, ex) + " (expect 0)");

        // FOREST: two disjoint trees, and node 0 is NOT the only root
        int[][] forest = {{1,0},{1,2},{4,3}};
        System.out.println("\nforest = " + part4Format(5, forest) + "  (expect 0 2 0 0 1)");
        System.out.println("  roots are 1 and 4, not 0 -> "
                + (part4Format(5, forest).equals("0 2 0 0 1") ? "PASS" : "FAIL"));

        // m = 0: every node isolated
        System.out.println("m=0    = " + part4Format(3, new int[0][0]) + "  (expect 0 0 0)");
        // n = 1
        System.out.println("n=1    = " + part4Format(1, new int[0][0]) + "  (expect 0)");

        // ⭐ THE STACK TEST: a 200,000-node PATH
        int n = 200_000;
        int[][] path = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) { path[i][0] = i; path[i][1] = i + 1; }

        long t0 = System.currentTimeMillis();
        int[] iter = part2(n, path);
        System.out.println("\npart2 on a 200k PATH: root has " + iter[0] + " descendants ("
                + (System.currentTimeMillis() - t0) + " ms) -> "
                + (iter[0] == n - 1 && iter[n - 1] == 0 ? "PASS" : "FAIL"));

        System.out.print("part1 (recursive) on the same input: ");
        try {
            part1Recursive(n, path);
            System.out.println("survived (JVM stack was unusually large)");
        } catch (StackOverflowError e) {
            System.out.println("⛔ StackOverflowError — exactly as predicted. Never write this.");
        }
    }
}
