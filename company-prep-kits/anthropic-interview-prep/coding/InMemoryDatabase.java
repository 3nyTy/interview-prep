import java.util.*;

/**
 * Anthropic OA — "In-Memory Database" (4 progressively-gated levels).
 *
 * Interface contract: process a list of command arrays ([op, ts, arg...]) and return one
 * result string per command. Timestamps are unique, strictly increasing milliseconds.
 *
 * DESIGN: each level is a subclass that adds ONLY the new behavior, so "what changed per
 * level" is exactly the body of each subclass:
 *
 *   Level 1  InMemoryDBLevel1 : SET / GET / DELETE / COMPARE_AND_SET / COMPARE_AND_DELETE
 *   Level 2  InMemoryDBLevel2 : + SCAN / SCAN_BY_PREFIX            (lexicographic, prefix)
 *   Level 3  InMemoryDBLevel3 : + SET_WITH_TTL                     (auto-expiry)
 *   Level 4  InMemoryDBLevel4 : + BACKUP / RESTORE                 (deep snapshot, delta-TTL)
 *
 * Plus InMemoryDBLookback: the OTHER Level-4 rotation (GET_WHEN time-travel) on an
 * append-only history — included because the OA ships one of the two Level-4 variants.
 *
 * KEY CORNER CASES (the ones graders target):
 *   - TTL boundary is [ts, ts+ttl): alive while ts < expiresAt, DEAD at exactly ts+ttl.
 *   - plain SET clears any existing TTL; COMPARE_AND_SET preserves the field's expiry.
 *   - DELETE / COMPARE_AND_DELETE on an already-expired field return "false".
 *   - RESTORE stores REMAINING TTL (a delta), never an absolute expiry, so countdowns
 *     resume against the restore clock. Storing absolute expiries is the #1 failed case.
 *   - RESTORE picks the latest backup whose id <= restore_at (nearest earlier snapshot).
 */
public class InMemoryDatabase {

    // ============================================================================================
    // Shared mutable cell for a single field's value + optional expiry.
    // ============================================================================================
    static final class FieldState {
        int value;
        Long expiresAt;            // null => never expires (absolute timestamp otherwise)
        FieldState(int value, Long expiresAt) { this.value = value; this.expiresAt = expiresAt; }
    }

    // ============================================================================================
    // LEVEL 1 — core records: SET / GET / DELETE / COMPARE_AND_SET / COMPARE_AND_DELETE
    //
    // records[key] is a TreeMap so fields are kept sorted lexicographically (needed in L2) and
    // so prefix scans (L2) can use an O(log F) range view instead of a full O(F log F) sort.
    // ============================================================================================
    static class InMemoryDBLevel1 {
        // key -> (field -> state). TreeMap on the inner map keeps fields in lexicographic order.
        protected final Map<String, TreeMap<String, FieldState>> records = new HashMap<>();

        /** A field is alive iff it has no TTL or the current time has not reached its expiry. */
        protected boolean isAlive(FieldState f, long ts) {
            return f.expiresAt == null || ts < f.expiresAt;   // strict: dead at exactly ts+ttl
        }

        protected TreeMap<String, FieldState> getOrCreateRecord(String key) {
            return records.computeIfAbsent(key, k -> new TreeMap<>());
        }

        /**
         * Read a field only if it is currently alive; lazily evict it (and an emptied record)
         * if it has expired. Centralizing reads here makes every L1 op automatically TTL-aware
         * once L3 introduces expiry — no L1 code changes needed.
         */
        protected FieldState readLiveField(long ts, String key, String field) {
            TreeMap<String, FieldState> rec = records.get(key);
            if (rec == null) return null;
            FieldState st = rec.get(field);
            if (st == null) return null;
            if (isAlive(st, ts)) return st;
            rec.remove(field);                       // lazy eviction of an expired field
            if (rec.isEmpty()) records.remove(key);
            return null;
        }

        /** SET: insert/overwrite field with NO TTL (clears any prior expiry). Returns "". */
        public String set(long ts, String key, String field, int value) {
            getOrCreateRecord(key).put(field, new FieldState(value, null));
            return "";
        }

        /** GET: value as a string, or "" if record/field missing or expired. */
        public String get(long ts, String key, String field) {
            FieldState cur = readLiveField(ts, key, field);
            return cur == null ? "" : Integer.toString(cur.value);
        }

        /** DELETE: remove a live field. "true" if removed, "false" if missing/expired. */
        public String delete(long ts, String key, String field) {
            if (readLiveField(ts, key, field) == null) return "false";
            TreeMap<String, FieldState> rec = records.get(key);
            rec.remove(field);
            if (rec.isEmpty()) records.remove(key);
            return "true";
        }

