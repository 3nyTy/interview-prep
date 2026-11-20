# Graphs and DAGs — 7 problems

This is the highest-frequency Robinhood coding area. The load-factor problem appears in
three forms. Learn one topological-sort-with-accumulator template and all three fall out.

---

## 1. Compute Service Load Factor in a Service Dependency Graph

Frequency: high (listed as a hot coding question)
Source: `ec6e8e84-19bc-4ef6-92a8-d2b50f5fead0`

### Statement

Process a directed acyclic graph of service dependencies. Each service has a base
request load. The effective load on a service is "its own base load plus the sum of loads
propagated from all upstream services." The load factor is load divided by capacity.

```
load(service)        = base_load(service) + sum(load(parent) * weight(parent -> service))
load_factor(service) = load(service) / capacity(service)
```

### Requirements

1. Accept services, capacities, base loads, and dependency edges (optionally weighted).
2. The graph is acyclic — this is guaranteed.
3. Return the load factor for each service, **sorted alphabetically**.

### Special cases named in the problem

- Isolated services (no edges)
- Services with multiple parents
- Edges with different weights

### Constraints

- N ≤ 10^5 services
- M ≤ 2 x 10^5 edges

### Example

`A->B (weight 1)`, `B->C (weight 2)`, `base_load(A) = 10`, `capacity(A, B, C) = (20, 10, 10)`

Load factors: `0.5, 1.0, 2.0`

Check: load(A) = 10 → 10/20 = 0.5. load(B) = 0 + 10*1 = 10 → 10/10 = 1.0.
load(C) = 0 + 10*2 = 20 → 20/10 = 2.0.

### Approach

