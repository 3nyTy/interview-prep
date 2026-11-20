# Okta — 1point3acres Question Bank (12 curated questions)

Source: 1point3acres qbank (`company=okta`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/okta>

The site lists 18 entries for this company: **12 curated questions** (below), 6 OJ links, 0 external links. Only the curated 12 are captured here.

> Okta runs a lightweight, team-driven loop with a fast weekly cadence — recruiter call, a short online assessment, one or two interviewer-watched coding rounds, and hiring-manager conversations, with offer decisions made at the team level rather than a central committee. Coding leans practical over heavily algorithmic, and the assessment stack is unusually AI-forward: recent OAs explicitly allow AI tools and lean on hands-on builds against Okta/Auth0's real identity APIs.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Network Access-Control Checker
2. Reverse Substrings Between Each Pair of Parentheses
3. Concurrent Simple Bank System
4. CD Rental System (Java DSA / LLD)
5. Array Duplication — O(n) and Memory Trade-offs
6. Python + SQL Technical Round (Typing, Debugging, Joins)
7. Validate IPv4 Addresses in an Array
8. Build an Auth0-based MCP Server (Hands-on OA)
9. Dependency Cycle Detection (Deadlock)
10. Request Rate Limiter / Sliding Window
11. Memory Allocator (Allocate / Free)
12. Hiring-Manager / Project Chat + AI Philosophy

---

## 1. Network Access-Control Checker

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-29
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: access-control, object-design, concurrency, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/network-access-control-checker
- **related discussions**: 1

### Statement

Implement a network-admission checker around users, roles, and allow/deny permissions. Support assigning permissions to roles, assigning roles to users, and evaluating a user's access to requested pro...

---

## 2. Reverse Substrings Between Each Pair of Parentheses

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: string, stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/reverse-substrings-in-parentheses
- **related discussions**: 1

### Statement

Reverse the text inside every matched pair of parentheses, working from the innermost pair outward, and return the result without parentheses. The phone-screen version uses the canonical problem witho...

---

## 3. Concurrent Simple Bank System

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: object-design, concurrency, race-condition, transactions, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/concurrent-simple-bank-system
- **related discussions**: 1

### Statement

Extend the canonical numbered-account bank model so deposit, withdrawal, and transfer remain thread-safe; the follow-up explicitly asks for a compare-and-swap approach such as `map.replace(oldValue, n...

---

## 4. CD Rental System (Java DSA / LLD)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: object-design, hashmap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/cd-rental-system
- **related discussions**: 1

### Statement

Implement a CD-rental service initialized from `[shop, cd, price]` records, with price-ordered shop search plus validated rent and return operations.

---

## 5. Array Duplication — O(n) and Memory Trade-offs

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-02
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: array, hashmap, frontend, code-review, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/array-deduplication
- **related discussions**: 1

### Statement

A Staff Frontend coding round: solve an array-duplication problem in O(n) time and space, then re-implement with object-based inline computation and discuss its memory-overhead risk on large datasets,...

---

## 6. Python + SQL Technical Round (Typing, Debugging, Joins)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: python, sql, debugging, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/python-sql-debugging-round
- **related discussions**: 1

### Statement

A senior/Staff technical round centered on Python and SQL fluency: Python typing, code debugging, complexity estimation, and table joins, followed by open discussion.

---

## 7. Validate IPv4 Addresses in an Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-20
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: string, parsing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/okta/ipv4-validation
- **related discussions**: 1

### Statement

Given an array of strings, determine whether every element is a valid IPv4 address. A deliberately easy warm-up in the senior/Staff loop, with an unrevealed follow-up.

---

## 8. Build an Auth0-based MCP Server (Hands-on OA)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-22
- **stage**: oa
- **access**: Free preview
- **roles**: SWE
- **tags**: oa, codesignal, ai-allowed, api-integration, access-control, tool-use, llm-agent, http, security, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/oa-mcp-auth0-server
- **related discussions**: 1

### Statement

A hands-on CodeSignal OA: build an Auth0-based MCP (Model Context Protocol) server with JWT authentication middleware and a single whoami tool, wired against Okta/Auth0's real identity APIs. AI tools are explicitly allowed.

---

## 9. Dependency Cycle Detection (Deadlock)

- **category**: coding
- **duration**: 50 min
- **frequency**: Single report
- **last asked**: 2025-11-05
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: graph, dfs, cycle-detection, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/dependency-cycle-detection
- **related discussions**: 1

### Statement

Resources depend on each other; a dependency cycle means deadlock. Given a list of lists where L[i] are the resources that resource i depends on, return True if any cycle exists, else False.

---

## 10. Request Rate Limiter / Sliding Window

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-05
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: hashmap, sliding-window, binary-search, heap, rate-limiting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/rate-limiter
- **related discussions**: 1

### Statement

Given a time-sorted list of (IP, timestamp) request pairs, a window W and an allowance Amnt, output accept/reject for each request: accept only while that IP has fewer than Amnt accepted requests in t...

---

## 11. Memory Allocator (Allocate / Free)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-05
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: simulation, data-structure, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/okta/memory-allocator
- **related discussions**: 1

### Statement

Implement a memory manager over a 0/1 array. Process a command stream of allocate/free operations: allocate the left-most consecutive free chunk and assign it an ascending id; free a chunk by id. Retu...

---

## 12. Hiring-Manager / Project Chat + AI Philosophy

- **category**: behavioral
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2026-06-24
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: behavioral, deep-dive, ai-collaboration, leadership
- **url**: https://www.1point3acres.com/interview/problems/company/okta/hm-project-and-ai-philosophy
- **related discussions**: 3

### Statement

Hiring-manager and project-lead conversations centered on resume/project deep-dive plus a recurring AI theme: your view of AI, how it changes your daily work, and how you collaborate with AI tools.

---
