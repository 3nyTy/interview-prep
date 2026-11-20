# Debate Record — Reddit Design Cribs

Cross-model adversarial validation of the four SWE-tagged 1p3a system designs.
**Date:** 2026-08-14 · **Challenger:** Codex (gpt-5.6-luna, reasoning effort high), one adversarial round
per design with a domain-specific persona · **Protocol:** `codex-connector:debate`.

Raw material is preserved in **`raw/`** — Round-0 proposals (each banner-flagged with its own known errors),
Codex's verbatim critiques, and the reusable challenge template. See `raw/README.md`.

**Headline:** the first drafts had the right instincts and the wrong numbers. Codex found **two factual
errors** (a Redis Cluster hash-tag bug and a DynamoDB `BatchGetItem` limit), **two false guarantees**
(coalescing multiplier, histogram error bound), **one self-contradiction**, and **one inverted control**
(policy filters after ranking). Almost everything was conceded. The crib is the post-concession version.

---

## 1. Subreddit Live Chat

**Verdict:** instincts sound, arithmetic wrong. Codex's summary was fair — the answer "fails on the two
issues a Staff interviewer will attack first: the physical cost of 5M fan-out, and the correctness of
failover/replay ordering."

**Conceded — changed the design:**

| Finding | Why I conceded |
|---|---|
| **Coalescing gives (msg_rate × tick), not 10–50×** | 250 ms tick at 10 msg/s = 4 frames/s/conn = 20M writes/s down from 50M. That is 2.5×. And it cuts **zero** bytes, TLS work or egress. My multiplier was invented. |
| **12.5 GB/s egress at 300 B/msg** | I never computed bandwidth at all. This reframes the problem from "gateway fan-out" to "multi-terabit product and cost decision," which is the real answer. |
| **Epoch fencing does not order across failover** | The dying sequencer's outbox row is already committed; a conditional write cannot retract it. The relay must reject old-epoch records at publish time and publish per-room in seq order. My existing fencing intuition stopped one layer too early. |
| **Redis pub/sub is at-most-once** | A disconnected gateway loses messages permanently. Pub/sub can only be a hint path; the log must be the source of truth with sequence-aware gateway recovery. |
| **The Kafka → Redis projector was missing entirely** | I defined DynamoDB → outbox → MSK and Redis → clients, and never said how messages crossed. A genuine hole. |
| **Replay is a thundering herd on one Redis key** | 40K reconnects × 600 messages = 24M entries. The **gateway** must fetch each range once and distribute. "Never hit DynamoDB" just moved the hot spot. |
| **NLB cannot route by room** | L4 cannot inspect WebSocket room membership, so a "dedicated mega-room pool" needs a room-aware bootstrap service before the socket opens. My design implied something impossible. |
| **`MAXLEN ~1000` ≠ 60 seconds** | At 20 msg/s that is ~50 s. Use time-based trimming if the requirement is time. |
| **Rejected messages must not consume `room_seq`** | Otherwise rate-limited senders create permanent client-visible gaps. Sharp point I had missed. |
| **50K conns/task is unvalidated** | 50K × 80 KB = 4 GB of connection state before runtime. It is a hypothesis to benchmark, not a limit to quote. |
| **Ingress is unprotected** | Capping *published* messages at 20/s does nothing about millions of attempted frames, auth work and parse cost. |

**Pushed back (partially):**
- **"Snowflake is a poor choice"** — I used Snowflake *only* for global uniqueness, never for ordering,
  which is the correct narrow use. Codex's clock-rollback concern is real but does not apply to the claim I
  actually made. UUIDv7 is a fine substitute; this changes nothing structural.
- **"Karma-based selection is not implementable fairness"** — agreed it needs bounded queues and starvation
  rules, but *some* selection policy is unavoidable once you accept the message budget. The critique
  strengthens the point rather than refuting it.

**Still open (interviewer-dependent, decide live):**
- Whether mega-room chat is ephemeral at all. Codex is right that "best effort but S3 covers moderation" is
  incoherent as stated. The fix is to make the **ack semantics** explicit — "admitted", not "stored" — but
  whether Reddit would ship that is a product call. Have both answers ready.

---

## 2. Notification System

**Verdict:** the best single hit of the whole exercise. My core split was right in mechanism and wrong in
scope.

**Conceded — changed the design:**

