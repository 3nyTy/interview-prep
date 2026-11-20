/*
 * Anthropic - Staff SWE OA practice
 * Problem: "Banking System" (CodeSignal 4-level) - cashback / top_spenders variant.
 *
 * This is the FULLY-SPECIFIED, in-rotation variant (Prompt 1 + the "alternate
 * canonical variant" in Prompt 2). The transfer/transfer_accept "Fellow" variant
 * lives in BankingSystemTwoStep.java.
 *
 * Levels (search "LEVEL n" for each part; later levels only ADD):
 *   L1 create_account / deposit / transfer
 *   L2 top_spenders (rank by outgoing)
 *   L3 pay (+2% cashback after 24h) / get_payment_status
 *   L4 merge_accounts / get_balance (historical, generation-keyed)
 *
 * Design notes (why this is optimal & correct):
 *   - Timestamps are strictly increasing, so cashbacks are processed LAZILY:
 *     a min-heap by due-time, drained at the start of every operation. No real
 *     scheduler needed; O(log k) amortized per cashback.
 *   - Each account id maps to a list of GENERATIONS. A generation is one live
 *     span [startTs, removedTs). Merge ends a generation; re-creating the id
 *     starts a fresh one. This is the only representation that survives the
 *     "merge then recreate the same id" hidden test.
 *   - Per-generation balance history (sorted (ts,balance)) answers get_balance
 *     in O(log h) via binary search and naturally reflects cashback refunds
 *     (they append a checkpoint at their due timestamp).
 *
 * Compile & run:  javac BankingSystem.java && java BankingSystem
 */

import java.util.*;

public class BankingSystem {

    private static final long DAY_MS = 24L * 60 * 60 * 1000;   // 86_400_000 (L3)

    // ----- L3: a scheduled cashback payment -----
    private static final class Payment {
        final String id;
        final long cashback;     // floor(amount * 0.02)
        final long dueTs;        // pay timestamp + 24h
        Account account;         // owner; reassigned on merge so cashback follows (L4)
        boolean cashbackApplied = false;
        Payment(String id, long cashback, long dueTs, Account account) {
            this.id = id; this.cashback = cashback; this.dueTs = dueTs; this.account = account;
        }
    }

    // ----- one generation (one live span) of an account id -----
    private static final class Account {
        final String id;
        final long startTs;                 // when this generation was created
        long removedTs = Long.MAX_VALUE;    // set on merge; MAX_VALUE while live (L4)
        long balance = 0;                   // L1 (long: amounts sum can exceed int)
        long outgoing = 0;                  // L2: transferred-out + paid
        final List<long[]> history = new ArrayList<>();      // L4: sorted {ts, balance}
        final Map<String, Payment> payments = new HashMap<>(); // L3/L4: id -> payment

        Account(String id, long ts) {
            this.id = id;
            this.startTs = ts;
            history.add(new long[]{ts, 0});  // the account exists with balance 0 at creation
        }

        /** Append (or overwrite if same ts) a balance checkpoint. (L4) */
        void record(long ts) {
            long[] last = history.get(history.size() - 1);
            if (last[0] == ts) last[1] = balance;            // multiple effects at one ts
            else history.add(new long[]{ts, balance});
        }

