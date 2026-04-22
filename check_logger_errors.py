import re

# Read the file
with open('src/test/java/stepdefinitions/LifeSteps.java', 'r') as f:
    content = f.read()

# Split into methods
methods = re.split(r'(@[A-Z][a-zA-Z]+(?:\([^)]*\))?\s*public\s+void\s+\w+\([^)]*\)\s*(?:throws\s+[^{]+)?\s*\{)', content)

errors_found = []
line_count = 0

for i in range(1, len(methods), 2):
    method_header = methods[i]
    method_body = methods[i+1] if i+1 < len(methods) else ""
    
    # Count lines to track position
    header_lines = method_header.count('\n')
    
    # Find logger statements in this method
    logger_matches = re.finditer(r'logger\.(info|debug|warn|error)\s*\([^;]+\);', method_body)
    
    for match in logger_matches:
        logger_stmt = match.group()
        # Check if logger has placeholder {} but missing variable
        if '{}' in logger_stmt:
            # Count placeholders
            placeholders = logger_stmt.count('{}')
            # Extract what's after the string literal
            parts = logger_stmt.split('"')
            if len(parts) >= 3:
                after_string = parts[2]
                # Count commas which indicate variables
                variables = after_string.count(',')
                if placeholders != variables:
                    stmt_line = line_count + header_lines + method_body[:match.start()].count('\n')
                    errors_found.append((stmt_line, logger_stmt[:100]))
    
    line_count += header_lines + method_body.count('\n')

if errors_found:
    print(f"Found {len(errors_found)} potential logger errors:")
    for line, stmt in errors_found[:20]:  # Show first 20
        print(f"Line {line}: {stmt}...")
else:
    print("No obvious logger errors found with placeholder mismatches!")

print(f"\nTotal methods analyzed: {len(methods)//2}")
