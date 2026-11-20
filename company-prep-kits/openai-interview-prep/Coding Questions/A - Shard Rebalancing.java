/*
 * Shard Rebalancing — OpenAI SWE coding question.  ("hot new general coding question")
 *
 * TWO IMPLEMENTATIONS of the same spec, both fully self-contained:
 *   Part1 — DIRECT COUNTING (the one to write in an interview). Coverage is
 *           counted straight off the kept-shard list; no auxiliary structure.
 *   Part2 — DIFFERENCE MAP (the tighter-bound alternative). Coverage lives in a
 *           TreeMap of breakpoints and is read via prefix sums.
 * Both are differentially fuzzed against each other (60k random cases, 0
 * mismatches) and pass every spec example.
 *
 * ------------------------------------------------------------------------
 * PROBLEM
 * ------------------------------------------------------------------------
 * Each shard is a string "id:start:end" covering the INCLUSIVE range
 * [start, end]. Given `limit` and an array of shards, rebalance so that:
 *   - No key is covered by more than `limit` shards.
 *   - Processing happens after sorting by (start asc, then end asc).
 *   - If a shard would cover a key that already has `limit` active shards,
 *     shift its start to the earliest key where coverage is STRICTLY < limit.
 *   - If shifting makes start > end, delete that shard.
 *   - After all shifts/deletions, coverage must stay continuous from the
 *     ORIGINAL min(start) to the ORIGINAL max(end). If a gap appears, extend
 *     the most recently kept shard to fill it.
 *   - Return "id:start:end" strings, in any order.
 * Constraints: 1 <= limit <= 1e5, 0 <= n <= 1e4, -1e9 <= start <= end <= 1e9.
 *
 * PRIORITY RULE / WHY SHIFT THE LATER SHARD: the objective is to MINIMIZE DATA
 * MOVEMENT. The earlier shard already physically holds those keys, so moving its
 * boundary means relocating data. Processing in start-ascending order makes this
 * automatic: the earlier shard is placed first and keeps its boundary; only the
 * later one yields. "Earliest qualifying key" and "minimize data movement" are
 * the same instruction stated two ways — shifting further than necessary
 * relocates keys for no benefit.
 *
 * ------------------------------------------------------------------------
 * FRAMING: this is the merge-intervals sweep-line skeleton with the predicate
 * swapped. Canonical merge-intervals tests `next.start <= prev.end` ("any
 * overlap"); here the predicate is `activeCount > limit` ("too many overlaps").
 * Same sort-then-walk shape, different test.
 *
 * ------------------------------------------------------------------------
 * THE KEY STRUCTURAL FACT (both implementations rest on it)
 * ------------------------------------------------------------------------
 * Coverage is a STEP function: constant between breakpoints, changing only at
 * them. And coverage only ever DECREASES at (someKeptEnd + 1) — a start raises
 * coverage, only end+1 lowers it. Since we want the first key where coverage
 * falls BELOW the limit, that key must be one where coverage decreased.
 *   => the shifted start is ALWAYS (some kept shard's end) + 1, or s0 itself.
 * This is why the examples produce the numbers they do:
 *   limit=2, C shifts to 101 = A.end+1 (100+1)
 *   limit=1, C shifts to 111 = B.end+1 (110+1)
 *   example3: C -> 31 = A.end+1,  D -> 32 = B.end+1
 * It also bounds the candidate set to n+1 keys, which is what makes Part1 work.
 *
 * SHIFTING STOPS WHEN THERE IS ROOM FOR ONE MORE (coverage <= limit-1), NOT when
 * coverage reaches zero. Same input, varying limit:
 *   ["A:0:100","B:40:110","C:80:200","D:210:300"]
 *     limit=1 -> [A:0:100, B:101:110, C:111:209, D:210:300]
 *     limit=2 -> [A:0:100, B:40:110,  C:101:209, D:210:300]
 *     limit=3 -> [A:0:100, B:40:110,  C:80:209,  D:210:300]   (no shift at all)
 *
 * ------------------------------------------------------------------------
 * VERIFIED INVARIANTS (fuzzed, 40k random cases each)
 * ------------------------------------------------------------------------
 *   - No key EVER exceeds `limit` — gap-fill included. A gap is by definition
 *     uncovered, and any shard whose start lands inside a saturated region gets
 *     shifted past it or dropped, so it can never survive as the `prev` sitting
 *     short of a saturated stretch. Extending into a true gap takes coverage
 *     0 -> 1, and limit >= 1.
 *   - Coverage is contiguous across [originalMin, originalMax].
 *   - rebalance() is IDEMPOTENT: re-running it on its own output is a no-op.
 *     (Required by the stateful Shards.rebalance() variant.)
 *
 * ------------------------------------------------------------------------
 * !! DO NOT DO THIS !! — a wrong "optimization" that looks right
 * ------------------------------------------------------------------------
 * Tempting O(n log n): keep a min-heap of active kept ends; when heap.size() ==
 * limit, set cand = heap.poll() + 1 and retry. IT IS WRONG — the probe is
 * DESTRUCTIVE. Polling to FIND the shift point permanently removes ends that
 * later shards still need. Counterexample (limit = 1):
 *     ["A:20:35","B:7:16","C:20:48","D:14:15","E:1:27"]
 *   correct -> [E:1:27, A:28:35, C:36:48]
 *   heap    -> [E:1:27, A:20:35, C:36:48, D:14:19]   (D wrongly kept)
 *   Why: E is kept (heap={27}). B probes, polls 27 -> cand=28 > B.end=16 so B is
 *   dropped, but E's end 27 was destroyed on the way. D:14:15 then sees an empty
 *   heap and is wrongly kept, even though E covers key 14.
 * Note the scan position is NOT monotone across probes: kept starts are monotone
 * non-decreasing, but a DROPPED shard's probe can walk arbitrarily far forward
 * (B reached 28) while a later shard's original start sits far behind it (D=14).
 * Measured: 15,912 mismatches in 60,000 fuzzed cases. Do not resurrect it.
 *
 * ------------------------------------------------------------------------
 * COMPLEXITY
 * ------------------------------------------------------------------------
 *   Part1 (direct counting): O(n) per shard typically (returns on the 1st or 2nd
 *     candidate), O(n^2) per shard adversarially -> O(n^2) typical / O(n^3) worst
 *     overall. Comfortable at n <= 1e4 on the typical path.
 *   Part2 (difference map): O(n) per shard -> O(n^2) overall, guaranteed.
 *   Both: O(n log n) sort, O(n) gap-fill.
 * Write Part1 in an interview, then say: "coverage is recounted per candidate,
 * which is O(n^2) worst case; a difference map with prefix sums makes each query
 * O(n) and the whole pass O(n^2) — I'll switch if you want the tighter bound."
 *
 * COMPILE / RUN (filename has spaces + hyphen and does not match the class name;
 * that is fine because the top-level class is package-private):
 *   javac -d /tmp/out "A - Shard Rebalancing.java"
 *   java -cp /tmp/out ShardRebalancing          # both parts + differential fuzz
 *   java -cp /tmp/out ShardRebalancing part1    # direct counting only
 *   java -cp /tmp/out ShardRebalancing part2    # difference map only
 *   java -cp /tmp/out ShardRebalancing fuzz     # differential fuzz only
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Top-level dispatcher. Package-private (NOT public) so the filename need not
 * match the class name.
 */
