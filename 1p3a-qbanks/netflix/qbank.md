# Netflix — 1point3acres Question Bank (40 curated questions)

Source: 1point3acres qbank (`company=netflix`), local capture 2026-08-26 (bank now paywalled).
Company page: <https://www.1point3acres.com/interview/problems/company/netflix>

The site lists 139 entries for this company: **40 curated questions**, all captured below, 57 OJ links, 42 external links.

> Netflix's current engineering loop is high-signal and repeat-heavy: Ads candidates see the same topo / frequency-cap / ads data-modeling trio, Member-Commerce-Games candidates see homepage dedupe and duplicate-detection variants, and Infra / Data Platform candidates see cache, latency, and concurrency rounds. The technical prompts are usually solvable, but the grading is communication-heavy: candidates are expected to clarify vague requirements, write tests, explain trade-offs, and connect their past work tightly to the target org.

Question statements only. The site's answer body is not reproduced here; the full captured page is archived verbatim under `raw/`.

## Index

1. Ads Audience Targeting / Custom Audience System
2. Ads Demand Intake Data Modeling
3. Ads Frequency Cap / Limiter
4. Basic Calculator
5. Command Executor with Undo and Tags
6. Count Disjoint String Pairs
7. Data Engineering Movie Success Pipeline
8. Duplicate Detection I / II / III
9. Error Rate Window Monitor
10. File Backup System
11. First Missing Positive
12. Global Config Deployment System
13. Homepage Video Recommendation System
14. Homepage Viewport / Shelf Dedupe
15. Human-Readable Duration Timer
16. JSON Path Query / Inverted Index
17. Latency Tracker with Percentile Window
18. Meeting Rooms (Conflict Check)
19. Merge Intervals
20. Compute Resource Scheduling for ML Jobs
21. ML Research Orals (Self-Attention / LoRA / Optimizers)
22. MLP Portal Full-Stack Design
23. Movie Billboard Rotation Service
24. Movie History Friends I / II
25. Music Playlist History
26. Netflix Culture / Domain Manager Rounds
27. Number Pairs That Match Target
28. Promotion Posting System Data Model
29. Reconstruct Itinerary
30. Resilient / Versioned In-Memory File System
31. Social Media Sentiment Tracking
32. Sort Shows by User Preference
33. Spam Email Detection
34. String to Integer (atoi)
35. Subscription Billing System (300M Subscribers)
36. Thread-Safe KV Store / Atomic Counter / Blocking Queue
37. Topological Sort / Course Schedule for Ads
38. User Engagement Pattern Classification
39. WAL Log Enrichment Pipeline
40. Weighted Cache / Timed Cache

---

## 1. Ads Audience Targeting / Custom Audience System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-24
- **stage**: onsite-system-design  onsite-deep-dive
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: ads, caching, redis, scaling, schema-design, distributed-systems, idempotency, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/ads-audience-targeting-system

### Statement

Design a system where advertisers upload first-party user lists (emails / phones / device IDs), match them to internal users, package them with AND/OR/NOT rules, and answer membership checks in under 10ms at 200M-user scale.

---

## 2. Ads Demand Intake Data Modeling

- **category**: system-design
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-05
- **stage**: onsite-system-design  onsite-deep-dive
- **access**: Members
- **roles**: SWE, PM
- **tags**: ads, data-modeling, schema-design, database, versioning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/ads-demand-data-modeling

### Statement

Model the demand side of Netflix Ads: advertiser accounts, campaigns, ad groups, creative, targeting, orders, budgets, impressions, and measurement. Candidates are expected to proactively cover bidding and targeting instead of waiting for the interviewer to ask.

---

## 3. Ads Frequency Cap / Limiter

- **category**: system-design
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-05
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: ads, rate-limiting, redis, kafka, scaling, idempotency, distributed-systems, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/ads-frequency-cap-system-design

### Statement

Design a system that enforces per-user / per-campaign ad frequency caps, often with special handling for strict one-ad caps and budget smoothing. The round expects a deep dive into Redis atomicity, queue keying, read/write path separation, and offline aggregation.

---

## 4. Basic Calculator

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, parsing, string-processing, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/basic-calculator

