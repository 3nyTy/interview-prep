"""Render one HTML + PDF resume per entry in roles.py.

Usage:
    python build.py              # all roles
    python build.py anthropic-swe databricks-staff   # just these

Output goes to resume/out/. PDFs are printed by headless Chrome or Edge.
Anything over 2 pages is flagged.
"""
import os, re, shutil, subprocess, sys
from pathlib import Path

HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(HERE))
from roles import R, SKILLS  # noqa: E402

NAME = "Gowri"  # used in document titles
BASE = (HERE / "base.html").read_text(encoding="utf-8")
OUT = HERE / "out"
MAX_PAGES = 2


def skills_html(order):
    keys = [k for k in order if k in SKILLS] + [k for k in SKILLS if k not in order]
    rows = [f"  <b>{SKILLS[k][0]}</b><span>{SKILLS[k][1]}</span>" for k in keys]
    return '<div class="skills">\n' + "\n".join(rows) + "\n</div>"


def build_html(slug, title, summary, order):
    doc = (BASE.replace("{{TITLE}}", f"{NAME} — Resume — {title}")
               .replace("{{SUMMARY}}", summary)
               .replace("{{SKILLS}}", skills_html(order)))
    path = OUT / f"{slug}.html"
    path.write_text(doc, encoding="utf-8")
    return path


def find_browser():
    candidates = [
        shutil.which("google-chrome"), shutil.which("chromium"), shutil.which("chrome"), shutil.which("msedge"),
        r"C:\Program Files\Google\Chrome\Application\chrome.exe",
        r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
        r"C:\Program Files\Microsoft\Edge\Application\msedge.exe",
        "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
        "/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge",
    ]
    for c in candidates:
        if c and os.path.exists(c):
            return c
    sys.exit("No Chrome or Edge found; install one or add it to PATH.")


def render_pdf(browser, html_path):
    pdf_path = html_path.with_suffix(".pdf")
    subprocess.run([browser, "--headless", "--disable-gpu", "--no-pdf-header-footer",
                    f"--print-to-pdf={pdf_path}", html_path.as_uri()],
                   capture_output=True, timeout=90)
    return pdf_path


def page_count(pdf_path):
    if not pdf_path.exists():
        return -1
    return len(re.findall(rb"/Type\s*/Page(?!s)", pdf_path.read_bytes()))


def main(slugs):
    OUT.mkdir(exist_ok=True)
    browser = find_browser()
    todo = {s: R[s] for s in (slugs or R) if s != "template"}
    bad = []
    for slug, (title, summary, order) in todo.items():
        pages = page_count(render_pdf(browser, build_html(slug, title, summary, order)))
        flag = "" if 0 < pages <= MAX_PAGES else "  <-- check length"
        print(f"  {slug:22s} {pages}pp  {title}{flag}")
        if not 0 < pages <= MAX_PAGES:
            bad.append(slug)
    unfilled = "[TODO" in BASE or any("[TODO" in v[1] for v in todo.values())
    print(f"\n{len(todo)} rendered, {len(bad)} over {MAX_PAGES} pages")
    if unfilled:
        print("Note: some [TODO] placeholders are still unfilled.")


if __name__ == "__main__":
    main(sys.argv[1:])
