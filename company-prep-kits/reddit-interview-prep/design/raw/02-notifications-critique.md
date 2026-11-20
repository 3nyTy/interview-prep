# 1. Critical Flaws

## A. Technical Soundness

**1. Broadcast fan-out-on-read is incompatible with push and email.**  
**Severity:** critical. **Confidence:** high.

A broadcast stored once can support an in-app pull feed. It cannot deliver push or email without eventually enumerating eligible recipients. SNS, APNs, FCM, and SES do not query a user’s subscriptions at send time.

The design also uses current subscriptions, which can notify users who subscribed after the event or omit users who unsubscribed before delivery. That is a product and privacy bug.

**Recommendation:** Separate broadcast storage from delivery. Use fan-out-on-read only for in-app. For push/email, snapshot or batch the eligible audience at event time, apply preferences, and fan out through a campaign/broadcast dispatcher. Use a hybrid threshold for large audiences.

---

**2. The badge-count algorithm is both expensive and semantically incorrect.**  
**Severity:** critical. **Confidence:** high.

`count(inbox where ts > last_read_at) + count(broadcasts where ts > last_read_at)` does not work with:

- Multiple subreddit streams
- Subscription and unsubscription boundaries
- Events arriving late
- Clock skew
- Selective reads
- Multiple devices advancing the read pointer
- Broadcasts that existed before the user subscribed

DynamoDB cannot efficiently count all records after a timestamp across many partition keys. A `Query` with `Select=COUNT` still reads the matching items and consumes capacity. A user subscribed to 500 subreddits creates a 500-query read path before counting anything.

“Capped and cached” changes the contract to approximate activity, not a badge count. The proposal does not define that contract.

**Recommendation:** Use a monotonic server-assigned sequence and explicit read cursors. Materialize targeted unread counters. For broadcasts, maintain per-subscription cursors and expose a clearly approximate “new activity” indicator unless the system is willing to fan out counters per user.

---

**3. The idempotency story does not prevent duplicate or lost sends.**  
**Severity:** critical. **Confidence:** high.

The deterministic ID and conditional DynamoDB write protect one inbox item. They do not make the whole pipeline idempotent.

Failure cases:

- Inbox write succeeds, SQS enqueue fails.
- Retry sees the existing inbox row and skips enqueueing.
- SQS delivers twice.
- A dispatcher times out after APNs, FCM, or SES accepted the message.
- The dispatcher retries and sends a duplicate.
- A DLQ replay occurs after the original provider call succeeded.
- A bundled notification has a different identity from each source event.

APNs, FCM, and SES do not provide generic exactly-once delivery keyed by the application’s notification ID.

**Recommendation:** Model a logical notification separately from channel delivery attempts. Persist a durable dispatch state/outbox, use conditional state transitions, record provider acceptance, and make retries provider-aware. Explicitly state that external delivery is at-least-once and that duplicate user-visible delivery is possible unless content-level coalescing prevents it.

---

**4. Bundling has lost-update and duplicate-flush races.**  
**Severity:** critical. **Confidence:** high.

Atomic `ADD` only protects the increment. It does not protect the lifecycle transition.

Example:

1. Flush worker reads count `5`.
2. A reply atomically increments it to `6`.
3. Flush worker deletes or resets the bundle using stale state.
4. Reply `6` is lost.

Another worker can flush the same bundle concurrently and send two messages. Two concurrent first replies can both believe they created the first notification unless creation is conditionally serialized.

`sample_actors` also cannot safely be maintained as an arbitrary capped list using only `ADD`.

**Recommendation:** Use bundle generations and conditional leases. Atomically close generation `N`, create generation `N+1` for new events, and flush an immutable snapshot identified by a unique delivery ID. Persist per-channel flush state. Use a conditional “claim” with fencing/version checks.

---

**5. Kafka partitioning does not provide user-visible ordering.**  
**Severity:** critical. **Confidence:** high.

A Kafka key orders records only within one topic partition, according to Kafka arrival order. It does not solve:

- Multiple producers publishing events at different times
- Cross-topic targeted and broadcast events
- Router concurrency
- Standard SQS reordering
- Retries
- Multiple devices
- APNs/FCM delivery order
- Email delivery order

“Your post was removed” can still arrive after “your post was approved.”

