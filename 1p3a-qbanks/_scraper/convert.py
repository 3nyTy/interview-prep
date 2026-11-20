#!/usr/bin/env python3
"""Build snowflake / netflix / reddit folders in the same shape as the 53 scraped ones.

These three banks are paywalled on the site now (isLocked: true, statement truncated
to a ~200-char teaser), so statements come from the local captures made in Aug 2026:

  snowflake : ~/personal-archive/applications/snowflake-application/raw/NN_slug.txt
  netflix   : ~/personal-archive/applications/netflix-application/1p3a-netflix-qbank.md
  reddit    : /tmp/qb/reddit_crawl.json  (metadata only; statements stay paywalled)
"""
import json
import pathlib
import re
import sys

HOME = pathlib.Path.home()
QB = pathlib.Path("/tmp/qb")
OUTROOT = HOME / "personal-archive/1p3a-qbanks"
SCRAPED_AT = "2026-09-20"

CATEGORIES = {"Coding", "System Design", "Behavioral", "Other", "Product", "ML"}
ROLES = {"All", "DS", "EM", "Fellows", "Infra Eng", "MLE", "Perf Eng", "RE", "RS", "SWE",
         "Data Eng", "Frontend", "Backend", "Mobile", "Security", "Quant", "PM"}


def slugify(display):
    if display is None:
        return None
    return display.strip().lower().replace(" ", "-")


def role_slug(r):
    return slugify(r)


def split_badges(badges):
    """Leading role labels are roles; everything after is a tag."""
    roles, tags, in_tags = [], [], False
    for b in badges:
        if not in_tags and b in ROLES:
            roles.append(b)
        else:
            in_tags = True
            tags.append(b)
    return roles, tags


def parse_page_body(body):
    """Parse the shared detail-page text layout into fields."""
    lines = [l.rstrip() for l in body.split("\n")]
    start = None
    for i in range(len(lines) - 1):
        if lines[i].strip() in ("Snowflake", "Netflix", "Reddit") and lines[i + 1].strip() in CATEGORIES:
            start = i
            break
    if start is None:
        return None
    out = {"company_line": lines[start].strip(), "category": lines[start + 1].strip()}
    j = start + 2
    while j < len(lines) and not lines[j].strip():
        j += 1
    if j < len(lines) and lines[j].strip() == "·":
        j += 1
    while j < len(lines) and not lines[j].strip():
        j += 1
    m = re.match(r"^(\d+)\s*min$", lines[j].strip()) if j < len(lines) else None
    if m:
        out["duration"] = lines[j].strip()
        j += 1
    while j < len(lines) and not lines[j].strip():
        j += 1
    if j < len(lines) and lines[j].strip() in ("Members", "Free preview"):
        out["access"] = lines[j].strip()
        j += 1
    while j < len(lines) and not lines[j].strip():
        j += 1
    out["title"] = lines[j].strip() if j < len(lines) else None
    j += 1

    # badges sit immediately above the Frequency label, separated from the statement
    # by a blank line
    fi = None
    for k in range(j, len(lines)):
        if lines[k].strip() == "Frequency":
            fi = k
            break
    if fi is None:
        return out
    b = fi - 1
    badges = []
    while b >= j and lines[b].strip():
        badges.append(lines[b].strip())
        b -= 1
    badges.reverse()
    out["roles"], out["tags"] = split_badges(badges)
    statement = "\n".join(lines[j:b + 1]).strip()
    out["overview"] = statement or None

    for k, key in ((fi + 1, "frequency"), (fi + 3, "last_asked"), (fi + 5, "stage")):
        if k < len(lines) and lines[k].strip():
            out[key] = lines[k].strip()
    return out


def load_snowflake():
    src = HOME / "personal-archive/applications/snowflake-application/raw"
    recs = []
    for p in sorted(src.glob("*.txt")):
        text = p.read_text(encoding="utf-8")
        h1 = re.search(r"===H1===\n(.+)", text)
        url = re.search(r"===URL===\n(\S+)", text)
        body = text.split("===BODY===\n", 1)[1] if "===BODY===\n" in text else ""
        f = parse_page_body(body) or {}
        recs.append({
            "slug": url.group(1).rstrip("/").split("/")[-1] if url else p.stem.split("_", 1)[-1],
            "title": f.get("title") or (h1.group(1).strip() if h1 else None),
            "category": f.get("category"), "duration": f.get("duration"),
            "frequency": f.get("frequency"), "last_asked": f.get("last_asked"),
            "stage": f.get("stage"), "access": f.get("access"),
            "roles": f.get("roles", []), "tags": f.get("tags", []),
            "overview": f.get("overview"),
            "url": url.group(1).strip() if url else None,
            "raw": "===H1===\n%s\n===URL===\n%s\n===BODY===\n%s\n" % (
                (h1.group(1).strip() if h1 else ""), (url.group(1).strip() if url else ""), body.rstrip()),
            "statement_available": True,
        })
    return recs


