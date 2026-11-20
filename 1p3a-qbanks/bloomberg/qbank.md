# Bloomberg — 1point3acres Question Bank (34 curated questions)

Source: 1point3acres qbank (`company=bloomberg`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/bloomberg>

The site lists 119 entries for this company: **34 curated questions** (below), 85 OJ links, 0 external links. Only the curated 34 are captured here.

> Bloomberg's loop is a coding-heavy gauntlet: three back-to-back technical rounds each pack two LeetCode-style problems with little hand-holding, plus an HR screen and an engineering manager round that anchors on a single deep project story. The coding bar emphasizes pristine communication — dry-running, edge cases, and complexity discussion are graded as heavily as the solution itself, and interviewers routinely push for second-best alternatives even after a working answer. New-grad and tech-screen verdicts often arrive within hours, while jump-shift onsites favor candidates who can explain past trade-offs without prompting.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Merge Intervals (LC 56)
2. Duplicate Records Within a Time Window
3. Longest Palindromic Substring
4. Design Underground System
5. Validate Binary Search Tree
6. Active Users Sliding Window
7. Find Tree Root from Edge List
8. Round-Robin Tournament Schedule
9. Gas Tank Grid Reachability
10. Candy Crush 1D
11. Equal Character Frequency (Remove-One Variant)
12. Meeting Rooms II
13. Decode String
14. Todo List OOP Design
15. Wordle Character Compare
16. String to Integer (atoi)
17. Binary Tree Vertical Order Traversal
18. Trapping Rain Water
19. Simplified Grep
20. Friends Of Appropriate Ages
21. Shortest Path in Grid with K Wall Breaks
22. LRU Cache
23. Minimum Cost Flight Split
24. Subway / Network Connectivity
25. Top-K Frequent Tickers (Stock Leaderboard)
26. Flatten a Multilevel Doubly Linked List
27. Lottery System / Insert Delete GetRandom O(1)
28. Word Search in 2D Grid
29. VWAP Analytic Provider
30. Holiday Service Latency Optimization
31. Real-time Market Data System
32. Streaming Social-Media Mentions & Aggregation
33. Trade Processor with Subscribe / Unsubscribe
34. Behavioral Question Pool

---

## 1. Merge Intervals (LC 56)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-08-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: interval, sorting, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/merge-intervals
- **related discussions**: 1

### Statement

Given a collection of intervals, merge every overlapping group and return non-overlapping intervals covering the same ranges. This appeared in a 60-minute phone screen with an explicit dry-run requirement.

---

## 2. Duplicate Records Within a Time Window

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, dedup, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/duplicate-records-time-window
- **related discussions**: 1

### Statement

Senior phone-screen stream-processing problem: given log records of `id, text, title, timestamp`, emit every record whose content repeats within a 60-second window, with an explicit follow-up to evict expired entries from the tracking structure so memory stays bounded by the window. Candidates often shorthand it as "the LRU question", but the real pattern is sliding-window deduplication.

---

## 3. Longest Palindromic Substring

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, dp, two-pointer, palindromes, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/longest-palindromic-substring
- **related discussions**: 1

### Statement

Find the longest palindromic substring of a string (LeetCode 5). A Bloomberg phone-screen problem asked alongside Validate BST. Expand-around-center is the expected baseline; the 'can you do better' push points at Manacher's O(n).

---

## 4. Design Underground System

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, hashmap, design-implementation, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/design-underground-system
- **related discussions**: 1

### Statement

A variant of Design Underground System (LeetCode 1396): track customer check-in/check-out events keyed by id and report average travel time between station pairs. An onsite coding round that explicitly grades communication during implementation; clarify the variant's twist before coding.

---

## 5. Validate Binary Search Tree

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, bst, dfs, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/validate-binary-search-tree
- **related discussions**: 1

### Statement

Validate whether a binary tree is a binary search tree (LeetCode 98). A staple Bloomberg phone-screen problem, paired with Longest Palindromic Substring in the same round; the interviewer probes the bounds-based versus in-order approaches and the integer-overflow edge.

---

## 6. Active Users Sliding Window

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: streaming, sliding-window, hashmap, queue, tree, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/active-users-sliding-window
- **related discussions**: 1

### Statement

Implement a request-log aggregator that supports adding `(timestamp, userId)` events and returning the number of distinct users active in the last five minutes. The main trap is duplicate users inside the window and whether timestamps are guaranteed to arrive in order.

---

## 7. Find Tree Root from Edge List

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-25
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, graph, hashmap, set, easy
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/find-tree-root-from-edges
- **related discussions**: 1

### Statement

Given an edge list `[node, [children]]` describing a tree of log nodes, find the root — the single node that is never anyone's child. The Bloomberg twist is that the input may include duplicates or noise; treat it as the warm-up before the harder bucketize-by-boundary follow-up.

---

## 8. Round-Robin Tournament Schedule

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: backtracking, recursion, math, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/round-robin-tournament-schedule
- **related discussions**: 1

### Statement

Given `n` teams, enumerate all valid round-robin schedules where each round pairs every team with a different opponent, every pair plays exactly once across all rounds, and each team plays every other team. Trickier when `n` is odd (a bye must be modeled).

---

## 9. Gas Tank Grid Reachability

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-31
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, state-machine, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/gas-tank-grid-bfs
- **related discussions**: 1

### Statement

Walk a 2-D grid from a start to a goal cell with a finite gas budget. Some cells are obstacles, some refuel you to a fixed capacity. Decide whether the goal is reachable. The trap is that gas adds a state dimension — a revisit at higher gas may unlock paths a first visit couldn't.

---

## 10. Candy Crush 1D

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, string-processing, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/candy-crush-1d
- **related discussions**: 1

### Statement

One-dimensional Candy-Crush style elimination: collapse runs of three or more equal adjacent values until nothing more can collapse. The Bloomberg twist usually involves a slightly non-standard input format (per-character or per-(value, count) pairs) that makes the obvious LeetCode-style solution fail a test case.

---

## 11. Equal Character Frequency (Remove-One Variant)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/equal-character-frequency
- **related discussions**: 1

### Statement

Decide whether all characters in a string occur with the same frequency, then a follow-up: whether removing exactly one character can produce a string in which all surviving characters have equal frequency. The interviewer escalates by demanding better-than-naive time and space.

---

## 12. Meeting Rooms II

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: heap, interval, sorting, sliding-window, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/meeting-rooms-ii
- **related discussions**: 1

### Statement

Given a list of meeting time intervals, return the minimum number of conference rooms required. The canonical sweep / heap problem; Bloomberg likes it in phone screens because the cleanest solution involves either a min-heap of end times or a paired array sweep.

---

## 13. Decode String

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-08
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, string-processing, parsing, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/decode-string
- **related discussions**: 1

### Statement

Decode a string encoded with the `k[encoded_string]` repetition rule, supporting arbitrary nesting and multi-digit counts. A staple Bloomberg phone-screen problem where the interviewer pushes for the clean stack-based solution and explicit complexity reasoning.

---

## 14. Todo List OOP Design

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, hashmap, linked-list, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/todo-list-oop
- **related discussions**: 1

### Statement

Design a small Todo-list service supporting `add`, `delete`, `get_todo` (pending only), and `get_all`, given a `check_todo(id)` predicate that tells you whether an entry is already done. Coding-flavored OOD round — pick data structures that make every operation cheap.

---

## 15. Wordle Character Compare

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/wordle-character-compare
- **related discussions**: 1

### Statement

Given a guess and a target word of equal length, color each guess letter: green for correct letter and position, yellow for correct letter wrong position, gray otherwise — but obey Wordle's nuanced "each target letter can be matched at most once" rule.

---

## 16. String to Integer (atoi)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string-processing, parsing, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/string-to-integer-atoi
- **related discussions**: 1

### Statement

Implement a robust `atoi` that parses an optional sign, leading whitespace, and digit run, clamping to the 32-bit signed range. Bloomberg uses it as a phone-screen warm-up where overflow handling and a few targeted edge cases distinguish strong candidates.

---

## 17. Binary Tree Vertical Order Traversal

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, bfs, tree-traversal, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/binary-tree-vertical-order-traversal
- **related discussions**: 1

### Statement

Group binary-tree node values by their column index (left of root negative, right positive) and return columns left-to-right, with each column's values in top-to-bottom level order. Watch the tie-break rule for nodes at the same column and row.

---

## 18. Trapping Rain Water

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-24
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: two-pointer, array, monotonic-stack, dp, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/trapping-rain-water
- **related discussions**: 1

### Statement

Given an elevation map of non-negative bar heights, compute how much water can be trapped after raining. Bloomberg interviewers commonly insist on the two-pointer `O(1)`-space solution after a stack or DP answer.

---

## 19. Simplified Grep

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string-processing, object-design, io, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/simplified-grep
- **related discussions**: 1

### Statement

Implement a simplified `grep`: scan a file line by line and emit lines containing a target substring, with extensibility for additional flags (case-insensitive, invert match, line numbers). Graded for clean parsing and extensible code shape, not raw algorithm.

---

## 20. Friends Of Appropriate Ages

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, math, prefix-sum, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/friends-of-appropriate-ages
- **related discussions**: 1

### Statement

Given an array of ages, count the number of valid friend requests under the rule that person A will request person B iff B's age is in `(0.5*A + 7, A]`. Bloomberg insists on the `O(σ^2)` bucket-frequency solution over the naive `O(n^2)`.

---

## 21. Shortest Path in Grid with K Wall Breaks

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-07
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, state-machine, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/shortest-path-with-k-wall-breaks
- **related discussions**: 2

### Statement

Find the shortest path from the top-left to the bottom-right of a binary grid, allowed to eliminate at most `k` obstacle cells along the way. Standard BFS plus a `remaining-breaks` state dimension.

---

## 22. LRU Cache

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, linked-list, hashmap, caching, concurrency, refactoring, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/lru-cache
- **related discussions**: 3

### Statement

Implement a fixed-capacity LRU cache supporting `get` and `put` in O(1). Bloomberg asks it both as a standard cache implementation and as an OOP design discussion, with follow-ups around thread safety, persistence, eviction callbacks, TTL, and class boundaries. In the AI-role R2 phone screen, the implementation may be provided and the task becomes refactoring it behind a generic eviction-policy abstraction that can also accommodate LFU.

---

## 23. Minimum Cost Flight Split

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: greedy, sorting, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/minimum-cost-flight-split
- **related discussions**: 2

### Statement

Schedule exactly half of `n` people to fly to San Francisco and the rest to New York, minimizing total travel cost. The follow-up changes the split to `k` people for one city and may require implementing the DP baseline rather than only the greedy shortcut.

---

## 24. Subway / Network Connectivity

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, bfs, dfs, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/subway-network-connectivity
- **related discussions**: 2

### Statement

Given a network of subway stations described as adjacency data, build a function that returns whether any two given stations are connected. Tests graph modeling, choice between BFS / DFS / Union-Find, and how to scale across many queries.

---

## 25. Top-K Frequent Tickers (Stock Leaderboard)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, heap, top-k, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/top-k-frequent-stock-tickers
- **related discussions**: 2

### Statement

Design a data structure that supports inserting stock ticker symbols (or transactions), removing them, and returning the current top-K most-frequent tickers. Bloomberg's house variant: emphasizes API-rate awareness and explicit input / output formats rather than the textbook LeetCode "return top K" snapshot.

---

## 26. Flatten a Multilevel Doubly Linked List

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-22
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: linked-list, dfs, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/flatten-multilevel-doubly-linked-list
- **related discussions**: 3

### Statement

Flatten a doubly linked list whose nodes carry an extra child pointer into a single-level doubly linked list, in-place. A long-running Bloomberg classic that appears in phone screens and onsite rounds alike.

---

## 27. Lottery System / Insert Delete GetRandom O(1)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, array, sampling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/lottery-system-insert-delete-getrandom
- **related discussions**: 3

### Statement

Design a data structure for a lottery that supports adding a participant, removing a participant, and randomly picking a winner — all in O(1). Appears framed both as the classic LeetCode problem and as a product story about a raffle / random pick.

---

## 28. Word Search in 2D Grid

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-01
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dfs, backtracking, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/word-search-grid
- **related discussions**: 2

### Statement

Given an m×n board of characters and a target word, decide whether the word exists in the board as a path of horizontally or vertically adjacent cells, without reusing any cell. Bloomberg interviewers consistently push DFS over BFS here because BFS blows memory on long words.

---

## 29. VWAP Analytic Provider

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-07
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: streaming, sharding, pubsub, websocket, ingestion, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/vwap-analytic-provider
- **related discussions**: 1

### Statement

Team-loop system-design round: ingest trade ticks from 100 exchanges (100K–1M symbols each, ~100 ticks/sec per symbol, ~20 bytes per tick), maintain per-(symbol, window) VWAP in a sharded stateful engine, and serve ~100K screen users via push and ~1K enterprise users via API. Expected beats: per-exchange feed handlers normalizing to a canonical tick, symbol sharding with running sums, snapshot/changelog recovery, and a pub/sub → WebSocket / Redis / query-API delivery tier.

---

## 30. Holiday Service Latency Optimization

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-07
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: caching, service-design, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/holiday-service-latency-optimization
- **related discussions**: 1

### Statement

Team-loop system-design round: a thin Holiday Service sits between a C++ client library and a lower-level data service owned by another team, exposing `getData(countryCode, year)` and reformatting dates. The round pivots on one follow-up — end-to-end requests take ~150 ms and clients want materially less — pushing the discussion into multi-tier caching, prefetch/sync, and what the client SDK can absorb.

---

## 31. Real-time Market Data System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, messaging, ingestion, dedup, streaming, scaling, storage, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/real-time-market-data-system
- **related discussions**: 1

### Statement

Design Bloomberg's real-time market-data distribution: upstream exchanges → Loader servers → UDP fan-out of multiple copies → Processor servers that rebuild reliability (loss, duplicates, ordering) at the application layer, then dedup, store, and serve to connected subscribers. A domain-core onsite SD prompt.

---

## 32. Streaming Social-Media Mentions & Aggregation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: streaming, kafka, ingestion, search, feed, notification, scaling, ambiguous-spec, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/streaming-social-media-mentions-aggregation
- **related discussions**: 1

### Statement

Design a system that ingests a stream of social posts and news articles, extracts company / ticker mentions, supports windowed visualizations from minutes to days, lets users subscribe to entities and get notifications, and supports keyword search across the raw corpus at very high QPS. A wide-scope SD prompt typical of the open-ended Bloomberg onsite round.

---

## 33. Trade Processor with Subscribe / Unsubscribe

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-27
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, design-implementation, pubsub, concurrency, ambiguous-spec, hard
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/trade-processor-subscribe-unsubscribe
- **related discussions**: 1

### Statement

Implement a `TradeProcessor` that accepts incoming trades, lets clients subscribe to trades matching a filter with a callback, returns a handle for the subscription, and supports unsubscription. A coding-flavored design round where the prompt is deliberately under-specified — clarification is half the work.

---

## 34. Behavioral Question Pool

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-06-16
- **stage**: recruiter-screen · phone-screen · onsite-bq · final-round
- **access**: Members
- **roles**: SWE
- **tags**: behavioral, star, why-company, deep-dive, easy
- **url**: https://www.1point3acres.com/interview/problems/company/bloomberg/behavioral-question-pool
- **related discussions**: 8

### Statement

Bloomberg interleaves short behavioral prompts into every round — recruiter screen, each technical round (10–15 minutes at the top), HR round, and the EM final. The pool is small and recycled; preparing five strong STAR stories covers most prompts.

---
