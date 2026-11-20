> ## ⛔ SUPERSEDED DRAFT — DO NOT STUDY THIS AS THE ANSWER
>
> This is the **Round-0 proposal**, preserved only as the input Codex attacked. It contains errors that
> were found and fixed. The authoritative version is `../reddit-design-crib.md` (Q3).
>
> **Known wrong in this file:**
> - `lb:{game}:alltime:{shard}` — in Redis Cluster the braces **are** the hash tag, so `{game}` pins every
>   shard to **one slot**. The entire sharding scheme is a no-op. Fix: `lb:{game:3}:alltime`.
> - Claims the approximate-rank histogram's error is bounded by **bucket width**. It is bounded by **bucket
>   population** — a bucket holding 40M players can mis-rank by ~40M.
> - Estimates 100M members at "1–2 GB". Real usage is **tens of GB** (dict + skiplist + member strings +
>   allocator overhead), before replicas.
> - Rolling-7-day `ZUNIONSTORE` precompute is not viable at this scale, the `rolling7` key is unsharded, and
>   cross-slot union does not work once sharding is fixed.
> - Says `ZMSCORE` can batch friend lookups. It operates on **one key**, so it cannot cross shards.
> - Describes ElastiCache as able to do multi-master. Global Datastore is async primary/secondary, not
>   active-active — and cumulative sums *can* be merged with per-region counters, so the claim is wrong twice.

# Proposal: Game Leaderboard System

Interview context: Reddit SWE L6 (Staff) system-design round, 60 min. Real prompt as reported:
"Design a real-time global game leaderboard that supports score updates and top-K queries for millions
of players. The interviewer starts from a single-server skeleton (10 reads / 1 write per second, no
friends) and incrementally piles on scale, friend leaderboards, time windows, and replication."

Constraint: AWS-native. The round is explicitly **incremental** — the opening answer must be small and
each escalation must change exactly one thing. Over-engineering step 1 is a scored failure here.

## Position

### Step 0 — the skeleton they actually asked for (10 reads / 1 write per sec)

One Postgres table `scores(player_id PK, score, updated_at)` with an index on `score DESC`.
Top-K = `ORDER BY score DESC LIMIT K`. Rank = `COUNT(*) WHERE score > mine`.
This genuinely handles 10 rps and I would say so rather than reaching for Redis immediately. Naming the
breaking point is the point: `COUNT(*)` is O(n) per rank query, so rank breaks long before top-K does.

### Step 1 — Redis Sorted Set as the serving layer

- `ZADD lb:{game}:alltime <score> <player_id>` — O(log n)
- Top-K: `ZREVRANGE ... 0 K-1 WITHSCORES` — O(log n + K)
- A player's rank: `ZREVRANK` — O(log n)

100M players ≈ 1–2 GB in one ZSET, which fits a single node. So the *memory* is fine; the problem is
throughput and the fact that it is one hot key.

**Redis is not the source of truth.** Score events land in a durable log first
(Kinesis Data Streams → DynamoDB `score_events`), and the ZSET is a **materialized view rebuildable by
replay**. This is the question behind "what happens when Redis dies," and a design that treats Redis as
primary fails it.

**Idempotency:** use `ZADD GT` (update only if greater) for high-score semantics. Retries and replays
then become naturally idempotent — no dedupe table needed. For last-score (not high-score) semantics,
that breaks, and you need `submission_id` dedupe; I would ask which one the game wants, because it
changes the answer.

### Step 2 — scale the write path

Single-node Redis tops out around 100–200K ops/sec, and one ZSET is one hot key regardless of cluster
size because a key lives on one shard.

**Shard by `hash(player_id) % N` into N ZSETs.**

- **Top-K stays exact.** A member of the global top-K must be in its own shard's top-K, so query top-K
  from all N shards and merge N*K candidates. This is the correctness argument the interviewer wants;
  it is not an approximation.
- **Exact arbitrary rank does not survive sharding.** `ZREVRANK` is per-shard. Exact global rank means
  counting every player above a score across all shards, which is O(N) queries per rank lookup and gets
  worse as you shard harder.

**Decision: exact top-N, approximate rank below it.**

