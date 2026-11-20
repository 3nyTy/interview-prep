import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;

/**
 * Valon domain coding — PART 4 (1p3a 709ae770)
 * "Design an In-Memory Database with SELECT, IN, and FILTER BY"
 *
 * Grammar:
 *   SELECT <cols> FROM <table> [WHERE <col> IN (<v1>,<v2>,...)] [FILTER BY <col> <op> <value>]
 *   <cols> is one name, comma-separated names, or *
 *   FILTER BY ops: = != < <= > >=
 *   Equality is exact string comparison.
 *   ⭐ Ordering compares NUMERICALLY IF BOTH SIDES convert to numbers, else lexicographically.
 *   Output: cols joined by ',', rows joined by ';', or EMPTY when nothing matches.
 *   n,q <= 1e4, m <= 50.
 *
 * Lines new or changed since Part 3 are marked. Unmarked lines are byte-identical
 * to Part 3.
 */
public class Part4 {

    /** The six FILTER BY operators. */                                                    // ← NEW in Part 4
    enum Op {                                                                              // ← NEW in Part 4
        EQ("="), NE("!="), LT("<"), LE("<="), GT(">"), GE(">=");                            // ← NEW in Part 4
        final String sym;                                                                  // ← NEW in Part 4
        Op(String s) { sym = s; }                                                           // ← NEW in Part 4
        static Op of(String s) {                                                            // ← NEW in Part 4
            for (Op o : values()) if (o.sym.equals(s)) return o;                            // ← NEW in Part 4
            throw new IllegalArgumentException("bad operator: " + s);                       // ← NEW in Part 4
        }                                                                                   // ← NEW in Part 4
    }                                                                                       // ← NEW in Part 4

    record Pred(String column, Op op, String value) {                    // ← CHANGED: gained an Op
        /** Parts 2-3 built equality predicates; keep them compiling. */ // ← NEW in Part 4
        Pred(String column, String value) { this(column, Op.EQ, value); } // ← NEW in Part 4
    }

    /** WHERE <col> IN (...). A set, so membership is O(1) not O(|values|). */             // ← NEW in Part 4
    record InPred(String column, Set<String> values) {}                                    // ← NEW in Part 4

    /**                                                                                     // ← NEW in Part 4
     * One query, so select() does not grow to seven positional arguments. Part 5 turns      // ← NEW in Part 4
     * `filter` into a LIST of AND predicates; this is the seam that makes that a one-       // ← NEW in Part 4
     * line change instead of another signature.                                             // ← NEW in Part 4
     */                                                                                     // ← NEW in Part 4
    record Query(List<String> cols, InPred in, Pred filter,                                 // ← NEW in Part 4
                 String orderBy, boolean ascending) {}                                      // ← NEW in Part 4

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

    /* ------------------- the Part 1-3 signatures, now thin ------------------- */

    public List<List<String>> select(String name, List<String> cols, String missing) {
        return select(name, cols, null, missing);
    }

    public List<List<String>> select(String name, List<String> cols,
                                     Pred where, String missing) {
        return select(name, cols, where, null, true, missing);
    }

    public List<List<String>> select(String name, List<String> cols,
                                     Pred where,
                                     String orderBy, boolean ascending,
                                     String missing) {
        return select(name, new Query(cols, null, where, orderBy, ascending), missing);  // ← CHANGED: delegates to the Query engine
    }

    /* ---------------------------- the engine ---------------------------- */

    /** THE ANSWER for Part 4. */                                                          // ← NEW in Part 4
    public List<List<String>> select(String name, Query q, String missing) {               // ← NEW in Part 4
        Table t = table(name);                                                             // ← NEW in Part 4
        List<String> cols = expand(t, q.cols());              // ← NEW: '*' resolves here, in SCHEMA order
        Predicate<Map<String, Object>> keep = compile(q);     // ← NEW: literals parsed ONCE per query, not per row

        List<Map<String, Object>> hits = new ArrayList<>();
        for (Map<String, Object> r : t.rows)
            if (keep.test(r)) hits.add(r);                    // ← CHANGED: one compiled predicate replaces the inline matches() call
        if (q.orderBy() != null) sortHits(hits, q.orderBy(), q.ascending());
        return project(hits, cols, missing);
    }

