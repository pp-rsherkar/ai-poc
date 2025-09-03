Feature: LIFE Regression - Create Line Items

  @regression
  Scenario Outline: Creation of different types of Line Items
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details with different line types "<LINE_NAME>" "<LINE_BUDGET>" "<LINE_TYPE>", enables the line item and saves the changes

    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | LINE_TYPE                                                                   |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Display, Audio, Video, Native Display, Native Video, DOOH, Search Extension |

