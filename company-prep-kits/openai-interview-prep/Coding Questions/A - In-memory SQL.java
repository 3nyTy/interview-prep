/*
 * In-Memory Database with SQL Operations — OpenAI Staff SWE coding question.
 *
 * MULTI-PART problem (6 parts), built incrementally. We deliberately do NOT
 * parse SQL strings; instead we define our own programmatic input format using
 * plain maps/lists (the interviewer explicitly recommends this).
 *
 * Rows are modeled as LinkedHashMap<String,String> because:
 *   - all example values are strings, and
 *   - LinkedHashMap preserves insertion order, which is exactly what we want
 *     for column-projection order (we insert requested columns in order) and
 *     gives us a deterministic, order-sensitive toString() for test assertions.
 *
 * ------------------------------------------------------------------------
 * SELF-CONTAINMENT CONTRACT (important for this codebase's convention)
 * ------------------------------------------------------------------------
 * Each part is a fully self-contained `static class PartN` with its OWN copy of
 * the Database class, its own helper types/methods, and its own
 * `public static void main`. No PartN references any other PartM and there are
 * NO shared top-level helpers. Duplication across parts is INTENTIONAL: copying
 * a single PartN body into its own .java file (promote `static class PartN` to
 * `class PartN`, drop `static`) must compile and run unchanged.
 *
 * The top-level class `InMemorySql` is package-private (NOT public), so the
 * filename ("A - In-memory SQL.java") need not match the class name. Its only
 * job is to dispatch to PartN by args[0] (accepts "1".."6" and "part1".."part6")
 * and to run ALL parts in order when no argument is given.
 *
 * ------------------------------------------------------------------------
 * PER-PART APPROACH SUMMARY
 * ------------------------------------------------------------------------
 *   Part 1: create_table / insert / query(table, columns). query does column
 *           projection: returns every row but only the requested columns, in
 *           the requested order. Missing projection columns are silently
 *           omitted; missing table throws IllegalArgumentException.
 *   Part 2: query gains an optional WHERE predicate (Predicate<Map>). null/empty
 *           WHERE = all rows (backward compatible with Part 1).
 *   Part 3: WHERE multiple conditions with AND. TWO overloads — a predicate
 *           lambda AND a tuple-list form List<Condition(col, op, value)> with
 *           operators = > < >= <= !=. Comparison uses numeric coercion: if both
 *           sides parse as doubles, compare numerically; else compare as strings.
 *   Part 4: ORDER BY a single column, ascending. Implemented with the extensible
 *           List<String> form so Part 5 needs no refactor.
 *   Part 5: ORDER BY multiple columns with a single direction (asc/desc) via an
 *           OrderBy(columns, ascending) record. STABLE sort gives deterministic
 *           tie-breaking (insertion order preserved among equal keys).
 *   Part 6: Indexing DISCUSSION ONLY (no asserts). Prints talking points about
 *           inverted indexes, B-tree/sorted indexes, composite indexes, index
 *           maintenance on INSERT, and query-optimization ordering, plus a small
 *           conceptual createIndex sketch.
 *
 * ------------------------------------------------------------------------
 * QUERY PIPELINE (every part that has a query implements this order)
 * ------------------------------------------------------------------------
 *   (1) table-existence check  -> throw IllegalArgumentException if missing
 *   (2) copy the stored row list (we never mutate stored data)
 *   (3) WHERE filter (null/empty -> all rows pass)
 *   (4) ORDER BY stable sort (if present)
 *   (5) projection: build a NEW LinkedHashMap per row, inserting requested
 *       columns in order, only when the row actually contains the column.
 *
 * insert() defensively copies the incoming row, and query() builds brand-new
 * projected maps, so stored rows are never mutated by callers or by reads.
 *
 * ------------------------------------------------------------------------
 * COMPLEXITY
 * ------------------------------------------------------------------------
 *   create_table : O(1)        insert : O(c) for the defensive copy (c columns)
 *   query filter + project : O(n * c)            sort : O(n log n * k) for k cols
 *   result space : O(n * c)
 *   Part 6 note: an inverted index turns an equality WHERE from O(n) into
 *   O(1) + O(result); a sorted/B-tree index gives O(log n + result) for ranges.
 *
 * ------------------------------------------------------------------------
 * COMPILE / RUN
 * ------------------------------------------------------------------------
 *   javac -d /tmp/out "A - In-memory SQL.java"
 *   java -cp /tmp/out InMemorySql           # runs all parts (default)
 *   java -cp /tmp/out InMemorySql 3         # runs only Part3
 *   java -cp /tmp/out InMemorySql part5     # runs only Part5
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Top-level dispatcher. Package-private (NOT public) so the filename need not
 * match the class name. Contains ONLY the dispatcher and the six self-contained
 * PartN classes — no shared helpers/types live here.
 */
class InMemorySql {