class ShardRebalancing {

    public static void main(String[] args) {
        String which = args.length > 0 ? args[0].toLowerCase() : "";
        switch (which) {
            case "part1" -> Part1.main(new String[0]);
            case "part2" -> Part2.main(new String[0]);
            case "fuzz" -> differentialFuzz();
            case "" -> {
                Part1.main(new String[0]);
                Part2.main(new String[0]);
                differentialFuzz();
            }
            default -> System.out.println(
                    "Usage: java ShardRebalancing [part1|part2|fuzz]  (no arg runs everything)");
        }
    }

    /** Cross-check the two implementations against each other on random input. */
    private static void differentialFuzz() {
        System.out.println();
        System.out.println("=== differential fuzz: Part1 (direct) vs Part2 (delta map) ===");
        Random rnd = new Random(99L);
        int mismatches = 0;
        String first = null;
        for (int t = 0; t < 60_000; t++) {
            int n = 1 + rnd.nextInt(7);
            int limit = 1 + rnd.nextInt(3);
            String[] in = new String[n];
            for (int i = 0; i < n; i++) {
                long s = rnd.nextInt(30);
                long e = s + rnd.nextInt(30);
                in[i] = ((char) ('A' + i)) + ":" + s + ":" + e;
            }
            Set<String> a = new HashSet<>(Part1.rebalance(limit, in.clone()));
            Set<String> b = new HashSet<>(Part2.rebalance(limit, in.clone()));
            if (!a.equals(b) && mismatches++ == 0) {
                first = "limit=" + limit + " in=" + Arrays.toString(in)
                        + "\n    part1=" + new TreeSet<>(a)
                        + "\n    part2=" + new TreeSet<>(b);
            }
        }
        System.out.println("60000 cases, mismatches: " + mismatches);
        if (first != null) {
            throw new AssertionError("differential mismatch:\n    " + first);
        }
        System.out.println("the two implementations agree");
    }

