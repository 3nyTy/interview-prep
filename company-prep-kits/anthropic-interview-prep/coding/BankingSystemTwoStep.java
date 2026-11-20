/*
 * Anthropic - Staff SWE OA practice
 * Problem: "Banking System" - SECONDARY variant (AI Safety Fellow rotation).
 *
 * L1-L2 : create_account / deposit / withdraw / transfer (atomic)
 * L3    : transfer becomes a TWO-STEP hold:  transfer(...) -> transfer_id puts
 *         funds on HOLD (sender's available balance drops, recipient unchanged);
 *         transfer_accept(transfer_id) finalizes. State machine:
 *             PENDING --accept--> ACCEPTED
 *             PENDING --(now >= expiresAt)--> EXPIRED  (held funds released to sender)
 * L4    : merge_account(from, into) + generation-keyed history (recreate-safe).
 *
 * >>> ASSUMPTION (the source says "expiry semantics vary"): an unaccepted
 * transfer expires `transferTtlMs` after it was created, at which point the held
 * funds are returned to the sender. Confirm the exact window with your recruiter.
 *
 * The two genuinely different bits vs BankingSystem.java are marked // [2STEP].
 * Everything about generation-keyed history / merge is identical in spirit.
 *
 * Compile & run:  javac BankingSystemTwoStep.java && java BankingSystemTwoStep
 */

import java.util.*;

public class BankingSystemTwoStep {

    // [2STEP] a held transfer
    private static final class Hold {
        final String id;
        Account from;            // reassigned on merge so escrow follows the survivor
        Account to;
        final long amount;
        final long expiresAt;
        String state = "PENDING"; // PENDING | ACCEPTED | EXPIRED
        Hold(String id, Account from, Account to, long amount, long expiresAt) {
            this.id = id; this.from = from; this.to = to; this.amount = amount; this.expiresAt = expiresAt;
        }
    }

    private static final class Account {
        final String id;
        final long startTs;
        long removedTs = Long.MAX_VALUE;
        long balance = 0;                                    // settled, AVAILABLE balance
        final List<long[]> history = new ArrayList<>();      // {ts, balance}
        Account(String id, long ts) { this.id = id; this.startTs = ts; history.add(new long[]{ts, 0}); }
        void record(long ts) {
            long[] last = history.get(history.size() - 1);
            if (last[0] == ts) last[1] = balance; else history.add(new long[]{ts, balance});
        }
        long balanceAt(long t) {
            int lo = 0, hi = history.size() - 1, ans = 0;
            while (lo <= hi) { int m = (lo + hi) >>> 1; if (history.get(m)[0] <= t) { ans = m; lo = m + 1; } else hi = m - 1; }
            return history.get(ans)[1];
        }
    }

    private final long transferTtlMs;                                        // [2STEP] expiry window
    private final Map<String, Account> active = new HashMap<>();
    private final Map<String, List<Account>> generations = new HashMap<>();
    private final Map<String, Hold> holds = new HashMap<>();                  // [2STEP] id -> hold
    private final PriorityQueue<Hold> expiryQueue =                           // [2STEP] PENDING by due
            new PriorityQueue<>(Comparator.comparingLong(h -> h.expiresAt));
    private int transferCounter = 0;

    public BankingSystemTwoStep(long transferTtlMs) { this.transferTtlMs = transferTtlMs; }

    // [2STEP] release funds for any transfer that timed out unaccepted
    private void processExpiries(long now) {
        while (!expiryQueue.isEmpty() && expiryQueue.peek().expiresAt <= now) {
            Hold h = expiryQueue.poll();
            if (h.state.equals("PENDING")) {                 // still unaccepted -> expire & refund
                if (h.from.removedTs == Long.MAX_VALUE) { h.from.balance += h.amount; h.from.record(h.expiresAt); }
                h.state = "EXPIRED";
            }
        }
    }

    // ===================== LEVEL 1-2 =====================
    public boolean create_account(long ts, String id) {
        processExpiries(ts);
        if (active.containsKey(id)) return false;
        Account a = new Account(id, ts);
        active.put(id, a);
        generations.computeIfAbsent(id, k -> new ArrayList<>()).add(a);
        return true;
    }

    public Long deposit(long ts, String id, int amount) {
        processExpiries(ts);
        Account a = active.get(id);
        if (a == null) return null;
        a.balance += amount; a.record(ts);
        return a.balance;
    }

    public Long withdraw(long ts, String id, int amount) {
        processExpiries(ts);
        Account a = active.get(id);
        if (a == null) return null;
        if (a.balance < amount) return null;
        a.balance -= amount; a.record(ts);
        return a.balance;
    }

