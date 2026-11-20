import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Robinhood — "Design a constrained data structure for concurrency and resource
 *              abuse scenarios"
 * Source: 1point3acres 6fa97fba-0586-408e-a3d1-b4d29293ece6
 *
 * "A constrained data structure supporting access in highly concurrent
 *  scenarios", focused on abnormal conditions and resource misuse. The stated
 *  requirements name permission checks, input validation, and "safety and
 *  controllability under high concurrency, malicious input, or abnormal requests".
 *
 * Read it as: a BOUNDED structure with ADMISSION CONTROL. See
 * ../cribs/06-security-designs.md for the full design discussion.
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE INSIGHT THIS FILE EXISTS TO DEMONSTRATE (part3):
 *
 * Eviction means different things depending on what you store:
 *   - in a CACHE, eviction causes a MISS  -> a performance problem
 *   - in a RATE LIMITER, eviction RESETS THE COUNTER -> a SECURITY BYPASS
 *
 * So a "bounded map of rate-limit buckets with LRU eviction" — which looks like
 * responsible engineering — is an exploitable quota bypass. An attacker floods
 * the map with junk principals, evicts the victim's bucket, and the victim's
 * (or their own) quota resets to full. part3 demonstrates the bypass and then
 * the fix. Almost nobody raises this, and it is the strongest thing you can say
 * in this question.
 * ---------------------------------------------------------------------------
 */
public class ConstrainedStructure {

    /* ===================================================================
     * PART 1 — A bounded store with per-key striped locking.
     *
     * Bounds on EVERY dimension: entry count, key size, value size, total bytes.
     * Striped locks rather than one global lock — a global lock makes the
     * structure itself the denial-of-service target, because an attacker only
     * needs to make ONE operation slow to stall everyone.
     * =================================================================== */
    static class BoundedStore {
        private final ConcurrentHashMap<String, byte[]> map = new ConcurrentHashMap<>();
        private final AtomicLong totalBytes = new AtomicLong();
        private final int maxEntries, maxKeyBytes, maxValueBytes;
        private final long maxTotalBytes;
        private final Object[] stripes;

        BoundedStore(int maxEntries, int maxKeyBytes, int maxValueBytes, long maxTotalBytes, int stripeCount) {
            this.maxEntries = maxEntries;
            this.maxKeyBytes = maxKeyBytes;
            this.maxValueBytes = maxValueBytes;
            this.maxTotalBytes = maxTotalBytes;
            this.stripes = new Object[stripeCount];
            for (int i = 0; i < stripeCount; i++) stripes[i] = new Object();
        }

        private Object stripeFor(String key) {
            return stripes[Math.floorMod(key.hashCode(), stripes.length)];
        }

        enum Result { OK, KEY_TOO_LARGE, VALUE_TOO_LARGE, AT_CAPACITY, OVER_BYTE_BUDGET }

        private final AtomicInteger entryCount = new AtomicInteger();

        /**
         * ⭐⭐ THE SUBTLE BUG THIS METHOD EXISTS TO GET RIGHT.
         *
         * The first version of this file did:
         *     synchronized (stripeFor(key)) {
         *         if (old == null && map.size() >= maxEntries) return AT_CAPACITY;
         *         map.put(key, value);
         *     }
         * and it OVER-ADMITTED — the concurrency test at the bottom stored 10003
         * entries against a cap of 10000.
         *
         * Why: STRIPED LOCKS CANNOT ENFORCE A GLOBAL INVARIANT. Two threads
         * writing different keys hold DIFFERENT stripe locks, so both can read
         * size() == 9999 and both insert. With 64 stripes the cap can be exceeded
         * by up to 63. The same race applies to the byte budget.
         *
         * The fix: a global bound needs a global atomic. RESERVE the slot (and the
         * bytes) with a compare-and-set loop, then insert. The stripe lock still
         * does its real job — serialising same-key read-modify-write — but the
         * capacity decision is made against an atomic counter, not a size() read.
         *
         * This is exactly the "check-then-act is not atomic" failure the question
         * is probing, and being able to name WHICH invariant each lock protects is
         * the answer they want.
         */
        Result put(String key, int declaredValueSize, byte[] value) {
            if (key == null || key.length() > maxKeyBytes) return Result.KEY_TOO_LARGE;
            if (declaredValueSize > maxValueBytes) return Result.VALUE_TOO_LARGE;   // on DECLARED size
            if (value.length > maxValueBytes) return Result.VALUE_TOO_LARGE;

            synchronized (stripeFor(key)) {
                byte[] old = map.get(key);          // safe: same key => same stripe
                int delta = value.length - (old == null ? 0 : old.length);

                // reserve an entry slot atomically (only when adding a NEW key)
                if (old == null) {
                    while (true) {
                        int cur = entryCount.get();
                        if (cur >= maxEntries) return Result.AT_CAPACITY;
                        if (entryCount.compareAndSet(cur, cur + 1)) break;
                    }
                }
                // reserve bytes atomically; roll the entry reservation back on failure
                while (true) {
                    long cur = totalBytes.get();
                    if (cur + delta > maxTotalBytes) {
                        if (old == null) entryCount.decrementAndGet();   // compensate
                        return Result.OVER_BYTE_BUDGET;
                    }
                    if (totalBytes.compareAndSet(cur, cur + delta)) break;
                }
                map.put(key, value);
                return Result.OK;
            }
        }

