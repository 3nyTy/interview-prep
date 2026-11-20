# Design a Job Board System

- Source: https://www.hellointerview.com/community/questions/job-board-system/cmq52v6650lci08adf9wofnpx?company=Robinhood
- Type: system design
- Robinhood reports: 1 — last asked Late May, 2026
- Level reported: **Staff**
- Also asked at: nowhere else — **Robinhood-only**
- Captured: 2026-08-14, detail page read

The outlier of the five system designs. No trading content and no scheduling content, so it
shares nothing with the rest of the bank. One report, and the lowest-value design here.

The detail page is bare: prompt, a one-row timeline, no editorial breakdown, no community
solutions, and no candidate note. Everything below the prompt is mine.

## Prompt as published

> Design a job board platform that allows employers to post job listings and job seekers to
> search, filter, and apply for positions, including features like resume uploads, application
> tracking, and job recommendations.

## What the prompt contains

Four separate subsystems, which is unusual for one prompt:

1. **Listings** — employers create and manage postings. Plain CRUD with an approval state.
2. **Search and filter** — the real system design content. Faceted search over title,
   location, salary, and remote status.
3. **Resume upload and application tracking** — blob storage plus an application state machine.
4. **Recommendations** — matching seekers to postings.

At Staff level, expect the interviewer to pick one and go deep. Ask which one before you draw
anything. A candidate who designs all four shallowly fails.

## Prep notes (mine — the page carries no guidance)

**Search is the core.** This is the search-and-filter shape already validated in
`memory/project_poi_yelp_design.md`, where the recorded decision was OpenSearch as the primary
store rather than a database with a search index bolted on. Faceted filters, pagination that
stays stable while new postings arrive, and a relevance score are the same problems. Geo
filtering by commute radius reuses the same fixed-level cell indexing.

**Applications are a state machine**, and the same conditional-write discipline as every other
question in this folder: `SUBMITTED → SCREENING → INTERVIEWING → OFFER → HIRED`, with
`REJECTED` and `WITHDRAWN` terminal.

**Resume upload** is presigned URLs to object storage, never bytes through the API. Then an
asynchronous parse to extract skills, which is a queue and worker pool — the long-running-task
pattern from `design_job_scheduler.md`.

**Recommendations** are the one part with a real choice to state: precompute per seeker on a
schedule, or rank at query time from an embedding index. At Staff level, say which and why, and
name the cold-start problem for a new posting with no interaction data.

**Stable pagination under insertion** is the trap worth naming without being asked. New postings
arrive while a seeker pages through results. Cursor pagination on a stable sort key, not
`OFFSET`, or the seeker sees duplicates. The same trap is recorded in
`~/reddit-interview-prep/hellointerview/design_news_aggregator.md`.

## Status

Detail page captured in full; it holds nothing beyond the prompt. No design written.

Lowest priority of the five designs. One report, no overlap with the rest of the loop, and no
Robinhood domain content.
