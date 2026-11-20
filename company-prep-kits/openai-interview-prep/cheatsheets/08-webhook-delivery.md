# Cheat Sheet — Webhook Delivery System

🔗 Diagram: https://excalidraw-production.a.musta.ch#346b073a-2def-4d8c-a9fc-42ada94048bf

## 1. Summary (in plain terms)
When something happens in our system (e.g. "payment succeeded"), call the customer's URL to tell them — 1 billion times a day. Customers' servers go down, so we must retry with backoff for up to 24h, never lose an event, and never hammer them with infinite duplicates.

## 2. Key deep dives (plain terms)

- **Don't use SQS's "visibility timeout" for retry delays.** Hiding a message keeps it "in flight," and there's a ceiling (~120K in-flight); the math caps you at ~100 seconds of delay, so 24-hour retries are impossible that way. Instead, store retries in a **DynamoDB schedule table organized by time buckets**, and a poller sweeps "what's due now." Visibility timeout is only for keeping *one current attempt* from being double-processed.

- **The retry schedule is a SEPARATE table**, not an index on the live delivery table — so a retry storm doesn't amplify writes onto your hot path.

- **"Write the row, then enqueue" can lose events** (crash in between). Use a **transactional outbox**: write the state row and an outbox row in one atomic action; a dispatcher enqueues from the outbox.

- **Three different IDs — keep them straight (this is the heart of it):**
  - `event_type` → routing ("who subscribed to this?")
  - `event_instance_id` → dedupe the event on *ingestion*
  - `delivery_id = hash(version, tenant, event_instance, subscription)` → stable across all retries and fan-out copies; **this** is what the customer dedupes on.
  Conflating these into one "event_id" was the original sin.

- **Stuck deliveries:** a row marked "delivering" whose worker died is reclaimed via a **lease takeover** (claim if the lease expired).

- **Hot partitions:** a celebrity event with millions of subscribers must be sharded (`event_type#shard`); never key a table on a low-cardinality `status`.

- **At-least-once only** (exactly-once over HTTP is impossible). Don't write 1.3B attempt-logs/day to DynamoDB — stream them to S3/Athena via Firehose.

- **SSRF protection at connect time:** the customer URL is re-resolved and validated *when you dial it* (block internal/metadata IPs, no redirects) — not just checked once at registration. Verify they own the endpoint (so we can't be used to DDoS someone).

- **Interview framing:** start with the simple 1-subscriber case end-to-end, *then* generalize to fan-out.

## 3. Database schema (DynamoDB)
```
SUBSCRIPTIONS (1:1 uniqueness enforced by the key itself, NOT a GSI):
  PK = <tenant>#<event_type>
  SK = SUB#<sub_id>
  attrs: target_url, secret, owner_verified, shard_hint
  → celebrity fan-out: shard as <event_type>#<shard>

DELIVERY STATE (live):
  PK = DELIVERY#<delivery_id>          # = hash(v1, tenant, event_instance, sub_id)
  attrs: status (pending|delivering|done|quarantined),
         attempt_lease_until, worker_id,    # lease takeover for stuck rows
         attempt_count

RETRY SCHEDULE (SEPARATE table — the time-bucketed scheduler):
  PK = <bucket>#<shard>                 # e.g. minute bucket
  SK = <due_ts>#<delivery_id>
  → poller fleet sweeps due rows; NOT SQS visibility timeout

OUTBOX (atomic with state write) → dispatcher → SQS (fresh / retry / quarantine)

ATTEMPT HISTORY → Firehose → S3/Athena   (NOT 1.3B/day DynamoDB writes)
```
