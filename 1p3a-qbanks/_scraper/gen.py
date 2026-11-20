#!/usr/bin/env python3
"""Generate scrape plan + JS workers for the 1p3a qbank scrape.

Modes:
  plan    -> /tmp/qb/plan.json from /tmp/companies.json
  phaseA  -> one JS per company returning its question slug list
  phaseB  -> initial detail batches from phaseA results
  merge   -> merge res/*.json into records.json, write missing.json
  retry   -> retry batches for every question still missing
"""
import json
import pathlib
import sys

QB = pathlib.Path("/tmp/qb")
SKIP = set()
BATCH = 10

COMPANY_JS = r"""
(function(){
  var C = "__COMPANY__";
  var x = new XMLHttpRequest();
  x.open('GET', '/interview/problems/company/' + C, false);
  x.send();
  var doc = new DOMParser().parseFromString(x.responseText, 'text/html');
  var pre = '/interview/problems/company/' + C + '/';
  var as = doc.querySelectorAll('a');
  var seen = {}, items = [];
  for (var i = 0; i < as.length; i++) {
    var h = as[i].getAttribute('href') || '';
    if (h.indexOf(pre) !== 0) continue;
    var slug = h.slice(pre.length);
    if (!slug || slug.indexOf('/') >= 0) continue;
    if (seen[slug]) continue;
    seen[slug] = 1;
    var h3 = as[i].querySelector('h3');
    items.push({ slug: slug, cardTitle: h3 ? h3.textContent.trim() : null });
  }
  return JSON.stringify({ company: C, httpStatus: x.status, count: items.length, items: items });
})()
"""

DETAIL_JS = r"""
(function(){
  function toText(root){
    var out = '';
    (function walk(n){
      for (var i = 0; i < n.childNodes.length; i++) {
        var c = n.childNodes[i];
        if (c.nodeType === 3) { out += c.nodeValue; continue; }
        if (c.nodeType !== 1) continue;
        var tag = c.tagName;
        if (tag === 'SCRIPT' || tag === 'STYLE' || tag === 'SVG' || tag === 'NOSCRIPT') continue;
        var block = /^(DIV|P|H1|H2|H3|H4|H5|H6|LI|UL|OL|TR|TABLE|THEAD|TBODY|SECTION|ARTICLE|HEADER|FOOTER|BLOCKQUOTE|PRE|BR|HR|FIGURE)$/.test(tag);
        if (block && out && out.charAt(out.length - 1) !== '\n') out += '\n';
        walk(c);
        if (block && out.charAt(out.length - 1) !== '\n') out += '\n';
      }
    })(root);
    return out.replace(/\n{3,}/g, '\n\n').replace(/[ \t]+\n/g, '\n').trim();
  }

  var urls = __URLS__;
  var res = [];
  for (var i = 0; i < urls.length; i++) {
    var u = urls[i];
    var parts = u.split('/');
    var rec = {
      slug: parts[parts.length - 1],
      company: parts[parts.length - 2],
      url: 'https://www.1point3acres.com' + u
    };
    try {
      var x = new XMLHttpRequest();
      x.open('GET', u, false);
      x.send();
      rec.httpStatus = x.status;
      rec.respLen = x.responseText.length;

      var doc = new DOMParser().parseFromString(x.responseText, 'text/html');
      var nd = doc.getElementById('__NEXT_DATA__');
      var pp = nd ? (JSON.parse(nd.textContent).props.pageProps || {}) : {};
      var h1 = doc.querySelector('h1');
      var main = h1 ? h1.closest('main') : null;
      if (!main) main = doc.querySelector('main');
      rec.hadMain = !!main;

      var meta = {};
      var grid = main ? main.querySelector('div.grid.grid-cols-3') : null;
      if (grid) {
        var kids = grid.children;
        for (var k = 0; k < kids.length; k++) {
          var lab = kids[k].children[0], val = kids[k].children[1];
          if (lab && val) meta[lab.textContent.trim()] = val.textContent.trim();
        }
      }

      var duration = null, access = null;
      var hdr = main ? main.querySelector('header') : null;
      if (hdr) {
        var ht = hdr.textContent || '';
        var dm = ht.match(/(\d+)\s*min/);
        if (dm) duration = dm[1] + ' min';
        if (ht.indexOf('Free preview') >= 0) access = 'Free preview';
        else if (ht.indexOf('Members') >= 0) access = 'Members';
      }

      rec.slug = pp.slug || rec.slug;
      rec.company = pp.company || rec.company;
      rec.questionTitle = pp.questionTitle || (h1 ? h1.textContent.trim() : null);
      rec.category = pp.category || null;
      rec.overview = pp.overview || null;
      rec.description = pp.description || null;
      rec.bankName = pp.bankName || null;
      rec.roles = pp.roles || [];
      rec.tags = pp.tags || [];
      rec.sourceCount = (pp.sourceCount === undefined ? null : pp.sourceCount);
      rec.isLocked = (pp.isLocked === undefined ? null : pp.isLocked);
      rec.duration = duration;
      rec.access = access;
      rec.frequency = meta['Frequency'] || null;
      rec.lastAsked = meta['Last asked'] || null;
      rec.stage = meta['Stage'] || null;
      rec.hasAnswer = !!pp.answerText;
      rec.answerLen = (pp.answerText || '').length;
      rec.rawBody = main ? toText(main) : null;
      rec.good = !!rec.questionTitle;
    } catch (e) {
      rec.error = String(e);
      rec.good = false;
    }
    res.push(rec);
  }
  return JSON.stringify(res);
})()
"""


