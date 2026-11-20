import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Robinhood — "Design a Calendar Booking System"
 * Source: 1point3acres f1096c0c-18ab-463d-a820-79fd45858a0a
 *
 * book(start, end) over HALF-OPEN intervals [start, end). Return false if it
 * overlaps an existing booking; otherwise store it and return true.
 * Constraints: up to 1e5 calls, 0 <= start < end <= 1e9.
 *
 * Given example: book(10,20) -> true
 *                book(15,25) -> false   (overlaps [10,20))
 *                book(20,30) -> true    (end is EXCLUSIVE, so no overlap)
 *
 * ---------------------------------------------------------------------------
 * ⭐ Framed as "design", graded as code. This is LeetCode 729.
 *
 * TWO THINGS DECIDE THE GRADE:
 * 1. HALF-OPEN ARITHMETIC. book(20,30) after book(10,20) must SUCCEED. Use
 *    strict > and <, never >= and <=. Getting this backwards fails the given
 *    example, and it is the single most common error.
 * 2. THE TWO-SIDED CHECK. floorKey alone is not enough — an interval that
 *    starts after s but before e is missed. You need floorKey AND ceilingKey.
 *
 * The 1e9 coordinate bound exists to kill any array/bucket/segment-array
 * approach and force a balanced tree. Say that out loud; it shows you read the
 * constraint rather than pattern-matched the problem.
 * ---------------------------------------------------------------------------
 */
public class CalendarBooking {

    /* ===================================================================
     * PART 1 — Linear scan. O(n) per book, O(n^2) overall.
     *
     * Correct, and the right thing to write first. At 1e5 calls this is 1e10
     * comparisons, so say the number and then improve it — do not pretend it is
     * fine, and do not skip it either.
     * =================================================================== */
    static class Naive {
        private final List<int[]> booked = new ArrayList<>();

        boolean book(int s, int e) {
            for (int[] b : booked)
                if (s < b[1] && b[0] < e) return false;   // ⭐ strict: half-open overlap test
            booked.add(new int[]{s, e});
            return true;
        }
    }

    /* ===================================================================
     * PART 2 — ⭐ TreeMap. O(log n) per book. THE ANSWER.
     *
     * CHANGES FROM PART 1:
     *   - List<int[]>  ->  TreeMap<Integer,Integer> keyed on start
     *   - the O(n) scan becomes two O(log n) neighbour lookups
     *
     * Why two lookups suffice: because the map holds only NON-overlapping
     * intervals (that is the invariant we maintain), a new [s,e) can only
     * conflict with its immediate predecessor or its immediate successor by
     * start. Nothing further away can reach it.
     * =================================================================== */
    static class Calendar1 {
        private final TreeMap<Integer, Integer> cal = new TreeMap<>();

        boolean book(int s, int e) {
            if (s >= e) return false;                       // degenerate/invalid

            Integer prev = cal.floorKey(s);                 // starts at or before s
            if (prev != null && cal.get(prev) > s) return false;   // strict >, not >=

            Integer next = cal.ceilingKey(s);               // starts at or after s
            if (next != null && next < e) return false;     // strict <, not <=

            cal.put(s, e);
            return true;
        }

        int size() { return cal.size(); }
    }

    /* ===================================================================
     * PART 3 — My Calendar II: allow a DOUBLE booking, reject a TRIPLE.
     *
     * CHANGES FROM PART 2:
     *   - two structures: `overlaps` holds regions already double-booked;
     *     `bookings` holds everything
     *   - a new interval is rejected if it touches any region in `overlaps`
     *   - otherwise, the intersection of the new interval with each existing
     *     booking is added to `overlaps`
     *
     * The insight: you cannot detect a triple by counting bookings. You detect
     * it by keeping the DOUBLE-booked regions explicitly and testing against
     * those. Trying to do it with one structure is where people get stuck.
     * =================================================================== */
    static class Calendar2 {
        private final List<int[]> bookings = new ArrayList<>();
        private final List<int[]> overlaps = new ArrayList<>();

