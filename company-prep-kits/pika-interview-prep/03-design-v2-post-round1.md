# Pika Creative Platform — design v2 (revised after adversarial review)

Structured as the 4-page submission will be. Every section is anchored to a named App or a
specific mockup interaction, not to generic platform vocabulary.

## 1. Thesis, and its honest boundary

An App's PRODUCT surface (form, price, catalog entry, output contract, handoffs) is always
data. Its EXECUTION is one of three classes, and I am explicit about which:

  Class A — Composed App. Manifest + DAG over allowlisted capabilities + pure expressions.
    Author: anyone, self-serve publish. Covers campaign/template/seasonal Apps: World Cup 2026
    Video, Anime World Cup 2026, Product Ad variants, and the thin single-model wrappers
    (Seedance 2.0 / 2.0 Mini). This is most of the catalog BY COUNT and it is where campaign
    velocity lives.
  Class B — Capability plugin. Engineer-written, versioned, sandboxed, reviewed, owned, with an
    SLO. New provider integrations, identity-locking, lip-sync/phoneme alignment,
    beat-synchronised editing, logo/product-geometry preservation.
  Class C — Custom App handler. A named escape hatch: the manifest declares
    handler: <workflow>@version INSTEAD of a DAG, for genuinely stateful/interactive products
    (Short Film timeline editing and scene replacement, Talking Head word-level correction).
    Engineer-owned, but it reuses the same manifest, pricing, catalog, and billing surface, so
    the product layer stays uniform. Budget expectation: 5-10 of these ever, not 50.

So the claim is bounded and testable: "Growth ships an App with no engineering" is TRUE for
Class A and FALSE for B and C. Publishing a Class A App is a DB write plus a publish protocol.
Anything needing a new primitive, a new validator, or interactive state is engineering work,
and pretending otherwise is how no-code platforms die.

Worked example (Character Creator), which is also the graded "how do you represent Apps":

  app: character-creator @ v7            lifecycle: published    canary: 5%
  input_schema: { name: str, voice: enum?|null, front_photo: asset(image) REQUIRED,
                  backstory: text?, image_model: enum[nano-banana-edit, ...] }
                + x-ui { widget, order, required, placeholder }     -> renders the form verbatim
  output_contract: character_kit { angles: [front, 3q, profile, back], bg: pure_white,
                                   identity: locked }               -> renders the result chips
  price_units: [ { unit: angle, credits: 30, count: 4 }, { unit: kit_assembly, credits: 30 } ]
                                                                    -> sums to the ⚡150 badge
  handoffs: [ i2v, short-film, product-ad ]                         -> renders the HAND-OFF chips
  workflow: consent_gate -> face_preprocess -> fanout(angles, max 4) -> identity_check
            -> moderation_gate -> kit_assemble
  pins: interpreter@v, capability@v, provider_model@v

The manifest is the single source of the form, the price badge, the result chips, and the
handoff buttons. That is why the mockup screens need no per-App frontend code.

## 2. Components (one line each; the diagram carries the rest)

Control plane: Catalog (manifests, collections incl. Coming Soon, favourites; CDN-cached with
a versioned publish protocol, not a single invalidation) | App Studio (author, lint, cost-sim,
fixture sandbox, publish + canary + rollback).
Execution: Run API (validate, quote, authorize, start via outbox, idempotency-key) |
Orchestrator (interprets Class A DAGs, invokes Class C handlers) | Provider Gateway, split
into three logical boundaries so it does not become the second monolith: admission/routing
(capacity, quota, priority, circuit-breaking), protocol adaptation (per-provider adapter:
submit/poll/webhook/cancel), metering (provider cost rows).
Data: Asset Service (content-addressed blobs, renditions, thumbnails, variant groups = the
filmstrip, lineage DAG, owner/workspace ACL, retention + derived-asset GC).
Money: Ledger (authorize / capture / void, per-unit) + separate Cost ledger (provider dollars).
Delivery: Event log -> SSE/WS with replay-from-seq, plus push for "I'll let you know".
Ops: Run Inspector (DAG with per-step latency, cost, provider, error class), margin dashboards.

## 3. Money — a state machine, not a phrase

