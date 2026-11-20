# Two Sigma — 1point3acres Question Bank (16 curated questions)

Source: 1point3acres qbank (`company=twosigma`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/twosigma>

The site lists 50 entries for this company: **16 curated questions** (below), 34 OJ links, 0 external links. Only the curated 16 are captured here.

> Two Sigma's loops split sharply by track: QR and data roles emphasize linear models, statistics, data-analysis case work, and domain reasoning, while SWE/QSE loops favor implementation-heavy trading, database, data-structure, and OOD prompts. The process is fast, high-variance, and often unforgiving on incomplete code or unclear statistical reasoning.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. QR OA — Linear Interpolator
2. QR OA — NYC Temperature Regression
3. QR OA — Efficient Univariate OLS Regression
4. SWE / QSE OA — IPO Share Allocation
5. SWE / QSE OA — Sewer Tree Partition
6. Huffman-Style Binary Encode / Decode
7. Maximum Independent Set on a Tree
8. Palindrome Warmup + Two-Task Worker Scheduling
9. Maximum Product Path in a Complete Directed Graph
10. Order Matching Engine
11. In-Memory SQL-Like Database
12. Add Two Strings with a Single-Digit Adder
13. HashMap from Scratch
14. QR Statistics / Domain Knowledge Oral
15. QR Data Analysis Prediction Case
16. GenAI Quant SWE Manager Deep Dive

---

## 1. QR OA — Linear Interpolator

- **category**: coding
- **duration**: 105 min
- **frequency**: High
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Free preview
- **roles**: RS, DS
- **tags**: binary-search, math, linear-regression, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/qr-oa-linear-interpolator
- **related discussions**: 9

### Statement

Implement a piecewise linear interpolator from sorted knot points. The function must support interpolation inside the knot range, extrapolation outside the range, efficient lookup, and duplicate x-coordinate edge cases.

---

## 2. QR OA — NYC Temperature Regression

- **category**: coding
- **duration**: 105 min
- **frequency**: High
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Members
- **roles**: rs, ds
- **tags**: pandas, linear-regression, scikit-learn, feature-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/qr-oa-nyc-temperature-regression
- **related discussions**: 9

### Statement

Analyze daily town temperatures and use simple linear models to predict NYC temperature. The task mixes pandas-style aggregation, statistics, least-squares regression, MSE comparison, and greedy featu...

---

## 3. QR OA — Efficient Univariate OLS Regression

- **category**: coding
- **duration**: 105 min
- **frequency**: Medium
- **last asked**: 2026-09-09
- **stage**: oa
- **access**: Members
- **roles**: rs, ds
- **tags**: linear-regression, streaming, pandas, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/qr-oa-efficient-univariate-ols
- **related discussions**: 6

### Statement

Compute no-intercept univariate OLS slopes, then update the slope over streaming or batched data using maintained sufficient statistics rather than refitting from scratch.

---

## 4. SWE / QSE OA — IPO Share Allocation

- **category**: coding
- **duration**: 120 min
- **frequency**: Medium
- **last asked**: 2026-07-23
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: queue, sorting, simulation, trading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/swe-oa-ipo-share-allocation
- **related discussions**: 7

### Statement

Allocate IPO shares to bidders by descending price. Single bidders receive their requested amount up to remaining supply; tied price groups are allocated round-robin by timestamp until supply or group...

---

## 5. SWE / QSE OA — Sewer Tree Partition

- **category**: coding
- **duration**: 120 min
- **frequency**: Medium
- **last asked**: 2026-07-23
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: tree, dfs, prefix-sum, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/swe-oa-sewer-tree-partition
- **related discussions**: 7

### Statement

Given a rooted tree encoded by parent pointers and per-node input values, cut one edge / node partition so the absolute difference between the two resulting component sums is minimized.

---

## 6. Huffman-Style Binary Encode / Decode

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-15
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: heap, tree, compression, binary-string, hard
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/huffman-style-binary-encode-decode
- **related discussions**: 1

### Statement

Build binary encodings for characters by constructing a frequency tree, then implement both encode and decode. The prompt is long and conceptually close to Huffman coding, with the main challenge bein...

---

## 7. Maximum Independent Set on a Tree

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: tree, dp, graph, hard
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/maximum-independent-set-on-tree
- **related discussions**: 1

### Statement

Given a social graph that is guaranteed to be a tree, select the largest possible group of people such that no two selected people know each other. Follow-up: remove the tree guarantee.

---

## 8. Palindrome Warmup + Two-Task Worker Scheduling

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: rs, ds
- **tags**: palindromes, greedy, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/palindrome-and-task-scheduling-round
- **related discussions**: 1

### Statement

QR onsite coding round: start with palindrome checks, then generate the smallest palindrome greater than K, then reason verbally about assigning 2n workers across two tasks to minimize total time.

---

## 9. Maximum Product Path in a Complete Directed Graph

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: rs, ds
- **tags**: graph, bitmask, dp, math, hard
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/maximum-product-path-in-complete-directed-graph
- **related discussions**: 1

### Statement

Given a complete directed graph with positive edge weights, find the no-repeated-node path that maximizes the product of edge weights. The candidate recognized the exponential nature and considered lo...

---

## 10. Order Matching Engine

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-05-12
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: order-book, heap, trading, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/order-matching-engine
- **related discussions**: 2

### Statement

Implement a simplified stock exchange that matches buy and sell orders by price-time priority, handles partial fills, and maintains an order book.

---

## 11. In-Memory SQL-Like Database

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-07
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: in-memory-database, sql, parsing, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/in-memory-sql-like-database
- **related discussions**: 2

### Statement

Build an in-memory database that supports creating tables, inserting integer rows, and selecting rows with AND-combined predicates over comparison operators.

---

## 12. Add Two Strings with a Single-Digit Adder

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: rs, ds
- **tags**: string, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/add-two-strings-with-single-digit-adder
- **related discussions**: 2

### Statement

Given only a helper that can add two single digits, implement addition for two non-negative integer strings. The live round expects careful carry handling and time/space complexity explanation.

---

## 13. HashMap from Scratch

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-11-13
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: hashmap, linked-list, data-structure, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/hashmap-from-scratch
- **related discussions**: 2

### Statement

Implement a hashmap without using built-in hash-table libraries. The round centers on buckets, collision handling, resizing, hash-function tradeoffs, and clean get/put semantics.

---

## 14. QR Statistics / Domain Knowledge Oral

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-02-18
- **stage**: onsite-research
- **access**: Members
- **roles**: rs, ds
- **tags**: probability, linear-algebra, linear-regression, expected-value, math-reasoning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/qr-statistics-domain-oral
- **related discussions**: 4

### Statement

Math-heavy oral round covering statistics, regression, Markov chains, random graph expectations, and candidate-specific domain knowledge. The interviewer expects derivations, not just concept names.

---

## 15. QR Data Analysis Prediction Case

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-02-18
- **stage**: onsite-research · onsite-deep-dive
- **access**: Members
- **roles**: rs, ds
- **tags**: data-analysis, linear-regression, feature-engineering, experiment-design, open-ended, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/qr-data-analysis-prediction-case
- **related discussions**: 6

### Statement

Open-ended data-analysis round: define features, targets, models, and evaluation for a prediction problem such as housing prices, CitiBike demand, rental price, stock price from search/social data, or...

---

## 16. GenAI Quant SWE Manager Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-12
- **stage**: final-round · onsite-deep-dive
- **access**: Members
- **roles**: mle, swe
- **tags**: llm, lora, agentic-workflow, evaluation, technical-deep-dive, medium
- **url**: https://www.1point3acres.com/interview/problems/company/twosigma/genai-quant-swe-manager-deep-dive
- **related discussions**: 1

### Statement

Manager round for a GenAI / quant-infra SWE role, focused on practical LLM fine-tuning tradeoffs, production agent frameworks, prior scope, scale, latency, quality, cost, and reliability decisions.

---