        /** COMPARE_AND_SET: set newValue only if current == expected. Preserves expiry. */
        public String compareAndSet(long ts, String key, String field, int expected, int newValue) {
            FieldState cur = readLiveField(ts, key, field);
            if (cur == null || cur.value != expected) return "false";
            cur.value = newValue;                    // mutate in place => expiresAt preserved
            return "true";
        }

        /** COMPARE_AND_DELETE: remove the field only if current == expected. */
        public String compareAndDelete(long ts, String key, String field, int expected) {
            FieldState cur = readLiveField(ts, key, field);
            if (cur == null || cur.value != expected) return "false";
            TreeMap<String, FieldState> rec = records.get(key);
            rec.remove(field);
            if (rec.isEmpty()) records.remove(key);
            return "true";
        }
    }

    // ============================================================================================
    // LEVEL 2 — search (NEW since L1): SCAN, SCAN_BY_PREFIX
    //
    // Output format: "<f1>(<v1>), <f2>(<v2>), ..." sorted by field name; "" if nothing matches.
    // Fields are already sorted (TreeMap), so SCAN is O(F) and prefix scan is O(log F + matches).
    // ============================================================================================
    static class InMemoryDBLevel2 extends InMemoryDBLevel1 {

        private static void append(StringBuilder sb, String field, int value) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(field).append('(').append(value).append(')');
        }

        /**
         * Shared formatter over either the whole record (scan) or a prefix range (scanByPrefix),
         * skipping + lazily evicting expired fields.
         */
        protected String formatLiveView(long ts, String key, boolean usePrefix, String prefix) {
            TreeMap<String, FieldState> rec = records.get(key);
            if (rec == null) return "";
            // tailMap gives the sorted suffix starting at `prefix`; we stop as soon as a key
            // no longer starts with prefix (the range is contiguous because the map is sorted).
            NavigableMap<String, FieldState> view = usePrefix ? rec.tailMap(prefix, true) : rec;

            StringBuilder sb = new StringBuilder();
            List<String> expired = new ArrayList<>();
            for (Map.Entry<String, FieldState> e : view.entrySet()) {
                String f = e.getKey();
                if (usePrefix && !f.startsWith(prefix)) break;   // left the prefix range -> done
                if (isAlive(e.getValue(), ts)) append(sb, f, e.getValue().value);
                else expired.add(f);
            }
            for (String f : expired) rec.remove(f);              // evict after iterating
            if (rec.isEmpty()) records.remove(key);
            return sb.toString();
        }

        /** SCAN: all live fields of a record, sorted by name. */
        public String scan(long ts, String key) {
            return formatLiveView(ts, key, false, null);
        }

