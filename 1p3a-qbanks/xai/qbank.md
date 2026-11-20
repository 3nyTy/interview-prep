# xAI — 1point3acres Question Bank (24 curated questions)

Source: 1point3acres qbank (`company=xai`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/xai>

The site lists 59 entries for this company: **24 curated questions** (below), 19 OJ links, 16 external links. Only the curated 24 are captured here.

> xAI's loop is built around speed and founder-style intensity: a single 15-minute video screen often gates the entire pipeline, the take-home assumes you will use Cursor / Claude Code, and onsite rounds run in person in Palo Alto with offers decided minutes after the final round. Bars are calibrated less for algorithmic novelty than for raw shipping velocity, low-level systems instincts (concurrency, GPU/inference, distributed numerics), and willingness to work 60–100 hour weeks RTO5.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Twitter Insight Platform (CodeSignal Take-Home)
2. Distributed Matrix Multiplication — DP and FSDP
3. Resumable Iterator with Save / Restore State
4. Distributed Token-Bucket Rate Limiter
5. Flatten / Unflatten Nested Python Structure
6. Weighted LRU Cache
7. Checkers Game — Backend System Design
8. Twitter Spaces — Aggregate Active Hours
9. Radix Cache
10. Transactional Key-Value Store
11. Maximum Distinct Integers After ±k Offset
12. K-th Element on a Streaming Time Window
13. Durable KV Cache
14. GPU Node Group Test — Identify Bad Nodes
15. Mock-LLM Inference Engine — Dynamic Batching
16. Multithreaded BankAccount Race-Condition Bug Hunt
17. In-Memory Database — Levels 1–4 (TTL + Backup/Restore)
18. Multithreaded Integer Sort
19. Notification System — Push to Followers on New Post
20. Design a RAG System
21. Rate Limiter — Inbound + Outbound
22. Most Challenging Project + Why xAI
23. Agentic Workflow for 1-Hour Movie Generation
24. Process vs Thread (Verbal CS Fundamentals)

---

## 1. Twitter Insight Platform (CodeSignal Take-Home)

- **category**: coding
- **duration**: 240 min
- **frequency**: Medium
- **last asked**: 2026-01-28
- **stage**: take-home · oa
- **access**: Members
- **roles**: swe, mle, infra-eng
- **tags**: backend, rate-limiting, ingestion, llm-api, docker, ai-allowed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/twitter-insight-platform-take-home
- **related discussions**: 4

### Statement

The most commonly chosen prompt from xAI's 4-hour CodeSignal take-home menu. Pull tweets from a Kaggle dataset, validate them with Pydantic, ingest into a local store, then run a worker that calls the...

---

## 2. Distributed Matrix Multiplication — DP and FSDP

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-04
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: mle, infra-eng, rs, re
- **tags**: distributed-systems, numpy, parallelism, ml-infra, transformer, hard
- **url**: https://www.1point3acres.com/interview/problems/company/xai/distributed-matmul-dp-fsdp
- **related discussions**: 1

### Statement

ML/Infra coding round. You are given a `Communicator` class that simulates inter-device send/recv over Python `Queue`s, plus a partial `dp_mat_mul` and a stub `fsdp_mat_mul`. You complete data-paralle...

---

## 3. Resumable Iterator with Save / Restore State

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-02
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: iterator, state-machine, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/resumable-iterator-design
- **related discussions**: 1

### Statement

Design and implement an iterator that can persist its position to a checkpoint and resume from that checkpoint later, including across process restarts. The bar is a clean state model + correct bounda...

---

## 4. Distributed Token-Bucket Rate Limiter

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: rate-limiting, throttling, concurrency, redis, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/distributed-token-bucket-limiter
- **related discussions**: 1

### Statement

Coding round (distinct from the inbound/outbound rate-limiter system-design prompt): implement a per-user token-bucket limiter with lazy on-read refill, backed by a shared cache so the whole fleet enf...

---

## 5. Flatten / Unflatten Nested Python Structure

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-02-26
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: swe
- **tags**: recursion, tree-traversal, json, easy
- **url**: https://www.1point3acres.com/interview/problems/company/xai/flatten-unflatten-nested-structure
- **related discussions**: 1

### Statement

Two paired sub-problems on nested Python containers. Part 1: walk an arbitrarily nested mix of `list / dict / tuple` whose leaves are `int` and emit a flat `list[int]` in encounter order. Part 2: give...

---

## 6. Weighted LRU Cache

- **category**: coding
- **duration**: 15 min
- **frequency**: Single report
- **last asked**: 2026-02-01
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: data-structure, hashmap, linked-list, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/weighted-lru-cache
- **related discussions**: 1

### Statement

An LRU cache variant where each entry carries a size/weight and the cache is bounded by total size rather than item count, so one insertion may evict several least-recently-used entries. Asked inside ...

---

## 7. Checkers Game — Backend System Design

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: object-design, game-engine, open-ended, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/checkers-backend-design
- **related discussions**: 1

### Statement

Open-ended onsite design-implementation hybrid. Build the backend for a checkers game: data model, board state, move validation, capture logic, win condition. Time is intentionally tight — the intervi...

---

## 8. Twitter Spaces — Aggregate Active Hours

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: interval-aggregation, hashmap, streaming, top-k, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/twitter-spaces-active-hours
- **related discussions**: 1

### Statement

Onsite backend round. Given a chronological event log of `(operation, space_id, user, timestamp)` records covering `create / join / leave`, compute the total user-active-time per Twitter Space. Follow...

---

## 9. Radix Cache

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-24
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: swe, infra-eng, mle
- **tags**: trie, data-structure, kv-cache, design-implementation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/xai/radix-cache

### Statement

Implement a prefix radix cache over integer sequences, including compressed-edge insertion, edge splitting, exact search, longest-prefix match, and sequence enumeration.

---

## 10. Transactional Key-Value Store

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-20
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: in-memory-database, transactions, hashmap, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/transactional-key-value-store

### Statement

Implement an in-memory key-value store with nested transactions, including begin, commit, rollback, read-your-write behavior, and deletion semantics.

---

## 11. Maximum Distinct Integers After ±k Offset

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-15
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: greedy, array, set, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/distinct-integers-after-offset
- **related discussions**: 1

### Statement

Given an integer array and an offset `k`, you may add to each element any **distinct** integer in `[-k, k]` (each offset value can be used at most once across the whole array). Return the maximum numb...

---

## 12. K-th Element on a Streaming Time Window

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: streaming, sliding-window, bucket-sort, memory-bounded, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/kth-element-streaming-window
- **related discussions**: 1

### Statement

Onsite coding round. Base problem is the standard "k-th element" (quickselect / heap). The follow-up is the actual round: extend it to a streaming setting where queries are over the **last N seconds**...

---

## 13. Durable KV Cache

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-04
- **stage**: onsite-coding · tech-screen
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: caching, persistence, filesystem, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/durable-kv-cache

### Statement

Build a key-value cache that survives process restarts by persisting writes to disk, then discuss stale log cleanup, compaction, and large-file storage tradeoffs.

---

## 14. GPU Node Group Test — Identify Bad Nodes

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-14
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: infra-eng, mle
- **tags**: binary-search, parallel-algorithm, gpu, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/gpu-node-group-test
- **related discussions**: 1

### Statement

There are `N` nodes, some good and some bad. You can call `test(S)` on a set `S` of nodes (with `|S| ≥ 2`) which returns `True` only if every node in `S` is good. Tests can run in parallel, but no nod...

---

## 15. Mock-LLM Inference Engine — Dynamic Batching

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-12-14
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: mle, infra-eng, rs
- **tags**: ml-infra, inference, scheduling, transformer, hard
- **url**: https://www.1point3acres.com/interview/problems/company/xai/mock-llm-dynamic-batching
- **related discussions**: 1

### Statement

Implement a simplified inference-engine loop on top of a `model(batch) → next_tokens` API. The bar is dynamic batching: as sequences finish (max-tokens or stop-token), free their slots and fill them w...

---

## 16. Multithreaded BankAccount Race-Condition Bug Hunt

- **category**: coding
- **duration**: 15 min
- **frequency**: Low
- **last asked**: 2026-04-04
- **stage**: phone-screen · tech-screen
- **access**: Free preview
- **roles**: SWE, Infra Eng
- **tags**: concurrency, threading, race-condition, python, easy
- **url**: https://www.1point3acres.com/interview/problems/company/xai/multithreaded-bank-account-bug-hunt
- **related discussions**: 3

### Statement

Inside a 15-minute video screen, the interviewer pastes a small `BankAccount` class with a buggy `deposit` method and asks you to find and fix every concurrency issue. The base bug is a classic read-sleep-write race; the follow-up presses you to rewrite the class without modifying it in place (subclass / wrap), which is where most candidates stumble.

---

## 17. In-Memory Database — Levels 1–4 (TTL + Backup/Restore)

- **category**: coding
- **duration**: 240 min
- **frequency**: Low
- **last asked**: 2026-04-01
- **stage**: oa · take-home
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: data-structure, ttl, in-memory-database, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/in-memory-database-ttl-backup
- **related discussions**: 3

### Statement

CodeSignal-style multi-level OA used for the Backend Engineering Specialist track. You implement an in-memory key/value store with field-level operations, then layer on lexicographic scan, TTL, and fi...

---

## 18. Multithreaded Integer Sort

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-15
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: concurrency, threading, sorting, parallel-algorithm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/parallel-integer-sort
- **related discussions**: 3

### Statement

Sort an integer array, but the solution must use multiple threads. Single-threaded `sort()` is rejected outright. The expected approach is a parallel merge / parallel quick sort that partitions the in...

---

## 19. Notification System — Push to Followers on New Post

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-06
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: fan-out, push-notification, social-graph, queueing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/notification-system-design
- **related discussions**: 1

### Statement

Onsite SD round. Design a push-notification service: when a user creates a new post, every follower receives a notification. The interviewer treats it as a product-flavored design (not pure infra), so...

---

## 20. Design a RAG System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-01-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe, mle, infra-eng
- **tags**: rag, retrieval, llm-api, ml-infra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/rag-system-design

### Statement

Design a retrieval-augmented generation system that connects an LLM to an external knowledge source so answers can be more current, grounded, and domain-specific.

---

## 21. Rate Limiter — Inbound + Outbound

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-28
- **stage**: onsite-system-design · take-home
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: rate-limiting, concurrency, throttling, queueing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/rate-limiter-design
- **related discussions**: 3

### Statement

Either a standalone SD round or an additional layer baked into the take-home prompt. Design a rate limiter that gates both inbound API requests (e.g. 100 req/s) and outbound calls to a downstream cons...

---

## 22. Most Challenging Project + Why xAI

- **category**: behavioral
- **duration**: 15 min
- **frequency**: High
- **last asked**: 2026-06-19
- **stage**: tech-screen · phone-screen · recruiter-screen
- **access**: Members
- **roles**: all-roles
- **tags**: project-deep-dive, behavioral, fit, easy
- **url**: https://www.1point3acres.com/interview/problems/company/xai/most-challenging-project-why-xai
- **related discussions**: 10

### Statement

The two questions every xAI candidate hears, regardless of role or stage: "what was the most challenging project you've worked on" and "why xAI / what do you want to work on here." The bar is technica...

---

## 23. Agentic Workflow for 1-Hour Movie Generation

- **category**: other
- **duration**: 15 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: tech-screen
- **access**: Members
- **roles**: rs, re, mle
- **tags**: ml-knowledge, agentic-workflow, video-generation, open-ended, medium
- **url**: https://www.1point3acres.com/interview/problems/company/xai/agentic-one-hour-movie-generation
- **related discussions**: 1

### Statement

15-minute screen for the Video-Generation / Agent-RL team. After a quick ML-direction intro, the interviewer asks: how would you design an agentic workflow that generates a one-hour-long movie? Open-e...

---

## 24. Process vs Thread (Verbal CS Fundamentals)

- **category**: other
- **duration**: 15 min
- **frequency**: Low
- **last asked**: 2026-01-27
- **stage**: tech-screen · phone-screen
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: cs-fundamentals, concurrency, operating-systems, verbal, easy
- **url**: https://www.1point3acres.com/interview/problems/company/xai/process-vs-thread-verbal
- **related discussions**: 2

### Statement

A short verbal CS-fundamentals question that interviewers tack onto the 15-minute screen, especially for infra / SRE / backend tracks. Be able to walk through the differences between processes and thr...

---
