# Design a Highly Scalable Webhook Delivery System

> OpenAI Staff SWE sample question (1B events/day). Stress-tested via debate skill (3 rounds vs Codex; converged to *"approaching Staff-level"*).
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#346b073a-2def-4d8c-a9fc-42ada94048bf

## Core AWS design
API Gateway → Fargate management service; DynamoDB **subscriptions** (PK=`tenant#event_type` enforces 1:1 uniqueness — **NOT** a GSI); ElastiCache Redis cache (`event_type → subs`); Ingestion API → SQS → fan-out workers → SQS delivery queues (fresh / retry / quarantine) → delivery workers → egress proxy (SSRF) → customer.

## The non-obvious lessons the debate forced (the differentiators)

### 1. Backoff must NOT use SQS visibility timeout
- Invisible msgs stay in-flight; the ~120K in-flight soft limit + Little's Law caps retry delay to ~100s at this scale, so a 24h retry is impossible.
- Use a durable **DynamoDB time-bucketed RetrySchedule table** (PK=`bucket#shard`, SK=`due_ts#delivery_id`) swept by a poller fleet.
- Visibility timeout covers **ONE** in-flight attempt only (heartbeat-extended).

### 2. RetrySchedule must be a SEPARATE table
- Not a GSI on the hot delivery-state table — avoids write-amplification coupling retry storms to live state.

### 3. Conditional writes ≠ atomicity
- "Write state row then enqueue" has a loss window.
- Use a **transactional outbox** (`TransactWriteItems`: state + outbox item; dispatcher enqueues from the outbox).

### 4. Identity: 3 distinct IDs
- `event_type` (routing).
- `event_instance_id` (ingestion dedupe).
- `delivery_id = H(v1 ‖ tenant ‖ event_instance_id ‖ sub_id)` (stable across retries + fan-out; consumer dedupes on it).
- Conflating `event_id` was the original sin.

### 5. Stranded `delivering` rows need lease takeover
- `attempt_lease_until` / `worker_id`; claim if expired.

### 6. Hot partitions
- Shard subscriptions by `event_type#shard` (celebrity fan-out).
- Write-shard delivery logs by `webhook_id#day#bucket`.
- Never use bare low-cardinality `status` as a PK.

### 7. At-least-once only
- Exactly-once over HTTP is impossible.
- Attempt history → Firehose → S3/Athena, **NOT** 1.3B/day DynamoDB writes.

### 8. SSRF enforced at connect-time
- Via the egress proxy (resolve + validate + pin IP, no redirects, block link-local/metadata/IPv6-mapped).
- Registration check is the first gate only.
- Ownership verification mandatory (anti DDoS-by-webhook).

## Interview framing
**LEAD with the 1:1 case** (registration row → cache lookup → 1 delivery task → retry scheduler → log), **THEN** generalize to fan-out. The interviewer cares about MQ retry primitives, REST, caching, DB schema — depth over breadth.
