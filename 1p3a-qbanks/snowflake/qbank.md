# Snowflake — 1point3acres Question Bank (73 curated questions)

Source: 1point3acres qbank (`company=snowflake`), local capture 2026-08-27 (bank now paywalled).
Company page: <https://www.1point3acres.com/interview/problems/company/snowflake>

The site lists 267 entries for this company: **73 curated questions**, all captured below, 129 OJ links, 65 external links.

> Snowflake's loop is small and bank-driven: a recruiter screen, two back-to-back coding phone screens, then a virtual onsite that mixes coding, system design, and behavioral. The same handful of coding families (multi-source BFS on a grid, course-schedule variants, tree-node-deletion height problems, transactional KV stores) cycle through most candidates, so recognition speed matters more than novel problem solving.

Question statements only. The site's answer body is not reproduced here; the full captured page is archived verbatim under `raw/`.

## Index

1. Course Schedule with Time / Batches
2. Closest Bathroom / Desk on a Grid
3. DAG Allow / Disallow Propagation
4. Tree Node Deletion → Max Height
5. React / TypeScript Kanban Board
6. Priority Task Executor
7. RecordCollection Sort Colors
8. Forest Parent Array Delete Node
9. SQL: Project Duration & Budget per Employee
10. Filter System with Dynamic Blacklist
11. Reverse Alphanumeric Segments
12. Reach Target via Add / Double / Halve
13. Happy Number
14. Connect Four - Can Play Win
15. Document Store with Predicate Query
16. Frontend Grid: Robot Eats Candies
17. String-Command Calculator
18. Min Coins to Pay with Change Allowed
19. Service Failure Forensics
20. Find All Anagrams in a String
21. Grep With Context Lines
22. N-Queens
23. Top-K Search Terms (Per-User Deduped)
24. Meeting Rooms II
25. Cheapest Flights Within K Stops
26. K Top Selling Books
27. Word Search
28. Maximum Number of Events That Can Be Attended
29. Design Circular Queue
30. Grid Drop and Remove Duplicates
31. Merge Two Sorted Lists
32. Merge Intervals
33. Throne Inheritance
34. Recipe as Contiguous Ingredient Subsequence
35. Single Query Type, Max Revenue in K Minutes
36. Merge K Sorted Lists
37. Maximum Profit in Job Scheduling
38. Find Median from Data Stream
39. 01 Matrix
40. In-Memory File System
41. Longest Univalue Path
42. Valid Parentheses
43. Copy List with Random Pointer
44. Serialize and Deserialize Dictionary Trie
45. Pre-order Traversal Skipping Marked Nodes
46. Check if an Original String Exists Given Two Encoded Strings
47. Boundary of Binary Tree
48. Document Target Coverage and Minimum Window
49. Word Search II
50. Step-By-Step Directions From a Binary Tree Node to Another
51. Valid Tic-Tac-Toe State (Extended)
52. Event Stream Count in Time Range
53. Rate Limiter — Return Dropped Times
54. SnowCal
55. Wiki Page Shortest-Click Path
56. Rewrite Tree With Subtree Sums
57. Calculate Amount Paid in Taxes
58. Max Credits with K Classes (Interval-Style)
59. Transactional KV Store
60. Job Scheduler with Cron / Pause / Resume
61. Audit Logs Service
62. Distributed Tree Node Count
63. Jira AI Auto-Fix System
64. Design an RPC Abstraction Layer
65. Cross-Platform Logging Library
66. Design a Quota System
67. Web URL Crawler at Scale
68. ACL Service for Another Service
69. Design Distributed Metrics System
70. S3-Style Storage with Dedup
71. Infra Recruiter Screen: Experience and Tooling
72. AI-Conducted Screening Interview
73. Onsite Behavioral: Proud Project / Conflict / Failure

---

## 1. Course Schedule with Time / Batches

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-11
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: topological-sort, graph, bfs, dijkstra, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/course-schedule-time-batches

### Statement

Course Schedule (LC 207 / 210) is the base, but Snowflake almost always layers a time or batching follow-up: each course takes a given unit time, and a batch of courses must finish before the next batch begins. The follow-up asks for total wall-clock time to complete all courses given prerequisites.

