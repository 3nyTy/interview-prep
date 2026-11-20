# Valon coding — TWO different problems

⚠️ **Read this first.** This folder holds solutions to two unrelated problems, and the second one
is the one Valon actually asked.

| | Problem | Where | Status |
|---|---|---|---|
| **Asked 2026-08-20, "Domain Coding" round** | **Loan ledger** — fee/payment events, then out-of-order backdated accounting events | `parts/LoanPart1`–`LoanPart3.java` | 66 assertions passing |
| Predicted from 1point3acres, **not asked** | In-memory SQL database, 7 variants | `InMemoryDatabase.java`, `ValonOaRunners.java`, `parts/Part1`–`Part4.java` | 81 assertions passing |

**The lesson.** The public 1p3a bank carries Valon's OA and technical-screen problems. The
**Domain Coding** round is a mortgage-domain problem written by Valon, and the bank gave no
signal about it. Prepare both; don't let the public bank set expectations for the domain round.

---

# Part A — the loan ledger (what was actually asked)

**Step 1.** `process_fee` increments the fee balance. `process_payment` pays the fee down first,
then principal with the remainder. Opening state `Balances(100_000, 0)`.

**Step 2.** `LoanLedger.process_event(event_type, amount, accounting_sequence)` returns the whole
history as `(accounting_sequence, principal, fee)`. Events **arrive** in real time but are
**accounted** in `accounting_sequence` order, so a backdated event lands mid-history and
everything after it changes. Stated constraint: *"we should not have to re-process all events
every time an out-of-order event comes in."*

## ⭐ The insight the whole problem turns on

`min()` makes the fold **order-dependent and non-invertible**. The same fee and payment give
`(600, 0)` in one order and `(500, 100)` in the other — `LoanPart1` proves it in a test. So a
backdated event **cannot** be folded in as a delta: no prefix sum, no Fenwick tree. You must
replay. The only question is how much.

## The structure

`TreeMap<Key, Node>` keyed on `(accounting_sequence, arrival_ordinal)` — sequence orders the
books, arrival breaks ties so same-date events keep receipt order. Each node caches
`balancesAfter`, so:

- `lowerEntry(key)` hands you the predecessor's snapshot — **the prefix is never recomputed**
- `tailMap(key, true)` replays **only the suffix**

O(log n + k), k = events accounted after this one. In-order arrival replays exactly **1** node
(measured: max 1 over 10,000 events). Backdating in front of 100 replays 101 — and that is
**output-bound**, because those 101 rows genuinely have different values now. No correct
implementation touches fewer.

## Things to say out loud

- **Immutability in Step 1 is load-bearing.** `Balances` is a record and both operations are
  pure. Step 2 caches a snapshot per position; in-place mutation would corrupt every snapshot
  that aliases the same object.
- **The return contract is the real bottleneck.** `process_event` returns the *whole* history,
  O(n) per call — so n events is O(n²) for any implementation. Offer returning the changed
  suffix instead.
- **Overpayment.** `LoanPart1` clamps principal at zero and flags it; `LoanPart3` fixes it
  properly with a `SUSPENSE` bucket, because a servicer must never destroy money. A conservation
  test pins `debt == opening + charged − (paid − suspense)` across 4,000 randomly-ordered,
  sequence-colliding events.
- **Clarify:** two events at the same `accounting_sequence` — one row each, or netted? These
  files emit one row per event, in receipt order.

## Files

| File | What | Verified |
|---|---|---|
| `parts/LoanPart1.java` | Step 1. Pure functions, order-matters proof, validation | 18/18 |
| `parts/LoanPart2.java` | Step 2. TreeMap + suffix replay, replay-count instrumentation | 21/21 |
| `parts/LoanPart3.java` | Not asked — built as the likely next escalation: N-bucket payment waterfall (`FEE → INTEREST → PRINCIPAL → ESCROW`), `SUSPENSE`, `balancesAsOf` | 27/27 |

Each file is standalone with a `main()`, and every line new or changed since the previous part
carries an inline marker.

```sh
cd parts && javac LoanPart2.java && java LoanPart2
```

---

# Part B — the in-memory SQL database (predicted, not asked)

