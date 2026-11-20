> ## ⛔ SUPERSEDED DRAFT — DO NOT STUDY THIS AS THE ANSWER
>
> This is the **Round-0 proposal**, preserved only as the input Codex attacked. It contains errors that
> were found and fixed. The authoritative version is `../reddit-design-crib.md` (Q2).
>
> **Known wrong in this file:**
> - Presents fan-out-on-read as the answer for broadcasts across **all** channels. It serves the in-app pull
>   only — SNS/APNs/FCM/SES never query a user's subscriptions at send time, so push and email need the
>   audience enumerated and **snapshotted at event time**.
> - Resolves eligibility from *current* subscriptions, so it notifies people who joined after the event and
>   misses people who left before it.
> - Leaves the inbox-write → SQS-enqueue **dual write** unaddressed; a retry sees the inbox row and never
>   enqueues.
> - Bundle flush has a lost-update race: atomic `ADD` protects the counter, never the lifecycle.
> - Claims Kafka partitioning delivers user-visible ordering. SQS, retries and the push providers all reorder.
> - Schema stores only `token_hash`, which **cannot send a push**.
> - Timezone model ("24+ hourly waves plus half-hour zones") is invalid — 15/45-minute offsets and DST exist.

# Proposal: Reddit Notification System

Interview context: Reddit SWE L6 (Staff) system-design round, 60 min. Real prompt as reported:
"Design Reddit's notification system: deliver in-app, push, and email notifications for events like
replies, mentions, mod actions, and trending posts. Supports per-user preferences, bundling/digesting,
and throttling."

Constraint: AWS-native.

## Position

Reddit's notification problem has **two structurally different event classes**, and the whole design
turns on separating them. Treating them uniformly is the failure mode.

- **Targeted** (reply, mention, mod action, DM): one event → 1..N specific recipients, N small.
- **Broadcast** (trending post, subreddit announcement, live-event start): one event → millions of
  potential recipients.

### 1. The core split: fan-out on write vs fan-out on read

- **Targeted → fan-out on write.** Materialize a row into each recipient's inbox. N is small, reads are
  cheap and simple, and per-user ordering falls out naturally.
- **Broadcast → fan-out on read.** Store the broadcast **once**. A user's notification feed is a merge
  of (a) their personal inbox and (b) the broadcast streams for subreddits they subscribe to, filtered
  by `last_read_at`. Writing 10M inbox rows for one trending post is the wrong answer: it is 10M writes
  for content most users never open, and it makes the trending-post latency a function of subscriber
  count.

This is the same insight as computing unread counts from a read pointer rather than per-member counters.

### 2. Pipeline

```
producers (comment svc, mod svc, ranking svc)
   -> MSK (Kafka), partition key = recipient_user_id for targeted
                   partition key = subreddit_id for broadcast
   -> Notification Router (ECS)  [dedupe, preference coarse-filter, bundling decision]
   -> DynamoDB inbox write  +  channel dispatch queues (SQS per channel)
   -> Dispatchers: in-app (no-op, inbox is the source), push (SNS -> APNs/FCM), email (SES)
```

Producers write with a **transactional outbox** in their own store, so a notification is never lost
because Kafka was briefly unavailable, and never sent for an action that was rolled back.

### 3. Idempotency

The event bus is at-least-once, so `notification_id = hash(event_id, recipient_id, channel)` —
deterministic, so a redelivery is a conditional-write no-op rather than a duplicate push. This must be
the *only* dedupe mechanism; time-window dedupe caches are not sufficient because retries can arrive
hours later after a DLQ replay.

### 4. Preferences — checked twice, deliberately

Preferences are per (user, notification_type, channel), plus global quiet hours in the user's timezone.

- **Coarse filter at enqueue** so obviously-unwanted work is never queued.
- **Authoritative re-check at dispatch**, because a user can disable a channel between enqueue and send,
  and a digest may be assembled hours before it is delivered.

Checking only at enqueue sends notifications users just turned off. Checking only at dispatch wastes the
whole pipeline on work that was never wanted. Both, and say why.

