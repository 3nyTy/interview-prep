# Codex critique — Round 1 (verbatim)

Adversarial persona: principal engineer at a competing AI-video company, instructed to be hostile. Reviewing `02-design-v1-initial.md`.

## 1. Critical Flaws

### 1. The “App is data” thesis is materially overstated

**Severity: Critical**  
**Confidence: High**  
**Recommendation:** Define a bounded declarative layer covering roughly 70–80% of Apps, then explicitly support versioned custom capabilities and custom workflow handlers.

A static DAG plus pure expressions can express a fixed pipeline. It cannot express the important behavior of several mockup Apps:

- **Character Creator:** multi-view identity consistency, face/reference preprocessing, model-specific conditioning, identity locking across later Apps, and possibly human selection among generated character variants.
- **World Cup 2026 Video:** conditional editorial logic, licensed assets, dynamic event data, beat-synchronized editing, template selection, moderation, and probably multiple aspect-ratio deliverables.
- **Product Ad:** brand-safe typography, logo protection, product geometry preservation, CTA composition, product-background relighting, legal disclaimers, and iterative approval.
- **Short Film:** scene continuity, character state across shots, script revision, user-directed scene replacement, branching revisions, and timeline-level editing.
- **Talking Head Studio:** voice consent, lip sync, phoneme alignment, speech regeneration, subtitle timing, facial motion constraints, and potentially user correction of individual words.

These are not merely more DAG nodes. They require loops, stateful iteration, semantic branching, interactive editing, domain-specific validation, and sometimes custom UI state.

The proposed escape hatch is “grow the capability library.” That is the honest answer, but it means engineers implement every meaningful new creative primitive, define its schema, test it, expose it to the interpreter, and operate it. The no-code claim becomes “marketing can compose pre-built capabilities.” That is useful, but it is not “ship a new App without engineering effort” in the broad sense.

“Publishing is never a deploy” is also false once a new capability, provider adapter, moderation rule, output validator, or custom interaction is needed.

### 2. The declarative escape hatch is unspecified and will become arbitrary code

**Severity: Critical**  
**Confidence: High**  
**Recommendation:** Specify three execution classes: declarative composition, reviewed capability plugins, and exceptional custom workflows. Give each a versioning, security, testing, and ownership model.

Without this, authors will immediately hit requirements the expression language cannot represent. The likely outcome is one of:

1. The expression language grows into an unsafe programming language.
2. Engineers add opaque capabilities until the DAG is just a thin configuration wrapper.
3. Apps acquire custom workflow code outside the stated model.

That is not inherently bad. Pretending it will not happen is bad. A Staff design should state where custom code lives, how it is sandboxed, how it is priced, how it is rolled back, and how App versions pin it.

### 3. Credits are not actually safe under the proposed hold/settle model

**Severity: Critical**  
**Confidence: High**  
**Recommendation:** Model billing as an explicit state machine with durable, idempotent accounting transactions and incremental authorization for long-running or open-ended work.

A 40-minute job exposes several holes:

- The hold expires while the provider is still running.
- The worker finishes after expiry and attempts settlement against an invalid reservation.
- The user spends the same balance through another concurrent run.
- A provider cancellation succeeds locally but still produces billable usage.
- A retry consumes another provider charge even though the quote was for one attempt.
- A partial six-shot delivery has no defined pricing semantics.

“Settle actual, refund delta” is not enough. You need to define:

- whether each shot has an individual retail price;
- whether a failed montage refunds successful shots;
- whether a user-selected partial result is charged;
- whether provider failures are platform-funded;
- how cancellation is priced;
- whether retries are free, capped, or charged;
- what happens when actual cost exceeds the quote;
- whether unused reserved credits remain locked during a hold extension.

A retry budget of “1.5x quote” merely moves the loss somewhere. It does not explain who pays when the provider cost exceeds the user’s authorization.

### 4. Ledger idempotency and refund races are underspecified

