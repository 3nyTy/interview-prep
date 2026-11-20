The proposal has a good skeleton, but several central claims are wrong. The biggest problems are invalid negative labels, nonexistent IPS, sampled feature logging, impossible DynamoDB batching, and putting policy filters last.

## 1. Critical Flaws — Technical Soundness

### 1.1 “No engagement” is not a valid negative label

- **Severity:** Critical
- **Confidence:** High
- **Problem:** No engagement within six hours does not mean the user disliked the video. The candidate may not have rendered, the user may have scrolled past before exposure, the client event may have been lost, the app may have gone offline, or the event may arrive late. `request_id` also does not prove that a specific candidate was actually shown.
- **Recommendation:** Emit a unique `impression_id` per candidate with client-confirmed render/viewability, position, display timestamp, client version, and event sequence. Model labels as positive, negative, censored, or unknown. Deduplicate events by `event_id`, use event time plus ingestion time, and support label updates after the initial window.

### 1.2 Logging position does not enable IPS

- **Severity:** Critical
- **Confidence:** High
- **Problem:** IPS requires the probability that an item was exposed at a position, not merely the realized position. A deterministic ranker has zero support for many counterfactuals, so its propensity is unknown. “Randomize 1–5% of requests” is not automatically unbiased, especially if only positions are randomized or the candidate pool is already selected by the production policy.
- **Recommendation:** Log `propensity` or the complete randomized policy needed to reconstruct it. Randomize candidate assignment and positions with explicit support constraints. Use clipped/self-normalized IPS or doubly robust estimation, and measure variance and coverage. Choose 1–5% from a power calculation, not intuition.

### 1.3 The logging policy contradicts the train/serve objective

- **Severity:** Critical
- **Confidence:** High
- **Problem:** The proposal first says to log the exact feature vector for every served candidate, then says to retain full snapshots only for shown candidates plus a sample of the rest. That destroys the claimed guarantee. Ranking training needs negatives from the scored-but-unshown set. Randomly sampling those negatives changes the training distribution unless inclusion probability is logged and corrected.
- **Recommendation:** Log all scored candidates at least as compact feature tensors, or sample them with a documented inclusion probability and apply sampling weights. Factor out request-level and user-level features, quantize numeric features, and store per-candidate IDs plus the exact post-preprocessing model input.

### 1.4 Policy filtering cannot run only after ranking

- **Severity:** Critical
- **Confidence:** High
- **Problem:** The ranker will score blocked, unsafe, already-seen, or legally unavailable content. The final positions then differ from the positions the model ranked. The system can also return too few candidates after removing the top results. Safety and legal filtering after ranking is not an acceptable sole enforcement point.
- **Recommendation:** Apply hard eligibility filters before ranking, then apply a final defense-in-depth filter after ranking. Log both pre-filter and post-filter candidate sets. Ensure the ranker sees the same eligible inventory that can actually be displayed.

### 1.5 The “one BatchGetItem round trip” is false

- **Severity:** High
- **Confidence:** High
- **Problem:** DynamoDB `BatchGetItem` supports at most 100 items and 16 MB per request. A 500-candidate or 1,000-candidate request will exceed that as soon as it needs per-item features. DAX does not remove these limits. Unprocessed keys also require retries.
- **Recommendation:** Precompute a compact candidate feature record, issue parallel bounded batches, or use an online feature service designed for multi-entity reads. Budget cache misses and retries explicitly. Do not claim one round trip unless the data model genuinely reduces the request to a small number of keys.

## 2. Significant Concerns — Technical Soundness

### 2.1 The 150 ms p99 budget is arithmetic, not an architecture

- **Severity:** High
- **Confidence:** High
- **Problem:** The stages sum to 120 ms, leaving 30 ms of slack, but p99 latency does not compose by simply adding independent p99 values. Candidate generation fanout latency is governed by the slowest source, plus orchestration and cancellation. SageMaker invocation adds network, queueing, serialization, and container overhead. A 50 ms p99 for scoring 500 candidates is unsubstantiated.
- **Recommendation:** Define per-source p99 budgets, deployment topology, request payload size, model runtime, concurrency, and queueing limits. Benchmark the complete path under production concurrency. Keep ranker and orchestrator in the same region/AZ where appropriate, and define a hard deadline with cancellation rather than retries.

