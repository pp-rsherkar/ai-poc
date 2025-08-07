package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class TacticCreatives {
    private final Page page;
    private final Locator VERIFY_TACTIC_CREATIVES_PAGE;
    private final Locator SEARCH_CREATIVE;
    private final Locator CLICK_SEARCH;
    private final Locator SELECT_CREATIVE;
    private final Locator ASSIGN_CREATIVE_OK_BUTTON;
    private final Locator ENABLE_CREATIVE;
    private final Locator SAVE_TACTIC_CREATIVES;
    private final Locator TACTIC_CREATIVE_SUCCESS;
    private final Locator NAVIGATE_TO_CAMPAIGN_DASHBOARD;
    private final Locator VERIFY_CAMPAIGN_RUNNING;
    private final Locator ASSIGN_CREATIVE_TITLE;

    public TacticCreatives(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_CREATIVES_PAGE = page.locator("//div[text()='Creative(s)']");
        this.SEARCH_CREATIVE = page.locator("//input[contains(@class, 'gaTableSearch')]");
        this.CLICK_SEARCH = page.locator("//div[@class='iconSprite search search-icon']");
        this.SELECT_CREATIVE = page.locator("(//sui-checkbox[contains(@class,'checkbox-fullheight ui checkbox ng-untouched ng-pristine ng-valid')])[2]");
        this.ASSIGN_CREATIVE_OK_BUTTON = page.locator("//button[@class='ui primary button okButton' and normalize-space(text())='Ok']");
        this.ENABLE_CREATIVE = page.locator("//sui-checkbox[@class='toggle ui checkbox ng-untouched ng-pristine ng-valid']");
        this.SAVE_TACTIC_CREATIVES = page.locator("//span[text()='Save']");
        this.TACTIC_CREATIVE_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.NAVIGATE_TO_CAMPAIGN_DASHBOARD = page.locator("//div[contains(@class,'campaign-tile')]");
        this.VERIFY_CAMPAIGN_RUNNING = page.locator("//span[@class='status-label running']");
        this.ASSIGN_CREATIVE_TITLE = page.locator("//div[contains(text(),'Assign Creatives')]");
    }

    public String verifyTacticCreativesText() {
        return VERIFY_TACTIC_CREATIVES_PAGE.innerText();
    }

    public void assignCreatives(String creative) {
        ASSIGN_CREATIVE_TITLE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_CREATIVE.fill(creative);
        CLICK_SEARCH.click();
        page.waitForTimeout(1000);
        SELECT_CREATIVE.scrollIntoViewIfNeeded();
        SELECT_CREATIVE.click();
        ASSIGN_CREATIVE_OK_BUTTON.click();
    }

    public void enableCreative() {
        ENABLE_CREATIVE.click();
    }

    public void saveTacticCreatives() {
        SAVE_TACTIC_CREATIVES.click();
    }

    public String tacticCreativesSuccess() {
        return TACTIC_CREATIVE_SUCCESS.innerText();
    }

    public String verifyCampaignRunning() {
        return VERIFY_CAMPAIGN_RUNNING.innerText();
    }

    public void navigateToCampaignDashboard() {
        NAVIGATE_TO_CAMPAIGN_DASHBOARD.click();
    }
}
