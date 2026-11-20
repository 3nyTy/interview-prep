"""Interview Python, Java -> Python. Every section runs; read it top to bottom.

    python python-fluency/reference.py      # prints "all reference checks pass"

Sections
 1. Java -> Python cheat lines        8. heapq (priority queues)
 2. Numbers & gotchas                 9. bisect (binary search on sorted lists)
 3. Strings                          10. itertools / functools
 4. Lists                            11. Classes & dataclasses (OOP rounds)
 5. Dicts                            12. Iterators & generators
 6. Sets & tuples                    13. Errors, typing, misc
 7. collections: Counter, defaultdict, deque, OrderedDict
"""
from collections import Counter, defaultdict, deque, OrderedDict
from dataclasses import dataclass, field
from functools import cache, reduce, cmp_to_key
from typing import Optional
import bisect
import heapq
import itertools
import math


# ── 1. JAVA -> PYTHON ────────────────────────────────────────────────────────
# for (int i = 0; i < n; i++)      ->  for i in range(n):
# for (int i = n-1; i >= 0; i--)   ->  for i in range(n - 1, -1, -1):   or reversed(range(n))
# for (String s : list)            ->  for s in lst:
# index + value                    ->  for i, x in enumerate(lst):
# two lists in step                ->  for a, b in zip(xs, ys):
# a ? b : c                        ->  b if a else c
# &&  ||  !                        ->  and  or  not
# null                             ->  None          (compare with `is None`)
# Integer.MAX_VALUE                ->  float("inf")  (or math.inf)
# list.size() / str.length()       ->  len(x)
# new int[n]                       ->  [0] * n
# new int[r][c]                    ->  [[0] * c for _ in range(r)]   NOT [[0]*c]*r (shared rows!)
# HashMap / HashSet                ->  dict / set
# ArrayDeque                       ->  collections.deque
# PriorityQueue (min)              ->  heapq on a list
# TreeMap                          ->  no built-in; sorted list + bisect
# swap a, b                        ->  a, b = b, a
# String.valueOf(5) / parseInt     ->  str(5) / int("5")
# integer division / modulo        ->  7 // 2 == 3 ;  -7 // 2 == -4 (floors!) ; int(-7 / 2) == -3


def section_numbers():
    assert 7 // 2 == 3 and -7 // 2 == -4 and int(-7 / 2) == -3   # floor vs truncate
    assert 7 % 3 == 1 and -7 % 3 == 2                             # result has sign of divisor
    assert divmod(17, 5) == (3, 2)
    assert 2 ** 10 == 1024 and pow(2, 10, 1000) == 24              # pow with modulus
    assert abs(-3) == 3 and max(3, 9, 1) == 9 and min([4, 2]) == 2
    assert round(2.5) == 2 and round(3.5) == 4                     # banker's rounding!
    assert math.isclose(0.1 + 0.2, 0.3)                            # never == on floats
    assert float("inf") > 10 ** 18                                 # ints never overflow
    assert int("101", 2) == 5 and bin(5) == "0b101" and (5).bit_count() == 2
    assert 5 & 3 == 1 and 5 | 3 == 7 and 5 ^ 3 == 6 and 1 << 3 == 8


# ── 3. STRINGS (immutable) ───────────────────────────────────────────────────
def section_strings():
    s = "Hello, World"
    assert s[0] == "H" and s[-1] == "d" and s[0:5] == "Hello" and s[::-1] == "dlroW ,olleH"
    assert s.lower() == "hello, world" and s.upper().startswith("HELLO")
    assert s.split(", ") == ["Hello", "World"] and "a b  c".split() == ["a", "b", "c"]
    assert "-".join(["a", "b", "c"]) == "a-b-c"
    assert "  pad ".strip() == "pad" and "xxhixx".strip("x") == "hi"
    assert s.find("World") == 7 and s.find("zzz") == -1 and "lo" in s
    assert s.replace("World", "Py") == "Hello, Py" and s.count("l") == 3
    assert "abc".isalpha() and "123".isdigit() and "a1".isalnum() and " ".isspace()
    assert ord("a") == 97 and chr(98) == "b" and ord("c") - ord("a") == 2
    assert f"{3.14159:.2f}" == "3.14" and f"{7:03d}" == "007" and f"{'x':>3}" == "  x"
    # Building strings: append to a list, join once (O(n)), don't += in a loop
    parts = []
    for ch in "abc":
        parts.append(ch.upper())
    assert "".join(parts) == "ABC"
    assert list("abc") == ["a", "b", "c"] and sorted("cba") == ["a", "b", "c"]