| Finding | Why I conceded |
|---|---|
| **Fan-out-on-read cannot deliver push or email** | Decisive. SNS/APNs/FCM/SES do not query subscriptions at send time. Fan-out-on-read serves the *in-app pull* only; push and email need the audience enumerated and snapshotted. I had presented one mechanism as covering all three channels. |
| **Eligibility must be snapshotted at event time** | Using *current* subscriptions notifies people who joined after the event and misses people who left before it. A product and privacy bug. |
| **Split by cardinality, not event type** | A "targeted" mention can reach millions; a "broadcast" can reach 3,000. Threshold plus hybrid path is strictly better than my semantic split. |
| **Inbox write → SQS enqueue is an unaddressed dual write** | I had a producer-side outbox but none from inbox to dispatch, so a retry sees the inbox row and never enqueues. DynamoDB Streams or a dispatch outbox. |
| **Bundle flush has a lost-update race** | Atomic `ADD` protects the counter, not the lifecycle: flush reads 5, a reply makes it 6, flush resets — reply 6 is lost. Needs generation-keyed close/open with an immutable flush snapshot. |
| **Kafka partitioning ≠ user-visible ordering** | SQS reorders, retries reorder, providers guarantee nothing. "Removed" can still beat "approved". Needs entity versions with stale suppression. |
| **The badge count is not computable as specified** | 500 subreddits = 500 queries before counting, and subscribe/unsubscribe boundaries break a single global timestamp. Per-stream cursors, and label the broadcast part "new activity". |
| **Provider acceptance ≠ delivery** | APNs gives no per-notification device receipt. My "delivered" funnel stage was mislabelled. |
| **`token_hash` cannot send a push** | The schema omitted the actual provider token, app bundle and environment. A real modelling error. |
| **Transactional and bulk email must not share reputation** | Password resets behind bulk-notification reputation is a genuine outage waiting to happen. I had SES suppression only. |
| **Timezone model was wrong** | 15- and 45-minute offsets exist and DST moves the UTC time, so "24 hourly waves plus half-hour zones" is invalid. Store IANA, compute next local occurrence, convert. |
| **`PK = flush_minute` is one hot partition** | Needs `flush_minute#shard` with leases and bounded claims. |

**Pushed back (partially):**
- **The 90 TB retention figure** rests on Codex's own assumption of 1B notifications/day, which I never
  stated. The *underlying* criticism — that I gave no capacity model at all — is fair and accepted; the
  specific number is not mine.
- **"Fan-out-on-read is a critical flaw"** — the mechanism is correct and standard for the in-app surface.
  The flaw was scope, not mechanism. The crib keeps it for in-app and adds audience snapshotting for
  push/email.

**Still open:**
- The cardinality threshold that flips targeted → broadcast. No principled number without traffic data; say
  it is measured and tuned, and give the shape of the trade-off rather than a fake constant.

---

## 3. Game Leaderboard

**Verdict:** contained the single most embarrassing bug — a sharding scheme that did not shard.

**Conceded — changed the design:**

| Finding | Why I conceded |
|---|---|
| **`lb:{game}:alltime:{shard}` puts every shard on ONE slot** | In Redis Cluster the braces *are* the hash tag, so `{game}` fixes the slot and shards 0..31 all land together. My sharding was a complete no-op. Fix: `lb:{game:3}:alltime`. Then cross-shard `ZUNIONSTORE` is unavailable, which cascades into the rolling-window design. Verifiable with `CLUSTER KEYSLOT` — exactly the kind of thing a Redis-experienced interviewer checks. |
| **Histogram rank error is bounded by bucket POPULATION, not width** | Flatly wrong in my draft. A bucket holding 40M players can mis-rank by ~40M no matter how narrow the score range, absent a distribution assumption. Needs a stated percentile-error SLA. |
| **100M members is tens of GB, not 1–2 GB** | 1–2 GB implies 10–20 bytes/member and ignores dict + skiplist + member strings + allocator overhead, then replicas and failover headroom. |
| **`ZUNIONSTORE` over 7 × 100M every minute is not viable** | And `lb:{game}:rolling7` was unsharded, rebuilding the hot key I had just removed — and cross-slot union does not work anyway. Must be incremental per shard with read-time top-K merge. |
| **Top-K exactness needs a version** | Independent cross-shard reads at different instants produce a merged result that never existed. Exact *for a fixed version under one total ordering*. Also ties: Redis orders equal scores lexicographically by member, so the tie-breaker must be pinned. |
| **Rebuild RTO is not credible** | Events partitioned by player mean rebuilding one game is a full scan; 100M writes at 100K/s is ~17 min before scan and transform. `player_best` cannot reconstruct cumulative sums, rolling sums, expired rolling maxima, or anti-cheat removals. |
| **`ZMSCORE` cannot batch across shards** | It operates on one key. Friend lookups spread over 8–32 shards need routed per-shard pipelines, so "one round trip, single-digit ms" was wrong. |
| **`hash % N` resharding migrates half the data** | Needs virtual shards with dual-read/dual-write migration. |
| **ElastiCache Global Datastore is not multi-master** | And cumulative sums *can* be merged via per-region contribution counters — so my claim was wrong in both directions. |
| **Failover needs an applied-watermark** | Async replication plus "replay from the log" is only correct with per-shard offsets and idempotent materialization. |
| **`ZADD GT` idempotency is conditional** | It holds only while decreases and corrections are impossible. |

