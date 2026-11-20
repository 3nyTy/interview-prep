"""Intervals & scheduling: eight templates that cover every question in this bucket.

Run:  python solutions.py      (all asserts must pass)

Conventions: intervals are [start, end]. Each function says whether `end` is
inclusive or exclusive -- ASK in the interview, it is the #1 edge case.
"""
from bisect import bisect_left, bisect_right, insort
from collections import defaultdict
import heapq


# ─────────────────────────────────────────────────────────────────────────────
# A. MERGE / UNION / INSERT  — sort by start, extend the last block
# ─────────────────────────────────────────────────────────────────────────────
def merge(intervals, touching_merges=True):
    """LC 56. O(n log n) time, O(n) out. touching_merges: does [1,2]+[2,3] merge?"""
    out = []
    for s, e in sorted(intervals):
        if out and (s <= out[-1][1] if touching_merges else s < out[-1][1]):
            out[-1][1] = max(out[-1][1], e)
        else:
            out.append([s, e])
    return out


def insert(intervals, new):
    """LC 57. Input already sorted & disjoint -> O(n), no sort needed."""
    out, i, n = [], 0, len(intervals)
    while i < n and intervals[i][1] < new[0]:          # entirely left
        out.append(intervals[i]); i += 1
    s, e = new
    while i < n and intervals[i][0] <= e:              # overlapping -> absorb
        s, e = min(s, intervals[i][0]), max(e, intervals[i][1]); i += 1
    out.append([s, e])
    return out + intervals[i:]


def covered_length(intervals, inclusive=False):
    """Total time at least one interval is active (IBM process execution time)."""
    return sum(e - s + (1 if inclusive else 0) for s, e in merge(intervals))


def merge_with_indices(intervals):
    """Walmart variant: also report which original indices formed each block."""
    order = sorted(range(len(intervals)), key=lambda i: intervals[i])
    out = []
    for i in order:
        s, e = intervals[i]
        if out and s <= out[-1][1]:
            out[-1][1] = max(out[-1][1], e); out[-1][2].append(i)
        else:
            out.append([s, e, [i]])
    return out


def highlight(text, spans, open_tag="<b>", close_tag="</b>"):
    """Bold Words / Harvey citation tagging: merge char spans [s,e) then wrap."""
    res, prev = [], 0
    for s, e in merge(spans):          # touching spans merge -> one tag
        res += [text[prev:s], open_tag, text[s:e], close_tag]
        prev = e
    return "".join(res + [text[prev:]])


# ─────────────────────────────────────────────────────────────────────────────
# B. OVERLAP COUNT / SWEEP LINE  — +1 at start, -1 at end, scan in time order
# ─────────────────────────────────────────────────────────────────────────────
def max_overlap(intervals, inclusive_end=False):
    """Meeting Rooms II / min CPU cores / max concurrent. O(n log n), O(n).
    Tie rule: for half-open [s,e) process -1 before +1 at the same time;
    for inclusive [s,e] process +1 first (a job ending at t overlaps one starting at t)."""
    events = []
    for s, e in intervals:
        events.append((s, 1))
        events.append((e + 1 if inclusive_end else e, -1))
    best = cur = 0
    for _, d in sorted(events):        # (-1) sorts before (+1) at equal time
        cur += d
        best = max(best, cur)
    return best


def can_attend_all(intervals):
    """Meeting Rooms I (Netflix): end == next start is fine."""
    iv = sorted(intervals)
    return all(iv[i][1] <= iv[i + 1][0] for i in range(len(iv) - 1))


def assign_rooms(intervals):
    """Court / worker / room ASSIGNMENT (Atlassian, Lyft): heap of (free_at, room).
    Returns (rooms_needed, room_for_each_interval). Half-open."""
    order = sorted(range(len(intervals)), key=lambda i: intervals[i][0])
    busy, free, rooms = [], [], [None] * len(intervals)   # busy: (end, room)
    count = 0
    for i in order:
        s, e = intervals[i]
        while busy and busy[0][0] <= s:
            heapq.heappush(free, heapq.heappop(busy)[1])
        room = heapq.heappop(free) if free else count
        if room == count:
            count += 1
        rooms[i] = room
        heapq.heappush(busy, (e, room))
    return count, rooms


