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
    private final Locator EXIT_TARGETING_PANEL;
    private final Locator TACTIC_SETTINGS_SUCCESS;

    public PMP(Page page) {
        this.page = page;
        this.GLOBAL_LIFE_SEARCH_MODAL = page.locator("//div[@class='iconSprite search-overlay-lens']");
        this.GLOBAL_SEARCH_TACTICS_TAB=page.locator("//a[normalize-space()='Tactics']");
        this.SEARCH_TACTIC_NAME=page.locator("//input[@id='global_search_input']");
        this.TACTIC_ROW=page.locator("//mark[normalize-space()='UNIQUE TACTIC FOR PMP 1203 SSS 1']");
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.ADD_TARGETING_RULE = page.locator("//div[@class='NewTargetingLink']//img[contains(@class,'icon')]");
        this.SEARCH_TARGETING = page.locator("//input[@name='search']");
        this.DEALS_TARGETING = page.locator("//a[@class='item targetType pointer firstItem']");
        this.PRIVATE_DEALS_TAB = page.locator("//a[normalize-space()='PRIVATE DEALS']");
        this.ASSIGN_PRIVATE_DEALS = page.locator("//div[@class='pmpDealsList']//div[2]//div[1]//div[5]//span[2]");
        this.PREMIUM_DEALS_TAB = page.locator("//a[normalize-space()='PREMIUM DEALS']");
        this.ASSIGN_PREMIUM_DEALS = page.locator("//div[@class='pmpDealsList']//div[2]//div[1]//div[5]//span[2]");
        this.EXIT_PMP_MODAL_OK_BUTTON = page.locator("//button[normalize-space()='Ok']");
        this.EXIT_TARGETING_PANEL=page.locator("//div[@class='right iconSprite icons_20-close pointer close_icon']");
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
        SEARCH_TARGETING.press("Enter");


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
        EXIT_TARGETING_PANEL.click();
    }

    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }

    public void tacticSettingsSuccess() {
        TACTIC_SETTINGS_SUCCESS.innerText();
    }
}

