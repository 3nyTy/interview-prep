# Frontend and mobile — 4 problems

Skip this file unless the loop includes a frontend or mobile round. The listing marks the
React and DOM problems as **low frequency**, except the DOM/async one, which is medium and
has the most recent recorded date in the whole bank (**2026-08-03**).

Relevant history: the Discord and Slack applications both stalled on a required deep React
background. If Robinhood's loop for this role has no frontend round, spend the time on
`01` and `04` instead.

---

## 1. Design a Chainable Async DOM Rectangle API

Source: `1f66d0df-ab49-54b8-820a-d9bb42224e55`
Frequency: medium | **Last asked 2026-08-03** — the most recent date in the bank
Tags: `javascript, dom, async, frontend`

### Statement

Build a JavaScript class `RectanglesClass` that manages a 3x3 grid of DOM rectangles (each
with a `data-rect-id` attribute). The API must support method chaining with asynchronous
operations.

### Required methods

| Method | Behaviour |
|---|---|
| `selectById(id)` | Target a rectangle by its data attribute |
| `color(cssColor)` | Set the background colour |
| `afterDelay(ms)` | Defer subsequent operations |
| `shiftByPx(px)` | Move the rectangle rightward — accumulates on `style.left` |
| `done()` | Await completion of all queued work |

### Key requirements

- "Operations in a single chain must execute strictly in the order in which they were
  written."
- Vanilla JavaScript only. No libraries.
- No animation is needed. Delays only defer execution.
- A safe no-op if no element is selected.

### Example

```javascript
const api = new RectanglesClass(document);
api.selectById(13).color("red").afterDelay(500).shiftByPx(10).color("green");
await api.done();
```

Expected outcome: the rectangle turns green with a 10px rightward shift.

### Approach

The chain is **synchronous** — every method returns `this` immediately — but execution is
**deferred**. You cannot `await` inside the chain, because the chain has already fully
evaluated by the time the first delay resolves.

Queue thunks, then drain them serially:

```javascript
class RectanglesClass {
  constructor(doc) { this.doc = doc; this.el = null; this.queue = Promise.resolve(); }

  #enqueue(fn) { this.queue = this.queue.then(fn); return this; }

  selectById(id) {
    return this.#enqueue(() => {
      this.el = this.doc.querySelector(`[data-rect-id="${id}"]`);
    });
  }
  color(c)       { return this.#enqueue(() => { if (this.el) this.el.style.backgroundColor = c; }); }
  afterDelay(ms) { return this.#enqueue(() => new Promise(r => setTimeout(r, ms))); }
  shiftByPx(px)  { return this.#enqueue(() => {
                     if (!this.el) return;
                     const cur = parseFloat(this.el.style.left) || 0;
                     this.el.style.left = `${cur + px}px`;
                   }); }
  done()         { return this.queue; }
}
```

Chaining `.then` onto a single promise is what guarantees strict ordering. Returning a
promise from a `then` callback makes the chain wait for it — that is how `afterDelay` works
without any extra machinery.

### Traps

- **Do not use `async` methods that return promises** for the chained calls. `selectById`
  must return `this`, not a promise, or `.color()` is not callable on the result.
- `shiftByPx` must accumulate. Read the current `style.left` and add. Note that
  `style.left` is empty on first read, so `parseFloat` gives `NaN` — hence `|| 0`.
- The no-op guard when `this.el` is null must be inside the deferred function, not at
  enqueue time. Selection happens later.
- `shiftByPx` needs the element positioned (`position: absolute` or `relative`) for
  `left` to have any effect. Mention it.

### Paywalled

Difficulty, duration, full tags, test cases, sample I/O, round metadata, frequency stats.

---

## 2. Build a Weekly Calendar Grid UI (7x24) With Event Creation Dialog

Source: `04cf484b-34b0-4ffa-a786-5a0f26769986`
Frequency: low

### Statement

Implement a web calendar component showing a 7-day week across 24 hourly slots as a grid.
Clicking a cell starts an event-creation workflow.

### Requirements

**UI and interaction**
- 7 rows (days) x 24 columns (hourly slots)
- A cell click opens a modal dialog
- The user enters an event name and confirms
- The event name appears in the matching cell

**State management** — this is the graded part
- Design the internal state structure for storing events.
- Avoid duplicate sources of truth. Do **not** keep a separate "grid state" and
  "events state".
- Explain how the renderable grid is derived from the event data, possibly with
  memoization.

**Scope** — create and display only. Edit, delete, multi-hour events, and drag-and-drop are
optional.

### Approach

The stated requirement is single source of truth. Keep events in a flat collection and
derive the grid:

