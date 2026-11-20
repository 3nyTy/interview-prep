# Mock Scorecard — Q3: CI/CD Scheduler

**Date:** 2026-07-11 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/03-cicd-scheduler.md`](../detailed/03-cicd-scheduler.md)

**Verdict:** Good product/orchestration instincts and I reason well under pushback — but the *defining* idea of the problem (fencing + honest exactly-once) came out only under heavy questioning. I leaned on racy fixes ("check status then retry", "kill the VM") before reaching the real primitive. This trio recurs across ≥4 of my designs; it must be the *first* thing out of my mouth on any "workers can die" problem.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **Control plane** — API GW → processing service → persist run metadata → return `build_id`; status/steps via a GET.
2. ✅ **Ordered-step orchestration** — chose **Step Functions** (a valid managed orchestrator). *Add:* say what it fences (the **task-token callback** — a stale worker's old token is rejected) and what it doesn't (the external side effect).
3. ⚠️ **Source of truth = state store with conditional writes (CAS)** — I had a DB for state; *fix:* every transition must be a **conditional write** gated on `(status, attempt)`, which is what makes transitions race-free without locks.
4. ❌ **Fencing** — *fix:* mint a **monotonic `attempt` number** on each (re)assignment; worker stamps it on every write; DB accepts **only if `attempt == current` (exact match, never `>=`)**. A thawed zombie writing with the old attempt is **rejected** → can't corrupt the run. *This is the crux and I needed full teaching.*
5. ❌ **Idempotency key for the side effect** — *fix:* `run_id # seq # operation` (keyed on **position, not attempt**); downstream (deploy/charge) dedupes → the duplicate side effect is a **no-op**. Fencing protects *my DB*; the idempotency key protects *prod*.
6. ❌ **Honest "exactly-once"** — *fix:* promise **exactly-once *committed* result + at-least-once execution + idempotent side effects**. Never "exactly-once execution." I dodged this question by proposing a mechanism instead of stating the guarantee.
7. ⚠️ **Liveness = lease + Reaper** — I had TTL slot-release (lease-like); *fix:* running step holds a **renewable lease** (heartbeat); a **Reaper** finds expired leases, **bumps the attempt**, and reassigns. The lease decides *who tries*; fencing decides *whose write counts*.
8. ❌ **Next-step scheduling + backstop** — *fix:* advance via a **change-stream/event**, but add a periodic **Reconciler** full-sweep to repair stuck runs (streams drop/lag). (Step Functions covers ordering here, but know the pattern.)
9. ✅ **Failure classification** — got it unprompted: a failed integration test is a **successful execution with a user-fail result** (report, don't retry) vs **infra failure** (retry). *Add:* **cap infra retries** and define the ambiguous case (user code OOMs the box vs host dies).
10. ✅ **Fairness** — per-tenant concurrency cap, book-slot-before-run, **TTL slot release** (crash-safe; the lease pattern again).
11. ❌ **Work-conserving fair-share** — *fix:* static caps waste the fleet (10k queued runs at 3am, everyone else idle, machines sit unused). Use **Deficit Round Robin / weighted fair queuing** sharded by tenant: burst into idle capacity, throttle back the instant another tenant appears.
12. ❌ **Cancellation as first-class state** — *fix:* `RUNNING → CANCELLING → CANCELLED` with its own reaper; the claim/continue conditional write also checks `cancel_requested == false` so a mid-flight cancel isn't lost.
13. ⚠️ **Isolation** — I had "a VM per step"; *fix:* be specific — **Firecracker microVM** (arbitrary untrusted user code), **per-run short-lived creds** (STS/OIDC), **deny-by-default egress**, tenant-isolated cache.
14. ✅ **Storage** — partition by `run_id`; archive cold runs (>7d) to a data lake. *Add:* don't key the lease index on bare `status=RUNNING` (one hot partition) — **shard into buckets**.
15. ✅ **Observability** — sensible infra + business metrics with alarms.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"You can't prevent the double-run — you make it harmless. Fencing (exact-match attempt CAS) rejects the zombie's write to my DB; a stable `run_id#seq#operation` idempotency key makes the duplicate deploy a no-op."*
- *"So I promise exactly-once committed, at-least-once execution, idempotent side effects — not exactly-once execution."*
- *"The lease decides who tries; the fencing token decides whose result counts."*

## Pattern to drill (shared with Chess, Slack, Sora, Cloud IDE)
Any "workers/servers die mid-work" problem → **fencing + idempotency key + honest exactly-once**, stated *first*. Stop reaching for "check-then-retry" or "kill the VM" — both are racy under partition.
