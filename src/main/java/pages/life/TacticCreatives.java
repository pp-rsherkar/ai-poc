package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.WaitUtility;

public class TacticCreatives {
    private final Page page;
    private final Locator VERIFY_TACTIC_CREATIVES_PAGE;
    private final Locator SEARCH_CREATIVE;
    private final Locator CLICK_SEARCH;
    private final Locator ASSIGN_CREATIVE_OK_BUTTON;
    private final Locator ENABLE_CREATIVE;
    private final Locator SAVE_TACTIC_CREATIVES;
    private final Locator TACTIC_CREATIVE_SUCCESS;
    private final Locator NAVIGATE_TO_CAMPAIGN_DASHBOARD;
    private final Locator VERIFY_CAMPAIGN_RUNNING;
    private final Locator ASSIGN_CREATIVE_TITLE;
    private final Locator CREATIVE_TAB;
    private final Locator ASSIGN_EXISTING_CREATIVE;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public TacticCreatives(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_CREATIVES_PAGE = page.locator("//div[text()='Creative(s)']");
        this.SEARCH_CREATIVE = page.locator("//input[contains(@class, 'gaTableSearch')]");
        this.CLICK_SEARCH = page.locator("//div[contains(@class,'gaTableSearchBtn')]");
        this.ASSIGN_CREATIVE_OK_BUTTON = page.locator("//button[@class='ui primary button okButton' and normalize-space(text())='Ok']");
        this.ENABLE_CREATIVE = page.locator("//sui-checkbox[@class='toggle ui checkbox ng-untouched ng-pristine ng-valid']");
        this.SAVE_TACTIC_CREATIVES = page.locator("//span[text()='Save']");
        this.TACTIC_CREATIVE_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.NAVIGATE_TO_CAMPAIGN_DASHBOARD = page.locator("//div[contains(@class,'campaign-tile')]");
        this.VERIFY_CAMPAIGN_RUNNING = page.locator("//span[@class='status-label running']");
        this.ASSIGN_CREATIVE_TITLE = page.locator("//div[contains(text(),'Assign Creatives')]");
        this.CREATIVE_TAB = page.locator("//a[contains(text(),'Creatives')]");
        this.ASSIGN_EXISTING_CREATIVE = page.locator("//span[contains(text(),'Assign Existing Creatives')]");
    }

    public String getTacticCreativesText() {
        return VERIFY_TACTIC_CREATIVES_PAGE.innerText();
    }

    public void assignCreatives(String creative) {
        Locator selectCreative = page.locator(String.format("//div[contains(text(),'%s')]/ancestor::td/preceding-sibling::td/sui-checkbox",creative));
        ASSIGN_CREATIVE_TITLE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_CREATIVE.fill(creative);
        CLICK_SEARCH.click();
        waitUtility.waitForLocatorVisible(selectCreative.first(), 5000);
        selectCreative.first().scrollIntoViewIfNeeded();
        if(!selectCreative.first().getAttribute("class").contains("checked"))
            selectCreative.first().click();
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

    public String getCampaignRunningText() {
        return VERIFY_CAMPAIGN_RUNNING.innerText();
    }

    public void navigateToCampaignDashboard() {
        NAVIGATE_TO_CAMPAIGN_DASHBOARD.click();
    }

    public void clickCreativeTab() {
        CREATIVE_TAB.click();
    }

    public void clickAssignCreatives() {
        ASSIGN_EXISTING_CREATIVE.click();
    }

    public boolean isCreativeAssigned(String CreativeName) {
        waitUtility.waitForElementVisible(String.format("//td[@title='%s']", CreativeName));
        return page.locator(String.format("//td[@title='%s']", CreativeName)).isVisible();
    }
}