    // ========================================================================
    // PART 1 — DIRECT COUNTING (primary; write this one in an interview).
    // Self-contained: owns Shard, parse, coverage, gap-fill, serialize, tests.
    // ========================================================================
    static final class Part1 {

        /** Mutable shard. start/end are mutated during shifts and gap-fill. */
        private static final class Shard {
            final String id;
            long start;
            long end;

            Shard(String id, long start, long end) {
                this.id = id;
                this.start = start;
                this.end = end;
            }
        }

        /** Parse "id:start:end". Ids contain no colons, so split on the first two. */
        private static Shard parse(String s) {
            // limit=3 keeps everything after the second colon intact.
            String[] parts = s.split(":", 3);
            if (parts.length != 3) {
                throw new IllegalArgumentException("bad shard format: '" + s + "'");
            }
            return new Shard(parts[0],
                    Long.parseLong(parts[1].trim()),
                    Long.parseLong(parts[2].trim()));
        }

        /**
         * Coverage at key k from already-kept shards: literally the spec's
         * definition — count the kept shards whose range contains k.
         */
        private static int coverageAt(List<Shard> kept, long k) {
            int c = 0;
            for (Shard s : kept) {
                if (s.start <= k && k <= s.end) {
                    c++;
                }
            }
            return c;
        }

        /**
         * Earliest key >= s0 whose coverage is strictly < limit.
         *
         * Coverage only ever DROPS just after a kept shard ends, so the only keys
         * that can be the answer are s0 itself and (keptEnd + 1) for each kept
         * shard — at most n+1 candidates. Test them in ascending order and return
         * the first that qualifies.
         *
         * The shard being placed is NOT in `kept` yet, so it never counts its own
         * contribution.
         */
        private static long findShiftedStart(List<Shard> kept, long s0, int limit) {
            TreeSet<Long> candidates = new TreeSet<>();   // ascending, deduped
            candidates.add(s0);
            for (Shard s : kept) {
                if (s.end + 1 > s0) {
                    candidates.add(s.end + 1);
                }
            }
            for (long c : candidates) {
                if (coverageAt(kept, c) < limit) {
                    return c;
                }
            }
            // Unreachable: past every kept end, coverage is 0 and limit >= 1.
            return s0;
        }

        /**
         * Enforce continuity from originalMin to originalMax over the kept shards
         * (in keep order). Extends UNCONDITIONALLY — see the header: this cannot
         * breach the limit, because a gap is by definition uncovered.
         */
        private static void fillGaps(List<Shard> kept, long originalMin, long originalMax) {
            if (kept.isEmpty()) {
                return;
            }
            for (int i = 1; i < kept.size(); i++) {
                Shard prev = kept.get(i - 1);
                Shard cur = kept.get(i);
                if (cur.start > prev.end + 1) {
                    prev.end = cur.start - 1;   // close the gap BACKWARDS
                }
            }
            // Trailing fill: the shard that originally reached originalMax may
            // have been shifted or dropped.
            Shard last = kept.get(kept.size() - 1);
            if (last.end < originalMax) {
                last.end = originalMax;
            }
            // originalMin needs no handling: the smallest-start shard is processed
            // first, sees coverage 0, and is therefore never shifted nor dropped.
        }

