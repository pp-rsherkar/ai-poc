*** Settings ***
Test Setup        test setup
Test Teardown     test teardown
Resource          res_general.txt

*** Test Cases ***
update_test_with_pass_result
    [Documentation]    Test that Glados can update a test result on TestRail to Pass. This should update the test status as well as the additional details such as time elapsed, browser, and browser version.
    ...    https://stg-squash.internetbrands.com/squash/test-case-workspace/test-case/696553/steps?anchor=steps
    ...    http://testrail.internetbrands.com/testrail/index.php?/cases/view/1854450
    # [Setup]    glados setup
    open browser    https://www.internetbrands.com/    chrome    remote_url=${REMOTE}
    sleep    10
    # Capture Page Screenshot And Add To Squash Image Attachments    screenshot_1
    capture_page_screenshot_and_return_url    screenshot_test    width=500
    Log    ${OUTPUT DIR}${/}screenshot_1.png
    # Append To List    ${SQUASH EXECUTION IMAGES}    ${OUTPUT DIR}${/}screenshot_1.png
    pass execution    This should always pass and mark the result on TestRail as Passed.

update_test_with_fail_result
    [Documentation]    Test that Glados can update a test result on TestRail to Fail. This should update the test status as well as the additional details such as time elapsed, browser, and browser version.
    ...    https://stg-squash.internetbrands.com/squash/test-case-workspace/test-case/696554/steps?anchor=steps
    ...    http://testrail.internetbrands.com/testrail/index.php?/cases/view/1854451
    # [Setup]    glados setup
    fail    This test should Fail, and mark it on TestRail with a Fail status.

update_test_with_retest_result
    [Documentation]    Test that using the keyword, 'override test result to retest' will mark the test as a retest.
    # [Setup]    glados setup
    override test result to retest

update_test_with_blocked_result
    [Documentation]    Test that using the keyword, 'override test result to blocked' will mark the test as blocked.
    ...    https://stg-squash.internetbrands.com/squash/test-case-workspace/test-case/696556/steps?anchor=steps
    # [Setup]    glados setup
    #run keyword and ignore error    override test result to blocked
    override test result to blocked
    Log    test if it is logged

update_test_with_na_result
    [Documentation]    Test that using the keyword, 'override test result to na' will mark the test as N/A.
    # [Setup]    glados setup
    run keyword and ignore error    override test result to na

update_test_with_time_elapsed
    [Documentation]    Test that updating a result on TestRail includes the time elapsed.
    ...
    ...    Additional Notes:
    ...    - If elapsed time is less than one second, it should force the time displayed on TestRail as 1 second, since TestRail throws an error for times less than 1 second.
    # [Setup]    glados setup
    pass execution    This should always pass.


test_with_many_steps
    [Documentation]    this is a test with 50 steps
    FOR    ${x}    IN RANGE    50
        Log    step ${x}
        ${step} =    Evaluate    ${x} + 1
        Set Test Variable    ${last_step_started}    ${step}
    END
    #FAIL    testing failing on a step
    log    this is a log message


embed_screenshot_in_comment
    log    ${LOG FILE}
    log    ${OUTPUT DIR}
    capture page screenshot and return url    test image

# update_test_via_hardcoded_run_id_and_case_id
#     [Documentation]    http://testrail.internetbrands.com/testrail/index.php?/cases/view/3520453
#     pass execution    This should always pass.
#     [Teardown]    run keywords    update testrail    19940    3520453    ### TODO: change the numbers here ...
#     ...    AND    close all browsers

should_not_hard_fail_if_browser_closed_before_teardown
    close all browsers
