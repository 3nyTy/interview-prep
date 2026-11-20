// ============================================================================
// Memory Allocator (OpenAI Staff-SWE interview question)
// ----------------------------------------------------------------------------
// Compile:  javac "A - Memory Allocator.java"
// Run all:  java MemoryAllocatorSolution
// Run one:  java MemoryAllocatorSolution part1
//           java MemoryAllocatorSolution part2
// ----------------------------------------------------------------------------
// The file name ("A - Memory Allocator.java") is NOT a valid Java identifier,
// so the top-level type is PACKAGE-PRIVATE (no `public` modifier) and is named
// MemoryAllocatorSolution. Target: Java 21.
//
// PART 1 — Basic implementation (first-fit, doubly-linked free list)
//   * Manages a contiguous address space [0, capacity) in integer units.
//   * Free blocks are kept in a doubly-linked list SORTED BY START ADDRESS so
//     that coalescing on free() is O(1) once the insertion point is found.
//   * allocate(size): first-fit — walk left-to-right, take the FIRST free block
//     whose size >= request. Exact fit => unlink the node; larger => shrink the
//     node from the front (start += size, size -= size). Shrinking keeps the
//     list sorted because the node's new start can never reach its successor.
//   * free(address, size): 4 coalescing cases — no-merge / merge-prev /
//     merge-next / merge-both.
//   * Complexity: allocate O(n), free O(n) over n free blocks; space O(m) for m
//     free blocks plus O(k) for k live allocations tracked for validation.
//
// PART 2 — Optimization (best-fit, dual-indexed, O(log n))
//   * Implements the "balanced BST with dual indexing" optimization (the
//     strategy glibc's ptmalloc2 uses with red-black trees).
//   * Two indexes kept in lockstep:
//       - byStart : TreeMap<start, size>  (source of truth; O(log n) neighbor
//                   lookup for coalescing via floorKey / ceilingKey)
//       - bySize  : TreeSet<Long> where each free block is encoded as
//                   ((long)size << 32) | (start & 0xFFFFFFFFL). Natural Long
//                   ordering sorts by size ASC, then start ASC => best-fit with
//                   a lowest-address tie-break, found via ceiling().
//   * Complexity: allocate O(log n), free O(log n); getLargestFreeBlock O(log n).
//   * See the "CHANGES FROM PART 1:" block inside Part2 for the full diff, and
//     the DISCUSSION block for the rest of the Part-2 analysis & follow-ups.
//
// SHARED SEMANTICS (both parts):
//   * Block [start, start+size); blocks A,B adjacent iff A.start+A.size==B.start.
//   * allocated map {address -> size} in BOTH parts to detect double-free and
//     wrong-size free.
//   * Exception contract (identical across both parts):
//       - IllegalArgumentException : non-positive capacity (ctor); non-positive
//         allocate size; non-positive free size; free address out of bounds;
//         free address+size > capacity; no allocation at address (double-free /
//         never-allocated); allocation exists but size mismatch (wrong-size).
//       - IllegalStateException    : insufficient contiguous memory in allocate.
//         (Chosen over java.lang.OutOfMemoryError so tests don't clash with a
//         real JVM OOM and so we never throw an Error for an expected case.)
//
// INDEPENDENCE CONTRACT: Part1 and Part2 are fully self-contained — each owns
// its FreeBlock / Allocator / check / expectThrows helpers with ZERO references
// to the other part. Copying either nested class into its own .java file
// (promote to top-level, drop `static`, rename to a valid type) compiles and
// runs unchanged. The only shared element is the java.util.* import below.
// ============================================================================

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

class MemoryAllocatorSolution {

    public static void main(String[] args) {
        String which = (args.length > 0) ? args[0].toLowerCase() : "";
        switch (which) {
            case "part1" -> Part1.main(new String[0]);
            case "part2" -> Part2.main(new String[0]);
            case "" -> {
                Part1.main(new String[0]);
                Part2.main(new String[0]);
            }
            default -> {
                System.out.println("Usage: java MemoryAllocatorSolution [part1|part2]");
                System.out.println("Unknown argument \"" + args[0] + "\"; running all parts.\n");
                Part1.main(new String[0]);
                Part2.main(new String[0]);
            }
        }
    }

    // ========================================================================
    // PART 1 — first-fit, doubly-linked free list sorted by address.
    // Self-contained: owns FreeBlock, Allocator, check, expectThrows.
    // ========================================================================
    static final class Part1 {

