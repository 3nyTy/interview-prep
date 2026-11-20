# Mock Scorecard — Q9: POI / Yelp Nearest-K (geospatial)

**Date:** 2026-07-16 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/09-poi-yelp-nearest-k.md`](../detailed/09-poi-yelp-nearest-k.md)

**Verdict:** Architecture/consistency reflexes are now automatic (SoR + CDC + version-respecting projector + reconciliation came out cleanly; nailed OpenSearch-vs-cell-table with the right reason). The only real gap was **pure geo-domain knowledge** (fixed-grid + KNN termination) — memorizable, not a reasoning failure.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **It's filtered + ranked search, not pure KNN** — asked about filters (cuisine/rating) early; the real query is "open Italian 4★+ near me, ranked by distance+quality."
2. ✅ **OpenSearch as the serving store** (the biggest decision). *Justification after a nudge:* **filter selectivity** — a cell holds 3,000 places, ~5 match, so a `cell_id → list` store must **overfetch thousands or cap-and-miss**; a search engine does geo+filters+rank *together*. (I first gave the **boundary** argument — real, but something you *solve*, not the differentiator.) Also got **`cell_id` hot partition** in dense metros.
3. ✅ **OpenSearch is NOT the source of truth** — a durable DB (SQL/DDB) holds truth and powers the index. *(Corrected after re-pose.)*
4. ✅ **Sync via CDC** + **reconciliation job** for drift. *Sharpen:* projector must be **idempotent + respect external version** (out-of-order/replayed events can't resurrect stale data); reconcile by **checksumming ranges**, repair only diffs; "rebuild from stream" is **DR-only**, not routine.
5. ❌ **Geo-index: fixed grid level, not variable** — *fix (needed teaching):* variable-size cells **break neighbor math** (neighbors aren't uniform → dedupe/miss bugs) and need a separate density index. Use **fixed** level (S2 ~L13 / H3 ~r8) + **expand outward**; use official **coverer functions** (S2 `RegionCoverer`, H3 `gridDisk`) for antimeridian/poles/pentagons — never hand-roll.
6. ⚠️→❌ **KNN termination** — I had the **max-radius cap** ✅ but not the correctness rule (needed teaching): visit cells by **min-distance (best-first)**; **stop when K-th-best < nearest *unvisited* cell's min-distance** (bound over *all* unvisited cells, not one cell's edge).
7. ⚠️ **Two-stage ranking** — hard geo+attribute filters → few-hundred candidates → richer blended score (distance+rating+popularity+sponsored+personalization) on just those. Not drilled.
8. ⚠️ **"Open now" computed at query time** from structured opening intervals — *not* a stored `is_open` boolean (mass updates twice daily + tz/DST/holiday bugs). (I flagged I'd use the DB for open-now — say *computed*.)
9. ⚠️ **Move-cell race** — I chose **doc-per-POI**, which largely avoids the delete-old+insert-new cross-cell race; external versioning on the projector closes the ordering gap.
10. ⚠️ **Caching** — never cache the **personalized/ranked/ad-injected final** result; may cache **anonymous coarse candidate sets** (single-flight + soft-TTL); don't edge-cache the geo query (snapping to a nearby center corrupts "near me").
11. ⚠️ **100K QPS @ p99<100ms** — geo-routed shards, **split hot metros** across shards, replicas ∝ QPS, filter-bitset caches.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"It's filtered+ranked search, so OpenSearch (geo + filters + ranking together) beats a DynamoDB cell-table — selective filters mean a cell-table overfetches thousands or caps-and-misses."*
- *"OpenSearch is a derived serving index; a durable DB is the SoR, synced by CDC + a version-respecting projector + a reconciliation job that detects drift."*
- *"Fixed grid level + expand outward (variable cells break neighbor math); stop the KNN search when the K-th best is closer than the nearest unvisited cell."*

## Pattern to drill
Consistency/architecture reflexes are now **top-tier** (this problem shares the SoR+CDC+reconciliation spine with Calendar/POI). The one gap was **geo-domain facts** — memorize: *fixed grid + official coverer + best-first termination*. Also: on a "why not X" rebuttal, lead with the **decisive** reason (selectivity), not a solvable side issue (boundaries).
