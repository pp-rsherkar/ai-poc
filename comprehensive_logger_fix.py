#!/usr/bin/env python3
"""
Comprehensive fix for all logger errors in LifeSteps.java
"""
import re

def read_file(path):
    with open(path, 'r') as f:
        return f.read()

def write_file(path, content):
    with open(path, 'w') as f:
        f.write(content)

def apply_fixes(content):
    """Apply all known logger fixes"""
    fixes_count = 0
    
    # List of all fixes to apply
    # Each fix is a tuple of (old_pattern, new_pattern, description)
    
    fixes = [
        # Fix: Duplicate logger in user_creates_below_tactics_under_same_line_item_and_verifies_it
        (
            r'(public void user_creates_below_tactics_under_same_line_item_and_verifies_it\(DataTable dataTable\) \{)\s*logger\.info\("Starting creation and verification of multiple tactics under the same Line Item"\);\s*(List<Map<String, String>> tactics = dataTable\.asMaps\(String\.class, String\.class\);\s*List<String> expectedTactic = new ArrayList<>\(\);\s*for \(Map<String, String> tacticData : tactics\) \{\s*)logger\.info\("User creates below tactics under same line item and verifies it", dataTable\);',
            r'\1\n        logger.info("Starting creation and verification of multiple tactics under the same Line Item");\n        \2',
            "Remove duplicate logger in user_creates_below_tactics_under_same_line_item_and_verifies_it"
        ),
        
        # Fix: verifyTheCountOfRulesAddedForTheSelectedTargetingRuleTypeOnTheTacticSettingsPage
        (
            r'(public void verifyTheCountOfRulesAddedForTheSelectedTargetingRuleTypeOnTheTacticSettingsPage\(\) \{)\s*logger\.info\("Verifying count of rules added for targeting rule types"\);\s*(String ruleType;\s*for \(Map\.Entry<String, List<String>> entry : rulesMap\.entrySet\(\) \) \{\s*)logger\.info\("Verifying: the count of rules added for the selected targeting rule type on the Tactic Settings page"\);',
            r'\1\n        logger.info("Verifying count of rules added for targeting rule types");\n        \2',
            "Remove duplicate logger in verifyTheCountOfRulesAddedForTheSelectedTargetingRuleTypeOnTheTacticSettingsPage"
        ),
        
        # Fix: verify_the_configured_targeting_rules - move logger after variable
        (
            r'(public void verify_the_configured_targeting_rules\(\) \{)\s*logger\.info\("User saves the settings"\);',
            r'\1',
            "Remove incorrect logger in verify_the_configured_targeting_rules"
        ),
        
        # Fix: verify_campaign_in_database
        (
            r'(public void verify_campaign_in_database\(\) throws SQLException \{)\s*logger\.info\("User saves the settings"\);',
            r'\1\n        logger.info("Verifying the newly created campaign in the database");',
            "Fix logger message in verify_campaign_in_database"
        ),
        
        # Fix: verifyTheDataShouldFilterAsPerTheSelectedFilterValues
        (
            r'(public void verifyTheDataShouldFilterAsPerTheSelectedFilterValues\(\) \{)\s*logger\.info\("Verifying Campaign Dashboard data is filtered as per selected values"\);\s*(for \(Object o : keyType\) \{\s*)logger\.info\("Verifying: the Campaign Dashboard data should filter as per the selected filter values"\);',
            r'\1\n        logger.info("Verifying Campaign Dashboard data is filtered as per selected values");\n        \2',
            "Remove duplicate logger in verifyTheDataShouldFilterAsPerTheSelectedFilterValues"
        ),
        
        # Fix: verifyTheTargetTypeWithRespectToCategory
        (
            r'(for \(Map\.Entry<String, List<String>> entry : categoryNameAndTypeMap\.entrySet\(\) \) \{\s*)logger\.info\("Verifying: target type with respect to category", categoryNameAndType\);',
            r'\1',
            "Remove incorrect logger in verifyTheTargetTypeWithRespectToCategory loop"
        ),
        
        # Fix: user_configures_targeting_rules
        (
            r'(for \(Map\.Entry<String, List<String>> entry : rulesMap\.entrySet\(\) \) \{\s*)logger\.info\("User configures targeting rules as below", ruleTypeAndOptions\);',
            r'\1',
            "Remove incorrect logger in user_configures_targeting_rules loop"
        ),
        
        # Fix: verifyTheStaticNPINumbersFromTheUploadedFileAreDisplayedCorrectlyInTheListDetailsPage
        (
            r'(if \(fileName\.contains\("StaticList"\)\) \{\s*)logger\.info\("Verifying: the NPI Numbers from the uploaded file \{\} are displayed correctly in the list details page", fileName\);',
            r'\1',
            "Remove duplicate logger in verifyTheStaticNPINumbersFromTheUploadedFileAreDisplayedCorrectlyInTheListDetailsPage"
        ),
        
        # Fix: verifyTheTemplateCreatedAreSaved
        (
            r'(public void verifyTheTemplateCreatedAreSaved\(\) \{)\s*logger\.info\("Verifying: the template created are saved"\);',
            r'\1\n        logger.info("Verifying template is saved");',
            "Fix logger in verifyTheTemplateCreatedAreSaved"
        ),
        
        # Fix: verifyCreativeLibraryPageIsDisplayed
        (
            r'(public void verifyCreativeLibraryPageIsDisplayed\(\) \{)\s*logger\.info\("User clicks Creative Library options present under Activation tab"\);',
            r'\1\n        logger.info("Verifying Creative Library page is displayed");',
            "Fix logger in verifyCreativeLibraryPageIsDisplayed"
        ),
        
        # Fix: verifyTheFollowingSortOptionsAreAvailableAndWorking
        (
            r'(for \(String sortOption : sortOptionsList\) \{\s*)logger\.info\("Verifying: the following sort options are available and working", sortOptions\);',
            r'\1',
            "Remove incorrect logger in verifyTheFollowingSortOptionsAreAvailableAndWorking loop"
        ),
        
        # Fix: verifySearchBoxIsAvailableAndWorking - two loggers
        (
            r'(public void verifySearchBoxIsAvailableAndWorking\(DataTable searchValues\) \{)\s*logger\.info\("Verify Search Box is available and working", searchValues\);\s*(List<String> searchValuesList = searchValues\.asList\(String\.class\);\s*createCreatives\.clickActivityButton\("Active"\);\s*for \(String searchValue : searchValuesList\) \{\s*)logger\.info\("Verifying: search Box is available and working", searchValues\);',
            r'\1\n        logger.info("Verifying Search Box is available and working");\n        \2',
            "Remove duplicate logger in verifySearchBoxIsAvailableAndWorking"
        ),
        
        # Fix: userMakesListAvailableInLifeAndHCP365AndClicksOnNext
        (
            r'(public void userMakesListAvailableInLifeAndHCP365AndClicksOnNext\(\) \{)\s*logger\.info\("User makes list available in LIFE and HCP365 and clicks on next"\);',
            r'\1\n        logger.info("Making list available in LIFE and HCP365 and clicking Next");',
            "Fix logger in userMakesListAvailableInLifeAndHCP365AndClicksOnNext"
        ),
    ]
    
    for old_pattern, new_pattern, description in fixes:
        if re.search(old_pattern, content, re.DOTALL):
            content = re.sub(old_pattern, new_pattern, content, flags=re.DOTALL)
            fixes_count += 1
            print(f"  ✓ {description}")
        else:
            print(f"  ✗ Pattern not found: {description}")
    
    return content, fixes_count

def main():
    filepath = 'src/test/java/stepdefinitions/LifeSteps.java'
    
    print("Reading file...")
    content = read_file(filepath)
    
    print("\nApplying regex-based fixes...")
    content, regex_fixes = apply_fixes(content)
    
    print(f"\nWriting file...")
    write_file(filepath, content)
    
    print(f"\n✅ Total fixes applied: {regex_fixes}")

if __name__ == '__main__':
    main()

