package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import factory.DriverFactory;
import utils.WaitUtility;

import java.util.List;

public class NPILists {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
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
    private final Locator SMART_LIST;
    private final Locator AUTO_IMPORTED_LIST;
    private final Locator STUDIO_FILTER_LABEL;

    public NPILists(Page page) {
        this.page = page;
        this.NPI_LISTS = page.locator("//div[contains(@class, 'menu') and contains(text(),'NPI Lists')]");
        this.NPI_LISTS_STG = page.getByText("NPI Lists");
        this.CREATE_NPI_LIST = page.getByText("Create New NPI List");
        this.STATIC_LIST = page.getByText("Plain static list of NPI");
        this.SEARCH_NPILISTS = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.AVAILABLE_IN_CHECKBOX = page.locator("//input[contains(@class,'mat-checkbox-input') and @aria-checked='true']/ancestor::div/following-sibling::span");
        this.AVAILABLE_IN_CONTAINER = page.locator("//div[contains(@class,'npiGroupAvailableSettingContainer')]");
        this.PARENT_LIST_LABEL = page.locator("//span[@class='parentListLabel']");
        this.CREATE_NEW_LIST = page.locator("//span[normalize-space(text())='Create New List']");
        this.SEARCH_BOX = page.locator("//input[@placeholder='Search']");
        this.SMART_LIST = page.getByText("Dynamic list of NPIs based on");
        this.AUTO_IMPORTED_LIST = page.locator("//app-npilisttype[@listtypename='Auto-Imported List']");
        this.STUDIO_FILTER_LABEL = page.locator("//span[contains(@class,'studioLabel')]");
    }

    public void clickNPILists() {
        waitUtility.waitForLocatorVisible(NPI_LISTS);
        NPI_LISTS.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void clickNPIListsStg() {
        NPI_LISTS_STG.click();
    }

    public void clickCreateNewList() {
        CREATE_NEW_LIST.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String verifyNPIListText() {
        waitUtility.waitForLocatorVisible(CREATE_NPI_LIST);
        return CREATE_NPI_LIST.innerText();
    }

    public void clickStaticList() {
        STATIC_LIST.click();
    }

    public void clickSmartList() {
        SMART_LIST.click();
    }

    public void searchNPILists(String workspaceName) {
        waitUtility.waitForLocatorVisible(SEARCH_NPILISTS);
        SEARCH_NPILISTS.fill(workspaceName);
        SEARCH_NPILISTS.press("Enter");
    }

    public void selectPublishedList(String listname) {
        page.locator(String.format("//div[contains(text(),'%s')]", listname)).click();
        waitUtility.waitForLocatorVisible(PARENT_LIST_LABEL);
        waitUtility.waitUntilSpinnerHidden();
    }

    public boolean availablePlatforms() {
        waitUtility.waitForLocatorVisible(AVAILABLE_IN_CONTAINER);
        AVAILABLE_IN_CONTAINER.scrollIntoViewIfNeeded();
        List<String> allTexts = AVAILABLE_IN_CHECKBOX.allInnerTexts();
        boolean containsLife = allTexts.stream().anyMatch(text -> text.contains("Life"));
        boolean containsHCP = allTexts.stream().anyMatch(text -> text.contains("HCP"));
        return containsLife && containsHCP;
    }

    public void searchList(String listName) {
        waitUtility.waitForLocatorVisible(CREATE_NEW_LIST);
        SEARCH_BOX.first().fill(listName);
        SEARCH_BOX.first().press("Enter");
    }

    public void openSearchedList(String listName) {
        String listNameXpath = String.format("//div[contains(text(),'%s')]", listName);
        waitUtility.waitForElementVisible(listNameXpath);
        page.locator(listNameXpath).first().click();
        waitUtility.waitForElementHidden(".block-ui-spinner");
    }

    public void clickAutoImportedList() {
        AUTO_IMPORTED_LIST.click();
    }

    public List<String> verifyStudioFilterLabel() {
        waitUtility.waitForLocatorVisible(STUDIO_FILTER_LABEL.first());
        return STUDIO_FILTER_LABEL.allInnerTexts();
    }
}