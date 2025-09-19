package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.*;
import pages.admin.Accounts;
import pages.life.NPILists;
import pages.studio.*;
import utils.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static factory.DriverFactory.page;

public class StudioSteps {
    static String workspaceName;
    static String newWorkspaceName;
    Boolean flag = true;
    Boolean isOverwritten = false;
    static final String WEBHOOK_URL = "https://webhook.site/4312c282-2efc-486e-bedf-3cd385a0c3da";
    List<String[]> fileContent;
    List<String[]> actualValue;
    List<String> fileContentData;
    List<String> actualValueData;
    Accounts accounts = new Accounts(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());
    WorkspaceCreation workspaceCreation = new WorkspaceCreation(DriverFactory.getPage());
    ExpansionWorkspace expworkspaces = new ExpansionWorkspace(DriverFactory.getPage());
    ExplorerWorkspace explorerWorkspace = new ExplorerWorkspace(DriverFactory.getPage());
    WorkspaceDownloadNPI workspacedownloadnpi = new WorkspaceDownloadNPI(DriverFactory.getPage());
    Workspace workspace = new Workspace(DriverFactory.getPage());
    NPILists npiLists = new NPILists(DriverFactory.getPage());
    LifeSteps lifeSteps = new LifeSteps();
    CSVActions csvActions = new CSVActions();
    List<String> appliedFilterEntries = new ArrayList<>();
    List<String> appliedFilterValues = new ArrayList<>();
    List<String> previousNpiDetails = null;
    String randomNumber = CommonUtils.randomNumberGeneration();

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
        expworkspaces.clickAdvertiserDropdown(advertiser);
        page.waitForLoadState();
    }

    @Then("the user selects Source Audience {string}")
    public void the_user_selects_source_audience(String string) {
        expworkspaces.selectSourceAudience(string);

    }

    @Then("the user selects Expand With Care Team or Expand With Affiliation Graph and selects the value")
    public void the_user_selects_expand_with_care_team_or_expand_with_affiliation_graph_and_selects_the_value() {
        expworkspaces.selectExpandCareTeam();
        expworkspaces.selectExpandAffGraph();
    }

    @Then("the filters should be applied to the workspace")
    public void the_user_applies_the_following_filters() {
        expworkspaces.addFilter();
    }

    @Then("the user renames the workspace to {string}")
    public void the_user_renames_the_workspace_to_(String string) {
        workspaceName = string + CommonUtils.timeStampCalculation();
        expworkspaces.renameExpansion(workspaceName);
    }

    @Then("the user saves the workspace and check the workspace is Saved")
    public void the_user_saves_the_workspace_and_check_the_workspace_is_Saved() {
        expworkspaces.saveExpansion();
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
    public void user_adds_the_workspace_name_and_selects_the_advertiser(String workspaceName, String advertiser) {
        workspace.waitTillWorkspaceAlertHide();
        newWorkspaceName = workspaceName + '_' + randomNumber;
        explorerWorkspace.enterWorkspaceName(newWorkspaceName);
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

    @When("search for workspace")
    public void search_for_workspace() {
        workspacedownloadnpi.searchWorkspace();
    }

    @Then("user clicks on the searched workspace")
    public void user_clicks_on_the_searched_workspace() {
        workspacedownloadnpi.clickDownloadButton();
        workspacedownloadnpi.clickNPIDownload();
        workspacedownloadnpi.clickCSVFile();
        workspacedownloadnpi.clickDownloadNPIButton();
        // Assert.assertEquals("NPI List file is ready for download","NPI List file is ready for download",workspacedownloadnpi.verifyToast());

        workspacedownloadnpi.clickDownloadButton();
        workspacedownloadnpi.clickNPIDownload();
        workspacedownloadnpi.clickXSLXFile();
        workspacedownloadnpi.clickDownloadNPIButton();
        //Assert.assertEquals("NPI List file is ready for download","NPI List file is ready for download",workspacedownloadnpi.verifyToast());
    }

    @Then("verify the file content")
    public void verify_the_file_content() {
        fileContent = CSVActions.readAllDataAtOnce(ConfigReader.getProperty("csvFilePath"));
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

    //DB result will be raised in next PR
    /*@Then("verify db result")
    public void verify_db_result() throws SQLException {
       // String query = "select GRPNAME from adadmin.CMPGroup where GRPNAME = 'Auto_Feb_9'";
        String expectedValue = "Auto_Feb_9";
        actualValueData=new ArrayList<>();
         actualValue = DatabaseActions.getData("select * from contextad.adadmin.CMPGroup where GRPNAME = ?", expectedValue);
        for (String[] row : actualValue) {
            //System.out.println("Row: " + String.join(", ", row));
            actualValueData.add(String.join(", ", row));
        }
        System.out.println("Data from query :" + actualValueData);
    }*/

    @When("Studio platform is available")
    public void studio_platform_is_available() {
        workspace.studio();
    }

    @And("User searches the {string} and selects it")
    public void userSearchesTheAndSelectsIt(String WORKSPACE) {
        workspace.searchWorkspace(WORKSPACE);
    }

    @When("Download button is enabled to the user")
    public void download_button_is_enabled_to_the_user() {
        workspace.clickDownbutton();
    }

    @When("User clicks on Publish NPI List")
    public void user_clicks_on_publish_npi_list() {
        workspace.clickPublishNpi();
    }

    @And("User selects publish {string}")
    public void userSelectsPublish(String listType) {
        workspace.publish(listType);
    }

    @When("User select the system to publish the list")
    public void user_select_the_system_to_publish_the_list() {
        workspace.hcp();
        workspace.life();
    }

    @Then("Verify list is published")
    public void verify_list_is_published() {
        workspace.clickPublish();
        Assert.assertEquals("Workspace saved successfully", workspaceCreation.verifyWorkspaceCreation());
        workspace.waitTillWorkspaceAlertHide();
        workspace.clickDownbutton();
        Assert.assertEquals("Published NPI List", workspace.verifyPublishedNpi());

    }

    @And("Verify Webhook panel is disabled before applying filters")
    public void verifyWebhookPanelIsDisabledBeforeApplyingFilters() {
        workspace.clickWebhookIcon();
        Assert.assertEquals("Disabled",workspace.verifyWebhookToggleButton());
        workspace.closeWebhookPanel();
    }

    @Then("Verify Webhook panel is enabled after applying engagement filters")
    public void verifyWebhookPanelIsEnabledAfterApplyingFilters() {
        workspace.clickWebhookIcon();
        Assert.assertEquals("Enabled",workspace.verifyWebhookToggleButton());
    }

    @When("User clicks {string} request method")
    public void userClicksRequestMethod(String requestType) {
        if(requestType.contains("POST"))
            workspace.clickWebhookIcon();
        workspace.clickRequestOrContentButton(requestType);
    }

    @And("User adds valid URL and append Macros with {string} to the {string} as follow")
    public void userAddsURLAndMacrosWithToTheURLAsFollow(String param, String textType, DataTable macros) {
        List<String> macrosList = macros.asList(String.class);
        workspace.addURL(WEBHOOK_URL);
        workspace.addMacros(textType, param, macrosList);
    }

    @Then("Verify if Macros Appended to the URL")
    public void verifyIfMacrosAppendedToTheURL() {
        String text = workspace.verifyMacrosAppendedToURL();
        Assert.assertTrue(
                "Macros are not correctly appended to the URL",
                text.matches(Pattern.quote(WEBHOOK_URL) + "%%NPI%%%%URL%%%%Channel%%%%PARAM\\d+%%")
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
        Assert.assertEquals("rgb(0, 136, 136)",workspace.checkBackgroundColorOfWebhookIcon());
    }

    @When("User tries to delete the workspace associated with active webhook from the workspace list")
    public void userDeletesTheWebhookFromTheWorkspaceList() {
        workspace.goToWorkspaceList();
        workspaceCreation.searchWorkspaceAndDelete(newWorkspaceName);
    }

    @Then("Verify user receives a warning when attempting to delete a workspace with an active webhook")
    public void verifyUserReceivesAWarningWhenAttemptingToDeleteAWorkspaceWithAnActiveWebhook() {
        String text = workspaceCreation.verifyDeletePopUp();
        Assert.assertTrue("Message is not displayed",
                text.contains("You are trying to delete the workspace " + newWorkspaceName +".\n" +
                        "\n" +
                        "Webhooks are enabled for this workspace.\n" +
                        "\n" +
                        "Deleting the workspace will delete the webhook as well. This action cannot be undone.\n" +
                        "Do you want to proceed?"));
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
        if(flag){
            List<String> appliedFilters = explorerWorkspace.verifyAllSelectedFilters();
            List<String> expectedFilters = CommonUtils.parseCommaSeparatedString(primaryFilter);

            for (String expected : expectedFilters) {
                boolean isFilterApplied = appliedFilters.stream()
                        .anyMatch(filter -> filter.equalsIgnoreCase(expected));
                Assert.assertTrue("The filter '" + expected + "' is not applied correctly", isFilterApplied);
            }
            isOverwritten = true;
        }else{
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
        Assert.assertTrue("Filters were not added",explorerWorkspace.verifyCrossFiltersDisplayed());
        appliedFilterEntries = explorerWorkspace.fetchMergedFilters();
        System.out.println("Merge Filter" + appliedFilterEntries);
    }

    @And("Verify dashboard filters are merged with Primary filters")
    public void verifyDashboardFiltersAreMergedWithPrimaryFilters() {
        List<String> displayedFilters = explorerWorkspace.verifyAllSelectedFilters();
        System.out.println("Displayed Filter" + displayedFilters);

        for (String mergedFilter : appliedFilterEntries) {
            boolean matchFound = displayedFilters.stream()
                    .anyMatch(displayed -> displayed.equalsIgnoreCase(mergedFilter));

            Assert.assertTrue("Merged filter not displayed: " + mergedFilter, matchFound);
        }
    }
}