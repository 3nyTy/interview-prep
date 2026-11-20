# Sora Video-Generation Scheduling — Detailed Explainer (plain English)

> Debate-validated (Codex). Pattern: v1 had the right primitives but underspecified the semantics — the fixes below are what make it Staff-level. Builds on the saved Sora scheduler deep-dives (admission queue, preemption, fencing-protects-the-DB-not-S3).

## What we're building
The system behind a text-to-video product like Sora. A user types a prompt; a **long-running** GPU job (tens of seconds to minutes) generates a video; the result comes back. We're designing the **scheduling and job-lifecycle** system — how jobs queue, get matched to scarce GPUs, survive failures, and produce exactly one correct result — **not** the ML model itself.

## What makes it hard
- **GPUs are scarce and expensive.** You must keep them busy (~90%+ utilized) but also queue gracefully when demand exceeds supply (which is most of the time at peak).
- **Jobs are long and get interrupted.** Spot instances get reclaimed; workers crash. Re-running a 3-minute job from scratch wastes a fortune in GPU time.
- **Correctness with money attached.** Exactly one final video, no double-billing, honest progress and ETAs.

## The overall flow
1. **Submit:** API → submission service validates, applies quota/admission control, writes a job record to **DynamoDB**. Idempotent on submit.
2. **Queue:** the job lands in a durable, partitioned queue. The **admission queue is decoupled from the running set** — the running set is bounded by GPU capacity, and backpressure happens at *admission* (we tell you "deferred, ETA ~5 min"), never by dropping a job mid-flight.
3. **Schedule:** a scheduler matches queued jobs to free GPU slots (by GPU type — some models need a specific GPU class).
4. **Execute:** a GPU worker runs the diffusion model, periodically checkpointing progress to S3.
5. **Complete:** the output video goes to S3; the job is marked done; the user is notified and served the video via CloudFront.

## Hard problem 1: don't double-assign a GPU (one owner)
A subtle inconsistency sinks naive designs: "the scheduler assigns jobs" *and* "the worker claims a job" — that's two schedulers, and they can double-book a GPU. **Pick one linearization path:** the **scheduler assigns** in a single atomic transaction (`QUEUED → ASSIGNED`, mint an `attempt_id`, write the `slot_id`, `gpu_class`, and a `lease_epoch`). The worker does **not** independently claim work — it only runs what's already assigned to its slot, and it heartbeats with that same epoch.

And **fence the GPU slot itself**, not just the job: each physical GPU slot has a lease (`slot_id`, `worker_id`, `lease_epoch`). A restarted or duplicated worker that thinks it still owns the GPU is rejected because its epoch is stale.

## Hard problem 2: the scheduler must survive its own death
If the scheduler crashes, its replacement must know **every running job, every GPU slot lease, and every checkpoint pointer** — otherwise it either leaks capacity (GPUs sitting idle, thought to be busy) or reschedules jobs that are actually still running. So:
- **DynamoDB is the durable source of truth** for running attempts and slot leases. Redis/ElastiCache is just a fast accelerator and is disposable.
- On failover, the new scheduler **rebuilds its state by scanning active leases and worker heartbeats.**
- **Shard the scheduler** by GPU-class / region / model-family so the matching loop isn't one global bottleneck at millions of jobs/day.

## Hard problem 3: exactly-one *artifact* is NOT exactly-one *side effect* (the killer insight)
Here's the trap. Suppose a worker's lease expired (we thought it died), we reassigned the job, but the original worker is actually still alive and finishes. Now two workers produced output. How do we ensure exactly one *correct* result?

The well-known half of the answer:
- Workers write to **attempt-scoped immutable S3 keys**: `job/<id>/attempt/<attempt_id>/out.mp4`. Each attempt writes to its *own* key, so they never overwrite each other.
- The **database promotes exactly one** attempt as `final_artifact_id`. Readers always resolve the video through the DB pointer, **never** by listing "the latest object in the folder."
- Why does this work? Because **a fencing token only protects the resource that actually checks it** — and S3 doesn't check tokens; it's dumb storage. So you don't try to make S3 enforce correctness. You make S3 write-only-to-your-own-key, and let the **DB be the single arbiter.**

**But that's only half.** Picking the right *file* does **not** prevent the *side effects* from happening twice: billing the customer, sending the "your video is ready" notification, releasing quota, emitting analytics. If those fire from the worker's local "I finished," you'll double-bill on a duplicate completion.

**The fix:** all externally-visible side effects fire from the **single terminal DB transition via an outbox** (same pattern as Webhook/Payment). When the job's state row transitions to its terminal value, an outbox row is written in the same transaction; a relay then bills/notifies/releases-quota exactly once, keyed by the terminal transition version. Never from worker-local success.

### Tighten the completion check
"CAS on job state + attempt" isn't enough — a worker can finish *after* its lease expired while a new attempt is already running. Completion must conditionally commit on **all** of: `job_id`, `attempt_id`, `lease_epoch`, `state == RUNNING`, **and** `lease_expires_at > now`. If the lease already expired, the worker must re-validate (or its promotion is refused) so a stale attempt can't win.

### Cancellation precedence
Define explicitly who wins when a cancel races a completion: `CANCEL_REQUESTED` is advisory until a terminal CAS commits. If artifact promotion commits first → `SUCCEEDED` wins; if the cancel commits first → `CANCELLED` wins. Workers poll for cancellation and fail their promotion if cancel already won.

