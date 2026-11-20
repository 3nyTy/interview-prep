import java.util.*;

/**
 * Reddit — Staff SWE coding problem:
 *   "Implement an In-Memory Chatter Message Store"
 *   (the phone-screen variant of Chat Message Merge where YOU own the store,
 *    instead of being handed Chat.getChatMessages(id, windowSize))
 *
 * A single self-contained, COMPILABLE, RUNNABLE file that solves every part.
 * Run:  javac ChatterMessageStore.java && java ChatterMessageStore
 *
 * Parts:
 *   PART 1  Batch loading — idempotent, order-agnostic ingest.
 *   PART 2  getContext(targetId, k) — target + up to k EXISTING neighbours a side.
 *           => TreeMap.lowerKey/higherKey. NOT targetId +/- k. (the trap)
 *   PART 3a Merge many neighborhoods — single watermark sweep. (same trick as the
 *           windowed-API version of this problem)
 *   PART 3b Merge via INTERVAL UNION on the ordered index — the staff-level answer
 *           that owning the store buys you: every message touched exactly once.
 *   PART 4  Caching + generation-counter invalidation. Key must include k.
 *   PART 5  Edits with append-only version history + asOf(t), and the order-vs-
 *           content generation distinction.
 */
public class ChatterMessageStore {

    /* =====================================================================
       DOMAIN MODEL
       ---------------------------------------------------------------------
       Ids are strictly increasing but NOT contiguous — gaps are legal.
       Every part below depends on that.
       ===================================================================== */
    static final class Message {
        final int id;
        final String content;
        Message(int id, String content) { this.id = id; this.content = content; }
        @Override public String toString() { return String.valueOf(id); }
    }

    /* =====================================================================
       PARTS 1-3 — THE STORE
       ---------------------------------------------------------------------
       Structure choice, committed and justified:

         TreeMap<Integer,Message>  <-- THIS ONE
           O(log n) insert, and lowerKey/higherKey walk the k neighbours
           directly. Correct when batches arrive out of order, which is the
           stated ingest model.

         sorted ArrayList + binarySearch
           O(1) index arithmetic for +/-k (faster reads) but O(n) insert for
           every out-of-order message. Only right for a build-once frozen store.

         HashMap<Integer,Message> alone
           CANNOT answer getContext at all — no order, so there is no way to ask
           for "the previous existing id". Naming this rules out the obvious
           wrong answer before the interviewer has to.
       ===================================================================== */
    static class MessageStore {

        private final NavigableMap<Integer, Message> byId = new TreeMap<>();

        /* Two counters, not one. See PART 5 — an edit moves content but never
           order, so a cache of IDS survives an edit while a cache of MESSAGE
           OBJECTS does not. */
        private long orderGeneration   = 0;   // bumped by loadBatch only
        private long contentGeneration = 0;   // bumped by loadBatch AND edits

        long orderGeneration()   { return orderGeneration; }
        long contentGeneration() { return contentGeneration; }
        int size()               { return byId.size(); }

        /* -----------------------------------------------------------------
           PART 1 — BATCH LOAD
           Idempotent and order-agnostic: putIfAbsent means a re-loaded batch
           is a no-op, and an unsorted or late batch lands in the right place
           because the TreeMap owns the ordering, not the caller.
           A batch of pure duplicates must NOT move the generation, or every
           redundant sync would needlessly flush the PART 4 cache.
           O(b log n) for a batch of b.
           ----------------------------------------------------------------- */
        int loadBatch(List<Message> batch) {
            if (batch == null || batch.isEmpty()) return 0;
            int added = 0;
            for (Message m : batch) {
                if (m == null) continue;
                if (byId.putIfAbsent(m.id, m) == null) added++;
            }
            if (added > 0) { orderGeneration++; contentGeneration++; }
            return added;
        }

