# Mock Scorecard — Q11: Cloud IDE

**Date:** 2026-07-16 · **Mode:** B (live mock, warm rep — punchlines seen just prior) · **Full write-up:** [`../detailed/11-cloud-ide.md`](../detailed/11-cloud-ide.md)

**Verdict:** Security instincts strong and automatic; once pointed at the right layer I produced every key answer (kill-before-route, CAS state machine). Growth edge is one **conceptual** idea worth internalizing: *fencing only works where the resource **checks** the token* — for a filesystem/object store you fall back to **physical serialization** (volume detach / kill-before-route). That unifies Sora, Cloud IDE, and the S3-artifact designs. (Also some focus drift — answered "durability" for two different questions.)

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **Security cluster (unprompted)** — auth boundary (only the authorized user), resource isolation (microVM), **egress** lockdown (workspace can't reach internal cloud resources). *(Called it "ingress" — it's egress.)*
2. ⚠️→✅ **Never two servers on one workspace (the #1 problem)** — I first applied the CI/CD job-fencing to the **build job**; the real risk is two **pods** on the **workspace filesystem (EBS)**. *Key insight (got after a probe):* **kill-before-route** — because **EBS/S3 don't check a fencing token** (fencing only works where the resource *checks* it — the DB), you must **physically serialize**: bump epoch → gateway stops routing to A → kill A + **confirm the volume detached** → attach to B → B routable. Token fences *routing*; volume detach fences the *filesystem*.
3. ✅ **Durability — continuous stream + recover onto a fresh volume** — right core idea. *Refinement:* stream at the **editor-change (app) layer**, not the filesystem — else you miss the **unsaved buffer** (what users care most about); EBS = durable *blocks*, not app *state*; Git only has committed work. **Two layers:** (1) continuous change-log → object store (low RPO, primary), (2) EBS snapshots → S3 (DR / cold-start seed only).
4. ✅ **Hibernate/resume without races** — got the **CAS transactional state machine** with a nudge: `RUNNING→DRAINING→QUIESCED→DETACHING→HIBERNATED→ATTACHING→STARTING→RUNNING`; each transition commits only if current state == expected, so "begin hibernate" and "begin resume" can't both win. (Naive `is_idle` flag → half-detached volume + two pods.)
5. ⚠️ **AZ pinning** — EBS is AZ-scoped → pin the workspace to an AZ, **warm pools per AZ**; cross-AZ recovery = slower snapshot-restore (say so). *Not drilled.*
6. ⚠️ **Cold-start honesty** — publish milestones (container→editor→files→terminal→language-intelligence); warm pool only saves the boot part. *Not drilled.*
7. ⚠️ **One multiplexed WebSocket** → per-channel **flow control** (a noisy terminal mustn't starve saves/control). *Not drilled.*
8. ⚠️ **Mass reconnect** → jitter + reconnect budgets + **per-workspace lifecycle dedupe** (5 reconnects ≠ 5 pods) + shard the control plane (EC2/EBS APIs throttle). *Not drilled.*
9. ✅ **Gateway = security boundary** — validate the token + authorize this user→this workspace on every connection (implied by the auth + routing discussion).

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Exactly-one-owner via a fencing lease — but because EBS won't reject a stale writer, it's kill-before-route: bump the epoch, kill the old pod, confirm the volume detached, then attach the new one."*
- *"Durability is two layers, and the continuous stream is at the editor-change layer (to catch unsaved buffers), not the disk; snapshots are DR only."*
- *"The workspace lifecycle is a CAS state machine, so a hibernate and a simultaneous resume can't both win."*

## Pattern to drill
**Fencing only works where the resource CHECKS the token** (a DB). For **dumb storage** (EBS, S3) you can't fence the write — you fall back to **physical serialization**: volume detach + **kill-before-route** (Cloud IDE), or attempt-scoped immutable keys + DB-promoted pointer (Sora/S3). Same lesson, two forms. Also: **answer the exact question asked** — I gave the durability answer to a lifecycle-race question.
