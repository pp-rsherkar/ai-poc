Feature: LIFE Regression - Targetings
  It ensures creation of a campaign with different Targeting Rules:
  1. Verify all Targeting Rules under categories and create a campaign by adding all Targeting Rules
  2. Verify list of Targeting Rules available under various LI types
  3. Verify the created targeting template is available on Targeting Templates page
  4. Verify the count of rules added for the selected targeting rule type on the tactic settings page

  @regression
  Scenario Outline: Verify all Targeting Rules under categories and create a campaign by adding all Targeting Rules
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "<CHANNEL>" as channel
    Then Verify targeting panel with all targeting under below categories
      | AUDIENCE ATTRIBUTE |
      | HEALTH JOURNEY     |
      | DEMOGRAPHICS       |
      | CONTEXTUAL         |
      | GEOGRAPHY          |
      | MEDIA SUPPLY       |
      | LEGAL TARGETINGS   |
    And Verify target type with respect to category
      | AUDIENCE ATTRIBUTE | Behavioral Segment,NPI,NPI Facility Affiliation,Retargeting Pixels,HCP by Specialty,Health Populations,OTC Populations,IP Address,Clickers,Converters,Keyword Populations,Practice Staff,Email,Sensitive Areas,Lookalike Audience |
      | HEALTH JOURNEY     | Health Populations+,In Condition                                                                                                                                                                                                  |
      | DEMOGRAPHICS       | Age,Ethnicity,Gender                                                                                                                                                                                                              |
      | CONTEXTUAL         | Health Pages,IAB Categories,Keywords,Language,Endemics                                                                                                                                                                            |
      | GEOGRAPHY          | Geo Targets,Geo Radius,Postal Codes,Area Codes,Weather Signals                                                                                                                                                                    |
      | MEDIA SUPPLY       | Brand Safety Profile,Brand Suitability,Browser,Curated Markets,Custom Targeting Bundle,Deal Groups,Device,Domains/Apps,IAS Context Control,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals,Viewability      |
      | LEGAL TARGETINGS   | Legal Pages,Legal Populations                                                                                                                                                                                                     |
    And User configures targeting rules as below
      | Behavioral Segment       | AutoSegment18577650                                                   |
      | NPI                      | AutoSmartList954103283                                                |
      | HCP by Specialty         | Radiology, Aerospace Medicine                                         |
      | Health Populations       | Anesthesia and Analgesia                                              |
      | Keyword Populations      | CustomTextForKeywordPopulations, KeywordPopulationsTest               |
      | Practice Staff           | SMART_Pixel_NPI_20250701_155147                                       |
      | Health Pages             | Animal Diseases                                                       |
      | Keywords                 | Custom_Keyword, TestingKeyword, Qwerty123                             |
      | Endemics                 | Endemic                                                               |
      | Geo Targets              | New York, California                                                  |
      | Postal Codes             | 123456, 10001, 987654                                                 |
      | Weather Signals          | Below 15F degrees, Outdoor Activity                                   |
      | Brand Safety Profile     | 51246802                                                              |
      | Brand Suitability        | Unknown Brand Safety, Highly Illicit Do Not Monetize                  |
      | Browser                  | Chrome, EDGE, Opera, Safari                                           |
      | Device                   | Mobile, Tablet, Connected Device                                      |
      | Domains/Apps             | APP Regular, updaedList106043912                                      |
      | Inventory Source         | Reporttest                                                          |
      | Operating System         | Windows, macOS, Blackberry                                            |
      | Viewability              | 50                                                                    |
      | Legal Pages              | Emancipation                                                          |
      | Legal Populations        | Adoption                                                              |
      | NPI Facility Affiliation | NEW AGE DERMATOLOGY CENTER PA (NC)                                    |
      | Retargeting Pixels       | Retargeting_20250814_011101                                           |
      | OTC Populations          | Dental/Oral Care                                                      |
      | IP                       | AutoIP101602041                                                       |
      | Clickers                 | Auto_20260506_153916                                                  |
      | Email                    | AutoEmail120220716113986417                                           |
      | Sensitive Areas          | Anxiety Disorders                                                     |
      | IAB Categories           | Agriculture                                                           |
      | IAB Categories New       | Communication                                                         |
      | Language                 | English, Spanish                                                      |
      | Custom Targeting Bundle  | 203397, 203396                                                        |
