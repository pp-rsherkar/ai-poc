"""
build_context.py
Scans all .feature files in FEATURE_DIR and writes:
  - step_dictionary.txt   (steps grouped by domain)
  - scenario_dictionary.txt (scenario titles prefixed by domain)
"""
import os
import glob
import re
from collections import defaultdict

feature_dir = os.environ.get("FEATURE_DIR", "src/test/resources/features")
steps_by_domain = defaultdict(set)
scenarios = []

for file_path in glob.glob(f"{feature_dir}/**/*.feature", recursive=True):
    domain = os.path.basename(os.path.dirname(file_path))
    if domain in ("features", ""):
        domain = "general"
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()
    for match in re.finditer(r"^\s*(Scenario|Scenario Outline):\s*(.+)", content, re.MULTILINE):
        scenarios.append(f"[{domain}] {match.group(0).strip()}")
    for line in content.split("\n"):
        line = line.strip()
        if re.match(r"^(Given|When|Then|And|But)\s+", line):
            steps_by_domain[domain].add(line)

with open("step_dictionary.txt", "w", encoding="utf-8") as f:
    for domain, steps in sorted(steps_by_domain.items()):
        f.write(f"\n--- Domain: {domain} ---\n")
        f.write("\n".join(sorted(steps)))
        f.write("\n")

with open("scenario_dictionary.txt", "w", encoding="utf-8") as f:
    f.write("\n".join(scenarios))

print(f"Domains found  : {sorted(steps_by_domain.keys())}")
print(f"Total steps    : {sum(len(v) for v in steps_by_domain.values())}")
print(f"Total scenarios: {len(scenarios)}")
