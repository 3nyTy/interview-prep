/*
 * Implement a CD Command — OpenAI Staff SWE coding question.
 *
 * Implement a cd() that behaves like running `cd <destination>` in a terminal
 * and then printing pwd: return the final normalized ABSOLUTE path as a string.
 * Pure path resolution — the real filesystem is NEVER touched.
 *
 * Special components handled throughout: ".." (parent), "." (stay), repeated
 * slashes ("foo//bar"), trailing slashes ("../A/B/"), and mixed navigation
 * ("./A/", "../A/B/../"). Navigating above "/" keeps you at "/".
 *
 * ------------------------------------------------------------------------
 * PARTS (each is a self-contained static nested class with its OWN main and
 * its OWN duplicated helpers — no cross-part references):
 * ------------------------------------------------------------------------
 *   Part1 — Normalize a relative destination against an absolute current_dir.
 *           Stack algorithm: seed the stack from current_dir, then fold the
 *           destination tokens in (skip ""/".", pop on "..", else push).
 *
 *   Part2 — Add absolute destinations ("/etc/nginx") and "~" / "~/..." (home).
 *           First resolve to ONE absolute path string, then run the SAME stack
 *           normalizer from an empty stack.
 *
 *   Part3 — Add symbolic links (Map<symlinkPath, realTarget>). ORDER MATTERS:
 *           do NOT fully normalize before resolving symlinks. Build the path
 *           keeping "." and "..", process tokens left-to-right, and after
 *           appending each NORMAL name check whether the current prefix is a
 *           symlink; if so, splice in the (normalized) target and keep going.
 *           Cycles are detected with a seen-set.
 *
 *   Part4 — CONCEPTUAL: how a real shell `cd` works (built-in rationale, shell
 *           flow, the filesystem tree + inodes, path resolution, symlinks).
 *           Prints a structured explanation; performs no syscalls.
 *
 * A top-level package-private class `CdCommand` holds the four parts plus a
 * dispatcher main(). The on-disk filename ("A - Implement a CD Command.java")
 * intentionally does NOT match the class name; that is legal because the
 * top-level class is package-private (the public-class/filename rule does not
 * apply).
 *
 * COMPILE / RUN (filename has spaces + hyphen, so copy to CdCommand.java first
 * OR use -d to an output dir):
 *   cp "A - Implement a CD Command.java" CdCommand.java
 *   javac CdCommand.java
 *   java CdCommand            # runs all four parts in order (default)
 *   java CdCommand part1      # runs only Part1 (likewise part2/part3/part4)
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Top-level dispatcher. Package-private (NOT public) on purpose so the source
 * filename need not match the class name. Each PartN below is fully
 * self-contained: the ONLY cross-class references in the whole file are this
 * dispatcher calling PartN.main(...).
 */
class CdCommand {

    public static void main(String[] args) {
        if (args.length == 0) {
            // Default: run every part in order.
            Part1.main(new String[0]);
            Part2.main(new String[0]);
            Part3.main(new String[0]);
            Part4.main(new String[0]);
            return;
        }
        switch (args[0]) {
            case "part1" -> Part1.main(new String[0]);
            case "part2" -> Part2.main(new String[0]);
            case "part3" -> Part3.main(new String[0]);
            case "part4" -> Part4.main(new String[0]);
            default -> {
                System.out.println("Usage: java CdCommand [part1|part2|part3|part4]"
                        + "  (no argument runs all four). Unknown arg '" + args[0]
                        + "' — running all parts.");
                Part1.main(new String[0]);
                Part2.main(new String[0]);
                Part3.main(new String[0]);
                Part4.main(new String[0]);
            }
        }
    }

    // ====================================================================
    // PART 1 — Normalize a relative destination.
    // ====================================================================
    /**
     * Part1 — fully self-contained. Seed a stack with current_dir's segments,
     * then fold the (relative) destination's tokens into it.
     *
     * <p>Copying this class to its own top-level {@code class Part1 { ... }}
     * (dropping {@code static}) compiles and runs unchanged: it references
     * nothing outside itself.
     */
    static final class Part1 {

