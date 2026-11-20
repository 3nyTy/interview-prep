# Amazon — 1point3acres Question Bank (107 curated questions)

Source: 1point3acres qbank (`company=amazon`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/amazon>

The site lists 1036 entries for this company: **107 curated questions** (below), 929 OJ links, 0 external links. Only the curated 107 are captured here.

> Amazon's bar leans on Leadership Principles and scenario thinking more than raw algorithm difficulty. Coding rounds skew toward OOD and under-specified prompts that reward clarification skill, and a GenAI-usage behavioral question appears in nearly every modern loop. Bar Raiser independence and writing-heavy work samples shape the senior-level signal.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. AI Assistant Repo Bug-Fix (OA Part 2)
2. Service Shutdown / Topological Sort
3. Drone Delivery on a Hub Ring — Minimum Travel
4. Max Money from K Consecutive Bags
5. Delivery-Center Grid — Minimum Inconvenience
6. Inventory Allocation by Bid Priority
7. LRU Cache (LC 146)
8. Word Search II
9. Longest Substring Without Repeating Characters
10. Timestamped Key-Value Store
11. Binary Array Rearrangement
12. Manual Queue Implementation
13. Maximum Frequency Stack
14. NumPy Mean and Variance with MLE Follow-Up
15. Spreadsheet Cell Relationships
16. Print Hierarchical Comments
17. Numbered Buttons with Data Popovers
18. Reorganize a String with No Equal Neighbors
19. Package Tracking & Status Aggregator Engine
20. Minimum-Point Redemption Across Programs
21. Extensible Cinema Ticket Pricing
22. Count Repeat Customers Across Dates
23. Create Binary Tree from Descriptions (LC 2196)
24. Asteroid Collision (LC 735)
25. Group Anagrams (LC 49)
26. Bootstrap Mean & Confidence Interval
27. Fulfillment-Center Inventory Transfer
28. Employee Total-Cost Object Model
29. Word Segmentation with a Dictionary Helper
30. Handwrite Sigmoid, Softmax, and Attention
31. Edit Distance (LC 72) with a Follow-Up Variant
32. Best Time to Buy & Sell Stock — Merged Variants
33. Max-Sum Window over a Compressed Array
34. Concatenated Words (LC 472) — Extended Variant
35. Maximum Concurrent Processes from Intervals
36. Closest Version Date
37. Basic Calculator with Postfix Notation
38. Find Conflicting Events
39. Org-Chart Salary Aggregation
40. Domain Score Accumulation (Reverse Trie)
41. Minimum Bills and Coins for an Amount
42. Caesar Cipher with Case Preservation
43. Detect and Break a Linked-List Cycle
44. Event Filter & Queue Routing Service
45. Message Latency from Send/Receive CSV Logs
46. Find Anagrams in a Character Stream
47. Circular Array — Multi-Query Distance Accumulation
48. Longest Consecutive Sequence — Adjacent Gap ≤ K
49. Tree Node Relationship — Sibling / Cousin / Other
50. Bucket Batching DP (Applied Scientist Phone Screen)
51. Lexicographically Largest String via State Flips
52. Min Errors in 0/1/! Subsequence DP
53. Get Nested Object by Path
54. Clone Linked List with Random + Next Pointer
55. Minimum Range-Add Ops to Non-Decreasing Array
56. Frequency-Priority Error Code Sort
57. Lexicographically Minimal BoxIds via +1/9 Op
58. Recursive Schema Validation
59. In-Flight Movie Pair (Two Sum Variant)
60. Line Intersection Points
61. Music Player with Frequency Priority
62. Smallest Time-Diff Pair Per User (Orders × Ads)
63. Handwritten InfoNCE in PyTorch
64. Abusive Books in Reading Event Stream
65. Merge Two Sorted Arrays In-Place
66. VM Inventory Rental — Heap Simulation
67. Equal-Frequency Block Prefix Count
68. Kth Smallest Element in a BST (LC 230)
69. Branch Merge Minimum Conflicts
70. Find Median from Data Stream (LC 295)
71. Top K Frequent Elements (LC 347) with a Follow-Up Variant
72. Catalog Top-K / Kth-Largest Selection
73. Product Quality Values into Contiguous Blocks
74. Package Weight Rearrangement
75. Out-of-Order Package Receiver
76. Package Dependency Installation & Build Order
77. Minimum Redistribution Cost on a Ring
78. Count Connected Components (Union-Find)
79. Earliest One-Time Visiting User
80. Minimum Replacements to Make Array Contiguous
81. Log Aggregation and Group-By
82. Morse Code Encoder / Decoder with Word-Break
83. Pizza / Restaurant System (OOD)
84. Restaurant Waitlist & Table Assignment
85. Core Dump System Design
86. Perishable-Goods Tracking & Location System
87. Playlist / Guide Voting System (OOD)
88. Ride-Hailing System Design (Uber)
89. ML System Design — Search / Ranking / Experimentation
90. Minesweeper Game Design
91. Credit Card System (OOD)
92. Distributed Training Data Pipeline (FAR)
93. Permission Control System Design
94. Rate Limiter (OOD)
95. Pub-Sub Messaging System (OOD)
96. BQ — Leadership Principles Standard Set
97. BQ — GenAI Usage (How / Failure / Tradeoff)
98. Work Simulation & Work Style Assessment (OA)
99. ML Breadth Orals — Linear / Logistic / Random Forest / Optimizers
100. Transformer / Attention Deep-Dive
101. MLP Compute-vs-Memory Bound Analysis
102. Applied Science Manager Phone Screen — Project Deep-Dive + Leadership BQ
103. Robot Human-Avoidance — Open-Ended Design
104. Paper Read Round (FAR / Premium Loops)
105. Science Application (Ambiguous Problem)
106. LoRA and PEFT Variants
107. RLHF: PPO vs GRPO vs GSPO

---

## 1. AI Assistant Repo Bug-Fix (OA Part 2)

- **category**: coding
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: ai-tools, debugging, refactoring, code-reading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/ai-assistant-repo-bug-fix
- **related discussions**: 34

### Statement

A 60-minute OA round where you are given a buggy full-stack repository and an in-browser AI assistant. You must reproduce the issues, locate the failing modules, prompt the AI for targeted fixes, and pass the test suite. Engineering loop is the scoring axis, not algorithmic insight.

---

## 2. Service Shutdown / Topological Sort

- **category**: coding
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-16
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: graph, bfs, topological-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/service-shutdown-topological-sort
- **related discussions**: 9

### Statement

Amazon has many services with dependencies; some are shut down. Return the full set of services that become unavailable, including transitive consumers. Equivalent to Course Schedule II / Kahn topological propagation.

---

## 3. Drone Delivery on a Hub Ring — Minimum Travel

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, prefix-sum, simulation, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/drone-hub-ring-min-travel
- **related discussions**: 7

### Statement

OA problem. `m` hubs sit on a ring (1-indexed); a drone starts at Hub 1 and must visit `requestedHubs` in order, each leg taking the shorter of the clockwise / counter-clockwise path measured by per-edge transition times. Return the minimum total delivery time. The classic traps are the tail-to-head wrap-around and the `m == 2` ring edge case.

---

## 4. Max Money from K Consecutive Bags

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-09-02
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: sliding-window, prefix-sum, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/max-money-k-consecutive-bags
- **related discussions**: 4

### Statement

OA algorithm problem: bags are laid on a line by range segments, each segment filling its positions with a fixed amount. Take any window of k consecutive positions and maximize the total money.

---

## 5. Delivery-Center Grid — Minimum Inconvenience

- **category**: coding
- **frequency**: Medium · New
- **last asked**: 2026-08-30
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, binary-search, shortest-path, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/delivery-center-grid-minimum-inconvenience
- **related discussions**: 4

### Statement

Given an `n × m` binary grid of delivery centers, minimize the maximum Chebyshev distance from any empty cell to its nearest center after converting at most one `0` to `1`. Constraints are `n, m ≤ 500`.

---

## 6. Inventory Allocation by Bid Priority

- **category**: coding
- **frequency**: Medium
- **last asked**: 2026-08-21
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: greedy, sorting, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/inventory-allocation-bid-priority
- **related discussions**: 4

### Statement

OA coding problem. Each request is `[customerId, quantity, bidAmount, timestamp]`; allocate a fixed `totalInventory` so higher bidAmount wins, ties break by round-robin on increasing timestamp, each customer takes at most one item per round until their quantity is met or stock runs out, and lower bids are only served after all higher bids. Return the IDs of customers who receive nothing.

---

## 7. LRU Cache (LC 146)

- **category**: coding
- **duration**: 30 min
- **frequency**: Medium
- **last asked**: 2026-08-17
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/lru-cache
- **related discussions**: 6

### Statement

Implement an LRU cache with O(1) get/put. A perennial Amazon onsite/phone-screen coding round; interviewers usually want to see a doubly-linked list plus hashmap rather than a deque trick.

---

## 8. Word Search II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, grid
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/word-search-ii
- **related discussions**: 1

### Statement

Find the dictionary words that can be formed through adjacent cells in a character board, without reusing a cell within a word.

---

## 9. Longest Substring Without Repeating Characters

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/longest-substring-without-repeating-characters
- **related discussions**: 1

### Statement

Given a string, return the length of its longest substring containing no repeated characters.

---

## 10. Timestamped Key-Value Store

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, data-structure
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/timestamped-key-value-store
- **related discussions**: 1

### Statement

Implement a store whose writes receive a timestamp, key, and value, and whose reads return the value at the largest timestamp for a key.

---

## 11. Binary Array Rearrangement

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-02
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/binary-array-minimum-adjacent-swaps
- **related discussions**: 1

### Statement

Return the fewest adjacent swaps needed to group all zeros at one end and all ones at the other; either ordering is allowed.

---

## 12. Manual Queue Implementation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: queue, linked-list, oop-design
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/manual-queue-implementation
- **related discussions**: 1

### Statement

Implement constant-time enqueue and dequeue without native data structures, then extend the queue with deletion operations.

---

## 13. Maximum Frequency Stack

- **category**: coding
- **duration**: 15 min
- **frequency**: Single report
- **last asked**: 2026-09-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: stack, data-structure
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/maximum-frequency-stack
- **related discussions**: 1

### Statement

Implement the unchanged Maximum Frequency Stack problem: pop the most frequent value, breaking ties by the most recent push.

---

## 14. NumPy Mean and Variance with MLE Follow-Up

- **category**: coding
- **duration**: 15 min
- **frequency**: Single report
- **last asked**: 2026-09-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: RS, MLE
- **tags**: numpy, probability
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/numpy-mean-variance-mle
- **related discussions**: 1

### Statement

Write NumPy computations for mean and variance, then discuss maximum-likelihood estimation.

---

## 15. Spreadsheet Cell Relationships

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-08-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/spreadsheet-cell-relationships
- **related discussions**: 1

### Statement

Design spreadsheet cells with value/formula relationships. The candidate explicitly identified the prompt family with LC 631 (Design Excel Sum Formula) and LC 3484 (Design Spreadsheet).

---

## 16. Print Hierarchical Comments

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-08-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, hashmap, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/hierarchical-comments-print
- **related discussions**: 1

### Statement

Given a flat array of comment maps containing comment ID, parent-comment ID, and text, print every comment in hierarchical order. Nesting may be arbitrarily deep.

---

## 17. Numbered Buttons with Data Popovers

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: frontend, javascript, dom, api-integration, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/frontend-numbered-popovers
- **related discussions**: 1

### Statement

Recreate a UI with three numbered buttons. Clicking a button opens its matching floating panel, fetches and renders the associated data, supports closing the panel, and matches the supplied image's layout and styling.

---

## 18. Reorganize a String with No Equal Neighbors

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, heap, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/reorganize-string-adjacent-different
- **related discussions**: 1

### Statement

Given a string containing repeated characters, rearrange all characters so no two adjacent characters are equal. Return any valid arrangement, or an empty string / failure marker when impossible.

---

## 19. Package Tracking & Status Aggregator Engine

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-08-21
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: backend, json, streaming, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/package-tracking-status-aggregator
- **related discussions**: 1

### Statement

Build a Package Tracking & Status Aggregator Engine from a shipment-rule JSON file and real-time carrier-scanner JSONL events. Compute each package's current location and final ON_TIME, LATE, or PENDING status, then emit a ledger sorted by tracking ID.

---

## 20. Minimum-Point Redemption Across Programs

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: greedy, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/minimum-points-redemption
- **related discussions**: 1

### Statement

Given reward programs with a name, available point balance, and dollar value per point, cover a fixed dollar target while consuming the fewest total points. Return failure if the available balances cannot cover the target.

---

## 21. Extensible Cinema Ticket Pricing

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/cinema-ticket-pricing-ood
- **related discussions**: 1

### Statement

Design and implement an extensible cinema price calculator. Price combines a movie-format base price, optional add-on prices, a showtime multiplier, and a seat-tier multiplier.

---

## 22. Count Repeat Customers Across Dates

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-08-19
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, streaming, dedup, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/repeat-customers-across-dates
- **related discussions**: 1

### Statement

Given customer store-entry time records, count customers who appear on at least two different dates. Follow-ups ask for counts within a unified time window and for a design that handles very large logs.

---

## 23. Create Binary Tree from Descriptions (LC 2196)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, hashmap, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/create-binary-tree-from-descriptions-lc-2196
- **related discussions**: 1

### Statement

Build a binary tree from parent-child descriptions and return its root. Follow-ups ask why a hashmap is useful, how it changes complexity, and whether the construction still works when descriptions arrive out of order.

---

## 24. Asteroid Collision (LC 735)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, simulation, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/asteroid-collision-lc-735
- **related discussions**: 1

### Statement

Given signed asteroid sizes, simulate collisions: positive values move right, negative values move left, the smaller absolute size explodes, and equal sizes both disappear. Return the survivors.

---

## 25. Group Anagrams (LC 49)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, bucket-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/group-anagrams-lc-49
- **related discussions**: 1

### Statement

Group strings that are anagrams. Start with a hashmap keyed by a canonical form, then replace per-string sorting with a fixed character-frequency signature in the optimization follow-up.

---

## 26. Bootstrap Mean & Confidence Interval

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-14
- **stage**: phone-screen
- **access**: Members
- **roles**: RS, MLE
- **tags**: numpy, sampling, probability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/bootstrap-mean-confidence-interval
- **related discussions**: 1

### Statement

Implement bootstrap resampling for a sample mean and confidence interval, discuss which statistics can or cannot be estimated reliably, then remove the explicit loop with NumPy vectorization.

---

## 27. Fulfillment-Center Inventory Transfer

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, shortest-path, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/fulfillment-center-inventory-transfer
- **related discussions**: 1

### Statement

Given bidirectional routes between fulfillment centers, a destination, a maximum hop count, and per-center inventory, return every non-destination center with positive inventory whose shortest path to the destination uses at most the allowed number of routes.

---

## 28. Employee Total-Cost Object Model

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/employee-total-cost-object-model
- **related discussions**: 1

### Statement

Define the employee and lookup-table abstractions needed to calculate total employer cost from base salary, percentage tax, level-and-title benefits, and a location adjustment, then implement the calculation.

---

## 29. Word Segmentation with a Dictionary Helper

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, backtracking, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/word-segmentation-dictionary-helper
- **related discussions**: 1

### Statement

Split a lowercase string with no spaces into valid dictionary words using a provided `isWord` helper. Return any one valid segmentation; the input is guaranteed to have at least one solution.

---

## 30. Handwrite Sigmoid, Softmax, and Attention

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-20
- **stage**: phone-screen
- **access**: Members
- **roles**: RS
- **tags**: ml-knowledge, attention, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/handwrite-sigmoid-softmax-attention
- **related discussions**: 1

### Statement

During an ML breadth-and-depth phone screen, implement sigmoid, softmax, and attention from scratch as the conversation reaches each concept. The implementation tasks are embedded in project probing rather than presented as a conventional LeetCode round.

---

## 31. Edit Distance (LC 72) with a Follow-Up Variant

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/edit-distance-one-reverse
- **related discussions**: 1

### Statement

Classic Edit Distance (LC 72): minimum insert/delete/replace operations to turn word1 into word2, coded in full with the standard dp[i][j] formulation. Follow-up (discussion only): one whole-string reverse is additionally allowed — explain how the formulation changes. Asked in the coding half of a four-round NG loop.

---

## 32. Best Time to Buy & Sell Stock — Merged Variants

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/buy-sell-stock-merged-variants
- **related discussions**: 1

### Statement

Whiteboard progression through the stock-profit family: LC 121 (single transaction) and LC 122 (unlimited transactions) coded separately, then a follow-up merges both into one function, finishing with unlimited transactions plus a per-transaction fee (LC 714 family) with an optimization ask. Seen in an in-person L4 loop.

---

## 33. Max-Sum Window over a Compressed Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-08
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, sliding-window, prefix-sum, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/max-sum-window-run-length-segments
- **related discussions**: 1

### Statement

OA Q1 at the hard tier: a sparse array is given as run-length segments ([1,3,4] means indices 1-3 hold value 4; uncovered indices are 0) and the task is the maximum sum over any window of length k. Hidden tests enforce memory — expanding the array (or prefix sums over the expansion) fails; the window must be computed over the segments directly.

---

## 34. Concatenated Words (LC 472) — Extended Variant

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, trie, backtracking, ambiguous-spec, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/concatenated-words-output-combos
- **related discussions**: 1

### Statement

Phone-screen hard in the LC 472 family: given a word list, find the words that are concatenations of other list words — but output the actual combinations rather than a boolean. The prompt is left vague on reuse rules; the interviewer may narrow scope to exactly-two-word concatenations and steers toward a Trie over the backtracking + hashset approach.

---

## 35. Maximum Concurrent Processes from Intervals

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-02
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, interval-aggregation, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/max-concurrent-processes-sweep-line
- **related discussions**: 1

### Statement

Given process running intervals such as `[1,3]`, `[2,5]`, `[3,6]`, return the maximum number of processes running at the same time. Sweep-line endpoint ordering is the main edge case.

---

## 36. Closest Version Date

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-02
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, binary-search, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/closest-version-date
- **related discussions**: 1

### Statement

Given a target version date and a list of released version dates, return the version whose date is closest to the target. The example target `2026-04-01` with versions `2023-04-01`, `2025-04-01`, and `2026-05-03` returns `2026-05-03`.

---

## 37. Basic Calculator with Postfix Notation

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-02
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/postfix-basic-calculator
- **related discussions**: 1

### Statement

Implement a basic calculator for postfix notation. Details on operators, integer division, unary negatives, and invalid-token handling are thin, so clarify the grammar before coding.

---

## 38. Find Conflicting Events

- **category**: coding
- **duration**: 25 min
- **frequency**: Single report
- **last asked**: 2026-06-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, sorting, sliding-window, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/find-conflicting-events
- **related discussions**: 1

### Statement

SDE II onsite coding. Given a list of events, each with a user name, region name, and timestamp, find conflicting events — pairs that satisfy all three: same user, different region, and timestamp difference below a threshold K. Group by user and scan time-ordered events within a window; the K-threshold and the different-region condition are the discriminating constraints.

---

## 39. Org-Chart Salary Aggregation

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-06-27
- **stage**: onsite-coding · final-round
- **access**: Members
- **roles**: MLE
- **tags**: hashmap, tree, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/org-chart-salary-aggregation
- **related discussions**: 1

### Statement

Final-round coding (MLE/AS loop). Part 1: from employee–manager–salary records, return the manager whose direct reports have the highest average salary; a hashmap grouping by manager suffices. Part 2: for a given manager, compute the average salary across the reporting hierarchy — the interviewer hints at a tree structure (recurse over the org chart) rather than a flat group-by. Exact aggregation semantics (direct reports vs full subtree) were left thin and worth clarifying.

---

## 40. Domain Score Accumulation (Reverse Trie)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: trie, dfs, backtracking, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/domain-score-trie
- **related discussions**: 1

### Statement

Coding round. Each domain (at any level) carries a score; the score of a leaf hostname is the sum of scores along its suffix hierarchy (e.g. `mail.domain.com = score(mail.domain.com) + score(domain.com) + score(com)`). Build a reverse trie keyed on the dotted labels read right-to-left and DFS / backtrack to accumulate each leaf's full hostname and summed score. Interviewers may insist the score live in a separate map rather than inside the trie node.

---

## 41. Minimum Bills and Coins for an Amount

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: greedy, math, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/min-bills-coins-change
- **related discussions**: 1

### Statement

Onsite coding. Given a monetary amount, return the minimum count of bills and coins using denominations 20/10/5/1 and 0.25/0.10/0.05/0.01 (e.g. 6.35 → one 5, one 1, one 0.25, one 0.10). Follow-up 1: add an early-exit `break` when the remaining amount hits 0, and answer where execution jumps afterward (first statement after the loop). Follow-up 2: respect a finite per-denomination drawer inventory and return 'cannot make change' when an exact amount is impossible.

---

## 42. Caesar Cipher with Case Preservation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/caesar-cipher
- **related discussions**: 1

### Statement

Onsite coding. Implement a Caesar cipher: shift each letter by a fixed amount, wrapping within A-Z and a-z separately and preserving case; leave non-letter characters (digits, symbols) unchanged. Follow-up: for very long strings (thousands of chars) the per-character `% 26` is repeated work and any given letter always maps to the same output, so precompute a 26-entry (or 52-entry) translation table once and index into it.

---

## 43. Detect and Break a Linked-List Cycle

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, cycle-detection, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/linked-list-detect-break-cycle
- **related discussions**: 1

### Statement

Onsite coding. Determine whether a singly linked list contains a cycle; if it does, break it by setting the offending `next` (the last node in the cycle, whose `next` points back to the cycle entry) to NULL. Floyd's tortoise-and-hare, then reset one pointer to head and advance both one step to find the entry. Expect heavy whiteboard tracing and 'prove why this works' / 'why reset to head' follow-ups.

---

## 44. Event Filter & Queue Routing Service

- **category**: coding
- **duration**: 25 min
- **frequency**: Single report
- **last asked**: 2026-06-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, json, parsing, backend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/event-filter-routing-service
- **related discussions**: 1

### Statement

AWS phone-screen implementation round: an events pipeline emits JSON events; build a service that holds filter expressions and routes each event to the matching output queue. Only a JSON parser helper is provided.

---

## 45. Message Latency from Send/Receive CSV Logs

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: csv, hashmap, parsing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/message-latency-from-csv-logs
- **related discussions**: 1

### Statement

Given send and receive CSV logs keyed by message type and id, print the latency (received minus sent timestamp) for every message that appears in both.

---

## 46. Find Anagrams in a Character Stream

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, string, hashmap, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/anagrams-in-string-stream
- **related discussions**: 1

### Statement

Over a stream of characters, report in real time every occurrence of an anagram of a fixed target word using a fixed-size sliding window and a frequency array.

---

## 47. Circular Array — Multi-Query Distance Accumulation

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-05-15
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, prefix-sum, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/circular-array-prefix-sum-multi-query
- **related discussions**: 1

### Statement

Bus-stops-on-a-ring variant of LC 1184: prefix-sum the ring once, then answer a stream of (a, b) queries by accumulating the shorter of the two arc distances.

---

## 48. Longest Consecutive Sequence — Adjacent Gap ≤ K

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, sorting, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/longest-consecutive-subsequence-bounded-gap
- **related discussions**: 1

### Statement

LC 128 variant: instead of demanding successive integers, two values are considered consecutive when their absolute difference is at most `k`.

---

## 49. Tree Node Relationship — Sibling / Cousin / Other

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, bfs, tree-traversal, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/tree-node-relationship-sibling-cousin
- **related discussions**: 1

### Statement

Given an arbitrary tree and two of its nodes, classify their relationship as `sibling`, `cousin`, or `other`.

---

## 50. Bucket Batching DP (Applied Scientist Phone Screen)

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-04-28
- **stage**: phone-screen
- **access**: Members
- **roles**: RS, MLE
- **tags**: dp, math-reasoning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/bucket-batching-dp-applied-scientist
- **related discussions**: 1

### Statement

K documents of varying lengths must be split into G GPU batches to minimize total padding (each batch pads to the longest document in it). A high-discrimination phone-screen DP — interviewers describe it as a low pass-rate question.

---

## 51. Lexicographically Largest String via State Flips

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-04-19
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy, heap, simulation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/lex-largest-string-via-state-flips
- **related discussions**: 1

### Statement

Build a length-m string by repeatedly picking the largest currently-available element. After each pick, every `0` directly to the right of a `1` in the state mask flips to `1`, unlocking more elements.

---

## 52. Min Errors in 0/1/! Subsequence DP

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-04-16
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/errors-in-zero-one-bang-subsequence-dp
- **related discussions**: 1

### Statement

A database string contains `0`, `1`, and unknown `!`. Each subsequence `01` costs `x`, each `10` costs `y`. Each `!` resolves to either `0` or `1`. Minimize the total cost.

---

## 53. Get Nested Object by Path

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-04-05
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/get-nested-object-by-path
- **related discussions**: 1

### Statement

Implement `get(object, path)` for a nested structure built from maps and arrays, with a path string that mixes dotted keys and bracketed indices like `ab[1].c.d[2][13]`.

---

## 54. Clone Linked List with Random + Next Pointer

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-03-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/clone-linked-list-random-and-next-pointer
- **related discussions**: 1

### Statement

Variant of LC 138 (Copy List with Random Pointer) where each node has both a `next` and a `random` pointer. Deep-clone in O(n) time.

---

## 55. Minimum Range-Add Ops to Non-Decreasing Array

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/min-ops-non-decreasing-via-range-add
- **related discussions**: 1

### Statement

Given an integer array, pick any closed interval and add a positive integer x to every element. Minimize total x summed across operations so the final array is non-decreasing.

---

## 56. Frequency-Priority Error Code Sort

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/frequency-priority-error-code-sort
- **related discussions**: 1

### Statement

Sort an integer array of error codes by ascending frequency (rare codes first); break ties by smaller numeric value. Output is the original list re-ordered (duplicates preserved).

---

## 57. Lexicographically Minimal BoxIds via +1/9 Op

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-03-22
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, greedy, simulation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/lex-min-box-ids-plus-one-mod-nine
- **related discussions**: 1

### Statement

Given a digit string `boxIds`, repeatedly remove a digit and re-insert `min(digit + 1, 9)` anywhere in the string. Return the lex-minimal achievable string.

---

## 58. Recursive Schema Validation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: RE, MLE
- **tags**: recursion, oop-design, data-structure, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/schema-validation-recursive
- **related discussions**: 1

### Statement

Given a JSON-like data structure and a schema definition, verify that the data conforms (types, required fields, nested shapes). An FAR onsite coding round emphasizing OOP design and edge-case coverage.

---

## 59. In-Flight Movie Pair (Two Sum Variant)

- **category**: coding
- **duration**: 25 min
- **frequency**: Single report
- **last asked**: 2026-03-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/in-flight-movie-pair-two-sum
- **related discussions**: 1

### Statement

Recommend two movies whose runtimes sum exactly to the flight duration (12 hours). Standard two-sum with a hashmap.

---

## 60. Line Intersection Points

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-03-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/line-intersection-points
- **related discussions**: 1

### Statement

Given a list of segments described by their two endpoints, return every pairwise intersection point. A computational-geometry problem that doubles as an OOD round — define point/segment classes and discuss precision.

---

## 61. Music Player with Frequency Priority

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, heap, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/music-player-frequency-priority
- **related discussions**: 1

### Statement

Design a streaming music player that ingests batches of `{user, songs[]}`, always picks the most-frequent unplayed song next, and resets the played set once every song has been played. A LC 347-flavored variant fused with a play-history dedupe.

---

## 62. Smallest Time-Diff Pair Per User (Orders × Ads)

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-02-12
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/smallest-time-diff-orders-ads
- **related discussions**: 1

### Statement

Given two timestamp-sorted event streams per user (orders and ad events), return the minimum absolute time difference for each user. A re-skin of LC interview 16.06 (Smallest Difference).

---

## 63. Handwritten InfoNCE in PyTorch

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-02-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: RS, MLE
- **tags**: pytorch, transformer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/infonce-handwritten-pytorch
- **related discussions**: 1

### Statement

Hand-implement the InfoNCE contrastive loss in PyTorch given a batch of query and key embeddings. An Amazon Applied Scientist intern phone-screen round, run alongside Transformer / Adam / RoPE concept questions.

---

## 64. Abusive Books in Reading Event Stream

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-10
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: streaming, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/audible-abusive-books-stream
- **related discussions**: 1

### Statement

Stream of `(user, book, position_sec, duration_sec)` reading events. Detect books where more than 20% of users reach the last 5% of the audio without ever passing 10% of the duration. An Audible (Amazon) phone-screen prompt.

---

## 65. Merge Two Sorted Arrays In-Place

- **category**: coding
- **duration**: 25 min
- **frequency**: Single report
- **last asked**: 2026-02-08
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/merge-sorted-arrays-in-place
- **related discussions**: 1

### Statement

Classic two-pointer merge of two ascending arrays into the first one, with no auxiliary array. Follow-ups generalize the direction of the input and what happens when the inputs are unsorted.

---

## 66. VM Inventory Rental — Heap Simulation

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-01-31
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: heap, simulation, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/vm-inventory-rental-heap
- **related discussions**: 1

### Statement

n VM types with initial inventory. m customers each rent from the type with the largest current stock; revenue per rental equals max-stock + min-stock. Decrement stock and repeat. Return total revenue.

---

## 67. Equal-Frequency Block Prefix Count

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-01-31
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/equal-block-prefix-via-prefix-hash
- **related discussions**: 1

### Statement

For every prefix of a string, find the maximum number of equal-length blocks the prefix can be split into such that each character has the same frequency in every block.

---

## 68. Kth Smallest Element in a BST (LC 230)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **tags**: tree
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/kth-smallest-element-in-a-bst
- **related discussions**: 1

### Statement

Return the kth smallest node value in a binary search tree, with k counted from one.

---

## 69. Branch Merge Minimum Conflicts

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-09-01
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, merge
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/branch-merge-minimum-conflicts
- **related discussions**: 2

### Statement

Merge two commit-priority strings while preserving each branch’s order, minimizing the number of lower-priority commits placed before higher-priority commits.

---

## 70. Find Median from Data Stream (LC 295)

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-09-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, streaming, heap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/find-median-data-stream-lc-295
- **related discussions**: 2

### Statement

The DSA round used LeetCode 295, Find Median from Data Stream: support inserting values from an ongoing stream and querying the current median.

---

## 71. Top K Frequent Elements (LC 347) with a Follow-Up Variant

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-09-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, bucket-sort, top-k, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/top-k-frequent-elements-streaming-follow-up
- **related discussions**: 2

### Statement

Return the `k` most frequent values from an array, implementing the bucket-based linear-time version after comparing sorting and heap alternatives. Follow-up: support a live stream whose current top `k` must be available at any time.

---

## 72. Catalog Top-K / Kth-Largest Selection

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-08-27
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: heap, top-k, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/catalog-top-k-selection
- **related discussions**: 2

### Statement

Solve a catalog Top-K / kth-largest task. Confirmed variants map directly to LC 215, LC 703, and LC 347, with follow-ups on heap-based improvement, streaming updates, and memory optimization.

---

## 73. Product Quality Values into Contiguous Blocks

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-08-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, interval, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/product-quality-contiguous-blocks
- **related discussions**: 3

### Statement

Given an integer array `quality`, repeatedly choose values `x` and `y` and replace every occurrence of `x` with `y`, paying the number of replaced elements. Minimize the total cost until every surviving quality value occupies one contiguous block.

---

## 74. Package Weight Rearrangement

- **category**: coding
- **duration**: 30 min
- **frequency**: Low · New
- **last asked**: 2026-08-24
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy, bucket-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/adjacent-package-weight-rearrangement
- **related discussions**: 2

### Statement

Given package weights drawn only from 0 through 9, decide whether they can be rearranged so every adjacent pair sums to at most 9; one version phrases the boundary as strictly below 10. The interviewer pushes for linear time by exploiting the ten-value domain.

---

## 75. Out-of-Order Package Receiver

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-08-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: streaming, data-structure, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/out-of-order-package-receiver
- **related discussions**: 2

### Statement

Design a receiver for package IDs that arrive out of order. One API records an ID; a query returns the endpoint of the contiguous prefix beginning at 0 plus all missing IDs before the largest observed package.

---

## 76. Package Dependency Installation & Build Order

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-08-14
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dependency-resolution, graph, topological-sort, dfs, cycle-detection, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/package-dependency-installation-build-order
- **related discussions**: 2

### Statement

Given packages and their dependency adjacency map, either install a target only after all dependencies are installed or return the complete build order for that target. Detect cycles with DFS visiting/visited state; the traversal takes O(V + E) time and O(V) auxiliary space, or O(V + E) total space including the input graph.

---

## 77. Minimum Redistribution Cost on a Ring

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-06-09
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: prefix-sum, greedy, array, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/min-redistribution-cost-ring
- **related discussions**: 2

### Statement

OA algorithm problem: warehouses on a circle hold differing product counts. Moving a product across one edge costs 1, and all moves must go in a single fixed direction. Equalize every position at minimum cost.

---

## 78. Count Connected Components (Union-Find)

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-05-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: union-find, graph, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/count-connected-components-union-find
- **related discussions**: 2

### Statement

Given items joined by transitive 'same group' relations (products in a category, or graph edges), count the distinct groups and, in some variants, each group's size or whether an edge closes a cycle.

---

## 79. Earliest One-Time Visiting User

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-04-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, data-structure, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/earliest-one-time-visiting-user
- **related discussions**: 2

### Statement

Build a structure that ingests user logins and can return the earliest-logged user who has visited exactly once. Follow-up generalizes to the top-X earliest one-time visitors.

---

## 80. Minimum Replacements to Make Array Contiguous

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-04-12
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, graph, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/min-ops-to-make-array-contiguous
- **related discussions**: 2

### Statement

You may replace every occurrence of value `x` in the array with another value `y`. Minimum number of such bulk replacements to make every value's occurrences form a single unbroken block.

---

## 81. Log Aggregation and Group-By

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/log-aggregation-and-grouping
- **related discussions**: 3

### Statement

Given a raw log stream, parse, aggregate, and group entries to produce a sorted report (e.g., dedupe by event id, group by user, return top-K per category). Multiple Amazon onsite reports describe variants of this prompt.

---

## 82. Morse Code Encoder / Decoder with Word-Break

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, RS, RE
- **tags**: string, hashmap, recursion, dp, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/morse-code-encoder-decoder
- **related discussions**: 2

### Statement

A four-part Morse code problem combining LC 804 with LC 139/140. Encode characters, decode delimited Morse, then handle delimiter-free Morse with a dictionary, ending in backtracking to enumerate every legal word combination.

---

## 83. Pizza / Restaurant System (OOD)

- **category**: system-design
- **duration**: 50 min
- **frequency**: Medium
- **last asked**: 2026-08-28
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, system-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/pizza-restaurant-ood
- **related discussions**: 4

### Statement

Design a pizza ordering and fulfillment system end-to-end — menu, order placement, kitchen workflow, delivery handoff — paired with LP questions on a deadline-driven project.

---

## 84. Restaurant Waitlist & Table Assignment

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, concurrency, race-condition, queue, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/restaurant-waitlist-table-assignment
- **related discussions**: 1

### Statement

AWS phone-screen design-and-code prompt: build a restaurant waitlist where parties queue FIFO but a freed table seats the first party that fits its size (a freed 8-top seats the earliest party of 8 over an earlier party of 2), table inventory must be elicited from the interviewer, and seated parties get an SMS notification. Coding is followed by race-condition and thread-safety follow-ups.

---

## 85. Core Dump System Design

- **category**: system-design
- **duration**: 55 min
- **frequency**: Single report
- **last asked**: 2026-07-02
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: storage, distributed-systems, debugging, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/core-dump-system-design
- **related discussions**: 1

### Statement

Design a core-dump collection system: agents upload crash artifacts, the backend stores large binaries and searchable metadata, engineers query and download dumps, and the platform enforces retention, access control, and scale limits.

---

## 86. Perishable-Goods Tracking & Location System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: database, schema-design, scaling, storage, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/perishable-goods-tracking-system
- **related discussions**: 1

### Statement

Phone-screen system design (part of a combined ~55-min round). Design a service that tracks and locates perishable products across multiple stores: when an item expires it moves to a storage area, and the system must locate any product by ID, persist a ledger, and answer 'which store has which expired items, and where in storage they are now.' Deep-dives: REST/GET API shape, relational (RDS/Postgres/MySQL) vs NoSQL (DynamoDB) with consistency *not* being the only justification, and avoiding a few-minutes full-table expiry scan over millions of items (expiry index + bucketing/queue, cache for read-heavy load, horizontal scaling + load balancing).

---

## 87. Playlist / Guide Voting System (OOD)

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-31
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/playlist-guide-voting-ood
- **related discussions**: 1

### Statement

Object-oriented design for a system of playlists holding guides, with add, upvote/downvote, and a getPlaylist that returns guides sorted by vote then timestamp. Extensibility follow-up points at the Strategy pattern.

---

## 88. Ride-Hailing System Design (Uber)

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: location, geohash, caching, scaling, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/ride-hailing-system-design
- **related discussions**: 1

### Statement

Design an Uber-style ride-hailing service. The interviewer drives on high-frequency driver GPS writes (dedup/throttle), geospatial indexing (Quadtree vs H3), and Redis GEO caching.

---

## 89. ML System Design — Search / Ranking / Experimentation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: RS, MLE
- **tags**: system-design, ml-knowledge, retrieval, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/as-ml-system-design-search-ranking
- **related discussions**: 1

### Statement

Applied Scientist L5 full-loop ML-system-design round on search / ranking / experimentation. The case study often goes beyond a single model into evaluation and live A/B design.

---

## 90. Minesweeper Game Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, system-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/minesweeper-game-design
- **related discussions**: 1

### Statement

A candidate-chosen game design round ("design the game you enjoy") — one candidate picked Minesweeper. Doubles as OOD plus a touch of system design for multi-user / persistence.

---

## 91. Credit Card System (OOD)

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-05
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/credit-card-system-ood
- **related discussions**: 1

### Statement

Lightweight OOD design of a credit card system — card issuance, swipe / authorize transactions, repayment, statement generation, and minimal business rules.

---

## 92. Distributed Training Data Pipeline (FAR)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RE, Infra Eng
- **tags**: system-design, storage, scaling, dedup, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/distributed-training-data-pipeline
- **related discussions**: 1

### Statement

Design a distributed pipeline for LLM training-data preprocessing: ingest from S3 / streaming, tokenize, deduplicate, quality-filter, and output sharded training corpora. Amazon FAR onsite Staff-level system-design round.

---

## 93. Permission Control System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, storage, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/permission-control-system
- **related discussions**: 1

### Statement

Design a file-sharing permission control system. Users can share files with other users; the homepage lists every file a user can access; explicit access checks must be supported.

---

## 94. Rate Limiter (OOD)

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-09-10
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, system-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/rate-limiter-ood
- **related discussions**: 3

### Statement

Implement a rate limiter for inbound user requests. The interviewer keeps the prompt sparse — driving clarification on time window and limit is half the round.

---

## 95. Pub-Sub Messaging System (OOD)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, pubsub, messaging, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/pub-sub-system-ood
- **related discussions**: 2

### Statement

Object-oriented design for an in-process publish/subscribe broker — topics, subscribers, fan-out delivery, and the usual concurrency / delivery-guarantee follow-ups.

---

## 96. BQ — Leadership Principles Standard Set

- **category**: behavioral
- **duration**: 25 min
- **frequency**: Very high
- **last asked**: 2026-09-10
- **stage**: onsite-bq · phone-screen · final-round
- **access**: Members
- **roles**: All
- **tags**: behavioral, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/bq-leadership-principles-standard-set
- **related discussions**: 77

### Statement

Every Amazon round runs 20-30 minutes of LP-anchored BQ. The recurring stems across 2026 loops are tight deadline, conflict, dive deep, out-of-scope contribution, tough feedback (given and received), disagree-and-commit, calculated risk, and customer obsession.

---

## 97. BQ — GenAI Usage (How / Failure / Tradeoff)

- **category**: behavioral
- **duration**: 15 min
- **frequency**: Very high
- **last asked**: 2026-09-10
- **stage**: onsite-bq · onsite-coding · phone-screen · final-round
- **access**: Members
- **roles**: All
- **tags**: behavioral, ai-tools
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/bq-genai-usage
- **related discussions**: 42

### Statement

A GenAI-themed BQ appears in nearly every Amazon round in 2026. Standard prompts: 'tell me about a time you used GenAI', 'a GenAI failure', or 'how do you use AI day-to-day'. Some technical rounds also probe Agent / multi-turn design depth.

---

## 98. Work Simulation & Work Style Assessment (OA)

- **category**: behavioral
- **duration**: 25 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: work-style, behavioral, oa
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/work-style-simulation-assessment
- **related discussions**: 12

### Statement

The non-coding portion of the SDE OA: a situational-judgment Work Simulation (rate responses 1-5 across workplace scenarios) and a forced-choice Work Style survey (pick which of two statements is more like you).

---

## 99. ML Breadth Orals — Linear / Logistic / Random Forest / Optimizers

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-01
- **stage**: phone-screen · onsite-research
- **access**: Members
- **roles**: RS, MLE
- **tags**: ml-knowledge, math-reasoning, probability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/as-ml-breadth-orals
- **related discussions**: 6

### Statement

Applied Scientist phone-screen / breadth round that drills classical ML: linear regression assumptions, why squared loss, logistic regression with log loss, Random Forest feature selection, Adam vs SGD, and a trick question on optimization landscapes.

---

## 100. Transformer / Attention Deep-Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-14
- **stage**: phone-screen · onsite-research · onsite-deep-dive
- **access**: Members
- **roles**: RS, MLE
- **tags**: transformer, ml-knowledge, pytorch, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/transformer-attention-deep-dive
- **related discussions**: 7

### Statement

Applied Scientist / MLE rounds drill the full Transformer stack: self-attention math, encoder vs decoder masking, multi-head intuition, optimizer choice, and modern variants (Flash Attention, RoPE, KV cache).

---

## 101. MLP Compute-vs-Memory Bound Analysis

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-08-08
- **stage**: phone-screen
- **access**: Members
- **roles**: EM
- **tags**: gpu, arithmetic-intensity, performance-modeling, inference, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/mlp-compute-memory-bound-analysis
- **related discussions**: 1

### Statement

Analyze `Y = GELU(X @ W + bias) + residual` on hardware with 1 TB/s HBM bandwidth, 25 MB SRAM, 100 TFLOP/s peak BF16 throughput, and model dimension 4096. Calculate compute time, memory time, and latency for batch sizes 1 and 256, then prove which case is compute-bound and which is memory-bound.

---

## 102. Applied Science Manager Phone Screen — Project Deep-Dive + Leadership BQ

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-07
- **stage**: phone-screen
- **access**: Members
- **roles**: EM
- **tags**: deep-dive, behavioral
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/asm-phone-screen-deep-dive-bq
- **related discussions**: 1

### Statement

Hour-long Applied Science Manager (Seller org) phone screen split roughly 30/30: a resume-driven deep-dive on two projects (one technical, one management) followed by manager-flavored behavioral questions. No algorithm testing — the graded signal is whether you genuinely led the work and can defend decisions through multi-layer why/how/what-if probing.

---

## 103. Robot Human-Avoidance — Open-Ended Design

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: open-ended, ambiguous-spec, system-design, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/robot-human-avoidance-open-design
- **related discussions**: 1

### Statement

Robotics-team intern coding slot that is really an architecture / pseudocode discussion: design how a warehouse robot detects and avoids humans, picking sensors and data structures along the way.

---

## 104. Paper Read Round (FAR / Premium Loops)

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-21
- **stage**: onsite-research · onsite-deep-dive
- **access**: Members
- **roles**: RS, MLE
- **tags**: deep-dive, ml-knowledge, presentation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/paper-read-round-far
- **related discussions**: 1

### Statement

Amazon FAR (Foundational AI Research) and premium-tier loops add a paper-read round: the paper arrives 48 hours before the interview, and the slot splits ~30-35 minutes of technical discussion plus 20-25 minutes of behavioral.

---

## 105. Science Application (Ambiguous Problem)

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-29
- **stage**: onsite-research
- **access**: Members
- **roles**: RS, MLE
- **tags**: deep-dive, ml-knowledge, behavioral
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/science-application-ambiguous-problem
- **related discussions**: 2

### Statement

Applied Scientist round explicitly designed around an under-defined business problem. The interviewer states a vague problem, and the candidate must clarify goals, propose a plan, and reason through tradeoffs aloud.

---

## 106. LoRA and PEFT Variants

- **category**: other
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-03-29
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: RS, MLE
- **tags**: ml-knowledge, transformer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/lora-and-peft-variants
- **related discussions**: 3

### Statement

Concept round on LoRA and its variants — the rank parameterization, how it composes with full fine-tuning, QLoRA quantization, DoRA, AdaLoRA, and practical hyperparameter trade-offs.

---

## 107. RLHF: PPO vs GRPO vs GSPO

- **category**: other
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-03-29
- **stage**: onsite-deep-dive · onsite-research
- **access**: Members
- **roles**: RS, MLE
- **tags**: ml-knowledge, math-reasoning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/amazon/rlhf-ppo-grpo-gspo
- **related discussions**: 3

### Statement

Applied Scientist deep-dive into RLHF training: PPO objective, the GRPO/GSPO group-relative variants, and the practical differences that drive choice between them.

---
