package stepdefinitions;

import com.microsoft.playwright.APIResponse;
import com.opencsv.exceptions.CsvValidationException;
import factory.DriverFactory;
import hooks.Hooks;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.Navigation;
import pages.admin.Accounts;
import pages.life.*;
import utils.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    static String pixelNameEdited;
    static String userType;
    List<Object> keyType = new ArrayList<>();
    List<Object> keyValues = new ArrayList<>();
    Map<String, Map<String, String>> keyValueMap = new LinkedHashMap<>();
    List<String> nameList = new ArrayList<>();
    List<String> capturedDetails = new ArrayList<>();
    List<String> itemList = new ArrayList<>();
    Navigation navigation = new Navigation(DriverFactory.getPage());
    Campaigns campaigns = new Campaigns(DriverFactory.getPage());
    LineItemDetails lineItemDetails = new LineItemDetails(DriverFactory.getPage());
    TacticDetails tacticDetails = new TacticDetails(DriverFactory.getPage());
    TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());
    TacticCreatives tacticCreatives = new TacticCreatives(DriverFactory.getPage());
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
    RunReportPanel runReportPanel = new RunReportPanel(DriverFactory.getPage());
    Accounts accounts = new Accounts(DriverFactory.getPage());
    ScheduleReport scheduleReport = new ScheduleReport(DriverFactory.getPage());
    LineItemFlights lineItemFlights = new LineItemFlights(DriverFactory.getPage());
    CampaignSettings campaignSettings = new CampaignSettings(DriverFactory.getPage());
    Constants constants = new Constants();
    int itemCount = 0;
    int totalListCount = 0;
    int flightStartDate = 0;
    int flightEndDate = 0;
    APIResponse response;
    boolean flag = false;
    String customFieldName;
    String uiCustomFieldName;
    BigDecimal campaignBaseBid;
    BigDecimal campaignMaxBid;
    Path targetFilePath;
    private static final Logger logger = LoggerFactory.getLogger(LifeSteps.class);

    @Given("This scenario will be executed in the {string} environment as a {string}")
    public void set_environment(String environment, String user) {
        logger.info("Setting up environment: {} for user type: {}", environment, user);
        userType = user;
        if (environment.equals("Demo")) {
            url = ConfigReader.getProperty("demoURL");
            // If the feature indicates an external user, prefer external demo credentials if available, otherwise fall back
            if (user != null && user.toLowerCase().contains("external") && ConfigReader.getProperty("demoExternalUser") != null) {
                username = ConfigReader.getProperty("demoExternalUser");
                password = ConfigReader.getProperty("demoExternalPassword");
            } else {
                username = ConfigReader.getProperty("demoUser");
                password = ConfigReader.getProperty("demoPassword");
            }
        } else if (environment.equals("Pre-release")) {
            url = ConfigReader.getProperty("preReleaseURL");
            // If the test is for an external user, use the pre-release external credentials
            if (user != null && user.toLowerCase().contains("external")) {
                username = ConfigReader.getProperty("preReleaseExternalUser");
                password = ConfigReader.getProperty("preReleaseExternalPassword");
            } else {
                username = ConfigReader.getProperty("preReleaseUser");
                password = ConfigReader.getProperty("preReleasePassword");
            }
        }
    }

    @And("{string} application is logged in successfully with Account {string}")
    public void life_application_is_logged_in_as(String application, String account) {
        navigation.navigateToUrl(url);
        navigation.enterUsername(username);
        navigation.enterPassword(password);
        navigation.clickLogin();
        switch (application) {
            case "Life":
                navigation.navigateToLife();
                break;
            case "HCP":
                navigation.navigateToHCP();
                break;
            case "Studio":
                if (userType.equals("User")) {
                    navigation.navigateToLife();
                }
                navigation.navigateToStudio();
                break;
        }
        navigation.selectAccount(account);
    }

    @Given("User clicks on Create Campaign")
    public void user_clicks_on_create_campaign() {
        campaigns.createCampaign();
        Assert.assertEquals("Create New Campaign", campaigns.verifyCampaignText());
    }

    @When("User enters the campaign details as {string} {string} {string} {string} and saves the campaign")
    public void user_enters_the_campaign_details_and_saves_the_campaign(String advertiser, String campaign_name, String campaign_type, String budget) {
        campaignNameRandom = campaign_name + '_' + CommonUtils.timeStampCalculation();
        campaigns.selectAdvertiser(advertiser);
        campaigns.enterCampaignName(campaignNameRandom);
        campaigns.setCampaignType(campaign_type);
        campaigns.enterBudget(budget);
        campaigns.saveCampaign();
    }

    @Then("Verify campaign details are saved and user is navigated to the line item page")
    public void verify_campaign_details_are_saved_and_user_is_navigated_to_line_item_page() {
        Assert.assertEquals("Campaign " + campaignNameRandom + " created.", campaigns.campaignSuccess());
        Assert.assertEquals("New Line Item", lineItemDetails.verifyLineItemText());
    }

    @Then("User navigates to campaign")
    public void user_navigates_to_campaign() {
        campaigns.selectCampaign();
    }

    @When("User enters the line item details as {string} {string}, enables the line item and saves the changes")
    public void user_enters_the_line_item_details_enables_the_line_item_and_saves_the_changes(String lineItemName, String lineBudget) {
        lineItemNameRandom = lineItemName + '_' + CommonUtils.timeStampCalculation();
        lineItemDetails.enterLineItemName(lineItemNameRandom);
        navigation.clickOnIcon("Add Flight");
        lineItemDetails.enterLineItemBudget(lineBudget);
        lineItemDetails.enableLineItem();
        lineItemDetails.saveLineItem();
    }

    @Then("Verify line item details are saved and user is navigated to the tactic page")
    public void verify_line_item_details_are_saved_and_user_is_navigated_to_tactic_page() {
        Assert.assertEquals("Lineitem " + lineItemNameRandom + " created.", lineItemDetails.lineItemSuccess());
        Assert.assertEquals("New Tactic", tacticDetails.verifyTacticDetailsText());
    }

    @Then("User creates below tactics under same line item and verifies it")
    public void user_creates_below_tactics_under_same_line_item_and_verifies_it(DataTable dataTable) {
        List<Map<String, String>> tactics = dataTable.asMaps(String.class, String.class);
        List<String> expectedTactic = new ArrayList<>();
        for (Map<String, String> tacticData : tactics) {
            String tacticName = tacticData.get("Tactic Name");
            metricName = tacticName;
            String channel = tacticData.get("Channel");
            String ruleType = tacticData.get("RuleType");
            expectedTactic.add(tacticName);
            // Enter tactic name
            tacticDetails.enterTacticName(tacticName);
            tacticDetails.saveTacticDetails();

            // Select channel and add targeting rules
            tacticSettings.selectChannel(channel);
            tacticDetails.clickTargetingRuleIcon();
            tacticSettings.addTargetingRules(ruleType);
            tacticSettings.saveTacticSettings();
            tacticDetails.clickNewTactic();
        }
        List<String> actualTactics = tacticDetails.getAllTactics();
        Assert.assertEquals(new HashSet<>(expectedTactic), new HashSet<>(actualTactics));
        List<String> expectedTarget = tacticSettings.getExpectedTargetRules();
        List<String> actualTarget = tacticSettings.getActualTargetRules();
        Assert.assertEquals(expectedTarget, actualTarget);
    }

    @When("User clicks the comments icon in the tactic {string} section and add {string}")
    public void userClicksTheCommentsIconInTheTacticSection(String entryPoint, String comment) {
        tacticDetails.addComment(entryPoint, comment);
    }

    @When("User validates the comment added in {string} is {string} then clear it")
    public void user_validates_the_comment_added_then_clear_it(String entryPoint, String expectedComment) {
        String actualComment = tacticDetails.validateComment(entryPoint);
        Assert.assertEquals(expectedComment, actualComment);
    }

    @Then("User adds frequency cap with details {string} {string} {string} {string}")
    public void user_adds_frequency_cap_with_details(String level, String FREQ_VALUE, String TIMES_PER, String SCOPE) {
        campaigns.addFrequencyCap(level, FREQ_VALUE, TIMES_PER, SCOPE);
    }

    @Then("User clicks on details tab")
    public void user_clicks_on_details_tab() {
        campaigns.clickDetailsTab();
    }

    @Then("User verifies if Frequency Cap is in disabled state by default")
    public void userVerifiedFrequencyCapIsInDisabledStatesByDefault() {
        boolean fc_checkbox_state = campaigns.isFrequencyCapDisabled();
        Assert.assertFalse(fc_checkbox_state);
    }

    @Then("User navigates to LineItem")
    public void userNavigatesToLineItem() {
        campaigns.clickLineItem();
    }

    @Then("User verifies if frequency cap is saved with details {string} {string} {string} {string}")
    public void userVerifiesIfFrequencyCapIsSavedWithDetailsOnCampaignLevel(String freqValue, String timesPer, String scope, String level) {
        String actualFrequencyCapText = campaigns.getSavedFrequencyCap(level);
        String expectedFrequencyCapText = String.format("%s x %s x %s %s", freqValue, timesPer, scope, level).toUpperCase();
        if(timesPer.contains("hour")){
            expectedFrequencyCapText = String.format("%s x Time Per %s hour %s %s", freqValue, freqValue, scope, level).toUpperCase();
        }
        Assert.assertEquals(expectedFrequencyCapText, actualFrequencyCapText);
    }

    @Then("User navigates to Tactic and clicks on settings tab")
    public void user_navigates_to_tactic_and_clicks_on_settings_tab() {
        tacticDetails.clickFirstTacticTab();
        tacticDetails.clickSettingsTab();
    }

    @Then("Verify that frequency cap is saved in tactic")
    public void verify_that_frequency_cap_is_saved_in_tactic() {
        boolean frequencyCapState = campaigns.getFrequencyCapState();
        Assert.assertTrue(frequencyCapState);
    }

    @Then("Verify that below tabs gets enabled only after saving tactics")
    public void verify_that_below_tabs_gets_enabled_only_after_saving_tactics(DataTable dataTable) {
        tacticDetails.verifyDetailsTab();
        List<String> tacticTabNames = new ArrayList<>(dataTable.asList(String.class));
        String detailsTab = tacticTabNames.remove(tacticTabNames.size() - 1);
        List<String> disabledTabs = tacticDetails.newTacticTabs();
        Assert.assertEquals(tacticTabNames, disabledTabs);
        tacticDetails.clickFirstTacticTab();
        List<String> enabledTabs = tacticDetails.allTacticsUnderLI();
        tacticTabNames.add(detailsTab);
        Assert.assertEquals(new HashSet<>(tacticTabNames), new HashSet<>(enabledTabs));

    }

    @And("Verify the status of first tactic under line item is {string}")
    public void verify_the_status_of_first_tactic_under_line_item_is(String ExpectedStatus) {
        tacticDetails.clickFirstTacticTab();
        String actualStatus = tacticDetails.verifyTacticState();
        Assert.assertEquals(ExpectedStatus, actualStatus);
    }

    @Then("User creates new custom field {string} and verifies the same")
    public void user_creates_new_custom_field_and_verifies_the_same(String customField) {
        String customFieldName = customField + "_" + CommonUtils.randomFourDigitNumber();
        this.customFieldName = customFieldName;
        tacticDetails.addCustomField(customFieldName);
        String raw = tacticDetails.verifyCustomField(customFieldName);
        String actualName = raw.split("\\R")[0];// To remove unwanted space and text
        Assert.assertEquals(customFieldName, actualName);
        this.uiCustomFieldName = actualName;
    }


    @And("User verifies if new custom field is visible and empty in new tactic")
    public void user_verifies_if_new_custom_field_is_visible_and_empty_in_new_tactic() {
        tacticDetails.clickNewTactic();
        Assert.assertEquals(customFieldName, uiCustomFieldName);
        Assert.assertTrue(tacticDetails.customFieldValue(customFieldName).inputValue().isEmpty());
        tacticDetails.clickLastTactic();
        Assert.assertEquals(customFieldName, uiCustomFieldName);
        Assert.assertFalse(tacticDetails.customFieldValue(customFieldName).inputValue().isEmpty());
    }

    @Then("User deletes the custom field and verify its removed from new tactic")
    public void user_deletes_the_custom_field_and_verify_its_removed_from_new_tactic() {
        tacticDetails.deleteCustomField(customFieldName);
    }

    @When("User enters the tactic details as {string} and saves the tactic")
    public void user_enters_the_tactic_details_and_saves_the_tactic(String tacticName) {
        tacticNameRandom = tacticName + '_' + CommonUtils.timeStampCalculation();
        tacticDetails.enterTacticName(tacticNameRandom);
        tacticDetails.saveTacticDetails();
    }

    @Then("Verify tactic details are saved and user is navigated to the settings tab")
    public void verify_tactic_details_are_saved_and_user_is_navigated_to_settings_tab() {
        Assert.assertEquals("Tactic " + tacticNameRandom + " updated.", tacticDetails.tacticDetailsSuccess());
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

    @Then("User clicks on first tactic and goes to details tab")
    public void user_clicks_on_first_tactic_and_goes_to_details_tab() {
        tacticDetails.clickFirstTacticTab();
        tacticDetails.clickDetailsTab();
    }

    @Then("User clears the custom field text")
    public void user_clears_the_custom_field_text() {
        tacticDetails.clearCustomFieldText(customFieldName);
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
        campaignDashboard.resetFiltersIfApplied();
        Assert.assertEquals("Running", tacticCreatives.verifyCampaignRunning());
    }

    @Then("Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name")
    public void verify_the_newly_created_campaign_details_in_the_campaign_list() {
        campaigns.navigateToCampaignDashboard();
        campaignDashboard.searchCreatedCampaign(campaignNameRandom);
        Assert.assertEquals(campaignNameRandom, campaignDashboard.verifyCreatedCampaign(campaignNameRandom));
        campaignDashboard.expandCreatedLineItem();
        Assert.assertEquals(lineItemNameRandom, campaignDashboard.verifyCreatedLineItem(lineItemNameRandom));
        campaignDashboard.expandCreatedLineItem();
        Assert.assertEquals(tacticNameRandom, campaignDashboard.verifyCreatedTactic());
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
        npiLists.searchNPILists(StudioSteps.workspaceName);
    }

    @And("User clicks on the published workspace")
    public void userClicksOnThePublished() {
        npiLists.selectPublishedList(StudioSteps.workspaceName);
    }

    @Then("User Verify the list is displayed in the Life")
    public void userVerifyTheListIsDisplayedInTheLife() {
        Assert.assertTrue("NPI list is not available in LIFE", npiLists.availablePlatforms());
    }

    @And("Verify the list should be available for LIFE platform by default")
    public void verifyTheListShouldBeAvailableForLIFEPlatformByDefault() {
        Assert.assertTrue("LIFE (only) is not selected as default", npiLists.checkOnlyLIFEIsSelected());
    }

    @When("User clicks on Create New List")
    public void user_clicks_on_create_new_list() {
        npiLists.clickCreateNewList();
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
        npiName = npiListName + '_' + CommonUtils.timeStampCalculation();
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
        navigation.clickMenuAngle();
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
        templateNameRandom = templateName + '_' + CommonUtils.timeStampCalculation();
        reportTemplates.enterTemplateName(templateNameRandom);
        reportTemplates.selectDimension(dimension);
        reportTemplates.clickMetricsTab();
        reportTemplates.selectMetric(metric);
    }

    @When("User enters the template details for end to end as {string} {string} {string}")
    public void user_enters_the_template_for_end_to_end_details_as(String templateName, String dimension, String metric) {
        templateNameRandom = templateName + '_' + CommonUtils.timeStampCalculation();
        reportTemplates.enterTemplateName(templateNameRandom);
        String[] dimensionList = dimension.split(",");

        for (String dimensionValue : dimensionList) {
            dimensionValue = dimensionValue.trim();
            reportTemplates.selectDimensione2e(dimensionValue);
        }

        reportTemplates.clickMetricsTab();
        String[] metricsList = metric.split(",");

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
            boolean matchFound = actualNormalizedRuleOptions.stream().anyMatch(actual -> actual.equalsIgnoreCase(expectedOption));
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

    @Then("User selects Smart List")
    public void user_selects_smart_list() {
        npiLists.clickSmartList();
    }

    @Then("User enters the NPI list details as {string} {string}")
    public void user_enters_the_npi_list_details_as(String listName, String advertiser) {
        npiName = listName + '_' + CommonUtils.timeStampCalculation();
        npiSmartList.enterListName(npiName);
        npiSmartList.selectAdvertiser(advertiser);
    }

    @When("User clicks on {string} and enters the drug details {string}")
    public void user_clicks_on_prescribed_drug_and_enters_the_drug_details(String smartListType, String drugName) {
        npiSmartList.selectSmartNPIListType(smartListType);
        npiSmartList.selectDrug(drugName);
    }

    @Then("Verify drug details are added")
    public void verify_drug_details_are_added() {
        Assert.assertEquals("Glynase", npiSmartList.fetchDrugName());
    }

    @When("User makes list available in LIFE, HCP365 and saves the list")
    public void user_makes_list_available_in_life_hcp365_and_saves_the_list() {
        npiSmartList.selectProduct();
    }

    @Then("User navigates to Campaign Dashboard")
    public void user_navigates_to_campaign_dashboard() {
        navigation.clickSubMenu();
        navigation.clickCampaigns();
        Assert.assertEquals("Life", campaigns.campaignDashboard());
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
        navigation.clickMenuAngle();
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
        navigation.clickMenuAngle();
        navigation.clickGeneratedReport();
    }

    @Then("User downloads the report and verify the data in downloaded report")
    public void user_download_the_report_from_generated_report_page_and_verify_the_data() throws Exception {
        String filePath = reportTemplates.downloadGeneratedReport(templateNameRandom);
        navigation.clickSubMenu();
        navigation.clickMenuAngle();
        navigation.clickReportTemplate();
        Assert.assertTrue("Report headers match expected values!", reportTemplates.verifyColumnsOfReport(templateNameRandom, filePath));
    }

    /*Roshani Sherkar - 18-06-2025
     * Campaign Dashbaord Features Start*/
    @And("Verify Campaign Dashboard is displayed with title {string}")
    public void verifyCampaignDashboardIsDisplayedWithTitle(String title) {
        Assert.assertEquals(title, campaignDashboard.isCampaignDashboardVisibleWithTitle(title));
    }

    @When("User enters {string} and click Search button")
    public void userEntersAndClickSearchButton(String campaignID) {
        campaignDashboard.searchCreatedCampaign(campaignID);
        campaignDashboard.expandCreatedLineItem();
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
            Assert.assertEquals("Notes saved successfully.", successAlertText);
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

    @And("User navigates to campaign, line item and tactic using {string} and verifies that the comments are displayed in the respective tile comment boxes")
    public void userNavigatesToCampaignLineItemAndTacticToVerifyTheCommentsAreDisplayedInRespectiveCommentSections(String campaignId) {
        List<String> actualComments = new ArrayList<>();
        campaignDashboard.navigateToCampaign(campaignId);
        actualComments.add(campaigns.fetchCommentFromCampaignLineItemTacticPanel());
        campaigns.clickLineItemTile();
        actualComments.add(campaigns.fetchCommentFromCampaignLineItemTacticPanel());
        campaigns.clickTacticTile();
        actualComments.add(campaigns.fetchCommentFromCampaignLineItemTacticPanel());
        Assert.assertEquals(keyValues, actualComments);
    }

    @And("User verifies the comments in the campaign, line item, and tactic dashboard's comment boxes")
    public void userVerifiesTheCommentsInTheCampaignLineItemAndTacticDashboardSCommentBoxes() {
        List<String> actualComments = new ArrayList<>();
        campaigns.clickCampaignTile();
        actualComments.add(campaigns.fetchCommentFromCampaignLineItemTacticDashboard());
        campaigns.clickLineItemTile();
        actualComments.add(campaigns.fetchCommentFromCampaignLineItemTacticDashboard());
        campaigns.clickTacticTile();
        actualComments.add(campaigns.fetchCommentFromCampaignLineItemTacticDashboard());
        Assert.assertEquals(keyValues, actualComments);
    }

    @When("User toggles the Enabled button for Line Items and Tactics")
    public void userTogglesEnabledButtonForLineItemsAndTacticFromDashboard() {
        campaignDashboard.clickLineAndTacticToggleButton();
    }

    @Then("Verify that Line Items and Tactics reflect the correct enabled or disabled state")
    public void verifyLineItemsAndTacticsAreEnabledDisabledAccordingly() {
        Assert.assertTrue("Buttons are clickable and functional", campaignDashboard.verifyLineTacticToggleStatus());
    }

    @And("User fetches the Line Items and Tactics enabled-disabled status from Campaign Dashboard using {string} and verifies the same status in the respective Line Item and Tactic pages")
    public void userFetchesTheLineItemsAndTacticsEnabledDisabledStatusFromCampaignDashboardAndVerifiesTheSameStatusInTheRespectiveLineItemAndTacticPages(String campaignID) {
        List<String> expectedStatus = campaignDashboard.fetchLineAndTacticToggleStatus();
        List<String> actualStatus = new ArrayList<>();
        campaignDashboard.navigateToCampaign(campaignID);
        campaigns.clickLineItemTile();
        actualStatus.add(campaigns.fetchToggleStatus());
        campaigns.clickTacticTile();
        actualStatus.add(campaigns.fetchToggleStatus());
        Assert.assertEquals(expectedStatus, actualStatus);
    }

    @When("User clicks Campaign {string}, Line Item and Tactic and verify navigation to respective pages")
    public void userClicksCampaignLineItemAndTacticOneByOne(String campaignID) {
        campaignDashboard.navigateToCampaign(campaignID);
        Assert.assertTrue("Navigation to Campaign details page is not successful", campaignDashboard.isCampaignPageDisplayed());
        campaigns.navigateToCampaignDashboard();
        campaignDashboard.searchCreatedCampaign(campaignID);
        campaignDashboard.expandCreatedLineItem();

        campaignDashboard.navigateToLineItemDetails();
        Assert.assertTrue("Navigation to Line Item details page is not successful", campaignDashboard.isLineItemPageDisplayed());
        campaigns.navigateToCampaignDashboard();
        campaignDashboard.searchCreatedCampaign(campaignID);
        campaignDashboard.expandCreatedLineItem();

        campaignDashboard.navigateToTacticDetails();
        Assert.assertTrue("Navigation to Tactic details page is not successful", campaignDashboard.isTacticPageDisplayed());
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
        List<String> columnName = campaignDashboard.fetchDashboardColumns();
        Assert.assertEquals(keyValues.stream().map(o -> ((String) o).toLowerCase()).collect(Collectors.toSet()), columnName.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }

    @And("User clicks HideAll option from Menu and verifies Dashboard columns are hidden accordingly")
    public void userClicksHideAllAndShowAllOptionsFromMenu() {
        Assert.assertTrue("Columns are not hidden successfully", campaignDashboard.clickHideAllOption());
    }

    @And("User clicks ShowAll option from Menu and verifies Dashboard columns are shown accordingly")
    public void userClicksShowAllOptionFromMenuAndVerifiesDashboardColumnsAreShownAccordingly() {
        Assert.assertTrue("Columns are not shown successfully", campaignDashboard.clickShowAllOption());
    }

    @When("Navigate to any Dashboard column, select the filter and apply")
    public void navigateToAnyDashboardColumnSelectTheFilterAndApply(DataTable filterNames) {
        Map<String, String> rawMap = filterNames.asMap(String.class, String.class);
        Map<String, List<String>> filterMap = CommonUtils.processDataTable(rawMap);
        keyValues.clear();
        keyType.clear();
        for (Map.Entry<String, List<String>> entry : filterMap.entrySet()) {
            keyType.add(entry.getKey());
            keyValues.addAll(entry.getValue());
            campaignDashboard.applyFilterOnSelectedColumns(entry.getKey(), entry.getValue());
        }
    }

    @And("Verify the filter list displays only the selected filter values")
    public void verifyTheFilterListDisplaysOnlyTheSelectedFilterValues() {
        List<String> selectedFilterLabels = campaignDashboard.fetchSelectedFilterLabels();
        List<String> cleanedActual = selectedFilterLabels.stream().map(s -> s.replaceAll(":$", "")).toList();
        Assert.assertEquals(keyType, cleanedActual);
        List<String> normalizedExpected = keyValues.stream()
                .map(obj -> obj.toString().toLowerCase().trim())
                .toList();
        List<String> normalizedActual = campaignDashboard.fetchSelectedFilterValues().stream()
                .map(s -> s.trim().toLowerCase())
                .toList();
        Assert.assertEquals(normalizedExpected, normalizedActual);
    }

    @Then("Verify the Campaign Dashboard data should filter as per the selected filter values")
    public void verifyTheDataShouldFilterAsPerTheSelectedFilterValues() {
        for (Object o : keyType) {
            Assert.assertTrue("Campaign Dashboard data is not filtered as per the selected filter values", campaignDashboard.isCampaignDataFilteredAccordingToSelectedFilters(o.toString(), keyValues));
        }
    }

    @And("Filter icon should display in the column header to which filter is applied and a red bullet {string} on the filter icon present next to global search")
    public void filterIconShouldDisplayInTheColumnHeaderToWhichFilterIsAppliedAndARedBulletOnTheFilterIconPresentNextToGlobalSearch(String iconColor) {
        String filterIconColor = campaignDashboard.verifyFilterIcon();
        Assert.assertEquals(iconColor, filterIconColor);
    }

    @And("User removes all the filters applied on the Dashboard and verifies the data is reset to default state")
    public void userRemovesAllTheFiltersAppliedOnTheDashboardAndVerifiesTheDataIsResetToDefaultState() {
        String campaignCountBeforeFilterRemoval = campaignDashboard.fetchCampaignDataCountFromPagination();
        campaignDashboard.clickResetAllFilters();
        String campaignCountAfterFilterRemoval = campaignDashboard.fetchCampaignDataCountFromPagination();
        Assert.assertNotEquals(campaignCountBeforeFilterRemoval, campaignCountAfterFilterRemoval);
    }

    @And("User verifies that the campaigns displayed on the Dashboard include all past and current flights")
    public void userVerifiesThatTheCampaignsDisplayedOnTheDashboardIncludeAllPastAndCurrentFlights() {
        List<LocalDate> dates = campaignDashboard.fetchFlightStartAndEndDate();
        LocalDate monthEnd = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        boolean allDatesValid = dates.stream().noneMatch(date -> date.isAfter(monthEnd));
        Assert.assertTrue("Campaigns include all past and current flights", allDatesValid);
    }

    @When("User clicks Favorite Only checkbox")
    public void userClicksFavoriteOnlyCheckbox() {
        campaignDashboard.clickFavoriteOnlyCheckbox();
    }

    @Then("Verify the dashboard results should show only campaigns which are marked as favorite")
    public void verifyTheDashboardResultsShouldShowOnlyCampaignsWhichAreMarkedAsFavorite() {
        Assert.assertTrue("Dashboard data has campaign details marked as favorite", campaignDashboard.isFavoriteCampaignShown());
    }

    @And("User unchecks Favorite Only checkbox")
    public void userUnchecksFavoriteOnlyCheckbox() {
        campaignDashboard.unselectFavoriteCheckboxIfSelected();
    }

    @And("Verify the dashboard results should show campaigns which are marked as favorite and nonfavorite")
    public void verifyTheDashboardResultsShouldShowCampaignsWhichAreMarkedAsFavoriteAndNonfavorite() {
        Assert.assertTrue("Dashboard data has campaign details marked as favorite", campaignDashboard.isFavoriteNonFavoriteCampaignAvailable());
    }

    @When("User clicks Hide Finished checkbox")
    public void userClicksHideFinishedCheckbox() {
        campaignDashboard.clickHideFinishedCheckbox();
    }

    @Then("Verify the dashboard data should not reflect campaigns with Finished status")
    public void verifyTheDashboardDataShouldNotReflectCampaignsWithFinishedStatus() {
        Assert.assertTrue("Campaigns with Finished Status are hidden", campaignDashboard.isFinishedCampaignListHidden());
    }

    @And("User unchecks Hide Finished checkbox")
    public void userUnchecksHideFinishedCheckbox() {
        campaignDashboard.unselectHideFinishedCheckboxIfSelected();
    }

    @And("Verify the dashboard data should reflect campaigns with Finished status")
    public void verifyTheDashboardDataShouldReflectCampaignsWithFinishedStatus() {
        Assert.assertTrue("Campaigns with Finished Status are hidden", campaignDashboard.isFinishedCampaignListShownWithOtherStatus());
    }

    @And("User clicks {string} filter")
    public void userClicksFilter(String filterType) {
        campaignDashboard.clickFilterTypeButton(filterType);
    }

    @Then("Verify only Current Month's Flights should render on the Dashboard")
    public void verifyOnlyActiveFlightsShouldRenderOnTheDashboard() {
        Assert.assertTrue("Inactive flights are not present", campaignDashboard.ifInactiveFlightPresent());
        List<LocalDate> dates = campaignDashboard.fetchFlightStartAndEndDate();
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDate monthEnd = now.withDayOfMonth(now.lengthOfMonth());
        boolean allDatesInCurrentMonth = dates.stream().noneMatch(date -> date.isBefore(monthStart) || date.isAfter(monthEnd));
        Assert.assertTrue("Only Active flights (current month's flights) should be visible on the Dashboard", allDatesInCurrentMonth);
    }

    @Then("Verify only Today's Flights should render on the Dashboard")
    public void verifyOnlyTodaySFlightsShouldRenderOnTheDashboard() {
        List<LocalDate> dates = campaignDashboard.fetchFlightStartAndEndDate();
        LocalDate today = LocalDate.now();
        boolean allDatesToday = dates.stream().allMatch(date -> date.isEqual(today));
        Assert.assertTrue("Only today's flights should be visible on the Dashboard", allDatesToday);
    }

    @And("User enters the custom date range from {string} to {string} and applies the filter")
    public void userEntersTheCustomDateRangeFromToAndAppliesTheFilter(String startDate, String endDate) {
        campaignDashboard.enterCustomDateRange(startDate, endDate);
    }

    @And("Verify only Custom date range Flights from {string} to {string} should render on the Dashboard if available")
    public void verifyOnlyCustomDateRangeFlightsShouldRenderOnTheDashboardIfAvailable(String startDate, String endDate) {
        boolean flag = campaignDashboard.isCampaignDataAvailableInCustomDateRange();
        if(flag)
            Assert.assertTrue("No campaign data is available", true);
        else{
            List<LocalDate> dates = campaignDashboard.fetchFlightStartAndEndDate();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate start = LocalDate.parse(startDate, inputFormatter);
            LocalDate end = LocalDate.parse(endDate, inputFormatter);
            boolean allDatesInCurrentMonth = dates.stream().noneMatch(date -> date.isBefore(start) || date.isAfter(end));
            Assert.assertTrue("Only flights within the selected date range should be visible on the Dashboard", allDatesInCurrentMonth);
        }
    }

    @When("User clicks the Settings icon and selects the following group by options and verify dashboard data is grouped accordingly")
    public void userClicksTheSettingsIconAndSelectsTheFollowingGroupByOptions(DataTable dataTable) {
        List<String> groupByOption = dataTable.asList(String.class);
        for(String option : groupByOption){
            campaignDashboard.clickSettingIcon();
            Assert.assertTrue("Dashboard data is not grouped by the selected options - " + option, campaignDashboard.clickGroupByOptionsAndCheckDashboardData(option));
        }
    }

    @When("User hover on the image icon for creative in red color and check whether creative is assigned to the campaign")
    public void userHoverOnTheImageIconForCreativeInRedColor() {
        String creativeStatus = campaignDashboard.fetchCreativeToolTipText();
        Assert.assertTrue("No status has been displayed", creativeStatus.contains("No creative assigned") || creativeStatus.contains("are pending approval") || creativeStatus.contains("are denied") || creativeStatus.contains("Creative assigned and approved"));
    }

    @When("User navigates to Tactic and assigns creative of status {string} to the Tactic")
    public void userNavigatesToTacticAndAssignsCreativeToTheTactic(String status) {
        campaignDashboard.navigateToTacticDetails();
        tacticCreatives.clickCreativeTab();
        tacticCreatives.clickAssignCreatives();
        tacticCreatives.selectAndAssignCreativeByStatus(status);
        tacticCreatives.saveTacticCreatives();
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
        npiName = npiListName + '_' + CommonUtils.timeStampCalculation();
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
        npiNameEdited = "Edited" + '_' + npiName;
        npiStaticList.editListName(npiNameEdited);
        npiStaticList.saveList();
        Assert.assertTrue("Unable to see success message", npiStaticList.saveListSuccess().contains("NPI list created"));
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

    @When("User enters below details in respective search field, verify that the deal list appears based on the selected filters")
    public void userEntersBelowDetailsInRespectiveSearchField(DataTable filterBy) {
        Map<String, String> rawMap = filterBy.asMap(String.class, String.class);
        Map<String, List<String>> filterMap = CommonUtils.processDataTable(rawMap);
        for (Map.Entry<String, List<String>> entry : filterMap.entrySet()) {
            Assert.assertTrue("Deals list is not available for " + entry.getKey(), pmp.applyFilter(entry.getKey(), entry.getValue()));
        }
    }

    @And("User clicks on Add New Deal button")
    public void userClicksOnAddNewDealButton() {
        pmp.clickAddNewDeals();
    }

    @Then("New Deal panel should open and user should be able to add new deal with details {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void newDealPanelShouldOpenAndUserShouldBeAbleToAddNewDealWithDetails(String exchangeType, String dealID, String dealName, String mediaType, String advertiser, String dealPriceType, String price, String curator) {
        dealIDRandom = dealID + CommonUtils.timeStampCalculation();
        dealNameRandom = dealName + CommonUtils.timeStampCalculation();
        List<String> mediaTypeList = Arrays.stream(mediaType.split(",")).toList();
        Assert.assertEquals("Success!", pmp.addAndSaveNewDeals(exchangeType, dealIDRandom, dealNameRandom, mediaTypeList, advertiser, dealPriceType, price, curator));
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
        Assert.assertTrue("Delete icon is not disabled", pmp.isDeleteIconDisabled());
        Assert.assertEquals(errorMessage, pmp.fetchMessageOnDeleteIconClick());
    }

    @And("Verify Pricing Strategy is editable for Deals present in Curated Market and Deals section")
    public void verifyPricingStrategyIsEditableForDealsPresentInCuratedMarketAndDealsSection(DataTable pricingStrategy) {
        Map<String, String> rawMap = pricingStrategy.asMap(String.class, String.class);
        Map<String, List<String>> filterMap = CommonUtils.processDataTable(rawMap);
        for (Map.Entry<String, List<String>> entry : filterMap.entrySet()) {
            pmp.verifyPricingStrategyIsEditable(dealNameRandom, entry.getKey(), entry.getValue());
        }
    }

    @And("Verify user can add new {string} deals by clicking Add Deal button present in Curated Market and Deals section using details {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string} with toggle {string}")
    public void verifyUserCanApplyDealsByClickingAddDealButtonPresentInCuratedMarketAndDealsSection(String dealType, String exchangeType, String dealID, String dealName, String mediaType, String advertiser, String dealPriceType, String price, String curator, String toggleButton) {
        List<String> mediaTypeList = Arrays.stream(mediaType.split(",")).toList();
        dealIDRandom = dealID + CommonUtils.timeStampCalculation() + "_01";
        dealNameRandom = dealName + CommonUtils.timeStampCalculation() + "_01";
        Assert.assertTrue("Assigned Deals are not present under targeting and deals section", pmp.applyDealsFromDealsSection(dealType, exchangeType, dealIDRandom, dealNameRandom, mediaTypeList, advertiser, dealPriceType, price, curator, toggleButton));
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
        npiName = listName + '_' + CommonUtils.timeStampCalculation();
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
        npiNameEdited = "Edited" + '_' + npiName;
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
        List<String> lineItemTypeList = Arrays.stream(lineItemType.split(",")).map(String::trim).toList();
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
        Assert.assertTrue("Copy option is not working properly", createCreatives.copyCreative().contains("updated."));
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

            Map<String, String> attributeMap = Arrays.stream(attributes.split(",")).map(String::trim).map(entry -> entry.split(":", 2)).collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));

            createCreatives.fillAttributes(type, attributeMap);
            String actualMessage = createCreatives.saveCreative();
            Assert.assertTrue("No message is displayed", actualMessage.contains("BulkUpload created successfully.") || actualMessage.contains("Creative " + newCreativeName + " created."));
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
        List<String> lineItemTypeList = Arrays.stream(lineItemType.split(",")).map(String::trim).toList();
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
        npiName = listName + '_' + CommonUtils.timeStampCalculation();
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
        for (String tab : subTabsList) {
            Assert.assertTrue(tab + " Tab is not present", sharedList.verifySubTabs(tab));
        }
        Assert.assertTrue("Both tab is not selected by default", sharedList.verifyDefaultSubTab(defaultTabName));
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
        Assert.assertTrue("Upload section is not available before list input", sharedList.verifyUploadSectionIsVisibleBeforeListInput());
        sharedList.enterDomainNames(keyValues);
        Assert.assertTrue("Upload section is available after list input", sharedList.verifyUploadSectionIsVisibleAfterListInput());
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
        Assert.assertEquals(itemCount + sharedList.fetchDomainCountFromUploadedFilesSection(fileName), domainCount);
    }

    @And("Verify that user is able to download the uploaded file {string}, {string}")
    public void verifyThatUserIsAbleToDownloadTheUploadedFile(String fileName1, String fileName2) throws IOException {
        Assert.assertTrue("Downloaded file is not available", CommonUtils.isDownloadedFileAvailable(sharedList.downloadFile(fileName1), "csv"));
        Assert.assertTrue("Downloaded file is not available", CommonUtils.isDownloadedFileAvailable(sharedList.downloadFile(fileName2), "csv"));
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
        newPixelName = pixelName + '_' + CommonUtils.timeStampCalculation();
        retargetingPixel.enterPixelName(newPixelName);
        retargetingPixel.selectAdvertiser(advertiser);
    }

    @And("User enters the pixel details as {string} {string} {string} {string}")
    public void userEntersPixelDetails(String pixelName, String advertiser, String conversionPixelScope, String conversionPixelType) {
        newPixelName = pixelName + '_' + CommonUtils.timeStampCalculation();
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

    @Then("Verify the pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list")
    public void verifyPixelIsSavedSuccessfullyAndDisplayedInPixelList() {
        Assert.assertTrue("Unable to save pixel", pixels.verifySaveSuccess().contains("Success!"));
        pixels.searchSavedPixel(newPixelName);
        Assert.assertEquals(newPixelName, pixels.verifyCreatedPixel(newPixelName));
    }

    @Then("Verify the smart pixel is saved successfully, search for it by name, and confirm it is displayed in the pixel list")
    public void verifySmartPixelIsSavedSuccessfullyAndDisplayedInPixelList() {
        Assert.assertTrue("Unable to save Smart Pixel",  pixels.verifySaveSuccess().contains("Success!"));
        newPixelName = smartPixel.getPixelNameFromHeader();
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

    @And("User enters the Smart NPI list details as {string} {string} and selects the created {string}")
    public void userEntersTheSmartNPIListDetailsAndSelectsTheCreatedSmartPixel(String npiListName, String advertiser, String smartListType) {
        npiName = npiListName + '_' + CommonUtils.timeStampCalculation();
        npiStaticList.enterListName(npiName);
        npiStaticList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
        npiSmartList.selectSmartNPIListType(smartListType);
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
        Assert.assertTrue("Expected 'Display' to be the default selected creative type.", bulkCreativeUpload.checkDefaultCreativeType(defaultOption));
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
        bulkCreativeUpload.clickUploadButton();
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
        bulkCreativeUpload.clickOKButton();
        metricName = creativeType + "_" + CommonUtils.timeStampCalculation();
        bulkCreativeUpload.updateCreativeName(metricName);
        bulkCreativeUpload.clickUploadButton();
        nameList.clear();
        nameList.add(metricName);
    }

    @And("User saves the creative")
    public void userSavesTheCreative() {
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("BulkUpload created successfully.", bulkCreativeUpload.fetchSuccessAlert());
    }

    /*Display Creative Bulk Upload*/
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

    @And("User is able to download a blank template using the Download Blank Template option")
    public void userIsAbleToDownloadABlankTemplateUsingTheOption() throws IOException {
        Assert.assertTrue("Downloaded file is not available", CommonUtils.isDownloadedFileAvailable(bulkCreativeUpload.clickBlankTemplateDownloadButton(), "xlsx"));
    }

    @And("Verify user is able to upload images {string} to get a template with URLs")
    public void userIsAbleToUploadImagesToGetATemplateWithURLsUsingTheOption(String imageFileName) throws IOException {
        bulkCreativeUpload.uploadImageFile(imageFileName);
        Assert.assertTrue("Downloaded file is not available", CommonUtils.isDownloadedFileAvailable(bulkCreativeUpload.clickTemplateWithURLsLink(), "xlsx"));
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
        logger.info("Verifying availability of Third Party Tracking Pixel and DoubleVerify Pixel under section: {}", sectionName);
        Assert.assertTrue("Add Third Party Tracking Pixel/Tag field is not available under " + sectionName, bulkCreativeUpload.isThirdPartyTrackingPixelAvailable());
        Assert.assertTrue("Add DoubleVerify Pixel is not available under " + sectionName, bulkCreativeUpload.isDoubleVerifyPixelAvailable());
        logger.info("Both pixel fields are available under section: {}", sectionName);
    }

    @And("User is able to click a third-party tracking pixel and add details {string}")
    public void userIsAbleToSelectAThirdPartyTrackingPixelForACreative(String pixelDetails) {
        logger.info("Adding Third Party Tracking Pixel with details: {}", pixelDetails);
        bulkCreativeUpload.addThirdPartyTrackingPixel(pixelDetails);
        logger.info("Third Party Tracking Pixel added successfully");
    }

    @And("User is able to add a DoubleVerify pixel")
    public void userIsAbleToAddADoubleVerifyPixelForACreative() {
        logger.info("Attempting to add DoubleVerify Pixel");
        Assert.assertTrue("Unable to add DoubleVerify Pixel", bulkCreativeUpload.addDoubleVerifyPixel());
        logger.info("DoubleVerify Pixel added successfully");
    }

    @And("User is able to delete third-party tracking pixel entries")
    public void userIsAbleToDeleteThirdPartyTrackingPixelEntries() {
        logger.info("Attempting to delete Third Party Tracking Pixel entries");
        Assert.assertTrue("Unable to delete Third Party Tracking Pixel", bulkCreativeUpload.deleteThirdPartyTrackingPixel());
        logger.info("Third Party Tracking Pixel entries deleted successfully");
    }

    @And("An error message is displayed when a blank template {string} is uploaded")
    public void anErrorMessageIsDisplayedWhenABlankTemplateIsUploaded(String fileName) {
        logger.info("Uploading blank template file: {}", fileName);
        bulkCreativeUpload.uploadBlankTemplate(fileName);
        logger.info("Blank template upload attempted, error message should be displayed");
    }

    @And("User enters {string}, {string} mandatory fields data for Display creative")
    public void userEntersMandatoryFieldsDataForCreative(String advertiserDSA, String financer) {
        logger.info("Entering mandatory fields data - Advertiser DSA: {}, Financer: {}", advertiserDSA, financer);
        bulkCreativeUpload.enterAdvertiserDSA(advertiserDSA);
        bulkCreativeUpload.enterFinancer(financer);
        logger.info("Mandatory fields data entered successfully");
    }

    @And("Verify Advertiser field should be mandatory")
    public void verifyAdvertiserFieldShouldBeMandatory() {
        logger.info("Verifying Advertiser field mandatory validation");
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("Select Advertiser", bulkCreativeUpload.fetchErrorAlert());
        logger.info("Advertiser mandatory validation message verified");
    }

    @And("Verify that the Landing Domain field is mandatory when all other required fields, including {string} are filled")
    public void verifyLandingDomainFieldShouldBeMandatoryByEnteringOtherMandatoryFields(String advertiser) {
        logger.info("Verifying Landing Domain mandatory validation after selecting advertiser: {}", advertiser);
        bulkCreativeUpload.selectAdvertiser(advertiser);
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("Landing Page Domain is required", bulkCreativeUpload.fetchErrorAlert());
        logger.info("Landing Domain mandatory validation message verified");
    }

    @And("Verify that an appropriate error message is displayed when invalid data {string} is entered for the Landing Domain")
    public void verifyThatAnAppropriateErrorMessageIsDisplayedWhenInvalidDataIsEnteredForTheLandingDomain(String invalidLandingDomain) {
        logger.info("Entering invalid Landing Domain: {}", invalidLandingDomain);
        bulkCreativeUpload.enterLandingPageDomain(invalidLandingDomain);
        bulkCreativeUpload.clickOKButton();
        Assert.assertEquals("Landing Page Domain is not valid.", bulkCreativeUpload.fetchErrorAlert());
        logger.info("Invalid Landing Domain validation message verified");
    }

    @And("Verify only valid Landing Domain {string} values should be permitted")
    public void verifyOnlyValidLandingDomainValuesShouldBePermitted(String validLandingDomain) {
        logger.info("Entering valid Landing Domain: {}", validLandingDomain);
        bulkCreativeUpload.enterLandingPageDomain(validLandingDomain);
        Assert.assertEquals("", bulkCreativeUpload.fetchErrorAlert());
        logger.info("Valid Landing Domain accepted successfully");
    }

    @And("Verify default value of the File field should be {string}")
    public void verifyDefaultValueOfTheFileFieldShouldBe(String defaultValue) {
        logger.info("Verifying default value of File field");
        Assert.assertEquals(defaultValue, bulkCreativeUpload.fetchFileDefaultValue());
        logger.info("Default File field value verified as: {}", defaultValue);
    }

    @And("Verify default value of the AdChoices Icon should be {string}")
    public void verifyDefaultValueOfTheAdChoicesIconShouldBe(String defaultValue) {
        logger.info("Verifying default value of AdChoices Icon");
        Assert.assertEquals(defaultValue, bulkCreativeUpload.fetchAdChoiceDefaultValue());
        logger.info("Default AdChoices Icon value verified as: {}", defaultValue);
    }

    @And("Verify default value of the Notes Column field should be {string}")
    public void verifyDefaultValueOfTheNotesColumnFieldShouldBe(String defaultValue) {
        logger.info("Verifying default value of Notes Column field");
        Assert.assertEquals(defaultValue, bulkCreativeUpload.fetchNotesColumnDefaultValue());
        logger.info("Default Notes Column value verified as: {}", defaultValue);
    }

    @And("Verify Rich Media checkbox should be present and selectable {string}")
    public void verifyRichMediaCheckboxShouldBePresentAndSelectable(String direction) {
        logger.info("Verifying Rich Media checkbox presence and clickability");
        Assert.assertTrue("Rich Media Checkbox is not available", bulkCreativeUpload.isRichMediaCheckboxAvailable());
        Assert.assertTrue("Rich Media Checkbox is not clickable", bulkCreativeUpload.isRichMediaCheckboxClickable());
        bulkCreativeUpload.selectAndClickDirection(direction);
        logger.info("Rich Media checkbox selected with direction: {}", direction);
    }

    @And("Verify that the user is able to browse the computer, upload the following file types, and create creatives using details - {string}, {string}, {string}, {string}, {string}, {string}")
    public void verifyThatTheUserIsAbleToBrowseTheComputerUploadTheFollowingFileTypesAndCreateCreativesUsingDetails(String advertiser, String advertiserDSA, String financer, String landingDomain, String status, String creativeName, DataTable dataTable) {
        logger.info("Starting bulk upload creative creation flow");
        nameList.clear();
        Map<String, String> rawFilters = dataTable.asMap(String.class, String.class);
        Map<String, List<String>> filtersMap = CommonUtils.processDataTable(rawFilters);
        for (Map.Entry<String, List<String>> entry : filtersMap.entrySet()) {
            logger.info("Uploading file type: {} with extensions: {}", entry.getKey(), entry.getValue());
            bulkCreativeUpload.selectAdvertiser(advertiser);
            bulkCreativeUpload.enterAdvertiserDSA(advertiserDSA);
            bulkCreativeUpload.enterFinancer(financer);
            bulkCreativeUpload.selectFileTypeAndUploadFile(entry.getKey(), entry.getValue());
            bulkCreativeUpload.enterLandingPageDomain(landingDomain);
            bulkCreativeUpload.selectApprovalStatus(status);
            nameList = bulkCreativeUpload.enterCreativeName(creativeName);
            if (bulkCreativeUpload.isWidthHeightVisibleAndBlank()) bulkCreativeUpload.enterWidthHeight("800x250");
            bulkCreativeUpload.clickUploadButton();
            bulkCreativeUpload.clickOKButton();
            Assert.assertEquals("BulkUpload created successfully.", bulkCreativeUpload.fetchSuccessAlert());
            logger.info("Bulk upload creative created successfully for file type: {}", entry.getKey());
        }
    }

    @And("Verify user is able to type in {string} categories")
    public void verifyUserIsAbleToTypeInCategories(String iabCategory) {
        logger.info("Typing IAB Category: {}", iabCategory);
        bulkCreativeUpload.typeIABCategory(iabCategory);
    }

    @And("Verify that the Clickthrough URL and Landing Domain fields are validated as mandatory when all other required fields are filled")
    public void verifyThatTheClickthroughURLAndLandingDomainFieldsAreValidatedAsMandatoryWhenAllOtherRequiredFieldsIncludingAreFilled() {
        logger.info("Verifying mandatory validation for Clickthrough URL and Landing Domain");
        bulkCreativeUpload.clickOKButton();
        List<String> expectedMessages = Arrays.asList("Clickthrough URL is required", "Landing Page Domain is required");
        Assert.assertEquals(expectedMessages, bulkCreativeUpload.fetchInlineValidationMessage());
        logger.info("Mandatory validation messages verified for Clickthrough URL and Landing Domain");
    }

    @And("Verify only valid Clickthrough URL {string} values should be permitted")
    public void verifyOnlyValidClickthroughURLValuesShouldBePermitted(String validURL) {
        logger.info("Entering valid Clickthrough URL: {}", validURL);
        bulkCreativeUpload.enterClickthroughURL(validURL);
        Assert.assertEquals("", bulkCreativeUpload.fetchErrorAlert());
        logger.info("Valid Clickthrough URL accepted successfully");
    }

    @When("User creates and saves {string} Bulk upload creative using details {string} as Advertiser, {string}, {string} and below Creative attributes")
    public void userCreatesAndSavesBulkUploadCreativeUsingDetailsAsAdvertiserAsCreativeNameAndBelowCreativeAttributes(String creativeType, String advertiser, String advertiserDSA, String financer, DataTable dataTable) {
        logger.info("Creating and saving Bulk Upload creatives of type: {}", creativeType);
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String type = row.get("CreativeType").trim();
            String attributes = row.get("CreativeAttributes").trim();
            String creativeName = creativeType + "_Creative_" + CommonUtils.timeStampCalculation();
            logger.info("Creating creative: {} with attributes: {}", creativeName, attributes);
            Map<String, String> attributeMap = Arrays.stream(attributes.split(",")).map(String::trim).map(entry -> entry.split(":", 2)).collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            bulkCreativeUpload.clickBulkUploadButton();
            bulkCreativeUpload.selectAndClickCreativeType(creativeType);
            bulkCreativeUpload.enterCreativeAndDSADetails(advertiser, advertiserDSA, financer);
            bulkCreativeUpload.fillAttributes(type, attributeMap, creativeName);
            Assert.assertEquals("BulkUpload created successfully.", bulkCreativeUpload.fetchSuccessAlert());
            nameList.add(creativeName);
            logger.info("Bulk upload creative created successfully: {}", creativeName);
        }
    }

    /*Run A Report - Fields verification and validation*/
    @And("Verify Run Report panel should be opened")
    public void verifyRunReportPanelIsOpened() {
        logger.info("Verifying Run Report panel is opened");
        Assert.assertTrue("Run Report panel is not opened", runReportPanel.isRunReportPanelOpened());
    }

    @And("Template drop-down should display templates created under {string}")
    public void templateDropDownShouldDisplayTemplatesCreatedUnder(String templateName) {
        logger.info("Verifying Template dropdown availability for templates under: {}", templateName);
        Assert.assertTrue("Template dropdown is not present", runReportPanel.isTemplateDropdownAvailable());
    }

    @When("User clicks on {string} link")
    public void userClicksOnLink(String linkName) {
        logger.info("Clicking on link: {}", linkName);
        runReportPanel.clickLink(linkName);
    }

    @Then("Dimensions and Metrics fields should be displayed")
    public void dimensionsAndMetricsFieldsShouldBeDisplayed() {
        logger.info("Verifying Dimensions and Metrics fields are displayed");
        Assert.assertTrue("Dimension and Metrics dropdown are not displayed", runReportPanel.isDimensionsAndMetricsDisplayed());
    }

    @And("User should navigate back to Template drop-down by clicking {string}")
    public void userShouldNavigateBackToTemplateDropDownByClicking(String linkName) {
        logger.info("Navigating back by clicking link: {}", linkName);
        runReportPanel.clickLink(linkName);
    }

    @Then("Template drop-down should be visible")
    public void templateDropDownShouldBeVisible() {
        logger.info("Verifying Template dropdown visibility");
        Assert.assertTrue("Template dropdown is not present", runReportPanel.isTemplateDropdownAvailable());
    }

    @Then("Data Granularity should have default value {string}")
    public void dataGranularityShouldHaveDefaultValue(String defaultValue) {
        logger.info("Verifying default Data Granularity value");
        Assert.assertEquals(defaultValue.trim(), runReportPanel.getDefaultDataGranularity());
    }

    @And("Verify Data Granularity dropdown should show below list of values")
    public void verifyDataGranularityDropdownShouldShowBelowListOfValues(DataTable dataTable) {
        List<String> dropdownValues = dataTable.asList(String.class);
        logger.info("Verifying Data Granularity dropdown values: {}", dropdownValues);
        runReportPanel.showDataGranularityOptions();
        Assert.assertEquals(new HashSet<>(dropdownValues), new HashSet<>(runReportPanel.fetchDataGranularityOptions()));
    }

    @And("Verify Data Granularity field should allow selection any of the values {string} from the dropdown")
    public void verifyDataGranularityFieldShouldAllowSelectionAnyOfTheValuesFromTheDropdown(String dropdownValue) {
        logger.info("Selecting Data Granularity value: {}", dropdownValue);
        Assert.assertTrue("Unable to set Data Granularity value from drop-down", runReportPanel.setDataGranularity(dropdownValue));
        logger.info("Data Granularity value selected successfully: {}", dropdownValue);
    }

    @Then("Advertiser drop-down should list advertisers mapped to {string}")
    public void advertiserDropDownShouldListAdvertisersMappedTo(String arg0) {
        logger.info("Verifying Advertiser dropdown lists mapped advertisers");
        Assert.assertTrue("Advertiser dropdown is not present", runReportPanel.isAdvertiserDropdownAvailable());
        runReportPanel.clickAdvertiserDropdown();
        List<String> advertiser = runReportPanel.fetchAdvertisers();
        Assert.assertTrue("Advertiser List does not match", advertiser.containsAll(itemList));
        logger.info("Advertiser dropdown contains all mapped advertisers");
    }

    @And("User should be able to select multiple advertisers from the list")
    public void userShouldBeAbleToSelectMultipleAdvertisersFromTheList() {
        logger.info("Selecting multiple advertisers from dropdown");
        runReportPanel.clickAdvertiserDropdown();
        Assert.assertTrue("Unable to select multiple advertisers", runReportPanel.selectMultipleAdvertisersFromDropdown());
        logger.info("Multiple advertisers selected successfully");
    }

    @And("Verify on selecting {string} option, previously selected individual advertisers should be cleared")
    public void verifyOnSelectingOptionPreviouslySelectedIndividualAdvertisersShouldBeCleared(String advertiser) {
        logger.info("Selecting advertiser option to clear previous selections: {}", advertiser);
        Assert.assertEquals(advertiser, runReportPanel.selectAdvertiser(advertiser));
        logger.info("Previously selected advertisers cleared successfully");
    }

    @And("User should be able to select template {string} from the dropdown")
    public void userShouldBeAbleToSelectTemplateFromTheDropdownAndAdvertiserAs(String templateName) {
        logger.info("Selecting template from dropdown: {}", templateName);
        runReportPanel.selectTemplateFromDropdown(templateName);
        templateNameRandom = runReportPanel.fetchTemplateValue().get(0);
        nameList.add(templateNameRandom);
        logger.info("Template selected successfully: {}", templateNameRandom);
    }

    @And("User should be able to select advertiser as {string}")
    public void userShouldBeAbleToSelectAdvertiserAs(String advertiser) {
        logger.info("Selecting advertiser: {}", advertiser);
        runReportPanel.clickAdvertiserDropdown();
        Assert.assertEquals(advertiser, runReportPanel.selectAdvertiser(advertiser));
        nameList.add(advertiser);
        logger.info("Advertiser selected successfully: {}", advertiser);
    }

    @When("Campaign should load for selection when user types campaign initials {string} in {string} field")
    public void campaignShouldLoadForSelectionWhenUserTypesCampaignInitialsInCampaignField(String campaignInitials, String fieldName) {
        logger.info("Typing campaign initials '{}' in field '{}'", campaignInitials, fieldName);
        Assert.assertTrue("Dropdown values are not loaded", runReportPanel.isDropdownValueLoadedForInitials(campaignInitials, fieldName));
        logger.info("Campaign dropdown values loaded successfully");
    }

    @Then("User should be able to select multiple values from dropdown")
    public void userShouldBeAbleToSelectMultipleValuesFromDropdown() {
        logger.info("Selecting multiple values from dropdown");
        List<String> valuesSelected = runReportPanel.selectMultipleValueFromDropdown();
        Assert.assertFalse("Unable to select multiple values from dropdown", valuesSelected.isEmpty());
        nameList.addAll(valuesSelected);
        logger.info("Multiple dropdown values selected: {}", valuesSelected);
    }

    @When("Line Items of selected campaigns should load when user types line items initials {string} in {string} field")
    public void lineItemsOfSelectedCampaignsShouldLoadWhenUserTypesLineItemsInitialsInLineItemField(String lineItemInitials, String fieldName) {
        logger.info("Typing line item initials '{}' in field '{}'", lineItemInitials, fieldName);
        Assert.assertTrue("Dropdown values are not loaded", runReportPanel.isDropdownValueLoadedForInitials(lineItemInitials, fieldName));
    }

    @When("Tactic of selected line items should load when user types tactic names initials {string} in {string} field")
    public void tacticOfSelectedLineItemsShouldLoadWhenUserTypesTacticNamesInitialsInTacticField(String tacticInitials, String fieldName) {
        logger.info("Typing tactic initials '{}' in field '{}'", tacticInitials, fieldName);
        Assert.assertTrue("Dropdown values are not loaded", runReportPanel.isDropdownValueLoadedForInitials(tacticInitials, fieldName));
    }

    @When("Creative of selected tactic should load when user types creative names initials {string} in {string} field")
    public void creativeOfSelectedTacticShouldLoadWhenUserTypesCreativeNamesInitialsInCreativeField(String creativeInitials, String fieldName) {
        logger.info("Typing creative initials '{}' in field '{}'", creativeInitials, fieldName);
        Assert.assertTrue("Dropdown values are not loaded", runReportPanel.isDropdownValueLoadedForInitials(creativeInitials, fieldName));
    }

    @When("User clicks on Advanced Settings")
    public void userClicksOn() {
        logger.info("Clicking on Advanced Settings");
        runReportPanel.clickAdvanceSettings();
    }

    @Then("{string} section should be visible with label {string} checkbox")
    public void checkboxShouldBeVisibleWithLabel(String filterReportSection, String checkboxLabel) {
        logger.info("Verifying filter section '{}' with checkbox '{}'", filterReportSection, checkboxLabel);
        Assert.assertTrue("Report Filter checkbox is not available", runReportPanel.isFilterReportSectionAvailable(filterReportSection));
        if (runReportPanel.isFilterReportCheckboxAvailable(checkboxLabel))
            Assert.assertEquals(checkboxLabel.trim(), runReportPanel.fetchFilterReportCheckboxLabel(checkboxLabel));
        logger.info("Filter section and checkbox verified successfully");
    }

    @Then("{string} and {string} tabs should be present in the Run Report pop-up")
    public void andTabsShouldBePresentInTheRunReportPopUp(String runNowTab, String scheduleTab) {
        logger.info("Verifying Run Report popup tabs: {}, {}", runNowTab, scheduleTab);
        Assert.assertTrue("Run Now and Schedule tabs are not available", runReportPanel.isRunNowAndScheduleTabsAvailable(runNowTab, scheduleTab));
    }

    @When("On Run Now tab, Report Period field should have options below")
    public void onRunNowTabReportPeriodFieldShouldHaveOptionsBelow(DataTable dataTable) {
        List<String> dropdownValues = dataTable.asList(String.class);
        logger.info("Verifying Report Period dropdown options: {}", dropdownValues);
        Assert.assertEquals(new HashSet<>(dropdownValues), new HashSet<>(runReportPanel.fetchReportPeriodOptions()));
    }

    @And("User selects the option Only Report on Impressions with Identifiable NPIs")
    public void userSelectsTheOptionOnlyReportOnImpressionsWithIdentifiableNPIs() {
        logger.info("Selecting 'Only Report on Impressions with Identifiable NPIs' option");
        runReportPanel.clickFilterReportCheckbox();
    }

    @And("User should be able to generate the report")
    public void userShouldAbleToGenerateTheReport() {
        String fileName = "Custom Report";
        logger.info("Generating report with file name: {}", fileName);
        metricName = runReportPanel.fetchFileName();
        runReportPanel.clickRunButton(fileName);
        Assert.assertEquals("You will get the report on your email", runReportPanel.fetchSuccessAlert());
        logger.info("Report generation triggered successfully");
    }

    @And("Confirms that the report panel retains the entered data")
    public void andConfirmThatTheReportPanelRetainsTheEnteredData() {
        logger.info("Verifying report panel retains entered data");
        runReportPanel.clickModifyOption(metricName);
        capturedDetails.addAll(runReportPanel.fetchTemplateValue());
        capturedDetails.addAll(runReportPanel.fetchDimensionAndMetricValues());
        capturedDetails.addAll(runReportPanel.fetchAdvertiserName());
        capturedDetails.addAll(runReportPanel.fetchCampaignName());
        capturedDetails.addAll(runReportPanel.fetchLineItemName());
        capturedDetails.addAll(runReportPanel.fetchTacticName());
        capturedDetails.addAll(runReportPanel.fetchCreativeName());
        capturedDetails.add(runReportPanel.fetchStartTime());
        capturedDetails.add(runReportPanel.fetchEndTime());
        capturedDetails.add(runReportPanel.fetchTimeZone());
        Assert.assertTrue("Not all entered data present in fetched values", capturedDetails.containsAll(nameList));
        logger.info("Report panel data persistence verified successfully");
    }

    @And("Verify that by default {string} option is selected for Report Period Field")
    public void verifyThatByDefaultCustomDatesOptionIsSelectedForReportPeriodField(String buttonType) {
        logger.info("Verifying default Report Period option selected: {}", buttonType);
        Assert.assertTrue("Custom Dates button is not enabled by default", runReportPanel.isReportPeriodSelected(buttonType));
    }

    @And("Verify that user is able to select start date and end date when Custom Dates option is selected")
    public void verifyThatUserIsAbleToSelectStartDateAndEndDateWhenCustomDatesOptionIsSelected() {
        logger.info("Selecting start and end date using Custom Dates option");
        Assert.assertTrue("Unable to select date from date picker", runReportPanel.selectStartAndEndDate());
    }

    @And("Verify that user is able to select start {string} and end time {string} when Custom Dates option is selected")
    public void verifyThatUserIsAbleToSelectStartAndEndTimeWhenCustomDatesOptionIsSelected(String startTime, String endTime) {
        logger.info("Selecting start time '{}' and end time '{}' for Custom Dates", startTime, endTime);
        Assert.assertTrue("Unable to select time", runReportPanel.enterStartAndEndTime(startTime, endTime));
        nameList.add(startTime);
        nameList.add(endTime);
    }

    @And("Verify that user is able to select Timezone field value {string}")
    public void verifyThatUserIsAbleToSelectTimezoneFieldValue(String timeZone) {
        logger.info("Selecting Timezone: {}", timeZone);
        Assert.assertTrue("Unable to select time zone " + timeZone, runReportPanel.selectTimeZone(timeZone.trim()));
        nameList.add(timeZone);
    }

    @And("Verify the default value of the Report Format field is {string}")
    public void verifyTheDefaultValueOfTheTheReportFormatFieldIsCSV(String fileFormat) {
        logger.info("Verifying default Report Format value: {}", fileFormat);
        Assert.assertEquals(fileFormat, runReportPanel.fetchDefaultReportFormat());
    }

    @And("Verify the availability of various options of the Report Format field - {string}")
    public void verifyTheAvailabilityOfVariousOptionsOfTheReportFormatField(String reportFormats) {
        List<String> expectedFormats = CommonUtils.convertStringToList(reportFormats);
        List<String> reportFormatValues = runReportPanel.fetchReportFormatList();
        logger.info("Verifying available Report Format options: {}", expectedFormats);
        for (String format : expectedFormats) {
            Assert.assertTrue("Missing report format: " + format, reportFormatValues.contains(format));
        }
    }

    @And("Verify by default the Text Qualifier checkbox is checked")
    public void verifyByDefaultTheTextQualifierCheckboxIsChecked() {
        logger.info("Verifying Text Qualifier checkbox is checked by default");
        Assert.assertTrue("Text Qualifier is not checked by default", runReportPanel.isTextQualifierCheckboxChecked());
    }

    @And("Verify that {string} and {string} options are disabled until a Line Item is selected")
    public void verifyThatLifetimeAndFlightsOptionsAreDisabledUntilALineItemIsSelected(String lifeTime, String flights) {
        logger.info("Verifying buttons disabled before Line Item selection: {}, {}", lifeTime, flights);
        List<String> disabledButtons = runReportPanel.verifyButtonsDisabledBeforeLineItemSelection();
        Assert.assertTrue("Expected disabled button missing: " + lifeTime, disabledButtons.contains(lifeTime));
        Assert.assertTrue("Expected disabled button missing: " + flights, disabledButtons.contains(flights));
    }

    @And("User should be able to select {string} and {string}")
    public void userShouldBeAbleToSelectAnd(String dimensions, String metrics) {
        List<String> dimensionList = CommonUtils.convertStringToList(dimensions);
        List<String> metricsList = CommonUtils.convertStringToList(metrics);
        logger.info("Selecting Dimensions: {} and Metrics: {}", dimensionList, metricsList);
        runReportPanel.selectDimension(dimensionList);
        runReportPanel.selectMetrics(metricsList);
        nameList.addAll(dimensionList);
        nameList.addAll(metricsList);
        templateNameRandom = "Custom Template";
    }

    @And("Verify that {string} and {string} options are enabled")
    public void verifyThatLifetimeAndFlightsOptionsAreEnabled(String lifeTime, String flights) {
        logger.info("Verifying buttons enabled after Line Item selection: {}, {}", lifeTime, flights);
        List<String> enabledButtons = runReportPanel.verifyButtonsEnabledAfterLineItemSelection();
        Assert.assertTrue("Expected " + lifeTime + " button to be enabled", enabledButtons.contains("Lifetime"));
        Assert.assertTrue("Expected " + flights + " button to be enabled", enabledButtons.contains("Flights"));
    }

    @And("User clicks {string} report period button")
    public void userClicksReportPeriodButton(String buttonName) {
        logger.info("Clicking Report Period button: {}", buttonName);
        runReportPanel.selectReportPeriodButton(buttonName);
    }

    @Then("User should be able to select value from dropdown")
    public void userShouldBeAbleToSelectValueFromDropdown() {
        logger.info("Selecting value from dropdown");
        String valuesSelected = runReportPanel.selectValueFromDropdown();
        Assert.assertFalse("Unable to select value from dropdown", valuesSelected.isEmpty());
        nameList.add(valuesSelected);
    }

    @And("User should be able to fetch details - Advertiser, Campaign, Line Item, Tactic")
    public void userShouldBeAbleToFetchDetailsAdvertiserCampaignLineItemTactic() {
        logger.info("Fetching Advertiser, Campaign, Line Item, and Tactic details from Run Report panel");
        nameList.addAll(runReportPanel.fetchAdvertiserName());
        nameList.addAll(runReportPanel.fetchCampaignName());
        nameList.addAll(runReportPanel.fetchLineItemName());
        logger.info("Fetched details successfully. Total values captured so far: {}", nameList.size());
    }

    @And("Verify that Flight details field is displayed with value")
    public void verifyThatFlightDetailsFieldIsDisplayedWithValue() {
        logger.info("Verifying that Flight details field is displayed and populated");
        Assert.assertFalse("Flight details are not populated", runReportPanel.isFlightDetailsDisplayed().isEmpty());
        logger.info("Flight details field is displayed with populated values");
    }

    @And("User navigates to Administrative section and fetches the advertiser for the account {string}")
    public void userNavigatesToAdministrativeSectionAndFetchesTheAdvertiserForTheAccount(String account) {
        logger.info("Navigating to Administrative section to fetch advertisers for account: {}", account);
        navigation.clickSubMenu();
        accounts.clickAdministration();
        accounts.clickAdvertiserTab();
        accounts.selectAccount(account);
        itemList = accounts.fetchAdvertiserList();
        logger.info("Fetched {} advertisers for account {}", itemList.size(), account);
        navigation.clickPulsePointLogo();
        logger.info("Navigated back to Life application");
    }

    @And("Expand all the groups and fetch dimensions and metrics")
    public void expandAllTheGroupsAndFetchDimensions() {
        logger.info("Expanding all template groups to fetch dimensions");
        nameList = reportTemplates.expandGroupsAndFetchDimensionsAndMetrics();
        logger.info("Fetched {} dimensions from template", nameList.size());
        reportTemplates.clickMetricsTab();
        logger.info("Switched to Metrics tab to fetch metrics");
        capturedDetails = reportTemplates.expandGroupsAndFetchDimensionsAndMetrics();
        logger.info("Fetched {} metrics from template", capturedDetails.size());
        reportTemplates.clickCancelButton();
        logger.info("Closed Report Template panel after fetching dimensions and metrics");
    }

    @And("Verify dropdown dimensions with the template")
    public void verifyDropdownDimensionsWithTheTemplate() {
        logger.info("Verifying Dimension dropdown values with template");
        List<String> dimensionList = runReportPanel.clickDimensionDropdownAndFetchValues();
        logger.info("Fetched Dimension values: {}", dimensionList);
        Assert.assertTrue("Template's Dimension values are not available in Run report", dimensionList.containsAll(nameList));
    }

    @And("Verify dropdown metrics with the template")
    public void verifyDropdownMetricsWithTheTemplate() {
        logger.info("Verifying Metrics dropdown values with template");
        List<String> metricList = runReportPanel.clickMetricDropdownAndFetchValues();
        logger.info("Fetched Metric values: {}", metricList);
        Assert.assertTrue("Template's Dimension values are not available in Run report", metricList.containsAll(capturedDetails));
    }

    @And("User selects {string} button")
    public void userSelects(String buttonType) {
        logger.info("User selects button: {}", buttonType);
        runReportPanel.clickFileBreakdownType(buttonType);
    }

    @Then("{string} section should be visible with label {string}, {string}, {string} checkbox")
    public void sectionShouldBeVisibleWithLabelCheckbox(String filterReportSection, String checkboxLabel1, String checkboxLabel2, String checkboxLabel3) {
        logger.info("Verifying filter report section: {}", filterReportSection);
        Assert.assertTrue("Report Filter checkbox is not available", runReportPanel.isFilterReportSectionAvailable(filterReportSection));
        if (runReportPanel.isFilterReportCheckboxAvailable(checkboxLabel1)){
            logger.info("Verifying checkbox label: {}", checkboxLabel1);
            Assert.assertEquals(checkboxLabel1.trim(), runReportPanel.fetchFilterReportCheckboxLabel(checkboxLabel1));
        }
        if (runReportPanel.isFilterReportCheckboxAvailable(checkboxLabel2)){
            logger.info("Verifying checkbox label: {}", checkboxLabel2);
            Assert.assertEquals(checkboxLabel2.trim(), runReportPanel.fetchFilterReportCheckboxLabel(checkboxLabel2));
        }
        if (runReportPanel.isFilterReportCheckboxAvailable(checkboxLabel3)){
            logger.info("Verifying checkbox label: {}", checkboxLabel3);
            Assert.assertEquals(checkboxLabel3.trim(), runReportPanel.fetchFilterReportCheckboxLabel(checkboxLabel3));
        }
    }

    @When("User navigates to Schedule report from mega menu of the life application")
    public void userNavigatesToScheduleReportFromMegaMenuOfTheLifeApplication() {
        logger.info("Navigating to Schedule Report from mega menu");
        navigation.clickSubMenu();
        navigation.clickMenuAngle();
        navigation.clickScheduledReport();
    }

    @And("User clicks Schedule Report button")
    public void userClicksScheduleReportButton() {
        logger.info("Verifying Schedule Report panel is opened");
        scheduleReport.clickScheduleReportButton();
    }

    @And("Verify Schedule Report panel should be opened")
    public void verifyScheduleReportPanelShouldBeOpened() {
        logger.info("Verifying Report Report panel is opened");
        Assert.assertTrue("Schedule Report panel is not opened", scheduleReport.isScheduleReportPanelOpened());
    }

    @And("Verify Report Name field is available and accepts input {string}")
    public void verifyReportNameFieldIsAvailableAndAcceptsInput(String name) {
        logger.info("Verifying Report Name field and entering value");
        Assert.assertTrue("Report Name field is not available", scheduleReport.isReportNameAvailable());
        metricName = name + "_" + CommonUtils.timeStampCalculation();
        logger.info("Generated Report Name: {}", metricName);
        scheduleReport.enterReportName(metricName);
        nameList.add(metricName);
    }

    @And("Verify availability of frequency field with options below")
    public void verifyAvailabilityOfFrequencyFieldWithOptions(DataTable dataTable) {
        logger.info("Verifying availability of Frequency field with expected options");
        List<String> buttonLabels = dataTable.asList(String.class);
        logger.info("Expected Frequency options: {}", buttonLabels);
        List<String> actualOptions = scheduleReport.fetchFrequencyOptions();
        logger.info("Actual Frequency options: {}", actualOptions);
        Assert.assertEquals(new HashSet<>(buttonLabels), new HashSet<>(actualOptions));
    }

    @And("Verify default value of the Frequency field is {string}")
    public void verifyDefaultValueOfTheFrequencyFieldIs(String defaultValue) {
        logger.info("Verifying default Frequency value is: {}", defaultValue);
        Assert.assertTrue("Weekly is not selected by default", scheduleReport.checkDefaultFrequencyOption(defaultValue));
    }

    @And("Verify that user is able to select Schedule start date and Schedule end date")
    public void verifyThatUserIsAbleToSelectScheduleStartDateAndScheduleEndDate() {
        logger.info("Verifying user can select Schedule Start Date and End Date");
        Assert.assertTrue("Unable to select start date from date picker", scheduleReport.selectScheduleStartDate());
        Assert.assertTrue("Unable to select end date from date picker", scheduleReport.selectScheduleEndDate());
    }

    @And("Verify default value of Data Timezone is {string}")
    public void verifyDefaultValueOfDataTimezoneIs(String timeZone) {
        logger.info("Verifying default Data Timezone is: {}", timeZone);
        String actualTimezone = scheduleReport.fetchDefaultTimeZone();
        logger.info("Actual default Data Timezone: {}", actualTimezone);
        Assert.assertEquals(timeZone.trim(), actualTimezone);
    }

    @And("Verify that user is able to select Data Timezone field value {string}")
    public void verifyThatUserIsAbleToSelectDataTimezoneFieldValue(String timeZone) {
        logger.info("Selecting Data Timezone: {}", timeZone);
        Assert.assertTrue("Unable to select time zone", scheduleReport.selectDataTimeZone(timeZone.trim()));
        nameList.add(timeZone);
        logger.info("Added timezone to nameList: {}", timeZone);
    }

    @And("The Send On field should contain all days of the week when {string} is selected as Frequency")
    public void theSendOnFieldShouldContainAllDaysOfTheWeekWhenIsSelectedAsFrequency(String frequencyOption, DataTable dataTable) {
        logger.info("Verifying Send On field for Frequency: {}", frequencyOption);
        List<String> expectedDays = dataTable.asList(String.class);
        logger.info("Expected Send On days: {}", expectedDays);
        Assert.assertTrue("Weekly is not selected by default", scheduleReport.checkDefaultFrequencyOption(frequencyOption));
        List<String> actualDays = scheduleReport.fetchWeekDays();
        logger.info("Actual Send On days: {}", actualDays);
        Assert.assertEquals(new HashSet<>(expectedDays), new HashSet<>(actualDays));
    }

    @And("Verify default value of Send On is {string}")
    public void verifyDefaultValueOfSendOnIs(String defaultValue) {
        logger.info("Verifying default Send On day is: {}", defaultValue);
        Assert.assertTrue("Sun is not selected by default", scheduleReport.checkDefaultWeekDay(defaultValue));
    }

    @And("Verify Send At field is available with Start Time and Timezone fields")
    public void verifySendAtFieldIsAvailableWithStartTimeAndTimezoneFields() {
        logger.info("Verifying Send At field availability (Start Time + Timezone)");
        Assert.assertTrue("Send At fields - Time and TimeZone are not available", scheduleReport.isSendAtFieldAvailable());
    }

    @And("Verify default value of Send At fields - Start Time is {string} and Timezone is {string}")
    public void verifyDefaultValueOfSendAtFieldsStartTimeIsAndTimezoneIs(String defaultTime, String defaultTimezone) {
        logger.info("Verifying default Send At values - Time: {}, Timezone: {}", defaultTime, defaultTimezone);
        Assert.assertEquals("Default time " + defaultTime + " is not present", defaultTime, scheduleReport.fetchSendAtTimeValue());
        Assert.assertEquals("Default time " + defaultTimezone + " is not present", defaultTimezone, scheduleReport.fetchSendAtTimezoneValue());
    }

    @And("Verify user is able to select Time {string} and Timezone {string} for Send At fields")
    public void verifyUserIsAbleToSelectTimeAndTimezoneForSendAtFields(String time, String timeZone) {
        logger.info("Selecting Send At Time: {} and Timezone: {}", time, timeZone);
        Assert.assertTrue("Unable to enter time and timezone", scheduleReport.enterSendAtTimeAndTimezone(time, timeZone));
        nameList.add(time);
        nameList.add(timeZone);
        logger.info("Added Send At Time and Timezone to nameList");
    }

    @And("Verify Delivery field has two methods - {string} and {string}")
    public void verifyDeliveryFieldHasTwoMethodsAnd(String email, String customDestination) {
        logger.info("Verifying Delivery methods: {} and {}", email, customDestination);
        List<String> methodNames = scheduleReport.verifyDeliveryMethods();
        logger.info("Available Delivery methods: {}", methodNames);
        Assert.assertTrue("Methods are not available", methodNames.contains(email) && methodNames.contains(customDestination));
    }

    @When("User clicks on {string} tab as Delivery Method")
    public void userClicksOnTabAsDeliveryMethod(String tabName) {
        logger.info("User clicks Delivery Method tab: {}", tabName);
        scheduleReport.clickDeliveryTab(tabName);
    }

    @Then("Verify Deliver to Users field is available")
    public void verifyDeliverToUsersFieldIsAvailable() {
        logger.info("Verifying Deliver To Users field availability");
        Assert.assertTrue("Deliver To Users field is not available", scheduleReport.isDeliveryToUserAvailable());
    }

    @And("User should be able to specify multiple users in Deliver to Users field")
    public void userShouldAbleToSpecifyMultipleUsersInDeliverToUsersField(DataTable dataTable) {
        List<String> userLists = dataTable.asList(String.class);
        logger.info("Adding multiple users for delivery: {}", userLists);
        scheduleReport.selectUsersForDelivery(userLists);
    }

    @And("Verify that Add Emails link is available below Deliver to Users")
    public void verifyThatAddEmailsLinkIsAvailableBelowDeliverToUsers() {
        logger.info("Verifying Add Emails link availability");
        Assert.assertTrue("Deliver To Users field is not available", scheduleReport.isAddEmailLinkAvailable());
    }

    @When("User clicks Add Emails link, Deliver to External Emails field should display")
    public void userClicksAddEmailsLinkDeliverToExternalEmailsFieldShouldDisplay() {
        logger.info("User clicks Add Emails link");
        scheduleReport.clickAddEmailsLink();
        Assert.assertTrue("Deliver To External Users field is not available", scheduleReport.isDeliverToExternalEmailsAvailable());
    }

    @And("User should be able to add multiple emails in Deliver to External Emails field")
    public void userShouldBeAbleToAddMultipleEmailsInDeliverToExternalEmailsField(DataTable dataTable) {
        List<String> emailList = dataTable.asList(String.class);
        logger.info("Adding external email addresses: {}", emailList);
        scheduleReport.enterExternalEmails(emailList);
    }

    @Then("Verify Destination dropdown field is available")
    public void verifyDestinationDropdownFieldIsAvailable() {
        logger.info("Verifying Destination dropdown field availability");
        Assert.assertTrue("Destination field is not available", scheduleReport.isDestinationAvailable());
    }

    @And("Verify {string} button is available in Destination dropdown field")
    public void verifyAddDestinationButtonIsAvailableInDestinationDropdownField(String buttonName) {
        logger.info("Verifying Add Destination button: {}", buttonName);
        Assert.assertTrue("Add Destination field is not available", scheduleReport.isAddDestinationAvailable(buttonName));
    }

    @When("User clicks {string} button")
    public void userClicksAddDestinationButton(String buttonName) {
        logger.info("User clicks Destination button: {}", buttonName);
        scheduleReport.clickAddDestination(buttonName);
    }

    @Then("Verify Destination Name, Destination Type fields are displayed")
    public void verifyDestinationNameDestinationTypeFieldsAreDisplayed() {
        logger.info("Verifying Destination Name and Destination Type fields");
        Assert.assertTrue("Destination Name and Type fields are not available", scheduleReport.isDestinationNameAndTypeDisplayed());
    }

    @And("Verify that Destination Type has values {string}")
    public void verifyThatDestinationTypeHasValues(String destinationTypes) {
        logger.info("Verifying Destination Type values: {}", destinationTypes);
        List<String> expectedTypes = CommonUtils.convertStringToList(destinationTypes);
        List<String> actualTypes = scheduleReport.fetchDestinationTypes();
        logger.info("Expected Destination Types: {}", expectedTypes);
        logger.info("Actual Destination Types: {}", actualTypes);
        for (String type : expectedTypes) {
            Assert.assertTrue("Missing Destination Type: " + type, actualTypes.contains(type));
        }
    }

    @And("Verify File Path field is available")
    public void verifyFilePathFieldIsAvailable() {
        logger.info("Verifying File Path field availability");
        Assert.assertTrue("File Path field is not available", scheduleReport.isFilePathAvailable());
    }

    @And("Verify File Name field is available")
    public void verifyFileNameFieldIsAvailable() {
        logger.info("Verifying File Name field availability");
        Assert.assertTrue("File Path field is not available", scheduleReport.isFileNameAvailable());
    }

    @And("Verify Compression field is available with below options and default value is {string}")
    public void verifyCompressionFieldIsAvailableWithBelowOptions(String defaultCompressionType, DataTable dataTable) {
        logger.info("Verifying Compression field with default value: {}", defaultCompressionType);
        List<String> expectedTypes = dataTable.asList(String.class);
        logger.info("Expected Compression types: {}", expectedTypes);
        Assert.assertTrue("None is not selected by default", scheduleReport.checkDefaultCompression(defaultCompressionType));
        List<String> actualTypes = scheduleReport.fetchCompressionTypes();
        logger.info("Actual Compression types: {}", actualTypes);
        Assert.assertEquals(new HashSet<>(expectedTypes), new HashSet<>(actualTypes));
    }

    @And("Verify Control File checkbox is present and by default it should be unchecked")
    public void verifyControlFileCheckboxIsPresentAndByDefaultItShouldBeUnchecked() {
        logger.info("Verifying Control File checkbox availability and default state");
        Assert.assertTrue("Control File checkbox is not available", scheduleReport.isControlFileCheckboxAvailable());
        Assert.assertTrue("Control File checkbox is not unchecked by default", scheduleReport.isControlFileCheckboxSelected());
    }

    @And("User clicks {string} as frequency")
    public void userClicksAsFrequency(String frequencyType) {
        logger.info("User selects Frequency option: {}", frequencyType);
        scheduleReport.clickFrequencyOption(frequencyType);
    }

    @And("User selects Send On date")
    public void userSelectsSendOnDate() {
        logger.info("User selects Send On date");
        Assert.assertTrue("Unable to select date from date picker", scheduleReport.selectScheduleStartDate());
    }

    @And("Verify Report Period is selected as {string}")
    public void verifyReportPeriodIsSelectedAs(String periodType) {
        logger.info("Selecting Report Period: {}", periodType);
        scheduleReport.selectReportPeriod(periodType);
        nameList.add(periodType);
        logger.info("Added Report Period to nameList: {}", periodType);
    }

    @And("User selects start date and end date when Custom Dates option is selected")
    public void userSelectsStartDateAndEndDateWhenCustomDatesOptionIsSelected() {
        logger.info("Selecting Start Date and End Date for Custom Dates");
        Assert.assertTrue("Unable to select start date", scheduleReport.selectStartDate());
        Assert.assertTrue("Unable to select end date", scheduleReport.selectEndDate());
    }

    @And("User selects start {string} and end time {string} when Custom Dates option is selected")
    public void userSelectsStartAndEndTimeWhenCustomDatesOptionIsSelected(String startTime, String endTime) {
        logger.info("Selecting Start Time: {} and End Time: {}", startTime, endTime);
        Assert.assertTrue("Unable to select start and end time", scheduleReport.selectStartAndEndTime(startTime, endTime));
        nameList.add(startTime);
        nameList.add(endTime);
    }

    @And("User clicks Schedule button to generate the report")
    public void userClicksScheduleButtonForReportGeneration() {
        logger.info("User clicks Schedule button to generate report");
        scheduleReport.clickScheduleButton();
        String successMsg = scheduleReport.fetchSuccessAlert();
        logger.info("Schedule Report success message: {}", successMsg);
        Assert.assertEquals("Success!", successMsg);
    }

    @And("User searches the report and checks the report panel retains the entered data")
    public void userSearchesTheReportAndChecksTheReportPanelRetainsTheEnteredData() {
        logger.info("Searching scheduled report and verifying retained data for report: {}", metricName);
        scheduleReport.searchReport(metricName);
        scheduleReport.clickReportName(metricName);
        capturedDetails.addAll(runReportPanel.fetchTemplateValue());
        capturedDetails.addAll(runReportPanel.fetchAdvertiserName());
        capturedDetails.addAll(runReportPanel.fetchCampaignName());
        capturedDetails.addAll(runReportPanel.fetchLineItemName());
        capturedDetails.addAll(runReportPanel.fetchTacticName());
        capturedDetails.addAll(runReportPanel.fetchCreativeName());
        capturedDetails.add(scheduleReport.fetchReportName());
        capturedDetails.add(scheduleReport.fetchTimeZone());
        capturedDetails.add(scheduleReport.fetchSendAtTime());
        capturedDetails.add(scheduleReport.fetchSendAtTimezone());
        capturedDetails.add(scheduleReport.fetchReportPeriod());
        capturedDetails.add(scheduleReport.fetchStartTime());
        capturedDetails.add(scheduleReport.fetchEndTime());
        logger.info("Captured Report Details: {}", capturedDetails);
        logger.info("Expected values: {}", nameList);
        Assert.assertTrue("Not all entered data present in fetched values", capturedDetails.containsAll(nameList));
    }

    @And("Verify Send On field is visible and user should be able to select the day {string} of the month")
    public void verifySendOnFieldIsVisibleAndUserShouldBeAbleToSelectTheDayOfTheMonth(String dayOfTheMonth) {
        logger.info("Verifying Send On field and selecting day of month: {}", dayOfTheMonth);
        Assert.assertTrue("Send On field is not available", scheduleReport.isSendOnAvailable());
        Assert.assertTrue("Unable to select Day of the Month", scheduleReport.selectDayOfTheMonth(dayOfTheMonth));
    }

    @And("Verify that user is able to select Send On day {string}")
    public void verifyThatUserIsAbleToSelectSendOnDay(String day) {
        logger.info("Selecting Send On weekday: {}", day);
        Assert.assertTrue("Unable to select Send On Day", scheduleReport.selectWeekDay(day));
    }

    @Then("User searches the Campaign {string}, navigates to LineItem and fetches the flight details")
    public void userSearchesTheCampaignNavigatesToLineItemAndFetchesTheFlightDetails(String campaignName) {
        logger.info("Searching Campaign: {} and fetching Line Item flight details", campaignName);
        campaignDashboard.searchCreatedCampaign(campaignName);
        campaignDashboard.expandCreatedLineItem();
        campaignDashboard.navigateToLineItemDetails();
        lineItemFlights.clickFlightTab();
        Assert.assertTrue("Flight details are not displayed", lineItemFlights.isFlightTableDisplayed());
        itemList = lineItemFlights.fetchFlightDates();
        logger.info("Fetched Flight Dates: {}", itemList);
    }

    @And("User fetches all the Flight details and verifies that selected Line Item flight details appear in the Flight tab of the Run Report panel")
    public void userFetchesAllTheFlightDetailsAndSelects() throws ParseException {
        logger.info("Fetching and validating Flight details in Run Report panel");
        List<String> flightDescriptions = runReportPanel.fetchAndSelectFlightDetails();
        logger.info("Flight Descriptions from Run Report: {}", flightDescriptions);
        SimpleDateFormat descFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        SimpleDateFormat extractedFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        SimpleDateFormat compareFormat = new SimpleDateFormat("MM/dd/yyyy");
        for (int i = 0; i < flightDescriptions.size() && (i * 2 + 1) < itemList.size(); i++) {
            String desc = flightDescriptions.get(i);
            String[] dateParts = desc.split(":", 2)[1].split("-");
            String startDate = dateParts[0].trim();
            String endDate = dateParts[1].trim();
            startDate = startDate.replaceAll("(\\d+)(st|nd|rd|th)", "$1");
            endDate = endDate.replaceAll("(\\d+)(st|nd|rd|th)", "$1");
            String actualStart = compareFormat.format(descFormat.parse(startDate));
            String actualEnd = compareFormat.format(descFormat.parse(endDate));
            String expectedStart = compareFormat.format(extractedFormat.parse(itemList.get(i * 2)));
            String expectedEnd = compareFormat.format(extractedFormat.parse(itemList.get(i * 2 + 1)));
            Assert.assertEquals("Start date mismatch for Flight #" + (i + 1), expectedStart, actualStart);
            Assert.assertEquals("End date mismatch for Flight #" + (i + 1), expectedEnd, actualEnd);
        }
    }


    @And("User downloads the Scheduled report and verify the data in downloaded report")
    public void userDownloadsTheScheduledReportAndVerifyTheDataInDownloadedReport() {
        logger.info("Downloading scheduled report");
        runReportPanel.downloadScheduledReport();
    }

    @When("User navigates to Administrative section and go to Accounts Tab")
    public void userNavigatesToAdministrativeSectionAndGoToTab() throws Exception {
        logger.info("Navigating to Administrative section - Accounts tab");
        navigation.clickSubMenu();
        accounts.clickAdministration();
        accounts.selectAccountsTab();
    }

    @And("User searches the account {string} in which Destination to be created")
    public void userSearchesTheAccountInWhichDestinationToBeCreated(String account) {
        logger.info("Searching account for destination creation: {}", account);
        accounts.searchAccount(account);
        Assert.assertTrue("Reporting Tab is not displayed", accounts.isReportingTabDisplayed());
    }

    @And("User navigates to Reporting tab")
    public void userNavigatesToReportingTab() {
        logger.info("Navigating to Reporting tab");
        accounts.clickReportingTab();
    }

    @Then("User clicks Add Destination button")
    public void userClicksAddDestinationButton() {
        logger.info("User clicks Add Destination button");
        accounts.clickAddDestination();
    }

    @And("User enters Destination details - {string}, {string}, {string}, {string}")
    public void userEntersDestinationDetails(String destinationName, String destinationType, String hostName, String port) throws Exception {
        dimensionName = destinationName + CommonUtils.timeStampCalculation();
        logger.info("Entering Destination details. Name: {}, Type: {}", dimensionName, destinationType);
        accounts.enterDestinationName(dimensionName);
        accounts.selectDestinationType(destinationType);
        accounts.enterHostName(hostName);
        accounts.enterUserName(ConfigReader.getCustomDestinationUsername());
        accounts.enterPassword(ConfigReader.getCustomDestinationPassword());
        accounts.enterPortName(port);
    }

    @And("User clicks Test Connection link to verify if connection happened successfully")
    public void userClicksTestConnectionLinkToVerifyIfConnectionHappenedSuccessfully() {
        logger.info("Clicking Test Connection for destination");
        accounts.clickTestConnection();
    }

    @Then("User selects destination name created, and other details - {string}, {string}")
    public void userSelectsDestinationNameCreatedAndOtherDetails(String filePath, String fileName) {
        logger.info("Selecting custom destination: {}, FilePath: {}, FileName: {}", dimensionName, filePath, fileName);
        scheduleReport.enterCustomDestinationDetailsOnReportPanel(dimensionName, filePath, fileName);
    }

    @And("User saves the custom destination")
    public void userSavesTheCustomDestination() {
        logger.info("Saving custom destination");
        accounts.clickOKButton();
    }

    @And("User clicks PulsePoint icon to navigate back to Life")
    public void userClicksPulsePointIconToNavigateBackToLife() {
        logger.info("Navigating back to Life via PulsePoint icon");
        navigation.clickPulsePointLogo();
    }

    @And("User clicks Lifetime filter")
    public void userClicksLifetimeFilter() {
        logger.info("Clicking Lifetime filter");
        campaignDashboard.clickLifetimeFilter();
    }

    @And("User removes all the filters applied on the Dashboard")
    public void userRemovesAllTheFiltersAppliedOnTheDashboard() {
        logger.info("Removing all applied dashboard filters");
        campaignDashboard.ensureCampaignRadioBtnSelected();
        campaignDashboard.unselectFavoriteCheckboxIfSelected();
        campaignDashboard.unselectHideFinishedCheckboxIfSelected();
        campaignDashboard.resetFiltersIfApplied();
    }

    @Then("Verify the tabs displayed on the Pixels page")
    public void verifyTabsDisplayedOnPixelsPage() {
        logger.info("Verifying tabs on Pixels page");
        Assert.assertEquals("RETARGETING", pixels.verifyRetargetingTab().toUpperCase());
        Assert.assertEquals("SMART", pixels.verifySmartTab().toUpperCase());
        Assert.assertEquals("CONVERSION", pixels.verifyConversionTab().toUpperCase());
    }

    @Then("Verify the Advertiser dropdown and search box are displayed on the Pixels page")
    public void verifyAdvertiserDropdownAndSearchBoxDisplayed() {
        logger.info("Verifying Advertiser dropdown and Search box on Pixels page");
        Assert.assertTrue("Advertiser Dropdown is not visible", pixels.verifyAdvertiserDropdown());
        Assert.assertTrue("Search Box is not visible", pixels.verifySearchBox());
    }

    @When("User tries to save the Retargeting pixel without entering any details, an error message should be displayed")
    public void userTriesToSaveRetargetingPixelWithoutDetails() {
        logger.info("Validating error messages when saving Retargeting pixel without required fields");
        String pixelNameTemp = "Temporary Pixel Name";
        retargetingPixel.enterPixelName(pixelNameTemp);
        pixels.savePixel();
        Assert.assertEquals("Advertiser is required", retargetingPixel.advertiserError());
        retargetingPixel.clearPixelName();
        pixels.savePixel();
        Assert.assertEquals("Pixel Name is required", retargetingPixel.pixelNameError());
    }

    @And("User selects the {string} pixel")
    public void userSelectsPixel(String pixelType) {
        logger.info("Selecting pixel type: {}", pixelType);
        retargetingPixel.selectPixelType(pixelType);
    }

    @When("User edits the name of the created {string}")
    public void userEditsPixel(String pixelType) {
        pixelNameEdited = newPixelName + '_' + "Edited";
        logger.info("Editing {} name to {}", pixelType, pixelNameEdited);
        pixels.clickEditIcon();
        switch (pixelType) {
            case "Retargeting Pixel" -> retargetingPixel.enterPixelName(pixelNameEdited);
            case "Smart Pixel" -> smartPixel.enterPixelName(pixelNameEdited);
            case "Conversion Pixel" -> conversionPixel.enterPixelName(pixelNameEdited);
        }
        pixels.savePixel();
    }

    @Then("Verify the {string} gets updated successfully")
    public void verifyPixelUpdated(String pixelType) {
        logger.info("Verifying {} update success", pixelType);
        if (pixelType.equals("Retargeting Pixel") || pixelType.equals("Conversion Pixel")) {
            Assert.assertEquals("PIXEL UPDATED SUCCESSFULLY", pixels.verifyUpdateSuccess().toUpperCase());
        } else if (pixelType.equals("Smart Pixel")) {
            Assert.assertEquals("SAVED SUCCESSFULLY", pixels.verifyUpdateSuccess().toUpperCase());
        }
        pixels.searchSavedPixel(pixelNameEdited);
        Assert.assertEquals(pixelNameEdited, pixels.verifyCreatedPixel(pixelNameEdited));
    }

    @When("User removes the created pixel")
    public void userRemovesPixel() {
        logger.info("Removing created pixel");
        pixels.removePixel();
    }

    @Then("Verify the pixel gets removed successfully")
    public void verifyPixelRemoved() {
        logger.info("Verifying pixel removal success");
        Assert.assertEquals("Pixel deleted successfully", pixels.removeSuccess());
    }

    @When("User selects {string} as advertiser")
    public void userSelectsAdvertiser(String advertiser) {
        logger.info("Select advertiser: {}", advertiser);
        smartPixel.selectAdvertiser(advertiser);
    }

    @Then("Verify the Smart Pixel name is auto populated with {string} and Smart Pixel text")
    public void verifySmartPixelNameIsAutoPopulated(String advertiser) {
        logger.info("STEP: Verify Smart Pixel name auto-population for advertiser: {}", advertiser);
        String pixelName = smartPixel.getPixelNameFromHeader();
        logger.info("Fetched Smart Pixel name from UI: {}", pixelName);
        String expectedPixelName = advertiser + " Smart Pixel";
        String regex = "^\\Q" + expectedPixelName + "\\E(\\s\\d+)?$";
        Assert.assertTrue(pixelName.matches(regex));
        logger.info("Smart Pixel name verified successfully");
    }

    @And("User selects the associated campaign")
    public void userSelectsAssociatedCampaign() {
        logger.info("User selects the associated campaign");
        smartPixel.selectAssociatedCampaign();
    }

    @And("User adds the associated Smart List and enters list details as {string}")
    public void addsAssociatedSmartList(String listName) {
        logger.info("Adding associated Smart List with base name: {}", listName);
        smartPixel.clickAddSmartListButton();
        npiName = listName + '_' + CommonUtils.timeStampCalculation();
        logger.info("Generated Smart List name: {}", npiName);
        npiStaticList.enterListName(npiName);
    }

    @Then("Verify the selected {string} and Smart Pixel")
    public void verifySelectedAdvertiserAndSmartPixel(String advertiser) {
        logger.info("Verifying selected advertiser: {}", advertiser);
        Assert.assertEquals(advertiser, npiSmartList.verifySelectedAdvertiser());
        logger.info("Verifying selected Smart Pixel: {}", newPixelName);
        Assert.assertEquals(newPixelName, npiSmartList.verifySelectedSmartPixel());
        npiSmartList.clickLifeCheckbox();
    }

    @And("User selects the created Smart Pixel")
    public void userSelectsCreatedSmartPixel() {
        logger.info("Selecting created Smart Pixel: {}", newPixelName);
        pixels.selectSmartPixelTab();
        pixels.searchSavedPixel(newPixelName);
        pixels.openSearchedPixel(newPixelName);
    }

    @Then("Verify the selected Smart List should be reflected in the Associated Smartlists tab")
    public void verifySmartListReflectedInAssociatedTab() {
        logger.info("Verifying Smart List is reflected in Associated Smart Lists tab: {}", npiName);
        smartPixel.clickAssociatedSmartListsTab();
        Assert.assertEquals(npiName, smartPixel.verifyAssociatedSmartList(npiName));
    }

    @And("User navigates to the Pixel Codes tab")
    public void userNavigatesToPixelCodesTab() {
        logger.info("Navigating to Pixel Codes tab");
        smartPixel.clickPixelCodesTab();
        Assert.assertTrue(smartPixel.verifyPixelCodesTabIsSelected());
    }

    @Then("Verify user should not be able to deactivate the Smart Pixel if any Smart list is associated with it")
    public void verifyUserCannotDeactivateSmartPixelWithAssociatedSmartList() {
        logger.info("Verifying Smart Pixel cannot be deactivated when Smart List is associated");
        pixels.clickEditIcon();
        smartPixel.clickDeactivatePixelIcon();
        Assert.assertEquals("PIXEL CAN'T BE DEACTIVATED", smartPixel.verifyDeactivateError().toUpperCase());
    }

    @When("User deactivates the created pixel")
    public void userDeactivatesCreatedPixel() {
        logger.info("Deactivating pixel: {}", newPixelName);
        pixels.clickEditIcon();
        smartPixel.deactivatePixel();
    }

    @Then("Verify the pixel gets deactivated successfully")
    public void verifyPixelDeactivatedSuccessfully() {
        logger.info("Verifying pixel deactivation success message");
        Assert.assertTrue("Deactivate success message is not displayed", smartPixel.deactivateSuccess().contains("Pixel Deactivated successfully"));
    }

    @When("User tries to save the Conversion pixel without entering any details, an error message should be displayed")
    public void userTriesToSaveConversionPixelWithoutDetails() {
        logger.info("Validating mandatory field errors while saving Conversion Pixel");
        String pixelNameTemp = "Temporary Pixel Name";
        logger.info("Using temporary pixel name: {}", pixelNameTemp);
        conversionPixel.enterPixelName(pixelNameTemp);
        pixels.savePixel();
        logger.info("Verifying advertiser mandatory error");
        Assert.assertEquals("Advertiser is required", conversionPixel.advertiserError());
        conversionPixel.clearPixelName();
        pixels.savePixel();
        logger.info("Verifying pixel name mandatory error");
        Assert.assertEquals("Pixel Name is required", conversionPixel.pixelNameError());
        conversionPixel.enterPixelName(pixelNameTemp);
        logger.info("Selecting temporary advertiser to validate conversion type mandatory field");
        // Temporary hardcoded selection of advertiser to validate mandatory fields
        conversionPixel.selectAdvertiser("01- Advertiser");
        pixels.savePixel();
        Assert.assertEquals("Conversion Type is required", conversionPixel.pixelTypeOptionError());
        logger.info("Cancelling Conversion Pixel creation");
        pixels.clickCancelButton();
    }

    @Then("Verify the removed pixel should not be displayed in the pixel list")
    public void verifyRemovedPixelNotDisplayedInPixelList() {
        logger.info("Verifying removed pixel is not displayed: {}", pixelNameEdited);
        pixels.searchSavedPixel(pixelNameEdited);
        String noResultText = pixels.verifyDeletedPixel().toUpperCase();
        Assert.assertTrue(noResultText.equals("NOTHING FOUND...") || noResultText.equals("NOTHING FOUND"));
    }

    @Then("Verify the deactivated pixel should not be displayed in the pixel list")
    public void verifyDeactivatedPixelNotDisplayedInPixelList() {
        logger.info("Verifying deactivated pixel is not displayed in pixel list: {}", pixelNameEdited);
        pixels.searchSavedPixel(pixelNameEdited);
        String noResultText = pixels.verifyDeletedPixel().toUpperCase();
        logger.info("Pixel search result text: {}", noResultText);
        Assert.assertTrue(noResultText.equals("NOTHING FOUND...") || noResultText.equals("NOTHING FOUND"));
    }


    @And("User should be able to select the created template from the dropdown")
    public void userShouldBeAbleToSelectTheTemplateCreatedFromTheDropdown() {
        logger.info("Selecting created template from dropdown: {}", templateNameRandom);
        runReportPanel.selectTemplateFromDropdown(templateNameRandom);
        templateNameRandom = runReportPanel.fetchTemplateValue().get(0);
        logger.info("Fetched selected template value: {}", templateNameRandom);
        nameList.add(templateNameRandom);
    }

    @When("User selects {string} as rule type and selects the published Studio list")
    public void userSelectsRuleTypeAndSelectsThePublishedStudioList(String ruleType) {
        logger.info("Selecting rule type '{}' and published Studio list '{}'", ruleType, StudioSteps.workspaceName);
        itemCount = tacticSettings.selectRuleType(ruleType, StudioSteps.workspaceName);
        logger.info("Item count returned after rule selection: {}", itemCount);
    }

    @Then("Verify the selected targeting rule {string} and rule option")
    public void verifyTheSelectedTargetingRuleAndRuleOption(String ruleType) {
        logger.info("Verifying selected targeting rule and rule option");
        Assert.assertEquals(ruleType, tacticSettings.verifyRuleType());
        Assert.assertEquals(StudioSteps.workspaceName, tacticSettings.verifyRuleOption());
        logger.info("Targeting rule '{}' and rule option '{}' verified successfully", ruleType, StudioSteps.workspaceName);
    }

    // The methods below are slight variations of existing ones used to navigate to Life, HCP and Studio from the Admin landing page after login.
    // These are specifically defined to navigate back to Life, HCP and Studio from other modules.
    @And("User navigates to {string} application")
    public void userNavigatesToApplication(String application) {
        logger.info("Navigating back to application: {}", application);
        switch (application.toLowerCase()) {
            case "life":
                navigation.navigateBackToLife();
                break;
            case "hcp":
                navigation.navigateBackToHCP();
                break;
            case "studio":
                navigation.navigateBackToStudio();
                break;
            default:
                logger.warn("Unknown application requested: {}", application);
        }
    }

    @And("Verify Line Item page has below tabs")
    public void verifyLineItemPageHasBelowTabs(DataTable dataTable) {
        List<String> tabNames = dataTable.asList(String.class);
        logger.info("Verifying Line Item page tabs: {}", tabNames);
        Assert.assertTrue("Line Item tabs are not available", lineItemDetails.verifyLineItemTabs(tabNames));
    }

    @And("Verify status of line item is Incomplete when there are no tactics under the line item")
    public void verifyStatusOfLineItemIsIncompleteWhenThereAreNoTacticsUnderTheLineItem() {
        logger.info("Verifying Line Item status when no tactics are present");
        Assert.assertEquals("Incomplete", lineItemDetails.verifyLineItemStatus());
        Assert.assertEquals("Campaign is enabled . Tactic is Incomplete.", lineItemDetails.fetchIncompleteStatusToolTip());
    }

    @When("User fills in required details {string} except for flight information and save")
    public void userFillsInRequiredDetailsExceptForFlightInformation(String lineItemName) {
        lineItemNameRandom = lineItemName + CommonUtils.timeStampCalculation();
        logger.info("Entering Line Item name without flight details: {}", lineItemNameRandom);
        lineItemDetails.enterLineItemName(lineItemNameRandom);
        lineItemDetails.saveLineItem();
    }

    @Then("User should see an error message to add flight details")
    public void userShouldSeeAnErrorMessageToAddFlightDetails() {
        logger.info("Verifying error message for missing flight details");
        Assert.assertEquals("LineItem Flight is required.", lineItemDetails.fetchErrorAlert());
    }

    @And("User clicks Add Flight button")
    public void userClicksAddFlightButton() {
        logger.info("Clicking Add Flight button");
        lineItemDetails.clickAddFlightButton();
    }

    @And("Verify if user enters flight budget that exceeds Campaign budget")
    public void verifyIfUserEntersFlightBudgetThatExceedsCampaignBudget() {
        String unaccountedBudget = lineItemDetails.fetchCampaignBudget();
        String modifiedBudget = String.valueOf(Integer.parseInt(unaccountedBudget) + 1000);
        logger.info("Entering flight budget exceeding campaign budget. Campaign: {}, Entered: {}", unaccountedBudget, modifiedBudget);
        lineItemDetails.enterLineItemBudget(modifiedBudget);
        lineItemDetails.saveLineItem();
    }

    @Then("User should see error message when tries to save line item page")
    public void userShouldSeeErrorMessageWhenTriesToSaveLineItemPage() {
        logger.info("Verifying error message when flight budget exceeds campaign budget");
        Assert.assertTrue("The total flight budget is exceeded", lineItemDetails.fetchErrorAlert().contains("The total flight budget could not exceed"));
    }

    @And("User adds the flight details - Flight Start Date, Flight End Date, {string}")
    public void userAddsTheFlightDetailsFlightStartDateFlightStartDate(String budget) {
        logger.info("Adding flight details with budget: {}", budget);
        lineItemDetails.enterLineItemBudget(budget);
        flightStartDate = lineItemDetails.selectStartDateOfFlight();
        flightEndDate = lineItemDetails.selectEndDateOfFlight();
        logger.info("Selected flight dates → Start: {}, End: {}", flightStartDate, flightEndDate);
    }

    @And("User adds new flight and enter overlapping flight details - Flight Start Date, Flight End Date, {string}")
    public void userAddsOverlappingFlightDetailsFlightStartDateFlightStartDate(String budget) {
        logger.info("Adding overlapping flight with budget: {}", budget);
        lineItemDetails.clickAddFlightButton();
        lineItemDetails.enterLineItemBudget(budget);
        lineItemDetails.selectOverlappingFlightDates(flightStartDate, flightEndDate);
        lineItemDetails.saveLineItem();
    }

    @And("User should see error message when tries to save line item page and dates fields should get highlighted with inline error message")
    public void userShouldSeeErrorMessageWhenTriesToSaveLineItemPageAndDatesFieldsShouldGetHighlighted() {
        logger.info("Verifying overlapping flight error message and inline validation");
        Assert.assertTrue("LineItem flights overlap message is not displayed", lineItemDetails.fetchErrorAlert().contains("LineItem flights overlap."));
        Assert.assertEquals("Flight overlap with other flights.", lineItemDetails.fetchInlineErrorMessage());
    }

    @When("User enters line item details {string}")
    public void userEntersLineItemDetails(String lineItemName) {
        lineItemNameRandom = lineItemName + CommonUtils.timeStampCalculation();
        logger.info("Entering Line Item name: {}", lineItemNameRandom);
        lineItemDetails.enterLineItemName(lineItemNameRandom);
    }

    @And("User adds {string} flights, fills in the details with {string} for each flight section, and saves the line item")
    public void userAddsMultipleFlightsAndFillsInDetailsForEachFlightSection(String noOfFlights, String budget) {
        logger.info("Adding {} flights with budget {} for each flight", noOfFlights, budget);
        lineItemDetails.addMultipleFlights(noOfFlights, budget);
    }

    @And("User generates sequential flights for the line item using {string} and {string}")
    public void userGeneratesSequentialFlightsToALineItem(String budget, String numberOfMonths) {
        logger.info("Generating sequential flights with budget {} for {} months", budget, numberOfMonths);
        capturedDetails = lineItemDetails.generateSequentialFlights(budget, numberOfMonths);
        logger.info("Generated sequential flight details: {}", capturedDetails);
    }

    @And("Verify that Sequential flights should be added based on the start month")
    public void verifyThatSequentialFlightsShouldBeAddedBasedOnTheStartMonth() {
        logger.info("Verifying sequential flights based on start month");
        String[] parts = capturedDetails.get(0).split(" ");
        Month startMonth = Month.valueOf(parts[0].toUpperCase(Locale.ENGLISH));
        int startYear = Integer.parseInt(parts[1]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        for (int i = 1; i < capturedDetails.size(); i++) {
            String dateStr = capturedDetails.get(i);
            LocalDate actualDate = LocalDate.parse(dateStr, formatter);
            LocalDate expectedDate = LocalDate.of(startYear, startMonth, 1).plusMonths(i - 1);
            if (actualDate.getMonthValue() != expectedDate.getMonthValue() || actualDate.getYear() != expectedDate.getYear()) {
                logger.error("Flight date mismatch. Expected: {}, Actual: {}", expectedDate, actualDate);
                Assert.assertEquals("Flight date mismatch ", actualDate, expectedDate);
            }
        }
    }

    @And("User fetches all the flight details added")
    public void userFetchesAllTheFlightDetailsAdded() {
        logger.info("Saving Line Item and fetching all flight details");
        lineItemDetails.saveLineItem();
        lineItemDetails.navigateToLineItemDetails(lineItemNameRandom);
        lineItemDetails.clickDetailsTab();
        itemList = lineItemDetails.fetchFlightDetails();
        logger.info("Fetched flight details: {}", itemList);
    }

    @Then("User navigates to the Flights tab and verifies the flight details")
    public void userNavigatesToTheFlightsTabAndVerifiesTheFlightDetails() {
        logger.info("Navigating to Flights tab and verifying flight details");
        List<String> flightDetails;
        lineItemFlights.clickFlightTab();
        Assert.assertTrue("Flight details are not displayed", lineItemFlights.isFlightTableDisplayed());
        flightDetails = lineItemFlights.fetchFlightDetailsFromFlightTab();
        logger.info("Flight details from Flights tab: {}", flightDetails);
        for (String expected : itemList) {
            boolean matchFound = flightDetails.stream().anyMatch(actual -> actual.contains(expected));
            Assert.assertTrue("Expected value not found in flight tab: " + expected, matchFound);
        }
        capturedDetails.clear();
        capturedDetails = flightDetails;
    }

    @When("User deletes some flight entries")
    public void userDeletesSomeFlightEntries() {
        logger.info("Deleting some flight entries");
        lineItemDetails.clickDetailsTab();
        lineItemDetails.deleteFlightEntry();
        lineItemDetails.saveLineItem();
        itemList = lineItemDetails.fetchFlightDetails();
        logger.info("Remaining flight details after deletion: {}", itemList);
    }

    @Then("User should see the remaining flights listed under the Flights section")
    public void userShouldSeeTheRemainingFlightsListedUnderTheFlightsSection() {
        logger.info("Verifying remaining flights after deletion");
        List<String> flightDetailsAfterDeletion;
        lineItemFlights.clickFlightTab();
        Assert.assertTrue("Flight details are not displayed", lineItemFlights.isFlightTableDisplayed());
        flightDetailsAfterDeletion = lineItemFlights.fetchFlightDetailsFromFlightTab();
        logger.info("Flight details after deletion: {}", flightDetailsAfterDeletion);
        for (String expected : itemList) {
            boolean matchFound = flightDetailsAfterDeletion.stream().anyMatch(actual -> actual.contains(expected));
            Assert.assertTrue("Expected value not found in flight tab after flight deletion: " + expected, matchFound);
        }
        Assert.assertNotEquals("Flight details did not change after deletion – deletion may have failed.", capturedDetails, flightDetailsAfterDeletion);
    }

    @When("User creates line items with below line types and other details, enables the line item and saves the changes")
    public void userEntersTheLineItemDetailsWithDifferentLineTypesEnablesTheLineItemAndSavesTheChanges(DataTable dataTable) {
        logger.info("Creating multiple Line Items with different line types");
        nameList.clear();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        int currentRowIndex = 0;
        int totalRows = rows.size();
        for (Map<String, String> row : rows) {
            String type = row.get("LINE_TYPE").trim();
            String attributes = row.get("LINE_ITEM_DETAILS").trim();
            Map<String, String> attributeMap = Arrays.stream(attributes.split(",")).map(String::trim).map(entry -> entry.split(":", 2)).collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            lineItemNameRandom = attributeMap.get("LineName") + "_" + type + "_" + CommonUtils.timeStampCalculation();
            logger.info("Creating Line Item: {} of type {}", lineItemNameRandom, type);
            nameList.add(lineItemNameRandom);
            lineItemDetails.createLineItem(type, lineItemNameRandom, attributeMap);
            Assert.assertEquals("Lineitem " + lineItemNameRandom + " created.", lineItemDetails.lineItemSuccess());
            List<String> lineItemLabelList = lineItemDetails.fetchLineItemName();
            Assert.assertTrue("Line Item '" + lineItemNameRandom + "' is not available", lineItemLabelList.stream().anyMatch(item -> item.equalsIgnoreCase(lineItemNameRandom)));
            lineItemDetails.cancelTactic();
            if (currentRowIndex < totalRows - 1) {
                lineItemDetails.selectNewLineItem();
            }
            currentRowIndex++;
        }
    }

    @Then("User adds Comments or Notes {string} to each line item")
    public void userAddsCommentsOrNotesToEachLineItem(String notes) {
        logger.info("Adding comments/notes to each Line Item");
        for (String name : nameList) {
            lineItemDetails.navigateToLineItemDetails(name);
            String newNotes = name + " " + notes;
            logger.info("Adding notes to {}: {}", name, newNotes);
            itemList.add(newNotes);
            Assert.assertEquals("Notes saved successfully.", lineItemDetails.addNotesToLineItem(newNotes));
        }
    }

    @And("Verify the notes added to each line item")
    public void verifyTheNotesAddedToEachLineItem() {
        logger.info("Verifying notes added to each Line Item");
        for (String name : nameList) {
            lineItemDetails.navigateToLineItemDetails(name);
            String notes = lineItemDetails.fetchLineItemNotes();
            logger.info("Fetching notes added to Line Item {}: {}", notes, name);
            Assert.assertTrue("Note of '" + name + "' is not available", itemList.stream().anyMatch(item -> item.equalsIgnoreCase(notes)));
        }
    }

    @And("Verify Bulk Edit Mode successfully {string} multiple selected line items")
    public void verifyBulkEditModeWorksForDisablingMultipleLineItems(String bulkOperations) {
        logger.info("Verifying Bulk Edit Mode operation: {}", bulkOperations);
        lineItemDetails.clickBulkEditMode();
        logger.info("Entered Bulk Edit Mode");
        for (String name : nameList) {
            logger.info("Selecting line item '{}' for bulk operation", name);
            lineItemDetails.selectLineItemUsingBulkEdit(name);
        }
        Assert.assertEquals("Lineitems status updated successfully", lineItemDetails.performBulkModeOperationsOnLineItems(bulkOperations));
        lineItemDetails.exitBulkEditMode();
        logger.info("Exited Bulk Edit Mode");
    }

    @And("Verify that each selected line item is {string}")
    public void verifyThatEachSelectedLineItemIsDisabled(String label) {
        logger.info("Verifying that each selected line item is '{}'", label);
        for (String name : nameList) {
            logger.info("Navigating to line item details for '{}'", name);
            lineItemDetails.navigateToLineItemDetails(name);
            boolean isDisabled = lineItemDetails.checkIfEachLineItemEnabledOrDisabled(label);
            logger.info("Line item '{}' enabled/disabled status: {}", name, isDisabled);
            Assert.assertTrue(name + " is not " + label + " using Bulk Edit Mode", isDisabled);
        }
    }

    @And("Verify user is able to create a copy of the line items using {string} option")
    public void verifyUserIsAbleToCreateACopyOfTheLineItems(String lineItemOption) {
        logger.info("Verifying that user can create a copy of line items using '{}' option", lineItemOption);
        itemList.clear();
        List<String> originalLineItemDetails;
        List<String> copiedLineItemDetails;
        for (String name : nameList) {
            logger.info("Processing line item '{}'", name);
            lineItemDetails.navigateToLineItemDetails(name);
            lineItemDetails.clickDetailsTab();
            originalLineItemDetails = lineItemDetails.fetchLineItemDetails();
            logger.info("Fetched original line item details: {}", originalLineItemDetails);
            lineItemDetails.clickLineItemOptions(lineItemOption);
            String lineItemName = "Copy of " + name;
            itemList.add(lineItemName);
            Assert.assertEquals("Line Item copied successfully.", lineItemDetails.createACopyOfLineItem(lineItemName));
            logger.info("Created copy of line item '{}'", lineItemName);
            Assert.assertTrue("Copied Line Item is not available", lineItemDetails.verifyLineItemAvailable(lineItemName));
            lineItemDetails.navigateToLineItemDetails(lineItemName);
            lineItemDetails.clickDetailsTab();
            copiedLineItemDetails = lineItemDetails.fetchLineItemDetails();
            logger.info("Fetched copied line item details: {}", copiedLineItemDetails);
            lineItemDetails.clickOverviewTab();
            Assert.assertEquals("Line item details do not match after copy.", originalLineItemDetails, copiedLineItemDetails);
        }
    }

    @And("Verify {string} option opens the Run report screen for user and run the report for {string}")
    public void verifyOptionOpensTheRunReportScreenForUser(String lineItemOption, String templateName) {
        logger.info("Verifying '{}' option opens Run Report screen and running report for '{}'", lineItemOption, templateName);
        for (String name : nameList) {
            logger.info("Navigating to line item '{}'", name);
            lineItemDetails.navigateToLineItemDetails(name);
            lineItemDetails.clickLineItemOptions(lineItemOption);
            lineItemDetails.runReportFromLineItemPage();
            runReportPanel.selectTemplateFromDropdown(templateName);
            String fileName = "Custom Report";
            runReportPanel.clickRunButton(fileName);
            String actualMessage = runReportPanel.fetchSuccessAlert();
            logger.info("Report run success message: '{}'", actualMessage);
            Assert.assertTrue("Unexpected success message: " + actualMessage, actualMessage.equals("Success!") || actualMessage.equals("You will get the report on your email"));
        }
    }

    @And("Verify that the reports generated on the Line Item page are available on the Generate Report page")
    public void verifyThatTheReportsGeneratedOnTheLineItemPageAreAvailableOnTheGenerateReportPage() {
        logger.info("Verifying reports generated on Line Item page are available on Generate Report page");
        navigation.clickSubMenu();
        navigation.clickMenuAngle();
        navigation.clickGeneratedReport();
        runReportPanel.clickSearchButton();
        for (String name : nameList) {
            boolean reportAvailable = reportTemplates.verifyReportGeneratedFromLineItemPage(name);
            logger.info("Report availability for line item '{}': {}", name, reportAvailable);
            Assert.assertTrue("Report generated using line item " + name + " is not available", reportAvailable);
        }
    }

    @And("Verify {string} is available for each item, and deleted items are removed from the Left menu")
    public void isAvailableForEachItemAndDeletedItemsAreRemovedFromTheLeftMenu(String lineItemOption) {
        logger.info("Verifying '{}' option availability and deletion for each item", lineItemOption);
        for (String name : itemList) {
            logger.info("Navigating to line item name '{}'", name);
            lineItemDetails.navigateToLineItemDetails(name);
            lineItemDetails.clickLineItemOptions(lineItemOption);
            lineItemDetails.performDeleteOperation();
            List<String> lineItemLabelList = lineItemDetails.fetchLineItemName();
            logger.info("Remaining line items after deletion: {}", lineItemLabelList);
            Assert.assertFalse("Line Item '" + name + "' is still available after performing Delete Operation", lineItemLabelList.stream().anyMatch(item -> item.equalsIgnoreCase(name)));
        }
    }

    //* Rajyalaxmi - Tactic max bid and base bid verification
    @When("User clicks on Campaign Settings")
    public void user_clicks_on_campaign_settings() {
        logger.info("Clicking on Campaign Settings link and navigating to Bid Settings tab");
        campaignSettings.campaignSettingsLink();
        campaignSettings.bidSettingsTab();
    }

    @Then("Verify user is on default bid settings page")
    public void verify_user_is_on_default_bid_settings_page() {
        String defaultSettings = campaignSettings.getDefaultSettings();
        logger.info("Verifying default bid settings page, found: '{}'", defaultSettings);
        Assert.assertEquals("Default Bid Settings", defaultSettings);
    }

    @Then("User gets Max Bid and Base Bid values")
    public void user_gets_max_bid_and_base_bid_values() {
        campaignBaseBid = (campaignSettings.getBaseBidPrice());
        campaignMaxBid = (campaignSettings.getMaxBidPrice());
        logger.info("Fetched Campaign Base Bid: {}, Max Bid: {}", campaignBaseBid, campaignMaxBid);
    }

    @Then("Verify Max Bid and Base Bid values on the tactic settings match with Campaign Settings values")
    public void verify_max_bid_and_base_bid_values_on_the_tactic_settings_match_with_campaign_settings_values() {
        BigDecimal tacticBaseBid = (tacticSettings.getTacticBaseBidPrice()).stripTrailingZeros();
        BigDecimal tacticMaxBid = (tacticSettings.getTacticMaxBidPrice()).stripTrailingZeros();
        logger.info("Verifying tactic settings: Base Bid={}, Max Bid={}", tacticBaseBid, tacticMaxBid);
        Assert.assertEquals("Max Bid did not match", campaignMaxBid, tacticMaxBid);
        Assert.assertEquals("Base Bid did not match", campaignBaseBid, tacticBaseBid);
    }

    @Then("User creates a new tactic with details {string} {string}")
    public void user_creates_a_new_tactics(String tacticName, String channel) {
        logger.info("Creating new tactic '{}' for channel '{}'", tacticName, channel);
        tacticDetails.enterTacticName(tacticName);
        tacticDetails.saveTacticDetails();
    }

    @Then("User deletes the tactic {string} and verifies it")
    public void user_deletes_the_tactic_and_verifies_it(String tacticName) {
        logger.info("Deleting tactic '{}'", tacticName);
        tacticDetails.deleteTactic();
        String currentTacticName = tacticSettings.verifyTacticName();
        logger.info("Current tactic after deletion: '{}'", currentTacticName);
        Assert.assertNotEquals(tacticName, currentTacticName);
        tacticDetails.globalSearchDeletedTactic(tacticName);
        String searchText = tacticDetails.getSearchText();
        logger.info("Search text for deleted tactic '{}': '{}'", tacticName, searchText);
        Assert.assertEquals("Nothing found...", searchText);
    }

    @And("User enables tactic {string} through bulk action and verifies the status")
    public void userEnableAllTacticsThroughBulkActionAndVerifiesTheStatus(String tacticName) {
        logger.info("Enabling tactic '{}' through bulk action", tacticName);
        tacticDetails.bulkEnableTactics(tacticName);
        boolean isEnabled = tacticDetails.getToggleClass(tacticName);
        logger.info("Tactic '{}' enabled status: {}", tacticName, isEnabled);
        Assert.assertTrue(isEnabled);

    }

    @When("User clicks on create new Campaign")
    public void userClicksOnCreateNewCampaign() {
        logger.info("Clicking on create new Campaign");
        campaigns.createCampaign();
    }

    @Then("Verify Smart List Creation Panel should display the following List Population Options")
    public void verifySmartListCreationPanelShouldDisplayTheFollowingListPopulationOptions(DataTable dataTable) {
        List<String> listPopulationOptions = dataTable.asList(String.class);
        logger.info("Verifying Smart List Population Options: {}", listPopulationOptions);
        for (String option : listPopulationOptions) {
            Assert.assertTrue("List Population Option - " + option + " is not available in Smart List Container", npiSmartList.verifyListPopulationOptions(option.trim()));
        }
    }

    @And("User selects the {string} and fetches Smart pixel list")
    public void userSelectsTheAndFetchesSmartPixelList(String advertiser) {
        logger.info("Selecting Smart Pixel tab and advertiser '{}'", advertiser);
        pixels.selectSmartPixelTab();
        pixels.selectAdvertiser(advertiser);
        itemList = pixels.fetchPixelsList();
        logger.info("Fetched Smart Pixel list: {}", itemList);
    }

    @And("User enters the Smart NPI list details as {string} {string}")
    public void userEntersTheSmartNPIListDetailsAsFor(String npiListName, String advertiser) {
        npiName = npiListName + '_' + CommonUtils.timeStampCalculation();
        logger.info("Entering Smart NPI list details: Name='{}', Advertiser='{}'", npiName, advertiser);
        npiSmartList.enterListName(npiName);
        npiSmartList.selectAdvertiser(advertiser);
        npiSmartList.clickLifeCheckbox();
    }

    @And("User selects Smart NPI list as {string}")
    public void userSelectsSmartNPIList(String smartListType) {
        logger.info("Selecting Smart NPI list type '{}'", smartListType);
        npiSmartList.selectSmartNPIListType(smartListType);
    }

    @And("User verifies the Smart Pixel dropdown displays all Smart Pixels for the selected advertiser and select the pixel")
    public void userVerifiesTheSmartPixelDropdownDisplaysAllSmartPixelsForTheSelectedAdvertiser() {
        logger.info("Verifying Smart Pixel dropdown values match fetched pixel list");
        npiSmartList.clickSmartPixelDropDown();
        List<String> list = npiSmartList.fetchSmartPixelDropdownValue();
        logger.info("Dropdown values: {}", list);
        Assert.assertTrue("Pixel list doesn't match", itemList.retainAll(list));
        npiSmartList.selectSmartPixelDropdownValue();
    }

    @And("User selects Engagement Type {string} and enter related details {string}, {string}, {string}")
    public void userSelectsEngagementType(String engagementType, String visitedURL, String ignoredURL, String keywords) {
        logger.info("Selecting engagement type '{}' with visitedURL='{}', ignoredURL='{}', keywords='{}'", engagementType, visitedURL, ignoredURL, keywords);
        List<String> visitedUrlList = CommonUtils.parseCommaSeparatedString(visitedURL);
        List<String> ignoredUrlList = CommonUtils.parseCommaSeparatedString(ignoredURL);
        List<String> keywordList = CommonUtils.parseCommaSeparatedString(keywords);

        npiSmartList.selectEngagementType(engagementType);
        switch (engagementType) {
            case "Engaged on Site":
                npiSmartList.addVisitedURL(visitedUrlList);
                npiSmartList.addIgnoredURL(ignoredUrlList);
                break;
            case "Engaged via Search":
                npiSmartList.clickSearchKeywordsCheckbox();
                npiSmartList.enterSearchKeywords(keywordList);
                break;
            case "Engaged Anywhere":
                npiSmartList.addVisitedURL(visitedUrlList);
                npiSmartList.addIgnoredURL(ignoredUrlList);
                npiSmartList.clickSearchKeywordsCheckbox();
                npiSmartList.enterSearchKeywords(keywordList);
                break;
        }
    }

    @And("User retrieves all the entered data before saving the list {string}")
    public void userRetrievesAllTheEnteredDataBeforeSavingTheList(String listType) {
        logger.info("Retrieving entered data before saving list '{}'", listType);
        if (listType.contains(",")) {
            List<String> listTypes = CommonUtils.parseCommaSeparatedString(listType);
            for (String list : listTypes) {
                capturedDetails.addAll(npiSmartList.retrieveEnteredData(list));
            }
        } else {
            capturedDetails = npiSmartList.retrieveEnteredData(listType);
        }
        logger.info("Captured details: {}", capturedDetails);
    }

    @And("User saves the Smart List and verifies the successful creation of the list")
    public void theUserSavesTheSmartListAndVerifiesTheSuccessfulCreationOfTheList() {
        logger.info("Saving Smart NPI list '{}'", npiName);
        npiSmartList.clickSaveButton();
        String alert = npiSmartList.fetchSuccessAlert();
        logger.info("Success alert: '{}'", alert);
        Assert.assertEquals("NPI list created successfully", alert);
    }

    @And("Verify that the retrieved data for the {string} list was saved correctly")
    public void verifyThatTheRetrievedDataForTheListWasSavedCorrectly(String listType) {
        logger.info("Verifying retrieved data for list '{}'", listType);
        List<String> onListSavedFetchData = new ArrayList<>();
        if (listType.contains(",")) {
            List<String> listTypes = CommonUtils.parseCommaSeparatedString(listType);
            for (String list : listTypes) {
                onListSavedFetchData.addAll(npiSmartList.retrieveEnteredData(list));
            }
        } else {
            onListSavedFetchData = npiSmartList.retrieveEnteredData(listType);
        }
        logger.info("Data fetched after save: {}", onListSavedFetchData);
        Assert.assertEquals("Data entered doesn't match after saving the list", capturedDetails, onListSavedFetchData);
    }

    @And("Verify {string} population option is disabled when Advertiser value is not selected")
    public void verifyNPIListPopulationOptionIsDisabledWhenAdvertiserValueIsNotSelected(String smartListType) {
        logger.info("Verifying that '{}' population option is disabled when Advertiser is not selected", smartListType);
        Assert.assertTrue("NPI List is not disabled", npiSmartList.verifyNPIListDisabled(smartListType));
    }

    @And("User selects the HCP switch {string}")
    public void userSelectsTheHCPSwitch(String hcpSwitch) {
        logger.info("Selecting HCP switch '{}'", hcpSwitch);
        npiSmartList.selectHCPSwitch(hcpSwitch);
    }

    @And("User selects the NPI Group {string}")
    public void userSelectsTheNPIGroup(String npiList) {
        logger.info("Selecting NPI Group '{}'", npiList);
        npiSmartList.selectNPIGroup(npiList);
    }

    @And("User selects the Speciality {string}")
    public void userSelectsTheSpeciality(String specialities) {
        List<String> specialityList = CommonUtils.parseCommaSeparatedString(specialities);
        logger.info("Selecting Specialities: {}", specialityList);
        for (String speciality : specialityList) {
            npiSmartList.selectSpeciality(speciality);
        }
    }

    @And("User selects the Profession {string}")
    public void userSelectsTheProfession(String professions) {
        List<String> professionList = CommonUtils.parseCommaSeparatedString(professions);
        logger.info("Selecting Professions: {}", professionList);
        for (String profession : professionList) {
            npiSmartList.selectProfession(profession);
        }
    }

    @And("Verify that Recency is set to {string} by default for {string}")
    public void verifyThatRecencyIsSetToByDefault(String recency, String type) {
        String currentRecency = npiSmartList.fetchRecency(type);
        logger.info("Verifying Recency for type '{}': expected='{}', actual='{}'", type, recency, currentRecency);
        Assert.assertEquals(recency, currentRecency);
    }

    @And("verify that Decile is set to {string} by default for {string}")
    public void verifyThatDecileIsSetToByDefault(String decile, String type) {
        String fetchedDecile = npiSmartList.fetchDecile(type);
        logger.info("Verifying Decile for type '{}': expected='{}', actual='{}'", type, decile + " decile", fetchedDecile);
        Assert.assertEquals(decile + " decile", fetchedDecile);
    }

    @And("User selects {string} from {string} dropdown")
    public void userSelectsTheDiagnosis(String options, String type) {
        List<String> optionList = CommonUtils.parseCommaSeparatedString(options);
        logger.info("Selecting options '{}' from dropdown '{}'", optionList, type);
        for (int i = 0; i < optionList.size(); i++) {
            npiSmartList.selectValueFromClinicalDropdown(optionList.get(i), type);
            if (i < optionList.size() - 1) {
                npiSmartList.clickAddButton(type);
                logger.info("Clicked Add button for '{}' in '{}'", optionList.get(i), type);
            }
        }
    }

    @And("User clicks Browse button to upload {string} file {string}")
    public void userClicksBrowseButtonToUploadDiagnosisFile(String type, String fileName) {
        logger.info("Uploading file '{}' for type '{}'", fileName, type);
        npiSmartList.browseBulkUploadTemplate(type, fileName);
    }


    @And("Verify that Prescribed Behavior Change should display below tabs")
    public void verifyThatPrescribedBehaviorChangeShouldDisplayDroppersAndNewPrescribersTabs(DataTable dataTable) {
        List<String> tabList = dataTable.asList(String.class);
        logger.info("Verifying Prescribed Behavior Change tabs: {}", tabList);
        for (String tabName : tabList) {
            Assert.assertTrue(tabName + " is not available", npiSmartList.fetchPrescriptionBehaviourTab(tabName));
        }
    }

    @And("Verify that {string} tab should be selected by default")
    public void verifyThatDroppersTabShouldBeSelectedByDefault(String defaultTabName) {
        logger.info("Verifying default tab selection: '{}'", defaultTabName);
        Assert.assertTrue("Droppers is not a default selection", npiSmartList.fetchDefaultPrescriptionBehaviourTab(defaultTabName));
    }

    @And("Verify that Top Droppers percentage slider should range from {string} to {string} and should be set to {string} by default")
    public void topDroppersPercentageSliderShouldRangeFromToAndShouldBeSetToByDefault(String topDropperMin, String topDropperMax, String topDropperDefault) {
        logger.info("Verifying Top Droppers slider: min='{}', max='{}', default='{}'", topDropperMin, topDropperMax, topDropperDefault);
        Assert.assertTrue("Top Dropper range is not set from 1 to 100%", npiSmartList.fetchTopDropperMinAndMaxValues(topDropperMin, topDropperMax));
        Assert.assertEquals(topDropperDefault, npiSmartList.fetchTopDropperDefaultValue());
    }

    @And("User selects the {string} value as {string} from the slider")
    public void userSelectsTheValueFromTheSlider(String sliderType, String sliderValue) {
        logger.info("Selecting '{}' value '{}' from slider", sliderType, sliderValue);
        npiSmartList.selectDropperValueFromSlider(sliderType, sliderValue);
    }

    @And("Verify that Time Frame Selector slider should range from {string} to {string} months and should be set to {string} by default")
    public void verifyThatTimeFrameSelectorSliderShouldRangeFromToMonthsAndShouldBeSetToByDefault(String timeframeSelectorMin, String timeframeSelectorMax, String timeframeSelectorDefault) {
        logger.info("Verifying Time Frame Selector slider: min='{}', max='{}', default='{}'", timeframeSelectorMin, timeframeSelectorMax, timeframeSelectorDefault);
        Assert.assertTrue("Time Frame Selector range is not set from 6 to 12 months", npiSmartList.fetchTimeframeSelectorMinAndMaxValues(timeframeSelectorMin, timeframeSelectorMax));
        Assert.assertEquals(timeframeSelectorDefault, npiSmartList.fetchTimeframeSelectorDefaultValue());
    }

    @And("User selects the prescription drug name {string}")
    public void userSelectsThePrescriptionDrugName(String drugNames) {
        List<String> drugList = CommonUtils.parseCommaSeparatedString(drugNames);
        logger.info("Selecting prescription drugs: {}", drugList);
        for (String drug : drugList) {
            npiSmartList.selectPrescriptionDrug(drug);
        }
    }

    @And("User selects the {string} tab under Prescription Behavior Change")
    public void userSelectsTheTabUnderPrescriptionBehaviorChange(String tabName) {
        logger.info("Selecting Prescription Behavior Change tab '{}'", tabName);
        npiSmartList.clickPrescriptionBehaviourTab(tabName);
    }

    @And("User selects Engagement Type {string} and contextual category {string}")
    public void userSelectsEngagementTypeAndContextualCategory(String engagementType, String contextualCategory) {
        logger.info("Selecting Engagement Type '{}' and Contextual Category '{}'", engagementType, contextualCategory);
        npiSmartList.selectEngagementTypeAndContextualCategory(engagementType, contextualCategory);
    }

    @And("User clicks MESH dropdown, enters {string} and selects it")
    public void userClicksMESHDropdownEntersAndSelectsIt(String meshCondition) {
        logger.info("Selecting MESH condition '{}'", meshCondition);
        npiSmartList.selectMESHCondition(meshCondition);
    }

    @And("Verify that Recency slider should range from {string} to {string} days and should be set to {string} by default")
    public void verifyThatRecencySliderShouldRangeFromToDaysAndShouldBeSetToByDefault(String recencyMin, String recencyMax, String recencyDefault) {
        logger.info("Verifying Recency slider: min='{}', max='{}', default='{}'", recencyMin, recencyMax, recencyDefault);
        Assert.assertTrue("Recency range is not set from 1 to 60 days", npiSmartList.fetchRecencyMinAndMaxValues(recencyMin, recencyMax));
        Assert.assertEquals(recencyDefault, npiSmartList.fetchRecencyDefaultValue());
    }

    @And("User checks Prime list with historical data check box")
    public void userChecksPrimeListWithHistoricalDataCheckBox() {
        logger.info("Selecting Prime list with historical data checkbox");
        npiSmartList.selectPrimeListWithHistoricalDataCheckbox();
    }

    @And("User hovers over the {string} question icon and fetches tool-tip {string}")
    public void userHoversOverTheMedscapeQuestionIconAndFetchesToolTipFor(String contextualCategory, String tooltip) {
        String actualTooltip = npiSmartList.hoverAndFetchTooltip(contextualCategory);
        logger.info("Hovering over '{}' icon: expected tooltip='{}', actual='{}'", contextualCategory, tooltip, actualTooltip);
        Assert.assertEquals(tooltip, actualTooltip);
    }

    @And("User clicks {string} primary concept dropdown, enters {string} and selects it")
    public void userClicksMedscapePrimaryConceptDropdownEntersAndSelectsIt(String contextualCategory, String primaryConcept) {
        List<String> primaryConceptList = CommonUtils.parseCommaSeparatedString(primaryConcept);
        logger.info("Selecting primary concepts '{}' under '{}'", primaryConceptList, contextualCategory);
        for (String concept : primaryConceptList) {
            npiSmartList.selectMedscapePrimaryConcept(contextualCategory, concept);
        }
    }

    @And("Verify that {string} is disabled under Endemic Network")
    public void verifyThatIsDisabledUnderEndemicNetwork(String contextualCategory) {
        logger.info("Verifying '{}' is disabled under Endemic Network", contextualCategory);
        Assert.assertTrue(contextualCategory + "is not disabled", npiSmartList.verifyMedscapeAndWebMDAreDisabled(contextualCategory));
    }

    @And("The user saves the Smart List without selecting any other Population options and verifies error message")
    public void theUserSavesTheSmartListWithoutSelectingAnyOtherPopulationOptionsAndVerifiesErrorMessage() {
        logger.info("Saving Smart List without selecting any Population options to verify error message");
        npiSmartList.clickSaveButton();
        String errorMsg = npiSmartList.fetchErrorAlert();
        logger.info("Error message displayed: '{}'", errorMsg);
        Assert.assertEquals("Select one or more List Population Options", errorMsg);
    }

    @And("User selects Smart NPI list as below with mandatory details")
    public void userSelectsSmartNPIListAsBelowWithMandatoryDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        logger.info("Selecting Smart NPI list with mandatory details: {}", rows);
        for (Map<String, String> row : rows) {
            String option = row.get("PopulationOption").trim();
            String details = row.get("OptionDetails").trim();
            npiSmartList.selectSmartNPIListType(option);
            Map<String, String> attributeMap = Arrays.stream(details.split(",")).map(String::trim).map(entry -> entry.split(":", 2)).collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            logger.info("Entering details for '{}': {}", option, attributeMap);
            npiSmartList.enterPopulationOptionsDetail(option, attributeMap);
        }
    }

    @And("Verify Bulk Upload template {string} records count matches UI count post upload")
    public void verifyBulkUploadTemplateEntryCountMatchesUICountPostUpload(String fileName) throws IOException {
        int recordsCountFromFile = FileActions.countRecordsFromTextFile(fileName);
        int recordsCountFromUI = 0;
        if (fileName.contains("Diagnosis_BulkUpload")) {
            recordsCountFromUI = Integer.parseInt(npiSmartList.fetchDiagnosisCodesFromUI());
        } else {
            recordsCountFromUI = Integer.parseInt(npiSmartList.fetchMedicalProcedureCodesFromUI());
        }
        logger.info("Verifying Bulk Upload counts: File='{}', UI='{}'", recordsCountFromFile, recordsCountFromUI);
        Assert.assertEquals("Bulk Upload template records doesn't match with UI", recordsCountFromFile, recordsCountFromUI);
    }

    @And("User navigates to Administrative section and fetches the advertisers and client value for the account {string}")
    public void userNavigatesToAdministrativeSectionAndFetchesTheAdvertisersAndClientValueForTheAccount(String account) {
        logger.info("Navigating to Administrative section to fetch advertisers and client for account '{}'", account);
        navigation.clickSubMenu();
        accounts.clickAdministration();
        accounts.clickAdvertiserTab();
        accounts.selectAccount(account);
        itemList = CommonUtils.normalize(accounts.fetchAdvertiserList());
        logger.info("Fetched advertisers for account '{}': {}", account, itemList);
        accounts.selectAccountsTab();
        accounts.searchAccount(account);
        metricName = accounts.fetchClientValue();
        logger.info("Fetched client value: '{}'", metricName);
        navigation.clickPulsePointLogo();
    }

    @Then("Verify Advertiser dropdown should show values which are mapped to the account")
    public void verifyAdvertiserDropdownShouldShowValuesWhichAreMappedToTheAccount() {
        List<String> actualAdvertiserList = CommonUtils.normalize(campaigns.fetchAdvertiserList());
        logger.info("Verifying Advertiser dropdown values: expected={}, actual={}", itemList, actualAdvertiserList);
        Assert.assertEquals("Advertisers list is not matched", actualAdvertiserList, itemList);
    }

    @And("Verify that an error message is displayed when no Advertiser is selected")
    public void verifyThatAnErrorMessageIsDisplayedWhenNoAdvertiserIsSelected() {
        logger.info("Saving campaign without selecting Advertiser to verify error message");
        campaigns.saveCampaign();
        List<String> errorList = campaigns.fetchMandatoryFieldsError();
        logger.info("Fetched mandatory field errors: {}", errorList);
        Assert.assertTrue("Mandatory field error message is not displayed", errorList.contains("Select Advertiser"));
    }

    @And("Verify that Campaign Type default value is set to {string}")
    public void verifyThatCampaignTypeDefaultValueIsSetTo(String campaignType) {
        String defaultValue = campaigns.fetchDefaultCampaignType();
        logger.info("Verifying Campaign Type default: expected='{}', actual='{}'", campaignType, defaultValue);
        Assert.assertEquals("Campaign Type default value is not set to " + campaignType, campaignType, defaultValue);
    }

    @And("Verify that if the account has a Client value set, the Client field is disabled and auto-populated; otherwise, it remains enabled for user selection {string}")
    public void verifyThatIfTheAccountHasAClientValueSetTheClientFieldIsDisabledAndAutoPopulatedOtherwiseItRemainsEnabledForUserSelection(String clientName) {
        boolean isEnabled = metricName.equalsIgnoreCase("None");
        String actualState = campaigns.verifyClientFieldEnabledOrDisabledBasedOnAccount(clientName);
        logger.info("Verifying Client field for '{}': expected='{}', actual='{}'", clientName, isEnabled ? "Enabled" : "Disabled", actualState);
        if (isEnabled) {
            Assert.assertEquals("Enabled", actualState);
        } else {
            Assert.assertEquals("Disabled", actualState);
        }
    }

    @And("Verify that user is able to enter and select the drug {string}")
    public void verifyThatUserIsAbleToEnterAndSelectTheDrug(String drugName) {
        logger.info("Selecting drug '{}'", drugName);
        campaigns.selectDrug(drugName);
    }

    @And("Verify that Campaign Budget accepts only numeric values {string}")
    public void verifyThatCampaignBudgetAcceptsOnlyNumericValues(String budget) {
        String invalidValue = campaigns.fetchCampaignBudget(budget);
        logger.info("Verifying Campaign Budget with input '{}': fetched value='{}'", budget, invalidValue);
        Assert.assertEquals("Campaign Budget accepts invalid values", "", invalidValue);
    }

    @And("Verify that user is able to enter the data {string} in the Description field")
    public void verifyThatUserIsAbleToEnterTheDataInTheDescriptionField(String campaignDescription) {
        logger.info("Entering campaign description: '{}'", campaignDescription);
        campaigns.enterCampaignDescription(campaignDescription);
    }

    @And("Verify that Budget Status has the below options, and the default status is {string}")
    public void verifyThatBudgetStatusHasTheOptionsAndAndTheDefaultStatusIs(String defaultButton, DataTable dataTable) {
        List<String> expectedOptions = dataTable.asList(String.class);
        List<String> actualOptions = campaigns.fetchBudgetStatus();
        logger.info("Verifying Budget Status options: expected={}, actual={}", expectedOptions, actualOptions);
        Assert.assertEquals("Budget status has different options", expectedOptions, actualOptions);

        String actualDefault = campaigns.fetchDefaultBudgetStatus();
        logger.info("Verifying default Budget Status button: expected='{}', actual='{}'", defaultButton, actualDefault);
        Assert.assertEquals(defaultButton + " button is not set as default", defaultButton, actualDefault);
    }

    @And("Verify the availability of the Management Fee checkbox and when clicked, below options should be displayed")
    public void verifyTheAvailabilityOfTheManagementFeeCheckboxAndWhenClickedTheOptionsAndShouldBeDisplayed(DataTable dataTable) {
        List<String> expectedOptions = dataTable.asList(String.class);
        Assert.assertTrue("Management Fee checkbox is not available", campaigns.isManagementFeeAvailable());
        logger.info("Management Fee checkbox is available. Clicking to display options.");
        campaigns.clickManagementFee();
        List<String> actualOptions = campaigns.fetchManagementFeeOptions();
        logger.info("Verifying Management Fee options: expected={}, actual={}", expectedOptions, actualOptions);
        Assert.assertEquals("Management Fee has different options", expectedOptions, actualOptions);
    }

    @And("Verify that the user is able to enter data in the selected Management Fee option - {string}, {string}, {string}")
    public void verifyThatTheUserIsAbleToEnterDataInTheSelectedManagementFeeOption(String managementFeeOption, String percent, String amount) {
        logger.info("Entering Management Fee data: Option='{}', Percent='{}', Amount='{}'", managementFeeOption, percent, amount);
        campaigns.clickManagementFeeOptionAndEnterData(managementFeeOption, percent, amount);
    }

    @And("User clicks the three-dot menu and verifies that {string} is enabled and {string} is disabled")
    public void userClicksTheThreeDotMenuAndVerifiesThatIsEnabledAndIsDisabled(String reportOption, String deleteOption) {
        campaigns.clickActionItemMenu();
        logger.info("Verifying three-dot menu options: '{}' should be enabled, '{}' should be disabled", reportOption, deleteOption);
        Assert.assertTrue("Generate Report option is not available and enabled", campaigns.isGenerateReportOptionAvailable(reportOption));
        Assert.assertTrue("Delete option is not available and disabled", campaigns.isDeleteOptionAvailable(deleteOption));
    }

    @And("User enters other campaign details {string} {string} {string} {string}")
    public void userEntersOtherCampaignDetails(String advertiser, String campaign_name, String campaign_type, String budget) {
        campaignNameRandom = campaign_name + '_' + CommonUtils.timeStampCalculation();
        logger.info("Entering campaign details: Advertiser='{}', Campaign='{}', Type='{}', Budget='{}'", advertiser, campaignNameRandom, campaign_type, budget);
        campaigns.selectAdvertiser(advertiser);
        campaigns.enterCampaignName(campaignNameRandom);
        campaigns.setCampaignType(campaign_type);
        campaigns.enterBudget(budget);
    }

    @And("User retrieves all the entered data, saves the Campaign and verifies successful creation")
    public void userRetrievesAllTheEnteredDataSavesTheCampaignAndVerifiesSuccessfulCreation() {
        capturedDetails.clear();
        capturedDetails = campaigns.fetchCampaignDetails();
        logger.info("Fetched campaign details before saving: {}", capturedDetails);
        campaigns.saveCampaign();
        String successMsg = campaigns.campaignSuccess();
        logger.info("Campaign save success message: '{}'", successMsg);
        Assert.assertEquals("Campaign " + campaignNameRandom + " created.", successMsg);
    }

    @And("Verify that the saved Campaign data matches the entered data")
    public void verifyThatTheSavedCampaignDataMatchesTheEnteredData() {
        campaigns.clickSavedCampaign(campaignNameRandom);
        campaigns.clickCampaignDetailsTab();
        List<String> fetchedData = campaigns.fetchCampaignDetails();
        logger.info("Comparing captured data vs saved data: captured={}, fetched={}", capturedDetails, fetchedData);
        Assert.assertEquals("The saved Campaign data doesn't match the entered data", capturedDetails, fetchedData);
    }


    @And("User verifies if Add Custom Field button is available")
    public void userVerifiesIfAddCustomFieldButtonIsAvailable() {
        Assert.assertTrue("Add Custom Field Button is not available", campaigns.isAddCustomFieldButtonAvailable());
        logger.info("Add Custom Field button is available");
    }

    @When("User adds a custom field with {string} on the campaign creation page successfully")
    public void userAddsACustomFieldWithOnTheCampaignCreationPage(String fieldName) {
        customFieldName = fieldName + '_' + CommonUtils.randomFourDigitNumber();
        logger.info("Adding Custom Field: '{}'", customFieldName);
        campaigns.clickAddCustomFieldButton();
        campaigns.enterCustomFieldName(customFieldName);
        campaigns.saveCustomField();
        String successAlert = campaigns.fetchCustomFieldSuccessAlert();
        logger.info("Custom Field creation success alert: '{}'", successAlert);
        Assert.assertEquals("Successfully created custom Field : " + customFieldName, successAlert);
    }

    @Then("Verify that the custom field is added on the campaign creation page")
    public void verifyThatTheCustomFieldIsAddedOnTheCampaignCreationPage() {
        logger.info("Verifying Custom Field '{}' is added", customFieldName);
        Assert.assertTrue(customFieldName + " Custom Field is not available", campaigns.isAddedCustomFieldAvailable(customFieldName));
    }

    @When("User modifies the custom field label to new label {string}")
    public void userModifiesTheCustomFieldToNewLabel(String newFieldName) {
        uiCustomFieldName = newFieldName + '_' + CommonUtils.randomFourDigitNumber();
        logger.info("Modifying Custom Field '{}' to new label '{}'", customFieldName, uiCustomFieldName);
        campaigns.clickCustomFieldLabel(customFieldName);
        campaigns.enterCustomFieldName(uiCustomFieldName);
        campaigns.saveCustomField();
        Assert.assertEquals("Successfully updated custom Field : " + uiCustomFieldName , campaigns.fetchCustomFieldSuccessAlert());
    }

    @Then("Verify that the custom field is updated with new label")
    public void verifyThatTheCustomFieldIsUpdatedWithNewLabel() {
        logger.info("Verifying updated Custom Field '{}' is available", uiCustomFieldName);
        Assert.assertTrue(customFieldName + " Custom Field label is not updated with " + uiCustomFieldName, campaigns.isAddedCustomFieldAvailable(uiCustomFieldName));
    }

    @And("User enters data {string} in the custom field")
    public void userEntersDataInTheCustomField(String customFieldData) {
        logger.info("Entering data '{}' in Custom Field '{}'", customFieldData, uiCustomFieldName);
        campaigns.enterCustomFieldData(uiCustomFieldName, customFieldData);
    }

    @Then("Verify that the custom field value {string} is saved and displayed in the campaign details page")
    public void verifyThatTheCustomFieldValueIsSavedAndDisplayedInTheCampaignDetailsPage(String customFieldData) {
        campaigns.navigateToCampaign(campaignNameRandom);
        campaigns.clickCampaignDetailsTab();
        String fetchedData = campaigns.fetchCustomFieldData(uiCustomFieldName);
        logger.info("Verifying saved Custom Field value: expected='{}', actual='{}'", customFieldData, fetchedData);
        Assert.assertEquals(customFieldData, fetchedData);
    }

    @And("User verifies if the added custom field is available on New Campaign creation page")
    public void userVerifiesIfTheAddedCustomFieldIsAvailableOnNewCampaignCreationPage() {
        campaigns.navigateToCampaignDashboard();
        campaigns.createCampaign();
        Assert.assertEquals("Create New Campaign", campaigns.verifyCampaignText());
        logger.info("Verifying added custom field '{}' is present on New Campaign creation page", uiCustomFieldName);
        Assert.assertTrue(uiCustomFieldName + " Custom Field is not available", campaigns.isAddedCustomFieldAvailable(uiCustomFieldName));
    }

    @When("User deletes the custom field for which campaign is created and verifies if it is deleted")
    public void userDeletesTheCustomFieldFromTheCampaignCreationPage() {
        logger.info("Deleting custom field '{}' for campaign already created", uiCustomFieldName);
        Assert.assertEquals("Custom Field Can't Be Removed", campaigns.deleteCustomField(uiCustomFieldName));
    }

    @And("User deletes the custom field for which campaign is not created and verifies if it is deleted")
    public void userDeletesTheCustomFieldForWhichCampaignIsNotCreatedAndVerifiesIfItIsDeleted() {
        customFieldName = "Test" + '_' + uiCustomFieldName;
        logger.info("Creating temporary custom field '{}' to delete later", customFieldName);
        campaigns.clickAddCustomFieldButton();
        campaigns.enterCustomFieldName(customFieldName);
        campaigns.saveCustomField();
        Assert.assertEquals("Successfully created custom Field : " + customFieldName , campaigns.fetchCustomFieldSuccessAlert());
        logger.info("Deleting temporary custom field '{}'", customFieldName);
        campaigns.deleteCustomField(customFieldName);
        Assert.assertEquals("Successfully deleted the Field : " + customFieldName , campaigns.fetchCustomFieldSuccessAlert());
    }

    @And("User verifies if the deleted custom field is available on New Campaign creation page")
    public void userVerifiesIfTheDeletedCustomFieldIsAvailableOnNewCampaignCreationPage() {
        navigation.clickPulsePointLogo();
        campaigns.createCampaign();
        Assert.assertEquals("Create New Campaign", campaigns.verifyCampaignText());
        logger.info("Verifying deleted custom field '{}' is not available", customFieldName);
        Assert.assertFalse(customFieldName + " Custom Field is available", campaigns.isAddedCustomFieldAvailable(customFieldName));
    }

    @And("Verify that user is able to download the uploaded {string} list")
    public void verifyThatUserIsAbleToDownloadTheUploadedFile(String listType) throws IOException {
        if (listType.equals("NPI")) {
            targetFilePath = npiStaticList.clickDownloadIcon();
        } else {
            targetFilePath = sharedList.clickDownloadIcon();
        }
        logger.info("Verifying downloaded file for '{}' list exists at '{}'", listType, targetFilePath);
        Assert.assertTrue("Downloaded file is not available", CommonUtils.isDownloadedFileAvailable(targetFilePath, "csv"));
    }

    @And("Verify that the count of items in the downloaded {string} list is the same as the item count displayed in the UI")
    public void verifyTheCountOfItemsInTheDownloadedList(String listType) throws IOException {
        String header = null;
        int recordsCountFromFile = 0;
        String recordsCountFromUI;

        switch (listType) {
            case "Keyword":
                header = "\ufeffKeywords";
                break;
            case "Domain":
                header = "\ufeffdomains";
                break;
            case "App Bundle":
                header = "\ufeffApp Bundles";
                break;
            case "IP":
                recordsCountFromFile = FileActions.fetchRowCountFromCSV(targetFilePath);
                break;
            case "NPI":
                header = "NPI";
                break;
        }

        if (header != null) {
            recordsCountFromFile = FileActions.fetchColumnCountFromCSV(targetFilePath, header);
        }

        if (listType.equals("NPI")) {
            recordsCountFromUI = npiStaticList.fetchSharedListCountFromUI();
        } else {
            recordsCountFromUI = sharedList.fetchSharedListCountFromUI();
        }
        logger.info("Verifying {} list count: File={}, UI={}", listType, recordsCountFromFile, recordsCountFromUI);
        Assert.assertEquals("Downloaded list count doesn't match with UI count", recordsCountFromFile, Integer.parseInt(recordsCountFromUI));
    }

    @And("User enters the list name as {string} and uploads the file {string}")
    public void userEntersTheListNameAndUploadsTheFile(String listName, String fileName) {
        metricName = listName + "_" + CommonUtils.timeStampCalculation();
        npiName = metricName;
        logger.info("Uploading '{}' as list name '{}'", fileName, metricName);
        sharedList.enterListName(metricName);
        npiStaticList.uploadStaticListFile(fileName);
    }

    @And("User saves the Email list and verify that the list is created successfully")
    public void userSavesTheListAndVerifyThatTheListIsCreatedSuccessfully() {
        sharedList.saveEmailList();
        logger.info("Verifying Email list '{}' is created successfully", metricName);
        Assert.assertEquals("Email list created successfully", sharedList.isListCreatedOrDeleted());
    }

    @And("Verify that the counter on the left displays the correct value after file upload for {string}")
    public void verifyThatTheCounterOnTheLeftDisplaysTheCorrectValueAfterFileUploadFor(String listType) {
        sharedList.searchAndOpenCreatedList(metricName);
        totalListCount = Integer.parseInt(sharedList.fetchCountFromLeftPanel(metricName));
        itemCount = sharedList.fetchEmailCount();
        logger.info("Verifying counter for {} list: Left Panel={}, Item Count={}", listType, totalListCount, itemCount);
        Assert.assertEquals(totalListCount, itemCount);
    }

    @And("Verify that download option should not be available for uploaded Email list")
    public void verifyThatDownloadOptionShouldNotBeAvailableForUploadedEmailList() {
        logger.info("Verifying download option is disabled for Email list '{}'", metricName);
        Assert.assertFalse("Download icon is available for Email list", sharedList.isEmailListDownloadIconVisible());
    }

    @Then("Verify that user is able to export the audit log for {string}")
    public void verifyThatUserIsAbleToExportTheAuditLogFor(String moduleName) {
        campaigns.clickCampaignOptions();
        campaigns.openExportAuditLogPopup();
        logger.info("Export audit log popup content for module '{}'", moduleName);

        switch (moduleName) {
            case "campaign":
                assert campaigns.fetchExportAuditLogPopupContent().contains(campaignNameRandom);
                break;
            case "line item":
                assert campaigns.fetchExportAuditLogPopupContent().contains(lineItemNameRandom);
                break;
            case "tactic":
                assert campaigns.fetchExportAuditLogPopupContent().contains(tacticNameRandom);
                break;
        }

        campaigns.clickConfirmExportAuditLog();
        Assert.assertEquals("Audit Log request created. File will be send via email.", campaigns.fetchExportAuditLogSuccessAlert());
    }

    @When("User navigates to {string} page")
    public void userNavigatesToItemPage(String moduleName) {
        logger.info("Navigating to '{}' page", moduleName);
        switch (moduleName) {
            case "campaign":
                campaigns.clickCampaignTile();
                break;
            case "line item":
                campaigns.clickLineItemTile();
                break;
            case "tactic":
                campaigns.clickTacticTile();
                break;
        }
    }

    @Then("Verify that user is able to export the campaign settings")
    public void verifyThatUserIsAbleToExportTheCampaignSettings() {
        campaigns.clickCampaignDetailsTab();
        campaigns.clickCampaignOptions();
        campaigns.exportCampaignSettings();
        String successMsg = campaigns.fetchExportCampaignSettingsSuccessAlert();
        logger.info("Export campaign settings success message: '{}'", successMsg);
        Assert.assertEquals("Done! The exported file will be sent to default@pulsepoint.com within 10 minutes.", successMsg);
    }

    @And("Verify that user is able to download the {string} list")
    public void verifyThatUserIsAbleToDownloadTheList(String listType) throws IOException {
        targetFilePath = npiStaticList.clickDownloadIcon();
        logger.info("Verifying downloaded '{}' list is available at '{}'", listType, targetFilePath);
        Assert.assertTrue("Downloaded file is not available", CommonUtils.isDownloadedFileAvailable(targetFilePath, "csv"));
    }

    @And("User searches and selects the NPI List {string}")
    public void userSearchesAndSelectsThePulsePointProvidedNPIList(String npiListName) {
        logger.info("Searching and selecting NPI list '{}'", npiListName);
        npiLists.searchList(npiListName);
        npiLists.openSearchedList(npiListName);
    }

    @And("User searches and selects the campaign {string}")
    public void userSearchesAndSelectsTheCampaign(String campaignName) {
        logger.info("Searching and opening campaign '{}'", campaignName);
        campaignDashboard.searchCreatedCampaign(campaignName);
        campaignDashboard.navigateToCampaign(campaignName);
    }

    @Then("Verify that the campaign page is displayed")
    public void verifyThatTheCampaignPageIsDisplayed() {
        logger.info("Verifying campaign page is displayed");
        Assert.assertTrue("Navigation to Campaign details page is not successful", campaignDashboard.isCampaignPageDisplayed());
    }
}
