import os
import glob
import re
import json

feature_dir = "src/test/resources/features"

steps = set()
scenarios = []

for file in glob.glob(f"{feature_dir}/**/*.feature", recursive=True):
    with open(file, 'r', encoding='utf-8') as f:
        content = f.read()
        # Find all steps
        for line in content.split('\n'):
            line = line.strip()
            if re.match(r'^(Given|When|Then|And|But)\s+', line):
                steps.add(line)
        
        # Find all scenarios
        scenario_matches = re.finditer(r'^\s*(Scenario|Scenario Outline):\s*(.+)', content, re.MULTILINE)
        for match in scenario_matches:
            scenarios.append(match.group(0).strip())

with open('step_dictionary.txt', 'w', encoding='utf-8') as f:
    f.write('\n'.join(sorted(steps)))

with open('scenario_dictionary.txt', 'w', encoding='utf-8') as f:
    f.write('\n'.join(sorted(scenarios)))
