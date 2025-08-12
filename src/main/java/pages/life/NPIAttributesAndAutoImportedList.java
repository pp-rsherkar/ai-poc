package pages.life;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.opencsv.exceptions.CsvValidationException;
import factory.DriverFactory;
import utils.ApiActions;
import utils.CommonUtils;
import utils.ExcelActions;
import utils.WaitUtility;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

public class NPIAttributesAndAutoImportedList {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    ApiActions apiActions = new ApiActions();
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
    private final Locator SETUP_IMPORTBUTTON;
    private final Locator ERROR_MESSAGE;
    private final Locator IMPORT_SETTING_TITLE;
    private final Locator DESTINATION_DROPDOWN;
    private final Locator DESTINATION_DROPDOWN_VALUE;
    private final Locator FILE_PATH;
    private final Locator FILE_NAME;
    private final Locator NPI_COLUMN;
    private final Locator CHECK_FILE_BUTTON;
    private final Locator OK_BUTTON;
    private final Locator TOTAL_NPI_COUNT;
    private final Locator RELOAD_NOW_BUTTON;
    private final Locator RELOAD_SUCCESS_ALERT;

    public NPIAttributesAndAutoImportedList(Page page) {
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
        this.SETUP_IMPORTBUTTON = page.locator("//span[contains(text(),'Setup Import')]");
        this.ERROR_MESSAGE = page.locator("//div[@aria-label='Advertiser is required']");
        this.IMPORT_SETTING_TITLE = page.locator("//div[contains(text(),'Import Settings')]");
        this.DESTINATION_DROPDOWN = page.locator("//ng-select[@placeholder='Select Destination']");
        this.DESTINATION_DROPDOWN_VALUE = page.locator("//ng-dropdown-panel//div//span");
        this.FILE_PATH = page.locator("//input[@placeholder='File Path']");
        this.FILE_NAME = page.locator("//input[@placeholder='File Name']");
        this.NPI_COLUMN = page.locator("//input[@formcontrolname='npiColumn']");
        this.CHECK_FILE_BUTTON = page.locator("//span[contains(text(),'Check File')]");
        this.OK_BUTTON = page.locator("//button[contains(@class,'saveButton')]");
        this.TOTAL_NPI_COUNT = page.locator("//div[contains(@class,'npiAttrsMatchedNPI')]//div[text()='Total NPI']//preceding-sibling::div");
        this.RELOAD_NOW_BUTTON = page.locator("//span[contains(text(),'Reload Now')]/parent::div");
        this.RELOAD_SUCCESS_ALERT = page.locator("//div[@aria-label='File is reloaded']");

    }

    public void uploadAttributesFile(String attributesFile) {
        CommonUtils.uploadFile(FILE_INPUT, attributesFile);
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
        FILE_UPLOAD_SUCCESS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        NPI_COLUMN_DROPDOWN.click();
        Locator columnOption = page.locator("//div[contains(@class,'ng-option') and normalize-space()='" + columnName + "']");
        columnOption.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
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
        return LIST_SUCCESS.innerText();
    }

    public void clickBackToNPILists() {
        LIST_SUCCESS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        BACK_TO_NPI_LISTS.click();
        page.waitForSelector(".block-ui-spinner", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void editListName(String newListName) {
        page.waitForTimeout(5000);
        BACK_TO_NPI_LISTS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        LIST_NAME.fill(newListName);
    }

    public void saveList() {
        SAVE_BUTTON.click();
    }

    public String updateListSuccess() {
        return LIST_UPDATE_SUCCESS.innerText();
    }

    public void deleteList() {
        LIST_SUCCESS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        DELETE_LIST_ICON.click();
        DELETE_LIST_BUTTON.click();
    }

    public String deleteSuccess() {
        return DELETE_SUCCESS.innerText();
    }

    public String verifyIfAutoImportPage(){
        SETUP_IMPORTBUTTON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return SETUP_IMPORTBUTTON.innerText().trim();
    }

    public String verifyErrorMessage(){
        String errorMessage = ERROR_MESSAGE.innerText().trim();
        waitUtility.waitUntilLoaderHidden();
        return errorMessage;
    }
    public void clickSetupImportButton(){
        SETUP_IMPORTBUTTON.scrollIntoViewIfNeeded();
        SETUP_IMPORTBUTTON.click();
    }

    public void waitForImportSettingPanel(){
        waitUtility.waitUntilLoaderHidden();
        waitUtility.waitForLocatorVisible(IMPORT_SETTING_TITLE);
    }

    public void enterFileDetails(String fileLocation, String filePath, String fileName) {
        DESTINATION_DROPDOWN.click();
        CommonUtils.selectAndClickElement(DESTINATION_DROPDOWN_VALUE, Collections.singletonList(fileLocation));
        FILE_PATH.fill(filePath);
        FILE_NAME.fill(fileName);
    }

    public void selectListType(String listType) {
        page.locator(String.format("//div[contains(text(),'%s')]/ancestor::mat-radio-button",listType)).click();
    }

    public void enterColumnName(String npiColumn, String columnName) {
        Locator locator = page.locator(String.format("//div[@class='selection' and contains(text(),'%s')]/parent::div", npiColumn));
        if(!locator.getAttribute("class").contains("active"))
            locator.click();
        NPI_COLUMN.fill(columnName);
    }

    public void selectImportType(String importType) {
        Locator locator = page.locator(String.format("//div[contains(text(),'%s')]/ancestor::mat-radio-button", importType));
        if(!locator.getAttribute("class").contains("mat-radio-checked"))
            locator.click();
    }

    public void clickCheckFile() {
        CHECK_FILE_BUTTON.click();
    }

    public void clickOKButton() {
        OK_BUTTON.click();
    }

    public APIResponse runAPI(String baseURL, String endpointPath, HashMap<String, String> headers) {
        APIResponse response;
        response = apiActions.postRequestWithoutBody(baseURL, endpointPath, headers);
        return response;
    }

    public String fetchTotalNPICount() {
        return TOTAL_NPI_COUNT.innerText();
    }

    public String fetchNPIRecordFromTestFile(String fileName) throws CsvValidationException, IOException {
        String filePath = "src/main/resources/uploadfiles/" + fileName;
        int count = ExcelActions.countCsvRecords(filePath);
        return String.valueOf(count);
    }

    public boolean verifyReloadNowButton() {
        return RELOAD_NOW_BUTTON.isVisible();
    }

    public void clickReloadNowButton() {
        RELOAD_NOW_BUTTON.click();
        waitUtility.waitUntilLoaderHidden();
    }

    public String verifyIfFileIsReloaded() {
        return RELOAD_SUCCESS_ALERT.innerText().trim();
    }
}