## Hard problem 4: checkpoint/resume — correct *and* worth it
Long jobs checkpoint so a preempted job resumes near where it stopped instead of restarting. But two real issues:

**Correctness.** For a diffusion job, resuming needs more than "latents + step index." It needs RNG state, sampler state, the **model artifact digest and container/code digest**, prompt-embedding digest, config hash, and a tensor checksum — captured as a **versioned manifest**. And you must **refuse to resume across an incompatible model/code version** (the model got updated mid-job → resumed output would diverge from what was started). Also, *an S3 object existing does not mean it's valid* — a partial multipart upload or truncated tensor would resume garbage. So **verify hashes + the complete manifest before advancing the checkpoint pointer.**

**Economics.** Checkpointing isn't free — you spend GPU-idle time and bandwidth uploading state. For a 40-second job, uploading a big checkpoint every 30 seconds can cost more than just re-running from the seed. So **gate checkpointing** on: expected remaining runtime × preemption probability × checkpoint size vs. recompute cost. Short jobs: don't checkpoint, just re-run.

**Honest recovery-point.** "We lose less than 30 seconds of work" is only true if you count the *whole* commit: checkpoint interval + serialize + upload + DB promote + detection delay. If the upload takes 20 seconds, a 30-second interval does **not** mean 30 seconds lost. Define `RPO = interval + commit_time`, *measure* it, and apply backpressure if workers can't commit within budget.

## Hard problem 5: fair sharing across *different* GPUs
You can't treat fair-share "tokens" as interchangeable. A token for an A100 slot is **not** the same as an H100 slot, an 8-GPU gang job, or a long video generation. Generic weighted-fair-queuing on a single token type creates unfairness and waste. Instead use **dominant-resource fairness / per-class pools**, weighting by GPU type, count, memory, expected duration, and tenant weight.

Then handle the classic scheduling pathologies:
- **Large-job starvation:** small jobs keep slipping into gaps while a job needing 8 contiguous GPUs waits forever → add **aging** (priority rises with wait), **gang-scheduling**, and **backfill with reservations**.
- **Priority inversion:** burst-borrowing lets a free-tier job sit on a scarce GPU when a paid job arrives → define **preemption rules by tier**, accounting for the kill/resume cost.

## Hard problem 6: demand >> supply, gracefully
"Queue, don't drop" with an *unbounded* queue is "an outage with better branding" — ETAs explode, storage grows, retries pile up. So **admission control** has explicit outcomes: accept / defer-with-ETA / reject-with-retry-after / downgrade-quality / shed low-priority — plus per-tenant queue caps and budgets.

And **spot-reclaim storms:** when a whole AZ of spot GPUs is reclaimed, all those jobs re-enter the queue at once and stampede onto scarce on-demand capacity. Dampen with retry budgets, jittered reschedule, checkpoint-aware priority, and an explicit spot→on-demand spillover policy.

## Honest ETAs
A single deterministic "ready in 4:32" is fiction under preemption, heterogeneous GPUs, tiers, and retries. Give a **probabilistic range + confidence**, computed per GPU-class / tier / job-shape, factoring current reservations, retry backlog, preemption rate, and warm capacity.

## Don't forget
- **Safety/moderation** gates the video's *visibility* before the job is marked SUCCEEDED — not bolted on after.
- **Garbage collection:** attempt-scoped artifacts and checkpoints create orphans by design → lifecycle cleanup keyed off terminal state + retention (never delete the promoted artifact early).

## Data model (DynamoDB)
```
JOBS (user-facing lifecycle — SEPARATE from attempts):
  PK = JOB#<job_id>
  attrs: state (QUEUED|ASSIGNED|RUNNING|SUCCEEDED|FAILED|CANCELLED|CANCEL_REQUESTED),
         tier, tenant_id, gpu_class_required,
         final_artifact_id,            # the DB-promoted single pointer
         submit_idempotency_key

ATTEMPTS (execution — checkpointing is an ATTEMPT sub-state, not a job state):
  PK = JOB#<job_id>  SK = ATTEMPT#<attempt_id>
  attrs: slot_id, worker_id, lease_epoch, lease_expires_at,
         latest_valid_checkpoint,      # advanced only after hash + manifest verify
         artifact_key = job/<id>/attempt/<attempt_id>/out.mp4   # immutable, per-attempt

GPU SLOT LEASES (fence the slot, not just the job):
  PK = SLOT#<slot_id>
  attrs: worker_id, lease_epoch, gpu_class, current_attempt, health

OUTBOX (ALL side effects fire once, from the terminal transition):
  billing / notify / quota-release / analytics   → keyed by terminal transition version

Completion CAS:
  commit on job_id + attempt_id + lease_epoch + state=RUNNING + lease_expires_at > now
```

## If the interviewer pushes further
- *"You said exactly-one artifact — so you're done?"* No — that picks the file. Billing/notify must fire from the terminal DB transition via an outbox, or a duplicate completion double-bills.
- *"Why not just fence S3?"* S3 doesn't check fencing tokens; it's dumb storage. So attempts write to their own immutable keys and the DB arbitrates.
- *"Always checkpoint?"* No — for short jobs the upload cost exceeds re-running from seed. Gate it on remaining-runtime × preemption-probability × size.
- *"One fair-share queue?"* No — a token isn't fungible across GPU classes; use dominant-resource fairness per class, plus aging/gang-scheduling to avoid starving big jobs.