        /* -----------------------------------------------------------------
           PART 2 — CONTEXT READ
           THE TRAP: "two messages either side" means the two nearest ids that
           EXIST, not targetId +/- 2. Walk adjacency in the ordered store.
           O(k log n).
           ----------------------------------------------------------------- */
        List<Message> getContext(int targetId, int k) {
            if (k < 0) throw new IllegalArgumentException("k must be >= 0");
            Message target = byId.get(targetId);
            if (target == null) return Collections.emptyList();   // absent id

            Deque<Message> left = new ArrayDeque<>();
            Integer cur = targetId;
            for (int i = 0; i < k; i++) {
                cur = byId.lowerKey(cur);                 // previous EXISTING id
                if (cur == null) break;                   // hit start of store
                left.addFirst(byId.get(cur));
            }

            List<Message> out = new ArrayList<>(left);
            out.add(target);

            cur = targetId;
            for (int i = 0; i < k; i++) {
                cur = byId.higherKey(cur);                // next EXISTING id
                if (cur == null) break;                   // hit end of store
                out.add(byId.get(cur));
            }
            return out;
        }

        /* -----------------------------------------------------------------
           PART 3a — MERGE BY WATERMARK SWEEP
           Sort the targets, then read neighborhoods in ascending order. Once
           targets are sorted, any overlap with what has already been emitted
           can only appear as a PREFIX of a later neighborhood — so ONE
           "largest id already emitted" watermark both merges and de-dupes.
           O(1) extra state: no hash set, no heap.
           ----------------------------------------------------------------- */
        List<Message> mergeNeighborhoods(List<Integer> targetIds, int k) {
            if (targetIds == null || targetIds.isEmpty()) return Collections.emptyList();
            List<Integer> ids = new ArrayList<>(targetIds);
            Collections.sort(ids);                        // handles unsorted input

            List<Message> out = new ArrayList<>();
            long watermark = Long.MIN_VALUE;              // > every int id
            for (int id : ids) {                          // duplicates self-skip
                for (Message m : getContext(id, k)) {
                    if (m.id <= watermark) continue;      // already emitted
                    out.add(m);
                    watermark = m.id;
                }
            }
            return out;
        }

        /* -----------------------------------------------------------------
           PART 3b — MERGE BY INTERVAL UNION  (the staff-level answer)
           Owning the store means a neighborhood does not have to be
           materialised before it can be merged. Resolve each target to its
           [loKey, hiKey] bounds, union the overlapping intervals FIRST, then
           emit each merged interval once with a single subMap walk.
           3a fetches then discards duplicates; 3b never produces them.
           Emits exactly R messages for a result of size R.
           ----------------------------------------------------------------- */
        List<Message> mergeNeighborhoodsByRanges(List<Integer> targetIds, int k) {
            if (targetIds == null || targetIds.isEmpty()) return Collections.emptyList();

            // TreeSet: sorted + de-duplicated targets in one step.
            List<int[]> ranges = new ArrayList<>();
            for (int id : new TreeSet<>(targetIds)) {
                if (!byId.containsKey(id)) continue;      // absent target contributes nothing

                Integer lo = id, hi = id;
                for (int i = 0; i < k; i++) { Integer p = byId.lowerKey(lo);  if (p == null) break; lo = p; }
                for (int i = 0; i < k; i++) { Integer n = byId.higherKey(hi); if (n == null) break; hi = n; }

                // Targets ascend => lo ascends, so only the LAST range can overlap.
                if (!ranges.isEmpty() && lo <= ranges.get(ranges.size() - 1)[1]) {
                    int[] last = ranges.get(ranges.size() - 1);
                    last[1] = Math.max(last[1], hi);      // extend
                } else {
                    ranges.add(new int[]{lo, hi});        // start a new run
                }
            }

            List<Message> out = new ArrayList<>();
            for (int[] r : ranges) out.addAll(byId.subMap(r[0], true, r[1], true).values());
            return out;
        }
    }

