# Distributed Crossword Solver — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#7619d9f7-8dc7-4bae-9461-1880a440fe66

## What we're building
Given a crossword grid (~50×50, ~100 word slots) and a dictionary (~1M words), fill the whole grid with valid interlocking words. Find **any** complete valid fill — and if none exists, **prove** it (the search must be *complete* and able to soundly report UNSAT). Spread the work across many machines.

This is a Constraint Satisfaction Problem (CSP), distributed. The interview is testing whether you understand *why* you'd distribute it and how to keep a distributed search **correct** under failures — not crossword trivia.

## The lead insight (say this first)
Each individual search step is **cheap**: "which words fit this slot given the letters already placed?" is a bitwise-AND of precomputed letter-position bitsets plus a popcount — microseconds, all in RAM. So a single machine isn't slow *per step*. The problem is that the **search tree is astronomically WIDE** — exponentially many partial fills to explore.

Therefore: **we distribute to parallelize the tree's width**, NOT because each node is expensive, and NOT because the state doesn't fit in memory (it does). Getting this framing right signals you understand the actual bottleneck. (Be honest, too: a *loose* 1M-word grid might solve in milliseconds on one box — distribution earns its keep on adversarial dense grids and the explicit UNSAT-proof requirement.)

## The local solver (what each worker runs)
Standard CSP search, tuned:
- **MRV variable ordering** — always fill the most-constrained slot next (fewest candidate words), so you fail fast and prune early.
- **Forward checking** (shallow AC-3) — after placing a word, check neighbors still have options; abandon dead branches immediately.
- **Length-local bitset index** — candidate words indexed by length and letter-position, kept small (~<100MB). *Call out and reject* the naive global word-ID index (~4GB) — it doesn't fit nicely and you don't need it.
- **No word reuse** — track used words via the path's used-set.

## The heart of it: correctness lives in a durable ledger, not in worker memory
The tempting-but-wrong design is "partition the search tree across workers, each explores its slice in memory." That invariant — *an exact partition over in-memory stacks* — is **false the moment a message is lost or a worker dies.** A lost slice means a subtree is never explored, so your "no solution exists" proof is now unsound. This was the single biggest flaw to avoid.

**The fix — a durable, fenced, idempotent work-unit ledger (DynamoDB):**
- A **work unit** = `{a deterministic path to a sub-problem, a variable seed, a budget B}`. Because the path is deterministic, any worker can *replay* it identically.
- Units have states: `AVAILABLE → LEASED(worker, epoch, deadline) → SPLIT | EXHAUSTED | SOLUTION`. A lease that expires (worker died) flips the unit back to `AVAILABLE`.

## Bounded units → cheap recovery, no write storms
Each unit has a **budget** (explore up to B nodes or T seconds). When a worker hits the budget without finishing, it **splits** the remaining frontier into new child units written durably to the ledger, and stops. Consequences:
- A worker death just means **one bounded unit** gets replayed by someone else — no need to checkpoint a deep search stack.
- Writes happen **only on splits**, not per node — so no write amplification despite billions of search nodes.

## The safety model: at-least-once + deterministic replay
- If an ack is lost or a worker dies, the unit is re-leased and explored **again**. So a subtree is explored **twice, not zero times.** Duplicate work is *wasteful but never incorrect*.
- Completeness and a sound UNSAT proof depend only on **full coverage** — every unit eventually reaches a terminal state. At-least-once guarantees coverage; determinism guarantees a replay covers the same subtree.

This is the crossword analogue of the recurring theme: **don't claim exact-once partitioning; build correctness on a durable ledger + at-least-once + idempotent replay.**

## Knowing when you're truly done (distributed termination)
You can't just count `units_created == units_completed` — counters race under concurrency and message reordering. Use proper **distributed termination detection** (Dijkstra-Scholten / Safra) over the unit tree. You may declare **UNSAT** only when: every unit is `EXHAUSTED`, there are zero `AVAILABLE`/`LEASED`/splitting units, and no split is in flight. Until that exact condition, you cannot soundly say "no solution."

## Two policy lanes over the same worker fleet
- **PROOF-SWEEP lane:** systematic work-stealing DFS that guarantees completeness. It owns correctness and is **never starved** — this is what lets you prove UNSAT.
- **SAT-RACE lane:** incomplete but fast — randomized-restart DFS with diverse seeds, plus min-conflicts / simulated-annealing local search. Capped resources. Its job is to crush the median time-to-first-solution on satisfiable boards.
- The first **valid** solution wins: an atomic conditional claim + a generation bump, then a cancel broadcast to stop everyone.

(The heavy-tail / Gomes-Selman randomized-restart portfolio is an *optional accelerator*, not load-bearing — be honest that it helps p50 but the proof-sweep is what guarantees correctness.)

## High availability (coordinator)
The coordinator is active/standby via a **DynamoDB lease** (conditional write + TTL). Acquiring the lease **bumps a `COORD_EPOCH`** (a fencing token). Every command and write carries the epoch; anything with a stale epoch is rejected → no split-brain damage if an old coordinator comes back. ("No ZooKeeper needed" is cosmetic; the real point is *fencing + a correct state machine.*)

## Define the objective (don't say "optimal")
State it explicitly: **correctness is a hard constraint** (complete + sound UNSAT); the optimization target is **minimize p50 time-to-first-solution and bound p99 proof time at reasonable cost.** Never say "optimal" without a metric — a Staff interviewer will ask "optimal with respect to what?"

## Data model (DynamoDB — the work-unit ledger)
```
WORK-UNIT LEDGER (correctness lives here):
  PK = UNIT#<unit_id>                 # derived from a deterministic path
  attrs:
    state (AVAILABLE|LEASED|SPLIT|EXHAUSTED|SOLUTION),
    path,                             # deterministic → replayable
    var_seed, budget_B,
    worker_id, epoch, lease_deadline, # fencing + lease takeover
    parent_unit_id
  → lease deadline passed → flips LEASED back to AVAILABLE
  → on budget exhaustion → worker writes child units (SPLIT); writes only on split

COORDINATOR LEASE (HA / fencing):
  PK = COORD
  attrs: owner, COORD_EPOCH           # bumped on takeover; every command carries it

SOLUTION CLAIM:
  atomic conditional write + generation bump → first valid fill wins → cancel broadcast
```

## AWS / infra
API Gateway + Lambda (async submit/poll); coordinator + workers on ECS/Fargate (Spot is fine — that's the whole point of cheap replayable units); gRPC bidirectional coordinator↔worker; the word index baked into the container image (avoids an S3 cold-start stampede); CloudWatch. At extreme scale (1000s of workers), name — don't build — hierarchical coordinators + peer-to-peer bulk transfer.

## If the interviewer pushes further
- *"Why distribute at all if nodes are cheap?"* Because the tree is too wide — you parallelize breadth. (And admit a loose board may not even need it; dense boards + UNSAT proofs do.)
- *"Why not partition the tree across workers' memory?"* That invariant breaks under message loss / worker death → unsound UNSAT. Correctness must live in a durable ledger with at-least-once replay.
- *"How do you know there's no solution?"* Distributed termination detection (Dijkstra-Scholten), not a created==completed counter — and only when every unit is EXHAUSTED with nothing in flight.
- *"Worker dies mid-search — what's lost?"* One bounded unit, replayed deterministically. No deep-stack checkpoint needed.
- *"Isn't duplicate work a correctness problem?"* No — at-least-once means a subtree is explored twice, never zero times; duplicates waste time but never corrupt the result.
