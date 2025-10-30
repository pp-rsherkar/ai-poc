package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CampaignSettings {
    private final Page page;
    private final Locator MAIN_MENU;
    private final Locator CAMPAIGN_SETTINGS;
    private final Locator BID_SETTINGS;
    private final Locator DEFAULT_BID_SETTINGS;
    private final Locator BASE_BID_PRICE;
    private final Locator MAX_BID_PRICE;

    public CampaignSettings(Page page) {
        this.page = page;
        this.MAIN_MENU = page.locator("//img[contains(@alt, 'menu')]");
        this.CAMPAIGN_SETTINGS = page.getByText("Campaign Settings");
        this.BID_SETTINGS = page.getByText("Bid Settings", new Page.GetByTextOptions().setExact(true));
        this.DEFAULT_BID_SETTINGS = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Default Bid Settings"));
        this.BASE_BID_PRICE = page.locator("//input[@formcontrolname='defBaseBidPrice']");
        this.MAX_BID_PRICE = page.getByRole(AriaRole.SPINBUTTON).nth(1);
    }

    public void campSettings() {
        MAIN_MENU.click();
        CAMPAIGN_SETTINGS.click();
    }

    public void bidSettings() {
        BID_SETTINGS.click();
    }

    public String getDefaultSettings() {
        return DEFAULT_BID_SETTINGS.textContent();
    }

    public String getBaseBidPrice() {
        return BASE_BID_PRICE.inputValue();
    }

    public String getMaxBidPrice() {
        return MAX_BID_PRICE.inputValue();
    }
}