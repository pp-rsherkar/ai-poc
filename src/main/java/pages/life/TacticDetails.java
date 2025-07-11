package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TacticDetails {
    Campaigns campaigns = new Campaigns(DriverFactory.getPage());
    LineItemDetails lineItemDetails = new LineItemDetails(DriverFactory.getPage());
    TargetingTemplate targetingTemplate = new TargetingTemplate(DriverFactory.getPage());
    NPISmartList npiSmartList = new NPISmartList(DriverFactory.getPage());
    TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());
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

    public TacticDetails(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_DETAILS_PAGE = page.locator("//div[text()='New Tactic']");
        this.TACTIC_NAME = page.locator("//input[@placeholder='Tactic Name']");
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
    }

    public String verifyTacticDetailsText() {
        return VERIFY_TACTIC_DETAILS_PAGE.innerText();
    }

    public void enterTacticName(String tacticName) {
        TACTIC_NAME.fill(tacticName);
    }

    public void saveTacticDetails() {
        SAVE_TACTIC_DETAILS.click();
    }

    public String tacticDetailsSuccess() {
        return TACTIC_DETAILS_SUCCESS.innerText();
    }

    public boolean createTacticWithLineItemsAndImport(List<String> lineItemTypeList, String advertiser, String campaignName, String campaignType, String budget, String lineItemName, String lineBudget, String tacticName, List<String> templateNameList) {
        boolean flag = false;
        for(String lineItemType : lineItemTypeList) {
            npiSmartList.clickPulsepointICon();
            campaigns.campaignDashboard();

            createCampaign(advertiser, campaignName + "_" + CommonUtils.timeStampCalculation(), campaignType, budget);
            createLineItem(lineItemName + "_" + CommonUtils.randomNumberGeneration(), lineItemType.trim(), lineBudget);
            createTactic(tacticName + "_" + CommonUtils.randomNumberGeneration());

            importTargetingTemplate(lineItemType.trim(), templateNameList);
            if(IMPORTED_TARGET_TEMPLATE.isVisible())
                    flag = true;

            saveTacticDetails();
        }
        return flag;
    }

    public List<String> createTacticWithLineItemsAndTargetingRules(List<String> lineItemTypeList, String advertiser, String campaignName, String campaignType, String budget, String lineItemName, String lineBudget, String tacticName, Map<String, List<String>> rulesMap) {
        List<String> templateNameList = new ArrayList<>();
        for (String lineItemType : lineItemTypeList) {
            npiSmartList.clickPulsepointICon();
            campaigns.campaignDashboard();

            createCampaign(advertiser, campaignName + "_" + CommonUtils.timeStampCalculation(), campaignType, budget);
            createLineItem(lineItemName + "_" + CommonUtils.randomNumberGeneration(), lineItemType.trim(), lineBudget);
            createTactic(tacticName + "_" + CommonUtils.randomNumberGeneration());

            targetingTemplate.addTargetingRules(rulesMap);
            saveTacticDetails();

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
    }

    private void createLineItem(String lineItemName, String lineItemType, String lineBudget) {
        lineItemDetails.enterLineItemName(lineItemName);
        lineItemDetails.selectLineItemType(lineItemType);
        lineItemDetails.clickAddFlightButton();
        lineItemDetails.enterLineItemBudget(lineBudget);
        lineItemDetails.enableLineItem();
        lineItemDetails.saveLineItem();
    }

    private void createTactic(String tacticName) {
        enterTacticName(tacticName);
        saveTacticDetails();
    }


    private void importTargetingTemplate(String lineItemType, List<String> templateNameList) {
        for(String templateName : templateNameList){
            if(templateName.contains(lineItemType.trim())){
                IMPORT_TEMPLATE_ICON.click();
                IMPORT_TEMPLATE_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                TEMPLATE_SEARCH_BOX.fill(templateName);
                page.locator(String.format("//div[contains(@class,'item text-truncate') and contains(text(), '%s')]", templateName)).click();
                IMPORT_BUTTON.click();
                if(OVERRIDE_DIALOG.isVisible())
                    REPLACE_BUTTON.click();
                TEMPLATE_IMPORT_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
                break;
            }
        }
    }

    private String saveTargetingTemplate(String lineItemType) {
        String templateName = lineItemType + "_Template_" + CommonUtils.timeStampCalculation();
        TACTIC_SETTINGS_TAB.click();
        tacticSettings.verifyTacticSettingsText();
        SAVE_TEMPLATE_BUTTON.click();
        SAVE_TEMPLATE_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        TEMPLATE_NAME_TEXT.fill(templateName);
        SAVE_BUTTON.click();
        TEMPLATE_SAVED_SUCCESS_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return templateName;
    }

}