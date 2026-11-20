# Harvey — 1point3acres Question Bank (9 curated questions)

Source: 1point3acres qbank (`company=harvey`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/harvey>

The site lists 38 entries for this company: **9 curated questions** (below), 20 OJ links, 9 external links. Only the curated 9 are captured here.

> Harvey builds domain-specific generative-AI tools for law firms and legal teams, layering retrieval and document-workflow products (Vault / Data Room) on top of frontier LLMs. Its interview loop is small and stable: a single coding screen drawn from a tight rotation, system-design rounds anchored on Harvey's own document products, and a values-driven behavioral bar — with recruiters who actively prep candidates before each round.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Text Highlighting / Citation Tagging
2. Spreadsheet / Formula Engine
3. In-Memory File System
4. Duplicate File Discovery
5. Database Connection Pool
6. RAG Notebook (ML Coding)
7. Design Google Drive / Data Room
8. Harvey Values / Behavioral
9. Project Deep Dive

---

## 1. Text Highlighting / Citation Tagging

- **category**: coding
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-08-09
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: string, interval, interval-aggregation, parsing, hashmap, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/text-highlighting-citation
- **related discussions**: 16

### Statement

Given an LLM-generated sentence and a list of source phrases, wrap every word-level matched region in <yellow></yellow> tags, merging overlapping matches into a single block. The follow-up appends every contributing source index after each block, ordered by global source frequency descending and then source index ascending.

---

## 2. Spreadsheet / Formula Engine

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-04
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: dependency-resolution, topological-sort, cycle-detection, dfs, hashmap, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/spreadsheet-formula-engine
- **related discussions**: 12

### Statement

Build a mini spreadsheet: set_cell(label, value) / get_cell(label), where a label is letters+digits (e.g. "A1"). Values are integers or +-only formulas referencing other cells; reads should be O(1), w...

---

## 3. In-Memory File System

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-25
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: filesystem, hashmap, parsing, recursion, data-structure, hashing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/in-memory-file-system
- **related discussions**: 6

### Statement

Implement an in-memory file system with addFile(path) and getFile(path): adding stores a file at a slash-delimited path (creating folders as needed), and getFile returns either a file's contents or th...

---

## 4. Duplicate File Discovery

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-29
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: filesystem, dfs, bfs, graph, hashing, io, medium
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/duplicate-file-discovery
- **related discussions**: 1

### Statement

Given directory-listing and path-type helpers, implement find_dups(root) to traverse a filesystem and group paths whose file contents are identical. Handle symlink cycles, then reason about unreadable...

---

## 5. Database Connection Pool

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-04-30
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: concurrency, object-design, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/db-connection-pool
- **related discussions**: 1

### Statement

Onsite coding round: given a mocked db.query, implement a connection pool — acquire() to hand out a connection and release() to return it — managing a fixed set of reusable connections.

---

## 6. RAG Notebook (ML Coding)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-25
- **stage**: onsite-coding
- **access**: Members
- **roles**: mle, swe
- **tags**: rag, retrieval, pandas, colab, evaluation, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/rag-ml-coding-notebook
- **related discussions**: 3

### Statement

ML-coding round in a Colab notebook: fill in a simple RAG pipeline — generate text embeddings from a pandas DataFrame, retrieve the top-10 by cosine similarity to the query embedding, and run a short ...

---

## 7. Design Google Drive / Data Room

- **category**: system-design
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-07
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe, mle
- **tags**: system-design, storage, access-control, scaling, schema-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/design-google-drive-data-room
- **related discussions**: 9

### Statement

Design Google Drive / Harvey's Data Room: file-and-folder storage with upload/download, large-file handling, and a heavy focus on access control (organization-level ACLs). Default content is PDFs; mul...

---

## 8. Harvey Values / Behavioral

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-02
- **stage**: onsite-bq
- **access**: Members
- **roles**: swe, mle
- **tags**: behavioral, values, culture-fit, conflict, mentorship, leadership, collaboration
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/harvey-values-behavioral
- **related discussions**: 10

### Statement

Behavioral round (Harvey doesn't call it a BQ) anchored on the published Harvey Values: bring experiences mapped to those values. Common probes cover conflict/disagreement, feedback, mentorship style,...

---

## 9. Project Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-25
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: swe, mle
- **tags**: deep-dive, project-deep-dive, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/harvey/project-deep-dive
- **related discussions**: 12

### Statement

Project deep-dive with the hiring manager / engineering lead: you present a past project while they probe its timeline, tech-stack choices, the decisions and trade-offs behind them, and how you handle...

---
