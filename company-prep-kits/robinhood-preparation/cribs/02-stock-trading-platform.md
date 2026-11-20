# Crib — Stock Trading Platform (Robinhood)

**13 reports, second-highest in the Hello Interview set.** Mid-June 2026. Mid-level–Staff, Manager–Senior Manager.

> Design a simplified stock trading platform that supports limit orders for buying and selling stocks,
> with basic portfolio tracking capabilities at **small scale (100 writes per second)**.

> ⚠️ **REVISED 2026-08-14 after codex review**, which found nine critical flaws in the first version.
> The headline ones: the reservation was racy, `price_minor × qty_micro` produces **micro-cents not
> cents**, the in-memory book is **not** transactional with the database, and "settle at fill" is the
> wrong domain statement — **execution and settlement are different events.**

## 1. Summary

⭐ **100 writes/sec is the trick.** The prompt deletes scale on purpose. Arrive with shards, regions and
Kafka and you've failed before starting. The round is about **correctness of money and order state**.

Opening line:
> *"At 100 writes a second I get a choice most trading systems don't — everything that must be atomic
> can be one database transaction. I'll use that, and flag where it breaks if you raise the scale."*

⚠️ But do **not** let right-sizing become an excuse for skipping correctness. Low scale removes the
*need for sharding*; it removes none of the races below.

## 2. Money correctness — the nine things that actually get graded

**① Reservation must be a conditional UPDATE with an affected-row check.** "Reserve at placement" is
not an implementation — two concurrent placements both read the same available cash otherwise.

```sql
UPDATE accounts SET cash_reserved_minor = cash_reserved_minor + :hold
 WHERE account_id = :id
   AND cash_total_minor - cash_reserved_minor >= :hold;   -- ⭐ guard IN the predicate
-- 0 rows affected => insufficient buying power => reject
```
A `version` column is useless unless a transaction actually uses it. Either lock the row or put the
condition in the `WHERE`.

**② The sell side over-commits too, and it's the interleaving people miss.** User owns 10 shares. Two
sell orders for 10 arrive concurrently; both see 10 available; both go open; both fill; the position
goes negative. So hold **shares** as well as cash:
`available_shares = total_shares − reserved_shares >= requested`.

**③ ⭐ The arithmetic is wrong if you multiply naively.** `price_minor` (cents) × `qty_micro`
(10⁻⁶ shares) = **micro-cents**, not cents. $150.01 × 0.000001 shares is not an integer number of
cents. So you must state:
- instrument price and quantity precision;
- a **rounding policy** and where it applies (per fill, or only at order aggregate);
- that the reservation rounds **up** (conservative ceiling) and includes fees, so the sum of rounded
  fills can never exceed the hold;
- `NUMERIC`, or checked wider arithmetic, because `bigint × bigint` can overflow.

**④ The fill must decrement the ORDER-level reservation, not just the account totals.** Otherwise
after a partial fill the order row still claims its original hold, and cancellation and reconciliation
are both wrong. Consume exactly what filled, retain the remainder, release the remainder on
cancel/expiry.

**⑤ `fills` needs an idempotency key.** A deadlock, timeout, or crash-after-commit lets a retry insert
a second fill and apply balances twice. Deterministic key, e.g.
`(symbol_epoch, match_sequence, execution_index)`, with a unique constraint, and the whole fill
transaction retry-safe.

**⑥ The fill UPDATE must verify the match is legal.** `WHERE order_id IN (:buy, :sell)` checks nothing.
Lock both orders and validate: both exist, opposite sides, same symbol, still matchable, and
`filled_qty + q <= qty`. Require **exactly two rows updated**. A `CHECK` constraint aborts a bad match
after the fact; it does not make matching correct.

**⑦ ⭐ One ACID transaction does NOT make the in-memory book transactional.** Commit succeeds, process
dies before removing the quantity from the book → the book holds **stale liquidity** and will match it
again. Book changes before commit and commit fails → valid liquidity vanishes.
→ Mutate the book **only after commit**, make the DB path idempotent (⑤), reconcile failed attempts,
and **rebuild the symbol's book from open orders before resuming**. If recovery guarantees matter, use
a durable per-symbol command log.

**⑧ "Exact-epoch fencing" must be persisted, not process-local.** A local epoch check cannot stop a
stale owner from committing after its lease expired — that's split brain with two matchers on one
symbol. Persist the ownership epoch and include it in **every** mutating statement so the database
rejects stale writes. (Identical lesson to `01-job-scheduler.md` §5.)

**⑨ ⭐ This is not a brokerage cash model, and execution ≠ settlement.** `cash_total` /
`cash_reserved` don't distinguish settled cash, unsettled sale proceeds, pending deposits, margin
buying power, or withdrawals. Model at minimum: **trade-date cash, settled cash, holds, unsettled
receivables/payables**, and a buying-power policy. Update trade-date positions immediately if you
like, but **cash and shares settle through a separate T+1 settlement process.** ⛔ Never say "settle at
fill."

## 3. Matching

- **Price-time priority = `TreeMap<price, ArrayDeque<order>>`.** Bids descend, asks ascend. Match while
  `bestBid >= bestAsk`. The FIFO at each price level *is* the time priority. Trade price convention:
  the **resting** order's price.
- **One writer per symbol**, or two matches double-fill one resting order. Shard by `symbol`, fenced
  per ⑧. At 100 wps this is one process — say it's sharded by symbol so the scaling story exists.
- ⚠️ **Two shard keys are in tension**: matching wants a single writer per **symbol**; buying power
  wants serialisation per **account**. A fill touches one symbol and *two* accounts, so no single key
  covers it. At 100 wps: one ACID transaction across both accounts. At real scale: a saga, or
  account-level reservation outside the matching path. **Name which key you're sharding on and why.**
  ⚠️ Lock the two account rows in a **deterministic order** (e.g. ascending `account_id`) or two
  opposing fills deadlock.
