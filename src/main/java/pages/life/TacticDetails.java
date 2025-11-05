package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import pages.Navigation;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.*;
import java.util.regex.Pattern;

public class TacticDetails {
    private final Page page;
    private final Locator VERIFY_TACTIC_DETAILS_PAGE;
    private final Locator TACTIC_NAME;
    private final Locator SAVE_TACTIC_DETAILS;
    private final Locator TACTIC_DETAILS_SUCCESS;
    private final Locator IMPORT_TEMPLATE_ICON;
    private final Locator TEMPLATE_SEARCH_BOX;
    private final Locator IMPORT_TEMPLATE_DIALOG;
    private final Locator IMPORT_BUTTON;
    private final Locator OVERRIDE_DIALOG;
    private final Locator REPLACE_BUTTON;
    private final Locator TEMPLATE_IMPORT_ALERT;
    private final Locator IMPORTED_TARGET_TEMPLATE;
    private final Locator TACTIC_SETTINGS_TAB;
    private final Locator SAVE_TEMPLATE_BUTTON;
    private final Locator SAVE_TEMPLATE_DIALOG;
    private final Locator TEMPLATE_NAME_TEXT;
    private final Locator SAVE_BUTTON;
    private final Locator TEMPLATE_SAVED_SUCCESS_ALERT;
    public final Locator TARGETTING_RULES_ICON;
    private final Locator NEW_TACTIC_BUTTON;
    private final Locator SAVED_TACTICS;
    private final Locator DETAILS_TAB;
    private final Locator NEW_TACTIC_TABS;
    private final Locator SAVED_TACTIC_TABS;
    private final Locator TACTIC_STATUS;
    private final Locator TACTIC_DETAILS_TAB;
    private final Locator ADD_CUSTOM_FIELD;
    private final Locator ADD_CUSTOM_FIELD_INPUT;
    private final Locator SAVE_CUSTOM_FIELD_BUTTON;
    private final Locator FIELD_CREATE_SUCCESS;
    private final Locator CUSTOM_FIELD_TEXT;
    private final Locator DELETE_BUTTON;
    private final Locator CONFIRM_DELETE;
    private final Locator DELETE_SUCCESS;
    private final Locator CUSTOM_FIELD;
    public final Locator TARGETING_RULES_ICON;
    private final Locator THREE_DOT_ICON;
    private final Locator TACTIC_DELETE_BUTTON;
    private final Locator TACTIC_REMOVE_BUTTON;
    private final Locator TACTIC_TOGGLE_CLASS;
    private final Locator EXIT_BULK_MODE;
    private final Locator ENABLE_TACTIC;
    private final Locator TACTIC_SELECT;
    private final Locator BULK_ACTION;
    private final Locator TACTIC_GLOBAL_SEARCH_TEXT;