    public static void main(String[] args) {
        // Normalize the selector: accept "1".."6" and "part1".."part6".
        String arg = args.length == 0 ? null : args[0].trim().toLowerCase();
        if (arg != null && arg.startsWith("part")) {
            arg = arg.substring("part".length()).trim();
        }

        if (arg == null || arg.isBlank()) {
            // No argument -> run every part in order, each with empty args.
            Part1.main(new String[0]);
            Part2.main(new String[0]);
            Part3.main(new String[0]);
            Part4.main(new String[0]);
            Part5.main(new String[0]);
            Part6.main(new String[0]);
            return;
        }

        switch (arg) {
            case "1" -> Part1.main(new String[0]);
            case "2" -> Part2.main(new String[0]);
            case "3" -> Part3.main(new String[0]);
            case "4" -> Part4.main(new String[0]);
            case "5" -> Part5.main(new String[0]);
            case "6" -> Part6.main(new String[0]);
            default -> System.err.println(
                    "Usage: java InMemorySql [1-6|part1-part6]  (no arg = run all)");
        }
    }

    // ====================================================================
    // PART 1 — Basic table operations and column-projection queries.
    // ====================================================================
    static final class Part1 {

        /**
         * In-memory database. Self-contained for Part 1: create_table, insert,
         * and a projection-only query.
         */
        private static final class Database {
            // table name -> rows (each row preserves column insertion order).
            private final Map<String, List<LinkedHashMap<String, String>>> tables = new HashMap<>();
            // table name -> declared column names (kept for reference / schema).
            private final Map<String, List<String>> schemas = new HashMap<>();

            void createTable(String name, List<String> columns) {
                schemas.put(name, new ArrayList<>(columns));
                tables.put(name, new ArrayList<>());
            }

            void insert(String name, Map<String, String> row) {
                if (!tables.containsKey(name)) {
                    throw new IllegalArgumentException("Table '" + name + "' does not exist");
                }
                // Defensive copy so later caller mutations cannot corrupt storage.
                tables.get(name).add(new LinkedHashMap<>(row));
            }

            /**
             * Projection query: return all rows but only the requested columns,
             * in the requested order. Missing columns are silently omitted.
             */
            List<LinkedHashMap<String, String>> query(String table, List<String> columns) {
                if (!tables.containsKey(table)) {
                    throw new IllegalArgumentException("Table '" + table + "' does not exist");
                }
                List<LinkedHashMap<String, String>> result = new ArrayList<>();
                for (LinkedHashMap<String, String> row : tables.get(table)) {
                    LinkedHashMap<String, String> projected = new LinkedHashMap<>();
                    for (String col : columns) {
                        if (row.containsKey(col)) {       // silently omit missing columns
                            projected.put(col, row.get(col));
                        }
                    }
                    result.add(projected);
                }
                return result;
            }
        }

