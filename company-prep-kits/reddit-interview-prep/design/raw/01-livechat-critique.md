## 1. Critical Flaws

### 1. The fan-out arithmetic exposes a product-level capacity problem

**Severity: critical. Confidence: high.**

At 10 accepted messages/sec:

- `5,000,000 × 10 = 50,000,000` logical deliveries/sec.
- At 20 messages/sec, it is **100,000,000 deliveries/sec**.
- At 300 gateways, 10 msg/sec is roughly **166,667 connection writes/sec/gateway** before batching.

The proposed 250–500 ms batching does not reduce this by 10–50x:

- 250 ms means 4 frames/connection/sec: **20M writes/sec**.
- 500 ms means 2 frames/connection/sec: **10M writes/sec**.

That is only a 2.5–5x reduction at 10 msg/sec. At 20 msg/sec it is 5–10x.

Batching reduces syscall and framing overhead. It does not reduce:

- TLS encryption per connection
- kernel copies
- socket-buffer pressure
- network bytes
- slow-client queues
- internet egress cost

At only 300 bytes/message, 10 msg/sec produces approximately **12.5 GB/sec of outbound payload**, before protocol overhead. At 1 KB/message it is over **50 GB/sec**. That is a multi-terabit-per-second product and cost problem, not merely a gateway problem.

**Recommendation:** State the actual delivery contract. If every client must receive every accepted message, quantify the required fleet, bandwidth, egress cost, and p99 latency. Otherwise introduce message sampling, curated highlights, per-client filtering, or a lower-rate mega-room feed. “Coalesce writes” is not a sufficient scaling strategy.

---

### 2. The gateway capacity number is invented, not designed

**Severity: critical. Confidence: high.**

“30–50K connections per gateway” is not an AWS capacity guarantee. Fargate has no meaningful universal “connections per task” limit. The limiting resources are:

- task memory and runtime overhead
- file descriptors
- TLS state and encryption CPU
- heartbeat frequency
- per-connection send buffers
- outbound bandwidth
- WebSocket frame parsing
- event-loop behavior
- metrics and logging overhead
- NLB target distribution
- subnet ENI/IP capacity
- Fargate vCPU quotas

At 50K connections and 80 KB each, connection state alone is approximately 4 GB. That leaves little room in a typical task for the runtime, buffers, application state, queues, and TLS overhead. A slow-client queue can make this dramatically worse.

Also, “300 gateways” is not 2–3x headroom unless the design specifies:

- task CPU/memory size
- expected bandwidth per task
- heartbeat rate
- AZ distribution
- one-AZ failure capacity
- NLB target distribution
- rolling deployment capacity
- reconnect-handshake capacity

**Recommendation:** Provide a benchmark-based envelope: connections/task, CPU/task, outbound Mbps/task, p99 write latency, buffer limits, and behavior under one-AZ and one-task failure. Treat 50K as an unvalidated hypothesis.

---

### 3. Fencing does not establish ordering across failover

**Severity: critical. Confidence: high.**

A lease plus epoch is directionally correct, but the proposal does not make the ordering guarantee true.

Failure sequence:

1. Old sequencer commits message 100 to DynamoDB.
2. Its outbox relay is delayed.
3. Lease expires and new sequencer owns epoch 2.
4. New sequencer commits message 101 and publishes it.
5. Old relay later publishes message 100.

Clients observe 101 before 100 unless the downstream path performs ordering and buffering. A conditional DynamoDB write prevents stale *new writes* after fencing. It does not retract an already committed outbox event or prevent delayed external side effects.

Kafka also only guarantees ordering within a partition. It does not automatically order messages by `room_seq` when old and new producers publish asynchronously.

**Recommendation:** Define all of the following:

- durable sequence allocation
- epoch validation at the commit boundary
- stale-epoch rejection in the relay
- per-room serialized publication
- handling of delayed old-epoch outbox records
- client behavior for late or missing sequence numbers
- bounded buffering and timeout behavior

The relay must publish only committed, current-epoch messages in sequence order, or clients need a durable ordered log from which they reconstruct order.

---

### 4. The Tier B durability decision is not actually specified

**Severity: critical. Confidence: high.**

The proposal says Tier B messages are “best effort,” but also says clients are told when their messages are rate-limited and that S3 covers moderation and audit.

Those statements conflict unless the product contract is explicit:

- Is an accepted message guaranteed to appear?
- Can it disappear after the sender receives success?
- Is it guaranteed to be available to moderators?
- What is the allowed RPO after Redis failure?
- What happens if S3 archival fails?
- Is a message “accepted” before or after moderation?
- Does the sender receive an acknowledgement of storage, publication, or merely admission?

A capped Redis Stream is not a durable audit log. Asynchronous S3 archival is not a zero-loss guarantee. Redis primary failure, replication lag, eviction, cluster failover, or relay failure can permanently lose accepted messages.

