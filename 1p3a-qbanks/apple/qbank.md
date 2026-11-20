# Apple — 1point3acres Question Bank (102 curated questions)

Source: 1point3acres qbank (`company=apple`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/apple>

The site lists 262 entries for this company: **102 curated questions** (below), 84 OJ links, 76 external links. Only the curated 102 are captured here.

> Apple interviews are unusually team-owned: the same company can run classic LeetCode screens, MLE case studies, hardware verification drills, project deep-dives, and system design loops with little advance disclosure. The strongest pattern is role fit: interviewers probe whether your prior work maps directly to the team's product surface, data domain, and engineering stack, while still expecting clean code, concise trade-off reasoning, and polished behavioral stories.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Sparse Matrix Multiplication (LC 311)
2. Number of Islands
3. BST to Sorted Circular Doubly Linked List (LC 426)
4. Copy List with Random Pointer (LC 138)
5. All Nodes Distance K in Binary Tree (LC 863)
6. Design Hit Counter
7. Top K Closest Pairs in a Sorted Array
8. Insert Interval (LC 57)
9. Merge Intervals
10. Alias Group Mapping
11. Subset Sum and All Subsets with Given Sum
12. Hotel Booking System OOD Coding
13. Binary Tree Vertical Order Traversal
14. Valid Sudoku
15. Top K Frequent Elements
16. Happy Number Service
17. Reconstruct Itinerary
18. Shortest Path in a Grid with Obstacle Removal
19. Rotate Image
20. Kubernetes Service Filter & Dependency Chain
21. Odd Even Linked List
22. Bag-of-Words Similarity Search from Scratch
23. Substring Pattern Matches
24. Valid Palindrome
25. Best Time to Buy and Sell Stock
26. Cheapest Flights Within K Stops
27. Search a 2D Matrix
28. Maximum Apples Before Rot
29. Flood Fill
30. Merge Two Sorted Lists
31. Bus Route Earliest Arrival
32. Koko Eating Bananas
33. Implement TF-IDF from Scratch
34. LRU Cache
35. Container With Most Water
36. Product of Array Except Self
37. Merge K Sorted Lists
38. Longest Mountain in Array
39. Trapping Rain Water
40. Query Image Similarity Search
41. Course Schedule
42. LFU Cache
43. Best Time to Buy and Sell Stock II
44. Longest Substring Without Repeating Characters
45. Bold Words in String
46. Implement Trie (Prefix Tree)
47. Intersection of Two Linked Lists
48. Kth Largest Element in an Array
49. Write a Debounce Function
50. Deck Shuffle and Draw OOD
51. UNet Debugging for Image MLE
52. Redundant Connection
53. Decode String
54. One Edit Distance
55. Dedupe Unsorted Array (Three Variants)
56. Valid Parentheses
57. Find Median (Oracle Variant)
58. Serialize and Deserialize Binary Tree
59. Zero Out Duplicates and Sort Without Native Sort
60. Lazy Binary Tree Inorder Iterator
61. Subarray Sum Equals K
62. Find K Pairs with Smallest Sums
63. Permutations
64. Airplane Seat Reservation API
65. Group Anagrams
66. Insert Delete GetRandom O(1)
67. Numbers with No Neighbors
68. Out of Boundary Paths
69. Number of Islands II
70. Online Token Processing, Embeddings, and Classification
71. Count Value Occurrences in a Binary Tree
72. Implement an Image Filter
73. Shortest Bridge (Distance Between Two Islands)
74. Failed Login Log Ranking
75. Design a HashMap
76. Weighted Random Load Balancer IP
77. Transformer Attention Mask and Heads Coding
78. Vectorized K-Means in NumPy or PyTorch
79. Object Storage / Dropbox / S3 Design
80. Cross-Device Wallpaper Synchronization
81. Large-Scale Media Transfer to ML Compute
82. Log Processing System
83. Design a RAG-Based Chatbot System
84. Design ChatGPT
85. Design App Store Search
86. Design a Chat Application
87. Design News Feed
88. Design TTL Cache
89. Apple News Search (No ML)
90. Design (key, value) Storage with Snapshots
91. Design a Concurrent Image Upload System
92. Distributed Task Scheduler
93. Distributed Rate Limiter with Lua Details
94. Ad Click Aggregator
95. Siri Grounded Response Generation
96. Behavioral Prioritization and Stakeholder Fit
97. ML Fundamentals & Model Debugging Drill
98. ML Systems Codebase Deep Dive
99. OS Fundamentals & Concurrency Drill
100. SystemVerilog DV Constraint & Architecture Loop
101. Accessories QA Benchmark and Regression Deep Dive
102. Frontend React Screen

---

## 1. Sparse Matrix Multiplication (LC 311)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, grid, hashmap
- **url**: https://www.1point3acres.com/interview/problems/company/apple/sparse-matrix-multiplication
- **related discussions**: 1

### Statement

Implement the canonical LC 311 sparse-matrix multiplication task, then explain how to store sparse matrices and avoid unnecessary work on zero entries.

---

## 2. Number of Islands

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, grid, dfs, bfs
- **url**: https://www.1point3acres.com/interview/problems/company/apple/number-of-islands
- **related discussions**: 1

### Statement

Given a 2D grid grid where '1' represents land and '0' represents water, count and return the number of islands.

---

## 3. BST to Sorted Circular Doubly Linked List (LC 426)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, bst, linked-list, tree-traversal, recursion, stack, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/bst-to-sorted-circular-doubly-linked-list
- **related discussions**: 1

### Statement

Convert a binary search tree in place into a sorted circular doubly linked list, using each node's left pointer as `prev` and right pointer as `next`. Handle an empty tree, connect the final head and tail, and explain or sketch an iterative version.

---

## 4. Copy List with Random Pointer (LC 138)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: linked-list, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/copy-list-with-random-pointer
- **related discussions**: 1

### Statement

Clone the linked list with random pointers from LC 138, then satisfy an O(1)-auxiliary-space follow-up without corrupting the original list. Pointer restoration and terminal-node handling receive explicit scrutiny.

---

## 5. All Nodes Distance K in Binary Tree (LC 863)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: tree, graph, bfs, dfs, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/all-nodes-distance-k-in-binary-tree
- **related discussions**: 1

### Statement

Given a binary tree, target node, and distance K, return every node exactly K edges from the target. Follow-ups cover duplicate values, K larger than the tree height, collecting all nodes within K, and avoiding an explicit parent map.

---

## 6. Design Hit Counter

- **category**: coding
- **duration**: 5 min
- **frequency**: Single report
- **last asked**: 2026-08-05
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: data-structure, queue, ring-buffer, sliding-window, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-hit-counter
- **related discussions**: 1

### Statement

Design a hit counter which counts the number of hits received in the past 5 minutes (that is, the past 300 seconds).

---

## 7. Top K Closest Pairs in a Sorted Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-08-05
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: array, top-k, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/top-k-closest-pairs-in-sorted-array
- **related discussions**: 1

### Statement

Given a sorted array, return the K pairs of values with the smallest absolute differences. The disclosed example uses [1, 2, 4, 7, 11, 16] and asks for the two closest pairs.

---

## 8. Insert Interval (LC 57)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, interval
- **url**: https://www.1point3acres.com/interview/problems/company/apple/insert-interval
- **related discussions**: 1

### Statement

Given a sorted list of pairwise non-overlapping intervals and one new interval, insert the new interval, merge any overlaps, and return the sorted non-overlapping result. The Apple round used the canonical LC 57 problem; an Apple-specific modification or follow-up was not disclosed.

---

## 9. Merge Intervals

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-07-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, interval, sorting
- **url**: https://www.1point3acres.com/interview/problems/company/apple/merge-intervals
- **related discussions**: 1

### Statement

Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.

---

## 10. Alias Group Mapping

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-27
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: union-find, graph, hashmap, sorting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/alias-group-mapping
- **related discussions**: 1

### Statement

Given pairs of words that are bidirectional aliases under a transitive relationship, return a map for every connected alias group. Each key is the lexicographically smallest word in its group, and each value is the group's complete word list in ascending lexicographic order.

---

## 11. Subset Sum and All Subsets with Given Sum

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, dp, backtracking, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/subset-sum-and-all-subsets
- **related discussions**: 1

### Statement

Two-part phone-screen prompt: given an array and a target, first decide whether any subset sums to the target using DP with space optimized to O(target) and O(n * target) time; then return all subsets that sum to the target via backtracking in O(2^n) time, with a space-complexity analysis.

---

## 12. Hotel Booking System OOD Coding

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: object-design, concurrency, backend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/hotel-booking-system-ood-coding
- **related discussions**: 1

### Statement

Design and implement a small hotel booking system with availability checks and room booking over date ranges, then discuss how to prevent conflicting bookings under concurrency.

---

## 13. Binary Tree Vertical Order Traversal

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-28
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: medium, tree, bfs, hashmap
- **url**: https://www.1point3acres.com/interview/problems/company/apple/binary-tree-vertical-order-traversal
- **related discussions**: 1

### Statement

Given the root of a binary tree, return the vertical order traversal of its nodes' values.

---

## 14. Valid Sudoku

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-06-24
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: grid, set, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/apple/valid-sudoku
- **related discussions**: 1

### Statement

Validate whether a partially filled Sudoku board is legal by checking every row, every column, and every 3x3 sub-grid for duplicate digits.

---

## 15. Top K Frequent Elements

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-06-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, hashmap, heap, top-k
- **url**: https://www.1point3acres.com/interview/problems/company/apple/top-k-frequent-elements
- **related discussions**: 1

### Statement

Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any order.

---

## 16. Happy Number Service

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: math, cycle-detection, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/apple/happy-number
- **related discussions**: 1

### Statement

Implement a service that decides whether a positive integer is a happy number, then scale it to billions of requests.

---

## 17. Reconstruct Itinerary

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-27
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, graph, dfs
- **url**: https://www.1point3acres.com/interview/problems/company/apple/reconstruct-itinerary

### Statement

You are given a list of airline tickets where tickets[i] = [fromi, toi] represent the departure and the arrival airports of one flight. Reconstruct the itinerary in order and return it.

---

## 18. Shortest Path in a Grid with Obstacle Removal

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-27
- **stage**: onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: bfs, grid, shortest-path, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/shortest-path-in-grid-with-obstacle-removal
- **related discussions**: 1

### Statement

BFS shortest path from a start cell to an end cell in a grid with blocked cells, with a follow-up that lets you convert blocked cells to open ones.

---

## 19. Rotate Image

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, grid, array
- **url**: https://www.1point3acres.com/interview/problems/company/apple/rotate-image
- **related discussions**: 1

### Statement

You are given an n x n 2D matrix representing an image. Rotate the image by 90 degrees clockwise, in place.

---

## 20. Kubernetes Service Filter & Dependency Chain

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-05-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: MLE, Infra Eng, SWE
- **tags**: graph, dfs, dependency-resolution, json, tool-use, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/kubernetes-service-filter-and-dependency-chain
- **related discussions**: 1

### Statement

Implement filters over Kubernetes service records, then compute dependency chains and query status along the chain. A final open-ended follow-up asks how to expose the code as an MCP server for agents while reducing noise.

---

## 21. Odd Even Linked List

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, linked-list
- **url**: https://www.1point3acres.com/interview/problems/company/apple/odd-even-linked-list

### Statement

Given the head of a singly linked list, group all nodes at odd indices together followed by the nodes at even indices, and return the reordered list.

---

## 22. Bag-of-Words Similarity Search from Scratch

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-05
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: medium, hashmap, retrieval, string-processing
- **url**: https://www.1point3acres.com/interview/problems/company/apple/bag-of-words-similarity-search-from-scratch

### Statement

This Apple Machine Learning Engineer phone screen asks you to build a tiny similarity search engine using the bag-of-words (BoW) representation, from scratch, without scikit-learn or any vectorizer library.

---

## 23. Substring Pattern Matches

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-05-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, string, string-processing
- **url**: https://www.1point3acres.com/interview/problems/company/apple/substring-pattern-matches

### Statement

Given a string s and a non-empty pattern pattern, return every starting index i in s where the substring s[i..i + pattern.length - 1] equals pattern.

---

## 24. Valid Palindrome

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: easy, algorithm
- **url**: https://www.1point3acres.com/interview/problems/company/apple/valid-palindrome

### Statement

Given a string s, return true if it is a palindrome, otherwise return false.

---

## 25. Best Time to Buy and Sell Stock

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-27
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: easy, array, greedy
- **url**: https://www.1point3acres.com/interview/problems/company/apple/best-time-to-buy-and-sell-stock

### Statement

You are given an integer array prices where prices[i] is the price of NeetCoin on the ith day.

---

## 26. Cheapest Flights Within K Stops

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hard, graph, shortest-path, bellman-ford
- **url**: https://www.1point3acres.com/interview/problems/company/apple/cheapest-flights-within-k-stops
- **related discussions**: 1

### Statement

There are n cities connected by some flights. You are given an array flights where flights[i] = [fromi, toi, pricei] indicates a flight from city fromi to city toi with cost pricei.

---

## 27. Search a 2D Matrix

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-25
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, binary-search, grid
- **url**: https://www.1point3acres.com/interview/problems/company/apple/search-a-2d-matrix

### Statement

You are given an m x n integer matrix matrix with the following two properties:.

---

## 28. Maximum Apples Before Rot

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-24
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: heap, greedy, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/maximum-apples-before-rot
- **related discussions**: 1

### Statement

Each day an apple tree grows `apples[i]` apples that rot after `days[i]`; eat at most one apple per day and maximize total apples eaten, including days after growth stops.

---

## 29. Flood Fill

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-20
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: easy, grid, dfs, bfs
- **url**: https://www.1point3acres.com/interview/problems/company/apple/flood-fill

### Statement

You are given an m x n integer grid image where image[r][c] is the pixel color at that position, and three integers sr, sc, and color.

---

## 30. Merge Two Sorted Lists

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-20
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: easy, linked-list, merge
- **url**: https://www.1point3acres.com/interview/problems/company/apple/merge-two-sorted-lists

### Statement

You are given the heads of two sorted linked lists list1 and list2.

---

## 31. Bus Route Earliest Arrival

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-04-18
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: graph, dijkstra, heap, binary-search, hard
- **url**: https://www.1point3acres.com/interview/problems/company/apple/bus-route-earliest-arrival
- **related discussions**: 1

### Statement

Given bus routes / schedules, travel times between stops, waiting times, origin, destination, and departure time, compute the earliest arrival time.

---

## 32. Koko Eating Bananas

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, binary-search, array
- **url**: https://www.1point3acres.com/interview/problems/company/apple/koko-eating-bananas

### Statement

Koko loves to eat bananas. There are n piles of bananas, the i\-th pile has piles[i] bananas. The guards have gone and will come back in h hours.

---

## 33. Implement TF-IDF from Scratch

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, hashmap, math, string-processing
- **url**: https://www.1point3acres.com/interview/problems/company/apple/implement-tf-idf-from-scratch

### Statement

This is an Apple Machine Learning Engineer phone screen Tech1 coding round.

---

## 34. LRU Cache

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, data-structure, hashmap, linked-list
- **url**: https://www.1point3acres.com/interview/problems/company/apple/lru-cache

### Statement

Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.

---

## 35. Container With Most Water

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-08
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, array, two-pointer
- **url**: https://www.1point3acres.com/interview/problems/company/apple/container-with-most-water

### Statement

You are given an integer array heights where heights[i] represents the height of the ithi^{th}ith bar.

---

## 36. Product of Array Except Self

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, array, prefix-sum
- **url**: https://www.1point3acres.com/interview/problems/company/apple/product-of-array-except-self

### Statement

Given an integer array nums, return an array output where output[i] is the product of all the elements of nums except nums[i].

---

## 37. Merge K Sorted Lists

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, heap, linked-list, merge
- **url**: https://www.1point3acres.com/interview/problems/company/apple/merge-k-sorted-lists

### Statement

You are given an array of k linked lists lists, where each list is sorted in ascending order.

---

## 38. Longest Mountain in Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, array, two-pointer
- **url**: https://www.1point3acres.com/interview/problems/company/apple/longest-mountain-in-array

### Statement

You may recall that an array arr is a mountain array if and only if:.

---

## 39. Trapping Rain Water

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-04-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hard, array, two-pointer, monotonic-stack
- **url**: https://www.1point3acres.com/interview/problems/company/apple/trapping-rain-water

### Statement

Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.

---

## 40. Query Image Similarity Search

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-30
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: medium, pytorch, retrieval, ann
- **url**: https://www.1point3acres.com/interview/problems/company/apple/query-image-similarity-search
- **related discussions**: 1

### Statement

This is a recent Apple onsite Round 4 ML Coding task for Machine Learning Engineer candidates.

---

## 41. Course Schedule

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-20
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, graph, topological-sort, dfs
- **url**: https://www.1point3acres.com/interview/problems/company/apple/course-schedule

### Statement

You are given an array prerequisites where prerequisites[i] = [a, b] indicates that you must take course b first if you want to take course a.

---

## 42. LFU Cache

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hard, data-structure, hashmap, linked-list
- **url**: https://www.1point3acres.com/interview/problems/company/apple/lfu-cache

### Statement

Design and implement a data structure for a Least Frequently Used (LFU) cache.

---

## 43. Best Time to Buy and Sell Stock II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: easy, array, greedy
- **url**: https://www.1point3acres.com/interview/problems/company/apple/best-time-to-buy-and-sell-stock-ii

### Statement

You are given an integer array prices where prices[i] is the price of a given stock on the ith day.

---

## 44. Longest Substring Without Repeating Characters

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, string, sliding-window, hashmap
- **url**: https://www.1point3acres.com/interview/problems/company/apple/longest-substring-without-repeating-characters

### Statement

Given a string s, find the length of the longest substring without duplicate characters.

---

## 45. Bold Words in String

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, string, interval, merge
- **url**: https://www.1point3acres.com/interview/problems/company/apple/bold-words-in-string

### Statement

Given an array of keywords words and a string s, make all appearances of all keywords words[i] in s bold. Any letters between <b> and </b> tags become bold.

---

## 46. Implement Trie (Prefix Tree)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, trie, data-structure
- **url**: https://www.1point3acres.com/interview/problems/company/apple/implement-trie-prefix-tree

### Statement

A prefix tree (also known as a trie) is a tree data structure used to efficiently store and retrieve keys in a set of strings. Some applications of this data structure include auto-complete and spell checker systems.

---

## 47. Intersection of Two Linked Lists

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-03-02
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: easy, linked-list, two-pointer
- **url**: https://www.1point3acres.com/interview/problems/company/apple/intersection-of-two-linked-lists

### Statement

Given the heads of two singly linked lists headA and headB, return the node at which the two lists intersect. If the two lists have no intersection, return null.

---

## 48. Kth Largest Element in an Array

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, heap, quickselect, top-k
- **url**: https://www.1point3acres.com/interview/problems/company/apple/kth-largest-element-in-an-array

### Statement

Given an integer array nums and an integer k, return the kth largest element in the array.

---

## 49. Write a Debounce Function

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-27
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, javascript, async, state-machine
- **url**: https://www.1point3acres.com/interview/problems/company/apple/write-a-debounce-function

### Statement

Implement a debounce(fn, wait) utility in JavaScript.

---

## 50. Deck Shuffle and Draw OOD

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-26
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: oop-design, sampling, array, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/deck-shuffle-and-draw-ood
- **related discussions**: 1

### Statement

Design `Card` and `Deck` classes that support shuffle and draw with correct probabilities. The prompt is OOD-shaped but the algorithmic core is random sampling without replacement.

---

## 51. UNet Debugging for Image MLE

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-25
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: pytorch, debugging, ml-knowledge, vlm, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/unet-debugging-for-image-mle
- **related discussions**: 1

### Statement

Image / video MLE phone screen asking ML concepts plus a PyTorch UNet debugging task: fix tensor shape, decoder boolean, final kernel size, and number of classes.

---

## 52. Redundant Connection

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, union-find, graph
- **url**: https://www.1point3acres.com/interview/problems/company/apple/redundant-connection

### Statement

In this problem, a tree is an undirected graph that is connected and has no cycles.

---

## 53. Decode String

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-16
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, stack, string, recursion
- **url**: https://www.1point3acres.com/interview/problems/company/apple/decode-string

### Statement

Given an encoded string, return its decoded string.

---

## 54. One Edit Distance

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-11
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, string, two-pointer
- **url**: https://www.1point3acres.com/interview/problems/company/apple/one-edit-distance

### Statement

Given two strings s and t, return true if they are both exactly one edit distance apart, otherwise return false.

---

## 55. Dedupe Unsorted Array (Three Variants)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-09
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, dedup, array, hashmap
- **url**: https://www.1point3acres.com/interview/problems/company/apple/dedupe-unsorted-array-three-variants

### Statement

You are given a method that takes in an array of unsorted integers and you must return the deduped array. The interviewer runs it as three progressively tighter variants, using each one to gauge a different skill:.

---

## 56. Valid Parentheses

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-07
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: easy, stack, string
- **url**: https://www.1point3acres.com/interview/problems/company/apple/valid-parentheses

### Statement

You are given a string s consisting of the following characters: '(', ')', '{', '}', '[' and ']'.

---

## 57. Find Median (Oracle Variant)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hard, binary-search, algorithm-design
- **url**: https://www.1point3acres.com/interview/problems/company/apple/find-median-oracle-variant
- **related discussions**: 1

### Statement

You are asked to find the median of an unseen array nums of n distinct or repeating integers. You cannot read the array directly — instead, you are given three oracle operations:.

---

## 58. Serialize and Deserialize Binary Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-02-04
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hard, tree, dfs, bfs
- **url**: https://www.1point3acres.com/interview/problems/company/apple/serialize-and-deserialize-binary-tree

### Statement

Implement an algorithm to serialize and deserialize a binary tree.

---

## 59. Zero Out Duplicates and Sort Without Native Sort

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, dedup, sorting, hashmap, easy
- **url**: https://www.1point3acres.com/interview/problems/company/apple/zero-out-duplicates-and-sort-without-native-sort
- **related discussions**: 1

### Statement

Given an unsorted integer array of about 75 elements with duplicates, zero out duplicate values and sort the data without using native sort, quicksort, or bubble sort.

---

## 60. Lazy Binary Tree Inorder Iterator

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: tree, iterator, stack, tree-traversal, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/lazy-binary-tree-inorder-iterator
- **related discussions**: 1

### Statement

Design an iterator over a binary tree that returns nodes in inorder sequence without precomputing all values. The follow-up asks about multiple iterators and thread safety.

---

## 61. Subarray Sum Equals K

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-29
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: array, prefix-sum, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/subarray-sum-equals-k
- **related discussions**: 1

### Statement

Given an integer array and target `k`, count all contiguous subarrays whose sum equals `k`. The follow-up checks whether negative numbers break sliding-window assumptions.

---

## 62. Find K Pairs with Smallest Sums

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, heap, top-k
- **url**: https://www.1point3acres.com/interview/problems/company/apple/find-k-pairs-with-smallest-sums

### Statement

You are given two integer arrays nums1 and nums2 sorted in non-decreasing order, and an integer k.

---

## 63. Permutations

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-17
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, backtracking, recursion
- **url**: https://www.1point3acres.com/interview/problems/company/apple/permutations

### Statement

Given an array nums of distinct integers, return all possible permutations. You can return the answer in any order.

---

## 64. Airplane Seat Reservation API

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-01-06
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: caching, array, parsing, design-implementation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/airplane-seat-reservation-api
- **related discussions**: 1

### Statement

Implement a C-style airplane seating API with create, print, free, and reserve functions. The reserve call parses seats like `4B` and may choose an alternate seat when movement is allowed.

---

## 65. Group Anagrams

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-01-01
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, hashmap, string, sorting
- **url**: https://www.1point3acres.com/interview/problems/company/apple/group-anagrams

### Statement

Given an array of strings strs, group all anagrams together into sublists. You may return the output in any order.

---

## 66. Insert Delete GetRandom O(1)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-22
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, data-structure, hashmap, sampling
- **url**: https://www.1point3acres.com/interview/problems/company/apple/insert-delete-getrandom-o-1

### Statement

Implement the RandomizedSet class:.

---

## 67. Numbers with No Neighbors

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, array, hashmap
- **url**: https://www.1point3acres.com/interview/problems/company/apple/numbers-with-no-neighbors

### Statement

Given an integer array nums, return every value n in nums that satisfies both conditions:.

---

## 68. Out of Boundary Paths

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-21
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: hard, dp, grid
- **url**: https://www.1point3acres.com/interview/problems/company/apple/out-of-boundary-paths

### Statement

There is an m x n grid with a ball. The ball starts at [startRow, startColumn]. On each move, the ball can move to an adjacent cell in one of four directions.

---

## 69. Number of Islands II

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-14
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, union-find, grid
- **url**: https://www.1point3acres.com/interview/problems/company/apple/number-of-islands-ii

### Statement

You are given an empty m x n grid initially filled with water. We may perform an addLand operation where positions[i] = [ri, ci] turns the cell (ri, ci) into land.

---

## 70. Online Token Processing, Embeddings, and Classification

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-11
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: medium, streaming, classification, ml-knowledge
- **url**: https://www.1point3acres.com/interview/problems/company/apple/online-token-processing-embeddings-and-classification

### Statement

This is an Apple Machine Learning Engineer phone screen (Round 2). The interviewer gives you a stream of labeled text records and asks you to build an end-to-end pipeline that:.

---

## 71. Count Value Occurrences in a Binary Tree

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-12-10
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, tree, tree-traversal
- **url**: https://www.1point3acres.com/interview/problems/company/apple/count-value-occurrences-in-a-binary-tree

### Statement

You are given the root of a binary tree (not a BST, just a generic binary tree) and a target value x. Count how many nodes in the tree have node.val == x.

---

## 72. Implement an Image Filter

- **category**: coding
- **duration**: 20 min
- **frequency**: Single report
- **last asked**: 2025-12-02
- **stage**: phone-screen
- **access**: Members
- **roles**: MLE
- **tags**: medium, numpy, pillow, grid
- **url**: https://www.1point3acres.com/interview/problems/company/apple/implement-an-image-filter
- **related discussions**: 1

### Statement

This is a recent Apple onsite ML Coding round (Round 3, 20 minutes of coding after 20 minutes of project plus ML fundamentals).

---

## 73. Shortest Bridge (Distance Between Two Islands)

- **category**: coding
- **frequency**: Single report
- **last asked**: 2025-11-28
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: medium, grid, bfs, dfs
- **url**: https://www.1point3acres.com/interview/problems/company/apple/shortest-bridge-distance-between-two-islands
- **related discussions**: 1

### Statement

You are given a binary grid containing exactly two islands, where 1 is land and 0 is water.

---

## 74. Failed Login Log Ranking

- **category**: coding
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-20
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: parsing, hashmap, sorting, top-k, easy
- **url**: https://www.1point3acres.com/interview/problems/company/apple/failed-login-log-ranking
- **related discussions**: 1

### Statement

Parse system logs, extract failed login attempts, group by IP and machine, and sort by failed-attempt count.

---

## 75. Design a HashMap

- **category**: coding
- **frequency**: Low
- **last asked**: 2026-08-21
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-a-hashmap
- **related discussions**: 2

### Statement

Design and implement a HashMap<K, V> from scratch, without using the language's built-in hash map. Support the standard API:.

---

## 76. Weighted Random Load Balancer IP

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-07-13
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: sampling, prefix-sum, binary-search, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/weighted-random-load-balancer-ip
- **related discussions**: 2

### Statement

Given a list of load-balancer IPs, return a random IP uniformly; then extend to weighted probability where higher-weight IPs are chosen more often.

---

## 77. Transformer Attention Mask and Heads Coding

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-16
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: transformer, attention, pytorch, debugging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/transformer-attention-mask-and-heads-coding
- **related discussions**: 3

### Statement

Apple AIML / MLE rounds can ask live ML coding around Transformer internals: implement or debug attention, add heads, add masks, and reason about tensor shapes.

---

## 78. Vectorized K-Means in NumPy or PyTorch

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-03-25
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: MLE
- **tags**: numpy, pytorch, classification, ml-knowledge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/vectorized-k-means-in-numpy-or-pytorch
- **related discussions**: 2

### Statement

Implement K-means with tensor operations rather than Python loops. Interviewers care about broadcasting, `unsqueeze`, `keepdim`, centroid updates, and whether the implementation stays efficient.

---

## 79. Object Storage / Dropbox / S3 Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-05
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: storage, filesystem, distributed-systems, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/object-storage-dropbox-s3-design
- **related discussions**: 4

### Statement

A recurring storage-system design family covering Dropbox-style file sync, S3-style object storage, and storage-team fundamentals.

---

## 80. Cross-Device Wallpaper Synchronization

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-16
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: caching, retry, versioning, async, concurrency, bandwidth, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/cross-device-wallpaper-synchronization
- **related discussions**: 1

### Statement

Design wallpaper exchange and synchronization across a user's devices. Cover opportunistic prefetching, resumable downloads, version detection, cache priority, concurrent updates, retry behavior, and the split between background work and main-thread UI commits.

---

## 81. Large-Scale Media Transfer to ML Compute

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-08-08
- **stage**: tech-screen
- **access**: Members
- **roles**: SWE, MLE, Infra Eng
- **tags**: storage, data-engineering, bandwidth, ml-infra, scaling, hard
- **url**: https://www.1point3acres.com/interview/problems/company/apple/large-scale-media-transfer-to-ml-compute
- **related discussions**: 1

### Statement

Design a one-time transfer of 200 TB of images and video from an on-premises source to a cloud GPU training cluster within a few days. Compare physical and network migration, use an object-store landing zone, preserve integrity and security, and extend the design into a recurring ingestion and GPU input pipeline.

---

## 82. Log Processing System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-06-28
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: streaming, data-engineering, parsing
- **url**: https://www.1point3acres.com/interview/problems/company/apple/log-processing-system
- **related discussions**: 1

### Statement

Design a log processing system that ingests a continuous stream of application logs from a large service fleet and counts errors in real time. A dashboard and alerting layer sits on top, so error counts must be accurate and available with low latency.

---

## 83. Design a RAG-Based Chatbot System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-06-10
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: llm, rag, retrieval, mlsd
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-a-rag-based-chatbot-system
- **related discussions**: 1

### Statement

Design an intelligent chatbot system that uses Retrieval-Augmented Generation (RAG) to answer user queries.

---

## 84. Design ChatGPT

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-05-26
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: llm, rag, retrieval, mlsd
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-chatgpt

### Statement

Design a conversational AI system like ChatGPT: users send text prompts and receive AI-generated responses streamed back token-by-token, with persistent conversation history across sessions.

---

## 85. Design App Store Search

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-04-08
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: search, ranking, retrieval
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-app-store-search

### Statement

Design the search system for the Apple App Store.

---

## 86. Design a Chat Application

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-02-03
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: chat, messaging, websocket, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-a-chat-application

### Statement

Design a chat application that supports one-on-one and group messaging with user registration, real-time message delivery, and offline support.

---

## 87. Design News Feed

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-01-14
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: feed, fan-out, ranking, system-design
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-news-feed

### Statement

Design a news feed system: users follow other users and see a reverse-chronological (or ranked) feed of posts from the people they follow, with support for posting, likes, and real-time-ish delivery at very large scale.

---

## 88. Design TTL Cache

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2026-01-02
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE
- **tags**: system-design, backend
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-ttl-cache

### Statement

Design a cache where every entry has a time-to-live (TTL): it is readable for a fixed window after insertion, then disappears.

---

## 89. Apple News Search (No ML)

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-12-18
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: search, ranking, retrieval
- **url**: https://www.1point3acres.com/interview/problems/company/apple/apple-news-search-no-ml

### Statement

Design the first version of search for Apple News. A user types a free-form query (e.g. "tesla earnings", "wildfires california") and expects back a ranked list of news articles drawn from tens of thousands of publishers.

---

## 90. Design (key, value) Storage with Snapshots

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-12-09
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: MLE
- **tags**: llm, rag, retrieval, mlsd
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-key-value-storage-with-snapshots

### Statement

Design a key-value store that supports point-in-time snapshots.

---

## 91. Design a Concurrent Image Upload System

- **category**: system-design
- **frequency**: Single report
- **last asked**: 2025-11-30
- **stage**: oa
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: concurrency, storage, backend
- **url**: https://www.1point3acres.com/interview/problems/company/apple/design-a-concurrent-image-upload-system

### Statement

Design a system like Craigslist that accepts a list of images and stores them concurrently, updating the user about the status of each upload as soon as possible.

---

## 92. Distributed Task Scheduler

- **category**: system-design
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2025-11-21
- **stage**: onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: scheduling, queueing, distributed-systems, dependency-resolution, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/distributed-task-scheduler
- **related discussions**: 1

### Statement

Storage infra onsite design for a distributed task scheduler, with follow-ups on queue structure, task representation, and scaling.

---

## 93. Distributed Rate Limiter with Lua Details

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-08-05
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: rate-limiting, redis, distributed-systems, scaling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/distributed-rate-limiter-with-lua
- **related discussions**: 2

### Statement

Storage infra onsite system design asking for a distributed rate limiter, with interviewer emphasis on implementation detail such as Redis Lua scripting.

---

## 94. Ad Click Aggregator

- **category**: system-design
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-18
- **stage**: phone-screen · onsite-system-design
- **access**: Members
- **roles**: SWE, Infra Eng, MLE
- **tags**: ads, data-engineering, streaming, kafka, idempotency, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/ad-click-aggregator
- **related discussions**: 2

### Statement

Design an ads click aggregation pipeline. The Apple version emphasizes multiple data sources such as web, mobile, and TV, plus click-count accuracy.

---

## 95. Siri Grounded Response Generation

- **category**: system-design
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-28
- **stage**: phone-screen · onsite-system-design
- **access**: Free preview
- **roles**: MLE
- **tags**: llm, rag, tool-use, evaluation, prompting, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/siri-grounded-response-generation
- **related discussions**: 2

### Statement

Design or case-study a Siri response-generation system where answers must stay grounded in tool results, avoid hallucination, fit a voice-chat length budget, and remain evaluable as context grows.

---

## 96. Behavioral Prioritization and Stakeholder Fit

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-09-01
- **stage**: phone-screen · onsite-bq · onsite-deep-dive
- **access**: Members
- **roles**: All
- **tags**: behavioral, bq, star, ambiguity, impact, collaboration, conflict, fit
- **url**: https://www.1point3acres.com/interview/problems/company/apple/behavioral-prioritization-and-stakeholder-fit
- **related discussions**: 13

### Statement

Common Apple behavioral / HM prompts: end-to-end ownership, difficult debugging, a mistaken technical decision, disagreement and collaboration, why Apple, work style, prioritization, stakeholder blockers, and measurable project impact. Debugging stories may be probed down to the logs inspected, how the search space narrowed, and why the first hypothesis failed.

---

## 97. ML Fundamentals & Model Debugging Drill

- **category**: other
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-09-01
- **stage**: phone-screen · onsite-deep-dive · onsite-system-design · final-round
- **access**: Members
- **roles**: MLE, SWE
- **tags**: ml-knowledge, llm, transformer, evaluation, ads, debugging, ab-testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/ml-fundamentals-and-model-debugging
- **related discussions**: 7

### Statement

Apple MLE loops repeatedly include verbal drills on model choice, evaluation, data processing, LLM concepts, ranking / ads models, GPU OOM, and production metric drops.

---

## 98. ML Systems Codebase Deep Dive

- **category**: other
- **duration**: 30 min
- **frequency**: Single report
- **last asked**: 2026-07-01
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: MLE
- **tags**: deep-dive, agents, agentic-workflow, rag, retrieval, database, backend, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/ml-systems-codebase-deep-dive
- **related discussions**: 1

### Statement

A no-coding HM technical screen for an ML Systems Engineer role that turns into a deep review of one recent project and its codebase. The interviewer probes architecture, deployment, data collection, cross-evaluation, database choices, agentic-system rationale, FastAPI / LangGraph state handling, PDF embedding, similarity search, and paper retrieval.

---

## 99. OS Fundamentals & Concurrency Drill

- **category**: other
- **frequency**: Single report
- **last asked**: 2026-06-15
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE, Infra Eng
- **tags**: operating-systems, concurrency, race-condition, threading, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/os-fundamentals-and-concurrency
- **related discussions**: 1

### Statement

Apple Core OS / low-level teams open with a hiring-manager round that stays almost entirely on OS fundamentals: process vs thread, CPU scheduling, synchronization primitives (mutex / semaphore / condition variable), race conditions and deadlock, escalating to multi-core synchronization.

---

## 100. SystemVerilog DV Constraint & Architecture Loop

- **category**: other
- **duration**: 45 min
- **frequency**: Single report
- **last asked**: 2026-03-06
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: SWE
- **tags**: cs-fundamentals, concurrency, probability, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/systemverilog-dv-constraint-and-architecture-loop
- **related discussions**: 1

### Statement

Hardware DV intern loop covering SystemVerilog constraints, UVM-style testbench components, edge detectors, assertions, virtual functions, semaphores / mailboxes, architecture fundamentals, and Python sorting.

---

## 101. Accessories QA Benchmark and Regression Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-09-09
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: SWE
- **tags**: deep-dive, presentation, optimization, metrics
- **url**: https://www.1point3acres.com/interview/problems/company/apple/accessories-qa-benchmark-and-regression-deep-dive
- **related discussions**: 2

### Statement

Present prior QA projects and explain regression reliability, benchmark evaluation, HPC resource use, and runtime improvements. Follow-ups probe whether pass/fail results capture failure severity and who decides whether performance deviations are acceptable.

---

## 102. Frontend React Screen

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-06-24
- **stage**: phone-screen
- **access**: Members
- **roles**: SWE
- **tags**: frontend, react, interval, cs-fundamentals, medium
- **url**: https://www.1point3acres.com/interview/problems/company/apple/frontend-react-screen
- **related discussions**: 2

### Statement

Frontend phone screen covering React fundamentals, CSS concepts, UI troubleshooting, component structure, and sometimes a coding warm-up such as merge intervals or debounce.

---