# ── 4. LISTS (dynamic arrays) ────────────────────────────────────────────────
def section_lists():
    a = [3, 1, 2]
    a.append(4); a.extend([5, 6]); a.insert(0, 0)      # insert is O(n)
    assert a == [0, 3, 1, 2, 4, 5, 6]
    assert a.pop() == 6 and a.pop(0) == 0               # pop(0) is O(n) -> use deque
    a.remove(3)                                         # first occurrence, O(n)
    assert a == [1, 2, 4, 5] and a.index(4) == 2 and 4 in a
    assert a[-1] == 5 and a[1:3] == [2, 4] and a[::2] == [1, 4]
    b = a[:]                                            # shallow copy
    b[0] = 99
    assert a[0] == 1
    a.sort(reverse=True)                                # in place, returns None
    assert a == [5, 4, 2, 1] and sorted(a) == [1, 2, 4, 5]
    assert sum(a) == 12 and max(a) == 5 and a.count(4) == 1
    # comprehensions
    assert [x * x for x in range(5) if x % 2 == 0] == [0, 4, 16]
    grid = [[0] * 3 for _ in range(2)]
    grid[0][0] = 1
    assert grid == [[1, 0, 0], [0, 0, 0]]
    assert [row[1] for row in [[1, 2], [3, 4]]] == [2, 4]        # a column
    assert list(zip(*[[1, 2], [3, 4]])) == [(1, 3), (2, 4)]      # transpose
    assert list(enumerate("ab", start=1)) == [(1, "a"), (2, "b")]
    assert any(x > 4 for x in a) and all(x > 0 for x in a)
    first, *rest = [1, 2, 3]                                     # unpacking
    assert first == 1 and rest == [2, 3]


# ── 5. DICTS (insertion-ordered hash maps) ───────────────────────────────────
def section_dicts():
    d = {"a": 1}
    d["b"] = 2
    assert d.get("z") is None and d.get("z", 0) == 0          # no KeyError
    assert d.setdefault("c", []) == [] and "c" in d
    del d["c"]
    assert d.pop("b") == 2 and d.pop("missing", None) is None
    d.update({"x": 9})
    assert list(d.keys()) == ["a", "x"] and list(d.values()) == [1, 9]
    for k, v in d.items():
        assert d[k] == v
    assert {k: v * 2 for k, v in d.items()} == {"a": 2, "x": 18}
    by_val = sorted(d.items(), key=lambda kv: -kv[1])          # sort map by value desc
    assert by_val[0] == ("x", 9)
    assert max(d, key=d.get) == "x"                            # key with max value
    assert {**d, "a": 0} == {"a": 0, "x": 9}                   # merge / override
    # keys must be hashable: tuples OK, lists not
    seen = {(0, 1): True}
    assert (0, 1) in seen


# ── 6. SETS & TUPLES ─────────────────────────────────────────────────────────
def section_sets_tuples():
    s = {1, 2, 3}
    s.add(4); s.discard(10)                           # discard: no error if absent
    t = {3, 4, 5}
    assert s & t == {3, 4} and s | t == {1, 2, 3, 4, 5} and s - t == {1, 2} and s ^ t == {1, 2, 5}
    assert {1, 2} <= s and set() == set([]) and len({1, 1, 2}) == 2
    fs = frozenset([1, 2])                            # hashable set (e.g. dict key)
    assert fs in {fs: 1}
    p = (3, 4)                                        # immutable, hashable, compares lexicographically
    x, y = p
    assert x == 3 and (1, 2) < (1, 3) < (2, 0)


