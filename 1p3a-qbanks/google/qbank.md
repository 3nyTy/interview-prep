# Google — 1point3acres Question Bank (75 curated questions)

Source: 1point3acres qbank (`company=google`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/google>

The site lists 493 entries for this company: **75 curated questions** (below), 418 OJ links, 0 external links. Only the curated 75 are captured here.

> Google's loop runs on hiring-committee arbitration, with a separate team-match phase deciding whether HC approval turns into an offer. Coding bars are remarkably consistent across teams; leveling rigor and behavioral depth drive most of the L3–L6 differentiation.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Meeting Rooms / Interval Scheduling Variants
2. Friend Graph with Timeline (Friend / Unfriend Events)
3. Top-K Frequent in Large Logs
4. Trie Prefix Search / Autocomplete
5. OA: Collect Coins on 1D Board
6. OA: Largest Group Sharing a Digit
7. Temperature Monitoring Data Structure
8. Minimum-Cost Tree Disconnection
9. Longest Same-Color Grid Sequence
10. Next-Word Predictor
11. Implement Python's itertools.tee
12. Nested List Weight Sum (LC 339) with Follow-Ups
13. Cutting Ribbons (LC 1891)
14. Union of Sorted Interval Lists
15. Template Variable Expansion with Follow-Ups
16. Target Expressions from Ordered Digits
17. Progressive Array Partitioning
18. Streaming Median (LC 295) with Follow-ups
19. Batch and Streaming Log Deduplication
20. Minimum Direction Violations in a Directed Graph
21. Snake Case to Camel Case with Repository-Wide Refactor
22. Count Word Decompositions into Chemical Element Symbols
23. Longest Consecutive Path in Binary Tree
24. Website Activity Analytics: Unique Users + Session Time
25. Active Communication Top Users
26. Streaming Points: Emit K Within Distance
27. First Bad Version with Parallel Search Follow-up
28. Employee Hierarchy Add / Delete
29. Tic-Tac-Toe System with K Players and 3-in-a-Row Win
30. OA: Email Checksum + Verification-String Lookup
31. OA: Feature Rollout with Dependency Cycle Detection
32. Count Perfect Wake Numbers up to N
33. Gate Entry / Exit Time Resolution with Tie-Break Rules
34. Necklace Cut into Two Halves with Equal D/R Counts
35. Fence Painting Minimum Operations (Gemini Team)
36. Gold Chain Split into Two Equal Halves
37. Sum of Good Subarrays (Adjacent Diff ±1)
38. Streaming Insert + Find K-th Largest
39. String Decompression with Nested Counts
40. Google Doc Line Wrap (Word Wrap with Whitespace and Newlines)
41. Restaurant Waitlist: join / delete / find_first_match
42. Run-Length Vector Storage + Dot Product
43. Tree Distance Sum (Re-root DP)
44. Babylon Tile-Stack Merge Game
45. Wordle-Style 5-Letter Minimum-Guess Strategy
46. Two-Day Log Intersection under Memory Limit
47. Find All Bad Pairs Using runTest Oracle
48. Shortest Directed Cycle Through a Node
49. Rotate Any m×n 2D Matrix
50. Jump Game with Profit
51. Russian Doll Envelopes
52. Robot Status Message Deduplication Window
53. Rotten Oranges / Multi-Source BFS (taxis)
54. Compute File System Total Size
55. Number of Islands
56. 2D Points Connected Components by Radius
57. Decode String
58. Mahjong Winning Hand Detection (14 Tiles)
59. Piano Hand Movement Grouping
60. Path Minimizing the Maximum Cell (Swim in Rising Water)
61. Expression Tree Single-Leaf Mutation
62. Greedy Longest-Match Dictionary Tokenizer
63. Nested Function Expression Evaluator
64. Huffman Tree Construction
65. Matrix Flower Placement with House Adjacency
66. Count Same-Color Squares in Matrix
67. ML System Design (Recsys / Chatbot / Image Classifier)
68. L6 System Design (Staff Loop)
69. Large-Scale Interactive Map Visualization
70. Web Crawler at Search Scale
71. Behavioral Round (Googliness Themes)
72. Google Hiring Assessment (GHA / Work Styles)
73. ML Fundamentals Deep Dive (AI/ML & MLE Roles)
74. Research Paper Presentation and Defense (DeepMind RS)
75. CPU Architecture and Simulator Design Deep Dive

---

## 1. Meeting Rooms / Interval Scheduling Variants

- **category**: coding
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: phone-screen · tech-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: interval, heap, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/meeting-rooms-variants
- **related discussions**: 15

### Statement

LC 253 (Meeting Rooms II) appears in many forms across phone screen and onsite coding rounds — minimum number of rooms, room assignment, cyclic time, daily server jobs, gate entry/exit. Common follow-ups push toward sweep line, min-heap, or distributed processing.

---

## 2. Friend Graph with Timeline (Friend / Unfriend Events)

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-12
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/friend-graph-with-timeline
- **related discussions**: 4

### Statement

Onsite coding question family: given a chronologically ordered log of friend / unfriend events on N people, answer connectivity questions over time. Base case is union-find for friend-only events; the follow-up adds deletions, pushing toward segment tree or fully dynamic connectivity.

---

## 3. Top-K Frequent in Large Logs

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-07-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: heap, hashmap, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/top-k-frequent-from-logs
- **related discussions**: 5

### Statement

Coding round staple: parse a log/chat stream and return the top-K most-frequent keys (users, IPs, words). Implementation walk-through covers heap vs quickselect vs bucket sort, complexity discussion, and scale-out follow-ups (large file, memory limit, distributed).

---

## 4. Trie Prefix Search / Autocomplete

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-07-02
- **stage**: tech-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: trie, tree, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/trie-prefix-search-autocomplete
- **related discussions**: 4

### Statement

LeetCode 208 + 1268. Build a trie from a dictionary and return all words sharing a query prefix. Standard Google onsite coding.

---

## 5. OA: Collect Coins on 1D Board

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-05-07
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/oa-collect-coins-1d-board
- **related discussions**: 5

### Statement

One of two problems in the 90-min Google SWE Early Career / NG OA. A 1D string board contains empty cells `.`, player tokens `T`, and coins `C`; each move slides any token exactly 3 cells right and may land on a coin (collected once). Find the maximum total coins collectable.

---

## 6. OA: Largest Group Sharing a Digit

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-05-07
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/google/oa-group-two-digit-numbers-shared-digit
- **related discussions**: 4

### Statement

Second problem in the 90-min Google SWE Early Career / NG OA. Given a list of two-digit numbers, return the size of the largest subset where every chosen number shares at least one common digit.

---

## 7. Temperature Monitoring Data Structure

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, streaming, sliding-window
- **url**: https://www.1point3acres.com/interview/problems/company/google/temperature-monitor-moving-window
- **related discussions**: 1

### Statement

Implement temperature insertion, a moving average, and a constant-time maximum query for a recent window. Clarify whether the window is based on reading count or elapsed days.

---

## 8. Minimum-Cost Tree Disconnection

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dp
- **url**: https://www.1point3acres.com/interview/problems/company/google/binary-tree-root-leaf-minimum-cut
- **related discussions**: 1

### Statement

Cut weighted edges in a binary tree to disconnect the root from every original leaf at minimum total cost. Begin with nonnegative weights, then analyze what changes when negative weights are allowed.

---

## 9. Longest Same-Color Grid Sequence

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, grid
- **url**: https://www.1point3acres.com/interview/problems/company/google/longest-same-color-grid-line
- **related discussions**: 1

### Statement

Given a grid of colors, return the longest consecutive same-color sequence along a horizontal, vertical, or diagonal line.

---

## 10. Next-Word Predictor

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-02
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, data-structure
- **url**: https://www.1point3acres.com/interview/problems/company/google/next-word-frequency-predictor
- **related discussions**: 1

### Statement

Process training data and predict the most frequent next word for a supplied word. Maintain prediction state during processing so prediction can run in constant time.

---

## 11. Implement Python's itertools.tee

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: iterator, queue, data-structure, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/python-tee-independent-iterators
- **related discussions**: 1

### Statement

Implement the behavior of Python's `tee`: accept one iterator and an integer `n`, then return `n` independent iterators over the same source sequence. Define the required function and classes yourself and improve a naive design toward an efficient shared-state implementation.

---

## 12. Nested List Weight Sum (LC 339) with Follow-Ups

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, recursion, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/nested-list-weighted-sum-parsing
- **related discussions**: 1

### Statement

LC 339 Nested List Weight Sum, followed by a raw-string parsing variant. Track depth through brackets, accumulate multi-digit integers, and flush each number at a comma, closing bracket, or end of input.

---

## 13. Cutting Ribbons (LC 1891)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, binary-search, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/cutting-ribbons-max-equal-length
- **related discussions**: 1

### Statement

LC 1891 Cutting Ribbons: given wood lengths and K, maximize the equal piece length that yields at least K pieces. Follow-ups cover the search bound, floating-point lengths, and why complexity depends on the value range rather than the number of elements.

---

## 14. Union of Sorted Interval Lists

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, two-pointer, heap, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/sorted-interval-list-union
- **related discussions**: 1

### Statement

Reverse of LC 986: given two individually sorted, internally non-overlapping interval lists, return their union. Follow-ups extend to K lists with a heap, inputs too large for memory, and lists that may overlap internally.

---

## 15. Template Variable Expansion with Follow-Ups

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, graph, topological-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/template-variable-expansion-dependencies
- **related discussions**: 1

### Statement

Replace `%KEY%` placeholders using a key-value map whose values may contain other placeholders. Clarify malformed delimiters and literal-percent escaping, flatten nested values, and detect cyclic dependencies before expansion.

---

## 16. Target Expressions from Ordered Digits

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, backtracking, dp, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/target-expressions-ordered-digits
- **related discussions**: 1

### Statement

Given digits in fixed order and a target, enumerate every expression using `+`, `*`, and parentheses that evaluates to the target under standard precedence. The follow-up generalizes from three digits to N; the available solution signal is memoization plus dynamic programming, without a fully specified state contract.

---

## 17. Progressive Array Partitioning

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/array-partition-unique-frequencies-straights
- **related discussions**: 1

### Statement

Given an integer multiset, first decide whether grouping equal values produces pairwise-distinct group sizes; then solve a follow-up in which every partition must form a consecutive run. The second variant's exact run-length rule is underspecified, so clarify it against the supplied examples before coding.

---

## 18. Streaming Median (LC 295) with Follow-ups

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-03
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: heap, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/streaming-median-with-follow-ups
- **related discussions**: 1

### Statement

A near-canonical LC 295 streaming-median implementation using two heaps, followed by two design extensions: exploit a very small value domain, then support an arbitrary k-th-smallest query instead of only the median.

---

## 19. Batch and Streaming Log Deduplication

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dedup, streaming, heap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/batch-streaming-log-deduplication
- **related discussions**: 1

### Statement

Process `(timestamp, message)` logs by deduplicating on message and sorting retained entries by timestamp, first keeping the first-seen entry and then the latest timestamp. The streaming extension adds `add(timestamp, message)` and `get_next()`, requiring a map plus min-heap, lazy deletion, and a discussion of watermarks and bounded state.

---

## 20. Minimum Direction Violations in a Directed Graph

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, bfs, dijkstra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/minimum-direction-violations-directed-graph
- **related discussions**: 1

### Statement

Given directed edges that may be traversed forward at cost 0 or backward at cost 1, return the minimum number of direction violations from a start node to an end node. The expected solution models both directions and applies 0-1 BFS, with Dijkstra as a valid alternative.

---

## 21. Snake Case to Camel Case with Repository-Wide Refactor

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, refactoring, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/snake-case-repository-refactor
- **related discussions**: 1

### Statement

Convert a valid snake_case identifier to camelCase, then design a safe repository-wide migration for variables and function names without changing third-party symbols, public interfaces, or the tests' ability to detect regressions.

---

## 22. Count Word Decompositions into Chemical Element Symbols

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/periodic-table-word-decomposition
- **related discussions**: 1

### Statement

Given the set of chemical-element symbols and a target word, return the number of distinct ways the word can be segmented entirely into valid symbols.

---

## 23. Longest Consecutive Path in Binary Tree

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/binary-tree-longest-consecutive-path
- **related discussions**: 1

### Statement

Given a binary tree, return the length of the longest parent-to-child path whose values increase consecutively by 1. Follow-up: allow the path to extend in any direction through the tree, not only from parent to child.

---

## 24. Website Activity Analytics: Unique Users + Session Time

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-26
- **stage**: phone-screen
- **access**: Members
- **roles**: DS
- **tags**: python, hashmap, data-analysis, sql, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/website-activity-analytics-unique-users-session-time
- **related discussions**: 1

### Statement

Given website action records with `user_id`, timestamp, and activity, compute per-activity unique-user counts and average time spent on the site per user in seconds. The screen paired this Python task with a lighter SQL section and emphasized edge-case discussion over perfect final code.

---

## 25. Active Communication Top Users

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, set, sorting, top-k, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/active-communication-top-users
- **related discussions**: 1

### Statement

Process a continuous stream of user pairs such as `A-B` and `B-C`; each pair creates an active communication edge, and the system should output users ordered by current communication count. Follow-ups add self-communication handling, duplicate communication dedupe, and top-K users by count.

---

## 26. Streaming Points: Emit K Within Distance

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, sorting, sliding-window, medium, ambiguous-spec
- **url**: https://www.1point3acres.com/interview/problems/company/google/streaming-points-k-within-distance
- **related discussions**: 1

### Statement

Process a stream of float positions. Whenever three points satisfy the distance condition, output and remove those three points; a follow-up generalizes from 3 points to K points. The important ambiguity is whether the condition means a span of length `d` or pairwise distance within `d`.

---

## 27. First Bad Version with Parallel Search Follow-up

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, parallelism, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/first-bad-version-parallel-search
- **related discussions**: 1

### Statement

Phone-screen R1 coding round. The base problem is the standard first-bad-version monotonic search — given an `isBadVersion(v)` oracle over versions `1..n`, find the first bad version with a single binary search. The graded follow-up requires parallelizing the search: partition the range into buckets and search them concurrently (bucket + binary search), with working code expected.

---

## 28. Employee Hierarchy Add / Delete

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, hashmap, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/employee-hierarchy-add-delete
- **related discussions**: 1

### Statement

Design an employee hierarchy class that supports adding employees under managers and deleting employees by id. Follow-ups cover deleting managers, deleting the CEO, cycle handling, and choosing a replacement manager by seniority.

---

## 29. Tic-Tac-Toe System with K Players and 3-in-a-Row Win

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-18
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, grid, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/tic-tac-toe-k-players-three-in-a-row
- **related discussions**: 1

### Statement

Design a Tic-Tac-Toe system for `k` players on an `n x n` board. After each move, return the game status; any player wins immediately after forming 3 consecutive marks in a straight line.

---

## 30. OA: Email Checksum + Verification-String Lookup

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-05-17
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/google/oa-email-checksum-verification-string
- **related discussions**: 1

### Statement

SDE II Early-Career HackerRank OA, problem 1 of 2 (90 min total). Implementation-heavy: slice an email body into k-character chunks, map each chunk to a checksum integer using a fixed letter→digit table, then index into a verification string (with modular wrap) to produce the final verification token. Framed as Gmail's anti-spam check.

---

## 31. OA: Feature Rollout with Dependency Cycle Detection

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-17
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, topological-sort, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/oa-feature-rollout-dependency-cycle-report
- **related discussions**: 1

### Statement

SDE II Early-Career HackerRank OA, problem 2 of 2 (90 min total). A multi-stage feature-gating system: first reject users by country and OS-version rules, then evaluate per-feature dependencies that may themselves be pending rollout, while detecting and *naming every member of* dependency cycles. Most of the work is fixing a buggy parser and extending an `evaluate()` function across several missing branches. AI helper enabled inside HackerRank.

---

## 32. Count Perfect Wake Numbers up to N

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-07
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, bit-manipulation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/perfect-wake-number-count
- **related discussions**: 1

### Statement

Fresh coding round 1: define a Perfect Wake Number as a positive integer with all distinct digits, no `0`, and no digit adjacent to two strictly-larger digits. Given `n`, count how many Perfect Wake Numbers lie in `[1, n]`. The base check is easy; the count follow-up is the actual signal.

---

## 33. Gate Entry / Exit Time Resolution with Tie-Break Rules

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, algorithm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/gate-entry-exit-scheduler
- **related discussions**: 1

### Statement

Onsite coding round 2: given an array indexed by person id, each containing `(timestamp, action ∈ {enter, exit})`, return per-person the actual time they passed through the gate after applying tie-break rules between simultaneous enters and exits.

---

## 34. Necklace Cut into Two Halves with Equal D/R Counts

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: string, two-pointer, math-reasoning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/necklace-cut-equal-counts
- **related discussions**: 1

### Statement

Onsite coding round 4 for an L4 AI/ML loop: given a circular string of `D` and `R` with equal counts of each, find a way to make at most 2 cuts to produce two pieces such that each piece has the same number of `D`s and the same number of `R`s.

---

## 35. Fence Painting Minimum Operations (Gemini Team)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-27
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/fence-painting-min-ops
- **related discussions**: 1

### Statement

Asked in a Gemini team coding round: given the heights of N adjacent fence planks (each width 1), compute the minimum number of paint-roller operations to cover the fence, given a 1-unit-wide brush that either paints one plank top-to-bottom (cost 1) or paints a horizontal strip across a contiguous block of planks tall enough (cost 1).

---

## 36. Gold Chain Split into Two Equal Halves

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/gold-chain-split-equal
- **related discussions**: 1

### Statement

Onsite coding interview 1: given a gold chain represented by an array of segment weights, remove exactly one segment and check whether the remaining segments — when reconnected — can be cut once more to give two halves of equal total weight. Follow-up: enumerate all valid (remove, cut) pairs.

---

## 37. Sum of Good Subarrays (Adjacent Diff ±1)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-08
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: array, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/good-subarrays-diff-one
- **related discussions**: 1

### Statement

MLE L5 coding round: sum over all contiguous subarrays whose elements form a strictly monotonic ±1 step sequence (every adjacent pair differs by exactly +1, OR every adjacent pair differs by exactly -1). Required time complexity is O(N).

---

## 38. Streaming Insert + Find K-th Largest

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, tree, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/insert-find-kth-largest
- **related discussions**: 1

### Statement

Onsite coding round 1 in NYC: design a class with `insert(num)` and `findLargest(k)` where `findLargest(k)` returns the `(k+1)`-th largest value with ties allowed. Time complexity of find should be as low as possible.

---

## 39. String Decompression with Nested Counts

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/string-decompression-nested-counts
- **related discussions**: 1

### Statement

NYC onsite coding round 2: decode a custom compressed string format where parenthesized groups are repeated using a `{k}` suffix. Variant of LC 394 with different bracketing and an explicit assumption that input is well-formed.

---

## 40. Google Doc Line Wrap (Word Wrap with Whitespace and Newlines)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/document-line-wrap
- **related discussions**: 1

### Statement

Coding round 1 of an SDE NG onsite, framed as "imagine a Google Doc where each page has a limited width": given a text blob and a max line width, return how many lines the page needs while handling newlines and whitespace correctly.

---

## 41. Restaurant Waitlist: join / delete / find_first_match

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/waitlist-data-structure-with-table-size-match
- **related discussions**: 1

### Statement

Onsite coding round: design and implement a restaurant waitlist data structure supporting join, delete, and find_first_match(table_size). The find call returns the earliest-joined user whose party size fits the given table.

---

## 42. Run-Length Vector Storage + Dot Product

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: data-structure, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/compressed-vector-dot-product
- **related discussions**: 1

### Statement

Phone-screen-style coding round for an AI Infra L4 loop: design an efficient storage format for a very long integer vector with many repeated values, then implement dot-product over two such compressed vectors.

---

## 43. Tree Distance Sum (Re-root DP)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dp, dfs, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/tree-distance-sum-reroot
- **related discussions**: 1

### Statement

Onsite coding round 1: given an undirected tree on N nodes, return for every node the sum of distances to all other nodes. Equivalent to LC 834. Interviewer expects baseline O(n²) DFS followed by the O(n) re-root DP.

---

## 44. Babylon Tile-Stack Merge Game

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-29
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, data-structure, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/babylon-stack-merge-game
- **related discussions**: 1

### Statement

Onsite NG round 1 (Munich office): implement a two-player tile-stack merge game ("Babylon") given the rules. Start state is 12 height-1 stacks of 4 colors (Y / W / G / B), 3 of each; a move places one stack on top of another under a constraint; the player who cannot move loses.

---

## 45. Wordle-Style 5-Letter Minimum-Guess Strategy

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-15
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: algorithm-design, greedy, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/wordle-min-guess-strategy
- **related discussions**: 1

### Statement

Phone screen coding: given a black-box oracle that tells you whether a specific letter is at a specific position in a hidden 5-letter word, design an algorithm to identify the word in as few oracle calls as possible. Follow-up assumes a dictionary of candidate words is available.

---

## 46. Two-Day Log Intersection under Memory Limit

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: hashmap, two-pointer, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/two-logs-intersection-memory-limit
- **related discussions**: 1

### Statement

PhD intern coding round 1: two log files with `(timestamp, obj_id, client_id)` entries. Find `obj_id`s that (a) appear in both files and (b) have at least two distinct `client_id`s. Follow-up imposes a memory limit, expecting external sort + two-pointer.

---

## 47. Find All Bad Pairs Using runTest Oracle

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, RS
- **tags**: algorithm-design, math-reasoning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/find-bad-pairs-with-oracle
- **related discussions**: 1

### Statement

PhD intern coding round 2: given a black-box oracle `runTest(S)` that returns `true` iff a subset `S` contains no bad pair, identify all bad pairs across N test items with as few oracle calls as possible.

---

## 48. Shortest Directed Cycle Through a Node

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: —
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/shortest-cycle-through-node
- **related discussions**: 1

### Statement

Round 4 of a 4-round NG loop. BFS from the target node in a directed graph; return the moment BFS re-enters the node.

---

## 49. Rotate Any m×n 2D Matrix

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: —
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/rotate-matrix-rectangle
- **related discussions**: 1

### Statement

LeetCode 48 generalized to rectangles. Out-of-place rotation is trivial; in-place rotation for `m ≠ n` is the trap.

---

## 50. Jump Game with Profit

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: —
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/jump-game-with-profit
- **related discussions**: 1

### Statement

LC 55 + LC 322 hybrid. From index `i`, take `profit[i]` and jump to `i + 1 + arr[i]`, or skip; maximize profit reaching the end.

---

## 51. Russian Doll Envelopes

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, dp, binary-search, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/russian-doll-envelopes
- **related discussions**: 1

### Statement

LeetCode 354. Maximum chain of envelopes that strictly fit inside each other. Sort + LIS reduction.

---

## 52. Robot Status Message Deduplication Window

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-09-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/google/robot-status-message-deduplication
- **related discussions**: 2

### Statement

Process timestamped robot messages and display a message only when it has not been displayed within the prior 10 seconds. Hidden arrivals do not refresh the last-displayed timestamp, and the exact 10-second boundary must be clarified.

---

## 53. Rotten Oranges / Multi-Source BFS (taxis)

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-09-08
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: grid, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/rotten-oranges-multi-source-bfs-taxis
- **related discussions**: 2

### Statement

LC 542 / 994 variant — for every cell, distance to the nearest active source (a 'taxi'). Multi-source BFS is the canonical solution.

---

## 54. Compute File System Total Size

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-09-02
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/file-system-total-size
- **related discussions**: 3

### Statement

Early Career / onsite coding prompt: traverse a file-system object with `class`, `size`, and `contains` fields and return the total size of all nested files. Follow-ups ask how to make repeated size computation faster.

---

## 55. Number of Islands

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-08-29
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: grid, dfs, bfs, union-find, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/number-of-islands
- **related discussions**: 3

### Statement

LeetCode 200. Count 4-connected components of 1s in a binary grid. DFS or BFS or Union-Find — all accepted; Google asks the follow-ups.

---

## 56. 2D Points Connected Components by Radius

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, bfs, union-find, clustering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/points-connected-components-by-radius
- **related discussions**: 2

### Statement

Given 2D points and radius `r`, connect two points when their distance is at most `r`; connectivity is transitive, and the output is the final number of clusters. The interviewer accepted or preferred a brute-force neighbor scan after discussing optimization ideas.

---

## 57. Decode String

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-07-29
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: string, stack, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/decode-string
- **related discussions**: 3

### Statement

LeetCode 394. Decode `k[group]` expansion. Iterative stack solution; Google variants swap the syntax (e.g. `(group){k}`) or add nesting rules.

---

## 58. Mahjong Winning Hand Detection (14 Tiles)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-07-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: recursion, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/mahjong-winning-hand-check
- **related discussions**: 2

### Statement

Mountain View onsite coding round 1: given a hand of 14 tiles, decide whether it is a winning mahjong hand. The candidate must clarify the rule set (typically: 4 melds of three identical tiles or three consecutive ranks + 1 pair).

---

## 59. Piano Hand Movement Grouping

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-07-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, simulation, sliding-window, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/piano-hand-movement-grouping
- **related discussions**: 3

### Statement

Given a sequence of integer piano-key positions and a maximum hand span k, compute the minimum number of hand lifts needed to play the sequence. Another version asks to print each group of notes playable from one hand position.

---

## 60. Path Minimizing the Maximum Cell (Swim in Rising Water)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-07-08
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, heap, algorithm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/path-min-max-grid-dijkstra
- **related discussions**: 2

### Statement

A bottleneck-path family: minimize the maximum value encountered on a grid path, then generalize the same reasoning to a weighted graph whose edges carry safety values and an admissibility threshold.

---

## 61. Expression Tree Single-Leaf Mutation

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, simulation, medium, state-machine
- **url**: https://www.1point3acres.com/interview/problems/company/google/expression-tree-single-leaf-mutation
- **related discussions**: 2

### Statement

Evaluate a boolean expression tree, then answer the root value after each single-leaf flip. Current variants include XOR/OR/AND/NOT operators, no parent pointer on the node interface, and an expectation that the candidate cache parent links or paths during the initial traversal.

---

## 62. Greedy Longest-Match Dictionary Tokenizer

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, trie, hashmap, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/tokenize-longest-dict-match-greedy
- **related discussions**: 2

### Statement

Phone-screen coding round (R1). Tokenize a text against a dictionary of `key:id` entries using greedy longest-match; emit the matched id when a key matches, otherwise output the unmatched character as a literal. One follow-up on changing the tie-breaking / priority rule for which token wins. A second follow-up appears when the dictionary is very large: discuss how to keep the matcher memory- and lookup-efficient at scale.

---

## 63. Nested Function Expression Evaluator

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: parsing, recursion, stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/nested-function-expression-evaluator
- **related discussions**: 2

### Statement

Parse and evaluate nested function-style arithmetic expressions such as `add(2, mul(3, pow(4, 5)))`. The follow-up extends fixed-arity operators to variable-length argument lists.

---

## 64. Huffman Tree Construction

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, heap, greedy, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/huffman-tree-construction
- **related discussions**: 2

### Statement

Classic Huffman build from a frequency map. L6 onsite coding — interviewer expects 25 min of code-then-discuss.

---

## 65. Matrix Flower Placement with House Adjacency

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-31
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, backtracking, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/matrix-flower-placement-with-house-adjacency
- **related discussions**: 2

### Statement

Onsite coding prompt: place flowers on a grid with houses and empty cells. Each row and column can contain at most one flower, and every house must have exactly one adjacent flower that is not shared with another house.

---

## 66. Count Same-Color Squares in Matrix

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: —
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/google/count-same-color-squares
- **related discussions**: 2

### Statement

LeetCode 221 variant — count maximal same-color square sub-grids. DP on each cell as the bottom-right corner.

---

## 67. ML System Design (Recsys / Chatbot / Image Classifier)

- **category**: system-design
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-25
- **stage**: onsite-system-design · tech-screen
- **access**: Members
- **roles**: MLE, SWE, RS
- **tags**: system-design, retrieval, rag, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/google/ml-system-design-recsys-chatbot-classifier
- **related discussions**: 8

### Statement

MLE / AI-ML L4-L5 loops include one ML System Design round (~60 min). Prompts span recommender, chatbot, image classifier, video moderation, fraud detection. Interviewers probe pipeline structure, feature engineering, training/serving split, cold start, A/B testing, and latency vs accuracy trade-offs.

---

## 68. L6 System Design (Staff Loop)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-06
- **stage**: onsite-system-design · onsite-deep-dive
- **access**: Members
- **roles**: SWE, Infra Eng, EM
- **tags**: system-design, scaling, storage, messaging, rate-limiting
- **url**: https://www.1point3acres.com/interview/problems/company/google/l6-system-design-staff-loop
- **related discussions**: 6

### Statement

L6 (Staff) onsite includes two System Design rounds (one infra-leaning, one product-leaning), each 60 min. Common prompts include distributed rate limiter, global notification system, anomaly detection service, Find-My-Device, dictionary range query store, and global chain restaurant menu sync. Senior bar is on requirements clarification, deep-dives into trade-offs, failure modes, and evolution path.

---

## 69. Large-Scale Interactive Map Visualization

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: frontend, location, caching, scaling, sse, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/interactive-map-100m-datapoints
- **related discussions**: 1

### Statement

Design an interactive Google Maps visualization for more than 100 million data points. Use aggregated heatmaps at coarse zoom levels and individual pins at the final zoom, while addressing rendering technology, caching, live updates, client threading, failure recovery, and hardware/network limits.

---

## 70. Web Crawler at Search Scale

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: web-crawler, distributed-systems, scaling, dedup, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/web-crawler-at-search-scale
- **related discussions**: 1

### Statement

Design a periodic crawler for tens of billions of pages feeding a search index, without JavaScript rendering. Deep dives cover a two-level URL frontier, robots.txt caching, exact and near-duplicate detection, crawler traps, host-based sharding, checkpoint recovery, and a 24-hour full-recrawl target.

---

## 71. Behavioral Round (Googliness Themes)

- **category**: behavioral
- **duration**: 45 min
- **frequency**: Very high
- **last asked**: 2026-09-10
- **stage**: phone-screen · tech-screen · onsite-bq · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, RS, Infra Eng, All
- **tags**: behavioral, culture-fit
- **url**: https://www.1point3acres.com/interview/problems/company/google/bq-round-google-style
- **related discussions**: 46

### Statement

Standalone 45-minute BQ round used in nearly every Google interview loop (NG, intern, L4/L5/L6). Interviewers cycle through 6–10 stock prompts, take detailed notes, and aggressively follow up on the situation, action, and impact. The recurring themes are conflict, collaboration with different perspectives, ownership, dealing with ambiguity, and feedback.

---

## 72. Google Hiring Assessment (GHA / Work Styles)

- **category**: other
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-03
- **stage**: oa
- **access**: Free preview
- **roles**: SWE, MLE, RS, Infra Eng, All
- **tags**: behavioral, culture-fit
- **url**: https://www.1point3acres.com/interview/problems/company/google/google-hiring-assessment-gha
- **related discussions**: 8

### Statement

Non-coding online assessment sent by Google recruiting after a resume pass. Current variants use forced-choice work-style triples plus a situational judgement section on policy, ethics, confidentiality, and collaboration consistency.

---

## 73. ML Fundamentals Deep Dive (AI/ML & MLE Roles)

- **category**: other
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: onsite-deep-dive · phone-screen · tech-screen · onsite-research
- **access**: Members
- **roles**: MLE, RS, SWE, RE
- **tags**: ml-knowledge, transformer, rag, retrieval, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/google/ml-fundamentals-deep-dive
- **related discussions**: 12

### Statement

One round in every MLE / AI/ML loop is a 45-60 minute domain interview: 10 min resume walkthrough, then rapid-fire ML fundamentals, often around the candidate's chosen track (LLM / NLP / CV / Recsys). Interviewers go deep on why each concept works, not just definitions.

---

## 74. Research Paper Presentation and Defense (DeepMind RS)

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-08-13
- **stage**: onsite-research
- **access**: Members
- **roles**: RS
- **tags**: presentation, critique, deep-dive, vlm, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/deepmind-rs-paper-presentation-defense
- **related discussions**: 1

### Statement

Present one of your papers, then defend the motivation, design choices, assumptions, alternatives, limitations, and next research steps. For multimodal/VLM work, expect close scrutiny of training and post-training depth and whether the contribution is more distinctive than evaluation or benchmark assembly.

---

## 75. CPU Architecture and Simulator Design Deep Dive

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-07-24
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: Perf Eng
- **tags**: cpu-architecture, cpp, cs-fundamentals, technical-deep-dive, hard
- **url**: https://www.1point3acres.com/interview/problems/company/google/cpu-architecture-simulator-deep-dive
- **related discussions**: 1

### Statement

A virtual English-language CPU Architect loop combines an assembly-code test, a broad microarchitecture oral, and C++ simulator design. Core topics include branch prediction, MOESI, caches, MSHRs, VIPT, TLB/page walks, ROB behavior, simulation models, and simulator optimization.

---
