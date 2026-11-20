# Capital One — 1point3acres Question Bank (44 curated questions)

Source: 1point3acres qbank (`company=capitalone`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/capitalone>

The site lists 113 entries for this company: **44 curated questions** (below), 69 OJ links, 0 external links. Only the curated 44 are captured here.

> Capital One's engineering loop is unusually predictable: a long CodeSignal OA gates everyone, then a single Power Day stitches four 45-60 minute rounds — coding, system design, case study, and behavioral — back to back. The technical bar is moderate but the case study and BQ rounds carry real weight, especially for MLE / Applied Research / Data Science where the business-context framing of a model decision is the differentiating signal.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Bank Class OOD: Deposit / Withdraw / Transfer
2. Pair Concatenation to Target
3. NYC Green Taxi Data Science Challenge
4. W-D-L Outcome Reordering
5. Outside-In String Reordering
6. Longest Same-Character Run
7. House Segments After Deletions
8. Even Digit Count
9. Laser Grid Robot Max Safe Run
10. Cumulative Views Reach Target Day
11. Beautify Houses to Strict Monotone
12. Odd Zero-Digit Count
13. Battery Usage with Recharge Cycle
14. AP Replacement Rounds
15. Lunar Phase State Lookup
16. ML Coding: Top-P (Nucleus) Sampling
17. ML Coding: Linear Regression from Scratch
18. Vowel-Wrapped Substring Middle Reverse
19. Train Schedule Nearest Departure
20. Color Match-Three Grid
21. Matrix Border Sort & Clockwise Fill
22. Dynamic Two-Array Pair Sum Queries
23. Tetris-Style Figure Placement
24. Repeated Leading-Nonzero Subtraction
25. Symmetric Triplets
26. Warehouse Round-Robin Allocation with Closures
27. Print All Root-to-Leaf Tree Paths
28. Sawtooth (Alternating Parity) Subarray Count
29. Matrix Expression Max Value
30. Matrix Commands: Reverse / Swap / Rotate
31. Online Banking Application (System Design)
32. Account Balance & Cross-Region Event Platform
33. Power Day Behavioral
34. Virtual Credit Card Validator (Case Study)
35. Amusement Park Profitability Case
36. Wheat Harvest Optimisation Case
37. Model Risk: VaR & Monte Carlo Orals
38. DS Role-Play: Explain Model Regression to PM
39. DS Business Analyst Case (P&L Decomposition)
40. Chatbot Case Study
41. AR Panel Talk Project Deep-Dive
42. MLE ML Knowledge Orals (Set A)
43. MLE Deployment & Latency (Set B)
44. Code Reading Pipeline Walkthrough

---

## 1. Bank Class OOD: Deposit / Withdraw / Transfer

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, object-design, payment, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/bank-class-ood-transactions
- **related discussions**: 4

### Statement

The default Power Day coding round. Design a small banking class that supports account creation, deposit, withdraw, and transfer-between-accounts. The interviewer adds requirements in stages — validation, multiple account types, transaction history, fraud flags, top-N activity ranking — and watches how cleanly the design extends.

---

## 2. Pair Concatenation to Target

- **category**: coding
- **duration**: 70 min
- **frequency**: Medium
- **last asked**: 2026-04-08
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: hashmap, string, string-processing, oa, codesignal, easy
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/pair-concatenation-to-target
- **related discussions**: 5

### Statement

Given a list of non-negative integers and a target integer, count the number of ordered index pairs whose decimal-string concatenation equals the target. Ordering matters; the same numeric value at different indices counts as distinct pairs.

---

## 3. NYC Green Taxi Data Science Challenge

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-28
- **stage**: take-home
- **access**: Members
- **roles**: DS
- **tags**: take-home, data-analysis, pandas, matplotlib, scikit-learn, distribution, clustering
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/nyc-green-taxi-data-science-challenge
- **related discussions**: 1

### Statement

Analyze September 2015 NYC green-taxi trips in a seven-day take-home: load and profile the data, examine trip-distance behavior by time and airport usage, model tip percentage, then complete one advanced extension in distributions, visualization, spatial search, anomaly detection, or an original direction.

---

## 4. W-D-L Outcome Reordering

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-07-19
- **stage**: oa
- **access**: Members
- **roles**: MLE
- **tags**: string, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/wdl-cyclic-reordering
- **related discussions**: 1

### Statement

Given a string containing only `W`, `D`, and `L`, reorder its characters by repeatedly emitting available symbols in `W`, `D`, `L` order while preserving the original count of each symbol.

---

## 5. Outside-In String Reordering

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-07-19
- **stage**: oa
- **access**: Members
- **roles**: MLE
- **tags**: string, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/outside-in-string-reordering
- **related discussions**: 1

### Statement

Reorder a string by taking characters alternately from its left and right ends: first character, last character, second character, second-to-last character, and so on until every character is used.

---

## 6. Longest Same-Character Run

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-05-31
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, string-processing, easy, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/longest-same-character-run
- **related discussions**: 1

### Statement

Given a lowercase string, find the longest contiguous substring made of one repeated character. If several runs have the same maximum length, return the rightmost run encoded as `<character><length>`.

---

## 7. House Segments After Deletions

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-05-31
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: set, interval-aggregation, simulation, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/house-segments-after-deletions
- **related discussions**: 1

### Statement

Given distinct house locations on a number line and a deletion order, return the number of remaining contiguous house segments after each deletion. Adjacent integer locations belong to the same segment.

---

## 8. Even Digit Count

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-05-31
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, easy, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/even-digit-count
- **related discussions**: 1

### Statement

Given an array of positive integers, count how many elements have an even number of decimal digits. This is a CodeSignal Q1-style warm-up where the intended solution can be direct string conversion or digit counting.

---

## 9. Laser Grid Robot Max Safe Run

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-05-31
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: grid, simulation, array, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/laser-grid-robot-max-safe-run
- **related discussions**: 1

### Statement

On a rectangular board, lasers destroy their entire row and column. Starting from a protected cell, return the maximum number of cells a robot can safely move in one straight direction before hitting a destroyed cell or border.

---

## 10. Cumulative Views Reach Target Day

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-04-25
- **stage**: oa
- **access**: Members
- **roles**: MLE
- **tags**: array, prefix-sum, easy, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/cumulative-views-target-day
- **related discussions**: 1

### Statement

Given a daily-view array and a target, return the 0-indexed day on which the cumulative views first reach or exceed the target. If the target is never met, return `-1`.

---

## 11. Beautify Houses to Strict Monotone

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-04-25
- **stage**: oa
- **access**: Members
- **roles**: MLE
- **tags**: array, greedy, math, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/beautify-houses-monotone
- **related discussions**: 1

### Statement

Given an integer array of house heights, return the minimum number of `+1` operations needed so the array is either strictly `+1`-increasing (each entry exactly one greater than the previous) or strictly `-1`-decreasing.

---

## 12. Odd Zero-Digit Count

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-04-16
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, math, easy, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/odd-zero-digit-count
- **related discussions**: 1

### Statement

Given a non-negative integer array, count how many entries contain an odd number of the digit `0` in their decimal representation.

---

## 13. Battery Usage with Recharge Cycle

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-02-18
- **stage**: oa
- **access**: Members
- **roles**: MLE
- **tags**: heap, simulation, scheduling, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/battery-recharge-cycle
- **related discussions**: 1

### Statement

A phone needs to run for `t` more minutes. Given a pool of spare batteries with per-battery capacity and recharge times, swap batteries when one drains; the swapped battery starts recharging immediately. Return the number of *full-charge cycles* consumed, or `-1` if the runtime cannot be reached.

---

## 14. AP Replacement Rounds

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-01-27
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, simulation, math, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/ap-replacement-rounds
- **related discussions**: 1

### Statement

Repeatedly transform an `A`/`P` array: if the trailing run of `P`s is at least `replacement_rate`, strip that many `P`s from the end; else if any `A` exists, replace the rightmost `A` with `P`; else stop. Return the number of rounds to reach the terminal state.

---

## 15. Lunar Phase State Lookup

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-01-27
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: math, simulation, easy, oa, codesignal, ambiguous-spec
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/lunar-phase-state
- **related discussions**: 1

### Statement

Given the moon's state on day 1 of the year, plus a target `(season, day)`, return the moon state on that date. The moon cycles through 8 states with a period of 8 days.

---

## 16. ML Coding: Top-P (Nucleus) Sampling

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-19
- **stage**: onsite-coding · onsite-research
- **access**: Members
- **roles**: RS, MLE
- **tags**: ml-knowledge, sampling, llm, numpy, numerical-stability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/ml-coding-top-p-sampling
- **related discussions**: 1

### Statement

Applied Researcher Tech Interview 1, second problem. Given a probability distribution over a vocabulary and a threshold `p`, sample from the smallest set of top tokens whose cumulative probability is at least `p`. Tests both algorithmic clarity and ML fluency.

---

## 17. ML Coding: Linear Regression from Scratch

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-19
- **stage**: onsite-coding · onsite-research
- **access**: Members
- **roles**: RS, MLE
- **tags**: ml-knowledge, linear-regression, numpy, gradient-descent, medium
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/ml-coding-linear-regression
- **related discussions**: 1

### Statement

Applied Researcher Tech Interview 1. Implement linear regression end-to-end in NumPy — closed-form solution, then a gradient-descent variant. Standard ML coding warm-up.

---

## 18. Vowel-Wrapped Substring Middle Reverse

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-01-19
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, string-processing, easy, oa, codesignal, ambiguous-spec
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/vowel-wrap-reverse-middle
- **related discussions**: 1

### Statement

Given a string, identify substrings whose first and last characters are both vowels, then reverse the substring between them. The exact selection rule (all such substrings? maximal ones? non-overlapping?) is under-specified in the prompt — clarify or stick to the simplest interpretation.

---

## 19. Train Schedule Nearest Departure

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-01-18
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: binary-search, array, easy, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/train-schedule-nearest-departure
- **related discussions**: 1

### Statement

Given a sorted list of daily departure times and the current time, return how long ago the most recent train left (in minutes). If the current time is before the day's first train, the answer wraps to the previous day's last departure.

---

## 20. Color Match-Three Grid

- **category**: coding
- **duration**: 70 min
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: grid, simulation, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/color-match-three-grid
- **related discussions**: 1

### Statement

Given a numeric matrix where each value represents a color, repeatedly explode every cell whose 4-neighborhood contains at least two same-colored cells, then let surviving cells fall under gravity. Return the resulting matrix.

---

## 21. Matrix Border Sort & Clockwise Fill

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-07-19
- **stage**: oa
- **access**: Members
- **roles**: DS, MLE
- **tags**: grid, array, simulation, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/matrix-border-sort-clockwise-fill
- **related discussions**: 2

### Statement

Given an integer matrix, process every border layer independently: read its cells clockwise from the layer's top-left corner, sort those values in ascending order, and write them back along the same clockwise coordinate sequence. Handle inner layers that collapse to one row, one column, or one cell without duplicating coordinates.

---

## 22. Dynamic Two-Array Pair Sum Queries

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-06-05
- **stage**: oa
- **access**: Members
- **roles**: SWE, DS
- **tags**: hashmap, array, simulation, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/dynamic-two-array-pair-sum-queries
- **related discussions**: 2

### Statement

Maintain arrays `a` and `b` under point updates to `a`. For each sum query, append the number of `(a[i], b[j])` pairs whose values add to the requested target.

---

## 23. Tetris-Style Figure Placement

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-04-29
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: grid, simulation, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/tetris-figure-placement
- **related discussions**: 2

### Statement

Given five fixed Tetris-like shapes and an arrival order, place each shape on an `n x m` grid at the smallest available row-then-column position and label the occupied cells with the shape's arrival number. Brute-force scan is the intended solution; code volume is the challenge.

---

## 24. Repeated Leading-Nonzero Subtraction

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-04-25
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, simulation, math, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/repeated-subtraction-zeroing
- **related discussions**: 2

### Statement

Repeatedly: find the leftmost non-zero entry `x` in the array, subtract `x` from each subsequent entry that is `≥ x`, stop at the first entry strictly less than `x`, then add `x` to a running answer. Return the final answer when the array is all zeros.

---

## 25. Symmetric Triplets

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-04-08
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: string, easy, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/symmetric-triplets
- **related discussions**: 3

### Statement

Count the number of length-3 contiguous substrings whose first and last characters match (case-insensitive). The classic Q1 of recent Capital One OAs — simple, but a quick win that frees time for the harder later problems.

---

## 26. Warehouse Round-Robin Allocation with Closures

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-04-08
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, simulation, scheduling, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/warehouse-round-robin-allocation
- **related discussions**: 3

### Statement

Simulate a round-robin package router: dispatch incoming packages to the next available warehouse starting from index 0, skip closed or full warehouses, and reset all capacities (keeping closures) when no warehouse can take the next package. Return the index of the warehouse that handled the most packages (largest index on ties).

---

## 27. Print All Root-to-Leaf Tree Paths

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, RS
- **tags**: tree, dfs, tree-traversal, recursion, easy
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/print-tree-paths
- **related discussions**: 2

### Statement

MLE / Applied Research Set B opener. Given a binary tree (or general tree, depending on variant), print every root-to-leaf path. Standard recursive DFS — the round uses it as a warm-up before the deployment / latency discussion.

---

## 28. Sawtooth (Alternating Parity) Subarray Count

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-01-19
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, sliding-window, medium, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/sawtooth-subarray-count
- **related discussions**: 2

### Statement

Count contiguous subarrays whose elements alternate parity (even-odd-even-... or odd-even-odd-...). Single-element subarrays always count. A brute-force O(n²) solution times out on the hidden tests.

---

## 29. Matrix Expression Max Value

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-01-19
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: grid, dp, parsing, hard, oa, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/matrix-expression-max-value
- **related discussions**: 3

### Statement

Each cell of a grid holds either a digit 0-9 or a `+` / `-` symbol. Walking only right or down, find the maximum value of any syntactically valid arithmetic expression that can be read off a monotonic path. Two consecutive operators or two consecutive digits invalidate that path's prefix.

---

## 30. Matrix Commands: Reverse / Swap / Rotate

- **category**: coding
- **duration**: 70 min
- **frequency**: Low
- **last asked**: 2026-01-19
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: grid, array, simulation, oa, codesignal, easy
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/matrix-commands-reverse-swap-rotate
- **related discussions**: 2

### Statement

Execute a sequence of in-place matrix commands — `reverseRow r`, `swap r1 r2`, `rotate` (90° clockwise) — and return the final matrix. Tests parsing discipline and 2-D index manipulation more than algorithmic depth.

---

## 31. Online Banking Application (System Design)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-01-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, payment, transactions, idempotency, schema-design, persistence, distributed-systems
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/online-banking-application
- **related discussions**: 5

### Statement

The default Power Day system design prompt. Design Capital One's online banking app — multi-account types (Checking, Savings, Mortgage, Auto Loan), money movement (deposit, withdraw, transfer, Zelle), statistics (daily / weekly / monthly), and the third-party / fraud / auth integration surface.

---

## 32. Account Balance & Cross-Region Event Platform

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, distributed-systems, transactions, messaging, kafka, scaling, persistence
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/account-balance-cross-region-events
- **related discussions**: 1

### Statement

Senior Principal Power Day variant — two system design rounds. The first prompt: design a highly reliable account balance service. The second: design a cross-region event processing platform. Both are senior-leveling discussions where consistency, replication, and failure modes are the entire conversation.

---

## 33. Power Day Behavioral

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-13
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: All
- **tags**: behavioral, bq, star, conflict, leadership, ambiguity
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/power-day-behavioral
- **related discussions**: 10

### Statement

Power Day's BQ round — three to four STAR-format questions with light follow-ups, 45-60 minutes total. The interviewer takes verbatim notes throughout; depth on each story matters more than count. The Capital One BQ pool is small and recurring.

---

## 34. Virtual Credit Card Validator (Case Study)

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-13
- **stage**: onsite-deep-dive · final-round
- **access**: Free preview
- **roles**: SWE, MLE, RS
- **tags**: payment, object-design, debugging, critical-thinking, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/virtual-credit-card-validator
- **related discussions**: 6

### Statement

The canonical Capital One case study. Three phases: discuss virtual credit cards as a product, implement a rule-based transaction validator using bit-position rules embedded in card and transaction IDs, then debug provided code that fails some tests. Appears in SDE, MLE, and Applied Research loops.

---

## 35. Amusement Park Profitability Case

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: final-round
- **access**: Members
- **roles**: DS
- **tags**: critical-thinking, open-ended, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/amusement-park-profitability-case
- **related discussions**: 1

### Statement

Strategy / Data Analytics Power Day case (SDA — Strategy & Data Analytics rotation). Opens with conceptual questions about an amusement park business, then dives into calculating park profitability under different operational assumptions. Tight 45-minute round; the case runs full length.

---

## 36. Wheat Harvest Optimisation Case

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-05-13
- **stage**: final-round
- **access**: Members
- **roles**: DS
- **tags**: critical-thinking, open-ended, verbal, medium, ambiguous-spec
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/wheat-harvest-case
- **related discussions**: 1

### Statement

Strategy / Data Analytics Power Day case. A second case round, distinct from the amusement-park one. The prompt is brief — a high-frequency analyst case about wheat harvest economics or scheduling. Expect the same structured-thinking format as the amusement-park case.

---

## 37. Model Risk: VaR & Monte Carlo Orals

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-15
- **stage**: final-round
- **access**: Members
- **roles**: EM
- **tags**: verbal, math-reasoning, probability, cs-fundamentals, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/model-risk-quant-orals
- **related discussions**: 1

### Statement

Model Risk Validation Manager loop — niche finance / risk-theory round. Recurring questions: VaR for long vs short positions, Monte Carlo path-count convergence. The interviewer probes one layer deeper on each surface answer; precision in terminology is the graded signal.

---

## 38. DS Role-Play: Explain Model Regression to PM

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-27
- **stage**: final-round
- **access**: Members
- **roles**: DS
- **tags**: verbal, open-ended, critical-thinking, ml-knowledge, ab-testing
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/ds-role-play-pm-explanation
- **related discussions**: 1

### Statement

DS Power Day round 3. The interviewer plays a non-technical Product Manager who asks why the model that performed well in validation is regressing in production. The candidate explains the likely causes in plain English without technical jargon.

---

## 39. DS Business Analyst Case (P&L Decomposition)

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-27
- **stage**: final-round
- **access**: Members
- **roles**: DS
- **tags**: critical-thinking, verbal, open-ended, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/ds-business-analyst-case-study
- **related discussions**: 1

### Statement

DS Power Day round 1. Given a credit / lending business scenario — typical: a credit line increase strategy, or a new product entering the market — frame the analysis, decompose the P&L, and propose levers. Mental math is part of the round.

---

## 40. Chatbot Case Study

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-13
- **stage**: final-round
- **access**: Members
- **roles**: SWE, MLE
- **tags**: critical-thinking, open-ended, llm, evaluation, verbal, ambiguous-spec
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/chatbot-case-study
- **related discussions**: 1

### Statement

Power Day case study variant focused on deploying a customer-service chatbot at Capital One. Typically under-specified — the round covers product strategy, model selection, evaluation, and rollout risks. Has surfaced in SDE Power Day loops as an alternative to the Virtual Credit Card case.

---

## 41. AR Panel Talk Project Deep-Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-13
- **stage**: onsite-research · onsite-deep-dive
- **access**: Members
- **roles**: RS
- **tags**: deep-dive, presentation, paper-read, verbal, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/ar-panel-talk-deep-dive
- **related discussions**: 1

### Statement

Applied Research opener. Present a project or paper to a panel of three senior researchers. The depth probe is the entire round — high-level slide-walks fail; the round expects derivations, ablations, and failure modes.

---

## 42. MLE ML Knowledge Orals (Set A)

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-04
- **stage**: onsite-research · final-round
- **access**: Members
- **roles**: MLE, RS
- **tags**: ml-knowledge, verbal, cs-fundamentals, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/mle-ml-knowledge-orals
- **related discussions**: 2

### Statement

MLE / AR loop opener. 60-minute verbal round covering ML fundamentals: bias-variance, parameter vs hyperparameter, batch vs real-time inference, large-data training, feature engineering, end-to-end ML lifecycle. Closes with the candidate walking through one of their own ML projects.

---

## 43. MLE Deployment & Latency (Set B)

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-04
- **stage**: onsite-research · final-round
- **access**: Members
- **roles**: MLE, RS
- **tags**: ml-knowledge, verbal, ml-infra, inference, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/mle-deployment-and-latency
- **related discussions**: 2

### Statement

MLE / AR loop second technical round. Opens with a short coding warm-up (usually printing all root-to-leaf tree paths), then transitions to a verbal discussion on ML deployment, monitoring, and a concrete latency-optimisation scenario.

---

## 44. Code Reading Pipeline Walkthrough

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-08
- **stage**: onsite-coding · final-round
- **access**: Members
- **roles**: SWE
- **tags**: code-reading, verbal, live-review, medium
- **url**: https://www.1point3acres.com/interview/problems/company/capitalone/code-reading-pipeline-walkthrough
- **related discussions**: 2

### Statement

TDP / Intern Super Day signature round. The interviewer presents a multi-step data pipeline (not a small snippet), the candidate explains what each section does, hand-calculates outputs for provided test inputs, then suggests improvements.

---
