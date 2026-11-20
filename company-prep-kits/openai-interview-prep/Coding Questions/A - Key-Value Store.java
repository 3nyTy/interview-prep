/*
 * Durable Key-Value Store Serialization — OpenAI Staff SWE coding question.
 *
 * ========================================================================
 * WHOLE-QUESTION SUMMARY
 * ========================================================================
 * Build a persistent string->string key-value store that serializes and
 * deserializes itself to/from a MOCK file system using a CUSTOM length-prefixed
 * binary format — NO JSON, NO pickle, NO built-in object serialization. Keys and
 * values may contain ANY characters: the delimiters ':' ',' '=', newlines, other
 * special characters, unicode, and the empty string. The encoding must be
 * completely reversible: serialize -> deserialize returns the exact same map.
 *
 * The reason a length-prefixed format is the right tool: any delimiter-based
 * scheme ("key:value", CSV, etc.) breaks the moment that delimiter appears
 * inside a key or value, and escaping is fiddly. Length-prefixing sidesteps the
 * whole problem — we write how many bytes follow, then exactly that many bytes,
 * so the content is opaque and no byte is ever "special". This is the same idea
 * used by Redis RESP, netstrings, and HTTP chunked encoding.
 *
 * ------------------------------------------------------------------------
 * PART 1 SUMMARY — Basic key-value store with serialization
 * ------------------------------------------------------------------------
 * FileSystem holds a single blob: saveBlob(bytes) / getBlob() -> bytes.
 * KVStore supports put(k,v), get(k), shutdown() (serialize whole store to the
 * blob) and restore() (deserialize the blob back into the store).
 *
 * Wire format, repeated once per entry, with NO separators between entries:
 *     <keyLen:4 bytes><keyBytes><valueLen:4 bytes><valueBytes>
 * Lengths are FIXED-WIDTH 4-byte big-endian integers (NOT ascii digits), so the
 * parser always knows it must read exactly 4 bytes to learn the next field's
 * size. An empty store serializes to zero bytes. Deserialization walks a
 * position pointer: read a length, read that many bytes, repeat, until the
 * pointer reaches the end. A readLengthAndData helper does one length+payload
 * read and reports where the cursor ended up.
 *
 * RETURNING TWO VALUES FROM readLengthAndData. The helper must hand back both the
 * decoded string AND the new position, and Java has no tuples, no out-params, and
 * no built-in Pair (there is no java.util.Pair and no javax.Pair; javafx.util.Pair
 * was unbundled from the JDK in Java 11, and Guava deliberately omits Pair). The
 * options, best first:
 *   1. MUTABLE CURSOR OBJECT — a small class holding data + pos as fields, exposing
 *      next(). Advancing is automatic and cannot be forgotten. Zero allocation per
 *      field. This is what production serializers do, and it is the same shape as
 *      the hand-rolled ProtocolReader in the Reddit memcached answer.
 *   2. ByteBuffer.wrap(data) — carries its own position cursor, and getInt() is
 *      big-endian by default so it matches serializeInt exactly. Least code, but it
 *      bypasses the interviewer-provided deserializeInt/deserializeStr helpers, so
 *      ask first.
 *   3. RECORD (what this file uses) — `record Parsed(String value, int nextPos)`.
 *      Cleanest signature and self-documenting field names. Costs one small
 *      allocation per field parsed (usually scalarized away by escape analysis, but
 *      not guaranteed), and threading nextPos through is the CALLER's job — reusing
 *      a stale pos silently re-reads the same field forever.
 *   4. one-element int[] as a mutable box — works, fine under time pressure, reads
 *      poorly. This file used to do this.
 *   5. AbstractMap.SimpleEntry<String,Integer> — avoid: getKey()/getValue() hide
 *      which member is the position, and the int gets autoboxed.
 *
 * ------------------------------------------------------------------------
 * PART 2 SUMMARY — Chunked persistence with a 1KB-per-file size limit
 * ------------------------------------------------------------------------
 * Follow-up: each file can hold at most 1KB (1024 bytes). The store may exceed
 * that, so we transparently split the serialized blob across many files; callers
 * still just call put/get/shutdown/restore and never see the chunking.
 *
 *   CHANGES FROM PART 1:
 *     1. FileSystem becomes multi-file: saveBlob(filename, bytes),
 *        getBlob(filename) -> bytes, listFiles() -> List<String>. (Part 1's
 *        FileSystem had a single nameless blob.)
 *     2. shutdown() no longer writes one blob. It serializes the store (Part 1's
 *        exact format), writes a metadata file "_metadata" holding the chunk
 *        count as a 4-byte int, then splits the blob into 1024-byte pieces named
 *        "chunk_0", "chunk_1", ... The last chunk is whatever remainder is left
 *        (<= 1024 bytes). totalChunks = ceil(len / 1024) = (len+1023)/1024.
 *     3. restore() reads "_metadata" to learn the chunk count, reads
 *        chunk_0..chunk_{n-1} IN ORDER, concatenates them back into the exact
 *        original blob, then deserializes with Part 1's parser.
 *     4. Empty store: metadata holds 0 and NO chunk files are written; restore
 *        sees 0 and yields an empty store.
 *   put / get / serialize / deserialize / readLengthAndData and the four
 *   primitives are otherwise byte-for-byte identical to Part 1 (re-declared here
 *   so Part 2 stands alone).
 *
 * ------------------------------------------------------------------------
 * COMPILE / RUN (filename has spaces + a hyphen and does NOT match the class
 * name; that is fine because the top-level class is package-private):
 *   javac "A - Key-Value Store.java"     # produces KeyValueStore.class (+ nested)
 *   java -ea KeyValueStore               # runs ALL parts (default)
 *   java -ea KeyValueStore part1         # runs Part 1 only
 *   java -ea KeyValueStore part2         # runs Part 2 only
 * Run with -ea so the embedded `assert` checks are active. The few headline
 * round-trips are ALSO guarded by an explicit require(...) that throws even
 * without -ea, so a plain `java KeyValueStore` still fails loudly on a bug.
 *
 * NOTE ON NAMING: Java convention is camelCase, so the Python interface's
 * save_blob/get_blob/list_files/serialize_int/... appear here as
 * saveBlob/getBlob/listFiles/serializeInt/... — same behavior, idiomatic names.
 */

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Top-level dispatcher. Package-private (NOT public) so the filename need not
 * match the class name. Delegates to the two parts.
 *
 * Each PartN below is FULLY self-contained: it re-declares its own FileSystem,
 * the four serialization primitives, and KVStore, with ZERO references to any
 * outer/shared member. Copying a single PartN class body into its own .java
 * (promoted to a top-level `class Part1 { ... }`) compiles and runs unchanged.
 * The only cross-class calls are here in this dispatcher (Part1.main /
 * Part2.main), which the PartN bodies themselves never make.
 */
