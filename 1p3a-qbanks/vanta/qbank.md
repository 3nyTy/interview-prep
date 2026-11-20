# Vanta — 1point3acres Question Bank (10 curated questions)

Source: 1point3acres qbank (`company=vanta`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/vanta>

The site lists 18 entries for this company: **10 curated questions** (below), 8 OJ links, 0 external links. Only the curated 10 are captured here.

> Vanta runs a deliberately small, stable interview loop built around a handful of recycled problems — a topological-sort phone screen, a local-machine `uniq` build, and an internal DAU/MAU analytics design. Difficulty skews easy-to-moderate, but the prompts are verbose and reading-comprehension-heavy, and most rejections trace to requirement-clarification or following the interviewer's instructions rather than raw algorithmic strength.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Implement `uniq` — Unique Lines in a File
2. Task Dependency Resolution (Topological Sort)
3. Employee Training Status + Group Tree Aggregation
4. Word Pattern / Meta-Pattern Match (Backtracking)
5. Test Run Status Monitor (Failing → Passing Windows)
6. Frontend Practical Coding — Work Against a Provided API
7. DAU / MAU Internal Analytics System
8. RAG Q&A Chatbot — ML / AI Technical Deep Dive
9. Principles & Leadership Behavioral Rounds
10. Project Deep Dive

---

## 1. Implement `uniq` — Unique Lines in a File

- **category**: coding
- **duration**: 90 min
- **frequency**: High
- **last asked**: 2026-07-16
- **stage**: onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: io, hashing, streaming, memory-bounded, set, medium
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/unique-lines-command
- **related discussions**: 9

### Statement

Build a CLI command that prints each distinct line of a file once while preserving first-seen order. The graded follow-ups push you toward handling a file too large to fit in memory and preserving global output order when partitioning or spilling data to disk.

---

## 2. Task Dependency Resolution (Topological Sort)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-16
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: topological-sort, graph, dfs, bfs, dependency-resolution, medium
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/task-dependency-resolution
- **related discussions**: 7

### Statement

Given a set of build/CI tasks each with a dependency list, return all tasks required to run a requested set, in a valid execution order with no duplicates — a topological sort dressed up in Vanta-prod...

---

## 3. Employee Training Status + Group Tree Aggregation

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-17
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: tree, dfs, hashmap, simulation, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/training-status-group-aggregation
- **related discussions**: 7

### Statement

Part 1: classify an employee's security-training status (not_required / pending / completed / overdue) on a given check day and compute days overdue. Part 2: roll those numbers up a tree of employee g...

---

## 4. Word Pattern / Meta-Pattern Match (Backtracking)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2024-12-04
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: backtracking, hashmap, dfs, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/word-pattern-meta-match
- **related discussions**: 7

### Statement

A Word Pattern variant: decide whether a numeric pattern can be consistently mapped to the words of a candidate string. The follow-up generalizes each position to a *set* of allowed numbers and asks w...

---

## 5. Test Run Status Monitor (Failing → Passing Windows)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-07-31
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: interval, interval-aggregation, streaming, hashmap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/test-status-monitor
- **related discussions**: 1

### Statement

A logging/query design problem over test runs: ingest (test_id, timestamp, status) events with strictly increasing timestamps, then answer queries about how fast a test recovers from failing to passin...

---

## 6. Frontend Practical Coding — Work Against a Provided API

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-07-24
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: frontend, react, typescript, api-integration, medium
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/frontend-api-practical
- **related discussions**: 2

### Statement

A frontend track of the practical-coding round: build against a provided REST API — wire up fetch GET/POST/update calls, filter the returned data, and use basic data structures — in TypeScript, agains...

---

## 7. DAU / MAU Internal Analytics System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-06-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: system-design, analytics-dashboard, metrics, ingestion, data-engineering
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/dau-mau-analytics-system
- **related discussions**: 5

### Statement

Design an internal analytics system that reports DAU/MAU, most-viewed pages, and the subscription funnel conversion rate for PMs and data scientists — then handle the wrinkle that the new instrumentat...

---

## 8. RAG Q&A Chatbot — ML / AI Technical Deep Dive

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-10
- **stage**: onsite-system-design · onsite-deep-dive
- **access**: Members
- **roles**: mle, rs
- **tags**: mlsd, rag, retrieval, evaluation, llm
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/rag-chatbot-ml-design
- **related discussions**: 3

### Statement

For Applied AI / ML roles, the AI Technical Deep Dive is a collaborative review of a retrieval-augmented Q&A chatbot: ingesting structured and unstructured data, evaluating answer quality, proposing i...

---

## 9. Principles & Leadership Behavioral Rounds

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-10
- **stage**: onsite-bq
- **access**: Members
- **roles**: swe, mle
- **tags**: behavioral, leadership, values, conflict, ambiguity
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/principles-and-leadership-bq
- **related discussions**: 8

### Statement

Vanta's behavioral loop runs in two flavors: a Principles round that hands you a hypothetical scenario and watches how you reason, and a Manager/Leadership round that digs into a project you led and h...

---

## 10. Project Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-06-10
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: swe, mle
- **tags**: deep-dive, presentation, impact
- **url**: https://www.1point3acres.com/interview/problems/company/vanta/project-deep-dive
- **related discussions**: 5

### Statement

A technical walkthrough of the largest / most significant project you've worked on: your specific role, the key technical decisions, and the trade-offs you made.

---
