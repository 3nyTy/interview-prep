# Roblox — 1point3acres Question Bank (44 curated questions)

Source: 1point3acres qbank (`company=roblox`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/roblox>

The site lists 136 entries for this company: **44 curated questions** (below), 50 OJ links, 42 external links. Only the curated 44 are captured here.

> Roblox interviews lean heavily on a small, stable repertoire of coding and system design prompts drawn from the company's own product surface — rate limiting, like-button counters, matchmaking queues, delayed payments. The bar is high but predictable: interviewers want bug-free implementations with crisp boundary handling and design discussions that ground every trade-off in real platform scale (millions of concurrent players, hot creator experiences, cross-region latency). HM and director rounds heavily probe past experience with comparable traffic and scope.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Most Frequent Call Path from Function Trace Logs
2. Rate Limiter (Sliding Window) with Per-Entity Follow-up
3. Topological Sort with Secondary Ordering
4. File Chunk Range Tracking
5. Piano Keys: Vanilla UI Interaction
6. Distinct Cyclic Number Pairs
7. Merge Intervals (LeetCode 56)
8. Cursor-Based Pagination Over Sorted Logs
9. Implement Trie (LeetCode 208)
10. Phone Battery Discharge Scheduling
11. Single-Threaded CPU (LeetCode 1834)
12. Candy Crush Grid Matching and Gravity
13. Flatten Nested Array
14. Highest-Earning Experience Tracker
15. Meeting Rooms II (LeetCode 253)
16. Grid Pathfinding with Obstacles (DFS)
17. Find the Closest Palindrome (LeetCode 564)
18. Closest Binary Search Tree Value Variant (LeetCode 270)
19. Design Search Autocomplete System
20. Maximize Distance to Closest Person (LeetCode 849)
21. Remove Prefix Strings
22. Minimum Height Difference Between Distant Peaks
23. Maximum Number of Balls in a Box (LeetCode 1742)
24. Maximum Safe Steps on a Laser Grid
25. Sliding Window: Target Containment and Most-Repeated Window
26. Like / Unlike Counter System
27. Delayed Payment Scheduler (Robux Transfer)
28. Matchmaking Service for Multiplayer Games
29. Notification System
30. Image Feed (Frontend System Design)
31. ML System Design: Game Genre Classification From Scratch
32. Design Roblox Wallet
33. Online Fraud Detection System
34. Design Roblox Release Deployment Workflow
35. Design a ROS-like Pub/Sub System
36. Feed Status App
37. Design Instagram
38. Design a Multi-Resource Resource Loader
39. TinyURL (URL Shortener)
40. To-Do List with Multi-User Collaboration
41. Near-Real-Time Friend-Played-Game Counter
42. HM Behavioral: Traffic Scale & Scope of Past Work
43. MLE Project / Paper Deep Dive
44. ML Modeling: Recommend Games to a Roblox User

---

## 1. Most Frequent Call Path from Function Trace Logs

- **category**: coding
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-13
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: stack, parsing, hashmap, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/most-frequent-call-path-from-trace-logs
- **related discussions**: 14

### Statement

Parse a stream of function entry / exit events, maintain the active call stack, and return the full call path (root → leaf) that occurs most often across the trace. Tie-breaking prefers the deeper path, then the path that first reached the top frequency. Two follow-ups extend the problem to multiple threads and to returning the longest active stack.

---

## 2. Rate Limiter (Sliding Window) with Per-Entity Follow-up

- **category**: coding
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-07-09
- **stage**: phone-screen · oa · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE, Infra Eng
- **tags**: rate-limiting, sliding-window, hashmap, queue, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/rate-limiter-sliding-window
- **related discussions**: 13

### Statement

The single most common Roblox phone-screen / OA coding problem: implement a sliding-window rate limiter that decides, request by request, whether each timestamped request would breach a `maxRequests` cap within a fixed `windowLength`. The follow-up generalizes the cap to be enforced independently per user and per experience (game), denying a request if either limit is exceeded.

---

## 3. Topological Sort with Secondary Ordering

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-06-29
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: topological-sort, heap, graph, scheduling, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/topological-sort-with-ordering
- **related discussions**: 4

### Statement

Given dependency declarations where each row starts with a component and the remaining entries are prerequisites, return a valid build order. The current canonical Roblox variant requires components that become buildable at the same level to be emitted in declaration order, and returns `["Error"]` for missing dependencies or cycles.

---

## 4. File Chunk Range Tracking

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: interval, array, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/file-chunks-consecutive-byte-ranges
- **related discussions**: 1

### Statement

Process a stream of file chunks represented by inclusive byte ranges [start, end]. After each arrival, return the merged continuous ranges, joining both overlaps and directly adjacent byte ranges.

---

## 5. Piano Keys: Vanilla UI Interaction

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: frontend, javascript, dom, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/piano-keys-vanilla-ui
- **related discussions**: 1

### Statement

Build six on-screen piano keys in vanilla HTML, CSS, and JavaScript. Mouse and keyboard presses must independently control held-state styling, multiple keys must work simultaneously, and unrelated keyboard input must have no effect.

---

## 6. Distinct Cyclic Number Pairs

- **category**: coding
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2026-08-12
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, string-processing, hashmap, codesignal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/distinct-cyclic-number-pairs
- **related discussions**: 1

### Statement

Count index pairs whose integers have equal digit lengths and whose decimal strings are cyclic rotations of one another. The initial pairwise rotation check missed hidden-test performance expectations; the target complexity is linear in the number of input digits through canonical grouping.

---

## 7. Merge Intervals (LeetCode 56)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-27
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: interval, merge, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/merge-intervals
- **related discussions**: 1

### Statement

Given a list of intervals, merge every overlapping interval and return the non-overlapping coverage.

---

## 8. Cursor-Based Pagination Over Sorted Logs

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: sorting, streaming, backend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/cursor-based-pagination-over-sorted-logs

### Statement

Implement cursor-based pagination over sorted logs while preserving stable order and continuation semantics.

---

## 9. Implement Trie (LeetCode 208)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-20
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: trie, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/implement-trie
- **related discussions**: 1

### Statement

Implement a prefix tree with insert, exact-word lookup, and prefix lookup operations.

---

## 10. Phone Battery Discharge Scheduling

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: scheduling, greedy, heap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/phone-battery-discharge-scheduling

### Statement

Schedule or select phone activities under battery discharge constraints to maximize usable time or completed work.

---

## 11. Single-Threaded CPU (LeetCode 1834)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: heap, sorting, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/single-threaded-cpu

### Statement

Simulate a single-threaded CPU that picks available tasks by shortest processing time, then original index.

---

## 12. Candy Crush Grid Matching and Gravity

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, simulation, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/candy-crush-grid-matching-and-gravity

### Statement

Implement Candy Crush-style grid matching, clearing, gravity, and repeated stabilization.

---

## 13. Flatten Nested Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, recursion, iterator, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/flatten-nested-array

### Statement

Flatten a nested array structure into a single list while preserving left-to-right order.

---

## 14. Highest-Earning Experience Tracker

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-10
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: heap, hashmap, streaming, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/highest-earning-experience-tracker
- **related discussions**: 1

### Statement

Build a streaming tracker over update/query operations: each update mutates an experience's running profit by a delta; each query returns the name of the experience with the current highest total. Tests the classic streaming-top-1 / max-heap pattern with stale-entry pruning.

---

## 15. Meeting Rooms II (LeetCode 253)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: interval, heap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/meeting-rooms-ii
- **related discussions**: 1

### Statement

Given meeting time intervals, compute the minimum number of rooms needed so no meetings assigned to the same room overlap.

---

## 16. Grid Pathfinding with Obstacles (DFS)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dfs, bfs, grid, graph, easy
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/dfs-grid-with-obstacles
- **related discussions**: 1

### Statement

Classic 2D grid reachability problem reported in the ads-team phone screen: given a grid with obstacles, determine whether a path exists from a start cell to a goal cell. Solved with DFS or BFS; complexity discussion expected.

---

## 17. Find the Closest Palindrome (LeetCode 564)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, palindromes, math, hard
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/find-the-closest-palindrome

### Statement

Given an integer string, find the nearest different integer that is a palindrome.

---

## 18. Closest Binary Search Tree Value Variant (LeetCode 270)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bst, tree, tree-traversal, easy
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/closest-binary-search-tree-value-variant

### Statement

Given a BST and a target value, return the tree value closest to the target.

---

## 19. Design Search Autocomplete System

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: trie, heap, search, hard
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/design-search-autocomplete-system

### Statement

Design an autocomplete system that returns top suggestions for the current prefix and learns completed queries.

---

## 20. Maximize Distance to Closest Person (LeetCode 849)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/maximize-distance-to-closest-person

### Statement

Given a row of occupied and empty seats, choose an empty seat that maximizes distance to the closest occupied person.

---

## 21. Remove Prefix Strings

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, sorting, trie, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/remove-prefix-strings

### Statement

Remove strings that are prefixes of another string, returning only the non-prefix survivors.

---

## 22. Minimum Height Difference Between Distant Peaks

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, sorting, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/minimum-height-difference-between-distant-peaks

### Statement

Select two sufficiently distant peaks with minimum height difference, using sorting or indexed scanning to satisfy the distance constraint.

---

## 23. Maximum Number of Balls in a Box (LeetCode 1742)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, math, easy
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/maximum-number-of-balls-in-a-box

### Statement

Count balls by digit-sum box number and return the maximum number of balls in any box.

---

## 24. Maximum Safe Steps on a Laser Grid

- **category**: coding
- **duration**: 50 min
- **frequency**: Low · New
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, grid, set, simulation, codesignal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/laser-grid-maximum-safe-steps
- **related discussions**: 2

### Statement

On a 1-indexed rectangular board, a robot starts at a given cell and may move in one straight cardinal direction. Laser centers make their entire rows and columns unsafe; return the greatest number of safe steps available before the robot reaches an unsafe line or the board boundary.

---

## 25. Sliding Window: Target Containment and Most-Repeated Window

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: sliding-window, hashmap, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/sliding-window-target-and-most-repeat
- **related discussions**: 2

### Statement

MLE onsite coding round: given a list of integers, a target value, and a fixed window size, (1) return every sliding window that contains the target, then (2) find the first window that maximizes the count of the target. Tests fluency with sliding-window + hashmap and crisp boundary handling.

---

## 26. Like / Unlike Counter System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-23
- **stage**: phone-screen · oa · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: system-design, ranking, caching, scaling, sharding, schema-design, idempotency, hard
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/like-unlike-counter-system
- **related discussions**: 7

### Statement

Design a like / unlike service that supports per-user, per-item like state, list of items a user has liked, and an approximate or exact total like count per item — at platform scale (hundreds of millions of users, hot creator experiences pushing billions of likes/day). Interviewers focus on schema design, idempotency under retry, hot-shard handling, and the read-vs-write trade-off in the counter path.

---

## 27. Delayed Payment Scheduler (Robux Transfer)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: system-design, payment, scheduling, idempotency, transactions, distributed-lock, hard
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/delayed-payment-scheduler
- **related discussions**: 5

### Statement

Design a system that lets a user schedule a virtual-currency transfer (Robux) from one account to another, to be executed automatically at a specified future time. The core deep-dive is on durable scheduling under failure, exactly-once execution despite retries, and how to keep the scheduler accurate when millions of payments are queued for the same wall-clock minute.

---

## 28. Matchmaking Service for Multiplayer Games

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-08
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: system-design, scaling, distributed-lock, queueing, game-engine, hard
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/matchmaking-service-multiplayer
- **related discussions**: 4

### Statement

Design a matchmaking service that places players into balanced multiplayer sessions. Players enter a queue, get grouped by skill into teams of a fixed size (commonly 16), and are allocated to a freshly provisioned game server. The interview focuses on queue partitioning, the skill-bucketing strategy, time-vs-fairness trade-offs as wait time grows, and how to keep matches from leaking across regions.

---

## 29. Notification System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-06-04
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: notification, push-notification, queueing, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/notification-system
- **related discussions**: 1

### Statement

Design a notification system with user preferences, fan-out, delivery channels, retries, and observability.

---

## 30. Image Feed (Frontend System Design)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-07
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, frontend, react, caching, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/image-feed-frontend-design
- **related discussions**: 1

### Statement

Frontend-leaning system design: build an infinite-scroll image feed (no like / comment surface) against a fixed backend API. The deep-dive concentrates on infinite-load mechanics, offline support, and testability — the three axes the interviewer scores on explicitly.

---

## 31. ML System Design: Game Genre Classification From Scratch

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-18
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, classification, feature-engineering, evaluation, active-learning, vlm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/ml-system-design-game-genre-classification
- **related discussions**: 1

### Statement

MLE phone-screen prompt: design a system to classify each game on the platform into a fixed genre taxonomy from scratch. Covers label collection, feature pipelines, model choice for a multi-modal item, training and refresh cadence, and downstream consumption.

---

## 32. Design Roblox Wallet

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-04-07
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: payment, transactions, idempotency, database
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/design-roblox-wallet

### Statement

Design a Roblox wallet with balances, ledger entries, idempotent transfers, reconciliation, and fraud controls.

---

## 33. Online Fraud Detection System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-03-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: integrity, streaming, ranking, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/online-fraud-detection-system

### Statement

Design an online fraud detection pipeline that scores user actions in near real time and escalates suspicious activity.

---

## 34. Design Roblox Release Deployment Workflow

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: ci-cd, versioning, scaling, distributed-systems
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/design-roblox-release-deployment-workflow

### Statement

Design a Roblox release deployment workflow with staged rollout, rollback, health checks, and multi-region coordination.

---

## 35. Design a ROS-like Pub/Sub System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-02-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: pubsub, messaging, distributed-systems, backend
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/design-a-ros-like-pub-sub-system

### Statement

Design a ROS-like publish/subscribe system for topics, publishers, subscribers, delivery guarantees, and scaling.

---

## 36. Feed Status App

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-02-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: feed, presence, social-graph, backend
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/feed-status-app

### Statement

Design a status/feed app that stores user updates, serves timelines, and handles real-time or near-real-time freshness.

---

## 37. Design Instagram

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: feed, social-graph, storage, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/design-instagram

### Statement

Design Instagram-style media posting, feed generation, follows, likes, comments, and large-scale media serving.

---

## 38. Design a Multi-Resource Resource Loader

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-12-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: scheduling, concurrency, backend, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/design-a-multi-resource-resource-loader

### Statement

Design a loader that schedules work across multiple constrained resources while respecting dependencies, fairness, and failure handling.

---

## 39. TinyURL (URL Shortener)

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-10-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: system-design, caching, scaling, schema-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/tiny-url
- **related discussions**: 1

### Statement

Standard URL-shortener system design used as a phone-screen SD warm-up in some loops. Cover the encoding scheme, collision handling, read-heavy caching, and analytics.

---

## 40. To-Do List with Multi-User Collaboration

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-09-05
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, schema-design, ambiguous-spec, concurrency, websocket, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/todo-list-multi-user-collab
- **related discussions**: 3

### Statement

Design a shared to-do list product where multiple users can read and edit the same list concurrently. The interview is intentionally under-specified; the candidate is expected to clarify requirements up front and drive the conversation. Focus is on the schema, the read/write conflict model, and the API surface for collaboration.

---

## 41. Near-Real-Time Friend-Played-Game Counter

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-19
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: system-design, scaling, social-graph, feed, fan-out, redis, caching, hard
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/friend-played-game-near-realtime
- **related discussions**: 3

### Statement

Design a service that, for a given (user, game) pair, surfaces near-real-time signals: how many of the user's friends have played the game, plus the total active-player count for the game. Targets 10M users and 100k games. The core problem is the fan-out from a play event to every friend of the player without overwhelming the storage layer at peak hours.

---

## 42. HM Behavioral: Traffic Scale & Scope of Past Work

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-13
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE, Infra Eng, EM, All
- **tags**: behavioral, bq, star, leadership, scaling, ambiguity, culture-fit, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/behavioral-hm-traffic-and-scope
- **related discussions**: 12

### Statement

Roblox HM, director, and executive conversations are short but high-leverage. They consistently weight traffic / scope of past work, ownership under ambiguity, product-specific motivation, and proactive decision-making; a strong technical loop can still fail at final matching if these signals are thin.

---

## 43. MLE Project / Paper Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-13
- **stage**: onsite-deep-dive · onsite-research
- **access**: Members
- **roles**: MLE, RS
- **tags**: deep-dive, paper-read, presentation, ml-knowledge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/mle-project-deep-dive
- **related discussions**: 5

### Statement

Standard MLE / Research onsite deep-dive round: walk the interviewer through one of your published papers or a shipped ML project. The interviewer goes deep on the technical detail, picks at edge cases, and steers the conversation toward how the work would transfer to Roblox's product surface.

---

## 44. ML Modeling: Recommend Games to a Roblox User

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, recommendation, two-tower, ranking, ml-knowledge, evaluation, ab-testing, feature-engineering, hard
- **url**: https://www.1point3acres.com/interview/problems/company/roblox/ml-modeling-game-recommendation
- **related discussions**: 2

### Statement

Open-ended ML / recommendation round for MLE candidates: "how would you recommend games to a Roblox user?" The interviewer expects end-to-end coverage of the recommender pipeline rather than depth on any single component. Used as the MLE onsite modeling slot.

---