**Recommendation:** Choose one contract:

- **Ephemeral:** explicitly tell users messages may be lost after acceptance and do not call S3 an audit guarantee.
- **Durable:** append to MSK, Kinesis, or DynamoDB before acknowledging acceptance, then archive asynchronously with measurable RPO/RTO.

Do not claim “accepted messages are covered for moderation” while using an asynchronous best-effort archive.

---

### 5. The Tier A data path is incomplete

**Severity: critical. Confidence: high.**

The proposal defines:

1. DynamoDB message and outbox transaction
2. outbox relay to MSK
3. Redis Pub/Sub for live delivery
4. Redis Stream for replay

It never defines how messages get from MSK to Redis Pub/Sub and Redis Streams. It also says replay comes from Redis, not DynamoDB, without explaining how Redis is rebuilt after loss.

This leaves basic failure cases unanswered:

- Redis is unavailable after DynamoDB commit.
- The relay publishes to Kafka but the Redis consumer is down.
- Redis loses the stream during failover.
- A gateway misses Pub/Sub messages.
- Redis is rebuilt from Kafka.
- Kafka consumer lag causes live delivery delay.
- Duplicate Kafka records reach Redis.

**Recommendation:** Make MSK the durable event log and explicitly define the Kafka-to-Redis projector, partitioning by `room_id`, idempotency, recovery, lag monitoring, and replay fallback. If Redis is only a cache, say so and provide a durable replay path.

---

### 6. A single-region design is not sufficient for a globally distributed 5M-user event

**Severity: critical. Confidence: high.**

“Single write region” is a valid ordering trade-off. It is not a complete deployment design.

The proposal does not say whether gateways are:

- all in the write region, creating global latency and a regional blast radius, or
- distributed across regions, requiring cross-region broadcast replication.

If gateways are distributed, Redis Pub/Sub is region-local and does not provide cross-region fan-out. If gateways are centralized, every message and every client connection is concentrated in one region.

**Recommendation:** Specify the topology. A defensible design is one regional sequencer plus regional fan-out clusters, with an explicit cross-region replication protocol, RPO/RTO, and behavior during write-region failure.

---

## 2. Significant Concerns

### 7. Admission control at the sequencer does not protect the ingress path

**Severity: high. Confidence: high.**

Capping visible output at 20 msg/sec does not cap:

- incoming WebSocket frames
- authentication work
- message parsing
- abuse checks
- sequencer requests
- rate-limit lookups
- rejected-message acknowledgements

A malicious client population can generate millions of attempted messages/sec while only 20 are published. The proposal protects fan-out but leaves the highest-volume input path exposed.

“Selection by karma, subscriber status, and moderator status” is also not an implementable fairness policy. Karma is gameable, subscribers may be abusive, and priority selection needs bounded queues and explicit starvation rules.

**Recommendation:** Enforce limits hierarchically at connection, user, IP/device, subreddit, and room levels. Put cheap deterministic filters before the sequencer. Use a bounded admission queue and return explicit `accepted`, `rejected`, and `not-selected` states.

---

### 8. Moderation is reduced to “run ML at 20 messages/sec”

**Severity: high. Confidence: high.**

That does not address the actual abuse surface.

Missing controls include:

- per-user and per-device spam limits
- duplicate-content detection
- ban and mute propagation
- moderator deletion/tombstones
- reports
- blocked-user filtering
- slow mode and subscriber-only mode
- link and malware checks
- brigading detection
- classifier timeout behavior
- classifier outage behavior
- uncertain-content quarantine
- legal/CSAM handling
- audit retention and deletion controls

If moderation is synchronous, the proposal needs a fail-open/fail-closed policy. If the classifier is slow, either message latency explodes or the system publishes unclassified content.

**Recommendation:** Use cheap synchronous policy filters, bounded ML classification before publication, explicit quarantine for uncertain results, and a real-time moderation control plane capable of broadcasting deletes and bans.

---

### 9. Redis Pub/Sub is not a reliable fan-out mechanism

**Severity: high. Confidence: high.**

Redis Pub/Sub is at-most-once delivery. A disconnected gateway misses messages permanently. It has no replay, no backpressure, and no consumer acknowledgement.

Redis sharded Pub/Sub also has important mechanics:

- `SPUBLISH` must be paired with `SSUBSCRIBE`.
- The channel is assigned to a Redis Cluster shard.
- All subscribers for a given channel concentrate on that shard.
- Slow gateway subscribers can accumulate output-buffer pressure.
- Cluster failover and resubscription create message loss windows.

Sharding reduces cluster-wide propagation. It does not eliminate the hot shard or the gateway fan-out cost.

