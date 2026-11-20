# Design POI / Yelp Nearest-K (Geospatial)

> OpenAI Staff SWE sample question: 500M POIs, 100K QPS read-heavy, p99 < 100ms, global. Validated via debate flow (Codex high-effort: v1 "fail as-is" → v2 fixes incorporated; optional Round-3 verify hung/unavailable, self-verified instead). AWS-native.
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#49f73fef-fa90-4618-b2da-c5fdc7e80de9

## The prompt is TWO questions
- **(A)** the geo-index / nearest-K deep-dive the interviewer explicitly wants (geohash / S2 / H3 / quadtree).
- **(B)** "design Yelp," whose REAL query is **filtered + blended-ranked** ("open Italian 4★+ near me"), not pure nearest-neighbor.
- **Answer B; use A to explain how the index works.**

## Key validated decisions (the debate-won must-fixes — v1 had all of these wrong)

### Serving primary = geo-capable SEARCH engine (OpenSearch), NOT a DynamoDB cell-table
- DDB = system-of-record **ONLY** (PK=`poi_id` + optimistic version).
- One **document per POI**: `geo_point` (BKD-tree) + `geo_distance` + inverted indexes (category/price/rating) + structured hours.
- Reason: filter selectivity — a cell holds thousands but few pass "open Italian 4★+"; a cell-list + in-memory-rerank either overfetches or returns incomplete.
- DDB PK=`cell_id` is **also** a hot-partition bug by design.

### Geo algo: FIXED S2 level (~13) / H3 res (~8) + official RegionCoverer/gridDisk covering + best-first KNN
- Adaptivity = search **radius / ring expansion**, NOT variable cell size.
- Variable-precision cells were the **#1 critical flaw**: they make the 3×3 neighbor model **undefined** (a fine cell borders coarse cells at multiple levels; geohash neighbor algebra assumes uniform length), need a separate density routing index, and cause dedupe/miss on precision-drop.
- Official coverer APIs handle antimeridian / poles / H3 pentagons — **never hand-roll neighbor algebra.**

### KNN termination
- Visit cells by min-distance from the query point; **STOP when `dist(K-th passing result) ≤ min dist to nearest unvisited cell`.**
- With selective filters this can scan forever → a **MAX-RADIUS CAP is mandatory** (return <K honestly).
- v1's "Kth dist > nearest cell EDGE" guard was underspecified/wrong (the bound is over the whole unvisited set, not the center cell's edge).

### POI move = single-document update with external versioning
- `version_type=external`; highest wins, stale rejected.
- This **deletes the cross-cell delete+insert race entirely** (the doc, not cell membership, is the unit).
- v1's delete-from-old-cell + insert-to-new-cell spanned TWO partition keys, and DynamoDB Streams only guarantee per-KEY ordering → lost-delete / resurrected-membership bug.

### CDC correctness
- Transactional **outbox** in the same DDB txn → Streams → Lambda projector applies external-version writes (idempotent) → DLQ + replay → **continuous reconciliation** (checksum SoT vs index by version/cell range, repair drift).
- "Rebuild from stream" is DR, not routine repair. Divergence detection is a must-have, not optional.

### Two-stage ranking
- Stage 1 = geo + hard filters → top-N.
- Stage 2 = blended rerank (distance + rating + popularity + sponsored + personalization) on the small N.
- Keeps expensive scoring off the full set.

### open-now is NOT a denormalized boolean
- (Mass updates, DST/holiday/timezone bugs.)
- Store structured opening intervals, evaluate at query time (per-tz precomputed bitset = the optimization).
- Temp-closure = first-class override.

### Caching honesty
- Edge/Redis cache only **anonymous coarse candidate sets** with single-flight + soft-TTL (stampede protection).
- **NEVER** personalized / ranked / ad final results.
- CloudFront centroid-snapping corrupts "near me" order.

### 100K QPS p99 < 100ms on OpenSearch is NOT automatic
- Size explicitly: geo-routed shards (split hot metros like NYC to avoid a hot shard), replicas ≈ QPS, filter bitset caches, index sorting, refresh ~1s, two-stage ranking.
- Honest escape hatch at extreme scale = a custom in-memory sharded geo-service.

### Global
- Per-region OpenSearch replicas fed by the same stream; region-pinned SoT writes; read-your-writes for business owners (route to home region); per-field freshness SLOs + lag dashboards.
- Footprint is multi-TB (× replicas × regions × overhead), not the naive 500GB.

### Quadtree / k-d tree
- = exact SINGLE-NODE KNN structure (best-first branch-and-bound, min-heap on dist-to-bbox) = the answer to "design a quadtree for nearest N."
- BKD-tree in the search engine is its productionized, shardable form.
- Don't propose a single mutable in-memory quadtree for 500M global POIs (strawman).
