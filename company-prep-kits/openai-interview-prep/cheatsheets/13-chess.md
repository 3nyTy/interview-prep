# Cheat Sheet — Chess.com (Real-Time Stateful Game)

> Debate-validated (Codex, high effort): v1 skeleton right (per-game sequencer, OCC, clock-as-data, outbox) → v2 closes the outcome-deciding semantics. Mandated stack: Redis + PostgreSQL (SoT) + Kafka. Builds on the saved chess deep-dive notes.

## 1. Summary (in plain terms)
Two players alternate moves; each has a clock that runs only on their turn; run out and you lose on time. The server — never the client — decides what's legal and who flagged. The system's job: apply moves in one true order with no double-apply, run the clock correctly and fairly, survive a game server dying without corrupting or forking the game, deliver every move to both players + up to 100k spectators without silently dropping one, and settle draw/resign/timeout races deterministically.

## 2. Key deep dives (plain terms)

- **One writer per game, but Postgres is the real guarantee.** Each game is owned by one game server (actor pattern) for a clean move order — but that's just for latency. Correctness rests on **PostgreSQL optimistic concurrency**: every move is `UPDATE games SET version=version+1 WHERE version=:expected AND owner_epoch=:mine AND active_side=:mover AND clock_timestamp()<deadline`. If a second server thinks it owns the game too, its writes still fail the CAS. No corruption, no forked version line.

- **Fencing = epoch monotonicity, NOT lease clocks (the subtle Staff point).** `owner_epoch` lives **on the games row**; taking over bumps it atomically. Predicate is `owner_epoch = :mine` **exact, never `>=`**. A zombie old owner (post-GC-pause) is rejected *only if* someone bumped the epoch; if nobody did, it's still the sole owner and its late write is just a slow move. So **don't** add `lease_expires_at>now()` to the write — that would wrongly fail a legit sole owner thinking for a long time. The lease is only a *trigger for when another server may attempt takeover* (liveness), and takeover = the epoch bump (which fences everything).

- **The clock is DATA, not a ticking timer.** Store `active_side`, `active_deadline_at` (absolute instant they flag), banked `remaining_ms` (**integer ms**). Turn start: `deadline = clock_timestamp() + their_remaining`. Remaining = `deadline − clock_timestamp()`. Nothing ticks. **Authority = the DB clock**, never an app host.

- **Use `clock_timestamp()`, not `now()` (real Postgres bug).** `now()`/`transaction_timestamp()` is frozen at transaction start — a stalled txn would judge the deadline against a stale instant and let a move commit *after* the flag. `clock_timestamp()` reads the true instant at execution.

- **Move vs timeout — and resign/draw/abort — are all the SAME versioned command.** Timeout commits iff `active_side=timed_out AND clock_timestamp()>=deadline`; move iff `clock_timestamp()<deadline`. Against one DB clock exactly one time-guard is true, and the shared `version` CAS means exactly one wins. Every terminal command (resign, draw-accept, claim-draw, abort) is the same shape with precedence rules — so resign-vs-move and draw-vs-flag resolve deterministically. A **deadline scanner only triggers** timeouts (never decides); late/double firing is harmless.

- **Lag compensation, or you flag players unfairly.** Pure server-receipt-time punishes a 300ms link. Base = receipt time **minus bounded, ping-measured compensation, capped per move** (so a client can't fake latency). Name the policy — a chess interviewer expects it. Implement Fischer (add increment) vs Bronstein/simple-delay (`max(0, elapsed−delay)`) explicitly.

- **Redis pub/sub silently loses events — don't rely on it.** Bare pub/sub is fire-and-forget with no per-subscriber sequence: miss version 42, and if nothing else arrives for minutes the client never knows it's stale. Fix: **durable ordered log per game (Redis Stream keyed by version, backed by the `move_event` table)**, plus an **idle heartbeat carrying the current version** so gaps are detected even when idle. Broadcast is a *hint*; correctness comes from the durable log + version dedup.

- **Reconnect has a classic race — order it right.** *Subscribe & buffer first → read snapshot at version V (from primary or an LSN-fresh replica, never a lagging one) → drain buffered events > V.* Otherwise an event committed between the snapshot query and the subscribe is lost with no gap signal.

- **Server-authoritative chess rules (interviewers drill this).** Persist full state — FEN + **castling rights + en-passant + halfmove clock** + a **Zobrist repetition-hash history**. Server validates legality, check/checkmate/stalemate, promotion; detects fifty-move (halfmove clock), threefold (hash history), insufficient material. Distinguish **automatic** draws (stalemate, insufficient material) from **claimable** (threefold, fifty-move) — claimable ones race a move/flag via the same versioned precedence.

- **Viral spectator fanout is a separate tier.** 2 players = low-latency authoritative path. 100k spectators = a separate coalesced, slightly-delayed **hierarchical/edge relay** path with admission limits and a **replay cache outside Postgres** (so a gateway-death reconnect storm doesn't stampede the DB).

- **Kafka off the critical path, but exact.** Same-txn **outbox → CDC relay → Kafka, keyed by `game_id` only** (keying by type reorders). Consumers idempotent on `(game_id, version, event_type)`. Move-to-player = synchronous; ratings/notify/anti-cheat = **explicitly eventual**. Retain outbox by CDC-ack LSN watermark (unbounded outbox = latent outage).

## 3. Data model (PostgreSQL, sharded by game_id)
```
games (the authoritative, self-contained row — resume needs NO replay):
  PK game_id
  version                       -- OCC / total order
  owner_epoch                   -- fencing lives HERE (bumped on takeover); predicate = EXACT
  fen, castling, ep_square, halfmove_clock, repetition_hash   -- full rules state
  active_side, active_deadline_at (timestamptz), remaining_ms_white, remaining_ms_black
  time_control_type, status (active|paused|adjourned|abandoned_pending|resigned|
                             timed_out|drawn|aborted|completed)

move_event (durable ordered log — powers replay + spectator catch-up):
  PK (game_id, version)         -- UNIQUE guards double-apply / double-emit
  ply, uci, san, clock_ms

outbox (same txn as the move; CDC → Kafka; retain by CDC-ack LSN):
  PK (game_id, version, event_type)   payload

idempotency (written in the SAME txn as the command; survives failover):
  PK (game_id, player_id, key)  request_hash, response

game_participants (double-match guard, scoped by mode):
  UNIQUE(player_id) WHERE active   -- enforced in the create/finish txn

Move txn: WITH upd AS (UPDATE games ... WHERE version=:exp AND owner_epoch=:mine
          AND active_side=:mover AND clock_timestamp()<active_deadline_at RETURNING version)
          INSERT move_event/outbox/idempotency SELECT FROM upd;  commit → THEN broadcast.
Deadline scanner: time-bucketed / timeout-wheel, FOR UPDATE SKIP LOCKED, only triggers.
```

**Biggest traps:** (1) `owner_epoch>=` instead of `=` (stale owner slips through); (2) `now()` instead of `clock_timestamp()` (move commits after the flag); (3) trusting bare Redis pub/sub for delivery (silent loss, no gap signal); (4) pure server-receipt-time clock (unfair flagging over latency); (5) storing only board squares, not full rules state (can't do threefold/fifty-move/en-passant); (6) treating spectator fanout as the same path as the 2 players.
