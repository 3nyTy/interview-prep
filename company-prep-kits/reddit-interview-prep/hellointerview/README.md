# Reddit questions from Hello Interview (captured 2026-08-13)

Source: https://www.hellointerview.com/community/questions?company=Reddit&sort=recentAndPopular&page=1

Nine questions, the complete Reddit set — page 2 of the same filter returns zero results and
the pager renders a single page. The "12,546 total reports" figure on that page is site-wide
across all companies, not Reddit's count.

All nine detail pages were read, so every file holds the verbatim prompt plus whatever
requirements, deep dives, and red flags the page carries.

Sibling capture: `../1p3a_question_bank.md` (1point3acres, same day, 49 items).

## The nine

| Question | Type | Reports | Levels reported | File |
|---|---|---|---|---|
| Give me an example of a tough or critical piece of feedback you received | behavioural | 1 | Senior | `behavioral_tough_feedback.md` |
| How do you use AI in your work or personal projects? | behavioural | 1 | Senior | `behavioral_ai_in_work.md` |
| Describe one thing you have learned recently | behavioural | 1 | Senior | `behavioral_learned_recently.md` |
| Tell me about a project you worked on and what your role was | behavioural | 1 | Senior | `behavioral_project_and_role.md` |
| Tell me about your most challenging project | behavioural | 1 | Senior | `behavioral_most_challenging_project.md` |
| Design a Circuit Breaker | **low-level design** | 1 | Senior | `design_circuit_breaker.md` |
| Design a Notification System | system design | 3 | Manager ×2, Senior | `design_notification_system.md` |
| Design a News Aggregator System | system design | 3 | **Staff ×2**, Manager | `design_news_aggregator.md` |
| Design a historic 'hot' posts page | system design | 1 | Senior | `design_hot_posts_page.md` |

Levels come from each question's timeline on its detail page. The listing page does not show
them.

## Two things the detail pages corrected

Design a Circuit Breaker is a low-level design question, not a distributed one. The prompt
asks for a thread-safe three-state component with configurable thresholds, fallbacks, and
metrics — an OOD exercise belonging beside `../coding/`, not on a whiteboard.

Design a News Aggregator System is the Staff-level question. Two of its three reports are
Staff and both are recent (Dec 2025). Notification System, despite tying on report count, is
Manager-track twice over with its only IC report from Jan 2024.

## What this changes

Zero overlap with `../coding/`. Hello Interview reports no coding questions for Reddit at
all — five behavioural, three system design, one low-level design. The 14 coding solutions
in `../coding/` are corroborated by `../1p3a_question_bank.md`, not by this source.

Four of the five behaviourals map onto scripted cards in `~/openai-interview-prep/bq/`
(⑰, ⑩+⑤, ⑲+⑤, and the AI-usage answer in `11-question-list.md` row 13 /
`13-lower-priority.md`).

Open work, in order:

1. `design_news_aggregator.md` — Staff level, recent, no counterpart among the 13 validated
   designs. Missing: near-duplicate clustering, fanout-on-write versus on-read, stable
   pagination under insertion.
2. `design_hot_posts_page.md` — the other real gap. Reddit-only question. Missing: hot
   formula with time decay, Top-K without full scans, snapshot retention.
3. `design_notification_system.md` — cheapest write-up of the three. Deep dives 1, 3, and 4
   are answerable from `memory/project_webhook_delivery_design.md` nearly unchanged; only
   priority isolation between traffic classes is uncovered.
4. `behavioral_learned_recently.md` — the only unscripted behavioural. Its rubric wants
   learning that changed how a team operates, not a technology picked up.
5. `design_circuit_breaker.md` — implement in Java beside the other OOD solutions.

## How this was captured

The listing and all nine detail pages are client-rendered; `curl` and WebFetch return the
shell with zero question text. The working route was Apple Events against a logged-in Chrome:

1. In Chrome: View → Developer → Allow JavaScript from Apple Events. Off by default, stored
   per profile in `<profile>/Preferences` as `browser.allow_javascript_apple_events`. The
   macOS `defaults` key of the same name is ignored, and `osascript` has no Accessibility
   grant on this machine, so this step is manual. Worth switching back off afterwards.
2. Navigate a tab and read the DOM:

```sh
osascript -e 'tell application "Google Chrome" to execute front window'"'"'s active tab javascript "document.body.innerText"'
```

Notes for whoever repeats this:

- `airchat mcp start playwright` no longer exists — `airchat mcp start` accepts only
  `proxy`, so the `/playwright-start` skill fails. No Playwright was used for this capture.
- Detail pages need roughly 6 seconds after navigation before the content renders.
- Hello Interview's prose breakdowns and model answers are deliberately not mirrored into
  these files, only the prompts, requirements, deep dives, and red-flag headings. The rest
  is on the source pages, which need a logged-in session.