---

## 2. Closest Bathroom / Desk on a Grid

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-17
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: bfs, grid, two-pointer, heap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/closest-bathroom-desk-grid

### Statement

Given a 2-D grid with cells marked `B` (bathroom), `D` (desk), and `_` (empty), return the shortest distance from each desk to its nearest bathroom under 4-directional movement. Variants use a 1-D array to find the nearest target value or character; follow-ups extend to global assignment and streaming input.

---

## 3. DAG Allow / Disallow Propagation

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, topological-sort, bitmask, set, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/dag-allow-disallow-letters

### Statement

Each node in a DAG carries an `allow` set and a `disallow` set of letters. Allow / disallow propagates transitively along edges. For every node, compute the effective (allowed minus disallowed) letter set.

---

## 4. Tree Node Deletion → Max Height

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-08-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, recursion, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/tree-node-deletion-max-height

### Statement

Given an N-ary tree and a set of deleted nodes, compute the maximum height after deletion, with descendants promoted past deleted ancestors. The canonical follow-up asks for the minimum deletions needed to keep height at most `k`; one phone-screen variant explicitly disallows dynamic programming.

---

## 5. React / TypeScript Kanban Board

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: react, frontend, typescript, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/react-typescript-kanban-board

### Statement

Build a React and TypeScript kanban board where tasks move between statuses through left/right controls rather than drag-and-drop. A required follow-up moves every just-updated task to the bottom of its destination status, followed by a scale discussion for 100,000 tasks.

---

## 6. Priority Task Executor

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: object-design, data-structure, scheduling, dedup, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/priority-task-executor

### Statement

Implement `addTask(task_id, priority, timestamp)` and `executeTask()`. The same task may be added multiple times at different timestamps; execution is priority-ordered, and an occurrence must be skipped if that task has already executed.

---

## 7. RecordCollection Sort Colors

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/recordcollection-sort-colors

### Statement

LeetCode 75 / Sort Colors variant: a `RecordCollection` holds a list of colors encoded as RGB / 0-1-2. The collection exposes `getColor(i)` and `swap(i, j)`; implement the in-place O(n) rearrangement and write tests against the class API.

---

## 8. Forest Parent Array Delete Node

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, array, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/forest-parent-array-delete-node

### Statement

Given a forest encoded as an array where `parent[i]` stores node `i`'s parent index and each root has `parent[i] == i`, delete a specified node and return a new parent-index array that still satisfies the same representation constraints. The post confirms valid input and no null / None entries; the child-promotion rule should be clarified before coding.

---

## 9. SQL: Project Duration & Budget per Employee

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-18
- **stage**: phone-screen
- **access**: Members
- **roles**: DS
- **tags**: sql, data-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/sql-project-duration-budget-per-employee

### Statement

Two SQL prompts: (1) names of employees who completed a project lasting more than 90 days; (2) average budget per employee per project, where each project's budget is split across its multiple employees. Schema not published; assume employees / projects plus a linking assignment table carrying start/end dates and a budget column.

---

## 10. Filter System with Dynamic Blacklist

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, set, hashmap, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/filter-system-dynamic-blacklist

### Statement

Design a filter with processFilter / processInput / emit: emit a value when it appears in the input stream and is not currently blacklisted. Follow-up adds an updateFlag so filter edits re-emit state changes — emit(false, v) when a previously-seen value becomes blocked, emit(true, v) when it becomes visible again.

---

## 11. Reverse Alphanumeric Segments

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, two-pointer, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/reverse-alphanumeric-segments

### Statement

Reverse each maximal run of alphanumeric characters (letters + digits) in place; every non-alphanumeric character (spaces, punctuation, apostrophes) stays at its original index. Example: "Hello! 2026!!! Let's dance!!!" → "olleH! 6202!!! teL's ecnad!!!".

---

## 12. Reach Target via Add / Double / Halve

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: math, bfs, greedy, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/reach-target-add-double-halve

### Statement

