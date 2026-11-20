import java.util.*;

/**
 * Robinhood — "Count Trigger Events in Directed Acyclic Graph"
 * Source: 1point3acres 1c708aa4-2fbb-4cc4-ae73-3c423e4e9490
 *
 * When an entry node is triggered, the trigger propagates to its children.
 * Count how many times each node is triggered.
 *
 * Given example: A->B, B->C, B->D, C->D, D->E, D->F, E->F, entry = A
 *                => A:1, B:1, C:1, D:2, E:2, F:4
 *   D is triggered via B and via C           => 2
 *   F is triggered via D (2) and via E (2)   => 4
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE CONTRAST — this is the sibling of SystemDependencyLoadFactor.java and
 * the pair is the trap.
 *
 *   trigger count  = NUMBER OF DISTINCT PATHS from the entry   -> SUM, multiplies
 *   load factor    = NUMBER OF NODES that can reach x          -> UNION, de-dupes
 *
 * Run both on this very graph and F is 4 by paths but only 4 ancestors too —
 * so use D: 2 paths, 2 ancestors. Take the diamond A->B,A->C,B->D,C->D:
 *   paths to D      = 2   (A-B-D, A-C-D)
 *   ancestors of D  = 3   (A, B, C)
 * Different question, different answer, identical-looking sweep. part4() below
 * prints them side by side so you can say this cleanly under pressure.
 *
 * ⚠️ Path counts grow EXPONENTIALLY with depth — a chain of k diamonds gives
 * 2^k paths. Use long, and ask whether they want a modulus. 63 diamonds
 * overflows a long.
 * ---------------------------------------------------------------------------
 */
public class CountTriggerEvents {

    /* ===================================================================
     * PART 1 — Single entry point. The core sweep.
     *
     * trigger[entry] = 1, then in topological order push counts forward:
     *     for each edge u -> v:   trigger[v] += trigger[u]
     *
     * Nodes unreachable from the entry keep 0, which falls out for free
     * because they simply never receive an addition.
     *
     * NOTE ON THE TOPO ORDER: we must sweep the WHOLE graph's topological
     * order, not just the reachable part, otherwise indegree never drains for
     * nodes that also have unreachable parents and we would stall. Counting
     * still comes out right because unreachable nodes contribute 0.
     * =================================================================== */
    static Map<String, Long> part1(List<String> nodes, List<String[]> edges, String entry) {
        return propagate(nodes, edges, List.of(entry), 0L);
    }

    /* ===================================================================
     * PART 2 — Multiple entry points.
     *
     * CHANGES FROM PART 1:
     *   - seed every entry with 1 instead of a single node
     *   - nothing else changes; the sweep is linear so multiple sources are
     *     free. (Contrast with the reachability problem, where multiple
     *     sources do NOT collapse like this.)
     *   - a node that is itself an entry AND reachable from another entry
     *     accumulates both, which is the correct reading of "triggered".
     * =================================================================== */
    static Map<String, Long> part2(List<String> nodes, List<String[]> edges, List<String> entries) {
        return propagate(nodes, edges, entries, 0L);
    }

    /* ===================================================================
     * PART 3 — Overflow. The follow-up that actually bites.
     *
     * CHANGES FROM PART 2:
     *   - a `mod` parameter. mod == 0 means "exact, use long".
     *   - when mod > 0 every addition is taken modulo mod.
     *   - detectOverflow() reports whether the exact count would have
     *     exceeded Long.MAX_VALUE, using Math.addExact and catching.
     *
     * Why this matters: 60 chained diamonds is a 121-node graph and 2^60
     * paths. A silent long overflow gives a NEGATIVE trigger count, which is
     * the kind of bug that passes small tests and is wrong in production.
     * =================================================================== */
    static Map<String, Long> part3(List<String> nodes, List<String[]> edges,
                                   List<String> entries, long mod) {
        return propagate(nodes, edges, entries, mod);
    }

    static boolean part3Overflows(List<String> nodes, List<String[]> edges, List<String> entries) {
        try {
            propagateExact(nodes, edges, entries);
            return false;
        } catch (ArithmeticException e) {
            return true;
        }
    }

