# Robinhood — 1point3acres Question Bank (13 curated questions)

Source: 1point3acres qbank (`company=robinhood`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/robinhood>

The site lists 40 entries for this company: **13 curated questions** (below), 27 OJ links, 0 external links. Only the curated 13 are captured here.

> Robinhood's loop leans on a small, stable bank of recurring problems: a graph load-factor / referral-count coding question, a Job Scheduler system design, and a stock-quote / trading-system design. Onsites pair these with a project deep dive that is graded as hard as the technical rounds, and interviewers consistently reward complete, edge-case-correct code over elegant-but-unfinished design.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Service Load Factor / Referral Count
2. Chainable Rectangle Manipulation API
3. Analytics Engineer: SQL + Python Sessionization
4. Security-Flavored Coding: Constrained Data Structure & Input Validation
5. Word Frequency from a Large Paragraph
6. Fractional Share Buy/Sell Inventory
7. Friend / Money-Transfer Request Parsing (OOD)
8. Weekly Calendar (React)
9. Job Scheduler System Design
10. Stock Trading / Real-Time Quote System Design
11. Photo Album Frontend System Design
12. Recruiter / HR Screen
13. Project Deep Dive

---

## 1. Service Load Factor / Referral Count

- **category**: coding
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-09-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: graph, dfs, topological-sort, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/service-load-factor-referral-count
- **related discussions**: 9

### Statement

Given a dependency (or referral) DAG, compute for each node the number of unique nodes that directly or indirectly depend on it. Robinhood's most frequent coding screen; shows up under several names (...

---

## 2. Chainable Rectangle Manipulation API

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-03
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: javascript, dom, async, frontend, oop-design, api-design
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/chainable-rectangle-manipulation-api
- **related discussions**: 1

### Statement

Implement a chainable JavaScript class for selecting rectangles in a supplied 3-by-3 HTML grid, changing their color, delaying subsequent actions, and shifting them while preserving method-chain execu...

---

## 3. Analytics Engineer: SQL + Python Sessionization

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-06
- **stage**: tech-screen
- **access**: Members
- **roles**: ds
- **tags**: sql, python, data-analysis, csv
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/analytics-engineer-sql-python
- **related discussions**: 1

### Statement

Analytics Engineer technical: SQL over a sharded user table plus its edit log to answer time-windowed residency questions, then a Python task to sessionize an event log with a 30-minute cap.

---

## 4. Security-Flavored Coding: Constrained Data Structure & Input Validation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: security, data-structure, string, concurrency
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/security-coding-constrained-ds-validation
- **related discussions**: 1

### Statement

Staff security-track coding: implement a resource-constrained data structure and a hardened string validator, with the interviewer repeatedly probing behavior under high concurrency, malicious input, ...

---

## 5. Word Frequency from a Large Paragraph

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-09-11
- **stage**: tech-screen
- **access**: Members
- **roles**: swe
- **tags**: string-processing, parsing, hashmap, sorting, memory-bounded, parallelism, medium
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/word-frequency-large-paragraph
- **related discussions**: 2

### Statement

Parse a potentially very large string into case-insensitive words, ignore punctuation, count occurrences, and return formatted word/count entries sorted by frequency. Follow-ups move from the in-memor...

---

## 6. Fractional Share Buy/Sell Inventory

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: hashmap, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/fractional-share-inventory
- **related discussions**: 3

### Statement

Process a sequence of fractional-share buy/sell operations and maintain the resulting inventory. A recurring Robinhood screen/onsite coding problem solvable with a hashmap.

---

## 7. Friend / Money-Transfer Request Parsing (OOD)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: oop-design, parsing, simulation, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/friend-money-transfer-ood
- **related discussions**: 3

### Statement

Parse a stream of request strings (sign up, send/acknowledge friend request, send money) into a small object model and output each user's final balance, validating invalid requests. Graded on working,...

---

## 8. Weekly Calendar (React)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, medium
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/weekly-calendar-react
- **related discussions**: 2

### Statement

Frontend coding round: build a 7-day weekly calendar grid in React where clicking a time cell opens a dialog to create a named event. Graded heavily on state management and avoiding two sources of tru...

---

## 9. Job Scheduler System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-system-design
- **access**: Free preview
- **roles**: SWE
- **tags**: system-design, job-system, scheduling, idempotency, retry, distributed-systems
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/job-scheduler-design
- **related discussions**: 7

### Statement

Design a distributed job scheduler that runs typed jobs under an SLA, surfaces failures and logs, and guarantees each job runs at most once. The anchor Robinhood system-design prompt, especially for Senior+; often delivered nearly word-for-word and open-book.

---

## 10. Stock Trading / Real-Time Quote System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-23
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: system-design, trading, api-integration, scaling, data-modeling
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/stock-trading-quote-system-design
- **related discussions**: 4

### Statement

Design Robinhood's core stock product: place buy/sell orders against an external exchange API and/or serve real-time quotes and historical price ranges. The second fixed system-design prompt alongside...

---

## 11. Photo Album Frontend System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, data-modeling, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/photo-album-frontend-design
- **related discussions**: 2

### Statement

Frontend-flavored system design for a photo-album app: album list, album detail with add/rename, and a photo-info view, with cross-device real-time sync. Centers on state management and data normaliza...

---

## 12. Recruiter / HR Screen

- **category**: behavioral
- **duration**: 30 min
- **frequency**: Medium
- **last asked**: 2026-07-16
- **stage**: recruiter-screen
- **access**: Members
- **roles**: swe
- **tags**: behavioral, recruiter, screening, why-company
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/recruiter-hr-screen
- **related discussions**: 7

### Statement

A ~15-minute recruiter/HR call covering motivation, logistics, and light behavioral signals. Treated as a real screen, not a formality — a fast reject can follow when there's no headcount.

---

## 13. Project Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-03
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: swe
- **tags**: deep-dive, behavioral
- **url**: https://www.1point3acres.com/interview/problems/company/robinhood/project-deep-dive
- **related discussions**: 12

### Statement

A near-universal onsite round: present a past project in depth and defend its design under detailed questioning. Graded as rigorously as the technical rounds and a frequent reason candidates fail.

---
