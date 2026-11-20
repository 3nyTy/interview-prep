# Atlassian — 1point3acres Question Bank (20 curated questions)

Source: 1point3acres qbank (`company=atlassian`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/atlassian>

The site lists 64 entries for this company: **20 curated questions** (below), 44 OJ links, 0 external links. Only the curated 20 are captured here.

> Atlassian interviews emphasize pragmatic product engineering: API design, backend data structures, careful clarification, and values-driven collaboration. Senior loops add management-signal and leveling weight, while MLE loops still include substantial backend coding alongside recommendation, RAG, and ML systems design.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Org Tree Lowest Common Department
2. Popular Content Counter
3. Stream Moving Average with Recent-Value Weighting
4. Expanding Tennis Club Court Assignment
5. Agent Rating Average Ranking
6. Cart Routes Origin Destinations
7. Robot Parts Assembly
8. Karat Carpool on Linear Routes
9. Shopping Category Trip Difference
10. Middleware Router Wildcards and Path Params
11. Rate Limiter Function and Distributed Design
12. Sequential, Deduped, and Parallel URL Fetcher
13. Commodity Price Checkpoints
14. Image Link Crawler Service
15. RAG Search Augmentation and Internal Chatbot
16. Role and Resource Access System
17. Tagging System REST API
18. Product Feed and Shopping Recommendation System
19. Atlassian Values and Management Rounds
20. Full-Stack Craft: Login and Upload Flow

---

## 1. Org Tree Lowest Common Department

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-02-15
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: tree, tree-traversal, medium, testing
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/org-tree-lowest-common-department
- **related discussions**: 9

### Statement

Given a company organization tree where internal nodes are departments or groups and leaves are employees, return the lowest department that contains every requested employee. Variants expand from two employees to an arbitrary set, then to employees or orgs with multiple parents and dynamic membership changes.

---

## 2. Popular Content Counter

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: data-structure, hashmap, linked-list, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/popular-content-all-o-one
- **related discussions**: 4

### Statement

Design a content-popularity tracker with increment, decrement, and most-popular operations. Follow-ups push toward O(1) operations and deterministic tie-breaking when multiple content IDs share the to...

---

## 3. Stream Moving Average with Recent-Value Weighting

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-02-26
- **stage**: onsite-coding
- **access**: Members
- **roles**: mle
- **tags**: sliding-window, streaming, queue, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/stream-moving-average-weighted-recent-values
- **related discussions**: 5

### Statement

Given a stream of numeric values and a window size, compute the moving average over the last N items. Follow-ups ask for a weighted moving average that emphasizes recent values and whether it can be d...

---

## 4. Expanding Tennis Club Court Assignment

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-02-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: interval, heap, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/expanding-tennis-club-court-assignment
- **related discussions**: 5

### Statement

Given tennis-court bookings with start and finish times, assign each booking to a court so no court has overlapping bookings while using the minimum number of courts. Follow-ups add maintenance buffer...

---

## 5. Agent Rating Average Ranking

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-02-22
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: hashmap, heap, ranking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/agent-rating-average-ranking
- **related discussions**: 7

### Statement

Implement a rating service for support agents or customer-service reps. The core APIs accept 1-5 ratings and return agents sorted by average rating; follow-ups focus on tie-breaking and maintaining th...

---

## 6. Cart Routes Origin Destinations

- **category**: coding
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2025-11-06
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: graph, dfs, bfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/cart-routes-origin-destinations
- **related discussions**: 1

### Statement

Given directed route pairs for autonomous delivery carts in a robot factory, identify every starting location and all possible ending destinations reachable from each start.

---

## 7. Robot Parts Assembly

- **category**: coding
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2025-11-06
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: hashmap, set, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/robot-parts-assembly
- **related discussions**: 1

### Statement

Given part identifiers that combine a robot name and a part name, return all robots that have every required part. The prompt uses names like `Rosie_wheel` and a comma-separated required-parts string.

---

## 8. Karat Carpool on Linear Routes

- **category**: coding
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2025-07-03
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: graph, shortest-path, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/karat-carpool-linear-routes
- **related discussions**: 1

### Statement

Given directed linear roads, two car starting locations, and people living at locations on the route to a campground, assign each person to the car that reaches them first. Ties can go to either car.

---

## 9. Shopping Category Trip Difference

- **category**: coding
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2025-07-03
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: array, hashmap, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/shopping-category-trip-difference
- **related discussions**: 1

### Statement

Given product-to-category mappings and a shopping list, compare category transitions under different shopping-trip orderings. The prompt asks for the difference in trip counts between the original ord...

---

## 10. Middleware Router Wildcards and Path Params

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-21
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: trie, parsing, backend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/middleware-router-wildcards-path-params
- **related discussions**: 3

### Statement

Build an in-process router that maps paths to string results. The base interface supports exact route registration and lookup; scale-ups add wildcard path segments and path parameters.

---

## 11. Rate Limiter Function and Distributed Design

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-31
- **stage**: phone-screen · onsite-coding · onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: rate-limiting, sliding-window, redis, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/rate-limiter-function-and-distributed-design
- **related discussions**: 2

### Statement

Start with a small `shouldPass` rate-limiter function, then discuss how the same limiter works across API gateways or services. Follow-ups focus on rejected requests, per-customer or per-IP limits, me...

---

## 12. Sequential, Deduped, and Parallel URL Fetcher

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-30
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: api-integration, async, dedup, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/sequential-deduped-parallel-url-fetcher
- **related discussions**: 2

### Statement

Given a list of URLs, send requests and return each response with URL, status code, and body. Follow-ups add deduplication, tests, and parallel request execution under a short phone-screen time box.

---

## 13. Commodity Price Checkpoints

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-09-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: heap, hashmap, snapshot, mvcc, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/commodity-price-checkpoints
- **related discussions**: 3

### Statement

Maintain commodity or stock prices keyed by timestamp, return the current maximum, then add checkpointed historical reads. The follow-up assigns checkpoint IDs on update and asks for the price effecti...

---

## 14. Image Link Crawler Service

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: web-crawler, api-integration, storage, queueing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/image-link-crawler-service
- **related discussions**: 4

### Statement

Design a service that accepts one or many root URLs, crawls pages and sublinks, extracts image links, and lets clients query job status and discovered images. The design is framed around API shape, pe...

---

## 15. RAG Search Augmentation and Internal Chatbot

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-02-26
- **stage**: tech-screen · onsite-system-design
- **access**: Members
- **roles**: mle
- **tags**: rag, llm, ann, search, recommendation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/rag-search-augmentation-internal-chatbot
- **related discussions**: 5

### Statement

Design an internal assistant or search augmentation layer over employee logs, Confluence, Jira, GDrive, or product data. The system detects fact-seeking/task-seeking queries, retrieves relevant docume...

---

## 16. Role and Resource Access System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: access-control, api-integration, schema-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/role-and-resource-access-system
- **related discussions**: 2

### Statement

Design an access-control system that supports both role-based and resource-based permissions. The expected direction is a flexible policy model rather than only fixed user-to-role mappings.

---

## 17. Tagging System REST API

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: api-integration, schema-design, data-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/tagging-system-rest-api
- **related discussions**: 3

### Statement

Design a tagging system with REST APIs for creating tags, associating tags to resources, deleting tags, and finding tagged objects. Interviewers focus on API design details and edge cases more than br...

---

## 18. Product Feed and Shopping Recommendation System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-01-05
- **stage**: tech-screen · onsite-system-design
- **access**: Members
- **roles**: mle
- **tags**: recommendation, ranking, personalization, two-tower, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/product-feed-and-shopping-recommendation-system
- **related discussions**: 3

### Statement

Design a recommendation system for a product feed, Jira-style recommendations, or an Amazon-style shopping surface. Variants distinguish landing-page recommendations from search-engine recommendations...

---

## 19. Atlassian Values and Management Rounds

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-04-21
- **stage**: onsite-bq
- **access**: Members
- **roles**: swe, mle
- **tags**: values, leadership, ambiguity, mentorship, culture-fit
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/atlassian-values-and-management-rounds
- **related discussions**: 8

### Statement

Atlassian's later-stage behavioral loop is usually split into a values round and a management or hiring-manager round. Questions repeatedly target mentorship, helping teammates, feedback, ownership, a...

---

## 20. Full-Stack Craft: Login and Upload Flow

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2025-11-19
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: swe
- **tags**: fullstack, frontend, backend, networking, react, medium
- **url**: https://www.1point3acres.com/interview/problems/company/atlassian/fullstack-craft-login-and-upload-flow
- **related discussions**: 5

### Statement

Full-stack candidates may get a craft or Karat-style discussion that starts from a user action, then walks through frontend, network, backend, database, and rendering behavior. Variants include login,...

---
