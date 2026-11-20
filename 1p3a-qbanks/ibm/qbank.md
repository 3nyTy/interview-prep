# IBM — 1point3acres Question Bank (19 curated questions)

Source: 1point3acres qbank (`company=ibm`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/ibm>

The site lists 129 entries for this company: **19 curated questions** (below), 110 OJ links, 0 external links. Only the curated 19 are captured here.

> IBM's recent technical screens are dominated by auto-triggered HackerRank online assessments for entry-level, intern, full-stack, backend, AI, and .NET roles. The question mix is mostly easy-to-medium data structures, sliding-window, interval, graph-connectivity, and small frontend/CSS tasks, while Red Hat / AI-infrastructure loops add live coding around inference kernels, Kubernetes controllers, and DevOps fundamentals.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. .NET OA: 3^x * 5^y Count and Decreasing Subarrays
2. Maximum Palindromes After Cross-String Swaps
3. Minimum Length Subarray with K Distinct Numbers
4. JSON Prediction Diff
5. Minimum Insertions to Form Repeated abc Pattern
6. Minimum Link Reallocation to Connect Repositories
7. Kubernetes Controller for Pod Balance
8. 1-D Valid Convolution with Multithreading Follow-up
9. Minimum Replacements for Adjacent Duplicate Characters
10. VM Rental Revenue from Changing Stock
11. Maximum Concurrent Processes / Meeting Rooms
12. Process Execution Time (Merge Inclusive Intervals)
13. Frontend CSS Flexbox Layout Task
14. Earliest Coordinate Reach with Optional Moves
15. Sliding-Window Rate Limiter / Abuse IP Detection
16. Minimum Removal Rounds by Smallest Response Time
17. Max Distinct Counts After Splitting Array
18. Competency and Resume Interview
19. DevSecOps Kubernetes / CI-CD Technical Screen

---

## 1. .NET OA: 3^x * 5^y Count and Decreasing Subarrays

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-25
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: math, array, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/dotnet-number-and-decreasing-subarray-oa
- **related discussions**: 1

### Statement

A Calgary .NET engineer OA contains two easy coding tasks: count numbers of the form `3^x * 5^y` in a range, and count subarrays of length at least two where each next element is one less than the pre...

---

## 2. Maximum Palindromes After Cross-String Swaps

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-22
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: string, hashmap, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/maximum-palindromes-after-cross-string-swaps
- **related discussions**: 1

### Statement

Given an array of lowercase strings, any operation may swap one character from one string with one character from another string. Return the maximum number of strings that can be made palindromic.

---

## 3. Minimum Length Subarray with K Distinct Numbers

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-22
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: sliding-window, hashmap, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/minimum-length-subarray-with-k-distinct
- **related discussions**: 1

### Statement

Given an integer list and `k`, return the minimum subarray length containing exactly or at least `k` distinct numbers, or `-1` when no such subarray exists.

---

## 4. JSON Prediction Diff

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-07
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: json, hashmap, string-processing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/json-prediction-diff
- **related discussions**: 1

### Statement

Parse two JSON-formatted strings and return keys whose values differ between the two prediction objects.

---

## 5. Minimum Insertions to Form Repeated abc Pattern

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-11-07
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: string, greedy, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/minimum-insertions-to-abc-pattern
- **related discussions**: 1

### Statement

Given a lowercase string, insert the fewest characters needed so the result is a repetition of `abc` blocks. Deletion and replacement are not allowed.

---

## 6. Minimum Link Reallocation to Connect Repositories

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-09-21
- **stage**: oa
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: graph, union-find, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/minimum-link-reallocation
- **related discussions**: 1

### Statement

Given `link_nodes` repositories and existing undirected sync links, compute the minimum number of existing links that must be reassigned so every repository becomes connected. If there are fewer than ...

---

## 7. Kubernetes Controller for Pod Balance

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-08-25
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, infra-eng
- **tags**: distributed-systems, sre, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/kubernetes-controller-pod-balance
- **related discussions**: 1

### Statement

Implement a Kubernetes-controller-style loop from a provided base class so pod counts remain balanced, then discuss controller semantics and API-server failure handling.

---

## 8. 1-D Valid Convolution with Multithreading Follow-up

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-08-13
- **stage**: tech-screen
- **access**: Members
- **roles**: mle, infra-eng
- **tags**: numpy, parallelism, optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/one-dimensional-valid-convolution
- **related discussions**: 1

### Statement

Implement valid 1-D convolution over a numeric input and kernel with bias, then discuss CPU/hardware optimisations and how to parallelise under different input/kernel/thread sizes.

---

## 9. Minimum Replacements for Adjacent Duplicate Characters

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-08-27
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: string, greedy, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/minimum-replacements-adjacent-duplicates
- **related discussions**: 2

### Statement

For each word, find the minimum number of character replacements needed so that no two adjacent characters are equal. Return one replacement count per input word.

---

## 10. VM Rental Revenue from Changing Stock

- **category**: coding
- **frequency**: Low · New
- **last asked**: 2026-08-27
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: heap, greedy, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/vm-rental-revenue
- **related discussions**: 2

### Statement

Given the available instance counts for several VM types and a number of arriving customers, repeatedly rent the type with the highest remaining stock. Charge that current stock count, decrement it, a...

---

## 11. Maximum Concurrent Processes / Meeting Rooms

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-26
- **stage**: oa
- **access**: Free preview
- **roles**: SWE, Infra Eng
- **tags**: interval, interval-aggregation, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/maximum-concurrent-processes
- **related discussions**: 2

### Statement

Given process or meeting intervals with inclusive endpoints, return the maximum number active at the same time. One version is a direct Meeting Rooms II-style prompt over `List<List<Integer>> meetingTimings`; another describes distributed-service process logs.

---

## 12. Process Execution Time (Merge Inclusive Intervals)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-11-03
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: interval, merge, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/process-execution-time-merge-intervals
- **related discussions**: 2

### Statement

Given arrays of process start and end times, compute the total amount of time during which at least one process is running. Endpoints are inclusive, so merged interval length is `end - start + 1`.

---

## 13. Frontend CSS Flexbox Layout Task

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-10-07
- **stage**: oa
- **access**: Members
- **roles**: mle, swe
- **tags**: frontend, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/frontend-css-flexbox-layout
- **related discussions**: 3

### Statement

A frontend-labelled IBM OA includes one standard coding problem plus a CSS task. HTML is provided, and the task is to write CSS selectors/classes to arrange elements, often flexbox-style.

---

## 14. Earliest Coordinate Reach with Optional Moves

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-09-09
- **stage**: oa
- **access**: Members
- **roles**: swe, mle
- **tags**: dp, grid, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/earliest-coordinate-reach-with-optional-moves
- **related discussions**: 2

### Statement

Given a string of directions and start/end coordinates, return the earliest second at which the target can be reached if each second allows either taking the indicated move or staying still.

---

## 15. Sliding-Window Rate Limiter / Abuse IP Detection

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-09-09
- **stage**: oa
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: sliding-window, queue, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/sliding-window-rate-limiter-and-abuse-ips
- **related discussions**: 2

### Statement

Maintain per-user or per-IP activity within a `t`-second window. One version returns allow/deny for each request; another returns IPs that appear more than `k` times in any window.

---

## 16. Minimum Removal Rounds by Smallest Response Time

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-07-09
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: heap, linked-list, simulation, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/minimum-removal-rounds-by-smallest-response-time
- **related discussions**: 2

### Statement

Given response times, repeatedly choose the currently smallest remaining request and remove it together with its left and right neighbours. Return the total number of removal operations needed.

---

## 17. Max Distinct Counts After Splitting Array

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-07-09
- **stage**: oa
- **access**: Members
- **roles**: swe
- **tags**: hashmap, set, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/distinct-count-split
- **related discussions**: 2

### Statement

Given an integer array, split it into two non-empty subarrays and maximise `distinct(left) + distinct(right)`. The expected solution updates left/right frequency structures in one pass.

---

## 18. Competency and Resume Interview

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-02-05
- **stage**: onsite-bq
- **access**: Members
- **roles**: swe, mle, all-roles
- **tags**: behavioral, bq, star, why-company, deep-dive
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/competency-and-resume-interview
- **related discussions**: 2

### Statement

IBM intern and co-op follow-up interviews can be non-coding behavioural/resume rounds. One video-style competency screen gives one minute to prepare and three minutes to answer each question; another ...

---

## 19. DevSecOps Kubernetes / CI-CD Technical Screen

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-23
- **stage**: tech-screen
- **access**: Members
- **roles**: infra-eng, swe
- **tags**: cs-fundamentals, sre, troubleshooting, networking, docker
- **url**: https://www.1point3acres.com/interview/problems/company/ibm/devsecops-kubernetes-cicd-screen
- **related discussions**: 1

### Statement

A DevSecOps intern interview starts with self-introduction and resume deep dive, then asks rapid-fire infrastructure questions around CI/CD, Argo CD, Helm, Kubernetes troubleshooting, Linux, networkin...

---
