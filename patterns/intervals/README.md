# Intervals & scheduling — 87 questions, 8 templates

**Recognize it:** the input has *(start, end)* pairs, timestamps, bookings, shifts, byte ranges or "at the same time". The question is about overlap, coverage, gaps, capacity, or what runs next.

**First 30 seconds in the interview — ask these out loud:**
1. Is `end` inclusive or exclusive? Does `[1,2]` touch `[2,3]`?
2. Is the input sorted? Can intervals repeat or be empty?
3. Return the merged list, a count, an assignment, or a timeline?

## The picture

```
sort by START, then sweep left → right

  [1────3]
     [2──────6]                A. MERGE: next.start <= last.end → extend last
               [8──10]            → [1,6] [8,10] [15,18]
                       [15─18]

time:   0    5   10   15   20   25   30
        [──────────────────────────────)  0-30
             [────)                       5-10
                       [────)             15-20
count:  1    2    1    2    1   ...         B. SWEEP: +1 at start, -1 at end
                                             max running count = rooms needed (2)

A: [0──2]   [5─────────10]               C. TWO LISTS: overlap = [max(starts), min(ends)]
B:    [1────────5]  [8──────12]             advance whichever list ends first
→      [1─2]    [5] [8──10]

sort by END for selection                 D. GREEDY: keep the one that frees up soonest
  [1─2][2─3][3─4]       keep 3              E. WEIGHTED: dp[i] = max(skip, profit + dp[last job ending ≤ start])
  [1────3]              drop 1

boundaries list  [10, 14, 16, 20]          F. RANGE MODULE: even index = start, odd = end
                   └─in─┘    └─in─┘           bisect to add / remove / query

heap of ready tasks ─► pop smallest ─► clock += duration     G. SIMULATION
rate history  t=0 $1/min │ t=60 $2/min    → split each delivery at rate changes   H. LEDGER
```

## Which template — decision order

| If the question asks… | Template |
|---|---|
| merged list / total covered time / highlight spans | **A** merge |
| max simultaneous / rooms / cores / assign each to a room / timeline of counts | **B** sweep or heap of end times |
| combine two already-sorted lists | **C** two pointers |
| max number kept / min removed / pack into batches | **D** sort by end, greedy |
| intervals carry **weights** (profit, credit) | **E** DP + `bisect` |
| live add / remove / "is this covered?" queries | **F** sorted boundaries + `bisect` |
| tasks arrive over time, pick next by priority | **G** heap + clock |
| multi-level OA: entries, rate/promotion changes "effective from", payouts | **H** sorted change log + `bisect` |

## Practice order (≈ 6 sessions)

1. **A + B core** (warm-ups, all from memory, ≤ 10 min each): Merge Intervals, Insert Interval, Meeting Rooms I and II, Minimum CPU Cores (inclusive ends).
2. **B assignment + C:** Expanding Tennis Club (Atlassian), Job Scheduler Minimum Workers (Lyft), Interval List Intersections, Union of Sorted Lists (Google, then K lists with a heap).
3. **D + E:** Non-Overlapping Intervals, then Max Profit Job Scheduling, then Max Credits with K Classes.
4. **F:** Range Module (Walmart), Deployment Window Scheduler (Stripe), Credit Tracker (Perplexity).
5. **G:** Single-Threaded CPU, Task Scheduler with Lazy Deletion, Data Labeling Task Scheduler (OpenAI, *High*).
6. **H, timed 60 min with AI allowed:** Delivery Billing System (Rippling, *High*), Code Craft: Dasher Pay (DoorDash, *Very high*), Working Hours Register (Airbnb).

Rule for each problem: say the template letter out loud before writing code, then state complexity and two edge cases (touching endpoints, empty input).

## All 87 questions

Each title opens the full prompt from the bank. Within each template, most frequently asked first.

