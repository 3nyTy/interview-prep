# Design a News Aggregator System

- Source: https://www.hellointerview.com/community/questions/news-aggregator-feed/cm96lh25n0039ad08067audlg?company=Reddit
- Type: system design
- Reddit reports: 3 — Mid December 2025 (Staff), Early December 2025 (Staff), Late March 2025 (Manager)
- Also reported at: Rippling, Amazon, Microsoft, +6 more
- Captured: 2026-08-13 (detail page read)

The highest-priority design question of the four: it is the only one with Staff-level Reddit
reports, and it has two of them, both recent. Ten community solutions are posted.

## Prompt as published

> Design a news aggregation system like Google News that allows users to subscribe to
> different news sources, categories, and topics, then view a personalized real-time news
> feed based on their interests.

## Common functional requirements

- Users should be able to subscribe to news sources, categories, and keyword topics to
  personalise their feed.
- Users should be able to view a personalised feed that updates in near real time with new
  and relevant stories.
- Users should be able to manage preferences (mute/unfollow sources or topics, set
  language/region) and see those reflected quickly.
- Users should be able to receive optional breaking-news notifications aligned with their
  interests.

## Common deep dives

1. How will you ingest and normalise content from thousands of heterogeneous sources
   reliably (RSS/Atom, publisher APIs, web pages) while respecting rate limits and ensuring
   idempotency?
2. How can you serve a personalised feed with low latency (<200 ms p95) at scale?
3. How will you deduplicate and cluster near-duplicate stories from different sources in
   near real time?
4. How will you deliver near real-time updates and notifications without overwhelming your
   system (fanout, connection scale, mobile push)?

## Patterns and technologies named on the page

Patterns: real-time updates (push versus pull, connection management, fanout without
broadcast storms); scaling reads (precomputation, caching, pagination, indexes that avoid
N-way joins at request time); multi-step processes — the ingestion pipeline is
fetch → parse → normalise → dedupe/cluster → enrich → index → fanout, run as a durable
workflow with retries and idempotency.

Technologies: Kafka to decouple fetchers from processors with per-source partitioning for
ordering and rate isolation; Redis sorted sets for hot feed pages and per-interest queues;
Elasticsearch for full-text search, filters, relevance, and "more like this" as a
near-duplicate detection aid.

Similar problems they point at: FB News Feed, Web Crawler.

The page also notes the role-play rewards scoping ranking simply — rule-based — and spending
the time on correctness, scale, and timely updates instead.

## Red flags to avoid

- Skipping deduplication and idempotency in the ingestion pipeline — syndicated sources
  produce the same story repeatedly and feed quality degrades.
- Computing the personalised feed entirely on demand — joining fresh articles across all
  followed sources per request misses the latency target.
- Synchronous fanout to every follower on each new article — write amplification, hot
  partitions, cascading failure during spikes.

## Coverage in existing prep

No counterpart. None of the 13 debate-validated designs in memory covers feed construction,
ingestion pipelines, or near-duplicate clustering.

Real adjacency now that the technologies are known: `memory/project_poi_yelp_design.md` is
OpenSearch-primary, which is the same engine family as the Elasticsearch role here, so the
search-as-primary-read-path argument transfers to deep dive 2. `memory/project_webhook_delivery_design.md`
covers the durable-pipeline-with-retries-and-idempotency half of deep dive 1.

Genuinely missing: near-duplicate clustering (deep dive 3), fanout-on-write versus
fanout-on-read for feed assembly, and stable pagination while new items arrive. On that last
one the trap is offset pagination breaking under insertion — cursor on a stable sort key.

Together with `design_hot_posts_page.md`, one of the two real design gaps for Reddit, and
the more urgent of the two on both level and recency.

## Status

Prompt, requirements, deep dives, and red flags captured. Design not written.