        boolean book(int s, int e) {
            if (s >= e) return false;
            for (int[] o : overlaps)
                if (s < o[1] && o[0] < e) return false;     // would be a third booking
            for (int[] b : bookings) {
                if (s < b[1] && b[0] < e)
                    overlaps.add(new int[]{Math.max(s, b[0]), Math.min(e, b[1])});
            }
            bookings.add(new int[]{s, e});
            return true;
        }
    }

    /* ===================================================================
     * PART 4 — My Calendar III: max concurrent bookings. Sweep line.
     *
     * CHANGES FROM PART 3:
     *   - every book() always succeeds; the question becomes "how deep is the
     *     deepest overlap right now"
     *   - a TreeMap<point, delta> holds +1 at each start and -1 at each end;
     *     a running prefix sum over the sorted keys gives the maximum
     *
     * This generalises the whole family: Calendar I is "reject if max would
     * exceed 1", Calendar II is "reject if max would exceed 2", Calendar III is
     * "report the max". Saying that out loud is the strong move — it shows the
     * three problems are one problem.
     * =================================================================== */
    static class Calendar3 {
        private final TreeMap<Integer, Integer> delta = new TreeMap<>();

        int book(int s, int e) {
            delta.merge(s,  1, Integer::sum);
            delta.merge(e, -1, Integer::sum);
            int cur = 0, max = 0;
            for (int d : delta.values()) {                   // O(n) per call; see note
                cur += d;
                max = Math.max(max, cur);
            }
            return max;
        }
        // If they push on the O(n) recompute: keep a running max in a segment
        // tree / BIT over coordinate-compressed points, or maintain the max
        // incrementally. Only offer that when asked — it is a lot of code for a
        // constraint nobody stated.
    }

    /* ===================================================================
     * PART 4b — Concurrency. The follow-up the "design" framing invites.
     *
     * ⚠️ check-then-put is NOT atomic. Two threads can both pass the overlap
     * check and both insert, producing an overlap that the invariant forbids.
     * TreeMap is not thread-safe at all, and ConcurrentSkipListMap does NOT
     * help here — the composite (floorKey, ceilingKey, put) still needs to be
     * one atomic unit.
     *
     * Options, in the order worth stating:
     *   1. One lock around the composite operation (below). Simple, correct.
     *   2. Single-writer per calendar: route all bookings for a calendar id to
     *      one owner. Scales across calendars, no lock contention within one.
     *   3. Optimistic: version the calendar and retry on conflict.
     * At 1e5 total calls, option 1 is right and options 2-3 are over-building.
     * =================================================================== */
    static class ThreadSafeCalendar {
        private final TreeMap<Integer, Integer> cal = new TreeMap<>();
        private final ReentrantLock lock = new ReentrantLock();