**Recommendation:** Define the required ordering precisely. For causal ordering, assign per-entity or per-user sequence/version numbers upstream and suppress stale events. Use ordered dispatch only where needed. Do not claim provider-level ordering that the providers cannot guarantee.

---

# 2. Significant Concerns

## A. Technical Soundness

**6. The targeted/broadcast boundary is drawn by semantics instead of cardinality.**  
**Severity:** high. **Confidence:** high.

A “targeted” event can have millions of recipients: a popular mention, moderator action, award notification, or follower event. A “broadcast” event may have only a few thousand eligible users.

**Recommendation:** Classify by recipient cardinality, fan-out cost, freshness, and delivery channel. Use thresholds and a hybrid path rather than fixed event types.

---

**7. The broadcast read path will have bad tail latency and hot partitions.**  
**Severity:** high. **Confidence:** high.

DynamoDB has no native merge across partition keys. The service must query every subscribed subreddit, merge and sort the results, paginate, and handle failures. Popular subreddit partitions also become read hotspots.

**Recommendation:** Bound the number of streams queried per request, cache hot broadcast data, maintain a per-user subscription index, and use pre-materialization or hybrid fan-out for high-read or high-subscriber streams.

---

**8. `PK = USER#<user_id>` creates hot-user bottlenecks.**  
**Severity:** high. **Confidence:** high.

The proposal gives every write and read for a prolific user one partition key. DynamoDB partitions have finite throughput, approximately 1,000 WCU and 3,000 RCU per physical partition under the standard limits. Adaptive capacity is not a substitute for capacity planning.

At one billion notifications per day, the average is about 11,600 notifications per second before peak factors, recipient expansion, retries, and channel multiplication. No peak model is provided.

**Recommendation:** Calculate peak fan-out and use sharded user keys for pathological users, with a mergeable read index. Define capacity, retry, and regional-failover budgets.

---

**9. The proposed retention can create an enormous DynamoDB dataset.**  
**Severity:** high. **Confidence:** high.

Ninety days of one billion logical rows per day is 90 billion rows. At only 1 KB per item, that is roughly 90 TB of logical item data before indexes, replication, backups, and storage overhead.

**Recommendation:** Store one logical inbox item rather than one row per channel, cap per-user history, archive older data to S3, and define whether every push generates an inbox record.

---

**10. The dispatch handoff is an unaddressed dual-write problem.**  
**Severity:** high. **Confidence:** high.

The router writes DynamoDB and sends to SQS. Those operations are not atomic. The proposal has an outbox from producer stores to Kafka, but no outbox or stream-backed mechanism from inbox persistence to channel dispatch.

**Recommendation:** Write a durable dispatch intent with the inbox mutation and publish it via DynamoDB Streams or a dedicated outbox processor. Make the processor replayable and state-based.

---

**11. Preference checks are not authoritative if Redis is the source of the check.**  
**Severity:** high. **Confidence:** high.

A short-TTL Redis cache can send after a user opts out. The dispatch check can also race with the opt-out immediately afterward. The proposal does not define whether preference changes cancel queued work, whether already accepted provider messages are allowed, or whether bundled events retain their original preference state.

**Recommendation:** Use versioned preferences with invalidation events and a strongly consistent source of truth at the final admission point. Define a clear linearization point and accept that messages already accepted by a provider cannot be recalled.

---

**12. Quiet-hours and timezone handling is underspecified and wrong at the stated granularity.**  
**Severity:** high. **Confidence:** high.

“24+ hourly waves plus half-hour zones” is not a valid timezone model. Current zones include 15- and 45-minute offsets, and DST changes the UTC execution time. Quiet hours can cross midnight, users can change timezone, and daylight-saving transitions can create ambiguous or nonexistent local times.

**Recommendation:** Store the user’s IANA timezone, compute the next local occurrence using a current tz database, convert it to UTC, and enqueue a durable due task. Make `(user, digest_period)` idempotent and define behavior for timezone changes and missed runs.

---

**13. Redis throttling is not durable and silently loses channels.**  
**Severity:** high. **Confidence:** high.

ElastiCache failover can lose token-bucket state. The proposal does not define fail-open versus fail-closed behavior. “Suppress push/email” is dropping those channels, not merely throttling them.

There are also missing global limits: provider quotas, domain-level email rates, APNs/FCM project limits, and reputation-aware email pacing.

