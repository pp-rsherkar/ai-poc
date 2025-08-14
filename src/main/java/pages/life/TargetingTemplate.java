package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TargetingTemplate {
    TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator NEW_TEMPLATE_BUTTON;
    private final Locator SEARCH_BOX;
    private final Locator TEMPLATE_NAME_TEXT;
    private final Locator LINE_ITEMTYPE_DROPDOWN;
    private final Locator LINE_ITEMTYPE_VALUE;
    private final Locator CHANNEL_DROPDOWN;
    private final Locator CHANNEL_VALUE;
    private final Locator ADD_TARGETINGRULE_BUTTON;
    private final Locator SAVE_BUTTON;
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
    private final Locator TARGET_ITEM_LABEL;
    private final Locator TARGET_ITEM_VALUE;
    private final Locator TARGET_TEMPLATE_RULES;
    private final Locator SUCCESS_ALERT;

    public TargetingTemplate(Page page) {
        this.page = page;
        this.NEW_TEMPLATE_BUTTON = page.locator("//span[contains(text(),'New Template')]");
        this.SEARCH_BOX = page.locator("//input[contains(@placeholder,'Search') and contains(@class,'search icon-pading')]");
        this.TEMPLATE_NAME_TEXT = page.locator("//input[contains(@placeholder,'Template Name')]");
        this.LINE_ITEMTYPE_DROPDOWN = page.locator("//div[contains(@class,'lineItemType')]");
        this.LINE_ITEMTYPE_VALUE = page.locator("//div[contains(@class,'lineItemType')]//../div[@class='inventory-key']");
        this.CHANNEL_DROPDOWN = page.locator("//div[contains(@class,'display-flex')]/following-sibling::div");
        this.CHANNEL_VALUE= page.locator("//div[contains(@class,'display-flex')]/following-sibling::div//../div[@class='inventory-key']");
        this.ADD_TARGETINGRULE_BUTTON = page.locator("//span[contains(text(),'Add Targeting Rule')]");
        this.SAVE_BUTTON = page.locator("//button[contains(text(),'Save')]");
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
        this.TARGET_ITEM_LABEL = page.locator("//label[contains(@class,'target-item__label')]");
        this.TARGET_ITEM_VALUE = page.locator("//span[@class='target-ellipse']");
        this.TARGET_TEMPLATE_RULES = page.locator("//div[@class='targets-list']");
        this.SUCCESS_ALERT = page.locator("//div[contains(text(),'Target template created successfully')]");
    }

    public boolean verifyTargetingButtonAndSearchBox() {
        return NEW_TEMPLATE_BUTTON.isVisible() && SEARCH_BOX.isVisible();
    }

    public boolean clickAndVerifyTargetingTemplate() {
        NEW_TEMPLATE_BUTTON.click();
        return TEMPLATE_NAME_TEXT.isVisible() && LINE_ITEMTYPE_DROPDOWN.isVisible() && CHANNEL_DROPDOWN.isVisible() && ADD_TARGETINGRULE_BUTTON.isVisible();
    }

    public Map<String, Map<String, String>> createAndSaveTargetingTemplate(String templateName, List<String> lineItemsList, List<String> channelList, Map<String, List<String>> rulesMap) {
        Map<String, Map<String, String>> lineItemsToRuleCounts = new HashMap<>();

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
                    SUCCESS_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                    waitUtility.waitUntilLoaderHidden();
                    Map<String, String> labelCountMap = fetchTargetingRulesCountFromTargeting();
                    lineItemsToRuleCounts.put(templateNameWithTimestamp, labelCountMap);
                    NEW_TEMPLATE_BUTTON.click();
                    break;
                }
            }
        }
        return lineItemsToRuleCounts;
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

    public void addTargetingRules(Map<String, List<String>> rulesMap) {
        ADD_TARGETINGRULE_BUTTON.click();
        for (Map.Entry<String, List<String>> entry : rulesMap.entrySet()) {
            tacticSettings.selectMultipleRuleTypes(entry.getKey(), entry.getValue());
        }
        tacticSettings.closeRuleTypePanel();
    }

    public Map<String, String> fetchTargetingRulesCountFromTargeting() {
        Map<String, String> labelCountMap = new HashMap<>();
        int count = TARGET_ITEM_LABEL.count();
        for (int i = 0; i < count; i++) {
            labelCountMap.put(TARGET_ITEM_LABEL.nth(i).innerText().trim(), TARGET_ITEM_VALUE.nth(i).innerText().trim());
        }
        return labelCountMap;
    }

    public boolean searchTargetingTemplate(List<String> templateNameList) {
        boolean flag = false;
        for (String s : templateNameList) {
            SEARCH_BOX.fill(s.trim());
            page.locator(String.format("//div[contains(text(),'%s')]",s)).click();
            waitUtility.waitUntilLoaderHidden();
            if (TEMPLATE_NAME_TEXT.isVisible() && TARGET_TEMPLATE_RULES.isVisible())
                flag = true;
        }
        return flag;
    }

    public String verifyErrorMessageForTemplateName(String targetingRule) {
        String alert = " ";
        NEW_TEMPLATE_BUTTON.click();
        waitUtility.waitUntilLoaderHidden();
        ADD_TARGETINGRULE_BUTTON.click();
        tacticSettings.selectRuleType(targetingRule);
        SAVE_BUTTON.click();
        alert = TEMPLATE_NAME_ERROR.innerText().trim();
        waitUtility.waitForLocatorHidden(TEMPLATE_NAME_ERROR);
        return alert;
    }

    public String verifyErrorMessageForTargetingRules(String templateName) {
        String alert = " ";
        NEW_TEMPLATE_BUTTON.click();
        waitUtility.waitUntilLoaderHidden();
        TEMPLATE_NAME_TEXT.fill(templateName+"_"+CommonUtils.timeStampCalculation());
        if(TARGETING_RULES_DELETE_ICON.isVisible())
            TARGETING_RULES_DELETE_ICON.click();
        SAVE_BUTTON.click();
        alert = TARGETING_RULES_ERROR.innerText().trim();
        waitUtility.waitForLocatorHidden(TARGETING_RULES_ERROR);
        return alert;
    }

    public boolean clickAndVerifyShowExpression(String templateName) {
        SEARCH_BOX.fill(templateName.trim());
        SEARCH_FIRST_ITEM.click();
        waitUtility.waitUntilLoaderHidden();
        SHOW_EXPRESSION_ICON.click();
        waitUtility.waitForLocatorVisible(TARGETING_CONTAINER);
        return TARGETING_CONTAINER.isVisible();
    }

    public boolean clickAndVerifyTargetTemplateEditable(String templateName) {
        SEARCH_BOX.fill(templateName.trim());
        SEARCH_FIRST_ITEM.click();
        waitUtility.waitUntilLoaderHidden();
        TEMPLATE_NAME_TEXT.fill(templateName + "_edited_" + CommonUtils.timeStampCalculation());
        if (SAVE_BUTTON.isVisible()) {
            SAVE_BUTTON.click();
            waitUtility.waitUntilLoaderHidden();
            return true;
        }
        return false;
    }

    public String clickAndVerifyTargetTemplateDeletion(String templateName) {
        SEARCH_BOX.fill(templateName.trim());
        SEARCH_FIRST_ITEM.click();
        waitUtility.waitUntilLoaderHidden();
        TARGET_TEMPLATE_DELETE_ICON.click();
        waitUtility.waitForLocatorVisible(DELETE_DIALOG);
        REMOVE_BUTTON.click();
        waitUtility.waitUntilLoaderHidden();
        return TEMPLATE_DELETED_ERROR.innerText();
    }

}