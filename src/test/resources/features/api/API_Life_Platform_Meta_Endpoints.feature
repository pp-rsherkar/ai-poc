Feature: Life Platform Meta Endpoints - IAB Categories, Media Optimization Types, Creative Name Uniqueness Removal

  1. GET /api/v2/meta/iabCategories - discoverable hierarchical IAB Content Taxonomy for contextual targeting.
  2. GET /v2/meta/mediaOptimizationTypes - discoverable optimization types with restrictions and deprecation metadata.
  3. Creative name-uniqueness validation is removed from the creative create/update endpoints, aligning API behavior with the UI.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24244
  @todo
  Scenario Outline: GET /api/v2/meta/iabCategories returns hierarchical categories filtered per query parameters, with leafOnly=false returning all levels
    When User calls GET /api/v2/meta/iabCategories with "<PARAMS>"
    Then the response returns "<EXPECTED_RESULT>"
    Examples:
      | PARAMS                                         | EXPECTED_RESULT                                                                              |
      | includeChildren=true, limit=10, no tier filter | exactly 10 tier-1 parents, though total objects in data may exceed 10 due to nested children |
      | leafOnly=true                                  | only leaf (isLeaf=true) categories                                                           |
      | leafOnly=false                                 | all levels, leaf and non-leaf together, not "non-leaf only"                                  |
      | limit=-1                                       | the full bounded taxonomy (450 total per spec) with no truncation or timeout                 |

  # Source: ET-24244
  @todo
  Scenario: GET /api/v2/meta/iabCategories rejects or resolves conflicting parent-filter parameters with a structured error
    Given parentCategoryCode (string) and parentId (numeric) are both supplied in the same request
    Then confirm whether this is rejected with 422 or resolved by a defined precedence rule, since the two parameters' relationship is not reconciled in the source requirement
    Given an invalid parameter combination
    Then the response is 422 with a structured error body, and a wholly invalid parameter is 400

  # Source: ET-24242
  @todo
  Scenario: GET /v2/meta/mediaOptimizationTypes returns restriction and deprecation metadata that matches actual tactic create/update validation
    When User calls GET /v2/meta/mediaOptimizationTypes
    Then each optimization type includes value, description, deprecated, deprecatedDate (only if deprecated), replacedBy, and a restrictions array
    And each restriction includes restrictionType of CHANNEL, LINE_ITEM_TYPE, DEVICE_TYPE, or TARGETING, with restrictedValues
    And returned value strings use the normalized name the tactic create/update API accepts, for example "CTR" not "CTR %"
    And Clinical Behavior and Audience Quality are excluded from the response, since they moved to Adaptive Optimization
    Given a deprecated optimization type
    When User attempts to create or update a tactic using it via a direct API call
    Then the request is rejected, not merely flagged, confirming deprecation blocks input at the API level
    Given each restriction in the documented Restriction List, for example CTV blocking CPC/CTR
    Then the metadata matches the validation actually enforced by the tactic creation/update endpoints for that restriction

  # Source: ET-24241
  @todo
  Scenario Outline: Creative name-uniqueness validation is removed from the create and update endpoints for all three creative types
    Given a creative name that duplicates an existing creative's name
    When User creates a new creative via POST for "<CREATIVE_TYPE>"
    Then the request succeeds with no uniqueness error
    When User updates an existing creative via PUT for "<CREATIVE_TYPE>" to a name duplicating another creative's name
    Then the request succeeds with no uniqueness error
    And no other validation behavior on these endpoints is altered
    Examples:
      | CREATIVE_TYPE |
      | Display       |
      | Video         |
      | Native        |
