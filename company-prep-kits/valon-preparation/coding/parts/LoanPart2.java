import java.util.*;

/**
 * Valon domain coding — PART 2
 * "LoanLedger.process_event(event_type, amount, accounting_sequence) -> list[LedgerEntry]"
 *
 * Events ARRIVE in real time but are ACCOUNTED in accounting_sequence order, so a
 * backdated event lands in the middle of the history and everything after it changes.
 *
 * Constraint from the prompt: "We should not have to re-process all events every time
 * an out-of-order event comes in." => replay only the SUFFIX after the insertion point,
 * starting from the predecessor's cached snapshot.
 *
 * Lines new or changed since Part 1 are marked. Unmarked lines are byte-identical
 * to Part 1.
 */
public class LoanPart2 {

    record Balances(long principalBalance, long feeBalance) {
        Balances {
            if (principalBalance < 0 || feeBalance < 0)
                throw new IllegalArgumentException("balances cannot go negative: "
                        + principalBalance + "/" + feeBalance);
        }
        @Override public String toString() {
            return "balance: " + principalBalance + ", fee: " + feeBalance;
        }
    }

    static Balances processFee(Balances b, long amount) {
        requireNonNegative(amount);
        return new Balances(b.principalBalance(), b.feeBalance() + amount);
    }

    static Balances processPayment(Balances b, long amount) {
        requireNonNegative(amount);
        long toFee       = Math.min(amount, b.feeBalance());
        long toPrincipal = Math.min(amount - toFee, b.principalBalance());
        return new Balances(b.principalBalance() - toPrincipal,
                            b.feeBalance()       - toFee);
    }

    private static void requireNonNegative(long amount) {
        if (amount < 0) throw new IllegalArgumentException("amount cannot be negative: " + amount);
    }

    /** One dispatch point, so Part 1's two functions stay the only place the rules live. */ // ← NEW in Part 2
    static Balances apply(String eventType, Balances b, long amount) {                        // ← NEW in Part 2
        return switch (eventType) {                                                           // ← NEW in Part 2
            case "PAYMENT" -> processPayment(b, amount);                                      // ← NEW in Part 2
            case "FEE"     -> processFee(b, amount);                                          // ← NEW in Part 2
            default -> throw new IllegalArgumentException("unknown event_type: " + eventType); // ← NEW: reject, don't ignore
        };                                                                                    // ← NEW in Part 2
    }                                                                                         // ← NEW in Part 2

    /** The returned row shape: (accounting_sequence, principal_balance, fee_balance). */     // ← NEW in Part 2
    public record LedgerEntry(long accountingSequence,                                        // ← NEW in Part 2
                              long principalBalance, long feeBalance) {                       // ← NEW in Part 2
        @Override public String toString() {                                                  // ← NEW in Part 2
            return "(" + accountingSequence + ", " + principalBalance + ", " + feeBalance + ")"; // ← NEW in Part 2
        }                                                                                     // ← NEW in Part 2
    }                                                                                         // ← NEW in Part 2

    public static final class LoanLedger {                                                    // ← NEW in Part 2

        private static final Balances INITIAL = new Balances(100_000, 0);                      // ← NEW in Part 2

        /**                                                                                    // ← NEW in Part 2
         * Ordering key. accounting_sequence first; arrival ordinal breaks ties so two          // ← NEW in Part 2
         * events on the SAME accounting date stay in the order we received them, which          // ← NEW in Part 2
         * is what the prompt asks for. Worth clarifying whether same-sequence events            // ← NEW in Part 2
         * should be one row each or netted into one — I emit one row per event.                 // ← NEW in Part 2
         */                                                                                    // ← NEW in Part 2
        private record Key(long sequence, long arrival) implements Comparable<Key> {            // ← NEW in Part 2
            public int compareTo(Key o) {                                                      // ← NEW in Part 2
                int c = Long.compare(sequence, o.sequence);                                    // ← NEW in Part 2
                return c != 0 ? c : Long.compare(arrival, o.arrival);                          // ← NEW: stable on ties
            }                                                                                  // ← NEW in Part 2
        }                                                                                      // ← NEW in Part 2

        /**                                                                                    // ← NEW in Part 2
         * The event is the immutable fact; `after` is DERIVED state, memoised so the            // ← NEW in Part 2
         * prefix never needs recomputing. Mutable on purpose — replay overwrites it.            // ← NEW in Part 2
         */                                                                                    // ← NEW in Part 2
        private static final class Node {                                                      // ← NEW in Part 2
            final String type;                                                                 // ← NEW in Part 2
            final long amount;                                                                 // ← NEW in Part 2
            Balances after;                                                                    // ← NEW in Part 2
            Node(String type, long amount) { this.type = type; this.amount = amount; }         // ← NEW in Part 2
        }                                                                                      // ← NEW in Part 2

        /** Sorted by accounting order, so insertion position is a O(log n) lookup. */         // ← NEW in Part 2
        private final NavigableMap<Key, Node> log = new TreeMap<>();                            // ← NEW in Part 2
        private long arrivals = 0;                                                              // ← NEW in Part 2
        private long lastReplayed = 0;   // ← NEW: instrumentation, to PROVE we don't redo everything

