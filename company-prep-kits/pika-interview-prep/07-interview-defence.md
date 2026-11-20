# Pika take-home — likely pushback and how to answer it

Derived from the two adversarial review rounds. The reviewer was cast as a hostile principal
engineer at a competing AI-video company, so these land close to what a real Staff loop asks.
Answers are short on purpose: say the load-bearing sentence, then stop and let them follow up.

---

## On the declarative thesis

**"Isn't this just a no-code platform that collapses into engineers writing every App anyway?"**

That is the standard failure mode, which is why the design names three execution classes instead
of claiming one. Class A composition covers campaign and template Apps, which is most of the
catalogue by count. New primitives and interactive products are engineering work, and I say so.
The claim is bounded: true for Class A, false for B and C.

**"Show me an App your DAG cannot express."**

Short Film. Scene continuity across shots, user-directed scene replacement, timeline editing,
branching revisions. That is stateful interactive work, not more DAG nodes. It is Class C — a named
workflow handler behind the same manifest, pricing and catalogue surface.

**"So what stops everything becoming Class C?"**

One technical control and two organisational ones. Technically, handlers get only declared
versioned capabilities: they cannot call providers, mutate assets, or touch the ledger. Class C is
more expressive, never more privileged. Organisationally, admission requires a written reason A or
B cannot express it, and the Class C share of the catalogue is a tracked metric. If it climbs, the
interpreter is missing a primitive — an engineering signal, not a process failure.

**"Publishing is never a deploy — is that actually true?"**

For Class A, yes. The moment you need a new capability, a new output validator, or a new
interactive surface, it is a deploy. I would not claim otherwise.

---

## On orchestration

**"Why durable execution and not Step Functions, or just SQS and a state machine?"**

Three properties of this product: approval waits of hours to days, real cancel and interrupt
propagating to a third party, and Part 2's agent loop needing the same substrate. Step Functions
gets awkward with dynamic fan-out over user data and would not host the agent loop, so I would end
up with two orchestration models. SQS plus Dynamo means rebuilding timers, signals, replay and
history and then making all of it idempotent — the work does not disappear, it becomes mine and
undocumented.

**"And if the team has no appetite for that dependency?"**

Then I ship Postgres plus an outbox plus SQS plus a reaper, and I say so in the doc. If the product
were fire-and-forget renders only, that is genuinely the right answer and it is cheaper. The
threshold that flips it is the approval waits and the shared agent substrate. Also: managed, not
self-hosted — a small team should not run that cluster.

**"A generic DAG interpreter in a durable engine will blow up your history."**

Unmanaged, yes. Four guardrails: payloads are asset ids and digests, never bytes; fan-out is a
child workflow per group with continue-as-new; versions pinned per run with in-flight runs draining
on old workers; one reconciler for webhook and poll keyed by `attempt_id`.

**"Do you get exactly-once?"**

No. At-least-once with idempotent submission and reconciliation. Provider APIs are
non-transactional and outside my control. I control my own state transitions and make the external
call as idempotent as each provider permits — which is why there is a per-provider capability
matrix rather than one retry policy.

---

## On money — expect the most pressure here

**"Your hold expires while a 40-minute job is still running."**

It does not expire on a timer. Authorization is bound to run lifetime, released by a lease with
heartbeats in a durable run registry.

**"That just moves the race into the reaper. What if the orchestrator is partitioned?"**

Correct, and that is the right follow-up. Absence of a workflow is not evidence of death — a
partition does not trigger release. Only an expired lease with no heartbeat does, and the reaper's
first act is to cancel provider jobs so they stop billing. There is also a per-account cap on
outstanding authorization so stale holds cannot lock a user out.

**"Four of six shots delivered. What am I charged?"**

Four. The billable unit is a deliverable, not a run — `price_units` in the manifest makes each one
a line item. Assembly fails after all shots land: shots captured, assembly voided, retry free. A
shot moderated out: voided.

**"You submitted, you timed out, you don't know if the provider did the work."**

That is the `UNKNOWN` state, the one most designs omit. The user is voided immediately and a
sweeper reconciles with the provider. We never hold credits hostage to our own uncertainty and
never charge for an outcome we cannot prove. If the work did happen, the cost lands in the cost
ledger as margin loss, which is the correct place for it.

**"Cancel races completion."**

Cancel is a request, not a guarantee. If the provider finishes anyway we have already paid, so the
asset is delivered free. The race becomes a giveaway rather than a bug.

**"How do you prevent a double refund?"**

In the database, not in application logic. `ledger_txn UNIQUE(run_id, unit_id, txn_type)` plus a
compare-and-set state column — the unique key alone would still permit capture-then-void. Balance
mutates under an account row lock.

**"Fixed retail price means you eat provider variance. Where does that break you?"**

It bounds the user's liability, not mine — and that is the gap worth naming. A retry bug or an
agent loop can spend fifty times the expected amount while the user is correctly charged 900
credits. The ledger looks fine and the margin is gone. So there is an independent platform
exposure budget per run, session, App version and provider, with circuit breakers, metered in
near-real-time. Monthly invoice reconciliation is only the financial backstop.

---

## On caching

**"Two users submit identical inputs. Does the second get the first user's video?"**

No. Generative output is owner-scoped by default, and anything derived from an uploaded likeness is
scope `none`. Cross-user reuse is allowed only for deterministic transforms — transcode, concat,
thumbnail, frame extract. Content addressing dedupes the bytes; it must never imply shared
authorization.

**"But a crash must not double-charge. Same hash, opposite requirements."**

