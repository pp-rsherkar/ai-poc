package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.*;
import pages.admin.Accounts;
import pages.studio.ExplorerWorkspace;
import pages.studio.Workspaces;

import java.util.UUID;

public class StudioSteps {
    static String workspaceNameRandom;
    static String filterName;
    static String filterOption;
    static Boolean clickFlag = true;
    Accounts accounts = new Accounts(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());
    Workspaces workspaces = new Workspaces(DriverFactory.getPage());
    ExplorerWorkspace explorerWorkspace = new ExplorerWorkspace(DriverFactory.getPage());

    @And("User enables the studio for an account")
    public void user_enables_the_studio_for_an_account()
    {
        navigation.clickSubMenu();
        accounts.clickAdministration();
        accounts.selectAccountsTab();
        accounts.searchAccount();
    }

    @And("User navigates to workspace permissions")
    public void User_navigates_to_workspace_permissions()
    {
        accounts.enableStudio();
    }

    @When("User selects the workspace types and saves the settings")
    public void user_selects_the_workspace_types_and_saves_the_settings()
    {
        accounts.workSpaceSettings();
    }

    @Then("Studio should be enabled for that account")
    public void Studio_should_be_enabled_for_that_account()
    {
        accounts.saveStudioSettings();
    }

    @And("User should be able to see the enabled workspaces for that account under Studio")
    public void userShouldBeAbleToSeeTheEnabledWorkspacesForThatAccountUnderStudio()
    {
        accounts.switchAccount();
        navigation.navigateToStudio();
        workspaces.createWorkspace();
        Assert.assertEquals("HCP Audience Expansion",accounts.verifyWorkspacePermission());

    }
    @And("User disables the studio permission for an account")
    public void userDisablesTheStudioPermissionForAnAccount()
    {
        navigation.clickSubMenu();
        accounts.disableStudioForAccount();
    }

    @Then("User should not be able to see the studio permission for that account")
    public void userShouldNotBeAbleToSeeTheStudioPermissionForThatAccount()
    {
        accounts.verifyStudioMenu();
    }

    @When("User clicks on Create New Workspace")
    public void user_clicks_on_create_new_workspace() {
        Assert.assertEquals("", "Genome Studio", workspaces.studioDashboard());
        //workspaces.createWorkspace();
        workspaces.createWorkspace(clickFlag);
    }

    @Then("User sees the types of workspaces they have permissions for")
    public void user_sees_the_types_of_workspaces_they_have_permissions_for() {
        Assert.assertEquals("HCP Explorer", workspaces.verifyHCPExplorer());
        System.out.println(workspaces.verifyHCPExplorer());
        Assert.assertEquals("HCP Audience Expansion", workspaces.verifyHCPAudienceExpansion());
        System.out.println(workspaces.verifyHCPAudienceExpansion());
    }

    @And("User clicks on HCP Explorer workspace")
    public void user_clicks_on_hcp_explorer_workspace() {
        workspaces.clickHCPExplorerWorkspace();
    }

    @Then("User adds the workspace name as {string} and selects the advertiser {string}")
    public void user_adds_the_workspace_name_and_selects_the_advertiser(String workspaceName, String advertiser) {
        workspaceNameRandom = workspaceName + '_' + UUID.randomUUID().toString().substring(0, 10);
        explorerWorkspace.enterWorkspaceName(workspaceNameRandom);
        explorerWorkspace.selectAdvertiser(advertiser);
    }

    @Then("User applies the {string} filter and selects {string} option")
    public void user_applies_the_filters_as_gender_and_age(String filter, String option) {
        filterName = filter;
        filterOption = option;
        explorerWorkspace.selectFilter(filterName, filterOption);
    }

    @Then("User clicks on Ok and closes the filter popup")
    public void user_clicks_on_ok_and_closes_the_filter_popup() {
        explorerWorkspace.applyFilter();
    }

    @Then("Verify that the applied filters are displayed correctly")
    public void verify_that_the_applied_filters_are_displayed_correctly() {
        Assert.assertEquals(filterName, explorerWorkspace.verifySelectedFilter());
        System.out.println(explorerWorkspace.verifySelectedFilter());
        Assert.assertEquals(filterOption, explorerWorkspace.verifySelectedOption());
        System.out.println(explorerWorkspace.verifySelectedOption());
    }

    @Then("User saves the workspace")
    public void user_saves_the_workspace() {
        explorerWorkspace.saveExplorerWorkspace();
    }

    @Then("Verify the HCP Explorer Workspace is saved")
    public void verify_the_hcp_explorer_workspace_is_saved() {
        assert explorerWorkspace.workspaceSuccess().contains("Workspace saved");
        System.out.println(explorerWorkspace.workspaceSuccess());
    }
}