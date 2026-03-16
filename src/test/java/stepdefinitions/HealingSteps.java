package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.Navigation;
import pages.healPageObjects.HealingPageObject;
import utils.CommonUtils;
import utils.ConfigReader;

public class HealingSteps {
    static String userType;
    static String url;
    static String username;
    static String password;
    static String campaignNameRandom;
    static String lineItemNameRandom;
    static String tacticNameRandom;
    Navigation navigation = new Navigation(DriverFactory.getPage());
    HealingPageObject healingPageObject = new HealingPageObject(DriverFactory.getPage());
    private static final Logger logger = LoggerFactory.getLogger(HealingSteps.class);

    @Given("This scenario is executing in the {string} environment as aa {string}")
    public void thisScenarioIsExecutingInTheEnvironmentAsAa(String environment, String user) throws Exception {
        logger.info("Setting up environment: {} for user type: {}", environment, user);
        userType = user;

        if (environment.equals("Demo")) {
            url = ConfigReader.getProperty("demoURL");
            if (user != null && user.toLowerCase().contains("external") && ConfigReader.getProperty("demoExternalUser") != null) {
                logger.info("Selecting Demo External User credentials");
                username = ConfigReader.getProperty("demoExternalUser");
                password = ConfigReader.getProperty("demoExternalPassword");
            } else {
                logger.info("Selecting Demo Internal User credentials");
                username = ConfigReader.getInternalDemoUsername();
                password = ConfigReader.getInternalDemoPassword();
            }
        } else if (environment.equals("Pre-release")) {
            logger.info("Configuring for Pre-release environment");
            url = ConfigReader.getProperty("preReleaseURL");
            if (user != null && user.toLowerCase().contains("external")) {
                logger.info("Selecting Pre-release External User credentials");
                username = ConfigReader.getProperty("preReleaseExternalUser");
                password = ConfigReader.getProperty("preReleaseExternalPassword");
            } else {
                logger.info("Selecting Pre-release Internal User credentials");
                username = ConfigReader.getInternalPreReleaseUsername();
                password = ConfigReader.getInternalPreReleasePassword();
            }
        }
    }