**Severity: Critical**  
**Confidence: High**  
**Recommendation:** Use unique business transaction keys and enforce legal state transitions in the ledger database, not only in application logic.

The design mentions an append-only ledger but does not define invariants. These races are routine:

- terminal worker settles;
- timeout reaper refunds;
- provider webhook arrives late;
- manual support replay runs;
- cancellation handler refunds;
- retry handler creates another hold.

Without a unique key such as `(run_id, settlement_type)` and a state transition enforced transactionally, the system can double-refund or settle after refund. `balance_after` is not sufficient protection; it is derived state that can be wrong after a race.

The same applies to concurrent holds. A balance check followed by two inserts is not atomic unless the ledger or account row is serialized.

### 5. The cache design contradicts itself

**Severity: Critical**  
**Confidence: High**  
**Recommendation:** Separate execution idempotency from reusable-result caching. Default generative results to private, run-scoped caching.

The proposal first says generative steps cache only within a run’s retry scope, then later explicitly claims memoization “across session and across users.” Those are incompatible policies.

If two users submit byte-identical inputs to the same App version, a shared cache returns the same video. That creates:

- privacy leaks when inputs contain private faces, product assets, or prompts;
- unexpected disclosure that another user generated the same content;
- licensing problems if the output is not licensed for cross-user reuse;
- likeness-consent problems if a face is involved;
- user dissatisfaction because “generate” no longer means a fresh result;
- possible abuse amplification if a cached disallowed output becomes broadly available.

Content-addressed storage is not automatically safe storage. Object deduplication must not imply shared authorization.

The seed policy is also incoherent. A worker crash should reuse the same provider request and avoid double-spend. “Regenerate” should produce a different result. Those require two different identities:

- **attempt identity:** stable across retries and crash recovery;
- **generation intent identity:** changes when the user explicitly regenerates.

They cannot be represented by one undifferentiated content hash.

### 6. The estimate is not credible as a production estimate

**Severity: Critical**  
**Confidence: High**  
**Recommendation:** Reframe the estimate as an internal vertical slice, or add explicit staffing and scope exclusions.

Ten weeks solo for:

- a generic App runtime;
- App Studio and schema-driven UI;
- durable orchestration;
- two provider integrations;
- capacity routing;
- retries and cancellation;
- credits and reconciliation;
- asset storage and lineage;
- SSE replay;
- moderation;
- canary rollout;
- agent sessions;
- streaming;
- approvals;
- context compaction;
- evaluation;
- billing dashboards;
- production hardening;

is not a credible production estimate. It is a credible prototype estimate only if most infrastructure, UI, security, identity, moderation, billing, and operations already exist.

The “30% buffer” is not enough. Provider heterogeneity, billing correctness, and T&S are not a single risk bucket that can be amortized with a percentage.

---

## 2. Significant Concerns

### 7. Temporal-class orchestration is plausible, but the operational argument is incomplete

**Severity: High**  
**Confidence: High**  
**Recommendation:** Pin interpreter versions, use child workflows and `Continue-As-New`, keep payloads out of history, and state whether the startup is buying managed Temporal or operating the cluster.

A generic workflow that dynamically interprets a DAG is possible. It is not simple.

Problems include:

- fan-out over hundreds of shots creates large workflow histories;
- every poll, retry, signal, and artifact event can inflate history;
- human approval signals can leave workflows open for days;
- interpreter changes can make old histories unreplayable;
- capability behavior can change even when the workflow code does not;
- dynamic DAG expansion makes “one generic workflow” difficult to inspect and replay;
- provider webhooks can arrive after timeout or cancellation;
- polling providers can create a poll storm across many jobs.

The claim that `provider_job_id` is “persisted before the call returns” is technically wrong for most providers. The provider job ID is usually returned by the call. You can persist an internal submission record and an idempotency token before calling, then reconcile the provider response afterward. You cannot durably persist a provider-generated ID before the provider generates it.

