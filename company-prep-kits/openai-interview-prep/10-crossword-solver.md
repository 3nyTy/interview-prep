# Design a Distributed Crossword Puzzle Solver

> OpenAI Staff SWE sample question: ~50×50 board, ~100 slots, ~1M-word dict, find ANY valid fill, must be COMPLETE + prove UNSAT. Validated via debate workflow (Codex Round-1 returned "fail as-is" with 6 criticals; Round-2 revision closed them).
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#7619d9f7-8dc7-4bae-9461-1880a440fe66

## Lead insight (the whole point of the question)
Each search node is **CHEAP** (in-RAM length-local bitset `AND` + popcount → candidates), so single-machine isn't slow-per-node — **the tree is too WIDE.** Distribute to parallelize **width**, NOT to make nodes affordable, NOT because state doesn't fit in RAM.

## Validated architecture

### Local solver
- MRV var-ordering + forward checking (AC-3 shallow only).
- **Length-LOCAL bitset index** (~<100MB; the naive global-word-ID index is ~4GB — call this out and reject it).
- Model word-reuse (no repeats) via a used-set from the path.

### Correctness lives in a durable, FENCED, IDEMPOTENT work-unit LEDGER (DynamoDB)
- NOT per-node Redis/Postgres writes.
- Unit = `{deterministic path, var-seed, budget B}`.
- States: `AVAILABLE → LEASED(worker, epoch, deadline) → SPLIT | EXHAUSTED | SOLUTION`; lease-expiry → `AVAILABLE`.

### BOUNDED units (budget B nodes/time)
- On budget, **SPLIT** the remaining frontier into durable child units.
- No deep-stack checkpoint needed → worker death replays **ONE** bounded unit.
- No write amplification (writes only on split).

### AT-LEAST-ONCE coverage + deterministic replay = the safety model
- Lost ack / dead worker → a subtree is explored **TWICE, not ZERO**.
- Duplicate work is wasteful, never incorrect.
- Completeness + sound UNSAT depend only on full coverage.

### Termination = distributed termination detection (Dijkstra-Scholten / Safra)
- Over the unit tree — **NOT** a `created == completed` counter (counters race).
- UNSAT iff every unit `EXHAUSTED` & 0 `AVAILABLE`/`LEASED`/`SPLITTING` & no in-flight split.

### Two policy lanes over the same fleet
- **PROOF-SWEEP**: systematic work-stealing DFS, COMPLETE, owns correctness, never starved.
- **SAT-RACE**: incomplete, capped — randomized-restart DFS diverse seeds + min-conflicts/SA, crushes p50.
- First VALID solution wins → atomic conditional claim + generation bump → cancel broadcast.

### HA
- Coordinator active/standby via DynamoDB lease (conditional write + TTL); acquire bumps `COORD_EPOCH` (fencing token).
- **Every** command/write carries the epoch; stale-epoch rejected → no split-brain damage.
- "No ZooKeeper" is cosmetic; the point is fencing + a correct state machine.

### AWS
- API GW + Lambda (async submit/poll); coordinator + workers on ECS/Fargate Spot; gRPC bidi coordinator↔worker; index baked into the container image (no S3 cold-start stampede); CloudWatch.
- Scale-out (named, not built): 1000s of workers → hierarchical coordinators + P2P bulk transfer.

## Key debate-won points
- **Define the OBJECTIVE** (correctness as a hard constraint; minimize p50 time-to-first-solution + bound p99 proof time at reasonable cost) — never say "optimal" without a metric.
- Heavy-tail / Gomes-Selman portfolio is an **OPTIONAL accelerator, NOT load-bearing** (be honest: a loose 1M-dict board may solve in ms single-box; distribution earns its keep on adversarial dense grids + the explicit UNSAT-proof requirement).
- Don't over-apply the generic system-design template; don't get trapped optimizing the algorithm.

## Biggest flaw to avoid (Codex's #1)
**Never claim an "exact partition over in-memory stacks"** — that invariant is false under message loss / worker death / failover. Make correctness = durable ledger + at-least-once + idempotent replay instead.
