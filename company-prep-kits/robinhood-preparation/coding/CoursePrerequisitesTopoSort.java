import java.util.*;

/**
 * Robinhood — "Course Prerequisites Topological Sort"
 * Source: 1point3acres 6a53ed24-90e9-5f8e-8a45-2cee62777da0
 *
 * n courses labelled 0..n-1, m prerequisite pairs. Return the LEXICOGRAPHICALLY
 * SMALLEST valid ordering, or -1 if a cycle exists.
 * Constraints: n <= 1e5, m <= 2e5.
 *
 * Given example: 4 courses, 4 prereqs (1<-0, 2<-0, 3<-1, 3<-2)  =>  "0 1 2 3"
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE ONE THING: "lexicographically smallest" means a MIN-HEAP, not a queue.
 * Plain Kahn's with an ArrayDeque gives *a* valid order. It does not give the
 * smallest one. Using a queue here reads as not having read the problem, and it
 * is the single most likely way to fail this question.
 *
 * ⚠️ ASK ABOUT THE PAIR CONVENTION. "Two integers representing a dependency" is
 * ambiguous and the two readings give different answers. Both are supported
 * below via PairOrder. The example (1<-0 etc.) is [course, prereq], which is
 * also the LeetCode 210 convention — but confirm rather than assume.
 * ---------------------------------------------------------------------------
 */
public class CoursePrerequisitesTopoSort {

    enum PairOrder {
        COURSE_THEN_PREREQ,   // [a, b] = "a depends on b"  => edge b -> a   (the example)
        PREREQ_THEN_COURSE    // [a, b] = "a comes before b" => edge a -> b
    }

    /* ===================================================================
     * PART 1 — Any valid order. Kahn's with a plain queue.
     *
     * Correct topological sort, WRONG for this problem. Write it, then say out
     * loud why it is not the answer: an ArrayDeque pops in insertion order, so
     * the result depends on input order rather than on value.
     * =================================================================== */
    static int[] part1AnyOrder(int n, int[][] pairs, PairOrder conv) {
        List<List<Integer>> adj = buildAdj(n, pairs, conv);
        int[] indeg = indegrees(n, adj);

        Deque<Integer> ready = new ArrayDeque<>();
        for (int v = 0; v < n; v++) if (indeg[v] == 0) ready.add(v);

        int[] order = new int[n];
        int k = 0;
        while (!ready.isEmpty()) {
            int u = ready.poll();
            order[k++] = u;
            for (int v : adj.get(u)) if (--indeg[v] == 0) ready.add(v);
        }
        return k == n ? order : null;   // null == cycle
    }

    /* ===================================================================
     * PART 2 — ⭐ Lexicographically smallest. THE ANSWER.
     *
     * CHANGES FROM PART 1:
     *   - ArrayDeque  ->  PriorityQueue<Integer>
     * That is the entire diff. One line. O((V+E) log V) instead of O(V+E).
     *
     * Why it works: at every step the set of currently-schedulable courses is
     * exactly the indegree-0 set. Greedily taking the smallest of those is
     * optimal because choosing any larger one would put a bigger number earlier,
     * and the smallest is always still schedulable later if we skip it now.
     * =================================================================== */
    static int[] part2Lexicographic(int n, int[][] pairs, PairOrder conv) {
        List<List<Integer>> adj = buildAdj(n, pairs, conv);
        int[] indeg = indegrees(n, adj);

        PriorityQueue<Integer> ready = new PriorityQueue<>();      // <-- THE ONE CHANGE
        for (int v = 0; v < n; v++) if (indeg[v] == 0) ready.add(v);

        int[] order = new int[n];
        int k = 0;
        while (!ready.isEmpty()) {
            int u = ready.poll();
            order[k++] = u;
            for (int v : adj.get(u)) if (--indeg[v] == 0) ready.add(v);
        }
        return k == n ? order : null;
    }