Heartbeats detect worker liveness. They do not provide provider progress, prevent duplicate external submissions, or solve webhook/poll races. External provider APIs are usually at-least-once and non-transactional. “Exactly-once side effects” is not available end to end.

For a startup, a simpler design may be better: a Postgres-backed run/step state machine, an outbox, SQS or equivalent queues, provider job records, and a reaper. Move to Temporal when long-lived approvals, replay, and workflow complexity justify the operational cost. The proposal needs to justify that threshold instead of treating Temporal as self-evidently correct.

### 8. Workflow versioning is hand-waved

**Severity: High**  
**Confidence: High**  
**Recommendation:** Pin both `app_version` and `interpreter_version`, and define compatibility rules for capability versions and provider adapters.

Pinning the App manifest is not enough. A 40-minute run may cross:

- an interpreter deploy;
- a provider model upgrade;
- a capability implementation change;
- a pricing formula change;
- a moderation policy change.

The design needs a migration policy:

- old runs finish on old workers;
- new runs use new workers;
- old histories replay on old interpreter binaries;
- capabilities are versioned;
- provider model versions are recorded;
- rollback does not invalidate in-flight work.

Otherwise “immutable App versions” provide false reproducibility.

### 9. Partial delivery pricing is absent

**Severity: High**  
**Confidence: High**  
**Recommendation:** Price outputs as independently billable units with explicit delivery and acceptance states.

A six-shot video App is not a single atomic output. Define behavior for:

- 4 of 6 shots succeed;
- all shots succeed but final assembly fails;
- assembly succeeds but one shot is moderated out;
- the user downloads two shots and abandons the rest;
- a fallback provider produces lower-quality output;
- the user cancels after five shots;
- a retry changes only one shot.

“Partial settle” is a phrase, not a billing policy. Without a per-artifact price ledger, support cannot explain charges and the platform cannot reconcile refunds.

### 10. Cache hits are economically confused with user pricing

**Severity: High**  
**Confidence: High**  
**Recommendation:** Separate retail price, provider cost, and marginal execution cost.

A cache hit may cost the platform zero marginal GPU dollars while still having a quoted user price. That can be intentional. It can also be a user-facing inconsistency if some users pay 150 credits and others receive the same result from a free cache hit.

The proposal alternates between “settle actual” and a product UI showing a fixed price badge such as `150 credits`. Those are different commercial models:

- fixed retail price with platform margin;
- dynamic pass-through pricing;
- discounted cache reuse;
- free deterministic transforms.

Choose one and document it. Otherwise cached and uncached execution will produce unexplained overcharging or margin leakage.

### 11. Agent spending is not solved by a session budget

**Severity: High**  
**Confidence: High**  
**Recommendation:** Require a spend authorization before every material budget increase, with a durable reservation and a clear maximum user-visible liability.

The agent has no quote for its complete plan. A session ceiling is not the same as authorization. The agent can consume the entire ceiling through many individually approved calls, or spend against stale budget state while concurrent tool calls execute.

The system needs:

- per-turn and per-tool reservations;
- atomic remaining-budget checks;
- approval scopes such as “up to 6 videos at 150 credits each”;
- expiration and cancellation behavior;
- a maximum total user liability;
- explicit treatment of failed attempts and retries.

The mockup’s “Want me to continue?” is a product authorization boundary, not merely a workflow signal.

### 12. Proxy-then-final is asserted without evidence

**Severity: High**  
**Confidence: High**  
**Recommendation:** Treat proxies as storyboard and coarse-failure checks until model-specific correlation data proves they predict final quality.

A 480p, 2-second, mini-model render may not predict:

- identity persistence over 10 seconds;
- motion coherence;
- temporal artifacts;
- lip sync;
- audio quality;
- composition at 1080p;
- behavior of the final model;
- long-horizon transitions;
- product/logo fidelity.

The proxy can fail positively and negatively. It may also add cost and latency without preventing the expensive failure. “One rejected final costs more than ten proxies” is not enough; the key question is how often a proxy changes the decision correctly.