        byte[] get(String key) { return map.get(key); }
        int size() { return map.size(); }
        long bytes() { return totalBytes.get(); }
    }

    /* ===================================================================
     * PART 2 — Admission control: a per-principal token bucket.
     *
     * CHANGES FROM PART 1:
     *   - every operation must first acquire a token for its principal
     *   - atomic compute() so two threads cannot both see the same token count
     *
     * ⚠️ compute() holds a BIN LOCK. Never do I/O, call out, or touch another
     * key inside it — that is how you turn a limiter into a deadlock.
     * =================================================================== */
    static class TokenBucket {
        final long capacity;
        final double refillPerMs;
        double tokens;
        long lastRefillMs;

        TokenBucket(long capacity, double refillPerMs, long nowMs) {
            this.capacity = capacity;
            this.refillPerMs = refillPerMs;
            this.tokens = capacity;
            this.lastRefillMs = nowMs;
        }
        boolean tryConsume(long nowMs) {
            double add = (nowMs - lastRefillMs) * refillPerMs;
            if (add > 0) { tokens = Math.min(capacity, tokens + add); lastRefillMs = nowMs; }
            if (tokens >= 1.0) { tokens -= 1.0; return true; }
            return false;
        }
    }

    /* ===================================================================
     * PART 3 — ⭐ THE EVICTION BYPASS, and the fix.
     *
     * VULNERABLE: buckets live in a bounded LinkedHashMap with LRU eviction. The
     * bound looks responsible — it stops unbounded memory growth from unbounded
     * principals. But evicting a bucket DISCARDS ITS CONSUMED-TOKEN STATE, so
     * the next request from that principal starts from a FULL bucket.
     *
     * SAFE: never evict a bucket that could still be rate-limiting. Only reclaim
     * buckets idle for longer than a full refill window (by then the bucket would
     * have refilled to capacity anyway, so dropping it changes nothing). If the
     * map is at capacity and NOTHING is reclaimable, FAIL CLOSED — reject the new
     * principal rather than evict a live counter.
     * =================================================================== */
    interface Limiter { boolean allow(String principal, long nowMs); }

    /** ⛔ Vulnerable: LRU eviction of live counters. */
    static class LruLimiter implements Limiter {
        private final LinkedHashMap<String, TokenBucket> buckets;
        private final long capacity;
        private final double refillPerMs;
        int evictions = 0;

        LruLimiter(int maxPrincipals, long capacity, double refillPerMs) {
            this.capacity = capacity;
            this.refillPerMs = refillPerMs;
            this.buckets = new LinkedHashMap<>(16, 0.75f, true) {
                @Override protected boolean removeEldestEntry(Map.Entry<String, TokenBucket> e) {
                    if (size() > maxPrincipals) { evictions++; return true; }   // ⛔ drops consumed state
                    return false;
                }
            };
        }
        public synchronized boolean allow(String p, long nowMs) {
            TokenBucket b = buckets.computeIfAbsent(p, k -> new TokenBucket(capacity, refillPerMs, nowMs));
            return b.tryConsume(nowMs);
        }
    }

    /** ✅ Safe: reclaim only fully-refilled idle buckets; fail closed otherwise. */
    static class SafeLimiter implements Limiter {
        private final Map<String, TokenBucket> buckets = new HashMap<>();
        private final int maxPrincipals;
        private final long capacity;
        private final double refillPerMs;
        private final long fullRefillMs;
        int rejectedForCapacity = 0;

        SafeLimiter(int maxPrincipals, long capacity, double refillPerMs) {
            this.maxPrincipals = maxPrincipals;
            this.capacity = capacity;
            this.refillPerMs = refillPerMs;
            this.fullRefillMs = (long) Math.ceil(capacity / refillPerMs);
        }

