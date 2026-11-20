# ByteDance / TikTok — 1point3acres Question Bank (70 curated questions)

Source: 1point3acres qbank (`company=bytedance`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/bytedance>

The site lists 431 entries for this company: **70 curated questions** (below), 361 OJ links, 0 external links. Only the curated 70 are captured here.

> ByteDance / TikTok runs a strict one-round-at-a-time funnel with sharp org-by-org variance in bar, language, and round mix. Coding rounds skew toward classic LeetCode-medium with one self-built test case; MLE and SRE loops front-load 30+ minutes of oral 八股 before any code is written. Interviewer professionalism varies — strong candidates drive the round structurally rather than waiting to be assessed.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Number of Islands and Grid BFS Variants
2. LRU Cache (with TTL and LFU Follow-ups)
3. Basic Calculator (with and without parentheses)
4. Merge Intervals
5. Course Schedule and Topological Sort on a Directed Graph
6. Reverse Nodes in K-Group (incomplete tail variant)
7. Binary Tree Maximum Path Sum (with path reconstruction)
8. Seller Task Scheduler
9. Timestamped Task Scheduler
10. Pythagorean Triple in an Integer Array
11. Stickers to Spell Word (LeetCode 691)
12. Minimum Window Substring (LeetCode 76)
13. Build a Nested Comment Tree from Flat Records
14. AI-Assisted Order Validator
15. Bounded Number Construction from Allowed Digits
16. Longest Repeating Character Replacement
17. Sliding Window Maximum
18. Flatten a Multilevel Doubly Linked List (Skip Empty Nodes)
19. Equalize Root-to-Leaf Path Sums in an N-ary Tree
20. Grid Shortest Path with K Obstacle Eliminations
21. Elements Appearing More Than n/3 in a Sorted Array
22. Longest Zigzag Path in a Grid
23. N-ary Tree Path Sum Count
24. Word Search II with Trie
25. Minimum-Cost Path on a Grid with Fuel and Recharge Cells
26. Reverse Words in a String (Preserve Spaces, In-Place)
27. Photo Groups via Transitive Similarity
28. Binary Tree Right Side View
29. Valid Parentheses with Wildcard '*' and DFS All Strings
30. Largest Rectangle in Histogram
31. CodeSignal ML / Research Scientist OA (10-Problem Battery)
32. Contiguous Subarray with Largest Min + Max
33. Subarray Sum Equals K (Count)
34. Create Binary Tree from Descriptions (LeetCode 2196)
35. Longest Substring Without Repeating Characters
36. Intersection of Two Interval Lists
37. Reconstruct Binary Tree from Preorder and Postorder
38. Reverse a Linked List In Place
39. Non-Overlapping Intervals (LeetCode 435)
40. Time Based Key-Value Store (LeetCode 981)
41. Longest Substring with At Most K Distinct Characters
42. Restore IP Addresses
43. Alien Dictionary
44. Search in Rotated Sorted Array
45. MinStack, MaxStack and Streaming Median
46. Remove Duplicate Letters for Lexicographically Smallest Result
47. Hand-Code Self-Attention and Cross-Entropy
48. CodeSignal SWE OA (Spring 2026 Bank)
49. Longest Palindromic Substring (LeetCode 5)
50. LeetCode 20 Valid Parentheses in SRE Loop
51. Content Moderation System Design
52. Top Songs by Country Leaderboard
53. AI-Driven QA Pipeline Workflow
54. Recent Like Count and Top Posts in a Sliding Window
55. Realtime Auction Bidding System
56. Agent Tool-Use System Design (AML Volcano Engine)
57. ML System Design: Dynamic K in Retrieval Stage
58. Design a Stripe-like Payment Platform
59. Short-Video Recommendation System
60. Video-Keyword Association Review System
61. Project Deep Dive (Resume Drill)
62. Hiring Manager / Final Behavioral Round
63. Data Engineering Experience Screen
64. Motivation, Ambiguity, and Collaboration Screen
65. SRE Linux / Networking Troubleshooting Round
66. MLE Oral 八股: Dropout, Overfitting, Normalization, Loss Functions
67. CAP / DB Indexing / Concurrency / Logging Oral 八股
68. LLM Safety Project Deep Dive
69. AI Agent Intern Technical Questions
70. RAG / Agent / Kafka Oral Drill

---

## 1. Number of Islands and Grid BFS Variants

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-08
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: grid, bfs, dfs, union-find, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/number-of-islands-family
- **related discussions**: 10

### Statement

Grid traversal in the 'number of islands' family — count connected components, then a follow-up that adds a constraint such as no-modify-in-place, equal-shape detection, water-flow direction, or 2D scaling.

---

## 2. LRU Cache (with TTL and LFU Follow-ups)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, linked-list, ttl, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/lru-cache-with-ttl
- **related discussions**: 7

### Statement

Design and implement an LRU cache in O(1) per op. Follow-ups extend it with TTL expiry, LFU eviction, or fixed-time get/put semantics under contention.

---

## 3. Basic Calculator (with and without parentheses)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-26
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: stack, parsing, string-processing, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/basic-calculator
- **related discussions**: 5

### Statement

Implement a string calculator supporting +, -, *, / on integers. Variants range from no-parentheses (operator precedence only) to full infix with parentheses and unary minus.

---

## 4. Merge Intervals

- **category**: coding
- **frequency**: Medium · New
- **last asked**: 2026-08-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, interval, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/merge-intervals
- **related discussions**: 4

### Statement

The canonical LeetCode 56 prompt: merge every overlapping interval and return the non-overlapping intervals that cover the same ranges.

---

## 5. Course Schedule and Topological Sort on a Directed Graph

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: graph, bfs, dfs, topological-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/course-schedule-topo-sort
- **related discussions**: 5

### Statement

Detect whether a directed-graph schedule is satisfiable (cycle detection on a DAG), then extend to producing a valid order — used both as a SWE coding question and as the warmup before a word-ladder shortest-path follow-up.

---

## 6. Reverse Nodes in K-Group (incomplete tail variant)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: linked-list, two-pointer, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/reverse-nodes-in-k-group
- **related discussions**: 7

### Statement

Reverse a linked list in groups of K. Variants flip the convention for the final partial group, split into a two-step problem (single-list reverse warmup first, then grouped reverse), or invert the task entirely — reverse the order of the K-groups while keeping each group's internal order intact.

---

## 7. Binary Tree Maximum Path Sum (with path reconstruction)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, recursion, dp, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/binary-tree-max-path-sum
- **related discussions**: 4

### Statement

Classic 'max path sum that may bend at any node' on a binary tree, plus the harder follow-up of reconstructing the actual node sequence — not just the score.

---

## 8. Seller Task Scheduler

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: scheduling, heap, queue
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/seller-task-scheduler
- **related discussions**: 1

### Statement

Implement task intake and next-task selection with per-seller priority ordering and tier-weighted turns across sellers.

---

## 9. Timestamped Task Scheduler

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-08-20
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: heap, scheduling, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/timestamped-task-scheduler
- **related discussions**: 1

### Statement

Implement a task scheduler supporting `addTask(taskID, timestamp)`, `removeTask(taskID)`, and `popTask(num)`, where `popTask` removes and returns the first `num` tasks ordered by start timestamp.

---

## 10. Pythagorean Triple in an Integer Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, math, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/pythagorean-triple-integer-array
- **related discussions**: 1

### Statement

Given an integer array, decide whether three available elements can be chosen as a, b, and c such that a² + b² = c². Negative values, zero, and duplicate counts all matter; the expected general bound discussed in the round was quadratic time.

---

## 11. Stickers to Spell Word (LeetCode 691)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, state-compression-dp, bitmask, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/stickers-to-spell-word
- **related discussions**: 1

### Statement

Given reusable letter stickers and a target string, return the minimum number of stickers needed to assemble the target. The prompt was identified as LeetCode 691, with the target length limited to roughly the low teens.

---

## 12. Minimum Window Substring (LeetCode 76)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, sliding-window, hashmap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/minimum-window-substring
- **related discussions**: 1

### Statement

Find the shortest substring of S containing every character of T with multiplicity, then handle variants requiring a threshold count and returning all equally short answers. The prompt was identified as LeetCode 76.

---

## 13. Build a Nested Comment Tree from Flat Records

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-08-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, hashmap, data-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/build-nested-comment-tree
- **related discussions**: 1

### Statement

Transform a flat list of comment objects with id, parent_id, and text into a nested root array by attaching children recursively. The supplied example contains two roots and a grandchild, plus a parent_id typo in the displayed expected payload that should be clarified.

---

## 14. AI-Assisted Order Validator

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: ai-coding, ai-allowed, graph, oop-design, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/ai-assisted-order-validator
- **related discussions**: 1

### Statement

Build an `OrderValidator` that rejects prohibited items and out-of-range prices, supports adding and removing validation rules, and respects dependencies between rules. The follow-up changes the dependency graph when a rule is removed: its prerequisites become prerequisites of its downstream rules.

---

## 15. Bounded Number Construction from Allowed Digits

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy, backtracking, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/bounded-number-from-allowed-digits
- **related discussions**: 1

### Statement

Given an integer N and a set A of decimal digits reusable without limit, construct the largest number strictly smaller than N using only digits from A. One interview example uses N = 23415 and A = {2, 4, 9}, producing 22999.

---

## 16. Longest Repeating Character Replacement

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, sliding-window, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/longest-repeating-character-replacement
- **related discussions**: 1

### Statement

The unmodified LeetCode 424 prompt: replace at most `k` characters in an uppercase string and return the longest substring that can be made entirely one repeated character.

---

## 17. Sliding Window Maximum

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-07-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, sliding-window, queue, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/sliding-window-maximum
- **related discussions**: 1

### Statement

The unmodified LeetCode 239 prompt: given an integer array and a fixed window size, return the maximum value in every window as it advances one position at a time.

---

## 18. Flatten a Multilevel Doubly Linked List (Skip Empty Nodes)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/flatten-multilevel-list-skip-empty-nodes
- **related discussions**: 1

### Statement

LC 430 (Flatten a Multilevel Doubly Linked List) with one delta: nested nodes carry empty values and must be excluded from the flattened output. Asked alongside a binary-tree max-path-sum question in the same IC round.

---

## 19. Equalize Root-to-Leaf Path Sums in an N-ary Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/equalize-root-to-leaf-path-sums
- **related discussions**: 1

### Statement

After a warm-up (most frequent array element, ties broken toward the smaller number), the main problem gives an n-ary tree of integers where each operation increments one node's value by 1; return the minimum operations to make all root-to-leaf path sums equal. Both problems require self-written, executed test cases.

---

## 20. Grid Shortest Path with K Obstacle Eliminations

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, shortest-path, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/grid-shortest-path-with-obstacle-elimination
- **related discussions**: 1

### Statement

First-round Growth coding prompt: find the shortest path in a grid, then handle the follow-up where up to `k` obstacles can be eliminated. Full I/O was thin, but the required state is `(row, col, remaining_eliminations)` for the follow-up.

---

## 21. Elements Appearing More Than n/3 in a Sorted Array

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-26
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, binary-search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/sorted-array-elements-more-than-one-third
- **related discussions**: 1

### Statement

Given a sorted array of length at least 3, return every value whose frequency is greater than `n / 3`. Examples include `[1,1,2,4,4] -> [1,4]`; follow-up requires time complexity below `O(n)` by exploiting sorted order.

---

## 22. Longest Zigzag Path in a Grid

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, dfs, backtracking, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/longest-zigzag-path-grid
- **related discussions**: 1

### Statement

TT Ads phone screen. Given an integer grid, return the length of the longest path (4-directional moves) whose consecutive value comparisons strictly alternate — grid[a] < grid[b] > grid[c] < ... or the inverse. DFS over a (cell, last-direction) state, best memoized like a longest-increasing-path-in-matrix DP; solved with DFS + backtracking.

---

## 23. N-ary Tree Path Sum Count

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, prefix-sum, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/n-ary-tree-path-sum-count
- **related discussions**: 1

### Statement

Count target-sum paths in a tree, with the binary-tree version generalized to an N-ary tree. The expected optimal solution uses DFS plus a prefix-sum counter rather than checking every ancestor path.

---

## 24. Word Search II with Trie

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: trie, grid, dfs, backtracking, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/word-search-ii-trie
- **related discussions**: 1

### Statement

Find words from a dictionary inside a 2D character board. The expected solution builds a trie over the word list, then runs backtracking DFS from each board cell with in-place visited marking.

---

## 25. Minimum-Cost Path on a Grid with Fuel and Recharge Cells

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-05
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, dijkstra, bfs, dp, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/shortest-path-grid-with-fuel
- **related discussions**: 1

### Statement

Weighted-cost grid traversal from top-left to bottom-right with a fuel budget that depletes per step, can be refilled at marked cells, and is bounded by some cells being fully blocked.

---

## 26. Reverse Words in a String (Preserve Spaces, In-Place)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, two-pointer, in-memory-database, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/reverse-words-in-place
- **related discussions**: 1

### Statement

Reverse the word order in a string. Follow-ups: rewrite to preserve the original number of spaces between every position, and reduce extra space below `O(n)`.

---

## 27. Photo Groups via Transitive Similarity

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/friend-circles-photo-groups
- **related discussions**: 1

### Statement

Given a symmetric similarity matrix where `isSimilar[i][j] == 1` means photos i and j are directly similar, count the number of photo groups under transitive closure.

---

## 28. Binary Tree Right Side View

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-01
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, bfs, dfs, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/binary-tree-right-side-view
- **related discussions**: 1

### Statement

Standard tree-from-the-right view problem, but reported with the extra constraint of building the input tree yourself and writing all test scaffolding inside the round.

---

## 29. Valid Parentheses with Wildcard '*' and DFS All Strings

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, stack, dfs, backtracking, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/valid-parentheses-with-wildcard
- **related discussions**: 1

### Statement

Validate a parenthesis string where `*` can act as `(`, `)`, or empty. Follow-up: enumerate every valid concrete string the input can produce.

---

## 30. Largest Rectangle in Histogram

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, array, complexity-analysis, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/largest-rectangle-histogram
- **related discussions**: 1

### Statement

Given a histogram represented by an array of bar heights, compute the largest rectangular area contained inside it. Reported as the second problem in an NG SWE phone screen after a sliding-window warmup.

---

## 31. CodeSignal ML / Research Scientist OA (10-Problem Battery)

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-01-04
- **stage**: oa
- **access**: Members
- **roles**: MLE, RS
- **tags**: ml-knowledge, classification, linear-algebra, codesignal, oa, scikit-learn, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/codesignal-ml-rs-oa
- **related discussions**: 1

### Statement

10-question, 70-minute CodeSignal battery for ByteDance Research Scientist intern: a mix of ML multiple-choice on classical model trade-offs, a hand-computed forward pass, a local-maximum array algorithm, and from-scratch implementations of Bagging and k-Means.

---

## 32. Contiguous Subarray with Largest Min + Max

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-11-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/largest-min-max-subarray
- **related discussions**: 1

### Statement

A custom 30-minute coding question: given a positive integer array, find a contiguous subarray (length ≥ 2) maximizing `min + max`, return the sum.

---

## 33. Subarray Sum Equals K (Count)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, prefix-sum, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/subarray-sum-equals-k
- **related discussions**: 1

### Statement

Given an integer array and a target `k`, return the number of contiguous subarrays whose sum equals `k`. The exact interview signature, constraints, and examples remain unspecified.

---

## 34. Create Binary Tree from Descriptions (LeetCode 2196)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: —
- **access**: Members
- **tags**: tree, hashmap
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/create-binary-tree-from-descriptions
- **related discussions**: 1

### Statement

Construct a binary tree from descriptions.

---

## 35. Longest Substring Without Repeating Characters

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, sliding-window, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/longest-substring-without-repeating-characters
- **related discussions**: 1

### Statement

Given a string, return the length of its longest substring containing no repeated characters. The exact interview signature, character set, constraints, and examples remain unspecified.

---

## 36. Intersection of Two Interval Lists

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: interval, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/intersection-of-two-interval-lists
- **related discussions**: 1

### Statement

Given two interval lists, return their intersections. The prompt aligns with LC 986, while the interview report leaves sorting guarantees, endpoint semantics, and the exact output contract unspecified.

---

## 37. Reconstruct Binary Tree from Preorder and Postorder

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, divide-and-conquer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/reconstruct-binary-tree-from-preorder-and-postorder
- **related discussions**: 1

### Statement

Reconstruct a binary tree from its preorder and postorder traversals. The prompt aligns with LC 889, but the interview report does not specify uniqueness assumptions or the exact return contract.

---

## 38. Reverse a Linked List In Place

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: —
- **access**: Members
- **tags**: linked-list
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/reverse-linked-list-in-place
- **related discussions**: 1

### Statement

Reverse a linked list in place.

---

## 39. Non-Overlapping Intervals (LeetCode 435)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: —
- **access**: Members
- **tags**: interval, array
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/non-overlapping-intervals
- **related discussions**: 1

### Statement

Find the minimum removals needed to eliminate overlapping intervals.

---

## 40. Time Based Key-Value Store (LeetCode 981)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: —
- **access**: Members
- **tags**: hashmap, data-structure
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/time-based-key-value-store
- **related discussions**: 1

### Statement

Implement time-indexed key-value storage.

---

## 41. Longest Substring with At Most K Distinct Characters

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-09-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, hashmap, string, easy
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/longest-substring-k-distinct
- **related discussions**: 2

### Statement

Sliding-window warmup in a TT SDE phone screen, paired with a much harder grid-with-fuel BFS problem.

---

## 42. Restore IP Addresses

- **category**: coding
- **duration**: 60 min
- **frequency**: Low · New
- **last asked**: 2026-09-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, dfs, backtracking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/restore-ip-addresses
- **related discussions**: 2

### Statement

Partition a digit string into every valid IPv4 address: exactly four numeric segments, each valid and with no leading zero unless the segment itself is zero. The follow-up generalizes the partition to an arbitrary `k` supplied as another argument.

---

## 43. Alien Dictionary

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-09-02
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: graph, topological-sort, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/alien-dictionary
- **related discussions**: 2

### Statement

The standard Alien Dictionary problem: infer a valid character order from words already sorted under an unknown alphabet. The implementation must include every character, avoid duplicate indegree increments, detect impossible ordering constraints, and explain each graph update.

---

## 44. Search in Rotated Sorted Array

- **category**: coding
- **duration**: 45 min
- **frequency**: Low · New
- **last asked**: 2026-08-21
- **stage**: phone-screen · final-round
- **access**: Members
- **roles**: SWE
- **tags**: array, binary-search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/search-in-rotated-sorted-array
- **related discussions**: 2

### Statement

LeetCode 33: search for a target in an ascending array rotated at an unknown pivot and return its index or `-1`. A current intern-screen follow-up removes the distinct-values guarantee, requiring explicit handling of duplicate boundary values.

---

## 45. MinStack, MaxStack and Streaming Median

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: stack, heap, data-structure, streaming, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/streaming-median-min-max-stack
- **related discussions**: 2

### Statement

Three linked structures in a single MLE round: `MinStack` with O(1) min, `MaxStack`, and a streaming-median follow-up that pushes you to two heaps — then asks how to retrofit `MaxStack` to maintain the running median.

---

## 46. Remove Duplicate Letters for Lexicographically Smallest Result

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-07-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, stack, monotonic-stack, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/remove-duplicate-letters-lexicographically-smallest
- **related discussions**: 2

### Statement

Given a string, remove duplicate letters so each character appears exactly once and the final string is lexicographically smallest among all valid results. The round explicitly included the two standard examples `bcabc -> abc` and `cbacdcbc -> acdb`, with a monotonic-stack solution expected.

---

## 47. Hand-Code Self-Attention and Cross-Entropy

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-01
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, RS
- **tags**: transformer, attention, numpy, pytorch, ml-knowledge, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/self-attention-cross-entropy-pseudocode
- **related discussions**: 2

### Statement

Write self-attention pseudocode and the cross-entropy loss derivation on the spot, interleaved with attention-mechanism oral questions (FFN role, attention scaling).

---

## 48. CodeSignal SWE OA (Spring 2026 Bank)

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-03-17
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: simulation, sliding-window, greedy, string-processing, codesignal, oa, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/codesignal-swe-oa-spring-2026
- **related discussions**: 2

### Statement

TikTok's Spring 2026 SWE intern OA on CodeSignal: 4 problems covering memory allocator simulation, alternating-parity zigzag counting, charging-station relay, and constrained text justification.

---

## 49. Longest Palindromic Substring (LeetCode 5)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: —
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, palindromes, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/longest-palindromic-substring
- **related discussions**: 2

### Statement

Return the longest contiguous palindromic substring. A follow-up requires Manacher's algorithm rather than the classic expand-around-center baseline.

---

## 50. LeetCode 20 Valid Parentheses in SRE Loop

- **category**: coding
- **frequency**: Low
- **last asked**: —
- **stage**: —
- **access**: Members
- **tags**: stack, string
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/valid-parentheses
- **related discussions**: 2

### Statement

Validate parentheses in a short SRE coding exercise and run the test cases.

---

## 51. Content Moderation System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: moderation, ml-infra, messaging, scaling, evaluation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/content-moderation-system-design
- **related discussions**: 5

### Statement

End-to-end design of a content-moderation pipeline for short-video / e-commerce content. Variants include ML inference layer, feature pipeline, job scheduler follow-up, and review-queue prioritization.

---

## 52. Top Songs by Country Leaderboard

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-30
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: leaderboard, streaming, top-k, caching, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/top-songs-by-country-leaderboard
- **related discussions**: 1

### Statement

Design a near-real-time service that ingests song listen/click events and serves each country's top ten songs, refreshing about every ten minutes. The stated scale is roughly 500 million users and about 50K read QPS plus 50K event writes per second, with availability favored over strong consistency.

---

## 53. AI-Driven QA Pipeline Workflow

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-08-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: testing, agentic-workflow, llm-agent, ci-cd, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/ai-driven-qa-pipeline-workflow
- **related discussions**: 1

### Statement

Design an AI-oriented QA workflow that turns a PRD into test cases, test data, automation scripts, execution, and a final report. The interviewer expects collaborating role-specific agents rather than merely inserting model calls into conventional CI stages, and asks how the system operates when CI has no GPU capacity.

---

## 54. Recent Like Count and Top Posts in a Sliding Window

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: feed, ranking, streaming, sliding-window, redis, kafka, top-k, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/recent-like-count-and-top-posts
- **related discussions**: 1

### Statement

Design the last-minute like-count backend for a social platform: exact count for one post, global top posts over the last minute, and region-based top posts over the same window.

---

## 55. Realtime Auction Bidding System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-20
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: messaging, kafka, redis, websocket, sse, state-machine, scaling, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/realtime-auction-bidding-system
- **related discussions**: 1

### Statement

Design a high-read, high-write auction service with REST bids, ordered bid processing, durable bid history, current-price state, and realtime client updates through SSE or WebSocket.

---

## 56. Agent Tool-Use System Design (AML Volcano Engine)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-20
- **stage**: onsite-system-design · onsite-research
- **access**: Members
- **roles**: RS, MLE
- **tags**: mlsd, agents, tool-use, llm-agent, evaluation, open-ended, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/agent-tool-use-system-design
- **related discussions**: 1

### Statement

Senior research-scientist intern round at ByteDance AML (Volcano Engine): open-ended design of an agent tool-use system, drilling on state management, evaluation, and failure modes of multi-step tool-invoking agents.

---

## 57. ML System Design: Dynamic K in Retrieval Stage

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-28
- **stage**: onsite-system-design · onsite-research
- **access**: Members
- **roles**: MLE, RS
- **tags**: mlsd, retrieval, ranking, recommendation, calibration, open-ended, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/dynamic-k-retrieval-ml-system-design
- **related discussions**: 1

### Statement

Open-ended ML system design for a senior / staff MLE round: in a recommendation stack, design a model or system to dynamically determine `K` — the number of candidates passed from retrieval to ranking — per request, rather than using a static cap.

---

## 58. Design a Stripe-like Payment Platform

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: Infra Eng, SWE
- **tags**: payment, idempotency, transactions, scaling, distributed-systems, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/payment-platform-design
- **related discussions**: 1

### Statement

Open-ended payment-platform system design for an SRE candidate: payment intent lifecycle, idempotency, transactional integrity, retry semantics under partial failure.

---

## 59. Short-Video Recommendation System

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-08-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: recommendation, ranking, retrieval, two-tower, personalization, streaming, ab-testing, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/short-video-recommendation-system
- **related discussions**: 2

### Statement

Design a multi-stage short-video recommendation stack: parallel retrieval produces hundreds to thousands of candidates per route, coarse ranking reduces the pool, fine ranking optimizes several engagement objectives, and reranking enforces diversity and exploration. Deep dives cover cold start, event-time feature correctness, real-time behavior updates, and online evaluation.

---

## 60. Video-Keyword Association Review System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-07
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: schema-design, versioning, ingestion, video-generation, retrieval, job-system, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/video-keyword-association-review-system
- **related discussions**: 2

### Statement

Design a service where users submit batches of 50+ videos and 1000+ keywords, then the system evaluates association between videos and keywords while supporting updates, versioning, and resumable upload.

---

## 61. Project Deep Dive (Resume Drill)

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-06
- **stage**: phone-screen · onsite-coding · onsite-deep-dive · final-round
- **access**: Members
- **roles**: SWE, MLE, RS, Infra Eng
- **tags**: deep-dive, behavioral, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/project-deep-dive-round
- **related discussions**: 47

### Statement

Most rounds open with 15-30 minutes of resume deep-dive before any coding. Interviewers anchor on one project from your resume, drill into the technical decisions, and probe `why this trade-off, not that one`.

---

## 62. Hiring Manager / Final Behavioral Round

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-17
- **stage**: final-round · onsite-bq
- **access**: Members
- **roles**: SWE, MLE, RS, Infra Eng
- **tags**: behavioral, bq, star, deep-dive, leadership, fit, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/hm-behavioral-final-round
- **related discussions**: 9

### Statement

Final HM round, typically 30-45 min, mixing standard STAR-format behavioral questions with one extra project deep-dive — and on the director / M2 variant, motivation and impact rather than implementation details.

---

## 63. Data Engineering Experience Screen

- **category**: behavioral
- **frequency**: Single report
- **last asked**: 2026-08-31
- **stage**: recruiter-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-engineering, data-modeling, database
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/data-engineering-experience-screen
- **related discussions**: 1

### Statement

A recruiter screen follows a previously completed experience form, covering data modeling, warehouses, and data services.

---

## 64. Motivation, Ambiguity, and Collaboration Screen

- **category**: behavioral
- **frequency**: Low · New
- **last asked**: 2026-08-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bq, ambiguity, fit, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/motivation-ambiguity-collaboration-screen
- **related discussions**: 3

### Statement

Prepare concise answers for why you are changing roles, difficult project communication, handling ambiguity, personal weakness, acceptable work pace, and cross-time-zone constraints. These questions appear inside technical phone screens rather than only in the final HM round.

---

## 65. SRE Linux / Networking Troubleshooting Round

- **category**: other
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-coding · final-round
- **access**: Members
- **roles**: Infra Eng, SWE
- **tags**: sre, networking, operating-systems, troubleshooting, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/sre-linux-networking-troubleshoot
- **related discussions**: 10

### Statement

Verbal round for SRE and network-focused SDE candidates drilling Linux internals, networking, and structured production triage. Recurring topics include disk-full RCA, slow-backend triage, browser-to-backend connectivity, HTTP / DNS / TCP fundamentals, and packet sizing.

---

## 66. MLE Oral 八股: Dropout, Overfitting, Normalization, Loss Functions

- **category**: other
- **duration**: 30 min
- **frequency**: High
- **last asked**: 2026-08-06
- **stage**: phone-screen · onsite-coding · onsite-deep-dive
- **access**: Members
- **roles**: MLE, RS
- **tags**: ml-knowledge, transformer, attention, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/ml-knowledge-oral-cluster
- **related discussions**: 8

### Statement

Oral ML-fundamentals quiz that runs at the front of nearly every MLE / Monetization / Commerce phone screen. Topics rotate but the deep-dive targets are stable: dropout mechanics, overfitting mitigation, normalization choices, and standard loss functions.

---

## 67. CAP / DB Indexing / Concurrency / Logging Oral 八股

- **category**: other
- **frequency**: Medium
- **last asked**: 2026-09-06
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: SWE
- **tags**: database, distributed-systems, concurrency, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/backend-fundamentals-oral-cluster
- **related discussions**: 5

### Statement

Backend oral cluster covering schema and index choices, CAP trade-offs, cache failure modes, messaging, web transport, SQL-versus-document storage, concurrency, and production logging.

---

## 68. LLM Safety Project Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-09-10
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: MLE
- **tags**: llm, ai-safety, deep-dive, ownership
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/llm-safety-project-deep-dive
- **related discussions**: 1

### Statement

A senior USDS JV MLE technical discussion probes LLM safety post-training ownership, escalation, design choices, and multimodal safety trade-offs.

---

## 69. AI Agent Intern Technical Questions

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-09-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: agents, evaluation, troubleshooting, operating-systems, networking
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/ai-agent-intern-oral-fundamentals
- **related discussions**: 1

### Statement

A Chinese-language AI Agent internship screen combines virtual-memory and TCP questions with agent-quality evaluation and tool-call debugging.

---

## 70. RAG / Agent / Kafka Oral Drill

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: rag, llm-agent, tool-use, kafka, redis, concurrency, networking, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bytedance/rag-agent-kafka-oral-drill
- **related discussions**: 1

### Statement

A verbal fundamentals screen covering RAG, LangGraph / LangChain agent design, tool use, MCP, Kafka consumer groups, Redis / MySQL persistence, concurrency, and long-context memory handling.

---
