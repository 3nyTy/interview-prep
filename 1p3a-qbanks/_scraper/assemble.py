#!/usr/bin/env python3
"""Assemble per-company qbank folders from scraped records.

Writes, per company:
  ~/personal-archive/1p3a-qbanks/<slug>/questions.json
  ~/personal-archive/1p3a-qbanks/<slug>/qbank.md
  ~/personal-archive/1p3a-qbanks/<slug>/raw/<qslug>.txt
"""
import json
import pathlib
import sys

QB = pathlib.Path("/tmp/qb")
OUTROOT = pathlib.Path("/Users/akshaykumar/personal-archive/1p3a-qbanks")
SCRAPED_AT = "2026-09-20"
SITE = "https://www.1point3acres.com"


def load_records():
    plan = json.loads((QB / "plan.json").read_text())
    byslug = {c["slug"]: c for c in plan["companies"]}
    order = []
    for e in json.loads((QB / "queue.json").read_text()):
        order.append((e["company"], e["slug"]))
    recs = {}
    bad = []
    files = (sorted((QB / "res").glob("*.json"))
             + sorted((QB / "res").glob("*/*.json"))
             + sorted((QB / "res_old").glob("*.json")))
    for p in files:
        try:
            data = json.loads(p.read_text())
        except Exception as ex:
            bad.append((p.name, str(ex)))
            continue
        for r in data:
            if r.get("error"):
                bad.append((r.get("slug"), r["error"]))
                continue
            if not r.get("questionTitle"):
                bad.append((r.get("slug"), f"empty (http {r.get('httpStatus')})"))
                continue
            recs[(r.get("company"), r["slug"])] = r
    return plan, byslug, order, recs, bad


def badges_from_raw(raw, n_roles):
    """Display forms of role/tag badges, read from the raw body.

    Layout is: company / category+duration / title / overview / <badges> / Frequency
    """
    lines = [l.strip() for l in (raw or "").split("\n")]
    fi = None
    for i, l in enumerate(lines):
        if l == "Frequency":
            fi = i
            break
    if fi is None or fi < 5:
        return None
    badges = [l for l in lines[4:fi] if l]
    return badges


def build_question(r, n, company_meta):
    raw = r.get("rawBody") or ""
    roles_raw = r.get("roles") or []
    tags_raw = r.get("tags") or []

    badges = badges_from_raw(raw, len(roles_raw))
    roles_disp, tags_disp = None, None
    if badges and len(badges) == len(roles_raw) + len(tags_raw):
        roles_disp = badges[: len(roles_raw)]
        tags_disp = badges[len(roles_raw):]

    return {
        "n": n,
        "slug": r["slug"],
        "title": r.get("questionTitle"),
        "category": r.get("category"),
        "duration": r.get("duration"),
        "frequency": r.get("frequency"),
        "last_asked": r.get("lastAsked"),
        "stage": r.get("stage"),
        "access": r.get("access"),
        "roles": roles_disp or roles_raw,
        "tags": tags_disp or tags_raw,
        "roles_slug": roles_raw,
        "tags_slug": tags_raw,
        "overview": r.get("overview") or r.get("description"),
        "source_count": r.get("sourceCount"),
        "has_answer_on_site": bool(r.get("hasAnswer")),
        "url": r.get("url"),
    }


def md_question(q):
    L = []
    L.append(f"## {q['n']}. {q['title']}")
    L.append("")
    rows = [
        ("category", q.get("category")),
        ("duration", q.get("duration")),
        ("frequency", q.get("frequency")),
        ("last asked", q.get("last_asked")),
        ("stage", q.get("stage")),
        ("access", q.get("access")),
        ("roles", ", ".join(q.get("roles") or [])),
        ("tags", ", ".join(q.get("tags") or [])),
    ]
    for k, v in rows:
        if v in (None, "", []):
            continue
        L.append(f"- **{k}**: {v}")
    L.append(f"- **url**: {q.get('url')}")
    if q.get("source_count"):
        L.append(f"- **related discussions**: {q['source_count']}")
    L.append("")
    L.append("### Statement")
    L.append("")
    L.append(q.get("overview") or "_(no statement captured)_")
    L.append("")
    return "\n".join(L)


