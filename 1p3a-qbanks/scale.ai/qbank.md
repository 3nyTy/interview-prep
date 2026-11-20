# Scale AI — 1point3acres Question Bank (9 curated questions)

Source: 1point3acres qbank (`company=scale.ai`), scraped 2026-09-20 from the logged-in session via AppleScript-driven Chrome. Company page: <https://www.1point3acres.com/interview/problems/company/scale.ai>

The site lists 44 entries for this company: **9 curated questions** (below), 35 OJ links, 0 external links. Only the curated 9 are captured here.

> Scale AI's recent engineering loops are practical and implementation-heavy: candidates are expected to write working code under shared-screen conditions, reason about production readiness, and explain prior ML or product work clearly. MLE loops add NumPy-level model implementation, LLM training theory, and paper or project trade-off discussions, while FDE and enterprise roles lean toward customer-facing AI systems.

Question statements only. The site's answer body for each question is not reproduced here; the full page is archived verbatim under `raw/`.

## Index

1. Task Processor: Dependencies and Deadlines
2. LLM API Practical and Output Validation
3. Party Time Blocks
4. Neuron Firing Cellular Automata
5. Existing Codebase Debugging Round
6. NumPy Top-p Sampling and Multi-Head Attention
7. Credo Behavioral and Customer Engagement
8. Project Deep Dive and Paper Presentation
9. LLM Post-Training Theory Conversation

---

## 1. Task Processor: Dependencies and Deadlines

- **category**: coding
- **duration**: 60 min
- **frequency**: High
- **last asked**: 2026-08-14
- **stage**: phone-screen · tech-screen
- **access**: Free preview
- **roles**: SWE, MLE
- **tags**: data-structure, heap, topological-sort, scheduling, medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/task-processor-dependencies-and-deadlines
- **related discussions**: 10

### Statement

Implement a task processor with `AddTask` and `ConsumeTask`. Start by returning the unconsumed task with the earliest deadline, then add subtask dependencies so a parent can only be consumed after all subtasks, and finally support deadline updates for tasks that have not already been consumed.

---

## 2. LLM API Practical and Output Validation

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-08-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: llm-api, api-integration, evaluation, testing, backend
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/llm-api-practical-and-output-validation
- **related discussions**: 4

### Statement

Build a small backend or notebook function that calls an LLM API, writes a prompt for a given scenario, parses the output, and validates whether the model response satisfies the task. The API key may ...

---

## 3. Party Time Blocks

- **category**: coding
- **duration**: 60 min
- **frequency**: Medium
- **last asked**: 2026-03-29
- **stage**: phone-screen · tech-screen
- **access**: Members
- **roles**: swe
- **tags**: interval, sorting, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/party-time-blocks
- **related discussions**: 4

### Statement

Given party intervals by neighborhood, compute party-time blocks and no-party gaps. The prompt emphasizes interval sorting, timestamp parsing, output-format precision, and production test design.

---

## 4. Neuron Firing Cellular Automata

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe
- **tags**: grid, simulation, medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/neuron-firing-cellular-automata
- **related discussions**: 2

### Statement

Update a matrix of neurons using rules similar to Game of Life, but with Scale-specific firing and decrement rules. The first part permits a deep copy; the follow-up asks for improved space complexity...

---

## 5. Existing Codebase Debugging Round

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-05-01
- **stage**: onsite-coding
- **access**: Members
- **roles**: swe, mle
- **tags**: debugging, testing, medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/existing-codebase-debugging-round
- **related discussions**: 3

### Statement

Debug an unfamiliar existing codebase under time pressure. The task is less about inventing a new algorithm and more about navigating files, running tests, identifying a few small defects, and communi...

---

## 6. NumPy Top-p Sampling and Multi-Head Attention

- **category**: coding
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-25
- **stage**: phone-screen · onsite-coding
- **access**: Members
- **roles**: mle
- **tags**: numpy, sampling, attention, transformer, medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/numpy-top-p-sampling-and-multi-head-attention
- **related discussions**: 2

### Statement

In a Google Colab-style MLE phone screen, implement top-p sampling and attention primitives in NumPy using provided starter code and tests. Syntax lookup can be allowed, but direct-answer AI assistant...

---

## 7. Credo Behavioral and Customer Engagement

- **category**: behavioral
- **duration**: 45 min
- **frequency**: High
- **last asked**: 2026-08-01
- **stage**: recruiter-screen · phone-screen · onsite-bq · final-round
- **access**: Members
- **roles**: all-roles
- **tags**: medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/credo-behavioral-and-customer-engagement
- **related discussions**: 9

### Statement

Scale AI behavioral evaluation spans credo, HM, recruiter, and customer-engagement conversations. Expect standard project and teamwork questions plus sharper probes on manager relationship, self-aware...

---

## 8. Project Deep Dive and Paper Presentation

- **category**: other
- **duration**: 45 min
- **frequency**: Medium
- **last asked**: 2026-08-01
- **stage**: recruiter-screen · phone-screen · onsite-deep-dive
- **access**: Members
- **roles**: mle, swe
- **tags**: ml-knowledge, system-design, domain-knowledge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/project-deep-dive-and-paper-presentation
- **related discussions**: 7

### Statement

Discuss a prior project, paper, or research-style artifact in enough depth to justify technical choices and fit with the target team. MLE loops can use a formal presentation; HM screens use a conversa...

---

## 9. LLM Post-Training Theory Conversation

- **category**: other
- **duration**: 60 min
- **frequency**: Low
- **last asked**: 2026-03-25
- **stage**: onsite-deep-dive
- **access**: Members
- **roles**: mle
- **tags**: ml-knowledge, transformer, domain-knowledge, medium
- **url**: https://www.1point3acres.com/interview/problems/company/scale.ai/llm-post-training-theory-conversation
- **related discussions**: 2

### Statement

Verbal MLE round covering LLM and post-training fundamentals, from regularization to tokenization and preference-optimization methods. No coding or whiteboarding is required in the reported format.

---
