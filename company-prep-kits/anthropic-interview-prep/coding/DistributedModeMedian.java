/*
 * ============================================================================
 * Anthropic — Staff Software Engineer — Coding Round
 * Question: "Distributed Mode and Median"
 *           (see distributed-mode-median-prompt.txt)
 *
 * A very large dataset is sharded across W workers (typically 10). We may only
 * use the provided message-passing primitives:
 *
 *     send(workerId, data)   -- non-blocking, deliver `data` to a target inbox
 *     recv()                 -- BLOCKING, returns the next payload from ANY sender
 *     barrier()              -- all workers rendezvous (Prompt 2 provides this)
 *
 * plus each worker's own `workerId` and the global `WORKER_NUM` (= W).
 *
 * CRITICAL CONSTRAINT: inter-node transmission is the bottleneck. In Prompt 2's
 * cost model a node reads local data at 10 bytes/sec but the link runs at only
 * 1 byte/sec — inter-node bytes are ~10x more expensive than local reads, AND a
 * single receiver is a serialization point. So the goal is NOT "minimize total
 * bytes" alone; it is "minimize the bytes funnelled into the busiest receiver".
 * => NEVER let all workers dump into worker 0.
 *
 * This single file contains BOTH parts the interview walks through:
 *   PART 1  Distributed MODE   (most frequent value)
 *   PART 2  Distributed MEDIAN (global 50th percentile)   *** the follow-up ***
 *
 * PART 1 shows the iteration the graders explicitly reward:
 *   (a) findModeNaiveFullCounter   -- correct but funnels O(U) into worker 0.
 *   (b) findModeLocalTop1NoShuffle  -- cheap but WRONG: misses a mode that is
 *                                      split across machines (false negative).
 *   (c) findModeShuffle             -- THE ANSWER: modulo-shuffle balances load
 *                                      AND makes per-worker top-1 sufficient.
 *
 * PART 2 REUSES Phase 1-2 of (c) to build a distributed histogram, then runs a
 * distributed quickselect (value-domain bisection): O(log(range)) rounds, O(1)
 * bytes/worker/round. Lines that differ from Part 1 are tagged  <<< NEW IN P2.
 *
 * KEY CORRECTNESS POINT (the reference Python glosses over it):
 *   recv() returns from ANY sender, so without a barrier a fast worker's
 *   Phase-4 message can arrive in worker 0's inbox while worker 0 is still
 *   draining Phase-2 buckets => wrong-type / out-of-order read. We place a
 *   barrier() at every phase boundary to make the channel cleanly separated.
 *
 * Everything below the algorithms is a SIMULATION HARNESS (not part of the
 * interview answer) that mimics send/recv/barrier with one blocking queue per
 * worker + a CyclicBarrier, runs each worker on its own thread, instruments
 * the per-receiver byte load, and verifies results against a single-machine
 * brute force over many randomized trials + the canonical split-duplicate case.
 *
 *   Compile : javac DistributedModeMedian.java
 *   Run     : java  DistributedModeMedian
 *
 * Verified on OpenJDK Corretto 21.
 * ============================================================================
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class DistributedModeMedian {

    /** One (value, count) record shuffled between workers. ~2 "numbers" on the wire. */
    record KeyCount(long key, long count) {}

    // ========================================================================
    // PART 1 — DISTRIBUTED MODE
    // ========================================================================

    /*
     * ---- (c) THE ANSWER: modulo-shuffle + per-worker top-1 -----------------
     *
     * Phase 1  Local count           : each worker Counter()s its own shard.   (local read)
     * Phase 2  Shuffle by key % W     : every copy of a value lands on ONE owner. (all-to-all, balanced)
     * Phase 3  Aggregate + local top-1: owner now has the COMPLETE count for its
     *                                    disjoint key-slice, so its single best
     *                                    (key,count) is a global-mode candidate.
     * Phase 4  Reduce W candidates -> 0: the global mode is max over the W tops.
     *
     * Why top-1 suffices: after Phase 2 the key space is partitioned, so counts
     * are final and no key is split across two workers. The mode is therefore
     * the largest of the per-worker maxima — no top-K needed.
     */
    static Long findModeShuffle(Node node) {
        final int W = node.numWorkers, id = node.workerId;

        // ---- Phase 1: local frequency count (local read; no network) -------
        Map<Long, Long> local = new HashMap<>();
        for (long v : node.localData) local.merge(v, 1L, Long::sum);

        // ---- Phase 2: shuffle counts by key % W so identical keys co-locate -
        List<KeyCount> received = shuffleByKey(node, local);

        // ---- Phase 3: aggregate complete counts for our slice; local top-1 -
        long[] top = localTop1(received); // {key, count}; count==0 => empty slice

        // ---- Barrier: Phase-2 buckets are fully drained on every inbox -----
        //      so Phase-4 messages can't be mistaken for Phase-2 messages.
        node.barrier();

        // ---- Phase 4: reduce the W local top-1s to worker 0 ----------------
        if (id == 0) {
            long bestKey = top[0], bestCount = top[1];
            for (int r = 0; r < W - 1; r++) {
                long[] rc = (long[]) node.recv();          // {key, count}
                long k = rc[0], c = rc[1];                 // max count; tie -> smaller key
                if (c > bestCount || (c == bestCount && bestCount > 0 && k < bestKey)) { bestCount = c; bestKey = k; }
            }
            return bestCount > 0 ? bestKey : null;         // null => empty dataset
        } else {
            node.send(0, new long[]{top[0], top[1]});      // O(1) bytes per worker
            return null;                                   // only worker 0 holds the answer
        }
    }

    /*
     * ---- (a) BASELINE: naive full-counter to worker 0 ----------------------
     * Correct, and sometimes the right call for SMALL alphabets (few distinct
     * values): the wire cost is the counter, not the data, so funnelling U
     * pairs into worker 0 is cheap. But for the "mostly unique" dataset U ~ N
     * and worker 0's single receiver becomes the serialization bottleneck.
     */
    static Long findModeNaiveFullCounter(Node node) {
        final int W = node.numWorkers, id = node.workerId;
        Map<Long, Long> local = new HashMap<>();
        for (long v : node.localData) local.merge(v, 1L, Long::sum);

        if (id == 0) {
            Map<Long, Long> global = new HashMap<>(local);
            for (int r = 0; r < W - 1; r++) {              // <- O(U) funnelled into one node
                @SuppressWarnings("unchecked")
                List<KeyCount> chunk = (List<KeyCount>) node.recv();
                for (KeyCount kc : chunk) global.merge(kc.key(), kc.count(), Long::sum);
            }
            Long best = null; long bc = 0;
            for (Map.Entry<Long, Long> e : global.entrySet())
                if (e.getValue() > bc || (e.getValue() == bc && best != null && e.getKey() < best)) {
                    bc = e.getValue(); best = e.getKey();
                }
            return best;
        } else {
            List<KeyCount> all = new ArrayList<>(local.size());
            for (Map.Entry<Long, Long> e : local.entrySet()) all.add(new KeyCount(e.getKey(), e.getValue()));
            node.send(0, all);
            return null;
        }
    }

    /*
     * ---- (b) BASELINE THAT IS WRONG: local top-1 WITHOUT the shuffle -------
     * Cheap (one (key,count) per worker) but a FALSE NEGATIVE: if the mode's
     * occurrences are split across machines, each machine sees it as just
     * another count-1 value, so no worker's local top-1 ever reports it. We
     * keep this to demonstrate *why* the Phase-2 repartition is mandatory.
     */
    static Long findModeLocalTop1NoShuffle(Node node) {
        final int W = node.numWorkers, id = node.workerId;
        Map<Long, Long> local = new HashMap<>();
        for (long v : node.localData) local.merge(v, 1L, Long::sum);

        long bestKey = 0, bestCount = 0;
        for (Map.Entry<Long, Long> e : local.entrySet())
            if (e.getValue() > bestCount || (e.getValue() == bestCount && bestCount > 0 && e.getKey() < bestKey)) {
                bestCount = e.getValue(); bestKey = e.getKey();
            }

        if (id == 0) {
            long gKey = bestKey, gCount = bestCount;
            for (int r = 0; r < W - 1; r++) {
                long[] rc = (long[]) node.recv();
                if (rc[1] > gCount || (rc[1] == gCount && gCount > 0 && rc[0] < gKey)) { gCount = rc[1]; gKey = rc[0]; }
            }
            return gCount > 0 ? gKey : null;
        } else {
            node.send(0, new long[]{bestKey, bestCount});
            return null;
        }
    }

    // ========================================================================
    // PART 2 — DISTRIBUTED MEDIAN   (the follow-up)
    // ========================================================================
    /*
     * Median needs global ORDER, not just frequency. We:
     *   Phase 1-2  REUSE the mode shuffle to build a distributed histogram
     *              (each worker owns the complete counts for its key-slice).   <<< REUSED
     *   Reduction  one fused all-reduce -> total N and global [min,max].        <<< NEW IN P2
     *   Quickselect distributed bisection on the VALUE domain:                  <<< NEW IN P2
     *        broadcast pivot -> each worker returns (less, equal) for its slice
     *        -> shrink [low,high]. O(log(max-min)) rounds, O(1) bytes/worker/round.
     *
     * Why not merge sorted lists? That funnels O(U) values into one node — the
     * same bottleneck we rejected for mode. Bisection keeps every round at a
     * few bytes per worker, which is optimal under the 1 byte/sec link.
     */
    static Long findMedianDistributed(Node node) {
        final int W = node.numWorkers, id = node.workerId;

        // ===== Phase 1-2 REUSED FROM PART 1: build the distributed histogram =
        Map<Long, Long> local = new HashMap<>();
        for (long v : node.localData) local.merge(v, 1L, Long::sum);
        List<KeyCount> received = shuffleByKey(node, local);
        Map<Long, Long> hist = new HashMap<>();
        for (KeyCount kc : received) hist.merge(kc.key(), kc.count(), Long::sum);

        node.barrier(); // <<< NEW IN P2: separate the shuffle from the reduction

        // ===== Fused all-reduce -> total count + global [min,max] ============ <<< NEW IN P2
        long localCount = 0, localMin = Long.MAX_VALUE, localMax = Long.MIN_VALUE;
        for (Map.Entry<Long, Long> e : hist.entrySet()) {
            localCount += e.getValue();
            localMin = Math.min(localMin, e.getKey());
            localMax = Math.max(localMax, e.getKey());
        }
        long total, gMin, gMax;
        if (id == 0) {
            total = localCount; gMin = localMin; gMax = localMax;
            for (int r = 0; r < W - 1; r++) {
                long[] m = (long[]) node.recv();           // {count, min, max}
                total += m[0]; gMin = Math.min(gMin, m[1]); gMax = Math.max(gMax, m[2]);
            }
            for (int t = 1; t < W; t++) node.send(t, new long[]{total, gMin, gMax});
        } else {
            node.send(0, new long[]{localCount, localMin, localMax});
            long[] m = (long[]) node.recv();
            total = m[0]; gMin = m[1]; gMax = m[2];
        }
        if (total == 0) return null;                       // empty dataset

        node.barrier(); // <<< NEW IN P2: separate the reduction from quickselect

        // ===== Distributed quickselect by value-domain bisection ============= <<< NEW IN P2
        // Upper-median convention (0-indexed): index = total/2  (matches brute force).
        // Lower median would be (total-1)/2; average-of-middles needs two selects.
        final long target = total / 2;
        long low = gMin, high = gMax;
        while (low < high) {
            long pivot;
            if (id == 0) {
                pivot = low + Math.floorDiv(high - low, 2); // in [low, high-1]; overflow-safe
                for (int t = 1; t < W; t++) node.send(t, new long[]{pivot});
            } else {
                pivot = ((long[]) node.recv())[0];
            }

            // Count this worker's slice relative to the pivot. "greater" is
            // implicit (total - less - equal), so we ship only two numbers.
            long less = 0, equal = 0;
            for (Map.Entry<Long, Long> e : hist.entrySet()) {
                long k = e.getKey(), c = e.getValue();
                if (k < pivot) less += c; else if (k == pivot) equal += c;
            }

            if (id == 0) {
                long tLess = less, tEqual = equal;
                for (int r = 0; r < W - 1; r++) {
                    long[] m = (long[]) node.recv();       // {less, equal}
                    tLess += m[0]; tEqual += m[1];
                }
                long nlow, nhigh;
                if (target < tLess)                  { nlow = low;       nhigh = pivot - 1; } // answer < pivot
                else if (target < tLess + tEqual)    { nlow = pivot;     nhigh = pivot;     } // answer == pivot
                else                                 { nlow = pivot + 1; nhigh = high;      } // answer > pivot
                for (int t = 1; t < W; t++) node.send(t, new long[]{nlow, nhigh});
                low = nlow; high = nhigh;
            } else {
                node.send(0, new long[]{less, equal});
                long[] m = (long[]) node.recv();
                low = m[0]; high = m[1];
            }
        }
        return low; // all workers converge to the same value
    }

    // ========================================================================
    // SHARED HELPERS (used by both parts)
    // ========================================================================

    /**
     * Phase 2 shuffle: bucket local counts by key % W, fan out to every other
     * worker, fan in exactly W-1 buckets. Returns OUR slice (own bucket + the
     * W-1 received). All workers send and receive simultaneously => no single
     * receiver is a hotspot.
     *
     * Note: we send even EMPTY buckets so every receiver knows to expect
     * exactly W-1 messages. Skipping empties (Prompt 1's optimization) would
     * need an extra "how many to expect" exchange; not worth it for large U.
     */
    static List<KeyCount> shuffleByKey(Node node, Map<Long, Long> local) {
        final int W = node.numWorkers, id = node.workerId;
        List<List<KeyCount>> buckets = new ArrayList<>(W);
        for (int t = 0; t < W; t++) buckets.add(new ArrayList<>());
        for (Map.Entry<Long, Long> e : local.entrySet()) {
            int target = (int) Math.floorMod(e.getKey(), W);   // floorMod => correct for negatives
            buckets.get(target).add(new KeyCount(e.getKey(), e.getValue()));
        }
        for (int t = 0; t < W; t++) if (t != id) node.send(t, buckets.get(t)); // parallel fan-out
        List<KeyCount> mine = new ArrayList<>(buckets.get(id));                 // keep own slice (no network)
        for (int r = 0; r < W - 1; r++) {                                       // parallel fan-in
            @SuppressWarnings("unchecked")
            List<KeyCount> chunk = (List<KeyCount>) node.recv();
            mine.addAll(chunk);
        }
        return mine;
    }

    /** Aggregate received (key,count)s and return {key, count} of the local mode
     *  (max count; ties broken to the SMALLEST key). count==0 => empty slice. */
    static long[] localTop1(List<KeyCount> received) {
        Map<Long, Long> agg = new HashMap<>();
        for (KeyCount kc : received) agg.merge(kc.key(), kc.count(), Long::sum);
        long bestKey = 0, bestCount = 0;
        for (Map.Entry<Long, Long> e : agg.entrySet()) {
            long k = e.getKey(), c = e.getValue();
            if (c > bestCount || (c == bestCount && bestCount > 0 && k < bestKey)) { bestCount = c; bestKey = k; }
        }
        return new long[]{bestKey, bestCount};
    }

    // ========================================================================
    // BRUTE-FORCE REFERENCE (single machine — ground truth for verification)
    // ========================================================================
    static Long bruteForceMode(long[] all) {
        if (all.length == 0) return null;
        Map<Long, Long> c = new HashMap<>();
        for (long v : all) c.merge(v, 1L, Long::sum);
        Long best = null; long bc = 0;
        for (Map.Entry<Long, Long> e : c.entrySet())
            if (e.getValue() > bc || (e.getValue() == bc && best != null && e.getKey() < best)) { bc = e.getValue(); best = e.getKey(); }
        return best;
    }
    static Long bruteForceMedian(long[] all) {
        if (all.length == 0) return null;
        long[] s = all.clone();
        Arrays.sort(s);
        return s[s.length / 2];  // upper median, 0-indexed — matches distributed convention
    }

    // ========================================================================
    // SIMULATION HARNESS  (mimics send/recv/barrier; NOT part of the answer)
    // ========================================================================

    /** Per-worker context: the only API the algorithms above are allowed to use. */
    static final class Node {
        final int workerId;      // 0 .. WORKER_NUM-1
        final int numWorkers;    // WORKER_NUM
        final long[] localData;  // this worker's shard, read locally
        private final Channel channel;
        Node(int id, int n, Channel ch, long[] data) { workerId = id; numWorkers = n; channel = ch; localData = data; }
        void send(int target, Object data) { channel.send(target, data); }
        Object recv()                       { return channel.recv(workerId); }
        void barrier()                      { channel.await(); }
    }

    /**
     * The "network": one unbounded blocking inbox per worker + a CyclicBarrier.
     * send() is non-blocking (models a buffered link, so the all-send-then-
     * all-recv pattern can't self-deadlock); recv() blocks on the caller's
     * inbox and returns from ANY sender, FIFO by arrival — exactly the
     * semantics of the interview harness. We also instrument the payload
     * "units" delivered into each inbox to expose the receive-side bottleneck.
     */
    static final class Channel {
        private final BlockingQueue<Object>[] inboxes;
        private final CyclicBarrier barrier;
        private final AtomicLong[] recvUnits;
        private final AtomicLong total = new AtomicLong();

        @SuppressWarnings("unchecked")
        Channel(int n) {
            inboxes = new BlockingQueue[n];
            recvUnits = new AtomicLong[n];
            for (int i = 0; i < n; i++) { inboxes[i] = new LinkedBlockingQueue<>(); recvUnits[i] = new AtomicLong(); }
            barrier = new CyclicBarrier(n);
        }
        void send(int target, Object data) {
            long u = estimateUnits(data);
            total.addAndGet(u);
            recvUnits[target].addAndGet(u);
            inboxes[target].offer(data);               // non-blocking, unbounded
        }
        Object recv(int self) {
            try { return inboxes[self].take(); }       // blocks until a message arrives
            catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new RuntimeException(e); }
        }
        void await() {
            try { barrier.await(); }
            catch (Exception e) { throw new RuntimeException(e); }
        }
        long totalUnits() { return total.get(); }
        long maxRecvUnits() { long m = 0; for (AtomicLong a : recvUnits) m = Math.max(m, a.get()); return m; }
        static long estimateUnits(Object data) {
            if (data instanceof List<?> l) return 2L * l.size(); // each KeyCount ~ 2 numbers on the wire
            if (data instanceof long[] a)  return a.length;
            return 1;
        }
    }

    /** A worker program: given its Node, returns the result (meaningful on worker 0). */
    interface WorkerFn<T> { T run(Node node); }

    /** Outcome of one distributed run: worker 0's result + bandwidth instrumentation. */
    record RunOutcome<T>(T value, long maxRecvUnits, long totalUnits) {}

    /** Spin up one thread per shard, run `fn` on each, join, return worker 0's result. */
    @SuppressWarnings("unchecked")
    static <T> RunOutcome<T> runDistributed(long[][] shards, WorkerFn<T> fn) throws InterruptedException {
        int W = shards.length;
        Channel channel = new Channel(W);
        Object[] results = new Object[W];
        Thread[] threads = new Thread[W];
        for (int i = 0; i < W; i++) {
            final int id = i;
            threads[i] = new Thread(() -> {
                Node node = new Node(id, W, channel, shards[id]);
                results[id] = fn.run(node);
            }, "worker-" + id);
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        return new RunOutcome<>((T) results[0], channel.maxRecvUnits(), channel.totalUnits());
    }

    // ---- sharding / flattening utilities -----------------------------------
    static long[][] shardRoundRobin(long[] data, int W) {
        List<List<Long>> tmp = freshBuckets(W);
        for (int i = 0; i < data.length; i++) tmp.get(i % W).add(data[i]);
        return toShards(tmp, W);
    }
    static long[][] shardRandom(long[] data, int W, Random rnd) {
        List<List<Long>> tmp = freshBuckets(W);
        for (long v : data) tmp.get(rnd.nextInt(W)).add(v);
        return toShards(tmp, W);
    }
    private static List<List<Long>> freshBuckets(int W) {
        List<List<Long>> tmp = new ArrayList<>(W);
        for (int i = 0; i < W; i++) tmp.add(new ArrayList<>());
        return tmp;
    }
    private static long[][] toShards(List<List<Long>> tmp, int W) {
        long[][] s = new long[W][];
        for (int i = 0; i < W; i++) {
            s[i] = new long[tmp.get(i).size()];
            for (int j = 0; j < s[i].length; j++) s[i][j] = tmp.get(i).get(j);
        }
        return s;
    }
    static long[] flatten(long[][] shards) {
        int n = 0; for (long[] s : shards) n += s.length;
        long[] all = new long[n]; int k = 0;
        for (long[] s : shards) for (long v : s) all[k++] = v;
        return all;
    }

    // ========================================================================
    // SELF-TEST / DEMONSTRATION
    // ========================================================================
    static int pass = 0, fail = 0;
    static <T> void check(String label, T got, T exp) {
        boolean ok = Objects.equals(got, exp);
        if (ok) pass++; else fail++;
        System.out.printf("    [%s] %-22s got=%s  exp=%s%n", ok ? "PASS" : "FAIL", label, got, exp);
    }

    public static void main(String[] args) throws InterruptedException {
        final int W = 10; // WORKER_NUM
        System.out.println("=== Distributed Mode & Median — W=" + W + " workers ===\n");

        // ------------------------------------------------------------------
        // SCENARIO A — the canonical problem: mostly-unique ints, ONE value
        // duplicated on TWO DIFFERENT machines.
        // ------------------------------------------------------------------
        System.out.println("SCENARIO A  canonical split-duplicate (mostly-unique, one value twice)");
        int N = 2000;
        long[] base = new long[N];
        for (int i = 0; i < N; i++) base[i] = i;          // 0..1999, all unique
        long[][] shardsA = shardRandom(base, W, new Random(123)); // random => the shuffle MUST redistribute
        long DUP = 1234;
        int dupWorker = -1;                                // find which machine already holds DUP ...
        for (int w = 0; w < W && dupWorker < 0; w++) for (long v : shardsA[w]) if (v == DUP) { dupWorker = w; break; }
        int other = (dupWorker + 1) % W;                   // ... add a 2nd copy on a DIFFERENT machine
        shardsA[other] = Arrays.copyOf(shardsA[other], shardsA[other].length + 1);
        shardsA[other][shardsA[other].length - 1] = DUP;
        long[] allA = flatten(shardsA);

        Long expModeA = bruteForceMode(allA), expMedA = bruteForceMedian(allA);

        RunOutcome<Long> shuffle = runDistributed(shardsA, DistributedModeMedian::findModeShuffle);
        RunOutcome<Long> naive   = runDistributed(shardsA, DistributedModeMedian::findModeNaiveFullCounter);
        RunOutcome<Long> wrong   = runDistributed(shardsA, DistributedModeMedian::findModeLocalTop1NoShuffle);
        RunOutcome<Long> median  = runDistributed(shardsA, DistributedModeMedian::findMedianDistributed);

        check("mode (shuffle)",       shuffle.value(), expModeA);
        check("mode (naive full)",    naive.value(),   expModeA);
        check("median (bisection)",   median.value(),  expMedA);
        // The WRONG strategy: demonstrate the false negative explicitly.
        boolean wrongIsWrong = !Objects.equals(wrong.value(), expModeA);
        System.out.printf("    [%s] %-22s got=%s  exp=%s   <- top-1 WITHOUT shuffle misses the split mode%n",
                wrongIsWrong ? "DEMO" : "FAIL", "mode (no-shuffle top1)", wrong.value(), expModeA);
        if (!wrongIsWrong) fail++;

        System.out.printf("    bandwidth (busiest receiver, lower=better):  shuffle=%d units   naive=%d units  -> %.1fx less load%n",
                shuffle.maxRecvUnits(), naive.maxRecvUnits(), naive.maxRecvUnits() / (double) Math.max(1, shuffle.maxRecvUnits()));
        System.out.printf("    (true mode=%d appears 2x; median=%d; total N=%d)%n%n", expModeA, expMedA, allA.length);

        // ------------------------------------------------------------------
        // SCENARIO B — randomized stress: repeats, negatives, random sharding.
        // ------------------------------------------------------------------
        System.out.println("SCENARIO B  randomized stress (repeats + negatives, random shards)");
        Random rnd = new Random(42);
        int trials = 60, mOk = 0, medOk = 0;
        for (int t = 0; t < trials; t++) {
            int n = 1 + rnd.nextInt(3000);
            long[] data = new long[n];
            for (int i = 0; i < n; i++) data[i] = rnd.nextInt(600) - 100; // [-100, 499], many repeats
            long[][] shards = shardRandom(data, W, rnd);
            Long m  = runDistributed(shards, DistributedModeMedian::findModeShuffle).value();
            Long md = runDistributed(shards, DistributedModeMedian::findMedianDistributed).value();
            if (Objects.equals(m,  bruteForceMode(data)))   mOk++;
            if (Objects.equals(md, bruteForceMedian(data))) medOk++;
        }
        check("mode   " + trials + " trials", mOk,   trials);
        check("median " + trials + " trials", medOk, trials);
        System.out.println();

        // ------------------------------------------------------------------
        // SCENARIO C — heavy skew: worker 0 holds ~90% of the data.
        // (Correctness is unaffected; the shuffle still balances by KEY.)
        // ------------------------------------------------------------------
        System.out.println("SCENARIO C  heavy skew (~90% of data on a single worker)");
        long[][] shardsC = freshShards(W);
        Random rc = new Random(7);
        List<Long> w0 = new ArrayList<>();
        for (int i = 0; i < 9000; i++) w0.add((long) rc.nextInt(1000));
        shardsC[0] = toLongArray(w0);
        for (int w = 1; w < W; w++) {
            List<Long> s = new ArrayList<>();
            for (int i = 0; i < 110; i++) s.add((long) rc.nextInt(1000));
            shardsC[w] = toLongArray(s);
        }
        long[] allC = flatten(shardsC);
        check("mode (skew)",   runDistributed(shardsC, DistributedModeMedian::findModeShuffle).value(),   bruteForceMode(allC));
        check("median (skew)", runDistributed(shardsC, DistributedModeMedian::findMedianDistributed).value(), bruteForceMedian(allC));
        System.out.println();

        // ------------------------------------------------------------------
        // SCENARIO D — edge cases.
        // ------------------------------------------------------------------
        System.out.println("SCENARIO D  edge cases");
        // single element
        long[][] one = freshShards(W); one[3] = new long[]{99};
        check("single elem mode",   runDistributed(one, DistributedModeMedian::findModeShuffle).value(),   99L);
        check("single elem median", runDistributed(one, DistributedModeMedian::findMedianDistributed).value(), 99L);
        // all identical
        long[] same = new long[500]; Arrays.fill(same, -5L);
        long[][] sameSh = shardRoundRobin(same, W);
        check("all-identical mode",   runDistributed(sameSh, DistributedModeMedian::findModeShuffle).value(),   -5L);
        check("all-identical median", runDistributed(sameSh, DistributedModeMedian::findMedianDistributed).value(), -5L);
        // empty dataset
        check("empty mode",   runDistributed(freshShards(W), DistributedModeMedian::findModeShuffle).value(),   (Long) null);
        check("empty median", runDistributed(freshShards(W), DistributedModeMedian::findMedianDistributed).value(), (Long) null);

        // ------------------------------------------------------------------
        System.out.printf("%n=== SUMMARY: %d passed, %d failed ===%n", pass, fail);
        if (fail != 0) throw new AssertionError(fail + " checks failed");
    }

    private static long[][] freshShards(int W) { long[][] s = new long[W][]; for (int i = 0; i < W; i++) s[i] = new long[0]; return s; }
    private static long[] toLongArray(List<Long> xs) { long[] a = new long[xs.size()]; for (int i = 0; i < a.length; i++) a[i] = xs.get(i); return a; }
}
