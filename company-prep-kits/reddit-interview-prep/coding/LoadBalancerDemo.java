import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Load Balancer — Debugging & Extension (Reddit Staff SWE coding exercise).
 *
 * The interview gives you a small load-balancer codebase and asks you to work through four
 * SEQUENTIAL parts. The key design signal (stated in the prompt) is to keep three concerns
 * SEPARATE so later parts never force you to rewrite earlier fixes:
 *
 *     1. Fault detection      -> {@link HealthChecker}   (is the process even up?)
 *     2. Request admission     -> {@link CircuitBreaker}  (should we send traffic there right now?)
 *     3. Backend selection     -> {@link RoutingPolicy}   (of the admitted set, which one?)
 *
 * The {@link LoadBalancer} just COMPOSES those three; each part below plugs into exactly one of
 * them. This file is fully runnable ("java LoadBalancerDemo") and the main() method demonstrates
 * each part in order and prints the evidence a debugger would rely on.
 */
public class LoadBalancerDemo {

    // =====================================================================================
    // Simulated backend process. Stands in for a real HTTP server so the demo is hermetic.
    // A real codebase would replace this with an actual socket/HTTP client call.
    // =====================================================================================
    static final class Server {
        final String host;
        final int port;                       // the ONE port this process is bound to (traffic + health)
        private volatile boolean up = true;   // is the process alive?
        private final long baseLatencyMillis;         // normal per-request processing cost
        private final Map<String, Long> pathSleeps;   // PART 2: injected excessive sleep on a specific path

        Server(String host, int port, long baseLatencyMillis, Map<String, Long> pathSleeps) {
            this.host = host;
            this.port = port;
            this.baseLatencyMillis = baseLatencyMillis;
            this.pathSleeps = pathSleeps;
        }

        /** Simulate handling a request. Returns an HTTP-like status code. */
        int handle(int targetPort, String path) {
            if (!up) return 503;                 // process down -> Service Unavailable
            if (targetPort != this.port) return 500; // hit the WRONG port -> Internal Server Error (Part 1)
            sleepQuietly(baseLatencyMillis + pathSleeps.getOrDefault(path, 0L));
            return 200;
        }

        void stop() { this.up = false; }
    }

    /** The load balancer's *view* of a backend (its config/registry entry). */
    static final class BackendConfig {
        final String id;
        final String host;
        final int port;         // service port (correct value)
        final int healthPort;   // PART 1: a separate health-check port field -> the bug lives here

        BackendConfig(String id, String host, int port, int healthPort) {
            this.id = id;
            this.host = host;
            this.port = port;
            this.healthPort = healthPort;
        }
    }

    // =====================================================================================
    // PART 2 building block: latency instrumentation (also feeds PART 4 routing).
    // Thread-safe; cheap to update on the hot path.
    // =====================================================================================
    static final class LatencyStats {
        private final AtomicLong count = new AtomicLong();
        private final AtomicLong totalNanos = new AtomicLong();
        private final AtomicLong maxNanos = new AtomicLong();
        private static final double ALPHA = 0.3;   // EMA smoothing: weight on the newest sample
        private volatile double emaMillis = -1.0;  // -1 => no samples yet (cold start)

        /** Called once per request with the measured wall-clock latency. */
        void record(long nanos) {
            count.incrementAndGet();
            totalNanos.addAndGet(nanos);
            maxNanos.accumulateAndGet(nanos, Math::max);
            double ms = nanos / 1_000_000.0;
            synchronized (this) {
                // Exponentially-weighted moving average => reacts to recent behavior, not all-time history.
                emaMillis = (emaMillis < 0) ? ms : ALPHA * ms + (1 - ALPHA) * emaMillis;
            }
        }

        boolean hasData()     { return count.get() > 0; }
        long    samples()     { return count.get(); }
        double  meanMillis()  { long c = count.get(); return c == 0 ? 0.0 : (totalNanos.get() / 1_000_000.0) / c; }
        double  maxMillis()   { return maxNanos.get() / 1_000_000.0; }
        double  emaMillis()   { double e = emaMillis; return e < 0 ? 0.0 : e; }
    }

