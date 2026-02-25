package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import pages.Navigation;
import pages.healPageObjects.HealingPageObject;
import pages.life.CampaignDashboard;
import utils.ConfigReader;

public class HealingSteps {
    static String userType;
    static String url;
    static String username;
    static String password;
    HealingPageObject healingPageObject = new HealingPageObject(DriverFactory.getPage());

    @Given("This scenario is executing in the {string} environment as aa {string}")
    public void thisScenarioIsExecutingInTheEnvironmentAsAa(String environment, String user) throws Exception {
        userType = user;
        if (environment.equals("Demo")) {
            url = ConfigReader.getProperty("demoURL");
            if (user != null && user.toLowerCase().contains("external") && ConfigReader.getProperty("demoExternalUser") != null) {
                username = ConfigReader.getProperty("demoExternalUser");
                password = ConfigReader.getProperty("demoExternalPassword");
            } else {
                username = ConfigReader.getInternalDemoUsername();
                password = ConfigReader.getInternalDemoPassword();
            }
        } else if (environment.equals("Pre-release")) {
            url = ConfigReader.getProperty("preReleaseURL");
            if (user != null && user.toLowerCase().contains("external")) {
                username = ConfigReader.getProperty("preReleaseExternalUser");
                password = ConfigReader.getProperty("preReleaseExternalPassword");
            } else {
                username = ConfigReader.getInternalPreReleaseUsername();
                password = ConfigReader.getInternalPreReleasePassword();
            }
        }
    }

    @And("{string} application is logged in withh Account {string}")
    public void applicationIsLoggedInWithhAccount(String application, String account) {
        healingPageObject.navigateToUrl(url);
        healingPageObject.enterUsername(username);
        healingPageObject.enterPassword(password);
        healingPageObject.clickLogin();
        switch (application) {
            case "Life":
                healingPageObject.navigateToLife();
                break;
            case "HCP":
                healingPageObject.navigateToHCP();
                break;
            case "Studio":
                if (userType.equals("User")) {
                    healingPageObject.navigateToLife();
                }
                healingPageObject.navigateToStudio();
                break;
        }
        healingPageObject.selectAccount(account);
    }

    @And("Verify Campaign Dashboard is displayedd with title as {string}")
    public void verifyCampaignDashboardIsDisplayeddWithTitleAs(String title) {
        Assert.assertEquals(title, healingPageObject.isCampaignDashboardVisibleWithTitle(title));
    }
}
