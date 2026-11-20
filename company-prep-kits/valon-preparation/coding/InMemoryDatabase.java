import java.util.*;

/**
 * Valon — In-Memory Database. ALL SEVEN 1point3acres variants are one progressive problem.
 *
 *   PART 1  bc8b7bb7  Insert and Select by Column Names
 *   PART 2  101b79f9  Filter Rows by Column Conditions          (WHERE equality)
 *   PART 3  e711c837  Order By a Column                          (stable, asc/desc)
 *   PART 4  d5d1b167  AND Filters                                (upsert by id, = > <)
 *   PART 5  709ae770  SELECT, IN, FILTER BY                      (IN set, six ops, *)
 *   PART 6  85ac94f9  Flexible-Schema, Column-Based Query        (column -> ids index)
 *   PART 7  e1de73a3  Sparse Table with Filtering and Projection (typed schema, rename)
 *
 * ---------------------------------------------------------------------------
 * THE THREE DECISIONS THAT CARRY THE WHOLE PROBLEM
 *
 * 1. ROW = Map, NOT a fixed array. Every variant allows a row to omit columns
 *    ("sparse", "flexible schema", "the attribute does not exist"). A LinkedHashMap
 *    per row makes absence first-class: containsKey() is the whole game. An array
 *    with sentinels cannot tell "absent" from "empty string", and several variants
 *    hinge on exactly that distinction.
 *
 * 2. ABSENT IS NOT FALSE, AND ABSENT IS NOT NULL-EQUAL. Stated in four of the seven:
 *    "a row missing the filter column does not match." But a missing *output* column
 *    projects as null. So absence FAILS a predicate and YIELDS null on projection —
 *    two different behaviours from one condition. Conflating them is the main bug.
 *
 * 3. INSERTION ORDER IS PART OF THE CONTRACT. Every variant says preserve it, and
 *    Part 4 sharpens it: replacing an existing id must NOT move the row. So rows
 *    live in an ArrayList and id -> position is a side index. Sorting must be STABLE.
 * ---------------------------------------------------------------------------
 */
public class InMemoryDatabase {

    /* ===================== shared model ===================== */

    enum ColType { INT, STRING, BOOL }

    enum Op {
        EQ("="), NE("!="), LT("<"), LE("<="), GT(">"), GE(">=");
        final String sym;
        Op(String s) { sym = s; }
        static Op of(String s) {
            for (Op o : values()) if (o.sym.equals(s)) return o;
            throw new IllegalArgumentException("unknown operator: " + s);
        }
    }

    /** column OP literal */
    record Pred(String column, Op op, String value) {}
    /** column IN (v1,v2,...) */
    record InPred(String column, Set<String> values) {}

    static final class Table {
        final List<String> columns = new ArrayList<>();            // declared order (may stay empty)
        Map<String, ColType> schema;                               // PART 7 only; null = untyped
        final List<Map<String, Object>> rows = new ArrayList<>();   // insertion order IS the contract
        final Map<String, Integer> byId = new HashMap<>();          // PART 4: id -> row index
        final Map<String, Set<Integer>> colIndex = new HashMap<>(); // PART 6: column -> row indexes
    }

    private final Map<String, Table> tables = new LinkedHashMap<>();

    private Table table(String name) {
        Table t = tables.get(name);
        if (t == null) throw new NoSuchElementException("no such table: " + name);
        return t;
    }
    /** PART 4: "a table is created automatically on its first insert." */
    private Table tableOrCreate(String name) {
        return tables.computeIfAbsent(name, k -> new Table());
    }

    /* ===================================================================
     * PART 1 — create, insert, select by column names.        (bc8b7bb7)
     *
     * select returns a 2D list in the requested column order. A requested column
     * absent from a row becomes `missing` (the spec says empty string or NULL and
     * to "align with the interviewer") — so it is a parameter, not a hardcoded "".
     * =================================================================== */
    public void createTable(String name, List<String> columns) {
        Table t = tableOrCreate(name);
        t.columns.clear();
        t.columns.addAll(columns);
    }

    public void insert(String name, Map<String, ?> row) {
        Table t = tableOrCreate(name);
        addRow(t, new LinkedHashMap<>(row));
    }

