package stepdefinitions;

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

import java.sql.SQLException;
import java.util.*;

import static utils.CommonUtils.normalize;

public class LifeSteps {

    static String campaignNameRandom;
    static String lineItemNameRandom;
    static String tacticNameRandom;
    static String dealNameRandom;
    static String dealIDRandom;
    static String tacticPMP;
    static String url;
    static String username;
    static String password;
    static String npiName;
    static String npiNameEdited;
    static String templateNameRandom;
    static String dimensionName;
    static String metricName;
    List<Object> keyType = new ArrayList<>();
    List<Object> keyValues = new ArrayList<>();
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
    Constants constants = new Constants();
    String timestamp = CommonUtils.timeStampCalculation();
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
        lineItemNameRandom = lineItemName + '_' + UUID.randomUUID().toString().substring(0, 10);
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
        tacticNameRandom = tacticName + '_' + UUID.randomUUID().toString().substring(0, 10);
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
    public void userVerifyTheListIsDisplayedInTheLife()
    {
        //Assert.assertTrue(npiLists.availablePlatforms());
    }

    @When("User clicks on Create New List")
    public void user_clicks_on_create_new_list() {
        npiLists.clickCreateNewList();
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
            keyType.add( entry.getKey());
            keyValues.addAll( entry.getValue());
            tacticSettings.selectMultipleRuleTypes(entry.getKey(), entry.getValue());
        }
        tacticSettings.closeRuleTypePanel();
    }

    @Then("Verify the configured targeting rules")
    public void verify_the_configured_targeting_rules() {
        List<String> expectedNormalizedRuleTypes = normalize(Collections.singletonList(keyType.toString()));
        List<String> actualNormalizedRuleTypes = normalize(Collections.singletonList(tacticSettings.fetchRulesTypes().toString()));

        List<String> expectedNormalizedRuleOptions = normalize(Collections.singletonList(keyValues.toString()));
        List<String> actualNormalizedRuleOptions = normalize(Collections.singletonList(tacticSettings.fetchRuleOptions().toString()));

        Assert.assertEquals("Rule types mismatch", expectedNormalizedRuleTypes, actualNormalizedRuleTypes);
        Assert.assertEquals("Rule options mismatch",  expectedNormalizedRuleOptions, actualNormalizedRuleOptions);
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

    @And("User navigates to run report from mega menu of the life application")
    public void user_navigate_to_run_report() {
        navigation.clickSubMenu();
        navigation.clickRunReport();
    }

    @Then("User selects the report template created tactic and other fields for running the report")
    public void user_enter_input_for_running_report() {
        reportTemplates.enterDetailsToRunReport(templateNameRandom,tacticNameRandom);
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
        if(count == 0){
            message = "No campaigns matching filtering criteria found";
        }else{
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
        Assert.assertTrue("Campaigns with Finished Status are hidden",campaignDashboard.verifyHideFinishedCampaignList());
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
        Assert.assertEquals("All Deals ",pmp.verifyPMPDealsPanel());
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
            pmp.verifyPricingStrategyIsEditable(dealIDRandom, entry.getKey(), entry.getValue());
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
        Assert.assertTrue("Base and Max Bid Price fields are editable",pmp.verifyBaseAndMaxPriceIsEditable(baseBidPrice,maxBidPrice));
    }

    @When("User clicks Save button from Tactic Setting tab")
    public void userClicksSaveButtonFromTacticSettingTab() {
        pmp.saveTacticSettings();
    }

    @Then("Deals should get assigned to the Tactic")
    public void dealsShouldGetAssignedToTheTactic() {
        Assert.assertEquals("Success!", pmp.verifyTacticIsSaved());
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
        Assert.assertTrue("Targeting Button and Search Box are not displayed", targetingTemplate.verifyTargetingBtnAndSearchBox());
    }

    @And("Verify Targeting template section opens by clicking New Template button")
    public void verifyTargetingTemplateSectionByClickingNewTemplateButton() {
        Assert.assertTrue("All fields require to create targeting template are not available",targetingTemplate.clickAndVerifyTargetingTemplate());
    }

    @When("User creates Targeting template {string} for the line items {string} with channel {string} and Targeting Rules")
    public void userCreatesTargetingTemplateForTheLineItemsWithChannelAndTargetingRules(String templateName, String lineItems, String channel, DataTable ruleTypeAndOptions) {
        Map<String, String> rawMap = ruleTypeAndOptions.asMap(String.class, String.class);
        Map<String, List<String>> rulesMap = CommonUtils.processDataTable(rawMap);
        List<String> lineItemsList = Arrays.stream(lineItems.split(",")).toList();
        List<String> channelList = Arrays.stream(channel.split(",")).toList();
        List<String> templateNameList  = targetingTemplate.createAndSaveTargetingTemplate(templateName, lineItemsList, channelList, rulesMap);
        flag = targetingTemplate.searchTargetingTemplate(templateNameList);
    }

    @Then("User searches and verifies the already created targeting template using the search option")
    public void userSearchesTheAlreadyCreatedTargetingTemplateUsingTheSearchOption() {
        Assert.assertTrue("Targeting template is not found in the search results", flag);
    }

    @And("User tries to save the targeting template with targeting rule {string} and without specifying a template name")
    public void userTriesToSaveTheTargetingTemplateWithTargetingRuleAndWithoutSpecifyingATemplateName(String targetingRule) {
        Assert.assertEquals("Template Name is required", targetingTemplate.verifyErrorMessageForTemplateName(targetingRule));
    }

    @And("User tries to save the targeting template with template name {string} without specifying any targeting")
    public void userTriesToSaveTheTargetingTemplateWithTemplateNameWithoutSpecifyingAnyTargeting(String templateName) {
        Assert.assertEquals("Please select atleast one targeting",targetingTemplate.verifyErrorMessageForTargetingRules(templateName));
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
        Assert.assertTrue("Unable to delete existing targeting template", targetingTemplate.clickAndVerifyTargetTemplateDeletion(templateName));
    }
}