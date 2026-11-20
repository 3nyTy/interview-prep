/*
 * =====================================================================================
 * Dependency Version Check  (OpenAI Staff-SWE coding question; ML Engineer; Medium freq)
 * =====================================================================================
 *
 * PROBLEM
 *   Given a list of dependency version strings ("{major}.{minor}.{patch}") and an API
 *   `isSupported(version) -> bool`, find the EARLIEST version that supports a feature.
 *   Versions arrive in LEXICOGRAPHIC order (e.g. "10.0.0" < "5.0.0" as strings), so they
 *   must be PARSED numerically and re-sorted chronologically before reasoning about them.
 *
 * FILE / STRUCTURE CONTRACT
 *   - Top-level class `DependencyVersionCheck` is PACKAGE-PRIVATE (no `public`). That is
 *     deliberate: the file name "A - Dependency Version Check.java" is not a legal Java
 *     identifier, and `javac` only forces the file name to match for a *public* top-level
 *     class. A package-private class can have any (legal-identifier) name, so we name it
 *     `DependencyVersionCheck` and the file compiles fine.
 *   - Part1 / Part2 / Part3 are each a `static final class` nested directly in the top
 *     level. EACH is 100% self-contained: its own Version type, parseVersion, isSupported
 *     mock, helpers, assertions, and `public static void main`. There are ZERO references
 *     between the parts and ZERO references to top-level helpers (the top level holds only
 *     the dispatcher `main` and the three nested classes). Litmus test: copy any single
 *     PartN class body into a fresh file as `public class Foo`, and it compiles & runs
 *     unchanged. This forces some DELIBERATE duplication; that is intentional.
 *   - Dispatcher `main` routes on args[0]: "part1"/"part2"/"part3" run that part; no arg
 *     (or anything unrecognized) runs all three in order with a banner before each.
 *   - Every check is a REAL assertion (we do NOT use the `assert` keyword, which is off
 *     unless the JVM is started with -ea). Each part has its own check(...) / expect(...)
 *     that throw AssertionError on mismatch and print "PASS: ..." otherwise.
 *   - JDK standard library only; Java 21 (records, switch expressions, var, List.of).
 *
 * PART 1 — Find earliest supporting version (baseline)
 *   Parse + sort the version strings chronologically (NO API calls for sorting), then do a
 *   linear scan in chronological order and return the first version for which the API says
 *   true; null if none. Numeric parsing is what fixes lexicographic-vs-chronological order
 *   ("10.0.0" sorts AFTER "5.0.0" numerically). Local cost O(N log N); API cost O(K)<=O(N).
 *
 * PART 2 — Non-monotonic support (CHANGES FROM PART 1)
 *   Discovery: support is NOT monotonic — a version may support while the next does not,
 *   and support can regress within a patch/minor. The ALGORITHM IS UNCHANGED (sorted linear
 *   scan returning the first true): once sorted chronologically, the first supported version
 *   IS the absolute earliest, regardless of later regressions. What changes is the REASONING:
 *   binary search is now UNSAFE because a `false` no longer eliminates a whole range. The
 *   "if some version supports, eventually a later one also supports" guarantee does not alter
 *   this baseline. Adds an `analyzePattern` table-printer to study the observed pattern.
 *
 * PART 3 — Rate-limited API (CHANGES FROM PART 2)
 *   New constraint: `isSupported` is rate-limited; we cannot afford O(N) calls — we need
 *   SUB-LINEAR API usage. Binary search needs a monotonic predicate, but Part 2 showed
 *   regressions, so we fork:
 *     - Approach A (flat first-true binary search): valid when support is globally monotonic
 *       across the sorted order. O(log N) API calls. Most common interview path.
 *     - Approach B (hierarchical / group-level search): valid under weaker structure — the
 *       "representative" property (if ANY version in a group supports, the group's LATEST
 *       version supports, so a false latest lets us skip the whole group) plus the
 *       "monotonic" property (latest-per-major is monotonic, and latest-per-minor within a
 *       major is monotonic). Binary search on latest-per-major -> binary search on
 *       latest-per-minor within the target major -> LINEAR SCAN through the patches of the
 *       target minor (patches can still regress, so binary search is invalid there).
 *       O(log M + log Mi + P) API calls.
 *     - Fully non-monotonic with NO structure: a linear scan is unavoidable in the worst case.
 *   Part 3 implements BOTH A and B with an API call COUNTER to prove sub-linear behavior, and
 *   a small memoizing wrapper to demonstrate the caching follow-up.
 *
 * FOLLOW-UPS (discussed in comments; only the cheap memoization is implemented, in Part3)
 *   1. Caching/memoization of API results — cheap; see MemoizingApi in Part3. A repeated
 *      query must not re-hit the underlying (rate-limited) API.
 *   2. Non-semver versions ("v2.1-beta", "2.1rc1"): strip a leading 'v', split on '.'/'-',
 *      parse the numeric core, and rank pre-release tags (alpha < beta < rc < release) as a
 *      secondary key. parseVersion would return a richer record; comparison gets a tie-break.
 *   3. API failure / timeout: bounded exponential backoff with jitter and a max retry count.
 *      Treat a persistent failure as UNKNOWN (propagate / surface), never silently coerce it
 *      to `false` — coercing to false could make us skip the true earliest version.
 *   4. Parallelism: the binary-search levels are inherently sequential (each probe depends on
 *      the previous), but the final patch LINEAR SCAN (and "find ALL" sweeps) can be fanned
 *      out with virtual threads / CompletableFuture, gated by a Semaphore sized to the rate
 *      limit. Java 21 virtual threads make the bounded-concurrency version cheap.
 *   5. Find ALL supporting versions: with a rate limit this is O(N) in the worst case unless
 *      the representative property lets us prune whole non-supporting groups.
 *   6. Millions of versions: sort is O(N log N) time / O(N) memory; group-level pruning shrinks
 *      the number of API calls; for truly huge inputs use streaming/external sort and a
 *      persistent cache of API results.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

class DependencyVersionCheck {

    /**
     * Dispatcher. Routes on args[0]; runs everything when no/unknown arg is given.
     * Each part is invoked with a fresh empty argv so the parts stay independent.
     */
    public static void main(String[] args) {
        String which = args.length > 0 ? args[0].toLowerCase() : "all";
        switch (which) {
            case "part1" -> { banner(1); Part1.main(new String[0]); }
            case "part2" -> { banner(2); Part2.main(new String[0]); }
            case "part3" -> { banner(3); Part3.main(new String[0]); }
            default -> {
                banner(1); Part1.main(new String[0]);
                banner(2); Part2.main(new String[0]);
                banner(3); Part3.main(new String[0]);
                System.out.println();
                System.out.println("ALL PARTS PASSED");
            }
        }
    }

    private static void banner(int part) {
        System.out.println();
        System.out.println("===== PART " + part + " =====");
    }

    // =================================================================================
    // PART 1 — sorted linear scan (self-contained)
    // =================================================================================
    static final class Part1 {

        /** Semantic version with numeric components; keeps the ORIGINAL string out of band. */
        record Version(int major, int minor, int patch) implements Comparable<Version> {
            @Override
            public int compareTo(Version o) {
                int c = Integer.compare(major, o.major);
                if (c != 0) return c;
                c = Integer.compare(minor, o.minor);
                if (c != 0) return c;
                return Integer.compare(patch, o.patch);
            }
        }

        /**
         * Parse "{major}.{minor}.{patch}" into numeric components.
         * NOTE: split("\\.") escapes the dot — split(".") would be "any char" regex and is wrong.
         * Integer.parseInt handles leading zeros for free and is decimal, NOT octal:
         * parseInt("003")==3, parseInt("02")==2.
         * Assumption: exactly 3 numeric dot-separated parts; malformed input may throw (acceptable
         * here). For the non-semver follow-up we'd relax this (strip a leading 'v', split on './-',
         * rank pre-release tags).
         */
        static Version parseVersion(String s) {
            String[] parts = s.split("\\.");
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);
            return new Version(major, minor, patch);
        }

        /** The (mockable) rate-unaware support API for Part 1. */
        interface SupportApi {
            boolean isSupported(String version);
        }

        /**
         * Sort the version strings chronologically by parsed numeric key (no API calls), then
         * scan in order and return the first ORIGINAL string the API supports; null if none.
         * Local: O(N log N) sort. API: O(K), worst O(N). Space: O(N).
         */
        static String findEarliestSupportedVersion(List<String> versions, SupportApi api) {
            List<String> sorted = new ArrayList<>(versions);
            sorted.sort(Comparator.comparing(Part1::parseVersion));
            for (String v : sorted) {
                if (api.isSupported(v)) {
                    return v; // first true in chronological order == absolute earliest
                }
            }
            return null;
        }

        public static void main(String[] args) {
            // --- parsing / leading-zero asserts ---------------------------------------
            expect(parseVersion("103.003.02"), new Version(103, 3, 2), "parse leading zeros 103.003.02");
            expect(parseVersion("103.010.01"), new Version(103, 10, 1), "parse leading zeros 103.010.01");

            // --- worked example from the prompt ---------------------------------------
            // 1.0.1->F, 1.0.2->F, 1.1.0->T, 2.0.0->T, 2.0.1->T  => "1.1.0"
            expect(findEarliestSupportedVersion(
                    List.of("1.0.1", "1.0.2", "1.1.0", "2.0.0", "2.0.1"),
                    supportedBy(Set.of("1.1.0", "2.0.0", "2.0.1"))),
                    "1.1.0", "main example -> 1.1.0");

            // --- Test Your Understanding ----------------------------------------------
            expect(findEarliestSupportedVersion(
                    List.of("1.0.0", "1.0.1", "1.0.2"),
                    supportedBy(Set.of("1.0.1", "1.0.2"))),
                    "1.0.1", "TYU1 -> 1.0.1");
            expect(findEarliestSupportedVersion(
                    List.of("1.0.0", "1.0.1", "1.0.2", "1.0.3"),
                    supportedBy(Set.of("1.0.2", "1.0.3"))),
                    "1.0.2", "TYU2 -> 1.0.2");

            // --- sparse versions: proves numeric (not lexicographic) ordering ----------
            // Input deliberately given out of order ("10.0.0" first) to prove we sort.
            List<String> sparse = List.of("10.0.0", "1.0.0", "5.0.0");
            expect(findEarliestSupportedVersion(sparse, supportedBy(Set.of("5.0.0"))),
                    "5.0.0", "sparse -> 5.0.0 (numeric sort)");
            // After sorting, "5.0.0" must precede "10.0.0" (lexicographically it would not).
            List<String> sortedSparse = new ArrayList<>(sparse);
            sortedSparse.sort(Comparator.comparing(Part1::parseVersion));
            check(sortedSparse.indexOf("5.0.0") < sortedSparse.indexOf("10.0.0"),
                    "sorted index of 5.0.0 precedes 10.0.0");

            // --- all unsupported -> null ----------------------------------------------
            expect(findEarliestSupportedVersion(
                    List.of("1.0.0", "2.0.0", "3.0.0"), supportedBy(Set.of())),
                    null, "all unsupported -> null");

            // --- single supported / single unsupported / empty ------------------------
            expect(findEarliestSupportedVersion(List.of("1.0.0"), supportedBy(Set.of("1.0.0"))),
                    "1.0.0", "single supported -> 1.0.0");
            expect(findEarliestSupportedVersion(List.of("1.0.0"), supportedBy(Set.of())),
                    null, "single unsupported -> null");
            expect(findEarliestSupportedVersion(List.of(), supportedBy(Set.of())),
                    null, "empty -> null");

            // --- leading-zero original string preserved -------------------------------
            // The returned value must be the ORIGINAL "103.003.02", not a reformatted "103.3.2".
            expect(findEarliestSupportedVersion(
                    List.of("103.003.02", "103.003.03", "103.010.01"),
                    supportedBy(Set.of("103.003.02"))),
                    "103.003.02", "earliest preserves original leading-zero string");

            System.out.println("Part1: all checks passed");
        }

        /** Build a mock API from a set of supported version strings. */
        private static SupportApi supportedBy(Set<String> supported) {
            return supported::contains;
        }

        // ---- self-contained assertion helpers ----
        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError("FAIL: " + label);
            System.out.println("PASS: " + label);
        }

        private static void expect(Object actual, Object expected, String label) {
            if (!Objects.equals(actual, expected)) {
                throw new AssertionError("FAIL: " + label + " (expected=" + expected + ", actual=" + actual + ")");
            }
            System.out.println("PASS: " + label + " -> " + actual);
        }
    }

    // =================================================================================
    // PART 2 — non-monotonic support (self-contained)
    //
    // CHANGES FROM PART 1:
    //   - Support is NO LONGER guaranteed monotonic: a version may support while the next
    //     does not, and support can regress within a patch or minor.
    //   - Consequence: BINARY SEARCH IS NOW UNSAFE. A `false` result no longer proves that
    //     every later version is also false, so a false can't be used to discard a range.
    //   - BUT the algorithm is UNCHANGED: a sorted linear scan returning the first `true`.
    //     Once versions are sorted chronologically, the first supported one IS the absolute
    //     earliest, even if later versions regress. The "eventually some later version also
    //     supports" guarantee does not change this baseline.
    //   - Full sort is still required because the input is lexicographic, not chronological.
    //   - Adds analyzePattern(...) to print an observed "Version | Supported" table.
    //   - Complexity is intentionally identical to Part 1: local O(N log N), API O(K)<=O(N).
    // =================================================================================
    static final class Part2 {

        record Version(int major, int minor, int patch) implements Comparable<Version> {
            @Override
            public int compareTo(Version o) {
                int c = Integer.compare(major, o.major);
                if (c != 0) return c;
                c = Integer.compare(minor, o.minor);
                if (c != 0) return c;
                return Integer.compare(patch, o.patch);
            }
        }

        /** See Part1.parseVersion — split("\\.") escapes the dot; parseInt is decimal (leading zeros OK). */
        static Version parseVersion(String s) {
            String[] parts = s.split("\\.");
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);
            return new Version(major, minor, patch);
        }

        interface SupportApi {
            boolean isSupported(String version);
        }

        /**
         * Identical strategy to Part 1, renamed to emphasize the new setting. Sort by numeric
         * key, scan, return the first supported ORIGINAL string. Non-monotonicity does NOT mean
         * we keep scanning past the first true — the first true is already the earliest. It only
         * means we may NOT skip ranges based on a false (which is why binary search is invalid).
         */
        static String findEarliestWithRegressions(List<String> versions, SupportApi api) {
            List<String> sorted = new ArrayList<>(versions);
            sorted.sort(Comparator.comparing(Part2::parseVersion));
            for (String v : sorted) {
                if (api.isSupported(v)) {
                    return v;
                }
            }
            return null;
        }

        /**
         * Diagnostic helper: sort by numeric key and print a "Version | Supported" table so we
         * can eyeball the (possibly non-monotonic) pattern.
         *
         * Questions to confirm with the interviewer about how far support can regress:
         *   - Can support regress within a patch series (same major.minor)?
         *   - Can it regress across minors within a major?
         *   - Are the patterns consistent across majors, or per-major?
         * The answers decide whether Part 3 can use Approach A, Approach B, or neither.
         */
        static void analyzePattern(List<String> versions, SupportApi api) {
            List<String> sorted = new ArrayList<>(versions);
            sorted.sort(Comparator.comparing(Part2::parseVersion));
            System.out.println("Version          | Supported");
            System.out.println("-".repeat(35));
            for (String v : sorted) {
                System.out.printf("%-15s | %s%n", v, api.isSupported(v));
            }
        }

        public static void main(String[] args) {
            // --- parsing / leading-zero asserts ---------------------------------------
            expect(parseVersion("103.003.02"), new Version(103, 3, 2), "parse leading zeros 103.003.02");
            expect(parseVersion("103.010.01"), new Version(103, 10, 1), "parse leading zeros 103.010.01");

            // --- non-monotonic example from the prompt --------------------------------
            // 103.003.02->T, 103.003.03->F, 103.003.04->T  => earliest "103.003.02".
            // No monotonicity is assumed by the mock (it's a plain set membership test).
            List<String> nm = List.of("103.003.02", "103.003.03", "103.003.04");
            SupportApi nmApi = supportedBy(Set.of("103.003.02", "103.003.04"));
            System.out.println("analyzePattern on non-monotonic example:");
            analyzePattern(nm, nmApi);
            expect(findEarliestWithRegressions(nm, nmApi), "103.003.02",
                    "non-monotonic 103.003.* -> 103.003.02 (earliest despite regression)");

            // --- test_non_monotonic_support: regression in the middle -----------------
            // 1.0.0->F, 1.0.1->T, 1.0.2->F (regression), 1.0.3->T  => "1.0.1".
            // This BREAKS a naive binary search: probing the middle (1.0.1/1.0.2) and seeing a
            // false at 1.0.2 would wrongly suggest "go left", and a false elsewhere can't prune.
            expect(findEarliestWithRegressions(
                    List.of("1.0.0", "1.0.1", "1.0.2", "1.0.3"),
                    supportedBy(Set.of("1.0.1", "1.0.3"))),
                    "1.0.1", "non-monotonic 1.0.* -> 1.0.1 (binary search would fail here)");

            // --- carry-forward correctness from Part 1 --------------------------------
            expect(findEarliestWithRegressions(
                    List.of("1.0.1", "1.0.2", "1.1.0", "2.0.0", "2.0.1"),
                    supportedBy(Set.of("1.1.0", "2.0.0", "2.0.1"))),
                    "1.1.0", "main example -> 1.1.0");
            expect(findEarliestWithRegressions(
                    List.of("1.0.0", "2.0.0", "3.0.0"), supportedBy(Set.of())),
                    null, "all unsupported -> null");
            expect(findEarliestWithRegressions(
                    List.of("10.0.0", "1.0.0", "5.0.0"), supportedBy(Set.of("5.0.0"))),
                    "5.0.0", "sparse -> 5.0.0 (numeric sort)");

            // --- single / empty -------------------------------------------------------
            expect(findEarliestWithRegressions(List.of("1.0.0"), supportedBy(Set.of("1.0.0"))),
                    "1.0.0", "single supported -> 1.0.0");
            expect(findEarliestWithRegressions(List.of("1.0.0"), supportedBy(Set.of())),
                    null, "single unsupported -> null");
            expect(findEarliestWithRegressions(List.of(), supportedBy(Set.of())),
                    null, "empty -> null");

            System.out.println("Part2: all checks passed");
        }

        private static SupportApi supportedBy(Set<String> supported) {
            return supported::contains;
        }

        // ---- self-contained assertion helpers ----
        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError("FAIL: " + label);
            System.out.println("PASS: " + label);
        }

        private static void expect(Object actual, Object expected, String label) {
            if (!Objects.equals(actual, expected)) {
                throw new AssertionError("FAIL: " + label + " (expected=" + expected + ", actual=" + actual + ")");
            }
            System.out.println("PASS: " + label + " -> " + actual);
        }
    }

    // =================================================================================
    // PART 3 — rate-limited API; BOTH approaches (self-contained)
    //
    // CHANGES FROM PART 2:
    //   - `isSupported` is RATE-LIMITED, so O(N) calls is not acceptable; we must be
    //     SUB-LINEAR in API calls.
    //   - Binary search needs a monotonic predicate, but Part 2 showed support can regress.
    //     Fork by what we can assume:
    //       * Globally monotonic  -> Approach A: flat first-true binary search, O(log N) API.
    //       * Group structure only -> Approach B: hierarchical search, O(log M + log Mi + P) API,
    //         using (a) the REPRESENTATIVE property [if any version in a group supports, the
    //         group's LATEST supports, so a false latest skips the whole group] and (b) the
    //         MONOTONIC property [latest-per-major is monotonic; latest-per-minor within a major
    //         is monotonic]. The final PATCH stage is a LINEAR SCAN because patches can regress.
    //       * Fully non-monotonic, no structure -> linear scan, unavoidable in the worst case.
    //   - We add an API call COUNTER (CountingApi) to PROVE sub-linear behavior, and a tiny
    //     MemoizingApi to demonstrate the caching follow-up (a repeated query must not re-hit
    //     the underlying counter).
    //   - Why the patch stage is linear and not binary: if we binary-searched EVERY level we'd
    //     be assuming global monotonicity — in which case Approach A is simpler and preferable.
    //
    // Complexity:
    //   - Approach A: assumes globally monotonic -> O(log N) API calls.
    //   - Approach B: assumes only latest-per-group monotonic -> O(log M + log Mi + P) API calls.
    //   - Both: Time O(N log N) sort + O(N) grouping; Space O(N).
    // =================================================================================
    static final class Part3 {

        record Version(int major, int minor, int patch) implements Comparable<Version> {
            @Override
            public int compareTo(Version o) {
                int c = Integer.compare(major, o.major);
                if (c != 0) return c;
                c = Integer.compare(minor, o.minor);
                if (c != 0) return c;
                return Integer.compare(patch, o.patch);
            }
        }

        /** Parsed key paired with the ORIGINAL string, so grouping/sorting never loses formatting. */
        record V(Version key, String original) {}

        /** See Part1.parseVersion — split("\\.") escapes the dot; parseInt is decimal (leading zeros OK). */
        static Version parseVersion(String s) {
            String[] parts = s.split("\\.");
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);
            return new Version(major, minor, patch);
        }

        /** Rate-limited support API mock that counts every call so tests can assert sub-linearity. */
        static final class CountingApi {
            private final Set<String> supported;
            int calls = 0;

            CountingApi(Set<String> s) {
                this.supported = s;
            }

            boolean isSupported(String v) {
                calls++;
                return supported.contains(v);
            }

            void reset() {
                calls = 0;
            }
        }

        /**
         * Follow-up #1 (caching): memoize results so a repeated query does not re-hit the
         * rate-limited underlying API. Backed by a HashMap over a CountingApi.
         */
        static final class MemoizingApi {
            private final CountingApi delegate;
            private final Map<String, Boolean> cache = new HashMap<>();

            MemoizingApi(CountingApi delegate) {
                this.delegate = delegate;
            }

            boolean isSupported(String v) {
                Boolean cached = cache.get(v);
                if (cached != null) return cached;
                boolean r = delegate.isSupported(v);
                cache.put(v, r);
                return r;
            }

            int underlyingCalls() {
                return delegate.calls;
            }
        }

        /**
         * Generic "first index where predicate is true" binary search over [0, n).
         * Returns the smallest such index, or -1 if none (the -1 plays the role of Python's None).
         * Overflow-safe midpoint (left + (right-left)/2). When n==0, right starts at -1 and the
         * loop body never runs, so the predicate is never invoked (0 API calls on empty input).
         */
        static int binarySearchFirstTrue(int n, java.util.function.IntPredicate predicate) {
            int left = 0, right = n - 1, result = -1;
            while (left <= right) {
                int mid = left + (right - left) / 2; // overflow-safe
                if (predicate.test(mid)) {
                    result = mid;
                    right = mid - 1; // record and keep searching left for an earlier true
                } else {
                    left = mid + 1;
                }
            }
            return result;
        }

        /**
         * APPROACH A — flat first-true binary search across all sorted versions.
         * CORRECT ONLY if support is globally monotonic over the sorted order. O(log N) API calls.
         */
        static String findEarliestFlatBinary(List<String> versions, CountingApi api) {
            List<V> sorted = toSortedV(versions);
            int idx = binarySearchFirstTrue(sorted.size(), i -> api.isSupported(sorted.get(i).original()));
            return idx < 0 ? null : sorted.get(idx).original();
        }

        /**
         * APPROACH B — hierarchical / group-level search for the non-monotonic-with-structure case.
         *
         * Properties relied upon:
         *   - REPRESENTATIVE: if ANY version in a (major / minor) group supports the feature, then
         *     the LATEST version in that group also supports it. So if a group's latest is false,
         *     the whole group can be skipped.
         *   - MONOTONIC: the latest-per-major sequence is monotonic, and within the chosen major the
         *     latest-per-minor sequence is monotonic. That makes binary search valid at those levels.
         *
         * Strategy:
         *   1. Binary-search the latest-per-major list for the first supporting major.
         *   2. Within that major, binary-search the latest-per-minor list for the first supporting minor.
         *   3. Within that minor, LINEAR-SCAN the patches in order — patches can regress, so binary
         *      search is invalid here (and binary-searching all levels would assume global monotonicity,
         *      i.e. Approach A). Return the first supporting patch.
         *
         * API calls: O(log M + log Mi + P), where M = #majors, Mi = #minors in the target major,
         * P = #patches in the target minor.
         */
        static String findEarliestGroupSearch(List<String> versions, CountingApi api) {
            List<V> sorted = toSortedV(versions);

            // Group by major; TreeMap keeps majors ascending. Members are already key-sorted, so the
            // LAST element of each group's list is that group's LATEST version.
            TreeMap<Integer, List<V>> majorGroups = new TreeMap<>();
            for (V v : sorted) {
                majorGroups.computeIfAbsent(v.key().major(), k -> new ArrayList<>()).add(v);
            }
            List<Integer> sortedMajors = new ArrayList<>(majorGroups.keySet());
            List<V> latestPerMajor = new ArrayList<>();
            for (int m : sortedMajors) {
                List<V> grp = majorGroups.get(m);
                latestPerMajor.add(grp.get(grp.size() - 1));
            }

            int mi = binarySearchFirstTrue(latestPerMajor.size(),
                    i -> api.isSupported(latestPerMajor.get(i).original()));
            if (mi < 0) return null; // no major's latest supports => nothing supports
            int targetMajor = sortedMajors.get(mi);

            // Within the target major, group by minor; TreeMap keeps minors ascending.
            TreeMap<Integer, List<V>> minorGroups = new TreeMap<>();
            for (V v : majorGroups.get(targetMajor)) {
                minorGroups.computeIfAbsent(v.key().minor(), k -> new ArrayList<>()).add(v);
            }
            List<Integer> sortedMinors = new ArrayList<>(minorGroups.keySet());
            List<V> latestPerMinor = new ArrayList<>();
            for (int mn : sortedMinors) {
                List<V> grp = minorGroups.get(mn);
                latestPerMinor.add(grp.get(grp.size() - 1));
            }

            int ni = binarySearchFirstTrue(latestPerMinor.size(),
                    i -> api.isSupported(latestPerMinor.get(i).original()));
            if (ni < 0) return null; // representative property guarantees this shouldn't happen, but be safe
            int targetMinor = sortedMinors.get(ni);

            // Patch stage: LINEAR scan (patches can regress; binary search would be invalid here).
            for (V v : minorGroups.get(targetMinor)) {
                if (api.isSupported(v.original())) {
                    return v.original();
                }
            }
            return null;
        }

        /** Parse + sort by numeric key, pairing each parsed key with its original string. */
        private static List<V> toSortedV(List<String> versions) {
            List<V> list = new ArrayList<>(versions.size());
            for (String s : versions) {
                list.add(new V(parseVersion(s), s));
            }
            list.sort(Comparator.comparing(V::key));
            return list;
        }

        public static void main(String[] args) {
            // --- parsing / leading-zero asserts ---------------------------------------
            expect(parseVersion("103.003.02"), new Version(103, 3, 2), "parse leading zeros 103.003.02");
            expect(parseVersion("103.010.01"), new Version(103, 10, 1), "parse leading zeros 103.010.01");

            // =========================================================================
            // APPROACH A headline: the 12-version monotonic flat walkthrough.
            // Everything < 2.1.0 -> False; {2.1.0, 2.1.1, 3.0.0, 3.0.1} -> True. Earliest "2.1.0".
            // The prompt notes the binary search lands "2.1.0" in 4 probes vs 12 linear calls.
            // =========================================================================
            List<String> flat = List.of(
                    "1.5.0", "1.5.1", "1.5.2", "1.6.0", "1.6.1",
                    "2.0.0", "2.0.1", "2.0.2", "2.1.0", "2.1.1",
                    "3.0.0", "3.0.1");
            CountingApi flatApi = new CountingApi(Set.of("2.1.0", "2.1.1", "3.0.0", "3.0.1"));
            expect(findEarliestFlatBinary(flat, flatApi), "2.1.0", "A: flat 12-version walkthrough -> 2.1.0");
            check(flatApi.calls <= 4, "A: flat walkthrough used <= 4 API calls (was " + flatApi.calls + ")");
            check(flatApi.calls < flat.size(), "A: flat walkthrough sub-linear (" + flatApi.calls + " < " + flat.size() + ")");

            // test_monotonic_support: 1.0.0,1.0.1->F; 1.0.2,2.0.0->T => "1.0.2".
            CountingApi monoApi = new CountingApi(Set.of("1.0.2", "2.0.0"));
            expect(findEarliestFlatBinary(List.of("1.0.0", "1.0.1", "1.0.2", "2.0.0"), monoApi),
                    "1.0.2", "A: test_monotonic_support -> 1.0.2");
            check(monoApi.calls < 4, "A: test_monotonic_support sub-linear (" + monoApi.calls + " < 4)");

            // =========================================================================
            // APPROACH B: non-monotonic WITH group structure.
            // latest-per-major: 1.0.1=F, 2.0.2=T, 3.0.1=T (monotonic).
            // within major 2, minor 0, latest 2.0.2=T; patch scan 2.0.0 -> T (earliest) => "2.0.0".
            // 2.0.1 is a genuine regression sitting between two Trues (2.0.0=T, 2.0.2=T).
            // NOTE: we deliberately do NOT assert findEarliestFlatBinary on THIS set — flat binary
            // is INVALID here because 2.0.1=False between two Trues violates global monotonicity,
            // so its result would be brittle/meaningless.
            // =========================================================================
            List<String> grouped = List.of("1.0.0", "1.0.1", "2.0.0", "2.0.1", "2.0.2", "3.0.0", "3.0.1");
            Set<String> groupedSupported = Set.of("2.0.0", "2.0.2", "3.0.0", "3.0.1");
            check(!groupedSupported.contains("2.0.1"), "B: 2.0.1 is a genuine regression (unsupported between supported)");
            CountingApi groupApi = new CountingApi(groupedSupported);
            expect(findEarliestGroupSearch(grouped, groupApi), "2.0.0", "B: group search -> 2.0.0 (earliest)");
            check(groupApi.calls <= 6, "B: group search used <= 6 API calls (was " + groupApi.calls + ")");
            check(groupApi.calls < grouped.size(), "B: group search sub-linear (" + groupApi.calls + " < " + grouped.size() + ")");

            // =========================================================================
            // Edge cases for both approaches.
            // =========================================================================
            // all unsupported -> both null, and fewer than N calls.
            CountingApi allUnsupA = new CountingApi(Set.of());
            expect(findEarliestFlatBinary(List.of("1.0.0", "2.0.0", "3.0.0"), allUnsupA), null, "A: all unsupported -> null");
            check(allUnsupA.calls < 3, "A: all unsupported sub-linear (" + allUnsupA.calls + " < 3)");
            CountingApi allUnsupB = new CountingApi(Set.of());
            expect(findEarliestGroupSearch(List.of("1.0.0", "2.0.0", "3.0.0"), allUnsupB), null, "B: all unsupported -> null");
            check(allUnsupB.calls < 3, "B: all unsupported sub-linear (" + allUnsupB.calls + " < 3)");

            // sparse: given out of lexicographic order; numeric sort fixes it. {5.0.0, 10.0.0} supported.
            List<String> sparse = List.of("1.0.0", "10.0.0", "5.0.0");
            CountingApi sparseA = new CountingApi(Set.of("5.0.0", "10.0.0"));
            expect(findEarliestFlatBinary(sparse, sparseA), "5.0.0", "A: sparse -> 5.0.0");
            CountingApi sparseB = new CountingApi(Set.of("5.0.0", "10.0.0"));
            expect(findEarliestGroupSearch(sparse, sparseB), "5.0.0", "B: sparse -> 5.0.0");

            // single supported / unsupported.
            CountingApi singleSupA = new CountingApi(Set.of("1.0.0"));
            expect(findEarliestFlatBinary(List.of("1.0.0"), singleSupA), "1.0.0", "A: single supported -> 1.0.0");
            CountingApi singleUnsupA = new CountingApi(Set.of());
            expect(findEarliestFlatBinary(List.of("1.0.0"), singleUnsupA), null, "A: single unsupported -> null");
            CountingApi singleSupB = new CountingApi(Set.of("1.0.0"));
            expect(findEarliestGroupSearch(List.of("1.0.0"), singleSupB), "1.0.0", "B: single supported -> 1.0.0");

            // empty -> null for both, and the predicate is never called (0 calls).
            CountingApi emptyA = new CountingApi(Set.of());
            expect(findEarliestFlatBinary(List.of(), emptyA), null, "A: empty -> null");
            check(emptyA.calls == 0, "A: empty made 0 API calls (binarySearchFirstTrue(0,..) skips predicate)");
            CountingApi emptyB = new CountingApi(Set.of());
            expect(findEarliestGroupSearch(List.of(), emptyB), null, "B: empty -> null");
            check(emptyB.calls == 0, "B: empty made 0 API calls");

            // leading-zero original-string preservation (monotonic so flat binary is valid).
            CountingApi lz = new CountingApi(Set.of("103.003.02", "103.003.03", "103.010.01"));
            expect(findEarliestFlatBinary(List.of("103.003.02", "103.003.03", "103.010.01"), lz),
                    "103.003.02", "A: earliest preserves original leading-zero string");

            // =========================================================================
            // Follow-up #1 (caching): a repeated query must NOT increment the underlying counter.
            // =========================================================================
            CountingApi backing = new CountingApi(Set.of("1.0.0"));
            MemoizingApi memo = new MemoizingApi(backing);
            boolean first = memo.isSupported("1.0.0");
            int afterFirst = memo.underlyingCalls();
            boolean second = memo.isSupported("1.0.0"); // served from cache
            int afterSecond = memo.underlyingCalls();
            check(first && second, "memo: cached value is correct (true both times)");
            check(afterFirst == 1, "memo: first query hit underlying API exactly once");
            check(afterSecond == 1, "memo: repeated query did NOT re-hit underlying API");

            System.out.println("Part3: all checks passed");
        }

        // ---- self-contained assertion helpers ----
        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError("FAIL: " + label);
            System.out.println("PASS: " + label);
        }

        private static void expect(Object actual, Object expected, String label) {
            if (!Objects.equals(actual, expected)) {
                throw new AssertionError("FAIL: " + label + " (expected=" + expected + ", actual=" + actual + ")");
            }
            System.out.println("PASS: " + label + " -> " + actual);
        }
    }
}
