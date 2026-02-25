package pages.healPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import pages.life.CampaignDashboard;
import utils.WaitUtility;

public class HealingPageObject {
    private final Page page;
    public final Locator USERNAME;
    private final Locator PASSWORD;
    private final Locator LOGIN_BUTTON;
    private final Locator LIFE;
    private final Locator SIGNAL;
    private final Locator STUDIO;
    private final Locator SUB_MENU;
    private final Locator ACCOUNT_NAME;
    private final Locator ACCOUNT_SEARCH;
    private final Locator ACCOUNT_ITEM;
    private final Locator STUDIO_TITLE;
    private final Locator CAMPAIGN_PAGE_TEXT;
    WaitUtility waitUtility;
    CampaignDashboard campaignDashboard;
    HealingActions healingActions;

    public HealingPageObject(Page page) {
        this.page = page;
        this.healingActions = new HealingActions(page);
        this.waitUtility = new WaitUtility(page);
        this.campaignDashboard = new CampaignDashboard(page);
        this.USERNAME = page.locator("#UserNe");
        this.PASSWORD = page.locator("#Passwd");
        this.LOGIN_BUTTON = page.locator(".loginLabe");
        this.LIFE = page.locator("//span[contains(@class,'buyerPorlLink')]");
        this.SIGNAL = page.getByText("Signal");
        this.STUDIO = page.locator("//div[contains(@class,'genomeMenuItem')]");
        this.SUB_MENU = page.locator("//img[contains(@alt,'menu')]");
        this.ACCOUNT_NAME = page.locator("//div[@class='accountname']");
        this.ACCOUNT_SEARCH = page.locator("//div[@id='accountSwitcher']/input[@placeholder='Search']");
        this.ACCOUNT_ITEM = page.locator("//div[@id='accountSwitcher']//div[@class='item']");
        this.STUDIO_TITLE = page.locator("//div[text()='Studio']");
        this.CAMPAIGN_PAGE_TEXT = page.locator("//span[contains(text(),'Campaigns')]");
    }

    public void navigateToUrl(String url) {
        this.page.navigate(url); // Standard navigation doesn't usually need healing
    }

    public void enterUsername(String userName) {
        healingActions.safeFill(USERNAME, userName, "The username input field on Login page");
    }

    public void enterPassword(String password) {
        healingActions.safeFill(PASSWORD, password, "The password input field on Login page");
    }

    public void clickLogin() {
        healingActions.safeClick(LOGIN_BUTTON, "The Login button");
    }

    public void navigateToLife() {
        healingActions.safeClick(LIFE, "The 'Life' link available on the Admin Dashboard");
        waitUtility.waitUntilSpinnerHidden();
    }

    public void navigateToHCP() {
        healingActions.safeClick(SIGNAL, "The 'Signal' link for HCP365");
    }

    public void navigateToStudio() {
        waitUtility.waitForLocatorVisible(SUB_MENU);
        healingActions.safeClick(SUB_MENU, "The hamburger/side menu icon");

        STUDIO.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        healingActions.safeClick(STUDIO, "The Studio menu item");
        waitUtility.waitForLocatorVisible(STUDIO_TITLE);
    }

    public void selectAccount(String account) {
        if (ACCOUNT_NAME.innerText().contains("buyer2")) {
            healingActions.safeClick(ACCOUNT_NAME, "The account name dropdown in the header");
            ACCOUNT_SEARCH.fill(account);
            page.waitForLoadState(LoadState.LOAD);
            healingActions.safeClick(ACCOUNT_ITEM, "The account item in the account switcher dropdown");
        }
        waitUtility.waitUntilSpinnerHidden();
    }

    public String isCampaignDashboardVisibleWithTitle(String text) {
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitUntilPreLoaderHidden();
        page.waitForCondition(() -> CAMPAIGN_PAGE_TEXT.filter(new Locator.FilterOptions().setHasText(text)).count() == 1);
        return CAMPAIGN_PAGE_TEXT.innerText();
    }
}
