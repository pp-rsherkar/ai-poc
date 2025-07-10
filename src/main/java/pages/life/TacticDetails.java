package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import pages.Navigation;
import utils.CommonUtils;

import java.util.List;

public class TacticDetails {

    private final Page page;
    Campaigns campaigns = new Campaigns(DriverFactory.getPage());
    LineItemDetails lineItemDetails = new LineItemDetails(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());
    NPISmartList npiSmartList = new NPISmartList(DriverFactory.getPage());
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

    public boolean createTacticWithLineItems(List<String> lineItemTypeList, String advertiser, String newCampaignName, String campaignType, String budget, String newLineItemName, String lineBudget, String newTacticName, List<String> templateNameList) {
        for(String lineItemType : lineItemTypeList) {

            npiSmartList.clickPulsepointICon();
            campaigns.campaignDashboard();
            campaigns.createCampaign();
            campaigns.verifyCampaignText();
            //Campaign creation
            campaigns.selectAdvertiser(advertiser);
            campaigns.enterCampaignName(newCampaignName + "_" + CommonUtils.timeStampCalculation());
            campaigns.setCampaignType(campaignType);
            campaigns.enterBudget(budget);
            campaigns.saveCampaign();

            //Line Item creation
            lineItemDetails.enterLineItemName(newLineItemName + "_" + CommonUtils.randomNumberGeneration());
            lineItemDetails.selectLineItemType(lineItemType);
            navigation.clickOnIcon("Add Flight");
            lineItemDetails.enterLineItemBudget(lineBudget);
            lineItemDetails.enableLineItem();
            lineItemDetails.saveLineItem();

            //Tactic creation
            enterTacticName(newTacticName + "_" + CommonUtils.randomNumberGeneration());
            saveTacticDetails();
            importTargetingTemplate(lineItemType, templateNameList);
            saveTacticDetails();
            if(IMPORTED_TARGET_TEMPLATE.isVisible())
                return true;
        }
        return false;
    }

    private void importTargetingTemplate(String lineItemType, List<String> templateNameList) {
        for(String templateName : templateNameList){
            if(templateName.contains(lineItemType)){
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
}