# Crib — Job Scheduler (Robinhood)

**The flagship. 67 Hello Interview reports vs 13 for the next question.** Both sources rank it
first. Asked at phone screen *and* onsite, levels Junior→Manager, last asked mid-July 2026.

⚠️ **The two sources state opposite guarantees.** 1point3acres says at-most-once per scheduled run;
Hello Interview says at-least-once with idempotency, names 10,000 jobs/sec at sub-2s precision, one
year of queryable history, and lists "promising exactly-once without idempotency" as its top red
flag. Do not walk in with one answer — §2 is how you reconcile them.

---

## THE SIX SENTENCES (if you read nothing else)

1. *"Before I design anything: what's the side effect, is a duplicate worse than a miss, does the
   downstream take an idempotency key, and what does 'retry' mean to you?"*
2. *"I can give you at-most-once **or** at-least-once request **issuance**. Nobody can give you
   exactly-once **delivery** across a network. Exactly-once **effect** has to be enforced where the
   effect happens."*
3. *"A database lease means one **current** owner. It does not mean one executor — an expired owner
   can still be alive and still call the broker."*
4. *"So the delivery contract is a property of the **job type**, not of the scheduler. Each job
   declares whether the downstream dedupes, which errors are retryable, and whether an unknown
   outcome may be retried."*
5. *"A known failure is not a known non-execution. A timeout can arrive after the broker accepted
   the order."*
6. *"At 10k dispatches/sec that's ~864M runs/day and ~315B a year, so active scheduling state and
   execution history cannot live in the same table."*

---

## 1. Summary (in plain terms)

Runs jobs on a schedule, including repeating ones. Each has a deadline; misses get recorded and the
logs stay readable. The interesting part is not running jobs — it is what you promise when a worker
dies at the worst possible moment.

## 2. The guarantee, decomposed (⭐ the core of the answer)

Four different things get called "once". Separate them at four boundaries and the question becomes
tractable:

| # | Guarantee | Where enforced | Achievable? |
|---|---|---|---|
| 1 | One logical **occurrence** per schedule slot | scheduler DB, unique key | ✅ yes |
| 2 | One **current lease** holder | claim CAS + epoch fencing | ✅ yes — but see below |
| 3 | Exactly one **request issuance** | the network | ❌ **never** |
| 4 | One **business effect** | **downstream** dedupe or reconciliation | ✅ only with cooperation |

⚠️ **#2 is weaker than it sounds and this is where candidates get caught.** It gives you one owner
whose *database writes* will be accepted. It does **not** give you one process capable of calling
the outside world. A worker can hold epoch 7, commit `ATTEMPTING`, pause, lose its lease, and wake
up later and still call the broker. Epoch fencing rejects its *database* write; it cannot un-send
its *request*. Exact-equality fencing is **necessary, not sufficient**.

Consequence: even `AT_MOST_ONCE` mode does not guarantee at most one execution. It guarantees **at
most one *original* attempt, with an outcome you may never learn.** Say it that precisely.

## 3. Reconciling the two sources: the delivery contract belongs to the job

Both sources are right about different jobs. ⛔ Do **not** make at-least-once the blanket default —
a duplicate liquidation or a duplicate cancellation can be materially harmful. Make it a declared
contract per job type:

```
delivery_contract per job_definition:
  downstream_idempotent      bool     -- does it honour a key, and for how long?
  idem_key_retention         interval -- must outlive every retry AND reconciliation
  retryable_error_classes    set
  unknown_outcome_retryable  bool     -- the at-most-once vs at-least-once switch
  reconciliation_api         bool     -- can I ask "did this happen?"
  duplicate_effect_risk      enum     -- what a double costs
```