        // ---- test helpers (self-contained) --------------------------------

        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError("FAIL: " + msg);
            }
            System.out.println("  PASS: " + msg);
        }

        /** Order-sensitive comparison of a result list against expected rows. */
        private static void expectRows(List<LinkedHashMap<String, String>> actual,
                                       List<LinkedHashMap<String, String>> expected,
                                       String label) {
            System.out.println("  " + label + " -> " + actual);
            check(actual.size() == expected.size(),
                    label + " size " + actual.size() + " == " + expected.size());
            for (int i = 0; i < expected.size(); i++) {
                // LinkedHashMap.toString() is insertion-ordered, so this also
                // verifies projection (column) order, not just key/value pairs.
                check(actual.get(i).toString().equals(expected.get(i).toString()),
                        label + " row[" + i + "] " + actual.get(i) + " == " + expected.get(i));
            }
        }

        /** Tiny ordered-map literal builder, e.g. row("id","1","name","Alice"). */
        private static LinkedHashMap<String, String> row(String... kv) {
            LinkedHashMap<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < kv.length; i += 2) {
                m.put(kv[i], kv[i + 1]);
            }
            return m;
        }

        public static void main(String[] args) {
            System.out.println("== Part 1: create_table / insert / projection query ==");
            Database db = new Database();
            db.createTable("users", List.of("id", "name", "birthday"));
            db.insert("users", row("id", "1", "name", "Alice", "birthday", "1990-05-15"));
            db.insert("users", row("id", "2", "name", "Bob", "birthday", "1985-08-20"));
            db.insert("users", row("id", "3", "name", "Charlie", "birthday", "1992-03-10"));

            // Project id + name.
            expectRows(db.query("users", List.of("id", "name")),
                    List.of(row("id", "1", "name", "Alice"),
                            row("id", "2", "name", "Bob"),
                            row("id", "3", "name", "Charlie")),
                    "query([id,name])");

            // Project name only.
            expectRows(db.query("users", List.of("name")),
                    List.of(row("name", "Alice"), row("name", "Bob"), row("name", "Charlie")),
                    "query([name])");

            // A non-existent projection column is silently omitted.
            expectRows(db.query("users", List.of("name", "missing")),
                    List.of(row("name", "Alice"), row("name", "Bob"), row("name", "Charlie")),
                    "query([name,missing]) omits missing");

            // Empty table -> empty result.
            db.createTable("empty", List.of("a", "b"));
            expectRows(db.query("empty", List.of("a", "b")),
                    new ArrayList<>(), "query(empty table)");

            // Missing table -> IllegalArgumentException.
            boolean threw = false;
            try {
                db.query("nope", List.of("a"));
            } catch (IllegalArgumentException e) {
                threw = true;
            }
            check(threw, "query(missing table) throws IllegalArgumentException");

            System.out.println("Part 1: all checks passed.\n");
        }
    }

    // ====================================================================
    // PART 2 — WHERE clause, single condition (predicate lambda).
    //
    // CHANGES FROM PART 1:
    //   - query() gains an optional WHERE parameter: Predicate<Map<String,String>>.
    //   - A null predicate means "no filter", so it is fully backward compatible
    //     with Part 1 (all rows pass).
    // ====================================================================
    static final class Part2 {

        private static final class Database {
            private final Map<String, List<LinkedHashMap<String, String>>> tables = new HashMap<>();
            private final Map<String, List<String>> schemas = new HashMap<>();

            void createTable(String name, List<String> columns) {
                schemas.put(name, new ArrayList<>(columns));
                tables.put(name, new ArrayList<>());
            }

            void insert(String name, Map<String, String> row) {
                if (!tables.containsKey(name)) {
                    throw new IllegalArgumentException("Table '" + name + "' does not exist");
                }
                tables.get(name).add(new LinkedHashMap<>(row));
            }

            /** Projection query with an optional WHERE predicate (null = all rows). */
            List<LinkedHashMap<String, String>> query(String table, List<String> columns,
                                                       Predicate<Map<String, String>> where) {
                if (!tables.containsKey(table)) {
                    throw new IllegalArgumentException("Table '" + table + "' does not exist");
                }
                List<LinkedHashMap<String, String>> rows = new ArrayList<>(tables.get(table));

                // (3) WHERE filter.
                List<LinkedHashMap<String, String>> filtered = new ArrayList<>();
                for (LinkedHashMap<String, String> r : rows) {
                    if (where == null || where.test(r)) {
                        filtered.add(r);
                    }
                }

                // (5) projection.
                List<LinkedHashMap<String, String>> result = new ArrayList<>();
                for (LinkedHashMap<String, String> r : filtered) {
                    LinkedHashMap<String, String> projected = new LinkedHashMap<>();
                    for (String col : columns) {
                        if (r.containsKey(col)) {
                            projected.put(col, r.get(col));
                        }
                    }
                    result.add(projected);
                }
                return result;
            }
        }

        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError("FAIL: " + msg);
            }
            System.out.println("  PASS: " + msg);
        }

        private static void expectRows(List<LinkedHashMap<String, String>> actual,
                                       List<LinkedHashMap<String, String>> expected,
                                       String label) {
            System.out.println("  " + label + " -> " + actual);
            check(actual.size() == expected.size(),
                    label + " size " + actual.size() + " == " + expected.size());
            for (int i = 0; i < expected.size(); i++) {
                check(actual.get(i).toString().equals(expected.get(i).toString()),
                        label + " row[" + i + "] " + actual.get(i) + " == " + expected.get(i));
            }
        }

        private static LinkedHashMap<String, String> row(String... kv) {
            LinkedHashMap<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < kv.length; i += 2) {
                m.put(kv[i], kv[i + 1]);
            }
            return m;
        }

        public static void main(String[] args) {
            System.out.println("== Part 2: WHERE single condition (predicate) ==");
            Database db = new Database();
            db.createTable("users", List.of("id", "name", "birthday"));
            db.insert("users", row("id", "1", "name", "Alice", "birthday", "1990-05-15"));
            db.insert("users", row("id", "2", "name", "Bob", "birthday", "1985-08-20"));
            db.insert("users", row("id", "3", "name", "Charlie", "birthday", "1992-03-10"));

            // birthday > "1990-01-01" (string comparison on ISO dates is correct).
            expectRows(db.query("users", List.of("name", "birthday"),
                            r -> r.get("birthday").compareTo("1990-01-01") > 0),
                    List.of(row("name", "Alice", "birthday", "1990-05-15"),
                            row("name", "Charlie", "birthday", "1992-03-10")),
                    "where birthday>1990-01-01");

            // int(id) > 1.
            expectRows(db.query("users", List.of("id", "name"),
                            r -> Integer.parseInt(r.get("id")) > 1),
                    List.of(row("id", "2", "name", "Bob"), row("id", "3", "name", "Charlie")),
                    "where int(id)>1");

            // where = null -> all rows (backward compatible with Part 1).
            expectRows(db.query("users", List.of("id", "name"), null),
                    List.of(row("id", "1", "name", "Alice"),
                            row("id", "2", "name", "Bob"),
                            row("id", "3", "name", "Charlie")),
                    "where null = all rows");

            System.out.println("Part 2: all checks passed.\n");
        }
    }

    // ====================================================================
    // PART 3 — WHERE with multiple conditions (AND). Two overloads.
    //
    // CHANGES FROM PART 2:
    //   - Keep the predicate overload (now naturally supports compound boolean
    //     expressions, since a single lambda can AND several conditions).
    //   - ADD a tuple-list overload: query(table, columns, List<Condition>),
    //     where Condition is (column, op, value) and all conditions are AND-ed.
    //     Operators: "=", "!=", ">", "<", ">=", "<=".
    //   - Comparisons use numeric coercion via compareCoerced: if BOTH sides
    //     parse as doubles, compare numerically; otherwise compare as strings.
    // ====================================================================
    static final class Part3 {

        /** A single WHERE condition for the tuple-list API. */
        private record Condition(String column, String op, String value) { }

        private static final class Database {
            private final Map<String, List<LinkedHashMap<String, String>>> tables = new HashMap<>();
            private final Map<String, List<String>> schemas = new HashMap<>();

            void createTable(String name, List<String> columns) {
                schemas.put(name, new ArrayList<>(columns));
                tables.put(name, new ArrayList<>());
            }

            void insert(String name, Map<String, String> row) {
                if (!tables.containsKey(name)) {
                    throw new IllegalArgumentException("Table '" + name + "' does not exist");
                }
                tables.get(name).add(new LinkedHashMap<>(row));
            }

            /** Overload 1: predicate WHERE (null = all rows). */
            List<LinkedHashMap<String, String>> query(String table, List<String> columns,
                                                       Predicate<Map<String, String>> where) {
                if (!tables.containsKey(table)) {
                    throw new IllegalArgumentException("Table '" + table + "' does not exist");
                }
                List<LinkedHashMap<String, String>> rows = new ArrayList<>(tables.get(table));
                List<LinkedHashMap<String, String>> filtered = new ArrayList<>();
                for (LinkedHashMap<String, String> r : rows) {
                    if (where == null || where.test(r)) {
                        filtered.add(r);
                    }
                }
                return project(filtered, columns);
            }

            /** Overload 2: tuple-list WHERE with AND semantics (null/empty = all rows). */
            List<LinkedHashMap<String, String>> query(String table, List<String> columns,
                                                       List<Condition> where) {
                if (!tables.containsKey(table)) {
                    throw new IllegalArgumentException("Table '" + table + "' does not exist");
                }
                List<LinkedHashMap<String, String>> rows = new ArrayList<>(tables.get(table));
                List<LinkedHashMap<String, String>> filtered = new ArrayList<>();
                for (LinkedHashMap<String, String> r : rows) {
                    if (where == null || matchesAll(r, where)) {
                        filtered.add(r);
                    }
                }
                return project(filtered, columns);
            }

            // ---- projection shared by both overloads (private to this Database) --
            private List<LinkedHashMap<String, String>> project(
                    List<LinkedHashMap<String, String>> rows, List<String> columns) {
                List<LinkedHashMap<String, String>> result = new ArrayList<>();
                for (LinkedHashMap<String, String> r : rows) {
                    LinkedHashMap<String, String> projected = new LinkedHashMap<>();
                    for (String col : columns) {
                        if (r.containsKey(col)) {
                            projected.put(col, r.get(col));
                        }
                    }
                    result.add(projected);
                }
                return result;
            }
        }

        /** AND semantics across all conditions (empty list -> true / all rows). */
        private static boolean matchesAll(Map<String, String> row, List<Condition> conds) {
            for (Condition c : conds) {
                if (!evaluate(row, c)) {
                    return false;
                }
            }
            return true;
        }

        /** Evaluate one condition; a missing column makes the condition false. */
        private static boolean evaluate(Map<String, String> row, Condition c) {
            if (!row.containsKey(c.column())) {
                return false;
            }
            int cmp = compareCoerced(row.get(c.column()), c.value());
            return switch (c.op()) {
                case "=" -> cmp == 0;
                case "!=" -> cmp != 0;
                case ">" -> cmp > 0;
                case "<" -> cmp < 0;
                case ">=" -> cmp >= 0;
                case "<=" -> cmp <= 0;
                default -> throw new IllegalArgumentException("Unsupported operator: " + c.op());
            };
        }

        /** Numeric if both parse as doubles, else lexicographic string compare. */
        private static int compareCoerced(String left, String right) {
            try {
                return Double.compare(Double.parseDouble(left), Double.parseDouble(right));
            } catch (NumberFormatException e) {
                return left.compareTo(right);
            }
        }

        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError("FAIL: " + msg);
            }
            System.out.println("  PASS: " + msg);
        }

        private static void expectRows(List<LinkedHashMap<String, String>> actual,
                                       List<LinkedHashMap<String, String>> expected,
                                       String label) {
            System.out.println("  " + label + " -> " + actual);
            check(actual.size() == expected.size(),
                    label + " size " + actual.size() + " == " + expected.size());
            for (int i = 0; i < expected.size(); i++) {
                check(actual.get(i).toString().equals(expected.get(i).toString()),
                        label + " row[" + i + "] " + actual.get(i) + " == " + expected.get(i));
            }
        }

        private static LinkedHashMap<String, String> row(String... kv) {
            LinkedHashMap<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < kv.length; i += 2) {
                m.put(kv[i], kv[i + 1]);
            }
            return m;
        }

        public static void main(String[] args) {
            System.out.println("== Part 3: WHERE multiple conditions (AND) ==");
            Database db = new Database();
            db.createTable("users", List.of("id", "name", "birthday"));
            db.insert("users", row("id", "1", "name", "Alice", "birthday", "1990-05-15"));
            db.insert("users", row("id", "2", "name", "Bob", "birthday", "1985-08-20"));
            db.insert("users", row("id", "3", "name", "Charlie", "birthday", "1992-03-10"));

            // Predicate form: int(id) > 1 AND name.startsWith("C").
            expectRows(db.query("users", List.of("id", "name"),
                            (Predicate<Map<String, String>>)
                                    r -> Integer.parseInt(r.get("id")) > 1
                                            && r.get("name").startsWith("C")),
                    List.of(row("id", "3", "name", "Charlie")),
                    "predicate int(id)>1 && name startsWith C");

            // Tuple-list form: id > 1 AND name = "Charlie" (id is numeric-coerced).
            expectRows(db.query("users", List.of("id", "name"),
                            List.of(new Condition("id", ">", "1"),
                                    new Condition("name", "=", "Charlie"))),
                    List.of(row("id", "3", "name", "Charlie")),
                    "tuple-list id>1 AND name=Charlie");

            // Empty condition list -> all rows.
            expectRows(db.query("users", List.of("id", "name"), List.<Condition>of()),
                    List.of(row("id", "1", "name", "Alice"),
                            row("id", "2", "name", "Bob"),
                            row("id", "3", "name", "Charlie")),
                    "empty condition list = all rows");

            System.out.println("Part 3: all checks passed.\n");
        }
    }

    // ====================================================================
    // PART 4 — ORDER BY a single column, ascending.
    //
    // CHANGES FROM PART 3:
    //   - query() gains an ORDER BY parameter, expressed as the EXTENSIBLE form
    //     List<String> (ascending). Using a list now means Part 5 only has to
    //     add a direction flag, with no signature churn.
    //   - For sorting we reuse the SAME compareCoerced rule per column, so a
    //     numeric column sorts numerically and a string column lexicographically.
    //     (The Python reference sorts raw strings; per this part's
    //     numeric-sort note we coerce. Results are identical for the examples.)
    //   - The sort is STABLE (List.sort uses TimSort), so equal keys keep their
    //     original insertion order.
    //   - We keep a single predicate WHERE overload here (null = all rows).
    // ====================================================================
    static final class Part4 {

        private static final class Database {
            private final Map<String, List<LinkedHashMap<String, String>>> tables = new HashMap<>();
            private final Map<String, List<String>> schemas = new HashMap<>();

            void createTable(String name, List<String> columns) {
                schemas.put(name, new ArrayList<>(columns));
                tables.put(name, new ArrayList<>());
            }

            void insert(String name, Map<String, String> row) {
                if (!tables.containsKey(name)) {
                    throw new IllegalArgumentException("Table '" + name + "' does not exist");
                }
                tables.get(name).add(new LinkedHashMap<>(row));
            }

            /**
             * Query with optional WHERE predicate (null = all rows) and optional
             * ORDER BY (ascending) given as a list of columns (null = no sort).
             */
            List<LinkedHashMap<String, String>> query(String table, List<String> columns,
                                                       Predicate<Map<String, String>> where,
                                                       List<String> orderByColumnsAsc) {
                if (!tables.containsKey(table)) {
                    throw new IllegalArgumentException("Table '" + table + "' does not exist");
                }
                // (2) copy stored rows so we never reorder/mutate the storage.
                List<LinkedHashMap<String, String>> rows = new ArrayList<>(tables.get(table));

                // (3) WHERE filter.
                List<LinkedHashMap<String, String>> filtered = new ArrayList<>();
                for (LinkedHashMap<String, String> r : rows) {
                    if (where == null || where.test(r)) {
                        filtered.add(r);
                    }
                }

                // (4) ORDER BY ascending, stable.
                if (orderByColumnsAsc != null && !orderByColumnsAsc.isEmpty()) {
                    Comparator<Map<String, String>> cmp = null;
                    for (String col : orderByColumnsAsc) {
                        Comparator<Map<String, String>> c =
                                (a, b) -> compareCoerced(a.get(col), b.get(col));
                        cmp = (cmp == null) ? c : cmp.thenComparing(c);
                    }
                    filtered.sort(cmp); // List.sort is a STABLE sort (TimSort)
                }

                // (5) projection.
                return project(filtered, columns);
            }

            /** Convenience overload: single sort column (ascending). */
            List<LinkedHashMap<String, String>> query(String table, List<String> columns,
                                                       Predicate<Map<String, String>> where,
                                                       String orderByColumnAsc) {
                return query(table, columns, where,
                        orderByColumnAsc == null ? null : List.of(orderByColumnAsc));
            }

            private List<LinkedHashMap<String, String>> project(
                    List<LinkedHashMap<String, String>> rows, List<String> columns) {
                List<LinkedHashMap<String, String>> result = new ArrayList<>();
                for (LinkedHashMap<String, String> r : rows) {
                    LinkedHashMap<String, String> projected = new LinkedHashMap<>();
                    for (String col : columns) {
                        if (r.containsKey(col)) {
                            projected.put(col, r.get(col));
                        }
                    }
                    result.add(projected);
                }
                return result;
            }
        }

        /** Numeric if both parse as doubles, else lexicographic string compare. */
        private static int compareCoerced(String left, String right) {
            try {
                return Double.compare(Double.parseDouble(left), Double.parseDouble(right));
            } catch (NumberFormatException e) {
                return left.compareTo(right);
            }
        }

        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError("FAIL: " + msg);
            }
            System.out.println("  PASS: " + msg);
        }

        private static void expectRows(List<LinkedHashMap<String, String>> actual,
                                       List<LinkedHashMap<String, String>> expected,
                                       String label) {
            System.out.println("  " + label + " -> " + actual);
            check(actual.size() == expected.size(),
                    label + " size " + actual.size() + " == " + expected.size());
            for (int i = 0; i < expected.size(); i++) {
                check(actual.get(i).toString().equals(expected.get(i).toString()),
                        label + " row[" + i + "] " + actual.get(i) + " == " + expected.get(i));
            }
        }

        private static LinkedHashMap<String, String> row(String... kv) {
            LinkedHashMap<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < kv.length; i += 2) {
                m.put(kv[i], kv[i + 1]);
            }
            return m;
        }

        public static void main(String[] args) {
            System.out.println("== Part 4: ORDER BY single column ascending ==");
            Database db = new Database();
            db.createTable("users", List.of("id", "name", "age"));
            db.insert("users", row("id", "1", "name", "Alice", "age", "30"));
            db.insert("users", row("id", "2", "name", "Bob", "age", "25"));
            db.insert("users", row("id", "3", "name", "Charlie", "age", "35"));

            // ORDER BY name ASC, no WHERE.
            expectRows(db.query("users", List.of("name", "age"), null, List.of("name")),
                    List.of(row("name", "Alice", "age", "30"),
                            row("name", "Bob", "age", "25"),
                            row("name", "Charlie", "age", "35")),
                    "order by [name] asc");

            // WHERE age>28 then ORDER BY name ASC.
            expectRows(db.query("users", List.of("name", "age"),
                            r -> Integer.parseInt(r.get("age")) > 28, List.of("name")),
                    List.of(row("name", "Alice", "age", "30"),
                            row("name", "Charlie", "age", "35")),
                    "where age>28 order by [name] asc");

            System.out.println("Part 4: all checks passed.\n");
        }
    }

    // ====================================================================
    // PART 5 — ORDER BY multiple columns with a single direction (asc/desc).
    //
    // CHANGES FROM PART 4:
    //   - Replace the List<String> ascending order parameter with an OrderBy
    //     record (List<String> columns, boolean ascending). All columns share
    //     one direction (the simplified API the question asks for).
    //   - To go descending we build the ascending comparator (chained over the
    //     columns) and then call .reversed(). The sort stays STABLE, so equal
    //     keys keep insertion order in BOTH directions — that's the stability
    //     the comprehensive example relies on (id 1 before id 5).
    //   - WHERE remains a predicate overload (null = all rows).
    // ====================================================================
    static final class Part5 {

        /** ORDER BY spec: a list of columns and one shared direction. */
        private record OrderBy(List<String> columns, boolean ascending) { }

        private static final class Database {
            private final Map<String, List<LinkedHashMap<String, String>>> tables = new HashMap<>();
            private final Map<String, List<String>> schemas = new HashMap<>();

            void createTable(String name, List<String> columns) {
                schemas.put(name, new ArrayList<>(columns));
                tables.put(name, new ArrayList<>());
            }

            void insert(String name, Map<String, String> row) {
                if (!tables.containsKey(name)) {
                    throw new IllegalArgumentException("Table '" + name + "' does not exist");
                }
                tables.get(name).add(new LinkedHashMap<>(row));
            }

            /**
             * Query with optional WHERE predicate (null = all rows) and optional
             * multi-column ORDER BY with a single direction (null = no sort).
             */
            List<LinkedHashMap<String, String>> query(String table, List<String> columns,
                                                       Predicate<Map<String, String>> where,
                                                       OrderBy orderBy) {
                if (!tables.containsKey(table)) {
                    throw new IllegalArgumentException("Table '" + table + "' does not exist");
                }
                List<LinkedHashMap<String, String>> rows = new ArrayList<>(tables.get(table));

                // (3) WHERE filter.
                List<LinkedHashMap<String, String>> filtered = new ArrayList<>();
                for (LinkedHashMap<String, String> r : rows) {
                    if (where == null || where.test(r)) {
                        filtered.add(r);
                    }
                }

                // (4) ORDER BY (stable). Build ascending comparator, reverse if desc.
                if (orderBy != null && !orderBy.columns().isEmpty()) {
                    Comparator<Map<String, String>> cmp = null;
                    for (String col : orderBy.columns()) {
                        Comparator<Map<String, String>> c =
                                (a, b) -> compareCoerced(a.get(col), b.get(col));
                        cmp = (cmp == null) ? c : cmp.thenComparing(c);
                    }
                    if (!orderBy.ascending()) {
                        cmp = cmp.reversed();
                    }
                    filtered.sort(cmp); // STABLE sort preserves insertion order on ties
                }

                // (5) projection.
                List<LinkedHashMap<String, String>> result = new ArrayList<>();
                for (LinkedHashMap<String, String> r : filtered) {
                    LinkedHashMap<String, String> projected = new LinkedHashMap<>();
                    for (String col : columns) {
                        if (r.containsKey(col)) {
                            projected.put(col, r.get(col));
                        }
                    }
                    result.add(projected);
                }
                return result;
            }
        }

        /** Numeric if both parse as doubles, else lexicographic string compare. */
        private static int compareCoerced(String left, String right) {
            try {
                return Double.compare(Double.parseDouble(left), Double.parseDouble(right));
            } catch (NumberFormatException e) {
                return left.compareTo(right);
            }
        }

        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError("FAIL: " + msg);
            }
            System.out.println("  PASS: " + msg);
        }

        private static void expectRows(List<LinkedHashMap<String, String>> actual,
                                       List<LinkedHashMap<String, String>> expected,
                                       String label) {
            System.out.println("  " + label + " -> " + actual);
            check(actual.size() == expected.size(),
                    label + " size " + actual.size() + " == " + expected.size());
            for (int i = 0; i < expected.size(); i++) {
                check(actual.get(i).toString().equals(expected.get(i).toString()),
                        label + " row[" + i + "] " + actual.get(i) + " == " + expected.get(i));
            }
        }

        private static LinkedHashMap<String, String> row(String... kv) {
            LinkedHashMap<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < kv.length; i += 2) {
                m.put(kv[i], kv[i + 1]);
            }
            return m;
        }

        public static void main(String[] args) {
            System.out.println("== Part 5: ORDER BY multiple columns with direction ==");
            Database db = new Database();
            db.createTable("users", List.of("id", "name", "birthday"));
            db.insert("users", row("id", "1", "name", "Ada", "birthday", "1815-12-10"));
            db.insert("users", row("id", "2", "name", "Charles", "birthday", "1791-12-26"));
            db.insert("users", row("id", "3", "name", "Charles", "birthday", "1903-06-23"));

            // ORDER BY (name, birthday) DESC -> ids [3, 2, 1].
            expectRows(db.query("users", List.of("id", "name", "birthday"), null,
                            new OrderBy(List.of("name", "birthday"), false)),
                    List.of(row("id", "3", "name", "Charles", "birthday", "1903-06-23"),
                            row("id", "2", "name", "Charles", "birthday", "1791-12-26"),
                            row("id", "1", "name", "Ada", "birthday", "1815-12-10")),
                    "order_by ([name,birthday], desc) -> ids [3,2,1]");

            // WHERE name=="Charles" ORDER BY birthday DESC -> ids [3, 2].
            expectRows(db.query("users", List.of("id"),
                            r -> r.get("name").equals("Charles"),
                            new OrderBy(List.of("birthday"), false)),
                    List.of(row("id", "3"), row("id", "2")),
                    "where name=Charles order_by ([birthday], desc) -> ids [3,2]");

            // ----------------------------------------------------------------
            // COMPREHENSIVE END-TO-END SCENARIO (mirrors the question's full
            // output block). Fresh Database so it is independent of the above.
            // NOTE: Part 5 has no tuple-list overload, so the "id>1 AND
            // name=Charlie" case is reproduced as an equivalent predicate.
            // ----------------------------------------------------------------
            System.out.println("  -- comprehensive end-to-end scenario --");
            Database c = new Database();
            c.createTable("users", List.of("id", "name", "age", "birthday"));
            c.insert("users", row("id", "1", "name", "Alice", "age", "30", "birthday", "1990-05-15"));
            c.insert("users", row("id", "2", "name", "Bob", "age", "25", "birthday", "1985-08-20"));
            c.insert("users", row("id", "3", "name", "Charlie", "age", "35", "birthday", "1992-03-10"));
            c.insert("users", row("id", "4", "name", "Diana", "age", "28", "birthday", "1995-12-25"));

            // query(["id","name"]) -> all four in insertion order.
            expectRows(c.query("users", List.of("id", "name"), null, null),
                    List.of(row("id", "1", "name", "Alice"),
                            row("id", "2", "name", "Bob"),
                            row("id", "3", "name", "Charlie"),
                            row("id", "4", "name", "Diana")),
                    "comprehensive query([id,name])");

            // age > 28.
            expectRows(c.query("users", List.of("name", "age"),
                            r -> Integer.parseInt(r.get("age")) > 28, null),
                    List.of(row("name", "Alice", "age", "30"),
                            row("name", "Charlie", "age", "35")),
                    "comprehensive age>28");

            // age > 28 AND name[0] in {A, C}.
            expectRows(c.query("users", List.of("name", "age"),
                            r -> Integer.parseInt(r.get("age")) > 28
                                    && "AC".indexOf(r.get("name").charAt(0)) >= 0, null),
                    List.of(row("name", "Alice", "age", "30"),
                            row("name", "Charlie", "age", "35")),
                    "comprehensive age>28 && name[0] in {A,C}");

            // id > 1 AND name == "Charlie" (tuple-list reproduced as predicate).
            expectRows(c.query("users", List.of("id", "name"),
                            r -> Integer.parseInt(r.get("id")) > 1
                                    && r.get("name").equals("Charlie"), null),
                    List.of(row("id", "3", "name", "Charlie")),
                    "comprehensive id>1 && name=Charlie");

            // ORDER BY name ASC.
            expectRows(c.query("users", List.of("name", "age"), null,
                            new OrderBy(List.of("name"), true)),
                    List.of(row("name", "Alice", "age", "30"),
                            row("name", "Bob", "age", "25"),
                            row("name", "Charlie", "age", "35"),
                            row("name", "Diana", "age", "28")),
                    "comprehensive order_by ([name], asc)");

            // Insert a 5th row, then ORDER BY (age, name) DESC.
            c.insert("users", row("id", "5", "name", "Alice", "age", "30", "birthday", "1993-01-01"));
            expectRows(c.query("users", List.of("name", "age"), null,
                            new OrderBy(List.of("age", "name"), false)),
                    List.of(row("name", "Charlie", "age", "35"),
                            row("name", "Alice", "age", "30"),
                            row("name", "Alice", "age", "30"),
                            row("name", "Diana", "age", "28"),
                            row("name", "Bob", "age", "25")),
                    "comprehensive order_by ([age,name], desc)");

            // age >= 28 ORDER BY name ASC -> id 1 before id 5 proves STABILITY.
            expectRows(c.query("users", List.of("id", "name"),
                            r -> Integer.parseInt(r.get("age")) >= 28,
                            new OrderBy(List.of("name"), true)),
                    List.of(row("id", "1", "name", "Alice"),
                            row("id", "5", "name", "Alice"),
                            row("id", "3", "name", "Charlie"),
                            row("id", "4", "name", "Diana")),
                    "comprehensive age>=28 order_by ([name], asc) [stable: id1 before id5]");

            System.out.println("Part 5: all checks passed.\n");
        }
    }

    // ====================================================================
    // PART 6 — Indexing DISCUSSION ONLY (no asserts).
    //
    // CHANGES FROM PART 5:
    //   - No query changes. This part is a discussion of how indexing would
    //     accelerate queries, plus a conceptual createIndex sketch (NOT used by
    //     the query path and NOT asserted). The Database here is minimal: just
    //     enough to demonstrate building an inverted index.
    // ====================================================================
    static final class Part6 {

        /** Minimal store with a conceptual inverted-index builder (not wired to query). */
        private static final class Database {
            private final Map<String, List<LinkedHashMap<String, String>>> tables = new HashMap<>();
            // table -> column -> value -> list of row indices.
            private final Map<String, Map<String, Map<String, List<Integer>>>> indexes = new HashMap<>();

            void createTable(String name, List<String> columns) {
                tables.put(name, new ArrayList<>());
            }

            void insert(String name, Map<String, String> row) {
                if (!tables.containsKey(name)) {
                    throw new IllegalArgumentException("Table '" + name + "' does not exist");
                }
                tables.get(name).add(new LinkedHashMap<>(row));
            }

            /**
             * Conceptual inverted index on one column:
             *   {value -> [rowIndex, ...]}
             * Built by a single O(n) scan over the table. With this in place an
             * equality WHERE on the column is O(1) lookup + O(result) to read the
             * matching rows, versus an O(n) full scan without it.
             */
            void createIndex(String table, String column) {
                if (!tables.containsKey(table)) {
                    throw new IllegalArgumentException("Table '" + table + "' does not exist");
                }
                Map<String, List<Integer>> index = new HashMap<>();
                List<LinkedHashMap<String, String>> rows = tables.get(table);
                for (int i = 0; i < rows.size(); i++) {
                    String value = rows.get(i).get(column);
                    if (value != null) {
                        index.computeIfAbsent(value, k -> new ArrayList<>()).add(i);
                    }
                }
                indexes.computeIfAbsent(table, k -> new HashMap<>()).put(column, index);
            }

            Map<String, List<Integer>> getIndex(String table, String column) {
                return indexes.getOrDefault(table, Map.of()).get(column);
            }
        }

        public static void main(String[] args) {
            System.out.println("== Part 6: Indexing (discussion only) ==");

            System.out.println("1) Inverted index for equality WHERE:");
            System.out.println("   - Structure: {column: {value: [rowIndex, ...]}}.");
            System.out.println("   - Example:   {\"name\": {\"Alice\": [0, 4], \"Bob\": [1]}}.");
            System.out.println("   - Turns an equality lookup from O(n) full scan into");
            System.out.println("     O(1) hash lookup + O(result) to gather matching rows.");

            System.out.println("2) B-tree / sorted index for range ops (>, <, >=, <=):");
            System.out.println("   - Keep keys in sorted order (TreeMap / B-tree).");
            System.out.println("   - Range query = find lower bound then scan: O(log n + result).");
            System.out.println("   - Trade-off: ordered inserts cost more than a hash insert.");

            System.out.println("3) Composite index for multi-condition AND:");
            System.out.println("   - Index on a column tuple, e.g. (age, name), to satisfy");
            System.out.println("     queries that filter both without intersecting two indexes.");
            System.out.println("   - Leftmost-prefix rule: (age, name) also serves filters on age.");

            System.out.println("4) Index maintenance on INSERT:");
            System.out.println("   - Every INSERT must update each affected index, so more");
            System.out.println("     indexes = faster reads but slower writes (write amplification).");
            System.out.println("   - When to build: maintain per-insert for read-heavy tables;");
            System.out.println("     defer/bulk-build after large loads for write-heavy ingest.");

            System.out.println("5) Query-optimization ordering:");
            System.out.println("   - Filter via the most selective index FIRST (shrink the row set).");
            System.out.println("   - Then PROJECT to the requested columns.");
            System.out.println("   - SORT LAST, on the already-filtered (smaller) result set.");

            // Conceptual demo (NOT asserted): build an inverted index and show it.
            Database db = new Database();
            db.createTable("users", List.of("id", "name"));
            LinkedHashMap<String, String> r0 = new LinkedHashMap<>();
            r0.put("id", "1"); r0.put("name", "Alice");
            LinkedHashMap<String, String> r1 = new LinkedHashMap<>();
            r1.put("id", "2"); r1.put("name", "Bob");
            LinkedHashMap<String, String> r2 = new LinkedHashMap<>();
            r2.put("id", "3"); r2.put("name", "Alice");
            db.insert("users", r0);
            db.insert("users", r1);
            db.insert("users", r2);
            db.createIndex("users", "name");
            System.out.println("   conceptual inverted index on 'name' -> "
                    + db.getIndex("users", "name")
                    + "  (equality WHERE name='Alice' -> rows [0, 2])");

            System.out.println("Part 6 discussion printed.");
        }
    }
}