Use cheap stills and keyframes for composition, short proxies for motion-risk classes where validated, and final-model low-duration previews where necessary. Measure rejection precision, recall, and cost saved by content class and provider.

### 13. The durable agent loop mishandles streaming and interruption semantics

**Severity: High**  
**Confidence: High**  
**Recommendation:** Persist logical assistant messages and tool-call state; treat token streaming as a best-effort projection outside the durable workflow.

Streaming tokens are not a durable workflow side effect. Persisting every token is expensive and creates replay noise. Persisting nothing means reconnects lose the in-progress response. The design needs a protocol for:

- message chunk checkpoints;
- reconnect and replay;
- duplicate tool-call suppression;
- user interruption during an LLM request;
- cancellation while a provider job is running;
- late tool results after the user has changed direction.

“Make the lighting warmer” cannot simply interrupt a completed video generation. It usually creates a new branch, possibly reuses the source asset, and starts a new edit. The system needs branch/version semantics and billing behavior.

### 14. Context compaction into a “creative brief” is lossy

**Severity: High**  
**Confidence: High**  
**Recommendation:** Maintain a structured constraint ledger with provenance, user overrides, asset references, and unresolved conflicts instead of relying on a prose summary.

A compact brief can lose:

- negative constraints;
- which instruction was later superseded;
- exact user wording;
- references to selected assets;
- rejected variants;
- safety restrictions;
- brand requirements;
- why a particular style decision was made.

“Kept verbatim” does not solve this if the summary is wrong or incomplete. The agent needs structured fields such as identity source, current asset version, aspect ratio, duration, approved style, rejected styles, and user-owned constraints, each linked to the originating turn.

### 15. “Policy is data” is not inherently safe

**Severity: High**  
**Confidence: High**  
**Recommendation:** Version policy data, validate it against a typed schema, require review for spend/tool/moderation changes, and make policy changes auditable and reversible.

Putting prompts, tool sets, budgets, and routing in data improves rollout speed but increases the blast radius of a bad database write. It can change:

- available tools;
- spend limits;
- moderation bypasses;
- provider selection;
- prompt injection behavior;
- output handling;
- privacy boundaries.

Policy must be treated as executable configuration with schema validation, staged rollout, approval, immutable versions, and emergency rollback. It is not safe merely because it is not compiled code.

### 16. App input schemas are insufficient agent tool definitions

**Severity: Medium**  
**Confidence: High**  
**Recommendation:** Create a separate agent-facing tool contract.

A UI schema describes widgets and required fields. An agent needs:

- semantic descriptions;
- examples;
- cost and latency;
- side effects;
- quality limitations;
- supported asset types;
- whether the operation is reversible;
- whether approval is required;
- whether the operation creates a new branch or mutates an asset.

Projecting JSON Schema directly into an LLM tool list will produce technically valid but poorly selected tool calls.

### 17. The evaluation section is a list, not an evaluation design

**Severity: High**  
**Confidence: High**  
**Recommendation:** Pick a narrow first evaluation loop with explicit gates and a go/no-go rule.

The proposal names nine layers but does not decide:

- which metric blocks a release;
- what threshold constitutes regression;
- which tasks are in scope;
- how failures are weighted;
- how cost trades against quality;
- how much data is needed;
- what happens when metrics disagree.

The first system one engineer should build is:

1. provider record/replay and tool-contract tests;
2. a stratified golden set of perhaps 30–50 briefs;
3. hard assertions for safety, schema, budget, artifact validity, and no duplicate spend;
4. a small blind human pairwise set for major releases;
5. cost and latency measurement.

The full judge stack, preference panel, behavioral canary, and trajectory infrastructure should follow only after the product loop exists.

### 18. Trajectory assertions can reject better agents

**Severity: Medium**  
**Confidence: High**  
**Recommendation:** Assert invariants and outcomes, not exact tool sequences.