- **The book is a derived index.** Durable order rows are the truth; the book is rebuilt on restart.
- **Partial fills:** track `filled_qty` and `avg_fill_price`; `PARTIALLY_FILLED` until
  `filled_qty == qty`. Every fill is an immutable row.
- **Cost basis:** on buy, `avg_cost = (avg_cost·qty + price·fillQty)/(qty+fillQty)`. On sell, quantity
  drops and **`avg_cost` is unchanged** — realised P&L is booked instead.
- **Self-trade prevention**, **market hours / DAY vs GTC**, and **idempotent placement** via a unique
  `client_order_id`.

## 4. Schema

```
accounts        account_id PK
                cash_trade_date_minor, cash_settled_minor,      -- ⑨ settlement is separate
                cash_reserved_minor, unsettled_receivable_minor,
                version int

positions       (account_id, symbol) PK
                qty_micro, reserved_qty_micro,                   -- ② share holds
                avg_cost_minor, version

orders          order_id PK, client_order_id UNIQUE NOT NULL     -- idempotent placement
                account_id, symbol, side, type=LIMIT
                limit_price_minor, qty_micro, filled_qty_micro, avg_fill_price_minor
                reserved_cash_minor, reserved_qty_micro          -- ④ decremented per fill
                status, time_in_force, expires_at, seq bigserial -- seq = time priority
                CHECK (filled_qty_micro <= qty_micro)
                INDEX (symbol, side, limit_price_minor, seq) WHERE status IN ('OPEN','PARTIALLY_FILLED')

fills           fill_id PK
                UNIQUE (symbol_epoch, match_sequence, execution_index)   -- ⑤ idempotency
                buy_order_id, sell_order_id, symbol,
                price_minor, qty_micro, gross_amount_minor, executed_at

symbol_owner    symbol PK, owner, epoch bigint, lease_expires_at  -- ⑧ persisted fencing
```

**The fill transaction, corrected:**

```sql
BEGIN;
  -- ⑧ fencing: reject a stale matcher outright
  UPDATE symbol_owner SET lease_expires_at = :t + interval '10 s'
   WHERE symbol = :sym AND owner = :me AND epoch = :myEpoch;   -- 0 rows => I am fenced, ABORT

  -- ⑤ idempotent insert; a retry is a no-op
  INSERT INTO fills (...) ON CONFLICT (symbol_epoch, match_sequence, execution_index) DO NOTHING;

  -- ⑥ validate the match, don't assume it
  SELECT * FROM orders WHERE order_id IN (:buy,:sell) FOR UPDATE;   -- deterministic order
  --   assert opposite sides, same symbol, both matchable, remaining >= :q

  UPDATE orders SET filled_qty_micro = filled_qty_micro + :q,
                    reserved_cash_minor = reserved_cash_minor - :consumedHold,  -- ④
                    status = CASE WHEN filled_qty_micro + :q = qty_micro THEN 'FILLED'
                                 ELSE 'PARTIALLY_FILLED' END
   WHERE order_id = :buy AND filled_qty_micro + :q <= qty_micro;    -- expect exactly 1 row
  --   ... same for :sell

  -- ③ rounding policy applied here, once, conservatively
  -- ⑨ trade-date cash moves now; SETTLED cash moves in the T+1 settlement run
  UPDATE accounts ...;    UPDATE positions ...;
COMMIT;
-- ⑦ only NOW mutate the in-memory book
```

## 5. Probes

**"Two users buy the last 10 shares at once."** → Serialised by the per-symbol writer; the first
consumes the resting quantity, the second partially fills or rests. The invariant
`filled_qty <= qty` is enforced in the UPDATE predicate, not just as a CHECK.

**"Cancel races a fill."** → `OPEN → CANCEL_REQUESTED → CANCELLED`; the match path's conditional update
requires `status IN ('OPEN','PARTIALLY_FILLED')`. A losing cancel is reported "already filled" — what a
real broker does. Release the remaining hold on the terminal transition, keyed not counted.

**"Matcher process dies mid-match."** → ⑦: the DB is the truth, the book is rebuilt from open orders,
and the fill insert is idempotent so the retry is safe. The old matcher is fenced by ⑧.

**"Portfolio value?"** → Positions from `positions`, prices from the market-data cache (crib `03`).
Never inside the fill transaction.

**"Now 100k writes/sec."** → Partition by symbol, move buying power to a per-account reservation
service, settlement async via outbox; eventual consistency on *display*, never on cash.

## Traps

- ⛔ Over-engineering. 100 wps.
- ⛔ Check-then-debit for buying power; forgetting **share** holds.
- ⛔ `price_minor × qty_micro` treated as cents.
- ⛔ "Settle at fill." Execution ≠ settlement.
- ⛔ `fills` with no idempotency key.
- ⛔ Trusting `order_id IN (...)` as a match validation.
- ⛔ Assuming one DB transaction makes the in-memory book atomic.
- ⛔ Process-local epoch "fencing".
- ⛔ Locking two accounts in non-deterministic order.
- ⛔ `double` for money or shares (see `coding/FractionalShares.java` for the right *reason*).
- ⛔ Changing `avg_cost` on a sell.
- ⛔ Matching *and* routing to an external exchange — that's crib `04`.

## Reuse

`project_payment_processor_design` (hold → charge → release), `project_chess_design` (single-writer
sequencer, exact-epoch fencing), `project_url_shortener_design` (right-sizing),
`cribs/04-stock-brokerage.md` (the external-venue variant).
