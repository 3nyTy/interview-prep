/*
 * ============================================================================
 *  IPv4 Address Iterator with CIDR Support  (OpenAI Staff-SWE interview)
 *  Original problem is in Python; this is a faithful Java 21 implementation.
 * ============================================================================
 *
 *  WHAT THIS FILE CONTAINS
 *  -----------------------
 *  One package-private top-level class `IPv4AddressIterator` whose only job is
 *  to dispatch to four fully self-contained static nested classes:
 *
 *    Part1  Forward iterator: start IP .. 255.255.255.255, then stop.
 *    Part2  Adds a `reverse` flag: reverse counts down to 0.0.0.0, then stops.
 *    Part3  Adds CIDR ("a.b.c.d/prefix"): iterate only within [network,
 *           broadcast]; a plain IP behaves exactly like Parts 1/2.
 *    Part4  Complexity discussion + a runnable laziness demo proving we never
 *           materialize a block (counts 1,000,000 of a /0 with O(1) space).
 *
 *  Each PartN has its OWN `public static void main(String[])`, embeds the
 *  question's examples as runtime checks, and prints "PartN OK" on success.
 *
 *  SELF-CONTAINMENT / DUPLICATION STRATEGY
 *  ---------------------------------------
 *  Every PartN is intentionally standalone: it duplicates the small helpers it
 *  needs (parseIpv4 / formatIpv4 / check, plus the CIDR math in Parts 3 & 4) and
 *  refers to NOTHING outside itself. "Promotion test": copy any single PartN
 *  out, rename `class PartN` -> top-level class, and it compiles and runs
 *  unchanged. This deliberately trades a little duplication for the property
 *  that each part is an independent, interview-submittable answer.
 *
 *  WHY `long`, NOT `int`  (the single most important modeling decision)
 *  --------------------------------------------------------------------
 *  An IPv4 address is a 32-bit UNSIGNED value (0 .. 0xFFFFFFFF = 4294967295).
 *  Java's `int` is signed 32-bit, so it cannot hold 0xFFFFFFFF as a positive
 *  number. We store the address in a `long` because:
 *    (a) forward termination is `current > limit` with limit = 0xFFFFFFFFL;
 *        an int can't represent that bound as a positive comparand.
 *    (b) after emitting the top IP, `current++` becomes 4294967296 in a long,
 *        which is cleanly `> limit` with NO signed wraparound to a negative.
 *    (c) reverse from 0 then `current--` is -1 (a perfectly valid long), so
 *        `current < 0`/`current < limit` ends iteration without underflow UB.
 *    (d) CIDR math uses shifts: `1L << hostBits` and
 *        `(0xFFFFFFFFL << hostBits) & 0xFFFFFFFFL`. In Java the shift amount is
 *        masked to the operand width, so shifting an *int* by 32 is a NO-OP
 *        (yields the original value, not 0). For /0, hostBits = 32, so the
 *        shift MUST be done on a `long` to get the correct mask (0) / blockSize
 *        (2^32). `long` makes /0 fall out correctly.
 *
 *  CONVERSION HELPERS: VALIDATE, DON'T MASK
 *  ----------------------------------------
 *  parseIpv4 VALIDATES (4 octets, numeric, each 0..255) and shift-accumulates one
 *  byte at a time. It deliberately does NOT end with `& 0xFFFFFFFFL`: with the
 *  octets validated, four bytes cannot exceed 32 bits, so the [0, 0xFFFFFFFF]
 *  invariant is PROVEN instead of clamped. That matters because masking bad input
 *  is worse than rejecting it — "999.0.0.1" masks to "231.0.0.1", a plausible
 *  address that is silently not what the caller asked for. Unmasked and
 *  unvalidated is worse still: the raw value lands outside [0, 0xFFFFFFFF], so
 *  hasNext() is false on the first call and iteration is SILENTLY EMPTY (or, for a
 *  negative octet, starts millions of addresses early).
 *
 *  formatIpv4 mirrors that validation, which also acts as a tripwire for this
 *  problem's signature bug: if `current` were ever an int and wrapped negative,
 *  formatIpv4 throws instead of printing a plausible-looking address. Inside it, the
 *  `& 255` masks ARE mandatory — shifting only moves a byte into the low
 *  position, it does not isolate it ((v >> 16) alone is 49320, not 168).
 *
 *  SELF-CHECKS RUN WITHOUT -ea
 *  ---------------------------
 *  We do NOT use the `assert` keyword (it is disabled unless the JVM is started
 *  with -ea). Each part defines a throwing `check(boolean, String)` helper that
 *  raises an AssertionError when a condition fails, so the embedded examples are
 *  verified under a plain `java` launch.
 *
 *  ENTRY POINTS
 *  ------------
 *    java IPv4AddressIterator            -> runs Part1..Part4, prints ALL PARTS OK
 *    java IPv4AddressIterator part1      -> runs only Part1 (etc. part2/3/4)
 * ============================================================================
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Package-private (NOT public) on purpose: the source file is named
 * "A - IPv4.java", which is not a legal Java identifier and does not match the
 * class name. The "public class must match file name" rule only applies to
 * PUBLIC top-level classes, so a package-private class compiles fine in a
 * differently-named file. Do not make this public and do not rename the file.
 */
