# Crib — Live Stock Price Ingestion (Robinhood)

2 reports. Last asked late May 2026. **Levels reported: Staff, Manager** — the only trading question
in the set reported at Staff.

> Design a live stock price ingestion system that allows users to view **real-time stock prices** and
> **historical price graphs** for any given stock symbol.

---

## THE FIVE SENTENCES

1. *"Two problems in one prompt — a fan-out problem and a time-series problem. Let me design them
   separately."*
2. ⭐ *"**Capture the event stream durably first, then derive conflated state for the UI.** Market
   data is both — an event log you need to reconstruct anything, and a state projection that's all a
   phone needs. If I conflate at ingest I've destroyed history; if I fan out raw ticks I've melted
   the client."*
3. *"Conflation is a **display-tier** decision, and it's deliberate, consumer-specific data loss.
   It's correct for a quote screen and wrong for anything that needs a tick."*
4. *"A quote is not a trade — I can't build an OHLCV bar from a last-price cache."*
5. *"Charts read pre-materialised candles. Nothing draws a chart by scanning ticks."*

---

## 1. Summary (in plain terms)

Two systems that share only a data source:

1. **Live** — a price changes, millions of phones showing that symbol must see it now. Write-once,
   read-by-millions. Fan-out.
2. **Charts** — "AAPL for the last year." A bounded range query over pre-aggregated history.

## 2. The pipeline (⭐ order matters, and this is where the crib used to be wrong)

```
venue/SIP feeds
   │  (per venue, per channel, per session — with sequence numbers)
   ▼
feed capture ──────► DURABLE APPEND-ONLY LOG (Kafka/Kinesis)   ◄── the source of truth
   │                 partitioned by symbol, retained for replay
   ├──► normaliser ──► bar accumulator ──► candles_1m/5m/1h/1d   (history)
   ├──► normaliser ──► quote projection  ──► Redis last-value    (live display)
   └──► archiver    ──► columnar cold store                      (replay, compliance)
```

⛔ **Never publish to the fan-out tier before durable capture.** If ticks go to Redis pub/sub first,
a crash or a Redis failover creates a **permanent** hole in history — pub/sub has no replay and
object storage is not a low-latency ingest log. Capture, then project. Everything downstream is
rebuildable from the log; the log is rebuildable from nothing.

Track `(source, venue, channel, session, sequence)` and consumer offsets on the log. That is what
makes "the aggregator catches up without data loss" a true statement rather than a hopeful one.

⚠️ **Market-data entitlements are a real constraint, not a footnote.** Real-time quotes are licensed;
who may see them, at what latency, and how long you may retain the feed are contractual. Retail
users often get delayed or SIP-consolidated data unless entitled. Mentioning entitlements is a
strong domain signal at a brokerage — and it may legally cap your retention.

## 3. The live path

- ⭐ **Conflation lives at the display tier, per connection.** Keep one slot per `(connection,
  symbol)` and overwrite it; flush on a timer (~10/sec). AAPL may tick thousands of times a second;
  a phone renders 10 frames and a human reads fewer. **This is intentional, consumer-specific data
  loss** — say it that way. It is correct for a quote screen.

  ⛔ Do **not** generalise it. Consumers that cannot tolerate conflation: order-book reconstruction,
  the trade tape, tick charts, VWAP/volume/volatility, **price-triggered alerts** (a conflated feed
  can hide the threshold crossing entirely), execution and risk, NBBO consolidation, surveillance and
  regulatory replay. Those read the log, not the conflated projection.

- **Conflation is also the backpressure.** One slot per symbol cannot grow, so a slow client cannot
  OOM the gateway. A queue would. That is the operational reason for the choice.

- ⭐ **Fan-out multiplies at the gateway.** feed → per-symbol topic → **gateway nodes** → clients. A
  gateway holding 50k connections subscribes to AAPL **once**, then does an in-process
  `symbol → Set<connection>` lookup. Upstream sees one subscriber per gateway per symbol, not one per
  user. Symbol popularity is extremely skewed, so name the hot-symbol case: either accept it
  (~10k symbols is cheap) or hash hot symbols to gateway groups.

- **Gateway failover — subscription state must be recoverable.** In-memory maps die with the node.
  So: persist the subscription set per session (Redis, keyed by session id) or have the client
  re-declare on reconnect; put clients behind a load balancer that can land them anywhere; and on
  reconnect send **snapshot then stream, with the snapshot's sequence cursor**, so the client can
  discard stream messages older than its snapshot. Without the cursor there is a genuine race where
  a client applies a stale tick over a fresher snapshot.

