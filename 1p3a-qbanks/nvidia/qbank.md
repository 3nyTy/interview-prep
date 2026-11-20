# NVIDIA — 1point3acres Question Bank (20 curated questions)

Source: 1point3acres qbank (`company=nvidia`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/nvidia>

The site lists 68 entries for this company: **20 curated questions** (below), 48 OJ links, 0 external links. Only the curated 20 are captured here.

> NVIDIA interviews are highly team-specific and much less LeetCode-bank-driven than most large tech loops. System Software, Linux, Tegra, DGX AI infra, and Deep Learning roles lean toward engineering scenarios: C/C++ debugging, graph validation, GPU / inference fundamentals, small utility coding, and project deep dives; some design and product-facing AI roles add system-design or technical-marketing discussions.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Reaching Points
2. Count Substrings Without Repeating Characters
3. FP32 Tensor to Int8 Quantization
4. Computation / Dependency Graph Validation and Pruning
5. C++ Project Debugging with AI Tools
6. Systems Utility Coding: Temperature Spike, Logs, Brackets
7. String Encoding / Decoding Variants
8. Disk Space Manager / KV Store with setAll
9. 2-D Matrix Transpose with Memory-Layout Discussion
10. ML Coding: 2-D Convolution, Decaying Attention, Training Loop
11. Telemetry Collector and GPU Utilization Dashboard
12. LLM SQL Chatbot Access Control and Query Safety
13. HPC Host Control Plane and Job Coordination
14. Real-Time Fraud Detection System
15. Rate Limiter Algorithm Design
16. Culture Fit, Motivation, and Learning Habits
17. GPU and Inference Systems Fundamentals
18. Data Platform, Pipeline, and ML Operations Fundamentals
19. Hardware-Adjacent Project Deep Dive
20. Deep Learning Fundamentals: Optimization, Drift, Calibration

---

## 1. Reaching Points

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: infra-eng
- **tags**: math, greedy, hard
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/reaching-points
- **related discussions**: 1

### Statement

Given positive start and target coordinates, determine whether repeated transformations from (x, y) to either (x + y, y) or (x, x + y) can reach the target. The interview used LeetCode 780 without a s...

---

## 2. Count Substrings Without Repeating Characters

- **category**: coding
- **frequency**: Single report
- **last asked**: 2026-07-14
- **stage**: onsite-coding
- **access**: Members
- **roles**: infra-eng
- **tags**: string, sliding-window, easy
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/count-substrings-without-repeating-characters
- **related discussions**: 1

### Statement

Given a string, count its non-empty contiguous substrings whose characters are all distinct. The interview used LeetCode 2743 without a stated modification.

---

## 3. FP32 Tensor to Int8 Quantization

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-12
- **stage**: phone-screen
- **access**: Free preview
- **roles**: SWE, MLE, Perf Eng
- **tags**: numpy, inference, numerical-stability, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/fp32-to-int8-quantization
- **related discussions**: 1

### Statement

Given an FP32 tensor and a floating-point scale, convert the tensor to INT8. Follow-ups add asymmetric quantization with zero point and ask how scale / zero point should change when the input distribution is not symmetric.

---

## 4. Computation / Dependency Graph Validation and Pruning

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, mle, infra-eng
- **tags**: graph, topological-sort, dfs, inference, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/graph-validation-and-pruning
- **related discussions**: 1

### Statement

Implement graph operations such as inserting nodes, configuring dependencies, validating structural requirements, detecting cycles, and pruning unneeded branches from a neural-network computation grap...

---

## 5. C++ Project Debugging with AI Tools

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-12
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: debugging, concurrency, ai-tools, hard
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/cpp-debugging-with-ai-tools
- **related discussions**: 1

### Statement

A larger C++ project is split into stages, each containing a logical bug, memory bug, or race condition. The candidate runs the program, observes output, debugs, and may use GPT / Gemini under screen ...

---

## 6. Systems Utility Coding: Temperature Spike, Logs, Brackets

- **category**: coding
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-02-05
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: heap, parsing, testing, easy
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/systems-utility-coding
- **related discussions**: 1

### Statement

System Software rounds use short utility tasks: detect a temperature spike from `(timestamp, cpu_temp)` pairs, validate brackets in an expression, aggregate logs by status code and latency, or minimiz...

---

## 7. String Encoding / Decoding Variants

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-26
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: string-processing, parsing, compression, easy
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/string-encoding-decoding
- **related discussions**: 3

### Statement

Two string parsing variants appear: encode/decode a list of strings using length prefixes and delimiters, and decode a compact alphabet-count expression where `#` forms two-digit letters and `(k)` rep...

---

## 8. Disk Space Manager / KV Store with setAll

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-04-15
- **stage**: phone-screen
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: oop-design, caching, hashmap, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/storage-manager-and-kv-setall
- **related discussions**: 2

### Statement

Implement storage-like APIs: a disk-space manager with `put(datasetId, size)` and `get(id)` under capacity / eviction constraints, or a KV store supporting `set`, `get`, and O(1) `setAll`.

---

## 9. 2-D Matrix Transpose with Memory-Layout Discussion

- **category**: coding
- **duration**: 120 min
- **frequency**: Low
- **last asked**: 2026-04-15
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: swe, infra-eng, perf-eng
- **tags**: grid, performance-modeling, memory-bounded, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/matrix-transpose-memory-layout
- **related discussions**: 2

### Statement

Transpose an `n x n` or general 2-D matrix, then discuss row-major vs column-major layout, cache locality, in-place constraints, and large-matrix performance. This appears in Linux / System Software-s...

---

## 10. ML Coding: 2-D Convolution, Decaying Attention, Training Loop

- **category**: coding
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2025-12-10
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: mle, swe, rs
- **tags**: numpy, pytorch, attention, gradient-descent, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/ml-coding-conv-attention-training-loop
- **related discussions**: 2

### Statement

Deep Learning and AI product roles ask small ML implementation tasks: write 2-D convolution in NumPy, implement decaying attention `softmax(q @ k + b) @ v`, and fill a standard training loop with a pr...

---

## 11. Telemetry Collector and GPU Utilization Dashboard

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: ds, infra-eng
- **tags**: data-engineering, metrics, analytics-dashboard, gpu, storage, data-modeling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/telemetry-collector-and-gpu-utilization-dashboard
- **related discussions**: 1

### Statement

Design a data collector and analytics dashboard for GPU-cluster telemetry. The system must ingest both 30-second and 1-second resolution metrics, show 3-month utilization trends, and allow drill-down ...

---

## 12. LLM SQL Chatbot Access Control and Query Safety

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-03
- **stage**: onsite-system-design
- **access**: Members
- **roles**: ds, infra-eng
- **tags**: llm, sql, access-control, query-optimization, security, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/llm-sql-chatbot-access-control-and-query-safety
- **related discussions**: 1

### Statement

Design an LLM chatbot for SQL query generation over internal data. The discussion focuses on ensuring users can only query data they are allowed to access and preventing hallucinated or expensive quer...

---

## 13. HPC Host Control Plane and Job Coordination

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-06-30
- **stage**: phone-screen
- **access**: Members
- **roles**: infra-eng, perf-eng
- **tags**: distributed-systems, presence, job-system, redis, concurrency, database, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/hpc-host-control-plane-and-job-coordination
- **related discussions**: 1

### Statement

Design a central service that tracks many hosts, knows their current state, and coordinates job assignment. The prompt explicitly focuses on communication between hosts and the central service, databa...

---

## 14. Real-Time Fraud Detection System

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-03-25
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe, mle, infra-eng
- **tags**: system-design, mlsd, scaling, ml-infra, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/real-time-fraud-detection-system
- **related discussions**: 1

### Statement

Design a real-time fraud evaluation system for millions of daily transactions, 10,000+ requests per second, 50 ms decision latency, and zero-downtime ML model updates. Black Friday-style bursts and tr...

---

## 15. Rate Limiter Algorithm Design

- **category**: system-design
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2025-09-23
- **stage**: onsite-system-design
- **access**: Members
- **roles**: swe, infra-eng
- **tags**: system-design, rate-limiting, distributed-systems, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/rate-limiter-algorithm-design
- **related discussions**: 1

### Statement

A system-design round focused less on drawing a distributed architecture and more on rate-limit algorithms: token bucket, leaky bucket, fixed window, sliding window, state storage, and trade-offs.

---

## 16. Culture Fit, Motivation, and Learning Habits

- **category**: behavioral
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-13
- **stage**: onsite-bq
- **access**: Members
- **roles**: all-roles
- **tags**: behavioral, culture-fit, values, why-company, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/culture-fit-motivation-and-learning-habits
- **related discussions**: 2

### Statement

A culture-fit or hiring-manager round can probe integrity, motivation and demotivation, recent technical habits, the hardest problem the candidate has solved, and how they handle disagreement with a c...

---

## 17. GPU and Inference Systems Fundamentals

- **category**: other
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-01
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: mle, swe, perf-eng, rs
- **tags**: gpu, kernel, inference, matmul, transformer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/gpu-and-inference-systems-fundamentals
- **related discussions**: 4

### Statement

DevTech, Deep Learning, and AI Software rounds ask GPU / inference fundamentals: Amdahl's Law, HBM vs SRAM, matmul complexity and tiling, CPU vs GPU matmul, tensor / pipeline parallelism, transformer ...

---

## 18. Data Platform, Pipeline, and ML Operations Fundamentals

- **category**: other
- **duration**: 60 min
- **frequency**: Single report
- **last asked**: 2026-07-03
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: ds, infra-eng
- **tags**: data-engineering, etl, streaming, metrics, debugging, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/data-platform-pipeline-and-ml-operations-fundamentals
- **related discussions**: 1

### Statement

Senior data-engineering candidates can face a one-hour technical assessment covering a small coding warmup plus production data-platform operations: Kinesis, Prometheus / Grafana, idempotency, Spark p...

---

## 19. Hardware-Adjacent Project Deep Dive

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-07-03
- **stage**: phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: infra-eng, perf-eng, ds
- **tags**: gpu, data-engineering, metrics, infra, domain-knowledge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/hardware-adjacent-project-deep-dive
- **related discussions**: 2

### Statement

Project deep-dive rounds can focus heavily on prior HPC / GPU / hardware-adjacent projects. Candidates were asked to walk through CV projects, LSF CPU / GPU clusters, DCGM, workflow analysis for GPU u...

---

## 20. Deep Learning Fundamentals: Optimization, Drift, Calibration

- **category**: other
- **duration**: 45 min
- **frequency**: Low
- **last asked**: 2026-01-29
- **stage**: phone-screen · onsite-research
- **access**: Members
- **roles**: mle, rs
- **tags**: ml-knowledge, optimization, calibration, distribution, behavioral
- **url**: https://www.1point3acres.com/interview/problems/company/nvidia/deep-learning-fundamentals
- **related discussions**: 2

### Statement

Deep Learning / Research screens can be pure fundamentals: gradient descent, batch vs SGD, convexity, sharp vs flat minima, bias-variance, calibration, model drift, and business framing of ML quality ...

---
