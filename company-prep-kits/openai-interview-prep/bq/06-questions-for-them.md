# Questions to ask them

OpenAI's own prep doc lists this as a tip: *"Ask questions about OpenAI — this is also your
evaluation of them."* So this is **scored**, not courtesy.

> ## ⚠️ GENERIC LOOP — use this set, not Tier 1 below
> The loop is generic and **team matching happens after it**, so the interviewer probably isn't from
> the API team. Asking *"how does this team decide when to break a contract?"* gets you a shrug.
>
> **Ask these:**
> 1. ⭐ **"How does team matching work after the loop? What actually drives where someone lands?"** —
>    the highest-information question available to you.
> 2. ⭐ **"Given what you've heard, which teams do you think this background maps to?"** — makes them
>    advocate for you out loud, which has a real effect on the debrief.
> 3. **"What's the difference between a strong senior engineer and a Staff engineer here? I'd rather
>    calibrate to your bar than my last company's."**
> 4. **"What's hardest about working here that people underestimate before they join?"**
> 5. **"You've grown enormously fast — what hasn't caught up yet?"** (tests whether they'll be straight)
> 6. **"Is there anything about my background that gives you pause?"** (also the flat-room rescue move)
> 7. **"I stayed five and a half years at Amazon because the problem kept outgrowing me. Honestly — is
>    there that kind of depth here, or is it a two-year sprint?"** (real question *and* pre-empts
>    flight-risk)
>
> **Hold Tier 1 below in reserve** — deploy it only if the interviewer turns out to be from a platform
> or infrastructure team.

**Budget:** 2–3 mid-round (one roughly every 12 minutes), 3–4 at the end. Do not save them all —
a quiet HM will have decided before you get there.

**Rule:** ask things only *this person* can answer. Anything answerable from the blog costs you.

---

## Tier 1 — the four that make you sound like you already work there

Pick two of these. They're built on the actual hard problem of an API platform, which is the
thing your background is *for*.

### 1. The moving-substrate question ⭐ your single best one
> *"The thing that looks hardest to me about this platform from the outside: what's behind the
> interface changes every few months — new model families, new modalities, reasoning models that
> behave differently from completion models — while developers need the surface to hold still.
> How does the team decide when to break a contract versus carry the old shape forward?"*

**Why it's good:** it names the central tension of the job, it's unanswerable from public
material, and it hands you the opening to say *"that's the problem I've spent five years on"* —
raising a hard capacity ceiling and replacing a live fleet underneath customers without breaking
the contract.

### 2. The research↔platform seam
> *"How much does this team get to shape what research ships, versus receiving a capability and
> then figuring out the interface for it? I'm asking because those are quite different jobs."*

Reveals whether the team is a design partner or a downstream service org. That's the single most
important thing for you to know about this role, and asking it shows you know the difference.

### 3. Where the guardrail lives
> *"On a platform you're handing a lot of capability to people you don't control. Where does this
> team's responsibility for how it gets used start and stop — how much of the guardrail is your
> layer versus the developer's?"*

Signals that you think about safety as an ownership-boundary question rather than a slogan. Sets
you up to mention allowlisted tools, scoped permissions, tenancy isolation and abuse detection.

### 4. The forward-looking one — they've said they care about this
Their prep doc flags interest in candidates who **anticipate future challenges in the AI
industry**. So ask something that only makes sense if you've thought ahead:
> *"What do you think the API looks like when most of the callers are agents rather than
> applications? A lot of what an API surface assumes — one caller, one intent, predictable call
> patterns, humans reading errors — seems to stop holding."*

This is the question most likely to make the HM lean in, and it's the one that best serves the
"do they see a long-term future here" criterion.

---

## Tier 2 — role and expectations

5. *"What does a strong first six months look like in this role? And what would tell you at
   twelve months that hiring me was right?"*
6. *"What's the difference between a strong senior engineer and a Staff engineer on this team?
   I'd rather calibrate to your bar than to my last company's."*
7. *"What does this team own that surprises people?"*
8. *"Where is the team losing time right now — what's the recurring tax you'd want someone to go
   kill?"*

Q8 is the best of these. Managers usually have an immediate, specific answer, and it tells you
what you'd actually do on day one.

---

## Tier 3 — the honest ones (use one, late)

9. *"What's the part of working here that people underestimate before they join?"*
10. *"Is there anything about my background that gives you pause? I'd rather address it now than
    leave you guessing."*
    → Also the quiet-room rescue move. See `05-quiet-hm-playbook.md`.
11. *"You've grown enormously fast. What's the thing that hasn't caught up yet?"*

Q11 is a genuine test of whether they'll be straight with you. A manager who claims nothing is
broken at a company that size is either not paying attention or not being candid — and either way
that's information you want.

---

## Two you should ask because *you* need the answer

You are weighing a competing Staff offer with a near-term start date. Ask the things that would
actually change your decision:

12. *"I stayed five and a half years at Amazon because the problem kept outgrowing me. Honestly
    — is there five years of depth in this charter, or is it a two-year build followed by
    maintenance?"*
    **This does double duty:** it's a real question, and it pre-empts the flight-risk concern by
    showing you're optimising for staying rather than for joining.

13. *"How does the team actually work — how much is deep individual ownership versus tight
    collaboration? And what's the on-call and incident culture like?"*
    You've owned on-call rotations and postmortem-driven design; you want to know if that
    discipline exists or if you'd be building it.

---

## Don't ask

- ❌ Comp, equity, refresh, levelling — recruiter's conversation, not this one.
- ❌ Anything answerable from the blog ("what models are you working on?").
- ❌ "What's the culture like?" — too open; you get a platitude and burn a slot.
- ❌ Remote/WFH policy in this round (find out from the recruiter).
- ❌ More than two questions in the mid-round slots. It's your interview to be interviewed in.

---

## Write these three on a card

If you only get 90 seconds at the end:
1. The moving-substrate question (#1).
2. *"Where's the team losing time right now?"* (#8).
3. *"Anything about my background that gives you pause?"* (#10).
