import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Billing Status Replay (OOD) — Reddit Staff SWE sample.
 *
 * We lost the DB holding each advertiser's CURRENT billing state, but the transaction
 * LOG survives. Replay the log (increasing transaction_timestamp, ties broken by
 * transaction_id) and rebuild one BillingStatus per user.
 *
 * The object model grows through three layers; crucially, only ingest(...) changes
 * between them — the replay loop is IDENTICAL, which we encode literally as a single
 * generic rebuild(...) over the AccountState interface:
 *   Part 1  additive aggregation over known monetary columns
 *   Part 2  + overwrite transactions (replace a present column instead of adding to it)
 *   Part 3  + undo_last / redo_last stack commands
 *
 * Central OOD decision (Part 3): store before/after SNAPSHOTS of the touched columns
 * per regular transaction (a reversible command record) — NOT just deltas. Once
 * overwrite exists, a delta alone cannot reverse the operation, but a before/after pair
 * makes undo (restore before) and redo (restore after) both trivial and correct.
 *
 * Complexity: sort O(n log n); ingest O(c); undo/redo O(k<=c); space O(u*c + history).
 */
public class BillingStatusReplay {

    // ============================================================= //
    //  Shared transaction model                                      //
    // ============================================================= //

    /** One immutable log row. `monetary` holds ONLY the columns present on that row. */
    static final class Transaction {
        final String id;
        final int userId;
        final long timestamp;
        final Map<String, Long> monetary; // present columns only (missing => "no change")
        final boolean overwrite;
        final boolean undoLast;
        final boolean redoLast;

        private Transaction(Builder b) {
            this.id = b.id;
            this.userId = b.userId;
            this.timestamp = b.timestamp;
            this.monetary = b.monetary;
            this.overwrite = b.overwrite;
            this.undoLast = b.undoLast;
            this.redoLast = b.redoLast;
        }

        static Builder of(String id, int userId, long timestamp) {
            return new Builder(id, userId, timestamp);
        }

        /** Fluent builder — keeps the test data readable. */
        static final class Builder {
            private final String id;
            private final int userId;
            private final long timestamp;
            private final Map<String, Long> monetary = new LinkedHashMap<>();
            private boolean overwrite, undoLast, redoLast;

            Builder(String id, int userId, long timestamp) {
                this.id = id; this.userId = userId; this.timestamp = timestamp;
            }
            Builder money(String column, long value) { monetary.put(column, value); return this; }
            Builder overwrite() { this.overwrite = true; return this; }
            Builder undoLast()  { this.undoLast = true;  return this; }
            Builder redoLast()  { this.redoLast = true;  return this; }
            Transaction build() { return new Transaction(this); }
        }
    }

    /** Common contract so ONE replay loop drives every version (historical replay OR live stream). */
    interface AccountState {
        void ingest(Transaction t);
        Map<String, Long> snapshot();
    }

    // ============================================================= //
    //  REPLAY LOOP — identical for all three parts                   //
    // ============================================================= //
    static <S extends AccountState> Map<Integer, S> rebuild(
            Collection<Transaction> transactions, Supplier<S> newAccount) {
        // Deterministic order: increasing timestamp, ties broken by transaction id.
        List<Transaction> ordered = new ArrayList<>(transactions);
        ordered.sort(Comparator.comparingLong((Transaction t) -> t.timestamp)
                                .thenComparing(t -> t.id));
        Map<Integer, S> statuses = new LinkedHashMap<>();
        for (Transaction t : ordered) {
            statuses.computeIfAbsent(t.userId, k -> newAccount.get()).ingest(t);
        }
        return statuses;
    }

    // ============================================================= //
    //  PART 1 — additive aggregation                                 //
    // ============================================================= //
    static final class BillingStatusV1 implements AccountState {
        private final List<String> columns;
        private final Map<String, Long> amounts = new LinkedHashMap<>();

