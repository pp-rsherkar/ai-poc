package stepdefinitions;

import com.microsoft.playwright.options.LoadState;
import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.Navigation;
import pages.life.*;
import utils.ConfigLoader;
import utils.Constants;
import utils.DatabaseActions;
import utils.WebActions;

import java.sql.SQLException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static factory.DriverFactory.page;

public class LifeSteps {

    static String campaignNameRandom;
    static String lineItemNameRandom;
    static String tacticNameRandom;
    static String tacticPMP;
    static String url;
    static String username;
    static String password;
    static String timestamp;
    static String npiName;
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
    Constants constants = new Constants();
    PMP pmp = new PMP(DriverFactory.getPage());
    NPISmartList npiSmartList = new NPISmartList(DriverFactory.getPage());

    @Given("This scenario will be executed in the {string} environment as a {string}")
    public void set_environment(String environment, String user) throws Exception {
        if (environment.equals("Demo")) {
            url = WebActions.getProperty("demoURL");
            username = WebActions.getProperty("demoUser");
            password = WebActions.getProperty("demoPassword");
        } else if (environment.equals("Pre-release")) {
            url = WebActions.getProperty("preReleaseURL");
            username = WebActions.getProperty("preReleaseUser");
//            password = WebActions.getProperty(ConfigLoader.getPassword());
            password = ConfigLoader.getPassword();
            System.out.println("Encrypted Password: " + ConfigLoader.getPassword());

        }
    }

