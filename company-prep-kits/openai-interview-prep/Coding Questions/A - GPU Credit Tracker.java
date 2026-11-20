/*
 * GPU Credit Tracker — OpenAI Staff SWE coding question.
 *
 * TWO-PART problem. Both parts solve the same spec; Part2 is the optimized
 * follow-up. Each part lives in its own fully self-contained nested class so it
 * can be lifted into a standalone .java by simply dropping `static` from the
 * class header (a top-level class cannot be static) — nothing else changes.
 * Each part uses fully-qualified java.util names (e.g. java.util.List,
 * java.util.HashMap, java.util.PriorityQueue) precisely so NO import lines are
 * needed: dropping `static` is literally the ONLY edit required to promote it.
 *
 * ------------------------------------------------------------------------
 * PROBLEM
 * ------------------------------------------------------------------------
 * A GPU credit ledger supporting three operations that can arrive OUT OF ORDER:
 *   add_credit(credit_id, amount, timestamp, expiration)
 *       Add a credit valid in [timestamp, timestamp + expiration] (INCLUSIVE).
 *   subtract(amount, timestamp)
 *       Consume credit at `timestamp`, SOONEST-EXPIRING first. Never raises if
 *       there is insufficient credit at that moment (balance may go negative).
 *   get_balance(timestamp) -> Integer | null
 *       Balance at `timestamp`, or null when EITHER no credit window is active
 *       at that timestamp OR the reconstructed balance is negative.
 *
 * Because operations arrive out of order, we must reconstruct state by EVENT
 * TIME, not insertion order. So add_credit/subtract merely APPEND to two lists;
 * ALL logic is in get_balance, which builds a filtered+sorted event stream up
 * to the query timestamp and replays it.
 *
 * ------------------------------------------------------------------------
 * EVENT MODEL (each part declares its own identical copy)
 * ------------------------------------------------------------------------
 * For a query at time t we synthesize three kinds of events:
 *   ADD     at grant.start          (only if grant.start  <= t)
 *   EXPIRE  at grant.end + 1         (only if grant.end+1  <= t)  // end is the
 *                                        last VALID instant; the credit is gone
 *                                        at end+1 because the window is inclusive
 *   SUBTRACT at sub.timestamp        (only if sub.timestamp <= t)
 *
 * Events are totally ordered by (time, priority, seq):
 *   1. time ascending — chronological replay.
 *   2. priority EXPIRE(0) < ADD(1) < SUBTRACT(2):
 *        - EXPIRE-before-ADD: a grant expiring at end+1 that coincides with
 *          another grant's start is removed BEFORE the new one is added (expire
 *          is strictly after the valid window, so this is correct).
 *        - ADD-before-SUBTRACT: a credit added at instant T is spendable by a
 *          subtract at T. (The problem guarantees no add+subtract share a
 *          timestamp, but the ordering must still be principled and total.)
 *   3. seq ascending (event-construction order) — makes the order fully
 *      deterministic, matching the Python reference's tuple sort; we do NOT
 *      rely on sort stability.
 *
 * Replay invariants:
 *   - activeWindows counts how many credit windows currently cover the instant.
 *     If it is 0 at the end, there is no active window -> return null.
 *   - deficit is outstanding unmet demand from underfunded subtracts. A newly
 *     ADDed credit first pays down any deficit before contributing to balance,
 *     which is how a late-arriving credit (added at an EARLIER event time than a
 *     subtract that already consumed it) reconciles an out-of-order subtract.
 *   - SUBTRACT consumes eligible pools (remaining > 0 and not yet expired)
 *     soonest-expiring-first; any shortfall increases deficit.
 *   - Final balance = available - deficit; return null if negative.
 *
 * ------------------------------------------------------------------------
 * APPROACHES
 * ------------------------------------------------------------------------
 *   Part1 — Replay + Sort: per subtract, collect eligible pools into a list and
 *           sort by expiration. Simple to explain/debug.
 *           Time  O((C+S) log (C+S) + S*C*log C) worst case; Space O(C+S).
 *   Part2 — Replay + Min-Heap: maintain a lazily-pruned min-heap keyed by
 *           expiration so each subtract pops the soonest-expiring pool without a
 *           per-subtract re-sort, plus a running totalAvailable.
 *           Time  O((C+S) log (C+S) + K log C); Space O(C+S). (K = heap ops.)
 *   (C = #credit grants, S = #subtract events.)
 *
 * COMPILE / RUN (filename has spaces + hyphen and does not match the class
 * name; that is fine because the top-level class is package-private):
 *   javac -d /tmp/out "A - GPU Credit Tracker.java"
 *   java -cp /tmp/out GpuCreditTracker          # runs BOTH parts (default)
 *   java -cp /tmp/out GpuCreditTracker part1     # runs Part1 only
 *   java -cp /tmp/out GpuCreditTracker part2     # runs Part2 only
 */

