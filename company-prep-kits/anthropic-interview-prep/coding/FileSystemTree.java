/*
 * Anthropic - Staff SWE OA practice  (COMPANION to CloudStorage.java)
 * Problem: "File Systems" -> the GENERIC tree-of-dicts file system the prompt's
 *          "Preparation" section says to be able to build from scratch in <45 min:
 *          mkdir, touch, read, write, ls, rm (recursive), mv  over /a/b/c paths.
 *
 * This is the in-rotation alternative to the flat-key cloud-storage variant; it
 * uses a real DIRECTORY TREE because these ops need hierarchy (ls, recursive rm,
 * mv-into-dir). Standard Unix-ish semantics; see each method.
 *
 * Path handling (the prompt's "clean helper for parsing/validating paths"):
 *   - absolute, '/'-separated; empty segments and "." are dropped; ".." pops.
 *   - so "/a/./b/../c" -> [a, c]; "/" -> [] (the root).
 *
 * Compile & run:  javac FileSystemTree.java && java FileSystemTree
 * Verified on OpenJDK Corretto 21.
 */

import java.util.*;

public class FileSystemTree {

    /** A node is either a directory (has children) or a file (has content). */
    private static final class Node {
        final boolean isDir;
        final TreeMap<String, Node> children;   // sorted -> ls is sorted for free
        String content;                          // files only
        Node(boolean isDir) {
            this.isDir = isDir;
            this.children = isDir ? new TreeMap<>() : null;
            this.content = isDir ? null : "";
        }
    }

    private final Node root = new Node(true);

    // --- path helpers ---
    /** Normalize an absolute path to its segment list, resolving "." and "..". */
    private static List<String> parse(String path) {
        Deque<String> stack = new ArrayDeque<>();
        for (String part : path.split("/")) {
            if (part.isEmpty() || part.equals(".")) continue;
            if (part.equals("..")) { if (!stack.isEmpty()) stack.pollLast(); }
            else stack.addLast(part);
        }
        return new ArrayList<>(stack);
    }

    /** Resolve a segment list to its node, or null if any component is missing. */
    private Node resolve(List<String> parts) {
        Node cur = root;
        for (String p : parts) {
            if (!cur.isDir) return null;
            cur = cur.children.get(p);
            if (cur == null) return null;
        }
        return cur;
    }

    // =====================================================================
    // Directory + file creation
    // =====================================================================

    /** mkdir -p: create the directory and any missing parents. False if a FILE blocks the path. */
    public boolean mkdir(String path) {
        Node cur = root;
        for (String p : parse(path)) {
            Node next = cur.children.get(p);
            if (next == null) { next = new Node(true); cur.children.put(p, next); }
            else if (!next.isDir) return false;     // a file sits where a dir must go
            cur = next;
        }
        return true;
    }

    /** touch: create an empty file (parent must already be a dir). Existing file -> ok; dir -> false. */
    public boolean touch(String path) {
        List<String> parts = parse(path);
        if (parts.isEmpty()) return false;          // that's the root
        Node parent = resolve(parts.subList(0, parts.size() - 1));
        if (parent == null || !parent.isDir) return false;
        String name = parts.get(parts.size() - 1);
        Node n = parent.children.get(name);
        if (n != null) return !n.isDir;             // file: no-op true; dir: false
        parent.children.put(name, new Node(false));
        return true;
    }

    // =====================================================================
    // Read / write
    // =====================================================================

    /** write: create-or-overwrite a file with content (parent must be a dir; path must not be a dir). */
    public boolean write(String path, String content) {
        List<String> parts = parse(path);
        if (parts.isEmpty()) return false;
        Node parent = resolve(parts.subList(0, parts.size() - 1));
        if (parent == null || !parent.isDir) return false;
        String name = parts.get(parts.size() - 1);
        Node n = parent.children.get(name);
        if (n != null && n.isDir) return false;     // can't write over a directory
        if (n == null) { n = new Node(false); parent.children.put(name, n); }
        n.content = content;
        return true;
    }

    /** read: file content, or null if missing / is a directory. */
    public String read(String path) {
        Node n = resolve(parse(path));
        return (n == null || n.isDir) ? null : n.content;
    }

    // =====================================================================
    // Listing
    // =====================================================================

    /** ls: dir -> sorted child names; file -> [its own name]; missing -> null. */
    public List<String> ls(String path) {
        List<String> parts = parse(path);
        Node n = resolve(parts);
        if (n == null) return null;
        if (!n.isDir) return List.of(parts.get(parts.size() - 1));   // ls of a file = the file
        return new ArrayList<>(n.children.keySet());                 // already sorted (TreeMap)
    }

    // =====================================================================
    // Remove (recursive) + move
    // =====================================================================

    /** rm -r: detach the node (and, for a dir, its whole subtree). False if missing or root. */
    public boolean rm(String path) {
        List<String> parts = parse(path);
        if (parts.isEmpty()) return false;          // refuse to remove root
        Node parent = resolve(parts.subList(0, parts.size() - 1));
        if (parent == null || !parent.isDir) return false;
        return parent.children.remove(parts.get(parts.size() - 1)) != null;
    }