    // =====================================================================================
    // CONCERN 1 — FAULT DETECTION.
    // Periodically probes each backend's health endpoint and records up/down.
    // =====================================================================================
    static final class HealthChecker {
        private final Map<String, Boolean> healthy = new ConcurrentHashMap<>();

        boolean isHealthy(String id) { return healthy.getOrDefault(id, false); }

        /**
         * PART 1 — the ORIGINAL (buggy) health check.
         * It targets {@code cfg.healthPort}, which was misconfigured to a port nothing is bound to.
         * The probe therefore hits the wrong endpoint and comes back 500, so every healthy host
         * gets flipped to UNHEALTHY -> the LB has nowhere to route -> repeated 500/503 to clients.
         */
        void checkBuggy(BackendConfig cfg, Server server) {
            int code = server.handle(cfg.healthPort, "/healthz"); // BUG: wrong port field
            healthy.put(cfg.id, code == 200);
        }

        /**
         * PART 1 — the FIX.
         * Health and traffic are served on the SAME port; probe the real service port. Root-caused by
         * logging the constructed health target and seeing port 9090 while the process listens on 8080.
         */
        void check(BackendConfig cfg, Server server) {
            int code = server.handle(cfg.port, "/healthz"); // FIX: use the port the process is bound to
            healthy.put(cfg.id, code == 200);
        }
    }

    // =====================================================================================
    // CONCERN 2 — REQUEST ADMISSION (PART 3).
    // A per-backend circuit breaker. Even if fault detection is stale (checks run on an interval),
    // this gives fast, local protection: after N consecutive failures it OPENs and refuses traffic,
    // then lets a single probe through after a cooldown (HALF_OPEN).
    // =====================================================================================
    static final class CircuitBreaker {
        enum State { CLOSED, OPEN, HALF_OPEN }

        private final int failureThreshold;
        private final long openMillis;      // cooldown before we probe again
        private State state = State.CLOSED;
        private int consecutiveFailures = 0;
        private long openedAt = 0L;

        CircuitBreaker(int failureThreshold, long openMillis) {
            this.failureThreshold = failureThreshold;
            this.openMillis = openMillis;
        }

        /** Admission decision: may the LB send a request to this backend right now? */
        synchronized boolean allowRequest(long now) {
            if (state == State.OPEN) {
                if (now - openedAt >= openMillis) {
                    state = State.HALF_OPEN;   // cooldown elapsed -> allow ONE trial request
                    return true;
                }
                return false;                  // still open -> reject fast, never touch the backend
            }
            return true;                       // CLOSED or HALF_OPEN
        }

        synchronized void onSuccess() {
            consecutiveFailures = 0;
            state = State.CLOSED;              // recovery confirmed
        }

        synchronized void onFailure(long now) {
            consecutiveFailures++;
            // A failed probe in HALF_OPEN, or crossing the threshold in CLOSED, trips the breaker.
            if (state == State.HALF_OPEN || consecutiveFailures >= failureThreshold) {
                state = State.OPEN;
                openedAt = now;
            }
        }

        synchronized State state() { return state; }
    }

    // =====================================================================================
    // CONCERN 3 — BACKEND SELECTION (PART 4 swaps the implementation here).
    // Pluggable: the LB depends only on this interface, so replacing the policy is a one-liner.
    // =====================================================================================
    interface RoutingPolicy {
        String name();
        /** Choose one backend from the already-admitted (healthy + circuit-closed) set. */
        BackendConfig select(List<BackendConfig> admitted, Map<String, LatencyStats> stats);
    }

    /** The ORIGINAL policy: plain round-robin, ignores how each backend is actually performing. */
    static final class RoundRobinPolicy implements RoutingPolicy {
        private final AtomicInteger idx = new AtomicInteger();
        public String name() { return "round-robin"; }
        public BackendConfig select(List<BackendConfig> admitted, Map<String, LatencyStats> stats) {
            // floorMod keeps the index positive even after int overflow.
            return admitted.get(Math.floorMod(idx.getAndIncrement(), admitted.size()));
        }
    }

