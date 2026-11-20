# Webhook Delivery System — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#346b073a-2def-4d8c-a9fc-42ada94048bf

## What we're building
When something happens in our platform (e.g. "payment.succeeded"), we notify our customers by making an HTTP POST to a URL they registered. At scale: **1 billion events per day.** Customers' servers are flaky and go down, so we must retry with increasing delays for up to 24 hours, never lose an event, and never spam them with endless duplicates.

## Why it's hard
The naive design — "for each event, POST to the customer; if it fails, put it back on the queue" — breaks in several specific ways at this scale. Each break teaches a lesson. Let me walk through them, because the *lessons* are the interview.

## The end-to-end flow
1. An event happens → **Ingestion API** receives it → drops it on an **SQS** queue.
2. **Fan-out workers** look up "who subscribed to this event type?" (subscriptions in **DynamoDB**, hot-cached in **Redis**), and create one **delivery task** per subscriber.
3. Each delivery task goes onto a **delivery queue** (there are separate queues: fresh / retry / quarantine).
4. **Delivery workers** pick up a task and POST to the customer through an **egress proxy** (which enforces security — more below).
5. Success → mark done. Failure → schedule a retry.

## Lesson 1: Don't use SQS's visibility timeout for retry backoff
The tempting trick: "to retry in 30 minutes, just set the message's visibility timeout to 30 minutes" (it stays hidden, then reappears). **This doesn't work at scale**, and explaining *why* is a strong signal:

- A message hidden by visibility timeout is still "in flight," and SQS has a **soft limit of ~120,000 in-flight messages per queue**.
- By Little's Law (in-flight = arrival rate × delay), if you want long delays for a meaningful fraction of 1B/day events, you blow past that 120K limit almost instantly. The math caps you at roughly **~100 seconds** of delay across the fleet — nowhere near 24 hours.

**Correct design:** a durable **RetrySchedule table in DynamoDB**, organized into time buckets:
```
PK = <time_bucket>#<shard>      # e.g. the minute the retry is due
SK = <due_timestamp>#<delivery_id>
```
A **poller fleet** sweeps "what's due in this bucket now" and re-enqueues those deliveries. This scales to 24h (or any) delay because it's just rows in a table, not in-flight messages. Visibility timeout is then used only for its real purpose: keeping *one current attempt* from being picked up by two workers at once (extended via heartbeat while a delivery is in progress).

## Lesson 2: The retry schedule must be its own table
Don't make it a GSI on the live delivery-state table. If you do, a retry storm (a popular customer goes down, millions of deliveries all need rescheduling) amplifies writes onto your hot path and couples two very different workloads. Keep them physically separate.

## Lesson 3: "Write the row, then enqueue" can lose events
If you write the delivery state to DynamoDB and *then* put it on SQS, a crash in between means a delivery that exists in the DB but never got queued (or vice versa). The fix is the **transactional outbox**: in one atomic `TransactWriteItems`, write the state row **and** an outbox row. A dispatcher then enqueues from the outbox. Now the two can't diverge.

## Lesson 4: Three different IDs (this is the heart of the design)
Conflating identity is "the original sin" here. You need three distinct IDs:

- **`event_type`** — used for *routing*: "who subscribed to `payment.succeeded`?"
- **`event_instance_id`** — used for *ingestion dedupe*: if the same event is submitted twice, we only process it once.
- **`delivery_id = hash(version, tenant, event_instance_id, subscription_id)`** — used for *customer-side dedupe*. It's **stable across all retries and across fan-out**, so the customer can safely ignore a duplicate POST by remembering delivery IDs they've already handled.

Why three? Because one event can fan out to many subscribers (each needs its own delivery identity), and each delivery can be retried many times (the identity must stay the same across retries). A single "event_id" can't serve all three roles.

## Lesson 5: Rescue stuck deliveries
A delivery marked "delivering" whose worker died would otherwise be stuck forever. Each in-progress delivery has a **lease** (`attempt_lease_until`, `worker_id`). If the lease expires, another worker can **take it over** (claim it via a conditional write). Same lease-takeover pattern as the other designs.

## Lesson 6: Avoid hot partitions
- A "celebrity" event type with millions of subscribers would hammer one partition → shard subscriptions as `event_type#shard`.
- Delivery logs are write-sharded by `webhook_id#day#bucket`.
- Never key a table on a low-cardinality `status` field (e.g. `status=pending`) — every pending row lands on one partition.

## Lesson 7: At-least-once, and don't log to DynamoDB
- **Exactly-once over HTTP is impossible** (you POST, the customer processes it, but the ACK is lost — did it happen? You must retry, so they might get it twice). So we promise **at-least-once** and give customers the stable `delivery_id` to dedupe on.
- Attempt history is ~1.3 billion writes/day — **don't** put that in DynamoDB. Stream it to **Firehose → S3 → Athena** for cheap analytics/audit.

## Lesson 8: Security (SSRF) at connect time, not just registration
Customers give us URLs, which we then connect to from inside our network. A malicious URL could point at internal services (`169.254.169.254` metadata endpoint, internal IPs) — that's a Server-Side Request Forgery attack.

- Validate at **registration** (first gate) AND, critically, at **connect time** via the egress proxy: resolve the hostname, validate the IP isn't internal/link-local/metadata/IPv6-mapped, **pin** that IP for the connection, and follow **no redirects** (a redirect could send you somewhere bad after the check).
- **Verify ownership** of the endpoint at registration (a confirmation handshake), so we can't be tricked into DDoSing a victim by registering their URL.

## Interview framing tip
**Lead with the simplest case — one subscriber:** event arrives → look up the one registration → create one delivery task → attempt → on failure, schedule a retry → log the attempt. Get that whole loop crisp. *Then* generalize to fan-out (one event → many subscribers). The interviewer cares about your command of MQ retry primitives, REST, caching, and DB schema — depth on the core loop beats breadth.

## Database schema (DynamoDB)
```
SUBSCRIPTIONS (1:1 uniqueness from the key itself, NOT a GSI):
  PK = <tenant>#<event_type>
  SK = SUB#<sub_id>
  attrs: target_url, signing_secret, owner_verified
  → celebrity fan-out: shard as <event_type>#<shard>

DELIVERY STATE (live):
  PK = DELIVERY#<delivery_id>            # = hash(v1, tenant, event_instance_id, sub_id)
  attrs: status (pending|delivering|done|quarantined),
         attempt_count,
         attempt_lease_until, worker_id  # lease takeover for stuck deliveries

RETRY SCHEDULE (SEPARATE table — the time-bucketed scheduler):
  PK = <time_bucket>#<shard>
  SK = <due_ts>#<delivery_id>
  → poller fleet sweeps due rows  (NOT SQS visibility timeout)

OUTBOX (atomic with the state write) → dispatcher → SQS delivery queues

ATTEMPT HISTORY → Firehose → S3 / Athena   (NOT 1.3B/day DynamoDB writes)
```

## If the interviewer pushes further
- *"Why not exactly-once?"* Over HTTP you can't distinguish "customer didn't get it" from "customer got it but the ACK was lost." So you retry and they may see duplicates → at-least-once + a stable delivery_id for them to dedupe.
- *"Why a separate retry table instead of just delaying in SQS?"* The ~120K in-flight cap + Little's Law makes long delays impossible in SQS. A DynamoDB time-bucket schedule has no such limit.
