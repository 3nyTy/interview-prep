# Mock Scorecard — Q6: URL Shortener

**Date:** 2026-07-15 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/06-url-shortener.md`](../detailed/06-url-shortener.md)

**Verdict:** Reached every right answer, but *led* with the two traps this question exists to catch — **over-building** (sharded a single-node workload) and **301-vs-302**. The distributed-systems machinery is clearly strong now; the growth edge here is **judgment / right-sizing** — sometimes the Staff answer is "this is small, one node, move on."

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ⚠️→✅ **Right-size the store** — *fix:* single primary (Aurora Multi-AZ) + read replicas + cache clears 99.9%. I **sharded by alias** (the over-engineering trap) and contradicted myself ("the whole dataset fits in cache"). Corrected after a nudge. **Compute the numbers → match the architecture → don't reflexively shard.**
2. ✅ **Cache-on-create** (redirects cluster right after creation), TTL, whole dataset fits in cache.
3. ✅ **Validate the URL first**, then generate.
4. ✅ **Uniqueness** — unique index; random Base62 code, retry on conflict.
5. ⚠️ **Bloom filter** — *fix:* unnecessary; the unique constraint + retry already handles collisions at 0.17% fill, and a bloom false-positive would wrongly reject an available custom alias. Dropped after nudge.
6. ✅ **Expiry** — store `expires_at`, validate `now() ≤ ttl` at read time.
7. ✅ **Custom-alias uniqueness (multi-region — the #1 deep dive)** — landed the fix (serialize the claim) + the correct read-heavy tradeoff, **after** I was taught the LWW mechanism. *Bank the mechanism:* async replication + local conditional check → both regions say "available" → LWW silently overwrites one → **not unique**. **LWW can never enforce uniqueness** (a conditional write is atomic only against a *single* copy). *Fix precisely:* **`hash(alias) → home region`** (not one global region — that's a write bottleneck+SPOF; hashing spreads load, one arbiter per alias). Random codes need **zero** coordination — **disjoint per-region RNG subspaces**. Multi-region is an **explicit extension, not the default** (single-region Multi-AZ already hits 99.9%).
8. ❌ **301 vs 302** — chose **301** ("mapping is permanent"). Wrong. *Fix:* **302/307 + short TTL.** A 301 is browser-cached ~forever → you lose **click analytics, expiry, takedown, and retargeting** (the browser stops asking you). "Rarely changes" ≠ "use 301" — you need operational control over every redirect.
9. ⚠️ **Read-after-create** — *fix:* on a cache miss for an unknown code, read the **primary, not a replica** (replica lag → false 404 that you'd then cache).
10. ⚠️ **Abuse** — I validated the URL (good start). *Add:* denylist at create + serve time, SSRF / internal-IP block, IDN/punycode handling, async Safe-Browsing scan → flip to `disabled`.
11. ⚠️ **Hot/cold cache correctness** — *add:* negative-cache 404s (~5s) to absorb scanners; single-flight on a viral key so a miss doesn't stampede the DB.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"The numbers fit one node — single primary + replicas + cache; reaching for DynamoDB + Global Tables here is the over-engineering tell."*
- *"Last-write-wins can never enforce uniqueness — route each alias's claim to a home region (`hash→region`) so it serializes against one arbiter; random codes use disjoint per-region subspaces, no coordination."*
- *"302/307, not 301 — a 301 is cached forever, so you lose analytics, expiry, and takedown."*

## Pattern to drill
**Judgment / right-sizing** — the opposite muscle from Slack/Calendar, where sharding *was* correct. The Staff skill is knowing *which*: state the load, compare to a single node's capacity, and only distribute when the numbers actually demand it. My reflexes (fencing/outbox/seq/sync) are strong; over-engineering is now the bigger risk than under-designing.