    /** '*' means every schema column, in declared order — not row key order. */           // ← NEW in Part 4
    private static List<String> expand(Table t, List<String> cols) {                       // ← NEW in Part 4
        if (cols.size() != 1 || !cols.get(0).equals("*")) return cols;                      // ← NEW in Part 4
        if (!t.columns.isEmpty()) return t.columns;           // ← NEW: this is what createTable's column list is FOR
        Set<String> seen = new LinkedHashSet<>();             // ← NEW: insert-only table (Part 1 allowed it) -> first-seen key order
        for (Map<String, Object> r : t.rows) seen.addAll(r.keySet());                       // ← NEW in Part 4
        return new ArrayList<>(seen);                                                       // ← NEW in Part 4
    }                                                                                       // ← NEW in Part 4

    /**                                                                                     // ← NEW in Part 4
     * Compiling beats interpreting: the IN set is already a HashSet and the FILTER BY       // ← NEW in Part 4
     * literal is parsed once, so the per-row cost is a map lookup plus one compare.         // ← NEW in Part 4
     */                                                                                     // ← NEW in Part 4
    private static Predicate<Map<String, Object>> compile(Query q) {                         // ← NEW in Part 4
        Predicate<Map<String, Object>> p = r -> true;                                        // ← NEW in Part 4
        if (q.in() != null) {                                                                // ← NEW in Part 4
            InPred in = q.in();                                                              // ← NEW in Part 4
            p = p.and(r -> {                                                                 // ← NEW in Part 4
                Object v = r.get(in.column());                                               // ← NEW in Part 4
                return v != null && in.values().contains(String.valueOf(v));                 // ← NEW: absence fails IN
            });                                                                              // ← NEW in Part 4
        }                                                                                    // ← NEW in Part 4
        if (q.filter() != null) p = p.and(compileFilter(q.filter()));                         // ← NEW in Part 4
        return p;                                                                            // ← NEW in Part 4
    }                                                                                        // ← NEW in Part 4

    private static Predicate<Map<String, Object>> compileFilter(Pred f) {                    // ← NEW in Part 4
        final BigDecimal lit = num(f.value());   // ← NEW: parsed ONCE. null => no numeric compare is possible at all
        return row -> {                                                                      // ← NEW in Part 4
            Object v = row.get(f.column());                                                  // ← NEW in Part 4
            // ⭐ Absence fails EVERY operator, != included. SQL: NULL != 'x' is NULL, not
            //    true, so the row is not returned. Same rule Part 2 established.
            if (v == null) return false;                                                     // ← NEW in Part 4
            String s = String.valueOf(v);                                                    // ← NEW in Part 4
            if (f.op() == Op.EQ) return s.equals(f.value());   // ← NEW: exact string equality, per spec
            if (f.op() == Op.NE) return !s.equals(f.value());  // ← NEW in Part 4
            int c;                                                                           // ← NEW in Part 4
            if (lit == null) {                                                               // ← NEW in Part 4
                c = s.compareTo(f.value());       // ← NEW: literal is not a number -> BOTH sides cannot be, skip parsing rows entirely
            } else {                                                                         // ← NEW in Part 4
                BigDecimal rv = num(s);                                                      // ← NEW in Part 4
                c = (rv == null) ? s.compareTo(f.value()) : rv.compareTo(lit);                // ← NEW: numeric only when BOTH sides parse
            }                                                                                // ← NEW in Part 4
            return switch (f.op()) {                                                         // ← NEW in Part 4
                case LT -> c < 0;                                                            // ← NEW in Part 4
                case LE -> c <= 0;                                                           // ← NEW in Part 4
                case GT -> c > 0;                                                            // ← NEW in Part 4
                case GE -> c >= 0;                                                           // ← NEW in Part 4
                default -> throw new IllegalStateException();                                // ← NEW in Part 4
            };                                                                               // ← NEW in Part 4
        };                                                                                   // ← NEW in Part 4
    }                                                                                        // ← NEW in Part 4

