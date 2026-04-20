package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.WaitUtility;

public class CuratedMarket {

    private final Page page;
    private final Locator MARKET_LIST_PANEL;
    private final Locator SEARCH_MARKET;
    WaitUtility waitUtility;

    public CuratedMarket(Page page) {
        this.page = page;
        this.waitUtility = new WaitUtility(page);
        this.MARKET_LIST_PANEL = page.locator("//div[contains(@class, 'markets-list-panel')]");
        this.SEARCH_MARKET = page.locator("//div[@id='searchMarkets']/input");
    }

    public boolean isCuratedMarketTabDisplayed() {
        return MARKET_LIST_PANEL.isVisible();
    }

    public void searchCuratedMarket(String metricName) {
        SEARCH_MARKET.fill(metricName);
        page.keyboard().press("Enter");
        waitUtility.waitUntilSpinnerHidden();
    }

    public boolean isCuratedMarketCreatedAvailable(String marketName) {
        Locator marketLocator = page.locator(String.format("//div[contains(text(),'%s')]", marketName));
        return marketLocator.isVisible();
    }

    public String fetchMediaTypeForCuratedMarket(String marketName) {
        Locator mediaTypeLocator = page.locator(String.format("//div[contains(text(),'%s')]/following-sibling::div//span[contains(text(),'Media Type:')]/following-sibling::span//span", marketName));
        return mediaTypeLocator.textContent().trim();
    }

    public String fetchMarketIdForCuratedMarket(String marketName) {
        Locator marketIdLocator = page.locator(String.format("//div[contains(text(),'%s')]/following-sibling::div//span[contains(text(), 'ID:')]/following-sibling::span[1]", marketName));
        return marketIdLocator.textContent().trim();
    }

    public String fetchFloorPriceForCuratedMarket(String marketName) {
        Locator floorPriceLocator = page.locator(String.format("//div[contains(text(),'%s')]/parent::div/following-sibling::div//div[text()='Floor Price']/preceding-sibling::div", marketName));
        return floorPriceLocator.textContent().replace("$", "").replace(",", "").trim();
    }
}
