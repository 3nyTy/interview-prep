/*
 * Anthropic - Staff SWE OA practice
 * Problem: "File Systems" -> canonical leaked rotation = in-memory CLOUD STORAGE
 *          service (CodeSignal, 90 min, 4 progressively-unlocking levels).
 *          (The generic tree-of-dicts FS the prompt lists under "Preparation"
 *           lives in FileSystemTree.java.)
 *
 * I/O CONTRACT (per the OA harness): every command is a String[] -> {verb, arg1, ...}
 * and every operation returns a STRING. "true"/"false" stand in for booleans,
 * "" for null/failure, and sizes/capacities are decimal integers as strings.
 *
 * Levels (search "LEVEL n"; later levels only ADD - never rewrite earlier ones):
 *   L1  ADD_FILE / COPY_FILE / GET_FILE_SIZE
 *   L2  FIND_FILE  (prefix + suffix match, exact result string)
 *   L3  ADD_USER / ADD_FILE_BY / UPDATE_CAPACITY  (owners + storage limits)
 *   L4  COMPRESS_FILE / DECOMPRESS_FILE
 *
 * Data model (the prompt's hint, and all you need): flat dict[path] -> FileEntry
 * plus dict[user] -> {capacity, used}. Directories are IMPLICIT - no tree needed,
 * paths are opaque flat keys. (A TreeMap is used so FIND_FILE's prefix match is a
 * sorted sub-range scan rather than a full table scan.)
 *
 * The THREE corners that fail most candidates (all handled + tested below):
 *   1. COPY_FILE preserves the SOURCE file's owner - not a caller - so the copy's
 *      bytes are billed to that owner (and rejected if they'd bust that owner's cap).
 *   2. UPDATE_CAPACITY is an EVICTION trigger, not just a future-add gate: it
 *      deletes the user's files largest-first (ties name-asc) until used <= cap,
 *      and returns how many it deleted. Files evict by their CURRENT size.
 *   3. FIND_FILE returns a LITERAL string ("name(size), name(size)" - no trailing
 *      comma, no space inside parens); hidden tests string-compare it.
 *
 * Compile & run:  javac CloudStorage.java && java CloudStorage
 * Verified on OpenJDK Corretto 21.
 */

import java.util.*;

public class CloudStorage {

    private static final String ADMIN = "admin";          // bare ADD_FILE owner (L3)
    private static final String SUFFIX = ".COMPRESSED";    // (L4)
    private static final long UNLIMITED = Long.MAX_VALUE;  // admin capacity (L3)

    /** A stored file. `owner` is fixed at creation; COPY copies the source's owner. */
    private static final class FileEntry {
        long size;
        final String owner;
        FileEntry(long size, String owner) { this.size = size; this.owner = owner; }
    }

    /** A user's quota. `used` is the running sum of the sizes of files they own. (L3) */
    private static final class User {
        long capacity;
        long used;
        User(long capacity) { this.capacity = capacity; }
    }

    // path -> file (TreeMap: FIND_FILE prefix match is a sorted range, O(matches) not O(n))
    private final TreeMap<String, FileEntry> files = new TreeMap<>();
    private final Map<String, User> users = new HashMap<>();   // (L3)

    public CloudStorage() {
        users.put(ADMIN, new User(UNLIMITED));   // admin always exists, unlimited (L3)
    }

    // --- small quota helpers (L3) ---
    private static boolean fits(User u, long add) {
        return u.capacity == UNLIMITED || u.used + add <= u.capacity;
    }
    private static String remaining(User u) {
        return String.valueOf(u.capacity - u.used);
    }

    // =====================================================================
    // LEVEL 1 - basic file management
    // =====================================================================

    /** ADD_FILE <name> <size>: admin-owned. "false" only if the path is taken. */
    public String addFile(String name, long size) {
        return addOwned(ADMIN, name, size) ? "true" : "false";
    }