class IPv4AddressIterator {

    /**
     * Top-level dispatcher. With no args, runs all four parts in order. With an
     * argument it runs just that part. Uses a Java 21 arrow switch.
     */
    public static void main(String[] args) {
        if (args.length >= 1) {
            String which = args[0].toLowerCase();
            switch (which) {
                case "part1" -> Part1.main(args);
                case "part2" -> Part2.main(args);
                case "part3" -> Part3.main(args);
                case "part4" -> Part4.main(args);
                default -> {
                    System.out.println("Unknown part: " + args[0]);
                    System.out.println("Usage: java IPv4AddressIterator [part1|part2|part3|part4]");
                    System.out.println("(no argument runs all four parts)");
                }
            }
            return;
        }

        // No args: run everything in sequence.
        Part1.main(args);
        Part2.main(args);
        Part3.main(args);
        Part4.main(args);
        System.out.println("ALL PARTS OK");
    }

    // ========================================================================
    //  PART 1 — Forward iterator only.
    //
    //  Iterate from a start IP up to 255.255.255.255 inclusive, then stop.
    //  The iterator models the Python protocol: it is both Iterable (iterator()
    //  returns `this`, mirroring __iter__ returning self) and Iterator
    //  (next() throws NoSuchElementException when exhausted, the Java analog of
    //  raising StopIteration). Single-pass: don't iterate one instance twice.
    // ========================================================================
    static final class Part1 {

        /** 0xFFFFFFFFL = 255.255.255.255 = 4294967295, the inclusive top. */
        private static final long MAX_IP = 0xFFFFFFFFL;

        /** Forward-only IPv4 iterator. */
        static final class Ipv4ForwardIterator implements Iterator<String>, Iterable<String> {
            // current MUST be a long: after emitting MAX_IP, current++ = 2^32,
            // which is > MAX_IP cleanly. An int would wrap to a negative value.
            private long current;
            private final long limit = MAX_IP;

            Ipv4ForwardIterator(String startIp) {
                this.current = parseIpv4(startIp);
            }

            // __iter__ returning self: this instance is itself the iterator.
            @Override public Iterator<String> iterator() { return this; }

            // Forward: more values remain while current is at or below the top.
            @Override public boolean hasNext() { return current <= limit; }