    // ===================== LEVEL 3: two-step transfer =====================
    // L1-2 form (atomic) for reference -- REPLACED by the hold-based version below:
    //   public Long transfer(long ts, String from, String to, int amt){
    //       ... if ok: from.balance-=amt; to.balance+=amt; return from.balance; }
    public String transfer(long ts, String fromId, String toId, int amount) {   // [2STEP]
        processExpiries(ts);
        if (fromId.equals(toId)) return null;
        Account from = active.get(fromId), to = active.get(toId);
        if (from == null || to == null) return null;
        if (from.balance < amount) return null;                   // insufficient available funds
        from.balance -= amount;                                   // hold: leave sender, escrow it
        from.record(ts);                                          // recipient UNCHANGED until accept
        String tid = "transfer" + (++transferCounter);
        Hold h = new Hold(tid, from, to, amount, ts + transferTtlMs);
        holds.put(tid, h);
        expiryQueue.add(h);
        return tid;
    }

    public boolean transfer_accept(long ts, String transferId) {  // [2STEP]
        processExpiries(ts);                                      // may have just expired
        Hold h = holds.get(transferId);
        if (h == null || !h.state.equals("PENDING")) return false; // missing / already accepted / expired
        h.to.balance += h.amount;                                 // finalize into recipient
        h.to.record(ts);
        h.state = "ACCEPTED";
        return true;
    }

    // ===================== LEVEL 4: merge + history =====================
    public boolean merge_account(long ts, String fromId, String intoId) {
        processExpiries(ts);
        if (fromId.equals(intoId)) return false;
        Account from = active.get(fromId), into = active.get(intoId);
        if (from == null || into == null) return false;
        into.balance += from.balance;                  // settled balances combine
        into.record(ts);
        for (Hold h : holds.values()) {                // pending escrow follows the survivor
            if (h.state.equals("PENDING")) {
                if (h.from == from) h.from = into;     // sender merged -> refund target becomes `into`
                if (h.to == from)   h.to = into;       // recipient merged -> credit `into` on accept
            }
        }
        from.removedTs = ts;
        active.remove(fromId);
        return true;
    }

    public Long get_balance(long ts, String id, long timeAt) {
        processExpiries(ts);
        List<Account> gens = generations.get(id);
        if (gens == null) return null;
        for (Account g : gens)
            if (g.startTs <= timeAt && timeAt < g.removedTs) return g.balanceAt(timeAt);
        return null;
    }

    // ===================== self-test =====================
    private static int passed = 0, failed = 0;
    private static void check(String n, Object got, Object want) {
        if (Objects.equals(got, want)) passed++;
        else { failed++; System.out.println("FAIL " + n + ": got " + got + " want " + want); }
    }

    public static void main(String[] args) {
        long TTL = 1000;                                  // assumed expiry window for the test
        BankingSystemTwoStep b = new BankingSystemTwoStep(TTL);
        check("create A", b.create_account(1, "A"), true);
        check("create B", b.create_account(2, "B"), true);
        check("deposit A", b.deposit(3, "A", 100), 100L);

        // hold -> accept
        String t1 = b.transfer(10, "A", "B", 40);         // A available 60, B still 0
        check("transfer id", t1, "transfer1");
        check("A held", b.get_balance(11, "A", 10), 60L);
        check("B unchanged", b.get_balance(12, "B", 11), 0L);
        check("accept", b.transfer_accept(13, t1), true); // B becomes 40
        check("B after accept", b.get_balance(14, "B", 13), 40L);
        check("double accept", b.transfer_accept(15, t1), false);

        // hold -> expire -> reclaim (a later op past TTL releases the held funds)
        String t2 = b.transfer(20, "A", "B", 30);         // A available 30
        check("A held2", b.get_balance(21, "A", 20), 30L);
        check("reclaim via later deposit", b.deposit(20 + TTL + 5, "A", 1), 61L); // 30 held + 30 back + 1
        check("accept expired -> false", b.transfer_accept(20 + TTL + 6, t2), false);

        // merge with a pending outgoing hold, then accept credits the survivor's counterpart
        BankingSystemTwoStep m = new BankingSystemTwoStep(100000);
        m.create_account(1, "C");
        m.create_account(2, "D");
        m.create_account(3, "E");
        m.deposit(4, "C", 100);
        String tc = m.transfer(5, "C", "E", 25);          // C available 75, escrow 25 -> E
        check("merge C into D", m.merge_account(6, "C", "D"), true);  // D += 75; C gone; hold.from -> D
        check("D balance", m.get_balance(7, "D", 6), 75L);
        check("C removed", m.get_balance(8, "C", 6), null);
        check("accept post-merge credits E", m.transfer_accept(9, tc), true);
        check("E got 25", m.get_balance(10, "E", 9), 25L);

        // generation-keyed recreate
        check("recreate C", m.create_account(11, "C"), true);
        check("new C @11", m.get_balance(12, "C", 11), 0L);
        check("old C @4 preserved", m.get_balance(13, "C", 4), 100L);
        check("C gap @6", m.get_balance(14, "C", 6), null);   // removed at 6, recreated at 11

        System.out.println("\nPassed " + passed + ", Failed " + failed);
        if (failed == 0) System.out.println("ALL TWO-STEP TESTS PASSED");
    }
}
