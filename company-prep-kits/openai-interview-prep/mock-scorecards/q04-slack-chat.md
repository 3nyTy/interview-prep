# Mock Scorecard — Q4: Slack (enterprise chat)

**Date:** 2026-07-14 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/04-slack-enterprise-chat.md`](../detailed/04-slack-enterprise-chat.md)

**Verdict:** Best round yet. I volunteered the three things I had to be dragged toward in Q1/Q3 — targeted fan-out, read-pointer unread, and OCC per-channel ordering — and explicitly transferred the chess version-CAS primitive. The one real gap: the **transactional-outbox reflex**. I circled it twice but kept trying to fix a dual-write with queue/DLQ error-handling, which a *crash* skips entirely.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **Real-time transport** — WebSocket per online user; REST/API GW for the rest.
2. ✅ **Connection liveness** — client heartbeat/ping → ElastiCache record + **TTL** (no ping → TTL expiry → assume dead).
3. ✅ **Targeted fan-out** — consumer finds online members and pushes **only to the WS servers holding them**, not a blast to all. *(The key fan-out insight — volunteered.)* *Add:* at 50K members, avoid 50K per-message cache lookups — a **channel→gateway routing tier** (Redis **sharded** pub/sub, `SPUBLISH`) sends one publish to just the handful of gateways with a listener.
4. ✅ **Ordering = per-channel sequence** — read-last + insert `seq+1` + **`UNIQUE(channel_id, seq)`** + retry-on-conflict = OCC; correctly identified as the same primitive as chess `version=N-1` CAS.
5. ⚠️ **Sequencer spectrum** — *fix/know:* OCC (simple, retries) → **atomic counter** (`UPDATE … SET seq=seq+1 RETURNING seq`, one op, watch the burned-seq gap) → **single fenced in-memory writer** (fastest, holds `latest_seq` in memory, epoch-fenced). Pick by contention.
6. ✅ **Shard by channel_id** — co-locate a channel's messages so the per-channel sequence works and no cross-shard lookup; honest hot-partition tradeoff.
7. ❌ **Single hot channel** — *fix:* ID randomness spreads *different* channels but does nothing for *one* mega-channel (all-company announce). The save: big channels are **read-heavy, not write-heavy** → serve reads from **cache/replicas + fan-out tier**; single-channel write hotness is human-rate-bounded.
8. ✅ **Unread without write storm** — `last_read_seq` per user per channel; `unread = latest_seq − last_read_seq`; **one write per read**. *(Nailed unprompted.)*
9. ❌ **Durability = transactional outbox** — *fix:* never "write DB then publish to queue" (dual write — a crash between them orphans the message with **no record a publish was owed**). Commit the **message + an outbox row in ONE txn**; an **independent relay (CDC/streams)** publishes from the outbox; consumers **dedupe on `msg_id`**. *I circled this twice; it must be a reflex.*
10. ⚠️ **Client catch-up pull** — I proposed pulling via `last_read_seq` on reconnect. *Good backstop*, but client-driven + eventual (doesn't meet the 200ms bar for the crash case). Outbox = primary guarantee; pull = safety net.
11. ❌ **Active gap-detection** — *fix:* every message carries `channel_seq`; client sees 5 then 7 → **refetch 6**; plus a periodic **"latest is N" heartbeat** to catch a dropped *last* message (no later msg reveals the gap). I have the seq — use it for detection, not just ordering.
12. ⚠️ **Isolation (listed, not drilled)** — membership check on every read/write + tenant scoping; per-tenant keys; silo the regulated giants. Say "strong logical isolation," not "leaks impossible."

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Never write the DB and publish to a queue as two steps — commit the message and an outbox row in one transaction and let an independent relay publish it; a crash in between can't lose it."*
- *"Ordering is a per-channel sequence from a single fenced writer (or OCC via `UNIQUE(channel_id, seq)`); Snowflake is only for the global unique ID, never for per-channel order — clocks drift."*
- *"Unread is subtraction against a read-pointer, not a counter per member."*

## Pattern to drill
**Dual-write and zombie-write are the same family** — "two things that must be atomic but aren't." Q3 was the zombie write (→ fencing); Q4 is the dual write (→ outbox). On any "store then emit/publish," the reflex is **transactional outbox + idempotent consumer**.
