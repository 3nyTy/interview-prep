# Uber — 1point3acres Question Bank (102 curated questions)

Source: 1point3acres qbank (`company=uber`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/uber>

The site lists 418 entries for this company: **102 curated questions** (below), 238 OJ links, 78 external links. Only the curated 102 are captured here.

> Uber's loop leans heavily on Uber Eats and rider/driver marketplace problems — Cart pricing, geo heatmaps, surge, and restaurant recommendation surface in nearly every onsite at L5+. Coding rounds are mostly tagged LeetCode patterns, but Hack2Hire OAs ship a small rotating set of original problems that rarely appear elsewhere. Coding bar is the gate; system design quality drives leveling between L5a / L5b / Senior / Staff.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. OA: Jump Game with Prime-3 Steps
2. Phone Screen: Find Robot Position from Blocker Distances
3. Phone Screen / Onsite: Meeting Room Scheduler
4. Onsite OOD: Multi-Level Parking Lot
5. Phone Screen: Longest Subarray with Bounded Diff
6. Phone Screen: First Customer Who Visited Only Once
7. Phone Screen: Rate Limiter (Coding + Trade-offs)
8. OA: Splash Zone / Balls Connection
9. OA: Maximize Pipeline Throughput
10. Phone Screen: Minimize a Convex Function
11. Onsite OOD / Coding: Uber Eats Cart & Pricing Engine
12. Phone Screen: Word Search on Straight 8-Direction Lines
13. MLE Onsite: ML Coding from Scratch (Regression / Markov / Facility)
14. Phone Screen: Number of Islands (Plain and Streaming)
15. Phone Screen: Service Dependency Topological Order
16. Onsite Coding: K Closest Points
17. Phone Screen OOD: Filesystem Navigation
18. Onsite Coding: Minesweeper (LC 529)
19. OOD Counter Class
20. Word Search
21. Spatial Quadtree: Insert and Range Query
22. Compare Two Structs / Classes for Equality
23. Serialize / Deserialize Binary Tree with N-ary Follow-up
24. Rotate Image In Place
25. Median of Two Sorted Arrays
26. Word Break
27. Sliding Window Maximum
28. Versioned Social Network with Snapshots
29. OOD: Random Bingo Card Generator
30. Leftmost Column with At Least a One
31. Last-Click Attribution Tracker
32. Fill Dashes with Nearest Letter
33. Maximum Number of Points from Grid Queries
34. Earliest Time to Connect All Riders
35. Seat Assignment / Exam Room
36. Merge Step Functions of (timestamp, value) Pairs
37. Rotting Oranges
38. Exclusive Time of Functions
39. Number of Ways to Wear Different Hats to Each Other
40. Minimum Knight Moves
41. Toy Order Completion Analysis
42. Active Couriers Timeline
43. Count Paths That Can Form a Palindrome in a Tree
44. Checking Existence of Edge Length Limited Paths
45. Customer Revenue and Referral Tracking
46. Vertical Order Traversal of a Binary Tree
47. Time Based Key-Value Store
48. Shortest Word Distance
49. Zero Array Transformation
50. Lowest Common Ancestor of a Binary Tree
51. Shortest Bridge
52. Word Search II
53. Evaluate Division
54. N-ary Tree Level Order Traversal
55. Cheapest Flights Within K Stops
56. Design Hit Counter
57. K Shuttle Pickup Locations
58. Sqrt(x)
59. Palindrome Ancestor Path Queries
60. Binary Tree Longest Consecutive Sequence II
61. Top K Frequent Elements
62. Interval List Intersections
63. My Calendar I
64. Making a Large Island
65. Number of Ways to Earn Points
66. Isomorphic Strings
67. OA: Round-Trip Mission Schedule
68. Number of Islands II (Dynamic Union-Find)
69. Phone Screen: Nested Add/Sub Expression Evaluator
70. Phone Screen: Bus Routes (LC 815)
71. Construct Quad Tree
72. OA: Minimum Edge Reversals from Every Root
73. Phone Screen: Tennis Rounds — Print Round Winners
74. VO: AI-Assisted Coding / Debug Round (HackerRank IDE + AI)
75. Kth Smallest Element in a BST
76. OA: Final Prices with Special Discount (Sum + Unsold Indices)
77. OA: Shortest Subarray with At Least K Distinct
78. OA: Minimum Operations to Reduce N to 0 (±2^i)
79. Phone Screen: Squares of a Sorted Array
80. Letter Combinations of a Phone Number
81. Best Time to Buy and Sell Stock
82. Phone Screen: Alien Dictionary (LC 269)
83. OA: Maximum Items by Budget with Prefix Sums
84. OA: Permutation Prefix Balanced (1..k Subarray Check)
85. OA: Stairs vs Elevator Energy Trade-off
86. Onsite System Design: Driver Location Heatmap
87. Onsite System Design: Top-K Popular Items (Restaurant / Search)
88. MLE Onsite: ML System Design (Recommendation / Feed Ranking / ETA)
89. Onsite System Design: Nearby Driver Proximity Service
90. Design a Stock Price Alert Notification System
91. Onsite System Design: App User Behavior Data Platform
92. Fraud Detection
93. Onsite System Design: Daily Puzzle / Submission Scoring
94. Design an Online Shopping Cart
95. Design a Pickup Area Driver Queue
96. Onsite System Design: Rider-Driver Matching
97. Design Messenger
98. Uber Eats Search
99. Onsite BQ + Hiring Manager + Project Deep-Dive
100. Bar Raiser: Architecture, Influence, and Values
101. Staff (BPS) Round: Scope & Impact + Architecture
102. Scientist Onsite: Experiment Design (Switchback + CI Interpretation)

---

## 1. OA: Jump Game with Prime-3 Steps

- **category**: coding
- **duration**: 90 min
- **frequency**: High
- **last asked**: 2026-06-01
- **stage**: oa
- **access**: Free preview
- **roles**: SWE
- **tags**: dp, array, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-jump-game-prime
- **related discussions**: 8

### Statement

One of two problems in the 90-min Hack2Hire OA. Variant of LeetCode 1696 (Jump Game VI). From index 0 of an integer array, at each step you may jump +1 or jump +k where k is a prime ending in digit 3 (3, 13, 23, 43, 53, 73, 83, …). Maximize the sum of values landed on, finishing at index n−1.

---

## 2. Phone Screen: Find Robot Position from Blocker Distances

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-robot-position-by-blocker-distance
- **related discussions**: 5

### Statement

Recurring phone-screen prompt. A 2D grid contains robots `O`, empty cells `E`, and blockers `X`. Given a query `[left, top, bottom, right]` of distances to the nearest blocker (with the grid boundary counted as a blocker), find every robot whose surroundings match the query.

---

## 3. Phone Screen / Onsite: Meeting Room Scheduler

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, interval, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-meeting-scheduler-rooms
- **related discussions**: 7

### Statement

High-frequency OOD coding round. Given a fixed list of rooms, implement `bookMeeting(start, end)` that returns an available room id (or sentinel) and records the booking. Follow-ups include removing bookings and querying the last `N` scheduled meetings.

---

## 4. Onsite OOD: Multi-Level Parking Lot

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-28
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-ood-parking-lot
- **related discussions**: 6

### Statement

High-frequency onsite OOD round. Design a parking-lot system supporting multiple vehicle types (motorcycle, car, optionally bus) and multiple slot types with size constraints. Implement `park`, `unpark`, and availability queries.

---

## 5. Phone Screen: Longest Subarray with Bounded Diff

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: two-pointer, array, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-longest-stable-subarray
- **related discussions**: 5

### Statement

Recurring phone-screen / onsite prompt, equivalent to LeetCode 1438 (Longest Continuous Subarray With Absolute Diff ≤ Limit). Two-pointer plus a monotonic deque or a multiset for min / max tracking.

---

## 6. Phone Screen: First Customer Who Visited Only Once

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-first-unique-visitor
- **related discussions**: 6

### Statement

Recurring phone-screen OOD prompt. A website receives a stream of `visit(customer_id)` events. Support two operations: `visit(id)` (record a visit) and `firstUniqueVisitor()` (return the earliest customer who has visited exactly once so far). Both should be efficient.

---

## 7. Phone Screen: Rate Limiter (Coding + Trade-offs)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-06-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, system-design, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-rate-limiter
- **related discussions**: 4

### Statement

Phone-screen / onsite coding prompt. Implement a rate limiter, with variants ranging from per-user windowed request counting to an explicitly thread-safe token bucket. Follow-ups cover custom rules, atomic token distribution, and lock contention.

---

## 8. OA: Splash Zone / Balls Connection

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-06-04
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: union-find, graph, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-splash-zone-union-find
- **related discussions**: 5

### Statement

Hack2Hire OA problem (companion to Pipeline Throughput). Balls live on a 2D grid; two balls connect if they share a row or column and their Manhattan-along-the-axis distance is ≤ `d`. Connections cascade transitively into clusters. Counting clusters yields the minimum number of triggers to vacuum the board.

---

## 9. OA: Maximize Pipeline Throughput

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-06-04
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: algorithm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-pipeline-throughput
- **related discussions**: 6

### Statement

Hack2Hire OA problem. A pipeline runs services in series, so the end-to-end throughput equals the minimum throughput across all services. Each service `i` starts at throughput `t[i]` and can be scaled `x` times to reach `t[i] * (1 + x)` at total spend `x * cost[i]`. Given a global `budget`, maximize the achievable pipeline throughput.

---

## 10. Phone Screen: Minimize a Convex Function

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-13
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: MLE, SWE
- **tags**: math-reasoning, algorithm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-convex-function-min
- **related discussions**: 5

### Statement

Recurring Uber phone-screen problem, especially for MLE / Senior MLE candidates. Given a black-box convex function `F(x)` defined on `[a, b]`, write a routine that finds the minimum by repeatedly probing `F`. Ternary search or analytic gradient-sign binary search both work.

---

## 11. Onsite OOD / Coding: Uber Eats Cart & Pricing Engine

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-12
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE, EM
- **tags**: oop-design, system-design, scaling, idempotency, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-ood-eats-cart-pricing-engine
- **related discussions**: 5

### Statement

Recurring Senior / L5+ onsite OOD round (also appears as a system-design prompt depending on team). Design the core classes and interfaces for Uber Eats's cart + pricing engine: item customizations, surge pricing, promos / BOGO, membership benefits, and a receipt breakdown.

---

## 12. Phone Screen: Word Search on Straight 8-Direction Lines

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-12
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, string, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-word-search-straight-line
- **related discussions**: 4

### Statement

Recurring phone-screen prompt. Given a 2D character grid and a target word, decide whether the word appears along any single straight line in 8 directions (up/down/left/right + 4 diagonals). No turns allowed. Follow-ups extend to turning paths and skip-letter matching.

---

## 13. MLE Onsite: ML Coding from Scratch (Regression / Markov / Facility)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-15
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: MLE, RS
- **tags**: pytorch, numpy, ml-knowledge, linear-algebra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/mle-onsite-ml-coding-from-scratch
- **related discussions**: 4

### Statement

MLE-specific onsite coding round. Implement a small ML primitive from scratch in Python / NumPy without using framework helpers. Recurring prompts: linear & logistic regression, a markov-chain text generator, and k-medoids facility-location for shuttle pickup with L1 distance.

---

## 14. Phone Screen: Number of Islands (Plain and Streaming)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-09
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: graph, bfs, dfs, union-find, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-number-of-islands
- **related discussions**: 5

### Statement

Very common phone-screen prompt, equivalent to LeetCode 200. Some loops dress it up as a virus-spread / infected-zone variant (LC 994 style) or extend it into a streaming Number of Islands II (LC 305) with union-find.

---

## 15. Phone Screen: Service Dependency Topological Order

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2025-12-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, topological-sort, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-topological-dependency-order
- **related discussions**: 4

### Statement

Phone-screen prompt, equivalent to LeetCode 207 / 210 (Course Schedule). Given a service dependency graph (initially acyclic), return a build order for a given target service. Follow-up handles cycles.

---

## 16. Onsite Coding: K Closest Points

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, heap, quickselect, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-k-closest-points
- **related discussions**: 1

### Statement

The base prompt is LC 973: return the K points closest to the origin, with K much smaller than N. The round asks for both a bounded max-heap solution and a written quickselect partition, then extends the problem to an arbitrary rider location, tens of millions of moving points, and road-network distance.

---

## 17. Phone Screen OOD: Filesystem Navigation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: filesystem, oop-design, dfs, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-ood-filesystem-navigation
- **related discussions**: 1

### Statement

Implement `mkdir(String name)`, `pwd()`, and `cd(String path)` over an in-memory directory tree. `cd` must resolve absolute and relative paths containing `.`, `..`, and a wildcard that may span multiple directory levels, changing the current directory on success and returning an error otherwise.

---

## 18. Onsite Coding: Minesweeper (LC 529)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-coding-minesweeper-lc-529
- **related discussions**: 1

### Statement

An onsite coding round used the canonical LC 529 Minesweeper update task after a simpler undisclosed warm-up. Given a board and a click, update the board according to the mine and reveal rules.

---

## 19. OOD Counter Class

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, data-structure, hashmap, queue, ttl, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/ood-counter-class
- **related discussions**: 1

### Statement

Design an expiring counter that tracks how many unexpired copies of each element are currently stored, supporting per-element and total counts under a sliding expiration window. A common follow-up asks how to scale to roughly one million inputs per second by batching counts into per-second aggregates.

---

## 20. Word Search

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, backtracking, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/word-search
- **related discussions**: 1

### Statement

Given a 2-D grid of characters and a target string, determine whether the word can be formed by a path through horizontally or vertically adjacent cells, where no cell is reused.

---

## 21. Spatial Quadtree: Insert and Range Query

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, location, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/spatial-quadtree-range-query
- **related discussions**: 1

### Statement

Design and implement a quadtree for latitude/longitude points. Support inserting a point with associated data and querying all points inside a rectangular range, then discuss overlapping points and tree balance.

---

## 22. Compare Two Structs / Classes for Equality

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: object-design, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/compare-two-structs-or-classes
- **related discussions**: 1

### Statement

Onsite coding prompt: write a simple implementation to compare two structs / classes and decide whether they are identical. The source gives only the high-level task, so clarify field traversal, nested objects, arrays, nulls, and custom equality rules before coding.

---

## 23. Serialize / Deserialize Binary Tree with N-ary Follow-up

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, parsing, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/serialize-deserialize-binary-tree-nary-followup
- **related discussions**: 1

### Statement

Senior tech phone-screen prompt. Implement serialization and deserialization for a binary tree, then extend the scheme to an N-ary tree. The interviewer asks the candidate to invent test cases and run code.

---

## 24. Rotate Image In Place

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/rotate-image-in-place
- **related discussions**: 1

### Statement

Phone-screen prompt equivalent to Rotate Image. Given an `n x n` matrix representing an image, rotate it 90 degrees clockwise in place without allocating another matrix.

---

## 25. Median of Two Sorted Arrays

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, array, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/median-of-two-sorted-arrays
- **related discussions**: 1

### Statement

Onsite problem-solving prompt equivalent to Median of Two Sorted Arrays. Given two sorted arrays of lengths `m` and `n`, return the median of the combined sorted order in `O(log(m+n))` time.

---

## 26. Word Break

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/word-break
- **related discussions**: 1

### Statement

Onsite coding prompt equivalent to Word Break. Given a string `s` and dictionary `wordDict`, determine whether `s` can be segmented into a sequence of dictionary words.

---

## 27. Sliding Window Maximum

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, queue, array, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/sliding-window-maximum
- **related discussions**: 1

### Statement

Onsite coding prompt equivalent to Sliding Window Maximum. Given `nums` and window size `k`, return the maximum value in every contiguous window of length `k`.

---

## 28. Versioned Social Network with Snapshots

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: design, snapshot, versioning, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/social-network-snapshot
- **related discussions**: 1

### Statement

Design a social network whose follow graph supports point-in-time snapshots: follow / unfollow apply to the current version, createSnapshot freezes a version, and isFollowing queries any historical snapshot. A snapshot/MVCC problem.

---

## 29. OOD: Random Bingo Card Generator

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-06
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, sampling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/bingo-card-generator
- **related discussions**: 1

### Statement

Onsite OOD coding round. Generate a random 3x9 Bingo board where each row has exactly 5 numbers and 4 blanks, each column is constrained to a decade range (1-10, 11-20, ...), and cells are filled probabilistically while keeping the row/column constraints feasible.

---

## 30. Leftmost Column with At Least a One

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, binary-search, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/leftmost-column-with-a-one
- **related discussions**: 1

### Statement

Given a binary matrix where every row is sorted in non-decreasing order, return the index of the leftmost column that contains at least one 1, or -1 if no such column exists.

---

## 31. Last-Click Attribution Tracker

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: design, hashmap, binary-search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/last-click-attribution
- **related discussions**: 1

### Statement

Phone-screen design-coding round. Build an in-memory attribution tracker: record click and conversion events, and for each conversion attribute it to the same user's most recent click inside a fixed conversion window. Implement the API and write your own tests.

---

## 32. Fill Dashes with Nearest Letter

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/matrix-fill-nearest-letter
- **related discussions**: 1

### Statement

L4 backend phone screen. In a matrix of dashes and letters, replace every '-' with an orthogonally adjacent letter at distance 1; the follow-up requires breaking ties by the alphabetically smallest letter. A Walls-and-Gates multi-source BFS.

---

## 33. Maximum Number of Points from Grid Queries

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, heap, bfs, sorting, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/max-points-grid-queries
- **related discussions**: 1

### Statement

Given an integer matrix and an array of query values, count for each query how many cells are reachable from the top-left moving in four directions through cells whose value is strictly less than the query.

---

## 34. Earliest Time to Connect All Riders

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: union-find, graph, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/earliest-time-connect-all-riders
- **related discussions**: 1

### Statement

Given timestamped ride logs where each entry links two riders, find the earliest timestamp at which every rider that appears anywhere in the logs belongs to a single connected component. Return "-1" if full connection never happens. A union-find / connected-component problem in the LeetCode 1101 family.

---

## 35. Seat Assignment / Exam Room

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: heap, greedy, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/exam-room-seat-assignment
- **related discussions**: 1

### Statement

Onsite coding round. Implement a class that seats people one at a time in a row of n seats, each time choosing the seat that maximizes the distance to the nearest occupied seat. Seats are never vacated. Confirmed expected output for n=10: 0, then 9, then 4 or 5.

---

## 36. Merge Step Functions of (timestamp, value) Pairs

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, merge, divide-and-conquer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/merge-step-functions
- **related discussions**: 1

### Statement

Given two step functions over time, each encoded as sorted [timestamp, value] checkpoints, return the checkpoints of their pointwise sum. The follow-up extends the merge to k functions via divide-and-conquer.

---

## 37. Rotting Oranges

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/rotting-oranges

### Statement

Given an m x n grid of empty cells, fresh oranges, and rotten oranges, return the minimum number of minutes for all fresh oranges to rot, where each minute rot spreads to the four-directionally adjacent fresh oranges. Return -1 if some fresh orange can never rot.

---

## 38. Exclusive Time of Functions

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: stack, simulation, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/exclusive-time-of-functions

### Statement

On a single-threaded CPU, given execution logs for n functions, compute the exclusive time spent in each function, excluding time spent in nested child calls.

---

## 39. Number of Ways to Wear Different Hats to Each Other

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, state-compression-dp, bitmask, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/number-of-ways-to-wear-hats

### Statement

Given n people and 40 hat types where each person lists the hats they are willing to wear, count the assignments in which every person gets exactly one liked hat and no two people share a hat, modulo 10^9 + 7.

---

## 40. Minimum Knight Moves

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-28
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: bfs, graph, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/minimum-knight-moves

### Statement

On an infinite chess board with a knight starting at [0, 0], return the minimum number of moves needed to reach a target square [x, y]. The answer is guaranteed to exist.

---

## 41. Toy Order Completion Analysis

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: data-analysis, pandas, python, metrics, troubleshooting, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/toy-order-completion-analysis

### Statement

A lightweight analytics exercise on a toy orders dataset: compute a simple slice metric (e.g. completion rate for a given city and order type), define the numerator/denominator precisely, handle edge cases, then reason through a short root-cause case study for why the metric might be low.

---

## 42. Active Couriers Timeline

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, interval-aggregation, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/active-couriers-timeline

### Statement

Given a set of courier working intervals [start, end) that may overlap, return the timeline broken into segments [segmentStart, segmentEnd, activeCount], creating a new segment only when the number of active couriers changes and emitting only segments where the active count is positive.

---

## 43. Count Paths That Can Form a Palindrome in a Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-22
- **stage**: oa · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, bitmask, bit-manipulation, hashmap, dfs, palindromes, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/count-palindrome-paths-tree

### Statement

Given a tree where each edge carries a lowercase character, count the unordered pairs of distinct nodes whose path edge-characters can be rearranged into a palindrome. The path is palindrome-formable when at most one character has an odd count, which maps to a 26-bit parity bitmask combined over root-to-node paths.

---

## 44. Checking Existence of Edge Length Limited Paths

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/edge-length-limited-paths

### Statement

Given an undirected weighted graph defined by edgeList, answer for each query whether a path exists between two nodes using only edges strictly shorter than a given limit. Efficient solutions sort edges and queries by weight and process them offline with union-find.

---

## 45. Customer Revenue and Referral Tracking

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, oop-design, top-k, heap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/customer-revenue-referral-tracking
- **related discussions**: 1

### Statement

Design a data structure that tracks customer revenue and referral credit. New customers can be inserted directly or via a referral that also credits the referrer, and a query returns up to K qualifying customers with the smallest total revenue at or above a threshold.

---

## 46. Vertical Order Traversal of a Binary Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, tree-traversal, bfs, sorting, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/vertical-order-traversal-bt

### Statement

Given the root of a binary tree, sort its nodes by column from left to right. Nodes in the same column are ordered top to bottom by row, with ties on the same row and column broken by node value.

---

## 47. Time Based Key-Value Store

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, binary-search, in-memory-database, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/time-based-key-value-store

### Statement

Design a time-based key-value data structure that supports storing multiple values for the same key at different timestamps, where a lookup returns the value tied to the largest timestamp at or before a queried time.

---

## 48. Shortest Word Distance

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, string, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/shortest-word-distance

### Statement

Given an array of strings and two distinct words that both appear in it, return the minimum index distance between any occurrence of the two words.

---

## 49. Zero Array Transformation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-08
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, prefix-sum, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/zero-array-transformation

### Statement

Given an integer array nums and a list of range queries [li, ri, vali], decide whether nums can be reduced entirely to zeros, where each query lets you subtract any amount between 0 and vali from every index in its range.

---

## 50. Lowest Common Ancestor of a Binary Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/lowest-common-ancestor-bt

### Statement

Given the root of a binary tree and two nodes p and q, return their lowest common ancestor.

---

## 51. Shortest Bridge

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, dfs, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/shortest-bridge

### Statement

Given a binary grid with exactly two islands (1 = land, 0 = water), return the minimum number of 0s that must be flipped to connect the two islands.

---

## 52. Word Search II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-19
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: trie, dfs, backtracking, grid, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/word-search-ii

### Statement

Given a 2-D grid of characters and a list of words, return every word that can be formed by a path of horizontally or vertically adjacent cells without reusing a cell. Efficient solutions build a trie over the words and DFS the grid against it.

---

## 53. Evaluate Division

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, union-find, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/evaluate-division

### Statement

You are given equations such as A / B = k, represented by equations[i] = [A, B] and values[i] = k. For each query [C, D], return the value of C / D if it can be derived from the given equations, or -1.0 otherwise.

---

## 54. N-ary Tree Level Order Traversal

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-12
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, tree-traversal, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/nary-tree-level-order-traversal

### Statement

Given an N-ary tree serialized in level-order form with -1 separators between sibling groups (e.g. [1,-1,3,2,4,-1,5,6]), return its level-order traversal as a list of value levels.

---

## 55. Cheapest Flights Within K Stops

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, bellman-ford, dijkstra, bfs, shortest-path, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/cheapest-flights-within-k-stops

### Statement

There are n cities connected by directed flights with prices. Given a source, destination, and a stop budget k, return the cheapest price from src to dst using at most k stops, or -1 if no such route exists.

---

## 56. Design Hit Counter

- **category**: coding
- **duration**: 5 min
- **frequency**: Single report
- **last asked**: 2026-01-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, queue, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/hit-counter-design

### Statement

Design a hit counter that counts the number of hits received in the past 5 minutes (300 seconds), with hits arriving in chronological order.

---

## 57. K Shuttle Pickup Locations

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: clustering, k-median, greedy, math, optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/k-shuttle-pickup-locations

### Statement

Given the coordinates of N people, choose K shuttle pickup locations so the total L1 distance from each person to their nearest pickup location is minimized. This is the k-median objective under Manhattan distance, where the best center of a fixed cluster is the coordinate-wise median.

---

## 58. Sqrt(x)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, math, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/sqrt-x

### Statement

Given a non-negative integer x, return the square root of x rounded down to the nearest integer, without using any built-in exponent function or operator. The result must be non-negative.

---

## 59. Palindrome Ancestor Path Queries

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-21
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, bitmask, palindromes, hashmap, prefix-sum, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/palindrome-ancestor-path-queries
- **related discussions**: 1

### Statement

Hierarchical data is stored as a tree of character-labeled nodes rooted at node 0. For each query node, count how many ancestors (including the node itself) form a path string whose characters can be rearranged into a palindrome.

---

## 60. Binary Tree Longest Consecutive Sequence II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-11
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, tree-traversal, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/bt-longest-consecutive-sequence-ii

### Statement

Given the root of a binary tree, return the length of the longest consecutive path, where adjacent node values differ by one. The path may be increasing or decreasing and can pass through a node in the child-parent-child direction.

---

## 61. Top K Frequent Elements

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-10
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: top-k, heap, hashmap, bucket-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/top-k-frequent-elements

### Statement

Given an integer array nums and an integer k, return the k most frequent elements within the array. The answer is guaranteed unique and may be returned in any order.

---

## 62. Interval List Intersections

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, two-pointer, array, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/interval-list-intersections

### Statement

Given two lists of closed intervals, each sorted by start time and pairwise disjoint within a list, return every intersection between the two lists using a two-pointer sweep. A follow-up asks how to handle input lists too large to fit in memory.

---

## 63. My Calendar I

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/my-calendar-i

### Statement

Implement a calendar that stores booked half-open intervals [start, end), accepting a new event only if it does not overlap any existing booking.

---

## 64. Making a Large Island

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-07
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: grid, dfs, union-find, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/making-a-large-island

### Statement

Given an n x n binary matrix, you may flip at most one 0 to 1. Return the size of the largest 4-directionally connected island of 1s achievable after the flip.

---

## 65. Number of Ways to Earn Points

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-05
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: dp, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/number-of-ways-to-earn-points

### Statement

Given several question types in an exam, each offering a fixed count of questions worth a fixed number of marks, count the number of distinct ways to score exactly a target number of points. Answers are returned modulo 10^9 + 7.

---

## 66. Isomorphic Strings

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/isomorphic-strings

### Statement

Given two strings s and t, determine whether they are isomorphic via a one-to-one character mapping that preserves order.

---

## 67. OA: Round-Trip Mission Schedule

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2025-07-10
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: two-pointer, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-codesignal-multi-mission-routing
- **related discussions**: 1

### Statement

Legacy CodeSignal SDE2 OA problem. Two locations A and B have fixed departure timetables. Each mission is a round trip A→B→A, departing only at the next available departure on each side; each leg takes 100 time units. Given a mission count, return the earliest completion time.

---

## 68. Number of Islands II (Dynamic Union-Find)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-08
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: union-find, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/number-of-islands-ii
- **related discussions**: 2

### Statement

Onsite coding round. Land cells are added to a water grid one at a time; after each addition return the current number of 4-directionally connected islands. A dynamic union-find problem (LC 305).

---

## 69. Phone Screen: Nested Add/Sub Expression Evaluator

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, recursion, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-expression-calculator
- **related discussions**: 3

### Statement

Recurring senior phone-screen prompt, also seen at the Hack2Hire problem set. Given a string expression using `add(a, b)` and `sub(a, b)` that can be nested arbitrarily, evaluate it. The interviewer states only 2–3 input/output examples; you must clarify the grammar and parser yourself.

---

## 70. Phone Screen: Bus Routes (LC 815)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-08
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, bfs, hashmap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-bus-routes
- **related discussions**: 3

### Statement

Onsite coding prompt, verbatim LeetCode 815 (Bus Routes). Given bus routes, find the minimum number of buses to travel between two stops. BFS on a stop-to-routes adjacency.

---

## 71. Construct Quad Tree

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-06-06
- **stage**: oa · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: recursion, grid, tree, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/construct-quad-tree
- **related discussions**: 2

### Statement

Given an n x n binary matrix where n is a power of 2, build and return the root of a quad tree representing the matrix. A region that is uniform becomes a leaf; otherwise it splits into four children for its quadrants.

---

## 72. OA: Minimum Edge Reversals from Every Root

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-05-31
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, tree, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-min-edge-reversals
- **related discussions**: 3

### Statement

Hack2Hire OA problem, equivalent to LeetCode 2858 / 1466 variant. Given an acyclic directed graph with `n` nodes and `n − 1` edges, for each node `r` compute the minimum number of edge reversals so that every node has a directed path to `r`.

---

## 73. Phone Screen: Tennis Rounds — Print Round Winners

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: simulation, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-tennis-rounds
- **related discussions**: 3

### Statement

Phone-screen prompt. Given a list of player rankings, simulate a single-elimination tournament. In each round, adjacent pairs play and the higher-ranked one wins. Print the surviving list after every round.

---

## 74. VO: AI-Assisted Coding / Debug Round (HackerRank IDE + AI)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-08
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: ai-tools, code-reading, debugging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/vo-ai-assisted-debug-round
- **related discussions**: 2

### Statement

New 2026 onsite round shape. Uses a HackerRank-hosted IDE that closely resembles Cursor, with an in-browser AI coding assistant available. The interviewer hands over a small full-stack app with a bug (e.g. transaction handling) and grades your ability to debug using AI as a teammate.

---

## 75. Kth Smallest Element in a BST

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-04-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, bst, tree, tree-traversal, recursion
- **url**: https://www.1point3acres.com/interview/problems/company/uber/kth-smallest-element-bst
- **related discussions**: 2

### Statement

Given the root of a binary search tree and an integer k, return the kth smallest value (1-indexed) in the tree.

---

## 76. OA: Final Prices with Special Discount (Sum + Unsold Indices)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-04-01
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, medium, algorithm
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-final-prices-monotonic-stack
- **related discussions**: 2

### Statement

Hack2Hire OA problem, extension of LeetCode 1475. For each index `i`, the discount equals the first later price `prices[j] ≤ prices[i]`. Items with no qualifying `j` sell at full price. Output the total final sum and the 0-based ascending indices of items sold at full price.

---

## 77. OA: Shortest Subarray with At Least K Distinct

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-04-01
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: two-pointer, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-shortest-subarray-k-distinct
- **related discussions**: 2

### Statement

Hack2Hire OA problem. Given an array `arr` and integer `k`, find the length of the shortest contiguous subarray that contains at least `k` distinct integers. Return `−1` if no such subarray exists.

---

## 78. OA: Minimum Operations to Reduce N to 0 (±2^i)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-04-01
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: bit-manipulation, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-min-ops-to-zero-power-of-two
- **related discussions**: 2

### Statement

Hack2Hire OA problem, equivalent to LeetCode 2571. Given a positive integer `n`, on each operation set `n = n ± 2^i` for some non-negative integer `i`. Return the minimum number of operations needed to reach 0.

---

## 79. Phone Screen: Squares of a Sorted Array

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: two-pointer, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-squares-of-sorted
- **related discussions**: 3

### Statement

Recurring warm-up phone-screen prompt, equivalent to LeetCode 977. Given a sorted integer array (negative values possible), return an array of the squares in non-decreasing order. The Senior MLE variant of the round adds a follow-up on the K-th smallest square.

---

## 80. Letter Combinations of a Phone Number

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-03-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: medium, backtracking, recursion, string
- **url**: https://www.1point3acres.com/interview/problems/company/uber/letter-combinations-phone-number
- **related discussions**: 2

### Statement

Given a string of digits from 2 to 9, return every possible letter combination it could represent using the classic phone keypad mapping. A standard backtracking exercise with follow-ups on recursion structure versus iterative generation.

---

## 81. Best Time to Buy and Sell Stock

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-03-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy, dp, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/best-time-buy-sell-stock
- **related discussions**: 2

### Statement

Given an array of daily prices, choose one day to buy and a later day to sell to maximize profit, returning 0 if no profitable transaction exists. Used as a phone-screen warm-up, with follow-ups exploring multiple solution approaches.

---

## 82. Phone Screen: Alien Dictionary (LC 269)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, topological-sort, string, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/phone-screen-alien-dictionary
- **related discussions**: 3

### Statement

Phone-screen / onsite coding prompt, verbatim LeetCode 269. Given words sorted by an alien lexicographical order, recover any valid character order. Topological sort on adjacent-word constraints.

---

## 83. OA: Maximum Items by Budget with Prefix Sums

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-02-11
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-prefix-sum-budget-purchase
- **related discussions**: 2

### Statement

Hack2Hire OA problem. Stalls have non-decreasing prices. For each query `(pos, amount)`, starting from stall `pos`, buy at most one item per stall while moving right; maximize the number of items bought without exceeding `amount`.

---

## 84. OA: Permutation Prefix Balanced (1..k Subarray Check)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-02-02
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-permutation-prefix-balanced
- **related discussions**: 3

### Statement

Hack2Hire OA problem. Given a permutation `p` of `1..n` and each `k` in `1..n`, decide whether `1..k` appears as a contiguous subarray somewhere in `p`. Return a binary string of length `n` (1 = balanced, 0 = not).

---

## 85. OA: Stairs vs Elevator Energy Trade-off

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-02-02
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: algorithm, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/oa-stairs-elevator-energy-tradeoff
- **related discussions**: 3

### Statement

Hack2Hire OA problem. Climb `n` floors using one elevator segment first (collects `e1` energy and costs `t1` time per floor) and then stairs (consumes `e2` per floor, time = `ceil(c / current_energy)`). Find the split that minimizes the absolute difference between elevator-time and stair-time. Energy must stay non-negative on stairs.

---

## 86. Onsite System Design: Driver Location Heatmap

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-15
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, storage, scaling, streaming, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-sd-driver-location-heatmap
- **related discussions**: 6

### Statement

The single most-asked Uber system design prompt. Design a service that ingests live driver locations and serves city-scale heatmap queries (and/or pushes real-time location to riders). Recruiters often pre-flag this prompt during the screen.

---

## 87. Onsite System Design: Top-K Popular Items (Restaurant / Search)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, streaming, scaling, storage, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-sd-top-k-popular-items
- **related discussions**: 4

### Statement

Recurring onsite SD prompt. Design a service that returns the top-K most-popular items (food items per restaurant, restaurants per city, search queries) over a rolling window. Variants ask for offline batch, near-realtime, or per-restaurant slicing.

---

## 88. MLE Onsite: ML System Design (Recommendation / Feed Ranking / ETA)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS
- **tags**: system-design, rag, retrieval, ml-knowledge, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/mle-onsite-ml-system-design
- **related discussions**: 4

### Statement

MLE / Applied Scientist onsite SD round. Design an ML-powered system end-to-end: feature pipeline, model training, online serving, evaluation, and A/B framework. Recurring prompts: Uber Eats restaurant recommendation, feed ranking, ETA prediction, driver-rider matching.

---

## 89. Onsite System Design: Nearby Driver Proximity Service

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: geohash, streaming, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-sd-nearby-driver-proximity-service
- **related discussions**: 1

### Statement

Design a proximity service that ingests frequently changing driver locations and returns eligible drivers within a rider's search radius. The round compares uniform grids, quadtrees, and Geohash/S2; then probes boundary cells, four-second update cadence, city partitioning, driver-state filtering, and map-result debouncing.

---

## 90. Design a Stock Price Alert Notification System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-07-28
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, notification, messaging, kafka, streaming, idempotency, sharding, state-machine
- **url**: https://www.1point3acres.com/interview/problems/company/uber/stock-price-alert-design
- **related discussions**: 1

### Statement

Design a stock-price alert system that ingests live quotes and delivers notifications without scanning every subscription on each update. One rotation uses absolute threshold crossings; a newer canonical variant triggers on an X% move over one user-selected rolling time unit, then removes the subscription after its first trigger or after one inactive month.

---

## 91. Onsite System Design: App User Behavior Data Platform

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: data-engineering, feature-engineering, analytics-dashboard, ingestion, schema-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-sd-app-user-behavior-data-platform
- **related discussions**: 1

### Statement

Design a system to analyze Uber App user behavior data and process raw events into downstream-ready data for models and business systems. The key is not just collecting clicks, but building pipelines for sessionization, funnel analysis, feature generation, quality checks, and schema evolution.

---

## 92. Fraud Detection

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, integrity, payment, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/uber/fraud-detection-design

### Statement

Design a machine learning system to detect fraudulent transactions for a payments platform, covering both the ML pipeline and the surrounding production architecture.

---

## 93. Onsite System Design: Daily Puzzle / Submission Scoring

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-30
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, storage, idempotency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-sd-puzzle-scoring
- **related discussions**: 1

### Statement

Onsite system design variant. Design a daily-puzzle product where users submit one answer per day, see their score and a leaderboard, and the system handles puzzle scheduling. Effectively a smaller-scale LeetCode-clone design.

---

## 94. Design an Online Shopping Cart

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-02-14
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, schema-design, concurrency, idempotency, caching, sharding, redis, kafka, data-modeling, streaming
- **url**: https://www.1point3acres.com/interview/problems/company/uber/online-shopping-cart-design

### Statement

Design an online shopping cart system for a marketplace-style product, with the key constraint of one active cart per merchant per user. The core difficulty lies in modeling per-merchant carts, handling concurrent updates from multiple devices, choosing the right database and indexes, and deciding whether to snapshot product price and metadata inside the cart. A reported follow-up extends the problem with a streaming-analytics requirement to rank highest average-rated products over arbitrary time windows.

---

## 95. Design a Pickup Area Driver Queue

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, geofence, location, streaming, kafka, redis, idempotency, state-machine, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/uber/pickup-area-driver-queue-design

### Statement

Design an internal pickup area driver queue service that maintains, in real time, an ordered queue of eligible drivers for each geofenced pickup zone (such as an airport staging lot). The core challenge is real-time geofence membership and ordered state management: deciding when a driver is in an area, keeping queue order stable under continuous movement and noisy events, and serving fast queue reads.

---

## 96. Onsite System Design: Rider-Driver Matching

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-26
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: location, geohash, scaling, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-sd-rider-driver-matching
- **related discussions**: 2

### Statement

Design Uber's rider-driver matching system. The round emphasizes million-scale driver location updates, low-latency geospatial search, scalable service decomposition, availability under network partitions, and consistency around match state.

---

## 97. Design Messenger

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-06-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, chat, messaging, websocket, kafka, redis, presence, idempotency, read-receipts
- **url**: https://www.1point3acres.com/interview/problems/company/uber/messenger-design
- **related discussions**: 2

### Statement

Design a Messenger-like 1-to-1 chat system similar to Facebook Messenger or WhatsApp, supporting near-real-time delivery, conversation history, offline delivery, and message status (accepted / delivered / read). A common follow-up pushes on whether Redis can really handle the routing throughput and on the exact checkmark semantics.

---

## 98. Uber Eats Search

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-03-06
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, search, retrieval, ranking, recommendation, ann, location, geohash, feature-engineering, ab-testing, two-tower
- **url**: https://www.1point3acres.com/interview/problems/company/uber/uber-eats-search
- **related discussions**: 2

### Statement

Design the Uber Eats search system for a Machine Learning Engineer onsite: a geo-aware restaurant and menu-item search that combines query understanding, hybrid candidate retrieval, hard marketplace filtering, and personalized ML ranking under a tight latency budget. The core challenge is connecting retrieval, ranking, online serving, feedback logging, and offline training into one coherent loop optimized toward completed orders.

---

## 99. Onsite BQ + Hiring Manager + Project Deep-Dive

- **category**: behavioral
- **duration**: 75 min
- **frequency**: Very high
- **last asked**: 2026-08-16
- **stage**: onsite-bq · onsite-deep-dive · final-round
- **access**: Members
- **roles**: SWE, MLE, EM
- **tags**: behavioral, deep-dive, presentation
- **url**: https://www.1point3acres.com/interview/problems/company/uber/onsite-bq-hm-deep-dive
- **related discussions**: 16

### Statement

Standard onsite HM round. 60–75 min. The first half is usually a project deep-dive (candidate may bring slides); the second half is structured BQ on collaboration, conflict, mentorship, and cross-functional influence.

---

## 100. Bar Raiser: Architecture, Influence, and Values

- **category**: behavioral
- **frequency**: Single report
- **last asked**: 2026-06-26
- **stage**: onsite-bq
- **access**: Members
- **roles**: SWE
- **tags**: behavioral, culture-fit, leadership
- **url**: https://www.1point3acres.com/interview/problems/company/uber/bar-raiser-architecture-and-values
- **related discussions**: 1

### Statement

A senior behavioral and architecture deep-dive round covering a complex system you designed, technical trade-offs, conflict and standards-setting, and examples tied to customer focus or ownership.

---

## 101. Staff (BPS) Round: Scope & Impact + Architecture

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-11
- **stage**: onsite-bq · onsite-deep-dive · final-round
- **access**: Members
- **roles**: MLE, SWE
- **tags**: behavioral, deep-dive, presentation
- **url**: https://www.1point3acres.com/interview/problems/company/uber/staff-bps-scope-architecture
- **related discussions**: 1

### Statement

Staff-level (L6) BPS loop replaces some standard rounds with two named segments: **Scope & Impact** (org-level decision radius) and **Architecture** (full-project architectural walkthrough). Coding is reduced or skipped; the depth of the architectural narrative is the deciding signal.

---

## 102. Scientist Onsite: Experiment Design (Switchback + CI Interpretation)

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-13
- **stage**: onsite-deep-dive · onsite-research
- **access**: Members
- **roles**: RS, DS, MLE
- **tags**: ml-knowledge, math-reasoning, probability, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/uber/scientist-experiment-design
- **related discussions**: 1

### Statement

Scientist II onsite round. Reasoning prompt rather than coding. Pick a metric (e.g. ETA vs session conversion), design an experiment to measure causal effect on Uber's marketplace, including rollout strategy, treatment design, guardrails, and confidence-interval interpretation.

---
