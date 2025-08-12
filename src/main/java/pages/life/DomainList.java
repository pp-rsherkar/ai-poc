package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class DomainList {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator DOMAIN_LIST;
    private final Locator SEARCH_KEYWORD;
    private final Locator SUB_TABS_BUTTON;
    private final Locator DOMAIN_CREATION_PAGE_TITLE;
    private final Locator LIST_NAME;
    private final Locator DOMAIN_NAME;
    private final Locator SAVE_BUTTON;
    private final Locator LIST_ERROR_MESSAGE_ALERT;
    private final Locator DOMAIN_ERROR_MESSAGE_ALERT;
    private final Locator VALIDATION_ERROR;
    private final Locator UPLOAD_SECTION;
    private final Locator SUCCESS_ALERT;
    private final Locator SEARCHED_DOMAIN_ENTRY;
    private final Locator LIST_DELETE_ICON;
    private final Locator REMOVAL_CONFIRMATION_DIALOG;
    private final Locator REMOVE_BUTTON;
    private final Locator DOMAIN_NAME_FROM_REMOVAL_CONFIRMATION_DIALOG;
    private final Locator DOMAIN_COUNT;
    private final Locator DUPLICATE_FILE_DIALOG;
    private final Locator DUPLICATE_FILE_DIALOG_TEXT;
    private final Locator REPLACE_BUTTON;

    public DomainList(Page page) {
        this.page = page;
        this.DOMAIN_LIST = page.locator("//div[contains(text(),'Domain & App Lists')]");
        this.SEARCH_KEYWORD = page.locator("//div[@id='searchKeyowrd']/input");
        this.SUB_TABS_BUTTON = page.locator("//div[contains(@class,'lookupGroups')]/button");
        this.DOMAIN_CREATION_PAGE_TITLE = page.locator("//div[normalize-space(text())='List Name']");
        this.LIST_NAME = page.locator("//input[@placeholder='List Name']");
        this.DOMAIN_NAME = page.locator("//textarea[@placeholder='Domains (one domain per line)']");
        this.SAVE_BUTTON = page.locator("//button[contains(text(),'Save')]");
        this.LIST_ERROR_MESSAGE_ALERT = page.locator("//div[@aria-label='List Name is required']");
        this.DOMAIN_ERROR_MESSAGE_ALERT = page.locator("//div[@aria-label='Domain name is required']");
        this.VALIDATION_ERROR = page.locator("//span[contains(text(),'validation error(s)')]");
        this.UPLOAD_SECTION = page.locator("//div[contains(@class,'fileUploadSection')]");
        this.SUCCESS_ALERT = page.locator("//div[text()='Domain list created successfully' or text()='Domains list created successfully' or text()='Domains list updated successfully' or text()='Domain deleted successfully']");
        this.SEARCHED_DOMAIN_ENTRY = page.locator("//span[@class='lookupCircel']//following-sibling::div[contains(@class, 'main-details')]");
        this.LIST_DELETE_ICON = page.locator("//div[@tooltip='Delete']//img[contains(@src,'delete.svg')]");
        this.REMOVAL_CONFIRMATION_DIALOG = page.locator("//div[contains(text(),'Removal Confirmation')]");
        this.REMOVE_BUTTON = page.locator("//span[contains(text(),'Remove')]");
        this.DOMAIN_NAME_FROM_REMOVAL_CONFIRMATION_DIALOG = page.locator("//div[contains(@class,'confirm-modal')]//span");
        this.DOMAIN_COUNT = page.locator("//div[contains(@class, 'right circular label')]");
        this.DUPLICATE_FILE_DIALOG = page.locator(" //div[contains(text(),'Duplicating File Names')]");
        this.DUPLICATE_FILE_DIALOG_TEXT = page.locator(" //div[contains(text(),'Duplicating File Names')]/following-sibling::div[contains(@class,'confirm-modal')]/div");
        this.REPLACE_BUTTON = page.locator("//span[contains(text(),'Replace')]");
    }

    public void clickDomainListFromMenu() {
        DOMAIN_LIST.click();
        waitUtility.waitUntilLoaderHidden();
    }

    public String verifyIfListPageIsOpen(String listName) {
        String text = " ";
        Locator locator = page.locator(String.format("//a[contains(text(),'%s')]", listName));
        if(locator.getAttribute("class").contains("active"))
            text = fetchLocatorText(locator);
        return text;
    }

    public boolean verifyIfSearchBoxIsPresent() {
        return SEARCH_KEYWORD.isVisible();
    }

    public boolean verifySubTabs(List<String> subTabsList) {
        boolean flag1 = false, flag2 = false;
        for(String subtabs : subTabsList){
            for (int i=0; i< SUB_TABS_BUTTON.count(); i++){
                if(SUB_TABS_BUTTON.nth(i).innerText().contains(subtabs)) {
                    flag1 = true;
                    if(SUB_TABS_BUTTON.nth(i).getAttribute("class").contains("active"))
                        flag2 = true;
                    break;
                }
            }
        }
        return flag1 && flag2;
    }

    public void clickSubTab(String tabName){
        SUB_TABS_BUTTON.locator("text=" + tabName).click();
        waitUtility.waitUntilLoaderHidden();
    }

    public boolean verifyListIsAvailable(String tabName) {
        int failed = 0;
        Locator locator = page.locator(String.format("//span/img[contains(translate(@src, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]",tabName.toLowerCase(Locale.ROOT)));
        List<String> srcList = (List<String>) locator.evaluateAll("elements => elements.map(el => el.getAttribute('src'))");
        for (String src : srcList) {
            if (src == null || !src.toLowerCase(Locale.ROOT).contains(tabName.toLowerCase(Locale.ROOT))) {
                failed++;
            }
        }
        return failed == 0;
    }

    public boolean verifyDomainListPage() {
        return DOMAIN_CREATION_PAGE_TITLE.isVisible();
    }

    public String validateErrorOnEmptyListNameInput(String listName) {
        LIST_NAME.fill(listName);
        LIST_NAME.clear();
        SAVE_BUTTON.click();
        return fetchLocatorText(LIST_ERROR_MESSAGE_ALERT);

    }

    public String validateErrorOnEmptyDomainListInput(String listName) {
        LIST_NAME.fill(listName);
        SAVE_BUTTON.click();
        return fetchLocatorText(DOMAIN_ERROR_MESSAGE_ALERT);
    }

    public String checkErrorOnSingleLineMultipleDomainsInput(List<String> domainNameList) {
        String text = "";
        DOMAIN_NAME.fill(String.valueOf(domainNameList));
        SAVE_BUTTON.click();
        if(VALIDATION_ERROR.isVisible())
            text = fetchLocatorText(VALIDATION_ERROR);
        DOMAIN_NAME.clear();
        return text;
    }


    public boolean verifyUploadSectionIsVisibleBeforeDomainInput() {
        return UPLOAD_SECTION.isVisible();
    }

    public void enterDomainNames(List<Object> domainNameList) {
        for (Object domainName : domainNameList) {
            if (domainName instanceof String) {
                DOMAIN_NAME.type((String) domainName);
                page.keyboard().press("Shift+Enter");
            }
        }
    }

    public boolean verifyUploadSectionIsVisibleAfterDomainInput() {
        return !UPLOAD_SECTION.isVisible();
    }

    public void saveDomainList() {
        SAVE_BUTTON.click();
    }

    public String verifyDomainListCreationOrDeletion() {
        String text = fetchLocatorText(SUCCESS_ALERT);
        waitUtility.waitUntilLoaderHidden();
        return text;
    }

    public String fetchDomainListCountFromLeftPanel() {
        return DOMAIN_COUNT.innerText();
    }

    public void searchAndOpenDomainList(String metricName) {
        SEARCH_KEYWORD.fill(metricName);
        SEARCHED_DOMAIN_ENTRY.isVisible();
        SEARCHED_DOMAIN_ENTRY.click();
        waitUtility.waitUntilLoaderHidden();
    }

    public void editAnExistingDomainList(List<Object> domainList) {
        DOMAIN_NAME.click();
        page.keyboard().press("Shift+Enter");
        enterDomainNames(domainList);
    }

    public void deleteDomainList(){
        LIST_DELETE_ICON.click();
    }

    public String fetchRemovalConfirmation() {
        waitUtility.waitForLocatorVisible(REMOVAL_CONFIRMATION_DIALOG);
        String text = fetchLocatorText(DOMAIN_NAME_FROM_REMOVAL_CONFIRMATION_DIALOG);
        REMOVE_BUTTON.click();
        return text;
    }

    public String fetchLocatorText(Locator locator){
        return locator.innerText().trim();
    }

    public void uploadDomainFile(String fileName){
        CommonUtils.uploadFileThroughSystemDialog(page, fileName);
    }

    public String fetchListErrorMessage() {
        return fetchLocatorText(LIST_ERROR_MESSAGE_ALERT);
    }

    public void enterListName(String listName) {
        LIST_NAME.fill(listName);
    }

    public boolean verifyTextAreaIsVisibleBeforeFileUpload() {
        return DOMAIN_NAME.isVisible();
    }

    public boolean verifyTextAreaIsVisibleAfterFileUpload() {
        return !DOMAIN_NAME.isVisible();
    }

    public String fetchFileNameFromUploadedFilesSection(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']", fileName));
        return fetchLocatorText(locator);
    }

    public int fetchDomainCountFromUploadedFilesSection(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']/parent::div/following-sibling::div/div[contains(@class,'fileDetails')]/div[1]", fileName));
        locator.scrollIntoViewIfNeeded();
        String text = locator.innerText().trim();
        String numberOnly = text.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly);
    }

    public boolean isDownloadIconVisible(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']/following-sibling::div//img[contains(@src,'export.svg')]", fileName));
        return locator.isVisible();
    }

    public boolean isDeleteIconVisible(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']/following-sibling::div//img[contains(@src,'delete.svg')]", fileName));
        return locator.isVisible();
    }

    public boolean verifyIfDuplicateFileDialogIsDisplayed(String fileName) {
        DUPLICATE_FILE_DIALOG.isVisible();
        String text = fetchLocatorText(DUPLICATE_FILE_DIALOG_TEXT);
        return text.contains(fileName);
    }

    public void clickReplaceButton(){
        REPLACE_BUTTON.isVisible();
        REPLACE_BUTTON.click();
        waitUtility.waitUntilLoaderHidden();
    }

    public void downloadFile(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']/following-sibling::div//img[contains(@src,'export.svg')]", fileName));
        locator.click();
    }

    public boolean verifyDownloadedFile(String fileName) {
        File downloadDir = new File(System.getProperty("user.home") + "/Downloads");
        File latest = Arrays.stream(Objects.requireNonNull(downloadDir.listFiles((d, name) -> name.matches(fileName + "( \\(\\d+\\))?\\.csv"))))
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);

        return latest != null;
    }

    public void deleteFile(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']/following-sibling::div//img[contains(@src,'delete.svg')]", fileName));
        locator.click();
    }
}
