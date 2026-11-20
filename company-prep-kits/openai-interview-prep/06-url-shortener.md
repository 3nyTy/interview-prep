# Design a URL Shortener

> OpenAI Staff SWE sample question. Stress-tested via debate skill (Claude × Codex, 3 rounds → Staff-passable verdict). AWS-native.
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#7573becd-2ca7-4a9c-97ed-e585990b70ef

## Final validated design (the defensible answer)

### Right-size to ONE relational node
- **Aurora PostgreSQL (Multi-AZ) + ElastiCache Redis + read replicas.**
- Workload (40 wps / 4k rps / 3TB) fits a single node — the interviewer plants this hint; jumping to DynamoDB + DAX + Global Tables is the **over-engineering trap**.

### ID generation
- **Random 7-char Base62 (CSPRNG) + `INSERT ON CONFLICT(code)` retry.**
- Fill ratio at 5y ≈ 0.17% → retries vanishingly rare. Unpredictable for free.
- **NO shared counter, NO Feistel scramble** (debate killed both as over-engineering).
- 8 chars if scale ×100.

### Uniqueness
- **`UNIQUE(code)` constraint** — single source of truth for **both** generated and custom aliases.
- Custom alias conflict → 409.

### Redirect
- **302/307 + short TTL** (keeps expiry/takedown/analytics control).
- 301 is cached ~forever by browsers → loses control.
- Edge-caching redirects OFF by default.

### Reads
- Redis hot map; on a miss for a NEW / not-found code → read the **PRIMARY** (avoid replica-lag false 404, then negative-cache it).
- Negative cache ~5s; single-flight for hot keys.

### Abuse handling
- **SYNC denylist at create** + async deep scan (SQS → Lambda → Safe Browsing) flips `status=disabled` + a redirect-time denylist check.
- URL validation: scheme allow-list, IDN/punycode handling, SSRF/internal-IP block.

### Expiry
- Read-time check is authoritative + batched background deletes (index on `expires_at`).

### Multi-region = EXPLICIT EXTENSION
- Not needed for 99.9% (single-region Multi-AZ meets it).
- If asked: route every custom-alias **CLAIM** to ONE home region (`hash(code) → region`) so claims serialize; generated codes get disjoint per-region RNG subspace.
- **LWW is NEVER used for uniqueness.**

## #1 deep-dive (where candidates get cornered)
Custom-alias uniqueness under multi-region active-active. Correct answer: active-active DynamoDB Global Tables + `attribute_not_exists` does **NOT** give global uniqueness (per-region conditional check + async LWW replication → two regions both "win" the same alias → one user silently lost). You need **single home-region authority per code.** This is THE thing to nail.

## Debate scorecard
- Round 1 caught: (a) the LWW uniqueness bug [critical], (b) over-engineered storage vs the hint, (c) Feistel oversold ("reversible-free" was wrong — a bijection is reversible by definition).
- Revised design fixed all.
- Round 3 verdict = Staff-passable **if** you proactively call out: read-after-create, alias normalization (case policy / charset / reserved words on the **normalized** form), idempotency scoping (per-account + request hash), and Aurora ops realism (restore time, vacuum/bloat, replica lag).
