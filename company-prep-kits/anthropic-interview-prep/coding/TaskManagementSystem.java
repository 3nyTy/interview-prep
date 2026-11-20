/*
 * Anthropic - Staff SWE OA practice
 * Problem: "Task Management System" (CodeSignal 4-level) - PRIORITY / QUOTA variant.
 *
 * This is the fully-specified, in-rotation variant (Prompt 1 + the "alternate
 * canonical variant" in Prompt 2). The TTL + time-travel-snapshot variant lives
 * in TaskManagerTtlSnapshot.java.
 *
 * Levels (search "LEVEL n"; later levels only ADD - earlier code is untouched):
 *   L1 addTask / updateTask / getTask (compact JSON)
 *   L2 searchTasks / listTasksSorted (priority desc, creation-order asc)
 *   L3 addUser / assignTask (TTL window + quota) / getUserTasks
 *   L4 completeTask (frees quota now) / getOverdueAssignments (expired, never done)
 *
 * Design notes (why this is optimal for the OA shape):
 *   - Tasks live in a dict keyed by id; creation order == the autoincrement N in
 *     "task_id_N", stored so sort tiebreaks are O(1) to compare.
 *   - Assignments are a per-user append-only list of immutable intervals
 *     [start, finish) + a `completed` flag. Every time-based question (active /
 *     quota / overdue) is a single linear scan with the stated sort. Timestamps
 *     strictly increase, so start_time is unique -> "earliest assignment" and all
 *     tie-breaks are unambiguous and need no secondary key.
 *
 * Compile & run:  javac TaskManagementSystem.java && java TaskManagementSystem
 */

import java.util.*;

public class TaskManagementSystem {

    // ----- L1: a task -----
    private static final class Task {
        String name;
        int priority;
        final int creationOrder;          // == N in task_id_N (L2 sort tiebreak)
        Task(String name, int priority, int creationOrder) {
            this.name = name; this.priority = priority; this.creationOrder = creationOrder;
        }
    }

    // ----- L3: a single timed assignment of a task to a user -----
    private static final class Assignment {
        final String taskId;
        final long start;                 // assign timestamp (unique, strictly increasing)
        final long finish;                // active over [start, finish)
        boolean completed = false;        // L4: set by completeTask, frees quota immediately
        Assignment(String taskId, long start, long finish) {
            this.taskId = taskId; this.start = start; this.finish = finish;
        }
        boolean activeAt(long t) { return start <= t && t < finish && !completed; }
    }

    // ----- L3: a user with a quota and their assignment ledger -----
    private static final class User {
        final int quota;
        final List<Assignment> assignments = new ArrayList<>();
        User(int quota) { this.quota = quota; }
    }

    private final Map<String, Task> tasks = new HashMap<>();
    private int taskCounter = 0;
    private final Map<String, User> users = new HashMap<>();   // L3

    // =====================================================================
    // LEVEL 1 - basic task management
    // =====================================================================
    public String addTask(long timestamp, String name, int priority) {
        taskCounter++;
        String taskId = "task_id_" + taskCounter;
        tasks.put(taskId, new Task(name, priority, taskCounter));
        return taskId;
    }

    public boolean updateTask(long timestamp, String taskId, String name, int priority) {
        Task t = tasks.get(taskId);
        if (t == null) return false;          // unknown task
        t.name = name;
        t.priority = priority;
        return true;
    }

    public Optional<String> getTask(long timestamp, String taskId) {
        Task t = tasks.get(taskId);
        if (t == null) return Optional.empty();
        // compact JSON, fixed key order (name, then priority), no spaces.
        return Optional.of("{\"name\":\"" + t.name + "\",\"priority\":" + t.priority + "}");
    }

    // =====================================================================
    // LEVEL 2 - search & sorting  (NEW: searchTasks, listTasksSorted)
    // Sort order everywhere: priority DESC, then creation order ASC.
    // =====================================================================
    private static final Comparator<Task> BY_RANK = (a, b) ->
            a.priority != b.priority ? Integer.compare(b.priority, a.priority)   // priority desc
                                     : Integer.compare(a.creationOrder, b.creationOrder); // creation asc

    public List<String> searchTasks(long timestamp, String nameFilter, int maxResults) {
        if (maxResults <= 0) return new ArrayList<>();
        List<Map.Entry<String, Task>> matched = new ArrayList<>();
        for (Map.Entry<String, Task> e : tasks.entrySet())
            if (e.getValue().name.contains(nameFilter)) matched.add(e);          // case-sensitive substring
        matched.sort((x, y) -> BY_RANK.compare(x.getValue(), y.getValue()));
        List<String> res = new ArrayList<>();
        for (int i = 0; i < Math.min(maxResults, matched.size()); i++) res.add(matched.get(i).getKey());
        return res;
    }

