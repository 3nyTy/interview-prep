# Robinhood questions from Hello Interview (captured 2026-08-14)

Source: https://www.hellointerview.com/community/questions?company=Robinhood&sort=recentAndPopular&page=1

Nine questions. This is the complete Robinhood set — the pager renders a single page. The
"12,546 total reports" figure on that page is site-wide across all companies, not Robinhood's
count.

Sibling capture in this folder: the 1point3acres bank (`../README.md`, `../01`–`../06`),
taken 2026-08-13. The two sources overlap on one question only. Read the reconciliation
section below before you prepare either.

## The nine

Also in this folder: `autotiering_leverage_frame.md` — the auto-tiering story rewritten against
the Staff impact rubric found on the impactful-project page. Reusable in any Staff loop.

| Question | Type | Levels reported | Reports | Last asked | File |
|---|---|---|---|---|---|
| Design a Job Scheduler | system design | Junior–Manager | **67** | Mid July 2026 | `design_job_scheduler.md` |
| Design a stock trading platform | system design | Mid-level–Staff, Manager–Senior Manager | 13 | Mid June 2026 | `design_trading_domain.md` |
| Design a Live Stock Price Ingestion System | system design | **Staff**, Manager | 2 | Late May 2026 | `design_trading_domain.md` |
| Design a Stock Brokerage System | system design | Manager | 1 | Early April 2026 | `design_trading_domain.md` |
| Design a Job Board System | system design | **Staff** | 1 | Late May 2026 | `design_job_board_system.md` |
| What was the biggest technical challenge you faced on a recent project? | technical project retrospective | **Staff** | 1 | Late May 2026 | `behavioral_and_retrospective.md` |
| Past Project Technical Overview | technical project retrospective | **Staff** | 1 | Late May 2026 | `behavioral_and_retrospective.md` |
| Tell me about something you are most proud of and why | behavioral | **Staff** | 1 | Late May 2026 | `behavioral_and_retrospective.md` |
| Describe your most impactful project | behavioral | **Staff** | 1 | Late May 2026 | `behavioral_and_retrospective.md` |

Levels and report counts come from the company-filtered listing page. Reports count users who
said they were asked the question at Robinhood.

## What this source adds

**The trading domain.** Three of the five system design questions are trading systems: a
trading platform, a price ingestion system, and a brokerage system. The 1point3acres bank has
no trading system design at all — its three entries are Job Scheduler twice and Calendar
Booking. If you prepare only from that bank, you prepare no trading design.

The three are **not one question at three scales**. The trading platform states 100 writes per
second and matches orders itself. The brokerage states 2M DAUs and submits to an external
exchange. Price ingestion gives you two TCP connections. Sizing one does not size the others.

Hello Interview also publishes a full breakdown of this exact problem shape:
https://www.hellointerview.com/learn/system-design/problem-breakdowns/robinhood

**A level signal.** All four non-design questions are reported at Staff, and all four are the
same request in different words: walk me through a project. Two are filed as "technical
project retrospective", two as "behavioral". The 1point3acres bank says the same thing —
"project deep dive" is its highest-frequency behavioral item, 60 minutes. Two independent
sources now agree that the Staff loop leans on one project story.

**A Staff rubric for that story.** The impactful-project page states the top-priority Staff
probe: results explained credibly and proportionally, where credible impact at Staff means
"broader adoption, capability creation, or systemic improvement — not just one successful
deliverable". It also warns that a Staff candidate who tells a purely individual-contributor
story "signals limited operating range". Two consequences for existing prep, both in
`behavioral_and_retrospective.md`: the unverified metrics need settling, and auto-tiering needs a
leverage frame.

**Job Scheduler is the flagship.** 67 reports against 13 for the next question. Both sources
rank it first. Its three visible reports are Senior, Senior, and Mid-level — the listing spans
Junior–Manager, so it is the question everybody gets, not a Staff filter.

**The trading platform is also a phone screen.** One Staff report from Mid May 2026 is annotated
"initial screening". Another, at Manager, was scoped down to "the write portion of how do you buy
and sell limit orders".

## The Job Scheduler conflict — read this

The two sources state **opposite delivery guarantees** for the same question.

| | 1point3acres | Hello Interview |
|---|---|---|
| Guarantee | **at-most-once** per scheduled run | **at-least-once** with idempotency |
| Named mechanism | compare-and-set on a claim/lease record | leases, heartbeats, dedupe keys |
| Listed red flag | claiming at-most-once *and* unlimited retry | promising exactly-once with no idempotency |
| Throughput | not stated | 10,000+ jobs/sec, sub-2s precision |
| History | log inspection | one year, queryable |

Both sources describe the same trap from opposite sides: a worker claims a run, starts, then
crashes, and you cannot tell whether the side effect happened. At-most-once forbids the
retry. At-least-once permits it and makes the handler idempotent.

Do not walk in with one answer. Ask which guarantee the system needs, then say why the other
one is wrong for it. The two prompts also differ in scale — 1point3acres describes a single
job type, Hello Interview describes 10,000 jobs per second — so confirm the scale before you
size any component.

## Detail-page coverage

**All nine detail pages captured.** Detail pages are not paywalled; they render for a signed-out
reader too. Only the listing hides rows.

**Editorial depth tracks report count, not level.** Only three of the nine pages carry
Hello Interview's own analysis. The rest are a prompt, an asked-at list, and a timeline.

