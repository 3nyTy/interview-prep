# Codex critique — Round 3 (verbatim)

Same persona, re-reviewing `03-design-v2-post-round1.md`, asked specifically whether the revisions fixed the findings or merely added vocabulary.

**Bottom line:** The revision fixes many of the original conceptual omissions. It is no longer merely vocabulary. However, the highest-risk correctness issue, the release gate, and the economics are still not closed. I would **not advance yet** without one more focused interview or written follow-up.

**A. Earlier findings**

1. **Declarative boundary and escape hatch — Medium, confidence 95%**

   **Fixed substantially.** Class A, B, and C are now explicit, with ownership and scope. The statement that Growth can ship only Class A Apps is appropriately honest.

   Residual issue: Class C handlers could bypass the intended capability, pricing, authorization, or safety boundaries unless the handler contract is explicit.

   **Recommendation:** State that handlers receive only declared, versioned capabilities and cannot call providers, mutate assets, or bypass billing directly. Add admission criteria for Class C.

2. **Billing state machine — High, confidence 98%**

   Partial delivery, per-unit capture, platform-funded retries, cache pricing, and fixed retail pricing are much clearer. The uniqueness constraints address duplicate transactions at the database layer.

   The major issue remains: a unique `(run_id, unit_id, txn_type)` does not by itself prevent a unit from being captured and later voided unless the legal state transition is a compare-and-set state machine. More importantly, late provider success after cancellation or reaping is unspecified. “Cancel provider job, then release the hold” is not atomic with the provider.

   Monthly reconciliation is also too slow for unknown provider outcomes, runaway retries, or margin explosions.

   **Recommendation:** Define explicit unit states, including `SUBMITTED`, `UNKNOWN`, `DELIVERED`, `CAPTURED`, `VOIDABLE`, and `VOIDED`. Specify what happens when cancellation races with provider completion. Add near-real-time usage reconciliation and an unknown-outcome queue; monthly invoice reconciliation is only the financial backstop.

3. **Cache contradiction and two identities — Mostly fixed, residual Medium, confidence 93%**

   The split between `attempt_key` and `reuse_key` is the correct correction. The privacy and authorization distinction is now explicit.

   Residual issue: `attempt_epoch` can create a duplicate provider job if a worker increments it after a timeout before proving the previous submission failed. Retry identity must remain stable across uncertain submission outcomes, not merely across crash recovery.

   **Recommendation:** Use a stable logical submission ID until the provider outcome is reconciled. Maintain a provider capability matrix: idempotent submit, queryable status, cancellation semantics, and non-idempotent fallback behavior.

4. **Durable execution, versioning, and the incorrect provider ID claim — Mostly fixed, residual Medium, confidence 95%**

   The Temporal justification now has a real threshold. Version pinning, payload discipline, child workflows, reconciliation, and at-least-once semantics are all meaningful improvements. The correction about provider-generated job IDs is factually right.

   The remaining gap is assuming that persisting an internal token before the call makes submission safe. It only does so if the provider honors that token or exposes a reliable reconciliation API.

   **Recommendation:** Make provider idempotency/reconciliation a hard integration requirement, or explicitly document the residual duplicate-job risk for providers that cannot support it.

5. **Agent spend authorization versus session ceiling — Medium-high, confidence 94%**

   The revision correctly rejects a simple cumulative ceiling and introduces an authorization with atomic decrement and maximum liability.

   It still does not define whether authorization covers nested App runs, LLM calls, retries, concurrent tool calls, or only generation units. It also conflicts somewhat with the separate one-hour TTL and run-lifetime authorization model.

   **Recommendation:** Define one ledger model for user authorization, platform exposure, and provider cost. Reserve atomically before every billable child run or tool dispatch, including concurrent dispatches, and define what happens when a child run outlives the session authorization.

6. **Proxy-then-final — Low-medium, confidence 92%**

   This is materially improved. The proposal now labels the proxy predictive value as unproven, uses a feature flag, defines measurements, and includes a kill switch.

   The “validated” still-image gate is still asserted without evidence, but it is a reasonable hypothesis and much less problematic than presenting proxy validation as fact.

   **Recommendation:** Replace “VALIDATED” with the actual evidence or call it “validated assumption.” Define minimum precision, recall, and credits-saved thresholds before rollout.

7. **Streaming and interrupt semantics — Medium-high, confidence 95%**

   Streaming semantics are now credible: best-effort token projection, durable checkpoints at message/tool boundaries, replay, and duplicate tool-call suppression.

   Interrupt semantics remain incomplete. “Edits are branches, not interruptions” handles one interaction, but not cancel-now, stop-generation, user disconnect, agent abort, provider cancellation, or a tool call stuck for 20 minutes.

   **Recommendation:** Specify `cancel_requested`, provider cancellation, late completion, billing, and UI behavior. State explicitly that tokens after the last checkpoint may be lost on reconnect.