    @And("{string} application is logged in with Account as {string}")
    public void applicationIsLoggedInWithAccount(String application, String account) {
        logger.info("Logging into application: {} with account: {}", application, account);
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

    @And("Verify Campaign Dashboard is displayed with a title as {string}")
    public void verifyCampaignDashboardIsDisplayedWithTitleAs(String title) {
        logger.info("Verifying Campaign Dashboard is visible with title: {}", title);
        Assert.assertEquals(title, healingPageObject.isCampaignDashboardVisibleWithTitle(title));
    }

    @Given("User clicks on Create Campaign button")
    public void user_clicks_on_create_campaign() {
        logger.info("Clicking on Create Campaign button");
        healingPageObject.createCampaign();
        String campaignText = healingPageObject.verifyCampaignText();
        logger.info("Verifying Campaign page text: {}", campaignText);
        Assert.assertEquals("Create New Campaign", campaignText);
        logger.info("Create Campaign page verified successfully");
    }
    @When("User enters the campaign details such as {string} {string} {string} {string} and saves the campaign")
    public void user_enters_the_campaign_details_and_saves_the_campaign(String advertiser, String campaign_name, String campaign_type, String budget) {
        campaignNameRandom = campaign_name + '_' + CommonUtils.timeStampCalculation();
        logger.info("Entering campaign details - Advertiser: {}, Name: {}, Type: {}, Budget: {}", advertiser, campaignNameRandom, campaign_type, budget);
        healingPageObject.selectAdvertiser(advertiser);
        healingPageObject.enterCampaignName(campaignNameRandom);
        healingPageObject.setCampaignType(campaign_type);
        healingPageObject.enterBudget(budget);
        logger.info("Saving campaign details");
        healingPageObject.saveCampaign();
    }

    @Then("Verify campaign details are saved and user is navigated to the Line Item page")
    public void verify_campaign_details_are_saved_and_user_is_navigated_to_line_item_page() {
        logger.info("Verifying Campaign creation success message");
        Assert.assertEquals("Campaign " + campaignNameRandom + " created.", healingPageObject.campaignSuccess());
        String lineItemText = healingPageObject.verifyLineItemText();
        logger.info("Verifying Line Item page text: {}", lineItemText);
        Assert.assertEquals("New Line Item", lineItemText);
        logger.info("Campaign verified and navigated to Line Item page successfully");
    }

    @When("User enters the line item details such as {string} {string}, enables the line item and saves the changes")
    public void user_enters_the_line_item_details_enables_the_line_item_and_saves_the_changes(String lineItemName, String lineBudget) {
        lineItemNameRandom = lineItemName + '_' + CommonUtils.timeStampCalculation();
        logger.info("Entering Line Item details - Name: {}, Budget: {}", lineItemNameRandom, lineBudget);
        healingPageObject.enterLineItemName(lineItemNameRandom);
        logger.info("Clicking Add Flight icon");
        navigation.clickOnIcon("Add Flight");
        healingPageObject.enterLineItemBudget(lineBudget);
        healingPageObject.isPlacementIdAvailable(lineItemNameRandom);
        logger.info("Enabling and saving Line Item");
        healingPageObject.enableLineItem();
        healingPageObject.saveLineItem();
    }

    @Then("Verify line item details are saved and user is navigated to the Tactic page")
    public void verify_line_item_details_are_saved_and_user_is_navigated_to_tactic_page() {
        logger.info("Verifying Line Item creation success message");
        Assert.assertEquals("Lineitem " + lineItemNameRandom + " created.", healingPageObject.lineItemSuccess());
        String tacticText = healingPageObject.verifyTacticDetailsText();
        logger.info("Verifying Tactic page text: {}", tacticText);
        Assert.assertEquals("New Tactic", tacticText);
        logger.info("Line Item verified and navigated to Tactic page successfully");
    }

    @When("User enters the tactic details such as {string} and saves the tactic")
    public void user_enters_the_tactic_details_and_saves_the_tactic(String tacticName) {
        tacticNameRandom = tacticName + '_' + CommonUtils.timeStampCalculation();
        logger.info("Entering tactic details for name: {}", tacticNameRandom);
        healingPageObject.enterTacticName(tacticNameRandom);
        healingPageObject.saveTacticDetails();
        logger.info("Tactic details saved");
    }

    @Then("Verify tactic details are saved and user is navigated to the Settings tab")
    public void verify_tactic_details_are_saved_and_user_is_navigated_to_settings_tab() {
        logger.info("Verifying tactic save success message");
        Assert.assertEquals("Tactic " + tacticNameRandom + " updated.", healingPageObject.tacticDetailsSuccess());
        String settingsText = healingPageObject.verifyTacticSettingsText();
        logger.info("Settings tab text: {}", settingsText);
        Assert.assertEquals("Bid Strategy", healingPageObject.verifyTacticSettingsText());
        logger.info("Tactic saved and navigated to Settings tab successfully");
    }

    @When("User selects the {string} as channel type")
    public void user_selects_the_channel(String channel) {
        logger.info("Selecting Channel: {}", channel);
        healingPageObject.selectChannel(channel);
        logger.info("Adding Targeting Rule");
        healingPageObject.clickOnIcon("Add Targeting Rule");
    }

    @Then("User selects {string} as rule type and configures the Targeting rules, and saves the settings")
    public void user_configures_the_targeting_rules_and_saves_the_settings(String ruleType) {
        logger.info("Selecting rule type: {}", ruleType);
        healingPageObject.selectRuleType(ruleType);
        logger.info("Saving Tactic settings");
        healingPageObject.saveTacticSettings();
    }

    @Then("Verify settings details are saved and user is navigated to the Creatives tab")
    public void verify_settings_details_are_saved_and_user_is_navigated_to_creatives_tab() {
        logger.info("Verifying settings save success and navigation to Creatives");
        String successMessage = healingPageObject.tacticSettingsSuccess();
        logger.info("Save Message: {}", successMessage);
        assert successMessage.contains("Success!");
        String creativesText = healingPageObject.verifyTacticCreativesText();
        logger.info("Creatives tab text: {}", creativesText);
        Assert.assertEquals("Creative(s)", creativesText);
        logger.info("Settings saved and navigated to Creatives successfully");
    }

    @Then("User assigns the Existing creative named {string}, enables the tactic and saves the changes")
    public void user_assigns_the_existing_creative_enables_the_tactic_and_saves_the_changes(String creative) {
        logger.info("Assigning existing creative: {}", creative);
        healingPageObject.clickOnIcon("Assign Existing Creatives");
        healingPageObject.assignCreatives(creative);
        logger.info("Enabling Tactic and saving Creatives");
        healingPageObject.enableCreative();
        healingPageObject.saveTacticCreatives();
    }

    @Then("Verify creative details are saved and the Campaign is in running state")
    public void verify_creative_details_are_saved_and_the_campaign_is_in_running_state() {
        logger.info("Verifying creative save success");
        assert healingPageObject.tacticCreativesSuccess().contains("Success!");
        logger.info("Navigating to Campaign Dashboard to verify 'Running' status");
        healingPageObject.navigateBackToCampaignDashboard();
        healingPageObject.resetFiltersIfApplied();
        String status = healingPageObject.verifyCampaignRunning();
        logger.info("Campaign status: {}", status);
        Assert.assertEquals("Running", status);
        logger.info("Campaign is in Running state");
    }

    @Then("Verify the newly created campaign details in the Campaign list: Campaign name, Line item name and Tactic name")
    public void verify_the_newly_created_campaign_details_in_the_campaign_list() {
        logger.info("Verifying newly created campaign details in the campaign list");
        healingPageObject.navigateToCampaignDashboard();
        logger.info("Searching for Campaign: {}", campaignNameRandom);
        healingPageObject.searchCreatedCampaign(campaignNameRandom);
        Assert.assertEquals(campaignNameRandom, healingPageObject.verifyCreatedCampaign(campaignNameRandom));
        logger.info("Expanding Line Item to verify: {}", lineItemNameRandom);
        healingPageObject.expandCreatedLineItem();
        Assert.assertEquals(lineItemNameRandom, healingPageObject.verifyCreatedLineItem(lineItemNameRandom));
        logger.info("Expanding Tactic to verify: {}", tacticNameRandom);
        healingPageObject.expandCreatedLineItem();
        Assert.assertEquals(tacticNameRandom, healingPageObject.verifyCreatedTactic());
        logger.info("Campaign, Line Item and Tactic hierarchy verified successfully in campaign list");
    }
}