        boolean book(int s, int e) {
            if (s >= e) return false;
            lock.lock();
            try {
                Integer prev = cal.floorKey(s);
                if (prev != null && cal.get(prev) > s) return false;
                Integer next = cal.ceilingKey(s);
                if (next != null && next < e) return false;
                cal.put(s, e);
                return true;
            } finally {
                lock.unlock();
            }
        }
        int size() { lock.lock(); try { return cal.size(); } finally { lock.unlock(); } }
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) throws Exception {
        System.out.println("=== part2: the stated example ===");
        Calendar1 c = new Calendar1();
        check("book(10,20)", c.book(10, 20), true);
        check("book(15,25)", c.book(15, 25), false);
        check("book(20,30)", c.book(20, 30), true);   // ⭐ half-open: must succeed

        System.out.println("=== half-open boundary cases ===");
        Calendar1 d = new Calendar1();
        check("book(10,20)",         d.book(10, 20), true);
        check("book(5,10)  touch L", d.book(5, 10),  true);    // ends exactly at 10
        check("book(20,25) touch R", d.book(20, 25), true);    // starts exactly at 20
        check("book(19,21) straddle",d.book(19, 21), false);
        check("book(12,15) inside",  d.book(12, 15), false);
        check("book(0,100) contains",d.book(0, 100), false);   // ⭐ needs the ceilingKey check
        check("book(10,20) exact dup",d.book(10, 20), false);

        System.out.println("=== the containment case in isolation ===");
        // Only floorKey would MISS this: nothing starts at or before 0, but
        // [10,20) starts after 0 and before 100.
        Calendar1 e2 = new Calendar1();
        e2.book(10, 20);
        check("book(0,100) over [10,20)", e2.book(0, 100), false);

        System.out.println("=== invalid input ===");
        check("book(5,5) empty",  new Calendar1().book(5, 5),  false);
        check("book(9,5) reversed", new Calendar1().book(9, 5), false);

        System.out.println("=== cross-check part1 vs part2 on random input ===");
        Random rnd = new Random(7);
        Naive naive = new Naive();
        Calendar1 tree = new Calendar1();
        boolean agree = true;
        for (int i = 0; i < 3000; i++) {
            int s = rnd.nextInt(10_000);
            int len = 1 + rnd.nextInt(50);
            boolean a = naive.book(s, s + len);
            boolean b = tree.book(s, s + len);
            if (a != b) { agree = false; System.out.println("  MISMATCH at " + s + "," + (s + len)); break; }
        }
        System.out.println("  3000 random bookings agree -> " + (agree ? "PASS" : "FAIL"));

        System.out.println("=== part3: Calendar II (double ok, triple rejected) ===");
        Calendar2 c2 = new Calendar2();
        check("book(10,20)", c2.book(10, 20), true);
        check("book(50,60)", c2.book(50, 60), true);
        check("book(10,40) 2nd", c2.book(10, 40), true);
        check("book(5,15)  3rd", c2.book(5, 15),  false);
        check("book(5,10)  ok",  c2.book(5, 10),  true);
        check("book(25,55) 2nd", c2.book(25, 55), true);

        System.out.println("=== part4: Calendar III (max concurrent) ===");
        Calendar3 c3 = new Calendar3();
        System.out.println("  book(10,20) -> " + c3.book(10, 20) + " (expect 1)");
        System.out.println("  book(50,60) -> " + c3.book(50, 60) + " (expect 1)");
        System.out.println("  book(10,40) -> " + c3.book(10, 40) + " (expect 2)");
        System.out.println("  book(5,15)  -> " + c3.book(5, 15)  + " (expect 3)");
        System.out.println("  book(5,10)  -> " + c3.book(5, 10)  + " (expect 3)");
        System.out.println("  book(25,55) -> " + c3.book(25, 55) + " (expect 3)");

        System.out.println("=== scale: 1e5 calls, 0..1e9 coordinates ===");
        Calendar1 big = new Calendar1();
        long t0 = System.currentTimeMillis();
        int accepted = 0;
        Random r2 = new Random(11);
        for (int i = 0; i < 100_000; i++) {
            int s = r2.nextInt(1_000_000_000 - 1000);
            if (big.book(s, s + 1 + r2.nextInt(1000))) accepted++;
        }
        System.out.println("  " + accepted + " accepted of 100000 in "
                + (System.currentTimeMillis() - t0) + " ms (TreeMap, O(log n) per call)");

        System.out.println("=== 4b: concurrency — 8 threads racing the SAME slot ===");
        ThreadSafeCalendar ts = new ThreadSafeCalendar();
        int threads = 8;
        java.util.concurrent.CountDownLatch go = new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.atomic.AtomicInteger wins = new java.util.concurrent.atomic.AtomicInteger();
        List<Thread> pool = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> {
                try { go.await(); } catch (InterruptedException ignored) {}
                if (ts.book(100, 200)) wins.incrementAndGet();
            });
            t.start();
            pool.add(t);
        }
        go.countDown();
        for (Thread t : pool) t.join();
        System.out.println("  exactly one winner? " + wins.get() + " -> "
                + (wins.get() == 1 && ts.size() == 1 ? "PASS" : "FAIL"));
    }

    private static void check(String label, boolean actual, boolean expected) {
        System.out.printf("  %-26s %-6s %s%n", label, actual, actual == expected ? "PASS" : "FAIL <<<");
    }
}
