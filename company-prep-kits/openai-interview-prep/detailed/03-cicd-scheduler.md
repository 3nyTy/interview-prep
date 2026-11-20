# CI/CD Workflow Scheduler — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#fd36820f-e85f-4a0f-a7e8-686978be26c8

## What we're building
A system that, whenever a developer pushes code, runs their pipeline — a sequence of steps like *build → test → deploy* — in order. Many teams ("tenants") share the system at once. If a step fails, the run stops (or follows the failure rules). The whole thing must keep working even when the machines running the steps crash, freeze, or get disconnected.

## Why this is harder than it looks
The naive version — "a queue of jobs, workers pick them up and run them" — falls apart on one question: **what happens when a worker dies halfway through a step?** You don't actually know if it finished. If you retry, you might run the step twice. If you don't, you might skip it. In CI/CD that can mean *deploying twice* or *never deploying*. So the entire design is really about **getting "did this step happen?" right under failure.**

## The single most important idea: be precise about "exactly once"
People love to say "exactly-once execution." It's a lie, and a Staff interviewer will pounce on it. Here's the honest breakdown:

- You **cannot** guarantee a step's *code* runs exactly one time. A worker can run it, then die before reporting back; a retry then runs it again. The world has already changed twice.
- What you **can** guarantee is that the *recorded result* is committed exactly once, and that you *try hard* to run it only once.

So we make two separate promises:
1. **Exactly-once committed result (fenced).** Even if the step ran twice, only one result is ever accepted into the system of record.
2. **Best-effort single execution (revocable lease).** We hand a worker a time-limited "lease" to run the step; if it goes silent, we take the lease back and give it to someone else.

And for steps with real side effects (like "charge the customer" or "deploy to prod"), the *user's* step has to be safe to repeat. We give them a stable key — `run_id#seq#operation` — that's identical across retries, so their own logic can detect "I already did this." Crucially the key uses the step's position (`seq`), **not** the attempt number — otherwise every retry looks like new work.

## How "fencing" works (the core trick)
Every time we (re)assign a step, we mint an **attempt number** that only goes up: 1, 2, 3… This is the "fencing token."

- The worker carries its attempt number with every write it makes.
- The database only accepts a write if the number **exactly equals** the current attempt on record.
- So if worker #1 freezes, we bump to attempt #2 and give it to worker #2. If the frozen worker #1 wakes up later ("zombie") and tries to write its result, the DB sees attempt #1 ≠ current #2 and **rejects it**. The zombie can't corrupt anything.

⚠️ The match must be **exactly equal (`=`)**, never "greater-or-equal (`>=`)". With `>=`, a stale worker's number would still pass and defeat the whole point. (This exact bug shows up across several designs — it's worth saying out loud.)

## The pieces and how they fit
- **DynamoDB = the source of truth.** Every step's state lives here (`PENDING / RUNNING / CANCELLING / CANCELLED / USER_FAILED / INFRA_FAILED / DONE`). Every state change is a **conditional write** — it only succeeds if the world is exactly as the worker expects (right state, right attempt). This is how we get atomic, race-free transitions without locks.
- **SQS = just an alarm clock.** It tells a worker "wake up, there's something to look at." That's it. The message is deleted the moment a worker claims the work. SQS is *never* used to track who's alive or to hold the real state — message queues are unreliable for that (duplicates, redelivery, ordering). The DB is the truth; SQS only nudges.
- **Liveness = lease + Reaper.** A running step holds a lease with an expiry time, and the worker keeps extending it ("I'm still alive"). A background **Reaper** process scans for leases that expired and resets those steps to be picked up again. That's how a dead worker's step gets rescued.
- **Scheduling the next step = change-stream + a backstop.** When a step finishes, that DB change flows through **DynamoDB Streams → a Lambda**, which kicks off the next step. But streams can occasionally drop or lag, so a periodic **Reconciler** sweeps the whole system looking for runs that are stuck or steps that should have advanced, and repairs them. The stream is the fast path; the reconciler is the safety net. (Don't rely on the stream alone — that's a real gap an interviewer will probe.)

## Fairness between teams
If one team pushes 10,000 builds, they shouldn't freeze everyone else. We use **Deficit Round Robin** sharded by tenant: each tenant gets a fair slice of the global capacity, and no single tenant can monopolize the workers. Think of it as round-robin where each team has a "budget" that refills over time.

## Cancellation is a real thing, not an afterthought
Users cancel runs. So "cancelled" is a **first-class state**, not just deleting a row: `RUNNING → CANCELLING → CANCELLED`, with its own reaper to clean up workers that were mid-step. And when a worker tries to claim or continue a step, the conditional write *also* checks `cancel_requested == false` — so a cancel that lands mid-flight is honored, not lost.

## Failure classification
"The step failed" isn't enough. We distinguish:
- **USER_FAILED** — their tests failed, their build broke. Not our problem to retry; report it.
- **INFRA_FAILED** — our machine died, network blipped. *This* is what we retry.
Mixing them up means either retrying doomed builds forever or giving up on transient glitches.

## Where the steps actually run
Steps run as containers, but because they execute arbitrary user code, each runs in a **Firecracker microVM** (lightweight VM-level isolation) on **EKS**, scaled by **Karpenter**. Each run gets short-lived credentials (per-run STS/OIDC), deny-by-default network egress, and a tenant-isolated cache so one team's build can't poison another's.

## Database schema (DynamoDB)
```
STEPS / RUNS table (the state machine):
  PK = RUN#<run_id>
  SK = STEP#<seq>                 # steps ordered within a run
  attrs:
    status        # PENDING|RUNNING|CANCELLING|CANCELLED|USER_FAILED|INFRA_FAILED|DONE
    attempt       # the fencing token — minted via atomic ADD, only increases
    lease_owner, lease_expiry      # liveness
    cancel_requested               # claim CAS requires this == false
    tenant_id
  → every transition is a conditional write (CAS on status AND attempt)

Sharded lease index (so the Reaper finds expired leases without a hot partition):
  GSI1PK = RUNNING#<bucket>        # bucketed; NOT bare status=RUNNING (that's one hot key)
  GSI1SK = lease_expiry            # Reaper queries "expired before now"

User-side idempotency key (the user makes side effects safe):
  run_id#seq#operation             # stable across retries; NOT keyed on attempt
```

## If the interviewer pushes further
- *"Why not just use the lease as the source of truth?"* Because a lease is liveness, not correctness. The fencing token + conditional write is what prevents a zombie from committing — the lease only decides *who tries*.
- *"What if the change-stream event is lost?"* The Reconciler catches it. Streams are best-effort enough that you need a periodic full-sweep backstop, or runs silently stall.
- *"Hot partition?"* Don't key the lease index on `status=RUNNING` (one value = one hot partition). Shard it into buckets.
