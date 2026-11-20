# Design Chess.com — Real-Time Stateful Multiplayer Game

> OpenAI Staff SWE prep. Validated via the cross-LLM debate skill (Codex, high effort, 1 round). Mandated stack: **Redis (cache/fanout) + PostgreSQL (source of truth) + Kafka (async side-effects)**. Verdict: v1 had the right skeleton (per-game sequencer, OCC, clock-as-data, outbox) but under-specified the semantics that decide *game outcomes* — lease/epoch authority, the `now()`-is-frozen bug, pub/sub silent loss, lag compensation, and server-authoritative rules. v2 closes them. Builds on the saved chess deep-dive notes in [99-interview-context].

## Problem
Backend for a real-time, stateful, turn-based multiplayer game (chess) with per-player **chess clocks**: two players alternate moves; each has a countdown that runs only on their turn; hitting zero loses on time ("flag"). Server-authoritative legality (no trusting the client). Live spectators. Focus = correctness under concurrency + failover, the clock, event delivery, and game-domain rules — not the UI.

## Scale assumptions
Millions of concurrent games at peak; per-game write rate is low (a move every few seconds) but aggregate write QPS is high. A single viral/streamed game can draw 100k+ spectators. Bullet/blitz means sub-second timing precision *changes outcomes*, so latency and clock semantics are first-class, not polish.

## Architecture (v2)

### Core shape — single-writer sequencer per game, OCC as the real guarantee
**One logical writer per `game_id`**, sticky-routed to a game server (the actor pattern on the mandated stack). All mutations to a game serialize through it → clean total order (`version` 1,2,3…) and one place to evaluate legality + clock. But the in-memory actor is only a *latency* optimization: **correctness rests on PostgreSQL OCC**, so a split-brain (two servers both believing they own the game) still cannot corrupt state or fork the version line. A stateless WS gateway tier terminates player/spectator connections and forwards commands to the owning server.

### Ownership & failover — epoch monotonicity is the safety property (lease is liveness-only)
`owner_epoch` lives **on the `games` row itself** (authoritative — not a separate lease row that can diverge). Taking ownership is a CAS that **bumps `owner_epoch` on that same row**. A server accepts commands only for games it owns at its current epoch, and every write carries `owner_epoch = :my_epoch` **exact** (never `>=` — `>=` lets a stale owner pass).

The Staff-level point: **safety comes from epoch monotonicity + the exact-match CAS, NOT from lease wall-clock timing.** A "zombie" old owner that resumes after a GC pause and tries to write is rejected *iff* someone has since bumped the epoch — and if nobody has, it's still the sole legitimate owner and its (late) write is simply a slow move. So I deliberately **do not** put `lease_expires_at > now()` in the write predicate (Codex suggested it): that would wrongly fail a legitimate sole owner who thought for a long time while its lease lapsed but nobody took over. The lease/heartbeat exists **only to decide *when another server is allowed to attempt takeover*** (liveness) — and that takeover is itself the atomic epoch bump, which fences all in-flight writes at the old epoch. Lease expiry decisions use **DB time**, not app-host wall clocks. On takeover the new owner rebuilds actor state from the self-contained `games` row (no replay needed — see below).

### Move application — one atomic transaction (fencing + ordering + idempotency + outbox)
```sql
WITH upd AS (
  UPDATE games
     SET version = version + 1,
         fen = :new_fen,                          -- full rules state, not just squares
         active_side = :opponent,
         active_deadline_at = clock_timestamp() + (:opp_remaining_ms || ' ms')::interval,
         remaining_ms_mover = :banked_mover_ms,   -- integer ms; time-control rule applied
         halfmove_clock = :hmc, repetition_hash = :zobrist, status = :status
   WHERE game_id = :gid
     AND version = :expected_version              -- OCC: total order, no double-apply
     AND owner_epoch = :my_epoch                  -- fencing: EXACT
     AND active_side = :mover
     AND clock_timestamp() < active_deadline_at   -- reject if they already flagged
  RETURNING version)
INSERT INTO move_event (game_id, version, ply, uci, san, clock_ms) SELECT ... FROM upd;
-- + INSERT outbox(game_id, version, event_type, payload) SELECT ... FROM upd;
-- + UPSERT idempotency(game_id, player_id, key, request_hash, response) in same txn
```
Inserts are **CTE-gated on the UPDATE affecting a row**, so you never persist an event for a failed move or miss one for a committed move. `UNIQUE(game_id, version)` on `move_event` and `outbox`. **Commit, *then* broadcast** — the broadcast is only a hint (it can fail post-commit); correctness is recovered via durable replay. If `affected_rows == 0`: the command lost the race → reload + resync.

