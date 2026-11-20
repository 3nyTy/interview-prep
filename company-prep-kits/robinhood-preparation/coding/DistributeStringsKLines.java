import java.util.*;

/**
 * Robinhood — "Distribute Strings into K Lines with Greedy Placement"
 * Source: 1point3acres 84ccf656-2a40-4ff8-8080-a648e545eb50
 *
 * Part 1 (as posed): assign each string to any of k lines; output the lines.
 * Part 2 (as posed): a line's LOAD is the total length of its strings. For each
 *   string IN ORDER, assign it to the line with MINIMUM current load; break ties
 *   by SMALLEST LINE INDEX. Output the strings per line and the loads.
 *
 * Constraints: list length 1..2e5, k 1..2e5, individual string length 1..1000.
 *
 * Given example: strings=["a","bbb","cc","dddd"], k=3
 *   line0 ["a","dddd"] load 5 | line1 ["bbb"] load 3 | line2 ["cc"] load 2
 *   Trace: a->L0 (all 0, smallest index). bbb->L1 (0 < 1). cc->L2 (0 available).
 *          dddd->L0 (loads 1,3,2 -> L0 lowest).
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE ONE THING: PriorityQueue is NOT stable, so the smallest-index tie-break
 * must live IN THE COMPARATOR. Comparing on load alone and trusting the heap to
 * return the lowest index is the bug — it will pass the given example by luck
 * and fail on ties later. Key on the tuple (load, index).
 *
 * ⚠️ k can exceed the number of strings. Empty lines must still appear, load 0.
 * ⚠️ Part 1 is deliberately underspecified ("any of k lines"). Do the simplest
 * thing (round-robin), say it satisfies the spec, and move on. Over-engineering
 * Part 1 wastes the time you need for Part 2.
 * ---------------------------------------------------------------------------
 */
public class DistributeStringsKLines {

