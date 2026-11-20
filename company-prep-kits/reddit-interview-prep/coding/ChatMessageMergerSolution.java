import java.util.*;

/**
 * Reddit — Staff SWE coding problem:
 *   "Chat Message Merge with Windowed API"
 *
 * A single self-contained, COMPILABLE, RUNNABLE file that solves every part.
 * Run:  javac ChatMessageMergerSolution.java && java ChatMessageMergerSolution
 *
 * Parts:
 *   PART 1  Merge context windows -> sorted, de-duplicated, better than O(M log M).
 *           => single last_id watermark pointer sweep.  (the answer interviewers want)
 *   PART 1b K-way min-heap fallback (correct but heavier: O(T log K)).
 *   PART 2  Caching wrapper in front of the expensive API (messages immutable).
 *   PART 3  Minimize API-CALL count (K), not just message count (M/T).
 *   PART 4  Editable messages with append-only version history + as_of(t).
 */
public class ChatMessageMergerSolution {

    /* =====================================================================
       PROVIDED (read-only) DOMAIN MODEL  — exactly as the interviewer gives it
       ===================================================================== */
    static final class Message {
        final int id;          // strictly increasing, immutable
        final String content;
        Message(int id, String content) { this.id = id; this.content = content; }
        @Override public String toString() { return id + ":" + content; }
    }

    /** Read-only chat API. getChatMessages returns the target plus up to
     *  windowSize existing messages before AND after it, sorted by id ascending.
     *  Empty list if the id does not exist. */
    interface Chat {
        List<Message> getChatMessages(int id, int windowSize);
    }

    /* =====================================================================
       PART 1  — THE MAIN ANSWER: single-watermark pointer sweep
       ---------------------------------------------------------------------
       Key insight the interviewer rewards:
         * Each getChatMessages call ALREADY returns a window sorted by id.
         * If we ALSO sort the input ids, windows arrive in increasing order,
           so any overlap between a later window and everything already emitted
           can only appear as a PREFIX of that later window.
         * Therefore ONE "largest id already appended" watermark (last_id) is
           enough to both MERGE and DE-DUPLICATE in a single pass:
           skip every message with id <= last_id, else append + advance.
         * Dedup exploits the strictly-increasing-id invariant: no hash set.

       Complexity:
         O(k log k) to sort the k input ids  +  O(T) to scan all T returned msgs
         O(1) extra state (besides the R-message output).
       This beats the naive "collect into a set then sort" = O(M log M) whenever
       k << M, and even beats the heap merge's O(T log k).
       ===================================================================== */
    static class ChatMessageMerger {
        private final Chat chat;
        private final int windowSize;

        ChatMessageMerger(Chat chat, int windowSize) {
            this.chat = chat;
            this.windowSize = windowSize;
        }

        List<Message> mergeMessages(List<Integer> ids) {
            List<Message> out = new ArrayList<>();
            if (ids == null || ids.isEmpty()) return out;

            // 1) Sort input ids ascending. Handles unsorted / duplicate inputs.
            int[] sorted = new int[ids.size()];
            for (int i = 0; i < ids.size(); i++) sorted[i] = ids.get(i);
            Arrays.sort(sorted);                       // O(k log k)

            // 2) Watermark: largest id already appended. Start below every id.
            long lastId = Long.MIN_VALUE;

            // 3) Single pass merge + dedup. No heap, no hash set.
            for (int id : sorted) {
                List<Message> window = chat.getChatMessages(id, windowSize);
                for (Message m : window) {             // window already sorted asc
                    if (m.id <= lastId) continue;      // skip already-emitted prefix / dupes
                    out.add(m);
                    lastId = m.id;                     // advance watermark
                }
            }
            return out;
        }
    }

    /* =====================================================================
       PART 1b — K-WAY MIN-HEAP FALLBACK (be able to write this in <10 min)
       ---------------------------------------------------------------------
       Correct but heavier: O(T log K). Useful to mention as the generic
       "merge K sorted lists" technique when the prefix-only property is not
       obvious. Still dedups with the same <= last-emitted-id trick.
       ===================================================================== */
    static class HeapMerger {
        private final Chat chat;
        private final int windowSize;
        HeapMerger(Chat chat, int windowSize) { this.chat = chat; this.windowSize = windowSize; }

        // Cursor into one window: (list, index-within-list).
        private static final class Cursor {
            final List<Message> list; int pos;
            Cursor(List<Message> list) { this.list = list; }
            Message peek() { return list.get(pos); }
        }