“Must use proxy before final” is a policy invariant only if the proxy is proven useful for that task. A better agent may use a cached storyboard, reuse an approved prior asset, or choose a cheaper valid path. Exact sequence assertions punish optimization.

Assert:

- no spend beyond authorization;
- required constraints appear in the final request;
- safety checks occur before delivery;
- no duplicate external charge;
- output contract is satisfied;
- the result meets a quality threshold.

Allow multiple valid trajectories.

### 19. The pairwise and canary plans lack statistical power

**Severity: High**  
**Confidence: High**  
**Recommendation:** Define minimum detectable effect, task stratification, and sample size before claiming a canary decision.

Fifty human tasks cannot reliably establish a 5% improvement except for very large effects. A 5% production canary may produce too few completed sessions, and download/share metrics are delayed, confounded, and heavily selection-biased.

The design needs:

- minimum detectable effect;
- confidence intervals;
- sequential testing or a fixed observation window;
- stratification by task type, provider, geography, and user cohort;
- delayed outcome handling;
- safety guardrails that override aggregate quality wins.

“Use confidence intervals” is not an experiment design.

### 20. Provider and GPU economics are underweighted

**Severity: High**  
**Confidence: High**  
**Recommendation:** Make capacity, queue wait, and unit economics first-class product and scheduling concepts.

The proposal mentions provider concurrency and reserved pools but does not model:

- GPU utilization;
- reservation versus on-demand economics;
- provider minimum commitments;
- queue wait as a function of load;
- preemption;
- cold starts;
- model-specific concurrency;
- egress and storage cost;
- fallback quality and cost;
- priority tiers;
- provider invoice lag;
- abandoned jobs that continue billing.

For a creative generation product, queue time is part of the user experience. “The job is resilient” is irrelevant if the user waits 25 minutes with no credible ETA.

### 21. Important product and platform requirements are absent

**Severity: High**  
**Confidence: High**  
**Recommendation:** Add a concise section covering trust, provenance, storage, regionality, and client behavior.

The design underweights or omits:

- moderation of uploaded faces and generated likenesses;
- consent, minors, biometric data retention, and deletion;
- watermarking, provenance, C2PA, and model disclosure;
- copyright and licensed asset handling;
- storage lifecycle and derived-asset garbage collection;
- CDN egress costs;
- multi-region latency, residency, and failover;
- mobile upload, backgrounding, reconnect, and notification behavior;
- quotas, upgrades, entitlement changes, and subscription accounting;
- abuse prevention and rate limiting at the account level.

These are table stakes for an AI media product, especially one whose mockup explicitly includes face upload and identity persistence.

### 22. The submission answers a generic workflow-platform question more than the Pika question

**Severity: High**  
**Confidence: High**  
**Recommendation:** Anchor every major design decision to the concrete Apps and mockup interactions.

The document has many generic platform components but few concrete decisions for:

- Character Creator’s schema-rendered form;
- identity handoff into Short Film and Ads;
- variant filmstrips;
- asset selection and editing;
- Talking Head semantics;
- approval before generating six more videos;
- the credit meter and upgrade flow.

The interviewer asked for an App-based creative platform, not a generic durable DAG engine. The missing product-specific behavior makes the design look broad rather than deep.

---

## 3. Minor Issues

### 23. “Exact-once” language is too strong

**Severity: Medium**  
**Confidence: High**  
**Recommendation:** Say “at-least-once orchestration with idempotent submission, reconciliation, and deduplication.”

You do not control exactly-once execution at external providers. You control your own state transitions and attempt to make external calls idempotent.

### 24. The provider gateway is doing too much

**Severity: Medium**  
**Confidence: High**  
**Recommendation:** Separate routing/admission, protocol adaptation, and metering contracts, even if they deploy together initially.

Routing, quota, circuit breaking, protocol normalization, cost accounting, webhooks, and provider lifecycle management are a large boundary. It risks becoming the second monolith.

### 25. The data model lacks tenancy and authorization detail

