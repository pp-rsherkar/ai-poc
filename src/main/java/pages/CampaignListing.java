package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CampaignListing {
    private final Page page;
    private final Locator SEARCH_CAMPAIGN;
    private final Locator CLICK_CAMPAIGN_SEARCH;
    private final Locator VERIFY_CREATED_CAMPAIGN;
    private final Locator VERIFY_CREATED_LINE_ITEM;
    private final Locator EXPAND_CREATED_LINE_ITEM;
    private final Locator VERIFY_CREATED_TACTIC;

    public CampaignListing(Page page) {
        this.page = page;
        this.SEARCH_CAMPAIGN = page.locator("//input[@placeholder='Search' and contains(@class, 'gaTableSearch')]");
        this.CLICK_CAMPAIGN_SEARCH = page.locator("//div[@class='iconSprite search1']");
        this.VERIFY_CREATED_CAMPAIGN = page.locator("//div[@classname='campaignNameTooltip']");
        this.VERIFY_CREATED_LINE_ITEM = page.locator("//span[contains(@class,'lineitem-name')]");
        this.EXPAND_CREATED_LINE_ITEM = page.locator("(//div[@class='icon_20 collapsed-thin'])[1]");
        this.VERIFY_CREATED_TACTIC = page.locator("//span[@class='color-black tactic-name-section tactic-name-ellipsis ng-star-inserted']");
    }

    public void searchCreatedCampaign(String createdCampaign) {
        page.waitForLoadState();
        SEARCH_CAMPAIGN.fill(createdCampaign);
        CLICK_CAMPAIGN_SEARCH.click();
    }

    public String verifyCreatedCampaign() {
        page.waitForLoadState();
        return VERIFY_CREATED_CAMPAIGN.innerText();
    }

    public String verifyCreatedLineItem() {
        return VERIFY_CREATED_LINE_ITEM.innerText();
    }

    public void expandCreatedLineItem() {
        EXPAND_CREATED_LINE_ITEM.click();
    }

    public String verifyCreatedTactic() {
        page.waitForLoadState();
        return VERIFY_CREATED_TACTIC.innerText();
    }
}


