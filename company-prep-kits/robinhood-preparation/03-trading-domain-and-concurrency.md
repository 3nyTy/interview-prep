# Trading domain and concurrency — 4 problems

Robinhood asks trading-flavoured versions of standard problems. The domain wrapper is
usually thin, but the money-handling edge cases are real and they are what gets graded.

---

## 1. Fractional Shares Trading & Inventory Updates

Source: `013a4cda-6619-4f71-abd9-f110618752d8`

### Statement

Build a system to process buy/sell trade operations and track stock positions per ticker
symbol, with support for fractional shares.

### Requirements

- Process chronological trade instructions (BUY / SELL).
- Maintain the current position (share count) for each ticker.
- Support fractional share quantities.
- Use a hash map for efficient updates.

### Input / output

Each instruction contains an action type, a ticker symbol, and a quantity (possibly
fractional). Output the final position per ticker, or a position snapshot after each
operation.

### Edge cases named in the problem

- Sells that exceed the current position
- Fractional precision management
- Output ordering — insertion order or lexicographic

### Scale

Up to 10^5 instructions and 10^5 distinct tickers.

### Example

`BUY AAPL 1.5`, `BUY TSLA 2`, `SELL AAPL 0.4` → `AAPL: 1.1`, `TSLA: 2`

### Approach

Easy on the surface. The grading is entirely in the precision handling.

**Never use `double`** — but be precise about why, because the obvious claim is **false**.
⚠️ `1.5 - 0.4` is **bit-identical** to the literal `1.1` in IEEE-754 double (both are exactly
`1.100000000000000088817841970012523233890533447265625`), so `(1.5 - 0.4) == 1.1` is `true`
and **the 3-trade sample passes with `double`.** Verified — see `coding/FractionalShares.java`.

The real argument is **accumulation across the stated 10^5 instructions**. Single operations
often round back to the nice value; chains don't:
- ten additions of `0.1` → `0.9999999999999999`
- `BUY 0.1, BUY 0.2, SELL 0.3` → `5.551115123125783E-17` instead of flat

At 100k mixed-quantity instructions, **1000 of 1000 tickers** end non-zero under `double` and
**0 of 1000** under scaled `long`. A position that should be flat isn't — at a brokerage that's a
reconciliation break. ⚠️ Note a *symmetric* workload (N buys then N sells of the same size)
cancels exactly under `double`, so you need mixed quantities to show the bug.

Two acceptable answers:
- `BigDecimal` with an explicit scale and `RoundingMode`. Correct, verbose, slower.
- **Scaled integers** — store quantity as a `long` in units of 10^-6 shares. Fast, exact,
  and it is what real trading systems do. Prefer this and say why.

```java
Map<String, Long> position = new HashMap<>();   // micro-shares
static final long SCALE = 1_000_000L;
```

Parse the decimal string to a scaled long directly. Do not parse to `double` first — that
reintroduces the error you were avoiding.

### Traps

- The `double` precision trap above. This is the entire question.
- Over-sell. Ask: reject the whole instruction, clamp to zero, or allow a short position?
  A brokerage would reject. State your choice.
- Output ordering is explicitly listed as ambiguous. Ask, then use a `TreeMap` if the
  answer is lexicographic.
- Should a ticker that nets to exactly zero appear in the output? Ask.

### Paywalled

Complete statement, test cases, sample I/O, solutions, round type, frequency, date asked.

---

## 2. Implement StockManager.placeOrder Method with Thread Safety

Source: `39c5c498-22ee-4aac-8c7a-d9d6cabe7983`
Note: this is an **iOS / Swift** problem. Skip if the loop is backend-only.

### Requirements (from the public text)

- Implement a `placeOrder` method that accepts stock names (for example APPL, GOOGLE, META).
- Execute orders **sequentially, in call order**.
- Retry logic: up to three attempts before moving on.
- Ensure thread safety.
- Consider the Swift `actor` pattern.
- Convert callback functions to `async`/`await` if using actors.

### Approach

The requirement pair "thread safe" plus "sequential in call order" rules out a plain lock —
a lock gives you mutual exclusion but not ordering. You need a **serial queue**:

- Swift: an `actor` serializes access to its state, but `await` inside an actor method
  suspends and lets other calls interleave. To hold strict FIFO order you need an explicit
  task queue inside the actor, or a `DispatchQueue` that is serial.
- The retry must not break the ordering. Three attempts for order 1 all complete before
  order 2 starts.

Retry shape: attempt, on failure retry up to 3 total, then record the failure and continue
to the next order. Do not abort the queue.

If asked the equivalent in Java: a single-threaded `ExecutorService` gives submission-order
FIFO execution and thread safety in one construct.

### Traps

- "Sequential in call order" is the requirement that most candidates drop. A concurrent
  queue with a mutex satisfies thread safety and fails ordering.
- Clarify whether the 3 attempts have a backoff.
- Clarify whether a permanently failed order blocks the queue or is skipped. The wording
  "before proceeding" implies skipped.

### Paywalled

Difficulty, duration, tags, I/O spec, constraints, examples, follow-ups, solution, metadata.

---

## 3. Implement Two Methods in an Android Stock Trading System Skeleton

Source: `fda2bc12-f789-424c-8628-4e10bef22aee`
Note: **Android / Java or Kotlin.** Skip if the loop is backend-only.
This page has the least public content of the 23.

### Statement (public portion, verbatim)

> "Implement these two methods, without changing existing public interfaces, so that the
> system can correctly perform the core logic such as order submission/processing and state
> updates."

The two TODO methods relate to:
- Order creation and validation
- State transitions
- Account and position updates
- Balance management

### What is missing

The skeleton code, the method signatures, the business rules, the validation requirements,
and the test cases are all members-only. The problem is not solvable from the public text.

Treat this as a signal about the round format rather than a practice problem: Robinhood
gives you existing code with TODOs and grades whether you can work inside a public
interface you did not design. Read the surrounding code before writing anything.

### Paywalled

Everything except the sentence quoted above.

---

## 4. Design a constrained data structure for concurrency and resource abuse scenarios

Source: `6fa97fba-0586-408e-a3d1-b4d29293ece6`
Relevant to the Security / IAM role. Pairs with `02` problem 4.

### Statement (public portion)

Design "a constrained data structure that supports access in highly concurrent scenarios",
with a focus on abnormal conditions and resource misuse.

### Requirements

- Security through permission checks and input validation.
- Demonstrate "rationality, robustness, and security consciousness".
- Address safety and controllability under high concurrency, malicious input, or abnormal
  requests.

### How to attack it

"Constrained" plus "resource abuse" points at a **bounded structure with admission
control** — in practice a rate limiter or a bounded cache. The existing rate limiter work
in `~/reddit-interview-prep/coding/` transfers directly here.

Structure the answer as three separable layers. Keeping them separate is the design point:

1. **Admission** — is this caller allowed to write at all? Per-principal quota, token
   bucket. This is where resource abuse is stopped.
2. **Authorization** — is this caller allowed to touch this key? Permission check before
   any read or write, not after.
3. **Storage** — the bounded structure itself. Fixed capacity, explicit eviction policy,
   per-key and total size caps.

On concurrency:
- Per-key striped locks over one global lock. Say why: a global lock makes the structure
  itself the denial-of-service target.
- `ConcurrentHashMap` with atomic `compute` for read-modify-write. A `get` then `put` is a
  race.
- Bound everything — entry count, key length, value size. An unbounded structure under
  concurrent load is the resource-exhaustion vector the question is naming.

On abuse:
- Reject early and cheaply. Validate size before allocating.
- Fail closed on a permission check error.
- Do not let error paths allocate more than success paths.
- Per-principal accounting, so one caller cannot consume the shared budget.

### Traps

- The question rewards "security consciousness" explicitly. Raise the threat model
  unprompted — who is the attacker, what do they control, what is the blast radius.
- Do not jump to a lock-free implementation. Correctness and bounds first.

### Paywalled

Complete details, sample I/O, test cases, constraints, edge cases, follow-ups, solutions,
round type, date asked, frequency.
