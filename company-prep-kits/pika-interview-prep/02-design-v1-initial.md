# Pika Creative Platform — System Design (candidate design, for adversarial review)

Context: take-home. Part 1 = App-based creative platform over existing creative primitives
(video gen APIs, vision models, image editors, ffmpeg) exposed as backend APIs.
Part 2 = add a Claude-Code-style agent experience over the same primitives.
Graded areas: (1) architecture & data model / how Apps are represented, (2) services &
components, (3) key trade-offs, (4) resiliency, (5) scalability = ship an App with no eng.
Part 2: (6) agent harness, (7) cost, (8) testing & evaluation. Deliverable: 4 pages max.

Stack stance: vendor-neutral core, AWS named where it helps. Estimate framing: solo engineer
with coding agents.

Mockup-derived requirements (from the actual images, not the prose):
- Character Creator screen is a form rendered from a schema: typed fields (text, dropdown,
  file upload, textarea), `required` flags, an "Image model" selector, and a live price badge
  on the submit button ("Generate character  ⚡150").
- Output panel shows typed attribute chips: BACKGROUND: pure white / ANGLES: front, 3-4,
  profile, back / IDENTITY: locked across shots / HAND-OFF: I2V, Short Film, Ads.
- Gallery has collections (Favorites, Featured, AI Video Toolkit, Pika, Models, Create Media,
  Image, Video, Templates, Audio, Voice, Coming Soon). Some "Apps" are thin model wrappers
  (Seedance 2.0, Seedance 2.0 Mini).
