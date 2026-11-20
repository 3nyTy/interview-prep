# Crib — Stock Brokerage System (Robinhood)

1 report, late April 2026, Manager level. The only one of the three trading questions that names an
**external exchange**.

> Design a stock brokerage system that allows users to place and manage limit orders, supporting
> order validation, order lifecycle tracking, and **reliable order submission to a stock exchange**.

> ⚠️ **CORRECTED 2026-08-14 after codex review.** An earlier version of this crib claimed that FIX
> `ClOrdID` is a universal per-session-per-day idempotency key, and that after a timeout you should
> resubmit the **same** `ClOrdID` because a duplicate-reject proves the original landed. **Both claims
> are false and the second is dangerous.** The verdict was that a Staff+ candidate repeating it
> unqualified should be rejected. The corrected version is §2. This is the single highest-value fix in
> the whole prep folder — read §2 before anything else.

## 1. Summary

⭐ **You are not matching orders.** Validation, lifecycle, submission. The exchange matches. Draw an
order book and you've answered crib `02` instead of this one.

The design is one boundary: **an outbound instruction to a system you don't control and can't roll
back.** Framing line:

> *"Two authorities own different facts. I own **intent** — what the customer asked for and whether
> they're allowed. The venue owns **reality** — whether it acknowledged, filled, or cancelled. My job
> is to keep them reconciled, and to never resolve uncertainty by guessing."*

## 2. ⭐ The recovery protocol (the corrected centerpiece)

**What FIX actually gives you.** `ClOrdID` is "unique identifier of the order as assigned by the
institution." That's it. FIX does **not** universally mandate uniqueness scope, and it does **not**
define universal duplicate-detection for a repeated `NewOrderSingle`. Venue behaviour genuinely
varies: unique per account / per session / per day / per active-order set; reuse allowed after
terminal state; duplicates rejected; duplicates detected only if other fields match; or no reliable
duplicate detection at all.

**Say this:**
> *"`ClOrdID` is a business identifier whose uniqueness and duplicate-handling are defined by the
> bilateral venue specification, not by FIX. It may support idempotent recovery. FIX itself gives no
> universal exactly-once order-submit guarantee."*

**⛔ Never say** *"resubmit the same `ClOrdID`; a duplicate reject proves it landed."* A duplicate
reject proves only that the venue saw a conflicting identifier **within its own dedup scope**. The
original could have been accepted-and-filled, accepted-and-cancelled, rejected-but-still-consuming the
id, or accepted by one component and invisible to the status path. And symmetrically, a retry being
*accepted* does not prove the first was absent — unless the venue guarantees dedup, you have just
**doubled the customer's order.**

⭐ **The correct recovery, and the distinction that carries the round — two different layers:**

| Layer | Problem | Mechanism |
|---|---|---|
| **FIX session** | did the bytes get delivered? | `ResendRequest`, original `MsgSeqNum`, `PossDupFlag=Y`, `OrigSendingTime`, gap fill |
| **Business order** | did the order take effect? | **`Order Status Request` (`MsgType=H`)**, venue order lookup, **drop copy**, reconciliation |

The order of operations after a transport failure:

1. **Recover the session first.** Do not blindly send a new application message.
2. **Query business state** via the venue-supported mechanism (`MsgType=H` / drop copy).
3. **Only if the venue's implementation guide explicitly specifies same-`ClOrdID` application retry**,
   follow that documented contract.
4. **Otherwise quarantine the order as `ACK_UNKNOWN`** and resolve through status, drop copy, or ops.
5. ⛔ **Never mint a new `ClOrdID` after uncertainty** unless a status protocol has proven the original
   took no effect.

**The precise guarantees to claim** (same decomposition as `01-job-scheduler.md` §2):

- exactly-once **customer intent creation**, internally — mine, enforceable;
- **at-least-once transport attempts** — unavoidable;
- externally observable order state **eventually resolved** — via status and reconciliation;
- **no new order identifier after uncertainty** unless status proves non-effect.

⛔ Do not promise "never lose or duplicate an instruction." That's impossible without a documented
counterparty protocol providing durable dedup and queryability.

