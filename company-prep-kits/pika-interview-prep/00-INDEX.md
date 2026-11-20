# Pika Creative Platform — take-home system design

Staff SWE, Product / Backend / AI Infra. Take-home received 2026-08-20, submission built the
same day. Reply-to-email with the PDF attached; 4-page cap; 3–5 day turnaround offered.

**⚠️ The brief is confidential.** Pika's email states the project details are intended solely for
this candidacy and asks that they not be shared or distributed. `brief/` holds a copy because this
archive is private and it is my own candidacy record. Do not republish it.

## Read in this order

| File | What it is |
|---|---|
| **`01-architecture-detailed.md`** | **Start here.** The full design with the reasoning the 4-page limit cut. Alternatives rejected, failure modes, and the parts that are honestly unproven. |
| `07-interview-defence.md` | Anticipated pushback with short answers, derived from the adversarial reviews. Read before any follow-up interview. |

## Design lineage

The design went through two rounds of adversarial cross-model review. All three versions are kept
because the deltas are the interesting part.

| File | State |
|---|---|
| `02-design-v1-initial.md` | First full design, pre-critique. Contains a real self-contradiction: it claimed retry-scoped caching in one paragraph and cross-user memoisation in another. |
| `05-codex-critique-round1.md` | Codex cast as a hostile principal engineer at a competing AI-video company. Found the cache contradiction, that "an App is just data" was overstated, that the credit model was a phrase rather than a state machine, and that "persist `provider_job_id` before the call" is factually impossible. |
| `03-design-v2-post-round1.md` | Revised: three execution classes, unit-lifecycle billing, the two-identity split. |
| `06-codex-critique-round3.md` | Second pass, asked specifically whether the revisions fixed the findings or merely added vocabulary. Found two more real problems — run-lifetime authorization only relocated the expiry race into the reaper, and fixed retail pricing caps user liability without capping platform exposure. |
| `04-design-v3-final.md` | Design of record. What the PDF was typeset from. |

## What the design says, in five lines

1. An App's product surface — form, price, output contract, hand-offs — is **data**. One manifest
   drives all four, so no App needs frontend code and publishing is not a deploy.
2. That claim is bounded: **Class A** composed Apps are self-serve; **Class B** capabilities and
   **Class C** interactive handlers are engineering work. Naming the boundary is the answer to
   "can Growth ship an App without engineering".
3. Money is a **per-deliverable unit lifecycle** with an `UNKNOWN` state, leases rather than timers
   for liveness, and **two independent caps** — user credits and platform dollars.
4. Crash-safety and result-reuse are **different identities** (`attempt_id` vs `intent_id`), and
   byte-level dedup must never imply shared authorization.
5. The Part 2 agent is the **same orchestrator with a different planner**: `run_app()` starts the
   Part 1 run workflow as a child, so every App is a tool for free.

## Layout

```
00-INDEX.md                     this file
01-architecture-detailed.md     the full design
02-design-v1-initial.md         pre-critique
03-design-v2-post-round1.md     after critique 1
04-design-v3-final.md           design of record
05-codex-critique-round1.md     adversarial review 1, verbatim
06-codex-critique-round3.md     adversarial review 2, verbatim
07-interview-defence.md         likely pushback and answers
brief/
  take-home-brief.pdf           the original brief (CONFIDENTIAL)
  take-home-brief-text.txt      extracted text
  mockups/                      the three product mockups at native resolution
submission/
  submission-source.html                   HTML source; rebuild with the command below
tools/
  export_transcript.py          rebuilds the transcript from a Claude Code session log
```

The mockups matter more than usual. Three requirements came only from reading them at native
resolution and appear nowhere in the brief's prose: the Character Creator form is rendered from a
schema, results carry typed hand-off chips, and the agent asks permission before spending. The
first of those is the entire architecture.

## Rebuilding the PDF

Chrome headless, because it was the only locally available renderer with full print-CSS and inline
SVG support — `wkhtmltopdf`, `weasyprint` and `pandoc` were all absent.

```sh
"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" --headless --disable-gpu \
  --no-pdf-header-footer --print-to-pdf=out.pdf "file://$PWD/submission-source.html"
```

The HTML uses fixed-height `.page` divs rather than a flowing multi-column layout. That is
deliberate: Chrome fragments `column-count` badly across page boundaries — the flowing version
silently clipped text mid-sentence at the bottom of a column, and once emitted a nearly empty first
page. Verify any edit by rendering to PNG and measuring ink extents, not by eye. Body type is
10.05px, which was the largest size that still fits four pages without clipping.

## Transcript export

`tools/export_transcript.py` rebuilds the chat-history deliverable from
`~/.claude/projects/-Users-<you>/<session-id>.jsonl`. Idempotent and re-runnable — worth
re-running immediately before sending, since a transcript cannot contain its own creation.

It also does necessary privacy work. A raw Claude Code session log contains harness-injected
`attachment` records carrying the full memory index, none of which belongs in an employer's inbox.
The script drops those wholesale, redacts tool blocks that echoed those terms back into the log,
neutralises a few agent sentences that restated them, and strips base64 image payloads.
**Never send a raw session log to an employer.**

## Status

Submission built, not yet sent at time of writing. Send the PDF as the email reply, with the
transcript as a second attachment.

Weakest section if they push: the billing lifecycle is described rather than fully specified
(§7.3–7.5 of the detailed doc). The adversarial reviewer named it and was right. `07` has the
answer to give.
