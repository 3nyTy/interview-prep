# Crib — The Two Security Designs (Robinhood)

From the 1point3acres bank. Both pages are thin on public text, which makes them **open-ended — and
that is the opportunity.** These are the closest questions in the loop to the Staff SWE, Identity &
Access Management role.

⚠️ **Everything below is stated as a policy you choose, not a rule you recite.** The failure mode in
these rounds is absolute claims ("always NFKC", "always fail closed", "canonicalisation is the cause")
— each one invites a probe that ends badly. Say *"here's the policy and here's what it costs."*

---

# A. String validation with security constraints

> "Design a string validation system that handles security-related constraints. It should prevent
> maliciously constructed inputs from bypassing validation and maintain performance under abnormal
> traffic."

## THE FOUR SENTENCES

1. *"Two attack goals are named — **bypass** and **degradation** — and one attack achieves both."*
2. ⭐ *"That's ReDoS: a crafted input against a backtracking regex is simultaneously a malicious input
   and a performance failure. So I cap length before any regex runs, and I don't put a backtracking
   engine on untrusted input at all."*
3. ⭐ *"For bypass, the thing I'd actually build is an **immutable validated value object**: validate
   once, then carry the canonical value through authorisation and storage. Most bypasses I've seen
   aren't a missing rule — they're validating one representation and consuming a different one."*
4. *"And the validation profile is **per field type**. There is no single normalisation policy that's
   right for usernames, emails, hostnames and display text."*

## 1. Bypass — classes, plural

⛔ Don't claim canonicalisation is the cause of "nearly every" bypass. It's one class. Name the others,
because an IAM interviewer will: **missing object-level authorisation** (the most common real one),
confused deputy, **parser differentials** (two components disagree about the same bytes), type
confusion, injection into SQL/LDAP/path/command, stale policy cache, and **trusting a caller-supplied
tenant or principal id**.

Then go deep on the canonicalisation class, since that's the one a *validation system* owns:

- ⭐ **The TOCTOU is real and "normalise once" does not close it.** The validated value can still
  diverge from the consumed value: a mutable `CharSequence` changes after the check; a downstream
  service trims, case-folds or re-normalises; the database collation differs; the filesystem
  normalises differently; authorisation uses the canonical form while storage keeps the raw one; one
  service runs an older policy version.
  **The fix is structural:** snapshot input into an **immutable validated value object**, and pass
  *that* (or its canonical bytes) through authorisation and storage. Keep the raw input only for
  display and audit. Define **one cross-service canonicalisation contract**, versioned.
- **Normalisation policy is per field type, not global.** NFKC deliberately collapses compatibility
  distinctions, so it can *create* identifier collisions. Usernames, emails, hostnames, URLs, account
  numbers and free text each need their own decision on normalisation form, case policy, and permitted
  scripts. ⛔ "NFKC for identifiers" as a blanket rule is wrong.
- **Confusables: detect, don't rewrite.** UTS #39 skeletons are for **detecting collisions** against
  existing identifiers, not for silently transforming input. And single-script restriction is not a
  complete remedy — legitimate identifiers span scripts, `Common`/`Inherited` characters complicate the
  rule, same-script confusables still exist (`rn` vs `m`), and skeleton mappings are **versioned
  policy** that changes under you.
- **Prefer opaque ASCII identifiers wherever you control the format.** The strongest answer to
  Unicode identifier attacks is to not use Unicode as a security-relevant identifier: authorise
  against an immutable internal id and let the Unicode string be display-only. Say this — it reframes
  the problem instead of fighting it.
- **Decode per protocol grammar, exactly once per layer.** ⛔ Do **not** "decode until stable", and
  ⛔ do **not** reject because a "generic encoding marker" remains — there is no such thing.
  Counterexample: a user legitimately wants the literal `%20`; the client sends `%2520`; one correct
  decode yields `%20`, and rejecting it breaks real input. The rule is: decode according to *that
  layer's* grammar once, and reject **malformed escapes at that layer**.
- **Reject NUL and control characters explicitly.** `admin\0.txt` truncates in consumers with C-string
  boundaries — a classic identity bypass. Also reject over-long UTF-8, lone surrogates, non-characters
  at the decoder, and **bidi controls** (RLO/LRO) that visually reorder a rendered identifier.
