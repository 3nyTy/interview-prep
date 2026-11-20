# Cloud IDE — Detailed Explainer (plain English)

> Debate-validated (Codex). Pattern: v1 had the right shape but "named the risks without designing the mechanisms" — the fixes below are what make it Staff-level.

## What we're building
A full development environment in your browser — like GitHub Codespaces, Replit, or Gitpod. You open a repo, edit files in a real editor, use a terminal, get autocomplete and go-to-definition, and run your code. The heavy lifting happens on a server next to your files, not in your browser. Idle workspaces shut down to save money and resume later with your work intact.

## Scale
~10M registered workspaces, ~500K active at peak, bursty by time zone. Most workspaces are idle at any given moment — which is exactly why "scale to zero when idle" matters so much for cost.

## The overall shape
- **Browser:** runs the editor UI (Monaco / VS Code Web). It's a *thin* client — it shows text and sends keystrokes; the real work is server-side.
- **One WebSocket** carries everything multiplexed: editor/file operations, the terminal stream (PTY), language-server (LSP) traffic, and control messages. Because they share one pipe, you need **per-channel flow control** — otherwise a terminal spewing output could starve your file-saves or control messages.
- **Connection gateway:** authenticates you, finds *your* workspace pod, and proxies your WebSocket to it.
- **Workspace pod (on Kubernetes):** runs the server-side IDE backend, your shell, the language servers, and your build/run processes. Your files live on a **per-workspace disk (EBS volume)** attached to that pod.
- **Orchestrator / control plane:** starts and stops pods, hibernates idle ones, keeps a warm pool to hide cold-start, autoscales the nodes.

## The hardest problem: never let two servers fight over one workspace
This is the #1 thing that makes or breaks the design. If two pods ever write to the same workspace filesystem at once, you get corruption. People assume "EBS only allows one attachment, so we're safe." **That's not enough** — a volume can fail to detach, a stale process can linger, or the control plane's records can disagree about who owns the workspace.

**The fix is a fencing lease** (the same idea as in the chat and CI/CD designs):
- Each workspace has a **strongly-consistent lease** with a **monotonic fencing token** (an ever-increasing number), stored in **DynamoDB** (the authority). Redis is only a fast cache, never the truth.
- When ownership moves to a new pod, the token bumps. The gateway checks the token on every connection and **rejects any pod showing a stale token**.
- Critically: you **kill the old owner and confirm it's dead *before* the new owner becomes routable.** "Kill-before-route." Otherwise both could be live for a moment.

## "The filesystem is the state" is a trap
It's tempting to say "the work lives on the EBS volume, and Git is the backup, so we're durable." Two problems:
- **EBS gives you durable *blocks*, not a durable *application state*.** Unsaved editor buffers, a half-written file, language-server caches, and in-flight saves can be lost or corrupted if the pod/node dies. And uncommitted work is *exactly* what users care about most.
- **Git is not a durability mechanism** — most of what's at risk is uncommitted.

**The fix: two separate layers.**
1. **Continuous durability for user work:** stream editor changes to a durable log / object store as they happen (low "how much could I lose" window). This protects uncommitted work.
2. **EBS snapshots → S3:** only for backup / disaster recovery / seeding a cold start. Not your primary save path.

## Hibernate and resume without races
Idle workspaces shut down (the pod is terminated, the volume detached) and wake up later. The naive "set a flag" approach races badly: a user reconnects just as the idle-reaper decides to hibernate; a detach hangs while a new pod starts elsewhere.

**The fix:** model the lifecycle as a **transactional state machine** with compare-and-swap transitions and epochs:
```
RUNNING → DRAINING → QUIESCED → DETACHING → HIBERNATED → ATTACHING → STARTING → RUNNING
```
Each transition only commits if the current state is what we expect, so two conflicting actions can't both win. A reconnecting user during hibernation cleanly waits for the resume path rather than colliding with the shutdown.

## EBS is tied to one Availability Zone
An EBS volume in zone `us-east-1a` **cannot** attach to a pod in `us-east-1b`. Consequences:
- **Pin each workspace to an AZ**, and keep **warm pools per AZ** (a warm pool in the wrong zone is useless).
- If a whole AZ goes down, recovering those workspaces means **restoring from a snapshot into another AZ** — that's a *degraded*, slower path, not "back in a few seconds." Say that honestly.

