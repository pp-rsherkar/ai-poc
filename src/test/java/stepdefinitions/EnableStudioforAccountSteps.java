package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.*;
import org.junit.Assert;
import pages.DisableStudioPermissions;
import pages.EnableStudioforAccount;
import pages.NavigateStudio;
import pages.Navigation;

public class EnableStudioforAccountSteps
{
    Navigation navigation = new Navigation(DriverFactory.getPage());
    EnableStudioforAccount esa= new EnableStudioforAccount(DriverFactory.getPage());
    NavigateStudio ns= new NavigateStudio(DriverFactory.getPage());
    DisableStudioPermissions ds= new DisableStudioPermissions(DriverFactory.getPage());

    @Given("user logged in as {string}")
    public void userLoggedInAsAn(String string) {

        navigation.navigateToUrl();
        navigation.enterUsername(string);
        navigation.enterPassword();
        navigation.clickLogin();
        Assert.assertEquals("", "Admin Dashboard", navigation.verifyProfilePage());
        System.out.println(navigation.verifyProfilePage());
        navigation.clickLifeMenu();

    }
    @When("user enable the studio for an account")
    public void user_enable_the_studio_for_an_account()
    {
        esa.clickMenu();
        esa.clickAdministration();
        esa.accountMenuClick();
        esa.searchAccount();

    }
    @Then("user should navigate to workspace permission")
    public void user_should_navigate_to_workspace_permission()
    {
        esa.enableStudio();

    }
    @Then("user selects the workspace types and saves the setting")
    public void user_selects_the_workspace_types_and_saves_the_setting()
    {
        esa.workSpaceSettings();

    }
    @Then("the studio should be enabled for that account")
    public void the_studio_should_be_enabled_for_that_account()
    {
        esa.Savestudiosettings();
    }


    @And("user should be able to see the enabled workspaces for that account under Studio")
    public void userShouldBeAbleToSeeTheEnabledWorkspacesForThatAccountUnderStudio()
    {
        ns.switchAccount();
        ns.navigateStudio();
        Assert.assertEquals("HCP Audience Expansion",ns.verifyWorkspacePermission());

    }
    @And("user disables the studio permission for an account")
    public void userDisablesTheStudioPermissionForAnAccount()
    {
       ds.disableStudioForAccount();
    }
    @Then("user should not be able to see the studio permission for that account")
    public void userShouldNotBeAbleToSeeTheStudioPermissionForThatAccount()
    {
        ds.verifyStudioMenu();
    }


}