    /** single place where a row lands, so every index stays consistent */
    private void addRow(Table t, Map<String, Object> row) {
        int pos = t.rows.size();
        t.rows.add(row);
        indexRow(t, row, pos);
    }
    private void indexRow(Table t, Map<String, Object> row, int pos) {
        for (String c : row.keySet())                                  // PART 6
            t.colIndex.computeIfAbsent(c, k -> new LinkedHashSet<>()).add(pos);
    }

    public List<List<String>> select(String name, List<String> cols, String missing) {
        return project(table(name).rows, cols, missing);
    }

    private List<List<String>> project(List<Map<String, Object>> rows, List<String> cols, String missing) {
        List<List<String>> out = new ArrayList<>(rows.size());
        for (Map<String, Object> r : rows) {
            List<String> line = new ArrayList<>(cols.size());
            for (String c : cols) {
                Object v = r.get(c);
                line.add(v == null ? missing : String.valueOf(v));     // absent -> `missing`
            }
            out.add(line);
        }
        return out;
    }

    /* ===================================================================
     * PART 2 — WHERE equality.                                (101b79f9)
     *
     * CHANGES FROM PART 1:
     *   - select takes an optional Pred
     *   - matching is factored into matches(), which every later part reuses
     *   ⭐ a row MISSING the predicate column does not match — not "matches null"
     * =================================================================== */
    public List<List<String>> select(String name, List<String> cols, Pred where, String missing) {
        List<Map<String, Object>> hits = new ArrayList<>();
        for (Map<String, Object> r : table(name).rows)
            if (where == null || matches(r, where)) hits.add(r);
        return project(hits, cols, missing);
    }

    private boolean matches(Map<String, Object> row, Pred p) {
        if (!row.containsKey(p.column())) return false;                // ⭐ absence fails
        String actual = String.valueOf(row.get(p.column()));
        int c = compareSmart(actual, p.value());
        return switch (p.op()) {
            case EQ -> actual.equals(p.value());                       // equality is exact string
            case NE -> !actual.equals(p.value());
            case LT -> c < 0;  case LE -> c <= 0;
            case GT -> c > 0;  case GE -> c >= 0;
        };
    }

    /**
     * PART 5's rule, applied everywhere: "compare numerically if both sides can be
     * converted to numbers; otherwise compare lexicographically." Using it uniformly
     * also satisfies PART 4, where ordering operands are guaranteed integer strings.
     */
    static int compareSmart(String a, String b) {
        Long la = asLong(a), lb = asLong(b);
        if (la != null && lb != null) return Long.compare(la, lb);
        return a.compareTo(b);
    }
    static Long asLong(String s) {
        if (s == null || s.isEmpty()) return null;
        int i = (s.charAt(0) == '-' || s.charAt(0) == '+') ? 1 : 0;
        if (i == s.length()) return null;
        for (int k = i; k < s.length(); k++) if (!Character.isDigit(s.charAt(k))) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }

    /* ===================================================================
     * PART 3 — ORDER BY.                                      (e711c837)
     *
     * CHANGES FROM PART 2:
     *   - orderBy column + ascending flag
     *   ⭐ MUST be stable: "if multiple rows have the same sort key, preserve
     *     insertion order." List.sort IS stable (TimSort), so do NOT reach for a
     *     PriorityQueue here — a heap is not stable and would silently break this.
     *   - rows missing the sort column sort first ascending; stated nowhere, so ASK.
     * =================================================================== */
    public List<List<String>> query(String name, List<String> cols, Pred where,
                                    String orderBy, boolean ascending, String missing) {
        List<Map<String, Object>> hits = new ArrayList<>();
        for (Map<String, Object> r : table(name).rows)
            if (where == null || matches(r, where)) hits.add(r);

        if (orderBy != null) {
            Comparator<Map<String, Object>> cmp = (x, y) -> {
                Object xv = x.get(orderBy), yv = y.get(orderBy);
                if (xv == null && yv == null) return 0;
                if (xv == null) return -1;                             // absent sorts first asc
                if (yv == null) return 1;
                return compareSmart(String.valueOf(xv), String.valueOf(yv));
            };
            hits.sort(ascending ? cmp : cmp.reversed());               // stable both directions
        }
        return project(hits, cols, missing);
    }

