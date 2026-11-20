# Design a Circuit Breaker

- Source: https://www.hellointerview.com/community/questions/api-circuit-breaker/cmj91hwwm01b408adkioh0xyh?company=Reddit
- Type: low-level design, not distributed system design — see below
- Reddit reports: 1 — Late June 2026, level Senior
- Also reported at: Microsoft
- Captured: 2026-08-13 (detail page read)

## Prompt as published

> Design a low-level circuit breaker system for managing API calls to external
> dependencies, supporting three states (CLOSED, OPEN, HALF_OPEN) with configurable failure
> thresholds, timeout durations, and automatic state transitions. The system should be
> thread-safe, provide fallback responses, and expose metrics for monitoring service
> reliability.

## Reclassification

This is a low-level design question, not a system design one. "Low-level", "thread-safe",
and "expose metrics" put it in the same family as the OOD questions in `../coding/` —
a single-process component with a state machine, concurrency, and a clean interface. Prepare
it as code, in Java, alongside the existing solutions rather than as a whiteboard design.

The page has no Hello Interview breakdown: no functional requirements, no deep dives, no
patterns, no red flags. One community solution is posted. That makes it the thinnest of the
four design-flavoured Reddit questions, and the prompt itself is the whole spec.

## Requirements pulled from the prompt

- Three states: CLOSED, OPEN, HALF_OPEN, with automatic transitions between them.
- Configurable failure threshold and timeout duration.
- Thread-safe under concurrent callers.
- Fallback responses when the breaker is open.
- Metrics exposed for monitoring.

Left open by the prompt, and worth deciding out loud: what trips the breaker (error rate
over a rolling window versus consecutive failures), how many trial calls HALF_OPEN admits
and what closes it again, and whether breaker state is per-dependency or per-endpoint.

## Coverage in existing prep

Closest existing material, both in this folder:

- `coding/LoadBalancerDemo.java` and `coding/LoadBalancer_prompt.txt`. Per
  `memory/project_reddit_loadbalancer_coding.md`, the framing that scored there was
  separating fault detection from admission from selection. Fault detection is the circuit
  breaker, so that decomposition transfers directly.
- `coding/RateLimiterSolutions.java`. Per `memory/project_reddit_rate_limiter.md`, four
  algorithms behind one interface, graded on clarity of the pick. Same shape: the rolling
  window that decides "is this dependency failing" is a rate-limiter window with failures
  counted instead of requests, and the HALF_OPEN trial budget is admission control.

Now that this is known to be low-level, those two are stronger matches than any of the 13
validated system designs.

## Status

Prompt, metadata, and requirements captured. Implementation not written.
