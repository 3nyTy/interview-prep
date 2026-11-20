# Pinterest — 1point3acres Question Bank (41 curated questions)

Source: 1point3acres qbank (`company=pinterest`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/pinterest>

The site lists 147 entries for this company: **41 curated questions** (below), 71 OJ links, 35 external links. Only the curated 41 are captured here.

> Pinterest's loop leans hard on a recurring custom prompt library — escape-room state-tracking, trie-backed access control, ads/log aggregation, splitwise-style settlement — over canonical LeetCode. ML system design is split into two distinct rounds ("ML Practitioner" and "ML System Design") that effectively cover the same recommender / retrieval / ranking surface from two angles, with embedding-heavy product framing throughout. Fully-remote, front-loaded compensation, and long team-match windows define the experience as much as the technical bar.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Escape Room / Room-by-Room Race
2. Reconstruct Itinerary (LC 332)
3. Pin / Board Graph Distance
4. Splitwise / Settle Group Balances
5. Trie Autocomplete / Prefix Search
6. Put Boxes Into the Warehouse (LC 1564)
7. Sparse Matrix Class (Store / Print / Add / Multiply)
8. Convert BST to Sorted Doubly Linked List (LC 426)
9. Lighthouse Light Propagation
10. Graph Distance / Connectivity (MLE Phone Variant)
11. Weighted Sampling from Score Distribution
12. New-Grad CodeSignal OA
13. Top-K Ads from Log + Sliding-Window Ingest
14. Newline-Split Stream Reader
15. Regenerate Strings from Run-Length Digit Encoding
16. Subarray Score ≤ K (Sliding-Window)
17. Coin Change (LC 322)
18. Policy Violation Records Aggregation
19. Expression Add Operators (LC 282)
20. Hyperparameter Combinations (Cartesian Product)
21. Tree-Encoded Subtree Deletion and Stable Compaction
22. Grant / Revoke / Check Access on Hierarchy
23. Robot Vacuum 8-Direction Grid
24. Experiment Bucket Allocation / Rebalancing
25. Elevator Assignment / Nearest Available Elevator
26. Stack Items into the Shortest Column (Min-Heap Load Balancing)
27. ML Intern OA (CodeSignal: MCQ + NN Forward + Coding + ML Implementations)
28. Implement round() From String + Round-to-Precision
29. Binary Search Log Entries by Date String
30. System Design: Search Typeahead / Input Suggestions
31. ML System Design: Search & Ranking
32. ML System Design: Notification Ranking & Ads CTR
33. System Design: Personalized Chatbot Recommending Pins
34. System Design: Bulk + Single Catalog Update
35. System Design: Ads Event Aggregation / Reporting
36. System Design: Comment Notification Service
37. System Design: Leaderboard / Category Ranking
38. System Design: Distributed Rate Limiter
39. ML System Design: Bad / Unsafe Content Detection
40. Behavioral: Leadership, Initiative, Project End-to-End
41. ML Fundamentals Quick-Fire

---

## 1. Escape Room / Room-by-Room Race

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: data-structure, object-design, hashmap, linked-list, leaderboard, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/escape-room-player-tracking
- **related discussions**: 9

### Statement

Design a small game-state data structure: `n` rooms and `m` players all start in room 0; each call advances one player to the next room. Support O(1) per-room headcount, O(1) per-player move, and a top-K leaderboard where farther rooms rank higher and arrival-order breaks ties within the same room. By far the most-recurring custom Pinterest coding prompt in the current rotation, asked at both phone and onsite.

---

## 2. Reconstruct Itinerary (LC 332)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-02
- **stage**: phone-screen · tech-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: graph, dfs, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/reconstruct-itinerary
- **related discussions**: 6

### Statement

LeetCode 332 verbatim: reconstruct a flight itinerary from a list of tickets, starting at JFK, lexicographically smallest when ties. Asked both as a phone-screen warmup and as an onsite coding round. A minority phone variant removes the fixed start and asks you to infer an origin before printing the full path.

---

## 3. Pin / Board Graph Distance

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: graph, bfs, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/pin-board-shortest-distance
- **related discussions**: 4

### Statement

Given a bipartite graph of boards and pins (each board owns a list of pins; pins can appear on multiple boards), answer three questions: are two pins related, shortest pin-to-pin distance via shared boards, and shortest distance between two boards via any pin path.

---

## 4. Splitwise / Settle Group Balances

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-18
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: hashmap, greedy, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/splitwise-settle-balances
- **related discussions**: 5

### Statement

Group of friends takes a trip; each person has paid an arbitrary subset of the bills. Output any valid sequence of paybacks `(from, to, amount)` that zeros every person's net balance. The minimum-number-of-transactions variant is the bonus follow-up, not the base ask.

---

## 5. Trie Autocomplete / Prefix Search

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-11
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: trie, string, hashmap, set, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/trie-autocomplete-prefix-search
- **related discussions**: 6

### Statement

Trie-backed prefix search appears as three core coding forms—classic autocomplete, first-index prefix lookup, and moderation matching—plus an MLE phone-screen alternate based on a frequency-ranked autocomplete system.

---

## 6. Put Boxes Into the Warehouse (LC 1564)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: greedy, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/put-boxes-into-warehouse
- **related discussions**: 1

### Statement

One-hour technical screen built on the "put boxes into the warehouse" problem — fit boxes (each with a height) into a row of warehouse rooms whose usable height is capped by the lowest room to their left. The follow-up flips the objective to maximizing total box height placed rather than box count.

---

## 7. Sparse Matrix Class (Store / Print / Add / Multiply)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-26
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: object-design, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/sparse-matrix-class
- **related discussions**: 1

### Statement

After a ~15-minute resume shallow-dive, the MLE phone screen turns into an object-design coding task: build a class that stores a sparse matrix compactly and supports printing it back as a dense grid, matrix addition, and matrix multiplication.

---

## 8. Convert BST to Sorted Doubly Linked List (LC 426)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: tree, bst, linked-list, tree-traversal, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/bst-to-doubly-linked-list
- **related discussions**: 1

### Statement

LeetCode 426 verbatim — convert a BST into a sorted circular doubly-linked list in place — with an insert follow-up: support inserting a new node into the resulting DLL while keeping the sort order.

---

## 9. Lighthouse Light Propagation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, simulation, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/lighthouse-2d-matrix
- **related discussions**: 1

### Statement

Simulate light propagation across a 2-D matrix: lighthouses emit light in defined directions, and walls or grid edges stop each ray. The core mechanic is ray casting or directional traversal; exact direction encoding is intentionally left for clarification.

---

## 10. Graph Distance / Connectivity (MLE Phone Variant)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-15
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: graph, bfs, dfs, union-find, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/graph-distance-mle-screen
- **related discussions**: 1

### Statement

Pinterest's graph-focused MLE teams (PinSAGE / PinnerSAGE adjacent) sometimes ask a graph problem framed in Pinterest product terms rather than a LeetCode problem. The base ask is connectivity / shortest distance over the inferred graph; specific prompts vary by interviewer.

---

## 11. Weighted Sampling from Score Distribution

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-26
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: sampling, probability, math, numerical-stability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/weighted-sampling-from-scores
- **related discussions**: 1

### Statement

Given a list of strings and their scores (real-valued, distributed over the full real line), draw a single sample where the probability of each string is proportional to a normalized version of its score. The numerical-stability discussion (softmax over arbitrary reals) is part of the signal.

---

## 12. New-Grad CodeSignal OA

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-02-10
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: codesignal, oa, string, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/code-signal-ng-oa
- **related discussions**: 1

### Statement

Four-problem CodeSignal OA for new-grad applicants. The first half is short algorithmic warm-up work; the second half uses longer, level-based custom interfaces with hidden tests.

---

## 13. Top-K Ads from Log + Sliding-Window Ingest

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: hashmap, heap, sliding-window, top-k, streaming, ads, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/top-k-ads-from-log-sliding-window
- **related discussions**: 1

### Statement

Two-part ads round: (1) given a static impression log, return the top-K advertisers by impression count; (2) extend to a streaming `ingestImpressions` that scores the top-K within a sliding time window. The streaming follow-up is the scored portion.

---

## 14. Newline-Split Stream Reader

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, iterator, string-processing, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/newline-stream-line-reader
- **related discussions**: 1

### Statement

Given a class with a `next()` method that returns arbitrary string chunks containing zero or more embedded newlines, write a wrapper that yields one full line per call. Chunks can split a line mid-word; partial lines from one chunk must be joined with the start of the next.

---

## 15. Regenerate Strings from Run-Length Digit Encoding

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-18
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, string-processing, compression, backtracking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/string-from-rle-digits
- **related discussions**: 1

### Statement

Inverse of an RLE-style decoding: given a digits-only string like `'2345'`, enumerate strings that decode to the same multiset when interpreted as run-length-encoded segments. For `'2345'` the decoded base is two of 3 followed by four of 5 (`'335555'`); equivalent encodings include `'2 of 345555'` shape variants.

---

## 16. Subarray Score ≤ K (Sliding-Window)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, sliding-window, two-pointer, prefix-sum, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/subarray-score-at-most-k
- **related discussions**: 1

### Statement

Given an integer array and an integer K, count the number of contiguous subarrays whose `(sum × length)` score is at most K. Must be O(n).

---

## 17. Coin Change (LC 322)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: onsite-coding
- **access**: Members
- **tags**: dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/coin-change
- **related discussions**: 1

### Statement

The topic is Coin Change (LC 322). Practice the canonical minimum-coin formulation; the exact onsite contract and any modifications remain unspecified.

---

## 18. Policy Violation Records Aggregation

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, dedup, interval, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/policy-violation-records-aggregation
- **related discussions**: 3

### Statement

Given violation records `(pinId, policy, date)`, implement three queries: unique pins that violated a given policy, unique pins that violated any policy inside a date range, and unique pins per policy inside a date range. The interviewer leaves return shapes and duplicate handling for the candidate to clarify, and duplicate `pinId` records are explicitly part of the problem.

---

## 19. Expression Add Operators (LC 282)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-09-01
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, SWE
- **tags**: string, backtracking, recursion, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/expression-add-operators-simplified
- **related discussions**: 3

### Statement

Expression Add Operators appears in both the canonical LeetCode 282 form, which uses normal arithmetic precedence, and a simplified Pinterest form that evaluates operators strictly left-to-right. Insert `+`, `-`, or `*` between digit groups so the expression reaches a target under the specified evaluation rule.

---

## 20. Hyperparameter Combinations (Cartesian Product)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-30
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: recursion, backtracking, hashmap, iterator, testing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/hyperparameter-cartesian-product
- **related discussions**: 3

### Statement

Given a dict of hyperparameter name → list of candidate values, generate every combination (one value per key). ML Platform asks this directly; the follow-up is to enumerate test cases the function should be exercised on.

---

## 21. Tree-Encoded Subtree Deletion and Stable Compaction

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, tree, dfs, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/tree-encoded-subtree-deletion-compaction
- **related discussions**: 3

### Statement

A forest is stored as a parent-index array where node `i` is a root when `parent[i] == i`. First mark a target node and every descendant with `-1`; then compact the survivors to the front in stable order and rewrite every surviving parent index to the new positions.

---

## 22. Grant / Revoke / Check Access on Hierarchy

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, tree-broadcast, hashmap, access-control, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/grant-revoke-access-tree
- **related discussions**: 3

### Statement

Design an access-control class on a static hierarchy (world → country → city). `grant_access(entity, group)` and `revoke_access(entity, group)` must be O(1); `check_access(entity, group)` is O(depth) by walking up the hierarchy and OR-ing access bits along the path. The trick is to look up the tree, not down, at check time.

---

## 23. Robot Vacuum 8-Direction Grid

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-18
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, dfs, bfs, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/robot-vacuum-grid
- **related discussions**: 2

### Statement

Three-part robot-traversal grid problem: a vacuum starts at a position and can move in 8 directions (orthogonal + diagonal). Return all reachable cells in three settings — open grid, single-obstacle grid, and two cooperating vacuums. A phone-screen form records visited cells, stops at a blocker, and returns the final matrix.

---

## 24. Experiment Bucket Allocation / Rebalancing

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, set, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/experiment-bucket-allocation-rebalancing
- **related discussions**: 2

### Statement

Manage experiment assignments over bucket IDs 0–999. First return every unallocated bucket; then rebalance control and enabled groups while preserving existing assignments. The free-bucket subtask is defined, but the rebalance variant still requires explicit endpoint, rounding, contiguity, and tie-break rules before implementation.

---

## 25. Elevator Assignment / Nearest Available Elevator

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-07-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, scheduling, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/elevator-assignment-nearest-available
- **related discussions**: 3

### Statement

Given elevators described by current floor and state (up, down, or idle), plus a passenger's floor and desired direction, return the nearest eligible elevator's index or -1. The follow-up starts every elevator idle at a given floor and processes timestamped passenger requests, asking which elevator serves the final passenger while cars move one floor per second. Its state-transition contract is incomplete and must be clarified before implementation.

---

## 26. Stack Items into the Shortest Column (Min-Heap Load Balancing)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: heap, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/shortest-column-stacking-heap
- **related discussions**: 2

### Statement

A stream of items (pins / ads), each with a height, is appended one by one onto a fixed number of columns. Each item always goes onto the column that is currently shortest; ties break to the leftmost such column. Maintain the running column heights and analyze time / space complexity. The base case uses two columns; the follow-up generalizes to k columns.

---

## 27. ML Intern OA (CodeSignal: MCQ + NN Forward + Coding + ML Implementations)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-06-14
- **stage**: oa
- **access**: Members
- **roles**: MLE
- **tags**: codesignal, oa, ml-knowledge, classification, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/mle-intern-codesignal-oa
- **related discussions**: 2

### Statement

ML-flavored CodeSignal OA for ML interns: a block of ML MCQs, a hand-computed neural-network forward pass (bring paper, calculator, and a webcam-friendly workspace), one LeetCode-style coding problem, and two ML implementation problems (bootstrap tree without NumPy; naive Bayes from formulas).

---

## 28. Implement round() From String + Round-to-Precision

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-27
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: string, string-processing, parsing, math, numerical-stability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/round-and-precision-string
- **related discussions**: 2

### Statement

Two-part string-arithmetic problem: (1) reimplement Python's `round()` on a string input — no float conversion — with all the edge cases that exposes; (2) round a numeric string `s` to a precision specified by another string `p` (e.g. `p='100'` rounds to the hundreds, `p='0.1'` rounds to one decimal).

---

## 29. Binary Search Log Entries by Date String

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, string, array, parsing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/binary-search-log-by-date
- **related discussions**: 2

### Statement

Given a list of log entries timestamped as date strings, find the entry corresponding to a target date via binary search. Recent variants extend to violation-log counting where the first part is a hashmap pass and the second part needs binary search on a sorted timestamp array.

---

## 30. System Design: Search Typeahead / Input Suggestions

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: search, trie, caching, ranking, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/system-design-typeahead-suggestions
- **related discussions**: 4

### Statement

Design Pinterest's search typeahead — autocomplete suggestions for partially-typed queries. Asked at SDE L4 system-design and as the search-team SD round in MLE loops. Standard architecture: trie + frequency + personalization layer, with edge caches.

---

## 31. ML System Design: Search & Ranking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ml-infra, retrieval, ranking, two-tower, recommendation, ann, search, hard
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/ml-system-design-search-and-ranking
- **related discussions**: 6

### Statement

Family of design prompts: Pin search, Pin ranking, home-feed retrieval, search input suggestions. Interviewers expect a candidate-generation → ranking stage architecture with explicit embedding sourcing, loss choice (pair-wise vs point-wise), offline metrics, and candidate-pool sizing math.

---

## 32. ML System Design: Notification Ranking & Ads CTR

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ml-infra, notification, push-notification, ads, calibration, ranking, feature-engineering, hard
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/ml-system-design-notifications-ctr
- **related discussions**: 4

### Statement

Two adjacent prompts that share an architecture: design the ML system for Pinterest notifications (which pin to push, when, with what frequency cap) or for ads CTR prediction (rank ad candidates for an impression slot). Both want explicit feature, loss, calibration, and online-experimentation design.

---

## 33. System Design: Personalized Chatbot Recommending Pins

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, chat, llm-agent, recommendation, rag, retrieval, hard
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/system-design-personalized-chat-recommend
- **related discussions**: 1

### Statement

Design a chatbot that converses with the user and recommends pins inline. Combines an LLM conversational layer with a Pinterest-style retrieval / ranking stack on the back end. New prompt — exact framing not fully transcribed; treat as a hybrid LLM-agent + recommendation design.

---

## 34. System Design: Bulk + Single Catalog Update

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: backend, ingestion, async, storage, etl, ads, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/system-design-catalog-update-bulk-single
- **related discussions**: 3

### Statement

Design the merchant catalog update pipeline — merchants push product updates either one-at-a-time (single API call) or in bulk (CSV upload, hundreds of thousands of rows). The bulk path goes async via S3 + worker pool; the single path is a synchronous API. The interviewer wants both modes designed coherently with one downstream.

---

## 35. System Design: Ads Event Aggregation / Reporting

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: ads, etl, data-engineering, streaming, kafka, storage, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/system-design-ads-event-aggregation
- **related discussions**: 3

### Statement

Design the ads event aggregation pipeline — billions of impression / click / conversion events ingested, aggregated by advertiser / campaign / segment, served to a reporting dashboard with both real-time and historical views.

---

## 36. System Design: Comment Notification Service

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-08-06
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: notification, push-notification, fan-out, rate-limiting, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/system-design-comment-notifications-viral-pins
- **related discussions**: 3

### Statement

Design a push-notification system that alerts a Pin owner whenever another user comments. Extend it for a viral Pin receiving too many comments: aggregate activity and enforce a limited notification rate per recipient instead of sending one push for every event.

---

## 37. System Design: Leaderboard / Category Ranking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: leaderboard, redis, ranking, caching, storage, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/system-design-leaderboard
- **related discussions**: 3

### Statement

Design a per-category leaderboard service — for each category, show the top-K most-clicked posts in near-real time. The standard Redis-sorted-set solution is the expected starting point; the round goes deep on category cardinality, write rate, and consistency.

---

## 38. System Design: Distributed Rate Limiter

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: rate-limiting, throttling, redis, distributed-systems, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/system-design-rate-limiter
- **related discussions**: 3

### Statement

Standard distributed rate-limiter SD — enforce per-API-key request quotas across a horizontally-sharded API gateway. Interviewers expect a token-bucket vs sliding-window comparison, plus a Redis-backed implementation discussion.

---

## 39. ML System Design: Bad / Unsafe Content Detection

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, moderation, integrity, vlm, weak-supervision, classification, hard
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/ml-system-design-content-safety
- **related discussions**: 2

### Statement

Design a content-understanding pipeline that flags unsafe / bad pins (or bad images) at upload time and at re-evaluation time. Used by Trust & Safety / Content Signal teams; the framing changes between teams but the underlying ML system is recognizably one design.

---

## 40. Behavioral: Leadership, Initiative, Project End-to-End

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-18
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, leadership, impact, deep-dive, ambiguity, conflict, star, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/behavioral-leadership-initiative
- **related discussions**: 12

### Statement

Standard BQ round with recurring weight on initiative and end-to-end ownership at L5+. Recent failed loops were specifically tagged to weak initiative-story answers. The format ranges from discrete prompts to a one-hour resume deep dive centered on two candidate-selected projects; hiring managers push for concrete details instead of a rigid story sequence.

---

## 41. ML Fundamentals Quick-Fire

- **category**: other
- **duration**: 30 min
- **frequency**: High
- **last asked**: 2026-07-16
- **stage**: phone-screen · onsite-research
- **access**: Members
- **roles**: MLE
- **tags**: ml-knowledge, verbal, cs-fundamentals, medium
- **url**: https://www.1point3acres.com/interview/problems/company/pinterest/ml-fundamentals-quick-fire
- **related discussions**: 9

### Statement

First half of the MLE phone screen — and occasionally a warm-up on technical-screen / onsite — is a rapid verbal quiz on overfitting, bias-variance, L1 vs L2, vanishing gradients (with placement: input-side vs output-side), and learning-rate behavior. Wrong answers lose the round before coding even starts.

---
