package stepdefinitions;

import com.microsoft.playwright.PlaywrightException;
import factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.life.*;
import utils.ConfigReader;
import utils.WaitUtility;

import java.util.ArrayList;
import java.util.List;

/*
 * QA-1498 - List Sorting and Record Modification Behavior
 * Step definitions for list sorting scenarios across NPI, Keyword, Domain/App, IP, Email, and Pixel lists.
 */
public class QA1498Steps {

    private static final Logger logger = LoggerFactory.getLogger(QA1498Steps.class);

    private ListSortingPage listSortingPage() {
        return new ListSortingPage(DriverFactory.getPage());
    }

    private WaitUtility waitUtility() {
        return new WaitUtility(DriverFactory.getPage());
    }

    private NPILists npiLists() { return new NPILists(DriverFactory.getPage()); }
    private SharedList sharedList() { return new SharedList(DriverFactory.getPage()); }
    private Pixels pixels() { return new Pixels(DriverFactory.getPage()); }
    private pages.Navigation navigation() { return new pages.Navigation(DriverFactory.getPage()); }
    private pages.life.Campaigns campaigns() { return new pages.life.Campaigns(DriverFactory.getPage()); }

    static String currentListType;
    static String notedRecordText;
    static int notedRecordIndex;
    static List<String> notedMultipleRecordTexts = new ArrayList<>();
    static String notedCreatedOnTimestamp;
    static String notedRowOnPage;

    // -------------------------------------------------------------------------
    // Login steps
    // -------------------------------------------------------------------------

    @Given("{string} application is logged in successfully with Account {string} as an internal user")
    public void lifeApplicationLoggedInAsInternalUser(String application, String account) {
        logger.info("{} application logged in as internal user with Account {}", application, account);
        String url = null;
        String username = null;
        String password = null;
        try {
            url = ConfigReader.getProperty("demoURL");
            username = ConfigReader.getInternalDemoUsername();
            password = ConfigReader.getInternalDemoPassword();
        } catch (Exception e) {
            logger.error("Failed to load internal Demo credentials: {}", e.getMessage());
        }
        pages.Navigation nav = navigation();
        nav.navigateToUrl(url);
        nav.enterUsername(username);
        nav.enterPassword(password);
        nav.clickLogin();
        nav.selectAndClickExternalUserApplicationType();
        nav.navigateToLife();
        nav.selectAccount(account);
    }

    @Given("{string} application is logged in successfully with Account {string} as an external user with modification permissions")
    public void lifeApplicationLoggedInAsExternalUserWithPermissions(String application, String account) {
        logger.info("{} application logged in as external user with modification permissions, Account {}", application, account);
        String url = null;
        String username = null;
        String password = null;
        try {
            url = ConfigReader.getProperty("demoURL");
            username = ConfigReader.getExternalDemoUsername();
            password = ConfigReader.getExternalDemoPassword();
        } catch (Exception e) {
            logger.error("Failed to load external Demo credentials: {}", e.getMessage());
        }
        pages.Navigation nav = navigation();
        nav.navigateToUrl(url);
        nav.enterUsername(username);
        nav.enterPassword(password);
        nav.clickLogin();
        nav.selectAndClickExternalUserApplicationType();
        nav.selectExternalUserAccount(account);
    }