    /**
     * mv: if dst is an existing directory, move src INTO it (keeping src's name);
     * otherwise rename src to dst (overwriting an existing file at dst). False if
     * src is missing, the dst parent isn't a dir, or it would move a dir into its
     * own subtree.
     */
    public boolean mv(String srcPath, String dstPath) {
        List<String> sp = parse(srcPath);
        if (sp.isEmpty()) return false;
        Node srcParent = resolve(sp.subList(0, sp.size() - 1));
        if (srcParent == null || !srcParent.isDir) return false;
        String srcName = sp.get(sp.size() - 1);
        Node src = srcParent.children.get(srcName);
        if (src == null) return false;

        List<String> dp = parse(dstPath);
        Node dstNode = resolve(dp);
        Node targetParent;
        String targetName;
        if (dstNode != null && dstNode.isDir) {     // move INTO an existing directory
            targetParent = dstNode;
            targetName = srcName;
        } else {                                    // rename (maybe overwriting a file)
            if (dp.isEmpty()) return false;
            targetParent = resolve(dp.subList(0, dp.size() - 1));
            if (targetParent == null || !targetParent.isDir) return false;
            targetName = dp.get(dp.size() - 1);
        }
        if (src.isDir && isAncestor(src, targetParent)) return false;   // no cycles
        if (targetParent == srcParent && targetName.equals(srcName)) return true; // no-op

        srcParent.children.remove(srcName);
        targetParent.children.put(targetName, src);
        return true;
    }

    /** Is `a` equal to, or an ancestor of, `b`? (guards mv-into-own-subtree). */
    private boolean isAncestor(Node a, Node b) {
        if (a == b) return true;
        if (!a.isDir) return false;
        for (Node ch : a.children.values()) if (isAncestor(ch, b)) return true;
        return false;
    }

    // =====================================================================
    // Self-verifying tests (not part of the OA answer)
    // =====================================================================
    private static int passed = 0, failed = 0;
    private static void check(String name, Object got, Object want) {
        if (Objects.equals(got, want)) passed++;
        else { failed++; System.out.println("FAIL " + name + ": got " + got + " want " + want); }
    }

    public static void main(String[] args) {
        FileSystemTree fs = new FileSystemTree();

        // mkdir -p + ls
        check("mkdir a/b/c", fs.mkdir("/a/b/c"), true);
        check("ls /",        fs.ls("/"), List.of("a"));
        check("ls /a",       fs.ls("/a"), List.of("b"));
        check("ls /a/b",     fs.ls("/a/b"), List.of("c"));
        check("mkdir dotseg", fs.mkdir("/a/./b/../x"), true);  // -> /a/x
        check("ls /a sorted", fs.ls("/a"), List.of("b", "x")); // sorted

        // touch / write / read
        check("touch file",  fs.touch("/a/b/note.txt"), true);
        check("write file",  fs.write("/a/b/note.txt", "hello"), true);
        check("read file",   fs.read("/a/b/note.txt"), "hello");
        check("write create", fs.write("/a/b/new.txt", "world"), true);
        check("read create", fs.read("/a/b/new.txt"), "world");
        check("read missing", fs.read("/a/b/none"), null);
        check("ls dir",      fs.ls("/a/b"), List.of("c", "new.txt", "note.txt"));
        check("ls file",     fs.ls("/a/b/note.txt"), List.of("note.txt"));

        // a file blocks dir creation; can't write over a dir
        check("touch f",     fs.touch("/a/f"), true);
        check("mkdir under file", fs.mkdir("/a/f/x"), false);
        check("write over dir", fs.write("/a/b", "x"), false);

        // mv rename, then mv into a directory
        check("mv rename",   fs.mv("/a/b/note.txt", "/a/b/renamed.txt"), true);
        check("read renamed", fs.read("/a/b/renamed.txt"), "hello");
        check("old name gone", fs.read("/a/b/note.txt"), null);
        check("mkdir dest",  fs.mkdir("/dest"), true);
        check("mv into dir", fs.mv("/a/b/renamed.txt", "/dest"), true);
        check("ls dest",     fs.ls("/dest"), List.of("renamed.txt"));
        check("mv dir into own subtree", fs.mv("/a", "/a/b"), false);
        check("mv missing",  fs.mv("/nope", "/dest"), false);

        // rm recursive
        check("rm subtree",  fs.rm("/a/b"), true);
        check("ls after rm", fs.ls("/a"), List.of("f", "x"));   // b (and its subtree) gone
        check("rm missing",  fs.rm("/a/b"), false);
        check("rm root",     fs.rm("/"), false);

        System.out.println("\nPassed " + passed + ", Failed " + failed);
        if (failed == 0) System.out.println("ALL FILE-SYSTEM-TREE TESTS PASSED");
    }
}
