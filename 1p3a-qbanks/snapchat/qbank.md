# Snapchat — 1point3acres Question Bank (23 curated questions)

Source: 1point3acres qbank (`company=snapchat`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/snapchat>

The site lists 100 entries for this company: **23 curated questions** (below), 77 OJ links, 0 external links. Only the curated 23 are captured here.

> Snap interviews are highly interviewer-dependent: candidates report classic graph and cache problems, open-ended backend coding, product-heavy system design, and MLE loops that dig into recommendation systems, ranking, fundamentals, and project judgment. The safest preparation strategy is to be fluent in BFS/DFS, cache/data-structure implementation, feed and ads design, and concise behavioral stories around innovation, ambiguity, and execution.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. LRU Cache with TTL or Weighted Size
2. Max Area of Island (LC 695)
3. Event Count Query over Timestamp Range
4. Stream Window Max Unique Users
5. Valid Set Cards
6. In-Memory Pub/Sub
7. Word Ladder Reachability
8. Custom List, Iterator & Map Function
9. Rate Limiter
10. Search Suggestion Service
11. Service Dependency Shortest Latency Path
12. Split Array Largest Sum (LC 410)
13. Most Stones Removed with Same Row or Column (LC 947)
14. Smallest Range Covering Elements from K Lists (LC 632)
15. Course Schedule Cycle Detection
16. Maximum Island Perimeter
17. Escape Grid with Fire and Waiting Time
18. Metrics Collection, Alerting & Click Counters
19. Short Video Recommendation & Ranking
20. Snap Story or Spotlight Feed Backend
21. Snap Ads Ranking
22. ML Fundamentals, Transformer & Regularization
23. iOS Scrollable Grid & Swift Concurrency

---

## 1. LRU Cache with TTL or Weighted Size

- **category**: coding
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-09-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: data-structure, ttl, linked-list, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/lru-cache-with-ttl-or-weighted-size
- **related discussions**: 6

### Statement

Implement an LRU-style cache, then handle production follow-ups such as TTL expiration, per-item size, or evicting multiple entries to stay within capacity.

---

## 2. Max Area of Island (LC 695)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-06
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: graph, grid, bfs, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/max-area-of-island-lc-695
- **related discussions**: 1

### Statement

Solve LC 695 (Max Area of Island). The first follow-up changes the return value to an integer array containing every island's size; the next asks how to return the maximum size.

---

## 3. Event Count Query over Timestamp Range

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-28
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: binary-search, prefix-sum, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/event-count-query-over-timestamp-range
- **related discussions**: 1

### Statement

Preprocess unordered and duplicate event timestamps, then answer count queries for arbitrary start and end timestamps.

---

## 4. Stream Window Max Unique Users

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: sliding-window, streaming, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/stream-window-max-unique-users
- **related discussions**: 1

### Statement

Given timestamped user events and a window size, find the maximum number of unique users appearing in any time window.

---

## 5. Valid Set Cards

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: array, set, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/valid-set-cards
- **related discussions**: 1

### Statement

Given cards with feature values in {1,2,3}, determine whether a selected group forms a valid set: for every feature, values are either all same or all different.

---

## 6. In-Memory Pub/Sub

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-18
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: ood, concurrency, pubsub, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/in-memory-pubsub
- **related discussions**: 1

### Statement

Implement an in-memory pub/sub service with topic creation, publishing, subscribing, and follow-up discussion around push notification and concurrency.

---

## 7. Word Ladder Reachability

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-12
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: graph, bfs, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/word-ladder-reachability
- **related discussions**: 1

### Statement

Given a start word, target word, and dictionary, return whether the target can be reached by changing one letter at a time, rather than returning the shortest path length.

---

## 8. Custom List, Iterator & Map Function

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-26
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: ood, iterator, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/custom-list-iterator-and-map-function
- **related discussions**: 1

### Statement

Implement a list without built-in collections, then add an iterator and a map function on top of the custom data structure.

---

## 9. Rate Limiter

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: rate-limiting, sliding-window, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/rate-limiter
- **related discussions**: 1

### Statement

Implement a rate limiter, commonly sliding window, then discuss race conditions, concurrent maps, synchronization, and lock efficiency.

---

## 10. Search Suggestion Service

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: trie, top-k, heap, string, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/search-suggestion-service
- **related discussions**: 1

### Statement

Design and implement a prefix search suggestion service, typically with a trie and top-K ranking by frequency and lexicographic tie-break.

---

## 11. Service Dependency Shortest Latency Path

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-30
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: graph, dijkstra, heap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/service-dependency-shortest-latency-path
- **related discussions**: 1

### Statement

Given services and pairwise service latencies, return the lowest-latency path from a start service to a destination service.

---

## 12. Split Array Largest Sum (LC 410)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: binary-search, greedy, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/split-array-largest-sum
- **related discussions**: 1

### Statement

Solve the public base problem identified as LC 410 in an L5 onsite coding round. Interview-specific modifications are unspecified.

---

## 13. Most Stones Removed with Same Row or Column (LC 947)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: graph, union-find, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/most-stones-removed-with-same-row-or-column
- **related discussions**: 1

### Statement

Solve the public base problem identified as LC 947. No follow-up was specified.

---

## 14. Smallest Range Covering Elements from K Lists (LC 632)

- **category**: coding
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **roles**: mle
- **tags**: heap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/smallest-range-covering-elements-from-k-lists
- **related discussions**: 1

### Statement

Solve the public base problem identified as LC 632 during a 30-minute MLE coding segment. Interview-specific modifications are unspecified.

---

## 15. Course Schedule Cycle Detection

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-13
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, mle
- **tags**: graph, dfs, topological-sort, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/course-schedule-cycle-detection
- **related discussions**: 3

### Statement

Given courses and prerequisite pairs, determine whether all courses can be completed by detecting cycles in a directed graph.

---

## 16. Maximum Island Perimeter

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-17
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: graph, grid, bfs, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/maximum-island-perimeter
- **related discussions**: 3

### Statement

Given a binary grid, find islands with BFS or DFS and return the largest island perimeter rather than just the island count.

---

## 17. Escape Grid with Fire and Waiting Time

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-02-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: grid, bfs, binary-search, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/escape-grid-with-fire-and-waiting-time
- **related discussions**: 3

### Statement

Given a grid with a start, target, blockers, and sometimes fire sources, decide whether escape is possible or compute the maximum safe waiting time before moving.

---

## 18. Metrics Collection, Alerting & Click Counters

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: system-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/metrics-collection-alerting-and-click-counters
- **related discussions**: 4

### Statement

Design a real-time metrics, click-counting, or alerting pipeline that supports low-latency queries, offline analysis, deduplication, and reliable counters.

---

## 19. Short Video Recommendation & Ranking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-21
- **stage**: phone-screen · onsite-system-design · onsite-deep-dive
- **access**: Members
- **roles**: mle
- **tags**: recommendation, ranking, retrieval, two-tower, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/short-video-recommendation-and-ranking
- **related discussions**: 6

### Statement

Design a short-video recommendation or ranking system for Spotlight / Lens-style products, covering retrieval, ranking, short-term interest, cold start, delayed labels, and multimodal features.

---

## 20. Snap Story or Spotlight Feed Backend

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe, mle
- **tags**: system-design, feed, cdn, fan-out, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/snap-story-or-spotlight-feed-backend
- **related discussions**: 4

### Statement

Design the backend for Snapchat Stories or Spotlight video feed vending, including upload, privacy, offline support, feed pagination, freshness, storage, and serving trade-offs.

---

## 21. Snap Ads Ranking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-21
- **stage**: onsite-system-design · onsite-deep-dive
- **access**: Members
- **roles**: mle
- **tags**: ads, ranking, feature-engineering, calibration, mlsd, hard
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/snap-ads-ranking
- **related discussions**: 3

### Statement

Design an ads ranking system with feature construction, multi-task objectives, delayed labels, calibration, and business constraints.

---

## 22. ML Fundamentals, Transformer & Regularization

- **category**: other
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-09-03
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: mle
- **tags**: ml-knowledge, transformer, optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/ml-fundamentals-transformer-and-regularization
- **related discussions**: 6

### Statement

Snap MLE screens repeatedly include rapid-fire ML fundamentals: overfitting, bias-variance, normalization, optimizers, loss functions, Transformer attention, positional encoding, and project deep-dive...

---

## 23. iOS Scrollable Grid & Swift Concurrency

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-20
- **stage**: phone-screen · onsite-coding · onsite-deep-dive
- **access**: Members
- **roles**: swe
- **tags**: concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/snapchat/ios-scrollable-grid-and-swift-concurrency
- **related discussions**: 1

### Statement

Build an iOS scrollable square grid, then answer Swift and GCD questions about value types, copy-on-write, memory, weak references, and dispatch ordering.

---