    Campaigns campaigns = new Campaigns(DriverFactory.getPage());
    LineItemDetails lineItemDetails = new LineItemDetails(DriverFactory.getPage());
    NPISmartList npiSmartList = new NPISmartList(DriverFactory.getPage());
    TargetingTemplate targetingTemplate = new TargetingTemplate(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());
    TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());
    TacticCreatives tacticCreatives = new TacticCreatives(DriverFactory.getPage());
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public TacticDetails(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_DETAILS_PAGE = page.locator("//div[text()='New Tactic']");
        this.TACTIC_NAME = page.locator("//input[@placeholder='Tactic Name' or @placeholder='Ad Group Name']");
        this.SAVE_TACTIC_DETAILS = page.locator("//span[text()='Save']");
        this.TACTIC_DETAILS_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.IMPORT_TEMPLATE_ICON = page.locator("//app-icon-lable-link[contains(@text,'Import Template')]/div");
        this.IMPORT_TEMPLATE_DIALOG = page.locator("//div[contains(text(),'Import Template')]");
        this.TEMPLATE_SEARCH_BOX = page.locator("//input[contains(@placeholder,'type in to search...')]");
        this.IMPORT_BUTTON = page.locator("//button[contains(text(),'Import')]");
        this.OVERRIDE_DIALOG = page.locator("//div[contains(text(),'Override Targeting Rules?')]");
        this.REPLACE_BUTTON = page.locator("//button[contains(text(),'Replace Targeting')]");
        this.TEMPLATE_IMPORT_ALERT = page.locator("//div[contains(text(),'Template Imported Successfully')]");
        this.IMPORTED_TARGET_TEMPLATE = page.locator("//div[@class='targets-list']");
        this.TACTIC_SETTINGS_TAB = page.locator("//a[contains(@class,'gaTabSettings')]");
        this.SAVE_TEMPLATE_BUTTON = page.locator("//app-icon-lable-link[contains(@text,'Save as Template')]/div");
        this.SAVE_TEMPLATE_DIALOG = page.locator("//div[contains(text(),'Save as Template')]");
        this.TEMPLATE_NAME_TEXT = page.locator("//input[contains(@placeholder,'Template Name')]");
        this.SAVE_BUTTON = page.locator("//button[contains(@class,'okButton')]");
        this.TEMPLATE_SAVED_SUCCESS_ALERT = page.locator("//div[contains(text(),'Saved as Template Successfully')]");
        this.TARGETTING_RULES_ICON = page.locator("//span[contains(text(),'Targeting Rule')]");
        this.NEW_TACTIC_BUTTON = page.locator("//span[normalize-space(text())='New Tactic']");
        this.SAVED_TACTICS = page.locator("//div[contains(@class,'tactic-main-details')]");
        this.DETAILS_TAB = page.locator("//a[contains(text(),'Detail')]");
        this.NEW_TACTIC_TABS = page.locator("//a[@disabled='disabled']");
        this.SAVED_TACTIC_TABS = page.locator("//a[contains(@class,'gaTab')]");
        this.TACTIC_STATUS = page.locator("//span[contains(@class, 'status-label')]/span");
        this.TACTIC_DETAILS_TAB = page.locator("//a[normalize-space()='Details']");
        this.ADD_CUSTOM_FIELD = page.locator("//span[contains(text(),'Add Custom Field')]");
        this.ADD_CUSTOM_FIELD_INPUT = page.locator("//input[@placeholder='Field Name']");
        this.SAVE_CUSTOM_FIELD_BUTTON = page.locator("//button[normalize-space()='Save']");
        this.FIELD_CREATE_SUCCESS = page.locator("//div[@role='alert' and contains(text(),'Successfully created custom Field')]");
        this.CUSTOM_FIELD_TEXT = page.locator("//input[contains(@class,'gaName')]");
        this.DELETE_BUTTON = page.locator("//app-icon-lable-link[contains(@class,'delete-field')]");
        this.CONFIRM_DELETE = page.locator("//span[contains(text(),'Delete Field')]");
        this.DELETE_SUCCESS = page.locator("//div[contains(text(),'Successfully deleted the Field')]");
        this.CUSTOM_FIELD = page.locator("(//label[contains(@class,'cmp-form-label')])[1]");
        this.TARGETING_RULES_ICON = page.locator("//span[contains(text(),'Targeting Rule')]");
        this.THREE_DOT_ICON =  page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Generate ReportDuplicateExport Audit LogDelete\\?$"))).first();
        this.TACTIC_DELETE_BUTTON = page.getByText("Delete");
        this.TACTIC_REMOVE_BUTTON = page.getByText("Remove");
        this.EXIT_BULK_MODE = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Exit Bulk edit mode"));
        this.TACTIC_TOGGLE_CLASS = page.locator("(//div[@class='item-list-control-toggle toggle-enabled ng-star-inserted'])[2]");
        this.ENABLE_TACTIC =page.locator("//div[@class='bulk-icon addBulkOpActive']").first();
        this.BULK_ACTION =page.locator(".pointer.inlineDiv.iconSprite").first();
        this.TACTIC_GLOBAL_SEARCH_TEXT =page.getByText("Nothing found...");
        this.TACTIC_SELECT =page.locator("//*[@id=\"lidcBody\"]/app-campaign-left-nav/div/div/div[3]/div[2]/div[2]/div[2]/div/div/div/div/div[1]/sui-checkbox");
//        this.TACTIC_SELECT = page.locator("//*[contains(text(), 'Targetting29102025')]/ancestor::div[1]//sui-checkbox");

    }
    public void clickNewTactic(){
        NEW_TACTIC_BUTTON.click();
    }
    public List<String> getAllTactics(){
        return SAVED_TACTICS.allInnerTexts();
    }
    public void verifyDetailsTab(){
        DETAILS_TAB.isVisible();
        System.out.println(DETAILS_TAB.innerText().trim());

    }
    public List<String> newTacticTabs(){
        return NEW_TACTIC_TABS.allInnerTexts();
    }
    public void CLICK_FIRST_TACTIC(){
        SAVED_TACTICS.first().click();
    }

    public List<String> savedTacticTabs(){
        return SAVED_TACTIC_TABS.allInnerTexts();
    }

    public String verifyTacticState(){
        return TACTIC_STATUS.innerText();
    }
    public void clickDetailsTab() {
        TACTIC_DETAILS_TAB.click();
    }
    public void addCustomField (String fieldName){
        ADD_CUSTOM_FIELD.click();
        ADD_CUSTOM_FIELD_INPUT.fill(fieldName);
        SAVE_CUSTOM_FIELD_BUTTON.click();
        waitUtility.waitForLocatorVisible(FIELD_CREATE_SUCCESS);
        CUSTOM_FIELD_TEXT.last().fill("CUST123");
        SAVE_TACTIC_DETAILS.click();
    }

    public String verifyCustomField(String fieldName){
        Locator customField = page.locator(String.format("//label[contains(text(),'%s')]", fieldName));
        return customField.innerText().trim();
    }

    public void clickTactic(){
        //Locator tacticTab = page.locator(String.format("//div[contains(text(),'%s')]", tacticName));
        Locator tacticTab = page.locator("//div[@class='tactic item-details']").last();
        tacticTab.click();

    }

    public void deleteCustomField (String customFieldName){
        Locator FIELD_OPTIONS = page.locator(String.format("//label[contains(text(),'%s')]/div/span", customFieldName));
        FIELD_OPTIONS.click();
        DELETE_BUTTON.click();
        CONFIRM_DELETE.click();
        DELETE_SUCCESS.isVisible();
    }


    public String verifyTacticDetailsText() {
        return VERIFY_TACTIC_DETAILS_PAGE.innerText();
    }

    public void enterTacticName(String tacticName) {
        TACTIC_NAME.fill(tacticName);
    }

    public void saveTacticDetails() {
        SAVE_TACTIC_DETAILS.click();
        waitUtility.waitUntilSpinnerHidden();
    }
    public void saveTactic() {
        waitUtility.waitForLocatorVisible(CUSTOM_FIELD);
        SAVE_TACTIC_DETAILS.click();
    }

    public String tacticDetailsSuccess() {
        String successMessage = TACTIC_DETAILS_SUCCESS.first().innerText().trim();
        waitUtility.waitUntilSpinnerHidden();
        return successMessage;
    }

    public boolean createTacticWithLineItemsAndImport(List<String> lineItemTypeList, String advertiser, String campaignName, String campaignType, String budget, String lineItemName, String lineBudget, String tacticName, List<String> templateNameList, List<Map<String, String>> ruleCountAndValueList) {
        List<Map<String, String>> labelCountMapList = new ArrayList<>();
        for (String lineItemType : lineItemTypeList) {
            navigation.clickSubMenu();
            navigation.clickCampaigns();
            campaigns.campaignDashboard();
            //Campaign, Line Item and Tactic creation
            createCampaign(advertiser, campaignName + "_" + CommonUtils.timeStampCalculation(), campaignType, budget);
            createLineItem(lineItemName + "_" + CommonUtils.generateRandomString(), lineItemType.trim(), lineBudget);
            createTactic(tacticName + "_" + CommonUtils.generateRandomString());
            Map<String, String> labelCountMap = importTargetingTemplate(lineItemType.trim(), templateNameList);
            labelCountMapList.add(labelCountMap);
            saveTacticDetails();
        }
        ruleCountAndValueList.sort(Comparator.comparing(Object::toString));
        labelCountMapList.sort(Comparator.comparing(Object::toString));
        return ruleCountAndValueList.equals(labelCountMapList);
    }

    public List<String> createTacticWithLineItemsAndTargetingRules(List<String> lineItemTypeList, String advertiser, String campaignName, String campaignType, String budget, String lineItemName, String lineBudget, String tacticName, Map<String, List<String>> rulesMap) {
        List<String> templateNameList = new ArrayList<>();
        for (String lineItemType : lineItemTypeList) {
            navigation.clickSubMenu();
            navigation.clickCampaigns();
            campaigns.campaignDashboard();

            createCampaign(advertiser, campaignName + "_" + CommonUtils.timeStampCalculation(), campaignType, budget);
            createLineItem(lineItemName + "_" + CommonUtils.generateRandomString(), lineItemType.trim(), lineBudget);
            createTactic(tacticName + "_" + CommonUtils.generateRandomString());

            targetingTemplate.addTargetingRules(rulesMap);
            saveTacticDetails();
            waitUtility.waitUntilSpinnerHidden();
            templateNameList.add(saveTargetingTemplate(lineItemType.trim()));
        }
        return templateNameList;
    }

    private void createCampaign(String advertiser, String campaignName, String campaignType, String budget) {
        campaigns.createCampaign();
        campaigns.verifyCampaignText();
        campaigns.selectAdvertiser(advertiser);
        campaigns.enterCampaignName(campaignName);
        campaigns.setCampaignType(campaignType);
        campaigns.enterBudget(budget);
        campaigns.saveCampaign();
        waitUtility.waitUntilSpinnerHidden();
    }

    private void createLineItem(String lineItemName, String lineItemType, String lineBudget) {
        lineItemDetails.enterLineItemName(lineItemName);
        lineItemDetails.selectLineItemType(lineItemType);
        lineItemDetails.clickAddFlightButton();
        lineItemDetails.enterLineItemBudget(lineBudget);
        lineItemDetails.enableLineItem();
        lineItemDetails.saveLineItem();
        waitUtility.waitUntilSpinnerHidden();
    }

    private void createTactic(String tacticName) {
        enterTacticName(tacticName);
        saveTacticDetails();
        waitUtility.waitUntilSpinnerHidden();
    }

    private Map<String, String> importTargetingTemplate(String lineItemType, List<String> templateNameList) {
        Map<String, String> labelCountMap = new LinkedHashMap<>();
        for (String templateName : templateNameList) {
            if (templateName.startsWith(lineItemType.trim() + "_")) {
                IMPORT_TEMPLATE_ICON.click();
                IMPORT_TEMPLATE_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                TEMPLATE_SEARCH_BOX.fill(templateName);
                page.locator(String.format("//div[contains(@class,'item text-truncate') and contains(text(), '%s')]", templateName)).click();
                IMPORT_BUTTON.click();
                if (OVERRIDE_DIALOG.isVisible()) REPLACE_BUTTON.click();
                waitUtility.waitUntilSpinnerHidden();
                TEMPLATE_IMPORT_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
                labelCountMap = targetingTemplate.fetchTargetingRulesCountFromTargeting();
                IMPORTED_TARGET_TEMPLATE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                break;
            }
        }
        return labelCountMap;
    }

    private String saveTargetingTemplate(String lineItemType) {
        String templateName = lineItemType + "_Template_" + CommonUtils.timeStampCalculation();
        TACTIC_SETTINGS_TAB.click();
        waitUtility.waitUntilSpinnerHidden();
        tacticSettings.verifyTacticSettingsText();
        SAVE_TEMPLATE_BUTTON.click();
        SAVE_TEMPLATE_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        TEMPLATE_NAME_TEXT.fill(templateName);
        SAVE_BUTTON.click();
        TEMPLATE_SAVED_SUCCESS_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return templateName;
    }
