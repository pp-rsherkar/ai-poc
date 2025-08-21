package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

public class NPIAttributesList {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator FILE_INPUT;
    private final Locator FILE_UPLOAD_SUCCESS;
    private final Locator LIST_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator NPI_COLUMN_DROPDOWN;
    private final Locator NEXT_BUTTON;
    private final Locator LIST_NAME_ERROR;
    private final Locator ADVERTISER_NAME_ERROR;
    private final Locator AVAILABLE_IN_LIFE;
    private final Locator AVAILABLE_IN_HCP365;
    private final Locator LIST_SUCCESS;
    private final Locator BACK_TO_NPI_LISTS;
    private final Locator SAVE_BUTTON;
    private final Locator LIST_UPDATE_SUCCESS;
    private final Locator DELETE_LIST_ICON;
    private final Locator DELETE_LIST_BUTTON;
    private final Locator DELETE_SUCCESS;
    private final Locator TOTAL_NPI_LIST_COUNT;

    public NPIAttributesList(Page page) {
        this.page = page;
        this.FILE_INPUT = page.locator("input[type='file']");
        this.FILE_UPLOAD_SUCCESS = page.locator("//div[contains(@aria-label,'Successfully uploaded')]");
        this.LIST_NAME = page.locator("//input[contains(@placeholder,'List Name')]");
        this.SEARCH_ADVERTISER = page.locator("//div[contains(text(),'Select Advertiser')]");
        this.SELECT_ADVERTISER = page.locator("//div[contains(@class,'dropdown-items ng-star-inserted')]");
        this.NPI_COLUMN_DROPDOWN = page.locator("//ng-select[contains(@class,'hcpDropdown') and contains(@placeholder,'NPI Column')]");
        this.NEXT_BUTTON = page.locator("//button[contains(@class,'saveButton')]");
        this.LIST_NAME_ERROR = page.locator("//div[contains(@aria-label,'List Name is required')]");
        this.ADVERTISER_NAME_ERROR = page.locator("//div[contains(@aria-label,'Advertiser is required')]");
        this.AVAILABLE_IN_LIFE = page.locator("(//span[@class='mat-checkbox-label' and normalize-space(text())='Life'])[1]");
        this.AVAILABLE_IN_HCP365 = page.locator("(//span[@class='mat-checkbox-label' and normalize-space(text())='HCP365'])[1]");
        this.LIST_SUCCESS = page.locator("//div[contains(@aria-label,'NPI list created')]");
        this.BACK_TO_NPI_LISTS = page.locator("//app-icon-lable-link[@icon='12-back.svg']");
        this.SAVE_BUTTON = page.locator("//span[text()='Save']");
        this.LIST_UPDATE_SUCCESS = page.locator("//div[contains(@aria-label,'NPI list updated')]");
        this.DELETE_LIST_ICON = page.locator("//app-icon-lable-link[@icon='icons_20-delete.svg']");
        this.DELETE_LIST_BUTTON = page.locator("//span[text()='Delete']");
        this.DELETE_SUCCESS = page.locator("//div[contains(text(),'Deleted Successfully')]");
        this.TOTAL_NPI_LIST_COUNT = page.locator("//div[@class='label' and text()='Total NPI']/preceding-sibling::div");
    }

    public void uploadAttributesFile(String attributesFile) {
        CommonUtils.uploadFile(FILE_INPUT, attributesFile);
        waitUtility.waitUntilSpinnerHidden();
    }

    public String verifyFileUploadSuccess() {
        return FILE_UPLOAD_SUCCESS.innerText();
    }

    public void enterListName(String listName) {
        LIST_NAME.scrollIntoViewIfNeeded();
        LIST_NAME.fill(listName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void selectNPIColumn(String columnName) {
        waitUtility.waitForLocatorDetached(FILE_UPLOAD_SUCCESS);
        NPI_COLUMN_DROPDOWN.click();
        Locator columnOption = page.locator("//div[contains(@class,'ng-option') and normalize-space()='" + columnName + "']");
        waitUtility.waitForLocatorVisible(columnOption);
        columnOption.click();
    }

    public void clickNextButton() {
        NEXT_BUTTON.click();
    }

    public String listNameError() {
        return LIST_NAME_ERROR.innerText();
    }

    public String advertiserError() {
        return ADVERTISER_NAME_ERROR.innerText();
    }

    public void selectProduct() {
        AVAILABLE_IN_LIFE.check();
        AVAILABLE_IN_HCP365.check();
    }

    public String saveListSuccess() {
        String text = LIST_SUCCESS.innerText();
        waitUtility.waitForLocatorHidden(LIST_SUCCESS);
        return text;
    }

    public void clickBackToNPILists() {
        waitUtility.waitForLocatorDetached(LIST_SUCCESS);
        BACK_TO_NPI_LISTS.click();
        page.waitForSelector(".block-ui-spinner", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void editListName(String newListName) {
        page.waitForTimeout(5000);
        waitUtility.waitForLocatorVisible(BACK_TO_NPI_LISTS);
        LIST_NAME.fill(newListName);
    }

    public void saveList() {
        SAVE_BUTTON.click();
    }

    public String updateListSuccess() {
        return LIST_UPDATE_SUCCESS.innerText();
    }

    public void deleteList() {
        waitUtility.waitForLocatorDetached(LIST_SUCCESS);
        DELETE_LIST_ICON.click();
        DELETE_LIST_BUTTON.click();
    }

    public String deleteSuccess() {
        return DELETE_SUCCESS.innerText();
    }

    public int fetchTotalNPIListCount(String listName) {
        int npiCount = 0;
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorVisible(TOTAL_NPI_LIST_COUNT);
        if(listName.contains(LIST_NAME.innerText()))
            npiCount = Integer.parseInt(TOTAL_NPI_LIST_COUNT.innerText().trim());
        return npiCount;
    }
}