8. **Context compaction — Medium, confidence 90%**

   The constraint ledger is a strong correction and is better than a prose summary.

   It does not specify bounded growth, conflict resolution, supersession rules, or how the ledger is compacted without losing provenance.

   **Recommendation:** Define typed merge rules, immutable ledger entries, snapshots with hashes, and behavior for contradictory constraints.

9. **“Policy is data” safety — Mostly fixed, residual Medium, confidence 90%**

   Typed schemas, immutable versions, mandatory review, staged rollout, audit, and rollback address the original hand-wave.

   A Staff reviewer will still ask whether policy is pinned for the lifetime of a run and whether App metadata, tool descriptions, or retrieved content can influence authorization policy.

   **Recommendation:** Sign and pin policy versions per run, use deny-by-default capability grants, and separate policy publication from policy execution.

10. **App input schema reused as agent tool definition — Fixed, confidence 97%**

   This is adequately addressed. The proposal now distinguishes UI schema from an enriched tool contract and names the missing semantic fields.

   Add explicit tool-contract versioning and compatibility behavior when an App’s input schema changes.

11. **Evaluation as a listicle with no go/no-go rule — High, confidence 99%**

   The structure is much better: merge blockers, release blockers, human evaluation, canary scope, and offline quality.

   It still claims to be a decision framework without naming actual cost, latency, quality, refusal, or approval-decline thresholds. “Hard thresholds” is still vocabulary unless the numbers exist.

   **Recommendation:** Give a release table with exact thresholds, confidence method, sample scope, and automatic rollback owner.

12. **Trajectory assertions punishing better agents — Mostly fixed, residual Low-medium, confidence 94%**

   The move to outcome and invariant assertions is correct.

   “Constraints from the ledger appear in the final request” could still punish a valid agent that satisfies a constraint through an equivalent transformation rather than literal inclusion.

   **Recommendation:** Assert semantic properties of the resolved request, not string presence or exact tool arguments.

13. **Human-set statistical power and canary — High, confidence 99%**

   The proposal is commendably honest that 50 tasks cannot establish a 5% improvement and that a 5% canary cannot measure quality reliably.

   But it still calls the 50-task, 55/45 rule a non-inferiority gate. Without a predefined non-inferiority margin, paired design, confidence interval, and treatment of ties, this is an operational cutoff, not a statistically meaningful gate. With 50 binary tasks, a 55/45 result is nowhere near strong evidence of a five-point difference.

   The canary also lacks numeric stop thresholds and minimum event counts.

   **Recommendation:** Either increase the sample or call this a screening gate. Define a one-sided confidence interval, non-inferiority margin, tie policy, and minimum canary exposure before automated rollback.

14. **Provider economics and queue wait — High, confidence 96%**

   The proposal correctly recognizes that wait time is product behavior and that provider capacity, not CPU, is the main constraint.

   The fixed-price decision creates substantial margin exposure, especially with platform-funded retries, provider variance, agent regeneration, and hidden queueing. The stated ETA is also not necessarily “honest”: queue position and historical p50/p90 do not reveal a third-party provider’s current queue, preemption, throttling, or outage.

   **Recommendation:** Return an ETA interval plus confidence or explicitly return “unknown.” Base estimates on observed submit-to-completion distributions by provider/model/parameters, and define fallback behavior when provider telemetry is unavailable. Add a platform-side exposure cap independent of user authorization.

15. **Estimate credibility — Medium-high, confidence 88%**

   This is substantially better than a single unqualified number. The scope, assumptions, exclusions, and separate risk buffers improve credibility.

   Ten weeks for one person to deliver a production vertical slice containing durable orchestration, billing invariants, asset lineage, two providers, an agent loop, realtime replay, and evaluation is still aggressive. “Primitives already exist” does not remove integration, failure testing, deployment, provider-contract, and operational work.

   **Recommendation:** Give P50/P90 ranges and identify the critical-path exit criteria. Explicitly exclude production hardening from the ten-week claim or reduce the vertical-slice scope.

**B. New problems**

1. **Run-lifetime authorization and the reaper — High, confidence 99%**

   This does not remove the race; it moves it into liveness detection.

   If the orchestrator is partitioned, the provider may continue running while the reaper decides whether the run is dead. If the run record is lost, a normal reaper may have nothing to discover. If the reaper releases the hold after cancellation but the provider later completes, the system can under-capture or deliver after authorization release. The per-account cap can then lock the user out while stale holds remain.

   **Recommendation:** Use an independent durable run registry, heartbeats/leases, a provider reconciliation sweeper, and an explicit “unknown provider outcome” state. Do not release authorization merely because a local workflow is absent.

2. **Fixed retail price and open-ended platform liability — High, confidence 96%**

   Fixed retail pricing is a valid product choice, but “provider variance is our margin problem” breaks under provider outages, repeated generation retries, unexpected model pricing, and agent behavior that repeatedly requests new work.

   The user’s 900-credit authorization caps user liability; it does not necessarily cap platform cost. A retry bug could spend much more than the quote while the user remains correctly charged 900 credits.

   **Recommendation:** Add hard platform cost caps per run, session, App version, and provider, with circuit breakers and kill switches. Separate user authorization from maximum platform exposure.

