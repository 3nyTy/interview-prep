# Detailed Explainers — Plain English, Deeper Than the Cheat Sheets

Middle tier of the prep kit. **More detail than the cheat sheets, still in simple terms** — each walks through *why* each decision is made, the traps, and what to say when the interviewer pushes. Includes the DB schema where the design is data-heavy.

Three tiers in this kit:
- `../cheatsheets/` — fastest glance (summary + key points + schema)
- `detailed/` (here) — plain-English deep explanation with "if pushed further" Q&A
- `../` (parent, numbered files) — full validated write-ups with debate scorecards

**Complete — all 13 designs** (numbers match the cheat-sheet and full-write-up files so the three tiers line up):

| # | Design | Plain-English crux |
|---|--------|--------------------|
| 01 | [ChatGPT Enterprise RAG](01-chatgpt-enterprise-rag.md) | Authorize-before-hydrate; the version+ACL-epoch "spine" fails closed |
| 02 | [ChatGPT Clone (frontend)](02-chatgpt-clone-frontend.md) | fetch()+ReadableStream SSE; force-flush; header-auth; stateless BFF |
| 03 | [CI/CD Scheduler](03-cicd-scheduler.md) | Exactly-once *committed* (not run); fencing = exact match, zombie writes rejected |
| 04 | [Slack Enterprise Chat](04-slack-enterprise-chat.md) | Targeted fan-out; single fenced writer per channel; unread = subtraction |
| 05 | [Google Calendar](05-google-calendar.md) | Month-bucket occurrence index (not duration-cap); high-watermark paginated sync |
| 06 | [URL Shortener](06-url-shortener.md) | Right-size to Aurora; random code + UNIQUE retry; LWW can't enforce uniqueness |
| 07 | [Payment Processor](07-payment-processor.md) | We're the middleman; strongly-consistent SettlementWork table beats the GSI trap |
| 08 | [Webhook Delivery](08-webhook-delivery.md) | DB time-bucket retry scheduler (SQS visibility timeout can't do 24h); 3 IDs |
| 09 | [POI / Yelp Nearest-K](09-poi-yelp-nearest-k.md) | Search engine not cell-table (filters are selective); fixed grid + radius expansion |
| 10 | [Crossword Solver](10-crossword-solver.md) | Parallelize tree WIDTH; durable fenced work-unit ledger + at-least-once replay |
| 11 | [Cloud IDE](11-cloud-ide.md) | Fencing lease + kill-before-route; continuous durability ≠ snapshots; AZ-pinned |
| 12 | [Sora Video Scheduling](12-sora-video-scheduling.md) | Exactly-one *side effect* via outbox (not just artifact); checkpoint must pay off |
| 13 | [Chess.com (real-time game)](13-chess.md) | One writer per game but Postgres OCC is the guarantee; clock is stored data; bare pub/sub loses moves silently |
