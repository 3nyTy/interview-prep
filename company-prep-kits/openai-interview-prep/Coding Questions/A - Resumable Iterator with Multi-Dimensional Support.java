// =============================================================================
// Resumable Iterator with Multi-Dimensional Support  (Java 21)
// =============================================================================
//
// OpenAI Staff-SWE style progressive coding question. The deliverable is Java,
// but the prompt is phrased in Python. The single contract, modeled identically
// across all four parts, is:
//
//     interface ResumableIterator<T> extends java.util.Iterator<T> {
//         Map<String,Object> getState();          // serializable snapshot
//         void setState(Map<String,Object> state); // restore snapshot
//     }
//
// PYTHON -> JAVA PROTOCOL MAPPING (explained in detail inside Part1):
//   - Python's __iter__/__next__ map to java.util.Iterator's hasNext()/next().
//   - Python's StopIteration maps to NoSuchElementException thrown by next()
//     when the structure is exhausted.
//   - get_state()/set_state() map 1:1 to getState()/setState(); the "state" is
//     a serializable Map<String,Object> whose values are always Integer.
//
// ITERATION STRATEGY (Strategy A, used in ALL parts, adapted to 1/2/3 indices):
//   Position indices point at the NEXT CANDIDATE to examine (never a buffered
//   pulled element). A private skipToNext() advances the indices past every
//   empty / exhausted list until it parks on a yieldable element or the whole
//   structure is exhausted. skipToNext() is IDEMPOTENT: if already parked on a
//   real element it does nothing. hasNext() runs skipToNext() and reports
//   whether a yieldable position exists (side-effect-free / repeatable). next()
//   runs skipToNext(), throws NoSuchElementException if exhausted, otherwise
//   reads the element at the current position, advances the innermost index by
//   one, and returns. Because nothing is buffered across getState(), saving the
//   raw indices (even "one past" a row boundary) restores EXACTLY because
//   skipToNext() re-normalizes them on the next access.
//
// STATE SERIALIZATION (dependency-free, hand-rolled, duplicated per part):
//   In-memory state is a LinkedHashMap<String,Object> with Integer values only
//   (indices + counts). Two tiny helpers per part:
//     - toJson(Map)   : writes a flat {"key":int,...} object (ASCII identifier
//                       keys, unquoted int values -> no escaping needed).
//     - fromJson(str) : parses exactly that flat shape back into a
//                       LinkedHashMap<String,Object> with Integer values.
//   All values are Integer (never Long) so Map.equals holds and a String
//   round-trip is provable. The codec only handles flat {string:int}.
//
// PART SUMMARY:
//   Part1 - Abstract interface + a minimal DemoListIterator. Establishes the
//           contract, the Python<->Java mapping, and the 4 unit-test-style
//           checks (basic_iteration, get_state_returns_serializable,
//           set_state_restores_position, pause_and_resume incl. a serialized
//           cross-iterator resume).
//   Part2 - ResumableListIterator: 1D, state {index,total_items}, cross-data
//           validation + bounds checks + full edge-case suite.
//   Part3 - Resumable2DIterator: list-of-lists, state {outer_index,inner_index},
//           skipToNext() skips empty/exhausted rows (amortized O(1)).
//   Part4 - Resumable3DIterator: list-of-lists-of-lists, state adds
//           {middle_index}, skipToNext() gains the third level.
//
// COMPLEXITY (same reasoning across parts):
//   next(): amortized O(1) (each list at each level is advanced-past at most
//           once across the whole iteration); full iteration of N elements: O(N).
//   getState()/setState(): O(1).  Space: O(1) (only indices, never copies).
//
// SELF-CONTAINMENT CONTRACT (hard requirement):
//   Each PartN is a self-contained static nested class with its OWN
//   public static void main, ALL helper types nested inside it, and ZERO
//   references to other parts or shared top-level types. Copying any single
//   PartN into its own file (promote to top-level, drop `static`, rename the
//   class to PartN) compiles and runs unchanged. Hence the interface, concrete
//   classes, JSON codec, and check helpers are DUPLICATED inside every PartN.
//
// -----------------------------------------------------------------------------
// ASYNC / CONCURRENT EXTENSION (documented, NOT a runnable part)
// -----------------------------------------------------------------------------
// The bonus asks about async iteration over file/network sources. It needs real
// async I/O, so it is intentionally out of scope for the pure-java self-contained
// contract above and is documented here as comments only.
//
//   * Python has `async def __anext__` + `async for`; Java has no async-iterator
//     keyword. Idiomatic Java mappings:
//       (a) CompletableFuture<T> nextAsync()  -> caller composes with thenApply
//           / thenCompose; completion signals "next item ready".
//       (b) java.util.concurrent.Flow.Publisher<T> (reactive streams) with a
//           Subscriber that requests items and stops on demand.
//       (c) Java 21 virtual threads doing ORDINARY BLOCKING I/O behind a normal
//           Iterator. Often the simplest: a virtual thread per consumer blocks
//           on read() while the platform stays responsive. This keeps the exact
//           same synchronous Iterator + getState/setState contract.
//   * STATE FOR I/O SOURCES: store a byte/char OFFSET (long), NOT a file handle
//     (handles/Channels are not serializable). On setState, REOPEN the stream
//     and seek/skip to that offset:
//         SeekableByteChannel.position(offset)   // NIO channel
//         RandomAccessFile.seek(offset)          // RAF
//         InputStream.skipNBytes(offset)         // forward-only streams
//     For line-oriented sources also persist any partial-line buffer length so a
//     resume that landed mid-line can reconstruct the leftover bytes.
//   * THREAD-SAFETY: the index-based iterators here are NOT thread-safe.
//     Concurrent resume requires external synchronization or per-consumer copies;
//     if a single iterator is shared, take getState()/setState() snapshots under
//     a lock. (next() mutates indices; concurrent next() calls race.)
//   * WHY THIS MATTERS: ETL checkpointing, distributed resume (pause on machine
//     A, resume on machine B from the serialized state), crash recovery across
//     process restarts, rate-limited APIs (pause on 429, resume later), and
//     streaming data across network interruptions.
// =============================================================================