def active_segments(intervals):
    """Uber Active Couriers Timeline: [segStart, segEnd, activeCount] where count changes."""
    delta = defaultdict(int)
    for s, e in intervals:
        delta[s] += 1; delta[e] -= 1
    out, cur, prev = [], 0, None
    for t in sorted(delta):
        if prev is not None and cur > 0:
            if out and out[-1][2] == cur and out[-1][1] == prev:
                out[-1][1] = t
            else:
                out.append([prev, t, cur])
        cur += delta[t]; prev = t
    return out


class MyCalendar:
    """LC 729 / Uber booking: sorted starts + bisect. book O(n) worst (list insert)."""
    def __init__(self):
        self.starts, self.ends = [], []

    def book(self, s, e):
        i = bisect_right(self.starts, s)
        if i > 0 and self.ends[i - 1] > s:      # previous one runs into us
            return False
        if i < len(self.starts) and self.starts[i] < e:   # we run into next
            return False
        self.starts.insert(i, s); self.ends.insert(i, e)
        return True


# ─────────────────────────────────────────────────────────────────────────────
# C. TWO SORTED LISTS  — two pointers, advance whoever ends first
# ─────────────────────────────────────────────────────────────────────────────
def intersect_lists(a, b):
    """LC 986 (ByteDance, Uber). O(m+n)."""
    i = j = 0
    out = []
    while i < len(a) and j < len(b):
        lo, hi = max(a[i][0], b[j][0]), min(a[i][1], b[j][1])
        if lo <= hi:
            out.append([lo, hi])
        if a[i][1] < b[j][1]:
            i += 1
        else:
            j += 1
    return out


def union_sorted_lists(a, b):
    """Google: union of two sorted disjoint lists in O(m+n) (merge step, then extend)."""
    out, i, j = [], 0, 0
    while i < len(a) or j < len(b):
        if j == len(b) or (i < len(a) and a[i][0] <= b[j][0]):
            s, e = a[i]; i += 1
        else:
            s, e = b[j]; j += 1
        if out and s <= out[-1][1]:
            out[-1][1] = max(out[-1][1], e)
        else:
            out.append([s, e])
    return out


# ─────────────────────────────────────────────────────────────────────────────
# D. GREEDY BY EARLIEST END  — keep the interval that frees up soonest
# ─────────────────────────────────────────────────────────────────────────────
def min_removals_to_disjoint(intervals):
    """LC 435. Keep max non-overlapping by earliest end; answer = n - kept."""
    kept, last_end = 0, float("-inf")
    for s, e in sorted(intervals, key=lambda x: x[1]):
        if s >= last_end:
            kept += 1; last_end = e
    return len(intervals) - kept


def min_batches(times, capacity, window):
    """DoorDash batching: sorted points, each batch <= capacity items and spans <= window."""
    batches, i, times = 0, 0, sorted(times)
    while i < len(times):
        j = i
        while j < len(times) and j - i < capacity and times[j] - times[i] <= window:
            j += 1
        batches += 1; i = j
    return batches


# ─────────────────────────────────────────────────────────────────────────────
# E. WEIGHTED SCHEDULING  — sort by end, DP + binary search for last compatible
# ─────────────────────────────────────────────────────────────────────────────
def max_profit_jobs(start, end, profit):
    """LC 1235 (Airbnb, Snowflake). O(n log n)."""
    jobs = sorted(zip(end, start, profit))
    ends = [e for e, _, _ in jobs]
    dp = [0] * (len(jobs) + 1)                     # dp[i] = best using first i jobs
    for i, (e, s, p) in enumerate(jobs, 1):
        k = bisect_right(ends, s, 0, i - 1)        # jobs ending <= s
        dp[i] = max(dp[i - 1], dp[k] + p)
    return dp[-1]


def max_credit_k_classes(classes, k):
    """Snowflake: same as above but at most k chosen -> dp[i][j]. O(n k log n)."""
    cl = sorted(classes, key=lambda c: c[1])
    ends = [c[1] for c in cl]
    n = len(cl)
    dp = [[0] * (k + 1) for _ in range(n + 1)]
    for i, (s, e, c) in enumerate(cl, 1):
        p = bisect_right(ends, s, 0, i - 1)
        for j in range(1, k + 1):
            dp[i][j] = max(dp[i - 1][j], dp[p][j - 1] + c)
    return dp[n][k]