### Statement

Evaluate a string expression with +, -, parentheses and unary minus without eval(); a stack handles nested parentheses and sign state.

---

## 5. Command Executor with Undo and Tags

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-30
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, stack, linked-list, oop-design, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/command-executor-with-undo-and-tags

### Statement

Design and implement a command executor that can execute commands and undo either the most recent command or the most recent command carrying a given tag. Interviewers probe data structures and time complexity.

---

## 6. Count Disjoint String Pairs

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-02
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bitmask, string, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/count-disjoint-string-pairs

### Statement

Count unique index pairs (i<j) of lowercase strings that share no common character; bitmask each word's 26-letter set and AND the masks.

---

## 7. Data Engineering Movie Success Pipeline

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-25
- **stage**: phone-screen
- **access**: Members
- **roles**: DS
- **tags**: data-engineering, sql, etl, streaming, data-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/data-engineering-movie-success-pipeline

### Statement

DataEng phone screens combine SQL, pipeline design, production data-quality concerns, and a short event-stream coding exercise around movie launch success.

---

## 8. Duplicate Detection I / II / III

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-26
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, set, sliding-window, bucket-sort, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/duplicate-detection-three-part-series

### Statement

A three-part duplicate-detection series often restyled as watch-history episode IDs or show names. The third part may require bucketization or a longest-unique-subarray variant.

---

## 9. Error Rate Window Monitor

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-14
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, sliding-window, easy
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/error-rate-window-monitor

### Statement

Given per-time error rates, decide if a system is healthy at a time point: every rate within [timePoint-timeRange, timePoint+timeRange] (clipped to valid indices) must be strictly below a threshold.

---

## 10. File Backup System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-01
- **stage**: phone-screen  onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: distributed-systems, storage, idempotency, checkpointing, scaling, persistence, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/file-backup-system

### Statement

Design a source?destination file backup that mirrors directory structure, supports full + incremental runs, resumes after crashes, and verifies integrity  anchored on a durable manifest as source of truth and stage-then-commit (.tmp+rename / multipart-complete) for atomic writes.

---

## 11. First Missing Positive

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-11
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/first-missing-positive

### Statement

Return the smallest positive integer absent from an unsorted array in O(n) time and O(1) extra space.

---

## 12. Global Config Deployment System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: ci-cd, distributed-systems, versioning, caching, config-management, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/global-config-deployment-system

### Statement

Design a control plane that publishes versioned config, rolls it out progressively by region / cohort with bake times and health gates, and rolls back instantly to a known-good immutable version.

---

## 13. Homepage Video Recommendation System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-31
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, SWE
- **tags**: recommendation, mlsd, ranking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/homepage-video-recommendation-system

### Statement

Open ML-system-design prompt: recommend videos for the Netflix home page. Reported rounds skew toward high-level service architecture (how many services, how they talk) over feature engineering / modeling / evaluation.

---

## 14. Homepage Viewport / Shelf Dedupe

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-02
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dedup, set, array, ambiguous-spec, testing, caching, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/homepage-viewport-shelf-dedupe

### Statement

Given Netflix homepage shelves, dedupe titles shown in the viewport. The prompt is deliberately vague: candidates must clarify horizontal shelf windows, vertical scrolling, global vs local dedupe, insertion in the middle, and production-scale trade-offs.

---

## 15. Human-Readable Duration Timer

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-25
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: recursion, math, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/human-readable-duration-timer

### Statement

Recursively format a non-negative seconds count into months/weeks/days/hours/minutes/seconds (30-day months), hiding zero units but always showing seconds  no date/time libraries allowed.

---

## 16. JSON Path Query / Inverted Index

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-24
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: json, parsing, search, hashmap, recursion, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/json-path-query-and-inverted-index

### Statement

Two related search/parsing prompts appear in recent Netflix rounds: implement a mini `jq` path query with wildcard support, or build an inverted index that supports word and phrase search over documents.

---

## 17. Latency Tracker with Percentile Window

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-24
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: streaming, sliding-window, concurrency, threading, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/latency-tracker-percentile-window

### Statement

Implement a thread-safe latency tracker that records `(timestamp, latency)` samples and returns P90 / P99 over a requested time window. Infra variants emphasize concurrency and cleanup.

