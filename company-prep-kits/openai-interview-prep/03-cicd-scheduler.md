# Design a Multi-Tenant CI/CD Workflow Scheduler

> OpenAI Staff SWE sample question: linear job sequences, git-push triggered, exactly-once emphasis. Stress-tested via debate skill (Codex, 2 rounds) — went from *"would not pass"* to *"lean pass."*
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#fd36820f-e85f-4a0f-a7e8-686978be26c8

## The crux (and the #1 debate lesson)
**Do NOT claim "exactly-once execution."** Claim:
- **exactly-once *committed* result per step (fenced)**, plus
- **best-effort single execution (revocable lease).**

Fencing prevents stale *commits*, not concurrent *execution*. Arbitrary user side-effects can only be made idempotent **by the user**, via a stable key `run_id#seq#operation` (NOT `attempt`).

## Core mechanism
- **DynamoDB = source of truth.** All transitions are conditional-write **CAS**.
- `attempt == fencing_token`, minted via atomic `ADD` (use the returned value).
- **SQS Standard = wakeup only**, deleted on claim — **never** the liveness owner.
- **Liveness = DynamoDB lease + Reaper.**
- **Scheduling = DynamoDB Streams → Lambda** (CDC, transactional-outbox), with a periodic **Reconciler** as the durable backstop (repairs lost stream events / stranded tenants / unfinalized runs).

### AWS stack
API Gateway, ECS Fargate, DynamoDB + Streams, SQS, Lambda, EKS + Karpenter, Firecracker microVMs, S3, ElastiCache, API Gateway WebSocket.

## Debate-hardened fixes worth remembering
- **Sharded GSI-lease** (`RUNNING#bucket`, not low-cardinality `status=RUNNING`).
- **Cancellation as a first-class state:** `RUNNING → CANCELLING → CANCELLED` + a cancel reaper; the claim CAS includes `cancel_requested == false`.
- **WFQ = Deficit Round Robin** sharded by tenant + fairly-allocated global capacity tokens.
- **`USER_FAILED` vs `INFRA_FAILED`** classification.
- **Per-run STS/OIDC** + deny-by-default egress + fork policy + tenant-isolated signed cache.
- **Artifacts attempt-scoped** with fenced promotion.
