/*
 * ============================================================================
 * Anthropic — Staff Software Engineer — Coding Round
 * Question: "Web Crawler"  (see web-crawler-prompt.txt)
 *
 * Crawl every URL reachable from startUrl whose hostname matches startUrl's
 * hostname. Never crawl the same URL twice. Order does not matter.
 * (LeetCode 1242 "Web Crawler Multithreaded" style.)
 *
 * This single file contains BOTH parts the interview walks through:
 *   Part 1  crawl()            — single-threaded BFS.
 *   Part 2  crawlConcurrent()  — bounded thread pool + thread-safe visited set
 *                                + correct termination.  *** the follow-up ***
 *           crawlConcurrentCF()— a cleaner CompletableFuture variant of Part 2.
 *
 * Assumptions from the prompt: all URLs are http with no port; URL
 * normalization is NOT required for the basic problem (fragments left as-is —
 * a clarifying question to raise with the interviewer).
 *
 * `main` builds mock link graphs (with cycles, a shared child, and an
 * out-of-scope host) and asserts that all three implementations return the
 * same set AND that each in-scope URL is fetched EXACTLY once (proving the
 * concurrent dedup has no race). Verified on OpenJDK Corretto 21.
 *
 *   Compile        : javac WebCrawler.java
 *   Run self-test  : java WebCrawler
 * ============================================================================
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WebCrawler {

    /** Provided by the interview harness; declared here so the file compiles standalone. */
    interface HtmlParser {
        List<String> getUrls(String url);
    }

    /** Bounded concurrency: cap threads so we never exhaust resources / hammer servers. */
    private static final int MAX_THREADS = 16;

    // =========================================================================
    // PART 1 — Single-threaded BFS.
    // Visit startUrl, expand its links, keep only same-host + unseen URLs.
    // Each page fetched exactly once -> O(V + E) time, O(V) space
    // (V = pages, E = links).
    // =========================================================================
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        String host = hostnameOf(startUrl);
        Set<String> visited = new HashSet<>();   // doubles as the dedup set AND the result
        Deque<String> queue = new ArrayDeque<>();

        visited.add(startUrl);
        queue.offer(startUrl);

        while (!queue.isEmpty()) {
            String url = queue.poll();
            for (String next : htmlParser.getUrls(url)) {
                // same hostname AND not seen before -> enqueue
                if (hostnameOf(next).equals(host) && visited.add(next)) {
                    queue.offer(next);           // visited.add returns true only when newly added
                }
            }
        }
        return new ArrayList<>(visited);
    }

    // =========================================================================
    // PART 2 (primary) — Multithreaded crawl.  *** the important follow-up ***
    //
    // CHANGES FROM PART 1, and why each is needed for correctness under threads:
    //   1. HashSet            -> ConcurrentHashMap.newKeySet(): a lock-free
    //      thread-safe set whose add() is ATOMIC and returns true for exactly
    //      one thread per URL -> that is our race-free "claim this URL" check.
    //   2. Explicit BFS queue -> a fixed-size thread pool (the work "queue" is
    //      the pool's internal task queue). Bounded => no thread explosion.
    //   3. The while-loop's natural "queue empty => done" termination is gone,
    //      because tasks spawn tasks. We track outstanding tasks with an
    //      AtomicInteger and signal a CountDownLatch when it hits zero.
    // No separate synchronized result list is needed: `visited` IS the result.
    // =========================================================================
    public List<String> crawlConcurrent(String startUrl, HtmlParser htmlParser) {
        final String host = hostnameOf(startUrl);
        final Set<String> visited = ConcurrentHashMap.newKeySet();   // change #1
        final ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS); // change #2
        final AtomicInteger pending = new AtomicInteger(0);          // change #3
        final CountDownLatch done = new CountDownLatch(1);

        visited.add(startUrl);
        submit(startUrl, host, htmlParser, visited, pool, pending, done);

        try {
            done.await();                  // block (main thread only) until all work finishes
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            pool.shutdown();               // let worker threads exit; all tasks already done
        }
        return new ArrayList<>(visited);
    }

    /** Register one crawl task, run it on the pool, and recursively spawn child tasks. */
    private void submit(String url, String host, HtmlParser parser, Set<String> visited,
                        ExecutorService pool, AtomicInteger pending, CountDownLatch done) {
        // Increment BEFORE the task can run/finish. Because a parent registers each
        // child before it decrements itself, `pending` can only reach 0 once the whole
        // crawl tree is truly drained -> no premature termination.
        pending.incrementAndGet();
        pool.execute(() -> {
            try {
                for (String next : safeGetUrls(parser, url)) {
                    // Atomic claim: only the FIRST thread to see `next` gets true from add(),
                    // so each in-scope URL is crawled exactly once even under contention.
                    if (hostnameOf(next).equals(host) && visited.add(next)) {
                        submit(next, host, parser, visited, pool, pending, done);
                    }
                }
            } finally {
                if (pending.decrementAndGet() == 0) {
                    done.countDown();      // last task standing wakes the main thread
                }
            }
        });
    }

    // =========================================================================
    // PART 2 (alternative) — same idea, expressed with CompletableFuture.
    // Cleaner termination: composing the futures means the root future completes
    // only when every transitively-spawned fetch has completed, so a single
    // join() replaces the manual pending-counter + latch. Deadlock-free because
    // the composition callbacks never block a pool thread — only getUrls (the
    // I/O) runs on the pool; fan-out just submits more tasks and returns.
    // =========================================================================
    public List<String> crawlConcurrentCF(String startUrl, HtmlParser htmlParser) {
        final String host = hostnameOf(startUrl);
        final Set<String> visited = ConcurrentHashMap.newKeySet();
        final ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        visited.add(startUrl);
        try {
            crawlCF(startUrl, host, htmlParser, visited, pool).join();
        } finally {
            pool.shutdown();
        }
        return new ArrayList<>(visited);
    }

    private CompletableFuture<Void> crawlCF(String url, String host, HtmlParser parser,
                                            Set<String> visited, ExecutorService pool) {
        return CompletableFuture
            .supplyAsync(() -> safeGetUrls(parser, url), pool)   // the blocking I/O runs on the pool
            .thenCompose(urls -> {                               // when fetched, fan out to children
                List<CompletableFuture<Void>> children = new ArrayList<>();
                for (String next : urls) {
                    if (hostnameOf(next).equals(host) && visited.add(next)) {
                        children.add(crawlCF(next, host, parser, visited, pool));
                    }
                }
                // root completes only after ALL descendants complete
                return CompletableFuture.allOf(children.toArray(new CompletableFuture[0]));
            });
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Extract the hostname. URLs look like "http://host/path..." (http, no port).
     * Manual slice avoids URISyntaxException noise and is O(length).
     */
    private static String hostnameOf(String url) {
        int start = url.indexOf("://");
        start = (start < 0) ? 0 : start + 3;     // skip the scheme separator
        int end = url.indexOf('/', start);       // host ends at the first '/' after it
        return (end < 0) ? url.substring(start) : url.substring(start, end);
    }

    /** Defensive fetch: a single broken page must not kill the whole crawl. */
    private static List<String> safeGetUrls(HtmlParser parser, String url) {
        try {
            return parser.getUrls(url);
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    // =========================================================================
    // Self-test harness (not part of the interview answer)
    // =========================================================================
    public static void main(String[] args) {
        boolean ok = true;

        // --- Scenario A: cycles + a shared child + an out-of-scope host (b.com) ---
        Map<String, List<String>> graphA = new HashMap<>();
        graphA.put("http://a.com",   List.of("http://a.com/1", "http://a.com/2", "http://b.com"));
        graphA.put("http://a.com/1", List.of("http://a.com/3", "http://a.com"));        // cycle back
        graphA.put("http://a.com/2", List.of("http://a.com/3", "http://a.com/1"));      // shared child
        graphA.put("http://a.com/3", List.of("http://a.com/2"));                        // cycle
        Set<String> expectedA = Set.of(
            "http://a.com", "http://a.com/1", "http://a.com/2", "http://a.com/3");
        ok &= runScenario("Scenario A (cycles, out-of-scope b.com)",
                          graphA, "http://a.com", expectedA, "http://b.com");

        // --- Scenario B: the prompt's yahoo example (google.com is out of scope) ---
        Map<String, List<String>> graphB = new HashMap<>();
        graphB.put("http://news.yahoo.com",
                   List.of("http://news.yahoo.com/news/topics/", "http://news.yahoo.com/news"));
        graphB.put("http://news.yahoo.com/news/topics/",
                   List.of("http://news.yahoo.com/news", "http://news.yahoo.com/news/sports"));
        graphB.put("http://news.yahoo.com/news", List.of("http://news.google.com"));
        graphB.put("http://news.yahoo.com/news/sports", List.of());
        Set<String> expectedB = Set.of(
            "http://news.yahoo.com",
            "http://news.yahoo.com/news/topics/",
            "http://news.yahoo.com/news",
            "http://news.yahoo.com/news/sports");
        ok &= runScenario("Scenario B (yahoo example, out-of-scope google.com)",
                          graphB, "http://news.yahoo.com", expectedB, "http://news.google.com");

        System.out.println(ok ? "\nALL TESTS PASSED ✅" : "\nTEST FAILED ❌");
        if (!ok) System.exit(1);
    }

    /** Runs all three crawlers on a graph with a FRESH parser each time and verifies them. */
    private static boolean runScenario(String title, Map<String, List<String>> graph,
                                       String start, Set<String> expected, String outOfScope) {
        System.out.println("=== " + title + " ===");
        WebCrawler c = new WebCrawler();
        boolean ok = true;

        // 20ms latency per fetch makes the concurrency real and lets us print a speedup.
        MockHtmlParser p1 = new MockHtmlParser(graph, 20);
        long t1 = System.nanoTime();
        List<String> r1 = c.crawl(start, p1);
        long d1 = (System.nanoTime() - t1) / 1_000_000;
        ok &= check("Part 1 single-threaded", r1, expected, p1, outOfScope, d1);

        MockHtmlParser p2 = new MockHtmlParser(graph, 20);
        long t2 = System.nanoTime();
        List<String> r2 = c.crawlConcurrent(start, p2);
        long d2 = (System.nanoTime() - t2) / 1_000_000;
        ok &= check("Part 2 concurrent (pool)", r2, expected, p2, outOfScope, d2);

        MockHtmlParser p3 = new MockHtmlParser(graph, 20);
        long t3 = System.nanoTime();
        List<String> r3 = c.crawlConcurrentCF(start, p3);
        long d3 = (System.nanoTime() - t3) / 1_000_000;
        ok &= check("Part 2 concurrent (CompletableFuture)", r3, expected, p3, outOfScope, d3);

        System.out.println();
        return ok;
    }

    /** Verifies: correct set, each in-scope URL fetched EXACTLY once, out-of-scope never fetched. */
    private static boolean check(String name, List<String> result, Set<String> expected,
                                 MockHtmlParser parser, String outOfScope, long millis) {
        boolean setOk = new HashSet<>(result).equals(expected) && result.size() == expected.size();

        boolean dedupOk = true;     // each in-scope page fetched exactly once -> no race / no rework
        for (String u : expected) {
            if (parser.calls(u) != 1) { dedupOk = false; break; }
        }
        boolean scopeOk = parser.calls(outOfScope) == 0; // never leave the start hostname

        boolean pass = setOk && dedupOk && scopeOk;
        System.out.printf("  [%s] %-38s  %3d ms  (size=%d, exactly-once=%b, in-scope-only=%b)%n",
                          pass ? "PASS" : "FAIL", name, millis, result.size(), dedupOk, scopeOk);
        return pass;
    }

    /** Read-only-after-construction mock; counts getUrls calls per URL to detect double-fetches. */
    static final class MockHtmlParser implements HtmlParser {
        private final Map<String, List<String>> graph;
        private final long latencyMs;
        private final Map<String, AtomicInteger> callCount = new ConcurrentHashMap<>();

        MockHtmlParser(Map<String, List<String>> graph, long latencyMs) {
            this.graph = graph;
            this.latencyMs = latencyMs;
        }

        @Override
        public List<String> getUrls(String url) {
            callCount.computeIfAbsent(url, k -> new AtomicInteger()).incrementAndGet();
            if (latencyMs > 0) {
                try { Thread.sleep(latencyMs); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            return graph.getOrDefault(url, List.of());
        }

        int calls(String url) {
            AtomicInteger c = callCount.get(url);
            return c == null ? 0 : c.get();
        }
    }
}
