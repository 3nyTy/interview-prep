# The trading cluster — three system design questions

Three of the five Hello Interview system design questions for Robinhood are trading systems.
The 1point3acres bank contains none, so this is the largest genuine gap in the prep.

All three detail pages captured 2026-08-14. Only the trading platform carries an editorial
breakdown; the other two have prompt, timeline, and nothing else. For those two the
**candidate notes in the timeline are the real content** — they carry constraints the one-line
prompt does not.

**The three scales are different. Do not reuse one sizing.** The trading platform says 100
writes per second. The brokerage says 2M DAUs. Price ingestion says two TCP connections.

---

## 1. Design a stock trading platform

- Source: https://www.hellointerview.com/community/questions/stock-trading-platform/cmbzeshg200003p6wbg45hbsi?company=Robinhood
- Reports: **13** — second-highest in the set — last asked Mid June, 2026
- Levels reported: Mid-level–Staff, and Manager–Senior Manager
- Also asked at: stake, Databricks, Ethos, and 3 more
- Has a Hello Interview answer key: https://www.hellointerview.com/learn/system-design/problem-breakdowns/robinhood
- 10 community solutions

### Prompt as published

> Design a simplified stock trading platform that supports limit orders for buying and selling
> stocks, with basic portfolio tracking capabilities at small scale (100 writes per second).

Framing from the page: users submit limit orders with price and quantity, the system matches
compatible orders, settles the trade, and updates the portfolio and order status. **The system
matches orders itself** — unlike the brokerage question below.

What it tests, per the page: designing for correctness under contention, preserving financial
consistency with ACID guarantees, and still giving real-time feedback. Even at ~100 writes per
second, the order book and portfolio ledger need care over transactions, idempotency, ordering,
and failure handling. The stated goal is to **right-size**: simple, reliable, auditable, with a
clear order lifecycle and low-latency updates.

### Common functional requirements

- Users can place limit buy and sell orders for specific symbols, prices, and quantities.
- Users can view order status (open, partially filled, filled, canceled) and execution history.
- Users can view a basic portfolio: cash balance, positions, and recent activity.
- Users receive near real-time updates when orders execute and when balances change.

### Common deep dives

1. How will your matching engine ensure correctness (**price–time priority**), avoid race
   conditions, and stay responsive under contention at ~100 writes/sec?
2. How do you make trade execution and portfolio updates atomic and resilient to failures?
3. How will clients receive near real-time order status and portfolio updates without
   overloading the system?
4. How do you enforce **pre-trade risk checks** so users cannot overspend cash or oversell
   shares, especially with concurrent orders?

### Patterns and technologies named on the page

Patterns:

- **Dealing with contention** — an order book concentrates writes on a few hot symbols and
  price levels. A deterministic, lock-efficient matching engine, **often single-threaded per
  symbol**, plus sequence numbers, avoids races and ensures fairness.
- **Real-time updates** — push over WebSockets or SSE, driven by internal pub/sub, keeps
  latency low and decouples the hot matching path from fan-out.
- **Multi-step processes** — placement → reservation → matching → execution → settlement →
  notification is a workflow. An order state machine plus transactions plus an outbox
  implements a reliable saga that recovers without breaking financial invariants.

Technologies:

- **PostgreSQL** — ACID transactions, rich constraints, serializable isolation. Suits the
  portfolio ledger, reservations, order states, and audit trail at ~100 writes/sec, and
  enforces invariants with unique and check constraints.
- **Redis** — in-memory order book, fast best bid/ask lookups, lightweight locking. Pub/Sub or
  streams drive the WebSocket tier without touching the primary database per update.
- **Kafka** — durable append-only log for executions and state changes. Enables outbox
  consumption, replay, and audit, and separates matching from downstream consumers.

Similar problems the page points at: Payment System (atomic money movement, idempotency,
immutable audit trail), Online Auction (concurrent bids, ordering rules, deterministic
matching), FB Live Comments (real-time fan-out isolated from the write path).

### Red flags to avoid

