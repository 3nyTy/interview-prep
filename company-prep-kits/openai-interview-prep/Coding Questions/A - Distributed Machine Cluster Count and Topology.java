/*
 * ============================================================================
 * Distributed Machine Cluster: Count + Topology + Failure Handling
 * ============================================================================
 *
 * PROBLEM
 * -------
 * A cluster of machines is arranged as a TREE. Each machine has a unique
 * nodeId, a parent (null for the root), and a list of children ids. Machines
 * communicate ONLY with their direct parent/children through a black-box
 * async primitive:
 *
 *     sendAsyncMessage(nodeId, message)   // fire-and-forget, may reorder
 *     receiveMessage(fromNodeId, message) // fromNodeId == null => externally
 *                                         //   initiated (the kickoff)
 *
 * We implement three independent parts, each driven by a CONVERGECAST: the
 * root broadcasts a request down the tree; leaves answer first; each internal
 * node aggregates its children's answers and replies to its own parent; the
 * root produces the final answer.
 *
 *   PART 1 - Count Total Machines.
 *            Leaf replies count 1. Internal node seeds count = 1 (itself),
 *            forwards the request to every child, sums the responses, and once
 *            ALL children have answered, sends the total up (root prints it).
 *
 *   PART 2 - Discover Cluster Topology.
 *            Same convergecast, but each node aggregates a serialized subtree
 *            string. "1(2(3[]))" means node 1 has child 2, whose only child is
 *            leaf 3. A leaf is "id[]"; an internal node is "id(child,child)".
 *            Special case: a root that is also a leaf prints just "id".
 *
 *   PART 3 - Handle Message Failures (follow-up).
 *            "What if messages can be lost and are retried? Ensure correctness
 *            and avoid double counting." We make Count robust under a lossy,
 *            duplicating network using: a unique requestId on every message,
 *            per-request state keyed by requestId, idempotent request handling
 *            (dedup + cached-response replay), idempotent response handling
 *            (the pendingChildren set guarantees each child counts at most
 *            once), and bounded retries. We PROVE no double counting by
 *            asserting exact subtree totals after a deliberately lossy +
 *            duplicating run.
 *
 * DELIBERATE DUPLICATION ACROSS PARTS
 * -----------------------------------
 * Part1.Node, Part2.Node and Part3.Node are SEPARATE classes with NO shared
 * base class, and each part embeds its OWN message bus/harness and helpers.
 * This is intentional: the contract is that each Part is a fully
 * self-contained answer. You can copy any PartN's body verbatim into its own
 * file, promote it to a top-level `public class PartN`, and it compiles and
 * runs unchanged with ZERO references to the other parts or to any top-level
 * helper. The repetition is the feature, not an oversight.
 *
 * The on-disk file name intentionally differs from the class name; javac
 * allows a package-private (non-public) top-level class to live in a file of
 * any name, so this single file compiles fine on Java 21.
 * ============================================================================
 */

// NOTE: package-private (NOT public) on purpose, so the file name may differ
// from the class name.
class DistributedMachineClusterCountAndTopology {

    // ========================================================================
    // Top-level dispatch. Runs all three parts with no args, or a single part
    // selected by args[0]. Delegates to each part's own main; holds no logic.
    // ========================================================================
    public static void main(String[] args) {
        if (args.length == 0) {
            Part1.main(new String[0]);
            Part2.main(new String[0]);
            Part3.main(new String[0]);
        } else {
            switch (args[0]) {
                case "part1" -> Part1.main(new String[0]);
                case "part2" -> Part2.main(new String[0]);
                case "part3" -> Part3.main(new String[0]);
                default -> System.out.println("usage: part1|part2|part3 (or no arg for all)");
            }
        }
    }