/**
 * Top-level dispatcher. Package-private (NOT public) so the filename need not
 * match the class name. Delegates to the two self-contained parts.
 */
class GpuCreditTracker {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("part1")) {
            Part1.main(args);
        } else if (args.length > 0 && args[0].equals("part2")) {
            Part2.main(args);
        } else if (args.length == 0) {
            // No argument: run BOTH parts.
            Part1.main(args);
            Part2.main(args);
        } else {
            System.out.println("Unknown part: " + args[0] + " (use 'part1' or 'part2')");
        }
    }

    /**
     * Part1 — Replay + Sort. Fully self-contained: every type, comparator,
     * field, method, and test lives inside this class with ZERO references to
     * the outer class or to Part2. It uses fully-qualified java.util names so no
     * import is needed. Promotion contract: copy this class into its own .java
     * and remove `static` from the header — that is the ONLY edit; it compiles
     * and runs unchanged.
     */
    static final class Part1 {

        /** A single credit grant. Plain final class (not a record). */
        static final class CreditGrant {
            String creditId;
            long amount;
            long start;
            long end; // last VALID instant (inclusive)

            CreditGrant(String creditId, long amount, long start, long end) {
                this.creditId = creditId;
                this.amount = amount;
                this.start = start;
                this.end = end;
            }
        }

        /** A single subtract (usage) event. */
        static final class SubtractEvent {
            long amount;
            long timestamp;

            SubtractEvent(long amount, long timestamp) {
                this.amount = amount;
                this.timestamp = timestamp;
            }
        }

        /**
         * A synthesized replay event. `priority` doubles as the kind
         * discriminator: EXPIRE=0, ADD=1, SUBTRACT=2. grantKey indexes into
         * `credits` for ADD/EXPIRE and is -1 for SUBTRACT; subAmount is used
         * only by SUBTRACT.
         */
        static final class Event {
            long time;
            int priority;
            int seq;
            int grantKey;
            long subAmount;

            Event(long time, int priority, int seq, int grantKey, long subAmount) {
                this.time = time;
                this.priority = priority;
                this.seq = seq;
                this.grantKey = grantKey;
                this.subAmount = subAmount;
            }
        }

        /** Total order: time asc, then priority (EXPIRE<ADD<SUBTRACT), then seq. */
        private static final java.util.Comparator<Event> CMP =
                java.util.Comparator.comparingLong((Event e) -> e.time)
                        .thenComparingInt(e -> e.priority)
                        .thenComparingInt(e -> e.seq);

        // Append-only ledgers; all logic is replayed in getBalance.
        private final java.util.List<CreditGrant> credits = new java.util.ArrayList<>();
        private final java.util.List<SubtractEvent> subtracts = new java.util.ArrayList<>();

        void addCredit(String creditId, int amount, int timestamp, int expiration) {
            if (amount < 0 || expiration < 0) {
                throw new IllegalArgumentException("amount and expiration must be non-negative");
            }
            // end = last valid instant; use long to avoid int overflow.
            long end = (long) timestamp + expiration;
            credits.add(new CreditGrant(creditId, amount, timestamp, end));
        }

        void subtract(int amount, int timestamp) {
            if (amount < 0) {
                throw new IllegalArgumentException("amount must be non-negative");
            }
            subtracts.add(new SubtractEvent(amount, timestamp));
        }

        /** Returns balance at `timestamp`, or null (no active window OR negative). */
        Integer getBalance(int timestamp) {
            long t = timestamp;

            // ---- Build the filtered event stream up to t (shared seq counter). ----
            java.util.List<Event> events = new java.util.ArrayList<>();
            int seq = 0;
            for (int grantKey = 0; grantKey < credits.size(); grantKey++) {
                CreditGrant g = credits.get(grantKey);
                if (g.start <= t) {
                    events.add(new Event(g.start, 1, seq++, grantKey, 0)); // ADD
                }
                if (g.end + 1 <= t) {
                    events.add(new Event(g.end + 1, 0, seq++, grantKey, 0)); // EXPIRE
                }
            }
            for (SubtractEvent s : subtracts) {
                if (s.timestamp <= t) {
                    events.add(new Event(s.timestamp, 2, seq++, -1, s.amount)); // SUBTRACT
                }
            }
            events.sort(CMP);

            // ---- Replay. active: grantKey -> [remaining, end]. ----
            java.util.Map<Integer, long[]> active = new java.util.HashMap<>();
            int activeWindows = 0;
            long deficit = 0;

            for (Event ev : events) {
                long now = ev.time;
                if (ev.priority == 1) { // ADD
                    CreditGrant g = credits.get(ev.grantKey);
                    activeWindows++;
                    long amount = g.amount;
                    // A new credit first offsets any outstanding deficit.
                    if (deficit > 0) {
                        long paid = Math.min(deficit, amount);
                        deficit -= paid;
                        amount -= paid;
                    }
                    // Store even if amount == 0 (the window is still active).
                    active.put(ev.grantKey, new long[] {amount, g.end});
                } else if (ev.priority == 0) { // EXPIRE
                    activeWindows--;
                    active.remove(ev.grantKey); // drop remainder, even if 0
                } else { // SUBTRACT
                    long toUse = ev.subAmount;
                    // Eligible pools: remaining > 0 and not yet expired at `now`.
                    java.util.List<long[]> eligible = new java.util.ArrayList<>(); // {end, grantKey}
                    for (java.util.Map.Entry<Integer, long[]> e : active.entrySet()) {
                        long[] v = e.getValue();
                        if (v[0] > 0 && v[1] >= now) {
                            eligible.add(new long[] {v[1], e.getKey()});
                        }
                    }
                    // Soonest-expiring first; grantKey breaks ties deterministically.
                    eligible.sort(java.util.Comparator.comparingLong((long[] a) -> a[0])
                            .thenComparingLong(a -> a[1]));
                    for (long[] pool : eligible) {
                        if (toUse == 0) {
                            break;
                        }
                        int gk = (int) pool[1];
                        long rem = active.get(gk)[0];
                        long take = Math.min(rem, toUse);
                        active.get(gk)[0] -= take;
                        toUse -= take;
                    }
                    // Insufficient credit at this instant -> goes (more) negative.
                    if (toUse > 0) {
                        deficit += toUse;
                    }
                }
            }

            if (activeWindows == 0) {
                return null; // no active credit window at t
            }
            long available = 0;
            for (long[] v : active.values()) {
                available += v[0];
            }
            long balance = available - deficit;
            return balance >= 0 ? (int) balance : null;
        }

        // ----------------------------------------------------------------
        // Embedded tests. Each scenario uses a fresh `new Part1()`.
        // ----------------------------------------------------------------

        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError(msg);
            }
        }

        /** Assert getBalance result equals expected (both may be null). */
        private static void expect(Integer actual, Integer expected, String label) {
            boolean ok = (actual == null) ? (expected == null) : actual.equals(expected);
            check(ok, label + ": expected " + expected + " but got " + actual);
        }

        public static void main(String[] args) {
            // Example 1: basic credit, valid [10, 40].
            Part1 p = new Part1();
            p.addCredit("microsoft", 10, 10, 30);
            expect(p.getBalance(0), null, "ex1 t=0");
            expect(p.getBalance(10), 10, "ex1 t=10");
            expect(p.getBalance(40), 10, "ex1 t=40");
            expect(p.getBalance(41), null, "ex1 t=41");

            // Example 2: subtraction, valid [10, 60].
            p = new Part1();
            p.addCredit("amazon", 40, 10, 50);
            p.subtract(30, 30);
            expect(p.getBalance(10), 40, "ex2 t=10");
            expect(p.getBalance(30), 10, "ex2 t=30");
            expect(p.getBalance(60), 10, "ex2 t=60");
            expect(p.getBalance(61), null, "ex2 t=61");

            // Example 3: out-of-order (subtract before credit), valid [20, 50].
            p = new Part1();
            p.subtract(4, 30);
            p.addCredit("a", 4, 20, 30);
            expect(p.getBalance(10), null, "ex3 t=10");
            expect(p.getBalance(20), 4, "ex3 t=20");
            expect(p.getBalance(30), 0, "ex3 t=30");
            expect(p.getBalance(50), 0, "ex3 t=50");
            expect(p.getBalance(51), null, "ex3 t=51");

            // Example 4: negative balance, valid [10, 40].
            p = new Part1();
            p.addCredit("openai", 10, 10, 30);
            p.subtract(100, 20);
            expect(p.getBalance(10), 10, "ex4 t=10");
            expect(p.getBalance(20), null, "ex4 t=20");
            expect(p.getBalance(30), null, "ex4 t=30");
            // Extra: still in [10,40] but negative -> null; expired at 41 -> null.
            expect(p.getBalance(40), null, "ex4 t=40");
            expect(p.getBalance(41), null, "ex4 t=41");

            // Example 5: repeated credit id, windows [10,60] and [30,80].
            p = new Part1();
            p.addCredit("openai", 10, 10, 50);
            p.addCredit("openai", 10, 30, 50);
            p.subtract(15, 35);
            expect(p.getBalance(10), 10, "ex5 t=10");
            expect(p.getBalance(30), 20, "ex5 t=30");
            expect(p.getBalance(35), 5, "ex5 t=35");
            expect(p.getBalance(70), 5, "ex5 t=70");

            // Smoke A.
            p = new Part1();
            p.addCredit("a", 4, 20, 30);
            expect(p.getBalance(10), null, "smokeA t=10");
            expect(p.getBalance(20), 4, "smokeA t=20");
            expect(p.getBalance(50), 4, "smokeA t=50");
            expect(p.getBalance(51), null, "smokeA t=51");

            // Smoke B: earliest-expiration-first. [20,60] and [30,40].
            p = new Part1();
            p.addCredit("a", 4, 20, 40);
            p.addCredit("b", 3, 30, 10);
            p.subtract(2, 30);
            expect(p.getBalance(30), 5, "smokeB t=30");
            expect(p.getBalance(40), 5, "smokeB t=40");
            expect(p.getBalance(41), 4, "smokeB t=41"); // 'b' (end 40) consumed first

            // Smoke C.
            p = new Part1();
            p.subtract(4, 30);
            p.addCredit("a", 4, 20, 30);
            expect(p.getBalance(10), null, "smokeC t=10");
            expect(p.getBalance(20), 4, "smokeC t=20");
            expect(p.getBalance(30), 0, "smokeC t=30");

            // Smoke D.
            p = new Part1();
            p.addCredit("a", 10, 10, 30);
            p.subtract(100, 20);
            expect(p.getBalance(10), 10, "smokeD t=10");
            expect(p.getBalance(20), null, "smokeD t=20");

            // Smoke E.
            p = new Part1();
            p.addCredit("openai", 10, 10, 50);
            p.addCredit("openai", 10, 30, 50);
            p.subtract(15, 35);
            expect(p.getBalance(10), 10, "smokeE t=10");
            expect(p.getBalance(30), 20, "smokeE t=30");
            expect(p.getBalance(35), 5, "smokeE t=35");
            expect(p.getBalance(70), 5, "smokeE t=70");

            // Edge: amount=0 credit, [10,15]. Active window with balance 0 -> 0, not null.
            p = new Part1();
            p.addCredit("z", 0, 10, 5);
            expect(p.getBalance(9), null, "edge0 t=9");
            expect(p.getBalance(10), 0, "edge0 t=10");
            expect(p.getBalance(15), 0, "edge0 t=15");
            expect(p.getBalance(16), null, "edge0 t=16");

            // Edge: expiration=0 single-instant window [10,10].
            p = new Part1();
            p.addCredit("p", 5, 10, 0);
            expect(p.getBalance(9), null, "edgeInst t=9");
            expect(p.getBalance(10), 5, "edgeInst t=10");
            expect(p.getBalance(11), null, "edgeInst t=11");

            // Edge: query before any event / only-future credit.
            p = new Part1();
            expect(p.getBalance(5), null, "edgeEmpty t=5");
            p = new Part1();
            p.addCredit("f", 5, 100, 10);
            expect(p.getBalance(50), null, "edgeFuture t=50");

            // Edge: exactly at end vs end+1, [10,15].
            p = new Part1();
            p.addCredit("q", 7, 10, 5);
            expect(p.getBalance(15), 7, "edgeEnd t=15");
            expect(p.getBalance(16), null, "edgeEnd t=16");

            // Edge: pure deficit, no credits -> activeWindows==0 short-circuits.
            p = new Part1();
            p.subtract(5, 10);
            expect(p.getBalance(10), null, "edgeDeficit t=10");
            expect(p.getBalance(0), null, "edgeDeficit t=0");

            // Edge: out-of-order deficit pay-down by a later-arriving credit.
            // [10,110] amount 3, [30,130] amount 10, subtract 8 at t=20.
            p = new Part1();
            p.subtract(8, 20);
            p.addCredit("g1", 3, 10, 100);
            p.addCredit("g2", 10, 30, 100);
            expect(p.getBalance(20), null, "edgePaydown t=20"); // avail 3 - 8 = -5 -> null
            expect(p.getBalance(30), 5, "edgePaydown t=30");     // g2 pays deficit 5 -> 5

            // Reported interviewer test: ONE subtract draining across THREE grants
            // with a partial take on the last, grants ADDED OUT OF EXPIRY ORDER to
            // prove insertion order is irrelevant and only expiry order governs.
            //   c1 valid [10,40] (dies first), c3 [20,50], c4 [30,60], c2 [40,70]
            //   subtract(45, 30) -> 20 from c1, 20 from c3, 5 from c4
            p = new Part1();
            p.addCredit("c1", 20, 10, 30);   // through 40
            p.addCredit("c2", 20, 40, 30);   // through 70
            p.addCredit("c3", 20, 20, 30);   // through 50
            p.addCredit("c4", 20, 30, 30);   // through 60
            p.subtract(45, 30);
            // At t=30: c1 and c3 drained to 0, c4 has 15 left, c2 not yet active.
            expect(p.getBalance(30), 15, "spanThreeGrants t=30");
            // At t=55: c1 and c3 expired, c4 has 15, c2 has 20 -> 35.
            expect(p.getBalance(55), 35, "spanThreeGrants t=55");

            System.out.println("Part1: all tests passed");
        }
    }

    /**
     * Part2 — Replay + Min-Heap (optimized follow-up). Fully self-contained and
     * uses fully-qualified java.util names so no import is needed (same
     * promotion contract as Part1: drop `static` from the header and nothing
     * else changes — it compiles and runs unchanged).
     *
     * CHANGES FROM PART 1:
     *   (1) Replace java.util.Map<Integer,long[]> active with
     *       java.util.Map<Integer,Long> remaining PLUS a
     *       java.util.PriorityQueue<long[]> heap of {end, grantKey} (min by end,
     *       then grantKey).
     *   (2) Maintain a running `long totalAvailable` instead of re-summing pools.
     *   (3) ADD also pushes onto the heap and bumps totalAvailable — but only
     *       when the (post-deficit) amount > 0.
     *   (4) EXPIRE zeroes the grant's remaining and decrements totalAvailable,
     *       leaving any stale heap entry to be lazily pruned.
     *   (5) SUBTRACT uses a heap loop with prune(now) instead of collect+sort,
     *       re-pushing a partially-consumed pool.
     *   (6) Final balance = totalAvailable - deficit (no re-sum).
     *   (7) Added a prune(now) helper for lazy pruning of stale/expired heads.
     *   (8) Complexity improves S*C*logC -> K*logC.
     * No public API / field-name / type-shape changes versus the spec.
     */
    static final class Part2 {

        static final class CreditGrant {
            String creditId;
            long amount;
            long start;
            long end; // last VALID instant (inclusive)

            CreditGrant(String creditId, long amount, long start, long end) {
                this.creditId = creditId;
                this.amount = amount;
                this.start = start;
                this.end = end;
            }
        }

        static final class SubtractEvent {
            long amount;
            long timestamp;

            SubtractEvent(long amount, long timestamp) {
                this.amount = amount;
                this.timestamp = timestamp;
            }
        }

        static final class Event {
            long time;
            int priority; // EXPIRE=0, ADD=1, SUBTRACT=2
            int seq;
            int grantKey; // -1 for SUBTRACT
            long subAmount; // SUBTRACT only

            Event(long time, int priority, int seq, int grantKey, long subAmount) {
                this.time = time;
                this.priority = priority;
                this.seq = seq;
                this.grantKey = grantKey;
                this.subAmount = subAmount;
            }
        }

        private static final java.util.Comparator<Event> CMP =
                java.util.Comparator.comparingLong((Event e) -> e.time)
                        .thenComparingInt(e -> e.priority)
                        .thenComparingInt(e -> e.seq);

        private final java.util.List<CreditGrant> credits = new java.util.ArrayList<>();
        private final java.util.List<SubtractEvent> subtracts = new java.util.ArrayList<>();

        void addCredit(String creditId, int amount, int timestamp, int expiration) {
            if (amount < 0 || expiration < 0) {
                throw new IllegalArgumentException("amount and expiration must be non-negative");
            }
            long end = (long) timestamp + expiration;
            credits.add(new CreditGrant(creditId, amount, timestamp, end));
        }

        void subtract(int amount, int timestamp) {
            if (amount < 0) {
                throw new IllegalArgumentException("amount must be non-negative");
            }
            subtracts.add(new SubtractEvent(amount, timestamp));
        }

        Integer getBalance(int timestamp) {
            long t = timestamp;

            // ---- Build the filtered event stream up to t (shared seq counter). ----
            java.util.List<Event> events = new java.util.ArrayList<>();
            int seq = 0;
            for (int grantKey = 0; grantKey < credits.size(); grantKey++) {
                CreditGrant g = credits.get(grantKey);
                if (g.start <= t) {
                    events.add(new Event(g.start, 1, seq++, grantKey, 0)); // ADD
                }
                if (g.end + 1 <= t) {
                    events.add(new Event(g.end + 1, 0, seq++, grantKey, 0)); // EXPIRE
                }
            }
            for (SubtractEvent s : subtracts) {
                if (s.timestamp <= t) {
                    events.add(new Event(s.timestamp, 2, seq++, -1, s.amount)); // SUBTRACT
                }
            }
            events.sort(CMP);

            // ---- Replay state ----
            java.util.Map<Integer, Long> remaining = new java.util.HashMap<>(); // grantKey -> remaining amount
            long totalAvailable = 0;
            long deficit = 0;
            int activeWindows = 0;
            // Min-heap of {end, grantKey}; stale entries lazily pruned.
            java.util.PriorityQueue<long[]> heap = new java.util.PriorityQueue<>(
                    java.util.Comparator.comparingLong((long[] a) -> a[0]).thenComparingLong(a -> a[1]));

            for (Event ev : events) {
                long now = ev.time;
                if (ev.priority == 1) { // ADD
                    CreditGrant g = credits.get(ev.grantKey);
                    activeWindows++;
                    long amount = g.amount;
                    if (deficit > 0) {
                        long paid = Math.min(deficit, amount);
                        deficit -= paid;
                        amount -= paid;
                    }
                    remaining.put(ev.grantKey, amount);
                    if (amount > 0) {
                        totalAvailable += amount;
                        heap.add(new long[] {g.end, ev.grantKey});
                    }
                } else if (ev.priority == 0) { // EXPIRE
                    activeWindows--;
                    long rem = remaining.getOrDefault(ev.grantKey, 0L);
                    if (rem > 0) {
                        totalAvailable -= rem;
                        remaining.put(ev.grantKey, 0L);
                    }
                    // Leave any stale heap entry; prune(now) drops it lazily.
                } else { // SUBTRACT
                    long toUse = ev.subAmount;
                    prune(heap, remaining, now);
                    while (toUse > 0) {
                        prune(heap, remaining, now);
                        if (heap.isEmpty()) {
                            break;
                        }
                        long[] top = heap.poll();
                        long end = top[0];
                        int gk = (int) top[1];
                        long rem = remaining.getOrDefault(gk, 0L);
                        if (rem == 0 || end < now) {
                            continue; // stale entry
                        }
                        long take = Math.min(rem, toUse);
                        rem -= take;
                        toUse -= take;
                        totalAvailable -= take;
                        remaining.put(gk, rem);
                        if (rem > 0) {
                            heap.add(new long[] {end, gk}); // re-push partial pool
                        }
                    }
                    if (toUse > 0) {
                        deficit += toUse;
                    }
                }
            }

            if (activeWindows == 0) {
                return null;
            }
            long balance = totalAvailable - deficit;
            return balance >= 0 ? (int) balance : null;
        }

        /** Drop heap heads that are expired (end < now) or fully consumed. */
        private static void prune(java.util.PriorityQueue<long[]> heap, java.util.Map<Integer, Long> remaining, long now) {
            while (!heap.isEmpty()) {
                long[] head = heap.peek();
                if (head[0] < now || remaining.getOrDefault((int) head[1], 0L) == 0) {
                    heap.poll();
                } else {
                    break;
                }
            }
        }

        // ----------------------------------------------------------------
        // Embedded tests (identical scenarios to Part1; fresh `new Part2()`).
        // ----------------------------------------------------------------

        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError(msg);
            }
        }

        private static void expect(Integer actual, Integer expected, String label) {
            boolean ok = (actual == null) ? (expected == null) : actual.equals(expected);
            check(ok, label + ": expected " + expected + " but got " + actual);
        }

        public static void main(String[] args) {
            // Example 1.
            Part2 p = new Part2();
            p.addCredit("microsoft", 10, 10, 30);
            expect(p.getBalance(0), null, "ex1 t=0");
            expect(p.getBalance(10), 10, "ex1 t=10");
            expect(p.getBalance(40), 10, "ex1 t=40");
            expect(p.getBalance(41), null, "ex1 t=41");

            // Example 2.
            p = new Part2();
            p.addCredit("amazon", 40, 10, 50);
            p.subtract(30, 30);
            expect(p.getBalance(10), 40, "ex2 t=10");
            expect(p.getBalance(30), 10, "ex2 t=30");
            expect(p.getBalance(60), 10, "ex2 t=60");
            expect(p.getBalance(61), null, "ex2 t=61");

            // Example 3.
            p = new Part2();
            p.subtract(4, 30);
            p.addCredit("a", 4, 20, 30);
            expect(p.getBalance(10), null, "ex3 t=10");
            expect(p.getBalance(20), 4, "ex3 t=20");
            expect(p.getBalance(30), 0, "ex3 t=30");
            expect(p.getBalance(50), 0, "ex3 t=50");
            expect(p.getBalance(51), null, "ex3 t=51");

            // Example 4.
            p = new Part2();
            p.addCredit("openai", 10, 10, 30);
            p.subtract(100, 20);
            expect(p.getBalance(10), 10, "ex4 t=10");
            expect(p.getBalance(20), null, "ex4 t=20");
            expect(p.getBalance(30), null, "ex4 t=30");
            expect(p.getBalance(40), null, "ex4 t=40");
            expect(p.getBalance(41), null, "ex4 t=41");

            // Example 5.
            p = new Part2();
            p.addCredit("openai", 10, 10, 50);
            p.addCredit("openai", 10, 30, 50);
            p.subtract(15, 35);
            expect(p.getBalance(10), 10, "ex5 t=10");
            expect(p.getBalance(30), 20, "ex5 t=30");
            expect(p.getBalance(35), 5, "ex5 t=35");
            expect(p.getBalance(70), 5, "ex5 t=70");

            // Smoke A.
            p = new Part2();
            p.addCredit("a", 4, 20, 30);
            expect(p.getBalance(10), null, "smokeA t=10");
            expect(p.getBalance(20), 4, "smokeA t=20");
            expect(p.getBalance(50), 4, "smokeA t=50");
            expect(p.getBalance(51), null, "smokeA t=51");

            // Smoke B.
            p = new Part2();
            p.addCredit("a", 4, 20, 40);
            p.addCredit("b", 3, 30, 10);
            p.subtract(2, 30);
            expect(p.getBalance(30), 5, "smokeB t=30");
            expect(p.getBalance(40), 5, "smokeB t=40");
            expect(p.getBalance(41), 4, "smokeB t=41");

            // Smoke C.
            p = new Part2();
            p.subtract(4, 30);
            p.addCredit("a", 4, 20, 30);
            expect(p.getBalance(10), null, "smokeC t=10");
            expect(p.getBalance(20), 4, "smokeC t=20");
            expect(p.getBalance(30), 0, "smokeC t=30");

            // Smoke D.
            p = new Part2();
            p.addCredit("a", 10, 10, 30);
            p.subtract(100, 20);
            expect(p.getBalance(10), 10, "smokeD t=10");
            expect(p.getBalance(20), null, "smokeD t=20");

            // Smoke E.
            p = new Part2();
            p.addCredit("openai", 10, 10, 50);
            p.addCredit("openai", 10, 30, 50);
            p.subtract(15, 35);
            expect(p.getBalance(10), 10, "smokeE t=10");
            expect(p.getBalance(30), 20, "smokeE t=30");
            expect(p.getBalance(35), 5, "smokeE t=35");
            expect(p.getBalance(70), 5, "smokeE t=70");

            // Edge: amount=0 credit, [10,15].
            p = new Part2();
            p.addCredit("z", 0, 10, 5);
            expect(p.getBalance(9), null, "edge0 t=9");
            expect(p.getBalance(10), 0, "edge0 t=10");
            expect(p.getBalance(15), 0, "edge0 t=15");
            expect(p.getBalance(16), null, "edge0 t=16");

            // Edge: expiration=0 single-instant window [10,10].
            p = new Part2();
            p.addCredit("p", 5, 10, 0);
            expect(p.getBalance(9), null, "edgeInst t=9");
            expect(p.getBalance(10), 5, "edgeInst t=10");
            expect(p.getBalance(11), null, "edgeInst t=11");

            // Edge: query before any event / only-future credit.
            p = new Part2();
            expect(p.getBalance(5), null, "edgeEmpty t=5");
            p = new Part2();
            p.addCredit("f", 5, 100, 10);
            expect(p.getBalance(50), null, "edgeFuture t=50");

            // Edge: exactly at end vs end+1, [10,15].
            p = new Part2();
            p.addCredit("q", 7, 10, 5);
            expect(p.getBalance(15), 7, "edgeEnd t=15");
            expect(p.getBalance(16), null, "edgeEnd t=16");

            // Edge: pure deficit, no credits.
            p = new Part2();
            p.subtract(5, 10);
            expect(p.getBalance(10), null, "edgeDeficit t=10");
            expect(p.getBalance(0), null, "edgeDeficit t=0");

            // Edge: out-of-order deficit pay-down by a later-arriving credit.
            p = new Part2();
            p.subtract(8, 20);
            p.addCredit("g1", 3, 10, 100);
            p.addCredit("g2", 10, 30, 100);
            expect(p.getBalance(20), null, "edgePaydown t=20");
            expect(p.getBalance(30), 5, "edgePaydown t=30");

            // Reported interviewer test: ONE subtract draining across THREE grants
            // with a partial take on the last, grants ADDED OUT OF EXPIRY ORDER.
            // Same expectations as Part1 — proves the heap ordering agrees with the
            // replay+sort ordering, which is the whole point of keeping both parts.
            p = new Part2();
            p.addCredit("c1", 20, 10, 30);   // through 40 (dies first)
            p.addCredit("c2", 20, 40, 30);   // through 70
            p.addCredit("c3", 20, 20, 30);   // through 50
            p.addCredit("c4", 20, 30, 30);   // through 60
            p.subtract(45, 30);              // 20 from c1, 20 from c3, 5 from c4
            expect(p.getBalance(30), 15, "spanThreeGrants t=30");
            expect(p.getBalance(55), 35, "spanThreeGrants t=55");

            System.out.println("Part2: all tests passed");
        }
    }
}