Still worth keeping: it is the 1p3a bank, so it is what an OA or a follow-up screen would draw
from. `parts/Part1`–`Part4.java` are standalone per-part files with the same inline change markers;
Parts 5–7 exist only inside the full engine, because the interview intervened before they were
split out.

Extracted 2026-08-19 from 1point3acres (premium session, via Apple Events against the
logged-in Chrome — `curl`/WebFetch return the paywalled preview only).
Raw captures in `../questions/`, cleaned specs in `../clean/`.

## The 7 variants map to 7 parts of one engine

| Part | 1p3a id | Title | What it adds |
|---|---|---|---|
| 1 | `bc8b7bb7` | Insert and Select by Column Names | create / insert / project |
| 2 | `101b79f9` | Filter Rows by Column Conditions | WHERE equality |
| 3 | `e711c837` | Order By a Column | stable sort, asc/desc |
| 4 | `d5d1b167` | AND Filters | upsert by `id`, AND predicates, `=` `>` `<` |
| 5 | `709ae770` | SELECT, IN, FILTER BY | `IN (...)`, six operators, `*` |
| 6 | `85ac94f9` | Flexible-Schema, Column-Based Query | inverted column→ids index |
| 7 | `e1de73a3` | Sparse Table, Filtering + Projection | typed schema, validation, renaming |

## Files
- **`InMemoryDatabase.java`** — the engine, Parts 1–7, with a `main()` that asserts
  **every published example verbatim**. `javac InMemoryDatabase.java && java InMemoryDatabase`
  → **29/29 PASS**, plus a 100k-row / 20-column scale run.
- **`ValonOaRunners.java`** — hand-rolled `MiniJson` + the three stdin/stdout drivers.
  All three reproduce the published sample output byte-for-byte:
  ```
  java ValonOaRunners and    < input   # d5d1b167
  java ValonOaRunners sparse < input   # e1de73a3
  java ValonOaRunners sql    < input   # 709ae770
  ```

## The three decisions that carry the whole problem

**1. A row is a `Map`, not a fixed array.** Every variant lets a row omit columns
("sparse", "flexible schema", "the attribute does not exist"). `LinkedHashMap` per row
makes absence first-class — `containsKey()` is the whole game. An array with sentinels
cannot distinguish *absent* from *empty string*, and several variants turn on exactly that.

**2. ⭐ Absent fails a predicate but yields `null` on projection.** Stated in four of the
seven: *"a row missing the filter column does not match"* — yet a missing **output**
column projects as `null`. Two different behaviours from one condition. Conflating them
is the single most likely bug.

**3. Insertion order is part of the contract**, and Part 4 sharpens it: replacing an
existing `id` must **not** move the row. So rows live in an `ArrayList` and `id → position`
is a side index. Sorting must be **stable** — `List.sort` is (TimSort); a `PriorityQueue`
is not and would silently break Part 3's stated tie rule.

## Traps worth saying out loud
- **`bool` must not satisfy `int`** (Part 7, stated for Python; same trap in Java is
  `Boolean` vs `Long`). `checkType` tests `Boolean` first and rejects it for `INT`.
- **Comparison semantics differ per variant.** Equality is always exact string compare.
  Ordering is *numeric if both sides parse as numbers, else lexicographic* — so `9 < 10`,
  not `"9" > "10"`. One `compareSmart` satisfies Parts 3, 4 and 5.
- **`>` `<` on a missing column excludes the row** rather than throwing.
- **`EMPTY`** is the literal output for a no-match query in Part 5 — not a blank line.
- **`*` expands to declared column order**, so the declared order must be retained even
  though rows are maps.
- **Part 6 is the only one where an index is the point.** `column → row positions` built
  at insert time makes "which records have this attribute" O(matches) instead of O(N·K).
  Say that; it is what the variant is testing.
- **A hand-rolled JSON parser is required** for three variants — no Gson/Jackson in an OA.
  `MiniJson` is ~70 lines: recursive descent over a char cursor, `LinkedHashMap` to keep
  key order, `Long` for integers so `1` does not serialise as `1.0`.

## Ask the interviewer
- Missing projected column → `""` or `null`? (Part 1 says "align with the interviewer".)
- Unknown filter column → empty result or error? (Part 2 says clarify.)
- Rows missing the ORDER BY column — first or last? Not specified anywhere.
- Are values typed or all strings? Parts 1–6 treat them as strings; Part 7 types them.
