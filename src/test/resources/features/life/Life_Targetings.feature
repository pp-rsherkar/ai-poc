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

  # Source: ET-24730
  @todo
  Scenario: Health Pages targeting renders the MeSH 2025 taxonomy with deep nesting and stable margins
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Health Pages"
    # Framework Gap: Requires step definitions for verifying the MeSH 2025 descriptor count on the Health Pages targeting in LifeSteps.java
    Then Verify the Health Pages targeting displays the MeSH 2025 taxonomy with approximately "13000" descriptors
    # Framework Gap: Requires step definitions for verifying taxonomy tree tier depth on expansion in LifeSteps.java
    And Verify the Health Pages taxonomy tree expands to at least "10" tiers
    # Framework Gap: Requires step definitions for verifying reduced per-level indentation margin increments in LifeSteps.java
    Then Verify the taxonomy tree reduces the indentation margin increment per level beyond level "4"
    # Framework Gap: Requires step definitions for verifying deep-nesting layout has no overflow or clipping in LifeSteps.java
    And Verify descriptors nested "8" levels deep render without overflow or clipping and remain legible
    # Framework Gap: Requires step definitions for rapidly expanding and collapsing taxonomy tree nodes in LifeSteps.java
    When User rapidly expands and collapses the taxonomy tree nodes
    # Framework Gap: Requires step definitions for verifying taxonomy tree render stability in LifeSteps.java
    Then Verify the taxonomy tree renders without errors or freezing

  # Source: ET-24730
  @todo
  Scenario: Health Pages search highlight persists across expansion states and updates with the search term
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Health Pages"
    # Framework Gap: Requires step definitions for searching the Health Pages taxonomy by term in LifeSteps.java
    When User searches the Health Pages taxonomy for "cardiovascular"
    # Framework Gap: Requires step definitions for verifying search highlight persistence across expand and collapse states in LifeSteps.java
    Then Verify the search term "cardiovascular" remains highlighted across expanded and collapsed states
    # Framework Gap: Requires step definitions for changing the Health Pages search term in LifeSteps.java
    When User changes the Health Pages search term to "oncology"
    # Framework Gap: Requires step definitions for verifying search highlight updates with the new term in LifeSteps.java
    Then Verify the highlight updates to match the search term "oncology"

  # Source: ET-24730
  @todo
  Scenario: Health Pages typeahead returns MeSH 2025 results within threshold and shows an empty state for no match
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Health Pages"
    # Framework Gap: Requires step definitions for entering a term in the Health Pages typeahead in LifeSteps.java
    When User enters "Hypertension" in the Health Pages typeahead
    # Framework Gap: Requires step definitions for verifying typeahead result latency threshold in LifeSteps.java
    Then Verify MeSH 2025 typeahead results are returned within "2" seconds
    # Framework Gap: Requires step definitions for searching the Health Pages taxonomy by term in LifeSteps.java
    When User searches the Health Pages taxonomy for a non-existent descriptor "xyzabc123"
    # Framework Gap: Requires step definitions for verifying the no-result empty state in LifeSteps.java
    Then Verify an empty state is displayed and no error is shown

  # Source: ET-24730
  @todo
  Scenario Outline: Selected Health Pages descriptors persist after saving and reopening the tactic
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Health Pages"
    # Framework Gap: Requires step definitions for selecting a number of descriptors across multiple taxonomy levels in LifeSteps.java
    When User selects "<DESCRIPTOR_COUNT>" descriptors across multiple taxonomy levels
    When User saves the settings
    # Framework Gap: Requires step definitions for reopening the saved tactic Health Pages targeting in LifeSteps.java
    When User reopens the saved tactic Health Pages targeting
    # Framework Gap: Requires step definitions for verifying the persisted descriptor count with no silent drop in LifeSteps.java
    Then Verify exactly "<DESCRIPTOR_COUNT>" descriptors persist with no silent drop
    Examples:
      | DESCRIPTOR_COUNT |
      | 10               |
      | 50               |

  # Source: ET-24730
  @todo
  Scenario: Selecting and deselecting a parent Health Pages descriptor propagates to its child descriptors
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Health Pages"
    # Framework Gap: Requires step definitions for selecting a parent descriptor with child descriptors in LifeSteps.java
    When User selects a parent descriptor with "20" child descriptors
    # Framework Gap: Requires step definitions for verifying the selected count includes child descriptors in LifeSteps.java
    Then Verify the selected count includes all "20" child descriptors
    # Framework Gap: Requires step definitions for deselecting a parent descriptor with child descriptors in LifeSteps.java
    When User deselects a parent descriptor with "10" child descriptors
    # Framework Gap: Requires step definitions for verifying child descriptors are removed on parent deselect in LifeSteps.java
    Then Verify all "10" child descriptors are removed from the selection

  # Source: ET-24730
  @todo
  Scenario: Health Pages handles legacy descriptors removed from the MeSH 2025 taxonomy without errors
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definitions for opening a saved tactic that contains a legacy Health Pages descriptor in LifeSteps.java
    Given User opens a saved tactic that contains a Health Pages descriptor removed in the MeSH 2025 taxonomy
    # Framework Gap: Requires step definitions for verifying a saved tactic with a removed descriptor loads gracefully in LifeSteps.java
    Then Verify the saved tactic loads without error and the removed descriptor is handled gracefully
    # Framework Gap: Requires step definitions for verifying removed descriptors are not selectable in the taxonomy in LifeSteps.java
    Then Verify the removed descriptor is not shown as a selectable option in the MeSH 2025 taxonomy

  # Source: ET-24730
  # HT-5112
  @todo
  Scenario: Regression - Health Pages, Health Populations and Health Populations+ all load together after MeSH 2025 migration
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    # Framework Gap: Requires step definitions for verifying multiple health targeting types load together in LifeSteps.java
    Then Verify Health Pages, Health Populations and Health Populations+ targeting types all load successfully
    # Framework Gap: Requires step definitions for verifying no targeting type fails leaving only Milkshake targeting in LifeSteps.java
    And Verify no targeting type fails to load leaving only Milkshake targeting available

  # Source: ET-24730
  # HT-4185
  @todo
  Scenario: Regression - Keyword Populations targeting is unaffected by the Health Pages MeSH 2025 changes
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Keyword Populations"
    # Framework Gap: Requires step definitions for verifying Keyword Populations behavior is unaffected by Health Pages changes in LifeSteps.java
    Then Verify Keyword Populations targeting loads and behaves as before the Health Pages MeSH 2025 changes

  # Source: ET-24719
  @todo
  Scenario: Keyword targeting search filters the list to matching items, highlights them, clears, and handles no matches
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Keywords"
    # Framework Gap: Requires step definitions for searching the Keyword targeting list by term in LifeSteps.java
    When User searches the Keyword targeting list for "hypertension"
    # Framework Gap: Requires step definitions for verifying the Keyword list is filtered to matches only in LifeSteps.java
    Then Verify only matching Keyword items are displayed and non-matching items are hidden
    # Framework Gap: Requires step definitions for searching the Keyword targeting list by term in LifeSteps.java
    When User searches the Keyword targeting list for "diabetes"
    # Framework Gap: Requires step definitions for verifying the search term is highlighted within filtered items in LifeSteps.java
    Then Verify the search term "diabetes" is highlighted within the filtered Keyword items
    # Framework Gap: Requires step definitions for clearing the Keyword targeting search in LifeSteps.java
    When User clears the Keyword targeting search
    # Framework Gap: Requires step definitions for verifying the full Keyword list is restored with no stale filter in LifeSteps.java
    Then Verify the full Keyword list is restored with no stale filter applied
    # Framework Gap: Requires step definitions for searching the Keyword targeting list by term in LifeSteps.java
    When User searches the Keyword targeting list for a non-matching term "xyzqrstabc"
    # Framework Gap: Requires step definitions for verifying the no-result empty state instead of the full list in LifeSteps.java
    Then Verify an empty state is displayed and the full list is not shown

  # Source: ET-24719
  @todo
  Scenario Outline: Keyword search matches partial words and is case-insensitive
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Keywords"
    # Framework Gap: Requires step definitions for searching the Keyword targeting list by term in LifeSteps.java
    When User searches the Keyword targeting list for "<SEARCH_TERM>"
    # Framework Gap: Requires step definitions for verifying the filtered results contain the expected matches in LifeSteps.java
    Then Verify the filtered Keyword results contain "<EXPECTED_MATCHES>"
    Examples:
      | SEARCH_TERM  | EXPECTED_MATCHES                    |
      | card         | cardiovascular, cardiology, cardiac |
      | Hypertension | hypertension                        |
      | hypertension | hypertension                        |

  # Source: ET-24719
  @todo
  Scenario: Keyword Population search filters identically to the Keyword list and matches Media Planner behavior
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Keyword Populations"
    # Framework Gap: Requires step definitions for searching the Keyword Population list by term in LifeSteps.java
    When User searches the Keyword Population list for "cardiovascular"
    # Framework Gap: Requires step definitions for verifying Keyword Population filtering matches the Keyword list in LifeSteps.java
    Then Verify the Keyword Population list filters to matching items identically to the Keyword targeting list
    # Framework Gap: Requires step definitions for verifying filter logic and result count match Media Planner in LifeSteps.java
    And Verify the Keyword search filter logic and result count match the Media Planner search

  # Source: ET-24719
  # HT-4185
  @todo
  Scenario: Items selected via filtered Keyword search persist and the Keyword Population count is preserved after save
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Keywords"
    # Framework Gap: Requires step definitions for searching the Keyword targeting list by term in LifeSteps.java
    When User searches the Keyword targeting list for "cardiovascular"
    # Framework Gap: Requires step definitions for selecting items from the filtered search results in LifeSteps.java
    When User selects "5" items from the filtered Keyword search results
    When User saves the settings
    # Framework Gap: Requires step definitions for verifying items selected via filtered search persist after save in LifeSteps.java
    Then Verify the "5" selected Keyword items persist after saving
    # Framework Gap: Requires step definitions for verifying the Keyword Population count is not reduced after save in LifeSteps.java
    Then Verify the Keyword Population count does not decrease after saving a list of "50" or more items

  # Source: ET-24719
  @todo
  Scenario: Health Pages taxonomy search is unaffected by the Keyword search filter change
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Test" "Regular" "10000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "120", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "Display Advanced" as channel
    When User clicks on Add Targeting Rule
    When User add new targeting rule for Rule Type "Health Pages"
    # Framework Gap: Requires step definitions for searching the Health Pages taxonomy by term in LifeSteps.java
    When User searches the Health Pages taxonomy for "cardiovascular"
    # Framework Gap: Requires step definitions for verifying Health Pages search still highlights without filtering in LifeSteps.java
    Then Verify the Health Pages search continues to highlight matches without filtering out non-matching descriptors
