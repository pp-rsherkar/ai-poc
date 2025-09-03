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

  #Scenario Outline: Opening an existing Campaign and creation of Line Item
   # When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    #Then Verify campaign details are saved and user is navigated to the line item page
   # When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
   # Then Verify line item details are saved and user is navigated to the tactic page
   # When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
   # Then Verify tactic details are saved and user is navigated to the settings tab
   # When User clicks on pulsepoint 'P' icon
    #Then Verify Search Box is available and working on Campaign Dashboard
   # When User searches for the above created Campaign
   # Then Verify matching results are displayed
   # When User selects the Campaign and clicks on New Line Item
   # Then Verify Line Item Page is opened
    #When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    #Then Verify line item details are saved and user is navigated to the tactic page

   # Examples:
     # | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET |
      #| 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         |