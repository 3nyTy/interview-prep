# Raw Debate Artifacts — Reddit System Designs

Working material behind `../reddit-design-crib.md`. **Nothing in this folder is authoritative.**

**Reading order if you want the conclusions:** `../reddit-design-crib.md` (live-read, 10 points per design)
→ `../debate-record.md` (what was conceded and why) → this folder only when you want the raw argument.

| File | What it is | Trust it? |
|---|---|---|
| `NN-*-proposal.md` | Claude's Round-0 design, the input Codex attacked | **No** — each carries a banner listing its known errors |
| `NN-*-critique.md` | Codex's verbatim adversarial critique | As evidence, yes; some findings were rebutted — see `../debate-record.md` |
| `challenge-template.md` | The reusable adversarial prompt wrapper, personas, and CLI gotchas | Yes — reuse for the next design |

Numbering matches the crib: **01** Live Chat · **02** Notifications · **03** Leaderboard · **04** Video Rec.

## Provenance

- **Date:** 2026-08-14
- **Challenger:** Codex `gpt-5.6-luna` via `airchat codex`, Codex CLI v0.145.0, `--reasoning-effort high`
- **Protocol:** `codex-connector:debate`, one adversarial round per design, independent personas
- **Proposal author:** Claude Opus 5 (1M context)
- Source designs come from the 1point3acres Reddit bank captured 2026-08-13
  (`../../1p3a_question_bank.md`). Only the **4 SWE-tagged** designs are here; ML Feature Store,
  Post/Comment Ranking and Comment-Prediction are MLE-only and were deliberately excluded.

## Why the proposals are kept at all

They are the only record of *what the wrong answer looked like*, which is the part that transfers. Three of
the four were broken by **a number rather than a concept** — an invented coalescing multiplier, an error
bound measured against the wrong quantity, a memory estimate off by ~10x. Re-reading a superseded draft
alongside its critique is a better drill than reading the corrected crib alone, because the crib no longer
shows where the reasoning went wrong.

Each proposal therefore opens with a blockquote banner naming its specific defects. **Do not strip those
banners** — without them these files read as finished designs.

## Not kept

The raw `airchat codex` session logs (~50 KB each). They are the echoed prompt plus the same response
already saved as `NN-*-critique.md`, with no unique metadata. The challenge files are not kept separately
either — they are the persona wrapper (in `challenge-template.md`) concatenated with the proposal.

## Re-running

See `challenge-template.md`. Two gotchas that cost time on the first attempt: redirect **stdin from
`/dev/null`** or Codex blocks forever, and run **at most two concurrently** or they race on the shared
plugin cache.