    public List<String> listTasksSorted(long timestamp, int limit) {
        if (limit <= 0) return new ArrayList<>();
        List<Map.Entry<String, Task>> all = new ArrayList<>(tasks.entrySet());
        all.sort((x, y) -> BY_RANK.compare(x.getValue(), y.getValue()));
        List<String> res = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, all.size()); i++) res.add(all.get(i).getKey());
        return res;
    }

    // =====================================================================
    // LEVEL 3 - users, quotas, timed assignments  (NEW: addUser, assignTask, getUserTasks)
    // =====================================================================
    public boolean addUser(long timestamp, String userId, int quota) {
        if (users.containsKey(userId)) return false;     // already exists
        users.put(userId, new User(quota));
        return true;
    }

    public boolean assignTask(long timestamp, String taskId, String userId, long finishTime) {
        if (!tasks.containsKey(taskId)) return false;
        User u = users.get(userId);
        if (u == null) return false;
        int active = 0;                                  // quota = # active assignments NOW
        for (Assignment a : u.assignments) if (a.activeAt(timestamp)) active++;
        if (active >= u.quota) return false;             // expired/completed slots don't count
        u.assignments.add(new Assignment(taskId, timestamp, finishTime));
        return true;
    }

    public List<String> getUserTasks(long timestamp, String userId) {
        User u = users.get(userId);
        if (u == null) return new ArrayList<>();
        List<Assignment> active = new ArrayList<>();
        for (Assignment a : u.assignments) if (a.activeAt(timestamp)) active.add(a);
        active.sort(byFinishThenStart());                // finish asc, then start asc
        List<String> res = new ArrayList<>();
        for (Assignment a : active) res.add(a.taskId);
        return res;
    }

    // =====================================================================
    // LEVEL 4 - completion & overdue tracking  (NEW: completeTask, getOverdueAssignments)
    // =====================================================================
    public boolean completeTask(long timestamp, String taskId, String userId) {
        if (!tasks.containsKey(taskId)) return false;
        User u = users.get(userId);
        if (u == null) return false;
        // earliest active assignment of THIS task for THIS user (smallest start; start is unique)
        Assignment earliest = null;
        for (Assignment a : u.assignments)
            if (a.taskId.equals(taskId) && a.activeAt(timestamp))
                if (earliest == null || a.start < earliest.start) earliest = a;
        if (earliest == null) return false;              // not assigned-and-active for this user
        earliest.completed = true;                       // frees one quota slot immediately
        return true;
    }

    public List<String> getOverdueAssignments(long timestamp, String userId) {
        User u = users.get(userId);
        if (u == null) return new ArrayList<>();
        List<Assignment> overdue = new ArrayList<>();
        for (Assignment a : u.assignments)
            // expired (finish <= now) and never completed (completion only happens while active,
            // i.e. before finish, so !completed == "not completed before its own finish_time")
            if (a.finish <= timestamp && !a.completed) overdue.add(a);
        overdue.sort(byFinishThenStart());
        List<String> res = new ArrayList<>();
        for (Assignment a : overdue) res.add(a.taskId);  // a task id may repeat (multiple expiries)
        return res;
    }

    private static Comparator<Assignment> byFinishThenStart() {
        return (a, b) -> a.finish != b.finish ? Long.compare(a.finish, b.finish)
                                              : Long.compare(a.start, b.start);
    }

    // =====================================================================
    // Self-verifying tests - taken verbatim from the prompt's worked examples
    // =====================================================================
    private static int passed = 0, failed = 0;
    private static void check(String n, Object got, Object want) {
        if (Objects.equals(got, want)) passed++;
        else { failed++; System.out.println("FAIL " + n + ": got " + got + " want " + want); }
    }

    public static void main(String[] args) {
        // ---- Level 1 ----
        TaskManagementSystem s = new TaskManagementSystem();
        check("L1 add1", s.addTask(1, "Task 1", 5), "task_id_1");
        check("L1 add2 dup-name", s.addTask(2, "Task 1", 5), "task_id_2");
        check("L1 update", s.updateTask(3, "task_id_1", "Updated Task 1", 4), true);
        check("L1 get json", s.getTask(4, "task_id_1"),
                Optional.of("{\"name\":\"Updated Task 1\",\"priority\":4}"));
        check("L1 get missing", s.getTask(5, "task_id_3"), Optional.empty());
        check("L1 update missing", s.updateTask(6, "task_id_3", "x", 1), false);

        // ---- Level 2 ----
        TaskManagementSystem s2 = new TaskManagementSystem();
        s2.addTask(1, "Alpha", 10);
        s2.addTask(2, "Bravo", 15);
        s2.addTask(3, "Bravo Alpha", 5);
        check("L2 listSorted 2", s2.listTasksSorted(4, 2), List.of("task_id_2", "task_id_1"));
        check("L2 search Bra", s2.searchTasks(5, "Bra", 5), List.of("task_id_2", "task_id_3"));
        check("L2 update", s2.updateTask(6, "task_id_1", "Alpha Updated", 20), true);
        check("L2 search Al limit1", s2.searchTasks(7, "Al", 1), List.of("task_id_1"));
        check("L2 search maxResults<=0", s2.searchTasks(8, "Al", 0), List.of());

        // ---- Level 3 ----
        TaskManagementSystem s3 = new TaskManagementSystem();
        check("L3 addUser", s3.addUser(1, "user1", 2), true);
        check("L3 addUser dup", s3.addUser(2, "user1", 3), false);
        s3.addTask(3, "Task A", 10);
        s3.addTask(4, "Task B", 5);
        check("L3 assign1", s3.assignTask(5, "task_id_1", "user1", 15), true);
        check("L3 getUser@6", s3.getUserTasks(6, "user1"), List.of("task_id_1"));
        check("L3 assign2", s3.assignTask(7, "task_id_2", "user1", 20), true);
        check("L3 getUser@8", s3.getUserTasks(8, "user1"), List.of("task_id_1", "task_id_2"));
        check("L3 assign quota-exceeded", s3.assignTask(9, "task_id_1", "user1", 25), false);
        check("L3 getUser@10", s3.getUserTasks(10, "user1"), List.of("task_id_1", "task_id_2"));
        check("L3 getUser@16 one-expired", s3.getUserTasks(16, "user1"), List.of("task_id_2"));
        check("L3 getUser@21 all-expired", s3.getUserTasks(21, "user1"), List.of());

        // ---- Level 4 (the big example) ----
        TaskManagementSystem s4 = new TaskManagementSystem();
        s4.addUser(1, "user1", 5);
        s4.addUser(2, "user2", 2);
        s4.addTask(3, "Task A", 10);   // task_id_1
        s4.addTask(4, "Task B", 5);    // task_id_2
        s4.addTask(5, "Task C", 8);    // task_id_3
        check("L4 assign t1", s4.assignTask(6, "task_id_1", "user1", 15), true);
        check("L4 complete t1@10", s4.completeTask(10, "task_id_1", "user1"), true);
        check("L4 getUser@11", s4.getUserTasks(11, "user1"), List.of());
        check("L4 assign t2", s4.assignTask(12, "task_id_2", "user1", 20), true);
        check("L4 assign t3", s4.assignTask(13, "task_id_3", "user1", 25), true);
        check("L4 getUser@14", s4.getUserTasks(14, "user1"), List.of("task_id_2", "task_id_3"));
        check("L4 complete wrong-user", s4.completeTask(16, "task_id_2", "user2"), false);
        check("L4 complete t2@17", s4.completeTask(17, "task_id_2", "user1"), true);
        check("L4 overdue@18", s4.getOverdueAssignments(18, "user1"), List.of());
        check("L4 overdue@22", s4.getOverdueAssignments(22, "user1"), List.of());
        check("L4 overdue@30", s4.getOverdueAssignments(30, "user1"), List.of("task_id_3"));
        check("L4 reassign t1 [35,45)", s4.assignTask(35, "task_id_1", "user1", 45), true);
        check("L4 reassign t1 [40,55)", s4.assignTask(40, "task_id_1", "user1", 55), true);
        check("L4 getUser@42 dup", s4.getUserTasks(42, "user1"), List.of("task_id_1", "task_id_1"));
        check("L4 complete earliest@43", s4.completeTask(43, "task_id_1", "user1"), true);
        check("L4 getUser@44", s4.getUserTasks(44, "user1"), List.of("task_id_1"));
        check("L4 assign t1 [50,60)", s4.assignTask(50, "task_id_1", "user1", 60), true);
        check("L4 overdue@65", s4.getOverdueAssignments(65, "user1"),
                List.of("task_id_3", "task_id_1", "task_id_1"));
        check("L4 addTask D", s4.addTask(70, "Task D", 15), "task_id_4");
        check("L4 assign t4", s4.assignTask(75, "task_id_4", "user1", 85), true);
        check("L4 complete t4@80", s4.completeTask(80, "task_id_4", "user1"), true);
        check("L4 overdue@90 (t4 done)", s4.getOverdueAssignments(90, "user1"),
                List.of("task_id_3", "task_id_1", "task_id_1"));

        System.out.println("\nPassed " + passed + ", Failed " + failed);
        if (failed == 0) System.out.println("ALL TASK-MANAGEMENT TESTS PASSED");
    }
}