//    public void saveTactic() {
//        waitUtility.waitForLocatorVisible(CUSTOM_FIELD);
//        SAVE_TACTIC_DETAILS.click();
//    }

    public boolean createTacticWithLineItemsAndAssignCreative(String lineItemType, String advertiser, String campaignName, String campaignType, String budget, String lineItemName, String lineBudget, String tacticName, String CreativeName) {
        npiSmartList.clickPulsepointIcon();
        campaigns.campaignDashboard();

        createCampaign(advertiser, campaignName + "_" + CommonUtils.timeStampCalculation(), campaignType, budget);
        createLineItem(lineItemName + "_" + CommonUtils.generateRandomString(), lineItemType.trim(), lineBudget);
        createTactic(tacticName + "_" + CommonUtils.generateRandomString());

        tacticCreatives.clickCreativeTab();
        tacticCreatives.clickAssignCreatives();
        tacticCreatives.assignCreatives(CreativeName);
        saveTacticDetails();
        waitUtility.waitUntilSpinnerHidden();
        return tacticCreatives.verifyCreativeAssigned(CreativeName);
    }

    public void deleteTactic(){
        THREE_DOT_ICON.click();
        TACTIC_DELETE_BUTTON.click();
        TACTIC_REMOVE_BUTTON.click();
    }

    public void bulkEnableTactics() {
        BULK_ACTION.click();
        TACTIC_SELECT.click();
        ENABLE_TACTIC.click();
        EXIT_BULK_MODE.click();
    }

    public String getToggleClass() {
        return TACTIC_TOGGLE_CLASS.getAttribute("class");
    }

    public void globalSearchDeletedTactic(String tacticName) {
        page.locator(".iconSprite").first().click();
        page.getByRole(AriaRole.SEARCHBOX, new Page.GetByRoleOptions().setName("Search")).fill( tacticName);
        page.getByRole(AriaRole.SEARCHBOX, new Page.GetByRoleOptions().setName("Search")).press("Enter");
    }

    public String getSearchText() {
        return TACTIC_GLOBAL_SEARCH_TEXT.innerText();
    }
}