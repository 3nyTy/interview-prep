# POI / Yelp Nearest-K (Geospatial) — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#49f73fef-fa90-4618-b2da-c5fdc7e80de9

## What we're building
"Find places near me" for **500M points of interest**, **100K queries/sec**, **p99 < 100ms**, worldwide. But here's the catch the interviewer is testing: the *real* Yelp query is not "nearest restaurants" — it's **"open Italian restaurants, 4★ and up, near me, sorted by a blend of distance and quality."** So it's nearest-K **plus filtering plus ranking**, not pure geometry.

## Recognize it's really two questions
- **(A)** A geo-index deep dive the interviewer explicitly wants you to discuss: geohash / S2 / H3 / quadtree — how do you index points on a sphere so "nearby" is fast?
- **(B)** "Design Yelp" — whose actual workload is filtered + ranked search.

**Answer (B), and use (A) to explain how the index underneath works.** If you only answer (A), you've solved a textbook KNN problem and missed the product.

## The single biggest decision: use a search engine, not a DynamoDB cell-table
A natural first instinct: divide the world into grid cells, store `cell_id → list of places` in DynamoDB, and to answer a query, fetch the nearby cells and rank in memory. **This is wrong here, and knowing why is the differentiator:**

- The filters are **selective**. A city cell might hold thousands of places, but only a few are "open Italian 4★+." If you fetch the cell and filter in memory, you either *overfetch* massively (pull thousands to keep five) or, if you cap how much you pull, you *miss* valid results that were just past your cutoff.
- Keying DynamoDB on `cell_id` is **a hot-partition bug by design** — a dense downtown cell becomes one scorching-hot key.

Instead, use a **geo-capable search engine (OpenSearch)** as the primary serving store. Store **one document per place**, containing:
- a `geo_point` (indexed with a BKD-tree for fast geo-distance queries),
- inverted indexes on category / price / rating (so filters are fast and combine cleanly),
- structured opening hours.

Now "open Italian 4★+ within 2km, ranked" is a single query the engine is built to do: geo filter + attribute filters + scoring, all together. **DynamoDB stays as the system-of-record only** (the durable truth: `PK = poi_id` + a version number); the search index is what serves reads.

## The geo-index deep dive (part A, done right)
Use a **fixed grid level** — S2 level ~13, or H3 resolution ~8 — and make the search adaptive by **expanding the radius / rings outward**, NOT by varying cell sizes.

### Why fixed, not variable-precision?
Variable-size cells (fine cells in dense areas, coarse in sparse) sound clever but were **the #1 critical flaw** in the first version:
- The whole "look at the 3×3 neighboring cells" model becomes **undefined** when a fine cell borders coarse cells — neighbors aren't the same size, and geohash/S2 neighbor math assumes uniform cell length.
- You'd need a *separate* index just to know how dense each area is (to pick the precision).
- You get dedupe/miss bugs when precision changes.

So: fixed level + use the **official library covering functions** (S2 `RegionCoverer`, H3 `gridDisk`) to compute which cells a query circle touches. These libraries correctly handle the nasty edge cases — the antimeridian (180° longitude), the poles, and H3's 12 pentagon cells. **Never hand-roll neighbor algebra** — you'll get those edges wrong.

### When do you stop searching outward? (KNN termination)
Visit cells in order of their minimum distance from the query point. **Stop when your current K-th best result is closer than the nearest cell you haven't looked at yet** — because nothing in an unvisited, farther cell could beat it. (This is best-first / branch-and-bound.)

But with selective filters, you might never collect K results (e.g. "open Italian 4★+" in a small town) — so a **maximum radius cap is mandatory**, and you honestly return fewer than K rather than scanning the planet. (The first version's termination guard — "K-th distance > nearest cell *edge*" — was subtly wrong; the bound is over *all* unvisited cells, not one cell's edge.)

