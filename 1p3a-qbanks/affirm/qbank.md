# Affirm — 1point3acres Question Bank (13 curated questions)

Source: 1point3acres qbank (`company=affirm`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/affirm>

The site lists 40 entries for this company: **13 curated questions** (below), 27 OJ links, 0 external links. Only the curated 13 are captured here.

> Affirm's loop leans on realistic fintech coding problems—loan aggregation, transaction reconciliation, fraud detection, promotion redemption—run on HackerRank with long boilerplate that rewards careful reading over algorithmic flash. System design centers on experimentation platforms and payment data models; hiring-manager rounds favor 'how did you' probing over scripted STAR. Expect strong signal on requirement comprehension and clean data-structure manipulation, plus a recruiting process that can be slow or go quiet.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Loan Aggregation & Transaction Matching (Parent-Company Resolution)
2. Fraud Detection: Debug + Event Matching
3. Card Game Simulation
4. Transaction Balance & Minimum Settlement
5. Shortest Unique Substring
6. Hit Counter
7. Promotion Offer Redemption Tracking
8. User Trust Score from Transaction Logs
9. Transaction Aggregation by 24-Hour Buckets
10. Design an A/B Testing / Experimentation Platform
11. Payment Processing for Installment Loans
12. Venmo-Style P2P Payment Data Model
13. Hiring Manager & Behavioral Rounds

---

## 1. Loan Aggregation & Transaction Matching (Parent-Company Resolution)

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-21
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: hashmap, payment, code-reading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/loan-aggregation-transaction-matching
- **related discussions**: 9

### Statement

A multi-part live-coding problem: resolve each company to its topmost parent in a parent/child hierarchy, aggregate loans onto that top-level company, then match incoming transactions to the right loan. The challenge is reading comprehension and clean map manipulation over long boilerplate, not algorithms.

---

## 2. Fraud Detection: Debug + Event Matching

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: hashmap, payment, code-reading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/fraud-detection-event-matching
- **related discussions**: 5

### Statement

A fraud-detection coding round in two to three parts: fix bugs in a provided DistinctPIIValuesCounter event consumer (count unique PII values across underwriting events), then implement a FraudDetecto...

---

## 3. Card Game Simulation

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/card-game-simulation
- **related discussions**: 5

### Statement

Simulate a turn-based card game between two players with a 1–52 deck, scoring each round, then extend to N players and M cards. Pure simulation with edge-case handling on uneven deals.

---

## 4. Transaction Balance & Minimum Settlement

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: greedy, heap, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/transaction-balance-min-settlement
- **related discussions**: 1

### Statement

Two parts: compute end-of-day balances from a day's transactions, then settle a set of positive/negative balances to zero using the minimum number of transactions (debt-settlement / minimum cash-flow)...

---

## 5. Shortest Unique Substring

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-17
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: string, trie, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/shortest-unique-substring
- **related discussions**: 1

### Statement

Given a list of words, return for each word its shortest substring that is unique among all the words (a LeetCode shortest-unique-substring–style variant).

---

## 6. Hit Counter

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-01
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: queue, metrics, data-structure, easy
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/design-hit-counter
- **related discussions**: 1

### Statement

Implement a hit counter that records timestamped hits and returns the number of hits in the last 5 minutes (LeetCode 'Design Hit Counter'–style), with the interviewer probing details closely.

---

## 7. Promotion Offer Redemption Tracking

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: hashmap, payment, simulation, csv, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/promotion-offer-redemption
- **related discussions**: 3

### Statement

Process a stream of offer-redemption events against promotions that have validity windows and per-user redemption caps, then output each user's still-redeemable offers as of a processing date. Pure ma...

---

## 8. User Trust Score from Transaction Logs

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-14
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: hashmap, simulation, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/user-trust-score-from-logs
- **related discussions**: 3

### Statement

A two-part log-processing problem: find users active across two days with enough order-type variety, then compute a 'trust score' for a new purchase from a user's historical order types and amount ran...

---

## 9. Transaction Aggregation by 24-Hour Buckets

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-12-08
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: hashmap, payment, csv, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/transaction-24h-aggregation
- **related discussions**: 2

### Statement

Read transaction logs (CSV), filter to completed transactions using a given status enum, then aggregate amounts into a nested map bucketed by how many hours ago each transaction occurred (a 'past 24 h...

---

## 10. Design an A/B Testing / Experimentation Platform

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-14
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: ab-testing, metrics, experiment-design
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/ab-testing-platform-design
- **related discussions**: 4

### Statement

Design an end-to-end A/B testing / experimentation platform: experiment configuration, traffic / bucket allocation, and metrics collection. A collaborative round that goes deep into trade-offs.

---

## 11. Payment Processing for Installment Loans

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-11
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: payment, messaging, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/installment-loan-payment-processing
- **related discussions**: 1

### Statement

Design and partially implement the repayment pipeline for installment loans: accept user-initiated and scheduled payment requests, debit the user's bank through a synchronous API that is only operatio...

---

## 12. Venmo-Style P2P Payment Data Model

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-06-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: payment, data-modeling, schema-design
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/venmo-payment-data-model
- **related discussions**: 1

### Statement

Design the data model for a Venmo-style P2P payments system, with table schemas that correctly support balance transfers and bank-funded transfers including the pending / clearing window and failure h...

---

## 13. Hiring Manager & Behavioral Rounds

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-25
- **stage**: recruiter-screen · onsite-deep-dive · onsite-bq · final-round
- **access**: Members
- **roles**: swe
- **tags**: behavioral, bq, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/affirm/hiring-manager-behavioral
- **related discussions**: 10

### Statement

Affirm's behavioral and hiring-manager rounds: a manager screen, an HM round that favors 'how did you' probing over scripted STAR, a detailed project deep-dive, and a lighter PM conversation. Common p...

---
