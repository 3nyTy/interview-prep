# Cheat Sheet — ChatGPT Enterprise (Multi-Tenant RAG)

🔗 Diagram: https://excalidraw-production.a.musta.ch#5f6d9b7c-8ed7-4804-997f-c80df3d6e6f8

## 1. Summary (in plain terms)
An internal "ask anything about our company's documents" assistant. It searches the company's files, finds the relevant ones, and feeds them to the LLM to answer. The catch: it must **never** show or use a document the asker isn't allowed to see. "Multi-tenant" = many companies share the system but are strictly walled off from each other.

## 2. Key deep dives (plain terms)

- **Authorize BEFORE you load the text ("authorize-before-hydrate").**
  Step 1: search returns just a list of document *IDs*. Step 2: check "is this user allowed to see each one?" Step 3: fetch the actual words of *only* the allowed ones, then rank.
  The naive way (fetch text first, filter later) leaks secret text into the reranker, the LLM, and the logs. Permission check comes first.

- **The "spine" = everything remembers what it was built from.**
  Every derived thing — a search vector, a cached answer, a conversation summary, a log line — records *which version of the doc* and *which permission-snapshot* it came from. If that's stale (doc edited, or access revoked), the system refuses to use it ("fail closed"). This stops revoked content from sneaking back via an old cache or summary. (This was the single biggest gap the review caught.)

- **Ranking is cheap → smart → expensive, in that order.**
  Cheap hybrid search first (dense + keyword, merged) → a slower cross-encoder scores ~100 candidates → pick a *diverse* final set (MMR). Only use the expensive LLM-based ranking for genuinely complex questions.

- **Multi-tenancy by size.** Big customers get their own index; small ones share one index with a tenant filter.

- **Caching:** never reuse one user's answer for another (their permissions differ). Cache per-person, short-lived, tagged with their permission snapshot. Embedding cache is per-tenant.

- **Latency:** stream the first token fast (<2s); run slow safety checks (faithfulness/NLI) in the background, not in the user's wait.

- **Conversation memory is "derived data":** re-check permissions on the referenced docs *every turn* — access may have been revoked since.

- **Be honest about gaps:** when very few docs pass a permission filter, search quality genuinely drops; deleting data everywhere (logs, backups, the model vendor) is hard.

## 3. Data / index model (light — this design is pipeline-heavy, not schema-heavy)
```
Chunk/vector document (in the vector index, one per chunk):
  chunk_id, doc_id, tenant_id,
  embedding (vector),
  doc_version,        # part of the "spine"
  acl_epoch,          # part of the "spine"
  text_ref            # pointer; text hydrated only AFTER authz

ACL store (authoritative):
  (tenant_id, doc_id) -> allowed principals / groups, acl_epoch
  → checked on the ID list BEFORE hydrating text
```
