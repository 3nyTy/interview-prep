> ## ⛔ SUPERSEDED DRAFT — DO NOT STUDY THIS AS THE ANSWER
>
> This is the **Round-0 proposal**, preserved only as the input Codex attacked. It contains errors that
> were found and fixed. The authoritative version is `../reddit-design-crib.md` (Q1).
>
> **Known wrong in this file:**
> - Claims message coalescing gives a "10–50×" reduction. It gives `msg_rate × tick` ≈ **2.5×** at 10 msg/s.
> - Gives **no bandwidth or egress figure at all**. The real number is ~**12.5 GB/s** at 300 B/msg.
> - Presents epoch fencing on the *write* as sufficient for ordering across sequencer failover. It is not —
>   the dying owner's already-committed outbox row still publishes.
> - Implies an NLB can steer mega-room connections to a dedicated pool. NLB is L4 and **cannot** inspect
>   room membership.
> - Describes `MAXLEN ~1000` as "60 seconds" of retention. At 20 msg/s it is ~50 s, and it is entry-based.
> - **Omits the Kafka → Redis projector entirely** — the live-delivery path has a hole in it.

# Proposal: Reddit Subreddit Live Chat System

Interview context: Reddit SWE L6 (Staff) system-design round, 60 min. Real prompt as reported:
"Design Reddit's subreddit live-chat product — real-time chat anchored to a subreddit, room sizes
ranging from tens to millions during live events. The Super Bowl scenario — fan-out to ~5M concurrent
users on a single subreddit chat — is the canonical scale follow-up."

Constraint: AWS-native (candidate has deep AWS expertise and will be asked to justify concrete services).

## Position

The mega-room is a **different product** from a normal room, and the design must say so explicitly and
tier accordingly. Trying to serve a 5M-person room with the same path as a 50-person room is the
failure mode. Below, "Tier A" = rooms up to ~50K concurrent, "Tier B" = mega-rooms.

### 1. Connection tier

- WebSocket gateways on ECS Fargate behind an **NLB** (not ALB — long-lived connections, raw TCP, no
  per-request overhead; ALB idle-timeout and target-count limits get in the way).
- Budget **30–50K connections per gateway**, not the 65K/75K theoretical figure. Memory per connection
  (TLS buffers + app state) is the real limit, ~40–80KB. 5M / 40K = **125 gateways**, and I would run
  ~300 for 2–3x headroom.
- Connection-time routing: gateway assignment is sticky for the connection lifetime. Mega-rooms get a
  **dedicated gateway pool** so a Super Bowl room cannot starve normal subreddit chat on shared hosts.

### 2. Ordering

- **Per-room monotonic `room_seq`** assigned by a single-writer sequencer per room. Not a wall-clock id
  (Snowflake), because clocks disagree across failover and clients need one identical order.
- Ownership = **lease + monotonic epoch fencing token**; every write is conditional on the epoch, so a
  partitioned old owner cannot keep writing. Snowflake used only for a globally unique message id.
- Sequencing is cheap even at Tier B: after admission control (below) a mega-room accepts ~20 msg/s, so
  one sequencer is never the bottleneck. **The scale problem here is fan-out, not ordering.**

### 3. Fan-out — and why the Slack-style optimization collapses at 5M

For Tier A, the standard two-tier fan-out is right: a channel router holds room → set of gateways that
actually have a subscriber, and one publish goes to just those gateways (maybe 50), via Redis
**sharded** pub/sub (`SPUBLISH`, not cluster-wide `PUBLISH`).

At Tier B that optimization buys nothing: with 5M subscribers, **every one of the ~300 gateways has a
subscriber**, so "publish only to interested gateways" = publish to all of them. The router is not the
bottleneck — 300 fan-out targets is trivial.

The actual bottleneck is **per-connection socket writes: 5M writes per message.** At even 10 accepted
messages/second that is 50M socket writes/sec across the fleet, ~170K writes/sec per gateway. That is
not achievable per-message.

Two mechanisms, both required:

**(a) Write-side admission control.** A 5M-person room is a broadcast medium, not a conversation.
Cap the *visible* publish rate per room (e.g. 20 msg/s) with a per-room token bucket at the sequencer.
Selection among the flood is by recency plus author signal (karma, subscriber status, mod). Everything
else is dropped, and the client is told its message was rate-limited rather than silently discarded.
Without this, the fan-out problem is unsolvable and any design that ignores it is dishonest.

**(b) Read-side coalescing.** The gateway does not forward message-by-message. It accumulates all
messages for room R over a **250–500 ms tick** and emits **one frame containing N messages** per
connection. This cuts syscalls by 10–50x and matches how people actually read a firehose chat. Tier A
keeps per-message delivery for latency.

### 4. Durability — tiered, and this is the main trade-off

- **Tier A:** durable path. Message + outbox row written in one **DynamoDB transaction**, relay
  publishes to Kafka (MSK). Idempotent consumers. Nothing the API accepted is ever lost.
- **Tier B:** best-effort. Do *not* run 5M-subscriber ephemeral event chat through a per-message
  transactional-outbox write — the write rate is fine (20/s after admission control) but the *value* is
  low and the operational coupling is not worth it. Keep a **capped Redis Stream per mega-room** holding
  the last ~1000 messages / 60 seconds for reconnect replay, and archive asynchronously to S3 for
  moderation/audit only.

Being explicit that mega-room chat is **not** durable-by-default is the correct product call and I would
state it as a decision with the moderation-archive caveat, not hide it.

### 5. Gap detection and reconnect

- Every frame carries `room_seq` (for coalesced frames, the range `[from_seq, to_seq]`). Client
  gap-detects and requests a replay for the missing range.
- Periodic `latest_seq` heartbeat catches a silently-dropped *last* message, which gap detection alone
  cannot see.
- Replay comes from the **Redis Stream**, never from DynamoDB — a reconnect storm must not hit the
  durable store.
- **Reconnect storm is the real availability risk.** One gateway dying = 40K simultaneous reconnects.
  Client backoff must be **jittered** (full jitter, base 500ms, cap 30s). Without jitter, a single
  gateway loss cascades.

### 6. Presence and participant count

Per-user presence in a 5M room is not worth its cost. Each gateway reports its local per-room
connection count every few seconds; an aggregator sums them for a displayed approximate count. If exact
distinct-user counting is wanted, HyperLogLog. Per-user "who is here" is a Tier A feature only.

### 7. Scheduled events are a pre-provisioning problem, not an autoscaling problem

The Super Bowl is on a calendar. Long-lived sticky connections mean scale-out does **not** rebalance
existing connections, and cold-starting 200 Fargate tasks while 5M people are connecting is too slow.
So: pre-warm the mega-room pool ahead of a known event, and drive rebalancing by asking a fraction of
clients to reconnect (with jitter) rather than by waiting for churn.

### 8. Data model (DynamoDB)

```
MESSAGES (Tier A durable log)
  PK = ROOM#<room_id>          SK = <room_seq>
  attrs: msg_id (Snowflake), author_id, body, ts, epoch

OUTBOX (same transaction as the message write)
  PK = ROOM#<room_id>          SK = OUTBOX#<room_seq>     -> relay -> MSK

ROOM_OWNERSHIP (sequencer lease)
  PK = ROOM#<room_id>
  attrs: owner_node, epoch, lease_expires_at              (writes conditional on epoch)

Redis Stream per room (both tiers): capped replay buffer, XADD + MAXLEN ~1000
Redis sharded pub/sub: one channel per room, SPUBLISH
```

### 9. Moderation

Mega-room chat is the highest-abuse surface Reddit has. Inline synchronous ML classification on a 20
msg/s admitted stream is affordable (unlike on the raw flood) — so admission control also *enables*
moderation. Async human review reads the S3 archive.

## Explicit trade-offs I am accepting

1. Mega-room messages are not durable. Accepted: it is ephemeral event chat, and the S3 archive covers
   moderation and audit.
2. Most messages in a mega-room are never delivered to anyone. Accepted: it is physically necessary and
   the product is a broadcast, not a conversation.
3. Participant count is approximate.
4. 250–500 ms coalescing adds latency in mega-rooms. Accepted: humans cannot read faster.
5. Single write region for a room, with cross-region clients paying RTT. A room is one conversation;
   multi-master ordering is not worth it.
