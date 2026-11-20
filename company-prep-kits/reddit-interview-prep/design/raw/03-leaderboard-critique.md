## 1. Critical Flaws — Technical Soundness

### 1. Redis key names defeat the proposed sharding

**Severity: critical**  
**Confidence: high**

`lb:{game}:alltime:{shard}` uses Redis Cluster hash-tag syntax. Every key containing the same `{game}` hashes to the same slot. Shards `0` through `31` therefore land on one Redis Cluster shard.

The same problem affects the daily and weekly keys. The design either fails to shard or must use a different hash tag per shard, such as `lb:{game:3}:alltime`.

**Recommendation:** Define the actual slotting scheme and verify it with `CLUSTER KEYSLOT`. Use distinct hash tags per physical leaderboard shard. Accept that cross-shard `ZUNIONSTORE` and other multi-key commands then do not work.

### 2. The 100M-player memory estimate is wrong

**Severity: critical**  
**Confidence: high**

“100M players ≈ 1–2 GB” implies roughly 10–20 bytes per member. A Redis sorted set at this size contains both a dictionary and skip-list representation, plus member strings, allocator overhead, and fragmentation. Real usage is likely many gigabytes, commonly tens of gigabytes depending on ID representation and Redis version.

The proposal also needs separate daily, weekly, rolling, and histogram data, replicas, failover capacity, replication buffers, and operational headroom. A single-node estimate is irrelevant to the deployed topology.

**Recommendation:** Benchmark the exact Redis/ElastiCache engine with realistic IDs using `MEMORY USAGE`, then size for replicas, fragmentation, failover, backups, and peak writes. Do not deploy at theoretical maximum memory.

### 3. Rolling-seven-day precomputation is not viable at this scale

**Severity: critical**  
**Confidence: high**

`ZUNIONSTORE` over seven 100M-member sorted sets is `O(total members)`. Repeating that every 1–5 minutes means rewriting potentially hundreds of millions of sorted-set entries continuously. At 100M active players, even a five-minute interval implies processing at least hundreds of thousands of members per second solely for rebuilds.

The declared key `lb:{game}:rolling7` is also unsharded, recreating the original hot-key problem. In Redis Cluster, the source keys would be on different slots, so `ZUNIONSTORE` cannot operate across them.

**Recommendation:** Maintain rolling state incrementally. For sums, add new deltas and schedule expiration/subtraction events. For maxima, retain enough per-player event history to recompute when the current maximum expires. Maintain per-shard rolling boards and merge top-K at read time, or explicitly accept an approximate batch product with a measured processing budget.

### 4. The histogram’s error guarantee is false

**Severity: critical**  
**Confidence: high**

Bucket width bounds score uncertainty, not rank uncertainty. If one bucket contains 40 million players, interpolation can mis-rank a player by nearly 40 million positions regardless of how small the score width is. Without a distribution assumption, the rank error is bounded by bucket population, not bucket width.

The maintenance description is also incomplete:

- An update must decrement the old bucket and increment the new bucket.
- With `ZADD GT`, rejected updates must not alter histogram counts.
- Concurrent updates can race.
- Retries can double-count.
- The histogram hash itself is a global hot key.
- Updating the sorted set and histogram is not atomic across Redis Cluster slots.

**Recommendation:** Define an actual rank or percentile error SLA. Use per-shard counters and merge them, or use a dynamic quantile structure with explicit deletion/update semantics. If exact bucket counts are required, update old score, new score, sorted set membership, and counters atomically within one shard using a Lua script or serialized stream consumer.

### 5. “Top-K stays exact” has missing conditions

**Severity: high**  
**Confidence: high**

The core argument is correct only under these conditions:

1. Each player belongs to exactly one shard.
2. Every shard returns at least its local top K.
3. All shards use the same total ordering.
4. The query represents one consistent snapshot.

The proposal does not satisfy condition 4. Reads from independent shards occur at different times while writes continue. The merged result may never have existed at any instant.

