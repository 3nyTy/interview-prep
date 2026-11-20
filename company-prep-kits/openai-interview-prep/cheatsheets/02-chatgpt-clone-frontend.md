# Cheat Sheet — ChatGPT Clone (Frontend-Focused)

🔗 Diagram: https://excalidraw-production.a.musta.ch#8c0eeeb4-f394-4591-a36c-253084346841

## 1. Summary (in plain terms)
Build the actual chat *website* — the input box and the answer appearing word-by-word — plus a thin backend ("BFF") that holds the secret model API key and forwards your message to the model. The backend stores nothing; refresh = new chat. This is the **frontend-heavy** variant: no RAG, no vector DB.

## 2. Key deep dives (plain terms)

- **Streaming transport:** use `fetch()` + a stream reader, **not** the browser's built-in `EventSource`. EventSource can only do GET, can't POST your message list, and can't send a login token. WebSockets are overkill (data only flows one way after you send).

- **Don't lose the last words (force-flush):** tokens are batched for smooth rendering. When the stream ends or errors, you must *immediately* flush the buffered tokens **before** flipping the UI to "done" — otherwise the final frame's words vanish.

- **Parse the stream in two layers:** bytes arrive in arbitrary chunks; a single character or a message boundary can be split across two chunks. Decode bytes with streaming on, accumulate into a string, and split on blank lines. Don't `JSON.parse` each raw chunk — that's the classic bug.

- **Auth = header, not cookie, for the chat call.** A bearer token in memory is immune to CSRF. The *refresh* endpoint uses a cookie, so that's the one you protect (SameSite / origin check). Validate the token when the request *starts*; once it's streaming, let it finish (the server's own key to the model doesn't expire mid-call).

- **Don't virtualize the message list by default.** Libraries like react-window break variable-height streaming bubbles, auto-scroll-to-bottom, and text-selection / find-in-page. The chat is short-lived, so just render it. (If you ever must virtualize, exclude the live streaming bubble.)

- **Keep live tokens in a separate store slice** from finished messages (use **Zustand**, not React Context which re-renders everything). New tokens re-render only the one live bubble; the finished list changes identity just once, at the end.

- **Retry = a brand-new generation** (it bills again and produces different text). Remove the half-finished answer from both the screen and the outgoing message list first.

- **Render markdown safely:** go through react-markdown (a React tree, not raw HTML). If you allow raw HTML you must sanitize it — otherwise injected markup can steal the in-memory token.

- **AWS:** static site on S3 + CloudFront; the BFF on **Lambda response-streaming** or Fargate — **not** classic API Gateway REST (it buffers responses and has a 29s timeout, which breaks streaming). Log metadata only, never message text.

## 3. Database schema
**None — stateless by design.** The BFF stores nothing. Browser state is in-memory; refresh starts a fresh chat. (sessionStorage is offered only as an off-by-default "survive an accidental refresh" option; never localStorage for messages or tokens.)