# ─────────────────────────────────────────────────────────────────────────────
# F. RANGE STRUCTURES  — keep a sorted disjoint set you can add/remove/query
# ─────────────────────────────────────────────────────────────────────────────
class RangeModule:
    """LC 715 (Walmart). Half-open. Flat sorted boundary list: even idx = start."""
    def __init__(self):
        self.b = []

    def add(self, s, e):
        i, j = bisect_left(self.b, s), bisect_right(self.b, e)
        mid = ([s] if i % 2 == 0 else []) + ([e] if j % 2 == 0 else [])
        self.b[i:j] = mid

    def remove(self, s, e):
        i, j = bisect_left(self.b, s), bisect_right(self.b, e)
        mid = ([s] if i % 2 == 1 else []) + ([e] if j % 2 == 1 else [])
        self.b[i:j] = mid

    def query(self, s, e):
        i, j = bisect_right(self.b, s), bisect_left(self.b, e)
        return i == j and i % 2 == 1


def apply_range_flips(arr, ranges):
    """JPMorgan sign flips: difference array of flip counts, O(n + q)."""
    diff = [0] * (len(arr) + 1)
    for l, r in ranges:                 # inclusive
        diff[l] += 1; diff[r + 1] -= 1
    out, run = [], 0
    for x, d in zip(arr, diff):
        run += d
        out.append(-x if run % 2 else x)
    return out


def subtract(allowed, blocked):
    """Stripe deployment windows: union(allowed) minus union(blocked). Half-open."""
    out, blocked = [], merge(blocked)
    for s, e in merge(allowed):
        cur = s
        for bs, be in blocked:
            if be <= cur or bs >= e:
                continue
            if bs > cur:
                out.append([cur, bs])
            cur = max(cur, be)
        if cur < e:
            out.append([cur, e])
    return out


# ─────────────────────────────────────────────────────────────────────────────
# G. HEAP-DRIVEN SCHEDULER SIMULATION  — time moves forward, heap picks next
# ─────────────────────────────────────────────────────────────────────────────
def single_threaded_cpu(tasks):
    """LC 1834 (Roblox). tasks[i] = (enqueue, duration). Returns processing order."""
    order = sorted(range(len(tasks)), key=lambda i: tasks[i][0])
    ready, out, t, k = [], [], 0, 0
    while len(out) < len(tasks):
        if not ready and t < tasks[order[k]][0]:
            t = tasks[order[k]][0]                     # idle: jump ahead
        while k < len(order) and tasks[order[k]][0] <= t:
            i = order[k]; heapq.heappush(ready, (tasks[i][1], i)); k += 1
        d, i = heapq.heappop(ready)
        t += d; out.append(i)
    return out


class LazyPriorityScheduler:
    """Citadel / Snowflake: insert, cancel, pop-highest. Cancel is O(1) via lazy delete."""
    def __init__(self):
        self.heap, self.live, self.seq = [], {}, 0

    def add(self, task_id, priority):
        self.seq += 1
        self.live[task_id] = self.seq
        heapq.heappush(self.heap, (-priority, self.seq, task_id))

    def cancel(self, task_id):
        self.live.pop(task_id, None)

    def pop(self):
        while self.heap:
            _, seq, tid = heapq.heappop(self.heap)
            if self.live.get(tid) == seq:              # skip stale entries
                del self.live[tid]
                return tid
        return None


# ─────────────────────────────────────────────────────────────────────────────
# H. EFFECTIVE-DATED EVENT LEDGER  — Rippling / CodeSignal multi-level OA core
# ─────────────────────────────────────────────────────────────────────────────
class DeliveryBilling:
    """Rippling Delivery Billing core.
    - add_driver(id, rate) at time 0; set_rate(id, rate, effective_from)
    - record(id, start, end): cost = minutes x rate in effect for each piece
    - total() in O(1); pay_up_to(t) settles everything ending <= t."""
    def __init__(self):
        self.rate_times = {}     # id -> sorted change times
        self.rates = {}          # id -> rate at each change time
        self.unpaid = []         # heap of (end, cost)
        self.total_cost = 0.0

    def add_driver(self, did, rate):
        self.rate_times[did], self.rates[did] = [0], [rate]

    def set_rate(self, did, rate, effective_from):
        ts = self.rate_times[did]
        i = bisect_left(ts, effective_from)
        if i < len(ts) and ts[i] == effective_from:
            self.rates[did][i] = rate
        else:
            ts.insert(i, effective_from); self.rates[did].insert(i, rate)

    def _cost(self, did, s, e):
        ts, rs = self.rate_times[did], self.rates[did]
        i, cost = bisect_right(ts, s) - 1, 0.0
        while s < e:                                   # walk rate segments
            seg_end = min(e, ts[i + 1]) if i + 1 < len(ts) else e
            cost += (seg_end - s) * rs[i]
            s, i = seg_end, i + 1
        return cost

    def record(self, did, start, end):
        c = self._cost(did, start, end)
        self.total_cost += c
        heapq.heappush(self.unpaid, (end, c))
        return c

    def total(self):
        return self.total_cost

    def pay_up_to(self, t):
        paid = 0.0
        while self.unpaid and self.unpaid[0][0] <= t:
            paid += heapq.heappop(self.unpaid)[1]
        return paid


