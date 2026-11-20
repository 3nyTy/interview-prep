# Intuit — 1point3acres Question Bank (14 curated questions)

Source: 1point3acres qbank (`company=intuit`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/intuit>

The site lists 44 entries for this company: **14 curated questions** (below), 30 OJ links, 0 external links. Only the curated 14 are captured here.

> Intuit's recent engineering loop is unusually AI-aware: even standard SWE screens often pair a short practical coding task with a separate conversation about using generative AI safely and effectively. New-grad assessment traffic is dominated by mixed-format OA work across Python, SQL, and Bash, while experienced screens skew toward product-flavored implementation and applied data tasks.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Small Business Network: Degrees of Separation
2. Pandas Meeting Work-Duration Calculation
3. DNA Substring Palindrome Cost Sum
4. Transactions Status Report SQL
5. Three-Column Grid Coloring Count
6. Clean String: Remove Punctuation and Stop Words
7. Weather Data Energy-Usage Ranking
8. Look-and-Say Sequence
9. Customer Scheduler: VIP Priority and 2:1 Fairness
10. Array Plus One with Large-Input Follow-Up
11. Bash Largest File from Listing Output
12. Premium Product Recommendation System
13. AI Application Conversation
14. Staff Project Architecture and Delivery Deep Dive

---

## 1. Small Business Network: Degrees of Separation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-08
- **stage**: phone-screen
- **access**: Free preview
- **roles**: SWE
- **tags**: graph, bfs, search, ambiguous-spec, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/small-business-network-degrees-of-separation
- **related discussions**: 1

### Statement

Build a QuickBooks-flavored graph feature that returns the shortest business relationship path between two companies. The prompt is under-specified, so clarifying the input and return type is part of the screen.

---

## 2. Pandas Meeting Work-Duration Calculation

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-22
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, ds
- **tags**: pandas, data-cleaning, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/pandas-meeting-work-duration
- **related discussions**: 1

### Statement

Applied screen for an AI Scientist role: process a CSV with employee id, meeting id, join time, and leave time, then compute each employee's longest daily work duration after accounting for meetings.

---

## 3. DNA Substring Palindrome Cost Sum

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2025-12-25
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: string, palindromes, dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/dna-substring-palindrome-cost-sum
- **related discussions**: 1

### Statement

OA coding task: for every substring of a DNA string, compute the minimum character-change cost to make it a palindrome, then return the total cost as a long integer.

---

## 4. Transactions Status Report SQL

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2025-12-25
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: sql, transactions, data-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/transactions-status-report-sql
- **related discussions**: 1

### Statement

OA SQL task over a `transactions` table: aggregate by status and produce total transaction count, total amount, and an ordered list of failure reasons.

---

## 5. Three-Column Grid Coloring Count

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2025-11-19
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: grid, dp, state-compression-dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/three-column-grid-coloring-count
- **related discussions**: 1

### Statement

OA coding task: count ways to color an `n x 3` grid with three colors so adjacent cells in each row and column are not the same, with `n <= 20000` and modulo `1e9+7`.

---

## 6. Clean String: Remove Punctuation and Stop Words

- **category**: coding
- **duration**: 90 min
- **frequency**: Single report
- **last asked**: 2025-11-19
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: string-processing, parsing, io, easy
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/clean-string-remove-punctuation-stop-words
- **related discussions**: 1

### Statement

OA Bash task: clean an input string by removing specified punctuation and words appearing in a stop-word list, then return the cleaned string.

---

## 7. Weather Data Energy-Usage Ranking

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-10-03
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, ds
- **tags**: pandas, data-cleaning, feature-engineering, python, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/weather-data-energy-usage-ranking
- **related discussions**: 1

### Statement

Applied AI Scientist screen in a notebook: load three years of weather data for one city and rank days by likely heating/cooling energy usage.

---

## 8. Look-and-Say Sequence

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-09-29
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: string, compression, easy
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/look-and-say-sequence
- **related discussions**: 1

### Statement

OA coding task on the look-and-say sequence, shown with the progression `11, 21, 1211, ...`.

---

## 9. Customer Scheduler: VIP Priority and 2:1 Fairness

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-24
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: object-design, queue, scheduling, easy
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/customer-scheduler-vip-fairness
- **related discussions**: 3

### Statement

Implement `Customer` and `Scheduler` classes for serving VIP and normal customers. Start with strict VIP priority, then change the scheduler to serve VIP and normal customers in a 2:1 ratio.

---

## 10. Array Plus One with Large-Input Follow-Up

- **category**: coding
- **duration**: 75 min
- **frequency**: Low · New
- **last asked**: 2026-06-28
- **stage**: phone-screen
- **access**: Members
- **roles**: swe
- **tags**: array, math, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/array-plus-one-with-large-input-follow-up
- **related discussions**: 2

### Statement

Phone-screen coding prompt: given an array of digits such as `[1, 2, 3]`, add one to the number represented by the array and return the resulting digit array. The follow-up asks how the approach chang...

---

## 11. Bash Largest File from Listing Output

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-01-30
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: io, parsing, string-processing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/bash-largest-file-from-ls-output
- **related discussions**: 2

### Statement

OA Bash/text-processing task: parse file-listing style input and return the largest file name, including cases where file names contain spaces.

---

## 12. Premium Product Recommendation System

- **category**: system-design
- **duration**: 75 min
- **frequency**: Low · New
- **last asked**: 2026-06-28
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: swe, mle
- **tags**: recommendation, mlsd, ml-infra, ranking, caching, llm, experiment-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/premium-product-recommendation-system
- **related discussions**: 2

### Statement

Design a recommendation or prediction system for Intuit products. One variant recommends premium-product content or offers; a newer staff+ variant predicts the top-selling items in a user-selected cat...

---

## 13. AI Application Conversation

- **category**: other
- **duration**: 30 min
- **frequency**: High
- **last asked**: 2026-08-24
- **stage**: recruiter-screen · phone-screen · tech-screen
- **access**: Members
- **roles**: swe, mle
- **tags**: ai-collaboration, prompt-engineering, llm, ai-safety, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/ai-application-conversation
- **related discussions**: 8

### Statement

A recurring verbal round on practical generative-AI usage: when AI should be used, when it should not, how to prompt for constrained outputs, and how to deliver software features with AI while verifyi...

---

## 14. Staff Project Architecture and Delivery Deep Dive

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-08-24
- **stage**: final-round
- **access**: Members
- **roles**: swe
- **tags**: deep-dive, technical-deep-dive, presentation, leadership, llm, open-ended
- **url**: https://www.1point3acres.com/interview/problems/company/intuit/staff-project-architecture-delivery-deep-dive
- **related discussions**: 1

### Statement

A composite Staff SWE final stage lasting more than three hours: introduce yourself and a past project with slides, answer project-specific questions, implement a change in the provided playground, th...

---
