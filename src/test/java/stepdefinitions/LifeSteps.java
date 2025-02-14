package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.*;
import pages.life.*;
import utils.WebActions;

import java.util.UUID;

public class LifeSteps {

    static String campaignNameRandom;
    static String lineItemNameRandom;
    static String tacticNameRandom;
    static String url;
    static String username;
    static String password;
    Navigation navigation = new Navigation(DriverFactory.getPage());
    Campaigns campaigns = new Campaigns(DriverFactory.getPage());
    LineItemDetails lineItemDetails = new LineItemDetails(DriverFactory.getPage());
    TacticDetails tacticDetails = new TacticDetails(DriverFactory.getPage());
    TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());
    TacticCreatives tacticCreatives = new TacticCreatives(DriverFactory.getPage());
    CampaignListing campaignListing = new CampaignListing(DriverFactory.getPage());

    @Given("This scenario will be executed in the {string} environment as a {string}")
    public void set_environment(String environment, String user) {
        if (environment.equals("Demo")) {
            url = WebActions.getProperty("demoURL");
            username = WebActions.getProperty("demoUser");
            password = WebActions.getProperty("demoPassword");
        } else if (environment.equals("Pre-release")) {
            url = WebActions.getProperty("preReleaseURL");
            username = WebActions.getProperty("preReleaseUser");
            password = WebActions.getProperty("preReleasePassword");
        }
    }

    @And("{string} application is logged in successfully")
    public void life_application_is_loged_in_as(String application) {
        navigation.navigateToUrl(url);
        navigation.enterUsername(username);
        navigation.enterPassword(password);
        navigation.clickLogin();
//        Add logic to conditional redirection to respective applications
        Assert.assertEquals("", "Admin Dashboard", navigation.verifyProfilePage());
        System.out.println(navigation.verifyProfilePage());
    }

    @Given("User navigates to the Campaign Dashboard")
    public void user_navigates_to_the_campaign_dashboard() {
        campaigns.navigateToLife();
        Assert.assertEquals("", "Life", campaigns.campaignDashboard());
    }

    @Given("User clicks on Create Campaign")
    public void user_clicks_on_create_campaign() {
        campaignListing.setGroupByFilter();
        navigation.clickOnIcon(" Group By Campaign ");
        campaigns.createCampaign();
        Assert.assertEquals("Create New Campaign", campaigns.verifyCampaignText());
    }

    @When("User enters the campaign details as {string} {string} {string} {string} and saves the campaign")
    public void user_enters_the_campaign_details_and_saves_the_campaign(String advertiser, String campaign_name, String campaign_type, String budget) {
        campaignNameRandom = campaign_name + '_' + UUID.randomUUID().toString().substring(0, 10);
        campaigns.selectAdvertiser(advertiser);
        campaigns.enterCampaignName(campaignNameRandom);
        campaigns.setCampaignType(campaign_type);
        campaigns.enterBudget(budget);
        campaigns.saveCampaign();
    }

    @Then("Verify campaign details are saved and user is navigated to the line item page")
    public void verify_campaign_details_are_saved_and_user_is_navigated_to_line_item_page() {
        assert campaigns.campaignSuccess().contains("Success");
        Assert.assertEquals("New Line Item", lineItemDetails.verifyLineItemText());
    }

    @When("User enters the line item details as {string} {string}, enables the line item and saves the changes")
    public void user_enters_the_line_item_details_enables_the_line_item_and_saves_the_changes(String lineItemName, String lineBudget) {
        lineItemNameRandom = lineItemName + '_' + UUID.randomUUID().toString().substring(0, 10);
        lineItemDetails.enterLineItemName(lineItemNameRandom);
        navigation.clickOnIcon("Add Flight");
        lineItemDetails.enterLineItemBudget(lineBudget);
        lineItemDetails.enableLineItem();
        lineItemDetails.saveLineItem();
    }

    @Then("Verify line item details are saved and user is navigated to the tactic page")
    public void verify_line_item_details_are_saved_and_user_is_navigated_to_tactic_page() {
        assert lineItemDetails.lineItemSuccess().contains("Success");
        Assert.assertEquals("New Tactic", tacticDetails.verifyTacticDetailsText());
    }

    @When("User enters the tactic details as {string} and saves the tactic")
    public void user_enters_the_tactic_details_and_saves_the_tactic(String tacticName) {
        tacticNameRandom = tacticName + '_' + UUID.randomUUID().toString().substring(0, 10);
        tacticDetails.enterTacticName(tacticNameRandom);
        tacticDetails.saveTacticDetails();
    }

    @Then("Verify tactic details are saved and user is navigated to the settings tab")
    public void verify_tactic_details_are_saved_and_user_is_navigated_to_settings_tab() {
        assert tacticDetails.tacticDetailsSuccess().contains("Success");
        Assert.assertEquals("Bid Strategy", tacticSettings.verifyTacticSettingsText());
    }

    @Then("User selects the {string} channel, configures the targeting rules, and saves the settings")
    public void user_selects_the_channel_configures_the_targeting_rules_and_saves_the_settings(String channel) {
        tacticSettings.selectChannel(channel);
        navigation.clickOnIcon("Add Targeting Rule");
        tacticSettings.selectRuleType();
        tacticSettings.saveTacticSettings();
    }

    @Then("Verify settings details are saved and user is navigated to the creatives tab")
    public void verify_settings_details_are_saved_and_user_is_navigated_to_creatives_tab() {
        assert tacticSettings.tacticSettingsSuccess().contains("Success");
        Assert.assertEquals("Creative(s)", tacticCreatives.verifyTacticCreativesText());
    }

    @Then("User assigns the existing creative named {string}, enables the tactic and saves the changes")
    public void user_assigns_the_existing_creative_enables_the_tactic_and_saves_the_changes(String creative) {
        navigation.clickOnIcon("Assign Existing Creatives");
        tacticCreatives.assignCreatives(creative);
        tacticCreatives.enableCreative();
        tacticCreatives.saveTacticCreatives();
    }

    @Then("Verify creative details are saved and the campaign is in running state")
    public void verify_creative_details_are_saved_and_the_campaign_is_in_running_state() {
        assert tacticCreatives.tacticCreativesSuccess().contains("Success");
        tacticCreatives.navigateToCampaignDashboard();
        Assert.assertEquals("Running", tacticCreatives.verifyCampaignRunning());
    }

    @Then("Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name")
    public void verify_the_newly_created_campaign_details_in_the_campaign_list() {
        campaigns.navigateToCampaignListing();
        campaignListing.searchCreatedCampaign(campaignNameRandom);
        Assert.assertEquals(campaignNameRandom, campaignListing.verifyCreatedCampaign(campaignNameRandom));
        Assert.assertEquals(lineItemNameRandom, campaignListing.verifyCreatedLineItem(lineItemNameRandom));
        campaignListing.expandCreatedLineItem();
        Assert.assertEquals(tacticNameRandom, campaignListing.verifyCreatedTactic());
    }
}