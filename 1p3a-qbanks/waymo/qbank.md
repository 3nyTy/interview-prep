# Waymo — 1point3acres Question Bank (50 curated questions)

Source: 1point3acres qbank (`company=waymo`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/waymo>

The site lists 94 entries for this company: **50 curated questions** (below), 44 OJ links, 0 external links. Only the curated 50 are captured here.

> Waymo loops mix nominally LeetCode-style coding with deliberately ambiguous, autonomy-flavored prompts and a signature 'data fluency' round that's hard to drill for. Coding bars are mid-hard but interviewers frequently swap in original problems framed around self-driving scenarios; ML and senior loops add a behavior-prediction-style ML system design round on top.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Hand-Write K-Means Clustering (NumPy)
2. Number of Visible People in a Queue (LC 1944)
3. Regular Expression Matching (LC 10)
4. House Robber II (LC 213)
5. Merge Object Histories Across Two Tracking Systems
6. Build an Arithmetic Expression to Reach a Target
7. Race Car: Minimum Instructions to Reach a Target
8. Implement Max Pooling with Argmax Coordinates
9. OO Design: Continuous Time Intervals from Two Timestamp Signal Streams
10. Validate a Forest from (parent, child) Edges
11. Android Phone Screen: Build a Pickup Screen in Jetpack Compose
12. ML Coding: Debug a NumPy / Tensor Framework
13. Contains Duplicate III (Bucket Sort)
14. Decode String with `(group){k}` Repeat Syntax
15. Hashmap + Prefix Sum Subarray Round
16. Universal Direction Sequence for an Unknown-Position Robot in a Maze
17. Count Same-Color Squares in an Unbounded Grid
18. Shortest Path from Source to Target Nodes (Dijkstra)
19. Serialize Arithmetic Expression Tree with Minimum Parentheses
20. Maximum-Area Axis-Aligned Rectangle from a Coordinate Set
21. Chess Piece Shortest Path on a Fixed Board
22. Battleship Board (React, Frontend Onsite)
23. Prefix Autocomplete via Trie
24. Car Maze with Incrementally Revealed Neighbors (DFS)
25. Sort a Quadratic-Transformed Sorted Array
26. Waymo Passenger Pickup Scheduler (OO Design)
27. Implement a Rate Limiter
28. Rectangle Copy Inside a 2-D Array with O(1) Memory
29. Run-Length Encoded String: Find by Index and Range Max
30. LED Digit Display Invariant Under 180° Rotation
31. Sort Target String by Custom Order
32. Two-Column Table: Place Divider to Minimize Total Height
33. Sparse Matrix Left- and Right-Multiply Vector
34. Shortest Distance from All Buildings
35. Randomly Populate Grid with Connected Equal-Size Token Regions
36. Process Raw CSV for Downstream Consumers
37. Streaming Interval Coverage on a Number Axis
38. Design a Global Cache for Small Images
39. ML System Design: Inference Serving with Back-of-Envelope Capacity Planning
40. System Design: Elevator Operation Logic
41. System Design: Simulation Log Collection and Result Aggregation
42. ML System Design: Choose Passenger Drop-off Location
43. System Design: Evaluation System with Human + LLM Evaluators
44. System Design: Evaluate a Self-Driving Model End-to-End
45. System Design: Parking Lot + Robotaxi Dispatch
46. ML System Design: Behavior Prediction from Sensor + Camera Data
47. L5+ Behavioral: Policy vs Efficiency Trade-offs & Leadership
48. Project Deep-Dive + ML Fundamentals Discussion
49. SWE Resume Deep Dive: Technologies and Outcome Metrics
50. Data Fluency: Self-Driving Progress Metrics & Experiment Comparison

---

## 1. Hand-Write K-Means Clustering (NumPy)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-13
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: numpy, pytorch, ml-knowledge, gradient-descent, linear-regression, attention, transformer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/ml-coding-handwrite-kmeans
- **related discussions**: 4

### Statement

ML coding round: hand-implement K-means in NumPy with explicit attention to broadcasting and vectorization. Explicit per-sample Python loops over the assignment / update steps are docked. Same family as the broader 'ML coding' round, which can also surface logistic regression, linear regression closed-form, attention, or a small transformer block.

---

## 2. Number of Visible People in a Queue (LC 1944)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-31
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, monotonic-stack, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/number-of-visible-people-in-a-queue-lc-1944
- **related discussions**: 1

### Statement

Canonical LeetCode 1944: for each person in a queue of distinct heights, count how many people to their right are visible under the rule that everyone between the pair must be shorter than both endpoints.

---

## 3. Regular Expression Matching (LC 10)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-31
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, dp, recursion, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/regular-expression-matching-lc-10
- **related discussions**: 1

### Statement

Canonical LeetCode 10: determine whether an entire input string matches a pattern containing ordinary characters plus `.` and `*`, where `.` matches any single character and `*` repeats the preceding element zero or more times.

---

## 4. House Robber II (LC 213)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-31
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/house-robber-ii-lc-213
- **related discussions**: 1

### Statement

Canonical LeetCode 213: maximize the money collected from a circular row of houses without taking from two adjacent houses. The first and last houses are adjacent.

---

## 5. Merge Object Histories Across Two Tracking Systems

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: object-design, hashmap, sorting, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/cross-system-object-tracker-history
- **related discussions**: 1

### Statement

Implement an `ObjectTracker` that links IDs from tracking systems A and B, accepts observations from either system, and returns the combined chronological history for the underlying real-world object from either ID namespace.

---

## 6. Build an Arithmetic Expression to Reach a Target

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: backtracking, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/arithmetic-expression-reach-target
- **related discussions**: 1

### Statement

Given three numbers and a target, insert addition, subtraction, multiplication, and parentheses between the numbers; return an expression whose value equals the target, or determine that none exists. The follow-up generalizes the input from three numbers to n numbers.

---

## 7. Race Car: Minimum Instructions to Reach a Target

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, shortest-path, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/race-car-minimum-instructions
- **related discussions**: 1

### Statement

The phone screen used LeetCode 818, Race Car, in its standard form: find the minimum instruction count needed for a car with position and signed speed to reach a target.

---

## 8. Implement Max Pooling with Argmax Coordinates

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, grid, cnn, computer-vision, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/max-pooling-with-argmax-coordinates
- **related discussions**: 1

### Statement

Implement max pooling over an input tensor. The follow-up requires returning the coordinates of each selected maximum together with the pooled values.

---

## 9. OO Design: Continuous Time Intervals from Two Timestamp Signal Streams

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, interval, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/timestamp-signals-time-intervals-ood
- **related discussions**: 1

### Statement

Phone screen with a real Waymo business framing: given two kinds of timestamp-based signals, design a class that returns the continuous time intervals during which a condition holds. The interviewer cares about how you analyze the problem and pin down details, and explicitly wants you to ask about anything ambiguous before coding.

---

## 10. Validate a Forest from (parent, child) Edges

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, cycle-detection, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/forest-validation-parent-child-edges
- **related discussions**: 1

### Statement

Phone screen: given a graph described as a list of (parent, child) edges, decide whether it forms a valid forest (a disjoint set of trees). The candidate must design their own test cases. The whole round is conducted under screen-share as an anti-cheat measure.

---

## 11. Android Phone Screen: Build a Pickup Screen in Jetpack Compose

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: android, frontend, mini-project, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/android-pickup-screen-jetpack-compose
- **related discussions**: 1

### Statement

Android engineer phone screen run live in Android Studio: build a Waymo pickup screen in Jetpack Compose. Touches a scrolling list, LiveData-driven state, and currency formatting / translation.

---

## 12. ML Coding: Debug a NumPy / Tensor Framework

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: numpy, debugging, code-reading, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/ml-coding-tensor-framework-debug
- **related discussions**: 1

### Statement

MLE onsite coding round framed as a debugging task: the interviewer hands over a small NumPy / tensor framework that is subtly broken, and the candidate has to read it, reproduce the failures, and fix them. Reported bugs cluster around array-construction aliasing, a missing reduction axis, and integer truncation in a conversion helper.

---

## 13. Contains Duplicate III (Bucket Sort)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bucket-sort, sliding-window, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/bucket-sort-contains-duplicate-iii
- **related discussions**: 1

### Statement

Phone screen: LeetCode 220 (Contains Duplicate III). Given an integer array, indexDiff `k`, and valueDiff `t`, decide whether there exists a pair `(i, j)` with `i ≠ j`, `|i − j| ≤ k`, `|nums[i] − nums[j]| ≤ t`. Optimal solution is bucket sort over a sliding window.

---

## 14. Decode String with `(group){k}` Repeat Syntax

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: stack, parsing, string, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/decode-string-curly-repeat
- **related discussions**: 1

### Statement

Phone screen variant of LeetCode 394 (Decode String). The repeat-count syntax is `(group){k}` instead of `k[group]`. Nested repeats such as `"a(b(c){2}){2}"` must expand correctly. Solvable with the same stack pattern as the canonical problem.

---

## 15. Hashmap + Prefix Sum Subarray Round

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, prefix-sum, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/hash-prefix-sum-coding
- **related discussions**: 1

### Statement

Phone screen: classic hashmap + prefix-sum pattern over an integer array (subarray sum / count-of-subarrays variants). Standard implementation; round graded on clean handling of the empty-prefix case and clear complexity discussion.

---

## 16. Universal Direction Sequence for an Unknown-Position Robot in a Maze

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, state-machine, algorithm-design, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/robot-maze-universal-direction-sequence
- **related discussions**: 1

### Statement

Onsite DSA from an ex-Google interviewer. A 2-D maze contains walls, empty cells, and a single exit `E`. A robot moves up / down / left / right but stays in place when it hits a wall. Without knowing the robot's starting cell, produce a *single fixed* sequence of moves that lands the robot on `E` regardless of where it started. Solution exists by assumption.

---

## 17. Count Same-Color Squares in an Unbounded Grid

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, hashmap, union-find, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/same-color-squares-in-grid
- **related discussions**: 1

### Statement

Phone screen on a 2-D grid whose cells carry color labels A / B / C / D. Count the number of axis-aligned squares formed by connected same-color cells. Grid has no a-priori size bound — the data structure must handle sparse cell sets.

---

## 18. Shortest Path from Source to Target Nodes (Dijkstra)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dijkstra, graph, heap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/dijkstra-shortest-path-to-targets
- **related discussions**: 1

### Statement

Classic Dijkstra phone screen: given a source node, a set of target nodes, and weighted edges between nodes, return the shortest distance from source to all targets (or the minimum over targets, depending on the asked variant). Aligned with LeetCode 743 (Network Delay Time).

---

## 19. Serialize Arithmetic Expression Tree with Minimum Parentheses

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, recursion, parsing, string-processing, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/binary-expression-tree-serialize-min-parens
- **related discussions**: 1

### Statement

Onsite coding: given a syntax tree whose nodes are variables or one of `+ − × ÷`, serialize the tree to an infix string using the minimum number of parentheses required to preserve evaluation semantics.

---

## 20. Maximum-Area Axis-Aligned Rectangle from a Coordinate Set

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, set, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/max-area-rectangle-coordinate-set
- **related discussions**: 1

### Statement

Onsite coding: given a set of `(x, y)` integer points, find the maximum-area axis-aligned rectangle whose four corners are all in the set. Variant of LeetCode 939 (Minimum Area Rectangle) with the objective flipped from min to max.

---

## 21. Chess Piece Shortest Path on a Fixed Board

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, graph, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/chess-shortest-path-fixed-board
- **related discussions**: 1

### Statement

Phone screen: find the shortest path of moves for a chess piece between two squares on a fixed-size board. Candidate defines the move set, function signature, and test cases. Solution is plain BFS with two follow-ups: blocked squares, then an infinite-board variant.

---

## 22. Battleship Board (React, Frontend Onsite)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: react, frontend, design-implementation, game-engine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/battleship-react-frontend
- **related discussions**: 1

### Statement

Frontend onsite: implement the Battleship board in React from a physical reference model. Cells cycle through empty / red marker / white marker on click. Compute and display ship-sunk vs ship-alive status from the marker state.

---

## 23. Prefix Autocomplete via Trie

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-27
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: trie, dfs, string, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/trie-prefix-autocomplete
- **related discussions**: 1

### Statement

Phone screen: given a word list, build a data structure that returns all words sharing a given prefix. Interviewer explicitly expects a trie; hashmap is the brute-force baseline. Common follow-up is DFS extraction of all completions below a prefix node.

---

## 24. Car Maze with Incrementally Revealed Neighbors (DFS)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dfs, graph, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/car-maze-incremental-discovery-dfs
- **related discussions**: 1

### Statement

Phone screen: a car explores a maze toward a goal; at each cell it learns which neighbors are reachable but has no global view. Implement DFS with a visited set to find a path while preventing cycles.

---

## 25. Sort a Quadratic-Transformed Sorted Array

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sort-transformed-quadratic-array
- **related discussions**: 1

### Statement

Phone screen: given coefficients `a, b, c` of a quadratic `f(x) = ax² + bx + c` and a sorted integer array, return the sorted array of `f(nums[i])`. `a` may be negative — solve in O(n) with a two-pointer sweep that handles the parabola's monotonicity. Equivalent to LeetCode 360 (Sort Transformed Array).

---

## 26. Waymo Passenger Pickup Scheduler (OO Design)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, simulation, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/passenger-pickup-scheduler-oo-design
- **related discussions**: 1

### Statement

Phone screen for a senior SWE: design and implement an OO scheduler that simulates a Waymo car picking up passengers in input order, returning the total elapsed time. Follow-ups extend the model with time-skipping and trip cancellation.

---

## 27. Implement a Rate Limiter

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: rate-limiting, concurrency, throttling, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/rate-limiter-implementation
- **related discussions**: 1

### Statement

Phone screen with ~10 minutes of behavioral up front, then implement a rate limiter. Interviewer expected the candidate to pick an algorithm (fixed window / sliding window / token bucket), justify the choice, and discuss thread-safety and distributed extensions.

---

## 28. Rectangle Copy Inside a 2-D Array with O(1) Memory

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, array, simulation, memory-bounded, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/rectangle-copy-o1-memory
- **related discussions**: 1

### Statement

Onsite coding: copy a rectangular sub-region of a 2-D array to another non-overlapping position in the same array, using strictly O(1) auxiliary memory. The interesting case is overlapping source and destination, which forces a directional copy order.

---

## 29. Run-Length Encoded String: Find by Index and Range Max

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: compression, binary-search, prefix-sum, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/run-length-encoding-find-by-index
- **related discussions**: 1

### Statement

Phone screen with two parts: (1) given an RLE-encoded string like `B1A2E3C1` (decodes to `BAAEEEC`), implement `char Find(int p)` returning the character at original index `p`. (2) Given the same encoding plus a sorted assumption, implement `FindByValue(target, left, right)` returning the maximum character strictly greater than `target` in `[left, right]`.

---

## 30. LED Digit Display Invariant Under 180° Rotation

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/strobogrammatic-led-display
- **related discussions**: 1

### Statement

Onsite coding: given an array of digits 0–9 representing an LED-style display, return whether the rendered display reads identically after a 180° rotation. Equivalent to LeetCode 246 (Strobogrammatic Number) on the digit-string interpretation.

---

## 31. Sort Target String by Custom Order

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, sorting, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/custom-sort-string-by-order
- **related discussions**: 1

### Statement

Onsite coding: given a `target` string and an `order` string, return a permutation of `target` so that characters that appear in `order` are emitted in `order`'s sequence. Characters not in `order` may go anywhere. Equivalent to LeetCode 791 (Custom Sort String).

---

## 32. Two-Column Table: Place Divider to Minimize Total Height

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, greedy, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/two-column-table-optimal-divider
- **related discussions**: 1

### Statement

Phone screen: a known total width must be split between two text columns. Given the words and the joint width budget, decide the divider position that minimizes the table's total height. Binary search on divider position.

---

## 33. Sparse Matrix Left- and Right-Multiply Vector

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-09-29
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, data-structure, linear-algebra, performance-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sparse-matrix-vector-multiply
- **related discussions**: 1

### Statement

Onsite coding: implement a sparse matrix class supporting both left- and right-multiplication by a dense vector. The round grades on choosing the right sparse representation (COO / CSR / dict-of-dicts) and explaining the complexity asymmetry between the two multiply directions.

---

## 34. Shortest Distance from All Buildings

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-09-08
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, shortest-path, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/shortest-distance-from-all-buildings
- **related discussions**: 2

### Statement

The onsite coding round used the unmodified LC 317 prompt: choose an empty grid cell minimizing its total distance to every building, or return `-1` when no such cell can reach them all.

---

## 35. Randomly Populate Grid with Connected Equal-Size Token Regions

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, sampling, simulation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/random-grid-token-fill
- **related discussions**: 2

### Statement

Onsite coding: populate an `m × n` grid with four tokens such that (a) each token occupies the same number of cells, (b) each token's region is 4-connected, (c) the entire grid is covered, and (d) the placement is randomized. Multi-source BFS expansion with a retry loop is the canonical approach.

---

## 36. Process Raw CSV for Downstream Consumers

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-22
- **stage**: phone-screen
- **access**: Free preview
- **roles**: SWE
- **tags**: parsing, string-processing, ambiguous-spec, data-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/csv-parser-open-ended
- **related discussions**: 3

### Statement

Open-ended phone screen prompt: given a raw CSV string, design a parser that produces a representation other engineering teams can consume. The interviewer refuses to pin down the downstream contract — clarifying the consumer use case, proposing multiple output shapes, and handling corrupted rows is the entire interview.

---

## 37. Streaming Interval Coverage on a Number Axis

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: interval, interval-aggregation, streaming, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/interval-pollution-coverage
- **related discussions**: 2

### Statement

Phone screen for SRE / SWE. Points stream in onto an axis `[0, 50]`; each point pollutes a length-1 interval centered at it (±0.5). Implement a function that ingests one point at a time and returns whether the full axis is polluted.

---

## 38. Design a Global Cache for Small Images

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: caching, cdn, scaling, database, distributed-systems, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/global-small-image-cache
- **related discussions**: 1

### Statement

Design a global small-image caching service backed by a fixed single database and serving hundreds of thousands of requests per second. Cover reads, updates, cache hierarchy, refresh or invalidation, and failures while allowing brief staleness.

---

## 39. ML System Design: Inference Serving with Back-of-Envelope Capacity Planning

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, inference, performance-modeling, arithmetic-intensity, kernel, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-ml-inference-serving-back-of-envelope
- **related discussions**: 1

### Statement

Onsite ML system design for a runtime / optimization-flavored MLE loop: design an inference serving system for a model with ~100M DAU and reason from first principles about memory footprint, bandwidth, and latency. Deep follow-ups move into accelerator efficiency — kernel fusion, memory layout, quantization-aware training, distillation, and model evaluation.

---

## 40. System Design: Elevator Operation Logic

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: scheduling, system-design, concurrency, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-elevator-operation-logic
- **related discussions**: 1

### Statement

Onsite system design: design the operation logic for a building's elevator system, scaling from single-car scheduling to multi-car coordination and tall-building zone partitioning (sky lobby).

---

## 41. System Design: Simulation Log Collection and Result Aggregation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, SWE, Infra Eng
- **tags**: data-engineering, etl, evaluation, storage, scaling, calibration, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-simulation-log-aggregation
- **related discussions**: 1

### Statement

Onsite system design: build a system that collects logs from large-scale autonomous-driving simulations and aggregates them into evaluation metrics. Cross-cutting follow-up: how do you reconcile the abundance of simulation data against the relative scarcity of real-world fleet data?

---

## 42. ML System Design: Choose Passenger Drop-off Location

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ranking, calibration, location, ml-knowledge, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-passenger-dropoff-location-ml
- **related discussions**: 1

### Statement

L6 ML design round: when a Waymo passenger arrives at the destination, decide where exactly to pull over. Walk through model features, ranking objective, safety constraints, and how human / regulatory rules interact with the ML score.

---

## 43. System Design: Evaluation System with Human + LLM Evaluators

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: mlsd, evaluation, llm, data-cleaning, ranking, calibration, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-evaluation-system-llm-human
- **related discussions**: 1

### Statement

Onsite design: build an evaluation platform that grades candidate outputs using both human raters and LLM-as-judge. Cover labor pool routing, score aggregation, calibration of LLM judges against human gold, and the operational hand-off between the two evaluator types.

---

## 44. System Design: Evaluate a Self-Driving Model End-to-End

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, SWE
- **tags**: mlsd, evaluation, simulation, ml-knowledge, open-ended, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-evaluate-self-driving-model
- **related discussions**: 1

### Statement

Open-ended onsite design: how would you evaluate a self-driving model? Reported as deliberately unstructured — interviewer asked about simulating emergent road conditions, detecting sim-to-real gap, accelerating simulations under compute budget, and handling buggy simulator artifacts. No whiteboard, no notes — verbal only.

---

## 45. System Design: Parking Lot + Robotaxi Dispatch

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-09-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, scheduling, queueing, data-modeling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-parking-and-dispatch
- **related discussions**: 1

### Statement

Onsite system design: model a parking lot for an autonomous fleet, with vehicles cycling between charging spots, maintenance bays, and idle spots. Riders arrive at the lot entrance and the system dispatches the next available vehicle. Discuss performance metrics that capture both rider experience and fleet utilization.

---

## 46. ML System Design: Behavior Prediction from Sensor + Camera Data

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ml-knowledge, transformer, attention, calibration, evaluation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/sd-ml-behavior-prediction-from-sensors
- **related discussions**: 3

### Statement

Onsite ML system design: train one or more agents that predict the behavior of surrounding vehicles, pedestrians, and cyclists from the AV's sensor and camera data, including uncertainty quantification handed downstream to the planner. Reported across MLE phone screens and senior onsite loops; deep follow-ups on loss design, uncertainty calibration, and long-tail data curation.

---

## 47. L5+ Behavioral: Policy vs Efficiency Trade-offs & Leadership

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-14
- **stage**: onsite-bq
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, leadership, ambiguity, conflict, deep-dive, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/behavioral-policy-vs-efficiency-leadership
- **related discussions**: 1

### Statement

L6 behavioral conducted by a senior Director. Discussion-style — interviewer probes past leadership stories (tree-level and forest-level) and weaves in policy-vs-efficiency dilemmas that feel like trolley-problem variants. No standard 'tell me about a time' prompts; expect to defend judgments live.

---

## 48. Project Deep-Dive + ML Fundamentals Discussion

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-13
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: MLE, RS
- **tags**: behavioral, deep-dive, ml-knowledge, presentation, open-ended, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/project-deep-dive-ml-fundamentals
- **related discussions**: 2

### Statement

MLE / senior loop round: walk through one or two past projects with an experienced interviewer who interrupts continuously to probe data scale, model choices, ablation results, and failure modes. Frequently bleeds into an ML fundamentals oral covering the candidate's stated areas of expertise.

---

## 49. SWE Resume Deep Dive: Technologies and Outcome Metrics

- **category**: other
- **duration**: 15 min
- **frequency**: Single report
- **last asked**: 2026-08-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: deep-dive, metrics, open-ended, medium
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/swe-resume-deep-dive-technologies-metrics
- **related discussions**: 1

### Statement

The opening portion of a 45-minute SWE phone screen drills into older resume projects: exactly what the candidate did, which technologies were used, and how the results were measured. Roughly 30 minutes of C++ debugging and coding followed this discussion.

---

## 50. Data Fluency: Self-Driving Progress Metrics & Experiment Comparison

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-24
- **stage**: onsite-research
- **access**: Members
- **roles**: SWE, MLE
- **tags**: experiment-design, ab-testing, evaluation, open-ended, hard
- **url**: https://www.1point3acres.com/interview/problems/company/waymo/data-fluency-progress-metrics
- **related discussions**: 3

### Statement

Waymo signature round with no LC analog. Two simulation experiments produce metrics — collision rate, intervention rate, planner latency — and the candidate must reason about which experiment 'won' and how to design a statistically defensible comparison. Often paired with an open-ended sub-question on AD-specific progress metrics.

---
