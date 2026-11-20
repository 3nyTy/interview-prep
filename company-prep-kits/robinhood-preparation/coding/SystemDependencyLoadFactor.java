import java.util.*;

/**
 * Robinhood — "System Dependency Load Factor (DAG reverse-dependency count)"
 * Source: 1point3acres 3d0ffbd1-ea36-4d17-90c4-db52ec75335a
 *
 * Input: a list of programs, and dependency pairs [a, b] meaning "a depends on b".
 * load_factor(x) = the number of nodes that can REACH x following dependency
 *                  edges, excluding x itself.
 * Rule: ignore any pair whose endpoints are not both in the programs list.
 *
 * Given example: programs A,B,C,D with [A->B],[B->C],[A->C],[D->C]
 *                => {A:0, B:1, C:3, D:0}
 *   C is reached by A, B and D  => 3.  Note A reaches C by TWO routes
 *   (A->C and A->B->C) and still counts ONCE.
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE TRAP, AND THE WHOLE POINT OF THIS PROBLEM
 * This is REACHABILITY (a set union — de-duplicates).
 * CountTriggerEvents.java is PATH COUNTING (a sum — multiplies at fan-in).
 * Same graph shape, same topo sweep, different accumulator, different answers.
 * If you accumulate additively here, C becomes 4 and you are wrong.
 *
 * ⚠️ Say this out loud: there is NO O(V+E) exact algorithm. Transitive closure
 * is genuinely quadratic in the worst case (a complete bipartite DAG has
 * Theta(V^2) reachable pairs, so just WRITING the answer is quadratic).
 * Stating that is worth more than groping for a linear sweep that cannot exist.
 * ---------------------------------------------------------------------------
 */
public class SystemDependencyLoadFactor {

    /* ===================================================================
     * PART 1 — Sanitise the input, build both directions.
     *
     * The filtering rule is a real part of the grade: an edge mentioning a
     * program not in the list is dropped ENTIRELY, not partially inserted.
     * Do this first and separately, or every later part inherits bad data.
     * =================================================================== */
    static Map<String, List<String>> buildForward(List<String> programs, List<String[]> deps) {
        Set<String> known = new HashSet<>(programs);
        Map<String, List<String>> adj = new HashMap<>();
        for (String p : programs) adj.put(p, new ArrayList<>());
        for (String[] d : deps) {
            if (d.length < 2) continue;
            if (!known.contains(d[0]) || !known.contains(d[1])) continue;   // <-- the rule
            if (d[0].equals(d[1])) continue;                                // self-loop: ignore
            adj.get(d[0]).add(d[1]);
        }
        return adj;
    }

    /* ===================================================================
     * PART 2 — The straightforward correct answer: reverse traversal per node.
     *
     * For each x, walk the REVERSE graph from x and count what you can touch.
     * O(V * (V + E)). Easy to state, easy to get right, and for the input
     * sizes an interviewer will hand you it is fine. Lead with this.
     * =================================================================== */
    static Map<String, Integer> part2(List<String> programs, List<String[]> deps) {
        Map<String, List<String>> fwd = buildForward(programs, deps);

        Map<String, List<String>> rev = new HashMap<>();
        for (String p : programs) rev.put(p, new ArrayList<>());
        for (var e : fwd.entrySet())
            for (String to : e.getValue()) rev.get(to).add(e.getKey());

        Map<String, Integer> out = new LinkedHashMap<>();
        for (String x : programs) {
            Set<String> seen = new HashSet<>();
            Deque<String> stack = new ArrayDeque<>(rev.get(x));
            seen.add(x);                            // guard, so x never counts itself
            int count = 0;
            while (!stack.isEmpty()) {
                String u = stack.pop();
                if (!seen.add(u)) continue;         // the de-duplication
                count++;
                for (String p : rev.get(u)) if (!seen.contains(p)) stack.push(p);
            }
            out.put(x, count);
        }
        return out;
    }

    /* ===================================================================
     * PART 3 — Ancestor bitsets in topological order.
     *
     * CHANGES FROM PART 2:
     *   - one sweep instead of V sweeps
     *   - each node carries the SET of its ancestors as a BitSet, and a node
     *     unions its own set + itself into each child
     *   - answer = cardinality of that set
     *   - O(V * E / 64) time, O(V^2 / 8) bytes  <-- memory is the real limit
     *
     * Use this when V is a few thousand. At V = 1e5 the bitsets are ~1.25 GB,
     * so do NOT offer this as the answer at the stated constraints; offer it
     * as the answer for moderate V and say why it stops working.
     * =================================================================== */
    static Map<String, Integer> part3(List<String> programs, List<String[]> deps) {
        int n = programs.size();
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(programs.get(i), i);

        Map<String, List<String>> fwd = buildForward(programs, deps);
        int[] indeg = new int[n];
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (var e : fwd.entrySet()) {
            int u = idx.get(e.getKey());
            for (String toName : e.getValue()) {
                int v = idx.get(toName);
                adj.get(u).add(v);
                indeg[v]++;
            }
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
                anc[v].or(anc[u]);        // everything that reaches u reaches v
                anc[v].set(u);            // ...and so does u itself
                if (--indeg[v] == 0) ready.add(v);
            }
        }
        if (processed != n) throw new IllegalArgumentException("cycle detected — not a DAG");

