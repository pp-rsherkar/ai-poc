package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.List;
import java.util.Locale;

public class SharedList {
    private final Page page;
    private final Locator SEARCH_KEYWORD;
    private final Locator SUB_TABS_BUTTON;
    private final Locator LIST_CREATION_PAGE_TITLE;
    private final Locator LIST_NAME;
    private final Locator LIST_TEXTAREA;
    private final Locator SAVE_BUTTON;
    private final Locator LIST_ERROR_MESSAGE_ALERT;
    private final Locator ERROR_MESSAGE_ALERT;
    private final Locator VALIDATION_ERROR;
    private final Locator UPLOAD_SECTION;
    private final Locator SUCCESS_ALERT;
    private final Locator LIST_DELETE_ICON;
    private final Locator REMOVAL_CONFIRMATION_DIALOG;
    private final Locator REMOVE_BUTTON;
    private final Locator DOMAIN_NAME_FROM_REMOVAL_CONFIRMATION_DIALOG;
    private final Locator DUPLICATE_FILE_DIALOG;
    private final Locator DUPLICATE_FILE_DIALOG_TEXT;
    private final Locator REPLACE_BUTTON;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public SharedList(Page page) {
        this.page = page;
        this.SEARCH_KEYWORD = page.locator("//div[@id='searchKeyowrd']/input");
        this.SUB_TABS_BUTTON = page.locator("//div[contains(@class,'lookupGroups')]/button");
        this.LIST_CREATION_PAGE_TITLE = page.locator("//div[normalize-space(text())='List Name']");
        this.LIST_NAME = page.locator("//input[@placeholder='List Name']");
        this.LIST_TEXTAREA = page.locator("//textarea[@placeholder='Domains (one domain per line)' or @placeholder='AppBundles (one appbundle per line)' or @placeholder='Keywords (one keyword per line)' or @placeholder='IPAddresses (one ipAddress per line)']");
        this.SAVE_BUTTON = page.locator("//button[contains(text(),'Save')]");
        this.LIST_ERROR_MESSAGE_ALERT = page.locator("//div[@aria-label='List Name is required']");
        this.ERROR_MESSAGE_ALERT = page.locator("//div[@aria-label='Domain name is required' or @aria-label='AppBundle name is required' or @aria-label='Keyword is required' or @aria-label='IPAddress is required']");
        this.VALIDATION_ERROR = page.locator("//span[contains(text(),'validation error(s)')]");
        this.UPLOAD_SECTION = page.locator("//div[contains(@class,'fileUploadSection')] | //app-drop-file[contains(@class,'setup-drag')]");
        this.SUCCESS_ALERT = page.locator("//div[text()='Domain list created successfully' " + "or text()='Domains list created successfully' " + "or text()='Domains list updated successfully' " + "or text()='Domain deleted successfully' " + "or text()='AppBundle list created successfully' " + "or text()='AppBundle list updated successfully' " + "or text()='AppBundleGroup deleted successfully' " + "or text()='Keywords list created successfully' " + "or text()='Keywords list updated successfully' " + "or text()='Keyword deleted successfully' " + "or text()='IPAddress list created successfully' " + "or text()='IPAddresses list created successfully' " + "or text()='IPAddresses list updated successfully' " + "or text()='IPAddress deleted successfully']");
        this.LIST_DELETE_ICON = page.locator("//div[@tooltip='Delete'] | //span[@tooltip='Delete']//img[contains(@src,'delete.svg')]");
        this.REMOVAL_CONFIRMATION_DIALOG = page.locator("//div[contains(text(),'Removal Confirmation')]");
        this.REMOVE_BUTTON = page.locator("//span[contains(text(),'Remove')]");
        this.DOMAIN_NAME_FROM_REMOVAL_CONFIRMATION_DIALOG = page.locator("//div[contains(@class,'confirm-modal')]//span");
        this.DUPLICATE_FILE_DIALOG = page.locator(" //div[contains(text(),'Duplicating File Names')]");
        this.DUPLICATE_FILE_DIALOG_TEXT = page.locator(" //div[contains(text(),'Duplicating File Names')]/following-sibling::div[contains(@class,'confirm-modal')]/div");
        this.REPLACE_BUTTON = page.locator("//span[contains(text(),'Replace')]");
    }