    /* ===================================================================
     * PART 3 — The -1 case, and how to detect it properly.
     *
     * CHANGES FROM PART 2:
     *   - formats the required output: the order, or the literal "-1"
     *
     * ⭐ Cycle detection is FREE: compare the number of emitted nodes to n. Do
     * NOT write a separate colour-marking DFS for this — it is redundant work
     * and it is a common over-engineering tell.
     * =================================================================== */
    static String part3Format(int n, int[][] pairs, PairOrder conv) {
        int[] order = part2Lexicographic(n, pairs, conv);
        if (order == null) return "-1";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(' ');
            sb.append(order[i]);
        }
        return sb.toString();
    }

    /* ===================================================================
     * PART 4 — Scale. Arrays instead of collections.
     *
     * CHANGES FROM PART 3:
     *   - CSR-style adjacency in flat int[] arrays (head/next/to) instead of
     *     List<List<Integer>>, which at n=1e5 / m=2e5 avoids ~300k boxed
     *     Integers and the pointer chasing that comes with them
     *   - an int-array min-heap instead of PriorityQueue<Integer>, removing the
     *     remaining boxing on the hot path
     *   - duplicate edges are tolerated (they inflate indegree consistently on
     *     both sides, so the result stays correct)
     *
     * This is the version to write if they say "n is 1e5, make it fast".
     * Otherwise Part 2 is the right amount of code.
     * =================================================================== */
    static int[] part4Fast(int n, int[][] pairs, PairOrder conv) {
        int m = pairs.length;
        int[] head = new int[n];
        int[] next = new int[m];
        int[] to   = new int[m];
        int[] indeg = new int[n];
        Arrays.fill(head, -1);

        for (int i = 0; i < m; i++) {
            int a = pairs[i][0], b = pairs[i][1];
            int from, dest;
            if (conv == PairOrder.COURSE_THEN_PREREQ) { from = b; dest = a; }
            else                                      { from = a; dest = b; }
            if (from < 0 || from >= n || dest < 0 || dest >= n) continue;   // out of range: ignore
            to[i] = dest;
            next[i] = head[from];
            head[from] = i;
            indeg[dest]++;
        }

        int[] heap = new int[n];
        int hs = 0;
        for (int v = 0; v < n; v++) if (indeg[v] == 0) heap[hs++] = v;
        // heapify
        for (int i = hs / 2 - 1; i >= 0; i--) siftDown(heap, hs, i);

        int[] order = new int[n];
        int k = 0;
        while (hs > 0) {
            int u = heap[0];
            heap[0] = heap[--hs];
            siftDown(heap, hs, 0);
            order[k++] = u;
            for (int e = head[u]; e != -1; e = next[e]) {
                if (--indeg[to[e]] == 0) {
                    heap[hs++] = to[e];
                    siftUp(heap, hs - 1);
                }
            }
        }
        return k == n ? order : null;
    }

    private static void siftDown(int[] h, int size, int i) {
        while (true) {
            int l = 2 * i + 1, r = l + 1, s = i;
            if (l < size && h[l] < h[s]) s = l;
            if (r < size && h[r] < h[s]) s = r;
            if (s == i) return;
            int t = h[i]; h[i] = h[s]; h[s] = t;
            i = s;
        }
    }
    private static void siftUp(int[] h, int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            if (h[p] <= h[i]) return;
            int t = h[i]; h[i] = h[p]; h[p] = t;
            i = p;
        }
    }

    /* ---------- shared helpers ---------- */
    private static List<List<Integer>> buildAdj(int n, int[][] pairs, PairOrder conv) {
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] p : pairs) {
            int from, to;
            if (conv == PairOrder.COURSE_THEN_PREREQ) { from = p[1]; to = p[0]; }
            else                                      { from = p[0]; to = p[1]; }
            if (from < 0 || from >= n || to < 0 || to >= n) continue;
            adj.get(from).add(to);
        }
        return adj;
    }
    private static int[] indegrees(int n, List<List<Integer>> adj) {
        int[] indeg = new int[n];
        for (List<Integer> l : adj) for (int v : l) indeg[v]++;
        return indeg;
    }

    /** Reads the stated input format: "n m" then m lines of "a b". */
    static String solveFromInput(Scanner in, PairOrder conv) {
        int n = in.nextInt(), m = in.nextInt();
        int[][ ] pairs = new int[m][2];
        for (int i = 0; i < m; i++) { pairs[i][0] = in.nextInt(); pairs[i][1] = in.nextInt(); }
        return part3Format(n, pairs, conv);
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        // the stated example: 1<-0, 2<-0, 3<-1, 3<-2  (course, prereq)
        int[][] ex = {{1,0},{2,0},{3,1},{3,2}};
        System.out.println("part2 lexicographic = " + part3Format(4, ex, PairOrder.COURSE_THEN_PREREQ)
                + "  -> " + (part3Format(4, ex, PairOrder.COURSE_THEN_PREREQ).equals("0 1 2 3") ? "PASS" : "FAIL"));

        // ⭐ THE CASE THAT SEPARATES A HEAP FROM A QUEUE.
        // n=3, single edge: course 1 depends on prereq 0. Initially free: {0, 2}.
        //   QUEUE: [0,2] -> emit 0, 1 becomes free -> [2,1] -> emit 2, emit 1  =>  0 2 1
        //   HEAP : {0,2} -> emit 0, 1 becomes free -> {1,2} -> emit 1, emit 2  =>  0 1 2
        // Both are valid topological orders. Only one is lexicographically smallest.
        int[][] tricky = {{1, 0}};
        String lex = part3Format(3, tricky, PairOrder.COURSE_THEN_PREREQ);
        int[] anyOrder = part1AnyOrder(3, tricky, PairOrder.COURSE_THEN_PREREQ);
        System.out.println("heap  (part2) = " + lex + "  (expect 0 1 2) -> "
                + (lex.equals("0 1 2") ? "PASS" : "FAIL"));
        System.out.println("queue (part1) = " + Arrays.toString(anyOrder)
                + "  <-- valid, but NOT smallest. ⭐ This is why the heap matters.");
        System.out.println("  they differ? " + (!Arrays.toString(anyOrder).equals("[0, 1, 2]")
                ? "PASS — demonstrates the trap" : "FAIL — pick a sharper case"));

        // cycle
        System.out.println("cycle -> " + part3Format(2, new int[][]{{0,1},{1,0}}, PairOrder.COURSE_THEN_PREREQ)
                + "  -> " + (part3Format(2, new int[][]{{0,1},{1,0}}, PairOrder.COURSE_THEN_PREREQ).equals("-1") ? "PASS" : "FAIL"));

        // no edges at all -> 0..n-1
        System.out.println("m=0  -> " + part3Format(5, new int[0][0], PairOrder.COURSE_THEN_PREREQ)
                + "  -> " + (part3Format(5, new int[0][0], PairOrder.COURSE_THEN_PREREQ).equals("0 1 2 3 4") ? "PASS" : "FAIL"));

        // n=1
        System.out.println("n=1  -> " + part3Format(1, new int[0][0], PairOrder.COURSE_THEN_PREREQ));

        // self-loop is a cycle
        System.out.println("self-loop -> " + part3Format(2, new int[][]{{0,0}}, PairOrder.COURSE_THEN_PREREQ)
                + "  (expect -1)");

        // duplicate edges must not break it
        System.out.println("dup edges -> " + part3Format(3, new int[][]{{1,0},{1,0},{2,1}}, PairOrder.COURSE_THEN_PREREQ)
                + "  (expect 0 1 2)");

        // part4 agrees with part2, and handles the stated scale
        System.out.println("\n--- scale: n=100000, m=200000 ---");
        int n = 100_000, m = 200_000;
        int[][] big = new int[m][2];
        Random rnd = new Random(42);
        for (int i = 0; i < m; i++) {          // only edges low->high, so guaranteed acyclic
            int a = rnd.nextInt(n - 1);
            int b = a + 1 + rnd.nextInt(Math.min(50, n - a - 1));
            big[i][0] = b; big[i][1] = a;      // course b depends on prereq a
        }
        long t0 = System.currentTimeMillis();
        int[] slow = part2Lexicographic(n, big, PairOrder.COURSE_THEN_PREREQ);
        long t1 = System.currentTimeMillis();
        int[] fast = part4Fast(n, big, PairOrder.COURSE_THEN_PREREQ);
        long t2 = System.currentTimeMillis();
        System.out.println("  part2 (collections) " + (t1 - t0) + " ms");
        System.out.println("  part4 (int arrays)  " + (t2 - t1) + " ms");
        System.out.println("  identical output?   " + (Arrays.equals(slow, fast) ? "PASS" : "FAIL"));
        System.out.println("  first 10 = " + Arrays.toString(Arrays.copyOf(fast, 10)));
    }
}
