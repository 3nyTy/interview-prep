# System design — 3 problems

Job Scheduler is the flagship. It is the only page in the bank with a **full free
write-up**, and it is marked high frequency. Two separate Job Scheduler entries exist with
different framings — treat them as one topic with two angles.

---

## 1. Job Scheduler System Design (the detailed entry)

Source: `company/robinhood/job-scheduler-design` — free preview, full content available
Difficulty: Medium | Duration: 60 min | Frequency: Medium-to-High
Stage: **phone screen and onsite system design** | Last asked: **2026-07-20**
Tags: `system-design, job-system, scheduling, idempotency, retry, distributed-systems`

### Core requirements (verbatim from the source)

- Schedule and execute jobs of a specified type, each with an SLA. Record errors when
  deadlines are missed and allow log inspection.
- Support recurring jobs (tasks that run at regular intervals).
- Guarantee **at-most-once** execution per scheduled run.
- Handle diverse failure scenarios gracefully.

### Key follow-up areas (verbatim)

- **Worker failure mid-execution** — how do you maintain at-most-once semantics and keep
  retrying?
- **Recurring job failures** — design reliable notification delivery even when system
  components fail.
- **Retry strategy** — how do backoff and retries preserve the at-most-once guarantee?

### Interview notes (verbatim from the source)

> "The round is open-book with a stable prompt, so early differentiation comes from
> proactively addressing reliability and fault tolerance. Candidates who defer these
> concerns often encounter follow-up traps and time pressure."

> Common at-most-once implementation: "conditional writes (compare-and-set on a claim/lease
> record) so two workers can't both execute the same run."

> "The scheduler often handles a single job type only — avoid over-generalization."

### Preparation guidance (verbatim)

- Build an end-to-end reference design (queue, workers, durable job/lease store, notifier)
  and rehearse at-most-once claims via conditional writes.
- Pre-script failure handling: worker crashes, lost notifications on recurring-job failure,
  and retry/backoff strategies.
- Study Hello Interview / ByteByteGo job-scheduler material.

### The one thing to get right

**At-most-once, not exactly-once.** This is unusual and it is deliberate. Most scheduler
questions ask for at-least-once. At-most-once means: if you are unsure whether a run
executed, **do not run it again**.

The tension the follow-ups probe: a worker claims a run, starts executing, then crashes.
You cannot distinguish "crashed before the side effect" from "crashed after the side
effect". Under at-most-once you must not blindly retry.

Answers that work:
- The claim record has a terminal state written **before** the side effect
  (`ATTEMPTED`), so a crashed run is never retried — it is marked failed and surfaced.
- Or: retry is allowed only when the worker recorded `CLAIMED` but provably never reached
  the side-effect call, which requires the side effect itself to be idempotent — at which
  point say so explicitly, because that is the honest trade-off.
- Do not claim both at-most-once and unlimited retry without addressing this. That
  contradiction is the follow-up trap the notes warn about.

Lease mechanics — the compare-and-set the source names:

```
UPDATE runs SET owner = :worker, lease_until = now() + 30s, state = 'CLAIMED'
WHERE run_id = :id AND (state = 'PENDING' OR (state = 'CLAIMED' AND lease_until < now()))
```

The conditional predicate is what makes it safe. Two workers race, one row is updated, the
loser's update affects 0 rows and it backs off.

### Structure to bring

1. **Job definition store** — job type, schedule, SLA, retry policy.
2. **Run materializer** — turns a recurring definition into concrete `run` rows with a
   `scheduled_for` timestamp. One row per occurrence, uniquely keyed on
   `(job_id, scheduled_for)`. This is what prevents duplicate runs at the source.
3. **Dispatcher** — polls for due runs, or a durable timer queue.
4. **Lease/claim store** — the compare-and-set above.
5. **Workers** — claim, heartbeat to extend the lease, write terminal state.
6. **SLA monitor** — separate sweep that finds runs past their deadline and records the
   error. Call this out; the requirement asks for it explicitly and candidates forget it.
7. **Notifier** — with an outbox, because the follow-up asks about lost notifications.

Say "one job type" out loud and keep the design narrow. The source explicitly warns against
over-generalization.

### Reuse from existing prep