### A. Merge / union / insert — 19 questions
Solution: `merge · insert · covered_length · merge_with_indices · highlight` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [Text Highlighting / Citation Tagging](../../1p3a-qbanks/harvey/raw/text-highlighting-citation.txt) | Harvey | Very high | medium |
| [Merge Intervals](../../1p3a-qbanks/bytedance/raw/merge-intervals.txt) | ByteDance / TikTok | Medium | medium |
| [Party Time Blocks](../../1p3a-qbanks/scale.ai/raw/party-time-blocks.txt) | Scale AI | Medium | medium |
| [Frontend React Screen](../../1p3a-qbanks/apple/raw/frontend-react-screen.txt) *(loose fit)* | Apple | Low | medium |
| [Merge Intervals (LC 56) + Interview Variant](../../1p3a-qbanks/microsoft/raw/merge-intervals.txt) | Microsoft | Low | medium |
| [Process Execution Time (Merge Inclusive Intervals)](../../1p3a-qbanks/ibm/raw/process-execution-time-merge-intervals.txt) | IBM | Low | medium |
| [Streaming Interval Coverage on a Number Axis](../../1p3a-qbanks/waymo/raw/interval-pollution-coverage.txt) | Waymo | Low | medium |
| [Bold Words in String](../../1p3a-qbanks/apple/raw/bold-words-in-string.txt) | Apple | Single report | medium |
| [Compress Consecutive Ranges](../../1p3a-qbanks/cisco/raw/compress-consecutive-ranges.txt) | Cisco | Single report | easy |
| [File Chunk Range Tracking](../../1p3a-qbanks/roblox/raw/file-chunks-consecutive-byte-ranges.txt) | Roblox | Single report | – |
| [Insert Interval (LC 57)](../../1p3a-qbanks/apple/raw/insert-interval.txt) | Apple | Single report | medium |
| [Interval Consolidation OA](../../1p3a-qbanks/jpmorgan/raw/interval-consolidation-oa.txt) | JPMorgan Chase | Single report | medium |
| [Merge Intervals](../../1p3a-qbanks/apple/raw/merge-intervals.txt) | Apple | Single report | medium |
| [Merge Intervals](../../1p3a-qbanks/netflix/raw/merge-intervals.txt) | Netflix | Single report | medium |
| [Merge Intervals](../../1p3a-qbanks/snowflake/raw/merge-intervals.txt) | Snowflake | Single report | medium |
| [Merge Intervals (LC 56)](../../1p3a-qbanks/bloomberg/raw/merge-intervals.txt) | Bloomberg | Single report | medium |
| [Merge Intervals (LeetCode 56)](../../1p3a-qbanks/roblox/raw/merge-intervals.txt) | Roblox | Single report | medium |
| [Merge Intervals Returning Original Start/End Indices](../../1p3a-qbanks/walmartlabs/raw/merge-intervals-with-indices.txt) | Walmart Labs | Single report | medium |
| [Union of Sorted Interval Lists](../../1p3a-qbanks/google/raw/sorted-interval-list-union.txt) | Google | Single report | medium |