        /**
         * cd against a relative destination.
         *
         * <p>Complexity: Time O(n + m) where n, m are the lengths of currentDir
         * and destination (each character is processed a constant number of
         * times). Space O(k) for the k path segments held in the stack.
         *
         * @param currentDir  absolute, already-normalized (e.g. "/usr/local")
         * @param destination relative path (may contain "."/".."//, trailing /)
         * @return the resulting absolute normalized path
         */
        private static String cd(String currentDir, String destination) {
            // ArrayList used as a stack: add == push, remove(size-1) == pop.
            List<String> stack = new ArrayList<>();

            // Seed the stack from the current directory's segments. Splitting
            // an absolute path yields a leading "" (before the first "/"),
            // which is skipped by the isEmpty() guard.
            for (String seg : currentDir.split("/")) {
                if (!seg.isEmpty()) {
                    stack.add(seg);
                }
            }

            // Fold in the destination tokens.
            for (String token : destination.split("/")) {
                if (token.isEmpty() || token.equals(".")) {
                    // "" comes from // or trailing /; "." means "stay".
                    continue;
                }
                if (token.equals("..")) {
                    // Pop one level if possible; above "/" we simply stay at "/".
                    if (!stack.isEmpty()) {
                        stack.remove(stack.size() - 1);
                    }
                    continue;
                }
                stack.add(token);
            }

            // Join back to an absolute path. Empty stack == root.
            if (stack.isEmpty()) {
                return "/";
            }
            return "/" + String.join("/", stack);
        }

        // --- Test harness (no `assert`; throws on mismatch). ---

        /** Throw with full context unless expected equals actual. */
        private static void check(String label, String expected, String actual) {
            if (!expected.equals(actual)) {
                throw new RuntimeException(
                        "Part1 check FAILED [" + label + "]: expected='"
                                + expected + "' actual='" + actual + "'");
            }
        }

        public static void main(String[] args) {
            // All 8 examples from the prompt.
            check("bin",            "/usr/local/bin", cd("/usr/local", "bin"));
            check("../bin",         "/usr/bin",       cd("/usr/local", "../bin"));
            check("../A/B/",        "/usr/A/B",       cd("/usr/local", "../A/B/"));
            check("./A/",           "/usr/local/A",   cd("/usr/local", "./A/"));
            check("../A/B/../",     "/usr/A",         cd("/usr/local", "../A/B/../"));
            check("../../..",       "/",              cd("/usr/local", "../../.."));
            check("root ..",        "/",              cd("/", ".."));
            check("../../x/./y//",  "/a/x/y",         cd("/a/b/c", "../../x/./y//"));

            System.out.println("Part1: all checks passed");
        }
    }

    // ====================================================================
    // PART 2 — Absolute paths and ~.
    // ====================================================================
    /**
     * Part2 — fully self-contained.
     *
     * <p>CHANGES FROM PART 1:
     * <ul>
     *   <li>The destination is no longer assumed relative. We first DISPATCH it
     *       into a single absolute path string:
     *       <ul>
     *         <li>"~"            -&gt; home_dir</li>
     *         <li>"~/..."        -&gt; home_dir + "/" + rest</li>
     *         <li>other "~..."   -&gt; error (only ~ and ~/... are supported)</li>
     *         <li>"/..."         -&gt; destination as-is (absolute)</li>
     *         <li>else           -&gt; current_dir + "/" + destination (relative)</li>
     *       </ul></li>
     *   <li>Then we run the SAME stack normalizer, but starting from an EMPTY
     *       stack (the path is already absolute), instead of seeding from
     *       current_dir. {@code normalizeAbsolutePath} is that normalizer.</li>
     * </ul>
     */
    static final class Part2 {

        /**
         * cd with home-directory and absolute-path support.
         *
         * <p>Complexity: Time O(L) where L is the length of the combined
         * absolute path string that is normalized. Space O(k) for the k
         * normalized path segments.
         *
         * @param currentDir  absolute, already-normalized current directory
         * @param destination relative, absolute, or "~"/"~/..."
         * @param homeDir      absolute home directory for "~" expansion
         * @return the resulting absolute normalized path
         */
        private static String cd(String currentDir, String destination, String homeDir) {
            String path;
            if (destination.equals("~")) {
                path = homeDir;
            } else if (destination.startsWith("~/")) {
                // Concatenate and let the normalizer collapse any extra slashes.
                path = homeDir + "/" + destination.substring(2);
            } else if (destination.startsWith("~")) {
                // e.g. "~bob" — other users' homes are out of scope.
                throw new IllegalArgumentException("Only ~ and ~/... are supported");
            } else if (destination.startsWith("/")) {
                path = destination;
            } else {
                path = currentDir + "/" + destination;
            }
            return normalizeAbsolutePath(path);
        }

