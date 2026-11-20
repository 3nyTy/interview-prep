import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Robinhood — "Fractional Shares Trading & Inventory Updates"
 * Source: 1point3acres 013a4cda-6619-4f71-abd9-f110618752d8
 *
 * Process chronological BUY/SELL instructions, track the position per ticker,
 * support fractional shares. Up to 1e5 instructions and 1e5 distinct tickers.
 *
 * Given example: BUY AAPL 1.5, BUY TSLA 2, SELL AAPL 0.4
 *                => AAPL: 1.1, TSLA: 2
 *
 * ---------------------------------------------------------------------------
 * ⭐ THIS PROBLEM IS A FLOATING-POINT TRAP AND NOTHING ELSE.
 * The algorithm is a hash map. The grade is entirely in the arithmetic.
 *
 * ⚠️ BUT BE PRECISE ABOUT *WHY*, because the tempting claim is FALSE:
 *
 *     1.5 - 0.4 is BIT-IDENTICAL to the literal 1.1 in IEEE-754 double.
 *     Both are exactly 1.100000000000000088817841970012523233890533447265625.
 *     So `(1.5 - 0.4) == 1.1` is TRUE, and the 3-trade sample PASSES with double.
 *
 * Do NOT walk in claiming the sample disproves double. An interviewer who knows
 * floating point will correct you and you will have spent your best point on a
 * false premise.
 *
 * The real argument is ACCUMULATION across the stated 1e5 instructions. A single
 * operation often rounds back to the nice value; long chains do not:
 *
 *     ten additions of 0.1  -> 0.9999999999999999      (!= 1.0)
 *     0.1+0.1+0.1 - 0.3     -> 5.551115123125783E-17   (!= 0)
 *
 * The sentence to say: *"the example survives double, but the constraints don't.
 * At 100,000 accumulating operations the error compounds, and a position that
 * should be flat reads as non-zero — at a brokerage that's a reconciliation
 * break, and it's unfalsifiable after the fact."*  part2_broken() shows it.
 *
 * Two correct answers. Prefer scaled integers and say why:
 *   - scaled long (micro-shares): exact, fast, and what real trading systems do
 *   - BigDecimal: exact, but allocates and is ~10x slower
 *
 * ⚠️ Parse the decimal STRING straight to a scaled long. Do NOT parse to double
 * first — that reintroduces the very error you are avoiding. part3 does it
 * digit-by-digit for exactly this reason.
 * ---------------------------------------------------------------------------
 */
public class FractionalShares {

    /** 6 decimal places. Robinhood quotes fractional shares to 1e-6. */
    static final int SCALE_DIGITS = 6;
    static final long SCALE = 1_000_000L;

    /* ===================================================================
     * PART 1 — Whole shares only. The warm-up the interviewer opens with.
     *
     * Nothing subtle here: a HashMap and a running sum. Get the over-sell
     * question asked NOW, before fractions muddy it.
     * =================================================================== */
    static Map<String, Long> part1(List<String[]> trades) {          // {action, ticker, qty}
        Map<String, Long> pos = new HashMap<>();
        for (String[] t : trades) {
            long q = Long.parseLong(t[2]);
            if (t[0].equals("BUY")) pos.merge(t[1], q, Long::sum);
            else                    pos.merge(t[1], -q, Long::sum);
        }
        return pos;
    }

    /* ===================================================================
     * PART 2 — Fractional shares with double. ⛔ THE WRONG ANSWER.
     *
     * CHANGES FROM PART 1: quantity is a double.
     *
     * Kept deliberately so you can SHOW the failure. ⚠️ But show it with the
     * ACCUMULATION case, not the 3-trade sample — the sample passes. Run
     * main() and point at "ten additions of 0.1", not at AAPL.
     * =================================================================== */
    static Map<String, Double> part2_broken(List<String[]> trades) {
        Map<String, Double> pos = new HashMap<>();
        for (String[] t : trades) {
            double q = Double.parseDouble(t[2]);
            if (t[0].equals("BUY")) pos.merge(t[1], q, Double::sum);
            else                    pos.merge(t[1], -q, Double::sum);
        }
        return pos;
    }

