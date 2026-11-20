# Google Calendar — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#045a7bd5-ce87-4907-8055-c04eeb8a1f7d

## What we're building
A calendar for 500M users: create events (including recurring and all-day), view them by day / week / month, sync changes across all your devices quickly, and get reminders. Read-heavy (people look far more than they edit). The two genuinely hard parts: **(1)** "show me everything that overlaps this week" fast, and **(2)** syncing changes to every device without missing or duplicating any.

## Scale
500M users, 100M daily active, ~250 billion events / 250TB, ~800 writes/sec, ~12K reads/sec (read:write ≈ 15:1), reads <200ms, sync <2s, eventual consistency OK, 99.9% availability.

## Why DynamoDB is the right call here
A calendar's natural access pattern is "**give me this one user's events**." That maps perfectly to a partition key of `calendar_id` — every user is their own partition, reads are local, no cross-user joins. So unlike the URL shortener (where Aurora won because the workload fit one node), here DynamoDB genuinely fits. Saying *why* the store fits the access pattern is the signal — not just naming a database.

## The data model
```
EVENTS table (source of truth):
  PK = CALENDAR#<calendar_id>     # calendar_id ≈ user_id (sharing is out of scope)
  SK = EVENT#<event_id>
```
That stores events. But it can't answer "what overlaps this week?" efficiently — which is the crux.

## Hard problem 1: viewing overlapping events fast
**The naive approach and why it fails:** "store events sorted by start time, and to render a week, scan back a bit and forward." The bug is long events: a conference you added that spans *this whole month*, or a multi-month project, *starts* before your lookback window, so a bounded "look back 30 days" scan **misses it**. The first version used a 30-day duration cap as a hack, and the debate broke it instantly with a multi-month-event counterexample.

**The fix — a month-bucketed occurrence index:**
```
OCCURRENCE INDEX:
  PK = CALENDAR#<calendar_id>#<YYYYMM in LOCAL tz>
  SK = <local_start>#<event_id>
```
When you save an event, you *also* write a small pointer into **every local month its `[start, end]` range touches**. A 3-day March trip → one March entry. A Feb–April project → three entries (Feb, Mar, Apr). For a typical short event the write-amplification is ~1.

Now viewing is trivial and correct: a week view reads 1–2 month buckets and filters to the exact window; a month view reads one bucket; a year view reads twelve. No event can be "missed" because it's registered in *every* month it intersects. Pathologically long events (spanning >24 buckets) go on a per-calendar **LONGSPAN list** that's always read, so you don't write hundreds of bucket entries.

## Hard problem 1b: time zones (the bugs that embarrass you)
- **All-day events = floating local civil dates with NO timezone.** Your birthday is "June 23," not "June 23 00:00 UTC" — it shouldn't shift when you fly to Tokyo. Storing it with a timezone causes the classic "Tokyo sees New Year's on Jan 2" bug.
- **Timed events = UTC + the origin timezone.** Keeping the original zone is what lets you correctly handle daylight-saving transitions (the 23-hour and 25-hour days) instead of naively adding 24 hours.

## Hard problem 2: syncing to all your devices
Each device needs "what changed since I last synced?" — fast, complete, and without skips.

**Sequence numbers first.** Each user has a monotonic counter; every change gets the next number. Subtlety the debate caught: **DynamoDB can't generate a counter value and use it within the same transaction.** So it's two steps:
1. `UpdateItem ADD seq` with `RETURN_VALUES` → get the new number N.
2. `TransactWrite` the event row + its index rows + a change-log row, all stamped with N.
(A bulk import reserves a *range* of numbers in one `ADD` to avoid hammering the counter.)

**The change-log + high-water-mark:**
```
CHANGE-LOG table:
  PK = CALENDAR#<calendar_id>
  SK = <seq>
  attrs: embedded current-state snapshot, op, TTL = 90 days
```
A device says "I last saw seq 100." The server reads the current high-water-mark (HWM), then returns everything where `100 < seq <= HWM`, **paginated**. The critical correctness rule: the device advances its saved sync token **only after every page has drained** — not to the max up front. (The first version set the token to the max immediately, so a crash mid-pagination skipped the un-fetched ranges. Real bug.) If a device's token is older than the 90-day retention, you return **410 Gone → force a destructive full resync.**

Note it's a **change-log table, not an LSI** — you want an independent append-only stream of changes, not a re-sort of the events.

## Hard problem 3: real-time updates ("notify, then pull")
You don't push the full event data in real time. You send a tiny nudge and let the device pull:
- An event write → **DynamoDB Streams** → a fan-out Lambda → a **coalescer** (Kinesis/SQS) keyed by `calendar_id` with debounce → sends **one** "your calendar is dirty, latest = (epoch, seq)" message.
- **Phones** get that nudge via **APNS/FCM silent push** — this carries the 100M-DAU bulk cheaply.
- **WebSockets only for active web tabs** — you physically can't hold 100M sockets (API Gateway WebSockets cap around 3.6M concurrent per region and would cost ~$1M+/mo at that scale).
- On receiving the nudge, the device calls `GET /sync` (the delta path above).

Coalescing matters: if you edit 10 events in 5 seconds, the device gets *one* nudge, not ten.

## Multi-region
A single multi-AZ region already meets 99.9% (DynamoDB single-region targets 99.99%). So global active-active is **not** the default — say that. If asked for a latency/DR extension: a single home-region writer, with the sync token carrying a **failover epoch** → `(region_epoch, seq)`. The epoch ensures that if a failover resets the sequence counter, a device detects the regression and is forced to 410 → resync, rather than silently corrupting its history. (The first version's plain Global-Tables sequence number was a real corruption bug.)

## Idempotency & conflicts
- Idempotency key scoped to `(user, operation)` + a payload hash; a replay returns the original response.
- Conflicts: field-level last-write-wins for independent scalars (you changed the title, I changed the location → both apply); reject + refetch on a true same-field collision.

## Full schema recap (DynamoDB)
```
EVENTS (source of truth):
  PK = CALENDAR#<calendar_id>, SK = EVENT#<event_id>
  attrs: title, start_utc, end_utc, origin_tz | (all_day: local civil dates, no tz),
         recurrence_rule, seq

OCCURRENCE INDEX (drives day/week/month views):
  PK = CALENDAR#<calendar_id>#<YYYYMM local>, SK = <local_start>#<event_id>
  → written into every local month [start,end] intersects; >24 buckets → LONGSPAN list

CHANGE-LOG (drives delta sync):
  PK = CALENDAR#<calendar_id>, SK = <seq>
  attrs: state snapshot, op, TTL 90d
  → delta = page where last_seen < seq <= HWM; advance token only after all pages drain

COUNTER (per calendar):
  atomic ADD seq RETURN_VALUES → N, then used in the next TransactWrite
```

## If the interviewer pushes further
- *"Why month buckets instead of a duration cap?"* A cap silently drops long events whose start is before your lookback window. Bucketing into every intersected month makes overlap queries exact, with ~1 write for typical events.
- *"Why can't you just increment-and-use a counter in one transaction?"* DynamoDB can't feed an `ADD`'s result into the same `TransactWriteItems` — so you read the new seq first, then transact with it.
- *"Why notify-then-pull instead of pushing the data?"* You can't hold 100M WebSockets; silent push scales to mobile, coalescing avoids storms, and the device pulls the authoritative delta.
- *"Why does multi-region need an epoch in the token?"* So a post-failover sequence regression can't be mistaken for normal progress — the device detects it and resyncs instead of corrupting history.
