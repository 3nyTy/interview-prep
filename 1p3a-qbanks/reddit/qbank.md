# Reddit — 1point3acres Question Bank (27 curated questions)

Source: 1point3acres qbank (`company=reddit`), metadata scraped 2026-09-20; statements paywalled.
Company page: <https://www.1point3acres.com/interview/problems/company/reddit>

The site lists 74 entries for this company: **28 curated questions**, 27 captured below, 26 OJ links, 20 external links.

> Reddit's loop runs on a small, slowly-rotating bank of company-specific coding prompts (Tennis Scoring, Report Chain, Chat Message Merge) paired with product-flavored system-design rounds (Game Leaderboard, Live Chat, Notification System). The bar is high on clean OOD and crisp clarification — multi-part prompts are routinely under-scoped on purpose, and rounds end without finishing all parts. Ads and ML Platform orgs add a hiring-manager domain round on top of the standard loop; recent cohorts also report aggressive down-leveling and long team-match limbo after a passing loop.

⚠️ **This bank is paywalled.** 27 of 28 questions render as locked ("会员专享题目") on the site and their statements are truncated to ~200-char teasers. Titles, tags, roles, frequency and stage below are authoritative; full statements were never captured. The teaser text for each locked question is in `questions.json` under `overview_teaser`.

Question statements only. The site's answer body is not reproduced here; the page as served is archived under `raw/`.

## Index

1. Tennis Match Scoring
2. Billing Status Replay (OOD)
3. Rate Limiter (Open-Ended Coding)
4. Moderator List Hierarchy
5. Report Chain / Org Tree
6. Chat Message Merge with Windowed API
7. Product Sense: Improve Reddit Onboarding
8. Admin Action Log: Order & Permission
9. Pseudo-Memcached Protocol Server
10. Odd Even Linked List
11. Shortest Palindrome
12. Post / Comment Ranking System
13. Word Search
14. Load Balancer Debugging & Extension
15. Word Search II
16. Word Sequence Validation
17. Comment-Prediction ML System (will_user_comment_on_posts)
18. ML Feature Store
19. Video Recommendation
20. MLE Live Coding: EDA + Post-Category CTR
21. Game Leaderboard System Design
22. Word Ladder (LC 127)
23. Word Ladder with One- or Two-Character Moves
24. Hiring-Manager Domain Round
25. ML Fundamentals
26. Subreddit Live Chat System
27. Notification System

---

## 1. Tennis Match Scoring

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-16
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: object-design, oop-design, simulation, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/tennis-match-scoring
- **related discussions**: 8

### Statement

Multi-part object-oriented design problem modeling a tennis match. Implement point / game / set / match scoring rules, then extend to best-of-three / best-of-five formats and run-time queries. The most common Reddit phone-screen prompt.

---

## 2. Billing Status Replay (OOD)

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-09-02
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE, SWE
- **tags**: medium, oop-design, object-design, simulation, stack, transactions, python
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/billing-status-replay
- **related discussions**: 3

### Statement

_Paywalled. The site serves only this teaser:_

> Rebuild one billing-state object per advertiser by replaying a transaction log in increasing timestamp order, breaking ties by transaction id. Model a BillingStatus class that ingests transactions and...

---

## 3. Rate Limiter (Open-Ended Coding)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: rate-limiting, sliding-window, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/rate-limiter-open-ended
- **related discussions**: 2

### Statement

_Paywalled. The site serves only this teaser:_

> Implement a per-user rate limiter and walk through the design trade-offs interactively. The interviewer leaves the exact algorithm open — sliding window log, sliding window counter, token bucket, leak...

---

## 4. Moderator List Hierarchy

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-01-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, parsing, csv, hashmap, simulation, sorting, access-control, moderation, design-implementation
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/moderator-list-hierarchy
- **related discussions**: 2

### Statement

_Paywalled. The site serves only this teaser:_

> Parse newline-delimited, comma-separated action logs to model a Reddit-style moderator hierarchy, where each moderator's effective access timestamp determines their rank and a moderator may remove onl...

---

## 5. Report Chain / Org Tree

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-20
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: tree, parsing, tree-traversal, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/report-chain-org-tree
- **related discussions**: 6