    /* ===================================================================
     * PART 4 — upsert by id + AND predicates.                 (d5d1b167)
     *
     * CHANGES FROM PART 3:
     *   - upsert(): "inserting a row with an existing id replaces the entire old
     *     row" and ⭐ "replacing an existing id does not change its position"
     *   - selectAnd(): a list of predicates, ALL must hold
     *   - output is JSON objects, so missing output columns are literal null
     * =================================================================== */
    public void upsert(String name, Map<String, ?> row) {
        Table t = tableOrCreate(name);
        Object idv = row.get("id");
        if (idv == null) { insert(name, row); return; }                // no id: plain append
        String id = String.valueOf(idv);
        Integer pos = t.byId.get(id);
        Map<String, Object> fresh = new LinkedHashMap<>(row);
        if (pos == null) {
            t.byId.put(id, t.rows.size());
            addRow(t, fresh);
        } else {
            for (Set<Integer> s : t.colIndex.values()) s.remove(pos);  // reindex in place
            t.rows.set(pos, fresh);                                    // ⭐ position preserved
            indexRow(t, fresh, pos);
        }
    }

    public List<Map<String, Object>> selectAnd(String name, List<String> cols, List<Pred> preds) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map<String, Object> r : table(name).rows) {
            boolean ok = true;
            for (Pred p : preds) if (!matches(r, p)) { ok = false; break; }
            if (!ok) continue;
            Map<String, Object> proj = new LinkedHashMap<>();
            for (String c : cols) proj.put(c, r.get(c));               // absent -> null in JSON
            out.add(proj);
        }
        return out;
    }

    /** PART 4 WHERE grammar: predicates joined only by AND. No OR, no parentheses. */
    static List<Pred> parseAnd(String where) {
        List<Pred> ps = new ArrayList<>();
        if (where == null || where.isBlank()) return ps;
        for (String clause : where.split("(?i)\\s+AND\\s+")) {
            String s = clause.trim();
            int i = -1; Op op = null;
            for (Op cand : new Op[]{Op.GE, Op.LE, Op.NE, Op.EQ, Op.GT, Op.LT}) {  // 2-char first
                int at = s.indexOf(cand.sym);
                if (at >= 0 && (i < 0 || at < i || (at == i && cand.sym.length() > op.sym.length()))) {
                    i = at; op = cand;
                }
            }
            if (op == null) throw new IllegalArgumentException("no operator in: " + s);
            String col = s.substring(0, i).trim();
            String val = s.substring(i + op.sym.length()).trim();
            if (val.length() >= 2 && val.charAt(0) == '\'' && val.endsWith("'"))
                val = val.substring(1, val.length() - 1);              // strip 'string literal'
            ps.add(new Pred(col, op, val));
        }
        return ps;
    }

    /* ===================================================================
     * PART 5 — IN set, six operators, and *.                  (709ae770)
     *
     * CHANGES FROM PART 4:
     *   - InPred for "WHERE col IN (a,b,c)" — a HashSet, so membership is O(1)
     *     rather than scanning the value list per row
     *   - "*" expands to the declared column order
     *   - both clauses may appear together and are ANDed
     * =================================================================== */
    public List<List<String>> selectInFilter(String name, List<String> cols,
                                             InPred in, Pred filter, String missing) {
        Table t = table(name);
        List<String> outCols = (cols.size() == 1 && cols.get(0).equals("*"))
                ? new ArrayList<>(t.columns) : cols;
        List<Map<String, Object>> hits = new ArrayList<>();
        for (Map<String, Object> r : t.rows) {
            if (in != null) {
                Object v = r.get(in.column());
                if (v == null || !in.values().contains(String.valueOf(v))) continue;
            }
            if (filter != null && !matches(r, filter)) continue;
            hits.add(r);
        }
        return project(hits, outCols, missing);
    }

    /** SELECT <cols> FROM <t> [WHERE <c> IN (..)] [FILTER BY <c> <op> <v>] */
    public List<List<String>> runSql(String name, String q, String missing) {
        String s = q.trim();
        int selIdx = indexOfWord(s, "SELECT"), fromIdx = indexOfWord(s, "FROM");
        int whereIdx = indexOfWord(s, "WHERE"), filterIdx = indexOfWord(s, "FILTER BY");
        if (selIdx < 0 || fromIdx < 0) throw new IllegalArgumentException("bad query: " + q);

        List<String> cols = new ArrayList<>();
        for (String c : s.substring(selIdx + 6, fromIdx).trim().split(",")) cols.add(c.trim());

        InPred in = null;
        if (whereIdx >= 0) {
            int end = (filterIdx >= 0 && filterIdx > whereIdx) ? filterIdx : s.length();
            String w = s.substring(whereIdx + 5, end).trim();          // "city IN (NY,SF)"
            int lp = w.indexOf('('), rp = w.lastIndexOf(')');
            String col = w.substring(0, w.toUpperCase().indexOf(" IN")).trim();
            Set<String> vals = new LinkedHashSet<>();
            for (String v : w.substring(lp + 1, rp).split(",")) vals.add(v.trim());
            in = new InPred(col, vals);
        }
        Pred filter = null;
        if (filterIdx >= 0) {
            String[] p = s.substring(filterIdx + 9).trim().split("\\s+");
            filter = new Pred(p[0], Op.of(p[1]), p[2]);
        }
        return selectInFilter(name, cols, in, filter, missing);
    }
    private static int indexOfWord(String s, String w) { return s.toUpperCase().indexOf(w.toUpperCase()); }

    /* ===================================================================
     * PART 6 — query by column presence.                      (85ac94f9)
     *
     * CHANGES FROM PART 5:
     *   ⭐ colIndex (column -> row positions) built at insert time, so "which records
     *     have this attribute" is O(matches) instead of an O(N*K) scan. That index is
     *     the whole point of this variant — a DynamoDB-style sparse store where the
     *     query IS "who has this attribute".
     *   - filter variant intersects presence with an equality test
     * =================================================================== */
    public List<String> idsWithColumn(String name, String column) {
        Table t = table(name);
        List<String> ids = new ArrayList<>();
        for (int pos : t.colIndex.getOrDefault(column, Set.of())) {
            Object id = t.rows.get(pos).get("id");
            ids.add(id == null ? String.valueOf(pos) : String.valueOf(id));
        }
        return ids;
    }

    public List<String> idsWithColumnFiltered(String name, String target,
                                              String filterCol, String filterVal) {
        Table t = table(name);
        List<String> ids = new ArrayList<>();
        for (int pos : t.colIndex.getOrDefault(target, Set.of())) {
            Map<String, Object> r = t.rows.get(pos);
            if (!r.containsKey(filterCol)) continue;                   // absence fails the filter
            if (!String.valueOf(r.get(filterCol)).equals(filterVal)) continue;
            Object id = r.get("id");
            ids.add(id == null ? String.valueOf(pos) : String.valueOf(id));
        }
        return ids;
    }

    /* ===================================================================
     * PART 7 — typed schema, validation, projection renaming. (e1de73a3)
     *
     * CHANGES FROM PART 6:
     *   - createTyped() stores column -> ColType
     *   - insertTyped() rejects unknown columns and type mismatches
     *     ⭐ THE TRAP: "Python bool must not be accepted as an int." In Java the
     *       same trap is Boolean vs Long — checkType() rejects Boolean for INT
     *       explicitly, because a lenient truthiness check would let it through.
     *   - projection is a source -> outputName MAP, not a list; missing source
     *     column emits null under the OUTPUT name
     * =================================================================== */
    public void createTyped(String name, Map<String, ColType> schema) {
        Table t = tableOrCreate(name);
        t.schema = new LinkedHashMap<>(schema);
        t.columns.clear();
        t.columns.addAll(schema.keySet());
    }

    /** @throws IllegalArgumentException with the message the spec wants in {"error":...} */
    public void insertTyped(String name, Map<String, Object> row) {
        Table t = table(name);
        if (t.schema == null) throw new IllegalStateException("table has no schema: " + name);
        for (Map.Entry<String, Object> e : row.entrySet()) {
            ColType ct = t.schema.get(e.getKey());
            if (ct == null) throw new IllegalArgumentException("unknown column: " + e.getKey());
            if (!checkType(ct, e.getValue()))
                throw new IllegalArgumentException("type mismatch for column: " + e.getKey());
        }
        addRow(t, new LinkedHashMap<>(row));                           // sparse: omissions are fine
    }

    static boolean checkType(ColType ct, Object v) {
        return switch (ct) {
            case BOOL   -> v instanceof Boolean;
            case STRING -> v instanceof String;
            // ⭐ Boolean must NOT satisfy INT. Order matters: test Boolean first.
            case INT    -> !(v instanceof Boolean) && (v instanceof Long || v instanceof Integer);
        };
    }

    /** filter is optional; only eq is required. Validates columns and value type. */
    public List<Map<String, Object>> selectProjected(String name, Pred eqFilter,
                                                     Map<String, String> projection) {
        Table t = table(name);
        if (t.schema != null) {
            for (String src : projection.keySet())
                if (!t.schema.containsKey(src)) throw new IllegalArgumentException("unknown projected column: " + src);
            if (eqFilter != null && !t.schema.containsKey(eqFilter.column()))
                throw new IllegalArgumentException("unknown filter column: " + eqFilter.column());
        }
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map<String, Object> r : t.rows) {
            if (eqFilter != null) {
                if (!r.containsKey(eqFilter.column())) continue;       // ⭐ absence fails
                if (!String.valueOf(r.get(eqFilter.column())).equals(eqFilter.value())) continue;
            }
            Map<String, Object> proj = new LinkedHashMap<>();
            for (Map.Entry<String, String> e : projection.entrySet())
                proj.put(e.getValue(), r.get(e.getKey()));             // rename; absent -> null
            out.add(proj);
        }
        return out;
    }

    /* ============ tiny JSON writer, for asserting exact spec output ============ */
    static String toJson(Object o) {
        if (o == null) return "null";
        if (o instanceof String s) return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        if (o instanceof Boolean || o instanceof Number) return String.valueOf(o);
        if (o instanceof Map<?, ?> m) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> e : m.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append(toJson(String.valueOf(e.getKey()))).append(':').append(toJson(e.getValue()));
            }
            return sb.append('}').toString();
        }
        if (o instanceof Collection<?> c) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object v : c) { if (!first) sb.append(','); first = false; sb.append(toJson(v)); }
            return sb.append(']').toString();
        }
        return toJson(String.valueOf(o));
    }

    /* ======================= self-check on every published example ======================= */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-52s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        System.out.println("--- PART 1  bc8b7bb7  insert + select by column names ---");
        InMemoryDatabase d1 = new InMemoryDatabase();
        d1.createTable("t", List.of("id", "name", "age"));
        d1.insert("t", Map.of("id", "1", "name", "alice", "age", "20"));
        check("select(name,id) == [[alice, 1]]", d1.select("t", List.of("name", "id"), ""), List.of(List.of("alice", "1")));
        check("missing column -> \"\"", d1.select("t", List.of("nope"), ""), List.of(List.of("")));

        System.out.println("--- PART 2  101b79f9  WHERE equality ---");
        InMemoryDatabase d2 = new InMemoryDatabase();
        d2.createTable("t", List.of("id", "name", "city"));
        d2.insert("t", new LinkedHashMap<>(Map.of("id", "1", "name", "a", "city", "sf")));
        d2.insert("t", new LinkedHashMap<>(Map.of("id", "2", "name", "b", "city", "ny")));
        check("select name where city=sf == [[a]]",
              d2.select("t", List.of("name"), new Pred("city", Op.EQ, "sf"), ""), List.of(List.of("a")));
        d2.insert("t", new LinkedHashMap<>(Map.of("id", "3", "name", "c")));   // no city
        check("row missing filter col does not match",
              d2.select("t", List.of("name"), new Pred("city", Op.EQ, "sf"), ""), List.of(List.of("a")));

        System.out.println("--- PART 3  e711c837  ORDER BY ---");
        InMemoryDatabase d3 = new InMemoryDatabase();
        d3.createTable("t", List.of("id", "name", "score"));
        d3.insert("t", new LinkedHashMap<>(Map.of("id", "1", "name", "alice", "score", "90")));
        d3.insert("t", new LinkedHashMap<>(Map.of("id", "2", "name", "bob", "score", "80")));
        check("order_by score asc == [[bob],[alice]]",
              d3.query("t", List.of("name"), null, "score", true, ""),
              List.of(List.of("bob"), List.of("alice")));
        check("order_by score desc == [[alice],[bob]]",
              d3.query("t", List.of("name"), null, "score", false, ""),
              List.of(List.of("alice"), List.of("bob")));
        // stability: equal keys keep insertion order
        InMemoryDatabase st = new InMemoryDatabase();
        st.createTable("t", List.of("k", "n"));
        for (String n : List.of("x", "y", "z")) st.insert("t", new LinkedHashMap<>(Map.of("k", "1", "n", n)));
        check("stable sort keeps insertion order",
              st.query("t", List.of("n"), null, "k", true, ""),
              List.of(List.of("x"), List.of("y"), List.of("z")));
        check("numeric not lexicographic (9 vs 10)",
              cmpDemo(), "9<10");

        System.out.println("--- PART 4  d5d1b167  upsert by id + AND filters ---");
        InMemoryDatabase d4 = new InMemoryDatabase();
        d4.upsert("employees", new LinkedHashMap<>(Map.of("id", "1", "name", "John", "email", "abc@gmail.com")));
        d4.upsert("employees", new LinkedHashMap<>(Map.of("id", "1", "name", "Jessie", "email", "qwe@com")));
        d4.upsert("employees", new LinkedHashMap<>(Map.of("id", "2", "email", "cde@com")));
        check("published example output",
              toJson(d4.selectAnd("employees", List.of("name"), List.of())),
              "[{\"name\":\"Jessie\"},{\"name\":null}]");
        check("replace preserves position", d4.table("employees").rows.get(0).get("name"), "Jessie");
        check("AND: name='Jessie' AND id > 0",
              toJson(d4.selectAnd("employees", List.of("id", "name"), parseAnd("name = 'Jessie' AND id > 0"))),
              "[{\"id\":\"1\",\"name\":\"Jessie\"}]");
        check("predicate col absent -> row excluded",
              toJson(d4.selectAnd("employees", List.of("id"), parseAnd("name = 'Nobody'"))), "[]");

        System.out.println("--- PART 5  709ae770  IN + FILTER BY + * ---");
        InMemoryDatabase d5 = new InMemoryDatabase();
        d5.createTable("users", List.of("id", "name", "age", "city"));
        String[][] rows = {{"1","Alice","30","NY"},{"2","Bob","25","LA"},{"3","Carol","35","NY"},{"4","David","40","SF"}};
        for (String[] r : rows) {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("id", r[0]); m.put("name", r[1]); m.put("age", r[2]); m.put("city", r[3]);
            d5.insert("users", m);
        }
        check("SELECT name", join(d5.runSql("users", "SELECT name FROM users", "")), "Alice;Bob;Carol;David");
        check("WHERE city IN (NY,SF)",
              join(d5.runSql("users", "SELECT id,name FROM users WHERE city IN (NY,SF)", "")),
              "1,Alice;3,Carol;4,David");
        check("IN + FILTER BY age > 30",
              join(d5.runSql("users", "SELECT name FROM users WHERE city IN (NY,LA) FILTER BY age > 30", "")),
              "Carol");
        check("SELECT * expands declared order",
              join(d5.runSql("users", "SELECT * FROM users FILTER BY id = 2", "")), "2,Bob,25,LA");
        check("no match -> EMPTY", join(d5.runSql("users", "SELECT name FROM users FILTER BY age > 99", "")), "EMPTY");

        System.out.println("--- PART 6  85ac94f9  query by column presence ---");
        InMemoryDatabase d6 = new InMemoryDatabase();
        d6.insert("recs", new LinkedHashMap<>(Map.of("id", "1", "name", "alice", "age", "30")));
        d6.insert("recs", new LinkedHashMap<>(Map.of("id", "2", "name", "bob")));
        d6.insert("recs", new LinkedHashMap<>(Map.of("id", "3", "age", "30", "city", "SF")));
        check("QUERY_COL name == [1, 2]", d6.idsWithColumn("recs", "name"), List.of("1", "2"));
        check("QUERY_COL city == [3]", d6.idsWithColumn("recs", "city"), List.of("3"));
        check("target=name filter age=30 == [1]", d6.idsWithColumnFiltered("recs", "name", "age", "30"), List.of("1"));
        check("target=city filter name=alice == []", d6.idsWithColumnFiltered("recs", "city", "name", "alice"), List.of());
        check("unknown column == []", d6.idsWithColumn("recs", "zzz"), List.of());

        System.out.println("--- PART 7  e1de73a3  typed schema + projection rename ---");
        InMemoryDatabase d7 = new InMemoryDatabase();
        Map<String, ColType> schema = new LinkedHashMap<>();
        schema.put("id", ColType.INT); schema.put("name", ColType.STRING);
        schema.put("age", ColType.INT); schema.put("active", ColType.BOOL);
        d7.createTyped("users", schema);
        Map<String, Object> a = new LinkedHashMap<>(); a.put("id", 1L); a.put("name", "Alice"); a.put("active", true);
        Map<String, Object> b = new LinkedHashMap<>(); b.put("id", 2L); b.put("name", "Bob"); b.put("age", 30L); b.put("active", false);
        d7.insertTyped("users", a); d7.insertTyped("users", b);
        Map<String, String> proj = new LinkedHashMap<>();
        proj.put("id", "user_id"); proj.put("name", "display_name"); proj.put("age", "age");
        check("published example output",
              toJson(d7.selectProjected("users", new Pred("active", Op.EQ, "true"), proj)),
              "[{\"user_id\":1,\"display_name\":\"Alice\",\"age\":null}]");
        check("unknown column rejected", err(() -> d7.insertTyped("users", new LinkedHashMap<>(Map.of("nope", "x")))), true);
        check("string in int column rejected", err(() -> d7.insertTyped("users", new LinkedHashMap<>(Map.of("id", "1")))), true);
        check("⭐ BOOL not accepted as INT", err(() -> d7.insertTyped("users", new LinkedHashMap<>(Map.of("id", true)))), true);
        check("checkType(INT, true) == false", checkType(ColType.INT, true), false);
        check("checkType(INT, 5L) == true", checkType(ColType.INT, 5L), true);
        check("unknown projected column rejected",
              err(() -> d7.selectProjected("users", null, Map.of("ghost", "g"))), true);

        System.out.println("--- scale: 100k rows, 20 cols ---");
        InMemoryDatabase big = new InMemoryDatabase();
        List<String> cols = new ArrayList<>();
        for (int i = 0; i < 20; i++) cols.add("c" + i);
        big.createTable("big", cols);
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", String.valueOf(i));
            for (int c = 1; c < 20; c++) if ((i + c) % 3 != 0) m.put("c" + c, "v" + (i % 97));
            big.upsert("big", m);
        }
        long t1 = System.currentTimeMillis();
        int n = big.selectAnd("big", List.of("id", "c1"), parseAnd("c1 = 'v5' AND id > 100")).size();
        long t2 = System.currentTimeMillis();
        System.out.printf("  insert 100k: %d ms | AND-scan: %d ms | %d matches%n", t1 - t0, t2 - t1, n);
        System.out.printf("  colIndex(c1) size = %d (presence index, O(matches) not O(N))%n",
                big.table("big").colIndex.get("c1").size());

        System.out.println("\n===== " + pass + " passed, " + fail + " failed =====");
    }

    private static String cmpDemo() { return compareSmart("9", "10") < 0 ? "9<10" : "9>10"; }

    /** PART 5 output format: columns by ',', rows by ';', EMPTY when nothing matched. */
    static String join(List<List<String>> rows) {
        if (rows.isEmpty()) return "EMPTY";
        List<String> parts = new ArrayList<>();
        for (List<String> r : rows) parts.add(String.join(",", r));
        return String.join(";", parts);
    }
    static boolean err(Runnable r) { try { r.run(); return false; } catch (RuntimeException e) { return true; } }
}