    /* =====================================================================
       PART 4 — CACHING
       ---------------------------------------------------------------------
       Two things the interviewer is listening for:

       1. THE KEY MUST INCLUDE k. A +/-2 neighborhood is not a +/-5
          neighborhood; keying on targetId alone silently serves a window of
          the wrong width. Composed here as (targetId, k).

       2. INVALIDATE BY GENERATION, NOT PER ENTRY. Inserting one message
          changes the neighborhood of up to 2k+1 already-cached targets, and
          you cannot cheaply enumerate which ones — so bump a counter and drop
          the cache wholesale. Cheap because loads are batched and rare
          relative to reads.

       This caches materialised Message objects, so it keys off
       contentGeneration — the stricter of the two counters (see PART 5).
       ===================================================================== */
    static class CachingStoreView {
        private final MessageStore store;
        private final Map<Long, List<Message>> cache = new HashMap<>();
        private long cachedGeneration = -1;
        int hits = 0, misses = 0, flushes = 0;

        CachingStoreView(MessageStore store) { this.store = store; }

        List<Message> getContext(int targetId, int k) {
            long gen = store.contentGeneration();
            if (gen != cachedGeneration) {                 // store mutated -> drop everything
                if (!cache.isEmpty()) flushes++;
                cache.clear();
                cachedGeneration = gen;
            }
            long key = (((long) targetId) << 32) | (k & 0xFFFFFFFFL);   // collision-free (id, k)
            List<Message> hit = cache.get(key);
            if (hit != null) { hits++; return hit; }
            misses++;
            List<Message> fresh = store.getContext(targetId, k);
            cache.put(key, fresh);
            return fresh;
        }
    }

    /* =====================================================================
       PART 5 — EDITS WITH RETAINED VERSION HISTORY
       ---------------------------------------------------------------------
       Separate (a) stable logical identity `id`, constant for life, from
       (b) immutable versions appended in chronological order. Old content is
       never overwritten, so audit and rollback stay possible.

       THE SHARP POINT: an edit changes content but never an id, so the
       ORDERING INDEX IS UNTOUCHED. That is why the store carries two
       counters — a cache of id lists survives an edit; a cache of
       materialised messages does not.

       De-duplicate the PART 3 merge by message ID, never by version number.
       Versioning is orthogonal to the merge; the merge code is unchanged.
       ===================================================================== */
    static final class MessageVersion {
        final int version;
        final String content;
        final long editedAt;
        MessageVersion(int version, String content, long editedAt) {
            this.version = version; this.content = content; this.editedAt = editedAt;
        }
        @Override public String toString() { return "v" + version + "@" + editedAt + ":" + content; }
    }

    static final class VersionedMessage {
        final int id;
        private final List<MessageVersion> versions = new ArrayList<>();   // append-only, by editedAt

        VersionedMessage(int id, String content, long createdAt) {
            this.id = id;
            versions.add(new MessageVersion(1, content, createdAt));
        }

        /** O(1) append. Rejects out-of-order timestamps: the list is the sort order. */
        void edit(String newContent, long editedAt) {
            MessageVersion last = versions.get(versions.size() - 1);
            if (editedAt < last.editedAt) {
                throw new IllegalArgumentException(
                    "out-of-order edit: " + editedAt + " < " + last.editedAt);
            }
            versions.add(new MessageVersion(last.version + 1, newContent, editedAt));
        }

        /** O(1). */
        MessageVersion latest() { return versions.get(versions.size() - 1); }

        /** O(log v): rightmost version with editedAt <= ts; null if it did not exist yet. */
        MessageVersion asOf(long ts) {
            int lo = 0, hi = versions.size() - 1, ans = -1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (versions.get(mid).editedAt <= ts) { ans = mid; lo = mid + 1; }
                else                                  { hi = mid - 1; }
            }
            return ans < 0 ? null : versions.get(ans);
        }