        Map<String, Integer> out = new LinkedHashMap<>();
        for (String p : programs) out.put(p, anc[idx.get(p)].cardinality());
        return out;
    }

    /* ===================================================================
     * PART 4 — The complexity conversation, and the contrast question.
     *
     * If asked "can you do better than quadratic?": no, not exactly. Options
     * to offer instead, in the order worth offering them:
     *   1. Only answer for the k nodes actually asked about  -> O(k*(V+E)).
     *   2. Approximate the count with HyperLogLog per node merged along the
     *      topo order -> near-linear time, ~2% error. Good when the number is
     *      a ranking signal rather than an exact figure.
     *   3. If the graph is a TREE/forest rather than a DAG, ancestors are just
     *      depth, so it collapses to O(V). Ask whether it is a tree — the
     *      sibling problem ReferralCount.java IS the tree case.
     * =================================================================== */
    static Map<String, Integer> part4Subset(List<String> programs, List<String[]> deps,
                                            List<String> asked) {
        Map<String, List<String>> fwd = buildForward(programs, deps);
        Map<String, List<String>> rev = new HashMap<>();
        for (String p : programs) rev.put(p, new ArrayList<>());
        for (var e : fwd.entrySet())
            for (String to : e.getValue()) rev.get(to).add(e.getKey());

        Map<String, Integer> out = new LinkedHashMap<>();
        for (String x : asked) {
            if (!rev.containsKey(x)) { out.put(x, 0); continue; }
            Set<String> seen = new HashSet<>(List.of(x));
            Deque<String> st = new ArrayDeque<>(rev.get(x));
            int c = 0;
            while (!st.isEmpty()) {
                String u = st.pop();
                if (!seen.add(u)) continue;
                c++;
                for (String p : rev.get(u)) if (!seen.contains(p)) st.push(p);
            }
            out.put(x, c);
        }
        return out;
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        List<String> programs = List.of("A", "B", "C", "D");
        List<String[]> deps = List.of(
                new String[]{"A", "B"},
                new String[]{"B", "C"},
                new String[]{"A", "C"},
                new String[]{"D", "C"},
                new String[]{"E", "C"});   // E is NOT in programs -> must be dropped

        Map<String, Integer> expected = Map.of("A", 0, "B", 1, "C", 3, "D", 0);

        Map<String, Integer> p2 = part2(programs, deps);
        Map<String, Integer> p3 = part3(programs, deps);
        System.out.println("part2 (reverse traversal) = " + p2 + " -> "
                + (p2.equals(expected) ? "PASS" : "FAIL"));
        System.out.println("part3 (ancestor bitsets)  = " + p3 + " -> "
                + (p3.equals(expected) ? "PASS" : "FAIL"));

        // ⭐ the de-duplication proof: A reaches C by two routes, C must be 3 not 4
        System.out.println("C == 3 (not 4) -> " + (p3.get("C") == 3 ? "PASS — de-duplicated" : "FAIL"));

        System.out.println("part4 (subset A,C)        = " + part4Subset(programs, deps, List.of("A", "C")));

        // wide fan-in: 1 sink reached by 500 independent sources
        List<String> big = new ArrayList<>();
        List<String[]> bigDeps = new ArrayList<>();
        big.add("sink");
        for (int i = 0; i < 500; i++) { big.add("s" + i); bigDeps.add(new String[]{"s" + i, "sink"}); }
        System.out.println("wide fan-in sink          = " + part3(big, bigDeps).get("sink")
                + " -> " + (part3(big, bigDeps).get("sink") == 500 ? "PASS" : "FAIL"));

        // deep chain: last node reached by all 999 predecessors
        List<String> chain = new ArrayList<>();
        List<String[]> chainDeps = new ArrayList<>();
        for (int i = 0; i < 1000; i++) chain.add("n" + i);
        for (int i = 0; i < 999; i++) chainDeps.add(new String[]{"n" + i, "n" + (i + 1)});
        System.out.println("deep chain (no recursion) = " + part3(chain, chainDeps).get("n999")
                + " -> " + (part3(chain, chainDeps).get("n999") == 999 ? "PASS" : "FAIL"));
    }
}
