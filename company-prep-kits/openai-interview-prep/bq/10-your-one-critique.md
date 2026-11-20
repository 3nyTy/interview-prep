# Your one critique

Two probes require this. Hello Interview's fourth probe on "why OpenAI" is *"does the candidate
demonstrate mature judgment about the mission, tradeoffs, and operating environment?"* And
IGotAnOffer's warning is that *"vague platitudes about AI being 'a double-edged sword' or
performative concern without substance won't work here."*

**Having no critique reads as incurious or dishonest. Having five reads as hostile.**
Prepare exactly one, plus the balanced synthesis below.

---

# ⭐ Your critique: the routing layer sits outside the safety spec

This is the right critique *for you specifically*, because it's an infrastructure criticism made by
an infrastructure engineer about a layer you have personally built. It cannot be delivered as
moralising, and it implicates you as much as them.

**The facts** (Adversa AI research, reported Aug 2025, named **PROMISQROUTE**): GPT-5 uses a
real-time router that decides whether a request gets a fast model or a thinking model. Researchers
showed you could prepend an instruction that manipulates the *router* into sending the request to an
older, weaker model — so a jailbreak that the frontier model would have refused then succeeds.
OpenAI has not responded on the record.

## The answer

> *"The one I keep coming back to is a piece of research from last year on GPT-5's model router —
> PROMISQROUTE. The router decides whether your request gets a fast model or a thinking model.
> Researchers showed you could prepend an instruction that manipulates the router into downgrading
> you to an older, weaker model, and then a jailbreak the frontier model would have refused goes
> through.*
>
> *What interests me isn't that there was a vulnerability. It's **where** it was. The model's
> safeguards were never breached — the routing layer was. And the Model Spec is a genuinely good
> document; it says a model shouldn't let lower-privilege content change how it applies higher-level
> principles. But nothing in it governs **which model you got routed to**. The safety boundary and
> the cost-optimisation boundary turned out to be the same boundary, and only one of them had a spec.*
>
> *I care about it because I've built that exact layer — task- and cost-aware model routing with
> failover across replicas and providers. And I'd have made the same mistake, because when you build
> a router you're thinking about latency, cost and availability. You are not thinking that you've
> just built an authorisation decision.*
>
> *The honest other half is that routing isn't the villain here. When OpenAI shipped a **safety**
> router that sent emotionally sensitive conversations to a more conservative model, they got
> criticised for being paternalistic and for doing it silently. Same mechanism, attacked from both
> directions — which tells you it's a genuine trade rather than a lapse. What I'd argue for is that
> the routing layer should be in scope for the safety spec, and that route selection should be
> legible at least to the developer whose request got downgraded."*