# ── 7. COLLECTIONS ───────────────────────────────────────────────────────────
def section_collections():
    c = Counter("banana")
    assert c["a"] == 3 and c["z"] == 0                # missing -> 0
    assert c.most_common(1) == [("a", 3)]
    assert Counter("ab") + Counter("b") == Counter({"b": 2, "a": 1})
    assert Counter("listen") == Counter("silent")     # anagram check

    g = defaultdict(list)                             # adjacency list
    for u, v in [(1, 2), (1, 3)]:
        g[u].append(v)
    assert g[1] == [2, 3] and g[99] == []             # auto-creates on read!

    q = deque([1, 2])                                 # O(1) both ends: BFS queue
    q.append(3); q.appendleft(0)
    assert q.popleft() == 0 and q.pop() == 3 and list(q) == [1, 2]
    w = deque(maxlen=2)                               # sliding window of last k
    for i in range(5):
        w.append(i)
    assert list(w) == [3, 4]

    lru = OrderedDict()                               # LRU cache building block
    lru["a"] = 1; lru["b"] = 2
    lru.move_to_end("a")                              # mark most recent
    assert lru.popitem(last=False) == ("b", 2)        # evict least recent


# ── 8. HEAPQ (min-heap on a plain list) ──────────────────────────────────────
def section_heapq():
    h = []
    for x in [5, 1, 4]:
        heapq.heappush(h, x)
    assert h[0] == 1 and heapq.heappop(h) == 1        # h[0] = peek
    h = [5, 1, 4]; heapq.heapify(h)                   # O(n)
    # max-heap: negate
    mx = []
    for x in [5, 1, 4]:
        heapq.heappush(mx, -x)
    assert -heapq.heappop(mx) == 5
    # tuples compare left-to-right; add a counter so ties never compare objects
    tasks, seq = [], itertools.count()
    heapq.heappush(tasks, (2, next(seq), "write"))
    heapq.heappush(tasks, (1, next(seq), "read"))
    assert heapq.heappop(tasks)[2] == "read"
    assert heapq.nlargest(2, [3, 9, 1, 7]) == [9, 7] and heapq.nsmallest(1, [3, 9]) == [3]
    # top-k pattern: keep a size-k MIN heap of the k largest -> O(n log k)
    k, top = 2, []
    for x in [3, 9, 1, 7]:
        heapq.heappush(top, x)
        if len(top) > k:
            heapq.heappop(top)
    assert sorted(top) == [7, 9]


# ── 9. BISECT (binary search on a sorted list) ───────────────────────────────
def section_bisect():
    a = [1, 3, 3, 5]
    assert bisect.bisect_left(a, 3) == 1             # first index >= 3
    assert bisect.bisect_right(a, 3) == 3            # first index > 3
    assert bisect.bisect_left(a, 4) == 3             # insertion point
    i = bisect.bisect_left(a, 3)
    assert i < len(a) and a[i] == 3                  # "contains" check
    bisect.insort(a, 4)
    assert a == [1, 3, 3, 4, 5]
    # count in range [lo, hi]: bisect_right(hi) - bisect_left(lo)
    assert bisect.bisect_right(a, 4) - bisect.bisect_left(a, 3) == 3
    # search with key (3.10+)
    people = [("ann", 20), ("bob", 30)]
    assert bisect.bisect_left(people, 25, key=lambda p: p[1]) == 1


# ── 10. ITERTOOLS / FUNCTOOLS ────────────────────────────────────────────────
@cache                                     # memoization for top-down DP
def fib(n):
    return n if n < 2 else fib(n - 1) + fib(n - 2)


def section_itertools_functools():
    assert list(itertools.permutations([1, 2, 3], 2))[:2] == [(1, 2), (1, 3)]
    assert list(itertools.combinations("abc", 2)) == [("a", "b"), ("a", "c"), ("b", "c")]
    assert len(list(itertools.product([0, 1], repeat=3))) == 8
    assert list(itertools.accumulate([1, 2, 3])) == [1, 3, 6]          # prefix sums
    assert [(k, len(list(g))) for k, g in itertools.groupby("aabccc")] == [("a", 2), ("b", 1), ("c", 3)]
    assert list(itertools.chain([1], [2, 3])) == [1, 2, 3]
    assert list(itertools.pairwise([1, 2, 3])) == [(1, 2), (2, 3)]       # 3.10+
    assert fib(80) == 23416728348467685
    assert reduce(lambda acc, x: acc * x, [1, 2, 3, 4]) == 24
    # custom comparator (largest number problem): Java Comparator -> cmp_to_key
    nums = ["3", "30", "34"]
    nums.sort(key=cmp_to_key(lambda a, b: -1 if a + b > b + a else 1))
    assert "".join(nums) == "34330"
    # multi-key sort: by length asc, then alphabetically desc -> negate / two passes
    words = ["bb", "a", "cc", "b"]
    assert sorted(words, key=lambda w: (len(w), w)) == ["a", "b", "bb", "cc"]


