package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;

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
    private final Locator CAMPAIGN_LISTING;
    private final Locator CAMPAIGN_SUCCESS;

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
        this.CAMPAIGN_LISTING = page.locator("//span[@class='breadCrumbRoot']");
    }

    public void createCampaign() {
        CREATE_CAMPAIGN.click();
    }

    public String campaignDashboard() {
        page.waitForLoadState();
        return this.page.title();
    }

    public String verifyCampaignText() {
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
        return CAMPAIGN_SUCCESS.innerText();
    }

    public void navigateToCampaignListing() {
        CAMPAIGN_LISTING.click();
        page.waitForLoadState();
    }
}