### 5. Bundling / digesting

Bundle key = `(user_id, notification_type, target_id)` — e.g. all replies to *post 123* collapse into
"3 people replied to your post".

- An open bundle is a DynamoDB item with `count`, `first_seen`, `sample_actors` (cap 3), and a flush
  deadline.
- Increment with an **atomic `ADD` in a conditional update**, not read-modify-write — concurrent replies
  to a hot post are the normal case and RMW loses counts.
- Flush is driven by a **time-bucketed scheduler table** (`PK = flush_minute`, `SK = bundle_id`), swept
  once per minute. Not a per-bundle timer, and not a full-table scan.
- Deliver-on-first, bundle-the-rest: the first reply notifies immediately, subsequent ones inside the
  window update the bundle. Otherwise every notification is delayed by the bundling window, which is a
  worse product.

### 6. Throttling

Per-user token bucket in ElastiCache Redis, spanning all types and channels, with separate buckets per
channel (push is more intrusive than in-app). On exhaustion, **degrade rather than drop**: keep writing
to the in-app inbox, suppress only push/email. Dropping the inbox row loses information the user asked
for; suppressing the interrupt is what throttling should mean.

### 7. Scheduled digests and timezones

"Daily digest at 08:00 local" is not one job — it is **24+ hourly waves** (plus half-hour zones). Shard
the sweep by `(timezone_bucket, user_shard)` so each wave is bounded work, and stagger within a wave to
avoid an SES/APNs spike at :00.

### 8. Device tokens and deliverability — the part most designs skip

- APNs/FCM invalid-token feedback **must** prune the device table. Otherwise cost grows forever and
  delivery metrics silently lie.
- SES bounce and complaint feedback via SNS into a suppression list, honored before every send. Ignoring
  this destroys sending reputation, and once reputation is gone all email stops working, including
  password resets.
- Per-channel DLQs with a replay tool, and DLQ replay must be safe *because* of the deterministic
  notification id.

### 9. Ordering

Global ordering is unnecessary; **per-user ordering matters** ("your post was removed" must not arrive
before "your post was approved"). Kafka partition key = recipient_user_id gives per-user ordering for
free on the targeted path. Broadcast has no ordering requirement against the targeted path.

### 10. Data model

```
INBOX
  PK = USER#<user_id>   SK = <ts>#<notification_id>
  attrs: type, actor_id, target_id, bundle_count, read_at, TTL (90d)

BROADCASTS
  PK = SUB#<subreddit_id>  SK = <ts>#<broadcast_id>
  attrs: type, target_id, TTL (30d)          # written ONCE, read by all subscribers

OPEN_BUNDLES
  PK = USER#<user_id>   SK = BUNDLE#<type>#<target_id>
  attrs: count (atomic ADD), first_seen, sample_actors, flush_at

FLUSH_SCHEDULE
  PK = <flush_minute>   SK = USER#<user_id>#BUNDLE#<...>

PREFERENCES
  PK = USER#<user_id>   SK = PREF#<type>#<channel>      (cached in Redis, short TTL)

DEVICES
  PK = USER#<user_id>   SK = DEVICE#<token_hash>
  attrs: platform, last_valid_at, invalidated_at
```

Read pointer `last_read_at` on the user record drives the badge count as
`count(inbox where ts > last_read_at) + count(broadcasts where ts > last_read_at)`, with the second term
capped and cached — not a stored per-user counter incremented on every event.

### 11. Observability

Funnel metrics per stage: generated → filtered by preference → throttled → dispatched → delivered →
opened. The gap between dispatched and delivered is where push problems hide, and it is only visible if
APNs/FCM receipts are joined back to the dispatch record.

## Explicit trade-offs I am accepting

1. Broadcast notifications cost more per read (merge at read time) in exchange for O(1) write.
2. The badge count for broadcasts is approximate/capped rather than exact.
3. Bundling delays the 2nd..Nth notification by up to the window; the 1st is immediate.
4. Digest delivery is minute-granular, not second-granular.
5. Inbox rows expire at 90 days; older history is not retained.
