/*
 * Anthropic - Staff SWE OA practice
 * Problem: "Worker / Employee Grant Management" (CodeSignal 4-level, interval / payroll).
 *
 * Track employees clocking in/out, apply promotions that take effect on the NEXT
 * clock_in, and aggregate pay across sessions with mergeable double-pay windows.
 *
 * Levels (search "LEVEL n"; later levels only ADD - earlier code is untouched
 * except where a comment explicitly flags a CHANGE):
 *   L1 addEmployee / clockIn / clockOut                  (build (in,out) sessions)
 *   L2 getTotalWorkTime / topKWorkers                    (leaderboard)
 *   L3 setPromotion (deferred to next clock_in) / calculatePay   (per-session rate)
 *   L4 setDoublePay (merge overlaps) + calculatePay extended     (2x bonus segments)
 *
 * THE KEY INSIGHT (why this stays simple):
 *   A promotion only takes effect AT a clock_in, and a session spans exactly
 *   [clock_in, clock_out). Therefore the pay rate is CONSTANT for the whole life
 *   of a session - there is never an intra-session promotion split. So we freeze
 *   the rate ONTO the session at clock_in time. The only intra-session split that
 *   can happen in L4 is the double-pay overlap, which is pure interval arithmetic.
 *
 * Design notes (why this is optimal for the OA shape):
 *   - Each Employee owns its own event log (sessions + a single pending-promotion
 *     slot + a running work-time total). No shared global timeline.
 *   - Sessions for one employee are disjoint and appended in clock_in order, so
 *     they are sorted by time -> calculatePay binary-searches to the first relevant
 *     session and stops early.  getTotalWorkTime is O(1) (maintained on clock_out).
 *   - topKWorkers uses a size-k min-heap: O(n log k), not a full O(n log n) sort.
 *   - Double-pay windows are kept as a TreeMap of MERGED disjoint intervals
 *     (start -> end). Insertion merges in O(log n + merged); overlap-with-a-range
 *     queries are O(log n + hit) via floor/higher navigation. Merging once up front
 *     is what prevents the classic "4x from two overlapping bonuses" bug.
 *
 * ASSUMPTION on add_employee: the transcribed signature is add_employee(name,
 * position, hourly_salary) yet every other method keys off `employee_id`, and the
 * L2 tiebreaker distinguishes "employee_id or name". The only self-consistent
 * reading is that an employee has BOTH an id and a name, so the method is modeled
 * as addEmployee(employeeId, name, position, hourlySalary). If the grader truly
 * passes only 3 args, treat `name` as the id (drop the separate id field) - no
 * other logic changes.
 *
 * Pay arithmetic follows the worked example literally: pay = ticks * rate
 * (no division by 3600); "hourly_salary" is simply the per-tick rate here.
 * Intervals are half-open [a, b) so a duration is exactly (b - a).
 *
 * Compile & run:  javac WorkerGrantManagement.java && java WorkerGrantManagement
 */

import java.util.*;

public class WorkerGrantManagement {

    // ----- L1: one completed office session, with its rate frozen at clock_in -----
    private static final class Session {
        final long in;       // clock_in timestamp  (inclusive)
        final long out;      // clock_out timestamp (exclusive) -> duration = out - in
        final long rate;     // pay rate in effect for the ENTIRE session (L3 freezes it here)
        Session(long in, long out, long rate) {
            this.in = in; this.out = out; this.rate = rate;
        }
    }

    // ----- L1: an employee owns its sessions; L3 adds a pending-promotion slot -----
    private static final class Employee {
        final String id;
        final String name;
        String position;
        long rate;                 // CURRENT rate; mutated only when a promotion activates
        boolean clockedIn = false; // is there an open clock_in awaiting clock_out?
        long openIn = -1;          // timestamp of the open clock_in (valid iff clockedIn)
        long totalWorkTime = 0;    // L2: running sum of completed session durations (O(1) query)
        final List<Session> sessions = new ArrayList<>(); // disjoint, sorted by time

        // L3: single pending-promotion slot, consumed on the next clock_in
        boolean hasPending = false;
        String pendingPosition;
        long pendingRate;

        Employee(String id, String name, String position, long rate) {
            this.id = id; this.name = name; this.position = position; this.rate = rate;
        }
    }

    private final Map<String, Employee> employees = new HashMap<>();

    // L4: MERGED, disjoint double-pay windows, keyed start -> end (half-open).
    private final TreeMap<Long, Long> doublePay = new TreeMap<>();

