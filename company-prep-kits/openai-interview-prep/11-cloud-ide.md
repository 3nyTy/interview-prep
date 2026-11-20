# Design a Cloud IDE

> OpenAI Staff SWE prep. Validated via the cross-LLM debate skill (Codex, high effort, 1 round). Verdict: v1 had the right shape and named the right risks, but "named the risks without designing the mechanisms that make them safe — at Staff level that gap is the interview failure." v2 below closes them.

## Problem
Browser-based dev environment (Codespaces / Replit / Gitpod / VS Code Web). Open a repo, edit, terminal, language intelligence, run code, persist across sessions, optional multiplayer. AWS-native.

## Scale assumptions
~10M registered workspaces, ~500K concurrently active, bursty by timezone, each active workspace = a long-lived connection. Most workspaces idle at any moment.

## Architecture (v2)

### Client
Browser SPA running Monaco / VS Code Web. Editor UI is local; heavy work server-side. **One multiplexed WebSocket** carries editor/file RPC + terminal PTY + LSP + control, with **per-channel flow control / priority** so terminal spam can't starve saves or control messages.

### Edge / routing
CloudFront (TLS edge) → NLB → **connection gateway** fleet. The gateway is an **authorization boundary, not a dumb proxy**: it authenticates (Cognito/JWT) and authorizes user→workspace on **every** connection, resolves `workspace_id → pod` from the lease store, validates the **routing epoch**, and proxies the WS. Reconnect re-resolves.

### Workspace runtime
One **workspace pod per active workspace** on EKS: server-side IDE backend (openvscode-server), PTY, language servers, build/run. Per-workspace **EBS volume** holds the project FS.

### Ownership & lifecycle (the #1 fix)
- **Strongly-consistent per-workspace lease with a monotonic fencing token.** DynamoDB is the authority; Redis is a disposable cache. Old owner is **killed and confirmed dead before** a new owner becomes routable; the gateway rejects any pod presenting a stale epoch. (Single-writer + epoch fencing — same family as Slack / Chess / CI-CD.)
- **Lifecycle = transactional state machine** with CAS transitions + epochs:
  `RUNNING → DRAINING → QUIESCED → DETACHING → HIBERNATED → ATTACHING → STARTING → RUNNING`.
  Kills the reconnect-vs-reaper race, the hung-detach-while-restarting-elsewhere race, and double-attach.

### Durability (two layers — separated)
- **Continuous editor-change persistence** to a durable log/object store (low RPO) — protects *uncommitted* work, which is the state users actually care about.
- **EBS snapshots → S3** for backup / DR / cold-start seed only.
- Git remote is *export*, not the durability story. ("Filesystem-as-state" alone is a false durability claim: EBS is block-durable, not crash-consistent for dirty buffers / unsaved files.)

### AZ placement
EBS is **AZ-scoped + single-attach**, so each workspace is **pinned to an AZ**. Maintain **per-AZ warm pools** and capacity buffers. AZ loss → restore-from-snapshot into another AZ = a *degraded* recovery path (not "a few seconds") — state it honestly.

### Cold-start honesty
Don't claim "a few seconds" — that ignores image pull + sandbox boot + EBS attach + repo clone + dependency install + **LSP indexing** (tens of seconds–minutes on big repos). Publish **separate SLOs**: container-ready / editor-connected / files-visible / terminal-ready / language-intelligence-ready. Prebuild images, cache deps, pre-index. Warm pool helps attach-to-editor time, **not** full developer-readiness (the generic pod still needs the user's volume/secrets/repo/extensions).

### Security (hostile multi-tenancy)
- **One** sandbox architecture: **Firecracker microVM** per workspace (document the TCB; no privileged host mounts/devices; seccomp/AppArmor).
- **Multi-layer egress lockdown**: block IMDS/IMDSv2, link-local, IPv6 metadata, RFC1918, the K8s API, and your own Redis/DynamoDB endpoints; DNS-proxy policy against tunneling. Enforce at sandbox netns + CNI + VPC + DNS + AWS endpoint policies.
- **Resource isolation** via cgroups: CPU/mem/PID/open-files/disk-bytes/inode/log-volume/PTY-output-rate (LSPs are memory/CPU hogs; user code can fork-bomb or spew infinite output).
- **Abuse model**: trust tiers, quotas, outbound rate limits, per-destination reputation, abuse detection, fast suspension (an IDE that runs arbitrary code is a crypto-mining / DDoS / scraping platform otherwise).
- **Secrets** via a scoped, short-lived, auditable broker — never persisted on the volume or injected into untrusted processes.

### Reconnect / control-plane resilience
- WS sessions don't migrate for free: define **per-channel reconnect semantics** — editor buffers recover, file writes idempotent, terminal/debug sessions best-effort (may die).
- **Mass reconnect** (gateway deploy, AZ blip, auth recovery) → stampede of lookups + pod starts + attaches. Defend with client jitter, reconnect budgets, admission control, cached negative responses, **per-workspace lifecycle dedupe**.
- **Shard the control plane** by region/AZ/tenant-hash; rate-limit lifecycle transitions (EC2/EBS APIs throttle; K8s informers lag). Workspace lifecycle is a **first-class trace** (correlation IDs across gateway / orchestrator / node-agent / EBS API / pod).

### Collaboration (stretch)
CRDT (Yjs); the **document service is authoritative for open buffers**, with defined save ordering + reconciliation against external file mutations (terminal edits, git ops, file watchers) — in-memory CRDT and on-disk files otherwise diverge.

### AWS stack
EKS + Karpenter, EC2 + Firecracker, EBS + snapshots, S3, DynamoDB (lease/registry authority), ElastiCache Redis (disposable cache), CloudFront, NLB/ALB, Cognito, ECR, CloudWatch.

## Top deep-dives an interviewer drills
1. Split-brain on the EBS volume → fencing lease + kill-before-route.
2. Uncommitted-work durability → continuous change log, not just snapshots.
3. Hibernate/resume races → transactional lifecycle state machine.
4. Untrusted-code isolation + egress completeness → microVM + multi-layer egress.
5. Cold-start honesty → separate readiness SLOs.

## Debate scorecard
Codex raised 8 "critical" items (all accepted): false durability story, no EBS fencing, racey hibernate/resume, AZ locality, snapshots ≠ no-lost-work, hand-wavy sandbox choice, gateway-as-authz-boundary, incomplete egress. Plus significant: latency realism, cold-start fiction, warm-pool limits, 500K-pod economics, control-plane API limits, mass-reconnect stampede, routing consistency, sticky-session migration, LSP resource isolation, abuse model, secrets, CRDT-vs-FS divergence. All incorporated above. "Broad shape is defensible; the gap was mechanisms" — which is the v1→v2 story.
