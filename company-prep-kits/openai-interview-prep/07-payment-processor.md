# Design a Payment Processor (Gateway)

> OpenAI Staff SWE prep (hellointerview "design a payment processor"). Validated via debate flow (Codex, high reasoning).
>
> **Excalidraw diagram (processor-in-the-middle):** https://excalidraw-production.a.musta.ch#44aa87ff-6d9b-4c03-b450-39a17d16287f

## Framing (get this right first)
**WE are the intermediary** forwarding hold/charge to **downstream** processors (Amex/Visa), batching captured charges per processor into one **10pm file → wire**. This is **NOT** a merchant wallet.

## Key validated decisions

### Storage
- **DynamoDB** primary store; conditional writes = atomic state machine + idempotency; partition by `transaction_id`.

### LSI-on-state is WRONG for the batch query
- The global "all captured" batch query can't use an LSI (an LSI shares the base PK, only re-sorts within one PK value).
- Experience #1's "I should've used an LSI" regret is a **trap**.
- Right answer = **sparse + write-sharded discovery key**.

### CRITICAL FIX from debate
- Do **NOT** use an eventually-consistent GSI as the batch's source of truth (a pre-cutoff capture settling a day late = a settlement miss).
- Use a dedicated **SettlementWork table**: PK=`SETTLE#proc#bizdate#shard`, written in the **SAME `TransactWriteItems`** as the charge, queried **strongly consistent**.

### Batch idempotency
- Separate `SETTLE_DEDUPE#` item with `attribute_not_exists` (an attribute merely *named* "idempotency_token" does **NOT** enforce uniqueness in DynamoDB).
- `batch_id = proc#bizdate` (stable, re-runnable).

### Exactly-once settlement across an external file submit is impossible purely in DDB
- → settlement **outbox**: persist token + manifest hash **BEFORE** send; on retry, query downstream by token.

### Keep a MINIMAL settlement double-entry ledger
- proc receivable / merchant payable / fees / rejects / chargebacks.
- Dropping it entirely is a ding. Dropping the merchant-**wallet** ledger is fine.

### Hold expiry
- Sweeper over a sparse `ExpiryIndex` + downstream reversal.
- **NOT** DynamoDB TTL (best-effort, delete-only, "typically within days").

### Vocabulary precision
- **CAPTURED** = recorded for clearing/presentment; **money moves at SETTLEMENT, not capture.**

### Global
- Region-pin writes; Global Tables for **DR only** (active-active LWW corrupts money state).
- PCI: P2PE + tokenization, never raw PAN.

## Alternative framing (v1)
There's also a v1 **merchant-side payment-service** framing (Aurora + double-entry ledger):
https://excalidraw-production.a.musta.ch#ebd25b12-a9be-48be-bd3b-d989c4f66668
Use this **only** if the prompt is merchant-acquirer, not processor-in-the-middle.