Tie handling is also unspecified. Redis orders equal scores lexicographically by member. If the product requires `score DESC, updated_at DESC`, or another tie-breaker, local Redis ordering may omit globally selected tied members.

**Recommendation:** Define whether the result is a best-effort current view or a snapshot at a stream/version watermark. Encode a deterministic tie-breaker and use it consistently. State that exactness means exact for a static version, not atomic under concurrent writes.

### 6. Rebuild recovery is not operationally credible

**Severity: critical**  
**Confidence: high**

DynamoDB is partitioned by player, not game. Rebuilding one game requires scanning the entire event table and filtering `game_id`, unless another access path exists. DynamoDB scans are expensive and parallelizable, but that does not make them fast enough for an unstated RTO.

At 100M players, even issuing 100M Redis writes at 100K writes/sec takes about 17 minutes before DynamoDB scan time, transformation, network, retries, replication, and validation. At realistic end-to-end rates, recovery can take much longer.

`player_best` is not sufficient:

- It can support all-time max.
- It cannot reconstruct cumulative sums.
- It cannot reconstruct rolling sums.
- It cannot reconstruct a rolling maximum after the current maximum expires.
- It cannot support anti-cheat removal correctly.
- It cannot reconstruct arbitrary historical daily or weekly windows.
- It is not transactionally updated with `score_events`.

**Recommendation:** State an RTO/RPO. Add periodic per-game/per-shard snapshots or materialized checkpoints, retain an event log partitioned for replay, record consumer offsets, and rebuild into a versioned replacement board. Prove recovery time with a load test.

### 7. Failover behavior is understated

**Severity: high**  
**Confidence: high**

ElastiCache replication is asynchronous. A promoted replica can lack acknowledged writes. “Replay from the durable log” is only correct if the system knows exactly which events were applied and can replay them idempotently.

The proposal has no:

- Redis-applied watermark.
- Per-shard checkpoint.
- Handling for Redis writes succeeding while the consumer crashes before recording progress.
- Handling for consumer progress being recorded before Redis writes.
- Read cutover behavior during promotion.
- RPO/RTO target.

“Replica lag is milliseconds” is not a guarantee. Under CPU pressure, network issues, large replication buffers, or failover, lag can be materially larger.

**Recommendation:** Use at-least-once processing with durable event IDs, per-shard offsets, idempotent materialization, and reconciliation. Define whether acknowledged leaderboard updates may disappear during failover.

### 8. The source-of-truth pipeline is not exactly-once

**Severity: high**  
**Confidence: high**

Kinesis does not atomically write to DynamoDB. A Lambda or consumer is required, and delivery is at least once. Kinesis ordering exists only within a shard. The design does not specify deduplication in DynamoDB, consumer checkpoints, dead-letter handling, or event ordering.

Kinesis retention is also finite, so it is not by itself a permanent source of truth.

**Recommendation:** Make `submission_id` the idempotency key and conditionally write events to DynamoDB. Define the consumer’s checkpoint protocol, retry behavior, and permanent retention strategy. Partition the event store for game replay rather than relying on a player-keyed scan.

### 9. Friend leaderboard latency and cost claims are unjustified

**Severity: high**  
**Confidence: high**

`ZSCORE` is `O(log N)` per lookup. `ZMScore` operates on one sorted-set key; it cannot batch scores across the proposed shard keys. A friend list spread across 8–32 Redis shards requires routed pipelines, likely one operation group per shard.

“F ≤ 1000” and “single-digit milliseconds” are not credible general guarantees once graph-service latency, cross-shard network calls, Redis queueing, application sorting, tail latency, and retries are included.

For 100K followed accounts, an exact top-20 requires examining all 100K scores unless a secondary index exists. Pagination does not make the exact query cheaper; it only changes the response shape.

**Recommendation:** Publish explicit degree limits and latency SLOs. Use per-shard pipelines, concurrency limits, caching, and abuse protection. For extreme-degree users, choose explicitly between an approximate result, an asynchronous precomputed index, or a product restriction.

