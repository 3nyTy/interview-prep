import java.util.*;

/**
 * Valon domain coding — PART 3
 * The escalation Part 1 was built for: MORE BALANCE TYPES, paid in priority order.
 *
 * Part 1 hardcoded "fee first, then principal" as two lines of control flow. A real
 * servicer has a waterfall: fees, interest, principal, escrow. So the priority becomes
 * DATA (an enum's declaration order) instead of code, and adding a bucket costs one
 * enum constant and zero logic changes.
 *
 * Also here, because the TreeMap makes it four lines: balancesAsOf(sequence), the
 * point-in-time query an accounting system always ends up needing.
 *
 * Lines new or changed since Part 2 are marked. Unmarked lines are byte-identical
 * to Part 2.
 */
public class LoanPart3 {

    /**                                                                                    // ← NEW in Part 3
     * DECLARATION ORDER IS THE PAYMENT WATERFALL. The prompt says fee-first, so FEE        // ← NEW in Part 3
     * leads. Real US servicing often runs interest -> principal -> escrow -> late fees;    // ← NEW in Part 3
     * that is a business policy, and it is now a one-line reorder. Worth asking which.     // ← NEW in Part 3
     *                                                                                      // ← NEW in Part 3
     * SUSPENSE is deliberately NOT in the waterfall: it is a credit we owe the borrower,    // ← NEW in Part 3
     * not a debt they owe us, so a payment never pays it down.                              // ← NEW in Part 3
     */                                                                                    // ← NEW in Part 3
    enum Bucket { FEE, INTEREST, PRINCIPAL, ESCROW, SUSPENSE }                              // ← NEW in Part 3

    static final Bucket[] WATERFALL =                                                       // ← NEW in Part 3
            { Bucket.FEE, Bucket.INTEREST, Bucket.PRINCIPAL, Bucket.ESCROW };                // ← NEW in Part 3

    /** Same immutability contract as Part 1, now over N buckets instead of two. */         // ← CHANGED: was (long, long)
    record Balances(Map<Bucket, Long> byBucket) {                                            // ← CHANGED in Part 3
        Balances(Map<Bucket, Long> byBucket) {                                               // ← NEW in Part 3
            EnumMap<Bucket, Long> copy = new EnumMap<>(Bucket.class);                        // ← NEW: defensive copy, caller cannot mutate us
            for (Bucket k : Bucket.values()) {                                               // ← NEW in Part 3
                long v = byBucket.getOrDefault(k, 0L);                                       // ← NEW: absent bucket == 0
                if (v < 0) throw new IllegalArgumentException(k + " cannot go negative: " + v); // ← NEW in Part 3
                copy.put(k, v);                                                              // ← NEW in Part 3
            }                                                                                // ← NEW in Part 3
            this.byBucket = Collections.unmodifiableMap(copy);                                // ← NEW in Part 3
        }                                                                                    // ← NEW in Part 3

        long get(Bucket k) { return byBucket.get(k); }                                       // ← NEW in Part 3

        /** Part 1 and 2 called these; keep every existing call site working. */             // ← NEW in Part 3
        long principalBalance() { return get(Bucket.PRINCIPAL); }                            // ← NEW in Part 3
        long feeBalance()       { return get(Bucket.FEE); }                                  // ← NEW in Part 3

        /** Total debt outstanding — SUSPENSE excluded, it is a credit. */                   // ← NEW in Part 3
        long totalDebt() {                                                                   // ← NEW in Part 3
            long t = 0;                                                                      // ← NEW in Part 3
            for (Bucket k : WATERFALL) t += get(k);                                          // ← NEW in Part 3
            return t;                                                                        // ← NEW in Part 3
        }                                                                                    // ← NEW in Part 3

        Balances with(Bucket k, long v) {                                                    // ← NEW in Part 3
            EnumMap<Bucket, Long> next = new EnumMap<>(byBucket);                            // ← NEW in Part 3
            next.put(k, v);                                                                  // ← NEW in Part 3
            return new Balances(next);                                                       // ← NEW in Part 3
        }                                                                                    // ← NEW in Part 3

        static Balances of(long principal) {                                                 // ← NEW in Part 3
            return new Balances(Map.of(Bucket.PRINCIPAL, principal));                        // ← NEW in Part 3
        }                                                                                    // ← NEW in Part 3

