# Lyft — 1point3acres Question Bank (13 curated questions)

Source: 1point3acres qbank (`company=lyft`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/lyft>

The site lists 37 entries for this company: **13 curated questions** (below), 24 OJ links, 0 external links. Only the curated 13 are captured here.

> Lyft's engineering loop is hands-on and execution-heavy: candidates repeatedly see practical API-wrapper, in-memory data-store, scheduling, and product-system prompts rather than abstract puzzle rounds. Senior loops add system design around payments, donation flows, web crawling, or marketplace-adjacent products, while frontend and intern tracks lean toward small project-style implementation.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Transactional In-Memory Key-Value Store
2. Rotten Oranges / Grid Infection BFS
3. Pagination API Wrapper
4. One-Hot Encoder + ML Error Diagnosis
5. Frontend Incremental Clicker with Configurable Buttons
6. Minesweeper Game Logic
7. Job Scheduler: Minimum Workers with Assignment History
8. Minimum Window Substring
9. Frontend Autocomplete Component
10. Wikipedia Internet Archive / Web Crawler
11. Charity Donation System
12. Behavioral: Project, Conflict, Failure, and DEI
13. Commuter Coupon Conditional Probability

---

## 1. Transactional In-Memory Key-Value Store

- **category**: coding
- **duration**: 90 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: in-memory-database, transactions, data-structure, hard
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/transactional-in-memory-kv-store
- **related discussions**: 8

### Statement

Implement an in-memory key-value database with transaction support. The core APIs include normal get/set-style operations plus `begin`, `rollback`, and `commit`, with careful edge-case handling under ...

---

## 2. Rotten Oranges / Grid Infection BFS

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-01
- **stage**: oa · phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: bfs, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/rotten-oranges-grid-bfs
- **related discussions**: 5

### Statement

A recurring phone-screen and OA-style coding prompt equivalent to the rotten-oranges family: run a BFS infection process on a grid and compute whether all reachable fresh cells can be converted.

---

## 3. Pagination API Wrapper

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-29
- **stage**: phone-screen · tech-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: api-integration, iterator, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/pagination-api-wrapper
- **related discussions**: 4

### Statement

Design a wrapper over an immutable paginated client. The underlying client can only fetch complete pages, while the new API must return the next requested number of records across repeated calls.

---

## 4. One-Hot Encoder + ML Error Diagnosis

- **category**: coding
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2026-03-21
- **stage**: phone-screen
- **access**: Members
- **roles**: mle
- **tags**: ml-knowledge, feature-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/one-hot-encoder-and-ml-error-diagnosis
- **related discussions**: 1

### Statement

MLE phone screen split into a 30-minute ML-concepts section and a 45-minute handwritten one-hot encoder implementation. The concepts section used five train/test error scenarios and asked for diagnosi...

---

## 5. Frontend Incremental Clicker with Configurable Buttons

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: frontend, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/frontend-incremental-clicker-table
- **related discussions**: 1

### Statement

Build a small clicker-style UI in stages: a button increases score, reaching five points unlocks another button that spends points and adds score over time, then a table lets users configure more such...

---

## 6. Minesweeper Game Logic

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: grid, bfs, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/minesweeper-game-logic
- **related discussions**: 1

### Statement

Frontend loop coding round implemented in Python: build the algorithmic core of Minesweeper without display concerns, including map expansion and win/loss detection.

---

## 7. Job Scheduler: Minimum Workers with Assignment History

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-07-29
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: heap, scheduling, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/job-scheduler-minimum-workers
- **related discussions**: 3

### Statement

Given jobs with start time and duration, assign them to the minimum number of workers so no worker runs overlapping jobs. Unlike plain Meeting Rooms II, the output must include each job's worker assig...

---

## 8. Minimum Window Substring

- **category**: coding
- **duration**: 50 min
- **frequency**: Low
- **last asked**: 2026-06-29
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: swe
- **tags**: string, sliding-window, hashmap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/minimum-window-substring
- **related discussions**: 3

### Statement

Asked as the LC 76 Minimum Window Substring prompt in Lyft technical screens. Given two strings, return the shortest contiguous substring of the first that covers every character of the second, includ...

---

## 9. Frontend Autocomplete Component

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-11-17
- **stage**: onsite-coding · onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/frontend-autocomplete-component
- **related discussions**: 2

### Statement

Frontend onsite round that begins as component/system design and then expects implementation of the autocomplete component. The interviewer may explicitly require the design to be implementable immedi...

---

## 10. Wikipedia Internet Archive / Web Crawler

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: web-crawler, dedup, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/wikipedia-internet-archive-crawler
- **related discussions**: 5

### Statement

Design a crawler/archive system that coordinates one reliable primary machine and about 1,000 unreliable, untrusted bots on different IPs to discover and download all Wikipedia pages, store them durab...

---

## 11. Charity Donation System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: payment, idempotency, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/charity-donation-system
- **related discussions**: 3

### Statement

Design a charity donation website where payment execution is abstracted behind an SDK, but the system still has to handle correctness, failure recovery, duplicate prevention, and traffic spikes.

---

## 12. Behavioral: Project, Conflict, Failure, and DEI

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-10
- **stage**: onsite-bq
- **access**: Members
- **roles**: all-roles
- **tags**: behavioral, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/behavioral-project-conflict-and-dei
- **related discussions**: 5

### Statement

Lyft behavioral rounds are standard but broad. Candidates are asked for many short examples across conflict, communication, feedback, failure, decisions, project challenges, and sometimes diversity su...

---

## 13. Commuter Coupon Conditional Probability

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-05-30
- **stage**: phone-screen
- **access**: Members
- **roles**: ds
- **tags**: probability, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/lyft/ds-commuter-coupon-probability
- **related discussions**: 1

### Statement

Data science statistics screen probability problem framed around Lyft commute couponing: given conditional ride probabilities for the to-work and return-home trips, compute the marginal probability of...

---