### 10. Time-window semantics are not defined enough to implement

**Severity: high**  
**Confidence: high**

The proposal says “summed or max” but does not define the event model.

- For delta events, use `ZINCRBY`, which requires deduplication.
- For absolute cumulative scores, `ZADD` replaces the value.
- For personal best, `ZADD GT` is appropriate only while decreases and corrections are impossible.
- For rolling sums, expired contributions must be subtracted.
- For rolling maxima, expired maxima must be recomputed.
- Late events require a watermark policy.
- Time zones and week boundaries are unspecified.
- TTL must be based on the fixed window’s expiration, not blindly reset on every write.

**Recommendation:** Specify whether events are deltas or absolute scores, event time versus ingestion time, late-arrival handling, correction/retraction behavior, and tie semantics before selecting Redis commands.

## 2. Significant Concerns — Technical Soundness

### Shard count and resharding are hand-waved

**Severity: high**  
**Confidence: high**

`hash(player_id) % N` makes changing `N` a full data migration. Changing from 16 to 32 moves roughly half the players. The proposal gives no migration, dual-write, backfill, or read-reconciliation procedure.

**Recommendation:** Use stable virtual shards mapped to physical nodes. Move virtual shards independently and support dual-read/dual-write during migration.

### Single-node throughput is not a valid capacity number

**Severity: medium**  
**Confidence: high**

“100–200K ops/sec” depends on instance class, engine version, sorted-set size, command mix, pipelining, replication, persistence, latency target, and failover configuration. It is not an ElastiCache guarantee and does not represent end-to-end write capacity.

The real write path may perform all-time, daily, weekly, histogram, dedupe, and rolling updates. Three `ZADD`s is already an underestimate for most semantics.

**Recommendation:** Capacity-test the actual command mix at the required p99 latency, with replicas and failure injection enabled.

### Replica reads are not automatically acceptable

**Severity: medium**  
**Confidence: high**

Stale data is acceptable only if product requirements explicitly permit it. In a competitive leaderboard, stale top-K results, stale ranks, and a score update not appearing immediately may be unacceptable.

A cross-shard read from replicas can also combine different replication positions, producing a result with no coherent global version.

**Recommendation:** Define read-after-write behavior. Route a player’s immediate reads to the primary or use replication-offset-aware routing. State which endpoints are allowed to be stale.

### Multi-master is not provided by ElastiCache

**Severity: medium**  
**Confidence: high**

ElastiCache Global Datastore provides asynchronous cross-region replication with a primary region and secondary clusters. It is not an active-active multi-master Redis deployment.

Application-level max merging can be designed for high scores, but it requires independent regional event logs and reconciliation. `ZADD GT` does not magically make Redis replication conflict-free. Cumulative scores can also be made mergeable with per-region contribution counters, so the proposal’s “not for cumulative sums” statement is too broad.

**Recommendation:** Use one write region unless active-active is a hard requirement. If active-active is required, design the event-level merge model rather than relying on Redis command commutativity.

### Anti-cheat rebuild has no serving transition

**Severity: medium**  
**Confidence: high**

Filtering bad events and rebuilding a view is not enough. The design needs to prevent bad scores from being reintroduced by lagging consumers and needs a consistent switch from the old board to the corrected board.

**Recommendation:** Materialize a new board version, replay through a validation version, atomically switch the serving version, and retain audit metadata for excluded events.

## 3. Minor Issues — Technical Soundness

### Exact rank semantics are incomplete

**Severity: low**  
**Confidence: high**

`COUNT(*) WHERE score > mine` is competition ranking, not dense ranking or ordinal ranking. Ties are not defined in either the Postgres or Redis design.

**Recommendation:** Define ranking semantics and deterministic tie-breaking at the API level.

### The initial Postgres claim is directionally right but imprecise

**Severity: low**  
**Confidence: high**