- **Decide whether the length limit is bytes, codepoints, or grapheme clusters** — they differ, and a
  limit measured in Java `char` units is none of them once you have astral-plane input.
- **Allowlist, not denylist**, for the permitted character set.

## 2. Degradation — ReDoS is the intended answer

- Java's `java.util.regex` is a **backtracking** engine, so `(a+)+$` or `(a|a)*` against a run of `a`
  is exponential. Live in any Java validator.
- Mitigations, cheapest first:
  1. **Cap length before the regex runs.** One comparison. ⚠️ But note it only *bounds* the damage —
     a polynomial blowup under a generous cap can still be expensive, so a cap alone is not a proof.
  2. **Linear-time engine** for untrusted input — RE2/`re2j`. Price: no backreferences, no lookaround.
  3. **Hand-write the parser** for simple grammars; a character loop can't backtrack.
  4. **Bound the work.** Java has **no built-in regex timeout**, so the standard trick is to wrap the
     input in a `CharSequence` whose `charAt` throws after N accesses. Effective, and worth naming
     because it shows you've hit this in production.
- Also bound: body size, field count, and **nesting depth** if you parse anything structured (JSON/XML
  depth bombs, billion laughs).

## 3. Concurrency and operations

- Compile `Pattern` once as `static final`. ⚠️ **`Pattern` is thread-safe; `Matcher` is not** — one per
  call or per `ThreadLocal`. Sharing a `Matcher` is silent corruption.
- **Failure policy per control, not blanket.** For an *authorisation* decision, fail closed. For a
  *validator* that can't reach a dependency, fail closed. But ⛔ "every dependency failure rejects" is
  not a complete design — a policy-service or IdP outage then becomes a total availability incident.
  State bounded policy caching with explicit TTLs, emergency behaviour, and a break-glass path.
- **Error messages: it depends.** In an authn/authz path keep them uniform so failures aren't an
  oracle (which rule fired, whether an account exists) and watch **timing** too. In a user-facing form,
  specific messages are expected and not sensitive.
- **Log a hash plus the rule id, never the raw payload** — raw malicious input in logs is second-order
  injection into whatever reads them.
- **Per-rule metrics**, so an attack is a spike on one rule rather than a flat error rate.

---

# B. Constrained data structure for concurrency and resource abuse

> Design "a constrained data structure that supports access in highly concurrent scenarios", focused on
> abnormal conditions and resource misuse. Requirements name permission checks, input validation, and
> "safety and controllability under high concurrency, malicious input, or abnormal requests".

Read it as a **bounded structure with admission control** — a rate limiter or a bounded cache.

## THE THREE SENTENCES

1. *"Layers that fail independently — and **authentication has to be its own layer**, because
   'per-principal' means nothing until the principal is trustworthy."*
2. ⭐ *"Every dimension bounded, and bounded per principal — but I'd be careful, because for a rate
   limiter **evicting state isn't a cache miss, it's a quota reset**, which is a bypass."*
3. *"Per-key striped locking rather than one global lock, because a global lock makes the structure
   itself the denial-of-service target."*

## 1. The layers (⭐ six, and authentication is one of them)

| # | Layer | Keyed on | Note |
|---|---|---|---|
| 1 | Byte / body / connection limits | connection | cheapest; no identity needed |
| 2 | Coarse pre-auth limit | **only trusted attributes** | ⚠️ see the DoS trap below |
| 3 | **Authentication** | — | derives the principal **server-side** |
| 4 | Per-principal / per-tenant quota | server-derived principal | the real quota |
| 5 | Object/action authorisation | principal + object | before any read or write |
| 6 | Storage operation | key | atomic |

⚠️ **The trap in "cheapest first".** A pre-auth limiter keyed on a **caller-supplied** tenant or
principal id is **spoofable** — an attacker exhausts someone else's bucket. And a global or IP-keyed
unauthenticated limiter can be exhausted by one attacker and **deny unrelated tenants**. Pre-auth
limits must key only on attributes you actually trust (the connection, the source address, with the
understanding that both are shared and partly forgeable). The meaningful quota comes **after**
authentication, on a server-derived principal.
Also: if the limiter behaves differently for valid and invalid identities, you've built an
**enumeration oracle**. Keep status, body and timing uniform across both.