        /**                                                                                    // ← NEW in Part 2
         * O(log n + k), k = events accounted AFTER this one.                                   // ← NEW in Part 2
         *   - in-order arrival  => k = 0, so one apply and no replay at all                     // ← NEW in Part 2
         *   - backdated arrival => only the suffix is redone, never the prefix                   // ← NEW in Part 2
         * k is a lower bound: those k rows genuinely have different values now, so any            // ← NEW in Part 2
         * correct implementation must touch them. This is output-bound, not wasteful.             // ← NEW in Part 2
         */                                                                                    // ← NEW in Part 2
        public List<LedgerEntry> processEvent(String eventType, long amount,                   // ← NEW in Part 2
                                              long accountingSequence) {                        // ← NEW in Part 2
            apply(eventType, INITIAL, amount);   // ← NEW: validate type+amount BEFORE mutating the log

            Key key = new Key(accountingSequence, arrivals++);                                 // ← NEW in Part 2
            log.put(key, new Node(eventType, amount));                                          // ← NEW in Part 2

            // the prefix is already correct: start from the predecessor's cached snapshot
            Map.Entry<Key, Node> prev = log.lowerEntry(key);                                    // ← NEW in Part 2
            Balances running = (prev == null) ? INITIAL : prev.getValue().after;                // ← NEW in Part 2

            lastReplayed = 0;                                                                   // ← NEW in Part 2
            for (Node n : log.tailMap(key, true).values()) {   // ← NEW: SUFFIX ONLY, inclusive of the new node
                running = apply(n.type, running, n.amount);                                     // ← NEW in Part 2
                n.after = running;                                                              // ← NEW: safe to store, Balances is immutable
                lastReplayed++;                                                                 // ← NEW in Part 2
            }                                                                                   // ← NEW in Part 2
            return history();                                                                   // ← NEW in Part 2
        }                                                                                       // ← NEW in Part 2

        /** Already in accounting order — it is a TreeMap traversal, no sort needed. */         // ← NEW in Part 2
        public List<LedgerEntry> history() {                                                    // ← NEW in Part 2
            List<LedgerEntry> out = new ArrayList<>(log.size());                                // ← NEW in Part 2
            for (Map.Entry<Key, Node> e : log.entrySet())                                       // ← NEW in Part 2
                out.add(new LedgerEntry(e.getKey().sequence(),                                  // ← NEW in Part 2
                                        e.getValue().after.principalBalance(),                  // ← NEW in Part 2
                                        e.getValue().after.feeBalance()));                      // ← NEW in Part 2
            return out;                                                                         // ← NEW in Part 2
        }                                                                                       // ← NEW in Part 2

        public Balances currentBalances() {                                                     // ← NEW in Part 2
            return log.isEmpty() ? INITIAL : log.lastEntry().getValue().after;                  // ← NEW in Part 2
        }                                                                                       // ← NEW in Part 2

        public long lastReplayed() { return lastReplayed; }                                     // ← NEW in Part 2
    }                                                                                           // ← NEW in Part 2

    /* ---------------------------- run loop ---------------------------- */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-56s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        /* ---------- the published example, step by step ---------- */
        LoanLedger ledger = new LoanLedger();

        check("PAYMENT 1000 @1",
              ledger.processEvent("PAYMENT", 1000, 1),
              List.of(new LedgerEntry(1, 99_000, 0)));

        check("PAYMENT 500 @3",
              ledger.processEvent("PAYMENT", 500, 3),
              List.of(new LedgerEntry(1, 99_000, 0),
                      new LedgerEntry(3, 98_500, 0)));

        // ⭐ the backdated FEE reopens seq 3: its 500 now splits 100 fee + 400 principal
        check("FEE 100 @2 rewrites seq 3 from 98500 to 98600",
              ledger.processEvent("FEE", 100, 2),
              List.of(new LedgerEntry(1, 99_000, 0),
                      new LedgerEntry(2, 99_000, 100),
                      new LedgerEntry(3, 98_600, 0)));

        check("that backdated insert replayed 2 nodes, not 3", ledger.lastReplayed(), 2L);

        /* ---------- the replay bound, measured ---------- */
        LoanLedger inOrder = new LoanLedger();
        long worst = 0;
        for (int i = 1; i <= 10_000; i++) {
            inOrder.processEvent(i % 3 == 0 ? "FEE" : "PAYMENT", 1, i);
            worst = Math.max(worst, inOrder.lastReplayed());
        }
        check("10k in-order events: max replay per event == 1", worst, 1L);

        LoanLedger back = new LoanLedger();
        for (int i = 1; i <= 100; i++) back.processEvent("PAYMENT", 1, i * 10);
        back.processEvent("FEE", 5, 1);                       // backdated in front of all 100
        check("backdating in front of 100 events replays 101", back.lastReplayed(), 101L);
        back.processEvent("FEE", 5, 99_999);                  // appended at the end
        check("appending after them replays 1", back.lastReplayed(), 1L);

