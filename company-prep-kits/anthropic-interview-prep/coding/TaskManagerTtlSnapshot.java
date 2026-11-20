/*
 * Anthropic - Staff SWE OA practice
 * Problem: "Task Manager" - SECONDARY variant (TTL auto-expiry + time-travel snapshot).
 *
 * This is the other 4-level shape that rotates under the same OA name (Prompt 2).
 * It is the in-memory-DB "TTL (L3) + look-back (L4)" combination dressed as tasks.
 * Read the harness signatures in the first 60s and pick THIS file vs
 * TaskManagementSystem.java (priority/quota) accordingly.
 *
 *   L1-2 : addTask / updateTask / completeTask / getTaskList  (per-user CRUD)
 *   L3   : optional TTL -> a task created at t with ttl behaves as deleted once
 *          t + ttl <= now (read-time filter, no active reaper)
 *   L4   : getTaskListAt(user, atTimestamp) -> the list AS OF any past moment
 *
 * KEY DESIGN = event sourcing (the representation that makes L3 and L4 one fold,
 * and the thing the prompt explicitly says to "pick up front"):
 *   per (user, taskId) keep an append-only list of events ADD/UPDATE/COMPLETE.
 *   stateAt(T) folds events with t <= T -> handles TTL and look-back uniformly,
 *   and naturally supports re-adding a task after it expired/completed (a later
 *   ADD starts a fresh generation; the fold resets on it).
 *
 * DOCUMENTED ASSUMPTIONS (Prompt 2 underspecifies these - confirm with harness):
 *   - ttl <= 0 means "no expiry"; TTL is measured from creation and is NOT reset
 *     by updateTask.
 *   - addTask returns false if a still-live task with that id exists for the user;
 *     after expiry/completion the id may be re-added.
 *   - update/complete only succeed while the task is live at their timestamp.
 *   - getTaskList sorts by creation time ascending.
 *
 * Compile & run:  javac TaskManagerTtlSnapshot.java && java TaskManagerTtlSnapshot
 */

import java.util.*;

public class TaskManagerTtlSnapshot {

    private enum Kind { ADD, UPDATE, COMPLETE }

    private static final class Event {
        final Kind kind;
        final long t;
        final long ttl;       // ADD only; <=0 means no expiry
        final int priority;   // ADD/UPDATE
        Event(Kind kind, long t, long ttl, int priority) { this.kind = kind; this.t = t; this.ttl = ttl; this.priority = priority; }
    }

    // user -> taskId -> chronological event log
    private final Map<String, Map<String, List<Event>>> store = new HashMap<>();

    private List<Event> log(String user, String taskId, boolean create) {
        Map<String, List<Event>> byTask = create
                ? store.computeIfAbsent(user, k -> new HashMap<>())
                : store.getOrDefault(user, Collections.emptyMap());
        if (!create) return byTask.get(taskId);
        return byTask.computeIfAbsent(taskId, k -> new ArrayList<>());
    }

    /** Folded state of one task at time T, or null if it isn't present then. */
    private static final class State { long createdAt; int priority; long expiry; }
    private static State stateAt(List<Event> events, long T) {
        if (events == null) return null;
        State st = null;                      // null => currently absent (never added / expired / completed)
        for (Event e : events) {
            if (e.t > T) break;               // events are chronological; ignore the future
            switch (e.kind) {
                case ADD:                     // a new generation begins (resets any prior state)
                    st = new State();
                    st.createdAt = e.t;
                    st.priority = e.priority;
                    // remember ttl by folding expiry lazily below via a sentinel:
                    st.expiry = (e.ttl <= 0) ? Long.MAX_VALUE : e.t + e.ttl;
                    break;
                case UPDATE:
                    if (st != null) st.priority = e.priority;
                    break;
                case COMPLETE:
                    st = null;                // completed -> drops out of the list
                    break;
            }
        }
        if (st == null) return null;
        if (st.expiry <= T) return null;      // TTL expired by time T
        return st;
    }

