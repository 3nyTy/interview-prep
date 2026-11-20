import java.util.*;

public class WorkerRegistry {

    /** Closed historical session. Position + compensation are snapshotted at session time. */
    private static final class Session {
        final long start;
        final long end;
        final String position;
        final long compensation;

        Session(long start, long end, String position, long compensation) {
            this.start = start;
            this.end = end;
            this.position = position;
            this.compensation = compensation;
        }
    }

    /** A queued promotion. Only the most recent one matters. */
    private static final class Promotion {
        final String position;
        final long compensation;
        final long timestamp;

        Promotion(String position, long compensation, long timestamp) {
            this.position = position;
            this.compensation = compensation;
            this.timestamp = timestamp;
        }
    }

    private static final class Worker {
        final String id;
        String position;
        long compensation;
        boolean inside;
        long entryTime;
        long totalTime;
        final List<Session> sessions = new ArrayList<>();
        Promotion pendingPromotion;

        Worker(String id, String position, long compensation) {
            this.id = id;
            this.position = position;
            this.compensation = compensation;
        }
    }

    private final Map<String, Worker> workers = new HashMap<>();

    // ---------------- Level 1 ----------------

    public void addWorker(String id, String position, long compensation) {
        workers.put(id, new Worker(id, position, compensation));
    }

    public void register(String id, long timestamp) {
        Worker w = workers.get(id);
        if (w == null) return;

        if (!w.inside) {
            // Entry: apply any pending promotion BEFORE the session opens.
            if (w.pendingPromotion != null) {
                w.position = w.pendingPromotion.position;
                w.compensation = w.pendingPromotion.compensation;
                w.pendingPromotion = null;
            }
            w.inside = true;
            w.entryTime = timestamp;
        } else {
            // Exit: close the session with the current (snapshot) position + compensation.
            long duration = timestamp - w.entryTime;
            w.totalTime += duration;
            w.sessions.add(new Session(w.entryTime, timestamp, w.position, w.compensation));
            w.inside = false;
        }
    }

    public long get(String id) {
        Worker w = workers.get(id);
        return w == null ? 0L : w.totalTime;
    }

    // ---------------- Level 2 ----------------

    public List<String> topN(int n) {
        return topN(n, null);
    }

    /**
     * Ranking is by completed totalTime, desc; ties broken by id ascending lex.
     * Position filter (when non-null) checks the worker's CURRENT position.
     * Output format is "id(totalTime)" — swap the .map(...) line if tests want raw ids.
     */
    public List<String> topN(int n, String position) {
        return workers.values().stream()
            .filter(w -> position == null || Objects.equals(w.position, position))
            .sorted((a, b) -> {
                int cmp = Long.compare(b.totalTime, a.totalTime);
                return cmp != 0 ? cmp : a.id.compareTo(b.id);
            })
            .limit(Math.max(0, n))
            .map(w -> w.id + "(" + w.totalTime + ")")
            .toList();
    }

    // ---------------- Level 3 ----------------

    /**
     * Schedule a promotion. Takes effect on the worker's NEXT entry (whether they
     * are currently in or out). If a previous promotion is pending, this overwrites it.
     */
    public void promote(String id, String newPosition, long newCompensation, long timestamp) {
        Worker w = workers.get(id);
        if (w == null) return;
        w.pendingPromotion = new Promotion(newPosition, newCompensation, timestamp);
    }

    /**
     * Pay over [start, end). Sums interval overlap with each completed session at
     * that session's snapshotted compensation. Open session, if any, is virtually
     * closed at `end` (so an in-progress shift contributes through the query end).
     */
    public long calcSalary(String id, long start, long end) {
        Worker w = workers.get(id);
        if (w == null || end <= start) return 0L;

        long total = 0L;
        for (Session s : w.sessions) {
            long overlap = Math.max(0L, Math.min(end, s.end) - Math.max(start, s.start));
            total += overlap * s.compensation;
        }
        if (w.inside) {
            long overlap = Math.max(0L, end - Math.max(start, w.entryTime));
            total += overlap * w.compensation;
        }
        return total;
    }

    // ---------------- Test harness ----------------

