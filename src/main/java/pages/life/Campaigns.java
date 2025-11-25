package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import factory.DriverFactory;
import utils.WaitUtility;

public class Campaigns {
    private final Page page;
    private final Locator CREATE_CAMPAIGN;
    private final Locator VERIFY_CAMPAIGN_PAGE;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator CAMPAIGN_NAME;
    private final Locator CAMPAIGN_TYPE_REGULAR;
    private final Locator CAMPAIGN_TYPE_SEQUENTIAL;
    private final Locator BUDGET;
    private final Locator SAVE_CAMPAIGN;
    private final Locator CAMPAIGN_DASHBOARD;
    private final Locator CAMPAIGN_SUCCESS;
    private final Locator LIFE_TIME_FILTER;
    private final Locator CAMPAIGN_ENTRIES;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public Campaigns(Page page) {
        this.page = page;
        this.CREATE_CAMPAIGN = page.locator("//button[text()='Create a Campaign']");
        this.VERIFY_CAMPAIGN_PAGE = page.locator("//div[text()='Create New Campaign']");
        this.SEARCH_ADVERTISER = page.locator("(//input[@placeholder='Select Advertiser'])[1]");
        this.SELECT_ADVERTISER = page.getByText("");
        this.CAMPAIGN_NAME = page.locator("//input[@placeholder='Campaign Name']");
        this.CAMPAIGN_TYPE_REGULAR = page.locator("//button[text()='Regular']");
        this.CAMPAIGN_TYPE_SEQUENTIAL = page.locator("//button[text()='Sequential']");
        this.BUDGET = page.locator("//input[@id='budgetcap']");
        this.SAVE_CAMPAIGN = page.locator("//span[text()='Save']");
        this.CAMPAIGN_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.CAMPAIGN_DASHBOARD = page.locator("//span[@class='breadCrumbRoot']");
        this.LIFE_TIME_FILTER = page.locator("//button[@data-title='Lifetime']");
        this.CAMPAIGN_ENTRIES = page.locator("//div[contains(@class,'name-section-wrapper')]");
    }

    public void createCampaign() {
        CREATE_CAMPAIGN.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String campaignDashboard() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        return this.page.title();
    }

    public String getCampaignText() {
        return VERIFY_CAMPAIGN_PAGE.innerText();
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.fill(advertiser);
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void enterCampaignName(String campaignNameRandom) {
        CAMPAIGN_NAME.fill(campaignNameRandom);
    }

    public void setCampaignType(String campaignType) {
        if ("Regular".equals(campaignType)) {
            CAMPAIGN_TYPE_REGULAR.click();
        } else if ("Sequential".equals(campaignType)) {
            CAMPAIGN_TYPE_SEQUENTIAL.click();
        }
    }

    public void enterBudget(String budget) {
        BUDGET.fill(budget);
    }

    public void saveCampaign() {
        SAVE_CAMPAIGN.click();
    }

    public String campaignSuccess() {
        String successMessage = CAMPAIGN_SUCCESS.innerText().trim();
        waitUtility.waitUntilSpinnerHidden();
        return successMessage;
    }

    public void navigateToCampaignDashboard() {
        CAMPAIGN_DASHBOARD.click();
        waitUtility.waitForLocatorVisible(CAMPAIGN_DASHBOARD);
        if (LIFE_TIME_FILTER.getAttribute("class").contains("inactive")) {
            LIFE_TIME_FILTER.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }
}