- **Splitting trade execution and portfolio updates across services with no transactional
  boundary.** Eventual consistency here creates money or shares out of thin air after failures
  or retries. You need one atomic commit, or an outbox applied idempotently.
- **Naive matching with ad-hoc database queries and row-level locks.** Scanning and updating the
  book in SQL under contention invites deadlocks, races, and crossed markets. A deterministic
  in-memory engine per symbol with an append-only log is simpler and safer at this scale.
- **Overengineering for scale at 100 writes/sec** — microservices, sharding, distributed
  consensus. It adds latency and failure modes for no benefit. Expected instead: a clear
  single-database core, simple per-symbol partitioning, asynchronous edges.

### Report timeline (Robinhood)

| When | Level | Candidate note |
|---|---|---|
| Mid June, 2026 | Manager | "I was asked to come up with **the write portion** of how do you buy and sell limit orders." |
| Late May, 2026 | Staff | — |
| Mid May, 2026 | Staff | "initial screening" |

10 more reports need a click. Note the Mid May entry: this question is used as a **phone
screen** at Staff, not only an onsite round.

---

## 2. Design a Live Stock Price Ingestion System

- Source: https://www.hellointerview.com/community/questions/stock-price-ingestion/cmmz3vla91fgx08adslyagdpy?company=Robinhood
- Reports: 2 — last asked Late May, 2026
- Levels reported: **Staff**, Manager
- Also asked at: Bloomberg
- No editorial breakdown and no community solutions

### Prompt as published

> Design a live stock price ingestion system that allows users to view real-time stock prices
> and historical price graphs for any given stock symbol.

### The real prompt — from the Staff candidate note, Late May 2026

> Designs a stock feed system that has **2 available TCP connections to stock exchanges**. Data
> must flow to a stock price service that queries it. Must support **1D, 1W, 1M rolling
> interval**. Must also support live data. The stock exchange sends feed data.

The Manager report, Mid March 2026, adds only: "Design a live stock price ingestion system both
for live price and the historical graph for a symbol".

**Two TCP connections is the constraint that defines the question**, and the published prompt
hides it. The exchange pushes; you do not poll. You have two sockets for the whole universe of
symbols, so the design questions are:

- How do you multiplex thousands of symbols over two connections, and how do you divide them —
  by symbol hash, by volume, active/standby?
- What happens when one connection drops? Failover, gap detection via sequence numbers, and
  recovery of the ticks you missed while disconnected.
- Where does the fan-out happen? The two sockets terminate on a small number of processes, so an
  internal pub/sub layer must exist between ingest and the client tier. Ingest cannot fan out to
  clients directly.
- **1D, 1W, 1M rolling intervals** are the storage spec. These are fixed windows, so
  pre-aggregate per interval rather than scanning raw ticks per request.

---

## 3. Design a Stock Brokerage System

- Source: https://www.hellointerview.com/community/questions/stock-brokerage-system/cmnw96bap0a1j08ad8ef63blw?company=Robinhood
- Reports: 1 — last asked Early April, 2026
- Level reported: Manager
- Also asked at: nowhere else — Robinhood-only
- No editorial breakdown. 3 community solutions, one filed as "Crypto Exchange Order System"

### Prompt as published

> Design a stock brokerage system that allows users to place and manage limit orders, supporting
> order validation, order lifecycle tracking, and reliable order submission to a stock exchange.

### The real prompt — from the candidate note, Early April 2026

> Design a stock broker that only handles limit orders. Focus on **2M DAUs** and **write path to
> exchange**. How would you **validate the users should be allowed the transactions**. **The
> order might take days to fulfill**, how is that handled. How are you **storing the data in the
> DB and why. Which DB**

Four things the one-line prompt omits:

1. **2M DAUs.** This is not the 100-writes-per-second question. Size it.
2. **The write path to the exchange is the focus.** Matching is out of scope — an external
   exchange fills the order. The interesting boundary is an outbound submission to a system you
   do not control, which must happen exactly one time.
