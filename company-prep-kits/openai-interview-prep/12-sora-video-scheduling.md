# Design Sora Video-Generation Scheduling

> OpenAI Staff SWE prep. Validated via the cross-LLM debate skill (Codex, high effort, 1 round). Builds on the saved Sora scheduler notes in [99-interview-context]. Verdict: right primitives, but v1 "named the primitives without precise ownership, fencing, terminal semantics, durable recovery, heterogeneous fairness, and checkpoint validity" — v2 closes them.

## Problem
The scheduling/serving system behind text→video generation: submit a prompt, a long-running diffusion job (seconds→minutes) runs on scarce GPUs, the video is stored and delivered. Focus = scheduling / queueing / capacity / job-lifecycle, **not** the model. AWS-native.

## Scale assumptions
Millions of jobs/day, durations seconds→minutes, thousands of GPUs across types (A100/H100), demand routinely exceeds supply. GPUs are the scarce, expensive bottleneck.

## Architecture (v2)

### Submission & state
API GW → submission service: validate, quota/admission, write job to **DynamoDB**. **Separate job lifecycle from attempt lifecycle** (checkpointing is an *attempt* sub-state, not a user-visible job state). Submit is idempotent (key per submit) — and so is every later transition (assignment, start, checkpoint commit, promotion, cancel, notify) via versioned CAS.

### Ownership — one linearization path (fixes double-assignment)
The **scheduler assigns**, the worker does not independently claim. One atomic transaction: `QUEUED → ASSIGNED`, mint `attempt_id`, write `slot_id` + `gpu_class` + `lease_epoch`. The worker only runs work already assigned to its slot and heartbeats that epoch. **Fence the GPU *slot* too** (`SLOT#<id>` lease with `worker_id` + `lease_epoch`) so a restarted/duplicated worker can't believe it owns the same physical GPU.

### Durable scheduler recovery
DynamoDB is the source of truth for running attempts + slot leases; ElastiCache is a disposable accelerator. On scheduler failover, **rebuild state by scanning active leases + heartbeats** (else you leak capacity or reschedule live jobs). **Shard scheduling** by GPU-class/region/model-family — never one global matching loop.

