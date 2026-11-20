# Design a ChatGPT Clone (Frontend-Focused Full-Stack)

> OpenAI Staff SWE prep — the "simple ChatGPT clone" variant: frontend-focused full-stack, 60 min, frontend-background interviewer. **Distinct from the Enterprise RAG question** — no RAG / vector DB. It's a streaming chat UI + thin stateless BFF. Solved 2026-06-21.
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#8c0eeeb4-f394-4591-a36c-253084346841
>
> **Validation note:** Codex debate ran >24 min and was killed (unavailable); fell back to adversarial Claude-subagent review (skill-sanctioned). Review found 5 real flame-out points, now incorporated.

## Core architecture
Browser SPA ↔ **stateless BFF proxy** ↔ Chat Completion API.
- BFF holds the provider key (browser **never** does), validates JWT, rate-limits, streams upstream SSE back, and **stores NOTHING**.

## The differentiators (shipped-it vs read-about-it)

### Streaming transport
- SSE via **`fetch()` + `ReadableStream`**, NOT native `EventSource`.
- Why not EventSource: GET-only (can't POST the `messages[]` body), can't set `Authorization`, and auto-reconnects with wrong semantics for non-resumable LLM generation.
- WebSockets = overkill (streaming is unidirectional after the request).

### C1 — Force-flush on terminal events
The rAF-batched token buffer MUST synchronously `cancelAnimationFrame` + flush buffer + **then** flip status, on **both** `[DONE]` and abort/error — else the last frame's tokens are lost. Also finalize on reader `done: true`, not just the `[DONE]` sentinel.

### C2 — Two-layer SSE parser
- Byte layer: `TextDecoder({ stream: true })` for split UTF-8.
- Frame layer: a **string accumulator across reads**, split on `\n\n`, handle `\r\n`, skip `:` comment/heartbeat lines, concat multiple `data:` lines.
- Per-chunk `JSON.parse` is the bug.

### C3 — Auth: header, not cookie, for `/api/chat`
- Bearer in-memory JWT = CSRF-immune.
- The **refresh** endpoint is cookie-auth = a CSRF target: `SameSite=Strict` if same registrable domain (e.g. `app.x.com` + `api.x.com` via CloudFront path routing), else origin-check + CSRF token. **State the domain topology explicitly.**

### C4 — Validate JWT at admission only
Once 200 + streaming, the BFF runs to completion (upstream auth = server-side provider key, doesn't expire mid-call). Proactive client refresh-before-send with clock-skew margin. A mid-stream 401 is **NOT** recoverable.

### S1 — Do NOT virtualize by default
`react-window` breaks: variable-height streaming messages (`resetAfterIndex` thrash at 60fps), auto-scroll-to-bottom (estimated heights), cross-message text selection + find-in-page (unmounted DOM). Ephemeral session = bounded transcript. If virtualization is truly needed, **exclude the live streaming message** from the virtualized set.

### S2 — Separate `streamingContent` slice
Keep it separate from committed `messages[]` in the store (**Zustand** for selector granularity, NOT Context which re-renders all consumers). Live tokens update one `<StreamingMessage>`; `messages[]` identity changes only once, at `[DONE]`.

### S3 — Memoize markdown parse per committed message
Defer code-block syntax highlighting until the fence closes (avoid O(n²) re-highlight of an unterminated block at 60fps).

### S4 — Retry = fresh non-idempotent generation
It bills again and produces different output. Remove the incomplete assistant turn from **both** display and outgoing `messages[]`; re-POST through the last user turn. Cost guard.

### M6 — Markdown XSS → in-memory token theft
Render model output via `react-markdown` (React tree, not `innerHTML`); if using `rehype-raw` then you **MUST** add `rehype-sanitize`. This is why "token in memory = safe" is incomplete.

### AWS
- SPA on S3 + CloudFront.
- BFF on **Lambda response streaming** (Function URL, `InvokeMode RESPONSE_STREAM`) or Fargate — **NOT** classic API Gateway REST (buffers, 29s timeout, breaks SSE).
- Cognito, Secrets Manager, WAF.
- Logs = metadata only, never message content; disable provider training/retention.

### Storage
- In-memory by default (refresh = new chat, per requirement).
- **Never** localStorage for messages/tokens.
- sessionStorage only as an off-by-default "survive accidental refresh" tradeoff.

## The 3 questions that break candidates
1. Byte-by-byte SSE parse with UTF-8 split across reads + frame boundary mid-buffer + abort-during-final-read.
2. Which components re-render per rAF flush under virtualization + what breaks scroll/selection.
3. Header-vs-cookie auth on `/api/chat` + CSRF on the expensive endpoint + markdown XSS stealing the in-memory token.
