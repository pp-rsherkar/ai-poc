package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class PMP {
    private final Page page;
    private final Locator GLOBAL_LIFE_SEARCH_MODAL;
    private final Locator GLOBAL_SEARCH_TACTICS_TAB;
    private final Locator SEARCH_TACTIC_NAME;
    private final Locator TACTIC_ROW;
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
        this.GLOBAL_LIFE_SEARCH_MODAL = page.locator("//div[@class='iconSprite search-overlay-lens']");
        this.GLOBAL_SEARCH_TACTICS_TAB=page.locator("//*[text()='Tactics'] and contains(@class, 'item tabBorder'");
        this.SEARCH_TACTIC_NAME=page.locator("//div[@id='global_search_input']");
        this.TACTIC_ROW=page.locator("//div[@class='description truncate']");
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.ADD_TARGETING_RULE = page.locator("//span[text()='New Targeting Rule']");
        this.SEARCH_TARGETING = page.locator("//div[@class='searchBox ng-pristine ng-valid ng-touched']");
        this.DEALS_TARGETING = page.locator("//div[@class='item targetType pointer firstItem']");
        this.PRIVATE_DEALS_TAB = page.locator("//*[text()='PRIVATE DEALS']");
        this.ASSIGN_PRIVATE_DEALS = page.locator("//span[@class='addDeal ng-star-inserted']");
        this.PREMIUM_DEALS_TAB = page.locator("//*[text()='PREMIUM DEALS']");
        this.ASSIGN_PREMIUM_DEALS = page.locator("//span[@class='addDeal ng-star-inserted']");
        this.EXIT_PMP_MODAL_OK_BUTTON = page.locator("//div[@class='ui primary button okButton']");
        this.SAVE_TACTIC_SETTINGS = page.locator("//span[text()='Save']");
        this.TACTIC_SETTINGS_SUCCESS = page.locator("//*[text()='Success!']");
    }

    public void navigateToTactic(String TACTIC) {
        GLOBAL_LIFE_SEARCH_MODAL.click();
        GLOBAL_SEARCH_TACTICS_TAB.click();
        SEARCH_TACTIC_NAME.fill(TACTIC);
        page.waitForLoadState();
        TACTIC_ROW.click();
    }

    public void verifyTacticSettingsText() {
        VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }

    public void setADD_TARGETING_RULE() {
        ADD_TARGETING_RULE.click();
    }

    public void SEARCH_TARGETING_RULE() {
        SEARCH_TARGETING.fill("Deals");
        page.waitForLoadState();

    }

    public void SET_DEALS_TARGETING() {
        DEALS_TARGETING.click();
    }

    public void PRIVATE_DEALS_ASSIGNMENT() {
        PRIVATE_DEALS_TAB.click();
        ASSIGN_PRIVATE_DEALS.click();
    }

    public void PREMIUM_DEALS_ASSIGNMENT() {
        PREMIUM_DEALS_TAB.click();
        ASSIGN_PREMIUM_DEALS.click();
    }

    public void EXIT_PMP_MODAL_OK_BUTTON_PRESS() {
        EXIT_PMP_MODAL_OK_BUTTON.click();
    }

    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }

    public String tacticSettingsSuccess() {
        return TACTIC_SETTINGS_SUCCESS.innerText();
    }
}