    /**
     * COPY_FILE <from> <to>: "false" if source missing (a "directory" is just a
     * non-file path, so the missing check covers it in the flat-key model), if the
     * destination already exists, OR if the copy would push the SOURCE'S OWNER over
     * capacity. The copy keeps the source's owner and size (so a .COMPRESSED source
     * copies to a .COMPRESSED-named file unchanged) -> corner #1.
     */
    public String copyFile(String from, String to) {
        FileEntry src = files.get(from);
        if (src == null) return "false";              // missing source (or implicit dir)
        if (files.containsKey(to)) return "false";    // destination exists
        User owner = users.get(src.owner);            // preserve SOURCE owner, not a caller
        if (!fits(owner, src.size)) return "false";   // copy must fit that owner's quota
        files.put(to, new FileEntry(src.size, src.owner));
        owner.used += src.size;
        return "true";
    }

    /** GET_FILE_SIZE <name>: the size, or "" if the file does not exist. */
    public String getFileSize(String name) {
        FileEntry f = files.get(name);
        return f == null ? "" : String.valueOf(f.size);
    }

    /** Shared add used by ADD_FILE (admin) and ADD_FILE_BY (a user). (L1 + L3) */
    private boolean addOwned(String owner, String name, long size) {
        if (files.containsKey(name)) return false;    // path already taken
        User u = users.get(owner);
        if (u == null) return false;                  // unknown user (only reachable from L3)
        if (!fits(u, size)) return false;             // would exceed capacity
        files.put(name, new FileEntry(size, owner));
        u.used += size;
        return true;
    }

    // =====================================================================
    // LEVEL 2 - search   (NEW: FIND_FILE)
    // =====================================================================

