package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class PMP {
    private final Page page;
    private final Locator VERIFY_TACTIC_SETTINGS_PAGE;
    private final Locator ADD_TARGETING_RULE;
    private final Locator SAVE_TACTIC_SETTINGS;
    private final Locator SEARCH_TARGETING;
    private final Locator DEALS_TARGETING;
    private final Locator PRIVATE_DEALS_TAB;
    private final Locator ASSIGN_PRIVATE_DEALS;
    private final Locator PREMIUM_DEALS_TAB;
    private final Locator ASSIGN_PREMIUM_DEALS;
    private final Locator EXIT_PMP_MODAL_OK_BUTTON;
    private final Locator TACTIC_SETTINGS_SUCCESS;

    public PMP(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.ADD_TARGETING_RULE=page.locator("//span[text()='New Targeting Rule']");
        this.SEARCH_TARGETING=page.locator("//div[@class='searchBox ng-pristine ng-valid ng-touched']");
        this.DEALS_TARGETING=page.locator("//div[@class='item targetType pointer firstItem']");
        this.PRIVATE_DEALS_TAB=page.locator("//*[text()='PRIVATE DEALS']");
        this.ASSIGN_PRIVATE_DEALS=page.locator("//span[@class='addDeal ng-star-inserted']");
        this.PREMIUM_DEALS_TAB=page.locator("//*[text()='PREMIUM DEALS']");
        this.ASSIGN_PREMIUM_DEALS=page.locator("//span[@class='addDeal ng-star-inserted']");
        this.EXIT_PMP_MODAL_OK_BUTTON=page.locator("//div[@class='ui primary button okButton']");
        this.SAVE_TACTIC_SETTINGS = page.locator("//span[text()='Save']");
        this.TACTIC_SETTINGS_SUCCESS = page.locator("//*[text()='Success!']");
    }


    public String verifyTacticSettingsText() {
        return VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }
    public void setADD_TARGETING_RULE(){ADD_TARGETING_RULE.click();}
    public void SEARCH_TARGETING_RULE(){SEARCH_TARGETING.fill("Deals");}
    public void SET_DEALS_TARGETING(){DEALS_TARGETING.click();}
    public void PRIVATE_DEALS_ASSIGNMENT(){
        PRIVATE_DEALS_TAB.click();
        ASSIGN_PRIVATE_DEALS.click();}
    public void PREMIUM_DEALS_ASSIGNMENT(){
        PREMIUM_DEALS_TAB.click();
        ASSIGN_PREMIUM_DEALS.click();}

    public void EXIT_PMP_MODAL_OK_BUTTON_PRESS(){EXIT_PMP_MODAL_OK_BUTTON.click();}

    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }
    public String tacticSettingsSuccess() {
        return TACTIC_SETTINGS_SUCCESS.innerText();
    }
}

