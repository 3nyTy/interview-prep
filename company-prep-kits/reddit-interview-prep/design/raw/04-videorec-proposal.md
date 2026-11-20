> ## ⛔ SUPERSEDED DRAFT — DO NOT STUDY THIS AS THE ANSWER
>
> This is the **Round-0 proposal**, preserved only as the input Codex attacked. It contains errors that
> were found and fixed. The authoritative version is `../reddit-design-crib.md` (Q4).
>
> **Known wrong in this file:**
> - Uses "impression with no engagement in the window" as a **negative label**. Unobserved exposure is
>   **censored**, not negative — the item may never have rendered or been scrolled to.
> - Claims logging the position enables IPS. IPS needs the **propensity** (probability of exposure at that
>   position); a deterministic ranker has none.
> - **Self-contradicts**: says log the exact feature vector for every served candidate, then says retain full
>   snapshots only for shown candidates plus a sample.
> - Places policy/eligibility filters **only after** ranking, so the ranker scores blocked content and the
>   slate can come back too short. Hard filters must run pre-rank.
> - Claims feature fetch is "one `BatchGetItem` round trip" for 500–1000 candidates. The cap is **100 items /
>   16 MB**, and DAX does not lift it.
> - Sums per-stage budgets into a 150 ms p99. p99 does not compose additively.
> - Joins engagement to impressions on `request_id`, which cannot attribute to a specific item or position.

# Proposal: Reddit Video Recommendation System

Interview context: Reddit SWE L6 (Staff) system-design round, 60 min. Real prompt as reported:
"ML system-design round: architect a video recommendation system covering candidate generation, ranking
models, and serving. **The interviewer pushes hard on the infrastructure side — how services are split,
how data flows end to end, and especially event logging, pipelines, and observability that power the
feedback loop, often spending more time here than on the model itself.**"

Constraint: AWS-native. Candidate is a backend/infra Staff engineer, not an ML researcher, so the answer
should treat the model as a black box and win on the data-and-serving infrastructure — which is exactly
where the reported interviewer focus is.

## Position

The model is the least interesting part of this system. I will spend the round on the **feedback loop**:
what gets logged, how labels are made, how train/serve skew is prevented, and how a bad model is caught
and rolled back.

### 1. Service split

```
Client -> Feed API (BFF)
            -> Recommendation Orchestrator          [owns the request lifecycle + budget]
                 -> Candidate Generators (N sources, parallel, independent failure)
                      - collaborative-filtering ANN (OpenSearch k-NN / SageMaker endpoint)
                      - content/embedding similarity (new videos, cold start)
                      - trending-in-subreddit
                      - follow-graph recency
                 -> Feature Store (online reads)
                 -> Ranker (SageMaker real-time endpoint, batched)
                 -> Filters/policy (already-seen, blocked subs, NSFW, geo/legal)
```

Two boundaries I would defend:

- **Candidate generation and ranking are separate services**, because they scale on different axes
  (candgen is I/O and index-bound, ranking is compute/accelerator-bound) and because a candgen source
  must be able to fail without taking down the feed.
- **Policy filtering runs last, after ranking, never inside the model.** Legal and safety removals must
  be deterministic and auditable, not a learned score. This is the boundary that keeps you out of trouble.

Each candidate source has an independent timeout and the orchestrator proceeds with whatever returned —
**partial degradation over total failure**. If ranking itself times out, fall back to the candidate set
ordered by its own source priors.

### 2. Latency budget (p99 ≈ 150 ms server-side)

| Stage | Budget |
|---|---|
| Candidate generation (parallel, ~1000 candidates) | 30 ms |
| Online feature fetch (batched, one round trip) | 20 ms |
| Ranking (one batched call scoring ~500) | 50 ms |
| Filters, assembly, serialization | 20 ms |
| Slack | 30 ms |

The ranker is called **once with the whole candidate batch**, never per-candidate. Per-candidate calls are
the single most common design error in this problem.

### 3. THE central point: feature snapshot logging

**Log the exact feature vector used at serving time, alongside the request.** Do not re-derive features
at training time from event history.

Why this dominates everything else: re-deriving features offline means training on values that differ
from what the model actually saw — **train/serve skew** — and it is silent. Nothing breaks; the model just
gets worse, and you cannot debug it because the offline pipeline "looks right."

Every served request emits an impression record:

```
request_id, user_id, ts, model_version, feature_schema_version,
candidate_ids[], source_per_candidate[], scores[], final_positions[],
feature_snapshot (per candidate, compressed), exploration_flag
```

