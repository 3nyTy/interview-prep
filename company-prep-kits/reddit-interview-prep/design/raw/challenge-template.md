# Reusable Adversarial Challenge Template

The wrapper that produced the critiques in this folder. Reuse it for any future design — Search Index
Engine, Job Scheduler, or another company's bank. The two-axis framing (technical soundness **and**
interview exposure) is what made the output useful for interview prep rather than just code review.

## The prompt wrapper

Substitute `${PERSONA}` and `${DIMENSIONS}`, then append the full proposal after `PROPOSAL:`.

```
You are ${PERSONA}. Your role is adversarial: find every flaw, unstated assumption,
edge case, and logical gap in the following proposal. Do not be polite. Do not hedge.
If something is wrong, say it is wrong and explain why. If something is missing, say
what is missing and why it matters.

IMPORTANT CONTEXT: this proposal is a candidate's prepared answer for a Staff-level
(L6) system-design interview at <COMPANY>, 60 minutes, whiteboard/verbal. Critique it on
TWO axes and keep them clearly separated:
  (A) TECHNICAL SOUNDNESS — is anything actually wrong, unworkable, or hand-waved?
      Check the numbers. Check the service/product claims. Check the failure modes.
  (B) INTERVIEW EXPOSURE — where would a strong Staff interviewer push and find the
      candidate has no next layer? Name the specific follow-up question that would
      break this answer, and what the correct response would be.

CRITIQUE DIMENSIONS:
${DIMENSIONS}

For each finding include:
  - severity (critical / high / medium / low)
  - confidence (state it plainly; if low, say so)
  - a concrete recommendation, not "consider revisiting"

Where the proposal cites a specific number (connections per host, latency budget,
throughput ceiling, memory footprint), check it and say if it is wrong or unjustifiable.
Where it names a specific AWS service, check the service actually supports the claimed
behavior and limits.

Zero credible findings is an acceptable outcome. Do not manufacture issues to fill a quota.

Structure your critique as:
1. Critical flaws — would cause failure, or would sink the interview
2. Significant concerns — degrade under stress or at scale
3. Minor issues — suboptimal but not dangerous
4. Interview follow-ups the candidate cannot currently answer
5. What's actually good — brief

PROPOSAL:
<full proposal text here — do not summarize>
```

**The two clauses that earned their keep.** The "check the numbers" and "check the AWS limits" instructions
are what surfaced the Redis Cluster hash-tag bug, the `BatchGetItem` cap, the memory estimate, and the
coalescing multiplier. Without them the critique stays at the level of architecture opinions. Keep them.

## Personas used (Round 1, 2026-08-14)

| Design | Persona |
|---|---|
| Live Chat | a principal distributed-systems architect who has operated real-time messaging platforms at 10M+ concurrent WebSocket connections and been on call through scheduled broadcast events at Super Bowl scale |
| Notifications | a principal engineer who owns a notification platform delivering billions of pushes, emails and in-app messages per day, including APNs/FCM integration, SES deliverability and sender reputation |
| Leaderboard | a staff engineer who has operated Redis and ElastiCache at very large scale and has been burned personally by hot keys, sorted-set sharding, rank-query cost and cluster failover |
| Video Rec | a principal ML-platform engineer responsible for a production recommender's feedback loop, who has personally debugged train/serve skew, position-bias collapse and runaway logging cost |

The "**has been burned personally by**" phrasing matters — it pushes the critique toward operational
experience rather than textbook concerns, and it is what produced the hash-tag and failover findings.

## Invocation

```bash
CLAUDECODE= airchat codex --reasoning-effort high -- exec -s read-only --skip-git-repo-check \
  -o /tmp/debate-response-<id>-<name>.txt \
  "$(cat /tmp/debate-challenge-<id>-<name>.txt)" < /dev/null
```

**Two operational gotchas, both hit during this run:**

1. **`< /dev/null` is required.** Codex prints "Reading additional input from stdin..." and blocks forever
   if stdin stays open. This silently ate a 10-minute run.
2. **Do not run more than ~2 in parallel.** Concurrent invocations race on the shared plugin cache at
   `~/.codex/plugins/cache/` and fail with `mkdir ... no such file or directory` or
   `unlinkat ... directory not empty`. Two of four died this way on the first attempt. Run sequentially, or
   in pairs.

Each run took roughly 4–8 minutes at `--reasoning-effort high` on a ~10 KB proposal.
