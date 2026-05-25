package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import factory.DriverFactory;
import pages.Navigation;

import java.math.BigDecimal;

public class CampaignSettings {
    private final Page page;
    private final Locator CAMPAIGN_SETTINGS;
    private final Locator BID_SETTINGS;
    private final Locator DEFAULT_BID_SETTINGS;
    private final Locator BASE_BID_PRICE;
    private final Locator MAX_BID_PRICE;
    private final Locator HIGHEST_BID_PRICE;
    Navigation navigation = new Navigation(DriverFactory.getPage());

    public CampaignSettings(Page page) {
        this.page = page;
        this.CAMPAIGN_SETTINGS = page.getByText("Campaign Settings");
        this.BID_SETTINGS = page.getByText("Bid Settings", new Page.GetByTextOptions().setExact(true));
        this.DEFAULT_BID_SETTINGS = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Default Bid Settings"));
        this.BASE_BID_PRICE = page.locator("//input[@formcontrolname='defBaseBidPrice']");
        this.MAX_BID_PRICE = page.locator("//input[@formcontrolname='defMaxBidPrice']");
        this.HIGHEST_BID_PRICE = page.locator("//input[@formcontrolname='highestMaxBidPrice']");
    }

    public void campaignSettingsLink() {
        navigation.clickSubMenu();
        CAMPAIGN_SETTINGS.click();
    }

    public void bidSettingsTab() {
        BID_SETTINGS.click();
    }

    public String getDefaultSettings() {
        return DEFAULT_BID_SETTINGS.textContent();
    }

    public BigDecimal getBaseBidPrice() {
        return new BigDecimal(BASE_BID_PRICE.inputValue());
    }

    public BigDecimal getMaxBidPrice() {
        return new BigDecimal(MAX_BID_PRICE.inputValue());
    }

    public BigDecimal getHighestPossibleMaxBidPrice() {
        return new BigDecimal(HIGHEST_BID_PRICE.inputValue());
    }
}