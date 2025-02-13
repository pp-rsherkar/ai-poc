package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CampaignListing {
    private final Page page;
    private final Locator CLICK_SETTINGS;
    private final Locator SEARCH_CAMPAIGN;
    private final Locator CLICK_CAMPAIGN_SEARCH;
    private final Locator VERIFY_CREATED_CAMPAIGN;
    private final Locator EXPAND_CREATED_CAMPAIGN;
    private final Locator VERIFY_CREATED_LINE_ITEM;
    private final Locator EXPAND_CREATED_LINE_ITEM;
    private final Locator VERIFY_CREATED_TACTIC;

    public CampaignListing(Page page) {
        this.page = page;
        this.CLICK_SETTINGS = page.locator("//i[@class='icon gearIcon']");
        this.SEARCH_CAMPAIGN = page.locator("//input[@placeholder='Search' and contains(@class, 'gaTableSearch')]");
        this.CLICK_CAMPAIGN_SEARCH = page.locator("//div[@class='iconSprite search1']");
        this.VERIFY_CREATED_CAMPAIGN = page.locator("//span[contains(@class,'adv-camp-name color-black')]");
        this.EXPAND_CREATED_CAMPAIGN = page.locator("(//div[@class='icon_20 collapsed-thin'])[1]");
        this.VERIFY_CREATED_LINE_ITEM = page.locator("//span[contains(@class,'lineitem-name')]");
        this.EXPAND_CREATED_LINE_ITEM = page.locator("//div[@class='icon_20 collapsed-thin']");
        this.VERIFY_CREATED_TACTIC = page.locator("//span[contains(@class,'tactic-name')]");
    }

    public void setGroupByFilter() {
        CLICK_SETTINGS.click();
    }

    public void searchCreatedCampaign(String createdCampaign) {
        page.waitForLoadState();
        page.waitForSelector("//i[@class='star display-inline']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_CAMPAIGN.fill(createdCampaign);
        CLICK_CAMPAIGN_SEARCH.click();
    }

    public String verifyCreatedCampaign(String createdCampaign) {
        String campaignNameXpath = String.format("//span[contains(text(),'%s')]", createdCampaign);
        page.waitForSelector(campaignNameXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(campaignNameXpath).innerText();
    }

    public void expandCreatedCampaign() {
        page.waitForLoadState();
        EXPAND_CREATED_CAMPAIGN.click();
    }

    public String verifyCreatedLineItem(String lineItemNameRandom) {
        String lineItemNameXpath = String.format("//span[contains(text(),'%s')]", lineItemNameRandom);
        page.waitForSelector(lineItemNameXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(lineItemNameXpath).innerText();
    }

    public void expandCreatedLineItem() {
        page.waitForLoadState();
        EXPAND_CREATED_LINE_ITEM.click();
        page.waitForLoadState();
    }

    public String verifyCreatedTactic() {
        page.waitForLoadState();
        return VERIFY_CREATED_TACTIC.innerText();
    }
}