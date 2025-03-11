package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.Navigation;
import pages.life.*;
import utils.WebActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LifeSteps {

    static String campaignNameRandom;
    static String lineItemNameRandom;
    static String tacticNameRandom;
    static String url;
    static String username;
    static String password;
    static String timestamp;
    static String npiStaticName;
    static String templateNameRandom;
    static String dimensionName;
    static String metricName;
    List<String> rules;
    List<String> rulesCompare = new ArrayList<>();
    Navigation navigation = new Navigation(DriverFactory.getPage());
    Campaigns campaigns = new Campaigns(DriverFactory.getPage());
    LineItemDetails lineItemDetails = new LineItemDetails(DriverFactory.getPage());
    TacticDetails tacticDetails = new TacticDetails(DriverFactory.getPage());
    TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());
    TacticCreatives tacticCreatives = new TacticCreatives(DriverFactory.getPage());
    CampaignListing campaignListing = new CampaignListing(DriverFactory.getPage());
    NPILists npiLists = new NPILists(DriverFactory.getPage());
    NPIStaticList npiStaticList = new NPIStaticList(DriverFactory.getPage());
    ReportTemplates reportTemplates = new ReportTemplates(DriverFactory.getPage());

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
        Assert.assertEquals("", "Admin Dashboard", navigation.verifyProfilePage());

        switch (application) {
            case "Life":
                navigation.navigateToLife();
                break;
            case "HCP":
                navigation.navigateToHCP();
                break;
            case "Studio":
                navigation.navigateToLife();
                navigation.navigateToStudio();
                break;
        }
    }

    @Given("User clicks on Create Campaign")
    public void user_clicks_on_create_campaign() {
        Assert.assertEquals("", "Life", campaigns.campaignDashboard());
        campaignListing.setGroupByFilter();
        navigation.clickOnIcon(" Group By Campaign ");
        campaigns.createCampaign();
        Assert.assertEquals("Create New Campaign", campaigns.verifyCampaignText());
    }

    @When("User enters the campaign details as {string} {string} {string} {string} and saves the campaign")
    public void user_enters_the_campaign_details_and_saves_the_campaign(String advertiser, String campaign_name, String campaign_type, String budget) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        campaignNameRandom = campaign_name + '_' + timestamp;
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

    @Given("User navigates to NPI Lists page")
    public void user_navigates_to_npi_lists_page() {
        navigation.clickSubMenu();
        npiLists.clickNPILists();
    }

    @When("User clicks on Add List")
    public void user_clicks_on_add_list() {
        npiLists.clickAddList();
    }

    @Then("Verify creation of NPI List screen is displayed")
    public void verify_creation_of_npi_list_screen_is_displayed() {
        Assert.assertEquals("Create New NPI List", npiLists.verifyNPIListText());
    }

    @Then("User selects Static List")
    public void user_selects_static_list() {
        npiLists.clickStaticList();
    }

    @Then("User enters the NPI list details as {string} {string} {string}")
    public void user_enters_the_npi_list_details_as(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiStaticName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiStaticName);
        npiStaticList.selectAdvertiser(advertiser);
        npiStaticList.enterNPINumber(npiNumber);
    }

    @When("User makes list available in LIFE and saves the list")
    public void user_makes_list_available_in_life_and_saves_the_list() {
        npiStaticList.selectProduct();
    }

    @Then("Verify list gets saved successfully.")
    public void verify_list_gets_saved_successfully() {
        npiStaticList.saveList();
        assert npiStaticList.saveListSuccess().contains("NPI list created");
    }

    @Given("User navigates to Report Templates page")
    public void user_navigates_to_report_templates_page() {
        navigation.clickSubMenu();
        reportTemplates.clickReportTemplatesLink();
    }

    @Then("Verify the tabs displayed on the Report Templates page")
    public void verify_the_tabs_displayed_on_the_report_templates_page() {
        Assert.assertEquals("TEMPLATES", reportTemplates.verifyTemplatesTab());
        Assert.assertEquals("GENERATED REPORTS", reportTemplates.verifyGeneratedReportsTab());
        Assert.assertEquals("SCHEDULING", reportTemplates.verifySchedulingTab());
    }

    @When("User clicks on New Template")
    public void user_clicks_on_new_template() {
        reportTemplates.createNewTemplate();
    }

    @Then("Verify the tabs displayed on the Create New Template panel")
    public void verify_the_tabs_displayed_on_the_create_new_template_panel() {
        Assert.assertEquals("DIMENSIONS", reportTemplates.verifyDimensionsTab());
        Assert.assertEquals("METRICS", reportTemplates.verifyMetricsTab());
    }

    @When("User enters the template details as {string} {string} {string}")
    public void user_enters_the_template_details_as(String templateName, String dimension, String metric) {
        dimensionName = dimension;
        metricName = metric;
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        templateNameRandom = templateName + '_' + timestamp;
        reportTemplates.enterTemplateName(templateNameRandom);
        reportTemplates.selectDimension(dimension);
        reportTemplates.clickMetricsTab();
        reportTemplates.selectMetric(metric);
    }

    @Then("Verify the selected dimensions and metrics under the Template Structure section")
    public void verify_the_selected_dimensions_and_metrics_under_the_template_structure_section() {
        Assert.assertEquals(dimensionName, reportTemplates.verifySelectedDimensions());
        Assert.assertEquals(metricName, reportTemplates.verifySelectedMetrics());
    }

    @When("User saves the new template")
    public void user_saves_the_new_template() {
        reportTemplates.saveReportTemplate();
    }

    @Then("Verify new template is saved and displayed in the template list")
    public void verify_new_template_is_saved_and_displayed_in_the_template_list() {
        assert reportTemplates.reportTemplateSuccess().contains("Template created successfully");
        reportTemplates.searchCreatedReportTemplate(templateNameRandom);
        Assert.assertEquals(templateNameRandom, reportTemplates.verifyCreatedReportTemplate(templateNameRandom));
        Assert.assertEquals(1, reportTemplates.searchResultRowCount());
    }

    @Given("User selects the {string} channel, configures targeting rules:")
    public void user_selects_the_channel_configures_targeting_rules(String channel, io.cucumber.datatable.DataTable ruleTypes) {
        rules = ruleTypes.asList(String.class);

        tacticSettings.selectChannel(channel);
        navigation.clickOnIcon("Add Targeting Rule");

        for (String rule : rules) {
            tacticSettings.selectMultipleRuleTypes(rule);
        }

        tacticSettings.closeRuleTypePanel();
    }

    @Then("Verify the configured targeting rules")
    public void verify_the_configured_targeting_rules() {
        rulesCompare.add(tacticSettings.verifyAudienceAttributeRule().replaceAll("\\(\\d+\\)", "").trim());
        rulesCompare.add(tacticSettings.verifyHealthJourneyRule().replaceAll("\\(\\d+\\)", "").trim());
        rulesCompare.add(tacticSettings.verifyDemographicsRule().replaceAll("\\(\\d+\\)", "").trim());
        rulesCompare.add(tacticSettings.verifyContextualRule().replaceAll("\\(\\d+\\)", "").trim());
        rulesCompare.add(tacticSettings.verifyGeographyRule().replaceAll("\\(\\d+\\)", "").trim());
        rulesCompare.add(tacticSettings.verifyMediaSupplyRule().replaceAll("\\(\\d+\\)", "").trim());
        rulesCompare.add(tacticSettings.verifyLegalTargetingsRule().replaceAll("\\(\\d+\\)", "").trim());
        assert rules.equals(rulesCompare);

        tacticSettings.verifyAudienceAttributeOption();
        tacticSettings.verifyHealthJourneyOption();
        tacticSettings.verifyDemographicsOption();
        tacticSettings.verifyContextualOption();
        tacticSettings.verifyGeographyOption();
        tacticSettings.verifyMediaSupplyOption();
        tacticSettings.verifyLegalTargetingsOption();
    }

    @When("User saves the settings")
    public void user_saves_the_settings() {
        tacticSettings.saveTacticSettings();
    }
}