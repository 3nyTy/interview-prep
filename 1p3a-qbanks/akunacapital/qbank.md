# Akuna Capital — 1point3acres Question Bank (25 curated questions)

Source: 1point3acres qbank (`company=akunacapital`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/akunacapital>

The site lists 59 entries for this company: **25 curated questions** (below), 34 OJ links, 0 external links. Only the curated 25 are captured here.

> Akuna Capital interviews lean heavily into the firm’s trading DNA: fast arithmetic, probability, market-making intuition, and implementation details that resemble exchange or trading-engine code. Quant roles can move through multiple asynchronous assessments before a superday, while developer tracks mix HackerRank-style coding, C++ debugging, data-structure design, and practical rate-limit or order-book exercises.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Minimum Swaps to Sort (Cycle Decomposition)
2. Rolling Statistics: Max, Mean, and Mode
3. Communications Handler (CommsHandler)
4. Exchange Order Matching Engine
5. Array Challenge: Left-Comparison Running Counter
6. SQL: Cloud Resource Utilization Dashboard
7. Portfolio Rebalancer
8. Maximum Difference Across Connected Components
9. Moving Average Signal System
10. Sliding-Window Order Message Rate Limiter
11. Count Set Bits in an Integer Array
12. Fun With Anagrams (Deduplicate Anagrams)
13. Enemy Factory with Shared Instances
14. Find Missing and Repeated Element
15. Minimum Days to Release Updates
16. Max Subsequence Sum Without Skipping Two in a Row
17. Maximum Distinct Elements After K Swaps
18. QR HackerRank: Profitable Pairs and Delivery Order
19. C++ Object Pool Debugging
20. Round Price to Nearest Valid Tick
21. Track Best Bid/Ask with Cancellations
22. Segregate Binary String (Move Ones to End)
23. Junior Trader Final: Investment & Market-Making Games
24. Trader Fair-Bet Probability and Number Sequences
25. QR Video OA: Advanced Math and Reasoning

---

## 1. Minimum Swaps to Sort (Cycle Decomposition)

- **category**: coding
- **frequency**: Medium
- **last asked**: 2025-12-20
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: cycle-detection, sorting, array, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/minimum-swaps-to-sort-cycle
- **related discussions**: 4

### Statement

Return the minimum number of swaps (any two positions) needed to sort the array. Some sittings sort ascending, some descending, and some include duplicates. Answer = n minus the number of cycles in the permutation that maps each element to its target position.

---

## 2. Rolling Statistics: Max, Mean, and Mode

- **category**: coding
- **frequency**: Medium
- **last asked**: 2025-11-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: streaming, sliding-window, monotonic-stack, hashmap, heap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/rolling-statistics-max-mean-mode
- **related discussions**: 5

### Statement

Implement a streaming statistics class with insert, max, mean, and mode; then extend it to the most recent K numbers with efficient expiry.

---

## 3. Communications Handler (CommsHandler)

- **category**: coding
- **frequency**: Medium
- **last asked**: 2025-11-04
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, state-machine, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/communications-handler
- **related discussions**: 4

### Statement

Implement a CommsHandler (subclassing an abstract base) plus a custom exception that manage one active call between exactly two Callers. Support connect, hangup, and clear_all, raising on self-connect, a busy line, or hanging up a pair that is not connected; return exact status strings.

---

## 4. Exchange Order Matching Engine

- **category**: coding
- **frequency**: Medium
- **last asked**: 2025-10-13
- **stage**: oa
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: trading, data-structure, queue, sorting, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/exchange-order-matching-engine
- **related discussions**: 4

### Statement

Implement an order book that supports BUY, SELL, CANCEL, MODIFY, and PRINT commands, with IOC/GFD order types and order-id based updates.

---

## 5. Array Challenge: Left-Comparison Running Counter

- **category**: coding
- **frequency**: Medium
- **last asked**: 2025-09-24
- **stage**: oa
- **access**: Members
- **roles**: RS, SWE
- **tags**: prefix-sum, array, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/left-comparison-running-counter
- **related discussions**: 4

### Statement

For each i, start a counter at 0 and compare a[i] against every left element a[j] (j<i): add |a[i]-a[j]| if a[j]<a[i], subtract it if a[j]>a[i]. Return the array of counters. The signs cancel so result[i] = i*a[i] - prefix_sum(0..i-1), giving an O(n) solution.

---

## 6. SQL: Cloud Resource Utilization Dashboard

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: sql, data-engineering, easy
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/sql-cloud-resource-utilization-dashboard
- **related discussions**: 1

### Statement

Write one SQL query merging CPU and memory metric tables into a unified utilization view: resource_type, application_id, and rounded average usage, filtered to a month and a >50% average threshold.

---

## 7. Portfolio Rebalancer

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, hashmap, trading, easy
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/portfolio-rebalancer
- **related discussions**: 1

### Statement

Implement an object model for portfolio rebalancing: given current and target percentage allocations behind an IPortfolio interface, return the per-asset percentage to buy (positive) or sell (negative).

---

## 8. Maximum Difference Across Connected Components

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: graph, bfs, union-find, easy
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/maximum-difference-connected-components
- **related discussions**: 1

### Statement

Given an undirected graph by node count and edge list, find each connected component, and return the maximum over components of (largest node id - smallest node id).

---

## 9. Moving Average Signal System

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: streaming, sliding-window, trading, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/moving-average-signal-system
- **related discussions**: 1

### Statement

Design a system that computes a moving average from market data and uses a fixed threshold to generate a trading signal.

---

## 10. Sliding-Window Order Message Rate Limiter

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: onsite-coding
- **access**: Free preview
- **roles**: SWE, Infra Eng
- **tags**: rate-limiting, sliding-window, queue, trading, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/sliding-window-order-message-rate-limiter
- **related discussions**: 1

### Statement

Design and implement a reusable class that decides how many order messages can be sent without breaching a moving-window rate limit such as 50 messages per 1000 ms.

---

## 11. Count Set Bits in an Integer Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: bit-manipulation, array, performance-modeling, easy
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/count-set-bits-in-integer-array
- **related discussions**: 1

### Statement

Implement a low-level function such as `uint64_t count_bits(int* arr, uint64_t size)` that returns the total number of set bits across an integer array.

---

## 12. Fun With Anagrams (Deduplicate Anagrams)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-04
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, string, sorting, dedup, easy
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/fun-with-anagrams-dedup
- **related discussions**: 1

### Statement

Given an array of strings (function funWithAnagrams), remove every string that is an anagram of an earlier kept string (keep the first occurrence), then return the remaining strings in lexicographic order. Anagram key = sorted characters or a character-frequency signature.

---

## 13. Enemy Factory with Shared Instances

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/enemy-factory-shared-instances
- **related discussions**: 1

### Statement

Design a factory that returns an enemy instance for a requested color (the enemy's only property). Follow-up: enemy objects are memory-heavy and created in huge numbers — redesign to mitigate. The intended answer is the Flyweight pattern: cache and share one immutable instance per color instead of allocating new ones.

---

## 14. Find Missing and Repeated Element

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-09-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bit-manipulation, math, array, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/find-missing-and-repeated-element
- **related discussions**: 1

### Statement

Given an array of 1..n with exactly one value missing and one duplicated, return both. A long prose setup hides a missing-and-repeated-number problem; the expected approach is XOR in O(1) extra space, then write your own tests and discuss alternative (sum / sum-of-squares) methods.

---

## 15. Minimum Days to Release Updates

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-10-29
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: greedy, sorting, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/minimum-days-to-release-updates
- **related discussions**: 3

### Statement

Given plannedDate[] and alternateDate[] for n updates that must launch in planned order (each on its planned or alternate day, multiple per day allowed), return the minimum number of days to release all updates. Greedy after sorting by planned date.

---

## 16. Max Subsequence Sum Without Skipping Two in a Row

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-10-29
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: dp, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/max-sum-no-two-consecutive-skips
- **related discussions**: 2

### Statement

Choose a subsequence to maximize the sum where you may skip elements but never two consecutive ones. Two-state DP: take[i] = val[i] + max(take[i-1], skip[i-1]); skip[i] = take[i-1]; answer = max(take[-1], skip[-1]).

---

## 17. Maximum Distinct Elements After K Swaps

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-10-29
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: hashmap, greedy, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/maximum-distinct-after-k-swaps
- **related discussions**: 3

### Statement

Given arrays a and b of n integers and at most k swaps (any a[i] with any b[j]), return the maximum number of distinct elements achievable in a. Greedy: spend swaps replacing duplicate slots in a with values from b that are not yet present in a.

---

## 18. QR HackerRank: Profitable Pairs and Delivery Order

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-10-21
- **stage**: oa
- **access**: Members
- **roles**: RS, SWE
- **tags**: sorting, two-pointer, graph, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/qr-hackerrank-profitable-pairs-and-delivery-order
- **related discussions**: 3

### Statement

A two-question HackerRank OA: count positive-sum project pairs after subtracting costs, then return city delivery order by graph distance with id tie-breaks.

---

## 19. C++ Object Pool Debugging

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2025-10-05
- **stage**: oa
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: code-reading, debugging, oop, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/c-plus-plus-object-pool-debugging
- **related discussions**: 2

### Statement

A 45-minute C++ OA2 task gives a mostly written object-pool class that does not compile; candidates repair syntax/logic bugs and implement `numAllocated()`.

---

## 20. Round Price to Nearest Valid Tick

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-09-22
- **stage**: oa
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: math, simulation, trading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/round-price-to-nearest-tick
- **related discussions**: 2

### Statement

Given price bands (each with a lower bound, upper bound, and tick width), snap an input price to the nearest valid tick. If the price is below the minimum, return the minimum; within a band, return low + n*width minimizing |price - (low + n*width)|. Inputs are doubles and prices may be negative.

---

## 21. Track Best Bid/Ask with Cancellations

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-09-22
- **stage**: oa
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: order-book, heap, data-structure, trading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/track-best-bid-ask-with-cancels
- **related discussions**: 2

### Statement

Process buy, sell, and cancel operations and maintain the highest buy price and the best sell price, each with its aggregated demand/quantity. Buy and sell sides are tracked independently with no crossing or fills. A max-heap (or ordered map) per side with cancellation support gives the current best.

---

## 22. Segregate Binary String (Move Ones to End)

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-08-26
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: greedy, prefix-sum, binary-string, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/segregate-binary-string-move-ones
- **related discussions**: 2

### Statement

Given a binary string, move each '1' right (until it hits the end or another '1') so all 1s end up segregated to the right. Return the total cost, where cost is the number of positions each '1' moves. Equivalent to counting (1,0) inversions / adjacent swaps to group the ones.

---

## 23. Junior Trader Final: Investment & Market-Making Games

- **category**: other
- **frequency**: Single report
- **last asked**: 2025-09-16
- **stage**: final-round
- **access**: Members
- **roles**: All
- **tags**: brainteaser, market-making, expected-value, fermi-estimation, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/junior-trader-final-games
- **related discussions**: 1

### Statement

Junior Trader final-round verbal games: (1) how to invest $1M over a 3-day vs a 3-year horizon; (2) a dice game rolling 1-100 with a $1 reroll option — how much would you pay to play and what is your strategy; (3) make a market on a quantity such as the population of Sydney or Chicago. Graded on reasoning speed and logic, not theory.

---

## 24. Trader Fair-Bet Probability and Number Sequences

- **category**: other
- **frequency**: Low
- **last asked**: 2025-11-20
- **stage**: oa
- **access**: Members
- **roles**: All
- **tags**: probability, math, math-reasoning, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/trader-fair-bet-probability-and-number-sequences
- **related discussions**: 2

### Statement

Trader and Sneak Peek assessments ask fast probability-to-fair-bet conversions, mental arithmetic, and sequence-recognition questions under tight time limits.

---

## 25. QR Video OA: Advanced Math and Reasoning

- **category**: other
- **frequency**: Low
- **last asked**: 2025-10-20
- **stage**: oa
- **access**: Members
- **roles**: RS
- **tags**: math, probability, linear-algebra, math-reasoning, verbal, hard
- **url**: https://www.1point3acres.com/interview/problems/company/akunacapital/qr-video-oa-advanced-math-and-reasoning
- **related discussions**: 2

### Statement

A six-question QR video OA gives about five minutes per question and asks candidates to explain math, probability, Markov-chain, calculus, and linear-algebra reasoning on camera.

---
