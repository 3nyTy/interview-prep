# Design a Notification System

- Source: https://www.hellointerview.com/community/questions/notification-system-scale/cm758vf17024kalw2qn7e57xs?company=Reddit
- Type: system design
- Reddit reports: 3 — Late January 2026 (Manager), Early February 2025 (Manager), Mid January 2024 (Senior)
- Also reported at: Coupang, Metropolis, Microsoft, Autodesk, +14 more
- Captured: 2026-08-13 (detail page read)

Level caveat: two of the three Reddit reports are Manager-track, and the only IC report is
Senior from January 2024. This is the most-reported Reddit design question, but it is the
least Staff-IC-weighted of the four.

## Prompt as published

> Design a scalable notification system that supports both individual and bulk notifications
> to multiple users. The system should handle different types of notifications including
> critical time-sensitive messages (like 1:1 chat) and promotional content (like
> system-generated recommendations) with appropriate delivery mechanisms. Consider
> scalability requirements (up to 1M notifications/second), expiration logic for different
> notification types, and the typical 80/20 distribution between critical and promotional
> notifications where promotional messages can target thousands of users simultaneously.

## Common functional requirements

- Clients can send a critical notification to a user over push, email, or SMS, with a
  durable record of what was sent and its delivery status.
- Clients can schedule a promotional notification for a target segment, and the system fans
  it out to that audience at the scheduled time.
- Users can set notification preferences (per-channel opt-outs, quiet hours) that are
  checked before every send.

## Common deep dives

1. How do you deliver notifications durably when a marketing surge hits 3,300/sec or a
   downstream provider goes dark for an hour?
2. How do you guarantee OTPs and account alerts arrive within 5 seconds when a promotional
   batch of 100K+ messages is in flight?
3. You promise at-least-once delivery. How do you keep a user from getting the same OTP
   three times when workers crash and queues redeliver?
4. If one SMS or email provider gets slow or starts erroring, how do you stop it from
   stalling delivery for every other channel?

## Patterns and technologies named on the page

Patterns: managing long-running tasks (accept, persist, enqueue, worker pool with retries,
backoff, DLQ); multi-step processes (segment expansion as a workflow, and the transactional
outbox for atomic write-then-enqueue); dealing with contention (atomic dedup claim, and
priority isolation between traffic classes).

Technologies: Kafka for the durable handoff with separate critical and promotional topics;
Redis for the dedup claim (SET NX on notification ID) with type-scoped TTLs — roughly 10
minutes for OTP, 48 hours for promo — plus per-provider token buckets; PostgreSQL as system
of record and outbox host.

Similar problems they point at: Job Scheduler, Web Crawler, Payment System.

## Red flags to avoid

- Provider calls in the request path — no surge buffer, no durable retry surface.
- One queue and one worker pool for all traffic — a 100K promo blocks OTPs and blows the
  5-second SLA. Priority isolation is the unstated requirement.
- One dedup TTL for every message type — OTP windows are minutes, promo dedup is about a day.
- Naive retry against a failing provider — needs circuit breakers, exponential backoff,
  per-provider concurrency limits, and failover, with failed work staying on the queue.

## Coverage in existing prep

Strongest transfer in the whole kit, and now confirmed rather than inferred:
`memory/project_webhook_delivery_design.md` is SQS + DynamoDB as a retry scheduler, the
transactional outbox, and a 3-ID model. The page independently names the transactional
outbox, at-least-once with a dedup claim, and per-provider isolation as the graded content.
Deep dives 1, 3, and 4 are answerable from that design almost unchanged.

Deep dive 2 (priority isolation) is the one it does not cover — separate lanes with reserved
worker capacity per traffic class.

Also not covered by the webhook design, and asked by the requirements above: fan-out on
write versus on read for segment expansion, per-channel dispatch behind one abstraction,
preferences and quiet hours, and expiration logic per notification type.

Note the fourth red flag names circuit breakers, which is `design_circuit_breaker.md` in
this folder. The two Reddit questions connect.

## Status

Prompt, requirements, deep dives, and red flags captured. Design not written.