        BillingStatusV1(List<String> monetaryColumns) {
            this.columns = monetaryColumns;
            for (String c : columns) amounts.put(c, 0L);
        }
        @Override public void ingest(Transaction t) {
            for (String c : columns) {                        // add every known column
                amounts.put(c, amounts.get(c) + t.monetary.getOrDefault(c, 0L));
            }
        }
        @Override public Map<String, Long> snapshot() { return new LinkedHashMap<>(amounts); }
    }

    // ============================================================= //
    //  PART 2 — + overwrite   (ONLY ingest changes vs Part 1)        //
    // ============================================================= //
    static final class BillingStatusV2 implements AccountState {
        private final List<String> columns;
        private final Map<String, Long> amounts = new LinkedHashMap<>();

        BillingStatusV2(List<String> monetaryColumns) {
            this.columns = monetaryColumns;
            for (String c : columns) amounts.put(c, 0L);
        }
        @Override public void ingest(Transaction t) {
            for (String c : columns) {
                if (!t.monetary.containsKey(c)) continue;     // CHANGED: absent column => no change
                long value = t.monetary.get(c);
                if (t.overwrite) amounts.put(c, value);       // CHANGED: replace when overwrite=true
                else amounts.put(c, amounts.get(c) + value);  // else add (the Part 1 behavior)
            }
        }
        @Override public Map<String, Long> snapshot() { return new LinkedHashMap<>(amounts); }
    }

    // ============================================================= //
    //  PART 3 — + undo_last / redo_last   (the final class)          //
    //  NEW vs Part 2: ReplayRecord snapshots + applied/undone stacks //
    // ============================================================= //

    /** Reversible command record: exact before/after for the columns a regular txn touched. */
    static final class ReplayRecord {
        final List<String> columns;
        final Map<String, Long> before;
        final Map<String, Long> after;
        ReplayRecord(List<String> columns, Map<String, Long> before, Map<String, Long> after) {
            this.columns = columns; this.before = before; this.after = after;
        }
    }

    static final class BillingStatus implements AccountState {
        private final List<String> columns;
        private final Map<String, Long> amounts = new LinkedHashMap<>();
        private final Deque<ReplayRecord> applied = new ArrayDeque<>(); // undo stack (LIFO)
        private final Deque<ReplayRecord> undone  = new ArrayDeque<>(); // redo stack (LIFO)

        BillingStatus(List<String> monetaryColumns) {
            this.columns = monetaryColumns;
            for (String c : columns) amounts.put(c, 0L);
        }

        @Override public void ingest(Transaction t) {
            // NEW: command rows are handled first and IGNORE any monetary fields on the row.
            if (t.undoLast) { undoLast(); return; }
            if (t.redoLast) { redoLast(); return; }

            // Regular transaction (plain OR overwrite-only): apply to touched columns only.
            List<String> touched = new ArrayList<>();
            for (String c : columns) if (t.monetary.containsKey(c)) touched.add(c);
            if (touched.isEmpty()) return;                     // no monetary effect => nothing to record

            Map<String, Long> before = new LinkedHashMap<>();
            Map<String, Long> after  = new LinkedHashMap<>();
            for (String c : touched) {
                before.put(c, amounts.get(c));                 // capture reversible state
                long value = t.monetary.get(c);
                long updated = t.overwrite ? value : amounts.get(c) + value;
                amounts.put(c, updated);
                after.put(c, updated);
            }
            applied.push(new ReplayRecord(touched, before, after)); // NEW: remember the effect
            undone.clear();                                          // NEW: fresh apply invalidates redo
        }

        /** NEW: undo the most recent regular transaction; discard if there is nothing to undo. */
        private void undoLast() {
            if (applied.isEmpty()) return;
            ReplayRecord r = applied.pop();
            for (String c : r.columns) amounts.put(c, r.before.get(c)); // restore BEFORE
            undone.push(r);
        }

