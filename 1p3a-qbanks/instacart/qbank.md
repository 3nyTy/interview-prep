# Instacart — 1point3acres Question Bank (14 curated questions)

Source: 1point3acres qbank (`company=instacart`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/instacart>

The site lists 50 entries for this company: **14 curated questions** (below), 36 OJ links, 0 external links. Only the curated 14 are captured here.

> Instacart's recent SWE loop is dominated by CodeSignal-style practical coding: in-memory databases, worker/task trackers, expression parsing, file I/O, and product/inventory consistency. Senior loops now commonly split the onsite into two phases: two coding rounds plus a Karat AI codebase round first, then system design and project deep-dive if the first phase clears. MLE loops keep the same practical coding bar but add a fundamentals-heavy ML concept screen.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Full-Stack AI-Pairing Assessment
2. Onsite Coding: Expression Evaluation with Variables
3. Karat AI: Bus Simulation Codebase
4. OA: In-Memory Key-Value Store
5. Onsite Coding: Product Pricing, Promotions & Shelf Ordering
6. OA: Banking Account System
7. Karat Coding: Max Adjacent Stock Price Change
8. OA: Worker Attendance & Payroll
9. OA: Task Management System
10. Onsite Coding: Decode Password
11. Onsite Coding: File Matrix Lookup
12. System Design: Inventory Management
13. System Design: Product Catalog
14. MLE Screen: Coding + ML Concepts

---

## 1. Full-Stack AI-Pairing Assessment

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-08-30
- **stage**: oa · take-home
- **access**: Members
- **roles**: swe
- **tags**: fullstack, ai-tools, idempotency, testing, hard
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/full-stack-ai-assessment
- **related discussions**: 6

### Statement

A five-round, 90-minute AI-pairing assessment delivered as the "Instacart Full-Stack Engineer Assessment". Two rounds make you elicit a feature spec from an AI product manager under a 10-minute chat c...

---

## 2. Onsite Coding: Expression Evaluation with Variables

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-07-29
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: parsing, dfs, stack, graph, medium
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/onsite-expression-evaluation-with-variables
- **related discussions**: 5

### Statement

A recurring onsite coding prompt combining calculator parsing with variable assignment / equation evaluation. Examples include `A = 5`, `B = A`, `C = 5`, `D = C - 10`; variants ask for DFS over variab...

---

## 3. Karat AI: Bus Simulation Codebase

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: ai-tools, ai-allowed, code-reading, simulation, testing, object-design
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/karat-ai-bus-simulation-codebase
- **related discussions**: 4

### Statement

AI-assisted Karat round on a large bus traffic simulation codebase. The interviewer asks code-reading questions, asks for small changes like priority boarding or wheelchair capacity, then probes trade...

---

## 4. OA: In-Memory Key-Value Store

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-04-23
- **stage**: oa
- **access**: Free preview
- **roles**: SWE
- **tags**: hashmap, in-memory-database, ttl, snapshot, codesignal, hard
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/oa-in-memory-key-value-store
- **related discussions**: 4

### Statement

The dominant Instacart OA prompt. Implement an in-memory database where records are keyed by `key`, each record stores `field -> value` strings, and later levels add scans, prefix scans, timestamped reads, TTL, backup, and restore.

---

## 5. Onsite Coding: Product Pricing, Promotions & Shelf Ordering

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-29
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: parsing, array, sorting, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/onsite-product-pricing-promotions-shelf-ordering
- **related discussions**: 1

### Statement

Parse pipe-delimited product records across three tasks: compute total merchandise cost, apply the cheapest percentage or buy-X-get-Y promotion per SKU, then order shelf records while placing frozen g...

---

## 6. OA: Banking Account System

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-07-12
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: codesignal, state-machine, transactions, hard
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/oa-banking-account-system
- **related discussions**: 1

### Statement

A 90-minute, five-part CodeSignal OA that incrementally builds a banking-account service: create accounts, deposit and make payments, rank accounts by transaction activity, initialize and accept expir...

---

## 7. Karat Coding: Max Adjacent Stock Price Change

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-05-27
- **stage**: tech-screen
- **access**: Members
- **roles**: swe
- **tags**: array, sorting, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/karat-stock-price-change
- **related discussions**: 1

### Statement

Karat coding follow-up using a list of stock price records. Convert records into a sortable sequence, sort by date, compare adjacent trading records, and return the largest adjacent price change with ...

---

## 8. OA: Worker Attendance & Payroll

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-01-26
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: simulation, interval, state-machine, ranking, codesignal, hard
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/oa-worker-attendance-payroll
- **related discussions**: 1

### Statement

A newer CodeSignal OA variant. Build a worker registry, alternate entry/exit timestamps to compute total office time, rank top workers by time in office, support promotions that take effect only on th...

---

## 9. OA: Task Management System

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2025-12-18
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: hashmap, heap, versioning, scheduling, codesignal, hard
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/oa-task-management-system
- **related discussions**: 1

### Statement

A CodeSignal OA variant similar in spirit to the database prompt. Implement timestamped CRUD for tasks, priority updates and sorted retrieval, user assignment, scheduled deletion, and historical user ...

---

## 10. Onsite Coding: Decode Password

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: string, parsing, stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/onsite-decode-password
- **related discussions**: 3

### Statement

String parsing prompt known as 'Decode Password' / decode. The exact Instacart syntax varies, but the repeated appearance across onsite loops makes the core task clear: manually parse an encoded strin...

---

## 11. Onsite Coding: File Matrix Lookup

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-04
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: io, grid, streaming, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/onsite-file-matrix-lookup
- **related discussions**: 2

### Statement

Given a file containing one matrix, return the value at a requested row and column. Follow-up: the file contains multiple matrices, each preceded by a key; return a map from key to that matrix's value...

---

## 12. System Design: Inventory Management

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-07-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: storage, transactions, idempotency, schema-design, data-modeling, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/system-design-inventory-management
- **related discussions**: 2

### Statement

Design an inventory management system for a grocery marketplace. The interviewer focuses on stock updates, item reservation, shopper collect-item API calls, database schema, and high consistency.

---

## 13. System Design: Product Catalog

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: search, caching, data-modeling, versioning, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/system-design-product-catalog
- **related discussions**: 2

### Statement

Senior system-design prompt around Instacart's product catalog. The central theme is consistency across retailer item feeds, customer-facing catalog pages, availability, and shopper substitutions.

---

## 14. MLE Screen: Coding + ML Concepts

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-24
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: mle
- **tags**: ml-knowledge, binary-search, two-pointer, gradient-descent, medium
- **url**: https://www.1point3acres.com/interview/problems/company/instacart/mle-coding-and-ml-concepts
- **related discussions**: 3

### Statement

MLE candidates often get two pre-VO rounds: a coding screen with short LeetCode-medium/easy problems and a separate ML concept screen. Recent topics include sorted squares, binary-search range boundar...

---
