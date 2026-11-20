# Mock Scorecard — Q7: Payment Processor (gateway)

**Date:** 2026-07-15 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/07-payment-processor.md`](../detailed/07-payment-processor.md)

**Verdict:** Strongest domain round. Correct framing, and I *built* the settlement worklist + claim/lease correctly instead of needing it taught. Fencing and persist-before-send are now automatic. The remaining edge is the **completing half of the outbox**: across an external system, exactly-once = *downstream idempotency on a stable token*, not an oncall alarm — plus a reclaim-status bug that quietly defeats the lease.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **Framing** — chose processor/gateway (middleman → card networks, no wallet), not a balance service. This is half the battle.
2. ✅ **Lifecycle + state machine** — thorough (`INITIATED → AUTHORIZED → CHARGED → SETTLEMENT_IN_PROGRESS → SETTLED`, plus failure/release states). *Vocabulary to keep crisp:* money moves at **settlement**, not capture; **capture is merchant-initiated**, not vendor.
3. ✅ **Data model** — token_id, idempotency_key (both unique-indexed), amounts, vendor, timestamps, lease fields. *Add:* on an idempotency-key **retry, return the original response** (don't error).
4. ✅ **Settlement worklist completeness** — used a **strongly-consistent** query on status. *Name the trap avoided:* a DynamoDB **GSI on `status=CHARGED` is eventually consistent** → a 9:58pm capture can be missing at 10:00pm → **missed settlement**. (An **LSI** can't help either — it only re-sorts within one partition key.) The clean DynamoDB alternative is a **SettlementWork row written in the same `TransactWriteItems` as the capture**, read strongly consistent.
5. ✅ **Claim-with-lease batch job** — transferred CI/CD fencing (`current_settlement_process_id` + `settlement_attempt_release_ts`).
6. ❌ **Reclaim bug** — *fix:* the reclaim query filtered `status IN (CHARGED, SETTLEMENT_FAILED)`, so a job that crashes *after* claiming leaves rows stuck in `SETTLEMENT_IN_PROGRESS` that the lease can **never** re-pick-up. Must include **`SETTLEMENT_IN_PROGRESS AND now() > release_ts`**. (Also: the "get claimed rows" query should select by `process_id = run_id`, not `status != SETTLEMENT_IN_PROGRESS` — which returns nothing.)
7. ⚠️ **Persist-intent-before-send** — I introduced `READY_TO_SEND` + a finalization_ts before sending. *That's the outbox first-half — correct.*
8. ❌ **Exactly-once settlement across the external file** — *fix:* you **cannot** wrap an external send in a DB txn. Give the file a **stable `batch_id`** (e.g., `processor#bizdate#shard`) + persist a **manifest hash** before sending; on retry either **resend the same token** (network dedupes) or **query the network "did you receive `batch_id` X?"**. Exactly-once purely in your DB is impossible across an external system → lean on the **downstream's idempotency keyed on your token.** I reached for "alarm oncall," which detects but doesn't resolve and doesn't scale — make it a last-resort backstop only.
9. ⚠️ **Holds expire** — *fix:* a **sweeper over a sparse index** of un-captured holds, **not DynamoDB TTL** (best-effort, can lag days — unacceptable for money).
10. ⚠️ **Ledger / reconciliation** — I listed it. *Keep:* a minimal **double-entry** ledger (receivable / payable / fees / chargebacks) for reconciliation — dropping it entirely is a ding.
11. ⚠️ **Multi-region + PCI** — pin money writes to **one region**; Global Tables **DR-only** (LWW corrupts money). Tokenize; never store the raw PAN.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Settlement worklist must be strongly consistent and written atomically with the capture — an eventually-consistent index silently misses a charge captured just before the batch runs."*
- *"You can't make an external file-send atomic with a DB write; you get exactly-once by giving the batch a stable token the network dedupes on, and querying by token on retry."*
- *"Money moves at settlement, not capture; holds expire via a sweeper, not TTL; and money state never tolerates last-write-wins across regions."*

## Pattern to drill
The outbox has **two halves**: (1) persist intent before the side effect *(I now do this reflexively)*; (2) make the side effect **idempotent on a stable token** so a crash-retry self-heals *(still reaching for a human alarm instead)*. And re-check claim/lease **reclaim predicates** — a status filter that excludes the in-progress state silently disables the whole lease.
