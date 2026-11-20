# Mock Scorecard — Q8: Webhook Delivery (1B events/day)

**Date:** 2026-07-15 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/08-webhook-delivery.md`](../detailed/08-webhook-delivery.md)

**Verdict:** Another strong round — **independently built the retry scheduler** (the single hardest part) and gave the correct at-least-once promise. Gaps were *mechanism polish*: dedupe belongs on a **stable delivery_id** (not a status-check), and SSRF needs **IP-pinning + no-redirects**, not just a DNS check. Scheduling/fencing reflexes are now top-tier; edges are identity-modeling and security detail.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **Pipeline + fan-out** — ingest queue → look up subscriptions → per-subscriber callback task → delivery worker → HTTP POST.
2. ✅ **Retry scheduler (the crux)** — durable **retry table + `execution_after_ts` + per-minute cron sweep + claim/lease** (transferred from payments). *Name the trap avoided:* using **SQS visibility timeout** for backoff collapses — ~**120K in-flight cap** + Little's Law → ≈**100 s** max delay, nowhere near 24h. Visibility timeout is only for keeping *one* in-progress attempt from being double-picked.
3. ✅ **At-least-once** — "minimum 1 call"; exactly-once over HTTP is impossible (customer processes the POST but the ACK is lost → indistinguishable from failure → you retry).
4. ❌ **Stable `delivery_id` for customer-side dedupe** — *fix:* don't try to prevent the duplicate (I proposed a non-standard GET-status-check: customers don't expose it, doubles traffic, same ACK-loss). Make it **harmless** — put a **stable `delivery_id` in every identical POST** (same across all retries *and* fan-out) so the customer dedupes. Learn the **three IDs**: `event_type` (routing) / `event_instance_id` (my ingestion dedupe) / `delivery_id = hash(version, tenant, event_instance_id, sub_id)` (customer dedupe).
5. ⚠️ **SSRF at connect time** — I moved validation to connect time (beats DNS rebinding — good). *Complete it:* **pin the validated IP** for the connection (a re-resolve is a fresh TOCTOU), **follow no redirects** (a 302 → `169.254.169.254` sidesteps the check), and **verify endpoint ownership at registration** (anti-DDoS-weaponization).
6. ⚠️ **Durability / outbox** — *add:* a **transactional outbox** for the state-write → enqueue step; be explicit that a crash between "HTTP succeeded" and "mark completed" causes a redelivery (→ handled by at-least-once + delivery_id).
7. ✅ **Operational maturity** — per-customer **rate limiting**, **circuit breaker** on a dead endpoint, DLQ. (Nice adds beyond the base.)
8. ⚠️ **Attempt history volume** — ~**1.3B writes/day** → stream to **Firehose → S3 → Athena**, *not* DynamoDB.
9. ⚠️ **Hot partitions** — shard a celebrity **`event_type#shard`**; never key a table on a low-cardinality `status` field.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Retries for 24h can't ride the SQS visibility timeout (in-flight cap + Little's Law caps you near 100s) — use a durable time-bucketed retry table swept by a poller."*
- *"Exactly-once over HTTP is impossible, so I promise at-least-once and put a stable `delivery_id` in every POST — identical across retries and fan-out — so the customer dedupes."*
- *"SSRF defense is at connect time: resolve, validate the IP, pin it, follow no redirects — registration-time checks lose to DNS rebinding."*

## Pattern to drill
Retry-scheduling and fencing are now **reflexive and top-tier** (built unprompted). The remaining edges are two *modeling/security* details: (1) **at-least-once is made safe by a stable dedupe id**, not by trying to prevent duplicates; (2) **SSRF = pin the IP + no redirects**, not just a DNS check.
