# Cheat Sheet — URL Shortener

🔗 Diagram: https://excalidraw-production.a.musta.ch#7573becd-2ca7-4a9c-97ed-e585990b70ef

## 1. Summary (in plain terms)
Turn a long URL into a short code (`/aB3xK9`) and redirect anyone who visits it. Reads (redirects) hugely outnumber writes. The interviewer's trap: the scale is small enough for one normal database — **don't** over-engineer it.

## 2. Key deep dives (plain terms)

- **Right-size: one Postgres (Aurora) + Redis cache.** The numbers (~40 writes/sec, ~4k reads/sec, 3TB) fit a single beefy relational node with read replicas. Reaching for DynamoDB + DAX + Global Tables is the over-engineering trap the interviewer is baiting.

- **Make codes randomly:** generate a random 7-character code; if it collides, the database's unique constraint rejects it and you just try again. Collisions are astronomically rare (the space is 0.17% full even after 5 years). No central counter, no fancy reversible math.

- **Uniqueness = one `UNIQUE(code)` rule** that covers both random codes and user-chosen custom aliases. A custom-alias clash returns 409.

- **Redirect with 302 (temporary), not 301 (permanent).** 301 gets cached by browsers basically forever, so you'd lose the ability to expire, disable, or count clicks.

- **Reads:** check Redis first. On a miss (brand-new or unknown code), read the **primary**, not a replica — a lagging replica could falsely say "not found." Cache "not found" briefly to absorb junk traffic.

- **Abuse:** check a denylist synchronously at creation, plus an async deep scan (Safe Browsing) that can disable a link later. Validate the URL (block internal IPs / SSRF, handle punycode).

- **Expiry:** the real check happens at read time; a background job lazily deletes expired rows.

## 3. The #1 deep dive (where candidates get cornered)
**Custom-alias uniqueness across multiple active regions.** If you went active-active (DynamoDB Global Tables + `attribute_not_exists`), two regions can *both* accept the same alias because each checks only its local copy, and replication reconciles later with "last write wins" — so one user is silently overwritten. **Last-write-wins can never enforce uniqueness.** Fix: route every alias *claim* to a single home region per code (`hash(code) → region`) so claims line up in one place.

## 4. Database schema (Aurora PostgreSQL)
```sql
CREATE TABLE links (
  code         VARCHAR(16) PRIMARY KEY,     -- random base62, or custom alias
  long_url     TEXT NOT NULL,
  owner_id     BIGINT,
  status       SMALLINT DEFAULT 0,          -- 0=active, 1=disabled (abuse)
  created_at   TIMESTAMPTZ DEFAULT now(),
  expires_at   TIMESTAMPTZ                  -- NULL = never
);
-- UNIQUE is implicit via PK on `code` → the single source of truth for
--   BOTH generated codes and custom aliases. Insert with ON CONFLICT(code)
--   DO NOTHING and retry on a fresh random code.
CREATE INDEX idx_expires ON links (expires_at) WHERE expires_at IS NOT NULL;
```
Redis: `code → long_url` (hot map) + short-lived negative cache for unknown codes.
