# JPMorgan Chase — 1point3acres Question Bank (24 curated questions)

Source: 1point3acres qbank (`company=jpmorgan`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/jpmorgan>

The site lists 84 entries for this company: **24 curated questions** (below), 60 OJ links, 0 external links. Only the curated 24 are captured here.

> JPMorgan Chase's engineering loop is predictable but operationally uneven: a light HackerRank-style OA is followed by a Super Day with coding, code review, system design, and behavioral rounds rotating across candidates. The technical bar skews practical: simple algorithms, review of production-ish Java code, concurrency/style critiques, and system designs around traffic, search, inventory, payments, and URL routing.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Customer Request PR Review
2. Zero Compaction Array Coding
3. Prerequisite Cycle OA
4. Interval Consolidation OA
5. Markdown Table of Contents
6. Interval Sign Flips
7. Minimum Swaps to Sort Descending
8. Split Array: Left Sum Greater
9. Circular Active Computers Window
10. Delete-One-Character Positions
11. Alternating Triplets
12. Alphanumeric Vowel / Consonant Count
13. Minimum CPU Cores
14. Minimum Anagram Edits
15. Grouped Binary Substrings
16. URL Shortener With Click Tracking
17. Property Rental Search System
18. Million-User Flash Sale System
19. Shopping Cart Latency and Inventory
20. Super Day Behavioral Round
21. Agentic Portfolio Workflow Deep Dive
22. C++ Virtual / Destructor / Pointer Review
23. Domino Tiling Recurrences
24. Quant Probability and Statistics Orals

---

## 1. Customer Request PR Review

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-07-10
- **stage**: onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: code-review, code-reading, refactoring, concurrency, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/customer-request-pr-review
- **related discussions**: 9

### Statement

Review a production-style customer-request class or PR and identify engineering issues before coding a small algorithm. The recurring critique areas are hard-coded values, single-responsibility violations, global variables, thread safety, low-level concurrency primitives such as `volatile`, and general maintainability.

---

## 2. Zero Compaction Array Coding

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-23
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, two-pointer, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/zero-compaction-array-coding
- **related discussions**: 1

### Statement

After the code-review portion, the interviewer asked the original LeetCode Move Zeroes prompt. The candidate solved it quickly and used the remaining time for conversation, which reinforces that clarity and edge cases matter more than algorithmic depth in this slot.

---

## 3. Prerequisite Cycle OA

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-23
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: graph, topological-sort, cycle-detection, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/prerequisite-cycle-oa
- **related discussions**: 1

### Statement

The OA used the original LeetCode Course Schedule prompt: given course prerequisites, decide whether all courses can be completed. It appeared in a two-question, medium-difficulty HackerRank assessment.

---

## 4. Interval Consolidation OA

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-23
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: interval, sorting, array, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/interval-consolidation-oa
- **related discussions**: 1

### Statement

The OA used the original LeetCode Merge Intervals prompt: given intervals, merge all overlapping intervals and return the consolidated list. It was paired with the prerequisite-cycle OA in the same assessment.

---

## 5. Markdown Table of Contents

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-14
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, simulation, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/markdown-table-of-contents
- **related discussions**: 1

### Statement

Given an array of document lines, generate a table of contents: lines starting with `#` are chapters, lines starting with `##` are sections under the latest chapter, and other lines are ignored.

---

## 6. Interval Sign Flips

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-19
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: interval, array, prefix-sum, simulation, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/interval-sign-flips
- **related discussions**: 1

### Statement

Given an integer array and a list of intervals, flip the sign of every element inside each interval, then return the final array.

---

## 7. Minimum Swaps to Sort Descending

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-10-29
- **stage**: oa
- **access**: Members
- **roles**: RS
- **tags**: array, sorting, cycle-detection, oa, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/minimum-swaps-sort-descending
- **related discussions**: 1

### Statement

Given an array, compute the minimum number of swaps needed to sort it in decreasing order.

---

## 8. Split Array: Left Sum Greater

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-09-05
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: array, prefix-sum, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/split-array-left-sum-greater
- **related discussions**: 1

### Statement

Given an integer array, count how many split positions divide it into a left sublist and a right sublist such that the left sum is greater than the right sum.

---

## 9. Circular Active Computers Window

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-07-29
- **stage**: oa · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, array, binary-string, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/circular-active-computers-window
- **related discussions**: 1

### Statement

Given `n` computers arranged in a circle as 0/1 states, find the maximum number of active computers in any `k` adjacent computers.

---

## 10. Delete-One-Character Positions

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-07-29
- **stage**: oa · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, two-pointer, string-processing, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/delete-one-character-positions
- **related discussions**: 1

### Statement

Given strings `s1` and `s2`, where `s2` can be obtained by deleting one character from `s1`, return all deletion positions that make the transformation valid.

---

## 11. Alternating Triplets

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-06-30
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, simulation, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/alternating-triplets
- **related discussions**: 1

### Statement

Given an array, inspect every adjacent triple and count triples where the middle element is greater than both neighbors or less than both neighbors.

---

## 12. Alphanumeric Vowel / Consonant Count

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-06-30
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, string-processing, parsing, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/alphanumeric-vowel-consonant-count
- **related discussions**: 1

### Statement

Determine whether a string contains only letters and digits; if it does, count vowel and consonant letters.

---

## 13. Minimum CPU Cores

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-31
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: interval, sorting, scheduling, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/minimum-cpu-cores
- **related discussions**: 2

### Statement

Given process start and end times, compute the minimum number of CPU cores required so every process can run. Endpoints are inclusive, so a process ending at time `t` overlaps a process starting at `t`.

---

## 14. Minimum Anagram Edits

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-18
- **stage**: oa · tech-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, string, string-processing, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/minimum-anagram-edits
- **related discussions**: 2

### Statement

Compute the minimum number of character operations needed to make one string an anagram of another. One OA variant provides two strings directly; another splits a numeric string into two halves and compares those halves.

---

## 15. Grouped Binary Substrings

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-14
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: binary-string, string, string-processing, oa, easy
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/grouped-binary-substrings
- **related discussions**: 2

### Statement

Count substrings of a binary string where the number of `0`s equals the number of `1`s and all `0`s and all `1`s appear in two contiguous groups, such as `000111`, `111000`, `01`, or `10`.

---

## 16. URL Shortener With Click Tracking

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-04-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: url-shortener, caching, storage, analytics-dashboard, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/url-shortener-with-click-tracking
- **related discussions**: 5

### Statement

Design a URL-shortening service, then extend it to track clicks. Interviewers usually stay at overall architecture depth: encoding, redirect path, storage, collision handling, latency, and analytics write flow.

---

## 17. Property Rental Search System

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-08
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: search, payment, caching, storage, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/property-rental-search-system
- **related discussions**: 1

### Statement

Redesign a property-rental website for global customers on cloud infrastructure. The prompt names payment, view, guest, search, and update services, with emphasis on fast, secure search and property browsing.

---

## 18. Million-User Flash Sale System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-06-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: flash-sale, queueing, rate-limiting, idempotency, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/million-user-flash-sale-system
- **related discussions**: 1

### Statement

Design a system that handles a million-user flash-sale event. The expected discussion covers necessary components, happy and unhappy paths, and smoothing high-concurrency traffic.

---

## 19. Shopping Cart Latency and Inventory

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: caching, storage, messaging, scaling, backend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/shopping-cart-latency-and-inventory
- **related discussions**: 3

### Statement

Design or improve an e-commerce shopping/cart system with fast user-facing operations, out-of-stock handling, duplicate-order prevention, and safe concurrent purchase of scarce inventory. Some versions frame it as improving an existing system; another asks for a shopping-website design with two focused deep dives.

---

## 20. Super Day Behavioral Round

- **category**: behavioral
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-20
- **stage**: onsite-bq
- **access**: Members
- **roles**: SWE, RS
- **tags**: leadership, project-deep-dive, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/superday-behavioral-round
- **related discussions**: 8

### Statement

JPMorgan Chase behavioral rounds are conventional but broad: project walkthroughs, proud work, blockers, conflict, leadership, helping colleagues, and collaboration scenarios. Managers may also ask about past work in detail.

---

## 21. Agentic Portfolio Workflow Deep Dive

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-08-04
- **stage**: tech-screen
- **access**: Members
- **roles**: MLE
- **tags**: llm, agents, agentic-workflow, tool-use, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/agentic-portfolio-workflow-deep-dive
- **related discussions**: 1

### Statement

The first half of an Applied AI Engineer screen is a project deep dive into an agentic portfolio workflow: model and provider choices, workflow behavior, ownership of endpoints, data access and entitlements, agent-orchestrator composition, and firsthand MCP server work. The interviewer then checks motivation and role alignment before moving to Python coding.

---

## 22. C++ Virtual / Destructor / Pointer Review

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-15
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: RS
- **tags**: cpp, code-reading, object-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/cpp-virtual-destructor-pointer-review
- **related discussions**: 1

### Statement

Quant finance onsite includes C++ code-reading and language-semantics questions around virtual functions, destructors, dangling pointers, and pointer-vs-reference differences.

---

## 23. Domino Tiling Recurrences

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-15
- **stage**: tech-screen · onsite-deep-dive
- **access**: Members
- **roles**: rs
- **tags**: dp, combinatorics, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/domino-tiling-recurrences
- **related discussions**: 1

### Statement

Quant finance screeners ask for tiling counts and, more importantly, the intuition behind the recurrence. The first-round version uses a `2 x N` rectangle with `1 x 2` / `2 x 1` dominoes; an onsite fo...

---

## 24. Quant Probability and Statistics Orals

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-15
- **stage**: tech-screen · onsite-deep-dive
- **access**: Members
- **roles**: rs
- **tags**: probability, expected-value, combinatorics, math-reasoning, medium
- **url**: https://www.1point3acres.com/interview/problems/company/jpmorgan/quant-probability-and-statistics-orals
- **related discussions**: 2

### Statement

Quant finance rounds include oral probability and statistics: nested radicals, couple-selection probability, dice expected value, first-heads coin-toss probability, and tests for whether a sample foll...

---
