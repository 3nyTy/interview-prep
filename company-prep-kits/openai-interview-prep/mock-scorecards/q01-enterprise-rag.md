# Mock Scorecard — Q1: Enterprise RAG

**Date:** 2026-07-11 · **Mode:** B (live mock) · **Full write-up:** [`../detailed/01-chatgpt-enterprise-rag.md`](../detailed/01-chatgpt-enterprise-rag.md)

**Verdict:** Strong bones and an above-average caching instinct — but the two ideas that *define* this problem (authorize-before-hydrate, and the version+ACL-epoch "spine") came out under questioning rather than up front. Say those two early and unprompted and I'm at the Staff bar.

Legend: ✅ identified myself · ⚠️ partial / with a nudge · ❌ missed (fix included)

## Everything a Staff answer needs

1. ✅ **API Gateway** — auth, rate-limit, routing, LB; **SSE streaming** to client.
2. ✅ **Retrieval pipeline shape** — retrieve nearest chunks → rerank with a small model → top-N + query → LLM.
3. ✅ **Filter by permission *before* the rerank/LLM** (correct relative order — most people rerank first, which leaks).
4. ❌ **Authorize BEFORE hydrate** — *fix:* search must return **IDs + metadata only**; load (hydrate) the text of **only the authorized IDs** afterward. I returned chunk text up front, so unauthorized text entered the service before the permission check (leaks via logs/caches/reranker).
5. ❌ **The "spine"** — *fix:* stamp every derived artifact (index entry, cached answer, summary) with **`doc_version` + `acl_epoch`**; on reuse, compare to current and **fail closed** if stale. This is how a revocation/edit propagates through a pipeline full of copies.
6. ❌ **Hybrid retrieval** — *fix:* run **dense (vector/HNSW) + sparse (keyword/BM25) in parallel**, merge with **Reciprocal Rank Fusion (RRF)**. I defaulted to pure vector; enterprise users search by exact error code / ticket ID / person name, which dense search blurs.
7. ⚠️ **Reranker + packing** — I had the cross-encoder rerank; *fix:* add **MMR packing** (relevant *and* diverse) so the context window isn't filled with near-duplicate chunks.
8. ✅ **Caching instinct (above average)** — cache answer *with* its source chunk/doc IDs and **re-validate access on read** (better than the rote "never cross-user cache").
9. ❌ **Cache staleness guard** — *fix:* access-check alone is insufficient; also verify each source's **`doc_version` + `acl_epoch`** still matches (else you serve week-old, since-redacted content to someone who now has access). Cross-user reuse is only safe if the answer is a pure function of *(query + chunks)* with no user-specific context.
10. ✅ **Multi-tenancy** — tenant filter on every query; dedicated index for big tenants, pooled for small; scale nodes for the large tenant. *Add:* bind `tenant_id` into auth so a missing filter can't leak (defense-in-depth); note noisy-neighbor.
11. ✅ **Ingestion** — push + pull; fan-out to vector / lexical / metadata / ACL stores; version-bump on update.
12. ⚠️ **Ingestion rigor** — *fix:* make it **async/queued, not inline** (backfills of tens of millions of docs will topple a synchronous API); name a **commit point** across the 4 stores (write the `doc_version` bump **last**, gate readers on it).
13. ✅ **Freshness split** — urgent **revocation via low-latency push**, non-urgent bulk via hourly pull. *Add:* the revoke is *effective instantly* via the **epoch bump + fail-closed**, and it's **cheap** (no re-embed) vs a content update (expensive re-embed).
14. ❌ **`acl_epoch` concept** — *fix:* it's a **version counter for the permission state** (mirror of `doc_version` for content); bump on any permission change; derived artifacts stamped with the old value fail closed. Needed teaching.
15. ⚠️ **Term precision** — I said "tokenize the query"; *fix:* for retrieval you **embed** the query into a vector (tokenizing is for the LLM).

## Lead-with-this (the 2–3 sentences that move me to Staff)
- *"Search returns IDs; I authorize the IDs; then I hydrate the text of only what's authorized — filtering at the end means revoked text already passed through my reranker, LLM, and logs."*
- *"Every derived artifact carries the `doc_version` + `acl_epoch` it was built from and fails closed when stale — that's how revocation propagates through a pipeline full of copies."*
- *"Hybrid dense+sparse with RRF — dense catches paraphrase, sparse catches exact codes/IDs/names."*

## Pattern to drill
The whole problem is **authorization across derived data**. Default to *authorize-before-hydrate* + *the spine* the moment permissions enter any design.
