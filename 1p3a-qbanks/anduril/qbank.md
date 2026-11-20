# Anduril — 1point3acres Question Bank (5 OJ problems, no curated bank)

Source: 1point3acres (`company=anduril`), captured 2026-09-21 from the logged-in session via AppleScript-driven Chrome plus the site's v3 API. Company page: <https://www.1point3acres.com/interview/problems/company/anduril>

Anduril is **not** one of the 56 companies with a curated qbank. The site lists 5 entries for it and all 5 are OJ problems (`qbank_count: 0`, `oj_count: 5`, `external_count: 0`). Statements render without membership; test cases and the run harness do not.

Separately the site carries **11 interview-experience threads** for Anduril, all in the 海外面经 forum. Their bodies sit behind a `[hide=200]` gate (积分 > 200); the thread replies are readable and captured in `threads.md`.

## Index

1. Dynamic Suffix Word Search
2. Evaluate a Flat Addition/Subtraction Expression
3. Find Unreachable Heap Objects with References
4. Find Unreachable Heap Objects from Stack Addresses
5. Max Profit from Cutting Rods into Equal Sale Length

---

## 1. Dynamic Suffix Word Search

- **kind**: oj problem (no curated qbank entry)
- **url**: https://www.1point3acres.com/interview/problems/c3fb79fa-5166-55c2-b3d8-82d68cac8450

### Statement

```
Design a data structure that supports dynamically adding lowercase English words and querying by suffix.

Implement:

addWord(word)
search(suffix)

addWord(word): add one word. Duplicate additions are allowed, and each addition counts as a separate word.

search(suffix): return the number of currently added words that end with suffix.

The empty suffix "" matches every added word.

The input consists of operations:

add <word>
search <suffix>

Print one result for every search operation.

Example:

add apple
add apply
add maple
search ple
search ly
search zz

Output:

2
1
0

Constraints:

At most 10^5 operations

Words and suffixes contain only a-z

Each word has length at most 100

The total length of all inserted words is at most 10^6.
```

## 2. Evaluate a Flat Addition/Subtraction Expression

- **kind**: oj problem (no curated qbank entry)
- **url**: https://www.1point3acres.com/interview/problems/625a7191-8e4d-5c30-8685-261662c1e982

### Statement

```
Given a non-empty array tokens, where the first element is either "+" or "-" and all remaining elements are integers:

For "+", return the sum of all operands.

For "-", start with the first operand and subtract every remaining operand from it.

Examples:

["+", 1, 2, 3]  => 6
["-", 10, 5, 3] => 2

Implement a function that evaluates the expression.

Constraints:

2 <= len(tokens) <= 10^5

tokens[0] is either "+" or "-"

Each integer is in [-10^9, 10^9]

The result fits in a signed 64-bit integer.

Follow-up: How would you structure the code to support multiplication ("*") and division ("/"), assuming division truncates toward zero and division by zero never occurs?
```

## 3. Find Unreachable Heap Objects with References

- **kind**: oj problem (no curated qbank entry)
- **url**: https://www.1point3acres.com/interview/problems/eb23542d-56fe-54a3-902f-bf2b36d17b6c

### Statement

```
Problem: Heap Object Reachability Analysis with References

You are given a list of addresses stored on the stack, stack_addresses, and a list of heap objects, heap_objects. Each heap object contains:

address: the object's own address.

references: a list of other addresses referenced by this object.

class HeapObject:
    def __init__(self, address: int, references: List[int]):
        self.address = address
        self.references = references

Implement:

def dead_objects(stack_addresses: List[int], heap_objects: List[HeapObject]) -> List[HeapObject]:

Starting from stack_addresses, if an address corresponds to a heap object, that object is reachable. Then, any heap object referenced by a reachable object is also reachable, recursively. Return all heap objects that are ultimately unreachable.

Assumptions

Addresses in heap_objects are unique.

references may contain addresses that do not exist in heap_objects; ignore them.

The reference graph may contain cycles.

The result should preserve the original order of heap_objects.

Input Format

For testing, use the following stdin format:

n m
s1 s2 ... sn
address_1 k_1 ref_1 ref_2 ... ref_k1
address_2 k_2 ref_1 ref_2 ... ref_k2
...
address_m k_m ref_1 ref_2 ... ref_km

Where:

n is the number of stack addresses.

m is the number of heap objects.

The second line contains n integers representing stack_addresses. If n = 0, this line may be empty.

Each of the next m lines describes one heap object: its address address_i, the number of references k_i, followed by k_i referenced addresses.

Output Format

Print the addresses of all dead objects in the original heap_objects order, separated by spaces. If there are no dead objects, print an empty line.

Constraints

0 <= n, m <= 10^5

Total number of references R <= 2 * 10^5

Addresses are 32-bit signed integers.
```

