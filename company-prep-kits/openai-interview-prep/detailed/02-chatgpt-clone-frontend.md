# ChatGPT Clone (Frontend-Focused) — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#8c0eeeb4-f394-4591-a36c-253084346841

## What we're building
The actual chat *website* — the message box, and the answer streaming in word-by-word — plus a thin backend that holds the secret model API key and forwards your message to the LLM. This is the **frontend-heavy** variant of the interview (a frontend-background interviewer, ~60 min): no RAG, no vector database, no document permissions. The whole game is the streaming UI and a clean, secure proxy. The backend stores **nothing**; refresh = a brand-new chat.

## The overall shape
```
Browser SPA  <-->  stateless BFF proxy  <-->  Chat Completion API
```
- **BFF** = "Backend For Frontend," a thin server that exists only to serve this UI.
- The BFF holds the provider API key (the browser **never** sees it), validates your login token, rate-limits you, streams the model's response back, and stores nothing.

## Why this is harder than it looks
"Just call the API and show the text" hides a pile of sharp edges, and at a Staff bar the interviewer is hunting for whether you've actually *shipped* streaming UI or only read about it. The depth is in: how tokens arrive over the wire, how you render them at 60fps without dropping any, how you authenticate an expensive streaming call, and how you avoid XSS in model output. Let me walk through each.

## Streaming transport: use `fetch()` + a stream reader, NOT `EventSource`
The model streams tokens as Server-Sent Events (SSE). The browser *has* a built-in SSE client called `EventSource` — and it's the wrong tool here:
- `EventSource` is **GET-only** — you can't POST the `messages[]` conversation body.
- It **can't set an `Authorization` header** — so you can't attach your bearer token.
- It **auto-reconnects** with semantics that are wrong for a non-resumable LLM generation (it would silently re-trigger billing and produce a different answer).

So instead you use `fetch()` with a `ReadableStream` reader: POST the body, set the header, and read the response stream chunk by chunk. (WebSockets would be overkill — after you send the request, data only flows one way, server→client.)

## C1 — Don't lose the last words (force-flush on terminal events)
For smooth rendering you batch incoming tokens and paint them once per animation frame (`requestAnimationFrame`). The bug: when the stream ends or errors, if you flip the UI to "done" *before* flushing the buffer, the last frame's tokens vanish. So on **both** the `[DONE]` sentinel **and** an abort/error, you must synchronously `cancelAnimationFrame`, flush the buffer, and *then* mark complete. Also finalize when the reader says `done: true`, not only on the `[DONE]` text — networks don't always deliver the sentinel.

## C2 — Parse the stream in two layers
Bytes arrive in arbitrary chunks. Two things break naive parsers:
- **A multi-byte character can split across two chunks** → decode with `TextDecoder({ stream: true })` so a half-character is held until its rest arrives.
- **An SSE message boundary can split across chunks** → accumulate decoded text into a **string buffer**, split on the blank-line delimiter (`\n\n`), handle `\r\n`, skip `:` comment/heartbeat lines, and concatenate multiple `data:` lines in one event.

The classic bug is calling `JSON.parse` on each raw network chunk — that fails the moment a chunk doesn't align with a message boundary. Parse the *frames*, not the chunks.