**Recommendation:** Implement atomic bucket operations, durable deferred work, retry-after scheduling, global and per-provider quotas, and explicit channel policy. Distinguish temporary throttling from permanent suppression.

---

**14. The token model cannot send a push.**  
**Severity:** high. **Confidence:** high.

`token_hash` is useful for lookup and deduplication, but the actual APNs/FCM token must be stored somewhere. The model omits it, along with app bundle/topic, environment, tenant, token ownership, logout/reassociation behavior, and encryption.

**Recommendation:** Store the encrypted provider token separately, keyed by a hash, with app/environment metadata. Handle APNs 410 responses, FCM `UNREGISTERED` and token-refresh responses, registration races, multi-account devices, and endpoint reactivation.

---

**15. “APNs/FCM receipts” are not what the proposal claims.**  
**Severity:** high. **Confidence:** high.

Provider acceptance is not device delivery. APNs does not provide a per-notification device-open receipt for ordinary push notifications. FCM delivery metrics are not equivalent to synchronous per-message delivery confirmation. SNS mobile push status does not turn this into an end-to-end receipt system.

**Recommendation:** Track separate states: accepted by dispatcher, accepted by provider, provider rejection, client receipt, and client open. Use client acknowledgements for actual delivery/open metrics and do not label provider acceptance as delivery.

---

**16. SES handling is far too shallow for a reputation-critical system.**  
**Severity:** high. **Confidence:** high.

A suppression list is necessary but insufficient. The design omits transient versus permanent bounce handling, complaint processing, domain-level reputation, sending identity separation, rate ramping, feedback loops, configuration sets, unsubscribe semantics, and retry backoff.

Putting transactional recovery email behind the same reputation path as bulk notification email is dangerous.

**Recommendation:** Separate transactional and bulk streams, use SES configuration sets and event destinations, suppress permanent bounces and complaints, retry only transient failures, rate-limit by recipient domain, and isolate sender identities and reputation pools.

---

## B. Numerical and AWS Claims

- The 90-day and 30-day TTLs are retention policies, not deletion deadlines. DynamoDB TTL deletion is asynchronous.
- A one-minute flush sweep is not a capacity model. A single `flush_minute` partition becomes a hot partition at scale.
- “24+ waves” omits real timezone offsets and DST.
- No connection count, latency budget, peak throughput, queue-age SLO, or AWS quota calculation is supplied. Therefore the claimed billion-scale design is not capacity-justified.
- SQS standard queues are at-least-once and unordered. FIFO deduplication is limited to a five-minute deduplication interval, so it does not solve DLQ replay hours later.
- DynamoDB atomic `ADD` is valid for counters, but it does not atomically coordinate the counter with scheduling, closing, flushing, or provider dispatch.

# 3. Minor Issues

**17. Timestamp sort keys are not a reliable ordering primitive.**  
**Severity:** medium. **Confidence:** high.

Client or producer timestamps can collide, arrive late, or move backward. `ts#notification_id` also makes read-pointer semantics vulnerable to clock skew.

**Recommendation:** Use a server-assigned sequence or sortable server ID and store event time separately for display.

---

**18. The schema conflates logical notifications and channels.**  
**Severity:** medium. **Confidence:** high.

The ID includes `channel`, but the inbox is also the source of truth. That can create separate inbox rows for push, email, and in-app delivery, inflating counts and showing duplicates.

**Recommendation:** Store one logical notification and separate channel-delivery records.

---

**19. Bundling and digesting are not reconciled.**  
**Severity:** medium. **Confidence:** high.

The proposal has immediate delivery, time-window bundles, and scheduled daily digests, but no precedence rules. It is unclear whether a reply included in an immediate bundle can later appear in a digest, or whether push and email use the same bundle.

**Recommendation:** Define a canonical inclusion state per logical event and channel-specific delivery windows.

---

**20. The scheduler partition key is insufficient.**  
**Severity:** medium. **Confidence:** high.

`PK = flush_minute` concentrates all work for a minute into one logical partition and requires workers to scan potentially millions of items.

**Recommendation:** Use `PK = flush_minute#shard`, deterministic shard assignment, leases, and bounded batch claims.

---

**21. Observability overstates what “delivered” means.**  
**Severity:** medium. **Confidence:** high.

