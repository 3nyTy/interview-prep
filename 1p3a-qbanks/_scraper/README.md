# 1point3acres qbank scraper

Scrapes curated interview questions from the logged-in 1point3acres session by driving
Chrome with AppleScript. No credentials are stored here; it borrows whatever session the
Chrome tab already has.

## Requirements

- Google Chrome running, signed into 1point3acres, with **View > Developer >
  Allow JavaScript from Apple Events** enabled.
- One tab open on any `1point3acres.com/interview/problems` URL. The scripts find that tab
  by URL substring and never navigate it.
- `python3`.

## How it works

The site is Next.js plus a tRPC backend on a separate host. Two things make this cheap to
scrape:

1. **The whole company list comes from one call.** `trpc.1point3acres.com` exposes
   `interview.getCombinedCompanyList` with `{page, pageSize, search}`. `pageSize: 5000`
   returns all ~1,227 companies with `qbank_count` / `oj_count` / `external_count` each.
   The rendered index paginates 24 at a time through React state, so driving the UI is not
   needed.
2. **Each question page embeds its own data.** `__NEXT_DATA__.props.pageProps` carries
   `overview`, `answerText`, `tags`, `roles`, `category`, `sourceCount`, `isLocked`, plus
   `duration` / `Frequency` / `Last asked` / `Stage` from the rendered
   `div.grid.grid-cols-3` metadata block.

Question statements only are assembled into the deliverable; `answerText` stays in the
`raw/<slug>.txt` archive.

## Files

| file | role |
|---|---|
| `gen.py plan` | build `plan.json` from a `companies.json` dump (skips already-stored banks) |
| `gen.py phaseA` | one JS worker per company, returns its question slug list |
| `gen.py phaseB` | batched detail-fetch JS workers |
| `gen.py merge` | merge all `res/*.json` into `records.json`, write `missing.json` |
| `gen.py retry` | regenerate workers for everything still missing |
| `run.sh <jsdir> <outdir> <delay>` | run workers in the Chrome tab, paced, resumable |
| `scrape.sh` | full run: initial batches then retry rounds until nothing is missing |
| `assemble.py` | write `questions.json` + `qbank.md` + `raw/` per company |
| `runjs.applescript` | the Chrome driver: executes a JS file in the matching tab |

## Re-running

```bash
cd /tmp && rm -rf qb && mkdir -p qb/{out,res,detail,retry}
cp _scraper/{gen.py,run.sh,scrape.sh,assemble.py} qb/
cp _scraper/runjs.applescript /tmp/
cp _scraper/state/plan.json _scraper/state/missing.json _scraper/state/queue.json qb/
cp -r _scraper/state/phaseA_out qb/out
cd qb && python3 gen.py retry && ./run.sh retry res 2.5 && python3 gen.py merge && python3 assemble.py
```

`gen.py retry` only queues what `missing.json` lists, so a re-run costs only the gaps.

## The Cloudflare ceiling, and how this run got past it

**Cloudflare starts returning 403 (an interactive JS challenge) after roughly 1,330
requests in a session.** In the 2026-09-20 run, requests 1-1,330 all returned 200 and
every request after that returned 403.

**The block is transient, not terminal.** It cleared after roughly 30-40 minutes of slow
paced retrying, and the run then finished the remaining 418 questions. Final state:
1,748 of 1,748 captured. So the correct response is to wait it out in slow rounds, not to
abandon the run.

Pacing does not raise the ceiling: the first attempt failed at ~5.7 req/s and the second
at ~2.2 req/s hit the same wall, so this is cumulative volume, not congestion. Once the
challenge clears, roughly another 1,300 requests are available.

Two rules for surviving it:

1. **Never delete results in order to retry.** The first version of `scrape.sh` cleared the
   previous round's output before re-fetching, so a 403 in round N+1 destroyed records
   round N had already captured (good records went 1,598 back down to 1,480). Each round
   now writes to its own `res/round_N/` and `gen.py merge` reads all of them, which makes
   progress monotonic.
2. **Stop when progress stops.** `scrape.sh` halts after three consecutive rounds that gain
   nothing, so a still-blocked session is not hammered.

Do not attempt to solve or bypass the challenge. Waiting and pacing is what worked.