class KeyValueStore {

    public static void main(String[] args) {
        if (args.length == 0) {
            // No argument: run every part in order.
            Part1.main(args);
            Part2.main(args);
            return;
        }
        switch (args[0]) {
            case "part1" -> Part1.main(args);
            case "part2" -> Part2.main(args);
            default -> {
                System.err.println("Usage: java KeyValueStore [part1|part2]   "
                        + "(no argument runs all parts). Unknown arg: " + args[0]);
                // Fall back to running everything so the program is still useful.
                Part1.main(args);
                Part2.main(args);
            }
        }
    }

    // =====================================================================
    // PART 1 — Basic key-value store with length-prefixed serialization.
    // =====================================================================
    static final class Part1 {

        // -------------------------------------------------------------
        // Mock file system: a single, nameless blob. Provided by the
        // interviewer in the real problem; modeled here. camelCase
        // saveBlob/getBlob map to Python's save_blob/get_blob.
        // -------------------------------------------------------------
        static final class FileSystem {
            private byte[] blob;

            /** Store a DEFENSIVE COPY so later caller mutation can't corrupt us. */
            void saveBlob(byte[] data) {
                this.blob = (data == null) ? new byte[0] : data.clone();
            }

            /** Return the saved blob, or an empty array if nothing was saved (never null). */
            byte[] getBlob() {
                return (blob == null) ? new byte[0] : blob;
            }
        }