**Recommendation:** Use Redis Pub/Sub only as a low-latency hint path. Make the ordered durable log the source of truth and implement gateway-local sequence-aware recovery. Benchmark hot-room output buffers, shard CPU, failover loss, and resubscription behavior.

---

### 10. Redis Stream replay creates a thundering herd

**Severity: high. Confidence: high.**

One gateway failure produces roughly 40K reconnects. If each client independently requests a replay range, Redis receives 40K similar range reads against the same stream key. A 30-second outage at 20 messages/sec creates a 600-message replay per client:

`40,000 × 600 = 24,000,000` message entries

That is before sending those messages back over the new connections.

The design has no:

- gateway-level replay coalescing
- replay rate limiter
- maximum replay size
- snapshot/resync behavior
- protection against malicious range requests
- policy for unreplayable gaps

“Never hit DynamoDB” simply moves the failure to a hot Redis key.

**Recommendation:** Have the new gateway fetch each missing range once, cache it, and distribute it to reconnecting clients. Bound replay size and return a “stream reset” marker when the gap exceeds retention.

---

### 11. NLB does not provide the proposed room-aware routing

**Severity: high. Confidence: high.**

NLB supports TCP and therefore WebSockets, but it does not route based on WebSocket room membership. It cannot inspect the upgraded application protocol and send mega-room clients to a dedicated pool.

The design needs an explicit bootstrap flow:

1. Client authenticates with a room-aware service.
2. Service returns the correct pool endpoint.
3. Client connects to that endpoint.
4. The endpoint has its own NLB target group or listener.

NLB also has TCP idle timeouts, with the default commonly 350 seconds. Heartbeats are still required. ALB’s idle timeout is not a reason by itself to reject ALB; ALB supports WebSockets and configurable idle timeouts.

**Recommendation:** Define room-to-pool routing before the WebSocket connection. Document NLB listener, target-group, DNS, health-check, deregistration, and quota behavior.

---

### 12. Rebalancing by asking clients to reconnect can create the exact storm being avoided

**Severity: high. Confidence: high.**

Scale-out does not move existing connections. Asking even 10% of 5M clients to reconnect means **500,000 simultaneous migration candidates**.

Jitter alone is insufficient if all clients receive the same migration instruction. The design needs a migration rate budget and server-side admission control.

**Recommendation:** Use tokenized rolling migration:

- assign clients deterministic migration windows
- send `Retry-After`
- close only a bounded number per second
- reject excess handshakes before expensive authentication
- keep old capacity until migration completes
- use connection draining during deploys and AZ evacuation

---

### 13. The DynamoDB schema has no retention or partitioning strategy

**Severity: medium. Confidence: high.**

`PK = ROOM#room_id` with an ever-growing message history creates a permanently hot logical partition and an unbounded item collection. It also makes retention, range queries, and archival awkward.

DynamoDB transactions support the two-item write, but they do not solve the long-term storage model. The design also omits:

- message size limits
- idempotency keys
- retention policy
- time bucketing
- pagination
- duplicate client retries
- transactional write capacity cost

**Recommendation:** Use time-bucketed or sequence-bucketed keys, a client idempotency key, bounded message size, explicit retention, and a separate durable log/archive strategy.

---

### 14. The Redis Stream retention claim is mathematically wrong

**Severity: medium. Confidence: high.**

`MAXLEN ~1000` means approximately 1,000 entries, not 60 seconds.

At:

- 20 msg/sec: roughly 50 seconds
- 10 msg/sec: roughly 100 seconds
- 1 msg/sec: roughly 16 minutes

Approximate trimming also permits temporary excess entries.

**Recommendation:** Use time-based trimming if the requirement is 60 seconds, or state retention strictly as “approximately 1,000 accepted messages.” Add a sequence-based gap-reset behavior.

---

### 15. Presence is connection count, not participant count

**Severity: low. Confidence: high.**

Summing gateway-local connections measures sockets. It does not measure distinct users. Multiple tabs, reconnects, shared accounts, and duplicate sessions all distort the count. HyperLogLog helps only if every gateway contributes stable user identifiers and the merge lifecycle is defined.

**Recommendation:** Label the metric “active connections” unless distinct-user estimation is implemented. For distinct users, merge per-gateway HLLs over a defined time window and account for churn.

---

## 3. Minor Issues

### 16. Snowflake is a poor choice for the only stated reason

**Severity: low. Confidence: high.**

Snowflake IDs are globally unique when worker identity and clock discipline are correct. They are not intrinsically safe under clock rollback or misconfigured worker IDs.

**Recommendation:** Use UUIDv7 or another monotonic-enough unique identifier unless Snowflake infrastructure already exists and clock/worker fencing is documented.

---

### 17. “Client gap detection” needs a defined gap model

**Severity: medium. Confidence: high.**

