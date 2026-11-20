"""Python fluency drills. Replace each `...` with your code, then run:

    python python-fluency/drills.py

Each drill targets one tool from reference.py (named in the docstring).
Goal: each one in under 3 minutes, without looking. Redo the file from scratch
(git checkout python-fluency/drills.py) after 2 days and again after 7.
"""
from collections import Counter, defaultdict, deque
import heapq
import bisect
import itertools


# ── Level 1: core syntax ─────────────────────────────────────────────────────
def reverse_words(s: str) -> str:
    """'  the sky  is blue ' -> 'blue is sky the'   (split, slicing, join)"""
    ...


def is_palindrome(s: str) -> bool:
    """Ignore non-alphanumerics and case. 'A man, a plan, a canal: Panama' -> True"""
    ...


def make_grid(rows: int, cols: int, fill: int) -> list[list[int]]:
    """A rows x cols grid where changing one cell doesn't change other rows."""
    ...


def evens_squared(nums: list[int]) -> list[int]:
    """[1,2,3,4] -> [4,16]   (one list comprehension)"""
    ...


def transpose(m: list[list[int]]) -> list[list[int]]:
    """[[1,2,3],[4,5,6]] -> [[1,4],[2,5],[3,6]]   (zip(*m))"""
    ...


# ── Level 2: dicts, sets, Counter, defaultdict ──────────────────────────────
def two_sum(nums: list[int], target: int) -> tuple[int, int]:
    """Indices (i, j), i < j, one pass with a dict."""
    ...


def group_anagrams(words: list[str]) -> list[list[str]]:
    """['eat','tea','tan','ate','nat','bat'] -> groups (any order). defaultdict(list) + sorted key."""
    ...


def top_k_frequent(nums: list[int], k: int) -> list[int]:
    """Counter.most_common. Return the k values, most frequent first."""
    ...


def first_unique_char(s: str) -> int:
    """Index of first non-repeating char, -1 if none. 'leetcode' -> 0"""
    ...


def common_elements(a: list[int], b: list[int]) -> list[int]:
    """Sorted unique values in both lists. Set intersection."""
    ...


# ── Level 3: sorting with keys ───────────────────────────────────────────────
def sort_people(people: list[tuple[str, int]]) -> list[tuple[str, int]]:
    """Sort by age DESC, then name ASC. [('bob',30),('amy',30),('cal',25)] -> amy, bob, cal"""
    ...


def largest_number(nums: list[int]) -> str:
    """[3,30,34,5,9] -> '9534330'. Needs functools.cmp_to_key."""
    ...


# ── Level 4: deque, heapq, bisect ────────────────────────────────────────────
def bfs_shortest(grid: list[str]) -> int:
    """Steps from top-left to bottom-right through '.' cells ('#' = wall), 4 directions.
    -1 if unreachable. Use deque and a visited set."""
    ...


def k_closest(points: list[tuple[int, int]], k: int) -> list[tuple[int, int]]:
    """k points closest to origin, any order. Size-k heap (negate distance for max-heap)."""
    ...


def merge_k_sorted(lists: list[list[int]]) -> list[int]:
    """Merge k sorted lists with a heap of (value, list_index, element_index)."""
    ...


def count_in_range(sorted_nums: list[int], lo: int, hi: int) -> int:
    """How many values in [lo, hi]? Two bisect calls, O(log n)."""
    ...


class TimeMap:
    """set(key, value, ts) with increasing ts; get(key, ts) -> value at the latest ts' <= ts, else ''.
    defaultdict(list) + bisect."""
    def __init__(self):
        ...

    def set(self, key: str, value: str, ts: int) -> None:
        ...

    def get(self, key: str, ts: int) -> str:
        ...


# ── Level 5: generators, itertools, classes ──────────────────────────────────
def run_length(s: str) -> str:
    """'aaabccdddd' -> 'a3b1c2d4'   (itertools.groupby)"""
    ...


