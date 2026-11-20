import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Report Chain / Org Tree — Reddit Staff SWE coding question.
 *
 * DESIGN (the rubric's two hot-spots): (1) clean parsing of BOTH input shapes, and
 * (2) a hard separation between tree CONSTRUCTION and tree QUERYING. We build ONCE in
 * the constructor and then answer every part as an O(n)-or-better method call.
 *
 *   children : manager -> reports, in input order  (LinkedHashMap => deterministic iteration)
 *   parent   : report  -> manager                  (an upward pointer makes P3 & P4 trivial)
 *   root     : the single top-level manager
 *
 * Invariant that keeps every traversal null-safe: EVERY node (including leaves) is a key
 * in `children`, mapping to a possibly-empty list. So `children.get(x)` is never null.
 */
public class OrgChart {

    /** Canonical indent unit. 4 spaces / dashes are equally valid — CONFIRM with interviewer. */
    private static final String INDENT = "....";

    private final Map<String, List<String>> children; // manager -> reports (input order)
    private final Map<String, String> parent;          // report  -> manager
    private final String root;                          // single top-level manager

    // =============================================================== //
    //  CONSTRUCTION (Part 1) — one pass, validates the single-root      //
    // =============================================================== //
    public OrgChart(List<List<String>> relations) {
        this.children = new LinkedHashMap<>();
        this.parent = new HashMap<>();
        // First-appearance order lets us pick the root deterministically at the end.
        LinkedHashSet<String> appearance = new LinkedHashSet<>();

        for (List<String> row : relations) {
            if (row == null || row.isEmpty()) continue;
            String manager = row.get(0);
            appearance.add(manager);
            children.computeIfAbsent(manager, k -> new ArrayList<>());
            for (int i = 1; i < row.size(); i++) {
                String report = row.get(i);
                appearance.add(report);
                children.computeIfAbsent(report, k -> new ArrayList<>()); // leaves are keys too
                String prior = parent.get(report);
                if (prior != null) {
                    // A report with two DIFFERENT managers is not a tree.
                    if (!prior.equals(manager)) {
                        throw new IllegalArgumentException("Invalid tree: '" + report
                                + "' reports to both '" + prior + "' and '" + manager + "'");
                    }
                    continue; // duplicate edge — ignore
                }
                parent.put(report, manager);
                children.get(manager).add(report);
            }
        }

        // roots = names that never appear as a report. Exactly one is required.
        List<String> roots = new ArrayList<>();
        for (String name : appearance) {
            if (!parent.containsKey(name)) roots.add(name);
        }
        if (roots.size() != 1) {
            throw new IllegalArgumentException(
                    "Expected exactly one root, found " + roots.size() + ": " + roots);
        }
        this.root = roots.get(0);
    }

    /** Factory for the comma-joined string shape: ["A,B,C", "C,D", "B,E"]. */
    public static OrgChart fromChainStrings(List<String> chains) {
        List<List<String>> relations = new ArrayList<>();
        for (String chain : chains) {
            List<String> row = new ArrayList<>();
            for (String token : chain.split(",")) row.add(token.trim()); // trim handles "A, B"
            relations.add(row);
        }
        return new OrgChart(relations);
    }

    // =============================================================== //
    //  PART 1 — pretty-print the whole tree (pre-order DFS)             //
    // =============================================================== //
    public String renderFullChain() {
        List<String> lines = new ArrayList<>();
        collectSubtree(lines, root, 0);
        return String.join("\n", lines); // joined => no trailing newline
    }

    /** Emit `node` at `depth`, then each child at depth+1 (input order). Shared by Parts 1 & 3. */
    private void collectSubtree(List<String> lines, String node, int depth) {
        lines.add(indent(depth) + node);
        for (String child : children.get(node)) {
            collectSubtree(lines, child, depth + 1);
        }
    }

    // =============================================================== //
    //  PART 2 — all skip-level (manager, grandchild) pairs              //
    // =============================================================== //
    public List<String[]> allSkipLevelPairs() {
        List<String[]> pairs = new ArrayList<>();
        for (String manager : children.keySet()) {          // appearance order (LinkedHashMap)
            for (String report : children.get(manager)) {   // direct reports (1 level down)
                for (String grand : children.get(report)) { // their reports  (2 levels down)
                    pairs.add(new String[]{manager, grand});
                }
            }
        }
        return pairs;
    }

    /**
     * PART 2 (minority variant): are two employees in an ancestor/descendant relationship?
     * Returns {ancestor, descendant}, or null if neither reports (transitively) to the other.
     */
    public String[] ancestorDescendant(String a, String b) {
        requireKnown(a);
        requireKnown(b);
        if (isAncestor(a, b)) return new String[]{a, b};
        if (isAncestor(b, a)) return new String[]{b, a};
        return null;
    }

    /** True if `ancestor` lies strictly above `descendant` on the path to the root. */
    private boolean isAncestor(String ancestor, String descendant) {
        for (String cur = parent.get(descendant); cur != null; cur = parent.get(cur)) {
            if (cur.equals(ancestor)) return true;
        }
        return false;
    }

    // =============================================================== //
    //  PART 3 — lineage: root->target path, then target's subtree       //
    // =============================================================== //
    public String renderChainFor(String target) {
        requireKnown(target);
        // Reconstruct the root->target path by walking parents UP, then reversing.
        List<String> path = new ArrayList<>();
        for (String cur = target; cur != null; cur = parent.get(cur)) path.add(cur);
        Collections.reverse(path); // now [root, ..., target]

        List<String> lines = new ArrayList<>();
        // The upward chain: root@0, ..., target@(path.size()-1).
        for (int depth = 0; depth < path.size(); depth++) {
            lines.add(indent(depth) + path.get(depth));
        }
        // Then DFS ONLY the target's children, starting one level below the target, so the
        // target is printed exactly once (as the last node of the path). This is the #1
        // error spot: do NOT re-print the target when descending into its subtree.
        int targetDepth = path.size() - 1;
        for (String child : children.get(target)) {
            collectSubtree(lines, child, targetDepth + 1);
        }
        return String.join("\n", lines);
    }

    // =============================================================== //
    //  PART 4 — lowest common manager (textbook tree LCA)               //
    // =============================================================== //

    /** Ancestor-set approach: simplest to explain. O(h) time, O(h) extra space. */
    public String lowestCommonManager(String e1, String e2) {
        requireKnown(e1);
        requireKnown(e2);
        Set<String> e1Ancestors = new HashSet<>();
        for (String cur = e1; cur != null; cur = parent.get(cur)) e1Ancestors.add(cur);
        // Walk up from e2; the FIRST node already seen on e1's chain is the lowest common one.
        for (String cur = e2; cur != null; cur = parent.get(cur)) {
            if (e1Ancestors.contains(cur)) return cur;
        }
        throw new IllegalStateException("No common manager (corrupt tree?)");
    }

    /** Two-pointer alternative: O(h) time, O(1) extra space (no ancestor set). */
    public String lowestCommonManagerTwoPointer(String e1, String e2) {
        requireKnown(e1);
        requireKnown(e2);
        int d1 = depthOf(e1), d2 = depthOf(e2);
        String a = e1, b = e2;
        while (d1 > d2) { a = parent.get(a); d1--; } // sink the deeper pointer to equal depth
        while (d2 > d1) { b = parent.get(b); d2--; }
        while (!a.equals(b)) {                        // then rise together until they meet
            a = parent.get(a);
            b = parent.get(b);
        }
        return a;
    }

    private int depthOf(String node) {
        int d = 0;
        for (String cur = parent.get(node); cur != null; cur = parent.get(cur)) d++;
        return d;
    }

    // =============================================================== //
    //  PART 5 — stretch variants (often skipped)                        //
    // =============================================================== //

    /** 5a: serialize back to list-of-lists (one row per manager that has >=1 report). */
    public List<List<String>> serialize() {
        List<List<String>> rows = new ArrayList<>();
        serializeDfs(root, rows); // pre-order => parents precede children
        return rows;
    }

    private void serializeDfs(String node, List<List<String>> rows) {
        List<String> kids = children.get(node);
        if (!kids.isEmpty()) {
            List<String> row = new ArrayList<>();
            row.add(node);
            row.addAll(kids);
            rows.add(row);
        }
        for (String kid : kids) serializeDfs(kid, rows);
    }

    /** 5b: employees EXACTLY n levels below `manager` (n level-order hops down). */
    public List<String> descendantsAtLevel(String manager, int n) {
        requireKnown(manager);
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");
        List<String> frontier = new ArrayList<>();
        frontier.add(manager); // level 0 == the manager itself
        for (int level = 0; level < n && !frontier.isEmpty(); level++) {
            List<String> next = new ArrayList<>();
            for (String node : frontier) next.addAll(children.get(node));
            frontier = next;
        }
        return frontier;
    }

    /**
     * 5c: validate raw input describes a single-rooted tree WITHOUT throwing.
     * Returns a human-readable reason, or null if valid. Catches: multiple managers,
     * multiple roots, a pure cycle (0 roots), and a disconnected cycle (root exists but
     * not everything is reachable from it).
     */
    public static String findStructuralError(List<List<String>> relations) {
        Map<String, String> parent = new HashMap<>();
        Map<String, List<String>> children = new HashMap<>();
        LinkedHashSet<String> names = new LinkedHashSet<>();

        for (List<String> row : relations) {
            if (row == null || row.isEmpty()) continue;
            String manager = row.get(0);
            names.add(manager);
            children.computeIfAbsent(manager, k -> new ArrayList<>());
            for (int i = 1; i < row.size(); i++) {
                String report = row.get(i);
                names.add(report);
                children.computeIfAbsent(report, k -> new ArrayList<>());
                String prior = parent.get(report);
                if (prior != null) {
                    if (!prior.equals(manager)) {
                        return "Employee '" + report + "' has two managers: '"
                                + prior + "' and '" + manager + "'";
                    }
                    continue;
                }
                parent.put(report, manager);
                children.get(manager).add(report);
            }
        }

        List<String> roots = new ArrayList<>();
        for (String name : names) if (!parent.containsKey(name)) roots.add(name);
        if (roots.isEmpty()) return "No root: every employee has a manager (cycle present)";
        if (roots.size() > 1) return "Multiple roots: " + roots;

        // Single root exists — confirm EVERY node is reachable from it (catches a stray cycle).
        String root = roots.get(0);
        Set<String> seen = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.add(root);
        seen.add(root);
        while (!queue.isEmpty()) {
            for (String c : children.get(queue.poll())) {
                if (seen.add(c)) queue.add(c);
            }
        }
        if (seen.size() != names.size()) {
            Set<String> unreachable = new LinkedHashSet<>(names);
            unreachable.removeAll(seen);
            return "Cycle/disconnected component, unreachable from root: " + unreachable;
        }
        return null; // valid
    }

    // =============================================================== //
    //  small helpers                                                    //
    // =============================================================== //

    /** INDENT repeated `depth` times. Written out (not String.repeat) to stay Java-8-safe. */
    private static String indent(int depth) {
        StringBuilder sb = new StringBuilder(depth * INDENT.length());
        for (int i = 0; i < depth; i++) sb.append(INDENT);
        return sb.toString();
    }

    private void requireKnown(String employee) {
        if (!children.containsKey(employee)) { // every node (incl. leaves) is a key
            throw new IllegalArgumentException("Unknown employee: " + employee);
        }
    }

    // Accessors — handy for tests / additional ad-hoc queries.
    public String getRoot() { return root; }
    public List<String> directReports(String manager) {
        requireKnown(manager);
        return Collections.unmodifiableList(children.get(manager));
    }

    // =============================================================== //
    //  demo / smoke test — mirrors the prompt's worked example          //
    // =============================================================== //
    public static void main(String[] args) {
        OrgChart org = OrgChart.fromChainStrings(Arrays.asList("A,B,C", "C,D", "B,E"));

        System.out.println("== Part 1: full chain ==");
        System.out.println(org.renderFullChain());

        System.out.println("\n== Part 2: all skip-level pairs ==");
        System.out.println(pairsToString(org.allSkipLevelPairs()));   // [(A, E), (A, D)]

        System.out.println("\n== Part 2 (variant): ancestorDescendant(A, E) ==");
        System.out.println(Arrays.toString(org.ancestorDescendant("A", "E"))); // [A, E]

        System.out.println("\n== Part 3: lineage for B ==");
        System.out.println(org.renderChainFor("B"));

        System.out.println("\n== Part 4: lowestCommonManager(C, E) ==");
        System.out.println(org.lowestCommonManager("C", "E")          // A
                + "  (two-pointer: " + org.lowestCommonManagerTwoPointer("C", "E") + ")");

        System.out.println("\n== Part 5a: serialize ==");
        System.out.println(org.serialize());                          // [[A, B, C], [B, E], [C, D]]

        System.out.println("\n== Part 5b: employees 2 levels below A ==");
        System.out.println(org.descendantsAtLevel("A", 2));           // [E, D]

        System.out.println("\n== Part 5c: validate inputs ==");
        System.out.println("multi-root : " + findStructuralError(
                Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("X", "Y"))));
        System.out.println("cycle      : " + findStructuralError(
                Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("B", "C"), Arrays.asList("C", "A"))));
        System.out.println("two-mgrs   : " + findStructuralError(
                Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("C", "B"))));
        System.out.println("valid      : " + findStructuralError(
                Arrays.asList(Arrays.asList("A", "B", "C"), Arrays.asList("C", "D"), Arrays.asList("B", "E"))));
    }

    /** Format List<String[]> like the prompt's Python tuple list: [(A, E), (A, D)]. */
    private static String pairsToString(List<String[]> pairs) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < pairs.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append("(").append(pairs.get(i)[0]).append(", ").append(pairs.get(i)[1]).append(")");
        }
        return sb.append("]").toString();
    }
}
