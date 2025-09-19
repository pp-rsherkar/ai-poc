package stepdefinitions;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.opencsv.exceptions.CsvValidationException;
import factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.Navigation;
import pages.life.*;
import utils.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static factory.DriverFactory.page;
import static utils.CommonUtils.normalize;
import static utils.CommonUtils.normalizeObjectList;

public class LifeSteps {

    static String campaignNameRandom;
    static String lineItemNameRandom;
    static String tacticNameRandom;
    static String dealNameRandom;
    static String dealIDRandom;
    static String url;
    static String username;
    static String password;
    static String npiName;
    static String npiNameEdited;
    static String templateNameRandom;
    static String dimensionName;
    static String metricName;
    static String newPixelName;
    List<Object> keyType = new ArrayList<>();
    List<Object> keyValues = new ArrayList<>();
    Map<String, Map<String, String>> keyValueMap = new LinkedHashMap<>();
    List<String> nameList = new ArrayList<>();
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
    PMP pmp = new PMP(DriverFactory.getPage());
    NPISmartList npiSmartList = new NPISmartList(DriverFactory.getPage());
    CampaignDashboard campaignDashboard = new CampaignDashboard(DriverFactory.getPage());
    TargetingTemplate targetingTemplate = new TargetingTemplate(DriverFactory.getPage());
    CreateCreatives createCreatives = new CreateCreatives(DriverFactory.getPage());
    NPIAttributesList npiAttributesList = new NPIAttributesList(DriverFactory.getPage());
    NPIAutoImportedList npiAutoImportedList = new NPIAutoImportedList(DriverFactory.getPage());
    SharedList sharedList = new SharedList(DriverFactory.getPage());
    Pixels pixels = new Pixels(DriverFactory.getPage());
    RetargetingPixel retargetingPixel = new RetargetingPixel(DriverFactory.getPage());
    ConversionPixel conversionPixel = new ConversionPixel(DriverFactory.getPage());
    SmartPixel smartPixel = new SmartPixel(DriverFactory.getPage());
    BulkCreativeUpload bulkCreativeUpload = new BulkCreativeUpload(DriverFactory.getPage());
    Constants constants = new Constants();
    String timestamp = CommonUtils.timeStampCalculation();
    int itemCount = 0;
    int totalListCount = 0;
    APIResponse response;
    boolean flag = false;

    @Given("This scenario will be executed in the {string} environment as a {string}")
    public void set_environment(String environment, String user) {
        if (environment.equals("Demo")) {
            url = ConfigReader.getProperty("demoURL");
            username = ConfigReader.getProperty("demoUser");
            password = ConfigReader.getProperty("demoPassword");
        } else if (environment.equals("Pre-release")) {
            url = ConfigReader.getProperty("preReleaseURL");
            username = ConfigReader.getProperty("preReleaseUser");
            password = ConfigReader.getProperty("preReleasePassword");
        }
    }

    @And("{string} application is logged in successfully with Account {string}")
    public void life_application_is_loged_in_as(String application, String account) {
        navigation.navigateToUrl(url);
        navigation.enterUsername(username);
        navigation.enterPassword(password);
        navigation.clickLogin();
        Assert.assertEquals("Admin Dashboard", navigation.verifyProfilePage());

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
        navigation.selectAccount(account);
    }

    @Given("User clicks on Create Campaign")
    public void user_clicks_on_create_campaign() {
        Assert.assertEquals("Life", campaigns.campaignDashboard());
        /*campaignListing.setGroupByFilter();
        navigation.clickOnIcon(" Group By Campaign ");*/
        campaigns.createCampaign();
        Assert.assertEquals("Create New Campaign", campaigns.verifyCampaignText());
    }

    @When("User enters the campaign details as {string} {string} {string} {string} and saves the campaign")
    public void user_enters_the_campaign_details_and_saves_the_campaign(String advertiser, String campaign_name, String campaign_type, String budget) {
        campaignNameRandom = campaign_name + '_' + timestamp;
        campaigns.selectAdvertiser(advertiser);
        campaigns.enterCampaignName(campaignNameRandom);
        campaigns.setCampaignType(campaign_type);
        campaigns.enterBudget(budget);
        campaigns.saveCampaign();
    }

    @Then("Verify campaign details are saved and user is navigated to the line item page")
    public void verify_campaign_details_are_saved_and_user_is_navigated_to_line_item_page() {
        assert campaigns.campaignSuccess().contains("Success!");
        Assert.assertEquals("New Line Item", lineItemDetails.verifyLineItemText());
    }

    @When("User enters the line item details as {string} {string}, enables the line item and saves the changes")
    public void user_enters_the_line_item_details_enables_the_line_item_and_saves_the_changes(String lineItemName, String lineBudget) {
        lineItemNameRandom = lineItemName + '_' + CommonUtils.randomNumberGeneration();
        lineItemDetails.enterLineItemName(lineItemNameRandom);
        navigation.clickOnIcon("Add Flight");
        lineItemDetails.enterLineItemBudget(lineBudget);
        lineItemDetails.enableLineItem();
        lineItemDetails.saveLineItem();
    }

    @Then("Verify line item details are saved and user is navigated to the tactic page")
    public void verify_line_item_details_are_saved_and_user_is_navigated_to_tactic_page() {
        assert lineItemDetails.lineItemSuccess().contains("Success!");
        Assert.assertEquals("New Tactic", tacticDetails.verifyTacticDetailsText());
    }

    @When("User enters the tactic details as {string} and saves the tactic")
    public void user_enters_the_tactic_details_and_saves_the_tactic(String tacticName) {
        tacticNameRandom = tacticName + '_' + CommonUtils.randomNumberGeneration();
        tacticDetails.enterTacticName(tacticNameRandom);
        tacticDetails.saveTacticDetails();
    }

    @Then("Verify tactic details are saved and user is navigated to the settings tab")
    public void verify_tactic_details_are_saved_and_user_is_navigated_to_settings_tab() {
        assert tacticDetails.tacticDetailsSuccess().contains("Success!");
        Assert.assertEquals("Bid Strategy", tacticSettings.verifyTacticSettingsText());
    }

    @When("User selects the {string} as channel")
    public void user_selects_the_channel(String channel) {
        tacticSettings.selectChannel(channel);
        navigation.clickOnIcon("Add Targeting Rule");
    }

    @Then("User selects {string} as rule type and configures the targeting rules, and saves the settings")
    public void user_configures_the_targeting_rules_and_saves_the_settings(String ruleType) {
        tacticSettings.selectRuleType(ruleType);
        tacticSettings.saveTacticSettings();
    }