        /**
         * Collapse an absolute path string using the stack algorithm, starting
         * from an EMPTY stack (the input already encodes its own absolute root).
         */
        private static String normalizeAbsolutePath(String path) {
            List<String> stack = new ArrayList<>();
            for (String token : path.split("/")) {
                if (token.isEmpty() || token.equals(".")) {
                    continue;
                }
                if (token.equals("..")) {
                    if (!stack.isEmpty()) {
                        stack.remove(stack.size() - 1);
                    }
                    continue;
                }
                stack.add(token);
            }
            if (stack.isEmpty()) {
                return "/";
            }
            return "/" + String.join("/", stack);
        }

        // --- Test harness (no `assert`; throws on mismatch). ---

        private static void check(String label, String expected, String actual) {
            if (!expected.equals(actual)) {
                throw new RuntimeException(
                        "Part2 check FAILED [" + label + "]: expected='"
                                + expected + "' actual='" + actual + "'");
            }
        }

        public static void main(String[] args) {
            // The 4 examples from the prompt.
            check("absolute",  "/etc/nginx",          cd("/usr/local", "/etc/nginx", "/home/alice"));
            check("~",         "/home/alice",         cd("/usr/local", "~",          "/home/alice"));
            check("~/projects","/home/alice/projects",cd("/usr/local", "~/projects", "/home/alice"));
            check("../docs",   "/docs",               cd("/tmp",       "../docs",    "/home/alice"));

            // Sanity: a plain relative destination still behaves like Part 1.
            check("relative",  "/usr/local/bin",      cd("/usr/local", "bin",        "/home/alice"));

            // "~bob" (other-user home) must be rejected.
            boolean threw = false;
            try {
                cd("/usr/local", "~bob", "/home/alice");
            } catch (IllegalArgumentException e) {
                threw = true;
            }
            if (!threw) {
                throw new RuntimeException("Part2 check FAILED [~bob]: expected an "
                        + "IllegalArgumentException but none was thrown");
            }

            System.out.println("Part2: all checks passed");
        }
    }

    // ====================================================================
    // PART 3 — Symbolic links.
    // ====================================================================
    /**
     * Part3 — fully self-contained.
     *
     * <p>CHANGES FROM PART 2: symlink resolution MUST interleave with token
     * processing — we can no longer fully normalize the path up front. Consider
     * destination "latest/logs/../config" where "/home/alice/latest" is a
     * symlink to "/mnt/releases/2026-03-01". If we collapsed ".." lexically
     * first we would cancel "logs" against ".." and lose the fact that "latest"
     * is a symlink that should expand BEFORE the ".." applies. Instead we walk
     * tokens left-to-right keeping "." and ".."; the moment the running prefix
     * (after pushing a normal name) matches a symlink key, we substitute the
     * normalized target for that prefix and reprocess the rest. This mirrors how
     * the kernel resolves a path one component at a time, following links as it
     * meets them. A seen-set detects cycles.
     *
     * <p>So here:
     * <pre>
     *   stack=[], remaining=[latest, logs, .., config]
     *   push "latest"  -> "/home/alice/latest" is a symlink
     *                     target "/mnt/releases/2026-03-01"
     *                     stack=[], remaining=[mnt, releases, 2026-03-01, logs, .., config]
     *   push mnt, releases, 2026-03-01, logs ; ".." pops "logs" ; push config
     *   => "/mnt/releases/2026-03-01/config"
     * </pre>
     */
    static final class Part3 {

        /** (stack, remaining) pair returned by the initial dispatch. */
        private record BuildState(List<String> stack, List<String> remaining) {}

        /**
         * cd with home, absolute paths, AND symbolic-link resolution.
         *
         * <p>Complexity: without symlink expansion, Time is linear in the number
         * of path tokens, O(n). With expansion the worst case is O(n + s*r)
         * where s is the number of symlink expansions and r is the amount of
         * path reprocessing after each expansion (we reset the index to 0 and
         * re-walk the prefix). Space O(k) for the working stack plus the
         * seen-set of expanded symlink prefixes.
         *
         * @param currentDir  absolute, already-normalized current directory
         * @param destination relative, absolute, or "~"/"~/..."
         * @param homeDir      absolute home directory for "~" expansion
         * @param symlinks     map from an absolute symlink path to its target
         * @return the resulting absolute normalized (symlink-resolved) path
         */
        private static String cd(String currentDir, String destination, String homeDir,
                                 Map<String, String> symlinks) {
            BuildState init = buildInitialState(currentDir, destination, homeDir);
            List<String> stack = init.stack();
            List<String> remaining = init.remaining();

            Set<String> seen = new HashSet<>();
            int i = 0;
            while (i < remaining.size()) {
                String token = remaining.get(i);

                if (token.equals(".")) {
                    i++;
                    continue;
                }
                if (token.equals("..")) {
                    if (!stack.isEmpty()) {
                        stack.remove(stack.size() - 1);
                    }
                    i++;
                    continue;
                }

                // Normal directory name: push it, then test the new prefix.
                stack.add(token);
                String currentPath = stack.isEmpty() ? "/" : "/" + String.join("/", stack);

                if (symlinks.containsKey(currentPath)) {
                    if (seen.contains(currentPath)) {
                        throw new IllegalStateException(
                                "Symlink cycle detected at " + currentPath);
                    }
                    seen.add(currentPath);

                    // Replace the matched prefix with the symlink's target and
                    // reprocess everything still to the right of token i.
                    String target = normalizeAbsolutePath(symlinks.get(currentPath));
                    List<String> newRemaining = new ArrayList<>(splitPath(target));
                    // Copy the tail into a FRESH list to avoid aliasing the old
                    // backing array via subList.
                    newRemaining.addAll(new ArrayList<>(remaining.subList(i + 1, remaining.size())));
                    remaining = newRemaining;
                    stack = new ArrayList<>();
                    i = 0;
                    continue;
                }

                i++;
            }

            return stack.isEmpty() ? "/" : "/" + String.join("/", stack);
        }