Given add(n)=n+2, dub(n)=n*2, split(n)=floor(n/2), implement func(a, b) returning one (not necessarily optimal) sequence of operations that takes a to b. BFS finds a shortest path, but the interviewer pushed for the simplest while-loop construction.

---

## 13. Happy Number

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-08
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: math, hashmap, cycle-detection, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/happy-number

### Statement

Write an algorithm to determine if a number n is happy.

---

## 14. Connect Four - Can Play Win

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: game-engine, simulation, backtracking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/connect-four-can-play-win

### Statement

You are given an m x n board for a two-player game.

---

## 15. Document Store with Predicate Query

- **category**: coding
- **duration**: 55 min
- **frequency**: Single report
- **last asked**: 2026-04-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, parsing, search, hashmap, sharding, distributed-systems, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/doc-predicate-store

### Statement

Build a web-server-style document store with `InsertDoc(filename)` and `CheckContains(filename, predicate)`. Predicates are boolean expressions over terms (`a && b || c`). Follow-ups extend to `GetAllFiles(predicate)` and a distributed-systems design discussion on sharding and replication.

---

## 16. Frontend Grid: Robot Eats Candies

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: frontend, bfs, grid, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/frontend-grid-robot-candy

### Statement

Frontend phone screen for a Snowflake frontend role: implement a small JavaScript game where a robot navigates a grid eating candies. Grid scaffolding and helper functions are provided.

---

## 17. String-Command Calculator

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: parsing, recursion, stack, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/calculator-string-commands

### Statement

Implement a calculator that consumes a stream of string commands (`ADD 2`, `MULT 3`, ...) and returns the running result. The follow-up nests a calculation inside another (parenthesized sub-expression).

---

## 18. Min Coins to Pay with Change Allowed

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, greedy, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/min-coins-with-change

### Statement

Coin denominations `{1, 5, 10, 50, 100, 200}` available in unlimited supply. Pay an exact amount `n`, but overpayment is allowed and the change comes back in the same denominations. Return the minimum total number of coins exchanged (paid + returned).

---

## 19. Service Failure Forensics

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-24
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, troubleshooting, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/service-failure-forensics

### Statement

You are analyzing service outages using logs and dependency data.

---

## 20. Find All Anagrams in a String

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, hashmap, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/find-all-anagrams-in-a-string

### Statement

Given two strings s and p, return an array of all the start indices of p's anagrams in s.

---

## 21. Grep With Context Lines

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string-processing, parsing, io, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/grep-with-context-lines

### Statement

You are given a scanned document represented as an array of strings, where each string is one line.

---

## 22. N-Queens

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: backtracking, recursion, array, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/n-queens

### Statement

The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.

---

## 23. Top-K Search Terms (Per-User Deduped)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, set, heap, top-k, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/top-k-search-terms-deduped

### Statement

Return the top-K most popular search terms across all users. A single user searching the same term multiple times counts only once for that term.

---

## 24. Meeting Rooms II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, heap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/meeting-rooms-ii

### Statement

Given an array of meeting time interval objects consisting of start and end times [[start1,end1],[start2,end2],...] (starti < endi), find the minimum number of meeting rooms required to schedule all meetings without any conflicts.

---

## 25. Cheapest Flights Within K Stops

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, shortest-path, bellman-ford, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/cheapest-flights-within-k-stops

### Statement

There are n cities connected by some flights.

---

## 26. K Top Selling Books

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-01
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: top-k, heap, hashmap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/k-top-selling-books

### Statement

Design a data structure that tracks cumulative book sales and returns the top sellers after each update.

---

## 27. Word Search

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, backtracking, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/word-search

### Statement

Given a 2-D grid of characters board and a string word, return true if the word is present in the grid, otherwise return false.

---

## 28. Maximum Number of Events That Can Be Attended

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: greedy, heap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/maximum-events-attend-greedy

### Statement

You are given an array of events where events[i] = [startDayi, endDayi].

---

## 29. Design Circular Queue

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: queue, array, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/design-circular-queue

### Statement

Design your implementation of the circular queue.

---

## 30. Grid Drop and Remove Duplicates

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, simulation, deduplication, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/grid-drop-and-remove-duplicates

### Statement

You are implementing a Connect-4-like board utility on an m x n grid.