    /**
     * PART 4 — the REPLACEMENT policy: pick the backend with the lowest observed latency.
     * Reuses PART 2's LatencyStats (EMA) unchanged. Cold-start rule: any backend with no samples
     * is probed first so we never starve a fresh/recovered backend of measurements.
     */
    static final class LeastLatencyPolicy implements RoutingPolicy {
        public String name() { return "least-latency"; }
        public BackendConfig select(List<BackendConfig> admitted, Map<String, LatencyStats> stats) {
            BackendConfig best = null;
            double bestScore = Double.MAX_VALUE;
            for (BackendConfig c : admitted) {
                LatencyStats st = stats.get(c.id);
                // No data => score -1 so unmeasured backends always sort first (a warm-up probe).
                double score = st.hasData() ? st.emaMillis() : -1.0;
                if (score < bestScore) {
                    bestScore = score;
                    best = c;
                }
            }
            return best;
        }
    }

    static final class Response {
        final int status;
        final String servedBy; // backend id, or null if nothing was reachable
        Response(int status, String servedBy) { this.status = status; this.servedBy = servedBy; }
    }

    // =====================================================================================
    // The load balancer: COMPOSES the three concerns. Note it never rewrites them — each part
    // above plugs into exactly one collaborator.
    // =====================================================================================
    static final class LoadBalancer {
        private final List<BackendConfig> configs;
        private final Map<String, Server> servers;
        private final HealthChecker health;
        private final Map<String, CircuitBreaker> breakers = new ConcurrentHashMap<>();
        private final Map<String, LatencyStats> stats = new ConcurrentHashMap<>();
        private volatile RoutingPolicy policy;

        LoadBalancer(List<BackendConfig> configs, Map<String, Server> servers,
                     HealthChecker health, RoutingPolicy policy) {
            this.configs = configs;
            this.servers = servers;
            this.health = health;
            this.policy = policy;
            for (BackendConfig c : configs) {
                breakers.put(c.id, new CircuitBreaker(/*failureThreshold*/ 2, /*openMillis*/ 500));
                stats.put(c.id, new LatencyStats());
            }
        }

        void setPolicy(RoutingPolicy policy) { this.policy = policy; } // PART 4 hot-swap

        Map<String, LatencyStats> stats() { return stats; }
        CircuitBreaker breaker(String id) { return breakers.get(id); }

        Response route(String path) {
            long now = System.currentTimeMillis();

            // Concern 1 (fault detection) + Concern 2 (admission) => build the eligible set.
            List<BackendConfig> admitted = new ArrayList<>();
            for (BackendConfig c : configs) {
                if (health.isHealthy(c.id) && breakers.get(c.id).allowRequest(now)) {
                    admitted.add(c);
                }
            }
            if (admitted.isEmpty()) return new Response(503, null); // nothing safe to route to

            // Concern 3 (selection).
            BackendConfig chosen = policy.select(admitted, stats);

            // PART 2: instrument request latency around the actual backend call.
            long t0 = System.nanoTime();
            int code = servers.get(chosen.id).handle(chosen.port, path);
            long elapsedNanos = System.nanoTime() - t0;
            stats.get(chosen.id).record(elapsedNanos);

            // Feed the outcome back to the admission gate (Concern 2).
            if (code == 200) breakers.get(chosen.id).onSuccess();
            else             breakers.get(chosen.id).onFailure(now);

            return new Response(code, chosen.id);
        }
    }

