import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Rate Limiter — open-ended coding (Reddit Staff phone screen).
 *
 * Interface under test (from the prompt):
 *     RateLimiter(max_requests, window_seconds)
 *     boolean allow(user_id, now)          // `now` is an integer timestamp (seconds)
 *
 * ============================================================================
 * HOW TO RUN THE ROUND
 *   1. Clarify:  per-user (yes) | hard cap vs burst-tolerant | `now` units (int
 *      seconds, monotonic non-decreasing) | sliding vs fixed window.
 *   2. Pick ONE and justify. Default = TOKEN BUCKET (O(1) time & space per user,
 *      burst-tolerant, the shape most production limiters use). State it and
 *      commit — switching algorithms mid-round burns 10 minutes.
 *   3. Implement it cleanly, then discuss cost + the distributed follow-up.
 *
 * All four algorithms below implement ONE interface, so the algorithm is a
 * swappable strategy — which is exactly why the "move to Redis" and
 * "per-endpoint quota" follow-ups need ZERO change to the calling contract.
 *
 * COST SUMMARY (per user)
 *   Token bucket .............. O(1) time, O(1) space,  burst-tolerant
 *   Sliding window log ........ O(1) amortized, O(reqs-in-window) space, EXACT
 *   Sliding window counter .... O(1) time, O(1) space,  ~small accuracy loss
 *   Leaky bucket .............. O(1) time, O(1) space,  smooth fixed throughput
 * ============================================================================
 */
public class RateLimiterSolutions {

    /** The minimal contract every algorithm honours. `now` = integer seconds. */
    interface RateLimiter {
        boolean allow(String userId, long now);
    }

    // Guards against floating-point drift when tokens/water are accumulated as doubles.
    private static final double EPS = 1e-9;

    // ========================================================================
    // PART 1 — TOKEN BUCKET  (my default pick)
    //
    // Per user keep (tokens, lastRefill). Refill at a constant rate up to a cap,
    // then spend one token per request. Map the prompt's (max, window) params to
    // the bucket's natural params:
    //     capacity   = maxRequests                 (largest instantaneous burst)
    //     refillRate = maxRequests / windowSeconds (tokens added per second)
    // Trade-off to say out loud: this is burst-tolerant, NOT a hard rolling cap —
    // a full bucket lets `max` through instantly. If they want an exact rolling
    // cap, that's the sliding window log (Part 2).
    // ========================================================================
    static final class TokenBucketRateLimiter implements RateLimiter {
        private final double capacity;
        private final double refillPerSec;
        private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

        private static final class Bucket {
            double tokens;
            long lastRefill;
            Bucket(double tokens, long lastRefill) { this.tokens = tokens; this.lastRefill = lastRefill; }
        }

        TokenBucketRateLimiter(int maxRequests, int windowSeconds) {
            this.capacity = maxRequests;
            this.refillPerSec = (double) maxRequests / windowSeconds;
        }

        @Override public boolean allow(String userId, long now) {
            // A brand-new user starts with a FULL bucket (may burst immediately).
            Bucket b = buckets.computeIfAbsent(userId, k -> new Bucket(capacity, now));
            synchronized (b) {                                   // serialise per-user; users run in parallel
                long elapsed = Math.max(0, now - b.lastRefill);  // clamp: never refill on clock skew
                b.tokens = Math.min(capacity, b.tokens + elapsed * refillPerSec);
                b.lastRefill = now;
                if (b.tokens + EPS >= 1.0) {                     // enough for one request?
                    b.tokens -= 1.0;
                    return true;
                }
                return false;
            }
        }
    }

    // ========================================================================
    // PART 2 — SLIDING WINDOW LOG   (the EXACT, always-defensible fallback)
    //
    // CHANGE vs Part 1: same interface, different state. Keep a deque of the
    // timestamps of accepted requests. On each call, pop everything older than
    // the window, then admit iff the deque is still under the limit.
    //
    // THE BUG THE PROMPT WARNS ABOUT: eviction MUST be a `while` loop (pop every
    // stale entry), not a single `if`. An `if` degrades to O(window) and admits
    // wrongly. Part 2's test exercises the mass-eviction path explicitly.
    //
    // Exactness: this enforces a HARD rolling cap of `max` per `window` — no
    // burst leak like the token bucket. Cost: O(1) amortised time (each stamp is
    // pushed once, popped once), O(requests-in-window) memory per user.
    // ========================================================================
    static final class SlidingWindowLogRateLimiter implements RateLimiter {
        private final int maxRequests;
        private final long windowSeconds;
        private final ConcurrentHashMap<String, Deque<Long>> logs = new ConcurrentHashMap<>();