def sliding_windows(nums: list[int], k: int):
    """Generator yielding each window of size k as a tuple. [1,2,3,4], 2 -> (1,2),(2,3),(3,4)"""
    ...


class LRUCache:
    """get / put in O(1); evict least recently used when over capacity. Use OrderedDict."""
    def __init__(self, capacity: int):
        ...

    def get(self, key: int) -> int:
        ...

    def put(self, key: int, value: int) -> None:
        ...


# ── tests (don't edit) ───────────────────────────────────────────────────────
def _tests():
    checks = {
        "reverse_words": lambda: reverse_words("  the sky  is blue ") == "blue is sky the",
        "is_palindrome": lambda: is_palindrome("A man, a plan, a canal: Panama") and not is_palindrome("race a car"),
        "make_grid": lambda: (lambda g: (g[0].__setitem__(0, 9), g[1][0] == 0)[1])(make_grid(2, 2, 0)),
        "evens_squared": lambda: evens_squared([1, 2, 3, 4]) == [4, 16],
        "transpose": lambda: [list(r) for r in transpose([[1, 2, 3], [4, 5, 6]])] == [[1, 4], [2, 5], [3, 6]],
        "two_sum": lambda: tuple(two_sum([2, 7, 11, 15], 9)) == (0, 1),
        "group_anagrams": lambda: sorted(sorted(g) for g in group_anagrams(["eat", "tea", "tan", "ate", "nat", "bat"]))
                                  == [["ate", "eat", "tea"], ["bat"], ["nat", "tan"]],
        "top_k_frequent": lambda: top_k_frequent([1, 1, 1, 2, 2, 3], 2) == [1, 2],
        "first_unique_char": lambda: first_unique_char("leetcode") == 0 and first_unique_char("aabb") == -1,
        "common_elements": lambda: common_elements([4, 1, 2, 2], [2, 4, 4, 9]) == [2, 4],
        "sort_people": lambda: sort_people([("bob", 30), ("amy", 30), ("cal", 25)]) == [("amy", 30), ("bob", 30), ("cal", 25)],
        "largest_number": lambda: largest_number([3, 30, 34, 5, 9]) == "9534330",
        "bfs_shortest": lambda: bfs_shortest(["..#", "#..", "#.."]) == 4 and bfs_shortest([".#", "#."]) == -1,
        "k_closest": lambda: sorted(k_closest([(1, 3), (-2, 2), (5, 8), (0, 1)], 2)) == [(-2, 2), (0, 1)],
        "merge_k_sorted": lambda: merge_k_sorted([[1, 4, 5], [1, 3, 4], [2, 6], []]) == [1, 1, 2, 3, 4, 4, 5, 6],
        "count_in_range": lambda: count_in_range([1, 3, 3, 5, 8], 3, 5) == 3 and count_in_range([], 0, 9) == 0,
        "TimeMap": lambda: (lambda t: (t.set("k", "a", 1), t.set("k", "b", 4),
                                       (t.get("k", 0), t.get("k", 3), t.get("k", 9), t.get("x", 1)))[2]
                            == ("", "a", "b", ""))(TimeMap()),
        "run_length": lambda: run_length("aaabccdddd") == "a3b1c2d4" and run_length("") == "",
        "sliding_windows": lambda: list(sliding_windows([1, 2, 3, 4], 2)) == [(1, 2), (2, 3), (3, 4)],
        "LRUCache": lambda: (lambda c: (c.put(1, 1), c.put(2, 2), c.get(1), c.put(3, 3),
                                        (c.get(2), c.get(1), c.get(3)))[4] == (-1, 1, 3))(LRUCache(2)),
    }
    passed = 0
    for name, check in checks.items():
        try:
            ok = bool(check())
        except Exception as e:                       # unfinished drills raise or return None
            ok, err = False, e
        print(f"  {'PASS' if ok else 'todo'}  {name}")
        passed += ok
    print(f"\n{passed}/{len(checks)} drills passing")


if __name__ == "__main__":
    _tests()