## Be honest about cold-start time
"Workspaces start in a few seconds" is a fiction if it includes: pulling a multi-GB container image, booting the sandbox, attaching the volume, cloning the repo, installing dependencies, and **indexing the codebase for the language server** (which alone can take tens of seconds to minutes on a big repo). So publish **separate readiness milestones**: container-ready → editor-connected → files-visible → terminal-ready → language-intelligence-ready. Prebuild images, cache dependencies, pre-index where possible. The **warm pool** only saves the boot portion — a warm generic pod still needs *your* volume, secrets, repo, and extensions.

## Security: this is hostile multi-tenancy
You're running **arbitrary untrusted user code** next to other tenants. That's not a normal Kubernetes workload; it's adversarial.
- **Pick one strong sandbox** — Firecracker microVM per workspace — and define exactly what's in the trusted computing base (no privileged host mounts, seccomp/AppArmor on).
- **Lock down egress at multiple layers**, not just "block the metadata endpoint." Block: the cloud metadata service (IMDS/IMDSv2), link-local addresses, IPv6 metadata paths, private IP ranges (RFC1918), the Kubernetes API, and your own internal Redis/DynamoDB endpoints. Add a DNS proxy policy so user code can't tunnel data out via DNS.
- **Resource limits via cgroups:** CPU, memory, process count (stop fork-bombs), open files, disk bytes, inode count, log volume, and terminal output rate. Language servers are memory/CPU hogs; a TypeScript monorepo can crush a small pod.
- **Abuse model:** an IDE that runs arbitrary code is a free crypto-mining / DDoS / scraping platform unless you add trust tiers, outbound rate limits, abuse detection, and fast workspace suspension.
- **Secrets** (Git tokens, cloud creds) come from a **short-lived, scoped secrets broker**, never baked onto the volume or injected into untrusted processes.

## The gateway is a security boundary, not a dumb pipe
It must authorize *this user → this workspace* on **every** connection and validate the routing token. If it blindly trusts a pod address from the routing store, a poisoned/stale entry becomes cross-tenant access or account takeover.

## Surviving mass reconnects
A deploy, an AZ blip, or an auth recovery can make hundreds of thousands of clients reconnect at once — triggering a stampede of routing lookups, pod starts, and volume attaches. Defend with: client-side **jitter**, **reconnect budgets**, admission control, cached negative responses, and **per-workspace lifecycle dedupe** (so 5 reconnects don't start 5 pods). Also **shard the control plane** — EC2/EBS APIs throttle, and Kubernetes controllers lag, when you're attaching/detaching hundreds of thousands of volumes.

## Multiplayer (stretch)
Real-time collaboration uses a CRDT (like Yjs), but note the document service must be **authoritative for open buffers** with defined save ordering — otherwise the in-memory collaborative state and the on-disk files diverge when someone also edits via the terminal or runs git.

## Control-plane data model
```
WORKSPACE LEASE / REGISTRY (DynamoDB = strongly-consistent authority):
  PK = WORKSPACE#<workspace_id>
  attrs:
    lifecycle_state  # RUNNING|DRAINING|QUIESCED|DETACHING|HIBERNATED|ATTACHING|STARTING
    owner_pod, owner_epoch     # fencing token; gateway validates; CAS transitions
    az                         # workspace pinned to one AZ (EBS is AZ-scoped)
    ebs_volume_id, last_snapshot_id

ROUTING:  Redis = disposable cache (workspace_id → pod); the DynamoDB lease is the truth.
          Gateway rejects any pod presenting epoch < current.

DURABILITY (two layers):
  - continuous editor-change log → object store   (protects uncommitted work; low RPO)
  - EBS snapshot → S3                              (backup / DR / cold-start seed only)
```

**AWS:** EKS + Karpenter, EC2 + Firecracker, EBS + snapshots, S3, DynamoDB, ElastiCache, CloudFront, NLB, Cognito, ECR, CloudWatch.

## If the interviewer pushes further
- *"EBS is single-attach — isn't that enough to prevent two writers?"* No. Failed detach, lingering processes, and disagreeing control-plane records can still produce two live owners. You need an explicit fencing lease + kill-before-route.
- *"Isn't the volume enough for durability?"* No — dirty buffers and unsaved files die on node failure. Continuously persist user changes; snapshots are DR only.
- *"Can a workspace start anywhere?"* No — EBS is AZ-scoped, so it's pinned to an AZ; cross-AZ recovery is a slower snapshot-restore path.