---

## 18. Meeting Rooms (Conflict Check)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-13
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, sorting, easy
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/meeting-rooms-conflict-check

### Statement

Given meeting intervals, decide if one person can attend all of them with no overlap; an end time equal to the next start is not a conflict.

---

## 19. Merge Intervals

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, sorting, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/merge-intervals

### Statement

Merge all overlapping intervals and return the non-overlapping cover; touching endpoints (e.g. [1,2] and [2,3]) count as overlapping.

---

## 20. Compute Resource Scheduling for ML Jobs

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-28
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, Infra Eng
- **tags**: ml-infra, scheduling, gpu, queueing, distributed-systems, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/ml-compute-resource-scheduling

### Statement

Design a scheduler that allocates CPU / GPU resources across multiple clusters for user-submitted ML jobs while keeping utilization high.

---

## 21. ML Research Orals (Self-Attention / LoRA / Optimizers)

- **category**: other
- **frequency**: Low
- **last asked**: 2026-05-09
- **stage**: phone-screen  onsite-research
- **access**: Members
- **roles**: RS, MLE
- **tags**: ml-knowledge, transformer, attention, lora
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/ml-research-orals

### Statement

A rapid-fire ML-knowledge round for research and ML roles. After a project deep-dive the interviewer drills fundamentals  self-attention, LoRA, optimizers, loss functions, ViT vs CNN  then closes with a short, easy coding task.

---

## 22. MLP Portal Full-Stack Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, Infra Eng, SWE
- **tags**: ml-infra, fullstack, frontend, backend, schema-design, ml-experiment, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/mlp-portal-fullstack-design

### Statement

Design a web portal that gives ML engineers and data scientists one place to record experiments, register models, deploy/promote models, and monitor production model health.

---

## 23. Movie Billboard Rotation Service

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, Infra Eng, SWE
- **tags**: heap, ranking, recommendation, caching, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/movie-billboard-rotation-service

### Statement

Build a service that chooses the next title for a user's homepage billboard. Titles have relevance scores, but the service should avoid returning the same title twice in a row when alternatives exist.

---

## 24. Movie History Friends I / II

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-28
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, set, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/movie-history-friends

### Statement

Two related prompts over per-customer watch histories: Part I pairs customers whose last k movies match exactly (same order); Part II pairs customers sharing at least m of their last k watches in any order. Return pairs with the smaller ID first, no duplicates.

---

## 25. Music Playlist History

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-26
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, sorting, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/music-playlist-history

### Statement

Design a MusicPlaylist storing (song, timestamp) plays: add, getAll sorted by timestamp (ties by insertion order), remove a single record, and removeAll records of a song.

---

## 26. Netflix Culture / Domain Manager Rounds

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-05
- **stage**: recruiter-screen  onsite-bq  onsite-deep-dive  final-round
- **access**: Members
- **roles**: All
- **tags**: behavioral, culture-fit, culture, leadership, collaboration, impact, fit
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/netflix-culture-and-domain-manager-rounds

### Statement

Netflix BQ and manager rounds probe culture memo alignment, domain match, feedback, ownership, ambiguity, and whether the candidate can contribute immediately to the target org.

---

## 27. Number Pairs That Match Target

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-01
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, array, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/number-pairs-matching-target

### Statement

Return every distinct expression string "a{op}b" from a list where one of +,-,*,/ yields the target (order matters, integer-only division, no divide-by-zero, dedupe identical strings).

---

## 28. Promotion Posting System Data Model

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-03
- **stage**: onsite-system-design  onsite-deep-dive
- **access**: Members
- **roles**: SWE
- **tags**: data-modeling, schema-design, database, versioning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/promotion-posting-data-modeling

### Statement

Design the relational schema for an internal tool that posts trailers/clips to many social platforms across regions and languages: campaigns, posts, creatives, localized content, multi-level budgets (cents/BIGINT), an approval workflow, and an audit log.

---

## 29. Reconstruct Itinerary

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, backtracking, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/reconstruct-itinerary

### Statement

Order all tickets into one itinerary starting at JFK, using every ticket exactly once and choosing the lexicographically smallest path (Hierholzer / Eulerian path).