### 2.2 SageMaker does not provide “batched ranking” automatically

- **Severity:** Medium
- **Confidence:** High
- **Problem:** SageMaker accepts one invocation payload, but the model container must interpret that payload as a batch and implement vectorized scoring. One API call does not guarantee efficient inference. Real-time invocation also has request/response payload limits commonly around 6 MB, so wide feature tensors can fail.
- **Recommendation:** Specify the model input contract, maximum batch size, serialization format, vectorization behavior, and benchmarked throughput. Use a dedicated low-latency inference service if SageMaker endpoint overhead cannot meet the deadline.

### 2.3 The six-hour watermark is arbitrary and label corrections are unspecified

- **Severity:** High
- **Confidence:** High
- **Problem:** A watermark must be based on event-time lateness distributions and business tolerance. Late events arriving after the streaming job emits a row will revise labels. The proposal says “backfill” but does not say how existing rows are updated, deduplicated, or excluded from training while unstable.
- **Recommendation:** Use immutable raw events, idempotent event IDs, an allowed-lateness policy based on observed percentiles, and versioned/upserted labels. Maintain a training cutoff that excludes unresolved windows, then backfill and retrain affected partitions.

### 2.4 The event join key is underspecified and likely wrong

- **Severity:** High
- **Confidence:** High
- **Problem:** Engagement events usually contain content ID, session ID, or client impression ID, not the server-side request ID. One request can contain many candidates, and one item can appear in multiple requests. Joining only on `request_id` cannot reliably attribute a watch or like to an item and position.
- **Recommendation:** Generate an item-level exposure token and return it to the client. Require engagement events to carry that token, content ID, position, event time, and client sequence number. Handle duplicate impressions and repeated views explicitly.

### 2.5 “One definition, two materializations” does not prove parity

- **Severity:** High
- **Confidence:** High
- **Problem:** Shared transformation code does not eliminate differences in clocks, watermarks, late data, TTLs, aggregation state, null handling, default values, floating-point behavior, feature freshness, or source availability. Streaming and batch engines can execute the same logical expression with different semantics.
- **Recommendation:** Add replay-based parity tests using captured production inputs, golden feature vectors, freshness checks, null/default counters, and a feature contract containing entity time, computation time, version, and default behavior. Compare logged model inputs, not merely feature-store records.

### 2.6 Feature schema mismatch should not fail the feed closed

- **Severity:** High
- **Confidence:** High
- **Problem:** A schema version mismatch can turn a routine rollout or partial deployment into a feed-wide outage. Schema compatibility is more nuanced than equality. This also does not address stale values, wrong units, changed normalization, or semantically incorrect features.
- **Recommendation:** Use immutable model-plus-preprocessing bundles and explicit compatibility checks. Fall back to the last known compatible model or a bounded baseline. Fail closed only for genuinely unsafe policy data, not ordinary feature-version mismatch.

### 2.7 Rollback is broader than a model pointer

- **Severity:** High
- **Confidence:** High
- **Problem:** A pointer flip does not roll back feature code, preprocessing, candidate indexes, policy configuration, source quotas, or model container changes. A new model may also remain cached in endpoint workers or have in-flight requests. Shadow traffic can double inference load and cause the incident it is meant to detect.
- **Recommendation:** Version and atomically deploy the complete serving bundle: model, container, preprocessing, feature contract, candidate configuration, and policy configuration. Use a tested baseline endpoint, bounded shadow sampling, and a kill switch that bypasses the new path.

### 2.8 The proposed guardrails are insufficient

- **Severity:** High
- **Confidence:** High
- **Problem:** PSI/KL on model scores detects distribution changes, not product harm. Feature null-rate misses stale, defaulted, shifted, unit-changed, or systematically wrong values. Aggregate metrics hide platform, geography, subreddit, creator, and new-user regressions. Engagement metrics are delayed and confounded by position changes.
- **Recommendation:** Add freshness, default-rate, range, cardinality, and source-level checks. Monitor guardrails by major slices. Use sequential rollout analysis with delayed labels, minimum sample sizes, and hard product constraints such as policy violations, hide/report rates, abandonment, and creator concentration.

### 2.9 The cost claim is not quantified

