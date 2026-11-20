/*
 * Anthropic — Staff SWE coding interview practice
 * Problem: "LRU Cache" (bug-fix in key generation, then make it durable)
 *
 * IMPORTANT CONTEXT: The original problem is PYTHON-SPECIFIC. The Part-1 bug is
 * about hashing Python's *args / **kwargs (unhashable dicts/lists, and kwarg
 * insertion-order). Java has no **kwargs and Java's List/Map already have
 * value-based equals/hashCode, so the bug does not exist verbatim. This file is
 * the faithful JAVA PORT (great for drilling the LRU + write-ahead-log
 * structure). The real interview answer is in lru_cache.py in this folder.
 *
 * The Java analogue of the bug: using the raw Object[] varargs array (or a Map)
 * directly as the cache key. Arrays use IDENTITY equals/hashCode, so two equal
 * argument lists yield different keys => the cache NEVER hits. The fix is to
 * build a canonical, value-based, order-stable key.
 *
 * Compile & run:  javac LRUCache.java && java LRUCache
 */

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;

public class LRUCache {

    // =================================================================
    // PART 1 — bug-fixed LRU (O(1) get/put via HashMap + doubly linked list)
    // =================================================================
    static class LRU {
        /** Doubly-linked-list node: lets us evict / promote in O(1). */
        protected static final class Node {
            final Object key;
            Object value;
            Node prev, next;
            Node(Object key, Object value) { this.key = key; this.value = value; }
        }

        protected final int capacity;
        protected final Map<Object, Node> map = new HashMap<>();
        protected final Node head = new Node(null, null); // sentinel: head.next == LRU
        protected final Node tail = new Node(null, null); // sentinel: tail.prev == MRU

        LRU(int capacity) {
            this.capacity = capacity;
            head.next = tail;
            tail.prev = head;
        }

        /* ----------------------------- THE FIX -----------------------------
         * Buggy version returned the raw Object[] (identity equals/hashCode) =>
         * cache never hits. We instead build a CANONICAL key whose equals/
         * hashCode are value-based and insertion-order-independent:
         *   - arrays  -> List          (value semantics, position preserved)
         *   - Set     -> HashSet        (order-independent equality)
         *   - Map     -> TreeMap        (sorted: f(a=1,b=2) == f(b=2,a=1))
         *   - recurse into nested containers
         * funcName makes keys unique per wrapped callable (like __qualname__).
         */
        Object generateKey(String funcName, Object[] args, Map<String, Object> kwargs) {
            return Arrays.asList(funcName, normalize(args), normalize(kwargs));
        }

        @SuppressWarnings("unchecked")
        protected static Object normalize(Object o) {
            if (o == null) return null;
            if (o instanceof Object[]) {                 // varargs array -> list
                List<Object> out = new ArrayList<>();
                for (Object x : (Object[]) o) out.add(normalize(x));
                return out;
            }
            if (o instanceof Set) {                       // set -> order-independent set
                Set<Object> out = new HashSet<>();
                for (Object x : (Set<Object>) o) out.add(normalize(x));
                return out;
            }
            if (o instanceof Collection) {                // list -> list (order matters)
                List<Object> out = new ArrayList<>();
                for (Object x : (Collection<Object>) o) out.add(normalize(x));
                return out;
            }
            if (o instanceof Map) {                       // map/kwargs -> sorted map
                TreeMap<String, Object> out = new TreeMap<>();
                for (Map.Entry<?, ?> e : ((Map<?, ?>) o).entrySet())
                    out.put(String.valueOf(e.getKey()), normalize(e.getValue()));
                return out;
            }
            return o;                                     // Integer/String/... already value-based
        }

        /** Look up the memoized result; compute + cache on a miss; evict LRU. */
        Object call(String funcName, BiFunction<Object[], Map<String, Object>, Object> func,
                    Object[] args, Map<String, Object> kwargs) {
            Object key = generateKey(funcName, args, kwargs);
            Node n = map.get(key);
            if (n != null) {                  // HIT
                moveToTail(n);                // mark most-recently-used
                return n.value;
            }
            Object result = func.apply(args, kwargs);  // MISS -> compute
            insert(key, result);
            return result;
        }