        /* ---------- ordering and correctness ---------- */
        LoanLedger s = new LoanLedger();
        s.processEvent("PAYMENT", 10, 5);
        s.processEvent("PAYMENT", 10, 2);
        s.processEvent("PAYMENT", 10, 9);
        s.processEvent("PAYMENT", 10, 1);
        check("history is in accounting order regardless of arrival",
              seqs(s.history()), List.of(1L, 2L, 5L, 9L));
        check("running balances are monotone down the ordered history",
              s.history(), List.of(new LedgerEntry(1, 99_990, 0), new LedgerEntry(2, 99_980, 0),
                                   new LedgerEntry(5, 99_970, 0), new LedgerEntry(9, 99_960, 0)));

        LoanLedger dup = new LoanLedger();
        dup.processEvent("FEE", 100, 7);
        dup.processEvent("PAYMENT", 40, 7);
        check("same accounting_sequence twice -> two rows, receipt order",
              dup.history(), List.of(new LedgerEntry(7, 100_000, 100),
                                     new LedgerEntry(7, 100_000, 60)));

        /* ---------- a backdated fee that changes a payment's SPLIT ---------- */
        LoanLedger split = new LoanLedger();
        split.processEvent("PAYMENT", 100, 10);
        check("before: 100 all to principal", split.currentBalances(), new Balances(99_900, 0));
        split.processEvent("FEE", 30, 5);
        check("after backdated fee 30 @5: 30 to fee, 70 to principal",
              split.currentBalances(), new Balances(99_930, 0));
        check("  and the fee row itself is visible mid-history",
              split.history(), List.of(new LedgerEntry(5, 100_000, 30),
                                       new LedgerEntry(10, 99_930, 0)));

        /* ---------- a fee backdated after an overpayment stays outstanding ---------- */
        LoanLedger op = new LoanLedger();
        op.processEvent("PAYMENT", 100_000, 2);
        op.processEvent("FEE", 500, 3);
        check("fee after full payoff remains a fee", op.currentBalances(), new Balances(0, 500));
        op.processEvent("FEE", 500, 1);
        // the @2 payment now spends 500 on the backdated fee, so 500 of principal survives
        check("fee backdated BEFORE the payoff is absorbed, principal keeps 500",
              op.currentBalances(), new Balances(500, 500));
        check("  history shows the absorbed fee cleared at seq 2",
              op.history(), List.of(new LedgerEntry(1, 100_000, 500),
                                    new LedgerEntry(2, 500, 0),
                                    new LedgerEntry(3, 500, 500)));

        /* ---------- validation ---------- */
        LoanLedger v = new LoanLedger();
        check("unknown event_type rejected",
              throwsIAE(() -> v.processEvent("REFUND", 1, 1)), true);
        check("negative amount rejected",
              throwsIAE(() -> v.processEvent("FEE", -1, 1)), true);
        check("  and a rejected event left NO row behind", v.history().size(), 0);

        /* ---------- replay work: the honest metric ---------- */
        // NOTE: processEvent must RETURN the whole history, which is O(n) per call on its
        // own. So total wall time is O(n^2) for ANY implementation. Count replayed NODES
        // instead to isolate what the algorithm actually contributes.
        final int N = 5_000;

        LoanLedger fwd = new LoanLedger();
        long fwdReplay = 0, t0 = System.nanoTime();
        for (int i = 1; i <= N; i++) { fwd.processEvent("PAYMENT", 1, i); fwdReplay += fwd.lastReplayed(); }
        long fwdMs = (System.nanoTime() - t0) / 1_000_000;

        LoanLedger rev = new LoanLedger();
        long revReplay = 0;
        t0 = System.nanoTime();
        for (int i = N; i >= 1; i--) { rev.processEvent("PAYMENT", 1, i); revReplay += rev.lastReplayed(); }
        long revMs = (System.nanoTime() - t0) / 1_000_000;

        check("in-order: total replay == N (linear)", fwdReplay, (long) N);
        check("reverse-order: total replay == N(N+1)/2 (quadratic)", revReplay, (long) N * (N + 1) / 2);

        System.out.printf("%n  %d events, all in order      : %,d nodes replayed, %dms%n", N, fwdReplay, fwdMs);
        System.out.printf("  %d events, all backdated     : %,d nodes replayed, %dms%n", N, revReplay, revMs);
        System.out.println("  Reverse order is the worst case and it is OUTPUT-BOUND: those rows really do");
        System.out.println("  change, so no correct implementation can touch fewer of them.");

        System.out.println("\n  final published history: " + ledger.history());
        System.out.println("\n  " + pass + " passed, " + fail + " failed");
    }

    static List<Long> seqs(List<LedgerEntry> h) {
        List<Long> out = new ArrayList<>();
        for (LedgerEntry e : h) out.add(e.accountingSequence());
        return out;
    }

    static boolean throwsIAE(Runnable r) {
        try { r.run(); return false; } catch (IllegalArgumentException e) { return true; }
    }
}
