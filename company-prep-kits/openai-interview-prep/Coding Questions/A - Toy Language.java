/*
 * ============================================================================
 * Toy Language — Type System (OpenAI Staff-SWE interview question)
 * ============================================================================
 *
 * We model types of a toy language:
 *   - Primitives:  int, float, str
 *   - Generics:    an uppercase letter with an optional number  (T, T1, S, ...)
 *   - Tuples:      a bracketed list of types, possibly nested   ([int, T1, [str, T2]])
 *   - Functions:   parameters + a return type                   ([int, T1] -> [T1, str])
 *
 * The core type is `Node`, which is EITHER:
 *   - a base node      (a single string: a primitive OR a generic), or
 *   - a tuple node     (an ordered list of child Nodes).
 * A `Function` holds a list of parameter Nodes plus one return-type Node.
 *
 * ---------------------------------------------------------------------------
 * PART 1 — String representation
 *   Node.toString():  base  -> the name as-is ("int", "T1")
 *                     tuple -> "[" + children joined by "," + "]"   (NO spaces)
 *   Function.toString(): "(p1,p2,...) -> returnType"
 *                     SPACES appear ONLY around the arrow "->",
 *                     never after commas or inside brackets.
 *
 * PART 2 — Type inference with generic substitution
 *   getReturnType(actualParams, function): given CONCRETE actual parameter
 *   types, bind the function's generics, then return the function's return
 *   type with every generic replaced by the concrete type it was bound to.
 *   Errors (IllegalArgumentException) are raised for:
 *     - argument-count mismatch
 *     - concrete-type mismatch              (expected int, got float)
 *     - generic conflict                    (T1 bound to int, then to str)
 *     - tuple-length mismatch during binding
 *
 * DESIGN NOTE (Java vs. the Python reference):
 *   The Python reference distinguishes "base vs. tuple" via truthiness
 *   (`if self.base`). That is subtly wrong for an empty tuple `[]` (whose
 *   `base` is None anyway, so it happens to work) but we make the rule
 *   explicit and robust in Java: a node is a BASE node iff `base != null`,
 *   otherwise it is a TUPLE node (even the empty tuple []). We use
 *   `base != null` EVERYWHERE rather than emulating truthiness.
 *
 * STRUCTURE:
 *   The top-level class `ToyLanguage` is package-private and only dispatches
 *   to the per-part demos. Each part lives in its own SELF-CONTAINED static
 *   nested class (Part1, Part2) with its OWN copies of Node/Function and its
 *   OWN `main`, so any single part can be lifted into a standalone file,
 *   promoted to a top-level `public class PartN`, and compiled/run unchanged.
 * ============================================================================
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
// NOTE: deliberately NOT importing java.util.function.Function — each part
// declares its own nested type named `Function`, and we want the unqualified
// name `Function` inside each part to resolve to that part's own type.

class ToyLanguage {

    public static void main(String[] args) {
        if (args.length == 0) {
            // No argument: run every part.
            Part1.main(args);
            Part2.main(args);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "part1" -> Part1.main(args);
            case "part2" -> Part2.main(args);
            default -> {
                System.out.println("Unknown part '" + args[0] + "'. Usage: ToyLanguage [part1|part2]");
                System.out.println("Running all parts instead:");
                Part1.main(args);
                Part2.main(args);
            }
        }
    }

    /* ========================================================================
     * PART 1 — String representation of Node and Function.
     *
     * Self-contained: its own Node + Function + test harness, no references to
     * any other part. To run standalone, copy this class out, rename it to a
     * top-level `public class Part1`, and `javac`/`java` it directly.
     * ====================================================================== */
    static final class Part1 {

        /** A type node: a base (primitive/generic) when base != null, else a tuple. */
        static final class Node {
            final String base;          // non-null => primitive/generic; null => tuple
            final List<Node> children;  // tuple children; empty list for a base node

            /** Base node: a primitive ("int") or generic ("T1"). */
            Node(String base) {
                this.base = base;
                this.children = new ArrayList<>();
            }

            /** Tuple node: an ordered list of child nodes (may be empty => "[]"). */
            Node(List<Node> children) {
                this.base = null;
                this.children = children;
            }

            @Override
            public String toString() {
                // Base: render the name verbatim.
                if (base != null) {
                    return base;
                }
                // Tuple: comma-joined children inside brackets, no spaces.
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < children.size(); i++) {
                    if (i > 0) sb.append(',');
                    sb.append(children.get(i));
                }
                return sb.append(']').toString();
            }
        }

        /** A function signature: parameter types plus a return type. */
        static final class Function {
            final List<Node> parameters;
            final Node outputType;

            Function(List<Node> parameters, Node outputType) {
                this.parameters = parameters;
                this.outputType = outputType;
            }

            @Override
            public String toString() {
                // "(p1,p2,...) -> returnType" — spaces ONLY around the arrow.
                StringBuilder sb = new StringBuilder("(");
                for (int i = 0; i < parameters.size(); i++) {
                    if (i > 0) sb.append(',');
                    sb.append(parameters.get(i));
                }
                return sb.append(") -> ").append(outputType).toString();
            }
        }

        // ---- tiny test harness (works without -ea / `assert`) ----
        static void check(String actual, String expected) {
            if (!actual.equals(expected)) {
                throw new AssertionError("expected <" + expected + "> but got <" + actual + ">");
            }
        }

        public static void main(String[] args) {
            // ----- Node string representation -----
            check(new Node("int").toString(), "int");
            check(new Node("T1").toString(), "T1");

            // [int, float] -> "[int,float]"
            check(new Node(List.of(new Node("int"), new Node("float"))).toString(), "[int,float]");

            // nested [int, [str, T1]] -> "[int,[str,T1]]"
            Node nested = new Node(List.of(
                    new Node("int"),
                    new Node(List.of(new Node("str"), new Node("T1")))));
            check(nested.toString(), "[int,[str,T1]]");

            // Edge cases: empty and single-element tuples.
            check(new Node(List.of()).toString(), "[]");
            check(new Node(List.of(new Node("int"))).toString(), "[int]");

            // Deeply nested [[T,float],T] (mirrors the reference __main__ node5).
            Node n1 = new Node("T");
            Node n2 = new Node("float");
            Node n3 = new Node("T");
            Node n4 = new Node(List.of(n1, n2));
            Node n5 = new Node(List.of(n4, n3));
            check(n5.toString(), "[[T,float],T]");

            // ----- Function string representation -----
            // params [int, T1], return [T1, str] => "(int,T1) -> [T1,str]"
            Function f1 = new Function(
                    List.of(new Node("int"), new Node("T1")),
                    new Node(List.of(new Node("T1"), new Node("str"))));
            check(f1.toString(), "(int,T1) -> [T1,str]");

            // Function([[T,float],T], S) return [S,T] => "([[T,float],T],S) -> [S,T]"
            Function f2 = new Function(
                    List.of(n5, new Node("S")),
                    new Node(List.of(new Node("S"), new Node("T"))));
            check(f2.toString(), "([[T,float],T],S) -> [S,T]");

            System.out.println("Part1: all checks passed");
        }
    }

    /* ========================================================================
     * PART 2 — Type inference with generic substitution.
     *
     * CHANGES FROM PART 1
     * -------------------
     * Part 2 keeps its OWN copies of Node and Function (it does NOT reference
     * Part1), and ADDS the machinery needed for inference:
     *   - PRIMITIVES set + isBaseGenericType() / isGenericType()
     *       isBaseGenericType(): base != null && not a primitive  (a bare generic)
     *       isGenericType():     this is a bare generic, OR any child is generic
     *   - clone(): deep copy of a node.
     *   - equals(Object)/hashCode(): structural equality via toString()
     *       (matches the reference __eq__, which compares str(self)==str(other)).
     *   - binding(funcParam, actualParam, map): recursive generic binding with
     *       the EXACT branch order of the reference solution (see below).
     *   - substitute(node, map): replace generics in the return type.
     *   - getReturnType(actualParams, function): the public entry point.
     *   - Four error conditions, all thrown as IllegalArgumentException.
     *
     * Self-contained: copy this class out, rename it to a top-level
     * `public class Part2`, and it compiles/runs unchanged.
     * ====================================================================== */
    static final class Part2 {

        /** Primitive (non-generic) base names. */
        static final Set<String> PRIMITIVES = Set.of("int", "float", "str");

        /** A type node: a base (primitive/generic) when base != null, else a tuple. */
        static final class Node {
            final String base;          // non-null => primitive/generic; null => tuple
            final List<Node> children;  // tuple children; empty list for a base node

            Node(String base) {
                this.base = base;
                this.children = new ArrayList<>();
            }

            Node(List<Node> children) {
                this.base = null;
                this.children = children;
            }

            /** True iff this is a bare generic base (a base that is not a primitive). */
            boolean isBaseGenericType() {
                return base != null && !PRIMITIVES.contains(base);
            }

            /** True iff this node, or any descendant, is generic. */
            boolean isGenericType() {
                if (isBaseGenericType()) {
                    return true;
                }
                for (Node child : children) {
                    if (child.isGenericType()) {
                        return true;
                    }
                }
                return false;
            }

            /** Deep copy. */
            Node clone_() {
                if (base != null) {
                    return new Node(base);
                }
                List<Node> copy = new ArrayList<>(children.size());
                for (Node child : children) {
                    copy.add(child.clone_());
                }
                return new Node(copy);
            }

            @Override
            public String toString() {
                if (base != null) {
                    return base;
                }
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < children.size(); i++) {
                    if (i > 0) sb.append(',');
                    sb.append(children.get(i));
                }
                return sb.append(']').toString();
            }

            // Structural equality via the canonical string form (matches the
            // reference __eq__: two nodes are equal iff their toString()s match).
            @Override
            public boolean equals(Object o) {
                return (o instanceof Node) && this.toString().equals(o.toString());
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }
        }

        /** A function signature: parameter types plus a return type. */
        static final class Function {
            final List<Node> parameters;
            final Node outputType;

            Function(List<Node> parameters, Node outputType) {
                this.parameters = parameters;
                this.outputType = outputType;
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder("(");
                for (int i = 0; i < parameters.size(); i++) {
                    if (i > 0) sb.append(',');
                    sb.append(parameters.get(i));
                }
                return sb.append(") -> ").append(outputType).toString();
            }
        }

        /**
         * Recursively bind the generics in {@code funcParam} against the concrete
         * {@code actualParam}, accumulating bindings in {@code map}.
         *
         * Branch order EXACTLY mirrors the reference solution:
         *   1. funcParam is a BARE generic (isGenericType() && base != null):
         *        - if already bound to a different type -> conflict error
         *        - otherwise record the binding
         *   2. funcParam equals actualParam (concrete match) -> done
         *   3. both are tuples (base == null on each):
         *        - lengths must match (else tuple-length error)
         *        - recurse element-wise
         *   4. otherwise -> general mismatch error
         */
        static void binding(Node funcParam, Node actualParam, Map<String, Node> map) {
            if (funcParam.isGenericType() && funcParam.base != null) {
                // (1) Bare generic such as T1 / S.
                Node existing = map.get(funcParam.base);
                if (existing != null && !existing.equals(actualParam)) {
                    throw new IllegalArgumentException(
                            "generic type conflict: " + funcParam.base + " bound to <"
                                    + existing + "> but also <" + actualParam + ">");
                }
                if (existing == null) {
                    map.put(funcParam.base, actualParam);
                }
            } else if (funcParam.equals(actualParam)) {
                // (2) Concrete types match exactly (e.g. int == int).
                return;
            } else if (funcParam.base == null && actualParam.base == null) {
                // (3) Both tuples: lengths must agree, then recurse.
                if (funcParam.children.size() != actualParam.children.size()) {
                    throw new IllegalArgumentException(
                            "tuple length mismatch: <" + funcParam + "> vs <" + actualParam + ">");
                }
                for (int i = 0; i < funcParam.children.size(); i++) {
                    binding(funcParam.children.get(i), actualParam.children.get(i), map);
                }
            } else {
                // (4) Anything else is a concrete mismatch (e.g. int vs float,
                //     or a base vs. a tuple).
                throw new IllegalArgumentException(
                        "type mismatch on <" + funcParam + "> and <" + actualParam + ">");
            }
        }

        /**
         * Return a copy of {@code node} with every generic replaced by the
         * concrete type recorded in {@code map}. Non-generic nodes are deep-copied
         * as-is; a bare generic becomes a copy of its bound type; a tuple recurses.
         */
        static Node substitute(Node node, Map<String, Node> map) {
            if (!node.isGenericType()) {
                return node.clone_();
            }
            if (node.base != null) {
                // Bare generic -> the concrete type it was bound to (deep-copied).
                return map.get(node.base).clone_();
            }
            // Tuple containing generics -> substitute into each child.
            List<Node> replaced = new ArrayList<>(node.children.size());
            for (Node child : node.children) {
                replaced.add(substitute(child, map));
            }
            return new Node(replaced);
        }

        /**
         * Resolve the concrete return type of {@code function} when invoked with
         * the concrete {@code parameters}.
         */
        static Node getReturnType(List<Node> parameters, Function function) {
            if (parameters.size() != function.parameters.size()) {
                throw new IllegalArgumentException(
                        "argument count mismatch: expected " + function.parameters.size()
                                + " but got " + parameters.size());
            }
            Map<String, Node> map = new HashMap<>();
            for (int i = 0; i < parameters.size(); i++) {
                binding(function.parameters.get(i), parameters.get(i), map);
            }
            if (!function.outputType.isGenericType()) {
                // Fully concrete return type — nothing to substitute.
                return function.outputType;
            }
            return substitute(function.outputType, map);
        }

        // ---- tiny test harness (works without -ea / `assert`) ----
        static void check(String actual, String expected) {
            if (!actual.equals(expected)) {
                throw new AssertionError("expected <" + expected + "> but got <" + actual + ">");
            }
        }

        static void expectThrows(Runnable r, String label) {
            try {
                r.run();
            } catch (RuntimeException e) {
                return; // expected
            }
            throw new AssertionError(label + ": expected exception but none thrown");
        }

        public static void main(String[] args) {
            // ----- toString still works in Part 2's own copies (regression) -----
            check(new Node("int").toString(), "int");
            check(new Node(List.of(new Node("int"),
                    new Node(List.of(new Node("str"), new Node("T1"))))).toString(),
                    "[int,[str,T1]]");

            // ===== Example 1: valid inference =====
            // func: [T1, T2, int, T1] -> [T1, T2]
            Function func1 = new Function(
                    List.of(new Node("T1"), new Node("T2"), new Node("int"), new Node("T1")),
                    new Node(List.of(new Node("T1"), new Node("T2"))));
            // actual: [int, str, int, int]  =>  [int, str]
            Node r1 = getReturnType(
                    List.of(new Node("int"), new Node("str"), new Node("int"), new Node("int")),
                    func1);
            check(r1.toString(), "[int,str]");

            // ===== Example 2: concrete mismatch (3rd expects int, got float) =====
            expectThrows(() -> getReturnType(
                    List.of(new Node("int"), new Node("str"), new Node("float"), new Node("int")),
                    func1), "Example 2 (concrete mismatch)");

            // ===== Example 3: generic conflict (T1 bound int then str) =====
            expectThrows(() -> getReturnType(
                    List.of(new Node("int"), new Node("str"), new Node("int"), new Node("str")),
                    func1), "Example 3 (generic conflict)");

            // ===== Example 4: nested tuples =====
            // func: [[T1, float], T1] -> [T1, [T1, float]]
            Function func4 = new Function(
                    List.of(
                            new Node(List.of(new Node("T1"), new Node("float"))),
                            new Node("T1")),
                    new Node(List.of(
                            new Node("T1"),
                            new Node(List.of(new Node("T1"), new Node("float"))))));
            // actual: [[str, float], str]  =>  [str, [str, float]]
            Node r4 = getReturnType(
                    List.of(
                            new Node(List.of(new Node("str"), new Node("float"))),
                            new Node("str")),
                    func4);
            check(r4.toString(), "[str,[str,float]]");

            // ===== Example 5: multiple generics =====
            // func: [[T1, float], T2, S] -> [S, T1]
            Function func5 = new Function(
                    List.of(
                            new Node(List.of(new Node("T1"), new Node("float"))),
                            new Node("T2"),
                            new Node("S")),
                    new Node(List.of(new Node("S"), new Node("T1"))));
            // actual: [[str, float], [int, str], int]  =>  T1=str, T2=[int,str], S=int  =>  [int, str]
            Node r5 = getReturnType(
                    List.of(
                            new Node(List.of(new Node("str"), new Node("float"))),
                            new Node(List.of(new Node("int"), new Node("str"))),
                            new Node("int")),
                    func5);
            check(r5.toString(), "[int,str]");

            // ===== __main__ inference example from the reference =====
            // node5 = [[T,float],T];  func = Function([node5, S], [S, T])
            Node n1 = new Node("T");
            Node n2 = new Node("float");
            Node n3 = new Node("T");
            Node n4 = new Node(List.of(n1, n2));
            Node n5 = new Node(List.of(n4, n3));
            Function funcMain = new Function(
                    List.of(n5, new Node("S")),
                    new Node(List.of(new Node("S"), new Node("T"))));
            // actual: first = [[str,float],str], second = [float,int]
            //   => T = str, S = [float,int]  => [S, T] = [[float,int],str]
            Node firstActual = new Node(List.of(
                    new Node(List.of(new Node("str"), new Node("float"))),
                    new Node("str")));
            Node secondActual = new Node(List.of(new Node("float"), new Node("int")));
            Node rMain = getReturnType(List.of(firstActual, secondActual), funcMain);
            check(rMain.toString(), "[[float,int],str]");

            // ===== Additional edge cases =====

            // (a) Argument-count mismatch -> throws.
            expectThrows(() -> getReturnType(
                    List.of(new Node("int")), func1), "arg-count mismatch");

            // (b) Tuple-length mismatch during binding: func [[T1,float]] vs actual [[str]].
            Function funcTupLen = new Function(
                    List.of(new Node(List.of(new Node("T1"), new Node("float")))),
                    new Node("T1"));
            expectThrows(() -> getReturnType(
                    List.of(new Node(List.of(new Node("str")))), funcTupLen),
                    "tuple-length mismatch");

            // (c) Fully-concrete return type: func [T1] -> int, actual [str]  =>  "int".
            Function funcConcreteRet = new Function(
                    List.of(new Node("T1")), new Node("int"));
            check(getReturnType(List.of(new Node("str")), funcConcreteRet).toString(), "int");

            // (d) Generic bound to a tuple: func [T2] -> T2, actual [[int,str]]  =>  "[int,str]".
            Function funcGenericTuple = new Function(
                    List.of(new Node("T2")), new Node("T2"));
            check(getReturnType(
                    List.of(new Node(List.of(new Node("int"), new Node("str")))),
                    funcGenericTuple).toString(), "[int,str]");

            // (e) Same generic used consistently many times: func [T1,T1,T1] -> T1, actual [int,int,int].
            Function funcRepeat = new Function(
                    List.of(new Node("T1"), new Node("T1"), new Node("T1")), new Node("T1"));
            check(getReturnType(
                    List.of(new Node("int"), new Node("int"), new Node("int")),
                    funcRepeat).toString(), "int");

            // (f) Empty-tuple binding length check: func [[]] vs actual [[int]] -> throws.
            Function funcEmptyTup = new Function(
                    List.of(new Node(List.of())), new Node("int"));
            expectThrows(() -> getReturnType(
                    List.of(new Node(List.of(new Node("int")))), funcEmptyTup),
                    "empty-vs-nonempty tuple mismatch");
            // ...but empty matches empty fine.
            check(getReturnType(List.of(new Node(List.of())), funcEmptyTup).toString(), "int");

            System.out.println("Part2: all checks passed");
        }
    }
}