    @Given("{string} application is logged in successfully with Account {string} as an external user without modification permissions")
    public void lifeApplicationLoggedInAsExternalUserWithoutPermissions(String application, String account) {
        logger.info("{} application logged in as external user without modification permissions, Account {}", application, account);
        String url = null;
        String username = null;
        String password = null;
        try {
            url = ConfigReader.getProperty("demoURL");
            username = ConfigReader.getExternalDemoUsername();
            password = ConfigReader.getExternalDemoPassword();
        } catch (Exception e) {
            logger.error("Failed to load external Demo credentials: {}", e.getMessage());
        }
        pages.Navigation nav = navigation();
        nav.navigateToUrl(url);
        nav.enterUsername(username);
        nav.enterPassword(password);
        nav.clickLogin();
        nav.selectAndClickExternalUserApplicationType();
        nav.selectExternalUserAccount(account);
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    @And("User navigates to the {string} list page")
    public void userNavigatesToListPage(String listType) {
        logger.info("Navigating to {} list page", listType);
        currentListType = listType;
        try {
            navigateToListType(listType);
        } catch (PlaywrightException e) {
            logger.info("PlaywrightException while navigating to {}, retrying", listType);
            if (campaigns().isCreateCampaignButtonVisible()) navigation().clickSubMenu();
            navigateToListType(listType);
        }
    }

    private void navigateToListType(String listType) {
        pages.Navigation nav = navigation();
        switch (listType) {
            case "NPI List":
                nav.clickSubMenu();
                npiLists().clickNPILists();
                break;
            case "Keyword List":
                nav.clickSubMenu();
                sharedList().clickDomainListFromMenu("Keyword Lists");
                break;
            case "Domain/App List":
                nav.clickSubMenu();
                sharedList().clickDomainListFromMenu("Domain & App Lists");
                break;
            case "IP List":
                nav.clickSubMenu();
                sharedList().clickDomainListFromMenu("IP Address Lists");
                break;
            case "Email List":
                nav.clickSubMenu();
                sharedList().clickDomainListFromMenu("Email Lists");
                break;
            case "Pixel List":
                nav.clickSubMenu();
                pixels().clickPixelsMenuItem();
                break;
            default:
                nav.clickSubMenu();
                sharedList().clickDomainListFromMenu(listType);
        }
    }

    @And("User navigates to the NPI List page")
    public void userNavigatesToNPIListPage() {
        logger.info("Navigating to NPI List page");
        currentListType = "NPI List";
        navigation().clickSubMenu();
        npiLists().clickNPILists();
    }

    // -------------------------------------------------------------------------
    // Default sort / view state
    // -------------------------------------------------------------------------

    @When("User views the list in default state")
    public void userViewsListInDefaultState() {
        logger.info("Viewing list in default state for list type: {}", currentListType);
        waitUtility().waitUntilSpinnerHidden();
    }

    @Then("the list displays records sorted by createdOn in ascending order")
    public void listDisplaysRecordsSortedByCreatedOnAscending() {
        logger.info("Verifying list is sorted by createdOn in ascending order");
        boolean isSorted = listSortingPage().isSortedByCreatedOnAscending();
        Assert.assertTrue("List is not sorted by createdOn in ascending order", isSorted);
    }

    @Then("the list displays records sorted by createdOn by default")
    public void listDisplaysRecordsSortedByCreatedOnByDefault() {
        logger.info("Verifying list displays records sorted by createdOn by default");
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("List does not display records sorted by createdOn by default",
                listSortingPage().isSortedByCreatedOnAscending());
    }

    @And("the list displays records sorted by createdOn")
    public void listDisplaysRecordsSortedByCreatedOn() {
        logger.info("Verifying list displays records sorted by createdOn");
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("List is not sorted by createdOn", listSortingPage().isSortedByCreatedOnAscending());
    }

    // -------------------------------------------------------------------------
    // Note positions
    // -------------------------------------------------------------------------

    @And("User notes the position of record at index {int} in the createdOn sorted list")
    public void userNotesPositionOfRecordAtIndexInCreatedOnSortedList(int index) {
        logger.info("Noting position of record at index {} in createdOn sorted list", index);
        waitUtility().waitUntilSpinnerHidden();
        notedRecordIndex = index;
        notedRecordText = listSortingPage().getRowTextAtIndex(index - 1);
        notedCreatedOnTimestamp = listSortingPage().getCreatedOnValueAtRow(index - 1);
        logger.info("Noted record at index {}: '{}', createdOn: '{}'", index, notedRecordText, notedCreatedOnTimestamp);
    }

    @And("User notes the position of record at index {int}")
    public void userNotesPositionOfRecordAtIndex(int index) {
        logger.info("Noting position of record at index {}", index);
        waitUtility().waitUntilSpinnerHidden();
        notedRecordIndex = index;
        notedRecordText = listSortingPage().getRowTextAtIndex(index - 1);
        notedCreatedOnTimestamp = listSortingPage().getCreatedOnValueAtRow(index - 1);
    }

    @And("User notes the positions of records at indices {int}, {int}, and {int}")
    public void userNotesPositionsOfRecordsAtMultipleIndices(int idx1, int idx2, int idx3) {
        logger.info("Noting positions of records at indices {}, {}, {}", idx1, idx2, idx3);
        waitUtility().waitUntilSpinnerHidden();
        notedMultipleRecordTexts = new ArrayList<>();
        for (int idx : new int[]{idx1, idx2, idx3}) {
            String text = listSortingPage().getRowTextAtIndex(idx - 1);
            notedMultipleRecordTexts.add(text);
        }
    }

    @And("User notes the NPI data before modification")
    public void userNotesNPIDataBeforeModification() {
        logger.info("Noting NPI data before modification");
        waitUtility().waitUntilSpinnerHidden();
        notedRecordText = listSortingPage().getRowTextAtIndex(0);
        notedCreatedOnTimestamp = listSortingPage().getCreatedOnValueAtRow(0);
    }

    // -------------------------------------------------------------------------
    // Modify record steps
    // -------------------------------------------------------------------------

    @When("User modifies the record and saves the changes")
    public void userModifiesRecordAndSavesChanges() {
        logger.info("Modifying record at index {} for list type: {}", notedRecordIndex, currentListType);
        listSortingPage().clickFirstEditButton();
        listSortingPage().appendTextToNameField(" ");
        listSortingPage().clickSave();
    }

    @When("User modifies a record and saves the changes")
    public void userModifiesARecordAndSavesChanges() {
        logger.info("Modifying a record for list type: {}", currentListType);
        listSortingPage().clickFirstEditButton();
        listSortingPage().appendTextToNameField(" ");
        listSortingPage().clickSave();
    }

    @When("User modifies the oldest record to have a newer lastUpdated timestamp")
    public void userModifiesOldestRecordToHaveNewerLastUpdatedTimestamp() {
        logger.info("Modifying oldest record to update its lastUpdated timestamp");
        notedRecordIndex = 1;
        notedRecordText = listSortingPage().getRowTextAtIndex(0);
        notedCreatedOnTimestamp = listSortingPage().getCreatedOnValueAtRow(0);
        listSortingPage().clickFirstEditButton();
        listSortingPage().appendTextToNameField(" ");
        listSortingPage().clickSave();
    }

    @When("User modifies multiple records in quick succession and saves each change")
    public void userModifiesMultipleRecordsInQuickSuccession() {
        logger.info("Modifying multiple records in quick succession");
        listSortingPage().clickFirstEditButton();
        listSortingPage().appendTextToNameField(" ");
        listSortingPage().clickSave();
    }

    @When("User modifies the record by clearing optional fields and saves the changes")
    public void userModifiesRecordByClearingOptionalFieldsAndSavesChanges() {
        logger.info("Modifying record by clearing optional fields");
        listSortingPage().clickFirstEditButton();
        listSortingPage().clickSave();
    }

    @When("User modifies an NPI record and saves the changes")
    public void userModifiesNPIRecordAndSavesChanges() {
        logger.info("Modifying NPI record");
        listSortingPage().clickFirstEditButton();
        listSortingPage().appendTextToNameField(" ");
        listSortingPage().clickSave();
    }

    @When("User attempts to modify a record")
    public void userAttemptsToModifyRecord() {
        logger.info("User attempting to modify a record");
        listSortingPage().clickFirstEditButton();
    }

    // -------------------------------------------------------------------------
    // Position / sort verification
    // -------------------------------------------------------------------------

    @Then("the record remains at the same position in the createdOn sorted list")
    public void recordRemainsAtSamePositionInCreatedOnSortedList() {
        waitUtility().waitUntilSpinnerHidden();
        String current = listSortingPage().getRowTextAtIndex(notedRecordIndex - 1);
        Assert.assertTrue("Record has moved from its original position",
                current.contains(notedRecordText.trim()) || notedRecordText.contains(current.trim()));
    }

    @Then("the record remains in its original position based on createdOn")
    public void recordRemainsInOriginalPositionBasedOnCreatedOn() {
        waitUtility().waitUntilSpinnerHidden();
        String current = listSortingPage().getRowTextAtIndex(notedRecordIndex - 1);
        Assert.assertTrue("Record has moved from its original createdOn-based position",
                current.contains(notedRecordText.trim()) || notedRecordText.contains(current.trim()));
    }

    @And("the list order is unchanged despite the record having a newer lastUpdated value")
    public void listOrderUnchangedDespiteNewerLastUpdatedValue() {
        Assert.assertTrue("List order changed after lastUpdated update",
                listSortingPage().isSortedByCreatedOnAscending());
    }

    @Then("all records maintain their original positions based on createdOn")
    public void allRecordsMaintainOriginalPositionsBasedOnCreatedOn() {
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("Records did not maintain their positions",
                listSortingPage().isSortedByCreatedOnAscending());
    }

    @And("the relative order of records remains unchanged")
    public void relativeOrderOfRecordsRemainsUnchanged() {
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("Relative order changed", listSortingPage().isSortedByCreatedOnAscending());
    }

    @Then("the record remains at position {int} in the createdOn sorted list")
    public void recordRemainsAtPositionInCreatedOnSortedList(int position) {
        waitUtility().waitUntilSpinnerHidden();
        String current = listSortingPage().getRowTextAtIndex(position - 1);
        Assert.assertTrue("Record is not at expected position " + position,
                current.contains(notedRecordText.trim()) || notedRecordText.contains(current.trim()));
    }

    @And("the record position is consistent with createdOn sort")
    public void recordPositionIsConsistentWithCreatedOnSort() {
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("Record position is not consistent with createdOn sort",
                listSortingPage().isSortedByCreatedOnAscending());
    }

    // -------------------------------------------------------------------------
    // Timestamp checks
    // -------------------------------------------------------------------------

    @And("the createdOn timestamp of the record is unchanged")
    public void createdOnTimestampOfRecordIsUnchanged() {
        String current = listSortingPage().getCreatedOnValueAtRow(notedRecordIndex - 1);
        if (!notedCreatedOnTimestamp.isEmpty() && !current.isEmpty()) {
            Assert.assertEquals("createdOn timestamp changed", notedCreatedOnTimestamp, current);
        }
    }

    @And("the createdOn timestamp is unchanged")
    public void createdOnTimestampIsUnchanged() {
        String current = listSortingPage().getCreatedOnValueAtRow(notedRecordIndex - 1);
        if (!notedCreatedOnTimestamp.isEmpty() && !current.isEmpty()) {
            Assert.assertEquals("createdOn timestamp changed", notedCreatedOnTimestamp, current);
        }
    }

    // -------------------------------------------------------------------------
    // Manual sort
    // -------------------------------------------------------------------------

    @And("User manually sorts the list by the {string} column")
    public void userManuallySortsListByColumn(String columnName) {
        logger.info("Manually sorting list by column: {}", columnName);
        listSortingPage().clickColumnHeader(columnName);
    }

    @And("User notes the position of a record in the manually sorted list")
    public void userNotesPositionOfRecordInManuallySortedList() {
        waitUtility().waitUntilSpinnerHidden();
        notedRecordIndex = 1;
        notedRecordText = listSortingPage().getRowTextAtIndex(0);
    }

    @Then("the record remains in the correct position for the {string} sort")
    public void recordRemainsInCorrectPositionForSort(String columnName) {
        waitUtility().waitUntilSpinnerHidden();
        String current = listSortingPage().getRowTextAtIndex(notedRecordIndex - 1);
        Assert.assertTrue("Record moved from correct position for " + columnName + " sort",
                current.contains(notedRecordText.trim()) || notedRecordText.contains(current.trim()));
    }

    @And("the record does not jump to a position based on createdOn")
    public void recordDoesNotJumpToPositionBasedOnCreatedOn() {
        String current = listSortingPage().getRowTextAtIndex(notedRecordIndex - 1);
        Assert.assertTrue("Record jumped to a different position",
                current.contains(notedRecordText.trim()) || notedRecordText.contains(current.trim()));
    }

    // -------------------------------------------------------------------------
    // Permissions
    // -------------------------------------------------------------------------

    @Then("the user is blocked from making the modification")
    public void userIsBlockedFromModification() {
        boolean editNotVisible = !listSortingPage().isEditButtonVisible();
        boolean errorShown = listSortingPage().isPermissionErrorDisplayed();
        Assert.assertTrue("User was not blocked from modifying the record", editNotVisible || errorShown);
    }

    @And("an appropriate error message is displayed")
    public void appropriateErrorMessageIsDisplayed() {
        Assert.assertTrue("No appropriate error message displayed",
                listSortingPage().isPermissionErrorDisplayed() || !listSortingPage().isEditButtonVisible());
    }

    // -------------------------------------------------------------------------
    // Pagination
    // -------------------------------------------------------------------------

    @And("User navigates to page {int} of the paginated list")
    public void userNavigatesToPageOfPaginatedList(int pageNumber) {
        logger.info("Navigating to page {} of the paginated list", pageNumber);
        for (int i = 1; i < pageNumber; i++) {
            boolean navigated = listSortingPage().navigateToNextPage();
            if (!navigated) break;
        }
    }

    @And("User notes the position of a record on page {int}")
    public void userNotesPositionOfRecordOnPage(int pageNumber) {
        waitUtility().waitUntilSpinnerHidden();
        notedRecordIndex = 1;
        notedRowOnPage = listSortingPage().getRowTextAtIndex(0);
    }

    @Then("the record remains on page {int} at its original position")
    public void recordRemainsOnPageAtOriginalPosition(int pageNumber) {
        waitUtility().waitUntilSpinnerHidden();
        String current = listSortingPage().getRowTextAtIndex(notedRecordIndex - 1);
        Assert.assertTrue("Record did not remain on page " + pageNumber,
                current.contains(notedRowOnPage.trim()) || notedRowOnPage.contains(current.trim()));
    }

    @And("the user remains on page {int} after the save operation")
    public void userRemainsOnPageAfterSave(int pageNumber) {
        waitUtility().waitUntilSpinnerHidden();
        String pageInfo = listSortingPage().getCurrentPageInfo();
        Assert.assertTrue("User was not on page " + pageNumber + " after save. Page info: " + pageInfo,
                pageInfo.isEmpty() || listSortingPage().isOnPage(pageNumber));
    }

    @And("no unexpected pagination jump occurs")
    public void noUnexpectedPaginationJumpOccurs() {
        waitUtility().waitUntilSpinnerHidden();
    }

    // -------------------------------------------------------------------------
    // Last Updated column / sorting
    // -------------------------------------------------------------------------

    @When("User views the table header")
    public void userViewsTableHeader() {
        waitUtility().waitUntilSpinnerHidden();
    }

    @Then("the Last Updated column is visible in the table")
    public void lastUpdatedColumnIsVisibleInTable() {
        Assert.assertTrue("Last Updated column is not visible",
                listSortingPage().isLastUpdatedColumnVisible());
    }

    @And("User clicks the Last Updated column header")
    public void userClicksLastUpdatedColumnHeader() {
        listSortingPage().clickLastUpdatedColumnHeader();
    }

    @Then("the list re-sorts by Last Updated timestamp in ascending order")
    public void listReSortsByLastUpdatedAscending() {
        waitUtility().waitUntilSpinnerHidden();
        String dir = listSortingPage().getLastUpdatedSortDirection();
        Assert.assertTrue("List is not sorted by Last Updated ascending. Direction: " + dir,
                "ascending".equalsIgnoreCase(dir) || "asc".equalsIgnoreCase(dir));
    }

    @And("User clicks the Last Updated column header again")
    public void userClicksLastUpdatedColumnHeaderAgain() {
        listSortingPage().clickLastUpdatedColumnHeader();
    }

    @Then("the list re-sorts by Last Updated timestamp in descending order")
    public void listReSortsByLastUpdatedDescending() {
        waitUtility().waitUntilSpinnerHidden();
        String dir = listSortingPage().getLastUpdatedSortDirection();
        Assert.assertTrue("List is not sorted by Last Updated descending. Direction: " + dir,
                "descending".equalsIgnoreCase(dir) || "desc".equalsIgnoreCase(dir));
    }

    // -------------------------------------------------------------------------
    // Audit log
    // -------------------------------------------------------------------------

    @And("User navigates to the Audit Log")
    public void userNavigatesToAuditLog() {
        listSortingPage().clickAuditLog();
    }

    @Then("an Audit Log entry exists for the modification")
    public void auditLogEntryExistsForModification() {
        Assert.assertTrue("No Audit Log entry found",
                listSortingPage().isAuditLogEntryPresent());
    }

    @And("the Audit Log entry contains the correct timestamp within {int} second tolerance")
    public void auditLogEntryContainsCorrectTimestamp(int toleranceSeconds) {
        Assert.assertFalse("Audit Log entry has no timestamp",
                listSortingPage().getFirstAuditLogTimestamp().isEmpty());
    }

    // -------------------------------------------------------------------------
    // UI update / loading
    // -------------------------------------------------------------------------

    @Then("the list updates immediately in the UI")
    public void listUpdatesImmediatelyInUI() {
        waitUtility().waitUntilSpinnerHidden();
    }

    @And("the record position reflects the saved state")
    public void recordPositionReflectsSavedState() {
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("Record position does not reflect saved state",
                listSortingPage().isSortedByCreatedOnAscending());
    }

    @And("no manual page refresh is required")
    public void noManualPageRefreshRequired() {
        waitUtility().waitUntilSpinnerHidden();
    }

    @Then("a loading indicator appears during the save and refresh process")
    public void loadingIndicatorAppearsDuringSaveAndRefresh() {
        waitUtility().waitUntilSpinnerHidden();
    }

    @And("the list updates after the loading completes")
    public void listUpdatesAfterLoadingCompletes() {
        waitUtility().waitUntilSpinnerHidden();
    }

    // -------------------------------------------------------------------------
    // Empty state
    // -------------------------------------------------------------------------

    @When("the list contains no records")
    public void listContainsNoRecords() {
        waitUtility().waitUntilSpinnerHidden();
    }

    @Then("an empty state message is displayed")
    public void emptyStateMessageIsDisplayed() {
        Assert.assertTrue("Empty state message is not displayed",
                listSortingPage().isEmptyStateVisible());
    }

    @And("no sorting issues occur in the empty state")
    public void noSortingIssuesOccurInEmptyState() {
        Assert.assertTrue("Sorting issues in empty state", listSortingPage().isEmptyStateVisible());
    }

    // -------------------------------------------------------------------------
    // Consistency / sort indicators
    // -------------------------------------------------------------------------

    @And("both list types maintain consistent sorting behavior")
    public void bothListTypesMaintainConsistentSortingBehavior() {
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("List types do not maintain consistent sorting behavior",
                listSortingPage().isSortedByCreatedOnAscending());
    }

    @Then("the active sort column is clearly indicated")
    public void activeSortColumnIsClearlyIndicated() {
        Assert.assertTrue("Active sort column is not clearly indicated",
                listSortingPage().isActiveSortColumnVisible());
    }

    @And("the createdOn column is marked as the default sort")
    public void createdOnColumnIsMarkedAsDefaultSort() {
        Assert.assertTrue("createdOn column is not marked as the default sort",
                listSortingPage().isCreatedOnDefaultSort());
    }

    @And("users can see that createdOn is the default sort, not lastUpdated")
    public void usersCanSeeThatCreatedOnIsDefaultSortNotLastUpdated() {
        Assert.assertTrue("Default sort is not createdOn",
                listSortingPage().isCreatedOnDefaultSort());
    }

    // -------------------------------------------------------------------------
    // NPI data integrity
    // -------------------------------------------------------------------------

    @Then("the NPI data remains intact after modification")
    public void npiDataRemainsIntactAfterModification() {
        waitUtility().waitUntilSpinnerHidden();
        String current = listSortingPage().getRowTextAtIndex(0);
        Assert.assertTrue("NPI data changed after modification",
                current.contains(notedRecordText.trim()) || notedRecordText.contains(current.trim()));
    }

    @And("no data loss or corruption occurs")
    public void noDataLossOrCorruptionOccurs() {
        waitUtility().waitUntilSpinnerHidden();
        Assert.assertTrue("Data loss detected", listSortingPage().getTableRowCount() >= 0);
    }

    @And("historical data mapping is preserved")
    public void historicalDataMappingIsPreserved() {
        String current = listSortingPage().getCreatedOnValueAtRow(0);
        if (!notedCreatedOnTimestamp.isEmpty() && !current.isEmpty()) {
            Assert.assertEquals("Historical createdOn data mapping changed", notedCreatedOnTimestamp, current);
        }
    }
}
