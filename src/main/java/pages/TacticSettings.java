package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TacticSettings {
    private final Page page;
    private final Locator VERIFY_TACTIC_SETTINGS_PAGE;
    private final Locator SELECT_CHANNEL;
    private final Locator SEARCH_RULE_TYPE;
    private final Locator SELECT_RULE_TYPE;
    private final Locator SELECT_OPTION;
    private final Locator RULE_TYPE_OK_BUTTON;
    private final Locator RULE_TYPE_CLOSE;
    private final Locator SAVE_TACTIC_SETTINGS;
    private final Locator TACTIC_SETTINGS_SUCCESS;


    public TacticSettings(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.SELECT_CHANNEL = page.locator("(//div[@id='billingTypeDropdown'])[1]");
        this.SEARCH_RULE_TYPE = page.locator("//input[@name='search']");
        this.SELECT_RULE_TYPE = page.locator("//a[@classname='target-tooltip']");
        this.SELECT_OPTION = page.locator("//div[contains(@class,'include-default')]");
        this.RULE_TYPE_OK_BUTTON = page.locator("//button[@class='ui primary button okButton' and normalize-space(text())='Ok']");
        this.RULE_TYPE_CLOSE = page.locator("//div[contains(@class,'close_icon')]");
        this.SAVE_TACTIC_SETTINGS = page.locator("//span[text()='Save']");
        this.TACTIC_SETTINGS_SUCCESS = page.locator("//*[text()='Success!']");
    }

    public String verifyTacticSettingsText() {
        return VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }

    public void selectChannel(String channel) {
        SELECT_CHANNEL.click();
        SELECT_CHANNEL.locator("text="+channel).click();
    }

    public void selectRuleType() {
        SEARCH_RULE_TYPE.fill("NPI");
        SELECT_RULE_TYPE.first().click();
        SELECT_OPTION.first().click();
        RULE_TYPE_OK_BUTTON.click();
        RULE_TYPE_CLOSE.click();
    }

    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }

    public String tacticSettingsSuccess() {
        return TACTIC_SETTINGS_SUCCESS.innerText();
    }
}