## 4. Find Unreachable Heap Objects from Stack Addresses

- **kind**: oj problem (no curated qbank entry)
- **url**: https://www.1point3acres.com/interview/problems/de2532f4-133e-527d-888e-20ebbb0cf406

### Statement

```
Problem: Find Unreachable Heap Objects from Stack Addresses

You are given a list of addresses stored on the stack, stack_addresses, and a list of heap objects, heap_objects. Each heap object only has one field, its address:

class HeapObject:
    def __init__(self, address: int):
        self.address = address

Implement:

def dead_objects(stack_addresses: List[int], heap_objects: List[HeapObject]) -> List[HeapObject]:

Return all heap objects that are not directly reachable from the stack. In other words, if a heap object's address is not contained in stack_addresses, then it is a dead object.

Assumptions

Addresses in heap_objects are unique.

The result should preserve the original order of heap_objects.

stack_addresses may contain addresses that do not correspond to any heap object.

Input Format

For testing, use the following stdin format:

n m
s1 s2 ... sn
h1
h2
...
hm

Where:

n is the number of stack addresses.

m is the number of heap objects.

The second line contains n integers representing stack_addresses. If n = 0, this line may be empty.

The next m lines each contain one integer representing a heap object's address.

Output Format

Print the addresses of all dead objects in the original heap_objects order, separated by spaces. If there are no dead objects, print an empty line.

Constraints

0 <= n, m <= 10^5

Addresses are 32-bit signed integers.
```

## 5. Max Profit from Cutting Rods into Equal Sale Length

- **kind**: oj problem (no curated qbank entry)
- **url**: https://www.1point3acres.com/interview/problems/67fbaffb-1cf3-4cf6-8274-93dcc53df969

### Statement

```
Problem: Max Profit from Cutting Rods into Equal Sale Length

You are given a list of rod lengths lengths. You may choose an integer saleLength (saleLength >= 1) and cut rods into smaller pieces of length saleLength to sell.

Each cut costs costPerCut.

Each produced piece of length saleLength earns revenue saleLength * salePrice.

For an original rod of length L:

You can obtain k = floor(L / saleLength) sellable pieces.

The leftover L % saleLength is discarded and yields no revenue.

If k = 0, the rod yields nothing.

If L % saleLength == 0 (exactly divisible), producing k pieces requires k - 1 cuts.

Otherwise, producing k pieces requires k cuts.

You may discard any rod entirely if using it would reduce total profit.

Choose saleLength to maximize total profit:

total profit = total revenue − total cutting cost

Implement:

def maxProfit(costPerCut: int, salePrice: int, lengths: list[int]) -> int:
    """Return the maximum achievable profit."""

Constraints (reasonable interview assumptions)

1 <= len(lengths) <= 2e5

1 <= lengths[i] <= 1e5 (or larger; justify complexity)

0 <= costPerCut <= 1e9

1 <= salePrice <= 1e9

Sample Tests

costPerCut=1, salePrice=10, lengths=[5] → 50

costPerCut=2, salePrice=1, lengths=[4,4,4] → 12

costPerCut=100, salePrice=1, lengths=[10,20] → 20

costPerCut=1, salePrice=2, lengths=[1,2,3,4,5] → 18

(Any valid set of 5 tests that covers divisibility vs. remainder and discarding rods is acceptable in an interview setting.)
```