        /** SCAN_BY_PREFIX: live fields whose name starts with prefix, sorted by name. */
        public String scanByPrefix(long ts, String key, String prefix) {
            return formatLiveView(ts, key, true, prefix);
        }
    }

    // ============================================================================================
    // LEVEL 3 — TTL (NEW since L2): SET_WITH_TTL
    //
    // The ONLY new code is one method. GET / SCAN / COMPARE_* became TTL-aware for free because
    // they all read through readLiveField / isAlive, which already honor expiry.
    // ============================================================================================
    static class InMemoryDBLevel3 extends InMemoryDBLevel2 {
        /** Field lives during [ts, ts+ttl); a later plain SET would clear this expiry. */
        public String setWithTtl(long ts, String key, String field, int value, long ttl) {
            getOrCreateRecord(key).put(field, new FieldState(value, ts + ttl));
            return "";
        }
    }

    // ============================================================================================
    // LEVEL 4 — backup / restore (NEW since L3): BACKUP, RESTORE
    //
    // CHANGES vs L3:
    //   (+) `backups`: id -> deep snapshot. Snapshots store REMAINING TTL (delta), not absolute
    //       expiry, so a restore can resume each countdown against the new (restore) clock.
    //   (+) BACKUP: deep-copy every record that has >=1 live field; return that record count.
    //   (+) RESTORE: pick the latest backup id <= restore_at (TreeMap.floorEntry), rebuild live
    //       state, recomputing expiresAt = restoreTs + remainingTtl.
    // ============================================================================================
    static class InMemoryDBLevel4 extends InMemoryDBLevel3 {

        /** Snapshot cell: value + remaining TTL delta (null = field had no TTL). */
        private static final class SnapField {
            final int value;
            final Long remainingTtl;
            SnapField(int value, Long remainingTtl) { this.value = value; this.remainingTtl = remainingTtl; }
        }

        // backup id -> (key -> (field -> SnapField)). TreeMap so floorEntry finds the nearest
        // earlier snapshot in O(log B).
        private final TreeMap<Long, Map<String, Map<String, SnapField>>> backups = new TreeMap<>();

        /** BACKUP: deep snapshot of all live data; returns the count of valid (non-empty) records. */
        public String backup(long ts, long backupId) {
            Map<String, Map<String, SnapField>> snap = new HashMap<>();
            for (Map.Entry<String, TreeMap<String, FieldState>> rk : records.entrySet()) {
                Map<String, SnapField> snapRec = new HashMap<>();
                for (Map.Entry<String, FieldState> fe : rk.getValue().entrySet()) {
                    FieldState st = fe.getValue();
                    if (!isAlive(st, ts)) continue;                       // skip expired at snapshot point
                    Long remaining = (st.expiresAt == null) ? null : st.expiresAt - ts;  // store DELTA
                    snapRec.put(fe.getKey(), new SnapField(st.value, remaining));
                }
                if (!snapRec.isEmpty()) snap.put(rk.getKey(), snapRec);   // valid = has >=1 live field
            }
            backups.put(backupId, snap);   // these are fresh objects => already a deep copy
            return Integer.toString(snap.size());
        }

        /** RESTORE: load nearest backup with id <= restoreAt; resume TTLs from the restore clock. */
        public String restore(long ts, long restoreAt) {
            Map.Entry<Long, Map<String, Map<String, SnapField>>> entry = backups.floorEntry(restoreAt);
            if (entry == null) return "";                 // no eligible backup -> no-op
            Map<String, Map<String, SnapField>> snap = entry.getValue();

            records.clear();                              // replace live state wholesale
            for (Map.Entry<String, Map<String, SnapField>> rk : snap.entrySet()) {
                TreeMap<String, FieldState> rec = new TreeMap<>();
                for (Map.Entry<String, SnapField> fe : rk.getValue().entrySet()) {
                    SnapField sf = fe.getValue();
                    Long expiresAt = (sf.remainingTtl == null) ? null : ts + sf.remainingTtl; // delta -> absolute
                    rec.put(fe.getKey(), new FieldState(sf.value, expiresAt));  // fresh cells: no aliasing
                }
                records.put(rk.getKey(), rec);
            }
            return "";
        }
    }

    // ============================================================================================
    // LEVEL 4 (ALTERNATE ROTATION) — GET_WHEN time-travel on append-only history.
    //
    // Standalone because it needs a different data shape: per (key, field) we keep the full
    // version history [(setTs, value, expiresAt)...]. Since timestamps strictly increase, each
    // list is naturally sorted by setTs, so GET_WHEN binary-searches the last write at-or-before
    // the query time, then checks that write's own expiry against the query time.
    // ============================================================================================
    static final class InMemoryDBLookback {
        private static final class Version {
            final long setTs; final int value; final Long expiresAt;
            Version(long setTs, int value, Long expiresAt) { this.setTs = setTs; this.value = value; this.expiresAt = expiresAt; }
        }
        private final Map<String, Map<String, List<Version>>> hist = new HashMap<>();

        private List<Version> versions(String key, String field) {
            return hist.computeIfAbsent(key, k -> new HashMap<>())
                       .computeIfAbsent(field, f -> new ArrayList<>());
        }

        public String set(long ts, String key, String field, int value) {
            versions(key, field).add(new Version(ts, value, null));        // append-only
            return "";
        }
        public String setWithTtl(long ts, String key, String field, int value, long ttl) {
            versions(key, field).add(new Version(ts, value, ts + ttl));    // append-only with expiry
            return "";
        }
        /** GET as of the current command time (Level-1 semantics). */
        public String get(long ts, String key, String field) { return getWhen(ts, key, field, ts); }

        /** GET_WHEN: value as of `at` (at == 0 => behave like a normal GET at command time). */
        public String getWhen(long ts, String key, String field, long at) {
            long when = (at == 0) ? ts : at;
            Map<String, List<Version>> rec = hist.get(key);
            if (rec == null) return "";
            List<Version> vs = rec.get(field);
            if (vs == null || vs.isEmpty()) return "";

            // Binary search: largest index with setTs <= when (the write in effect at `when`).
            int lo = 0, hi = vs.size() - 1, idx = -1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (vs.get(mid).setTs <= when) { idx = mid; lo = mid + 1; }
                else hi = mid - 1;
            }
            if (idx < 0) return "";                       // no write existed yet at `when`
            Version v = vs.get(idx);
            if (v.expiresAt == null || when < v.expiresAt) return Integer.toString(v.value);
            return "";                                    // that write had already expired by `when`
        }
    }

    // ============================================================================================
    // Dispatchers: route command arrays to the right method. BACKUP/RESTORE rotation by default.
    // ============================================================================================
    public static List<String> solve(List<String[]> queries) {
        InMemoryDBLevel4 db = new InMemoryDBLevel4();
        List<String> out = new ArrayList<>(queries.size());
        for (String[] q : queries) {
            String op = q[0];
            long ts = Long.parseLong(q[1]);
            switch (op) {
                case "SET"                -> out.add(db.set(ts, q[2], q[3], Integer.parseInt(q[4])));
                case "GET"                -> out.add(db.get(ts, q[2], q[3]));
                case "DELETE"             -> out.add(db.delete(ts, q[2], q[3]));
                case "COMPARE_AND_SET"    -> out.add(db.compareAndSet(ts, q[2], q[3], Integer.parseInt(q[4]), Integer.parseInt(q[5])));
                case "COMPARE_AND_DELETE" -> out.add(db.compareAndDelete(ts, q[2], q[3], Integer.parseInt(q[4])));
                case "SCAN"               -> out.add(db.scan(ts, q[2]));
                case "SCAN_BY_PREFIX"     -> out.add(db.scanByPrefix(ts, q[2], q[3]));
                case "SET_WITH_TTL", "SET_AT_WITH_TTL"
                                          -> out.add(db.setWithTtl(ts, q[2], q[3], Integer.parseInt(q[4]), Long.parseLong(q[5])));
                // BACKUP <ts> [<backupId>]; if id omitted, the timestamp itself is the id.
                case "BACKUP"             -> out.add(db.backup(ts, q.length > 2 ? Long.parseLong(q[2]) : ts));
                case "RESTORE"            -> out.add(db.restore(ts, Long.parseLong(q[2])));
                default -> throw new IllegalArgumentException("Unknown op: " + op);
            }
        }
        return out;
    }

    /** Dispatcher for the GET_WHEN look-back rotation. */
    public static List<String> solveLookback(List<String[]> queries) {
        InMemoryDBLookback db = new InMemoryDBLookback();
        List<String> out = new ArrayList<>(queries.size());
        for (String[] q : queries) {
            String op = q[0];
            long ts = Long.parseLong(q[1]);
            switch (op) {
                case "SET"          -> out.add(db.set(ts, q[2], q[3], Integer.parseInt(q[4])));
                case "GET"          -> out.add(db.get(ts, q[2], q[3]));
                case "SET_WITH_TTL", "SET_AT_WITH_TTL"
                                    -> out.add(db.setWithTtl(ts, q[2], q[3], Integer.parseInt(q[4]), Long.parseLong(q[5])));
                case "GET_WHEN"     -> out.add(db.getWhen(ts, q[2], q[3], Long.parseLong(q[4])));
                default -> throw new IllegalArgumentException("Unknown op: " + op);
            }
        }
        return out;
    }

    // ============================================================================================
    // Test harness — asserts every example from both prompts plus extra corner cases.
    // ============================================================================================
    static int pass = 0, fail = 0;
    static void check(String label, List<String> actual, List<String> expected) {
        boolean ok = actual.equals(expected);
        System.out.println((ok ? "PASS " : "FAIL ") + label);
        if (!ok) { System.out.println("   expected: " + expected); System.out.println("   actual:   " + actual); }
        if (ok) pass++; else fail++;
    }
    static List<String[]> Q(String[]... a) { return Arrays.asList(a); }
    static String[] c(String... a) { return a; }
    static List<String> E(String... a) { return Arrays.asList(a); }

    public static void main(String[] args) {
        // ---- Level 1: example ----
        check("L1 example",
            solve(Q(c("SET","0","A","B","4"), c("SET","1","A","C","6"),
                    c("COMPARE_AND_SET","2","A","B","4","9"), c("COMPARE_AND_SET","3","A","C","4","9"),
                    c("COMPARE_AND_DELETE","4","A","C","6"), c("GET","5","A","C"), c("GET","6","A","B"))),
            E("", "", "true", "false", "true", "", "9"));

        // ---- Level 1: provided test case ----
        check("L1 test case",
            solve(Q(c("SET","160000000","a","a","1"), c("SET","160000001","a","A","2"),
                    c("GET","160000002","a","a"), c("COMPARE_AND_DELETE","160000003","a","a","0"),
                    c("GET","160000004","a","a"), c("COMPARE_AND_DELETE","160000005","a","a","1"),
                    c("GET","160000006","a","a"), c("GET","160000007","a","A"),
                    c("COMPARE_AND_DELETE","160000008","a","A","2"), c("SET","160000009","a","A","7"),
                    c("SET","160000010","a","A","9"), c("GET","160000011","a","a"), c("GET","160000012","a","A"))),
            E("", "", "1", "false", "1", "true", "", "2", "true", "", "", "", "9"));

        // ---- Level 2: example ----
        check("L2 example",
            solve(Q(c("SET","1","A","BC","1"), c("SET","2","A","BD","2"), c("SET","3","A","C","3"),
                    c("SCAN_BY_PREFIX","4","A","B"), c("SCAN","5","A"), c("SCAN_BY_PREFIX","6","B","B"))),
            E("", "", "", "BC(1), BD(2)", "BC(1), BD(2), C(3)", ""));

        // ---- Level 3: example 1 (plain SET clears TTL) ----
        check("L3 example 1 (SET clears TTL)",
            solve(Q(c("SET_WITH_TTL","1","A","BC","1","9"), c("SET_WITH_TTL","5","A","BC","2","10"),
                    c("SET","6","A","BC","3"), c("SCAN_BY_PREFIX","14","A","B"),
                    c("SCAN_BY_PREFIX","15","A","B"), c("SCAN_BY_PREFIX","100","A","B"))),
            E("", "", "", "BC(3)", "BC(3)", "BC(3)"));

        // ---- Level 3: example 2 (TTL expiry across SCAN/GET/CAD) ----
        check("L3 example 2",
            solve(Q(c("SET","1","A","B","1"), c("SET_WITH_TTL","2","X","Y","5","15"), c("GET","3","X","Y"),
                    c("SET_WITH_TTL","4","A","D","2","10"), c("SCAN","13","A"), c("SCAN","14","A"),
                    c("SCAN","16","X"), c("SCAN","17","X"), c("COMPARE_AND_DELETE","20","X","Y","5"))),
            E("", "", "5", "", "B(1), D(2)", "B(1)", "Y(5)", "", "false"));

        // ---- Level 3: 1point3acres example (TTL boundary at re-SET) ----
        check("L3 boundary example (1p3a)",
            solve(Q(c("SET_WITH_TTL","1","A","BC","1","9"), c("SET_WITH_TTL","5","A","BC","2","10"),
                    c("SET","6","A","BD","3"), c("SCAN_BY_PREFIX","14","A","B"), c("SCAN_BY_PREFIX","15","A","B"))),
            E("", "", "", "BC(2), BD(3)", "BD(3)"));

        // ---- Level 4: backup/restore with nearest-backup + delta-TTL recalculation ----
        check("L4 example (nearest backup + delta TTL)",
            solve(Q(c("SET_WITH_TTL","1","A","x","10","10"), c("SET","2","B","y","20"),
                    c("BACKUP","5","5"), c("SET","6","A","x","99"), c("BACKUP","8","8"),
                    c("COMPARE_AND_DELETE","9","B","y","20"), c("RESTORE","12","7"),
                    c("GET","13","A","x"), c("GET","14","B","y"), c("SCAN","19","A"))),
            E("", "", "2", "", "2", "true", "", "10", "20", ""));

        // ---- Extra: DELETE rotation, incl. delete-on-expired returns "false" ----
        check("DELETE rotation + delete-on-expired",
            solve(Q(c("SET","0","A","B","4"), c("DELETE","1","A","B"), c("DELETE","2","A","B"),
                    c("SET_WITH_TTL","3","A","C","5","10"), c("DELETE","20","A","C"))),
            E("", "true", "false", "", "false"));

        // ---- Extra: Level-4 GET_WHEN look-back rotation ----
        check("L4 alt rotation: GET_WHEN",
            solveLookback(Q(c("SET","1","A","x","10"), c("SET","5","A","x","20"),
                    c("GET_WHEN","6","A","x","3"), c("GET_WHEN","7","A","x","5"), c("GET_WHEN","8","A","x","0"),
                    c("SET_WITH_TTL","10","A","y","7","2"), c("GET_WHEN","11","A","y","11"),
                    c("GET_WHEN","13","A","y","13"), c("GET_WHEN","14","A","z","14"))),
            E("", "", "10", "20", "20", "", "7", "", ""));

        System.out.println("\n" + pass + " passed, " + fail + " failed");
        if (fail > 0) System.exit(1);
    }
}