    private static BigDecimal num(String s) {                                                // ← NEW in Part 4
        try { return new BigDecimal(s); } catch (NumberFormatException e) { return null; }    // ← NEW in Part 4
    }                                                                                        // ← NEW in Part 4

    private boolean matches(Map<String, Object> row, Pred p) {
        return compileFilter(p).test(row);   // ← CHANGED: Part 2's method now defers to the compiled form, one definition of truth
    }

    private void sortHits(List<Map<String, Object>> hits,
                          String orderBy, boolean ascending) {
        final boolean numeric = numericColumn(hits, orderBy);

        Comparator<Map<String, Object>> cmp = (x, y) -> {
            Object ax = x.get(orderBy), ay = y.get(orderBy);
            if (ax == null && ay == null) return 0;
            if (ax == null) return 1;
            if (ay == null) return -1;
            String sx = String.valueOf(ax), sy = String.valueOf(ay);
            return numeric ? new BigDecimal(sx).compareTo(new BigDecimal(sy))
                           : sx.compareTo(sy);
        };
        hits.sort(ascending ? cmp : cmp.reversed());
    }

    private static boolean numericColumn(List<Map<String, Object>> rows, String col) {
        boolean sawOne = false;
        for (Map<String, Object> r : rows) {
            Object v = r.get(col);
            if (v == null) continue;
            if (num(String.valueOf(v)) == null) return false;   // ← CHANGED: reuses num(), no duplicate try/catch
            sawOne = true;
        }
        return sawOne;
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
    // ^ project() and sortHits() are still untouched from Part 3.

    /* --------------------- query parsing + stdin driver --------------------- */
    // All NEW in Part 4. Kept apart from the engine on purpose: parsing is I/O, the
    // engine is the data structure. They asked for a stdin format, so this is where
    // it lives.

    static Query parse(String raw) {                                                        // ← NEW in Part 4
        String s = raw.trim();
        String filterPart = null, wherePart = null;
        int fb = s.indexOf(" FILTER BY ");        // two words, and it is always the last clause
        if (fb >= 0) { filterPart = s.substring(fb + " FILTER BY ".length()).trim(); s = s.substring(0, fb); }
        int wh = s.indexOf(" WHERE ");
        if (wh >= 0) { wherePart = s.substring(wh + " WHERE ".length()).trim(); s = s.substring(0, wh); }

        // what remains is "SELECT <cols> FROM <table>"; the table is ignorable, there is one
        String head = s.trim();
        if (!head.startsWith("SELECT ")) throw new IllegalArgumentException("not a SELECT: " + raw);
        head = head.substring("SELECT ".length()).trim();
        int from = head.indexOf(" FROM ");
        String colPart = from >= 0 ? head.substring(0, from) : head;
        List<String> cols = new ArrayList<>();
        for (String c : colPart.split(",")) cols.add(c.trim());

        InPred in = null;
        if (wherePart != null) {
            int at = wherePart.indexOf(" IN ");
            String col = wherePart.substring(0, at).trim();
            String body = wherePart.substring(at + " IN ".length()).trim();
            if (body.startsWith("(")) body = body.substring(1);
            if (body.endsWith(")"))   body = body.substring(0, body.length() - 1);
            Set<String> vals = new HashSet<>();
            for (String v : body.split(",")) vals.add(v.trim());
            in = new InPred(col, vals);
        }

        Pred filter = null;
        if (filterPart != null) {
            String[] tok = filterPart.split("\\s+");   // values contain no spaces, per spec
            filter = new Pred(tok[0], Op.of(tok[1]), tok[2]);
        }
        return new Query(cols, in, filter, null, true);
    }

    /** Runs the published stdin format and returns the exact stdout. */                    // ← NEW in Part 4
    static String runDriver(String input) {
        Scanner sc = new Scanner(input);
        int n = sc.nextInt(), m = sc.nextInt();
        sc.nextLine();
        List<String> cols = Arrays.asList(sc.nextLine().trim().split(","));

        Part4 db = new Part4();
        db.createTable("t", cols);
        for (int i = 0; i < n; i++) {
            String[] vals = sc.nextLine().trim().split(",", -1);
            Map<String, Object> row = new LinkedHashMap<>();
            for (int j = 0; j < m; j++) row.put(cols.get(j), vals[j]);
            db.insert("t", row);
        }
        int q = Integer.parseInt(sc.nextLine().trim());

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < q; i++) {
            List<List<String>> res = db.select("t", parse(sc.nextLine()), "");
            if (res.isEmpty()) { out.append("EMPTY\n"); continue; }   // ⭐ easy to forget
            StringJoiner rows = new StringJoiner(";");
            for (List<String> r : res) rows.add(String.join(",", r));
            out.append(rows).append('\n');
        }
        return out.toString();
    }