**Use `clock_timestamp()`, not `now()`/`transaction_timestamp()`.** In PostgreSQL `now()` is frozen at transaction start; a txn that stalls then commits would evaluate the deadline against a stale instant and could let a move commit *after* the real flag. `clock_timestamp()` reads the wall instant at statement execution.

### The clock — data, not a running timer
Store `active_side`, `active_deadline_at` (absolute instant the mover flags), and banked `remaining_ms_white/black` as **integer milliseconds** (rounding matters in bullet). On turn start `deadline = clock_timestamp() + their_remaining`; remaining at any instant = `deadline − clock_timestamp()`. **Nothing ticks** — arithmetic vs a stored deadline. **Clock authority = the DB clock**, never an app host (many app servers, clocks drift; one PG clock decides).

- **Time-control types are explicit, not hand-waved.** Persist `time_control_type`. **Fischer:** add increment to the mover's bank on a committed move (up to any cap). **Bronstein/simple(US) delay:** charge `max(0, elapsed − delay)` (delay refunded up to the delay amount); the arithmetic differs from Fischer and is implemented per-rule with boundary tests (zero-time, exact-boundary, delayed move).
- **Lag compensation (the chess-server crux).** Pure server-receipt-time unfairly flags a player on a 300ms link. Policy: server-receipt-time is the base, minus **bounded** compensation derived from a per-connection ping/RTT estimate, **capped** per move (e.g. ≤ measured RTT/2, hard-capped) to block a client from claiming fake latency. State the policy explicitly; it's a product+fairness decision an interviewer expects named.

### Every command is a versioned transition — races resolve themselves
Move, **timeout, resignation, draw-offer, draw-accept, abort, claim-draw** are all the *same* fenced versioned transaction shape with terminal-state guards + explicit precedence — so resign-vs-move, draw-accept-vs-move, timeout-vs-resign, and claimable-draw-vs-flag all resolve deterministically (only one wins the `version` CAS).
- **Move** commits iff `version=:expected AND owner_epoch=:mine AND active_side=:mover AND clock_timestamp() < active_deadline_at`.
- **Timeout** commits iff `version=:expected AND active_side=:timed_out_side AND clock_timestamp() >= active_deadline_at`.

Against the single DB clock exactly one time-guard is true, and the shared `version=:expected` CAS means exactly one command wins — a move a microsecond before the flag and a scanner-fired timeout cannot both commit. A **durable deadline scanner only *triggers*** a timeout (submits the same versioned txn); it never *decides*. Late or double firing is harmless (idempotent via version CAS).

### Deadline scanner — at scale
Don't naively `WHERE active_deadline_at <= now()` across millions of rows (hot global index, polling latency delays rating settlement). Use a **time-bucketed / timeout-wheel index partitioned by shard**; workers **claim due deadlines with `FOR UPDATE SKIP LOCKED`** and submit the versioned timeout txn. Scanner lateness never changes the *outcome* (the move path already rejects after deadline) — only settlement UX.

### Server-authoritative rules (a chess interviewer drills this)
Persist **full rules state**, not just board squares: FEN-equivalent (piece placement + **castling rights** + **en-passant square** + **halfmove clock** + fullmove number) plus a **repetition-hash history** (Zobrist) for threefold. Server does authoritative move generation/validation (proven engine/rules module): legal moves, check/checkmate/stalemate, castling, en passant, promotion, **fifty-move** (halfmove clock), **threefold** (hash history), **insufficient material**. Distinguish **automatic** draws (stalemate, insufficient material, fivefold/75-move) from **claimable** draws (threefold, fifty-move) — the latter race a move/flag and go through the same versioned-command precedence.

