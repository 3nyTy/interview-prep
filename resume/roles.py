# One entry per job you apply to.
# slug -> (role title shown in the doc title, summary HTML, skills order)
#
# Rule: every claim here must be true for you. Tailor by choosing WHICH true
# things to lead with, not by adding new ones. Fill the [TODO] metrics once in
# SKILLS / base.html and reuse them.

P = lambda s: "<p class=\"summary\">" + s + "</p>"

# Skill rows. Key -> (label, content). Each role picks an order.
SKILLS = {
    "lang":  ("Languages", "Python, Java (10 yrs), SQL, KQL, TypeScript / JavaScript"),
    "ai":    ("AI &amp; Evals", "LLM evaluation SDKs and harnesses, eval dashboards, RAG, agent tool use (MCP), Azure OpenAI, Responsible AI review"),
    "data":  ("Data Platforms", "Azure Synapse, ADX / Kusto, Microsoft Fabric, KPI and metrics pipelines, semantic layers"),
    "dist":  ("Distributed Systems", "Backend services, API design, pipelines at scale, reliability and observability"),
    "fs":    ("Full Stack", "Frontend, backend, data and infrastructure, end to end"),
    "lead":  ("Leadership", "Platforms other teams adopt, cross-team alignment, mentoring, design review"),
}

# Reusable opening line (edit once, used everywhere).
OPEN = ("Software engineer with <b>10 years</b> across frontend, backend, data and infrastructure, "
        "now focused on <b>proving AI systems work</b>: evaluation, measurement and the data platforms behind them. ")

R = {}

R["template"] = ("[Company] — [Role title]", P(
    OPEN +
    "[Sentence 2: the one thing from your experience that matches this JD's top requirement.] "
    "[Sentence 3: second match, with a number.] "
    "[Sentence 4: why this company / what you'd own.]"),
    ["ai", "data", "lang", "dist", "fs", "lead"])

R["databricks-staff"] = ("Databricks — Staff Software Engineer", P(
    OPEN +
    "Own KPI measurement pipelines on <b>Synapse, Kusto and Fabric</b>, and built an <b>LLM evaluation SDK and platform</b> "
    "that [TODO: N] teams onboarded onto. "
    "Designed a self-service analytics platform with <b>natural-language-to-KQL</b>, a semantic layer and an MCP server. "
    "[TODO: one line tying this to the specific Databricks team/JD.]"),
    ["data", "ai", "dist", "lang", "lead", "fs"])

R["anthropic-swe"] = ("Anthropic — Software Engineer", P(
    OPEN +
    "Built an <b>LLM evaluation SDK and platform</b> with dashboards adopted by [TODO: N] teams, and apply "
    "<b>Responsible AI</b> practice in production. "
    "[TODO: one line tying this to the specific Anthropic team/JD.]"),
    ["ai", "lang", "dist", "data", "lead", "fs"])

R["arize-staff"] = ("Arize AI — Staff Software Engineer", P(
    OPEN +
    "Built the evals SDK and dashboarding platform my org standardized on ([TODO: N] teams), and the KPI pipelines that "
    "measure AI-driven support outcomes such as ticket deflection. "
    "[TODO: one line tying this to the specific Arize JD.]"),
    ["ai", "data", "lang", "dist", "lead", "fs"])