- **Sequencing — scoped, not global.** ⛔ There is no such thing as "the per-symbol sequence number."
  Sequences are scoped by `(venue, channel, session, message type)`, they reset on reconnect, and
  they are **not comparable across venues** — sequence 100 from venue A says nothing about sequence
  100 from venue B. Keep independent per-venue state and consolidate for NBBO. Trades and quotes
  carry separate sequences, so one `seq` field for both is wrong: a newer ask would wrongly discard
  an older bid.
  Detect **gaps** and recover by replay or a fresh venue snapshot. ⛔ And never drop a late event
  *before* aggregation — a lower sequence may be genuinely late and still belong in a closed bar. Drop
  it at the *display* projection if you like; keep it in the log.

## 4. The history path

- **Charts read pre-materialised OHLCV candles.** Aggregator writes 1m / 5m / 1h / 1d. Zoom picks the
  table: "1 day" → 1m candles (~390 points in a session); "1 year" → daily (~252 points). Both are
  bounded indexed range scans. A year of raw ticks would be billions of rows for one chart.

- ⭐ **The in-flight bar comes from a bar accumulator, not the quote cache.** A quote (`last, bid,
  ask`) cannot yield open, high, low, or volume — and a **quote is not a trade**. Maintain explicit
  separate state:

  ```
  last_trade:{symbol}     price, size, seq, sale_conditions, ts
  nbbo:{symbol}           bid, bid_size, ask, ask_size, per-venue state
  bar_acc:{symbol}        bucket_start, open, high, low, close, volume, trade_count
  session:{symbol}        PRE | REGULAR | POST | HALTED | CLOSED
  ```

  Also decide and state **what the chart plots** — trade prices, midpoint, or mark. They differ, and
  only a trade-based bar can carry volume. Filter by sale condition; not every print is
  chart-eligible.

- **Event time plus watermarks decide when a bar is final**, not arrival time. A bar closes when the
  watermark passes its boundary; late events inside a closed bucket trigger a **recompute**, which is
  safe because a candle is keyed `(symbol, bucket_start)` — reprocessing overwrites with the same
  value. Trade **corrections and busts** are real and do exactly this.

- **Tiering by cost:** 1m retained ~30d, 5m ~1y, 1h/1d forever; raw ticks in columnar cold storage
  for replay and compliance.

- ⚠️ **Corporate actions: store raw prices plus an adjustment factor, apply at read.** A 4:1 split
  turns $600 into $150; an unadjusted chart shows a 75% crash that never happened. In-place rewriting
  destroys the audit trail and is not reversible when an action is **corrected** — which happens.
  Splits and dividends both need factors (price and volume adjust differently); symbol changes and
  delistings need identity mapping, so key the series on a stable internal `instrument_id`, never the
  ticker.

- **Market hours make real gaps.** Don't interpolate across the close. Sessions are first-class.

```
candles_1m/_5m/_1h/_1d   (instrument_id, bucket_start) PK, o,h,l,c,volume, trade_count, final bool
corporate_actions        (instrument_id, effective_date) PK, type, ratio, cumulative_factor
instruments              instrument_id PK, current_ticker, ticker_history

chart read: pick table by range → range scan → multiply by cumulative_factor
            → append in-flight bar from bar_acc:{symbol}
```

## 5. Likely probes

**"How do you not lose ticks?"** → I don't lose them — I capture the feed to a durable log first.
What I *drop* is at the display projection, on purpose, because the newest price is the only correct
thing to render. Two tiers, two guarantees, and the log means every drop is recoverable.

**"A million users on one symbol."** → One upstream subscription per gateway; inside a gateway one
map lookup and N bounded socket writes. Scale gateways horizontally.

**"The aggregator falls behind."** → It replays from its offset. The chart is briefly stale, the live
quote is not. Recompute is idempotent because bars are keyed `(instrument_id, bucket_start)`. Sustained
lag is an alarm, not a correctness bug.

**"A trade is corrected an hour later."** → Recompute the affected bars from the log and mark them
`final=false` until the correction window closes. This is exactly why conflating at ingest would be
unrecoverable.

## Traps

- ⛔ "Market data is state, not events" — only true for the display tier. Never say it unqualified.
- ⛔ Publishing to pub/sub before durable capture.
- ⛔ Building an OHLCV bar from a last-price cache.
- ⛔ "The per-symbol sequence number" — sequences are per venue/channel/session/type.
- ⛔ Dropping late events before aggregation.
- ⛔ Rewriting historical prices for a split; keying a price series on the ticker.
- ⛔ Snapshot-then-stream without a sequence cursor.
- ⛔ Forgetting entitlements.
- ⛔ Interpolating across market close.

## Reuse

`~/reddit-interview-prep/hellointerview/design_hot_posts_page.md` (pre-materialised reads),
`project_slack_chat_design` (fan-out to connected clients; pub/sub loses events, the durable log is
the truth), `project_chess_design` (per-key sequencing).