    /* ---------- shared engine ---------- */
    private static Map<String, Long> propagate(List<String> nodes, List<String[]> edges,
                                               List<String> entries, long mod) {
        if (mod < 0) throw new IllegalArgumentException("mod must be >= 0 (0 means exact mode)");
        for (String s : entries)                       // an unknown entry is bad input, not "no events"
            if (!nodes.contains(s)) throw new IllegalArgumentException("unknown entry node: " + s);
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indeg = new HashMap<>();
        for (String n : nodes) { adj.put(n, new ArrayList<>()); indeg.put(n, 0); }
        for (String[] e : edges) {
            if (!adj.containsKey(e[0]) || !adj.containsKey(e[1])) continue;
            adj.get(e[0]).add(e[1]);
            indeg.merge(e[1], 1, Integer::sum);
        }

        Map<String, Long> trig = new HashMap<>();
        for (String n : nodes) trig.put(n, 0L);
        for (String s : entries) if (trig.containsKey(s)) trig.merge(s, 1L, Long::sum);

        Deque<String> ready = new ArrayDeque<>();
        for (String n : nodes) if (indeg.get(n) == 0) ready.add(n);

        int processed = 0;
        while (!ready.isEmpty()) {
            String u = ready.poll();
            processed++;
            long cu = trig.get(u);
            for (String v : adj.get(u)) {
                if (cu != 0) {
                    // addExact, NOT +. In exact mode (mod <= 0) a plain + wraps silently, and
                    // 2^70 wraps to exactly 0 — the most plausible-looking wrong answer there is.
                    // Fixed after codex debate 2026-08-14; see DEBATE-FINDINGS.md finding 1.
                    long nv = Math.addExact(trig.get(v), cu);   // <-- SUM: the accumulator
                    if (mod > 0) nv %= mod;
                    trig.put(v, nv);
                }
                if (indeg.merge(v, -1, Integer::sum) == 0) ready.add(v);
            }
        }
        if (processed != nodes.size()) throw new IllegalArgumentException("cycle detected — not a DAG");
        return new TreeMap<>(trig);
    }

    private static void propagateExact(List<String> nodes, List<String[]> edges, List<String> entries) {
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indeg = new HashMap<>();
        for (String n : nodes) { adj.put(n, new ArrayList<>()); indeg.put(n, 0); }
        for (String[] e : edges) {
            if (!adj.containsKey(e[0]) || !adj.containsKey(e[1])) continue;
            adj.get(e[0]).add(e[1]);
            indeg.merge(e[1], 1, Integer::sum);
        }
        Map<String, Long> trig = new HashMap<>();
        for (String n : nodes) trig.put(n, 0L);
        for (String s : entries) if (trig.containsKey(s)) trig.merge(s, 1L, Long::sum);
        Deque<String> ready = new ArrayDeque<>();
        for (String n : nodes) if (indeg.get(n) == 0) ready.add(n);
        while (!ready.isEmpty()) {
            String u = ready.poll();
            long cu = trig.get(u);
            for (String v : adj.get(u)) {
                if (cu != 0) trig.put(v, Math.addExact(trig.get(v), cu));   // throws on overflow
                if (indeg.merge(v, -1, Integer::sum) == 0) ready.add(v);
            }
        }
    }

    /* ===================================================================
     * PART 4 — ⭐ Both answers on one graph. Say this difference out loud.
     *
     * CHANGES FROM PART 3:
     *   - also computes ancestor COUNT (union semantics) alongside the path
     *     count (sum semantics), so the two are directly comparable
     *   - proves the divergence on a diamond
     * =================================================================== */
    static Map<String, Integer> part4AncestorCount(List<String> nodes, List<String[]> edges) {
        int n = nodes.size();
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(nodes.get(i), i);
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[] indeg = new int[n];
        for (String[] e : edges) {
            Integer u = idx.get(e[0]), v = idx.get(e[1]);
            if (u == null || v == null) continue;
            adj.get(u).add(v);
            indeg[v]++;
        }
        BitSet[] anc = new BitSet[n];
        for (int i = 0; i < n; i++) anc[i] = new BitSet(n);
        Deque<Integer> ready = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) ready.add(i);
        int processed = 0;
        while (!ready.isEmpty()) {
            int u = ready.poll();
            processed++;
            for (int v : adj.get(u)) {
                anc[v].or(anc[u]);      // <-- UNION: de-duplicates
                anc[v].set(u);
                if (--indeg[v] == 0) ready.add(v);
            }
        }
        // Without this the method returned all-zero counts on a cyclic graph, while the sibling
        // SystemDependencyLoadFactor.part3 threw on the same input. See DEBATE-FINDINGS.md finding 2.
        if (processed != n) throw new IllegalArgumentException("cycle detected — not a DAG");
        Map<String, Integer> out = new TreeMap<>();
        for (String s : nodes) out.put(s, anc[idx.get(s)].cardinality());
        return out;
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        List<String> nodes = List.of("A", "B", "C", "D", "E", "F");
        List<String[]> edges = List.of(
                new String[]{"A","B"}, new String[]{"B","C"}, new String[]{"B","D"},
                new String[]{"C","D"}, new String[]{"D","E"}, new String[]{"D","F"},
                new String[]{"E","F"});

