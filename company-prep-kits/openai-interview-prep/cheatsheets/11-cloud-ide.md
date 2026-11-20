# Cheat Sheet — Cloud IDE

> Debate-validated (Codex, high effort): v1 "right shape, mechanisms undesigned" → v2 below incorporates the fixes. Same pattern as the other prepped designs.

## 1. Summary (in plain terms)
A dev environment in your browser (like GitHub Codespaces / Replit): you open a repo, edit files, use a terminal, get autocomplete, and run code — but everything heavy runs on a server next to your files, not in the browser. Idle workspaces shut down to save money and wake back up with your files intact. The hard parts are: not losing uncommitted work, never letting two servers fight over one workspace, and safely running untrusted code.

## 2. Key deep dives (plain terms)

- **Server-side IDE backend, thin browser.** The browser runs the editor UI (Monaco); the actual file ops, terminal (PTY), language servers, and builds run in a per-user **workspace pod** on Kubernetes, right next to the files. One multiplexed **WebSocket** carries editor + terminal + LSP, with **per-channel flow control** (a noisy terminal must not starve file-saves or control messages).

- **⚠️ Fencing on workspace ownership (the #1 fix).** Two pods writing one filesystem = corruption. So a workspace has a **strongly-consistent lease with a monotonic fencing token** (DynamoDB is the authority; Redis is just a disposable cache). The old owner must be **killed and confirmed dead before** the new one becomes routable. The gateway rejects any pod presenting a stale epoch. (This is the same single-writer + epoch-fencing pattern as Slack/Chess/CI-CD.)

- **Don't trust "filesystem = durable."** EBS is block storage, not crash-consistent app state — dirty editor buffers and unsaved files die on node failure. So: **continuously persist editor changes to a durable log/object store** (low RPO), and use EBS snapshots only for backup/DR. Git is *export*, not your durability story.

- **Hibernate/resume is a transactional state machine, not a flag.** `RUNNING → DRAINING → QUIESCED → DETACHING → HIBERNATED → ATTACHING → STARTING`, all CAS transitions with epochs. This kills the race where a reconnecting user hits a pod mid-hibernation, or a detach hangs while a new pod starts elsewhere.

- **EBS is AZ-locked → pin the workspace to an AZ.** A volume in `us-east-1a` can't attach to a pod in `1b`. So keep **per-AZ warm pools** and capacity; AZ loss = restore-from-snapshot into another AZ (a *degraded*, not instant, path — say so).

- **Be honest about cold start.** "A few seconds" is a lie if it includes image pull + repo clone + volume attach + dependency install + **LSP indexing** (tens of seconds on big repos). Publish **separate SLOs**: editor-connected vs files-visible vs terminal-ready vs language-intelligence-ready. Prebuild images, cache deps, pre-index.

- **Security = hostile multi-tenancy, not normal EKS.** Pick **one** sandbox (Firecracker microVM) and define the trusted computing base. **Egress lockdown is multi-layer**: block IMDS/IMDSv2, link-local, IPv6 metadata, RFC1918, the K8s API, and your own Redis/DynamoDB endpoints; DNS-proxy policy to stop tunneling. Add an **abuse model** (crypto-mining/DDoS/scraping): quotas, outbound rate limits, fast suspension. **Secrets** via a short-lived scoped broker, never baked onto the volume.

- **The gateway is an authz boundary, not a dumb proxy.** It must authorize the user→workspace on *every* connection and validate routing epochs — otherwise one auth bug = cross-tenant file/terminal access.

- **Mass reconnect (deploy / AZ blip) will stampede** lookups + pod starts + volume attaches. Defend with client jitter, reconnect budgets, admission control, and per-workspace lifecycle dedupe. Shard the control plane (cloud APIs throttle on attach/detach at scale).

## 3. Data model (control-plane state)
```
WORKSPACE REGISTRY / LEASE (DynamoDB = strongly-consistent authority):
  PK = WORKSPACE#<workspace_id>
  attrs: lifecycle_state (RUNNING|DRAINING|QUIESCED|DETACHING|HIBERNATED|ATTACHING|STARTING),
         owner_pod, owner_epoch,        # fencing token; gateway validates; CAS transitions
         az,                            # workspace pinned to one AZ (EBS is AZ-scoped)
         ebs_volume_id, last_snapshot_id
  → all lifecycle transitions = conditional writes (CAS on state + epoch)

ROUTING:  Redis = disposable cache of workspace_id → pod;  DynamoDB lease is the truth.
          Gateway rejects any pod presenting epoch < current.

DURABILITY (two layers, separated):
  - continuous editor-change log → object store   (low RPO; protects uncommitted work)
  - EBS snapshot → S3                              (backup / DR / cold-start seed only)
```

**AWS:** EKS + Karpenter, EC2 + Firecracker, EBS + snapshots, S3, DynamoDB, ElastiCache, CloudFront, NLB/ALB, Cognito, ECR, CloudWatch.

**Biggest trap:** claiming "filesystem-as-state" + periodic snapshots meets "no lost work," and assuming EBS single-attach alone prevents split-brain. It doesn't — you need continuous durability + an explicit fencing lease.