| Question | Reports | On the page | URL |
|---|---|---|---|
| Design a Job Scheduler | 67 | **full breakdown** + answer key | `/community/questions/job-scheduler-cron-history/cm7w828yv00sejmejlmofwtvp` |
| Design a stock trading platform | 13 | **full breakdown** + answer key | `/community/questions/stock-trading-platform/cmbzeshg200003p6wbg45hbsi` |
| Describe your most impactful project | 1 | **full behavioural rubric** | `/community/questions/impactful-project/cm6jwzhtl00vlui4bkj7k4lcd` |
| Design a Live Stock Price Ingestion System | 2 | prompt + **candidate note** | `/community/questions/stock-price-ingestion/cmmz3vla91fgx08adslyagdpy` |
| Design a Stock Brokerage System | 1 | prompt + **candidate note** | `/community/questions/stock-brokerage-system/cmnw96bap0a1j08ad8ef63blw` |
| Design a Job Board System | 1 | prompt only | `/community/questions/job-board-system/cmq52v6650lci08adf9wofnpx` |
| Biggest technical challenge | 1 | prompt only | `/community/questions/technical-challenge/cmkcro2a305gj07ada9sa3yfm` |
| Past Project Technical Overview | 1 | prompt only | `/community/questions/technical-project-overview/cmmmozzdx00ht07ade785rs7k` |
| Something you are most proud of | 1 | prompt only | `/community/questions/proud-accomplishment/cmpa3ore50wv108adqpvyv6ee` |

The impactful-project rubric is the exception that proves the rule — it has one Robinhood report
but 24 companies overall, so Hello Interview wrote it up for the general case. Its rubric grades
all four project questions here, so read it even though its Robinhood count is 1. It is
reproduced in `behavioral_and_retrospective.md`.

**Read the candidate notes before the prompts.** On the two ingestion and brokerage pages the
one-line published prompt omits the constraint that defines the question. Live Stock Price
Ingestion gives you **2 TCP connections to the exchanges**. Stock Brokerage is **2M DAUs** with
orders that stay open for **days**, and asks "which DB and why" outright. Neither appears in the
published prompt. Details in `design_trading_domain.md`.

## How this was captured

The listing page is client-rendered. `curl` and WebFetch return the shell with zero question
text.

Four of the nine questions are hidden behind the premium paywall when you are signed out —
the listing replaces them with `Lorem ipsum` placeholder rows and an "Unlock all 9" call to
action. Their links are absent from the DOM, which is why four URLs above are missing.

The listing was read from the logged-in premium session in the main Chrome profile, through
Apple Events:

```sh
osascript -e 'tell application "Google Chrome" to execute front window'"'"'s active tab javascript "document.body.innerText"'
```

The nine detail pages were then read with a loop over the same tab:
`scratchpad/capture_all.sh`, which calls `get_links.applescript` for the hrefs and
`read_page.applescript` per page.

Notes for whoever repeats this:

- Chrome needs View → Developer → Allow JavaScript from Apple Events. It is off by default and
  stored per profile in `<profile>/Preferences` as `browser.allow_javascript_apple_events`. It
  was already enabled on this machine from the 2026-08-13 Reddit capture.
- Playwright cannot reach the logged-in session. Playwright needs a Chrome started with
  `--remote-debugging-port`, and **Chrome 136 and later ignore that flag on the default
  profile**. `airchat util start-browser` therefore launches a separate, signed-out profile at
  `~/chrome-debug-profile`. Cloning the session cookies into that profile is the usual
  workaround and it was refused by the sandbox, correctly.
- The signed-out Playwright profile is still useful: detail pages render in full without a
  login, so only the listing needs the premium session.
- Poll `document.body.innerText.length` and stop when it is unchanged across two reads two
  seconds apart. A fixed delay either truncates the long pages or wastes 30 seconds on the short
  ones, which range from 1.5 KB to 8.7 KB.
- The href on the listing **already carries `?company=Robinhood`**. Appending it again produces
  `?company=Robinhood?company=Robinhood`, which the site accepts but reads as a company named
  `Robinhood?company=Robinhood`, and the timeline then renders empty. The first pass lost every
  timeline this way, which is where the candidate notes live.
- Hello Interview's prose breakdowns and model answers are deliberately not mirrored here, only
  prompts, requirements, deep dives, red-flag headings, and the verbatim candidate notes.

## Prep priority

1. **Design a Job Scheduler.** 67 reports, ranked first by both sources, and already largely
   built in `memory/project_cicd_scheduler_design.md`,
   `memory/project_webhook_delivery_design.md`, and
   `memory/project_sora_scheduling_design.md`. The work left is the guarantee conflict above,
   not the architecture. See `../04-system-design.md` for the 1point3acres write-up.
2. **The trading cluster.** Three questions, one domain, and the largest genuine gap — no
   validated design covers an order book, a two-connection constrained ingest, or market-data
   fan-out. The trading platform has a full breakdown and 13 reports, so start there. See
   `design_trading_domain.md`.
3. **The one project story.** Four Staff-level reports ask for it, and there is now a Staff
   rubric to test the story against. Two concrete fixes are open, both in
   `behavioral_and_retrospective.md`: settle the unverified metrics, and reframe auto-tiering for
   leverage. Cheaper than a design and it covers four of the nine questions.
4. **Design a Job Board System.** One report, Robinhood-only, no breakdown, and the only
   question here with no trading and no scheduling content. Lowest value of the five designs.
