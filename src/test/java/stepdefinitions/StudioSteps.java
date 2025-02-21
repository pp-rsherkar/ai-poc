package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.*;
import pages.admin.Accounts;

public class StudioSteps {
    Accounts accounts = new Accounts(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());

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
        accounts.createWorkspace();
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
}