---

## 31. Merge Two Sorted Lists

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-20
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, merge, recursion, easy
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/merge-two-sorted-lists

### Statement

You are given the heads of two sorted linked lists list1 and list2.

---

## 32. Merge Intervals

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-20
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, sorting, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/merge-intervals

### Statement

Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.

---

## 33. Throne Inheritance

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-18
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, hashmap, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/throne-inheritance

### Statement

A kingdom consists of a king and his children in a family tree.

---

## 34. Recipe as Contiguous Ingredient Subsequence

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, hashing, two-pointer, sliding-window, state-machine, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/recipe-ingredient-subsequence

### Statement

Given an ordered ingredient list and a set of recipes (each itself an ordered sub-list of ingredients), decide whether each recipe occurs as a contiguous subsequence of the ingredient list. Follow-ups: O(1) extra space; ingredient list arrives as a stream.

---

## 35. Single Query Type, Max Revenue in K Minutes

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: math, greedy, easy
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/single-query-max-revenue

### Statement

A virtual warehouse can execute one of `n` query types. Each query type has a per-run revenue and per-run duration. Pick a single query type and run it as many times as fits in `k` minutes; return the chosen type and the maximum total revenue.

---

## 36. Merge K Sorted Lists

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-13
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, heap, divide-and-conquer, merge, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/merge-k-sorted-lists

### Statement

You are given an array of k linked lists lists, where each list is sorted in ascending order.

---

## 37. Maximum Profit in Job Scheduling

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-07
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, interval, binary-search, sorting, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/maximum-profit-in-job-scheduling

### Statement

You are given n jobs where every job is represented by three arrays: startTime, endTime, and profit.

---

## 38. Find Median from Data Stream

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: heap, data-structure, streaming, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/find-median-from-data-stream

### Statement

The median is the middle value in a sorted list of integers.

---

## 39. 01 Matrix

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-30
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/01-matrix

### Statement

Given an m x n binary matrix mat, return the distance of the nearest 0 for each cell.

---

## 40. In-Memory File System

- **category**: coding
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2025-12-27
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, trie, filesystem, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/in-memory-file-system

### Statement

Design and implement an in-memory file system supporting `ls`, `mkdir`, `addContentToFile`, `readContentFromFile` (LC 588).

---

## 41. Longest Univalue Path

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-23
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/longest-univalue-path

### Statement

Given the root of a binary tree, return the length of the longest path where each node in the path has the same value.

---

## 42. Valid Parentheses

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, string, parsing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/valid-parentheses

### Statement

You are given a string s consisting of the following characters: '(', ')', '{', '}', '[' and ']'.

---

## 43. Copy List with Random Pointer

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-14
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/copy-list-with-random-pointer

### Statement

A linked list of length n is represented in this format for testing: nodes[i] = [val, randomIndex] val is the value of the ith node.

---

## 44. Serialize and Deserialize Dictionary Trie

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: trie, compression, parsing, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/serialize-and-deserialize-dictionary-trie

### Statement

Implement a codec for a dictionary trie.

---

## 45. Pre-order Traversal Skipping Marked Nodes

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, tree-traversal, recursion, easy
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/tree-preorder-skip-invalid

### Statement

A tree is given as an edge list. Some nodes are marked invalid. Return the pre-order traversal of the tree that skips the invalid nodes (their children promote to attach at the nearest valid ancestor).

---

## 46. Check if an Original String Exists Given Two Encoded Strings

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-09
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, dp, recursion, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/encoded-string-original-exists

### Statement

An original string is encoded by replacing some non-empty substrings with their lengths.

---

## 47. Boundary of Binary Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/boundary-of-binary-tree

### Statement

Given the root of a binary tree, return the values of its boundary in anti-clockwise direction starting from the root.

---

## 48. Document Target Coverage and Minimum Window

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-05
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, hashmap, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/document-target-coverage-and-minimum-window

### Statement

You are given: A document string with arbitrary characters (letters, digits, punctuation, etc.) A targets list of words The interview progresses in 3 parts: 1.

---

## 49. Word Search II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: trie, backtracking, dfs, grid, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/word-search-ii