    public void clickDomainListFromMenu(String pageName) {
        Locator locator = page.locator(String.format("//div[contains(@class,'menuLabel') and contains(text(),'%s')]", pageName));
        locator.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String verifyIfListPageIsOpen(String listName) {
        String text = " ";
        Locator locator = page.locator(String.format("//a[contains(text(),'%s')]", listName));
        if (locator.getAttribute("class").contains("active")) text = fetchLocatorText(locator);
        return text;
    }

    public boolean verifyIfSearchBoxIsPresent() {
        return SEARCH_KEYWORD.isVisible();
    }

    public boolean verifySubTabs(List<String> subTabsList) {
        boolean flag1 = false, flag2 = false;
        for (String subtabs : subTabsList) {
            for (int i = 0; i < SUB_TABS_BUTTON.count(); i++) {
                if (SUB_TABS_BUTTON.nth(i).innerText().contains(subtabs)) {
                    flag1 = true;
                    if (SUB_TABS_BUTTON.nth(i).getAttribute("class").contains("active")) flag2 = true;
                    break;
                }
            }
        }
        return flag1 && flag2;
    }

    public void clickSubTab(String tabName) {
        SUB_TABS_BUTTON.locator("text=" + tabName).click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public boolean verifyListIsAvailable(String tabName) {
        int failed = 0;
        Locator locator = page.locator(String.format("//span/img[contains(translate(@src, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]", tabName.toLowerCase(Locale.ROOT)));
        List<String> srcList = (List<String>) locator.evaluateAll("elements => elements.map(el => el.getAttribute('src'))");
        for (String src : srcList) {
            if (src == null || !src.toLowerCase(Locale.ROOT).contains(tabName.toLowerCase(Locale.ROOT))) {
                failed++;
            }
        }
        return failed == 0;
    }

    public boolean verifyNewListPage() {
        return LIST_CREATION_PAGE_TITLE.isVisible();
    }

    public String validateErrorOnEmptyListNameInput(String listName) {
        LIST_NAME.fill(listName);
        LIST_NAME.clear();
        SAVE_BUTTON.click();
        return fetchLocatorText(LIST_ERROR_MESSAGE_ALERT);
    }

    public String validateErrorOnEmptyListInput(String listName) {
        LIST_NAME.fill(listName);
        SAVE_BUTTON.click();
        return fetchLocatorText(ERROR_MESSAGE_ALERT);
    }

    public String checkErrorOnSingleLineMultipleDomainsInput(List<String> domainNameList) {
        String text = "";
        for (String domainName : domainNameList) {
            LIST_TEXTAREA.type(domainName);
            page.keyboard().press("Space");
        }
        SAVE_BUTTON.click();
        if (VALIDATION_ERROR.isVisible()) text = fetchLocatorText(VALIDATION_ERROR);
        LIST_TEXTAREA.clear();
        return text;
    }

    public boolean verifyUploadSectionIsVisibleBeforeListInput() {
        return UPLOAD_SECTION.first().isVisible();
    }

    public void enterDomainNames(List<Object> domainNameList) {
        for (Object domainName : domainNameList) {
            if (domainName instanceof String) {
                LIST_TEXTAREA.type((String) domainName);
                page.keyboard().press("Shift+Enter");
            }
        }
    }

    public boolean verifyUploadSectionIsVisibleAfterListInput() {
        return !UPLOAD_SECTION.isVisible();
    }

    public void saveList() {
        SAVE_BUTTON.click();
    }

    public String isListCreatedOrDeleted() {
        String text = fetchLocatorText(SUCCESS_ALERT);
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
        waitUtility.waitUntilSpinnerHidden();
        return text;
    }

    public String fetchCountFromLeftPanel(String listName) {
        Locator locator = page.locator(String.format(
                "(//div[contains(text(), '%s')]/parent::div/following-sibling::div//div | " +
                        "//div[contains(text(), '%s')]/following-sibling::div//div | " +
                        "//div[contains(text(), '%s')]/following-sibling::div[@class='list-item-counter'])",
                listName, listName, listName));
        return locator.innerText();
    }

    public void searchAndOpenCreatedList(String listName) {
        SEARCH_KEYWORD.fill(listName);
        Locator SEARCHED_DOMAIN_ENTRY = page.locator(String.format("//div[contains(text(),'%s')]", listName));
        SEARCHED_DOMAIN_ENTRY.isVisible();
        SEARCHED_DOMAIN_ENTRY.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void editAnExistingList(List<Object> domainList) {
        LIST_TEXTAREA.click();
        page.keyboard().press("Shift+Enter");
        enterDomainNames(domainList);
    }

    public void deleteList() {
        LIST_DELETE_ICON.click();
    }

    public String fetchRemovalConfirmation() {
        waitUtility.waitForLocatorVisible(REMOVAL_CONFIRMATION_DIALOG);
        String text = fetchLocatorText(DOMAIN_NAME_FROM_REMOVAL_CONFIRMATION_DIALOG);
        REMOVE_BUTTON.click();
        return text;
    }

    public String fetchLocatorText(Locator locator) {
        return locator.innerText().trim();
    }

    public void uploadDomainFile(String fileName) {
        CommonUtils.uploadFileThroughSystemDialog(page, fileName);
        waitUtility.waitUntilSpinnerHidden();
    }

    public String fetchListErrorMessage() {
        return fetchLocatorText(LIST_ERROR_MESSAGE_ALERT);
    }

    public void enterListName(String listName) {
        LIST_NAME.fill(listName);
    }

    public boolean verifyTextAreaIsVisibleBeforeFileUpload() {
        return LIST_TEXTAREA.isVisible();
    }

    public boolean verifyTextAreaIsVisibleAfterFileUpload() {
        return !LIST_TEXTAREA.isVisible();
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

    public void clickReplaceButton() {
        REPLACE_BUTTON.isVisible();
        REPLACE_BUTTON.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void downloadFile(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']/following-sibling::div//img[contains(@src,'export.svg')]", fileName));
        locator.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public boolean verifyDownloadedFile(String fileName, String fileExtension) {
        return CommonUtils.isDownloadedFileAvailable(fileName, fileExtension);
    }

    public void deleteFile(String fileName) {
        Locator locator = page.locator(String.format("//div[@title='%s']/following-sibling::div//img[contains(@src,'delete.svg')]", fileName));
        locator.click();
    }

    public boolean fetchPulsepointIcon(String listName) {
        Locator locator = page.locator(String.format("//div[contains(text(),'%s')]/ancestor::div/following-sibling::div/div[contains(@class,'sharedList-icon')] | " + "//div[contains(text(),'%s')]/following-sibling::div/div[contains(@class,'sharedList-icon')]", listName, listName));
        return locator.isVisible();
    }

    public void clickListTypeRadioButton(String listType) {
        Locator locator = page.locator(String.format("//label[contains(text(),'%s')]/parent::sui-radio-button", listType));
        locator.click();
    }
}