3. **Orders can stay open for days.** Good-till-cancelled changes the storage shape: open orders
   are a small hot working set queried constantly, filled and cancelled orders are a large cold
   append-only history. Do not put them in one hot table and hope.
4. **"Which DB and why" is asked explicitly.** Have the answer ready and justify it on
   invariants, not familiarity.

---

## How the three differ

| | Trading platform | Price ingestion | Brokerage |
|---|---|---|---|
| Core object | order + portfolio | tick + time series | order + exchange submission |
| Stated scale | **100 writes/sec** | **2 TCP connections** | **2M DAUs** |
| Direction | read/write balanced | read-heavy fan-out | write-heavy, outbound |
| Matching | **in scope**, simplified | none | **out of scope** — external exchange |
| Hard part | money correctness under contention | constrained ingest, then fan-out | exactly-one submission, long-lived orders |
| Editorial breakdown | yes, plus answer key | no | no |

Prepare them as one topic with three endings. The shared core is an order state machine plus a
position ledger. Then add matching, or constrained fan-out, or the exchange boundary.

## Prep notes (mine, not the source's)

**Never store money or share counts as `double`.** Use scaled integers as `long`, or `BigDecimal`.
Say it out loud in a trading design; it is cheap credibility.

⚠️ **CORRECTED 2026-08-14.** An earlier version of this line (and of
`../03-trading-domain-and-concurrency.md`) claimed `1.5 - 0.4` gives `1.0999999999999999` and so
fails the sample test. **That is false.** `1.5 - 0.4` is bit-identical to the literal `1.1`, so the
sample *passes* under `double`. The correct argument is **accumulated** error over the problem's
stated 10^5 instructions — `BUY 0.1, BUY 0.2, SELL 0.3` leaves `5.55e-17` instead of flat, and at
100k mixed-quantity instructions every ticker ends non-zero under `double` versus none under scaled
`long`. Verified in `../coding/FractionalShares.java`; run it to see both.

**Reuse for money correctness.** `memory/project_payment_processor_design.md` records the
hold-then-charge-then-batch model, and the page itself names Payment System as the closest
problem. A limit order is the same shape: reserve buying power on placement, settle on fill,
release on cancel or expiry. That reservation *is* the answer to deep dive 4 and to the
brokerage's "validate the users should be allowed the transactions".

**Reuse for the single writer.** `memory/project_chess_design.md` records a single-writer
sequencer per `game_id` with fencing on an exact epoch match. The page independently arrives at
the same answer — "often single-threaded per symbol" plus sequence numbers. Per `symbol` gives
deterministic price–time priority; per `account_id` serialises buying-power changes. Name which
one you shard on and why.

**Reuse for the exchange boundary.** Reliable submission is the outbox pattern from
`memory/project_webhook_delivery_design.md` and `memory/project_sora_scheduling_design.md` —
exactly-one *side effect*, an idempotency key the exchange honours, and reconciliation for a
reply that never arrives. The days-to-fulfill requirement makes reconciliation mandatory, not
optional.

**For price ingestion**, history is the pre-materialised read pattern from
`~/reddit-interview-prep/hellointerview/design_hot_posts_page.md`: downsampled rollups per
interval, never a scan of raw ticks. For the live path, conflate — send the latest price, not
every tick, when a client falls behind.

**Market hours and corporate actions** separate a candidate who has thought about brokerages
from one who has not. Orders queue outside trading hours. Splits and dividends rewrite
historical prices and position quantities, so a price series needs an adjustment factor rather
than a rewrite in place.

## Genuinely missing from existing prep

No validated design covers: limit order book structure with price–time priority; partial fills
against one order; buying-power reservation under concurrent orders; multiplexing many symbols
over a fixed small number of exchange connections, with gap detection and failover; tick storage
with downsampled rollups per fixed interval; WebSocket fan-out with conflation and backpressure.

## Status

All three prompts, both candidate notes, and the trading platform's full breakdown captured.
No design written. The order book and the two-connection ingest are the two pieces with no
existing counterpart.
