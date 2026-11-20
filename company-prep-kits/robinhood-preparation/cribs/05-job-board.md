# Crib — Job Board System (Robinhood)

1 report, late May 2026, **Staff**. Lowest-value design in the set: no trading content, no
scheduling content, shares nothing with the rest of the loop. This crib is deliberately shorter —
that is triage, not an omission.

> Design a job board platform that allows employers to post job listings and job seekers to
> search, filter, and apply for positions, including features like resume uploads, application
> tracking, and job recommendations.

## 1. Summary (in plain terms)

⭐ **The prompt contains four separate systems, and that is the test.** Listings CRUD, faceted
search, resume upload + application tracking, and recommendations. Nobody designs four systems well
in 60 minutes.

**Ask which one to go deep on before drawing anything.** A candidate who gives all four equal
shallow treatment fails at Staff. If the interviewer declines to choose, pick **search** and say
why: it is the only one with a genuine scaling and relevance problem, and everything else is CRUD
plus a state machine.

## 2. Key deep dives (plain terms)

- **Search is the core, and OpenSearch is the primary store for it — not a bolt-on index.** Faceted
  filters (title, location, salary band, remote, seniority) with counts per facet, plus a relevance
  score. The recorded lesson from the POI/Yelp design applies directly: making the search engine
  authoritative for the searchable projection beats keeping a database primary and syncing an index
  you then have to reconcile. Postings still live in Postgres as the write model; the projection is
  derived.

- ⭐ **Stable pagination under insertion is the trap to name unprompted.** A seeker pages through
  results while employers post new jobs. `OFFSET 20` re-runs the query against a shifted result set
  and the seeker sees duplicates or misses rows. Use a **cursor on a stable, unique sort key**
  (`(score, posting_id)` or `(posted_at, posting_id)`), and snapshot the query at first page if
  relevance ordering must stay frozen for the session.

- **Commute-radius filtering** is fixed-level cell indexing (S2/H3) plus a distance re-rank — the
  same structure as POI nearest-K. Do not compute great-circle distance across every posting.

- **Resume upload never streams bytes through your API.** Presigned URL straight to object storage,
  then an asynchronous parse job to extract skills and text for indexing. That parse is the
  long-running-task pattern: queue, worker pool, lease, retry with backoff, dead letter. ⚠️ Also a
  security surface — uploaded files are untrusted input, so validate type and size, never trust the
  client-supplied filename or content type, and parse in a sandbox.

- **Applications are a state machine** with the same conditional-write discipline as everything else
  in this loop: `SUBMITTED → SCREENING → INTERVIEWING → OFFER → HIRED`, with `REJECTED` and
  `WITHDRAWN` terminal. Unique on `(seeker_id, posting_id)` so a double-click cannot double-apply.

- **Recommendations — state the choice, don't hand-wave.** Precompute per seeker on a schedule
  (cheap reads, stale, scales with seekers) **or** rank at query time from an embedding index
  (fresh, costlier per request). Say which and why. Then name **cold start**: a brand-new posting
  has no interaction data, so it needs content-based matching until it earns behavioural signal.

- **Listings need an approval state**, because spam and scam postings are the actual operational
  problem on every real job board: `DRAFT → PENDING_REVIEW → ACTIVE → EXPIRED/CLOSED`.

## 3. Schema (sketch)

```
postings            posting_id PK, employer_id, title, description, salary_min/max_minor,
                    location_cell, remote_flag, seniority, state, posted_at, expires_at
                    → projected into OpenSearch: analyzed text + facet fields + cell id

applications        (seeker_id, posting_id) UNIQUE, application_id PK,
                    state, resume_object_key, submitted_at, version
                    INDEX (posting_id, state)      -- employer pipeline view
                    INDEX (seeker_id, submitted_at) -- seeker's tracker

resumes             object_key PK (S3), seeker_id, parsed_at NULL, parse_state
                    → skills/text land back on the seeker document for matching

recommendations     (seeker_id, posting_id, score, computed_at)   -- if precomputed
```

## 4. Likely probes

**"Employer posts, seeker searches immediately."** → Index lag is real; the write model commits then
the projection updates. Sub-second is achievable; promise read-your-writes only for the employer's
own view of their posting, which you can serve from Postgres.

**"Millions of postings, one popular query."** → Cache the hot query's first page. Facet counts are
the expensive part — approximate them, or cap the counted set.

**"Rank relevance."** → Text match plus recency plus completeness, tuned; then behavioural signal
(apply-through rate). Do not claim an ML ranker you cannot explain.

## Traps

- ⛔ Don't design all four subsystems. Ask, then go deep on one.
- ⛔ Don't use `OFFSET` pagination.
- ⛔ Don't stream file uploads through the API.
- ⛔ Don't trust uploaded file metadata.
- ⛔ Don't claim exact facet counts at scale without saying they're approximate.

## Reuse

`memory/project_poi_yelp_design.md` (OpenSearch-primary, fixed-level cells, faceted filter),
`~/reddit-interview-prep/hellointerview/design_news_aggregator.md` (stable pagination under
insertion), `cribs/01-job-scheduler.md` (the resume-parse worker pool is the same long-running-task
pattern).
