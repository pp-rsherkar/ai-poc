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

  # Source: ET-24248
  @todo
  Scenario: WebMD Health Markets targeting is fully removed from the UI, API, and feature flag configuration
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User views the Health Markets targeting UI
    Then the "WebMD" section is no longer present
    And the one existing production test deal previously under the WebMD health market has been deleted
    Given User attempts to select "WebMD" targeting via a direct API call, bypassing the UI
    Then the request is rejected, confirming the backend also blocks it and not merely the UI hiding it
    And the WebMDPremiumPublisher feature flag no longer exists in feature-flag configuration
    And the remaining Health Markets targeting options, such as Medscape, are unaffected by this removal

  # Source: ET-24695
  @todo
  Scenario Outline: IAS Quality Sync ID field on the Brand Safety Profile form accepts only a 7-digit numeric ID within a fixed range and enforces uniqueness
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User enters "<IAS_SYNC_ID>" in the IAS Quality Sync ID field on a Brand Safety Profile and saves
    Then the outcome is "<RESULT>"
    Examples:
      | IAS_SYNC_ID | RESULT                                        |
      |             | inline validation error - empty field         |
      | 40A0000     | inline validation error - non-numeric         |
      | 400000      | inline validation error - fewer than 7 digits |
      | 40000000    | inline validation error - more than 7 digits  |
      | 3999999     | inline validation error - below range         |
      | 5000000     | inline validation error - above range         |
      | 4000000     | accepted - lower boundary                     |
      | 4999999     | accepted - upper boundary                     |

  # Source: ET-24695
  @todo
  Scenario: A duplicate IAS Quality Sync ID is rejected with an inline message naming the conflicting entity at the Media Planner level
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    Given an IAS Quality Sync ID is already assigned to a Media Planner
    When an internal user enters that same ID on a different Media Planner
    Then an inline message identifies the Media Planner where the ID is already in use
    Given a previously-saved valid ID outside the current validation range from before this change shipped
    Then it is not force-invalidated on an unrelated re-save

  # Source: ET-24234
  @todo
  Scenario: A new "WebMD Brand's Bundled Deals" AO factor builds a lookalike-modeled seed audience from first-party WebMD page-visitation URL patterns
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User configures the "WebMD Brand's Bundled Deals" AO factor, classified as Secondary
    Then the URL pattern picker's first row cannot be removed
    And clicking "Add URL" adds a new row with a remove icon
    When User enters an invalid URL in the pattern picker
    Then the invalid URL is rejected, not silently accepted
    Given URL patterns spanning two different domains, for example webmd.com and rxlist.com
    Then visitors matching either domain's pattern are correctly included in the seed audience
    And the seed audience is scored against the full US population via lookalike modeling to produce the final factor output

  # Source: ET-24233
  @todo
  Scenario: A new "Medscape Custom URL" AO factor (titled "Medscape Brand's Bundled Deals") builds a seed audience from SQL-LIKE-wrapped URL patterns
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User configures the AO factor internally named "Medscape Custom URL"
    Then each entered pattern is always wrapped in "%...%" SQL-LIKE-style wildcards, for example "%drug%"
    And the URL pattern picker's first row cannot be removed, matching the WebMD sibling factor's rule
    Given multiple overlapping patterns are entered, for example "%drug%" and "%drug-monograph%"
    Then users are not double-counted in the seed audience
    Given a pattern contains a literal "%" or "_" character intended as match text
    Then the character is escaped/handled rather than breaking the pattern match

  # Source: ET-24232
  @todo
  Scenario: Medscape Top-Level Condition AO factor gains a permission-gated Specific Concept Id selector for granular targeting, without altering its existing coarse behavior
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    Given only the single named account rkuzmych@pulsepoint.com has this feature enabled
    When any other internal or external user views the Medscape Top-Level Condition factor
    Then the "Specific Concept Id" field is not visible to them at all
    Given the permitted user views the factor
    Then a searchable multi-select "Specific Concept Id" field is available, populated from the linked Medscape Concepts Taxonomy Google Sheet
    When multiple concept IDs are selected
    Then the seed audience correctly unions users across all selected IDs
    Given a top-level condition is selected without any specific concept ID
    Then the existing coarse-grained behavior still works unchanged as a baseline regression check

  # Source: ET-24231
  @todo
  Scenario: An internal-only "Medscape Drug Monograph Specific" AO factor targets engagement with individually selected drug monograph pages
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When an internal user configures the "Medscape Drug Monograph Specific" AO factor
    Then a curated, searchable drug picker is populated from the linked Medscape drug taxonomy Google Sheet
    And the factor is not exposed as a client-configurable option
    When multiple drugs are selected simultaneously
    Then the seed audience correctly unions engagement across all selected drugs
    Given the drug name/value shown in the factor picker screen
    Then it must match the drug name/value shown in the Tactic's AO section after selection
