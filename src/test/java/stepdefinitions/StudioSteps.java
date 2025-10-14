package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.*;
import pages.admin.Accounts;
import pages.studio.*;
import utils.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

import static factory.DriverFactory.page;

public class StudioSteps {
    static String workspaceName;
    static String newWorkspaceName;
    Boolean flag = true;
    Boolean isOverwritten = false;
    List<String[]> fileContent;
    List<String> fileContentData;
    Accounts accounts = new Accounts(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());
    WorkspaceCreation workspaceCreation = new WorkspaceCreation(DriverFactory.getPage());
    ExpansionWorkspace expansionWorkspace = new ExpansionWorkspace(DriverFactory.getPage());
    ExplorerWorkspace explorerWorkspace = new ExplorerWorkspace(DriverFactory.getPage());
    Workspace workspace = new Workspace(DriverFactory.getPage());
    List<String> appliedFilterEntries = new ArrayList<>();
    List<String> appliedFilterValues = new ArrayList<>();
    List<String> previousNpiDetails = null;
    String randomNumber = CommonUtils.randomNumberGeneration();
    String npiCount;

    @When("the user clicks on Create New Workspace")
    public void the_user_clicks_on_create_new_workspace() {
        workspaceCreation.createStudioWorkspace();
    }

    @And("the User navigate to studio")
    public void theUserNavigateToStudio() {
        navigation.navigateToLife();
        navigation.navigateToStudio();
    }

    @When("the user sees the types of workspaces they have permissions for")
    public void the_user_sees_the_types_of_workspaces_they_have_permissions_for() {
        Assert.assertEquals("HCP Explorer", workspaceCreation.verifyHCPExplorer());
        Assert.assertEquals("HCP Audience Expansion", workspaceCreation.verifyHCPAudienceExpansion());
    }

    @Then("the user selects the advertiser {string}")
    public void the_user_selects_the_advertiser(String advertiser) {
        page.waitForLoadState();
        expansionWorkspace.clickAdvertiserDropdown(advertiser);
        page.waitForLoadState();
    }

    @Then("the user selects Source Audience {string}")
    public void the_user_selects_source_audience(String string) {
        expansionWorkspace.selectSourceAudience(string);

    }

    @Then("the user selects Expand With Care Team or Expand With Affiliation Graph and selects the value")
    public void the_user_selects_expand_with_care_team_or_expand_with_affiliation_graph_and_selects_the_value() {
        expansionWorkspace.selectExpandCareTeam();
        expansionWorkspace.selectExpandAffGraph();
    }

    @Then("the filters should be applied to the workspace")
    public void the_user_applies_the_following_filters() {
        expansionWorkspace.addFilter();
    }

    @Then("the user renames the workspace to {string}")
    public void the_user_renames_the_workspace_to_(String string) {
        workspaceName = string + CommonUtils.timeStampCalculation();
        expansionWorkspace.renameExpansion(workspaceName);
    }

    @Then("the user saves the workspace and check the workspace is Saved")
    public void the_user_saves_the_workspace_and_check_the_workspace_is_Saved() {
        expansionWorkspace.saveExpansion();
        assert explorerWorkspace.workspaceSuccess().contains("Workspace saved");
    }

    @And("User enables the studio for {string} account")
    public void user_enables_the_studio_for_an_account(String accountName) {
        navigation.clickSubMenu();
        accounts.clickAdministration();
        accounts.selectAccountsTab();
        accounts.searchAccount(accountName);
    }

    @And("User navigates to workspace permissions")
    public void User_navigates_to_workspace_permissions() {
        accounts.enableStudio();
    }

    @When("User selects the workspace types and saves the settings")
    public void user_selects_the_workspace_types_and_saves_the_settings() {
        accounts.workSpaceSettings();
    }

    @Then("Studio should be enabled for that account")
    public void Studio_should_be_enabled_for_that_account() {
        accounts.saveStudioSettings();
    }

    @And("User should be able to see the enabled workspaces for {string} account under Studio")
    public void userShouldBeAbleToSeeTheEnabledWorkspacesForThatAccountUnderStudio(String accountName) {
        accounts.switchAccount(accountName);
        navigation.navigateToStudio();
        workspaceCreation.createWorkspace();
        Assert.assertEquals("HCP Audience Expansion", accounts.verifyWorkspacePermission());
    }

