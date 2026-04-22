package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.Navigation;
import pages.hcp.SmartActions;
import utils.CommonUtils;

public class HcpSteps {
    private static final Logger logger = LoggerFactory.getLogger(HcpSteps.class);
    String timestamp = CommonUtils.timeStampCalculation();
    SmartActions smartActions = new SmartActions(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());

    @Given("User navigates to Smart actions from the main menu")
    public void user_navigates_to_smart_actions_from_the_main_menu() {
        logger.info("Navigating to Smart actions from the main menu");
        navigation.clickSubMenu();
        smartActions.clickSmartActions();
    }

    @When("User clicks on Add Smart Action")
    public void user_clicks_on_add_smart_action() {
        logger.info("Clicking on Add Smart Action");
        smartActions.clickAddSmartAction();
    }

    @Then("Verify smart action creation page is displayed")
    public void verify_smart_action_creation_page_is_displayed() {
        String pageHeader = smartActions.getSmartAction();
        logger.info("Smart action page header: {}", pageHeader);
        Assert.assertEquals("New Smart Action", pageHeader);
    }

    @Then("User enters smart action details as {string} {string}")
    public void user_enters_smart_action_details_as(String smartActionName, String advertiser) {
        smartActionName = smartActionName + '_' + timestamp;
        logger.info("Entering smart action details - Name: {}, Advertiser: {}", smartActionName, advertiser);
        smartActions.enterSmartActionName(smartActionName);
        smartActions.enterAdvertiser(advertiser);
    }

    @When("User saves the smart action")
    public void user_saves_the_smart_action() {
        logger.info("Saving the smart action");
        smartActions.saveSmartAction();
    }

    @Then("Verify smart action is saved successfully and navigates to Audience tab")
    public void verify_smart_action_is_saved_successfully_and_navigates_to_audience_tab() {
        String successMessage = smartActions.getSmartActionSuccessMessage().trim();
        logger.info("Save Message: {}", successMessage);
        Assert.assertEquals("New Smart Action created successfully.", successMessage);
        String audienceTab = smartActions.getAudienceTab();
        logger.info("Navigated tab: {}", audienceTab);
        Assert.assertEquals("Audience", audienceTab);
    }

    @When("User clicks on NPI Lists")
    public void user_clicks_on_npi_lists() {
        logger.info("Clicking on NPI Lists option");
        smartActions.clickNPIListsOption();
    }

    @Then("Verify NPI list created is present")
    public void verify_npi_list_created_in_life_is_present() {
        String actualNpiList = smartActions.searchNPIList(LifeSteps.npiName);
        logger.info("Found NPI list: {}", actualNpiList);
        Assert.assertEquals(LifeSteps.npiName, actualNpiList);
    }

    @Then("User adds NPI list to the smart action")
    public void user_adds_npi_list_to_the_smart_action() {
        logger.info("Adding NPI list to the smart action");
        smartActions.targetNPIList();
    }

    @When("User clicks on Ok and Save")
    public void user_clicks_on_ok_and_save() {
        logger.info("Saving NPI Lists targeting configuration");
        smartActions.saveNPILists();
    }

    @Then("Verify data is saved successfully")
    public void verify_data_is_saved_successfully() {
        String savedMessage = smartActions.getSavedMessage().trim();
        logger.info("Save Message: {}", savedMessage);
        Assert.assertEquals("Data saved successfully", savedMessage);
    }

    @When("User clicks on Action and enters the details and saves")
    public void user_clicks_on_action_and_enters_the_details_and_saves() {
        logger.info("Entering details in Action tab and saving");
        smartActions.actionTabDataEntryAndSave();
    }

    @Then("Verify Action data is saved successfully")
    public void verify_action_data_is_saved_successfully() {
        String savedMessage = smartActions.getSavedMessage().trim();
        logger.info("Action Save Message: {}", savedMessage);
        Assert.assertEquals("Data saved successfully", savedMessage);
    }

    @When("User clicks on Response and enter the details and creates smart list {string} {string} and saves")
    public void user_clicks_on_response_and_enter_the_details_and_creates_smart_list_and_saves(String smartListName, String days) {
        smartListName = smartListName + '_' + timestamp;
        logger.info("Entering details in Response tab, creating smart list: {} for {} days, and saving", smartListName, days);
        smartActions.responseTabDataEntryAndSave(smartListName, days);
    }

    @Then("Verify Response data is saved successfully")
    public void verify_response_data_is_saved_successfully() {
        String savedMessage = smartActions.getSavedMessage().trim();
        logger.info("Response Save Message: {}", savedMessage);
        Assert.assertEquals("Data saved successfully", savedMessage);
    }
}