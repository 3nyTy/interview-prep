# Figma — 1point3acres Question Bank (18 curated questions)

Source: 1point3acres qbank (`company=figma`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/figma>

The site lists 34 entries for this company: **18 curated questions** (below), 16 OJ links, 0 external links. Only the curated 18 are captured here.

> Figma builds its interviews around its own product surface and runs them as collaborative, test-case-driven pair-programming sessions where candidates pick their own language and prompts are deliberately long and under-specified. Coding signal rewards clean handling of messy object models over algorithmic trickiness; system-design rounds reward pragmatic, right-sized solutions over over-engineering. Since the IPO the bar has tightened, and final leveling is often settled in a separate executive conversation.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Document / Layer — Apply, Undo, Commit Batch, Redo
2. FigJam Table — Selection & Set Color
3. Sort Documents on a 2D Plane
4. File / Folder / Team Permissions — Fewest Grants
5. Data Engineer Screen — SQL Collaborator Model + IPv4 Validation
6. ML Coding — BERT Span / Keyphrase Extraction
7. Validate Brackets & Parse Nested Rich Text
8. Resize Stacked Rectangles — Distribute Height
9. Real-Time Comment Threads (FigJam / Figma)
10. Trending System / Top-K Hot Files
11. Permission-Aware Retrieval & Ranking for Figma Files
12. Async Job Scheduler
13. ML System Design — Asset / Template Recommendation & Feed
14. ML Model Design — Prompt-to-Design Generation
15. Template & Instance System with Update Propagation
16. Hiring Manager & Behavioral Rounds
17. Project Deep Dive
18. Deep Dive — What Happens When You Access a URL

---

## 1. Document / Layer — Apply, Undo, Commit Batch, Redo

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-06
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: object-design, data-structure, stack, simulation, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/document-layer-apply-undo
- **related discussions**: 12

### Statement

Figma's signature phone-screen coding question. Build a Document made of layers, where each layer holds key/value properties. Implement applying a property to a layer and undoing operations, then extend to batched commits and redo. Algorithmically light; the challenge is a clean state model and handling the verbose, awkward spec.

---

## 2. FigJam Table — Selection & Set Color

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-12
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: object-design, data-structure, simulation, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/figjam-table-selection-set-color
- **related discussions**: 5

### Statement

Model a FigJam table that supports selecting the whole table, a single row, or a single cell, then setting a color on the entire current selection. The interesting part is a layered color model where ...

---

## 3. Sort Documents on a 2D Plane

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-12
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: array, sorting, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/sort-documents-left-to-right
- **related discussions**: 6

### Statement

Given `Document { id, x, y, width, height, children }` rectangles on a 2D canvas, first sort by x; then form transitive groups from overlapping half-open vertical intervals `[y, y + height)`, order gr...

---

## 4. File / Folder / Team Permissions — Fewest Grants

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: tree, dfs, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/file-system-permissions
- **related discussions**: 4

### Statement

Given Teams, Folders, and Files arranged in a hierarchy where each node lists user ids that can access it (and access inherits down to all descendants), answer access queries for a user — typically th...

---

## 5. Data Engineer Screen — SQL Collaborator Model + IPv4 Validation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: phone-screen
- **access**: Members
- **roles**: ds
- **tags**: sql, python, data-engineering, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/data-engineer-sql-collaborator-ip-validation
- **related discussions**: 1

### Statement

Data Engineer phone screen with a SQL half and a Python half. SQL: over users/files/actions tables, find each user's first design-share event, then build a 'closest collaborator' model from share orde...

---

## 6. ML Coding — BERT Span / Keyphrase Extraction

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-31
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: mle, rs
- **tags**: pytorch, transformer, ml-knowledge, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/bert-keyphrase-span-extraction
- **related discussions**: 3

### Statement

ML coding round in a notebook: extract the single most relevant span from a text (e.g. an IMDB review) by predicting start/end positions with BERT. Scaffolding is provided; you implement the model (in...

---

## 7. Validate Brackets & Parse Nested Rich Text

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: stack, parsing, recursion, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/nested-brackets-rich-text-parse
- **related discussions**: 3

### Statement

Two-parter: first validate that a tokenized string of parentheses and characters is well-formed; then parse a valid nested bracket string into the equivalent nested list structure.

---

## 8. Resize Stacked Rectangles — Distribute Height

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-09
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: array, greedy, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/figma/resize-rectangles-distribute-height
- **related discussions**: 2

### Statement

A set of rectangles each have a height (minimum 1). Given a signed resize delta, distribute it across the rectangles: positive deltas split evenly; negative deltas shrink as evenly as possible while n...

---

## 9. Real-Time Comment Threads (FigJam / Figma)

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-12
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: websocket, sse, data-modeling, collaboration, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/figma/realtime-comments-design
- **related discussions**: 7

### Statement

Design the feature where a user clicks anywhere on a Figma/FigJam canvas to start a comment thread, and comments update in real time for everyone viewing the file. Often framed as 'design the data mod...

---

## 10. Trending System / Top-K Hot Files

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-07
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: top-k, streaming, caching, scaling, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/figma/trending-top-k-hot-files
- **related discussions**: 6

### Statement

Design a real-time trending system: ingest a high-volume stream of impression/view events and serve a frequently-updated global top-K list of the hottest items (files, search terms, or articles). Disc...

---

## 11. Permission-Aware Retrieval & Ranking for Figma Files

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-04
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: search, retrieval, ranking, access-control, distributed-systems, hard
- **url**: https://www.1point3acres.com/interview/problems/company/figma/permission-aware-file-retrieval-ranking
- **related discussions**: 1

### Statement

Design retrieval and ranking for a Figma design-editing agent across current files, design systems, components, styles, and prior designs. The system serves users and agents issuing short queries with...

---

## 12. Async Job Scheduler

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-12
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: job-system, scheduling, idempotency, distributed-systems, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/figma/async-job-scheduler-design
- **related discussions**: 2

### Statement

Design an asynchronous job scheduler supporting two job types: run-immediately jobs and delayed jobs that fire after a timer (e.g. one hour). The focus is fault tolerance — exactly-once execution and ...

---

## 13. ML System Design — Asset / Template Recommendation & Feed

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: mle, rs
- **tags**: recommendation, feed, ranking, mlsd, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/figma/ml-recsys-assets-feed
- **related discussions**: 2

### Statement

ML system-design round: build a recommender for Figma assets/templates, or a home feed. Treated as a fairly standard newsfeed-style recsys — candidate generation plus ranking — adapted to Figma's cont...

---

## 14. ML Model Design — Prompt-to-Design Generation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-13
- **stage**: onsite-system-design · onsite-research
- **access**: Members
- **roles**: mle, rs
- **tags**: vlm, llm, evaluation, mlsd, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/figma/prompt-to-design-generation
- **related discussions**: 2

### Statement

ML model-design round: design a generative system that turns a natural-language prompt into a design — e.g. a VLM that outputs a concept image, or a model that outputs webpage HTML from a description....

---

## 15. Template & Instance System with Update Propagation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2024-05-28
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe
- **tags**: data-modeling, notification, versioning, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/figma/template-instance-propagation-system
- **related discussions**: 2

### Statement

Design a template/instance model (Figma components, or online slide/PPT templates): a template defines a reusable object and instances customize it. When a template changes, instances should reflect t...

---

## 16. Hiring Manager & Behavioral Rounds

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-08-12
- **stage**: onsite-bq · final-round
- **access**: Members
- **roles**: all-roles
- **tags**: behavioral, conflict, ownership, why-company
- **url**: https://www.1point3acres.com/interview/problems/company/figma/hiring-manager-behavioral
- **related discussions**: 17

### Statement

Figma's behavioral footprint spans a hiring-manager screen, an onsite all-around/BQ round, and (for senior roles) a director-level BQ. Common themes: most challenging project, scope/ownership, conflic...

---

## 17. Project Deep Dive

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-12
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: swe, mle, rs
- **tags**: deep-dive, presentation, project-retro
- **url**: https://www.1point3acres.com/interview/problems/company/figma/project-deep-dive
- **related discussions**: 7

### Statement

A dedicated onsite round to present and defend a past project, often slide-based. Pick your largest, most complex work, anonymize/abstract it as needed, and be ready for deep technical follow-ups. For...

---

## 18. Deep Dive — What Happens When You Access a URL

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-02-01
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: swe
- **tags**: networking, cs-fundamentals, http, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/figma/deep-dive-url-request-lifecycle
- **related discussions**: 1

### Statement

Open-ended deep-dive round: explain everything that happens when you enter a URL in a browser. The interviewer probes wherever you go — DNS, CDN, service discovery, authentication, inter-service commu...

---
