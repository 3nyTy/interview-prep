# Coinbase — 1point3acres Question Bank (28 curated questions)

Source: 1point3acres qbank (`company=coinbase`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/coinbase>

The site lists 112 entries for this company: **28 curated questions** (below), 66 OJ links, 18 external links. Only the curated 28 are captured here.

> Coinbase runs a long, gated pipeline — AI recruiter screen, a cognitive aptitude test, a culture/personality questionnaire, and a CodeSignal coding OA all precede the recruiter call, and team match still happens before the 4-round VO. Coding rounds bias toward multi-level CodeSignal-style problems (banking system, in-memory database, mining-block knapsack, crypto order management) over canonical LeetCode; bring your own scaffolding because interviewers often hand you a blank file with no class structure or tests. AI-assisted coding is now an explicit round on its own.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Transaction Filter + Pagination Endpoint
2. Banking System (Multi-Level OA)
3. Crypto Order Management (Kafka Consumer)
4. Flappy Bird (AI-Assisted Strategy OA)
5. Mining Block — Fee-Maximizing Transactions
6. In-Memory Database / Cloud Storage (Multi-Level OA)
7. Food Delivery System (Multi-Level Coding)
8. MLE CodeSignal OA (MCQ + NN + Classical-ML Coding)
9. Ring Buffer (Producer / Consumer with Backpressure)
10. Frontend Tech Execution — Reusable Dropdown Component
11. Worker Salary OA
12. Interleave Iterator (Round-Robin + Iterator Pattern)
13. Task Management System OA
14. MLE Onsite — Jupyter Pair Programming on Messy Classification Data
15. MLE Onsite — Domain Round (ML Coding + SQL Window Function)
16. Recipe Management System OA
17. Parse Log File by Thread Id
18. AI Enhanced Pilot — Domain (AI-Assisted Coding Round)
19. Generate Random NFT (DFS + Weighted Sampling)
20. Frontend OA — Render Blog Posts (Multi-Stage CodeSignal)
21. Frontend Domain — Card / Validation Component to Mockup
22. System Design — Coinbase Explore (Real-Time Price Dashboard)
23. System Design — Crypto Ledger & Wallet Transaction History
24. System Design — Credit Approval Risk Engine
25. Frontend System Design — Signup Form
26. System Design — Crypto Brokerage / Trading Platform
27. Frontend System Design — Crypto Trading UI Architecture
28. Hiring Manager — Project Deep-Dive + AI Usage BQ

---

## 1. Transaction Filter + Pagination Endpoint

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-11
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, object-design, ambiguous-spec, iterator, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/transaction-filter-pagination
- **related discussions**: 9

### Statement

Build a typed endpoint over an in-memory list of transactions that filters by user / currency / time range and paginates the results. The unstated trap is offset vs cursor pagination — many interviewers will dock candidates who default to numeric offsets without raising the trade-off, and some explicitly ask for the cursor variant in the follow-up.

---

## 2. Banking System (Multi-Level OA)

- **category**: coding
- **duration**: 70 min
- **frequency**: Medium
- **last asked**: 2026-08-23
- **stage**: oa
- **access**: Free preview
- **roles**: SWE
- **tags**: hashmap, heap, data-structure, object-design, codesignal, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/banking-system-oa
- **related discussions**: 6

### Statement

Four-level CodeSignal OA: implement a small bank backend that grows from basic account operations to spending leaderboards, scheduled / cancellable payments, and account merges that preserve history. A current Cloud Compute Credits rotation keeps the leaderboard and merge/history spine but replaces scheduled outgoing payments with delayed compute-consumption rebates. The volume of code is the difficulty; each level is straightforward in isolation but few finish all four in 70 minutes without prior practice.

---

## 3. Crypto Order Management (Kafka Consumer)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-11
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: state-machine, hashmap, kafka, idempotency, parsing, testing, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/crypto-order-management-kafka
- **related discussions**: 4

### Statement

Build the consumer side of an order management system that ingests Kafka messages, tracks each order through New → In_Progress → Completed (with a Cancel extension), and exposes a status query. Spec is deliberately under-specified — you parse the input format, define the API, and decide where idempotency lives. The production-grade signal is whether you raise idempotency keys, exactly-once semantics, and state-machine validation unprompted.

---

## 4. Flappy Bird (AI-Assisted Strategy OA)

- **category**: coding
- **duration**: 70 min
- **frequency**: Medium
- **last asked**: 2026-05-19
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: simulation, algorithm-design, ai-tools, ai-allowed, codesignal, oa, medium, new-round
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/flappy-bird-ai-oa
- **related discussions**: 5

### Statement

A newer AI-assisted OA format that replaces the canonical CodeSignal multi-level problem on some loops. Candidates write a `shouldJump(state)` or `shouldBounce(...)` strategy for a Flappy-Bird-like game with an in-IDE AI assistant they can prompt freely; scoring is by how many auto-played simulation tests the strategy passes. The signal is how well you collaborate with the AI under time pressure, not raw algorithm design alone.

---

## 5. Mining Block — Fee-Maximizing Transactions

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-05
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: greedy, dfs, tree, sorting, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/mining-block-knapsack
- **related discussions**: 7

### Statement

A two-part onsite coding round. Part 1 selects a fee-maximizing subset of independent transactions to fit a fixed block size; Part 2 adds parent-child dependencies between transactions. The trap: the canonical DP / 0-1 knapsack is not what the interviewer wants — 'production-realistic' greedy by fee/size + DFS over dependency bundles is the signal.

---

## 6. In-Memory Database / Cloud Storage (Multi-Level OA)

- **category**: coding
- **duration**: 70 min
- **frequency**: Medium
- **last asked**: 2026-04-28
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, data-structure, object-design, ttl, in-memory-database, codesignal, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/in-memory-database-oa
- **related discussions**: 5

### Statement

The other canonical Coinbase CodeSignal rotation: a four-level in-memory key/value store that grows from CRUD to scan / TTL / backup-restore, with a recent variant adding per-user storage quotas and file compression. Same volume-over-insight calibration as the banking-system OA.

---

## 7. Food Delivery System (Multi-Level Coding)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, object-design, binary-search, dp, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/food-delivery-system
- **related discussions**: 4

### Statement

A multi-part onsite coding round around a food / restaurant delivery system. Early levels exercise hash-map plumbing (registering restaurants, dishes, orders); later levels add binary search on price history, shortest-route variants, and a knapsack-flavored final follow-up. The interviewer-defined level count varies — finishing levels 1–3 cleanly is the realistic bar.

---

## 8. MLE CodeSignal OA (MCQ + NN + Classical-ML Coding)

- **category**: coding
- **duration**: 70 min
- **frequency**: Medium
- **last asked**: 2026-02-08
- **stage**: oa
- **access**: Members
- **roles**: MLE
- **tags**: ml-knowledge, numpy, classification, scikit-learn, linear-algebra, codesignal, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/mle-codesignal-oa
- **related discussions**: 4

### Statement

MLE candidates get a 70-minute CodeSignal bundle: 6 multi-select ML conceptual questions, 1 numerical fill-in (forward-pass through a small NN), and 3 coding problems mixing one warm-up algorithm with two from-scratch classical-ML implementations (bagging, k-means, decision tree). Webcam on; screen-share recorded; the AI assistant is not the explicit collaborator here.

---

## 9. Ring Buffer (Producer / Consumer with Backpressure)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: concurrency, queueing, ring-buffer, hard
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/ring-buffer-producer-consumer
- **related discussions**: 1

### Statement

Low-level design / concurrency round: implement a fixed-capacity ring buffer with producer/consumer semantics, a backpressure policy when the buffer is full, and support for registering multiple consumers. The interviewer is exacting about production-grade behavior — thread-safety, full/empty handling, and slow or lagging consumers — and drills many edge cases. The bar is production realism, not a passing happy path.

---

## 10. Frontend Tech Execution — Reusable Dropdown Component

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-08
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: react, frontend, object-design, testing, ux, hard
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/fe-dropdown-tech-execution
- **related discussions**: 1

### Statement

Frontend Tech Execution round (staff level): build a reusable React dropdown component to a unit-test specification. Bar is reusable-from-day-one: controlled vs uncontrolled, accessible keyboard handling, viewport-aware placement. Production-readiness articulation (what you'd cover with more time) matters as much as the code that lands.

---

## 11. Worker Salary OA

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-05-06
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, object-design, codesignal, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/worker-salary-oa
- **related discussions**: 1

### Statement

An older CodeSignal rotation that has resurfaced on backend loops: a multi-level employee / salary tracking system. Full prompt has not been fully leaked recently, but candidates report the volume-of-code character of the standard 4-level rotation, with Java being especially time-tight.

---

## 12. Interleave Iterator (Round-Robin + Iterator Pattern)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: iterator, object-design, oop-design, queue, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/interleave-iterator

### Statement

Build an interleaving system that round-robins across multiple integer sources. Part 1 flattens a list of arrays by taking turns; Part 2 defines an Iterator interface with list-backed and range-backed implementations; Part 3 composes them into an InterleaveIterator that cycles across underlying iterators and drops each as it exhausts. Tests iterator-pattern fluency and clean OOD more than algorithms.

---

## 13. Task Management System OA

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, heap, object-design, ttl, codesignal, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/task-management-oa
- **related discussions**: 1

### Statement

Newer 4-level rotation: priority-based task CRUD, then filtered listing, then per-user assignment with quotas and TTL, then time-aware completion with explicit handling of expired tasks. Same shape as the banking / DB rotations but built around priority queues and TTL bookkeeping.

---

## 14. MLE Onsite — Jupyter Pair Programming on Messy Classification Data

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: classification, data-cleaning, scikit-learn, pandas, live-review, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/mle-jupyter-classification
- **related discussions**: 1

### Statement

MLE onsite pair-programming round: build a working classification model end-to-end on a messy dataset inside a Jupyter notebook, with the interviewer asking conceptual drive-by questions while you code. The signal is whether you ship a baseline cleanly — over-engineering with full feature stores or gradient-boosted ensembles is the most common time-sink.

---

## 15. MLE Onsite — Domain Round (ML Coding + SQL Window Function)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: ml-knowledge, sql, query-optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/mle-domain-sql-window
- **related discussions**: 1

### Statement

MLE domain round: one short ML coding warm-up (e.g. a logistic-regression update step or k-means iteration) followed by a medium SQL problem that almost always requires a window function and a sub-query. Tight on time — both halves need to land.

---

## 16. Recipe Management System OA

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2025-12-15
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, object-design, versioning, snapshot, codesignal, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/recipe-management-oa
- **related discussions**: 1

### Statement

A recipe-CRUD rotation of the standard four-level OA — add / update / get / delete a recipe object, then case-insensitive search and sorted listing, then per-user ownership, then a version-history + rollback model that mirrors the in-memory database Level-4 pattern.

---

## 17. Parse Log File by Thread Id

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string-processing, parsing, hashmap, sorting, easy
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/parse-log-by-thread-id
- **related discussions**: 2

### Statement

Given a flat list of log lines, group them by `threadId` and return each group sorted by timestamp. Pure string-parsing warm-up that has shown up as the easier of the two coding rounds in some loops.

---

## 18. AI Enhanced Pilot — Domain (AI-Assisted Coding Round)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-11
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: ai-coding, ai-tools, ai-collaboration, code-reading, refactoring, debugging, new-round
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/ai-enhanced-pilot-domain
- **related discussions**: 3

### Statement

A newer onsite "Domain" round (announced in postings as "AI Enhanced Pilot") in which the candidate works with an in-IDE AI assistant on a 4-part script: find a bug, propose a new feature, review a PR, debug a production scenario. Interviewers explicitly observe how you prompt, validate, and accept AI output — the format of the collaboration is graded, not just the final answer. One instantiation runs all four parts against an in-memory transaction-pagination service.

---

## 19. Generate Random NFT (DFS + Weighted Sampling)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-06
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dfs, backtracking, dedup, sampling, ambiguous-spec, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/nft-generation-combinations
- **related discussions**: 3

### Statement

Generate NFTs from a set of attribute categories (e.g. ears, eyes, mouth), each with multiple possible values. Level 1 enumerates all valid combinations (DFS / Cartesian product); Level 2 deduplicates against an existing collection; Level 3 introduces per-value weights and asks for weighted random sampling. The unstated work is parsing your own input and writing your own tests.

---

## 20. Frontend OA — Render Blog Posts (Multi-Stage CodeSignal)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-03-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: react, frontend, api-integration, codesignal, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/fe-render-blog-posts-oa
- **related discussions**: 2

### Statement

Frontend CodeSignal OA: build a blog-post rendering app in four stages — render from local JSON, add a submit form, fetch from an external API and resolve author ids to names (showing 'you' for the current user), then sort by likes and add a delete button for own posts. 90-minute hard cap; tooling friction (slow IDE, missing autocomplete) eats time.

---

## 21. Frontend Domain — Card / Validation Component to Mockup

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: react, frontend, ux, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/fe-domain-card-component
- **related discussions**: 2

### Statement

Frontend Domain round: build a small UI component to a provided mockup — typically a card with an input, button, and validation message — then ship two announced 'extension' features. Spec is precise and unit-tested, but feedback frequently fails candidates whose code matches the spec exactly with a vague 'could be written better' — production-readiness signals matter even at the mid-level bar.

---

## 22. System Design — Coinbase Explore (Real-Time Price Dashboard)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: websocket, sse, fan-out, caching, scaling, pubsub, frontend, backend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/sd-coinbase-explore-realtime-price
- **related discussions**: 5

### Statement

Design the Coinbase Explore page (coinbase.com/explore), which streams real-time prices for hundreds of crypto assets to every visitor. The discussion centers on push vs pull delivery (WebSocket / SSE vs polling), fan-out on millions of subscribers, cache layering between exchange feeds and the browser, and how to degrade gracefully when one upstream feed is delayed.

---

## 23. System Design — Crypto Ledger & Wallet Transaction History

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-06
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: storage, data-modeling, sharding, ingestion, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/sd-crypto-ledger-wallet-history
- **related discussions**: 1

### Statement

Design a crypto-asset ledger that tracks every transaction across chains and supports a 'wallet history' query: given a wallet address, return all transactions touching it, paginated. Recent and unusual variant of the SD round — driven by a single candidate report; details on scale and durability targets are thin.

---

## 24. System Design — Credit Approval Risk Engine

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-02-15
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: idempotency, state-machine, kafka, messaging, async, circuit-breaker, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/sd-credit-approval-risk-engine

### Statement

Design a credit-approval risk engine that accepts an application, returns a decision id instantly, fans out to several slow external providers (credit bureau, KYC, fraud, income) asynchronously, and combines their signals into approve / deny / manual-review. Discussion centers on returning fast while deciding in the background, three-layer idempotency, a single-writer decision state machine, circuit-breakers on flaky vendors, and an append-only audit trail for compliance.

---

## 25. Frontend System Design — Signup Form

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-01
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: frontend, react, design, ab-testing, ux, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/fe-sd-signup-form
- **related discussions**: 1

### Statement

Conversational frontend SD round: design a signup form. Not whiteboard-heavy — the interviewer asks a sequence of open questions about field-by-field architecture, validation, and A/B testing strategy. Despite the 'no right answer' framing, feedback indicates the interviewer has specific expected answers; A/B testing fluency is the recurring signal.

---

## 26. System Design — Crypto Brokerage / Trading Platform

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: trading, messaging, idempotency, transactions, concurrency, scaling, kafka, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/sd-crypto-brokerage
- **related discussions**: 3

### Statement

Design the trading flow of a crypto brokerage: a user submits a buy/sell order, the platform routes it to one or more matching venues, balances and positions update atomically, and the user sees confirmation in seconds. Discussion centers on order lifecycle (state machine), idempotency under network retries, balance accounting, and integration with third-party matching engines.

---

## 27. Frontend System Design — Crypto Trading UI Architecture

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-08
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: frontend, react, design, ux, ab-testing, performance-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/fe-sd-trading-ui
- **related discussions**: 2

### Statement

Frontend System Design round: design the architecture of a crypto trading UI given a prototype screenshot — framework choice, UI library, state management, error handling, loading patterns, performance, security, A/B testing. A staff-level signaling round; "not comprehensive enough" is the most common rejection feedback.

---

## 28. Hiring Manager — Project Deep-Dive + AI Usage BQ

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-07-11
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, deep-dive, leadership, ai-collaboration, culture-fit, medium
- **url**: https://www.1point3acres.com/interview/problems/company/coinbase/hm-bq-project-deep-dive
- **related discussions**: 11

### Statement

The hiring-manager round in every loop: a deep dive on one or two projects from the resume, leveling probes ("why aren't you a level higher"), standard BQ (most challenging / most proud / cross-team conflict), and — newly common in 2026 — an explicit "how do you use AI in your day-to-day" segment. The HM round has the highest interviewer-variance in the loop; treat the conversation as graded even when the interviewer seems checked out.

---