    /* ---------------------------- run loop ---------------------------- */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-54s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        /* ---- the two published examples, byte for byte ---- */
        String in1 = """
                4 4
                id,name,age,city
                1,Alice,30,NY
                2,Bob,25,LA
                3,Carol,35,NY
                4,David,40,SF
                3
                SELECT name FROM users
                SELECT id,name FROM users WHERE city IN (NY,SF)
                SELECT name FROM users WHERE city IN (NY,LA) FILTER BY age > 30
                """;
        check("published example 1 (exact stdout)", runDriver(in1),
              "Alice;Bob;Carol;David\n1,Alice;3,Carol;4,David\nCarol\n");

        String in2 = """
                3 3
                id,name,age
                1,Alice,30
                2,Bob,25
                3,Carol,35
                2
                SELECT name FROM users
                SELECT id,name FROM users
                """;
        check("published example 2 (exact stdout)", runDriver(in2),
              "Alice;Bob;Carol\n1,Alice;2,Bob;3,Carol\n");

        /* ---- build the same table via the API for the rest ---- */
        Part4 db = new Part4();
        db.createTable("t", List.of("id", "name", "age", "city"));
        db.insert("t", row("1", "Alice", "30", "NY"));
        db.insert("t", row("2", "Bob", "25", "LA"));
        db.insert("t", row("3", "Carol", "35", "NY"));
        db.insert("t", row("4", "David", "40", "SF"));

        check("'*' expands in SCHEMA order",
              db.select("t", parse("SELECT * FROM t WHERE id IN (1)"), ""),
              List.of(List.of("1", "Alice", "30", "NY")));
        check("EMPTY when nothing matches",
              runDriver("""
                        1 2
                        id,name
                        1,Alice
                        1
                        SELECT name FROM t FILTER BY id > 99
                        """), "EMPTY\n");

        check("!= excludes only the equal row",
              names(db.select("t", parse("SELECT name FROM t FILTER BY city != NY"), "")),
              List.of("Bob", "David"));
        check("<  strictly less",  names(db.select("t", parse("SELECT name FROM t FILTER BY age < 30"), "")),  List.of("Bob"));
        check("<= includes equal", names(db.select("t", parse("SELECT name FROM t FILTER BY age <= 30"), "")), List.of("Alice", "Bob"));
        check(">  strictly more",  names(db.select("t", parse("SELECT name FROM t FILTER BY age > 35"), "")),  List.of("David"));
        check(">= includes equal", names(db.select("t", parse("SELECT name FROM t FILTER BY age >= 35"), "")), List.of("Carol", "David"));

        // ⭐ numeric when both sides parse: age > 9 must match every row
        check("age > 9 -> all four (numeric, not lexicographic)",
              names(db.select("t", parse("SELECT name FROM t FILTER BY age > 9"), "")),
              List.of("Alice", "Bob", "Carol", "David"));
        check("  (lexicographic would have matched none)", "30".compareTo("9") < 0, true);

