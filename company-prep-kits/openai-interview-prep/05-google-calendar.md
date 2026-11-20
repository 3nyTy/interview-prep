# Design Google Calendar

> OpenAI Staff SWE sample question. Validated via codex-debate (2 rounds; Round 3 starved out by concurrent Codex sessions, but 2 is the sweet spot).
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#045a7bd5-ce87-4907-8055-c04eeb8a1f7d

## Scale
500M users, 100M DAU, 250B events / 250TB, ~800 wps / ~12K rps (read-heavy 15:1), reads <200ms, sync <2s, eventual consistency, 99.9%.

## The design (DynamoDB-based, AWS-native)

### Storage
- DynamoDB. **EVENTS table** (PK=`calendar_id`, SK=`EVENT#event_id`) = source of truth.
- `calendar_id ≈ user_id` (sharing out of scope).

### Views = month-bucketed OCCURRENCE INDEX
- PK=`calendar_id#YYYYMM` (in **local** tz), SK=`local_start#event_id`.
- Event written into every local-month bucket its `[start, end]` intersects (write-amp ≈ #months ≈ 1 typical).
- Day/week/month/year = read 1–12 buckets + overlap-filter.
- **This REPLACED v1's "bounded-lookback + 30-day duration cap,"** which Codex broke with multi-month event counterexamples.
- Events spanning >24 buckets → a per-cal **LONGSPAN list** always read.

### Time handling
- **All-day events = floating local civil dates (no tz)**; timed events = UTC + origin-tz.
- Fixes the "Tokyo sees Jan 1 on Jan 2" bug and the 23/25h DST bugs.

### Sequencing
- DynamoDB **CANNOT** feed an incremented value between actions in one `TransactWriteItems` (Codex caught this in v1).
- So: (1) `UpdateItem ADD seq` with `RETURN_VALUES` → N, then (2) `TransactWrite`(event row + index rows + change-log row).
- Bulk import reserves a **seq RANGE** in one `ADD` (avoids hot-item contention).

### Sync = append-only CHANGE-LOG table
- PK=`calendar_id`, SK=`seq`, embedded current-state snapshot, TTL 90d. **NOT** an LSI.
- Delta sync uses **high-watermark + pagination**: read current `(epoch, seq)` = HWM, page `N < seq <= HWM` with `LastEvaluatedKey`, advance the durable token **only after all pages drain** (v1's `next_token = max` bug skipped ranges on mid-pagination crash).
- Token expired / older than retention → **410 → destructive full resync.**

### Real-time = coalesced notify-then-pull
- EVENTS → DDB Streams → fan-out Lambda → **buffer/coalescer (Kinesis/SQS) keyed by `calendar_id` with debounce** → push ONE "calendar dirty, latest=`(epoch, seq)`".
- **Mobile = APNS/FCM silent push** (carries the 100M-DAU bulk).
- **WebSocket only for active web** (API GW WS caps ~3.6M concurrent/region & ~$1M+/mo for 100M — can't hold 100M sockets).
- Device then does `GET /sync`.

### Multi-region
- A single multi-AZ region already meets 99.9% (DDB single-region is designed for 99.99%). Global active-active is **NOT** the default.
- Optional latency/DR extension: single home-region writer, sync token = `(region_epoch, seq)` with a **fenced failover epoch** so a post-failover seq regression can't corrupt history (forces 410 → resync). v1's plain Global-Tables seq was a real corruption bug.

### Idempotency & conflicts
- Idempotency key scoped `(user, op)` + payload hash; returns the original response on replay.
- Conflicts: field-level LWW for independent scalars; reject + refetch on same-field collision.

## Top deep-dive (what an interviewer drills)
The view-overlap data model (month-bucket vs duration-cap) and multi-region sync-token monotonicity under failover. Both were v1 flaws Codex found; both now closed.

## Pattern note
Codex consistently rewards right-sizing & correctness over AWS-service-reaching. Here **DynamoDB IS right** (per-user partition is the ideal access pattern) — unlike the URL Shortener where Aurora won.
