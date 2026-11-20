# Chess.com (Real-Time Stateful Game) — Detailed Explainer (plain English)

> Debate-validated (Codex). Pattern: v1 had the right skeleton (one writer per game, optimistic concurrency, clock-as-data, outbox) but under-specified the semantics that actually decide *who wins the game*. The fixes below are what make it Staff-level. Mandated stack: Redis + PostgreSQL (source of truth) + Kafka. Builds on the saved chess deep-dive notes.

## What we're building
The backend for online chess. Two players alternate moves. Each has a **clock** that counts down only while it's their turn; if it hits zero they lose "on time" (they "flag"). The **server is the referee** — it decides what moves are legal and who ran out of time, never the client (or players would cheat). Thousands to millions of games run at once, and a popular streamed game can have 100,000+ spectators watching live.

## What makes it hard
- **One true order of moves, even when a server dies.** If two servers ever both think they own a game, they must not corrupt it or create two different histories.
- **The clock is subtle.** It mixes time, concurrency, and fairness: a move arriving a hair before the flag vs. a timeout firing at the same instant must have exactly one winner — and a player on a slow connection shouldn't lose unfairly.
- **Delivering every move.** Both players and all spectators must see every move in order; silently dropping one leaves someone staring at a wrong board.
- **Real chess rules.** Checkmate, stalemate, castling, en passant, promotion, threefold repetition, the fifty-move rule, insufficient material — all server-side and correct.

## The overall flow
1. **Connect:** player opens a WebSocket to a stateless gateway; the gateway routes commands to the one game server that **owns** that game.
2. **Move:** the owning server validates legality + clock, then commits the move to PostgreSQL in a single transaction that also advances the clock and writes the outbound event.
3. **Broadcast:** after the commit, the move is pushed to both players and spectators over a durable ordered log.
4. **Side effects:** rating updates, notifications, and anti-cheat happen asynchronously (eventually) off a transactional outbox → Kafka.
5. **End:** checkmate/resign/timeout/draw finalizes the game and settles ratings.

## Hard problem 1: one writer per game — but Postgres is the real guarantee
Each game is assigned to a single game server that processes its commands one at a time (the "actor" pattern). That gives a clean, single-threaded order of moves and one natural place to check the rules and the clock. **But that in-memory ownership is only a speed optimization.** The actual correctness guarantee is **optimistic concurrency in PostgreSQL**: every move is a conditional update —

> `UPDATE games SET version = version+1, ... WHERE version = :expected AND owner_epoch = :mine AND active_side = :mover AND clock_timestamp() < deadline`

If a network partition makes two servers both believe they own the game, only one's update matches `version = :expected`; the other's fails and it reloads. So **split-brain can't corrupt state or fork the move history** — the database is the referee of last resort.

## Hard problem 2: fencing done right — epoch monotonicity, not lease clocks
How do we stop a "zombie" server — one that froze in a garbage-collection pause, was presumed dead, and then woke up — from writing a stale move?

Each game row carries an `owner_epoch`. Taking ownership **bumps that number** on the same row. Every write requires `owner_epoch = :mine` — **exact match, never `>=`** (a `>=` check would let a stale owner sneak through, defeating the whole point).

Here's the subtle Staff-level part. **Safety comes from the epoch being monotonic + the exact-match check — not from a lease's wall-clock expiry.** A zombie is rejected *only if* someone actually took over and bumped the epoch. If nobody took over, the zombie is still the *sole legitimate owner*, and its late write is simply a slow move — perfectly fine. That's why I deliberately **do not** add "and the lease hasn't expired" to the move's WHERE clause (Codex suggested it). Doing so would wrongly reject a legitimate sole owner who happened to think for a long time while its lease lapsed but no one else took over. The lease and heartbeats exist for one purpose only: to decide **when another server is allowed to *attempt* a takeover** (a liveness concern) — and that takeover is itself the atomic epoch bump, which fences every in-flight write at the old epoch. Those takeover decisions use the **DB's clock**, not app-server wall clocks (which drift and cause false takeovers).

## Hard problem 3: the clock — data, not a ticking timer
Naive designs run a countdown thread per game. That doesn't survive restarts and drifts across servers. Instead, **the clock is stored data:**
- `active_side` — whose clock is running.
- `active_deadline_at` — the absolute timestamp at which the current player flags.
- banked `remaining_ms` for each side, stored as **integer milliseconds** (rounding errors matter in bullet chess).

When a turn starts, `deadline = clock_timestamp() + their banked remaining`. The time left at any moment is just `deadline − clock_timestamp()`. **Nothing ticks — it's arithmetic against a stored deadline.** And the **authority is the database's clock**, never an app host: with many app servers whose clocks drift, one consistent DB clock has to be the single source of truth.