        // ---- shared mutators (also reused by the durable subclass) ----
        protected void insert(Object key, Object value) {
            Node n = new Node(key, value);
            map.put(key, n);
            addToTail(n);
            if (map.size() > capacity) {       // evict least-recently-used
                Node lru = head.next;
                remove(lru);
                map.remove(lru.key);
            }
        }
        protected void touch(Object key) {     // promote an existing key to MRU
            Node n = map.get(key);
            if (n != null) moveToTail(n);
        }

        private void addToTail(Node n) { n.prev = tail.prev; n.next = tail; tail.prev.next = n; tail.prev = n; }
        private void remove(Node n)    { n.prev.next = n.next; n.next.prev = n.prev; }
        private void moveToTail(Node n){ remove(n); addToTail(n); }

        /** Keys in LRU -> MRU order (used for snapshotting & tests). */
        List<Object> keysLruToMru() {
            List<Object> out = new ArrayList<>();
            for (Node n = head.next; n != tail; n = n.next) out.add(n.key);
            return out;
        }
    }

    // =================================================================
    // PART 2 — durable LRU: survives a crash via a Write-Ahead Log (WAL)
    // =================================================================
    // CHANGES FROM PART 1 (all marked with // [P2]):
    //   * constructor replays the WAL to rebuild contents AND LRU order
    //   * call() appends a record (fsync'd) BEFORE returning  -> durability
    //       - miss  : append a 's'(et) record {key, value}
    //       - hit   : append a 't'(ouch) record {key}  (value already on disk -> save I/O)
    //   * recover() tolerates a torn final line and truncates it
    //   * compact() periodically rewrites a snapshot so the log stays bounded
    //   * call() is synchronized (single-writer) for thread + multi-call safety
    // Eviction is NOT logged: it is recomputed deterministically on replay from
    // the same 's' sequence + capacity bound, so the recovered state matches
    // exactly what it would have been without the crash.
    static class DurableLRU extends LRU {

        /** One WAL record. Serializable because keys are Lists/Maps/scalars. */
        static final class Entry implements Serializable {
            private static final long serialVersionUID = 1L;
            final char op;          // 's' = set, 't' = touch
            final Object key;
            final Object value;     // null for touch
            Entry(char op, Object key, Object value) { this.op = op; this.key = key; this.value = value; }
        }

        private final String path;
        private final boolean fsync;
        private final int compactEvery;
        private int sinceCompact = 0;
        private FileOutputStream fos;
        private FileChannel channel;

        DurableLRU(int capacity, String path, boolean fsync, int compactEvery) throws IOException {
            super(capacity);
            this.path = path;
            this.fsync = fsync;
            this.compactEvery = compactEvery;
            recover();          // [P2] rebuild in-memory state from disk
            openLog();          // [P2] open append handle for new writes
        }

        private void openLog() throws IOException {
            fos = new FileOutputStream(path, true);   // append mode
            channel = fos.getChannel();
        }

        @Override
        synchronized Object call(String funcName, BiFunction<Object[], Map<String, Object>, Object> func,
                                 Object[] args, Map<String, Object> kwargs) {
            Object key = generateKey(funcName, args, kwargs);
            Node n = map.get(key);
            if (n != null) {                                 // HIT
                touch(key);
                writeRecord(new Entry('t', key, null));      // [P2] log recency (cheap, value-less)
                return n.value;
            }
            Object result = func.apply(args, kwargs);         // MISS -> compute
            writeRecord(new Entry('s', key, result));         // [P2] WAL BEFORE returning -> durable
            insert(key, result);                              // eviction here is implicit on replay
            return result;
        }

