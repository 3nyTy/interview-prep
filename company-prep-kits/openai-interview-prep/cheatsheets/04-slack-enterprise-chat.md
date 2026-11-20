# Cheat Sheet — Slack-Like Enterprise Chat

🔗 Diagram: https://excalidraw-production.a.musta.ch#b58b550a-a7d7-40b5-b3ec-84007b67ca18

## 1. Summary (in plain terms)
A team chat app (channels, real-time messages) used by many companies at once. The hard parts: getting a new message to thousands of connected people *fast*, keeping everyone's message order *identical*, and counting unread messages without melting the database.

## 2. Key deep dives (plain terms)

- **Two-tier fan-out (don't blast every server).** A "channel router" knows which gateway servers actually have someone listening to a channel. One publish goes only to *those* servers (maybe 50), not all 10,000. Uses Redis **sharded** pub/sub (`SPUBLISH`).

- **One writer per channel decides the order.** Each channel has a single owner that stamps every message with the next number (`channel_seq` = 1, 2, 3…). Everyone everywhere then sees the same order. Don't use a timestamp (Snowflake) for order — clocks drift and disagree across failover.

- **Ownership uses a lease + an "epoch" fencing token.** If the owner dies and another takes over, the epoch bumps; writes from the old owner are rejected. No split-brain, no two writers.

- **Reliability = transactional outbox.** Save the message *and* an "to-be-published" row in one atomic write, then a relay publishes it. You never lose a message that the DB accepted.

- **Catching missed messages:** every message carries its sequence number, so a client notices a gap ("I have 5 then 7 — where's 6?") and refetches. A periodic "latest is N" heartbeat catches a silently-dropped *last* message.

- **Unread counts the cheap way:** store one "last message I read" pointer per person. Unread = `latest_seq − last_read_seq`. Do **not** write +1 to 10,000 members on every message.

- **Multi-tenancy:** shared infra with strong *logical* isolation (central authz + per-tenant encryption keys) for most; *physical* separate stacks for regulated giants. Don't claim leaks are "impossible."

## 3. Database schema (DynamoDB + read pointers)
```
MESSAGES table (the durable log):
  PK = CHANNEL#<channel_id>
  SK = <channel_seq>                # monotonic per channel, set by the single writer
  attrs: msg_id (Snowflake, global unique), sender, body, ts, outbox_flag

OUTBOX (same atomic txn as the message insert):
  PK = CHANNEL#<channel_id>, SK = OUTBOX#<channel_seq>  → relay → Kafka

CHANNEL OWNERSHIP / lease:
  PK = CHANNEL#<channel_id>
  attrs: owner_node, epoch          # writes conditional on epoch (fencing)

READ POINTERS (drives unread):
  PK = USER#<user_id>, SK = CHANNEL#<channel_id>
  attrs: last_read_seq
  → unread = channel.latest_seq − last_read_seq   (no per-member fan-out writes)
```
Kafka partitioning: channels hash to a **fixed set of partitions** (not one partition per channel).
