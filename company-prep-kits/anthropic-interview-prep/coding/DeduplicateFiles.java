/*
 * ============================================================================
 * Anthropic — Staff Software Engineer — Coding Round
 * Question: "Deduplicate Files"  (see deduplicate-files-prompt.txt)
 *
 * Find all groups of byte-identical files under a root directory.
 * Returns List<List<String>>; each inner list has >= 2 paths; singletons are
 * excluded. Recurses into nested directories.
 *
 * This single file contains all THREE parts the interview walks through:
 *   Part 1  DeduplicateBasic     — hash every file fully, group by hash.
 *   Part 2  DeduplicateOptimized — 3-stage filter (size -> 1KB partial hash ->
 *                                  full hash).  *** the expected answer ***
 *   Part 3  DeduplicateStreaming — lock-step byte-exact comparison that
 *                                  short-circuits the instant two streams
 *                                  diverge (the "huge / all-identical" follow-up).
 *
 * Cross-cutting edge cases handled in every part:
 *   - nested directories      : Files.walkFileTree recurses automatically
 *   - symbolic links          : walkFileTree does NOT follow them by default,
 *                               and attrs.isRegularFile() filters them out
 *   - unreadable / no-perms    : per-file try/catch + visitFileFailed -> the
 *                               walk never crashes, the bad file is skipped
 *   - empty files             : all 0-byte files share size 0 and an identical
 *                               hash, so they form one group
 *
 * `main` builds a temp fixture (two dup pairs, a singleton, an empty-file pair,
 * a same-size-but-diverging-content trap, a skipped symlink) and asserts that
 * all three implementations return the same correct answer.
 * Verified on: OpenJDK Corretto 21.
 *
 *   Compile : javac DeduplicateFiles.java
 *   Run self-test : java DeduplicateFiles
 *   Run on a dir   : java DeduplicateFiles /path/to/root
 * ============================================================================
 */

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeduplicateFiles {

    // =========================================================================
    // Shared hashing / utility helpers (used by all parts)
    // =========================================================================

    /** SHA-256: collision probability ~2^-128, never observed; the safe default. */
    private static MessageDigest newDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("SHA-256 is guaranteed to exist on every JVM", e);
        }
    }

    /** Full streaming SHA-256 in 64 KB chunks — constant memory, never loads whole file. */
    private static String fullHash(Path file) throws IOException {
        MessageDigest md = newDigest();
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file))) {
            byte[] buf = new byte[1 << 16]; // 64 KB
            int read;
            while ((read = in.read(buf)) != -1) {
                md.update(buf, 0, read);
            }
        }
        return HexFormat.of().formatHex(md.digest());
    }

    /** Hash only the first `chunkSize` bytes — the cheap mid-tier filter (Part 2). */
    private static String partialHash(Path file, int chunkSize) throws IOException {
        MessageDigest md = newDigest();
        try (InputStream in = Files.newInputStream(file)) {
            md.update(in.readNBytes(chunkSize)); // up to chunkSize; fewer if file is smaller
        }
        return HexFormat.of().formatHex(md.digest());
    }

    private static List<String> toStringList(List<Path> paths) {
        List<String> out = new ArrayList<>(paths.size());
        for (Path p : paths) out.add(p.toString());
        return out;
    }

    /** Walk the tree and bucket every REGULAR file by its size (Stage 1, shared by Parts 2 & 3). */
    private static Map<Long, List<Path>> groupBySize(String rootPath) throws IOException {
        Map<Long, List<Path>> bySize = new HashMap<>();
        Files.walkFileTree(Paths.get(rootPath), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (attrs.isRegularFile()) { // symlinks are not regular -> skipped
                    bySize.computeIfAbsent(attrs.size(), k -> new ArrayList<>()).add(file);
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE; // unreadable dir/file -> skip, don't crash the walk
            }
        });
        return bySize;
    }

    // =========================================================================
    // PART 1 — Basic: full-hash every file, group by hash.
    //          Correct but O(total bytes): reads every byte of every file.
    // =========================================================================
    static final class DeduplicateBasic {
        static List<List<String>> findDuplicateFiles(String rootPath) throws IOException {
            Map<String, List<String>> byHash = new HashMap<>(); // full hash -> paths

            Files.walkFileTree(Paths.get(rootPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile()) {
                        try {
                            byHash.computeIfAbsent(fullHash(file), k -> new ArrayList<>())
                                  .add(file.toString());
                        } catch (IOException e) {
                            // unreadable (permissions) -> skip this file
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });

            List<List<String>> result = new ArrayList<>();
            for (List<String> group : byHash.values()) {
                if (group.size() > 1) result.add(group); // drop singletons
            }
            return result;
        }
    }

    // =========================================================================
    // PART 2 — Optimized 3-stage filter  *** the expected interview answer ***
    //   Stage 1: bucket by size       (metadata only, zero content I/O)
    //   Stage 2: hash first 1024 bytes (cheap mid-tier; only within size buckets >=2)
    //   Stage 3: full hash             (only when size AND prefix already match)
    // Best case O(#files) when sizes are unique; worst case O(total bytes).
    // =========================================================================
    static final class DeduplicateOptimized {
        private static final int PARTIAL_SIZE = 1024;

        static List<List<String>> findDuplicateFiles(String rootPath) throws IOException {
            Map<Long, List<Path>> bySize = groupBySize(rootPath);          // STAGE 1
            List<List<String>> result = new ArrayList<>();

            for (Map.Entry<Long, List<Path>> sizeEntry : bySize.entrySet()) {
                List<Path> sameSize = sizeEntry.getValue();
                if (sameSize.size() < 2) continue;        // unique size -> not a duplicate
                long size = sizeEntry.getKey();

                Map<String, List<Path>> byPartial = new HashMap<>();        // STAGE 2
                for (Path f : sameSize) {
                    try {
                        byPartial.computeIfAbsent(partialHash(f, PARTIAL_SIZE), k -> new ArrayList<>())
                                 .add(f);
                    } catch (IOException e) { /* unreadable -> skip */ }
                }

                for (List<Path> partialGroup : byPartial.values()) {
                    if (partialGroup.size() < 2) continue; // unique prefix -> not a duplicate

                    // If the file fits inside the partial window, the partial hash already
                    // hashed the WHOLE file -> a Stage-3 re-read would be wasted I/O.
                    // (Empty files, size 0, also resolve here.)
                    if (size <= PARTIAL_SIZE) {
                        result.add(toStringList(partialGroup));
                        continue;
                    }

                    Map<String, List<Path>> byFull = new HashMap<>();       // STAGE 3
                    for (Path f : partialGroup) {
                        try {
                            byFull.computeIfAbsent(fullHash(f), k -> new ArrayList<>()).add(f);
                        } catch (IOException e) { /* unreadable -> skip */ }
                    }
                    for (List<Path> fullGroup : byFull.values()) {
                        if (fullGroup.size() > 1) result.add(toStringList(fullGroup));
                    }
                }
            }
            return result;
        }
    }

    // =========================================================================
    // PART 3 — Streaming lock-step byte-exact comparison.
    // Follow-up: "all files huge and identical-sized" defeats Part 2 (every file
    // gets fully hashed). Here we read all candidates in lock-step, one chunk per
    // round, re-partitioning by the chunk's exact bytes, and STOP reading a file
    // the moment its partition drops to size 1 (proven non-duplicate). Byte-exact
    // => zero hash-collision risk. Cost: one open file handle per candidate.
    // =========================================================================
    static final class DeduplicateStreaming {
        private static final int CHUNK = 1 << 20; // 1 MB lock-step read

        static List<List<String>> findDuplicateFiles(String rootPath) throws IOException {
            Map<Long, List<Path>> bySize = groupBySize(rootPath); // STAGE 1 (unchanged)
            List<List<String>> result = new ArrayList<>();
            for (List<Path> sameSize : bySize.values()) {
                if (sameSize.size() < 2) continue;
                result.addAll(groupBucketStreaming(sameSize, CHUNK)); // replaces Stage 2+3
            }
            return result;
        }

        /** Compare equal-size candidates byte-exactly via lock-step chunked reads. */
        private static List<List<String>> groupBucketStreaming(List<Path> candidates, int chunkSize)
                throws IOException {
            List<List<String>> result = new ArrayList<>();
            List<InputStream> allStreams = new ArrayList<>(); // for guaranteed cleanup
            try {
                Partition initial = new Partition();
                for (Path p : candidates) {
                    InputStream in = new BufferedInputStream(Files.newInputStream(p), chunkSize);
                    allStreams.add(in);
                    initial.add(p, in);
                }

                List<Partition> active = new ArrayList<>();
                active.add(initial);

                while (!active.isEmpty()) {
                    List<Partition> next = new ArrayList<>();
                    for (Partition part : active) {
                        if (part.size() < 2) continue; // singleton: stop reading (short-circuit)

                        // Read one chunk from each member; re-bucket by exact chunk bytes.
                        Map<ByteArrayKey, Partition> sub = new LinkedHashMap<>();
                        for (int i = 0; i < part.size(); i++) {
                            byte[] chunk = part.streams.get(i).readNBytes(chunkSize);
                            sub.computeIfAbsent(new ByteArrayKey(chunk), k -> new Partition())
                               .add(part.paths.get(i), part.streams.get(i));
                        }

                        for (Map.Entry<ByteArrayKey, Partition> e : sub.entrySet()) {
                            Partition g = e.getValue();
                            boolean atEof = e.getKey().bytes.length == 0; // empty read => exhausted
                            if (atEof) {
                                if (g.size() > 1) result.add(toStringList(g.paths)); // confirmed dups
                            } else if (g.size() > 1) {
                                next.add(g); // still tied -> compare next chunk
                            }
                            // non-EOF size-1 partitions are dropped (proven unique)
                        }
                    }
                    active = next;
                }
            } finally {
                for (InputStream in : allStreams) {
                    try { in.close(); } catch (IOException ignore) { }
                }
            }
            return result;
        }

        /** A set of files still believed identical, with their open streams (parallel lists). */
        private static final class Partition {
            final List<Path> paths = new ArrayList<>();
            final List<InputStream> streams = new ArrayList<>();
            void add(Path p, InputStream s) { paths.add(p); streams.add(s); }
            int size() { return paths.size(); }
        }

        /** Wraps byte[] for value-based HashMap keys (arrays default to identity equality). */
        private static final class ByteArrayKey {
            final byte[] bytes;
            ByteArrayKey(byte[] b) { this.bytes = b; }
            @Override public boolean equals(Object o) {
                return o instanceof ByteArrayKey k && Arrays.equals(bytes, k.bytes);
            }
            @Override public int hashCode() { return Arrays.hashCode(bytes); }
        }
    }

    // =========================================================================
    // Self-test harness — builds a fixture and verifies all three parts agree.
    // =========================================================================
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            // Run on a real directory: print the optimized (Part 2) result.
            System.out.println(DeduplicateOptimized.findDuplicateFiles(args[0]));
            return;
        }

        Path root = Files.createTempDirectory("dedup_fixture");
        Files.createDirectories(root.resolve("a"));
        Files.createDirectories(root.resolve("b/tmp"));
        Files.write(root.resolve("a/f1.mp4"), "hello world".getBytes());  // dup pair 1 (small)
        Files.write(root.resolve("a/f2.mp4"), "hello world".getBytes());
        Files.write(root.resolve("b/f3.mp4"), repeat('X', 5000));         // dup pair 2 (large)
        Files.write(root.resolve("b/tmp/f4.mp4"), repeat('X', 5000));
        Files.write(root.resolve("unique.txt"), "i am unique".getBytes());// singleton -> excluded
        Files.write(root.resolve("empty1.dat"), new byte[0]);             // empty dup pair (size 0)
        Files.write(root.resolve("empty2.dat"), new byte[0]);
        // same-size (5000) trap: identical first 1024 bytes, diverge later
        byte[] pa = new byte[5000]; Arrays.fill(pa, 0, 2000, (byte) 'P'); Arrays.fill(pa, 2000, 5000, (byte) 'A');
        byte[] pb = new byte[5000]; Arrays.fill(pb, 0, 2000, (byte) 'P'); Arrays.fill(pb, 2000, 5000, (byte) 'B');
        Files.write(root.resolve("pa.bin"), pa);                          // dup pair 3 (pa == pc)
        Files.write(root.resolve("pc.bin"), pa.clone());
        Files.write(root.resolve("pb.bin"), pb);                          // singleton (diverges at full stage)
        try { // symlink that must be SKIPPED (else empty group would wrongly be size 3)
            Files.createSymbolicLink(root.resolve("empty_link.dat"), root.resolve("empty1.dat"));
        } catch (Exception e) {
            System.out.println("(symlink creation skipped: " + e.getMessage() + ")");
        }

        List<List<String>> expected = List.of(
            List.of("a/f1.mp4", "a/f2.mp4"),
            List.of("b/f3.mp4", "b/tmp/f4.mp4"),
            List.of("empty1.dat", "empty2.dat"),
            List.of("pa.bin", "pc.bin")
        );
        List<List<String>> exp = new ArrayList<>();
        for (List<String> g : expected) { List<String> s = new ArrayList<>(g); s.sort(null); exp.add(s); }
        exp.sort((x, y) -> String.join(",", x).compareTo(String.join(",", y)));

        List<List<String>> basic = normalize(DeduplicateBasic.findDuplicateFiles(root.toString()), root);
        List<List<String>> opt   = normalize(DeduplicateOptimized.findDuplicateFiles(root.toString()), root);
        List<List<String>> strm  = normalize(DeduplicateStreaming.findDuplicateFiles(root.toString()), root);

        System.out.println("Expected : " + exp);
        System.out.println("Part 1   : " + basic);
        System.out.println("Part 2   : " + opt);
        System.out.println("Part 3   : " + strm);

        boolean ok = basic.equals(exp) && opt.equals(exp) && strm.equals(exp);
        System.out.println(ok ? "\nALL TESTS PASSED ✅" : "\nTEST FAILED ❌");
        if (!ok) System.exit(1);
    }

    private static byte[] repeat(char c, int n) {
        byte[] b = new byte[n];
        Arrays.fill(b, (byte) c);
        return b;
    }

    /** Make results comparable: relative paths, each group sorted, groups sorted. */
    private static List<List<String>> normalize(List<List<String>> groups, Path root) {
        List<List<String>> out = new ArrayList<>();
        for (List<String> g : groups) {
            out.add(g.stream()
                     .map(s -> root.relativize(Paths.get(s)).toString().replace('\\', '/'))
                     .sorted()
                     .collect(Collectors.toList()));
        }
        out.sort((a, b) -> String.join(",", a).compareTo(String.join(",", b)));
        return out;
    }
}
