# Cheat Sheet — POI / Yelp Nearest-K (Geospatial)

🔗 Diagram: https://excalidraw-production.a.musta.ch#49f73fef-fa90-4618-b2da-c5fdc7e80de9

## 1. Summary (in plain terms)
"Find places near me" for 500M places, very fast, worldwide. But the *real* Yelp query isn't just "nearest" — it's "**open Italian restaurants, 4★+, near me**." So it's nearest-K **plus filtering plus ranking**, not pure geometry.

> The prompt is two questions: (A) the geo-index deep dive the interviewer wants to hear (geohash/S2/H3/quadtree), and (B) actually-design-Yelp. **Answer B, use A to explain the index.**

## 2. Key deep dives (plain terms)

- **Use a search engine (OpenSearch) as the main serving store, NOT a DynamoDB "cell table."** Why: the filters are selective — a map cell holds thousands of places but only a few are "open Italian 4★+." A search engine with a geo-index + inverted indexes does geo + filters + ranking together. A DynamoDB table keyed by cell either over-fetches or returns incomplete results, *and* keying on `cell_id` is a hot-partition bug. DynamoDB stays as the system-of-record only.

- **Use a FIXED grid level (S2 ~13 / H3 ~8), not variable-size cells.** Make the search adaptive by **expanding the radius / rings**, not by changing cell sizes. Variable cell sizes break the whole "look at the 3×3 neighboring cells" model (neighbors stop being well-defined when sizes differ) — this was the #1 flaw. And use the **official library's covering functions** (RegionCoverer / gridDisk); never hand-write neighbor math (it gets the antimeridian, poles, and H3 pentagons wrong).

- **When do you stop searching outward (KNN termination)?** Visit cells nearest-first; stop when your current K-th best result is closer than the nearest cell you haven't looked at yet. With strict filters this could search forever, so a **maximum radius cap is mandatory** (then honestly return fewer than K).

- **Moving a place = update ONE document** (with "external versioning": higher version wins, stale updates rejected). This kills the nasty race of "delete from old cell, insert into new cell" — which spanned two keys and could resurrect or lose a place. The document, not cell-membership, is the unit.

- **Keeping the search index in sync:** transactional outbox in the same DB write → stream → a projector updates the index idempotently → plus **continuous reconciliation** (checksum the source-of-truth vs the index and repair drift). Divergence detection is a must-have.

- **Two-stage ranking:** stage 1 = geo + hard filters → a few hundred candidates; stage 2 = the expensive blended score (distance + rating + popularity + sponsored + personalization) on just those. Never score the whole set.

- **"Open now" is computed, not a stored boolean.** A stored flag means mass updates and time-zone/DST/holiday bugs. Store opening hours as intervals and evaluate at query time (a precomputed per-timezone bitset is the optimization).

- **Caching honesty:** you may cache *anonymous, coarse* candidate sets; never cache the personalized/ranked/ad final result. CloudFront "snap to a center point" corrupts "near me" ordering.

## 3. Document / data model (OpenSearch primary + DynamoDB SoR)
```
OpenSearch — ONE document per POI (the serving store):
  poi_id,
  location: geo_point,             # BKD-tree; geo_distance queries
  category, price, rating,         # inverted indexes for filtering
  hours: [structured open intervals],   # evaluate "open now" at query time
  popularity, version
  → shards geo-routed; split hot metros (e.g. NYC) to avoid a hot shard

DynamoDB — system of record ONLY:
  PK = poi_id
  attrs: ..., version              # optimistic / external version; highest wins
  + transactional OUTBOX row (same txn) → stream → projector → OpenSearch

Reconciler: checksum SoR vs index by version/cell range → repair drift
```
Note: a quadtree/k-d tree is the *single-machine* exact-KNN structure (the answer to "design a quadtree for nearest N"); the search engine's BKD-tree is its shardable production form. Don't propose one in-memory quadtree for 500M global places.
