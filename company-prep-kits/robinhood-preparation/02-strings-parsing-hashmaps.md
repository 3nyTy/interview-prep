# Strings, parsing, and hash maps — 4 problems

The listing marks "String Processing/Parsing" as a medium-frequency hot coding area, tagged
`string-processing, parsing, hashmap, sorting`.

---

## 1. Parse and Validate User Requests and Output Final Balances

Source: `2c4d57aa-b397-483d-acc4-87bc7a7d4599`
This is the most substantial coding problem in the bank. Prioritize it.

### Statement

You receive a series of requests as strings. The system handles these operations:

| Operation | Form |
|---|---|
| Register a user | `SIGNUP <user>` |
| Send friend request | `FRIEND_REQUEST <from> <to>` |
| Accept friend request | `FRIEND_ACK <to> <from>` |
| Transfer funds | `SEND_MONEY <from> <to> <amount>` |

### Validation rules

- **SIGNUP** — username must be non-empty and unregistered. Otherwise invalid.
- **FRIEND_REQUEST** — both users must exist, and `from != to`. Invalid if they are already
  friends or a pending request already exists.
- **FRIEND_ACK** — a matching pending request must exist. On success, remove the request and
  create the friendship.
- **SEND_MONEY** — both users must exist and must be friends. `amount > 0`. Invalid if the
  balance is insufficient.
- **State preservation** — invalid requests must not change any state.

### Output

Print every user with their final balance, sorted alphabetically by username.
Format: `user balance`, one per line.

### Constraints

- Requests: R ≤ 200,000
- Username length ≤ 30 characters
- Sums fit in a 64-bit integer

### Approach

The rule "invalid requests do not alter any state" is the core of the problem. Use
**validate fully, then mutate** — never mutate as you validate. A partially applied
`SEND_MONEY` (debit succeeds, credit fails) is the failure this rule is written to catch.

```java
// one handler per verb, same shape every time
boolean sendMoney(String from, String to, long amount) {
    if (!users.containsKey(from) || !users.containsKey(to)) return false;
    if (amount <= 0) return false;
    if (!friends.get(from).contains(to)) return false;
    if (balances.get(from) < amount) return false;
    // every check passed — now mutate
    balances.merge(from, -amount, Long::sum);
    balances.merge(to,    amount, Long::sum);
    return true;
}
```

State to keep:
- `Map<String, Long> balances` — also serves as the user registry
- `Map<String, Set<String>> friends` — symmetric, write both directions on ACK
- `Set<String> pending` — key it as `from + "\0" + to` so the direction is preserved

Use a `TreeMap` for balances and the alphabetical output requirement is free.

### Traps

- **Friendship is symmetric, pending requests are not.** `FRIEND_ACK <to> <from>` has the
  arguments reversed relative to `FRIEND_REQUEST <from> <to>`. Read the argument order
  twice; this is a deliberate trip-wire.
- A pending request in the reverse direction — B requests A while A→B is already pending.
  Ask whether that auto-accepts or is invalid.
- Starting balance is not stated in the public text. Ask. Assume 0 and say so.
- `amount` may be fractional in some variants. The 64-bit note suggests integers here.
- Do not print users who never signed up.

### Paywalled

Sample input/output, test cases, difficulty, round type, frequency, follow-ups, solution.

---

## 2. Word Frequency in a Large Paragraph

Source: `d4e34cd5-46fe-55f5-9eff-4fd1b728f694`

### Statement

Process a large string by splitting it into words on spaces and punctuation, then tally and
display each word's frequency.

### Requirements

1. Case-insensitive matching (`Hello` = `hello` = `HELLO`).
2. Punctuation acts as a delimiter only — it is never part of a word.
3. A word is "a consecutive sequence of English letters or digits, equivalent to
   `[A-Za-z0-9]+`".
4. Sort by frequency descending, then lexicographically ascending for ties.
5. Format: `word frequency`, one per line.

### Constraints

- Large string of length n
- m distinct words
- Time complexity and memory use should be optimized

### Example

Input: `hello world, hello computer`

```
hello 2
computer 1
world 1
```

Note `computer` before `world` — equal counts, so lexicographic ascending applies.

### Approach

Single pass, hand-rolled tokenizer. Do not use `split()` with a regex on a large input —
it allocates the whole array at once.

```java
Map<String, Integer> freq = new HashMap<>();
int i = 0, n = s.length();
while (i < n) {
    while (i < n && !isAlnum(s.charAt(i))) i++;
    int start = i;
    while (i < n && isAlnum(s.charAt(i))) i++;
    if (i > start) freq.merge(s.substring(start, i).toLowerCase(), 1, Integer::sum);
}
```

