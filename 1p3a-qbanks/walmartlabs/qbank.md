# Walmart Labs — 1point3acres Question Bank (23 curated questions)

Source: 1point3acres qbank (`company=walmartlabs`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/walmartlabs>

The site lists 74 entries for this company: **23 curated questions** (below), 51 OJ links, 0 external links. Only the curated 23 are captured here.

> Walmart's engineering loop runs through Karat / HackerRank phone screens followed by a three-to-four round virtual or in-person onsite covering coding, system design, behavioral, and an extended Team Fit phase. Coding bars are moderate but heavily weighted toward Java / Spring Boot fluency, and the system-design slot leans toward retail and logistics prompts (membership, delivery, ticketing, sensor ingestion). The Team Fit phase that follows technical approval is unusually long and team-shopping in nature, and is responsible for a meaningful share of loop attrition.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Longest Even-Length Word
2. Range Module (LC 715)
3. Course Schedule II (LC 210)
4. Merge Strings Alternately
5. Minimum Talent-Complete Team from Every Start
6. Permutation Maximizing Σ B[i] where B[i] > A[i]
7. Insert Spaces Around Palindrome Layers
8. Merge Intervals Returning Original Start/End Indices
9. Live Refactor: Function → Production Service
10. Remove m Elements to Minimize Unique Count
11. Two Sum with Smallest-Indices Tiebreak (+ 3 Sum Follow-up)
12. Plants Pesticide — Days Until Stable
13. Validate Nonogram Solution
14. Validate N×N Grid as Latin Square
15. Course Overlap by Student ID
16. Design Ticketmaster — Event Booking & Seat Reservation
17. Design Multi-Carrier Package Delivery Routing System
18. ML System Design — Predict Item Category
19. Design Walmart+ Membership System
20. Design State-Wide Temperature Sensor Ingestion
21. Walmart Behavioral / HM Loop
22. Spring Boot Verbal Quiz
23. AI-Assisted Full-Stack OA (Spring Boot + React)

---

## 1. Longest Even-Length Word

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-05
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: string, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/longest-even-length-word
- **related discussions**: 1

### Statement

Given a space-delimited English sentence, return the longest even-length word; on a length tie, return the earliest one. Return "00" when the input is null or empty, or when no even-length word exists...

---

## 2. Range Module (LC 715)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-04
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: interval, binary-search, data-structure, hard
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/range-module-lc-715
- **related discussions**: 1

### Statement

The first technical phone screen used LC 715 Range Module unchanged: implement add, remove, and full-coverage query operations over tracked half-open intervals. The sharp edge is boundary correctness ...

---

## 3. Course Schedule II (LC 210)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-30
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: graph, topological-sort, cycle-detection, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/course-schedule-ii-lc-210
- **related discussions**: 1

### Statement

Both the phone screen and physical onsite coding round used LC 210 Course Schedule II. Given a course count and prerequisite pairs, return any valid order that completes every course, or an empty arra...

---

## 4. Merge Strings Alternately

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-07
- **stage**: tech-screen
- **access**: Members
- **roles**: swe
- **tags**: string, easy
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/merge-strings-alternately
- **related discussions**: 1

### Statement

Merge two strings by taking characters alternately, beginning with the first string, then append the unmatched suffix when one string is exhausted. The candidate explicitly identified the prompt as un...

---

## 5. Minimum Talent-Complete Team from Every Start

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-07
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: array, sliding-window, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/minimum-talent-complete-team-from-each-start
- **related discussions**: 1

### Statement

Given a student talent array whose values range from 1 through talentsCount, return for every starting index the length of the shortest contiguous segment containing every talent; return -1 when no su...

---

## 6. Permutation Maximizing Σ B[i] where B[i] > A[i]

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-05-07
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: array, greedy, two-pointer, sorting, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/permutation-max-greater-sum
- **related discussions**: 1

### Statement

Given two equal-length integer arrays A and B, choose a permutation of B that maximizes `s = Σ B[i] for indices where B[i] > A[i]`. Return that maximum.

---

## 7. Insert Spaces Around Palindrome Layers

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-24
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, mle
- **tags**: string, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/shrink-palindrome-spaces
- **related discussions**: 1

### Statement

Given a palindrome string, split it into nested mirror layers and join them with single spaces, separating the symmetric outer characters from the innermost asymmetric core.

---

## 8. Merge Intervals Returning Original Start/End Indices

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-24
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: array, interval, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/merge-intervals-with-indices
- **related discussions**: 1

### Statement

Given a list of intervals, return the merged intervals along with the start and end original indices that contributed to each merged span. The HackerRank harness expects printed output with the contri...

---

## 9. Live Refactor: Function → Production Service

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-06
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: backend, api-integration, refactoring, design-implementation, oop-design, testing
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/function-to-service-refactor
- **related discussions**: 1

### Statement

After solving a routine scheduling problem in HackerRank, candidates are asked to refactor the function into a production-quality REST service on the same platform, then defend the design under aggres...

---

## 10. Remove m Elements to Minimize Unique Count

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: array, hashmap, heap, greedy, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/remove-m-minimize-unique-elements
- **related discussions**: 1

### Statement

Given an integer array and a budget `m`, remove exactly `m` elements such that the number of remaining distinct values is minimized. Return that minimum distinct count.

---

## 11. Two Sum with Smallest-Indices Tiebreak (+ 3 Sum Follow-up)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: hashmap, array, 3sum, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/two-sum-smallest-indices
- **related discussions**: 1

### Statement

Given an unsorted array and a target sum, return the indices `(i, j)` of any two elements summing to the target. If multiple pairs exist, return the one with the smallest `i`; on ties, the smallest `j...

---

## 12. Plants Pesticide — Days Until Stable

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-24
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: monotonic-stack, stack, array, simulation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/plants-pesticide-days
- **related discussions**: 1

### Statement

Given a row of plants with pesticide amounts, each day any plant whose pesticide is strictly greater than its left neighbor dies. All dying plants are removed simultaneously. Return the number of days until no more plants die.

---

## 13. Validate Nonogram Solution

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-31
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: array, grid, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/validate-nonogram
- **related discussions**: 1

### Statement

Given a binary matrix of `B` (black) / `W` (white) cells plus a per-row and per-column list of black-run lengths, verify that the matrix is a valid solution to the nonogram puzzle described by those r...

---

## 14. Validate N×N Grid as Latin Square

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-31
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: array, grid, hashing, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/validate-square-grid
- **related discussions**: 1

### Statement

Given an N×N integer grid, return whether each row and each column is a permutation of 1..N. The grid is rectangular only if N equals the number of columns — non-square inputs fail immediately.

---

## 15. Course Overlap by Student ID

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-31
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: hashmap, string, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/course-overlap
- **related discussions**: 1

### Statement

Given a list of `(student_id, course_name)` pairs, output for every unordered pair of distinct students the list of courses they both take. The output keys are formatted as the two student IDs joined ...

---

## 16. Design Ticketmaster — Event Booking & Seat Reservation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: system-design, distributed-lock, caching, concurrency, search, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/design-ticketmaster-event-booking
- **related discussions**: 1

### Statement

Design a ticket booking site supporting event browsing, search, and seat-level booking, with strong guarantees against double-booking and the ability to handle traffic spikes from high-demand on-sales...

---

## 17. Design Multi-Carrier Package Delivery Routing System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: system-design, api-integration, messaging, circuit-breaker, data-modeling, backend
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/design-multi-carrier-delivery-system
- **related discussions**: 1

### Statement

Design a system that ships customer packages through multiple delivery partners (UPS, USPS, FedEx, last-mile contractors), choosing a partner per package based on cost, capacity, and SLA, and tracking...

---

## 18. ML System Design — Predict Item Category

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-17
- **stage**: onsite-system-design
- **access**: Members
- **roles**: ds, mle
- **tags**: mlsd, classification, evaluation, calibration, transformer, retrieval
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/mlsd-predict-item-category
- **related discussions**: 1

### Statement

Build an ML system that predicts the category of a new catalog item (e.g. "Outdoor → Camping → Tents") from its title, description, attributes, and images. Cover data, labels, model architecture, serv...

---

## 19. Design Walmart+ Membership System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-20
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: system-design, caching, state-machine, idempotency, payment, data-modeling
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/design-membership-system
- **related discussions**: 1

### Statement

Design a paid-membership system (Walmart+ analog) covering signup, recurring billing, entitlement checks at read time, family / household plan sharing, and graceful handling of payment failures and ca...

---

## 20. Design State-Wide Temperature Sensor Ingestion

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: system-design, ingestion, streaming, top-k, geohash, kafka, redis
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/design-sensor-temperature-ingestion
- **related discussions**: 2

### Statement

Design an ingestion pipeline for a temperature sensor every N square miles across a state. Serve three queries: the latest temperature per sensor, the global top-K hottest sensors, and a real-time hea...

---

## 21. Walmart Behavioral / HM Loop

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-30
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: swe, mle, ds
- **tags**: behavioral, bq, conflict, leadership, impact, culture-fit, star
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/walmart-bq-loop
- **related discussions**: 5

### Statement

Two behavioral surfaces appear across Walmart loops: a 45-60 minute HM round during the main onsite, and additional behavioral content woven into every Team Fit conversation afterward. Both lean towar...

---

## 22. Spring Boot Verbal Quiz

- **category**: other
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-12-31
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: backend, cs-fundamentals, verbal
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/spring-boot-verbal-quiz
- **related discussions**: 1

### Statement

An open-format verbal block appended to the coding round, drilling Spring Boot fundamentals, web-app design hygiene, environment management, accessibility, and request-lifecycle hooks.

---

## 23. AI-Assisted Full-Stack OA (Spring Boot + React)

- **category**: other
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-09-03
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: oa, ai-tools, fullstack, frontend, backend, debugging, new-round
- **url**: https://www.1point3acres.com/interview/problems/company/walmartlabs/ai-assisted-fullstack-oa
- **related discussions**: 3

### Statement

A 90-minute take-home OA pairs one algorithm problem with two full-stack bug-fix tasks (frontend + backend) inside a packaged Spring Boot + React project, using an in-platform AI assistant of intentio...

---