    /* ===================================================================
     * PART 3 — Scaled integers. ✅ THE ANSWER.
     *
     * CHANGES FROM PART 2:
     *   - quantity is a long in units of 1e-6 shares ("micro-shares")
     *   - parseScaled() converts the decimal STRING directly, never via double
     *   - all arithmetic is integer, therefore exact
     *   - formatting back to a decimal string is also integer work
     * =================================================================== */
    static long parseScaled(String s) {
        s = s.trim();
        boolean neg = false;
        int i = 0;
        if (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            neg = s.charAt(i) == '-';
            i++;
        }
        long whole = 0;
        boolean anyDigit = false;
        while (i < s.length() && s.charAt(i) != '.') {
            char c = s.charAt(i++);
            if (c < '0' || c > '9') throw new NumberFormatException("bad quantity: " + s);
            whole = whole * 10 + (c - '0');
            anyDigit = true;
        }
        long frac = 0;
        int fracDigits = 0;
        if (i < s.length() && s.charAt(i) == '.') {
            i++;
            while (i < s.length()) {
                char c = s.charAt(i++);
                if (c < '0' || c > '9') throw new NumberFormatException("bad quantity: " + s);
                anyDigit = true;
                if (fracDigits < SCALE_DIGITS) {          // extra precision is truncated
                    frac = frac * 10 + (c - '0');
                    fracDigits++;
                }
            }
        }
        if (!anyDigit) throw new NumberFormatException("bad quantity: " + s);
        while (fracDigits < SCALE_DIGITS) { frac *= 10; fracDigits++; }
        long scaled = whole * SCALE + frac;
        return neg ? -scaled : scaled;
    }

    /** Exact decimal rendering, trailing zeros trimmed. Integer arithmetic only. */
    static String formatScaled(long scaled) {
        boolean neg = scaled < 0;
        long v = Math.abs(scaled);
        long whole = v / SCALE, frac = v % SCALE;
        StringBuilder sb = new StringBuilder();
        if (neg) sb.append('-');
        sb.append(whole);
        if (frac != 0) {
            String f = String.format("%0" + SCALE_DIGITS + "d", frac);
            int end = f.length();
            while (end > 0 && f.charAt(end - 1) == '0') end--;
            sb.append('.').append(f, 0, end);
        }
        return sb.toString();
    }

    static SortedMap<String, Long> part3(List<String[]> trades) {
        SortedMap<String, Long> pos = new TreeMap<>();               // lexicographic output, free
        for (String[] t : trades) {
            long q = parseScaled(t[2]);
            if (q <= 0) continue;                                    // reject non-positive quantity
            if (t[0].equals("BUY")) pos.merge(t[1], q, Long::sum);
            else if (t[0].equals("SELL")) pos.merge(t[1], -q, Long::sum);
        }
        return pos;
    }

    /* ===================================================================
     * PART 4 — The over-sell policy, and the questions to ask.
     *
     * CHANGES FROM PART 3:
     *   - an explicit OverSellPolicy instead of silently allowing a negative
     *   - returns which instructions were rejected
     *
     * ⚠️ THREE THINGS THE PROBLEM DOES NOT SPECIFY — ask, don't guess:
     *   1. Over-sell: reject the instruction, clamp to zero, or allow a short
     *      position? A brokerage REJECTS. That is the default here.
     *   2. Output ordering: insertion order or lexicographic? The source lists
     *      this as an ambiguity explicitly. TreeMap = lexicographic.
     *   3. Does a ticker that nets to exactly zero still appear in the output?
     *      Default here: yes, showing 0. Say it.
     * =================================================================== */
    enum OverSellPolicy { REJECT, CLAMP_TO_ZERO, ALLOW_SHORT }

    record Result(SortedMap<String, Long> positions, List<Integer> rejectedIndexes) {}

