# ChatGPT Enterprise RAG — Detailed Explainer (plain English)

🔗 Diagram: https://excalidraw-production.a.musta.ch#5f6d9b7c-8ed7-4804-997f-c80df3d6e6f8

## What we're building
An internal "ask anything about our company's stuff" assistant (like Glean). You ask a question; it searches the company's documents — wikis, docs, tickets, code — finds the relevant ones, and an LLM answers using them, with citations. "RAG" = Retrieval-Augmented Generation: retrieve relevant text, then generate an answer grounded in it.

The defining constraint: **strict per-document access control.** It must *never* show you, or even feed the LLM, a document you're not allowed to see. And "multi-tenant" means many companies use the same system while being completely walled off from each other.

## Why permissions make this hard
A plain RAG system is "search → stuff results into the prompt → answer." The moment you add real enterprise permissions, that pipeline becomes dangerous, because permission changes constantly (people leave teams, docs get reclassified, access is revoked) and the system has *derived copies* of document content everywhere — in the search index, in caches, in conversation summaries, in logs. Any one of those can leak content the user shouldn't see. The whole design is really about **getting authorization right across a pipeline full of derived data.**

## The core idea: authorize BEFORE you load the text
Think of retrieval in three steps, and the *order* is the whole point:

1. **Search returns IDs only.** The first-stage search (over vectors + keywords) returns a list of *document/chunk IDs* — not their text yet.
2. **Check permissions on those IDs** against the authoritative access-control system. Throw out anything this user can't see.
3. **Hydrate (load the actual text) of only the authorized IDs**, then rerank and feed to the LLM.

The naive ordering — load all the text, rerank, *then* filter by permission — is a leak: the secret text has already passed through the reranker, possibly the LLM, and your logs before you filtered it. **Authorize first, hydrate second.** This single ordering decision is the most important thing to say.

## The "spine": every derived thing remembers what it came from
This was the highest-leverage idea in the design. Every piece of derived data records two things:
- the **document version** it was built from, and
- the **access-control epoch** (a version number for the permission state) it was built under.

That includes: each search vector, each cached answer, each conversation summary, each log entry. When the system uses any of them, it checks those stamps against the current truth. If the document changed or permissions were revoked since (the stamp is stale), the system **fails closed** — it refuses to use the stale artifact rather than risk serving revoked content. This "spine" of version + ACL-epoch stamps running through every component is what makes the whole pipeline trustworthy. (It's also the part a sharp reviewer will say is missing if you don't mention it.)

## The retrieval & ranking pipeline (cheap → smart → expensive)
You don't run expensive models on everything. You funnel:

1. **Hybrid retrieval:** run a **dense** vector search (semantic similarity, via an HNSW index) *and* a **sparse** keyword search in parallel, then merge the two ranked lists with **Reciprocal Rank Fusion (RRF)**. Dense catches "means the same thing"; sparse catches exact terms, names, error codes. Together they beat either alone.
2. **Authorize + hydrate** the merged candidates (as above).
3. **Cross-encoder rerank** the ~75–100 survivors: a slower model that reads the query and each document *together* and scores relevance precisely. It uses **ensemble abstention** — it only trusts a result when a calibrated score, a margin check, and dense-and-sparse agreement line up (rather than a single global threshold).
4. **MMR packing:** assemble the final context choosing results that are relevant *and diverse* (Maximal Marginal Relevance), so you don't fill the prompt with five near-duplicates.
5. **LLM answers**, streaming, with citations.
- Reserve the most expensive step — an **LLM-based listwise rerank** — only for genuinely complex queries (a lightweight router decides). And **defer** learning-to-rank (LambdaMART) until you have click data; don't pretend you have it on day one.

## Multi-tenancy (many companies, isolated) — by size
- **Big customers:** their own dedicated search index, with access-control-aware partitions.
- **Small customers:** share a pooled index, separated by a strict tenant filter on every query.
This balances isolation against the cost of running a dedicated index for a tiny tenant.

## Caching (the careful part)
- **No cross-user answer cache.** Two users asking the same question may have different document access, so reusing one's answer for another can leak content. Never do it.
- Cache **per-person, short-lived, and tagged with their access-control epoch** — so a permission change invalidates it.
- The **embedding cache** (vectors for documents) is per-tenant.

## Latency
- **Stream the first token fast** — target first-token under ~2 seconds.
- Push the slow, thorough safety checks **off the synchronous path**: the LLM-based listwise rerank and an NLI-based "is this answer actually faithful to the sources?" check run asynchronously or on sampled traffic, not in the user's wait.

## Conversation memory is "derived data" too
Follow-up questions reference earlier turns. But access can be revoked *between* turns. So we **re-authorize the referenced document IDs on every turn**, and conversation summaries carry their provenance (which docs, which versions) so they're subject to the same fail-closed checks.

## Index / data model (this design is pipeline-heavy, not schema-heavy)
```
Vector index — one entry per chunk:
  chunk_id, doc_id, tenant_id,
  embedding (vector),          # dense semantic search (HNSW)
  doc_version,                 # part of the "spine"
  acl_epoch,                   # part of the "spine"
  text_ref                     # pointer only — text hydrated AFTER authorization

ACL store (authoritative source of permission truth):
  (tenant_id, doc_id) → allowed principals/groups, acl_epoch
  → checked against the ID list BEFORE any text is loaded

Caches (all spine-stamped, fail-closed on staleness):
  per-principal answer cache (short TTL, keyed by acl_epoch)   # never cross-user
  per-tenant embedding cache
```

## Honest gaps to acknowledge (Staff-level maturity)
- When permissions are *very* selective (say only 0.1% of documents pass the filter), filtered approximate-nearest-neighbor search loses recall — you mitigate it, you don't fully solve it.
- Provenance-preserving summarization (keeping per-source attribution through a summary) is genuinely hard.
- True deletion across logs, backups, and the model vendor's retention is hard to *prove*.

Naming these unprompted signals you understand the system's real limits — which is exactly what a Staff interviewer is listening for.

## If the interviewer pushes further
- *"Why return IDs first instead of just filtering at the end?"* Because filtering at the end means revoked text already passed through the reranker, LLM, and logs. Authorize-before-hydrate is the only leak-free order.
- *"How does a permission revocation take effect everywhere immediately?"* The spine: every derived artifact is stamped with an ACL-epoch and fails closed when stale, so a bumped epoch invalidates caches/summaries/vectors that depended on the old permission.
- *"Can you cache answers to save cost?"* Per-user, short-TTL, epoch-keyed only — never across users, because their document access differs.