    record Lines(List<List<String>> lines, long[] loads) {
        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines.size(); i++)
                sb.append("line").append(i).append(' ').append(lines.get(i))
                  .append(" load=").append(loads[i]).append(i + 1 < lines.size() ? " | " : "");
            return sb.toString();
        }
    }

    /* ===================================================================
     * PART 1 — "any of k lines". Round robin. O(n).
     *
     * The spec permits literally any assignment, so the cheapest correct answer
     * is index % k. Note it also happens to balance COUNT (not load), which is
     * a different objective from Part 2 — worth saying, because it sets up why
     * Part 2 needs a heap at all.
     * =================================================================== */
    static Lines part1RoundRobin(List<String> strings, int k) {
        List<List<String>> lines = newLines(k);
        long[] loads = new long[k];
        for (int i = 0; i < strings.size(); i++) {
            int line = i % k;
            lines.get(line).add(strings.get(i));
            loads[line] += strings.get(i).length();
        }
        return new Lines(lines, loads);
    }

    /* ===================================================================
     * PART 2 — ⭐ Greedy least-loaded with an index tie-break. THE ANSWER.
     *
     * CHANGES FROM PART 1:
     *   - a min-heap of long[]{load, index}
     *   - the comparator compares load FIRST, then index — that second clause
     *     is the whole correctness argument
     *   - pop the min, append, add the length, push it back
     *
     * O(n log k). Every line is seeded at load 0 so empty lines exist from the
     * start and appear in the output even when k > n.
     * =================================================================== */
    static Lines part2Greedy(List<String> strings, int k) {
        List<List<String>> lines = newLines(k);
        long[] loads = new long[k];

        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) ->
                a[0] != b[0] ? Long.compare(a[0], b[0])    // lower load wins
                             : Long.compare(a[1], b[1]));  // ⭐ then LOWER INDEX wins
        for (int i = 0; i < k; i++) pq.add(new long[]{0L, i});

        for (String s : strings) {
            long[] top = pq.poll();
            int idx = (int) top[1];
            lines.get(idx).add(s);
            loads[idx] += s.length();
            top[0] = loads[idx];
            pq.add(top);
        }
        return new Lines(lines, loads);
    }

    /* ===================================================================
     * PART 3 — Output format, and the k > n case.
     *
     * CHANGES FROM PART 2:
     *   - renders exactly what the problem asks for: the strings per line AND
     *     the load per line
     *   - k > n is exercised in main(), because "does he emit the empty lines"
     *     is a real hidden test on this problem
     * =================================================================== */
    static String part3Format(List<String> strings, int k) {
        Lines r = part2Greedy(strings, k);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sb.append("line").append(i).append(": ").append(r.lines().get(i))
              .append(", load=").append(r.loads()[i]).append('\n');
        }
        return sb.toString();
    }

    /* ===================================================================
     * PART 4 — Scale, and the optimality follow-up.
     *
     * CHANGES FROM PART 3:
     *   - an O(n) fast path for the common k >= n case: every string gets its
     *     own line, so no heap is needed at all
     *   - a maxLoad() helper for the follow-up
     *
     * ⚠️ IF THEY ASK "IS GREEDY OPTIMAL?" — it is NOT. This is multiway number
     * partitioning, which is NP-hard, and the online first-fit style greedy here
     * is a 2-approximation (actually 4/3 for the sorted/LPT variant). Two things
     * to say:
     *   1. The problem SPECIFIES the greedy rule, so greedy is the required
     *      answer, not a heuristic choice I'm making.
     *   2. If they want a better balance and allow reordering, sort descending
     *      first (Longest Processing Time) — same code, better bound. But that
     *      violates "for each string in sequence", so only do it if asked.
     * Volunteering this distinction is the strongest move available here.
     * =================================================================== */
    static Lines part4Fast(List<String> strings, int k) {
        if (k >= strings.size()) {                    // no contention: one string per line
            List<List<String>> lines = newLines(k);
            long[] loads = new long[k];
            for (int i = 0; i < strings.size(); i++) {
                lines.get(i).add(strings.get(i));
                loads[i] = strings.get(i).length();
            }
            return new Lines(lines, loads);
        }
        return part2Greedy(strings, k);
    }

    /** LPT variant: sort descending first. Better balance, but reorders the input. */
    static Lines part4LPT(List<String> strings, int k) {
        List<String> sorted = new ArrayList<>(strings);
        sorted.sort((a, b) -> Integer.compare(b.length(), a.length()));
        return part2Greedy(sorted, k);
    }

    static long maxLoad(Lines r) {
        long m = 0;
        for (long l : r.loads()) m = Math.max(m, l);
        return m;
    }

    private static List<List<String>> newLines(int k) {
        List<List<String>> lines = new ArrayList<>(k);
        for (int i = 0; i < k; i++) lines.add(new ArrayList<>());
        return lines;
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        List<String> ex = List.of("a", "bbb", "cc", "dddd");

        System.out.println("=== part2: the stated example, k=3 ===");
        Lines r = part2Greedy(ex, 3);
        System.out.println(r);
        boolean ok = r.lines().get(0).equals(List.of("a", "dddd"))
                  && r.lines().get(1).equals(List.of("bbb"))
                  && r.lines().get(2).equals(List.of("cc"))
                  && r.loads()[0] == 5 && r.loads()[1] == 3 && r.loads()[2] == 2;
        System.out.println("expected L0[a,dddd]=5 L1[bbb]=3 L2[cc]=2 -> " + (ok ? "PASS" : "FAIL"));

        System.out.println("\n=== ⭐ the tie-break, isolated ===");
        // four equal-length strings, k=2. Loads tie at every step, so the answer
        // is decided ENTIRELY by the smallest-index rule.
        Lines tie = part2Greedy(List.of("x", "y", "z", "w"), 2);
        System.out.println(tie);
        System.out.println("expect L0[x,z] L1[y,w] -> "
                + (tie.lines().get(0).equals(List.of("x", "z"))
                && tie.lines().get(1).equals(List.of("y", "w")) ? "PASS" : "FAIL"));

        System.out.println("\n=== k > n: empty lines must still appear ===");
        System.out.print(part3Format(List.of("aa", "b"), 4));
        Lines big = part2Greedy(List.of("aa", "b"), 4);
        System.out.println("4 lines present, last two empty with load 0 -> "
                + (big.lines().size() == 4 && big.lines().get(2).isEmpty()
                   && big.loads()[3] == 0 ? "PASS" : "FAIL"));

        System.out.println("\n=== k = 1: everything on one line ===");
        Lines one = part2Greedy(ex, 1);
        System.out.println(one + " -> " + (one.loads()[0] == 10 ? "PASS" : "FAIL"));

        System.out.println("\n=== part1 round robin, for contrast ===");
        System.out.println(part1RoundRobin(ex, 3));
        System.out.println("  balances COUNT, not LOAD — hence part 2 needs the heap");

        System.out.println("\n=== part4: fast path agrees with the heap ===");
        Lines viaHeap = part2Greedy(List.of("aa", "b", "ccc"), 5);
        Lines viaFast = part4Fast(List.of("aa", "b", "ccc"), 5);
        System.out.println("  " + viaFast);
        System.out.println("  identical to heap? " + (Arrays.equals(viaHeap.loads(), viaFast.loads())
                && viaHeap.lines().equals(viaFast.lines()) ? "PASS" : "FAIL"));

        System.out.println("\n=== part4: in-order greedy is NOT optimal ===");
        // lengths 3,3,4,4,5,5 in THAT order, k=3. Total 24, so the best max is 8.
        // In-order greedy reaches 9; LPT (sort desc first) reaches 8.
        List<String> adv = List.of("aaa", "bbb", "cccc", "dddd", "eeeee", "fffff");
        Lines g = part2Greedy(adv, 3);
        Lines l = part4LPT(adv, 3);
        System.out.println("  in-order greedy maxLoad = " + maxLoad(g) + "  " + Arrays.toString(g.loads()));
        System.out.println("  LPT (sorted desc)       = " + maxLoad(l) + "  " + Arrays.toString(l.loads()));
        System.out.println("  total 24 over k=3 -> best possible max is 8");
        System.out.println("  LPT strictly better here? " + (maxLoad(l) < maxLoad(g)
                ? "PASS — proves in-order greedy is a heuristic, not optimal"
                : "no (they tie on this input)"));
        System.out.println("  ⚠️ but the PROBLEM SPECIFIES in-order greedy, so part2 is the required");
        System.out.println("     answer. Only offer LPT if they ask for better balance AND allow reordering.");

        System.out.println("\n=== scale: n=200000, k=200000 ===");
        List<String> huge = new ArrayList<>(200_000);
        Random rnd = new Random(5);
        for (int i = 0; i < 200_000; i++) huge.add("x".repeat(1 + rnd.nextInt(1000)));
        long t0 = System.currentTimeMillis();
        Lines h1 = part2Greedy(huge, 200_000);
        long t1 = System.currentTimeMillis();
        Lines h2 = part4Fast(huge, 200_000);
        long t2 = System.currentTimeMillis();
        System.out.println("  heap      " + (t1 - t0) + " ms   maxLoad=" + maxLoad(h1));
        System.out.println("  fast path " + (t2 - t1) + " ms   maxLoad=" + maxLoad(h2));

        long t3 = System.currentTimeMillis();
        Lines h3 = part2Greedy(huge, 1000);
        System.out.println("  n=200k k=1000 (real contention) " + (System.currentTimeMillis() - t3)
                + " ms  maxLoad=" + maxLoad(h3) + "  spread="
                + (maxLoad(h3) - Arrays.stream(h3.loads()).min().orElse(0)));
    }
}