        /** Append one record, flush, and fsync (the durability point). */
        private void writeRecord(Entry e) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try (ObjectOutputStream oos = new ObjectOutputStream(bos)) { oos.writeObject(e); }
                // one base64 line per record => the truncated-tail check is trivial
                String line = Base64.getEncoder().encodeToString(bos.toByteArray()) + "\n";
                fos.write(line.getBytes(StandardCharsets.US_ASCII));
                fos.flush();                          // push out of JVM buffer to OS
                if (fsync) channel.force(true);       // force OS cache -> physical disk
                if (++sinceCompact >= compactEvery) compact();
            } catch (IOException ex) {
                throw new RuntimeException("WAL write failed", ex);
            }
        }

        /** Replay the log on startup; tolerate (and truncate) a torn final line. */
        private void recover() throws IOException {
            File f = new File(path);
            if (!f.exists()) return;
            byte[] all = java.nio.file.Files.readAllBytes(f.toPath());
            long goodLen = 0;
            int start = 0;
            for (int i = 0; i < all.length; i++) {
                if (all[i] != '\n') continue;
                try {
                    byte[] seg = Arrays.copyOfRange(all, start, i);
                    byte[] raw = Base64.getDecoder().decode(seg);
                    Entry e;
                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(raw))) {
                        e = (Entry) ois.readObject();
                    }
                    if (e.op == 's') {
                        insert(e.key, e.value);    // same insert+evict as live => exact reconstruction
                    } else if (e.op == 't') {
                        touch(e.key);              // CRUCIAL: re-apply recency or LRU degrades to FIFO
                    }
                } catch (Exception ex) {
                    break;                          // partial/corrupt tail from a crash mid-write
                }
                goodLen = i + 1;
                start = i + 1;
            }
            if (goodLen < all.length) {            // drop torn tail so future appends stay contiguous
                try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) { raf.setLength(goodLen); }
            }
        }

        /** Snapshot current cache (LRU->MRU) to a temp file and atomically swap. */
        private void compact() throws IOException {
            String tmp = path + ".tmp";
            try (FileOutputStream t = new FileOutputStream(tmp, false)) {
                for (Node node = head.next; node != tail; node = node.next) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                        oos.writeObject(new Entry('s', node.key, node.value));
                    }
                    t.write((Base64.getEncoder().encodeToString(bos.toByteArray()) + "\n")
                            .getBytes(StandardCharsets.US_ASCII));
                }
                t.flush();
                t.getChannel().force(true);
            }
            channel.close();
            fos.close();
            java.nio.file.Files.move(new File(tmp).toPath(), new File(path).toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                    java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            openLog();
            sinceCompact = 0;
        }

        void close() throws IOException { channel.close(); fos.close(); }
    }

    // =================================================================
    // Self-verifying demo (mirrors the Python test suite)
    // =================================================================
    static void require(boolean cond, String msg) { if (!cond) throw new AssertionError(msg); }

    static List<List<?>> argsList(LRU c) {           // extract the positional-args view of each key
        List<List<?>> out = new ArrayList<>();
        for (Object key : c.keysLruToMru()) out.add((List<?>) ((List<?>) key).get(1));
        return out;
    }

    public static void main(String[] args) throws Exception {
        BiFunction<Object[], Map<String, Object>, Object> square =
                (a, k) -> { int x = (Integer) a[0]; return x * x; };

        // ---- Part 1: key stability + eviction ----
        LRU c = new LRU(2);
        Map<String, Object> m1 = new LinkedHashMap<>(); m1.put("a", 1); m1.put("b", 2);
        Map<String, Object> m2 = new LinkedHashMap<>(); m2.put("b", 2); m2.put("a", 1);
        require(c.generateKey("f", new Object[]{1, 2}, Map.of())
                 .equals(c.generateKey("f", new Object[]{1, 2}, Map.of())), "same args -> same key");
        require(c.generateKey("f", new Object[]{}, m1)
                 .equals(c.generateKey("f", new Object[]{}, m2)), "kwarg order must not matter");
        require(!c.generateKey("f", new Object[]{1, 2}, Map.of())
                  .equals(c.generateKey("f", new Object[]{2, 1}, Map.of())), "positional order matters");
        Map<String, Object> n1 = new LinkedHashMap<>(); n1.put("p", List.of(1, 2)); n1.put("q", 3);
        Map<String, Object> n2 = new LinkedHashMap<>(); n2.put("q", 3); n2.put("p", List.of(1, 2));
        require(c.generateKey("f", new Object[]{}, Map.of("d", n1))
                 .equals(c.generateKey("f", new Object[]{}, Map.of("d", n2))), "nested order must not matter");
        LRU e = new LRU(2);
        e.call("sq", square, new Object[]{1}, Map.of());
        e.call("sq", square, new Object[]{2}, Map.of());
        e.call("sq", square, new Object[]{1}, Map.of());   // touch 1 -> 2 is now LRU
        e.call("sq", square, new Object[]{3}, Map.of());   // evict 2
        require(argsList(e).equals(List.of(List.of(1), List.of(3))), "LRU eviction order");
        System.out.println("PART 1 OK");

        // ---- Part 2: durability (exact contents + order after a crash) ----
        String p = newTempPath("wal");
        DurableLRU live = new DurableLRU(3, p, true, 10_000);
        for (int x : new int[]{1, 2, 3, 4, 1}) live.call("sq", square, new Object[]{x}, Map.of());
        List<List<?>> snap = argsList(live);
        live.close();
        DurableLRU re = new DurableLRU(3, p, true, 10_000);   // "restart"
        require(argsList(re).equals(snap), "durable order " + argsList(re) + " vs " + snap);
        re.close();
        System.out.println("PART 2 durability OK; order=" + snap);

        // ---- Part 2: recency from HITS survives restart (else FIFO bug) ----
        String p2 = newTempPath("wal2");
        DurableLRU L = new DurableLRU(2, p2, true, 10_000);
        L.call("sq", square, new Object[]{1}, Map.of());
        L.call("sq", square, new Object[]{2}, Map.of());
        L.call("sq", square, new Object[]{1}, Map.of());      // touch 1
        L.close();
        DurableLRU R = new DurableLRU(2, p2, true, 10_000);
        R.call("sq", square, new Object[]{3}, Map.of());      // must evict 2, keep 1
        List<List<?>> a2 = argsList(R);
        require(a2.contains(List.of(1)) && a2.contains(List.of(3)) && !a2.contains(List.of(2)),
                "recency must survive restart");
        R.close();
        System.out.println("PART 2 recency-survives-restart OK");

        // ---- Part 2: tolerate a torn tail, then keep going cleanly ----
        String p3 = newTempPath("wal3");
        DurableLRU d = new DurableLRU(5, p3, true, 10_000);
        d.call("sq", square, new Object[]{7}, Map.of());
        d.call("sq", square, new Object[]{8}, Map.of());
        d.close();
        try (FileOutputStream fo = new FileOutputStream(p3, true)) { fo.write("garbage-partial-tail".getBytes()); }
        DurableLRU d2 = new DurableLRU(5, p3, true, 10_000);
        require(argsList(d2).size() == 2, "torn tail tolerated");
        d2.call("sq", square, new Object[]{9}, Map.of());
        d2.close();
        DurableLRU d3 = new DurableLRU(5, p3, true, 10_000);   // second restart must be clean
        require(argsList(d3).size() == 3, "clean after torn-tail truncation");
        d3.close();
        System.out.println("PART 2 torn-tail tolerated + truncated OK");

        // ---- Part 2: compaction mid-stream + recover ----
        String p4 = newTempPath("wal4");
        DurableLRU cm = new DurableLRU(3, p4, true, 5);        // compact every 5 writes
        for (int x : new int[]{1, 2, 3, 1, 2, 3, 4, 1}) cm.call("sq", square, new Object[]{x}, Map.of());
        List<List<?>> snap4 = argsList(cm);
        cm.close();
        DurableLRU cr = new DurableLRU(3, p4, true, 5);
        require(argsList(cr).equals(snap4), "compaction + recover");
        cr.close();
        System.out.println("PART 2 compaction + recover OK; final=" + snap4);

        System.out.println("\nALL JAVA CHECKS PASSED");
    }

    private static String newTempPath(String prefix) throws IOException {
        File f = File.createTempFile(prefix, ".log");
        f.delete();        // start from "no file" so recover() takes the empty path
        return f.getPath();
    }
}