            @Override public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("iteration past 255.255.255.255");
                }
                // Snapshot BEFORE advancing so endpoints are INCLUSIVE.
                String result = formatIpv4(current);
                current++;
                return result;
            }
        }

        // ---- helpers (duplicated into this part; references nothing outside) --

        /**
         * "a.b.c.d" -> 32-bit value in a long, VALIDATED to 0..0xFFFFFFFF.
         * Rejects the wrong octet count, non-numeric octets, and octets outside
         * 0..255, so the caller's range invariant is proven, not clamped.
         */
        private static long parseIpv4(String ip) {
            String[] o = ip.split("\\.");          // regex! a bare "." matches everything
            if (o.length != 4) {
                throw new IllegalArgumentException("expected 4 octets: " + ip);
            }
            long v = 0;
            for (String part : o) {
                int octet = Integer.parseInt(part.trim());   // throws on non-numeric
                if (octet < 0 || octet > 255) {
                    throw new IllegalArgumentException("octet out of range: " + part);
                }
                v = (v << 8) | octet;              // shift-accumulate, one byte at a time
            }
            // No & 0xFFFFFFFFL needed: four validated octets can never exceed 32 bits,
            // so the [0, 0xFFFFFFFF] invariant is now PROVEN rather than clamped.
            return v;
        }

        /**
         * 32-bit value -> "a.b.c.d". Exact inverse of parseIpv4, and validates the
         * same range so the two read as an obvious mirrored pair.
         */
        private static String formatIpv4(long v) {
            // Mirror of parseIpv4's validation. Doubles as a TRIPWIRE for this
            // problem's signature bug: if `current` were ever an int and wrapped
            // negative, this throws loudly instead of printing a plausible address.
            if (v < 0 || v > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("not a 32-bit address: " + v);
            }
            StringBuilder sb = new StringBuilder(15);   // "255.255.255.255".length()
            for (int shift = 24; shift >= 0; shift -= 8) {
                // >>> not >>: signals an UNSIGNED quantity. & 255 is mandatory here —
                // shifting only moves the byte down, it does not isolate it.
                sb.append((v >>> shift) & 255);
                if (shift > 0) {
                    sb.append('.');
                }
            }
            return sb.toString();
        }

        /** Throwing assertion that runs without -ea. */
        private static void check(boolean cond, String msg) {
            if (!cond) throw new AssertionError("CHECK FAILED: " + msg);
        }

        public static void main(String[] args) {
            // Primary example: "255.255.255.250" yields exactly 6 addresses.
            List<String> expected = List.of(
                "255.255.255.250", "255.255.255.251", "255.255.255.252",
                "255.255.255.253", "255.255.255.254", "255.255.255.255");
            List<String> actual = new ArrayList<>();
            for (String ip : new Ipv4ForwardIterator("255.255.255.250")) actual.add(ip);
            check(actual.equals(expected), "primary example 255.255.255.250 -> 6 values, got " + actual);

            // Octet rollover: first two values are .255 then the next /24.
            Ipv4ForwardIterator roll = new Ipv4ForwardIterator("192.168.0.255");
            check(roll.next().equals("192.168.0.255"), "rollover first == 192.168.0.255");
            check(roll.next().equals("192.168.1.0"),   "rollover second == 192.168.1.0");

            // Single value: top IP yields exactly one, then next() throws.
            Ipv4ForwardIterator single = new Ipv4ForwardIterator("255.255.255.255");
            check(single.hasNext(), "single: hasNext before");
            check(single.next().equals("255.255.255.255"), "single value == 255.255.255.255");
            check(!single.hasNext(), "single: hasNext false after");
            boolean threw = false;
            try { single.next(); } catch (NoSuchElementException e) { threw = true; }
            check(threw, "single: next() throws after exhaustion");

            // Low start: don't drain 4.3B; just confirm the first two values.
            Ipv4ForwardIterator low = new Ipv4ForwardIterator("0.0.0.0");
            check(low.next().equals("0.0.0.0"), "low start first == 0.0.0.0");
            check(low.next().equals("0.0.0.1"), "low start second == 0.0.0.1");

            System.out.println("Part1 OK");
        }
    }

    // ========================================================================
    //  PART 2 — Reverse iteration via a `reverse` flag.
    //
    //  CHANGES FROM PART 1:
    //    - Added a `reverse` field + constructor parameter.
    //    - `limit` is now direction-dependent: 0xFFFFFFFF forward, 0 reverse.
    //    - hasNext() branches: forward `current <= limit`, reverse `current >= limit`.
    //    - next() advances +1 forward, -1 reverse (still snapshot-before-advance,
    //      keeping both endpoints inclusive so the examples match exactly).
    // ========================================================================
    static final class Part2 {

        private static final long MAX_IP = 0xFFFFFFFFL; // 255.255.255.255
        private static final long MIN_IP = 0L;          // 0.0.0.0

        /** Bidirectional IPv4 iterator (full range). */
        static final class Ipv4Iterator implements Iterator<String>, Iterable<String> {
            private long current;       // long: reverse from 0 makes this -1 cleanly.
            private final long limit;   // direction-dependent stop bound (inclusive).
            private final boolean reverse;

            Ipv4Iterator(String startIp, boolean reverse) {
                this.reverse = reverse;
                this.current = parseIpv4(startIp);
                this.limit = reverse ? MIN_IP : MAX_IP;
            }

            @Override public Iterator<String> iterator() { return this; }

            @Override public boolean hasNext() {
                // Reverse runs while at or above 0; forward while at or below top.
                return reverse ? current >= limit : current <= limit;
            }

            @Override public String next() {
                if (!hasNext()) throw new NoSuchElementException("iteration exhausted");
                String result = formatIpv4(current);
                if (reverse) current--; else current++;
                return result;
            }
        }

        // ---- helpers (duplicated; references nothing outside this part) ------

        private static long parseIpv4(String ip) {
            String[] o = ip.split("\\.");          // regex! a bare "." matches everything
            if (o.length != 4) {
                throw new IllegalArgumentException("expected 4 octets: " + ip);
            }
            long v = 0;
            for (String part : o) {
                int octet = Integer.parseInt(part.trim());   // throws on non-numeric
                if (octet < 0 || octet > 255) {
                    throw new IllegalArgumentException("octet out of range: " + part);
                }
                v = (v << 8) | octet;              // shift-accumulate, one byte at a time
            }
            // No & 0xFFFFFFFFL needed: four validated octets can never exceed 32 bits,
            // so the [0, 0xFFFFFFFF] invariant is now PROVEN rather than clamped.
            return v;
        }

        private static String formatIpv4(long v) {
            // Mirror of parseIpv4's validation. Doubles as a TRIPWIRE for this
            // problem's signature bug: if `current` were ever an int and wrapped
            // negative, this throws loudly instead of printing a plausible address.
            if (v < 0 || v > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("not a 32-bit address: " + v);
            }
            StringBuilder sb = new StringBuilder(15);   // "255.255.255.255".length()
            for (int shift = 24; shift >= 0; shift -= 8) {
                // >>> not >>: signals an UNSIGNED quantity. & 255 is mandatory here —
                // shifting only moves the byte down, it does not isolate it.
                sb.append((v >>> shift) & 255);
                if (shift > 0) {
                    sb.append('.');
                }
            }
            return sb.toString();
        }

        private static void check(boolean cond, String msg) {
            if (!cond) throw new AssertionError("CHECK FAILED: " + msg);
        }

        public static void main(String[] args) {
            // Reverse example: "0.0.0.5" reverse=true yields exactly 6 descending.
            List<String> expectedRev = List.of(
                "0.0.0.5", "0.0.0.4", "0.0.0.3", "0.0.0.2", "0.0.0.1", "0.0.0.0");
            List<String> actualRev = new ArrayList<>();
            for (String ip : new Ipv4Iterator("0.0.0.5", true)) actualRev.add(ip);
            check(actualRev.equals(expectedRev), "reverse 0.0.0.5 -> 6 descending, got " + actualRev);

            // Reverse from zero: one value (0.0.0.0) then next() throws.
            Ipv4Iterator revZero = new Ipv4Iterator("0.0.0.0", true);
            check(revZero.next().equals("0.0.0.0"), "reverse-from-zero value == 0.0.0.0");
            check(!revZero.hasNext(), "reverse-from-zero: hasNext false after");
            boolean threw = false;
            try { revZero.next(); } catch (NoSuchElementException e) { threw = true; }
            check(threw, "reverse-from-zero: next() throws after 0.0.0.0");

            // Reverse underflow across octet boundary: .0 then previous /24's .255.
            Ipv4Iterator under = new Ipv4Iterator("192.168.1.0", true);
            check(under.next().equals("192.168.1.0"),   "underflow first == 192.168.1.0");
            check(under.next().equals("192.168.0.255"), "underflow second == 192.168.0.255");

            // Forward regression: Part 1's example still works with reverse=false.
            List<String> expectedFwd = List.of(
                "255.255.255.250", "255.255.255.251", "255.255.255.252",
                "255.255.255.253", "255.255.255.254", "255.255.255.255");
            List<String> actualFwd = new ArrayList<>();
            for (String ip : new Ipv4Iterator("255.255.255.250", false)) actualFwd.add(ip);
            check(actualFwd.equals(expectedFwd), "forward regression 255.255.255.250 -> 6 ascending, got " + actualFwd);

            System.out.println("Part2 OK");
        }
    }

    // ========================================================================
    //  PART 3 — CIDR notation (forward & reverse) plus plain IP.
    //
    //  CHANGES FROM PART 2:
    //    - Constructor takes "a.b.c.d/prefix" OR a plain "a.b.c.d" and detects '/'.
    //    - CIDR: compute hostBits = 32 - prefix; blockSize = 1L << hostBits;
    //      mask = (0xFFFFFFFFL << hostBits) & 0xFFFFFFFFL; network = start & mask;
    //      broadcast = network + blockSize - 1. Forward stops at broadcast,
    //      reverse stops at network.
    //    - Plain IP: same as Part 2 (forward to 255.255.255.255, reverse to 0.0.0.0).
    //
    //  /0  : prefix 0 -> hostBits 32. The mask/blockSize shifts MUST be on a long
    //        (shifting an int by 32 is a no-op in Java). Result: mask = 0,
    //        network = 0.0.0.0, blockSize = 2^32, broadcast = 0xFFFFFFFF. Correct.
    //  /32 : prefix 32 -> hostBits 0 -> blockSize 1 -> network == broadcast ==
    //        start. A single-IP block.
    //
    //  Start-outside-block: we deliberately do NOT clamp or error. The problem's
    //  examples always satisfy network <= start <= broadcast, so we trust the
    //  caller and iterate from `start` to the relevant boundary.
    // ========================================================================
    static final class Part3 {

        /** Bidirectional IPv4 iterator over either a CIDR block or the full range. */
        static final class Ipv4Iterator implements Iterator<String>, Iterable<String> {
            private long current;       // long for the same reasons as Parts 1 & 2.
            private final long limit;   // inclusive stop bound for the chosen direction.
            private final boolean reverse;

            Ipv4Iterator(String ipOrCidr, boolean reverse) {
                this.reverse = reverse;

                if (ipOrCidr.indexOf('/') >= 0) {
                    // ---- CIDR notation ----
                    String[] parts = ipOrCidr.split("/");
                    int prefix = Integer.parseInt(parts[1]);
                    long startAddress = parseIpv4(parts[0]);

                    // hostBits can be 32 (for /0). All of these MUST be long math:
                    // shifting an int by 32 is a no-op, so an int /0 would compute
                    // the wrong mask and block size.
                    int hostBits = 32 - prefix;
                    long blockSize = 1L << hostBits;                       // 2^hostBits
                    long mask = (0xFFFFFFFFL << hostBits) & 0xFFFFFFFFL;   // leading-1s mask
                    long network = startAddress & mask;                        // first IP in block
                    long broadcast = network + blockSize - 1;              // last IP in block

                    this.current = startAddress;
                    this.limit = reverse ? network : broadcast;
                } else {
                    // ---- plain IP: full-range behavior (Parts 1 & 2) ----
                    this.current = parseIpv4(ipOrCidr);
                    this.limit = reverse ? 0L : 0xFFFFFFFFL;
                }
            }

            @Override public Iterator<String> iterator() { return this; }

            @Override public boolean hasNext() {
                return reverse ? current >= limit : current <= limit;
            }

            @Override public String next() {
                if (!hasNext()) throw new NoSuchElementException("iteration exhausted");
                String result = formatIpv4(current);
                if (reverse) current--; else current++;
                return result;
            }
        }

        // ---- helpers (duplicated; references nothing outside this part) ------

        private static long parseIpv4(String ip) {
            String[] o = ip.split("\\.");          // regex! a bare "." matches everything
            if (o.length != 4) {
                throw new IllegalArgumentException("expected 4 octets: " + ip);
            }
            long v = 0;
            for (String part : o) {
                int octet = Integer.parseInt(part.trim());   // throws on non-numeric
                if (octet < 0 || octet > 255) {
                    throw new IllegalArgumentException("octet out of range: " + part);
                }
                v = (v << 8) | octet;              // shift-accumulate, one byte at a time
            }
            // No & 0xFFFFFFFFL needed: four validated octets can never exceed 32 bits,
            // so the [0, 0xFFFFFFFF] invariant is now PROVEN rather than clamped.
            return v;
        }

        private static String formatIpv4(long v) {
            // Mirror of parseIpv4's validation. Doubles as a TRIPWIRE for this
            // problem's signature bug: if `current` were ever an int and wrapped
            // negative, this throws loudly instead of printing a plausible address.
            if (v < 0 || v > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("not a 32-bit address: " + v);
            }
            StringBuilder sb = new StringBuilder(15);   // "255.255.255.255".length()
            for (int shift = 24; shift >= 0; shift -= 8) {
                // >>> not >>: signals an UNSIGNED quantity. & 255 is mandatory here —
                // shifting only moves the byte down, it does not isolate it.
                sb.append((v >>> shift) & 255);
                if (shift > 0) {
                    sb.append('.');
                }
            }
            return sb.toString();
        }

        private static void check(boolean cond, String msg) {
            if (!cond) throw new AssertionError("CHECK FAILED: " + msg);
        }

        private static List<String> collect(Ipv4Iterator it) {
            List<String> out = new ArrayList<>();
            for (String ip : it) out.add(ip);
            return out;
        }

        public static void main(String[] args) {
            // ---- CIDR examples from the prompt ----

            // "192.168.1.250/29" forward => .250 .. .255 (block is .248-.255).
            check(collect(new Ipv4Iterator("192.168.1.250/29", false)).equals(List.of(
                    "192.168.1.250", "192.168.1.251", "192.168.1.252",
                    "192.168.1.253", "192.168.1.254", "192.168.1.255")),
                "CIDR forward 192.168.1.250/29");

            // "192.168.1.5/29" reverse => .5 .. .0.
            check(collect(new Ipv4Iterator("192.168.1.5/29", true)).equals(List.of(
                    "192.168.1.5", "192.168.1.4", "192.168.1.3",
                    "192.168.1.2", "192.168.1.1", "192.168.1.0")),
                "CIDR reverse 192.168.1.5/29");

            // ---- All five provided test cases ----

            // 1) /32 single IP.
            check(collect(new Ipv4Iterator("192.168.1.100/32", false))
                    .equals(List.of("192.168.1.100")), "test1 /32 single");

            // 2) /31 two IPs.
            check(collect(new Ipv4Iterator("10.0.0.0/31", false))
                    .equals(List.of("10.0.0.0", "10.0.0.1")), "test2 /31 two");

            // 3) /29 forward: length 8, last == 172.16.0.7.
            List<String> t3 = collect(new Ipv4Iterator("172.16.0.0/29", false));
            check(t3.size() == 8, "test3 /29 length 8");
            check(t3.get(t3.size() - 1).equals("172.16.0.7"), "test3 last == 172.16.0.7");

            // 4) /29 reverse: first == 172.16.0.7, last == 172.16.0.0.
            List<String> t4 = collect(new Ipv4Iterator("172.16.0.7/29", true));
            check(t4.get(0).equals("172.16.0.7"), "test4 first == 172.16.0.7");
            check(t4.get(t4.size() - 1).equals("172.16.0.0"), "test4 last == 172.16.0.0");

            // 5) mid-block forward: first == 192.168.1.5, last == 192.168.1.7.
            List<String> t5 = collect(new Ipv4Iterator("192.168.1.5/29", false));
            check(t5.get(0).equals("192.168.1.5"), "test5 first == 192.168.1.5");
            check(t5.get(t5.size() - 1).equals("192.168.1.7"), "test5 last == 192.168.1.7");

            // ---- Plain-IP regression (no slash behaves like Parts 1/2) ----
            check(collect(new Ipv4Iterator("255.255.255.250", false)).equals(List.of(
                    "255.255.255.250", "255.255.255.251", "255.255.255.252",
                    "255.255.255.253", "255.255.255.254", "255.255.255.255")),
                "plain forward 255.255.255.250");
            check(collect(new Ipv4Iterator("0.0.0.5", true)).equals(List.of(
                    "0.0.0.5", "0.0.0.4", "0.0.0.3", "0.0.0.2", "0.0.0.1", "0.0.0.0")),
                "plain reverse 0.0.0.5");

            // ---- /0 laziness spot-check: first emitted value is 0.0.0.0; do NOT drain. ----
            Ipv4Iterator allV4 = new Ipv4Iterator("0.0.0.0/0", false);
            check(allV4.next().equals("0.0.0.0"), "/0 forward first == 0.0.0.0");

            System.out.println("Part3 OK");
        }
    }

    // ========================================================================
    //  PART 4 — Complexity follow-up (with a runnable laziness proof).
    //
    //  CHANGES FROM PART 3:
    //    - The iterator surface is UNCHANGED (same CIDR/plain logic as Part 3).
    //    - main() prints the complexity analysis and, instead of collecting a
    //      list, counts addresses lazily with a cap so the /0 demo does not try
    //      to walk all 4.29 billion entries.
    //
    //  COMPLEXITY:
    //    next()       : O(1) time, O(1) space (one increment + one formatIpv4).
    //    full walk    : O(N) time to emit N addresses — unavoidable, since the
    //                   contract is to produce N outputs.
    //    space        : O(1) — we hold only `current`, `limit`, `reverse`, never
    //                   a materialized list of the block.
    // ========================================================================
    static final class Part4 {

        /** Same iterator as Part 3 (CIDR + plain, forward + reverse). */
        static final class Ipv4Iterator implements Iterator<String>, Iterable<String> {
            private long current;
            private final long limit;
            private final boolean reverse;

            Ipv4Iterator(String ipOrCidr, boolean reverse) {
                this.reverse = reverse;
                if (ipOrCidr.indexOf('/') >= 0) {
                    String[] parts = ipOrCidr.split("/");
                    int prefix = Integer.parseInt(parts[1]);
                    long startAddress = parseIpv4(parts[0]);
                    int hostBits = 32 - prefix;
                    long blockSize = 1L << hostBits;                     // long: /0 needs 2^32
                    long mask = (0xFFFFFFFFL << hostBits) & 0xFFFFFFFFL; // long: int<<32 is a no-op
                    long network = startAddress & mask;
                    long broadcast = network + blockSize - 1;
                    this.current = startAddress;
                    this.limit = reverse ? network : broadcast;
                } else {
                    this.current = parseIpv4(ipOrCidr);
                    this.limit = reverse ? 0L : 0xFFFFFFFFL;
                }
            }

            @Override public Iterator<String> iterator() { return this; }
            @Override public boolean hasNext() { return reverse ? current >= limit : current <= limit; }
            @Override public String next() {
                if (!hasNext()) throw new NoSuchElementException("iteration exhausted");
                String result = formatIpv4(current);
                if (reverse) current--; else current++;
                return result;
            }
        }

        // ---- helpers (duplicated; references nothing outside this part) ------

        private static long parseIpv4(String ip) {
            String[] o = ip.split("\\.");          // regex! a bare "." matches everything
            if (o.length != 4) {
                throw new IllegalArgumentException("expected 4 octets: " + ip);
            }
            long v = 0;
            for (String part : o) {
                int octet = Integer.parseInt(part.trim());   // throws on non-numeric
                if (octet < 0 || octet > 255) {
                    throw new IllegalArgumentException("octet out of range: " + part);
                }
                v = (v << 8) | octet;              // shift-accumulate, one byte at a time
            }
            // No & 0xFFFFFFFFL needed: four validated octets can never exceed 32 bits,
            // so the [0, 0xFFFFFFFF] invariant is now PROVEN rather than clamped.
            return v;
        }

        private static String formatIpv4(long v) {
            // Mirror of parseIpv4's validation. Doubles as a TRIPWIRE for this
            // problem's signature bug: if `current` were ever an int and wrapped
            // negative, this throws loudly instead of printing a plausible address.
            if (v < 0 || v > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("not a 32-bit address: " + v);
            }
            StringBuilder sb = new StringBuilder(15);   // "255.255.255.255".length()
            for (int shift = 24; shift >= 0; shift -= 8) {
                // >>> not >>: signals an UNSIGNED quantity. & 255 is mandatory here —
                // shifting only moves the byte down, it does not isolate it.
                sb.append((v >>> shift) & 255);
                if (shift > 0) {
                    sb.append('.');
                }
            }
            return sb.toString();
        }

        private static void check(boolean cond, String msg) {
            if (!cond) throw new AssertionError("CHECK FAILED: " + msg);
        }

        /**
         * Walk an iterator counting emitted addresses, stopping early once `cap`
         * is reached. Crucially this never stores the addresses, so it proves
         * O(1) space even over enormous ranges like /0.
         */
        private static long countViaIterator(Iterator<String> it, long cap) {
            long n = 0;
            while (it.hasNext() && n < cap) {
                it.next();   // consume and discard; do not collect.
                n++;
            }
            return n;
        }

        public static void main(String[] args) {
            // Print the complexity write-up.
            System.out.println("Part4 complexity analysis:");
            System.out.println("  next(): O(1) time and O(1) space (one increment + one formatIpv4).");
            System.out.println("  Full iteration of N addresses: O(N) time, which is unavoidable —");
            System.out.println("    the iterator contract is to emit N outputs.");
            System.out.println("  Space: O(1) — we store only current position, direction, and bounds;");
            System.out.println("    we never materialize the block into a list.");
            System.out.println("  Practical notes: keep the IP as a 32-bit value in a long; parse once in");
            System.out.println("    the constructor; precompute network/broadcast boundaries; format to a");
            System.out.println("    string only when yielding; stay lazy; never build a list of a large CIDR.");

            // Laziness demo: consume 1,000,000 of a /0 WITHOUT building a list.
            long cap = 1_000_000L;
            Ipv4Iterator all = new Ipv4Iterator("0.0.0.0/0", false);
            long consumed = countViaIterator(all, cap);
            check(consumed == cap, "/0 lazy consume count == " + cap + ", got " + consumed);
            System.out.println("Consumed " + consumed + " addresses from 0.0.0.0/0 without allocating a list");

            // Concrete block-size sanity checks via lazy counting (no list built).
            long c29 = countViaIterator(new Ipv4Iterator("172.16.0.0/29", false), Long.MAX_VALUE);
            check(c29 == 8, "/29 count == 8");
            check(c29 == (1L << (32 - 29)), "/29 count == 2^(32-29)");

            long c31 = countViaIterator(new Ipv4Iterator("10.0.0.0/31", false), Long.MAX_VALUE);
            check(c31 == 2, "/31 count == 2");

            long c32 = countViaIterator(new Ipv4Iterator("192.168.1.100/32", false), Long.MAX_VALUE);
            check(c32 == 1, "/32 count == 1");

            System.out.println("Part4 OK");
        }
    }
}