        /** NEW: reapply the most recently undone transaction; discard if there is nothing to redo. */
        private void redoLast() {
            if (undone.isEmpty()) return;
            ReplayRecord r = undone.pop();
            for (String c : r.columns) amounts.put(c, r.after.get(c));  // restore AFTER
            applied.push(r);
        }

        @Override public Map<String, Long> snapshot() { return new LinkedHashMap<>(amounts); }
    }

    // ============================================================= //
    //  demo / self-verifying smoke tests (mirror the prompt)         //
    // ============================================================= //
    private static final String AD  = "ad_delivery_pennies";
    private static final String PAY = "payment_pennies";
    private static final List<String> COLS = Arrays.asList(AD, PAY);

    private static int checks = 0, failures = 0;

    public static void main(String[] args) {
        // ---------- Part 1 dataset: pure additive ----------
        List<Transaction> part1 = Arrays.asList(
            Transaction.of("ff8bc1c2-8d45-11e9-bc42-526af7764f64", 1, 1500000001L).money(AD, 1000).build(),
            Transaction.of("ff8bc2e4-8d45-11e9-bc42-526af7764f64", 1, 1500000002L).money(AD, 1000).build(),
            Transaction.of("ff8bc4ec-8d45-11e9-bc42-526af7764f64", 1, 1500000003L).money(PAY, 500).build(),
            Transaction.of("fv24z4ec-8d45-11e9-bc42-526af7764f64", 1, 1500000004L).money(AD, 1000).money(PAY, 500).build()
        );
        System.out.println("== Part 1: additive ==");
        Map<Integer, BillingStatusV1> r1 = rebuild(part1, () -> new BillingStatusV1(COLS));
        expect("user 1", r1.get(1).snapshot(), money(3000, 1000));

        // ---------- Part 2 dataset: + overwrite ----------
        List<Transaction> part2 = Arrays.asList(
            Transaction.of("ff8ba98a-8d45-11e9-bc42-526af7764f64", 1, 1500000001L).money(AD, 1000).build(),
            Transaction.of("ff8bad4a-8d45-11e9-bc42-526af7764f64", 2, 1500000004L).money(AD, 1000).build(),
            Transaction.of("ff8baea8-8d45-11e9-bc42-526af7764f64", 2, 1500000007L).money(PAY, 600).build(),
            Transaction.of("ff8bb4ac-8d45-11e9-bc42-526af7764f64", 1, 1500000002L).money(AD, 1000).build(),
            Transaction.of("ff8bb600-8d45-11e9-bc42-526af7764f64", 2, 1500000003L).money(AD, 1000).money(PAY, 500).build(),
            Transaction.of("ff8bb89e-8d45-11e9-bc42-526af7764f64", 2, 1500000005L).money(PAY, 2000).overwrite().build(),
            Transaction.of("ff8bb9c0-8d45-11e9-bc42-526af7764f64", 1, 1500000003L).money(PAY, 500).build(),
            Transaction.of("ff8bbf74-8d45-11e9-bc42-526af7764f64", 1, 1500000004L).money(AD, 1000).money(PAY, 500).overwrite().build(),
            Transaction.of("ff8bc0a0-8d45-11e9-bc42-526af7764f64", 2, 1500000001L).money(AD, 1000).build(),
            Transaction.of("ff8bc1c2-8d45-11e9-bc42-526af7764f64", 2, 1500000002L).money(AD, 1000).build(),
            Transaction.of("ff923488-8d45-11e9-bc42-526af7764f64", 1, 1500000013L).money(PAY, 100).build()
        );
        System.out.println("\n== Part 2: + overwrite ==");
        Map<Integer, BillingStatusV2> r2 = rebuild(part2, () -> new BillingStatusV2(COLS));
        expect("user 1", r2.get(1).snapshot(), money(1000, 600));
        expect("user 2", r2.get(2).snapshot(), money(4000, 2600));

        // ---------- Part 3 dataset: + undo_last ----------
        List<Transaction> part3 = Arrays.asList(
            Transaction.of("ff8bc1c2-8d45-11e9-bc42-526af7764f64", 1, 1500000001L).money(AD, 1000).build(),
            Transaction.of("ff8bc2e4-8d45-11e9-bc42-526af7764f64", 1, 1500000002L).undoLast().build(),
            Transaction.of("ff8bc4ec-8d45-11e9-bc42-526af7764f64", 1, 1500000003L).money(PAY, 500).build(),
            Transaction.of("fv24z4ec-8d45-11e9-bc42-526af7764f64", 1, 1500000004L).money(AD, 1000).money(PAY, 500).build()
        );
        System.out.println("\n== Part 3: + undo_last / redo_last ==");
        Map<Integer, BillingStatus> r3 = rebuild(part3, () -> new BillingStatus(COLS));
        expect("user 1", r3.get(1).snapshot(), money(1000, 1000));

        // ---------- Part 3 extra: redo semantics ----------
        List<Transaction> redoReapplies = Arrays.asList(
            Transaction.of("a", 9, 1L).money(AD, 1000).build(),
            Transaction.of("b", 9, 2L).undoLast().build(),
            Transaction.of("c", 9, 3L).redoLast().build()          // reapplies the undone +1000
        );
        expect("redo reapplies", rebuild(redoReapplies, () -> new BillingStatus(COLS)).get(9).snapshot(), money(1000, 0));

        List<Transaction> redoInvalidated = Arrays.asList(
            Transaction.of("a", 10, 1L).money(AD, 1000).build(),
            Transaction.of("b", 10, 2L).undoLast().build(),
            Transaction.of("c", 10, 3L).money(PAY, 500).build(),   // new regular apply clears redo history
            Transaction.of("d", 10, 4L).redoLast().build()          // => discarded, nothing happens
        );
        expect("redo invalidated", rebuild(redoInvalidated, () -> new BillingStatus(COLS)).get(10).snapshot(), money(0, 500));

        // ---------- final class is a superset: it also satisfies Parts 1 & 2 ----------
        System.out.println("\n== Superset: final BillingStatus on Part 1 & 2 data ==");
        expect("P1 via final", rebuild(part1, () -> new BillingStatus(COLS)).get(1).snapshot(), money(3000, 1000));
        Map<Integer, BillingStatus> r2f = rebuild(part2, () -> new BillingStatus(COLS));
        expect("P2 via final u1", r2f.get(1).snapshot(), money(1000, 600));
        expect("P2 via final u2", r2f.get(2).snapshot(), money(4000, 2600));

        System.out.println("\n" + (failures == 0
                ? "ALL " + checks + " CHECKS PASSED"
                : failures + "/" + checks + " CHECKS FAILED"));
    }

    /** Build an expected {ad_delivery_pennies, payment_pennies} map. */
    private static Map<String, Long> money(long ad, long pay) {
        Map<String, Long> m = new LinkedHashMap<>();
        m.put(AD, ad);
        m.put(PAY, pay);
        return m;
    }

    private static void expect(String label, Map<String, Long> actual, Map<String, Long> expected) {
        checks++;
        boolean ok = actual.equals(expected);          // Map.equals ignores order
        if (!ok) failures++;
        System.out.println((ok ? "  PASS " : "  FAIL ") + label + " => " + fmt(actual)
                + (ok ? "" : "   (expected " + fmt(expected) + ")"));
    }

    private static String fmt(Map<String, Long> m) {
        StringBuilder sb = new StringBuilder("BillingStatus(");
        boolean first = true;
        for (Map.Entry<String, Long> e : m.entrySet()) {
            if (!first) sb.append(", ");
            sb.append(e.getKey()).append('=').append(e.getValue());
            first = false;
        }
        return sb.append(')').toString();
    }
}
