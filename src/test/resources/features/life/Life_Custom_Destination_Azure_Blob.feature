Feature: Azure Blob Storage as a Custom Destination in Report Builder and Account Administration

  1. Adds Azure Blob Storage as a selectable Destination Type with a SAS URL / SAS Connection String
     auth tab switcher, alongside the existing FTP, SFTP, GCS, and S3 destination types.
  2. Test access validates SAS credential permissions (read+write, read-only, invalid) via the
     backend connection-validation API; the FE only surfaces the outcome returned by the backend.
  3. Per Ambiguity §8, SAS URL expiry is covered only for reactive failure handling in this release;
     proactive expiry warnings are an open requirement question and are out of scope here.
  4. Each scenario walks a single continuous pass through Admin > Reporting and Report Builder,
     chaining the checks reachable along that path, per the domain's existing workflow-driven style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: TC-01, TC-02, TC-03, TC-04, TC-05, TC-06, TC-20
  @todo
  Scenario: Verify the Azure Blob Storage destination type, auth tab switcher, and tooltip content in Admin Reporting
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "automation@pulsepoint" and selects the account
    And User navigates to Reporting tab
    Then User clicks Add Destination button
    And Verify that Destination Type has values "FTP, SFTP, Google Cloud Storage, Amazon S3, Azure Blob Storage"
    When User selects "Azure Blob Storage" as the Destination Type
    Then A two-tab switcher is displayed with "SAS URL" and "SAS Connection String"
    When User selects the "SAS URL" tab
    Then The SAS URL input and Path URL field are displayed
    And User hovers over the "SAS URL" question icon and fetches tool-tip "A URL that grants permission to upload files to a specific Azure Blob container without sharing account credentials"
    When User selects the "SAS Connection String" tab
    Then The SAS Connection String input and Path URL field are displayed
    And User hovers over the "SAS Connection String" question icon and fetches tool-tip "A pre-authenticated connection string that grants permission to upload files without sharing account credentials"
    # Regression anchor: PROD-13840 Open Question - Account Key / Azure AD auth is explicitly out of scope
    Then Only "SAS URL" and "SAS Connection String" are offered as authentication methods, with no Account Key or Azure AD option

  # Source: TC-07, TC-08, TC-09, TC-10
  @todo
  Scenario Outline: Verify SAS URL format validation for the Azure Blob Storage destination
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "automation@pulsepoint" and selects the account
    And User navigates to Reporting tab
    Then User clicks Add Destination button
    When User selects "Azure Blob Storage" as the Destination Type
    And User selects the "SAS URL" tab
    And User enters "<SAS_URL>" in the SAS URL field
    Then Verify the SAS URL validation result is "<EXPECTED_RESULT>"
    Examples:
      | SAS_URL                                                                           | EXPECTED_RESULT             |
      | https://rxusmkthubprodsac.blob.core.windows.net/pulse-point?sv=2024-11-04&sig=xyz | Accepted, no format error   |
      | https://example.com/container?sig=x                                               | Invalid or expired SAS URL. |
      | https://acct.blob.core.windows.net/c?sv=2024-11-04                                | Rejected as malformed       |
      | https://acct.blob.core.windows.net/c?sv=2024-11-04&se=2020-01-01&sig=expiredtoken | Invalid or expired SAS URL. |

  # Source: TC-11, TC-13, TC-14
  @todo
  Scenario Outline: Verify Test access validates Azure Blob Storage SAS credential permissions
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "automation@pulsepoint" and selects the account
    And User navigates to Reporting tab
    Then User clicks Add Destination button
    When User selects "Azure Blob Storage" as the Destination Type
    And User selects the "SAS URL" tab
    And User enters "<SAS_URL>" in the SAS URL field
    And The backend connection validation API returns "<BACKEND_PERMISSION>"
    And User clicks Test Connection link to verify if connection happened successfully
    Then Verify the Test access message is "<PRIMARY_MESSAGE>"
    And Verify the Test access secondary message is "<SECONDARY_MESSAGE>"
    And Verify the Test access control state is "<CONTROL_STATE>"
    Examples:
      | SAS_URL                                                          | BACKEND_PERMISSION | PRIMARY_MESSAGE                                                     | SECONDARY_MESSAGE                                                         | CONTROL_STATE      |
      | https://rxusmkthubprodsac.blob.core.windows.net/rw?sv=1&sig=full | READ_WRITE         | Connection successful. Permissions for file transfer validated.     |                                                                           | Hidden or disabled |
      | https://rxusmkthubprodsac.blob.core.windows.net/bad?sv=1&sig=bad | INVALID            | Connection failed. Please validate the SAS URL / connection string. |                                                                           | Enabled            |
      | https://rxusmkthubprodsac.blob.core.windows.net/ro?sv=1&sig=ro   | READ_ONLY          | Connection successful.                                              | SAS is read-only. Please update permissions to allow file read and write. | Enabled            |

  # Source: TC-12
  @todo
  Scenario: Verify editing a validated SAS field resets Test access state
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "automation@pulsepoint" and selects the account
    And User navigates to Reporting tab
    Then User clicks Add Destination button
    When User selects "Azure Blob Storage" as the Destination Type
    And User selects the "SAS URL" tab
    And User enters "https://rxusmkthubprodsac.blob.core.windows.net/rw?sv=1&sig=full" in the SAS URL field
    And The backend connection validation API returns "READ_WRITE"
    And User clicks Test Connection link to verify if connection happened successfully
    Then Verify the Test access message is "Connection successful. Permissions for file transfer validated."
    When User edits the SAS URL field after a successful Test access
    Then The success message disappears and Test access reappears

  # Source: TC-16
  @todo
  Scenario: Verify the Azure Blob Storage destination dropdown is fully hidden when SAS permission is removed
    # Regression anchor: QA-1603 - dropdown previously stayed visible after SAS permission removal; confirmed fixed, verify no regression
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "automation@pulsepoint" and selects the account
    And User navigates to Reporting tab
    And SAS permission is removed for the account
    Then The entire Azure Blob Storage destination dropdown entry is hidden, not just its input fields

  # Source: TC-15, TC-19
  @todo
  Scenario: Verify Azure Blob Storage destination consistency across Report Builder and Account Reporting admin surfaces
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "automation@pulsepoint" and selects the account
    And User navigates to Reporting tab
    Then User clicks Add Destination button
    And Verify that Destination Type has values "FTP, SFTP, Google Cloud Storage, Amazon S3, Azure Blob Storage"
    And A two-tab switcher is displayed with "SAS URL" and "SAS Connection String"
    When User clicks PulsePoint icon to navigate back to Life
    And User navigates to Report Templates page
    And User navigates to run report from mega menu of the life application
    And User clicks on "Custom Destination" tab as Delivery Method
    Then Verify Destination Name, Destination Type fields are displayed
    And Verify that Destination Type has values "FTP, SFTP, Google Cloud Storage, Amazon S3, Azure Blob Storage"
    # Coverage note: HCP365 surfaces (Custom Destination, Run Report, Schedule Report) are excluded here.
    # No HCP destination/report automation hooks exist in HcpSteps.java today - see Codebase Alignment Summary.

  # Source: TC-18
  @todo
  Scenario Outline: Verify existing destination types remain unaffected by the Azure Blob Storage addition
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "automation@pulsepoint" and selects the account
    And User navigates to Reporting tab
    Then User clicks Add Destination button
    And User enters Destination details - "Auto_Destination_", "<DESTINATION_TYPE>", "ma2-qa-automation01", "22"
    And User clicks Test Connection link to verify if connection happened successfully
    And User saves the custom destination
    Examples:
      | DESTINATION_TYPE     |
      | FTP                  |
      | SFTP                 |
      | Google Cloud Storage |
      | Amazon S3            |