    // =====================================================================
    // LEVEL 1 - office sessions
    // =====================================================================

    /** Register an employee with a base per-tick rate. Duplicate ids are ignored. */
    public void addEmployee(String employeeId, String name, String position, long hourlySalary) {
        employees.putIfAbsent(employeeId, new Employee(employeeId, name, position, hourlySalary));
    }

    /**
     * Start a session. Repeated clock_in while already clocked in is a NO-OP
     * (we keep the first open clock_in and, importantly, do NOT consume a pending
     * promotion - a promotion is only consumed when a real new session begins).
     */
    public void clockIn(String employeeId, long timestamp) {
        Employee e = employees.get(employeeId);
        if (e == null || e.clockedIn) return;     // unknown employee, or double clock_in -> no-op
        // (L3) consume the pending promotion HERE, before this session's rate is frozen,
        // so the new session (and everything after) bills at the promoted rate.
        if (e.hasPending) {
            e.position = e.pendingPosition;
            e.rate = e.pendingRate;
            e.hasPending = false;
        }
        e.clockedIn = true;
        e.openIn = timestamp;
    }

    /**
     * End the open session, recording (openIn, timestamp) at the current rate.
     * clock_out without a matching clock_in is a NO-OP. A non-positive-length
     * session (timestamp <= openIn) records no time.
     */
    public void clockOut(String employeeId, long timestamp) {
        Employee e = employees.get(employeeId);
        if (e == null || !e.clockedIn) return;    // clock_out without clock_in -> no-op
        if (timestamp > e.openIn) {
            e.sessions.add(new Session(e.openIn, timestamp, e.rate)); // rate frozen at clock_in
            e.totalWorkTime += timestamp - e.openIn;                  // (L2) maintain total
        }
        e.clockedIn = false;
        e.openIn = -1;
    }

    // =====================================================================
    // LEVEL 2 - time tracking + leaderboard            (only ADDS to L1)
    // =====================================================================

    /** Sum of completed session durations. O(1) - maintained on each clock_out. */
    public long getTotalWorkTime(String employeeId) {
        Employee e = employees.get(employeeId);
        return e == null ? 0 : e.totalWorkTime;
    }

    /**
     * Top k employees by total office time, descending; ties broken by employee id
     * ascending. O(n log k) via a size-k min-heap that evicts the current "worst".
     */
    public List<String> topKWorkers(int k) {
        if (k <= 0) return new ArrayList<>();
        // Min-heap whose TOP is the worst kept candidate (smallest time; on a tie,
        // the largest id, because a smaller id is "better" and must survive).
        PriorityQueue<Employee> heap = new PriorityQueue<>((a, b) -> {
            if (a.totalWorkTime != b.totalWorkTime)
                return Long.compare(a.totalWorkTime, b.totalWorkTime); // smaller time = worse = top
            return b.id.compareTo(a.id);                                // tie: larger id = worse = top
        });
        for (Employee e : employees.values()) {
            heap.offer(e);
            if (heap.size() > k) heap.poll();      // drop the worst, keep best k
        }
        List<Employee> top = new ArrayList<>(heap);
        // Emit best-first: time descending, then id ascending.
        top.sort((a, b) -> {
            if (a.totalWorkTime != b.totalWorkTime)
                return Long.compare(b.totalWorkTime, a.totalWorkTime); // time desc
            return a.id.compareTo(b.id);                                // id asc
        });
        List<String> res = new ArrayList<>(top.size());
        for (Employee e : top) res.add(e.id);
        return res;
    }

    // =====================================================================
    // LEVEL 3 - promotions (deferred to next clock_in) + pay   (ADDS + 1 CHANGE)
    // =====================================================================
    //
    // CHANGE vs L1: clockIn() now consumes a pending promotion BEFORE freezing the
    // session rate. We add that single block inside clockIn via this helper so the
    // L1 method body above stays readable; functionally the consume happens at the
    // marked spot. (See clockIn comment - the actual consume is inlined below.)

    /**
     * Queue a promotion. It does NOT apply now; it applies at the employee's NEXT
     * clock_in. Only one slot is kept - a newer set_promotion overwrites an
     * unconsumed older one (latest pending wins).
     */
    public void setPromotion(String employeeId, String newPosition, long newHourlySalary) {
        Employee e = employees.get(employeeId);
        if (e == null) return;
        e.hasPending = true;
        e.pendingPosition = newPosition;
        e.pendingRate = newHourlySalary;
    }