## Moving a place without a race (a subtle but important point)
When a place's location changes, it may move from one grid cell to another. The naive approach — "delete it from the old cell, insert into the new cell" — touches **two different partition keys**, and DynamoDB Streams only guarantee ordering *per key*. So the delete and insert can be processed out of order → a place gets resurrected in its old cell, or lost entirely.

**Fix:** model a move as a **single-document update with external versioning** (`version_type=external`: the highest version wins, stale updates are rejected). Because the *document* (not cell-membership) is the unit of change, there's only one key to update — the cross-cell race disappears entirely.

## Keeping the search index in sync with the source of truth
- Writes go to DynamoDB with a **transactional outbox** in the same transaction → **DynamoDB Streams → a Lambda projector** applies the change to OpenSearch (idempotently, respecting external version) → failures go to a **DLQ with replay**.
- Plus **continuous reconciliation**: periodically checksum the source-of-truth against the index (by version / cell range) and repair any drift. "Rebuild the index from the stream" is a disaster-recovery action, not routine repair — divergence detection is a must-have, not optional.

## Two-stage ranking (keep expensive work small)
- **Stage 1:** geo + hard filters (open? Italian? 4★+? within radius?) → narrow to a few hundred candidates.
- **Stage 2:** a richer blended score (distance + rating + popularity + sponsored placement + personalization) computed on just those few hundred.
You never run the expensive scoring over the whole dataset.

## "Open now" is computed, not stored
Tempting to store a boolean `is_open`. Don't — it means mass updates twice a day and creates time-zone / daylight-saving / holiday bugs. Instead store **structured opening intervals** and evaluate "open now" at query time. The optimization is a **precomputed per-timezone bitset** of open-ness by time slot. Temporary closures are a first-class override.

## Caching honesty
- You *may* cache **anonymous, coarse candidate sets** (with single-flight + soft-TTL to prevent stampedes when a cache entry expires).
- You must **never** cache the personalized / ranked / ad-injected *final* result — it's user-specific.
- And CloudFront-style "snap the user to a nearby center point" corrupts "near me" ordering — don't edge-cache the geo query.

## Hitting 100K QPS at p99 < 100ms is not automatic
You have to size it deliberately: **geo-routed shards** (and split a hot metro like NYC across shards so it isn't one hot shard), replicas roughly proportional to QPS, filter bitset caches, index sorting, ~1s refresh interval, and the two-stage ranking above. At truly extreme scale, the honest escape hatch is a custom in-memory sharded geo-service.

## Document / data model
```
OpenSearch — ONE document per POI (the serving store):
  poi_id
  location: geo_point                 # BKD-tree; powers geo_distance queries
  category, price, rating             # inverted indexes → fast, composable filters
  hours: [structured open intervals]  # evaluate "open now" at query time
  popularity, version
  → shards geo-routed; split hot metros to avoid a single hot shard

DynamoDB — system of record ONLY:
  PK = poi_id
  attrs: ..., version                 # external versioning; highest wins, stale rejected
  + transactional OUTBOX row (same txn) → Streams → projector → OpenSearch

Reconciler: checksum source-of-truth vs index by version/cell range → repair drift
```

## A note on quadtrees (so you can speak to part A confidently)
A **quadtree / k-d tree** is the exact *single-machine* nearest-neighbor structure — best-first branch-and-bound with a min-heap on distance-to-bounding-box. That's the right answer to "design a quadtree for nearest N points." The search engine's **BKD-tree** is essentially the productionized, shardable version of the same idea. Don't propose one giant in-memory quadtree for 500M global places — that's a strawman that doesn't shard or persist.

## If the interviewer pushes further
- *"Why not just the cell-table in DynamoDB?"* Selective filters: a cell holds thousands, few match, so you overfetch or miss. And `cell_id` as a partition key is a hot-partition bug. A search engine does geo+filter+rank together.
- *"Why fixed cell size?"* Variable precision breaks neighbor math and forces a density index; fixed level + official coverer functions handle the edges correctly.
- *"How do you not lose a place when it moves cells?"* Single-document update with external versioning — the document is the unit, so there's no two-key delete+insert race.
