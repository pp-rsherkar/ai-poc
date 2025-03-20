package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class NPIStaticList {
    private final Page page;
    private final Locator LIST_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator NPI_NUMBER;
    private final Locator AVAILABLE_IN;
    private final Locator SAVE_BUTTON;
    private final Locator LIST_SUCCESS;

    public NPIStaticList(Page page) {
        this.page = page;
        this.LIST_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("List Name"));
        this.SEARCH_ADVERTISER = page.locator("//div[text()= 'Select Advertiser']");
        this.SELECT_ADVERTISER = page.locator("//div[contains(@class,'dropdown-items ng-star-inserted')]");
        this.NPI_NUMBER = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("NPI Numbers (one number per"));
        this.AVAILABLE_IN = page.locator(".mat-checkbox-inner-container").first();
        this.SAVE_BUTTON = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        this.LIST_SUCCESS = page.getByText("× NPI list created");
    }

    public void enterListName(String npiListName) {
        LIST_NAME.fill(npiListName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void enterNPINumber(String npiNumber) {
        NPI_NUMBER.fill(npiNumber);
    }

    public void selectProduct() {
        AVAILABLE_IN.check();
    }

    public void saveList() {
        SAVE_BUTTON.click();
    }

    public String saveListSuccess() {
        return LIST_SUCCESS.innerText();
    }
}