        public synchronized boolean allow(String p, long nowMs) {
            TokenBucket b = buckets.get(p);
            if (b == null) {
                if (buckets.size() >= maxPrincipals && !reclaim(nowMs)) {
                    rejectedForCapacity++;
                    return false;                       // ⭐ fail CLOSED, never evict a live counter
                }
                b = new TokenBucket(capacity, refillPerMs, nowMs);
                buckets.put(p, b);
            }
            return b.tryConsume(nowMs);
        }

        /** Reclaim only buckets idle long enough to have refilled fully — dropping
         *  those is information-free, so it cannot grant extra quota. */
        private boolean reclaim(long nowMs) {
            Iterator<Map.Entry<String, TokenBucket>> it = buckets.entrySet().iterator();
            boolean freed = false;
            while (it.hasNext()) {
                TokenBucket b = it.next().getValue();
                if (nowMs - b.lastRefillMs >= fullRefillMs) { it.remove(); freed = true; }
            }
            return freed;
        }
        synchronized int size() { return buckets.size(); }
    }

    /* ===================================================================
     * PART 4 — Authorization, and the full layered path.
     *
     * ⭐ Note the layer order and that AUTHENTICATION is its own step. A quota
     * keyed on a CALLER-SUPPLIED principal is spoofable — an attacker exhausts
     * someone else's bucket. The principal must be SERVER-DERIVED.
     * =================================================================== */
    interface Authenticator { String authenticate(String credential); }   // null == unauthenticated
    interface Authorizer { boolean mayWrite(String principal, String key); }

    static class Guarded {
        private final Authenticator authn;
        private final Authorizer authz;
        private final Limiter limiter;
        private final BoundedStore store;
        final AtomicInteger unauth = new AtomicInteger(), throttled = new AtomicInteger(),
                            denied = new AtomicInteger(), stored = new AtomicInteger();

        Guarded(Authenticator authn, Authorizer authz, Limiter limiter, BoundedStore store) {
            this.authn = authn; this.authz = authz; this.limiter = limiter; this.store = store;
        }

        enum Outcome { UNAUTHENTICATED, THROTTLED, FORBIDDEN, REJECTED_BOUNDS, OK }