        /** A run of free memory [start, start+size). Nodes form a doubly-linked
         *  list kept sorted by {@code start}. */
        static final class FreeBlock {
            int start;
            int size;
            FreeBlock next;
            FreeBlock prev;

            FreeBlock(int start, int size) {
                this.start = start;
                this.size = size;
            }
        }

        /** First-fit allocator backed by a doubly-linked free list. */
        static final class Allocator {
            private final int capacity;
            // Head of the address-sorted free list (may be null when full).
            private FreeBlock head;
            // address -> size of every live allocation (for free() validation).
            private final Map<Integer, Integer> allocated = new HashMap<>();

            Allocator(int totalCapacity) {
                if (totalCapacity <= 0) {
                    throw new IllegalArgumentException(
                            "total capacity must be positive, got " + totalCapacity);
                }
                this.capacity = totalCapacity;
                this.head = new FreeBlock(0, totalCapacity); // all memory free
            }

            /** Allocate {@code size} contiguous units; returns the start address.
             *  First-fit. O(n) over the free list. */
            int allocate(int size) {
                if (size <= 0) {
                    throw new IllegalArgumentException(
                            "allocation size must be positive, got " + size);
                }
                for (FreeBlock cur = head; cur != null; cur = cur.next) {
                    if (cur.size >= size) {
                        int addr = cur.start;
                        if (cur.size == size) {
                            removeFreeBlock(cur);        // exact fit: drop node
                        } else {
                            cur.start += size;           // shrink from the front
                            cur.size -= size;            // (list stays sorted)
                        }
                        allocated.put(addr, size);
                        return addr;
                    }
                }
                throw new IllegalStateException(
                        "insufficient contiguous memory for size " + size
                                + " (free=" + getFreeMemory()
                                + ", largest=" + getLargestFreeBlock() + ")");
            }

            /** Free the block at {@code address} of exactly {@code size} units,
             *  merging with adjacent free neighbours. O(n). */
            void free(int address, int size) {
                // Validation order matters for predictable error messages.
                if (size <= 0) {
                    throw new IllegalArgumentException(
                            "free size must be positive, got " + size);
                }
                if (address < 0 || address >= capacity) {
                    throw new IllegalArgumentException(
                            "free address out of bounds: " + address
                                    + " (capacity=" + capacity + ")");
                }
                if (address + size > capacity) {
                    throw new IllegalArgumentException(
                            "free range [" + address + ", " + (address + size)
                                    + ") exceeds capacity " + capacity);
                }
                Integer live = allocated.get(address);
                if (live == null) {
                    throw new IllegalArgumentException(
                            "no allocation at address " + address
                                    + " (double-free or never allocated)");
                }
                if (live != size) {
                    throw new IllegalArgumentException(
                            "size mismatch freeing address " + address
                                    + ": allocated " + live + ", got " + size);
                }
                allocated.remove(address);

                int freedEnd = address + size;
                // Find prev = last node with start < address; cur = first node
                // with start >= address (i.e. the right neighbour candidate).
                FreeBlock prev = null;
                FreeBlock cur = head;
                while (cur != null && cur.start < address) {
                    prev = cur;
                    cur = cur.next;
                }
                boolean mergePrev = (prev != null && prev.start + prev.size == address);
                boolean mergeNext = (cur != null && freedEnd == cur.start);

                if (mergePrev && mergeNext) {                 // merge all three
                    prev.size += size + cur.size;
                    removeFreeBlock(cur);
                } else if (mergePrev) {                       // extend prev right
                    prev.size += size;
                } else if (mergeNext) {                       // extend cur left
                    cur.start = address;
                    cur.size += size;
                } else {                                      // no merge: insert
                    insertFreeBlockAfter(prev, new FreeBlock(address, size));
                }
            }

            /** Unlink {@code b} from the doubly-linked list. */
            private void removeFreeBlock(FreeBlock b) {
                if (b.prev != null) {
                    b.prev.next = b.next;
                } else {
                    head = b.next;                 // removing the head
                }
                if (b.next != null) {
                    b.next.prev = b.prev;
                }
                b.next = null;
                b.prev = null;
            }

            /** Insert {@code nb} immediately after {@code prev} (head-insert when
             *  {@code prev} is null). */
            private void insertFreeBlockAfter(FreeBlock prev, FreeBlock nb) {
                if (prev == null) {
                    nb.next = head;
                    nb.prev = null;
                    if (head != null) {
                        head.prev = nb;
                    }
                    head = nb;
                } else {
                    nb.next = prev.next;
                    nb.prev = prev;
                    if (prev.next != null) {
                        prev.next.prev = nb;
                    }
                    prev.next = nb;
                }
            }