### Statement

_Paywalled. The site serves only this teaser:_

> Build an organizational reporting hierarchy from a list of (employee, manager) pairs, then answer multi-part queries against it: pretty-print the indented tree, find skip-level pairs, locate an indivi...

---

## 6. Chat Message Merge with Windowed API

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-29
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, merge, sliding-window, dedup, two-pointer, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/chat-message-merge
- **related discussions**: 5

### Statement

_Paywalled. The site serves only this teaser:_

> Given a provided chat API that returns a window of messages centered on a target message id, implement a class that merges the context windows of an input list of message ids into a single sorted, de-...

---

## 7. Product Sense: Improve Reddit Onboarding

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: onsite-bq · onsite-research
- **access**: Members
- **roles**: MLE
- **tags**: product-design, ux, ambiguity, open-ended, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/product-sense-improve-onboarding
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Open-ended product round: pick a part of Reddit's onboarding flow and propose improvements. The interviewer cares about the framework — user segmentation, goal definition, hypothesis generation, measu...

---

## 8. Admin Action Log: Order & Permission

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, hashmap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/admin-action-log-ordering
- **related discussions**: 2

### Statement

_Paywalled. The site serves only this teaser:_

> Parse chronological moderator action logs, track active moderators by most-recent promotion time, answer whether one moderator can remove another by seniority, and return the current ranking. The newe...

---

## 9. Pseudo-Memcached Protocol Server

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: backend, networking, parsing, in-memory-database, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/pseudo-memcached-protocol
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Implement a simplified memcached-style TCP key-value server supporting line-based `get` / `set` commands with byte-count-framed values and a multi-key `get` bonus. Reddit's "Backend Coding" round, whi...

---

## 10. Odd Even Linked List

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: linked-list, two-pointer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/odd-even-linked-list
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Given the head of a singly linked list, group all odd-indexed nodes together followed by the even-indexed nodes (1-based, first node is odd), preserving relative order within each group, and return th...

---

## 11. Shortest Palindrome

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-02
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, SWE
- **tags**: string, palindromes, two-pointer, hard
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/shortest-palindrome
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Given a string, prepend the fewest characters to the front to make it a palindrome and return the result. This is LeetCode 214: the linear-time approach finds the longest palindromic prefix via a KMP ...

---

## 12. Post / Comment Ranking System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ranking, recommendation, feed, feature-engineering, inference, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/post-comment-ranking-sd
- **related discussions**: 2

### Statement

_Paywalled. The site serves only this teaser:_

> Design a ranking system for posts (subreddit / home feed) or comments (within a post). Covers candidate generation, feature retrieval, online inference, A/B testing, and the cold-start path. The inter...

---

## 13. Word Search

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-18
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, backtracking, grid, dfs
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/word-search

### Statement

_Paywalled. The site serves only this teaser:_

> Determine whether a target string can be formed by a path through a 2-D character grid, moving only to horizontally or vertically adjacent cells and reusing no cell. Solved with grid DFS plus backtrac...

---

## 14. Load Balancer Debugging & Extension

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: backend, code-reading, debugging, circuit-breaker, troubleshooting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/load-balancer-debugging-and-extension
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Debug and extend a provided load-balancer codebase across four parts: repair failed health checks, diagnose excess latency, add a circuit breaker for unhealthy servers, and replace round-robin routing...

---

## 15. Word Search II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: trie, grid, dfs, backtracking, string, hard
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/word-search-ii

### Statement

_Paywalled. The site serves only this teaser:_

> Given a 2-D character grid and a list of up to 30,000 words, return every word that can be traced along a path of horizontally or vertically adjacent cells, with no cell reused within a single word. T...

---

## 16. Word Sequence Validation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/word-sequence-validation
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Validate a supplied word sequence. The examples distinguish a valid single-character transition from a two-character jump and an unchanged adjacent word.

---

## 17. Comment-Prediction ML System (will_user_comment_on_posts)

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-06-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ranking, recommendation, inference, feature-engineering, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/comment-prediction-mlsd
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Staff-level MLE system design: build an end-to-end ML system exposing `will_user_comment_on_posts(user_id, [post_ids])` that scores each post by the probability the user would comment, used as a signa...

