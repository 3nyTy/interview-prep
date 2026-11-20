# Reddit Interview Crib — Plain-English Cheat Sheet

**How to use, live:** name a design → read **Open with** (your first 30 seconds), then work down the points. The **bold phrase** is what you say out loud.

**Hard rules for this file:** max 10 numbered points per design — major problems only, not caveats. Each point is one line. Everything else lives in **Detail**, which you do *not* read live.

**Ordering:** sorted by **cost of getting it wrong**. Designs ordered by prep priority — Q1 is the only one with Staff-level Reddit reports.

**Validated:** every design below was stress-tested against Codex (adversarial staff-interviewer persona) on 2026-08-14 and revised. The recurring correction was the same one your OpenAI kit already records: **conditional design choices had been written as guarantees.** Absolutes are what get probed.

**Jump to:** [1 News Aggregator](#q1) · [2 Historic Hot Posts](#q2) · [3 Notifications](#q3) · [4 Circuit Breaker](#q4) · [Repeating ideas](#cross)

---

<a id="q1"></a>
## Q1 — News Aggregator (personalised real-time feed)
**Open with:** ingestion is the hard half, not the feed — **cluster near-duplicates without destroying the source records**, **fan out to shared interest buckets rather than per-follower feeds**, and **paginate against a pinned list version**.

1. Takedowns, retractions and moderation must actually propagate → **A tombstone plus a serve-time visibility check, because versioning creates new data and never removes the old.** Cached pages, CDN objects, already-queued pushes and search indexes can all still hold the withdrawn item — say that out loud and bound the stale window.
2. The same story arrives from twenty syndicated sources → **Cluster on a content signature and keep every source document; canonical is a read-time choice, not a delete.** Clustering is probabilistic, so you need merge *and* split, and two reports of one event can carry genuinely different facts.
3. How a feed gets assembled → **Fan out to shared interest buckets — source, topic, keyword — and assemble a user's page by merging their buckets.** This removes per-follower write amplification; it does *not* remove the hotspot, it relocates it to the popular bucket, which you then shard by time or by hash.
4. Read latency at p95 → **Merge K bucket heads in one batched multi-get, with K capped and the drop policy stated, and dedup on cluster id at merge.** Never join fresh articles against a subscription table per request, and say what happens when one bucket is unavailable.
   ↳ **The hot bucket is the easy case — hot means cached means fast.** The slow read is the user with hundreds of *cold* subscriptions: K cache misses shared with nobody. Fix with batching, a stated cap, and a materialised per-user feed for the high-K/high-activity percentile — **the inverse of the Twitter hybrid, absorbing hot readers rather than hot writers.**
5. Order changing while you paginate → **Never OFFSET; and a cursor alone isn't enough, because scores move, clusters merge, and the feed is a merge of K independently-advancing buckets — so there is no single list for a cursor to be consistent with.** Pin the ordering, then page inside it.
   ↳ **Pin a *time*, not a list, whenever the score is a pure function of (item, T)** — recency decay × source weight × topic match qualifies, so "as of T" is just an insertion-time filter and you store **zero per-user state**. Only when the score depends on live engagement counts do you freeze the actual list in Redis under a token with a short TTL.
   ↳ **This is not fan-out-on-write in disguise:** ingest cost stays O(buckets touched), snapshots are built on read, they exist only for sessions that scroll, and losing one costs a rebuild rather than a repair.
   ↳ New items arriving mid-scroll are **a "12 new stories" pill and a fresh snapshot — never an injection into the pinned order.** Say that's a product decision, not a limitation.
6. Thousands of heterogeneous sources → **Per-source adapters that normalise into one envelope, with an idempotency key of (source, guid) falling back to a content hash.** Name the failure: a stable guid with a changed body is an update, not a new story.
7. Mutes and unfollows have to bite immediately → **Filter at serve time and overfetch, because a user who mutes their hottest topic makes the page-fill cost unbounded otherwise.** Read-time filtering is cheap on average and needs a bound on the tail.
8. Ranking → **Say you're keeping ranking rule-based — recency decay × source weight × topic match — and spending the budget on ingestion correctness.** Expect them to push anyway on source-weight bootstrapping and coverage diversity; have a sentence, not a design.
9. Breaking-news push → **Push the story id, not the payload, through the async notification path with per-user caps — and re-check visibility at fetch.** Otherwise a retracted story is already in someone's notification tray.
10. One slow publisher must not starve the rest → **Per-source token bucket, bounded concurrency and a fetch timeout — rate limiting alone doesn't isolate a hung fetch.**

**Ingestion concurrent with a read** — four rules, all cheap to state: **pick a read horizon T at request start and filter every bucket to `insertion_time <= T`**, which turns K staggered reads into one consistent view with no locking. **Publish visibility last** (bucket append after the record is durable and hydratable) or readers get ids that don't render. **Additions are deferred, removals are immediate** — say that asymmetry is deliberate, because stable order is worth seconds of delay and serving removed content is worth none. **Never invalidate a hot bucket's cache on append** — a topic taking ten articles a second would never keep a cache warm, which destroys the property that made hot buckets cheap; let TTL bound it and write through only for a breaking item that's already going out as a push. Cluster ids are stable-with-forwarding: a merge writes a redirect, ids are never reused, bucket history is never rewritten.

**Detail:** near-dup at scale = shingle the body, 64-bit SimHash, Hamming ≤3 as a *heuristic* whose threshold depends on language and boilerplate, with a blocking key so you compare within candidate groups. Anchor the scale before designing: order 10⁴–10⁵ sources, order 10⁶ articles/day, feed reads dominating writes by 3–4 orders of magnitude — those numbers are what make "in-memory" and "cap K" defensible. Language and region are a ranking and filtering dimension, and only a partition key if you accept key explosion. Personalised feeds can still be cached privately by snapshot version; only anonymous and regional ones are CDN-cacheable. If pushed on cluster quality, concede it degrades on aggressive rewrites and fall back to a per-source cap inside a cluster.

---

<a id="q2"></a>
## Q2 — Historic 'Hot' Posts Page (top posts for any past date)
**Open with:** offline materialisation, not a request-time sort — **the *ranking* is frozen per date and scope, the *response* is not**, because current moderation state is applied at serve time. **Popular scopes are precomputed; the long tail is computed on demand.**

1. Never rank at query time for the scopes that matter → **Precompute Top-N per (date, scope) in a batch job and serve ranked ids from a key lookup; compute the long tail on demand and cache it.** Say which side a given scope falls on — claiming nothing is ever computed per request contradicts the long-tail path.
2. Ranking stable, response not → **The snapshot is immutable and versioned; deleted, removed and quarantined posts are filtered at serve time, so pages have holes you refill by overfetching.** State plainly that a post removed tomorrow disappears from yesterday's page — that is the product decision, and pretending the page is byte-stable is the trap.
3. Determinism → **Fix the window (UTC day), the score formula, the evaluation instant, and an explicit tie-break on (score, post_id).** Ties aren't merely unstable, they're *unspecified* — which is what makes a backfill unreproducible.
4. What "hot on that day" means → **Score from signals inside that day's window, and say out loud that this is a product decision, not a technical one.** Per-day deltas are sufficient only if the score decomposes from them; vote reversals and non-additive features break that.
5. Rendering the page → **The key lookup returns ids; hydrate in one bulk fetch and tolerate misses.** Titles, authors, thumbnails and current moderation state all come from live services — a ranking service is not a page.
6. Event correctness → **Event-time windows with a watermark, idempotent handling of duplicate and late events, and a stated policy for vote reversals.** This is where the answer is actually won; processing-time windows quietly produce a different answer on every run.
7. Late data, corrections and reprocessing → **Publish a new snapshot version and commit it with a manifest, then flip the pointer — never mutate a published snapshot, and never let a reader see a pointer whose parts aren't all present.**
8. Global reads, cheaply → **CDN-cache the *ranked ids* keyed by (date, scope, version) with a long TTL; do the visibility and hydration behind it.** Caching the rendered page is unsafe — it will keep serving removed content, and privacy, NSFW and regional policy aren't in that key.
9. Pagination → **Cursor is (snapshot version, position), with overfetch so a filtered page still fills.** Position alone breaks the moment visibility filtering removes entries.
10. Storage across years → **Keep Top-N per (date, scope), N in the low thousands, and define the response beyond N.** Removals eat into N, so N is a product cap you state rather than a depth guarantee.

**Detail:** name the day-boundary rule explicitly, and keep eligibility (created in-window) separate from scoring (activity in-window) separate from the evaluation instant. Distributed Top-K by local heaps is exact only when the score aggregates correctly from partition-local state and pruning is monotone — say that condition rather than "never a global sort". Backfill reproducibility needs the algorithm and config version pinned alongside the event log; replaying the same code over a mutated log does not reproduce. Failure path: if the nightly job fails or a version is incomplete, serve the last good version and say so, rather than a partial page. A wide-row store fits date+scope keys, but global dates are hot keys — that's a partition-size and hotspot conversation, not a settled choice.

---

<a id="q3"></a>
## Q3 — Notification System (critical + promotional, one platform)
**Open with:** two traffic classes sharing one platform — **separate lanes**, **durable intent at accept**, and **at-least-once delivery with duplicate suppression**. Say the honest bound early: **external delivery cannot be exactly-once, so the goal is that duplicates are rare and never harmful.**

1. Delivery semantics → **At-least-once to the provider with best-effort suppression — a worker can succeed, crash before recording, and retry.** Do not claim user-visible exactly-once; providers offer no portable idempotency contract, and claiming one is the fastest way to lose the round.
2. Never call a provider inside the request → **Commit the notification and an outbox row in one transaction; a relay publishes at least once.** The outbox makes *intent* durable — it does not atomically enqueue into an external broker, and the relay itself can double-publish.
3. Caller retries → **Require a caller-supplied event id enforced by a uniqueness constraint at accept.** Without it a client timeout after commit creates a second notification before any dispatch-time dedup can help.
4. The unstated requirement → **Separate queues and reserved worker capacity per class, so an OTP never queues behind a 100K promo.** Then name what's still shared — database, broker, and especially the provider account and sender pool — because that's where isolation actually leaks.
5. Dedup identity → **Key on business identity — (recipient, event_id, channel) — with the TTL as policy on top: minutes for an OTP, about two days for a promo.** Type governs expiry; it is not the identity.
6. A provider degrades → **Circuit breaker per provider, backoff with jitter, per-provider concurrency caps, and bounded attempts into a dead-letter queue.** Failover only on failures known to precede acceptance — a timeout is ambiguous, and re-sending through a second provider is how one OTP becomes two.
7. Surge handling → **Admission control on campaigns, plus quotas, retention and explicit backpressure.** A queue absorbs a finite burst; if ingress stays above provider throughput, depth grows until something expires — say what you shed and in what order.
8. Bulk fan-out → **Chunked, checkpointed workflow so a crash resumes at the last chunk — and per-recipient dedup, because a crash after sending a chunk and before recording it re-sends that chunk.** Checkpointing bounds the damage; it doesn't prevent duplicates.
9. Preferences, quiet hours and expiry → **Evaluate at dispatch with a stated linearisation point, and record expired or suppressed as terminal states rather than dropping silently.** An opt-out landing mid-submission is a race you name, not one you pretend away.
10. Delivery status → **A state machine wider than the happy path — dispatching, provider_accepted, retryable, unknown, suppressed, expired, failed — fed by verified provider webhooks that arrive late, duplicated and out of order.** "Accepted by the provider" and "delivered" are different facts, and email "delivered" only means a mail server took it.

**Detail:** the 1M/sec headline is promotional peak, so it becomes queue depth rather than concurrent sends — provider throughput is the real ceiling and absorbing the surge is the job. Token buckets in Redis per provider and per tenant, with vendor limits that may be scoped per account, sender, region or destination, plus 429 handling. Channels share a dispatch interface but genuinely differ in consent, payload limits, ordering, sender identity and regional compliance — don't say they differ "only in adapter". OTP message expiry is not token expiry; the verifier enforces token validity independently. Unread counts are a separate read model. Point 6's per-provider breaker is Q4 — say so if both come up.

---

<a id="q4"></a>
## Q4 — Circuit Breaker (low-level design, thread-safe)
**Open with:** a state machine plus a rolling window with a concurrency contract — **the trip decision is evidence, the recovery attempt is a timer**, and **every admitted call carries the generation it was admitted under**.

1. Generation is the correctness core → **Stamp each admitted call with the breaker's generation; a result from an older generation updates statistics but can never change current state.** Without it, a call admitted while CLOSED can return after the breaker reopened and close it again — this is the bug most candidates miss.
2. State is more than the enum → **State, opened-at, half-open permits and window generation move together under one CAS on an immutable state object.** An atomic enum alone permits inconsistent combinations.
3. What trips it → **Error rate over a rolling window with a minimum-volume floor.** Say why rather than asserting dominance: consecutive-failure counting is cheap but pattern-sensitive; rate needs the floor so one-of-one can't trip it. Both need threshold, window length and open duration named.
4. OPEN → HALF_OPEN → **Time-based, claimed by exactly one thread via CAS — and once HALF_OPEN, callers that win a permit are admitted while only the losers fast-fail.** "Everyone else fast-fails" is wrong; someone has to probe.
5. HALF_OPEN admission → **A bounded permit count claimed atomically, with results attributed per generation.** Unbounded half-open sends a thundering herd at a recovering dependency.
6. Retry interaction → **Retries sit *below* the breaker and count as attempts, with a total deadline and jitter — and no retry while open.** Get the ordering wrong and one logical request becomes many dependency calls, so the breaker amplifies the overload it exists to stop.
7. What counts as a failure → **Semantic, per dependency — not HTTP status class.** 429 is backpressure, 401 is your credential, 404 may be normal; timeouts and connection failures count. Say the classification is configurable and name your defaults.
8. Fallback policy → **Decided per call type, not delegated wholesale to the caller: fail-open for a recommendation, fail-closed for authorization or a write.** A breaker that returns stale or permissive results on a safety path converts an outage into silent wrong answers.
9. Fleet behaviour → **The breaker is process-local, so thousands of instances keep probing — jitter the open duration, lengthen it on repeated failure, and pair with a bulkhead and a shared rate limit.** Local breakers plus synchronised timers produce a fleet-wide probe storm.
10. Closing again → **Several consecutive trial successes within one generation, and any failure returns to OPEN and restarts the timer.** With concurrent permits, define result ownership or "consecutive" is meaningless.

**Detail:** the window is a ring of short buckets holding success/failure/timeout counts, normalised on both read *and* write so a writer can't increment an expired bucket; rotation needs CAS ownership, not just a volatile read. The hot path should avoid a *contended* lock rather than all locking — a short uncontended lock can beat heavily contended atomics, and permit claiming is inherently atomic anyway. Slow-call rate is a legitimate second trip condition. Inject the clock so the machine is testable without sleeping. Granularity is per dependency, possibly per operation, traded off against reaching the minimum-volume floor. Transition telemetry is best-effort — an async event can drop, and a synchronous one violates the hot path. Interview shape: state object, window, then `allow()` / `recordSuccess()` / `recordFailure()`; fallback and metrics are follow-ups.

---

<a id="cross"></a>
## Repeating ideas across these four

- **Precompute, then look up — and say which requests bypass it.** Q1 interest buckets, Q2 date snapshots. Both have an on-demand path; own it rather than claiming it doesn't exist.
- **Pin what you paginate.** A cursor gives deterministic order; a snapshot version gives stability. Q1 and Q2 both need the version, not just the cursor.
- **Stable record, live visibility.** Q1 retractions and Q2 moderation are the same move — filter at serve time and overfetch — with the honest cost that historic pages change when content is removed. Immutable *audit* history, not immutable user data: deletion, PII erasure and legal takedown have to be able to reach the bytes.
- **Identity, then dedup.** Q1 (source, guid) and Q3 (recipient, event_id, channel). A content hash is an optimisation, not an identity, and dedup only counts at the durable side-effect boundary.
- **Separate lanes, then name what's still shared.** Q3 critical vs promotional, Q4 per-dependency breakers. The leak is always the resource you forgot: a provider account, a broker, a connection pool.
- **Evidence, not duration.** Q4's trip window and Q2's watermark both refuse to trust elapsed time — the same instinct as the auto-tiering readiness gate you present in the deep dive, which is why it reads as a principle rather than a memorised line.
- **State the condition, not the absolute.** Every "never" in the first draft of this file was a place Codex found a counterexample. Local top-K is exact *given* a decomposable score; a queue absorbs *a finite* burst; exactly-once is *committed*, never delivered.
