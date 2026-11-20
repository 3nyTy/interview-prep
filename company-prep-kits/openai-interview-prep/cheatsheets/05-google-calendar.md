# Cheat Sheet — Google Calendar

🔗 Diagram: https://excalidraw-production.a.musta.ch#045a7bd5-ce87-4907-8055-c04eeb8a1f7d

## 1. Summary (in plain terms)
A calendar for 500M users: create events, view them by day/week/month, sync across devices fast, and get reminders. The hard parts are: (a) showing "everything that overlaps this week" quickly, and (b) syncing changes to all your devices without missing or duplicating any.

## 2. Key deep dives (plain terms)

- **Views via a "month-bucket" index.** When you save an event, also write a small pointer into every *local* month it touches (a 3-day trip in March → one March bucket; a 2-month project → two buckets). Viewing a week = read 1–2 buckets and filter. This beats the naive "look back 30 days" trick, which breaks for long events. Super-long events go on a separate "long-span" list that's always read.

- **Time zones done right:** all-day events are floating dates with *no* timezone (your birthday is the same date everywhere); timed events store UTC + the original zone. This fixes the "Tokyo sees New Year on Jan 2" and the daylight-saving 23/25-hour-day bugs.

- **Sequence numbers for sync:** DynamoDB can't generate a counter and use it in the same transaction. So do it in two steps: (1) atomically bump the user's counter and read the new value N, (2) write the event + its index rows + a change-log entry. Bulk imports grab a *range* of numbers at once.

- **Sync = an append-only change-log + a high-water-mark.** Each user has a log of changes numbered by sequence. A device says "I last saw #100"; the server sends everything from 101 up to now, **page by page**, and the device only advances its saved bookmark *after every page arrives*. (Advancing too early — to the max — skips changes if it crashes mid-download.) If the bookmark is too old (log expired), force a full resync (HTTP 410).

- **Real-time = "nudge, then pull."** A change flows through a coalescer (debounced per calendar) that sends *one* tiny "your calendar changed, latest=N" push. Phones get it via **silent APNS/FCM push** (handles the 100M-user bulk); only active web tabs use WebSockets (you can't hold 100M sockets). The device then calls `/sync`.

- **One region is enough for 99.9%.** Multi-region is an optional add-on; if added, the sync token carries a **failover epoch** so a post-failover counter reset can't corrupt history.

## 3. Database schema (DynamoDB)
```
EVENTS (source of truth):
  PK = CALENDAR#<calendar_id>           # calendar_id ≈ user_id
  SK = EVENT#<event_id>
  attrs: title, start_utc, end_utc, origin_tz | (all_day: local civil dates, no tz),
         recurrence_rule, seq

OCCURRENCE INDEX (drives day/week/month views):
  PK = CALENDAR#<calendar_id>#<YYYYMM in LOCAL tz>
  SK = <local_start>#<event_id>
  → write into every local month the [start,end] intersects
  → very long events (> ~24 buckets) go to a per-calendar LONGSPAN list instead

CHANGE-LOG (drives delta sync):
  PK = CALENDAR#<calendar_id>
  SK = <seq>
  attrs: embedded current-state snapshot, op, TTL=90d
  → delta = read rows where last_seen < seq <= HWM, paginated;
    advance device token only after ALL pages drain

COUNTER (per calendar):
  atomic ADD seq, RETURN_VALUES → N, used in the next TransactWrite
```
