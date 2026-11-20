# Mock Interview Scorecards

Per-design self-assessment from live mock interviews (Mode B — defend out loud).
Each card lists **everything a Staff answer needs**, marks **what I identified myself** vs **what I missed**, and gives a **1–2 line fix** for every miss. A new card is added after each mock.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

| Q | Design | Verdict (one line) | Date |
|---|--------|--------------------|------|
| 01 | [Enterprise RAG](q01-enterprise-rag.md) | Strong bones + above-average caching instinct; but the two *defining* ideas (authorize-before-hydrate, version+epoch spine) came out under prompting, not up front | 2026-07-11 |
| 02 | ChatGPT clone (frontend) | *skipped — to revisit* | — |
| 03 | [CI/CD Scheduler](q03-cicd-scheduler.md) | Good orchestration/product instincts, reasons well under pushback; the correctness *core* (fencing + honest exactly-once) needed heavy prompting | 2026-07-11 |
| 04 | [Slack (enterprise chat)](q04-slack-chat.md) | Best round yet — volunteered targeted fan-out, read-pointer unread, OCC ordering (transferred chess); one gap = the transactional-outbox reflex on dual writes | 2026-07-14 |
| 05 | [Google Calendar](q05-google-calendar.md) | Most fluent transfer yet (seq/change-log/HWM/CDC all reused correctly); gaps were *modeling* facts — bucket granularity, rule-based recurrence, timezones | 2026-07-14 |
| 06 | [URL Shortener](q06-url-shortener.md) | Reached every answer but *led* with the two traps — over-building (sharded a single-node workload) and 301-vs-302; growth edge = right-sizing judgment, not machinery | 2026-07-15 |
| 07 | [Payment Processor](q07-payment-processor.md) | Strongest domain round — correct framing, built the strongly-consistent settlement worklist + claim/lease unprompted; edges = outbox's 2nd half (downstream idempotency, not an alarm) + a reclaim-status bug | 2026-07-15 |
| 08 | [Webhook Delivery](q08-webhook-delivery.md) | Built the retry scheduler (hardest part) unprompted + correct at-least-once; edges = stable delivery_id for dedupe (not a status-check) & SSRF IP-pinning/no-redirects | 2026-07-15 |
| 09 | [POI / Yelp Nearest-K](q09-poi-yelp.md) | Nailed the big calls (OpenSearch-not-cell-table via selectivity; SoR+CDC+reconciliation); only gap was memorizable geo-domain facts (fixed grid + KNN termination) | 2026-07-16 |
| 10 | [Crossword Solver](q10-crossword-solver.md) | Most abstract problem + fatigue → largely taught; ledger *is* the Q7/Q8 claim-with-lease pattern, new ideas = why-width framing, deterministic replay, termination detection. **Re-attempt fresh** | 2026-07-16 |
| 11 | [Cloud IDE](q11-cloud-ide.md) | Security half automatic; produced kill-before-route + CAS state machine with nudges. Key idea to bank: **fencing only works where the resource checks the token** — dumb storage (EBS/S3) needs physical serialization | 2026-07-16 |
| 12 | [Sora Scheduling](q12-sora-scheduling.md) | Correctness reflexes transfer cleanly (fencing, CDC/outbox, S3-won't-fence→DB-pointer straight from Cloud IDE); edge = scheduling/capacity domain — keep admission vs scheduling vs preemption distinct + non-fungible fair-share | 2026-07-16 |
| 13 | [Chess.com](q13-chess.md) | Knows the machinery (built it) — produced every crux mechanism under probing; gap is recall *crispness*: lead with the 3 crux problems, state mechanisms unified (version≠epoch, timeout=versioned-command, heartbeat). **Most likely Q — drill the opening 90s** | 2026-07-16 |

## The recurring pattern across cards (drill this hardest)
On any **"workers/servers can die"** problem, *lead with* the trio — it appears in CI/CD, Chess, Slack, Sora, Cloud IDE:
1. **Fencing** = monotonic token (attempt / epoch), **exact-match** conditional write (`=`, never `>=`) → a zombie's late write is **rejected**.
2. **Idempotency key** on the external side effect (stable across retries, keyed on *position* not attempt) → duplicate side effect becomes a **no-op**.
3. **Honest "exactly-once"** = *committed once + at-least-once execution + idempotent side effects*. Never say "exactly-once execution."