    // ------------------------------------------------------------------------------------
    private static void sleepQuietly(long millis) {
        if (millis <= 0) return;
        try { Thread.sleep(millis); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // =====================================================================================
    // Demonstration of all four parts, in order.
    // =====================================================================================
    public static void main(String[] args) {
        // Three backend processes, all bound to port 8080. s3 has an injected 200ms sleep on "/".
        Map<String, Server> servers = new LinkedHashMap<>();
        servers.put("s1", new Server("10.0.0.1", 8080, 5, Map.of()));
        servers.put("s2", new Server("10.0.0.2", 8080, 8, Map.of()));
        servers.put("s3", new Server("10.0.0.3", 8080, 6, Map.of("/", 200L))); // PART 2 slow path

        // Registry: note healthPort=9090 (misconfigured) vs service port=8080.
        List<BackendConfig> configs = List.of(
                new BackendConfig("s1", "10.0.0.1", 8080, 9090),
                new BackendConfig("s2", "10.0.0.2", 8080, 9090),
                new BackendConfig("s3", "10.0.0.3", 8080, 9090));

        HealthChecker health = new HealthChecker();

        // ---------------- PART 1: health-check failure ----------------
        System.out.println("=== PART 1: Health-check failure ===");
        for (BackendConfig c : configs) health.checkBuggy(c, servers.get(c.id));
        System.out.println("BUGGY check (targets healthPort 9090 -> 500): " + healthSnapshot(health, configs));
        for (BackendConfig c : configs) health.check(c, servers.get(c.id));
        System.out.println("FIXED check (targets service port 8080): " + healthSnapshot(health, configs));

        LoadBalancer lb = new LoadBalancer(configs, servers, health, new RoundRobinPolicy());

        // ---------------- PART 2: latency diagnosis ----------------
        System.out.println("\n=== PART 2: Latency diagnosis (round-robin, instrumented) ===");
        for (int i = 0; i < 12; i++) lb.route("/");
        printLatency(lb);
        System.out.println("Diagnosis: s3 mean/max latency dominates -> excessive sleep on its \"/\" path.");

        // ---------------- PART 3: circuit breaker ----------------
        System.out.println("\n=== PART 3: Circuit breaker ===");
        servers.get("s2").stop(); // s2 crashes; health checks haven't re-run yet (stale = still 'healthy')
        System.out.println("s2 stopped. Health state is stale; breaker must protect fast.");
        for (int i = 0; i < 6; i++) {
            Response r = lb.route("/");
            System.out.printf("  req %d -> %s (status %d), s2 breaker=%s%n",
                    i, r.servedBy, r.status, lb.breaker("s2").state());
        }
        System.out.println("After 2 failures s2's breaker OPENs; LB stops selecting it.");

        // ---------------- PART 4: least-latency routing ----------------
        System.out.println("\n=== PART 4: Least-request-latency routing ===");
        // Recover s2 and refresh fault detection so all three are eligible again.
        servers.put("s2", new Server("10.0.0.2", 8080, 8, Map.of()));
        for (BackendConfig c : configs) health.check(c, servers.get(c.id));
        lb.breaker("s2").onSuccess(); // close the breaker after recovery

        lb.setPolicy(new LeastLatencyPolicy());   // <-- the ONLY change vs Part 2/3: swap the policy
        Map<String, Integer> hits = new LinkedHashMap<>();
        for (int i = 0; i < 30; i++) {
            Response r = lb.route("/");
            hits.merge(r.servedBy, 1, Integer::sum);
        }
        System.out.println("Request distribution: " + hits);
        System.out.println("Least-latency steers traffic toward fast s1/s2 and away from slow s3.");
    }

    private static String healthSnapshot(HealthChecker h, List<BackendConfig> configs) {
        StringBuilder sb = new StringBuilder("{");
        for (BackendConfig c : configs) sb.append(c.id).append('=').append(h.isHealthy(c.id)).append(' ');
        return sb.append('}').toString();
    }

    private static void printLatency(LoadBalancer lb) {
        for (Map.Entry<String, LatencyStats> e : lb.stats().entrySet()) {
            LatencyStats s = e.getValue();
            System.out.printf("  %s: n=%d mean=%.1fms max=%.1fms ema=%.1fms%n",
                    e.getKey(), s.samples(), s.meanMillis(), s.maxMillis(), s.emaMillis());
        }
    }
}
