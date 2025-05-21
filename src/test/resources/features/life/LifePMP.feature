Feature: PMP Marketplace Regression - Sanity and Regression


  Scenario Outline: Verify premium deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And User clicks on Deals Targeting
    And User assigns Life Marketplace(Premium) deals
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to both targeting and PMP deals section
    Examples:
      | USER  | tacticPMP         |
      | Admin | TACTIC_0aa5cf1c-0 |

  Scenario Outline: Verify private deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And   User clicks on Deals Targeting
    And  User assigns private deals
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to both targeting and PMP deals section
    Examples:
      | USER  | tacticPMP         |
      | Admin | TACTIC_0aa5cf1c-0 |

  Scenario Outline: Verify premium deal can be assigned to a tactic with Toggle "Only Target Applied Deals" OFF
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And   User clicks on Deals Targeting
    And  User assigns Life Marketplace(Premium) deals
    And User turns the Only Target Applied deals OFF
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to only PMP deals section
    Examples:
      | USER  | tacticPMP       |
      | Admin | Tactic_0512_SSS |

  Scenario Outline: Verify private deal can be assigned to a tactic with Toggle "Only Target Applied Deals" OFF
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And   User clicks on Deals Targeting
    And  User assigns private deals
    And User turns the Only Target Applied deals OFF
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to only PMP deals section
    Examples:
      | USER  | tacticPMP       |
      | Admin | Tactic_0512_SSS |

  Scenario Outline: Verify filters of All Deals Tab of the PMP Modal
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And  User clicks on Deals Targeting
    Then Filters Exchange,Deal Type, Device Type, Ad Size,Floor Price Range, Estd. Avails and Compatible with this tactic checkbox should be present
    Examples:
      | USER  | tacticPMP       |
      | Admin | Tactic_0512_SSS |

  Scenario Outline: Verify filters of Private Deals tab of the PMP Modal
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And  User clicks on Deals Targeting
    And User navigates to private deals tab
    Then Filters Exchange,Deal Type, Device Type, Ad Size,Floor Price Range, Estd. Avails and Compatible with this tactic checkbox should be present
    Examples:
      | USER  | tacticPMP       |
      | Admin | Tactic_0512_SSS |

  Scenario Outline: Verify filters of Premium Deals tab of the PMP Modal
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And  User clicks on Deals Targeting
    And User navigates to Life Marketplace deals tab
    Then Filters Exchange,Deal Type,Channel, Device Type, Ad Size,Floor Price Range, Estd. Avails and Compatible with this tactic checkbox should be present
    Examples:
      | USER  | tacticPMP       |
      | Admin | Tactic_0512_SSS |

  Scenario Outline: Verify Curated markets can be created
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to the curated markets administration portal
    And User clicks on Create Curated Markets
    And User fills Curated markets mandatory fields and clicks on Save
    Then Curated Market should be created successfully

  Scenario Outline: Verify Deals can be imported for the newly created curated markets
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to the curated markets administration portal
    And User clicks on Create Curated Markets
    And User fills Curated markets mandatory fields and clicks on Save
    And  Curated Market should be created successfully
    And User clicks on Import Deals
    And User Uploads correct template and clicks Preview button
    And User clicks OK button of Import Deals if there are no errors
    Then Deals should be imported

  Scenario Outline: Verify Deals(Standalone) Page has three tabs
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to Deals page
    Then User should see three tabs - All Deals, Private Deals and Life Marketplace Deals

  Scenario Outline: Verify User can create Private deals through Deals(Standalone) page
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to Deals page
    And User navigates to Private Deals tab of the Deals page
    And User clicks on Add New Private Deal button
    And User fills the mandatory fields of New Deal Pop up and clicks on Save
    Then Deal should be created successfully