    // ============================ L1-2 (+ L3 ttl) ============================
    public boolean addTask(long timestamp, String userId, String taskId, long ttl, int priority) {
        if (stateAt(log(userId, taskId, false), timestamp) != null) return false;  // already live
        log(userId, taskId, true).add(new Event(Kind.ADD, timestamp, ttl, priority));
        return true;
    }

    public boolean updateTask(long timestamp, String userId, String taskId, int priority) {
        List<Event> ev = log(userId, taskId, false);
        if (stateAt(ev, timestamp) == null) return false;                          // missing/expired/done
        ev.add(new Event(Kind.UPDATE, timestamp, 0, priority));
        return true;
    }

    public boolean completeTask(long timestamp, String userId, String taskId) {
        List<Event> ev = log(userId, taskId, false);
        if (stateAt(ev, timestamp) == null) return false;
        ev.add(new Event(Kind.COMPLETE, timestamp, 0, 0));
        return true;
    }

    public List<String> getTaskList(long timestamp, String userId) {
        return snapshot(userId, timestamp);                  // "now" is just at = timestamp
    }

    // ============================ L4: time travel ============================
    public List<String> getTaskListAt(long timestamp, String userId, long atTimestamp) {
        return snapshot(userId, atTimestamp);                // reconstruct historical state on demand
    }

    private List<String> snapshot(String userId, long at) {
        Map<String, List<Event>> byTask = store.get(userId);
        if (byTask == null) return new ArrayList<>();
        List<String[]> live = new ArrayList<>();             // {taskId, createdAt}
        for (Map.Entry<String, List<Event>> e : byTask.entrySet()) {
            State st = stateAt(e.getValue(), at);
            if (st != null) live.add(new String[]{e.getKey(), Long.toString(st.createdAt)});
        }
        live.sort(Comparator.comparingLong(a -> Long.parseLong(a[1])));   // creation time asc
        List<String> res = new ArrayList<>();
        for (String[] a : live) res.add(a[0]);
        return res;
    }

    // ============================ self-test ============================
    private static int passed = 0, failed = 0;
    private static void check(String n, Object got, Object want) {
        if (Objects.equals(got, want)) passed++;
        else { failed++; System.out.println("FAIL " + n + ": got " + got + " want " + want); }
    }

    public static void main(String[] args) {
        TaskManagerTtlSnapshot s = new TaskManagerTtlSnapshot();
        String u = "user1";
        check("add t1 ttl10", s.addTask(1, u, "t1", 10, 5), true);     // live [1,11)
        check("add t2 no-ttl", s.addTask(2, u, "t2", 0, 7), true);     // live forever
        check("add t1 dup-live", s.addTask(3, u, "t1", 5, 1), false);  // already live
        check("list@4", s.getTaskList(4, u), List.of("t1", "t2"));
        check("update t1@5 live", s.updateTask(5, u, "t1", 9), true);
        check("complete t2@6", s.completeTask(6, u, "t2"), true);
        check("list@7 (t2 done)", s.getTaskList(7, u), List.of("t1"));
        check("list@12 (t1 expired)", s.getTaskList(12, u), List.of());
        check("update t1@13 expired", s.updateTask(13, u, "t1", 3), false);
        check("re-add t1@14", s.addTask(14, u, "t1", 0, 2), true);     // new generation
        check("list@15", s.getTaskList(15, u), List.of("t1"));

        // ---- L4 time-travel snapshots (querying the PAST from now) ----
        check("snap@4", s.getTaskListAt(16, u, 4), List.of("t1", "t2"));
        check("snap@7", s.getTaskListAt(17, u, 7), List.of("t1"));     // t2 completed at 6
        check("snap@12", s.getTaskListAt(18, u, 12), List.of());       // t1 expired, t2 done
        check("snap@1 (only t1)", s.getTaskListAt(19, u, 1), List.of("t1"));
        check("snap@0 (empty)", s.getTaskListAt(20, u, 0), List.of());
        check("missing user", s.getTaskList(21, "ghost"), List.of());

        System.out.println("\nPassed " + passed + ", Failed " + failed);
        if (failed == 0) System.out.println("ALL TTL/SNAPSHOT TESTS PASSED");
    }
}
