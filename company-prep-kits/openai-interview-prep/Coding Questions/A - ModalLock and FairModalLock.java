// =============================================================================
//  ModalLock and FairModalLock  (Java 21)
// =============================================================================
//
//  This file translates a Python concurrency problem into Java. The Python
//  reference is the source of truth for the algorithm and the exact expected
//  timelines; this file reproduces both behaviors and PROVES them with
//  deterministic, bounded, self-checking harnesses.
//
//  A "ModalLock" is a lock with MODES. It is shared-within-a-mode and
//  exclusive-across-modes:
//    - When idle, any mode may acquire it.
//    - While held in mode M, other acquirers of mode M proceed immediately
//      (they share), but acquirers of a different mode block.
//    - When the last holder releases, the lock becomes idle and all waiters
//      are woken to re-contend.
//  (Think of a body of terrain that can be "water" or "land": many swimmers
//  can share the water at once, but a hiker must wait for the water to drain
//  before the terrain can become land.)
//
//  -------------------------------------------------------------------------
//  PART 1 -- ModalLock (Level 1): the unfair primitive.
//  -------------------------------------------------------------------------
//  Approach: a ReentrantLock + a single Condition guard the state
//  { currentMode : String|null, holders : int }. acquire() spins on a while
//  loop (`while currentMode != null && currentMode != mode`) awaiting the
//  Condition; release() decrements holders and, when it hits zero, clears the
//  mode and signalAll()s every waiter. There is NO fairness: when the lock
//  goes idle, whichever scheduler-chosen waiter wins sets the new mode, and a
//  same-mode latecomer can "barge" ahead of an earlier different-mode waiter.
//  signalAll + while-recheck are mandatory (broadcast wakeups; losers re-block;
//  spurious wakeups tolerated). Illegal release throws.
//
//  -------------------------------------------------------------------------
//  PART 2 -- FairModalLock (Levels 2-5): adds cross-mode request-order
//  fairness, while still letting a "herd" of same-mode waiters at the FRONT of
//  the queue all enter together.
//  -------------------------------------------------------------------------
//  Approach: same lock/condition/state, PLUS a FIFO ArrayDeque<Group> of
//  *waiting* batches, where a Group is { mode, waiting, granted }. A new
//  acquirer joins the current mode only if no earlier different-mode batch is
//  queued ahead of it (canJoinCurrentMode); otherwise it appends to (or
//  coalesces with) a tail batch and parks until its batch is at the FRONT and
//  the lock is grantable in that mode. release() pops the front batch once it
//  is fully drained, and resets the mode + signalAll at holders==0. The net
//  invariant is: no mode bypasses an earlier different-mode waiter, but a run
//  of same-mode requests at the front forms one batch that enters as a herd.
//  Part 2 verifies FOUR scenarios (example, staggered, herd, trimodal), each
//  with a FRESH lock and its own oracle assertions.
//
//  -------------------------------------------------------------------------
//  HOW THE TESTS PROVE CORRECTNESS (same design, duplicated, in both parts):
//  -------------------------------------------------------------------------
//  A monotonically increasing logical `clock` is stepped by the main thread.
//  Each logical thread runs a SCRIPT of (op, mode, submitClock) steps. A worker
//  busy-waits until clock >= submitClock, then for an acquire it records
//  ACQUIRING, calls lock.acquire(mode) (which may BLOCK while the clock keeps
//  advancing), then records HOLDING with the clock value observed AFTER the
//  call returns. Because a blocked acquire only returns later, it naturally
//  stamps a LATER clock -- that is precisely how "this thread was blocked until
//  clock C" is encoded and verified. The ACQUIRING submit clocks and the
//  release clocks are scripted; the HOLDING clocks are NOT -- they are produced
//  by the lock and checked against the expected timeline. A wrong lock yields a
//  wrong timeline (FAIL); the correct lock reproduces it exactly. A watchdog
//  guarantees the program always terminates: any deadlock becomes a FAILURE
//  rather than an infinite hang. We do NOT rely on `-ea`; every check throws or
//  System.exit(1) explicitly so `java` (without -ea) still validates.
//
//  RUN:
//    javac "A - ModalLock and FairModalLock.java"
//    java ModalLockAndFairModalLock          # runs all parts
//    java ModalLockAndFairModalLock part1    # runs Part 1 only
//    java ModalLockAndFairModalLock part2    # runs Part 2 only
//  Exit code is non-zero if any part fails.
//
//  NOTE on the file name vs. class name: the file is named "A - ModalLock and
//  FairModalLock.java" (with spaces and an "A - " prefix) while the top-level
//  class is `ModalLockAndFairModalLock`. javac permits this ONLY because the
//  top-level class is package-private (not public). That is intentional and
//  required here.
//
//  Each PartN below is fully SELF-CONTAINED: its own lock implementation, its
//  own harness, recorder, and watchdog, and its own public static void main.
//  Copying any single PartN class into its own file (promoted to a top-level
//  public class) compiles and runs unchanged. The duplication between Part1 and
//  Part2 is therefore intentional, not a smell.
// =============================================================================