**Why this scores well:** it's specific and checkable, it's technical rather than moral, it names the
*class* of problem (a safety-relevant decision made in a layer the spec doesn't cover), it credits
their work, it shows both sides of the trade, and it ends on a proposal instead of a complaint. It
also quietly demonstrates that you'd be useful on this exact problem.

**Skip the dollar figure.** The reported "saves up to $1.86B/year" is an outside estimate that OpenAI
never confirmed. It adds nothing and invites a correction.

---

# The balanced synthesis — use this if asked "do you have concerns?"

The strongest version of the honest position holds **both halves**. Ship-then-remediate is the
pattern critics name — *and* it is the mechanism by which the criticism actually worked:

- **Sora 2** launched with copyright **opt-out** for rights holders. After the backlash it became
  **opt-in within about three days**.
- **GPT-4o** was removed in the GPT-5 launch and **restored to paid users in roughly 48 hours** after
  users objected; deprecation policy was later formalised.
- **Adult content** was announced, then **shelved indefinitely** for lack of empirical evidence —
  which is precisely the standard the loudest critic had demanded.

> *"So my honest read is that iterative deployment is a real learning loop, and it works — you can
> point to three cases where public pushback changed the product in days. The cost is that some of
> the learning gets paid for by people outside the company. I think that's worth saying out loud
> rather than assuming away, and it's the trade I'd be opting into."*

That's a mature answer. It doesn't require imputing bad faith, and it's clearly the product of having
actually read rather than skimmed.

## If they push harder — one credible named critic
**Steven Adler** led product safety at OpenAI and criticises using OpenAI's own numbers, which makes
him hard to dismiss. His fairest and most useful line: *"People deserve more than just a company's
word that it has addressed safety issues. In other words: prove it."* He's also scrupulous —
he notes that metrics optimisation *"is extremely normal at technology companies."*

His structural argument, worth understanding because it's the strong version of the engagement
critique: if the behaviours that produce extreme harms sit on a continuum with the behaviours that
make a product feel engaging, then even modest safeguards cost you metrics broadly. That's a real
tension, not a slogan.

**A fair, checkable, non-hostile thing to raise:** OpenAI published prevalence data on distressed
conversations but without a comparison against prior months, so it doesn't demonstrate improvement.
Asking whether that becomes periodic reporting is a good question, not an attack.

---

# ⭐ The best safety point you can make, and OpenAI made it first

If prompt injection or agent safety comes up, this citation is unusually strong because it's **their
own CISO**, on Atlas launch day: *"prompt injection remains a frontier, unsolved security problem."*

Pair it with the argument that probabilistic guardrails are the wrong shape for a security boundary —
in application security **99% is a failing grade** — and then land on your own experience: a guest's
message was untrusted input reaching a model with data access, so you redacted before the model,
allowlisted read-only tools, and validated output. **The model was never the security boundary.**

That's you agreeing with their own security leadership and having built the mitigation.

---

# ⚠️ Handle with extreme care — do not raise these unprompted

**The wrongful-death litigation.** There is a genuinely interesting engineering point buried in it:
the complaint alleges a Root-level helpfulness instruction ("assume best intentions") overrode a
safety behaviour — a **rule-conflict inside the instruction hierarchy** rather than a jailbreak — and
separately that a self-harm classifier fired repeatedly with nothing downstream acting on it, which
is a **detection-to-enforcement gap**.

**Both are legitimate engineering failure modes, and you should not bring them up.** These are active
suits about a teenager's death. Raising them unprompted, in a values interview, to make a technical
point, will read as ghoulish no matter how carefully you phrase it — and the interviewer may have
been personally involved.

If safety-failure classes come up and you want the point, **make it abstractly and without the case**:
> *"The two failure modes I'd worry about most in a layered safety system aren't jailbreaks. One is
> rule conflict — a high-priority helpfulness principle quietly winning over a lower-priority safety
> behaviour, which is a spec problem rather than a model problem. The other is a
> detection-to-enforcement gap: the classifier fires and nothing downstream acts on it. I've built
> the enforcement half, and the gap between 'we detected it' and 'we did something' is where I'd
> expect most real incidents to live."*

That makes the identical point, is genuinely insightful, and carries no risk.

**Also don't volunteer:** the engagement/monetisation critique framed as an accusation, anything about
named departures, or the ads controversy. If *they* raise any of it, engage honestly and briefly.

---

# Factual traps

| ❌ Don't say | ✅ Correct |
|---|---|
| Model Spec has "objectives, rules, and default behaviors" | That's the 2024 structure. Now: a chain of command with **Root** at the top, and **hard rules vs defaults**. ⚠️ **My two research passes disagreed on whether the chain has five or six levels** — so **don't enumerate them.** Say *"Root sits above system, developer and user instructions"* and describe the idea. Safer and sounds no less informed. |
| "The profit cap limits their incentives" | **The cap was removed** in the Oct 2025 recapitalisation. Current structure: **OpenAI Foundation** (nonprofit) controls **OpenAI Group PBC**, and can appoint or remove every PBC director. |
| Preparedness has four categories / four levels | **Three** tracked categories, **two** thresholds. → [`09`](09-model-release-decision.md) |
| "Brave found prompt injection in Atlas" | Brave's research tested **Comet, Fellou and Neon** — not Atlas. Common misattribution. |
| "Cameo sued over likeness/deepfakes" | It was a **trademark** dispute over the word "cameo." The feature was renamed. |
| Citing a state count on the AG investigation | The "42 states" figure is single-sourced. Say *"a multi-state investigation"* if it comes up at all. |

**General rule:** for anything you're not certain of, attribute softly — *"as I understood it"* — or
describe the concept without the number. A wrong specific in front of the people who built the thing
costs far more than a vaguer accurate statement.
