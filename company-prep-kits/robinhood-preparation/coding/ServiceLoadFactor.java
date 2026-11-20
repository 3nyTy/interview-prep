import java.util.*;

/**
 * Robinhood — "Compute Service Load Factor in a Service Dependency Graph"
 * Source: 1point3acres ec6e8e84-19bc-4ef6-92a8-d2b50f5fead0   (flagged HOT / high frequency)
 *
 *   load(s)        = base_load(s) + sum over parents p of  load(p) * weight(p -> s)
 *   load_factor(s) = load(s) / capacity(s)
 *
 * Constraints: N <= 1e5 services, M <= 2e5 edges. Graph guaranteed acyclic.
 * Output: load factor per service, sorted alphabetically.
 *
 * Given example: A->B(1), B->C(2), base(A)=10, capacity(A,B,C)=(20,10,10)
 *                => 0.5, 1.0, 2.0
 *
 * ---------------------------------------------------------------------------
 * THE TEMPLATE. This is Kahn's topological sort with an accumulator, and it is
 * the same skeleton as SystemDependencyLoadFactor and CountTriggerEvents. Only
 * WHAT you accumulate changes. Learn it once:
 *
 *     1. build adjacency + indegree
 *     2. queue every indegree-0 node
 *     3. pop u, append to order, for each edge u->v: ACCUMULATE, then if
 *        --indegree[v] == 0 push v
 *     4. order.size() != n  =>  a cycle exists
 *
 * ITERATIVE, never recursive: N = 1e5 with a path-shaped graph blows the stack.
 * ---------------------------------------------------------------------------
 */
public class ServiceLoadFactor {

    /* ===================================================================
     * PART 1 — Propagate base load along unweighted edges. Return raw load.
     *
     * The interviewer almost always starts here: no weights, no capacity.
     * Get the topo-order accumulation right before adding anything.
     * =================================================================== */
    static Map<String, Double> part1(List<String> services,
                                     Map<String, Double> baseLoad,
                                     List<String[]> edges) {
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indeg = new HashMap<>();
        for (String s : services) {
            adj.put(s, new ArrayList<>());
            indeg.put(s, 0);
        }
        for (String[] e : edges) {
            adj.get(e[0]).add(e[1]);
            indeg.merge(e[1], 1, Integer::sum);
        }

        // load starts at the node's own base load; parents add to it as we sweep
        Map<String, Double> load = new HashMap<>();
        for (String s : services) load.put(s, baseLoad.getOrDefault(s, 0.0));

        Deque<String> ready = new ArrayDeque<>();
        for (String s : services) if (indeg.get(s) == 0) ready.add(s);

        int processed = 0;
        while (!ready.isEmpty()) {
            String u = ready.poll();
            processed++;
            for (String v : adj.get(u)) {
                load.merge(v, load.get(u), Double::sum);   // <-- THE ACCUMULATOR
                if (indeg.merge(v, -1, Integer::sum) == 0) ready.add(v);
            }
        }
        if (processed != services.size()) throw new IllegalArgumentException("cycle detected");
        return load;
    }

    /* ===================================================================
     * PART 2 — Add edge weights.
     *
     * CHANGES FROM PART 1:
     *   - adjacency now carries a weight per edge, so it holds Edge not String
     *   - the accumulator multiplies:  load[v] += load[u] * w
     *   - unweighted input defaults to w = 1.0, so Part 1 is the w=1 case
     * Everything else is byte-for-byte identical.
     * =================================================================== */
    record Edge(String to, double w) {}

    static Map<String, Double> part2(List<String> services,
                                     Map<String, Double> baseLoad,
                                     List<Object[]> weightedEdges) {   // {from, to, weight}
        Map<String, List<Edge>> adj = new HashMap<>();
        Map<String, Integer> indeg = new HashMap<>();
        for (String s : services) {
            adj.put(s, new ArrayList<>());
            indeg.put(s, 0);
        }
        for (Object[] e : weightedEdges) {
            String from = (String) e[0], to = (String) e[1];
            double w = e.length > 2 ? ((Number) e[2]).doubleValue() : 1.0;
            adj.get(from).add(new Edge(to, w));
            indeg.merge(to, 1, Integer::sum);
        }

        Map<String, Double> load = new HashMap<>();
        for (String s : services) load.put(s, baseLoad.getOrDefault(s, 0.0));

        Deque<String> ready = new ArrayDeque<>();
        for (String s : services) if (indeg.get(s) == 0) ready.add(s);

        int processed = 0;
        while (!ready.isEmpty()) {
            String u = ready.poll();
            processed++;
            for (Edge e : adj.get(u)) {
                load.merge(e.to(), load.get(u) * e.w(), Double::sum);   // <-- * weight
                if (indeg.merge(e.to(), -1, Integer::sum) == 0) ready.add(e.to());
            }
        }
        if (processed != services.size()) throw new IllegalArgumentException("cycle detected");
        return load;
    }

    /* ===================================================================
     * PART 3 — Divide by capacity, return sorted alphabetically. THE ANSWER.
     *
     * CHANGES FROM PART 2:
     *   - result is a TreeMap, so "sorted alphabetically" costs nothing
     *   - divide by capacity
     *   - capacity 0 handled explicitly (see the note) instead of silently
     *     producing Infinity/NaN
     * =================================================================== */
    static SortedMap<String, Double> part3(List<String> services,
                                           Map<String, Double> baseLoad,
                                           Map<String, Double> capacity,
                                           List<Object[]> weightedEdges) {
        Map<String, Double> load = part2(services, baseLoad, weightedEdges);

        SortedMap<String, Double> out = new TreeMap<>();               // <-- alphabetical, free
        for (String s : services) {
            double cap = capacity.getOrDefault(s, 0.0);
            // Capacity 0 is a real input question. Infinity is a defensible answer only if
            // you SAY it is; silently emitting NaN for 0/0 is not. Ask the interviewer, then
            // pick one and be explicit.
            double lf = (cap == 0.0)
                    ? (load.get(s) == 0.0 ? 0.0 : Double.POSITIVE_INFINITY)
                    : load.get(s) / cap;
            out.put(s, lf);
        }
        return out;
    }