**Severity: Medium**  
**Confidence: High**  
**Recommendation:** Add owner/workspace IDs, access-control policy, asset sharing state, and deletion propagation to every relevant entity.

The proposed schema has `created_by_step` but not enough authorization context for private assets, shared projects, organization workspaces, or cross-user cache access.

### 26. “Cache invalidation” is treated as trivial

**Severity: Low**  
**Confidence: High**  
**Recommendation:** Version catalog responses and use event-driven invalidation with bounded staleness.

Publishing affects catalog CDN caches, App Studio previews, agent tool registries, pricing, entitlements, and possibly client-side schema caches. A single invalidation is not a complete publication protocol.

### 27. The synchronous fast path adds complexity without a clear requirement

**Severity: Low**  
**Confidence: Medium**  
**Recommendation:** Start with one async contract and optimize latency within it unless measured agent latency requires otherwise.

A sync/async split for sub-two-second primitives creates two execution paths, two observability paths, and potentially different idempotency semantics. It may be justified later, but it should not be asserted as free.

### 28. Automated face-embedding identity checks are not a complete quality metric

**Severity: Medium**  
**Confidence: High**  
**Recommendation:** Combine identity metrics with consent state, human review sampling, pose/occlusion stratification, and false-positive analysis.

Face similarity can reward an undesirable or unsafe output, fail on stylized content, and behave differently across demographic groups. It is a useful signal, not proof of identity preservation.

### 29. Provider replay is not full agent determinism

**Severity: Medium**  
**Confidence: High**  
**Recommendation:** Record model versions, tool schemas, policy versions, retrieved context, random seeds, and clock behavior.

Temperature zero does not guarantee deterministic LLM output. Provider record/replay makes a test fixture deterministic, but it does not prove production behavior is deterministic.

---

## 4. What’s Actually Good

The proposal has several solid instincts:

- Pinning App versions into runs is correct.
- Treating media as immutable, content-addressed assets with lineage is directionally right.
- Using an outbox for run startup is appropriate.
- Separating provider adapters behind a gateway is necessary.
- Per-capability queues and tenant fairness are better than one global queue.
- Persisting provider attempts and using idempotency keys is the right reliability posture, even though the “exactly once” wording is wrong.
- Durable sessions, typed artifacts, explicit user selection, and approval gates are good foundations for the agent UI.
- Billing, cost, latency, quality, and policy version should all be observable together.
- The proposal correctly recognizes that the declarative model has an expressiveness ceiling and that agent generation requires budget controls.

The core architecture is salvageable. The problem is that it currently presents a plausible platform direction as a set of solved claims.

### Page Budget Reality

For four pages, cut:

- the exhaustive service inventory;
- the full Postgres table list;
- the taxonomy and storage-path details;
- generic retry and observability buzzwords;
- the nine-layer evaluation list;
- repeated restatements of the mockup;
- detailed prompt-caching and model-routing tactics;
- “Temporal versus Step Functions” prose beyond one decision paragraph.

The four pages must retain:

1. One architecture diagram showing control plane, execution plane, provider gateway, asset store, and ledger.
2. One concrete App manifest example, including the declarative boundary and custom-capability escape hatch.
3. One run/billing state machine covering retries, partial delivery, cancellation, hold expiry, and reconciliation.
4. One paragraph explaining why the orchestrator is chosen, how history/versioning works, and what the startup MVP omits.
5. The Part 2 delta: agent loop, approval, context, tool contracts, and spend controls.
6. One evaluation decision: the first golden set, hard invariants, human comparison, and release gates.
7. A realistic estimate with explicit assumptions.

A Staff reviewer skimming four pages should conclude:

> The candidate understands which parts are configuration, which parts require code, how expensive external side effects are made safe, how users are charged, how long-running creative jobs behave, and what the first shippable slice excludes.

The current submission instead communicates breadth, vocabulary, and architectural enthusiasm. It does not yet communicate operational judgment.