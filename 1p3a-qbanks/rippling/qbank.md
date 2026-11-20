# Rippling — 1point3acres Question Bank (17 curated questions)

Source: 1point3acres qbank (`company=rippling`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/rippling>

The site lists 102 entries for this company: **17 curated questions** (below), 72 OJ links, 13 external links. Only the curated 17 are captured here.

> Rippling's engineering loop is unusually concentrated: a small set of OOD coding prompts, two data-heavy system-design prompts, and a practical web API round account for most SWE interviews. The bar is driven by speed through multi-part prompts, clean extensible design, and whether the candidate can collaborate with AI tools without losing ownership of the solution.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Delivery Billing System
2. Task Manager Filter / Sort / Dedupe
3. Expense Rules Engine
4. Logger OOD
5. Camel Cards / Poker OOD
6. Article Voting / Top-K
7. Web API Round: Stack Overflow / Quora-like Service
8. Currency Exchange / Maximize After Two Days of Conversions
9. In-Memory Key-Value Store with Transactions
10. Intern OA Fixed Set
11. Median of Two Sorted Arrays
12. Music Player OOD
13. Google News / News Aggregator
14. Hotel Booking System
15. User Behavior / Metrics Monitoring Aggregator
16. Frontend News Feed
17. Project Deep Dive / HM BQ

---

## 1. Delivery Billing System

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-07
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: oop-design, heap, interval, interval-aggregation, ai-allowed, hard
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/delivery-billing-system
- **related discussions**: 15

### Statement

Build an in-memory delivery payout service. Start with driver registration, delivery recording, and O(1) total cost; add effective-dated driver rate changes and pay-up-to cutoff processing; then compu...

---

## 2. Task Manager Filter / Sort / Dedupe

- **category**: coding
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: sorting, deduplication, data-modeling, ai-allowed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/task-manager-filter-sort-dedupe
- **related discussions**: 9

### Statement

Given a list of tasks, remove duplicate tasks by description plus due date, filter to open unassigned work, sort by due date, high-priority flag, and creation age, then print each task in a specified ...

---

## 3. Expense Rules Engine

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: oop-design, hashmap, data-modeling, streaming, ai-allowed, hard
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/expense-rules-engine
- **related discussions**: 10

### Statement

Design an extensible corporate-card rules engine. Evaluate per-expense and per-trip policies, choose a useful return type, then support composite AND / OR / NOT rules and scale the design to millions of expenses and tens of thousands of rules.

---

## 4. Logger OOD

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-31
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: oop-design, string-processing, search, ai-allowed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/logger-ood
- **related discussions**: 5

### Statement

AI-coding OOD prompt for a configurable logger. Implement output transformations and an internal message store, then add search while discussing read/write trade-offs.

---

## 5. Camel Cards / Poker OOD

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: oop-design, hashmap, simulation, ai-allowed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/camel-cards-poker-ood
- **related discussions**: 7

### Statement

Compare two simplified poker hands. Classify four-card hands by extensible hand types, break ties without sorting, then handle partial hands where network loss makes the result win / loss / tie / unkn...

---

## 6. Article Voting / Top-K

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-25
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: swe
- **tags**: oop-design, hashmap, queue, top-k, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/article-voting-system
- **related discussions**: 4

### Statement

Design an in-memory article voting tracker. Support article creation, user upvotes/downvotes, each user's most recent three opinion flips, and global top-K by score.

---

## 7. Web API Round: Stack Overflow / Quora-like Service

- **category**: coding
- **duration**: 75 min
- **frequency**: Medium
- **last asked**: 2026-02-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: backend, http, api-integration, database, access-control, rate-limiting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/web-api-stack-overflow-quora
- **related discussions**: 4

### Statement

Implement a runnable REST service live in your own framework. The common product is a Stack Overflow / Quora-like question system with create/query and voting-style operations, followed by backend tri...

---

## 8. Currency Exchange / Maximize After Two Days of Conversions

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-13
- **stage**: oa
- **access**: Members
- **roles**: mle
- **tags**: graph, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/currency-exchange
- **related discussions**: 1

### Statement

Starting from 1.0 unit of a given currency, maximize the final amount by applying Day 1 exchange rates first, then Day 2 rates, with self-to-self conversion always allowed at 1.0. Each day's pairs for...

---

## 9. In-Memory Key-Value Store with Transactions

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-11
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: in-memory-database, transactions, hashmap, data-structure, object-design, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/in-memory-kv-store-transactions

### Statement

Design and implement an in-memory key-value datastore backed by a hashmap that supports set/get/delete plus begin/commit/rollback transactions. The round builds incrementally: a plain CRUD store, then...

---

## 10. Intern OA Fixed Set

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-16
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: oa, heap, dfs, api-integration, sliding-window, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/intern-oa-fixed-set
- **related discussions**: 1

### Statement

Intern OA combining DSA multiple choice and coding. Topics include time complexity, heap validation, DFS path validation, heapify, paginated REST API parsing, and a sliding-window consistent logs prob...

---

## 11. Median of Two Sorted Arrays

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-02-01
- **stage**: oa · phone-screen
- **access**: Members
- **roles**: swe
- **tags**: array, binary-search, divide-and-conquer, hard
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/median-of-two-sorted-arrays
- **related discussions**: 2

### Statement

Given two sorted arrays nums1 and nums2 of sizes m and n, return the median of the combined arrays in O(log(m+n)) time. This is the classic LeetCode-hard problem: the intended solution binary-searches...

---

## 12. Music Player OOD

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-01-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: oop-design, hashmap, set, heap, top-k, complexity-analysis, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/music-player-ood
- **related discussions**: 2

### Statement

Design and implement a Spotify-style music-player analytics class across three escalating parts: add songs and record plays with per-song unique-listener counts, return a user's last 3 unique songs wi...

---

## 13. Google News / News Aggregator

- **category**: system-design
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-11
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: swe, em
- **tags**: feed, fan-out, ranking, caching, search, top-k, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/google-news-aggregator
- **related discussions**: 17

### Statement

Design a news aggregator with topic and publisher subscriptions, story clustering across publishers, general top stories, and personalized news. The main follow-ups are push versus pull, celebrity pub...

---

## 14. Hotel Booking System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: api-integration, transactions, idempotency, schema-design, location, geohash, hard
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/hotel-booking-system
- **related discussions**: 6

### Statement

Design a hotel booking product integrated with external hotel inventory. The key evaluation point is consistency: search and booking depend on third-party room availability that can change outside Rip...

---

## 15. User Behavior / Metrics Monitoring Aggregator

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-06-28
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: swe, ds
- **tags**: data-engineering, ingestion, etl, ads, database, schema-design, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/user-behavior-monitoring-aggregator
- **related discussions**: 6

### Statement

Design an event monitoring and enrichment platform for product behavior. Collect browser/mobile events, power low-latency dashboards, keep raw events for offline analytics, and enrich data with geo or...

---

## 16. Frontend News Feed

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-06
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, feed, caching, performance-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/frontend-news-feed
- **related discussions**: 1

### Statement

Design a Facebook/X-style news feed frontend. Cover rich text, mention and hashtag hover cards, API and local data models, pagination, offline behavior, list virtualization, component boundaries, and ...

---

## 17. Project Deep Dive / HM BQ

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-11
- **stage**: onsite-deep-dive · onsite-bq · final-round
- **access**: Members
- **roles**: all-roles
- **tags**: deep-dive, presentation, behavioral, bq, impact, mentorship, medium
- **url**: https://www.1point3acres.com/interview/problems/company/rippling/project-deep-dive-and-bq
- **related discussions**: 12

### Statement

HM and onsite deep-dive rounds center on a past project presentation plus behavioral follow-ups. Senior/staff loops now probe AI practice, cross-team influence, platform ownership, business impact, me...

---
