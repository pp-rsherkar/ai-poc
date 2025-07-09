package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.CommonUtils;

import java.util.ArrayList;
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
    private final Locator TEMPLATE_NAME_ERROR;
    private final Locator TARGETING_RULES_ERROR;
    private final Locator TARGETING_RULES_DELETE_ICON;
    private final Locator SHOW_EXPRESSION_ICON;
    private final Locator TARGETING_CONTAINER;
    private final Locator SEARCH_FIRST_ITEM;
    private final Locator TARGET_TEMPLATE_DELETE_ICON;
    private final Locator DELETE_DIALOG;
    private final Locator REMOVE_BUTTON;
    private final Locator TEMPLATE_DELETED_ERROR;

    public TargetingTemplate(Page page) {
        this.page = page;
        this.NEW_TEMPLATE_BUTTON = page.locator("//span[contains(text(),'New Template')]");
        this.SEARCH_BOX = page.locator("//input[contains(@placeholder,'Search') and contains(@class,'search icon-pading')]");
        this.TEMPLATE_NAME_TEXT = page.locator("//input[contains(@placeholder,'Template Name')]");
        this.LINE_ITEMTYPE_DROPDOWN = page.locator("//div[contains(@class,'lineItemType')]");
        this.LINE_ITEMTYPE_VALUE = page.locator("//div[contains(@class,'lineItemType')]//../div[@class='inventory-key']");
        this.CHANNEL_DROPDOWN = page.locator("//div[contains(@class,'display-flex')]/following-sibling::div");
        this.CHANNEL_VALUE= page.locator("//div[contains(@class,'display-flex')]/following-sibling::div//../div[@class='inventory-key']");
        this.ADD_TARGETINGRULE_BTN = page.locator("//span[contains(text(),'Add Targeting Rule')]");
        this.SAVE_BUTTON = page.locator("//button[contains(text(),'Save')]");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
        this.TEMPLATE_NAME_ERROR = page.locator("//div[contains(text(),'Template Name is required')]");
        this.TARGETING_RULES_ERROR = page.locator("//div[contains(text(),'Please select atleast one targeting')]");
        this.TARGETING_RULES_DELETE_ICON = page.locator("//div[contains(@title,'delete')]");
        this.SHOW_EXPRESSION_ICON = page.locator("//app-icon-lable-link[@text='Show Expression']");
        this.TARGETING_CONTAINER = page.locator("//div[@class='targetingDetailsContainer']");
        this.SEARCH_FIRST_ITEM = page.locator("//div[contains(@class,'first-list-item')]");
        this.TARGET_TEMPLATE_DELETE_ICON = page.locator("//app-icon-lable-link[contains(@text,'Delete')]");
        this.DELETE_DIALOG = page.locator("//div[contains(text(),' Delete Target Template ')]");
        this.REMOVE_BUTTON = page.locator("//span[contains(text(),'Remove')]");
        this.TEMPLATE_DELETED_ERROR = page.locator("//div[contains(text(),'Target template deleted successfully')]");
    }

    public boolean verifyTargetingBtnAndSearchBox() {
        return NEW_TEMPLATE_BUTTON.isVisible() && SEARCH_BOX.isVisible();
    }

    public boolean clickAndVerifyTargetingTemplate() {
        NEW_TEMPLATE_BUTTON.click();
        return TEMPLATE_NAME_TEXT.isVisible() && LINE_ITEMTYPE_DROPDOWN.isVisible() && CHANNEL_DROPDOWN.isVisible() && ADD_TARGETINGRULE_BTN.isVisible();
    }

    public List<String> createAndSaveTargetingTemplate(String templateName, List<String> lineItemsList, List<String> channelList, Map<String, List<String>> rulesMap) {
        List<String> templateNameList = new ArrayList<>();
        for (String s : lineItemsList) {
            LINE_ITEMTYPE_DROPDOWN.click();
            for (int i = 0; i < LINE_ITEMTYPE_VALUE.count(); i++) {
                LINE_ITEMTYPE_VALUE.nth(i).scrollIntoViewIfNeeded();
                String lineItemText = LINE_ITEMTYPE_VALUE.nth(i).innerText();
                if (lineItemText.equalsIgnoreCase(s.trim())) {
                    LINE_ITEMTYPE_VALUE.nth(i).click();
                    String templateNameWithTimestamp = lineItemText + "_" + templateName + "_" + CommonUtils.timeStampCalculation();
                    TEMPLATE_NAME_TEXT.fill(templateNameWithTimestamp);
                    selectChannel(channelList);
                    addTargetingRules(rulesMap);
                    SAVE_BUTTON.click();
                    SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
                    templateNameList.add(templateNameWithTimestamp);
                    NEW_TEMPLATE_BUTTON.click();
                    break;
                }
            }
        }
        return templateNameList;
    }

    public void selectChannel(List<String> channelList) {
        if (CHANNEL_DROPDOWN.isVisible()) {
            CHANNEL_DROPDOWN.click();
            for (int i = 0; i < CHANNEL_VALUE.count(); i++) {
                String channelText = CHANNEL_VALUE.nth(i).innerText().trim();
                for (String channel : channelList) {
                    if (channelText.equalsIgnoreCase(channel.trim())) {
                        CHANNEL_VALUE.nth(i).click();
                        return;
                    }
                }
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

    public boolean searchTargetingTemplate(List<String> templateNameList) {
        boolean flag = false;
        for (String s : templateNameList) {
            SEARCH_BOX.fill(s.trim());
            page.locator(String.format("//div[contains(text(),'%s')]",s)).click();
            SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
            System.out.println(TEMPLATE_NAME_TEXT.innerText());
            if (TEMPLATE_NAME_TEXT.isVisible())
                flag = true;
        }
        return flag;
    }

    public String verifyErrorMessageForTemplateName(String targetingRule) {
        NEW_TEMPLATE_BUTTON.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        ADD_TARGETINGRULE_BTN.click();
        tacticSettings.selectRuleType(targetingRule);
        SAVE_BUTTON.click();
        TEMPLATE_NAME_ERROR.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return TEMPLATE_NAME_ERROR.innerText().trim();
    }

    public String verifyErrorMessageForTargetingRules(String templateName) {
        NEW_TEMPLATE_BUTTON.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        TEMPLATE_NAME_TEXT.fill(templateName+"_"+CommonUtils.timeStampCalculation());
        if(TARGETING_RULES_DELETE_ICON.isVisible())
            TARGETING_RULES_DELETE_ICON.click();
        SAVE_BUTTON.click();
        TARGETING_RULES_ERROR.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return TARGETING_RULES_ERROR.innerText().trim();
    }

    public boolean clickAndVerifyShowExpression(String templateName) {
        SEARCH_BOX.fill(templateName.trim());
        SEARCH_FIRST_ITEM.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        SHOW_EXPRESSION_ICON.click();
        TARGETING_CONTAINER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return TARGETING_CONTAINER.isVisible();
    }

    public boolean clickAndVerifyTargetTemplateEditable(String templateName) {
        SEARCH_BOX.fill(templateName.trim());
        SEARCH_FIRST_ITEM.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        TEMPLATE_NAME_TEXT.fill(templateName + "_edited_" + CommonUtils.timeStampCalculation());
        if (SAVE_BUTTON.isVisible()) {
            SAVE_BUTTON.click();
            return true;
        }
        return false;
    }

    public boolean clickAndVerifyTargetTemplateDeletion(String templateName) {
        SEARCH_BOX.fill(templateName.trim());
        SEARCH_FIRST_ITEM.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        TARGET_TEMPLATE_DELETE_ICON.click();
        DELETE_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        REMOVE_BUTTON.click();
        return TEMPLATE_DELETED_ERROR.isVisible();
    }

}