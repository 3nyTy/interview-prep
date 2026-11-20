# Ramp — 1point3acres Question Bank (15 curated questions)

Source: 1point3acres qbank (`company=ramp`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/ramp>

The site lists 53 entries for this company: **15 curated questions** (below), 25 OJ links, 13 external links. Only the curated 15 are captured here.

> Ramp front-loads a CodeSignal "industry coding framework" online assessment — a four-level progressive object-modeling problem — often before any resume review, then runs a small, heavily-repeated set of live coding rounds. Prompts are deliberately under-specified: printing and exploring the data format yourself is the real signal, and the bar rewards fast, debuggable, pragmatic code over elegance. The loop also explicitly probes how you collaborate with AI tools.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. URL Maze — Recursive API Crawl to "Congrats"
2. Frontend Challenge — DOM Flag Extraction + React Wordle
3. AI-Assisted Receipt Upload and Transaction Matching
4. Excel Cell Calculation
5. Tic Tac Toe
6. OA — Digital Recipe Manager (4 Levels)
7. OA — Task Management System (4 Levels)
8. Convert snake_case names to lowerCamelCase
9. OA — Sliding-Window Rate Limiter
10. OA — Cloud File-Storage System (4 Levels)
11. Detect Recurring Transactions
12. Flight Tracker — User Location at a Given Time
13. OA — In-Memory Banking System (4 Levels)
14. Design an Expedia-Style Hotel Reservation System
15. Async Video Behavioral Screen

---

## 1. URL Maze — Recursive API Crawl to "Congrats"

- **category**: coding
- **frequency**: Medium
- **last asked**: 2026-07-20
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: api-integration, http, bfs, graph, retry, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/url-maze-crawler
- **related discussions**: 6

### Statement

Call a given URL; the response contains the next URLs to follow. Keep crawling until a response says "Congrats", returning only that final URL. Follow-ups add HTTP error handling (503/504/404, retries...

---

## 2. Frontend Challenge — DOM Flag Extraction + React Wordle

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-07-15
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, typescript, dom, javascript, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/frontend-challenge-dom-wordle
- **related discussions**: 5

### Statement

The frontend OA: a React + TypeScript + Mocha challenge. Extract a hidden flag scattered across wildcard-attributed DOM nodes via a custom query selector, then build a simplified Wordle in React whose...

---

## 3. AI-Assisted Receipt Upload and Transaction Matching

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: ai-coding, ai-tools, ai-allowed, frontend, ux, api-integration, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/ai-receipt-upload-matching
- **related discussions**: 1

### Statement

Improve an existing receipt-upload UI so users can upload receipts, observe asynchronous OCR processing, inspect extracted merchant/date/amount data, and confirm or override the suggested transaction ...

---

## 4. Excel Cell Calculation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-18
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: swe
- **tags**: hard, oop-design, data-structure, dependency-resolution, grid
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/excel-cell-calculation
- **related discussions**: 1

### Statement

Design an Excel-like spreadsheet class supporting set/get operations and dependency-aware formulas. One canonical variant stores sums over cells and ranges; another accepts basic arithmetic formulas b...

---

## 5. Tic Tac Toe

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/tic-tac-toe

### Statement

Build a single-screen Tic Tac Toe mini-game from scratch. The interviewer layers several small follow-up requirements on top of the base game as you go.

---

## 6. OA — Digital Recipe Manager (4 Levels)

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: oa, codesignal, oop-design, object-design, simulation, versioning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/oa-recipe-manager
- **related discussions**: 1

### Statement

A CodeSignal four-level OA: a recipe manager with CRUD and case-insensitive name uniqueness, ingredient search and sorting, user accounts/edits, and per-recipe version history with rollback.

---

## 7. OA — Task Management System (4 Levels)

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-01-17
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: oa, codesignal, oop-design, object-design, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/oa-task-management-system
- **related discussions**: 1

### Statement

A CodeSignal four-level OA: a task manager with CRUD + sequential IDs, substring search and priority sorting, time-windowed user assignments under a quota, and completion / overdue tracking.

---

## 8. Convert snake_case names to lowerCamelCase

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: easy, string, string-processing, parsing
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/snake-case-to-camelcase

### Statement

Convert a variable name from snake_case to lowerCamelCase: strip word-separating underscores and capitalize the first letter of every word after the first. Leading and trailing underscores must be pre...

---

## 9. OA — Sliding-Window Rate Limiter

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-09-29
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: sliding-window, hashmap, rate-limiting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/oa-rate-limiter
- **related discussions**: 1

### Statement

A standalone 45-minute single-problem OA (Applied AI Engineer pipeline): given time-sorted requests with source IPs, accept or reject each so that no IP exceeds a per-window limit. Sliding window + pe...

---

## 10. OA — Cloud File-Storage System (4 Levels)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-08-10
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: oa, codesignal, oop-design, object-design, simulation, filesystem, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/oa-cloud-storage-system
- **related discussions**: 1

### Statement

A CodeSignal four-level OA modeling an in-memory cloud storage system: add/copy/get files, prefix/suffix search, per-user capacity limits, and file compression/decompression.

---

## 11. Detect Recurring Transactions

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-07-20
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: hashmap, parsing, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/recurring-transactions
- **related discussions**: 2

### Statement

Given a list of transactions (date, merchant, amount, currency), identify the recurring ones and print them as e.g. "Netflix, $30 per month". The hard part is separating genuine subscriptions from fre...

---

## 12. Flight Tracker — User Location at a Given Time

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-06-01
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: binary-search, array, simulation, io, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/flight-user-location
- **related discussions**: 3

### Statement

Given a list of flights (departure/arrival airport + time, user_id), answer where a given user was at a given time, plus which user took the most flights. Binary search over each user's chronologically-sorted flights, with careful edge cases.

---

## 13. OA — In-Memory Banking System (4 Levels)

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-01-17
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: oa, codesignal, oop-design, object-design, simulation, in-memory-database, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/oa-banking-system
- **related discussions**: 3

### Statement

The most common Ramp CodeSignal OA domain: a four-level in-memory banking system — core accounts and payments, ranking accounts by outgoing volume, scheduled payments with cashback, and merging two ac...

---

## 14. Design an Expedia-Style Hotel Reservation System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-04-17
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: caching, api-integration, system-design, hard
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/hotel-reservation-system
- **related discussions**: 1

### Statement

Design an aggregator-style hotel reservation system (Expedia-like) that resells rooms from many hotel-group APIs. The pivotal requirement is low-latency room availability without serving stale invento...

---

## 15. Async Video Behavioral Screen

- **category**: behavioral
- **frequency**: Single report
- **last asked**: 2025-11-10
- **stage**: recruiter-screen
- **access**: Members
- **roles**: swe
- **tags**: behavioral, why-company, ai-collaboration
- **url**: https://www.1point3acres.com/interview/problems/company/ramp/video-interview-behavioral
- **related discussions**: 1

### Statement

An async, pre-recorded video screen (Hireflix) with a fixed set of behavioral questions covering motivation, learning, AI usage, and a recent project.

---