Decision: RETAIL PRICE IS FIXED AND QUOTED FROM THE MANIFEST. The user never sees pass-through
pricing; the mockup's ⚡150 is a promise. Provider cost variance is our margin problem.
That single decision resolves three things the first draft was inconsistent about.

Billable unit is a DELIVERABLE, not a run: price_units above. Vocabulary is card-network
semantics because the semantics are identical:
  authorize(run, sum(price_units))  at submit, under a row lock on the account
  capture(unit)                     per accepted deliverable, as it lands
  void(remainder)                   at terminal state
Invariants enforced in the database, not in application logic:
  - ledger_txn UNIQUE (run_id, unit_id, txn_type)   -> double-capture and double-void impossible
  - legal transitions via CHECK + partial unique index; balance mutated under account row lock
  - authorizations DO NOT EXPIRE ON A TIMER. They are bound to run lifetime and released only
    when the orchestrator declares the run terminal, or when the reaper declares the RUN dead
    (and the reaper's first act is to cancel provider jobs so they stop billing). This removes
    the hold-expires-mid-40-minute-job race entirely. A per-account cap on total outstanding
    authorization prevents indefinite lock-up.
Policy, stated so support can explain any charge:
  - 4 of 6 shots delivered -> 4 units captured, 2 voided.
  - Assembly fails after all shots succeed -> shots captured, assembly voided, retry free.
  - A shot moderated out -> voided, not captured.
  - Retries and provider failures are PLATFORM-FUNDED. Never billed. The 1.5x retry budget is a
    margin alarm and brownout trigger, not a user charge.
  - Cancellation captures delivered units only.
  - Cache hit still charges the posted price; we keep the margin. Deterministic transforms
    (transcode, concat, thumbnail, frame extract) are priced at 0 anyway, so the awkward case
    does not arise.
  - Actual provider cost above quote: we eat it, and it fires a margin alert on that App version.
Reconciliation: provider invoices lag by days. The cost ledger is reconciled monthly against
invoices; margin per App version and per provider is a first-class dashboard, not an afterthought.

## 4. Two identities, because caching and crash-safety are different problems

The first draft used one hash for both and contradicted itself. Split:
  attempt_key  = H(run_id, step_id, attempt_epoch)      sent as the provider's idempotency key.
                 Stable across crash recovery -> a worker that dies after submitting does not
                 double-spend. We persist OUR submission record and token BEFORE the call and
                 reconcile the provider's job id after it returns (the provider generates that
                 id, so it cannot be persisted first — the first draft was simply wrong here).
  reuse_key    = H(capability, capability_v, model_v, resolved_inputs, generation_intent_id)
                 generation_intent_id is FRESH on user-initiated regenerate, so "generate again"
                 differs by construction rather than by convention.
  reuse_scope  = deterministic_public (cross-user reuse allowed: transcode, concat, thumbnail,
                 extract-frame) | owner (default for all generative output) | none (anything
                 derived from an uploaded likeness or biometric input).
Enforced in the cache lookup path. Content-addressed dedup at the byte layer must never imply
shared authorization: no user ever receives another user's generated video, which closes the
privacy, licensing, likeness-consent, and "my output should be unique" holes at once.

## 5. Orchestration — the choice, with its threshold

Managed durable execution (Temporal Cloud, bought not operated). It is justified by three
properties this product actually has: approval waits measured in hours to days, real
cancel/interrupt mid-flight, and Part 2's agent loop needing the same substrate. If the product
were fire-and-forget renders only, Postgres + outbox + SQS + a reaper is enough and cheaper,
and I would ship that instead. Naming the threshold is the point.
Operational guardrails, because a generic DAG interpreter is not free:
  - interpreter_version, capability_version, provider_model_version all pinned into the run.
    In-flight runs drain on old workers; rollback never invalidates live work. Pinning the App
    manifest alone gives false reproducibility.
  - Payloads in history are asset IDs and digests, never bytes.
  - Fan-out over N shots is a child workflow per group with continue-as-new, so a 200-shot job
    does not blow up one history.
  - Webhook and poll both land on the SAME reconciler keyed by attempt_key, so late, duplicate,
    or post-cancellation callbacks are idempotent. Poll backoff is capped and jittered per
    provider to avoid poll storms.
  - Language: AT-LEAST-ONCE with idempotent submission and reconciliation. Not exactly-once —
    external provider APIs are non-transactional and we do not control them.