def load_netflix():
    md = HOME / "personal-archive/applications/netflix-application/1p3a-netflix-qbank.md"
    text = md.read_text(encoding="utf-8")
    blocks = re.split(r"\n## \d+\. ", "\n" + text)[1:]
    recs = []
    for blk in blocks:
        lines = blk.split("\n")
        title = lines[0].strip()
        um = re.search(r"<(https://\S+)>", blk)
        url = um.group(1) if um else None
        f = parse_page_body(blk) or {}
        recs.append({
            "slug": url.rstrip("/").split("/")[-1] if url else slugify(title),
            "title": f.get("title") or title,
            "category": f.get("category"), "duration": f.get("duration"),
            "frequency": f.get("frequency"), "last_asked": f.get("last_asked"),
            "stage": f.get("stage"), "access": f.get("access"),
            "roles": f.get("roles", []), "tags": f.get("tags", []),
            "overview": f.get("overview"),
            "url": url,
            "raw": "===H1===\n%s\n===URL===\n%s\n===BODY===\n%s\n" % (title, url or "", blk.rstrip()),
            "statement_available": True,
        })
    return recs


def load_reddit():
    d = json.loads((QB / "reddit_crawl.json").read_text())
    recs = []
    for r in d["records"]:
        locked = bool(r.get("isLocked"))
        recs.append({
            "slug": r["slug"],
            "title": r.get("questionTitle"),
            "category": r.get("category"), "duration": r.get("duration"),
            "frequency": r.get("frequency"), "last_asked": r.get("lastAsked"),
            "stage": r.get("stage"), "access": r.get("access"),
            "roles": [x.upper() if x.lower() in ("swe", "mle", "ds", "em", "re", "rs") else x
                      for x in (r.get("roles") or [])],
            "tags": r.get("tags") or [],
            "overview": None if locked else r.get("overviewTeaser"),
            "overview_teaser": r.get("overviewTeaser") if locked else None,
            "url": r.get("url"),
            "raw": "===H1===\n%s\n===URL===\n%s\n===BODY===\n%s\n" % (
                r.get("questionTitle") or "", r.get("url") or "", (r.get("rawBody") or "").rstrip()),
            "statement_available": not locked,
            "source_count": r.get("sourceCount"),
        })
    return recs


def build(slug, display, recs, meta, source_note):
    d = OUTROOT / slug
    (d / "raw").mkdir(parents=True, exist_ok=True)
    questions = []
    for i, r in enumerate(recs, 1):
        roles = r.get("roles") or []
        tags = r.get("tags") or []
        questions.append({
            "n": i,
            "slug": r["slug"],
            "title": r.get("title"),
            "category": (r.get("category") or "").lower().replace(" ", "-") or None,
            "duration": r.get("duration"),
            "frequency": r.get("frequency"),
            "last_asked": r.get("last_asked"),
            "stage": r.get("stage"),
            "access": r.get("access"),
            "roles": roles,
            "tags": tags,
            "roles_slug": [role_slug(x) for x in roles],
            "tags_slug": tags,
            "overview": r.get("overview"),
            "overview_teaser": r.get("overview_teaser"),
            "statement_available": bool(r.get("statement_available")),
            "source_count": r.get("source_count"),
            "url": r.get("url"),
        })
        if r.get("raw"):
            (d / "raw" / f"{r['slug']}.txt").write_text(r["raw"], encoding="utf-8")

    payload = {
        "company": slug, "display_name": display,
        "qbank_count": meta.get("qbank_count"), "questions_scraped": len(questions),
        "site_total_count": meta.get("total_count"), "oj_count": meta.get("oj_count"),
        "external_count": meta.get("external_count"), "description": meta.get("description"),
        "company_url": f"https://www.1point3acres.com/interview/problems/company/{slug}",
        "scraped_at": SCRAPED_AT,
        "content_source": source_note,
        "note": "Question block only. The site's answer body is not included here; "
                "the untouched page is kept verbatim in raw/<slug>.txt.",
        "questions": questions,
    }
    (d / "questions.json").write_text(json.dumps(payload, indent=2, ensure_ascii=False) + "\n",
                                      encoding="utf-8")

    M = [f"# {display} — 1point3acres Question Bank ({len(questions)} curated questions)", "",
         f"Source: 1point3acres qbank (`company={slug}`), {source_note}.",
         f"Company page: <{f'https://www.1point3acres.com/interview/problems/company/{slug}'}>", "",
         f"The site lists {meta.get('total_count')} entries for this company: "
         f"**{meta.get('qbank_count')} curated questions**, "
         + (f"{len(questions)} captured below, "
            if len(questions) != meta.get("qbank_count") else "all captured below, ")
         + f"{meta.get('oj_count')} OJ links, {meta.get('external_count')} external links.", ""]
    if meta.get("description"):
        M += [f"> {meta['description']}", ""]
    if slug == "reddit":
        M += ["⚠️ **This bank is paywalled.** 27 of 28 questions render as locked "
              "(\"会员专享题目\") on the site and their statements are truncated to "
              "~200-char teasers. Titles, tags, roles, frequency and stage below are "
              "authoritative; full statements were never captured. The teaser text for "
              "each locked question is in `questions.json` under `overview_teaser`.", ""]
        M += ["Question statements only. The site's answer body is not reproduced here; "
              "the page as served is archived under `raw/`.", ""]
    else:
        M += ["Question statements only. The site's answer body is not reproduced here; "
              "the full captured page is archived verbatim under `raw/`.", ""]
    M += ["## Index", ""]
    for q in questions:
        M.append(f"{q['n']}. {q['title']}")
    M += ["", "---", ""]
    for q in questions:
        M.append(f"## {q['n']}. {q['title']}")
        M.append("")
        for k, v in (("category", q.get("category")), ("duration", q.get("duration")),
                     ("frequency", q.get("frequency")), ("last asked", q.get("last_asked")),
                     ("stage", q.get("stage")), ("access", q.get("access")),
                     ("roles", ", ".join(q.get("roles") or [])),
                     ("tags", ", ".join(q.get("tags") or []))):
            if v:
                M.append(f"- **{k}**: {v}")
        if q.get("url"):
            M.append(f"- **url**: {q['url']}")
        if q.get("source_count"):
            M.append(f"- **related discussions**: {q['source_count']}")
        M.append("")
        M.append("### Statement")
        M.append("")
        if q.get("overview"):
            M.append(q["overview"])
        elif q.get("overview_teaser"):
            M.append("_Paywalled. The site serves only this teaser:_")
            M.append("")
            M.append(f"> {q['overview_teaser']}")
        else:
            M.append("_(not captured)_")
        M += ["", "---", ""]
    (d / "qbank.md").write_text("\n".join(M).rstrip() + "\n", encoding="utf-8")
    return len(questions)