3. **Class C becoming the default — Medium-high, confidence 94%**

   “Five to ten ever” is an aspiration, not a control. Once the DAG interpreter becomes restrictive, teams will choose Class C for speed. Class C could quietly become the normal implementation path and hollow out the declarative platform.

   **Recommendation:** Require an architecture review and written reason why Class A/B cannot express the behavior. Track Class C percentage, handler ownership, cost, and SLO. Make handlers consume the same capability and billing interfaces.

4. **`generation_intent_id` is underdefined — High, confidence 96%**

   “Fresh on user-initiated regenerate” is insufficient across fan-out siblings, branches, agent retries, bad-output retries, and agent-initiated regeneration.

   It must distinguish:
   - the logical user request,
   - each intended variant,
   - provider retry of the same intent,
   - a deliberate regeneration,
   - and a branch derived from an existing asset.

   Otherwise it can either incorrectly reuse output or prevent legitimate reuse. A collision could cause cross-request reuse; a shared ID across siblings could cause accidental deduplication.

   **Recommendation:** Define opaque, globally unique IDs with scope and lineage. Keep provider retry identity stable; create a new generation intent only for deliberate new generation.

5. **55/45 on 50 tasks — High, confidence 99%**

   This is still evaluation theatre unless presented as a deliberately low-power operational screen. It cannot support a credible non-inferiority claim.

   **Recommendation:** Replace “non-inferiority gate” with “regression screen,” or specify a defensible statistical design and increase sample size.

6. **ETA with unobserved provider queues — High, confidence 97%**

   Queue position is not enough if the meaningful queue is inside a provider you cannot observe. A live p90 can be stale or irrelevant to the exact prompt, resolution, duration, or priority.

   **Recommendation:** Provide ranges with uncertainty, refresh them as the job progresses, and admit when no credible estimate is available. “Honest ETA” must include an honest unknown state.

7. **Branch-on-edit at scale — Medium-high, confidence 94%**

   Keeping every prior version in a filmstrip makes every edit a persistent branch. Content addressing reduces duplicate bytes but does not eliminate generation cost, metadata, thumbnails, lineage edges, ACL checks, or abandoned branches.

   **Recommendation:** Define branch retention, quotas, lazy materialization, garbage collection, reference counting, and billing behavior for abandoned or superseded branches. Clarify whether every branch is a billable deliverable and how branch ownership propagates.

**C. Four-page budget**

The current text will not fit four pages with a useful diagram. Cut these items specifically:

- Remove the introductory prose, the catalog of named Apps, and the repeated `-> renders...` annotations. Keep the Class A/B/C table and a six-line manifest example.
- Replace all of Section 2 with one architecture diagram. Put only the data flow and trust boundaries in prose.
- In Section 3, remove the card-network analogy and reduce the policy examples to one partial-delivery example and one retry example.
- In Section 4, retain the two formulas and reuse scopes. Remove the explanatory closing paragraph.
- In Section 5, retain the durable-execution threshold and four guardrails. Remove repeated rationale and compress the error taxonomy to a table.
- In Section 6, retain one paragraph on provider scheduling, ETA uncertainty, downgrade, and cancellation. Move consent, moderation, provenance, and retention into diagram annotations.
- Delete the “Stays / New / Changed” list in Section 7; the diagram already conveys it.
- Remove the LLM optimization details in Section 8: prefix caching, model routing, eight tools, thumbnails, and captions. They are implementation garnish, not architecture.
- In Section 9, delete Phase 2. Keep only merge blockers, release blockers, the corrected statistical rule, and canary rollback signals.
- In Section 10, retain assumptions, vertical-slice scope, platform scope, and two or three major risks. Remove the surrounding justification.

A workable allocation is:

- Page 1: thesis, Class A/B/C boundary, manifest, architecture diagram.
- Page 2: billing, cache identities, durable execution.
- Page 3: agent semantics, spend authorization, branch/cancellation behavior.
- Page 4: evaluation rule, canary, estimate.

The essential material still missing is:

- The reaper/provider-outcome protocol.
- A platform exposure cap separate from user authorization.
- Exact evaluation thresholds and statistical treatment.
- The Class C admission contract.
- ETA behavior when provider telemetry is unavailable.
- Branch retention and billing rules.

Those should replace the cut material.

**D. Decision**

**I would not advance the candidate now.** The revision demonstrates strong responsiveness and materially improves the proposal, but the remaining gaps are in billing liveness, platform exposure, and release decision science rather than presentation.

The single weakest section is **Section 3, Money**. It presents the authorization expiry race as solved when it has actually been transferred to the reaper, cancellation protocol, and reconciliation system. A Staff-level reviewer will require that lifecycle protocol before accepting the billing design.