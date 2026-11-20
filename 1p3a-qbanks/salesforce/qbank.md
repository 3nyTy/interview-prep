# Salesforce — 1point3acres Question Bank (36 curated questions)

Source: 1point3acres qbank (`company=salesforce`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/salesforce>

The site lists 116 entries for this company: **36 curated questions** (below), 80 OJ links, 0 external links. Only the curated 36 are captured here.

> Salesforce's loop is bank-driven: a small set of coding prompts (LFU Cache, Flatten JSON, linked-list dedup, frontend curry + traffic light, OA HackerRank classics) and one signature system design (Coffee Ordering, with its SQL follow-ups) cover the bulk of recent rounds. Down-leveling at offer time is common — the HM screen and behavioural depth materially shape the final level, often more than coding correctness alone.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Minimum One-Character Word Transformations
2. Find the Celebrity
3. Valid Word Abbreviation (LeetCode 408) with Follow-Up Variant
4. Lowest Common Ancestor with Parent Pointers (LeetCode 1650)
5. Kth Smallest in a BST (LeetCode 230) with Follow-Ups
6. Optimal Account Balancing (LeetCode 465)
7. Delete One Character Type, Minimize Unique-Character Segments
8. Maximal Rectangle (LeetCode 85)
9. Maximal Square (LeetCode 221)
10. Campaign Cost Weekly Partition
11. String Compression (HackerRank OA, LeetCode 443)
12. Maximum Number of Palindromic Strings (OA)
13. Maximum Requests in a Time Window (OA)
14. Onsite Mini Project — Concurrent Web Crawler
15. Code Review OA — Python Files (Salesforce / Slack)
16. Array Left / Right Duplicate Check (Binary Strings)
17. Binary String Synchronous '01' → '10' Replacement
18. TypeScript Refactor OA (HackerRank — fullstack)
19. Minimum Operations to Reduce an Integer to 0 (LeetCode 2571)
20. Frontend Traffic Light (HTML / CSS / JS)
21. Frontend Curry: addTwoNumbers → addThreeNumbers(a)(b)(c)
22. Singly Linked List — Remove Duplicate Values
23. LFU Cache (LeetCode 460)
24. Longest Subsequence of X That Is a Substring of Y
25. Flatten Nested JSON / HashMap to String
26. Coffee Ordering System Design
27. Design an Enterprise Messaging / Collaboration System
28. Design a Collaborative Spreadsheet (Google Sheets)
29. Rate Limiter for Expensive API with User-Configurable Monthly Quota
30. Design an Async Job / Task System
31. Design a High-Throughput Distributed Cache
32. Design an Analytics Metrics Dashboard for ChatGPT / LLM Service
33. Hiring Manager + Behavioral Rounds
34. AI / ML Fundamentals Oral Round (AI Engineer)
35. Project Architecture Deep Dive (AI Engineer / Backend)
36. Cloud / Domain Knowledge Oral Round (GCP / AWS / K8s)

---

## 1. Minimum One-Character Word Transformations

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, string, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/minimum-word-transformation-steps
- **related discussions**: 1

### Statement

Find the minimum number of steps required to transform `startWord` into `endWord`. Each step may change one character, and intermediate words are constrained by `wordsList`.

---

## 2. Find the Celebrity

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/find-the-celebrity
- **related discussions**: 1

### Statement

Given `n` people and a helper `knows(a, b)`, return the celebrity if one exists, otherwise return `-1`. A celebrity knows nobody, while everyone else knows the celebrity.

---

## 3. Valid Word Abbreviation (LeetCode 408) with Follow-Up Variant

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, backtracking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/valid-word-abbreviation
- **related discussions**: 1

### Statement

Validate whether an abbreviation legally represents a word by interpreting each number as a run of skipped characters and matching literal letters in place. The follow-up reverses the task: generate every legal abbreviation while merging adjacent omitted characters into a single count.

---

## 4. Lowest Common Ancestor with Parent Pointers (LeetCode 1650)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/lowest-common-ancestor-parent-pointers
- **related discussions**: 1

### Statement

Given two nodes in a binary tree where each node has a parent pointer and the root is unavailable, return their lowest common ancestor; the interview expects the constant-space pointer-switching solution and an explanation of the disconnected-tree case. A follow-up replaces the tree with a multi-parent DAG, where common ancestry can be non-unique.

---

## 5. Kth Smallest in a BST (LeetCode 230) with Follow-Ups

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bst, tree-traversal, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/kth-smallest-bst-follow-ups
- **related discussions**: 1

### Statement

Return the kth-smallest value in a binary search tree, then optimize repeated order-statistic queries under frequent inserts and deletes. Further follow-ups move the structure into a database index and introduce tenant-scoped visibility that can invalidate globally maintained subtree counts.

---

## 6. Optimal Account Balancing (LeetCode 465)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-25
- **stage**: —
- **access**: Members
- **roles**: SWE
- **tags**: transactions, hard
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/optimal-account-balancing
- **related discussions**: 1

### Statement

The Futureforce university-recruiting prompt is the unchanged LeetCode 465 problem: consolidate a set of person-to-person transactions and return the minimum number of additional transfers required to settle every net balance.

---

## 7. Delete One Character Type, Minimize Unique-Character Segments

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-07-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/delete-character-minimize-unique-segments
- **related discussions**: 1

### Statement

Choose one character value, delete every occurrence of it from the input string, then partition the remaining string into the minimum number of contiguous segments such that no segment contains a repeated character. Return the smallest segment count achievable over all deletion choices.

---

## 8. Maximal Rectangle (LeetCode 85)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, stack, monotonic-stack, hard
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/maximal-rectangle
- **related discussions**: 1

### Statement

The prompt is the unchanged LeetCode 85 Maximal Rectangle problem: given a binary matrix, return the area of the largest rectangle containing only `1` values. The expected path converts each row into a histogram and derives the monotonic-stack procedure for the largest rectangle.

---

## 9. Maximal Square (LeetCode 221)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/maximal-square
- **related discussions**: 1

### Statement

The prompt is the unchanged LeetCode 221 Maximal Square problem: given a binary matrix, return the area of the largest square containing only `1` values. The expected explanation derives the dynamic-programming transition before implementation.

---

## 10. Campaign Cost Weekly Partition

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/campaign-cost-weekly-partition
- **related discussions**: 1

### Statement

Given an array of campaign costs and an integer number of weeks, split the campaigns into exactly that many contiguous non-empty groups. The cost of a week is the maximum campaign cost assigned to that week; minimize the sum of those weekly maxima.

---

## 11. String Compression (HackerRank OA, LeetCode 443)

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-04-03
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, two-pointer, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/string-compression-oa
- **related discussions**: 1

### Statement

First problem on the recent Staff SWE HackerRank OA: classic run-length string compression done in-place on a character array.

---

## 12. Maximum Number of Palindromic Strings (OA)

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-03-19
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, greedy, palindromes, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/maximum-palindromic-strings
- **related discussions**: 1

### Statement

First problem on a recent Salesforce fulltime OA (HackerRank, 2 problems / 60 min). Given an array of lowercase strings, you may swap characters between any two distinct strings any number of times. Return the maximum number of strings you can make palindromic.

---

## 13. Maximum Requests in a Time Window (OA)

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-03-19
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, sliding-window, two-pointer, sorting, easy
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/maximum-requests-in-time-window
- **related discussions**: 1

### Statement

Second problem on the same recent Salesforce OA (HackerRank, 2 problems / 60 min). Given a list of integer timestamps and a window size W, find the maximum number of timestamps that fit inside any contiguous window of length W (window = `[start, start + W - 1]`).

---

## 14. Onsite Mini Project — Concurrent Web Crawler

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: python, concurrency, web-crawler, mini-project, async, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/web-crawler-mini-project
- **related discussions**: 1

### Statement

Salesforce AI Engineer onsite "Hands-on Mini Project" round: implement a small end-to-end project on the spot. A representative instance is a concurrent web crawler that scrapes a specified site, orders the results per a given rule, and exports to CSV. Code-while-narrating is part of the signal.

---

## 15. Code Review OA — Python Files (Salesforce / Slack)

- **category**: coding
- **duration**: 120 min
- **frequency**: Single report
- **last asked**: 2026-03-05
- **stage**: oa · take-home
- **access**: Members
- **roles**: SWE
- **tags**: code-review, code-reading, sql, security, race-condition, python, take-home, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/code-review-oa
- **related discussions**: 1

### Statement

Salesforce / Slack joint OA in a code-review format: candidate is given several Python files (SQL inserts, queries, etc.) and asked to leave at least 10 comments identifying bugs and design issues across different areas. Take-home format with a 24-hour window despite the recruiter's stated 1-2 hour estimate.

---

## 16. Array Left / Right Duplicate Check (Binary Strings)

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2025-12-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, string, binary-string, easy
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/array-left-right-duplicate-check
- **related discussions**: 1

### Statement

First problem on the Salesforce Futureforce 2026 Coding Challenge (intern OA). For each index of an integer array, output whether the value appears earlier in the array and whether it appears later. Encode both answers as binary strings of length n.

---

## 17. Binary String Synchronous '01' → '10' Replacement

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-12-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, binary-string, simulation, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/binary-string-01-10-swap
- **related discussions**: 1

### Statement

Salesforce Futureforce 2026 Coding Challenge (intern OA). Given a binary string, every second all occurrences of `"01"` simultaneously become `"10"`. Repeat until the string is stable. Return the number of seconds taken.

---

## 18. TypeScript Refactor OA (HackerRank — fullstack)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-07-25
- **stage**: oa · onsite-coding · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: refactoring, code-reading, typescript, frontend, fullstack, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/typescript-refactor-oa
- **related discussions**: 2

### Statement

Salesforce uses an existing-codebase exercise in full-stack loops: the detailed TypeScript form asks candidates to fix snapshot tests, add coverage, and refactor duplicated conditional logic while keeping tests green. A newer 90-minute OA includes a thinner Java Spring variant in which candidates modify the supplied project until its test cases pass.

---

## 19. Minimum Operations to Reduce an Integer to 0 (LeetCode 2571)

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-04-03
- **stage**: oa · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bit-manipulation, greedy, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/minimum-operations-reduce-to-zero
- **related discussions**: 2

### Statement

Recurring HackerRank coding problem on the Salesforce OA. Given a positive integer `n`, in each operation you may add or subtract any power of 2; return the minimum number of operations to reduce `n` to 0.

---

## 20. Frontend Traffic Light (HTML / CSS / JS)

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-01-30
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: frontend, traffic-light, javascript, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/frontend-traffic-light
- **related discussions**: 3

### Statement

Salesforce's signature frontend coding round, asked even of generalist / backend candidates applying to fullstack-titled roles. Build a traffic light that cycles red 4s → green 1s → yellow 1s using only HTML, CSS, and vanilla JavaScript (no React allowed in the CoderPad sandbox).

---

## 21. Frontend Curry: addTwoNumbers → addThreeNumbers(a)(b)(c)

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-01-30
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: currying, frontend, javascript, easy
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/frontend-currying-add-numbers
- **related discussions**: 2

### Statement

Salesforce's recurring fullstack phone-screen warm-up. Given `addTwoNumbers(a, b) = a + b`, implement a curried `addThreeNumbers(a)(b)(c)` that returns `a + b + c`. Frequently extended into a generic `curry` or `addN` variadic version.

---

## 22. Singly Linked List — Remove Duplicate Values

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-01-30
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, hashmap, deduplication, easy
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/linked-list-remove-duplicates
- **related discussions**: 3

### Statement

Online assessment classic asked in Salesforce's HackerRank OA, both SMTS and MTS levels. Given a singly linked list, keep only the first occurrence of each value and remove every later node with a value seen earlier. Return the head of the modified list.

---

## 23. LFU Cache (LeetCode 460)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, hashmap, design, caching, hard
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/lfu-cache
- **related discussions**: 2

### Statement

Implement an O(1) LFU (Least Frequently Used) cache supporting `get(key)` and `put(key, value)` under a fixed capacity. Asked both as a phone-screen coding problem and as the second coding round in onsite loops.

---

## 24. Longest Subsequence of X That Is a Substring of Y

- **category**: coding
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-01-20
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, dp, two-pointer, subsequence, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/longest-subsequence-substring
- **related discussions**: 2

### Statement

Second problem on Salesforce's SMTS HackerRank OA: given strings `x` and `y`, find the length of the longest subsequence of `x` that also appears as a contiguous substring of `y`.

---

## 25. Flatten Nested JSON / HashMap to String

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-11
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: recursion, dfs, json, hashmap, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/flatten-nested-json
- **related discussions**: 2

### Statement

Given a nested JSON object (or equivalent nested HashMap), flatten it into a single-level representation. The natural representation is dotted key paths (`a.b.c -> value`) — recursion on values that are themselves objects, base case on scalars.

---

## 26. Coffee Ordering System Design

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-02-09
- **stage**: phone-screen · onsite-system-design
- **access**: Free preview
- **roles**: SWE
- **tags**: system-design, coffee-ordering, schema-design, sql, notification, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/coffee-ordering-system
- **related discussions**: 4

### Statement

Salesforce's signature backend SD prompt: design an online coffee ordering / management service end-to-end. Interviewers compress the design into 30 minutes and then drill on SQL queries and edge cases ("customers who never ordered", "top spender last month"), so a clean ER model and explicit notification flow are weighted more than novel architecture.

---

## 27. Design an Enterprise Messaging / Collaboration System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: messaging, chat, access-control, schema-design, search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/enterprise-messaging-collaboration-system
- **related discussions**: 1

### Statement

Design an enterprise messaging product with direct messages, group channels, durable history, read receipts, search, presence, offline delivery, and strict cross-organization isolation. The round focuses on tenant-aware storage and search, deterministic per-conversation ordering, compact read watermarks, and server-enforced permissions.

---

## 28. Design a Collaborative Spreadsheet (Google Sheets)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, SWE
- **tags**: system-design, google-sheets, concurrency, data-modeling, schema-design, versioning, snapshot, websocket, storage, hard
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/google-sheets-design
- **related discussions**: 1

### Statement

Salesforce AI Engineer onsite system-design round. Design a web service like Google Sheets, with the interviewer steering focus onto the backend and data model — concurrency control for simultaneous editors, storage of large sheets, snapshot / version backup.

---

## 29. Rate Limiter for Expensive API with User-Configurable Monthly Quota

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: onsite-system-design · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: system-design, rate-limiting, throttling, caching, redis, quotas, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/rate-limiter-user-configurable-quota
- **related discussions**: 1

### Statement

Tech-screen system-design round in the Salesforce MTS/SMTS loop. The product has one expensive API; each customer can set their own monthly call limit via dashboard. Design the rate-limiting layer that enforces those quotas, accepts updates to them, and explains overage behaviour.

---

## 30. Design an Async Job / Task System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, job-system, scheduling, queueing, idempotency, retry, storage, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/async-job-task-system
- **related discussions**: 1

### Statement

First system-design round in the Salesforce LMTS onsite. Design a generic async job execution service — API surface, state machine, retry, idempotency, storage choice. Not asking for novel architecture, but for the candidate to explain a complete service cleanly end-to-end.

---

## 31. Design a High-Throughput Distributed Cache

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, caching, sharding, distributed-systems, concurrency, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/high-throughput-cache-design
- **related discussions**: 1

### Statement

Second system-design round in the Salesforce LMTS onsite — distinct from the service-design round earlier in the loop. Design an in-memory caching layer (Redis-class) that fronts a primary database, with explicit attention to eviction, consistency, hot keys, and sharding under high concurrent load.

---

## 32. Design an Analytics Metrics Dashboard for ChatGPT / LLM Service

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, analytics-dashboard, streaming, data-engineering, data-modeling, scaling, llm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/analytics-dashboard-llm-product
- **related discussions**: 2

### Statement

Salesforce backend SD prompt asked in onsite VOs and tech-screens: design an analytics dashboard that pulls metrics from an existing LLM service ("ChatGPT-like"). Interviewers explicitly de-scope frontend UI and model design — the round is about the back-end data pipeline that aggregates raw events into queryable metrics (DAU, latency percentiles, error rates, token counts).

---

## 33. Hiring Manager + Behavioral Rounds

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-24
- **stage**: recruiter-screen · phone-screen · onsite-bq
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, bq, star, leadership, conflict, mentorship, ownership, why-company, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/hm-and-bq-rounds
- **related discussions**: 14

### Statement

Salesforce loops include a 30–45 minute Hiring Manager (HM) screen plus dedicated BQ time inside many technical rounds. The HM screen can be a pure-BQ chat, a project-and-tech-stack discussion, or a chat-plus-surprise-coding mix; in every shape, the interaction can materially affect progression and final level. Recurring prompts cover communication, mentorship, conflict, ownership, and post-failure retrospectives.

---

## 34. AI / ML Fundamentals Oral Round (AI Engineer)

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-10
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: MLE
- **tags**: ml-knowledge, llm, transformer, attention, rag, evaluation, inference, cs-fundamentals, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/ai-ml-fundamentals-oral
- **related discussions**: 1

### Statement

Third virtual interview in the Salesforce AI Engineer (SMTS) loop. Verbal ML / LLM fundamentals quiz covering ML basics, model-serving practicalities, and LLM-specific concepts (Transformers, context engineering, RAG, grounding, guardrails). No coding; structured Q&A.

---

## 35. Project Architecture Deep Dive (AI Engineer / Backend)

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-17
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: MLE, SWE
- **tags**: deep-dive, presentation, ml-knowledge, llm-agent, system-design, kafka, messaging, concurrency, race-condition, circuit-breaker, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/project-deep-dive-ai-engineer
- **related discussions**: 2

### Statement

Salesforce dedicates a round to one past project and drills into architecture, trade-offs, challenges, scale, and hindsight. AI Engineer versions favor agent orchestration and ML-system decisions; a backend version uses a notification system to probe service decomposition, event-driven messaging, failure handling, consumer concurrency, and production operations.

---

## 36. Cloud / Domain Knowledge Oral Round (GCP / AWS / K8s)

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-04
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: domain-knowledge, cs-fundamentals, infra, cloud, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/salesforce/cloud-domain-knowledge-round
- **related discussions**: 2

### Statement

Salesforce GCP / Public-Cloud-team loops include a dedicated domain-knowledge round drilling on cloud platform internals and Kubernetes — even if the candidate's background is on a different cloud. Concept fluency on K8s, cloud pipelines, and (where relevant) cloud-migration experience.

---