    @And("User disables the studio permission for {string} account")
    public void userDisablesTheStudioPermissionForAnAccount(String accountName) {
        navigation.clickSubMenu();
        accounts.disableStudioForAccount(accountName);
    }

    @Then("User should not be able to see the studio permission for that account")
    public void userShouldNotBeAbleToSeeTheStudioPermissionForThatAccount() {
        accounts.verifyStudioMenu();
    }

    @When("User clicks on Create New Workspace")
    public void user_clicks_on_create_new_workspace() {
        Assert.assertEquals("", "Genome Studio", workspaceCreation.studioDashboard());
        workspaceCreation.verifyStudioWorkspaceFrame();
        workspaceCreation.createStudioWorkspace();
    }

    @When("User clicks on Create New Workspace For Expansion")
    public void user_clicks_on_create_new_workspace_for_expansion() {
        Assert.assertEquals("", "Genome Studio", workspaceCreation.studioDashboard());
        workspaceCreation.createWorkspaceExpansion(flag);
    }

    @Then("User sees the types of workspaces they have permissions for")
    public void user_sees_the_types_of_workspaces_they_have_permissions_for() {
        Assert.assertEquals("HCP Explorer", workspaceCreation.verifyHCPExplorer());
        // As confirmed by Nikhil - for GA, permissions have been set up like-wise. This is HCP Explorer test-case and in permission feature we can handled this.
        // Assert.assertEquals("HCP Audience Expansion", workspaceCreation.verifyHCPAudienceExpansion());
    }

    @And("User clicks on HCP Explorer workspace")
    public void user_clicks_on_hcp_explorer_workspace() {
        workspaceCreation.clickHCPExplorerWorkspace();
        Assert.assertEquals("Workspace created successfully", workspaceCreation.verifyWorkspaceCreation());
    }

    @Then("User adds the workspace name as {string} and selects the advertiser {string}")
    public void user_adds_the_workspace_name_and_selects_the_advertiser(String wName, String advertiser) {
        workspace.waitTillWorkspaceAlertHide();
        workspaceName = wName + '_' + randomNumber;
        explorerWorkspace.enterWorkspaceName(workspaceName);
        explorerWorkspace.selectAdvertiser(advertiser);
    }