A coalesced range assumes contiguous sequence numbers. The proposal does not define whether rejected messages consume sequence numbers. If they do, clients see permanent gaps. If they do not, the sender needs a separate admission result.

**Recommendation:** Assign `room_seq` only to accepted, publishable messages. Give rejected attempts an independent request ID and explicit rejection status.

---

### 18. Per-message S3 archival is operationally poor

**Severity: low. Confidence: high.**

S3 is not an append log. One object per message creates excessive request and metadata overhead. Async buffering also creates an undefined loss window.

**Recommendation:** Batch messages into time/room-partitioned objects through a durable stream consumer, with checksums, manifests, retention, encryption, and a stated RPO.

---

### 19. The “single sequencer is never the bottleneck” statement misses the real bottlenecks

**Severity: medium. Confidence: high.**

20 messages/sec is trivial CPU-wise. The sequencer still creates:

- a per-room availability dependency
- failover latency
- lease-renewal sensitivity
- a durable sequence-allocation hotspot
- a serial moderation/admission path

**Recommendation:** Measure and specify failover time, lease duration, sequence allocation behavior, and acceptable write unavailability during ownership transfer.

---

## 4. Interview Follow-Ups the Candidate Cannot Currently Answer

### 1. “What is the actual bandwidth and egress cost at 5M users?”

**Severity: critical. Confidence: high.**

Correct response: calculate payload size, framing, TLS overhead, heartbeat traffic, regional traffic, and AWS internet egress. If every client receives every message, the system may be economically or physically unacceptable. The candidate must change the product contract or show the fleet and cost.

---

### 2. “Show the exact failover sequence when an old sequencer’s outbox relay is delayed.”

**Severity: critical. Confidence: high.**

Correct response: use durable sequence state, epoch fencing at every write/publish boundary, per-room ordered relay behavior, and handling for late old-epoch records. “The write is conditional on epoch” is insufficient.

---

### 3. “What happens to messages when Redis Pub/Sub drops a gateway?”

**Severity: critical. Confidence: high.**

Correct response: Pub/Sub is lossy; the durable log or replay source recovers the gap. The answer must describe replay coalescing, rate limiting, retention exhaustion, and the behavior when the gap is too large.

---

### 4. “What exactly does a successful send acknowledgement mean in Tier B?”

**Severity: critical. Confidence: high.**

Correct response: distinguish admission, moderation, durable storage, publication, and display. If success means durable acceptance, Redis/S3 best effort is invalid. If it means ephemeral admission, the UI and API contract must say so.

---

### 5. “How does the message get from MSK to Redis?”

**Severity: high. Confidence: high.**

Correct response: identify the Kafka consumer/projector, partition key, idempotency mechanism, lag handling, Redis failure recovery, and replay source. The proposal currently omits the core live-delivery pipeline.

---

### 6. “How does an NLB know this connection belongs to the mega-room pool?”

**Severity: high. Confidence: high.**

Correct response: it does not. Use a room-aware bootstrap service and separate endpoint, listener, target group, or DNS name before opening the WebSocket.

---

### 7. “What is the tested capacity of one Fargate task?”

**Severity: high. Confidence: high.**

Correct response must include task size, connection state, TLS CPU, heartbeats, outbound Mbps, send-buffer limits, file descriptors, runtime, and p99 latency. “40–80 KB per connection” is not a capacity model.

---

### 8. “How do you survive a gateway or AZ failure without a reconnect storm?”

**Severity: high. Confidence: high.**

Correct response: jitter plus server-side handshake admission, reconnect token buckets, replay coalescing, connection draining, spare capacity, and one-AZ failure calculations. Full jitter alone is not enough.

---

### 9. “What happens when the moderation service times out?”

**Severity: high. Confidence: high.**

Correct response: define fail-open versus fail-closed by content category, quarantine uncertain messages, cap classifier latency, and preserve moderator deletion/tombstone propagation.

---

### 10. “What happens when the write region fails?”

**Severity: high. Confidence: high.**

Correct response: define RPO, RTO, ownership fencing across regions, sequence continuity, replicated event-log behavior, and whether the room becomes read-only or unavailable.

---

## 5. What’s Actually Good

- Separating mega-rooms from normal rooms is correct.
- The proposal correctly identifies fan-out, not sequencing CPU, as the dominant scale problem.
- Per-room sequence numbers are better than using wall-clock IDs for ordering.
- Epoch fencing is the right direction, though the publication protocol is incomplete.
- Pre-provisioning scheduled events is necessary.
- Jittered reconnects, bounded replay, approximate presence, and dedicated capacity pools are all sound design instincts.
- The proposal at least states its durability trade-off instead of pretending that every 5M-user event has ordinary chat semantics.

The answer has the right instincts, but it currently fails on the two issues a Staff interviewer will attack first: **the physical cost of 5M fan-out** and **the correctness of failover/replay ordering**.