**A real PostgreSQL gotcha:** use `clock_timestamp()`, *not* `now()` or `transaction_timestamp()`. In Postgres, `now()` is frozen at the moment the transaction *started* — so a transaction that stalls for a second and then commits would evaluate the deadline against a stale instant, potentially letting a move commit *after* the player already flagged. `clock_timestamp()` returns the true wall-clock instant at statement execution.

**Time-control variants must be spelled out**, not waved at. Persist the `time_control_type`. **Fischer increment:** add the increment to the mover's bank when their move commits. **Bronstein / simple (US) delay:** charge `max(0, elapsed − delay)` — the delay is refunded up to its amount, and the formula genuinely differs from Fischer. Each is implemented as its own rule with boundary tests (zero time left, exact-boundary move, a move faster than the delay).

**Lag compensation — the fairness crux of a chess server.** If we adjudicate purely on when the server *received* the move, a player on a 300ms connection loses 300ms every move versus a local player. That changes outcomes in blitz. So the policy must be explicit: base timing on server receipt, **minus a bounded compensation** derived from a measured ping/round-trip estimate, **capped per move** so a client can't fake huge latency to steal time. There's no single "right" number — but a Staff candidate must *name the policy*, not ignore it.

## Hard problem 4: move vs timeout (and resign/draw) — all the same versioned command
What if a player moves a microsecond before their flag, at the same time the timeout scanner fires? Both must not commit.

The trick: **a move and a timeout are the same fenced, versioned transaction.**
- A **move** commits only if `clock_timestamp() < deadline`.
- A **timeout** commits only if `active_side = the timed-out side AND clock_timestamp() >= deadline`.

Against the single DB clock, exactly one of `< deadline` / `>= deadline` is true, and both carry the same `version = :expected` check — so the optimistic-concurrency CAS lets exactly one win. The race resolves itself with no ambiguity. A **durable deadline scanner** (which finds games whose deadline has passed) only *triggers* a timeout by submitting that same versioned transaction — it never *decides* the result. So if the scanner is late, or fires twice, it's harmless.

And this generalizes: **every terminal command — resignation, draw offer, draw acceptance, claim-a-draw, abort — is the same versioned-transition shape** with explicit precedence. That's how resign-racing-a-move, draw-accept-racing-a-move, and a claimable-draw-racing-a-flag all get a single deterministic winner instead of a corrupted game.

## Hard problem 5: delivering every move (bare Redis pub/sub will betray you)
The instinct is to publish each move to a Redis pub/sub channel and let gateways forward it to clients. **This silently loses events.** Redis pub/sub is fire-and-forget with no per-subscriber sequence number. If a gateway's subscription blips and misses version 42, and then nothing else happens for a few minutes (both players thinking), the client **never finds out it's stale** — it's showing the wrong board and clock with no way to know.