    static Result part4(List<String[]> trades, OverSellPolicy policy, boolean dropZeroPositions) {
        SortedMap<String, Long> pos = new TreeMap<>();
        List<Integer> rejected = new ArrayList<>();

        for (int i = 0; i < trades.size(); i++) {
            String[] t = trades.get(i);
            if (t.length < 3) { rejected.add(i); continue; }
            long q;
            try { q = parseScaled(t[2]); }
            catch (NumberFormatException e) { rejected.add(i); continue; }

            if (q <= 0) { rejected.add(i); continue; }

            if (t[0].equals("BUY")) {
                pos.merge(t[1], q, Long::sum);
            } else if (t[0].equals("SELL")) {
                long held = pos.getOrDefault(t[1], 0L);
                if (q > held) {
                    switch (policy) {
                        case REJECT        -> { rejected.add(i); continue; }
                        case CLAMP_TO_ZERO -> pos.put(t[1], 0L);
                        case ALLOW_SHORT   -> pos.merge(t[1], -q, Long::sum);
                    }
                } else {
                    pos.put(t[1], held - q);
                }
            } else {
                rejected.add(i);                                  // unknown action
            }
        }
        if (dropZeroPositions) pos.values().removeIf(v -> v == 0L);
        return new Result(pos, rejected);
    }

