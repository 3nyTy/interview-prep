import java.util.*;

/**
 * Valon domain coding — PART 2 (1p3a 101b79f9)
 * "Filter Rows by Column Conditions"  — extend Part 1 with a WHERE-like filter.
 *
 * Spec: a condition is `column = value` (equality only). If the column does not
 * exist, return an empty result or raise an error — CLARIFY WITH THE INTERVIEWER.
 * Preserve insertion order in results.
 *
 * Every line below that is new or changed since Part 1 is marked. Everything
 * unmarked is byte-identical to Part 1 — that is the point of Part 1's structure.
 */
public class Part2 {

    /** column = value. Equality only; Part 2's spec says so, so no Op enum yet. */  // ← NEW in Part 2
    record Pred(String column, String value) {}                                       // ← NEW in Part 2

    static final class Table {
        final List<String> columns = new ArrayList<>();
        final List<Map<String, Object>> rows = new ArrayList<>();
    }

    private final Map<String, Table> tables = new HashMap<>();

    private Table tableOrCreate(String name) {
        return tables.computeIfAbsent(name, k -> new Table());
    }

    private Table table(String name) {
        Table t = tables.get(name);
        if (t == null) throw new NoSuchElementException("no such table: " + name);
        return t;
    }

    public void createTable(String name, List<String> columns) {
        Table t = tableOrCreate(name);
        t.columns.clear();
        t.columns.addAll(columns);
    }

    public void insert(String name, Map<String, ?> row) {
        tableOrCreate(name).rows.add(new LinkedHashMap<>(row));
    }

    /** Part 1's signature, kept so existing callers still compile. */
    public List<List<String>> select(String name, List<String> cols, String missing) {
        return select(name, cols, null, missing);            // ← CHANGED: now delegates, null == no filter
    }

    /** Part 1's select plus an optional predicate. */                                  // ← NEW in Part 2
    public List<List<String>> select(String name, List<String> cols,                    // ← NEW in Part 2
                                     Pred where, String missing) {                      // ← NEW in Part 2
        List<Map<String, Object>> hits = new ArrayList<>();                             // ← NEW: rows that survive
        for (Map<String, Object> r : table(name).rows)                                   // ← NEW: single linear scan
            if (where == null || matches(r, where)) hits.add(r);                        // ← NEW: order preserved by append
        return project(hits, cols, missing);                                            // ← CHANGED: projects hits, not all rows
    }

    /**                                                                                  // ← NEW in Part 2
     * Factored out even though Part 2 only needs equality — Parts 3-7 all call this      // ← NEW in Part 2
     * unchanged. Inlining the comparison here is what forces a rewrite later.            // ← NEW in Part 2
     */                                                                                  // ← NEW in Part 2
    private boolean matches(Map<String, Object> row, Pred p) {                           // ← NEW in Part 2
        if (!row.containsKey(p.column())) return false;                                  // ← NEW: ⭐ ABSENCE FAILS, it is not null-equal
        return String.valueOf(row.get(p.column())).equals(p.value());                    // ← NEW: exact string equality, per spec
    }

    private List<List<String>> project(List<Map<String, Object>> rows, List<String> cols, String missing) {
        List<List<String>> out = new ArrayList<>(rows.size());
        for (Map<String, Object> r : rows) {
            List<String> line = new ArrayList<>(cols.size());
            for (String c : cols) {
                Object v = r.get(c);
                line.add(v == null ? missing : String.valueOf(v));
            }
            out.add(line);
        }
        return out;
    }
    // ^ project() is completely untouched from Part 1. Filtering changes WHICH rows
    //   you render, never HOW — which is why Part 2 costs ~6 lines of real logic.

    /* ---------------------------- run loop ---------------------------- */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-50s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        Part2 db = new Part2();
        db.createTable("loans", List.of("id", "borrower", "state"));
        db.insert("loans", row("1", "alice", "CA"));
        db.insert("loans", row("2", "bob", "NY"));

        // the published example (cols id,name,city -> here id,borrower,state)
        check("where state=CA -> [[alice]]",
              db.select("loans", List.of("borrower"), new Pred("state", "CA"), ""),
              List.of(List.of("alice")));

        // ⭐ the trap: a row missing the filter column must NOT match
        Map<String, Object> noState = new LinkedHashMap<>();
        noState.put("id", "3"); noState.put("borrower", "carol");     // no state at all
        db.insert("loans", noState);
        check("row missing filter column excluded",
              db.select("loans", List.of("borrower"), new Pred("state", "CA"), ""),
              List.of(List.of("alice")));

        check("no match -> empty list",
              db.select("loans", List.of("borrower"), new Pred("state", "TX"), ""),
              List.of());
        check("filter on column absent from every row -> empty",
              db.select("loans", List.of("borrower"), new Pred("escrow", "0"), ""),
              List.of());
        check("null filter == Part 1 behaviour (all rows)",
              db.select("loans", List.of("id"), null, "").size(), 3);
        check("Part 1 3-arg overload still works",
              db.select("loans", List.of("id"), "").size(), 3);
        check("equality is case-sensitive",
              db.select("loans", List.of("borrower"), new Pred("state", "ca"), ""),
              List.of());
        check("insertion order preserved among matches",
              db.select("loans", List.of("id"), new Pred("state", "CA"), ""),
              List.of(List.of("1")));

        // string equality, not numeric — worth flagging to the interviewer
        Part2 n = new Part2();
        n.insert("t", Map.of("bal", "10"));
        check("\"10\" does not equal \"10.0\" (string compare)",
              n.select("t", List.of("bal"), new Pred("bal", "10.0"), ""), List.of());
        check("\"10\" equals \"10\"",
              n.select("t", List.of("bal"), new Pred("bal", "10"), ""), List.of(List.of("10")));

        System.out.println("\n  " + pass + " passed, " + fail + " failed");
    }

    static Map<String, Object> row(String id, String borrower, String state) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id); m.put("borrower", borrower); m.put("state", state);
        return m;
    }
}
