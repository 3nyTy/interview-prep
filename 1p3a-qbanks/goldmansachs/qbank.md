# Goldman Sachs — 1point3acres Question Bank (39 curated questions)

Source: 1point3acres qbank (`company=goldmansachs`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/goldmansachs>

The site lists 111 entries for this company: **39 curated questions** (below), 72 OJ links, 0 external links. Only the curated 39 are captured here.

> Goldman Sachs interview loops lean heavily on traditional LeetCode-style coding — the firm publishes its own tagged problem list on LeetCode, and the OA / phone screens recycle from a stable bank that maps closely to it. Behavioral signal is concentrated in a Hirevue video screen with tight 30-second prep windows, and quant-flavored desks (GSAM, Quant Engineering) layer probability and statistics drills on top of the standard coding bar.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Highest Average Score Per Person
2. Role Permission Inheritance and Overrides
3. ClayWorkspace Resource Tree
4. Implement a Deque
5. Transaction Authorizer / Fraud Checks
6. Movement Command Controller
7. Transaction Segments / Increasing Subarrays of Length K
8. Ethernet Cable Square Count
9. Equalize Letter Frequencies With Add / Remove
10. Encyclopedia Removal Grid
11. First Missing Positive
12. Extend Queue: Min-Size & Min-Sum Selector
13. Longest Substring of All Same Letter
14. Spiral Matrix Traversal
15. Maximum-Sum Path in a Matrix (No Revisits)
16. Second-Smallest Unique Element
17. Longest Subarray With Sum ≤ K
18. Min × Max Product After Push / Pop
19. Valid Triangle + Point Inclusion
20. Plus-Multiply Even / Odd Parity
21. Chairs / Restaurant Order Simulation
22. Count Unique Pairs With Difference K
23. Anagram Queries on Word List
24. Compliance Alert / Trailing-Average Sliding Window
25. Count Palindromic Substrings
26. Largest Tree in a Forest
27. First Unique Character
28. Efficient Tasks / Three-Server Difficulty Split
29. Process Starvation Time
30. Decode Ways / Alphanumeric Combination
31. Merge K Sorted Lists (incl. K = 3)
32. Trapping Rain Water
33. String Compression (Run-Length)
34. Hirevue Behavioral Bank (6 Questions)
35. Explain Recursion to a Non-CS Audience
36. Quant OA Math / Probability Multiple Choice Bank
37. 25 Horses, 5 Lanes Puzzle
38. Hash Collision: Causes, Resolution, Complexity
39. Quant Strat Superday Probability + Fixed Income Drill

---

## 1. Highest Average Score Per Person

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-07-15
- **stage**: oa · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/max-average-score-per-person
- **related discussions**: 6

### Statement

Given a list of `(name, score)` records, return the highest per-person average score. A recurring Goldman warm-up that doubles as a follow-up about handling ties and streaming input.

---

## 2. Role Permission Inheritance and Overrides

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: access-control, graph, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/role-permission-inheritance-overrides
- **related discussions**: 1

### Statement

Given a list of roles that may define a parent, an acceptlist, and a denylist, implement getPermission(role) to return the role's effective permissions. A child inherits its parent's permissions and overrides them with its own settings; the follow-up extends the model to multiple parents.

---

## 3. ClayWorkspace Resource Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, tree, filesystem, data-structure, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/clay-workspace-resource-tree
- **related discussions**: 1

### Statement

Implement a `ClayWorkspace` backed by a dictionary of resource ids to `Resource` objects. The initial API creates resources and lists a folder's contents; the follow-ups add recursive deletion and moving a resource either to the home level or under another folder id.

---

## 4. Implement a Deque

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-18
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, queue, data-structure, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/implement-deque
- **related discussions**: 1

### Statement

Implement a string deque from scratch with addFirst/addLast, removeFirst/removeLast, peekFirst/peekLast, and getSize, all O(1). A doubly linked list is the expected backing structure; a Python list makes addFirst O(n), which the interviewer probes directly.

---

## 5. Transaction Authorizer / Fraud Checks

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-12
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, hashmap, sliding-window, payment, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/transaction-authorizer-fraud-checks
- **related discussions**: 1

### Statement

Build an in-memory credit-card transaction authorizer. Start with globally blocked MCCs, add per-user blocked MCCs, then implement a rolling one-hour spend velocity limit.

---

## 6. Movement Command Controller

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-05
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/movement-command-controller
- **related discussions**: 1

### Statement

Implement a simple up/down/left/right movement controller from a command string, then adapt it when command characters may mix uppercase and lowercase.

---

## 7. Transaction Segments / Increasing Subarrays of Length K

- **category**: coding
- **duration**: 120 min
- **frequency**: Single report
- **last asked**: 2025-11-25
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, sliding-window, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/transaction-segments-increasing-subarrays
- **related discussions**: 1

### Statement

Given transaction values over time, count contiguous segments of exactly length k whose values are strictly increasing from left to right. The prompt is a clean one-pass window / run-length counting problem from the 2026 Engineering OA.

---

## 8. Ethernet Cable Square Count

- **category**: coding
- **duration**: 180 min
- **frequency**: Single report
- **last asked**: 2025-09-22
- **stage**: oa
- **access**: Members
- **roles**: RS
- **tags**: math, grid, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/ethernet-cable-square-count
- **related discussions**: 1

### Statement

For each grid query `(rows, columns)`, sum the number of possible `a × a` square selections for every side length `a` from 1 through `min(rows, columns)`.

---

## 9. Equalize Letter Frequencies With Add / Remove

- **category**: coding
- **duration**: 180 min
- **frequency**: Single report
- **last asked**: 2025-09-10
- **stage**: oa
- **access**: Members
- **roles**: RS
- **tags**: string, hashmap, greedy, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/equalize-letter-frequencies
- **related discussions**: 1

### Statement

Given a lowercase string, each operation may add one letter or remove one existing letter. Return the minimum operations needed so every present letter has the same frequency.

---

## 10. Encyclopedia Removal Grid

- **category**: coding
- **duration**: 180 min
- **frequency**: Single report
- **last asked**: 2025-09-08
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: grid, graph, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/encyclopedia-removal-grid
- **related discussions**: 1

### Statement

A library shelf is an `n × m` grid of encyclopedias, each labeled by author. Selecting one encyclopedia removes all books by the same author in its row and column; minimize selections to clear the shelf.

---

## 11. First Missing Positive

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-06-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hard
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/first-missing-positive
- **related discussions**: 1

### Statement

Find the smallest positive integer missing from an unsorted array, in O(n) time and O(1) extra space. The cyclic-sort trick is the expected solution.

---

## 12. Extend Queue: Min-Size & Min-Sum Selector

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-06-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, object-design, heap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/queue-extension-min-size-min-sum
- **related discussions**: 1

### Statement

Extend a basic `Queue` class with two selectors over a collection of queues: find the queue with the minimum size, and find the queue with the minimum sum of elements. Tests data-structure design and operation-cost trade-off discussion.

---

## 13. Longest Substring of All Same Letter

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-11-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/longest-same-letter-substring
- **related discussions**: 1

### Statement

Given a string, return the length of the longest contiguous substring consisting of a single repeated character. A simple linear-scan warm-up in VP-level phone screens.

---

## 14. Spiral Matrix Traversal

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-11-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: grid, simulation, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/spiral-matrix-output
- **related discussions**: 1

### Statement

Output the elements of an `m × n` matrix in spiral order, walking from the outside layer to the innermost. A warm-up problem in GSAM Infra phone screens.

---

## 15. Maximum-Sum Path in a Matrix (No Revisits)

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-11-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: grid, dfs, backtracking, hard
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/matrix-max-sum-path-no-repeat
- **related discussions**: 1

### Statement

Find the maximum-sum path through a matrix where cells may contain negative numbers and the path cannot revisit any cell. The hard follow-up to the spiral-matrix warm-up in GSAM Infra.

---

## 16. Second-Smallest Unique Element

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-11-14
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/second-smallest-unique-element
- **related discussions**: 1

### Statement

Return the second-smallest value among elements that occur exactly once in the input array. Intern-level Superday warm-up.

---

## 17. Longest Subarray With Sum ≤ K

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, array, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/longest-subarray-sum-at-most-k
- **related discussions**: 1

### Statement

Classic two-pointer / sliding-window: given an array of non-negative integers and a bound K, return the length of the longest contiguous subarray whose sum does not exceed K.

---

## 18. Min × Max Product After Push / Pop

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, bst, streaming, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/min-max-product-stream
- **related discussions**: 1

### Statement

Maintain a multiset under a stream of push / pop operations and, after each operation, output the product of the current minimum and maximum elements. Tests whether the candidate reaches for a balanced BST / `TreeMap` rather than re-scanning.

---

## 19. Valid Triangle + Point Inclusion

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/valid-triangle-point-inclusion
- **related discussions**: 1

### Statement

Geometry OA: validate that three points form a real triangle, then classify whether each of two query points lies inside it. A coordinate-geometry exercise testing the area-sum invariant.

---

## 20. Plus-Multiply Even / Odd Parity

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: array, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/plus-multiply-even-odd-parity
- **related discussions**: 1

### Statement

Walk the input array splitting it into even-index and odd-index streams; alternate `+` and `*` operations within each stream; compare the final parity of the two sums and return EVEN / ODD / NEUTRAL.

---

## 21. Chairs / Restaurant Order Simulation

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: simulation, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/chairs-restaurant-simulation
- **related discussions**: 1

### Statement

Given a sequence of customer / chair events (`C`, `R`, `U`, `L`), simulate the chairs required to seat every customer. The OA-bank version of an order-simulation problem Goldman has recycled across cycles.

---

## 22. Count Unique Pairs With Difference K

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, set, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/unique-pairs-difference-k
- **related discussions**: 1

### Statement

Count the number of unique pairs `(a, b)` in an integer array such that `b - a = k`. The variant where `k` may be zero is a common trip-up.

---

## 23. Anagram Queries on Word List

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, sorting, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/anagram-queries
- **related discussions**: 1

### Statement

For each query string, return every word from a given word list that is an anagram of the query, sorted lexicographically.

---

## 24. Compliance Alert / Trailing-Average Sliding Window

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2024-10-26
- **stage**: oa
- **access**: Members
- **roles**: SWE
- **tags**: sliding-window, array, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/compliance-alert-sliding-window
- **related discussions**: 1

### Statement

A compliance system raises an alert whenever the trailing-window average of call volumes exceeds a threshold. Count how many such alerts are raised over the full input. A sliding-window staple that recurs in Goldman's OA bank.

---

## 25. Count Palindromic Substrings

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-08-29
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: string, palindromes, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/palindromic-substrings
- **related discussions**: 2

### Statement

Count the total number of palindromic substrings in the input string. The canonical expand-around-center problem appears verbatim in both engineering and Quant Strats OAs, including a 30-minute coding segment for the 2027 summer-intern cycle.

---

## 26. Largest Tree in a Forest

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-12
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, bfs, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/largest-tree-in-forest
- **related discussions**: 2

### Statement

Given a dict mapping child node id -> parent node id that encodes a forest, return the root id of the tree with the most nodes; on a tie, return the smallest root id. Build a parent->children adjacency list, find roots (ids that appear as a value but never as a key), and count each tree's size (e.g. BFS) in O(n). A newer variant supplies parent->child associations directly, so confirm the edge direction and whether the original tie-break still applies.

---

## 27. First Unique Character

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-12-12
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/first-unique-character
- **related discussions**: 2

### Statement

Return the first non-repeating character in a string. Recent CoderPad screens use it as the warm-up before Highest Average Score.

---

## 28. Efficient Tasks / Three-Server Difficulty Split

- **category**: coding
- **duration**: 120 min
- **frequency**: Low
- **last asked**: 2025-11-25
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: array, greedy, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/efficient-tasks-three-server-difficulty
- **related discussions**: 3

### Statement

Partition task difficulties across three servers, then each server chooses one task to minimize `|d1-d2| + |d2-d3|`; maximize that minimized value over all valid partitions. Multiple 2026 OA candidates describe this as the harder coding problem.

---

## 29. Process Starvation Time

- **category**: coding
- **duration**: 180 min
- **frequency**: Low
- **last asked**: 2025-09-22
- **stage**: oa
- **access**: Members
- **roles**: SWE, RS
- **tags**: array, monotonic-stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/process-starvation-time
- **related discussions**: 2

### Statement

Given process priorities executed from right to left, compute each process's starvation time: how long it waits after the earliest lower-priority process to its right starts running.

---

## 30. Decode Ways / Alphanumeric Combination

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-07-11
- **stage**: oa · phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: dp, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/decode-ways
- **related discussions**: 2

### Statement

Classic decode-ways DP: given a digit string, count the number of ways to decode it using the mapping A=1, B=2, …, Z=26. Appears verbatim on Goldman's OA bank.

---

## 31. Merge K Sorted Lists (incl. K = 3)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2025-06-09
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, heap, merge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/merge-k-sorted-lists
- **related discussions**: 2

### Statement

Merge K sorted linked lists into a single sorted linked list. Goldman has asked both the general K-list form and an explicit K=3 variant ("merge three sorted lists in one go").

---

## 32. Trapping Rain Water

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2024-12-27
- **stage**: phone-screen · onsite-coding
- **access**: Free preview
- **roles**: SWE
- **tags**: array, two-pointer, monotonic-stack, hard
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/trapping-rain-water
- **related discussions**: 2

### Statement

Given non-negative heights of bars, compute the total volume of water that can be trapped between them after rain. The single most recurring Goldman coding question — featured on its own tagged LeetCode list.

---

## 33. String Compression (Run-Length)

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2024-12-27
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: string, two-pointer, compression, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/string-compression
- **related discussions**: 2

### Statement

Run-length-encode a character array in place: `[a,a,b,b,c,c,c]` becomes `[a,2,b,2,c,3]`. Goldman's interviewers consistently expect the in-place version, not a fresh-allocation string output.

---

## 34. Hirevue Behavioral Bank (6 Questions)

- **category**: behavioral
- **duration**: 30 min
- **frequency**: Medium
- **last asked**: 2025-11-06
- **stage**: recruiter-screen
- **access**: Members
- **roles**: SWE, All
- **tags**: bq, values, culture-fit, screening, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/hirevue-behavioral-six-questions
- **related discussions**: 4

### Statement

The Goldman Hirevue is a 6-question recorded video interview, 30s prep + 2 min answer per question. Five questions rotate from a small behavioral bank focused on integrity, deadlines, teamwork, and ethics; the sixth is an engineering-specific closer.

---

## 35. Explain Recursion to a Non-CS Audience

- **category**: behavioral
- **duration**: 30 min
- **frequency**: Low
- **last asked**: 2024-12-05
- **stage**: recruiter-screen
- **access**: Members
- **roles**: SWE, All
- **tags**: bq, verbal, cs-fundamentals, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/explain-recursion-no-cs-audience
- **related discussions**: 2

### Statement

The engineering-specific closer of the Goldman Hirevue. Explain recursion to someone with no CS background — what it is, why one might use it, and an everyday analogy.

---

## 36. Quant OA Math / Probability Multiple Choice Bank

- **category**: other
- **duration**: 180 min
- **frequency**: Medium
- **last asked**: 2026-08-29
- **stage**: oa
- **access**: Members
- **roles**: RS, SWE
- **tags**: probability, math-reasoning, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/quant-oa-math-probability-bank
- **related discussions**: 5

### Statement

Goldman quant / engineering OAs include multiple-choice calculus, probability, statistics, and linear-algebra questions alongside coding. Recent examples cover constrained optimization, Laplace-style densities, covariance bounds, coin-run expectations, arc length, matrix inverses, rank inequalities, conditional probability, and standard errors.

---

## 37. 25 Horses, 5 Lanes Puzzle

- **category**: other
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2025-06-09
- **stage**: onsite-coding · onsite-bq
- **access**: Members
- **roles**: SWE
- **tags**: math-reasoning, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/horse-race-25
- **related discussions**: 1

### Statement

25 horses, 5 lanes per race, no timer — only relative order within a race is observable. What is the minimum number of races needed to identify the top 3 fastest horses?

---

## 38. Hash Collision: Causes, Resolution, Complexity

- **category**: other
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2024-11-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: cs-fundamentals, hashmap, verbal, complexity-analysis, easy
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/hash-collision-fundamentals
- **related discussions**: 1

### Statement

Verbal CS-fundamentals question: explain time complexity of hashmap operations, the cause of hash collisions, and the standard collision-resolution strategies. Common in VP-level phone screens as a fit-and-fundamentals warm-up.

---

## 39. Quant Strat Superday Probability + Fixed Income Drill

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-05-13
- **stage**: onsite-research · onsite-deep-dive
- **access**: Members
- **roles**: RS
- **tags**: probability, math-reasoning, domain-knowledge, verbal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/goldmansachs/quant-strat-superday-probability-fixed-income
- **related discussions**: 3

### Statement

Quant Strat Superday rounds combine probability brainteasers, short coding around stochastic processes, and fixed-income domain knowledge. The interview is verbal-heavy and rewards clear variable definitions and assumptions.

---
