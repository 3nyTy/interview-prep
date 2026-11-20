# Valon — Staff Software Engineer

| Folder | What it is | Status |
|---|---|---|
| `behavioral/` | Behavioral-round brief + spoken scripts Q1–Q12 | ⭐ **live — round is Monday 24 Aug 2026** |
| `coding/` | Java solutions. **Two different problems** — see its README | done, 147 assertions passing |
| `questions/` | 7 raw 1point3acres captures | reference |
| `clean/` | the same 7, cleaned | reference |

## Round history

| Date | Round | What was actually asked |
|---|---|---|
| 2026-08-20 | Domain Coding | **Loan ledger** — fee/payment events, then out-of-order backdated accounting events |
| 2026-08-24 | Behavioral / values | six operating standards — see `behavioral/prep.html` |

⚠️ **The 1point3acres bank predicted the wrong problem.** All seven scraped variants are an
in-memory SQL database, and that is *not* what the domain-coding round asked. The lesson worth
keeping: 1p3a carries Valon's **OA and technical-screen** problems, while the **Domain Coding**
round is a mortgage-domain problem written by Valon. Prepare both; don't let the public bank
set your expectations for the domain round.

## The thread between the two rounds

The loan-ledger problem is a miniature of ValonOS's first listed primitive — a
*"bitemporal ledger: immutable yet reversible events."* Backdated events, replay, and the fact
that a payment waterfall is order-dependent and non-invertible are all the same subject. That
connection is a scripted talking point in `behavioral/answers.md` (Q3), and it is the strongest
single move in the behavioral round because it cannot be faked.

## Where the behavioral material also lives

Mirrored into the private archive at
`personal-archive/interview-prep/valon-preparation/`, and published as a private page at
<https://claude.ai/code/artifact/cfcdeea0-b8dc-4903-bc82-4699af5263a4>.