        // -------------------------------------------------------------
        // Serialization primitives (provided in the real problem).
        // -------------------------------------------------------------

        /** int -> fixed-width 4-byte BIG-ENDIAN. MUST be exactly 4 bytes. */
        static byte[] serializeInt(int value) {
            return new byte[] {
                    (byte) ((value >>> 24) & 0xFF),
                    (byte) ((value >>> 16) & 0xFF),
                    (byte) ((value >>> 8) & 0xFF),
                    (byte) (value & 0xFF)
            };
        }

        /** Read exactly 4 big-endian bytes back into an int. The &0xFF masks are mandatory. */
        static int deserializeInt(byte[] d) {
            return ((d[0] & 0xFF) << 24)
                    | ((d[1] & 0xFF) << 16)
                    | ((d[2] & 0xFF) << 8)
                    | (d[3] & 0xFF);
        }

        /** String -> UTF-8 bytes. Empty string -> empty array. */
        static byte[] serializeStr(String value) {
            return value.getBytes(StandardCharsets.UTF_8);
        }

        /** UTF-8 bytes -> String. Empty array -> "". */
        static String deserializeStr(byte[] data) {
            return new String(data, StandardCharsets.UTF_8);
        }

        // -------------------------------------------------------------
        // The key-value store.
        // -------------------------------------------------------------
        static final class KVStore {
            private final FileSystem fs;
            // LinkedHashMap: deterministic iteration order -> deterministic bytes.
            private LinkedHashMap<String, String> store = new LinkedHashMap<>();

            KVStore(FileSystem fs) {
                this.fs = fs;
            }

            /** Store a pair in memory (O(1) avg). Overwrites an existing key. */
            void put(String key, String value) {
                store.put(key, value);
            }

            /** Retrieve a value, or null if the key is absent (O(1) avg). */
            String get(String key) {
                return store.get(key);
            }

            /** Serialize the entire store and persist it to the file system. */
            void shutdown() {
                fs.saveBlob(serialize());
            }

            /** Load and deserialize the store from the file system. */
            void restore() {
                byte[] data = fs.getBlob();
                store = (data == null || data.length == 0)
                        ? new LinkedHashMap<>()
                        : deserialize(data);
            }

            /**
             * Length-prefixed encoding. Per entry, with NO separators:
             *   <keyLen:4><keyBytes><valueLen:4><valueBytes>
             * Empty store -> empty byte[]. O(B) time and space (B = total bytes).
             */
            private byte[] serialize() {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                for (var entry : store.entrySet()) {
                    byte[] kb = serializeStr(entry.getKey());
                    byte[] vb = serializeStr(entry.getValue());
                    // write(byte[]) on ByteArrayOutputStream cannot throw IOException.
                    out.write(serializeInt(kb.length), 0, 4);
                    out.write(kb, 0, kb.length);
                    out.write(serializeInt(vb.length), 0, 4);
                    out.write(vb, 0, vb.length);
                }
                return out.toByteArray();
            }

            /**
             * One parsed field plus where the cursor ended up. Java has no built-in
             * Pair (no java.util.Pair, no javax.Pair — javafx.util.Pair was unbundled
             * from the JDK in Java 11), so a RECORD is the idiomatic way to return two
             * values. `nextPos` is self-documenting in a way SimpleEntry.getValue()
             * would not be, and it avoids autoboxing the position.
             */
            private record Parsed(String value, int nextPos) { }

            /** Reverse of serialize(): thread the cursor to the end. O(B). */
            private LinkedHashMap<String, String> deserialize(byte[] data) {
                LinkedHashMap<String, String> result = new LinkedHashMap<>();
                int pos = 0;
                while (pos < data.length) {
                    Parsed key = readLengthAndData(data, pos);
                    Parsed value = readLengthAndData(data, key.nextPos());
                    result.put(key.value(), value.value());
                    pos = value.nextPos();      // MUST thread it through; see caveat below
                }
                return result;
            }