        /** Split into non-empty tokens, KEEPING "." and ".." (unlike a full
         *  normalize). Used for raw destinations and symlink targets. */
        private static List<String> splitPath(String path) {
            List<String> tokens = new ArrayList<>();
            for (String token : path.split("/")) {
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
            }
            return tokens;
        }

        /** Fully collapse "."/".." in an absolute path. Used ONLY on symlink
         *  targets, which are themselves absolute and safe to normalize. */
        private static String normalizeAbsolutePath(String path) {
            List<String> stack = new ArrayList<>();
            for (String token : splitPath(path)) {
                if (token.equals(".")) {
                    continue;
                }
                if (token.equals("..")) {
                    if (!stack.isEmpty()) {
                        stack.remove(stack.size() - 1);
                    }
                    continue;
                }
                stack.add(token);
            }
            return stack.isEmpty() ? "/" : "/" + String.join("/", stack);
        }

        /**
         * Mirror Part 2's dispatch but return SEGMENT LISTS (fresh mutable
         * ArrayLists), keeping "." and ".." in {@code remaining} so the symlink
         * loop can resolve links before collapsing parents.
         */
        private static BuildState buildInitialState(String currentDir, String destination,
                                                    String homeDir) {
            if (destination.equals("~")) {
                return new BuildState(new ArrayList<>(), new ArrayList<>(splitPath(homeDir)));
            }
            if (destination.startsWith("~/")) {
                List<String> remaining = new ArrayList<>(splitPath(homeDir));
                remaining.addAll(splitPath(destination.substring(2)));
                return new BuildState(new ArrayList<>(), remaining);
            }
            if (destination.startsWith("~")) {
                throw new IllegalArgumentException("Only ~ and ~/... are supported");
            }
            if (destination.startsWith("/")) {
                return new BuildState(new ArrayList<>(), new ArrayList<>(splitPath(destination)));
            }
            return new BuildState(new ArrayList<>(splitPath(currentDir)),
                    new ArrayList<>(splitPath(destination)));
        }

        // --- Test harness (no `assert`; throws on mismatch). ---

        private static void check(String label, String expected, String actual) {
            if (!expected.equals(actual)) {
                throw new RuntimeException(
                        "Part3 check FAILED [" + label + "]: expected='"
                                + expected + "' actual='" + actual + "'");
            }
        }

        public static void main(String[] args) {
            // The prompt's worked example: "latest" expands, then ".." pops the
            // "logs" that was appended AFTER expansion.
            Map<String, String> sym1 = new HashMap<>();
            sym1.put("/home/alice/latest", "/mnt/releases/2026-03-01");
            check("latest/logs/../config",
                    "/mnt/releases/2026-03-01/config",
                    cd("/home/alice", "latest/logs/../config", "/home/alice", sym1));

            // (a) An absolute destination whose first component is a symlink.
            check("absolute via symlink",
                    "/var/data/archive/data",
                    cd("/anything", "/shortcut/data", "/home/alice",
                            Map.of("/shortcut", "/var/data/archive")));

            // (b) No symlinks present -> behaves like Part 2 / Part 1.
            check("no symlinks",
                    "/usr/bin",
                    cd("/usr/local", "../bin", "/home/alice", new HashMap<>()));

            // Cycle detection: "/a" -> "/a/b" re-derives the prefix "/a".
            //   stack=[], remaining=[a]
            //   push a -> "/a" is a symlink (not yet seen) -> seen={/a},
            //             target "/a/b" -> remaining=[a,b], stack=[], i=0
            //   push a -> "/a" is a symlink AND already in seen -> throw.
            boolean threw = false;
            try {
                cd("/", "/a", "/home/alice", Map.of("/a", "/a/b"));
            } catch (IllegalStateException e) {
                threw = true;
            }
            if (!threw) {
                throw new RuntimeException("Part3 check FAILED [cycle]: expected an "
                        + "IllegalStateException but none was thrown");
            }

            System.out.println("Part3: all checks passed");
        }
    }

