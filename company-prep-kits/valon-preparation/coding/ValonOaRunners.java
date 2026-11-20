import java.io.*;
import java.util.*;

/**
 * Valon — stdin/stdout drivers for the three variants that specify real I/O.
 * The engine lives in InMemoryDatabase.java; this file is only parsing + printing.
 *
 *   java ValonOaRunners and     < input   # d5d1b167  AND filters, JSON Lines with a Q count
 *   java ValonOaRunners sparse  < input   # e1de73a3  typed sparse table, JSON Lines
 *   java ValonOaRunners sql     < input   # 709ae770  CSV rows + SELECT/IN/FILTER BY
 *
 * ⭐ WHY THERE IS A HAND-ROLLED PARSER HERE: three variants read JSON Lines, and an
 * OA judge gives you no Jackson/Gson. MiniJson below is ~70 lines and is the part
 * candidates lose time on, so it is worth having memorised in outline: a recursive
 * descent parser over a char cursor, LinkedHashMap so key order survives, Long for
 * integers so 1 does not print as 1.0.
 */
public class ValonOaRunners {

    /* ============================== MiniJson ============================== */
    static final class MiniJson {
        private final String s; private int i;
        private MiniJson(String s) { this.s = s; }
        static Object parse(String text) {
            MiniJson p = new MiniJson(text);
            p.ws(); Object v = p.value(); p.ws();
            if (p.i != text.length()) throw new IllegalArgumentException("trailing input at " + p.i);
            return v;
        }
        private void ws() { while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++; }
        private char peek() { if (i >= s.length()) throw new IllegalArgumentException("eof"); return s.charAt(i); }
        private void expect(char c) { if (peek() != c) throw new IllegalArgumentException("expected " + c + " at " + i); i++; }