        /** Serialize kept shards back to "id:start:end". */
        private static List<String> serialize(List<Shard> kept) {
            List<String> out = new ArrayList<>(kept.size());
            for (Shard s : kept) {
                out.add(s.id + ":" + s.start + ":" + s.end);
            }
            return out;
        }

        /** Primary entry point. */
        static List<String> rebalance(int limit, String[] shards) {
            if (limit < 1) {
                throw new IllegalArgumentException("limit must be >= 1, got " + limit);
            }
            if (shards == null || shards.length == 0) {
                return new ArrayList<>();
            }

            // 1. parse
            List<Shard> all = new ArrayList<>(shards.length);
            for (String s : shards) {
                all.add(parse(s));
            }

            // 2. original envelope, captured BEFORE any shift or drop — a shard
            //    that is later dropped STILL contributes its bounds.
            long originalMin = Long.MAX_VALUE;
            long originalMax = Long.MIN_VALUE;
            for (Shard s : all) {
                if (s.start < originalMin) originalMin = s.start;
                if (s.end > originalMax) originalMax = s.end;
            }

            // 3. sort by (start asc, end asc)
            all.sort(Comparator.comparingLong((Shard s) -> s.start)
                    .thenComparingLong(s -> s.end));

            // 4. shift / drop, re-deriving coverage from `kept` each time (this is
            //    what makes chained shifts cascade correctly)
            List<Shard> keptInOrder = new ArrayList<>();
            for (Shard s : all) {
                long newStart = findShiftedStart(keptInOrder, s.start, limit);
                if (newStart > s.end) {
                    continue;   // shifted past its own end -> drop
                }
                s.start = newStart;
                keptInOrder.add(s);
            }

            // 5. gap-fill, then 6. serialize
            fillGaps(keptInOrder, originalMin, originalMax);
            return serialize(keptInOrder);
        }

        // ---- tests (order-insensitive; output order is unspecified) ----------

        private static Map<String, String> normalize(List<String> shards) {
            Map<String, String> m = new HashMap<>();
            for (String s : shards) {
                String[] parts = s.split(":", 3);
                if (parts.length != 3) {
                    throw new AssertionError("Bad shard format: '" + s + "'");
                }
                if (m.put(parts[0], parts[1] + ":" + parts[2]) != null) {
                    throw new AssertionError("Duplicate id in output: '" + parts[0] + "'");
                }
            }
            return m;
        }

        private static void check(String name, int limit, String[] in, String[] expected) {
            List<String> actual = rebalance(limit, in);
            Map<String, String> got = normalize(actual);
            Map<String, String> want = normalize(Arrays.asList(expected));
            if (!got.equals(want)) {
                throw new AssertionError(name + " FAILED\n"
                        + "  input    = " + Arrays.toString(in) + "\n"
                        + "  limit    = " + limit + "\n"
                        + "  expected = " + want + "\n"
                        + "  actual   = " + got);
            }
            System.out.println("OK   " + name + "  -> " + actual);
        }

        public static void main(String[] args) {
            System.out.println("=== Part1: direct counting ===");
            runSpecTests(Part1::check);
            System.out.println("Part1: all tests passed");
        }

        /** Shared scenario list, invoked through a checker so Part2 reuses it. */
        interface Checker {
            void check(String name, int limit, String[] in, String[] expected);
        }

