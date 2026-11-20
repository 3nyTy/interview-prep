# Shopify — 1point3acres Question Bank (14 curated questions)

Source: 1point3acres qbank (`company=shopify`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/shopify>

The site lists 34 entries for this company: **14 curated questions** (below), 20 OJ links, 0 external links. Only the curated 14 are captured here.

> Shopify's loop is unusually lightweight and behavior-heavy: a cognitive assessment and a long "life story" values interview gate most candidates before any real coding. The signature technical round is a 75-minute AI-assisted pair-programming session where bringing your own AI coding tool (Cursor, Claude Code, Cline, Copilot) is encouraged — the real test is communication, reviewing AI-generated code, edge-case handling, and testing rigor rather than algorithmic difficulty. MLE loops add ML modeling and ML system-design rounds built around Shopify's own product-taxonomy, forecasting, and recommendation problems.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Mars Rover / Robot Controller (CLI)
2. LRU Cache
3. Word Guessing Game (Wordle-style)
4. URL Shortener Service (CLI)
5. Text Editor (AI-Assisted Pair Programming)
6. SQL Pair Programming (Data Engineer)
7. Cows and Bulls (Guess the Number)
8. Secret Santa / Gift Assignment from CSV
9. In-Memory File System / Directory Tree
10. SWE / SDE Intern Coding OA (DP + C++/Ruby)
11. ML Modeling Round (Forecasting / Targeting / Fraud)
12. ML System Design: Product Categorization / Taxonomy
13. Life Story / Values Interview
14. Project / Technical Deep Dive

---

## 1. Mars Rover / Robot Controller (CLI)

- **category**: coding
- **duration**: 75 min
- **frequency**: High
- **last asked**: 2026-08-28
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: MLE, SWE
- **tags**: oop-design, simulation, state-machine, ai-tools, ai-allowed, testing, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/mars-rover-robot-controller
- **related discussions**: 12

### Statement

Build a command-line app that controls a robot/rover on a 2D grid: it starts at a position facing a direction and accepts commands to turn left/right and move forward, reporting its position and heading. The single most common Shopify pair-programming prompt; difficulty comes from the follow-ups and the design discussion, not the base problem.

---

## 2. LRU Cache

- **category**: coding
- **duration**: 75 min
- **frequency**: Medium
- **last asked**: 2026-07-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: mle, swe
- **tags**: data-structure, linked-list, hashmap, ttl, ai-tools, ai-allowed, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/lru-cache
- **related discussions**: 5

### Statement

Implement a Least Recently Used (LRU) cache with O(1) operations and an eviction policy when capacity is exceeded, then discuss productionizing it (TTL, concurrency, logging). A frequent pair-programm...

---

## 3. Word Guessing Game (Wordle-style)

- **category**: coding
- **duration**: 75 min
- **frequency**: Medium
- **last asked**: 2026-05-12
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: mle, swe
- **tags**: simulation, string, backtracking, ai-tools, ai-allowed, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/word-guessing-game
- **related discussions**: 4

### Statement

Implement a Wordle-style word guessing game: initialized with a dictionary and a (randomly chosen) target word, the user enters guesses and gets per-position hints, with a limited number of attempts. ...

---

## 4. URL Shortener Service (CLI)

- **category**: coding
- **duration**: 75 min
- **frequency**: Medium
- **last asked**: 2026-05-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: mle, swe
- **tags**: hashing, oop-design, design, ai-tools, ai-allowed, testing, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/url-shortener-service
- **related discussions**: 5

### Statement

Implement a local, runnable URL shortener (encode a long URL to a short code and resolve it back), exposed as a CLI/API. Lightweight on the algorithm but heavy on the design conversation: encoding sch...

---

## 5. Text Editor (AI-Assisted Pair Programming)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: object-design, string-processing, ai-tools, ai-allowed
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/text-editor-output-formats
- **related discussions**: 1

### Statement

Build a text editor supporting append and remove, then organize it around clear abstractions and extend its output to formats such as binary, Morse code, and HTML.

---

## 6. SQL Pair Programming (Data Engineer)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-01-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: ds
- **tags**: sql, data-engineering, ai-allowed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/sql-pair-programming-de
- **related discussions**: 1

### Statement

The DE technical screen: a 1-hour CoderPad SQL session with ~4 problems run live against two tables (a shop table with known duplicates and a sessions table), escalating from a simple daily aggregate ...

---

## 7. Cows and Bulls (Guess the Number)

- **category**: coding
- **duration**: 75 min
- **frequency**: Single report
- **last asked**: 2025-06-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: mle
- **tags**: simulation, string, hashmap, ai-tools, ai-allowed, testing, io, easy
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/cows-and-bulls
- **related discussions**: 1

### Statement

Implement the Cows and Bulls game: one player sets a secret number, the other guesses; score a 'bull' for each digit that matches in both value and position, and a 'cow' for each digit that is the rig...

---

## 8. Secret Santa / Gift Assignment from CSV

- **category**: coding
- **duration**: 75 min
- **frequency**: Low
- **last asked**: 2026-07-27
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: mle, swe
- **tags**: csv, sampling, array, hashmap, ai-tools, ai-allowed, ambiguous-spec, easy
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/secret-santa-gift-assignment
- **related discussions**: 3

### Statement

Read a CSV list of people and their emails, then build a gift-giving system that randomly assigns each person someone to give a gift to such that nobody is assigned themselves, and print the result. T...

---

## 9. In-Memory File System / Directory Tree

- **category**: coding
- **duration**: 75 min
- **frequency**: Low
- **last asked**: 2026-07-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: mle, swe
- **tags**: trie, filesystem, design, oop-design, ai-tools, ai-allowed, medium
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/in-memory-file-system
- **related discussions**: 2

### Statement

Design an in-memory file/directory query system supporting directory listing, directory and file creation, and file reads, with error handling for conflicts. The intended structure is a trie/tree of p...

---

## 10. SWE / SDE Intern Coding OA (DP + C++/Ruby)

- **category**: coding
- **duration**: 75 min
- **frequency**: Low
- **last asked**: 2025-08-29
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: oa, dp, cpp, ruby, string, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/swe-intern-coding-oa
- **related discussions**: 2

### Statement

The 2026 SWE/SDE intern online assessment: a CoderPad coding section with a ~45-minute DP problem (any language), a short C++-only string-manipulation problem, and a short Ruby-only list-filter proble...

---

## 11. ML Modeling Round (Forecasting / Targeting / Fraud)

- **category**: system-design
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-05-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: mle
- **tags**: mlsd, ml-knowledge, feature-engineering, evaluation, classification, ranking
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/ml-modeling-round
- **related discussions**: 8

### Statement

A modeling-focused MLE round: given a real Shopify ML problem, walk through how you'd model it end-to-end — framing (classification vs. regression), data and features, algorithm choice with trade-offs...

---

## 12. ML System Design: Product Categorization / Taxonomy

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-05-10
- **stage**: onsite-system-design
- **access**: Members
- **roles**: mle
- **tags**: mlsd, classification, data-mining, search, personalization, scaling, ml-knowledge
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/ml-system-design-product-categorization
- **related discussions**: 7

### Statement

The MLE system-design round, most often a product-categorization problem over Shopify's product taxonomy (10,000+ hierarchical categories across 26+ business verticals): design real-time and streaming...

---

## 13. Life Story / Values Interview

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-10
- **stage**: recruiter-screen · onsite-bq
- **access**: Members
- **roles**: all-roles
- **tags**: behavioral, culture-fit, values, why-company, star
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/life-story-values-interview
- **related discussions**: 13

### Statement

Shopify's defining behavioral round: a ~1-hour 'life story' conversation with a recruiter/HR walking through your entire career, the reasoning behind every transition, and your alignment with Shopify'...

---

## 14. Project / Technical Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-10
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: mle, swe
- **tags**: deep-dive, technical-deep-dive, presentation, ml-knowledge
- **url**: https://www.1point3acres.com/interview/problems/company/shopify/project-technical-deep-dive
- **related discussions**: 10

### Statement

A ~1-hour project deep dive: pick one impactful, complex, end-to-end project and present it (an Excalidraw/diagram or a slide or two is welcome), then field detailed follow-ups on trade-offs, conflict...

---
