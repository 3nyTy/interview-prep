# Cheat Sheet — Multi-Tenant CI/CD Workflow Scheduler

🔗 Diagram: https://excalidraw-production.a.musta.ch#fd36820f-e85f-4a0f-a7e8-686978be26c8

## 1. Summary (in plain terms)
A system that runs your build → test → deploy steps in order whenever you push code, for many teams at once. The hard part isn't running steps — it's making sure each step's **result is recorded exactly once** even when machines crash, time out, and retry.

## 2. Key deep dives (plain terms)

- **Don't promise "runs exactly once" — you can't.** A machine might run a step, get cut off before reporting, and a retry runs it again. What you *can* promise: the committed **result** is recorded once (via fencing), and you *try hard* to run it only once (a revocable lease). If a step has real side effects (a deploy), the *user* must make it safe-to-repeat using a stable key like `run_id#seq#operation`.

- **Fencing token:** each attempt gets a number. The database only accepts a write whose number matches the *current* attempt. A zombie old worker that wakes up late gets rejected. ⚠️ The match must be **exact (`=`), never `>=`** — `>=` lets a stale worker through.

- **DynamoDB is the source of truth; SQS is just an alarm clock.** Every state change is a conditional write (only succeeds if the world is as expected). SQS only says "wake up and look" and is deleted on claim — it never tracks who's alive.

- **Liveness = lease + Reaper.** A running step holds a time-limited lease; a "Reaper" reclaims steps whose lease expired (worker died).

- **Scheduling = change-stream + a backstop.** A DB change-stream (Streams → Lambda) triggers the next step. A periodic **Reconciler** is the safety net that repairs anything the stream dropped or stranded.

- **Fairness:** Deficit Round Robin per tenant so one busy team can't starve everyone else.

- **Cancellation is a real state** (`RUNNING → CANCELLING → CANCELLED`), and the claim check includes "not cancelled."

- **Classify failures:** "user's code failed" vs "our infra failed" are different outcomes.

## 3. Database schema (DynamoDB)
```
STEPS / RUNS table (the state machine):
  PK = RUN#<run_id>
  SK = STEP#<seq>
  attrs: status (PENDING|RUNNING|CANCELLING|CANCELLED|USER_FAILED|INFRA_FAILED|DONE),
         attempt            # fencing token, minted via atomic ADD
         lease_owner, lease_expiry,
         cancel_requested,  # claim CAS requires this == false
         tenant_id, ...
  → all transitions are conditional writes (CAS on status/attempt)

Sharded lease GSI (so the Reaper can scan only RUNNING work without a hot key):
  GSI1PK = RUNNING#<bucket>          # NOT bare status=RUNNING (hot partition)
  GSI1SK = lease_expiry

User-side idempotency (the user owns side-effect safety):
  side-effect key = run_id#seq#operation   # stable across retries; NOT 'attempt'
```
