# Oracle — 1point3acres Question Bank (47 curated questions)

Source: 1point3acres qbank (`company=oracle`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/oracle>

The site lists 110 entries for this company: **47 curated questions** (below), 63 OJ links, 0 external links. Only the curated 47 are captured here.

> Oracle's loops split along two clear axes: OCI (Cloud Infrastructure) runs a fairly standard LC + system-design loop with heavy Indian interviewer rotation, while OHAI (Oracle Health, the post-Cerner business unit) leans into OOD/class-design problems built around healthcare scenarios. Coding bars are mostly easy-to-medium, but interviewer variance and post-loop down-leveling / ghosting are unusually common — the loop result often determines the eventual level, not just the yes/no.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Token Expiration Service
2. Accounts Merge (LC 721)
3. Two-Value Exchange Warm-Up
4. Run-Length String Compression
5. REST API: Medical Records by Age
6. Sort Array by Parity — Minimum Swaps
7. Monotone Increasing Digits (LC 738)
8. Hospital Appointment Booking API
9. Simplified Redis-Like KV / List Store
10. Apply List of Operations (Command Pattern)
11. Code Reading — CLI Tool Feature Audit
12. ML Coding — Handwritten MHA and Sparse Matmul
13. Container With Most Water (LC 11)
14. First-Login Only-Once User Tracker (O(1) Worst-Case)
15. Anagram Word Replacement in Phrases
16. Merge K Sorted (Key, Value) Lists with Later Override
17. Course Scheduler — Print Path (Simplified)
18. Valid 5-Card Poker Straight
19. OOD — File Management System
20. Trie-Based Autocomplete
21. OOD — Treatment Frequency Scheduler
22. Set Matrix Zeros (in-place)
23. Minimum Sum After K Halvings (Max-Heap)
24. Binary Search — Rightmost Index of Duplicate
25. Simplify Expression by Removing Parentheses
26. OOD — Body-Temperature Measurement Classes
27. Spiral Matrix (LC 54)
28. LRU Cache with Expiration (LC 146 + TTL)
29. Number of Islands (LC 200)
30. Time-Based Key-Value Store (LC 981)
31. Best Time to Buy and Sell Stock — Single Transaction
32. Maximum Completable Tasks with Prerequisites (Topological)
33. Decode String (k[encoded])
34. Same N-ary Tree Comparison
35. Log Parser with Multi-Line Follow-up
36. System Design — GPU Health and Repair
37. System Design — Healthcare Data Ingestion Pipeline
38. System Design — Video Upload + Search with LLM
39. System Design — Event Ingestion + Top-K Aggregation
40. System Design — Rate Limiter
41. System Design — VM Health Monitoring at 1M Scale
42. Behavioral & Bar Raiser BQ Set
43. Verbal Java / CS-Fundamentals Quiz
44. Resume / Project Deep Dive (HM or Tech Screen Opener)
45. Data-Center Camera Access Incident Response
46. Evaluate Subjective, Nondeterministic Agent Outputs
47. Code Review Round (Verbal)

---

## 1. Token Expiration Service

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: ttl, api-design
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/token-expiration-service
- **related discussions**: 1

### Statement

Implement a service that generates and renews token expiration times and counts tokens that remain unexpired at a supplied time.

---

## 2. Accounts Merge (LC 721)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/accounts-merge-lc721
- **related discussions**: 1

### Statement

Implement the original LC 721 Accounts Merge prompt: merge account records that share an email address and return each person's name with the sorted set of emails in the connected group.

---

## 3. Two-Value Exchange Warm-Up

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: math, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/swap-two-values-without-temp
- **related discussions**: 1

### Statement

Start with a = 30 and b = 40, then swap their values without using a third variable.

---

## 4. Run-Length String Compression

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, compression, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/run-length-string-compression
- **related discussions**: 1

### Statement

Compress contiguous runs in a string from left to right: emit a character alone when its run length is one, otherwise emit the character followed by its count.

---

## 5. REST API: Medical Records by Age

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-31
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, http, json, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/rest-api-medical-records-by-age
- **related discussions**: 1

### Statement

Implement `getRecordsByAgeGroup` to retrieve every page from a JSON mock medical-records API, calculate each patient's age when a record was created, filter by an inclusive age range and a blood-pressure-difference threshold, and return matching record IDs in sorted order. The prompt conflicts on both the subtraction order and comparison direction for the pressure predicate, so clarify that condition before coding.

---

## 6. Sort Array by Parity — Minimum Swaps

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: two-pointer, array, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/sort-array-by-parity-min-swaps
- **related discussions**: 1

### Statement

Rearrange an integer array so every even precedes every odd using arbitrary swaps, and return the minimum number of swaps. A two-pointer sweep from both ends gives the optimal count.

---

## 7. Monotone Increasing Digits (LC 738)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/monotone-increasing-digits
- **related discussions**: 1

### Statement

Return the largest number not exceeding n whose digits are monotonically non-decreasing. The greedy fix is to find the first descending pair, drop that digit by one, and fill the rest with nines.

---

## 8. Hospital Appointment Booking API

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: design, data-structure, concurrency, mini-project, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/hospital-appointment-booking-api
- **related discussions**: 1

### Statement

Design and implement an appointment-booking REST API for a hospital with 1,000 doctors, each working 9 AM–5 PM in 15-minute slots. The API books the first available slot for a given doctor on a given date and must maintain state across calls. Asked as a coding-design hybrid round in an OCI virtual onsite.

---

## 9. Simplified Redis-Like KV / List Store

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-04
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: data-structure, in-memory-database, design, mini-project, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/simplified-redis-kv-store
- **related discussions**: 1

### Statement

Implement a simplified Redis-like data structure in Go supporting strings, lists, and partial list-removal semantics. Asked as the screening (pre-VO) coding problem for an OCI loop. Expiration was an explicit deferred follow-up.

---

## 10. Apply List of Operations (Command Pattern)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: oop-design, design, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/apply-operations-command-pattern
- **related discussions**: 1

### Statement

Given a list of operations and an initial state, apply all operations to derive the final state. Recognising and using the Command pattern is the explicit signal. Follow-up: identify cases where operations can be applied out of order or merged. Oracle Health AI IC4 onsite, second technical round.

---

## 11. Code Reading — CLI Tool Feature Audit

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: code-reading, refactoring, design-review, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/code-reading-cli-tool-refactor
- **related discussions**: 1

### Statement

Read an existing CLI-tool codebase against a written requirements list; identify missing features and propose optimisations. Asked as the first technical round in an Oracle Health AI IC4 onsite — not a traditional LeetCode round.

---

## 12. ML Coding — Handwritten MHA and Sparse Matmul

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, RS
- **tags**: pytorch, numpy, transformer, attention, linear-algebra, ml-knowledge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/ml-coding-mha-sparse-matmul
- **related discussions**: 1

### Statement

Two ML-coding problems in a 90-minute phone screen for Oracle's AI org: (1) hand-implement multi-head attention from scratch; (2) hand-implement sparse matrix multiplication. Both straightforward; the candidate flagged a mismatch between the senior-principal interviewer's title and demonstrated knowledge.

---

## 13. Container With Most Water (LC 11)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/container-with-most-water
- **related discussions**: 1

### Statement

Find the maximum-area container formed by two vertical lines from a non-negative integer array — explicit LeetCode 11. Asked verbatim in an OCI phone screen, paired with behavioural questions about approaching unfamiliar problems.

---

## 14. First-Login Only-Once User Tracker (O(1) Worst-Case)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: linked-list, hashmap, design, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/first-unique-login-tracker
- **related discussions**: 1

### Statement

Design a structure that records user logins and, on demand, returns the earliest-logged-in user whose login count is exactly one. Both operations must be O(1) worst-case. Asked as the coding portion of a difficult OCI phone screen where the interviewer iteratively rewrote the prompt.

---

## 15. Anagram Word Replacement in Phrases

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, hashing, sliding-window, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/anagram-phrase-replacement
- **related discussions**: 1

### Statement

Given a list of phrases and a list of allowed replacement words, find for each phrase a starting position where some contiguous substring (or a word within the phrase) is an anagram of any allowed word. Return all such positions. Asked on HackerRank in an Oracle Health phone screen; hidden test case timed out.

---

## 16. Merge K Sorted (Key, Value) Lists with Later Override

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-20
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: heap, linked-list, merge, top-k, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/merge-k-sorted-key-value-lists
- **related discussions**: 1

### Statement

Merge `k` linked lists, each holding `(key, value)` pairs sorted ascending by key. On key collision across lists, the later list (higher index) overrides the earlier list. Return one sorted output list. Asked in an OCI Consultant Engineer phone screen.

---

## 17. Course Scheduler — Print Path (Simplified)

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2025-11-19
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, topological-sort, tree, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/course-scheduler-simplified
- **related discussions**: 1

### Statement

A simplified course-scheduler variant: no multi-prerequisite courses, and the answer is the explicit ordered path of courses (not just a count). Asked as the 20-minute coding portion of an Oracle Health phone screen, paired with a job-scheduler design in the same round.

---

## 18. Valid 5-Card Poker Straight

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, set, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/valid-poker-straight
- **related discussions**: 1

### Statement

Given five cards drawn from a standard deck, determine whether they form a straight (five consecutive ranks). J/Q/K must be mapped to 11/12/13; Ace can play as either 1 or 14. Asked as a single 40-minute coding problem inside an Oracle Health phone screen heavy on behavioural questions.

---

## 19. OOD — File Management System

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-09-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, filesystem, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/ood-file-management-system
- **related discussions**: 1

### Statement

Implement a simplified in-memory file-management system on HackerRank, with a starter HashMap of folder-name to file-list provided. Required operations: `checkIfFolderExists`, `createFolder`, `createFile`. Edge-case handling matters more than algorithmic depth.

---

## 20. Trie-Based Autocomplete

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-09-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: trie, dfs, string, data-structure, design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/trie-autocomplete
- **related discussions**: 1

### Statement

Build an autocomplete service: given a prefix and a dictionary, return all strings that match the prefix. The interviewer pushes hard on maintainability — Trie and Solution classes must be separated, and the dictionary must initialise once in the constructor. Follow-up: rank results by past-query frequency.

---

## 21. OOD — Treatment Frequency Scheduler

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-09-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/ood-treatment-scheduler
- **related discussions**: 1

### Statement

Design classes that schedule patient treatments at varying frequencies within a date range, then enumerate every required treatment instance. Asked as the OHAI phone-screen problem with explicit allowance to write pseudo-code or comments for date arithmetic syntax.

---

## 22. Set Matrix Zeros (in-place)

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-09-11
- **stage**: phone-screen
- **access**: Free preview
- **roles**: SWE, Infra Eng
- **tags**: array, grid, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/set-matrix-zeros-inplace
- **related discussions**: 1

### Statement

Given a 2D integer matrix, set every cell sharing a row or column with a zero to zero, in place. Asked as the coding portion of an OCI IC4 phone screen with two follow-ups: handle ragged (non-rectangular) input, and generalise to char[][] with an arbitrary marker value.

---

## 23. Minimum Sum After K Halvings (Max-Heap)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-06
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: heap, greedy, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/min-sum-after-k-halvings
- **related discussions**: 1

### Statement

Given a non-negative integer array and an integer `k`, repeatedly pick one element and replace it with `ceil(element / 2)`. After at most `k` operations, minimise the remaining sum. Asked as the third VO round in an Oracle Health AI loop; the reporting candidate hit hidden-test-case timeouts and suspected the zero-handling edge case.

---

## 24. Binary Search — Rightmost Index of Duplicate

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-07-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: array, binary-search, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/binary-search-rightmost-duplicate
- **related discussions**: 1

### Statement

In a sorted array with duplicate values, find the rightmost (last) occurrence index of a target value using binary search. Asked as the coding portion of an OCI IC4 phone screen after a 30-minute resume walk-through.

---

## 25. Simplify Expression by Removing Parentheses

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-06-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: string, stack, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/simplify-parentheses-expression
- **related discussions**: 1

### Statement

Given an arithmetic expression containing variables, `+`, `-`, and parentheses, return an equivalent expression with all parentheses removed and signs propagated. Asked in the Oracle Labs AI/ML SDE3 onsite as one of two coding problems.

---

## 26. OOD — Body-Temperature Measurement Classes

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-01-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/ood-patient-temperature-classes
- **related discussions**: 1

### Statement

Design Java classes for storing and reasoning about patient body-temperature readings. The interviewer drives the requirements iteratively: measurement methods, unit handling, and method-specific fever thresholds emerge as the discussion deepens. No algorithm is required.

---

## 27. Spiral Matrix (LC 54)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/spiral-matrix-lc54
- **related discussions**: 1

### Statement

Traverse a rectangular matrix in clockwise spiral order. The onsite exercise included a test-case walkthrough and complexity analysis.

---

## 28. LRU Cache with Expiration (LC 146 + TTL)

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-09-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, linked-list, ttl, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/lru-cache-with-expiration
- **related discussions**: 2

### Statement

Implement an O(1) LRU cache with a hash map plus doubly linked list, then extend it with per-entry expiration so expired keys read as absent.

---

## 29. Number of Islands (LC 200)

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-08-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dfs, graph, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/number-of-islands
- **related discussions**: 2

### Statement

Count distinct islands in a 2D grid, then extend the traversal to track the maximum height within each connected island. State the time and space complexity after implementation.

---

## 30. Time-Based Key-Value Store (LC 981)

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-08-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, binary-search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/time-based-key-value-store-lc981
- **related discussions**: 2

### Statement

Implement the LC 981 time-based key-value store: record values for keys at timestamps and retrieve the value associated with the latest timestamp not greater than the query time. A recent IC4 screen used the prompt on HackerRank without requiring code execution.

---

## 31. Best Time to Buy and Sell Stock — Single Transaction

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/single-transaction-stock-profit
- **related discussions**: 2

### Statement

Given prices ordered over time, choose one buy followed by one sell to maximize profit. Only a single transaction is allowed.

---

## 32. Maximum Completable Tasks with Prerequisites (Topological)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-11
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: graph, topological-sort, bfs, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/course-prerequisites-task-count
- **related discussions**: 2

### Statement

Given tasks and prerequisite dependencies, return the maximum number of completable tasks when cycles make some tasks impossible. A later onsite used the ordered-output form as an LC 210 package-deployment reskin, so confirm whether the required result is a count or a deployment order.

---

## 33. Decode String (k[encoded])

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-11
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, stack, recursion, compression, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/decode-string-lc394
- **related discussions**: 2

### Statement

Decode a string of the form `k[encoded_string]`, where the bracketed segment repeats exactly `k` times and nesting is allowed. It has appeared as the LC 394 prompt in both an Oracle Health phone screen and a later onsite, where mandatory brackets and live approach changes were central traps.

---

## 34. Same N-ary Tree Comparison

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-04-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, recursion, easy
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/same-nary-tree-comparison
- **related discussions**: 2

### Statement

Decide whether two N-ary trees are identical in structure and values. The recursive solution is quick; the bar raiser's follow-up asks for an iterative version.

---

## 35. Log Parser with Multi-Line Follow-up

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2025-08-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: string, parsing, string-processing, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/log-parser-multiline
- **related discussions**: 2

### Statement

Parse a stream of log lines of the form `<unix_timestamp> <LEVEL> <message>` into a structured format and validate well-formedness. The recurring follow-up extends each log entry to span multiple physical lines until the next timestamp-prefixed line starts a new entry.

---

## 36. System Design — GPU Health and Repair

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-09-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: gpu, metrics, scaling, distributed-systems
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/system-design-gpu-health-repair
- **related discussions**: 1

### Statement

Design GPU health monitoring and repair triggering for 100,000 hosts. Explain GPU-specific signal collection, aggregate events into a host dashboard with pseudocode, and handle failed repair workflows.

---

## 37. System Design — Healthcare Data Ingestion Pipeline

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: ingestion, messaging, storage, scaling, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/system-design-healthcare-data-ingestion-pipeline
- **related discussions**: 1

### Statement

Design a healthcare data-ingestion pipeline that efficiently handles millions of records. Cover ingestion, queue or message-broker selection, storage, processing, fault tolerance, scalability, monitoring and alerting, and the handling of failed or unprocessed records.

---

## 38. System Design — Video Upload + Search with LLM

- **category**: system-design
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-02-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng, MLE
- **tags**: system-design, storage, ingestion, retrieval, rag, mlsd, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/system-design-video-upload-search
- **related discussions**: 1

### Statement

Design a video-upload + search system with an LLM / embeddings layer for semantic retrieval. The interviewer compressed the design into 30 minutes and drilled hard on video upload internals (chunking, presigned URLs, encoding) when the candidate steered toward the search side. OCI phone-screen.

---

## 39. System Design — Event Ingestion + Top-K Aggregation

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-01-15
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: system-design, streaming, top-k, scaling, distributed-systems, ingestion, kafka, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/system-design-event-ingestion-topk
- **related discussions**: 1

### Statement

An originally-single-server event-reporting system must scale to a much higher request volume, and must additionally compute top-K aggregations across the event stream. OHAI onsite SD round with a heavy emphasis on the scale-up narrative.

---

## 40. System Design — Rate Limiter

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: system-design, rate-limiting, throttling, caching, distributed-systems, redis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/system-design-rate-limiter
- **related discussions**: 2

### Statement

Design a rate limiter and compare the available algorithmic approaches. Cover caching, distributed implementation, scalability, and the trade-offs that determine which design to choose.

---

## 41. System Design — VM Health Monitoring at 1M Scale

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: system-design, distributed-systems, presence, scaling, infra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/system-design-vm-health-monitoring
- **related discussions**: 2

### Statement

Design centralized health monitoring for roughly one million VMs or edge devices. Compare pull-based pings with pushed heartbeats, identify each bottleneck, and justify a hybrid in which push is primary and pull verifies missed health signals.

---

## 42. Behavioral & Bar Raiser BQ Set

- **category**: behavioral
- **duration**: 45 min
- **frequency**: Very high
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE, Infra Eng, All
- **tags**: behavioral, bq, leadership, ownership, ambiguity, conflict, mentorship, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/behavioral-bq-bar-raiser
- **related discussions**: 22

### Statement

Oracle weighs behavioural rounds heavily — both as standalone bar-raiser / hiring-manager interviews and as 5-15 minute BQ openers folded into nearly every technical round. The recurring question bank is small enough to drill cold, and the bar raiser ("Bartender" / BT in OHAI) intentionally applies BQ-pressure follow-ups.

---

## 43. Verbal Java / CS-Fundamentals Quiz

- **category**: other
- **duration**: 30 min
- **frequency**: High
- **last asked**: 2026-08-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: cs-fundamentals, verbal, networking, operating-systems, domain-knowledge
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/verbal-java-cs-fundamentals
- **related discussions**: 8

### Statement

Verbal trivia rounds appear frequently inside Oracle coding screens. Common items include abstract class vs interface, HashMap collision handling, immutable objects, public-API concerns, networking fundamentals, and how to write unit tests and explain their benefits.

---

## 44. Resume / Project Deep Dive (HM or Tech Screen Opener)

- **category**: other
- **frequency**: Medium
- **last asked**: —
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: All
- **tags**: deep-dive, ownership
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/resume-project-deep-dive
- **related discussions**: 7

### Statement

Prepare two or three projects for a resume-led deep dive covering technical challenges, architecture choices, ownership, customer communication, on-call work, and questions about the team's business architecture.

---

## 45. Data-Center Camera Access Incident Response

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-08-11
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: security, infra, domain-knowledge
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/data-center-camera-access-incident-response
- **related discussions**: 1

### Statement

A data-center security-camera password or access failure has occurred. Explain the response within the first hour, within 24 hours, and within one week.

---

## 46. Evaluate Subjective, Nondeterministic Agent Outputs

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-07-18
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: llm-agent, evaluation, ml-knowledge, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/evaluate-subjective-agent-outputs
- **related discussions**: 1

### Statement

Design an evaluation approach for an agent whose outputs are subjective and nondeterministic. The interviewer challenges LLM-as-judge and user-feedback proposals with evaluator overconfidence, missing feedback, and developer bias, then asks how online user signals can close the gap.

---

## 47. Code Review Round (Verbal)

- **category**: other
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2025-10-24
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: code-review, design-review, verbal, deep-dive, medium
- **url**: https://www.1point3acres.com/interview/problems/company/oracle/code-review-verbal
- **related discussions**: 1

### Statement

An OCI phone screen that combines 20 minutes of verbal code review (read and critique a pre-written code sample) with a free-choice system-design discussion. A non-traditional screen format — the candidate picks the SD topic from their own past work.

---