Written to Kinesis Firehose → S3 (Parquet/Iceberg), partitioned by date/hour.

### 4. Label generation is a windowed join, not a lookup

Engagement events (`view_start`, `watch_progress`, `like`, `share`, `skip`, `hide`) arrive separately and
**late**. A label like "watched >30s" only exists after the fact.

- Streaming job (**Flink on Kinesis Data Analytics**) keyed by `request_id` joins impressions to
  engagement over a bounded window (e.g. 6 h) with watermarks, and emits training rows.
- Events later than the watermark go to a **late-arrival path** that lands in S3 and is picked up by the
  next batch backfill, rather than being dropped or held forever.
- **Negative labels come from impressions with no engagement inside the window** — which is only possible
  because impressions are logged. You cannot construct negatives from engagement data alone.

### 5. Position bias — do not train on your own ranking

Logged data is confounded: an item got clicked partly because the previous model put it at position 1.
Training naively on this makes the model learn its predecessor's ranking, and the loop tightens.

Two mitigations, both cheap:

- **Log the position** and apply inverse-propensity weighting in training.
- Reserve a small **randomized exploration slate** (1–5 % of requests, flagged in the log) that produces
  unbiased data. The exploration flag is in the impression record precisely so training can select on it.

### 6. Feature store: one definition, two materializations

- **Online:** DynamoDB (single-digit ms, keyed by entity id) fronted by DAX for the hottest user/video
  features. Read path is a single `BatchGetItem`.
- **Offline:** S3 + Iceberg, with **point-in-time-correct** joins for training.
- Both are materialized from the **same transformation code** — one definition compiled into a streaming
  path and a batch path. Two hand-written pipelines that "should agree" is how parity silently breaks.
- **Features are versioned with the model.** A model must never read a feature schema it was not trained
  against; the served `feature_schema_version` is checked against the model's expectation and the request
  fails closed if they disagree.

### 7. Model rollout and rollback

Registry (SageMaker Model Registry) → **shadow** (score live traffic, serve nothing, compare
distributions) → **canary** by traffic % → A/B with guardrail metrics → ramp.

- Rollback is a **pointer flip on model version**, and must not require a redeploy.
- Guardrails abort a ramp automatically: p99 latency, error rate, score-distribution drift (PSI/KL against
  the shadow baseline), and **feature null-rate**.
- A/B assignment is sticky per user and logged in the impression record, so analysis does not depend on
  reconstructing bucket membership later.

### 8. Observability — the metrics that actually catch failures

- **Feature null-rate per feature.** This is the number-one silent killer: an upstream pipeline breaks, a
  feature becomes null, the model degrades gracefully to garbage, and no alarm fires because latency and
  error rate are perfect.
- **Train/serve skew monitor:** sample logged serving features, recompute them offline, and alert on
  divergence. This is a real recurring job, not a one-time check.
- Per-stage latency and per-candidate-source timeout rate.
- Score distribution drift vs the shadow baseline.
- Candidate-set size and post-filter survival rate — a policy filter that suddenly removes 80 % of
  candidates looks like a model problem but is a filter bug.
- End-to-end: impression-to-engagement rate by model version and by position.

### 9. Cold start

- **New video:** no interaction history, so collaborative filtering cannot see it. Content-based
  embedding (title, subreddit, thumbnail, transcript) puts it in the ANN index immediately, plus a small
  guaranteed-exposure budget so it can earn interaction data. Without the exposure budget, new content can
  never escape.
- **New user:** popularity priors scoped by subreddit and geo, with a wider exploration rate that decays
  as signal accumulates.

### 10. Storage and cost

Impression logging is the dominant data cost — one record per candidate per request, so ~1000x the
request rate. Mitigations: log the full feature snapshot only for the candidates that were **actually
shown** plus a sample of the rest; compress; Parquet columnar; and lifecycle to Glacier after the training
window. Logging every feature of every candidate at full fidelity is unaffordable and unnecessary.

## Explicit trade-offs I am accepting

1. Full feature snapshots are kept only for shown candidates plus a sample — full-fidelity logging of all
   candidates is too expensive.
2. Labels are available only after the join window (hours), so the model trains on data that is hours old.
3. 1–5 % of traffic is deliberately randomized, which costs engagement in exchange for unbiased data.
4. Approximate nearest neighbour means recall < 100 % at the candidate stage.
5. Rolling model updates are hourly/daily, not continuous online learning.