            /** Total free units (sum over free list). O(n). */
            int getFreeMemory() {
                int total = 0;
                for (FreeBlock cur = head; cur != null; cur = cur.next) {
                    total += cur.size;
                }
                return total;
            }

            /** Size of the largest single free block, or 0 if none. O(n). */
            int getLargestFreeBlock() {
                int largest = 0;
                for (FreeBlock cur = head; cur != null; cur = cur.next) {
                    if (cur.size > largest) {
                        largest = cur.size;
                    }
                }
                return largest;
            }

            /** (start, size) pairs of free blocks in address order — for tests. */
            List<int[]> snapshot() {
                List<int[]> out = new ArrayList<>();
                for (FreeBlock cur = head; cur != null; cur = cur.next) {
                    out.add(new int[]{cur.start, cur.size});
                }
                return out;
            }

            /** Number of free blocks (fragmentation indicator). */
            int freeBlockCount() {
                int n = 0;
                for (FreeBlock cur = head; cur != null; cur = cur.next) {
                    n++;
                }
                return n;
            }
        }

        // ---- tiny test harness (independent of -ea / assert keyword) --------
        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError("[Part1] " + msg);
            }
        }

        private static void expectThrows(Class<? extends Throwable> type,
                                         Runnable r, String msg) {
            try {
                r.run();
            } catch (Throwable t) {
                if (type.isInstance(t)) {
                    return; // expected
                }
                throw new AssertionError("[Part1] " + msg + " — expected "
                        + type.getSimpleName() + " but got "
                        + t.getClass().getSimpleName() + ": " + t.getMessage());
            }
            throw new AssertionError("[Part1] " + msg + " — expected "
                    + type.getSimpleName() + " but nothing was thrown");
        }

        /** Assert the free-list snapshot equals the expected (start,size) pairs. */
        private static void checkFreeList(Allocator a, int[][] expected, String label) {
            List<int[]> got = a.snapshot();
            check(got.size() == expected.length,
                    label + ": expected " + expected.length + " free blocks, got "
                            + got.size());
            for (int i = 0; i < expected.length; i++) {
                check(got.get(i)[0] == expected[i][0] && got.get(i)[1] == expected[i][1],
                        label + ": block " + i + " expected ("
                                + expected[i][0] + "," + expected[i][1] + ") got ("
                                + got.get(i)[0] + "," + got.get(i)[1] + ")");
            }
        }

        public static void main(String[] args) {
            // ---- Worked example from the prompt (capacity 100) --------------
            Allocator a = new Allocator(100);

            int addr1 = a.allocate(20);
            check(addr1 == 0, "step1 addr expected 0, got " + addr1);
            checkFreeList(a, new int[][]{{20, 80}}, "step1");
            check(a.getFreeMemory() == 80, "step1 free expected 80, got " + a.getFreeMemory());

            int addr2 = a.allocate(30);
            check(addr2 == 20, "step2 addr expected 20, got " + addr2);
            checkFreeList(a, new int[][]{{50, 50}}, "step2");
            check(a.getFreeMemory() == 50, "step2 free expected 50, got " + a.getFreeMemory());

            int addr3 = a.allocate(40);
            check(addr3 == 50, "step3 addr expected 50, got " + addr3);
            checkFreeList(a, new int[][]{{90, 10}}, "step3");
            check(a.getFreeMemory() == 10, "step3 free expected 10, got " + a.getFreeMemory());

            a.free(20, 30);
            checkFreeList(a, new int[][]{{20, 30}, {90, 10}}, "step4");
            check(a.getFreeMemory() == 40, "step4 free expected 40, got " + a.getFreeMemory());

            int addr4 = a.allocate(25);
            check(addr4 == 20, "step5 addr expected 20, got " + addr4);
            checkFreeList(a, new int[][]{{45, 5}, {90, 10}}, "step5");
            check(a.getFreeMemory() == 15, "step5 free expected 15, got " + a.getFreeMemory());

            a.free(0, 20);
            checkFreeList(a, new int[][]{{0, 20}, {45, 5}, {90, 10}}, "step6");
            check(a.getFreeMemory() == 35, "step6 free expected 35, got " + a.getFreeMemory());

            // free(20,25) merges with BOTH neighbours: (0,20)+(20,25)+(45,5) => (0,50)
            a.free(20, 25);
            checkFreeList(a, new int[][]{{0, 50}, {90, 10}}, "step7 (merge-both)");
            check(a.getFreeMemory() == 60, "step7 free expected 60, got " + a.getFreeMemory());

            // ---- Invalid-operation contract ---------------------------------
            expectThrows(IllegalArgumentException.class,
                    () -> new Allocator(0), "ctor capacity 0");
            expectThrows(IllegalArgumentException.class,
                    () -> new Allocator(-5), "ctor capacity -5");

            Allocator fresh = new Allocator(100);
            expectThrows(IllegalArgumentException.class,
                    () -> fresh.allocate(0), "allocate(0)");
            expectThrows(IllegalArgumentException.class,
                    () -> fresh.allocate(-1), "allocate(-1)");
            expectThrows(IllegalStateException.class,
                    () -> fresh.allocate(101), "allocate(101) > capacity");

            // Fragmentation exhaustion: in the worked-example allocator we have
            // free blocks (0,50) and (90,10): total 60 but largest only 50, so a
            // contiguous request of 51 MUST fail even though 60 >= 51.
            check(a.getFreeMemory() == 60, "pre-frag free expected 60");
            check(a.getLargestFreeBlock() == 50, "pre-frag largest expected 50");
            expectThrows(IllegalStateException.class,
                    () -> a.allocate(51), "allocate(51) with fragmented 50+10");

            // double-free
            Allocator df = new Allocator(100);
            int d = df.allocate(40);
            check(d == 0, "double-free setup addr expected 0, got " + d);
            df.free(0, 40);
            expectThrows(IllegalArgumentException.class,
                    () -> df.free(0, 40), "double free(0,40)");

            // wrong-size free
            Allocator ws = new Allocator(100);
            ws.allocate(40); // -> addr 0
            expectThrows(IllegalArgumentException.class,
                    () -> ws.free(0, 30), "wrong-size free(0,30) of a 40-block");

            // out-of-bounds frees
            Allocator ob = new Allocator(100);
            ob.allocate(40);
            expectThrows(IllegalArgumentException.class,
                    () -> ob.free(-1, 5), "free(-1,5)");
            expectThrows(IllegalArgumentException.class,
                    () -> ob.free(100, 5), "free(100,5) address == capacity");
            expectThrows(IllegalArgumentException.class,
                    () -> ob.free(95, 10), "free(95,10) range 105 > capacity");

            // never-allocated
            Allocator na = new Allocator(100);
            expectThrows(IllegalArgumentException.class,
                    () -> na.free(10, 5), "free(10,5) never allocated");

            System.out.println("Part1 (first-fit doubly-linked list): all checks passed.");
        }
    }

    // ========================================================================
    // PART 2 — best-fit, dual-indexed (TreeMap byStart + TreeSet bySize), O(log n).
    // Self-contained: owns Allocator, check, expectThrows (NO shared helpers).
    // ========================================================================
    //
    // ------------------------------------------------------------------------
    // CHANGES FROM PART 1:
    //   1. Policy: first-fit  ->  best-fit (smallest free block that fits, with
    //      a lowest-address tie-break).
    //   2. Data structure: a single doubly-linked list  ->  TWO indexes kept in
    //      lockstep — TreeMap<start,size> (byStart) + TreeSet<Long> (bySize,
    //      where each block is encoded as (size<<32)|start).
    //   3. allocate: linear O(n) scan  ->  bySize.ceiling(encode(size,0)),
    //      O(log n). ceiling() only returns a block whose OWN size >= request,
    //      so fragmentation exhaustion (e.g. 50+10 free, request 51) still
    //      correctly throws IllegalStateException — it never sums fragments.
    //   4. free coalescing: prev/cur pointer walk O(n)  ->  byStart.floorKey /
    //      ceilingKey lookups O(log n). The freed start is ABSENT from byStart
    //      (it was an allocation), so floorKey = strict-left neighbour and
    //      ceilingKey = strict-right neighbour. Neighbour sizes are captured
    //      BEFORE removal because removeFree() deletes the map entries.
    //   5. Block mutation: shrink-in-place / pointer-splice  ->  every change
    //      routes through addFree()/removeFree() so the two indexes never drift.
    //   6. getLargestFreeBlock: O(n) scan  ->  O(log n) via bySize.last().
    //      NOT O(1): TreeSet.last() -> TreeMap.lastKey() walks `right` pointers
    //      from the root to the rightmost node, i.e. O(height) = O(log n); the
    //      JDK does not cache the extremes. True O(1) would need an explicitly
    //      cached maximum, and maintaining that across removals is itself
    //      O(log n), so it buys nothing over just calling last().
    //   7. Added a dual-index consistency invariant (checkInvariants()).
    //
    // ------------------------------------------------------------------------
    // DISCUSSION (Part 2 — complexity, drawbacks, optimizations, follow-ups):
    //
    //   Complexity of Part 1: allocate O(n), free O(n) over n free blocks;
    //     getLargestFreeBlock O(n); space O(m) free blocks + O(k) live allocs.
    //   Complexity of Part 2: allocate O(log n), free O(log n),
    //     getLargestFreeBlock O(log n); space O(m) + O(k) (two index entries/block).
    //
    //   Drawbacks of the linear free-list (Part 1): external fragmentation
    //     (free memory exists but not contiguous), O(n) linear search per op,
    //     and no compaction (we can't relocate live blocks to coalesce holes).
    //
    //   Optimization strategies (menu):
    //     * Best-fit / worst-fit policies (best-fit reduces wasted tail; can
    //       increase tiny unusable slivers).
    //     * Segregated / binned free lists: one free list per size class — near
    //       O(1) for common sizes (jemalloc / tcmalloc style).
    //     * Buddy system: split/merge power-of-two blocks; O(log n) with low
    //       external fragmentation but internal fragmentation up to ~2x.
    //     * Balanced BST with dual indexing (THIS PART): O(log n) for both ops;
    //       glibc ptmalloc2 uses red-black trees of free chunks.
    //     * Bitmap + index for fixed-size slabs: O(1) alloc/free of fixed cells.
    //
    //   Achieving O(1) SPACE overhead (no separate node objects): implicit free
    //     lists with IN-BAND headers, plus boundary tags / footers (Knuth) so a
    //     freed block can find and coalesce with its physical neighbours in O(1)
    //     by reading the adjacent headers — metadata lives inside the managed
    //     region itself rather than in side maps/lists.
    //
    //   Follow-up questions:
    //     * 8-byte alignment: round requested size up to a multiple of 8 and
    //       keep block starts aligned (here addresses are abstract units; in a
    //       real allocator align both start and size).
    //     * Tracking allocated blocks to detect invalid free(): we keep an
    //       {address -> size} map; this catches double-free and wrong-size free.
    //     * realloc(p, n): try to grow in place by absorbing an adjacent free
    //       block; else allocate(n), copy, free(old).
    //     * Thread-safety: a global lock is simplest; better is per-size-class
    //       locks or per-thread arenas/caches (tcmalloc/jemalloc) to cut
    //       contention; lock-free free lists for fixed sizes.
    //     * Hardware vs abstract data structure: a real allocator manages
    //       virtual address space backed by the OS (sbrk/mmap), must respect
    //       page granularity, cache-line/alignment effects, and TLB behaviour;
    //       this exercise models only the abstract bookkeeping over a flat
    //       integer address range.
    //
    //   long-encode assumption: 0 <= start, size <= capacity and capacity fits
    //     in 31 bits, so size and start each pack into the high/low 32 bits of a
    //     non-negative long and natural Long ordering = (size asc, start asc).
    // ------------------------------------------------------------------------
    static final class Part2 {

        /** Best-fit allocator backed by two lock-step indexes. */
        static final class Allocator {
            private final int capacity;
            // start -> size : source of truth, ordered by address for coalescing.
            private final TreeMap<Integer, Integer> byStart = new TreeMap<>();
            // (size<<32)|start encoded longs : ordered by size then start.
            private final TreeSet<Long> bySize = new TreeSet<>();
            // address -> size of every live allocation (for free() validation).
            private final Map<Integer, Integer> allocated = new HashMap<>();

            Allocator(int totalCapacity) {
                if (totalCapacity <= 0) {
                    throw new IllegalArgumentException(
                            "total capacity must be positive, got " + totalCapacity);
                }
                this.capacity = totalCapacity;
                addFree(0, totalCapacity); // all memory free
            }

            // ---- encode/decode helpers for the bySize index -----------------
            private static long encode(int size, int start) {
                return ((long) size << 32) | (start & 0xFFFFFFFFL);
            }

            private static int decodeSize(long key) {
                return (int) (key >>> 32);
            }

            private static int decodeStart(long key) {
                return (int) key;
            }

            // ---- the ONLY two mutators of free state; keep indexes in sync --
            private void addFree(int start, int size) {
                byStart.put(start, size);
                bySize.add(encode(size, start));
            }

            private void removeFree(int start, int size) {
                byStart.remove(start);
                bySize.remove(encode(size, start));
            }

            /** Allocate {@code size} contiguous units; returns the start address.
             *  Best-fit via bySize.ceiling. O(log n). */
            int allocate(int size) {
                if (size <= 0) {
                    throw new IllegalArgumentException(
                            "allocation size must be positive, got " + size);
                }
                // Smallest free block whose own size >= request (start tie -> 0
                // is the minimum start, so ceiling lands on the best fit).
                Long key = bySize.ceiling(encode(size, 0));
                if (key == null) {
                    throw new IllegalStateException(
                            "insufficient contiguous memory for size " + size
                                    + " (free=" + getFreeMemory()
                                    + ", largest=" + getLargestFreeBlock() + ")");
                }
                int bStart = decodeStart(key);
                int bSize = decodeSize(key);
                int addr = bStart;

                removeFree(bStart, bSize);
                if (bSize > size) {
                    addFree(bStart + size, bSize - size); // remainder stays free
                }
                allocated.put(addr, size);
                return addr;
            }

            /** Free the block at {@code address} of exactly {@code size} units,
             *  merging with adjacent free neighbours. O(log n). */
            void free(int address, int size) {
                if (size <= 0) {
                    throw new IllegalArgumentException(
                            "free size must be positive, got " + size);
                }
                if (address < 0 || address >= capacity) {
                    throw new IllegalArgumentException(
                            "free address out of bounds: " + address
                                    + " (capacity=" + capacity + ")");
                }
                if (address + size > capacity) {
                    throw new IllegalArgumentException(
                            "free range [" + address + ", " + (address + size)
                                    + ") exceeds capacity " + capacity);
                }
                Integer live = allocated.get(address);
                if (live == null) {
                    throw new IllegalArgumentException(
                            "no allocation at address " + address
                                    + " (double-free or never allocated)");
                }
                if (live != size) {
                    throw new IllegalArgumentException(
                            "size mismatch freeing address " + address
                                    + ": allocated " + live + ", got " + size);
                }
                allocated.remove(address);

                int start = address;
                int end = address + size;

                // address is absent from byStart (it was allocated), so:
                //   floorKey  -> greatest free start strictly < start (left nbr)
                //   ceilingKey-> smallest free start strictly > start (right nbr)
                Integer p = byStart.floorKey(start);
                boolean mergePrev = (p != null && p + byStart.get(p) == start);
                Integer nx = byStart.ceilingKey(start);
                boolean mergeNext = (nx != null && end == nx);

                if (mergePrev && mergeNext) {                 // merge all three
                    int pSize = byStart.get(p);
                    int nSize = byStart.get(nx);
                    removeFree(p, pSize);
                    removeFree(nx, nSize);
                    addFree(p, pSize + size + nSize);
                } else if (mergePrev) {                       // extend prev right
                    int pSize = byStart.get(p);
                    removeFree(p, pSize);
                    addFree(p, pSize + size);
                } else if (mergeNext) {                       // extend right left
                    int nSize = byStart.get(nx);
                    removeFree(nx, nSize);
                    addFree(start, size + nSize);
                } else {                                      // no merge: insert
                    addFree(start, size);
                }
            }

            /** Total free units. O(n) (sum of values). */
            int getFreeMemory() {
                int total = 0;
                for (int v : byStart.values()) {
                    total += v;
                }
                return total;
            }

            /** Size of the largest free block, or 0 if none. O(log n) — last()
             *  walks to the rightmost node; TreeMap does not cache extremes. */
            int getLargestFreeBlock() {
                return bySize.isEmpty() ? 0 : decodeSize(bySize.last());
            }

            /** (start, size) pairs of free blocks in address order — for tests. */
            List<int[]> snapshot() {
                List<int[]> out = new ArrayList<>();
                for (Map.Entry<Integer, Integer> e : byStart.entrySet()) {
                    out.add(new int[]{e.getKey(), e.getValue()});
                }
                return out;
            }

            int freeBlockCount() {
                return byStart.size();
            }

            /** Dual-index consistency + fully-coalesced invariant (test-only). */
            void checkInvariants() {
                if (byStart.size() != bySize.size()) {
                    throw new AssertionError("[Part2] index size mismatch: byStart="
                            + byStart.size() + " bySize=" + bySize.size());
                }
                Integer prevStart = null;
                int prevSize = 0;
                for (Map.Entry<Integer, Integer> e : byStart.entrySet()) {
                    int s = e.getKey();
                    int z = e.getValue();
                    // every byStart entry must exist in bySize too
                    if (!bySize.contains(encode(z, s))) {
                        throw new AssertionError(
                                "[Part2] bySize missing block (" + s + "," + z + ")");
                    }
                    // no two adjacent free blocks left un-coalesced
                    if (prevStart != null && prevStart + prevSize >= s) {
                        throw new AssertionError(
                                "[Part2] un-coalesced/overlapping free blocks at "
                                        + prevStart + "(+" + prevSize + ") and " + s);
                    }
                    prevStart = s;
                    prevSize = z;
                }
            }
        }

        // ---- tiny test harness (own copy; NOT shared with Part1) ------------
        private static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError("[Part2] " + msg);
            }
        }

        private static void expectThrows(Class<? extends Throwable> type,
                                         Runnable r, String msg) {
            try {
                r.run();
            } catch (Throwable t) {
                if (type.isInstance(t)) {
                    return; // expected
                }
                throw new AssertionError("[Part2] " + msg + " — expected "
                        + type.getSimpleName() + " but got "
                        + t.getClass().getSimpleName() + ": " + t.getMessage());
            }
            throw new AssertionError("[Part2] " + msg + " — expected "
                    + type.getSimpleName() + " but nothing was thrown");
        }

        private static void checkFreeList(Allocator a, int[][] expected, String label) {
            List<int[]> got = a.snapshot();
            check(got.size() == expected.length,
                    label + ": expected " + expected.length + " free blocks, got "
                            + got.size());
            for (int i = 0; i < expected.length; i++) {
                check(got.get(i)[0] == expected[i][0] && got.get(i)[1] == expected[i][1],
                        label + ": block " + i + " expected ("
                                + expected[i][0] + "," + expected[i][1] + ") got ("
                                + got.get(i)[0] + "," + got.get(i)[1] + ")");
            }
        }

        public static void main(String[] args) {
            // ---- Worked example (capacity 100) ------------------------------
            // NOTE: best-fit yields the SAME addresses as first-fit here because
            // at every allocate step exactly one free block is large enough, so
            // "first that fits" == "smallest that fits". In particular at step 5
            // the candidates are (20,30) and (90,10) and only (20,30) >= 25. The
            // dedicated DIVERGENCE test below is what actually proves best-fit.
            Allocator a = new Allocator(100);
            a.checkInvariants();

            int addr1 = a.allocate(20);
            a.checkInvariants();
            check(addr1 == 0, "step1 addr expected 0, got " + addr1);
            checkFreeList(a, new int[][]{{20, 80}}, "step1");
            check(a.getFreeMemory() == 80, "step1 free expected 80, got " + a.getFreeMemory());

            int addr2 = a.allocate(30);
            a.checkInvariants();
            check(addr2 == 20, "step2 addr expected 20, got " + addr2);
            checkFreeList(a, new int[][]{{50, 50}}, "step2");
            check(a.getFreeMemory() == 50, "step2 free expected 50, got " + a.getFreeMemory());

            int addr3 = a.allocate(40);
            a.checkInvariants();
            check(addr3 == 50, "step3 addr expected 50, got " + addr3);
            checkFreeList(a, new int[][]{{90, 10}}, "step3");
            check(a.getFreeMemory() == 10, "step3 free expected 10, got " + a.getFreeMemory());

            a.free(20, 30);
            a.checkInvariants();
            checkFreeList(a, new int[][]{{20, 30}, {90, 10}}, "step4");
            check(a.getFreeMemory() == 40, "step4 free expected 40, got " + a.getFreeMemory());

            int addr4 = a.allocate(25);
            a.checkInvariants();
            check(addr4 == 20, "step5 addr expected 20, got " + addr4);
            checkFreeList(a, new int[][]{{45, 5}, {90, 10}}, "step5");
            check(a.getFreeMemory() == 15, "step5 free expected 15, got " + a.getFreeMemory());

            a.free(0, 20);
            a.checkInvariants();
            checkFreeList(a, new int[][]{{0, 20}, {45, 5}, {90, 10}}, "step6");
            check(a.getFreeMemory() == 35, "step6 free expected 35, got " + a.getFreeMemory());

            a.free(20, 25);
            a.checkInvariants();
            checkFreeList(a, new int[][]{{0, 50}, {90, 10}}, "step7 (merge-both)");
            check(a.getFreeMemory() == 60, "step7 free expected 60, got " + a.getFreeMemory());

            // ---- DIVERGENCE test: proves best-fit (load-bearing) ------------
            // Fresh allocator. Carve FOUR live blocks spanning the whole space,
            // then free two NON-ADJACENT ones of different sizes (separated by
            // live blocks so they can't coalesce), leaving holes:
            //     [free 30 @0][live][free 20 @40][live ...]
            // Then allocate(15) fits in BOTH holes. Best-fit must pick the
            // SMALLER hole (size-20 @ addr 40); first-fit would pick the
            // lower-address hole (size-30 @ addr 0). This asymmetry is the proof.
            Allocator d = new Allocator(100);
            int d1 = d.allocate(30);            // [0,30)  live
            d.checkInvariants();
            check(d1 == 0, "D1 addr expected 0, got " + d1);
            checkFreeList(d, new int[][]{{30, 70}}, "D1");

            int d2 = d.allocate(10);            // [30,40) live (separator)
            d.checkInvariants();
            check(d2 == 30, "D2 addr expected 30, got " + d2);
            checkFreeList(d, new int[][]{{40, 60}}, "D2");

            int d3 = d.allocate(20);            // [40,60) live
            d.checkInvariants();
            check(d3 == 40, "D3 addr expected 40, got " + d3);
            checkFreeList(d, new int[][]{{60, 40}}, "D3");

            int d4 = d.allocate(40);            // [60,100) live (consumes tail)
            d.checkInvariants();
            check(d4 == 60, "D4 addr expected 60, got " + d4);
            checkFreeList(d, new int[][]{}, "D4 (fully allocated)");

            d.free(0, 30);                      // hole A: (0,30)
            d.checkInvariants();
            checkFreeList(d, new int[][]{{0, 30}}, "D5");

            d.free(40, 20);                     // hole B: (40,20), non-adjacent
            d.checkInvariants();
            checkFreeList(d, new int[][]{{0, 30}, {40, 20}}, "D6");
            check(d.getLargestFreeBlock() == 30, "D6 largest expected 30, got " + d.getLargestFreeBlock());

            // Free holes now sized 30 (@0) and 20 (@40). allocate(15) fits BOTH;
            // best-fit picks the SMALLEST that fits => size-20 hole at addr 40.
            int d7 = d.allocate(15);
            d.checkInvariants();
            check(d7 == 40, "D7 BEST-FIT addr expected 40 (first-fit would give 0), got " + d7);
            checkFreeList(d, new int[][]{{0, 30}, {55, 5}}, "D7");

            // ---- Invalid-operation contract (strategy-independent) ----------
            expectThrows(IllegalArgumentException.class,
                    () -> new Allocator(0), "ctor capacity 0");
            expectThrows(IllegalArgumentException.class,
                    () -> new Allocator(-5), "ctor capacity -5");

            Allocator fresh = new Allocator(100);
            expectThrows(IllegalArgumentException.class,
                    () -> fresh.allocate(0), "allocate(0)");
            expectThrows(IllegalArgumentException.class,
                    () -> fresh.allocate(-1), "allocate(-1)");
            expectThrows(IllegalStateException.class,
                    () -> fresh.allocate(101), "allocate(101) > capacity");

            // Fragmentation exhaustion: the worked-example allocator has free
            // (0,50) and (90,10): total 60, largest 50 — request 51 must fail
            // because ceiling() never sums fragments.
            check(a.getFreeMemory() == 60, "pre-frag free expected 60");
            check(a.getLargestFreeBlock() == 50, "pre-frag largest expected 50");
            expectThrows(IllegalStateException.class,
                    () -> a.allocate(51), "allocate(51) with fragmented 50+10");

            // double-free
            Allocator df = new Allocator(100);
            int dd = df.allocate(40);
            check(dd == 0, "double-free setup addr expected 0, got " + dd);
            df.free(0, 40);
            expectThrows(IllegalArgumentException.class,
                    () -> df.free(0, 40), "double free(0,40)");

            // wrong-size free
            Allocator ws = new Allocator(100);
            ws.allocate(40);
            expectThrows(IllegalArgumentException.class,
                    () -> ws.free(0, 30), "wrong-size free(0,30) of a 40-block");

            // out-of-bounds frees
            Allocator ob = new Allocator(100);
            ob.allocate(40);
            expectThrows(IllegalArgumentException.class,
                    () -> ob.free(-1, 5), "free(-1,5)");
            expectThrows(IllegalArgumentException.class,
                    () -> ob.free(100, 5), "free(100,5) address == capacity");
            expectThrows(IllegalArgumentException.class,
                    () -> ob.free(95, 10), "free(95,10) range 105 > capacity");

            // never-allocated
            Allocator na = new Allocator(100);
            expectThrows(IllegalArgumentException.class,
                    () -> na.free(10, 5), "free(10,5) never allocated");

            System.out.println("Part2 (best-fit dual-indexed O(log n)): all checks passed.");
        }
    }
}
