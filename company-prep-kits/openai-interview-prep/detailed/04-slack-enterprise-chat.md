# Slack-Like Enterprise Chat — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#b58b550a-a7d7-40b5-b3ec-84007b67ca18

## What we're building
A team chat app — channels, direct messages, real-time delivery — used by many companies at once. When someone posts in a channel with thousands of members, everyone online should see it almost instantly, everyone should see messages in the *same order*, and unread badges should be correct. All without melting the database.

## The three hard problems
1. **Fan-out:** one message may need to reach thousands of connected people, fast.
2. **Ordering:** everyone must see the same sequence of messages, even across server failures.
3. **Unread counts:** naively, one message in a 10,000-person channel means 10,000 counter updates. That's a write storm.

## Problem 1: Fan-out without blasting everything
People connect via long-lived WebSockets to **gateway servers** (each gateway holds tens of thousands of connections). A big channel's members are spread across many gateways.

The naive approach — "publish the message to all 10,000 gateways" — wastes enormous effort, since most gateways may have zero members of that channel. Instead:

- A **Channel Router / Dispatcher tier** keeps track of *which gateways actually have at least one member of each channel listening*.
- When a message is posted, we publish it once, and it goes only to the handful of gateways that actually have a listener — maybe 50, not 10,000.
- Under the hood this uses **Redis sharded pub/sub** (`SPUBLISH`), not classic cluster-wide `PUBLISH` (which would broadcast to every node). Sharded pub/sub keeps the message on the relevant shard.

So: **one publish → only the gateways that care.** That's the difference between a system that scales and one that doesn't.

## Problem 2: Identical ordering everywhere
If two people post "at the same time," who's first? Everyone must agree, forever, even if a server crashes and another takes over.

- Each channel has a **single writer** that stamps every message with the next number in a per-channel counter: `channel_seq` = 1, 2, 3, 4… Because one writer assigns them in order, the order is unambiguous and identical for all viewers.
- **Why not a timestamp (Snowflake)?** Because wall clocks drift between machines, and across a failover two machines can disagree about "now." Timestamps give you a *roughly*-ordered global ID, not a *guaranteed* per-channel order. Use Snowflake only for a globally-unique message ID, not for ordering within a channel.

### What stops two writers from existing at once?
Ownership of "I'm the writer for this channel" is a **lease with a monotonic epoch (a fencing token)**. If the current owner dies and another server takes over, the epoch bumps (say 7 → 8). Every write is conditional on the epoch. So if the old owner comes back as a zombie and tries to write with epoch 7, it's rejected — no split-brain, no two writers stamping conflicting sequence numbers.

## Problem 3: Unread counts without a write storm
The naive design writes "+1 unread" to every member's record on every message — 10,000 writes per message in a big channel. Don't.

Instead store **one "read pointer" per person per channel**: `last_read_seq` — the sequence number they've read up to. Then:

```
unread = channel.latest_seq − user.last_read_seq
```

When you open a channel, you update *your own* pointer. One write per reader when they read — not N writes per message. Computing unread is just subtraction.

## Making sure messages are never lost (reliability)
A subtle bug: "save the message to the DB, then publish it to subscribers" can lose messages if the server crashes in between (saved but never published, or published but the save failed).

Fix: the **transactional outbox**. In a *single atomic DynamoDB transaction*, write both the message row **and** an "outbox" row that says "this needs to be published." A separate relay process reads the outbox and publishes to **Kafka** (for history, search indexing, and cross-region). Because the message and its outbox entry commit together, you can never have one without the other. Consumers are **idempotent** (they dedupe on message ID) so a redelivered message isn't shown twice.

Channels hash to a **fixed set of Kafka partitions** — not one partition per channel (millions of partitions is unmanageable).

## Catching a silently-missed message
Even with all this, a client might miss a frame. Two safety nets:
- Every message carries its `channel_seq`, so a client notices a gap ("I have 5, then 7 — where's 6?") and refetches the missing range.
- A periodic "the latest is N" heartbeat catches the nasty case where the *last* message was dropped (no later message arrives to reveal the gap).

## Multi-tenancy (many companies, isolated)
- **Most companies:** shared infrastructure with strong *logical* isolation — a centralized authorization library on every read/write, row-level security, and per-tenant encryption keys.
- **Regulated giants:** physically separate stacks ("silo").
- Be honest: say "strong logical isolation," not "leaks are impossible." Claiming perfection is a red flag to a Staff interviewer.

Encryption uses **KMS envelope encryption with data-key caching** — you don't call KMS for every single message (too slow/expensive); you cache a data key and rotate it.

## Database schema (DynamoDB + Redis + Kafka)
```
MESSAGES (durable log, ordered per channel):
  PK = CHANNEL#<channel_id>
  SK = <channel_seq>             # monotonic per channel, assigned by the single writer
  attrs: msg_id (Snowflake, globally unique), sender_id, body, ts

OUTBOX (written in the SAME transaction as the message):
  PK = CHANNEL#<channel_id>
  SK = OUTBOX#<channel_seq>
  → relay publishes to Kafka, then deletes/marks the row

CHANNEL OWNERSHIP / lease:
  PK = CHANNEL#<channel_id>
  attrs: owner_gateway, epoch   # writes conditional on epoch (fencing → no split-brain)

READ POINTERS (drive unread counts):
  PK = USER#<user_id>
  SK = CHANNEL#<channel_id>
  attrs: last_read_seq
  → unread = channel.latest_seq − last_read_seq   (one write per read, not per message)
```

## Fleet sizing reality
Roughly **30–50K connections per gateway** (not the theoretical 75K), with 2–3× headroom for failover spikes. When a gateway dies, its connections reconnect en masse to others — you need the headroom or you cascade.

## If the interviewer pushes further
- *"Why a router tier instead of each gateway subscribing to every channel?"* Because a gateway would then receive every message for every channel it doesn't care about. The router keeps publishes targeted.
- *"What breaks with Snowflake ordering?"* Across a failover, clock skew means two messages can get out-of-order IDs; per-channel sequence from a single fenced writer is the only way to guarantee a total order.
- *"Polling for new messages at 10M users?"* Fantasy — that's why we push via WebSocket + sharded pub/sub. (CloudFront is for media files, not message deltas.)
