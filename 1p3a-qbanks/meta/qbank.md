# Meta — 1point3acres Question Bank (61 curated questions)

Source: 1point3acres qbank (`company=meta`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/meta>

The site lists 539 entries for this company: **61 curated questions** (below), 478 OJ links, 0 external links. Only the curated 61 are captured here.

> Meta sits mid-transition between two interview loops in 2026. The legacy track still drives most candidates through CodeSignal-style OAs and tightly time-boxed coding rounds, while a new AI-Native loop layers in AI-assisted coding, AI-Enabled system design, and an AI-oriented behavioral round on top. Which track you land in depends on org and recruiter, not candidate preference.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. AI Coding — Maze Solver
2. AI Coding — Maximum Unique Character Subset
3. CodeSignal OA — Banking System (Progressive OOD)
4. Random Pick with Weight
5. Valid Palindrome II
6. Lowest Common Ancestor (BT / BST / N-ary)
7. Min Remove to Make Valid Parentheses
8. Find Peak Element
9. AI Coding — Friend Recommendation
10. Binary Tree Vertical Order / Right Side View
11. AI Coding — Card Game (Three Cards Summing to 15)
12. Kth Largest Element / Quick Select
13. Sum Root-to-Leaf Numbers
14. Shortest Path in a Binary Matrix (LC 1091)
15. Number of Islands API Variant
16. Tally Service
17. Randomized Container
18. Attention Implementation + Flash / Linear Attention Follow-ups
19. Shortest Unique Prefix
20. Valid Number
21. Best Time to Buy and Sell Stock
22. Group Anagrams
23. Remove Nth Node From End of List
24. Continuous Subarray Sum
25. Minimum Add to Make Parentheses Valid
26. Minimum Window Substring
27. AI Project Round (Take-Home-Style)
28. LRU Cache
29. CodeSignal OA — In-Memory Database
30. Range Sum of BST
31. Subarray Sum Equals K
32. Dinosaur Speed (Meta PE classic)
33. Minimum Round-Trip Flight Cost
34. Basic Calculator I / II
35. AI Coding — Shared Substring in String List
36. Walls and Gates / Max Area of Island
37. DE AI-Native Full-Stack Round
38. Pow(x, n)
39. Alien Dictionary
40. AI Coding — Compiler Optimization
41. Tree Diameter / Longest Path
42. Mouse and Cheese / Maze Rewards
43. Longest Consecutive Sequence
44. System Design — Facebook Search (Mini Elasticsearch)
45. MLSD — Harmful / Weapon-Sales Content Detection
46. MLSD — Nearby Place Recommendation
47. MLSD — Reels / Short Video Recommendation
48. MLSD — Ads Ranking
49. System Design — Instagram / News Feed
50. System Design — Online Auction / eBay
51. AI-Enabled System Design — Agent System
52. System Design — Trending Hashtags
53. MLSD — Image Copyright Violation Detection
54. System Design — Web Crawler / Google Flight
55. System Design — Ticketmaster / Ticketing
56. System Design — LeetCode / Online Judge + Contest Leaderboard
57. MLSD — Notification Ranking (Multi-source)
58. Traditional Behavioral — Meta
59. AI-Oriented Behavioral (AI BQ, NEW)
60. ML Foundations — Optimizers, Scaling Laws, K-Means and GMM
61. PE Troubleshooting — Web Server / Disk Full

---

## 1. AI Coding — Maze Solver

- **category**: coding
- **duration**: 60 min
- **frequency**: Very high
- **last asked**: 2026-09-04
- **stage**: onsite-coding · phone-screen
- **access**: Free preview
- **roles**: SWE, MLE, Infra Eng, RS
- **tags**: ai-coding, bfs, bitmask, simulation, grid, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-coding-maze-solver
- **related discussions**: 26

### Statement

Meta's signature AI-Enabled Coding prompt. 4-5 progressive sub-tasks on a grid-maze codebase, building from bug-fix to BFS to key/door bitmask state to a Q5 bomb-blast variant added in April.

---

## 2. AI Coding — Maximum Unique Character Subset

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-20
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng, RS
- **tags**: ai-coding, backtracking, bitmask, state-compression-dp, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-coding-max-unique-character-subset
- **related discussions**: 13

### Statement

Given a list of words, pick a subset whose combined characters are all unique and cover the maximum number of distinct letters. Progressive sub-tasks push backtracking to bitmask + state-compression DP on word lists in the 10k range.

---

## 3. CodeSignal OA — Banking System (Progressive OOD)

- **category**: coding
- **duration**: 90 min
- **frequency**: High
- **last asked**: 2026-05-26
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: codesignal, ood, state-machine, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/codesignal-oa-banking-system
- **related discussions**: 9

### Statement

CodeSignal 90-minute, 4-level Progressive OOD. Banking-system variant: create accounts, deposit, transfer; top-spenders ranking; scheduled payments; merge accounts with historical-balance queries.

---

## 4. Random Pick with Weight

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-08-24
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: array, binary-search, sampling, probability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/random-pick-with-weight
- **related discussions**: 4

### Statement

LeetCode 528. Sample an index proportional to its weight. Prefix-sum + binary-search is the LC answer; Meta's MLE follow-up pushes you to the Alias Method for O(1)-per-call sampling under millions of calls.

---

## 5. Valid Palindrome II

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-08-24
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: string, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/meta/valid-palindrome-ii
- **related discussions**: 6

### Statement

LeetCode 680 / 1216. Two-pointer with a single allowed deletion (or K deletions in the harder follow-up). Meta-canon phone-screen problem.

---

## 6. Lowest Common Ancestor (BT / BST / N-ary)

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-08-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: tree, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/lowest-common-ancestor
- **related discussions**: 4

### Statement

LeetCode 235 (BST), 236 (binary tree), and 1644 (with optional nodes). The N-ary variant is Meta's preferred follow-up.

---

## 7. Min Remove to Make Valid Parentheses

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-08-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: string, stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/min-remove-to-make-valid-parens
- **related discussions**: 4

### Statement

LeetCode 1249. Stack-based scan removing minimum parens to make the string valid. Multi-bracket-type extension is the canonical follow-up.

---

## 8. Find Peak Element

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-08-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: binary-search, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/find-peak-element
- **related discussions**: 4

### Statement

LeetCode 162. Binary search for any peak in an array where `nums[i] != nums[i+1]`. Edge-handling at boundaries is the differentiator.

---

## 9. AI Coding — Friend Recommendation

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: ai-coding, graph, oop-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-coding-friend-recommendation
- **related discussions**: 4

### Statement

Starter codebase exposes a `User` class with `friends` and `valid_recommend` helpers. Three sub-tasks build from logic-bug fix to AI-assisted random recommender to mutual-friends Top-K ranking.

---

## 10. Binary Tree Vertical Order / Right Side View

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-07-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: tree, bfs, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/binary-tree-vertical-order-right-side-view
- **related discussions**: 7

### Statement

LeetCode 314 (Vertical Order Traversal) and LeetCode 199 (Right Side View). Phone-screen staple, frequently asked as a paired combo (left view + right view) or as the vertical-order traversal with deterministic ordering.

---

## 11. AI Coding — Card Game (Three Cards Summing to 15)

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-06-03
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: ai-coding, 3sum, backtracking, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-coding-card-game-sum-fifteen
- **related discussions**: 7

### Statement

Card game with 36 cards (4 suits × values 1-9). Starting table has 16 random cards; each round pick 3 cards summing to 15 for 15 points, then refill until no more pairs are possible. Perfect score is 12 pairs × 15 = 180. Four progressive sub-tasks: debug a draw method → naive scorer → simulate to measure quality → optimize with DP.

---

## 12. Kth Largest Element / Quick Select

- **category**: coding
- **duration**: 40 min
- **frequency**: Medium
- **last asked**: 2026-05-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: array, heap, quickselect, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/kth-largest-quick-select
- **related discussions**: 4

### Statement

LeetCode 215 (Kth Largest Element in an Array). Phone-screen workhorse — heap or in-place quickselect, with the streaming-Kth variant as a typical follow-up.

---

## 13. Sum Root-to-Leaf Numbers

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, tree-traversal, dfs, recursion, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/sum-root-to-leaf-numbers
- **related discussions**: 1

### Statement

Solve the LC 129 prompt: treat each root-to-leaf sequence of digits as a number and return the sum of all such numbers. Follow-ups compare recursive and iterative traversal and ask what happens when the tree is very deep.

---

## 14. Shortest Path in a Binary Matrix (LC 1091)

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, shortest-path, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/shortest-path-binary-matrix
- **related discussions**: 1

### Statement

Starting from the LC 1091 binary-grid prompt, find a path from the top-left cell to the bottom-right and then print the route. The follow-up rejects carrying a full path in every queue entry and asks for a more space-efficient reconstruction.

---

## 15. Number of Islands API Variant

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: dfs, grid, set, api-design, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/number-of-islands-api-variant
- **related discussions**: 1

### Statement

Given a binary grid, implement `isSizeExist(int size)` to answer whether any island has exactly the requested area. Precompute complete component sizes once, then answer each query from the resulting set.

---

## 16. Tally Service

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-20
- **stage**: —
- **access**: Members
- **roles**: SWE
- **tags**: concurrency, sliding-window, metrics, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/tally-service-concurrent-windowed-counter
- **related discussions**: 1

### Statement

Implement a Tally Service with `bump(timestamp)` to record an event and `query(startTime, endTime)` to return the inclusive count for a time range. Follow-ups require thread-safe high-concurrency behavior, bounded memory through a sliding window or time buckets, and faster queries.

---

## 17. Randomized Container

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-19
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, sampling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/randomized-container
- **related discussions**: 1

### Statement

Implement a generic container with `insert(T element)` and `popRandom()`, where `popRandom` removes and returns one stored element with equal probability. The expected implementation keeps insertion and random removal at O(1).

---

## 18. Attention Implementation + Flash / Linear Attention Follow-ups

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-30
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, RS
- **tags**: attention, transformer, pytorch, numpy, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/attention-implementation-and-follow-ups
- **related discussions**: 1

### Statement

Implement the standard attention computation in code, analyze its time and space complexity, then answer oral follow-ups on FlashAttention and linear attention. This appears in a Meta Superintelligence / MLE-style loop alongside research-design and ML-foundations rounds.

---

## 19. Shortest Unique Prefix

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-18
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: trie, string, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/shortest-unique-prefix
- **related discussions**: 1

### Statement

For each word in a list, return the shortest prefix that uniquely distinguishes it from every other word, preserving the original input order. Solved with a Trie tracking per-node pass counts. The variant seen guaranteed that no word is a prefix of another, which drops one class of edge cases.

---

## 20. Valid Number

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-05-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, parsing, state-machine, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/lc-valid-number
- **related discussions**: 1

### Statement

Classic LC 65 — decide whether a string is a valid number (integer, decimal, with optional sign, optional `e`/`E` exponent). Surfaced as the warm-up in a recent E5 phone screen with light wording changes.

---

## 21. Best Time to Buy and Sell Stock

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-05-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, dp, greedy, easy
- **url**: https://www.1point3acres.com/interview/problems/company/meta/best-time-to-buy-sell-stock
- **related discussions**: 1

### Statement

LC 121 — maximum profit from a single buy / sell of a stock price array. Asked in a recent E5 onsite coding round, with a follow-up extending to multiple transactions.

---

## 22. Group Anagrams

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-05-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, string, sorting, easy
- **url**: https://www.1point3acres.com/interview/problems/company/meta/lc-group-anagrams
- **related discussions**: 1

### Statement

Classic LC 49 — bucket strings into anagram groups. Surfaced in a recent Sr SDE phone screen as the warm-up.

---

## 23. Remove Nth Node From End of List

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-05-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, two-pointer, easy
- **url**: https://www.1point3acres.com/interview/problems/company/meta/lc-remove-nth-from-end
- **related discussions**: 1

### Statement

Classic LC 19 — remove the Nth-from-end node of a singly-linked list in one pass. Asked as the second phone-screen problem in a Sr SDE loop.

---

## 24. Continuous Subarray Sum

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-04-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, hashmap, prefix-sum, math, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/continuous-subarray-sum
- **related discussions**: 1

### Statement

LC 523 — find a contiguous subarray (length ≥ 2) whose sum is a multiple of `k`. Asked as the second E5 phone-screen problem.

---

## 25. Minimum Add to Make Parentheses Valid

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2026-04-23
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, stack, greedy, easy
- **url**: https://www.1point3acres.com/interview/problems/company/meta/minimum-add-to-make-valid-parens
- **related discussions**: 1

### Statement

LC 921 — count the minimum insertions (of `(` or `)`) needed to make a parens string valid. Surfaced as the first E5 phone-screen problem.

---

## 26. Minimum Window Substring

- **category**: coding
- **duration**: 25 min
- **frequency**: Single report
- **last asked**: 2026-04-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: string, sliding-window, hashmap, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/minimum-window-substring
- **related discussions**: 1

### Statement

LC 76 — find the shortest substring of `s` that contains every character of `t` (counts respected). Asked as the second phone-screen problem in a recent SWE loop; missing the optimal-window approach gets the candidate dropped.

---

## 27. AI Project Round (Take-Home-Style)

- **category**: coding
- **duration**: 240 min
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: take-home · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: ai-coding, fullstack, take-home, moderation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-project-take-home-style-round
- **related discussions**: 1

### Statement

Pilot Meta round (Feb 2026): a multi-hour AI-assisted project session. 30-min intro to a mock full-stack app, ~1 hour solo implementation with any AI tools, ~1.5-hour interviewer walkthrough + live feature extension.

---

## 28. LRU Cache

- **category**: coding
- **duration**: 40 min
- **frequency**: Single report
- **last asked**: 2026-01-13
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: data-structure, hashmap, linked-list, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/lru-cache
- **related discussions**: 1

### Statement

LeetCode 146. O(1) get/put with capacity eviction. Doubly-linked list + hashmap is the canonical implementation; an MLE/SDE onsite warm-up.

---

## 29. CodeSignal OA — In-Memory Database

- **category**: coding
- **duration**: 90 min
- **frequency**: Low
- **last asked**: 2026-09-08
- **stage**: oa
- **access**: Members
- **roles**: SWE, MLE
- **tags**: codesignal, in-memory-database, ttl, versioning
- **url**: https://www.1point3acres.com/interview/problems/company/meta/codesignal-oa-in-memory-database
- **related discussions**: 2

### Statement

Implement an in-memory database with record operations, filtered scans, timestamps, and TTL. A later variant adds get_when(key, field, at) to retrieve a field’s historical value instead of restoring the entire database.

---

## 30. Range Sum of BST

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-08-24
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: tree, bst, dfs, easy
- **url**: https://www.1point3acres.com/interview/problems/company/meta/range-sum-of-bst
- **related discussions**: 3

### Statement

LeetCode 938. DFS through a BST summing nodes whose values fall in `[low, high]`. Pruning via BST property is the discussion point.

---

## 31. Subarray Sum Equals K

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-08-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: array, hashmap, prefix-sum, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/subarray-sum-equals-k
- **related discussions**: 3

### Statement

LeetCode 560. Count subarrays whose sum equals K. Prefix-sum + hashmap is the canonical solution; appears as the second half of Meta phone screens paired with a heavier first problem.

---

## 32. Dinosaur Speed (Meta PE classic)

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-15
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: Perf Eng, Infra Eng
- **tags**: array, binary-search, two-pointer, interval, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/dinosaur-speed-meta-pe
- **related discussions**: 3

### Statement

Meta Production Engineer classic. Given timestamps when dinosaurs cross sensor lines, pair them up so each pair represents the same dinosaur, and compute speeds. Time-window pairing + binary search.

---

## 33. Minimum Round-Trip Flight Cost

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-07-13
- **stage**: phone-screen
- **access**: Members
- **roles**: Perf Eng
- **tags**: array, optimization, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/minimum-round-trip-flight-cost
- **related discussions**: 2

### Statement

Given parallel arrays of departure and return flight prices, choose a departure day strictly before the return day to minimize total cost. Return both indices; when several pairs have the same minimum cost, prefer the earliest departure and the latest return. The round explicitly asks for an O(n^2) baseline followed by an O(n) optimization.

---

## 34. Basic Calculator I / II

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-06-24
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: string, stack, parsing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/basic-calculator-i-ii
- **related discussions**: 3

### Statement

LeetCode 224 / 227. Evaluate arithmetic expressions from a string. Stack-based evaluation with operator precedence; Meta-variant adds extra rules around eval order or unary operators.

---

## 35. AI Coding — Shared Substring in String List

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-06-09
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: string, trie, sorting, ai-coding, ai-allowed, complexity-analysis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-coding-substring-from-list
- **related discussions**: 3

### Statement

AI-Enabled Coding variant outside the Maze / Card-Game canon: given a list of strings, find a string that contains another from the list as a substring (or any pair sharing a substring relationship), then improve beyond brute force across 2-3 successive approaches.

---

## 36. Walls and Gates / Max Area of Island

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-04-21
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: grid, bfs, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/walls-and-gates-multi-source-bfs
- **related discussions**: 2

### Statement

LeetCode 286 (Walls and Gates) and LC 695 (Max Area of Island). Multi-source BFS for the distance-fill variant; matrix DFS for the area variant. Both surface as standard grid-traversal warm-ups at Meta.

---

## 37. DE AI-Native Full-Stack Round

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-17
- **stage**: tech-screen
- **access**: Members
- **roles**: DS
- **tags**: data-engineering, sql, data-modeling, ai-disallowed-section, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/de-ai-native-full-stack-round
- **related discussions**: 3

### Statement

Single 60-min Data Engineer round threading Business Case + Data Modeling + SQL + Python through one continuous scenario. AI is allowed in most sections but disallowed for SQL.

---

## 38. Pow(x, n)

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-04-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, MLE
- **tags**: math, recursion, easy
- **url**: https://www.1point3acres.com/interview/problems/company/meta/pow-x-n
- **related discussions**: 3

### Statement

LeetCode 50. Fast-exponentiation `x^n` with negative-exponent handling. Classic Meta phone-screen warm-up.

---

## 39. Alien Dictionary

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-04-13
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: graph, topological-sort, bfs, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/alien-dictionary
- **related discussions**: 2

### Statement

LeetCode 269. Given a list of words sorted in an unknown alphabet's lexicographic order, return a valid letter ordering. Build a precedence graph from adjacent word pairs, run topological sort.

---

## 40. AI Coding — Compiler Optimization

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-04-13
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: ai-coding, parsing, ambiguous-spec, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-coding-compiler-optimization
- **related discussions**: 2

### Statement

A new April-2026 AI Coding prompt. Implement `extract_time_and_mem_cost(instruction_file)` for a toy compiler IR. Catch: instruction costs are not given — they must be inferred from the unit-test assertions.

---

## 41. Tree Diameter / Longest Path

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-04-02
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: tree, dfs, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/tree-diameter
- **related discussions**: 2

### Statement

LeetCode 543 / 1245. Longest path between any two nodes in a tree. DFS returns the deepest descendant chain; at each node, the diameter candidate is the sum of the two longest child chains.

---

## 42. Mouse and Cheese / Maze Rewards

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-03-19
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: grid, dp, dfs, backtracking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/mouse-and-cheese-maze-rewards
- **related discussions**: 3

### Statement

Meta-classic DP-on-grid: maximize reward collected along a path from corner to corner. Two confirmed variants — a memo-DP version, and an API-exploration version where only `move()` / `canMove()` are exposed (no global view).

---

## 43. Longest Consecutive Sequence

- **category**: coding
- **duration**: 40 min
- **frequency**: Low
- **last asked**: 2026-03-17
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE, MLE
- **tags**: array, hashmap, set, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/longest-consecutive-sequence
- **related discussions**: 2

### Statement

LeetCode 128. Find the length of the longest consecutive-integer subsequence in an unsorted array. The trick is the O(n) hashset solution that only expands from sequence-starts.

---

## 44. System Design — Facebook Search (Mini Elasticsearch)

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-20
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: search, retrieval, ranking, scaling, sharding, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-facebook-search
- **related discussions**: 4

### Statement

Design Facebook post search — given query terms, return all matching posts. The exercise is essentially "build a mini Elasticsearch": inverted index, ranking, top-K per query, real-time ingestion.

---

## 45. MLSD — Harmful / Weapon-Sales Content Detection

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS
- **tags**: mlsd, integrity, weak-supervision, active-learning, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/mlsd-harmful-content-detection
- **related discussions**: 4

### Statement

Integrity-style MLSD: detect harmful content (weapon sales, harmful ads) at scale. Weak labels, active learning, policy alignment, and precision-recall trade-offs are the live discussion points.

---

## 46. MLSD — Nearby Place Recommendation

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-13
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS
- **tags**: mlsd, recommendation, geohash, location, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/mlsd-nearby-place-recommendation
- **related discussions**: 5

### Statement

Location-aware recommender for nearby places (Marketplace shops, FB Places). Geohash retrieval + mobile real-time constraints + context features distinguish it from generic feed MLSD.

---

## 47. MLSD — Reels / Short Video Recommendation

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-06-09
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS, RE
- **tags**: mlsd, recommendation, two-tower, mmoe, ab-testing, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/mlsd-reels-short-video-recommendation
- **related discussions**: 4

### Statement

Design the Reels short-video recommender. Four-stage retrieval → pre-rank → rank → re-rank scaffold is table stakes; differentiation is in features, metrics, and A/B-test design.

---

## 48. MLSD — Ads Ranking

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-05-30
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS
- **tags**: mlsd, ads, ranking, calibration, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/mlsd-ads-ranking
- **related discussions**: 4

### Statement

Design the Meta ads-ranking system. Feature engineering, model architecture, and calibration are the three deep-dive zones reported.

---

## 49. System Design — Instagram / News Feed

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-05-19
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: feed, fan-out, caching, ranking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-instagram-news-feed
- **related discussions**: 4

### Statement

Classic Meta SD prompt. Design Instagram or Facebook News Feed with personalized ranking. Push vs pull fan-out, hot-key handling, and a ranking layer are the canonical decision points.

---

## 50. System Design — Online Auction / eBay

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-05-08
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: pubsub, sse, concurrency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-online-auction
- **related discussions**: 4

### Statement

Auction with concurrent bidding, real-time price updates, and close-out semantics. Pub/sub + SSE-pushed bid notifications and queue-partitioning by auction-id are the canonical answer.

---

## 51. AI-Enabled System Design — Agent System

- **category**: system-design
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-05-05
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: llm-agent, react, tool-use, evaluation, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-ai-enabled-agent-system
- **related discussions**: 5

### Statement

New round introduced April 16, 2026 in the AI-Native full loop. Design an LLM-agent system for an automation task (auto-ticket-triage and code-review agent are confirmed prompts). ReAct-loop vs multi-agent decomposition + cost / memory / tool design are the live drill-downs.

---

## 52. System Design — Trending Hashtags

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-28
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: feed, ranking, streaming, metrics, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-trending-hashtags
- **related discussions**: 1

### Statement

Design a backend system that detects and serves trending hashtags from Facebook posts. The system must surface trends while the real-world event is happening, target about 1-minute latency, consider the last 24 hours of posts with recency weighting, reward popularity across the community, and distinguish genuinely novel spikes from long-running background chatter.

---

## 53. MLSD — Image Copyright Violation Detection

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-24
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS
- **tags**: mlsd, vlm, contrastive-learning, ann, adversarial, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/mlsd-image-copyright-detection
- **related discussions**: 2

### Statement

Detect whether a user-uploaded image violates copyright. VLM embedding + ANN lookup is the consensus retrieval approach; the interesting depth lives in adversarial robustness and partial-violation follow-ups.

---

## 54. System Design — Web Crawler / Google Flight

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: scaling, queue, scheduling, storage, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-web-crawler
- **related discussions**: 2

### Statement

Distributed web crawler at Meta scale, occasionally framed as Google Flight price aggregator. URL frontier, DAG scheduling, politeness rules, and storage are the standard deep-dive zones.

---

## 55. System Design — Ticketmaster / Ticketing

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-22
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: concurrency, distributed-lock, ttl, flash-sale, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-ticketmaster
- **related discussions**: 2

### Statement

Ticket-purchase system: seat inventory, hold/reserve flow, flash-sale concurrency. Reservation TTL + distributed locking is the central trade-off.

---

## 56. System Design — LeetCode / Online Judge + Contest Leaderboard

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: sandbox, queue, leaderboard, redis, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/system-design-leetcode-online-judge
- **related discussions**: 3

### Statement

Design an online code-judge with a contest leaderboard. Sandbox execution, judging pipeline, and real-time leaderboard updates are the three drill-down zones.

---

## 57. MLSD — Notification Ranking (Multi-source)

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-15
- **stage**: onsite-system-design
- **access**: Members
- **roles**: MLE, RS
- **tags**: mlsd, recommendation, ranking, calibration, notification, hard
- **url**: https://www.1point3acres.com/interview/problems/company/meta/mlsd-notification-ranking
- **related discussions**: 2

### Statement

Rank notifications from heterogeneous sources (friend activity, ads, system) under a unified objective. Multi-objective ranking + frequency-cap + value-normalization across sources are the deep-dive axes.

---

## 58. Traditional Behavioral — Meta

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-09-06
- **stage**: onsite-bq
- **access**: Members
- **roles**: SWE, MLE, Infra Eng, RS, EM
- **tags**: bq, leadership, ambiguity, conflict, star, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/traditional-behavioral-meta
- **related discussions**: 14

### Statement

Meta's classic 45-min BQ round. Heavy on project-deepening: ambiguity, conflict, most-proud project, role models, self-improvement. L6+ candidates face a markedly different bar focused on org-level scope and influence-without-authority.

---

## 59. AI-Oriented Behavioral (AI BQ, NEW)

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-06-09
- **stage**: onsite-bq · phone-screen
- **access**: Members
- **roles**: SWE, MLE, Infra Eng, RS
- **tags**: bq, ai-collaboration, star, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ai-oriented-behavioral
- **related discussions**: 9

### Statement

New behavioral round in the post-April-16 AI-Native loop. Standard STAR-shaped questions reweighted toward AI-tool collaboration, prompt judgment, and recognizing AI failure modes.

---

## 60. ML Foundations — Optimizers, Scaling Laws, K-Means and GMM

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-30
- **stage**: onsite-deep-dive · onsite-research
- **access**: Members
- **roles**: MLE, RS
- **tags**: ml-knowledge, optimization, clustering, llm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/ml-foundations-optimizers-scaling-law-clustering
- **related discussions**: 1

### Statement

A machine-learning foundations round covering optimizers, scaling laws, k-means clustering, and Gaussian Mixture Models. The leaked detail is topic-level rather than full question text, so treat this as a drill checklist for an oral fundamentals round.

---

## 61. PE Troubleshooting — Web Server / Disk Full

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-15
- **stage**: onsite-deep-dive · phone-screen
- **access**: Members
- **roles**: Perf Eng, Infra Eng
- **tags**: operating-systems, cs-fundamentals, debugging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/meta/pe-troubleshooting
- **related discussions**: 3

### Statement

Meta Production Engineer signature round. Interviewer states a production incident (web server down, disk full, customers failing) and you drive the diagnosis step by step — pure Linux/SRE skills, no code.

---