        List<Message> mergeMessages(List<Integer> ids) {
            List<Message> out = new ArrayList<>();
            if (ids == null || ids.isEmpty()) return out;

            // Sorting the input ids is NOT required for correctness with a heap,
            // but keeps windows overlapping for the API-call optimization later.
            int[] sorted = new int[ids.size()];
            for (int i = 0; i < ids.size(); i++) sorted[i] = ids.get(i);
            Arrays.sort(sorted);

            // Min-heap ordered by the current head id of each window.
            PriorityQueue<Cursor> pq =
                new PriorityQueue<>(Comparator.comparingInt(c -> c.peek().id));
            for (int id : sorted) {
                List<Message> w = chat.getChatMessages(id, windowSize);
                if (!w.isEmpty()) pq.add(new Cursor(w));
            }

            long lastId = Long.MIN_VALUE;
            while (!pq.isEmpty()) {
                Cursor c = pq.poll();
                Message m = c.peek();
                if (m.id > lastId) { out.add(m); lastId = m.id; }  // dedup by id
                c.pos++;
                if (c.pos < c.list.size()) pq.add(c);              // push next head back
            }
            return out;
        }
    }

    /* =====================================================================
       PART 2  — CACHING (follow-up: getChatMessages is expensive)
       ---------------------------------------------------------------------
       Because messages are IMMUTABLE, a cached window can never go stale, so
       NO invalidation is needed while immutability holds. A plain memo works.

       Key choice discussion:
         * Key by the WHOLE WINDOW request (id + windowSize) -> memoizes an
           entire call. Simple, exact, what we implement here.
         * Key by INDIVIDUAL message id -> higher hit rate across overlapping
           windows and lets you reconstruct sub-windows, but you must store
           ordering / neighbor links to answer a windowSize query, and it costs
           more memory. Prefer window-keyed unless windowSize varies a lot.
       Memory bound: LRU-cap the map if the conversation is huge.
       Invalidation (Part 4): once messages become editable, key must include
       the version / as_of snapshot, or you cache the immutable version objects.
       ===================================================================== */
    static class CachingChat implements Chat {
        private final Chat delegate;
        private final Map<Long, List<Message>> cache = new HashMap<>();
        int hits, misses;

        CachingChat(Chat delegate) { this.delegate = delegate; }

        @Override public List<Message> getChatMessages(int id, int windowSize) {
            // Compose a collision-free key from (id, windowSize).
            long key = (((long) id) << 32) | (windowSize & 0xffffffffL);
            List<Message> cached = cache.get(key);
            if (cached != null) { hits++; return cached; }
            misses++;
            List<Message> fresh = delegate.getChatMessages(id, windowSize);
            cache.put(key, fresh);          // immutable => safe to keep forever
            return fresh;
        }
    }

    /* =====================================================================
       PART 3  — MINIMIZE THE NUMBER OF API CALLS (K), not just M
       ---------------------------------------------------------------------
       The stated bar is message-count complexity (T). The follow-up the
       interviewer almost always asks: what is the MINIMUM number of
       getChatMessages calls?

       Reframe: each id j needs its whole window [j-w .. j+w] present. All
       windows have the SAME half-width w, so this is a classic greedy
       INTERVAL COVER — cover the union of intervals with as few of the given
       (id-centered) windows as possible:
         * Sort ids; track `frontier` = furthest id-position covered so far.
         * If the next id has a GAP (id - w > frontier), we must fetch it to
           cover its left edge -> start a new covered run.
         * Otherwise the id connects. Skip every id already fully covered, and
           among the ids that still connect (id - w <= frontier) fetch the
           FURTHEST one, so a single call advances the frontier as far right as
           possible. Ids skipped between the two fetches are fully contained in
           the fetched windows, so no message is lost.
       On contiguous ids 0..10 with w=5 this fetches just {0,10} — 2 calls
       instead of 11 — and still returns every message via the watermark.

       Staff-level nuance: the window is POSITIONAL (w *existing* messages each
       side), so id +/- w is exact only for dense ids. For arbitrarily sparse
       ids the safe fallback is Part 1 (always fetch); this greedy assumes the
       dense/overlapping regime the prompt's own example uses.
       ===================================================================== */
    static class FewerCallsMerger {
        private final Chat chat;
        private final int windowSize;
        int apiCalls;                       // instrumentation to prove the win
        private long lastId;                // Part-1 merge/dedup watermark
        private long lastFetchedMax;        // max id returned by the most recent call

