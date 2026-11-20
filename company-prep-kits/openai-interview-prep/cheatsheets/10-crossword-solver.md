# Cheat Sheet — Distributed Crossword Solver

🔗 Diagram: https://excalidraw-production.a.musta.ch#7619d9f7-8dc7-4bae-9461-1880a440fe66

## 1. Summary (in plain terms)
Fill a big crossword grid (~50×50, ~100 slots) from a ~1M-word dictionary. Find *any* valid complete fill — and if none exists, *prove* it (must be complete). Spread the work across many machines.

## 2. Key deep dives (plain terms)

- **Why distribute? The tree is too WIDE, not the nodes too slow.** Each step (which word fits this slot?) is cheap: a bitwise AND over word-bitsets + popcount, all in RAM. The problem is the *number* of branches to explore. So we parallelize the *breadth* of the search — not because a single node is slow, and not because data doesn't fit in memory.

- **Local solving is classic CSP:** pick the most-constrained slot next (MRV), check forward to prune dead ends, keep a per-length word index (small, ~<100MB — the naive global index is ~4GB, reject it), and track used words so you don't repeat.

- **Correctness lives in a durable work-unit LEDGER, not in worker memory.** A "unit" = (a deterministic path to a sub-problem, a seed, a budget of N nodes). Units move AVAILABLE → LEASED(worker, epoch, deadline) → SPLIT / EXHAUSTED / SOLUTION. If a lease expires (worker died), it goes back to AVAILABLE.

- **Units are bounded.** A worker explores up to budget N, then **splits** the rest into new child units in the ledger. So a crash just replays *one small bounded unit* — no giant checkpoint of a deep stack, and writes happen only on splits (no write storm).

- **The safety model = at-least-once + deterministic replay.** A lost message or dead worker means a subtree gets explored *twice, not zero times*. Duplicate work wastes time but is never *wrong*. Completeness and a sound "no solution exists" proof depend only on covering every unit.

- **Knowing you're truly done (termination):** use proper distributed termination detection (Dijkstra-Scholten / Safra) over the unit tree — **not** a "created == completed" counter, which races. "No solution" is provable only when every unit is EXHAUSTED and nothing is AVAILABLE/LEASED/splitting/in-flight.

- **Two lanes on the same fleet:** a **PROOF-SWEEP** lane (systematic, complete, owns correctness, never starved) + a **SAT-RACE** lane (randomized restarts / diverse seeds to find *a* solution fast). First valid solution wins via an atomic claim, then broadcast cancel.

- **High availability:** coordinator is active/standby via a DB lease; taking over bumps a **fencing epoch**, and every command carries it, so a stale old coordinator can't cause damage.

- **Define the objective** (correctness is a hard constraint; minimize median time-to-first-solution and bound the worst-case proof time at reasonable cost). Never say "optimal" without a metric.

## 3. Database schema (DynamoDB — the work-unit ledger)
```
WORK-UNIT LEDGER (this IS where correctness lives):
  PK = UNIT#<unit_id>               # unit_id derived from a deterministic path
  attrs: state (AVAILABLE|LEASED|SPLIT|EXHAUSTED|SOLUTION),
         path,                      # deterministic → replayable
         var_seed, budget_B,
         worker_id, epoch, lease_deadline,   # fencing + lease takeover
         parent_unit_id
  → lease expiry (deadline passed) flips LEASED back to AVAILABLE
  → on budget exhaustion, worker creates child units (SPLIT)

COORDINATOR LEASE (HA / fencing):
  PK = COORD
  attrs: owner, COORD_EPOCH         # bumped on takeover; every command carries it

SOLUTION CLAIM:
  atomic conditional write + generation bump → first valid fill wins → cancel broadcast
```
**Biggest trap to avoid:** never claim an "exact partition over in-memory stacks." That guarantee is false the moment a message is lost or a worker dies. Correctness must rest on the durable ledger + at-least-once + idempotent replay.