        SlidingWindowLogRateLimiter(int maxRequests, int windowSeconds) {
            this.maxRequests = maxRequests;
            this.windowSeconds = windowSeconds;
        }

        @Override public boolean allow(String userId, long now) {
            Deque<Long> dq = logs.computeIfAbsent(userId, k -> new ArrayDeque<>());
            synchronized (dq) {
                long cutoff = now - windowSeconds;               // window covers (cutoff, now]
                while (!dq.isEmpty() && dq.peekFirst() <= cutoff) // <-- the CRITICAL while-loop
                    dq.pollFirst();
                if (dq.size() < maxRequests) {
                    dq.addLast(now);
                    return true;
                }
                return false;
            }
        }

        /**
         * PART 5 (memory pressure): drop users whose window is now empty.
         * Note the benign race — a concurrent allow() that already fetched this
         * deque may add to it after we unmap it, briefly under-counting that user.
         * The airtight alternative is to do the whole read-modify-write inside
         * ConcurrentHashMap.compute() and return null to evict.
         */
        int pruneIdle(long now) {
            long cutoff = now - windowSeconds;
            int removed = 0;
            for (Map.Entry<String, Deque<Long>> e : logs.entrySet()) {
                Deque<Long> dq = e.getValue();
                synchronized (dq) {
                    while (!dq.isEmpty() && dq.peekFirst() <= cutoff) dq.pollFirst();
                    if (dq.isEmpty() && logs.remove(e.getKey(), dq)) removed++;
                }
            }
            return removed;
        }
    }

    // ========================================================================
    // PART 3 — SLIDING WINDOW COUNTER   (use when they press on MEMORY)
    //
    // CHANGE vs Part 2: replace the unbounded deque with THREE numbers per user
    // (aligned window start, current-window count, previous-window count) and
    // estimate the rolling count by blending the two fixed windows:
    //     weightPrev = (windowSeconds - elapsedInCurrentWindow) / windowSeconds
    //     estimate   = prevCount * weightPrev + currentCount
    // Admit iff estimate < max. O(1) time AND O(1) memory. The cost is a small
    // accuracy loss (assumes the previous window's load was uniform); it can
    // over- or under-count near a bursty boundary, bounded by one window's load.
    // ========================================================================
    static final class SlidingWindowCounterRateLimiter implements RateLimiter {
        private final int maxRequests;
        private final long windowSeconds;
        private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

        private static final class Window {
            long windowStart = Long.MIN_VALUE; // sentinel = uninitialised
            long current = 0;
            long prev = 0;
        }

        SlidingWindowCounterRateLimiter(int maxRequests, int windowSeconds) {
            this.maxRequests = maxRequests;
            this.windowSeconds = windowSeconds;
        }

        @Override public boolean allow(String userId, long now) {
            Window w = windows.computeIfAbsent(userId, k -> new Window());
            synchronized (w) {
                long start = now - Math.floorMod(now, windowSeconds); // align to a fixed window boundary
                if (w.windowStart == Long.MIN_VALUE) {
                    w.windowStart = start;                            // first ever call
                } else if (start != w.windowStart) {
                    // Advanced exactly one window -> the old current becomes prev.
                    // Jumped further (idle) -> there is no meaningful previous window.
                    w.prev = (start == w.windowStart + windowSeconds) ? w.current : 0;
                    w.current = 0;
                    w.windowStart = start;
                }
                double weightPrev = (double) (windowSeconds - (now - start)) / windowSeconds;
                double estimate = w.prev * weightPrev + w.current;
                if (estimate < maxRequests) {
                    w.current++;
                    return true;
                }
                return false;
            }
        }
    }

