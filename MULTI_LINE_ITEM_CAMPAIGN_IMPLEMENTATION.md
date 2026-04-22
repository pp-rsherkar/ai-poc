# Multi-Line Item Campaign Implementation

## Overview
This implementation provides a comprehensive test scenario for creating campaigns with multiple line items of different types, each containing multiple tactics with various targeting rule configurations.

## Scenario Description
The test scenario creates a complex campaign structure:
- **5 Line Item Types**: Display, Audio, Video, Native Display, Native Video
- **3 Tactics per Line Item**: Each with unique configurations
- **Multiple Targeting Rules per Tactic**: 3 different rule types per tactic
- **Shared Creative**: All 15 tactics use the same creative

Total: **5 line items × 3 tactics = 15 tactics** with diverse targeting configurations

## Feature File Location
`src/test/resources/features/life/Create_Campaign.feature`

### Scenario: Create Campaign with Multiple Line Item Types and Multiple Tactics with Different Rule Types

## Implementation Details

### 1. Feature File Structure
The scenario uses a data table to define:
- Line item type (Display, Audio, Video, Native Display, Native Video)
- Line item name and budget
- Three tactics per line item with:
  - Tactic name
  - Channel configuration
  - Comma-separated list of targeting rule types

### 2. Step Definitions
Located in: `src/test/java/stepdefinitions/LifeSteps.java`

#### Main Step Method
```java
@When("User creates multiple line items with different types and tactics as below")
public void userCreatesMultipleLineItemsWithDifferentTypesAndTacticsAsBelow(DataTable dataTable)
```

**Functionality:**
- Iterates through each row in the data table
- Creates line items with specified type and attributes
- For each line item, creates 3 tactics with different configurations
- Handles navigation between line items

#### Helper Method: createMultipleTacticsForLineItem
```java
private void createMultipleTacticsForLineItem(Map<String, String> row)
```

**Functionality:**
- Creates 3 tactics for the current line item
- Configures channel for each tactic
- Adds multiple targeting rules to each tactic
- Each tactic has unique targeting rule types

#### Helper Method: selectRuleOptions
```java
private void selectRuleOptions(String ruleType)
```

**Functionality:**
- Handles selection of options for different rule types
- Supports all major targeting categories:
  - Audience Attributes (Behavioral Segment, NPI, Health Populations, etc.)
  - Health Journey (In Condition, Health Populations+)
  - Demographics (Age, Gender, Ethnicity)
  - Contextual (Health Pages, Keywords, IAB Categories, Language)
  - Geography (Geo Targets, Postal Codes)
  - Media Supply (Device, Browser, OS, etc.)
  - Legal Targetings (Legal Populations, Legal Pages)

### 3. Page Object Methods
Located in: `src/main/java/pages/life/TacticSettings.java`

#### New Method: selectFirstAvailableOption
```java
public void selectFirstAvailableOption()
```

**Functionality:**
- Waits for options to load
- Selects the first available option for any targeting rule type
- Handles different UI patterns for option selection

## Targeting Rules Configuration

### Line Item Type 1: Display
- **Tactic A**: Behavioral Segment, Age, Health Pages
- **Tactic B**: In Condition, Gender, Keywords
- **Tactic C**: Device, Postal Codes, Legal Populations

### Line Item Type 2: Audio
- **Tactic A**: Health Populations, Ethnicity, IAB Categories
- **Tactic B**: NPI, Age, Language
- **Tactic C**: Geo Targets, Browser, Brand Safety Profile

### Line Item Type 3: Video
- **Tactic A**: Retargeting Pixels, Gender, Domains/Apps
- **Tactic B**: Behavioral Segment, Health Pages, Device
- **Tactic C**: Postal Codes, Operating System, Legal Pages

### Line Item Type 4: Native Display
- **Tactic A**: In Condition, Age, Curated Markets
- **Tactic B**: Health Populations, Keywords, Inventory Type
- **Tactic C**: NPI, Ethnicity, Viewability

### Line Item Type 5: Native Video
- **Tactic A**: Behavioral Segment, Gender, Geo Targets
- **Tactic B**: In Condition, Device, Invalid Traffic
- **Tactic C**: Age, IAB Categories, Deal Group

## Creative Assignment

The final step assigns a single creative (`Auto_Creative`) to all 15 tactics:

```java
@And("User assigns the existing creative named {string} to all tactics")
public void userAssignsTheExistingCreativeNamedToAllTactics(String creativeName)
```

## Verification

The scenario concludes with verification:

```java
@Then("Verify all tactics are enabled and campaign is in running state")
public void verifyAllTacticsAreEnabledAndCampaignIsInRunningState()
```

**Verifies:**
- All tactics are successfully created and enabled
- Campaign status is "Running"
- All configurations are properly saved

## Usage

### Running the Test
```bash
mvn test -Dcucumber.filter.tags="@regression" -Dcucumber.features="src/test/resources/features/life/Create_Campaign.feature:391"
```

### Prerequisites
- Java 21
- Maven 3.x
- Valid test account credentials
- Existing creative named "Auto_Creative" in the system
- Access to Demo environment

## Benefits of This Implementation

1. **Comprehensive Coverage**: Tests multiple line item types in a single scenario
2. **Diverse Rule Configurations**: Each tactic has different targeting rules
3. **Realistic Scenario**: Mimics real-world complex campaign setups
4. **Reusable Components**: Helper methods can be reused for similar scenarios
5. **Maintainable**: Clear separation of concerns and well-documented code

## Future Enhancements

Potential improvements:
1. Add validation for each targeting rule configuration
2. Verify budget allocation across line items
3. Test different cost models per line item type
4. Add negative test scenarios (invalid configurations)
5. Implement parallel tactic creation for performance testing

## Notes

- Line items are created sequentially (one after another)
- Tactics within each line item are also created sequentially
- Each tactic name and line item name gets a unique timestamp
- The implementation uses existing page object methods for maximum code reuse
- Error handling is built into the page object layer
