# Pika Creative Platform — design of record (v3, post-debate)

Page allocation for the 4-page submission:
  p1 thesis + Class A/B/C boundary + manifest example + architecture diagram
  p2 money (unit lifecycle + two caps) + the two cache identities + orchestration threshold
  p3 agent: loop, tool contract, spend authorization, cancel/branch semantics, cost
  p4 evaluation rule with numbers + estimate

## 1. Thesis and its boundary

An App's PRODUCT surface (form, price, catalog entry, output contract, handoffs) is always data.
Execution is one of three classes, stated explicitly because the honest boundary is the answer:

  A  Composed App    manifest + DAG over allowlisted capabilities + pure expressions.
                     Self-serve publish. Campaign/seasonal/template Apps and thin model
                     wrappers (World Cup 2026, Anime World Cup, Product Ad variants,
                     Seedance 2.0 / Mini). Most of the catalog by count.
  B  Capability      engineer-written, versioned, sandboxed, owned, with an SLO. Provider
                     integrations, identity-locking, lip-sync alignment, beat-synced editing,
                     logo/geometry preservation.
  C  Custom handler  manifest declares handler:<workflow>@v instead of a DAG, for stateful or
                     interactive products (Short Film timeline editing, Talking Head word-level
                     correction).

Class C is a controlled escape hatch, not a bypass:
  - handlers receive ONLY declared versioned capabilities. They cannot call providers, mutate
    assets, or touch the ledger directly. Same authorization and safety gates as Class A.
  - admission requires a written reason why A or B cannot express the behaviour, plus review.
  - the Class C share of the catalog is a tracked platform-health metric with a target. Without
    that control, "5-10 ever" is an aspiration and C quietly becomes the default path.

So: "Growth ships an App with no engineering" is TRUE for A and FALSE for B and C. That is the
claim, bounded and measurable.

Worked example, which doubles as the data model answer:

  app character-creator@v7   lifecycle published   canary 5%
  input_schema  name:str, voice:enum?, front_photo:asset(image)!, backstory:text?,
                image_model:enum + x-ui{widget,order,required}         -> IS the form
  output        character_kit{angles[front,3q,profile,back], bg:pure_white, identity:locked}
                                                                        -> IS the result chips
  price_units   4 x angle @30 + kit_assembly @30                        -> IS the ⚡150 badge
  handoffs      i2v, short-film, product-ad                             -> IS the HAND-OFF chips
  dag           consent_gate -> face_preprocess -> fanout(angles,4) -> identity_check
                -> moderation_gate -> kit_assemble
  pins          interpreter@v, capability@v, provider_model@v, policy@v

One manifest drives the form, the price, the chips, and the handoffs. That is why no App needs
frontend code, and it is the whole scalability answer.

## 2. Money — a unit lifecycle, and TWO caps

Decision 1: retail price is FIXED, quoted from the manifest. The ⚡150 is a promise. Provider
cost variance is our margin problem, not the user's.
Decision 2: the billable unit is a DELIVERABLE, not a run.

Unit lifecycle (per price_unit, not per run):
  AUTHORIZED -> SUBMITTED -> DELIVERED -> CAPTURED
                          -> FAILED    -> VOIDED
                          -> UNKNOWN   -> VOIDED for the user, immediately
UNKNOWN is the primitive the first two drafts were missing. When we cannot establish whether a
provider did the work, the USER IS VOIDED AT ONCE and the unit goes to a reconciliation sweeper
that queries the provider until it resolves. We never hold a user's credits hostage to our own
uncertainty, and we never charge for an outcome we cannot prove.

Liveness, which is where a run-lifetime authorization would otherwise just relocate the race:
authorization release is driven by a LEASE in a durable run registry with heartbeats — never by
"the workflow is absent," because absence is not death. A partitioned orchestrator does not
cause a release. Cancel racing completion: cancel is a REQUEST; if the provider completes
anyway we already paid, so the asset is DELIVERED FREE rather than discarded. The race becomes
a giveaway instead of a bug.

