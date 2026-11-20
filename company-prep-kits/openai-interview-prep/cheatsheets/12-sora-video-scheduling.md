# Cheat Sheet — Sora Video-Generation Scheduling

> Debate-validated (Codex, high effort): v1 "right primitives, semantics underspecified" → v2 below. Builds on your saved Sora scheduler notes (admission queue / preemption / fencing-protects-the-DB-not-S3).

## 1. Summary (in plain terms)
Users submit a text→video prompt; scarce, expensive GPUs run a long job (seconds→minutes); the video comes back. The system's job is **scheduling**: queue fairly when demand exceeds GPUs, keep GPUs busy, survive workers getting killed (spot reclaim) without redoing all the work, and guarantee exactly one correct result with no double-billing.

## 2. Key deep dives (plain terms)

- **One owner, one linearization path (fixes double-assignment).** Either the scheduler assigns or the worker claims — not both. The scheduler does **one atomic transaction**: `QUEUED → ASSIGNED`, mint `attempt_id`, write `slot_id` + `gpu_class` + `lease_epoch`. The worker only runs work already assigned to its slot and heartbeats that same epoch. **Fence the GPU *slot*, not just the job** (a restarted worker mustn't think it owns the same physical GPU).

- **Scheduler state must be durably rebuildable.** Redis is not enough — if the scheduler dies, its replacement must recover every running attempt, slot lease, and checkpoint pointer by **scanning DynamoDB leases + worker heartbeats**. Cache is disposable. Shard scheduling by GPU-class/region (not one global matching loop).

- **Exactly-one *artifact* ≠ exactly-one *side effect* (the killer insight).** Attempt-scoped immutable S3 keys + a DB-promoted `final_artifact_id` solves *which file is real*. It does **not** stop double billing, duplicate notifications, or stale progress events. Fix: drive **all external side effects (billing, notify, quota release) from the single terminal DB transition via an outbox** — never from worker-local "I finished." (Same outbox pattern as Webhook/Payment.)

- **Tighten the completion fence.** "CAS on state+attempt" isn't enough — a worker can finish *after* its lease expired while the job's already been reassigned. Completion must conditionally commit on `job_id + attempt_id + lease_epoch + state=RUNNING + lease_expires_at > now`. Expired lease → must re-validate before it's allowed to promote.

- **Cancellation precedence is explicit.** Define who wins: `CANCEL_REQUESTED` is advisory until a terminal CAS commits; `SUCCEEDED` wins if artifact promotion commits first, else `CANCELLED` wins. Workers poll cancel and fail promotion if cancel already won.

- **Checkpoints are versioned snapshots, validated before use.** Resuming a diffusion job needs more than "latents + step": RNG/sampler state, **model + container digest**, config hash, prompt-embedding digest, tensor checksum. **Refuse resume across incompatible model/code versions** (or output diverges). S3 object *existing* ≠ valid — verify content hash + complete manifest **before** advancing the checkpoint pointer (guards partial/truncated uploads).

- **Checkpointing must earn its keep.** For a 40s job, uploading state every 30s can cost more than re-running from seed. **Gate checkpointing** on expected-remaining-runtime × preemption-probability × checkpoint-size vs recompute cost. Short jobs: just re-run. And "<30s lost work" only holds if you enforce `RPO = interval + checkpoint_commit_time` (upload included) — measure it, backpressure if workers can't commit in budget.

- **Fair-share tokens are NOT fungible across GPU types.** An A100 token ≠ an H100 token ≠ an 8-GPU gang job. Use **dominant-resource fairness / per-class pools** weighted by GPU type, count, memory, expected duration. Add **aging + gang-scheduling + backfill** so large/pinned jobs don't starve, and **preemption rules by tier** so a free job can't sit on a GPU a paid job needs (priority inversion).

- **Admission control, because demand >> supply.** "Queue, don't drop" with an *unbounded* queue is just an outage with nicer branding. Explicit admission states: **accept / defer-with-ETA / reject-with-retry-after / downgrade-quality / shed low-priority**, plus per-tenant queue caps.

- **Honest ETA is probabilistic, not `depth ÷ throughput`.** Under preemption, heterogeneous GPUs, tiers, and retries, give a **range + confidence per GPU-class/tier/job-shape**, not a single deterministic number.

- **Spot reclaim storms:** a whole AZ of spot GPUs vanishing re-queues everything at once. Dampen with retry budgets, jittered reschedule, checkpoint-aware priority, and an explicit spot→on-demand spillover policy.

## 3. Data model (DynamoDB)
```
JOBS (user-facing lifecycle — separate from attempts):
  PK = JOB#<job_id>
  attrs: state (QUEUED|ASSIGNED|RUNNING|SUCCEEDED|FAILED|CANCELLED|CANCEL_REQUESTED),
         tier, tenant_id, gpu_class_required, final_artifact_id (DB-promoted pointer),
         submit_idempotency_key

ATTEMPTS (execution — checkpointing is an ATTEMPT sub-state, not a job state):
  PK = JOB#<job_id>  SK = ATTEMPT#<attempt_id>
  attrs: slot_id, worker_id, lease_epoch, lease_expires_at,
         latest_valid_checkpoint (advanced only after hash+manifest verify),
         artifact_key = job/<id>/attempt/<attempt_id>/out.mp4   # immutable, attempt-scoped

GPU SLOT LEASES (fence the slot, not just the job):
  PK = SLOT#<slot_id>
  attrs: worker_id, lease_epoch, gpu_class, current_attempt, health

OUTBOX (ALL side effects fire from the terminal transition, once):
  billing, user-notify, quota-release, analytics  → keyed by terminal transition version

Completion CAS: commit on job_id+attempt_id+lease_epoch+state=RUNNING+lease_expires_at>now
```

**Biggest traps:** (1) treating exactly-one-artifact as exactly-one-side-effect (bill twice); (2) fair-share tokens fungible across GPU classes; (3) "honest ETA = depth/throughput"; (4) resuming a checkpoint across a model-version change.