Maintain a coarse **score histogram** (Redis hash: score bucket → count, incremented on update).
Rank ≈ sum of counts in buckets above yours + interpolation within your bucket. Error is bounded by
bucket width, which you choose. Product-wise, nobody needs "you are exactly #4,271,993" — they need an
exact top 100 and a credible "top 3%". I would commit to this and name the cost rather than pretend
sharding preserves exact rank.

### Step 3 — friend leaderboards

**Fan-out on read. Do not precompute per-user friend boards.**

- Fetch the user's friend ids (from the social graph service), then one pipelined `ZSCORE`/`ZMSCORE`
  batch, then sort in the application. F friends → O(F) Redis work in one round trip plus O(F log F)
  sort. F is typically ≤ 1000, so this is single-digit milliseconds.
- The alternative — a ZSET per user containing their friends' scores — has catastrophic **write
  amplification**: one score update must write into every friend's board, so a player with 5000 friends
  turns 1 write into 5000. For a bidirectional friend graph, total write cost scales with average degree.
  That is the trap in this part of the question.
- Cap it: a user following 100K accounts gets a paginated/top-slice view, not a full sort.

### Step 4 — time windows

- Separate ZSET per window: `lb:{game}:daily:{yyyymmdd}`, `lb:{game}:weekly:{iso_week}`, `alltime`.
- Set a Redis **TTL** on daily/weekly keys so expiry is automatic rather than a cleanup job.
- A write updates all live windows — 3 `ZADD`s instead of 1. Acceptable and explicit.
- **Rolling 7-day** is the interesting case: `ZUNIONSTORE` over 7 dailies is O(total members) and far too
  expensive per query. Precompute it on a schedule (every 1–5 min) into `lb:{game}:rolling7`, and serve
  reads from the precomputed key. Freshness cost stated up front.
- Note the semantic difference: daily/weekly windows use `ZADD` with **summed** or **max** score
  depending on whether the game scores cumulative points or a personal best. These are different
  aggregations and I would confirm which before writing the command.

### Step 5 — replication and multi-region

- Redis primary + read replicas (ElastiCache, Multi-AZ). Reads served from replicas; replica lag is
  milliseconds and **stale leaderboard reads are acceptable** — this is the one place staleness is
  genuinely free.
- Writes go to the primary only. Failover promotes a replica; because the ZSET is a rebuildable view,
  worst case is replay from the durable log rather than data loss.
- Cross-region: a leaderboard is inherently **global**, so single write region + cross-region read
  replicas. Multi-master would need conflict resolution on scores, and with `ZADD GT` semantics the merge
  is actually commutative (max wins) — so multi-master *is* possible for high-score boards but not for
  cumulative-sum boards. Worth naming, but I would still pick single-write-region for operational
  simplicity.

### Anti-cheat and trust

Scores must be **server-authoritative**. Never accept a client-reported score for a leaderboard that has
value. Validate plausibility (max achievable delta per unit time) at ingest, and keep the raw event log
so a cheating wave can be reverted by rebuilding the view with the offending submissions filtered — which
is another payoff of Redis-as-view.

### Data model

```
DynamoDB score_events (durable source of truth, append-only)
  PK = PLAYER#<player_id>   SK = <ts>#<submission_id>
  attrs: game_id, score, delta, client_meta, validated

DynamoDB player_best (for rebuild without full history scan)
  PK = PLAYER#<player_id>   SK = GAME#<game_id>#<window>
  attrs: best_score, updated_at

Redis
  lb:{game}:alltime:{shard}      ZSET   member=player_id score=score
  lb:{game}:daily:{date}:{shard} ZSET   TTL 8 days
  lb:{game}:rolling7             ZSET   precomputed every 1-5 min
  lb:{game}:hist                 HASH   score_bucket -> count   (approximate rank)
```

## Explicit trade-offs I am accepting

1. Rank outside the top N is approximate, with error bounded by histogram bucket width. Top-K is exact.
2. Rolling-window boards are up to 5 minutes stale.
3. Reads from replicas can be milliseconds stale.
4. Sharding multiplies top-K query cost by N (N*K candidates merged); N is chosen small (8–32).
5. Friend boards are capped for extreme-degree users rather than fully general.