        /** Balance at the latest checkpoint with ts <= timeAt (binary search). (L4) */
        long balanceAt(long timeAt) {
            int lo = 0, hi = history.size() - 1, ans = 0;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (history.get(mid)[0] <= timeAt) { ans = mid; lo = mid + 1; }
                else hi = mid - 1;
            }
            return history.get(ans)[1];
        }
    }

    // live id -> current generation
    private final Map<String, Account> active = new HashMap<>();
    // id -> every generation ever (for historical get_balance after removal/recreate) (L4)
    private final Map<String, List<Account>> generations = new HashMap<>();
    // cashbacks ordered by due time (L3)
    private final PriorityQueue<Payment> cashbackQueue =
            new PriorityQueue<>(Comparator.comparingLong(p -> p.dueTs));
    private int paymentCounter = 0;                          // L3: "payment1", "payment2", ...

    /** L3: apply every cashback due at or before `timestamp` (driven by the clock). */
    private void processCashbacks(long timestamp) {
        while (!cashbackQueue.isEmpty() && cashbackQueue.peek().dueTs <= timestamp) {
            Payment p = cashbackQueue.poll();
            Account acct = p.account;                         // follows merges (L4)
            if (acct != null && acct.removedTs == Long.MAX_VALUE) {   // refund only if still alive
                acct.balance += p.cashback;
                acct.record(p.dueTs);                         // historical checkpoint at due time
            }
            p.cashbackApplied = true;                         // status flips even if amount was 0
        }
    }

    // =====================================================================
    // LEVEL 1 - basic account operations
    // =====================================================================
    public boolean create_account(long timestamp, String accountId) {
        processCashbacks(timestamp);
        if (active.containsKey(accountId)) return false;      // already exists
        Account acct = new Account(accountId, timestamp);
        active.put(accountId, acct);
        generations.computeIfAbsent(accountId, k -> new ArrayList<>()).add(acct);
        return true;
    }

    public Long deposit(long timestamp, String accountId, int amount) {
        processCashbacks(timestamp);
        Account acct = active.get(accountId);
        if (acct == null) return null;                        // no such account
        acct.balance += amount;
        acct.record(timestamp);
        return acct.balance;
    }

    public Long transfer(long timestamp, String sourceId, String targetId, int amount) {
        processCashbacks(timestamp);
        if (sourceId.equals(targetId)) return null;           // same account
        Account src = active.get(sourceId), tgt = active.get(targetId);
        if (src == null || tgt == null) return null;          // missing account
        if (src.balance < amount) return null;                // insufficient funds
        src.balance -= amount;
        tgt.balance += amount;
        src.outgoing += amount;                               // L2: counts as outgoing
        src.record(timestamp);
        tgt.record(timestamp);
        return src.balance;
    }

    // =====================================================================
    // LEVEL 2 - top spenders  (NEW: top_spenders; transfer/pay feed `outgoing`)
    // =====================================================================
    public List<String> top_spenders(long timestamp, int n) {
        processCashbacks(timestamp);
        List<Account> accts = new ArrayList<>(active.values());   // only LIVE accounts rank
        accts.sort((a, b) -> a.outgoing != b.outgoing
                ? Long.compare(b.outgoing, a.outgoing)            // outgoing desc
                : a.id.compareTo(b.id));                          // tie -> id asc
        List<String> res = new ArrayList<>();
        for (int i = 0; i < Math.min(n, accts.size()); i++)
            res.add(accts.get(i).id + "(" + accts.get(i).outgoing + ")");  // exact format
        return res;
    }

    // =====================================================================
    // LEVEL 3 - scheduled payments with cashback  (NEW: pay, get_payment_status)
    // =====================================================================
    public String pay(long timestamp, String accountId, int amount) {
        processCashbacks(timestamp);
        Account acct = active.get(accountId);
        if (acct == null) return null;                        // no such account
        if (acct.balance < amount) return null;               // insufficient funds
        acct.balance -= amount;
        acct.outgoing += amount;                              // L2: withdrawals count as outgoing
        acct.record(timestamp);
        String pid = "payment" + (++paymentCounter);
        long cashback = (long) amount * 2 / 100;              // floor(amount * 0.02)
        Payment p = new Payment(pid, cashback, timestamp + DAY_MS, acct);
        acct.payments.put(pid, p);
        cashbackQueue.add(p);
        return pid;
    }

    public String get_payment_status(long timestamp, String accountId, String payment) {
        processCashbacks(timestamp);
        Account acct = active.get(accountId);
        if (acct == null) return null;                        // no such account
        Payment p = acct.payments.get(payment);               // only finds payments this acct OWNS
        if (p == null) return null;                           // missing, or belongs to another id
        return p.cashbackApplied ? "CASHBACK_RECEIVED" : "IN_PROGRESS";
    }

    // =====================================================================
    // LEVEL 4 - merging + historical balances  (NEW: merge_accounts, get_balance)
    // =====================================================================
    public boolean merge_accounts(long timestamp, String id1, String id2) {
        processCashbacks(timestamp);
        if (id1.equals(id2)) return false;
        Account a1 = active.get(id1), a2 = active.get(id2);
        if (a1 == null || a2 == null) return false;

        a1.balance += a2.balance;                 // balances combine...
        a1.record(timestamp);                     // ...recorded on a1 at merge time
        a1.outgoing += a2.outgoing;               // L2 tally combines

        for (Payment p : a2.payments.values()) {  // a2's payments become queryable via a1,
            p.account = a1;                        // and their pending cashbacks redirect to a1
            a1.payments.put(p.id, p);
        }

        a2.removedTs = timestamp;                 // end a2's generation (history kept for time<T)
        active.remove(id2);                       // a2 no longer live; id2 can be recreated
        return true;
    }

    public Long get_balance(long timestamp, String accountId, long timeAt) {
        processCashbacks(timestamp);
        List<Account> gens = generations.get(accountId);
        if (gens == null) return null;
        for (Account g : gens) {                  // generations are disjoint in time -> 0 or 1 match
            if (g.startTs <= timeAt && timeAt < g.removedTs) return g.balanceAt(timeAt);
        }
        return null;                              // didn't exist at timeAt
    }

    // =====================================================================
    // Self-verifying tests
    // =====================================================================
    private static int passed = 0, failed = 0;
    private static void check(String name, Object got, Object want) {
        if (Objects.equals(got, want)) { passed++; }
        else { failed++; System.out.println("FAIL " + name + ": got " + got + " want " + want); }
    }

    public static void main(String[] args) {
        // ---- Level 1 ----
        BankingSystem b = new BankingSystem();
        check("create A", b.create_account(1, "A"), true);
        check("create A dup", b.create_account(2, "A"), false);
        check("deposit missing", b.deposit(3, "Z", 100), null);
        check("deposit A", b.deposit(4, "A", 100), 100L);
        check("create B", b.create_account(5, "B"), true);
        check("transfer self", b.transfer(6, "A", "A", 10), null);
        check("transfer missing", b.transfer(7, "A", "Z", 10), null);
        check("transfer insufficient", b.transfer(8, "A", "B", 1000), null);
        check("transfer ok", b.transfer(9, "A", "B", 40), 60L);   // A:60 B:40

        // ---- Level 2 ----
        check("top2", b.top_spenders(10, 2), List.of("A(40)", "B(0)"));   // A spent 40, B 0
        check("top fewer-than-n", b.top_spenders(11, 5).size(), 2);

        // ---- Level 3 ----
        check("pay missing", b.pay(12, "Z", 10), null);
        check("pay insufficient", b.pay(13, "A", 10_000), null);
        check("pay A", b.pay(14, "A", 50), "payment1");           // A:10, cashback floor(1.0)=1
        check("A outgoing incl pay", b.top_spenders(15, 1), List.of("A(90)"));  // 40 + 50
        check("status in-progress", b.get_payment_status(16, "A", "payment1"), "IN_PROGRESS");
        check("status wrong acct", b.get_payment_status(17, "B", "payment1"), null);
        long due = 14 + DAY_MS;
        check("status received", b.get_payment_status(due, "A", "payment1"), "CASHBACK_RECEIVED");
        check("balance after cashback", b.get_balance(due + 1, "A", due), 11L);  // 10 + 1 cashback
        check("cashback not in outgoing", b.top_spenders(due + 2, 1), List.of("A(90)"));

        // ---- Level 4: merge + historical + recreate ----
        BankingSystem m = new BankingSystem();
        m.create_account(1, "A");
        m.create_account(2, "B");
        m.deposit(3, "A", 100);
        m.deposit(4, "B", 200);
        check("L4 pay B", m.pay(5, "B", 50), "payment1");        // B:150, out 50, cashback 1 @ due
        check("L4 transfer A->B", m.transfer(6, "A", "B", 30), 70L);  // A:70 out30, B:180
        check("L4 merge", m.merge_accounts(7, "A", "B"), true);  // A:250 out80, B gone, payment1->A
        check("L4 status via A", m.get_payment_status(8, "A", "payment1"), "IN_PROGRESS");
        check("L4 status via B(gone)", m.get_payment_status(9, "B", "payment1"), null);
        check("L4 top after merge", m.top_spenders(10, 5), List.of("A(80)"));
        check("L4 bal A@6", m.get_balance(11, "A", 6), 70L);     // before merge
        check("L4 bal A@7", m.get_balance(12, "A", 7), 250L);    // at merge
        check("L4 bal B@4", m.get_balance(13, "B", 4), 200L);    // B historical (pre-merge)
        check("L4 bal B@7", m.get_balance(14, "B", 7), null);    // B removed at 7
        check("L4 recreate B", m.create_account(15, "B"), true); // new generation, balance 0
        check("L4 newB@15", m.get_balance(16, "B", 15), 0L);
        check("L4 oldB@4 still", m.get_balance(17, "B", 4), 200L);  // old generation preserved
        check("L4 gap B@14", m.get_balance(19, "B", 14), null);  // between merge(7) and recreate(15)
        long bdue = 5 + DAY_MS;                                  // B's payment1 cashback -> now A
        check("L4 cashback->A", m.get_payment_status(bdue, "A", "payment1"), "CASHBACK_RECEIVED");
        check("L4 A balance +cashback", m.get_balance(bdue + 1, "A", bdue), 251L);  // 250 + 1
        check("L4 top w/ recreated B", m.top_spenders(bdue + 2, 5), List.of("A(80)", "B(0)"));

        System.out.println("\nPassed " + passed + ", Failed " + failed);
        if (failed == 0) System.out.println("ALL BANKING-SYSTEM TESTS PASSED");
    }
}
