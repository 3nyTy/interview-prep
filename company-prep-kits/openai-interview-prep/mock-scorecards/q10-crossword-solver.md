# Mock Scorecard — Q10: Distributed Crossword Solver

**Date:** 2026-07-16 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/10-crossword-solver.md`](../detailed/10-crossword-solver.md)

**Verdict:** The most abstract problem in the set, attempted while fatigued → **largely taught this round** (needed the design re-explained twice, landing on the "shared to-do list" analogy + concrete ledger rows). Got the precompute-index instinct and "distribute across machines," but the *why-distribute* framing and the entire correctness-under-failure core were taught. **Flagged for a fresh re-attempt.** Good news: the ledger *is* the claim-with-lease pattern already mastered in Q7/Q8 — so re-study should click fast.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ⚠️ **Why distribute = parallelize tree WIDTH** — *fix:* each step is cheap (bitset AND + popcount, μs; state fits in RAM); the tree is astronomically *wide* → distribute for breadth, **not** per-node cost, **not** memory. I framed it as "recursion is slow per step," which is the wrong bottleneck.
2. ✅ **Precomputed candidate index** — got the instinct ("4-letter words with T as the 2nd letter" = a length-local letter-position bitset). *Add:* reject the naive ~4GB global index; **MRV** ordering (most-constrained slot first) + **forward checking / AC-3**; no word reuse.
3. ❌ **Correctness in a durable fenced work-unit ledger** — *fix (taught):* NOT an exact partition over in-memory stacks (a dead worker silently skips a subtree → **unsound UNSAT**). Durable ledger; **work unit = {deterministic path, seed, budget B}**; states `AVAILABLE→LEASED(worker,epoch,deadline)→SPLIT|EXHAUSTED|SOLUTION`; expired lease → `AVAILABLE`. **(Same claim-with-lease ledger as Q7/Q8.)**
4. ❌ **Bounded units → cheap recovery, no write storm** — *fix:* budget (B nodes / T sec); on budget-hit, write leftover branches as **child units** then stop; **writes only on split**, not per node; a death replays **one bounded unit**.
5. ❌ **Safety = at-least-once + deterministic replay** — *fix:* a lost ack / dead worker → subtree explored **twice, never zero**; duplicates wasteful, never wrong. Coverage (every unit terminal) is what guarantees completeness + sound UNSAT.
6. ❌ **Sound "no solution" = distributed termination detection** — *fix:* **Dijkstra-Scholten / Safra**, not a `created==completed` counter (races); declare UNSAT only when **every unit is `EXHAUSTED` and nothing is `AVAILABLE`/`LEASED`/mid-split.**
7. ❌ **Two lanes on one fleet** — *fix:* **PROOF-SWEEP** (systematic, complete, never starved → owns UNSAT) + **SAT-RACE** (randomized restarts + heuristics, fast, incomplete); **first valid fill wins** via atomic claim + generation bump + cancel broadcast.
8. ⚠️ **Coordinator HA** — DynamoDB lease + **`COORD_EPOCH`** fencing (stale epoch rejected). *(Transferable from other designs.)*
9. ⚠️ **Objective** — correctness = hard constraint (complete + sound UNSAT); optimize **p50 time-to-first-solution**, bound **p99 proof time**; never "optimal" without a metric.

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Nodes are cheap (bitset AND + popcount); the tree is astronomically wide, so I parallelize its width."*
- *"Correctness can't live in worker memory — a dead worker would silently skip a subtree and make my UNSAT proof unsound — so it lives in a durable fenced work-unit ledger of bounded, deterministically-replayable units; at-least-once explores a subtree twice, never zero."*
- *"I can only declare no-solution via distributed termination detection (Dijkstra-Scholten), when every unit is exhausted with nothing in flight."*

## Pattern to drill
The **ledger is a pattern I already own** (Q7 settlement, Q8 retries): durable work items, conditional-write claim, lease + reclaim, epoch fencing. The genuinely **new** ideas to internalize: (a) **why-width** framing, (b) **deterministic replay** as the guarantee of coverage, (c) **termination detection** for a sound UNSAT. Re-attempt fresh — the machinery is familiar, only the framing is new.