- Downstream dedupes, unknown retryable → **at-least-once issuance** (HI's 67 reports).
- No dedupe, duplicate unacceptable → **at-most-once issuance**, uncertainty terminates in
  `UNRESOLVED` and pages (1p3a's framing).

⚠️ An idempotency key proves nothing on its own. It only works if the dedupe record and the effect
commit **atomically**, and if retention outlives every retry. Ask that.

One state machine serves both; the only difference is one reaper rule.

## 4. State machine

| From | To | Who | Guard |
|---|---|---|---|
| — | `PENDING` | materializer | `INSERT ON CONFLICT DO NOTHING` on occurrence identity |
| `PENDING` | `READY` | dep resolver | all dependency conditions satisfied |
| `READY` | `CLAIMED` | worker | CAS on `state` **and** `next_attempt_at <= :txTime` |
| `CLAIMED` | `ATTEMPTING` | worker | `state='CLAIMED' AND epoch=:myEpoch` + attempt row written **first** |
| `CLAIMED` | `READY` | reaper | lease expired — **safe, nothing was issued** |
| `ATTEMPTING` | `SUCCEEDED`/`FAILED` | worker | `state='ATTEMPTING' AND epoch=:myEpoch` |
| `ATTEMPTING` | `READY` | reaper | **only if `unknown_outcome_retryable`** |
| `ATTEMPTING` | `UNRESOLVED` | reaper | otherwise — never auto-retried |
| any live | `CANCELLED` | operator | audited, authz-gated |

The two `ATTEMPTING` reaper rows **are** the entire guarantee difference. One line of policy.

## 5. Fencing — where the epoch is actually *checked*

Minting does nothing. Every ownership-sensitive write carries **both** expected state and exact epoch:

```sql
-- claim (mint). Capture ONE database timestamp and reuse it across the transaction.
UPDATE runs SET state='CLAIMED', owner=:me, epoch=epoch+1,
                lease_expires_at = :txTime + interval '30 s'
 WHERE run_id=:id AND state='READY' AND next_attempt_at <= :txTime
RETURNING epoch;                                    -- :myEpoch

-- pre-dispatch / heartbeat / completion / failure — ALL of them:
UPDATE runs SET state='ATTEMPTING'
 WHERE run_id=:id
   AND state='CLAIMED'                              -- state AND epoch, never epoch alone
   AND epoch = :myEpoch                             -- ⭐ exact equality; >= accepts stale tokens
   AND lease_expires_at > :txTime;
-- 0 rows affected → I am fenced. Stop. Do NOT call the downstream.
```

- `epoch` is `bigint`. All identity columns `NOT NULL` — **Postgres unique constraints permit
  multiple NULLs**, so a nullable identity column silently defeats the constraint.
- Check affected-row count on every transition. Zero rows is a fencing event, not a no-op.
- One authoritative clock: the Postgres primary. `now()` is transaction-start (stable across
  predicates, which is usually what you want); `clock_timestamp()` is live but **not monotonic** and
  moves under NTP correction. Never use worker clocks for lease validity.
- ⛔ There is still a gap between the epoch check and the outbound call. Nothing closes it. That is
  why guarantee #4 exists.

## 6. Occurrence identity — a timestamp is not enough

```
UNIQUE (job_id, schedule_revision, occurrence_id, execution_generation)   -- all NOT NULL
  schedule_revision      immutable snapshot; a run pins the revision it was born under
  occurrence_id          canonical slot id derived from the schedule, not the wall clock
  execution_generation   0 = natural run; a backfill mints one, keyed by backfill_request_id
                         so a retried backfill API call does NOT create a second run
idem_key = job_id#schedule_revision#occurrence_id#execution_generation   -- stable; NEVER epoch
```

Also persist an **immutable execution snapshot** (payload, SLA, policy) on the run, or a job edited
while a run sits `PENDING` executes new payload under an old schedule.

**DST — state the policy, it's a favourite probe.** Materialize local time, the resolved UTC
instant, the IANA zone, the **fold choice** for fall-back (first occurrence, by convention), and the
tzdata version. Nonexistent spring-forward times shift to the next valid instant. **Never recompute
an already-materialized occurrence under newer tzdata.**

## 7. Scale — Postgres *is* on the dispatch path; say so honestly

⛔ HI's #1 red flag is a central cron polling the DB. But do not over-correct into claiming Postgres
is out of the hot path — the claim, the `ATTEMPTING` transition, heartbeats, and completion are all
per-dispatch writes. At 10k/sec that is 20k+ writes/sec before retries and heartbeats.

```
Redis (sorted set)  near-term ready index. score = due_at_ms. Rebuildable from Postgres —
                    losing it costs latency, not runs.
                    ⚠️ ZRANGEBYSCORE + ZPOPMIN is NOT an atomic due-pop — ZPOPMIN takes the
                    minimum regardless of whether its score is due. Use one Lua script doing
                    ZRANGEBYSCORE + ZREM atomically. Entries carry run_id, duplicates tolerated,
                    because Postgres is the authoritative filter.
Postgres            authoritative control plane, ON the dispatch path. Active runs partitioned
                    by shard and time; terminal runs moved out; attempts append-only.
History             separate columnar/KV store, partitioned (job_id, time_bucket), rollups,
                    TTL hot → cold archive. 315B rows/yr does not belong in the OLTP table.
Coordination        dispatcher shard assignment by lease/ZooKeeper so several run active/active.
```

- **Shard key:** `hash(job_id)` concentrates a single high-volume job on one shard. Shard on
  `run_id` unless per-job ordering is required; if it is, say so and accept the hot shard.
- **`shard` is logical routing, not physical partitioning.** `INDEX (shard, next_attempt_at)` is one
  index covering all shards — call it what it is, or actually declare table partitions.
- **Top-of-minute herd:** jitter the enqueue inside the slot, jitter retries, cap per-shard dispatch
  rate, apply backpressure on queue depth. Name it before you're asked.
- If Postgres must serve dispatch directly: partial index on due work + `FOR UPDATE SKIP LOCKED`.

## 8. Schema (control plane)

```
job_definitions   job_id PK, schedule_expr, timezone, schedule_revision, sla_seconds,
                  deadline_semantics (ALERT_ONLY|DISPATCH_CUTOFF|RETRY_CUTOFF),
                  delivery_contract jsonb, catchup_policy (SKIP|RUN_ALL),
                  max_concurrency, priority_class, enabled

runs              run_id PK
                  job_id, schedule_revision, occurrence_id, execution_generation   -- all NOT NULL
                  UNIQUE (job_id, schedule_revision, occurrence_id, execution_generation)
                  shard, state, epoch bigint, owner, lease_expires_at
                  scheduled_for, next_attempt_at, deadline
                  pending_deps, exec_snapshot jsonb, idem_key
                  sla_breached_at timestamptz NULL      -- ⭐ orthogonal to state
                  INDEX (shard, next_attempt_at) WHERE state='READY'
                  INDEX (shard, lease_expires_at) WHERE state IN ('CLAIMED','ATTEMPTING')

run_attempts      ⭐ append-only; the authoritative audit trail, not counters
                  attempt_id PK, run_id, attempt_no, epoch, worker_id,
                  request_id, request_digest, started_at,
                  outcome (CONFIRMED_ACCEPTED|CONFIRMED_REJECTED|UNKNOWN|LOCAL_NOT_SENT),
                  response_digest, ended_at
                  -- written BEFORE the call; from that moment treat it as possibly issued

dependency_edges  run_id, predecessor_run_id, condition (ON_SUCCESS|ON_TERMINAL|ON_SKIP)
dependency_sat    (run_id, predecessor_run_id) PK, satisfied_at, result
                  -- only the FIRST insert decrements pending_deps, same transaction

outbox            id PK, dedupe_id UNIQUE NOT NULL, run_id, payload, created_at, sent_at NULL
run_logs          → dedicated log store, keyed (run_id, seq). Not in the OLTP DB at volume.
```

⭐ **`sla_breached_at` is a flag, never a state.** If the SLA sweep writes `state='FAILED'` it can
overwrite a run that actually succeeded, and the worker's real success write then loses to fencing —
leaving the database permanently wrong about whether money moved. Lateness and outcome are
independent axes. And **define what the deadline means**: alert-only, dispatch cutoff, or retry
cutoff. Those are materially different systems.

## 9. The failure matrix (what you can actually observe)

⛔ Don't claim you know more than the protocol tells you. A 2xx may mean "accepted", not "money
moved". A connection reset may come from a proxy *after* the bytes crossed.

| Observation | Classify as | Action |
|---|---|---|
| Explicit business rejection | `CONFIRMED_REJECTED` | terminal, no retry |
| Client library guarantees not-sent | `LOCAL_NOT_SENT` | safe retry |
| Timeout / 5xx / reset / worker crash | **`UNKNOWN`** | contract decides: retry with same `idem_key`, or `UNRESOLVED` + reconcile |
| Ack that the *effect* is durable | `CONFIRMED_ACCEPTED` | `SUCCEEDED` |

The attempt row is written **before** the call, so an `UNKNOWN` is durable even if the worker never
returns. Counters can't do that — a worker can increment and crash before calling, or call and crash
before incrementing.

## 10. Dependencies, notifications, HA, operations

- **Dependency condition is data, not code.** Does a `FAILED` predecessor block, satisfy, or skip
  the dependent? Declare it per edge. Detect cycles at definition time. Handle the predecessor
  finishing *before* the dependent is materialized (satisfaction row inserted on arrival).
- **Notifications:** transactional outbox — failure transition and outbox row commit together; relay
  publishes at-least-once with a deterministic `dedupe_id` the notifier honours. Duplicate pages are
  *recoverable* (dedupe anyway, alert fatigue is real); a dropped SLA alert is not.
- **HA:** ⛔ "writes go to the primary" is not an HA design. Fence the old primary on failover;
  dispatcher leadership is lease-fenced so a partitioned leader can't double-promote; Redis loss is
  a latency event, rebuilt from Postgres. ⭐ **Failover produces `UNKNOWN`; it does not resolve
  ambiguity.** Re-reading the run tells you what the surviving database knows, never whether the
  broker acted.
- **Operator actions are first-class, audited, authz-gated:** cancel, pause, force-skip, reconcile,
  replay. ⚠️ Replaying an `UNRESOLVED` run is the most dangerous button in the system — gate it on a
  live downstream idempotency key or a successful reconciliation query.
- **Fairness, actually specified:** weighted fair queues per priority class, per-class token buckets,
  bounded queues. Not "a priority queue plus a floor" — that's a slogan.

## Traps

- ⛔ "Exactly-once execution." ⛔ Unqualified "at-most-once execution."
- ⛔ "One owner" as if it meant one executor.
- ⛔ "The state machine tells me whether it dispatched." It never does.
- ⛔ "Skipping is safer than duplicating" — depends entirely on the operation.
- ⛔ At-least-once as a blanket default.
- ⛔ Fencing minted but never checked; `>=` instead of `=`; epoch without state; nullable identity columns.
- ⛔ Claiming Postgres is off the dispatch path while workers claim in Postgres.
- ⛔ `ZPOPMIN` as a due-time pop.
- ⛔ SLA breach written into `state`.
- ⛔ Attempt *counters* as the audit trail for money movement.
- ⛔ Reciting any of this before establishing requirements. **Requirements first, always.**

## Reuse

`project_cicd_scheduler_design` (exactly-once committed vs executed — same argument),
`project_webhook_delivery_design` (DB retry scheduler + outbox + 3 IDs),
`project_sora_scheduling_design` (exactly-one side effect, slot fencing),
`project_chess_design` (exact-epoch fencing, clock-as-data),
`project_cloud_ide_design` (kill-before-route).
