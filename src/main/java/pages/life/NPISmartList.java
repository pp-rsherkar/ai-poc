package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

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


    public NPISmartList(Page page) {
        this.page = page;
        this.CLICK_SMART_LIST = page.getByText("Smart List", new Page.GetByTextOptions().setExact(true));
        this.LIST_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("List Name"));
        this.SEARCH_ADVERTISER = page.locator("app-npilists-manager").getByRole(AriaRole.COMBOBOX);
        this.SELECT_ADVERTISER = page.getByText("Z_Automation");
        this.PRESCRIBED_DRUG = page.locator("#mat-checkbox-12 > .mat-checkbox-layout > .mat-checkbox-inner-container");
        this.SEARCH_DRUG = page.getByRole(AriaRole.LISTBOX).filter(new Locator.FilterOptions().setHasText(Pattern.compile("^$"))).getByRole(AriaRole.COMBOBOX);
        this.SEARCH_DRUG_FILL = page.getByRole(AriaRole.LISTBOX).filter(new Locator.FilterOptions().setHasText(Pattern.compile("Type to search"))).getByRole(AriaRole.COMBOBOX);
        this.SELECT_DRUG = page.getByText("Glynase0009-0352, 0009-0341, 0009-");
        this.VERIFY_DRUG = page.locator("//span[@class='ng-value-label ng-star-inserted'][contains(text(),'Glynase')]");
        this.LIFE_AVAILABLE_IN = page.locator("//span[contains(text(),'Life')]");
        this.HCP365_AVAILABLE_IN = page.locator("//span[contains(text(),'HCP365')]");
        this.PULSEPOINT_ICON = page.locator("//div[@class='logo-lists']/img[@alt='logo']");

    }


    public void clickSmartList() {
        CLICK_SMART_LIST.click();
    }

    public void enterListName(String listName) {
        LIST_NAME.fill(listName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void selectPrescribedDrug() {
        PRESCRIBED_DRUG.check();
    }

    public void selectDrug(String Drug) {
        SEARCH_DRUG.click();
        SEARCH_DRUG_FILL.fill("nas");
        SELECT_DRUG.locator(page.getByText("Glynase0009-0352, 0009-0341, 0009-")).click();
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

