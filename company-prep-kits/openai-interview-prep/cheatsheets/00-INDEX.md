# Cheat Sheets — Quick Revision

Plain-language revision cards. Each file has: **(1)** a simple summary, **(2)** key deep dives in simple terms, **(3)** a DB schema where the design is database-modeling-heavy.

For the full validated write-ups (with all debate-won nuances), see the parent folder `../`.

| # | Design | DB schema included? | One-line hook |
|---|--------|:---:|---------------|
| 01 | [ChatGPT Enterprise RAG](01-chatgpt-enterprise-rag.md) | light | Authorize before you load the text |
| 02 | [ChatGPT Clone (frontend)](02-chatgpt-clone-frontend.md) | none (stateless) | Stream with fetch(), not EventSource |
| 03 | [CI/CD Scheduler](03-cicd-scheduler.md) | ✅ | Exactly-once *committed*, not executed |
| 04 | [Slack Enterprise Chat](04-slack-enterprise-chat.md) | ✅ | One writer per channel; read-pointer unreads |
| 05 | [Google Calendar](05-google-calendar.md) | ✅ | Month-bucket index + high-water-mark sync |
| 06 | [URL Shortener](06-url-shortener.md) | ✅ | Right-size to Aurora; LWW ≠ uniqueness |
| 07 | [Payment Processor](07-payment-processor.md) | ✅ | SettlementWork table; LSI is a trap |
| 08 | [Webhook Delivery](08-webhook-delivery.md) | ✅ | DB retry scheduler, not visibility timeout; 3 IDs |
| 09 | [POI / Yelp Nearest-K](09-poi-yelp-nearest-k.md) | ✅ | OpenSearch primary; fixed-level grid |
| 10 | [Crossword Solver](10-crossword-solver.md) | ✅ | Parallelize width; durable work-unit ledger |
| 11 | [Cloud IDE](11-cloud-ide.md) | ✅ | Fencing lease on the volume; continuous durability ≠ snapshots |
| 12 | [Sora Video Scheduling](12-sora-video-scheduling.md) | ✅ | Exactly-one *side effect* (not just artifact); slot fencing |
| 13 | [Chess.com (real-time game)](13-chess.md) | ✅ | Sequencer + OCC; clock is data (`clock_timestamp()`); pub/sub loses events |

**Recurring traps to never fall into:**
- Don't claim "exactly-once *execution*" or "optimal" (no metric).
- Fencing predicate is `= my_epoch` (exact), never `>=`.
- "Last write wins" can never enforce uniqueness.
- "Write row, then enqueue/emit" loses data → use a transactional outbox.
- Right-size the database to the access pattern; don't reflexively reach for DynamoDB + Global Tables.
