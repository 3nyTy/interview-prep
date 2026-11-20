# OpenAI Staff SWE — interview prep

Everything for the OpenAI Backend / Staff SWE loop.

## Start here

| | |
|---|---|
| **[98-onsite-loop.md](98-onsite-loop.md)** | **The four rounds, what each is graded on, and the values + operating principles.** Read this first. |
| [official/](official/) | The two documents OpenAI sent, verbatim: the Backend SWE onsite prep doc and the values / operating-principles doc. Everything in `98-onsite-loop.md` is distilled from these. |
| [deep-dive/](deep-dive/) | **The 45-min Technical Deep Dive: the deck, speaker notes, slide previews.** Project = Amazon AWS Directory Service dynamic auto-tiering. See [deep-dive/README.md](deep-dive/README.md) for the delivery plan. |
| **[bq/](bq/)** | **The Hiring Manager / behavioural round.** Why OpenAI · AGI & safety · the *"decide whether to release a model"* Staff question · story bank · why-you're-leaving · the quiet-HM playbook. Start at [bq/00-INDEX.md](bq/00-INDEX.md); read [bq/08-crib-onepager.md](bq/08-crib-onepager.md) 10 min before. |
| [interview-crib.md](interview-crib.md) | The in-round quick reference for system design. Read *during* the interview, not before. |
| [Coding Questions/](Coding%20Questions/) | 17 question + solution pairs. ⚠️ Solutions are Java; OpenAI's coding round prefers **modern Python**. |
| [mock-scorecards/](mock-scorecards/) | Per-design mock debriefs — what was identified unprompted vs. missed. |

## System design — the 13 designs

Reconstructed from validated prep notes. Every design below was stress-tested via the cross-LLM debate skill (Claude × Codex) before being saved. Each file has its own Excalidraw diagram link. Three tiers, file numbers aligned across all three so you can jump between them for the same design: [`cheatsheets/`](cheatsheets/) (fastest glance) → [`detailed/`](detailed/) (plain-English explainer + "if they push further") → this folder (full write-ups with debate scorecards).

> **Provenance note:** these docs are reconstructed from condensed, validated memory notes — not the verbatim original long-form write-ups or the full Codex debate transcripts (those were ephemeral and are gone). The technical substance is preserved exactly; nothing new was invented.

| # | Design | Headline decision |
|---|--------|-------------------|
| 01 | [ChatGPT Enterprise RAG](01-chatgpt-enterprise-rag.md) | Authorize-before-hydrate; the provenance "spine" |
| 02 | [ChatGPT Clone (frontend)](02-chatgpt-clone-frontend.md) | Streaming chat UI + stateless BFF; `fetch()`+ReadableStream SSE |
| 03 | [CI/CD Scheduler](03-cicd-scheduler.md) | Exactly-once *committed* (not execution); fenced CAS on DynamoDB |
| 04 | [Slack Enterprise Chat](04-slack-enterprise-chat.md) | Two-tier fan-out; per-channel seq + epoch fencing |
| 05 | [Google Calendar](05-google-calendar.md) | Month-bucketed occurrence index; high-watermark sync |
| 06 | [URL Shortener](06-url-shortener.md) | Right-size to Aurora+Redis; random code + UNIQUE retry |
| 07 | [Payment Processor](07-payment-processor.md) | Hold→charge→10pm batch; sparse SettlementWork table; LSI-is-a-trap |
| 08 | [Webhook Delivery (1B/day)](08-webhook-delivery.md) | DynamoDB retry scheduler (not visibility timeout); 3-ID identity |
| 09 | [POI / Yelp Nearest-K](09-poi-yelp-nearest-k.md) | OpenSearch-primary; fixed-level S2/H3 + best-first KNN |
| 10 | [Crossword Solver](10-crossword-solver.md) | Parallelize tree WIDTH; fenced idempotent work-unit ledger |
| 11 | [Cloud IDE](11-cloud-ide.md) | Fencing lease on the EBS volume (kill-before-route); continuous durability ≠ snapshots |
| 12 | [Sora Video Scheduling](12-sora-video-scheduling.md) | Exactly-one *side effect* via outbox (not just artifact); GPU-slot fencing |
| 13 | [Chess.com (real-time game)](13-chess.md) | Per-game sequencer + OCC; clock-as-data w/ `clock_timestamp()`; durable ordered fanout (not bare pub/sub) |

## Cross-cutting prep context
See [99-interview-context.md](99-interview-context.md) for the interview-style notes and the Chess.com / Sora deep-dive lessons that recur across designs (fencing semantics, attempt-scoped S3 artifacts, single-writer sequencer, etc.).

## Recurring themes across the prep
- **Right-size, don't service-reach.** Codex repeatedly rewarded matching the store to the access pattern (Aurora for the URL shortener, DynamoDB for Calendar) over reflexively reaching for DynamoDB+Global Tables everywhere.
- **Never claim "exactly-once execution" or "optimal" without a metric.** Claim exactly-once *committed* + at-least-once execution; define the objective.
- **Fencing protects the resource that *checks* the token** — the DB, not S3. Stale workers write attempt-scoped immutable keys; the DB promotes exactly one.
- **Fencing predicate must be EXACT (`= my_epoch`), never `>=`.**
- **Transactional outbox** wherever "write state then enqueue/emit" appears (conditional write ≠ atomicity).
- **Be honest about gaps** rather than overstating ("complete isolation," "impossible to leak," "exactly-once over HTTP").
