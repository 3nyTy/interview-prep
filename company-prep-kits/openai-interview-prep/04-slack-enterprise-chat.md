# Design a Slack-Like Enterprise Chat System

> OpenAI Staff SWE sample question (multi-tenant enterprise chat). Solved + debate-validated 2026-06-20 (Codex Round 1). AWS-centric.
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#b58b550a-a7d7-40b5-b3ec-84007b67ca18

## Key validated design decisions

### Fan-out (two-tier)
- Explicit **Channel Router / Dispatcher** tier: channel → one owner holds the gateway-subscriber map.
- Redis **sharded** pub/sub (`SPUBLISH`, NOT classic cluster `PUBLISH`).
- One publish → N gateways-with-a-subscriber (≤ fleet size), not 10K.

### Ordering
- **Per-channel monotonic `channel_seq`** (NOT Snowflake wall-clock), assigned by the single-writer owner.
- Ownership = **lease + monotonic epoch fencing token**; writes conditional on epoch (no split-brain).
- Snowflake used only for a globally unique id.

### Reliability
- **Transactional outbox** (message + outbox row in one DynamoDB txn) → Kafka.
- Idempotent consumers.
- Channels hash to a **fixed partition set** (NOT partition-per-channel).

### Live gap closure
- Every frame carries `channel_seq`; the client gap-detects.
- Periodic `latest_seq` heartbeat catches a silently-missed last message.

### Unread counts
- **`latest_seq − last_read_seq`** (read pointer), NOT per-member counter writes — avoids 10K writes per message.

### Multi-tenancy
- Pooled = strong **logical** isolation (centralized authz lib + RLS + per-tenant data keys).
- Silo = **physical** isolation for regulated / whale tenants.
- Don't claim "impossible to leak."

### Encryption
- KMS envelope encryption + **data-key caching** (NOT per-message KMS calls).

### Fleet sizing
- ~30–50K connections/gateway (NOT the 75K freebie) + 2–3× headroom.

## Codex's sharpest hits I conceded
- The unread-counter fan-out contradiction.
- Snowflake ≠ per-channel order across failover; need fencing.
- "Complete isolation" was overstated.
- Polling at 10M is a fantasy.
- CloudFront is media-only, not message-deltas.