    public static void main(String[] args) {
        int passed = 0, failed = 0;

        // Level 1: state machine
        {
            WorkerRegistry r = new WorkerRegistry();
            r.addWorker("A", "engineer", 10);
            r.register("A", 0);   // in
            r.register("A", 10);  // out, +10
            r.register("A", 20);  // in
            r.register("A", 30);  // out, +10
            passed += check("L1 totalTime", 20L, r.get("A")); failed += 20L == r.get("A") ? 0 : 1;
        }

        // Level 1: open session does not count toward totalTime
        {
            WorkerRegistry r = new WorkerRegistry();
            r.addWorker("A", "engineer", 10);
            r.register("A", 0);
            r.register("A", 10);  // close
            r.register("A", 20);  // open again, no exit yet
            passed += check("L1 open session not in totalTime", 10L, r.get("A"));
        }

        // Level 2: ranking + position filter + lex tie-break
        {
            WorkerRegistry r = new WorkerRegistry();
            r.addWorker("A", "engineer", 10);
            r.addWorker("B", "engineer", 12);
            r.addWorker("C", "manager", 20);
            r.addWorker("D", "engineer", 9);
            r.register("A", 0); r.register("A", 100);
            r.register("B", 0); r.register("B", 50);
            r.register("C", 0); r.register("C", 80);
            r.register("D", 0); r.register("D", 100); // tie with A at 100
            passed += check("L2 topN(3)",
                List.of("A(100)", "D(100)", "C(80)"),
                r.topN(3));
            passed += check("L2 topN(3, engineer)",
                List.of("A(100)", "D(100)", "B(50)"),
                r.topN(3, "engineer"));
            passed += check("L2 topN(2, manager)",
                List.of("C(80)"),
                r.topN(2, "manager"));
        }

        // Level 3: promotion deferred until next entry
        {
            WorkerRegistry r = new WorkerRegistry();
            r.addWorker("A", "engineer", 10);
            r.register("A", 0);
            r.promote("A", "senior", 20, 5); // queued, worker is inside
            r.register("A", 10);             // exit — does NOT apply promotion
            // After exit, position should still be engineer (we'll verify via topN filter).
            passed += check("L3 still engineer after exit",
                List.of("A(10)"),
                r.topN(1, "engineer"));
            r.register("A", 20);             // entry — promotion applies here
            r.register("A", 30);             // exit at senior, comp=20
            passed += check("L3 promoted to senior",
                List.of("A(20)"),
                r.topN(1, "senior"));
            passed += check("L3 no longer ranked under engineer",
                List.<String>of(),
                r.topN(5, "engineer"));
            // Salary: session1 [0,10] @10 = 100, session2 [20,30] @20 = 200 -> 300 over [0,30]
            passed += check("L3 calcSalary full range", 300L, r.calcSalary("A", 0, 30));
            // Partial: [5,25] -> session1 overlap [5,10]=5*10=50; session2 overlap [20,25]=5*20=100 -> 150
            passed += check("L3 calcSalary partial", 150L, r.calcSalary("A", 5, 25));
            // Window before any session
            passed += check("L3 calcSalary empty range", 0L, r.calcSalary("A", -10, -1));
        }

        // Level 3: latest pending promotion wins
        {
            WorkerRegistry r = new WorkerRegistry();
            r.addWorker("B", "engineer", 10);
            r.register("B", 0);
            r.promote("B", "senior", 20, 5);
            r.promote("B", "manager", 30, 8); // overwrites
            r.register("B", 10);              // exit, still engineer
            r.register("B", 20);              // entry, applies manager@30
            r.register("B", 30);              // exit
            passed += check("L3 latest promotion wins", 100L + 300L, r.calcSalary("B", 0, 30));
            passed += check("L3 current position is manager",
                List.of("B(20)"), r.topN(1, "manager"));
        }

        // Level 3: open session contributes virtually
        {
            WorkerRegistry r = new WorkerRegistry();
            r.addWorker("C", "engineer", 5);
            r.register("C", 0);  // entry, no exit
            // Worker is still inside at query time. Salary over [0,10] should be 5*10=50.
            passed += check("L3 calcSalary includes open session", 50L, r.calcSalary("C", 0, 10));
            // Pending promotion does NOT take effect during current session.
            r.promote("C", "lead", 100, 2);
            passed += check("L3 pending promotion not applied to open session",
                50L, r.calcSalary("C", 0, 10));
        }

        // Level 3: promotion when worker is currently outside still defers to next entry
        {
            WorkerRegistry r = new WorkerRegistry();
            r.addWorker("D", "engineer", 10);
            r.promote("D", "senior", 20, 1);  // worker is outside; still queued
            r.register("D", 5);               // entry — applies senior@20
            r.register("D", 15);              // exit
            passed += check("L3 outside-then-promote applies on next entry",
                List.of("D(10)"), r.topN(1, "senior"));
            passed += check("L3 outside-then-promote salary", 200L, r.calcSalary("D", 5, 15));
        }

        System.out.printf("%n%d checks, %d failed%n", passed + failed, failed);
        if (failed > 0) System.exit(1);
    }

    private static <T> int check(String name, T expected, T actual) {
        boolean ok = Objects.equals(expected, actual);
        System.out.printf("[%s] %s%n  expected: %s%n  actual:   %s%n",
            ok ? "PASS" : "FAIL", name, expected, actual);
        return ok ? 1 : 0;
    }
}
