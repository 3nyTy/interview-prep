import java.util.*;

/**
 * Robinhood — "Implement Android findViewById"
 * Source: 1point3acres 20f0926e-8b1b-4160-b794-3e5c1f012212
 *
 * "Implement findViewById(int id) in Android. Each View has a unique ID. Find and
 *  return the corresponding View. Describe the implementation and PROVIDE UNIT
 *  TESTS to verify correctness."
 *
 * ---------------------------------------------------------------------------
 * ⭐ THE PROBLEM ASKS FOR TESTS EXPLICITLY, so half the grade is the test list,
 * not the traversal. The traversal is ten lines. Candidates who write the
 * traversal and stop have answered half the question.
 *
 * The real Android behaviour is DEPTH-FIRST PRE-ORDER, checking self before
 * children. State the traversal order out loud — with duplicate ids (which the
 * framework permits even though the problem says unique) the ORDER *is* the
 * answer.
 * ---------------------------------------------------------------------------
 */
public class FindViewById {

    /** Minimal stand-in for the framework types. A ViewGroup has children; a View does not. */
    static class View {
        final int id;
        final String tag;
        View(int id) { this(id, null); }
        View(int id, String tag) { this.id = id; this.tag = tag; }
        int getId() { return id; }
        @Override public String toString() { return (tag != null ? tag : "View") + "#" + id; }
    }

    static class ViewGroup extends View {
        private final List<View> children = new ArrayList<>();
        ViewGroup(int id) { super(id); }
        ViewGroup(int id, String tag) { super(id, tag); }
        ViewGroup add(View v) { children.add(v); return this; }
        int getChildCount() { return children.size(); }
        View getChildAt(int i) { return children.get(i); }
    }

    static final int NO_ID = -1;

    /* ===================================================================
     * PART 1 — ⭐ Recursive depth-first pre-order. THE ANSWER.
     *
     * Check self, then recurse into children left to right, returning the first
     * match. That ordering is what real Android does.
     *
     * The instanceof check is the only structural subtlety: a plain View has no
     * children, so the recursion terminates there.
     * =================================================================== */
    static View part1(View root, int id) {
        if (root == null) return null;
        if (root.getId() == id) return root;            // self BEFORE children => pre-order
        if (!(root instanceof ViewGroup g)) return null; // leaf: nothing below it
        for (int i = 0; i < g.getChildCount(); i++) {
            View found = part1(g.getChildAt(i), id);
            if (found != null) return found;            // first match wins
        }
        return null;
    }

