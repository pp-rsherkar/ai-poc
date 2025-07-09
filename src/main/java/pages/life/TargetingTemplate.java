package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.CommonUtils;

import java.util.List;
import java.util.Map;

public class TargetingTemplate {
    TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());
    private final Page page;
    private final Locator NEW_TEMPLATE_BUTTON;
    private final Locator SEARCH_BOX;
    private final Locator TEMPLATE_NAME_TEXT;
    private final Locator LINE_ITEMTYPE_DROPDOWN;
    private final Locator LINE_ITEMTYPE_VALUE;
    private final Locator CHANNEL_DROPDOWN;
    private final Locator CHANNEL_VALUE;
    private final Locator ADD_TARGETINGRULE_BTN;
    private final Locator SAVE_BUTTON;
    private final Locator SPINNER;

    public TargetingTemplate(Page page) {
        this.page = page;
        this.NEW_TEMPLATE_BUTTON = page.locator("//span[contains(text(),'New Template')]");
        this.SEARCH_BOX = page.locator("//div[contains(@id,'searchKeyowrd')]");
        this.TEMPLATE_NAME_TEXT = page.locator("//input[contains(@placeholder,'Template Name')]");
        this.LINE_ITEMTYPE_DROPDOWN = page.locator("//div[contains(@class,'lineItemType')]");
        this.LINE_ITEMTYPE_VALUE = page.locator("//div[contains(@class,'lineItemType')]//../div[@class='inventory-key']");
        this.CHANNEL_DROPDOWN = page.locator("//div[contains(@class,'display-flex')]/following-sibling::div");
        this.CHANNEL_VALUE= page.locator("//div[contains(@class,'display-flex')]/following-sibling::div//../div[@class='inventory-key']");
        this.ADD_TARGETINGRULE_BTN = page.locator("//span[contains(text(),'Add Targeting Rule')]");
        this.SAVE_BUTTON = page.locator("//button[contains(text(),'Save')]");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
    }

    public boolean verifyTargetingBtnAndSearchBox() {
        return NEW_TEMPLATE_BUTTON.isVisible() && SEARCH_BOX.isVisible();
    }

    public boolean clickAndVerifyTargetingTemplate() {
        NEW_TEMPLATE_BUTTON.click();
        return TEMPLATE_NAME_TEXT.isVisible() && LINE_ITEMTYPE_DROPDOWN.isVisible() && CHANNEL_DROPDOWN.isVisible() && ADD_TARGETINGRULE_BTN.isVisible();
    }

    public void createAndSaveTargetingTemplate(String templateName, List<String> lineItemsList, String channel, Map<String, List<String>> rulesMap) {
        for(int i=0; i<lineItemsList.size(); i++){
            LINE_ITEMTYPE_DROPDOWN.click();
            if(LINE_ITEMTYPE_VALUE.nth(i).innerText().contains(lineItemsList.get(i)))
                LINE_ITEMTYPE_VALUE.nth(i).click();
            TEMPLATE_NAME_TEXT.fill(LINE_ITEMTYPE_VALUE.nth(i).innerText()+"_"+templateName+"_"+CommonUtils.timeStampCalculation());
            selectChannel(channel);
            addTargetingRules(rulesMap);
            SAVE_BUTTON.click();
            SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
            NEW_TEMPLATE_BUTTON.click();
        }
    }

    public void selectChannel(String channel){
        CHANNEL_DROPDOWN.click();
        for(int i=0; i<CHANNEL_VALUE.count(); i++){
            if(CHANNEL_VALUE.nth(i).innerText().contains(channel)){
                CHANNEL_VALUE.nth(i).click();
                break;
            }
        }
    }

    public void addTargetingRules(Map<String, List<String>> rulesMap){
        ADD_TARGETINGRULE_BTN.click();
        for (Map.Entry<String, List<String>> entry : rulesMap.entrySet()) {
            tacticSettings.selectMultipleRuleTypes(entry.getKey(), entry.getValue());
        }
        tacticSettings.closeRuleTypePanel();
    }

}