        private Object value() {
            char c = peek();
            switch (c) {
                case '{': return object();
                case '[': return array();
                case '"': return string();
                case 't': lit("true");  return Boolean.TRUE;
                case 'f': lit("false"); return Boolean.FALSE;
                case 'n': lit("null");  return null;
                default:  return number();
            }
        }
        private void lit(String w) {
            if (!s.startsWith(w, i)) throw new IllegalArgumentException("bad literal at " + i);
            i += w.length();
        }
        private Map<String, Object> object() {
            expect('{'); Map<String, Object> m = new LinkedHashMap<>(); ws();
            if (peek() == '}') { i++; return m; }
            while (true) {
                ws(); String k = string(); ws(); expect(':'); ws();
                m.put(k, value()); ws();
                char c = peek();
                if (c == ',') { i++; continue; }
                expect('}'); return m;
            }
        }
        private List<Object> array() {
            expect('['); List<Object> a = new ArrayList<>(); ws();
            if (peek() == ']') { i++; return a; }
            while (true) {
                ws(); a.add(value()); ws();
                char c = peek();
                if (c == ',') { i++; continue; }
                expect(']'); return a;
            }
        }
        private String string() {
            expect('"'); StringBuilder sb = new StringBuilder();
            while (true) {
                char c = s.charAt(i++);
                if (c == '"') return sb.toString();
                if (c != '\\') { sb.append(c); continue; }
                char e = s.charAt(i++);
                switch (e) {
                    case 'n' -> sb.append('\n'); case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r'); case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'u' -> { sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16)); i += 4; }
                    default  -> sb.append(e);   // covers \" \\ \/
                }
            }
        }
        /** Integers become Long so they serialise as 1, not 1.0 — the specs use ints. */
        private Object number() {
            int st = i;
            while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) i++;
            String n = s.substring(st, i);
            if (n.indexOf('.') < 0 && n.indexOf('e') < 0 && n.indexOf('E') < 0) return Long.parseLong(n);
            return Double.parseDouble(n);
        }
    }

    /* ===== d5d1b167 — AND filters. First line Q, then Q JSON commands. ===== */
    static void runAnd(BufferedReader in, PrintStream out) throws IOException {
        InMemoryDatabase db = new InMemoryDatabase();
        String first = in.readLine();
        if (first == null) return;
        int q = Integer.parseInt(first.trim());
        for (int k = 0; k < q; k++) {
            String line = in.readLine();
            if (line == null) break;
            if (line.isBlank()) { k--; continue; }
            @SuppressWarnings("unchecked")
            Map<String, Object> cmd = (Map<String, Object>) MiniJson.parse(line);
            String op = String.valueOf(cmd.get("op")), table = String.valueOf(cmd.get("table"));
            if ("insert".equals(op)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> row = (Map<String, Object>) cmd.get("row");
                db.upsert(table, row);                        // insert-or-replace, position kept
            } else if ("select".equals(op)) {
                @SuppressWarnings("unchecked")
                List<Object> cs = (List<Object>) cmd.get("columns");
                List<String> cols = new ArrayList<>();
                for (Object c : cs) cols.add(String.valueOf(c));
                Object w = cmd.get("where");
                List<InMemoryDatabase.Pred> preds =
                        InMemoryDatabase.parseAnd(w == null ? null : String.valueOf(w));
                out.println(InMemoryDatabase.toJson(db.selectAnd(table, cols, preds)));
            }
        }
    }

    /* ===== e1de73a3 — typed sparse table. JSON Lines; ok/error per command. ===== */
    static void runSparse(BufferedReader in, PrintStream out) throws IOException {
        InMemoryDatabase db = new InMemoryDatabase();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.isBlank()) continue;
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> cmd = (Map<String, Object>) MiniJson.parse(line);
                String op = String.valueOf(cmd.get("op")), table = String.valueOf(cmd.get("table"));
                switch (op) {
                    case "create" -> {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> sch = (Map<String, Object>) cmd.get("schema");
                        Map<String, InMemoryDatabase.ColType> typed = new LinkedHashMap<>();
                        for (Map.Entry<String, Object> e : sch.entrySet()) {
                            String t = String.valueOf(e.getValue());
                            typed.put(e.getKey(), switch (t) {
                                case "int" -> InMemoryDatabase.ColType.INT;
                                case "string" -> InMemoryDatabase.ColType.STRING;
                                case "bool" -> InMemoryDatabase.ColType.BOOL;
                                default -> throw new IllegalArgumentException("unknown type: " + t);
                            });
                        }
                        db.createTyped(table, typed);
                        out.println("{\"ok\":true}");
                    }
                    case "insert" -> {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> row = (Map<String, Object>) cmd.get("row");
                        db.insertTyped(table, row);
                        out.println("{\"ok\":true}");
                    }
                    case "select" -> {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> f = (Map<String, Object>) cmd.get("filter");
                        InMemoryDatabase.Pred pred = null;
                        if (f != null) pred = new InMemoryDatabase.Pred(
                                String.valueOf(f.get("column")),
                                InMemoryDatabase.Op.EQ,
                                String.valueOf(f.get("value")));
                        @SuppressWarnings("unchecked")
                        Map<String, Object> pj = (Map<String, Object>) cmd.get("projection");
                        Map<String, String> proj = new LinkedHashMap<>();
                        for (Map.Entry<String, Object> e : pj.entrySet())
                            proj.put(e.getKey(), String.valueOf(e.getValue()));
                        out.println(InMemoryDatabase.toJson(db.selectProjected(table, pred, proj)));
                    }
                    default -> out.println("{\"error\":\"unknown op\"}");
                }
            } catch (RuntimeException e) {
                // spec: errors print {"error":"..."} and processing continues
                out.println("{\"error\":" + InMemoryDatabase.toJson(String.valueOf(e.getMessage())) + "}");
            }
        }
    }

    /* ===== 709ae770 — CSV rows then SELECT / IN / FILTER BY queries. ===== */
    static void runSql(BufferedReader in, PrintStream out) throws IOException {
        StringTokenizer st = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        List<String> cols = Arrays.asList(in.readLine().trim().split(","));
        InMemoryDatabase db = new InMemoryDatabase();
        db.createTable("t", cols);
        for (int r = 0; r < n; r++) {
            String[] vals = in.readLine().trim().split(",", -1);
            Map<String, Object> row = new LinkedHashMap<>();
            for (int c = 0; c < m && c < vals.length; c++) row.put(cols.get(c), vals[c]);
            db.insert("t", row);
        }
        int q = Integer.parseInt(in.readLine().trim());
        for (int k = 0; k < q; k++) {
            String query = in.readLine();
            out.println(InMemoryDatabase.join(db.runSql("t", query, "")));
        }
    }

    public static void main(String[] args) throws IOException {
        String mode = args.length > 0 ? args[0] : "sparse";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        switch (mode) {
            case "and"    -> runAnd(in, System.out);
            case "sparse" -> runSparse(in, System.out);
            case "sql"    -> runSql(in, System.out);
            default -> System.err.println("modes: and | sparse | sql");
        }
    }
}
