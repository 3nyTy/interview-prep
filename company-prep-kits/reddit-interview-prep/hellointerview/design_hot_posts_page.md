# Design a historic 'hot' posts page

- Source: https://www.hellointerview.com/community/questions/historic-hot-posts/cm7t9ars700023b6rgm7pusr2?company=Reddit
- Type: system design
- Reddit reports: 1 — Late February 2025, level Senior
- Also reported at: nowhere else — this is Reddit-only
- Captured: 2026-08-13 (detail page read)

Oldest of the nine reports by 10 months, and the only Reddit-specific question in the set.

## Prompt as published

> Design a system that displays the most popular Reddit posts from any given day in history,
> where users can query by date using 'YYYY-MM-DD' format to view that day's trending content.

Product framing from the page: Historic Hot lets users view the most popular Reddit posts
from a specific date, like YouTube Trending or Twitter Top Tweets but frozen in time, so you
can jump to any day in history and see what was hot then.

What it tests, per the page: translating a seemingly simple read-heavy feature into a system
that mixes offline computation (ranking, aggregation) with fast online reads and sensible
caching — deterministic ranking rules, batch/stream pipelines for daily snapshots,
low-latency query paths, backfills, late data, and moderation updates, at reasonable cost.

## Common functional requirements

- Users should be able to enter a date in 'YYYY-MM-DD' and see the top N posts for that day
  with stable, deterministic ordering.
- Users should be able to scope results globally or by subreddit (e.g. r/science) for the
  chosen date.
- Users should be able to paginate through results for that day without the order changing
  between requests.
- Users should be able to view basic metadata per post (title, subreddit, score, comment
  count, author, link/thumbnail) as of that day's snapshot.

## Common deep dives

1. How do you define and compute a deterministic 'hot' score for a historical day,
   including edge cases near day boundaries?
2. How do you aggregate signals and compute Top-K efficiently at scale (global and
   per-subreddit) without scanning all posts?
3. How do you deliver sub-200ms reads globally for popular dates while keeping costs under
   control?
4. How do you handle late-arriving data, moderation changes, and backfills without serving
   inconsistent results?

## Patterns and technologies named on the page

Patterns: scaling reads (pre-materialised Top-K lists, CDN edge caching, in-memory cache —
never recompute per request); managing long-running tasks (daily aggregation as a
checkpointed job with retries and an atomic publish step); multi-step processes (ingestion →
per-post aggregation → Top-K selection → snapshot versioning → publish/invalidate).

Technologies: Kafka for vote/comment event ingestion with windowed aggregation and replay
for backfills; Cassandra wide rows for per-day aggregates and Top-K partitioned by date and
subreddit; Redis for serving precomputed Top-K with versioned keys so invalidation after
reprocessing is straightforward.

Similar problems they point at: YouTube Top K, FB News Feed, Ad Click Aggregator.

## Red flags to avoid

- Computing rankings on demand per request — sorting millions of posts at query time blows
  the latency budget and prevents stable ordering.
- No explicit snapshot semantics — a canonical UTC window, deterministic score formula, and
  defined tie-breakers are required, or backfills cannot be reproduced.
- Ignoring late data and moderation updates — needs versioned snapshots, idempotent
  reprocessing, and cache invalidation.

## Coverage in existing prep

No counterpart among the 13 validated designs.

The central choice — retain ranked snapshots over time, or recompute rank as of a past
instant from a score history — is the same shape as two questions already in this folder.
`memory/project_reddit_billing_status_replay.md` records that the graded insight in Billing
Status Replay was storing before/after snapshots rather than deltas, and
`memory/project_reddit_modlist_hierarchy.md` uses log replay with timestamp as rank. Code in
`coding/BillingStatusReplay.java` and `coding/ModeratorListHierarchy.java`. The page's second
red flag lands on the same point from the other direction: snapshot semantics must be
explicit or nothing is reproducible.

Genuinely missing: the hot-ranking formula and its time decay, efficient Top-K without full
scans, and snapshot retention and granularity.

## Status

Prompt, requirements, deep dives, and red flags captured. Design not written.
