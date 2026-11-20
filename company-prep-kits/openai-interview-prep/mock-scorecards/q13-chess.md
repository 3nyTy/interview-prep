# Mock Scorecard — Q13: Chess.com (real-time game)

**Date:** 2026-07-16 · **Mode:** B (live mock, warm rep — design built earlier this session) · **Full write-up:** [`../13-chess.md`](../../13-chess.md) · [`../detailed/`—n/a, see full write-up]

**Verdict:** I clearly **know the machinery** (I helped build this design) — with probing I produced *every* key mechanism. The gap is **recall crispness under pressure**: I *assembled* the answer under questioning rather than *leading* with the unified forms. Since this is my **most likely question**, the drill is to open with the three crux problems and state each mechanism tightly without a nudge.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ❌ **Lead with the three crux problems** — I opened with matchmaking / clock-*sync* / move-*validation* (the surface). *The crux is:* **ordering-under-failover, the clock *race*, lossless delivery.** Open there; park matchmaking.
2. ⚠️ **Ordering under failover** — I gave the happy path first (didn't answer failover), then got **single-writer + lease + atomic txn** under probing. *Fixes needed:* (a) fence with an **exact-match monotonic epoch**, not TTL/worker-id ambiguity; (b) the **OCC version-CAS is the real guarantee** (`WHERE version=:expected`), lease/sequencer is just a latency optimization; (c) **separate `version` (per move → ordering) from `owner_epoch` (per takeover → fencing)** — I conflated them, which breaks per-move OCC.
3. ✅ **Move applies in one atomic txn** — player1 + player2 + move-update together (also fixed the split-clock-update).
4. ✅ **DB clock is the authority** (unprompted) — consistent regardless of client/WS/consumer clocks. *Sharp add:* use **`clock_timestamp()`, not `now()`** (frozen at txn start → a stalled txn commits a move after the flag).
5. ✅ **Deadline check on the move** — `deadline >= now()` rejects a late move.
6. ⚠️ **Move-vs-timeout race** — I had **two separate checks** (move deadline + a Redis-TTL "game over" write). *Fix:* make the **timeout the *same versioned command*** — `WHERE version=:expected AND now() >= deadline` — so the **version CAS** picks one winner and `now()<deadline` vs `>=deadline` are mutually exclusive against the single DB clock → **impossible for both to commit.** Redis TTL **triggers, never decides**; add a **durable deadline scanner** backstop.
7. ✅ **Event-delivery catch-up** — client tracks its move number, server backfills from a **durable log** (implies not trusting pub/sub alone).
8. ✅ **Idle heartbeat with latest move number** — got it once the idle scenario was posed; this is what makes a missed move *non-silent*. *Fold in:* substrate = durable per-game log (Redis Stream keyed by version + move_event), pub/sub only a fast-path; reconnect = **subscribe-buffer → snapshot@V → drain > V**.
9. ⚠️ **Server-authoritative rules** — touched "validate moves"; *depth:* full **FEN** (castling/en-passant/halfmove) + **Zobrist repetition history** → threefold/fifty-move/insufficient-material; automatic vs claimable draws. Not drilled.
10. ⚠️ **Lag compensation** — a slow-link player flags unfairly without bounded, capped ping-based compensation; time-control types (Fischer vs Bronstein/delay) per rule. Not raised.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"One fenced sequencer per game + Postgres OCC is the real guarantee: `UPDATE … WHERE version=:expected AND owner_epoch=:mine`; version bumps per move (ordering), epoch per takeover (fencing)."*
- *"The clock is stored data, not a timer; a move and a timeout are the SAME versioned command (`now()<deadline` vs `>=`), so the version CAS + one DB clock guarantee exactly one wins; use `clock_timestamp()`; a scanner only triggers."*
- *"Never bare pub/sub — a durable per-game log + an idle heartbeat with the latest version, so a missed move is never silent; reconnect = subscribe-buffer → snapshot → drain."*

## Pattern to drill
This design is the **capstone of every reflex** — fencing, OCC, outbox, delta-sync/heartbeat, DB-clock authority — and I *have* them all. The single gap is **leading with the crux + stating mechanisms in unified form** (version≠epoch; timeout = a versioned command; heartbeat kills silent gaps) instead of assembling them under questioning. Drill the *opening 90 seconds* of this one specifically.
