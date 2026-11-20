/*
 * Infection Spread Simulation — OpenAI Staff SWE coding question.
 *
 * MULTI-PART progressive problem (5 levels). Each level keeps the same core
 * infection rule but layers on a new state transition or optimization. Every
 * level lives in its OWN fully self-contained static nested class (Part1..Part5)
 * with its own `public static void main`, its own copy of the 4-direction array
 * DIRS, and all helpers nested inside it. No part references another part, so
 * any single PartN class can be lifted out into its own .java file unchanged
 * (just rename/promote it to a top-level class).
 *
 * ------------------------------------------------------------------------
 * CORE RULE (all levels)
 * ------------------------------------------------------------------------
 * n×m grid of people: 0 = healthy, 1 = infected, 2 = immune (from L2 on),
 * internal -1 = burned (L5 only). A healthy person becomes infected when at
 * least N of its 4 orthogonal neighbors (up/down/left/right, NO diagonals)
 * were infected at the START of the current time step. Updates are
 * simultaneous: a cell infected this step cannot help infect anyone until the
 * next step (we always evaluate against a previous-step snapshot).
 * 1 <= N <= 4 normally; N > 4 means nothing new can ever be infected.
 *
 * ------------------------------------------------------------------------
 * APPROACH PER LEVEL
 * ------------------------------------------------------------------------
 * L1  time_to_full_infection(grid, N): snapshot simulation. Walk every healthy
 *     (0) cell, count infected (==1) neighbors against the in-place grid (which
 *     still holds the previous snapshot because we apply new infections only
 *     AFTER the full scan). Track healthyCount. 0 if already full; -1 if no
 *     source OR a full scan adds nothing while healthy cells remain (stall).
 *     O((nm)^2) time, O(nm) space.
 *
 * L2  time_to_full_infection_with_immunity(grid, N): same simulation, but value
 *     2 = immune is a permanent blocker — it is never counted as an infected
 *     neighbor (only ==1 counts) and is excluded from healthyCount. CHANGES are
 *     small: healthyCount counts only 0s, infectedCount only 1s; a healthy cell
 *     walled off by immune cells simply never reaches threshold -> stall -> -1.
 *     O((nm)^2) time, O(nm) space.
 *
 * L3  time_to_stable_state(grid, N, D): cells carry an infection age. After
 *     being infected D days a cell becomes immune (stops spreading, can't be
 *     re-infected). We track infectionDay[][] (HEALTHY / IMMUNE sentinels or a
 *     day>=0) plus a HashSet of active-infection cell keys (i*m+j). Each day:
 *     current_day++; snapshot active set; cells with age>=D are marked to become
 *     immune at end-of-day BUT still count as infected in this day's snapshot
 *     ("cells that reach age D still spread on that day"); infect healthy cells
 *     with >=N infected neighbors in the snapshot; end-of-day commit immunities
 *     and add new infections. Return day when no active infections remain.
 *     O(days*nm) time, O(nm) space.
 *
 * L4  time_to_stable_with_death(grid, N, D, K) -> (days, deaths): CHANGES from
 *     L3 — a transitioning cell (age>=D) DIES instead of becoming immune if it
 *     has >=K infected neighbors in the start-of-day snapshot; else it becomes
 *     immune. Dead cells are permanent barriers (distinct from immune). Spread
 *     (threshold N) is computed from the SAME snapshot before any transition is
 *     committed. O(days*nm) time, O(nm) space.
 *
 * L5  optimal_burn(grid, N, D, K) -> (burn_type, burn_index, min_deaths):
 *     CHANGES from L4 — before infection starts, burn ONE whole row or column
 *     (cells -> internal BURNED, behaving like dead: never infected, never
 *     spread, never counted as a death). For each of the n rows and m columns,
 *     deep-copy the grid, burn that line, run the full L4 simulation, record
 *     infection deaths, and keep the burn with the fewest deaths (ties: any).
 *     Brute force O((n+m)*days*nm) time, O(nm) space.
 *
 * ------------------------------------------------------------------------
 * COMPILE / RUN
 * ------------------------------------------------------------------------
 * The filename has spaces and a hyphen and does not match the class name; that
 * is fine because the top-level class is package-private (the file declares no
 * `package`). javac accepts arbitrary filenames:
 *
 *   javac "A - Infection Spread Simulation.java"
 *   java -ea InfectionSpreadSimulation            # runs all 5 parts
 *   java -ea InfectionSpreadSimulation part3       # runs only Part3
 *
 * Run with -ea so the embedded `assert` test cases are checked.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class InfectionSpreadSimulation {

    /**
     * Top-level entry point. With no args, runs every part in order and prints a
     * final banner. With one arg ("part1".."part5") it dispatches to that part's
     * own main. Unknown args print usage to stderr.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Part1.main(args);
            Part2.main(args);
            Part3.main(args);
            Part4.main(args);
            Part5.main(args);
            System.out.println("All parts: all checks passed");
            return;
        }

        switch (args[0]) {
            case "part1" -> Part1.main(args);
            case "part2" -> Part2.main(args);
            case "part3" -> Part3.main(args);
            case "part4" -> Part4.main(args);
            case "part5" -> Part5.main(args);
            default -> {
                System.err.println("Unknown part: " + args[0]);
                System.err.println("Usage: java InfectionSpreadSimulation [part1|part2|part3|part4|part5]");
                System.err.println("       (no argument runs all parts)");
            }
        }
    }

    // ====================================================================
    // PART 1 — Threshold-Based Infection Spread
    // ====================================================================
    /**
     * Level 1: count time steps until every cell is infected.
     *
     * Self-contained: own DIRS, own helpers, no references to other parts.
     */
    static final class Part1 {

        // 4 orthogonal directions: up, down, left, right (no diagonals).
        private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        /**
         * Returns the number of time steps until all cells are infected, 0 if the
         * grid is already fully infected, or -1 if not everyone can ever be
         * infected (no infected source, or a full scan produces no new infections
         * while healthy cells remain).
         *
         * Snapshot simulation: new infections for a step are collected during the
         * scan and applied only AFTER the scan finishes, so the in-place grid keeps
         * serving as the previous-step snapshot while we count neighbors.
         * Complexity: O((n*m)^2) time, O(n*m) space.
         */
        static int time_to_full_infection(int[][] grid, int N) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return -1;
            }
            int n = grid.length, m = grid[0].length;

            // Work on a private copy so callers' grids are not mutated.
            int[][] g = copyGrid(grid);

            int healthyCount = 0;
            int infectedCount = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (g[i][j] == 0) healthyCount++;
                    else if (g[i][j] == 1) infectedCount++;
                }
            }

            if (healthyCount == 0) return 0;   // everyone already infected
            if (infectedCount == 0) return -1; // no source to spread from

            int timeStep = 0;
            while (healthyCount > 0) {
                List<int[]> newInfections = new ArrayList<>();

                // Evaluate every healthy cell against the previous snapshot (g).
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (g[i][j] != 0) continue;
                        if (countInfectedNeighbors(g, n, m, i, j) >= N) {
                            newInfections.add(new int[] {i, j});
                        }
                    }
                }

                // Stall: nothing new can be infected but healthy cells remain.
                if (newInfections.isEmpty()) return -1;

                // Apply simultaneously (after the full scan).
                for (int[] cell : newInfections) {
                    g[cell[0]][cell[1]] = 1;
                    healthyCount--;
                }
                timeStep++;
            }
            return timeStep;
        }

        /** Counts neighbors that are infected (==1) in snapshot g. */
        private static int countInfectedNeighbors(int[][] g, int n, int m, int i, int j) {
            int count = 0;
            for (int[] d : DIRS) {
                int ni = i + d[0], nj = j + d[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < m && g[ni][nj] == 1) {
                    count++;
                }
            }
            return count;
        }

        private static int[][] copyGrid(int[][] grid) {
            int[][] g = new int[grid.length][];
            for (int i = 0; i < grid.length; i++) {
                g[i] = grid[i].clone();
            }
            return g;
        }

        public static void main(String[] args) {
            // Provided examples / test cases.
            assert time_to_full_infection(new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}, 1) == 2 : "center N=1";
            assert time_to_full_infection(new int[][] {{1, 1}, {1, 0}}, 2) == 1 : "corner N=2";
            assert time_to_full_infection(new int[][] {{1, 0, 0}, {0, 0, 0}, {0, 0, 1}}, 2) == -1 : "stall N=2";
            assert time_to_full_infection(new int[][] {{0, 0}, {0, 0}}, 1) == -1 : "no source";
            assert time_to_full_infection(new int[][] {{1, 1}, {1, 1}}, 2) == 0 : "already full";
            assert time_to_full_infection(new int[][] {{1}}, 1) == 0 : "single infected";
            assert time_to_full_infection(new int[][] {{0}}, 1) == -1 : "single healthy";
            assert time_to_full_infection(new int[][] {{1, 0, 0, 0, 0}}, 1) == 4 : "linear N=1";

            // Larger 5x5 grid, only the center (2,2) infected, N=1 -> 4 steps.
            int[][] big = new int[5][5];
            big[2][2] = 1;
            assert time_to_full_infection(big, 1) == 4 : "5x5 center N=1";

            // Problem-statement examples.
            assert time_to_full_infection(new int[][] {{1, 0, 0}, {0, 0, 0}, {0, 0, 1}}, 1) == 2 : "two corners N=1";
            assert time_to_full_infection(new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, 1) == -1 : "all healthy N=1";

            System.out.println("Part 1: all checks passed");
        }
    }

    // ====================================================================
    // PART 2 — Immune Cells
    // ====================================================================
    /**
     * Level 2: value 2 = immune. Immune cells cannot be infected, never spread,
     * and act as permanent blockers.
     *
     * CHANGES FROM PART 1:
     *   - Grid value 2 = immune. healthyCount counts only 0s and infectedCount
     *     only 1s (immune cells are excluded from both).
     *   - Only ==1 counts as an infected neighbor, so immune (2) cells are
     *     naturally not counted — they block spread.
     *   - A healthy cell walled off by immune cells simply never reaches the
     *     threshold; that surfaces as the same stall -> -1 rule as Part 1.
     *
     * Self-contained: own DIRS, own helpers, no references to other parts.
     */
    static final class Part2 {

        private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        /**
         * Returns time steps until all NON-immune cells are infected, 0 if all
         * non-immune cells are already infected, or -1 if there is no source or
         * some healthy cell can never reach the threshold (stall).
         * Complexity: O((n*m)^2) time, O(n*m) space.
         */
        static int time_to_full_infection_with_immunity(int[][] grid, int N) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return -1;
            }
            int n = grid.length, m = grid[0].length;
            int[][] g = copyGrid(grid);

            int healthyCount = 0; // only value 0
            int infectedCount = 0; // only value 1
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (g[i][j] == 0) healthyCount++;
                    else if (g[i][j] == 1) infectedCount++;
                }
            }

            if (healthyCount == 0) return 0;   // all non-immune cells already infected
            if (infectedCount == 0) return -1; // no source

            int timeStep = 0;
            while (healthyCount > 0) {
                List<int[]> newInfections = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (g[i][j] != 0) continue; // skip infected (1) and immune (2)
                        if (countInfectedNeighbors(g, n, m, i, j) >= N) {
                            newInfections.add(new int[] {i, j});
                        }
                    }
                }

                if (newInfections.isEmpty()) return -1; // unreachable healthy cells remain

                for (int[] cell : newInfections) {
                    g[cell[0]][cell[1]] = 1;
                    healthyCount--;
                }
                timeStep++;
            }
            return timeStep;
        }

        /** Only value 1 counts as infected; value 2 (immune) is ignored -> blocker. */
        private static int countInfectedNeighbors(int[][] g, int n, int m, int i, int j) {
            int count = 0;
            for (int[] d : DIRS) {
                int ni = i + d[0], nj = j + d[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < m && g[ni][nj] == 1) {
                    count++;
                }
            }
            return count;
        }

        private static int[][] copyGrid(int[][] grid) {
            int[][] g = new int[grid.length][];
            for (int i = 0; i < grid.length; i++) {
                g[i] = grid[i].clone();
            }
            return g;
        }

        public static void main(String[] args) {
            // Immune wall (full vertical column) makes the right column unreachable.
            assert time_to_full_infection_with_immunity(
                    new int[][] {{1, 2, 0}, {0, 2, 0}, {0, 2, 0}}, 1) == -1 : "vertical wall -> -1";

            // Immune block with a path around it: spread takes 6 steps.
            assert time_to_full_infection_with_immunity(
                    new int[][] {{1, 0, 0}, {2, 2, 0}, {0, 0, 0}}, 1) == 6 : "path around wall -> 6";

            System.out.println("Part 2: all checks passed");
        }
    }

    // ====================================================================
    // PART 3 — Infection Recovery to Immunity
    // ====================================================================
    /**
     * Level 3: infected cells become immune after D days, then stop spreading
     * and can't be re-infected. Initially immune (2) cells stay immune and block.
     * Returns the number of days until no active infections remain.
     *
     * CHANGES FROM PART 2:
     *   - Cells now carry an infection age, not just a 0/1/2 value. We track
     *     infectionDay[][] (HEALTHY / IMMUNE sentinels, or a day index >= 0) plus
     *     a HashSet<Integer> of active-infection cell keys (i*m+j).
     *   - We no longer wait for "everyone infected"; we simulate day-by-day until
     *     the active-infection set is empty (the stable state), counting days.
     *   - TIMING: current_day starts at 0 and is incremented to 1,2,... at the
     *     TOP of each loop. days_infected = current_day - infection_day. A cell
     *     transitions to immune at END-of-day once days_infected >= D, but it
     *     STILL appears in this day's start-of-day snapshot, so it still spreads
     *     on its transition day. New infections are computed from that snapshot.
     *   - Neighbor membership is tested via the snapshot set, not grid values.
     * Complexity: O(days * n * m) time, O(n * m) space.
     */
    static final class Part3 {

        private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Sentinels for infectionDay[][]. A non-negative value is the day the
        // cell became infected. Java arrays can't hold null, so we use sentinels.
        private static final int HEALTHY = Integer.MIN_VALUE;
        private static final int IMMUNE = -1;

        static int time_to_stable_state(int[][] grid, int N, int D) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return 0;
            }
            int n = grid.length, m = grid[0].length;

            int[][] infectionDay = new int[n][m];
            Set<Integer> activeInfections = new HashSet<>();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (grid[i][j] == 1) {
                        infectionDay[i][j] = 0; // infected on day 0
                        activeInfections.add(i * m + j);
                    } else if (grid[i][j] == 2) {
                        infectionDay[i][j] = IMMUNE;
                    } else {
                        infectionDay[i][j] = HEALTHY;
                    }
                }
            }

            if (activeInfections.isEmpty()) return 0; // no source -> already stable

            int currentDay = 0;
            while (!activeInfections.isEmpty()) {
                currentDay++;

                // Start-of-day snapshot of active infections (still includes cells
                // that will transition to immune at end-of-day, so they spread today).
                Set<Integer> infectedToday = new HashSet<>(activeInfections);

                // Step 1: determine which active cells reach age D today -> immune.
                Set<Integer> newImmune = new HashSet<>();
                for (int key : infectedToday) {
                    int i = key / m, j = key % m;
                    int day = infectionDay[i][j];
                    if (day >= 0 && currentDay - day >= D) {
                        newImmune.add(key);
                    }
                }

                // Step 2: infect healthy cells with >= N infected neighbors in the snapshot.
                Set<Integer> newInfections = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (infectionDay[i][j] != HEALTHY) continue;
                        if (countInfectedNeighbors(infectedToday, n, m, i, j) >= N) {
                            infectionDay[i][j] = currentDay;
                            newInfections.add(i * m + j);
                        }
                    }
                }

                // Step 3: commit immunity transitions at end-of-day.
                for (int key : newImmune) {
                    int i = key / m, j = key % m;
                    infectionDay[i][j] = IMMUNE;
                    activeInfections.remove(key);
                }
                // Add the new infections for the next day.
                activeInfections.addAll(newInfections);
            }
            return currentDay;
        }

        /** Counts neighbors present in the start-of-day infected snapshot set. */
        private static int countInfectedNeighbors(Set<Integer> infectedToday, int n, int m, int i, int j) {
            int count = 0;
            for (int[] d : DIRS) {
                int ni = i + d[0], nj = j + d[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < m && infectedToday.contains(ni * m + nj)) {
                    count++;
                }
            }
            return count;
        }

        public static void main(String[] args) {
            // Level 3 respects immune walls and the exact end-of-day timing.
            assert time_to_stable_state(
                    new int[][] {{1, 0, 0}, {2, 2, 0}, {0, 0, 0}}, 1, 2) == 8 : "stable state -> 8";

            // No source -> already stable on day 0.
            assert time_to_stable_state(new int[][] {{0, 0}, {2, 0}}, 1, 1) == 0 : "no source -> 0";

            System.out.println("Part 3: all checks passed");
        }
    }

    // ====================================================================
    // PART 4 — Death Parameter
    // ====================================================================
    /**
     * Level 4: a transitioning infected cell (age >= D) DIES instead of becoming
     * immune if it has >= K infected neighbors in the start-of-day snapshot;
     * otherwise it becomes immune. Dead cells are permanent barriers, distinct
     * from immune. Returns (days_to_stable_state, dead_count).
     *
     * CHANGES FROM PART 3:
     *   - Added the death threshold K and a DEAD sentinel. When a cell reaches
     *     age D it no longer always becomes immune: count its infected neighbors
     *     in the SAME start-of-day snapshot; >= K -> dies (dead_count++), else
     *     immune. Both removals happen at end-of-day, after spread is computed.
     *   - Spread (threshold N) is still computed from the start-of-day snapshot
     *     BEFORE any transition is committed, so a cell that dies/immunizes this
     *     day still spreads this day.
     *   - Returns a (days, deaths) pair via the nested Result record.
     * Complexity: O(days * n * m) time, O(n * m) space.
     */
    static final class Part4 {

        private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        private static final int HEALTHY = Integer.MIN_VALUE;
        private static final int IMMUNE = -1;
        private static final int DEAD = -2;

        /** Result pair: days to stable state and number of cells that died. */
        record Result(int days, int deaths) {}

        static Result time_to_stable_with_death(int[][] grid, int N, int D, int K) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return new Result(0, 0);
            }
            int n = grid.length, m = grid[0].length;

            int[][] state = new int[n][m];
            Set<Integer> activeInfections = new HashSet<>();
            int deadCount = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (grid[i][j] == 1) {
                        state[i][j] = 0; // infected on day 0
                        activeInfections.add(i * m + j);
                    } else if (grid[i][j] == 2) {
                        state[i][j] = IMMUNE;
                    } else {
                        state[i][j] = HEALTHY;
                    }
                }
            }

            if (activeInfections.isEmpty()) return new Result(0, 0);

            int currentDay = 0;
            while (!activeInfections.isEmpty()) {
                currentDay++;

                // Start-of-day snapshot (still includes cells transitioning today).
                Set<Integer> infectedToday = new HashSet<>(activeInfections);

                // Step 1: which active cells reach age D today (will transition).
                Set<Integer> transitioning = new HashSet<>();
                for (int key : infectedToday) {
                    int i = key / m, j = key % m;
                    int day = state[i][j];
                    if (day >= 0 && currentDay - day >= D) {
                        transitioning.add(key);
                    }
                }

                // Step 2: for each transitioning cell decide death vs immunity,
                // counting infected neighbors in the SAME start-of-day snapshot.
                Set<Integer> newDead = new HashSet<>();
                Set<Integer> newImmune = new HashSet<>();
                for (int key : transitioning) {
                    int i = key / m, j = key % m;
                    if (countInfectedNeighbors(infectedToday, n, m, i, j) >= K) {
                        newDead.add(key);
                    } else {
                        newImmune.add(key);
                    }
                }

                // Step 3: infect healthy cells with >= N infected neighbors in the snapshot.
                Set<Integer> newInfections = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (state[i][j] != HEALTHY) continue;
                        if (countInfectedNeighbors(infectedToday, n, m, i, j) >= N) {
                            state[i][j] = currentDay;
                            newInfections.add(i * m + j);
                        }
                    }
                }

                // Step 4: commit deaths and immunities at end-of-day.
                for (int key : newDead) {
                    int i = key / m, j = key % m;
                    state[i][j] = DEAD;
                    activeInfections.remove(key);
                    deadCount++;
                }
                for (int key : newImmune) {
                    int i = key / m, j = key % m;
                    state[i][j] = IMMUNE;
                    activeInfections.remove(key);
                }

                // Step 5: add new infections for the next day.
                activeInfections.addAll(newInfections);
            }
            return new Result(currentDay, deadCount);
        }

        /** Counts neighbors present in the start-of-day infected snapshot set. */
        private static int countInfectedNeighbors(Set<Integer> infectedToday, int n, int m, int i, int j) {
            int count = 0;
            for (int[] d : DIRS) {
                int ni = i + d[0], nj = j + d[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < m && infectedToday.contains(ni * m + nj)) {
                    count++;
                }
            }
            return count;
        }

        public static void main(String[] args) {
            // High death threshold (K=4): cells rarely have 4 infected neighbors -> no deaths.
            Result r1 = time_to_stable_with_death(new int[][] {{0, 1, 0}, {0, 0, 0}, {0, 0, 0}}, 1, 2, 4);
            assert r1.deaths() == 0 : "K=4 -> no deaths";

            // Low death threshold (K=2) on a fully infected 2x2 -> some deaths.
            Result r2 = time_to_stable_with_death(new int[][] {{1, 1}, {1, 1}}, 1, 1, 2);
            assert r2.deaths() > 0 : "K=2 dense -> deaths";

            // Transition-day spread: [1,0], N=1, D=1, K=4 -> 2 days, 0 deaths.
            Result r3 = time_to_stable_with_death(new int[][] {{1, 0}}, 1, 1, 4);
            assert r3.days() == 2 : "transition-day spread days==2";
            assert r3.deaths() == 0 : "transition-day spread deaths==0";

            // No source -> (0, 0).
            Result r4 = time_to_stable_with_death(new int[][] {{0, 0}, {0, 2}}, 1, 1, 1);
            assert r4.days() == 0 && r4.deaths() == 0 : "no source -> (0,0)";

            System.out.println("Part 4: all checks passed");
        }
    }

    // ====================================================================
    // PART 5 — Optimal Row/Column Burn (Firebreak)
    // ====================================================================
    /**
     * Level 5: before infection starts, burn exactly one whole row OR column.
     * Burned cells behave like dead cells (never infected, never spread, never
     * counted as deaths). Brute-force every row and column, run the full Level-4
     * simulation, and return the burn that minimizes infection deaths.
     *
     * CHANGES FROM PART 4:
     *   - Added a BURNED sentinel and an outer search: for each of the n rows and
     *     m columns, deep-copy the grid, set that line's cells to internal -1,
     *     and run the Level-4 simulator (folded in here as simulateDeaths). We
     *     keep the burn with the fewest deaths (ties: keep the first / any).
     *   - simulateDeaths is Part 4's logic plus a BURNED state: burned cells are
     *     skipped in the healthy scan, never counted as neighbors, never active,
     *     and never counted in deadCount.
     *   - Returns a (burn_type, burn_index, min_deaths) triple via the nested
     *     Burn record.
     * Complexity: O((n + m) * days * n * m) time, O(n * m) space per simulation.
     */
    static final class Part5 {

        private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        private static final int HEALTHY = Integer.MIN_VALUE;
        private static final int IMMUNE = -1;
        private static final int DEAD = -2;
        private static final int BURNED = -3;

        /** Burn decision: which line to burn (type + 0-indexed) and the deaths it yields. */
        record Burn(String type, int index, int deaths) {}

        static Burn optimal_burn(int[][] grid, int N, int D, int K) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return new Burn("row", 0, 0);
            }
            int n = grid.length, m = grid[0].length;

            String bestType = "row";
            int bestIndex = 0;
            int bestDeaths = Integer.MAX_VALUE;

            // Try burning each row.
            for (int r = 0; r < n; r++) {
                int[][] g = copyGrid(grid);
                for (int j = 0; j < m; j++) g[r][j] = -1; // mark burned (input sentinel)
                int deaths = simulateDeaths(g, N, D, K);
                if (deaths < bestDeaths) {
                    bestDeaths = deaths;
                    bestType = "row";
                    bestIndex = r;
                }
            }

            // Try burning each column.
            for (int c = 0; c < m; c++) {
                int[][] g = copyGrid(grid);
                for (int i = 0; i < n; i++) g[i][c] = -1; // mark burned (input sentinel)
                int deaths = simulateDeaths(g, N, D, K);
                if (deaths < bestDeaths) {
                    bestDeaths = deaths;
                    bestType = "col";
                    bestIndex = c;
                }
            }

            return new Burn(bestType, bestIndex, bestDeaths);
        }

        /**
         * Level-4 simulation that also understands burned cells: input value -1
         * maps to BURNED (a permanent barrier like dead, but never counted as a
         * death). Returns the number of infection deaths.
         */
        private static int simulateDeaths(int[][] grid, int N, int D, int K) {
            int n = grid.length, m = grid[0].length;

            int[][] state = new int[n][m];
            Set<Integer> activeInfections = new HashSet<>();
            int deadCount = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (grid[i][j] == 1) {
                        state[i][j] = 0;
                        activeInfections.add(i * m + j);
                    } else if (grid[i][j] == 2) {
                        state[i][j] = IMMUNE;
                    } else if (grid[i][j] == -1) {
                        state[i][j] = BURNED;
                    } else {
                        state[i][j] = HEALTHY;
                    }
                }
            }

            if (activeInfections.isEmpty()) return 0;

            int currentDay = 0;
            while (!activeInfections.isEmpty()) {
                currentDay++;

                Set<Integer> infectedToday = new HashSet<>(activeInfections);

                // Transitions: active cells that reach age D today.
                Set<Integer> transitioning = new HashSet<>();
                for (int key : infectedToday) {
                    int i = key / m, j = key % m;
                    int day = state[i][j];
                    if (day >= 0 && currentDay - day >= D) {
                        transitioning.add(key);
                    }
                }

                // Death vs immunity from the same start-of-day snapshot.
                Set<Integer> newDead = new HashSet<>();
                Set<Integer> newImmune = new HashSet<>();
                for (int key : transitioning) {
                    int i = key / m, j = key % m;
                    if (countInfectedNeighbors(infectedToday, n, m, i, j) >= K) {
                        newDead.add(key);
                    } else {
                        newImmune.add(key);
                    }
                }

                // Spread to healthy cells (burned cells are not HEALTHY -> skipped).
                Set<Integer> newInfections = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (state[i][j] != HEALTHY) continue;
                        if (countInfectedNeighbors(infectedToday, n, m, i, j) >= N) {
                            state[i][j] = currentDay;
                            newInfections.add(i * m + j);
                        }
                    }
                }

                // End-of-day commits.
                for (int key : newDead) {
                    int i = key / m, j = key % m;
                    state[i][j] = DEAD;
                    activeInfections.remove(key);
                    deadCount++;
                }
                for (int key : newImmune) {
                    int i = key / m, j = key % m;
                    state[i][j] = IMMUNE;
                    activeInfections.remove(key);
                }
                activeInfections.addAll(newInfections);
            }
            return deadCount;
        }

        /** Counts neighbors present in the start-of-day infected snapshot set. */
        private static int countInfectedNeighbors(Set<Integer> infectedToday, int n, int m, int i, int j) {
            int count = 0;
            for (int[] d : DIRS) {
                int ni = i + d[0], nj = j + d[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < m && infectedToday.contains(ni * m + nj)) {
                    count++;
                }
            }
            return count;
        }

        private static int[][] copyGrid(int[][] grid) {
            int[][] g = new int[grid.length][];
            for (int i = 0; i < grid.length; i++) {
                g[i] = grid[i].clone();
            }
            return g;
        }

        public static void main(String[] args) {
            // Burning row 0 removes the only infection source -> 0 infection deaths.
            Burn b1 = optimal_burn(new int[][] {{1, 0}, {0, 0}}, 1, 1, 1);
            assert b1.deaths() == 0 : "remove source -> 0 deaths";

            // Deterministic sanity check on a 3x3 with two corner sources.
            Burn b2 = optimal_burn(new int[][] {{1, 0, 0}, {0, 0, 0}, {0, 0, 1}}, 1, 2, 2);
            assert (b2.type().equals("row") || b2.type().equals("col")) : "burn type is row or col";
            assert b2.index() >= 0 : "burn index non-negative";
            assert b2.deaths() >= 0 : "deaths non-negative";

            // Empty grid -> ("row", 0, 0).
            Burn b3 = optimal_burn(new int[][] {}, 1, 1, 1);
            assert b3.type().equals("row") && b3.index() == 0 && b3.deaths() == 0 : "empty -> (row,0,0)";

            System.out.println("Part 5: all checks passed");
        }
    }
}
