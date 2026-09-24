Feature: LIFE Regression - Create Pixel of following types:
  1. Retargeting Pixel using Javascript pixel type
  2. Retargeting Pixel using Image pixel type
  3. Smart Pixel with associated Smart List
  4. Smart Pixel without associated Smart List
  5. Conversion Pixel

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to Pixels page
    Then Verify the tabs displayed on the Pixels page
    Then Verify the Advertiser dropdown and search box are displayed on the Pixels page
    When User clicks on Add Pixel button
    Then Verify the Create New Pixel panel and types of Pixel

  @regression
  Scenario Outline: Manage a Retargeting Pixel using Javascript pixel type (Create, Edit and Remove)
    And User selects the "<PIXEL_TYPE>" type
    When User tries to save the Retargeting pixel without entering any details, an error message should be displayed
    And User enters the pixel details as "<PIXEL_NAME>" "<ADVERTISER>"
    And User selects the "JavaScript" pixel
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    When User edits the name of the created "<PIXEL_TYPE>"
    Then Verify the "<PIXEL_TYPE>" gets updated successfully
    When User removes the created pixel
    Then Verify the pixel gets removed successfully
    Then Verify the removed pixel should not be displayed in the pixel list
    Examples:
      | PIXEL_TYPE        | PIXEL_NAME  | ADVERTISER   |
      | Retargeting Pixel | Retargeting | Z_Automation |

  @regression
  Scenario Outline: Manage a Retargeting Pixel using Image pixel type (Create, Edit and Remove)
    And User selects the "<PIXEL_TYPE>" type
    When User tries to save the Retargeting pixel without entering any details, an error message should be displayed
    And User enters the pixel details as "<PIXEL_NAME>" "<ADVERTISER>"
    And User selects the "Image" pixel
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    When User edits the name of the created "<PIXEL_TYPE>"
    Then Verify the "<PIXEL_TYPE>" gets updated successfully
    When User removes the created pixel
    Then Verify the pixel gets removed successfully
    Then Verify the removed pixel should not be displayed in the pixel list
    Examples:
      | PIXEL_TYPE        | PIXEL_NAME  | ADVERTISER             |
      | Retargeting Pixel | Retargeting | CacheTestAdvertise232n |

  @regression
  Scenario Outline: Manage a Smart Pixel with associated Smart List (Create and Edit)
    And User selects the "<PIXEL_TYPE>" type
    When User selects "<ADVERTISER>" as advertiser
    Then Verify the Smart Pixel name is auto populated with "<ADVERTISER>" and Smart Pixel text
    And User selects the associated campaign
    And User saves the pixel
    Then Verify the smart pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    And User adds the associated Smart List and enters list details as "<LIST_NAME>"
    Then Verify the selected "<ADVERTISER>" and Smart Pixel
    Then Save and Verify the list gets saved successfully
    And User navigates to Pixels page
    And User selects the created Smart Pixel
    Then Verify the selected Smart List should be reflected in the Associated Smartlists tab
    And User navigates to the Pixel Codes tab
    When User edits the name of the created "<PIXEL_TYPE>"
    Then Verify the "<PIXEL_TYPE>" gets updated successfully
    Then Verify user should not be able to deactivate the Smart Pixel if any Smart list is associated with it
    Examples:
      | PIXEL_TYPE  | ADVERTISER     | LIST_NAME   |
      | Smart Pixel | 01- Advertiser | Smart_Pixel |

  @regression
  Scenario Outline: Manage a Smart Pixel without associated Smart List (Create, Edit and Deactivate)
    And User selects the "<PIXEL_TYPE>" type
    When User selects "<ADVERTISER>" as advertiser
    Then Verify the Smart Pixel name is auto populated with "<ADVERTISER>" and Smart Pixel text
    And User selects the associated campaign
    And User saves the pixel
    Then Verify the smart pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    When User edits the name of the created "<PIXEL_TYPE>"
    Then Verify the "<PIXEL_TYPE>" gets updated successfully
    When User deactivates the created pixel
    Then Verify the pixel gets deactivated successfully
    Then Verify the deactivated pixel should not be displayed in the pixel list
    Examples:
      | PIXEL_TYPE  | ADVERTISER       |
      | Smart Pixel | 1Demo Advertiser |

  @regression
  Scenario Outline: Manage a Conversion Pixel (Create, Edit and Remove)
    And User selects the "<PIXEL_TYPE>" type
    When User tries to save the Conversion pixel without entering any details, an error message should be displayed
    And User enters the pixel details as "<PIXEL_NAME>" "<ADVERTISER>" "<SCOPE>" "<TYPE>"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    When User edits the name of the created "<PIXEL_TYPE>"
    Then Verify the "<PIXEL_TYPE>" gets updated successfully
    When User removes the created pixel
    Then Verify the pixel gets removed successfully
    Then Verify the removed pixel should not be displayed in the pixel list
    Examples:
      | PIXEL_TYPE       | PIXEL_NAME | ADVERTISER       | SCOPE  | TYPE               |
      | Conversion Pixel | Conversion | 1Demo Advertiser | Person | Submit Application |

  # Source: ET-25040, TC_ET-25040_05, TC_ET-25040_06, TC_ET-25040_07, TC_ET-25040_08, TC_ET-25040_09, TC_ET-25040_11, TC_ET-25040_19, TC_ET-25040_26, TC_ET-25040_27, TC_ET-25040_29, TC_ET-25040_30, TC_ET-25040_38, TC_ET-25040_47, TC_ET-25040_52, HT-5081, QA-2102, QA-2111
  @todo
  Scenario: Save a Conversion Pixel without line items, then associate and remove line items from the Associated Line Items section
    And User selects the "Conversion Pixel" type
    # Framework Gap: Requires a step definition and page-object hook for the "Associate this pixel with line item" footer checkbox in LifeSteps.java / pages/life/ConversionPixel.java
    Then Verify the "Associate this pixel with line item" checkbox is not displayed in the Create New Pixel panel
    When User tries to save the Conversion pixel without entering any details, an error message should be displayed
    And User enters the pixel details as "QA Assoc NoLI" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    # Framework Gap: Requires a step definition to open a created Conversion Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Conversion Pixel from the pixel list
    # Framework Gap: Requires page-object hooks for the Associated Line Items section in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section title counter shows "0"
    # Framework Gap: Requires page-object hooks for the Associated Line Items blank state in pages/life/ConversionPixel.java
    And Verify the Associated Line Items section shows the blank state
    # Framework Gap: Requires page-object hooks for the Associated Line Items warning in pages/life/ConversionPixel.java
    And Verify the warning "LI must be associated for pixel to work" is displayed in the Associated Line Items section
    # Framework Gap: Requires step definitions for the Add Line Items panel in LifeSteps.java
    When User clicks Add in the Associated Line Items section
    # Framework Gap: Requires step definitions for expanding campaigns in the Add Line Items panel in LifeSteps.java
    And User expands campaign "C1" in the Add Line Items panel
    # Framework Gap: Requires step definitions for ticking line items in the Add Line Items panel in LifeSteps.java
    And User ticks line item "LI-A" in the Add Line Items panel
    And User ticks line item "LI-B" in the Add Line Items panel
    And User expands campaign "C2" in the Add Line Items panel
    And User ticks line item "LI-C" in the Add Line Items panel
    # Framework Gap: Requires a step definition to confirm the Add Line Items panel selection in LifeSteps.java
    And User confirms the Add Line Items panel selection
    # Framework Gap: Requires a generic page reload step in LifeSteps.java
    And User reloads the pixel page
    # Framework Gap: Requires page-object hooks for the Associated Line Items list in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
      | LI-B |
      | LI-C |
    And Verify the Associated Line Items section title counter shows "3"
    # Framework Gap: Requires a step definition comparing listed line item IDs with the Line Item detail page IDs in LifeSteps.java
    And Verify each line item in the Associated Line Items section shows its numeric line item ID
    # Framework Gap: Requires page-object hooks for the Associated Line Items warning in pages/life/ConversionPixel.java
    And Verify the warning "LI must be associated for pixel to work" is not displayed in the Associated Line Items section
    # Framework Gap: Requires a database lookup of the pixel to line item mapping in utils/DatabaseActions.java
    And Verify the pixel to line item mapping in the database contains the created pixel ID and the line item ID of "LI-A"
    When User clicks Add in the Associated Line Items section
    And User expands campaign "C2" in the Add Line Items panel
    And User ticks line item "LI-D" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    Then Verify the Associated Line Items section title counter shows "4"
    # Framework Gap: Requires step definitions for removing a line item from the Associated Line Items section in LifeSteps.java
    When User removes line item "LI-A" from the Associated Line Items section
    And User removes line item "LI-B" from the Associated Line Items section
    And User saves the pixel
    And User reloads the pixel page
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-C |
      | LI-D |
    And Verify the Associated Line Items section title counter shows "2"
    When User removes line item "LI-C" from the Associated Line Items section
    And User removes line item "LI-D" from the Associated Line Items section
    And User saves the pixel
    And User reloads the pixel page
    Then Verify the Associated Line Items section title counter shows "0"
    And Verify the Associated Line Items section shows the blank state
    And Verify the warning "LI must be associated for pixel to work" is displayed in the Associated Line Items section

  # Source: ET-25040, TC_ET-25040_12, TC_ET-25040_13, TC_ET-25040_15, TC_ET-25040_16, TC_ET-25040_17, TC_ET-25040_18, QA-2103, QA-2105
  @todo
  Scenario: Hide finished filter, campaign select-all and PG line items in the Add Line Items panel
    And User selects the "Conversion Pixel" type
    And User enters the pixel details as "QA Assoc HideFinished" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    # Framework Gap: Requires a step definition to open a created Conversion Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Conversion Pixel from the pixel list
    # Framework Gap: Requires step definitions for the Add Line Items panel in LifeSteps.java
    And User clicks Add in the Associated Line Items section
    # Framework Gap: Requires page-object hooks for the Hide finished checkbox in the Add Line Items panel
    Then Verify the "Hide finished" checkbox is ticked in the Add Line Items panel
    When User expands campaign "C1" in the Add Line Items panel
    # Framework Gap: Requires page-object hooks for line item rows in the Add Line Items panel
    Then Verify line item "LI-A" is listed under campaign "C1" in the Add Line Items panel
    And Verify line item "LI-P" is listed under campaign "C1" in the Add Line Items panel
    And Verify line item "LI-F" is not listed under campaign "C1" in the Add Line Items panel
    # Framework Gap: Requires a step definition for the campaign select-all checkbox in the Add Line Items panel
    When User ticks the campaign checkbox of "C1" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    And User reloads the pixel page
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
      | LI-P |
    And Verify the Associated Line Items section title counter shows "2"
    When User clicks Add in the Associated Line Items section
    # Framework Gap: Requires a step definition for unticking the Hide finished checkbox in the Add Line Items panel
    And User unticks the "Hide finished" checkbox in the Add Line Items panel
    And User expands campaign "C1" in the Add Line Items panel
    Then Verify line item "LI-F" is listed under campaign "C1" in the Add Line Items panel
    When User ticks the campaign checkbox of "C1" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    And User reloads the pixel page
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
      | LI-P |
      | LI-F |
    And Verify the Associated Line Items section title counter shows "3"
    When User clicks Add in the Associated Line Items section
    # Framework Gap: Requires a step definition for ticking the Hide finished checkbox in the Add Line Items panel
    And User ticks the "Hide finished" checkbox in the Add Line Items panel
    And User expands campaign "C-PG" in the Add Line Items panel
    Then Verify line item "LI-PG-01" is listed under campaign "C-PG" in the Add Line Items panel
    And Verify line item "LI-PG-02" is not listed under campaign "C-PG" in the Add Line Items panel
    # Framework Gap: Requires page-object hooks for line item IDs in the Add Line Items panel
    And Verify line item "LI-PG-01" shows its numeric line item ID in the Add Line Items panel
    When User unticks the "Hide finished" checkbox in the Add Line Items panel
    Then Verify line item "LI-PG-02" is listed under campaign "C-PG" in the Add Line Items panel
    # Framework Gap: Requires step definitions for ticking line items in the Add Line Items panel in LifeSteps.java
    When User ticks line item "LI-PG-01" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    And User reloads the pixel page
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A     |
      | LI-P     |
      | LI-F     |
      | LI-PG-01 |
    And Verify the Associated Line Items section title counter shows "4"

  # Source: ET-25040, TC_ET-25040_20, TC_ET-25040_21, TC_ET-25040_22, TC_ET-25040_23, TC_ET-25040_25, TC_ET-25040_45, TC_ET-25040_46, GAP-1, QA-2131, HT-6340
  @todo
  Scenario: Search the Add Line Items panel by line item ID and name within the pixel advertiser
    And User selects the "Conversion Pixel" type
    And User enters the pixel details as "QA Assoc Search" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    # Framework Gap: Requires a step definition to open a created Conversion Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Conversion Pixel from the pixel list
    # Framework Gap: Requires step definitions for the Add Line Items panel in LifeSteps.java
    And User clicks Add in the Associated Line Items section
    # Framework Gap: Requires page-object hooks for campaign rows in the Add Line Items panel
    Then Verify every campaign in the Add Line Items panel belongs to advertiser "100Advertiser"
    And Verify campaign "C1-200" is not listed in the Add Line Items panel
    # Framework Gap: Requires a step definition for searching the Add Line Items panel in LifeSteps.java
    When User searches the Add Line Items panel by the line item ID of "LI-A"
    # Framework Gap: Requires page-object hooks for line item rows in the Add Line Items panel
    Then Verify only line item "LI-A" is listed under campaign "C1" in the Add Line Items panel
    When User searches the Add Line Items panel for "HCP"
    Then Verify the Add Line Items panel lists the below line items
      | CAMPAIGN | LINE_ITEM       |
      | C1       | Retargeting HCP |
      | C2       | Retargeting HCP |
      | C1       | Endemic HCP     |
    And Verify line item "DTC Display" is not listed in the Add Line Items panel
    # Framework Gap: Requires a step definition for ticking a line item under a specific campaign in the Add Line Items panel
    When User ticks line item "Retargeting HCP" under campaign "C1" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    # Framework Gap: Requires a generic page reload step in LifeSteps.java
    And User reloads the pixel page
    # Framework Gap: Requires page-object hooks for the Associated Line Items list in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section lists the line item ID of "Retargeting HCP" under campaign "C1"
    And Verify the Associated Line Items section does not list the line item ID of "Retargeting HCP" under campaign "C2"
    And Verify the Associated Line Items section title counter shows "1"
    When User clicks Add in the Associated Line Items section
    And User searches the Add Line Items panel for "99999999"
    # Framework Gap: Requires page-object hooks for the Add Line Items panel empty-result state
    Then Verify the Add Line Items panel shows the empty-result state
    When User searches the Add Line Items panel by the line item ID of "LI-F"
    Then Verify the Add Line Items panel shows the empty-result state
    # Framework Gap: Requires a step definition for unticking the Hide finished checkbox in the Add Line Items panel
    When User unticks the "Hide finished" checkbox in the Add Line Items panel
    Then Verify only line item "LI-F" is listed under campaign "C1" in the Add Line Items panel
    When User searches the Add Line Items panel by the line item ID of a line item in campaign "C1-200"
    Then Verify the Add Line Items panel shows the empty-result state

  # Source: ET-25040, TC_ET-25040_10, TC_ET-25040_31, TC_ET-25040_32, TC_ET-25040_50, TC_ET-25040_51, QA-2111
  @todo
  Scenario: Abandoned or failed line item selections and opening an associated line item do not change the associations
    And User selects the "Conversion Pixel" type
    And User enters the pixel details as "QA Assoc Abandon" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    # Framework Gap: Requires a step definition to open a created Conversion Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Conversion Pixel from the pixel list
    # Framework Gap: Requires step definitions for the Add Line Items panel in LifeSteps.java
    And User clicks Add in the Associated Line Items section
    And User expands campaign "C1" in the Add Line Items panel
    And User ticks line item "LI-A" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    # Framework Gap: Requires page-object hooks for the Associated Line Items section in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section title counter shows "1"
    When User clicks Add in the Associated Line Items section
    And User expands campaign "C2" in the Add Line Items panel
    And User ticks line item "LI-C" in the Add Line Items panel
    # Framework Gap: Requires a step definition to cancel the Add Line Items panel in LifeSteps.java
    And User cancels the Add Line Items panel
    # Framework Gap: Requires a generic page reload step in LifeSteps.java
    And User reloads the pixel page
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
    And Verify the Associated Line Items section title counter shows "1"
    When User clicks Add in the Associated Line Items section
    And User expands campaign "C2" in the Add Line Items panel
    And User ticks line item "LI-C" in the Add Line Items panel
    And User navigates to Pixels page
    And User opens the created Conversion Pixel from the pixel list
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
    And Verify the Associated Line Items section title counter shows "1"
    When User clicks Add in the Associated Line Items section
    And User expands campaign "C1" in the Add Line Items panel
    And User ticks line item "LI-B" in the Add Line Items panel
    And User expands campaign "C2" in the Add Line Items panel
    And User ticks line item "LI-C" in the Add Line Items panel
    # Framework Gap: Requires Playwright request interception for the association save request in LifeSteps.java
    And User blocks the association save request
    And User confirms the Add Line Items panel selection
    # Framework Gap: Requires page-object hooks for the association save error in pages/life/ConversionPixel.java
    Then Verify an error message is displayed for the failed association save
    When User unblocks the association save request
    And User reloads the pixel page
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
    And Verify the Associated Line Items section title counter shows "1"
    # Framework Gap: Requires a step definition to open a line item from the Associated Line Items section in LifeSteps.java
    When User clicks line item "LI-A" in the Associated Line Items section
    # Framework Gap: Requires step definitions verifying the Line Item detail page name, header ID and URL ID in LifeSteps.java / pages/life/LineItemDetails.java
    Then Verify the Line Item detail page is displayed for "LI-A"
    And Verify the Line Item detail page header shows the line item ID of "LI-A"
    And Verify the Line Item detail page URL contains the line item ID of "LI-A"
    # Framework Gap: Requires a browser back navigation step in LifeSteps.java
    When User navigates back to the pixel page
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
    And Verify the Associated Line Items section title counter shows "1"

  # Source: ET-25040, TC_ET-25040_53, HT-5380
  @todo
  Scenario: Each Conversion Pixel lists only its own associated line items
    And User selects the "Conversion Pixel" type
    And User enters the pixel details as "QA Assoc P1" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    # Framework Gap: Requires a step definition to open a created Conversion Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Conversion Pixel from the pixel list
    # Framework Gap: Requires step definitions for the Add Line Items panel in LifeSteps.java
    And User clicks Add in the Associated Line Items section
    And User expands campaign "C1" in the Add Line Items panel
    And User ticks line item "LI-1" in the Add Line Items panel
    And User ticks line item "LI-2" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    And User navigates to Pixels page
    When User clicks on Add Pixel button
    And User selects the "Conversion Pixel" type
    And User enters the pixel details as "QA Assoc P2" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    When User opens the created Conversion Pixel from the pixel list
    And User clicks Add in the Associated Line Items section
    And User expands campaign "C1" in the Add Line Items panel
    And User ticks line item "LI-3" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    # Framework Gap: Requires a generic page reload step in LifeSteps.java
    And User reloads the pixel page
    # Framework Gap: Requires page-object hooks for the Associated Line Items list in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-3 |
    And User navigates to Pixels page
    # Framework Gap: Requires a step definition to open a Conversion Pixel by name from the pixel list in LifeSteps.java
    When User opens the Conversion Pixel "QA Assoc P1" from the pixel list
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-1 |
      | LI-2 |

  # Source: ET-25040, TC_ET-25040_55, TC_ET-25040_56, HT-5260, HT-6019, HT-6111, QA-2111
  @todo
  Scenario: Associating every line item of a campaign leaves the pixel verification status and pixel code unchanged
    And User selects the "Conversion Pixel" type
    And User enters the pixel details as "QA Assoc Unverified" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    # Framework Gap: Requires a step definition to open a created Conversion Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Conversion Pixel from the pixel list
    # Framework Gap: Requires step definitions for the Add Line Items panel in LifeSteps.java
    And User clicks Add in the Associated Line Items section
    # Framework Gap: Requires a step definition for unticking the Hide finished checkbox in the Add Line Items panel
    And User unticks the "Hide finished" checkbox in the Add Line Items panel
    # Framework Gap: Requires a step definition for the campaign select-all checkbox in the Add Line Items panel
    And User ticks the campaign checkbox of "C1" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    # Framework Gap: Requires a generic page reload step in LifeSteps.java
    And User reloads the pixel page
    # Framework Gap: Requires page-object hooks for the Associated Line Items list in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section lists every line item of campaign "C1"
    And Verify the warning "LI must be associated for pixel to work" is not displayed in the Associated Line Items section
    # Framework Gap: Requires page-object hooks for the pixel verification status in pages/life/ConversionPixel.java
    And Verify the pixel verification status is "Unverified"
    # Framework Gap: Requires step definitions for the pixel code versions toggle in LifeSteps.java / pages/life/ConversionPixel.java
    When User expands the pixel code versions toggle
    # Framework Gap: Requires page-object hooks for reading each pixel code version snippet in pages/life/ConversionPixel.java
    Then Verify every pixel code version snippet contains the created pixel ID as "cv" in the "tr.contextweb.com" URL

  # Source: ET-25040, TC_ET-25040_43, TC_ET-25040_44, GAP-7
  @todo
  Scenario: Retargeting and Smart Pixels show no Associated Line Items section or association warning
    And User selects the "Retargeting Pixel" type
    And User enters the pixel details as "QA Assoc Retargeting" "100Advertiser"
    And User selects the "JavaScript" pixel
    And User saves the pixel
    Then Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    # Framework Gap: Requires a step definition to open a created Retargeting Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Retargeting Pixel from the pixel list
    # Framework Gap: Requires page-object hooks for the Associated Line Items section in pages/life/RetargetingPixel.java
    Then Verify the Associated Line Items section is not displayed
    And Verify the warning "LI must be associated for pixel to work" is not displayed
    And User navigates to Pixels page
    When User clicks on Add Pixel button
    And User selects the "Smart Pixel" type
    When User selects "100Advertiser" as advertiser
    And User selects the associated campaign
    And User saves the pixel
    Then Verify the smart pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list
    And User adds the associated Smart List and enters list details as "QA Assoc SmartList"
    Then Save and Verify the list gets saved successfully
    And User navigates to Pixels page
    And User selects the created Smart Pixel
    # Framework Gap: Requires page-object hooks for the Associated Line Items section in pages/life/SmartPixel.java
    Then Verify the Associated Line Items section is not displayed
    And Verify the warning "LI must be associated for pixel to work" is not displayed

  # Source: ET-25040, TC_ET-25040_03, TC_ET-25040_04, GAP-7
  @todo
  Scenario: Tag Manager conversion trackers show their associated line items or the blank state and warning
    # Framework Gap: TagManagerPage is marked framework_gap=true in navigation-map/navigation-tree.html; requires a Tag Manager page object and navigation step
    When User opens the mega menu and selects "Tag Manager"
    # Framework Gap: Requires step definitions for opening a Tag Manager conversion tracker in LifeSteps.java
    And User opens the Tag Manager conversion tracker "TM-Conv-01"
    # Framework Gap: Requires page-object hooks for the Associated Line Items section on the Tag Manager conversion tracker
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
    And Verify each line item in the Associated Line Items section shows its numeric line item ID
    And Verify the Associated Line Items section title counter shows "1"
    When User opens the Tag Manager conversion tracker "TM-Conv-02"
    Then Verify the Associated Line Items section title counter shows "0"
    And Verify the Associated Line Items section shows the blank state
    And Verify the warning "LI must be associated for pixel to work" is displayed in the Associated Line Items section
