package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class NPILists {
    private final Page page;
    private final Locator NPI_LISTS;
    private final Locator NPI_LISTS_STG;
    private final Locator CREATE_NPI_LIST;
    private final Locator STATIC_LIST;
    private final Locator LIFE_CHECKBOX;
    private final Locator HCP_CHECKBOX;
    private final Locator SEARCH_NPILISTS;
    private final Locator PARENT_LIST_LABEL;
    private final Locator CREATE_NEW_LIST;
    private final Locator SEARCH_BOX;
    private final Locator SPINNER;

    public NPILists(Page page) {
        this.page = page;
        this.NPI_LISTS = page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("NPI Lists")).nth(3);
        this.NPI_LISTS_STG = page.getByText("NPI Lists");
        this.CREATE_NPI_LIST = page.getByText("Create New NPI List");
        this.STATIC_LIST = page.getByText("Plain static list of NPI");
        this.SEARCH_NPILISTS = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.LIFE_CHECKBOX = page.locator("xpath=//*[@id='mat-checkbox-4']/label/div");
        this.HCP_CHECKBOX = page.locator("xpath=//*[@id='mat-checkbox-5']/label/div");
        this.PARENT_LIST_LABEL = page.locator("//span[@class='parentListLabel']");
        this.CREATE_NEW_LIST = page.locator("//span[normalize-space(text())='Create New List']");
        this.SEARCH_BOX = page.locator("//input[@placeholder='Search']");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
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

    public void searchNPILists(String workspaceName) {
        SEARCH_NPILISTS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_NPILISTS.fill(workspaceName);
        SEARCH_NPILISTS.press("Enter");
    }

    public void selectPublishedList(String listname) {
        page.locator(String.format("//div[contains(text(),'%s')]", listname)).click();
        PARENT_LIST_LABEL.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public boolean availablePlatforms() {
        return LIFE_CHECKBOX.isChecked() && HCP_CHECKBOX.isChecked();
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