Invariants in the database, not in application code:
  ledger_txn UNIQUE (run_id, unit_id, txn_type); a compare-and-set state column (not the
  unique key alone) enforces legal transitions, so capture-then-void is impossible; account
  balance mutates under a row lock; a per-account cap on outstanding authorization.

Policy, so support can explain any charge: 4 of 6 shots -> capture 4, void 2. Assembly fails
after shots succeed -> capture shots, void assembly, retry free. Shot moderated out -> void.
Retries and provider failures are PLATFORM-FUNDED, never billed; the 1.5x retry budget is a
margin alarm, not a charge. Cache hit charges the posted price and we keep the margin;
deterministic transforms are priced at 0 so the awkward case never arises.

THE SECOND CAP, which the fixed-price decision makes mandatory: user authorization caps USER
liability in credits; it does not cap PLATFORM exposure in dollars. A retry bug can spend 50x
while the user is correctly charged 900 credits. So there is an independent platform exposure
budget per run, per session, per App version, and per provider, with circuit breakers and kill
switches, metered in near-real-time. Monthly invoice reconciliation is the financial backstop
only — it is far too slow to catch a runaway.

## 3. Two identities, because caching and crash-safety are different problems

  request_id    the logical user ask
  variant_id    one per intended output inside a fan-out, so siblings never dedupe into
                each other (the 4 character angles must not collapse to one asset)
  intent_id  = H(request_id, variant_id, regeneration_counter)
                counter bumps ONLY on deliberate regeneration -> "generate again" differs by
                construction, not by convention
  attempt_id    stable across provider retries AND across uncertain outcomes. A new attempt is
                minted only once the prior submission is PROVEN failed via status query, or the
                provider is known-idempotent. Incrementing on timeout is how you buy the same
                video twice.
  reuse_key  = H(capability_v, model_v, resolved_inputs, intent_id)
  reuse_scope = deterministic_public (transcode, concat, thumbnail, frame extract — cross-user
                reuse is safe) | owner (default for ALL generative output) | none (anything
                derived from an uploaded likeness or biometric input)

Byte-level dedup must never imply shared authorization: no user ever receives another user's
generated video. That closes privacy, licensing, likeness-consent, and output-uniqueness at once.
We persist our own submission token BEFORE the call and reconcile the provider's job id after
(the provider generates it, so it cannot be persisted first). A per-provider capability matrix —
idempotent submit? queryable status? cancel semantics? — drives retry policy, and providers that
support none of it carry a documented residual duplicate-charge risk.

## 4. Orchestration — the choice, with its threshold

Managed durable execution (Temporal Cloud, bought not operated). Justified by three properties
this product has: approval waits of hours to days, real cancel/interrupt mid-flight, and Part 2's
agent loop needing the same substrate. If it were fire-and-forget renders only, Postgres +
outbox + SQS + a reaper is enough and cheaper, and I would ship that. Naming the threshold is
the point.
Guardrails: interpreter/capability/provider-model/policy versions pinned per run, in-flight runs
drain on old workers; history carries asset IDs and digests, never bytes; fan-out is a child
workflow per group with continue-as-new; webhook and poll land on ONE reconciler keyed by
attempt_id so late, duplicate, and post-cancel callbacks are idempotent; poll backoff capped and
jittered per provider.
Semantics: at-least-once with idempotent submission and reconciliation. Not exactly-once —
provider APIs are non-transactional and we do not control them.
Error taxonomy: transient -> retry same provider | capacity -> reroute to fallback tier |
policy_refusal -> no retry, void, explain | bad_output -> one retry with a new intent_id |
permanent -> honour on_error | unknown -> sweeper.

## 5. Scalability, and wait time as the product