Top-K can use the score index efficiently. Rank is not literally always `O(n)`; PostgreSQL may perform an index-only range scan, but it still generally scans the qualifying entries because a B-tree does not provide an order-statistics count.

**Recommendation:** Say rank cost is proportional to the number of qualifying index entries in the normal implementation, rather than simply calling it `O(n)`.

### The histogram does not specify score ranges

**Severity: low**  
**Confidence: high**

Fixed-width buckets can create an unbounded number of buckets as scores grow. Fixed-count or adaptive buckets require merge and rebalance logic.

**Recommendation:** Specify bucket policy, maximum bucket count, and behavior when scores exceed the configured range.

## 4. Interview Follow-Ups the Candidate Cannot Currently Answer

These are the questions that would expose the missing next layer.

### “What Redis Cluster slot do these two keys use?”

`lb:{game}:alltime:0` and `lb:{game}:alltime:1` use the same hash tag and therefore the same slot.

Correct response: change the key-tag scheme, then acknowledge that cross-shard unions are unavailable and must be performed by the application or a separate aggregation layer.

**Severity: critical. Confidence: high.**

### “Prove top-K is exact while writes happen during the query.”

The candidate cannot prove this because the query has no cross-shard snapshot.

Correct response: exact for a fixed version with a shared total ordering; otherwise best-effort current results. Add versioned materialization or explicitly document non-atomic reads.

**Severity: high. Confidence: high.**

### “What is the maximum rank error of your histogram?”

The answer is not “bucket width.” It is up to the population of the relevant bucket, absent distribution assumptions.

Correct response: specify a rank/percentile error target and build the data structure to satisfy that target.

**Severity: critical. Confidence: high.**

### “How do you maintain the histogram when two updates race?”

The current answer has no atomic transition or accepted-update decision.

Correct response: execute old-score lookup, acceptance check, sorted-set update, and counter movement atomically per shard, or serialize updates through a consumer.

**Severity: high. Confidence: high.**

### “How do you build rolling7 for 100M players every minute?”

The proposed full `ZUNIONSTORE` cannot meet that requirement, and the key is unsharded.

Correct response: incremental per-player expiration/aggregation, per-shard serving boards, or an explicitly approximate batch system with measured freshness and compute limits.

**Severity: critical. Confidence: high.**

### “What does one score event mean?”

The design needs to distinguish absolute score, delta, personal best, correction, and retraction.

Correct response: choose one semantic model and derive the Redis command, idempotency key, ordering policy, and window aggregation from it.

**Severity: high. Confidence: high.**

### “What is the RTO after losing the entire Redis cluster?”

The proposal has no number and no credible rebuild procedure.

Correct response: provide snapshots/checkpoints, replay partitions, offsets, parallelism, and a measured RTO.

**Severity: critical. Confidence: high.**

### “Can an exact top-20 friend leaderboard support 100K friends?”

Not with read fan-out alone. Exactness requires inspecting all 100K scores or maintaining an index updated on score changes.

Correct response: define a hard degree limit, asynchronous precomputation, or an approximate result.

**Severity: high. Confidence: high.**

### “How do you reshard from 16 to 32?”

Modulo hashing requires migrating most data and changing query routing.

Correct response: use virtual shards and a controlled migration protocol.

**Severity: high. Confidence: high.**

## 5. What’s Actually Good

- Starting with Postgres for the small opening requirement is correct.
- The distinction between top-K and exact arbitrary rank under sharding is fundamentally correct.
- The top-K merge theorem is correct under static data and a consistent total ordering.
- Rejecting per-user friend ZSETs as the default avoids severe write amplification.
- Treating Redis as a rebuildable materialized view is the right architectural instinct.
- Separating high-score semantics from last-score and cumulative semantics is necessary.
- Server-authoritative scoring and an auditable event history are appropriate.
- The proposal correctly identifies stale reads and rolling-window freshness as product trade-offs, even though the operational details are missing.