# Interview Crib — Plain-English Cheat Sheet

**How to use, live:** name a design → you get the whole thing in ONE message. Read **Open with** (your first 30 seconds), then work down the **10 problems**. The **bold phrase** is what you say out loud.

**Hard rules for this file:** **max 10 numbered points per design** — major problems only, not caveats. **Each point is one line.** Everything else lives in **Detail**, which you do *not* read live — it's there for when they dig.

**Ordering:** sorted by **cost of getting it wrong**. The **first three are the round-deciders.**

**Kept current:** every problem surfaced in a mock gets folded in. Capped to 10 points/design 2026-08-06.

**Jump to:** [1 RAG](#q1) · [2 Chat UI](#q2) · [3 CI/CD](#q3) · [4 Slack](#q4) · [5 Calendar](#q5) · [6 URL](#q6) · [7 Payments](#q7) · [8 Webhooks](#q8) · [9 Yelp](#q9) · [10 Crossword](#q10) · [11 Cloud IDE](#q11) · [12 Sora](#q12) · [13 Chess](#q13) · [Repeating ideas](#cross)

---

<a id="q1"></a>
## Q1 — Company AI Search (RAG)
**Open with:** permissions are the whole problem — **authorize before you load any text**, **stamp every derived copy with a permission-version**, **never reuse one user's answer for another**.

1. Don't let the AI read a doc the user can't see → **Search returns IDs only → check permissions → then load text of just the allowed ones.** Filter at the end and the secret text already went through the model and your logs.
2. Access revoked or a doc changed → **Stamp every derived copy with a doc-version + permission-version; on mismatch, discard and redo.** Bumping the permission number invalidates everything at once.
3. Caching answers → **Per-user, short TTL, keyed by permission-version. Never across users** — same question, different access.
4. Follow-up turns in one conversation → **Re-check permissions on the referenced docs EVERY turn** — access can be revoked mid-conversation, and the summary is itself a copy of those docs.
5. Many companies, one system → **A tenant filter on every search, enforced where code can't skip it.** Big customers get a dedicated index; small ones share.
6. Retrieval quality → **Run two searches — meaning-based and keyword — and merge.** Keyword catches error codes, IDs and names that embeddings blur.
7. Right docs, wrong answer → **Cite per claim, and say "I don't know" when retrieval is weak.** A confident ungrounded answer is the worst outcome.
8. Poisoned documents → **Wikis and tickets are user-writable, so retrieved text is DATA, never instructions.**
9. Getting docs in, and changing your mind later → **Push or scheduled pull; chunk + embed in the background — and re-embedding 100M docs is a live migration** (build alongside, dual-read, cut over).
10. Latency and failure → **First words in ~2s with slow checks async; and if the permission service is down, fail closed and refuse to answer.**

**Detail:** a departure is *cheap* (bump the permission-version, no reprocessing) — a **content edit** is the expensive one. Real deletion must reach index, caches, chat history, logs and the vendor's retention, plus a who-saw-what audit trail and EU-stays-in-EU. Prove retrieval improved with a hand-labelled **golden question set** — click data only exists later. Push the expensive LLM re-rank and faithfulness checks off the synchronous path.

<a id="q2"></a>
## Q2 — ChatGPT Web App (streaming chat screen)
**Open with:** it's a browser problem, not a distributed-systems one — **`fetch()` not `EventSource`**, **parse frames not chunks**, **token in a header in memory**, **backend stores nothing**.

1. Stream the reply word by word → **`fetch()` with a stream reader, not the browser's `EventSource`.**
2. Parse the stream correctly → **Decode bytes with streaming on, then split on blank lines into whole messages.** Never JSON-parse a raw network chunk.
3. Don't lose the last few words → **Flush the buffered text BEFORE marking it done** — on normal end *and* on error.
4. Keep it smooth → **Put the in-progress reply in its own state slice so only that bubble redraws.** Don't virtualize the live message.
5. Model output safety → **Render markdown as safe elements, never raw HTML** — injected HTML would steal your in-memory token.
6. Auth on an expensive streaming call → **Token in a header, in memory, not a cookie.** The cookie-based refresh endpoint is the CSRF target.
7. Retry → **A brand-new billable generation: drop the half-finished reply from screen AND history, then resend.**
8. Where it runs → **S3+CloudFront; backend on Lambda response-streaming or Fargate — NOT classic API Gateway** (buffers, 29s timeout).
9. Stop button and cost → **Cancelling must abort upstream so you stop paying**; rate-limit per user; the API key never reaches the browser.
10. Scrolling → **Stick to the bottom only if the user is already at the bottom.**

**Detail:** `EventSource` fails three ways — GET-only (can't POST the messages body), can't set an auth header, and auto-reconnects (re-billing a non-resumable generation). Highlight code only *after* the closing fence. Validate the token at admission only — a mid-stream expiry isn't recoverable, so refresh proactively before sending.

<a id="q3"></a>
## Q3 — CI/CD (build → test → deploy runner)
**Open with:** you can't prevent a double-run, so make it harmless — **an attempt number matched exactly** protects your database, **a fixed action ID** protects production, and the promise is **"recorded once, run at-least-once, safe to repeat."**

1. A worker dies mid-step — did it finish? → **An attempt number that only goes up; the DB accepts a write only on an EXACT match.** Never fence on the worker's ID — pooled containers get reused.
2. Don't deploy or charge twice → **Give the real action a fixed ID from run+step (NOT the attempt) so downstream ignores the repeat.**
3. Be honest about "once" → **"Recorded once, run at-least-once, safe to repeat" — never "runs exactly once."**
4. Is the worker alive? → **A short lease (~5 min) it keeps renewing; a watcher reclaims it, bumps the number, reassigns.** Lease = who *tries*; number = whose *result counts*.
5. Move to the next step → **A change event triggers it, plus a periodic sweeper for anything stuck** — events drop and a stalled run is silent.
6. Retry the right failures → **Their test failing = "successful execution, failed run": report it, STOP the run, don't page.** Infra failure → retry, capped.
7. Cancel → **"Cancelling/cancelled" are real states, and "not cancelled" is part of every claim AND completion write.** Killing is best-effort only.
8. Admission is not scheduling → **One gate decides what's allowed in; a separate global scheduler decides what runs now**, over per-tenant queues.
9. Fairness → **A fair share that flexes (use idle capacity, back off the instant another team appears), and cap machine-SLOTS in flight, not runs.**
10. Untrusted code → **A Firecracker microVM per step, short-lived per-run credentials, no outbound network by default.**

**Detail:** downstream must *actually* dedupe your action ID — declarative deploys (`apply`) are idempotent, "scale up by 2" and "send email" are not. One global FIFO undoes tenant fairness; a new tenant needs **headroom + autoscale + an honest ETA**, and you preempt only cheap-to-restart work. Spot: use the **2-minute warning** to cordon, drain, mark infra-failed and reassign in seconds rather than waiting out the lease; arbitrary shell steps **can't** checkpoint. **Pin the config** (pipeline + commit + image digest) at run start, or a retry isn't a retry. Artifacts: **each attempt writes its own path, the DB marks the official one, readers resolve through the DB.** Live log tailing is its own subsystem. Cancel the step's VM, never via autoscaling.

<a id="q4"></a>
## Q4 — Slack (team chat)
**Open with:** one write becomes millions of ordered deliveries — **send to servers, not people**; **one number per channel handed out by the database**; **save-and-publish in a single write**; **unread is subtraction against a read pointer**.

1. One post → thousands of people → **Send only to the servers holding members of that channel** (250 sends, not 50,000); best, servers **subscribe** per channel so you never read the member list.
2. Everyone sees the same order → **One number per channel from an atomic increment on the channel's own row — the DB is the single writer, so servers stay stateless.** Never timestamps.
3. Never lose a posted message → **Message + a "needs sending" row in ONE write, and a relay publishes from it** *(transactional outbox)*.
4. But durable ≠ fast → **publish to the fan-out tier right after the commit and keep the outbox as the backstop**; change-capture is too slow to be the live path.
5. Unread without a storm → **One "last message I READ" number per person per channel; unread = latest − that.** Not "last *received*" — a connected device receives everything, so unread would always be zero.
6. Client missed a message → **Every message carries its number (sees 5 then 7 → asks for 6) plus a quiet "latest is N" heartbeat** for a dropped last message.
7. Don't track "who received it" → **per-recipient delivery rows are millions of writes/sec.** Push best-effort; the client detects gaps and pulls.
8. Scale the data → **Split by channel.** The one giant channel is mostly reads → cache/replicas; its write rate is human-limited.
9. Presence (green dots) → **Soft state only: in-memory with ~30s expiry refreshed by heartbeat, subscribe only to who's on screen, batch updates.** Never durable.
10. A server dies and 40,000 clients reconnect → **Random backoff, lazy resume (counts only, fetch a channel when opened), real headroom, `Retry-After`.**

**Detail:** put the increment + message + outbox row in **one transaction**, or a crash burns a number and leaves a permanent hole — which breaks both unread-by-subtraction and gap detection. Real loss isn't a dropped TCP frame (you'd lose the connection) — it's a **full send buffer** or a **failed publish to one server**. **Mention counts must be exact** → track separately. On deploys, drain slowly and **jitter max connection age** or you manufacture your own storm. Multi-device: each device syncs on its own cursor, "too far behind → resync." Also expect threads (own order + own unread), edits/deletes/reactions over an append-only log, search indexing, compliance, files on a separate path.

<a id="q5"></a>
## Q5 — Google Calendar
**Open with:** two hard parts — **"what's on this week" has to be one range read**, and **every device needs a numbered change log it can resume from**. Everything else is date/timezone modelling.

1. "What's on this week?" fast + correct → **File each event under every MONTH it touches — one entry per month — so a view is one range read.** "Look back 30 days" silently drops long events.
2. A 3-year event → **anything spanning >~12 buckets goes on a small per-calendar "long events" list that every view also reads.** One row instead of 120.
3. Repeating events → **Store the *rule*, never copies; expand only for the window being viewed.** Overrides keyed by **(series, original occurrence date)**; "this and all following" ends the old rule and starts a new series.
4. Time zones → **All-day = a plain date, no zone. Timed = UTC + the IANA zone ID** (never the offset). **Expand recurrence in LOCAL time** — +168h lands at the wrong hour across DST.
5. Device sync → **Number every change per calendar; the client presents its number and you send what's after it — and it advances only after a page is applied.**
6. Sync completeness → **deletes need tombstones, each row carries a snapshot, and a token older than retention gets "gone, resync."**
7. Real-time → **A tiny "your calendar changed" nudge (phone push), then the device pulls. Coalesce** — 10 edits in 5s is one nudge.
8. Reminders → **A SHARDED due-time index** (`due_at <= now` on one index is a hot partition at 250B events), and **an infinite series is one row that reschedules itself.**
9. Invitations → **Fan out on write** (reads are 15:1): a slim pointer row into each attendee's buckets, done **async**, body stays single-copy, decline writes a **tombstone**.
10. Two devices edit one event → **version check-and-increment**, plus **client-generated IDs for creates** (a retried create makes two events) and field-level merge for offline replay.

**Detail:** bucket key = the **local civil month in the calendar's zone** (an all-day event has no UTC to bucket by); the entry holds a **slim render-only copy**, written in the same transaction as the event. Per-*day* entries make a 3-month event 90 rows and a year view 53 reads. Pre-filter recurrence by each series' span so you don't expand 200 rules per view; optional polish is a **rolling ~18-month materialisation horizon** so views have one code path. The change log **is** the outbox — no second "to_sync" table. Reminders: dedupe on (event, occurrence, offset), and drop stale ones after an outage (20 min late is worse than never); most meetings start on the hour, so pre-compute and spread the :00 pile-up. **Find-a-time** = project each attendee to opaque busy intervals, merge, intersect gaps; **room double-booking can't be a uniqueness constraint** — use a range **exclusion constraint** or serialise per room. Multi-region: put a **failover epoch in the sync token** so a counter reset reads as "resync."

<a id="q6"></a>
## Q6 — URL Shortener
**Open with the arithmetic, out loud:** *1B links/yr ≈ **32 writes/sec**; 100B redirects/yr ≈ **3,200 reads/sec**; 5B × 500B ≈ **3TB**.* → **one database node + a cache.** The two traps are **over-building** and **301**.

1. Don't over-build → **One normal database + a cache. Don't shard, and don't distribute the write path for 32 writes/sec.** State the numbers first.
2. Redirect type → **302, NOT 301.** A 301 is browser-cached ~forever → you lose click counts, expiry, **and the ability to kill a bad link**.
3. Custom alias, unique worldwide (**the #1 deep dive**) → **Last-write-wins can NEVER enforce uniqueness; you need exactly one arbiter per alias.** At this rate, one global write region + read replicas.
4. Don't auto-fail-over that arbiter → **unreachable ≠ dead, and two arbiters = two owners.** Fail the claim, or promote with a fenced epoch.
5. Make the short code → **Random 7 characters; the unique constraint catches the rare clash → retry.** The same constraint decides aliases → taken = 409.
6. Read path → **Cache first; on a miss read the PRIMARY, not a replica** (lag → a false 404 you'd then cache); **single-flight** the miss; evict **LFU/LRU, never FIFO**.
7. Clicks are the real write load → **3,200/sec, ~100× the creates. Emit async off the redirect path with a bounded buffer that drops** — analytics down, redirects still work.
8. The dashboard → **reads rollup counters (minute/hour/day), never raw events**; raw to object storage; uniques via **HyperLogLog**.
9. Abuse (it's a phishing laundering machine) → **Check at create AND at serve time** — the destination changes after approval, and **a takedown must invalidate the cache.**
10. Expiry → **`expires_at` on the row is truth, checked at read; the cache TTL is not.** Expired → **410**, not 404.

**Detail:** negative-cache only *after* an authoritative check (~5s), and remember 3TB won't fit in memory — cache the hot set. Block **loopback/link-local/private** targets, allow-list `http(s)`, flag **punycode/mixed-script lookalikes**. Global reads = **read local, write home**, and a local miss must fall through to the authoritative copy *before* returning 404. Create idempotency = key on `(account, request-hash)`; the same long URL twice gets a **new** code by default (campaign analytics). Normalise aliases and enforce uniqueness on the normalised form; reserve `api`, `login`, `admin`, `static`. Per-account quotas are the real abuse defence. Cleanup = chunked deletes or drop monthly partitions; never set the cache TTL to the link's TTL.

<a id="q7"></a>
## Q7 — Payments (card processor)
**Open with:** *"We're the middleman forwarding charges to Visa/Amex — not a wallet."* Then the lifecycle, then the crux: **every external money-move must be written down before you send it, and idempotent on a stable token.**

1. Get the type right first → **Middleman to the card networks; we don't hold balances, we never store raw card numbers (vault tokens), and money writes pin to ONE region.**
2. The steps → **Authorize (hold) → capture (enter clearing) → settle (money actually moves, once a day).** Money moves at **settle**, not capture.
3. Never call the network before your DB knows → **Commit `INITIATED` + the idempotency key FIRST.** A crash in between leaves a live hold nothing can find or release.
4. The network times out — approved or not? (*the defining failure*) → **Never blind-retry (two holds): query by your own reference, else void-then-re-auth, else park it `UNKNOWN`.**
5. And never lie about it → **"declined" on a timeout makes the merchant re-charge the customer.** Return a distinct pending outcome.
6. Idempotency key → **unique index, and on retry return the ORIGINAL stored response.** Same key + different amount = **conflict**, not success.
7. "Everything settles tonight" → **Write the settlement to-do row in the SAME transaction as the capture and read it strongly consistent** — an eventually-consistent index misses a 9:58pm capture.
8. Don't pay a merchant twice → **The batch token must be DETERMINISTIC (`processor#bizdate#shard`) — no UUID, no entry count** — and on retry resend the identical token or ask the network.
9. Crash recovery → **Lease the in-progress rows and make the reclaim predicate INCLUDE the in-progress state**; bound retries, then a `STUCK` state that pages a human.
10. Money flows backwards too → **Refund vs void, chargebacks arriving weeks later, and a merchant balance that can go NEGATIVE** → reserves, rolling holds, blocked payouts.

**Detail:** a *local* secondary index can't serve the worklist either — it only re-sorts within one partition key; change-capture is fine for dashboards, never a money worklist. Persist a **manifest hash** before sending the batch. Set the client timeout **longer than the network's p99** or you manufacture unknowns. **Holds expire (~7 days)** → a sweeper over a sparse index of un-captured holds, not a database TTL (lags days). **Reconciliation is the real backstop:** a minimal double-entry ledger + a daily diff against the network's own settlement report. Partial refunds = **one conditional write on a running `refunded_amount`**; refunds can fail (closed card) → exception queue. **Payouts are a separate fenced leg** (`merchant#bizdate`), net = captured − refunds − fees − chargebacks − reserve. Dashboard reads ledger rollups. 200M+ captures/day → shard the worklist; authorize is latency-critical, settlement throughput-critical; handle partial/multiple captures and late captures.

<a id="q8"></a>
## Q8 — Webhooks (notify customers, 1 billion/day)
**Open with:** 1.3B deliveries/day **into servers we don't control and can't trust** → the crux is a **durable time-bucketed retry scheduler** plus an **honest at-least-once promise made harmless by a stable ID**.

1. Retry for 24 hours → **Rows keyed `due_minute#shard`; a poller leases a bucket-shard and drains it.** **The queue's own delay timer caps near 100 seconds** at this scale.
2. Two details decide whether it works → **assign the shard RANDOMLY at write time (never by tenant)**, and **sweep every bucket ≤ now**, not just the current minute.
3. First attempt must not wait for a sweep → **delivery row + schedule row (due now) in ONE transaction, enqueue on the fast path, poller is the backstop.**
4. Can't promise exactly-once over the internet → **a lost acknowledgement is indistinguishable from a failure. Promise at-least-once.**
5. Make the duplicate harmless → **`delivery_id = hash(event_instance_id, subscription_id)`, sent as a header, identical across attempts and re-fan-outs.** **Never derive it from the payload.**
6. "We want events in order" → **ordering and independent retry are in direct tension.** Default: don't promise it, send a per-subscription sequence number.
7. A customer is down for hours → **per-endpoint in-flight cap + jittered backlog-aware backoff (default ON) + a circuit breaker that half-opens with exactly ONE probe.**
8. Security is two problems → **SSRF: validate at CONNECT time, PIN the resolved IP, ZERO redirects.** And **sign the POST: HMAC over the raw body + timestamp.**
9. Fan-out → **one independent row per subscriber**, and a **deterministic `delivery_id` makes a half-finished fan-out safe to replay.**
10. After 24 hours → **dead-letter it, show a failed-deliveries view, and give them a replay API** — a customer who was down needs to fetch what they missed.

**Detail:** the poller is also **admission control** — don't re-enqueue faster than the fleet drains. Three IDs, not one: `event_type` (routing), `event_instance_id` (producer-supplied, *your* ingestion dedupe), `delivery_id` (customer dedupe). Two byte-identical events can be legitimately distinct (same customer charged $5 twice) — that's why content hashing is wrong. If you *must* promise order: **ordered lanes on `subscription+entity`, one in-flight per lane, a failure blocks the lane** (one poison event stalls it 24h) → add a stall budget, dead-letter, mark the stream **gapped**; lanes cap parallelism at lane count. Verify **endpoint ownership at registration** or you're a DDoS weapon; rotate signing secrets with two signatures. Quarantine + notify after prolonged failure. Attempt history (~1.3B/day, bigger than delivery volume) streams to object storage, never the operational DB. A **slow** customer needs a concurrency cap, not a rate limit. Shard celebrity event types.

<a id="q9"></a>
## Q9 — Yelp / "places near me"
**Open with:** this is **filtered + ranked search that happens to have a distance filter** — not textbook nearest-neighbour. So: **a geo search engine as the serving store, a durable DB as the source of truth, fixed-level grid, best-first termination.**

1. It's filtered search, not just "nearest" → **Use a geo search engine doing location + filters + ranking together — not a grid-of-cells in a key-value store.**
2. Why — **selectivity** → **a downtown cell holds ~3,000 places and ~5 match, so a cell-table must over-fetch thousands or cap-and-miss.** Also `cell_id` as a partition key is a hot partition by design.
3. The index is a derived copy → **a durable DB is the source of truth; outbox in the same transaction → change-capture → an idempotent, version-respecting projector → plus continuous reconciliation.**
4. **Two mechanisms — don't muddle them: cell IDs are for ROUTING and cache keys; the engine's BKD-tree does the geometry.**
5. If you index it yourself → **ONE fixed grid level, expand outward in rings, and use the library covering functions** (variable-size cells break neighbour math).
6. When to stop searching outward → **stop when your K-th best is closer than the nearest unchecked cell** — and **a max radius is mandatory** since filters may never yield K.
7. A place that moves → **one document per place; a move is a single-document update with external versioning.** Modelling cell *membership* means two keys, and streams only order per key → resurrected or lost.
8. "Open now" → **model the week as 336 half-hour slots indexed in the place's OWN timezone → one cached term filter.** Never a stored `is_open` flag.
9. Ranking → **two stages: non-scoring filters narrow to a few hundred, then the expensive blend (with distance *decay*) runs only on those** — and stage 2 lives in the app, not the index.
10. 100K QPS at p99 <100ms → **route queries by a coarse cell so you hit 1–2 shards, not 40** — the win is **tail latency** — and split hot metros deliberately.

**Detail:**
- **Routing:** default `hash(_id) % shards` hits every shard. P(hitting a slow shard) = **1 − 0.99⁴⁰ ≈ 33% across 40 vs ~2% across 2** — that's why you can't fan out. **Over-covering costs one extra shard visit; under-covering silently drops results → when in doubt, over-cover.** Non-geo queries can't route → separate path.
- **Cell assignment is a pure function** — `.parent(level)` is bit truncation, zero shared state. Sub-shard with `hash(poi_id) % k`, **never `random(k)`**.
- **Hot cells: two kinds, two fixes** — data-hot → more **sub-shards** (needs reindexing); query-hot → more **replicas** (elastic). *Sub-shard for volume, replicate for traffic.* Registry is kilobytes → in memory, refreshed, never per-query; fall back to last-known-good. Changing k rehashes the cell → over-provision or use **virtual buckets**. Roll out **read-side first**. Day-one shortcut: a curated list of ~200 metros.
- **Search is two round trips** — shards return IDs+scores, bodies are fetched from winners only. So `from`/`size` deep paging is catastrophic → **search-after cursor**. Don't return full `_source`; best is `poi_id` only + hydrate from a KV cache. **Over-fetch 200–500** because stage 2 drops candidates. Use a **point-in-time** across phases.
- **Why stage 2 can't be in the index:** per-user signals, a versioned model, and **mutable ad budgets** — plus dedupe/chain-collapse is a **set-level** decision. **The ranker degrades, the search never fails.** Latency budget: retrieval 25–40ms / features 10–20ms / model 5–10ms.
- **Caching:** anonymous coarse candidates only, never the personalised ad-injected result; **don't edge-cache the geo query** (snapping to a POP corrupts "near me").
- **"Rebuild from the stream" is DR, not repair** — 500M docs takes hours. Region-per-index beats routing-in-one-index globally (per-region replicas, residency). Vague addresses **geocode to the city centroid** — validate. This design assumes **STATIC** places.

**Facts to have ready:**
- **S2 level = quadtree depth.** 6 cube faces at level 0, ×4 per level → `6 × 4ⁿ`, max 30. **L6 ≈ 145km (routing) · L8 ≈ 36km · L13 ≈ 1.1km (index) · L16 ≈ 140m.**
- **Why cell IDs work as keys:** a 64-bit int whose cell = a **contiguous range of leaf IDs**, so containment is `BETWEEN` and **any B-tree indexes it**. S2 orders along a **Hilbert** curve; geohash uses Z-order (jumps more).
- **Why space-filling curves exist:** **a composite B-tree on `(lat, lon)` can't answer a bounding box** — after ranging on lat, lon prunes nothing. Cells flatten 2D→1D. The alternative is native 2D: **BKD / R-tree / GiST**.
- **BKD:** quantised ints packed into immutable leaves of ~512–1024 points, internal nodes hold **bounding boxes** → **wholly inside = accept all with no distance checks; outside = prune; crossing = recurse.** Cost ∝ points near you, not index size.
- **Quadtree vs BKD:** 4-way split at the geometric midpoint (→ the prefix property that *is* a cell ID) vs 2-way at the data median (balanced, but **no compact ID, can't be a routing key**). Same best-first search.
- **Geohash vs S2 vs H3:** rectangles/Z-order/**huge latitude distortion** · quads/Hilbert/**~2×, exact nesting** · **hexagons/6 equidistant neighbours** but **12 pentagons, no exact nesting**.

<a id="q10"></a>
## Q10 — Crossword Solver (across many machines)
**Open with:** **each step is cheap (a bitset AND + popcount, microseconds, all in RAM) — the tree is astronomically WIDE, so I parallelise breadth.** Then: **correctness can't live in worker memory, or a dead worker silently skips a subtree and my "no solution" proof is unsound.**

1. Why many machines? → **Each guess is cheap; the tree is astronomically WIDE → spread the width.** Not because a step is slow, not because it doesn't fit in memory.
2. Correctness can't live in worker memory → **a durable, fenced to-do ledger; a worker claims a chunk with a lease; expiry returns it.**
3. Why that matters → **an exact partition over in-memory stacks dies with the first lost message: a subtree is never explored and you'd still report "no solution."** Silently unsound, no failing metric.
4. What a chunk IS → **`{deterministic path, seed, budget B}`; on hitting the budget, split the remaining frontier into child units and stop.** A death costs one bounded unit; **writes happen only on splits**.
5. Is duplicate work bad? → **No — explored twice, never zero. Wasteful, never wrong.** Completeness depends only on **coverage**.
6. Prove there's NO solution → **distributed termination detection, NOT a `created == completed` counter** — declare UNSAT only when all units are exhausted, none leased, and no split in flight.
7. What each worker runs → **MRV ordering + forward checking + a length-local letter-position bitset index (<100MB) + no word reuse.** Name and reject the naive ~4GB global index.
8. Fast AND provable → **two lanes: PROOF-SWEEP (systematic, owns correctness, never starved) + SAT-RACE (randomised restarts, capped, incomplete).** First valid fill wins via atomic claim + cancel broadcast.
9. Don't trust the worker that claims a win → **the coordinator verifies the fill independently** (real words, interlocks agree, no reuse) before publishing.
10. Chunk size → **too large → stragglers; too small → ledger writes dominate.** Bounded budgets *are* the straggler fix, plus adaptive budgets and work stealing.

**Detail:** be honest that a **loose grid may solve in milliseconds on one box** — distribution earns its keep on dense boards and the UNSAT-proof requirement. The general form: *don't claim exact-once partitioning; durable ledger + at-least-once + deterministic replay.* What must be deterministic is **the split** (children provably cover the parent's frontier); local search order may vary. **Endgame trick your at-least-once property unlocks:** when a few units remain, run them **redundantly** (MapReduce backup tasks). **Coordinator HA:** active/standby on a lease, bumping a `COORD_EPOCH` carried on every write — a revived old coordinator is worse than a dead one. **Never say "optimal"** — correctness is a hard constraint; the target is minimise p50 time-to-first-solution, bound p99 proof time. Accelerators to mention but not lean on: a **worker-local** transposition table, and nogood learning (**an unsound nogood destroys the proof**). **Spot instances are ideal** — a reclaimed node costs one unit.

<a id="q11"></a>
## Q11 — Cloud IDE (VS Code in the browser)
**Open with:** the crux is **exactly one owner per workspace — and because a disk won't reject a stale writer, that means kill-before-route, not just a token.** Then: **durability lives at the editor-change layer, not the disk**, and **the lifecycle is a compare-and-swap state machine**.

1. Never two servers writing one workspace → **one owner via a lease with a monotonic epoch in a strongly-consistent store** (the DB is the authority, Redis is a cache).
2. Handover = **kill-before-route** → **bump the epoch → stop routing to A → kill A and CONFIRM the volume detached → attach B → only now is B routable.** Single-attach is *not* enough.
3. The conceptual point → **fencing only works where the resource CHECKS the token.** A DB checks it, a disk doesn't → the token fences *routing*, the detach fences the *filesystem*.
4. Don't lose unsaved work → **persist at the EDITOR-CHANGE layer, not the filesystem** — the volume gives durable *blocks*, not *application state*, and Git only has committed work.
5. Mechanism → **periodically persist each dirty buffer's current TEXT** (idempotent, no replay); a record exists ⇔ there's unsaved content, so a save deletes it.
6. Scope it → **the change log owns editor-authored unsaved content; the volume + snapshots own everything else** (terminal, `git checkout`, builds bypass it).
7. Sleep/wake without a mess → **a CAS state machine (`RUNNING→DRAINING→QUIESCED→DETACHING→HIBERNATED→ATTACHING→STARTING`)** so hibernate and resume can't both win.
8. Deciding a workspace is idle → **no keystrokes ≠ idle** (a 40-minute test run, a dev server, a paused debugger) → require *all* signals, a grace period, and a "keep alive"/pin.
9. Untrusted code is hostile multi-tenancy → **a microVM per workspace, egress blocked at multiple layers (incl. IPv6 metadata and DNS tunnelling), cgroup caps incl. process count and inodes.**
10. The gateway is a security boundary → **authorise *this user → this workspace* AND validate the epoch on EVERY connection** — a stale routing entry is cross-tenant access.

**Detail:**
- **Storage layers.** *Delta* layers hold unsaved content and are deleted on save; *full-state* layers hold the filesystem.

| # | Layer | Write trigger | RPO | Survives |
|---|---|---|---|---|
| 1 | **pod RAM** | every edit op | 0 | nothing |
| 2 | **WAL on the volume** | coalesced ~250–500ms + `fsync` | **~0.5s** | process crash, pod death, node loss |
| 3 | **file on the volume** | save · **autosave ~1s** · hibernation drain · *terminal/git write here directly* | save cadence | pod & node death |
| 4 | **remote row** | coalesced **15–30s** if changed · **forced on graceful shutdown** | **15–30s** | **volume loss, AZ loss** |
| 5 | **snapshot** | hibernation (quiesced) · ~daily · before risky ops | hours | volume corruption, AZ loss |

- **Planned termination must force a flush** (SIGTERM on deploy/scale-down/spot) or you eat the full RPO **on every rollout**.
- **Ordering:** buffer→WAL is fire-and-forget; **on save: write file → `fsync` → THEN delete the WAL entry and remote row**; recovery takes `max(doc_version)` and loads it into the **buffer as unsaved** — never writes the file automatically.
- **The WAL is ~free** because the volume is flat-rate (3,000 baseline IOPS; a 500ms fsync is ~2) while a KV store is metered per write. **Not instance-store NVMe** — that belongs to the machine, the WAL must belong to the workspace.
- **The WAL shares the volume's failure domain** → the tiers are deliberately orthogonal. **You may not need the remote row at all** if an AZ-loss RPO isn't required.
- **The op-log alternative** to snapshots: position ops are neither idempotent nor commutative, so it only pays off with multiplayer (CRDT ops *are* both).
- **Autosave (~1s) is the cheapest durability there is** — but it's a client preference, so a platform can't promise an RPO it doesn't control.
- **Other real problems:** AZ-pinned volumes (warm pools *per AZ*; cross-AZ recovery is a degraded snapshot restore, lazily hydrated); **cold-start milestones** (container → editor → files → terminal → language-intelligence; the warm pool only saves boot); **per-channel flow control** on the multiplexed socket (a noisy build starves your saves and Ctrl-C); **mass reconnects** (jitter, budgets, lifecycle dedupe, shard the control plane — EC2/EBS APIs throttle); **editor/filesystem divergence** (`git checkout` under an open buffer → an authoritative document service + a file watcher); **disk full** (quotas on bytes *and* inodes); secrets from a short-lived broker; **extensions are an attack path**; snapshot cost dominates at 10M workspaces.

<a id="q12"></a>
## Q12 — Sora (video-generation scheduler)
**Open with:** **more demand than GPUs is the real problem — that's admission control, not a queue.** Then: **the scheduler assigns, the worker never grabs**, and **the database promotes the official artifact, with billing fired only off the DB's done-event.**

1. Way more demand than GPUs (**deep dive #1**) → **real admission control: accept, quote a wait, say "try later," offer lower quality, or drop low-priority.** Queueing everything forever is an outage in disguise.
2. Don't give one GPU job to two workers → **the scheduler assigns it (the worker doesn't grab it) and locks the specific GPU slot with a lease + number.**
3. Two workers both finished → **each writes to its OWN path; the database promotes ONE as official** — storage won't reject a stale writer, so the pointer lives where a token *is* checked.
4. Bill and notify → **only from the database's "done" event, never from a worker saying "I'm done"** — and make billing ignore duplicates.
5. Resume after a crash (**deep dive #2**) → **checkpoint only when it pays (long job, likely interrupted); for a short job just re-run.**
6. Refuse to resume → **if the model version changed**, the checkpoint is no longer valid input.
7. Fairness → **a slot on one GPU type isn't equal to another — don't pool them as one.**
8. And → **let small jobs through, but don't let big jobs starve.**
9. Keep three concepts distinct → **admission (what's allowed in), scheduling (what runs now), preemption (what gets taken away).** Conflating them is the classic slip.
10. Completion must be **two-phase and idempotent** → a crash between "artifact written" and "job marked done" must not double-bill or lose the video.

**Detail:** "<30s of lost work" only holds if the **committed-checkpoint cadence including upload time** is *enforced*, not just a target. Stale workers must write to **attempt-scoped immutable keys**; readers resolve through the DB pointer (`current_attempt_id`), never "latest object." Capacity exhaustion and preemption/resume are the two make-or-break deep dives — expect both.

<a id="q13"></a>
## Q13 — Chess.com (real-time game)
**Open with:** **one writer per game**, **two DIFFERENT numbers** (a version for move order, an owner-epoch for ownership), **the clock is a stored deadline evaluated on the database's clock**, and **a timeout is just another versioned command**.

1. Same moves for everyone, survives a crash → **one writer per game; each move is `UPDATE … WHERE version = expected AND owner = me`.**
2. Two numbers, don't merge them → **version goes up per move (that's the order); a separate owner-epoch goes up on ownership change (that blocks a zombie).**
3. The fencing predicate must be **exact equality** → **`owner_epoch = mine`, never `>=`** — `>=` lets a stale owner through and defeats the whole point.
4. The clock → **store the deadline, not a running timer; "time left" = deadline − now, and the DATABASE's clock decides "now."**
5. Move lands just before the flag vs the clock hitting zero → **treat a timeout as the SAME kind of versioned command**; the timer only *triggers* a check, it never decides.
6. Why that's safe → **a move needs `now < deadline`, a timeout needs `now ≥ deadline`; against one clock only one can be true, and the version check picks the winner.**
7. Sending moves to players and watchers → **don't use fire-and-forget pub/sub (a dropped message is silent) — a durable per-game log + a quiet "latest move is N" heartbeat.**
8. Reconnect mid-game → **the client resumes from its last version off that log**, so a dropped connection never loses a move.
9. Cheating → **the server validates every move against the real rules; it never trusts the client.**
10. Slow connection is unfair → **give a little clock credit back based on measured lag**, or a laggy player flags unfairly.

**Detail:** one transaction does fencing + ordering + idempotency (version CAS + insert the move event + insert a **transactional outbox** row), and you broadcast **only** the committed row — if the update affected zero rows, reload and resync. Kafka/pub-sub delivery still needs the outbox + a relay keyed `(game_id, version, event_type)`; "off the critical path" doesn't excuse losing committed events. The idempotency key must store the **request hash *and* the response**, so the same key with a different payload is a **conflict**, not a false success. **Double-match prevention** = a `game_participants` table with `UNIQUE(player) WHERE active`, not an index on the games row. Say database capacity is **"credible pending benchmark," never "proven."**

---

<a id="cross"></a>
## Repeating ideas (these show up again and again)
- **Two things must happen together but can't be one action.** Either a stale worker writing (fix: **a number the database checks *exactly*** → old number rejected), or "save then send" (fix: **save both in one write**, a helper sends). Say "**transactional outbox**" for the second.
- **You can't promise "exactly once" through the outside world** → promise **at-least-once + a stable ID** so duplicates are safely ignored.
- **The "number check" only works if the thing being written actually checks it.** A database checks it. A **file store or disk does NOT** — so there you must **physically remove the old writer** (kill it, detach the disk) or **let the database promote the official copy**.
- **Syncing to devices** → number the changes, each device remembers its number, send what's after it, and nudge with "latest is N."
- **Match the tool to the size** → small problem = one database + a cache; don't reach for giant distributed systems by reflex.
- **Be honest** → "strong isolation," not "impossible to leak"; give a time *range*, not a fake exact ETA; say what "best" even means.
- **Anything that scales with (events × recipients) is a write storm.** Per-user state should be written on a *human action*, never on every machine event.
- **"Who's allowed in" and "what runs now" are different questions.** Fairness lives in the second one, and it needs spare headroom for whoever shows up next.
- **A durable pipeline promises *eventual* delivery, not *fast* delivery.** With a latency target, pair it with a direct fast path and keep the durable one as the backstop.
- **Cost-check your own machinery.** A lease costs a write per item; a receipt costs a write per recipient. If duplicates are already harmless, you don't need the lock.
