package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ConversionPixel {
    private final Page page;
    private final Locator PIXEL_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator PIXEL_SCOPE_DROPDOWN;
    private final Locator PIXEL_SCOPE_OPTION;
    private final Locator PIXEL_TYPE_DROPDOWN;
    private final Locator PIXEL_TYPE_OPTION;

    public ConversionPixel(Page page) {
        this.page = page;
        this.PIXEL_NAME = page.locator("//input[@placeholder='Pixel Name']");
        this.SEARCH_ADVERTISER = page.locator("//input[contains(@class,'dropdown-search') and @placeholder='Advertisers']");
        this.SELECT_ADVERTISER = page.locator("//div[contains(@class,'item text-truncate')]");
        this.PIXEL_SCOPE_DROPDOWN = page.locator("//sui-select[@placeholder='Scope' and contains(@class,'filter-dropdown conversion-pixel')]");
        this.PIXEL_SCOPE_OPTION = page.locator("//sui-select-option[contains(@class,'item')]/span");
        this.PIXEL_TYPE_DROPDOWN = page.locator("//sui-select[@placeholder='Select Conversion Type' and contains(@class,'filter-dropdown conversion-pixel')]");
        this.PIXEL_TYPE_OPTION = page.locator("//sui-select-option[contains(@class,'item')]/span");
    }

    public void enterPixelName(String pixelName) {
        PIXEL_NAME.fill(pixelName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void selectConversionPixelScope(String scope) {
        PIXEL_SCOPE_DROPDOWN.click();
        PIXEL_SCOPE_OPTION.locator("text=" + scope).click();
    }

    public void selectConversionPixelType(String type) {
        PIXEL_TYPE_DROPDOWN.click();
        PIXEL_TYPE_OPTION.locator("text=" + type).click();
    }
}