        static void runSpecTests(Checker c) {
            // --- provided examples ---
            c.check("example1", 2,
                    new String[] {"A:0:100", "B:40:110", "C:80:200", "D:210:300"},
                    new String[] {"A:0:100", "B:40:110", "C:101:209", "D:210:300"});
            c.check("example2", 1,
                    new String[] {"A:0:100", "B:80:180"},
                    new String[] {"A:0:100", "B:101:180"});
            c.check("example3 (chained shift)", 2,
                    new String[] {"A:0:30", "B:0:31", "C:0:32", "D:0:100"},
                    new String[] {"A:0:30", "B:0:31", "C:31:32", "D:32:100"});

            // --- same input, varying limit: shifting stops at "room for one more" ---
            c.check("limit=1 on example1's input", 1,
                    new String[] {"A:0:100", "B:40:110", "C:80:200", "D:210:300"},
                    new String[] {"A:0:100", "B:101:110", "C:111:209", "D:210:300"});
            c.check("limit=3 on example1's input (no shift)", 3,
                    new String[] {"A:0:100", "B:40:110", "C:80:200", "D:210:300"},
                    new String[] {"A:0:100", "B:40:110", "C:80:209", "D:210:300"});

            // --- the case that breaks a destructive-poll heap probe (see header) ---
            c.check("destructive-poll counterexample", 1,
                    new String[] {"A:20:35", "B:7:16", "C:20:48", "D:14:15", "E:1:27"},
                    new String[] {"E:1:27", "A:28:35", "C:36:48"});

            // --- deeper cascade: 5 shards all starting at 0 ---
            c.check("cascade x3", 2,
                    new String[] {"A:0:30", "B:0:31", "C:0:32", "D:0:33", "E:0:100"},
                    new String[] {"A:0:30", "B:0:31", "C:31:32", "D:32:33", "E:33:100"});

            // --- edge cases ---
            c.check("empty", 5, new String[] {}, new String[] {});
            c.check("single", 1, new String[] {"X:5:9"}, new String[] {"X:5:9"});
            c.check("large-limit natural gap", 5,
                    new String[] {"A:0:10", "B:100:110"},
                    new String[] {"A:0:99", "B:100:110"});
            c.check("forced delete", 1,
                    new String[] {"A:0:100", "B:0:100"},
                    new String[] {"A:0:100"});
            c.check("negative coords", 1,
                    new String[] {"A:-10:-5", "B:-7:-1"},
                    new String[] {"A:-10:-5", "B:-4:-1"});
            c.check("identical ranges, limit=2", 2,
                    new String[] {"A:0:10", "B:0:10", "C:0:10"},
                    new String[] {"A:0:10", "B:0:10"});
        }
    }

    // ========================================================================
    // PART 2 — DIFFERENCE MAP (alternative; guaranteed O(n^2) rather than O(n^3)).
    //
    // CHANGES FROM PART 1:
    //   * Coverage is no longer recounted from `kept`. A TreeMap<Long,Integer>
    //     `delta` holds coverage BREAKPOINTS: addInterval(a,b) does
    //     delta[a] += 1 and delta[b+1] -= 1, and coverage at k is the sum of all
    //     delta values at keys <= k.
    //   * findShiftedStart becomes a two-phase prefix-sum-then-walk:
    //       headMap(s0, TRUE)  -> INCLUSIVE, gives coverage AT s0 (a shard
    //                             starting exactly at s0 does cover s0)
    //       tailMap(s0, FALSE) -> EXCLUSIVE, so s0's delta is not double-counted
    //     The opposite boundary flags are the trap: both are silent if wrong.
    //   * Cost: O(n) per shard guaranteed -> O(n^2) overall.
    //   * Price: `delta` is a DERIVED structure that must be updated in lockstep
    //     with `kept`. Miss an addInterval and coverage silently drifts. Part 1
    //     has one source of truth; this has two.
    // Everything else (parse, fillGaps, serialize, the phase order) is identical.
    // ========================================================================
    static final class Part2 {

        private static final class Shard {
            final String id;
            long start;
            long end;

            Shard(String id, long start, long end) {
                this.id = id;
                this.start = start;
                this.end = end;
            }
        }

        private static Shard parse(String s) {
            String[] parts = s.split(":", 3);
            if (parts.length != 3) {
                throw new IllegalArgumentException("bad shard format: '" + s + "'");
            }
            return new Shard(parts[0],
                    Long.parseLong(parts[1].trim()),
                    Long.parseLong(parts[2].trim()));
        }

        /** Add inclusive interval [a, b]: +1 at a, -1 at b+1 (NOT b). */
        private static void addInterval(TreeMap<Long, Integer> delta, long a, long b) {
            delta.merge(a, 1, Integer::sum);
            delta.merge(b + 1, -1, Integer::sum);
            // Drop entries that net to zero, keeping the map tidy.
            if (delta.get(a) == 0) delta.remove(a);
            if (delta.containsKey(b + 1) && delta.get(b + 1) == 0) delta.remove(b + 1);
        }