// NOTE: No top-level imports. Per the self-containment contract, each PartN is
// promotable to its own top-level file unchanged, so every part references
// standard-library types by their FULLY-QUALIFIED java.* names inline (e.g.
// java.util.NoSuchElementException) rather than relying on a shared import.

class ResumableIteratorMultiDim {

    // Top-level dispatcher only. It must NOT reference any helper type that lives
    // inside a PartN class; it only invokes each PartN.main(...).
    public static void main(String[] args) {
        if (args.length == 0) {
            // No arg -> run every part end to end.
            Part1.main(new String[0]);
            Part2.main(new String[0]);
            Part3.main(new String[0]);
            Part4.main(new String[0]);
            System.out.println("ALL PARTS: all checks passed");
            return;
        }
        switch (args[0]) {
            case "part1" -> Part1.main(new String[0]);
            case "part2" -> Part2.main(new String[0]);
            case "part3" -> Part3.main(new String[0]);
            case "part4" -> Part4.main(new String[0]);
            default -> {
                System.out.println("Unknown part: " + args[0]
                        + " (expected part1|part2|part3|part4, or no arg for all)");
                throw new IllegalArgumentException("Unknown part: " + args[0]);
            }
        }
    }

    // =========================================================================
    // PART 1 - Design the Abstract Interface
    // =========================================================================
    // Establishes the contract and the Python<->Java protocol mapping. Provides a
    // minimal DemoListIterator (state = {"index": i}) just to exercise the four
    // unit-test-style checks. Fully self-contained: interface, demo class, JSON
    // codec, and check helpers all live INSIDE Part1.
    //
    // WHAT PART 1 ESTABLISHES:
    //   * The single iteration+state contract reused by every later part.
    //   * Iterator-protocol mapping: hasNext()/next() <-> __iter__/__next__;
    //     NoSuchElementException <-> StopIteration; getState/setState identical.
    //   * That state is a JSON-serializable flat {string:int} map and survives a
    //     String round-trip, including resuming a FRESH iterator from the parsed
    //     state (the essence of pause-and-resume / checkpointing).
    static final class Part1 {

        // --- The contract -----------------------------------------------------
        // Python's ResumableIterator(ABC) with __iter__/__next__/get_state/
        // set_state maps to java.util.Iterator (hasNext/next) plus two state
        // methods. next() throwing NoSuchElementException is the Java analog of
        // raising StopIteration.
        interface ResumableIterator<T> extends java.util.Iterator<T> {
            java.util.Map<String, Object> getState();
            void setState(java.util.Map<String, Object> state);
        }

        // --- Minimal concrete demo backed by a List + an int index -----------
        static final class DemoListIterator<T> implements ResumableIterator<T> {
            private final java.util.List<T> items;
            private int index = 0;

            DemoListIterator(java.util.List<T> items) {
                this.items = java.util.Objects.requireNonNull(items, "items");
            }

            @Override public boolean hasNext() { return index < items.size(); }

            @Override public T next() {
                if (index >= items.size()) {
                    throw new java.util.NoSuchElementException("iterator exhausted"); // <-> StopIteration
                }
                return items.get(index++);
            }

            @Override public java.util.Map<String, Object> getState() {
                java.util.Map<String, Object> s = new java.util.LinkedHashMap<>();
                s.put("index", index); // autoboxes to Integer
                return s;
            }

            @Override public void setState(java.util.Map<String, Object> state) {
                this.index = (Integer) state.get("index");
            }
        }

        // --- Hand-rolled flat {string:int} JSON codec -------------------------
        private static String toJson(java.util.Map<String, Object> m) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (var e : m.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(e.getKey()).append("\":").append(((Integer) e.getValue()).intValue());
            }
            return sb.append('}').toString();
        }

        private static java.util.Map<String, Object> fromJson(String json) {
            java.util.Map<String, Object> m = new java.util.LinkedHashMap<>();
            String body = json.trim();
            body = body.substring(1, body.length() - 1).trim(); // strip { }
            if (body.isEmpty()) return m;
            for (String pair : body.split(",")) {
                int colon = pair.indexOf(':');
                String key = pair.substring(0, colon).trim();
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                int val = Integer.parseInt(pair.substring(colon + 1).trim()); // -> Integer
                m.put(key, val);
            }
            return m;
        }

