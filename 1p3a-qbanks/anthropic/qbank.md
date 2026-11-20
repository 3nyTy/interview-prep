# Anthropic — 1point3acres Question Bank (35 curated questions)

Source: 1point3acres qbank (`company=anthropic`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/anthropic>

The site lists 134 entries for this company: **35 curated questions** (below), 78 OJ links, 21 external links. Only the curated 35 are captured here.

> Anthropic runs an unusually small, transparent technical question bank — the recruiter typically names which prompt family you'll see days before the round, so candidates can target preparation precisely instead of guessing. Bars are calibrated for problem-solving velocity and follow-up depth rather than novelty; offers are decided by hiring committee + reference check, and the culture round on AI-safety alignment is the single most common rejection point even for candidates who clear every technical stage.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Coding Q2 — File Deduplication
2. Coding Q1 — Image Processing Pipeline
3. Coding Q1 — Concurrent Web Crawler
4. Agents / Coding with LLMs
5. Coding Q3 — Stack Trace Reconstruction
6. Coding Q2 — In-Memory Cache: Bug-Fix + Durability
7. Coding Q4 — Distributed Mode / Median
8. OA — In-Memory Database (4-Level CodeSignal)
9. Coding Q6 — Longest-Match Tokenizer
10. OA — Recipe Manager (4-Level CodeSignal)
11. OA — Bank System with Transfer/Accept and Merge
12. Coding & Design — Weighted Data Batcher with Checkpointing
13. OA — Task Manager (TTL + Snapshot)
14. Coding — Bootloader Instruction Simulator (Fix the Loop)
15. OA — File Systems
16. OA — DNS Resolver (Fellows CodeSignal, Round 1)
17. OA — Worker / Employee Grant Management
18. RL Fundamentals — GRPO Debug
19. ML Programming Screen — QKV Attention & einsum
20. Prompting and Engineering with LLMs (Colab)
21. Performance Engineer Take-Home — Kernel Optimization
22. System Design Q1 — Inference API / Batched GPU Serving
23. Onsite Design Doc Review
24. System Design Q3 — Model Weight Distribution
25. System Design Q4 — 1-on-1 Chat System
26. System Design Q2 — Prompt Playground (Product Design)
27. System Design Q5 — Data Infrastructure
28. ML Configuration System
29. Recruiter Screen — Why Anthropic Deep-Dive
30. Hiring Manager Round — Behavioral & Collaboration
31. Culture Round — AI-Safety Alignment Behavioral
32. Project Deep-Dive — Technical Presentation
33. Mechanistic Interpretability Take-Home — Sample-Aspect Double Descent
34. ML Take-Home (4–5 hours)
35. Performance Modeling — Matmul Arithmetic Intensity

---

## 1. Coding Q2 — File Deduplication

- **category**: coding
- **duration**: 55 min
- **frequency**: Very high
- **last asked**: 2026-09-02
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: filesystem, hashing, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-q2-file-deduplication
- **related discussions**: 19

### Statement

Walk a directory tree and group duplicate files. Two-layer dedup: first by file size, then by content hash. The base problem is short; the follow-ups about IO/CPU bounds, hash choice, and scale dominate the round.

---

## 2. Coding Q1 — Image Processing Pipeline

- **category**: coding
- **duration**: 55 min
- **frequency**: Very high
- **last asked**: 2026-08-30
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: concurrency, multiprocessing, pillow, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-q1-image-processing
- **related discussions**: 18

### Statement

The other Q1 prompt: apply a sequence of image transformations (grayscale, flip, scale, blur, rotate) to a directory of images using a Python imaging library, then optimize with multiprocessing/multithreading for a target wall-clock budget.

---

## 3. Coding Q1 — Concurrent Web Crawler

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-09-09
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE, Infra Eng
- **tags**: concurrency, threading, async, bfs, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-q1-web-crawler
- **related discussions**: 14

### Statement

Implement a same-domain web crawler given a pre-supplied `htmlParser.getUrls(url)` helper. Starts as a single-threaded BFS; the bulk of the round is the follow-up to parallelize it (threads, processes, or async) and reason about scaling across machines.

---

## 4. Agents / Coding with LLMs

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-09-04
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: SWE, RE, RS, MLE
- **tags**: llm, agents, tool-use, prompt-engineering, colab, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/agents-coding-llm-tool-use
- **related discussions**: 9

### Statement

Research-track 1-of-N option: write code in Colab that uses an LLM (via the Anthropic API) as a building block — designing prompts and a tool-use loop to solve a multi-step problem. Open-book on documentation; starter API code is supplied so you don't memorize syntax.

---

## 5. Coding Q3 — Stack Trace Reconstruction

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: two-pointer, string-processing, stack, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-q3-stack-trace
- **related discussions**: 13

### Statement

Given a stream of `enter <func>` / `exit <func>` lines representing a program's execution, reconstruct the active call stack at every point and compress consecutive identical traces. Bonus follow-ups include denoising and handling traces where only the last m frames are visible. The inverse rotation starts from timestamped stack samples and emits ordered start/end events, with recursion, debounce, and streaming constraints.

---

## 6. Coding Q2 — In-Memory Cache: Bug-Fix + Durability

- **category**: coding
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-06-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: caching, persistence, python, kwargs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-q2-lru-cache-durability
- **related discussions**: 9

### Statement

A pre-written `LRU` class wrapping a generic callable is provided. Part 1: find the bug in `generate_key` (it doesn't correctly hash `*args/**kwargs`). Part 2: extend the cache to survive process crashes by persisting state and recovering on restart.

---

## 7. Coding Q4 — Distributed Mode / Median

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-09-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: distributed-systems, map-reduce, bandwidth, python, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-q4-distributed-mode-median
- **related discussions**: 6

### Statement

10 worker nodes hold disjoint shards of a large numeric dataset. Find the global mode (most common value) and global median using `send`/`recv`/`barrier` primitives with a fixed read bandwidth of 10 bytes/sec and inter-node bandwidth of 1 byte/sec. The grade is on iterative optimization, not the first solution.

---

## 8. OA — In-Memory Database (4-Level CodeSignal)

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-07-18
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: oa, codesignal, oop, ttl, snapshot, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/oa-in-memory-database
- **related discussions**: 6

### Statement

The canonical Anthropic OA. CodeSignal, 90 minutes, gated 4-level CRUD on an in-memory key/value/field database. Each level depends on the prior. Modern variants drop compare-set/delete and replace level 4 with backup/restore or look-back time travel.

---

## 9. Coding Q6 — Longest-Match Tokenizer

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-06-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: string-processing, greedy, trie, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-q6-tokenizer
- **related discussions**: 7

### Statement

Given a vocabulary mapping tokens to ids (plus a sentinel `UNK`), implement a longest-match tokenizer over a text string. The base solution is two nested loops; optimizations and follow-ups (consecutive UNK merging, vocab-length bound, trie acceleration) drive the score.

---

## 10. OA — Recipe Manager (4-Level CodeSignal)

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-06-16
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: oa, codesignal, oop, versioning, user-system, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/oa-recipe-manager
- **related discussions**: 6

### Statement

Recent OA variant. CodeSignal, 90 minutes, 4 levels. CRUD on recipes (case-insensitive names), sorting by ingredient count, attaching a user model with edit permissions, and a version-history layer with rollback.

---

## 11. OA — Bank System with Transfer/Accept and Merge

- **category**: coding
- **duration**: 90 min
- **frequency**: Medium
- **last asked**: 2026-06-15
- **stage**: oa
- **access**: Members
- **roles**: SWE, Fellows
- **tags**: oa, codesignal, oop, transactions, merge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/oa-bank-system
- **related discussions**: 5

### Statement

AI Safety Fellow OA variant. CodeSignal, 4 levels on a simple bank: deposits, withdrawals, a two-step `transfer + transfer_accept` flow with on-hold balances, and an account-merge step that has to survive the merged account being recreated later.

---

## 12. Coding & Design — Weighted Data Batcher with Checkpointing

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-05-14
- **stage**: phone-screen · tech-screen · onsite-coding
- **access**: Members
- **roles**: MLE, RE, RS
- **tags**: sampling, checkpointing, iterator, python, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-design-data-batcher
- **related discussions**: 5

### Statement

A research-track 1-of-N coding-and-design problem. Build a `DataBatcher` that samples weighted batches from a `DataRegistry`, supports deterministic save/resume via an `offset` argument, and handles the case where `batch_size` is not divisible by the sum of weights.

---

## 13. OA — Task Manager (TTL + Snapshot)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-08-21
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: codesignal, data-structure, snapshot, ttl, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/oa-task-management
- **related discussions**: 3

### Statement

CodeSignal four-level task manager. Levels 1–2 are CRUD on a per-user task list; Level 3 adds TTL-based auto-deletion; Level 4 requires a time-travel snapshot — `get_task_list(user, at_timestamp)` must return that user's tasks as of any past moment.

---

## 14. Coding — Bootloader Instruction Simulator (Fix the Loop)

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-07-18
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, cycle-detection, parsing, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/coding-bootloader
- **related discussions**: 3

### Statement

SWE coding prompt seen in both phone and virtual-onsite loops. A list of `plus` / `next` / `jump` instructions mutates an accumulator and program counter; first detect the loop, then identify or repair the single bad instruction that makes execution cycle.

---

## 15. OA — File Systems

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-05-28
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: oa, codesignal, oop, filesystem, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/oa-file-systems
- **related discussions**: 2

### Statement

Recently observed CodeSignal OA in the file-system family — implement a simplified file system with paths, directory traversal, and (in the final level) an LLM-related extension. Public details are thin; the third level in particular is reported as bug-prone in the supplied helpers.

---

## 16. OA — DNS Resolver (Fellows CodeSignal, Round 1)

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-05-27
- **stage**: oa
- **access**: Members
- **roles**: Fellows
- **tags**: codesignal, simulation, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/oa-fellows-dns-resolver
- **related discussions**: 2

### Statement

First of two Fellows CodeSignal OAs (Spring 2026 rotation). 90 minutes, Python, proctored. Implement a simplified DNS resolver step-by-step against a fixed unit-test harness — early steps cover basic name lookup, later steps layer aliasing, fallback, error handling, cycle detection, caching, and concurrency.

---

## 17. OA — Worker / Employee Grant Management

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-05-10
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: oa, codesignal, oop, interval, payroll, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/oa-worker-management
- **related discussions**: 3

### Statement

Recent OA variant present on the worker/employee scheduling theme. Track employees clocking in and out, apply promotions taking effect on the next office entry, and aggregate granted bonuses across overlapping work sessions.

---

## 18. RL Fundamentals — GRPO Debug

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-03-02
- **stage**: phone-screen · tech-screen · onsite-coding
- **access**: Members
- **roles**: RE, RS, MLE
- **tags**: reinforcement-learning, grpo, pytorch, ml-knowledge, debugging, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/rl-fundamentals-grpo-debug
- **related discussions**: 2

### Statement

Debug a GRPO training script (typically three implanted bugs), then defend the underlying RL math: ratio clipping, advantage normalization, on-policy vs. off-policy effects. The last 15 minutes pivot to a knowledge interrogation tied to the code you just fixed.

---

## 19. ML Programming Screen — QKV Attention & einsum

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-02-18
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: MLE, RE, RS
- **tags**: transformer, attention, einsum, numpy, pytorch, ml-knowledge, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/ml-programming-screen
- **related discussions**: 2

### Statement

One of the research-track 1-of-N phone screens. ~40 minutes of programming with sequence models / scaled dot-product attention, plus short ML knowledge questions. Comfort with einsum and reading mathematical equations is explicitly called out by the recruiter.

---

## 20. Prompting and Engineering with LLMs (Colab)

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-01-31
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: RE, RS, MLE
- **tags**: llm, prompting, colab, evaluation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/prompting-engineering-with-llms
- **related discussions**: 3

### Statement

Research-track 1-of-N option, distinct from the Agents/Coding round: a Colab-based 55-minute exercise centered on designing prompts for a defined task and engineering the surrounding evaluation/scaffolding code.

---

## 21. Performance Engineer Take-Home — Kernel Optimization

- **category**: coding
- **duration**: 120 min
- **frequency**: Low
- **last asked**: 2025-12-11
- **stage**: take-home
- **access**: Members
- **roles**: Perf Eng
- **tags**: gpu, kernel, optimization, memory-coalescing, loop-unrolling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/performance-engineer-take-home
- **related discussions**: 2

### Statement

Performance Engineer track replaces the standard coding phone screen with a 2-hour AI-allowed take-home: optimize a mocked GPU-kernel-style core function. Scoring is by measured speedup; passing scores reported in the 600/1000 range with ~8× speedup.

---

## 22. System Design Q1 — Inference API / Batched GPU Serving

- **category**: system-design
- **duration**: 55 min
- **frequency**: Very high
- **last asked**: 2026-08-31
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: llm-inference, batching, kv-cache, gpu, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/sd-q1-inference-api
- **related discussions**: 20

### Statement

The default Anthropic system-design round. Design an LLM inference API that batches requests across GPUs efficiently. Interviewers dive deep on a few areas — GPU utilization, scale-out under traffic spikes, prefill/decode separation — rather than walk a full BBYE-style template.

---

## 23. Onsite Design Doc Review

- **category**: system-design
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-31
- **stage**: onsite-system-design · phone-screen
- **access**: Members
- **roles**: EM, SWE, MLE
- **tags**: design-review, critique, new-round, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/onsite-design-doc-review
- **related discussions**: 8

### Statement

A newer onsite round, particularly common for EM candidates and increasingly for senior IC loops. The interviewer hands the candidate a design doc (sometimes one straight from the team's own backlog) and asks them to critique it — surface risks, propose alternatives, identify gaps.

---

## 24. System Design Q3 — Model Weight Distribution

- **category**: system-design
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-21
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: MLE, Infra Eng, SWE
- **tags**: distribution, bandwidth, tree-broadcast, bittorrent, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/sd-q3-model-distribution
- **related discussions**: 13

### Statement

Push a multi-hundred-GB model checkpoint from a model repo to several hundred hosts in a cluster, given a 10 Gbps bandwidth budget on every link. Design the fastest distribution strategy; expect to evolve from naive download → tree broadcast → chunked BitTorrent-style swarm. A phone-screen variant shares one 10 Gbps budget between worker upload and download, then probes deployment observability and readiness-gated routing.

---

## 25. System Design Q4 — 1-on-1 Chat System

- **category**: system-design
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-21
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: chat, websocket, kafka, redis, presence, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/sd-q4-1to1-chat-system
- **related discussions**: 11

### Statement

Design a 1-on-1 chat system (no groups, no channels). Single device per user, focused on message delivery, presence detection, offline-recipient queueing, session storage, and the tradeoff between Kafka and Redis for the message bus.

---

## 26. System Design Q2 — Prompt Playground (Product Design)

- **category**: system-design
- **duration**: 55 min
- **frequency**: High
- **last asked**: 2026-08-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: product-design, schema-design, sharing, ux, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/sd-q2-prompt-playground
- **related discussions**: 11

### Statement

Despite the SD label, Q2 is closer to product design. Design a prompt playground similar to Anthropic's Console. The interviewer supplies the deployment diagram (client → web server → database) and tells you not to draw architecture — focus on user flows, schema, sharing semantics, and UX edge cases.

---

## 27. System Design Q5 — Data Infrastructure

- **category**: system-design
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-09-07
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, DS, Infra Eng
- **tags**: data-engineering, ingestion, etl, access-control, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/sd-q5-data-infrastructure
- **related discussions**: 2

### Statement

A data-engineering / data-infra system design: design a system that ingests large-scale data, processes it, and serves it to many stakeholders with varying access needs and sensitivities. Specifics have not yet been publicly documented; the recruiter blurb spells out the expected coverage.

---

## 28. ML Configuration System

- **category**: system-design
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-04-17
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: RE, RS, MLE
- **tags**: config-management, ml-infra, design, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/ml-configuration-system
- **related discussions**: 3

### Statement

A 1-of-N research-track option. Design and partially implement an ML configuration system — schemas, inheritance/overrides, validation, and reproducibility. Specifics of the task have not been publicly documented; treat this card as the structural shell.

---

## 29. Recruiter Screen — Why Anthropic Deep-Dive

- **category**: behavioral
- **duration**: 30 min
- **frequency**: Very high
- **last asked**: 2026-09-07
- **stage**: recruiter-screen
- **access**: Members
- **roles**: All
- **tags**: recruiter, behavioral, ai-safety, screening
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/recruiter-screen-why-anthropic
- **related discussions**: 18

### Statement

Anthropic's recruiter screen is unusually substantive. Skips resume walkthroughs, opens straight on Why Anthropic, and probes the candidate's familiarity with Anthropic's published research, blog posts, and policy stances. Failing to pass this round prevents reaching technical interviews.

---

## 30. Hiring Manager Round — Behavioral & Collaboration

- **category**: behavioral
- **duration**: 55 min
- **frequency**: Very high
- **last asked**: 2026-09-04
- **stage**: onsite-bq
- **access**: Members
- **roles**: All
- **tags**: behavioral, collaboration, leadership, mentorship, impact
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/onsite-hm-behavioral
- **related discussions**: 21

### Statement

Standard hiring-manager behavioral round, distinct from the Culture round. Focuses on past project impact, collaboration with non-engineering partners, mentorship, and roadmap influence. Reported as the round most likely to feel uneven — HM engagement varies a lot.

---

## 31. Culture Round — AI-Safety Alignment Behavioral

- **category**: behavioral
- **duration**: 55 min
- **frequency**: Very high
- **last asked**: 2026-08-30
- **stage**: onsite-bq
- **access**: Members
- **roles**: All
- **tags**: culture, ai-safety, values, critical-thinking
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/onsite-culture-ai-safety
- **related discussions**: 29

### Statement

Anthropic's culture round is the round that decides the most loops. AI-safety alignment is screened through behavioral framing — interviewers want concrete personal experience that maps to specific Anthropic values, plus the ability to thoughtfully critique Anthropic itself.

---

## 32. Project Deep-Dive — Technical Presentation

- **category**: behavioral
- **duration**: 55 min
- **frequency**: Very high
- **last asked**: 2026-08-21
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: All
- **tags**: presentation, technical-deep-dive, project-retro
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/onsite-project-deep-dive
- **related discussions**: 18

### Statement

Present a past technical project of your choice for 20–25 minutes, then field 25–30 minutes of deep-dive Q&A. Interviewers probe architecture decisions, tradeoffs, your personal role, and what you'd do differently. The round is universal across roles and is one of the most consistent positive signals when done well.

---

## 33. Mechanistic Interpretability Take-Home — Sample-Aspect Double Descent

- **category**: other
- **duration**: 240 min
- **frequency**: Single report
- **last asked**: 2026-04-19
- **stage**: take-home
- **access**: Members
- **roles**: RE, RS
- **tags**: take-home, interpretability, double-descent, linear-regression, experiment-design, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/mech-interp-take-home
- **related discussions**: 1

### Statement

Mechanistic-interpretability team take-home: design and run an experiment that reproduces sample-aspect double descent on a small model, explain why double descent occurs using theory, and propose a fix. Deliverable is a slide deck or notebook plus a live discussion.

---

## 34. ML Take-Home (4–5 hours)

- **category**: other
- **duration**: 240 min
- **frequency**: Low
- **last asked**: 2026-02-13
- **stage**: take-home
- **access**: Members
- **roles**: RE, RS, MLE, Fellows
- **tags**: take-home, ml-experiment, pandas, numpy, matplotlib, live-review, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/ml-take-home-research
- **related discussions**: 2

### Statement

Research-track alternative to the live ML screen. A self-contained 4-hour ML problem on the candidate's own machine or Colab, immediately followed by a 40-minute live review where you present results, walk through code or slides, and field questions. Open-book on docs; no AI tools.

---

## 35. Performance Modeling — Matmul Arithmetic Intensity

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-12-11
- **stage**: onsite-research · tech-screen
- **access**: Members
- **roles**: Perf Eng, MLE
- **tags**: gpu, a100, matmul, arithmetic-intensity, performance-modeling, ml-knowledge, hard
- **url**: https://www.1point3acres.com/interview/problems/company/anthropic/performance-engineer-modeling
- **related discussions**: 2

### Statement

An onsite round for the Performance Engineer track: estimate wall-clock time for matrix multiplications of varying `(m, k, n)` on a given A100 40 GB GPU, then extend to a 72-layer pipeline alternating between two matmul patterns and reason about pipeline/tensor parallelism.

---
