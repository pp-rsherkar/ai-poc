package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.WaitUtility;

import java.util.ArrayList;
import java.util.List;

public class ListSortingPage {

    private final Page page;
    WaitUtility waitUtility;

    // Column header locators
    private final Locator CREATED_ON_HEADER;
    private final Locator LAST_UPDATED_HEADER;

    // Table rows - covers Angular Material mat-table and div-based list layouts
    private final Locator TABLE_ROWS;

    // Pagination
    private final Locator PAGINATION_NEXT;
    private final Locator PAGINATION_PAGE_INFO;

    // Empty state
    private final Locator EMPTY_STATE;

    // Edit and save buttons
    private final Locator EDIT_BUTTON_FIRST;
    private final Locator SAVE_BUTTON;

    // Name input for record modification
    private final Locator NAME_INPUT;

    // Audit log navigation and entries
    private final Locator AUDIT_LOG_NAV;
    private final Locator AUDIT_LOG_ENTRIES;

    // Error/permission message
    private final Locator PERMISSION_ERROR_MESSAGE;

    public ListSortingPage(Page page) {
        this.page = page;
        this.waitUtility = new WaitUtility(page);

        this.CREATED_ON_HEADER = page.locator(
                "//th[normalize-space(.)='Created On' or .//span[normalize-space(text())='Created On'] " +
                "or normalize-space(.)='Created Date' or .//div[normalize-space(text())='Created On']]"
        );

        this.LAST_UPDATED_HEADER = page.locator(
                "//th[normalize-space(.)='Last Updated' or .//span[normalize-space(text())='Last Updated'] " +
                "or normalize-space(.)='Updated On' or .//div[normalize-space(text())='Last Updated']]"
        );

        this.TABLE_ROWS = page.locator(
                "//tr[contains(@class,'mat-row') or contains(@class,'mat-mdc-row')] | " +
                "//tr[@role='row' and not(contains(@class,'header'))]"
        );

        this.PAGINATION_NEXT = page.locator(
                "//button[@aria-label='Next page'] | //button[contains(@aria-label,'next page')]"
        );

        this.PAGINATION_PAGE_INFO = page.locator(
                "//*[contains(@class,'mat-paginator-range-label') or contains(@class,'paginator-range')]"
        );

        this.EMPTY_STATE = page.locator(
                "//*[contains(text(),'No data') or contains(text(),'No lists found') " +
                "or contains(text(),'No results found') or contains(text(),'Nothing found') " +
                "or contains(text(),'No records')]"
        );

        this.EDIT_BUTTON_FIRST = page.locator("//img[@alt='edit' and contains(@src,'edit')]").first();

        this.SAVE_BUTTON = page.locator(
                "//button[normalize-space(text())='Save'] | //button[contains(@class,'save') and contains(text(),'Save')]"
        ).first();

        this.NAME_INPUT = page.locator(
                "//input[@placeholder='List Name'] | //input[contains(@placeholder,'Name') and not(contains(@placeholder,'Search'))]"
        ).first();

        this.AUDIT_LOG_NAV = page.locator(
                "//div[contains(text(),'Audit Log')] | //a[contains(text(),'Audit Log')] | " +
                "//button[contains(text(),'Audit Log')]"
        );

        this.AUDIT_LOG_ENTRIES = page.locator(
                "//tr[contains(@class,'mat-row') or contains(@class,'audit')]"
        );

        this.PERMISSION_ERROR_MESSAGE = page.locator(
                "//div[@role='alert'] | //div[contains(@class,'error-message')] | " +
                "//span[contains(@class,'error')] | //div[contains(text(),'permission') or contains(text(),'not authorized')]"
        );
    }

    public boolean isLastUpdatedColumnVisible() {
        waitUtility.waitUntilSpinnerHidden();
        return LAST_UPDATED_HEADER.isVisible();
    }

    public boolean isCreatedOnColumnVisible() {
        waitUtility.waitUntilSpinnerHidden();
        return CREATED_ON_HEADER.isVisible();
    }