Org axis (the graded one): section 1. Machine axis: the ceiling is provider concurrency, so
capacity is a scheduling input — reserved vs on-demand commitments, per-capability queues with
per-account fair share, priority by plan.
Wait time IS the experience; a resilient job behind a 25-minute spinner has failed. But the
meaningful queue is inside a third party we cannot observe, so the ETA is an INTERVAL with a
confidence band, computed from observed submit-to-complete distributions bucketed by (provider,
model, duration, resolution), refreshed as the job progresses, biased to p90 so we under-promise
— and with an explicit "we don't know yet" state rather than a fabricated number when telemetry
is missing. We also offer the honest trade ("start now on Mini, or wait ~8 min for the
flagship"), and the reaper cancels abandoned jobs so we stop paying for output nobody will watch.
Folded into the flow rather than given a section: consent gate on uploaded faces before any
generation, moderation as a first-class step type on input and output, C2PA provenance and
watermark on egress, storage lifecycle where renditions expire on schedule and lineage lets
deletion propagate to every derived asset.

## 6. Part 2 — the agent, as a delta

Shared unchanged: Provider Gateway, Orchestrator, Asset Service + lineage, Ledger, Realtime,
Catalog, Ops. New: Agent Service, tool contracts, constraint ledger, LLM Gateway, eval harness.
Changed: Ledger gains scoped session authorizations; Realtime gains thinking/tool-step events.

Loop: a durable workflow per session, because one turn can span 20 minutes of generation and
must survive a deploy. run_app() starts the SAME Part 1 run workflow as a child, so every App is
an agent tool; search_catalog means an App Growth published on Tuesday is usable Tuesday with no
agent deploy.
Streaming is explicitly NOT a durable side effect: tokens go to Redis pub/sub best-effort, the
workflow checkpoints only at message and tool-call boundaries, reconnect replays from the last
checkpoint, and tokens after it may be lost — stated, not hidden. Duplicate tool calls suppressed
by tool_call_id.
Cancel matrix, because "edits are branches" only covers one interaction: stop-generation sets
cancel_requested and issues provider cancel (late completion delivers free, per section 2); user
disconnect keeps the workflow running and notifies on completion; a tool stuck past its budget
trips a per-tool deadline and asks the user; agent abort voids unstarted units.
Tool contracts are a SEPARATE, VERSIONED artifact projected from the UI schema and enriched with
semantics, examples, expected cost and latency, side-effect class (read-only / creates-version /
mutates), reversibility, and approval_required. A widget schema makes a poor tool definition.
Context is a structured CONSTRAINT LEDGER, not a prose brief: immutable typed entries (identity
source, current asset version, aspect, duration, approved style, rejected variants, negative
constraints, brand rules), each with its originating turn and a supersession pointer, compacted
by snapshot-with-hash rather than by summarisation. Prose summaries silently drop negative
constraints and supersession, which is exactly how an agent drifts off-brand on video 5 of 6.
Edits branch, they do not interrupt: "make the lighting a bit warm" creates v2 from v1 on the
asset version DAG and keeps v1 in the filmstrip. At scale, branches are lazily materialised —
only the selected variant gets full renditions, the rest keep a thumbnail plus the recipe to
regenerate — reference-counted, with session branches expiring in 30 days unless favourited or
downloaded. Only DELIVERED branches are billable.

## 7. Part 2 — cost

Spend authorization, not a ceiling: a ceiling drains through many individually plausible calls.
The mockup's "I can do 6 more videos — want me to continue?" is a product authorization
boundary, so: authorize(scope = 6 x short_video @150, max 900 credits), atomically reserved
before EVERY dispatch including concurrent ones, with a stated maximum session liability. Scope
covers billable deliverables only; LLM tokens are platform cost and never user credits. A child
run that outlives the session extends the authorization rather than orphaning it.
Generation is ~95% of spend, in confidence order:
  Validated assumption, ships first: gate on stills. Generate keyframes, check composition and
  identity (face-embedding similarity against the reference is measurable on a still), approve,
  THEN animate. Kills the dominant expensive failure — paying for 10 seconds of video with the
  wrong character. It must clear stated precision/recall and credits-saved thresholds to stay on.
  Unproven, ships behind a flag: a 480p 2s proxy predicting a 1080p 10s final. Identity
  persistence over time, motion coherence, temporal artifacts, and lip sync are precisely what a
  short low-res proxy is worst at, and a bad proxy adds cost and latency while preventing
  nothing. Measured per content class before it is trusted; kill switch by default.
  Always: fan out at the cheapest tier and upgrade only the selection; owner-scoped reuse; a
  no-progress detector (two turns, no accepted artifact, spend over X -> stop and ask).
LLM cost drives turn count and latency more than dollars: prefix caching over system prompt +
tool contracts + constraint ledger (stable prefix, high hit rate), a router sending routine
dispatch to a cheap model and planning/critique/recovery to a frontier model, 8 relevant tools
plus search_catalog rather than 60 definitions, thumbnails and once-computed cached captions
instead of raw frames.
Policy (prompt, tool set, budgets, routing) is versioned data — and data is not automatically
safe. Typed schema, immutable signed versions PINNED PER RUN, deny-by-default capability grants,
mandatory review for anything touching tools, spend, or moderation, staged rollout, audit,
one-click rollback. Same publish protocol as Apps.
Every LLM and provider call writes a cost row tagged (session, turn, tool, provider, model,
policy_version). The metric that matters is cost per ACCEPTED artifact.

## 8. Part 2 — evaluation, with the numbers and the honest power limits

Blocks a merge (deterministic, cheap): provider record/replay + tool-contract tests, and four
invariants at zero tolerance — no spend beyond authorization, no duplicate external charge,
safety gate before delivery, output contract satisfied.
Blocks a release: a 40-brief stratified golden set asserting OUTCOMES AND SEMANTIC PROPERTIES of
the resolved request, never tool sequences or string presence (an exact-sequence assertion
punishes a better agent that finds a cheaper path, and a string assertion punishes one that
satisfies a constraint by an equivalent transformation). Illustrative gates: invariant
violations = 0; cost per accepted artifact <= 1.15x baseline; p95 turn latency <= 90s; refusal
rate <= baseline + 2pp.
Human judgement, sized honestly and NAMED honestly: 50 blind pairwise tasks per major release is
a REGRESSION SCREEN, not a non-inferiority gate. Fifty binary tasks cannot support a five-point
claim, and calling it non-inferiority would be theatre with better wording. It catches gross
regressions; that is its job.
Canary, where the statistical power actually is: 5% of traffic on high-frequency low-variance
signals with numeric stops — error/refusal rate, spend per session, approval-decline rate,
cancel rate — minimum 2,000 sessions before auto-promote, auto-rollback on a named owner's
thresholds. Quality deltas are decided offline because a 5% canary cannot see them. Power is
spent where it exists.
Determinism caveat stated rather than assumed: record/replay plus temperature 0 makes a FIXTURE
deterministic, not production. Model, tool-schema, and policy versions are recorded on every
trace.
Phase 2 once the loop exists: VLM judge calibrated against the human set with judge-human
agreement tracked as its own metric, production-trace replay against candidate policies, and
behavioural metrics (regenerate rate, download/share).

## 9. Estimate — solo with coding agents, P50/P90, with exclusions

Assumes what the prompt states (primitives exist as backend APIs), plus existing identity and
moderation services and design help for the canvas UI.
P50 10 weeks / P90 15 weeks -> a PRODUCTION VERTICAL SLICE, explicitly not the platform.
  In: Class A interpreter, one Class C handler, 3 real Apps, 2 providers, the unit lifecycle
  ledger with the invariants above, asset lineage and variants, SSE with replay, the agent loop
  with scoped authorization and the still-gate, merge-blocking eval.
  Out, stated so the number means something: App Studio is internal YAML plus a validator rather
  than a visual builder; single region; no mobile background upload; no workspace tenancy; no
  automated invoice reconciliation; video proxies still behind the flag; production hardening
  (load testing, runbooks, on-call) is NOT in the 10 weeks.
~2 quarters with 4-5 engineers -> the platform: Studio GA, 10+ providers with capacity
brokering, near-real-time cost reconciliation, multi-region, full eval stack, mobile.
Risks buffered individually because they are not one risk: provider protocol heterogeneity +2wk,
billing correctness +1.5wk (needs its own adversarial suite), eval curation needs creative-team
time so it starts week 1.
