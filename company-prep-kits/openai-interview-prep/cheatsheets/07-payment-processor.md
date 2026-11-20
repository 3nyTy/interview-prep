# Cheat Sheet — Payment Processor (Gateway)

🔗 Diagram (processor-in-the-middle): https://excalidraw-production.a.musta.ch#44aa87ff-6d9b-4c03-b450-39a17d16287f

## 1. Summary (in plain terms)
**We sit in the middle.** A merchant asks us to authorize ("hold") and later capture ("charge") a card; we forward that to the real card networks (Visa/Amex). Then once a day at 10pm we bundle all the captured charges for each network into one file and wire the money. We are **not** a wallet holding balances.

> Get the framing right first: "processor in the middle," not "merchant wallet." There's a separate v1 diagram for the merchant-side framing if the prompt is actually about that.

## 2. Key deep dives (plain terms)

- **State machine in DynamoDB, partitioned by transaction.** Each payment moves hold → captured → settled via conditional writes (which also give idempotency). Money words matter: **"captured" = recorded; money actually moves at "settlement."**

- **The batch query is the tricky part — and an LSI does NOT solve it.** You need "give me every captured charge for Visa today." An LSI can't do that (it only re-sorts *within a single partition key*). The "I should've used an LSI" instinct is a **trap**.

- **Don't trust an eventually-consistent index as the batch's truth.** A charge that settles into the index a moment late = a *missed payment*. Instead, write a row into a dedicated **SettlementWork table in the same transaction** as the capture, and read it with **strong consistency**. That's a guaranteed, complete worklist.

- **Settlement file = exactly-once is impossible inside the DB alone** (you're sending an external file). Use a **settlement outbox**: persist the batch token + file hash *before* sending; on retry, ask the network "did you get token X?" instead of blindly resending.

- **Batch idempotency:** a separate dedupe item with `attribute_not_exists` (not just an attribute *named* "idempotency_token" — naming it doesn't enforce uniqueness). `batch_id = processor#businessdate` so reruns are safe.

- **Keep a minimal double-entry ledger** (receivables/payables/fees/rejects/chargebacks). Dropping it entirely is a ding; dropping the *wallet* ledger is fine.

- **Hold expiry via a sweeper** over a sparse index + a reversal downstream — *not* DynamoDB TTL (TTL is best-effort and can lag days).

- **Money + multi-region:** pin writes to one region; use Global Tables for disaster recovery only. Active-active "last write wins" would corrupt money state. PCI: tokenize, never store raw card numbers.

## 3. Database schema (DynamoDB)
```
TRANSACTIONS (state machine + idempotency):
  PK = TXN#<transaction_id>
  attrs: state (HELD|CAPTURED|SETTLED|REVERSED|FAILED),
         amount, processor, merchant_id, idempotency_key, ...
  → all transitions = conditional writes

SETTLEMENT WORK (the complete, strongly-consistent batch worklist):
  PK = SETTLE#<processor>#<bizdate>#<shard>     # write-sharded
  SK = TXN#<transaction_id>
  → written in the SAME TransactWriteItems as the capture
  → queried STRONGLY CONSISTENT at 10pm (NOT via an eventually-consistent GSI)

SETTLE DEDUPE (batch idempotency):
  PK = SETTLE_DEDUPE#<processor>#<bizdate>
  → put with attribute_not_exists  (batch_id = processor#bizdate, re-runnable)

SETTLEMENT OUTBOX (external send exactly-once-ish):
  token + manifest_hash persisted BEFORE sending the file;
  on retry, query downstream by token instead of resending

EXPIRY INDEX (sparse) → sweeper reverses expired holds (NOT TTL)
```