Then sort the entry set with a comparator: count descending, key ascending.
O(n + m log m).

### Traps

- The tie-break has two levels. Missing the lexicographic tie-break is the common failure.
- Digits count as word characters. `abc123` is one word.
- Lowercase once, at insert time. Do not lowercase the whole input string first — that
  doubles peak memory on a large input.
- If m is huge and only the top K matter, mention a heap. The stated output is all words.

### Paywalled

Complete details, test cases, solution approach, round type, frequency, date asked.

---

## 3. Distribute Strings into K Lines with Greedy Placement

Source: `84ccf656-2a40-4ff8-8080-a648e545eb50`

### Statement

Two parts.

**Part 1** — implement a method that assigns each string from a list to any of `k` lines,
then output the final strings for each line. Strings cannot be split.

**Part 2** — a line's load is the total length of its assigned strings. For each string in
order, assign it to the line with the **minimum current load**. Break ties by choosing the
**smallest line index**. Output the final strings per line and the load values.

### Constraints

- String list length: 1 to 2 x 10^5
- k: 1 to 2 x 10^5
- Individual string length: 1 to 1000

### Example

`strings = ["a","bbb","cc","dddd"]`, `k = 3`. Part 2 result:

```
line0: ["a","dddd"],  load = 5
line1: ["bbb"],       load = 3
line2: ["cc"],        load = 2
```

Trace: `a`→line0 (all loads 0, smallest index). `bbb`→line1 (0 < 1). `cc`→line2 (0 is
still available). `dddd`→line0 (loads are 1,3,2 — line0 is lowest).

### Approach

Min-heap keyed on `(load, index)`. The tuple ordering gives the smallest-index tie-break
for free.

```java
PriorityQueue<long[]> pq = new PriorityQueue<>(
    (a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]));
for (int i = 0; i < k; i++) pq.add(new long[]{0, i});
for (String s : strings) {
    long[] top = pq.poll();
    lines.get((int) top[1]).add(s);
    top[0] += s.length();
    pq.add(top);
}
```

O(n log k).

### Traps

- The tie-break must be the smallest **index**, not whichever line the heap happens to
  return. A `PriorityQueue` is not stable — you must put the index in the comparator.
- k can exceed the number of strings. Empty lines must still appear in the output with
  load 0.
- Part 1 is deliberately underspecified ("any of k lines"). It is the warm-up; do the
  simplest round-robin and move on. Do not over-engineer it.

### Paywalled

Sample I/O, test cases, solution, round type, frequency, date asked.

---

## 4. String validation with security constraints

Source: `bb2f32f5-f19a-4151-be47-412833e0e4a6`
Relevant to the Security / IAM role. Low public detail, high signal opportunity.

### Statement (public portion, verbatim)

> "Design a string validation system that handles security-related constraints. It should
> prevent maliciously constructed inputs from bypassing validation and maintain performance
> under abnormal traffic."

### Requirements

- Handle both valid and invalid input types.
- Maintain consistent performance under high concurrency.
- Interviewers may ask how you prevent security vulnerabilities.

### How to attack it

The public text is thin, so the round is open-ended. Two named concerns — bypass and
performance under abnormal traffic — tell you what to cover.

**Validation bypass.** Name the specific classes, do not speak generally:
- Unicode normalization. Validate **after** NFKC normalization, never before, or
  homoglyphs and full-width variants slip through.
- Null-byte and control-character truncation.
- Over-long UTF-8 encodings and surrogate abuse.
- Canonicalization order — decode once, then validate. Repeated decoding is how
  double-encoded payloads get through.
- Allowlist, never denylist. State this as a principle.

**Performance under abnormal traffic.** The likely intended answer is **ReDoS**:
- A backtracking regex with nested quantifiers goes exponential on a crafted input.
  `(a+)+$` against a long run of `a` is the classic.
- Fixes: a linear-time engine (RE2), a hand-written parser, an input length cap before the
  regex runs, or a match timeout.
- This is the single best thing to raise unprompted. It connects both stated concerns —
  it is simultaneously a malicious input and a performance failure.

**Concurrency.** Validators must be stateless or immutable. Note that `java.util.regex`
`Pattern` is thread-safe but `Matcher` is not — one `Matcher` per thread, or use a
`ThreadLocal`.

Also mention: fail closed, uniform error responses so validation failures do not leak
which rule fired, and bounded input size at the edge.

### Paywalled

Complete problem details, sample I/O, test cases, solution approaches, round metadata.
The public text may be all that this page offers without a membership.
