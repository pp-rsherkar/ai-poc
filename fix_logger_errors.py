import re
import sys

def read_file(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        return f.read()

def write_file(filename, content):
    with open(filename, 'w', encoding='utf-8') as f:
        f.write(content)

def parse_java_methods(content):
    """Parse Java methods and track their local variables"""
    # Pattern to match method definitions
    method_pattern = r'(@\w+.*?\n\s*public\s+\w+\s+\w+\([^)]*\)\s*(?:throws\s+[^{]+)?\s*\{)'
    
    methods = []
    for match in re.finditer(method_pattern, content, re.DOTALL):
        start = match.start()
        # Find matching closing brace
        brace_count = 0
        i = match.end() - 1
        method_start = i
        while i < len(content):
            if content[i] == '{':
                brace_count += 1
            elif content[i] == '}':
                brace_count -= 1
                if brace_count == 0:
                    methods.append({
                        'start': start,
                        'end': i + 1,
                        'content': content[start:i+1],
                        'header': match.group(0)
                    })
                    break
            i += 1
    
    return methods

def extract_variables_from_method(method_content):
    """Extract all variable names declared in a method"""
    # Pattern for variable declarations
    var_pattern = r'\b(?:String|int|boolean|List|Map|BigDecimal|LocalDate|APIResponse|Path|double|float|long)\s+(\w+)\s*[=;]'
    variables = set()
    for match in re.finditer(var_pattern, method_content):
        variables.add(match.group(1))
    return variables

def find_logger_issues_in_method(method_content):
    """Find logger statements and check if they reference undefined variables"""
    issues = []
    
    # Find all logger statements
    logger_pattern = r'(logger\.\w+\([^)]*\);)'
    
    for logger_match in re.finditer(logger_pattern, method_content, re.DOTALL):
        logger_stmt = logger_match.group(1)
        logger_pos = logger_match.start()
        
        # Extract variable references from logger statement (between {})
        var_refs = re.findall(r'\{[^}]*\}', logger_stmt)
        if not var_refs:
            continue
            
        # Find what code comes BEFORE this logger statement
        code_before = method_content[:logger_pos]
        
        # Extract all variables declared before this logger
        vars_before = extract_variables_from_method(code_before)
        
        # Find the actual variable names being referenced in logger
        # Pattern: variable names after commas (parameters to logger)
        params_pattern = r'logger\.\w+\([^,)]+,\s*(.+)\);'
        params_match = re.search(params_pattern, logger_stmt, re.DOTALL)
        
        if params_match:
            params_str = params_match.group(1)
            # Extract variable names (simple heuristic)
            var_names = re.findall(r'\b([a-zA-Z_]\w*)\b', params_str)
            
            # Check if any referenced variable is not yet declared
            undefined_vars = []
            for var in var_names:
                # Skip known constants, method calls, and common terms
                if var in ['String', 'format', 'toString', 'trim', 'toLowerCase', 'toUpperCase',
                          'size', 'get', 'contains', 'equals', 'this', 'true', 'false']:
                    continue
                    
                if var not in vars_before:
                    # Check if this variable is declared AFTER the logger statement
                    code_after = method_content[logger_pos:]
                    if re.search(r'\b(?:String|int|boolean|List|Map|BigDecimal|LocalDate)\s+' + var + r'\s*[=;]', code_after):
                        undefined_vars.append(var)
            
            if undefined_vars:
                issues.append({
                    'statement': logger_stmt,
                    'position': logger_pos,
                    'undefined_vars': undefined_vars
                })
    
    return issues

# For a simpler and safer approach, let's just parse line by line
# and identify patterns that are problematic

def fix_logger_errors_simple(content):
    """Simple pattern-based fixes for common logger errors"""
    lines = content.split('\n')
    fixed_lines = []
    i = 0
    
    while i < len(lines):
        line = lines[i]
        
        # Check if this is a logger line
        if 'logger.' in line and ('logger.info' in line or 'logger.debug' in line or 'logger.error' in line or 'logger.warn' in line):
            # Check common patterns and fix them
            
            # Pattern 1: logger references ruleType but method doesn't have it
            if 'ruleType' in line and 'public void userNavigatesToSetupTab()' in '\n'.join(lines[max(0,i-5):i]):
                fixed_line = line.replace('for {} rule type", ruleType', '')
                fixed_line = fixed_line.replace(' for rule type {}",ruleType', '')
                fixed_line = fixed_line.replace('for rule type', '')
                if '{}' in fixed_line and  'ruleType' in line:
                    # Just make it generic
                    fixed_line = '        logger.info("Navigating to Setup Tab");'
                fixed_lines.append(fixed_line)
                i += 1
                continue
                
        fixed_lines.append(line)
        i += 1
    
    return '\n'.join(fixed_lines)

# Main execution
if __name__ == '__main__':
    filename = 'src/test/java/stepdefinitions/LifeSteps.java'
    
    content = read_file(filename)
    
    # For now, let's just analyze and report
    methods = parse_java_methods(content)
    print(f"Found {len(methods)} methods")
    
    total_issues = 0
    for method in methods[:10]:  # Test first 10
        issues = find_logger_issues_in_method(method['content'])
        if issues:
            total_issues += len(issues)
            header_match = re.search(r'public\s+\w+\s+(\w+)\(', method['header'])
            method_name = header_match.group(1) if header_match else "unknown"
            print(f"\nMethod: {method_name}")
            for issue in issues:
                print(f"  Undefined vars: {issue['undefined_vars']}")
                print(f"  Statement: {issue['statement'][:80]}...")