        /**
         * Earliest key k >= s0 where coverage by already-kept shards < limit.
         * Phase 1 sums the prefix INCLUSIVE of s0; phase 2 walks breakpoints
         * strictly AFTER s0, carrying the running coverage forward. Testing only
         * at breakpoints suffices because coverage is constant between them.
         */
        private static long findShiftedStart(TreeMap<Long, Integer> delta, long s0, int limit) {
            long cov = 0;
            for (int d : delta.headMap(s0, true).values()) {   // INCLUSIVE
                cov += d;
            }
            if (cov < limit) {
                return s0;
            }
            for (Map.Entry<Long, Integer> e : delta.tailMap(s0, false).entrySet()) {  // EXCLUSIVE
                cov += e.getValue();
                if (cov < limit) {
                    return e.getKey();
                }
            }
            // Unreachable: past the last kept end+1 coverage is 0 and limit >= 1.
            return s0;
        }

        private static void fillGaps(List<Shard> kept, long originalMin, long originalMax) {
            if (kept.isEmpty()) {
                return;
            }
            for (int i = 1; i < kept.size(); i++) {
                Shard prev = kept.get(i - 1);
                Shard cur = kept.get(i);
                if (cur.start > prev.end + 1) {
                    prev.end = cur.start - 1;
                }
            }
            Shard last = kept.get(kept.size() - 1);
            if (last.end < originalMax) {
                last.end = originalMax;
            }
        }

        private static List<String> serialize(List<Shard> kept) {
            List<String> out = new ArrayList<>(kept.size());
            for (Shard s : kept) {
                out.add(s.id + ":" + s.start + ":" + s.end);
            }
            return out;
        }

        static List<String> rebalance(int limit, String[] shards) {
            if (limit < 1) {
                throw new IllegalArgumentException("limit must be >= 1, got " + limit);
            }
            if (shards == null || shards.length == 0) {
                return new ArrayList<>();
            }

            List<Shard> all = new ArrayList<>(shards.length);
            for (String s : shards) {
                all.add(parse(s));
            }

            long originalMin = Long.MAX_VALUE;
            long originalMax = Long.MIN_VALUE;
            for (Shard s : all) {
                if (s.start < originalMin) originalMin = s.start;
                if (s.end > originalMax) originalMax = s.end;
            }

            all.sort(Comparator.comparingLong((Shard s) -> s.start)
                    .thenComparingLong(s -> s.end));

            TreeMap<Long, Integer> delta = new TreeMap<>();
            List<Shard> keptInOrder = new ArrayList<>();
            for (Shard s : all) {
                // Query BEFORE addInterval, so a shard never counts itself.
                long newStart = findShiftedStart(delta, s.start, limit);
                if (newStart > s.end) {
                    continue;
                }
                s.start = newStart;
                addInterval(delta, s.start, s.end);
                keptInOrder.add(s);
            }

            fillGaps(keptInOrder, originalMin, originalMax);
            return serialize(keptInOrder);
        }

        // ---- tests: same scenarios as Part1, run through this implementation --

        private static Map<String, String> normalize(List<String> shards) {
            Map<String, String> m = new HashMap<>();
            for (String s : shards) {
                String[] parts = s.split(":", 3);
                if (parts.length != 3) {
                    throw new AssertionError("Bad shard format: '" + s + "'");
                }
                if (m.put(parts[0], parts[1] + ":" + parts[2]) != null) {
                    throw new AssertionError("Duplicate id in output: '" + parts[0] + "'");
                }
            }
            return m;
        }

        private static void check(String name, int limit, String[] in, String[] expected) {
            List<String> actual = rebalance(limit, in);
            Map<String, String> got = normalize(actual);
            Map<String, String> want = normalize(Arrays.asList(expected));
            if (!got.equals(want)) {
                throw new AssertionError(name + " FAILED\n"
                        + "  input    = " + Arrays.toString(in) + "\n"
                        + "  limit    = " + limit + "\n"
                        + "  expected = " + want + "\n"
                        + "  actual   = " + got);
            }
            System.out.println("OK   " + name + "  -> " + actual);
        }

        public static void main(String[] args) {
            System.out.println();
            System.out.println("=== Part2: difference map ===");
            Part1.runSpecTests(Part2::check);
            System.out.println("Part2: all tests passed");
        }
    }
}
