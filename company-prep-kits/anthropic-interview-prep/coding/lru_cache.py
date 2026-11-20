"""
Anthropic - Staff SWE coding interview practice
Problem: "LRU Cache" - fix the *args/**kwargs key-generation bug, then make the
cache durable across crashes.

This is the PRIMARY (Python) solution - the interview is in Python and the
Part-1 bug is Python-specific. A faithful Java port lives in LRUCache.java.

Run:  python3 lru_cache.py
"""
import json
import os
import threading
from collections import OrderedDict


# =====================================================================
# PART 1 - bug-fixed LRU cache
# =====================================================================
#
# THE BUG (typical skeleton):
#     def generate_key(self, *args, **kwargs):
#         return (args, kwargs)          # <-- broken
#
#   1. kwargs is a dict -> dicts are UNHASHABLE -> `key in self.cache`
#      raises `TypeError: unhashable type: 'dict'`.
#   2. Even `(args, tuple(kwargs.items()))` is NOT order-stable:
#      f(a=1, b=2) and f(b=2, a=1) would produce different keys.
#   3. If args/kwargs contain lists or dicts, the tuple is STILL unhashable
#      because it transitively contains an unhashable element.
#
# THE FIX: recursively "freeze" every argument into a hashable, canonical,
# insertion-order-independent form (lists/tuples -> tuple, dict -> sorted
# tuple of items, set -> sorted tagged tuple), then sort kwargs by key.
# This is the same idea behind functools._make_key.
class LRU:
    def __init__(self, capacity=128):
        self.capacity = capacity
        self.cache = OrderedDict()          # preserves LRU (front) -> MRU (back) order

    @staticmethod
    def _freeze(obj):
        """Turn an arbitrary argument into a hashable, order-stable value."""
        if isinstance(obj, (list, tuple)):
            return tuple(LRU._freeze(x) for x in obj)
        if isinstance(obj, dict):
            # sort by key so {a:1,b:2} and {b:2,a:1} collapse to one key.
            # sort on repr(key) so heterogeneous/unorderable keys never raise.
            return tuple(sorted(((k, LRU._freeze(v)) for k, v in obj.items()),
                                key=lambda kv: repr(kv[0])))
        if isinstance(obj, (set, frozenset)):
            return ("__set__", tuple(sorted((LRU._freeze(x) for x in obj), key=repr)))
        return obj                           # int/str/float/bool/None - already hashable

    def generate_key(self, *args, **kwargs):
        # args stay positional (order matters); kwargs are frozen+sorted (order doesn't)
        return (self._freeze(args), self._freeze(kwargs))

    def call(self, func, *args, **kwargs):
        # include the callable's identity so f(1) and g(1) don't collide
        key = (getattr(func, "__qualname__", repr(func)),) + self.generate_key(*args, **kwargs)
        if key in self.cache:                       # HIT
            self.cache.move_to_end(key)             # mark most-recently-used
            return self.cache[key]
        result = func(*args, **kwargs)              # MISS -> compute
        self.cache[key] = result
        self.cache.move_to_end(key)
        if len(self.cache) > self.capacity:
            self.cache.popitem(last=False)          # evict least-recently-used (front)
        return result