## C3 — Auth: header for the chat call, protect the refresh endpoint
- The chat call (`/api/chat`) carries the login token as a **`Bearer` header**, kept **in memory** (a JS variable, not a cookie). A header token is **immune to CSRF** because another site can't make the browser attach it.
- The token-**refresh** endpoint *does* use a cookie (refresh tokens shouldn't live in JS), so *that's* the CSRF target. Protect it: `SameSite=Strict` if the app and API share a registrable domain (e.g. `app.x.com` + `api.x.com` behind CloudFront path routing), otherwise an origin check + CSRF token. **State your domain topology explicitly** — the right answer depends on it.

## C4 — Validate the token at the start, then let the stream finish
Check the token at **admission** (when the request begins). Once you've returned 200 and started streaming, let it run to completion — the BFF's own auth to the model is a server-side provider key that doesn't expire mid-call. A token expiring mid-stream is **not** recoverable, so the client should proactively refresh *before* sending, with a clock-skew margin.

## S1 — Do NOT virtualize the message list by default
Virtualization libraries (react-window) only render visible rows. For a chat they break three things:
- **Variable-height streaming bubbles** → constant `resetAfterIndex` thrash at 60fps.
- **Auto-scroll-to-bottom** → relies on estimated heights it doesn't have.
- **Text selection across messages + find-in-page** → unmounted DOM isn't selectable or searchable.

The session is ephemeral and the transcript is bounded, so just render it. If you *must* virtualize a huge history, **exclude the live streaming message** from the virtualized set.

## S2 — Keep live tokens in a separate store slice
Put the in-progress streaming text in its own slice, separate from the committed `messages[]` array. Use **Zustand** (fine-grained selectors) rather than React Context (which re-renders every consumer on any change). Then each token re-renders only the one `<StreamingMessage>`; the committed list changes identity exactly once, at `[DONE]`. This is the difference between a smooth stream and the whole page janking on every token.

## S3 — Memoize markdown, defer highlighting
Re-parsing the full markdown of the growing message every token is O(n²). Memoize the parse per committed message, and **defer code-block syntax highlighting until the closing fence arrives** — otherwise you re-highlight an unterminated block on every token.

## S4 — Retry = a fresh, non-idempotent generation
Regenerating bills again and produces different text. So on retry: remove the incomplete assistant turn from **both** the display **and** the outgoing `messages[]`, then re-POST through the last user turn. Treat it as new work, with a cost guard — not a free "resume."

## M6 — Markdown XSS can steal the in-memory token
Since the token lives in memory (C3), people assume it's safe. It isn't if you render model output as raw HTML — injected markup could exfiltrate it. Render via `react-markdown` (a React element tree, not `innerHTML`). If you enable raw HTML (`rehype-raw`), you **must** also sanitize (`rehype-sanitize`). "Token in memory = safe" is incomplete without this.

## AWS / infra
- **SPA on S3 + CloudFront.**
- **BFF on Lambda response-streaming** (Function URL, `InvokeMode = RESPONSE_STREAM`) or Fargate — **NOT** classic API Gateway REST, which buffers the response and has a 29-second timeout that breaks streaming.
- Cognito (auth), Secrets Manager (the provider key), WAF (rate-limit/abuse).
- **Logs are metadata only — never message content**, and disable provider-side training/retention.

## Storage
- **In-memory by default** — refresh starts a new chat, per the requirement.
- **Never** localStorage for messages or tokens.
- sessionStorage offered only as an off-by-default "survive an accidental refresh" tradeoff.

## The 3 questions that separate candidates
1. Byte-by-byte SSE parsing with a UTF-8 char split across reads + a frame boundary mid-buffer + an abort during the final read.
2. Which components re-render per animation-frame flush under virtualization, and what that breaks (scroll/selection).
3. Header-vs-cookie auth on `/api/chat` + CSRF on the expensive endpoint + markdown XSS stealing the in-memory token.

## Database schema
**None — stateless by design.** The BFF persists nothing; the browser holds state in memory and a refresh is a fresh chat. The only "schema" decisions are client-side store *shape* (committed `messages[]` vs. a separate live `streamingContent` slice), which is S2 above.

## If the interviewer pushes further
- *"Why not EventSource? It's literally the SSE API."* It's GET-only, can't send the messages body or an auth header, and auto-reconnects with wrong semantics for a billable non-resumable generation. `fetch()` + ReadableStream gives you POST, headers, and explicit control.
- *"Where's the SSE parsing bug people make?"* `JSON.parse` per network chunk. You must decode bytes with streaming on, accumulate into a string, and split on `\n\n` frame boundaries.
- *"The token's in memory, so XSS can't hurt it, right?"* Wrong if you render model output as raw HTML — sanitize markdown, or the injected script reads your in-memory token.
