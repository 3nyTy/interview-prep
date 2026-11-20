# Payment Processor (Gateway) — Detailed Explainer (plain English)

🔗 Diagram (processor-in-the-middle): https://excalidraw-production.a.musta.ch#44aa87ff-6d9b-4c03-b450-39a17d16287f
🔗 Alt v1 (merchant-side framing): https://excalidraw-production.a.musta.ch#ebd25b12-a9be-48be-bd3b-d989c4f66668

## Get the framing right first (this is half the battle)
There are two different "payment" questions, and they have different answers:
- **Merchant wallet / payment service:** you hold balances for users, like PayPal. (That's the alt v1 diagram.)
- **Payment PROCESSOR / gateway (this one):** *we are the middleman.* A merchant sends us a request to authorize and charge a card; we forward it to the real card networks (Visa, Amex). We don't hold a wallet. Once a day (say 10pm) we bundle up all the charges we captured for each network into one file and wire the money.

If you build a wallet when they asked for a processor, you've answered the wrong question. State your interpretation explicitly up front.

## The lifecycle of a payment
1. **Authorize (hold):** "Does this card have $50 and is it valid?" The bank places a *hold*. No money moves yet.
2. **Capture (charge):** "Actually take the $50." This **records** that money is owed to the merchant — but money *still* hasn't physically moved.
3. **Settlement:** at 10pm, we batch all captured charges per network into a file, submit it, and the actual money transfer happens.

The key vocabulary point an interviewer checks: **"captured" means recorded for clearing; the money actually moves at settlement, not at capture.** Mixing these up is a tell that you don't know payments.

## The core data design
- **DynamoDB** is the primary store. Each transaction is a row, partitioned by `transaction_id`.
- Each transaction is a little **state machine** (`HELD → CAPTURED → SETTLED`, plus `REVERSED / FAILED`), and every transition is a **conditional write** — which also gives us idempotency for free (a retried "capture" that's already captured is a no-op, not a double charge).

## The hardest part: "give me everything to settle tonight"
At 10pm we need: *"every charge captured today for Visa."* This sounds trivial; it's the crux of the design, and there are two traps.

### Trap 1: "I'll just add an LSI on the status field"
A Local Secondary Index shares the table's partition key and only re-sorts *within a single partition key value*. But our partition key is `transaction_id`, so an LSI can only answer "sort this one transaction's states" — useless for a *global* "all captured charges across all transactions" query. (This is a documented regret from a real interview — candidates reach for an LSI and it can't do the job. Don't fall for it.)

### Trap 2: "I'll use a GSI on status=CAPTURED as the settlement list"
A Global Secondary Index *can* span partitions — but it's **eventually consistent**. Picture a charge captured at 9:58pm. At 10:00pm the batch job queries the GSI, but that charge hasn't propagated into the index yet. **You miss it — a real payment doesn't get settled.** For money, "eventually" is a bug.

### The correct answer: a dedicated SettlementWork table
When we capture a charge, in the **same `TransactWriteItems`** (atomic) we also write a row into a **SettlementWork table**:
```
PK = SETTLE#<processor>#<bizdate>#<shard>
SK = TXN#<transaction_id>
```
At 10pm the batch job reads this table with **strong consistency**. Because the SettlementWork row was written atomically with the capture, the worklist is guaranteed complete — no late-propagation gap. The `#<shard>` suffix spreads writes so a busy day doesn't create a hot partition.

## Don't settle the same batch twice
The batch sends an external file. If the job retries (crash, timeout), we must not wire money twice.
- **Batch idempotency:** a separate dedupe item written with `attribute_not_exists` — meaning "only create this if it doesn't already exist." (Note: just *naming* an attribute "idempotency_token" does nothing; uniqueness in DynamoDB comes from the conditional `attribute_not_exists` on the key.) The `batch_id = processor#bizdate` is stable, so a rerun targets the same batch.
- **Settlement outbox:** before sending the file, we persist the batch token + a hash of the file's contents. On retry, instead of blindly resending, we *ask the network* "did you already receive token X?" True exactly-once across an external system is impossible purely inside our DB, so we lean on the downstream's idempotency keyed by our token.

## Keep a minimal ledger
Even though we're not a wallet, we keep a small **double-entry ledger** for settlement: processor receivable, merchant payable, fees, rejects, chargebacks. Dropping this entirely is a ding (you can't reconcile money without it). But we *don't* need a full per-user wallet ledger — that's the part that's out of scope for a processor.

## Holds expire
A hold isn't forever (often ~7 days). We need to release expired holds.
- **Don't use DynamoDB TTL** — it's best-effort and can lag by days ("typically within 48 hours"), which is unacceptable for money.
- Instead, a **sweeper** scans a sparse `ExpiryIndex` (only holds that haven't been captured) and issues a reversal downstream when one expires.

## Money + multiple regions
Money state cannot tolerate "last write wins" conflict resolution (two regions both editing the same balance = corruption). So:
- **Pin writes to one region** per transaction.
- Use **Global Tables for disaster recovery only**, never active-active for live money writes.
- **PCI compliance:** use point-to-point encryption and tokenization; never store raw card numbers (PANs).

## Database schema (DynamoDB)
```
TRANSACTIONS (state machine + idempotency):
  PK = TXN#<transaction_id>
  attrs: state (HELD|CAPTURED|SETTLED|REVERSED|FAILED),
         amount, processor, merchant_id, idempotency_key, hold_expiry
  → transitions = conditional writes

SETTLEMENT WORK (complete, strongly-consistent worklist for the 10pm batch):
  PK = SETTLE#<processor>#<bizdate>#<shard>     # write-sharded to avoid hot partition
  SK = TXN#<transaction_id>
  → written in the SAME TransactWriteItems as the capture
  → read STRONGLY CONSISTENT at settlement time (never an eventually-consistent GSI)

SETTLE DEDUPE (don't double-settle a batch):
  PK = SETTLE_DEDUPE#<processor>#<bizdate>
  → put with attribute_not_exists   (batch_id is stable + re-runnable)

SETTLEMENT OUTBOX:
  persist {token, manifest_hash} BEFORE sending the file;
  on retry, query downstream by token instead of resending

EXPIRY INDEX (sparse): only un-captured holds → sweeper reverses on expiry (NOT TTL)
```

## If the interviewer pushes further
- *"Why not the GSI? It's so much simpler."* Because eventual consistency means a just-captured charge can be missing from the index at query time → missed settlement. Money requires the strongly-consistent SettlementWork table written atomically with the capture.
- *"Is settlement truly exactly-once?"* No — across an external file submit it can't be, purely in our DB. We get as close as possible with the outbox (persist-before-send) + downstream idempotency keyed on our token.
- *"Why keep any ledger if you're not a wallet?"* For reconciliation and dispute/chargeback handling. Dropping the settlement ledger entirely means you can't prove what you owe whom.
