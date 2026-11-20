import java.util.*;

/**
 * Valon domain coding — PART 1 (1p3a bc8b7bb7)
 * "Insert and Select by Column Names"
 *
 * Create a table from a list of column names. Insert rows as column=value.
 * select(columns) returns a 2D list, one list per row, in the requested order.
 * A requested column missing from a row -> empty string or NULL (ask the interviewer).
 *
 * ⭐ THE ONE DECISION: a row is a Map, not a class with fields or a String[].
 * Every later requirement lets rows omit columns, and only a map can tell
 * "absent" from "present but empty". Flexible SHAPE, minimal BEHAVIOUR.
 */
public class Part1 {

    static final class Table {
        // declared order — rows don't need it, but SELECT * later must expand in this order
        final List<String> columns = new ArrayList<>();
        // insertion order IS part of the contract, so a List (not a Map keyed by id)
        final List<Map<String, Object>> rows = new ArrayList<>();
    }

    private final Map<String, Table> tables = new HashMap<>();   // lookup only; order irrelevant

    /** create-on-first-use, so insert works whether or not createTable was called */
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
        // copy so the caller can't mutate our row afterwards; LinkedHashMap keeps field order
        tableOrCreate(name).rows.add(new LinkedHashMap<>(row));
    }

    /** @param missing what to emit when a requested column is absent from a row */
    public List<List<String>> select(String name, List<String> cols, String missing) {
        return project(table(name).rows, cols, missing);
    }

    /**
     * Separated from select() deliberately: later parts change WHICH rows you render,
     * never HOW. Filtering and sorting will reuse this untouched.
     */
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

    /* ---------------- run loop: set this up FIRST in the interview ---------------- */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-46s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        Part1 db = new Part1();
        db.createTable("loans", List.of("id", "borrower", "balance"));
        db.insert("loans", Map.of("id", "1", "borrower", "alice", "balance", "1000"));

        // the published example, in loan clothing
        check("select(borrower,id)", db.select("loans", List.of("borrower", "id"), ""),
              List.of(List.of("alice", "1")));

        // edge cases you raise WITHOUT being asked
        Map<String, Object> sparse = new LinkedHashMap<>();
        sparse.put("id", "2"); sparse.put("borrower", "bob");        // no balance
        db.insert("loans", sparse);
        check("absent column -> missing marker",
              db.select("loans", List.of("borrower", "balance"), ""),
              List.of(List.of("alice", "1000"), List.of("bob", "")));
        check("column not in schema at all",
              db.select("loans", List.of("escrow"), ""),
              List.of(List.of(""), List.of("")));
        check("insertion order preserved",
              db.select("loans", List.of("id"), ""),
              List.of(List.of("1"), List.of("2")));
        check("projection order follows request, not schema",
              db.select("loans", List.of("balance", "id"), ""),
              List.of(List.of("1000", "1"), List.of("", "2")));
        check("empty projection", db.select("loans", List.of(), ""),
              List.of(List.of(), List.of()));
        check("insert into unknown table auto-creates", autoCreate(), 1);

        System.out.println("\n  " + pass + " passed, " + fail + " failed");
    }

    static int autoCreate() {
        Part1 d = new Part1();
        d.insert("fresh", Map.of("id", "9"));              // no createTable call
        return d.select("fresh", List.of("id"), "").size();
    }
}
