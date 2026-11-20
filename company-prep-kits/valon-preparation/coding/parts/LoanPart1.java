import java.util.*;

/**
 * Valon domain coding — PART 1
 * "Fee and payment events against a loan."
 *
 *   FEE     : increments the fee balance
 *   PAYMENT : pays down the fee balance first, then the principal with the remainder
 *
 * Published example:
 *   Balances(100000, 0) --fee 100--> (100000, 100) --payment 500--> (99600, 0)
 *
 * Money is `long`, not `int`: Python's int is arbitrary precision, Java's is 32-bit,
 * so long is the faithful translation. In production these are minor units (cents).
 */
public class LoanPart1 {

    /**
     * A record, so it is IMMUTABLE and both operations are pure functions that
     * return a NEW value. That is not a style preference — Part 2 caches a
     * snapshot of these per ledger position, and in-place mutation would corrupt
     * every cached snapshot that aliases the same object.
     */
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

    /** A fee never touches principal. */
    static Balances processFee(Balances b, long amount) {
        requireNonNegative(amount);
        return new Balances(b.principalBalance(), b.feeBalance() + amount);
    }

    /**
     * Fee first, principal with whatever is left.
     *
     * Both min() clamps matter:
     *   - min(amount, fee)       : a payment smaller than the fee never reaches principal
     *   - min(remaining, princ.) : an overpayment cannot drive principal negative
     * The second clamp is a CLARIFY: it silently absorbs the excess. Real servicing
     * puts the excess in a suspense / unapplied-funds bucket instead of dropping it.
     */
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

    /* ---------------------------- run loop ---------------------------- */
    static int pass = 0, fail = 0;
    static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("  %-52s %s%n", label, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        Balances start = new Balances(100_000, 0);

        Balances afterFee = processFee(start, 100);
        check("published: fee 100 -> (100000, 100)", afterFee, new Balances(100_000, 100));
        check("published: then payment 500 -> (99600, 0)",
              processPayment(afterFee, 500), new Balances(99_600, 0));

        // ⭐ the inputs must be untouched, or Part 2's cached snapshots corrupt
        check("process_fee did not mutate its input",     start, new Balances(100_000, 0));
        check("process_payment did not mutate its input", afterFee, new Balances(100_000, 100));

        check("payment < fee stops at the fee",
              processPayment(new Balances(100_000, 100), 50), new Balances(100_000, 50));
        check("payment == fee leaves principal alone",
              processPayment(new Balances(100_000, 100), 100), new Balances(100_000, 0));
        check("payment 1 over the fee touches principal by 1",
              processPayment(new Balances(100_000, 100), 101), new Balances(99_999, 0));
        check("zero payment is a no-op",
              processPayment(new Balances(100_000, 100), 0), new Balances(100_000, 100));
        check("zero fee is a no-op",
              processFee(new Balances(100_000, 100), 0), new Balances(100_000, 100));
        check("no fee: payment goes straight to principal",
              processPayment(new Balances(100_000, 0), 500), new Balances(99_500, 0));

        // overpayment: clamped, and worth naming out loud
        check("overpayment clamps principal at 0",
              processPayment(new Balances(100, 0), 500), new Balances(0, 0));
        check("overpayment clears fee then clamps",
              processPayment(new Balances(100, 50), 500), new Balances(0, 0));
        check("exact payoff",
              processPayment(new Balances(100, 50), 150), new Balances(0, 0));

        // validation, not silent coercion
        check("negative payment rejected", throwsIAE(() -> processPayment(start, -1)), true);
        check("negative fee rejected",     throwsIAE(() -> processFee(start, -1)), true);

        /* ⭐ ORDER MATTERS — this is why Part 2 cannot just keep a running total */
        Balances feeThenPay = processPayment(processFee(new Balances(1000, 0), 100), 500);
        Balances payThenFee = processFee(processPayment(new Balances(1000, 0), 500), 100);
        check("fee-then-payment -> (600, 0)", feeThenPay, new Balances(600, 0));
        check("payment-then-fee -> (500, 100)", payThenFee, new Balances(500, 100));
        check("  the two orders DISAGREE", feeThenPay.equals(payThenFee), false);

        System.out.println("\n  same two events, two orders:  " + feeThenPay + "   vs   " + payThenFee);
        System.out.println("  min() makes the fold order-dependent and non-invertible, so a");
        System.out.println("  backdated event cannot be folded in as a delta. Part 2 replays.");

        System.out.println("\n  " + pass + " passed, " + fail + " failed");
    }

    static boolean throwsIAE(Runnable r) {
        try { r.run(); return false; } catch (IllegalArgumentException e) { return true; }
    }
}