The fix is a **durable, ordered, replayable log per game:**
- Fan out over a **Redis Stream** per game (`XADD` with the move's `version` as/in the ID → ordered, replayable, and gaps are detectable), backed by the **`move_event` table** as the ultimate durable log. Bare pub/sub, if used at all, is only an optional low-latency accelerator — never the source of truth.
- Send an **idle heartbeat carrying the current version**, so even when no move is happening a client can notice it's behind and pull the missing tail.
- The **broadcast is only a hint** — it can fail after the commit. Correctness is recovered from the durable log plus version-based dedup, which makes delivery exactly-once *in effect*.

**Reconnect has a classic race.** "Send a snapshot, then subscribe" can miss a move committed *between* the snapshot query and the subscription going live. Order it correctly: **subscribe and buffer first → read the snapshot at version V → drain the buffered events with version > V** (dedup by version). And read that snapshot from the **primary or a replica verified fresh enough** (its log position ≥ the client's last version) — a lagging replica would resurrect a stale board or clock.

## Hard problem 6: real chess rules, server-authoritative
An interviewer will ding you if you "store the board." You must persist **full rules state**: the FEN-equivalent (piece placement + **castling rights** + **en-passant target square** + **halfmove clock** + move number) plus a **repetition-hash history** (Zobrist hashes of positions). The server generates and validates legal moves with a proven rules module, and detects check, checkmate, stalemate, castling, en passant, and promotion. It detects the **fifty-move rule** from the halfmove clock, **threefold repetition** from the hash history, and **insufficient material**. And it distinguishes **automatic** draws (stalemate, insufficient material) from **claimable** ones (threefold, fifty-move) — the claimable ones can race a move or a flag and go through the same versioned-command precedence.

## Hard problem 7: spectators and scale
Two players get a **low-latency, authoritative path**. Spectators are a different beast: a viral game can have 100,000 watchers, and one committed move must not turn into 100,000 writes against Redis or PostgreSQL. So spectators get a **separate, coalesced, slightly-delayed broadcast path** — hierarchical/edge relay fanout, per-game spectator admission and rate limits, and a **replay cache outside PostgreSQL** so that when a gateway dies, the reconnect storm doesn't stampede the database. Spectators are read-only, so they need no per-connection game state — just a cursor to catch up on the game's event log.

## The async side (Kafka) and housekeeping
Each move writes an **outbox row in the same transaction**. A **CDC relay** (Debezium-style) publishes those to Kafka, **keyed by `game_id` only** — all of a game's events land in one partition, in order (keying by event *type* would let types reorder). Consumers — rating (Elo/Glicko), notifications, anti-cheat, analytics — are **idempotent on `(game_id, version, event_type)`**. Be explicit that the move result to players is **synchronous** (it's committed and pushed) while ratings/notifications/anti-cheat are **eventual**. Monitor outbox/CDC lag, and **retain the outbox by the CDC-ack log position** (archive/delete below it) — an unbounded outbox is a slow-motion outage.

## Don't forget
- **Idempotency** is written **in the same transaction** as the accepted command, keyed `(game_id, player_id, key)` with the request hash + response — so it survives a failover (in-memory idempotency would break on ownership change). Same key + same payload → replay the stored response; same key + different payload → conflict, not a false success.
- **Double-match:** a partial unique index `UNIQUE(player_id) WHERE active`, **scoped by mode** (a player can hold many correspondence games but only one live blitz game), enforced in the same transaction that creates or finishes a game.
- **Lifecycle states are explicit:** active, paused, adjourned, abandoned-pending, resigned, timed-out, drawn, aborted, completed — recording who/why/when. A disconnect is *not* an abandonment or a forfeit: it starts an abandonment timer (its own versioned deadline); pause/adjourn freezes the clock by banking the remaining time and clearing the deadline.
- **Premove:** a client "intent" that isn't authoritative — a bounded queue (usually one), applied as a normal versioned move only after the opponent's move commits and only if still legal.
- **Capacity:** shard PostgreSQL by `game_id`, and budget the real **write amplification** per move (games update + move_event insert + outbox insert + idempotency upsert), not just the games update. Say capacity is **"credible pending benchmark," never "proven."**

## Data model (PostgreSQL, sharded by game_id)
```
games (authoritative, self-contained — resume needs NO replay):
  PK game_id
  version                       -- optimistic concurrency / total order
  owner_epoch                   -- fencing lives HERE, bumped on takeover; predicate is EXACT (=)
  fen, castling, ep_square, halfmove_clock, repetition_hash   -- full rules state
  active_side, active_deadline_at (timestamptz), remaining_ms_white, remaining_ms_black
  time_control_type, status

move_event (durable ordered log — powers replay + spectator catch-up):
  PK (game_id, version)         -- UNIQUE guards double-apply / double-emit
  ply, uci, san, clock_ms

outbox (same txn as the move; CDC -> Kafka; retain by CDC-ack log position):
  PK (game_id, version, event_type)   payload

idempotency (same txn as the command; survives failover):
  PK (game_id, player_id, key)  request_hash, response

game_participants (double-match guard, scoped by mode):
  UNIQUE(player_id) WHERE active

Move transaction:
  WITH upd AS (
    UPDATE games SET version=version+1, ...
     WHERE version=:expected AND owner_epoch=:mine
       AND active_side=:mover AND clock_timestamp() < active_deadline_at
    RETURNING version)
  INSERT INTO move_event/outbox/idempotency SELECT ... FROM upd;   -- gated on the update
  COMMIT;  -- THEN broadcast (broadcast is only a hint)

Deadline scanner: time-bucketed / timeout-wheel, claim due rows with FOR UPDATE SKIP LOCKED,
submit the same versioned timeout txn. Only triggers; never decides.
```

## If the interviewer pushes further
- *"Two servers both own the game — corruption?"* No — only one wins the `version` + exact-`owner_epoch` CAS; the other reloads. The DB is the referee.
- *"Why not check lease expiry on the write?"* Not needed for safety — epoch monotonicity + exact-match already fence a zombie once someone takes over; and adding it would wrongly fail a legitimate sole owner who thought for a long time. The lease only decides *when takeover is allowed*.
- *"A move and a timeout at the same instant?"* Same versioned transaction; `< deadline` vs `>= deadline` against one DB clock makes exactly one true, and the version CAS picks one winner. The scanner only triggers.
- *"Just use `now()`?"* No — `now()` is frozen at transaction start; use `clock_timestamp()` or a stalled txn can commit a move after the flag.
- *"Redis pub/sub for delivery?"* No — it drops events silently with no gap signal. Durable ordered log (Stream + `move_event`) plus a version heartbeat, and subscribe-buffer-then-snapshot-then-drain on reconnect.
- *"Player on a slow link — fair?"* Bounded, capped lag compensation from measured ping; name the policy.
- *"100k spectators?"* Separate coalesced fanout tier with edge relays, admission limits, and a replay cache outside Postgres — not the players' authoritative path.
