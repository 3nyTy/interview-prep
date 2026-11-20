# Design ChatGPT Enterprise (Multi-Tenant RAG)

> OpenAI Staff SWE system-design prep. Validated with the cross-LLM debate skill (3 rounds vs Codex). Codex final verdict: *"borderline-to-strong Staff-level, right architectural instincts."* Solved 2026-06-20.
>
> **Excalidraw diagram:** https://excalidraw-production.a.musta.ch#5f6d9b7c-8ed7-4804-997f-c80df3d6e6f8

## Problem
Glean-style multi-tenant enterprise RAG: retrieval-augmented Q&A over a company's documents, with strict per-document access control, low latency, and provenance.

## Key validated decisions (the differentiators)

### Authorize-before-hydrate
Stage A returns **IDs only** → authoritative ACL check → hydrate **ONLY** authorized text → rerank.
- The naive "late-binding after rerank" approach leaks revoked text to the cross-encoder / LLM / logs. Authorization must gate hydration, not just the final answer.

### The spine (Codex's #1 highest-leverage gap)
End-to-end authorization/provenance consistency model: **every derived artifact** (vector, cache entry, conversation summary, log) records the `doc-version` + `ACL-epoch` it depends on, and **fails closed when stale**.

### Reranking pipeline
- Hybrid retrieval: dense (HNSW) + sparse, combined with **RRF**.
- → pointwise **cross-encoder** (~75–100 candidates) with **ensemble abstention** (calibrated score + margin + dense∧sparse agreement — NOT global calibration).
- → deterministic **MMR** packing.
- LLM **listwise** reranking ONLY for router-detected complex queries.
- **Defer LambdaMART** (it's a feature-blender; needs click data you don't have yet).

### Multi-tenancy (hybrid by size)
- Large tenants: dedicated index + ACL-aware partitions.
- Small tenants: pooled + filter.

### Latency (split SLOs)
- TTFT p95 < 2s.
- Listwise-LLM rerank + NLI faithfulness check are **OFF the synchronous path** (async / sampled).

### Caching
- **NO cross-user answer cache.**
- Per-principal short-TTL cache keyed by ACL epoch.
- Embedding cache per-tenant.

### Conversation memory = derived data
- Re-authorize doc IDs **every turn**.
- Provenance-carrying summaries.

## Honest gaps to acknowledge in the interview
- Filtered-ANN recall at pathological ACL selectivity (~0.1%) is **reduced, not solved**.
- Provenance-preserving summarization is genuinely hard.
- Deletion proofs across logs / backups / vendor retention.

## Note
Full debate artifacts were ephemeral (`/tmp/debate-*-07f54a21.*`) and are gone. This doc is reconstructed from validated notes.