```javascript
// source of truth
const events = [{ id, day, hour, name }];

// derived, memoized
const grid = useMemo(() => {
  const g = Array.from({ length: 7 }, () => Array(24).fill(null));
  for (const e of events) g[e.day][e.hour] = e;
  return g;
}, [events]);
```

Keying events by `` `${day}-${hour}` `` in a `Map` is also defensible and makes lookup O(1)
without building the grid at all — mention both and pick one.

Say the words "derived state" and "single source of truth". The requirement is written to
test whether you reach for them.

### Traps

- 7x24 = 168 cells. Re-rendering all of them on every keystroke in the modal is the
  performance failure. Keep the modal's input state local to the modal.
- Note the axis order: 7 **rows** and 24 **columns**, which is the transpose of how Google
  Calendar looks. Follow the spec, not the mental image.
- Do not store a `grid` in state alongside `events`. That is the exact anti-pattern named.

### Paywalled

Complete details, sample I/O, test cases, Python/Java stubs, solution verification.

---

## 3. Timer Management App

Source: `925ae70d-d4ce-4b4c-9716-87231080d9f4`

### Statement (verbatim)

> "Design and implement a one-page application with a table view containing 50 rows. Each
> row represents a timer. When scrolling down, a new timer starts and the previous one
> stops. Clicking on a timer row will stop the timer. Optimize the application if time
> permits."

### Test cases (verbatim)

- "Scroll to the bottom to ensure the new timer starts and the previous timer stops."
- "Click on any row to ensure that the timer for that row stops."
- "Ensure the performance is not significantly affected with 50 timers running."

### Approach

The third test case is the real question. 50 independent `setInterval` calls each triggering
a state update means 50 re-renders per tick.

The optimization: **one interval, not 50.** A single `setInterval` ticks a shared clock; each
row derives its own elapsed time from `now - startedAt`. Rows become pure functions of the
tick.

```javascript
// one timer for the whole page
useEffect(() => {
  const id = setInterval(() => setNow(Date.now()), 1000);
  return () => clearInterval(id);
}, []);
// each row: elapsed = (row.stoppedAt ?? now) - row.startedAt
```

Store `startedAt` and `stoppedAt` timestamps, never an incrementing counter. A counter
drifts, and it breaks if the tab is backgrounded — browsers throttle timers in inactive
tabs. Timestamp subtraction is immune to both.

Scroll detection: `IntersectionObserver`, not a scroll handler with `getBoundingClientRect`.
The observer does not run on the main thread per scroll event.

### Traps

- Clean up the interval on unmount, or it leaks.
- The scroll rule says a new timer starts **and** the previous stops — one active timer at a
  time, not accumulating.
- Timer drift with `setInterval` is real. Derive from timestamps.
- Row rendering should be memoized so a tick does not re-render stopped rows.

### Paywalled

Difficulty, duration, tags, I/O spec, constraints, expected outputs, follow-ups, round
metadata, code execution.

---

## 4. Implement Android findViewById

Source: `20f0926e-8b1b-4160-b794-3e5c1f012212`
Android. Skip unless the loop is mobile.

### Statement (verbatim)

> "Implement the `findViewById(int id)` method in Android. Suppose you have a simple View
> hierarchy where each View has a unique ID. You need to find and return the corresponding
> View for a given ID. Describe the implementation process and provide unit tests to verify
> the correctness of the implementation."

### Approach

A tree search. `ViewGroup` has children, `View` does not.

```java
View findViewById(View root, int id) {
    if (root == null) return null;
    if (root.getId() == id) return root;
    if (!(root instanceof ViewGroup)) return null;
    ViewGroup g = (ViewGroup) root;
    for (int i = 0; i < g.getChildCount(); i++) {
        View found = findViewById(g.getChildAt(i), id);
        if (found != null) return found;
    }
    return null;
}
```

The problem explicitly asks for **unit tests**, so half the answer is the test list:
- id on the root itself
- id on a deep leaf
- id not present → returns null
- null root → returns null
- empty `ViewGroup`
- a leaf `View` that is not a `ViewGroup`
- duplicate ids → the documented behaviour is first match in depth-first order

### Traps

- Deep hierarchies and recursion. Offer the iterative version with an explicit `Deque` when
  asked about depth limits.
- Depth-first pre-order is the real Android behaviour. State the traversal order — with
  duplicate ids the order is the answer.
- The problem says unique ids, but a test for duplicates shows you thought about it.

### Paywalled

Everything except the statement above — details, sample I/O, test cases, constraints,
follow-ups, solution, round metadata.