    @And("{string} application is logged in successfully")
    public void life_application_is_loged_in_as(String application) {
        navigation.navigateToUrl(url);
        navigation.enterUsername(username);
        navigation.enterPassword(password);
        navigation.clickLogin();
        page.waitForLoadState(LoadState.NETWORKIDLE);
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

    @Then("User selects the {string} as channel, selects {string} as rule type and configures the targeting rules, and saves the settings")
    public void user_selects_the_channel_configures_the_targeting_rules_and_saves_the_settings(String channel, String ruleType) {
        tacticSettings.selectChannel(channel);
        navigation.clickOnIcon("Add Targeting Rule");
        tacticSettings.selectRuleType(ruleType);
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
    @And("User navigates to NPI Lists page in LIFE")
    public void userNavigatesToNPIListsPageInLIFE()
    {
        navigation.clickSubMenu();
        npiLists.clickNPIListsStg();
    }
    @And("User searches the {string} in LIFE and selects it")
    public void userSearchesTheInLIFEAndSelectsIt(String Studio_list)
    {
        npiLists.searchNPILists(StudioSteps.workspaceNameRandom);

    }

    @And("User clicks on the published {string}")
    public void userClicksOnThePublished(String Studio_list)
    {
        npiLists.selectPublishedList(Studio_list);
    }

    @Then("User Verify the list is displayed in the Life")
    public void userVerifyTheListIsDisplayedInTheLife()
    {
        //Assert.assertTrue(npiLists.availablePlatforms());


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

    @Then("User selects Smart List to create NPI list")
    public void user_selects_smart_list_to_create_npi_list() {

        npiLists.clickSmartList();
    }


    @Then("User enters the NPI list details as {string} {string} {string}")
    public void user_enters_the_npi_list_details_as(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiStaticList.enterNPINumber(npiNumber);
    }
    @Then("User enters the Smart NPI list details as {string} {string} {string} for Smart Pixel")
    public void user_enters_the_smart_npi_list_details_as_for_Smart_Pixel(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickSmartPixel();
        npiSmartList.clickSmartPixelDropDown();
        npiSmartList.clickSmartPixelDropDownValue();
        npiSmartList.clickLifeCheckbox();

    }
    @Then("User enters the Smart NPI list details as {string} {string} {string} for NPI List")
    public void user_enters_the_smart_npi_list_details_as_for_NPI_List(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickNPIList();
        npiSmartList.clickNPIGroup();
        npiSmartList.clickNPIGroupValue();
        npiSmartList.clickLifeCheckbox();

    }
    @Then("User enters the Smart NPI list with Expand based on practice and hospital affiliation details as {string} {string} {string} for NPI List")
    public void user_enters_the_smart_npi_list_with_expand_based_details_as_for_NPI_List(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickNPIList();
        npiSmartList.clickNPIGroup();
        npiSmartList.clickNPIGroupValue();
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickExpandPractice();

    }
    @Then("User enters the details as {string} {string} {string} for Specialty")
    public void user_enters_the_smart_npi_list_details_as_for_Specialty(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickSpecialty();
        npiSmartList.clickSpecialtyDropdown();
        npiSmartList.selectSpecialtyValue();



    }
    @Then("User enters the details as {string} {string} {string} for Profession")
    public void user_enters_the_smart_npi_list_details_as_for_Profession(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickProfession();
        npiSmartList.clickProfessionDropdown();
        npiSmartList.selectProfessionValue();



    }
    @Then("User enters the details as {string} {string} {string} for Prescribed Drug")
    public void user_enters_the_smart_npi_list_details_as_for_PrescribedDrug(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickPrescribedDrug();

    }
    @Then("User enters the details as {string} {string} {string} for Diagnosis")
    public void user_enters_the_smart_npi_list_details_as_for_Diagnosis(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickDiagnosis();

    }
    @Then("User enters the details as {string} {string} {string} for Medical Procedure")
    public void user_enters_the_smart_npi_list_details_as_for_Medical_Procedure(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickMedicalProcedure();

    }
    @Then("User enters the details as {string} {string} {string} for Endemic Research")
    public void user_enters_the_smart_npi_list_details_as_for_Endemic_Research(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickEndemicResearch();
        npiSmartList.SelectEndemicDetails();


    }
    @Then("User enters the details as {string} {string} {string} for Prescription Behavior Change")
    public void user_enters_the_smart_npi_list_details_as_for_Prescription_Behavior_Change(String npiListName, String advertiser, String npiNumber) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();

        npiSmartList.clickPrescriptionBehaviorChange();
        npiSmartList.SelectPrescriptionBehaviorDetails();

    }
    @Then("User enters the Smart NPI list details as {string} {string} for {string}")
    public void user_enters_the_smart_npi_list_details_as_for_type(String npiListName, String advertiser, String type) {
        switch (type) {
            case "Smart Pixel":
                {
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickSmartPixel();
                npiSmartList.clickSmartPixelDropDown();
                npiSmartList.clickSmartPixelDropDownValue();
                npiSmartList.clickLifeCheckbox();
                    break;
            }
                case "NPI List":
                    timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                    npiName = npiListName + '_' + timestamp;
                    npiStaticList.enterListName(npiName);
                    npiStaticList.selectAdvertiser(advertiser);
                    npiSmartList.clickNPIList();
                    npiSmartList.clickNPIGroup();
                    npiSmartList.clickNPIGroupValue();
                    npiSmartList.clickLifeCheckbox();

                    break;
            case "Specialty":
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickLifeCheckbox();
                npiSmartList.clickSpecialty();
                npiSmartList.clickSpecialtyDropdown();
                npiSmartList.selectSpecialtyValue();

                break;
            case "Profession":
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickLifeCheckbox();
                npiSmartList.clickProfession();
                npiSmartList.clickProfessionDropdown();
                npiSmartList.selectProfessionValue();

                break;
            case "Prescribed Drug":
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickLifeCheckbox();
                npiSmartList.clickPrescribedDrug();

                break;
            case "Prescription Behaviour Change":
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickLifeCheckbox();

                npiSmartList.clickPrescriptionBehaviorChange();
                npiSmartList.SelectPrescriptionBehaviorDetails();
                break;
            case "Diagnosis":
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickLifeCheckbox();
                npiSmartList.clickDiagnosis();
                break;
            case "Medical Procedure":
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickLifeCheckbox();
                npiSmartList.clickMedicalProcedure();
                break;
            case "Endemic Research":
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickLifeCheckbox();
                npiSmartList.clickEndemicResearch();
                npiSmartList.SelectEndemicDetails();

                break;
            case "Expand":

                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                npiName = npiListName + '_' + timestamp;
                npiStaticList.enterListName(npiName);
                npiStaticList.selectAdvertiser(advertiser);
                npiSmartList.clickNPIList();
                npiSmartList.clickNPIGroup();
                npiSmartList.clickNPIGroupValue();
                npiSmartList.clickLifeCheckbox();
                npiSmartList.clickExpandPractice();

                break;

            default : System.out.println("Invalid Type");

        }
    }

    @When("User makes list available in LIFE and saves the list")
    public void user_makes_list_available_in_life_and_saves_the_list() {
        npiStaticList.selectProduct();
    }

    @Then("Verify list gets saved successfully")
    public void verify_list_gets_saved_successfully() {
        npiStaticList.saveList();
        assert npiStaticList.saveListSuccess().contains("NPI list created");
    }
    @Then("Save and Verify the list gets saved successfully")
    public void verify_smart_list_gets_saved_successfully() {
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
    @When("User enters the template details for end to end as {string} {string} {string}")
    public void user_enters_the_template_for_end_to_end_details_as(String templateName, String dimension, String metric) {


        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        templateNameRandom = templateName + '_' + timestamp;
        reportTemplates.enterTemplateName(templateNameRandom);
        List<String> dimensionList = Arrays.asList(dimension.split(","));

        for (String dimensionValue : dimensionList) {
            dimensionValue = dimensionValue.trim();
            reportTemplates.selectDimensione2e(dimensionValue);
        }

        reportTemplates.clickMetricsTab();
        List<String> metricsList = Arrays.asList(metric.split(","));

        for (String metricValue : metricsList) {
            metricValue = metricValue.trim();
            reportTemplates.selectDimensione2e(metricValue);
        }


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

    @Then("Verify the newly created campaign in the database")
    public void verify_campaign_in_database() throws SQLException {
        String actualValue = DatabaseActions.getData(constants.CAMPAIGN_NAME, campaignNameRandom);
        if (actualValue != null) {
            Assert.assertEquals(campaignNameRandom, actualValue);
        } else {
            throw new SQLException("Campaign not found in the database with the expected name: " + campaignNameRandom);
        }
    }

    @And("User has navigated to mentioned tactic {string}")
    public void user_navigates_to_the_tactic(String tacticPMP) {
        pmp.navigateToTactic(tacticPMP);
    }

    @When("Targeting panel is opened on Tactic Settings tab")
    public void user_navigates_to_targeting_panel() {
        pmp.verifyTacticSettingsText();
        pmp.setADD_TARGETING_RULE();
    }

    @And("User clicks on Deals Targeting")
    public void deals_targeting_navigation() {
        pmp.SEARCH_TARGETING_RULE();
        pmp.SET_DEALS_TARGETING();
    }

    @And("User assigns premium deals")
    public void user_assigns_premium_deals() {
        pmp.PREMIUM_DEALS_ASSIGNMENT();
    }

    @And("User clicks on OK button of PMP Modal")
    public void user_clicks_on_OK_PMP_Modal() {
        pmp.EXIT_PMP_MODAL_OK_BUTTON_PRESS();
    }

    @And("User assigns private deals")
    public void user_assigns_private_deals() {
        pmp.PRIVATE_DEALS_ASSIGNMENT();
    }

    @And("User saves the changes")
    public void users_saves_deal_changes() {
        pmp.saveTacticSettings();
    }

    @Then("Deals should be assigned")
    public void deals_are_assigned() {
        pmp.tacticSettingsSuccess();
    }

    @Then("User selects Smart List")
    public void user_selects_smart_list() {
        npiSmartList.clickSmartList();

    }

    @Then("User enters the NPI list details as {string} {string}")
    public void user_enters_the_npi_list_details_as(String listName, String advertiser) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        npiName = listName + '_' + timestamp;
        npiSmartList.enterListName(npiName);
        npiSmartList.selectAdvertiser(advertiser);
    }

    @When("User clicks on Prescribed Drug and enters the drug details {string}")
    public void user_clicks_on_prescribed_drug_and_enters_the_drug_details(String drugName) {
        npiSmartList.selectPrescribedDrug();
        npiSmartList.selectDrug(drugName);

    }

    @Then("Verify drug details are added")
    public void verify_drug_details_are_added() {
        Assert.assertEquals("Glynase", npiSmartList.verifyDrug());

    }

    @When("User makes list available in LIFE, HCP365 and saves the list")
    public void user_makes_list_available_in_life_hcp365_and_saves_the_list() {
        npiSmartList.selectProduct();
    }

    @Then("User navigates to Campaign Dashboard")
    public void user_navigates_to_campaign_dashboard() {
        npiSmartList.clickPulsepointICon();
    }

    @Then("Verify smart list is targeted in the tactic successfully")
    public void verify_smart_list_is_targeted_in_the_tactic_successfully() {
        tacticSettings.verifyNPIRule();
        Assert.assertTrue(tacticSettings.verifyNPIRule().contains("NPI"));

    }

    @Then("User saves the targeting")
    public void u_ser_saves_the_targeting() {
        tacticSettings.saveTacticSettings();
    }

    @When("User selects the {string} channel, configure NPI targeting rule")
    public void user_selects_the_channel_configure_npi_targeting_rule(String channel) {
        tacticSettings.selectChannel(channel);
        navigation.clickOnIcon("Add Targeting Rule");
        tacticSettings.selectNPIRule(npiName);
        tacticSettings.clickTarget();
        tacticSettings.clickOk();
        tacticSettings.clickClose();
    }


    /// ///////////////////////
    @And("User navigates to run report from mega menu of the life application")
    public void user_navigate_to_run_report() {
        navigation.clickSubMenu();
        navigation.clickRunReport();


    }

    /// /////////////
    @Then("User selects the report template created tactic and other fields for running the report")
    public void user_enter_input_for_running_report() {
        reportTemplates.enterDetailsToRunReport(templateNameRandom,tacticNameRandom);

    }
    /// ///////////////
    @Then("User verifies the selected campaign,lineitem, tactic and runs report by clicking on Run button")
    public void user_verifies_the_selected_details() {
       Assert.assertEquals(campaignNameRandom, reportTemplates.verifyAutopopulatedCampaign(campaignNameRandom));
        Assert.assertEquals(lineItemNameRandom, reportTemplates.verifyAutopopulatedLineitem(lineItemNameRandom));

        reportTemplates.runReport();

   }

    /// ///////////////

    @Then("User navigates to generate report field and verifies the report name by campaign name")
    public void user_navigate_to_generate_report_page() {

        navigation.clickSubMenu();
        navigation.clickScheduledReport();
        navigation.clickSubMenu();
        navigation.clickGeneratedReport();



    }

    /// ///////////////
    @Then("User downloads the report and verify the data in downloaded report")
    public void user_download_the_report_from_generated_report_page_and_verify_the_data() throws IOException {
        reportTemplates.downloadGeneratedReport();
        navigation.clickSubMenu();
        navigation.clickReportTemplate();
        reportTemplates.verifyColumnsOfReport(templateNameRandom);



    }



}