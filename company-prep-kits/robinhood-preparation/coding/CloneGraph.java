import java.util.*;

/**
 * Robinhood — "Clone a Graph"   (LeetCode 133)
 * Source: 1point3acres 9b3c28be-4683-47bc-97a0-ed6cf56e5398
 *
 * Deep-copy a graph. Each node has a unique `val` and a list of neighbours.
 * Constraints: node values 1..100, neighbours per node 0..100.
 * Example input: adjList = [[2,4],[1,3],[2,4],[1,3]]   (1-indexed, undirected)
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE ONE THING: put the clone in the map BEFORE recursing into neighbours.
 * The graph has cycles (any undirected edge is a 2-cycle), so if you recurse
 * first you never terminate. That single line ordering IS the test.
 *
 * The map does double duty: it is the visited-set AND the original->copy
 * mapping. Recognising that is what makes the solution short.
 * ---------------------------------------------------------------------------
 */
public class CloneGraph {

    static class Node {
        int val;
        List<Node> neighbors = new ArrayList<>();
        Node(int v) { val = v; }
        Node(int v, List<Node> n) { val = v; neighbors = n; }
    }

    /* ===================================================================
     * PART 1 — ⭐ DFS with a map. THE ANSWER.
     *
     * seen.put(n, copy) happens BEFORE the neighbour loop. Move it after and
     * this infinite-loops on the very first example.
     * =================================================================== */
    static Node part1(Node node) {
        return dfs(node, new HashMap<>());
    }
    private static Node dfs(Node n, Map<Node, Node> seen) {
        if (n == null) return null;
        Node existing = seen.get(n);
        if (existing != null) return existing;

        Node copy = new Node(n.val);
        seen.put(n, copy);                       // ⭐ BEFORE recursing. This is the whole problem.
        for (Node nb : n.neighbors) copy.neighbors.add(dfs(nb, seen));
        return copy;
    }

    /* ===================================================================
     * PART 2 — Iterative BFS.
     *
     * CHANGES FROM PART 1:
     *   - explicit queue instead of the call stack
     *   - nodes are created on first sight, wired up when dequeued
     *
     * Offer this when asked about depth. The stated bound (100 nodes) makes
     * recursion perfectly safe here, so say that — do not pretend the iterative
     * version is required. It matters on ReferralCount.java, not here.
     * =================================================================== */
    static Node part2Bfs(Node node) {
        if (node == null) return null;
        Map<Node, Node> seen = new HashMap<>();
        seen.put(node, new Node(node.val));

        Deque<Node> q = new ArrayDeque<>();
        q.add(node);
        while (!q.isEmpty()) {
            Node u = q.poll();
            for (Node nb : u.neighbors) {
                if (!seen.containsKey(nb)) {
                    seen.put(nb, new Node(nb.val));
                    q.add(nb);
                }
                seen.get(u).neighbors.add(seen.get(nb));
            }
        }
        return seen.get(node);
    }

    /* ===================================================================
     * PART 3 — Build from / convert to the stated adjList format.
     *
     * CHANGES FROM PART 2:
     *   - adjacency list is 1-INDEXED in the LeetCode format: adjList[0] holds
     *     the neighbours of the node with val 1. Off-by-one here is the second
     *     most common error on this problem.
     * =================================================================== */
    static Node part3FromAdjList(int[][] adjList) {
        if (adjList == null || adjList.length == 0) return null;
        int n = adjList.length;
        Node[] nodes = new Node[n + 1];                 // 1-indexed
        for (int i = 1; i <= n; i++) nodes[i] = new Node(i);
        for (int i = 1; i <= n; i++)
            for (int nb : adjList[i - 1]) nodes[i].neighbors.add(nodes[nb]);
        return nodes[1];
    }

    /** Serialise back, so we can compare structures. */
    static int[][] part3ToAdjList(Node start) {
        if (start == null) return new int[0][0];
        Map<Integer, List<Integer>> m = new TreeMap<>();
        Set<Node> seen = new HashSet<>();
        Deque<Node> q = new ArrayDeque<>(List.of(start));
        seen.add(start);
        while (!q.isEmpty()) {
            Node u = q.poll();
            List<Integer> nbs = new ArrayList<>();
            for (Node nb : u.neighbors) {
                nbs.add(nb.val);
                if (seen.add(nb)) q.add(nb);
            }
            m.put(u.val, nbs);
        }
        int[][] out = new int[m.size()][];
        int i = 0;
        for (List<Integer> l : m.values()) {
            out[i] = new int[l.size()];
            for (int j = 0; j < l.size(); j++) out[i][j] = l.get(j);
            i++;
        }
        return out;
    }