    /**
     * Total earnings for the employee within [start, end), counting only time spent
     * clocked in (completed sessions). Each session is paid at its frozen rate; the
     * portion overlapping a double-pay window is paid at 2x (see L4).
     */
    public long calculatePay(String employeeId, long start, long end) {
        Employee e = employees.get(employeeId);
        if (e == null || end <= start) return 0;
        List<Session> ss = e.sessions;
        long total = 0;
        // Sessions are sorted by time; jump to the first one that can overlap [start,end).
        for (int i = firstSessionEndingAfter(ss, start); i < ss.size(); i++) {
            Session s = ss.get(i);
            if (s.in >= end) break;                       // no later session can overlap
            long lo = Math.max(s.in, start);
            long hi = Math.min(s.out, end);
            if (hi <= lo) continue;                       // no overlap with the query window
            long bonus = bonusOverlap(lo, hi);            // (L4) ticks inside a double-pay window
            // Whole overlap at 1x, plus an EXTRA 1x on the bonus ticks => bonus ticks at 2x.
            total += (hi - lo) * s.rate + bonus * s.rate;
        }
        return total;
    }

    /** First index whose session.out > start. Sessions are disjoint & sorted, so
     *  `out` is ascending -> plain lower-bound binary search. */
    private static int firstSessionEndingAfter(List<Session> ss, long start) {
        int lo = 0, hi = ss.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (ss.get(mid).out > start) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    // =====================================================================
    // LEVEL 4 - double-pay windows                     (ADDS + extends calculatePay)
    // =====================================================================

    /**
     * Mark [start, end) as a 2x window for EVERY employee. Overlapping/adjacent
     * windows are merged into the disjoint set immediately, so a region covered by
     * two windows is still only 2x (never 4x).
     */
    public void setDoublePay(long start, long end) {
        if (end <= start) return;                                   // empty window
        // Absorb a left neighbor that reaches into `start` (overlap or touch).
        Map.Entry<Long, Long> left = doublePay.floorEntry(start);
        if (left != null && left.getValue() >= start) {
            start = left.getKey();
            end = Math.max(end, left.getValue());
            doublePay.remove(left.getKey());
        }
        // Absorb every window that starts within the (growing) merged range.
        Map.Entry<Long, Long> nxt;
        while ((nxt = doublePay.ceilingEntry(start)) != null && nxt.getKey() <= end) {
            end = Math.max(end, nxt.getValue());
            doublePay.remove(nxt.getKey());
        }
        doublePay.put(start, end);
    }

    /** Total length of the intersection of [lo, hi) with the merged double-pay set. */
    private long bonusOverlap(long lo, long hi) {
        long sum = 0;
        Long k = doublePay.floorKey(lo);          // a window may start at/just below lo
        if (k == null) k = doublePay.ceilingKey(lo);
        while (k != null && k < hi) {             // windows are sorted by start
            long s = k, en = doublePay.get(k);
            long ov = Math.min(en, hi) - Math.max(s, lo);
            if (ov > 0) sum += ov;
            k = doublePay.higherKey(k);
        }
        return sum;
    }

    // =====================================================================
    // Verification harness (worked example + edge cases)
    // =====================================================================
    private static int passed = 0, failed = 0;

    private static void check(String label, Object actual, Object expected) {
        boolean ok = Objects.equals(actual, expected);
        if (ok) passed++; else failed++;
        System.out.printf("%-42s %s  (got=%s, want=%s)%n",
                label, ok ? "PASS" : "FAIL", actual, expected);
    }

    public static void main(String[] args) {
        // ---------- The exact worked example from the prompt ----------
        WorkerGrantManagement m = new WorkerGrantManagement();
        m.addEmployee("e1", "Erin", "Worker", 10);
        m.clockIn("e1", 100);
        m.clockOut("e1", 200);                 // session1 [100,200) @ 10
        m.setPromotion("e1", "Senior", 20);    // pending - does NOT apply yet
        m.clockIn("e1", 300);                  // consumes pending -> rate now 20
        m.clockOut("e1", 400);                 // session2 [300,400) @ 20
        m.setDoublePay(350, 450);
        // s1: 100*10=1000 ; s2: [300,350)50*20=1000 + [350,400)50*40=2000 => 3000 ; total 4000
        check("L4 worked example calculatePay", m.calculatePay("e1", 0, 500), 4000L);
        check("L1/L2 total work time e1", m.getTotalWorkTime("e1"), 200L);

        // ---------- L1 edge cases ----------
        WorkerGrantManagement m1 = new WorkerGrantManagement();
        m1.addEmployee("a", "A", "W", 5);
        m1.clockIn("a", 10);
        m1.clockIn("a", 20);                   // double clock_in -> no-op (first one stands)
        m1.clockOut("a", 30);                  // session [10,30), NOT [20,30)
        check("L1 double clock_in is no-op", m1.getTotalWorkTime("a"), 20L);
        m1.clockOut("a", 40);                  // clock_out without open session -> no-op
        check("L1 stray clock_out is no-op", m1.getTotalWorkTime("a"), 20L);
        m1.clockIn("a", 50);
        m1.clockOut("a", 50);                  // zero-length session -> records nothing
        check("L1 zero-length session ignored", m1.getTotalWorkTime("a"), 20L);

        // ---------- L2 leaderboard + tiebreak ----------
        WorkerGrantManagement m2 = new WorkerGrantManagement();
        for (String id : new String[]{"e1", "e2", "e3"}) m2.addEmployee(id, id, "W", 1);
        // e1: 100, e2: 100, e3: 50
        m2.clockIn("e1", 0); m2.clockOut("e1", 100);
        m2.clockIn("e2", 0); m2.clockOut("e2", 100);
        m2.clockIn("e3", 0); m2.clockOut("e3", 50);
        check("L2 topK desc + id tiebreak", m2.topKWorkers(2), List.of("e1", "e2"));
        check("L2 topK k>=n returns all",   m2.topKWorkers(9), List.of("e1", "e2", "e3"));
        check("L2 topK k<=0 empty",         m2.topKWorkers(0), List.of());

        // ---------- L3 promotion deferral ----------
        WorkerGrantManagement m3 = new WorkerGrantManagement();
        m3.addEmployee("p", "P", "W", 10);
        m3.clockIn("p", 0); m3.clockOut("p", 100);     // @10
        m3.setPromotion("p", "Senior", 30);            // pending, not applied
        check("L3 pay before next clock_in @ old rate", m3.calculatePay("p", 0, 100), 1000L);
        m3.clockIn("p", 200); m3.clockOut("p", 300);   // promotion consumed -> @30
        check("L3 pay after promotion (1000+3000)", m3.calculatePay("p", 0, 300), 4000L);
        // newer pending overwrites an unconsumed one (latest wins)
        m3.setPromotion("p", "X", 40);
        m3.setPromotion("p", "Y", 50);
        m3.clockIn("p", 400); m3.clockOut("p", 500);   // @50
        check("L3 latest pending wins",      m3.calculatePay("p", 400, 500), 5000L);

        // ---------- L3 window straddling a session boundary ----------
        WorkerGrantManagement m4 = new WorkerGrantManagement();
        m4.addEmployee("s", "S", "W", 10);
        m4.clockIn("s", 50); m4.clockOut("s", 150);
        check("L3 partial-window overlap", m4.calculatePay("s", 100, 200), 500L); // [100,150)

        // ---------- L4 overlapping bonuses must NOT 4x ----------
        WorkerGrantManagement m5 = new WorkerGrantManagement();
        m5.addEmployee("b", "B", "W", 10);
        m5.clockIn("b", 0); m5.clockOut("b", 100);     // [0,100) @10
        m5.setDoublePay(20, 60);
        m5.setDoublePay(40, 80);                       // merges -> [20,80), the [40,60) overlap stays 2x
        // base 100*10=1000 + extra 1x over 60 bonus ticks = 600 => 1600 (NOT 1800)
        check("L4 overlapping bonuses merge (no 4x)", m5.calculatePay("b", 0, 100), 1600L);

        // ---------- L4 bonus window strictly inside a session ----------
        WorkerGrantManagement m6 = new WorkerGrantManagement();
        m6.addEmployee("c", "C", "W", 10);
        m6.clockIn("c", 0); m6.clockOut("c", 100);
        m6.setDoublePay(40, 60);                       // 20 bonus ticks inside
        check("L4 bonus inside session", m6.calculatePay("c", 0, 100), 1200L); // 1000 + 20*10

        // ---------- L4 bonus clipped by the query window ----------
        check("L4 query window clips bonus", m6.calculatePay("c", 0, 50), 600L); // [0,50): 500 + [40,50)10*10=100

        System.out.println("\nPassed " + passed + ", Failed " + failed);
        if (failed == 0) System.out.println("ALL WORKER-GRANT-MANAGEMENT TESTS PASSED");
    }
}
