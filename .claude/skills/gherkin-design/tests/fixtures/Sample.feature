Feature: Sample request validation

  # Source: QA-100-R01 TC_QA-100_01 TC_QA-100_03
  @todo
  Scenario Outline: Save a valid request
    Given A request with "<REQUEST_STATUS>" status is available
    When The request is saved
    Then The request is persisted with an active status
    Examples:
      | REQUEST_STATUS |
      | ACTIVE         |
      | APPROVED       |

  # Source: QA-100-R01 TC_QA-100_02
  @todo
  Scenario: Reject an incomplete request
    Given A request is missing required data
    When The request is saved
    Then The request is rejected