        FewerCallsMerger(Chat chat, int windowSize) { this.chat = chat; this.windowSize = windowSize; }

        List<Message> mergeMessages(List<Integer> ids) {
            List<Message> out = new ArrayList<>();
            if (ids == null || ids.isEmpty()) return out;

            int[] s = new int[ids.size()];
            for (int i = 0; i < ids.size(); i++) s[i] = ids.get(i);
            Arrays.sort(s);

            lastId = Long.MIN_VALUE;
            long frontier = Long.MIN_VALUE;
            int n = s.length, i = 0;

            while (i < n) {
                if (s[i] - (long) windowSize > frontier) {
                    // GAP: must fetch s[i] to cover the left edge of a new run.
                    fetchMerge(s[i], out);
                    frontier = Math.max(frontier, lastFetchedMax);
                    i++;
                } else {
                    // s[i] connects. Advance k to the FURTHEST still-connecting id.
                    int k = i;
                    while (k + 1 < n && s[k + 1] - (long) windowSize <= frontier) k++;
                    // Fetch that furthest id only if it actually extends coverage.
                    if (s[k] + (long) windowSize > frontier) {
                        fetchMerge(s[k], out);
                        frontier = Math.max(frontier, lastFetchedMax);
                    }
                    i = k + 1;   // every id in (i..k] is covered by the fetched windows
                }
            }
            return out;
        }

        private void fetchMerge(int id, List<Message> out) {
            List<Message> window = chat.getChatMessages(id, windowSize);
            apiCalls++;
            if (!window.isEmpty()) lastFetchedMax = Math.max(lastFetchedMax, window.get(window.size() - 1).id);
            for (Message m : window) {          // watermark merge + dedup (Part 1)
                if (m.id <= lastId) continue;
                out.add(m);
                lastId = m.id;
            }
        }
    }

    /* =====================================================================
       PART 4  — EDITABLE MESSAGES WITH VERSION HISTORY
       ---------------------------------------------------------------------
       Drop the immutability assumption. Design move: SEPARATE
         (a) the stable logical identity `id` (constant for the message's life)
         (b) the immutable VERSIONS appended in chronological order.
       Each edit appends a new version; latest() is O(1); as_of(t) binary-
       searches for the rightmost version with edited_at <= t.

       Crucial for Part 1: the merge is UNCHANGED. It still de-dupes by the
       stable message id (NOT by version) — versioning is orthogonal to it.
       Maps to two tables: messages(id, current_version, ...) and
       message_versions(id, version, content, edited_at, ...).
       ===================================================================== */
    static final class MessageVersion {
        final int version; final String content; final long editedAt;
        MessageVersion(int version, String content, long editedAt) {
            this.version = version; this.content = content; this.editedAt = editedAt;
        }
        @Override public String toString() { return "v" + version + "@" + editedAt + ":" + content; }
    }

    static final class VersionedMessage {
        final int id;                                 // stable logical identity
        private int currentVersion;
        private final List<MessageVersion> versions = new ArrayList<>(); // append-only, sorted by editedAt

        VersionedMessage(int id, String content, long createdAt) {
            this.id = id;
            versions.add(new MessageVersion(1, content, createdAt));
            currentVersion = 1;
        }

        /** O(1) append. Rejects out-of-order timestamps (must be non-decreasing). */
        void edit(String newContent, long editedAt) {
            MessageVersion last = versions.get(versions.size() - 1);
            if (editedAt < last.editedAt)
                throw new IllegalArgumentException("edited_at must be non-decreasing");
            versions.add(new MessageVersion(++currentVersion, newContent, editedAt));
        }

        /** O(1) latest read. */
        MessageVersion latest() { return versions.get(versions.size() - 1); }

