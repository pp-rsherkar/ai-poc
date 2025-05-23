package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.ExcelActions;

public class NPIStaticList {
    private final Page page;
    private final Locator LIST_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator NPI_NUMBER;
    private final Locator AVAILABLE_IN;
    private final Locator SAVE_BUTTON;
    private final Locator LIST_SUCCESS;
    private final Locator LIST_NAME_ERROR;
    private final Locator ADVERTISER_NAME_ERROR;
    private final Locator BACK_TO_NPI_LISTS;
    private final Locator DELETE_LIST_ICON;
    private final Locator DELETE_LIST_BUTTON;
    private final Locator DELETE_SUCCESS;

    public NPIStaticList(Page page) {
        this.page = page;
        this.LIST_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("List Name"));
        this.SEARCH_ADVERTISER = page.locator("//div[text()= 'Select Advertiser']");
        this.SELECT_ADVERTISER = page.locator("//div[contains(@class,'dropdown-items ng-star-inserted')]");
        this.NPI_NUMBER = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("NPI Numbers (one number per"));
        this.AVAILABLE_IN = page.locator(".mat-checkbox-inner-container").first();
        this.SAVE_BUTTON = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        this.LIST_SUCCESS = page.getByText("× NPI list created");
        this.LIST_NAME_ERROR = page.locator("//div[contains(text(),'List Name is required')]");
        this.ADVERTISER_NAME_ERROR = page.locator("//div[contains(text(),'Advertiser is required')]");
        this.BACK_TO_NPI_LISTS = page.locator("//img[@src='assets/images/12-back.svg']");
        this.DELETE_LIST_ICON = page.locator("//app-icon-lable-link[@icon='icons_20-delete.svg']");
        this.DELETE_LIST_BUTTON = page.locator("//span[text()='Delete']");
        this.DELETE_SUCCESS = page.locator("//div[contains(text(),'Deleted Successfully')]");
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

    public String listNameError() {
        return LIST_NAME_ERROR.innerText();
    }

    public String advertiserError() {
        return ADVERTISER_NAME_ERROR.innerText();
    }

    public void uploadStaticListFile(String fileName) {
        Locator fileInput = page.locator("input[type='file']"); // will remove the hardcoding
        ExcelActions.uploadFile(fileInput, fileName);
    }

    public void clickBackToNPILists() {
        BACK_TO_NPI_LISTS.click();
    }

    public String updatedListName() {
        return LIST_NAME.inputValue();
    }

    public void deleteList() {
        DELETE_LIST_ICON.click();
        DELETE_LIST_BUTTON.click();
    }

    public String deleteSuccess() {
        return DELETE_SUCCESS.innerText();
    }
}
