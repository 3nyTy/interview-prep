# Stripe — 1point3acres Question Bank (48 curated questions)

Source: 1point3acres qbank (`company=stripe`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/stripe>

The site lists 250 entries for this company: **48 curated questions** (below), 169 OJ links, 33 external links. Only the curated 48 are captured here.

> Stripe favors long realistic prompts in a full IDE — integration, debugging, refactor — over LeetCode-style algorithms. System design is grounded in payments and regulatory reality, and behavioral signals weigh ownership and written communication as heavily as raw technical depth.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Request Routing System (REGISTER / DISTANCE / ROUTE)
2. AI-Assisted Coding Round (HackerRank)
3. Bikemap Integration
4. Invoice / Payment Reconciliation
5. Mako Debug (Bug Bash)
6. Incident Monitor — Transaction Alert Trigger / Resolve
7. Transaction Fee Calculator
8. Email Subscription Scheduler
9. BitFont / Bitmap Renderer
10. Financial Account Ledger (OA)
11. Six Degrees of Collusion (OA)
12. Matching Contacts
13. Review Assignment (JGit)
14. Account Balance Manager
15. Colander Validation Debug (BugSquash)
16. Time Slot Generator from Weekly Working Hours
17. Factory Cost — Min-Cost Path Across Layers
18. URL / Path Segment Compression
19. RBAC Role Resolver
20. Shipping Cost Calculator
21. Rate Limiter
22. Record Linkage by Weighted Similarity
23. Currency Exchange Rate Converter
24. Feature Flag SDK
25. Http Request Language Preference
26. Payment Reconciliation
27. Worker Task Assignment
28. Tabular Data Neural Network
29. Hierarchical CSV Task / Subtask Formatter
30. Business Account Data Verification
31. Deployment Window Scheduler (OA)
32. Debug: Moshi JSON Library
33. Dataset Validation (CSV with banned + stop words)
34. Merchant Fraud Score (OA)
35. Requests Library Debug (BugSquash)
36. SnakeYAML Debug (Java)
37. Chat Billing Calculation (OA)
38. Dataset Join (joinDataSet OA)
39. Account Scheduler with LRU
40. Request Replay Integration
41. Jupyter / WebSocket Load Balancer (OA)
42. Design a Ledger Service (Record Transaction + Merchant Balance)
43. Fraud Detection System
44. Payment Webhook System
45. Account Takeover Prediction System
46. Metric Counter Library
47. Design a Feature Flag System
48. Hiring Manager Behavioral

---

## 1. Request Routing System (REGISTER / DISTANCE / ROUTE)

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: simulation, string, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/request-routing-haversine-oa
- **related discussions**: 8

### Statement

HackerRank OA. Build a data-center router: register datacenters with lat/lon and capacity, support health toggles, compute great-circle distance, and route incoming requests to the nearest healthy datacenter with available capacity.

---

## 2. AI-Assisted Coding Round (HackerRank)

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-29
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: ai-tools, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/ai-assisted-coding-round
- **related discussions**: 13

### Statement

An onsite/tech-screen round run inside HackerRank with an embedded AI assistant. Candidates are given a problem — most often a rule parser that evaluates accept/block conditions over a list of transactions — and must solve it by driving the AI rather than coding by hand. How you prompt, validate, and reason about the AI's output is the graded signal.

---

## 3. Bikemap Integration

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, http, io, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/bikemap-integration
- **related discussions**: 14

### Statement

Integration-round task built around real HTTP API calls. Read JSON describing geometric/landmark data, hit a remote API to download tiles or static map images, then progressively annotate the images with markers and connecting lines. Speed-coding round — most candidates finish 3 of 5 parts.

---

## 4. Invoice / Payment Reconciliation

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-13
- **stage**: phone-screen · tech-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, simulation, api-integration, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/invoice-payment-reconciliation
- **related discussions**: 11

### Statement

Tech-screen and integration favorite. Given a payment and a list of invoices, decide which invoice the payment settles using progressively looser matching rules. The integration variant adds API calls and authorization headers.

---

## 5. Mako Debug (Bug Bash)

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-06-30
- **stage**: onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: debugging, code-reading, hard
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/mako-debug
- **related discussions**: 14

### Statement

Onsite debug round on the open-source Python Mako template engine. Candidates clone the repo, run failing tests, and locate/fix 2-3 bugs using an IDE debugger. Heavy emphasis on debugger fluency — print-based debugging usually loses signal.

---

## 6. Incident Monitor — Transaction Alert Trigger / Resolve

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-07
- **stage**: oa · tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: streaming, data-structure, interval, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/transaction-logs-trigger-resolve
- **related discussions**: 6

### Statement

HackerRank OA and technical-screen problem. Process sorted `(timestamp, merchant_id, status_code, count)` logs in a 30-second window, trigger per-error alerts when both volume and impact thresholds are met, and resolve active alerts when later traffic causes the condition to lapse.

---

## 7. Transaction Fee Calculator

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-24
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, payment, easy
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/transaction-fee-calculator
- **related discussions**: 4

### Statement

Phone-screen-style string-processing round. Given a CSV of transactions and a fee-rule table, compute the fee per row based on payment provider, payment type, and status.

---

## 8. Email Subscription Scheduler

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, scheduling, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/email-subscription-scheduler
- **related discussions**: 7

### Statement

Onsite coding round. Given a list of users with subscription plans and a schedule of relative offsets for different email types, print the full timeline of emails. Later parts add mid-stream plan changes and plan extensions.

---

## 9. BitFont / Bitmap Renderer

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/bitfont-renderer
- **related discussions**: 6

### Statement

Onsite coding. Take a bitmap-style ASCII glyph alphabet and render a string. Part 2 enforces a line-wrap width; later parts add a decoder/encoder that converts an arbitrary input through the bitmap glyphs.

---

## 10. Financial Account Ledger (OA)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-09-11
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: payment, simulation, parsing
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/financial-account-ledger-oa
- **related discussions**: 1

### Statement

Implement process_ledger(commands) with cumulative funding settlement, business-day and cutoff rules, and per-account ACH limits. Return comma-separated settled-balance query results while preserving chronological command semantics.

---

## 11. Six Degrees of Collusion (OA)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, hashmap, parsing, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/six-degrees-of-collusion-oa
- **related discussions**: 1

### Statement

HackerRank OA titled Six Degrees of Collusion. Parse transaction strings to identify users connected by shared identifiers, compute the size of the target user's fraud ring, then decide whether to block the ring using average risk score with trusted users excluded from the average but retained for connectivity.

---

## 12. Matching Contacts

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-10
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, hashmap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/matching-contacts
- **related discussions**: 1

### Statement

Phone-screen coding task. Match contacts to people by email domain, then filter and rank the matches by each person's stated preferences. A long, verbose prompt where reading speed is the main constraint.

---

## 13. Review Assignment (JGit)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, java, csv, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/review-assignment-jgit
- **related discussions**: 1

### Statement

Integration round (Java). Use JGit to diff two branches of a git repo, take the list of changed files, and from a CSV owner mapping return the owner who owns the most changed files — i.e. auto-assign a PR reviewer.

---

## 14. Account Balance Manager

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-12
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: easy, hashmap, simulation, python, payment
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/account-balance-manager
- **related discussions**: 1

### Statement

Build an account balance management system for a payment platform that processes an ordered list of transactions. The problem grows over three steps: tally final balances per account, reject withdrawals that would overdraft, then cover shortfalls from a platform account so a user's balance never goes negative.

---

## 15. Colander Validation Debug (BugSquash)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: debugging, code-reading, python, hard
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/colander-validation-debug
- **related discussions**: 1

### Statement

Python BugSquash round on a Colander-style validation library. Multiple validators aggregate their errors into a structured dict via `asdict()`; the planted bugs live in error aggregation, nested validation, and default handling during serialization.

---

## 16. Time Slot Generator from Weekly Working Hours

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-30
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: interval, easy
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/time-slot-generator
- **related discussions**: 1

### Statement

Phone-screen. Given a `start_date`, `end_date`, and a weekly-repeating `working_hours` config, generate every 30-minute slot inside the range, with strict boundary handling for the first and last day.

---

## 17. Factory Cost — Min-Cost Path Across Layers

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, graph, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/factory-cost-min-path
- **related discussions**: 1

### Statement

Phone-screen DP/graph problem. Each row of a 2D grid represents a factory type; each element is a single factory with `(distance_from_origin, build_cost)`. Pick one factory per type to minimize total cost, eventually including inter-factory transit distance with arbitrary layer count.

---

## 18. URL / Path Segment Compression

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/url-segment-compression
- **related discussions**: 1

### Statement

Onsite coding. Compress a URL-like string by collapsing inner characters of each segment into a count. Major segments split by `/`, minor segments by `.`. Part 2 enforces a cap on minor-segment count before further compression.

---

## 19. RBAC Role Resolver

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-27
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: access-control, tree, hashmap, tree-traversal, caching, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/rbac-role-resolver
- **related discussions**: 1

### Statement

Design and implement a Role-Based Access Control (RBAC) system that manages user roles across a hierarchical account structure. Roles assigned at a parent account are inherited by all descendant accounts, and the API is built up over several phases from direct lookup to inheritance, reverse user lookup, and role filtering.

---

## 20. Shipping Cost Calculator

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-19
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, interval, simulation, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/shipping-cost-calculator

### Statement

Implement a shipping cost calculator for an online store where the cost depends on the destination country and the product. The problem grows in three steps: flat per-unit pricing, quantity-tiered (volume-discount) pricing, and mixed fixed/incremental tier pricing within a single product.

---

## 21. Rate Limiter

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-06
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: rate-limiting, sliding-window, queue, hashmap, concurrency, threading, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/rate-limiter

### Statement

Design and implement a rate limiter that tracks API access patterns per client key and enforces request limits over a sliding time window. The round builds up from a basic implementation to memory cleanup, tricky edge cases (bursts, out-of-order timestamps, long gaps), and thread safety.

---

## 22. Record Linkage by Weighted Similarity

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, union-find, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/record-linkage-similarity
- **related discussions**: 1

### Statement

Phone-screen coding. Given user records (id, name, email, company) and per-field similarity weights, find all records linked to a target user, with follow-ups for transitive linkage.

---

## 23. Currency Exchange Rate Converter

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, bfs, dfs, hashmap, backtracking, python, hard
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/currency-exchange-rate-converter

### Statement

Build a currency exchange system for a global payments platform that converts money between currencies using a set of given exchange rates. The problem escalates across four phases, from a direct O(1) rate lookup to finding the best-rate conversion path of arbitrary length through a graph of currencies.

---

## 24. Feature Flag SDK

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-10-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: object-design, caching, api-integration, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/feature-flag-sdk

### Statement

Build an SDK that internal company services use to manage and evaluate feature flags. The SDK exposes a simple lookup API and must reason about latency, update propagation, reliability, and targeting rules such as percentage rollouts.

---

## 25. Http Request Language Preference

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-10-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: parsing, string, http, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/http-language-preference

### Statement

Build a system that serves localized content by resolving an HTTP Accept-Language header against the languages a server supports. The problem grows across four parts: exact matches, prefix matching, wildcards, and q-factor quality scores.

---

## 26. Payment Reconciliation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, http, parsing, json, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/payment-reconciliation-integration

### Statement

A hands-on integration assessment where you build a payment reconciliation system that processes payment data and communicates with a clearing service API. It is structured in four escalating parts: parsing payment data, submitting a clearing file, comparing transactions, and handling disputes.

---

## 27. Worker Task Assignment

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-10-09
- **stage**: oa · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: scheduling, greedy, heap, hashmap, simulation, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/worker-task-assignment

### Statement

Build a task-assignment system for a support platform, adding one rule at a time across four parts: balance workload to the least-busy worker, restrict to workers with the required skills, prefer the worker who has handled the same account before (affinity), and skip workers who go offline mid-stream.

---

## 28. Tabular Data Neural Network

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-10-07
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: pytorch, classification, feature-engineering, data-cleaning, ml-experiment, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/tabular-data-neural-network

### Statement

A hands-on ML implementation round: build a neural network that predicts two target labels at once from a table of tabular data. The interviewer grades both your working code and how clearly you explain your data-analysis and modeling choices.

---

## 29. Hierarchical CSV Task / Subtask Formatter

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-09-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: csv, parsing, tree
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/hierarchical-csv-task-formatter
- **related discussions**: 2

### Statement

Parse task and subtask CSV rows and render parent-child output even when rows arrive out of hierarchy order. Later parts change the final-child format and add recursively nested subtasks; exact formatting details require clarification.

---

## 30. Business Account Data Verification

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-09-07
- **stage**: oa · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, parsing, csv, string-processing, python, set, integrity
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/kyc-business-verification
- **related discussions**: 3

### Statement

Build a "Know Your Customer" (KYC) system that reads business accounts from a CSV string and decides whether each one is VERIFIED or NOT VERIFIED. The problem grows in five steps, layering on missing-field checks, description-length limits, blocked generic words, business-name-to-description matching, and finally specific error codes.

---

## 31. Deployment Window Scheduler (OA)

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-24
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: interval, scheduling, csv, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/deployment-window-scheduler-oa
- **related discussions**: 2

### Statement

Build a deployment-window scheduler over a recurring 10,080-minute week. First compute the union of allowed intervals minus all freeze intervals; then support per-window timezone offsets, cross-week wrapping, a lead-time cutoff, a minimum continuous duration, and a limit on returned windows.

---

## 32. Debug: Moshi JSON Library

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: medium, debugging, code-reading, java, json, testing
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/moshi-debug
- **related discussions**: 2

### Statement

A live debugging round on an unfamiliar codebase: you are given a deliberately broken version of the Java JSON library Moshi and must use its test suite to locate and fix the planted bugs while narrating your reasoning.

---

## 33. Dataset Validation (CSV with banned + stop words)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-13
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, easy
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/dataset-validation-csv
- **related discussions**: 3

### Statement

Tech-screen string-processing round. Input is a multi-line CSV string. Part 1 validates non-empty fields, Part 2 filters banned words, Part 3 tokenizes specific columns and counts stop-word occurrences.

---

## 34. Merchant Fraud Score (OA)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-25
- **stage**: oa · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/merchant-fraud-score-oa
- **related discussions**: 2

### Statement

OA. Given transactions, fraud-scoring rules, and a baseline merchant score, compute each merchant's final fraud score by applying rule contributions over their transactions.

---

## 35. Requests Library Debug (BugSquash)

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-06-10
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: debugging, code-reading, python, hard
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/requests-library-debug
- **related discussions**: 2

### Statement

Debug (BugSquash) round on a checkout of the Python `requests` library. Run the failing tests, trace into library internals, and fix the planted bugs. A newer alternative to the Mako round on the same format.

---

## 36. SnakeYAML Debug (Java)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: debugging, code-reading, hard
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/snakeyaml-debug
- **related discussions**: 3

### Statement

Java alternative to the Python Mako debug round. Candidates clone a SnakeYAML-style repo, run failing tests, and locate fixes in YAML parsing internals.

---

## 37. Chat Billing Calculation (OA)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-17
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: payment, simulation, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/chat-billing-oa
- **related discussions**: 3

### Statement

Recent high-frequency HackerRank OA. Compute each user's monthly bill across two plans: pay-as-you-go (per-token) and subscription with a token threshold. If a user has both, split usage proportionally.

---

## 38. Dataset Join (joinDataSet OA)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-09
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/dataset-join-oa
- **related discussions**: 2

### Statement

OA. Implement a 4-part SQL-style join over two CSV-style string lists keyed by a configurable field, progressing from inner join through left join, one-to-many, and skipUnmatched.

---

## 39. Account Scheduler with LRU

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/account-scheduler-lru
- **related discussions**: 2

### Statement

Onsite coding. Implement an `AccountScheduler` that tracks which accounts are locked until what time. Follow-ups add `acquire` and an LRU-based auto-pick.

---

## 40. Request Replay Integration

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-15
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: api-integration, http, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/request-replay-integration
- **related discussions**: 3

### Statement

Integration round. A sequence of API requests where later requests depend on ids returned by earlier ones. Identify the failing requests, extract dynamic ids from the first response, and propagate them through subsequent URLs and bodies.

---

## 41. Jupyter / WebSocket Load Balancer (OA)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-02
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: simulation, scheduling, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/jupyter-load-balancer-oa
- **related discussions**: 3

### Statement

HackerRank OA. Simulate a load balancer that routes long-lived websocket connections across servers. Five progressive parts: round-robin → duplicate-aware → disconnects → capacity limits → SHUTDOWN with eviction and re-route.

---

## 42. Design a Ledger Service (Record Transaction + Merchant Balance)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-29
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, payment, idempotency, storage, hard
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/ledger-record-transaction-design
- **related discussions**: 5

### Statement

Onsite system design. Build a ledger with a `record_transaction` API and a `merchant_balance` query API. Interviewer drills heavily on money modeling and consistency under concurrent writes.

---

## 43. Fraud Detection System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, integrity, payment, classification, ml-infra, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/fraud-detection-system-design
- **related discussions**: 1

### Statement

Design a machine learning system to detect fraudulent transactions on a payments platform. The round blends ML system design with traditional system design, with a heavy emphasis on the end-to-end architecture and how it scales.

---

## 44. Payment Webhook System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-12-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: webhook, payment, system-design, idempotency, retry, circuit-breaker, messaging, queueing, scaling, security, http
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/payment-webhook-system

### Statement

Design a webhook delivery service for a payments platform that notifies merchants when payment events occur (payment success, refund, dispute). The emphasis is on reliability: at-least-once delivery, retries, idempotency, and signature verification.

---

## 45. Account Takeover Prediction System

- **category**: system-design
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2025-10-26
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, integrity, classification, ambiguous-spec, inference, ab-testing
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/account-takeover-prediction-design

### Statement

Design a machine learning system to predict account takeover (ATO) risk for a payments platform. The prompt is deliberately open-ended, so clarifying the scenario's rules and requirements up front is part of the exercise.

---

## 46. Metric Counter Library

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-08-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, metrics, concurrency, memory-bounded, object-design
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/metric-counter-library
- **related discussions**: 2

### Statement

Design a library that lets services collect and aggregate metrics. It must support counting events, querying counts over time windows (last minute / hour / day), and labeling data with tags such as region or status code.

---

## 47. Design a Feature Flag System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-30
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, scaling, messaging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/feature-flag-system-design
- **related discussions**: 2

### Statement

Onsite system design. Build a feature-flag platform that lets engineers roll out flags by percentage / user attributes, evaluated cheaply at scale on the server-side request path.

---

## 48. Hiring Manager Behavioral

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-18
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, why-company, deep-dive, easy
- **url**: https://www.1point3acres.com/interview/problems/company/stripe/hiring-manager-behavioral
- **related discussions**: 12

### Statement

Hiring-manager round. Mostly resume-driven STAR-format questions; recent rounds add an explicit AI-usage sub-block. Recruiter ratings on this round drive the final decision in close cases.

---
