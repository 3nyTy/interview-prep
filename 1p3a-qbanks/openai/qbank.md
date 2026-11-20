# OpenAI — 1point3acres Question Bank (52 curated questions)

Source: 1point3acres qbank (`company=openai`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/openai>

The site lists 263 entries for this company: **52 curated questions** (below), 164 OJ links, 47 external links. Only the curated 52 are captured here.

> OpenAI pairs an unusually high raw-coding bar with explicit no-AI-tools rules during most interviews — a deliberate contrast with the product. Phone screens often run a single multi-part simulation for the full hour, and senior loops add deep ML and research-grounded system-design rounds that probe mechanism, not vocabulary.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Infection Spread / Cellular Automata
2. Social Network / Follow Graph
3. Distributed Machine Cluster Count and Topology
4. Autograd / Manual Backprop + Hillis-Steele Scan
5. GPU Credits / GPU Credit 2
6. NumPy Puzzle: 1-NN Vectorization → Wx+b Network
7. Monster Battle System
8. Transformer Bug Hunt
9. Memory Allocator
10. Classifier with Noisy Annotators
11. Toy Language / Type Inference
12. Data Labeling Task Scheduler
13. Durable Key-Value Store Serialization
14. Streaming Entropy (numerical stability + online accumulation)
15. Chat Bot System Refactoring
16. IP Address / CIDR Iterator (5-part)
17. Shard Rebalance / Overlapping Key Range
18. Math Reasoning: Stopping Time / Las Vegas Algorithm
19. Version Dependency
20. Fault-Tolerant Work Queue
21. Cross-Entropy Loss Implementation
22. Maximum-Score Grid Path with Limited Jumps
23. Implement a CD Command
24. OpenSheet: Spreadsheet with Cell Dependencies
25. ModalLock and FairModalLock
26. Sharded Matmul and Backprop Debugging
27. Resumable Iterator with Multi-Dimensional Support
28. Code Reading — 400-line PyTorch Refactor
29. In-Memory Database with SQL Operations
30. Time-Based Key-Value Store with Production Testing
31. Payment / Coffee-Shop Ordering (read the prompt!)
32. Design Chess.com (Online Chess Game)
33. Design Sora / Video Generation Pipeline
34. Design a Cloud IDE
35. Real-Time AI Product Feature
36. Mining Novel Data from Large Unlabeled Corpus
37. ChatGPT Enterprise (RAG ML System Design)
38. GPT-3 Playground - Full-Stack Architecture
39. Image Sharing with Content Deduplication
40. Design a Crossword Puzzle Solver
41. Design an AI Chatbot System
42. Multi-Tenant CI/CD Workflow System
43. Slack
44. RAG / Search ML Design (oral)
45. Design Google Calendar
46. Design a URL Shortener
47. Webhook Delivery System
48. Points of Interest (POI) System / Yelp
49. HM BQ — Why OpenAI / AI Safety / AGI
50. Recruiter / HR Screen (BQ)
51. Engineering Management Fundamentals & Team Operations
52. Technical Deep Dive (slide-based)

---

## 1. Infection Spread / Cellular Automata

- **category**: coding
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-09
- **stage**: phone-screen · tech-screen
- **access**: Free preview
- **roles**: SWE, MLE, RE, RS, EM
- **tags**: algorithm, simulation, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/infection-spread-cellular-automata
- **related discussions**: 46

### Statement

M×N grid with infected cells (X) and susceptible cells (./*); infection propagates each day under escalating rules. 5 sub-parts in 60 min; passing bar is typically solving the first 3 with clean BFS.

---

## 2. Social Network / Follow Graph

- **category**: coding
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-08-14
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, RE
- **tags**: algorithm, graph, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/social-network-follow-graph
- **related discussions**: 21

### Statement

Build a follower/followee data structure with timestamped queries and friend-of-friend recommendations.

---

## 3. Distributed Machine Cluster Count and Topology

- **category**: coding
- **frequency**: High
- **last asked**: 2026-09-10
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, tree-broadcast, distributed-systems, recursion, state-machine, idempotency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/distributed-cluster-count-topology
- **related discussions**: 13

### Statement

You are given a tree structure where each node represents a machine in a distributed cluster. Machines can only communicate with their direct parent or children via asynchronous message passing. The problem asks you to implement a receiveMessage() function that enables the cluster to count its own nodes, map the full tree topology, and handle duplicate or failed messages idempotently.

---

## 4. Autograd / Manual Backprop + Hillis-Steele Scan

- **category**: coding
- **duration**: 75 min
- **frequency**: High
- **last asked**: 2026-08-28
- **stage**: tech-screen · onsite-coding
- **access**: Members
- **roles**: RS, RE, MLE
- **tags**: ml-knowledge, autograd, pytorch, parallel-algorithm, math-reasoning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/openai/autograd-hillis-steele-scan
- **related discussions**: 8

### Statement

Given a sequence of square matrices: first write an in-place matmul, then explain why it can't backprop. Write an out-of-place version that works with autograd. Then hand-write the backward. Finally, implement forward + backward using a Hillis-Steele scan.

---

## 5. GPU Credits / GPU Credit 2

- **category**: coding
- **duration**: 75 min
- **frequency**: High
- **last asked**: 2026-08-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: algorithm, data-structure, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/gpu-credits
- **related discussions**: 15

### Statement

Simulate a GPU credit grant system: grant credit with an expiration time, subtract on consumption, query balance. Latest 'GPU Credit 2' variant adds 'subtract affects future + drain from earliest-expiring grant first' semantics.

---

## 6. NumPy Puzzle: 1-NN Vectorization → Wx+b Network

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-21
- **stage**: tech-screen · onsite-coding
- **access**: Members
- **roles**: MLE, RE
- **tags**: ml-knowledge, numpy, linear-algebra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/numpy-1nn-wx-b
- **related discussions**: 10

### Statement

Implement 1-NN classification in pure NumPy (no for-loops). Follow-up: rewrite as the forward pass of a neural network, forced into `Wx + b` form with an activation function.

---

## 7. Monster Battle System

- **category**: coding
- **frequency**: High
- **last asked**: 2026-08-18
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/monster-battle-system
- **related discussions**: 11

### Statement

Design and implement a battle simulation system where two teams of monsters fight each other in turn-based combat. The problem is structured in three progressive parts: basic battle logic, elemental type effectiveness, and an optimized smart-targeting strategy. It is an Object-Oriented Design problem that emphasizes encapsulation, single responsibility, and extensibility.

---

## 8. Transformer Bug Hunt

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-13
- **stage**: tech-screen · onsite-coding
- **access**: Members
- **roles**: MLE, RS, RE
- **tags**: ml-knowledge, transformer, debugging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/transformer-bug-hunt
- **related discussions**: 13

### Statement

A working-ish Transformer + train script + tests is provided with ~4 bugs planted. Find them so loss converges and outputs are correct. The appetizer of any ML coding round — almost guaranteed.

---

## 9. Memory Allocator

- **category**: coding
- **duration**: 75 min
- **frequency**: High
- **last asked**: 2026-08-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE, RE
- **tags**: algorithm, data-structure, hard
- **url**: https://www.1point3acres.com/interview/problems/company/openai/memory-allocator
- **related discussions**: 11

### Statement

LeetCode 'Design Memory Allocator'. `Allocator(size=1000)` with `malloc(size) -> pointer` and `free(pointer) -> bool`. The interviewer explicitly rejects O(n) linked-list solutions and asks for a better approach.

---

## 10. Classifier with Noisy Annotators

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-17
- **stage**: tech-screen
- **access**: Members
- **roles**: MLE, RS, RE
- **tags**: ml-knowledge, data-cleaning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/classifier-noisy-annotators
- **related discussions**: 10

### Statement

A classifier trains on data labeled by multiple human annotators, but some annotators are noisy. Task: identify the bad annotators, exclude their labels, check whether the classifier improves.

---

## 11. Toy Language / Type Inference

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, RE
- **tags**: algorithm, oop-design, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/toy-language-type-inference
- **related discussions**: 9

### Statement

Implement a type system for a toy language with primitives, generics, nested tuples, and function signatures. Core task: a type-inference engine that unifies generics with concrete call-site types and substitutes them in the return type.

---

## 12. Data Labeling Task Scheduler

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, RE, MLE
- **tags**: algorithm, simulation, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/data-labeling-task-scheduler
- **related discussions**: 8

### Statement

Build a scheduler for a data labeling platform: t tasks, m models, h human labelers. Emit a schedule (list of (task, model, human) tuples) under fairness + uniqueness constraints. Tied with infection-spread as the hottest 'new question'.

---

## 13. Durable Key-Value Store Serialization

- **category**: coding
- **frequency**: Medium
- **last asked**: 2026-08-19
- **stage**: onsite-coding · phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: io, filesystem, persistence, hashmap, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/durable-kv-store-serialization
- **related discussions**: 6

### Statement

Implement a persistent key-value store that can serialize and deserialize data to/from a file system. The challenge is designing a custom binary encoding format — without JSON or pickle — that correctly round-trips keys and values containing arbitrary characters (newlines, emojis, null bytes, commas). A follow-up extends the problem to a chunked multi-file layout when each file is capped at 1 KB.

---

## 14. Streaming Entropy (numerical stability + online accumulation)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-26
- **stage**: tech-screen
- **access**: Members
- **roles**: RS, RE, MLE
- **tags**: numerical-stability, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/streaming-entropy
- **related discussions**: 5

### Statement

Given logits, compute the entropy of the distribution, handling exp/log numerical stability. Then make it streaming: logits arrive one at a time, accumulate entropy online.

---

## 15. Chat Bot System Refactoring

- **category**: coding
- **frequency**: Medium
- **last asked**: 2026-07-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: refactoring, oop-design, design-implementation, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/chat-bot-system-refactoring
- **related discussions**: 4

### Statement

You are given a legacy chat service codebase that supports various bots (AwayBot, MeetBot, TacoBot) triggered by slash commands. The task is to refactor the monolithic, global-state implementation into a clean object-oriented design using interfaces and — as a follow-up — an event-driven architecture with a central event bus.

---

## 16. IP Address / CIDR Iterator (5-part)

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-07-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: algorithm, string, bit-manipulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/ip-address-cidr-iterator
- **related discussions**: 6

### Statement

Given IPv4 / CIDR notation, implement parsing, range iteration, containment checks, etc. 5 parts revealed one at a time; the next part unlocks only when the previous one passes its tests.

---

## 17. Shard Rebalance / Overlapping Key Range

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-06-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: algorithm, interval, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/shard-rebalance
- **related discussions**: 4

### Statement

Given a set of shards (each with id/start/end key range), support `add_shard / remove_shard` and implement `rebalance()` so that no more than `limit` shards overlap at any point. Trim excess, fill gaps, minimize data movement.

---

## 18. Math Reasoning: Stopping Time / Las Vegas Algorithm

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-31
- **stage**: tech-screen
- **access**: Members
- **roles**: RS, RE
- **tags**: ml-knowledge, math-reasoning, probability, algorithm-design, hard
- **url**: https://www.1point3acres.com/interview/problems/company/openai/math-reasoning-stopping-time
- **related discussions**: 4

### Statement

An oracle LLM does math reasoning; the 'solving time' follows a known distribution. Design a restart strategy to compress the expected solving time. Final part: find a strategy that beats single-threshold restart on worst-case performance.

---

## 19. Version Dependency

- **category**: coding
- **duration**: 75 min
- **frequency**: Medium
- **last asked**: 2026-05-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, RE
- **tags**: algorithm, graph, dependency-resolution, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/version-dependency
- **related discussions**: 5

### Statement

Given packages with version dependencies ('package A v1 requires B >= 2.0'), implement parsing, queries, and likely a SAT-like resolver. 4 sub-parts; very large code volume.

---

## 20. Fault-Tolerant Work Queue

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: Infra Eng
- **tags**: queue, state-machine, retry, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/fault-tolerant-work-queue
- **related discussions**: 1

### Statement

Implement a work queue with `reserve`, `complete`, and `fail` operations, then extend it with reservation timeouts, retries, and a dead-letter queue. Timeout behavior and retry limits are intentionally underspecified, so clarify them before coding.

---

## 21. Cross-Entropy Loss Implementation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-10
- **stage**: tech-screen
- **access**: Members
- **roles**: MLE, RE
- **tags**: ml-knowledge, numpy, numerical-stability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/masked-cross-entropy-loss
- **related discussions**: 1

### Statement

Implement cross-entropy loss from NumPy starter code, then extend it with masking and label smoothing while discussing the underlying ML concepts and the relationship to KL divergence.

---

## 22. Maximum-Score Grid Path with Limited Jumps

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, grid, combinatorics, hard
- **url**: https://www.1point3acres.com/interview/problems/company/openai/maximum-score-grid-path-limited-jumps
- **related discussions**: 1

### Statement

Given an integer grid, a top-row starting column, and a budget of at most K two-row jumps, maximize the score on a path to the bottom; then reconstruct an optimal path, count all optimal paths modulo 1e9+7, and incorporate adjacency-based bonuses.

---

## 23. Implement a CD Command

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string-processing, stack, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/implement-cd-command
- **related discussions**: 1

### Statement

Implement a cd() method that behaves like running cd <destination> in a terminal and then calling pwd. The problem builds from relative-path normalization (handling .., ., repeated slashes, trailing slashes) through absolute paths and ~ expansion, to full symbolic-link resolution with cycle detection. A final follow-up explores why cd must be a shell built-in and how Linux resolves paths via inodes.

---

## 24. OpenSheet: Spreadsheet with Cell Dependencies

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: graph, dfs, topological-sort, recursion, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/opensheet-spreadsheet
- **related discussions**: 1

### Statement

Design and implement a spreadsheet system called OpenSheet that supports cell values, formulas, and automatic dependency resolution. The system must handle cell names (e.g. A1, B2), numeric literals, and arithmetic formulas referencing other cells, while detecting and rejecting circular dependencies. A follow-up asks for an optimized O(1) getCell via eager recomputation using a dependency graph and topological sort.

---

## 25. ModalLock and FairModalLock

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: concurrency, threading, python, hard
- **url**: https://www.1point3acres.com/interview/problems/company/openai/modal-lock-fair-modal-lock
- **related discussions**: 2

### Statement

A Python concurrency problem for ML Infra roles. Candidates implement two synchronization primitives — ModalLock (mode-aware shared lock allowing concurrent same-mode holders while blocking cross-mode) and FairModalLock (adds FIFO fairness across mode groups) — by filling in four methods in a provided file and passing five progressively unlocked test stages.

---

## 26. Sharded Matmul and Backprop Debugging

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-07-31
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, RE
- **tags**: autograd, linear-algebra, numpy, pytorch, parallelism, debugging, hard
- **url**: https://www.1point3acres.com/interview/problems/company/openai/sharded-matmul-backprop-debugging
- **related discussions**: 2

### Statement

Derive a simplified sharded matrix multiplication, implement its forward and backward behavior across devices with NumPy or PyTorch, and debug a fixed set of planted defects.

---

## 27. Resumable Iterator with Multi-Dimensional Support

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-06-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: iterator, oop-design, recursion, python, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/resumable-iterator
- **related discussions**: 3

### Statement

You need to implement a resumable iterator system that supports pause and resume functionality through state management. The problem progresses in four steps: defining an abstract base class, implementing a 1D list iterator, extending to a 2D matrix iterator, and finally a 3D nested iterator — each with full get_state / set_state serialization semantics.

---

## 28. Code Reading — 400-line PyTorch Refactor

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: RE, MLE
- **tags**: code-reading, pytorch
- **url**: https://www.1point3acres.com/interview/problems/company/openai/code-reading-pytorch-refactor
- **related discussions**: 2

### Statement

A 400+ line PyTorch file is provided; complete 3 parts + 1 bonus. Lots of time/space complexity questions on the existing code. The codebase being modified is the noisy-annotator classifier seen elsewhere in the ML coding rounds.

---

## 29. In-Memory Database with SQL Operations

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-11-07
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: in-memory-database, sql, data-structure, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/in-memory-database-sql
- **related discussions**: 3

### Statement

Design and implement an in-memory database management system that supports basic SQL-like operations. The problem is structured in up to six progressive parts covering table creation, row insertion, column projection, WHERE filtering, ORDER BY sorting, and an optimization discussion around indexing strategies.

---

## 30. Time-Based Key-Value Store with Production Testing

- **category**: coding
- **frequency**: Low
- **last asked**: 2025-10-19
- **stage**: oa · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, binary-search, data-structure, concurrency, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/time-based-kv-store

### Statement

Design and implement a time-based key-value data structure that stores multiple values for the same key at different timestamps and retrieves values based on temporal queries. The problem extends beyond basic implementation to cover test strategy with mock clocks, monotonic timestamp enforcement, and thread-safe concurrency approaches.

---

## 31. Payment / Coffee-Shop Ordering (read the prompt!)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-10
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/payment-coffee-shop
- **related discussions**: 31

### Statement

'Payment' is the title, but the actual prompt is often a specific vertical — coffee-shop ordering, e-commerce checkout, etc. Read before you design; don't apply a generic payment template.

---

## 32. Design Chess.com (Online Chess Game)

- **category**: system-design
- **frequency**: Very high
- **last asked**: 2026-09-09
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: websocket, redis, schema-design, distributed-systems, idempotency, scaling, concurrency, game-engine
- **url**: https://www.1point3acres.com/interview/problems/company/openai/design-chess-game
- **related discussions**: 19

### Statement

Design an online chess platform similar to Chess.com supporting real-time 1v1 games, matchmaking, and server-authoritative clock management. The core challenges are pairing players quickly by rating, propagating moves under 150ms via WebSocket, and maintaining sub-100ms clock accuracy without per-second database writes.

---

## 33. Design Sora / Video Generation Pipeline

- **category**: system-design
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-08-20
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng, EM
- **tags**: gpu, scheduling, messaging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/design-sora-video-generation
- **related discussions**: 28

### Statement

A client sends a video-generation request; design the full scheduling + worker flow with limited / fluctuating GPU pool. Focus on failure-scenario handling.

---

## 34. Design a Cloud IDE

- **category**: system-design
- **frequency**: Medium
- **last asked**: 2026-09-01
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng, EM
- **tags**: sandbox, vm-management, websocket, kafka, streaming, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/openai/design-cloud-ide
- **related discussions**: 6

### Statement

Design a cloud-based IDE similar to Replit or GitHub Codespaces, where users can write code, manage files, and run terminal commands entirely in the browser. The core challenges are VM lifecycle management, real-time terminal output streaming, and strong per-user isolation at scale. A current alternate rotation is host-centric rather than browser-based: the user connects over SSH, while a task scheduler manages work across the host fleet.

---

## 35. Real-Time AI Product Feature

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-08
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: fullstack, streaming, api-design, rate-limiting, sse, websocket
- **url**: https://www.1point3acres.com/interview/problems/company/openai/real-time-ai-product-feature
- **related discussions**: 1

### Statement

Design a user-facing AI product feature that serves a large audience with real-time or streaming responses. Cover streaming API choices, request admission and queueing, cost controls, frontend error handling, observability, and how the design changes at 10× scale.

---

## 36. Mining Novel Data from Large Unlabeled Corpus

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-26
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, data-mining, retrieval
- **url**: https://www.1point3acres.com/interview/problems/company/openai/mining-novel-data-unlabeled-corpus
- **related discussions**: 1

### Statement

An ML system-design prompt for ML engineers: mine new, unique information out of a very large unlabeled corpus, and locate the images that contain specific objects of interest. Only the core task is known; the detailed spec was not pinned down.

---

## 37. ChatGPT Enterprise (RAG ML System Design)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RE
- **tags**: rag, retrieval, ml-knowledge
- **url**: https://www.1point3acres.com/interview/problems/company/openai/chatgpt-enterprise-rag
- **related discussions**: 1

### Statement

Design ChatGPT Enterprise: enterprise users upload internal data; the system gives them a customized ChatGPT based on that data.

---

## 38. GPT-3 Playground - Full-Stack Architecture

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-10-18
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: fullstack, frontend, backend, api-integration, streaming, react, schema-design
- **url**: https://www.1point3acres.com/interview/problems/company/openai/gpt-3-playground
- **related discussions**: 1

### Statement

Design the high-level architecture for a GPT-3 Playground website that lets users experiment with GPT-3. The prompt spans both backend data organization and frontend state management, covering how to accept user prompts, stream model responses, manage parameters, and persist saved presets.

---

## 39. Image Sharing with Content Deduplication

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-08-22
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: storage, deduplication, bandwidth, medium
- **url**: https://www.1point3acres.com/interview/problems/company/openai/image-sharing-content-deduplication
- **related discussions**: 2

### Statement

Design an image storage and sharing service with upload, view/download, and delete operations. Identical content should be stored once using a SHA-256 identity, with careful handling of concurrent uploads, metadata/blob consistency, failure recovery, and deletion lifecycle.

---

## 40. Design a Crossword Puzzle Solver

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-08-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: distributed-systems, dfs, job-system, scheduling, backtracking, messaging, redis, concurrency
- **url**: https://www.1point3acres.com/interview/problems/company/openai/crossword-puzzle-solver
- **related discussions**: 3

### Statement

Design a distributed service that solves crossword puzzles given a board (~50×50, ~100 slots) and a dictionary of ~1 million words. The central insight is that brute-force on a single machine is computationally infeasible (≈10^300 combinations), so the design must distribute a DFS search across a worker pool with task splitting, constraint propagation, and coordinated early termination.

---

## 41. Design an AI Chatbot System

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-07-21
- **stage**: onsite-system-design · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: frontend, fullstack, chat, streaming, sse, websocket, api-integration, access-control
- **url**: https://www.1point3acres.com/interview/problems/company/openai/design-ai-chatbot-system
- **related discussions**: 2

### Statement

Design a simple AI chatbot system similar to ChatGPT's main interface. The prompt emphasizes frontend architecture and full-stack concepts, with no server-side persistence — all chat state lives in the browser. Key design decisions include streaming response delivery, client-side session management, and authentication flow.

---

## 42. Multi-Tenant CI/CD Workflow System

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-06-11
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: ci-cd, distributed-systems, idempotency, scheduling, docker, websocket, scaling, concurrency
- **url**: https://www.1point3acres.com/interview/problems/company/openai/multi-tenant-ci-cd-workflow
- **related discussions**: 3

### Statement

Design a scalable, fault-tolerant CI/CD system for a multi-tenant environment that schedules and executes user-defined workflows in response to git pushes. The system must run each job exactly once (even across crashes), display real-time status to users, and keep tenants isolated from one another.

---

## 43. Slack

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-05
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, EM
- **tags**: messaging
- **url**: https://www.1point3acres.com/interview/problems/company/openai/design-slack
- **related discussions**: 3

### Statement

Design Slack: DM + channels, multi-device delivery, notifications, scale (large channel / sharding).

---

## 44. RAG / Search ML Design (oral)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: RE, MLE
- **tags**: ml-knowledge, retrieval
- **url**: https://www.1point3acres.com/interview/problems/company/openai/rag-search-ml-design
- **related discussions**: 2

### Statement

An oral 'ML Design' round; interviewer often specializes in search and drills deep — embedding training (contrastive learning), retrieval flow, hybrid retrieval, ranking.

---

## 45. Design Google Calendar

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-01-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: schema-design, caching, data-modeling, scaling
- **url**: https://www.1point3acres.com/interview/problems/company/openai/design-google-calendar

### Statement

Design Google Calendar: users create events and view their schedule across day / week / month / year ranges. The core challenges are fast retrieval for the different calendar views and near-real-time propagation of updates across devices.

---

## 46. Design a URL Shortener

- **category**: system-design
- **frequency**: Low
- **last asked**: 2026-01-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: hashing, caching, scaling, database
- **url**: https://www.1point3acres.com/interview/problems/company/openai/design-url-shortener

### Statement

Design a URL shortener that converts a long web address into a short alias and redirects clients to the original page (Bit.ly / TinyURL style). Surfaces key generation / hashing, the redirect path, storage, caching, and scale.

---

## 47. Webhook Delivery System

- **category**: system-design
- **frequency**: Low
- **last asked**: 2025-11-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: messaging, retry, caching, scaling, schema-design, idempotency, http, backend, distributed-systems, queueing, webhook
- **url**: https://www.1point3acres.com/interview/problems/company/openai/webhook-delivery-system

### Statement

Design a highly scalable webhook delivery system that allows users to register callback URLs for specific events. The system must handle 1 billion events per day with reliable at-least-once delivery, including retry logic, caching, and failure isolation.

---

## 48. Points of Interest (POI) System / Yelp

- **category**: system-design
- **frequency**: Low
- **last asked**: 2025-10-07
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: location, geohash, scaling, sharding, caching, schema-design, database
- **url**: https://www.1point3acres.com/interview/problems/company/openai/points-of-interest-yelp

### Statement

Design a scalable location-based service that allows users to discover nearby points of interest (POIs) such as restaurants, shops, and businesses. The system must handle 500 million locations worldwide, 100,000 queries per second (read-heavy), and return results in under 100 milliseconds. Geospatial indexing using Geohash or QuadTree is central to the solution.

---

## 49. HM BQ — Why OpenAI / AI Safety / AGI

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-08-28
- **stage**: onsite-bq
- **access**: Members
- **roles**: All
- **tags**: behavioral, why-company, culture-fit
- **url**: https://www.1point3acres.com/interview/problems/company/openai/hm-bq-why-openai
- **related discussions**: 23

### Statement

HM BQ round with high-frequency questions about why OpenAI, AGI, AI safety, hypothetical safety dilemmas, failure / conflict / pushback stories, reasons for past job changes.

---

## 50. Recruiter / HR Screen (BQ)

- **category**: behavioral
- **duration**: 30 min
- **frequency**: High
- **last asked**: 2026-08-08
- **stage**: recruiter-screen
- **access**: Members
- **roles**: All
- **tags**: behavioral
- **url**: https://www.1point3acres.com/interview/problems/company/openai/recruiter-hr-screen-bq
- **related discussions**: 9

### Statement

HR runs standard BQ + process intro (timeline, salary expectations, location). Mandatory for every loop.

---

## 51. Engineering Management Fundamentals & Team Operations

- **category**: behavioral
- **frequency**: Single report
- **last asked**: 2026-08-07
- **stage**: phone-screen
- **access**: Members
- **roles**: EM
- **tags**: leadership, mentorship, ownership
- **url**: https://www.1point3acres.com/interview/problems/company/openai/engineering-management-fundamentals
- **related discussions**: 1

### Statement

A structured management round spanning leadership philosophy, one-on-ones, hands-on coding balance, hiring, mentorship, performance management, and team composition. The interviewer works through a broad prompt bank and asks for concrete examples.

---

## 52. Technical Deep Dive (slide-based)

- **category**: other
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-08-20
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: SWE, MLE, RE, Infra Eng
- **tags**: deep-dive, presentation, project-deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/openai/technical-deep-dive
- **related discussions**: 25

### Statement

Prepare slides about a past project; interviewer drills the technical detail. Research-org loop always has it; bar is high.

---
