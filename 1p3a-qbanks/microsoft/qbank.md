# Microsoft — 1point3acres Question Bank (49 curated questions)

Source: 1point3acres qbank (`company=microsoft`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/microsoft>

The site lists 276 entries for this company: **49 curated questions** (below), 227 OJ links, 0 external links. Only the curated 49 are captured here.

> Microsoft runs two parallel hiring tracks: the standardized Hiring Event (HE) loop covering Azure, Cloud, Copilot, and Office orgs, and Mustafa Suleyman's MAI org with its own recruiter pool and a distinctly OpenAI-flavored coding rotation. The HE bar is consistent — four 60-minute rounds back-to-back on a fixed weekday — with medium-difficulty algorithms and one system-design slot; MAI loops add project deep-dive depth and pull problems from an in-memory-DB / beam-search / streaming-decoder pool that does not overlap with classic LeetCode prep.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. LRU Cache (LC 146) + Multithreading Variant
2. In-Memory SQL Engine
3. DNA Shotgun Sequencing
4. Delete and Earn (LC 740)
5. Count Binary Substrings (LC 696)
6. Alien Dictionary (LC 269)
7. Multi-Key Bounded Task Executor
8. Spiral Matrix (LC 54) + Follow-Ups
9. Binary Tree Zigzag Level Order Traversal (LC 103)
10. Palindrome Linked List (LC 234) + Engineering Follow-Up
11. Lowest Common Ancestor (LC 235) with a Follow-Up Variant
12. Contiguous Memory Allocator
13. Tiny Next-Token Prediction Model (PyTorch Fill-In)
14. SFT Sample Packing
15. Sliding-Window Stream Session Bucketing
16. In-Place Sub-Matrix Move
17. KV Store with Snapshot Versions
18. Beam Search Decoder Implementation
19. Gen / Score Task Lock Scheduler
20. Multiply Strings (LC 43)
21. Parse Email Addresses from a Log String
22. 15-Puzzle Minimum Moves (BFS)
23. Idempotency API with Idempotency-Key Header
24. Distance from Each Node to the Cycle in an Undirected Graph
25. Sort 0..32000 with Bit-Vector Storage
26. Validate Lat/Long With Minimum Expensive API Calls
27. Maximum-Value Frequency in Array Suffixes
28. Merge Intervals (LC 56) + Interview Variant
29. Minimum Fuel Cost Across a Road Network
30. Top-K Largest Elements (Retain / Rank Stores)
31. Org Chart Report Count (Tree Aggregation)
32. Balanced Permutation Prefix Check
33. Count Positive Pairs Satisfying 1/x + 1/y = 1/N
34. Resumable Batch Dataloader with Weighted Sampling
35. Streaming Stop-Token Detection
36. Hand-Written K-Means
37. Palindrome Number + Next-Greater Palindrome
38. Rate Limiter (Design + Implementation)
39. Todo List Full-Stack System Design
40. Chatbot Personalization / Memory System Design
41. Distributed Web Crawler System Design
42. Search Autocomplete / Typeahead System Design
43. Job Scheduler / ETL Pipeline System Design
44. URL Shortener / Tiny URL System Design
45. Ticket Booking / Ticketmaster System Design
46. Behavioral Round (Growth Mindset / Customer Obsession)
47. Project Deep Dive
48. Object-Oriented Design Round
49. Transformer Roofline + FP32→FP16 Drill

---

## 1. LRU Cache (LC 146) + Multithreading Variant

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: linked-list, hashmap, data-structure, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/lru-cache
- **related discussions**: 6

### Statement

Microsoft's most repeated cache implementation prompt: O(1) get/put on a bounded cache, with follow-ups spanning thread safety, TTL, LFU, and cross-machine sharding.

---

## 2. In-Memory SQL Engine

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: data-structure, parsing, hard, sql, in-memory-database, state-machine
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/in-memory-sql-engine
- **related discussions**: 5

### Statement

Build a single-table in-memory database supporting SELECT, WHERE, and ORDER BY in 4-5 escalating follow-ups. CSV string is the initialization input; you write the parser too.

---

## 3. DNA Shotgun Sequencing

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-28
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: MLE, SWE
- **tags**: graph, hard, string-processing, dfs
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/dna-shotgun-sequencing
- **related discussions**: 5

### Statement

Reconstruct a DNA string from fragments tagged at both ends. Three-part: ordered chain → undirected chain (Eulerian path) → multi-chain decomposition. The signature MAI "platform" coding problem.

---

## 4. Delete and Earn (LC 740)

- **category**: coding
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/delete-and-earn
- **related discussions**: 1

### Statement

Maximize points by choosing values from an integer array: taking a value earns its points and removes values one smaller or one larger. The OA prompt “Maximize Score” is unchanged LC 740.

---

## 5. Count Binary Substrings (LC 696)

- **category**: coding
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: binary-string, easy
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/count-binary-substrings
- **related discussions**: 1

### Statement

Count contiguous binary substrings with equal numbers of zeros and ones, with each digit grouped consecutively. The intern OA uses the unchanged LC 696 problem under a renamed prompt.

---

## 6. Alien Dictionary (LC 269)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, topological-sort, cycle-detection, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/alien-dictionary
- **related discussions**: 1

### Statement

The live-coding prompt was confirmed as LC 269: infer a valid character order from words already sorted in an alien language, return any valid ordering, and return an empty string when the ordering is invalid.

---

## 7. Multi-Key Bounded Task Executor

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-08-20
- **stage**: phone-screen
- **access**: Members
- **roles**: Infra Eng, SWE
- **tags**: concurrency, threading, scheduling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/multi-key-bounded-task-executor
- **related discussions**: 1

### Statement

Implement a thread-safe task executor that preserves submission order within each key, runs different keys concurrently under a global limit, bounds queued plus in-flight work, and shuts down gracefully with a timeout.

---

## 8. Spiral Matrix (LC 54) + Follow-Ups

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, grid, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/spiral-matrix-traversal
- **related discussions**: 1

### Statement

Traverse a 2-D matrix in clockwise spiral order and return the values as a one-dimensional sequence. Follow-ups switch to counterclockwise traversal and to filling an n × n matrix in spiral order.

---

## 9. Binary Tree Zigzag Level Order Traversal (LC 103)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, bfs, queue, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/binary-tree-zigzag-level-order
- **related discussions**: 1

### Statement

Return a binary tree's values level by level, alternating left-to-right and right-to-left order on successive levels. The follow-up asks for a formulation using two alternating stacks.

---

## 10. Palindrome Linked List (LC 234) + Engineering Follow-Up

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, two-pointer, palindromes, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/palindrome-linked-list
- **related discussions**: 1

### Statement

Determine whether a singly linked list is a palindrome using O(1) auxiliary space. The follow-up requires restoring the list to its original structure before returning.

---

## 11. Lowest Common Ancestor (LC 235) with a Follow-Up Variant

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: tree, bst, dfs, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/lowest-common-ancestor-bst-binary-tree
- **related discussions**: 1

### Statement

Start with LC 235 and find the lowest common ancestor of two nodes in a binary search tree. The follow-up removes the BST ordering guarantee and becomes the general binary-tree problem in LC 236.

---

## 12. Contiguous Memory Allocator

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-21
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, SWE
- **tags**: linked-list, data-structure, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/memory-allocator
- **related discussions**: 1

### Statement

MAI / Copilot phone-screen coding: implement a fixed-size contiguous memory pool supporting allocate-by-size and free-a-region, then defend your allocation strategy.

---

## 13. Tiny Next-Token Prediction Model (PyTorch Fill-In)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-25
- **stage**: phone-screen · onsite-research
- **access**: Members
- **roles**: MLE, RS, RE
- **tags**: pytorch, transformer, medium, ml-knowledge
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/tiny-next-token-pytorch
- **related discussions**: 1

### Statement

MSR Senior Researcher take-home-style live round: fill in TODOs in a tiny PyTorch repo — DataLoader, model layers, forward, training loop, next-token input/target preparation.

---

## 14. SFT Sample Packing

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: pytorch, data-engineering, batching, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/sft-sample-packing
- **related discussions**: 1

### Statement

Implement supervised fine-tuning sample packing: greedily pack variable-length (prompt, answer) pairs into fixed-length training sequences, emit per-token loss masks aligned to answer positions only, and balance sequence usage across the batch.

---

## 15. Sliding-Window Stream Session Bucketing

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, hashmap, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/sliding-window-stream-bucketing
- **related discussions**: 1

### Statement

Azure phone screen. Given a stream of timestamped events, return the (min, max) of each fixed-width time bucket in a single O(N) pass.

---

## 16. In-Place Sub-Matrix Move

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-07
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/in-place-submatrix-move
- **related discussions**: 1

### Statement

Given a large matrix and the coordinates of a smaller embedded sub-matrix, move the sub-matrix to a new location within the larger matrix — in-place — handling overlap correctly.

---

## 17. KV Store with Snapshot Versions

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: data-structure, snapshot, mvcc, versioning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/kv-store-with-snapshot
- **related discussions**: 1

### Statement

Build an in-memory KV store that supports point-in-time snapshots, reads scoped to a snapshot id, and writes that branch from a snapshot without polluting earlier snapshots.

---

## 18. Beam Search Decoder Implementation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: beam-search, heap, llm, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/beam-search-decoder
- **related discussions**: 1

### Statement

Implement beam search decoding for a language model — greedy first, then top-K beam expansion. The neural network is stubbed; you only own the decoding logic and the beam bookkeeping.

---

## 19. Gen / Score Task Lock Scheduler

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, Infra Eng
- **tags**: concurrency, threading, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/gen-score-task-lock-scheduler
- **related discussions**: 1

### Statement

Implement a `TaskLock` so that a pool of `gen` and `score` tasks executes in parallel but `score` tasks of a given id only run after their matching `gen` task completes. Pure Python threading.

---

## 20. Multiply Strings (LC 43)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, string-processing, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/multiply-strings
- **related discussions**: 1

### Statement

Classic big-integer multiplication on string inputs without using built-in big-int types. HE coding round; complexity follow-up is expected.

---

## 21. Parse Email Addresses from a Log String

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: parsing, state-machine, string-processing, hard, dedup
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/parse-emails-state-machine
- **related discussions**: 1

### Statement

Single-pass character scanner over a long log string with `;`-separated email records. Each record may have an optional display name and comments anywhere; deduplicate and return the list of emails.

---

## 22. 15-Puzzle Minimum Moves (BFS)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bfs, graph, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/15-puzzle-bfs
- **related discussions**: 1

### Statement

Given a 4×4 sliding tile puzzle state, return the minimum number of moves to reach the solved configuration (or `-1` if unreachable). Pure BFS on a state graph.

---

## 23. Idempotency API with Idempotency-Key Header

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: idempotency, concurrency, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/idempotency-api-with-key
- **related discussions**: 1

### Statement

HE round opener: design and implement an API endpoint that deduplicates retries via an `Idempotency-Key` header. The interviewer hands you a spec sheet and watches whether you cover replay semantics, concurrent in-flight retries, and TTL eviction.

---

## 24. Distance from Each Node to the Cycle in an Undirected Graph

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, bfs, topological-sort, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/distance-to-cycle-in-undirected-graph
- **related discussions**: 1

### Statement

Given an undirected graph that contains exactly one cycle, return for every node its shortest distance to any node on the cycle. Cycle nodes return 0.

---

## 25. Sort 0..32000 with Bit-Vector Storage

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-11-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bit-manipulation, sorting, io, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/bit-vector-sort-0-to-32000
- **related discussions**: 1

### Statement

Classic Bell Labs / Bentley problem revived for an Azure storage HM round. Sort an unsorted, duplicate-free integer file with values in [0, 32000] using only `getNum()` / `putNum()` helpers — and minimal memory.

---

## 26. Validate Lat/Long With Minimum Expensive API Calls

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: algorithm-design, hashmap, dedup, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/geocode-api-call-minimization
- **related discussions**: 1

### Statement

Process a multi-million-row CSV of store locations; for each row, verify that the stored (lat, long) matches the geocoded version of its address. The geocoding API is expensive — minimize the number of calls.

---

## 27. Maximum-Value Frequency in Array Suffixes

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/max-number-occurrences-in-suffix
- **related discussions**: 2

### Statement

Given `nums` and query indices, return for each index how many times the maximum value of that suffix appears. Two independent MAI interviews used the same example and contract.

---

## 28. Merge Intervals (LC 56) + Interview Variant

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, interval, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/merge-intervals
- **related discussions**: 3

### Statement

The canonical prompt is LC 56: sort intervals by start and merge overlaps into disjoint ranges. A Hiring Event variant instead asks for the original intervals that participate in any overlap, so clarify the expected output before coding.

---

## 29. Minimum Fuel Cost Across a Road Network

- **category**: coding
- **duration**: 90 min
- **frequency**: Low · New
- **last asked**: 2026-08-11
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: graph, shortest-path, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/minimum-fuel-cost-road-network
- **related discussions**: 2

### Statement

In a 90-minute Microsoft OA, minimize the cost to travel from A to B in an undirected weighted road network where each city sells fuel at a different price and vehicles have unlimited capacity. Return -1 when B is unreachable; the prompt supplies a complete getMinCost contract and a five-city example.

---

## 30. Top-K Largest Elements (Retain / Rank Stores)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-04
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, SWE
- **tags**: heap, top-k, sorting, easy
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/top-k-largest-retain
- **related discussions**: 3

### Statement

The recurring MAI phone-screen coding warm-up: return the top-K elements of a list under a simple ranking rule. It appears as direct top-K selection, retaining the K largest in original order, or ranking business candidates by a composite key.

---

## 31. Org Chart Report Count (Tree Aggregation)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, tree-broadcast, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/org-chart-report-count
- **related discussions**: 2

### Statement

Given a manager → reports graph, answer `countReports(id)` quickly under read-heavy workloads. Follow-up: how to update the aggregate when a single relationship changes.

---

## 32. Balanced Permutation Prefix Check

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy, medium, oa
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/balanced-permutation-prefix
- **related discussions**: 2

### Statement

OA prompt also seen on Uber. For each k in [1..n], decide whether some contiguous subarray of permutation p contains exactly the multiset {1..k}. Return a binary string.

---

## 33. Count Positive Pairs Satisfying 1/x + 1/y = 1/N

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: math, medium, oa
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/equations-reciprocal-pair-count
- **related discussions**: 2

### Statement

HackerRank OA staple branded "Equations". Given N ≤ 10⁶, count ordered positive-integer pairs (x, y) with 1/x + 1/y = 1/N. The naive scan TLEs; the right answer reduces to a divisor count.

---

## 34. Resumable Batch Dataloader with Weighted Sampling

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: data-structure, iterator, checkpointing, sampling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/resumable-batch-dataloader
- **related discussions**: 2

### Statement

Build an iterator over a multi-source dataset that supports checkpointable resume, mixed batching by source weight, and weights that don't divide the batch size cleanly.

---

## 35. Streaming Stop-Token Detection

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: trie, string-processing, streaming, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/streaming-stop-token-detection
- **related discussions**: 3

### Statement

Decode an LLM token stream and stop emission the instant a forbidden stop sequence appears — including when the stop sequence straddles chunk boundaries. No full-stream buffering allowed.

---

## 36. Hand-Written K-Means

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-02-13
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, RS
- **tags**: ml-knowledge, numpy, pytorch, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/handwritten-k-means
- **related discussions**: 2

### Statement

Implement K-Means from scratch in numpy / PyTorch / plain Python. The HM phone screen for Applied Scientist 2 routinely opens with this; sometimes followed by a time-complexity discussion.

---

## 37. Palindrome Number + Next-Greater Palindrome

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: math, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/palindrome-number-and-next-palindrome
- **related discussions**: 2

### Statement

Two-part HE coding round. First: detect whether an integer is a palindrome without converting to a string. Second: find the smallest palindrome strictly greater than the input.

---

## 38. Rate Limiter (Design + Implementation)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-21
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: rate-limiting, redis, distributed-systems, throttling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/rate-limiter-design-and-implementation
- **related discussions**: 5

### Statement

Recurring Microsoft prompt that crosses categories. Some loops ask for a 30-minute coding implementation, others for a full system-design treatment, and one HE pairs both in the same round. Scale follow-up converges on 100K QPS.

---

## 39. Todo List Full-Stack System Design

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-06-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: fullstack, backend, access-control, rate-limiting, caching, versioning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/todo-list-full-stack-system-design
- **related discussions**: 1

### Statement

Design a todo-list application with create-list, task CRUD, viewing, ordering, authentication / authorization, cache behavior, API versioning, service-to-service auth, and a 500 requests-per-second user-load follow-up.

---

## 40. Chatbot Personalization / Memory System Design

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, SWE
- **tags**: rag, retrieval, llm, mlsd, personalization
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/chatbot-personalization-memory-design
- **related discussions**: 1

### Statement

MAI / Copilot onsite system design: design the long-term memory and personalization layer behind a text chatbot — how to store, retrieve, and reconcile per-user history so responses are personalized without re-reading everything each turn.

---

## 41. Distributed Web Crawler System Design

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: distributed-systems, messaging, storage, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/web-crawler-system-design
- **related discussions**: 1

### Statement

MAI phone-screen SD slot. 30-minute compressed treatment — get the pipeline shape, politeness, and dedup right before time runs out.

---

## 42. Search Autocomplete / Typeahead System Design

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: trie, search, caching, scaling, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/search-autocomplete-typeahead
- **related discussions**: 1

### Statement

MAI VO SD slot. Design a low-latency prefix-search service that powers a search bar's typeahead suggestions.

---

## 43. Job Scheduler / ETL Pipeline System Design

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-26
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, scheduling, etl, messaging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/job-scheduler-etl-pipeline
- **related discussions**: 1

### Statement

HE SD slot. Design a system that schedules and executes long-running ETL jobs reliably, with retry and dependency semantics.

---

## 44. URL Shortener / Tiny URL System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-29
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, caching, sharding, scaling, redis, medium, design-implementation
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/url-shortener-system-design
- **related discussions**: 3

### Statement

Recurring HE / VO / phone-screen prompt. Variants now include vanilla HLD, a higher-scale write-spike design, and a compressed HLD + LLD round where working HackerRank code and AI-generated-code bug review are expected.

---

## 45. Ticket Booking / Ticketmaster System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-20
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-lock, redis, concurrency, scaling, caching, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/ticket-booking-system-design
- **related discussions**: 2

### Statement

Azure / Office VO SD prompt. The core question is seat-locking under concurrency for high-traffic event drops.

---

## 46. Behavioral Round (Growth Mindset / Customer Obsession)

- **category**: behavioral
- **duration**: 15 min
- **frequency**: Very high
- **last asked**: 2026-08-25
- **stage**: onsite-bq · phone-screen · onsite-coding · final-round
- **access**: Members
- **roles**: All
- **tags**: behavioral, star, conflict, leadership, ai-collaboration, culture-fit
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/behavioral-round
- **related discussions**: 17

### Statement

Behavioral questions appear inside coding rounds and in standalone HM or AA rounds. Microsoft's value-driven prompts cluster around growth mindset, collaboration, ownership, judgment, and technical decision-making.

---

## 47. Project Deep Dive

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: onsite-deep-dive · onsite-bq · phone-screen · tech-screen
- **access**: Members
- **roles**: SWE, MLE, RS
- **tags**: deep-dive, behavioral, leadership, impact
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/project-deep-dive
- **related discussions**: 15

### Statement

Mandatory across MAI and most traditional loops. 30-60 minutes of unstructured drilling on one project the candidate picks. Interviewers probe ownership, decision rationale, and what would change in hindsight.

---

## 48. Object-Oriented Design Round

- **category**: other
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-06-30
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, ood, medium
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/object-oriented-design-round
- **related discussions**: 4

### Statement

Specifically a Microsoft staple. Some Azure loops replace one coding round with an explicit OOD prompt — design class hierarchies, interfaces, and inheritance relationships for a small system in code.

---

## 49. Transformer Roofline + FP32→FP16 Drill

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-22
- **stage**: onsite-coding · onsite-deep-dive
- **access**: Members
- **roles**: Infra Eng, Perf Eng, MLE
- **tags**: transformer, gpu, kernel, arithmetic-intensity, numerical-stability, ai-coding, hard
- **url**: https://www.1point3acres.com/interview/problems/company/microsoft/transformer-roofline-fp-precision
- **related discussions**: 1

### Statement

MAI AI-infra loop: read transformer code, compute matmul wall-clock from roofline, vibe-code CUDA in your own editor, then defend the residual-connection gradient path under FP32→FP16 casting.

---