Error taxonomy drives behaviour: transient -> retry same provider; capacity -> reroute to
fallback tier; policy_refusal -> no retry, void, explain; bad_output (failed a quality gate)
-> one retry with a new generation_intent_id; permanent -> honour on_error.

## 6. Scalability — and queue time is the product

Org axis (the graded question): answered in section 1 by Class A + Studio + canary + rollback.
Machine axis: the ceiling is provider concurrency, not our CPUs. So capacity is a scheduling
input: reserved vs on-demand commitments per provider, per-capability queues with per-account
fair share, priority tiers by plan.
The part that is easy to miss: for a generation product, WAIT TIME IS THE EXPERIENCE. A
resilient job that leaves the user staring at a spinner for 25 minutes has failed. So we
publish an honest ETA from live provider p50/p90 plus queue position, we offer an explicit
downgrade ("start now on Mini, or wait ~8 min for the flagship"), and the reaper cancels
abandoned jobs so we stop paying for output nobody will watch.
Non-negotiables folded into the flow rather than given their own section: a consent gate on
uploaded faces before any generation, moderation gates on input and output as first-class step
types, C2PA provenance and watermark on egress, and a storage lifecycle where proxies/renditions
expire on a schedule while lineage lets deletion propagate to every derived asset.

## 7. Part 2 — what changes, what stays

Stays: Provider Gateway, Orchestrator, Asset Service + lineage, Ledger, Realtime, Catalog, Ops.
New: Agent Service (the loop), agent tool contracts, constraint ledger, LLM Gateway, eval harness.
Changed: Ledger gains scoped session authorizations; Realtime gains thinking/tool-step events.

Harness. The loop is a durable workflow, one per session, because a single turn can span 20
minutes of video generation and must survive a deploy. run_app(app_id, inputs) starts the
SAME Part 1 run workflow as a child, so every App is an agent tool and the Part 1 investment
is the agent's action space. search_catalog means an App published by Growth on Tuesday is
usable by the agent on Tuesday, with no agent deploy.
Token streaming is explicitly NOT a durable side effect: tokens go to a Redis pub/sub channel
as a best-effort projection; the workflow persists durable checkpoints only at message and
tool-call boundaries. Reconnect replays from the last checkpoint, and duplicate tool-call
suppression is keyed on the turn's tool_call_id.
Tool contracts are a SEPARATE artifact from the UI input_schema, projected from it and enriched:
semantic description, examples, expected cost and latency, side-effect class (read-only /
creates-new-version / mutates), reversibility, and approval_required. A widget schema makes a
poor tool definition; the agent needs to know what a call will cost and whether it can be undone.
Edits are branches, not interruptions. "Make the lighting for this video a bit warm" does not
interrupt a finished render; it creates v2 from v1 on the asset version DAG, keeps v1 in the
filmstrip, and is billed as new work. The client sends selection:[asset_id] with every message
so "this video" resolves deterministically instead of being inferred.
Context is a structured CONSTRAINT LEDGER, not a prose brief: typed fields (identity source,
current asset version, aspect, duration, approved style, rejected variants, negative
constraints, brand rules), each carrying the turn that created it and whether a later turn
superseded it. A prose summary silently loses negative constraints and supersession, which is
exactly how an agent drifts off-brand on video 5 of 6.

## 8. Part 2 — cost, with the unproven part labelled

Spend authorization, not a budget ceiling. A ceiling can be drained through many individually
plausible calls. The mockup's "I can do 6 more videos — want me to continue?" is a product
authorization boundary, so it is modelled as one: authorize(scope = 6 x short_video @ 150,
max 900 credits, ttl 1h), atomically decremented per dispatch, with a stated maximum session
liability. Every tool dispatch reserves before it runs; failed attempts are platform-funded.
Generation cost (about 95% of spend), in confidence order:
  VALIDATED: gate on stills. Generate keyframes, check composition and identity (face-embedding
  similarity against the reference is measurable on a still), get approval, THEN animate. This
  kills the dominant expensive failure: paying for 10 seconds of video with the wrong character.
  UNPROVEN, so it ships as an experiment: a 480p 2s proxy predicting a 1080p 10s final. It may
  not — identity persistence over time, motion coherence, temporal artifacts, and lip sync are
  exactly what a short low-res proxy is worst at, and a bad proxy adds cost and latency while
  preventing nothing. So it launches behind a flag per content class, and we measure rejection
  precision, recall, and credits saved per class before trusting it. Kill switch by default.
  ALWAYS: fan out at the cheapest tier and upgrade only the selected variant; reuse cache
  within owner scope; no-progress detector (two turns, no accepted artifact, spend over X ->
  stop and ask).