#      | IAS Context Control      | Pollution                                                             |
      | Invalid Traffic          | Sites/Apps with Insufficient Fraud & IVT Stats, Fraudulent Sites/Apps |
      | Inventory Type           | App, Site                                                             |
      | Health Populations+      | Dental Polishing, Dental Cavity Preparation                           |
      | In Condition             | Liver Diseases                                                        |
      | Bespoke                  | AutoSegment384105361                                                  |
      | Ethnicity                | Asian, Arab                                                           |
      | Gender                   | Male                                                                  |
      | Age                      | 25-29, 35-39                                                          |
      | Geo Radius               | 35.5::122.42::400::California                                         |
    Then Verify the configured targeting rules
    And Verify the count of rules added for the selected targeting rule type on the Tactic Settings page
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    And User saves tactic details as a target template "Display" and verifies the template is saved successfully
    Then Verify the newly created campaign is in running state
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name
    When User navigates to Targeting template page by clicking the icon from Activation section
    Then User searches and verifies the created targeting template is available on Targeting Templates page
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | CREATIVE      |
      | 01- Advertiser | Test    | Regular | 10000     | Line      | 120         | Tactic      | Display Advanced | Auto_Creative |

  @regression
  Scenario Outline: Verify list of Targeting Rules available under Video Targeting Category and create a campaign by adding selected Targeting Rules
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>" "<LINE_ITEMS>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "<CHANNEL>" as channel
    Then Verify targeting panel with all targeting under below categories
      | AUDIENCE ATTRIBUTE |
      | HEALTH JOURNEY     |
      | DEMOGRAPHICS       |
      | CONTEXTUAL         |
      | GEOGRAPHY          |
      | MEDIA SUPPLY       |
      | Video              |
      | LEGAL TARGETINGS   |
    And Verify target type with respect to category
      | AUDIENCE ATTRIBUTE | Behavioral Segment,NPI,NPI Facility Affiliation,Retargeting Pixels,HCP by Specialty,Health Populations,OTC Populations,IP Address,Clickers,Converters,Keyword Populations,Practice Staff,Sensitive Areas,Lookalike Audience  |
      | HEALTH JOURNEY     | Health Populations+,In Condition                                                                                                                                                                                             |
      | DEMOGRAPHICS       | Age,Ethnicity,Gender                                                                                                                                                                                                         |
      | CONTEXTUAL         | Health Pages,IAB Categories,IAB Categories New, Keywords,Language,Endemics                                                                                                                                                   |
      | GEOGRAPHY          | Geo Targets,Geo Radius,Postal Codes,Area Codes,Weather Signals                                                                                                                                                               |
      | MEDIA SUPPLY       | Brand Safety Profile,Brand Suitability,Browser,Curated Markets,Custom Targeting Bundle,Deal Groups,Device,Domains/Apps,IAS Context Control,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals,Viewability |
      | Video              | Video Size,Video Placement,Video Skipping                                                                                                                                                                                    |
      | LEGAL TARGETINGS   | Legal Pages,Legal Populations                                                                                                                                                                                                |
    And User configures targeting rules as below
      | Video Size      | Small, Large                          |
      | Video Placement | Interstitial, Accompanying Content    |
      | Video Skipping  | Skippable and Non-Skippable Inventory |
    Then Verify the configured targeting rules
    And Verify the count of rules added for the selected targeting rule type on the Tactic Settings page
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    And User saves tactic details as a target template "<LINE_ITEMS>" and verifies the template is saved successfully
    Then Verify the newly created campaign is in running state
    When User navigates to Targeting template page by clicking the icon from Activation section
    Then User searches and verifies the created targeting template is available on Targeting Templates page
    Examples:
      | ADVERTISER     | CP_NAME       | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | LINE_ITEMS | TACTIC_NAME | CHANNEL        | CREATIVE      |
      | 01- Advertiser | External_Auto | Regular | 10000     | Line      | 500         | Video      | Tactic      | Video Advanced | Auto_Creative |

  @regression
  Scenario Outline: Verify list of Targeting Rules available under Native Video Targeting Category and create a campaign by adding selected Targeting Rules
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>" "<LINE_ITEMS>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    And User clicks on Add Targeting Rule
    Then Verify targeting panel with all targeting under below categories
      | AUDIENCE ATTRIBUTE |
      | HEALTH JOURNEY     |
      | DEMOGRAPHICS       |
      | CONTEXTUAL         |
      | GEOGRAPHY          |
      | MEDIA SUPPLY       |
      | Video              |
      | LEGAL TARGETINGS   |
    And Verify target type with respect to category
      | AUDIENCE ATTRIBUTE | Behavioral Segment,NPI,NPI Facility Affiliation,Retargeting Pixels,HCP by Specialty,Health Populations,OTC Populations,IP Address,Clickers,Converters,Keyword Populations,Practice Staff,Sensitive Areas,Lookalike Audience  |
      | HEALTH JOURNEY     | Health Populations+,In Condition                                                                                                                                                                                             |
      | DEMOGRAPHICS       | Age,Ethnicity,Gender                                                                                                                                                                                                         |
      | CONTEXTUAL         | Health Pages,IAB Categories,IAB Categories New, Keywords,Language,Endemics                                                                                                                                                   |
      | GEOGRAPHY          | Geo Targets,Geo Radius,Postal Codes,Area Codes,Weather Signals                                                                                                                                                               |
      | MEDIA SUPPLY       | Brand Safety Profile,Brand Suitability,Browser,Curated Markets,Custom Targeting Bundle,Deal Groups,Device,Domains/Apps,IAS Context Control,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals,Viewability |
      | Video              | Video Size,Video Placement                                                                                                                                                                                                   |
      | LEGAL TARGETINGS   | Legal Pages,Legal Populations                                                                                                                                                                                                |
    And User configures targeting rules as below
      | Video Size      | Small, Large                               |
      | Video Placement | NoContent/Standalone, Accompanying Content |
    Then Verify the configured targeting rules
    And Verify the count of rules added for the selected targeting rule type on the Tactic Settings page
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    And User saves tactic details as a target template "<LINE_ITEMS>" and verifies the template is saved successfully
    Then Verify the newly created campaign is in running state
    When User navigates to Targeting template page by clicking the icon from Activation section
    Then User searches and verifies the created targeting template is available on Targeting Templates page
    Examples:
      | ADVERTISER     | CP_NAME       | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | LINE_ITEMS   | TACTIC_NAME | CREATIVE      |
      | 01- Advertiser | External_Auto | Regular | 10000     | Line      | 500         | Native Video | Tactic      | Auto_Creative |

  @regression
  Scenario Outline: Verify list of Targeting Rules available under Search Extension Targeting Category and create a campaign by adding selected Targeting Rules
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>" "<LINE_ITEMS>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    And User clicks on Add Targeting Rule
    Then Verify targeting panel with all targeting under below categories
      | SEARCH SPECIFIC    |
      | AUDIENCE ATTRIBUTE |
      | HEALTH JOURNEY     |
      | DEMOGRAPHICS       |
      | CONTEXTUAL         |
      | GEOGRAPHY          |
      | MEDIA SUPPLY       |
      | Video              |
      | LEGAL TARGETINGS   |
    And Verify target type with respect to category
      | SEARCH SPECIFIC    | Search Keywords                                                                                                                                                                                                              |
      | AUDIENCE ATTRIBUTE | Behavioral Segment,NPI,NPI Facility Affiliation,Retargeting Pixels,HCP by Specialty,Health Populations,OTC Populations,IP Address,Clickers,Converters,Keyword Populations,Practice Staff,Sensitive Areas,Lookalike Audience  |
      | HEALTH JOURNEY     | Health Populations+,In Condition                                                                                                                                                                                             |
      | DEMOGRAPHICS       | Age,Ethnicity,Gender                                                                                                                                                                                                         |
      | CONTEXTUAL         | Health Pages,IAB Categories,IAB Categories New, Keywords,Language,Endemics                                                                                                                                                   |
      | GEOGRAPHY          | Geo Targets,Geo Radius,Postal Codes,Area Codes,Weather Signals                                                                                                                                                               |
      | MEDIA SUPPLY       | Brand Safety Profile,Brand Suitability,Browser,Curated Markets,Custom Targeting Bundle,Deal Groups,Device,Domains/Apps,IAS Context Control,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals,Viewability |
      | LEGAL TARGETINGS   | Legal Pages,Legal Populations                                                                                                                                                                                                |
    And User configures targeting rules as below
      | Search Keywords | Pandemic, Intestine |
    Then Verify the configured targeting rules
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    And User saves tactic details as a target template "<LINE_ITEMS>" and verifies the template is saved successfully
    Then Verify the newly created campaign is in running state
    When User navigates to Targeting template page by clicking the icon from Activation section
    Then User searches and verifies the created targeting template is available on Targeting Templates page
    Examples:
      | ADVERTISER     | CP_NAME       | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | LINE_ITEMS       | TACTIC_NAME | CREATIVE      |
      | 01- Advertiser | External_Auto | Regular | 10000     | Line      | 500         | Search Extension | Tactic      | Auto_Creative |

  @regression
  Scenario Outline: Verify list of Targeting Rules available under DOOH Targeting Category and create a campaign by adding selected Targeting Rules
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>" "<LINE_ITEMS>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    And User clicks on Add Targeting Rule
    Then Verify targeting panel with all targeting under below categories
      | AUDIENCE ATTRIBUTE |
      | GEOGRAPHY          |
      | MEDIA SUPPLY       |
    And Verify target type with respect to category
      | AUDIENCE ATTRIBUTE | IP Address                                                                                           |
      | GEOGRAPHY          | Geo Targets,Geo Radius,Postal Codes,Area Codes,Venue Type,Weather Signals                            |
      | MEDIA SUPPLY       | Audience Multiplier,Curated Markets,Custom Targeting Bundle,Deal Groups,Device,Inventory Source,Deals |
    And User configures targeting rules as below
      | Venue Type              | Transit, Retail, Office Buildings |
      | Custom Targeting Bundle | 203397, 203396                    |
      | Audience Multiplier     | 6-500                             |
    Then Verify the configured targeting rules
    And Verify the count of rules added for the selected targeting rule type on the Tactic Settings page
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    And User saves tactic details as a target template "<LINE_ITEMS>" and verifies the template is saved successfully
    Then Verify the newly created campaign is in running state
    When User navigates to Targeting template page by clicking the icon from Activation section
    Then User searches and verifies the created targeting template is available on Targeting Templates page
    Examples:
      | ADVERTISER     | CP_NAME       | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | LINE_ITEMS | TACTIC_NAME | CREATIVE      |
      | 01- Advertiser | Campaign_DOOH | Regular | 10000     | Line      | 500         | DOOH       | Tactic      | Auto_Creative |

  @regression
  Scenario Outline: Verify list of Targeting Rules available under Audio Targeting Category and create a campaign by adding selected Targeting Rules
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>" "<LINE_ITEMS>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    And User clicks on Add Targeting Rule
    Then Verify targeting panel with all targeting under below categories
      | AUDIENCE ATTRIBUTE |
      | HEALTH JOURNEY     |
      | DEMOGRAPHICS       |
      | GEOGRAPHY          |
      | MEDIA SUPPLY       |
      | LEGAL TARGETINGS   |
    And Verify target type with respect to category
      | AUDIENCE ATTRIBUTE | Behavioral Segment,NPI,NPI Facility Affiliation,Retargeting Pixels,HCP by Specialty,Health Populations,OTC Populations,IP Address,Clickers,Converters,Keyword Populations,Practice Staff,Sensitive Areas,Lookalike Audience |
      | HEALTH JOURNEY     | Health Populations+,Bespoke,In Condition                                                                                                                                                                                    |
      | DEMOGRAPHICS       | Age,Ethnicity,Gender                                                                                                                                                                                                        |
      | GEOGRAPHY          | Geo Targets,Geo Radius,Postal Codes,Area Codes,Weather Signals                                                                                                                                                              |
      | MEDIA SUPPLY       | Brand Safety Profile,Brand Suitability,Browser,Curated Markets,Custom Targeting Bundle,Deal Groups,Device,Domains/Apps,IAS Context Control,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals            |
      | LEGAL TARGETINGS   | Legal Populations                                                                                                                                                                                                           |
    And User configures targeting rules as below
      | Clickers | DomainList_Campaign_20260503_001635, TargetingTemplate_20260503_003625 |
      | Age      | 18-24, 50-54, 60-64                                     |
    Then Verify the configured targeting rules
    And Verify the count of rules added for the selected targeting rule type on the Tactic Settings page
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    And User saves tactic details as a target template "<LINE_ITEMS>" and verifies the template is saved successfully
    Then Verify the newly created campaign is in running state
    When User navigates to Targeting template page by clicking the icon from Activation section
    Then User searches and verifies the created targeting template is available on Targeting Templates page
    Examples:
      | ADVERTISER     | CP_NAME        | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | LINE_ITEMS | TACTIC_NAME | CREATIVE      |
      | 01- Advertiser | Campaign_Audio | Regular | 10000     | Line      | 500         | Audio      | Tactic      | Auto_Creative |

  @todo
  # Source: ET-24730
  Scenario: Health Pages targeting lists the seven productized branches and supports full tier navigation
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    And User clicks on Add Targeting Rule
    # Framework Gap: Requires step definition for the MeSH 2025 Health Pages branch picker in LifeSteps.java
    Then Health Pages targeting lists exactly the seven branches "Diseases & Conditions, Drugs & Substances, Diagnostics & Treatments, Mental Health & Behavior, Biological Processes, Healthcare Professions, Health Care System" with no legacy groupings or duplicate nodes
    # Framework Gap: Requires step definition for tree drill-down/expand-to-leaf in LifeSteps.java
    When User drills from branch "Diseases & Conditions" down to a leaf descriptor at tier depth "13"
    Then Every tier through the deepest level expands and the level-13 descriptor is selectable with no cap truncating the path at 10
    When User selects a mid-tier branch node at tier depth "3" instead of a leaf
    Then The rule targets the branch subtree consistently with the taxonomy's descendant-inclusion definition

  @todo
  # Source: ET-24730, AMB-2
  Scenario Outline: Searching the Health Pages tree unwraps or folds results per the resolved depth thresholds
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    And User clicks on Add Targeting Rule
    # Framework Gap: Requires step definitions for search unwrap-depth and fold-collapse behavior in LifeSteps.java
    When User searches the Health Pages tree for a descriptor at "<SEARCH_DEPTH>"
    Then "<EXPECTED_UNWRAP_FOLD_BEHAVIOR>"
    Examples:
      | SEARCH_DEPTH                   | EXPECTED_UNWRAP_FOLD_BEHAVIOR                                                                                |
      | level 6 match                  | Tree auto-expands the ancestor path down to the match and highlights the matched node                        |
      | level 8 match                  | Levels beyond the fold boundary collapse into a single fold marker reachable via hover underline and pointer |
      | fold click on a level 8 result | Clicking the collapsed fold expands every nested level beneath it to reveal the match and its ancestors      |
      | no matching descriptor         | No nodes are expanded, no fold marker appears, and an empty/no-results state is shown                        |

  @todo
  # Source: ET-24730, GAP-1
  Scenario Outline: Live legacy-targeting rules remap onto the new MeSH 2025 taxonomy without silent targeting loss
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definition for legacy-to-new-descriptor remap resolution in LifeSteps.java
    When A live tactic targeting rule reproduces the "<LEGACY_RULE_CASE>" condition post-cutover
    Then "<EXPECTED_REMAP_OUTCOME>"
    Examples:
      | LEGACY_RULE_CASE                                | EXPECTED_REMAP_OUTCOME                                                                                          |
      | Legacy code with a defined 1:1 mapping          | Rule references the new descriptor and evaluates the same intended pages; targeting is not lost                 |
      | Legacy code with no automatic mapping           | Rule appears in the remap review queue and retains a resolvable state; targeting does not silently go empty     |
      | Legacy code mapping to multiple new descriptors | Remap applies the defined selection rule deterministically and the outcome is recorded for product confirmation |

  @todo
  # Source: ET-24730
  Scenario: The MeSH 2025 taxonomy cutover is fully reversible with no downtime during the bake period
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definition toggling the MeSH 2025 feature flag in LifeSteps.java
    When The MeSH 2025 feature flag is set to "100%"
    Then Picker, bid-time matching, and reporting all reference the new MeSH-2025 taxonomy
    When The MeSH 2025 feature flag is set to "OFF" during the bake period
    Then The legacy taxonomy path still resolves and campaigns keep targeting with no downtime on toggle

  @todo
  # Source: ET-24730
  Scenario: New-taxonomy descriptors resolve consistently across AI search, Media Planner, and Ad Manager
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definitions for AI search / Media Planner / Ad Manager MeSH 2025 parity checks in LifeSteps.java
    When User runs an AI search query for a new-taxonomy category "Mental Health & Behavior"
    Then AI search surfaces the correct new-taxonomy descriptor and it is selectable for targeting
    And Media Planner lists the same seven productized branches and resolves the same new descriptors as Portal
    And Ad Manager targeting on a Health Pages rule resolves and serves against the new-taxonomy descriptors consistently with Portal
    When A sample page categorized to the targeted descriptor is evaluated at bid time on a staging tactic
    Then Bid-time evaluation produces a match on the sampled live tactic in staging

  @todo
  # Source: ET-24730
  # Regression anchor: HT-6056, HT-6100 - LIFE tactics delivering on empty/mismatched targeting values
  Scenario: A migrated tactic does not begin delivering on empty or mismatched targeting after cutover
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definition reproducing the HT-6056/HT-6100 migrated-tactic condition in LifeSteps.java
    When A migrated tactic reproduces the HT-6056/HT-6100 empty-targeting condition post-cutover
    Then Delivery occurs only against pages matching the remapped descriptor and no impressions serve on empty or mismatched targeting

  @todo
  # Source: ET-24719, GAP-1, GAP-2, GAP-3
  Scenario Outline: Keyword and Keyword Population search filters the list and highlights matches like Media Planner
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    And User clicks on Add Targeting Rule for "<TARGETING_TYPE>"
    # Framework Gap: Requires step definitions for Condition Keyword Search match/highlight/removal behavior in LifeSteps.java
    When User searches "<SEARCH_TERM>" in the "<TARGETING_TYPE>" targeting search box
    Then "<MATCH_RULE_CHECK>" and non-matching records are removed from the list
    Examples:
      | TARGETING_TYPE     | SEARCH_TERM                              | MATCH_RULE_CHECK                                                           |
      | Keyword            | asthma                                   | Only matching records remain and the matched text is highlighted           |
      | Keyword Population | diabetes                                 | Only matching records remain and the matched text is highlighted           |
      | Keyword            | diab                                     | Substring match is applied consistently with Media Planner                 |
      | Keyword            | ASTHMA                                   | Search is case-insensitive and returns the same records as "asthma"        |
      | Keyword            | a term matching only a nested child node | The matched child is shown with enough parent context to remain selectable |

  @todo
  # Source: ET-24719
  Scenario: Clearing the Keyword search restores the full list and a no-match search shows an empty state
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    And User clicks on Add Targeting Rule for "Keyword"
    When User searches "asthma" in the Keyword targeting search box
    And User clears the search box
    Then All previously removed records return and highlighting is cleared
    When User searches "zzzznotarealterm" in the Keyword targeting search box
    Then The list shows no records instead of the full unfiltered list
    When User searches a punctuation-only string in the Keyword targeting search box
    Then No crash occurs and the list stays consistent per the defined punctuation-handling rule

  @todo
  # Source: ET-24719, AMB-2
  Scenario: A keyword selected before searching remains selected after the search that excludes it is cleared
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    And User clicks on Add Targeting Rule for "Keyword"
    And User selects the keyword "Custom_Keyword"
    When User searches "TestingKeyword" in the Keyword targeting search box, excluding the selected keyword
    And User clears the search box
    Then The keyword "Custom_Keyword" remains selected after clearing

  @todo
  # Source: ET-24719
  Scenario: Life keyword search returns the same result set as Media Planner for an identical query
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definition comparing Life and Media Planner keyword-search result sets in LifeSteps.java
    When The same keyword search query is run in Life Keyword targeting and in Media Planner
    Then The filtered record sets match between Life and Media Planner

  @todo
  # Source: ET-24719
  Scenario: Keywords selected while a search filter is active save correctly to the tactic targeting
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    And User clicks on Add Targeting Rule for "Keyword"
    When User searches "Custom_Keyword" in the Keyword targeting search box
    And User selects two matching keywords from the filtered list
    And User saves the settings
    Then Both selected keywords persist on the tactic targeting regardless of the active filter at save time