Right, and my first draft had exactly that contradiction. They are separate identities.
`attempt_id` is stable across retries and across uncertain outcomes — the crash-safety identity.
`intent_id` includes a regeneration counter, so "generate again" differs by construction. And
`variant_id` exists so fan-out siblings do not dedupe into each other — without it, Character
Creator's four angles collapse into one image returned four times.

**"When do you mint a new attempt?"**

Only when the prior submission is proven failed by a status query, or the provider is
known-idempotent. Incrementing on timeout is the natural implementation and it is how you buy the
same video twice.

---

## On the agent

**"Where does the agent code live, and why isn't it a loop in a service?"**

An Agent Service hosting a durable workflow, one per session. A single turn can span twenty minutes
of generation, so an in-process loop dies with the pod and a deploy drops every active session.
Durable also gives cancellation, mid-flight user interrupts, and replay for eval — all of which the
product needs anyway.

**"How does it reuse Part 1?"**

`run_app()` starts the same Part 1 run workflow as a child. Every App is a tool at no extra cost,
and `search_catalog` means an App published on Tuesday is usable by the agent on Tuesday with no
agent deploy.

**"You're streaming tokens out of a durable workflow. That sounds wrong."**

It would be. Tokens are a best-effort projection on pub/sub; the workflow checkpoints only at
message and tool-call boundaries. A reconnect replays from the last checkpoint and tokens after it
may be lost — I would rather state that than claim durability I do not have.

**"'Make the lighting warmer' arrives while a render is in flight."**

Edits branch, they do not interrupt: v2 from v1 on the asset version DAG, v1 stays in the
filmstrip. Separately there is a cancel matrix for genuine interrupts — stop-generation,
disconnect, tool deadline, agent abort.

**"Won't branching forever wreck your storage?"**

Content addressing removes duplicate bytes but not generation cost, thumbnails, lineage edges or
ACL checks. So branches are lazily materialised — only the selected variant gets full renditions,
the rest keep a thumbnail plus the recipe — reference-counted, expiring at 30 days unless
favourited or downloaded. Only delivered branches are billable.

**"Can't you reuse the App input schema as the tool definition?"**

You can, and you get technically valid but badly chosen tool calls. A widget schema says which
fields exist. It does not say what the call costs, how long it takes, whether it mutates or
creates, or whether it can be undone. So the tool contract is a separate versioned artifact
projected from the schema and enriched with exactly those things.

---

## On agent cost

**"Does a 480p two-second proxy actually predict a 1080p ten-second final?"**

Probably not for the things that matter — identity persistence over time, motion coherence,
temporal artifacts and lip sync are what a short low-res proxy is worst at. So I ship the
still-frame gate first, where identity is genuinely measurable by face embedding on a single image,
and the video proxy goes behind a flag measured per content class on rejection precision, recall
and credits saved. The question is not "is a proxy cheaper" — it is "how often does the proxy change
the decision correctly".

**"A session budget doesn't stop an agent spending all of it."**

Agreed, which is why it is a scoped authorization rather than a ceiling: `6 × short_video @150,
max 900 credits`, reserved atomically before every dispatch including concurrent ones, with a
stated maximum session liability. The product's "want me to continue?" is an authorization
boundary, so it is modelled as one.

**"How do you optimise LLM cost specifically?"**

Prefix caching over system prompt, tool contracts and constraint ledger — the prefix is stable
within a session so hit rates are high, and that is the biggest single lever. Then a router sending
routine dispatch to a cheap model and planning, critique and recovery to a frontier model. Eight
tools plus `search_catalog` rather than sixty definitions. Thumbnails with cached captions instead
of raw frames.

**"Prompts as data — how is that safe?"**

Not automatically, and that was a fair hit. Policy gets a typed schema, immutable signed versions
pinned per run, deny-by-default capability grants, mandatory review for anything touching tools,
spend or moderation, staged rollout, audit, and one-click rollback. The same publish protocol as
Apps.

---

## On evaluation

**"Creative output is subjective. How do you gate a release at all?"**

By gating on what is objective and being explicit that the rest is a screen. Merge blockers are
record–replay tests plus four zero-tolerance invariants: no spend beyond authorization, no
duplicate external charge, safety before delivery, output contract satisfied. Release blockers add
a 40-brief golden set asserting outcomes, plus cost per accepted artifact and p95 latency.

**"Asserting the agent uses the proxy first punishes an agent that found a cheaper path."**

Correct, so assertions are on semantic properties of the resolved request and on outcomes, not on
tool sequences or string presence. Multiple valid trajectories are allowed.

**"Fifty human tasks cannot detect a five-point improvement."**

They cannot, which is why I call it a regression screen rather than a non-inferiority gate. It
catches gross regressions. Quality deltas are decided offline on the larger set, and the 5% canary
watches only high-frequency low-variance signals — refusal rate, spend per session,
approval-decline rate, cancel rate — with a 2,000-session minimum before auto-promote. Power gets
spent where it exists.

---

## On the estimate

**"Ten weeks solo for all of that is not credible."**

For the platform, agreed — that is two quarters with four or five engineers. Ten weeks P50,
fifteen P90 is a production vertical slice, and the exclusions are stated: Studio is internal YAML
not a visual builder, single region, no tenancy, no invoice reconciliation, and production
hardening is explicitly outside the number. Risks are buffered individually rather than with one
blanket percentage, because provider heterogeneity, billing correctness and eval curation are not
one risk.

---

## Two things to volunteer rather than defend

Raising these first reads as judgement rather than as a gap being found.

1. **The billing lifecycle is described, not fully specified.** The states and the liveness
   mechanism are there; a complete design enumerates every transition and the reconciliation
   contract per provider. It is the section I would write next.
2. **Multi-region, workspace tenancy and mobile are deliberately absent** to hold four pages, and
   all three have real design content.