    /**
     * FIND_FILE <prefix> <suffix>: files whose path startsWith prefix AND endsWith
     * suffix, sorted size DESC then name ASC, formatted EXACTLY as
     * "name(size), name(size)" (corner #3). "" if nothing matches.
     */
    public String findFile(String prefix, String suffix) {
        List<Map.Entry<String, FileEntry>> matches = new ArrayList<>();
        // tailMap(prefix): keys that share the prefix form a contiguous block at the
        // front, so we can stop at the first key that no longer starts with it.
        for (Map.Entry<String, FileEntry> e : files.tailMap(prefix).entrySet()) {
            if (!e.getKey().startsWith(prefix)) break;
            if (e.getKey().endsWith(suffix)) matches.add(e);
        }
        if (matches.isEmpty()) return "";
        matches.sort((a, b) -> a.getValue().size != b.getValue().size
                ? Long.compare(b.getValue().size, a.getValue().size)   // size desc
                : a.getKey().compareTo(b.getKey()));                   // tie -> name asc
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matches.size(); i++) {
            if (i > 0) sb.append(", ");                                // comma-space, none trailing
            sb.append(matches.get(i).getKey()).append('(')
              .append(matches.get(i).getValue().size).append(')');    // no space inside parens
        }
        return sb.toString();
    }

    // =====================================================================
    // LEVEL 3 - users & storage limits   (NEW: ADD_USER, ADD_FILE_BY, UPDATE_CAPACITY)
    // =====================================================================

    /** ADD_USER <userId> <capacity>: "false" if the user already exists. */
    public String addUser(String userId, long capacity) {
        if (users.containsKey(userId)) return "false";
        users.put(userId, new User(capacity));
        return "true";
    }

    /**
     * ADD_FILE_BY <userId> <name> <size>: like ADD_FILE but owned by userId.
     * "" if the user is unknown, the path is taken, or it would exceed capacity;
     * otherwise the user's REMAINING capacity.
     */
    public String addFileBy(String userId, String name, long size) {
        if (!users.containsKey(userId)) return "";    // unknown user (distinct from admin path)
        if (!addOwned(userId, name, size)) return ""; // path taken OR over capacity
        return remaining(users.get(userId));
    }

    /**
     * UPDATE_CAPACITY <userId> <capacity>: set the cap; if usage now exceeds it,
     * EVICT the user's files largest-first (ties name asc) until used <= cap.
     * Returns the deleted count, or "" for an unknown user -> corner #2.
     */
    public String updateCapacity(String userId, long newCap) {
        User u = users.get(userId);
        if (u == null) return "";
        u.capacity = newCap;

        int deleted = 0;
        if (u.used > newCap) {
            // gather this user's files and order them largest-first, ties by name asc
            List<Map.Entry<String, FileEntry>> owned = new ArrayList<>();
            for (Map.Entry<String, FileEntry> e : files.entrySet())
                if (e.getValue().owner.equals(userId)) owned.add(e);
            owned.sort((a, b) -> a.getValue().size != b.getValue().size
                    ? Long.compare(b.getValue().size, a.getValue().size)   // size desc
                    : a.getKey().compareTo(b.getKey()));                   // tie -> name asc
            for (Map.Entry<String, FileEntry> e : owned) {
                if (u.used <= newCap) break;
                files.remove(e.getKey());
                u.used -= e.getValue().size;          // evict by CURRENT size (compressed or not)
                deleted++;
            }
        }
        return String.valueOf(deleted);
    }

    // =====================================================================
    // LEVEL 4 - compression   (NEW: COMPRESS_FILE, DECOMPRESS_FILE)
    // =====================================================================

    /**
     * COMPRESS_FILE <userId> <name>: rename name -> name.COMPRESSED and halve its
     * size (integer division; OA inputs are even). "" if the file is missing, is
     * not owned by userId, or already ends in .COMPRESSED. Returns user's remaining.
     * (Compressing only shrinks usage, so no capacity check is needed.)
     */
    public String compressFile(String userId, String name) {
        FileEntry f = files.get(name);
        if (f == null) return "";                       // missing
        if (!f.owner.equals(userId)) return "";         // not owned by this user
        if (name.endsWith(SUFFIX)) return "";           // already compressed
        User u = users.get(userId);
        long freed = f.size - f.size / 2;               // bytes returned to the quota
        files.remove(name);
        f.size -= freed;                                // = size / 2
        files.put(name + SUFFIX, f);
        u.used -= freed;
        return remaining(u);
    }

    /**
     * DECOMPRESS_FILE <userId> <name>: strip .COMPRESSED and double the size back.
     * "" if the file is missing / not .COMPRESSED / not owned by userId, if the
     * un-suffixed name already exists, or if the full-size file would exceed the
     * user's capacity. Returns user's remaining.
     */
    public String decompressFile(String userId, String name) {
        FileEntry f = files.get(name);
        if (f == null) return "";                       // missing
        if (!name.endsWith(SUFFIX)) return "";          // nothing to decompress
        if (!f.owner.equals(userId)) return "";         // owner mismatch
        String base = name.substring(0, name.length() - SUFFIX.length());
        if (files.containsKey(base)) return "";         // un-suffixed name taken
        User u = users.get(userId);
        long extra = f.size;                            // doubling adds exactly the current size
        if (!fits(u, extra)) return "";                 // would exceed capacity
        files.remove(name);
        f.size += extra;                                // = 2 * compressed size
        files.put(base, f);
        u.used += extra;
        return remaining(u);
    }

    // =====================================================================
    // Dispatch  (the harness hands us String[] queries; every result is a String)
    // =====================================================================
    public String run(String[] q) {
        return switch (q[0]) {
            case "ADD_FILE"        -> addFile(q[1], Long.parseLong(q[2]));
            case "COPY_FILE"       -> copyFile(q[1], q[2]);
            case "GET_FILE_SIZE"   -> getFileSize(q[1]);
            case "FIND_FILE"       -> findFile(q[1], q[2]);
            case "ADD_USER"        -> addUser(q[1], Long.parseLong(q[2]));
            case "ADD_FILE_BY"     -> addFileBy(q[1], q[2], Long.parseLong(q[3]));
            case "UPDATE_CAPACITY" -> updateCapacity(q[1], Long.parseLong(q[2]));
            case "COMPRESS_FILE"   -> compressFile(q[1], q[2]);
            case "DECOMPRESS_FILE" -> decompressFile(q[1], q[2]);
            default -> throw new IllegalArgumentException("Unknown command: " + q[0]);
        };
    }

    /** Batch entry point matching the OA signature: String[][] -> String[]. */
    public String[] process(String[][] queries) {
        String[] out = new String[queries.length];
        for (int i = 0; i < queries.length; i++) out[i] = run(queries[i]);
        return out;
    }

    // =====================================================================
    // Self-verifying tests (not part of the OA answer)
    // =====================================================================
    private static int passed = 0, failed = 0;
    private static void check(String name, String got, String want) {
        if (Objects.equals(got, want)) passed++;
        else { failed++; System.out.println("FAIL " + name + ": got \"" + got + "\" want \"" + want + "\""); }
    }

    public static void main(String[] args) {
        // ---------- LEVEL 1 ----------
        CloudStorage s = new CloudStorage();
        check("add f1",        s.run(new String[]{"ADD_FILE", "/dir/file1.txt", "100"}), "true");
        check("add f1 dup",    s.run(new String[]{"ADD_FILE", "/dir/file1.txt", "50"}),  "false");
        check("add f2",        s.run(new String[]{"ADD_FILE", "/dir/file2.txt", "200"}), "true");
        check("add f3",        s.run(new String[]{"ADD_FILE", "/dir/sub/file3.txt", "200"}), "true");
        check("size f1",       s.run(new String[]{"GET_FILE_SIZE", "/dir/file1.txt"}), "100");
        check("size missing",  s.run(new String[]{"GET_FILE_SIZE", "/nope"}), "");
        check("copy missing",  s.run(new String[]{"COPY_FILE", "/nope", "/x"}), "false");
        check("copy dest used", s.run(new String[]{"COPY_FILE", "/dir/file1.txt", "/dir/file2.txt"}), "false");
        check("copy ok",       s.run(new String[]{"COPY_FILE", "/dir/file1.txt", "/dir/copy1.txt"}), "true");
        check("size copy",     s.run(new String[]{"GET_FILE_SIZE", "/dir/copy1.txt"}), "100");

        // ---------- LEVEL 2 ----------  files: file1(100) file2(200) sub/file3(200) copy1(100)
        check("find all .txt", s.run(new String[]{"FIND_FILE", "/dir/", ".txt"}),
                "/dir/file2.txt(200), /dir/sub/file3.txt(200), /dir/copy1.txt(100), /dir/file1.txt(100)");
        check("find one",      s.run(new String[]{"FIND_FILE", "/dir/", "file3.txt"}), "/dir/sub/file3.txt(200)");
        check("find no prefix", s.run(new String[]{"FIND_FILE", "/zzz", ".txt"}), "");
        check("find no suffix", s.run(new String[]{"FIND_FILE", "/dir/", ".png"}), "");

        // ---------- LEVEL 3 ----------
        CloudStorage u = new CloudStorage();
        check("add user",      u.run(new String[]{"ADD_USER", "u1", "1000"}), "true");
        check("add user dup",  u.run(new String[]{"ADD_USER", "u1", "1"}), "false");
        check("addby ghost",   u.run(new String[]{"ADD_FILE_BY", "ghost", "/a.txt", "10"}), "");
        check("addby a",       u.run(new String[]{"ADD_FILE_BY", "u1", "/a.txt", "300"}), "700"); // used 300
        // COPY preserves owner u1 -> bills u1 (used 600), corner #1
        check("copy bills owner", u.run(new String[]{"COPY_FILE", "/a.txt", "/a-copy.txt"}), "true");
        check("addby over cap", u.run(new String[]{"ADD_FILE_BY", "u1", "/big.txt", "500"}), ""); // 600+500>1000
        check("addby fill",    u.run(new String[]{"ADD_FILE_BY", "u1", "/big.txt", "400"}), "0"); // used 1000
        check("copy over cap", u.run(new String[]{"COPY_FILE", "/a.txt", "/a3.txt"}), "false");   // 1000+300>1000
        check("addby taken",   u.run(new String[]{"ADD_FILE_BY", "u1", "/a.txt", "1"}), "");
        // bare admin file copies freely (admin unlimited), u1 untouched
        check("admin add",     u.run(new String[]{"ADD_FILE", "/admin.bin", "999999"}), "true");
        check("admin copy",    u.run(new String[]{"COPY_FILE", "/admin.bin", "/admin2.bin"}), "true");
        // UPDATE_CAPACITY evicts largest-first, ties name asc: big(400), a-copy(300), a(300) -> drop 2
        check("update cap evict", u.run(new String[]{"UPDATE_CAPACITY", "u1", "500"}), "2");
        check("evicted big",   u.run(new String[]{"GET_FILE_SIZE", "/big.txt"}), "");
        check("evicted a-copy", u.run(new String[]{"GET_FILE_SIZE", "/a-copy.txt"}), "");
        check("kept a",        u.run(new String[]{"GET_FILE_SIZE", "/a.txt"}), "300");
        check("update cap ghost", u.run(new String[]{"UPDATE_CAPACITY", "ghost", "1"}), "");
        check("addby after evict", u.run(new String[]{"ADD_FILE_BY", "u1", "/c.txt", "200"}), "0"); // 300+200=500

        // ---------- LEVEL 4 ----------
        CloudStorage c = new CloudStorage();
        c.run(new String[]{"ADD_USER", "u", "1000"});
        check("L4 add",        c.run(new String[]{"ADD_FILE_BY", "u", "/f.txt", "400"}), "600"); // used 400
        check("L4 compress",   c.run(new String[]{"COMPRESS_FILE", "u", "/f.txt"}), "800");       // 200 freed -> used 200
        check("L4 old gone",   c.run(new String[]{"GET_FILE_SIZE", "/f.txt"}), "");
        check("L4 compressed size", c.run(new String[]{"GET_FILE_SIZE", "/f.txt.COMPRESSED"}), "200");
        check("L4 double-compress", c.run(new String[]{"COMPRESS_FILE", "u", "/f.txt.COMPRESSED"}), "");
        check("L4 compress missing", c.run(new String[]{"COMPRESS_FILE", "u", "/ghost"}), "");
        check("L4 compress not-owner", c.run(new String[]{"COMPRESS_FILE", "stranger", "/f.txt.COMPRESSED"}), "");
        // COPY preserves the .COMPRESSED suffix + halved size, bills owner u (used 400)
        check("L4 copy compressed", c.run(new String[]{"COPY_FILE", "/f.txt.COMPRESSED", "/g.txt.COMPRESSED"}), "true");
        check("L4 copy size",  c.run(new String[]{"GET_FILE_SIZE", "/g.txt.COMPRESSED"}), "200");
        check("L4 decompress", c.run(new String[]{"DECOMPRESS_FILE", "u", "/f.txt.COMPRESSED"}), "400"); // +200 -> used 600
        check("L4 decompress not-suffixed", c.run(new String[]{"DECOMPRESS_FILE", "u", "/f.txt"}), "");
        check("L4 decompress owner", c.run(new String[]{"DECOMPRESS_FILE", "stranger", "/g.txt.COMPRESSED"}), "");
        check("L4 decompress missing", c.run(new String[]{"DECOMPRESS_FILE", "u", "/missing.COMPRESSED"}), "");
        // un-suffixed name already exists -> ""
        check("L4 add g.txt",  c.run(new String[]{"ADD_FILE_BY", "u", "/g.txt", "50"}), "350");   // used 650
        check("L4 decompress base-exists", c.run(new String[]{"DECOMPRESS_FILE", "u", "/g.txt.COMPRESSED"}), "");
        // lower cap so decompress would exceed it -> ""
        check("L4 update cap noevict", c.run(new String[]{"UPDATE_CAPACITY", "u", "700"}), "0");   // used 650<=700
        check("L4 decompress over cap", c.run(new String[]{"DECOMPRESS_FILE", "u", "/g.txt.COMPRESSED"}), ""); // 650+200>700

        System.out.println("\nPassed " + passed + ", Failed " + failed);
        if (failed == 0) System.out.println("ALL CLOUD-STORAGE TESTS PASSED");
    }
}