    // ========================================================================
    // PART 1 - COUNT TOTAL MACHINES
    // ------------------------------------------------------------------------
    // Convergecast count. Self-contained: bus, node, message constants, tests.
    //
    // Complexity (tree of N nodes):
    //   Messages : 2*(N-1)  -- one COUNT_REQUEST down + one response up per edge
    //   Work     : O(N)     -- O(children) per node
    //   Space    : O(N)     -- O(children) of pending state per node
    //
    // Out-of-order delivery is tolerated because an internal node finalizes
    // and replies to its parent ONLY when its pendingChildren set is empty;
    // the arrival order of child responses is irrelevant. Duplicate responses
    // are ignored by the !pendingChildren.contains(from) guard.
    // ========================================================================
    static final class Part1 {

        // Message protocol: a single request token; responses are numeric.
        static final String COUNT_REQUEST = "COUNT_REQUEST";

        // --- Deterministic single-threaded async message bus --------------
        // sendAsyncMessage ENQUEUES a message (it does NOT call receiveMessage
        // directly), so delivery is asynchronous and reorderable. pump()
        // drains the queue FIFO. The root publishes its answer to `result`
        // (read by assertions) AND prints it (so the run is visibly correct).
        // Non-final so the out-of-order/duplicate stress test in main() can
        // subclass it and override pump().
        static class Bus {
            record Msg(String to, String from, String body) {}

            final java.util.Map<String, Node> registry = new java.util.HashMap<>();
            final java.util.ArrayDeque<Msg> queue = new java.util.ArrayDeque<>();
            String result;

            void register(Node n) { registry.put(n.nodeId, n); }
            void setResult(String r) { this.result = r; }
            String getResult() { return result; }

            // The black-box async primitive: fire-and-forget enqueue.
            void sendAsyncMessage(String to, String from, String body) {
                queue.addLast(new Msg(to, from, body));
            }

            // Deliver messages until the queue drains.
            void pump() {
                while (!queue.isEmpty()) {
                    Msg m = queue.removeFirst();
                    registry.get(m.to).receiveMessage(m.from, m.body);
                }
            }

            // Kickoff: externally-initiated request to the root (fromNodeId null).
            String run(String rootId) {
                registry.get(rootId).receiveMessage(null, COUNT_REQUEST);
                pump();
                return result;
            }
        }

        static final class Node {
            final String nodeId;
            final String parent;            // null => root
            final java.util.List<String> children;
            final Bus bus;

            int totalCount = 0;
            // LinkedHashSet => deterministic iteration; tracks who still owes a reply.
            java.util.Set<String> pendingChildren = new java.util.LinkedHashSet<>();

            Node(String nodeId, String parent, java.util.List<String> children, Bus bus) {
                this.nodeId = nodeId;
                this.parent = parent;
                this.children = children;
                this.bus = bus;
                bus.register(this);
            }

            // Convenience: send a message to a target, stamping this node as sender.
            void send(String target, String body) {
                bus.sendAsyncMessage(target, this.nodeId, body);
            }

            void receiveMessage(String from, String msg) {
                // A request must come from "outside" (root kickoff) or our parent.
                if (msg.equals(COUNT_REQUEST) && (from == null || from.equals(parent))) {
                    if (children.isEmpty()) {
                        // Leaf: its count is exactly 1.
                        if (parent == null) {                 // single-node tree
                            bus.setResult("1");
                            System.out.println(nodeId + " -> count = 1");
                        } else {
                            send(parent, "1");
                        }
                    } else {
                        // Internal node: count self, then ask every child.
                        totalCount = 1;
                        pendingChildren = new java.util.LinkedHashSet<>(children);
                        for (String child : children) {
                            send(child, COUNT_REQUEST);
                        }
                    }
                } else if (children.contains(from)) {
                    // A response (numeric) from one of our children.
                    if (!pendingChildren.contains(from)) {
                        return; // duplicate / already-counted reply: ignore (idempotent)
                    }
                    totalCount += Integer.parseInt(msg);
                    pendingChildren.remove(from);
                    if (pendingChildren.isEmpty()) {
                        // All children in => finalize.
                        if (parent == null) {
                            bus.setResult(String.valueOf(totalCount));
                            System.out.println(nodeId + " -> count = " + totalCount);
                        } else {
                            send(parent, String.valueOf(totalCount));
                        }
                        totalCount = 0; // reset for any future independent run
                    }
                }
                // else: stray message (unknown sender / unexpected) -> ignore.
            }
        }