**Pushed back (partially):**
- **"Postgres rank is not literally O(n)"** — correct refinement; cost is proportional to qualifying index
  entries because a B-tree offers no order statistics. The conclusion I was drawing — **rank breaks long
  before top-K does** — survives intact, which is the point that matters in the room.

**Still open:** nothing material. This design changed the most.

---

## 4. Video Recommendation

**Verdict:** right priorities, several central claims wrong. Codex's own summary: "invalid negative labels,
nonexistent IPS, sampled feature logging, impossible DynamoDB batching, and putting policy filters last."

**Conceded — changed the design:**

| Finding | Why I conceded |
|---|---|
| **"No engagement" is not a negative label** | The candidate may never have rendered or been scrolled to, and telemetry may be lost. Needs served / rendered / viewable / engaged states, with unobserved exposure treated as **censored**, not negative. The sharpest hit. |
| **Logging position does not enable IPS** | IPS needs the *probability* of exposure at that position, not the realized position. A deterministic ranker has no propensity. Must log propensity and the randomization policy, and use clipped or self-normalized IPS. |
| **My logging policy contradicted itself** | I said log the exact vector for every served candidate, then said retain full snapshots only for shown candidates plus a sample. Those cannot both hold. Fix: log inclusion probability and reweight. |
| **Policy filters cannot run only after ranking** | Backwards in my draft. Hard eligibility must run pre-rank or the ranker scores blocked content and can leave too few items; keep a defence-in-depth pass after. |
| **`BatchGetItem` caps at 100 items / 16 MB** | "One round trip for 500–1000 candidates" is impossible, and DAX does not lift it. Factual error. |
| **p99 does not compose additively** | The fan-out tail is the slowest source, and SageMaker adds queueing and serialization. 120 ms of stages is not a 150 ms p99. |
| **`request_id` is the wrong join key** | Engagement events carry content/session/client ids, and one item appears in many requests. Needs an item-level exposure token returned to the client. |
| **"One definition, two materializations" does not prove parity** | Shared code still diverges on clocks, watermarks, late data, TTLs, nulls and defaults. Needs replay-based golden-vector parity tests on **logged model inputs**. |
| **Failing the feed closed on schema mismatch is worse than the bug** | A routine partial deployment becomes a feed-wide outage. Fall back to the last compatible bundle; fail closed only for unsafe policy data. |
| **Rollback is a bundle, not a pointer** | Model, container, preprocessing, feature contract and policy config must version and deploy atomically. |
| **Firehose is at-least-once** | Needs stable event ids, dedupe and Iceberg compaction. |
| **Privacy and deletion were absent** | Snapshots hold behavioral features and embeddings; Glacier retention is not a deletion story. |
| **Null-rate alone is insufficient** | Misses stale, defaulted, unit-shifted and systematically wrong values, and aggregates hide platform/geo/subreddit/new-user regressions. |

**Pushed back (partially):**
- **"1000× request rate is wrong as stated"** — the direction was right (per-candidate records dominate);
  Codex is right that the factor depends on whether the record is one row with an array or N expanded rows.
  Accepted as a precision fix, and the cost equation now appears in the crib.
- **The 6-hour watermark** — Codex is right that it should come from observed lateness percentiles, but a
  stated placeholder with the reasoning attached is still better in an interview than refusing to give a
  number.

**Still open:**
- Exploration budget (1–5 %). Codex wants it derived from a power calculation. Correct in principle, but
  without traffic numbers the honest answer is to describe the calculation rather than assert a value.

---

## What this changed about how to answer

1. **Do the arithmetic out loud.** Three of the four designs were broken by a number, not by a concept:
   the coalescing multiplier, the histogram error bound, the memory estimate. Every quantitative claim is an
   attack surface, and an invented number is worse than an admitted unknown.
2. **Check where the side effect escapes.** Fencing on the write while the *publish* leaks, filtering after
   ranking while the *ranker* already saw blocked content, protecting fan-out while *ingress* is open — the
   same mistake three times in three designs.
3. **Say what the acknowledgement means.** Provider-accepted ≠ delivered, committed ≠ published,
   admitted ≠ stored, impression ≠ seen. Every one of the four had a mislabelled state.
4. **A snapshot claim needs a version.** "Exact top-K" and "exact feature snapshot" both assumed a
   consistency that concurrent reads do not provide.
5. This is the same gap the mock debrief already flagged — stopping at the high-level idea. The difference
   here is that the next layer is usually **a number or a state name**, not a better structure.
