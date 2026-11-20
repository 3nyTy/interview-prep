# Mock Scorecard — Q12: Sora Video Scheduling

**Date:** 2026-07-16 · **Mode:** B (live mock, warm rep — punchlines seen just prior) · **Full write-up:** [`../detailed/12-sora-video-scheduling.md`](../detailed/12-sora-video-scheduling.md)

**Verdict:** Correctness reflexes now transfer cleanly — fencing, CDC/outbox for side effects, and the "S3 won't fence → DB-promoted pointer" lesson all showed up (the last one *because* Cloud IDE was fresh — exactly the point of drilling patterns). Growth edge is the **scheduling/capacity domain**: cleanly separating admission vs scheduling vs preemption, the full admission menu, and non-fungible fair-share.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **Double-assignment / ownership** — DB-fencing (only the lease-holder writes state). *Add:* scheduler assigns (worker never self-claims); fence the **GPU slot**, not just the job.
2. ⚠️ **Exactly-one *artifact*** — I tried "flip DB status → then upload," which is fragile (mid-upload lease expiry; status-set-before-artifact-exists bug). *Fix (got after one pointer — the Cloud IDE "S3 won't fence" lesson):* each attempt writes an **immutable attempt-scoped key** (`job/<id>/attempt/<attempt_id>/out.mp4`) → **fenced DB-promote** (`final_artifact_id`) → readers resolve via the **pointer**, never "latest object." Make the duplicate *harmless*, don't prevent it.
3. ✅ **Exactly-one *side effect*** — **billing off a CDC event** (fires from the committed DB transition, not worker-local) + cron backstop for a missed CDC event. *Must-add:* the consumer is **idempotent** (dedupe on transition version) — CDC is at-least-once and the cron can re-fire.
4. ⚠️ **Completion fence** — the promote must CAS on `lease_epoch == mine AND lease_expires_at > now AND state==RUNNING` (a worker can finish *after* its lease expired while reassigned).
5. ⚠️→✅ **Admission control (central: demand ≫ supply)** — I first answered **scheduling** (priority `ready_ts` delay — a good anti-starvation idea) and **preemption** (time-box + resume) instead. Got the **decoupled admission buffer** (buffer ≠ running set) after a nudge. *Complete the menu:* a buffer is still unbounded if you only hold — admission needs **accept / defer-with-ETA / reject-with-retry-after / downgrade-quality / shed-low-priority + per-tenant caps.** "Queue, don't drop" with an unbounded queue = "an outage with better branding."
6. ✅ **Fair scheduling (anti-hog + anti-starvation)** — priority delay with `ready_ts` so low-priority isn't dropped, just deferred.
7. ❌ **Fair-share tokens NOT fungible across GPU classes** — A100 ≠ H100 ≠ gang job → **dominant-resource fairness / per-class pools** + aging + gang-scheduling + preemption-by-tier. *Not raised.*
8. ⚠️ **Checkpointing economics + validity** — I had time-box + resume-from-partial. *Add:* **gate** checkpointing on remaining-runtime × preemption-prob × size (short jobs: just re-run); checkpoints are **versioned + validated** (model/container digest; refuse resume across incompatible versions; verify hash+manifest before advancing the pointer); RPO = interval + commit time.
9. ⚠️ **Honest ETA** — probabilistic range + confidence per class/tier/shape, not depth ÷ throughput. *Not raised.*
10. ⚠️ **Scheduler failover + spot storms** — rebuild the running-set by scanning leases+heartbeats; dampen spot-reclaim storms (retry budgets, jittered reschedule, spillover). *Not raised.*

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Exactly-one artifact = attempt-scoped immutable S3 keys + a DB-promoted pointer (S3 won't fence); exactly-one side effect = fire billing/notify from the terminal DB transition via CDC/outbox, idempotent, never worker-local."*
- *"Demand ≫ supply → admission control decoupled from the running set, with a real NO and a LESS: accept / defer-with-ETA / reject-retry-after / downgrade / shed + per-tenant caps."*
- *"Fair-share tokens aren't fungible across GPU classes, and checkpointing must earn its keep."*

## Pattern to drill
Correctness patterns are now **portable** (fencing, CDC/outbox, S3-won't-fence→DB-pointer — the last transferred straight from Cloud IDE). Growth edge is a **vocabulary/domain** one: keep **admission** (accept/defer/reject/downgrade/shed) distinct from **scheduling** (who runs next) distinct from **preemption** (checkpoint/resume) — I collapsed them at first.