## 3. State machine — transport state ≠ business state

⛔ `SUBMITTED = "write succeeded on the wire"` is meaningless. A successful socket write means bytes
entered a **local kernel buffer**. And even a venue `New` execution report doesn't mean the order is
still live — later reports may already have filled or cancelled it.

```
TRANSPORT:  OUTBOXED -> SENT -> ACK_UNKNOWN ─┐
                                             ├─> (resolved by status/drop-copy)
BUSINESS:   VENUE_ACKED -> LIVE -> PARTIALLY_FILLED -> FILLED
                                \-> CANCELED / REJECTED / EXPIRED
```

Two columns, tracked separately, reconciled continuously. `ACK_UNKNOWN` is a first-class state that
only a **query** clears — never a timer, never an assumption.

**Cancel is a request you can lose.** A cancel is its own message with its own `ClOrdID` referencing
`OrigClOrdID`. Local state is `PENDING_CANCEL`; only the venue's acknowledgement moves it to
`CANCELED`. A cancel can be rejected because the order already filled. A UI that shows "cancelled" on
button-press is lying — brokerages get sued over this. Cancel/replace adds more outcomes: replace
accepted, replace rejected, or **replace accepted after a partial fill**, which changes the remaining
quantity mid-flight.

## 4. Fills — `CumQty`/`LeavesQty`, not sequence numbers

⛔ **"Order execution reports by exchange sequence number" is wrong.** FIX sequence numbers are scoped
to a **session**, and the order-entry session and drop-copy session have **different sequence spaces**.
Reports can arrive through multiple channels.

⛔ And you cannot discard anything with a lower sequence: **trade busts, corrections and restatements
legitimately change previously recorded facts.**

✅ Instead:
- Process `ExecType`, `OrdStatus`, **`CumQty` and `LeavesQty`** — FIX carries cumulative state
  precisely so you don't have to infer it from arrival order.
- Dedup on a **source-scoped** key: `exec_id` is not necessarily globally unique — scope it by
  `(source, venue, trade_date[, session])`.
- Support **trade cancel/correct** semantics explicitly.
- ⭐ **Persist raw inbound messages** immutably. Derived state can be rebuilt; a discarded message
  cannot.

## 5. Money

Reserve buying power at placement, settle at the fill price, release on terminal state — and put the
check **in the UPDATE predicate**, not in application code:

```sql
BEGIN;
  UPDATE accounts SET cash_reserved_minor = cash_reserved_minor + :cost
   WHERE account_id = :a AND cash_total_minor - cash_reserved_minor >= :cost;  -- 0 rows => reject
  INSERT INTO orders (... state='OUTBOXED', clord_id=:clord ...);
  INSERT INTO outbox (...);
COMMIT;
```

⚠️ Release must be **idempotent by construction** — release keyed on the terminal transition, not a
counted decrement, or a replayed terminal report releases twice. A reservation that leaks silently
freezes customer cash: a support ticket, not a crash, which makes it worse.
Money and shares as scaled integers (`long` minor units / micro-shares), never `double` — see
`coding/FractionalShares.java` for the correct *reason*.

Also underspecified and worth naming: cash vs **margin** buying power, unsettled funds under **T+1**,
and short sales (which need a **locate**, not just cash).

## 6. Validation — a heuristic, not a law

Cheapest-first is a good default, **not** a rule: syntax → symbol tradable / not halted → session →
price collar → buying power → risk & compliance. ⚠️ But some checks are **mandatory regardless of
cost** — Rule 15c3-5 market-access controls must run pre-trade even if expensive, and a Reg SHO locate
must precede a short sale. Say "cheapest-first unless a control is regulatorily required earlier."

## 7. Reconciliation and the regulatory surface

⛔ **"EOD reconciliation" is too late** for a system moving money intraday. Continuous **drop-copy**
comparison plus periodic intraday reconciliation, with EOD as the backstop.
⛔ **"Every divergence has a regulatory clock" is too vague** — different breaks carry different
reporting, correction, retention and supervisory obligations.

