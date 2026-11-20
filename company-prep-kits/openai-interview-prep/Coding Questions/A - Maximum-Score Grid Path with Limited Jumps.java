/*
 * =====================================================================================
 * Maximum-Score Grid Path with Limited Jumps  (OpenAI coding question; SWE / MLE)
 * =====================================================================================
 *
 * PROBLEM
 *   Given an N x M integer grid `board`, a starting column `p`, and a budget of at most
 *   K "two-row jumps", walk from (0, p) down to the LAST ROW maximizing the sum of the
 *   values of the cells you visit.
 *     From (i, j) the legal moves are:
 *       (i+1, j-1)   down-left
 *       (i+1, j)     down
 *       (i+1, j+1)   down-right
 *       (i+2, j)     SPECIAL two-row jump — usable at most K times over the whole path
 *     Any move landing outside the grid is illegal. The path ends when it reaches row N-1.
 *   Part 1 returns the max score, Part 2 returns one optimal path, Part 3 counts all
 *   optimal paths mod 1e9+7, Part 4 adds two history-dependent bonuses.
 *
 * FILE / STRUCTURE CONTRACT  (same convention as the other answers in this folder)
 *   - Top-level class `MaximumScoreGridPath` is PACKAGE-PRIVATE (no `public`). That is
 *     deliberate: the file name "A - Maximum-Score Grid Path with Limited Jumps.java" is
 *     not a legal Java identifier, and `javac` only forces file name == class name for a
 *     *public* top-level class. A package-private class may have any legal name.
 *   - Part1 / Part2 / Part3 / Part4 are each a `static final class` nested at the top
 *     level. EACH is 100% self-contained: its own DP, its own brute-force oracle, its own
 *     validation, its own assertions, its own `public static void main`. There are ZERO
 *     references between parts and ZERO references to top-level helpers. Litmus test: copy
 *     any single PartN body into a fresh file as `public class Foo` and it compiles and
 *     runs unchanged. This forces DELIBERATE duplication; that is intentional.
 *   - Dispatcher `main` routes on args[0]: "part1".."part4"; no arg runs all four in order.
 *   - Every check is a REAL assertion (we do NOT use the `assert` keyword, which is off
 *     unless the JVM runs with -ea). Each part throws AssertionError on mismatch and prints
 *     "PASS: ..." otherwise. Every part also FUZZES its DP against an exponential
 *     brute-force enumerator on hundreds of random boards.
 *   - JDK standard library only; Java 21 (records, switch expressions, var).
 *   - Scores are accumulated in `long`, not `int`: the base score is a sum of up to N cell
 *     values, so a tall grid with large values overflows `int`. Cheap insurance.
 *
 * THE CORE MODEL (say this out loud before writing code)
 *   Every legal move strictly increases the row index, so the grid is a DAG layered by row
 *   and there is no cycle and no revisiting. That is what makes a plain row-by-row DP valid
 *   rather than needing Dijkstra/Bellman-Ford — even with NEGATIVE cell values.
 *   The jump budget is not a constraint to be patched on afterwards; it is a DP DIMENSION.
 *   State = (row, col, jumps still available). Answer = state (0, p, K).
 *
 *   Direction: Parts 1-3 use a BACKWARD (suffix) DP, `best[i][j][k]` = best score of the
 *   remainder of a path that is standing on (i, j) with k jumps left. Two reasons:
 *     (a) the answer is a SINGLE cell, best[0][p][K], not a max over the last row; and
 *     (b) reconstruction then walks FORWARD from the start, which is the natural order to
 *         emit a path in.
 *
 * PART 1 — maximum score
 *   best[N-1][j][k] = board[N-1][j]                                    (already at the end)
 *   best[i][j][k]   = board[i][j] + max( best[i+1][j-1][k],            if j-1 >= 0
 *                                        best[i+1][j][k],
 *                                        best[i+1][j+1][k],            if j+1 <  M
 *                                        best[i+2][j][k-1] )           if k >= 1 && i+2 <= N-1
 *   Answer: best[0][p][K].  Time O(N*M*K), space O(N*M*K) — or O(M*K) with a 3-row window
 *   (Part 1 only; Parts 2-3 need the whole table).
 *   NOTE — no dead states: for any i <= N-2 the plain "down" move (i+1, j) is always legal,
 *   so every state has at least one successor and no -infinity ever propagates. We still
 *   keep a NEG sentinel because it costs nothing and documents the intent.
 *
 * PART 2 — reconstruct one optimal path  (CHANGES FROM PART 1)
 *   The score recurrence is UNTOUCHED. We only record, per state, WHICH successor attained
 *   the max (`choice[i][j][k]`), then walk forward from (0, p, K) following those codes,
 *   decrementing k on a jump, until we land on row N-1. Two independent implementations are
 *   provided and cross-checked against each other:
 *     (a) choice-array parent tracking  — O(N*M*K) extra memory, O(N) walk;
 *     (b) re-derivation from `best` alone — ZERO extra memory: at (i, j, k) the remaining
 *         target is best[i][j][k] - board[i][j], so pick any successor whose best equals it.
 *   (b) is the better answer when memory is the constraint and is worth naming out loud.
 *
 * PART 3 — count all maximum-score paths mod 1e9+7  (CHANGES FROM PART 2)
 *   Add a parallel table cnt[i][j][k] = number of max-score continuations from that state.
 *   cnt[N-1][j][k] = 1; otherwise sum cnt over exactly the successors that ATTAIN the max.
 *   The classic bug is mixing up the two cases while scanning successors:
 *       strictly better successor -> OVERWRITE  (ways = cnt[succ])
 *       equal-scoring successor   -> ACCUMULATE (ways += cnt[succ])
 *   Accumulating on "better" inflates the count; overwriting on "equal" drops ties.
 *   Two correctness points worth stating unprompted:
 *     1. "at most k" states already contain every path that uses FEWER than k jumps, so we
 *        must NOT also sum over k — that would double count. Each path is generated by
 *        exactly one branch sequence in this recurrence.
 *     2. Distinct move sequence <=> distinct visited-cell sequence here, so there is no
 *        aliasing to worry about: a jump (i,j)->(i+2,j) and the two-step
 *        (i,j)->(i+1,j)->(i+2,j) end on the same cell but visit different cell sets.
 *
 * PART 4 — adjacency bonuses  (CHANGES FROM PART 3)
 *   New scoring rules, both over CONSECUTIVELY VISITED cells (path order — NOT grid
 *   adjacency, so they apply ACROSS a two-row jump too):
 *       +X for every adjacent visited pair with EQUAL values
 *       +Y for every three consecutively visited cells with STRICTLY increasing values
 *   Overlapping occurrences each score (a,b,c,d increasing pays Y twice).
 *   The score is no longer a function of the current cell alone, so (row, col, k) stops
 *   being a Markov state: to decide the triple bonus we need the previous TWO values.
 *   The trick is that we do NOT have to store values (unbounded) — the previous cell is
 *   always at one of four fixed offsets from the current one, so we store HOW WE ARRIVED:
 *       START, FROM_UL (i-1,j-1), FROM_U (i-1,j), FROM_UR (i-1,j+1), FROM_JUMP (i-2,j)
 *   That 5-valued tag reconstructs the predecessor's position, hence its value, and the
 *   current cell supplies the second value. Bonuses are attributed ON THE TRANSITION into
 *   the third cell, which keeps every pattern counted exactly once.
 *   State (i, j, k, arrivalTag); time O(N*M*K*20), space O(N*M*K*5).
 *   Equivalently: DP over EDGES instead of nodes. Say it that way if the interviewer prefers.
 *
 * FOLLOW-UPS (discussed here, not all implemented)
 *   1. Exactly K jumps instead of at most K: same table, read best[0][p] at the state whose
 *      budget is fully consumed — i.e. track jumps USED and require used == K at the end,
 *      or subtract the at-most-(K-1) answer. Do not confuse the two readings; ask which.
 *   2. Must END on a specific column: backward DP already supports it — seed only that cell
 *      of the last row and leave the others at -infinity (dead states become real here).
 *   3. Path must avoid blocked cells: seed those states to -infinity; the "no dead states"
 *      argument above no longer holds, so the NEG sentinel starts doing real work.
 *   4. Jumps of arbitrary length L (or a set of jump sizes): the recurrence gains one term
 *      per jump size; state is unchanged. Cost O(N*M*K*|moves|).
 *   5. Lexicographically smallest optimal path: break ties in Part 2 by preferring the
 *      smallest column, and prove it is safe by induction on rows.
 *   6. Huge N with tiny K: rolling window keeps O(M*K) memory for the score, but Part 2's
 *      reconstruction then needs Hirschberg-style divide-and-conquer or a recomputation pass.
 *   7. Bonuses depending on the last R values: the arrival tag generalizes to the last R-1
 *      moves, i.e. 4^(R-1) tags — exponential in R, which is the honest tradeoff to state.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

class MaximumScoreGridPath {

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
            case "part4" -> { banner(4); Part4.main(new String[0]); }
            default -> {
                banner(1); Part1.main(new String[0]);
                banner(2); Part2.main(new String[0]);
                banner(3); Part3.main(new String[0]);
                banner(4); Part4.main(new String[0]);
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
    // PART 1 — maximum score with at most K two-row jumps (self-contained)
    //
    // best[i][j][k] = max score of the remainder of a path standing on (i,j) with k jumps
    //                 still available, INCLUDING board[i][j].
    // =================================================================================
    static final class Part1 {

        /** Unreachable sentinel. Safe to add board values to without overflowing. */
        private static final long NEG = Long.MIN_VALUE / 4;

        /**
         * Maximum score of a path from (0, p) to any cell of the last row using at most
         * K two-row jumps. Time O(N*M*K), space O(N*M*K).
         */
        static long maxScore(int[][] board, int p, int K) {
            validate(board, p, K);
            int n = board.length, m = board[0].length;

            long[][][] best = new long[n][m][K + 1];

            // Base case: already standing on the last row -> nothing left to collect.
            for (int j = 0; j < m; j++) {
                for (int k = 0; k <= K; k++) {
                    best[n - 1][j][k] = board[n - 1][j];
                }
            }

            for (int i = n - 2; i >= 0; i--) {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k <= K; k++) {
                        // NO SENTINEL NEEDED. For any i <= n-2 the plain "down" move
                        // (i+1, j) is ALWAYS in bounds, so we seed with it instead of a
                        // -infinity placeholder. This puts the "no dead states" invariant
                        // in the CODE rather than in a comment, and removes the only place
                        // a sentinel would have had board values added to it (where
                        // Long.MIN_VALUE would silently wrap to a huge POSITIVE and win
                        // the max). Compare Part 4, which genuinely needs NEG because it
                        // pre-fills unreachable arrival-tag states.
                        long bestNext = best[i + 1][j][k];

                        // The two DIAGONAL single-row moves; budget unchanged.
                        for (int dj = -1; dj <= 1; dj += 2) {
                            int nj = j + dj;
                            if (nj < 0 || nj >= m) continue;
                            bestNext = Math.max(bestNext, best[i + 1][nj][k]);
                        }

                        // Special two-row jump; costs one unit of budget. Landing on the
                        // last row from row N-3 is legal; landing past it is not.
                        if (k >= 1 && i + 2 <= n - 1) {
                            bestNext = Math.max(bestNext, best[i + 2][j][k - 1]);
                        }

                        best[i][j][k] = board[i][j] + bestNext;
                    }
                }
            }
            return best[0][p][K];
        }

        /**
         * Same answer with O(M*K) memory instead of O(N*M*K): only rows i+1 and i+2 are
         * ever read. Works for Part 1 ONLY — reconstruction and counting need the table.
         */
        static long maxScoreRollingWindow(int[][] board, int p, int K) {
            validate(board, p, K);
            int n = board.length, m = board[0].length;

            long[][] rowPlus1 = new long[m][K + 1];  // row i+1
            long[][] rowPlus2 = new long[m][K + 1];  // row i+2

            for (int j = 0; j < m; j++) {
                Arrays.fill(rowPlus1[j], board[n - 1][j]);
            }
            if (n == 1) return board[0][p];

            for (int i = n - 2; i >= 0; i--) {
                long[][] cur = new long[m][K + 1];
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k <= K; k++) {
                        // Seed with the always-valid "down" move; no sentinel (see maxScore).
                        long bestNext = rowPlus1[j][k];
                        for (int dj = -1; dj <= 1; dj += 2) {   // the two diagonals only
                            int nj = j + dj;
                            if (nj < 0 || nj >= m) continue;
                            bestNext = Math.max(bestNext, rowPlus1[nj][k]);
                        }
                        if (k >= 1 && i + 2 <= n - 1) {
                            bestNext = Math.max(bestNext, rowPlus2[j][k - 1]);
                        }
                        cur[j][k] = board[i][j] + bestNext;
                    }
                }
                rowPlus2 = rowPlus1;
                rowPlus1 = cur;
            }
            return rowPlus1[p][K];
        }

        /** Exponential oracle: enumerate every legal path, no memoization. */
        static long bruteForceMax(int[][] board, int p, int K) {
            validate(board, p, K);
            return dfs(board, 0, p, K);
        }

        private static long dfs(int[][] board, int i, int j, int k) {
            int n = board.length, m = board[0].length;
            if (i == n - 1) return board[i][j];
            long best = NEG;
            for (int dj = -1; dj <= 1; dj++) {
                int nj = j + dj;
                if (nj < 0 || nj >= m) continue;
                best = Math.max(best, dfs(board, i + 1, nj, k));
            }
            if (k >= 1 && i + 2 <= n - 1) {
                best = Math.max(best, dfs(board, i + 2, j, k - 1));
            }
            return board[i][j] + best;
        }

        private static void validate(int[][] board, int p, int K) {
            if (board == null || board.length == 0 || board[0] == null || board[0].length == 0) {
                throw new IllegalArgumentException("board must be non-empty");
            }
            int m = board[0].length;
            for (int[] row : board) {
                if (row == null || row.length != m) {
                    throw new IllegalArgumentException("board must be rectangular");
                }
            }
            if (p < 0 || p >= m) throw new IllegalArgumentException("start column out of range: " + p);
            if (K < 0) throw new IllegalArgumentException("jump budget must be >= 0: " + K);
        }

        public static void main(String[] args) {
            // ---- the prompt's worked example ----------------------------------------
            // board = [[1,2,3,4],[5,6,1,2],[7,8,9,1],[3,2,5,6]], p = 1, K = 1
            // Optimal: (0,1)->(1,1)->(2,2)->(3,3) = 2+6+9+6 = 23, using ZERO jumps.
            // The prompt's jump path (0,1)->(2,1)->(3,2) = 2+8+5 = 15 is legal but not
            // optimal — "at most K" means the budget may go unused.
            int[][] spec = {
                    {1, 2, 3, 4},
                    {5, 6, 1, 2},
                    {7, 8, 9, 1},
                    {3, 2, 5, 6}
            };
            expect(maxScore(spec, 1, 1), 23L, "spec example, p=1 K=1");
            expect(maxScore(spec, 1, 0), 23L, "spec example, p=1 K=0 (jump unused anyway)");
            expect(maxScore(spec, 0, 1), 22L, "spec example, p=0 K=1");
            expect(maxScoreRollingWindow(spec, 1, 1), 23L, "rolling window agrees on spec");

            // ---- the jump is strictly necessary --------------------------------------
            int[][] toxicMiddle = {
                    {5, 5},
                    {-100, -100},
                    {5, 5}
            };
            expect(maxScore(toxicMiddle, 0, 0), -90L, "no budget -> forced through -100");
            expect(maxScore(toxicMiddle, 0, 1), 10L, "one jump -> skip the -100 row");

            // ---- a jump from row N-3 onto the last row is legal ----------------------
            int[][] threeRows = {{1}, {-50}, {2}};
            expect(maxScore(threeRows, 0, 1), 3L, "jump row 0 -> row 2 (last row) is legal");
            expect(maxScore(threeRows, 0, 0), -47L, "without budget must pass through -50");

            // ---- the budget is genuinely a budget ------------------------------------
            int[][] alternating = {{0}, {-1}, {0}, {-1}, {0}};
            expect(maxScore(alternating, 0, 0), -2L, "K=0 pays both -1 cells");
            expect(maxScore(alternating, 0, 1), -1L, "K=1 skips one -1 cell");
            expect(maxScore(alternating, 0, 2), 0L, "K=2 skips both -1 cells");
            expect(maxScore(alternating, 0, 9), 0L, "surplus budget cannot help further");

            // ---- degenerate shapes ---------------------------------------------------
            expect(maxScore(new int[][]{{7, 8, 9}}, 1, 0), 8L, "single row -> start cell only");
            expect(maxScore(new int[][]{{7, 8, 9}}, 1, 5), 8L, "single row, unusable budget");
            expect(maxScore(new int[][]{{1, 2}, {3, 4}}, 0, 5), 5L, "two rows: jump would overshoot");
            expect(maxScore(new int[][]{{1}, {2}, {3}}, 0, 1), 6L, "single column, jump not worth it");
            expect(maxScore(new int[][]{{-5}}, 0, 0), -5L, "1x1 grid");

            // ---- column walls constrain the diagonals --------------------------------
            int[][] narrow = {
                    {1, 0},
                    {0, 9},
                    {9, 0}
            };
            expect(maxScore(narrow, 0, 0), 19L, "(0,0)->(1,1)->(2,0) = 1+9+9");

            // ---- overflow safety: sums exceed Integer.MAX_VALUE ----------------------
            int[][] big = new int[5][1];
            for (int i = 0; i < 5; i++) big[i][0] = 1_000_000_000;
            expect(maxScore(big, 0, 0), 5_000_000_000L, "long accumulation, no int overflow");

            // ---- input validation ----------------------------------------------------
            expectThrows(() -> maxScore(spec, -1, 1), "negative start column rejected");
            expectThrows(() -> maxScore(spec, 4, 1), "out-of-range start column rejected");
            expectThrows(() -> maxScore(spec, 0, -1), "negative budget rejected");
            expectThrows(() -> maxScore(new int[][]{{1, 2}, {3}}, 0, 0), "ragged board rejected");

            // ---- fuzz against the exponential oracle ---------------------------------
            Random rnd = new Random(20260724L);
            int cases = 0;
            for (int t = 0; t < 400; t++) {
                int n = 1 + rnd.nextInt(6);
                int m = 1 + rnd.nextInt(4);
                int K = rnd.nextInt(4);
                int p = rnd.nextInt(m);
                int[][] b = new int[n][m];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) b[i][j] = rnd.nextInt(9) - 4;   // -4..4, negatives on purpose
                }
                long dp = maxScore(b, p, K);
                long bf = bruteForceMax(b, p, K);
                long win = maxScoreRollingWindow(b, p, K);
                if (dp != bf || win != bf) {
                    throw new AssertionError("FAIL: fuzz mismatch dp=" + dp + " window=" + win
                            + " brute=" + bf + " p=" + p + " K=" + K
                            + " board=" + Arrays.deepToString(b));
                }
                cases++;
            }
            check(cases == 400, "fuzz: " + cases + " random boards match brute force");

            // ---- monotonicity in K (a free invariant worth asserting) ----------------
            for (int t = 0; t < 100; t++) {
                int n = 1 + rnd.nextInt(6);
                int m = 1 + rnd.nextInt(4);
                int p = rnd.nextInt(m);
                int[][] b = new int[n][m];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) b[i][j] = rnd.nextInt(9) - 4;
                }
                long prev = maxScore(b, p, 0);
                for (int K = 1; K <= 4; K++) {
                    long cur = maxScore(b, p, K);
                    if (cur < prev) {
                        throw new AssertionError("FAIL: more budget scored worse at K=" + K);
                    }
                    prev = cur;
                }
            }
            check(true, "score is non-decreasing in the jump budget");

            System.out.println("Part1: all checks passed");
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

        private static void expectThrows(Runnable r, String label) {
            try {
                r.run();
            } catch (IllegalArgumentException e) {
                System.out.println("PASS: " + label);
                return;
            }
            throw new AssertionError("FAIL: " + label + " (no exception thrown)");
        }
    }

    // =================================================================================
    // PART 2 — reconstruct ONE maximum-score path (self-contained)
    //
    // CHANGES FROM PART 1:
    //   - The score recurrence is IDENTICAL. Nothing about the max changes.
    //   - (a) `choice[i][j][k]` records which successor attained the max, then a forward
    //         walk from (0,p,K) emits the path. O(N*M*K) extra memory.
    //   - (b) `reconstructFromScoresOnly` proves the choice array is optional: the target
    //         for the remainder at (i,j,k) is best[i][j][k] - board[i][j], so any successor
    //         whose best equals that target is on some optimal path. ZERO extra memory.
    //   - Both are asserted to produce a LEGAL path whose score equals the Part 1 optimum.
    // =================================================================================
    static final class Part2 {

        private static final long NEG = Long.MIN_VALUE / 4;

        // Move codes, in the fixed tie-break order used by the choice array.
        private static final byte LEFT = 0;   // (i+1, j-1)
        private static final byte DOWN = 1;   // (i+1, j)
        private static final byte RIGHT = 2;  // (i+1, j+1)
        private static final byte JUMP = 3;   // (i+2, j), costs one unit of budget

        /** A path (list of {row, col}) together with its score. */
        record Path(long score, List<int[]> cells) {
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                for (int t = 0; t < cells.size(); t++) {
                    if (t > 0) sb.append(" -> ");
                    sb.append('(').append(cells.get(t)[0]).append(',').append(cells.get(t)[1]).append(')');
                }
                return sb + " = " + score;
            }
        }

        /** Approach (a): parent/choice tracking alongside the unchanged score DP. */
        static Path maxScorePath(int[][] board, int p, int K) {
            validate(board, p, K);
            int n = board.length, m = board[0].length;

            long[][][] best = new long[n][m][K + 1];
            byte[][][] choice = new byte[n][m][K + 1];

            for (int j = 0; j < m; j++) {
                for (int k = 0; k <= K; k++) best[n - 1][j][k] = board[n - 1][j];
            }

            for (int i = n - 2; i >= 0; i--) {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k <= K; k++) {
                        long bestNext = NEG;
                        byte bestMove = -1;

                        // Strict '>' keeps the FIRST move in (LEFT, DOWN, RIGHT, JUMP)
                        // order among ties -> deterministic output.
                        for (int dj = -1; dj <= 1; dj++) {
                            int nj = j + dj;
                            if (nj < 0 || nj >= m) continue;
                            long v = best[i + 1][nj][k];
                            if (v > bestNext) {
                                bestNext = v;
                                bestMove = (byte) (dj == -1 ? LEFT : dj == 0 ? DOWN : RIGHT);
                            }
                        }
                        if (k >= 1 && i + 2 <= n - 1) {
                            long v = best[i + 2][j][k - 1];
                            if (v > bestNext) {
                                bestNext = v;
                                bestMove = JUMP;
                            }
                        }
                        best[i][j][k] = board[i][j] + bestNext;
                        choice[i][j][k] = bestMove;
                    }
                }
            }

            List<int[]> cells = new ArrayList<>();
            int i = 0, j = p, k = K;
            cells.add(new int[]{i, j});
            while (i < n - 1) {
                switch (choice[i][j][k]) {
                    case LEFT -> { i += 1; j -= 1; }
                    case DOWN -> { i += 1; }
                    case RIGHT -> { i += 1; j += 1; }
                    case JUMP -> { i += 2; k -= 1; }
                    default -> throw new IllegalStateException("no recorded move at a non-terminal state");
                }
                cells.add(new int[]{i, j});
            }
            return new Path(best[0][p][K], cells);
        }

        /** Approach (b): no choice array — re-derive the move from the score table alone. */
        static Path reconstructFromScoresOnly(int[][] board, int p, int K) {
            validate(board, p, K);
            int n = board.length, m = board[0].length;

            long[][][] best = new long[n][m][K + 1];
            for (int j = 0; j < m; j++) {
                for (int k = 0; k <= K; k++) best[n - 1][j][k] = board[n - 1][j];
            }
            for (int i = n - 2; i >= 0; i--) {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k <= K; k++) {
                        long bestNext = NEG;
                        for (int dj = -1; dj <= 1; dj++) {
                            int nj = j + dj;
                            if (nj < 0 || nj >= m) continue;
                            bestNext = Math.max(bestNext, best[i + 1][nj][k]);
                        }
                        if (k >= 1 && i + 2 <= n - 1) {
                            bestNext = Math.max(bestNext, best[i + 2][j][k - 1]);
                        }
                        best[i][j][k] = board[i][j] + bestNext;
                    }
                }
            }

            List<int[]> cells = new ArrayList<>();
            int i = 0, j = p, k = K;
            cells.add(new int[]{i, j});
            while (i < n - 1) {
                long target = best[i][j][k] - board[i][j];   // what the remainder must score
                boolean moved = false;
                for (int dj = -1; dj <= 1 && !moved; dj++) {
                    int nj = j + dj;
                    if (nj < 0 || nj >= m) continue;
                    if (best[i + 1][nj][k] == target) {
                        i += 1;
                        j = nj;
                        moved = true;
                    }
                }
                if (!moved && k >= 1 && i + 2 <= n - 1 && best[i + 2][j][k - 1] == target) {
                    i += 2;
                    k -= 1;
                    moved = true;
                }
                if (!moved) throw new IllegalStateException("optimal successor not found — DP is inconsistent");
                cells.add(new int[]{i, j});
            }
            return new Path(best[0][p][K], cells);
        }

        /** Independent validator: is this a legal path, and does its score match the claim? */
        static void assertLegalPath(int[][] board, int p, int K, Path path) {
            int n = board.length, m = board[0].length;
            List<int[]> c = path.cells();
            if (c.isEmpty()) throw new AssertionError("FAIL: empty path");
            if (c.get(0)[0] != 0 || c.get(0)[1] != p) throw new AssertionError("FAIL: wrong start cell");
            if (c.get(c.size() - 1)[0] != n - 1) throw new AssertionError("FAIL: path does not end on the last row");

            int jumps = 0;
            long sum = board[c.get(0)[0]][c.get(0)[1]];
            for (int t = 1; t < c.size(); t++) {
                int pi = c.get(t - 1)[0], pj = c.get(t - 1)[1];
                int ci = c.get(t)[0], cj = c.get(t)[1];
                if (ci < 0 || ci >= n || cj < 0 || cj >= m) throw new AssertionError("FAIL: cell off grid");
                boolean single = ci == pi + 1 && Math.abs(cj - pj) <= 1;
                boolean jump = ci == pi + 2 && cj == pj;
                if (!single && !jump) throw new AssertionError("FAIL: illegal move at step " + t);
                if (jump) jumps++;
                sum += board[ci][cj];
            }
            if (jumps > K) throw new AssertionError("FAIL: used " + jumps + " jumps, budget was " + K);
            if (sum != path.score()) {
                throw new AssertionError("FAIL: claimed score " + path.score() + " but cells sum to " + sum);
            }
        }

        /** Exponential oracle for the optimum only (used to validate the path's score). */
        static long bruteForceMax(int[][] board, int p, int K) {
            return dfs(board, 0, p, K);
        }

        private static long dfs(int[][] board, int i, int j, int k) {
            int n = board.length, m = board[0].length;
            if (i == n - 1) return board[i][j];
            long best = NEG;
            for (int dj = -1; dj <= 1; dj++) {
                int nj = j + dj;
                if (nj < 0 || nj >= m) continue;
                best = Math.max(best, dfs(board, i + 1, nj, k));
            }
            if (k >= 1 && i + 2 <= n - 1) best = Math.max(best, dfs(board, i + 2, j, k - 1));
            return board[i][j] + best;
        }

        private static void validate(int[][] board, int p, int K) {
            if (board == null || board.length == 0 || board[0] == null || board[0].length == 0) {
                throw new IllegalArgumentException("board must be non-empty");
            }
            int m = board[0].length;
            for (int[] row : board) {
                if (row == null || row.length != m) throw new IllegalArgumentException("board must be rectangular");
            }
            if (p < 0 || p >= m) throw new IllegalArgumentException("start column out of range: " + p);
            if (K < 0) throw new IllegalArgumentException("jump budget must be >= 0: " + K);
        }

        public static void main(String[] args) {
            int[][] spec = {
                    {1, 2, 3, 4},
                    {5, 6, 1, 2},
                    {7, 8, 9, 1},
                    {3, 2, 5, 6}
            };

            Path viaChoice = maxScorePath(spec, 1, 1);
            expect(viaChoice.score(), 23L, "spec example optimum via choice array");
            expect(cellsOf(viaChoice), "(0,1) -> (1,1) -> (2,2) -> (3,3)", "spec example optimal path");
            assertLegalPath(spec, 1, 1, viaChoice);
            check(true, "spec path is legal and self-consistent");

            Path viaScores = reconstructFromScoresOnly(spec, 1, 1);
            expect(viaScores.score(), 23L, "same optimum without a choice array");
            assertLegalPath(spec, 1, 1, viaScores);
            check(true, "score-only reconstruction is legal too");

            // ---- the reconstructed path actually uses a jump when it must -------------
            int[][] toxicMiddle = {{5, 5}, {-100, -100}, {5, 5}};
            Path jumped = maxScorePath(toxicMiddle, 0, 1);
            expect(jumped.score(), 10L, "toxic middle row: optimum is 10");
            expect(cellsOf(jumped), "(0,0) -> (2,0)", "path uses the two-row jump");
            assertLegalPath(toxicMiddle, 0, 1, jumped);
            check(true, "jump path is legal");

            // ---- degenerate: already on the last row ---------------------------------
            Path single = maxScorePath(new int[][]{{7, 8, 9}}, 1, 3);
            expect(single.score(), 8L, "single-row optimum");
            expect(cellsOf(single), "(0,1)", "single-row path is just the start cell");
            assertLegalPath(new int[][]{{7, 8, 9}}, 1, 3, single);
            check(true, "single-cell path is legal");

            // ---- budget exhaustion mid-path is tracked correctly ----------------------
            int[][] alternating = {{0}, {-1}, {0}, {-1}, {0}};
            Path twoJumps = maxScorePath(alternating, 0, 2);
            expect(twoJumps.score(), 0L, "K=2 optimum skips both -1 rows");
            expect(cellsOf(twoJumps), "(0,0) -> (2,0) -> (4,0)", "path spends exactly two jumps");
            assertLegalPath(alternating, 0, 2, twoJumps);
            check(true, "two-jump path is legal and within budget");

            // ---- fuzz: both reconstructions must be legal AND optimal on every board --
            Random rnd = new Random(777L);
            for (int t = 0; t < 400; t++) {
                int n = 1 + rnd.nextInt(6);
                int m = 1 + rnd.nextInt(4);
                int K = rnd.nextInt(4);
                int p = rnd.nextInt(m);
                int[][] b = new int[n][m];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) b[i][j] = rnd.nextInt(7) - 3;
                }
                long opt = bruteForceMax(b, p, K);

                Path a = maxScorePath(b, p, K);
                assertLegalPath(b, p, K, a);
                if (a.score() != opt) {
                    throw new AssertionError("FAIL: choice-array path scored " + a.score() + " vs optimum " + opt
                            + " board=" + Arrays.deepToString(b));
                }

                Path c = reconstructFromScoresOnly(b, p, K);
                assertLegalPath(b, p, K, c);
                if (c.score() != opt) {
                    throw new AssertionError("FAIL: score-only path scored " + c.score() + " vs optimum " + opt
                            + " board=" + Arrays.deepToString(b));
                }
            }
            check(true, "fuzz: 400 random boards — both reconstructions legal and optimal");

            System.out.println("Part2: all checks passed");
        }

        private static String cellsOf(Path p) {
            StringBuilder sb = new StringBuilder();
            for (int t = 0; t < p.cells().size(); t++) {
                if (t > 0) sb.append(" -> ");
                sb.append('(').append(p.cells().get(t)[0]).append(',').append(p.cells().get(t)[1]).append(')');
            }
            return sb.toString();
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
    // PART 3 — count all maximum-score paths mod 1e9+7 (self-contained)
    //
    // CHANGES FROM PART 2:
    //   - Drop the choice array; add cnt[i][j][k] = number of max-score continuations.
    //   - cnt[N-1][j][k] = 1. Otherwise, scanning successors in one pass:
    //         v >  bestNext -> bestNext = v; ways  = cnt[succ]      (OVERWRITE)
    //         v == bestNext ->              ways += cnt[succ]       (ACCUMULATE)
    //     Getting these two backwards is the single most common bug in this problem.
    //   - Do NOT sum across the budget dimension: the "at most k" state already includes
    //     every path that uses fewer than k jumps, so each path is produced exactly once.
    // =================================================================================
    static final class Part3 {

        private static final long NEG = Long.MIN_VALUE / 4;
        private static final long MOD = 1_000_000_007L;

        record Result(long maxScore, long paths) { }

        /** Max score and the number of distinct paths attaining it, mod 1e9+7. */
        static Result countMaxScorePaths(int[][] board, int p, int K) {
            validate(board, p, K);
            int n = board.length, m = board[0].length;

            long[][][] best = new long[n][m][K + 1];
            long[][][] cnt = new long[n][m][K + 1];

            for (int j = 0; j < m; j++) {
                for (int k = 0; k <= K; k++) {
                    best[n - 1][j][k] = board[n - 1][j];
                    cnt[n - 1][j][k] = 1;                 // the empty continuation
                }
            }

            for (int i = n - 2; i >= 0; i--) {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k <= K; k++) {
                        long bestNext = NEG;
                        long ways = 0;

                        for (int dj = -1; dj <= 1; dj++) {
                            int nj = j + dj;
                            if (nj < 0 || nj >= m) continue;
                            long v = best[i + 1][nj][k];
                            if (v > bestNext) {
                                bestNext = v;
                                ways = cnt[i + 1][nj][k];                 // OVERWRITE
                            } else if (v == bestNext) {
                                ways = (ways + cnt[i + 1][nj][k]) % MOD;  // ACCUMULATE
                            }
                        }

                        if (k >= 1 && i + 2 <= n - 1) {
                            long v = best[i + 2][j][k - 1];
                            if (v > bestNext) {
                                bestNext = v;
                                ways = cnt[i + 2][j][k - 1];
                            } else if (v == bestNext) {
                                ways = (ways + cnt[i + 2][j][k - 1]) % MOD;
                            }
                        }

                        best[i][j][k] = board[i][j] + bestNext;
                        cnt[i][j][k] = ways % MOD;
                    }
                }
            }
            return new Result(best[0][p][K], cnt[0][p][K]);
        }

        /** Exponential oracle: enumerate every path, keep the max and how many hit it. */
        static Result bruteForce(int[][] board, int p, int K) {
            validate(board, p, K);
            long[] acc = {NEG, 0};                       // {best, count}
            dfs(board, 0, p, K, board[0][p], acc);
            return new Result(acc[0], acc[1] % MOD);
        }

        private static void dfs(int[][] board, int i, int j, int k, long running, long[] acc) {
            int n = board.length, m = board[0].length;
            if (i == n - 1) {
                if (running > acc[0]) {
                    acc[0] = running;
                    acc[1] = 1;
                } else if (running == acc[0]) {
                    acc[1]++;
                }
                return;
            }
            for (int dj = -1; dj <= 1; dj++) {
                int nj = j + dj;
                if (nj < 0 || nj >= m) continue;
                dfs(board, i + 1, nj, k, running + board[i + 1][nj], acc);
            }
            if (k >= 1 && i + 2 <= n - 1) {
                dfs(board, i + 2, j, k - 1, running + board[i + 2][j], acc);
            }
        }

        private static void validate(int[][] board, int p, int K) {
            if (board == null || board.length == 0 || board[0] == null || board[0].length == 0) {
                throw new IllegalArgumentException("board must be non-empty");
            }
            int m = board[0].length;
            for (int[] row : board) {
                if (row == null || row.length != m) throw new IllegalArgumentException("board must be rectangular");
            }
            if (p < 0 || p >= m) throw new IllegalArgumentException("start column out of range: " + p);
            if (K < 0) throw new IllegalArgumentException("jump budget must be >= 0: " + K);
        }

        public static void main(String[] args) {
            // ---- the prompt's example has a UNIQUE optimum ---------------------------
            int[][] spec = {
                    {1, 2, 3, 4},
                    {5, 6, 1, 2},
                    {7, 8, 9, 1},
                    {3, 2, 5, 6}
            };
            Result specRes = countMaxScorePaths(spec, 1, 1);
            expect(specRes.maxScore(), 23L, "spec example optimum");
            expect(specRes.paths(), 1L, "spec example has exactly one optimal path");

            // ---- flat board: every single-step path ties -----------------------------
            int[][] ones = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
            Result flat0 = countMaxScorePaths(ones, 1, 0);
            expect(flat0.maxScore(), 3L, "all-ones 3x3, K=0 optimum");
            expect(flat0.paths(), 7L, "all-ones 3x3, K=0 -> 7 tied paths");
            Result flat1 = countMaxScorePaths(ones, 1, 1);
            expect(flat1.maxScore(), 3L, "all-ones 3x3, K=1 optimum unchanged");
            expect(flat1.paths(), 7L, "budget adds a shorter path that scores 2, so still 7");

            // ---- optimal paths using DIFFERENT numbers of jumps must both count ------
            // (0,0)->(1,0)->(2,0) = 1+0+1 = 2 (zero jumps)
            // (0,0)->(2,0)        = 1+1   = 2 (one jump)
            int[][] tiedBudgets = {{1}, {0}, {1}};
            Result mixed = countMaxScorePaths(tiedBudgets, 0, 1);
            expect(mixed.maxScore(), 2L, "tie across jump counts: optimum");
            expect(mixed.paths(), 2L, "tie across jump counts: both counted exactly once");
            expect(countMaxScorePaths(tiedBudgets, 0, 0).paths(), 1L, "with K=0 only the walk survives");

            // ---- degenerate shapes ---------------------------------------------------
            expect(countMaxScorePaths(new int[][]{{5}}, 0, 0).paths(), 1L, "1x1 -> one path");
            expect(countMaxScorePaths(new int[][]{{7, 8, 9}}, 1, 2).paths(), 1L, "single row -> one path");
            Result twoRows = countMaxScorePaths(new int[][]{{0, 0}, {1, 1}}, 0, 3);
            expect(twoRows.maxScore(), 1L, "two rows optimum");
            expect(twoRows.paths(), 2L, "two rows: down and down-right both optimal");

            // ---- the count is taken mod 1e9+7 ---------------------------------------
            // A tall all-zero grid has an astronomically large number of optimal paths;
            // the only requirement is that the reduced value matches the oracle's.
            int[][] zeros = new int[8][3];
            Result modCase = countMaxScorePaths(zeros, 1, 2);
            Result modOracle = bruteForce(zeros, 1, 2);
            expect(modCase.paths(), modOracle.paths(), "all-zero 8x3: every path ties, count matches oracle");
            check(modCase.paths() > 0, "count stays positive after modular reduction");

            // ---- fuzz against the exponential oracle, tie-heavy value range ----------
            Random rnd = new Random(31337L);
            for (int t = 0; t < 500; t++) {
                int n = 1 + rnd.nextInt(6);
                int m = 1 + rnd.nextInt(4);
                int K = rnd.nextInt(4);
                int p = rnd.nextInt(m);
                int[][] b = new int[n][m];
                for (int i = 0; i < n; i++) {
                    // Deliberately tiny value range: forces many ties, which is exactly
                    // where the overwrite-vs-accumulate bug shows up.
                    for (int j = 0; j < m; j++) b[i][j] = rnd.nextInt(3) - 1;
                }
                Result dp = countMaxScorePaths(b, p, K);
                Result bf = bruteForce(b, p, K);
                if (dp.maxScore() != bf.maxScore() || dp.paths() != bf.paths()) {
                    throw new AssertionError("FAIL: fuzz mismatch dp=" + dp + " brute=" + bf
                            + " p=" + p + " K=" + K + " board=" + Arrays.deepToString(b));
                }
            }
            check(true, "fuzz: 500 tie-heavy boards — max and count both match brute force");

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

    // =================================================================================
    // PART 4 — adjacency bonuses (self-contained)
    //
    // CHANGES FROM PART 3:
    //   - Two extra scoring rules over CONSECUTIVELY VISITED cells (path order, so they
    //     apply across a two-row jump as well):
    //         +X per adjacent visited pair with EQUAL values
    //         +Y per three consecutively visited cells with STRICTLY increasing values
    //     Overlapping windows each pay: 1,2,3,4 pays Y twice.
    //   - (row, col, k) is no longer a Markov state — the triple bonus needs the previous
    //     TWO values. We do NOT store values; we store HOW WE ARRIVED at the current cell,
    //     a 5-valued tag that pins the predecessor's POSITION (hence its value):
    //         START | FROM_UL (i-1,j-1) | FROM_U (i-1,j) | FROM_UR (i-1,j+1) | FROM_JUMP (i-2,j)
    //     Current cell + predecessor = the two values the bonuses need. Equivalently: this
    //     is a DP over EDGES rather than nodes.
    //   - Bonuses are attributed ON THE TRANSITION into the third cell, so each pattern is
    //     counted exactly once and the suffix value stays well defined:
    //         S[i][j][k][t] = best score STRICTLY AFTER (i,j), given arrival t and k jumps left
    //         answer = board[0][p] + S[0][p][K][START]
    //   - State/time: O(N*M*K*5) states, <=4 transitions each.
    // =================================================================================
    static final class Part4 {

        private static final long NEG = Long.MIN_VALUE / 4;

        // Arrival tags: where the PREVIOUS visited cell sits relative to the current one.
        private static final int START = 0;      // no predecessor (only at row 0)
        private static final int FROM_UL = 1;    // predecessor at (i-1, j-1)
        private static final int FROM_U = 2;     // predecessor at (i-1, j)
        private static final int FROM_UR = 3;    // predecessor at (i-1, j+1)
        private static final int FROM_JUMP = 4;  // predecessor at (i-2, j)
        private static final int TAGS = 5;

        /**
         * Maximum score from (0,p) to the last row with at most K jumps, where the score is
         * the sum of visited values plus X per equal adjacent visited pair plus Y per
         * strictly increasing consecutively visited triple.
         */
        static long maxScoreWithBonuses(int[][] board, int p, int K, int X, int Y) {
            validate(board, p, K);
            int n = board.length, m = board[0].length;

            // S[i][j][k][t] = best score of the part of the path AFTER (i,j).
            long[][][][] s = new long[n][m][K + 1][TAGS];
            for (long[][][] a : s) for (long[][] b : a) for (long[] c : b) Arrays.fill(c, NEG);

            for (int i = n - 1; i >= 0; i--) {
                for (int j = 0; j < m; j++) {
                    for (int t = 0; t < TAGS; t++) {
                        if (!arrivalPossible(i, j, t, m)) continue;    // unreachable state

                        boolean hasPrev = t != START;
                        long prev = hasPrev ? board[predRow(i, t)][predCol(j, t)] : 0;
                        long cur = board[i][j];

                        for (int k = 0; k <= K; k++) {
                            if (i == n - 1) {
                                s[i][j][k][t] = 0;                     // nothing left to collect
                                continue;
                            }
                            long bestRest = NEG;

                            // --- three single-row moves ---
                            for (int dj = -1; dj <= 1; dj++) {
                                int nj = j + dj;
                                if (nj < 0 || nj >= m) continue;
                                int nextTag = dj == -1 ? FROM_UR : dj == 0 ? FROM_U : FROM_UL;
                                long next = board[i + 1][nj];
                                long gain = next
                                        + (cur == next ? X : 0)
                                        + (hasPrev && prev < cur && cur < next ? Y : 0);
                                bestRest = Math.max(bestRest, gain + s[i + 1][nj][k][nextTag]);
                            }

                            // --- special two-row jump; the bonuses still apply, because
                            //     the landing cell is the next cell VISITED ---
                            if (k >= 1 && i + 2 <= n - 1) {
                                long next = board[i + 2][j];
                                long gain = next
                                        + (cur == next ? X : 0)
                                        + (hasPrev && prev < cur && cur < next ? Y : 0);
                                bestRest = Math.max(bestRest, gain + s[i + 2][j][k - 1][FROM_JUMP]);
                            }

                            s[i][j][k][t] = bestRest;
                        }
                    }
                }
            }
            return board[0][p] + s[0][p][K][START];
        }

        /** Is arrival tag t consistent with standing on (i, j)? (Predecessor must be on grid.) */
        private static boolean arrivalPossible(int i, int j, int t, int m) {
            return switch (t) {
                case START -> i == 0;
                case FROM_UL -> i >= 1 && j - 1 >= 0;
                case FROM_U -> i >= 1;
                case FROM_UR -> i >= 1 && j + 1 < m;
                case FROM_JUMP -> i >= 2;
                default -> false;
            };
        }

        private static int predRow(int i, int t) {
            return t == FROM_JUMP ? i - 2 : i - 1;
        }

        private static int predCol(int j, int t) {
            return switch (t) {
                case FROM_UL -> j - 1;
                case FROM_UR -> j + 1;
                default -> j;                                  // FROM_U, FROM_JUMP
            };
        }

        /** Exponential oracle: enumerate every path and score it from scratch. */
        static long bruteForce(int[][] board, int p, int K, int X, int Y) {
            validate(board, p, K);
            List<int[]> path = new ArrayList<>();
            path.add(new int[]{0, p});
            return dfs(board, 0, p, K, X, Y, path);
        }

        private static long dfs(int[][] board, int i, int j, int k, int X, int Y, List<int[]> path) {
            int n = board.length, m = board[0].length;
            if (i == n - 1) return scoreOf(board, path, X, Y);
            long best = NEG;
            for (int dj = -1; dj <= 1; dj++) {
                int nj = j + dj;
                if (nj < 0 || nj >= m) continue;
                path.add(new int[]{i + 1, nj});
                best = Math.max(best, dfs(board, i + 1, nj, k, X, Y, path));
                path.remove(path.size() - 1);
            }
            if (k >= 1 && i + 2 <= n - 1) {
                path.add(new int[]{i + 2, j});
                best = Math.max(best, dfs(board, i + 2, j, k - 1, X, Y, path));
                path.remove(path.size() - 1);
            }
            return best;
        }

        /** Reference scorer, written straight off the problem statement. */
        static long scoreOf(int[][] board, List<int[]> path, int X, int Y) {
            long score = 0;
            for (int[] c : path) score += board[c[0]][c[1]];
            for (int t = 1; t < path.size(); t++) {
                if (valAt(board, path, t - 1) == valAt(board, path, t)) score += X;
            }
            for (int t = 2; t < path.size(); t++) {
                long a = valAt(board, path, t - 2), b = valAt(board, path, t - 1), c = valAt(board, path, t);
                if (a < b && b < c) score += Y;
            }
            return score;
        }

        private static long valAt(int[][] board, List<int[]> path, int idx) {
            return board[path.get(idx)[0]][path.get(idx)[1]];
        }

        private static void validate(int[][] board, int p, int K) {
            if (board == null || board.length == 0 || board[0] == null || board[0].length == 0) {
                throw new IllegalArgumentException("board must be non-empty");
            }
            int m = board[0].length;
            for (int[] row : board) {
                if (row == null || row.length != m) throw new IllegalArgumentException("board must be rectangular");
            }
            if (p < 0 || p >= m) throw new IllegalArgumentException("start column out of range: " + p);
            if (K < 0) throw new IllegalArgumentException("jump budget must be >= 0: " + K);
        }

        public static void main(String[] args) {
            // ---- X = 0 and Y = 0 must collapse to the Part 1 answer ------------------
            int[][] spec = {
                    {1, 2, 3, 4},
                    {5, 6, 1, 2},
                    {7, 8, 9, 1},
                    {3, 2, 5, 6}
            };
            expect(maxScoreWithBonuses(spec, 1, 1, 0, 0), 23L, "zero bonuses reduce to the base problem");
            // The optimal path visits 2,6,9,6. Windows: (2,6,9) is strictly increasing, but
            // (6,9,6) is not — so exactly ONE triple bonus, and no board path can earn two
            // (that would need 2 < v1 < v2 < v3, and row 3 has no value above 6).
            expect(maxScoreWithBonuses(spec, 1, 1, 0, 100), 123L, "spec example with Y=100 -> one triple");
            // No two adjacent reachable cells share a value anywhere on this board, so X is dead.
            expect(maxScoreWithBonuses(spec, 1, 1, 50, 100), 123L, "no equal-valued adjacency to exploit");

            // ---- the equal-pair bonus --------------------------------------------------
            expect(maxScoreWithBonuses(new int[][]{{1}, {1}, {2}}, 0, 0, 10, 100), 14L,
                    "1,1,2: base 4 + X for the equal pair; 1<1 is NOT strictly increasing");
            expect(maxScoreWithBonuses(new int[][]{{1}, {2}, {2}}, 0, 0, 10, 100), 15L,
                    "1,2,2: base 5 + X; 2<2 blocks the triple bonus");

            // ---- the increasing-triple bonus, including overlap -----------------------
            expect(maxScoreWithBonuses(new int[][]{{1}, {2}, {3}}, 0, 0, 10, 100), 106L,
                    "1,2,3: base 6 + one triple");
            expect(maxScoreWithBonuses(new int[][]{{1}, {2}, {3}, {4}}, 0, 0, 0, 100), 210L,
                    "1,2,3,4: base 10 + TWO overlapping triples");

            // ---- bonuses span a two-row jump (path order, not grid adjacency) ---------
            // no jump : 1,99,2,3 -> base 105, no triple            = 105
            // jump 0->2: 1,2,3   -> base 6 + triple(1<2<3) = 106   <-- optimum
            int[][] jumpTriple = {{1}, {99}, {2}, {3}};
            expect(maxScoreWithBonuses(jumpTriple, 0, 1, 0, 100), 106L,
                    "triple bonus counts ACROSS a jump and beats the higher base score");
            expect(maxScoreWithBonuses(jumpTriple, 0, 0, 0, 100), 105L,
                    "without budget the base-heavy walk wins");

            // ---- a bonus can flip which path is optimal ------------------------------
            // Column 0 is flat 5,5,5 (two equal pairs); column 1 climbs 5,6,7.
            int[][] flip = {
                    {5, 0},
                    {5, 6},
                    {5, 7}
            };
            expect(maxScoreWithBonuses(flip, 0, 0, 0, 0), 18L, "no bonuses: 5+6+7 wins");
            expect(maxScoreWithBonuses(flip, 0, 0, 100, 0), 215L, "big X: the flat column wins");

            // ---- degenerate shapes ---------------------------------------------------
            expect(maxScoreWithBonuses(new int[][]{{9}}, 0, 0, 7, 7), 9L, "1x1: no pair, no triple");
            expect(maxScoreWithBonuses(new int[][]{{1}, {1}}, 0, 0, 10, 100), 12L, "two cells: pair only");

            // ---- fuzz against the exponential oracle ---------------------------------
            Random rnd = new Random(4242L);
            for (int t = 0; t < 600; t++) {
                int n = 1 + rnd.nextInt(6);
                int m = 1 + rnd.nextInt(4);
                int K = rnd.nextInt(4);
                int p = rnd.nextInt(m);
                int X = rnd.nextInt(6);
                int Y = rnd.nextInt(6);
                int[][] b = new int[n][m];
                for (int i = 0; i < n; i++) {
                    // Tiny value range so equal pairs and increasing triples both occur often.
                    for (int j = 0; j < m; j++) b[i][j] = rnd.nextInt(4) - 1;
                }
                long dp = maxScoreWithBonuses(b, p, K, X, Y);
                long bf = bruteForce(b, p, K, X, Y);
                if (dp != bf) {
                    throw new AssertionError("FAIL: fuzz mismatch dp=" + dp + " brute=" + bf
                            + " p=" + p + " K=" + K + " X=" + X + " Y=" + Y
                            + " board=" + Arrays.deepToString(b));
                }
            }
            check(true, "fuzz: 600 random boards x random (X,Y) match brute force");

            // ---- and with bonuses switched off it must equal the plain optimum -------
            for (int t = 0; t < 200; t++) {
                int n = 1 + rnd.nextInt(6);
                int m = 1 + rnd.nextInt(4);
                int K = rnd.nextInt(4);
                int p = rnd.nextInt(m);
                int[][] b = new int[n][m];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) b[i][j] = rnd.nextInt(9) - 4;
                }
                long withZeroBonuses = maxScoreWithBonuses(b, p, K, 0, 0);
                long plain = bruteForce(b, p, K, 0, 0);
                if (withZeroBonuses != plain) {
                    throw new AssertionError("FAIL: X=Y=0 did not reduce to the base problem");
                }
            }
            check(true, "X=Y=0 reduces to the Part 1 problem on 200 random boards");

            System.out.println("Part4: all checks passed");
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