class ModalLockAndFairModalLock {

    /** Dispatch: no arg => run all parts; "part1"/"part2" => that part only. */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            Part1.main(new String[0]);
            Part2.main(new String[0]);
            System.out.println("ALL PARTS PASS");
            return;
        }
        switch (args[0]) {
            case "part1" -> Part1.main(new String[0]);
            case "part2" -> Part2.main(new String[0]);
            default -> {
                System.err.println("Unknown part: " + args[0] + " (expected part1|part2)");
                System.exit(2);
            }
        }
    }

    // =========================================================================
    //  PART 1 -- ModalLock (the unfair primitive).
    //  SELF-CONTAINED: lock + harness + recorder + watchdog + main, all nested
    //  here with zero references outside Part1.
    // =========================================================================
    static final class Part1 {

        // ----- The lock under test -------------------------------------------
        // shared-within-mode, exclusive-across-modes; NO fairness.
        static final class ModalLock {
            private final java.util.concurrent.locks.ReentrantLock lock =
                    new java.util.concurrent.locks.ReentrantLock();
            private final java.util.concurrent.locks.Condition cond = lock.newCondition();
            private String currentMode = null; // null == idle
            private int holders = 0;

            void acquire(String mode) throws InterruptedException {
                lock.lock();
                try {
                    // Block while held in a DIFFERENT mode. Same mode shares.
                    while (currentMode != null && !currentMode.equals(mode)) {
                        cond.await();
                    }
                    currentMode = mode;
                    holders++;
                } finally {
                    lock.unlock();
                }
            }

            void release(String mode) {
                lock.lock();
                try {
                    if (!mode.equals(currentMode) || holders == 0) {
                        throw new IllegalMonitorStateException("release without matching acquire");
                    }
                    holders--;
                    if (holders == 0) {
                        currentMode = null;
                        cond.signalAll(); // broadcast: wake all to re-contend
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        // ----- Timeline model -------------------------------------------------
        enum State { ACQUIRING, HOLDING, RELEASING, IDLE }

        record Event(int tid, int clock, State state, String mode) {}

        /** A scripted step: op in {"acquire","release"}, mode, submit clock. */
        record Step(String op, String mode, int submitClock) {}

        /** Thread-safe recorder of (tid, clockAtEvent, state[, mode]). */
        static final class Recorder {
            private final java.util.List<Event> events =
                    java.util.Collections.synchronizedList(new java.util.ArrayList<>());
            void record(int tid, int clock, State state, String mode) {
                events.add(new Event(tid, clock, state, mode));
            }
            java.util.List<Event> snapshot() {
                synchronized (events) { return new java.util.ArrayList<>(events); }
            }
        }

        // ----- Tunables -------------------------------------------------------
        static final long STEP_MS = 40;       // logical-tick duration (CI-generous)
        static final int  CLOCK_CAP = 60;     // safety cap on the logical clock
        static final long WATCHDOG_MS = 10_000;

        // Shared logical clock, stepped by the driver (main) thread.
        static volatile int clock = 0;

        public static void main(String[] args) throws Exception {
            // Arm a watchdog so a deadlock becomes a FAILURE, never a hang.
            Thread watchdog = new Thread(() -> {
                try { Thread.sleep(WATCHDOG_MS); } catch (InterruptedException e) { return; }
                System.err.println("part1 FAIL: watchdog timeout (possible deadlock)");
                System.exit(1);
            });
            watchdog.setDaemon(true);
            watchdog.start();

            negativeCheck(); // illegal release must throw
            testModalLockExample();

            watchdog.interrupt();
            System.out.println("part1 PASS");
        }

        /** Releasing an unheld mode on a fresh lock must throw. */
        static void negativeCheck() {
            ModalLock m = new ModalLock();
            boolean threw = false;
            try {
                m.release("water");
            } catch (IllegalMonitorStateException expected) {
                threw = true;
            }
            if (!threw) {
                System.err.println("part1 FAIL: release without acquire did not throw");
                System.exit(1);
            }
        }

        // ----- The Part-1 oracle test ----------------------------------------
        static void testModalLockExample() throws Exception {
            // Scripts (submit clocks). HOLDING clocks are produced by the lock.
            java.util.Map<Integer, java.util.List<Step>> scripts = new java.util.LinkedHashMap<>();
            scripts.put(0, java.util.List.of(
                    new Step("acquire", "water", 0), new Step("release", "water", 4),
                    new Step("acquire", "water", 10), new Step("release", "water", 10)));
            scripts.put(1, java.util.List.of(
                    new Step("acquire", "land", 1), new Step("release", "land", 7)));
            scripts.put(2, java.util.List.of(
                    new Step("acquire", "water", 2), new Step("release", "water", 7)));
            scripts.put(3, java.util.List.of(
                    new Step("acquire", "land", 6), new Step("release", "land", 9)));
            scripts.put(4, java.util.List.of(
                    new Step("acquire", "land", 6), new Step("release", "land", 9)));

            // Expected per-thread timelines (the correctness oracle).
            java.util.Map<Integer, java.util.List<Event>> expected = new java.util.LinkedHashMap<>();
            expected.put(0, java.util.List.of(
                    ev(0, 0, State.ACQUIRING), ev(0, 0, State.HOLDING, "water"),
                    ev(0, 4, State.RELEASING), ev(0, 4, State.IDLE),
                    ev(0, 10, State.ACQUIRING), ev(0, 10, State.HOLDING, "water"),
                    ev(0, 10, State.RELEASING), ev(0, 10, State.IDLE)));
            expected.put(1, java.util.List.of(
                    ev(1, 1, State.ACQUIRING), ev(1, 7, State.HOLDING, "land"),
                    ev(1, 7, State.RELEASING), ev(1, 7, State.IDLE)));
            expected.put(2, java.util.List.of(
                    ev(2, 2, State.ACQUIRING), ev(2, 2, State.HOLDING, "water"),
                    ev(2, 7, State.RELEASING), ev(2, 7, State.IDLE)));
            expected.put(3, java.util.List.of(
                    ev(3, 6, State.ACQUIRING), ev(3, 7, State.HOLDING, "land"),
                    ev(3, 9, State.RELEASING), ev(3, 9, State.IDLE)));
            expected.put(4, java.util.List.of(
                    ev(4, 6, State.ACQUIRING), ev(4, 7, State.HOLDING, "land"),
                    ev(4, 9, State.RELEASING), ev(4, 9, State.IDLE)));

            runScenario("part1", scripts, expected);
        }

        static Event ev(int tid, int clock, State s) { return new Event(tid, clock, s, null); }
        static Event ev(int tid, int clock, State s, String mode) { return new Event(tid, clock, s, mode); }

        // ----- Generic driver: runs scripts against a fresh lock, compares ----
        static void runScenario(String name,
                                java.util.Map<Integer, java.util.List<Step>> scripts,
                                java.util.Map<Integer, java.util.List<Event>> expected) throws Exception {
            clock = 0;
            ModalLock lockUnderTest = new ModalLock();
            Recorder rec = new Recorder();
            java.util.concurrent.CountDownLatch done =
                    new java.util.concurrent.CountDownLatch(scripts.size());

            java.util.List<Thread> workers = new java.util.ArrayList<>();
            for (var entry : scripts.entrySet()) {
                final int tid = entry.getKey();
                final java.util.List<Step> script = entry.getValue();
                Thread t = new Thread(() -> {
                    try {
                        for (Step st : script) {
                            busyWaitUntil(st.submitClock());
                            if (st.op().equals("acquire")) {
                                rec.record(tid, clock, State.ACQUIRING, null);
                                lockUnderTest.acquire(st.mode());
                                rec.record(tid, clock, State.HOLDING, st.mode());
                            } else { // release
                                rec.record(tid, clock, State.RELEASING, null);
                                lockUnderTest.release(st.mode());
                                rec.record(tid, clock, State.IDLE, null);
                            }
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }
                }, name + "-t" + tid);
                t.setDaemon(true);
                workers.add(t);
            }

            for (Thread t : workers) t.start();

            // Driver: step the clock until all workers finish or we hit the cap.
            while (done.getCount() > 0) {
                try { Thread.sleep(STEP_MS / 2); } catch (InterruptedException ignored) {}
                clock++;
                try { Thread.sleep(STEP_MS / 2); } catch (InterruptedException ignored) {}
                if (clock > CLOCK_CAP) {
                    System.err.println(name + " FAIL: clock exceeded cap " + CLOCK_CAP
                            + " (workers stuck => lock bug)");
                    System.exit(1);
                }
            }

            // Join with a bounded timeout; a live thread after join == stuck.
            for (Thread t : workers) {
                t.join(2000);
                if (t.isAlive()) {
                    System.err.println(name + " FAIL: worker " + t.getName() + " still alive after join");
                    System.exit(1);
                }
            }

            compareTimelines(name, rec, expected);
        }

        /** Busy-wait (cheap sleep loop) until the logical clock reaches target. */
        static void busyWaitUntil(int target) throws InterruptedException {
            while (clock < target) Thread.sleep(1);
        }

        /** Group events by tid (in observed order) and compare EXACTLY. */
        static void compareTimelines(String name, Recorder rec,
                                     java.util.Map<Integer, java.util.List<Event>> expected) {
            java.util.Map<Integer, java.util.List<Event>> actual = new java.util.LinkedHashMap<>();
            for (Event e : rec.snapshot()) {
                actual.computeIfAbsent(e.tid(), k -> new java.util.ArrayList<>()).add(e);
            }
            boolean ok = true;
            for (var entry : expected.entrySet()) {
                int tid = entry.getKey();
                java.util.List<Event> exp = entry.getValue();
                java.util.List<Event> act = actual.getOrDefault(tid, java.util.List.of());
                if (!sameTimeline(exp, act)) {
                    ok = false;
                    System.err.println(name + " FAIL: thread " + tid + " timeline mismatch");
                    System.err.println("  expected: " + fmt(exp));
                    System.err.println("  actual:   " + fmt(act));
                }
            }
            if (!ok) System.exit(1);
        }

        /** Compare two timelines on (clock, state, mode), ignoring tid noise. */
        static boolean sameTimeline(java.util.List<Event> a, java.util.List<Event> b) {
            if (a.size() != b.size()) return false;
            for (int i = 0; i < a.size(); i++) {
                Event x = a.get(i), y = b.get(i);
                if (x.clock() != y.clock() || x.state() != y.state()
                        || !java.util.Objects.equals(x.mode(), y.mode())) {
                    return false;
                }
            }
            return true;
        }

        static String fmt(java.util.List<Event> evs) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < evs.size(); i++) {
                Event e = evs.get(i);
                sb.append("(").append(e.clock()).append(",").append(e.state());
                if (e.mode() != null) sb.append(",").append(e.mode());
                sb.append(")");
                if (i < evs.size() - 1) sb.append(", ");
            }
            return sb.append("]").toString();
        }
    }

    // =========================================================================
    //  PART 2 -- FairModalLock (cross-mode request-order fairness + herd entry).
    //  SELF-CONTAINED: lock + harness + recorder + watchdog + main, all nested
    //  here with zero references outside Part2.
    // =========================================================================
    static final class Part2 {

        // ---------------------------------------------------------------------
        //  CHANGES FROM PART 1:
        //    * Added a FIFO ArrayDeque<Group> `queue` of WAITING batches, and a
        //      nested Group { mode, waiting, granted }. (Part 1 had no queue and
        //      no fairness at all.)
        //    * acquire() now respects request order across modes:
        //        - True-idle fast path fires ONLY when currentMode==null AND the
        //          queue is empty (Part 1 only checked currentMode==null).
        //        - Same-mode fast-join is now GATED by canJoinCurrentMode: you
        //          may join the running mode only if no earlier different-mode
        //          batch sits ahead of you in the queue (the lone front batch
        //          must be your mode AND already granted).
        //        - Otherwise you append to a trailing not-yet-started same-mode
        //          batch (coalescing the herd) or create a new batch, then PARK
        //          until your batch is at the FRONT and the lock is grantable.
        //    * release() now pops the front batch when it is fully drained
        //      (waiting==0 && granted==0), but still only resets currentMode and
        //      signalAll at holders==0 (same broadcast-at-idle rule as Part 1).
        //    * New invariant: request-order fairness across modes, with a herd
        //      of same-mode requests at the front entering together. Part 1 had
        //      NO fairness (same-mode latecomers could barge).
        //    * Same primitives as Part 1: ReentrantLock + a single Condition,
        //      signalAll + while-recheck, IllegalMonitorStateException on
        //      illegal release. Group front membership uses IDENTITY (==), never
        //      .equals; modes are compared with .equals, never ==.
        // ---------------------------------------------------------------------
        static final class FairModalLock {
            // A batch of same-mode requests queued together.
            static final class Group {
                final String mode;
                int waiting; // members still parked, not yet granted
                int granted; // members of this batch currently holding
                Group(String mode) { this.mode = mode; }
            }

            private final java.util.concurrent.locks.ReentrantLock lock =
                    new java.util.concurrent.locks.ReentrantLock();
            private final java.util.concurrent.locks.Condition cond = lock.newCondition();
            private String currentMode = null; // null == idle
            private int holders = 0;
            private final java.util.ArrayDeque<Group> queue = new java.util.ArrayDeque<>();

            /** May a new `mode` acquirer join the running mode right now? */
            private boolean canJoinCurrentMode(String mode) {
                if (!mode.equals(currentMode)) return false;
                if (queue.isEmpty()) return true;
                // Only if the single queued batch IS our mode and already started.
                return queue.size() == 1
                        && queue.peekFirst().mode.equals(mode)
                        && queue.peekFirst().granted > 0;
            }

            void acquire(String mode) throws InterruptedException {
                lock.lock();
                try {
                    // Fast path 1: truly idle AND nobody queued ahead of us.
                    if (currentMode == null && queue.isEmpty()) {
                        currentMode = mode;
                        holders = 1;
                        return;
                    }
                    // Fast path 2: join the running mode (fairness permitting).
                    if (canJoinCurrentMode(mode)) {
                        holders++;
                        if (!queue.isEmpty()) queue.peekFirst().granted++;
                        return;
                    }
                    // Slow path: join a trailing not-yet-started same-mode batch
                    // (herd coalescing), else start a new batch at the tail.
                    Group group;
                    if (!queue.isEmpty()
                            && queue.peekLast().mode.equals(mode)
                            && queue.peekLast().granted == 0) {
                        group = queue.peekLast();
                    } else {
                        group = new Group(mode);
                        queue.addLast(group);
                    }
                    group.waiting++;
                    // Park until our batch is at the front AND the lock is
                    // grantable in our mode.
                    while (true) {
                        boolean isFront = !queue.isEmpty() && queue.peekFirst() == group; // IDENTITY
                        if (isFront && currentMode == null) {
                            currentMode = mode;
                            holders++;
                            group.waiting--;
                            group.granted++;
                            return;
                        }
                        if (isFront && mode.equals(currentMode) && group.granted > 0) {
                            holders++;
                            group.waiting--;
                            group.granted++;
                            return;
                        }
                        cond.await();
                    }
                } finally {
                    lock.unlock();
                }
            }

            void release(String mode) {
                lock.lock();
                try {
                    if (!mode.equals(currentMode) || holders == 0) {
                        throw new IllegalMonitorStateException("release without matching acquire");
                    }
                    holders--;
                    // Account this release against the front batch if applicable.
                    if (!queue.isEmpty()
                            && queue.peekFirst().mode.equals(mode)
                            && queue.peekFirst().granted > 0) {
                        Group front = queue.peekFirst();
                        front.granted--;
                        if (front.waiting == 0 && front.granted == 0) {
                            queue.pollFirst(); // fully drained => remove
                        }
                    }
                    if (holders == 0) {
                        currentMode = null;
                        cond.signalAll(); // broadcast: herd + cross-mode wakeups
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        // ----- Timeline model (duplicated; identical shape to Part 1) ---------
        enum State { ACQUIRING, HOLDING, RELEASING, IDLE }

        record Event(int tid, int clock, State state, String mode) {}

        record Step(String op, String mode, int submitClock) {}

        static final class Recorder {
            private final java.util.List<Event> events =
                    java.util.Collections.synchronizedList(new java.util.ArrayList<>());
            void record(int tid, int clock, State state, String mode) {
                events.add(new Event(tid, clock, state, mode));
            }
            java.util.List<Event> snapshot() {
                synchronized (events) { return new java.util.ArrayList<>(events); }
            }
        }

        // ----- Tunables -------------------------------------------------------
        static final long STEP_MS = 40;
        static final int  CLOCK_CAP = 60;
        static final long WATCHDOG_MS = 10_000;

        static volatile int clock = 0;

        public static void main(String[] args) throws Exception {
            Thread watchdog = new Thread(() -> {
                try { Thread.sleep(WATCHDOG_MS); } catch (InterruptedException e) { return; }
                System.err.println("part2 FAIL: watchdog timeout (possible deadlock)");
                System.exit(1);
            });
            watchdog.setDaemon(true);
            watchdog.start();

            negativeCheck();        // illegal release must throw
            testExample();          // Level 2: same scripts as Part 1, fair result
            testStaggered();        // Level 3: staggered cross-mode arrivals
            testHerd();             // Level 4: a herd of same-mode waiters
            testTrimodal();         // Level 5: three modes interleaved

            watchdog.interrupt();
            System.out.println("part2 PASS");
        }

        static void negativeCheck() {
            FairModalLock m = new FairModalLock();
            boolean threw = false;
            try {
                m.release("water");
            } catch (IllegalMonitorStateException expected) {
                threw = true;
            }
            if (!threw) {
                System.err.println("part2 FAIL: release without acquire did not throw");
                System.exit(1);
            }
        }

        // ----- Level 2: example (fair result differs from Part 1) -------------
        static void testExample() throws Exception {
            java.util.Map<Integer, java.util.List<Step>> scripts = new java.util.LinkedHashMap<>();
            scripts.put(0, java.util.List.of(
                    new Step("acquire", "water", 0), new Step("release", "water", 4),
                    new Step("acquire", "water", 10), new Step("release", "water", 10)));
            scripts.put(1, java.util.List.of(
                    new Step("acquire", "land", 1), new Step("release", "land", 5)));
            scripts.put(2, java.util.List.of(
                    new Step("acquire", "water", 2), new Step("release", "water", 7)));
            scripts.put(3, java.util.List.of(
                    new Step("acquire", "land", 6), new Step("release", "land", 9)));
            scripts.put(4, java.util.List.of(
                    new Step("acquire", "land", 6), new Step("release", "land", 9)));

            java.util.Map<Integer, java.util.List<Event>> expected = new java.util.LinkedHashMap<>();
            expected.put(0, java.util.List.of(
                    ev(0, 0, State.ACQUIRING), ev(0, 0, State.HOLDING, "water"),
                    ev(0, 4, State.RELEASING), ev(0, 4, State.IDLE),
                    ev(0, 10, State.ACQUIRING), ev(0, 10, State.HOLDING, "water"),
                    ev(0, 10, State.RELEASING), ev(0, 10, State.IDLE)));
            expected.put(1, java.util.List.of(
                    ev(1, 1, State.ACQUIRING), ev(1, 4, State.HOLDING, "land"),
                    ev(1, 5, State.RELEASING), ev(1, 5, State.IDLE)));
            expected.put(2, java.util.List.of(
                    ev(2, 2, State.ACQUIRING), ev(2, 5, State.HOLDING, "water"),
                    ev(2, 7, State.RELEASING), ev(2, 7, State.IDLE)));
            expected.put(3, java.util.List.of(
                    ev(3, 6, State.ACQUIRING), ev(3, 7, State.HOLDING, "land"),
                    ev(3, 9, State.RELEASING), ev(3, 9, State.IDLE)));
            expected.put(4, java.util.List.of(
                    ev(4, 6, State.ACQUIRING), ev(4, 7, State.HOLDING, "land"),
                    ev(4, 9, State.RELEASING), ev(4, 9, State.IDLE)));

            runScenario("part2.example", scripts, expected);
        }

        // ----- Level 3: staggered cross-mode arrivals -------------------------
        static void testStaggered() throws Exception {
            java.util.Map<Integer, java.util.List<Step>> scripts = new java.util.LinkedHashMap<>();
            scripts.put(0, java.util.List.of(new Step("acquire", "water", 0), new Step("release", "water", 9)));
            scripts.put(1, java.util.List.of(new Step("acquire", "land", 1), new Step("release", "land", 10)));
            scripts.put(2, java.util.List.of(new Step("acquire", "water", 2), new Step("release", "water", 11)));
            scripts.put(3, java.util.List.of(new Step("acquire", "water", 3), new Step("release", "water", 12)));
            scripts.put(4, java.util.List.of(new Step("acquire", "land", 4), new Step("release", "land", 13)));
            scripts.put(5, java.util.List.of(new Step("acquire", "land", 5), new Step("release", "land", 14)));
            scripts.put(6, java.util.List.of(new Step("acquire", "water", 6), new Step("release", "water", 15)));
            scripts.put(7, java.util.List.of(new Step("acquire", "land", 7), new Step("release", "land", 15)));

            java.util.Map<Integer, java.util.List<Event>> expected = new java.util.LinkedHashMap<>();
            expected.put(0, java.util.List.of(
                    ev(0, 0, State.ACQUIRING), ev(0, 0, State.HOLDING, "water"),
                    ev(0, 9, State.RELEASING), ev(0, 9, State.IDLE)));
            expected.put(1, java.util.List.of(
                    ev(1, 1, State.ACQUIRING), ev(1, 9, State.HOLDING, "land"),
                    ev(1, 10, State.RELEASING), ev(1, 10, State.IDLE)));
            expected.put(2, java.util.List.of(
                    ev(2, 2, State.ACQUIRING), ev(2, 10, State.HOLDING, "water"),
                    ev(2, 11, State.RELEASING), ev(2, 11, State.IDLE)));
            expected.put(3, java.util.List.of(
                    ev(3, 3, State.ACQUIRING), ev(3, 10, State.HOLDING, "water"),
                    ev(3, 12, State.RELEASING), ev(3, 12, State.IDLE)));
            expected.put(4, java.util.List.of(
                    ev(4, 4, State.ACQUIRING), ev(4, 12, State.HOLDING, "land"),
                    ev(4, 13, State.RELEASING), ev(4, 13, State.IDLE)));
            expected.put(5, java.util.List.of(
                    ev(5, 5, State.ACQUIRING), ev(5, 12, State.HOLDING, "land"),
                    ev(5, 14, State.RELEASING), ev(5, 14, State.IDLE)));
            expected.put(6, java.util.List.of(
                    ev(6, 6, State.ACQUIRING), ev(6, 14, State.HOLDING, "water"),
                    ev(6, 15, State.RELEASING), ev(6, 15, State.IDLE)));
            expected.put(7, java.util.List.of(
                    ev(7, 7, State.ACQUIRING), ev(7, 15, State.HOLDING, "land"),
                    ev(7, 15, State.RELEASING), ev(7, 15, State.IDLE)));

            runScenario("part2.staggered", scripts, expected);
        }

        // ----- Level 4: herd of same-mode waiters enters together -------------
        static void testHerd() throws Exception {
            java.util.Map<Integer, java.util.List<Step>> scripts = new java.util.LinkedHashMap<>();
            scripts.put(0, java.util.List.of(
                    new Step("acquire", "water", 0), new Step("release", "water", 2),
                    new Step("acquire", "water", 3), new Step("release", "water", 5)));
            for (int tid = 1; tid <= 4; tid++) {
                scripts.put(tid, java.util.List.of(
                        new Step("acquire", "land", 1), new Step("release", "land", 2),
                        new Step("acquire", "land", 4), new Step("release", "land", 5)));
            }

            java.util.Map<Integer, java.util.List<Event>> expected = new java.util.LinkedHashMap<>();
            expected.put(0, java.util.List.of(
                    ev(0, 0, State.ACQUIRING), ev(0, 0, State.HOLDING, "water"),
                    ev(0, 2, State.RELEASING), ev(0, 2, State.IDLE),
                    ev(0, 3, State.ACQUIRING), ev(0, 3, State.HOLDING, "water"),
                    ev(0, 5, State.RELEASING), ev(0, 5, State.IDLE)));
            // t1..t4 are all identical: they herd in at clock 2, then again at 5.
            java.util.List<Event> herdMember = java.util.List.of(
                    ev(0, 1, State.ACQUIRING), ev(0, 2, State.HOLDING, "land"),
                    ev(0, 2, State.RELEASING), ev(0, 2, State.IDLE),
                    ev(0, 4, State.ACQUIRING), ev(0, 5, State.HOLDING, "land"),
                    ev(0, 5, State.RELEASING), ev(0, 5, State.IDLE));
            for (int tid = 1; tid <= 4; tid++) expected.put(tid, herdMember);

            runScenario("part2.herd", scripts, expected);
        }

        // ----- Level 5: trimodal (water / land / lava) interleaving -----------
        static void testTrimodal() throws Exception {
            java.util.Map<Integer, java.util.List<Step>> scripts = new java.util.LinkedHashMap<>();
            scripts.put(0, java.util.List.of(
                    new Step("acquire", "water", 0), new Step("release", "water", 8),
                    new Step("acquire", "water", 11), new Step("release", "water", 20)));
            scripts.put(1, java.util.List.of(
                    new Step("acquire", "land", 1), new Step("release", "land", 9),
                    new Step("acquire", "water", 12), new Step("release", "water", 21)));
            scripts.put(2, java.util.List.of(
                    new Step("acquire", "lava", 2), new Step("release", "lava", 10),
                    new Step("acquire", "land", 13), new Step("release", "land", 22)));
            scripts.put(3, java.util.List.of(new Step("acquire", "water", 3), new Step("release", "water", 15)));
            scripts.put(4, java.util.List.of(new Step("acquire", "land", 4), new Step("release", "land", 16)));
            scripts.put(5, java.util.List.of(new Step("acquire", "lava", 5), new Step("release", "lava", 17)));
            scripts.put(6, java.util.List.of(new Step("acquire", "land", 6), new Step("release", "land", 18)));
            scripts.put(7, java.util.List.of(new Step("acquire", "water", 7), new Step("release", "water", 19)));
            scripts.put(8, java.util.List.of(new Step("acquire", "lava", 14), new Step("release", "lava", 23)));

            java.util.Map<Integer, java.util.List<Event>> expected = new java.util.LinkedHashMap<>();
            expected.put(0, java.util.List.of(
                    ev(0, 0, State.ACQUIRING), ev(0, 0, State.HOLDING, "water"),
                    ev(0, 8, State.RELEASING), ev(0, 8, State.IDLE),
                    ev(0, 11, State.ACQUIRING), ev(0, 18, State.HOLDING, "water"),
                    ev(0, 20, State.RELEASING), ev(0, 20, State.IDLE)));
            expected.put(1, java.util.List.of(
                    ev(1, 1, State.ACQUIRING), ev(1, 8, State.HOLDING, "land"),
                    ev(1, 9, State.RELEASING), ev(1, 9, State.IDLE),
                    ev(1, 12, State.ACQUIRING), ev(1, 18, State.HOLDING, "water"),
                    ev(1, 21, State.RELEASING), ev(1, 21, State.IDLE)));
            expected.put(2, java.util.List.of(
                    ev(2, 2, State.ACQUIRING), ev(2, 9, State.HOLDING, "lava"),
                    ev(2, 10, State.RELEASING), ev(2, 10, State.IDLE),
                    ev(2, 13, State.ACQUIRING), ev(2, 21, State.HOLDING, "land"),
                    ev(2, 22, State.RELEASING), ev(2, 22, State.IDLE)));
            expected.put(3, java.util.List.of(
                    ev(3, 3, State.ACQUIRING), ev(3, 10, State.HOLDING, "water"),
                    ev(3, 15, State.RELEASING), ev(3, 15, State.IDLE)));
            expected.put(4, java.util.List.of(
                    ev(4, 4, State.ACQUIRING), ev(4, 15, State.HOLDING, "land"),
                    ev(4, 16, State.RELEASING), ev(4, 16, State.IDLE)));
            expected.put(5, java.util.List.of(
                    ev(5, 5, State.ACQUIRING), ev(5, 16, State.HOLDING, "lava"),
                    ev(5, 17, State.RELEASING), ev(5, 17, State.IDLE)));
            expected.put(6, java.util.List.of(
                    ev(6, 6, State.ACQUIRING), ev(6, 17, State.HOLDING, "land"),
                    ev(6, 18, State.RELEASING), ev(6, 18, State.IDLE)));
            expected.put(7, java.util.List.of(
                    ev(7, 7, State.ACQUIRING), ev(7, 18, State.HOLDING, "water"),
                    ev(7, 19, State.RELEASING), ev(7, 19, State.IDLE)));
            expected.put(8, java.util.List.of(
                    ev(8, 14, State.ACQUIRING), ev(8, 22, State.HOLDING, "lava"),
                    ev(8, 23, State.RELEASING), ev(8, 23, State.IDLE)));

            runScenario("part2.trimodal", scripts, expected);
        }

        static Event ev(int tid, int clock, State s) { return new Event(tid, clock, s, null); }
        static Event ev(int tid, int clock, State s, String mode) { return new Event(tid, clock, s, mode); }

        // ----- Generic driver (duplicated; identical to Part 1's) -------------
        static void runScenario(String name,
                                java.util.Map<Integer, java.util.List<Step>> scripts,
                                java.util.Map<Integer, java.util.List<Event>> expected) throws Exception {
            clock = 0;
            FairModalLock lockUnderTest = new FairModalLock();
            Recorder rec = new Recorder();
            java.util.concurrent.CountDownLatch done =
                    new java.util.concurrent.CountDownLatch(scripts.size());

            java.util.List<Thread> workers = new java.util.ArrayList<>();
            for (var entry : scripts.entrySet()) {
                final int tid = entry.getKey();
                final java.util.List<Step> script = entry.getValue();
                Thread t = new Thread(() -> {
                    try {
                        for (Step st : script) {
                            busyWaitUntil(st.submitClock());
                            if (st.op().equals("acquire")) {
                                rec.record(tid, clock, State.ACQUIRING, null);
                                lockUnderTest.acquire(st.mode());
                                rec.record(tid, clock, State.HOLDING, st.mode());
                            } else { // release
                                rec.record(tid, clock, State.RELEASING, null);
                                lockUnderTest.release(st.mode());
                                rec.record(tid, clock, State.IDLE, null);
                            }
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }
                }, name + "-t" + tid);
                t.setDaemon(true);
                workers.add(t);
            }

            for (Thread t : workers) t.start();

            while (done.getCount() > 0) {
                try { Thread.sleep(STEP_MS / 2); } catch (InterruptedException ignored) {}
                clock++;
                try { Thread.sleep(STEP_MS / 2); } catch (InterruptedException ignored) {}
                if (clock > CLOCK_CAP) {
                    System.err.println(name + " FAIL: clock exceeded cap " + CLOCK_CAP
                            + " (workers stuck => lock bug)");
                    System.exit(1);
                }
            }

            for (Thread t : workers) {
                t.join(2000);
                if (t.isAlive()) {
                    System.err.println(name + " FAIL: worker " + t.getName() + " still alive after join");
                    System.exit(1);
                }
            }

            compareTimelines(name, rec, expected);
            System.out.println(name + " PASS");
        }

        static void busyWaitUntil(int target) throws InterruptedException {
            while (clock < target) Thread.sleep(1);
        }

        static void compareTimelines(String name, Recorder rec,
                                     java.util.Map<Integer, java.util.List<Event>> expected) {
            java.util.Map<Integer, java.util.List<Event>> actual = new java.util.LinkedHashMap<>();
            for (Event e : rec.snapshot()) {
                actual.computeIfAbsent(e.tid(), k -> new java.util.ArrayList<>()).add(e);
            }
            boolean ok = true;
            for (var entry : expected.entrySet()) {
                int tid = entry.getKey();
                java.util.List<Event> exp = entry.getValue();
                java.util.List<Event> act = actual.getOrDefault(tid, java.util.List.of());
                if (!sameTimeline(exp, act)) {
                    ok = false;
                    System.err.println(name + " FAIL: thread " + tid + " timeline mismatch");
                    System.err.println("  expected: " + fmt(exp));
                    System.err.println("  actual:   " + fmt(act));
                }
            }
            if (!ok) System.exit(1);
        }

        static boolean sameTimeline(java.util.List<Event> a, java.util.List<Event> b) {
            if (a.size() != b.size()) return false;
            for (int i = 0; i < a.size(); i++) {
                Event x = a.get(i), y = b.get(i);
                if (x.clock() != y.clock() || x.state() != y.state()
                        || !java.util.Objects.equals(x.mode(), y.mode())) {
                    return false;
                }
            }
            return true;
        }

        static String fmt(java.util.List<Event> evs) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < evs.size(); i++) {
                Event e = evs.get(i);
                sb.append("(").append(e.clock()).append(",").append(e.state());
                if (e.mode() != null) sb.append(",").append(e.mode());
                sb.append(")");
                if (i < evs.size() - 1) sb.append(", ");
            }
            return sb.append("]").toString();
        }
    }
}