# ─────────────────────────────────────────────────────────────────────────────
if __name__ == "__main__":
    # A
    assert merge([[1, 3], [2, 6], [8, 10], [15, 18]]) == [[1, 6], [8, 10], [15, 18]]
    assert merge([[1, 2], [2, 3]]) == [[1, 3]]
    assert merge([[1, 2], [2, 3]], touching_merges=False) == [[1, 2], [2, 3]]
    assert merge([]) == []
    assert insert([[1, 2], [3, 5], [6, 7], [8, 10]], [4, 8]) == [[1, 2], [3, 10]]
    assert insert([], [5, 7]) == [[5, 7]]
    assert covered_length([[1, 3], [2, 5], [7, 8]], inclusive=True) == 7
    assert merge_with_indices([[5, 6], [1, 3], [2, 4]]) == [[1, 4, [1, 2]], [5, 6, [0]]]
    assert highlight("aaabbcc", [[0, 3], [3, 5]]) == "<b>aaabb</b>cc"
    # B
    assert max_overlap([[0, 30], [5, 10], [15, 20]]) == 2
    assert max_overlap([[1, 3], [3, 5]]) == 1
    assert max_overlap([[1, 3], [3, 5]], inclusive_end=True) == 2
    assert can_attend_all([[0, 5], [5, 10]]) and not can_attend_all([[0, 6], [5, 10]])
    assert assign_rooms([[0, 30], [5, 10], [15, 20]]) == (2, [0, 1, 1])
    assert active_segments([[1, 4], [2, 6], [4, 5]]) == [[1, 2, 1], [2, 5, 2], [5, 6, 1]]
    cal = MyCalendar()
    assert cal.book(10, 20) and not cal.book(15, 25) and cal.book(20, 30)
    # C
    assert intersect_lists([[0, 2], [5, 10]], [[1, 5], [8, 12]]) == [[1, 2], [5, 5], [8, 10]]
    assert union_sorted_lists([[1, 3], [6, 9]], [[2, 4], [10, 11]]) == [[1, 4], [6, 9], [10, 11]]
    # D
    assert min_removals_to_disjoint([[1, 2], [2, 3], [3, 4], [1, 3]]) == 1
    assert min_batches([1, 3, 5, 7, 9], capacity=2, window=10) == 3
    # E
    assert max_profit_jobs([1, 2, 3, 3], [3, 4, 5, 6], [50, 10, 40, 70]) == 120
    assert max_credit_k_classes([(1, 3, 5), (3, 5, 6), (5, 7, 4)], 2) == 11
    # F
    rm = RangeModule(); rm.add(10, 20); rm.remove(14, 16)
    assert rm.query(10, 14) and not rm.query(13, 15) and rm.query(16, 17)
    assert apply_range_flips([1, 2, 3, 4], [[0, 1], [1, 2]]) == [-1, 2, -3, 4]
    assert subtract([[0, 10]], [[2, 3], [5, 7]]) == [[0, 2], [3, 5], [7, 10]]
    # G
    assert single_threaded_cpu([(1, 2), (2, 4), (3, 2), (4, 1)]) == [0, 2, 3, 1]
    s = LazyPriorityScheduler(); s.add("a", 1); s.add("b", 5); s.cancel("b")
    assert s.pop() == "a" and s.pop() is None
    # H
    b = DeliveryBilling(); b.add_driver("d1", 1.0); b.set_rate("d1", 2.0, 60)
    assert b.record("d1", 30, 90) == 30 * 1.0 + 30 * 2.0
    assert b.total() == 90.0 and b.pay_up_to(80) == 0 and b.pay_up_to(90) == 90.0
    print("all interval templates pass")