LLM cost is small in dollars but drives turn count and latency: prefix caching over
system prompt + tool contracts + constraint ledger (the prefix is stable within a session, so
hit rates are high); a router sending routine dispatch to a cheap model and planning, critique,
and failure recovery to a frontier model; 8 relevant tools plus search_catalog rather than 60
definitions; thumbnails and once-computed cached captions instead of raw frames.
Policy (system prompt, tool set, budgets, routing) is versioned data — but data is not
automatically safe. It gets a typed schema, immutable versions, mandatory review for anything
touching tools, spend, or moderation, staged rollout, an audit trail, and one-click rollback.
The same publish protocol as Apps, for the same reason.
Every LLM and provider call writes a cost row tagged (session, turn, tool, provider, model,
policy_version). The gate is cost per ACCEPTED artifact, not cost per call.

## 9. Part 2 — evaluation, as a decision

What blocks a merge: provider record/replay plus tool-contract tests, and four hard invariants
— no spend beyond authorization, no duplicate external charge, safety gate before delivery,
output contract satisfied. These are objective, cheap, and catch the failures that actually
cost money.
What blocks a release: a 40-brief stratified golden set asserting OUTCOMES AND INVARIANTS, not
tool sequences. Asserting an exact sequence punishes a better agent that finds a cheaper path,
so the assertions are: constraints from the ledger appear in the final request, safety ran
before delivery, output contract met, spend within authorization, quality above threshold.
Plus hard thresholds on cost per accepted artifact and p95 latency.
Human judgement, sized honestly: 50 blind pairwise tasks per major release used as a
NON-INFERIORITY gate — block if the candidate loses worse than 55/45, do not require proving a
win. Fifty tasks cannot detect a 5% improvement, and pretending otherwise is how eval theatre
starts. We gate regressions, and we look for wins over a longer horizon.
Canary, sized honestly: 5% of traffic watches only high-frequency low-variance signals — error
and refusal rate, spend per session, approval-decline rate, cancel rate. Quality deltas are
decided offline because a 5% canary lacks the power to see them. Statistical power is allocated
where it exists.
Phase 2, once the product loop exists: VLM-as-judge calibrated against the human set with
judge-human agreement tracked as its own metric, production trace replay against candidate
policies, and behavioural metrics (regenerate rate, download/share). Determinism caveat stated:
record/replay plus temperature 0 makes a FIXTURE deterministic; it does not make production
deterministic, so model, tool-schema, and policy versions are recorded on every trace.

## 10. Estimate — solo with coding agents, two numbers

Assumes what the prompt states: primitives already exist as backend APIs. Also assumes identity
and moderation exist as services, and design help for the canvas UI.
10 weeks -> a PRODUCTION VERTICAL SLICE, not the platform. In: interpreter for Class A, one
Class C handler, 3 real Apps, 2 providers, authorize/capture ledger with the invariants above,
asset lineage and variants, SSE with replay, the agent loop with scoped authorization and the
still-gate, and merge-blocking eval. Explicitly OUT: App Studio is internal YAML plus a
validator rather than a visual builder, single region, no mobile background upload, no
workspace tenancy, no automated invoice reconciliation, video proxies still behind the flag.
About 2 quarters with 4-5 engineers -> the platform: Studio GA, 10+ providers with capacity
brokering, reconciliation, multi-region, the full eval stack, mobile.
Risks buffered individually rather than with one blanket percentage, because they are not one
risk: provider protocol heterogeneity +2 weeks, billing correctness +1.5 weeks (it needs its
own adversarial test suite), eval curation needs creative-team time so it starts in week 1.
