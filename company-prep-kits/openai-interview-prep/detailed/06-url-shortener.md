# URL Shortener — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#7573becd-2ca7-4a9c-97ed-e585990b70ef

## What we're building
Turn a long URL into a short code (`short.ly/aB3xK9`) and redirect anyone who visits the code to the original URL. Optionally let users pick a custom alias (`short.ly/my-promo`). Reads (redirects) vastly outnumber writes.

## The trap the interviewer is setting
This question *looks* like it wants a giant distributed system, and the interviewer is watching to see if you over-build. The scale numbers — roughly 40 writes/sec, 4,000 reads/sec, ~3TB over 5 years — **fit comfortably on a single relational node**. Reaching for DynamoDB + DAX + Global Tables is the over-engineering tell. The strong answer is to **right-size**: one well-run database plus a cache.

## The design: Aurora PostgreSQL + Redis + read replicas
- **Aurora PostgreSQL (Multi-AZ)** as the primary — a single node handles this workload with room to spare, and Multi-AZ gives you the 99.9% availability target.
- **ElastiCache Redis** in front for the hot redirect path.
- **Read replicas** for scaling reads further.

The relational DB also hands you a unique constraint for free, which turns out to be the linchpin (below).

## ID generation: random, not sequential
**Generate a random 7-character Base62 code** (using a cryptographically-secure RNG). If by chance it collides with an existing code, the database's unique constraint rejects the insert and you simply generate a new one and retry. How often does that happen? After 5 years the keyspace is only ~0.17% full, so a collision is astronomically rare — retries are essentially never needed.

Why random rather than a counter or a fancy scheme?
- **No shared counter:** a global incrementing counter is a contention bottleneck and a coordination headache across nodes.
- **No Feistel/scramble:** during the debate this got killed as over-engineering — and the justification ("reversible-free") was just wrong, since a bijection is reversible by definition. Random + retry is simpler and unpredictable for free (sequential IDs leak how many links exist and are guessable).
- Bump to 8 characters if scale grows ×100.

## Uniqueness: one constraint to rule them both
Use a single **`UNIQUE(code)`** constraint (it's the primary key). It's the single source of truth for uniqueness of **both** randomly-generated codes **and** user-chosen custom aliases. A custom alias that's taken simply fails the constraint → return **409 Conflict**. You don't need separate logic for the two cases.

## Redirects: 302, not 301
Return **302 (temporary) or 307**, with a short cache TTL — **not 301 (permanent)**. A 301 gets cached by browsers essentially forever, which means you lose the ability to expire links, disable abusive ones, or count clicks (the browser stops asking you). Keep edge-caching of redirects **off by default** for the same control reasons.

## Reads: cache, but handle the cold/missing cases correctly
- Check **Redis** first (`code → long_url`).
- On a miss for a brand-new or unknown code, read the **primary, not a replica.** Why: a just-created code may not have replicated yet, so a replica could falsely return "not found" and you'd cache a wrong 404.
- **Negative-cache** "not found" briefly (~5s) to absorb junk/scanning traffic without hammering the DB.
- Use **single-flight** for hot keys so a cache miss on a viral link doesn't stampede the DB with thousands of identical lookups.

## Abuse handling
- **Synchronous denylist check at creation** (block known-bad domains immediately).
- **Asynchronous deep scan** (SQS → Lambda → Safe Browsing) that can later flip a link to `status = disabled`; the redirect path also checks the denylist at serve time.
- **URL validation:** allow-list schemes (http/https), handle IDN/punycode lookalikes, and block SSRF / internal-IP targets.

## Expiry
The authoritative check is at **read time** (is this link past `expires_at`?). A background job lazily batch-deletes expired rows, using an index on `expires_at`.

## #1 deep dive: custom-alias uniqueness across multiple regions
This is where candidates get cornered, so have it ready. Suppose you went multi-region active-active to be "global." The intuitive design — DynamoDB Global Tables with `attribute_not_exists(code)` — **does not give global uniqueness**:
- Each region checks its *local* copy and both can say "available" for `my-promo` at the same moment.
- Replication reconciles later with **last-write-wins**, so both regions think they won, and one user's mapping is silently overwritten.

**Last-write-wins can never enforce uniqueness.** The correct fix: route every custom-alias **claim** to a single **home region per code** (`hash(code) → region`), so claims for a given alias serialize in one place. Generated random codes can use disjoint per-region RNG subspaces so they never collide cross-region without coordination. Multi-region is an **explicit extension**, not the default — single-region Multi-AZ already meets 99.9%.

## Database schema (Aurora PostgreSQL)
```sql
CREATE TABLE links (
  code         VARCHAR(16) PRIMARY KEY,     -- random base62, OR a custom alias
  long_url     TEXT NOT NULL,
  owner_id     BIGINT,
  status       SMALLINT DEFAULT 0,          -- 0 = active, 1 = disabled (abuse)
  created_at   TIMESTAMPTZ DEFAULT now(),
  expires_at   TIMESTAMPTZ                  -- NULL = never
);
-- PK on `code` IS the uniqueness guarantee for both generated codes and aliases.
-- Insert with ON CONFLICT (code) DO NOTHING; on conflict for a generated code,
--   retry with a fresh random code; for a custom alias, return 409.
CREATE INDEX idx_expires ON links (expires_at) WHERE expires_at IS NOT NULL;
```
Redis: `code → long_url` hot map + a short-TTL negative cache for unknown codes.

## Things to proactively call out (Staff polish)
- **Read-after-create:** the create path / first read must hit the primary to avoid replica-lag false 404s.
- **Alias normalization:** decide case policy, allowed charset, and reserved words — and enforce uniqueness on the *normalized* form (so `My-Promo` and `my-promo` don't both get claimed).
- **Idempotency:** scope create idempotency per-account + request hash so a double-submit doesn't make two links.
- **Aurora ops realism:** acknowledge restore time, vacuum/bloat, and replica lag rather than treating the DB as magic.

## If the interviewer pushes further
- *"Why not DynamoDB for web scale?"* The workload fits one Aurora node; DynamoDB + DAX + Global Tables is over-engineering here — and Global Tables actively *breaks* custom-alias uniqueness (LWW). Right-sizing is the stronger answer.
- *"Why random codes instead of a counter?"* No shared-counter contention, unpredictable for free, and collisions are vanishingly rare at 0.17% fill — caught cleanly by the unique constraint.
- *"How do you guarantee a custom alias is globally unique?"* A single home region per code so claims serialize; never last-write-wins, which can't enforce uniqueness.
- *"301 or 302?"* 302 — a 301 is cached ~forever and you lose expiry, takedown, and analytics control.