- Agent pane: session title + switcher, live credit meter ("430 credits"), tool calls render as
  collapsible steps with typed artifact cards ("Extract frame > Image / Ready"), "Thinking..."
  streaming, agent asks permission before spending ("I can do 6 more videos - want me to
  continue?"), left canvas shows selected asset with a variant filmstrip and per-asset actions.

## 0. Central thesis

An App is DATA, not code. AppManifest (input schema + workflow DAG + price formula +
presentation) is interpreted by ONE generic orchestrator workflow. Consequence: publishing an
App is a database write plus a cache invalidation, never a deploy. Everything in the
"scalability / Growth ships an App" answer falls out of this.

Second thesis: the agent in Part 2 is the same orchestrator with a different planner. Apps
become agent tools; primitives stay shared; only the planning loop, context store, LLM
gateway, and eval harness are new.

## 1. Data model

AppManifest (jsonb, immutable per version):
  app_id, version, slug, title, subtitle, icon, hero_media
  taxonomy { collections[], tags[] }
  lifecycle: draft | preview | published | coming_soon | deprecated
  visibility { entitlement, flag_key, allowlist }
  input_schema: JSON Schema + x-ui hints (widget, order, group, placeholder, show-if)
  output_contract { kind: image|video|audio|character_kit, count, aspect, duration }
  workflow_ref: workflow_id@version
  bindings { model_slot -> allowed capabilities/providers, default }
  pricing { formula over inputs -> credits }        # powers the ⚡150 badge
  handoffs [ { target_app_id, input_mapping } ]     # powers the HAND-OFF chips
  limits { per_user_concurrency, timeout, retry_cost_budget }

Workflow spec (also data):
  steps[]: { id, capability, provider_policy, inputs (restricted expression templates),
             fanout { over, max_parallel }, retry {...}, cache { enabled, key_fields },
             on_error: fail | skip | fallback(step), gate { moderation | quality | approval } }
  outputs: { name -> expr }
Expression language is restricted and side-effect-free (CEL/JSONata-like). No author-supplied
code in v1: engineers add CAPABILITIES, authors COMPOSE them. That boundary is what makes
"no engineering effort" both true and safe, and it makes specs statically analyzable (cost
estimate + DAG shape known before running).

Postgres (control plane + run metadata):
  apps, app_versions(manifest jsonb, state, canary_pct), workflows(spec jsonb)
  projects
  runs(run_id, app_id, app_version, inputs_snapshot, status, quote_credits, hold_id,
       total_cost_micros, error_class, idem_key UNIQUE)
  steps(run_id, step_id, attempt, capability, provider, provider_job_id, cache_key,
        input_digest, status, cost_micros, output_asset_ids[], error_class)
  assets(asset_id, kind, content_hash, storage_uri, bytes, duration_ms, w, h,
         created_by_step, status, expires_at)
  asset_edges(child, parent, role)          -- lineage DAG
  asset_meta(asset_id, prompt, model, model_version, seed, params)
  variants(group_id, asset_id, ordinal, is_selected)   -- the filmstrip
  ledger_entries(append-only: hold|settle|refund|grant, credits_delta, balance_after)
  holds(hold_id, run_id, credits, state, expires_at)
  step_cache(cache_key PK, output_asset_ids[], hits, cost_micros)
  run_events(run_id, seq, ts, type, payload)  -- monotonic seq, replay on reconnect

Blobs: S3 content-addressed s3://media/{sha[0:2]}/{sha256}, CDN + signed URLs. Redis for hot
counters, rate limits, provider quota.

App version is PINNED into every run -> reproducibility, and rollback is a pointer flip.

cache_key = sha256(capability, provider, model_version, canonical_json(resolved_inputs), seed_policy)
Nuance: deterministic steps (frame extract, ffmpeg concat, upscale, caption) always cache.
Generative steps cache only within a run's retry scope, because a user pressing "generate
again" MUST get a different result. Cache must be keyed on intent, and regenerate must bust it.

## 2. Services

Control plane: Catalog Service (manifests, taxonomy, search, favorites; CDN-cacheable,
server-driven UI schema) and App Studio (no-code authoring: form builder, DAG builder, lint,
sandbox preview, cost simulator, publish + canary).
Execution: Run Service / BFF (validate, quote, hold, create run, start workflow, idempotency),
Orchestrator (durable execution engine interpreting workflow specs), Primitive Gateway
(uniform capability API, provider adapters, routing, quota, circuit breakers, protocol
normalization, cost metering; the internal ffmpeg/upscale/frame-extract pool is just another
provider).
Data: Asset Service (blobs, renditions, thumbnails, lineage, variants, retention/GC).
Commerce: Ledger Service (hold/settle/refund, quotes, entitlements).
Delivery: Event/Realtime Service (run event log -> SSE/WS fanout with replay, plus push).
Observability: traces, per-step cost, failure taxonomy, Run Inspector.

## 3. Request flows

A. Browse -> run: GET /catalog (CDN) -> GET /apps/{slug} render schema -> client renders form
generically -> POST /quote -> presigned S3 direct upload (never through the API) + async
moderation -> POST /runs {inputs, idem_key}: validate against schema server-side, resolve
canary app_version by user hash, quote, ledger.hold, insert run, start workflow via
transactional outbox, return 202 {run_id, stream_url} -> client opens SSE from seq 0 ->
steps emit events -> terminal: ledger.settle(actual) + refund delta, emit assets + handoffs.

B. Step execution: resolve inputs -> cache_key -> hit? emit completed(cached), cost 0. Else
Gateway.submit() recording provider_job_id durably BEFORE the call returns; webhook providers
land on a webhook receiver that signals the workflow, poll providers use a heartbeated poll
activity with capped backoff. On result: download to content-addressed S3, create asset +
lineage edges, run output gates (moderation, then quality: duration/aspect/black-frame).
Error taxonomy drives behavior: transient (5xx/timeout/429) -> retry same provider with
jitter; capacity -> reroute to fallback provider; policy_refusal -> no retry, surface, refund;
bad_output -> one retry with new seed; permanent -> honor on_error.

C. Author publishes an App: draft -> validate (schema lint, DAG acyclicity, expression
type-check, capabilities exist, price within band) -> cost-capped sandbox run on fixtures ->
auto-approve if only allowlisted capabilities and price in band, else review queue -> publish
writes an immutable app_versions row at canary_pct=5 -> catalog invalidation -> watch success
rate / cost per run / completion -> promote or roll back by pointer flip. No deploy.

## 4. Key trade-offs (as argued)

1. Async everywhere, sync-shaped API: 202 + run_id + SSE, never hold a request. But keep a
   sync fast path for sub-2s primitives (caption, frame extract) because latency compounds
   inside the agent loop. Cost: two gateway code paths.
2. Durable execution engine (Temporal-class) over hand-rolled SQS+DDB state machine or Step
   Functions. Reason: hours-long fan-out with mid-run human approval, cancel, and
   exactly-once side effects is precisely the use case; and I want the SAME engine to host the
   Part 2 agent loop. Step Functions rejected for history limits and awkward dynamic fan-out
   over user data; SQS+DDB rejected because you rebuild timers, signals, and history anyway.
   Honesty: workflow code is versioned code, but the App is DATA interpreted by one generic
   workflow, so adding an App does not deploy a workflow.
3. Interpreter over codegen: instant publish, static analysis, safe sandbox, one hot path to
   optimize. Cost: expressiveness ceiling, mitigated by growing the capability library.
4. Postgres for control + run metadata, S3 for bytes, Redis for hot counters. Not DynamoDB:
   access patterns are relational (joins across runs/steps/assets, lineage traversal, admin
   queries) and volume is human-initiated, not a machine firehose.
5. Idempotency at three layers: client Idempotency-Key -> run; deterministic step_id+attempt
   -> provider dedupe; content-hash -> asset dedupe.
6. Credits held, not charged: hold at submit, settle at terminal, refund on our-fault failure,
   partial settle on partial delivery. Never charge for a policy refusal or provider outage.
7. Queue per capability class + per-tenant fair share, not one global queue: a World Cup App
   launch must not starve Character Creator, and one power user must not eat provider quota.

## 5. Resiliency

Durable state at step granularity; provider_job_id + cache_key persisted before the call so a
worker crash never double-spends. Retry taxonomy above. Per-run retry cost budget (<= 1.5x
quote) so retries cannot destroy unit economics. Per-provider circuit breaker + health score
feeding the router. Fallback chains per capability with quality tiers. Brownout: under
capacity pressure downgrade non-interactive runs to a cheaper model or queue with an honest
ETA. Timeouts + heartbeats on every activity. Orphan reaper cancels provider jobs we lost
track of so they stop billing. Poison-pill quarantine. Cancellation propagates to provider
cancel(). Task-level monitoring: every step emits capability/provider/model/latency/cost/
error_class; alert on error_class MIX shift, not just rate. Run Inspector renders the DAG with
per-step timing, cost, and assets for support. Single-step replay against a different provider
without redoing the run. DLQ + replay tooling.

## 6. Scalability

Machine axis: stateless services + HPA, orchestrator workers scale on queue depth, S3+CDN for
egress. The real ceiling is provider concurrency, so a capacity manager holds reserved pools
per tier and admission control sheds load honestly.
Org axis (the graded one): App Studio + manifest-as-data + capability library + canary +
rollback + cost simulator. A Growth marketer builds "World Cup 2026 Video" by picking
capabilities, wiring a 4-step DAG, writing prompt templates over {{inputs.selfie}}, setting a
price, previewing, and publishing at 5%. Engineering's job shifts to adding capabilities,
keeping the interpreter fast, and keeping guardrails honest.

## 7. Part 2 — agent harness

Unchanged and shared: Primitive Gateway, Orchestrator, Asset Service + lineage, Ledger,
Realtime, Observability, Catalog.
New: Agent Service (the loop), Tool Registry, Session/Context Store, LLM Gateway, Eval
Harness, approval protocol.
Changed: every App gains an agent_tool projection (name, description, args = its existing
input_schema); Ledger gains session-scoped budgets; Realtime gains thinking/step event types.

The loop IS a durable workflow (AgentSession, one per conversation). Per turn: assemble
context -> LLM Gateway (streaming) -> tool calls become child workflows/activities, where
run_app(app_id, inputs) starts the same Part 1 run workflow as a child -> observations append
typed artifact refs + compact summaries (never blobs) -> loop until no tool calls, budget hit,
or user interrupt.
Why durable and not an in-process loop: a single turn can span 20 minutes of video generation,
so a deploy or pod eviction must not lose the session. Free cancel, signal-based mid-flight
user interrupt ("make the lighting warmer"), replay for eval, and one observability model
shared with Part 1.
Where the code lives: an agent-service hosting the workflow + tool adapters. The POLICY
(system prompt, tool set, budgets, model routing) is versioned DATA, like a manifest, so
prompts ship without deploys and can be A/B'd and rolled back. Same control-plane pattern as
Apps.
Tools: run_app(...); primitives (generate_video, edit_image, extract_frame, describe_media,
tts, compose); workspace (list_assets, get_asset, get_lineage, select_variant); control
(plan, ask_user, request_approval); search_catalog so newly published Apps become usable with
no agent redeploy.
Context: durable append-only transcript, context window is a projection of it. Never media
bytes in context: pass asset_id, kind, dims/duration, a 256px thumbnail (3 keyframes for
video), and a VLM caption computed once and cached on the asset. Rolling compaction summarizes
older turns into a structured CREATIVE BRIEF (character identity, style, aspect, brand
constraints) kept verbatim in context; the brief is what keeps 6 videos on-brand. The UI sends
selection:[asset_id] with each message so "this video" resolves deterministically instead of
being inferred.

## 8. Part 2 — cost

Generation cost is ~95% of spend.
- Proxy-then-final ladder: render 480p / 2-3s / mini-model first, self-critique with a VLM
  against the brief and/or get user approval, only then pay for the final. One rejected 1080p
  10s render costs more than ten proxies.
- Storyboard before motion: for multi-shot work generate still keyframes (cheap), approve
  composition and identity, then animate. Kills the dominant failure mode of paying for video
  with the wrong character.
- Memoization across session and across users (content-addressed).
- Fan out at the cheapest tier, upgrade only the selected variant.
- Hard budgets: per-session ceiling, per-turn cap, and a spend-approval gate above a
  threshold (request_approval suspends the workflow on a signal) - exactly the "6 more videos"
  moment in the mockup.
- Loop / no-progress detector: two consecutive turns with no accepted artifact and spend > X
  stops and asks.
LLM cost (small in dollars, large in latency and turn count):
- Model router by turn type: cheap model for routine tool dispatch and formatting, frontier
  model for planning, critique, and failure recovery; escalate on failure.
- Prompt caching on the stable prefix (system prompt + tool schemas + brief) - the single
  biggest lever, and hit rates are high because the prefix is stable within a session.
- Tool-schema thrift: ship 8 relevant tools plus search_catalog, not 60 definitions.
- Compaction bounds context growth so cost per turn does not grow with session length.
- Thumbnails + cached captions instead of raw frames; cap image tokens per turn. Stream so
  perceived latency drops without extra spend.
Unit economics: every LLM and primitive call writes a cost ledger row tagged
(session, turn, tool, provider, model). Dashboards on cost per ACCEPTED artifact and cost per
session, sliced by policy version. Margin guardrail: policy version whose canary cost/session
regresses past a threshold auto-rolls back.

## 9. Part 2 — testing & evaluation

1. Determinism by construction: record/replay every gateway call (request digest -> fixture).
   Replay mode + temperature 0 makes the agent loop deterministic, so agent tests become
   ordinary tests.
2. Tool-contract tests: schema conformance, idempotency, error taxonomy, cancel semantics.
3. Trajectory evals on ~100-200 golden briefs, asserting on PROCESS, which is objective: did
   it use the proxy tier before the final render; did it call request_approval before
   exceeding budget; did it carry identity/aspect/duration from the brief into the final call;
   tool-sequence sanity; no loops; turn count; credits spent; wall clock.
4. Automated output proxies: aspect/duration/resolution conformance, black-frame and artifact
   detection, A/V sync, identity similarity via face embedding against the reference (directly
   tests "identity locked across shots"), temporal consistency, prompt adherence via VLM
   rubric, aesthetic score. Report distributions, not means.
5. VLM/LLM-as-judge, PAIRWISE against current production rather than absolute scores. Calibrate
   against human labels; track judge-human agreement as its own metric so we know when to
   distrust the judge.
6. Human preference panel: blind pairwise on ~50 tasks per release, Bradley-Terry/Elo. Ground
   truth that anchors layers 4-5.
7. CI + canary: PRs run layers 1-3; releases run 4-6 with thresholds on quality, task success,
   cost/task, p95 latency, refusal rate; then canary the policy version at 5% and watch
   BEHAVIORAL metrics - regenerate rate, edit-after-delivery rate, download/share rate,
   abandonment, thumbs-down. Download/share is the honest quality signal; judges are a proxy.
8. Offline replay of production traces against a candidate policy: same briefs, compare cost
   and trajectory. Cheap regression radar without waiting for canary.
9. Flake management: run the non-deterministic suite N times and gate on confidence intervals,
   not single runs.

## 10. Estimate (solo, agent-assisted)

Assumes primitives already exist as backend APIs (stated in the prompt), a T&S/moderation
service exists, and design/frontend help for the canvas UI.
Part 1: wk1 manifest+catalog+form renderer+upload (skeleton E2E, one hardcoded App);
wk2-3 orchestrator + interpreter + gateway with 2 providers + retry taxonomy + SSE;
wk4 ledger (hold/settle/refund) + quotes + Run Inspector + observability;
wk5 App Studio (author, validate, sandbox, publish, canary);
wk6 memoization + fan-out + hardening + load test + 3 real Apps migrated. ~6 weeks to
production v1, ~2 weeks to a credible internal demo.
Part 2: wk7 agent workflow + tool registry + App-as-tool + session store + stream protocol;
wk8 brief compaction + thumbnails/captions + model router + prompt caching + budgets and
approval gates; wk9 eval harness (record/replay, trajectory suite, VLM judge) + CI gates +
canary; wk10 cost dashboards + tuning + human preference loop. ~4 weeks on top.
Total ~10 weeks solo, agent-assisted. Riskiest: provider protocol heterogeneity (30% buffer)
and eval-suite curation (needs creative-team time, not eng time - parallelize from week 1).
Add ~3 weeks if the internal media-compute pool (ffmpeg farm) must also be built.