    /** BigDecimal alternative — also exact. Offer it, then explain why you chose long. */
    static SortedMap<String, BigDecimal> part4BigDecimal(List<String[]> trades) {
        SortedMap<String, BigDecimal> pos = new TreeMap<>();
        for (String[] t : trades) {
            BigDecimal q = new BigDecimal(t[2]);                  // String ctor, never the double one
            BigDecimal signed = t[0].equals("BUY") ? q : q.negate();
            pos.merge(t[1], signed, BigDecimal::add);
        }
        pos.replaceAll((k, v) -> v.setScale(SCALE_DIGITS, RoundingMode.DOWN).stripTrailingZeros());
        return pos;
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        List<String[]> trades = List.of(
                new String[]{"BUY",  "AAPL", "1.5"},
                new String[]{"BUY",  "TSLA", "2"},
                new String[]{"SELL", "AAPL", "0.4"});

        System.out.println("=== ⭐ PART 2: what double does and does NOT get wrong ===");
        Map<String, Double> broken = part2_broken(trades);
        System.out.println("  double  AAPL = " + broken.get("AAPL") + "   expected 1.1");
        System.out.println("  == 1.1?      = " + (broken.get("AAPL") == 1.1)
                + "   <-- ⚠️ the SAMPLE PASSES. Do not claim otherwise.");
        System.out.println("  1.5 - 0.4 exact = " + new BigDecimal(1.5 - 0.4));
        System.out.println("  literal 1.1 exact = " + new BigDecimal(1.1));
        System.out.println("  ...bit-identical. The sample cannot disprove double.");

        System.out.println("\n=== ✅ PART 3: scaled long ===");
        SortedMap<String, Long> pos = part3(trades);
        String aapl = formatScaled(pos.get("AAPL"));
        String tsla = formatScaled(pos.get("TSLA"));
        System.out.println("  AAPL = " + aapl + "   TSLA = " + tsla);
        System.out.println("  expected AAPL=1.1 TSLA=2 -> "
                + (aapl.equals("1.1") && tsla.equals("2") ? "PASS" : "FAIL"));

        System.out.println("\n=== parse/format round-trip ===");
        for (String v : new String[]{"1.5", "0.4", "2", "0.000001", "1234.567891", "0.1"}) {
            String rt = formatScaled(parseScaled(v));
            System.out.printf("  %-12s -> %-12s %s%n", v, rt,
                    rt.equals(new BigDecimal(v).stripTrailingZeros().toPlainString()) ? "PASS" : "check");
        }

        System.out.println("\n=== ⭐ WHERE double ACTUALLY FAILS: accumulation ===");
        double d = 0.0;
        for (int i = 0; i < 10; i++) d += 0.1;
        long l = 0;
        for (int i = 0; i < 10; i++) l += parseScaled("0.1");
        System.out.println("  ten additions of 0.1  = " + d + "  (== 1.0? " + (d == 1.0) + ")  <-- THE BUG");
        System.out.println("  scaled long           = " + formatScaled(l)
                + "  (== 1? " + formatScaled(l).equals("1") + ")");
        double flat = 0.0;
        for (int i = 0; i < 3; i++) flat += 0.1;
        flat -= 0.3;
        System.out.println("  buy 0.1 x3, sell 0.3  = " + flat
                + "  <-- a position that should be FLAT is not. This is the");
        System.out.println("                            reconciliation break, and it is the argument.");

        System.out.println("\n=== PART 4: over-sell policies ===");
        List<String[]> over = List.of(
                new String[]{"BUY",  "AAPL", "1.0"},
                new String[]{"SELL", "AAPL", "2.5"});
        for (OverSellPolicy p : OverSellPolicy.values()) {
            Result r = part4(over, p, false);
            System.out.printf("  %-14s AAPL = %-8s rejected = %s%n",
                    p, formatScaled(r.positions().get("AAPL")), r.rejectedIndexes());
        }

        System.out.println("\n=== PART 4: malformed input rejected, not thrown ===");
        Result r = part4(List.of(
                new String[]{"BUY", "AAPL", "1.5"},
                new String[]{"BUY", "AAPL", "abc"},
                new String[]{"BUY", "AAPL", "-1"},
                new String[]{"HOLD", "AAPL", "1"}), OverSellPolicy.REJECT, false);
        System.out.println("  positions = " + formatScaled(r.positions().get("AAPL"))
                + "  rejected = " + r.rejectedIndexes() + " -> "
                + (r.rejectedIndexes().equals(List.of(1, 2, 3)) ? "PASS" : "FAIL"));

        System.out.println("\n=== BigDecimal cross-check ===");
        System.out.println("  " + part4BigDecimal(trades));

        System.out.println("\n=== scale: 100k instructions ===");
        // ⚠️ Constructing this test taught me something worth carrying into the room:
        // a SYMMETRIC workload (50 buys of 0.1 then 50 sells of 0.1) cancels EXACTLY
        // under double, because each subtraction retraces the addition that preceded it.
        // So a naive "at scale it breaks" demo does not break. You need MIXED
        // quantities, which is what a real trade stream looks like:
        //     BUY 0.1, BUY 0.2, SELL 0.3  -> should be flat, is 5.55e-17
        // Emit COMPLETE triples per ticker, or the test is unbalanced and proves nothing.
        // (An i%3 / i%1000 interleave does not divide evenly — I got that wrong twice.)
        List<String[]> big = new ArrayList<>();
        for (int round = 0; round < 33; round++) {
            for (int t = 0; t < 1000; t++) {
                String tk = "T" + t;
                big.add(new String[]{"BUY",  tk, "0.1"});
                big.add(new String[]{"BUY",  tk, "0.2"});
                big.add(new String[]{"SELL", tk, "0.3"});
            }
        }
        long t0 = System.currentTimeMillis();
        SortedMap<String, Long> bigPos = part3(big);
        System.out.println("  ~100k instructions, " + bigPos.size() + " tickers, "
                + (System.currentTimeMillis() - t0) + " ms");
        System.out.println("  each ticker: buy .1 + buy .2 - sell .3, repeated -> must be exactly 0");
        System.out.println("  scaled long : " + bigPos.values().stream().filter(v -> v != 0L).count()
                + " of " + bigPos.size() + " non-zero  -> "
                + (bigPos.values().stream().allMatch(v -> v == 0L) ? "PASS — exact" : "FAIL"));
        Map<String, Double> bigDouble = part2_broken(big);
        long nonZero = bigDouble.values().stream().filter(v -> v != 0.0).count();
        System.out.println("  double      : " + nonZero + " of " + bigDouble.size()
                + " non-zero  -> " + (nonZero > 0
                ? "⭐ THE BUG AT SCALE. Sample e.g. " + bigDouble.get("T0")
                : "(none — pick a less symmetric workload)"));
    }
}
