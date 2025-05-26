Feature: LIFE Regression - Create Static NPI List

  @jenkins2005
  Scenario Outline: Create Static NPI List by specifying NPI Numbers.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    Then Verify creation of NPI List screen is displayed
    And User selects Static List
    And User enters the NPI list details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>"
    When User makes list available in LIFE and saves the list
    Then Verify list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | STATIC_NPI |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Smart Pixel.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Smart Pixel
   Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_Pixel_NPI |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying NPI List.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for NPI List
   Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying NPI List and Expand Practice.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the Smart NPI list with Expand based on practice and hospital affiliation details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for NPI List
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Speciality.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Specialty
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_SPECIALITY_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Profession.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Profession
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_PROFESSION_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Prescriber Drug.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Prescribed Drug
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_PRESCRIBED_DRUG_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Diagnosis.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Diagnosis
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_DIAGNOSIS_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Medical Procedure.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Medical Procedure
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_MEDICAL_PROCEDURE_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Endemic Research.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Endemic Research
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_ENDEMIC_RESEARCH_NPI_LIST |

  @jenkins2005
  Scenario Outline: Create Smart NPI List by specifying Prescription Behavior Change.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>" for Prescription Behavior Change
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | SMART_PRESCRIPTION_CHANGE_NPI_LIST |


  @jenkinsSwitch
  Scenario Outline: Create Smart NPI List by specifying Type.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    And User selects Smart List to create NPI list
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for "<Type>"
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER      | LIST_NAME       |Type|
      | 01- Advertiser  | SMART_Pixel_NPI |Smart Pixel|