    // ========================================================================
    // PART 4 — LEAKY BUCKET   (fixed-throughput framing)
    //
    // CHANGE vs Part 1: this is the mirror image of the token bucket. Instead of
    // tokens refilling up to a cap, "water" leaks out at a constant rate and each
    // request adds a drop; admit iff the bucket would not overflow.
    //     capacity  = maxRequests
    //     leakPerSec = maxRequests / windowSeconds
    // It is isomorphic to the token bucket (water == capacity - tokens), so pick
    // token bucket unless the interviewer explicitly wants leaky's smoother,
    // fixed-drain mental model.
    // ========================================================================
    static final class LeakyBucketRateLimiter implements RateLimiter {
        private final double capacity;
        private final double leakPerSec;
        private final ConcurrentHashMap<String, Cell> cells = new ConcurrentHashMap<>();

        private static final class Cell {
            double water;
            long lastLeak;
            Cell(double water, long lastLeak) { this.water = water; this.lastLeak = lastLeak; }
        }

        LeakyBucketRateLimiter(int maxRequests, int windowSeconds) {
            this.capacity = maxRequests;
            this.leakPerSec = (double) maxRequests / windowSeconds;
        }

        @Override public boolean allow(String userId, long now) {
            Cell c = cells.computeIfAbsent(userId, k -> new Cell(0.0, now)); // new user: empty bucket
            synchronized (c) {
                long elapsed = Math.max(0, now - c.lastLeak);
                c.water = Math.max(0.0, c.water - elapsed * leakPerSec);     // drain
                c.lastLeak = now;
                if (c.water + 1.0 <= capacity + EPS) {                       // room for one more drop?
                    c.water += 1.0;
                    return true;
                }
                return false;
            }
        }
    }

    // ========================================================================
    // PART 5 — FOLLOW-UPS
    //
    // (a) PER-ENDPOINT QUOTA (multi-key): each endpoint gets its own limiter
    //     instance (its own max/window), and each limiter keys per-user inside.
    //     Contract unchanged — we just compose the keys.
    // (b) IDLE-USER PRUNING: see SlidingWindowLogRateLimiter.pruneIdle above; for
    //     the buckets, a user whose bucket is FULL again (token) / EMPTY (leaky)
    //     carries no state and can be evicted (an LRU/TTL map or a sweeper thread).
    // (c) DISTRIBUTED (Redis): keep the SAME algorithm, move state to Redis and
    //     make the read-modify-write ATOMIC with a Lua script. One primitive per
    //     algorithm:  sliding-log -> ZADD + ZREMRANGEBYSCORE + ZCARD;
    //     fixed-window -> INCR + EXPIRE;  token bucket -> HGET/HSET in Lua (GCRA).
    //     Hard parts: (1) one authoritative clock — pass server time / Redis TIME,
    //     don't trust each node; (2) the Redis call's own failure mode —
    //     fail-OPEN (allow on error: availability first) vs fail-CLOSED (deny:
    //     correctness/abuse-protection first). Pick per use case.
    // ========================================================================
    static final class MultiKeyRateLimiter {
        private final ConcurrentHashMap<String, RateLimiter> perEndpoint = new ConcurrentHashMap<>();
        private final Function<String, RateLimiter> factory; // endpoint -> a limiter with that endpoint's config

        MultiKeyRateLimiter(Function<String, RateLimiter> factory) { this.factory = factory; }

        boolean allow(String userId, String endpoint, long now) {
            RateLimiter rl = perEndpoint.computeIfAbsent(endpoint, factory);
            return rl.allow(userId, now); // per-user keying happens inside the chosen limiter
        }
    }

    // ========================================================================
    // ALTERNATE CANONICAL VARIANT — LOGGER (per-message 10s cooldown, LC 359)
    //
    // Key on the MESSAGE, not a user. Exclusive-of-full-window cooldown: a message
    // printed at t is blocked through t+9 and printable again at t+10, i.e.
    //     allow  <=>  timestamp >= lastPrinted[msg] + 10
    // Update lastPrinted ONLY on a True result. Because timestamps arrive
    // non-decreasing, a single map (message -> lastPrintedTimestamp) is O(1) per
    // call and needs no eviction for correctness.
    // ========================================================================
    static final class Logger {
        private static final int COOLDOWN = 10;
        private final Map<String, Long> lastPrinted = new HashMap<>();

