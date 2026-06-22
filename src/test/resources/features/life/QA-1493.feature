Feature: LIFE AI Test Case Workflow - Audience Archive and Unarchive Management

  @todo
  Scenario: Verify Active and Archive tabs are present on sidebar and Status tab filter is removed
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    Then Verify Active tab is visible on the sidebar
    And Verify Archive tab is visible on the sidebar
    And Verify Status tab filter is not present on the sidebar

  @todo
  Scenario: Verify toggles on audience cards in Active tab list view are replaced with archive icons
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    Then Verify each audience card in list view displays an archive icon instead of a toggle

  @todo
  Scenario: Verify toggle on audience panel is replaced with archive icon
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks on an audience card to open the audience panel
    Then Verify the top right toggle on audience panel is replaced with an archive icon

  @todo
  Scenario: Verify warning popup appears when user clicks archive icon on audience card in Active tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks the archive icon on an audience card
    Then Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"

  @todo
  Scenario: Verify user can confirm archiving audience from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks the archive icon on an audience card
    And Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"
    And User clicks the confirm button on the warning popup
    Then Verify toast message appears "Audience has been archived successfully."
    And Verify the audience is moved to Archive tab

  @todo
  Scenario: Verify user can cancel archiving audience from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks the archive icon on an audience card
    And Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"
    And User clicks the cancel button on the warning popup
    Then Verify the audience remains in Active tab
    And Verify no toast message is displayed

  @todo
  Scenario: Verify warning popup appears when user turns off last remaining integration toggle in Audience Manager Activation tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks on an audience card to open the audience panel
    And User navigates to Audience Manager Activation tab
    And User turns off the last remaining integration toggle
    Then Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"

  @todo
  Scenario: Verify user can confirm archiving audience when disabling last integration toggle
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks on an audience card to open the audience panel
    And User navigates to Audience Manager Activation tab
    And User turns off the last remaining integration toggle
    And Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"
    And User clicks the confirm button on the warning popup
    Then Verify toast message appears "Audience has been archived successfully."
    And Verify the audience is moved to Archive tab

  @todo
  Scenario: Verify user can cancel archiving audience when disabling last integration toggle
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks on an audience card to open the audience panel
    And User navigates to Audience Manager Activation tab
    And User turns off the last remaining integration toggle
    And Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"
    And User clicks the cancel button on the warning popup
    Then Verify the integration toggle remains enabled
    And Verify the audience remains in Active tab

  @todo
  Scenario: Verify bulk action footer appears when checkboxes are selected on audience cards in Active tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User selects checkboxes on at least one audience card
    Then Verify bulk action footer appears at the bottom of the page

  @todo
  Scenario: Verify Archive Audiences button is enabled and Unarchive Audiences button is disabled in bulk action footer in Active tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User selects checkboxes on at least one audience card
    Then Verify bulk action footer appears
    And Verify Archive Audiences button is enabled
    And Verify Unarchive Audiences button is disabled

  @todo
  Scenario: Verify warning popup appears when Archive Audiences button is clicked in bulk action footer in Active tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User selects checkboxes on multiple audience cards
    And User clicks Archive Audiences button in the bulk action footer
    Then Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"

  @todo
  Scenario: Verify user can confirm bulk archiving audiences from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User selects checkboxes on multiple audience cards
    And User clicks Archive Audiences button in the bulk action footer
    And Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"
    And User clicks the confirm button on the warning popup
    Then Verify toast message appears "Audience has been archived successfully."
    And Verify all selected audiences are moved to Archive tab

  @todo
  Scenario: Verify user can cancel bulk archiving audiences from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User selects checkboxes on multiple audience cards
    And User clicks Archive Audiences button in the bulk action footer
    And Verify warning popup appears with message "This audience is going to be archived. Do you want to continue?"
    And User clicks the cancel button on the warning popup
    Then Verify all selected audiences remain in Active tab
    And Verify no toast message is displayed

  @todo
  Scenario: Verify warning popup appears when user clicks unarchive icon on audience card in Archive tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks the unarchive icon on an audience card
    Then Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"

  @todo
  Scenario: Verify user can confirm unarchiving audience from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks the unarchive icon on an audience card
    And Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"
    And User clicks the confirm button on the warning popup
    Then Verify toast message appears "Audience has been unarchived successfully."
    And Verify the audience is moved to Active tab

  @todo
  Scenario: Verify user can cancel unarchiving audience from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks the unarchive icon on an audience card
    And Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"
    And User clicks the cancel button on the warning popup
    Then Verify the audience remains in Archive tab
    And Verify no toast message is displayed

  @todo
  Scenario: Verify warning popup appears when user enables any platform toggle in Audience Manager Activation tab in Archive section
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks on an audience card to open the audience panel
    And User navigates to Audience Manager Activation tab
    And User turns on any platform integration toggle
    Then Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"

  @todo
  Scenario: Verify user can confirm unarchiving audience when enabling platform toggle
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks on an audience card to open the audience panel
    And User navigates to Audience Manager Activation tab
    And User turns on any platform integration toggle
    And Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"
    And User clicks the confirm button on the warning popup
    Then Verify toast message appears "Audience has been unarchived successfully."
    And Verify the audience is moved to Active tab

  @todo
  Scenario: Verify user can cancel unarchiving audience when enabling platform toggle
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks on an audience card to open the audience panel
    And User navigates to Audience Manager Activation tab
    And User turns on any platform integration toggle
    And Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"
    And User clicks the cancel button on the warning popup
    Then Verify the integration toggle remains disabled
    And Verify the audience remains in Archive tab

  @todo
  Scenario: Verify bulk action footer appears when checkboxes are selected on audience cards in Archive tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User selects checkboxes on at least one audience card
    Then Verify bulk action footer appears at the bottom of the page

  @todo
  Scenario: Verify Unarchive Audiences button is enabled and Archive Audiences button is disabled in bulk action footer in Archive tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User selects checkboxes on at least one audience card
    Then Verify bulk action footer appears
    And Verify Unarchive Audiences button is enabled
    And Verify Archive Audiences button is disabled

  @todo
  Scenario: Verify warning popup appears when Unarchive Audiences button is clicked in bulk action footer in Archive tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User selects checkboxes on multiple audience cards
    And User clicks Unarchive Audiences button in the bulk action footer
    Then Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"

  @todo
  Scenario: Verify user can confirm bulk unarchiving audiences from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User selects checkboxes on multiple audience cards
    And User clicks Unarchive Audiences button in the bulk action footer
    And Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"
    And User clicks the confirm button on the warning popup
    Then Verify toast message appears "Audience has been unarchived successfully."
    And Verify all selected audiences are moved to Active tab

  @todo
  Scenario: Verify user can cancel bulk unarchiving audiences from warning popup
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User selects checkboxes on multiple audience cards
    And User clicks Unarchive Audiences button in the bulk action footer
    And Verify warning popup appears with message "This audience is going to be unarchived. Do you want to continue?"
    And User clicks the cancel button on the warning popup
    Then Verify all selected audiences remain in Archive tab
    And Verify no toast message is displayed

  @todo
  Scenario: Verify archive icon on audience panel in Active tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks on an audience card to open the audience panel
    Then Verify the archive icon is displayed in the top right section of the audience panel

  @todo
  Scenario: Verify unarchive icon on audience panel in Archive tab
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks on an audience card to open the audience panel
    Then Verify the unarchive icon is displayed in the top right section of the audience panel

  @todo
  Scenario: Verify audience remains in Active tab after enabling integration toggle when multiple toggles are active
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks on an audience card with multiple active integration toggles to open the audience panel
    And User navigates to Audience Manager Activation tab
    And User turns off one integration toggle while at least one other toggle remains enabled
    Then Verify no warning popup appears
    And Verify the audience remains in Active tab

  @todo
  Scenario: Verify audience list refreshes after archiving and displays updated state
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Active tab
    When User clicks the archive icon on an audience card
    And User confirms the archive action from the warning popup
    And Verify toast message appears "Audience has been archived successfully."
    And User refreshes the browser
    Then Verify the archived audience is no longer visible in Active tab
    And Verify the archived audience appears in Archive tab

  @todo
  Scenario: Verify audience list refreshes after unarchiving and displays updated state
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Audience list page
    And User clicks on Archive tab
    When User clicks the unarchive icon on an audience card
    And User confirms the unarchive action from the warning popup
    And Verify toast message appears "Audience has been unarchived successfully."
    And User refreshes the browser
    Then Verify the unarchived audience is no longer visible in Archive tab
    And Verify the unarchived audience appears in Active tab