            /**
             * Read one length-prefixed segment starting at pos: a 4-byte length, then
             * that many bytes decoded as UTF-8. Returns the decoded string together
             * with the position just past the segment. A length of 0 yields "".
             *
             * CAVEAT of the record form: advancing is the CALLER's job. Passing the
             * stale `pos` to both calls re-reads the same field forever (a silent
             * infinite loop). A mutable cursor object — data + pos as fields, exposing
             * next() — is the one variant where advancing cannot be forgotten; see the
             * file header for the ranking of the alternatives.
             */
            private Parsed readLengthAndData(byte[] data, int pos) {
                byte[] lenBytes = Arrays.copyOfRange(data, pos, pos + 4);
                int len = deserializeInt(lenBytes);
                pos += 4;
                byte[] strBytes = Arrays.copyOfRange(data, pos, pos + len); // len 0 -> ""
                return new Parsed(deserializeStr(strBytes), pos + len);
            }
        }

        // -------------------------------------------------------------
        // require(): a hard check that fires even WITHOUT -ea. Used for the
        // headline round-trips so a plain `java` run still fails loudly.
        // -------------------------------------------------------------
        private static void require(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }

        public static void main(String[] args) {
            // ---- Example group 1 (from the problem's "Example Usage") ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("name", "John:Doe");
                kv.put("city", "New,York");
                kv.put("key\nwith\nnewlines", "value=with=equals");
                kv.shutdown();

                KVStore restored = new KVStore(fs);
                restored.restore();
                require("John:Doe".equals(restored.get("name")), "name round-trip");
                require("New,York".equals(restored.get("city")), "city round-trip");
                require("value=with=equals".equals(restored.get("key\nwith\nnewlines")),
                        "newline-key round-trip");
            }

            // ---- Example group 2 (from the problem's __main__) ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("key:with:colons", "value,with,commas");
                kv.put("key\nwith\nnewlines", "value=with=equals");
                kv.put("", "empty key");
                kv.put("empty value", "");
                kv.shutdown();

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require("value,with,commas".equals(kv2.get("key:with:colons")), "colon key");
                require("value=with=equals".equals(kv2.get("key\nwith\nnewlines")), "newline key");
                require("empty key".equals(kv2.get("")), "empty key -> 'empty key'");
                require("".equals(kv2.get("empty value")), "empty value -> ''");
            }