        boolean shouldPrintMessage(long timestamp, String message) {
            Long last = lastPrinted.get(message);
            if (last == null || timestamp >= last + COOLDOWN) { // exclusive boundary: printable at last+10
                lastPrinted.put(message, timestamp);            // record ONLY when we actually print
                return true;
            }
            return false;
        }
    }

    /**
     * Bounded Logger — same semantics, but caps memory at O(distinct messages
     * seen in the last window) by lazily evicting expired entries. Exploits the
     * monotonic-timestamp guarantee: an expiry queue is drained from the front,
     * and a message is unmapped only if its LATEST print has also expired
     * (so a re-printed message is not dropped early).
     */
    static final class BoundedLogger {
        private static final int COOLDOWN = 10;
        private final Map<String, Long> lastPrinted = new HashMap<>();
        private final Deque<Entry> expiry = new ArrayDeque<>();     // FIFO of prints, oldest first

        private static final class Entry {
            final long expiresAt;   // stamp + COOLDOWN
            final String message;
            final long stamp;       // the print time this entry records
            Entry(long expiresAt, String message, long stamp) {
                this.expiresAt = expiresAt; this.message = message; this.stamp = stamp;
            }
        }

        boolean shouldPrintMessage(long timestamp, String message) {
            // Lazy cleanup: unmap any message whose window has fully elapsed AND that
            // has not been re-printed since (lastPrinted still equals this entry's stamp).
            while (!expiry.isEmpty() && expiry.peekFirst().expiresAt <= timestamp) {
                Entry e = expiry.pollFirst();
                Long lp = lastPrinted.get(e.message);
                if (lp != null && lp == e.stamp) lastPrinted.remove(e.message); // (Long unboxed vs long)
            }
            Long last = lastPrinted.get(message);
            if (last == null || timestamp >= last + COOLDOWN) {
                lastPrinted.put(message, timestamp);
                expiry.addLast(new Entry(timestamp + COOLDOWN, message, timestamp));
                return true;
            }
            return false;
        }
        int trackedMessages() { return lastPrinted.size(); }
    }

    // ========================================================================
    // TESTS — self-checking. `now` is an explicit argument, so tests are
    // deterministic with NO sleeping.
    // ========================================================================
    static int testsRun = 0, testsPassed = 0;
    static void check(String label, boolean cond) {
        testsRun++;
        if (cond) testsPassed++;
        System.out.println((cond ? "  PASS  " : "  FAIL  ") + label);
    }

    static void testTokenBucket() {
        System.out.println("PART 1 — Token Bucket (max=3, window=3 -> 1 token/sec, cap 3):");
        RateLimiter rl = new TokenBucketRateLimiter(3, 3);
        check("burst 3 at t=0", rl.allow("u", 0) && rl.allow("u", 0) && rl.allow("u", 0));
        check("4th at t=0 blocked", !rl.allow("u", 0));
        check("t=1: +1 token -> allow", rl.allow("u", 1));
        check("t=1: 2nd blocked",       !rl.allow("u", 1));
        check("t=3: +2 tokens -> allow twice", rl.allow("u", 3) && rl.allow("u", 3));
        check("t=3: 3rd blocked",        !rl.allow("u", 3));
        check("separate user is independent", new TokenBucketRateLimiter(3, 3).allow("v", 0));
    }

    static void testSlidingWindowLog() {
        System.out.println("PART 2 — Sliding Window Log (max=3, window=10):");
        RateLimiter rl = new SlidingWindowLogRateLimiter(3, 10);
        check("t=1,2,3 admitted", rl.allow("u", 1) && rl.allow("u", 2) && rl.allow("u", 3));
        check("t=4 blocked (3 in window)", !rl.allow("u", 4));
        check("t=11 admitted (t=1 evicted)", rl.allow("u", 11));
        check("t=12 admitted (t=2 evicted)", rl.allow("u", 12));

        // Explicitly exercise the mass-eviction while-loop path.
        SlidingWindowLogRateLimiter big = new SlidingWindowLogRateLimiter(3, 10);
        big.allow("w", 0); big.allow("w", 1); big.allow("w", 2);       // window full: [0,1,2]
        check("blocked before jump", !big.allow("w", 2));
        check("jump to t=100 evicts ALL 3", big.allow("w", 100)
                && big.allow("w", 100) && big.allow("w", 100));         // 3 fresh slots => all evicted
        check("blocked again after refill", !big.allow("w", 100));

        // Pruning idle users.
        check("pruneIdle removes empty user", big.pruneIdle(1000) >= 1);
    }