## 2. Concurrency

- **Per-key striped locks**, or `ConcurrentHashMap` with atomic `compute`/`merge`.
  ⛔ `get` then `put` is a race. ⚠️ And `compute` holds a **bin lock** — never do I/O, call out, or
  touch another key inside it, or you get stalls and deadlocks.
- ⛔ **One global lock is a self-inflicted DoS**: an attacker only needs to make one operation slow to
  stall everyone.
- **Bound the queue too.** An unbounded work queue in front of a bounded structure just relocates the
  exhaustion.

## 3. Resource abuse — and the two claims worth being careful about

- ⭐ **Eviction semantics differ by what you're storing, and this is the sharpest point here.**
  - In a **cache**, eviction causes a miss. A shared LRU with attacker-controlled keys is an *eviction
    attack*: flood cheap keys and evict every legitimate hot entry.
  - In a **rate limiter**, eviction **resets the counter — which permits quota bypass.** Opposite
    failure, and partitioning per principal does *not* fix it. Remedies: an algorithm whose state is
    fixed-size per principal, never evicting a live counter (only ones idle past a full window), or
    reconstructing conservatively (assume the bucket is empty, not full) on a miss.
- **Per-principal capacity partitioning helps the cache case but has real costs** — say them:
  it wastes capacity when a tenant is idle, denies a hot tenant while global capacity is free, does
  nothing against an attacker holding **many valid principals**, does nothing about global CPU,
  network, queue or downstream exhaustion, and it presumes a non-spoofable principal (layer 3).
- **Bound every dimension**: `max_entries`, `max_key_bytes`, `max_value_bytes`, `max_total_bytes`, and
  per-principal versions of each.
- **Validate size before allocating** — read the declared length, reject, *then* allocate.
- **Keep error paths cheaper than success paths**, or the rejection path is the attack.
- **Hash-collision DoS:** attacker-chosen colliding keys degrade a bucket toward linear. Randomly
  seeded hash, or a structure that treeifies (Java 8+ `HashMap` does).
- **TTL plus active expiry** — lazy-only expiry lets dead entries hold memory until touched.
- **Per-principal metrics and a runtime kill switch**, so mitigating one abusive principal doesn't
  require a deploy.

## 4. What I cannot guarantee (say this — it reads as senior)

> *"Bounds make abuse **survivable**, not impossible. A caller inside their quota can still degrade
> their own service; an attacker with many valid principals still aggregates; and none of this touches
> downstream or network exhaustion. What the bounds buy me is that the blast radius is one principal
> and the system stays up. That's the guarantee I'd commit to, and I'd rather commit to that one than
> overstate it."*

## Traps

- ⛔ "Canonicalisation is the cause of nearly every bypass."
- ⛔ "Always NFKC" / one normalisation policy for all field types.
- ⛔ "NFKC handles homoglyphs" — it doesn't; and TR39 skeletons *detect*, they don't rewrite.
- ⛔ "Single-script restriction solves confusables."
- ⛔ Decode-until-stable, or rejecting on a "generic encoding marker" (the literal `%20` case).
- ⛔ "Normalise once and the TOCTOU is closed."
- ⛔ Omitting **authentication** as its own layer, or keying a pre-auth limit on caller-supplied identity.
- ⛔ Blanket "fail closed on every dependency failure."
- ⛔ Treating limiter eviction as a cache miss.
- ⛔ Presenting per-principal partitioning as a complete fix.
- ⛔ Sharing a `Matcher`; doing I/O inside `compute`.
- ⛔ A backtracking regex on untrusted input; a length cap presented as a *proof* rather than a bound.
- ⛔ Claiming abuse becomes impossible.

## Reuse

`~/reddit-interview-prep/coding/RateLimiterSolutions.java` (four algorithms behind one interface),
`coding/ConstrainedStructure.java` in this folder, `project_reddit_rate_limiter`,
`memory/project_robinhood_application.md` (non-human service identity, server-side authz on every
access, allow-listed read-only tool surface).
