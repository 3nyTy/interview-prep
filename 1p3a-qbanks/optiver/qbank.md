# Optiver — 1point3acres Question Bank (29 curated questions)

Source: 1point3acres qbank (`company=optiver`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/optiver>

The site lists 61 entries for this company: **29 curated questions** (below), 32 OJ links, 0 external links. Only the curated 29 are captured here.

> Optiver is a market-making trading firm whose interview process is built to find fast, calibrated decision-makers under pressure. The funnel is dominated by a heavy online assessment — a probability/sequence/reaction battery for quant roles and OOP simulation coding for engineers — and the quant loop centers on a live EV betting game where speed and bet-sizing matter more than ending profit.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Stock Transaction Sequence Counting (Catalan / DP)
2. Proportional Allocation Strategy Backtest
3. Squirrel Nut Storage Tracker
4. News Aggregation / Subscription System
5. Days Between Two Dates
6. Build Binary Tree from Edges / S-Expression
7. Order Book Matching Simulation
8. Optimal Execution with a Broker Backstop
9. Hot Air Balloon Festival Simulation
10. PowerBank Rack & Cell Management
11. NASDAQ Market Data Price-Level Book
12. Design Circular Queue (Ring Buffer)
13. Multicore Overheat Prevention Controller
14. Thread-Safe Buy/Sell Without Overselling
15. OptiCargo — Cargo-to-Flight Scheduling
16. Design a Queue — Implementation Trade-offs
17. Currency Arbitrage Detection
18. Satellite Message Propagation
19. Low-Latency Trading System Design
20. HR / Behavioral Phone Screen
21. Zap-N Reaction & Memory Mini-Games
22. Beat the Odds — Probability Test
23. Trading Betting Game (EV / Market-Making)
24. Number Logic — Sequence / Find the Pattern
25. QR Take-Home Data Project & Presentation
26. Programming & Computer-System Knowledge Quiz
27. QR Technical Round — Brainteasers & Estimation
28. Market-Making Interval Estimation Game
29. Likelihood / Event-Ordering Test

---

## 1. Stock Transaction Sequence Counting (Catalan / DP)

- **category**: coding
- **duration**: 90 min
- **frequency**: High
- **last asked**: 2026-08-08
- **stage**: oa
- **access**: Members
- **roles**: RS, SWE, DS
- **tags**: dp, combinatorics, math, trading, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/stock-transaction-counting
- **related discussions**: 12

### Statement

A HackerRank counting problem framed as market-making transactions: count the number of valid buy/sell sequences that start and end flat without ever going negative. The canonical version is a Catalan number; common variants generalize to bounded transactions or arbitrary order sizes and are solved with DP.

---

## 2. Proportional Allocation Strategy Backtest

- **category**: coding
- **duration**: 90 min
- **frequency**: High
- **last asked**: 2026-08-08
- **stage**: oa
- **access**: Members
- **roles**: RS, SWE, DS
- **tags**: simulation, trading, array, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/proportional-allocation-backtest
- **related discussions**: 11

### Statement

A HackerRank simulation: backtest a momentum strategy that allocates capital each day proportionally to the previous day's positive returns, then report the average daily log return and the standard deviation of daily log returns. Light on algorithms, heavy on careful bookkeeping.

---

## 3. Squirrel Nut Storage Tracker

- **category**: coding
- **duration**: 75 min
- **frequency**: Medium
- **last asked**: 2026-08-17
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, data-structure, oa, hard
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/squirrel-nut-storage
- **related discussions**: 6

### Statement

An OOP simulation OA: model cone-shaped hiding locations whose level capacities grow Fibonacci-style, then support hiding nuts (deepest-level-first, with expiry) and retrieving them under a layered weight/percentage rule with a fall-down refill mechanic. Heavy on careful rule implementation.

---

## 4. News Aggregation / Subscription System

- **category**: coding
- **duration**: 120 min
- **frequency**: Medium
- **last asked**: 2026-07-27
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, sliding-window, hashmap, heap, oa, hard
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/news-subscription-system
- **related discussions**: 7

### Statement

The flagship SWE/SDE OA: implement a NewsProvider class that registers topic subscriptions with interest thresholds and per-second rate limits, ingests timestamped news, and publishes the right news to the right subscribers under sliding-window and priority rules. Most sittings allow about 75–90 minutes; a senior SWE variant allows two hours.

---

## 5. Days Between Two Dates

- **category**: coding
- **duration**: 72 min
- **frequency**: Medium
- **last asked**: 2026-07-16
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: math, string, easy, oa
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/days-between-dates
- **related discussions**: 6

### Statement

A warm-up OA problem (often the first of the experienced/campus SWE OA, including a C variant): compute the number of days between two given calendar dates, handling months of varying length and leap years.

---

## 6. Build Binary Tree from Edges / S-Expression

- **category**: coding
- **duration**: 120 min
- **frequency**: Medium
- **last asked**: 2026-07-16
- **stage**: oa · take-home
- **access**: Members
- **roles**: SWE
- **tags**: tree, parsing, simulation, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/binary-tree-from-edges
- **related discussions**: 7

### Statement

An OA coding problem (experienced-SWE rotation): given parent-child pairs as raw strings, validate the input against a priority-ordered list of error types and, if valid, output the tree's S-expression. The input validation is deliberately fiddly.

---

## 7. Order Book Matching Simulation

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2025-11-18
- **stage**: oa
- **access**: Members
- **roles**: RS, SWE, DS
- **tags**: heap, order-book, trading, simulation, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/order-book-matching
- **related discussions**: 4

### Statement

A HackerRank matching-engine simulation: process a stream of buy/sell limit orders against an order book with price-time priority and return the sum of all executed transaction prices. Naturally solved with two heaps.

---

## 8. Optimal Execution with a Broker Backstop

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2025-10-24
- **stage**: oa
- **access**: Members
- **roles**: RS, DS
- **tags**: dp, expected-value, trading, oa, hard
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/optimal-execution-broker
- **related discussions**: 4

### Statement

A HackerRank optimal-stopping / DP problem: buy n units one at a time, each at an i.i.d. price uniform on [a,b]; at each step you may accept the observed price or fall back to a broker's fixed price p, usable at most k times. Return the minimum expected total execution price under the optimal policy.

---

## 9. Hot Air Balloon Festival Simulation

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2025-09-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/balloon-festival-simulation
- **related discussions**: 5

### Statement

An OOP simulation OA: track balloons ascending/descending, compute additive wind speed at each balloon's altitude via a given formula, manage a stability rule (unstable above 15 m/s, regains stability after 300 s of calm), and report the stable balloons at/above the highest stable altitude. Timestamps are strictly increasing.

---

## 10. PowerBank Rack & Cell Management

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-08-11
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, state-machine, data-structure, hard
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/powerbank-rack-cell-management
- **related discussions**: 1

### Statement

A 90-minute OOD simulation: implement a PowerBank with ordered capacity-constrained racks, time-dependent charged/depleted/spent cells, insertion, an equalization pass that swaps cells between adjacent racks, and a dispatch pass that drains charged cells while refilling vacancies.

---

## 11. NASDAQ Market Data Price-Level Book

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-06
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: order-book, data-structure, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/nasdaq-market-data-price-level-book
- **related discussions**: 1

### Statement

Maintain the AAPL order book from insert, modify, and cancel callbacks, then implement `GetPriceLevel(side, level_index)` to return the price and aggregate quantity at the requested zero-based bid or ask level.

---

## 12. Design Circular Queue (Ring Buffer)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: queue, data-structure, easy
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/circular-queue-design
- **related discussions**: 1

### Statement

A guided phone-screen coding problem equivalent to LeetCode 622: implement a fixed-capacity circular queue. The interviewer leads you to recognize that the scenario needs a ring-buffer design, then has you implement it.

---

## 13. Multicore Overheat Prevention Controller

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-08-18
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, state-machine, hard
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/multicore-overheat-prevention-controller
- **related discussions**: 3

### Statement

Implement an OverheatPreventionController with lazy load changes, time-based thermal evolution under shared passive and per-core active cooling, automatic shutdown and restart rules, and status-delta output. The central complication is a cascading cooling decision: engaging active cooling reduces shared passive efficiency and can force additional cores over the engagement threshold.

---

## 14. Thread-Safe Buy/Sell Without Overselling

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: concurrency, threading, oop-design, distributed-systems
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/multithreaded-buy-sell-lock
- **related discussions**: 2

### Statement

A phone-screen coding round: implement buy and sell functions keyed by client, symbol, and quantity while ensuring concurrent requests can never oversell inventory. The follow-up extends the design to multiple buy/sell systems connected to multiple exchanges.

---

## 15. OptiCargo — Cargo-to-Flight Scheduling

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: greedy, scheduling, simulation, code-reading, hard
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/opticargo-flight-scheduling
- **related discussions**: 2

### Statement

A phone/onsite engineering round (also an intern super-day round): given a long provided skeleton, implement order/cargo assignment to flights respecting departure-time and capacity constraints, with cargo splittable across planes. Heavy on reading large code, reasoning about correctness, and follow-up debugging/optimization.

---

## 16. Design a Queue — Implementation Trade-offs

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2025-12-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, data-structure, queue
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/design-queue-tradeoffs
- **related discussions**: 2

### Statement

A phone-screen OOD round: design a queue and discuss how different internal implementations (array vs. linked list, circular buffer, etc.) trade off against each other in performance.

---

## 17. Currency Arbitrage Detection

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2025-08-26
- **stage**: oa
- **access**: Members
- **roles**: RS, SWE, DS
- **tags**: graph, bellman-ford, trading, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/currency-arbitrage-detection
- **related discussions**: 2

### Statement

A HackerRank graph problem: given an n×n matrix of currency exchange rates, decide whether an arbitrage cycle exists after accounting for a small per-cycle transaction fee. The classic reduction is negative-cycle detection via Bellman-Ford on log-transformed rates.

---

## 18. Satellite Message Propagation

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2025-08-05
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: graph, bfs, simulation, oop-design, oa, hard
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/satellite-message-propagation
- **related discussions**: 3

### Statement

A graph-simulation OA: model a satellite network where a message floods from Earth, each satellite forwards to its lower-id-first neighbors with a 10s-per-forward cost, then reports back to Earth after a 30s processing delay. Output the order in which satellites report back, with timing tie-breaks.

---

## 19. Low-Latency Trading System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: trading, networking, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/trading-system-design
- **related discussions**: 1

### Statement

An onsite/super-day system-design round (SWE intern loop): design a trading system given a colocated 'locker box' next to the exchange while your traders work in an office in another city. The discussion centers on the network/infrastructure between the two sites and leans heavily on TCP/IP knowledge.

---

## 20. HR / Behavioral Phone Screen

- **category**: behavioral
- **duration**: 30 min
- **frequency**: Medium
- **last asked**: 2025-12-13
- **stage**: recruiter-screen · phone-screen
- **access**: Members
- **roles**: SWE, RS, DS
- **tags**: behavioral, recruiter, why-company
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/hr-behavioral-screen
- **related discussions**: 5

### Statement

An early ~20–30 minute recruiter/behavioral call covering logistics (graduation, sponsorship, office preference), a project deep-dive with reflective follow-ups, and motivation questions. Largely standard and well-circulated.

---

## 21. Zap-N Reaction & Memory Mini-Games

- **category**: other
- **duration**: 30 min
- **frequency**: Very high
- **last asked**: 2026-08-17
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS, DS
- **tags**: reaction-speed, brainteaser, oa
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/zap-n-reaction-games
- **related discussions**: 16

### Statement

A battery of fast reaction / working-memory mini-games inside the OA. Reportedly up to nine games, though many candidates only see three. Each game accelerates as you progress; raw reaction speed and pattern recognition matter more than reasoning.

---

## 22. Beat the Odds — Probability Test

- **category**: other
- **duration**: 45 min
- **frequency**: Very high
- **last asked**: 2026-02-16
- **stage**: oa
- **access**: Free preview
- **roles**: RS, DS
- **tags**: probability, mental-math, expected-value, brainteaser, math-reasoning, oa
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/beat-the-odds-probability
- **related discussions**: 20

### Statement

The signature Optiver quant assessment: 30 multiple-choice probability questions, ~90 seconds each, no going back. You pick the answer closest to the true value, so speed and estimation matter more than exact computation. Wrong answers are penalized.

---

## 23. Trading Betting Game (EV / Market-Making)

- **category**: other
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-03-21
- **stage**: phone-screen · onsite-research
- **access**: Members
- **roles**: RS, DS
- **tags**: expected-value, probability, trading, mental-math, market-making
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/trading-betting-game
- **related discussions**: 10

### Statement

The core QR/QT interview round: a live betting game over many rounds. Starting from \$1000, you choose how much to wager across several sub-games (coin flips, dice sums, card products) given randomized odds each round, plus a market-making sub-game on the sum of three cards. The interviewer probes your sizing and reasoning; speed and decision-making under pressure matter more than ending profit.

---

## 24. Number Logic — Sequence / Find the Pattern

- **category**: other
- **duration**: 25 min
- **frequency**: High
- **last asked**: 2026-02-16
- **stage**: oa
- **access**: Members
- **roles**: RS, DS
- **tags**: pattern-recognition, math, mental-math, oa
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/number-logic-sequence
- **related discussions**: 14

### Statement

A no-calculator aptitude section: ~25–26 number sequences in 25 minutes, infer the rule and supply the next term. Scoring is +1 correct / −1 wrong. Most candidates report needing ~15 correct to pass comfortably.

---

## 25. QR Take-Home Data Project & Presentation

- **category**: other
- **duration**: 120 min
- **frequency**: Medium
- **last asked**: 2026-01-12
- **stage**: take-home · onsite-deep-dive
- **access**: Members
- **roles**: RS, DS
- **tags**: take-home, pandas, data-analysis, presentation
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/qr-take-home-data-project
- **related discussions**: 4

### Statement

Mid-loop QR step: a take-home data project (often a modeling/backtest task on provided data) followed, if you pass, by a presentation round where interviewers drill into your approach, your pandas/time-series handling, and project decisions.

---

## 26. Programming & Computer-System Knowledge Quiz

- **category**: other
- **duration**: 20 min
- **frequency**: Medium
- **last asked**: 2025-11-17
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: cs-fundamentals, networking, operating-systems, oa
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/coding-knowledge-test
- **related discussions**: 6

### Statement

A multiple-choice CS-fundamentals section in the SWE/SDE OA: ~20 questions in ~20 minutes covering networking, databases, operating systems, multithreading, data structures and complexity. Single- and multi-select; no going back.

---

## 27. QR Technical Round — Brainteasers & Estimation

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-14
- **stage**: onsite-research
- **access**: Members
- **roles**: RS
- **tags**: brainteaser, probability, fermi-estimation, math-reasoning
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/qr-technical-brainteasers
- **related discussions**: 1

### Statement

A traditional QR technical round mixing probability/statistics brainteasers with Fermi estimation: invariant-style stone games, expected waiting-time questions, and market-sizing with confidence intervals.

---

## 28. Market-Making Interval Estimation Game

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-06
- **stage**: phone-screen
- **access**: Members
- **roles**: RS, DS
- **tags**: market-making, fermi-estimation, expected-value
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/market-making-interval-game
- **related discussions**: 1

### Statement

A QR/QT interview game: across five rounds the interviewer asks a numeric estimation question and you respond with a closed interval [L, U]. You score 0 if the true answer falls outside your interval and L/U if it falls inside — so tighter honest intervals score higher. The target is reaching at least 2.0 total points.

---

## 29. Likelihood / Event-Ordering Test

- **category**: other
- **duration**: 25 min
- **frequency**: Low
- **last asked**: 2026-02-16
- **stage**: oa
- **access**: Members
- **roles**: RS, DS
- **tags**: probability, data-analysis, new-round, oa
- **url**: https://www.1point3acres.com/interview/problems/company/optiver/likelihood-ordering-test
- **related discussions**: 2

### Statement

A newer OA section (appearing in 2026 sittings): read a chart or data table and rank three events from most to least likely. About 15 questions at a fixed ~90 seconds each. It tests data intuition rather than exact calculation.

---