### Statement

Given a 2-D grid of characters board and a list of strings words, return all words that are present in the grid.

---

## 50. Step-By-Step Directions From a Binary Tree Node to Another

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-02
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/binary-tree-step-by-step-directions

### Statement

You are given the root of a binary tree with n nodes where each node has a unique value from 1 to n.

---

## 51. Valid Tic-Tac-Toe State (Extended)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, simulation, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/tic-tac-toe-valid-state-extended

### Statement

Extended variant of LC 794: given a board state (3×3 or larger N×N), decide whether it is reachable from a legal sequence of moves under standard tic-tac-toe rules.

---

## 52. Event Stream Count in Time Range

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, binary-search, streaming, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/event-stream-count-range

### Statement

Stream of `(event_type, timestamp)` events with monotonically increasing timestamps. Implement `Receive(event_type, timestamp)` and `Count(event_type, start_time, end_time)` returning how many events of that type fell in the inclusive window.

---

## 53. Rate Limiter — Return Dropped Times

- **category**: coding
- **duration**: 50 min
- **frequency**: Low
- **last asked**: 2026-08-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, queue, simulation, rate-limiting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/rate-limiter-dropped-requests

### Statement

Given request arrival times, enforce rate-limit rules and identify rejected requests. An online variant expands the task to multiple simultaneous rules, thread safety, handler failures, and deferred processing of throttled work.

---

## 54. SnowCal

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: parsing, simulation, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/snowcal

### Statement

SnowCal is a small programming language that keeps a single integer in memory. The program arrives as one complete list of instructions rather than a stream of individual commands.

---

## 55. Wiki Page Shortest-Click Path

- **category**: coding
- **duration**: 50 min
- **frequency**: Low
- **last asked**: 2026-07-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bfs, graph, api-integration, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/wiki-page-shortest-path

### Statement

Use a linked-page API to traverse an implicit web graph. Variants ask either for the shortest click distance to a target or for every reachable page, followed by a multithreaded traversal discussion.

---

## 56. Rewrite Tree With Subtree Sums

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-07-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, dfs, recursion, parallelism, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/rewrite-tree-with-subtree-sums

### Statement

You are given the roots of two complete binary trees, root1 and root2.

---

## 57. Calculate Amount Paid in Taxes

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-06-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, math, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/calculate-amount-paid-in-taxes

### Statement

You are given a 0-indexed 2D integer array brackets where brackets[i] = [upperi, percenti] means that the ith tax bracket has an upper bound of upperi and is taxed at a rate of percenti.

---

## 58. Max Credits with K Classes (Interval-Style)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, interval, binary-search, sorting, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/course-credit-max-k-classes

### Statement

Given a list of classes, each with `(start_time, end_time, credit)`, and a cap `K` on the number of classes that may be taken, return the maximum total credit achievable subject to no overlapping classes.

---

## 59. Transactional KV Store

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-26
- **stage**: phone-screen · onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, transactions, concurrency, in-memory-database, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/kv-store-transactions

### Statement

Implement an in-memory key-value store with `get`, `set`, `delete`, and nested `begin` / `commit` / `rollback`. Required to support O(1) amortized operations and a multi-threading follow-up. A senior-loop variant moves values to disk and reframes the round around lock contention.

---

## 60. Job Scheduler with Cron / Pause / Resume

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-10
- **stage**: onsite-system-design · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, scheduling, queueing, persistence, idempotency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/job-scheduler-cron-pause-resume

### Statement

Design a distributed job scheduler that supports cron-style recurring jobs, pause (allow in-flight jobs to complete) and resume. Workers are assumed unlimited.

---

## 61. Audit Logs Service

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-25
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, storage, scaling, sharding, access-control, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/audit-logs-service

### Statement

Design an audit-log service where users can query audit events for their own account. The round focuses on transaction-log storage, scaling the write and query paths, and keeping account-scoped queries low latency.

---

## 62. Distributed Tree Node Count

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, tree, parallelism, map-reduce, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/distributed-tree-node-count

### Statement

You are given an N-ary tree where each node is a distributed server.

---