def main():
    plan, byslug, order, recs, bad = load_records()
    OUTROOT.mkdir(parents=True, exist_ok=True)

    by_company = {}
    for (comp, slug) in order:
        r = recs.get((comp, slug))
        if r is None:
            continue
        by_company.setdefault(comp, []).append(r)

    allc = json.loads(pathlib.Path("/tmp/companies.json").read_text())["items"]
    already = sum(i.get("qbank_count", 0) for i in allc
                  if i["identifier"] in set(plan.get("skipped_already_stored", [])))

    written = 0
    total_q = 0
    summary = []
    for comp, meta in sorted(byslug.items()):
        qs = by_company.get(comp, [])
        if not qs:
            continue
        d = OUTROOT / comp
        (d / "raw").mkdir(parents=True, exist_ok=True)

        questions = [build_question(r, i + 1, meta) for i, r in enumerate(qs)]
        total_q += len(questions)

        payload = {
            "company": comp,
            "display_name": meta.get("display_name"),
            "qbank_count": meta.get("qbank_count"),
            "questions_scraped": len(questions),
            "site_total_count": meta.get("total_count"),
            "oj_count": meta.get("oj_count"),
            "external_count": meta.get("external_count"),
            "description": meta.get("description"),
            "company_url": f"{SITE}/interview/problems/company/{comp}",
            "scraped_at": SCRAPED_AT,
            "source": "1point3acres qbank (logged-in session)",
            "note": "Question block only. The site's answer body is not included here; "
                    "the untouched page is kept verbatim in raw/<slug>.txt.",
            "questions": questions,
        }
        (d / "questions.json").write_text(
            json.dumps(payload, indent=2, ensure_ascii=False) + "\n", encoding="utf-8"
        )

        for r in qs:
            (d / "raw" / f"{r['slug']}.txt").write_text(
                f"===H1===\n{r.get('questionTitle')}\n"
                f"===URL===\n{r.get('url')}\n"
                f"===BODY===\n{r.get('rawBody')}\n",
                encoding="utf-8",
            )

        M = []
        M.append(f"# {meta.get('display_name')} — 1point3acres Question Bank "
                 f"({len(questions)} curated questions)")
        M.append("")
        M.append(f"Source: 1point3acres qbank (`company={comp}`), scraped {SCRAPED_AT} from the "
                 f"logged-in session via AppleScript-driven Chrome. "
                 f"Company page: <{SITE}/interview/problems/company/{comp}>")
        M.append("")
        M.append(f"The site lists {meta.get('total_count')} entries for this company: "
                 f"**{meta.get('qbank_count')} curated questions** (below), "
                 f"{meta.get('oj_count')} OJ links, {meta.get('external_count')} external links. "
                 f"Only the curated {len(questions)} are captured here.")
        M.append("")
        if meta.get("description"):
            M.append(f"> {meta['description']}")
            M.append("")
        M.append("Question statements only. The site's answer body for each question is not "
                 "reproduced here; the full page is archived verbatim under `raw/`.")
        M.append("")
        M.append("## Index")
        M.append("")
        for q in questions:
            M.append(f"{q['n']}. {q['title']}")
        M.append("")
        M.append("---")
        M.append("")
        for q in questions:
            M.append(md_question(q))
            M.append("---")
            M.append("")
        (d / "qbank.md").write_text("\n".join(M).rstrip() + "\n", encoding="utf-8")
        written += 1
        summary.append((comp, meta, len(questions)))

    R = []
    R.append("# 1point3acres Question Banks")
    R.append("")
    R.append(f"Curated interview-question banks scraped from 1point3acres on {SCRAPED_AT} "
             f"from the logged-in session (AppleScript-driven Chrome).")
    R.append("")
    R.append(f"Scope: the site lists {plan.get('companies_total_in_index')} companies, of which "
             f"**{plan.get('companies_with_questions')} carry curated questions** "
             f"({plan.get('questions_to_scrape', 0) + already} in total). "
             f"{len(summary)} are captured here.")
    R.append("")
    skipped = plan.get("skipped_already_stored", [])
    if skipped:
        R.append("Already-stored banks are not duplicated here: " +
                 ", ".join(f"`{s}`" for s in skipped) +
                 " (see `../applications/`).")
        R.append("")
    R.append("Each company folder contains:")
    R.append("")
    R.append("- `questions.json` — structured question records (statement, tags, roles, "
             "frequency, last asked, stage)")
    R.append("- `qbank.md` — the same content, readable")
    R.append("- `raw/<slug>.txt` — the untouched question page, verbatim, including the "
             "site's answer body")
    R.append("")
    R.append("## Companies")
    R.append("")
    R.append("| company | questions | roles of note |")
    R.append("|---|---|---|")
    for comp, meta, n in sorted(summary, key=lambda x: -x[2]):
        R.append(f"| [{meta.get('display_name')}]({comp}/qbank.md) | {n} | "
                 f"[questions.json]({comp}/questions.json) |")
    R.append("")

    pending = {}
    for m in json.loads((QB / "missing.json").read_text()):
        pending.setdefault(m["company"], []).append(m["slug"])
    if pending:
        R.append("## Pending")
        R.append("")
        R.append(f"{sum(len(v) for v in pending.values())} questions across "
                 f"{len(pending)} companies were not fetched: the site's bot protection "
                 f"(Cloudflare) began returning 403 after roughly 1,330 requests in this "
                 f"session. Re-run the scraper to pick these up once it clears.")
        R.append("")
        for c, slugs in sorted(pending.items(), key=lambda x: -len(x[1])):
            R.append(f"- **{c}** ({len(slugs)}): {', '.join(slugs)}")
        R.append("")
    (OUTROOT / "README.md").write_text("\n".join(R) + "\n", encoding="utf-8")

    print(f"companies written : {written}")
    print(f"questions written : {total_q}")
    print(f"records available : {len(recs)}")
    if bad:
        print(f"PROBLEMS ({len(bad)}):")
        for b in bad[:20]:
            print("   ", b)
    print(f"output root       : {OUTROOT}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