---

## 30. Resilient / Versioned In-Memory File System

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-21
- **stage**: oa  phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: filesystem, object-design, snapshot, versioning, persistence, binary-search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/resilient-versioned-file-system

### Statement

Implement or design an in-memory file system that supports CRUD and versions / crash recovery. Follow-ups pivot into persistence, bottlenecks, and production readiness.

---

## 31. Social Media Sentiment Tracking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-02
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, DS, SWE
- **tags**: data-engineering, streaming, mlsd, ingestion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/social-media-sentiment-tracking

### Statement

Design a system that tracks public sentiment toward Netflix on social media over time: ingest posts, classify sentiment, aggregate, and surface trends so the team can see opinion shift around launches or price changes.

---

## 32. Sort Shows by User Preference

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-17
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: ambiguous-spec, sorting, ranking, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/sort-shows-by-user-preference

### Statement

Deliberately open-ended: define what user preferences look like, choose data structures, and rank a list of shows into an ordered list of title IDs  graded on clarification and clean modeling, not the fastest algorithm.

---

## 33. Spam Email Detection

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-01
- **stage**: phone-screen  onsite-system-design
- **access**: Members
- **roles**: MLE, DS, SWE
- **tags**: classification, mlsd, evaluation, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/spam-email-detection

### Statement

Open-ended design + light coding: from a large email corpus, pick spam signals, propose a classification approach, and define accuracy metrics (precision, recall, F1, false-positive rate), reasoning explicitly about the catch-more-spam vs block-real-mail trade-off.

---

## 34. String to Integer (atoi)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/string-to-integer-atoi

### Statement

Implement myAtoi: skip leading whitespace, read an optional sign, parse digits until a non-digit, and clamp to the 32-bit signed range.

---

## 35. Subscription Billing System (300M Subscribers)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: payment, idempotency, scheduling, scaling, transactions, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/subscription-billing-system

### Statement

Design monthly subscription billing for 300M users: charge on the right date, never double-charge (idempotency at Redis + DB + gateway), retry failed payments with a dunning schedule, and survive scheduler / worker failures via a hybrid scheduler + catch-up cron.

---

## 36. Thread-Safe KV Store / Atomic Counter / Blocking Queue

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-31
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: concurrency, threading, in-memory-database, ttl, queue, race-condition, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/thread-safe-kv-and-concurrency-basics

### Statement

Netflix Infra and Data Platform rounds include hands-on concurrency: synchronized KV store, atomic counter, blocking queue, double-checked locking, and TTL map variants.

---

## 37. Topological Sort / Course Schedule for Ads

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-06-04
- **stage**: phone-screen  onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: topological-sort, graph, bfs, backtracking, dependency-resolution, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/topological-sort-course-schedule-ads

### Statement

The Ads coding staple is a Course Schedule / dependency-ordering problem. The base task is to produce or validate a topological order; follow-ups ask for runtime, multiple valid orders, all possible orders, or parallel-course scheduling.

---

## 38. User Engagement Pattern Classification

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-02
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE, DS
- **tags**: hashmap, array, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/user-engagement-pattern-classification

### Statement

Classify each viewing session as Abandoned / Sampled / Completed by watched percentage, find each user's most-common pattern, and return a count of users per dominant pattern.

---

## 39. WAL Log Enrichment Pipeline

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng, DS
- **tags**: data-engineering, kafka, streaming, idempotency, caching, distributed-systems, hard
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/wal-log-enrichment-pipeline

### Statement

Design a CDC pipeline that captures WAL entries at 1M writes/sec, enriches them (IDs ? names/regions via a Redis-fronted lookup), and delivers them in per-key order with exactly-once semantics (Kafka transactions + LSN dedupe), DLQ, and replay.

---

## 40. Weighted Cache / Timed Cache

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-09
- **stage**: phone-screen  onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: data-structure, heap, caching, ttl, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/netflix/weighted-cache-and-timed-cache

### Statement

Implement a cache with either weighted capacity eviction or fixed per-item expiration. Follow-ups focus on cleanup strategy, hot-path `get`, capacity limits, and production trade-offs.

---