# ── 11. CLASSES & DATACLASSES (practical / OOP rounds) ───────────────────────
@dataclass(order=True)
class Task:
    priority: int
    name: str = field(compare=False)
    tags: list = field(default_factory=list, compare=False)   # never `tags: list = []`


class Account:
    fee_rate = 0.01                       # class attribute (shared, like static)

    def __init__(self, owner: str, balance: int = 0):
        self.owner = owner                # instance attributes
        self._balance = balance           # leading _ = "private by convention"

    @property
    def balance(self) -> int:             # read like a field: acct.balance
        return self._balance

    def deposit(self, amount: int) -> int:
        if amount <= 0:
            raise ValueError("amount must be positive")
        self._balance += amount
        return self._balance

    def __repr__(self):                   # what print() / debugger shows
        return f"Account({self.owner!r}, {self._balance})"


class Savings(Account):                   # inheritance
    def deposit(self, amount):
        return super().deposit(amount + 1)


def section_classes():
    assert Task(1, "a") < Task(2, "b")    # order=True -> comparable, works in heapq
    t = Task(1, "x"); t.tags.append("urgent")
    assert Task(1, "y").tags == []        # default_factory gives each a fresh list
    acct = Savings("gowri")
    assert acct.deposit(10) == 11 and acct.balance == 11 and repr(acct) == "Account('gowri', 11)"
    try:
        Account("x").deposit(0)
        assert False
    except ValueError as e:
        assert "positive" in str(e)


# ── 12. ITERATORS & GENERATORS ───────────────────────────────────────────────
def read_in_chunks(items, size):
    """Generator: yields lazily, keeps O(size) memory (streaming questions)."""
    for i in range(0, len(items), size):
        yield items[i:i + size]


class Countdown:
    """Custom iterator protocol (Resumable Iterator style questions)."""
    def __init__(self, n):
        self.n = n

    def __iter__(self):
        return self

    def __next__(self):
        if self.n <= 0:
            raise StopIteration
        self.n -= 1
        return self.n + 1


def section_iterators():
    assert list(read_in_chunks([1, 2, 3, 4, 5], 2)) == [[1, 2], [3, 4], [5]]
    assert list(Countdown(3)) == [3, 2, 1]
    it = iter([1, 2])
    assert next(it) == 1 and next(it) == 2 and next(it, None) is None
    squares = (x * x for x in range(3))        # generator expression: () not []
    assert sum(squares) == 5


# ── 13. ERRORS, TYPING, MISC GOTCHAS ─────────────────────────────────────────
def find(nums: list[int], target: int) -> Optional[int]:
    """Type hints are optional but read well in interviews."""
    for i, x in enumerate(nums):
        if x == target:
            return i
    return None


def bad_default(x, acc=[]):          # GOTCHA: default list is shared across calls
    acc.append(x)
    return acc


def good_default(x, acc=None):
    acc = [] if acc is None else acc
    acc.append(x)
    return acc


def section_misc():
    assert find([4, 5], 5) == 1 and find([], 1) is None
    bad_default(1)
    assert bad_default(2) == [1, 2]                  # surprise!
    assert good_default(2) == [2]
    assert [] == [] and [] is not []                  # == value, `is` identity
    x = 5
    def bump():
        nonlocal_holder[0] += 1                      # closures can mutate, not rebind
    nonlocal_holder = [x]
    bump()
    assert nonlocal_holder == [6]
    import sys
    sys.setrecursionlimit(10_000)                    # default ~1000: deep DFS needs this or a stack
    assert 1 < 2 < 3 and not (1 < 3 < 2)             # chained comparisons
    assert (lambda a, b=2: a * b)(3) == 6


if __name__ == "__main__":
    for fn in [section_numbers, section_strings, section_lists, section_dicts,
               section_sets_tuples, section_collections, section_heapq, section_bisect,
               section_itertools_functools, section_classes, section_iterators, section_misc]:
        fn()
    print("all reference checks pass")
