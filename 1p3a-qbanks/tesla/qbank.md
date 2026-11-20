# Tesla — 1point3acres Question Bank (23 curated questions)

Source: 1point3acres qbank (`company=tesla`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/tesla>

The site lists 55 entries for this company: **23 curated questions** (below), 32 OJ links, 0 external links. Only the curated 23 are captured here.

> Tesla interviews are highly team-specific: Autopilot, Optimus, Dojo, data engineering, infotainment, and factory-software teams each run their own practical technical screens. The common pattern is a fast transition from resume/project discussion into runnable code, with interviewers pushing for optimality, clean implementation style, and concrete ownership of past technical decisions.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. NumPy Point-to-Plane Distance and Robust Plane Fitting
2. Bulls and Cows with Per-Position Match Signal
3. Task Scheduler with Timed Execution and Dynamic Insertion
4. Data Cleaning Pipeline and SQL Analytics Screen
5. Speed-Limit RL Reward from Trajectory Samples
6. Batched Trajectory Suffix Sum
7. Parallel Runner with Exclusive Targets A and B
8. First Solar Panel Placement in a Binary Grid
9. Robot Room Navigation Take-Home
10. Debug Dijkstra Shortest Path for Navigation
11. Priority + Expiration + LRU Eviction
12. Attention / Transformer Module with Backward Pass
13. Trajectory Waypoint Distance Queries
14. Dojo Pythonic Coding Pair: Permutation Check and Pow
15. Rank Vehicles for OTA Peer-to-Peer Seeding
16. Basic Calculator with Operators, Variables, and Functions
17. Event Bus with Emit, Subscribe, and Unsubscribe
18. Rollback-Capable KV Store with Nested Transactions
19. NumPy Conv2D Forward and Parameter Count
20. Word Ladder Variants with Trie Optimization
21. Ticketmaster-Style Seat Booking Design
22. CV / Sensing ML Fundamentals Oral
23. Battery Cell Quality Statistical Case

---

## 1. NumPy Point-to-Plane Distance and Robust Plane Fitting

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-09
- **stage**: tech-screen
- **access**: Members
- **roles**: mle
- **tags**: numpy, computer-vision, linear-algebra, linear-regression, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/numpy-point-plane-distance-robust-fitting
- **related discussions**: 1

### Statement

Given 3D points and a plane represented by `a*x + b*y + c*z + d = 0`, compute every point's distance to the plane in NumPy. Then estimate the dominant plane when roughly 70% of the points lie on it an...

---

## 2. Bulls and Cows with Per-Position Match Signal

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-06-03
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: string, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/bulls-and-cows-position-signal
- **related discussions**: 1

### Statement

Coding round on a Bulls-and-Cows / Guess-the-Word matching problem: given a target and a guess of equal length, emit a per-position signal marking exact matches, present-but-misplaced characters, and ...

---

## 3. Task Scheduler with Timed Execution and Dynamic Insertion

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-27
- **stage**: phone-screen
- **access**: Members
- **roles**: infra-eng, swe
- **tags**: scheduling, heap, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/task-scheduler-timed-execution
- **related discussions**: 1

### Statement

Infra coding screen: implement a task scheduler that runs each task at its designated time, while allowing new tasks to be added at any point. The code must actually run.

---

## 4. Data Cleaning Pipeline and SQL Analytics Screen

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-27
- **stage**: phone-screen
- **access**: Members
- **roles**: ds
- **tags**: data-cleaning, sql, data-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/data-engineering-cleaning-and-sql-screen
- **related discussions**: 1

### Statement

Data Engineer first-round screen combining a noisy CSV cleaning pipeline with three SQL tasks: monthly-reset cumulative sales, recursive employee hierarchy, and conditional order aggregation.

---

## 5. Speed-Limit RL Reward from Trajectory Samples

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, rs
- **tags**: reinforcement-learning, numpy, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/speed-limit-rl-reward
- **related discussions**: 1

### Statement

Implement a reward function that penalizes speed-limit violations from raw trajectory samples shaped `[batch, num_waypoint, 2]` sampled at 10 Hz, then reason about state-dependent speed limits.

---

## 6. Batched Trajectory Suffix Sum

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, rs
- **tags**: numpy, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/trajectory-suffix-sum
- **related discussions**: 1

### Statement

Autopilot AI Foundation coding task over trajectory tensors: given input shaped `[batch, num_waypoint, 2]`, compute a suffix-style aggregate with output shaped `[batch, num_waypoint]`.

---

## 7. Parallel Runner with Exclusive Targets A and B

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-15
- **stage**: phone-screen
- **access**: Members
- **roles**: infra-eng, swe
- **tags**: concurrency, parallelism, multiprocessing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/parallel-target-runner
- **related discussions**: 1

### Statement

Concurrency prompt for infra / GPU-adjacent teams: start 10 parallel copies of `main`; each copy must run target `A` or `B`, never more than one per target at a time, while keeping both targets maxima...

---

## 8. First Solar Panel Placement in a Binary Grid

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-10-31
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: grid, prefix-sum, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/solar-panel-placement-grid
- **related discussions**: 1

### Statement

Given a 2-D binary grid and a solar panel height / width, return the first coordinate where the panel can fit. `0` means empty, `1` means blocked; the round asks for brute force, implementation, tests...

---

## 9. Robot Room Navigation Take-Home

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-09-24
- **stage**: take-home
- **access**: Members
- **roles**: swe
- **tags**: grid, simulation, parsing, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/robot-room-navigation-takehome
- **related discussions**: 1

### Statement

Factory-monitoring backend take-home: parse a room map from stdin and simulate a robot moving from start to finish through obstacles, direction-changing tags, jumpers, and configurable search-directio...

---

## 10. Debug Dijkstra Shortest Path for Navigation

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-23
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: dijkstra, graph, debugging, code-review, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/dijkstra-code-review-navigation
- **related discussions**: 1

### Statement

Intern infotainment technical round: review a Java shortest-path finder for a navigation system, identify bugs in its Dijkstra implementation, analyze complexity, and discuss alternatives for negative...

---

## 11. Priority + Expiration + LRU Eviction

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-19
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: data-structure, heap, ttl, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/priority-expire-cache-eviction
- **related discussions**: 1

### Statement

Implement `evictItem()` for a cache-like class that already has `set(key, value, priority, expiretime)` and `get(key)`. Expired items are evicted first; otherwise lowest priority wins; ties break by l...

---

## 12. Attention / Transformer Module with Backward Pass

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-11
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, rs
- **tags**: attention, transformer, autograd, numpy, hard
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/autoregressive-attention-transformer-backward
- **related discussions**: 1

### Statement

ML coding round on autoregressive architectures: implement Attention and Transformer components, then complete the backward pass around attention when softmax backward is provided.

---

## 13. Trajectory Waypoint Distance Queries

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-11
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, rs
- **tags**: array, binary-search, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/trajectory-waypoint-distance-queries
- **related discussions**: 1

### Statement

Given a 2-D coordinate sequence and a list of travel distances in miles, determine for each distance whether any coordinate point lies within a threshold of the current traveled position.

---

## 14. Dojo Pythonic Coding Pair: Permutation Check and Pow

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-07-25
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: array, bit-manipulation, recursion, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/dojo-permutation-and-pow
- **related discussions**: 1

### Statement

Dojo phone screen with two compact coding tasks: verify whether a list is a permutation of `[0, 1, ..., n-1]` and implement `pow(a: float, b: int)`. The interviewer pushes hard on optimal memory and P...

---

## 15. Rank Vehicles for OTA Peer-to-Peer Seeding

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-07-24
- **stage**: phone-screen
- **access**: Free preview
- **roles**: SWE
- **tags**: graph, hashmap, dedup, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/ota-p2p-vehicle-seeding-rank
- **related discussions**: 1

### Statement

Implement `rank_vehicles` for a Tesla OTA update network. Vehicles share unique 10MB chunks over WiFi; rank vehicles by historical direct and indirect contribution to peer-to-peer distribution so cellular seed downloads can be minimized.

---

## 16. Basic Calculator with Operators, Variables, and Functions

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-07-24
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: parsing, stack, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/basic-calculator-extended-language
- **related discussions**: 1

### Statement

Start from a Basic Calculator-style expression evaluator, then discuss extensions: exponentiation, parentheses, boolean operators, bitwise operators, variable declarations, and function declarations.

---

## 17. Event Bus with Emit, Subscribe, and Unsubscribe

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-07-04
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: object-design, hashmap, async, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/event-bus-emit-subscribe
- **related discussions**: 1

### Statement

Object-design coding screen: define a listener interface and an event bus class with `emit` and `subscribe`, then support multiple listeners per event and unsubscribe.

---

## 18. Rollback-Capable KV Store with Nested Transactions

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-30
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, mle
- **tags**: data-structure, hashmap, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/rollback-kv-store-nested-transactions
- **related discussions**: 2

### Statement

Design and implement a key-value store that supports normal set / get / delete operations plus rollback of in-progress transactions. The Optimus variant specifically requires nested transaction rollba...

---

## 19. NumPy Conv2D Forward and Parameter Count

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2025-11-22
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: mle
- **tags**: numpy, ml-knowledge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/numpy-conv2d-forward
- **related discussions**: 2

### Statement

Implement a Conv2D forward pass in NumPy without using deep-learning framework kernels. The round can also ask for output-shape reasoning, total parameter count, bias handling, and vectorization.

---

## 20. Word Ladder Variants with Trie Optimization

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2025-08-21
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: bfs, trie, string, hard
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/word-ladder-variants
- **related discussions**: 2

### Statement

Fullstack / intern coding prompt around Word Ladder-style string transformations. One version asks for all shortest sequences; another uses fuzzy string matching with a prefix-tree optimization.

---

## 21. Ticketmaster-Style Seat Booking Design

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-11
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: system-design, transactions, concurrency, frontend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/ticketmaster-double-booking-design
- **related discussions**: 1

### Statement

Phone-screen system design: design a Ticketmaster-like seat booking flow with a heavy focus on preventing double booking, API semantics, frontend seat-map behavior, and user experience during contenti...

---

## 22. CV / Sensing ML Fundamentals Oral

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-16
- **stage**: phone-screen
- **access**: Members
- **roles**: mle, swe
- **tags**: ml-knowledge, cnn, computer-vision, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/cv-sensing-ml-fundamentals-oral
- **related discussions**: 1

### Statement

Sensing / computer-vision engineer phone screen mixing a quick coding warm-up with rapid-fire ML fundamentals: CNN vs MLP, how ResNet works, activation-function choices, and basic probability.

---

## 23. Battery Cell Quality Statistical Case

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-16
- **stage**: phone-screen
- **access**: Members
- **roles**: ds
- **tags**: experiment-design, probability, data-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/tesla/battery-cell-quality-statistical-case
- **related discussions**: 1

### Statement

Data Engineer / Cell Integrity & Quality case: five randomly assigned battery groups each undergo three pass/fail tests; identify which group has the best overall quality and justify the statistical m...

---