        // THROW on mismatch (do not rely on -ea being enabled).
        static void assertEquals(String label, String expected, String actual) {
            if (!expected.equals(actual)) {
                throw new AssertionError(label + ": expected " + expected + " got " + actual);
            }
            System.out.println("  PASS " + label + " = " + actual);
        }

        public static void main(String[] args) {
            System.out.println("== Part1: Count ==");

            // single node "1" -> 1
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of(), bus);
                assertEquals("single", "1", bus.run("1"));
            }

            // 1 -> [2,3] -> 3
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of(), bus);
                new Node("3", "1", java.util.List.of(), bus);
                assertEquals("star-3", "3", bus.run("1"));
            }

            // chain 1 -> 2 -> 3 -> 3
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2"), bus);
                new Node("2", "1", java.util.List.of("3"), bus);
                new Node("3", "2", java.util.List.of(), bus);
                assertEquals("chain-3", "3", bus.run("1"));
            }

            // complete-7: 1->[2,3], 2->[4,5], 3->[6,7] -> 7
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of("4", "5"), bus);
                new Node("3", "1", java.util.List.of("6", "7"), bus);
                new Node("4", "2", java.util.List.of(), bus);
                new Node("5", "2", java.util.List.of(), bus);
                new Node("6", "3", java.util.List.of(), bus);
                new Node("7", "3", java.util.List.of(), bus);
                assertEquals("complete-7", "7", bus.run("1"));
            }

            // 6-node: 1->[2,3], 2->[4,5], 3->[6] -> 6
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of("4", "5"), bus);
                new Node("3", "1", java.util.List.of("6"), bus);
                new Node("4", "2", java.util.List.of(), bus);
                new Node("5", "2", java.util.List.of(), bus);
                new Node("6", "3", java.util.List.of(), bus);
                assertEquals("six", "6", bus.run("1"));
            }

            // Out-of-order + duplicate stress on the 6-node tree:
            // we reverse the queue once before each pump (to scramble delivery
            // order) and inject a duplicate "1" response into node 2. Result
            // must still be exactly 6 -- proving order-independence and that
            // duplicate responses are ignored.
            {
                Bus bus = new Bus() {
                    @Override void pump() {
                        // Scramble order, then deliver; duplicates are injected
                        // by re-delivering. We loop until stable.
                        while (!queue.isEmpty()) {
                            // reverse current contents to attack arrival order
                            java.util.ArrayList<Msg> snapshot = new java.util.ArrayList<>(queue);
                            queue.clear();
                            java.util.Collections.reverse(snapshot);
                            for (Msg m : snapshot) queue.addLast(m);

                            Msg m = queue.removeFirst();
                            registry.get(m.to).receiveMessage(m.from, m.body);
                            // Duplicate any response carrying "1" to node "2".
                            if ("2".equals(m.to) && "1".equals(m.body)) {
                                registry.get(m.to).receiveMessage(m.from, m.body);
                            }
                        }
                    }
                };
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of("4", "5"), bus);
                new Node("3", "1", java.util.List.of("6"), bus);
                new Node("4", "2", java.util.List.of(), bus);
                new Node("5", "2", java.util.List.of(), bus);
                new Node("6", "3", java.util.List.of(), bus);
                assertEquals("six-reordered-dup", "6", bus.run("1"));
            }

            System.out.println("Part1: ALL PASSED");
        }
    }

    // ========================================================================
    // PART 2 - DISCOVER CLUSTER TOPOLOGY
    // ------------------------------------------------------------------------
    // CHANGES FROM PART 1:
    //   * Aggregate a serialized subtree STRING instead of an int.
    //   * Child responses are stored keyed by child id in a LinkedHashMap and
    //     reassembled in DECLARED children order -- never arrival order -- so
    //     the output is deterministic regardless of message reordering.
    //   * Message types are "TOPOLOGY_REQUEST"; responses are pipe-delimited
    //     "TOPOLOGY|<childId>|<serializedSubtree>".
    //   * Serialization: leaf = id+"[]"; internal = id+"("+join(children,",")+")".
    //   * Special case: a ROOT that is also a leaf prints just "id" (NOT "id[]").
    //
    // Complexity is identical to Part 1: O(N) messages / work / space.
    // ========================================================================
    static final class Part2 {

        static final String TOPOLOGY_REQUEST = "TOPOLOGY_REQUEST";
        static final String TOPOLOGY_PREFIX = "TOPOLOGY|";

        // Non-final so the reordered/duplicate stress test in main() can
        // subclass it and override pump().
        static class Bus {
            record Msg(String to, String from, String body) {}

            final java.util.Map<String, Node> registry = new java.util.HashMap<>();
            final java.util.ArrayDeque<Msg> queue = new java.util.ArrayDeque<>();
            String result;

            void register(Node n) { registry.put(n.nodeId, n); }
            void setResult(String r) { this.result = r; }
            String getResult() { return result; }

            void sendAsyncMessage(String to, String from, String body) {
                queue.addLast(new Msg(to, from, body));
            }

            void pump() {
                while (!queue.isEmpty()) {
                    Msg m = queue.removeFirst();
                    registry.get(m.to).receiveMessage(m.from, m.body);
                }
            }

            String run(String rootId) {
                registry.get(rootId).receiveMessage(null, TOPOLOGY_REQUEST);
                pump();
                return result;
            }
        }

        static final class Node {
            final String nodeId;
            final String parent;            // null => root
            final java.util.List<String> children;
            final Bus bus;

            java.util.Set<String> pendingChildren = new java.util.LinkedHashSet<>();
            // child id -> its full serialized subtree, e.g. "4[]" or "2(4[],5[])".
            java.util.Map<String, String> childTopologies = new java.util.LinkedHashMap<>();

            Node(String nodeId, String parent, java.util.List<String> children, Bus bus) {
                this.nodeId = nodeId;
                this.parent = parent;
                this.children = children;
                this.bus = bus;
                bus.register(this);
            }

            void send(String target, String body) {
                bus.sendAsyncMessage(target, this.nodeId, body);
            }

            void receiveMessage(String from, String msg) {
                if (msg.equals(TOPOLOGY_REQUEST) && (from == null || from.equals(parent))) {
                    if (children.isEmpty()) {
                        // Leaf.
                        if (parent == null) {                 // root-leaf special case
                            bus.setResult(nodeId);            // just "id", not "id[]"
                            System.out.println(nodeId + " -> topology = " + nodeId);
                        } else {
                            send(parent, TOPOLOGY_PREFIX + nodeId + "|" + nodeId + "[]");
                        }
                    } else {
                        // Internal node: ask every child, await all subtrees.
                        pendingChildren = new java.util.LinkedHashSet<>(children);
                        childTopologies = new java.util.LinkedHashMap<>();
                        for (String child : children) {
                            send(child, TOPOLOGY_REQUEST);
                        }
                    }
                } else if (msg.startsWith(TOPOLOGY_PREFIX) && children.contains(from)) {
                    // "TOPOLOGY|<childId>|<serializedSubtree>"
                    String[] parts = msg.split("\\|", 3);
                    String childId = parts[1];
                    String childSerial = parts[2];
                    if (!pendingChildren.contains(childId)) {
                        return; // duplicate / already-recorded subtree: ignore.
                    }
                    childTopologies.put(childId, childSerial);
                    pendingChildren.remove(childId);
                    if (pendingChildren.isEmpty()) {
                        // Reassemble strictly in DECLARED children order.
                        String inner = String.join(",",
                                children.stream().map(childTopologies::get).toList());
                        String own = nodeId + "(" + inner + ")";
                        if (parent == null) {
                            bus.setResult(own);
                            System.out.println(nodeId + " -> topology = " + own);
                        } else {
                            send(parent, TOPOLOGY_PREFIX + nodeId + "|" + own);
                        }
                        childTopologies = new java.util.LinkedHashMap<>(); // reset
                    }
                }
            }
        }

        static void assertEquals(String label, String expected, String actual) {
            if (!expected.equals(actual)) {
                throw new AssertionError(label + ": expected " + expected + " got " + actual);
            }
            System.out.println("  PASS " + label + " = " + actual);
        }

        public static void main(String[] args) {
            System.out.println("== Part2: Topology ==");

            // single node "1" -> "1"
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of(), bus);
                assertEquals("single", "1", bus.run("1"));
            }

            // 1 -> [2,3] -> "1(2[],3[])"
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of(), bus);
                new Node("3", "1", java.util.List.of(), bus);
                assertEquals("star-3", "1(2[],3[])", bus.run("1"));
            }

            // chain 1 -> 2 -> 3 -> "1(2(3[]))"
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2"), bus);
                new Node("2", "1", java.util.List.of("3"), bus);
                new Node("3", "2", java.util.List.of(), bus);
                assertEquals("chain-3", "1(2(3[]))", bus.run("1"));
            }

            // complete-7 -> "1(2(4[],5[]),3(6[],7[]))"
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of("4", "5"), bus);
                new Node("3", "1", java.util.List.of("6", "7"), bus);
                new Node("4", "2", java.util.List.of(), bus);
                new Node("5", "2", java.util.List.of(), bus);
                new Node("6", "3", java.util.List.of(), bus);
                new Node("7", "3", java.util.List.of(), bus);
                assertEquals("complete-7", "1(2(4[],5[]),3(6[],7[]))", bus.run("1"));
            }

            // 6-node -> "1(2(4[],5[]),3(6[]))"
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of("4", "5"), bus);
                new Node("3", "1", java.util.List.of("6"), bus);
                new Node("4", "2", java.util.List.of(), bus);
                new Node("5", "2", java.util.List.of(), bus);
                new Node("6", "3", java.util.List.of(), bus);
                assertEquals("six", "1(2(4[],5[]),3(6[]))", bus.run("1"));
            }

            // Reordered + duplicate stress on complete-7: scramble delivery
            // order and duplicate every TOPOLOGY response. The declared-order
            // reassembly + pendingChildren guard must still yield the exact
            // canonical string.
            {
                Bus bus = new Bus() {
                    @Override void pump() {
                        while (!queue.isEmpty()) {
                            java.util.ArrayList<Msg> snapshot = new java.util.ArrayList<>(queue);
                            queue.clear();
                            java.util.Collections.reverse(snapshot);
                            for (Msg m : snapshot) queue.addLast(m);

                            Msg m = queue.removeFirst();
                            registry.get(m.to).receiveMessage(m.from, m.body);
                            if (m.body().startsWith(TOPOLOGY_PREFIX)) {
                                // duplicate the response delivery
                                registry.get(m.to).receiveMessage(m.from, m.body);
                            }
                        }
                    }
                };
                new Node("1", null, java.util.List.of("2", "3"), bus);
                new Node("2", "1", java.util.List.of("4", "5"), bus);
                new Node("3", "1", java.util.List.of("6", "7"), bus);
                new Node("4", "2", java.util.List.of(), bus);
                new Node("5", "2", java.util.List.of(), bus);
                new Node("6", "3", java.util.List.of(), bus);
                new Node("7", "3", java.util.List.of(), bus);
                assertEquals("complete-7-reordered-dup", "1(2(4[],5[]),3(6[],7[]))", bus.run("1"));
            }

            System.out.println("Part2: ALL PASSED");
        }
    }

    // ========================================================================
    // PART 3 - HANDLE MESSAGE FAILURES (CONCRETE, RUNNABLE)
    // ------------------------------------------------------------------------
    // CHANGES FROM PART 2:
    //   * We demonstrate robustness using COUNT, because double-counting is the
    //     canonical failure mode; the SAME technique (requestId + dedup +
    //     response cache + idempotent merge) extends verbatim to topology.
    //   * Every message carries a requestId. Wire format is pipe-delimited:
    //         "COUNT_REQUEST|<reqId>"
    //         "COUNT_RESPONSE|<reqId>|<count>"
    //   * Per-request state is keyed by reqId in a map, so different requests
    //     are fully isolated and a retried request reuses the SAME state.
    //   * Idempotent REQUEST handling: the first request creates state and acts;
    //     a duplicate request to a node that has already finished simply REPLAYS
    //     the cached response (or re-publishes the root result) -- it never
    //     recomputes or re-forwards.
    //   * Idempotent RESPONSE handling: a child's response is merged only if it
    //     is still in pendingChildren, so even if the same response is delivered
    //     many times it is counted AT MOST ONCE. This is the anti-double-count
    //     invariant.
    //   * The bus can LOSE and DUPLICATE messages (seeded Random). The harness
    //     runs one lossy round to exercise the failure paths, then retries
    //     RELIABLY -- modeling a network that eventually delivers.
    //
    // Complexity:
    //   No failures           : identical to Part 1, O(N) messages / work.
    //   With retries          : O(N * rounds) messages.
    //   Correctness invariant : the final answer is exactly N, with no double
    //                           counting, under ANY pattern of drops, duplicates
    //                           and retries -- guaranteed by the idempotent merge
    //                           (pendingChildren) plus the per-request response
    //                           cache (we keep ReqState; we never delete it).
    // ========================================================================
    static final class Part3 {

        static final long SEED = 42L;
        static final String COUNT_REQUEST = "COUNT_REQUEST";
        static final String COUNT_RESPONSE = "COUNT_RESPONSE";

        static final class Node {
            final String nodeId;
            final String parent;            // null => root
            final java.util.List<String> children;
            final Bus bus;

            // Per-request state, keyed by requestId. Retained after completion
            // so retries can REPLAY (this map IS the response cache).
            final java.util.Map<String, ReqState> requests = new java.util.HashMap<>();

            static final class ReqState {
                int total;
                java.util.Set<String> pendingChildren = new java.util.LinkedHashSet<>();
                boolean done;
                String cachedResponse; // the exact "COUNT_RESPONSE|..." we sent up
            }

            Node(String nodeId, String parent, java.util.List<String> children, Bus bus) {
                this.nodeId = nodeId;
                this.parent = parent;
                this.children = children;
                this.bus = bus;
                bus.register(this);
            }

            void send(String target, String body) {
                bus.sendAsyncMessage(target, this.nodeId, body);
            }

            // Has this node finished the given request? (used by the harness)
            boolean isDone(String reqId) {
                ReqState s = requests.get(reqId);
                return s != null && s.done;
            }

            // Children still owed for this request (empty if none/unknown).
            java.util.Set<String> pending(String reqId) {
                ReqState s = requests.get(reqId);
                return s == null ? java.util.Set.of() : s.pendingChildren;
            }

            void receiveMessage(String from, String msg) {
                String[] t = msg.split("\\|");
                String type = t[0];
                String reqId = t[1];

                if (type.equals(COUNT_REQUEST) && (from == null || from.equals(parent))) {
                    ReqState st = requests.get(reqId);
                    if (st != null) {
                        // We've seen this request. If finished, REPLAY the answer
                        // (idempotent); otherwise the in-flight work continues and
                        // we simply drop the duplicate request.
                        if (st.done) {
                            if (parent != null) {
                                send(parent, st.cachedResponse);
                            } else {
                                bus.setResult(String.valueOf(st.total));
                            }
                        }
                        return;
                    }
                    // First time we see this request: create state and act.
                    st = new ReqState();
                    requests.put(reqId, st);
                    if (children.isEmpty()) {
                        // Leaf.
                        st.done = true;
                        st.total = 1;
                        if (parent == null) {                 // single-node tree
                            bus.setResult("1");
                            System.out.println(nodeId + " -> count = 1");
                        } else {
                            st.cachedResponse = COUNT_RESPONSE + "|" + reqId + "|1";
                            send(parent, st.cachedResponse);
                        }
                    } else {
                        // Internal node: count self, ask every child.
                        st.total = 1;
                        st.pendingChildren = new java.util.LinkedHashSet<>(children);
                        for (String child : children) {
                            send(child, COUNT_REQUEST + "|" + reqId);
                        }
                    }
                } else if (type.equals(COUNT_RESPONSE) && children.contains(from)) {
                    ReqState st = requests.get(reqId);
                    if (st == null || st.done) {
                        return; // no in-flight request, or already finalized: ignore.
                    }
                    if (!st.pendingChildren.contains(from)) {
                        return; // duplicate / already-counted child: ignore (anti-double-count).
                    }
                    st.total += Integer.parseInt(t[2]);
                    st.pendingChildren.remove(from);
                    if (st.pendingChildren.isEmpty()) {
                        st.done = true;
                        if (parent == null) {
                            bus.setResult(String.valueOf(st.total));
                            System.out.println(nodeId + " -> count = " + st.total);
                        } else {
                            st.cachedResponse = COUNT_RESPONSE + "|" + reqId + "|" + st.total;
                            send(parent, st.cachedResponse);
                        }
                        // KEEP st: it is the response cache for retries. Do NOT delete.
                    }
                }
            }
        }

        // --- Lossy, duplicating message bus -------------------------------
        static final class Bus {
            record Msg(String to, String from, String body) {}

            final java.util.Map<String, Node> registry = new java.util.HashMap<>();
            final java.util.ArrayDeque<Msg> queue = new java.util.ArrayDeque<>();
            String result;

            final java.util.Random rng = new java.util.Random(SEED);
            double dropProb = 0.0;
            double dupProb = 0.0;
            int dropped = 0;
            int duplicated = 0;

            void register(Node n) { registry.put(n.nodeId, n); }
            void setResult(String r) { this.result = r; }
            String getResult() { return result; }

            void setLossy(double drop, double dup) { this.dropProb = drop; this.dupProb = dup; }
            void setReliable() { this.dropProb = 0.0; this.dupProb = 0.0; }

            void sendAsyncMessage(String to, String from, String body) {
                queue.addLast(new Msg(to, from, body));
            }

            // Drain unreliably: each message may be dropped, and a delivered
            // message may be duplicated.
            void pumpUnreliable() {
                while (!queue.isEmpty()) {
                    Msg m = queue.removeFirst();
                    if (rng.nextDouble() < dropProb) {
                        dropped++;
                        continue; // lost in the network
                    }
                    registry.get(m.to).receiveMessage(m.from, m.body);
                    if (rng.nextDouble() < dupProb) {
                        duplicated++;
                        registry.get(m.to).receiveMessage(m.from, m.body); // duplicate delivery
                    }
                }
            }

            // Drain reliably (no drops, no dups).
            void runReliablePump() {
                setReliable();
                while (!queue.isEmpty()) {
                    Msg m = queue.removeFirst();
                    registry.get(m.to).receiveMessage(m.from, m.body);
                }
            }
        }

        static void assertEquals(String label, String expected, String actual) {
            if (!expected.equals(actual)) {
                throw new AssertionError(label + ": expected " + expected + " got " + actual);
            }
            System.out.println("  PASS " + label + " = " + actual);
        }

        // Harness: run a count under loss/duplication, then retry reliably until
        // the root reports done. Retries re-send the request to the root and
        // re-prod every node that still has pending children -- all idempotent.
        static String runCount(Bus bus, String rootId, String reqId, double drop, double dup) {
            int n = bus.registry.size();

            // Round 0: lossy, to exercise the failure paths.
            bus.setLossy(drop, dup);
            bus.registry.get(rootId).receiveMessage(null, COUNT_REQUEST + "|" + reqId);
            bus.pumpUnreliable();

            // Retry rounds: reliable. "first round lossy to exercise failure
            // paths; retries reliable to guarantee convergence -- models a
            // network that eventually delivers."
            for (int r = 0; r < n + 5 && !bus.registry.get(rootId).isDone(reqId); r++) {
                bus.setReliable();
                // Re-deliver the kickoff (idempotent: replays if root already done).
                bus.registry.get(rootId).receiveMessage(null, COUNT_REQUEST + "|" + reqId);
                // Re-prod every node that still awaits children.
                for (Node node : bus.registry.values()) {
                    // copy to avoid ConcurrentModificationException (pending shrinks)
                    for (String child : new java.util.ArrayList<>(node.pending(reqId))) {
                        node.send(child, COUNT_REQUEST + "|" + reqId);
                    }
                }
                bus.runReliablePump();
            }
            return bus.result;
        }

        // Build the complete-7 tree onto a fresh bus and return the node map.
        static java.util.Map<String, Node> buildComplete7(Bus bus) {
            new Node("1", null, java.util.List.of("2", "3"), bus);
            new Node("2", "1", java.util.List.of("4", "5"), bus);
            new Node("3", "1", java.util.List.of("6", "7"), bus);
            new Node("4", "2", java.util.List.of(), bus);
            new Node("5", "2", java.util.List.of(), bus);
            new Node("6", "3", java.util.List.of(), bus);
            new Node("7", "3", java.util.List.of(), bus);
            return bus.registry;
        }

        public static void main(String[] args) {
            System.out.println("== Part3: Failures (count, lossy + duplicating) ==");

            // complete-7 under 30% drop + 30% duplication -> must be exactly 7.
            {
                Bus bus = new Bus();
                java.util.Map<String, Node> nodes = buildComplete7(bus);
                String res = runCount(bus, "1", "req-1", 0.3, 0.3);
                assertEquals("complete7-lossy", "7", res);
                System.out.println("  (network: dropped=" + bus.dropped
                        + ", duplicated=" + bus.duplicated + ")");

                // PROVE no double counting: every internal node's subtree total
                // is exactly right despite the drops/dups/retries.
                assertEquals("node2-subtree", "3",
                        String.valueOf(nodes.get("2").requests.get("req-1").total));
                assertEquals("node3-subtree", "3",
                        String.valueOf(nodes.get("3").requests.get("req-1").total));
                assertEquals("root-subtree", "7",
                        String.valueOf(nodes.get("1").requests.get("req-1").total));

                // Per-request isolation: a brand-new requestId on the SAME nodes
                // computes from scratch and again yields exactly 7.
                String res2 = runCount(bus, "1", "req-2", 0.3, 0.3);
                assertEquals("complete7-lossy-req2", "7", res2);
            }

            // chain 1->2->3 under loss -> 3
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of("2"), bus);
                new Node("2", "1", java.util.List.of("3"), bus);
                new Node("3", "2", java.util.List.of(), bus);
                assertEquals("chain-lossy", "3", runCount(bus, "1", "req-chain", 0.3, 0.3));
            }

            // single node under loss -> 1
            {
                Bus bus = new Bus();
                new Node("1", null, java.util.List.of(), bus);
                assertEquals("single-lossy", "1", runCount(bus, "1", "req-single", 0.3, 0.3));
            }

            // pure duplication (no drops): 60% duplication -> still exactly 7.
            // This isolates the anti-double-count guard from the retry logic.
            {
                Bus bus = new Bus();
                buildComplete7(bus);
                assertEquals("complete7-dupOnly", "7", runCount(bus, "1", "reqD", 0.0, 0.6));
            }

            System.out.println("Part3: ALL PASSED");
        }
    }
}