For a US equities broker-dealer, name these:
- **SEC 17a-3 / 17a-4** — books and records, immutable audit trail, retention
- **CAT** — order lifecycle reporting. ⚠️ **Say CAT, not "OATS/CAT". OATS was retired and replaced by
  CAT** — pairing them signals stale knowledge in a 2026 interview.
- **Rule 15c3-5** — market-access pre-trade risk controls
- **Reg SHO** — locate, order marking, close-out
- **Best execution** and order handling
- **T+1** allocation, affirmation, settlement, fails

## 8. Schema

```
orders      order_id PK
            client_order_id UNIQUE NOT NULL   -- customer idempotency; scope + bind to payload digest
            clord_id UNIQUE NOT NULL          -- venue-facing, per the VENUE's uniqueness scope
            orig_clord_id                     -- cancel/replace chains
            account_id, symbol, side, type=LIMIT
            limit_price_minor, qty_micro
            cum_qty_micro, leaves_qty_micro, avg_fill_price_minor   -- from FIX, not inferred
            transport_state, business_state    -- ⭐ two columns
            reserved_cash_minor / reserved_qty_micro
            time_in_force, expires_at, version

executions  PK (source, venue, trade_date, exec_id)   -- ⭐ source-scoped, not bare exec_id
            order_id, exec_type, ord_status, last_px_minor, last_qty_micro,
            cum_qty_micro, leaves_qty_micro, corrects_exec_id NULL   -- bust/correct support

raw_messages  seq PK, session_id, direction, msg_type, payload, received_at   -- ⭐ immutable
outbox        id PK, dedupe_id UNIQUE NOT NULL, order_id, clord_id, msg_type, payload, sent_at NULL
recon_breaks  break_id, as_of, order_id, our_state, their_state, source, resolved_at, resolution
```

⚠️ The outbox gives at-least-once **broker → FIX adapter**. It does **not** give exactly-once
**adapter → exchange**. The adapter can write to the socket and die before recording `sent_at`.

## 9. Probes

**"The exchange never responds."** → Session recovery first (`ResendRequest`). Then `Order Status
Request` or drop copy to establish business state. The order sits in `ACK_UNKNOWN` until a query
resolves it — I don't retry blind and I don't mint a new id. If the venue documents same-`ClOrdID`
retry, I follow that contract; otherwise it's quarantined for ops.

**"Customer cancels as it fills."** → The venue decides. `PENDING_CANCEL` locally; if the cancel is
rejected because it filled, the order is `FILLED` and the customer is told the cancel was too late.
Optimistically releasing the reservation on cancel-request would force a re-debit.

**"Duplicate execution report."** → Ignored **if** it's genuinely a duplicate on the source-scoped
key. ⚠️ But a report with the same id and *different* content is a **correction**, not a duplicate —
those must be applied, and that's why `CumQty`/`LeavesQty` and `corrects_exec_id` exist.

**"How do you know your state is right?"** → I don't, alone. Continuous drop copy, intraday
reconciliation, EOD three-way against venue and clearing firm, and every break has a named owner.

## Traps

- ⛔ Building a matching engine.
- ⛔ "`ClOrdID` is a FIX-standard idempotency key" / "duplicate reject proves it landed."
- ⛔ Minting a new `ClOrdID` after uncertainty.
- ⛔ Treating a socket write as an acknowledgement, or a timeout as a rejection.
- ⛔ Ordering fills by sequence number; discarding lower sequences; bare `exec_id` as a global key.
- ⛔ Marking cancelled on the customer's click.
- ⛔ Promising exactly-once external submission.
- ⛔ "OATS/CAT". Say **CAT**.
- ⛔ EOD-only reconciliation.
- ⛔ `double` for money or shares.

## Reuse

`project_webhook_delivery_design` (outbox + the 3-ID model = `client_order_id` / `order_id` /
`clord_id`), `project_sora_scheduling_design` (exactly-one side effect),
`project_payment_processor_design` (hold → charge → release),
`cribs/01-job-scheduler.md` §2 (the identical guarantee decomposition — the effect is deduplicated
where the effect happens, or it isn't deduplicated at all).