    /* ===================================================================
     * PART 4 — The verification that actually proves "deep".
     *
     * CHANGES FROM PART 3:
     *   - isDeepCopy() walks both graphs in lockstep and asserts (a) the shapes
     *     match and (b) NO node object is shared between them
     *
     * (b) is the part people forget to check. A "clone" that returns the same
     * node objects passes a naive value comparison and is not a copy at all.
     * =================================================================== */
    static boolean part4IsDeepCopy(Node a, Node b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        Map<Node, Node> pair = new IdentityHashMap<>();
        Deque<Node[]> q = new ArrayDeque<>();
        q.add(new Node[]{a, b});
        pair.put(a, b);
        while (!q.isEmpty()) {
            Node[] p = q.poll();
            Node x = p[0], y = p[1];
            if (x == y) return false;                        // ⭐ shared object: not a copy
            if (x.val != y.val) return false;
            if (x.neighbors.size() != y.neighbors.size()) return false;
            for (int i = 0; i < x.neighbors.size(); i++) {
                Node xn = x.neighbors.get(i), yn = y.neighbors.get(i);
                Node known = pair.get(xn);
                if (known == null) { pair.put(xn, yn); q.add(new Node[]{xn, yn}); }
                else if (known != yn) return false;          // structure diverges
            }
        }
        return true;
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        int[][] adj = {{2,4},{1,3},{2,4},{1,3}};
        Node original = part3FromAdjList(adj);

        Node c1 = part1(original);
        Node c2 = part2Bfs(original);

        System.out.println("original  = " + Arrays.deepToString(part3ToAdjList(original)));
        System.out.println("dfs clone = " + Arrays.deepToString(part3ToAdjList(c1)));
        System.out.println("bfs clone = " + Arrays.deepToString(part3ToAdjList(c2)));
        System.out.println("structure preserved (dfs)? "
                + (Arrays.deepEquals(part3ToAdjList(original), part3ToAdjList(c1)) ? "PASS" : "FAIL"));
        System.out.println("structure preserved (bfs)? "
                + (Arrays.deepEquals(part3ToAdjList(original), part3ToAdjList(c2)) ? "PASS" : "FAIL"));

        System.out.println("\n⭐ genuinely DEEP (no shared node objects)?");
        System.out.println("  dfs -> " + (part4IsDeepCopy(original, c1) ? "PASS" : "FAIL"));
        System.out.println("  bfs -> " + (part4IsDeepCopy(original, c2) ? "PASS" : "FAIL"));
        System.out.println("  same object returned -> " + (part4IsDeepCopy(original, original)
                ? "FAIL (should be rejected)" : "PASS — the check actually detects sharing"));

        System.out.println("\n--- edge cases ---");
        System.out.println("null        -> " + part1(null) + "  " + (part1(null) == null ? "PASS" : "FAIL"));
        Node solo = new Node(1);
        Node soloCopy = part1(solo);
        System.out.println("single node -> val=" + soloCopy.val + " nbrs=" + soloCopy.neighbors.size()
                + "  " + (soloCopy != solo && soloCopy.neighbors.isEmpty() ? "PASS" : "FAIL"));

        // self-loop: a node that is its own neighbour. Terminates only because
        // put() precedes the recursion.
        Node self = new Node(1);
        self.neighbors.add(self);
        Node selfCopy = part1(self);
        System.out.println("self-loop   -> copy.nbr[0] == copy? "
                + (selfCopy.neighbors.get(0) == selfCopy ? "PASS" : "FAIL")
                + "   and != original? " + (selfCopy != self ? "PASS" : "FAIL"));

        // two-node cycle, the minimal infinite-recursion trap
        Node a = new Node(1), b = new Node(2);
        a.neighbors.add(b); b.neighbors.add(a);
        Node ac = part1(a);
        System.out.println("2-cycle     -> terminates, and copy.b.a == copy.a? "
                + (ac.neighbors.get(0).neighbors.get(0) == ac ? "PASS" : "FAIL"));

        System.out.println("\n--- a bigger dense graph (100 nodes, 100 nbrs each) ---");
        int n = 100;
        int[][] dense = new int[n][];
        for (int i = 0; i < n; i++) {
            dense[i] = new int[n];
            for (int j = 0; j < n; j++) dense[i][j] = j + 1;   // everyone adjacent to everyone
        }
        Node d = part3FromAdjList(dense);
        Node dc = part1(d);
        System.out.println("  deep copy of K100 (with self-loops) -> "
                + (part4IsDeepCopy(d, dc) ? "PASS" : "FAIL"));
    }
}
