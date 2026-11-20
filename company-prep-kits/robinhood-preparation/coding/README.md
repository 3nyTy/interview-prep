# Java solutions — part by part

Every file compiles and runs standalone with a `main()` that self-checks against the problem's stated
example plus edge cases. Format follows the OpenAI/Reddit convention: **part N = question + code +
"CHANGES FROM PART N-1"**, so the progression matches how an interviewer escalates.

```sh
cd ~/robinhood-preparation/coding
javac ServiceLoadFactor.java && java ServiceLoadFactor
```

| File | Problem | Verified behaviour |
|---|---|---|
| `ServiceLoadFactor.java` | Service load factor (weighted DAG) | example PASS, isolated node, diamond fan-in, cycle detection |
| `SystemDependencyLoadFactor.java` | Reverse-dependency **reachability** count | example PASS, **C==3 not 4** (de-dup proof), 500-wide fan-in, 1000-deep chain |
| `CountTriggerEvents.java` | Trigger **path** count | example PASS, **diamond: 2 paths vs 3 ancestors**, overflow at 70 diamonds, 100k chain |
| `CoursePrerequisitesTopoSort.java` | Lexicographically smallest topo order | example PASS, **heap 0 1 2 vs queue 0 2 1**, cycle→-1, n=1e5 m=2e5 in 13ms |
| `ReferralCount.java` | Subtree descendant count (forest) | example PASS, forest w/ non-zero roots, **200k path: iterative 8ms vs recursive StackOverflowError** |
| `ReferralLeaderboard.java` | Transitive referral credit + top-3 | A→B→C credits both, tie by id, cycle guards, **8k chain 8.7s quadratic vs 3ms depth-capped** |
| `CloneGraph.java` | Deep copy a graph | example PASS, **true deep-copy check (no shared objects)**, self-loop, 2-cycle, K100 |
| `WordFrequency.java` | Word frequency, 2-level sort | example PASS incl. **lex tie-break**, digits, 9.6MB input: tokenizer 52ms vs split 112ms |
| `ParseValidateRequests.java` | 4-op state machine w/ validation | 24 assertions PASS, **reversed FRIEND_ACK args**, no-partial-mutation proof, 200k ops 53ms |
| `DistributeStringsKLines.java` | Greedy least-loaded k lines | example PASS, **tie→lowest index**, k>n empty lines, **LPT 8 vs in-order greedy 9** |
| `FractionalShares.java` | Fractional position tracking | ⚠️ see below — the `double` claim is subtler than it looks |
| `CalendarBooking.java` | Interval booking (LC 729) | example PASS, all half-open boundaries, containment case, Cal II + III, **8-thread race → 1 winner** |
| `FindViewById.java` | Android view lookup + unit tests | **19/19 tests**, duplicate ids → pre-order first, NO_ID, depth 1000 |
| `ConstrainedStructure.java` | Bounded structure, concurrency + abuse | ⚠️ see below — two real bugs found by its own tests |

Not written: the three browser-JS problems (chainable DOM, calendar grid, 50 timers) and Swift
`StockManager.placeOrder`, per the agreed Java-appropriate scope. The Android trading skeleton has no
usable public content.

## ⚠️ Codex debate on the DAG trio — read `DEBATE-FINDINGS.md`

The three DAG files were stress-tested against codex on 2026-08-14. It found **three
silent-wrong-answer bugs** that the self-checks did not catch, all now fixed and re-verified:
exact-mode path counting wrapped 2^70 to `0`; `part4AncestorCount` returned all zeros on a cyclic
graph; and `double` drift flagged a node **exactly at capacity** as overloaded.

It also killed a claim in this file's own "pattern worth learning" section — see below — and
established that "no O(V+E) algorithm **exists**" for ancestor counts is false; only "none is
**known**". Four findings remain open by design (cycle localisation needs SCC, the BitSet OOMs at
V=1e5, and the graph input contract is inconsistent across the three files).

## Two findings from writing these

**`FractionalShares` — the obvious claim about `double` is false.** `1.5 - 0.4` is **bit-identical** to
the literal `1.1` (both exactly `1.100000000000000088817841970012523233890533447265625`), so
`(1.5-0.4) == 1.1` is `true` and **the 3-trade sample passes under `double`.** Don't claim the sample
disproves it — that's a false premise an interviewer will correct.

The real argument is **accumulation across the stated 10⁵ instructions**: ten additions of `0.1` gives
`0.9999999999999999`; `BUY 0.1, BUY 0.2, SELL 0.3` leaves `5.55e-17` instead of flat. At 100k
mixed-quantity instructions, **1000 of 1000 tickers end non-zero under `double`, 0 of 1000 under scaled
`long`**. Also learned: a *symmetric* workload (N buys then N sells of the same size) cancels
*exactly*, so a demo needs **mixed** quantities. All runnable in the file.

**`ConstrainedStructure` — its own tests found two real bugs.**
1. ⭐ **Striped locks cannot enforce a global bound.** The first version checked
   `map.size() >= maxEntries` inside a per-key stripe lock and stored **10003** against a cap of 10000
   — two threads on different stripes both read 9999 and both inserted. A global invariant needs a
   global atomic: CAS-reserve the slot (and the bytes), then insert. Now exactly 10000 across repeated
   runs. Being able to say *which invariant each lock protects* is the answer this problem wants.
2. ⭐ **Rate-limiter eviction is a quota bypass, not a cache miss.** A bounded LRU map of token buckets
   looks responsible, but evicting a bucket discards its consumed-token state, so the principal resets
   to full quota. The file demonstrates the bypass, then the fix: reclaim only buckets idle past a full
   refill window (dropping those is information-free), and **fail closed** when nothing is reclaimable.

## The pattern worth learning once

Four problems are one template — Kahn's topological sort with an accumulator. Only *what* you
accumulate changes:

| Problem | Accumulator | Semantics |
|---|---|---|
| Service load factor | `load[v] += load[u] * w` | weighted sum |
| System dependency load factor | `anc[v] |= anc[u]; anc[v].set(u)` | **union — de-duplicates** |
| Count trigger events | `trig[v] += trig[u]` | **sum — multiplies at fan-in** |
| Referral count | `size[parent] += size[v]` | upward sum |

⭐ The trap is rows 2 and 3: reachability de-duplicates (a node reached by two paths counts once), path
counting does not. `CountTriggerEvents.part4AncestorCount` prints both on the same diamond — 2 paths,
3 ancestors — so you can state the difference cleanly under pressure.

Also: **anything with n ≥ 10⁵ must be iterative.** `ReferralCount` proves it — the recursive version
throws `StackOverflowError` on a 200k path while the sweep finishes in 8ms.