        /** O(log v): rightmost version with edited_at <= timestamp; null if none
         *  (message did not yet exist at t). Old content is never overwritten. */
        MessageVersion asOf(long timestamp) {
            int lo = 0, hi = versions.size() - 1, ans = -1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (versions.get(mid).editedAt <= timestamp) { ans = mid; lo = mid + 1; }
                else hi = mid - 1;
            }
            return ans < 0 ? null : versions.get(ans);
        }
    }

    /** Versioned chat: resolve each message to the version visible at `asOf`
     *  (or latest when asOf omitted). Windowing/sorting/dedup are identical. */
    interface VersionedChat {
        /** asOf == null -> latest. Messages not yet existing at asOf are dropped. */
        List<Message> getChatMessages(int id, int windowSize, Long asOf);
    }

    static class VersionedChatMessageMerger {
        private final VersionedChat chat;
        private final int windowSize;
        VersionedChatMessageMerger(VersionedChat chat, int windowSize) { this.chat = chat; this.windowSize = windowSize; }

        List<Message> mergeMessagesAsOf(List<Integer> ids, Long asOf) {
            List<Message> out = new ArrayList<>();
            if (ids == null || ids.isEmpty()) return out;
            int[] sorted = new int[ids.size()];
            for (int i = 0; i < ids.size(); i++) sorted[i] = ids.get(i);
            Arrays.sort(sorted);

            long lastId = Long.MIN_VALUE;                 // SAME watermark, dedup by id
            for (int id : sorted) {
                for (Message m : chat.getChatMessages(id, windowSize, asOf)) {
                    if (m.id <= lastId) continue;         // dedup by STABLE id, not version
                    out.add(m);
                    lastId = m.id;
                }
            }
            return out;
        }
    }

    /* =====================================================================
       DEMO / SELF-TEST
       ===================================================================== */

    /** Simple in-memory Chat over a contiguous conversation 0..N-1. */
    static final class InMemoryChat implements Chat {
        final Message[] all;
        int apiCalls;
        InMemoryChat(int n) {
            all = new Message[n];
            for (int i = 0; i < n; i++) all[i] = new Message(i, "m" + i);
        }
        @Override public List<Message> getChatMessages(int id, int windowSize) {
            apiCalls++;
            if (id < 0 || id >= all.length) return Collections.emptyList();  // id not found
            int lo = Math.max(0, id - windowSize);
            int hi = Math.min(all.length - 1, id + windowSize);
            List<Message> w = new ArrayList<>();
            for (int i = lo; i <= hi; i++) w.add(all[i]);
            return w;
        }
    }

    private static List<Integer> ids(List<Message> ms) {
        List<Integer> r = new ArrayList<>();
        for (Message m : ms) r.add(m.id);
        return r;
    }

    public static void main(String[] args) {
        InMemoryChat chat = new InMemoryChat(11);   // ids 0..10 exist
        int windowSize = 5;

        // ---- PART 1: example from the prompt ----
        List<Integer> input = Arrays.asList(5, 1, 3, 3);  // unsorted + duplicate
        List<Message> merged = new ChatMessageMerger(chat, windowSize).mergeMessages(input);
        System.out.println("PART 1 watermark : " + ids(merged));
        //  expected [0,1,2,3,4,5,6,7,8,9,10]

        // ---- PART 1b: heap fallback gives the same result ----
        List<Message> heap = new HeapMerger(chat, windowSize).mergeMessages(input);
        System.out.println("PART 1b heap     : " + ids(heap));

        // ---- PART 2: caching ----
        InMemoryChat backend = new InMemoryChat(11);
        CachingChat cached = new CachingChat(backend);
        ChatMessageMerger cm = new ChatMessageMerger(cached, windowSize);
        cm.mergeMessages(input);            // populates cache (2 distinct ids after dedup: 1,3,5 -> 3 calls)
        cm.mergeMessages(input);            // second run: all cache hits
        System.out.println("PART 2 cache     : backendCalls=" + backend.apiCalls
                + " hits=" + cached.hits + " misses=" + cached.misses);

        // ---- PART 3: fewer API calls ----
        InMemoryChat b2 = new InMemoryChat(1000);
        FewerCallsMerger fc = new FewerCallsMerger(b2, windowSize);
        // ids 0,2,4,...,40 -> heavily overlapping windows collapse into a few calls
        List<Integer> dense = new ArrayList<>();
        for (int i = 0; i <= 40; i += 2) dense.add(i);
        fc.mergeMessages(dense);
        System.out.println("PART 3 fewercalls: ids=" + dense.size()
                + " apiCalls=" + fc.apiCalls + " backendCalls=" + b2.apiCalls);

        // ---- PART 4: versioned message ----
        VersionedMessage vm = new VersionedMessage(7, "hello", 100);
        vm.edit("hello v2", 120);
        vm.edit("hello v3", 150);
        System.out.println("PART 4 latest    : " + vm.latest());          // v3@150
        System.out.println("PART 4 asOf(130) : " + vm.asOf(130));         // v2@120
        System.out.println("PART 4 asOf(90)  : " + vm.asOf(90));          // null (didn't exist)
    }
}