- **Severity:** High
- **Confidence:** High
- **Problem:** “1,000x request rate” is wrong as stated. The record may contain one request row with a 1,000-element array, or 1,000 expanded candidate rows. Cost depends on requests per second, feature bytes, compression ratio, retention, replication, Firehose buffering, and backfill volume. No traffic or payload assumptions are provided.
- **Recommendation:** Define the cost equation:

  `daily bytes = requests/day × candidates/request × bytes/candidate`

  Include raw, compressed, replicated, catalog, query, and retention costs. Measure it with representative tensors. Firehose can deliver Parquet and Iceberg, but delivery is at-least-once, so deduplication and compaction are mandatory.

### 2.10 Cold-start exposure is an unowned objective

- **Severity:** High
- **Confidence:** High
- **Problem:** “Guaranteed exposure” can force low-quality, unsafe, or spam content into feeds and contaminate the training distribution. It also changes the ranking objective from engagement optimization to constrained exploration. The proposal gives no quota, eligibility rule, creator fairness rule, or evaluation method.
- **Recommendation:** Define exploration as a constrained policy with safety eligibility, creator/subreddit quotas, exposure caps, explicit propensity logging, and separate evaluation. Train or optimize for long-term utility rather than simply mixing guaranteed impressions into the main engagement objective.

### 2.11 Rank 500 candidates after generating 1,000 is unexplained

- **Severity:** Medium
- **Confidence:** High
- **Problem:** The proposal never defines how half the candidates disappear before ranking. If that reduction is a heuristic, it creates another hidden selection policy and can remove long-tail or cold-start items. If it happens after ranking, the latency and feature claims are inconsistent.
- **Recommendation:** Define candidate deduplication, eligibility filtering, source quotas, and the exact pre-rank reduction rule. Log every selection probability needed for training and evaluation.

### 2.12 Fallback ordering by source priors is unsafe

- **Severity:** Medium
- **Confidence:** High
- **Problem:** Source priors are not necessarily comparable. A ranker timeout can systematically overexpose trending or collaborative-filtering content, producing a different product and different feedback data.
- **Recommendation:** Maintain a calibrated, policy-filtered fallback ranker or cached baseline model. Monitor fallback traffic separately and exclude or weight it appropriately during training.

## 3. Minor Issues — Technical Soundness

### 3.1 DAX is presented as a generic latency guarantee

- **Severity:** Medium
- **Confidence:** High
- **Problem:** DAX helps cache DynamoDB reads; it does not guarantee single-digit latency for a multi-key request with misses, retries, cache churn, or cross-network calls.
- **Recommendation:** Treat DAX as an optimization. Define hit-rate, miss latency, consistency mode, eviction behavior, and fallback capacity.

### 3.2 Exact model inputs are not necessarily raw feature-store values

- **Severity:** Medium
- **Confidence:** High
- **Problem:** Normalization, missing-value handling, feature ordering, embeddings, and model-side transformations may occur after feature retrieval. Logging the feature store response is not enough.
- **Recommendation:** Log the final serialized model input or a reproducible hash plus the exact preprocessing artifact.

### 3.3 Firehose delivery is not exactly once

- **Severity:** Medium
- **Confidence:** High
- **Problem:** AWS Firehose delivery is at least once. Retries can duplicate records. Direct Parquet/Iceberg delivery also requires explicit format conversion, catalog, buffering, schema, and compaction configuration.
- **Recommendation:** Require stable event IDs and downstream deduplication. Monitor delivery lag, failed records, S3 backup, small files, and Iceberg compaction.

### 3.4 Privacy and deletion are absent

- **Severity:** High
- **Confidence:** High
- **Problem:** Feature snapshots containing user IDs, behavioral features, embeddings, and possibly content text create a sensitive historical data store. Glacier retention does not solve deletion, access control, or subject-level erasure.
- **Recommendation:** Minimize fields, tokenize identifiers, encrypt with scoped KMS keys, enforce access policies, define retention by field class, and implement deletion propagation through raw, derived, and backup data.

### 3.5 No capacity model is supplied

- **Severity:** Medium
- **Confidence:** High
- **Problem:** There are no QPS, peak multiplier, candidate-source concurrency, Firehose throughput, partition count, endpoint instance count, memory, or connections-per-host assumptions. Therefore the numerical design cannot be validated.
- **Recommendation:** State traffic assumptions and derive capacity, including peak QPS, fanout, request bytes, feature-store RCUs, inference concurrency, and failure-mode capacity.