### B. Overlap count (sweep line) & room assignment — 15 questions
Solution: `max_overlap · can_attend_all · assign_rooms · active_segments · MyCalendar` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [Meeting Rooms / Interval Scheduling Variants](../../1p3a-qbanks/google/raw/meeting-rooms-variants.txt) | Google | High | medium |
| [Expanding Tennis Club Court Assignment](../../1p3a-qbanks/atlassian/raw/expanding-tennis-club-court-assignment.txt) | Atlassian | Medium | medium |
| [Phone Screen / Onsite: Meeting Room Scheduler](../../1p3a-qbanks/uber/raw/phone-screen-meeting-scheduler-rooms.txt) | Uber | Medium | medium |
| [Job Scheduler: Minimum Workers with Assignment History](../../1p3a-qbanks/lyft/raw/job-scheduler-minimum-workers.txt) | Lyft | Low | medium |
| [Maximum Concurrent Processes / Meeting Rooms](../../1p3a-qbanks/ibm/raw/maximum-concurrent-processes.txt) | IBM | Low | medium |
| [Minimum CPU Cores](../../1p3a-qbanks/jpmorgan/raw/minimum-cpu-cores.txt) | JPMorgan Chase | Low | easy |
| [Active Couriers Timeline](../../1p3a-qbanks/uber/raw/active-couriers-timeline.txt) | Uber | Single report | medium |
| [Largest Team Whose Members Share a Common Office Window](../../1p3a-qbanks/citadel/raw/interval-overlap-max-team.txt) | Citadel / Citadel Securities | Single report | medium |
| [Maximum Concurrent Processes from Intervals](../../1p3a-qbanks/amazon/raw/max-concurrent-processes-sweep-line.txt) | Amazon | Single report | medium |
| [Meeting Rooms (Conflict Check)](../../1p3a-qbanks/netflix/raw/meeting-rooms-conflict-check.txt) | Netflix | Single report | easy |
| [Meeting Rooms II](../../1p3a-qbanks/bloomberg/raw/meeting-rooms-ii.txt) | Bloomberg | Single report | medium |
| [Meeting Rooms II](../../1p3a-qbanks/snowflake/raw/meeting-rooms-ii.txt) | Snowflake | Single report | medium |
| [Meeting Rooms II (LeetCode 253)](../../1p3a-qbanks/roblox/raw/meeting-rooms-ii.txt) | Roblox | Single report | medium |
| [My Calendar I](../../1p3a-qbanks/uber/raw/my-calendar-i.txt) | Uber | Single report | medium |
| [Twitter Spaces — Aggregate Active Hours](../../1p3a-qbanks/xai/raw/twitter-spaces-active-hours.txt) | xAI | Single report | medium |

### C. Two sorted lists (two pointers) — 2 questions
Solution: `intersect_lists · union_sorted_lists` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [Intersection of Two Interval Lists](../../1p3a-qbanks/bytedance/raw/intersection-of-two-interval-lists.txt) | ByteDance / TikTok | Single report | medium |
| [Interval List Intersections](../../1p3a-qbanks/uber/raw/interval-list-intersections.txt) | Uber | Single report | medium |

### D. Greedy by earliest end — 3 questions
Solution: `min_removals_to_disjoint · min_batches` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [Battery Usage with Recharge Cycle](../../1p3a-qbanks/capitalone/raw/battery-recharge-cycle.txt) *(loose fit)* | Capital One | Single report | medium |
| [Code Craft: Batch with Capacity & Time Window (MLE)](../../1p3a-qbanks/doordash/raw/code-craft-batch-with-capacity-window-mle.txt) | DoorDash | Single report | medium |
| [Non-Overlapping Intervals (LeetCode 435)](../../1p3a-qbanks/bytedance/raw/non-overlapping-intervals.txt) | ByteDance / TikTok | Single report | – |

### E. Weighted scheduling (DP + binary search) — 3 questions
Solution: `max_profit_jobs · max_credit_k_classes` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [Max Credits with K Classes (Interval-Style)](../../1p3a-qbanks/snowflake/raw/course-credit-max-k-classes.txt) | Snowflake | Low | medium |
| [Maximum Profit in Job Scheduling](../../1p3a-qbanks/airbnb/raw/max-profit-job-scheduling.txt) | Airbnb | Low | medium |
| [Maximum Profit in Job Scheduling](../../1p3a-qbanks/snowflake/raw/maximum-profit-in-job-scheduling.txt) | Snowflake | Single report | hard |

