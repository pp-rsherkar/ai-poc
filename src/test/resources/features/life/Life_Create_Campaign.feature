Feature: LIFE Regression - Create a Campaign
  It ensures creation of a campaign with a line item and a tactic, including:
  1. Create a campaign with a tactic and a line item
  2. Create a campaign with multiple targeting rules added to a tactic
  3. Create a campaign and verify all targetings under categories
  4. Verify campaign creation, check field-level validation, and default values
  5. Custom field addition, modification, and deletion on the Campaign creation page
  6. Create a campaign for an external user

  @regression
  Scenario Outline: Create a Campaign with a Tactic & a Line Item
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "<CHANNEL>" as channel
    And User selects "<RULE_TYPE>" as rule type and configures the targeting rules, and saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    Then Verify the newly created campaign is in running state
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name
    #Then Verify the newly created campaign in the database
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | RULE_TYPE          | CREATIVE      |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Tactic      | Display Advanced | Behavioral Segment | Auto_Creative |

  @regression
  Scenario Outline: Create a Campaign with multiple Targeting Rules added to a Tactic
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
    And User configures targeting rules as below
      | Behavioral Segment | 111 > 222 > Patients of HCPs prescribing Ivig and SCIg competitors |
      | In Condition       | Digestive System Diseases                                          |
      | Age                | 25-29, 35-39                                                       |
      | Health Pages       | Animal Diseases                                                    |
      | Postal Codes       | 123456, 10001, 987654                                              |
      | Device             | Connected Device, OOH Device                                       |
      | Legal Populations  | Adoption                                                           |
    Then Verify the configured targeting rules
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    Then Verify the newly created campaign is in running state
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | CREATIVE           |
      | 01- Advertiser | Test    | Regular | 10000     | Line      | 120         | Tactic      | Display Advanced | Please_Dont_Delete |

  @regression
  Scenario Outline: Create a Campaign and add and verify all Targetings under categories :: Audience Attribute, Health Journey,  Demographics, Contextual, Geography, Media Supply, Legal Targetings
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
      | MEDIA SUPPLY       | Brand Safety Profile,Brand Suitability,Browser,Curated Markets,Custom Targeting Bundle,Deal Group,Device,Domains/Apps,IAS Context Control,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals,Viewability      |
      | LEGAL TARGETINGS   | Legal Pages,Legal Populations                                                                                                                                                                                                     |
    Examples:
      | ADVERTISER             | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          |
      | CacheTestAdvertise232n | Test    | Regular | 10000     | Line      | 120         | Tactic      | Display Advanced |

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
      | MEDIA SUPPLY       | Brand Safety Profile,Brand Suitability,Browser,Curated Markets,Custom Targeting Bundle,Deal Group,Device,Domains/Apps,IAS Context Control,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals,Viewability      |
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
      | Inventory Source         | New Report                                                            |
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
  Scenario Outline: Verify campaign creation, check field-level validation and default values of the fields
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section and fetches the advertisers and client value for the account "automation@pulsepoint"
    And User clicks on Create Campaign
    Then Verify Advertiser dropdown should show values which are mapped to the account
    And Verify that an error message is displayed when no Advertiser is selected
    And Verify that Campaign Type default value is set to "Regular"
    And Verify that if the account has a Client value set, the Client field is disabled and auto-populated; otherwise, it remains enabled for user selection "<CP_CLIENT>"
    And Verify that user is able to enter and select the drug "<DRUG_NAME>"
    And Verify that Campaign Budget accepts only numeric values "<INVALID_CP_BUDGET>"
    And Verify that user is able to enter the data "<DESCRIPTION>" in the Description field
    And Verify that Budget Status has the below options, and the default status is "Approved"
      | Pending Approval |
      | Approved         |
      | Denied           |
    And Verify the availability of the Management Fee checkbox and when clicked, below options should be displayed
      | Percentage |
      | CPM        |
      | % + CPM    |
      | Fixed CPM  |
    And Verify that the user is able to enter data in the selected Management Fee option - "<MANAGEMENT_FEE>", "<PERCENT>", "<AMOUNT>"
    And User clicks the three-dot menu and verifies that "Generate Report" is enabled and "Delete" is disabled
    And User enters other campaign details "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>"
    And User retrieves all the entered data, saves the Campaign and verifies successful creation
    And Verify that the saved Campaign data matches the entered data
    Examples:
      | ADVERTISER     | CP_NAME  | CP_TYPE | CP_BUDGET | MANAGEMENT_FEE | DRUG_NAME | INVALID_CP_BUDGET | DESCRIPTION     | PERCENT | AMOUNT | CP_CLIENT   |
      | 01- Advertiser | Campaign | Regular | 50000     | % + CPM        | Glynase   | Test              | Automation test | 35      | 300    | PHM Chicago |

  @regression
  Scenario Outline: Custom field addition, modification, and deletion on the Campaign creation page, and verification of its persistence
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    And User verifies if Add Custom Field button is available
    When User adds a custom field with "<FIELD_NAME>" on the campaign creation page successfully
    Then Verify that the custom field is added on the campaign creation page
    When User modifies the custom field label to new label "<NEW_FIELD_NAME>"
    Then Verify that the custom field is updated with new label
    And User enters data "<CUSTOM_FIELD_VALUE>" in the custom field
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    Then Verify that the custom field value "<CUSTOM_FIELD_VALUE>" is saved and displayed in the campaign details page
    And User verifies if the added custom field is available on New Campaign creation page
    When User deletes the custom field for which campaign is created and verifies if it is deleted
    And User deletes the custom field for which campaign is not created and verifies if it is deleted
    And User verifies if the deleted custom field is available on New Campaign creation page
    Examples:
      | FIELD_NAME  | NEW_FIELD_NAME | ADVERTISER     | CP_NAME  | CP_TYPE | CP_BUDGET | CUSTOM_FIELD_VALUE |
      | CustomField | NewCustomField | 01- Advertiser | Campaign | Regular | 50000     | Test               |

  @regression
  Scenario Outline: Create a Campaign with a Tactic & a Line Item for an External user
    Given This scenario will be executed in the "Demo" environment as a "External User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>"
    Then Verify that the campaign budget status is "Pending Appr" and is greyed out
    And User saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User clicks on Add Targeting Rule
    And User selects "<RULE_TYPE>" as rule type and configures the targeting rules, and saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    Then Verify creative details are saved
    Then Verify that the campaign is in "Pending Appr" state
    Then Verify that the approval status of the campaign is "Pending Appr"
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name
    Examples:
      | ADVERTISER       | CP_NAME       | CP_TYPE | CP_BUDGET | LINE_NAME     | LINE_BUDGET | TACTIC_NAME     | RULE_TYPE          | CREATIVE          |
      | 1Demo Advertiser | External_Auto | Regular | 10000     | External_Line | 500         | External_Tactic | Behavioral Segment | External_Creative |

  @regression @ps
  Scenario: Create a Campaign with 3 Line Items, 3 Tactics each, and 6 Targeting Rules with multiple values per Tactic
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Multi_LI_Tactic_Campaign" "Regular" "50000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User creates line items with tactics and targeting rules as below
      | LI_TYPE        | LI_NAME    | LI_BUDGET | TACTIC_NAME           | CHANNEL          | RULE_1             | VALUES_1                                                                                            | RULE_2             | VALUES_2                                                                                             | RULE_3       | VALUES_3                                                                                            | RULE_4            | VALUES_4                                                                                | RULE_5            | VALUES_5                                                                      | RULE_6            | VALUES_6                                                                      |
      | Audio          | Audio_Line | 5000      | Audio_DA_Tactic       | Display Advanced | Behavioral Segment | AutoSegment18577650                                                                                 | Geo Targets        | New York, California, Texas, Florida, Illinois, Ohio, Georgia                                        | Age          | 18-24, 25-29, 30-34, 35-39, 40-44, 45-49, 50-54                                                     | HCP by Specialty  | Radiology, Aerospace Medicine, Cardiology, Neurology, Oncology, Dermatology, Pediatrics | Device            | Mobile, Tablet, Connected Device, Desktop, OOH Device, CTV, Smart TV          | Browser           | Chrome, EDGE, Opera, Safari, Firefox, IE, Samsung Internet                    |
      | Audio          | Audio_Line | 5000      | Audio_Email_Tactic    | Email            | Email              | AutoEmail120220716113986417                                                                         | Health Populations | Anesthesia and Analgesia, Dental/Oral Care, Cardiology, Neurology, Oncology, Dermatology, Pediatrics | In Condition | Liver Diseases, Digestive System Diseases, Heart Disease, Diabetes, Cancer, Arthritis, Hypertension | Legal Populations | Adoption, Emancipation, LegalPop3, LegalPop4, LegalPop5, LegalPop6, LegalPop7           | Age               | 18-24, 25-29, 35-39, 45-49, 50-54, 55-59, 60-64                               | Gender            | Male, Female                                                                  |
      | Audio          | Audio_Line | 5000      | Audio_EHR_Tactic      | EHR              | NPI                | AutoSmartList954103283                                                                              | HCP by Specialty   | Radiology, Aerospace Medicine, Cardiology, Neurology, Oncology, Dermatology, Pediatrics              | Geo Targets  | New York, California, Texas, Florida, Illinois, Ohio, Georgia                                       | Postal Codes      | 10001, 90210, 60601, 77001, 30301, 85001, 94102, 19101                                  | Device            | Mobile, Tablet, Connected Device, Desktop, OOH Device, CTV, Smart TV          | Age               | 18-24, 25-29, 35-39, 40-44, 45-49, 50-54, 55-59                               |
      | Video          | Video_Line | 8000      | Video_DA_Tactic       | Display Advanced | Video Size         | Small, Large                                                                                        | Video Placement    | Interstitial, Accompanying Content, NoContent/Standalone                                             | Geo Targets  | New York, California, Texas, Florida, Illinois, Ohio, Georgia                                       | Age               | 18-24, 25-29, 30-34, 35-39, 40-44, 45-49, 50-54                                         | Device            | Mobile, Tablet, Connected Device, Desktop, OOH Device, CTV, Smart TV          | Browser           | Chrome, EDGE, Opera, Safari, Firefox, IE, Samsung Internet                    |
      | Video          | Video_Line | 8000      | Video_Standard_Tactic | Standard         | Keywords           | Custom_Keyword, TestingKeyword, Qwerty123, MedKeyword1, MedKeyword2, HealthKeyword1, PharmaKeyword1 | IAB Categories     | Agriculture, Arts & Entertainment, Automotive, Business, Careers, Education, Family                  | Language     | English, Spanish, French, German, Italian, Portuguese, Chinese                                      | Ethnicity         | Asian, Arab, Hispanic, African American, Native American, Pacific Islander, White       | Age               | 18-24, 25-29, 35-39, 40-44, 45-49, 50-54, 55-59                               | Device            | Mobile, Tablet, Connected Device, Desktop, OOH Device, CTV, Smart TV          |
      | Video          | Video_Line | 8000      | Video_Email_Tactic    | Email            | Email              | AutoEmail120220716113986417                                                                         | In Condition       | Liver Diseases, Digestive System Diseases, Heart Disease, Diabetes, Cancer, Arthritis, Hypertension  | Health Pages | Animal Diseases, Liver Diseases, Heart Disease, Diabetes, Cancer, Allergy, Respiratory              | Age               | 18-24, 25-29, 30-34, 35-39, 40-44, 45-49, 50-54                                         | Geo Targets       | New York, California, Texas, Florida, Illinois, Ohio, Georgia                 | Legal Populations | Adoption, Emancipation, LegalPop3, LegalPop4, LegalPop5, LegalPop6, LegalPop7 |
      | Native Display | ND_Line    | 6000      | ND_DA_Tactic          | Display Advanced | Behavioral Segment | AutoSegment18577650, AutoSegment384105361                                                           | Geo Targets        | New York, California, Texas, Florida, Illinois, Ohio, Georgia                                        | Age          | 18-24, 25-29, 30-34, 35-39, 40-44, 45-49, 50-54                                                     | HCP by Specialty  | Radiology, Aerospace Medicine, Cardiology, Neurology, Oncology, Dermatology, Pediatrics | Browser           | Chrome, EDGE, Opera, Safari, Firefox, IE, Samsung Internet                    | Device            | Mobile, Tablet, Connected Device, Desktop, OOH Device, CTV, Smart TV          |
      | Native Display | ND_Line    | 6000      | ND_Standard_Tactic    | Standard         | HCP by Specialty   | Radiology, Aerospace Medicine, Cardiology, Neurology, Oncology, Dermatology, Pediatrics             | Age                | 18-24, 25-29, 35-39, 40-44, 45-49, 50-54, 55-59                                                      | Keywords     | Custom_Keyword, TestingKeyword, Qwerty123, MedKeyword1, MedKeyword2, HealthKeyword1, PharmaKeyword1 | Ethnicity         | Asian, Arab, Hispanic, African American, Native American, Pacific Islander, White       | Inventory Type    | App, Site                                                                     | Geo Targets       | New York, California, Texas, Florida, Illinois, Ohio, Georgia                 |
      | Native Display | ND_Line    | 6000      | ND_EHR_Tactic         | EHR              | NPI                | AutoSmartList954103283                                                                              | In Condition       | Liver Diseases, Digestive System Diseases, Heart Disease, Diabetes, Cancer, Arthritis, Hypertension  | Postal Codes | 10001, 90210, 60601, 77001, 30301, 85001, 94102, 19101                                              | Device            | Mobile, Tablet, Connected Device, Desktop, OOH Device, CTV, Smart TV                    | Legal Populations | Adoption, Emancipation, LegalPop3, LegalPop4, LegalPop5, LegalPop6, LegalPop7 | Age               | 18-24, 25-29, 35-39, 40-44, 45-49, 50-54, 55-59                               |
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name

#  @regression
#  Scenario Outline: API Sample Test
#    Given I call "<apiName>" with parameters "<param1>" & "<param2>"
#    Then Verify response have "<statusCode>" & "<expected1>" & "<expected2>"
#    Examples:
#      | apiName | param1 | param2 | statusCode | expected1 | expected2 |
#      | GET     | 1      | 2      | 404        | 1         | 2         |
#      | POST    | 1      | 2      | 404        | 1         | 2         |