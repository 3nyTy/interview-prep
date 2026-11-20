import java.math.BigDecimal;
import java.util.*;

/**
 * Valon domain coding — PART 3 (1p3a e711c837)
 * "In-Memory SQL Database: Order By a Column"
 *
 * Spec: query(select_cols, where=None, order_by=None, ascending=True).
 *   - sort by the values in the order_by column
 *   - ties MUST preserve insertion order (stable sort)
 *   - "values may be compared as strings; if numeric sorting is required, describe parsing"
 *   - N up to 1e5 rows, M up to 50 columns
 *
 * Lines new or changed since Part 2 are marked. Unmarked lines are byte-identical
 * to Part 2.
 */
public class Part3 {

    record Pred(String column, String value) {}

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

    /** Part 1's signature. */
    public List<List<String>> select(String name, List<String> cols, String missing) {
        return select(name, cols, null, missing);
    }

    /** Part 2's signature, kept so Part 2's callers still compile. */
    public List<List<String>> select(String name, List<String> cols,
                                     Pred where, String missing) {
        return select(name, cols, where, null, true, missing);   // ← CHANGED: delegates; null order_by == unsorted
    }

    /** Part 2's select plus ORDER BY. THE ANSWER for Part 3. */                          // ← NEW in Part 3
    public List<List<String>> select(String name, List<String> cols,                      // ← NEW in Part 3
                                     Pred where,                                          // ← NEW in Part 3
                                     String orderBy, boolean ascending,                   // ← NEW in Part 3
                                     String missing) {                                    // ← NEW in Part 3
        List<Map<String, Object>> hits = new ArrayList<>();
        for (Map<String, Object> r : table(name).rows)
            if (where == null || matches(r, where)) hits.add(r);
        if (orderBy != null) sortHits(hits, orderBy, ascending);   // ← NEW: sort ROWS, before projecting
        return project(hits, cols, missing);
    }

    private boolean matches(Map<String, Object> row, Pred p) {
        if (!row.containsKey(p.column())) return false;
        return String.valueOf(row.get(p.column())).equals(p.value());
    }

    /**                                                                                    // ← NEW in Part 3
     * `hits` holds REFERENCES into table.rows, but is itself a fresh list, so sorting      // ← NEW in Part 3
     * it never disturbs insertion order. Sorting table.rows in place would corrupt         // ← NEW in Part 3
     * every later unsorted query — test 10 pins that.                                      // ← NEW in Part 3
     */                                                                                    // ← NEW in Part 3
    private void sortHits(List<Map<String, Object>> hits,                                  // ← NEW in Part 3
                          String orderBy, boolean ascending) {                             // ← NEW in Part 3
        final boolean numeric = numericColumn(hits, orderBy);   // ← NEW: decided ONCE, O(n), not per comparison

        Comparator<Map<String, Object>> cmp = (x, y) -> {                                  // ← NEW in Part 3
            Object ax = x.get(orderBy), ay = y.get(orderBy);                               // ← NEW in Part 3
            if (ax == null && ay == null) return 0;                                        // ← NEW: both missing -> tie, stability decides
            if (ax == null) return 1;                                                      // ← NEW: missing sorts LAST on ASC
            if (ay == null) return -1;                                                     // ← NEW in Part 3
            String sx = String.valueOf(ax), sy = String.valueOf(ay);                       // ← NEW in Part 3
            return numeric ? new BigDecimal(sx).compareTo(new BigDecimal(sy))              // ← NEW: 90 < 100
                           : sx.compareTo(sy);                                             // ← NEW: lexicographic fallback
        };

        // ⭐ cmp.reversed(), NEVER sort-then-Collections.reverse(). List.sort is stable
        //    (TimSort); reversed() still returns 0 for ties so their order survives,
        //    whereas reversing the list flips tied rows too. Test 5 proves it.
        hits.sort(ascending ? cmp : cmp.reversed());                                       // ← NEW in Part 3
    }

    /**                                                                                    // ← NEW in Part 3
     * True when every present value in the column parses as a number. Classifying once     // ← NEW in Part 3
     * costs n parses; try/catch inside the comparator costs ~n log n exceptions on a        // ← NEW in Part 3
     * text column, and exception construction is the expensive part. Benchmarked below.     // ← NEW in Part 3
     */                                                                                    // ← NEW in Part 3
    private static boolean numericColumn(List<Map<String, Object>> rows, String col) {      // ← NEW in Part 3
        boolean sawOne = false;                                                             // ← NEW in Part 3
        for (Map<String, Object> r : rows) {                                                // ← NEW in Part 3
            Object v = r.get(col);                                                          // ← NEW in Part 3
            if (v == null) continue;                                                        // ← NEW: absent values do not vote
            try { new BigDecimal(String.valueOf(v)); sawOne = true; }                       // ← NEW in Part 3
            catch (NumberFormatException e) { return false; }                               // ← NEW: one non-number -> whole column is text
        }                                                                                   // ← NEW in Part 3
        return sawOne;                                                                      // ← NEW: an all-absent column is not "numeric"
    }                                                                                       // ← NEW in Part 3

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
    // ^ project() and matches() are still untouched. Ordering changes the SEQUENCE of
    //   rows, not which rows survive nor how they render.

    /* ---------------------------- run loop ---------------------------- */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-52s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        Part3 db = new Part3();
        db.createTable("loans", List.of("id", "name", "score"));
        db.insert("loans", row("1", "alice", "90"));
        db.insert("loans", row("2", "bob", "80"));

