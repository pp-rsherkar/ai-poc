package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.List;

public class NPILists {
    private final Page page;
    private final Locator NPI_LISTS;
    private final Locator NPI_LISTS_STG;
    private final Locator CREATE_NPI_LIST;
    private final Locator STATIC_LIST;
    private final Locator AVAILABLE_IN_CHECKBOX;
    private final Locator AVAILABLE_IN_CONTAINER;
    private final Locator SEARCH_NPILISTS;
    private final Locator PARENT_LIST_LABEL;
    private final Locator CREATE_NEW_LIST;
    private final Locator SEARCH_BOX;
    private final Locator SPINNER;
    private final Locator SMART_LIST;

    public NPILists(Page page) {
        this.page = page;
        this.NPI_LISTS = page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("NPI Lists")).nth(3);
        this.NPI_LISTS_STG = page.getByText("NPI Lists");
        this.CREATE_NPI_LIST = page.getByText("Create New NPI List");
        this.STATIC_LIST = page.getByText("Plain static list of NPI");
        this.SEARCH_NPILISTS = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.AVAILABLE_IN_CHECKBOX = page.locator("//input[contains(@class,'mat-checkbox-input') and @aria-checked='true']/ancestor::div/following-sibling::span");
        this.AVAILABLE_IN_CONTAINER = page.locator("//div[contains(@class,'npiGroupAvailableSettingContainer')]");
        this.PARENT_LIST_LABEL = page.locator("//span[@class='parentListLabel']");
        this.CREATE_NEW_LIST = page.locator("//span[normalize-space(text())='Create New List']");
        this.SEARCH_BOX = page.locator("//input[@placeholder='Search']");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
        this.SMART_LIST = page.getByText("Dynamic list of NPI");
    }

    public void clickNPILists() {
        NPI_LISTS.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void clickNPIListsStg() {
        NPI_LISTS_STG.click();
    }

    public void clickCreateNewList() {
        CREATE_NEW_LIST.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public String verifyNPIListText() {
        return CREATE_NPI_LIST.innerText();
    }

    public void clickStaticList() {
        STATIC_LIST.click();
    }

    public void clickSmartList() {
        SMART_LIST.click();

    }

    public void searchNPILists(String workspaceName) {
        SEARCH_NPILISTS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_NPILISTS.fill(workspaceName);
        SEARCH_NPILISTS.press("Enter");
    }

    public void selectPublishedList(String listname) {
        page.locator(String.format("//div[contains(text(),'%s')]", listname)).click();
        PARENT_LIST_LABEL.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public boolean availablePlatforms() {
        AVAILABLE_IN_CONTAINER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        AVAILABLE_IN_CONTAINER.scrollIntoViewIfNeeded();
        List<String> allTexts = AVAILABLE_IN_CHECKBOX.allInnerTexts();
        boolean containsLife = allTexts.stream().anyMatch(text -> text.contains("Life"));
        boolean containsHCP = allTexts.stream().anyMatch(text -> text.contains("HCP"));
        return containsLife && containsHCP;
    }

    public void searchList(String listName) {
        CREATE_NEW_LIST.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_BOX.first().fill(listName);
        SEARCH_BOX.first().press("Enter");
    }

    public void openSearchedList(String listName) {
        String listNameXpath = String.format("//div[contains(text(),'%s')]", listName);
        page.waitForSelector(listNameXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(listNameXpath).first().click();
        page.waitForSelector(".block-ui-spinner", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }
}