        // --- Check helpers (replace `assert`, which needs -ea) ----------------
        private static void expect(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }
        private static <T> void expectEquals(T actual, T expected, String msg) {
            expect(java.util.Objects.equals(actual, expected),
                    msg + " expected=" + expected + " got=" + actual);
        }

        public static void main(String[] args) {
            // test_basic_iteration: iterate all elements, then next() raises.
            {
                ResumableIterator<Integer> it = new DemoListIterator<>(java.util.List.of(1, 2, 3));
                expectEquals(it.next(), 1, "basic[0]");
                expectEquals(it.next(), 2, "basic[1]");
                expectEquals(it.next(), 3, "basic[2]");
                expect(!it.hasNext(), "basic: should be exhausted");
                boolean threw = false;
                try { it.next(); } catch (java.util.NoSuchElementException ex) { threw = true; }
                expect(threw, "basic: next() must throw NoSuchElementException at end (StopIteration analog)");
            }

            // test_get_state_returns_serializable: json.dumps analog must not raise,
            // and a String round-trip must reproduce the same map.
            {
                ResumableIterator<Integer> it = new DemoListIterator<>(java.util.List.of(1, 2, 3));
                java.util.Map<String, Object> state = it.getState();
                String json = toJson(state);            // must not throw
                expect(json.contains("\"index\""), "serializable: json contains index key");
                expectEquals(fromJson(json), state, "serializable: round-trip equals");
            }

            // test_set_state_restores_position: advance to idx1, save, advance to 2,
            // restore, next() == element at index 1 == 2.
            {
                ResumableIterator<Integer> it = new DemoListIterator<>(java.util.List.of(1, 2, 3));
                it.next();                               // index -> 1
                java.util.Map<String, Object> state = it.getState();
                it.next();                               // index -> 2
                it.setState(state);                      // restore to index 1
                expectEquals(it.next(), 2, "restore: element at index 1 == 2");
            }

            // test_pause_and_resume: iterate [10,20,30,40]; next->10,20; save; build a
            // NEW iterator; setState; next->30,40. Plus a serialized-String variant.
            {
                java.util.List<Integer> data = java.util.List.of(10, 20, 30, 40);
                ResumableIterator<Integer> it = new DemoListIterator<>(data);
                expectEquals(it.next(), 10, "pause[0]");
                expectEquals(it.next(), 20, "pause[1]");
                java.util.Map<String, Object> state = it.getState();

                ResumableIterator<Integer> fresh = new DemoListIterator<>(data);
                fresh.setState(state);
                expectEquals(fresh.next(), 30, "resume[0]");
                expectEquals(fresh.next(), 40, "resume[1]");

                // Serialized cross-iterator resume: save -> String -> parse -> fresh.
                String saved = toJson(state);
                ResumableIterator<Integer> fresh2 = new DemoListIterator<>(data);
                fresh2.setState(fromJson(saved));
                expectEquals(fresh2.next(), 30, "resume(json)[0]");
                expectEquals(fresh2.next(), 40, "resume(json)[1]");
            }

            System.out.println("Part 1: all checks passed");
        }
    }

    // =========================================================================
    // PART 2 - Resumable List Iterator
    // =========================================================================
    // CHANGES FROM PART 1:
    //   * Concrete ResumableListIterator over a real List<T>.
    //   * State grows to {"index","total_items"}; setState now VALIDATES:
    //       - total_items must match items.size() (else IllegalArgumentException:
    //         "State was saved for different data"), guarding cross-data restores;
    //       - index must satisfy 0 <= index <= size (index==size == exhausted,
    //         which is legal), else IndexOutOfBoundsException.
    //   * Full edge-case suite: empty list, set_state beyond bounds, incompatible
    //     total_items, multiple getState idempotent, restore after exhaustion.
    //   * The interface / codec / check helpers are unchanged but RE-DECLARED
    //     locally so this part is independently compilable.
    static final class Part2 {

        interface ResumableIterator<T> extends java.util.Iterator<T> {
            java.util.Map<String, Object> getState();
            void setState(java.util.Map<String, Object> state);
        }

        static final class ResumableListIterator<T> implements ResumableIterator<T> {
            private final java.util.List<T> items;
            private int index = 0;

            ResumableListIterator(java.util.List<T> items) {
                this.items = java.util.Objects.requireNonNull(items, "items");
            }

            @Override public boolean hasNext() { return index < items.size(); }

            @Override public T next() {
                if (index >= items.size()) {
                    throw new java.util.NoSuchElementException("iterator exhausted"); // <-> StopIteration
                }
                return items.get(index++);
            }

            @Override public java.util.Map<String, Object> getState() {
                java.util.Map<String, Object> s = new java.util.LinkedHashMap<>();
                s.put("index", index);
                s.put("total_items", items.size());
                return s;
            }

            @Override public void setState(java.util.Map<String, Object> state) {
                Object total = state.get("total_items");
                if (total != null && ((Integer) total) != items.size()) {
                    throw new IllegalArgumentException("State was saved for different data");
                }
                int newIndex = (Integer) state.get("index");
                if (newIndex < 0 || newIndex > items.size()) { // ==size is exhausted, legal
                    throw new IndexOutOfBoundsException(
                            "index " + newIndex + " out of bounds for size " + items.size());
                }
                this.index = newIndex;
            }
        }

        private static String toJson(java.util.Map<String, Object> m) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (var e : m.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(e.getKey()).append("\":").append(((Integer) e.getValue()).intValue());
            }
            return sb.append('}').toString();
        }

        private static java.util.Map<String, Object> fromJson(String json) {
            java.util.Map<String, Object> m = new java.util.LinkedHashMap<>();
            String body = json.trim();
            body = body.substring(1, body.length() - 1).trim();
            if (body.isEmpty()) return m;
            for (String pair : body.split(",")) {
                int colon = pair.indexOf(':');
                String key = pair.substring(0, colon).trim();
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                int val = Integer.parseInt(pair.substring(colon + 1).trim());
                m.put(key, val);
            }
            return m;
        }

        private static void expect(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }
        private static <T> void expectEquals(T actual, T expected, String msg) {
            expect(java.util.Objects.equals(actual, expected),
                    msg + " expected=" + expected + " got=" + actual);
        }

        public static void main(String[] args) {
            // ---- The exact a..e usage example from the prompt, as checks --------
            {
                ResumableIterator<String> it =
                        new ResumableListIterator<>(java.util.List.of("a", "b", "c", "d", "e"));
                expectEquals(it.next(), "a", "ae: next->a");
                expectEquals(it.next(), "b", "ae: next->b");

                java.util.Map<String, Object> state = it.getState();
                // state == {'index':2,'total_items':5}; compare order-independently.
                expectEquals(state, java.util.Map.of("index", 2, "total_items", 5), "ae: state snapshot");

                expectEquals(it.next(), "c", "ae: next->c");
                it.setState(state);                       // restore to index 2
                expectEquals(it.next(), "c", "ae: next->c (again after restore)");
                expectEquals(it.next(), "d", "ae: next->d");
                expectEquals(it.next(), "e", "ae: next->e");
                expect(!it.hasNext(), "ae: exhausted");
                boolean threw = false;
                try { it.next(); } catch (java.util.NoSuchElementException ex) { threw = true; }
                expect(threw, "ae: next() past end throws");
            }

            // ---- Serialization round-trip + FRESH iterator resume to c,d,e ------
            {
                java.util.List<String> data = java.util.List.of("a", "b", "c", "d", "e");
                ResumableIterator<String> it = new ResumableListIterator<>(data);
                it.next(); it.next();                     // consume a, b
                java.util.Map<String, Object> state = it.getState();
                String json = toJson(state);
                expectEquals(fromJson(json), state, "ae: json round-trip equals");

                ResumableIterator<String> fresh = new ResumableListIterator<>(data);
                fresh.setState(fromJson(json));
                expectEquals(fresh.next(), "c", "ae(fresh): next->c");
                expectEquals(fresh.next(), "d", "ae(fresh): next->d");
                expectEquals(fresh.next(), "e", "ae(fresh): next->e");
            }

            // ---- Edge case 1: empty list -> immediately exhausted ---------------
            {
                ResumableIterator<String> it = new ResumableListIterator<>(java.util.List.<String>of());
                expect(!it.hasNext(), "empty: no next");
                boolean threw = false;
                try { it.next(); } catch (java.util.NoSuchElementException ex) { threw = true; }
                expect(threw, "empty: next() throws");
                // getState on empty is well-defined.
                expectEquals(it.getState(), java.util.Map.of("index", 0, "total_items", 0), "empty: state");
            }

            // ---- Edge case 2: set_state beyond bounds -> throws -----------------
            {
                ResumableIterator<String> it = new ResumableListIterator<>(java.util.List.of("x", "y"));
                java.util.Map<String, Object> bad = new java.util.LinkedHashMap<>();
                bad.put("index", 99);
                bad.put("total_items", 2);                // size matches, only index is bad
                boolean threw = false;
                try { it.setState(bad); } catch (IndexOutOfBoundsException ex) { threw = true; }
                expect(threw, "beyond-bounds: setState throws IndexOutOfBoundsException");
            }

            // ---- Edge case 3: incompatible total_items -> throws ----------------
            {
                ResumableIterator<String> small = new ResumableListIterator<>(java.util.List.of("a", "b"));
                java.util.Map<String, Object> stateFromBig =
                        new ResumableListIterator<>(java.util.List.of("a", "b", "c", "d", "e")).getState();
                boolean threw = false;
                try { small.setState(stateFromBig); } catch (IllegalArgumentException ex) { threw = true; }
                expect(threw, "incompatible: setState across different data throws");
            }

            // ---- Edge case 4: multiple getState without advancing is idempotent -
            {
                ResumableIterator<String> it = new ResumableListIterator<>(java.util.List.of("a", "b", "c"));
                it.next();                                // index -> 1
                java.util.Map<String, Object> s1 = it.getState();
                java.util.Map<String, Object> s2 = it.getState();
                expectEquals(s1, s2, "idempotent: repeated getState equal");
                expectEquals(it.next(), "b", "idempotent: getState did not advance");
            }

            // ---- Edge case 5: restoring state after exhaustion ------------------
            {
                java.util.List<String> data = java.util.List.of("a", "b");
                ResumableIterator<String> it = new ResumableListIterator<>(data);
                it.next(); it.next();                     // exhaust -> index == size == 2
                java.util.Map<String, Object> end = it.getState();
                expectEquals(end, java.util.Map.of("index", 2, "total_items", 2), "exhausted: state index==size");
                ResumableIterator<String> fresh = new ResumableListIterator<>(data);
                fresh.setState(end);                      // index==size is legal (exhausted)
                expect(!fresh.hasNext(), "exhausted: restored fresh iterator is exhausted");
            }

            System.out.println("Part 2: all checks passed");
        }
    }

    // =========================================================================
    // PART 3 - 2D Resumable Iterator
    // =========================================================================
    // CHANGES FROM PART 2:
    //   * Input is now List<List<T>>; iteration flattens row by row.
    //   * State is {"outer_index","inner_index"} (total_items is DROPPED to match
    //     the reference design; setState is permissive and relies on skipToNext()
    //     to normalize any "one-past-the-row" indices).
    //   * next() gains skipToNext(): it skips empty AND exhausted inner rows. The
    //     single guard `inner >= cur.size()` folds BOTH cases because on first
    //     arrival at a row inner==0, so an empty row (size 0) trips the same guard
    //     as a fully consumed row. This makes next() amortized O(1): each row is
    //     advanced-past at most once over the whole iteration.
    static final class Part3 {

        interface ResumableIterator<T> extends java.util.Iterator<T> {
            java.util.Map<String, Object> getState();
            void setState(java.util.Map<String, Object> state);
        }

        static final class Resumable2DIterator<T> implements ResumableIterator<T> {
            private final java.util.List<java.util.List<T>> items;
            private int outer = 0;
            private int inner = 0;

            Resumable2DIterator(java.util.List<java.util.List<T>> items) {
                this.items = java.util.Objects.requireNonNull(items, "items");
            }

            // Advance (outer,inner) past every empty/exhausted row until parked on
            // a yieldable element or the structure is exhausted. IDEMPOTENT: if
            // already on a real element the loop breaks immediately.
            private void skipToNext() {
                while (outer < items.size()) {
                    java.util.List<T> cur = items.get(outer);
                    if (inner >= cur.size()) { // empty row (inner==0) OR exhausted row
                        outer++;
                        inner = 0;
                        continue;
                    }
                    break; // parked on a real element at (outer, inner)
                }
            }

            @Override public boolean hasNext() {
                skipToNext();              // side-effect-free w.r.t. yieldable position
                return outer < items.size();
            }

            @Override public T next() {
                skipToNext();
                if (outer >= items.size()) {
                    throw new java.util.NoSuchElementException("iterator exhausted"); // <-> StopIteration
                }
                T result = items.get(outer).get(inner);
                inner++;                   // advance innermost; skipToNext() fixes row crossings later
                return result;
            }

            @Override public java.util.Map<String, Object> getState() {
                java.util.Map<String, Object> s = new java.util.LinkedHashMap<>();
                s.put("outer_index", outer);
                s.put("inner_index", inner);
                return s;
            }

            // Permissive: assign both indices verbatim. Even a "one past the row
            // end" pair (e.g. outer=0,inner=1 on a size-1 row) restores correctly
            // because the next hasNext()/next() runs skipToNext() to normalize.
            @Override public void setState(java.util.Map<String, Object> state) {
                this.outer = (Integer) state.get("outer_index");
                this.inner = (Integer) state.get("inner_index");
            }
        }

        private static String toJson(java.util.Map<String, Object> m) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (var e : m.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(e.getKey()).append("\":").append(((Integer) e.getValue()).intValue());
            }
            return sb.append('}').toString();
        }

        private static java.util.Map<String, Object> fromJson(String json) {
            java.util.Map<String, Object> m = new java.util.LinkedHashMap<>();
            String body = json.trim();
            body = body.substring(1, body.length() - 1).trim();
            if (body.isEmpty()) return m;
            for (String pair : body.split(",")) {
                int colon = pair.indexOf(':');
                String key = pair.substring(0, colon).trim();
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                int val = Integer.parseInt(pair.substring(colon + 1).trim());
                m.put(key, val);
            }
            return m;
        }

        private static void expect(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }
        private static <T> void expectEquals(T actual, T expected, String msg) {
            expect(java.util.Objects.equals(actual, expected),
                    msg + " expected=" + expected + " got=" + actual);
        }

        // Drain an iterator into a List for whole-iteration checks.
        private static <T> java.util.List<T> drain(ResumableIterator<T> it) {
            java.util.List<T> out = new java.util.ArrayList<>();
            while (it.hasNext()) out.add(it.next());
            return out;
        }

        public static void main(String[] args) {
            // ---- Main usage example: [[1,2,3],[],[4,5],[6]] ---------------------
            {
                java.util.List<java.util.List<Integer>> data = java.util.List.of(
                        java.util.List.of(1, 2, 3),
                        java.util.List.<Integer>of(),     // explicit type witness for empty row
                        java.util.List.of(4, 5),
                        java.util.List.of(6));
                ResumableIterator<Integer> it = new Resumable2DIterator<>(data);
                expectEquals(it.next(), 1, "2d-main: next->1");
                expectEquals(it.next(), 2, "2d-main: next->2");
                expectEquals(it.next(), 3, "2d-main: next->3");

                java.util.Map<String, Object> state = it.getState(); // finished row0; about to skip empty row1

                expectEquals(it.next(), 4, "2d-main: next->4 (skips empty list)");
                expectEquals(it.next(), 5, "2d-main: next->5");

                it.setState(state);
                expectEquals(it.next(), 4, "2d-main: next->4 (resumes, skipping empty)");
                expectEquals(it.next(), 5, "2d-main: next->5 (again)");
                expectEquals(it.next(), 6, "2d-main: next->6");
                expect(!it.hasNext(), "2d-main: exhausted");
                boolean threw = false;
                try { it.next(); } catch (java.util.NoSuchElementException ex) { threw = true; }
                expect(threw, "2d-main: next() past end throws");

                // Serialization round-trip + FRESH iterator resume to 4,5,6.
                String json = toJson(state);
                expectEquals(fromJson(json), state, "2d-main: json round-trip equals");
                ResumableIterator<Integer> fresh = new Resumable2DIterator<>(data);
                fresh.setState(fromJson(json));
                expectEquals(fresh.next(), 4, "2d-main(fresh): next->4");
                expectEquals(fresh.next(), 5, "2d-main(fresh): next->5");
                expectEquals(fresh.next(), 6, "2d-main(fresh): next->6");
            }

            // ---- Corner case 1: all-empty -> immediately exhausted --------------
            {
                java.util.List<java.util.List<Integer>> data = java.util.List.of(
                        java.util.List.<Integer>of(), java.util.List.<Integer>of(), java.util.List.<Integer>of());
                ResumableIterator<Integer> it = new Resumable2DIterator<>(data);
                expect(!it.hasNext(), "2d-allempty: no next");
                boolean threw = false;
                try { it.next(); } catch (java.util.NoSuchElementException ex) { threw = true; }
                expect(threw, "2d-allempty: next() throws immediately");
            }

            // ---- Corner case 2: empty at boundaries -> drain == [1,2] -----------
            {
                java.util.List<java.util.List<Integer>> data = java.util.List.of(
                        java.util.List.<Integer>of(), java.util.List.of(1, 2), java.util.List.<Integer>of());
                expectEquals(drain(new Resumable2DIterator<>(data)), java.util.List.of(1, 2),
                        "2d-boundaries: list(iter)==[1,2]");
            }

            // ---- Corner case 3: state capture at row boundary [[1],[2],[3]] -----
            {
                java.util.List<java.util.List<Integer>> data = java.util.List.of(
                        java.util.List.of(1), java.util.List.of(2), java.util.List.of(3));
                ResumableIterator<Integer> it = new Resumable2DIterator<>(data);
                expectEquals(it.next(), 1, "2d-bound: next->1");
                java.util.Map<String, Object> state = it.getState(); // outer=0, inner=1 (one past row0)
                expectEquals(state, java.util.Map.of("outer_index", 0, "inner_index", 1),
                        "2d-bound: state outer=0,inner=1");
                expectEquals(it.next(), 2, "2d-bound: next->2");
                it.setState(state);                       // restore one-past-row0
                expectEquals(it.next(), 2, "2d-bound: resume next==2 (skipToNext normalizes)");
            }

            // ---- Corner case 4: irregular [[1],[2,3,4,5],[6,7]] -----------------
            // Save state AFTER the next() that returns the 3rd produced value (3),
            // restore, expect next()==4.
            {
                java.util.List<java.util.List<Integer>> data = java.util.List.of(
                        java.util.List.of(1), java.util.List.of(2, 3, 4, 5), java.util.List.of(6, 7));
                ResumableIterator<Integer> it = new Resumable2DIterator<>(data);
                java.util.Map<String, Object> stateAfter3 = null;
                int produced = 0;
                while (it.hasNext()) {
                    int v = it.next();
                    produced++;
                    if (produced == 3) {                  // 3rd produced value is 3
                        expectEquals(v, 3, "2d-irregular: 3rd produced value is 3");
                        stateAfter3 = it.getState();      // capture AFTER returning 3
                        break;
                    }
                }
                ResumableIterator<Integer> resume = new Resumable2DIterator<>(data);
                resume.setState(stateAfter3);
                expectEquals(resume.next(), 4, "2d-irregular: resume next==4");
            }

            // ---- Comprehensive table --------------------------------------------
            {
                record Case(java.util.List<java.util.List<Integer>> input, java.util.List<Integer> expected) {}
                java.util.List<Case> cases = java.util.List.of(
                        new Case(java.util.List.of(java.util.List.of(1, 2), java.util.List.of(3, 4)),
                                java.util.List.of(1, 2, 3, 4)),
                        new Case(java.util.List.of(java.util.List.<Integer>of(), java.util.List.of(1), java.util.List.<Integer>of()),
                                java.util.List.of(1)),
                        new Case(java.util.List.of(java.util.List.<Integer>of(), java.util.List.<Integer>of(), java.util.List.<Integer>of()),
                                java.util.List.<Integer>of()),
                        new Case(java.util.List.of(java.util.List.of(1)),
                                java.util.List.of(1)),
                        new Case(java.util.List.of(java.util.List.of(1, 2, 3)),
                                java.util.List.of(1, 2, 3)),
                        new Case(java.util.List.of(java.util.List.of(1), java.util.List.of(2), java.util.List.of(3)),
                                java.util.List.of(1, 2, 3)));
                for (Case c : cases) {
                    expectEquals(drain(new Resumable2DIterator<>(c.input())), c.expected(),
                            "2d-table: " + c.input());
                }
            }

            // ---- Pause/resume on [[1,2],[3,4]] ----------------------------------
            // two next, state, one next, setState, next==3.
            {
                java.util.List<java.util.List<Integer>> data = java.util.List.of(
                        java.util.List.of(1, 2), java.util.List.of(3, 4));
                ResumableIterator<Integer> it = new Resumable2DIterator<>(data);
                it.next();                                // 1
                it.next();                                // 2
                java.util.Map<String, Object> state = it.getState();
                it.next();                                // 3 (advance past saved point)
                it.setState(state);
                expectEquals(it.next(), 3, "2d-pause: resume next==3");
            }

            System.out.println("Part 3: all checks passed");
        }
    }

    // =========================================================================
    // PART 4 - 3D Resumable Iterator
    // =========================================================================
    // CHANGES FROM PART 3:
    //   * Input is now List<List<List<T>>>; iteration flattens depth-first.
    //   * State gains a third field: {"outer_index","middle_index","inner_index"}.
    //   * skipToNext() gains the third level. ORDER MATTERS:
    //       1) if middle >= items.get(outer).size()  -> advance outer (reset
    //          middle+inner). This MUST come first so we never index a middle that
    //          doesn't exist in the current outer.
    //       2) else look at cur = items.get(outer).get(middle); if
    //          inner >= cur.size() -> advance middle (reset inner). The unified
    //          `inner >= cur.size()` guard folds BOTH an empty innermost list
    //          (size 0, inner==0) and a fully consumed one.
    //       3) else break: parked on a real element.
    //   * setState is permissive (assign all three; skipToNext normalizes).
    //   * O(1) amortized still holds: each list at EACH level is advanced-past at
    //     most once across the full iteration.
    static final class Part4 {

        interface ResumableIterator<T> extends java.util.Iterator<T> {
            java.util.Map<String, Object> getState();
            void setState(java.util.Map<String, Object> state);
        }

        static final class Resumable3DIterator<T> implements ResumableIterator<T> {
            private final java.util.List<java.util.List<java.util.List<T>>> items;
            private int outer = 0;
            private int middle = 0;
            private int inner = 0;

            Resumable3DIterator(java.util.List<java.util.List<java.util.List<T>>> items) {
                this.items = java.util.Objects.requireNonNull(items, "items");
            }

            // Skip empty/exhausted lists at the middle AND inner levels until
            // parked on a real element or fully exhausted. IDEMPOTENT.
            private void skipToNext() {
                while (outer < items.size()) {
                    // (1) middle-bound check FIRST: middle exhausted for this outer.
                    if (middle >= items.get(outer).size()) {
                        outer++;
                        middle = 0;
                        inner = 0;
                        continue;
                    }
                    // (2) inner empty OR exhausted (unified guard).
                    java.util.List<T> cur = items.get(outer).get(middle);
                    if (inner >= cur.size()) {
                        middle++;
                        inner = 0;
                        continue;
                    }
                    // (3) parked on a real element at (outer, middle, inner).
                    break;
                }
            }

            @Override public boolean hasNext() {
                skipToNext();
                return outer < items.size();
            }

            @Override public T next() {
                skipToNext();
                if (outer >= items.size()) {
                    throw new java.util.NoSuchElementException("iterator exhausted"); // <-> StopIteration
                }
                T result = items.get(outer).get(middle).get(inner);
                inner++;
                return result;
            }

            @Override public java.util.Map<String, Object> getState() {
                java.util.Map<String, Object> s = new java.util.LinkedHashMap<>();
                s.put("outer_index", outer);
                s.put("middle_index", middle);
                s.put("inner_index", inner);
                return s;
            }

            @Override public void setState(java.util.Map<String, Object> state) {
                this.outer = (Integer) state.get("outer_index");
                this.middle = (Integer) state.get("middle_index");
                this.inner = (Integer) state.get("inner_index");
            }
        }

        private static String toJson(java.util.Map<String, Object> m) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (var e : m.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(e.getKey()).append("\":").append(((Integer) e.getValue()).intValue());
            }
            return sb.append('}').toString();
        }

        private static java.util.Map<String, Object> fromJson(String json) {
            java.util.Map<String, Object> m = new java.util.LinkedHashMap<>();
            String body = json.trim();
            body = body.substring(1, body.length() - 1).trim();
            if (body.isEmpty()) return m;
            for (String pair : body.split(",")) {
                int colon = pair.indexOf(':');
                String key = pair.substring(0, colon).trim();
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                int val = Integer.parseInt(pair.substring(colon + 1).trim());
                m.put(key, val);
            }
            return m;
        }

        private static void expect(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }
        private static <T> void expectEquals(T actual, T expected, String msg) {
            expect(java.util.Objects.equals(actual, expected),
                    msg + " expected=" + expected + " got=" + actual);
        }

        private static <T> java.util.List<T> drain(ResumableIterator<T> it) {
            java.util.List<T> out = new java.util.ArrayList<>();
            while (it.hasNext()) out.add(it.next());
            return out;
        }

        // Build the canonical 3D example: [[[1,2],[3]],[[],[4,5,6]],[[7]]].
        private static java.util.List<java.util.List<java.util.List<Integer>>> sampleData() {
            return java.util.List.of(
                    java.util.List.of(java.util.List.of(1, 2), java.util.List.of(3)),
                    java.util.List.of(java.util.List.<Integer>of(), java.util.List.of(4, 5, 6)),
                    java.util.List.of(java.util.List.of(7)));
        }

        public static void main(String[] args) {
            // ---- Main usage example ---------------------------------------------
            {
                var data = sampleData();
                ResumableIterator<Integer> it = new Resumable3DIterator<>(data);
                expectEquals(it.next(), 1, "3d-main: next->1");
                expectEquals(it.next(), 2, "3d-main: next->2");
                expectEquals(it.next(), 3, "3d-main: next->3");

                java.util.Map<String, Object> state = it.getState(); // about to skip empty inner [] next

                expectEquals(it.next(), 4, "3d-main: next->4 (skips empty list)");

                it.setState(state);
                expectEquals(it.next(), 4, "3d-main: next->4 (resumes, skipping empty)");
                expectEquals(it.next(), 5, "3d-main: next->5");
                // Drain the rest: 6, 7, then exhausted.
                expectEquals(it.next(), 6, "3d-main: next->6");
                expectEquals(it.next(), 7, "3d-main: next->7");
                expect(!it.hasNext(), "3d-main: exhausted");
                boolean threw = false;
                try { it.next(); } catch (java.util.NoSuchElementException ex) { threw = true; }
                expect(threw, "3d-main: next() past end throws");

                // Serialization round-trip + FRESH iterator resume to 4,5.
                String json = toJson(state);
                expectEquals(fromJson(json), state, "3d-main: json round-trip equals");
                ResumableIterator<Integer> fresh = new Resumable3DIterator<>(data);
                fresh.setState(fromJson(json));
                expectEquals(fresh.next(), 4, "3d-main(fresh): next->4");
                expectEquals(fresh.next(), 5, "3d-main(fresh): next->5");
            }

            // ---- 3D edge cases ---------------------------------------------------
            // [[[]]] -> empty (one outer, one middle, empty inner).
            {
                java.util.List<java.util.List<java.util.List<Integer>>> data =
                        java.util.List.of(java.util.List.of(java.util.List.<Integer>of()));
                expect(!new Resumable3DIterator<>(data).hasNext(), "3d-edge[[[]]]: empty");
            }
            // [] -> empty (no outer at all).
            {
                java.util.List<java.util.List<java.util.List<Integer>>> data = java.util.List.of();
                expect(!new Resumable3DIterator<>(data).hasNext(), "3d-edge[]: empty");
            }
            // [[],[]] -> empty (two outers, each with no middle).
            {
                java.util.List<java.util.List<java.util.List<Integer>>> data = java.util.List.of(
                        java.util.List.<java.util.List<Integer>>of(), java.util.List.<java.util.List<Integer>>of());
                expect(!new Resumable3DIterator<>(data).hasNext(), "3d-edge[[],[]]: empty");
            }
            // [[[1],[],[2]]] -> [1,2] (empty middle inner skipped).
            {
                java.util.List<java.util.List<java.util.List<Integer>>> data = java.util.List.of(
                        java.util.List.of(java.util.List.of(1), java.util.List.<Integer>of(), java.util.List.of(2)));
                expectEquals(drain(new Resumable3DIterator<>(data)), java.util.List.of(1, 2),
                        "3d-edge[[[1],[],[2]]]: [1,2]");
            }
            // Boundary state [[[1]],[[2]]]: next->1, save (outer0,middle0,inner1),
            // next->2, restore, next==2.
            {
                java.util.List<java.util.List<java.util.List<Integer>>> data = java.util.List.of(
                        java.util.List.of(java.util.List.of(1)), java.util.List.of(java.util.List.of(2)));
                ResumableIterator<Integer> it = new Resumable3DIterator<>(data);
                expectEquals(it.next(), 1, "3d-boundary: next->1");
                java.util.Map<String, Object> state = it.getState();
                expectEquals(state, java.util.Map.of("outer_index", 0, "middle_index", 0, "inner_index", 1),
                        "3d-boundary: state (0,0,1)");
                expectEquals(it.next(), 2, "3d-boundary: next->2");
                it.setState(state);
                expectEquals(it.next(), 2, "3d-boundary: resume next==2");
            }

            // ---- Small input/expected table -------------------------------------
            {
                record Case(java.util.List<java.util.List<java.util.List<Integer>>> input,
                            java.util.List<Integer> expected) {}
                java.util.List<Case> cases = java.util.List.of(
                        new Case(sampleData(), java.util.List.of(1, 2, 3, 4, 5, 6, 7)),
                        new Case(java.util.List.of(java.util.List.of(java.util.List.<Integer>of())),
                                java.util.List.<Integer>of()),
                        new Case(java.util.List.of(java.util.List.of(java.util.List.of(1), java.util.List.<Integer>of(),
                                java.util.List.of(2))), java.util.List.of(1, 2)));
                for (Case c : cases) {
                    expectEquals(drain(new Resumable3DIterator<>(c.input())), c.expected(),
                            "3d-table: " + c.input());
                }
            }

            System.out.println("Part 4: all checks passed");
        }
    }
}
