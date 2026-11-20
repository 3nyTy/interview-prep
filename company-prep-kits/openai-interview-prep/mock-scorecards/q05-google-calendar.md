# Mock Scorecard — Q5: Google Calendar

**Date:** 2026-07-14 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/05-google-calendar.md`](../detailed/05-google-calendar.md)

**Verdict:** On par with Slack — most fluent transfer yet. Reused the sequence-number / change-log / high-water-mark / CDC machinery correctly and reasoned through both delta-sync edge cases with light nudging. The gaps were *data-modeling* choices (bucket granularity, recurrence, timezones), not correctness reflexes — i.e. learnable facts, and the core machinery is clearly sticking.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **Store fits per-user access pattern** — sharded by user/calendar_id so a read hits one shard. (DynamoDB is the natural fit; say *why* the store fits, not just its name.)
2. ⚠️ **View index (hard-part A)** — got the **bucketing insight** (decompose a multi-day event so a long event isn't missed), but went **per-day** (month view = 30 reads, a 3-month event = 90 writes) and detoured into **precomputed weekly/monthly/yearly views** (can't cover arbitrary navigation; more write-amp; still needs the index underneath). *Fix:* **month-bucketed occurrence index** — write the event into every month `[start,end]` touches; date in the **SK** under `PK=calendar_id#YYYYMM` so a view is **one range query**; `>24` months → per-user **LONGSPAN list** read on every query.
3. ❌ **Recurrence** — tried to *ban* infinite recurrence ("no calendar allows forever" — false; Google/Outlook do). *Fix:* store the **RRULE + overall span**, **never materialized**; **expand on read within the bounded view window** (infinity never materializes); use an **overlap** test (not "contains"); handle **exceptions** via an override/EXDATE list keyed `(series_id, original_occurrence_date)` → occurrences − cancellations + overrides.
4. ❌ **Time zones** — not addressed. *Fix:* **all-day = floating local civil date, no tz** (your birthday shouldn't shift when you fly to Tokyo); **timed = UTC + origin tz** (needed for DST 23h/25h days).
5. ✅ **Delta sync (hard-part B)** — append-only **change-log** + per-user **auto-increment seq** + device sends its **high-water-mark token**; server returns everything beyond it. Clean transfer from Slack's `last_read_seq`.
6. ✅ **Pagination without the skip bug** — advance the token to the **last seq of the applied page**, not the global max, so a mid-sync crash just re-pulls. *Tighten:* order = **apply page, then persist token**; at-least-once → **client dedupe by event_id**.
7. ✅ **Expired token** — older than retention → **410 Gone → destructive full resync**, then resume delta from the new HWM.
8. ✅ **Notify-then-pull via CDC** — CDC off the committed tables drives the nudge (outbox done right, *not* a dual-write); device then pulls. *Add:* can't hold 100M WebSockets → **APNS/FCM silent push** for mobile, **WS only for active web tabs**; **coalesce** nudges (10 edits in 5s → 1).
9. ⚠️ **One change = several consistent writes** — had the transaction instinct. *Add:* if DynamoDB, the counter is a **2-step** (`ADD`→get N, then `TransactWrite` event+index+change-log stamped with N); embed a **state snapshot** in each change-log row to avoid an N+1 fetch on sync.
10. ⚠️ **Multi-region (only if pushed)** — single region meets 99.9%; DR extension = sync token carries a **`(region_epoch, seq)`** so a post-failover counter reset is detected → resync, not silent corruption.
11. ⚠️ **Conflict resolution** — field-level **last-write-wins** for independent fields; reject + refetch on a true same-field collision. Not discussed.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Views run off a month-bucketed occurrence index — I write an event into every month its `[start,end]` touches, so a month-long event is never missed, and a view is one range query."*
- *"Recurring events are stored as a rule and expanded on read within the bounded window — I never materialize infinity; exceptions are an override list."*
- *"Cross-device sync is a per-user change-log + a high-water-mark token; advance the token only after a page is applied, and an expired token forces a full resync."*

## Pattern to drill
The **sequence + change-log + high-water-mark** delta-sync spine is now reflexive (Slack → Calendar). CDC-off-the-committed-table is the **outbox reflex done right**. Remaining growth is *domain modeling* (bucket granularity, rule-based recurrence, tz correctness), not distributed-systems reflexes.