        Map<String, Long> expected = new TreeMap<>(Map.of(
                "A", 1L, "B", 1L, "C", 1L, "D", 2L, "E", 2L, "F", 4L));

        Map<String, Long> p1 = part1(nodes, edges, "A");
        System.out.println("part1 (entry A)           = " + p1
                + " -> " + (p1.equals(expected) ? "PASS" : "FAIL"));

        System.out.println("part2 (entries A and C)   = " + part2(nodes, edges, List.of("A", "C")));

        // ⭐ part4: paths vs ancestors diverge on a diamond
        List<String> dia = List.of("A", "B", "C", "D");
        List<String[]> diaE = List.of(new String[]{"A","B"}, new String[]{"A","C"},
                                      new String[]{"B","D"}, new String[]{"C","D"});
        Map<String, Long> paths = part1(dia, diaE, "A");
        Map<String, Integer> ancs = part4AncestorCount(dia, diaE);
        System.out.println("\n⭐ diamond A->{B,C}->D");
        System.out.println("   trigger/path count D   = " + paths.get("D") + "  (expect 2)");
        System.out.println("   ancestor count D       = " + ancs.get("D") + "  (expect 3)");
        System.out.println("   -> " + (paths.get("D") == 2 && ancs.get("D") == 3
                ? "PASS — the two problems genuinely differ" : "FAIL"));

        // part3: overflow. 62 chained diamonds = 2^62 paths, still fits; 64 does not.
        System.out.println("\npart3 overflow check");
        System.out.println("   40 diamonds overflows? " + part3Overflows(chainNodes(40), chainEdges(40), List.of("d0"))
                + "  (expect false, 2^40 fits)");
        System.out.println("   70 diamonds overflows? " + part3Overflows(chainNodes(70), chainEdges(70), List.of("d0"))
                + "  (expect true, 2^70 does not)");
        Map<String, Long> modded = part3(chainNodes(70), chainEdges(70), List.of("d0"), 1_000_000_007L);
        System.out.println("   with mod 1e9+7 -> d70  = " + modded.get("d70") + " (finite, no overflow)");

        // deep chain, no recursion
        List<String> chain = new ArrayList<>();
        List<String[]> ce = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) chain.add("n" + i);
        for (int i = 0; i < 99_999; i++) ce.add(new String[]{"n" + i, "n" + (i + 1)});
        System.out.println("\n100k-node chain (iterative, no stack overflow) -> n99999 = "
                + part1(chain, ce, "n0").get("n99999"));
    }

    /** k chained diamonds: d0 -> {a_i, b_i} -> d_{i+1}. 2^k paths to d_k. */
    private static List<String> chainNodes(int k) {
        List<String> ns = new ArrayList<>();
        for (int i = 0; i <= k; i++) ns.add("d" + i);
        for (int i = 0; i < k; i++) { ns.add("a" + i); ns.add("b" + i); }
        return ns;
    }
    private static List<String[]> chainEdges(int k) {
        List<String[]> es = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            es.add(new String[]{"d" + i, "a" + i});
            es.add(new String[]{"d" + i, "b" + i});
            es.add(new String[]{"a" + i, "d" + (i + 1)});
            es.add(new String[]{"b" + i, "d" + (i + 1)});
        }
        return es;
    }
}