    @Then("Verify settings details are saved and user is navigated to the creatives tab")
    public void verify_settings_details_are_saved_and_user_is_navigated_to_creatives_tab() {
        assert tacticSettings.tacticSettingsSuccess().contains("Success!");
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
        assert tacticCreatives.tacticCreativesSuccess().contains("Success!");
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
    public void userNavigatesToNPIListsPageInLIFE() {
        navigation.clickSubMenu();
        npiLists.clickNPIListsStg();
    }

    @And("User searches the workspace in LIFE and selects it")
    public void userSearchesTheInLIFEAndSelectsIt() {
        npiLists.searchNPILists(StudioSteps.newWorkspaceName);
    }

    @And("User clicks on the published workspace")
    public void userClicksOnThePublished() {
        npiLists.selectPublishedList(StudioSteps.newWorkspaceName);
    }

    @Then("User Verify the list is displayed in the Life")
    public void userVerifyTheListIsDisplayedInTheLife() {
        Assert.assertTrue("NPI list is not available in LIFE", npiLists.availablePlatforms());
    }

    @When("User clicks on Create New List")
    public void user_clicks_on_create_new_list() {
        npiLists.clickCreateNewList();
    }

    @Then("User selects Smart List to create NPI list")
    public void user_selects_smart_list_to_create_npi_list() {
        npiLists.clickSmartList();
    }

    @Then("Save and Verify the list gets saved successfully")
    public void verify_smart_list_gets_saved_successfully() {
        npiStaticList.saveList();
        assert npiStaticList.saveListSuccess().contains("NPI list created");
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
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiStaticList.enterNPINumber(npiNumber);
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

    @Given("User navigates to Report Templates page")
    public void user_navigates_to_report_templates_page() {
        navigation.clickSubMenu();
        reportTemplates.clickReportTemplatesLink();
    }

    @Then("Verify the tabs displayed on the Report Templates page")
    public void verify_the_tabs_displayed_on_the_report_templates_page() {
        Assert.assertEquals("TEMPLATES", reportTemplates.verifyTemplatesTab().toUpperCase());
        Assert.assertEquals("GENERATED REPORTS", reportTemplates.verifyGeneratedReportsTab().toUpperCase());
        Assert.assertEquals("SCHEDULING", reportTemplates.verifySchedulingTab().toUpperCase());
    }

    @Then("User enters the Smart NPI list details as {string} {string} for {string} with {string} {string} {string}")
    public void user_enters_the_smart_npi_list_details_as_for_type(String npiListName, String advertiser, String type, String professionValue, String smartPixelDropdownValue, String npiGroupValue) {
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        switch (type.trim()) {
            case "Smart Pixel":
                npiSmartList.clickSmartPixel();
                npiSmartList.clickSmartPixelDropDown();
                npiSmartList.clickSmartPixelDropDownValue(smartPixelDropdownValue);
                break;
            case "NPI List":
                npiSmartList.clickNPIList();
                npiSmartList.clickNPIGroup();
                npiSmartList.clickNPIGroupValue(npiGroupValue);
                break;
            case "Specialty":
                npiSmartList.clickSpecialty();
                npiSmartList.clickSpecialtyDropdown();
                npiSmartList.selectSpecialtyValue();
                break;
            case "Profession":
                npiSmartList.clickProfession();
                npiSmartList.clickProfessionDropdown();
                npiSmartList.selectProfessionValue(professionValue);
                break;
            case "Prescribed Drug":
                npiSmartList.clickPrescribedDrug();
                break;
            case "Prescription Behaviour Change":
                npiSmartList.clickPrescriptionBehaviorChange();
                npiSmartList.SelectPrescriptionBehaviorDetails();
                break;
            case "Diagnosis":
                npiSmartList.clickDiagnosis();
                break;
            case "Medical Procedure":
                npiSmartList.clickMedicalProcedure();
                break;
            case "Endemic Research":
                npiSmartList.clickEndemicResearch();
                npiSmartList.SelectEndemicDetails();
                break;
            case "Expand":
                npiSmartList.clickNPIList();
                npiSmartList.clickNPIGroup();
                npiSmartList.clickNPIGroupValue(npiGroupValue);
                npiSmartList.clickExpandPractice();
                break;
        }
    }

    @When("User clicks on New Template")
    public void user_clicks_on_new_template() {
        reportTemplates.createNewTemplate();
    }

    @Then("Verify the tabs displayed on the Create New Template panel")
    public void verify_the_tabs_displayed_on_the_create_new_template_panel() {
        Assert.assertEquals("DIMENSIONS", reportTemplates.verifyDimensionsTab().toUpperCase());
        Assert.assertEquals("METRICS", reportTemplates.verifyMetricsTab().toUpperCase());
    }

    @When("User enters the template details as {string} {string} {string}")
    public void user_enters_the_template_details_as(String templateName, String dimension, String metric) {
        dimensionName = dimension;
        metricName = metric;
        templateNameRandom = templateName + '_' + timestamp;
        reportTemplates.enterTemplateName(templateNameRandom);
        reportTemplates.selectDimension(dimension);
        reportTemplates.clickMetricsTab();
        reportTemplates.selectMetric(metric);
    }

    @When("User enters the template details for end to end as {string} {string} {string}")
    public void user_enters_the_template_for_end_to_end_details_as(String templateName, String dimension, String metric) {
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

    @Given("User configures targeting rules as below")
    public void user_selects_the_channel_configures_targeting_rules(DataTable ruleTypeAndOptions) {
        Map<String, String> rawMap = ruleTypeAndOptions.asMap(String.class, String.class);
        Map<String, List<String>> rulesMap = CommonUtils.processDataTable(rawMap);
        for (Map.Entry<String, List<String>> entry : rulesMap.entrySet()) {
            keyType.add(entry.getKey());
            keyValues.addAll(entry.getValue());
            tacticSettings.selectMultipleRuleTypes(entry.getKey(), entry.getValue());
        }
        tacticSettings.closeRuleTypePanel();
    }

    @Then("Verify the configured targeting rules")
    public void verify_the_configured_targeting_rules() {
        List<String> expectedNormalizedRuleTypes = normalizeObjectList(keyType);
        int expectedCount = expectedNormalizedRuleTypes.size();
        tacticSettings.fetchRulesTypesCount(expectedCount);
        List<String> actualNormalizedRuleTypes = normalizeObjectList(tacticSettings.fetchRulesTypes());

        Set<String> expectedSet = new LinkedHashSet<>(expectedNormalizedRuleTypes);
        Set<String> actualSet = new LinkedHashSet<>(actualNormalizedRuleTypes);

        List<String> expectedUniqueAndSorted = new ArrayList<>(expectedSet);
        List<String> actualUniqueAndSorted = new ArrayList<>(actualSet);

        Collections.sort(expectedUniqueAndSorted);
        Collections.sort(actualUniqueAndSorted);

        List<String> expectedNormalizedRuleOptions = normalizeObjectList(keyValues);
        List<String> actualNormalizedRuleOptions = normalizeObjectList(tacticSettings.fetchRuleOptions());

        Assert.assertEquals("Rule types mismatch", expectedUniqueAndSorted, actualUniqueAndSorted);
        for (String expectedOption : expectedNormalizedRuleOptions) {
            boolean matchFound = actualNormalizedRuleOptions.stream()
                    .anyMatch(actual -> actual.equalsIgnoreCase(expectedOption));
            Assert.assertTrue("Expected rule option not found: " + expectedOption, matchFound);
        }
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

    @When("Targeting panel is opened on Tactic Settings tab")
    public void user_navigates_to_targeting_panel() {
        pmp.verifyTacticSettingsText();
        pmp.addNewTargetingRule();
    }

    @And("User clicks on {string} Targeting")
    public void deals_targeting_navigation(String Deals) {
        pmp.searchTargetingRuleAndSelect(Deals);
    }

    @And("User assigns premium deals")
    public void user_assigns_premium_deals() {
    }

    @And("User clicks on OK button of PMP Modal")
    public void user_clicks_on_OK_PMP_Modal() {
    }

    @And("User assigns private deals")
    public void user_assigns_private_deals() {
    }

    @And("User saves the changes")
    public void users_saves_deal_changes() {
        pmp.saveTacticSettings();
    }

    @Then("Deals should be assigned")
    public void deals_are_assigned() {
        pmp.verifyTacticIsSaved();
    }

    @Then("User selects Smart List")
    public void user_selects_smart_list() {
        npiSmartList.clickSmartList();
    }

    @Then("User enters the NPI list details as {string} {string}")
    public void user_enters_the_npi_list_details_as(String listName, String advertiser) {
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
        navigation.clickSubMenu();
        navigation.clickCampaigns();
    }

    @Then("Verify list is targeted in the tactic successfully")
    public void verify_list_is_targeted_in_the_tactic_successfully() {
        tacticSettings.verifyNPIRule();
        Assert.assertTrue(tacticSettings.verifyNPIRule().contains("NPI"));
    }

    @Then("User saves the targeting")
    public void u_ser_saves_the_targeting() {
        tacticSettings.saveTacticSettings();
    }

    @When("User selects the {string} channel, configure {string} targeting rule")
    public void user_selects_the_channel_configure_npi_targeting_rule(String channel, String ruleType) {
        tacticSettings.selectChannel(channel);
        navigation.clickOnIcon("Add Targeting Rule");
        tacticSettings.selectTargetingRule(ruleType, npiName);
        tacticSettings.clickTarget(npiName);
        tacticSettings.clickOk();
        tacticSettings.clickClose();
    }

    @And("User navigates to run report from mega menu of the life application")
    public void user_navigate_to_run_report() {
        navigation.clickSubMenu();
        navigation.clickRunReport();
    }

    @Then("User selects the report template created tactic and other fields for running the report")
    public void user_enter_input_for_running_report() {
        reportTemplates.enterDetailsToRunReport(templateNameRandom, tacticNameRandom);
    }

    @Then("User verifies the selected campaign,line item, tactic and runs report by clicking on Run button")
    public void user_verifies_the_selected_details() {
        Assert.assertEquals(campaignNameRandom, reportTemplates.verifyAutopopulatedCampaign(campaignNameRandom));
        Assert.assertEquals(lineItemNameRandom, reportTemplates.verifyAutopopulatedLineitem(lineItemNameRandom));
        reportTemplates.runReport();
    }

    @Then("User navigates to generate report field and verifies the report name by campaign name")
    public void user_navigate_to_generate_report_page() {
        navigation.clickSubMenu();
        navigation.clickScheduledReport();
        navigation.clickSubMenu();
        navigation.clickGeneratedReport();
    }

    @Then("User downloads the report and verify the data in downloaded report")
    public void user_download_the_report_from_generated_report_page_and_verify_the_data() throws Exception {
        String filePath = reportTemplates.downloadGeneratedReport(templateNameRandom);
        navigation.clickSubMenu();
        navigation.clickReportTemplate();
        Assert.assertTrue("Report headers match expected values!", reportTemplates.verifyColumnsOfReport(templateNameRandom, filePath));
    }

    /*Roshani Sherkar - 18-06-2025
     * Campaign Dashbaord Features Start*/
    @And("Verify Campaign Dashboard is displayed with title {string}")
    public void verifyCampaignDashboardIsDisplayedWithTitle(String title) {
        Assert.assertEquals(title, campaignDashboard.verifyCampaignDashbaord(title));
    }

    @When("User enters {string} and click Search button")
    public void userEntersAndClickSearchButton(String campaignID) {
        campaignListing.searchCreatedCampaign(campaignID);
        campaignListing.expandCreatedLineItem();
    }

    @Then("Verify Campaigns, line items, tactics names matching the {string} should display on Dashboard table")
    public void verifyCampaignsLineItemsTacticsNamesMatchingTheShouldDisplayOnDashboardTable(String campaignID) {
        Assert.assertEquals(campaignID, campaignDashboard.verifyCampaignDetails(campaignID));
    }

    @When("User add and save comments to Campaign, Line Items and Tactics")
    public void userAddCommentsToCampaignLineItemsAndTactics(DataTable comments) {
        Map<String, String> rawMap = comments.asMap(String.class, String.class);
        Map<String, List<String>> commentMap = CommonUtils.processDataTable(rawMap);
        keyValues.clear();
        for (Map.Entry<String, List<String>> entry : commentMap.entrySet()) {
            keyValues.addAll(entry.getValue());
            String successAlertText = campaignDashboard.addCommentsToCampaign(entry.getKey(), entry.getValue());
            Assert.assertEquals("Success!", successAlertText);
        }
    }

    @Then("Verify comments, icon should display in bluish-green color {string} and comments should available on individual panel")
    public void verifyCommentsAreSavedSuccessfullyIconShouldDisplayInBLUISHGREENAndCommentsShouldAvailableOnIndividualPanel(String colour) {
        List<String> backgroundImage = campaignDashboard.verifyCommentIconColor();
        Assert.assertTrue("Image is matched", backgroundImage.contains(colour));
        List<String> expectedComments = normalize(Collections.singletonList(keyValues.toString()));
        List<String> actualComments = normalize(Collections.singletonList(String.valueOf(campaignDashboard.verifyCommentIconText())));
        Assert.assertEquals(expectedComments, actualComments);
    }

    @When("User toggles the Enabled button for Line Items and Tactics")
    public void userTogglesEnabledButtonForLineItemsAndTacticFromDashboard() {
        campaignDashboard.clickLineAndTacticToggleButton();
    }

    @Then("Verify that Line Items and Tactics reflect the correct enabled or disabled state")
    public void verifyLineItemsAndTacticsAreEnabledDisabledAccordingly() {
        Assert.assertTrue("Buttons are clickable and functional", campaignDashboard.verifyLineTacticToggleStatus());
    }

    @When("User clicks Campaign {string}, Line Item and Tactic")
    public void userClicksCampaignLineItemAndTacticOneByOne(String campaignID) {
        campaignDashboard.navigateToCampaignLIAndTactic(campaignID);
    }

    @Then("Verify user should navigate to Campaign, Line Item and Tactic")
    public void verifyUserShouldNavigateToRespectivePanel() {
        Assert.assertTrue("Navigated to each panel successfully", campaignDashboard.verifyPanelTitleText());
    }

    @When("User clicks Menu option and selects column names")
    public void userClicksMenuOptionAndSelectsColumnNames(DataTable columnNames) {
        List<String> columns = columnNames.asList(String.class);
        keyValues.clear();
        keyValues.addAll(columns);
        campaignDashboard.clickMenuTransitionIcon(columns);
    }

    @Then("Verify dashboard is customized and only selected columns are displayed")
    public void verifyDashboardIsCustomizedAndOnlySelectedColumnsAreDisplayed() {
        List<String> columnName = campaignDashboard.fecthDashboardColumns();
        Assert.assertEquals(new HashSet<>(keyValues), new HashSet<>(columnName));
    }

    @When("User clicks HideAll and ShowAll options from Menu")
    public void userClicksHideAllAndShowAllOptionsFromMenu() {
        campaignDashboard.clickHideAndShowAllOption();
    }

    @Then("Dashboard columns should be hidden and shown accordingly")
    public void dashboardColumnsShouldBeHiddenAndShownAccordingly() {
        Assert.assertTrue("Columns are hidden and shown successfully", campaignDashboard.verifyColumnsCount());
    }

    @When("Navigate to any Dashboard column, select the filter and apply")
    public void navigateToAnyDashboardColumnSelectTheFilterAndApply(DataTable filterNames) {
        Map<String, String> rawMap = filterNames.asMap(String.class, String.class);
        Map<String, List<String>> filterMap = CommonUtils.processDataTable(rawMap);
        keyValues.clear();
        for (Map.Entry<String, List<String>> entry : filterMap.entrySet()) {
            keyValues.add(entry.getKey());
            campaignDashboard.applyFilterOnSelectedColumns(entry.getKey(), entry.getValue());
        }
    }

    @Then("Verify the data should filter as per the selected filter values")
    public void verifyTheDataShouldFilterAsPerTheSelectedFilterValues() {
        List<String> selectedFilter = campaignDashboard.verifySelectedFilter();
        Assert.assertEquals(keyValues, selectedFilter);
    }

    @And("Filter icon should display in the column header to which filter is applied and a red bullet {string} on the filter icon present next to global search")
    public void filterIconShouldDisplayInTheColumnHeaderToWhichFilterIsAppliedAndARedBulletOnTheFilterIconPresentNextToGlobalSearch(String iconColor) {
        String filterIconColor = campaignDashboard.verifyFilterIcon();
        Assert.assertEquals(iconColor, filterIconColor);
    }

    @When("User clicks Favorite star icon on few campaigns and checks Favorite Only checkbox")
    public void userClicksFavoriteStarIconOnFewCampaignsAndChecksFavoriteOnlyCheckbox() {
        campaignDashboard.clickFavoriteOnlyCheckbox();
    }

    @Then("Verify the dashboard results should show only campaigns which are marked as favorite")
    public void verifyTheDashboardResultsShouldShowOnlyCampaignsWhichAreMarkedAsFavorite() {
        int count = campaignDashboard.verifyCampaignMarkedFavorite();
        String message = " ";
        if (count == 0) {
            message = "No campaigns matching filtering criteria found";
        } else {
            message = "Campaigns matching filtering criteria found";
        }
        Assert.assertTrue(message, true);
    }

    @When("User clicks Hide Finished checkbox")
    public void userClicksHideFinishedCheckbox() {
        campaignDashboard.clickHideFinishedCheckbox();
    }

    @Then("Verify the dashboard data should not reflect campaigns with Finished status")
    public void verifyTheDashboardDataShouldNotReflectCampaignsWithFinishedStatus() {
        Assert.assertTrue("Campaigns with Finished Status are hidden", campaignDashboard.verifyHideFinishedCampaignList());
    }

    @When("User clicks Active Flights, Today and Yesterday filter option type")
    public void userClicksActiveFlightsTodayAndYesterdayFilterOptionType() {
        flag = campaignDashboard.clickAndVerifyFilterOptionTypeButton();
    }

    @Then("Verify only Active Flights should render on the Dashboard")
    public void verifyOnlyActiveFlightsShouldRenderOnTheDashboard() {
        Assert.assertTrue("Only Active flights are visible", flag);
    }

    @When("User clicks Custom filter option type and selects date")
    public void userClicksCustomFilterOptionTypeAndSelectsDate() {
        campaignDashboard.clickAndVerifyCustomFilterOption();
    }

    @When("User clicks the Settings icon and selects the following group by options")
    public void userClicksTheSettingsIconAndSelectsTheFollowingGroupByOptions() {
        flag = campaignDashboard.clickGroupByOptionsAndFilterDashboardData();
    }

    @Then("Verify the Dashboard data is grouped by the selected options")
    public void verifyTheDashboardDataIsGroupedByTheSelectedOptions() {
        Assert.assertTrue("Dashboard data is grouped by the selected options", flag);
    }

    @When("User hover on the image icon for creative in red color")
    public void userHoverOnTheImageIconForCreativeInRedColor() {
        flag = campaignDashboard.fetchCreativeToolTipText();
    }

    @Then("Tool tip whether creative is assigned to the campaign or not should be reflected")
    public void toolTipWhetherCreativeIsAssignedToTheCampaignOrNotShouldBeReflected() {
        Assert.assertTrue("Tool Tip text is available", flag);
    }

    /* Roshani Sherkar
    Life PMP - Deals Assignment
    * */
    @When("User clicks Tactic Setting tab")
    public void userClicksTacticSettingTab() {
        pmp.navigateToTacticSettingTab();
    }

    @Then("User should navigate to respective Tactic Setting tab")
    public void userShouldNavigateToRespectiveTacticSettingTab() {
        pmp.verifyTacticSettingsText();
    }

    @When("User add new targeting rule for Rule Type {string}")
    public void userAddNewTargetingRuleForRuleType(String ruleType) {
        pmp.addNewTargetingRule();
        pmp.searchTargetingRuleAndSelect(ruleType);
    }

    @Then("user should navigate to PMP Deals Panel")
    public void userShouldNavigateToPMPDealsPanel() {
        Assert.assertEquals("All Deals ", pmp.verifyPMPDealsPanel());
    }

    @When("User clicks {string} Deals Tab")
    public void userClicksPrivateDealsTab(String dealType) {
        pmp.clickDealsTab(dealType);
    }

    @Then("User should see Add New Deal button, filters such as Exchange, Search")
    public void userShouldSeeButtonFiltersSuchAsExchangeAdvertiser() {
        Assert.assertTrue("Button and Filters are not available", pmp.verifyPrivateDealsFilterPanel());
    }

    @When("User tries to save the list without entering any details, an error message should be displayed")
    public void user_tries_to_save_the_list_without_entering_any_details() {
        npiStaticList.saveList();
        assert npiStaticList.listNameError().contains("List Name is required");
        String npiNameTemp = "Temporary List Name";
        npiStaticList.enterListName(npiNameTemp);
        npiStaticList.saveList();
        assert npiStaticList.advertiserError().contains("Advertiser is required");
    }

    @And("User enters the NPI Static list details as {string} {string}")
    public void user_enters_npi_static_list_details(String npiListName, String advertiser) {
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
    }

    @And("User uploads the file {string}")
    public void user_uploads_the_file(String fileName) {
        npiStaticList.uploadStaticListFile(fileName);
    }

    @When("User edits the created list")
    public void user_edits_the_created_list() {
        npiStaticList.clickBackToNPILists();
        npiLists.searchList(npiName);
        npiLists.openSearchedList(npiName);
        npiNameEdited = "Edited" + '_' + timestamp;
        npiStaticList.editListName(npiNameEdited);
        npiStaticList.saveList();
        assert npiStaticList.saveListSuccess().contains("NPI list created");
    }

    @Then("Verify list gets updated successfully")
    public void verify_list_gets_updated_successfully() {
        npiStaticList.clickBackToNPILists();
        npiLists.searchList(npiNameEdited);
        npiLists.openSearchedList(npiNameEdited);
    }

    @When("User deletes the created list")
    public void user_deletes_the_created_list() {
        npiStaticList.deleteList();
    }

    @Then("Verify list gets deleted successfully")
    public void verify_list_gets_deleted_successfully() {
        assert npiStaticList.deleteSuccess().contains("NPI List Deleted");
    }

    @When("User enters below details in respective search field")
    public void userEntersBelowDetailsInRespectiveSearchField(DataTable filterBy) {
        Map<String, String> rawMap = filterBy.asMap(String.class, String.class);
        Map<String, List<String>> filterMap = CommonUtils.processDataTable(rawMap);
        for (Map.Entry<String, List<String>> entry : filterMap.entrySet()) {
            flag = pmp.applyFilter(entry.getKey(), entry.getValue());
        }
    }

    @Then("Verify private deals list should appear based on the filter selected")
    public void verifyPrivateDealsBasedOnTheFilterSelected() {
        Assert.assertTrue("Button and Filters are not available", flag);
    }

    @And("User clicks on Add New Deal button")
    public void userClicksOnAddNewDealButton() {
        pmp.clickAddNewDeals();
    }

    @Then("New Deal panel should open and user should be able to add new deal with details {string}, {string}, {string}, {string}, {string}, {string}")
    public void newDealPanelShouldOpenAndUserShouldBeAbleToAddNewDealWithDetails(String exchangeType, String dealID, String dealName, String mediaType, String dealPriceType, String price) {
        dealIDRandom = dealID + CommonUtils.timeStampCalculation();
        dealNameRandom = dealName + CommonUtils.timeStampCalculation();
        List<String> mediaTypeList = Arrays.stream(mediaType.split(",")).toList();
        Assert.assertEquals("Success!", pmp.addAndSaveNewDeals(exchangeType, dealIDRandom, dealNameRandom, mediaTypeList, dealPriceType, price));
    }

    @When("User searches the deal and assign it from the deal list")
    public void userSelectsTheDealFromTheDealList() {
        pmp.selectDealFromListAndAssign(dealNameRandom);
    }

    @Then("Selected Deals should appear in Applied Deals panel")
    public void selectedDealsShouldAppearInAppliedDealsPanel() {
        Assert.assertTrue("Unable to assign deals", pmp.verifyAsignedDealsList());
    }

    @And("Verify Target Applied Deals Toggle button is available with default value as {string}")
    public void verifyTargetAppliedDealsToggleButtonIsAvailableWithDefaultValueAsON(String toggleButton) {
        Assert.assertTrue("Default value is " + toggleButton, pmp.verifyTargetAppliedDealsToggle(toggleButton));
    }

    @When("User clicks on OK button")
    public void userClicksOnOKButton() {
        pmp.saveDealsAssigned();
    }

    @Then("Deal details should appear on Tactic Settings tab under Targeting section, Curated Market and Deals section depending on toggle button {string}")
    public void dealDetailsShouldAppearOnTacticSettingsTab(String toggleButton) {
        Assert.assertTrue("Assigned Deals are not present under targeting and deals section", pmp.verifyAssignedDealsOnTactic(dealNameRandom, toggleButton));
    }

    @And("Verify Delete icon is disabled and error message {string}")
    public void verifyDeleteIconIsDisabledAndOnHoverShowErrorMessage(String errorMessage) {
        flag = pmp.verifyDeleteIconAndMessage(errorMessage);
        Assert.assertEquals("Go to the Curated Markets & Deals section to remove the market.", errorMessage);
    }

    @And("Verify Pricing Strategy is editable for Deals present in Curated Market and Deals section")
    public void verifyPricingStrategyIsEditableForDealsPresentInCuratedMarketAndDealsSection(DataTable pricingStrategy) {
        Map<String, String> rawMap = pricingStrategy.asMap(String.class, String.class);
        Map<String, List<String>> filterMap = CommonUtils.processDataTable(rawMap);
        for (Map.Entry<String, List<String>> entry : filterMap.entrySet()) {
            pmp.verifyPricingStrategyIsEditable(dealNameRandom, entry.getKey(), entry.getValue());
        }
    }

    @And("Verify user can add new {string} deals by clicking Add Deal button present in Curated Market and Deals section using details {string}, {string}, {string}, {string}, {string}, {string} with toggle {string}")
    public void verifyUserCanApplyDealsByClickingAddDealButtonPresentInCuratedMarketAndDealsSection(String dealType, String exchangeType, String dealID, String dealName, String mediaType, String dealPriceType, String price, String toggleButton) {
        List<String> mediaTypeList = Arrays.stream(mediaType.split(",")).toList();
        dealIDRandom = dealID + CommonUtils.timeStampCalculation() + "_01";
        dealNameRandom = dealName + CommonUtils.timeStampCalculation() + "_01";
        Assert.assertTrue("Assigned Deals are not present under targeting and deals section",
                pmp.applyDealsFromDealsSection(dealType, exchangeType, dealIDRandom, dealNameRandom, mediaTypeList, dealPriceType, price, toggleButton));
    }

    @And("Verify Base Bid Price {string} and Max Bid Price {string} fields are editable when deals are targeted")
    public void verifyBaseBidPriceAndMaxBidPriceFieldsAreEditableWhenDealsAreTargeted(String baseBidPrice, String maxBidPrice) {
        Assert.assertTrue("Base and Max Bid Price fields are editable", pmp.verifyBaseAndMaxPriceIsEditable(baseBidPrice, maxBidPrice));
    }

    @When("User clicks Save button from Tactic Setting tab")
    public void userClicksSaveButtonFromTacticSettingTab() {
        pmp.saveTacticSettings();
    }

    @Then("Deals should get assigned to the Tactic")
    public void dealsShouldGetAssignedToTheTactic() {
        Assert.assertEquals("Success!", pmp.verifyTacticIsSaved().trim());
    }


    @Then("User should see All Premium Pubs, filters such as Exchange, Search")
    public void userShouldSeeAllPremiumPubsFiltersSuchAsExchangeSearch(DataTable premiumHubs) {
        List<String> premiumHubsList = premiumHubs.asList(String.class);
        Assert.assertTrue("All premium Hubs are available and clickable", pmp.verifyAllPremiumHubsOnMarketPlace(premiumHubsList));
    }

    /*Roshani Sherkar
     * 01-07-2025*/
    @Then("Verify targeting panel with all targeting under below categories")
    public void verifyTargetingPanelWithAllTargetingUnderBelowCategories(DataTable targetCategory) {
        List<String> targetCategoryList = targetCategory.asList(String.class);
        Assert.assertTrue("Category names are not matched", tacticSettings.fetchAndVerifyTargetCategoryName(targetCategoryList));
    }

    @And("Verify target type with respect to category")
    public void verifyTargetTypeWithRespectToCategory(DataTable categoryNameAndType) {
        Map<String, String> rawMap = categoryNameAndType.asMap(String.class, String.class);
        Map<String, List<String>> categoryNameAndTypeMap = CommonUtils.processDataTable(rawMap);
        for (Map.Entry<String, List<String>> entry : categoryNameAndTypeMap.entrySet()) {
            String key = entry.getKey();
            List<String> expectedValues = entry.getValue();
            List<String> actualValues = tacticSettings.getTargetTypesForCategory(key);
            for (String expected : expectedValues) {
                Assert.assertTrue("Expected value '" + expected + "' not found for category '" + key + "'. Found: " + actualValues, actualValues.contains(expected));
            }
        }
    }

    /*Roshani Sherkar
     * 08-07-2025*/
    @When("User navigates to Targeting template page by clicking the icon from Activation section")
    public void userNavigatesToTargetingTemplatePageByClickingTheIconFromActivationSection() {
        navigation.clickSubMenu();
        navigation.clickTargetingTemplate();
    }

    @Then("Verify New Template button is present above the Search option")
    public void verifyNewTemplateButtonIsPresentAboveTheSearchOption() {
        Assert.assertTrue("Targeting Button and Search Box are not displayed", targetingTemplate.verifyTargetingButtonAndSearchBox());
    }

    @And("Verify Targeting template section opens by clicking New Template button")
    public void verifyTargetingTemplateSectionByClickingNewTemplateButton() {
        Assert.assertTrue("All fields require to create targeting template are not available", targetingTemplate.clickAndVerifyTargetingTemplate());
    }

    @When("User creates Targeting template {string} for the line items {string} with channel {string} and Targeting Rules")
    public void userCreatesTargetingTemplateForTheLineItemsWithChannelAndTargetingRules(String templateName, String lineItems, String channel, DataTable ruleTypeAndOptions) {
        Map<String, String> rawMap = ruleTypeAndOptions.asMap(String.class, String.class);
        Map<String, List<String>> rulesMap = CommonUtils.processDataTable(rawMap);
        List<String> lineItemsList = Arrays.stream(lineItems.split(",")).toList();
        List<String> channelList = Arrays.stream(channel.split(",")).toList();
        keyValueMap = targetingTemplate.createAndSaveTargetingTemplate(templateName, lineItemsList, channelList, rulesMap);
    }

    @Then("User searches and verifies the already created targeting template using the search option")
    public void userSearchesTheAlreadyCreatedTargetingTemplateUsingTheSearchOption() {
        Assert.assertTrue("Targeting template is not found in the search results", targetingTemplate.searchTargetingTemplate(new ArrayList<>(keyValueMap.keySet())));
    }

    @And("User tries to save the targeting template with targeting rule {string} and without specifying a template name")
    public void userTriesToSaveTheTargetingTemplateWithTargetingRuleAndWithoutSpecifyingATemplateName(String targetingRule) {
        Assert.assertEquals("Template Name is required", targetingTemplate.verifyErrorMessageForTemplateName(targetingRule));
    }

    @And("User tries to save the targeting template with template name {string} without specifying any targeting")
    public void userTriesToSaveTheTargetingTemplateWithTemplateNameWithoutSpecifyingAnyTargeting(String templateName) {
        Assert.assertEquals("Please select atleast one targeting", targetingTemplate.verifyErrorMessageForTargetingRules(templateName));
    }

    @And("User clicks on Show Expression and verifies the query is displayed for the {string}")
    public void userClicksOnAndVerifiesTheQueryIsDisplayed(String templateName) {
        Assert.assertTrue("Targeting container is not displayed", targetingTemplate.clickAndVerifyShowExpression(templateName));
    }

    @And("User edits an existing targeting template and verifies the changes are saved for the {string}")
    public void userEditsAnExistingTargetingTemplateAndVerifiesTheChangesAreSaved(String templateName) {
        Assert.assertTrue("Unable to edit targeting template", targetingTemplate.clickAndVerifyTargetTemplateEditable(templateName));
    }

    @And("User deletes an existing targeting template and verifies it is removed from the list for the {string}")
    public void userDeletesAnExistingTargetingTemplateAndVerifiesItIsRemovedFromTheList(String templateName) {
        Assert.assertEquals("Target template deleted successfully", targetingTemplate.clickAndVerifyTargetTemplateDeletion(templateName));
    }

    @And("Create a tactic with {string} line items and other details {string} {string} {string} {string} {string} {string} {string} and import the template in Tactic")
    public void createATacticWithLineItemsAndOtherDetails(String lineItemType, String advertiser, String campaign_name, String campaign_type, String budget, String lineItemName, String lineBudget, String tacticName) {
        List<String> lineItemTypeList = Arrays.stream(lineItemType.split(",")).toList();
        List<String> templateList = new ArrayList<>(keyValueMap.keySet());
        List<Map<String, String>> ruleCountAndValueList = new ArrayList<>(keyValueMap.values());
        flag = tacticDetails.createTacticWithLineItemsAndImport(lineItemTypeList, advertiser, campaign_name, campaign_type, budget, lineItemName, lineBudget, tacticName, templateList, ruleCountAndValueList);
    }

    @Then("Verify the template created can be imported in the Tactic")
    public void verifyTheTemplateCreatedCanBeImported() {
        Assert.assertTrue("Tactic is not created with the imported template", flag);
    }

    @And("User selects the Attributes List and uploads the file {string}")
    public void userSelectsTheAttributesListAndUploadsTheFile(String attributesFile) {
        npiAttributesList.uploadAttributesFile(attributesFile);
        assert npiAttributesList.verifyFileUploadSuccess().contains("Successfully uploaded");
    }

    @Then("Verify file {string} is uploaded successfully")
    public void verifyFileIsUploadedSuccessfully(String attributesFile) {
        assert npiAttributesList.verifyFileUploadSuccess().contains("Successfully uploaded Excel file : " + attributesFile);
    }

    @And("User selects the {string} column and clicks on Next")
    public void userSelectsTheNPIColumnAndClicksOnNext(String columnName) {
        npiAttributesList.selectNPIColumn(columnName);
        npiAttributesList.clickNextButton();
    }

    @When("User tries to save the Attribute list without entering any details, an error message should be displayed")
    public void userSavesAttributeListWithoutAnyDetails() {
        npiAttributesList.clickNextButton();
        assert npiAttributesList.listNameError().contains("List Name is required");
        String listName = "Temporary List Name";
        npiAttributesList.enterListName(listName);
        npiAttributesList.clickNextButton();
        assert npiAttributesList.advertiserError().contains("Advertiser is required");
    }

    @And("User enters the Attributes list details as {string} {string}")
    public void userEntersTheAttributesListDetailsAs(String listName, String advertiser) {
        npiName = listName + '_' + timestamp;
        npiAttributesList.enterListName(npiName);
        npiAttributesList.selectAdvertiser(advertiser);
    }

    @When("User makes list available in LIFE and HCP365 and clicks on next")
    public void userMakesListAvailableInLifeAndHCP365AndClicksOnNext() {
        npiAttributesList.selectProduct();
        npiAttributesList.clickNextButton();
    }

    @Then("Verify the Attributes list is saved successfully")
    public void verifyTheAttributesListIsSavedSuccessfully() {
        assert npiAttributesList.saveListSuccess().contains("NPI list created");
    }

    @When("User edits the saved list")
    public void userEditsTheSavedList() {
        npiAttributesList.clickBackToNPILists();
        npiLists.searchList(npiName);
        npiLists.openSearchedList(npiName);
        npiNameEdited = "Edited" + '_' + timestamp;
        npiAttributesList.editListName(npiNameEdited);
        npiAttributesList.saveList();
        assert npiAttributesList.updateListSuccess().contains("NPI list updated");
    }

    @Then("Verify the updates are applied successfully")
    public void verifyTheUpdatesAreAppliedSuccessfully() {
        npiAttributesList.clickBackToNPILists();
        npiLists.searchList(npiNameEdited);
        npiLists.openSearchedList(npiNameEdited);
    }

    @When("User deletes the Attribute list")
    public void userDeletesTheAttributeList() {
        npiAttributesList.deleteList();
    }

    @Then("Verify the list is deleted successfully")
    public void verifyTheListIsDeletedSuccessfully() {
        assert npiAttributesList.deleteSuccess().contains("NPI List Deleted");
    }

    /* Roshani Sherkar
     * 18-07-2025
     * Targeting Template Creation from Tactic
     * */
    @And("Create a tactic with below targeting rules and {string} line items and other details {string} {string} {string} {string} {string} {string} {string}")
    public void createATacticWithBelowTargetingRulesAndLineItemsAndOtherDetails(String lineItemType, String advertiser, String campaign_name, String campaign_type, String budget, String lineItemName, String lineBudget, String tacticName, DataTable ruleTypeAndOptions) {
        Map<String, String> rawMap = ruleTypeAndOptions.asMap(String.class, String.class);
        Map<String, List<String>> rulesMap = CommonUtils.processDataTable(rawMap);
        List<String> lineItemTypeList = Arrays.stream(lineItemType.split(","))
                .map(String::trim)
                .toList();
        List<String> templateNameList = tacticDetails.createTacticWithLineItemsAndTargetingRules(lineItemTypeList, advertiser, campaign_name, campaign_type, budget, lineItemName, lineBudget, tacticName, rulesMap);
        for (String templateName : templateNameList) {
            keyValueMap.put(templateName, new HashMap<>());
        }
    }

    @Then("Verify the template created are saved")
    public void verifyTheTemplateCreatedAreSaved() {
        Assert.assertFalse("Unable to save targeting templates", keyValueMap.isEmpty());
    }


    /*Roshani Sherkar
     * 14-07-2024
     * Creatives creation*/
    @And("User clicks Creative Library options present under Activation tab")
    public void userClicksCreativeLibraryOptionsPresentUnderActivationTab() {
        navigation.clickSubMenu();
        navigation.clickCreativeLibrary();
    }

    @Then("Verify Creative Library page is displayed")
    public void verifyCreativeLibraryPageIsDisplayed() {
        Assert.assertEquals("Creatives", createCreatives.verifyCreativeLibraryPageTitle());
    }

    @And("Check Activity buttons {string} and verify following filters are available and working")
    public void checkActivityButtonsAndVerifyFollowingFiltersAreAvailableAndWorking(String buttonType, DataTable filters) {
        Map<String, String> rawFilters = filters.asMap(String.class, String.class);
        Map<String, List<String>> filtersMap = CommonUtils.processDataTable(rawFilters);
        createCreatives.clickActivityButton(buttonType);
        Assert.assertTrue("Activity " + buttonType + " button is not clicked", createCreatives.verifyArchiveUnarchiveButtonsPresent(buttonType));
        Assert.assertTrue("Archive/Urachive buttons are not working", createCreatives.clickArchiveUnarchiveButtons());
        for (Map.Entry<String, List<String>> entry : filtersMap.entrySet()) {
            flag = createCreatives.verifyFilterOptions(entry.getKey(), entry.getValue());
        }
    }


    @And("Verify the following sort options are available and working")
    public void verifyTheFollowingSortOptionsAreAvailableAndWorking(DataTable sortOptions) {
        List<String> sortOptionsList = sortOptions.asList(String.class);
        Assert.assertTrue("Sort is not working", createCreatives.verifySortOptions(sortOptionsList));
    }

    @And("Verify Search Box is available and working")
    public void verifySearchBoxIsAvailableAndWorking(DataTable searchValues) {
        List<String> searchValuesList = searchValues.asList(String.class);
        createCreatives.clickActivityButton("Active");
        Assert.assertTrue("Search is not working", createCreatives.searchByValues(searchValuesList));
    }

    @And("Verify Copy option is available and working")
    public void verifyCopyOptionIsAvailableAndWorking() {
        Assert.assertEquals("Success!", createCreatives.copyCreative());
    }


    @When("User creates and saves {string} creative using details {string} as Advertiser, {string} as Creative Name, {string}, {string} and below Creative attributes")
    public void userCreatesAndSavesCreativeUsingDetailsAsAdvertiserAsCreativeNameAndBelowCreativeAttributes(String creativeType, String advertiser, String creativeName, String advertiserDSA, String financer, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        nameList.clear();
        for (Map<String, String> row : rows) {
            String type = row.get("CreativeType").trim();
            String attributes = row.get("CreativeAttributes").trim();

            String newCreativeName = creativeType + "_" + creativeName + '_' + CommonUtils.timeStampCalculation();
            createCreatives.clickNewCreativeButton();
            createCreatives.enterCreativeDetails(advertiser, newCreativeName, advertiserDSA, financer);
            createCreatives.selectCreativeType(creativeType);

            Map<String, String> attributeMap = Arrays.stream(attributes.split(","))
                    .map(String::trim)
                    .map(entry -> entry.split(":", 2))
                    .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));

            createCreatives.fillAttributes(type, attributeMap);
            Assert.assertEquals("Success!", createCreatives.saveCreative());
            nameList.addAll(createCreatives.fetchCreatives());
        }
    }

    @Then("Verify the newly created creative is displayed in the Creative Library page")
    public void verifyTheNewlyCreatedCreativeIsDisplayedInTheCreativeLibraryPage() {
        for (String name : nameList) {
            Assert.assertTrue("Creative " + name + " is not found in the library", createCreatives.verifyCreativesInLibrary(name));
        }
    }

    @And("Create and verify a tactic with {string} line items and other details {string} {string} {string} {string} {string} {string} {string} and assign the created creatives to it")
    public void createATacticWithLineItemsAndOtherDetailsAndAssignTheCreatedCreativesToIt(String lineItemType, String advertiser, String campaign_name, String campaign_type, String budget, String lineItemName, String lineBudget, String tacticName) {
        List<String> lineItemTypeList = Arrays.stream(lineItemType.split(","))
                .map(String::trim)
                .toList();
        for (String creativeName : nameList) {
            String creativeType = creativeName.replaceAll("_Creative_\\d+_\\d+", "").trim();
            String matchedLineItemType;
            switch (creativeType) {
                case "HTML" -> matchedLineItemType = "Display";
                case "Native" -> matchedLineItemType = "Native Display";
                default -> matchedLineItemType = creativeType;
            }
            if (lineItemTypeList.contains(matchedLineItemType)) {
                boolean result = tacticDetails.createTacticWithLineItemsAndAssignCreative(matchedLineItemType, advertiser, campaign_name, campaign_type, budget, lineItemName, lineBudget, tacticName, creativeName);
                Assert.assertTrue("Creative is not assigned to Tactic", result);
            }
        }

    }

    /*Roshani Sherkar
     * Auto-Imported List*/
    @And("User selects the Auto-Imported List")
    public void userSelectsTheAutoImportedList() {
        npiLists.clickAutoImportedList();
    }

    @And("Verify if user navigates to the Auto-Imported List page")
    public void verifyIfUserNavigatesToTheAutoImportedListPage() {
        Assert.assertEquals("Setup Import", npiAutoImportedList.verifyIfAutoImportPage());
    }

    @Then("User tries to save the Auto-Imported list without entering any details, an error message should be displayed")
    public void userTriesToSaveTheAutoImportedListWithoutEnteringAnyDetailsAnErrorMessageShouldBeDisplayed() {
        npiAutoImportedList.clickSetupImportButton();
        Assert.assertEquals("Advertiser is required", npiAutoImportedList.verifyErrorMessage());
    }

    @When("User enters the Auto-Imported list details as {string} {string}")
    public void userEntersTheAutoImportedListDetailsAs(String listName, String advertiser) {
        npiName = listName + '_' + timestamp;
        npiAttributesList.enterListName(npiName);
        npiAttributesList.selectAdvertiser(advertiser);
    }

    @And("User makes list available in LIFE and HCP365 module")
    public void userMakesListAvailableInLIFEAndHCP() {
        npiAttributesList.selectProduct();
    }

    @And("User clicks Setup Import button to import File details")
    public void userClicksSetupImportButtonToImportFileDetails() {
        npiAutoImportedList.clickSetupImportButton();
        npiAutoImportedList.waitForImportSettingPanel();
    }

    @And("User enters file details {string} {string} {string}")
    public void userEntersImportSettingWithDetails(String fileLocation, String filePath, String fileName) {
        npiAutoImportedList.enterFileDetails(fileLocation, filePath.trim(), fileName.trim());
    }

    @And("User selects the {string} radio button")
    public void userSelectsTheListType(String listType) {
        npiAutoImportedList.selectListType(listType);
    }

    @And("User enters NPI column {string} {string}")
    public void userEntersNPIColumnName(String npiColumn, String columnName) {
        npiAutoImportedList.enterColumnName(npiColumn, columnName);
    }

    @And("User selects the {string}")
    public void userSelectsTheImportType(String importType) {
        npiAutoImportedList.selectImportType(importType);
    }

    @Then("User clicks Check File button to verify the file details are correct")
    public void userClicksCheckFileButtonToVerifyTheFileDetailsAreCorrect() {
        npiAutoImportedList.clickCheckFile();
    }

    @Then("User saves the import settings and verifies the data is imported successfully")
    public void userSavesTheImportSettingsAndVerifiesTheIsSavedSuccessfully() {
        npiAutoImportedList.clickOKButton();
    }

    @And("Verify that Token is fetched successfully from URL {string}")
    public void verifyThatTokenIsFetchedSuccessfully(String url) {
        constants.TOKEN = npiAutoImportedList.fetchToken(url);
        Assert.assertNotNull("Token is not fetched", constants.TOKEN);
    }

    @And("Pass token in the API Header and run it to upload the data into the list")
    public void runAPIToUploadTheListDataIntoTheList() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Token", constants.TOKEN);
        response = npiAutoImportedList.runAPI(constants.BASE_URL, constants.ENDPOINT_PATH, headers);
    }

    @And("Verify list data is uploaded successfully")
    public void verifyListDataIsUploadedSuccessfully() {
        Assert.assertEquals(204, response.status());
    }

    @And("Refresh the Browser to view the data uploaded")
    public void refreshTheBrowserToViewTheDataUploaded() {
        Assert.assertTrue("NPI List is not available", npiAutoImportedList.refreshBrowser());
    }

    @And("Verify the Total NPI count displayed in Matched NPI section is similar to NPI records present in {string}")
    public void verifyMatchedNPISectionIsDisplayedWithTheTotalNPICount(String fileName) throws CsvValidationException, IOException {
        String totalNPICount = npiAutoImportedList.fetchTotalNPICount();
        String npiRecordsFromFile = npiAutoImportedList.fetchNPIRecordFromTestFile(fileName);
        Assert.assertEquals("Count is not matching", totalNPICount, npiRecordsFromFile);
    }

    @And("Verify Reload Now button is available and enabled")
    public void verifyReloadNowButtonIsAvailableAndEnabled() {
        npiAutoImportedList.verifyIfImportSettingButtonIsVisible();
        Assert.assertTrue("Reload Now Button is not available", npiAutoImportedList.verifyReloadNowButton());
    }

    @When("User clicks on Reload Now button")
    public void userClicksOnReloadNowButton() {
        npiAutoImportedList.clickReloadNowButton();
    }

    @Then("Verify the file is reloaded successfully")
    public void verifyTheFileIsReloadedSuccessfully() {
        Assert.assertEquals("File is reloaded", npiAutoImportedList.verifyIfFileIsReloaded());
    }

    /*Roshani Sherkar
     * 07-08-2025
     * Domain List*/
    @Given("User navigates to the {string} page")
    public void userNavigatesToTheDomainListPage(String pageName) {
        navigation.clickSubMenu();
        sharedList.clickDomainListFromMenu(pageName);
    }

    @And("Verify that the search option is present on the {string} tab")
    public void verifyThatTheSearchOptionIsPresentOnTheTab(String listName) {
        Assert.assertEquals("Tab is not opened", listName.trim(), sharedList.verifyIfListPageIsOpen(listName.trim()));
        Assert.assertTrue("Search Box is not available", sharedList.verifyIfSearchBoxIsPresent());
    }

    @And("Verify that the sub-tabs {string} on the left navigation panel are available and {string} is selected by default")
    public void verifyThatTheSubTabsOnTheLeftNavigationPanelAreAvailable(String subTabs, String defaultTabName) {
        List<String> subTabsList = CommonUtils.convertStringToList(subTabs);
        Assert.assertTrue("Tabs are not present", sharedList.verifySubTabs(subTabsList));
    }

    @And("Verify that when the {string} tab is selected, only {string} lists are visible in the panel")
    public void verifyThatWhenTheTabIsSelectedListsAreVisibleInThePanel(String tabName, String listName) {
        sharedList.clickSubTab(tabName);
        Assert.assertTrue(tabName + " Tab list is not available", sharedList.verifyListIsAvailable(listName));
    }

    @And("User selects the {string} radio button from create new list page")
    public void userSelectsTheRadioButtonFromCreateNewListPage(String listType) {
        sharedList.clickListTypeRadioButton(listType);
    }

    @Then("Verify that the Create New List screen is displayed")
    public void verifyThatTheCreateListScreenIsDisplayed() {
        Assert.assertTrue("", sharedList.verifyNewListPage());
    }

    @And("Verify that an error message is displayed when no listname {string} or {string} names are specified")
    public void verifyThatAnErrorMessageIsDisplayedWhenNoNamesAreSpecified(String listName, String listType) {
        metricName = listName + "_" + CommonUtils.timeStampCalculation();
        Assert.assertEquals("List Name is required", sharedList.validateErrorOnEmptyListNameInput(metricName));
        switch (listType) {
            case "Domains":
                Assert.assertEquals("Domain name is required", sharedList.validateErrorOnEmptyListInput(metricName));
                break;
            case "AppBundle":
                Assert.assertEquals("AppBundle name is required", sharedList.validateErrorOnEmptyListInput(metricName));
                break;
            case "Keywords":
                Assert.assertEquals("Keyword is required", sharedList.validateErrorOnEmptyListInput(metricName));
                break;
            case "IP Address":
                Assert.assertEquals("IPAddress is required", sharedList.validateErrorOnEmptyListInput(metricName));
                break;
            default:
                Assert.fail("Invalid list type specified: " + listType);
        }
    }

    @And("Verify that if multiple {string} are specified on a single line, a validation error is shown")
    public void verifyThatIfMultipleDomainNamesAreSpecifiedOnASingleLineAValidationErrorIsShown(String domainName) {
        List<String> domainNameList = CommonUtils.convertStringToList(domainName);
        Assert.assertTrue("No Validation error is displayed", sharedList.checkErrorOnSingleLineMultipleDomainsInput(domainNameList).contains("validation error(s)"));
    }

    @And("Verify that when {string} names are specified manually, the option to upload a file disappears")
    public void verifyThatWheNamesAreSpecifiedManuallyTheOptionToUploadAFileDisappears(String listType) {
        keyValues.clear();
        keyValues = new ArrayList<>(CommonUtils.convertStringToList(listType));
        Assert.assertTrue("Upload section is not available", sharedList.verifyUploadSectionIsVisibleBeforeListInput());
        sharedList.enterDomainNames(keyValues);
        Assert.assertTrue("Upload section is available", sharedList.verifyUploadSectionIsVisibleAfterListInput());
    }

    @And("Verify that the user is able to create a {string} list by specifying names manually")
    public void verifyThatTheUserIsAbleToCreateAListBySpecifyingNamesManually(String listType) {
        sharedList.saveList();
        switch (listType) {
            case "Domains":
                Assert.assertEquals("Domain list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "AppBundle":
                Assert.assertEquals("AppBundle list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "Keywords":
                Assert.assertEquals("Keywords list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "IP Address":
                Assert.assertEquals("IPAddress list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            default:
                Assert.fail("Invalid list type specified: " + listType);
        }
    }

    @And("Verify that PulsePoint provided domain list {string} is denoted with a purple P icon")
    public void verifyThatPulsePointProvidedListsAreDenotedWithAPurpleIconUnderTheTab(String pulsepointProvidedDomainList) {
        sharedList.searchAndOpenCreatedList(pulsepointProvidedDomainList);
        Assert.assertTrue("P icon is not present on the PulsePoint provided list", sharedList.fetchPulsepointIcon(pulsepointProvidedDomainList));
    }

    @And("Verify that the counter on the left displays the correct value for each list in the navigation panel")
    public void verifyThatTheCounterOnTheLeftDisplaysTheCorrectValueForEachListInTheNavigationPanel() {
        sharedList.searchAndOpenCreatedList(metricName);
        totalListCount = Integer.parseInt(sharedList.fetchCountFromLeftPanel(metricName));
        Assert.assertEquals(totalListCount, keyValues.size());
    }

    @And("Verify that the user is able to edit an existing {string} name list {string}")
    public void verifyThatTheUserIsAbleToEditAnExistingNameList(String listType, String modifiedName) {
        keyValues = new ArrayList<>(CommonUtils.convertStringToList(modifiedName));
        sharedList.editAnExistingList(keyValues);
        sharedList.saveList();
        switch (listType) {
            case "Domains":
                Assert.assertEquals("Domains list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "AppBundle":
                Assert.assertEquals("AppBundle list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "Keywords":
                Assert.assertEquals("Keywords list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "IP Address":
                Assert.assertEquals("IPAddresses list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            default:
                Assert.fail("Invalid list type specified: " + listType);
        }
    }

    @And("Verify that the user is able to delete an existing {string} name list")
    public void verifyThatTheUserIsAbleToDeleteAnExistingNameList(String listType) {
        sharedList.deleteList();
        Assert.assertEquals(metricName, sharedList.fetchRemovalConfirmation());
        switch (listType) {
            case "Domains":
                Assert.assertEquals("Domain deleted successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "AppBundle":
                Assert.assertEquals("AppBundleGroup deleted successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "Keywords":
                Assert.assertEquals("Keyword deleted successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "IP Address":
                Assert.assertEquals("IPAddress deleted successfully", sharedList.isListCreatedOrDeleted());
                break;
            default:
                Assert.fail("Invalid list type specified: " + listType);
        }
    }

    /*Roshani Sherkar
     * 11/08/2025
     * Domain List Creation by File Upload*/
    @And("Verify that an error message is displayed when no list names is specified and user tries to upload a file {string}")
    public void verifyThatAnErrorMessageIsDisplayedWhenNoListNamesIsSpecifiedAndUserTriesToUploadAFile(String fileName) {
        sharedList.uploadDomainFile(fileName);
        Assert.assertEquals("List Name is required", sharedList.fetchListErrorMessage());
    }

    @And("Verify that when enters {string} and upload file {string} option is selected, the text area to direct enter the names disappears")
    public void verifyThatWhenUploadFileOptionIsSelectedTheTextAreaToDirectEnterTheNamesDisappears(String listName, String fileName) {
        metricName = listName + "_" + CommonUtils.timeStampCalculation();
        npiName = metricName;
        sharedList.enterListName(metricName);
        Assert.assertTrue("Text area is not available", sharedList.verifyTextAreaIsVisibleBeforeFileUpload());
        sharedList.uploadDomainFile(fileName);
        Assert.assertTrue("Text area is available", sharedList.verifyTextAreaIsVisibleAfterFileUpload());
    }

    @And("Verify the Uploaded Files section displays the entries count, includes download and delete icons after the file {string} is uploaded")
    public void verifyUploadedFilesSectionDisplaysEntriesIncludedInTheFileTimestampDownloadAndDeleteIconsOnceTheFileIsUploaded(String fileName) throws CsvValidationException, IOException {
        Assert.assertEquals(fileName, sharedList.fetchFileNameFromUploadedFilesSection(fileName));
        Assert.assertEquals(ExcelActions.countCsvRecords("src/main/resources/uploadfiles/" + fileName), sharedList.fetchDomainCountFromUploadedFilesSection(fileName));
        Assert.assertTrue("No Download icon is available", sharedList.isDownloadIconVisible(fileName));
        Assert.assertTrue("No Delete icon is available", sharedList.isDeleteIconVisible(fileName));
    }

    @And("Verify that the user is able to create a {string} list through file upload")
    public void verifyThatTheUserIsAbleToCreateAListThroughFileUpload(String listType) {
        sharedList.saveList();
        switch (listType) {
            case "Domains":
                Assert.assertEquals("Domains list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "AppBundle":
                Assert.assertEquals("AppBundle list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "Keywords":
                Assert.assertEquals("Keywords list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "IP Address":
                Assert.assertEquals("IPAddresses list created successfully", sharedList.isListCreatedOrDeleted());
                break;
            default:
                Assert.fail("Invalid list type specified: " + listType);
        }
    }

    @And("Verify that the counter on the left displays the correct value after file upload {string}")
    public void verifyThatTheCounterOnTheLeftDisplaysTheCorrectValueAfterFileUpload(String fileName) {
        sharedList.searchAndOpenCreatedList(metricName);
        totalListCount = Integer.parseInt(sharedList.fetchCountFromLeftPanel(metricName));
        itemCount = sharedList.fetchDomainCountFromUploadedFilesSection(fileName);
        Assert.assertEquals(totalListCount, itemCount);
    }

    @And("Verify that the user is able to edit an existing list by uploading same file {string} again and verify the changes")
    public void verifyThatTheUserIsAbleToEditAnExistingNameListByUploadingSameFileAgainAndVerifyTheChanges(String fileName) {
        sharedList.uploadDomainFile(fileName);
        Assert.assertTrue("Different filename is displayed", sharedList.verifyIfDuplicateFileDialogIsDisplayed(fileName));
        sharedList.clickReplaceButton();
    }

    @And("Verify that the user is able to edit and save an existing {string} list by uploading another file {string} and verify the changes")
    public void verifyThatTheUserIsAbleToEditAnExistingNameListByUploadingAnotherFileAndVerifyTheChanges(String listType, String fileName) throws CsvValidationException, IOException {
        sharedList.uploadDomainFile(fileName);
        Assert.assertEquals(fileName, sharedList.fetchFileNameFromUploadedFilesSection(fileName));
        Assert.assertEquals(ExcelActions.countCsvRecords("src/main/resources/uploadfiles/" + fileName), sharedList.fetchDomainCountFromUploadedFilesSection(fileName));
        Assert.assertTrue("No Download icon is available", sharedList.isDownloadIconVisible(fileName));
        Assert.assertTrue("No Delete icon is available", sharedList.isDeleteIconVisible(fileName));
        sharedList.saveList();
        switch (listType) {
            case "Domains":
                Assert.assertEquals("Domains list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "AppBundle":
                Assert.assertEquals("AppBundle list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "Keywords":
                Assert.assertEquals("Keywords list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            case "IP Address":
                Assert.assertEquals("IPAddresses list updated successfully", sharedList.isListCreatedOrDeleted());
                break;
            default:
                Assert.fail("Invalid list type specified: " + listType);
        }
    }

    @And("Verify that the counter on the left displays the updated value after new file upload {string}")
    public void verifyThatTheCounterOnTheLeftDisplaysTheUpdatedValueAfterNewFileUpload(String fileName) {
        sharedList.searchAndOpenCreatedList(metricName);
        int domainCount = Integer.parseInt(sharedList.fetchCountFromLeftPanel(metricName));
        Assert.assertEquals(domainCount, itemCount + sharedList.fetchDomainCountFromUploadedFilesSection(fileName));
    }

    @And("Verify that user is able to download the uploaded file {string}, {string}")
    public void verifyThatUserIsAbleToDownloadTheUploadedFile(String fileName1, String fileName2) {
        sharedList.downloadFile(fileName1);
        Assert.assertTrue("Downloaded file is not available", sharedList.verifyDownloadedFile("domains", "csv"));
        sharedList.downloadFile(fileName2);
        Assert.assertTrue("Downloaded file is not available", sharedList.verifyDownloadedFile("domains", "csv"));
    }

    @And("Verify that the user is able to delete the uploaded file {string}")
    public void verifyThatTheUserIsAbleToDeleteTheUploadedFile(String fileName) {
        sharedList.deleteFile(fileName);
        Assert.assertEquals(fileName, sharedList.fetchRemovalConfirmation());
    }

    /*Roshani Sherkar
     * 20-08-2025
     * Atrribute NPI List creation and targeting it at tactic level*/
    @And("Navigate to Campaign Dashboard and clicks on Create Campaign")
    public void navigateToCampaignDashboardAndClicksOnCreateCampaign() {
        navigation.clickSubMenu();
        navigation.clickCampaigns();
        campaigns.createCampaign();
    }

    @And("User add and configure {string} targeting rule and verify list is displayed in the targeting rule")
    public void userAddAndConfigureNPITargetingRule(String ruleType) {
        tacticSettings.selectTargetingRule(ruleType, npiName);
        Assert.assertTrue("List is not available", tacticSettings.isListAvailableInTargetingPanel(npiName));
        tacticSettings.clickTarget(npiName);
        itemCount = tacticSettings.fetchSelectedListCountFromTargetingPanel();
    }


    @And("Verify that the total NPI count and the matched NPI count from the list are correctly displayed in the targeting rule")
    public void verifyTheTotalNPICountFromTheListIsDisplayedInTheTargetingRule() {
        String npiCount = tacticSettings.fetchTotalNPICountFromNewTab(npiName);
        String[] parts = npiCount.split("&");
        totalListCount = Integer.parseInt(parts[0].split("-")[1]);
        int matchedNPIListCount = Integer.parseInt(parts[1].split("-")[1]);
        String npiCountFromTargetingPanel = tacticSettings.fetchNPICountFromTargetingPanel();
        String matchedNpiCountFromTargetingPanel = tacticSettings.fetchMatchedNPICountFromTargetingPanel();
        Assert.assertEquals("Total NPI count from the list is not matching with the count in targeting rule", String.valueOf(totalListCount), npiCountFromTargetingPanel);
        Assert.assertTrue("Matched NPI count from the list is not matching with the count in targeting rule", matchedNpiCountFromTargetingPanel.contains(String.valueOf(matchedNPIListCount)));

    }

    @And("User saves the rule configured in the tactic")
    public void userSavesTheRuleConfiguredInTheTactic() {
        tacticSettings.clickOk();
        tacticSettings.clickClose();
    }

    @Then("Verify that the {string} rule is added to the tactic and retrieve the count of selected lists")
    public void verifyThatTheNPIRuleIsAddedToTheTacticAndRetrieveTheCountOfSelectedLists(String ruleType) {
        Assert.assertTrue("Unable to add Rule", tacticSettings.verifyIfRuleIsAdded().contains(ruleType));
        String text = tacticSettings.fetchSelectedListCountFromTactic();
        Assert.assertTrue("Selected list count is not matching", text.contains(String.valueOf(itemCount)));
    }

    @And("Verify that the selected list is displayed in the targeting rule and retrieve the total count of targeted items")
    public void verifyThatTheSelectedListIsDisplayedInTheTargetingRuleAndRetrieveTheTotalNPICount() {
        Assert.assertTrue("Selected List is not available", tacticSettings.isSelectedListPresentInTactic(npiName));
        String text = tacticSettings.fetchSelectedListItemCountFromTactic(npiName);
        Assert.assertTrue("Selected list count is not matching", text.contains(String.valueOf(totalListCount)));
    }


    @And("User navigates to Pixels page")
    public void userNavigatesToPixelsPage() {
        navigation.clickSubMenu();
        pixels.clickPixelsMenuItem();
    }

    @When("User clicks on Add Pixel button")
    public void userClicksOnAddPixelButton() {
        pixels.clickAddPixelButton();
    }

    @Then("Verify the Create New Pixel panel and types of Pixel")
    public void verifyCreateNewPixelPanelAndTypesOfPixel() {
        Assert.assertEquals("CREATE NEW PIXEL", pixels.verifyCreateNewPixelLabel().toUpperCase());
        Assert.assertEquals("RETARGETING PIXEL", pixels.verifyRetargetingPixel().toUpperCase());
        Assert.assertEquals("SMART PIXEL", pixels.verifySmartPixel().toUpperCase());
        Assert.assertEquals("CONVERSION PIXEL", pixels.verifyConversionPixel().toUpperCase());
    }

    @And("User selects the {string} type")
    public void userSelectsThePixelType(String pixelType) {
        pixels.selectPixelType(pixelType);
    }

    @And("User enters the pixel details as {string} {string}")
    public void userEntersPixelDetails(String pixelName, String advertiser) {
        newPixelName = pixelName + '_' + timestamp;
        retargetingPixel.enterPixelName(newPixelName);
        retargetingPixel.selectAdvertiser(advertiser);
    }

    @And("User enters the pixel details as {string} {string} {string} {string}")
    public void userEntersPixelDetails(String pixelName, String advertiser, String conversionPixelScope, String conversionPixelType) {
        newPixelName = pixelName + '_' + timestamp;
        conversionPixel.enterPixelName(newPixelName);
        conversionPixel.selectAdvertiser(advertiser);
        conversionPixel.selectConversionPixelScope(conversionPixelScope);
        conversionPixel.selectConversionPixelType(conversionPixelType);
    }

    @And("User selects the {string} and the associated campaign")
    public void userSelectsTheFromTheList(String advertiser) {
        smartPixel.selectAdvertiser(advertiser);
        smartPixel.selectAssociatedCampaign();
    }

    @And("User saves the pixel")
    public void userSavesThePixel() {
        pixels.savePixel();
    }

    @Then("Verify the pixel is saved successfully and displayed in the pixel list")
    public void verifyPixelIsSavedSuccessfullyAndDisplayedInPixelList() {
        assert pixels.verifySaveSuccess().contains("Success!");
        pixels.searchSavedPixel(newPixelName);
        Assert.assertEquals(newPixelName, pixels.verifyCreatedPixel(newPixelName));
    }

    @Then("Verify the smart pixel is saved successfully and displayed in the pixel list")
    public void verifySmartPixelIsSavedSuccessfullyAndDisplayedInPixelList() {
        assert pixels.verifySaveSuccess().contains("Success!");
        newPixelName = smartPixel.getPixelName();
        pixels.searchSavedPixel(newPixelName);
        Assert.assertEquals(newPixelName, pixels.verifyCreatedPixel(newPixelName));
    }

    @When("User selects {string} as rule type and selects the created pixel")
    public void userSelectsRuleTypeAndSelectsCreatedPixelAndSavesSettings(String ruleType) {
        itemCount = tacticSettings.selectRuleType(ruleType, newPixelName);
    }

    @Then("Verify the selected targeting rule {string}")
    public void verifyTheSelectedTargetingRule(String ruleType) {
        Assert.assertEquals(ruleType, tacticSettings.verifyRuleType());
        Assert.assertEquals(newPixelName, tacticSettings.verifyRuleOption());
    }

    @Then("Verify the selected targeting rule {string} for Smart list")
    public void verifyTheSelectedTargetingRuleForSmartList(String ruleType) {
        Assert.assertEquals(ruleType, tacticSettings.verifyRuleType());
        Assert.assertEquals(npiName, tacticSettings.verifyRuleOption());
    }

    @And("User enters the Smart NPI list details as {string} {string} and selects the created Smart Pixel")
    public void userEntersTheSmartNPIListDetailsAndSelectsTheCreatedSmartPixel(String npiListName, String advertiser) {
        npiName = npiListName + '_' + timestamp;
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.clickSmartPixel();
        npiSmartList.clickSmartPixelDropDown();
        npiSmartList.clickSmartPixelDropDownValue(newPixelName);
    }

    @Then("Verify the selected Smart Pixel")
    public void verifyTheSelectedSmartPixel() {
        Assert.assertEquals(newPixelName, npiSmartList.verifySelectedSmartPixel());
    }

    @And("User selects {string} as rule type and selects the created Smart list")
    public void userSelectsRuleTypeAndSelectsCreatedSmartList(String ruleType) {
        itemCount = tacticSettings.selectRuleType(ruleType, npiName);
    }

    @Then("Verify the count of rule options for the selected targeting rule on the Tactic Settings page")
    public void verifyTheCountOfSelectedRuleOptions() {
        String optionsCount = tacticSettings.fetchSelectedListCountFromTactic();
        int targetedOptionsCount = Integer.parseInt(optionsCount.replaceAll("[^0-9]", ""));
        Assert.assertEquals("Selected options count does not match", itemCount, targetedOptionsCount);
    }

    /*Roshani Sherkar
     * 25-08-2025
     * E2E Domain List creation and targeting it at tactic level*/
    @And("User enters {string} in the List Name field")
    public void userEntersInTheListNameField(String listName) {
        metricName = listName + "_" + CommonUtils.timeStampCalculation();
        sharedList.enterListName(metricName);
        npiName = metricName;
    }
    /*Ampoli Rajyalaxmi
     * 24/08/2025
     * Different types line item*/


    @When("User enters the line item details with different line types {string} {string} {string}, enables the line item and saves the changes")
    public void user_enters_the_line_item_details_with_different_line_types_enables_the_line_item_and_saves_the_changes(String lineItemName, String lineBudget, String lineItemType) {
        List<String> lineItemTypeList = CommonUtils.convertStringToList(lineItemType);
        for (int i = 0; i < lineItemTypeList.size(); i++) {
            String lineItem = lineItemTypeList.get(i);
            lineItemNameRandom = lineItemName + '_' + lineItem + '_' + CommonUtils.randomNumberGeneration();
            tacticDetails.createLineItem(lineItemNameRandom, lineItem, lineBudget);
            assert lineItemDetails.lineItemSuccess().contains("Success!");
            Assert.assertEquals(lineItemNameRandom, lineItemDetails.verifyLineItemPanelName());
            lineItemDetails.cancelTactic();
            if (i < lineItemTypeList.size() - 1) {
                lineItemDetails.selectNewLineItem();
            }
        }
    }

    /*Creative Bulk Upload */
    @Given("User clicks Bulk Upload button on Creative Library page")
    public void userClicksBulkUploadButtonOnCreativeLibraryPage() {
        bulkCreativeUpload.clickBulkUploadButton();
    }

    @Then("Verify Bulk Upload panel is displayed with following options - Creative Type and Advertiser Dropdown")
    public void verifyBulkUploadPanelIsDisplayedWithFollowingOptionsCreativeTypeAndAdvertiserDropdown() {
        Assert.assertTrue("Creative Type Options are not available", bulkCreativeUpload.checkCreativeTypeButtonsArePresent());
        Assert.assertTrue("Advertiser dropdown is not available", bulkCreativeUpload.checkAdvertiserDropdownIsShown());
    }

    @And("Verify the availability of below Creative Type options and the Default option is {string}")
    public void verifyTheAvailabilityOfBelowCreativeTypeOptionsAndTheDefaultOptionIsDisplay(String defaultOption, DataTable dataTable) {
        List<String> creativeTypeOptions = dataTable.asList(String.class);
        Assert.assertEquals("All creative type options are available.", bulkCreativeUpload.verifyCreativeTypeOptions(creativeTypeOptions));
        Assert.assertTrue("Expected 'Display' to be the default selected creative type.",
                bulkCreativeUpload.checkDefaultCreativeType(defaultOption));
    }

    @And("Verify the Advertiser dropdown is displaying all Advertisers mapped to the logged in account")
    public void verifyTheAdvertiserDropdownIsDisplayingAllAdvertisersMappedToTheLoggedInAccount() {
    }

    @When("User selects the {string} creative type")
    public void userSelectsTheCreativeType(String creativeType) {
        bulkCreativeUpload.selectAndClickCreativeType(creativeType);
    }

    @And("User selects the Approval status {string}")
    public void userSelectsTheApprovalStatus(String status) {
        bulkCreativeUpload.selectApprovalStatus(status);
    }

    @And("Verify an appropriate error message when user attempts to click the Preview or OK button without selecting a creative file")
    public void userAttemptsToClickThePreviewButtonWithoutSelectingACreativeFile() {
        bulkCreativeUpload.isRemoveFileIconAvailable();
        bulkCreativeUpload.clickPreviewButton();
        Assert.assertEquals("Atleast one creative should be selected", bulkCreativeUpload.fetchErrorAlert());
    }

    @Then("Verify the header message for {string} status")
    public void verifyForStatusTheHeaderMessageShouldBeDisplayed(String status) {
        switch (status) {
            case "Pending":
                Assert.assertEquals("CREATIVE IS NOT APPROVED YET", bulkCreativeUpload.fetchHeaderMessage());
                break;
            case "Approved":
                Assert.assertEquals("CREATIVE IS APPROVED", bulkCreativeUpload.fetchHeaderMessage());
                break;
            case "Denied":
                Assert.assertEquals("CREATIVE IS DENIED", bulkCreativeUpload.fetchHeaderMessage());
                break;
        }

    }

    @And("User uploads a valid file {string} for {string} creative")
    public void userUploadsAValidFileForTheCreative(String fileName, String creativeType) {
        bulkCreativeUpload.uploadDisplayCreativeTemplate(fileName);
    }

    @And("User uploads a valid file {string} for {string} creative and previews the creative details")
    public void userUploadsAValidFileAndPreviewsTheCreativeDetails(String fileName, String creativeType) {
        bulkCreativeUpload.uploadDisplayCreativeTemplate(fileName);
        bulkCreativeUpload.clickPreviewButton();
        metricName = creativeType + "_" + CommonUtils.timeStampCalculation();
        bulkCreativeUpload.updateCreativeName(metricName);
        nameList.clear();
        nameList.add(metricName);
    }

    @And("User saves the creative")
    public void userSavesTheCreative() {
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("BulkUpload created successfully.", bulkCreativeUpload.fetchSuccessAlert());
    }

    /*Dislay Creative Bulk Upload*/
    @When("The advertiser {string} is selected for {string} creative the following sections are visible")
    public void theAdvertiserIsSelectedForCreativeTheFollowingSectionsAreVisible(String advertiser, String creativeType, DataTable dataTable) {
        bulkCreativeUpload.selectAndClickCreativeType(creativeType);
        bulkCreativeUpload.selectAdvertiser(advertiser);
        List<String> displayCreativeSections = dataTable.asList(String.class);
        for (String section : displayCreativeSections) {
            boolean flag = bulkCreativeUpload.verifyDisplayCreativeSections(section);
            Assert.assertTrue(section + " section is not available", flag);
        }
    }

    @And("Verify under the {string} section the options {string} and {string} are available")
    public void underTheSectionTheOptionsAndAreAvailable(String sectionName, String option1, String option2) {
        Assert.assertTrue(option1 + " is not available under " + sectionName, bulkCreativeUpload.isDownloadTemplateButtonVisible());
        Assert.assertTrue(option2 + " is not available under " + sectionName, bulkCreativeUpload.isBrowseFileButtonVisible(option2));
    }

    @And("User is able to download a blank template using the {string} option")
    public void userIsAbleToDownloadABlankTemplateUsingTheOption(String arg0) {
        bulkCreativeUpload.clickBlankTemplateDownloadButton();
        Assert.assertTrue("Downloaded file is not available", bulkCreativeUpload.verifyDownloadedFile("DisplayBulkUploadTemplate", "xlsx"));
    }

    @And("Verify user is able to upload images {string} to get a template with URLs")
    public void userIsAbleToUploadImagesToGetATemplateWithURLsUsingTheOption(String imageFileName) {
        bulkCreativeUpload.uploadImageFile(imageFileName);
        bulkCreativeUpload.clickTemplateWithURLsLink();
        Assert.assertTrue("Downloaded file is not available", bulkCreativeUpload.verifyDownloadedFile("DisplayBulkUploadTemplate", "xlsx"));
    }

    @And("Verify under the {string} section the fields {string}, {string}, {string} are available")
    public void verifyUnderTheSectionTheFieldsAreAvailable(String sectionName, String field1, String field2, String field3) {
        Assert.assertTrue(field1 + " field is not available under " + sectionName, bulkCreativeUpload.isCampaignToRestrictVisible());
        Assert.assertTrue(field2 + " field is not available under " + sectionName, bulkCreativeUpload.isBrowseFileButtonVisible(field2));
        Assert.assertTrue(field2 + " field is not available under " + sectionName, bulkCreativeUpload.isApprovalStatusVisible());
    }

    @And("User is able to select a {string} from the Campaign Restrict dropdown")
    public void userIsAbleToSelectAFromTheCampaignRestrictDropdown(String campaignName) {
        bulkCreativeUpload.clickCampaignName(campaignName);
    }

    @And("User is able to browse and select a template {string} from the system")
    public void userIsAbleToBrowseAndSelectATemplateFromTheSystem(String fileName) {
        bulkCreativeUpload.uploadDisplayCreativeTemplate(fileName);
    }

    @And("Verify default value of the Approval Status field is {string}")
    public void defaultValueOfTheFieldIsForCreatives(String defaultStatus) {
        Assert.assertTrue("Expected 'Pending' to be the default selected status.", bulkCreativeUpload.checkDefaultApprovalStatus(defaultStatus));
    }

    @And("Verify under the {string} section the fields Add Third Party Tracking Pixel and Add DoubleVerify Pixel are available")
    public void verifyUnderTheSectionTheFieldsAddThirdPartyTrackingPixelTagAndAddDoubleVerifyPixelAreAvailable(String sectionName) {
        Assert.assertTrue("Add Third Party Tracking Pixel/Tag field is not available under " + sectionName, bulkCreativeUpload.isThirdPartyTrackingPixelAvailable());
        Assert.assertTrue("Add DoubleVerify Pixel is not available under " + sectionName, bulkCreativeUpload.isDoubleVerifyPixelAvailable());
    }

    @And("User is able to click a third-party tracking pixel and add details {string}")
    public void userIsAbleToSelectAThirdPartyTrackingPixelForACreative(String pixelDetails) {
        bulkCreativeUpload.addThirdPartyTrackingPixel(pixelDetails);
    }

    @And("User is able to add a DoubleVerify pixel")
    public void userIsAbleToAddADoubleVerifyPixelForACreative() {
        Assert.assertTrue("Unable to add DoubleVerify Pixel", bulkCreativeUpload.addDoubleVerifyPixel());
    }

    @And("User is able to delete third-party tracking pixel entries")
    public void userIsAbleToDeleteThirdPartyTrackingPixelEntries() {
        Assert.assertTrue("Unable to delete Third Party Tracking Pixel", bulkCreativeUpload.deleteThirdPartyTrackingPixel());
    }

    @And("An error message is displayed when a blank template {string} is uploaded")
    public void anErrorMessageIsDisplayedWhenABlankTemplateIsUploaded(String fileName) {
        bulkCreativeUpload.uploadBlankTemplate(fileName);
    }

    @And("User enters {string}, {string} mandatory fields data for Display creative")
    public void userEntersMandatoryFieldsDataForCreative(String advertiserDSA, String financer) {
        bulkCreativeUpload.enterAdvertiserDSA(advertiserDSA);
        bulkCreativeUpload.enterFinancer(financer);
    }

    @And("Verify Advertiser field should be mandatory")
    public void verifyAdvertiserFieldShouldBeMandatory() {
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("Select Advertiser", bulkCreativeUpload.fetchErrorAlert());
    }

    @And("Verify that the Landing Domain field is mandatory when all other required fields, including {string} are filled")
    public void verifyLandingDomainFieldShouldBeMandatoryByEnteringOtherMandatoryFields(String advertiser) {
        bulkCreativeUpload.selectAdvertiser(advertiser);
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("Landing Page Domain is required", bulkCreativeUpload.fetchErrorAlert());
    }

    @And("Verify that an appropriate error message is displayed when invalid data {string} is entered for the Landing Domain")
    public void verifyThatAnAppropriateErrorMessageIsDisplayedWhenInvalidDataIsEnteredForTheLandingDomain(String invalidLandingDomain) {
        bulkCreativeUpload.enterLandingPageDomain(invalidLandingDomain);
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("Landing Page Domain is not valid.", bulkCreativeUpload.fetchErrorAlert());
    }

    @And("Verify only valid Landing Domain {string} values should be permitted")
    public void verifyOnlyValidLandingDomainValuesShouldBePermitted(String validLandingDomain) {
        bulkCreativeUpload.enterLandingPageDomain(validLandingDomain);
        Assert.assertEquals("", bulkCreativeUpload.fetchErrorAlert());
    }

    @And("Verify default value of the File field should be {string}")
    public void verifyDefaultValueOfTheFileFieldShouldBe(String defaultValue) {
        Assert.assertEquals(defaultValue, bulkCreativeUpload.fetchFileDefaultValue());
    }

    @And("Verify default value of the AdChoices Icon should be {string}")
    public void verifyDefaultValueOfTheAdChoicesIconShouldBe(String defaultValue) {
        Assert.assertEquals(defaultValue, bulkCreativeUpload.fetchAdChoiceDefaultValue());
    }

    @And("Verify default value of the Notes Column field should be {string}")
    public void verifyDefaultValueOfTheNotesColumnFieldShouldBe(String defaultValue) {
        Assert.assertEquals(defaultValue, bulkCreativeUpload.fetchNotesColumnDefaultValue());
    }

    @And("Verify Rich Media checkbox should be present and selectable {string}")
    public void verifyRichMediaCheckboxShouldBePresentAndSelectable(String direction) {
        Assert.assertTrue("Rich Media Checkbox is not available", bulkCreativeUpload.isRichMediaCheckboxAvailable());
        Assert.assertTrue("Rich Media Checkbox is not clickable", bulkCreativeUpload.isRichMediaCheckboxClickable());
        bulkCreativeUpload.selectAndClickDirection(direction);
    }

    @And("Verify that the user is able to browse the computer, upload the following file types, and create creatives using details - {string}, {string}, {string}, {string}, {string}, {string}")
    public void verifyThatTheUserIsAbleToBrowseTheComputerUploadTheFollowingFileTypesAndCreateCreativesUsingDetails(String advertiser, String advertiserDSA, String financer, String landingDomain, String status, String creativeName, DataTable dataTable) {
        nameList.clear();
        Map<String, String> rawFilters = dataTable.asMap(String.class, String.class);
        Map<String, List<String>> filtersMap = CommonUtils.processDataTable(rawFilters);
        for (Map.Entry<String, List<String>> entry : filtersMap.entrySet()) {
            bulkCreativeUpload.selectAdvertiser(advertiser);
            bulkCreativeUpload.enterAdvertiserDSA(advertiserDSA);
            bulkCreativeUpload.enterFinancer(financer);
            bulkCreativeUpload.selectFileTypeAndUploadFile(entry.getKey(), entry.getValue());
            bulkCreativeUpload.enterLandingPageDomain(landingDomain);
            bulkCreativeUpload.selectApprovalStatus(status);
            nameList = bulkCreativeUpload.enterCreativeName(creativeName);
            if (bulkCreativeUpload.isWidthHeightVisibleAndBlank())
                bulkCreativeUpload.enterWidthHeight("800x250");
            bulkCreativeUpload.clickOKButton();
            Assert.assertEquals("BulkUpload created successfully.", bulkCreativeUpload.fetchSuccessAlert());
        }
    }

    @And("Verify user is able to type in {string} categories")
    public void verifyUserIsAbleToTypeInCategories(String iabCategory) {
        bulkCreativeUpload.typeIABCategory(iabCategory);
    }

    @And("Verify that the Clickthrough URL and Landing Domain fields are validated as mandatory when all other required fields are filled")
    public void verifyThatTheClickthroughURLAndLandingDomainFieldsAreValidatedAsMandatoryWhenAllOtherRequiredFieldsIncludingAreFilled() {
        bulkCreativeUpload.clickOKButton();
        List<String> expectedMessages = Arrays.asList(
                "Clickthrough URL is required",
                "Landing Page Domain is required");
        Assert.assertEquals(expectedMessages, bulkCreativeUpload.fetchInlineValidationMessage());
    }

    @And("Verify only valid Clickthrough URL {string} values should be permitted")
    public void verifyOnlyValidClickthroughURLValuesShouldBePermitted(String validURL) {
        bulkCreativeUpload.enterClickthroughURL(validURL);
        Assert.assertEquals("", bulkCreativeUpload.fetchErrorAlert());
    }

    @When("User creates and saves {string} Bulk upload creative using details {string} as Advertiser, {string}, {string} and below Creative attributes")
    public void userCreatesAndSavesBulkUploadCreativeUsingDetailsAsAdvertiserAsCreativeNameAndBelowCreativeAttributes(String creativeType, String advertiser, String advertiserDSA, String financer, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String type = row.get("CreativeType").trim();
            String attributes = row.get("CreativeAttributes").trim();
            String creativeName = creativeType + "_Creative_" + CommonUtils.timeStampCalculation();
            Map<String, String> attributeMap = Arrays.stream(attributes.split(","))
                    .map(String::trim)
                    .map(entry -> entry.split(":", 2))
                    .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            bulkCreativeUpload.clickBulkUploadButton();
            bulkCreativeUpload.selectAndClickCreativeType(creativeType);
            bulkCreativeUpload.enterCreativeAndDSADetails(advertiser, advertiserDSA, financer);
            bulkCreativeUpload.fillAttributes(type, attributeMap, creativeName);
            bulkCreativeUpload.clickOKButton();
            Assert.assertEquals("BulkUpload created successfully.", bulkCreativeUpload.fetchSuccessAlert());
            nameList.add(creativeName);
        }
    }
}