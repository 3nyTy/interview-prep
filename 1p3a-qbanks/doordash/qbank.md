# DoorDash — 1point3acres Question Bank (35 curated questions)

Source: 1point3acres qbank (`company=doordash`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/doordash>

The site lists 143 entries for this company: **35 curated questions** (below), 108 OJ links, 0 external links. Only the curated 35 are captured here.

> DoorDash's loop is dominated by a small, slow-moving library of original Code Craft prompts (Dasher Pay, Bootstrap API, Round Robin debugging) plus a handful of recurring system-design favorites (3-day Donation, Notification, Food Review). Coding bar is the gate; system design correctness and behavioral consistency drive leveling. The bar swings sharply with the interviewer — multiple candidates report bug-free coding rounds that still ended in rejection — so prepping the canonical question set cold matters more than chasing breadth.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Code Craft: Dasher Pay
2. Debugging: Pick Dasher / Round Robin Load Balancer
3. Code Craft: Bootstrap Aggregated API
4. AI Code Craft Challenge: Delayed-Delivery Workflow Engine
5. Coding: Minimum Dasher Processing Speed
6. Coding: Minimum Parenthesis Deletions
7. Coding: First Unique Restaurant in a Stream
8. Code Craft: Validate Cart
9. Code Craft: Chef Skill → Dish Profit Assignment
10. Code Craft: Hierarchical Path Key-Value Store
11. Code Craft: Basic Calculator (No Parentheses)
12. Code Craft: Batch with Capacity & Time Window (MLE)
13. Menu Tree Diff — Count Changed Nodes
14. Code Craft: Eligible Orders / Neighbor-Check Sweep
15. Code Craft: Common Restaurant Pickup Order (LCS Variant)
16. Code Craft: Generate 5-Minute Day-Time Codes
17. Code Craft: Cart with Promotions (OOD)
18. Code Craft: Dasher Pay via Mock Service API
19. Code Craft: Nearest Destination on 2D Grid (Multi-Source BFS)
20. Code Craft: Restaurant Delivery Heatmap
21. Frontend VO: Build UI from Mockup with API
22. DE / AE Screen: SQL + Python (Sliding Window + Joins)
23. Code Craft: Similar Restaurant Names (K-Swap Anagram)
24. System Design: Food Review System with Reward Bonus
25. System Design: 3-Day Donation Service
26. System Design: Notification / Alert System
27. ML System Design: Restaurant / Store Recommendation
28. System Design: Real-Time Restaurant Leaderboard
29. System Design: Realtime Monitoring System
30. System Design: Customer Review Page (EM)
31. DE / AE Onsite: Data Modeling (Fitness App)
32. Hiring Manager Behavioral Round
33. Onsite: Project Deep Dive
34. Onsite: On-Call Case Investigation
35. MLE Onsite: ML Knowledge / Discussion Round

---

## 1. Code Craft: Dasher Pay

- **category**: coding
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-10
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, EM
- **tags**: simulation, interval, state-machine, json, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-dasher-pay
- **related discussions**: 27

### Statement

The canonical DoorDash Code Craft prompt. Given a chronological event stream for a dasher's day (orders ACCEPTED / FULFILLED / CANCELED with timestamps), compute the dasher's total pay. Pay accrues per minute that the dasher has at least one ongoing delivery; multiple concurrent orders typically multiply the per-minute rate. Production-style follow-ups dominate the second half of the round.

---

## 2. Debugging: Pick Dasher / Round Robin Load Balancer

- **category**: coding
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: debugging, code-reading, hashing, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/debugging-pick-dasher-round-robin
- **related discussions**: 16

### Statement

The canonical DoorDash debugging round. A roughly 200-line load-balancer / dasher-picker codebase contains planted state, boundary, and removal bugs. One branch ends in a consistent-hashing redesign; the random-picker branch focuses on O(1) swap-delete correctness, synchronization scope, and avoiding external calls while holding a shared lock.

---

## 3. Code Craft: Bootstrap Aggregated API

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-09
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, circuit-breaker, object-design, json, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-bootstrap-aggregated-api
- **related discussions**: 9

### Statement

The second-most-common Code Craft prompt. Given three internal services (consumer / payment / address), implement a single bootstrap endpoint that takes a `user_id`, fans out to all three downstream APIs, and assembles the responses into one composite payload. Resilience to partial downstream failure is the central signal.

---

## 4. AI Code Craft Challenge: Delayed-Delivery Workflow Engine

- **category**: coding
- **duration**: 75 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE, EM
- **tags**: ai-coding, ai-tools, ai-allowed, ai-collaboration, parsing, new-round, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/ai-code-craft-workflow-engine
- **related discussions**: 11

### Statement

A 2026 round that explicitly encourages AI coding assistants in the candidate's own IDE. Delivery varies between a starter-code workflow engine and a from-scratch refund-service variant; the latter requires real HTTP services running on separate local ports. The graded signal is rapid, validated AI collaboration plus early clarification of the expected runtime shape.

---

## 5. Coding: Minimum Dasher Processing Speed

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, binary-search, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/coding-minimum-dasher-processing-speed
- **related discussions**: 1

### Statement

A DoorDash business wrapper around LC 875, Koko Eating Bananas. Given N order workloads and H hours, find the minimum integer processing rate K when a dasher can work on only one order per hour and unused time in a finished hour is lost.

---

## 6. Coding: Minimum Parenthesis Deletions

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, greedy, stack, easy
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/coding-minimum-parenthesis-deletions
- **related discussions**: 1

### Statement

Given a string containing only `(` and `)`, return the minimum number of characters that must be deleted to make it valid. The follow-up asks for the deletion indices rather than only the count.

---

## 7. Coding: First Unique Restaurant in a Stream

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, streaming, hashmap, linked-list, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/coding-first-unique-restaurant-stream
- **related discussions**: 1

### Statement

DoorDash wrapper around LC 1429, First Unique Number. Support `add(restaurant_id)` and return the earliest restaurant ID that has appeared exactly once, preserving arrival order; follow-ups probe unbounded metadata growth and concurrent calls.

---

## 8. Code Craft: Validate Cart

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-10
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-validate-cart
- **related discussions**: 1

### Statement

Phone-screen Code Craft prompt. Given a cart with item entries, validate whether the cart is orderable under quantity, availability, and min/max-limit rules. The prompt is intentionally open-ended; candidates are expected to negotiate exact rule semantics before coding.

---

## 9. Code Craft: Chef Skill → Dish Profit Assignment

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: greedy, sorting, two-pointer, prefix-sum, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-chef-dish-profit-assignment
- **related discussions**: 1

### Statement

Assign chefs to dishes to maximize total profit. Each chef can cook at most one dish; multiple chefs can cook the same dish. Each dish has a difficulty level, and a chef can cook a dish only if their skill ≥ the dish's difficulty. Profits sorted (or queryable) by difficulty.

---

## 10. Code Craft: Hierarchical Path Key-Value Store

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, tree, oop-design, filesystem, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-path-key-value-store
- **related discussions**: 1

### Statement

OOD Code Craft round. Implement a hierarchical key-value store using directory-like paths (e.g. `/a/b/c`). The store starts with a root path `/` whose value is `"root"`. Operations: `create`, `set`, `get`, `delete` (leaf-only; root cannot be deleted).

---

## 11. Code Craft: Basic Calculator (No Parentheses)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: stack, string, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-basic-calculator-no-parens
- **related discussions**: 1

### Statement

Phone-screen coding round. Implement a basic calculator that handles `+`, `−`, `×`, `÷` over integer operands. No parentheses. Standard operator-precedence evaluation.

---

## 12. Code Craft: Batch with Capacity & Time Window (MLE)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-07
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: greedy, sliding-window, interval, heap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-batch-with-capacity-window-mle
- **related discussions**: 1

### Statement

MLE PhD intern coding round. Given timepoints `1, 3, 5, ...` and per-batch constraints `(capacity B, window W)`, group timepoints into the minimum number of batches such that each batch holds at most B items and the latest item in a batch is within W time units of the earliest. Follow-up extends to a meeting-room-2 style sweep-line variant.

---

## 13. Menu Tree Diff — Count Changed Nodes

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, recursion, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-menu-tree-diff
- **related discussions**: 1

### Statement

DoorDash phone-screen coding prompt. Given an existing menu tree (in our system) and a new menu tree sent by a merchant, count how many nodes changed. Nodes are matched by key and position: a value change on a matching node counts as 1; a removed or added subtree counts every node inside it; and moving a node under a different parent counts as one removal plus one addition. The clean solution is a recursive co-traversal that matches children by key. Two worked examples leaked verbatim.

---

## 14. Code Craft: Eligible Orders / Neighbor-Check Sweep

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-18
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, data-structure, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-eligible-orders-sweep
- **related discussions**: 1

### Statement

Code Craft round centered on processing an ordered list of orders where each removal can re-enable its neighbors. Naive `O(n²)` works but interviewer pushes for `O(n)` using a doubly-linked-list / neighbor-eligibility recomputation pattern.

---

## 15. Code Craft: Common Restaurant Pickup Order (LCS Variant)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-common-restaurant-list
- **related discussions**: 1

### Statement

Two drivers each have an ordered list of restaurants to pick up from; one car, can only pick up at restaurants both lists contain, must preserve each list's order. Return the longest common pickup list. Equivalent to the classic Longest Common Subsequence reconstruction.

---

## 16. Code Craft: Generate 5-Minute Day-Time Codes

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string-processing, simulation, parsing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-day-time-interval-codes
- **related discussions**: 1

### Statement

Given a start and end day-time (e.g. `Mon 10:00 AM` to `Tue 11:00 PM`), generate an integer list representing every 5-minute interval in the range, encoded as `DHHMM` where D is the day index (Mon=1..Sun=7) and HHMM is 24-hour time.

---

## 17. Code Craft: Cart with Promotions (OOD)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-cart-promotions-ood
- **related discussions**: 1

### Statement

OOD Code Craft round. A shopping cart contains items with prices and a shipping fee. A list of promotions applies — either a percentage discount (does not apply to shipping) or a fixed-dollar discount. Find the promotion that yields the lowest payment and return the final amount. Multiple parts, each adding edge cases.

---

## 18. Code Craft: Dasher Pay via Mock Service API

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-09-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: interval, interval-aggregation, api-integration, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-dasher-pay-via-api-mock
- **related discussions**: 2

### Statement

Variant of the Dasher Pay round in which the dasher's order data is exposed by a mock service. The candidate must call the service, compute pay, and (mid-round) be told to also write a mock API wrapper around the underlying function. Tests whether the candidate can keep the algorithmic core decoupled from the API plumbing.

---

## 19. Code Craft: Nearest Destination on 2D Grid (Multi-Source BFS)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-multi-source-bfs-grid
- **related discussions**: 3

### Statement

Given a 2D grid containing obstacles and DashMart destinations, compute shortest distances to the nearest destination. The output is either a distance grid or one distance per supplied query location. Obstacles, queries already on a destination, and unreachable locations require explicit contract clarification.

---

## 20. Code Craft: Restaurant Delivery Heatmap

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: grid, prefix-sum, interval-aggregation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-restaurant-heatmap
- **related discussions**: 2

### Statement

A grid-coverage coding round seen in DoorDash MLE and SWE loops. Given an n×n grid and a list of restaurants `(i, j, r, d)` (location, range, expected deliveries), build a heatmap where each cell holds the sum of expected deliveries from all restaurants whose `(Chebyshev) range r` covers that cell.

---

## 21. Frontend VO: Build UI from Mockup with API

- **category**: coding
- **duration**: 75 min
- **frequency**: Low
- **last asked**: 2026-03-20
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: frontend, react, fullstack, api-integration, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/frontend-vo-build-ui
- **related discussions**: 2

### Statement

Frontend phone-screen and onsite coding rounds. Given a mock API and a design mockup, build the UI to match in HackerRank's in-browser editor. Variants include adding filter buttons, conditional category rendering, see-more / collapse behavior, and an exact CSS / flexbox layout match. Tests both React / TypeScript fluency and ability to work in the platform's editor.

---

## 22. DE / AE Screen: SQL + Python (Sliding Window + Joins)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-20
- **stage**: phone-screen
- **access**: Members
- **roles**: DS
- **tags**: sql, python, sliding-window, query-optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/de-ae-sql-python-screen
- **related discussions**: 2

### Statement

Data Engineer / Analytics Engineer screen. One Python coding (sliding-window max-sum returning the 1-based start day) + four SQL queries against a merchant / menu / order / dasher ER diagram (bucket assignment, top-5 selection, vegetarian-merchant percentage). Candidate chooses Python-first or SQL-first ordering.

---

## 23. Code Craft: Similar Restaurant Names (K-Swap Anagram)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/code-craft-similar-restaurant-names
- **related discussions**: 2

### Statement

K-anagram-family Code Craft variant. Determine if two restaurant names are "similar" — equivalent up to at most `k` character swaps. Several recent loops use `k = 2` (one swap of two character positions allowed); some older loops use the general K-anagram form.

---

## 24. System Design: Food Review System with Reward Bonus

- **category**: system-design
- **duration**: 75 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: ranking, feed, caching, idempotency, moderation, kafka, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/system-design-food-review-system
- **related discussions**: 10

### Statement

Design a food-review system on top of DoorDash orders. Users submit reviews (text, rating 1–5, optional photos) tied to a delivered order. The system aggregates per-item ratings (5-point scale), shows posts with like counts, and rewards high-engagement reviews (100+ thumbs up) with DoorDash credit. Sometimes paired with a monthly driver-payout subsystem in the same prompt.

---

## 25. System Design: 3-Day Donation Service

- **category**: system-design
- **duration**: 75 min
- **frequency**: High
- **last asked**: 2026-07-30
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, EM
- **tags**: payment, idempotency, transactions, messaging, async, kafka, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/system-design-three-day-donation
- **related discussions**: 9

### Statement

Design a donation service for a 3-day charity event integrated into the DoorDash app. Users browse charities, donate via the existing DoorDash payment integration, and receive confirmation. The round is dominated by payment-integration trade-offs: idempotency, exactly-once semantics, async vs sync flows, webhook handling, and reconciliation.

---

## 26. System Design: Notification / Alert System

- **category**: system-design
- **duration**: 75 min
- **frequency**: Medium
- **last asked**: 2026-08-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, EM
- **tags**: notification, fan-out, messaging, pubsub, rate-limiting, kafka, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/system-design-notification-alert-system
- **related discussions**: 4

### Statement

Design an alerting system that receives events from upstream services and fans out to subscribers. The broad variant targets users across email, SMS, push, and in-app channels; a narrower variant targets downstream services instead. Subscription semantics, deduplication, retries, failure isolation, backpressure, and scaling are the core deep-dive themes.

---

## 27. ML System Design: Restaurant / Store Recommendation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-02
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS
- **tags**: recommendation, mlsd, two-tower, ranking, ml-infra, ann, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/mle-system-design-restaurant-recommendation
- **related discussions**: 6

### Statement

The dominant MLE / Applied Scientist system-design prompt. Design a recommendation system that surfaces restaurants or store items to a DoorDash user given query + context. Interviewers tend to push the discussion toward infrastructure (feature store, two-tower retrieval, online serving) rather than model architecture; multi-objective optimization (CVR vs delivery time) is a recurring follow-up.

---

## 28. System Design: Real-Time Restaurant Leaderboard

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: leaderboard, streaming, top-k, sliding-window, sharding, dedup, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/system-design-realtime-restaurant-leaderboard
- **related discussions**: 1

### Statement

Design a system that continuously displays the ten restaurants with the most orders in a sliding one-hour window. Second-level freshness is acceptable, approximate answers are allowed, and extensions cover per-city leaderboards, late or duplicate events, and stream-processor recovery.

---

## 29. System Design: Realtime Monitoring System

- **category**: system-design
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2026-04-06
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: distributed-systems, kafka, sharding, storage, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/system-design-realtime-monitoring
- **related discussions**: 1

### Statement

Design a real-time monitoring system: services emit metrics, the platform ingests at high volume, supports queries / dashboards / alerts. This is the canonical Datadog-style metrics-monitoring prompt (ingest → storage → query → alerting); the DoorDash variant follows that standard structure closely.

---

## 30. System Design: Customer Review Page (EM)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-02
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: EM
- **tags**: search, ranking, llm, caching, product-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/system-design-customer-review-page-em
- **related discussions**: 1

### Statement

EM-track SD prompt. Design a customer-facing review page for DoorDash with text + rating + multimedia, search, and (newer dimension) AI-based summarization of reviews. Less infra-deep than the SDE SD round; more focus on product trade-offs and team-level decisions.

---

## 31. DE / AE Onsite: Data Modeling (Fitness App)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-16
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: DS
- **tags**: sql, data-modeling, schema-design, etl, data-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/de-ae-data-modeling
- **related discussions**: 1

### Statement

Data modeling round for Analytics Engineering. Design a schema for a fitness app, define DAU, and write the DAU query against the schema. A small set of standard SQL follow-ups extend the round; window functions cover most of them.

---

## 32. Hiring Manager Behavioral Round

- **category**: behavioral
- **duration**: 45 min
- **frequency**: Very high
- **last asked**: 2026-09-01
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE, EM
- **tags**: behavioral, bq, star, conflict, leadership, ai-collaboration, easy
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/hm-behavioral-round
- **related discussions**: 21

### Statement

Standard HM BQ slot. 45–60 minutes covering 4–6 questions on conflict, mistakes, mentorship, and increasingly AI-usage philosophy. Same shape across SDE, MLE, EM tracks; EM gets deeper on people-management and decision-making, MLE gets a domain-knowledge thread woven in.

---

## 33. Onsite: Project Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-15
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: SWE, MLE, EM
- **tags**: deep-dive, presentation, behavioral, leadership, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/onsite-project-deep-dive
- **related discussions**: 13

### Statement

Standalone deep-dive round (especially for MLE, Staff, and EM). DoorDash publishes a prep outline ahead of time: problem statement, your role, methods used, difficulties, success metrics. The interviewer drills on trade-offs, metric selection (offline / online / business), and post-launch learnings.

---

## 34. Onsite: On-Call Case Investigation

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-17
- **stage**: onsite-coding · onsite-deep-dive
- **access**: Members
- **roles**: SWE, EM
- **tags**: sre, troubleshooting, verbal, open-ended, operating-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/onsite-case-investigation
- **related discussions**: 4

### Statement

Verbal-only onsite round that simulates discovering a live production incident. The candidate is given a scenario (alert fired, dashboard anomaly, customer complaint), and must verbally walk through investigation, mitigation, and recovery. Tests real on-call experience.

---

## 35. MLE Onsite: ML Knowledge / Discussion Round

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-02
- **stage**: onsite-research
- **access**: Members
- **roles**: MLE, RS
- **tags**: ml-knowledge, ab-testing, ranking, experiment-design, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/doordash/mle-ml-discussion-round
- **related discussions**: 6

### Statement

MLE-specific onsite slot. Open-ended ML discussion driven by the interviewer's interests: A/B testing fundamentals, ranking metric definitions, multi-objective ranker design, offline-vs-online metric reconciliation, marketplace-specific experiment design. Less infrastructure than the ML system design round.

---
