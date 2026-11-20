# Design a Job Scheduler

- Source: https://www.hellointerview.com/community/questions/job-scheduler-cron-history/cm7w828yv00sejmejlmofwtvp?company=Robinhood
- Type: system design
- Robinhood reports: **67** — last asked Mid July, 2026
- Levels reported: Junior–Manager (the widest range in the set)
- Also asked at: DoorDash, NVIDIA, Palo Alto Networks, and 17 more companies
- Captured: 2026-08-14, detail page read in full

The flagship Robinhood system design question. 67 reports against 13 for the next one. The
1point3acres bank ranks it first as well, and it is the only page there with a full free
write-up (`../04-system-design.md`).

## Prompt as published

> Design a distributed job scheduler that can handle high-throughput job processing (10,000+
> jobs per second), support both scheduled (cron-based) and ad-hoc job execution, include
> retry mechanisms for failed jobs, and maintain execution history for up to one year.

Framing from the page: think Airflow, Quartz, or AWS EventBridge/CloudWatch Events — trigger
and run 10,000 jobs per second, with reliable execution semantics and a year of searchable
history.

What it tests, per the page: whether you separate scheduling from execution, design for high
throughput and bursty top-of-minute load, and reason about delivery guarantees (at-least-once,
idempotency, retries). Interviewers also look for pragmatic data modelling for long-term
history (hot against cold), worker lifecycle management, and ways to avoid single points of
failure while the scheduler stays effectively stateless.

## Common functional requirements

- Users can create jobs that run immediately, at a specific future time, or on a recurring
  schedule with cron expressions.
- Users can submit ad-hoc job runs in addition to scheduled executions.
- Users can view live job status and detailed execution history — timestamps, attempts,
  outcome, logs — for up to one year.
- Users can configure execution policies: retries with backoff, timeouts, and optional
  concurrency limits per job.

## Common deep dives

1. How do you dispatch 10,000 scheduled jobs per second with sub-2s precision without
   overloading your data stores?
2. How do you provide at-least-once execution while you prevent duplicates from causing harm?
3. How do you manage worker lifecycle, scaling, and safe draining at this throughput?
4. How do you store and query one year of execution history cost-effectively while common
   queries stay fast?

## Patterns and technologies named on the page

Patterns:

- **Managing long-running tasks** — jobs run for seconds to minutes and must survive worker
  restarts and preemption. Gives leases and visibility timeouts, heartbeats for renewal,
  retries with exponential backoff, and dead-letter handling.
- **Scaling writes** — scheduling and history ingest together exceed 10k writes/sec. Needs
  partitioning, time bucketing, and write-optimised schemas to avoid hot partitions.
- **Dealing with contention** — cron workloads spike at boundaries, for example at :00 of
  every minute, and after outages. Needs backpressure, jittered retries, distributed locks or
  shards, and fair scheduling to prevent thundering herds.

Technologies:

- **DynamoDB** — for both the job catalogue and high-throughput execution history. Partition
  by `job_id` and time buckets, GSIs for status and time queries, TTLs for lifecycle before
  archiving.
- **Redis** — sorted sets or streams for the near-term, time-ordered ready queue and delayed
  scheduling. `ZADD`/`ZPOPMIN` or `XREADGROUP` give low-latency atomic operations for
  sub-second dispatch.
- **ZooKeeper** — leader election, membership, and shard coordination, so several schedulers
  run active/active and divide time windows or key ranges without duplicate promotions.

Similar problems the page points at: Job Scheduler (its own breakdown), Web Crawler (pull-based
workers, leases, retries, dedupe), Ad Click Aggregator (sustained write throughput, time-bucketed
storage).

## Red flags to avoid

- **A single central cron that polls the database for due jobs.** Creates hot partitions, lock
  contention, and missed precision during spikes. It cannot sustain 10k/sec with low jitter.
- **Promising exactly-once execution without idempotency or deduplication.** Crashes and
  retries make duplicates inevitable. Without idempotent handlers or dedupe keys you
  double-send email, double-charge customers, or corrupt state.
- **No backpressure or capacity planning for bursty schedules.** Ignoring queue depth, rate
  limits, and autoscaling leads to thundering herds at minute boundaries and cascading
  retries.

## Report timeline (Robinhood only)

| When | Level |
|---|---|
| Mid July, 2026 | Senior |
| Early July, 2026 | Senior |
| Early July, 2026 | Mid-level |

Three rows are shown and the pager offers "Show 64 More", which confirms the 67 Robinhood
reports on the listing. None of the three visible rows carries a candidate note.

Note the levels: Senior twice and Mid-level once. The listing reports the question across
Junior–Manager, so it is not a Staff-specific question — it is the one everybody gets.

## The conflict with the other source — resolve this before the loop

1point3acres states the requirement as **at-most-once** per scheduled run, and calls that
choice deliberate and unusual. This page states **at-least-once** with idempotency, and lists
"promising exactly-once without idempotency" as a red flag.

Both describe the same failure: a worker claims a run, starts, then crashes, and you cannot
tell whether the side effect completed.

- Under at-most-once you must not retry. Write a terminal `ATTEMPTED` state **before** the side
  effect, so a crashed run is surfaced as failed and never repeated.
- Under at-least-once you retry and make the handler idempotent with a dedupe key on
  `(job_id, scheduled_for, attempt)`.

Ask which guarantee the system needs, then explain why the other is wrong for it. Also confirm
the scale: 1point3acres describes one job type, this page describes 10,000 jobs per second.

## Coverage in existing prep

Substantially covered. Reuse:

- `memory/project_cicd_scheduler_design.md` — exactly-once *committed* against *executed*
- `memory/project_webhook_delivery_design.md` — SQS + DynamoDB retry scheduler, outbox
- `memory/project_sora_scheduling_design.md` — slot fencing, exactly-one side effect via outbox
- `memory/project_cloud_ide_design.md` — fencing lease, kill-before-route

The fencing discipline from the Sora and Chess designs applies directly: an expired lease
holder is fenced out by an **exact epoch match**, not a comparison.

New here, and not in the 1point3acres write-up:

- 10,000 dispatches/sec at sub-2s precision — needs the Redis sorted-set ready queue plus
  sharded dispatchers, not a database poll.
- One year of queryable history — hot and cold tiering, time-bucketed partitions, TTL then
  archive.
- Top-of-minute contention — jitter and backpressure, named before you are asked.

## Status

Prompt, requirements, deep dives, patterns, technologies, red flags, and timeline captured.
Design not written. The guarantee conflict is the one open decision.
