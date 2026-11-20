# Pika Creative Platform — detailed architecture

The 4-page submission is compressed to the point where every sentence carries a decision.
This document is the uncompressed version: the same design with the reasoning that had to be
cut, the alternatives that were rejected, and the parts that are honestly unproven.

Written 2026-08-20. Validated by two rounds of adversarial cross-model review (see
`05-codex-critique-round1.md`, `06-codex-critique-round3.md`).

---

## 1. What was actually asked

Pika Creative Platform is an extensible platform for AI-driven video and creative media, built
around a library of pre-defined **Apps**. Each App is a standalone tool for creating or editing
media: Character Creation, World Cup Video, Product Ad Studio. The platform sits on top of
existing creative primitives — video generation APIs, vision models, image editors, ffmpeg —
all already available as backend APIs.

Part 1 asks for the App-based platform, graded on five areas: architecture and data model
(specifically "how do you represent Apps"), services and components, key trade-offs, resiliency,
and scalability (specifically "how could a Growth marketing team ship a new App without
engineering effort").

Part 2 adds a Claude-Code-style agent: instead of stepping through a pre-defined App, the user
describes what they want in natural language and the agent iteratively creates and edits media,
using the same primitives. Graded on three areas: agent harness, cost, and testing/evaluation.

Deliverables per part: an architecture document, a walk-through, a build estimate, and the
prompts used to steer a coding agent. Part 2 additionally wants the full chat history with that
coding agent. Hard cap: **4 pages total.**

The primitives being given means this is explicitly **not** a "how do you build a video model"
question. It is a platform question: how do you turn a pile of expensive, slow, failure-prone
third-party capabilities into a product that a non-engineer can extend and that does not lose
money.

## 2. Reading the mockups

Three screenshots came with the brief. They carry requirements the prose does not, and pulling
them out at native resolution changed the design materially.

**Character Creator screen.** A left-hand form with typed fields — a text input (Character
name), an optional dropdown (Voice), a required file upload (Front-facing photo), a textarea
(Background story), and a model selector reading "Nano Banana Edit (uses reference)". Explicit
`required` and `(optional)` annotations. The submit button reads "Generate character" with a
price badge: `⚡150`.

That form is *rendered from a schema*. Nobody hand-codes a screen and then annotates which
fields are optional. This single observation is the whole architecture: if the form is a
projection of a schema, then an App is data, and the platform is an interpreter.

**The result panel** shows typed attribute chips: `BACKGROUND: Pure white`,
`ANGLES: Front · 3/4 · profile · back`, `IDENTITY: Locked across shots`,
`HAND-OFF: I2V · Short Film · Ads`. So an App does not return "a file". It returns a **typed
artifact** with declared properties, and it advertises which other Apps can consume it. That
implies an asset graph with provenance, and an output contract in the manifest.

**The gallery** has collection tabs: Favorites, Featured, AI Video Toolkit, Pika, Models, Create
Media, Image, Video, Templates, Audio, Voice, **Coming Soon**. Some entries are thin wrappers
over a single model (Seedance 2.0, Seedance 2.0 Mini). So the catalogue needs taxonomy,
curation, lifecycle states, and it has to accommodate an App whose entire workflow is one node.

**The agent pane** (Part 2) shows a session title with a switcher, a live credit meter reading
`430 credits` next to an Upgrade button, tool calls rendered as collapsible steps with typed
artifact cards (`Extract frame ▸` containing `Image / Ready`), a `Thinking…` indicator, and the
agent proactively asking permission to spend: *"Your fitness Short is ready… Want me to continue
building out the rest of the week from here? I can do 6 more videos — varying the routines,
framing, and energy so each day feels distinct but stays on-brand with this character."* Then a
user follow-up: *"Make the lighting for this video a bit warm."* The left pane is a canvas with
the selected asset, a vertical variant filmstrip, and per-asset actions.

Four things fall out of that pane:
- Credits are a first-class product surface, not an accounting detail.
- The agent asks before spending. That is an **authorization boundary**, not a UI nicety.
- Tool calls stream as steps that produce typed artifacts — the same artifacts Part 1 produces.
- "Make the lighting warm" refers to a selected asset. Deictic reference needs resolving, and
  the edit needs somewhere to go that does not destroy v1.

---

## 3. Core thesis: an App is data — and where that stops being true

### 3.1 The claim

An App's **product surface** is always data: the input schema, the output contract, the price
formula, the catalogue metadata, the hand-off targets. One manifest row drives the form, the
price badge, the result chips, and the hand-off buttons. No App needs frontend code. Publishing
an App is a database write plus a cache-invalidation protocol, never a deploy.

This is the entire answer to the graded question about Growth shipping an App. Everything else
in the design exists to make that claim safe.

### 3.2 Why not code-per-App

The obvious alternative is: each App is a service or a module, engineers write it, it ships on
the normal deploy train. That is faster to build for App #1 and catastrophic by App #40:

- Every campaign App (World Cup 2026, a seasonal promo, a brand tie-in) needs an engineer, so
  campaign velocity is bounded by headcount and by the deploy cadence.
- Forty hand-written Apps means forty places that call providers, forty retry policies, forty
  ways to price something, forty chances to bill a user for a failed render.
- You cannot statically answer "what will this cost" or "what does this App touch" without
  reading code.

The declarative model inverts all three. Because a Class A App is a spec in a restricted
language, the platform can compute its cost envelope, validate its DAG, sandbox it, and price it
*before it ever runs*.

### 3.3 Where it breaks — the three execution classes

This is where the first draft overclaimed, and where the adversarial review landed hardest. A
static DAG plus side-effect-free expressions genuinely cannot express several Apps visible in
the mockup: Short Film needs scene continuity, timeline editing, and user-directed scene
replacement; Talking Head Studio needs phoneme alignment and word-level correction. Those need
loops, stateful iteration, semantic branching, and interactive UI state.

Pretending otherwise is how no-code platforms die: the expression language grows into an unsafe
programming language, or engineers bury everything in opaque capabilities until the DAG is
decoration. So the design names three classes explicitly:

| Class | What it is | Who ships it | Examples |
|---|---|---|---|
| **A — Composed** | Manifest + DAG over allowlisted capabilities + pure expressions. Self-serve publish. | Anyone | World Cup 2026 Video, Anime World Cup, Product Ad variants, single-model wrappers |
| **B — Capability** | Engineer-written, versioned, sandboxed, owned, carries an SLO. | Engineering | Provider integrations, identity-locking, lip-sync alignment, beat-synced editing, logo/geometry preservation |
| **C — Handler** | Manifest declares `handler:<workflow>@v` instead of a DAG. | Engineering | Short Film timeline editing, Talking Head word-level correction |

So the claim is bounded and measurable: **true for Class A, false for B and C.** Class A is most
of the catalogue by count, and it is where campaign velocity lives. Engineering's job shifts from
"write Apps" to "grow the capability library and keep the interpreter honest".

### 3.4 Keeping Class C from eating the platform

"Only five to ten of these, ever" is an aspiration, not a control. The moment the interpreter
feels restrictive, teams will reach for Class C because it is faster, and the declarative
platform hollows out. Three controls:

1. **A capability-only contract.** Handlers receive only declared, versioned capabilities. They
   cannot call providers directly, mutate assets, or touch the ledger. They pass the same consent
   and moderation gates as Class A. So a handler is *more expressive*, not *more privileged* —
   that distinction is what makes the escape hatch safe.
2. **Admission control.** Publishing a Class C App requires a written justification of why A or B
   cannot express the behaviour, plus architecture review.
3. **A tracked ratio.** Class C share of the catalogue is a platform-health metric with a target.
   If it climbs, the interpreter is missing a primitive and that is an engineering signal, not a
   process failure.

### 3.5 The manifest, field by field

```
AppManifest (immutable per version, stored as jsonb)
  app_id, version                 monotonic; every run pins the exact version it ran
  slug, title, subtitle, icon, hero_media
  taxonomy { collections[], tags[] }        drives the gallery tabs
  lifecycle  draft | preview | published | coming_soon | deprecated
  visibility { entitlement, flag_key, allowlist }
  input_schema   JSON Schema + x-ui hints (widget, order, group, placeholder, show-if)
  output_contract { kind, count, aspect, duration, properties }
  execution      dag: <workflow>@v   |   handler: <workflow>@v      # Class A vs Class C
  bindings       { model_slot -> allowed capabilities, default }
  price_units    [ { unit, credits, count_expr } ]
  handoffs       [ { target_app_id, input_mapping } ]
  limits         { per_user_concurrency, timeout, retry_cost_budget }
```

`input_schema` is authoritative on the server. The client renders it, but validation happens
server-side — a client that posts a field the schema does not declare gets rejected, which means
a malicious client cannot smuggle parameters into a provider call.

`x-ui` hints live alongside the schema rather than in the client, because otherwise adding a
field to an App requires a client release, which reintroduces the deploy the whole design is
trying to remove.

`price_units` is a list, not a scalar. This is the detail that makes partial delivery billable
(§7.2). Character Creator is `4 × angle @30 + kit_assembly @30`, which sums to the `⚡150` badge.

`handoffs` is what renders the `HAND-OFF: I2V · Short Film · Ads` chips. It also makes Apps
composable by declaration rather than by convention: Character Creator does not know how Short
Film works, it just declares that its `character_kit` output can be mapped into Short Film's
input.

### 3.6 The workflow spec and the expression language

```
Workflow (also data)
  steps[]
    id
    capability            e.g. text_to_video, image_edit, extract_frame, describe_media, compose
    provider_policy       quality tier + fallback chain
    inputs                restricted expressions over {{inputs.*}} and {{steps.*.outputs}}
    fanout                { over: <expr>, max_parallel }
    retry                 { max_attempts, backoff }
    cache                 { enabled, key_fields, ttl }
    on_error              fail | skip | fallback(step_id)
    gate                  moderation | quality | approval
  outputs { name -> expr }
```

The expression language is deliberately **restricted and side-effect-free** — CEL or JSONata
class, not a scripting language. No I/O, no loops, no user-supplied code. Three consequences:

- **Safety.** A non-engineer's App cannot exfiltrate data or spin forever.
- **Static analysis.** The DAG can be checked for cycles, undeclared capabilities, and type
  errors at publish time rather than at run time.
- **Pre-computable cost.** Because fan-out width is an expression over declared inputs, the
  platform can compute a price and a worst-case cost envelope before running anything. That is
  what makes the `⚡150` badge possible and what makes the sandbox cost-capped.

### 3.7 The publish protocol

Publishing is not "write a row and invalidate a cache". It is:

1. **Draft** in App Studio (form builder, DAG builder, prompt templates).
2. **Validate**: schema lint, DAG acyclicity, expression type-check, every referenced capability
   exists and is allowlisted, price formula within band, fan-out width bounded.
3. **Sandbox run** on fixture inputs with a hard cost cap. This catches the class of error that
   validation cannot: a prompt template that produces garbage.
4. **Approve**: auto-approved if it uses only allowlisted capabilities and the price is in band;
   otherwise a review queue. The auto-approve path is what makes it genuinely self-serve.
5. **Publish** writes an immutable `app_versions` row at `canary_pct = 5`.
6. **Watch** success rate, cost per run, completion rate, margin.
7. **Promote or roll back.** Rollback is a pointer flip, because versions are immutable.

Catalogue invalidation is versioned and event-driven with bounded staleness, not a single purge —
publishing touches the CDN, App Studio previews, the agent's tool registry, pricing, and
entitlements. A single invalidation call would leave those inconsistent.

### 3.8 Why version pins matter more than they look

Every run records `app_version`, `interpreter_version`, `capability_version`,
`provider_model_version`, and `policy_version`.

Pinning only the manifest gives **false reproducibility**. A 40-minute run can cross an
interpreter deploy, a provider model upgrade, a capability reimplementation, a pricing change, and
a moderation policy change. If any of those float, "we ran version 7" tells you nothing about what
the user actually got, and a support investigation six weeks later is guesswork.

Operationally: in-flight runs drain on old workers; new runs land on new workers; rollback never
invalidates live work.

---

## 4. Components

### 4.1 Control plane

**Catalogue Service.** Serves manifests, collections, search, favourites. Read-mostly and
CDN-cacheable per (app_version, locale, entitlement bucket) with ETags. Also serves the
*render schema* to clients — this is server-driven UI, and it is why the gallery and every App
screen are generic.

**App Studio.** The authoring surface: form builder, DAG builder (or YAML for the internal
version), prompt-template editor, linter, cost simulator, fixture sandbox, publish flow with
canary and rollback. This is the component that makes the scalability answer real rather than
theoretical, and it is the one most likely to be under-scoped in a first pass.

**Policy Registry** (added in Part 2). Agent system prompts, tool sets, budgets, model routing —
versioned, signed, reviewed, pinned per run. Same publish protocol as Apps, for the same reason.

### 4.2 Execution plane

**Run API / BFF.** Validates inputs against the schema, computes the quote, authorizes credits,
inserts the run, starts the workflow through a transactional outbox, returns `202`. Requires an
`Idempotency-Key`. It is deliberately thin — no business logic that the orchestrator needs.

**Orchestrator.** Managed durable execution. One generic workflow interprets Class A DAGs;
Class C handlers are named workflows invoked through the same machinery. This is the piece that
is shared with Part 2 (§11.2).

**Provider Gateway.** Split into three logical boundaries so it does not become the second
monolith, even if all three deploy together initially:
- *Admission and routing*: capacity, quota, priority, fair-share, circuit breaking.
- *Protocol adaptation*: one adapter per provider handling submit / poll / webhook / cancel.
- *Metering*: writes provider-dollar cost rows.

The internal media-compute pool (ffmpeg, upscale, frame extraction) is modelled as just another
provider. That keeps one execution contract instead of two, and it means a step does not care
whether its capability is served by Runway or by our own worker.

### 4.3 Data plane

**Asset Service.** Content-addressed blobs in S3 (`s3://media/{sha[0:2]}/{sha256}`) behind a CDN
with signed URLs. Presigned direct upload so bytes never traverse the API. Renditions,
thumbnails, and poster frames. Variant groups (the filmstrip). A **lineage DAG** recording, for
every asset, the step that produced it, its parents, the prompt, the model and version, and the
seed.

Lineage is not documentation. It is load-bearing for four features: iterative edits ("make it
warmer" needs to know what "it" was made from), reproducibility, deletion propagation for GDPR,
and provenance/C2PA claims.

### 4.4 Money

**Credit Ledger** (user-facing credits) and **Cost Ledger** (provider dollars) are separate
services with separate invariants. Conflating them is the single most common way a platform like
this leaks margin, because the questions "what do we charge" and "what did it cost us" have
different answers, different latencies, and different sources of truth. Detail in §7.

### 4.5 Delivery

**Event log + Realtime.** Every run writes to `run_events(run_id, seq, …)` with a monotonic
sequence. Clients subscribe over SSE with `?from_seq=`, so a reconnect replays rather than losing
progress. That matters more than it sounds: a 20-minute render must survive a closed laptop lid, a
tab switch, and a mobile network handoff. Push notification on completion covers the case where
the user has left entirely — which the mockup's *"I'll let you know when it's ready"* implies.

### 4.6 Operations

Per-step emission of capability, provider, model, latency, cost, and error class. A **Run
Inspector** that renders the DAG with per-step timing, cost, and assets, and can replay a single
failed step against a different provider without redoing the run. Margin dashboards per App
version and per provider.

One non-obvious alerting note: alert on a shift in the **error-class mix**, not only on error
rate. A provider that silently degrades from producing video to producing policy refusals looks
healthy on a success-rate chart while the product is broken.

---

## 5. Data model

```sql
apps(app_id, slug, owner_team, created_at)
app_versions(app_id, version, manifest jsonb, state, canary_pct, published_at, created_by)
workflows(workflow_id, version, spec jsonb)

projects(project_id, owner_id, workspace_id, title)
runs(run_id, project_id, user_id, app_id, app_version,
     interpreter_version, policy_version,
     inputs_snapshot jsonb, status, quote_credits,
     total_cost_micros, error_class, idem_key UNIQUE)
steps(run_id, step_id, attempt_id, capability, capability_version,
      provider, provider_model_version, provider_job_id,
      reuse_key, input_digest, status, cost_micros,
      output_asset_ids[], error_class, error_detail)

assets(asset_id, owner_id, workspace_id, project_id, kind, content_hash,
       bytes, duration_ms, width, height, storage_uri, status,
       created_by_step, expires_at)
asset_edges(child_asset_id, parent_asset_id, role)         -- lineage DAG
asset_meta(asset_id, prompt, model, model_version, seed, params jsonb)
variants(group_id, asset_id, ordinal, is_selected, materialised)

price_units_ledger(run_id, unit_id, unit_type, credits, state)
ledger_txn(txn_id, run_id, unit_id, txn_type, credits_delta,
           UNIQUE(run_id, unit_id, txn_type))
authorizations(auth_id, user_id, run_id|session_id, credits, state, lease_id)
cost_rows(id, run_id|session_id, step_id|turn_id, provider, model,
          dollars_micros, invoiced_dollars_micros NULL)

reuse_cache(reuse_key PK, output_asset_ids[], owner_scope, hits, produced_at)
run_events(run_id, seq, ts, type, payload jsonb)
run_registry(run_id, lease_id, lease_expires_at, heartbeat_at)
```

**Why Postgres for all of it.** The access patterns are relational: joins across runs, steps and
assets for the Run Inspector; recursive traversal of `asset_edges` for lineage and deletion
propagation; ad-hoc support and admin queries; ledger invariants that want real constraints and
row locks. And the volume is human-initiated — a person clicking "generate", not a machine
firehose — so this is thousands of runs per minute at scale, not millions of events per second.
DynamoDB would force the lineage traversal and the ledger invariants into application code, which
is exactly where they must not live.

Bytes go to S3 with a CDN. Redis holds hot counters, rate limits, and provider quota.

**Content addressing** gives byte-level deduplication for free, but see §8.3: dedup at the byte
layer must never imply shared authorization.

---

## 6. Request flows

### 6.1 Browse and submit an App run

1. `GET /catalogue?collection=featured` → Catalogue Service, CDN-cached with ETag.
2. `GET /apps/character-creator` → the render schema. The client renders the form generically.
   No App-specific frontend code exists.
3. `POST /apps/character-creator/quote` with partial inputs → evaluates `price_units` → the
   `⚡150` badge. Quoting is a pure function of the manifest and the inputs, so it is cheap and
   cacheable.
4. Upload: `POST /assets/upload-intent` → presigned S3 PUT. Client uploads directly. On
   completion the service verifies the object, creates the asset row, and enqueues a **consent
   gate** and moderation scan. The asset cannot be referenced by a run until both pass.
5. `POST /runs {app_id, inputs, idempotency_key}`:
   - validate inputs against the schema, server-side and authoritatively
   - resolve the App version by canary bucket (hash of user id)
   - compute the quote, `authorize()` the unit set under an account row lock
   - insert the `runs` row and enqueue the workflow start in the **same transaction** via an
     outbox — so a crash between "charged" and "started" is impossible
   - return `202 {run_id, stream_url}`
6. Client opens `SSE /runs/{id}/events?from_seq=0`.
7. Steps execute (§6.2), emitting events.
8. Terminal: capture delivered units, void the remainder, emit assets plus hand-off options.

### 6.2 One step, including the failure branches

1. Resolve the step's input expressions against run inputs and prior step outputs.
2. Compute `reuse_key`. On hit (and if the scope permits, §8.3) emit `step_completed(cached)` at
   zero marginal cost.
3. On miss: persist a submission record with **our** idempotency token *before* calling the
   provider. Call. Reconcile the provider's job id from the response.
   - The first draft of this design said "persist `provider_job_id` before the call", which is
     impossible: the provider generates that id. The distinction matters, because what makes the
     call safe is *our* token plus a reconciliation path, not their identifier.
4. Await completion. Webhook providers land on a receiver that signals the workflow; polling
   providers use a heartbeated activity with capped, jittered backoff. **Both paths land on the
   same reconciler, keyed by `attempt_id`**, so a late callback, a duplicate callback, or a
   callback arriving after cancellation are all idempotent.
5. On result: download to content-addressed S3, create the asset and its lineage edges, run the
   output gates — moderation first, then contract checks (duration, aspect, resolution,
   black-frame detection).
6. Classify the outcome and act (§9.4).

### 6.3 Authoring flow

Covered in §3.7. The point worth repeating: the sandbox run is cost-capped and uses fixtures, so
an author cannot discover a pricing bug by spending real money at scale.

---

## 7. Money

This is the section that separates a design that has thought about this product from one that has
not, and it is where both rounds of adversarial review pushed hardest.

### 7.1 Decision one: retail price is fixed

The `⚡150` in the mockup is a **promise**. The user is quoted a price from the manifest before
they commit, and that is what they pay. Provider cost variance is the platform's margin problem.

The rejected alternative is pass-through pricing ("settle actual"), which the first draft drifted
into. It breaks three ways:
- The badge becomes a lie, because the real cost is not known until after the run.
- A cache hit costs us nothing, so pass-through pricing would mean two users pay different
  amounts for the same output, which is unexplainable at a support desk.
- Provider price changes become user-visible price changes.

Fixed retail also makes the whole system *quotable*, which the UI requires.

### 7.2 Decision two: the billable unit is a deliverable

A six-shot video App is not one atomic output. `price_units` makes each deliverable a billable
line item, so partial delivery has defined semantics instead of a hand-wave.

```
AUTHORIZED → SUBMITTED → DELIVERED → CAPTURED
                       → FAILED    → VOIDED
                       → UNKNOWN   → user VOIDED immediately; sweeper reconciles
```

Stated policy, so support can explain any charge on any invoice:

| Situation | Outcome |
|---|---|
| 4 of 6 shots deliver | capture 4, void 2 |
| all shots succeed, assembly fails | capture the shots, void the assembly, retry free |
| one shot moderated out | void it |
| retry, or provider failure | platform-funded, never billed |
| user cancels after five shots | capture the five delivered |
| cache hit | charges the posted price; we keep the margin |
| deterministic transform (transcode, concat, thumbnail) | priced at zero |
| actual provider cost exceeds the quote | we absorb it, and it fires a margin alert on that App version |

The `1.5× of quote` retry budget is a **margin alarm and brownout trigger**, not a user charge.
That distinction was worth making explicit: a retry budget that silently moves cost onto the user
is just pass-through pricing with extra steps.

### 7.3 `UNKNOWN`, and why it is the important state

Most designs of this shape have `SUCCESS` and `FAILURE`. Real provider integrations have a third
outcome constantly: we submitted, we timed out, and we cannot establish whether the work
happened. A cancel request raced a completion. A webhook was lost. The provider's status endpoint
is 503.

The rule: **when we cannot prove the provider did the work, the user is voided immediately** and
the unit moves to a reconciliation sweeper that queries the provider until it resolves. We never
hold a user's credits hostage to our own uncertainty, and we never charge for an outcome we
cannot demonstrate. The cost, if the work did happen, lands in the cost ledger as margin loss —
which is the correct place for it.

The corollary for cancellation: cancel is a *request*, not a guarantee. If the provider completes
anyway, we have already paid for the output, so the asset is **delivered free**. That converts a
race condition into a giveaway, which is both simpler to implement and better for the user than
discarding work we paid for.

### 7.4 Liveness: leases, not absence

The obvious way to avoid a hold expiring mid-run is to bind the authorization to the run's
lifetime instead of a timer. That is right, but on its own it just relocates the race into the
reaper: if the orchestrator is partitioned, or the run record is lost, what releases the
authorization?

So authorization release is driven by a **lease with heartbeats** in a durable run registry,
independent of the orchestrator. Absence of a workflow is not evidence of death. A partitioned
orchestrator does not trigger a release; an expired lease with no heartbeat does, and the
reaper's *first* act is to cancel provider jobs so they stop billing. A per-account cap on total
outstanding authorization prevents a user being locked out by stale holds.

### 7.5 Invariants belong in the database

The races here are routine, not exotic: the terminal worker captures while the timeout reaper
voids; a late webhook arrives after a void; a support engineer replays an action; a retry handler
creates a second authorization; two concurrent runs race the same balance.

- `ledger_txn UNIQUE (run_id, unit_id, txn_type)` makes double-capture and double-void impossible.
- A **compare-and-set state column** enforces legal transitions. The unique key alone is not
  enough — it would still permit capture followed by void, which is a refund of money already
  recognised.
- Balance mutates under an account row lock, so a balance check followed by two inserts cannot
  both succeed.

`balance_after` is derived state and is not a safety mechanism. It is useful for reconciliation
and useless as a guard.

### 7.6 Two caps, not one

This was the sharpest finding of the whole review, and it is a business point as much as a
technical one.

User authorization bounds **user liability in credits**. It does nothing to bound **platform
exposure in dollars**. A retry bug, a provider price change, or an agent stuck in a loop can
spend fifty times the expected amount while the user is still correctly charged 900 credits. The
ledger says everything is fine. The margin is gone.

So there is an independent **platform exposure budget** per run, per session, per App version and
per provider, with circuit breakers and kill switches, metered in near-real-time.

### 7.7 Reconciliation

Provider invoices lag by days. The cost ledger records our *estimate* at call time
(`dollars_micros`) and is reconciled later against the invoice (`invoiced_dollars_micros`).
Margin per App version and per provider is a first-class dashboard.

Monthly invoice reconciliation is the **financial backstop only**. It is far too slow to catch a
runaway, which is why §7.6 exists as a separate real-time mechanism.

---

## 8. Idempotency and reuse

### 8.1 Why one hash cannot do both jobs

Two requirements look similar and are incompatible:

- A worker that dies after submitting to a provider must not, on recovery, buy the same video
  twice. That wants an identity **stable** across retries.
- A user pressing "generate again" must get a different result. That wants an identity that
  **changes** on regeneration.

The first draft used a single content hash for both and therefore contradicted itself — it
claimed retry-scoped caching in one paragraph and cross-user memoisation in another. The fix is
to separate the two concepts entirely.

### 8.2 Four identities

```
request_id    the logical user ask
variant_id    one per intended output inside a fan-out
intent_id   = H(request_id, variant_id, regeneration_counter)
attempt_id    the provider-call identity
reuse_key   = H(capability_v, model_v, resolved_inputs, intent_id)
```

`variant_id` is not cosmetic. Without it, the four character angles in Character Creator resolve
to identical inputs and **dedupe into a single asset** — the App silently returns one image four
times. Fan-out siblings must be distinguishable by construction.

`regeneration_counter` increments only on a deliberate regeneration, so "generate again" differs
by construction rather than by a convention someone can forget.

`attempt_id` is stable across provider retries **and across uncertain outcomes**. A new attempt
is minted only once the prior submission is *proven* failed by a status query, or the provider is
known-idempotent. Incrementing on timeout — the natural implementation — is precisely how you buy
the same video twice.

### 8.3 `reuse_scope`, and why cross-user caching is mostly forbidden

Content-addressed storage makes cross-user reuse trivially available, and mostly wrong.

| Scope | Applies to | Cross-user reuse |
|---|---|---|
| `deterministic_public` | transcode, concat, thumbnail, frame extract | allowed |
| `owner` | all generative output | denied |
| `none` | anything derived from an uploaded likeness or biometric input | denied entirely |

If two users submit byte-identical inputs to the same App and the second receives the first's
video, the platform has created: a privacy leak when inputs contain private faces or unreleased
product assets; a licensing problem if the output is not licensed for reuse; a
likeness-consent problem; a product problem, because "generate" no longer means generate; and an
abuse amplification path, where one disallowed output becomes broadly reachable.

The rule stated once: **byte-level deduplication must never imply shared authorization.** Dedup
the bytes, scope the access.

### 8.4 The provider capability matrix

Retry policy cannot be uniform, because providers are not uniform. For each provider we declare:
does submit accept an idempotency token and honour it; is there a queryable status endpoint; what
are the cancellation semantics; is billing at submit or at completion.

Retry behaviour is a function of that declaration. Providers that support none of it carry a
**documented residual duplicate-charge risk** rather than a pretended guarantee. Making that
explicit is more useful than a blanket "we retry safely", which would be false.

---

## 9. Orchestration

### 9.1 The choice, and the threshold that justifies it

**Managed durable execution** (Temporal Cloud class, bought not operated).

Three properties of this product justify it:
- Approval waits measured in hours to days (the agent asking permission to spend, a human
  approval gate in an App).
- Real cancel and interrupt mid-flight, propagating to a third party.
- Part 2's agent loop needing the same substrate — so the investment is amortised across both
  halves of the problem.

And the honest converse, which is the part that makes this a decision rather than a fashion:
**if this were fire-and-forget renders only, Postgres + an outbox + SQS + a reaper would be
enough and cheaper, and I would ship that instead.** Naming the threshold is the point. The engine
is a consequence of the product's shape.

Buying rather than operating removes most of the operational objection at startup scale — running
a Temporal cluster is a real cost that a small team should not absorb for this.

### 9.2 Rejected alternatives

**AWS Step Functions.** Workflow history limits become awkward with dynamic fan-out over
user-supplied data, the expression language is constraining for an interpreter, and it would not
host the agent loop well. Choosing it would mean two orchestration models.

**Hand-rolled SQS + DynamoDB state machine.** You end up rebuilding timers, signals, replay, and
history anyway, then making all of it idempotent and observable. The work does not disappear; it
just becomes yours and undocumented.

### 9.3 Guardrails, because a generic DAG interpreter is not free

- **Versions pinned per run**, in-flight runs drain on old workers (§3.8).
- **Payloads in history are asset ids and digests, never bytes.** Video in workflow history is
  how you discover history size limits in production.
- **Fan-out is a child workflow per group with continue-as-new**, so a 200-shot job cannot blow a
  single history.
- **One reconciler** for webhook and poll, keyed by `attempt_id`.
- **Capped, jittered backoff per provider** to prevent poll storms across many concurrent jobs.

### 9.4 Error taxonomy

Behaviour is driven by classification, not by a generic retry wrapper.

| Class | Behaviour |
|---|---|
| transient — 5xx, timeout, 429 | retry the same provider, jittered, within the run's retry budget |
| capacity | reroute to the fallback quality tier; brownout non-interactive runs first |
| policy refusal | no retry — void, explain to the user, do not burn credits |
| bad output — failed a contract or quality gate | one retry with a **new `intent_id`**, so it is a genuinely fresh generation |
| permanent | honour the step's `on_error`: fail, skip, or fallback step |
| unknown | reconciliation sweeper; the user has already been voided |

The `policy refusal` row is worth dwelling on. A content-policy rejection is a *permanent* result
for that input, and retrying it burns money and time while producing the same refusal. Treating it
as transient is a common and expensive bug.

### 9.5 At-least-once, stated honestly

The semantics are **at-least-once with idempotent submission and reconciliation** — not
exactly-once. External provider APIs are non-transactional and outside our control. We control
our own state transitions and we make our external calls as idempotent as each provider allows.

Claiming exactly-once end-to-end would be a lie that the on-call engineer eventually pays for.

---

## 10. Scale, capacity, and wait time

### 10.1 The real ceiling

The organisational axis is §3 — Class A plus Studio plus canary is what lets the catalogue grow
without engineering. The machine axis ceiling is **provider concurrency, not our CPUs**. Our
services are stateless and scale on queue depth; what runs out is a third party's GPU capacity.

So capacity is a scheduling input, not an afterthought: reserved versus on-demand commitments per
provider, per-capability queues with per-account fair share, priority tiers by plan. A World Cup
launch must not starve Character Creator, and one power user must not consume a provider's entire
quota.

### 10.2 Wait time is the product

A resilient job behind a 25-minute spinner has still failed. Resiliency and experience are
different problems and the first is often mistaken for the second.

But the queue that matters sits inside a provider we cannot observe. We do not know their current
backlog, their preemption, or their throttling. So an "honest ETA" cannot be a single number:

- It is an **interval with a confidence band**, computed from observed submit-to-complete
  distributions bucketed by (provider, model, duration, resolution).
- It is **refreshed as the job progresses**.
- It is **biased toward p90**, because under-promising is the correct product bias.
- It has an explicit **"not yet known"** state when telemetry is missing, rather than a
  fabricated number.

Alongside it, the honest trade: *start now on Mini, or wait ~8 minutes for the flagship.* And the
reaper cancels abandoned jobs, so we stop paying for output nobody will watch.

### 10.3 Non-negotiables, folded into the flow

Deliberately not given their own section — for a product with a face upload these are step types
and lifecycle rules, not a compliance appendix:

- A **consent gate** on uploaded faces, before any generation.
- **Moderation** as a first-class step type, on input and on output.
- **C2PA provenance and watermarking** on egress.
- A **storage lifecycle** where renditions and proxies expire on schedule, and where lineage lets
  a deletion request propagate to every derived asset.

---

## 11. Part 2 — the agent

### 11.1 Shared, new, changed

| | |
|---|---|
| **Shared, unchanged** | Provider Gateway, Orchestrator, Asset Service + lineage, Credit and Cost ledgers, Realtime, Catalogue, Ops |
| **New** | Agent Service, agent tool contracts, constraint ledger, LLM Gateway, eval harness |
| **Changed** | Ledger gains scoped session authorizations; Realtime gains thinking and tool-step event types |

The ratio is the point. If Part 1 is built properly, the agent is a planner bolted onto an
existing execution substrate, not a second platform.

### 11.2 The loop is a durable workflow

One `AgentSession` workflow per conversation. Each turn: assemble context → LLM Gateway call
(streaming) → tool calls dispatched as activities or child workflows → observations appended →
repeat until no tool calls, budget exhausted, or user interrupt.

Why durable rather than an in-process loop: a single turn can span twenty minutes of video
generation. An in-process loop dies with the pod, and a deploy would drop every active session.
Durable gives, for free, the things this product needs anyway — cancellation, mid-flight
signalling (the user typing while a render runs), replay for evaluation, and one observability
model shared with Part 1.

**`run_app(app_id, inputs)` starts the same Part 1 run workflow as a child.** That one line is the
whole thesis. Every App is an agent tool at no additional cost, and `search_catalog` means an App
that Growth publishes on Tuesday is usable by the agent on Tuesday, with no agent deploy.

### 11.3 Streaming is deliberately not durable

Persisting every token is expensive and floods the workflow history with replay noise. Persisting
nothing loses the in-progress response on reconnect.

So: tokens go to Redis pub/sub as a **best-effort projection**; the workflow persists durable
checkpoints only at message and tool-call boundaries. A reconnect replays from the last
checkpoint, and tokens after it may be lost — which is stated rather than hidden, because the
alternative is a design that quietly loses data and calls itself durable. Duplicate tool calls are
suppressed by `tool_call_id`.

### 11.4 The cancel matrix

"Edits are branches" covers exactly one interaction. The rest need defining:

| Event | Behaviour |
|---|---|
| stop generation | set `cancel_requested`, issue provider cancel; late completion delivers free (§7.3) |
| user disconnect | workflow keeps running; notify on completion |
| tool exceeds its deadline | ask the user rather than hanging |
| agent abort | void unstarted units |

### 11.5 Tool contracts are not the UI schema

Projecting an App's `input_schema` straight into an LLM tool definition produces technically valid
but poorly chosen tool calls. A widget schema says which fields exist. It does not say what the
call will cost, how long it takes, whether it mutates or creates, or whether it can be undone.

So the agent tool contract is a **separate, versioned artifact** projected from the input schema
and enriched with: a semantic description, examples, expected cost and latency, side-effect class
(`read-only` / `creates-version` / `mutates`), reversibility, and `approval_required`. It carries
its own version, with defined behaviour when the underlying App's schema changes.

Tool set: `run_app`; primitives (`generate_video`, `edit_image`, `extract_frame`,
`describe_media`, `tts`, `compose`); workspace tools (`list_assets`, `get_asset`, `get_lineage`,
`select_variant`); control tools (`plan`, `ask_user`, `request_approval`); and `search_catalog`.

### 11.6 Context is a constraint ledger, not a prose brief

The intuitive approach is to summarise older turns into a prose brief. It fails in a specific and
damaging way: prose silently loses **negative constraints** ("not that shirt", "never a white
background") and **supersession** (which of two conflicting instructions came later). That is
exactly how an agent drifts off-brand on video five of six, and it is invisible until a user
complains.

So context is a structured ledger of **immutable typed entries** — identity source, current asset
version, aspect, duration, approved style, rejected variants, negative constraints, brand rules —
each carrying its originating turn and a supersession pointer. Compaction is by
**snapshot-with-hash**, not by re-summarisation, so provenance survives.

Media never enters context as bytes. What goes in: `asset_id`, kind, dimensions or duration, a
256px thumbnail (three keyframes for video), and a VLM caption computed once and cached on the
asset. That is both a quality and a cost decision.

Deictic reference is resolved by the client, not inferred by the model: the UI sends
`selection: [asset_id]` with every message, so "this video" is unambiguous. Cheaper and more
reliable than asking the model to guess.

### 11.7 Edits branch, they do not interrupt

*"Make the lighting for this video a bit warm"* does not interrupt a finished render. It creates
v2 from v1 on the asset version DAG and keeps v1 in the filmstrip.

At scale, unbounded branching is a storage and cost problem: content addressing removes duplicate
bytes but not generation cost, metadata, thumbnails, lineage edges, or ACL checks. So branches are
**lazily materialised** — only the selected variant gets full renditions, the rest keep a
thumbnail plus the recipe to regenerate — **reference-counted**, and expiring after 30 days unless
favourited or downloaded. Only *delivered* branches are billable.

---

## 12. Part 2 — cost

### 12.1 Spend authorization, not a ceiling

A session ceiling drains through many individually plausible calls, and it says nothing about what
the user agreed to. The mockup's *"I can do 6 more videos — want me to continue?"* is a **product
authorization boundary**, so it is modelled as one:

```
authorize(scope = 6 × short_video @150, max 900 credits)
```

Reserved atomically before every dispatch, including concurrent ones. A stated maximum session
liability. Scope covers billable deliverables only — **LLM tokens are platform cost and never user
credits**, which is both simpler to explain and the right commercial choice. A child run that
outlives its session extends the authorization rather than being orphaned by it.

### 12.2 The generation ladder, with the unproven part labelled

Generation is roughly 95% of spend. Ordered by confidence, because asserting a cost saving that
does not hold is worse than not claiming one:

**Ships first — a validated assumption.** Gate on stills. Generate keyframes, check composition
and identity (face-embedding similarity against the reference is genuinely measurable on a still),
get approval, *then* animate. This kills the dominant expensive failure mode: paying for ten
seconds of video with the wrong character. It must clear stated precision, recall and
credits-saved thresholds to stay enabled.

**Ships behind a flag — unproven.** A 480p two-second proxy predicting a 1080p ten-second final.
Identity persistence over time, motion coherence, temporal artifacts, and lip sync are precisely
what a short low-resolution proxy is *worst* at. A bad proxy adds cost and latency while
preventing nothing. The relevant question is not "a proxy is cheaper than a final" but "how often
does the proxy change the decision correctly" — so it is measured per content class, with rejection
precision, recall, and credits saved, and a kill switch by default.

**Always on.** Fan out at the cheapest tier and upgrade only the selection. Owner-scoped reuse. A
no-progress detector: two consecutive turns with no accepted artifact and spend over a threshold →
stop and ask.

### 12.3 LLM cost

Small in dollars relative to generation, but it drives turn count and latency, which drive
generation cost.

- **Prefix caching** over system prompt + tool contracts + constraint ledger. The prefix is stable
  within a session, so hit rates are high. This is the largest single lever.
- **A model router**: routine tool dispatch and formatting to a cheap model; planning, critique,
  and failure recovery to a frontier model; escalate on failure.
- **Tool-schema thrift**: ship the eight relevant tools plus `search_catalog`, not sixty
  definitions.
- **Bounded context** via the constraint ledger, so cost per turn does not grow with session
  length.
- **Thumbnails and cached captions** instead of raw frames; a cap on image tokens per turn.

### 12.4 Policy is data — and data is not automatically safe

Putting prompts, tool sets, budgets and routing in versioned data is right: prompts ship without
deploys, and can be A/B tested and rolled back.

But it widens the blast radius of a bad database write to include available tools, spend limits,
moderation behaviour, and provider selection. So policy is treated as **executable
configuration**: a typed schema, immutable signed versions **pinned per run**, deny-by-default
capability grants, mandatory review for anything touching tools, spend or moderation, staged
rollout, an audit trail, and one-click rollback. The same publish protocol as Apps, for the same
reason.

### 12.5 Unit economics

Every LLM and provider call writes a cost row tagged
`(session, turn, tool, provider, model, policy_version)`.

The metric that matters is **cost per accepted artifact**, not cost per call — an agent that
halves its per-call cost while doubling its rejection rate has made the product worse and the
spend higher. Dashboards slice by policy version, and a policy version whose canary cost per
session regresses past a threshold auto-rolls back.

---

## 13. Part 2 — evaluation

Creative output is subjective and the agent is non-deterministic. The design's stance is to put
statistical power where it actually exists and to say plainly where it does not.

### 13.1 What blocks what

| Stage | Gate | Rule |
|---|---|---|
| **PR / merge** | provider record–replay + tool-contract tests; four invariants | Zero tolerance: no spend beyond authorization; no duplicate external charge; safety gate before delivery; output contract satisfied |
| **Release** | 40-brief stratified golden set | Assert outcomes and semantic properties of the resolved request. Illustrative gates: invariant violations 0; cost per accepted artifact ≤ 1.15× baseline; p95 turn latency ≤ 90s; refusal rate ≤ baseline +2pp |
| **Release** | 50-task blind human pairwise | A **regression screen**, not a non-inferiority gate |
| **Canary 5%** | high-frequency, low-variance signals only | Error and refusal rate, spend per session, approval-decline rate, cancel rate. Minimum 2,000 sessions before auto-promote; named owner on auto-rollback |

### 13.2 Determinism by construction

Every gateway call is recorded (request digest → response fixture). In replay mode with
temperature 0, the agent loop becomes deterministic and agent tests become ordinary tests. This is
the cheapest and highest-leverage piece of eval infrastructure, and it is what one engineer should
build first.

Stated caveat: record–replay plus temperature 0 makes a **fixture** deterministic, not production.
Model, tool-schema, and policy versions are recorded on every trace so that a behaviour change can
be attributed.

### 13.3 Assert outcomes, not trajectories

The tempting design is to assert the tool sequence: "must call the proxy before the final render".
It is wrong in two ways. It punishes a better agent that finds a cheaper valid path — reusing an
approved prior asset, or a cached storyboard. And a string-presence assertion ("the constraint
appears in the final request") punishes an agent that satisfies the constraint through an
equivalent transformation.

So assertions are on **semantic properties of the resolved request** and on outcomes: no spend
beyond authorization, no duplicate external charge, safety ran before delivery, output contract
satisfied, constraints from the ledger honoured, quality above threshold. Multiple valid
trajectories are allowed.

### 13.4 Statistical honesty

Fifty binary tasks cannot establish a five-point quality difference. Calling a 55/45 threshold a
"non-inferiority gate" would be evaluation theatre with better vocabulary, so it is named what it
is: a **regression screen** that catches gross regressions, which is a useful job.

Likewise a 5% canary produces too few completed sessions to resolve a small quality delta, and
download/share metrics are delayed and selection-biased. So the canary watches only
high-frequency, low-variance signals with numeric stops and a minimum exposure, and **quality
deltas are decided offline**.

Phase 2, once the product loop exists: a VLM judge calibrated against the human set with
judge–human agreement tracked as its own metric; production-trace replay against candidate
policies; and behavioural metrics — regenerate rate, download and share rate, which is the honest
signal every judge is only a proxy for.

---

## 14. Estimate

Solo, with coding agents. Assumes what the brief states — the creative primitives already exist as
backend APIs — plus existing identity and moderation services, and design help for the canvas UI.

**P50 10 weeks / P90 15 weeks → a production vertical slice, explicitly not the platform.**

*In scope:* the Class A interpreter, one Class C handler, three real Apps, two providers, the
unit-lifecycle ledger with the invariants of §7.5, asset lineage and variants, SSE with replay,
the agent loop with scoped authorization and the still-gate, merge-blocking eval.

*Out of scope, stated so the number means something:* App Studio is internal YAML plus a validator
rather than a visual builder; single region; no mobile background upload; no workspace tenancy; no
automated invoice reconciliation; video proxies still behind the flag; and production hardening —
load testing, runbooks, on-call — is **not** inside the ten weeks.

**~2 quarters with 4–5 engineers → the platform:** Studio GA, 10+ providers with capacity
brokering, near-real-time cost reconciliation, multi-region, the full eval stack, mobile.

Weekly shape: **1** manifest, catalogue, form renderer, upload path · **2–3** interpreter,
gateway, two providers, error taxonomy, SSE · **4** ledger, quotes, Run Inspector · **5**
Studio-as-YAML, publish and canary · **6** reuse cache, fan-out, hardening · **7** agent loop,
tools, App-as-tool · **8** constraint ledger, authorization, still-gate · **9** eval harness and
merge gates · **10** cost dashboards and tuning.

Risks are buffered individually rather than with one blanket percentage, because they are not one
risk: provider protocol heterogeneity **+2wk**; billing correctness **+1.5wk**, since it needs its
own adversarial suite; eval curation needs creative-team time, so it starts in week 1.

---

## 15. Known weaknesses

Listed deliberately. These are where a Staff-level reviewer should push, and pretending they are
settled is worse than naming them.

1. **The billing lifecycle protocol is described, not fully specified.** §7.3 and §7.4 give the
   states and the liveness mechanism, but a complete design would enumerate every transition,
   every race, and the reconciliation contract with each provider. The adversarial reviewer named
   this the weakest section and was right; four pages cannot carry it.
2. **The still-gate is a validated *assumption*, not a measured result.** It is well-motivated and
   the metric is measurable, but no data yet.
3. **Eval thresholds are illustrative.** The numbers in §13.1 show what would be instrumented, not
   values derived from a real baseline.
4. **Ten weeks solo is aggressive** even with the exclusions. "Primitives exist" does not remove
   integration, failure testing, provider contracts, or deployment work. The P90 absorbs some of
   this; a skeptic would still push.
5. **Class C admission relies on process.** The capability-only contract is a real technical
   control; the review requirement and the tracked ratio are organisational, and organisational
   controls decay.
6. **Multi-region, tenancy, and mobile are unaddressed** by choice, to hold the page budget. All
   three have real design content.