        // ⭐ lexicographic when the literal is not a number
        check("city > NA -> lexicographic",
              names(db.select("t", parse("SELECT name FROM t FILTER BY city > NA"), "")),
              List.of("Alice", "Carol", "David"));

        // ⭐ absence fails != as well as =
        Part4 sp = new Part4();
        sp.createTable("t", List.of("id", "name", "city"));
        sp.insert("t", row3("1", "hasCity", "NY"));
        Map<String, Object> bare = new LinkedHashMap<>();
        bare.put("id", "2"); bare.put("name", "noCity");
        sp.insert("t", bare);
        check("absent column fails '=' ",
              names(sp.select("t", parse("SELECT name FROM t FILTER BY city = LA"), "")), List.of());
        check("absent column fails '!=' too (SQL NULL semantics)",
              names(sp.select("t", parse("SELECT name FROM t FILTER BY city != LA"), "")),
              List.of("hasCity"));

        check("IN + FILTER BY combined",
              names(db.select("t", parse("SELECT name FROM t WHERE city IN (NY,LA) FILTER BY age > 30"), "")),
              List.of("Carol"));
        check("IN with one value",
              names(db.select("t", parse("SELECT name FROM t WHERE city IN (SF)"), "")), List.of("David"));
        check("IN value absent from data -> empty",
              names(db.select("t", parse("SELECT name FROM t WHERE city IN (TX)"), "")), List.of());

        /* ---- Parts 1-3 must still work ---- */
        check("Part 1 3-arg overload", db.select("t", List.of("id"), "").size(), 4);
        check("Part 2 4-arg overload (equality Pred)",
              names(db.select("t", List.of("name"), new Pred("city", "SF"), "")), List.of("David"));
        check("Part 3 ORDER BY still works",
              names(db.select("t", List.of("name"), null, "age", false, "")),
              List.of("David", "Carol", "Alice", "Bob"));
        check("Part 3 stability + Part 4 filter together",
              names(db.select("t", new Query(List.of("name"), null,
                                             new Pred("age", Op.GE, "30"), "city", true), "")),
              List.of("Alice", "Carol", "David"));

        /* ---- parse-literal-once, measured ---- */
        Part4 big = new Part4();
        Random rnd = new Random(11);
        List<Map<String, Object>> raw = new ArrayList<>();
        for (int i = 0; i < 200_000; i++) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", String.valueOf(i));
            m.put("city", "c" + rnd.nextInt(50_000));
            big.insert("t", m);
            raw.add(m);
        }
        Query textQ = parse("SELECT id FROM t FILTER BY city > NA");
        long t0 = System.nanoTime();
        int got = big.select("t", textQ, "").size();
        long compiled = (System.nanoTime() - t0) / 1_000_000;

        t0 = System.nanoTime();
        int naive = 0;
        for (Map<String, Object> r : raw) {                 // the version that reparses both sides per row
            String s = String.valueOf(r.get("city"));
            BigDecimal a = num(s), b = num("NA");
            int c = (a != null && b != null) ? a.compareTo(b) : s.compareTo("NA");
            if (c > 0) naive++;
        }
        long perRow = (System.nanoTime() - t0) / 1_000_000;
        System.out.printf("%n  200k rows, non-numeric literal:  literal-parsed-once %dms   reparse-per-row %dms   (both matched %d)%n",
                          compiled, perRow, got == naive ? got : -1);

        System.out.println("\n  " + pass + " passed, " + fail + " failed");
    }

    /* helpers */
    static List<String> names(List<List<String>> rows) {
        List<String> out = new ArrayList<>();
        for (List<String> r : rows) out.add(r.get(0));
        return out;
    }

    static Map<String, Object> row(String id, String name, String age, String city) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id); m.put("name", name); m.put("age", age); m.put("city", city);
        return m;
    }
    static Map<String, Object> row3(String id, String name, String city) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id); m.put("name", name); m.put("city", city);
        return m;
    }
}
