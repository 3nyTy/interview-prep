# Perplexity — 1point3acres Question Bank (16 curated questions)

Source: 1point3acres qbank (`company=perplexity`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/perplexity>

The site lists 54 entries for this company: **16 curated questions** (below), 23 OJ links, 15 external links. Only the curated 16 are captured here.

> Perplexity interviews lean toward long, product-flavored engineering simulations rather than generic LeetCode. Candidates should expect Python-first coding, incremental multi-part specs, careful edge-case preservation across parts, and system-design conversations tied closely to search, discovery, retrieval, inference, and operational reliability.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. In-Memory File System
2. ToDo List for AI
3. Stop Sequences Before First Match
4. Frontend Discover Infinite Scroll
5. KV Store with Timestamps and Restore
6. scikit-learn Binary Classifier
7. Stream Sampling & Distribution Verification
8. Stream Deduplication with Near-Duplicate Detection
9. Premium-Zone Escape Path
10. Beam Search Decoder
11. Credit Tracker with Expiring Credits
12. Byte Tokenizer and Token-Count Estimator
13. Embedding Model Batching Service
14. Durable Long-Running Agentic Query Execution
15. Behavioral and Project Deep-Dive
16. Map-Reduce and Blockwise Attention

---

## 1. In-Memory File System

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-10
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: oop-design, tree, trie, filesystem, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/in-memory-file-system
- **related discussions**: 7

### Statement

Build an in-memory Unix-style file system backed by a tree of directory and file nodes, implemented incrementally across parts. Supports creating, listing, reading/writing, and removing files and dire...

---

## 2. ToDo List for AI

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-26
- **stage**: phone-screen
- **access**: Free preview
- **roles**: SWE
- **tags**: oop-design, graph, dfs, state-machine, hard
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/todo-list-for-ai
- **related discussions**: 4

### Statement

Implement a multi-part ToDoList with TaskStatus, Task, and ToDoList classes. Later parts add task dependencies, blocked / ready transitions, and cascade failure propagation over parent-child dependency links.

---

## 3. Stop Sequences Before First Match

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2025-10-12
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: string-processing, streaming, iterator, trie, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/stop-sequences-before-first-match
- **related discussions**: 4

### Statement

Given text and a list of stop words or stop sequences, return only the content before the first stop sequence. Follow-ups turn the input into a stream of chunks that cannot all be held in memory.

---

## 4. Frontend Discover Infinite Scroll

- **category**: coding
- **duration**: 120 min
- **frequency**: Single report
- **last asked**: 2026-04-12
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, api-integration, feed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/frontend-discover-infinite-scroll
- **related discussions**: 1

### Statement

Build a TypeScript frontend similar to Perplexity Discover using provided dummy data and fetch helpers. The core work is pagination, infinite scroll, component structure, and CSS layout.

---

## 5. KV Store with Timestamps and Restore

- **category**: coding
- **duration**: 240 min
- **frequency**: Single report
- **last asked**: 2026-02-23
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: in-memory-database, mvcc, snapshot, binary-search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/kv-store-with-timestamps-and-restore
- **related discussions**: 1

### Statement

Implement a key-value store where set, get, and delete operations are timestamp-aware. A later part adds restore behavior that must find the correct historical timestamp state.

---

## 6. scikit-learn Binary Classifier

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-22
- **stage**: phone-screen
- **access**: Members
- **roles**: mle
- **tags**: scikit-learn, classification, ml-experiment, optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/sklearn-binary-classifier
- **related discussions**: 1

### Statement

For an AI Engineer phone screen, train a binary classifier on a provided toy dataset using Python and scikit-learn, then improve the initial evaluation metric through model choice or tuning.

---

## 7. Stream Sampling & Distribution Verification

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-19
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, mle, ds
- **tags**: streaming, sampling, probability, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/stream-sampling-and-distribution-verification

### Statement

Implement reservoir sampling over an unknown-size stream under O(k) memory and one-pass constraints, then write a simulation that verifies the selected items are close to uniformly distributed. The fo...

---

## 8. Stream Deduplication with Near-Duplicate Detection

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-08
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: streaming, dedup, set, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/stream-deduplication-with-near-duplicate-detection

### Statement

Implement a streaming deduplicator that preserves the first occurrence of each string, then extend it to suppress near-duplicates using a similarity threshold. The core trade-off is exact O(1) set mem...

---

## 9. Premium-Zone Escape Path

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: bfs, grid, location, geohash, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/premium-zone-escape-path
- **related discussions**: 1

### Statement

Given a longitude / latitude coordinate and an API or predicate for whether the coordinate is in a premium rental-car zone, return a path out of the premium area or one coordinate outside it.

---

## 10. Beam Search Decoder

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-30
- **stage**: phone-screen
- **access**: Members
- **roles**: mle, swe
- **tags**: beam-search, top-k, heap, llm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/beam-search-decoder
- **related discussions**: 1

### Statement

Complete a beam-search implementation for sequence generation using a provided `next_token_fn`. The function returns the best generated token sequences under beam size, max-token, and stop-token const...

---

## 11. Credit Tracker with Expiring Credits

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-08-09
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: interval, heap, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/credit-tracker-with-expiring-credits
- **related discussions**: 1

### Statement

Design a `CreditTracker` class that adds credits over time ranges, subtracts credits at a point in time using earliest-expiration-first order, and checks available credit at a timestamp.

---

## 12. Byte Tokenizer and Token-Count Estimator

- **category**: coding
- **duration**: 240 min
- **frequency**: Low
- **last asked**: 2026-01-06
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: code-reading, string-processing, heap, linked-list, optimization, hard
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/byte-tokenizer-and-token-count-estimator
- **related discussions**: 3

### Statement

Study a slow reference byte tokenizer, describe it precisely, then implement a faster `tokenize` method with identical output. The final part estimates token counts under a sampling budget without rei...

---

## 13. Embedding Model Batching Service

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-10-09
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, mle
- **tags**: inference, batching, concurrency, threading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/embedding-model-batching-service
- **related discussions**: 2

### Statement

Wrap a provided embedding model in a service that supports batched requests, shutdown, and concurrent processing. One variant adds max batch and max-token limits for batches of sequences.

---

## 14. Durable Long-Running Agentic Query Execution

- **category**: system-design
- **frequency**: Low · New
- **last asked**: 2026-09-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: agents, agentic-workflow, distributed-systems, storage, streaming, hard
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/durable-long-running-agentic-query-execution
- **related discussions**: 2

### Statement

Redesign an in-memory ask service so agentic queries that may run for hours execute durably, resume after interruptions, finish after the browser or network disconnects, and stream progress to the cli...

---

## 15. Behavioral and Project Deep-Dive

- **category**: behavioral
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2025-10-03
- **stage**: recruiter-screen · onsite-bq · onsite-deep-dive
- **access**: Members
- **roles**: all-roles
- **tags**: behavioral, project-deep-dive, why-company, collaboration, medium
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/behavioral-and-project-deep-dive
- **related discussions**: 4

### Statement

Perplexity screens and HM rounds ask standard but selective fit questions: Why Perplexity, proudest or hardest project, technical stack, cross-team collaboration, unpopular decisions, and requirement ...

---

## 16. Map-Reduce and Blockwise Attention

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-07-03
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, infra-eng
- **tags**: map-reduce, attention, parallelism, numerical-stability, hard
- **url**: https://www.1point3acres.com/interview/problems/company/perplexity/map-reduce-and-blockwise-attention
- **related discussions**: 1

### Statement

A technical screening for deep-learning roles covered map-reduce style vector operations and context parallelization for attention, including segmented Q/K/V, blockwise attention, and robust softmax.

---