The funnel needs queue age, provider acceptance, provider rejection class, token invalidation, bounce rate, complaint rate, domain reputation, preference-race drops, throttle deferrals, and DLQ age. “Opened” is not available for every channel.

**Recommendation:** Define channel-specific event semantics and separate operational metrics from client-confirmed engagement.

# 4. Interview Follow-Ups the Candidate Cannot Currently Answer

## B. Interview Exposure

**1. “How does a trending notification reach a user’s phone if the user never opens the app?”**  
**Severity:** critical. **Confidence:** high.  
**Correct response:** Fan-out-on-read applies only to in-app retrieval. Push and email require audience resolution and dispatch, using a snapshot or scheduled batch with preference and eligibility filtering.

**2. “A user subscribes to 1,000 subreddits. How many DynamoDB reads does opening the notification page perform, and what is the P99?”**  
**Severity:** high. **Confidence:** high.  
**Correct response:** Provide a bounded read strategy, cache/hot-stream handling, pagination semantics, and a hybrid materialization threshold.

**3. “What does the badge show after a user unsubscribes and resubscribes to a subreddit?”**  
**Severity:** high. **Confidence:** high.  
**Correct response:** Define subscription start cursors, per-stream read cursors, and whether historical broadcasts are eligible. A single global timestamp is not sufficient.

**4. “DynamoDB writes the inbox row, then SQS enqueue fails. What causes the retry to enqueue the message?”**  
**Severity:** critical. **Confidence:** high.  
**Correct response:** A durable dispatch outbox or stream-driven state machine. The conditional inbox write alone is insufficient.

**5. “Two replies arrive while a flush worker is closing the bundle. Show every state transition.”**  
**Severity:** critical. **Confidence:** high.  
**Correct response:** Versioned bundle generations, conditional close/claim, immutable flush snapshots, and a new open generation for concurrent arrivals.

**6. “APNs times out. Did the device receive the push?”**  
**Severity:** critical. **Confidence:** high.  
**Correct response:** Unknown. Retry can duplicate. Record provider acceptance separately from client receipt and design the user experience around at-least-once delivery.

**7. “The user disables push between the final preference read and the APNs call. Is the notification sent?”**  
**Severity:** high. **Confidence:** high.  
**Correct response:** Define the admission linearization point, invalidate queued work, and state that already accepted provider requests cannot be revoked.

**8. “How do you guarantee ‘removed’ does not arrive before ‘approved’ when those events come from different services?”**  
**Severity:** critical. **Confidence:** high.  
**Correct response:** Carry causal/entity versions or assign a sequencing number upstream. Kafka partitioning alone is not enough, and provider delivery order cannot be guaranteed.

**9. “What happens when a DLQ message is replayed six hours later after SES already accepted the original?”**  
**Severity:** critical. **Confidence:** high.  
**Correct response:** Durable channel delivery state, replay-aware state transitions, provider acceptance recording, and an explicit duplicate-send policy. The notification hash alone does not solve this.

**10. “Give the partition count, peak records per second, queue capacity, and monthly DynamoDB cost for one billion notifications per day.”**  
**Severity:** high. **Confidence:** high.  
**Correct response:** Start with peak/average assumptions, recipient expansion, channel ratios, item sizes, retry rates, regional redundancy, and retention. The proposal currently supplies none of this.

**11. “What happens to an 08:00 digest during DST or after the user changes timezone?”**  
**Severity:** high. **Confidence:** high.  
**Correct response:** Compute the next occurrence with IANA timezone rules, persist the intended digest period, and make execution idempotent.

**12. “Which exact APNs/FCM feedback events prune tokens, and how do you distinguish a transient provider outage from an invalid token?”**  
**Severity:** high. **Confidence:** high.  
**Correct response:** Enumerate provider error classes, retry transient failures, invalidate permanent failures, and re-register tokens on client refresh.

# 5. What’s Actually Good

- The proposal correctly recognizes that targeted and broadcast workloads have different cost shapes.
- The transactional-outbox instinct is correct for producer-side event publication.
- Checking preferences both before queueing and near dispatch is the right direction.
- Atomic counter updates are necessary for concurrent bundling.
- Token invalidation, complaint suppression, DLQs, and explicit trade-offs are important concerns many designs omit.
- The proposal correctly rejects global ordering as unnecessary, but it overclaims what Kafka partitioning can guarantee.