## 63. Jira AI Auto-Fix System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, queueing, ai-tools, idempotency, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/jira-ai-auto-fix-system

### Statement

Design an internal tool that pulls new Jira bug tickets, where each bug corresponds to a test failure, and sends each ticket to an AI system that can generate a fix PR in roughly 30 minutes. The service buffers work with a message queue, tracks ticket state in a DB, and needs schema, sharding, and scaling discussion.

---

## 64. Design an RPC Abstraction Layer

- **category**: system-design
- **frequency**: Single report · New
- **last asked**: 2026-05-08
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: rpc, distributed-systems, idempotency, versioning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/rpc-abstraction-layer

### Statement

Design an RPC-style abstraction over hand-written REST calls so application code invokes a local-looking method (e.g. exampleService.processSomething(...)) instead of building HTTP requests by hand. A strong answer covers client/server stubs, marshalling, binding / service discovery, transport hiding, retries with idempotency and duplicate filtering, and interface versioning.

---

## 65. Cross-Platform Logging Library

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, ingestion, queueing, frontend, backend, batching, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/logging-library-design

### Statement

Design a logging library that runs in both web and mobile clients and ships logs to a backend without degrading client-side performance. The round drives hard on the backpressure / batching trade-off when call volume grows.

---

## 66. Design a Quota System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-02-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: quotas, distributed-systems, rate-limiting, caching, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/design-a-quota-system

### Statement

Design a quota management system that tracks and enforces resource usage limits across multiple backend services.

---

## 67. Web URL Crawler at Scale

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: web-crawler, distributed-systems, queueing, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/web-url-crawler-at-scale

### Statement

You are given helper APIs: fetchpage(url) -> str returns page content parseurls(content) -> list[str] returns outgoing URLs from that page Design traversal and failure handling for crawling problems.

---

## 68. ACL Service for Another Service

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-27
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, access-control, caching, throttling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/acl-service-design

### Statement

Design a service that performs ACL-style authorization checks on behalf of another service: given (subject, resource, action) decide allow / deny.

---

## 69. Design Distributed Metrics System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-12-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, metrics, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/design-distributed-metrics-system

### Statement

Design Distributed Metrics System

---

## 70. S3-Style Storage with Dedup

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-23
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, storage, hashing, dedup, compression, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/s3-dedup-storage-design

### Statement

Design an S3-like blob storage system, scoped down from full S3 and focused on avoiding duplicate file storage to optimize cost.

---

## 71. Infra Recruiter Screen: Experience and Tooling

- **category**: behavioral
- **duration**: 15 min
- **frequency**: Single report
- **last asked**: 2026-08-17
- **stage**: recruiter-screen
- **access**: Members
- **roles**: Infra Eng
- **tags**: infra, fit
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/infra-recruiter-experience-screen

### Statement

A sub-15-minute HR screen for an Infra role covering prior infrastructure experience, infrastructure or platform projects, and the tools and technology stack used. The recruiter then checks the candidate's fit with the target team before deciding the next step.

---

## 72. AI-Conducted Screening Interview

- **category**: behavioral
- **duration**: 20 min
- **frequency**: Low
- **last asked**: 2026-08-08
- **stage**: recruiter-screen
- **access**: Members
- **roles**: SWE
- **tags**: ai-screening, screening, deep-dive, ai-tools, verbal, new-round, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/ai-conducted-screening

### Statement

An automated AI interviewer conducts a short screen for general SWE applicants. It checks background and logistics, then adaptively deep-dives into projects, design decisions, conflicts, and workplace AI usage while recording camera, screen, and transcript.

---

## 73. Onsite Behavioral: Proud Project / Conflict / Failure

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Low · New
- **last asked**: 2026-08-01
- **stage**: onsite-bq
- **access**: Members
- **roles**: SWE
- **tags**: behavioral, ownership, conflict, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snowflake/onsite-behavioral-proud-conflict-failure

### Statement

The onsite behavioral round asks for a project the candidate is most proud of, a conflict or failure example, the biggest challenge faced, advice for the current team, and motivation for changing jobs. Prepare concise stories that connect ownership, technical judgment, and collaboration outcomes.

---