def _load_plan():
    return json.loads((QB / "plan.json").read_text())


def mode_plan():
    data = json.loads(pathlib.Path("/tmp/companies.json").read_text())
    items = data["items"]
    todo = [i for i in items if i.get("qbank_count", 0) > 0 and i["identifier"] not in SKIP]
    todo.sort(key=lambda x: -x["qbank_count"])
    plan = {
        "scraped_at": "2026-09-20",
        "skipped_already_stored": sorted(SKIP),
        "companies_total_in_index": data.get("total"),
        "companies_with_questions": sum(1 for i in items if i.get("qbank_count", 0) > 0),
        "companies_to_scrape": len(todo),
        "questions_to_scrape": sum(i["qbank_count"] for i in todo),
        "companies": [
            {
                "slug": i["identifier"],
                "display_name": i.get("display_name") or i.get("name"),
                "qbank_count": i["qbank_count"],
                "oj_count": i.get("oj_count", 0),
                "external_count": i.get("external_count", 0),
                "total_count": i.get("total_count", 0),
                "description": i.get("description"),
                "logo": i.get("logo"),
                "last_activity": i.get("last_activity"),
            }
            for i in todo
        ],
    }
    (QB / "plan.json").write_text(json.dumps(plan, indent=2, ensure_ascii=False))
    print(f"companies to scrape : {plan['companies_to_scrape']}")
    print(f"questions to scrape : {plan['questions_to_scrape']}")
    print("wrote /tmp/qb/plan.json")


def mode_phaseA():
    plan = _load_plan()
    d = QB / "phaseA"
    d.mkdir(parents=True, exist_ok=True)
    for i, c in enumerate(plan["companies"]):
        (d / f"{i:03d}_{c['slug'].replace('.', '_')}.js").write_text(
            COMPANY_JS.replace("__COMPANY__", c["slug"])
        )
    print(f"wrote {len(plan['companies'])} phaseA JS files")


def _queue():
    plan = _load_plan()
    byslug = {c["slug"]: c for c in plan["companies"]}
    allq = []
    # out/ first, then out_extra/, so late additions append to the end of the
    # queue and the already-fetched detail batches keep their composition.
    for dirname in ("out", "out_extra"):
        for p in sorted((QB / dirname).glob("*.json")):
            try:
                d = json.loads(p.read_text())
            except Exception:
                continue
            if d.get("company") not in byslug:
                continue
            for it in d["items"]:
                allq.append((d["company"], it["slug"]))
    return allq


def mode_phaseA_extra():
    slugs = [s for s in sys.argv[2].split(",") if s]
    d = QB / "phaseA_extra"
    d.mkdir(parents=True, exist_ok=True)
    for c in slugs:
        (d / f"{c.replace('.', '_')}.js").write_text(
            COMPANY_JS.replace("__COMPANY__", c)
        )
    print(f"wrote {len(slugs)} extra phaseA JS files to {d}")


def _write_batches(pairs, outdir, prefix):
    outdir.mkdir(parents=True, exist_ok=True)
    for old in outdir.glob("*.js"):
        old.unlink()
    n = 0
    for start in range(0, len(pairs), BATCH):
        chunk = pairs[start:start + BATCH]
        urls = [f"/interview/problems/company/{c}/{s}" for c, s in chunk]
        (outdir / f"{prefix}{start // BATCH:04d}.js").write_text(
            DETAIL_JS.replace("__URLS__", json.dumps(urls))
        )
        n += 1
    return n


def mode_phaseB():
    allq = _queue()
    n = _write_batches(allq, QB / "detail", "batch_")
    (QB / "queue.json").write_text(
        json.dumps([{"company": c, "slug": s} for c, s in allq], indent=1, ensure_ascii=False)
    )
    print(f"questions queued : {len(allq)}")
    print(f"detail batches   : {n} (batch size {BATCH})")


def mode_merge():
    recs, bad = {}, 0
    files = (sorted((QB / "res").glob("*.json"))
             + sorted((QB / "res").glob("*/*.json"))
             + sorted((QB / "res_old").glob("*.json")))
    for p in files:
        try:
            data = json.loads(p.read_text())
        except Exception:
            bad += 1
            continue
        if not isinstance(data, list):
            bad += 1
            continue
        for r in data:
            if not r.get("slug"):
                continue
            key = f"{r.get('company')}/{r['slug']}"
            if r.get("good") and r.get("questionTitle"):
                recs[key] = r
    (QB / "records.json").write_text(json.dumps(recs, ensure_ascii=False, indent=1))
    allq = _queue()
    missing = [{"company": c, "slug": s} for c, s in allq if f"{c}/{s}" not in recs]
    (QB / "missing.json").write_text(json.dumps(missing, ensure_ascii=False))
    print(f"good records : {len(recs)}")
    print(f"missing      : {len(missing)}")
    print(f"bad files    : {bad}")


def mode_retry():
    missing = json.loads((QB / "missing.json").read_text())
    pairs = [(m["company"], m["slug"]) for m in missing]
    if not pairs:
        print("nothing to retry")
        return
    n = _write_batches(pairs, QB / "retry", "retry_")
    print(f"retry questions : {len(pairs)}")
    print(f"retry batches   : {n} (batch size {BATCH})")


if __name__ == "__main__":
    {"plan": mode_plan, "phaseA": mode_phaseA, "phaseAextra": mode_phaseA_extra,
     "phaseB": mode_phaseB, "merge": mode_merge, "retry": mode_retry}[sys.argv[1]]()