### F. Range structures (add / remove / query) — 11 questions
Solution: `RangeModule · apply_range_flips · subtract` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [IP / CIDR Firewall](../../1p3a-qbanks/databricks/raw/ip-cidr-firewall.txt) | Databricks | High | medium |
| [Shard Rebalance / Overlapping Key Range](../../1p3a-qbanks/openai/raw/shard-rebalance.txt) | OpenAI | Medium | medium |
| [Cipher Cover Interval Editing](../../1p3a-qbanks/databricks/raw/cipher-cover-interval-editing.txt) | Databricks | Low | medium |
| [Deployment Window Scheduler (OA)](../../1p3a-qbanks/stripe/raw/deployment-window-scheduler-oa.txt) | Stripe | Low | medium |
| [Policy Violation Records Aggregation](../../1p3a-qbanks/pinterest/raw/policy-violation-records-aggregation.txt) | Pinterest | Low | medium |
| [Credit Tracker with Expiring Credits](../../1p3a-qbanks/perplexity/raw/credit-tracker-with-expiring-credits.txt) | Perplexity | Single report | medium |
| [House Segments After Deletions](../../1p3a-qbanks/capitalone/raw/house-segments-after-deletions.txt) | Capital One | Single report | medium |
| [Interval Sign Flips](../../1p3a-qbanks/jpmorgan/raw/interval-sign-flips.txt) | JPMorgan Chase | Single report | easy |
| [Range Module (LC 715)](../../1p3a-qbanks/walmartlabs/raw/range-module-lc-715.txt) | Walmart Labs | Single report | hard |
| [Shipping Cost Calculator](../../1p3a-qbanks/stripe/raw/shipping-cost-calculator.txt) *(loose fit)* | Stripe | Single report | medium |
| [Time Slot Generator from Weekly Working Hours](../../1p3a-qbanks/stripe/raw/time-slot-generator.txt) | Stripe | Single report | easy |

### G. Heap-driven scheduler simulation — 19 questions
Solution: `single_threaded_cpu · LazyPriorityScheduler` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [Data Labeling Task Scheduler](../../1p3a-qbanks/openai/raw/data-labeling-task-scheduler.txt) | OpenAI | High | medium |
| [Email Subscription Scheduler](../../1p3a-qbanks/stripe/raw/email-subscription-scheduler.txt) | Stripe | Medium | medium |
| [Account Scheduler with LRU](../../1p3a-qbanks/stripe/raw/account-scheduler-lru.txt) | Stripe | Low | medium |
| [Customer Scheduler: VIP Priority and 2:1 Fairness](../../1p3a-qbanks/intuit/raw/customer-scheduler-vip-fairness.txt) | Intuit | Low | easy |
| [Elevator Assignment / Nearest Available Elevator](../../1p3a-qbanks/pinterest/raw/elevator-assignment-nearest-available.txt) *(loose fit)* | Pinterest | Low | medium |
| [Jupyter / WebSocket Load Balancer (OA)](../../1p3a-qbanks/stripe/raw/jupyter-load-balancer-oa.txt) | Stripe | Low | medium |
| [OptiCargo — Cargo-to-Flight Scheduling](../../1p3a-qbanks/optiver/raw/opticargo-flight-scheduling.txt) | Optiver | Low | hard |
| [Task Scheduler with Priority + Lazy Deletion](../../1p3a-qbanks/citadel/raw/task-scheduler-priority-lazy-delete.txt) | Citadel / Citadel Securities | Low | medium |
| [Warehouse Round-Robin Allocation with Closures](../../1p3a-qbanks/capitalone/raw/warehouse-round-robin-allocation.txt) | Capital One | Low | medium |
| [Implement a Round-Robin Task Scheduler](../../1p3a-qbanks/citadel/raw/round-robin-task-scheduler.txt) | Citadel / Citadel Securities | Single report | medium |
| [OOD — Treatment Frequency Scheduler](../../1p3a-qbanks/oracle/raw/ood-treatment-scheduler.txt) | Oracle | Single report | medium |
| [Palindrome Warmup + Two-Task Worker Scheduling](../../1p3a-qbanks/twosigma/raw/palindrome-and-task-scheduling-round.txt) *(loose fit)* | Two Sigma | Single report | medium |
| [Phone Battery Discharge Scheduling](../../1p3a-qbanks/roblox/raw/phone-battery-discharge-scheduling.txt) | Roblox | Single report | medium |
| [Priority Task Executor](../../1p3a-qbanks/snowflake/raw/priority-task-executor.txt) | Snowflake | Single report | medium |
| [Seller Task Scheduler](../../1p3a-qbanks/bytedance/raw/seller-task-scheduler.txt) | ByteDance / TikTok | Single report | – |
| [Single-Threaded CPU (LeetCode 1834)](../../1p3a-qbanks/roblox/raw/single-threaded-cpu.txt) | Roblox | Single report | medium |
| [Timestamped Task Scheduler](../../1p3a-qbanks/bytedance/raw/timestamped-task-scheduler.txt) | ByteDance / TikTok | Single report | medium |
| [Waymo Passenger Pickup Scheduler (OO Design)](../../1p3a-qbanks/waymo/raw/passenger-pickup-scheduler-oo-design.txt) | Waymo | Single report | medium |
| [Worker Task Assignment](../../1p3a-qbanks/stripe/raw/worker-task-assignment.txt) | Stripe | Single report | medium |

