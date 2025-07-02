Feature: LIFE Regression - Create a Campaign

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign

  @regression
  Scenario Outline: Create a Campaign with a Tactic & a Line Item
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
    Then Verify creative details are saved and the campaign is in running state
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name
    Then Verify the newly created campaign in the database

    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | RULE_TYPE          | CREATIVE      |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Tactic      | Display Advanced | Behavioral Segment | Auto_Creative |

  @regression
  Scenario Outline: Create a Campaign with multiple Targeting Rules added to a Tactic
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "<CHANNEL>" as channel
    And User configures targeting rules as below
      | Behavioral Segment | 111 > 222 > Patients of HCPs prescribing Ivig and SCIg competitors |
      | In Condition       | Digestive System Diseases, Liver Diseases                          |
      | Age                | 25-29, 35-39                                                       |
      | Health Pages       | Animal Diseases                                                    |
      | Postal Codes       | 123456, 10001, 987654                                              |
      | Device             | Connected Device, OOH Device                                       |
      | Legal Populations  | Adoption                                                           |
    Then Verify the configured targeting rules
    When User saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    Then Verify creative details are saved and the campaign is in running state
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name

    Examples:
      | ADVERTISER               | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | CREATIVE           |
      | 00CacheTestAdvertise232n | Test    | Regular | 10000     | Line      | 120         | Tactic      | Display Advanced | Please_Dont_Delete |

  @regression
  Scenario Outline: Create a Campaign and add and verify all Targetings under categories :: Audience Attribute, Health Journey,  Demographics, Contextual, Geography, Media Supply, Legal Targetings
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
      | MEDIA SUPPLY       | Authentic Brand Suitability,Brand Safety & Suitability,Browser,Curated Market,Custom Targeting Bundle,Device,Domains/Apps,Invalid Traffic,Inventory Source,Inventory Type,Operating System,Deals,Viewability                      |
      | LEGAL TARGETINGS   | Legal Pages,Legal Populations                                                                                                                                                                                                     |

    Examples:
      | ADVERTISER               | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | CREATIVE           |
      | 00CacheTestAdvertise232n | Test    | Regular | 10000     | Line      | 120         | Tactic      | Display Advanced | Please_Dont_Delete |


#  @regression
#  Scenario Outline: API Sample Test
#    Given I call "<apiName>" with parameters "<param1>" & "<param2>"
#    Then Verify response have "<statusCode>" & "<expected1>" & "<expected2>"
#    Examples:
#      | apiName | param1 | param2 | statusCode | expected1 | expected2 |
#      | GET     | 1      | 2      | 404        | 1         | 2         |
#      | POST    | 1      | 2      | 404        | 1         | 2         |