## 4. Interview Follow-Ups the Candidate Cannot Currently Answer — Interview Exposure

### 4.1 “What exactly is the IPS propensity?”

- **Exposure severity:** Critical
- **Confidence:** High
- **Breaking question:** “You logged position. Give me the propensity used in the IPS denominator.”
- **Correct response:** Log the policy probability for each item-position exposure, including randomization policy and candidate-set support. Use clipped/self-normalized IPS or doubly robust estimation and quantify variance.

### 4.2 “How do you know an item was actually seen?”

- **Exposure severity:** Critical
- **Confidence:** High
- **Breaking question:** “The server logged an impression, but the user never scrolled to it. Is that a negative?”
- **Correct response:** Distinguish served, rendered, viewable, started, and engaged events. Treat unobserved exposure and missing telemetry as censored or unknown, not negative.

### 4.3 “How does 1,000-candidate feature retrieval fit in one BatchGet?”

- **Exposure severity:** Critical
- **Confidence:** High
- **Breaking question:** “What is the AWS limit?”
- **Correct response:** `BatchGetItem` is capped at 100 items and 16 MB. Use parallel batches, compact precomputed records, or a purpose-built multi-get feature service.

### 4.4 “What happens when a late event changes a label already used for training?”

- **Exposure severity:** Critical
- **Confidence:** High
- **Breaking question:** “Do you mutate historical training rows? How do you avoid duplicates?”
- **Correct response:** Immutable raw events, idempotent event IDs, versioned labels, allowed lateness, backfill/upsert, and training cutoffs that exclude unstable windows.

### 4.5 “Why are policy filters after ranking?”

- **Exposure severity:** Critical
- **Confidence:** High
- **Breaking question:** “Can the ranker return illegal or blocked content?”
- **Correct response:** Hard eligibility filters before ranking, defense-in-depth filters after ranking, and logging of both candidate sets.

### 4.6 “What does your 150 ms p99 include?”

- **Exposure severity:** High
- **Confidence:** High
- **Breaking question:** “What is the slowest candidate source, and what happens when it exceeds its deadline?”
- **Correct response:** Per-source deadlines, cancellation, fanout-tail analysis, measured inference concurrency, no retries on the critical path, and an explicit fallback budget.

### 4.7 “How do you prove parity?”

- **Exposure severity:** High
- **Confidence:** High
- **Breaking question:** “Show me a test that proves a streaming feature equals its offline value.”
- **Correct response:** Replay production inputs through both implementations, compare golden vectors, test late events and null/default paths, and monitor serving-versus-recomputed divergence continuously.

### 4.8 “What exactly does rollback revert?”

- **Exposure severity:** High
- **Confidence:** High
- **Breaking question:** “The model is good, but the feature transformation is bad. What pointer do you flip?”
- **Correct response:** Roll back an immutable serving bundle, not just a model artifact, with a known-compatible baseline and a tested kill switch.

### 4.9 “How did you choose 1–5% exploration?”

- **Exposure severity:** High
- **Confidence:** High
- **Breaking question:** “What is the minimum sample size for a long-tail subreddit and a position-10 estimate?”
- **Correct response:** Allocate exploration from a power and support analysis, use stratification or adaptive exploration, and report confidence intervals and effective sample size.

### 4.10 “What is the objective impact of guaranteed cold-start exposure?”

- **Exposure severity:** High
- **Confidence:** High
- **Breaking question:** “Are you optimizing watch time, discovery, creator coverage, or all three?”
- **Correct response:** Define a constrained multi-objective policy, separate exploration traffic in evaluation, enforce safety and exposure caps, and correct training for the changed policy.

## 5. What’s Actually Good

The proposal correctly prioritizes the feedback loop over model architecture. It also correctly identifies the value of logging serving-time inputs, recognizes that labels arrive asynchronously, includes late-event handling, separates candidate generation from ranking, uses partial degradation, keeps policy decisions auditable, and includes shadow/canary rollout with feature-quality monitoring.

Those are good instincts. They are not yet a production design because the data contracts, exposure semantics, propensity logging, AWS limits, failure handling, and capacity model are missing or incorrect.