### Queueing & fair scheduling
- Durable job table + indexed **ready-sets** per class (don't assume SQS/Kinesis gives cheap priority reordering / fair scans / heterogeneous matching — pick the model deliberately, design for reorder + cancellation-removal + class filtering).
- **Dominant-resource fairness / per-class pools** — fair-share tokens are **NOT fungible** across A100 vs H100 vs an 8-GPU gang job vs a long video. Weight by GPU type, count, memory, expected duration, tenant weight, preemption cost.
- **Aging + gang-scheduling + backfill** to bound starvation of large/pinned jobs.
- **Preemption rules by tier** (paid/internal can preempt free; account kill/resume cost) to avoid priority inversion from burst-borrowing.

### Admission control (demand >> supply)
Unbounded queue = "an outage with better branding." Explicit states: **accept / defer-with-ETA / reject-with-retry-after / downgrade-quality / shed low-priority**, plus per-tenant queue caps and budgets.

### Execution on GPU workers
EKS or EC2 P4/P5, spot for some tiers. Worker (already assigned) loads the model (kept warm/resident where the workload mix allows — **model-aware routing + warm-pool sizing + eviction + cold-load accounting** once you have many model versions/resolutions/LoRAs). Runs inference. **Bin-packing (MIG/MPS) is not a free win** — define supported sharing modes per model/GPU, hard memory admission, per-job device isolation, OOM containment, conservative concurrency.

### Checkpointing — versioned, validated, and economically gated
- A checkpoint is a **versioned execution snapshot + manifest**: model artifact digest, **code/container digest**, RNG state, sampler state, prompt-embedding digest, step index, latent-tensor checksum, config hash. **Refuse resume across incompatible model/code versions** (else output diverges).
- **Validate before use**: S3 object existing ≠ valid. Verify content hashes + complete manifest, then advance the `latest_valid_checkpoint` pointer only after the full object set commits (guards partial multipart / truncated tensors).
- **Gate checkpointing** on expected-remaining-runtime × preemption-probability × checkpoint-size vs recompute cost — for short jobs, re-run from seed.
- RPO honesty: lost work = `interval + checkpoint_commit_time` (serialize + upload + DB promote + detect). Measure it; backpressure if workers can't commit in budget. "<30s" is a claim you must *enforce*.

### Exactly-one artifact AND exactly-one side effect (the killer point)
- **Artifact selection**: workers write to **attempt-scoped immutable S3 keys** (`job/<id>/attempt/<attempt_id>/out.mp4`); the **DB promotes exactly one** as `final_artifact_id`; readers resolve via the DB pointer, **never "latest object."** Fencing protects the resource that *checks* it — the DB, not S3.
- **Tighten completion**: CAS on `job_id + attempt_id + lease_epoch + state=RUNNING + lease_expires_at > now` (a worker can finish *after* lease expiry while reassigned — it must re-validate before promoting).
- **Side effects ≠ artifact**: billing, user-notify, quota-release, analytics must fire from the **single terminal DB transition via an outbox** keyed by transition version — never from worker-local "I finished" (else double-bill / duplicate notify). Stale **progress** events carry `attempt_id + lease_epoch`; readers ignore stale epochs.

### Cancellation precedence (explicit)
`CANCEL_REQUESTED` is advisory until a terminal CAS commits: `SUCCEEDED` wins if artifact promotion commits first, else `CANCELLED` wins. Workers poll cancel and fail promotion if cancel already won.

### Spot reclaim storms
A whole AZ of spot reclaimed re-queues everything at once. Dampen: retry budgets, jittered reschedule, checkpoint-aware priority, explicit spot→on-demand spillover policy with per-tier limits.

### Honest ETA (probabilistic, not depth÷throughput)
Under preemption / heterogeneous GPUs / tiers / retries / cold starts, a single deterministic ETA is fiction. Give a **range + confidence per GPU-class / tier / job-shape**, factoring reservations, retry backlog, preemption rate, warm capacity.

### Other must-mentions
- **Safety/moderation** is part of the terminal artifact pipeline (gate visibility *before* SUCCEEDED), not an afterthought.
- **GC**: attempt-scoped artifacts/checkpoints create orphans by design → lifecycle cleanup keyed off terminal state + retention; never delete the promoted artifact early.
- **Cost controls**: per-tenant budgets, retry caps, max runtime, max resolution/duration by tier, abuse throttles.
- **Regional failure model**: region-pin jobs; state AZ/regional dependency assumptions.

### AWS stack
API GW, Lambda/ECS control, DynamoDB (job/attempt state + slot leases + outbox), durable job table + ready-set indexes (or broker partitions w/ scheduler-owned cursors), EKS / EC2 P-family, S3 (checkpoints + attempt artifacts), CloudFront, ElastiCache, CloudWatch, spot+on-demand.

## Top deep-dives an interviewer drills
1. Double-assignment / slot fencing / scheduler failover recovery.
2. Exactly-one *side effect* (not just artifact) — outbox off the terminal transition.
3. Completion-vs-lease-expiry and completion-vs-cancel races.
4. Checkpoint validity + version compatibility + whether checkpointing even pays off.
5. Fair-share across heterogeneous GPUs + large-job starvation.

## Debate scorecard
Codex raised 10 "critical" (all accepted): scheduler/worker ownership inconsistency, no durable running-set reconstruction, loose lease-expiry fence, artifact≠side-effect, missing cancellation semantics, hand-waved checkpoint correctness, checkpoint corruption/partial-write, asserted-not-designed ETA, non-fungible fair-share tokens, no real admission policy. Plus significant: central-scheduler bottleneck, slot fencing, spot retry amplification, checkpoint economics, RPO enforceability, model-residency vs heterogeneity, bin-packing isolation, large-job starvation, priority inversion, vague queue substrate, lying progress, GC. All incorporated. "The DB-pointer-never-latest-S3-object idea is the right primitive" — the gap was the surrounding semantics.