# =====================================================================
# PART 2 - durable LRU via a Write-Ahead Log (WAL)
# =====================================================================
#
# CHANGES FROM PART 1 (search for "# [P2]"):
#   * __init__ replays the WAL to rebuild contents AND LRU order.
#   * call() appends an fsync'd record BEFORE returning -> durability:
#         - miss : append {"op":"s", key, value}
#         - hit  : append {"op":"t", key}  (value already on disk -> saves I/O)
#   * _recover() tolerates a torn final line and truncates it.
#   * _compact() periodically snapshots so the log stays bounded.
#   * call() is guarded by a lock (single writer).
#
# Why not log evictions? They are deterministic: replaying the same 's'
# sequence with the same capacity reproduces the exact evictions, so the
# recovered cache equals what it would have been with no crash.
class DurableLRU(LRU):
    def __init__(self, capacity=128, path="cache.wal", fsync=True, compact_every=10_000):
        super().__init__(capacity)
        self.path = path
        self.fsync = fsync
        self.compact_every = compact_every
        self._lock = threading.Lock()
        self._since_compact = 0
        self._recover()                              # [P2] rebuild from disk
        self._log = open(self.path, "a")             # [P2] append handle for new writes

    # ---- JSON helpers: tuples must round-trip (a JSON array decodes to a list) ----
    @staticmethod
    def _encode(o):
        if isinstance(o, tuple):
            return {"__t__": [DurableLRU._encode(x) for x in o]}      # tag tuples
        if isinstance(o, list):
            return [DurableLRU._encode(x) for x in o]
        if isinstance(o, dict):
            return {"__d__": [[DurableLRU._encode(k), DurableLRU._encode(v)] for k, v in o.items()]}
        return o

    @staticmethod
    def _decode(o):
        if isinstance(o, dict):
            if "__t__" in o:
                return tuple(DurableLRU._decode(x) for x in o["__t__"])
            if "__d__" in o:
                return {DurableLRU._decode(k): DurableLRU._decode(v) for k, v in o["__d__"]}
        if isinstance(o, list):
            return [DurableLRU._decode(x) for x in o]
        return o

    def _write(self, record):                        # [P2]
        self._log.write(json.dumps(record, separators=(",", ":")) + "\n")
        self._log.flush()                            # JVM/Python buffer -> OS
        if self.fsync:
            os.fsync(self._log.fileno())             # OS page cache -> physical disk (durable)
        self._since_compact += 1
        if self._since_compact >= self.compact_every:
            self._compact()

    def call(self, func, *args, **kwargs):           # [P2] overrides Part 1
        with self._lock:                             # single-writer thread safety
            key = (getattr(func, "__qualname__", repr(func)),) + self.generate_key(*args, **kwargs)
            if key in self.cache:                            # HIT
                value = self.cache[key]
                self.cache.move_to_end(key)
                self._write({"op": "t", "k": self._encode(key)})    # log recency only
                return value
            value = func(*args, **kwargs)                    # MISS -> compute
            self.cache[key] = value
            self.cache.move_to_end(key)
            self._write({"op": "s", "k": self._encode(key), "v": self._encode(value)})  # durable before return
            if len(self.cache) > self.capacity:
                self.cache.popitem(last=False)
            return value

    def _recover(self):                              # [P2]
        if not os.path.exists(self.path):
            return
        good_len = 0
        with open(self.path, "rb") as f:
            for raw in f:                            # iterate by line (each record is one line)
                try:
                    rec = json.loads(raw.decode("utf-8"))
                    op = rec["op"]
                    key = self._decode(rec["k"])
                    value = self._decode(rec["v"]) if op == "s" else None
                except Exception:
                    break                            # torn / partial tail from a crash mid-write
                if op == "s":
                    self.cache[key] = value
                    self.cache.move_to_end(key)
                    if len(self.cache) > self.capacity:
                        self.cache.popitem(last=False)
                elif op == "t":
                    if key in self.cache:
                        self.cache.move_to_end(key)  # CRUCIAL: re-touch or LRU degrades to FIFO
                good_len += len(raw)
        if good_len < os.path.getsize(self.path):    # drop torn tail -> future appends stay contiguous
            os.truncate(self.path, good_len)

    def _compact(self):                              # [P2] snapshot + atomic swap
        tmp = self.path + ".tmp"
        with open(tmp, "w") as f:
            for key, value in self.cache.items():    # LRU -> MRU order
                f.write(json.dumps({"op": "s", "k": self._encode(key), "v": self._encode(value)},
                                   separators=(",", ":")) + "\n")
            f.flush()
            os.fsync(f.fileno())
        self._log.close()
        os.replace(tmp, self.path)                   # atomic on POSIX
        self._log = open(self.path, "a")
        self._since_compact = 0

    def close(self):
        self._log.close()


# =====================================================================
# Self-verifying demo
# =====================================================================
if __name__ == "__main__":
    import tempfile

    calls = {"n": 0}
    def square(x):
        calls["n"] += 1
        return x * x

    # Part 1: key stability across the four shapes the prompt asks for.
    c = LRU(2)
    assert c.generate_key(1, 2) == c.generate_key(1, 2)                       # positional
    assert c.generate_key(a=1, b=2) == c.generate_key(b=2, a=1)               # kwargs (order)
    assert c.generate_key(1, x=2) == c.generate_key(1, x=2)                   # mixed
    assert c.generate_key(d={'p': [1, 2], 'q': 3}) == c.generate_key(d={'q': 3, 'p': [1, 2]})  # nested
    assert c.generate_key(1, 2) != c.generate_key(2, 1)                       # positional order matters
    _ = c.generate_key([1, 2], cfg={'k': {'n': [1]}})                         # unhashable args don't crash
    calls["n"] = 0
    assert c.call(square, 5) == 25 and c.call(square, 5) == 25 and calls["n"] == 1  # second call hits
    print("PART 1 OK")

    tmp = tempfile.mkdtemp()
    # Part 2: durability + LRU order survive a "crash"
    p = os.path.join(tmp, "a.wal")
    live = DurableLRU(capacity=3, path=p)
    for x in [1, 2, 3, 4, 1]:
        live.call(square, x)
    snap = list(live.cache.keys()); live.close()
    assert list(DurableLRU(capacity=3, path=p).cache.keys()) == snap
    print("PART 2 durability OK; order(args) =", [k[1] for k in snap])

    # Part 2: recency from hits survives restart (catches the FIFO-degradation bug)
    p2 = os.path.join(tmp, "b.wal")
    L = DurableLRU(capacity=2, path=p2)
    L.call(square, 1); L.call(square, 2); L.call(square, 1); L.close()   # touch 1
    R = DurableLRU(capacity=2, path=p2)
    R.call(square, 3)                                                    # must evict 2, keep 1
    assert sorted(k[1] for k in R.cache.keys()) == [(1,), (3,)]; R.close()
    print("PART 2 recency-survives-restart OK")

    # Part 2: torn tail tolerated + truncated
    p3 = os.path.join(tmp, "c.wal")
    D = DurableLRU(capacity=5, path=p3); D.call(square, 7); D.call(square, 8); D.close()
    with open(p3, "a") as fh:
        fh.write('{"op":"s","k":{"__t__":["square"')          # crash mid-write
    D2 = DurableLRU(capacity=5, path=p3)
    assert sorted(k[1] for k in D2.cache.keys()) == [(7,), (8,)]
    D2.call(square, 9); D2.close()
    assert sorted(k[1] for k in DurableLRU(capacity=5, path=p3).cache.keys()) == [(7,), (8,), (9,)]
    print("PART 2 torn-tail tolerated + truncated OK")

    print("\nALL TESTS PASSED")
