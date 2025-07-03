package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.Navigation;
import pages.hcp365.SmartActions;
import utils.CommonUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HcpSteps {

    String timestamp = CommonUtils.timeStampCalculation();

    SmartActions smartActions = new SmartActions(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());


    @Given("User navigates to Smart actions from the main menu")
    public void user_navigates_to_smart_actions_from_the_main_menu() {
        navigation.clickSubMenu();
        smartActions.clickSmartActions();

    }

    @When("User clicks on Add Smart Action")
    public void user_clicks_on_add_smart_action() {
        smartActions.clickAddSmartAction();
    }

    @Then("Verify smart action creation page is displayed")
    public void verify_smart_action_creation_page_is_displayed() {
        Assert.assertEquals("Smart Action Properties", smartActions.getSmartAction());
    }

    @Then("User enters smart action details as {string} {string}")
    public void user_enters_smart_action_details_as(String smartActionName, String advertiser) {
        smartActionName = smartActionName + '_' + timestamp;
        smartActions.enterSmartActionName(smartActionName);
        smartActions.enterAdvertiser(advertiser);

    }

    @When("User saves the smart action")
    public void user_saves_the_smart_action() {
        smartActions.saveSmartAction();
    }

    @Then("Verify smart action is saved successfully and navigates to Audience tab")
    public void verify_smart_action_is_saved_successfully_and_navigates_to_audience_tab() {
        Assert.assertEquals("New Smart Action created successfully.", smartActions.getSmartActionSuccessMessage().trim());
        Assert.assertEquals("Audience", smartActions.getAudienceTab());
    }

    @When("User clicks on NPI Lists")
    public void user_clicks_on_npi_lists() {
        smartActions.clickNPIListsOption();
    }

    @Then("Verify NPI list created in LIFE is present")
    public void verify_npi_list_created_in_life_is_present() {
        Assert.assertEquals(LifeSteps.npiName, smartActions.searchNPIList(LifeSteps.npiName));
    }

    @Then("User adds NPI list to the smart action")
    public void user_adds_npi_list_to_the_smart_action() {
        smartActions.targetNPIList();
    }

    @When("User clicks on Ok and Save")
    public void user_clicks_on_ok_and_save() {
        smartActions.saveNPILists();
    }

    @Then("Verify data is saved successfully")
    public void verify_data_is_saved_successfully() {
        Assert.assertEquals("Data saved successfully", smartActions.getSavedMessage().trim());

    }

    @When("User clicks on Action and enters the details and saves")
    public void user_clicks_on_action_and_enters_the_details_and_saves() {
        smartActions.actionTabDataEntryAndSave();
    }

    @Then("Verify Action data is saved successfully")
    public void verify_action_data_is_saved_successfully() {
        Assert.assertEquals("Data saved successfully", smartActions.getSavedMessage().trim());
    }

    @When("User clicks on Response and enter the details and creates smart list {string} {string} and saves")
    public void user_clicks_on_response_and_enter_the_details_and_creates_smart_list_and_saves(String smartListName, String days) {
        smartListName = smartListName + '_' + timestamp;
        smartActions.responseTabDataEntryAndSave(smartListName, days);
    }

    @Then("Verify Response data is saved successfully")
    public void verify_response_data_is_saved_successfully() {
        Assert.assertEquals("Data saved successfully", smartActions.getSavedMessage().trim());
    }


}