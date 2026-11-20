# Resume generator

One base resume, a tailored version per role.

- `base.html` — layout, contact, experience, education. Filled once. `{{SUMMARY}}` and `{{SKILLS}}` are swapped per role.
- `roles.py` — one entry per application: title, tailored summary, and which skill rows to lead with. Copy `R["template"]` to add a role.
- `build.py` — writes `out/<slug>.html` and `out/<slug>.pdf` (headless Chrome or Edge) and flags any over 2 pages.

```
cd resume
python build.py                    # every role
python build.py databricks-staff   # one role
```

Workflow per application:
1. Fill every `[TODO]` in `base.html` once (numbers matter most — teams onboarded, data volume, time saved).
2. Paste the JD's top 3 requirements as a comment above a new `roles.py` entry.
3. Write a 3–4 sentence summary that leads with the true parts of your experience matching those 3.
4. Reorder skill rows so the JD's language comes first. Build, open the PDF, check it reads well at 2 pages.

Every claim must be true. Tailoring means choosing what to lead with, not adding things you haven't done.
