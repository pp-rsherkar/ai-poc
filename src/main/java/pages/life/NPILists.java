package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class NPILists {
    private static Locator SEARCH_NPILISTS;
    private final Page page;
    private final Locator NPI_LISTS;
    private final Locator NPI_LISTS_STG;
    private final Locator ADD_LIST;
    private final Locator CREATE_NPI_LIST;
    private final Locator STATIC_LIST;
    private final Locator LIFE_CHECKBOX;
    private final Locator HCP_CHECKBOX;
    private final Locator SEARCH_BOX;
    private final Locator NPI_LISTS_TITLE;

    public NPILists(Page page) {
        this.page = page;
        this.NPI_LISTS = page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("NPI Lists")).nth(3);
        this.NPI_LISTS_STG = page.getByText("NPI Lists");
        this.ADD_LIST = page.getByText("Add List");
        this.CREATE_NPI_LIST = page.getByText("Create New NPI List");
        this.STATIC_LIST = page.getByText("Plain static list of NPI");
        SEARCH_NPILISTS = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.LIFE_CHECKBOX = page.locator("xpath=//*[@id='mat-checkbox-4']/label/div");
        this.HCP_CHECKBOX = page.locator("xpath=//*[@id='mat-checkbox-5']/label/div");
        this.SEARCH_BOX = page.locator("//input[@placeholder='Search']");
        this.NPI_LISTS_TITLE = page.locator("//span[@class='header-title' and text()='NPI Lists']");
    }

    public void clickNPILists() {
        NPI_LISTS.click();
    }

    public void clickNPIListsStg() {
        NPI_LISTS_STG.click();
    }

    public void clickAddList() {
        ADD_LIST.click();
    }

    public String verifyNPIListText() {
        return CREATE_NPI_LIST.innerText();
    }

    public void clickStaticList() {
        STATIC_LIST.click();
    }

    public void searchNPILists(String Studio_list) {

        SEARCH_NPILISTS.fill(Studio_list);
        SEARCH_NPILISTS.press("Enter");
    }

    public void selectPublishedList(String Studio_list) {

        Locator STUDIO_LIST = page.getByText(Studio_list);
        STUDIO_LIST.click();
        SEARCH_NPILISTS.click();
        //added code because of an issue with displaying list after search (maybe env issue)
//        SEARCH_NPILISTS.fill("NP_test_Stud382");
//        SEARCH_NPILISTS.press("Enter");
//        page.getByText("NP_test_Stud382").click();
//        searchNPILists(Studio_list);
        //needs to remove once the issue is fixed
    }

    public boolean availablePlatforms() {
        return LIFE_CHECKBOX.isChecked() && HCP_CHECKBOX.isChecked();
    }

    public void searchList(String listName) {
        page.waitForLoadState();
        NPI_LISTS_TITLE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_BOX.first().fill(listName);
        SEARCH_BOX.first().press("Enter");
//        SEARCH_BOX.fill(listName);
//        SEARCH_BOX.press("Enter");
    }

    public void openSearchedList(String listName) {
        String listNameXpath = String.format("//div[contains(text(),'%s')]", listName);
        page.waitForSelector(listNameXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(listNameXpath).first().click();
    }
}

