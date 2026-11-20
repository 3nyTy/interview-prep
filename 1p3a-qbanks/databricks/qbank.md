# Databricks — 1point3acres Question Bank (35 curated questions)

Source: 1point3acres qbank (`company=databricks`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/databricks>

The site lists 159 entries for this company: **35 curated questions** (below), 88 OJ links, 36 external links. Only the curated 35 are captured here.

> Databricks interviews are unusually bank-driven: candidates who know the recurring prompt families often recognize the round within the first few minutes. The loop mixes algorithmic coding with low-level systems and product-grounded design, then puts real weight on behavioral, hiring-committee, and reference signals after the technical rounds.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Optimal Commute Grid
2. Multi-Threaded Log Writer
3. IP / CIDR Firewall
4. KV Store With Rolling QPS
5. RLE + Bit-Packing Encoder / Decoder
6. File Encryption Tree Optimization
7. Generalized Tic-Tac-Toe
8. Snapshot Set / MVCC Iterator
9. Fibonacci Tree Path
10. Lazy Array
11. Implement Circuit Breaker Pattern
12. String Pattern Partition
13. SQL Query Plan Optimizer
14. Maximal Square and Rectangle in Binary Matrix
15. House Robber Series
16. Multi-Threaded Chat System
17. Revenue Tracking System with Referrals
18. Anagrammed indexOf
19. Cipher Cover Interval Editing
20. Uniformly Connect Node Groups
21. Linear Regression Gradient Descent
22. Book Seller External Fan-Out
23. Chat system similar to Slack
24. In-Memory KV Cache With WAL
25. Distributed File System
26. Stock Trading Agent Service
27. Job Scheduler With Dependencies
28. Design a Network Throttling System
29. Design an Ad Marketplace System
30. Payment Gateway System
31. Design an Autonomous Vehicle Ride-Hailing App (Waymo-like)
32. Harmful Content Detection ML Design
33. Collaborative Playlist Design
34. SWE / MLE Behavioral Round
35. Senior FDE Recruiter Screen

---

## 1. Optimal Commute Grid

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-31
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, dijkstra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/optimal-commute-grid
- **related discussions**: 13

### Statement

Find the fastest transportation mode from `S` to `D` in a grid where each cell is a mode, `X` is blocked, and each mode has per-block time and cost. Tie-break on total cost; follow-ups allow turns or mode switching with penalties.

---

## 2. Multi-Threaded Log Writer

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-13
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: io, concurrency, threading, persistence, hard
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/multi-threaded-log-writer
- **related discussions**: 13

### Statement

Design and partially implement a durable event writer shared by thousands of application threads on one server. Each opaque payload is at most 1 KB; follow-ups center on batching, `fsync`, group commit, ordering, and crash recovery.

---

## 3. IP / CIDR Firewall

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-11
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, Infra Eng
- **tags**: bit-manipulation, interval, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/ip-cidr-firewall
- **related discussions**: 10

### Statement

Given an ordered list of IP or CIDR allow / deny rules, return the first matching rule for an input IP. Follow-ups switch the input from a single IP to a CIDR block and ask whether the entire range is allowed after applying ordered rules.

---

## 4. KV Store With Rolling QPS

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-07
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: in-memory-database, sliding-window, rate-limiting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/kv-store-rolling-qps
- **related discussions**: 14

### Statement

Implement an in-memory key-value store with `put` / `get` / sometimes `delete`, plus APIs that return average put/get load or QPS over a rolling time window such as the last five minutes.

---

## 5. RLE + Bit-Packing Encoder / Decoder

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-13
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: compression, iterator, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/rle-bit-packing-encoder-decoder
- **related discussions**: 7

### Statement

Implement a streaming integer encoder that chooses between run-length encoding and bit-packing, then optionally implement an iterator-style decoder. Edge cases include negative values and integer limits.

---

## 6. File Encryption Tree Optimization

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-08-11
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: tree, dp, filesystem, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/file-encryption-tree-optimization
- **related discussions**: 4

### Statement

Traverse a file-system tree to count encrypted vs unencrypted files, then choose the minimum-cost set of file-level or directory-level encryption calls.

---

## 7. Generalized Tic-Tac-Toe

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-07
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, grid, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/tic-tac-toe-generalized
- **related discussions**: 7

### Statement

Implement tic-tac-toe with custom board dimensions and a custom win length. One intern variant adds a simple AI move by calling a random API.

---

## 8. Snapshot Set / MVCC Iterator

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-07-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: snapshot, mvcc, iterator, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/snapshot-set-iterator
- **related discussions**: 6

### Statement

Implement a set or key-value structure with snapshot reads. Values retain version history by snapshot id, and an iterator returns a consistent snapshot view.

---

## 9. Fibonacci Tree Path

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-07-07
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, recursion, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/fibonacci-tree-path
- **related discussions**: 7

### Statement

Given a Fibonacci tree order `k` and two preorder node labels `a` and `b`, return the path between those nodes without constructing the exponentially large tree.

---

## 10. Lazy Array

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: iterator, object-design, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/lazy-array
- **related discussions**: 4

### Statement

Implement a lazy array abstraction where transformations are recorded and only evaluated when values are read or iterated. Interviewers often focus on how to test laziness, not just functional output.

---

## 11. Implement Circuit Breaker Pattern

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: circuit-breaker, state-machine, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/circuit-breaker-pattern
- **related discussions**: 1

### Statement

Design and implement circuit breakers for a database topology with one primary and two secondary replicas. Each server has its own breaker; two consecutive failures move it from CLOSED to OPEN, and requests must fall back to a replica when the primary is unavailable. The operation result is supplied through a lambda-style callback.

---

## 12. String Pattern Partition

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, dp, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/string-pattern-partition
- **related discussions**: 1

### Statement

Given two strings, represent the target string as a minimum sequence of substrings indexed from the source string. A follow-up deletes one character and asks for the new minimum partition.

---

## 13. SQL Query Plan Optimizer

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: sql, query-optimization, tree, hard
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/sql-query-plan-optimizer
- **related discussions**: 1

### Statement

Given a node-based representation of a SQL query plan, implement optimizer rewrites such as predicate pushdown and make the transformed plan runnable.

---

## 14. Maximal Square and Rectangle in Binary Matrix

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, grid, monotonic-stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/maximal-square-and-rectangle

### Statement

Given a binary matrix of 0s and 1s, find the largest all-1s square and return its area, then extend to the largest all-1s rectangle.

---

## 15. House Robber Series

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-18
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/house-robber-series

### Statement

The House Robber DP family: maximize the sum of values from non-adjacent houses, with follow-ups such as a circular street and a binary-tree variant.

---

## 16. Multi-Threaded Chat System

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-10-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: concurrency, threading, chat, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/multi-threaded-chat-system

### Statement

Design and implement a thread-safe, single-machine chat system where multiple clients communicate through channels.

---

## 17. Revenue Tracking System with Referrals

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-24
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: object-design, design-implementation, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/revenue-tracking-with-referrals
- **related discussions**: 3

### Statement

Design and implement an in-memory revenue-tracking system that records transactions and supports customer referrals, attributing referred revenue.

---

## 18. Anagrammed indexOf

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, sliding-window, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/anagrammed-indexof
- **related discussions**: 2

### Statement

Find the first index in a string where a substring that is an anagram (any permutation) of a query string occurs. Follow-ups probe character counting for UTF-8 / Unicode inputs, edge-case tests, and invalid-input or error-return behavior.

---

## 19. Cipher Cover Interval Editing

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, interval-aggregation, binary-search, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/cipher-cover-interval-editing
- **related discussions**: 2

### Statement

Given covers — a list of (start, end) intervals into a source string (the 'cipher book') — delete the element at a given flattened index across the covered text. The deletion can split an interval; a follow-up asks to return the simplified cover with all mergeable intervals merged.

---

## 20. Uniformly Connect Node Groups

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-05-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, sampling, math, hard
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/randomly-connect-node-groups
- **related discussions**: 2

### Statement

Given several connected components, return a random minimum-size edge set that connects all groups into one connected graph, with the edge set sampled uniformly from all valid solutions.

---

## 21. Linear Regression Gradient Descent

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: linear-regression, gradient-descent, numpy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/linear-regression-gradient-descent
- **related discussions**: 2

### Statement

Hand-code gradient descent for simple linear regression using MSE loss, then discuss learning rate, early stopping, convergence bugs, and project / ML-design follow-ups.

---

## 22. Book Seller External Fan-Out

- **category**: system-design
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-13
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, fan-out, caching, idempotency, circuit-breaker, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/book-seller-external-fanout
- **related discussions**: 9

### Statement

Design a broker that fans out an ISBN, bid price, and payment method to partner bookstores, selects the lowest available price, and orders or returns the best quote. An alternate storefront form covers browse/search, inventory, checkout, payment, and order management within the marketplace itself.

---

## 23. Chat system similar to Slack

- **category**: system-design
- **frequency**: Medium
- **last asked**: 2026-07-30
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: chat, messaging, system-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/slack-chat-system
- **related discussions**: 5

### Statement

Design a Slack-like chat system from APIs through storage. Deep dives include message and conversation deletion for online and offline users, cache placement and partitioning, cache-miss behavior, and application-memory versus shared-cache trade-offs.

---

## 24. In-Memory KV Cache With WAL

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-28
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: in-memory-database, persistence, concurrency, caching, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/in-memory-kv-cache-with-wal
- **related discussions**: 4

### Statement

Design a single-machine in-memory cache for a web service, then add persistence with a write-ahead log and discuss scaling via sharding and lock-contention reduction.

---

## 25. Distributed File System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-15
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: distributed-systems, filesystem, storage, data-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/distributed-file-system
- **related discussions**: 1

### Statement

Design the metadata layer of a distributed file system supporting create-directory, list, put / get file, delete, and a recursive hierarchy. The defining requirement is strong consistency: a list must immediately reflect prior puts (unlike eventually-consistent S3). Blob storage is assumed to exist.

---

## 26. Stock Trading Agent Service

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-04-06
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: trading, api-integration, idempotency, queueing, circuit-breaker, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/stock-trading-agent
- **related discussions**: 1

### Statement

Design an intermediary service between clients and a third-party broker API. Clients submit buy/sell orders with a deadline; the service forwards them, polls status until filled, and auto-cancels unfilled orders at the deadline. The crux is tracking thousands of deadlines and absorbing spikes when many orders expire at once.

---

## 27. Job Scheduler With Dependencies

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-02
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: scheduling, topological-sort, queueing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/job-scheduler-with-dependencies
- **related discussions**: 1

### Statement

Design a low-level scheduler where each job contains tasks with dependencies. The interviewer expects task-state modeling, dependency execution order, retries, and worker scheduling.

---

## 28. Design a Network Throttling System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-11-20
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: rate-limiting, throttling, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/network-throttling-system

### Statement

Design a throttling system for serving infrastructure handling both internal and external users, covering per-user and global rate limits, fairness, and overload protection.

---

## 29. Design an Ad Marketplace System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-11-05
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: ads, scaling, system-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/ad-marketplace-system

### Statement

Design a scalable ad marketplace connecting publisher websites with ad providers, covering bidding, matching, serving, and budget accounting.

---

## 30. Payment Gateway System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-10-30
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: payment, idempotency, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/payment-gateway-system

### Statement

Design a payment gateway supporting multiple credit and bank card types, covering authorization, capture, retries, idempotency, and failure handling.

---

## 31. Design an Autonomous Vehicle Ride-Hailing App (Waymo-like)

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-10-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: product-design, location, system-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/waymo-ride-hailing-app

### Statement

Design a user-facing autonomous-vehicle ride-hailing app (Waymo-like): request a ride, match a vehicle, track the trip, and handle lifecycle and edge cases.

---

## 32. Harmful Content Detection ML Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, moderation, ai-safety, evaluation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/harmful-content-detection-ml-design
- **related discussions**: 2

### Statement

Design an ML system to detect harmful LLM content. The LLM-focused form is broader than social-media moderation and expects both classical integrity considerations and LLM-specific concerns across data, evaluation, deployment, and policy trade-offs.

---

## 33. Collaborative Playlist Design

- **category**: system-design
- **frequency**: Low · New
- **last asked**: 2026-07-30
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: frontend, data-modeling, api-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/collaborative-playlist-design
- **related discussions**: 3

### Statement

Design a shared playlist app — add, remove, and reorder songs — that multiple users edit collaboratively. Distributed scale is explicitly out of scope: the round is scored on data modeling for the ordered song list and on API / event design for propagating one user's edits to the others. The same prompt covers both the frontend screen and the onsite frontend system-design round.

---

## 34. SWE / MLE Behavioral Round

- **category**: behavioral
- **frequency**: Medium · New
- **last asked**: 2026-08-13
- **stage**: onsite-bq
- **access**: Members
- **roles**: SWE, MLE
- **tags**: why-company, deep-dive, conflict, culture-fit
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/swe-behavioral-round
- **related discussions**: 5

### Statement

Prepare for motivation, proud-project impact, project delays, difficult team pain points, feedback or technical disagreements, and cross-functional collaboration. Answers should make individual ownership, judgment, and measurable outcomes explicit.

---

## 35. Senior FDE Recruiter Screen

- **category**: behavioral
- **frequency**: Single report
- **last asked**: 2026-07-21
- **stage**: recruiter-screen
- **access**: Members
- **roles**: SWE
- **tags**: why-company, fit, culture-fit, verbal
- **url**: https://www.1point3acres.com/interview/problems/company/databricks/senior-fde-recruiter-screen
- **related discussions**: 1

### Statement

The recruiter asks for the candidate's exact city, why they applied to the FDE role, why their background fits it, and how well they understand Databricks before opening the call for candidate questions.

---
