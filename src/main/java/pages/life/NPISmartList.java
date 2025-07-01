package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.regex.Pattern;

public class NPISmartList {
    private final Page page;
    private final Locator CLICK_SMART_LIST;
    private final Locator LIST_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator PRESCRIBED_DRUG;
    private final Locator SEARCH_DRUG;
    private final Locator SEARCH_DRUG_FILL;
    private final Locator SELECT_DRUG;
    private final Locator VERIFY_DRUG;
    private final Locator LIFE_AVAILABLE_IN;
    private final Locator HCP365_AVAILABLE_IN;
    private final Locator PULSEPOINT_ICON;
    private final Locator SPINNER;
    private final Locator ADD_DRUG_BTN;

    public NPISmartList(Page page) {
        this.page = page;
        this.CLICK_SMART_LIST = page.getByText("Smart List", new Page.GetByTextOptions().setExact(true));
        this.LIST_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("List Name"));
        this.SEARCH_ADVERTISER = page.locator("app-npilists-manager").getByRole(AriaRole.COMBOBOX);
        this.SELECT_ADVERTISER = page.locator("//div[contains(@class,'dropdown-items ng-star-inserted')]");
        this.PRESCRIBED_DRUG = page.locator("//label[contains(@class, 'mat-checkbox-layout') and .//span[contains(text(), 'Prescribed Drug')]]");
        this.SEARCH_DRUG = page.locator("//ng-select[contains(@class, 'drugDropdown ')]");
        this.SEARCH_DRUG_FILL = page.locator("//ng-select[contains(@class, 'drugDropdown ')]//input");
        this.SELECT_DRUG = page.locator("//div[contains(@class,'dropdown-items')]");
        this.VERIFY_DRUG = page.locator("//ng-select[contains(@class, 'drugDropdown ')]//span[contains(@class,'ng-value-label')]");
        this.LIFE_AVAILABLE_IN = page.locator("//span[contains(text(),'Life')]");
        this.HCP365_AVAILABLE_IN = page.locator("//span[contains(text(),'HCP365')]");
        this.PULSEPOINT_ICON = page.locator("//div[@class='logo-lists']/img[@alt='logo']");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
        this.ADD_DRUG_BTN = page.locator("//span[contains(text(), 'Add Drug')]");

    }

    public void clickSmartList() {
        CLICK_SMART_LIST.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void enterListName(String listName) {
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        LIST_NAME.fill(listName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void selectPrescribedDrug() {
        PRESCRIBED_DRUG.scrollIntoViewIfNeeded();
        PRESCRIBED_DRUG.check();
        ADD_DRUG_BTN.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void selectDrug(String drug) {
        ADD_DRUG_BTN.click();
        SEARCH_DRUG.click();
        SEARCH_DRUG_FILL.fill(drug);
        SELECT_DRUG.click();
    }

    public String verifyDrug() {
        return VERIFY_DRUG.innerText();
    }

    public void selectProduct() {
        LIFE_AVAILABLE_IN.check();
        HCP365_AVAILABLE_IN.check();
    }

    public void clickPulsepointICon() {
        PULSEPOINT_ICON.click();
    }
}