        @Override public String toString() {
            StringJoiner j = new StringJoiner(", ");                                         // ← CHANGED: prints every non-zero bucket
            for (Bucket k : Bucket.values()) if (get(k) != 0) j.add(k + ": " + get(k));      // ← CHANGED in Part 3
            return j.length() == 0 ? "all zero" : j.toString();                              // ← CHANGED in Part 3
        }
    }

    /** Part 1's processFee generalised: charge ANY bucket. */                              // ← NEW in Part 3
    static Balances processCharge(Balances b, Bucket k, long amount) {                       // ← NEW in Part 3
        requireNonNegative(amount);                                                          // ← NEW in Part 3
        if (k == Bucket.SUSPENSE)                                                            // ← NEW in Part 3
            throw new IllegalArgumentException("SUSPENSE is not chargeable");                 // ← NEW in Part 3
        return b.with(k, b.get(k) + amount);                                                 // ← NEW in Part 3
    }

    /** Part 1's signature, unchanged for callers. */                                       // ← NEW in Part 3
    static Balances processFee(Balances b, long amount) {
        return processCharge(b, Bucket.FEE, amount);   // ← CHANGED: was an inline field bump
    }

    /**
     * ⭐ THE PART 3 CHANGE. Part 1's two hardcoded min() clamps become one loop over
     *    WATERFALL, so the priority lives in the enum, not in the branch structure.
     *    Anything left after the last bucket goes to SUSPENSE rather than being
     *    silently dropped — a servicer must never destroy money.
     */
    static Balances processPayment(Balances b, long amount) {
        requireNonNegative(amount);
        EnumMap<Bucket, Long> next = new EnumMap<>(b.byBucket());   // ← NEW: working copy, b stays immutable
        long left = amount;                                         // ← CHANGED: was two named locals toFee/toPrincipal
        for (Bucket k : WATERFALL) {                                // ← NEW: priority is data
            if (left == 0) break;                                   // ← NEW: stop early, nothing left to allocate
            long applied = Math.min(left, next.get(k));             // ← CHANGED: same min() rule, now per bucket
            next.put(k, next.get(k) - applied);                     // ← NEW in Part 3
            left -= applied;                                        // ← NEW in Part 3
        }
        if (left > 0) next.put(Bucket.SUSPENSE, next.get(Bucket.SUSPENSE) + left); // ← NEW: overpayment is held, not clamped away
        return new Balances(next);
    }

    private static void requireNonNegative(long amount) {
        if (amount < 0) throw new IllegalArgumentException("amount cannot be negative: " + amount);
    }

    /** PAYMENT runs the waterfall; every other event type charges the bucket it names. */  // ← CHANGED in Part 3
    static Balances apply(String eventType, Balances b, long amount) {
        if (eventType.equals("PAYMENT")) return processPayment(b, amount);   // ← CHANGED: no switch needed now
        for (Bucket k : WATERFALL)                                           // ← NEW: "FEE"/"INTEREST"/"ESCROW" all work
            if (k.name().equals(eventType)) return processCharge(b, k, amount); // ← NEW: adding a type costs ZERO code
        throw new IllegalArgumentException("unknown event_type: " + eventType);
    }

    /** Row shape from Part 2 preserved, plus the full bucket view. */                      // ← CHANGED in Part 3
    public record LedgerEntry(long accountingSequence, Balances balances) {                  // ← CHANGED: carries Balances now
        long principalBalance() { return balances.principalBalance(); }                      // ← NEW in Part 3
        long feeBalance()       { return balances.feeBalance(); }                            // ← NEW in Part 3
        @Override public String toString() {
            return "(" + accountingSequence + ", " + principalBalance() + ", " + feeBalance() + ")";
        }
        /** Every bucket, for when the interviewer asks to see interest/escrow. */           // ← NEW in Part 3
        String full() { return "(" + accountingSequence + ", " + balances + ")"; }           // ← NEW in Part 3
    }

    public static final class LoanLedger {

        private static final Balances INITIAL = Balances.of(100_000);   // ← CHANGED: was new Balances(100_000, 0)

        private record Key(long sequence, long arrival) implements Comparable<Key> {
            public int compareTo(Key o) {
                int c = Long.compare(sequence, o.sequence);
                return c != 0 ? c : Long.compare(arrival, o.arrival);
            }
        }

