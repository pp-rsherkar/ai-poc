#!/usr/bin/env python3
import re

with open('src/test/java/stepdefinitions/LifeSteps.java', 'r') as f:
    content = f.read()
    lines = content.split('\n')

# Find all methods
method_pattern = r'(@\w+[^\n]*\n\s*)?public\s+\w+\s+(\w+)\s*\([^)]*\)\s*(?:throws[^{]*)?\s*\{'

issues = []
current_method = None
method_start_line = 0

for i, line in enumerate(lines, 1):
    # Check if this line starts a method
    method_match = re.search(r'public\s+\w+\s+(\w+)\s*\(', line)
    if method_match:
        current_method = method_match.group(1)
        method_start_line = i
    
    # Check if line has logger with variable placeholders
    if 'logger.' in line and '{}' in line:
        # Extract variables after the string
        logger_match = re.search(r'logger\.\w+\([^"]*"[^"]*"[^)]*,\s*(.+)\);', line)
        if logger_match:
            var_part = logger_match.group(1).strip()
            # Count placeholders
            placeholder_count = line.count('{}')
            # Count variables (rough heuristic)
            variables = [v.strip() for v in var_part.split(',')]
            var_count = len([v for v in variables if v and not v.startswith('"')])
            
            if placeholder_count != var_count:
                issues.append(f"Line {i} ({current_method}): Placeholder mismatch - {placeholder_count} placeholders but {var_count} vars")
                print(f"Line {i}: {line.strip()[:100]}")

# Find duplicate loggers
for i in range(len(lines) - 1):
    if 'logger.info' in lines[i] and 'logger.info' in lines[i+1]:
        if lines[i].strip() and lines[i+1].strip():
            print(f"\nPossible duplicate at lines {i+1}-{i+2}:")
            print(f"  {lines[i].strip()[:80]}")
            print(f"  {lines[i+1].strip()[:80]}")

print(f"\nTotal potential issues found: {len(issues)}")