---

## 18. ML Feature Store

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-23
- **stage**: onsite-system-design · onsite-research
- **access**: Members
- **roles**: MLE
- **tags**: mlsd, ml-infra, feature-engineering, ranking, caching, ci-cd, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/feature-store-sd
- **related discussions**: 3

### Statement

_Paywalled. The site serves only this teaser:_

> Design a feature store that serves both training pipelines (batch reads, point-in-time correctness) and online inference (low-latency lookups by entity id). Discuss deployment, CI/CD, caching, online/...

---

## 19. Video Recommendation

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-03-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, SWE
- **tags**: system-design, mlsd, recommendation, ranking, retrieval, ml-infra, metrics, data-engineering, personalization
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/video-recommendation-sd
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> ML system-design round: architect a video recommendation system covering candidate generation, ranking models, and serving. The interviewer pushes hard on the infrastructure side — how services are sp...

---

## 20. MLE Live Coding: EDA + Post-Category CTR

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-09-10
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: pandas, scikit-learn, feature-engineering, classification, ai-allowed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/mle-eda-ctr-notebook
- **related discussions**: 4

### Statement

_Paywalled. The site serves only this teaser:_

> Live Jupyter notebook session. Load a small post-engagement dataset (per-row reading time across post categories plus a click label), do quick EDA, build a CTR classifier, and discuss model choice and...

---

## 21. Game Leaderboard System Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-02
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, MLE
- **tags**: leaderboard, ranking, redis, sharding, caching, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/game-leaderboard-sd
- **related discussions**: 3

### Statement

_Paywalled. The site serves only this teaser:_

> Design a real-time global game leaderboard that supports score updates and top-K queries for millions of players. The interviewer starts from a single-server skeleton (10 reads / 1 write per second, n...

---

## 22. Word Ladder (LC 127)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-09-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, graph, codesignal
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/word-ladder
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Find the length of the shortest single-character transformation sequence from a start word to an end word using a supplied dictionary. The candidate explicitly identified the second phone-screen quest...

---

## 23. Word Ladder with One- or Two-Character Moves

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-07-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: bfs, graph, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/word-ladder-two-char-moves
- **related discussions**: 1

### Statement

_Paywalled. The site serves only this teaser:_

> Given a dictionary of words and a start and end word, decide whether a transformation path exists where each step changes one or two characters. Variant of classic word ladder with two-character moves...

---

## 24. Hiring-Manager Domain Round

- **category**: behavioral
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-01-29
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: SWE, MLE
- **tags**: behavioral, deep-dive, leadership, fit, ads, ml-infra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/hiring-manager-domain-round
- **related discussions**: 4

### Statement

_Paywalled. The site serves only this teaser:_

> Conversation with the hiring manager that mixes standard behavioral prompts with deep, role-specific domain interrogation. For Ads roles the pivot lands on ad-tech mechanics; for ML Platform it lands ...

---

## 25. ML Fundamentals

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-09-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: ml-knowledge, classification, feature-engineering, evaluation, calibration, verbal, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/ml-fundamentals-oral
- **related discussions**: 3

### Statement

_Paywalled. The site serves only this teaser:_

> A fundamentals-heavy ML discussion built around modeling a feature x against a target y, then interpreting a plot of two overlapping class-conditional distributions to reason about separability, Bayes...

---

## 26. Subreddit Live Chat System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: chat, websocket, fan-out, messaging, presence, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/live-chat-system-sd
- **related discussions**: 3

### Statement

_Paywalled. The site serves only this teaser:_

> Design Reddit's subreddit live-chat product (real-time chat anchored to a subreddit, room sizes ranging from tens to millions during live events). The Super Bowl scenario — fan-out to ~5M concurrent u...

---

## 27. Notification System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: notification, push-notification, fan-out, messaging, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/reddit/notification-system-sd
- **related discussions**: 3

### Statement

_Paywalled. The site serves only this teaser:_

> Design Reddit's notification system: deliver in-app, push, and email notifications for events like replies, mentions, mod actions, and trending posts. Supports per-user preferences, bundling/digesting...

---