        private static final class Node {
            final String type;
            final long amount;
            Balances after;
            Node(String type, long amount) { this.type = type; this.amount = amount; }
        }

        private final NavigableMap<Key, Node> log = new TreeMap<>();
        private long arrivals = 0;
        private long lastReplayed = 0;

        public List<LedgerEntry> processEvent(String eventType, long amount,
                                              long accountingSequence) {
            apply(eventType, INITIAL, amount);

            Key key = new Key(accountingSequence, arrivals++);
            log.put(key, new Node(eventType, amount));

            Map.Entry<Key, Node> prev = log.lowerEntry(key);
            Balances running = (prev == null) ? INITIAL : prev.getValue().after;

            lastReplayed = 0;
            for (Node n : log.tailMap(key, true).values()) {
                running = apply(n.type, running, n.amount);
                n.after = running;
                lastReplayed++;
            }
            return history();
        }

        public List<LedgerEntry> history() {
            List<LedgerEntry> out = new ArrayList<>(log.size());
            for (Map.Entry<Key, Node> e : log.entrySet())
                out.add(new LedgerEntry(e.getKey().sequence(), e.getValue().after));  // ← CHANGED: passes Balances through
            return out;
        }

        public Balances currentBalances() {
            return log.isEmpty() ? INITIAL : log.lastEntry().getValue().after;
        }

        /**                                                                                 // ← NEW in Part 3
         * Point-in-time: what did the books say as of this accounting sequence?             // ← NEW in Part 3
         * O(log n), and free — the snapshots are already cached per position. MAX_VALUE      // ← NEW in Part 3
         * as the arrival tiebreak means "after every event that shares this sequence".       // ← NEW in Part 3
         */                                                                                 // ← NEW in Part 3
        public Balances balancesAsOf(long accountingSequence) {                              // ← NEW in Part 3
            Map.Entry<Key, Node> e = log.floorEntry(new Key(accountingSequence, Long.MAX_VALUE)); // ← NEW in Part 3
            return e == null ? INITIAL : e.getValue().after;                                 // ← NEW: before any event == opening balance
        }                                                                                    // ← NEW in Part 3

        public long lastReplayed() { return lastReplayed; }
    }

    /* ---------------------------- run loop ---------------------------- */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-58s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        /* ---------- Part 2's published example must be BIT-IDENTICAL ---------- */
        LoanLedger ledger = new LoanLedger();
        check("published step 1", ledger.processEvent("PAYMENT", 1000, 1).toString(),
              "[(1, 99000, 0)]");
        check("published step 2", ledger.processEvent("PAYMENT", 500, 3).toString(),
              "[(1, 99000, 0), (3, 98500, 0)]");
        check("published step 3 (backdated FEE)", ledger.processEvent("FEE", 100, 2).toString(),
              "[(1, 99000, 0), (2, 99000, 100), (3, 98600, 0)]");
        check("replay was still only the suffix", ledger.lastReplayed(), 2L);

        /* ---------- the waterfall ---------- */
        Balances owing = new Balances(Map.of(Bucket.FEE, 10L, Bucket.INTEREST, 20L,
                                             Bucket.PRINCIPAL, 30L, Bucket.ESCROW, 40L));
        check("payment 5 stops inside FEE",
              processPayment(owing, 5).toString(), "FEE: 5, INTEREST: 20, PRINCIPAL: 30, ESCROW: 40");
        check("payment 10 clears FEE exactly",
              processPayment(owing, 10).toString(), "INTEREST: 20, PRINCIPAL: 30, ESCROW: 40");
        check("payment 35 spans FEE+INTEREST and dents PRINCIPAL",
              processPayment(owing, 35).toString(), "PRINCIPAL: 25, ESCROW: 40");
        check("payment 100 clears every bucket",
              processPayment(owing, 100).toString(), "all zero");
        check("payment 130 leaves 30 in SUSPENSE",
              processPayment(owing, 130).toString(), "SUSPENSE: 30");
        check("SUSPENSE is never paid down",
              processPayment(processPayment(owing, 130), 50).toString(), "SUSPENSE: 80");