    public void clickLastUpdatedColumnHeader() {
        waitUtility.waitForLocatorVisible(LAST_UPDATED_HEADER);
        LAST_UPDATED_HEADER.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String getLastUpdatedSortDirection() {
        String ariaSort = LAST_UPDATED_HEADER.getAttribute("aria-sort");
        if (ariaSort != null && !ariaSort.isEmpty()) return ariaSort;
        // Fallback: check for sort icon class inside header
        Locator sortIconAsc = page.locator(
                "//th[.//span[normalize-space(text())='Last Updated']]//mat-sort-header-arrow[@dir='asc'] | " +
                "//th[normalize-space(.)='Last Updated']//*[contains(@class,'sort-asc')]"
        );
        if (sortIconAsc.isVisible()) return "ascending";
        Locator sortIconDesc = page.locator(
                "//th[.//span[normalize-space(text())='Last Updated']]//mat-sort-header-arrow[@dir='desc'] | " +
                "//th[normalize-space(.)='Last Updated']//*[contains(@class,'sort-desc')]"
        );
        if (sortIconDesc.isVisible()) return "descending";
        return "none";
    }

    public boolean isCreatedOnDefaultSort() {
        String ariaSort = CREATED_ON_HEADER.getAttribute("aria-sort");
        if ("ascending".equals(ariaSort)) return true;
        // Check for active sort indicator on the column
        Locator activeCreatedOnSort = page.locator(
                "//th[@aria-sort='ascending' and (.//span[contains(text(),'Created')] or contains(normalize-space(.),'Created On'))]"
        );
        return activeCreatedOnSort.isVisible();
    }

    public boolean isActiveSortColumnVisible() {
        Locator activeSortHeader = page.locator("//th[@aria-sort and @aria-sort!='none']");
        return activeSortHeader.count() > 0 && activeSortHeader.first().isVisible();
    }

    public String getActiveSortColumnName() {
        Locator activeSortHeader = page.locator("//th[@aria-sort and @aria-sort!='none']");
        if (activeSortHeader.count() > 0) {
            return activeSortHeader.first().innerText().trim();
        }
        return "";
    }

    public int getTableRowCount() {
        waitUtility.waitUntilSpinnerHidden();
        return TABLE_ROWS.count();
    }

    public String getRowTextAtIndex(int index) {
        waitUtility.waitUntilSpinnerHidden();
        if (TABLE_ROWS.count() <= index) return "";
        return TABLE_ROWS.nth(index).innerText().trim();
    }

    public List<String> getAllRowTexts() {
        waitUtility.waitUntilSpinnerHidden();
        List<String> rows = new ArrayList<>();
        int count = TABLE_ROWS.count();
        for (int i = 0; i < count; i++) {
            rows.add(TABLE_ROWS.nth(i).innerText().trim());
        }
        return rows;
    }

    public String getCreatedOnValueAtRow(int rowIndex) {
        // Try column-specific locator first
        Locator createdOnCells = page.locator(
                "//td[contains(@class,'cdk-column-createdOn') or contains(@class,'cdk-column-created') " +
                "or contains(@class,'created-on') or @data-column='createdOn']"
        );
        if (createdOnCells.count() > rowIndex) {
            return createdOnCells.nth(rowIndex).innerText().trim();
        }
        return "";
    }

    public boolean isSortedByCreatedOnAscending() {
        waitUtility.waitUntilSpinnerHidden();
        // First check aria-sort attribute on the Created On column header
        if (isCreatedOnDefaultSort()) return true;
        // Fallback: check the actual cell values are ascending
        Locator createdOnCells = page.locator(
                "//td[contains(@class,'cdk-column-createdOn') or contains(@class,'cdk-column-created') " +
                "or contains(@class,'created-on')]"
        );
        int count = createdOnCells.count();
        if (count <= 1) return true;
        List<String> values = createdOnCells.allInnerTexts();
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i).compareTo(values.get(i + 1)) > 0) return false;
        }
        return true;
    }

    public void clickColumnHeader(String columnName) {
        Locator header = page.locator(
                String.format("//th[normalize-space(.)='%s' or .//span[normalize-space(text())='%s'] " +
                        "or .//div[normalize-space(text())='%s']]", columnName, columnName, columnName)
        );
        waitUtility.waitForLocatorVisible(header);
        header.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String getColumnSortDirection(String columnName) {
        Locator header = page.locator(
                String.format("//th[normalize-space(.)='%s' or .//span[normalize-space(text())='%s']]", columnName, columnName)
        );
        if (!header.isVisible()) return "none";
        String ariaSort = header.getAttribute("aria-sort");
        return ariaSort != null ? ariaSort : "none";
    }

    public boolean navigateToNextPage() {
        if (PAGINATION_NEXT.isVisible() && !PAGINATION_NEXT.isDisabled()) {
            PAGINATION_NEXT.click();
            waitUtility.waitUntilSpinnerHidden();
            return true;
        }
        return false;
    }

    public String getCurrentPageInfo() {
        if (PAGINATION_PAGE_INFO.isVisible()) {
            return PAGINATION_PAGE_INFO.innerText().trim();
        }
        return "";
    }

    public boolean isOnPage(int pageNumber) {
        String pageInfo = getCurrentPageInfo();
        if (pageInfo.isEmpty()) return pageNumber == 1;
        return pageInfo.contains(String.valueOf(pageNumber));
    }

    public boolean isEmptyStateVisible() {
        waitUtility.waitUntilSpinnerHidden();
        return EMPTY_STATE.isVisible();
    }

    public void clickFirstEditButton() {
        waitUtility.waitUntilSpinnerHidden();
        if (EDIT_BUTTON_FIRST.isVisible()) {
            EDIT_BUTTON_FIRST.click();
            waitUtility.waitUntilSpinnerHidden();
        }
    }

    public void appendTextToNameField(String suffix) {
        if (NAME_INPUT.isVisible()) {
            String current = NAME_INPUT.inputValue();
            NAME_INPUT.fill(current + suffix);
        }
    }

    public void clickSave() {
        if (SAVE_BUTTON.isVisible()) {
            SAVE_BUTTON.click();
            waitUtility.waitUntilSpinnerHidden();
        }
    }

    public boolean isEditButtonVisible() {
        return EDIT_BUTTON_FIRST.isVisible();
    }

    public boolean isPermissionErrorDisplayed() {
        waitUtility.waitUntilSpinnerHidden();
        return PERMISSION_ERROR_MESSAGE.isVisible();
    }

    public void clickAuditLog() {
        waitUtility.waitForLocatorVisible(AUDIT_LOG_NAV);
        AUDIT_LOG_NAV.first().click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public boolean isAuditLogEntryPresent() {
        return AUDIT_LOG_ENTRIES.count() > 0;
    }

    public String getFirstAuditLogTimestamp() {
        if (AUDIT_LOG_ENTRIES.count() > 0) {
            return AUDIT_LOG_ENTRIES.first().innerText().trim();
        }
        return "";
    }
}
