import re

# Read the file
with open('/home/runner/work/qa-automation/qa-automation/src/test/java/stepdefinitions/LifeSteps.java', 'r') as f:
    content = f.read()

# Count logger statements before
before_count = content.count('logger.info')
print(f"Logger statements before: {before_count}")

# Pattern 1: Remove "verified successfully" logs
content = re.sub(r'\s*logger\.info\([^)]*verified successfully[^)]*\);\s*\n', '\n', content)

# Pattern 2: Remove "Verifying X" when followed by actual logging with data
content = re.sub(r'(\s*logger\.info\("Verifying[^"]*"\);\s*\n)(\s*[A-Z][a-zA-Z]+ [a-zA-Z]+ = )', r'\2', content)

# Pattern 3: Remove standalone navigating/clicking logs  
patterns_to_remove = [
    r'\s*logger\.info\("Navigating to[^"]*"\);\s*\n',
    r'\s*logger\.info\("Clicking[^"]*"\);\s*\n',
    r'\s*logger\.info\("Saving[^"]*"\);\s*\n',
    r'\s*logger\.info\("Selecting[^"]*"\);\s*\n(?![^{]*logger\.info)',
    r'\s*logger\.info\("Adding[^"]*"\);\s*\n(?![^{]*logger\.info)',
]

for pattern in patterns_to_remove:
    content = re.sub(pattern, '', content)

# Count logger statements after
after_count = content.count('logger.info')
print(f"Logger statements after: {after_count}")
print(f"Removed: {before_count - after_count}")

# Write back
with open('/home/runner/work/qa-automation/qa-automation/src/test/java/stepdefinitions/LifeSteps.java', 'w') as f:
    f.write(content)
