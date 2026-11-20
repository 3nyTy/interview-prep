# Design cribs — quick revision

Same three-tier convention as `~/openai-interview-prep/cheatsheets/`: **(1)** a plain-language summary,
**(2)** key deep dives in plain terms, **(3)** a schema where the design is data-modelling-heavy.

**Every crib in this folder was stress-tested against codex and revised.** The `⚠️ CORRECTED` /
`⚠️ REVISED` banners are not decoration — they mark claims that were wrong in the first draft and would
have been punished in the room.

| # | Design | Reports | Debated | One-line hook |
|---|---|---|---|---|
| 01 | [Job Scheduler](01-job-scheduler.md) | **67** 🔥 | ✅ 2 rounds | Guarantee decomposition; delivery mode is a per-job property |
| 02 | [Stock Trading Platform](02-stock-trading-platform.md) | 13 | ✅ | 100 wps is the trick — but execution ≠ settlement |
| 03 | [Live Price Ingestion](03-live-price-ingestion.md) | 2 (Staff) | ✅ | Capture durably, *then* conflate for display |
| 04 | [Stock Brokerage](04-stock-brokerage.md) | 1 | ✅ | Session recovery ≠ order-status recovery |
| 05 | [Job Board](05-job-board.md) | 1 (Staff) | — | Four subsystems; ask which one, then go deep |
| 06 | [The two security designs](06-security-designs.md) | 1p3a | ✅ | Best fit for the IAM role; both open-ended |

## The five corrections that mattered most

These came out of the codex debates and each one changes what you'd say in the room:

1. **Job Scheduler — the fencing token was minted but never *checked*.** Every ownership-sensitive
   write needs `AND epoch = :myEpoch` **and** the expected state. Miss one path and a zombie worker
   overwrites the new owner. Exact equality is **necessary, not sufficient** — DB fencing protects
   database writes only, never the outbound call.
2. **Job Scheduler — "skipping is safer than duplicating" is wrong as a blanket claim.** Missing a
   liquidation, cancellation or regulatory filing can be far worse than a duplicate. That's what
   resolves the two sources' contradiction: **the delivery contract belongs to the job type**, not the
   scheduler. Hello Interview's at-least-once and 1p3a's at-most-once are both right, for different jobs.
3. **Brokerage — the centerpiece was dangerous.** `ClOrdID` is **not** a universal FIX idempotency key,
   and "resubmit the same `ClOrdID`, a duplicate-reject proves it landed" is **false**. Correct: recover
   the FIX *session* (`ResendRequest`, `PossDupFlag=Y`), then query *business* state
   (`Order Status Request` / drop copy). Verdict was that repeating the original claim unqualified
   should get a Staff candidate rejected.
4. **Price ingestion — "market data is state, not events" is only true at the display tier.** Capture
   the feed to a durable log *first*, then derive the conflated projection. And a **quote is not a
   trade** — you cannot build an OHLCV bar from a last-price cache.
5. **Security — authentication was missing as its own layer**, and a pre-auth limiter keyed on a
   caller-supplied principal is spoofable. Plus: **rate-limiter eviction resets quota** (a bypass),
   which is the opposite failure from a cache miss.

## Recurring discipline across all six

- **Decompose the guarantee** before designing. Name what you own, what the network owns, and what the
  counterparty must do. Almost every hard question in this loop is that one move.
- **Conditional writes with an affected-row check**, never read-then-write. Put the guard in the
  `WHERE`.
- **Persisted, exact-epoch fencing.** Process-local checks are not fencing.
- **Scaled integers for money and shares.** See `../coding/FractionalShares.java` for the correct
  reason — the obvious one is false.
- **State what you cannot guarantee.** Every crib has a "what I can't promise" line. It reads as senior
  and it pre-empts the probe.
- **Requirements first.** Reciting any of this before establishing requirements is the failure mode
  codex flagged hardest.