    // ====================================================================
    // PART 4 — How real `cd` works in a shell (conceptual).
    // ====================================================================
    /**
     * Part4 — fully self-contained.
     *
     * <p>CHANGES FROM PART 3: this part is purely conceptual/printed systems
     * knowledge — it makes NO syscalls and does no string processing. It just
     * explains what an actual shell does when you type `cd`.
     */
    static final class Part4 {

        public static void main(String[] args) {
            String explanation = """
                ============================================================
                How a real `cd` works in a shell
                ============================================================

                SECTION 1 — Why `cd` is a shell built-in
                ------------------------------------------------------------
                * `cd` must change the current working directory (CWD) of the
                  CURRENT shell process.
                * The CWD is per-process state held by the kernel; chdir() only
                  affects the process that calls it.
                * If `cd` were an external program, the shell would fork() a
                  child, the child would chdir() and then exit — leaving the
                  parent shell's CWD completely unchanged.
                * Therefore `cd` is implemented INSIDE the shell (a built-in),
                  so the chdir() runs in the shell's own process.

                SECTION 2 — High-level shell flow for `cd <dest>`
                ------------------------------------------------------------
                1) Parse the command line and expand shell syntax: tilde (~ ->
                   $HOME), environment variables ($VAR), globs, and quote
                   removal — all BEFORE the path is interpreted.
                2) Resolve the destination, choosing logical vs physical:
                     - logical  (cd -L, the default): textual, computed against
                       $PWD, KEEPS symlinks in the path, collapses ".."
                       lexically.
                     - physical (cd -P): resolve symlinks against the real
                       filesystem so the result is fully canonical.
                3) Call chdir(path) — the OS interface that changes the
                   process's working directory.
                4) On success: set OLDPWD to the old PWD, set PWD to the new
                   directory, and export them (this is what enables `cd -`).
                5) On failure (no such directory / not a directory / permission
                   denied): print an error, return a non-zero status, and leave
                   PWD unchanged.

                SECTION 3 — Linux filesystem tree and inodes
                ------------------------------------------------------------
                * All directories form ONE tree rooted at "/".
                * A directory is a special file mapping NAMES to INODE NUMBERS
                  (these entries are called dentries / directory entries).
                * An inode stores a file's metadata: owner (uid/gid),
                  permissions, timestamps, size, link count, and pointers to the
                  data blocks — but NOT the file's name.
                * The NAME lives in the parent directory, which maps e.g. "bin"
                  to an inode number. Because the name is separate from the
                  inode, multiple names can point to the same inode — that is a
                  hard link (link count > 1).

                SECTION 4 — Path resolution (one segment at a time)
                ------------------------------------------------------------
                * Resolution starts at "/" for an absolute path, or at the CWD
                  for a relative path.
                * For EACH component the kernel:
                    - looks the name up in the current directory to get the next
                      inode,
                    - checks that inode is a directory and that the caller has
                      search/execute (x) permission on it,
                    - descends into it.
                * It proceeds one segment at a time until the path is exhausted
                  (success) or it hits an error: a missing component (ENOENT), a
                  non-directory used as a directory (ENOTDIR), or a permission
                  failure (EACCES).

                SECTION 5 — Symbolic links
                ------------------------------------------------------------
                * A symbolic link is a special file whose CONTENTS are another
                  pathname.
                * When resolution meets a symlink, the kernel substitutes the
                  link's target and continues:
                    - an ABSOLUTE target restarts resolution at "/",
                    - a RELATIVE target resolves against the link's own
                      directory.
                * This is exactly the interleaved substitution Part 3 performs in
                  user space (expand the link, then keep processing the rest).
                * To stop infinite loops the kernel BOUNDS the number of chained
                  symlinks (ELOOP, typically ~40 links) — the kernel analogue of
                  Part 3's seen-set cycle detection.
                """;
            System.out.println(explanation);
            System.out.println("Part4: explanation printed");
        }
    }
}