    /* ===================================================================
     * PART 2 — Iterative, same order.
     *
     * CHANGES FROM PART 1:
     *   - explicit Deque instead of the call stack
     *   - ⚠️ children are pushed in REVERSE so they POP left-to-right. Push them
     *     forwards and you get pre-order with children visited right-to-left,
     *     which silently returns a different View when ids repeat. That reversal
     *     is the whole correctness detail of this version.
     *
     * Offer this when asked about deeply nested layouts. Real view hierarchies
     * are shallow (tens of levels), so recursion is genuinely fine here — say
     * that rather than pretending the iterative form is required.
     * =================================================================== */
    static View part2Iterative(View root, int id) {
        if (root == null) return null;
        Deque<View> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            View v = stack.pop();
            if (v.getId() == id) return v;
            if (v instanceof ViewGroup g)
                for (int i = g.getChildCount() - 1; i >= 0; i--)   // ⭐ reverse push
                    stack.push(g.getChildAt(i));
        }
        return null;
    }

    /* ===================================================================
     * PART 3 — The O(1) version, and the trade-off to name.
     *
     * CHANGES FROM PART 2:
     *   - an id -> View index built once per hierarchy
     *
     * O(1) lookup instead of O(n). ⚠️ The costs, which you must state or the
     * suggestion looks naive:
     *   - the index goes stale on addView/removeView, so it needs invalidation
     *   - it holds strong references to Views, so a stale index leaks the whole
     *     hierarchy (a real Android memory-leak class)
     *   - it only pays off when lookups outnumber mutations
     * Real Android does NOT do this — it walks the tree every time. Knowing why
     * (the tree mutates constantly and correctness beats speed here) is the
     * senior answer.
     * =================================================================== */
    static class IndexedHierarchy {
        private final Map<Integer, View> index = new HashMap<>();
        private final View root;

        IndexedHierarchy(View root) {
            this.root = root;
            reindex();
        }
        final void reindex() {
            index.clear();
            Deque<View> st = new ArrayDeque<>();
            if (root != null) st.push(root);
            while (!st.isEmpty()) {
                View v = st.pop();
                if (v.getId() != NO_ID) index.putIfAbsent(v.getId(), v);   // FIRST wins, matching pre-order
                if (v instanceof ViewGroup g)
                    for (int i = g.getChildCount() - 1; i >= 0; i--) st.push(g.getChildAt(i));
            }
        }
        View findViewById(int id) { return index.get(id); }
    }

    /* ===================================================================
     * PART 4 — ⭐ THE UNIT TESTS. The half of the question people skip.
     * =================================================================== */
    static int pass = 0, fail = 0;

    static void assertSame(String name, View actual, View expected) {
        boolean ok = actual == expected;
        System.out.printf("  %-46s %s%n", name, ok ? "PASS" : "FAIL <<< got " + actual + " want " + expected);
        if (ok) pass++; else fail++;
    }
    static void assertNull(String name, View actual) {
        boolean ok = actual == null;
        System.out.printf("  %-46s %s%n", name, ok ? "PASS" : "FAIL <<< got " + actual);
        if (ok) pass++; else fail++;
    }

    public static void main(String[] args) {
        //            root(1)
        //          /    |     \
        //     a(2)   b(3)[VG]  c(4)
        //                |  \
        //            d(5)   e(6)[VG]
        //                      |
        //                    f(7)
        View a = new View(2, "a"), c = new View(4, "c"), d = new View(5, "d"), f = new View(7, "f");
        ViewGroup e = new ViewGroup(6, "e"); e.add(f);
        ViewGroup b = new ViewGroup(3, "b"); b.add(d).add(e);
        ViewGroup root = new ViewGroup(1, "root"); root.add(a).add(b).add(c);

        System.out.println("--- 1. the id is on the root itself ---");
        assertSame("find(1) == root", part1(root, 1), root);

        System.out.println("--- 2. shallow child ---");
        assertSame("find(2) == a", part1(root, 2), a);
        assertSame("find(4) == c", part1(root, 4), c);

        System.out.println("--- 3. deep leaf ---");
        assertSame("find(7) == f (3 levels down)", part1(root, 7), f);

        System.out.println("--- 4. intermediate ViewGroup ---");
        assertSame("find(3) == b (a ViewGroup, not a leaf)", part1(root, 3), b);
        assertSame("find(6) == e", part1(root, 6), e);

        System.out.println("--- 5. id not present ---");
        assertNull("find(999) == null", part1(root, 999));

        System.out.println("--- 6. null root ---");
        assertNull("find on null root == null", part1(null, 1));

        System.out.println("--- 7. leaf View that is not a ViewGroup ---");
        assertSame("find(2) starting AT the leaf", part1(a, 2), a);
        assertNull("find(999) starting at a leaf", part1(a, 999));

        System.out.println("--- 8. empty ViewGroup ---");
        ViewGroup empty = new ViewGroup(50, "empty");
        assertSame("find(50) on childless ViewGroup", part1(empty, 50), empty);
        assertNull("find(51) on childless ViewGroup", part1(empty, 51));

        System.out.println("--- 9. ⭐ duplicate ids: FIRST in pre-order wins ---");
        // dup(8) appears twice: once shallow-right, once deeper-left.
        // Pre-order visits the LEFT subtree first, so deepLeft must win.
        View deepLeft = new View(8, "deepLeft");
        View shallowRight = new View(8, "shallowRight");
        ViewGroup left = new ViewGroup(20, "left"); left.add(deepLeft);
        ViewGroup dupRoot = new ViewGroup(10, "dupRoot"); dupRoot.add(left).add(shallowRight);
        assertSame("find(8) == deepLeft (left subtree first)", part1(dupRoot, 8), deepLeft);
        assertSame("iterative agrees", part2Iterative(dupRoot, 8), deepLeft);
        assertSame("indexed agrees", new IndexedHierarchy(dupRoot).findViewById(8), deepLeft);

        System.out.println("--- 10. NO_ID views are skipped, not matched ---");
        ViewGroup noIdRoot = new ViewGroup(NO_ID, "unnamed");
        View target = new View(60, "target");
        noIdRoot.add(target);
        assertSame("find(60) through a NO_ID parent", part1(noIdRoot, 60), target);

        System.out.println("--- 11. all three implementations agree everywhere ---");
        IndexedHierarchy idx = new IndexedHierarchy(root);
        boolean allAgree = true;
        for (int id : new int[]{1, 2, 3, 4, 5, 6, 7, 999, -1}) {
            View r1 = part1(root, id), r2 = part2Iterative(root, id), r3 = idx.findViewById(id);
            if (r1 != r2 || r1 != r3) { allAgree = false; System.out.println("    MISMATCH on id " + id); }
        }
        System.out.printf("  %-46s %s%n", "recursive == iterative == indexed", allAgree ? "PASS" : "FAIL");
        if (allAgree) pass++; else fail++;

        System.out.println("--- 12. deep hierarchy (1000 levels) ---");
        ViewGroup deepRoot = new ViewGroup(0, "L0");
        ViewGroup cur = deepRoot;
        for (int i = 1; i < 1000; i++) { ViewGroup n = new ViewGroup(i, "L" + i); cur.add(n); cur = n; }
        assertSame("find(999) at depth 1000 (recursive)", part1(deepRoot, 999), cur);
        assertSame("find(999) at depth 1000 (iterative)", part2Iterative(deepRoot, 999), cur);
        System.out.println("  (real layouts are tens of levels deep, so recursion is fine —");
        System.out.println("   but say so rather than assuming it)");

        System.out.println("\n===== " + pass + " passed, " + fail + " failed =====");
    }
}