### H. Effective-dated event ledger (multi-level OA) — 15 questions
Solution: `DeliveryBilling` in [solutions.py](solutions.py)

| Question | Company | Freq | Level |
|---|---|---|---|
| [Code Craft: Dasher Pay](../../1p3a-qbanks/doordash/raw/code-craft-dasher-pay.txt) | DoorDash | Very high | medium |
| [Delivery Billing System](../../1p3a-qbanks/rippling/raw/delivery-billing-system.txt) | Rippling | High | hard |
| [Incident Monitor — Transaction Alert Trigger / Resolve](../../1p3a-qbanks/stripe/raw/transaction-logs-trigger-resolve.txt) | Stripe | Medium | medium |
| [Progressive Banking System OA](../../1p3a-qbanks/airbnb/raw/oa-banking-system.txt) | Airbnb | Medium | hard |
| [Alert Monitor — Rolling Window + Severity Distribution + Spike Detection](../../1p3a-qbanks/linkedin/raw/coding-alert-monitor.txt) | LinkedIn | Low | medium |
| [Code Craft: Dasher Pay via Mock Service API](../../1p3a-qbanks/doordash/raw/code-craft-dasher-pay-via-api-mock.txt) | DoorDash | Low | medium |
| [Dinosaur Speed (Meta PE classic)](../../1p3a-qbanks/meta/raw/dinosaur-speed-meta-pe.txt) *(loose fit)* | Meta | Low | medium |
| [OA — Worker / Employee Grant Management](../../1p3a-qbanks/anthropic/raw/oa-worker-management.txt) | Anthropic | Low | medium |
| [Product Quality Values into Contiguous Blocks](../../1p3a-qbanks/amazon/raw/product-quality-contiguous-blocks.txt) *(loose fit)* | Amazon | Low | medium |
| [Working Hours Register](../../1p3a-qbanks/airbnb/raw/oa-working-hours-register.txt) | Airbnb | Low | hard |
| [OA: Task Management System](../../1p3a-qbanks/instacart/raw/oa-task-management-system.txt) | Instacart | Single report | hard |
| [OA: Worker Attendance & Payroll](../../1p3a-qbanks/instacart/raw/oa-worker-attendance-payroll.txt) | Instacart | Single report | hard |
| [OO Design: Continuous Time Intervals from Two Timestamp Signal Streams](../../1p3a-qbanks/waymo/raw/timestamp-signals-time-intervals-ood.txt) | Waymo | Single report | medium |
| [Task Management System OA](../../1p3a-qbanks/airbnb/raw/oa-task-management-system.txt) | Airbnb | Single report | hard |
| [Test Run Status Monitor (Failing → Passing Windows)](../../1p3a-qbanks/vanta/raw/test-status-monitor.txt) | Vanta | Single report | hard |