Topological order (Kahn's algorithm on in-degree), then push load forward:

```java
// after topo sort
for (String u : topoOrder) {
    for (Edge e : adj.get(u)) {
        load.merge(e.to, load.get(u) * e.weight, Double::sum);
    }
}
```

Initialize `load[v] = baseLoad[v]` for every v before the sweep. Output sorted by name —
use a `TreeMap` so the alphabetical requirement is free.

### Traps

- Do **not** use recursion for N = 10^5. Stack overflow. Use Kahn's iterative topo sort.
- Isolated services must still appear in the output with `baseLoad / capacity`.
- Capacity could be 0. Ask the interviewer what to return.
- Weight defaults to 1 when the input is unweighted.

### Paywalled

Test cases, official solution, round type, frequency data, exact date asked.

---

## 2. System Dependency Load Factor (DAG reverse-dependency count)

Source: `3d0ffbd1-ea36-4d17-90c4-db52ec75335a`

### Statement

Calculate how many programs depend on each given program in a DAG. You receive a list of
programs and dependency pairs, where `[a, b]` means **a depends on b**.

Load factor of x = "the number of nodes that can reach `x` following dependency edges",
excluding x itself.

### Rules

- Ignore pairs where either endpoint is not in the programs list.
- Return a mapping of each program to its load factor.

### Example

Programs: A, B, C, D. Dependencies: `[A→B], [B→C], [A→C], [D→C]`.
(A pair mentioning E is filtered out, because E is not in the programs list.)

Result: `{"A": 0, "B": 1, "C": 3, "D": 0}`

Check: nothing reaches A or D. A reaches B, so B = 1. A, B, and D all reach C, so C = 3.

### Approach

This is **reachability count, not a path count**. Node C gets 3, not 4 — A reaches C by
two routes (`A→C` and `A→B→C`) but counts once. Contrast this with problem 3 below, where
paths do multiply. Getting this distinction right is the whole question.

Two options:

- **Reverse DFS/BFS per node** — for each x, traverse the reverse graph and count visited
  nodes. O(V*(V+E)). Fine for small V, and easy to state correctly.
- **Bitset of ancestors in topo order** — `ancestors[v] = union of ancestors[u] | {u}` for
  each edge `u→v`. Answer is `ancestors[v].cardinality()`. O(V*E/64). Use this if V is
  bounded to a few thousand.

There is no O(V+E) exact solution — transitive closure is genuinely quadratic in the worst
case. Say so out loud; it shows you understand the problem rather than fumbling for a
faster sweep that does not exist.

### Traps

- The direction is inverted from the wording. `[a, b]` = a depends on b = edge a→b, and
  you count nodes that **reach** the target.
- Filter the edge list against the programs list **first**.
- Do not accumulate counts additively along edges. That gives path counts, which is wrong
  here (C would be 4).

### Paywalled

Difficulty, duration, tags, frequency, round type, test cases, follow-ups.

---

## 3. Count Trigger Events in Directed Acyclic Graph

Source: `1c708aa4-2fbb-4cc4-ae73-3c423e4e9490`

### Statement

Calculate trigger counts for nodes in a DAG. When an entry point is triggered, the trigger
propagates to child nodes.

### Example

Edges: `A→B, B→C, B→D, C→D, D→E, D→F, E→F`. Entry point: A.

Result: `A:1, B:1, C:1, D:2, E:2, F:4`

Check: D is triggered by B and by C, so 2. E is triggered by D, so 2. F is triggered by D
(2) and by E (2), so 4.

### Approach

This one **is** additive — the opposite of problem 2. Count distinct paths:

```
trigger[entry] = 1
for u in topological order:
    for v in children(u):
        trigger[v] += trigger[u]
```

### Traps

- Numbers can grow exponentially with graph depth. Path counts double at each fan-in.
  Use `long`, and ask whether a modulus is expected.
- Nodes unreachable from the entry point keep a count of 0.
- Note the contrast with problem 2 explicitly if both come up. Reachability de-duplicates;
  trigger propagation does not.

### Paywalled

Complete details, constraints, test cases, follow-ups, round metadata.

---

## 4. Course Prerequisites Topological Sort

Source: `6a53ed24-90e9-5f8e-8a45-2cee62777da0`

### Statement

Find a valid order to complete n courses (labeled 0 to n-1) given prerequisite pairs.
Return the **lexicographically smallest** valid ordering, or `-1` if a cycle exists.

### Input

- Line 1: two integers n (course count) and m (prerequisite relationships)
- Next m lines: two integers representing a dependency

### Output

The valid course sequence on one line, or `-1` if impossible.

### Constraints

- 1 ≤ n ≤ 10^5
- 0 ≤ m ≤ 2 x 10^5
- 0 ≤ course, prereq < n

### Example

4 courses, 4 prerequisites (1←0, 2←0, 3←1, 3←2). Output: `0 1 2 3`

### Approach

Kahn's algorithm, but replace the queue with a **min-heap** to get the lexicographically
smallest order. Standard BFS queue gives *a* valid order, not the smallest one.

```java
PriorityQueue<Integer> ready = new PriorityQueue<>();
for (int v = 0; v < n; v++) if (indeg[v] == 0) ready.add(v);
while (!ready.isEmpty()) {
    int u = ready.poll();
    order.add(u);
    for (int v : adj[u]) if (--indeg[v] == 0) ready.add(v);
}
if (order.size() != n) return -1;   // cycle
```

O((V+E) log V).

### Traps

- The min-heap is the entire point. A plain `ArrayDeque` fails the lexicographic
  requirement and looks like you did not read the problem.
- Detect the cycle by comparing the output size against n. Do not try to detect it
  separately.
- Parse the edge direction carefully. The example writes `1←0`, so the input pair order
  needs a check against the sample.

### Paywalled

Complete details, test cases, solution, round type, frequency, date asked.

---

## 5. Referral Count (Subtree Descendant Count in a Referral Tree)

Source: `3a296606-9af1-431e-be45-5c7d1a2532f1`

### Statement

Given a referral network forming a tree or forest, where each node has at most one parent,
calculate the number of descendants for every node.

### Input

- Line 1: two integers `n` (nodes labeled 0 to n-1) and `m` (referral edges)
- Next m lines: two integers `p c` — a directed edge from p to c

### Output

n space-separated integers. Position i is the descendant count for node i
(subtree size minus 1).

### Constraints

- 1 ≤ n ≤ 2 x 10^5
- 0 ≤ m ≤ n-1
- Each node has at most one parent
- The graph may be disconnected (it is a forest)
- No self-loops

### Example

6 nodes, 5 edges: `0→1, 0→2, 1→3, 1→4, 2→5`. Output: `5 2 1 0 0 0`

### Approach

Reverse topological order (children before parents), then accumulate upward:

```
size[v] = 1 for all v
for v in reverse topo order:
    if parent[v] exists: size[parent[v]] += size[v]
answer[v] = size[v] - 1
```

Because m ≤ n-1 and every node has at most one parent, a single parent array is enough.
No adjacency list is required for the accumulate step.

### Traps

- n = 2 x 10^5 with a possible path-shaped tree. **Recursive DFS overflows the stack.**
  Use an explicit stack or the reverse-topo sweep above.
- It is a forest, not a tree. Multiple roots. Do not assume node 0 is the root.
- The answer is subtree size **minus 1** — the node does not count as its own descendant.

### Paywalled

Complete details, test cases, solution, round type, frequency, date asked.

---

## 6. Referral Program Leaderboard

Source: `40184b3e-adcc-4b4b-9804-5167e4d81d01`

### Statement

"Design a referral program leaderboard with relational support such that if A refers B,
and B refers C, C's count adds to both A and B." Implement "an efficient method to query
the top 3 in the leaderboard" while keeping performance "under a large number of referral
relationships."

### Approach

This is problem 5 turned into a streaming/online design question. Two parts:

**Transitive credit on insert.** When C joins under B, walk up the parent chain from B and
increment every ancestor. Cost is O(depth) per insert. Note the worst case — a
degenerate chain gives O(n) per insert. If the interviewer pushes, discuss:
- Capping credited depth (many real referral programs credit only 1–2 levels).
- Heavy-path decomposition or an Euler-tour + BIT if arbitrary depth must stay fast.

**Top-3 query.** Do not sort on every query.
- A size-3 min-heap gives O(1) reads and O(log 3) maintenance, but it cannot handle a
  score that **decreases**. Fine here, because referral counts only grow.
- For general top-K with updates, use a `TreeSet` ordered by `(score desc, id asc)` and
  remove-then-reinsert on each score change. O(log n) per update, O(K) per query.

State the trade-off out loud: the heap is cheaper but only correct for monotonically
increasing scores. That observation is what the question is testing.

### Traps

- Ties. Define the tie-break (insertion order or user id) before you code.
- The "adds to both A and B" wording means every ancestor gets credit, not just the direct
  referrer.

### Paywalled

Difficulty, duration, tags, constraints, examples, follow-ups, solution, round metadata.

---

## 7. Clone a Graph

Source: `9b3c28be-4683-47bc-97a0-ed6cf56e5398`

### Statement

Given a graph represented as a 2D list, clone the graph. Each node contains a `val`
(unique identifier) and a list that stores the node's neighbors.

Produce "a deep copy of the graph" where "all nodes and edge structures should be
identical but each node should be a new entity."

### Constraints

- Node values: between 1 and 100
- Neighbors per node: 0 to 100

### Example input

`adjList = [[2,4],[1,3],[2,4],[1,3]]`

### Approach

LeetCode 133. Hash map from original node to clone, then DFS or BFS:

```java
Map<Node, Node> seen = new HashMap<>();
Node clone(Node n) {
    if (n == null) return null;
    if (seen.containsKey(n)) return seen.get(n);
    Node copy = new Node(n.val);
    seen.put(n, copy);                    // put BEFORE recursing
    for (Node nb : n.neighbors) copy.neighbors.add(clone(nb));
    return copy;
}
```

### Traps

- `seen.put` must happen **before** the recursion, or cycles cause infinite recursion.
  This is the whole test.
- Handle a null input and a single node with no neighbors.
- The graph may be undirected, so every edge appears twice. The map handles it.

### Paywalled

Difficulty, full I/O spec, test cases, solution, round type, frequency, follow-ups.