### Event delivery — NOT bare pub/sub
Redis pub/sub is fire-and-forget with no per-subscriber sequence: a gateway that briefly drops its Redis subscription misses version 42, and if no later event arrives for minutes the client **never learns it's stale** — players/spectators sit on a wrong board/clock. Fix:
- **Durable ordered log per game.** Fan out over a **Redis Stream** per game (`XADD` with the `version` as/inside the ID → ordered, replayable, gap-detectable), backed by PostgreSQL `move_event` as the ultimate durable log. Bare pub/sub is at most an optional low-latency accelerator, never the source of correctness.
- **Idle heartbeat carries current `version`**, so a client detects a gap even when no new move happens and pulls the missing tail.
- **Reconnect has no snapshot/subscribe race:** *subscribe & buffer first*, then read snapshot at version `V` (from **primary or an LSN-fresh replica** ≥ the client's last version — replica lag must not resurrect stale board/clock), then **drain buffered events > V** (dedup by version). Broadcast is a hint; the durable log + version dedup make delivery exactly-once *in effect*.

### Spectator fanout — separate path from players
Two players get the **low-latency authoritative path**. Spectators (up to 100k on a viral game) get a **separate, scalable, slightly-delayed, coalesced broadcast path**: hierarchical/edge relay fanout (one committed move → 100k WS writes must not hit Redis or PG directly), per-game spectator **admission/rate limits**, and a **replay cache outside PostgreSQL** so a gateway-death reconnect storm doesn't stampede the DB. Spectators are read-only, so no per-connection game state — just cursor-based catch-up on the game's event log.

### Kafka / async side-effects
Same-txn `outbox` → **CDC relay** (Debezium-style) → Kafka, **keyed by `game_id` only** (all events for a game land in one partition, in order; version/type live in the payload — keying by `(game_id,version,event_type)` would let different types reorder). Consumers (Elo/Glicko rating, notifications, anti-cheat, analytics, spectator-history) are **idempotent on `(game_id, version, event_type)`**. Be explicit about **synchronous vs eventual**: the move result to players is synchronous (PG commit + fanout); ratings/notifications/anti-cheat are **eventual**. Monitor outbox/CDC lag; **retain outbox by CDC-ack LSN watermark**, archive/delete below it (unbounded outbox is a latent outage).

### Idempotency, matchmaking, lifecycle
- **Idempotency** row is written **in the same PG txn** as the accepted command, keyed `(game_id, player_id, key)` storing `request_hash + response` — survives failover; same key+same payload → stored response; same key+different payload → conflict (not a false success). (In-memory idempotency would break on ownership change.)
- **Double-match:** `game_participants` with partial unique index `UNIQUE(player_id) WHERE active`, **scoped by mode** (a player may hold many correspondence games but one live blitz game), enforced in the same txn that creates/finishes a game.
- **Explicit game states:** `active | paused | adjourned | abandoned_pending | resigned | timed_out | drawn | aborted | completed`, storing who/why/when. Disconnect ≠ abandonment ≠ forfeit: a disconnect starts an abandonment timer (its own versioned deadline), pause/adjourn freezes the clock by banking remaining and clearing `active_deadline_at`.
- **Premove:** a non-authoritative client intent, bounded queue (typically 1), applied as a normal versioned move only after the opponent's transition commits and only if still legal; clock cost per product policy (usually ~0 via lag-comp).

### Capacity
Shard/partition PG by `game_id` hash. Budget **write amplification honestly** (one `games` UPDATE + `move_event` insert + `outbox` insert + idempotency upsert per move, plus scanner claims and reconnect reads), size connection pooling, offload viral-game hot-shard reads to Redis/replicas. PG capacity is **"credible pending benchmark," never "proven."**

## Top deep-dives an interviewer drills
1. Sequencer + OCC correctness under failover/split-brain — why exact-epoch CAS is sufficient and lease timing is liveness-only.
2. The clock: data-not-timer, `clock_timestamp()` (not `now()`), lag compensation, delay/increment rules, move-vs-timeout.
3. Event delivery correctness: why bare pub/sub loses events silently, and the Stream + heartbeat + subscribe-then-snapshot-then-drain fix.
4. Draw/resign/timeout/move race precedence (all commands are versioned transitions).
5. Server-authoritative rules (repetition/fifty-move/insufficient-material, claimable vs automatic draws) + viral spectator fanout.

## Debate scorecard
Codex raised 10 "critical" (9 accepted, 1 refined) + 12 significant + 4 minor. **Accepted:** epoch-must-be-authoritative-on-the-row; `now()`-frozen-at-txn-start → use `clock_timestamp()`; show move-txn atomicity (CTE-gated, unique(game_id,version), commit-before-broadcast); bare-pub/sub silent loss → durable Stream + heartbeat gap-signal; reconnect subscribe/snapshot race → subscribe-buffer-then-drain; lag compensation; delay/increment per-rule; server-authoritative rules state (FEN+Zobrist+halfmove); replica-read staleness on resync; deadline-scanner scale (timeout wheel + SKIP LOCKED); viral spectator fanout tier; hot-partition/write-amplification budget; CDC-lag = explicitly-eventual; outbox retention by LSN; idempotency-in-PG-txn; all-commands-versioned (resign/draw races); premove; disconnect/abandon/pause state machine; mode-scoped uniqueness; int-ms precision; self-contained row (no replay to resume); broadcast-is-a-hint; Kafka key by game_id only. **Refined (partial rebut):** the "add `lease_expires_at>now()` to the write predicate" fix — safety is already guaranteed by epoch monotonicity + exact-match CAS, and adding a wall-clock gate to the write would incorrectly fail a legitimate sole owner mid-think; the lease is purely a takeover *trigger*. **Consensus strengths:** per-game single-writer/actor shape, versioned OCC on the authoritative row, clock-as-stored-deadline, transactional outbox. The gap was never the skeleton — it was the outcome-deciding semantics around it.