    static void testSlidingWindowCounter() {
        System.out.println("PART 3 — Sliding Window Counter (max=10, window=10):");
        RateLimiter rl = new SlidingWindowCounterRateLimiter(10, 10);
        boolean all10 = true;
        for (int i = 0; i < 10; i++) all10 &= rl.allow("u", 0);         // fill current window [0,10)
        check("10 admitted in window 0", all10);
        check("11th at t=0 blocked", !rl.allow("u", 0));
        check("t=5 still blocked (current=10)", !rl.allow("u", 5));
        check("t=10 blocked (full prev counts fully)", !rl.allow("u", 10));
        check("t=15 admitted (prev weighted 0.5 -> est 5)", rl.allow("u", 15));
    }

    static void testLeakyBucket() {
        System.out.println("PART 4 — Leaky Bucket (max=3, window=3 -> leak 1/sec, cap 3):");
        RateLimiter rl = new LeakyBucketRateLimiter(3, 3);
        check("burst 3 at t=0", rl.allow("u", 0) && rl.allow("u", 0) && rl.allow("u", 0));
        check("4th at t=0 blocked", !rl.allow("u", 0));
        check("t=1: leaked 1 -> allow once", rl.allow("u", 1));
        check("t=1: 2nd blocked",             !rl.allow("u", 1));
        check("t=3: leaked 2 -> allow twice", rl.allow("u", 3) && rl.allow("u", 3));
        check("t=3: 3rd blocked",              !rl.allow("u", 3));
    }

    static void testMultiKey() {
        System.out.println("PART 5 — Per-endpoint quota (multi-key):");
        // /login allows 1 per 60s; /search allows 5 per 60s. Independent per endpoint AND per user.
        MultiKeyRateLimiter mk = new MultiKeyRateLimiter(endpoint ->
                endpoint.equals("/login")
                        ? new TokenBucketRateLimiter(1, 60)
                        : new TokenBucketRateLimiter(5, 60));
        check("/login: 1st ok",        mk.allow("u", "/login", 0));
        check("/login: 2nd blocked",  !mk.allow("u", "/login", 0));
        check("/search: independent",  mk.allow("u", "/search", 0));
        check("other user /login ok",  mk.allow("v", "/login", 0));
    }

    static void testLogger() {
        System.out.println("ALTERNATE — Logger (per-message 10s cooldown):");
        Logger lg = new Logger();
        check("foo@1 -> true",  lg.shouldPrintMessage(1, "foo"));
        check("foo@3 -> false", !lg.shouldPrintMessage(3, "foo"));   // within 10s window
        check("bar@2 -> true (independent)", lg.shouldPrintMessage(2, "bar"));
        check("foo@11 -> true (11 >= 1+10)", lg.shouldPrintMessage(11, "foo"));
        check("foo@11 repeat -> false",     !lg.shouldPrintMessage(11, "foo")); // same ts blocked
        check("foo@20 -> false (20 < 11+10)", !lg.shouldPrintMessage(20, "foo"));
        check("foo@21 -> true (21 >= 11+10)",  lg.shouldPrintMessage(21, "foo"));

        BoundedLogger bl = new BoundedLogger();
        bl.shouldPrintMessage(1, "a");
        bl.shouldPrintMessage(2, "b");
        check("bounded: prints once each", bl.trackedMessages() == 2);
        bl.shouldPrintMessage(100, "c");                              // t=100 evicts a,b (expired)
        check("bounded: idle entries evicted", bl.trackedMessages() == 1);
    }

    public static void main(String[] args) {
        testTokenBucket();
        testSlidingWindowLog();
        testSlidingWindowCounter();
        testLeakyBucket();
        testMultiKey();
        testLogger();
        System.out.printf("%n==== %d / %d checks passed ====%n", testsPassed, testsRun);
    }
}