        check("published example: order_by score asc -> bob,alice",
              db.select("loans", List.of("name"), null, "score", true, ""),
              List.of(List.of("bob"), List.of("alice")));
        check("descending -> alice,bob",
              db.select("loans", List.of("name"), null, "score", false, ""),
              List.of(List.of("alice"), List.of("bob")));

        // ⭐ numeric vs lexicographic: "100" sorts BEFORE "90" as a string
        Part3 nm = new Part3();
        nm.insert("t", row("1", "big", "100"));
        nm.insert("t", row("2", "small", "90"));
        check("numeric compare: 90 before 100",
              nm.select("t", List.of("name"), null, "score", true, ""),
              List.of(List.of("small"), List.of("big")));
        check("  (lexicographic would have given big,small)",
              "100".compareTo("90") < 0, true);

        // ⭐ stability, both directions
        Part3 st = new Part3();
        for (String who : List.of("a", "b", "c", "d")) st.insert("t", row(who, who, "5"));
        check("ties keep insertion order (asc)",
              st.select("t", List.of("name"), null, "score", true, ""),
              List.of(List.of("a"), List.of("b"), List.of("c"), List.of("d")));
        check("ties keep insertion order (desc) <- reversed() not reverse()",
              st.select("t", List.of("name"), null, "score", false, ""),
              List.of(List.of("a"), List.of("b"), List.of("c"), List.of("d")));

        // rows missing the sort column
        Part3 ms = new Part3();
        ms.insert("t", row("1", "hasScore", "50"));
        Map<String, Object> bare = new LinkedHashMap<>();
        bare.put("id", "2"); bare.put("name", "noScore");
        ms.insert("t", bare);
        ms.insert("t", row("3", "alsoHas", "10"));
        check("missing sort column goes LAST on asc",
              ms.select("t", List.of("name"), null, "score", true, ""),
              List.of(List.of("alsoHas"), List.of("hasScore"), List.of("noScore")));
        check("missing sort column goes FIRST on desc (Postgres default)",
              ms.select("t", List.of("name"), null, "score", false, ""),
              List.of(List.of("noScore"), List.of("hasScore"), List.of("alsoHas")));

        // sort key need not be projected
        check("order_by a column NOT in select_cols",
              db.select("loans", List.of("id"), null, "score", true, ""),
              List.of(List.of("2"), List.of("1")));

        // where + order_by together
        Part3 wo = new Part3();
        wo.insert("t", rowS("id", "1", "state", "CA", "score", "70"));
        wo.insert("t", rowS("id", "2", "state", "NY", "score", "99"));
        wo.insert("t", rowS("id", "3", "state", "CA", "score", "40"));
        check("where state=CA + order_by score asc",
              wo.select("t", List.of("id"), new Pred("state", "CA"), "score", true, ""),
              List.of(List.of("3"), List.of("1")));

        // ⭐ a sorted query must not mutate stored insertion order
        db.select("loans", List.of("id"), null, "score", true, "");
        check("insertion order intact after a sorted query",
              db.select("loans", List.of("id"), ""),
              List.of(List.of("1"), List.of("2")));

        check("Part 2 4-arg overload still works",
              db.select("loans", List.of("name"), new Pred("id", "1"), ""),
              List.of(List.of("alice")));
        check("Part 1 3-arg overload still works",
              db.select("loans", List.of("id"), "").size(), 2);

        // lexicographic fallback on a text column
        check("text column sorts lexicographically",
              db.select("loans", List.of("name"), null, "name", true, ""),
              List.of(List.of("alice"), List.of("bob")));

        /* ---- why reversed() and not reverse(): shown, not asserted ---- */
        List<String> tied = new ArrayList<>(List.of("a", "b", "c", "d"));
        List<String> wrong = new ArrayList<>(tied);
        wrong.sort(Comparator.comparingInt(s -> 0));      // all equal
        Collections.reverse(wrong);                        // the bug
        System.out.println("\n  sort-then-reverse on all-tied input : " + wrong + "   <- ties flipped");
        List<String> right = new ArrayList<>(tied);
        right.sort(Comparator.<String>comparingInt(s -> 0).reversed());
        System.out.println("  reversed() comparator              : " + right + "   <- ties preserved");

        /* ---- classify-once vs try/catch-per-compare, on a text column ---- */
        Part3 big = new Part3();
        Random rnd = new Random(7);
        List<Map<String, Object>> raw = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", String.valueOf(i));
            m.put("city", "c" + rnd.nextInt(50_000));      // never numeric
            big.insert("t", m);
            raw.add(m);
        }
        long t0 = System.nanoTime();
        big.select("t", List.of("id"), null, "city", true, "");
        long classified = (System.nanoTime() - t0) / 1_000_000;

        List<Map<String, Object>> copy = new ArrayList<>(raw);
        t0 = System.nanoTime();
        copy.sort((x, y) -> {
            String sx = String.valueOf(x.get("city")), sy = String.valueOf(y.get("city"));
            try { return new BigDecimal(sx).compareTo(new BigDecimal(sy)); }
            catch (NumberFormatException e) { return sx.compareTo(sy); }
        });
        long perCompare = (System.nanoTime() - t0) / 1_000_000;
        System.out.printf("%n  100k rows, text sort column:  classify-once %dms   try/catch-per-compare %dms%n",
                          classified, perCompare);

        System.out.println("\n  " + pass + " passed, " + fail + " failed");
    }

    static Map<String, Object> row(String id, String name, String score) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id); m.put("name", name); m.put("score", score);
        return m;
    }

    static Map<String, Object> rowS(String... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) m.put(kv[i], kv[i + 1]);
        return m;
    }
}