        int versionCount() { return versions.size(); }
    }

    /** The store again, but versioned. Ordering logic is byte-for-byte the
     *  PART 2 walk — only content resolution changed. */
    static class VersionedMessageStore {
        private final NavigableMap<Integer, VersionedMessage> byId = new TreeMap<>();
        private long orderGeneration = 0, contentGeneration = 0;

        long orderGeneration()   { return orderGeneration; }
        long contentGeneration() { return contentGeneration; }

        int loadBatch(List<Message> batch, long createdAt) {
            int added = 0;
            for (Message m : batch) {
                if (m == null) continue;
                if (byId.putIfAbsent(m.id, new VersionedMessage(m.id, m.content, createdAt)) == null) added++;
            }
            if (added > 0) { orderGeneration++; contentGeneration++; }
            return added;
        }

        /** Content moves, order does not: contentGeneration only. */
        void edit(int id, String newContent, long editedAt) {
            VersionedMessage vm = byId.get(id);
            if (vm == null) throw new NoSuchElementException("no message " + id);
            vm.edit(newContent, editedAt);
            contentGeneration++;
        }

        /** asOf == null -> latest. Messages not yet existing at asOf are dropped. */
        List<String> getContextAsOf(int targetId, int k, Long asOf) {
            if (!byId.containsKey(targetId)) return Collections.emptyList();

            List<VersionedMessage> window = new ArrayList<>();
            Deque<VersionedMessage> left = new ArrayDeque<>();
            Integer cur = targetId;
            for (int i = 0; i < k; i++) {
                cur = byId.lowerKey(cur);
                if (cur == null) break;
                left.addFirst(byId.get(cur));
            }
            window.addAll(left);
            window.add(byId.get(targetId));
            cur = targetId;
            for (int i = 0; i < k; i++) {
                cur = byId.higherKey(cur);
                if (cur == null) break;
                window.add(byId.get(cur));
            }

            List<String> out = new ArrayList<>();
            for (VersionedMessage vm : window) {
                MessageVersion v = (asOf == null) ? vm.latest() : vm.asOf(asOf);
                if (v == null) continue;                  // did not exist at that snapshot
                out.add(vm.id + "=" + v.content);
            }
            return out;
        }
    }

    /* =====================================================================
       DEMO / SELF-CHECK
       ===================================================================== */
    private static List<Message> batch(int... ids) {
        List<Message> out = new ArrayList<>();
        for (int id : ids) out.add(new Message(id, "m" + id));
        return out;
    }

    private static void check(String label, Object actual, Object expected) {
        boolean ok = String.valueOf(actual).equals(String.valueOf(expected));
        System.out.printf("%-46s %-34s %s%n", label, actual, ok ? "OK" : "FAIL expected " + expected);
        if (!ok) failures++;
    }

    private static int failures = 0;

    public static void main(String[] args) {
        System.out.println("=== PART 1: batch load (idempotent, order-agnostic) ===");
        MessageStore store = new MessageStore();
        // deliberately unsorted, arriving in two out-of-order batches, with gaps
        check("added [17,10,19,13]",       store.loadBatch(batch(17, 10, 19, 13)), 4);
        check("added [25,11,30,18,26]",    store.loadBatch(batch(25, 11, 30, 18, 26)), 5);
        check("size",                      store.size(), 9);
        long genBefore = store.orderGeneration();
        check("re-load duplicates -> added", store.loadBatch(batch(13, 17, 25)), 0);
        check("generation unchanged",      store.orderGeneration() == genBefore, true);
        // store now holds: 10 11 13 17 18 19 25 26 30

        System.out.println("\n=== PART 2: getContext — nearest EXISTING neighbours, not id +/- k ===");
        check("getContext(17,2)",  store.getContext(17, 2), "[11, 13, 17, 18, 19]");
        System.out.println("   ^ NOT [15,16,17,18,19] — ids are sparse, so walk adjacency");
        check("getContext(10,2)",  store.getContext(10, 2), "[10, 11, 13]");
        check("getContext(30,2)",  store.getContext(30, 2), "[25, 26, 30]");
        check("getContext(14,2) absent",  store.getContext(14, 2), "[]");
        check("getContext(17,0)",  store.getContext(17, 0), "[17]");
        check("getContext(17,99) whole store", store.getContext(17, 99),
              "[10, 11, 13, 17, 18, 19, 25, 26, 30]");

        System.out.println("\n=== PART 3: merge neighborhoods (both routes must agree) ===");
        List<Integer> targets = Arrays.asList(18, 13, 18);      // unsorted + duplicate
        Object sweep  = store.mergeNeighborhoods(targets, 2);
        Object ranges = store.mergeNeighborhoodsByRanges(targets, 2);
        check("3a watermark sweep",  sweep,  "[10, 11, 13, 17, 18, 19, 25]");
        check("3b interval union",   ranges, "[10, 11, 13, 17, 18, 19, 25]");
        check("3a == 3b",            sweep.equals(ranges), true);
        check("disjoint targets [10,30]", store.mergeNeighborhoods(Arrays.asList(10, 30), 1),
              "[10, 11, 26, 30]");
        check("disjoint via ranges",      store.mergeNeighborhoodsByRanges(Arrays.asList(10, 30), 1),
              "[10, 11, 26, 30]");
        check("absent target ignored",    store.mergeNeighborhoodsByRanges(Arrays.asList(14, 30), 1),
              "[26, 30]");
        check("empty targets",            store.mergeNeighborhoods(new ArrayList<>(), 2), "[]");

        System.out.println("\n=== PART 4: caching — key includes k, generation invalidates ===");
        CachingStoreView view = new CachingStoreView(store);
        view.getContext(17, 2);
        view.getContext(17, 2);                       // hit
        check("hits after repeat",  view.hits, 1);
        view.getContext(17, 5);                       // different k MUST miss
        check("misses (k=2, k=5)",  view.misses, 2);
        store.loadBatch(batch(12));                   // mutate -> generation bump
        view.getContext(17, 2);
        check("flushed on mutation", view.flushes, 1);
        check("neighbour now visible", store.getContext(13, 1), "[12, 13, 17]");

        System.out.println("\n=== PART 5: edits + append-only version history ===");
        VersionedMessageStore vs = new VersionedMessageStore();
        vs.loadBatch(batch(10, 11, 13), 100L);
        long orderGen = vs.orderGeneration();
        vs.edit(11, "edited-at-200", 200L);
        vs.edit(11, "edited-at-300", 300L);
        check("latest",              vs.getContextAsOf(11, 1, null),  "[10=m10, 11=edited-at-300, 13=m13]");
        check("asOf(250)",           vs.getContextAsOf(11, 1, 250L),  "[10=m10, 11=edited-at-200, 13=m13]");
        check("asOf(150) original",  vs.getContextAsOf(11, 1, 150L),  "[10=m10, 11=m11, 13=m13]");
        check("asOf(50) pre-history",vs.getContextAsOf(11, 1, 50L),   "[]");
        check("orderGeneration frozen by edits", vs.orderGeneration() == orderGen, true);
        check("contentGeneration moved",         vs.contentGeneration() > orderGen, true);

        VersionedMessage vm = new VersionedMessage(1, "a", 10L);
        vm.edit("b", 20L);
        check("version count", vm.versionCount(), 2);
        try {
            vm.edit("c", 5L);
            check("out-of-order edit rejected", "no throw", "throw");
        } catch (IllegalArgumentException e) {
            check("out-of-order edit rejected", "threw", "threw");
        }

        System.out.println(failures == 0
            ? "\nALL CHECKS PASSED"
            : "\n" + failures + " CHECK(S) FAILED");
    }
}
