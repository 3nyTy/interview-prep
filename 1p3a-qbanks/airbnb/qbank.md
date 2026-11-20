# Airbnb — 1point3acres Question Bank (47 curated questions)

Source: 1point3acres qbank (`company=airbnb`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/airbnb>

The site lists 108 entries for this company: **47 curated questions** (below), 61 OJ links, 0 external links. Only the curated 47 are captured here.

> Airbnb's loop is famously consistent and famously slow. The coding pool is small and reused — a candidate who has drilled the canonical questions (split-stay, terrain water-drop, KV store, code review) walks in advantaged. Differentiation happens in the code-review round, the experience deep-dive, and the standalone core-values round, which is calibrated separately from the rest of the loop and serves as a real gate. Expect strong technical performance to land in a multi-week team-match queue rather than a fast offer.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Code Review — Multi-PR Walkthrough
2. In-Memory File System OA
3. Smallest Permutation ≥ Lower Bound
4. URL Query-String Parser
5. Review — Token Tagging / Replacement
6. Progressive Banking System OA
7. Text Justification — Print as Table
8. Split Stay — Two-Listing Date Coverage
9. In-Memory Object Manager OA
10. Task Management System OA
11. Recipe Management System OA
12. Parcel Tracking System
13. Nested-List Iterator
14. Boxes, Keys & Candies (OOD)
15. Property Combination for Group Size
16. Multi-Stream Round-Robin Iterator
17. In-Memory SQL Database
18. Implement Retryer
19. Working Hours Register
20. Chain Booking — Max Cascading Reservations
21. Layover Experiences — Exact-Fill with Min Count
22. Connect-4-Style Board Game
23. Key Store With Sum-Dependency Cascades
24. Linked-List Intersection With Cycles
25. Menu — Minimum Cost to Cover Wanted Items
26. Account Merge / User Deduplication
27. Refund Waterfall Across Payment Methods
28. Board Score — Connected Areas × Crowns
29. Ski Path — Max Score on a Weighted DAG
30. Terrain Rendering + Water Drop Simulation
31. Maximum Profit in Job Scheduling
32. Banking — Deposit / Withdraw / Transaction / Balance
33. Group Chat / Messaging System Design
34. Distributed Key-Value Store Design
35. Support Ticket Routing & Claiming System
36. Airbnb Experiences — Search Ranking (ML Design)
37. Listing Lifetime Value — Estimation (ML Design)
38. Waitlist System Design
39. Improve Booking via Cover Photo Selection (ML Design)
40. Home Page — Search + Availability + Ranking
41. Host Listings Page — Aggregation Optimization
42. Query System — Time + Geo Filtered User Activity
43. Airbnb Booking System Design
44. Notification System (Event + Batch)
45. Experience / Project Deep-Dive Round
46. Core Values Behavioral Round
47. Hiring Manager Screen

---

## 1. Code Review — Multi-PR Walkthrough

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: code-reading, refactoring, python, concurrency, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/code-review-multi-pr
- **related discussions**: 9

### Statement

A 60-minute round in which the candidate reviews 2–3 pull requests of increasing complexity (Python or Java). The interviewer is mostly silent; the candidate is expected to drive: read top-down, leave inline comments, and surface bugs, design issues, missing tests, and operational concerns.

---

## 2. In-Memory File System OA

- **category**: coding
- **frequency**: Medium · New
- **last asked**: 2026-09-08
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: filesystem, object-design, data-structure, compression, testing, codesignal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/oa-in-memory-file-system
- **related discussions**: 5

### Statement

Implement a progressive in-memory file or cloud-storage system. One rotation adds copy/search, user capacity, and compression; another uses add/get/delete, prefix-ranked largest-file queries, user ownership and merge, then per-user backup and restore.

---

## 3. Smallest Permutation ≥ Lower Bound

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-26
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: math, backtracking, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/smallest-permutation-bound
- **related discussions**: 5

### Statement

Given either an integer `n` or a list of digits `0..9`, return the smallest integer string that can be formed from the available digits. The follow-up adds a lower bound `L`: return the smallest value ≥ `L` using the same digit multiset, with zeros excluded in part 1 but usable in part 2.

---

## 4. URL Query-String Parser

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-26
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, hashmap, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/url-query-string-parser
- **related discussions**: 5

### Statement

Given a URL string, parse its query portion into a dictionary. Handle keys without values, repeated keys (collect into a list), and URL-encoded characters (`%26`, `%3D`, …) via an unquote helper.

---

## 5. Review — Token Tagging / Replacement

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-26
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, trie, string-processing, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/review-token-replace
- **related discussions**: 4

### Statement

Given a review text and a map of tokens to labels, scan the text and wrap each occurrence of a token with `[label]{token}`. Case-insensitive matching; preserve the original casing in the output.

---

## 6. Progressive Banking System OA

- **category**: coding
- **frequency**: Medium · New
- **last asked**: 2026-08-23
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: object-design, data-structure, scheduling, top-k, hard
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/oa-banking-system
- **related discussions**: 5

### Statement

Build a four-level banking system. The progression covers account creation, deposits and transfers; ranking the highest-spending accounts; scheduling and canceling future payments; and finally merging accounts while retaining balances, transaction state, and historical queries. A workspace-credit rotation replaces scheduled payments with delayed cashback while preserving the same state-ordering challenge.

---

## 7. Text Justification — Print as Table

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-19
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, string-processing, simulation, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/text-justification-table
- **related discussions**: 4

### Statement

Given a list of articles (or sentences) and a fixed line width, format each article into a bordered table where every line is exactly `width` characters wide. Words must not split; punctuation cannot start a line; insert a `----` separator between consecutive articles.

---

## 8. Split Stay — Two-Listing Date Coverage

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-02-28
- **stage**: tech-screen · phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: array, hashmap, set, bitmask, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/split-stay-availability-combinations
- **related discussions**: 4

### Statement

Given a set of Airbnb listings, each with a list of available days, and a requested date range, return every pair of two distinct listings whose combined availability covers the range with no overlap (the first listing covers a contiguous prefix, the second covers the remainder).

---

## 9. In-Memory Object Manager OA

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-08-28
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: in-memory-database, hashmap, ttl, simulation, codesignal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/oa-object-manager-prefix-ttl
- **related discussions**: 1

### Statement

Implement an in-memory object manager keyed by `object_id`, where every object owns unique field/value pairs. Extend basic CRUD with prefix scans, timestamped writes with finite lifetimes, expiry cleanup, and point-in-time queries over still-valid fields.

---

## 10. Task Management System OA

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-07
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, object-design, sorting, scheduling, testing, codesignal, hard
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/oa-task-management-system
- **related discussions**: 1

### Statement

Implement a four-level task-management system. The progression covers task creation, update, lookup, and compact JSON output; filtered and priority-ordered search; users, quotas, and expiring assignments; then completion and overdue-assignment history.

---

## 11. Recipe Management System OA

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-29
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: object-design, hashmap, sorting, testing, hard
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/oa-recipe-management-system
- **related discussions**: 1

### Statement

Implement a progressive recipe-management service. Levels 1–3 cover recipe CRUD with case-insensitive name uniqueness, ingredient search and deterministic listing, then user creation and user-scoped recipe edits; the final level has not yet been exposed.

---

## 12. Parcel Tracking System

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-27
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, state-machine, object-design, testing, codesignal, oa, hard
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/oa-parcel-tracking-system
- **related discussions**: 1

### Statement

Implement a progressively unlocked parcel-tracking system. The first level records per-parcel event counts; later levels add parcel ranking, courier assignment with courier-specific events, and undo plus courier sign-out behavior.

---

## 13. Nested-List Iterator

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: iterator, data-structure, object-design, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/nested-list-iterator-with-remove
- **related discussions**: 1

### Statement

Implement a Python iterator over an array of integer arrays with `hasNext()`, `next()`, and `remove()`. Traversal is row-major, and `remove()` must delete the last returned value from the original nested list without causing the following value to be skipped.

---

## 14. Boxes, Keys & Candies (OOD)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-02
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, object-design, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/boxes-keys-candies-ood
- **related discussions**: 1

### Statement

Starting from one open box in a house, explore nested boxes. Each box may hold candies, keys, and child boxes; a box opens only if it is open initially or you hold its key. Count the maximum candies collectable. The interviewer pushes hard on the object model (a Box class).

---

## 15. Property Combination for Group Size

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: backtracking, optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/property-combination-min-capacity
- **related discussions**: 1

### Statement

Given properties (each with a capacity) in a neighborhood and a target group size, find the combination that fits the group while minimizing total capacity, breaking ties by fewest properties. A long-standing Airbnb onsite coding problem.

---

## 16. Multi-Stream Round-Robin Iterator

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: iterator, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/multi-stream-iterator
- **related discussions**: 1

### Statement

Implement a merging iterator over multiple input streams that yields elements round-robin — one from each stream in turn, skipping exhausted streams. Supports has_next() and next(). The live trap is conforming to a provided base-class interface.

---

## 17. In-Memory SQL Database

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-25
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, in-memory-database, schema-design, data-structure, sql, hard
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/in-memory-sql-database
- **related discussions**: 1

### Statement

Open-ended prompt: implement an in-memory SQL-like database supporting basic table creation, insertion, and SELECT with WHERE filters. Notoriously under-specified; clarification carries most of the signal.

---

## 18. Implement Retryer

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-19
- **stage**: tech-screen · phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: object-design, oop-design, idempotency, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/implement-retryer
- **related discussions**: 2

### Statement

Implement a `Retryer` that wraps a callable and retries on failure under a configurable policy such as maximum attempts, backoff, and retryable-exception filtering. The prompt also appears as an AI-assisted onsite implementation round where Claude Code is available.

---

## 19. Working Hours Register

- **category**: coding
- **duration**: 90 min
- **frequency**: Low · New
- **last asked**: 2026-08-17
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, state-machine, interval-aggregation, sorting, payroll, codesignal, oa, hard
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/oa-working-hours-register
- **related discussions**: 2

### Statement

Build a four-level Working Hours Register that records employee entry and exit, ranks employees by accumulated work time, activates pending promotions on a later entry, calculates pay using historical compensation, and applies double-pay grant periods.

---

## 20. Chain Booking — Max Cascading Reservations

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-03
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/chain-booking-cascade
- **related discussions**: 2

### Statement

Given a list of listings, each described as `[x, y, r]` (coordinates plus a chain-effect radius), booking one listing auto-books every listing within its radius, which then recursively cascades. Choosing exactly one starting listing, return the maximum number of listings that can end up booked. Reduces to a max-reachable-set over a directed graph built from radius containment; candidates solve it with DFS/BFS from each node (SCC-condensation + DAG DP is the optimization).

---

## 21. Layover Experiences — Exact-Fill with Min Count

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-26
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: MLE, SWE
- **tags**: dp, hashmap, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/layover-experiences-exact-fill
- **related discussions**: 3

### Statement

Given a list of experience durations (rounded to a single decimal) and a layover length `X`, choose the minimum number of bookings whose durations sum to exactly `X`. Experiences can be repeated. If no exact-fill exists, book nothing.

---

## 22. Connect-4-Style Board Game

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-24
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, oop-design, grid, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/board-game-ood
- **related discussions**: 2

### Statement

Implement the rules of a two-player drop-disc board game; Connect-4 is the confirmed canonical variant. Players alternate, discs fall to the lowest empty slot in a column, win condition is K in a row in any direction.

---

## 23. Key Store With Sum-Dependency Cascades

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-19
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, graph, dependency-resolution, topological-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/key-dependency-sum-store
- **related discussions**: 2

### Statement

Implement a key store supporting `set_value(k, v)`, `set_sum(k, [other_keys])`, and `get_value(k)`. Updating a base key cascades through all dependent sum keys.

---

## 24. Linked-List Intersection With Cycles

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, two-pointer, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/linked-list-intersection-with-cycles
- **related discussions**: 2

### Statement

Given two singly-linked lists that may each contain a cycle, determine whether they share any node. The intersection (if any) can be a shared chain or a shared cycle.

---

## 25. Menu — Minimum Cost to Cover Wanted Items

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-03
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: bitmask, state-compression-dp, dp, set, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/menu-min-cost-bitmask
- **related discussions**: 3

### Statement

Given a menu where each item is a bundle of foods with a price, plus a target set of foods you want, return the minimum total cost to acquire every wanted food (and the chosen bundles). Bundles may overlap; you can buy a bundle more than once.

---

## 26. Account Merge / User Deduplication

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-28
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, hashmap, dedup, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/account-merge-dedup-users
- **related discussions**: 2

### Statement

Given a list of user records with attributes such as `name`, `email`, `phone`, mark each record as either a fresh registration or a duplicate. Two records collide if they share any attribute value across email / phone / additional identifiers; the first record wins, later collisions are flagged duplicates. Designed to extend to an arbitrary attribute set.

---

## 27. Refund Waterfall Across Payment Methods

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-28
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, simulation, greedy, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/refund-waterfall-payment-methods
- **related discussions**: 2

### Statement

Given an ordered history of payments (each with method, date, and amount) and an optional history of prior refunds against those payments, produce the refund allocation for a new refund amount. Refunds are issued in full per payment before moving to the next, prioritized by payment method (CREDIT > CREDIT_CARD > PAYPAL) and then by recency (most recent first).

---

## 28. Board Score — Connected Areas × Crowns

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-29
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, bfs, dfs, union-find, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/board-score-area-crowns
- **related discussions**: 2

### Statement

Given an `R × C` board where each cell is a 2-character tile like `G2` (area type = `G`, crown count = `2`), compute the total board score. A connected region of same-type cells contributes `cell_count × total_crowns_in_region` to the score.

---

## 29. Ski Path — Max Score on a Weighted DAG

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, dp, topological-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/ski-path-max-score-dag
- **related discussions**: 2

### Statement

Given a weighted directed acyclic graph where edges carry a cost and nodes carry a reward, compute the maximum `sum(rewards) - sum(costs)` over any path from a fixed `START` to any `END` node.

---

## 30. Terrain Rendering + Water Drop Simulation

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-02-28
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, simulation, grid, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/terrain-water-drop
- **related discussions**: 3

### Statement

Two-part problem. Part 1: given an array of column heights, print the terrain as an ASCII column chart. Part 2: drop `W` units of water at column `c` and print the terrain after the water settles; water flows downhill and pools in valleys.

---

## 31. Maximum Profit in Job Scheduling

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-02-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dp, binary-search, interval, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/max-profit-job-scheduling
- **related discussions**: 2

### Statement

Given a set of jobs each with `(start, end, profit)`, choose a subset of non-overlapping jobs that maximizes total profit. Asked at Airbnb both as a phone-screen warmup and as an onsite-coding prompt; the senior-staff follow-up pushes past the standard `O(n log n)` DP toward an even tighter bound.

---

## 32. Banking — Deposit / Withdraw / Transaction / Balance

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-02-19
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, data-structure, binary-search, easy
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/banking-transactions-class
- **related discussions**: 2

### Statement

Implement a bank account class supporting `deposit(timestamp, amount)`, `withdraw(timestamp, amount)`, `transaction_history(start, end)`, and `balance(timestamp)`. Time-range queries should run faster than linear scan when histories grow large.

---

## 33. Group Chat / Messaging System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-06-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: chat, messaging, websocket, fan-out, schema-design, scaling, presence, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/group-chat-messaging-system-design
- **related discussions**: 7

### Statement

Design a group-chat / messaging product that supports 1:1 and group conversations, real-time delivery, offline message retention, and read receipts. Asked at both the phone-screen-SD slot and onsite system-design slot; the deep-dive direction shifts toward top-N hottest groups, fan-out trade-offs, and message storage schema.

---

## 34. Distributed Key-Value Store Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-08
- **stage**: onsite-system-design · phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: storage, in-memory-database, distributed-systems, sharding, caching, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/distributed-key-value-store-design
- **related discussions**: 4

### Statement

Design a horizontally-scalable distributed key-value store supporting `get` / `set` / `delete` with configurable TTL, high availability, and low-latency reads. Appears as both a coding-style 'implement a KV store' prompt and a full SD round emphasizing CAP, consistent hashing, and replication.

---

## 35. Support Ticket Routing & Claiming System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: service-design, schema-design, metrics, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/ticket-routing-system-design
- **related discussions**: 1

### Statement

Design an internal support system where tickets can be submitted via email, a public API, and a web contact form; each agent sees and claims tickets matching routing rules (e.g. language, location); and managers can view metrics such as average first-response time and average close time over a chosen window. The graded decisions are normalizing multi-channel ingestion into one ticket model, a rules-based routing/claim path that avoids double-claim races, and a metrics pipeline that aggregates over rolling time windows without scanning full history.

---

## 36. Airbnb Experiences — Search Ranking (ML Design)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ranking, recommendation, two-tower, feature-engineering, ab-testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/experiences-search-ranking-ml-design
- **related discussions**: 1

### Statement

Design the ranking model for Airbnb Experiences (or for the main listings search). Standard MLSD scope: problem framing, training data, features, model architecture, eval, online serving, A/B test plan.

---

## 37. Listing Lifetime Value — Estimation (ML Design)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, classification, feature-engineering, evaluation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/listing-lifetime-value-ml-design
- **related discussions**: 1

### Statement

Estimate the lifetime value of a listing — projected revenue over the next 12 months. Used downstream for ranking, host outreach, and inventory acquisition decisions. The prompt is intentionally open and rewards strong scoping.

---

## 38. Waitlist System Design

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-28
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: scaling, schema-design, notification, queueing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/waitlist-system-design
- **related discussions**: 1

### Statement

Design a waitlist subsystem: users join a waitlist for a listing; when capacity opens, the system notifies subscribers in priority order. Booking integration is assumed to exist; the design focus is API + schema + notification fan-out.

---

## 39. Improve Booking via Cover Photo Selection (ML Design)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ranking, ab-testing, evaluation, vlm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/cover-photo-selection-ml-design
- **related discussions**: 1

### Statement

Pick the cover photo for each listing to maximize click-through and booking. Open-ended ML design — the candidate drives the framing.

---

## 40. Home Page — Search + Availability + Ranking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: search, ranking, scaling, recommendation, mlsd, hard
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/home-page-search-availability-design
- **related discussions**: 1

### Statement

Design the backend powering the Airbnb home page: search, availability filtering, ranking with ML integration. Senior / staff version of the booking-system prompt with heavier emphasis on the recommendation layer.

---

## 41. Host Listings Page — Aggregation Optimization

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: scaling, caching, query-optimization, schema-design, sql, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/host-listings-page-optimization
- **related discussions**: 1

### Statement

A host opens their dashboard and picks a date range; the page renders per-listing aggregates (nights booked, average price). With 100+ listings per host, the page becomes slow. Diagnose and redesign.

---

## 42. Query System — Time + Geo Filtered User Activity

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-30
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: geohash, location, search, data-modeling, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/query-system-time-geo-search
- **related discussions**: 1

### Statement

Design a query system that lets internal users look up an end-user's searches and bookings, filtered by time window and geographic region. Read-heavy, batch-tolerant freshness, large catalog.

---

## 43. Airbnb Booking System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: scaling, schema-design, concurrency, distributed-lock, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/booking-system-design
- **related discussions**: 3

### Statement

Design the core booking pipeline: search availability, hold a listing for a date range, complete payment, confirm reservation. Race conditions over the same listing-date pair are the central deep-dive.

---

## 44. Notification System (Event + Batch)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-12-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: notification, push-notification, messaging, scaling, queueing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/notification-system-design
- **related discussions**: 2

### Statement

Design Airbnb's notification system. Supports two pipelines: event-triggered single notifications (booking confirmation, check-in lock code) and bulk batch sends (promotional pushes to host cohorts). Delivery channels include email, SMS, push, and X / Twitter.

---

## 45. Experience / Project Deep-Dive Round

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-19
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: SWE, MLE
- **tags**: deep-dive, behavioral, technical-deep-dive, impact, leadership, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/experience-project-deep-dive
- **related discussions**: 13

### Statement

A 60-minute project deep-dive (sometimes runs to 90 minutes if both sides are engaged). One project is picked from the resume and explored in detail. The graded signals are scope, decision quality, ownership, and the ability to compress a complex project into a clear narrative.

---

## 46. Core Values Behavioral Round

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-13
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, bq, culture-fit, values, why-company, star, medium
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/core-values-bq-round
- **related discussions**: 8

### Statement

A 45-minute conversation with a calibrated culture-fit interviewer, scheduled after the technical loop. Centers on Airbnb's mission and the candidate's alignment with it. Less STAR-heavy than the Amazon LP loop; more reflective and biographical.

---

## 47. Hiring Manager Screen

- **category**: behavioral
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-06-17
- **stage**: recruiter-screen · phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, screening, fit, deep-dive, easy
- **url**: https://www.1point3acres.com/interview/problems/company/airbnb/hiring-manager-screen
- **related discussions**: 6

### Statement

A 45-minute conversation with the hiring manager, scheduled between the tech screen and the onsite (sometimes also as a team-match gate after onsite). Centered on background fit, role expectations, and the candidate's questions.

---