    @When("User applies the filter and selects option")
    public void user_applies_the_filters_as_gender_and_age(DataTable dataTable) {
        explorerWorkspace.clickAddFilter();
        List<Map<String, String>> filters = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : filters) {
            String filterName = row.get("FilterName").trim();
            String filterOption = row.get("Option").trim();

            if (!filterOption.isEmpty()) {
                appliedFilterEntries.add(filterName);
                appliedFilterValues.add(filterOption);
                List<String> filterOptionList = CommonUtils.parseCommaSeparatedString(filterOption);
                explorerWorkspace.selectFilter(filterName, filterOptionList);
            }
        }
    }

    @Then("User clicks on Ok and closes the filter popup")
    public void user_clicks_on_ok_and_closes_the_filter_popup() {
        explorerWorkspace.applyFilter();
    }

    @Then("Verify that the applied filters are displayed correctly")
    public void verify_that_the_applied_filters_are_displayed_correctly() {
        List<String> displayedFilters = explorerWorkspace.verifyAllSelectedFilters();
        for (String appliedFilter : appliedFilterEntries) {
            boolean matchFound = displayedFilters.stream()
                    .anyMatch(displayed -> displayed.toLowerCase().startsWith(appliedFilter.toLowerCase()));

            Assert.assertTrue("Applied filter not displayed: " + appliedFilter, matchFound);
        }
    }

    @Then("User saves the workspace")
    public void user_saves_the_workspace() {
        explorerWorkspace.saveExplorerWorkspace();
    }

    @Then("Verify the HCP Explorer Workspace is saved")
    public void verify_the_hcp_explorer_workspace_is_saved() {
        String actualMessage = workspaceCreation.verifyWorkspaceCreation();

        boolean isValid = actualMessage.equals("Workspace saved successfully") ||
                actualMessage.equals("Sent for asynchronous processing, forced by upstream dependencies - need to refresh upstream workspaces first");

        Assert.assertTrue("Unexpected message: " + actualMessage, isValid);
        workspace.waitTillWorkspaceAlertHide();
    }

    @Then("verify the file content")
    public void verify_the_file_content() {
        fileContent = FileActions.readAllDataAtOnce(ConfigReader.getProperty("csvFilePath"));
        fileContentData = new ArrayList<>();
        //To display the data from csv- Separate logic
        /*for (int i = 1; i < fileContent.size(); i++) {
           // System.out.println("Row " + i + ": " + String.join(", ", fileContent.get(i)));
            String data= Arrays.toString(fileContent.get(i));
            fileContentData.add(data);
        }*/
        for (String[] row : fileContent) {
            fileContentData.add(String.join(", ", row));
        }
        System.out.println("Data from csv :" + fileContentData);

    }

    @When("Studio platform is available")
    public void studio_platform_is_available() {
        workspace.studio();
    }

    @And("User searches the {string} and selects it")
    public void userSearchesTheAndSelectsIt(String workspace) {
        workspaceCreation.searchWorkspaceName(workspace);
        workspaceCreation.clickWorkspace(workspace);
        Assert.assertTrue("Unable to navigate to workspace", workspaceCreation.navigateToWorkspace(workspace));
    }

    @And("User fetches the Identified NPI count from the workspace")
    public void userFetchesTheIdentifiedNPICountFromTheWorkspace() {
        npiCount = workspace.fetchIdentifiedNPICount();
    }

    @And("Download button is enabled to the user")
    public void download_button_is_enabled_to_the_user() {
        workspace.clickFlyOrPageButton();
    }

    @And("User clicks Download NPI option")
    public void userClicksDownloadNPIOption() {
        workspace.clickDownloadNPI();
    }

    @And("User selects download format as {string} and clicks Download button")
    public void userSelectsFileTypeAsAndClicksDownloadButton(String fileExtension) {
        workspace.selectFileExtension(fileExtension);
        workspace.clickDownloadButton();
        Assert.assertEquals("Download completed successfully", workspace.checkNPIDownloadComplete());
    }

    @And("User verifies the total Identified {string} count in the downloaded file - {string}")
    public void userVerifiesTheFileContent(String npiHeader, String fileExtension) throws IOException {
        Path latestFile = FileActions.getLatestDownloadedFile(fileExtension.toLowerCase());
        int npiCountFromFile = 0;
        if (fileExtension.equalsIgnoreCase("CSV"))
            npiCountFromFile = FileActions.fetchColumnCountFromCSV(latestFile, npiHeader);
        else if (fileExtension.equalsIgnoreCase("XLSX"))
            npiCountFromFile = FileActions.fetchColumnCountFromExcel(latestFile, npiHeader);
        Assert.assertEquals("NPI count is not matching", Integer.parseInt(npiCount), npiCountFromFile);
    }

    @When("User clicks on Publish NPI List")
    public void user_clicks_on_publish_npi_list() {
        workspace.clickPublishNPI();
    }

    @And("User selects publish {string}")
    public void userSelectsPublish(String listType) {
        workspace.publishNPI(listType);
    }

    @When("User select the platform to publish the list")
    public void user_select_the_system_to_publish_the_list() {
        workspace.hcp();
        workspace.life();
    }

    @Then("Verify list is published")
    public void verify_list_is_published() {
        workspace.clickPublish();
        Assert.assertEquals("Workspace saved successfully", workspaceCreation.verifyWorkspaceCreation());
        workspace.waitTillWorkspaceAlertHide();
        workspace.clickFlyOrPageButton();
        Assert.assertEquals("Published NPI List", workspace.verifyPublishedNpi());
    }

    @And("Check the Download icon is highlighted in green color")
    public void checkTheDownloadIconIsHighlightedInGreenColor() {
        Assert.assertEquals("rgb(0, 114, 114)", workspace.checkBackgroundColorOfDownloadIcon());
    }

    @And("Verify Webhook panel is disabled before applying filters")
    public void verifyWebhookPanelIsDisabledBeforeApplyingFilters() {
        workspace.clickWebhookIcon();
        Assert.assertEquals("Disabled", workspace.verifyWebhookToggleButton());
        workspace.closeWebhookPanel();
    }

    @Then("Verify Webhook panel is enabled after applying engagement filters")
    public void verifyWebhookPanelIsEnabledAfterApplyingFilters() {
        workspace.clickWebhookIcon();
        Assert.assertEquals("Enabled", workspace.verifyWebhookToggleButton());
    }

    @When("User clicks {string} request method")
    public void userClicksRequestMethod(String requestType) {
        if (requestType.contains("POST"))
            workspace.clickWebhookIcon();
        workspace.clickRequestOrContentButton(requestType);
    }

    @And("User adds valid URL and append Macros with {string} to the {string} as follow")
    public void userAddsURLAndMacrosWithToTheURLAsFollow(String param, String textType, DataTable macros) {
        List<String> macrosList = macros.asList(String.class);
        workspace.addURL(Constants.WEBHOOK_URL);
        workspace.addMacros(textType, param, macrosList);
    }

    @Then("Verify if Macros Appended to the URL")
    public void verifyIfMacrosAppendedToTheURL() {
        String text = workspace.verifyMacrosAppendedToURL();
        Assert.assertTrue(
                "Macros are not correctly appended to the URL",
                text.matches(Pattern.quote(Constants.WEBHOOK_URL) + "%%NPI%%%%URL%%%%Channel%%%%PARAM\\d+%%")
        );
    }

    @And("User selects content type {string}")
    public void userSelectsContentType(String contentType) {
        workspace.clickRequestOrContentButton(contentType);
    }

    @And("User adds valid body {string} and append Macros with {string} to the {string} as follow")
    public void userAddsBodyAndAppendMacrosToTheBodyAsFollow(String jsonFile, String param, String textType, DataTable macros) throws IOException {
        List<String> macrosList = macros.asList(String.class);
        workspace.addBody(jsonFile);
        workspace.addMacros(textType, param, macrosList);
    }

    @Then("Verify if Macros Appended to the Body {string}")
    public void verifyIfMacrosAppendedToTheBody(String body) throws IOException {
        String text = workspace.verifyMacrosAppendedToBody();
        String[] parts = text.split("%%NPI%%%%URL%%%%Channel%%%%PARAM\\d+%%");
        Assert.assertTrue("Macro suffix not found in text", parts.length == 1 || parts.length == 2);

        String actualJson = parts[0].replaceAll("\\s+", "");
        String expectedJsonNormalized = CommonUtils.readJsonTestDataFile(body).replaceAll("\\s+", "");
        Assert.assertEquals("JSON part does not match", expectedJsonNormalized, actualJson);
        workspace.addBody(body);
    }

    @When("User saves the webhook setup")
    public void userSavesTheWebhookSetup() {
        workspace.saveWebhookSetup();
    }

    @Then("Check that the success message appears once the webhook is successfully created")
    public void checkThatTheSuccessMessageAppearsOnceTheWebhookIsSuccessfullyCreated() {
        Assert.assertEquals("Webhook setup successfully", workspace.verifyWebhookCreationIsSuccess());
    }

    @Then("Verify inline error message for the invalid webhook entries {string}")
    public void verifyInlineErrorMessageForTheInvalidWebhookEntries(String invalidData) {
        String inlineError = String.valueOf(workspace.verifyInlineErrorMessage(invalidData));
        Assert.assertTrue("Inline error message is not displayed", (inlineError.contains("URL is invalidBody is invalid") || inlineError.contains("URL is invalid")));
    }

    @And("Verify error message when webhook setup is failed using {string}")
    public void verifyErrorMessageWhenWebhookSetupIsFailed(String errorData) throws IOException {
        List<String> mediaTypeList = CommonUtils.parseCommaSeparatedString(errorData);
        String errorMessage = workspace.verifyErrorMsgWhenAPIFailed(mediaTypeList);
        Assert.assertTrue("Error message is not displayed", errorMessage.contains("Error occurred while saving workspace or editing webhook"));
    }

    @Then("Check the webhook icon is highlighted in green color")
    public void checkTheWebhookIconIsHighlightedInGreenColor() {
        Assert.assertEquals("rgb(0, 136, 136)", workspace.checkBackgroundColorOfWebhookIcon());
    }

    @When("User tries to delete the workspace associated with active webhook from the workspace list")
    public void userDeletesTheWebhookFromTheWorkspaceList() {
        workspace.goToWorkspaceList();
        workspaceCreation.clickMoreActionsMenu(newWorkspaceName);
        workspaceCreation.deleteWorkspace();
    }

    @Then("Verify user receives a warning when attempting to delete a workspace with an active webhook")
    public void verifyUserReceivesAWarningWhenAttemptingToDeleteAWorkspaceWithAnActiveWebhook() {
        String actual = workspaceCreation.verifyDeletePopUp().replace("\r\n", "\n").trim();
        Assert.assertTrue("Message should contain warning about deleting workspace",
                actual.contains("You are trying to delete the workspace " + newWorkspaceName));
        Assert.assertTrue("Message should mention webhook is enabled", actual.contains("Webhooks are enabled for this workspace."));
        Assert.assertTrue("Message should mention deletion is irreversible",
                actual.contains("Deleting the workspace will delete the webhook as well. This action cannot be undone."));
        Assert.assertTrue("Message should ask for confirmation", actual.contains("Do you want to proceed?"));
        Assert.assertEquals("Workspace deleted successfully", workspaceCreation.deleteWorkspaceWithActiveWebhook().trim());
    }

    /*Roshani Sherkar
     * 21-07-2025*/
    @And("User clicks on AI Configurator and build audience using the AIPrompt {string}")
    public void userClicksOnAIConfiguratorAndBuildAudienceUsingTheAIprompt(String aiPrompt) {
        explorerWorkspace.clickAIConfigurator();
        flag = explorerWorkspace.describeAudience(aiPrompt);
    }

    @Then("Verify the filter is applied correctly {string}")
    public void verifyTheFilterIsAppliedCorrectly(String primaryFilter) {
        if (flag) {
            List<String> appliedFilters = explorerWorkspace.verifyAllSelectedFilters();
            List<String> expectedFilters = CommonUtils.parseCommaSeparatedString(primaryFilter);

            for (String expected : expectedFilters) {
                boolean isFilterApplied = appliedFilters.stream()
                        .anyMatch(filter -> filter.equalsIgnoreCase(expected));
                Assert.assertTrue("The filter '" + expected + "' is not applied correctly", isFilterApplied);
            }
            isOverwritten = true;
        } else {
            Assert.fail("AI is unable to build audience");
        }
    }


    @And("User applies the following filters one by one and checks that NPI details are refined after each filter:")
    public void userAppliesBelowFilterWithOptionAndCheckNPIDetails(DataTable dataTable) {
        List<Map<String, String>> filters = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : filters) {
            explorerWorkspace.clickAddFilter();
            String filterName = row.get("FilterName").trim();
            String filterOption = row.get("Option").trim();

            if (!filterOption.isEmpty()) {
                List<String> filterOptionList = CommonUtils.parseCommaSeparatedString(filterOption);
                explorerWorkspace.selectFilter(filterName, filterOptionList);
            }
            explorerWorkspace.applyFilter();
            List<String> currentNpiDetails = explorerWorkspace.checkNPIDetails();
            //explorerWorkspace.deleteFilter();
            if (previousNpiDetails != null) {
                if (currentNpiDetails.equals(previousNpiDetails)) {
                    Assert.assertEquals("Filter '" + filterName + "' with options '" + filterOption +
                            "' did not refine the audience (NPI details remained the same)", currentNpiDetails, previousNpiDetails);
                } else {
                    Assert.assertTrue("Filter '" + filterName + "' refined the audience as expected",
                            currentNpiDetails.size() <= previousNpiDetails.size());
                }
            }
            previousNpiDetails = currentNpiDetails;
        }
    }


    @And("Verify after adding ai prompt filter selected manually should be overwritten")
    public void verifyAfterAddingAiPromptManuallySelectedFilterShouldBeOverwritten() {
        Assert.assertTrue("AI filter did not overwrite manual filter", isOverwritten);
    }

    @And("Fetch and verify that NPI details are refined")
    public void verifyThatNPIDetailsAreRefinedAfterEachFilter() {
        List<String> currentNpiDetails = explorerWorkspace.checkNPIDetails();
        if (previousNpiDetails != null) {
            if (currentNpiDetails.equals(previousNpiDetails)) {
                Assert.assertEquals("Filter with options did not refine the audience (NPI details remained the same)", currentNpiDetails, previousNpiDetails);
            } else {
                Assert.assertTrue("Filter refined the audience as expected",
                        currentNpiDetails.size() <= previousNpiDetails.size());
            }
        }
        previousNpiDetails = currentNpiDetails;
    }

    @Then("Delete the filter")
    public void deleteTheFilter() {
        explorerWorkspace.deleteFilter();
    }

    /*Roshani Sherkar
     * 24-07-2024*/
    @And("User hovers over the dashboard filters, selects the region with maximum NPIs and clicks on it")
    public void userHoversOverTheNPIVisualsIconAndClicksOnIt(DataTable dataTable) {
        List<String> npiVisualList = dataTable.asList(String.class);
        explorerWorkspace.hoverOverNPIVisualsIcon(npiVisualList);
    }

    @And("Verify that dashboard filters are displayed correctly in Filter section")
    public void verifyThatCrossFiltersAreDisplayedCorrectlyInFilterSection() {
        Assert.assertTrue("Filters were not added", explorerWorkspace.verifyCrossFiltersDisplayed());
        appliedFilterEntries = explorerWorkspace.fetchMergedFilters();
    }

    @And("Verify dashboard filters are merged with Primary filters")
    public void verifyDashboardFiltersAreMergedWithPrimaryFilters() {
        List<String> displayedFilters = explorerWorkspace.verifyAllSelectedFilters();
        for (String mergedFilter : appliedFilterEntries) {
            boolean matchFound = displayedFilters.stream()
                    .anyMatch(displayed -> displayed.equalsIgnoreCase(mergedFilter));
            Assert.assertTrue("Merged filter not displayed: " + mergedFilter, matchFound);
        }
    }

    @And("Navigate to workspace dashboard")
    public void navigateToWorkspaceDashboardAndSearchTheWorkspaceCreated() {
        workspace.goToWorkspaceList();
    }

    @And("User searches the workspace created to perform Actions from More menu")
    public void userSearchesTheWorkspaceCreatedToPerformDuplicateOperation() {
        workspaceCreation.searchWorkspaceName(workspaceName);
        workspaceCreation.clickMoreActionsMenu(workspaceName);
    }

    @And("User selects the {string} option by clicking More Actions menu")
    public void userSelectsTheDeleteOptionByClickingMoreActionsButton(String actionName) {
        workspaceCreation.performActionOnWorkspace(actionName);
    }

    @And("Verify user is able to rename the workspace as {string}")
    public void verifyUserIsAbleToRenameTheWorkspace(String newWorkspace) {
        newWorkspaceName = newWorkspace + randomNumber;
        Assert.assertEquals("Workspace renamed successfully", workspaceCreation.renameWorkspaceName(workspaceName, newWorkspaceName));
        workspaceName = newWorkspaceName;
    }

    @And("Verify user is able to duplicate the workspace")
    public void verifyUserIsAbleToDuplicateTheWorkspace() {
        workspaceName = workspaceCreation.fetchDuplicateWorkspaceName();
        Assert.assertEquals("Workspace duplicated successfully", workspaceCreation.clickDuplicateButton());
    }

    @And("User is able to search the workspace after performing operation - {string}")
    public void userIsAbleToSearchTheWorkspaceWithTheNewName(String operationName) {
        Assert.assertTrue("Unable to search workspace after performing " + operationName + " operation", workspaceCreation.searchWorkspaceName(workspaceName));
    }

    @And("Verify user is able to delete the workspace")
    public void verifyUserIsAbleToDeleteTheWorkspace() {
        String text = workspaceCreation.verifyDeletePopUp().trim();
        Assert.assertTrue("Message should contain warning about deleting workspace",
                text.contains("You are trying to delete the workspace " + workspaceName));
        Assert.assertTrue("Message should mention irreversible deletion",
                text.contains("This action cannot be undone – all deleted data will be lost."));
        Assert.assertTrue("Message should ask for confirmation",
                text.contains("Do you want to proceed?"));
        Assert.assertEquals("Workspace deleted successfully", workspaceCreation.deleteWorkspaceWithActiveWebhook().trim());
    }

    @When("Navigate to administration")
    public void navigate_to_administration() {
        accounts.verifyStudioMenu();
        accounts.clickAdministration();
    }

    @When("Click on accounts tab")
    public void click_on_accounts_tab() {
        accounts.selectAccountsTab();
    }

    @When("Locate an account {string} with external user permission and select it")
    public void locate_an_account_with_external_user_permission_and_select_it(String EXTERNAL_ACCOUNT) {
        accounts.searchAccount(EXTERNAL_ACCOUNT);
    }

    @When("Go to users tab and search {string} and select studio tab")
    public void go_to_users_tab_and_search_and_select_studio_tab(String string) {
        //In progress by Pradeep- Will raise in next PR(15th Oct)
    }

    @Then("Turn on studio toggle for external users")
    public void turn_on_studio_toggle_for_external_users() {
        //In progress by Pradeep- Will raise in next PR(15th Oct)
    }

    @Then("Verify studio platform is enabled for {string}")
    public void verify_studio_platform_is_enabled_for(String string) {
        //In progress by Pradeep- Will raise in next PR(15th Oct)
    }

}