            // ---- Empty store: blob length 0; absent get returns null ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.shutdown();
                assert fs.getBlob().length == 0 : "empty store -> 0-byte blob";
                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                assert kv2.get("anything") == null : "absent key -> null";
            }

            // ---- Delimiters, newlines, empty key/value, mixed together ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put(":,=", "=,:");                       // pure delimiters
                kv.put("a\nb\tc", "x\r\ny");                // newlines / tabs / CR
                kv.put("", "");                             // both empty
                kv.put("only-key-empty-val", "");
                kv.put("", "later-overwrites-empty-key");   // overwrite empty key
                kv.shutdown();

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                assert "=,:".equals(kv2.get(":,="));
                assert "x\r\ny".equals(kv2.get("a\nb\tc"));
                assert "".equals(kv2.get("only-key-empty-val"));
                assert "later-overwrites-empty-key".equals(kv2.get(""));
            }

            // ---- Unicode: emoji (surrogate pair), accent, arrow, Greek ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                String key = "café→Ω";                      // accent, arrow, Greek
                String val = "emoji=😀 surrogate-pair";      // 4-byte UTF-8 code point
                kv.put(key, val);
                kv.put("Ω", "café");
                kv.shutdown();

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require(val.equals(kv2.get(key)), "unicode value round-trip");
                require("café".equals(kv2.get("Ω")), "unicode key round-trip");
            }

            // ---- Very long strings ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                String bigKey = "k".repeat(2000);
                String bigVal = "x".repeat(5000);
                kv.put(bigKey, bigVal);
                kv.shutdown();

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require(bigVal.equals(kv2.get(bigKey)), "very long string round-trip");
            }

            // ---- Multiple put/shutdown/restore cycles with mutation between ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("a", "1");
                kv.shutdown();

                kv.put("b", "2");            // mutate after first shutdown
                kv.shutdown();               // re-persist

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                assert "1".equals(kv2.get("a"));
                assert "2".equals(kv2.get("b"));

                kv2.put("a", "1-updated");   // mutate the restored store
                kv2.shutdown();

                KVStore kv3 = new KVStore(fs);
                kv3.restore();
                assert "1-updated".equals(kv3.get("a"));
                assert "2".equals(kv3.get("b"));
            }

            // ---- Duplicate-key overwrite: latest wins, only one entry persists ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("dup", "first");
                kv.put("dup", "second");
                kv.put("dup", "third");
                kv.shutdown();

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                assert "third".equals(kv2.get("dup")) : "latest value wins";
                // Exactly one logical entry: re-serializing yields the bytes of a single pair.
                // (4 + len("dup") + 4 + len("third")) = 4 + 3 + 4 + 5 = 16 bytes.
                assert fs.getBlob().length == 16 : "only one entry persisted for a duplicate key";
            }

            System.out.println("Part 1: all assertions passed");
        }
    }

    // =====================================================================
    // PART 2 — Chunked persistence with a 1KB per-file size limit.
    //
    // CHANGES FROM PART 1 (see the file header for the full list):
    //   - FileSystem is multi-file: saveBlob(name, bytes) / getBlob(name) /
    //     listFiles().
    //   - shutdown() writes a "_metadata" file (chunk count) and splits the
    //     serialized blob into 1024-byte "chunk_i" files.
    //   - restore() reads the count from "_metadata", then reads + concatenates
    //     chunk_0..chunk_{n-1} before deserializing.
    // Everything else (put/get/serialize/deserialize/primitives) is identical
    // to Part 1, re-declared here so this class is self-contained.
    // =====================================================================
    static final class Part2 {

        // -------------------------------------------------------------
        // Mock file system: now MANY named files. camelCase
        // saveBlob/getBlob/listFiles map to Python save_blob/get_blob/list_files.
        // -------------------------------------------------------------
        static final class FileSystem {
            // LinkedHashMap -> listFiles() is deterministic (insertion order).
            private final LinkedHashMap<String, byte[]> files = new LinkedHashMap<>();

            /** Store a DEFENSIVE COPY under the given filename. */
            void saveBlob(String filename, byte[] data) {
                files.put(filename, (data == null) ? new byte[0] : data.clone());
            }

            /** Return the named file's bytes, or null if it does not exist. */
            byte[] getBlob(String filename) {
                return files.get(filename);
            }

            /** List all filenames currently stored. */
            List<String> listFiles() {
                return new ArrayList<>(files.keySet());
            }
        }

        // -------------------------------------------------------------
        // Serialization primitives — identical to Part 1.
        // -------------------------------------------------------------

        /** int -> fixed-width 4-byte BIG-ENDIAN. MUST be exactly 4 bytes. */
        static byte[] serializeInt(int value) {
            return new byte[] {
                    (byte) ((value >>> 24) & 0xFF),
                    (byte) ((value >>> 16) & 0xFF),
                    (byte) ((value >>> 8) & 0xFF),
                    (byte) (value & 0xFF)
            };
        }

        /** Read exactly 4 big-endian bytes back into an int. The &0xFF masks are mandatory. */
        static int deserializeInt(byte[] d) {
            return ((d[0] & 0xFF) << 24)
                    | ((d[1] & 0xFF) << 16)
                    | ((d[2] & 0xFF) << 8)
                    | (d[3] & 0xFF);
        }

        /** String -> UTF-8 bytes. Empty string -> empty array. */
        static byte[] serializeStr(String value) {
            return value.getBytes(StandardCharsets.UTF_8);
        }

        /** UTF-8 bytes -> String. Empty array -> "". */
        static String deserializeStr(byte[] data) {
            return new String(data, StandardCharsets.UTF_8);
        }

        // -------------------------------------------------------------
        // The chunked key-value store.
        // -------------------------------------------------------------
        static final class KVStore {
            static final int CHUNK_SIZE = 1024;            // 1KB per file
            static final String METADATA_FILE = "_metadata";
            static final String CHUNK_PREFIX = "chunk_";

            private final FileSystem fs;
            private LinkedHashMap<String, String> store = new LinkedHashMap<>();

            KVStore(FileSystem fs) {
                this.fs = fs;
            }

            /** Store a pair in memory (O(1) avg). Overwrites an existing key. */
            void put(String key, String value) {
                store.put(key, value);
            }

            /** Retrieve a value, or null if absent (O(1) avg). */
            String get(String key) {
                return store.get(key);
            }

            /**
             * Serialize, write metadata, then split into <=1KB chunk files. O(B).
             * Produces C+1 files (1 metadata + C chunks); the last chunk is the
             * remainder (<= CHUNK_SIZE). Empty store -> metadata 0, no chunks.
             */
            void shutdown() {
                byte[] data = serialize();
                if (data.length == 0) {
                    // Empty store: record 0 chunks and write no chunk files.
                    fs.saveBlob(METADATA_FILE, serializeInt(0));
                    return;
                }
                int totalChunks = (data.length + CHUNK_SIZE - 1) / CHUNK_SIZE; // ceil
                // We write metadata FIRST to mirror the reference solution. NOTE: in a
                // real durable/crash-safe system you'd write the chunks first and the
                // metadata LAST, so the metadata acts as a commit marker — a crash
                // mid-write then leaves stale-but-ignored chunks rather than metadata
                // that points at chunks which don't exist yet.
                fs.saveBlob(METADATA_FILE, serializeInt(totalChunks));
                for (int i = 0; i < totalChunks; i++) {
                    int start = i * CHUNK_SIZE;
                    int end = Math.min(start + CHUNK_SIZE, data.length);
                    byte[] chunk = Arrays.copyOfRange(data, start, end);
                    fs.saveBlob(CHUNK_PREFIX + i, chunk);
                }
            }

            /**
             * Read metadata, read chunk_0..chunk_{n-1} in order, concatenate, and
             * deserialize. O(B).
             *
             * WHY BYTE-BOUNDARY SPLITTING IS SAFE: every chunk is concatenated back
             * into the EXACT original byte[] BEFORE any UTF-8 decode or length-prefix
             * parse happens. So a multi-byte UTF-8 sequence — or even one of our
             * 4-byte length prefixes — may straddle a chunk boundary with zero risk;
             * the boundary is purely a storage artifact and is gone before parsing.
             * Char-boundary (codepoint-aware) splitting is therefore unnecessary.
             */
            void restore() {
                byte[] meta = fs.getBlob(METADATA_FILE);
                int totalChunks = (meta == null || meta.length == 0) ? 0 : deserializeInt(meta);
                if (totalChunks == 0) {
                    store = new LinkedHashMap<>();
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int i = 0; i < totalChunks; i++) {
                    byte[] chunk = fs.getBlob(CHUNK_PREFIX + i);
                    baos.write(chunk, 0, chunk.length);
                }
                store = deserialize(baos.toByteArray());
                // NOTE: if the store SHRANK across shutdowns, stale chunk_i files for
                // i >= totalChunks may linger, but restore only ever reads 0..count-1
                // from the freshly written metadata, so it stays correct. A production
                // system would also delete those stale files for tidiness.
            }

            // ---- Below here: byte-for-byte identical to Part 1. ----

            private byte[] serialize() {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                for (var entry : store.entrySet()) {
                    byte[] kb = serializeStr(entry.getKey());
                    byte[] vb = serializeStr(entry.getValue());
                    out.write(serializeInt(kb.length), 0, 4);
                    out.write(kb, 0, kb.length);
                    out.write(serializeInt(vb.length), 0, 4);
                    out.write(vb, 0, vb.length);
                }
                return out.toByteArray();
            }

            /** One parsed field plus where the cursor ended up. See Part 1's note. */
            private record Parsed(String value, int nextPos) { }

            private LinkedHashMap<String, String> deserialize(byte[] data) {
                LinkedHashMap<String, String> result = new LinkedHashMap<>();
                int pos = 0;
                while (pos < data.length) {
                    Parsed key = readLengthAndData(data, pos);
                    Parsed value = readLengthAndData(data, key.nextPos());
                    result.put(key.value(), value.value());
                    pos = value.nextPos();
                }
                return result;
            }

            private Parsed readLengthAndData(byte[] data, int pos) {
                byte[] lenBytes = Arrays.copyOfRange(data, pos, pos + 4);
                int len = deserializeInt(lenBytes);
                pos += 4;
                byte[] strBytes = Arrays.copyOfRange(data, pos, pos + len);
                return new Parsed(deserializeStr(strBytes), pos + len);
            }
        }

        // require(): hard check active even without -ea (headline round-trips).
        private static void require(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }

        // Count how many of the listed files are chunk files (start with "chunk_").
        private static int countChunks(FileSystem fs) {
            int n = 0;
            for (String f : fs.listFiles()) {
                if (f.startsWith(KVStore.CHUNK_PREFIX)) n++;
            }
            return n;
        }

        public static void main(String[] args) {
            // ---- (1) The problem's Part 2 example: 15 entries spanning >1 chunk ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                for (int i = 0; i < 15; i++) {
                    kv.put("key_" + i + "_" + "x".repeat(80),
                           "value_" + i + "_" + "y".repeat(80));
                }
                kv.shutdown();
                require(countChunks(fs) > 1, "15 large entries must span more than one chunk");

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                for (int i = 0; i < 15; i++) {
                    String ek = "key_" + i + "_" + "x".repeat(80);
                    String ev = "value_" + i + "_" + "y".repeat(80);
                    require(ev.equals(kv2.get(ek)), "15-entry round-trip i=" + i);
                }
            }

            // ---- (2) Store < 1KB -> exactly one chunk ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("k", "v");                          // bytes = 4+1+4+1 = 10
                kv.shutdown();
                assert deserializeInt(fs.getBlob(KVStore.METADATA_FILE)) == 1 : "metadata == 1";
                assert countChunks(fs) == 1 : "exactly one chunk file";
                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require("v".equals(kv2.get("k")), "<1KB single-chunk round-trip");
            }

            // ---- (3) Exactly 1KB -> still a single chunk of length 1024 ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                // overhead per pair = 4 (keyLen) + len(key) + 4 (valLen) = 4+1+4 = 9
                // value length 1015 -> total = 9 + 1015 = 1024 bytes exactly.
                kv.put("k", "v".repeat(1015));
                kv.shutdown();
                assert deserializeInt(fs.getBlob(KVStore.METADATA_FILE)) == 1 : "metadata == 1 (exactly 1KB)";
                assert countChunks(fs) == 1 : "single chunk at exactly 1KB";
                assert fs.getBlob(KVStore.CHUNK_PREFIX + 0).length == 1024 : "chunk_0 length == 1024";
                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require(("v".repeat(1015)).equals(kv2.get("k")), "exactly-1KB round-trip");
            }

            // ---- (4) Slightly over 1KB -> 2 chunks (1024 + 1); last chunk smaller ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("k", "v".repeat(1016));             // total = 9 + 1016 = 1025 bytes
                kv.shutdown();
                assert deserializeInt(fs.getBlob(KVStore.METADATA_FILE)) == 2 : "metadata == 2";
                assert fs.getBlob(KVStore.CHUNK_PREFIX + 0).length == 1024 : "chunk_0 == 1024";
                assert fs.getBlob(KVStore.CHUNK_PREFIX + 1).length == 1 : "chunk_1 == 1 (remainder)";
                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require(("v".repeat(1016)).equals(kv2.get("k")), "slightly-over-1KB round-trip");
            }

            // ---- (5) Many chunks (~10KB -> 10), plus a unicode multi-chunk case ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                // total = 9 + (10240 - 9) = 10240 bytes -> exactly 10 chunks.
                String bigVal = "z".repeat(10240 - 9);
                kv.put("k", bigVal);
                kv.shutdown();
                assert deserializeInt(fs.getBlob(KVStore.METADATA_FILE)) == 10 : "metadata == 10";
                assert countChunks(fs) == 10 : "10 chunk files";
                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require(bigVal.equals(kv2.get("k")), "~10KB many-chunk round-trip");

                // Unicode across many chunks: 300 copies of a 4-byte UTF-8 emoji =
                // 1200 value bytes -> spans multiple chunks; proves multi-byte
                // sequences survive byte-boundary splitting intact.
                FileSystem fsu = new FileSystem();
                KVStore kvu = new KVStore(fsu);
                String emojis = "😀".repeat(300);
                kvu.put("emoji", emojis);
                kvu.shutdown();
                require(countChunks(fsu) > 1, "emoji payload must span >1 chunk");
                KVStore kvu2 = new KVStore(fsu);
                kvu2.restore();
                require(emojis.equals(kvu2.get("emoji")),
                        "multi-byte unicode survives byte-boundary chunk split");
            }

            // ---- (6) Empty store -> metadata 0, NO chunk files, restore is empty ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.shutdown();                             // no puts
                assert deserializeInt(fs.getBlob(KVStore.METADATA_FILE)) == 0 : "empty -> metadata 0";
                assert countChunks(fs) == 0 : "empty -> no chunk files";
                List<String> files = fs.listFiles();
                assert files.size() == 1 && files.contains(KVStore.METADATA_FILE)
                        : "empty store lists only _metadata";
                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                assert kv2.get("anything") == null : "restored empty store -> get null";
            }

            // ---- (7) Last chunk smaller than 1KB: verified in case (4) (==1).
            //          Also assert it generically for the ~10KB case (==1024 here). ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("k", "v".repeat(1500));             // total 9+1500 = 1509 -> 2 chunks
                kv.shutdown();
                int total = deserializeInt(fs.getBlob(KVStore.METADATA_FILE));
                int lastLen = fs.getBlob(KVStore.CHUNK_PREFIX + (total - 1)).length;
                assert lastLen <= KVStore.CHUNK_SIZE : "last chunk <= CHUNK_SIZE";
                assert lastLen == 1509 - 1024 : "last chunk == remainder (485)";
            }

            // ---- (8) Multiple shutdown/restore cycles crossing the 1KB boundary ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("k", "small");                      // well under 1KB -> 1 chunk
                kv.shutdown();
                assert countChunks(fs) == 1 : "small store -> 1 chunk";
                KVStore r1 = new KVStore(fs);
                r1.restore();
                require("small".equals(r1.get("k")), "cycle-1 round-trip");

                kv.put("k", "v".repeat(2000));             // now > 1KB -> 2+ chunks
                kv.shutdown();
                assert countChunks(fs) >= 2 : "grown store -> 2+ chunks";
                KVStore r2 = new KVStore(fs);
                r2.restore();
                require(("v".repeat(2000)).equals(r2.get("k")), "cycle-2 round-trip (latest wins)");
            }

            // ---- Reuse Part 1 content edge cases inside a Part 2 round-trip ----
            {
                FileSystem fs = new FileSystem();
                KVStore kv = new KVStore(fs);
                kv.put("key:with:colons", "value,with,commas");
                kv.put("key\nwith\nnewlines", "value=with=equals");
                kv.put("", "empty key");
                kv.put("empty value", "");
                kv.put("café→Ω", "emoji=😀");
                kv.shutdown();

                KVStore kv2 = new KVStore(fs);
                kv2.restore();
                require("value,with,commas".equals(kv2.get("key:with:colons")), "p2 colon key");
                require("value=with=equals".equals(kv2.get("key\nwith\nnewlines")), "p2 newline key");
                require("empty key".equals(kv2.get("")), "p2 empty key");
                require("".equals(kv2.get("empty value")), "p2 empty value");
                require("emoji=😀".equals(kv2.get("café→Ω")), "p2 unicode");
            }

            System.out.println("Part 2: all assertions passed");
        }
    }
}
