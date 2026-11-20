# Reddit Design Crib — Plain-English Cheat Sheet

**How to use, live:** name a design → read **Open with** (your first 30 seconds), then work down the
numbered points. The **bold phrase** is what you say out loud.

**Hard rules for this file:** **max 10 numbered points per design** — major problems only, not caveats.
**Each point is one line.** Everything else lives in **Detail**, which you do *not* read live.

**Ordering:** sorted by **cost of getting it wrong**. The **first three are the round-deciders.**

**Validation:** every point below survived a Codex adversarial round on 2026-08-14. Points that exist
*because* Codex broke the first draft are marked ⚑ — those are the ones a Staff interviewer will find.
Full record in `debate-record.md`.

**Scope:** the 4 SWE-tagged designs from the 1p3a bank. ML Feature Store, Post/Comment Ranking and
Comment-Prediction are MLE-only and deliberately excluded. **Job Scheduler** is also in rotation and maps
onto the already-validated CI/CD scheduler design.

**Jump to:** [1 Live Chat](#q1) · [2 Notifications](#q2) · [3 Leaderboard](#q3) · [4 Video Rec](#q4) ·
[Repeating ideas](#cross)

---

<a id="q1"></a>
## Q1 — Subreddit Live Chat (the 5M Super Bowl room)

**Open with:** a 5M-person room is a **broadcast, not a chat** — so **the delivery contract changes before
any architecture does**, **coalescing is arithmetic not magic**, and **pub/sub is a hint while the log is
the truth**.

1. ⚑ Every client cannot receive every message → **Change the product contract: each client gets a
   sampled or curated subset.** At 300 bytes and 10 msg/s, 5M clients is **12.5 GB/s of egress** — a
   multi-terabit bandwidth and cost problem, not a gateway problem.
2. ⚑ Coalescing is not a free multiplier → **Batching cuts writes by exactly (msg_rate × tick): a 250 ms
   tick at 10 msg/s is 2.5×, not 10–50×** — and it cuts zero bytes, zero TLS work, zero egress.
3. ⚑ Ordering across sequencer failover → **Fence at the RELAY, not just the write — reject old-epoch
   records at publish time and publish per-room in seq order.** A conditional write blocks new stale
   writes; it cannot retract an outbox row the dying owner already committed.
4. ⚑ Redis pub/sub is **at-most-once** → **Treat it as a low-latency hint; the durable log is the source
   of truth and gateways recover gaps by sequence number.** A briefly disconnected gateway loses messages
   permanently.
5. ⚑ The live path has a missing link → **Name the Kafka → Redis projector out loud**: partitioned by
   `room_id`, idempotent, lag-monitored, and able to rebuild Redis from the log.
6. ⚑ Reconnect replay → **The GATEWAY fetches each missing range once and distributes it to its clients.**
   40K reconnects × 600 messages = 24M entries if every client asks Redis itself.
7. ⚑ NLB cannot route by room → **A room-aware bootstrap service hands back the pool endpoint BEFORE the
   WebSocket opens.** L4 cannot inspect room membership. (NLB idle timeout is 350 s, so heartbeat regardless.)
8. ⚑ Rejected messages must not burn sequence numbers → **Assign `room_seq` only to accepted messages**,
   and give rejected attempts an independent request id — otherwise rate-limited senders gap-detect forever.
9. ⚑ Capacity → **"30–50K connections per task" is a hypothesis to benchmark, not a limit.** At 50K × 80 KB
   that is 4 GB of connection state before runtime, buffers and TLS.
10. Admission control protects fan-out, not ingress → **Rate-limit at connection, user and IP before the
    sequencer.** Publishing 20 msg/s still means millions attempted.

**Detail:** Tier the product — Tier A (≤50K) keeps per-message delivery and the durable DynamoDB +
transactional-outbox path; Tier B (mega-rooms) is explicitly ephemeral. **Pick one durability contract and
say it:** either an accepted message is durable before you ack it, or you tell users it can vanish — do not
call an async best-effort S3 archive an audit guarantee. Presence is **active connections**, not distinct
users, unless you merge per-gateway HLLs. Scheduled events are a **pre-provisioning** problem: sticky
connections mean scale-out never rebalances, so pre-warm and migrate with a rate budget (asking 10 % of 5M
to reconnect is 500K migrations — jitter alone is not enough, you need `Retry-After` and bounded
handshake admission). Mega-rooms get a dedicated gateway pool so they cannot starve normal chat. Moderation
gets *cheaper* because admission control shrinks the stream, but define fail-open vs fail-closed on
classifier timeout, and the control plane still has to broadcast bans and delete tombstones. S3 archival is
batched objects via a stream consumer, never one object per message.

---

<a id="q2"></a>
## Q2 — Notification System

**Open with:** there are two event classes with different cost shapes — so **split by recipient
CARDINALITY, not by event type**, **fan-out-on-read serves in-app only**, and **push and email need the
audience resolved at event time**.

1. ⚑ Fan-out-on-read **cannot deliver push or email** → **In-app pulls the merge; push/email require the
   eligible audience enumerated and snapshotted at event time.** SNS, APNs, FCM and SES do not query a
   user's subscriptions at send time. This is the round-decider.
2. ⚑ Subscription boundaries → **Snapshot eligibility at event time, not delivery time** — otherwise you
   notify people who subscribed afterwards and miss people who left before.
3. ⚑ Split by cardinality, not semantics → **A "targeted" mention can hit millions and a "broadcast" can
   hit 3,000.** Use a cardinality threshold with a hybrid path, not fixed event types.
4. ⚑ Inbox write then SQS enqueue is a **dual write** → **Drive dispatch off DynamoDB Streams or a real
   dispatch outbox.** Otherwise the retry sees the existing inbox row and never enqueues at all.
5. ⚑ Bundle flush is a lost-update race → **Generation-keyed bundles: conditionally close generation N,
   open N+1, flush an immutable snapshot.** Atomic `ADD` protects the counter and never the lifecycle.
6. ⚑ Kafka partitioning does **not** give user-visible ordering → **Carry an entity version and suppress
   stale.** SQS reorders, retries reorder, and APNs/FCM guarantee no order — so "removed" can still beat
   "approved".
7. ⚑ Badge counts → **Per-stream read cursors, and call the broadcast part "new activity", not a count.**
   A user in 500 subreddits cannot get a counted badge out of 500 queries.
8. ⚑ Provider acceptance is **not** delivery → **Track accepted-by-dispatcher, accepted-by-provider and
   client-receipt as separate states.** APNs gives no per-notification device receipt.
9. ⚑ The token model must actually be able to send → **Store the encrypted provider token plus app and
   environment; a `token_hash` alone cannot push.** Prune on APNs 410 and FCM `UNREGISTERED`.
10. ⚑ Email reputation → **Separate transactional from bulk sending identities.** Put password resets
    behind bulk-notification reputation and one bad campaign takes out password resets.

**Detail:** Timezones — store the **IANA zone**, compute the next local occurrence with a current tz
database, convert to UTC and enqueue a durable due-task. "24 hourly waves plus half-hour zones" is wrong:
15- and 45-minute offsets exist and DST moves the UTC time. Flush scheduler must be
`PK = flush_minute#shard` with leases and bounded batch claims, or one minute is one hot partition.
Throttling should **degrade, not drop** — suppress push/email but still write the inbox row — and needs
durable buckets plus per-provider and per-domain quotas. Preferences: coarse filter at enqueue, then an
authoritative check at a **defined linearization point** near dispatch, accepting that a provider-accepted
message cannot be recalled. Store one **logical** notification with separate per-channel delivery records —
putting `channel` in the id while the inbox is also source of truth creates duplicate inbox rows.
Retention: give a real capacity model; 90 days of 1B/day at 1 KB is ~90 TB before indexes and replicas.
Deterministic `notification_id = hash(event_id, recipient_id)` is necessary but **not** sufficient for
DLQ replay hours later — you need durable per-channel dispatch state.

---

<a id="q3"></a>
## Q3 — Game Leaderboard

**Open with:** this round escalates, so **start small and change exactly one thing per step** —
**Postgres genuinely handles the 10 rps they open with**, **Redis is a rebuildable view and not the source
of truth**, and **top-K survives sharding while exact rank does not**.

1. ⚑ Redis Cluster hash tags → **`lb:{game}:alltime:0` and `:1` land on the SAME slot, because the braces
   are the hash tag — the sharding is a no-op.** Put the shard inside the tag: `lb:{game:3}:alltime`, then
   accept that cross-shard `ZUNIONSTORE` is gone.
2. ⚑ Histogram rank error is bounded by **bucket population, not bucket width** → **A bucket holding 40M
   players can mis-rank by 40M however narrow the score range is.** Commit to a percentile-error SLA and
   build to it.
3. ⚑ "Top-K stays exact" has conditions → **Exact for a fixed version under one total ordering; independent
   cross-shard reads are not a snapshot.** Also pin a deterministic tie-breaker — Redis breaks score ties
   lexicographically by member.
4. ⚑ Friend leaderboards → **Fan-out on read with per-shard pipelines.** `ZMSCORE` works on one key, so it
   cannot batch across shards. Still reject per-user friend ZSETs: one score update would write into every
   friend's board.
5. ⚑ Rolling 7-day → **Maintain it incrementally per shard and merge top-K at read.** `ZUNIONSTORE` over
   7 × 100M every minute is not viable, and an unsharded `rolling7` key rebuilds the hot key you just removed.
6. ⚑ Rebuild → **State an RTO and a checkpoint story.** Player-partitioned events mean rebuilding one game
   is a full scan, and 100M Redis writes at 100K/s is 17 minutes before you even read DynamoDB.
7. ⚑ Memory → **100M members is tens of GB, not 1–2 GB** — dict plus skiplist plus member strings plus
   allocator overhead, then replicas and failover headroom on top.
8. ⚑ "What is one score event?" → **Absolute, delta, personal-best or correction — pick one, then derive
   the command.** `ZADD GT` is only idempotent while decreases and corrections are impossible.
9. ⚑ Resharding → **Virtual shards mapped to physical nodes.** `hash % N` from 16 → 32 migrates half the
   data and the design has no dual-read path.
10. ⚑ Replica reads → **Define read-after-write and route a player's own score read to the primary.**
    Cross-shard replica reads combine different replication positions into a view that never existed.

**Detail:** Open with `scores(player_id, score)` in Postgres and say why it holds at 10 rps — then name the
break: rank is proportional to the qualifying index entries because a B-tree gives no order statistics, so
**rank breaks long before top-K does**. Score events land in Kinesis → DynamoDB with `submission_id` as the
idempotency key and an explicit consumer-checkpoint protocol (Kinesis is at-least-once with finite
retention, so it is not itself the permanent source of truth), and the event store must be partitioned for
**game replay**, not only by player. Histogram maintenance is a decrement-old plus increment-new under one
atomic per-shard Lua script, and a `ZADD GT` rejection must not move counts. Time windows: `ZINCRBY` for
deltas with dedupe, `ZADD` for absolutes, `ZADD GT` for personal bests; rolling sums must subtract expired
contributions and rolling maxima must recompute when the max expires; set TTL from the window's fixed
expiry, never reset per write. Anti-cheat needs a **versioned board switch**, not just a filtered rebuild.
ElastiCache Global Datastore is **not** multi-master — and cumulative sums *can* be made mergeable with
per-region contribution counters, so do not claim only max-semantics merge.

---

<a id="q4"></a>
## Q4 — Video Recommendation (asked as an infra round)

**Open with:** the model is the boring part, so spend the hour on the feedback loop — **log the exact
serving input**, **"no engagement" is not a negative label**, and **hard eligibility filters run before
ranking, not after**.

1. ⚑ "No engagement" is **not** a negative → **Distinguish served / rendered / viewable / engaged, and
   treat unobserved exposure as CENSORED, not negative.** A server-side impression does not mean the user
   ever scrolled to it.
2. ⚑ Logging position does **not** give you IPS → **Log the propensity — the probability the item was
   exposed at that position — plus the randomization policy.** A deterministic ranker has no propensity to
   divide by.
3. ⚑ Hard eligibility filters must run **before** ranking → **Blocked, unsafe and already-seen removed
   pre-rank, then a defence-in-depth pass after.** Filter only after and the ranker scores illegal content
   while you risk returning too few items.
4. ⚑ Sampling the scored-but-unshown candidates destroys the snapshot guarantee → **Log the inclusion
   probability and reweight**, or you silently shift the training distribution while claiming exactness.
5. ⚑ The join key → **An item-level exposure token returned to the client**, not `request_id`. One request
   holds many candidates and one item appears across many requests.
6. ⚑ `BatchGetItem` is capped at **100 items / 16 MB** → **Parallel bounded batches, or one precomputed
   compact candidate record.** "One round trip for 500 candidates" is not possible, and DAX does not lift
   the limit.
7. ⚑ p99 does not add → **The fan-out tail is the slowest source; give per-source deadlines with
   cancellation, not retries on the critical path.** 120 ms of stage budgets is not a 150 ms p99.
8. ⚑ Parity needs a test, not an argument → **Replay production inputs through both paths and diff golden
   vectors.** Shared code still diverges on clocks, watermarks, late data, nulls and defaults.
9. ⚑ Rollback is a **bundle**, not a pointer → **Version model + container + preprocessing + feature
   contract + policy config and deploy them atomically.** Flipping the model pointer does not revert bad
   feature code.
10. ⚑ A schema mismatch must **not** fail the feed closed → **Fall back to the last compatible bundle.**
    Fail closed only for genuinely unsafe policy data, not a routine version skew.

**Detail:** Feature null-rate is the top silent killer but is **not** sufficient alone — add freshness,
default-rate, range and cardinality checks, and slice guardrails by platform, geo, subreddit, creator and
new-user, because aggregates hide those regressions. Late events **revise** labels, so keep immutable raw
events with idempotent event ids, versioned/upserted labels, an allowed-lateness policy derived from the
observed lateness percentiles rather than a round 6 hours, and a training cutoff that excludes unresolved
windows. Firehose delivery is at-least-once, so dedupe and compact, and Iceberg needs explicit catalog and
compaction config. Log the **final serialized model input** (or a hash plus the preprocessing artifact), not
the feature-store response — normalization and embedding happen after retrieval. Cold-start exposure is a
**constrained policy** with safety eligibility and creator quotas plus logged propensity, not "guaranteed
exposure" bolted onto an engagement objective. Cost equation:
`daily bytes = requests/day × candidates/request × bytes/candidate`. Privacy: snapshots hold behavioral
data and embeddings, so tokenize identifiers, scope KMS keys and propagate deletion into derived and backup
data — Glacier retention is not a deletion story. Also define the pre-rank reduction rule (1000 → 500 is
itself a hidden selection policy) and make the ranker-timeout fallback a calibrated policy-filtered
baseline, not raw source priors.

---

<a id="cross"></a>
## Repeating ideas across all four

- **Fence at the boundary that matters.** An epoch check on the *write* does not protect the *publish*.
  Ask where the side effect actually escapes, and fence there.
- **Generation-keyed invalidation beats per-entry.** True for the notification bundle lifecycle, for the
  message-store cache, and for the leaderboard board version. Bump a counter, drop wholesale.
- **Classify by cardinality, not by name.** Targeted vs broadcast, Tier A vs Tier B room — the threshold is
  the design, the label is not.
- **Provider or downstream acceptance is never delivery.** APNs accepted ≠ phone received. DynamoDB
  committed ≠ published. Name the states separately.
- **A snapshot claim needs a version.** "Exact top-K" and "exact feature snapshot" both quietly assume a
  consistent version that concurrent reads do not give you.
- **State the contract before the architecture.** 5M fan-out, mega-room durability, and negative-label
  semantics are all product decisions masquerading as engineering ones.
- **Give a number or say you would benchmark it.** Connections per host, rank error, RTO, egress GB/s. An
  invented number is worse than an admitted unknown.
