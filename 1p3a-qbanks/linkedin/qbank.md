# LinkedIn — 1point3acres Question Bank (37 curated questions)

Source: 1point3acres qbank (`company=linkedin`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/linkedin>

The site lists 132 entries for this company: **37 curated questions** (below), 95 OJ links, 0 external links. Only the curated 37 are captured here.

> LinkedIn's loop pivoted heavily in 2025 — Staff candidates now sit a packed two-round phone screen (coding + project retro) before a five-round onsite that includes an AI-Coding session in CoderPad's assistant pane. Behavioural depth is the dominant downlevel axis; the same coding bar that passes at Senior frequently caps a Staff loop with a downlevel offer.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Merge N-ary Trees by Node Key
2. Weighted Sampling from a Probability Distribution
3. Phone Keypad — Letter Combinations / Word Filter
4. Minimum Sum-of-Distances Meeting Point on a Line
5. LRU Cache + Thread-Safe Follow-up
6. Recommender Training Pipeline
7. Nested List Weight Sum II (LC 364)
8. Logistic Regression Debugging
9. Robot Navigation in an Unknown Grid
10. Count Distinct Values in a Massive Sorted Array
11. Find K Closest Elements in a Sorted Array (LC 658)
12. Pow(x, n) (LC 50)
13. 2-D Matrix Range Sum with Updates (OA SumTable)
14. Stratified Sampling from Class-Bucketed JSON
15. Cache with Rank-Based Eviction Policy
16. Service Dependency Impact Propagation
17. Sparse Vector / Matrix Product
18. Count Subarrays Where First = Last = Max
19. Ranked-Choice Voting (Instant Runoff)
20. Largest Number (LC 179)
21. LFU Cache
22. Max Stack
23. Biased Coin to Uniform Range
24. Meeting Scheduler — Earliest Available Slot
25. Alert Monitor — Rolling Window + Severity Distribution + Spike Detection
26. Function Inclusive / Exclusive Time
27. Distributed Job Scheduler / Calendar Service
28. AI-Powered Personalized Recruiter Message Generation
29. Metrics & Monitoring Platform
30. News Feed / Timeline
31. Real-Time Collaborative Code Editor
32. Type-Ahead / Autocomplete Suggestions
33. LinkedIn Learning / Job Recommendation Ranking
34. LinkedIn Skills — Data Mining & ML System Design
35. Single-Machine Key-Value Store with Filesystem Spill
36. Project Deep-Dive (Four-Axis Rubric)
37. Behavioral / BQ Question Pack (Staff & Senior)

---

## 1. Merge N-ary Trees by Node Key

- **category**: coding
- **frequency**: Medium
- **last asked**: 2026-08-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: tree, dfs, recursion, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-merge-nary-trees-by-key
- **related discussions**: 4

### Statement

Merge two N-ary trees whose nodes carry stable keys, combining nodes only when their keys match. The prompt is an N-ary transformation of the familiar binary-tree merge family, with conflict-policy and concurrent-update follow-ups.

---

## 2. Weighted Sampling from a Probability Distribution

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-25
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, SWE, RS
- **tags**: probability, sampling, binary-search, prefix-sum, numerical-stability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-weighted-sampling-die
- **related discussions**: 6

### Statement

Given a probability distribution over `N` categories (e.g. an M-faced biased die or softmax weights), draw a sample in `O(log N)`. The canonical solution is inverse-CDF sampling — build a cumulative prefix and binary-search a uniform `[0, 1)` draw. Follow-ups dig into probabilities that don't sum to 1, very large or peaked distributions, and rejection-sampling expected cost.

---

## 3. Phone Keypad — Letter Combinations / Word Filter

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-03-02
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: backtracking, string, hashmap, trie, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-phone-letter-combinations
- **related discussions**: 5

### Statement

The classic phone-keypad letter-combinations problem shows up nearly every recent loop, often as the screen warm-up. A common variant: given a digit string and a dictionary of words, return which words can be typed on the keypad — the structural twist that turns it into a hash / trie problem rather than pure backtracking.

---

## 4. Minimum Sum-of-Distances Meeting Point on a Line

- **category**: coding
- **duration**: 55 min
- **frequency**: Medium
- **last asked**: 2026-02-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE, SWE, RS
- **tags**: math, quickselect, sorting, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-min-sum-distance-line
- **related discussions**: 4

### Statement

Given a 1-D array of points, find the point that minimizes the sum of absolute distances to every other point. The answer is the median; the interview signal is the proof, not the code. Follow-ups extend to L2 distance with k chosen positions (much harder) and to streaming median maintenance.

---

## 5. LRU Cache + Thread-Safe Follow-up

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-01-22
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: data-structure, hashmap, linked-list, concurrency, threading, ai-tools, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-lru-cache-multithread
- **related discussions**: 5

### Statement

Implement an LRU cache, then extend to a thread-safe version. In post-reform loops this is often delivered as the AI-coding round: candidates outline the doubly-linked-list + hashmap design, let the in-IDE assistant produce the skeleton, then dry-run unit tests and reason about lock granularity.

---

## 6. Recommender Training Pipeline

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-06
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: recommendation, classification, feature-engineering, json, ambiguous-spec
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-recommender-training-pipeline
- **related discussions**: 1

### Statement

Complete a text-embedding recommender training pipeline using member profiles, item text, and clicked/skipped labels. Implement the RecommenderTrainer data-loading, feature-preparation, and training methods; the interviewer also requested a handwritten MLP.

---

## 7. Nested List Weight Sum II (LC 364)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: recursion
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-nested-list-weight-sum-ii
- **related discussions**: 1

### Statement

Compute the inverse-depth-weighted sum of integers in a nested list. The candidate explicitly identified this as unchanged LeetCode 364.

---

## 8. Logistic Regression Debugging

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-24
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: debugging, classification, gradient-descent, ml-knowledge, hard
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-logistic-regression-debugging
- **related discussions**: 1

### Statement

Debug a logistic-regression implementation with errors in the weight gradient, sigmoid-derivative contract, and binary cross-entropy loss. The interviewer expects the corrected gradient to be derived and simplified to its final matrix form.

---

## 9. Robot Navigation in an Unknown Grid

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-18
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: grid, graph, bfs, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-robot-navigation-unknown-grid
- **related discussions**: 1

### Statement

Control a robot in an initially unexplored matrix using turn, forward-move, wall-detection, and return-to-explored-location operations. Discover traversable cells while finding a path from the starting position to the destination.

---

## 10. Count Distinct Values in a Massive Sorted Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, binary-search, divide-and-conquer, recursion, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-count-distinct-sorted-array
- **related discussions**: 1

### Statement

Count distinct values in a sorted array where `n` may be in the billions but the number of distinct values `k` is only in the hundreds. Skip each equal-value run with boundary searches for O(k log n), and analyze a divide-and-conquer alternative that can degrade to O(n).

---

## 11. Find K Closest Elements in a Sorted Array (LC 658)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, binary-search, two-pointer, heap, top-k, io, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-k-closest-elements-sorted-array
- **related discussions**: 1

### Statement

Given a sorted array, target `x`, and integer `k`, return the `k` closest values in sorted order, preferring the smaller value on equal distance. Implement the binary-search-on-window solution and discuss unsorted-input and out-of-memory variants.

---

## 12. Pow(x, n) (LC 50)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: math, recursion, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-pow-x-n
- **related discussions**: 1

### Statement

Implement `pow(x, n)` for positive and negative integer exponents. The round expects an O(log n) solution in both recursive and iterative form, plus explicit handling of the minimum signed-integer exponent where ordinary negation overflows.

---

## 13. 2-D Matrix Range Sum with Updates (OA SumTable)

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2026-04-08
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: prefix-sum, data-structure, io, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-prefix-sum-2d-matrix-oa
- **related discussions**: 1

### Statement

HackerRank OA for the SWE AI Trainer (LLM rater) role. Five TypeScript multiple-choice questions on code-quality judgement plus one Python implementation: process a stream of `get`/`set` queries against a 2-D matrix returning the rectangle sum for `get` and applying point updates for `set`.

---

## 14. Stratified Sampling from Class-Bucketed JSON

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-03-18
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: sampling, probability, hashmap, data-engineering, easy
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-stratified-sample
- **related discussions**: 1

### Statement

ML-coding sub-round (~20 min slot inside a triple-task phone screen). Input is a JSON object keyed by class label; the values are example arrays. Implement a uniform-over-classes sampler — pick a class uniformly at random first, then sample one example from that class.

---

## 15. Cache with Rank-Based Eviction Policy

- **category**: coding
- **duration**: 55 min
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, heap, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-cache-rank-eviction
- **related discussions**: 1

### Statement

Given an interface where every cached value implements `rank()`, build a cache whose eviction policy drops the lowest-`rank()` entry on overflow. Hashmap plus a min-heap on `rank` is the baseline; the interesting follow-up is a stable last-seen-timestamp tiebreaker via doubly-linked-list bookkeeping per rank bucket.

---

## 16. Service Dependency Impact Propagation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-12-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: graph, dfs, bfs, topological-sort, tree, ai-tools, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-service-dependency-impact
- **related discussions**: 1

### Statement

Given a JSON manifest mapping services to their `read_from` and `write_to` paths, and a list of deleted paths, identify which services are impacted. Three escalating sub-tasks layer a directory-tree projection and a service dependency chain on top of a basic intersection scan. The AI-coding round template — long prompt, IDE-context loading, assistant-driven implementation.

---

## 17. Sparse Vector / Matrix Product

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, RS
- **tags**: data-structure, object-design, math, linear-algebra, memory-bounded, hard
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-sparse-vector-matrix-product
- **related discussions**: 1

### Statement

Design `SparseVector` and `SparseMatrix` classes from scratch, then implement vector-vector and matrix-matrix products without materializing the dense form. The bar is *both* sub-`O(M × N)` time and minimal memory — a list-of-coordinates representation is the expected baseline; CSR/CSC if pushed.

---

## 18. Count Subarrays Where First = Last = Max

- **category**: coding
- **duration**: 55 min
- **frequency**: Single report
- **last asked**: 2025-11-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, stack, hashmap, monotonic-stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-subarray-first-last-equal-max
- **related discussions**: 1

### Statement

Count subarrays where the first element equals the last element equals the maximum value in the subarray. Not a canonical LC problem; the optimal `O(N)` solution uses a monotonic-decreasing stack to find, for each index, the next strictly greater element on the right and group equal-value runs accordingly.

---

## 19. Ranked-Choice Voting (Instant Runoff)

- **category**: coding
- **duration**: 55 min
- **frequency**: Single report
- **last asked**: 2025-11-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: simulation, hashmap, algorithm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-ranked-choice-voting
- **related discussions**: 1

### Statement

Implement instant-runoff voting: each voter submits a preference ranking; in each round, the lowest-vote candidate is eliminated and that candidate's votes redistribute to their next-preferred remaining candidate. Return the candidate that first crosses 50%. Pure simulation with care needed on tie-breaking and ballot iteration pointers.

---

## 20. Largest Number (LC 179)

- **category**: coding
- **frequency**: Single report
- **last asked**: —
- **stage**: phone-screen
- **access**: Members
- **tags**: sorting, string
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-largest-number
- **related discussions**: 1

### Statement

Arrange non-negative integers to form the largest concatenated number. The standard formulation returns a string; any interview-specific modifications remain unspecified.

---

## 21. LFU Cache

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-17
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, hashmap, linked-list, hard
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-lfu-cache
- **related discussions**: 2

### Statement

LeetCode 460 — implement an `O(1)` LFU cache. Two hashmaps (key→node, freq→doubly-linked-list) plus a `min_freq` cursor. Surfaces in both phone screens and onsite coding rounds, often as the candidate's second algorithmic problem in a tight window.

---

## 22. Max Stack

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-03-02
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, hashmap, linked-list, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-max-stack
- **related discussions**: 2

### Statement

LeetCode 716: implement a stack with `push`, `pop`, `top`, `peekMax`, and `popMax`. The `popMax` operation is the discriminator — the optimal answer is a doubly-linked list of nodes plus a TreeMap (or skip list) keyed on value, giving `O(log N)` for `popMax` and `O(1)` for the rest.

---

## 23. Biased Coin to Uniform Range

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2026-02-23
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: probability, sampling, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-biased-coin-uniform
- **related discussions**: 2

### Statement

Given a biased Bernoulli (returns 0 with probability `p`, 1 with probability `1-p`), construct a uniform sampler over `[0, 6]`. The canonical recipe: combine two biased draws to get a fair coin (Von Neumann's trick), then bit-pack three fair coins and reject draws outside `[0, 6]`.

---

## 24. Meeting Scheduler — Earliest Available Slot

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: interval, binary-search, data-structure, concurrency, ai-tools, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-meeting-scheduler-earliest-slot
- **related discussions**: 2

### Statement

Given a list of existing meetings and a new meeting's required start-time-no-earlier-than and duration, return the earliest start that fits. Delivered as the AI-coding round in recent loops — the interviewer watches whether the candidate uses the assistant for boilerplate while keeping the data-structure decision (sorted-by-start with binary search vs segment tree) under their own control.

---

## 25. Alert Monitor — Rolling Window + Severity Distribution + Spike Detection

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-04
- **stage**: onsite-coding
- **access**: Free preview
- **roles**: SWE, Infra Eng
- **tags**: data-structure, queue, stack, sliding-window, streaming, interval, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-alert-monitor
- **related discussions**: 3

### Statement

Build an `AlertMonitor` class with three queries against an append-only stream: count alerts in the trailing 15 minutes, return the severity histogram for the current hour-block, and emit a next-greater-volume index per minute. The third sub-task is monotonic-stack territory; the round tests whether the candidate picks the right data structure for each query rather than retrofitting one structure for everything.

---

## 26. Function Inclusive / Exclusive Time

- **category**: coding
- **duration**: 55 min
- **frequency**: Low
- **last asked**: 2025-11-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: stack, string, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/coding-function-exclusive-inclusive-time
- **related discussions**: 2

### Statement

Given a list of `(function_id, start|end, timestamp)` log lines, compute the inclusive and exclusive elapsed time for a target function. LeetCode 636 variant — stack-based bookkeeping where each `start` pushes a context and each `end` pops while attributing elapsed time to the *current top* of the stack.

---

## 27. Distributed Job Scheduler / Calendar Service

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: scheduling, distributed-systems, storage, query-optimization, data-modeling, ci-cd, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-job-scheduler
- **related discussions**: 6

### Statement

Design a job scheduler / calendar service that supports millions of scheduled tasks, range queries for upcoming tasks, and real-time dashboard updates. Variants include event scheduling with timezones and recurring events.

---

## 28. AI-Powered Personalized Recruiter Message Generation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, SWE
- **tags**: mlsd, llm-agent, rag, tool-use, ranking, retrieval, prompt-engineering, evaluation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-personalized-inmail-ai
- **related discussions**: 4

### Statement

Design an end-to-end system that helps recruiters generate personalised outreach: pull the candidate profile, the open role, the recruiter's past outreach style, and produce a draft InMail. Recurring sub-prompts cover tool registration, RAG, LLM fine-tuning vs prompting, draft / human-in-the-loop, and offline + online evaluation.

---

## 29. Metrics & Monitoring Platform

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-20
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: mlsd, system-design, storage, streaming, scaling, ranking, top-k, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-metrics-monitoring-platform
- **related discussions**: 5

### Statement

Design a metrics platform that ingests production telemetry, supports arbitrary-granularity queries, and powers a visualization layer. Recurring twists: `top-k` exceptions by error class, severity histograms over time, and explicit *no-alerting* scope (the interviewer wants you to scope tight and not drift into pager design).

---

## 30. News Feed / Timeline

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: feed, fan-out, ranking, caching, queueing, data-modeling, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-news-feed
- **related discussions**: 1

### Statement

Design a ranked news feed for a read-heavy social product with seconds-level freshness. Use a hybrid fan-out strategy, lightweight per-user inboxes, asynchronous celebrity delivery, bounded history, two-stage ranking, and explicit read-your-writes handling for a user's own posts.

---

## 31. Real-Time Collaborative Code Editor

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, websocket, concurrency, pubsub, scaling, frontend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-collab-coderpad
- **related discussions**: 1

### Statement

Design a CoderPad-like session: one admin can edit, all viewers see real-time updates, new joiners catch up immediately, optional code execution. The interviewer explicitly forbids referring to operational-transform algorithms — derive the propagation model from scratch.

---

## 32. Type-Ahead / Autocomplete Suggestions

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: search, trie, ranking, caching, ann, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-typeahead-autocomplete
- **related discussions**: 2

### Statement

Design a type-ahead service that returns the top-K most relevant completions for a query prefix in `< 100 ms`. Recurring follow-ups: personalisation, multi-region freshness, and tail-latency budget when the trie spans hundreds of millions of phrases.

---

## 33. LinkedIn Learning / Job Recommendation Ranking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-18
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, recommendation, ranking, two-tower, retrieval, feature-engineering, ab-testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-job-recommendation-ranking
- **related discussions**: 2

### Statement

Design a recommender that surfaces LinkedIn Learning courses (or jobs) based on the user's profile, recently applied jobs, and behavioural history. Standard candidate-generation + ranking architecture; the LinkedIn-specific twist is heavy feature engineering on the job-posting side and explicit explainability for the recommended item.

---

## 34. LinkedIn Skills — Data Mining & ML System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, data-engineering, data-mining, retrieval, classification, feature-engineering, evaluation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-linkedin-skills-extraction
- **related discussions**: 2

### Statement

Design the pipeline behind LinkedIn's Skills graph: how skill phrases are extracted from resumes / job posts, normalized into a canonical taxonomy, ranked per profile, and continually updated as new skills emerge. The round is graded as much on data collection / labeling strategy as on modeling.

---

## 35. Single-Machine Key-Value Store with Filesystem Spill

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-12-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: storage, in-memory-database, filesystem, memory-bounded, persistence, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/sd-single-machine-kv-store
- **related discussions**: 3

### Statement

Design a single-machine KV store that holds a billion keys and MB-scale values. Memory is bounded; on-disk format is append-only files; values cannot live entirely in memory. The round straddles system design and data-structure design — many candidates over-pivot to distributed systems and lose signal.

---

## 36. Project Deep-Dive (Four-Axis Rubric)

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-27
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: deep-dive, project-retro, behavioral, leadership, impact, presentation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/behavioral-project-deep-dive
- **related discussions**: 12

### Statement

Conversational deep-dive on one or two recent projects, graded against four axes: Leadership, Craftsmanship, Execution, Impact. Bring an architecture diagram (white-board or pre-drawn) and lead the interviewer through with two-to-three years of context. The round runs from `phone screen` through `onsite` depending on level.

---

## 37. Behavioral / BQ Question Pack (Staff & Senior)

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-16
- **stage**: phone-screen · onsite-bq
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: behavioral, star, leadership, impact, conflict, ambiguity, medium
- **url**: https://www.1point3acres.com/interview/problems/company/linkedin/behavioral-bq-pack
- **related discussions**: 15

### Statement

Recurring behavioral prompts across recent loops, especially at Staff level. LinkedIn's bar is explicit about *leadership narrative depth* — interviewers flag when a Staff candidate's stories sound like Senior-IC work. Bring two-to-three multi-quarter, cross-functional stories with quantified business impact for each top theme.

---