This overlaps heavily with work already done. Pull from:
- `project_cicd_scheduler_design` — exactly-once *committed* vs *executed* distinction
- `project_webhook_delivery_design` — SQS + DynamoDB retry scheduler, the outbox pattern
- `project_sora_scheduling_design` — slot fencing, exactly-one side effect via outbox
- `project_cloud_ide_design` — fencing lease, kill-before-route

The fencing-token discipline from the Sora and Chess designs is directly reusable: an
expired lease holder must be fenced out by an **exact epoch match**, not a comparison.

---

## 2. Job Scheduler (the state-machine entry)

Source: `d7f93d7c-5482-4b4a-a828-ffa4e14efc86`

### Statement (verbatim)

> "Design a job scheduling system that efficiently manages a large number of tasks using a
> state machine. The system must handle tasks with various priorities and dependencies and
> maintain high efficiency under heavy loads."

Required elements: architecture design, task scheduling logic, key use cases, and expected
handling approaches.

### How this differs from entry 1

Entry 1 emphasizes reliability semantics. This one emphasizes **state machine, priorities,
and dependencies**. Prepare the extra pieces:

**Explicit state machine.** Draw it. Name every transition and who triggers it:

```
PENDING → READY → CLAIMED → RUNNING → SUCCEEDED
                     ↓         ↓    ↘ FAILED → (retry) → READY
                  EXPIRED   TIMED_OUT        ↘ DEAD_LETTER
                                    CANCELLED
```

State lives in one durable place. Transitions are conditional writes. Never infer state
from a queue's contents.

**Dependencies.** A run becomes `READY` only when all upstream runs reached `SUCCEEDED`.
Keep a pending-predecessor counter per run and decrement it on each upstream success —
this is the same Kahn's-algorithm counter as the DAG coding problems in `01`. Point that
connection out if both come up in the same loop; it reads well.

**Priorities.** Priority queue per job class, plus a floor on low-priority throughput so
starvation cannot happen. Name starvation before you are asked.

**Heavy load.** Shard the due-run scan by hash bucket so dispatchers do not contend.
Batch claims. Bound the in-flight set per worker.

### Paywalled

Test cases, full I/O spec, constraints, examples, solutions, difficulty, duration, tags,
frequency, round type.

---

## 3. Design a Calendar Booking System

Source: `f1096c0c-18ab-463d-a820-79fd45858a0a`

### Statement

You receive sequential calendar booking requests as intervals `[start, end)` — start
inclusive, end exclusive, integer time. Process each booking in order.

### Requirements

1. Design a data structure with a `book(start, end)` method.
2. Return `false` if the interval overlaps an existing booking. Otherwise store it and
   return `true`.

### Constraints

- Up to 1 x 10^5 `book` calls
- 0 ≤ start < end ≤ 1 x 10^9

### Example

```
book(10, 20) → true
book(15, 25) → false     // overlaps [10,20)
book(20, 30) → true      // end is exclusive, so it does not overlap
```

### Approach

LeetCode 729 (My Calendar I). This is a coding problem despite the "design" framing.

`TreeMap<Integer, Integer>` from start to end. For a new `[s, e)`:

```java
Integer prevStart = cal.floorKey(s);
if (prevStart != null && cal.get(prevStart) > s) return false;
Integer nextStart = cal.ceilingKey(s);
if (nextStart != null && nextStart < e) return false;
cal.put(s, e);
return true;
```

O(log n) per call. The two-sided check is the whole solution: the interval starting at or
before `s` must end at or before `s`, and the next interval must start at or after `e`.

The `1 x 10^9` bound rules out any array or bucket approach. It exists to force the
balanced-tree answer.

### Traps

- **Half-open intervals.** `book(20, 30)` after `book(10, 20)` succeeds. Use strict `>`
  and `<`, not `>=` and `<=`. Getting this backwards fails the given example.
- Only `floorKey` is not enough — you need the `ceilingKey` check too, or an interval fully
  contained inside the new one is missed.

### Likely follow-ups

- **My Calendar II** — allow double booking, reject triple. Keep a second map of overlaps,
  or use a sweep-line delta map.
- **My Calendar III** — return the maximum concurrent booking count. Delta map with a
  running prefix maximum.
- Concurrency: two `book` calls racing. The check-then-put is not atomic. Needs a lock or
  a single-writer design.

### Paywalled

Difficulty, duration, tags, test cases, Python/Java solutions, follow-ups, round metadata.