    /* ===================================================================
     * PART 4 — Hardening, and the follow-up they actually ask.
     *
     * CHANGES FROM PART 3:
     *   - returns which services are OVER capacity (lf > 1.0), the natural
     *     "so what do you do with this" follow-up
     *   - keeps insertion of isolated services (they have no edges but must
     *     still appear, at base/capacity)
     *   - reports the cycle instead of throwing, since real dependency graphs
     *     acquire cycles and "guaranteed acyclic" is an input assumption you
     *     should verify rather than trust
     * =================================================================== */
    /**
     * Epsilon, not a bare {@code > 1.0}. Double accumulation drifts UPWARD on some operand sets:
     * base loads .33/.56/.11 into one sink of capacity 1.0 sum to 1.0000000000000002, so a node
     * exactly AT capacity was reported as overloaded. Added after codex debate 2026-08-14; see
     * DEBATE-FINDINGS.md finding 3.
     *
     * Consequence to state out loud: a node whose true load exceeds capacity by less than EPS is
     * deliberately not flagged. That is the right trade for an advisory signal. If the threshold
     * were a money or a regulatory boundary, this would have to be BigDecimal or scaled integers.
     */
    static final double OVERLOAD_EPS = 1e-9;

    static List<String> part4Overloaded(SortedMap<String, Double> factors) {
        List<String> over = new ArrayList<>();
        for (Map.Entry<String, Double> e : factors.entrySet())
            if (e.getValue() > 1.0 + OVERLOAD_EPS) over.add(e.getKey());
        return over;   // already alphabetical: it came from a TreeMap
    }

    /** Cycle-tolerant variant: returns the nodes NOT in any topological order. */
    static Set<String> part4FindCycleNodes(List<String> services, List<Object[]> weightedEdges) {
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indeg = new HashMap<>();
        for (String s : services) { adj.put(s, new ArrayList<>()); indeg.put(s, 0); }
        for (Object[] e : weightedEdges) {
            adj.get((String) e[0]).add((String) e[1]);
            indeg.merge((String) e[1], 1, Integer::sum);
        }
        Deque<String> ready = new ArrayDeque<>();
        for (String s : services) if (indeg.get(s) == 0) ready.add(s);
        Set<String> settled = new HashSet<>();
        while (!ready.isEmpty()) {
            String u = ready.poll();
            settled.add(u);
            for (String v : adj.get(u))
                if (indeg.merge(v, -1, Integer::sum) == 0) ready.add(v);
        }
        Set<String> inCycle = new LinkedHashSet<>(services);
        inCycle.removeAll(settled);
        return inCycle;                    // empty => genuinely acyclic
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        List<String> services = List.of("A", "B", "C");
        Map<String, Double> base = Map.of("A", 10.0, "B", 0.0, "C", 0.0);
        Map<String, Double> cap  = Map.of("A", 20.0, "B", 10.0, "C", 10.0);
        List<Object[]> edges = List.of(
                new Object[]{"A", "B", 1.0},
                new Object[]{"B", "C", 2.0});

        System.out.println("part1 (unweighted loads)  = " + new TreeMap<>(
                part1(services, base, List.of(new String[]{"A","B"}, new String[]{"B","C"}))));
        System.out.println("part2 (weighted loads)    = " + new TreeMap<>(part2(services, base, edges)));

        SortedMap<String, Double> lf = part3(services, base, cap, edges);
        System.out.println("part3 (load factors)      = " + lf);
        boolean ok = Math.abs(lf.get("A") - 0.5) < 1e-9
                  && Math.abs(lf.get("B") - 1.0) < 1e-9
                  && Math.abs(lf.get("C") - 2.0) < 1e-9;
        System.out.println("  expected {A=0.5, B=1.0, C=2.0} -> " + (ok ? "PASS" : "FAIL"));

        System.out.println("part4 (overloaded)        = " + part4Overloaded(lf));

        // isolated node must still appear
        SortedMap<String, Double> iso = part3(
                List.of("A", "Z"), Map.of("A", 5.0, "Z", 7.0),
                Map.of("A", 10.0, "Z", 7.0), List.of());
        System.out.println("isolated node kept        = " + iso
                + " -> " + (Math.abs(iso.get("Z") - 1.0) < 1e-9 ? "PASS" : "FAIL"));

        // diamond: multiple parents both contribute
        SortedMap<String, Double> diamond = part3(
                List.of("A","B","C","D"),
                Map.of("A", 8.0, "B", 0.0, "C", 0.0, "D", 0.0),
                Map.of("A", 8.0, "B", 8.0, "C", 8.0, "D", 8.0),
                List.of(new Object[]{"A","B",1.0}, new Object[]{"A","C",1.0},
                        new Object[]{"B","D",1.0}, new Object[]{"C","D",1.0}));
        System.out.println("diamond (D gets both)     = " + diamond
                + " -> " + (Math.abs(diamond.get("D") - 2.0) < 1e-9 ? "PASS" : "FAIL"));

        System.out.println("cycle detection           = " + part4FindCycleNodes(
                List.of("X","Y"), List.of(new Object[]{"X","Y",1.0}, new Object[]{"Y","X",1.0})));
    }
}