        /* ---------- new event types cost ZERO code ---------- */
        LoanLedger multi = new LoanLedger();
        multi.processEvent("INTEREST", 400, 1);
        multi.processEvent("ESCROW", 250, 2);
        multi.processEvent("FEE", 50, 3);
        check("INTEREST/ESCROW/FEE all charge their bucket",
              multi.currentBalances().toString(),
              "FEE: 50, INTEREST: 400, PRINCIPAL: 100000, ESCROW: 250");
        multi.processEvent("PAYMENT", 500, 4);
        check("payment walks FEE(50) -> INTEREST(400) -> PRINCIPAL(50)",
              multi.currentBalances().toString(),
              "PRINCIPAL: 99950, ESCROW: 250");
        check("unknown event_type still rejected",
              throwsIAE(() -> multi.processEvent("REFUND", 1, 9)), true);
        check("SUSPENSE is not chargeable",
              throwsIAE(() -> processCharge(owing, Bucket.SUSPENSE, 1)), true);

        /* ---------- ⭐ a backdated INTEREST reroutes a later payment ---------- */
        LoanLedger re = new LoanLedger();
        re.processEvent("PAYMENT", 300, 10);
        check("before: 300 all to principal", re.currentBalances().toString(), "PRINCIPAL: 99700");
        re.processEvent("INTEREST", 120, 5);
        check("after backdated interest 120: 120 to interest, 180 to principal",
              re.currentBalances().toString(), "PRINCIPAL: 99820");
        check("  mid-history row shows the interest outstanding",
              re.history().get(0).full(),
              "(5, INTEREST: 120, PRINCIPAL: 100000)");

        /* ---------- point-in-time query ---------- */
        check("asOf(0) is the opening balance", ledger.balancesAsOf(0).toString(), "PRINCIPAL: 100000");
        check("asOf(1)", ledger.balancesAsOf(1).toString(), "PRINCIPAL: 99000");
        check("asOf(2) sees the fee", ledger.balancesAsOf(2).toString(), "FEE: 100, PRINCIPAL: 99000");
        check("asOf(3)", ledger.balancesAsOf(3).toString(), "PRINCIPAL: 98600");
        check("asOf between events floors to the earlier one",
              ledger.balancesAsOf(2).toString(), ledger.balancesAsOf(2).toString());
        check("asOf past the end == current",
              ledger.balancesAsOf(999).toString(), ledger.currentBalances().toString());

        /* ---------- ⭐ money is conserved, never destroyed ---------- */
        LoanLedger cons = new LoanLedger();
        Random rnd = new Random(42);
        long charged = 0, paid = 0;
        String[] types = { "FEE", "INTEREST", "ESCROW", "PAYMENT", "PAYMENT" };
        for (int i = 0; i < 4000; i++) {
            String t = types[rnd.nextInt(types.length)];
            long amt = rnd.nextInt(2000);
            cons.processEvent(t, amt, rnd.nextInt(4000));    // sequences collide and backdate on purpose
            if (t.equals("PAYMENT")) paid += amt; else charged += amt;
        }
        Balances end = cons.currentBalances();
        long suspense = end.get(Bucket.SUSPENSE);
        check("debt == opening + charged - (paid - suspense)",
              end.totalDebt(), 100_000 + charged - (paid - suspense));
        check("  nothing was lost to clamping", end.totalDebt() >= 0, true);

        /* ---------- immutability ---------- */
        Balances snap = new Balances(Map.of(Bucket.FEE, 10L, Bucket.PRINCIPAL, 100L));
        processPayment(snap, 999);
        check("processPayment did not mutate its input", snap.toString(), "FEE: 10, PRINCIPAL: 100");
        check("byBucket() is unmodifiable",
              throwsUOE(() -> snap.byBucket().put(Bucket.FEE, 0L)), true);

        System.out.println("\n  published history, full buckets:");
        for (LedgerEntry e : ledger.history()) System.out.println("    " + e.full());
        System.out.printf("%n  conservation run: charged %,d  paid %,d  suspense %,d  debt %,d%n",
                          charged, paid, suspense, end.totalDebt());

        System.out.println("\n  " + pass + " passed, " + fail + " failed");
    }

    static boolean throwsIAE(Runnable r) {
        try { r.run(); return false; } catch (IllegalArgumentException e) { return true; }
    }
    static boolean throwsUOE(Runnable r) {
        try { r.run(); return false; } catch (UnsupportedOperationException e) { return true; }
    }
}
