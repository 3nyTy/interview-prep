# Citadel / Citadel Securities — 1point3acres Question Bank (36 curated questions)

Source: 1point3acres qbank (`company=citadel`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/citadel>

The site lists 139 entries for this company: **36 curated questions** (below), 103 OJ links, 0 external links. Only the curated 36 are captured here.

> Citadel and Citadel Securities run two distinct loops sharing one brand. Citsec (the market maker) hires for HFT software, quant research and quant dev; its phone screens lean on small, sharp coding prompts (merge-K, ring buffers, stock-DP ladders) and the loop is famously single-fail — any weak round and the next interview is cancelled within a day. Citadel (the hedge fund) and its sub-orgs (GQS, EQR, NXT) lean harder on probability brainteasers, deep resume drilling and a multi-week team-match stage where coding bar drops but cultural and PM-style fit dominates the offer decision.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Deduplicating File System
2. Task Dependency Ordering
3. Candy (LC 135)
4. SRE Python Fundamentals — Five-Question Set
5. Real-Time Trading Event Feed Dashboard (React)
6. N-ary Tree Sum + Leaf `next` Pointer
7. Thread-Safe Key Call Counter
8. HFT Onsite Round 3 — Sort Complexity + Linux Fundamentals + tail(n) Design
9. Citadel QD On-Site Coding Ladder (3 Rounds, 3-4 Problems Each)
10. Game of Life — In-Place and Infinite Board
11. Single-Producer Multi-Consumer Ring Buffer
12. Time-Keyed Key-Value Store (set / get-before-timestamp)
13. Refactor Obfuscated C++ Code (Hidden Best-Time-To-Buy-Sell)
14. Multi-Exchange BBO / NBBO Class
15. Application-to-Exchange Routing OOD with Sliding-Window Alerts
16. Weighted Random Sampling with Insert / Delete
17. 2048 Simulation + Compress State to a `long long`
18. LRU + LFU + Custom Eviction Function
19. BST from Scratch (insert / search / delete) + Balancing Discussion
20. Implement a Round-Robin Task Scheduler
21. Largest Team Whose Members Share a Common Office Window
22. Non-Consecutive Process Allocation Counting (Modulo 1e9 + 7)
23. Wildcard / Regex String Matching (`*` operator)
24. Order Book Pair Coding
25. Task Scheduler with Priority + Lazy Deletion
26. Implement `tac` — Reverse-Order File Reader
27. Best Time to Buy and Sell Stock — Full DP Ladder
28. Merge K Sorted Streams (OOD + Algorithm)
29. Fleet Configuration and Deployment Control Plane
30. Metrics Collection & Alerting System
31. Behavioral / Resume Drilling Across the Loop
32. GQS SWE Whiteboard: Boundary Search + Sliding-Window Top K
33. When Does `min x'Qx + c'x` Have a Finite Minimum?
34. EQR Alpha-Factor Research Deep-Dive + LLM Inference Stability
35. Trading OA — 15-Question Probability Quickfire (30 Minutes)
36. GQS Quant Phone — Correlation Range + Box-Muller-Style Sampling

---

## 1. Deduplicating File System

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: filesystem, dedup, hashing, python, oop-design
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/deduplicating-file-system
- **related discussions**: 1

### Statement

Implement a Python FileSystem with writefile(path, data) and readfile(path), storing identical file contents only once. Discuss avoiding unnecessary hashing and extending shared storage to support deletion.

---

## 2. Task Dependency Ordering

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, topological-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/task-dependency-ordering
- **related discussions**: 1

### Statement

Given a map from string task names to prerequisite lists, return any valid ordering of all tasks. The screen supplies a Java-style `Map<String, List<String>>` interface and follows with feasibility and time-complexity questions.

---

## 3. Candy (LC 135)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, greedy
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/candy-lc-135
- **related discussions**: 1

### Statement

Citadel phone-screen opener using the standard LC 135 Candy problem. The candidate explained the approach without writing code, then moved to a data-stream OOD problem; the interviewer emphasized candidate-led trade-off discussion throughout the screen.

---

## 4. SRE Python Fundamentals — Five-Question Set

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-08-05
- **stage**: tech-screen
- **access**: Members
- **roles**: Infra Eng
- **tags**: python, cs-fundamentals, easy
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/sre-python-fundamentals-set
- **related discussions**: 1

### Statement

Complete five short Python tasks in CoderPad: repeated output with blank-line separation, reverse-order squaring, FizzBuzz, word-frequency counting, and the second-largest unique integer with a `None` fallback. The coding set shares a one-hour first round with a verbal system-design prompt.

---

## 5. Real-Time Trading Event Feed Dashboard (React)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-27
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: frontend, react, streaming, sliding-window, trading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/react-trading-event-feed-dashboard
- **related discussions**: 1

### Statement

Build a React dashboard that subscribes to a supplied stream of trade and quote events. Render newest-first feeds by event type, maintain summary statistics over the most recent 30 seconds, and support pause/resume with an explicit explanation of resume behavior; styling is out of scope and AI assistance is disallowed.

---

## 6. N-ary Tree Sum + Leaf `next` Pointer

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: tree, dfs, tree-traversal, recursion, linked-list, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/tree-sum-leaf-next-pointer
- **related discussions**: 1

### Statement

Citadel Data Storage / Infra technical phone screen: define a tree-node structure, compute the sum of all node values, then connect all leaf nodes in DFS order using a `next` pointer. The final follow-up asks for `O(1)` extra space by reusing the node's own pointer fields instead of a helper array or traversal stack.

---

## 7. Thread-Safe Key Call Counter

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, metrics, concurrency, threading, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/thread-safe-key-call-counter
- **related discussions**: 1

### Statement

Citsec phone-screen ladder: implement a counter that returns how many times a key has been called so far, make it thread-safe, then discuss how the same counter should work when multiple applications on one host call it concurrently.

---

## 8. HFT Onsite Round 3 — Sort Complexity + Linux Fundamentals + tail(n) Design

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-07
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: operating-systems, cs-fundamentals, io, sorting, complexity-analysis, hard
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/citadel-hft-onsite-fundamentals
- **related discussions**: 1

### Statement

Citsec HFT (Singapore) onsite round 3: no coding submission, but a wide-ranging interrogation on sort-algorithm complexity (especially quicksort worst case), Linux systems primitives (shared memory, interrupts), and a `tail -n` design on a very large file. Reported as the round that failed the loop and triggered same-day cancellation of round 4.

---

## 9. Citadel QD On-Site Coding Ladder (3 Rounds, 3-4 Problems Each)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-30
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, RS, SWE
- **tags**: dp, hashmap, heap, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/qd-eqr-onsite-coding-ladder
- **related discussions**: 1

### Statement

Citadel QD onsite coding pattern reported by a senior bank quant: three coding rounds, each packing 3-4 LeetCode-style problems (mostly mediums plus 1-2 hards) into the slot. Partial credit on one hard does not block advance, but pace is the dominant grading axis.

---

## 10. Game of Life — In-Place and Infinite Board

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, All
- **tags**: grid, simulation, bit-manipulation, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/game-of-life-in-place-infinite
- **related discussions**: 1

### Statement

Citsec phone screen 2 of 2: implement Conway's Game of Life on a 2D board, then escalate to in-place updates, sparse-board optimization, and finally the infinite-board case.

---

## 11. Single-Producer Multi-Consumer Ring Buffer

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, concurrency, threading, oop-design, hard
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/single-producer-multi-consumer-ring-buffer
- **related discussions**: 1

### Statement

Citsec C++ SWE phone screen with a senior Chinese engineer: 30 minutes of resume drilling followed by a quick OOD — design a single-producer, multi-consumer ring buffer over a fixed-capacity circular array. Reference framing is a circular-deque interface in the spirit of LC 641, generalized to concurrent consumers.

---

## 12. Time-Keyed Key-Value Store (set / get-before-timestamp)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-18
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, hashmap, data-structure, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/time-keyed-kv-store
- **related discussions**: 1

### Statement

Citadel SWE intern phone screen single problem: design a key-value store where each value is tagged with a timestamp, and reads return the most recent value at or before a query timestamp. Classic LC-981 framing with the binary-search-on-sorted-timestamps solution as the target.

---

## 13. Refactor Obfuscated C++ Code (Hidden Best-Time-To-Buy-Sell)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-06
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oa, code-reading, refactoring, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/obfuscated-code-refactor
- **related discussions**: 1

### Statement

Citadel OA round: a 30+ line C++ function `root_node(std::vector<int> output)` packed with red-herring variables, `std::pow`, `std::distance`, bit-shift tricks, and constexpr noise. The task is to read past the obfuscation, recognize the underlying algorithm (max profit from a buy-low / sell-high pass), and rewrite it cleanly.

---

## 14. Multi-Exchange BBO / NBBO Class

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, trading, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/multi-exchange-nbbo-class
- **related discussions**: 1

### Statement

Citsec SWE phone-screen prompt that walks the candidate through a stripped-down market-data class. Ingest a stream of orders tagged with exchange id, price, quantity, and order type; expose per-exchange best bid / best ask and the consolidated NBBO across all exchanges.

---

## 15. Application-to-Exchange Routing OOD with Sliding-Window Alerts

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-28
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, oop-design, data-structure, rate-limiting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/application-exchange-routing-alerting
- **related discussions**: 1

### Statement

Citadel SWE intern onsite round 3: simulate a routing system where applications register subscriptions to exchanges; design when to raise an alert if a single exchange takes on too many subscriptions in a time window. Follow-ups push into distributed control plane design.

---

## 16. Weighted Random Sampling with Insert / Delete

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, sampling, probability, binary-search, hard
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/weighted-sampling-with-updates
- **related discussions**: 1

### Statement

Citadel SWE intern onsite round 2: support dynamic insert and delete of weighted items, plus uniform random sampling proportional to weight. Reported solution uses a Fenwick / binary indexed tree over prefix sums of weights for `O(log n)` per operation.

---

## 17. 2048 Simulation + Compress State to a `long long`

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, bit-manipulation, grid, compression, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/simulate-2048-encode-state
- **related discussions**: 1

### Statement

Citadel SWE US-intern onsite round 1: hand-implement the 2048 game's tile-shift mechanic, then design an encoding that packs the 4x4 board state into a single 64-bit integer for compact storage / hashing.

---

## 18. LRU + LFU + Custom Eviction Function

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, linked-list, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/lru-lfu-custom-evict
- **related discussions**: 1

### Statement

Cache-design ladder asked in a Citsec phone screen. Implement an LRU cache, then an LFU cache, then generalize to a cache parameterized by a pluggable eviction function. The interviewer explicitly grades how cleanly each variant inherits scaffolding from the previous one.

---

## 19. BST from Scratch (insert / search / delete) + Balancing Discussion

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bst, tree, data-structure, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/bst-from-scratch
- **related discussions**: 1

### Statement

Citsec SWE Intern second phone screen: build a binary search tree class in C++ supporting insert, search, and delete; then defend the average / worst-case complexity and explain how to evolve it into an AVL or red-black tree.

---

## 20. Implement a Round-Robin Task Scheduler

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: scheduling, data-structure, queue, operating-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/round-robin-task-scheduler
- **related discussions**: 1

### Statement

Citadel NXT third-round live coding: implement a round-robin task scheduler. Limited detail leaked under the in-post spoiler tag, but the round shape (1-hour coding paired with a 1-hour systems design slot) is consistent with several NXT loops.

---

## 21. Largest Team Whose Members Share a Common Office Window

- **category**: coding
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2025-12-22
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oa, interval-aggregation, sorting, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/interval-overlap-max-team
- **related discussions**: 1

### Statement

Citadel SWE Campus Assessment (HackerRank, 75 minutes total). Given `n` employees with `[startTime[i], endTime[i]]` office windows, return the largest team that contains at least one "core employee" whose window overlaps with every other member's window. Reduces to the maximum number of intervals covering a single point.

---

## 22. Non-Consecutive Process Allocation Counting (Modulo 1e9 + 7)

- **category**: coding
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2025-12-22
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oa, math, dp, easy
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/grace-hopper-non-consecutive-processes
- **related discussions**: 1

### Statement

Citadel SWE Campus Assessment (HackerRank). Count the number of ways to allocate `n_processes` to `n_intervals` consecutive time slots such that the same process never occupies two consecutive slots. Returns the count modulo `10^9 + 7`. Combinatorial closed-form is the intended fast path.

---

## 23. Wildcard / Regex String Matching (`*` operator)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, RS
- **tags**: string, dp, recursion, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/wildcard-regex-matching
- **related discussions**: 1

### Statement

Citsec EQR (NG QR) live-coding round: implement string matching where `*` is the only wildcard operator. The interviewer steers the candidate from recursion-with-memo to a two-pointer iterative solution after the initial submission.

---

## 24. Order Book Pair Coding

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, trading, oop-design, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/order-book-pair-coding
- **related discussions**: 3

### Statement

Open-ended order book implementation appears in Citsec phone screens (EQR Python QD) and Singapore HFT onsite round 1. The interviewer expects the candidate to enumerate the required APIs first, then implement add / cancel / view-best mechanics. Scope varies by round: a 45-minute first-round CoderPad variant requires a matching engine, while another round explicitly excludes matching.

---

## 25. Task Scheduler with Priority + Lazy Deletion

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: heap, data-structure, scheduling, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/task-scheduler-priority-lazy-delete
- **related discussions**: 2

### Statement

Citadel SWE intern onsite round 4: design an in-memory task scheduler that supports insert, delete, and execute-highest-priority. Reported solution uses a priority queue with a lazy-deletion marker so cancellation does not require an `O(n)` heap scan. The same family anchors a 90-minute C++ round in Citadel GQS onsites, framed as a scheduler whose callers register tasks together with their arguments.

---

## 26. Implement `tac` — Reverse-Order File Reader

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-07
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: io, string-processing, operating-systems, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/reverse-file-tac-implementation
- **related discussions**: 2

### Statement

Citsec C++ phone-screen prompt: implement the `tac` command (cat with line order reversed), then ladder up through `O(1)` space optimizations and large-file scaling. A separate Citsec HFT onsite round in Singapore asked the analogous `tail -n` over a very large file.

---

## 27. Best Time to Buy and Sell Stock — Full DP Ladder

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-07
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, All
- **tags**: dp, array, state-machine, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/stock-trading-dp-ladder
- **related discussions**: 3

### Statement

Phone-screen staple at Citsec: start with the single-transaction max profit, ladder up through unlimited transactions, then bounded-k transactions, and on some loops add cooldown / transaction-fee variants. Interviewers grade as much on the generalization narrative (state definition, complexity, edge cases) as on the final code.

---

## 28. Merge K Sorted Streams (OOD + Algorithm)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: heap, merge, data-structure, code-reading, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/merge-k-sorted-streams-ood
- **related discussions**: 2

### Statement

Two flavors of merge-K appear in Citsec phone screens: a C++ OOD variant where the candidate fills in an abstract `Stream` class given a template heavy with `shared_ptr` and type aliases, and a tighter algorithm-only variant with a timestamp tie-break that nudges the standard LC 23 solution.

---

## 29. Fleet Configuration and Deployment Control Plane

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-08-05
- **stage**: tech-screen
- **access**: Members
- **roles**: Infra Eng
- **tags**: distributed-systems, cloud, sre
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/sre-configuration-deployment-control-plane
- **related discussions**: 1

### Statement

Design a system for safely changing configuration and deploying 200+ services across multiple data centers and cloud regions, with rollback support. The starting point mixes checked-in property files, hand-managed environment variables, a directly edited shared NFS mount, and SSH-driven deployment scripts.

---

## 30. Metrics Collection & Alerting System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-07-08
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: metrics, distributed-systems, hard
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/metrics-alerting-system-design
- **related discussions**: 1

### Statement

Citsec C++ SWE onsite design round: build an end-to-end metrics and alerting pipeline — instrumentation on a machine fleet, collection, time-series storage, a serving/query layer, threshold alerting, and per-worker failure detection. Storage-engine choice (time-series / SSTable vs. relational) and the failure-detection mechanism are the probed decision points; a ~2-second query-latency requirement surfaces mid-round.

---

## 31. Behavioral / Resume Drilling Across the Loop

- **category**: behavioral
- **duration**: 30 min
- **frequency**: High
- **last asked**: 2026-09-08
- **stage**: recruiter-screen · phone-screen · onsite-bq · final-round
- **access**: Members
- **roles**: All
- **tags**: behavioral, deep-dive, why-company, culture-fit, fit
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/citadel-bq-resume-loop
- **related discussions**: 14

### Statement

Citadel and Citsec both front-load resume / behavioral content into nearly every round. Phone screens dedicate 10-30 minutes to resume drilling before coding; onsite has dedicated team-fit / culture rounds; the loop typically closes with a final leadership call on the Citadel side.

---

## 32. GQS SWE Whiteboard: Boundary Search + Sliding-Window Top K

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-30
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, array, sliding-window, top-k, data-structure, bst, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/gqs-swe-whiteboard-boundary-search-top-k
- **related discussions**: 1

### Statement

Discussion-only GQS SWE phone screen with a Quant Developer interviewer. The first problem asks for all values equal to `K` in a sorted array under progressively tighter binary-search API constraints; the second asks how to maintain the top `K` elements inside each sliding window and drives toward an ordered data structure supporting insert, erase, and traversal.

---

## 33. When Does `min x'Qx + c'x` Have a Finite Minimum?

- **category**: other
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-04-28
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, RS
- **tags**: math-reasoning, linear-algebra, optimization, hard
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/quadratic-form-finite-min
- **related discussions**: 1

### Statement

Citadel GQS Quant Researcher first round: after 30 minutes of resume discussion, a single technical question on the conditions under which an unconstrained quadratic program over `R^n` attains a finite minimum.

---

## 34. EQR Alpha-Factor Research Deep-Dive + LLM Inference Stability

- **category**: other
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2025-12-16
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, RS
- **tags**: deep-dive, ml-knowledge, math-reasoning, probability, llm, hard
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/eqr-alpha-factor-deep-dive
- **related discussions**: 1

### Statement

Citsec EQR (NG QR) round combining resume-grade alpha-research drilling with a quantitative case study on LLM inference noise. Scheduled as 45 minutes; ran 75 minutes. Six themed sub-prompts plus a single live coding closer.

---

## 35. Trading OA — 15-Question Probability Quickfire (30 Minutes)

- **category**: other
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-11-21
- **stage**: oa
- **access**: Members
- **roles**: MLE, RS, All
- **tags**: oa, probability, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/trading-probability-quickfire
- **related discussions**: 1

### Statement

Citadel Trading OA format: 15 probability / expected-value questions in 30 minutes, averaging 2 minutes per question. Standard quant-interview probability staples (color-pair runs, uniform-pair differences, binary-choice population balancing) at a pace that rewards pattern recognition over derivation.

---

## 36. GQS Quant Phone — Correlation Range + Box-Muller-Style Sampling

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-28
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, RS
- **tags**: probability, linear-algebra, math-reasoning, sampling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/citadel/gqs-quant-phone-brainteasers
- **related discussions**: 2

### Statement

Citadel GQS Quant Researcher phone screen: short brainteaser-style probability questions delivered after a deep resume drill. Reported questions cover the achievable range of `corr(Y, Z)` given two other pairwise correlations, and how to transform `Uniform(0, 1)` samples into `Normal(0, 1)`.

---