        Outcome put(String credential, String key, byte[] value, long nowMs) {
            // 1. cheap byte bounds first — no identity needed, and cheapest to reject
            if (key == null || value == null || key.isEmpty()) return Outcome.REJECTED_BOUNDS;
            // 2. AUTHENTICATE -> server-derived principal (never trust a supplied id)
            String principal = authn.authenticate(credential);
            if (principal == null) { unauth.incrementAndGet(); return Outcome.UNAUTHENTICATED; }
            // 3. per-principal quota, on the DERIVED principal
            if (!limiter.allow(principal, nowMs)) { throttled.incrementAndGet(); return Outcome.THROTTLED; }
            // 4. object-level authorization BEFORE touching storage
            if (!authz.mayWrite(principal, key)) { denied.incrementAndGet(); return Outcome.FORBIDDEN; }
            // 5. bounded storage
            BoundedStore.Result r = store.put(key, value.length, value);
            if (r != BoundedStore.Result.OK) return Outcome.REJECTED_BOUNDS;
            stored.incrementAndGet();
            return Outcome.OK;
        }
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) throws Exception {
        System.out.println("=== part1: bounds are enforced ===");
        BoundedStore s = new BoundedStore(3, 10, 8, 20, 16);
        System.out.println("  put ok            = " + s.put("a", 4, new byte[4]));
        System.out.println("  key too large     = " + s.put("k".repeat(11), 1, new byte[1]));
        System.out.println("  value too large   = " + s.put("b", 99, new byte[99]));
        s.put("b", 4, new byte[4]); s.put("c", 4, new byte[4]);
        System.out.println("  4th entry         = " + s.put("d", 4, new byte[4]) + "  (maxEntries=3)");
        System.out.println("  byte budget       = " + s.put("a", 8, new byte[8])
                + "  size=" + s.size() + " bytes=" + s.bytes());

        System.out.println("\n=== ⭐ part3: THE EVICTION BYPASS ===");
        // Quota: 5 requests per principal. Bucket map bounded to 2 principals.
        // "victim" spends its 5, then an attacker cycles junk principals to evict
        // the victim's bucket. Under LRU the victim gets a FULL quota back.
        long now = 1_000_000L;
        LruLimiter lru = new LruLimiter(2, 5, 0.0);       // refill 0 => quota never refills by time
        int allowedBefore = 0;
        for (int i = 0; i < 5; i++) if (lru.allow("victim", now)) allowedBefore++;
        boolean sixth = lru.allow("victim", now);
        System.out.println("  victim used " + allowedBefore + "/5, 6th request allowed? " + sixth
                + "  (correct: false)");
        for (int i = 0; i < 10; i++) lru.allow("junk" + i, now);   // flood, evicting victim
        boolean afterEviction = lru.allow("victim", now);
        System.out.println("  after attacker floods " + lru.evictions + " evictions,");
        System.out.println("  victim allowed again? " + afterEviction
                + "   <-- " + (afterEviction ? "⛔ QUOTA BYPASS" : "ok"));

        System.out.println("\n  --- the fix: never evict a live counter ---");
        SafeLimiter safe = new SafeLimiter(2, 5, 0.0);
        int a2 = 0;
        for (int i = 0; i < 5; i++) if (safe.allow("victim", now)) a2++;
        System.out.println("  victim used " + a2 + "/5, 6th allowed? " + safe.allow("victim", now));
        int junkAllowed = 0;
        for (int i = 0; i < 10; i++) if (safe.allow("junk" + i, now)) junkAllowed++;
        boolean afterSafe = safe.allow("victim", now);
        System.out.println("  attacker floods 10 principals: " + junkAllowed + " admitted, "
                + safe.rejectedForCapacity + " rejected for capacity (fail closed)");
        System.out.println("  victim allowed again? " + afterSafe
                + "   <-- " + (!afterSafe ? "✅ quota HELD" : "⛔ still bypassable"));

        System.out.println("\n=== part2/4: layered path, and the counts ===");
        Authenticator authn = cred -> cred != null && cred.startsWith("tok:") ? cred.substring(4) : null;
        Authorizer authz = (p, k) -> k.startsWith(p + "/");        // may only write own namespace
        Guarded g = new Guarded(authn, authz, new SafeLimiter(100, 3, 0.0),
                                new BoundedStore(100, 64, 64, 4096, 16));
        System.out.println("  no credential        -> " + g.put(null, "alice/x", new byte[4], now));
        System.out.println("  bad credential       -> " + g.put("nope", "alice/x", new byte[4], now));
        System.out.println("  wrong namespace      -> " + g.put("tok:alice", "bob/x", new byte[4], now));
        System.out.println("  ok                   -> " + g.put("tok:alice", "alice/x", new byte[4], now));
        System.out.println("  ok                   -> " + g.put("tok:alice", "alice/y", new byte[4], now));
        System.out.println("  4th (quota 3)        -> " + g.put("tok:alice", "alice/z", new byte[4], now));
        System.out.println("  counts: unauth=" + g.unauth.get() + " throttled=" + g.throttled.get()
                + " denied=" + g.denied.get() + " stored=" + g.stored.get());
        System.out.println("  ⭐ note the wrong-namespace attempt consumed a token before being denied —");
        System.out.println("     that ordering is deliberate: quota protects the authz check itself.");

        System.out.println("\n=== concurrency: 16 threads, quota must hold exactly ===");
        SafeLimiter cl = new SafeLimiter(10, 100, 0.0);
        int threads = 16, perThread = 50;
        AtomicInteger allowed = new AtomicInteger();
        CountDownLatch go = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try { go.await(); } catch (InterruptedException ignored) {}
                for (int i = 0; i < perThread; i++) if (cl.allow("shared", now)) allowed.incrementAndGet();
            });
        }
        go.countDown();
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println("  " + threads * perThread + " concurrent attempts against a quota of 100");
        System.out.println("  allowed = " + allowed.get() + "  -> "
                + (allowed.get() == 100 ? "PASS — no over-admission under contention" : "FAIL"));

        System.out.println("\n=== striped vs global lock, under a slow victim key ===");
        BoundedStore striped = new BoundedStore(10_000, 64, 64, 1 << 20, 64);
        long t0 = System.nanoTime();
        ExecutorService p2 = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 8; i++) {
            final int id = i;
            p2.submit(() -> {
                for (int j = 0; j < 20_000; j++) striped.put("k" + id + "_" + j, 4, new byte[4]);
            });
        }
        p2.shutdown();
        p2.awaitTermination(60, TimeUnit.SECONDS);
        System.out.println("  8 threads x 20k puts across 64 stripes: "
                + (System.nanoTime() - t0) / 1_000_000 + " ms, stored=" + striped.size()
                + " (capped at 10000 -> " + (striped.size() == 10_000 ? "PASS" : "FAIL") + ")");
        System.out.println("  ⭐ with ONE global lock every thread serialises here — and an attacker");
        System.out.println("     who makes a single op slow stalls the whole structure.");
    }
}