def rebuild_readme():
    dirs = sorted(p for p in OUTROOT.iterdir() if p.is_dir() and not p.name.startswith("_"))
    rows = []
    for d in dirs:
        q = d / "questions.json"
        if not q.exists():
            continue
        data = json.loads(q.read_text())
        rows.append((d.name, data.get("display_name"), len(data["questions"]),
                     data.get("content_source")))
    R = ["# 1point3acres Question Banks", "",
         f"Curated interview-question banks scraped from 1point3acres on {SCRAPED_AT} "
         f"from the logged-in session (AppleScript-driven Chrome).", "",
         f"Scope: the site lists 1227 companies, of which **56 carry curated questions** "
         f"(1889 in total). All {len(rows)} are captured here.", "",
         "Each company folder contains:", "",
         "- `questions.json` — structured question records (statement, tags, roles, "
         "frequency, last asked, stage)",
         "- `qbank.md` — the same content, readable",
         "- `raw/<slug>.txt` — the captured page, verbatim, including any answer body",
         "", "## Companies", "", "| company | questions | source |", "|---|---|---|"]
    for name, disp, n, src in sorted(rows, key=lambda x: -x[2]):
        tag = "local capture" if src and "local" in str(src).lower() else "live scrape"
        R.append(f"| [{disp}]({name}/qbank.md) | {n} | {tag} |")
    R += ["", "`live scrape` = fetched 2026-09-20. `local capture` = the bank is paywalled "
              "on the site now, so statements come from the August 2026 capture "
              "(see each folder's `qbank.md` header).", ""]
    (OUTROOT / "README.md").write_text("\n".join(R) + "\n", encoding="utf-8")
    return rows


def main():
    plan = json.loads((QB / "plan.json").read_text())
    meta = {c["slug"]: c for c in plan["companies"]}
    n1 = build("snowflake", "Snowflake", load_snowflake(), meta.get("snowflake", {}),
               "local capture 2026-08-27 (bank now paywalled)")
    n2 = build("netflix", "Netflix", load_netflix(), meta.get("netflix", {}),
               "local capture 2026-08-26 (bank now paywalled)")
    n3 = build("reddit", "Reddit", load_reddit(), meta.get("reddit", {}),
               "metadata scraped 2026-09-20; statements paywalled")
    print(f"snowflake : {n1} questions")
    print(f"netflix   : {n2} questions")
    print(f"reddit    : {n3} questions")
    rows = rebuild_readme()
    print(f"README    : {len(rows)} companies listed")


if __name__ == "__main__":
    sys.exit(main())
