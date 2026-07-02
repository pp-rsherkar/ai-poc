package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.Navigation;
import pages.life.DealGroups;
import utils.ConfigReader;

import java.util.List;
import java.util.Map;

/**
 * Step definitions for Deal Groups - Applied Only Tab (QA-1557).
 * Separated from LifeSteps to keep single-responsibility per feature area.
 * The existing step "User clicks {string} Deals Tab" in LifeSteps delegates
 * to PMP.clickDealsTab(), which must be extended to handle "Applied Only"
 * and "All Deals" tab types (see PMP.java modification note in PR).
 *
 * @author iAutomate (QA-1557)
 */
public class DealGroupsSteps {

    private static final Logger logger = LoggerFactory.getLogger(DealGroupsSteps.class);
    DealGroups dealGroups = new DealGroups(DriverFactory.getPage());
    Navigation navigation = new Navigation(DriverFactory.getPage());
    static String url;
    static String username;
    static String password;

    // ==================== Navigation ====================

    @And("User navigates to Supply section")
    public void userNavigatesToSupplySection() {
        logger.info("Navigating to Supply section");
        dealGroups.navigateToSupplySection();
    }

    @And("User navigates to Deal Groups page")
    public void userNavigatesToDealGroupsPage() {
        logger.info("Navigating to Deal Groups page");
        dealGroups.navigateToDealGroupsPage();
    }

    @When("User selects the {string} Deal Group")
    public void userSelectsTheDealGroup(String dealGroupName) {
        logger.info("Selecting Deal Group: {}", dealGroupName);
        dealGroups.selectDealGroup(dealGroupName);
    }

    // ==================== Modal Verification ====================

    @Then("Deal modal should open with {string} and {string} tabs and {string} summary panel")
    public void dealModalShouldOpenWithTabsAndSummaryPanel(
            String tab1, String tab2, String summaryPanel) {
        logger.info("Verifying Deal modal opens with '{}' and '{}' tabs and '{}' panel",
                tab1, tab2, summaryPanel);
        Assert.assertTrue(
                "Deal modal did not open with expected tabs and summary panel",
                dealGroups.isDealModalOpenWithExpectedTabs());
    }

    // ==================== Count Sync ====================

    @Then("Verify {string} tab count matches {string} and the {string} summary panel shows the same count")
    public void verifyTabCountMatchesSummaryPanel(
            String tabName, String expectedCount, String panelName) {
        logger.info("Verifying '{}' tab count matches '{}' and '{}' shows same count",
                tabName, expectedCount, panelName);
        String tabCount = dealGroups.getAppliedOnlyTabCount();
        String summaryCount = dealGroups.getAppliedDealsSummaryPanelCount();
        Assert.assertEquals("Tab count does not match expected", expectedCount, tabCount);
        Assert.assertEquals("Tab count and summary panel count do not match",
                tabCount, summaryCount);
    }

    // ==================== Metadata Parity ====================

    @Then("Verify the deal list under {string} tab displays the following details for each associated deal")
    public void verifyDealListDisplaysDetails(String tabName, DataTable dataTable) {
        logger.info("Verifying deal list under '{}' tab displays expected metadata", tabName);
        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);
        List<Map<String, String>> actualRows = dealGroups.getDealListData();
        Assert.assertEquals("Deal list row count mismatch",
                expectedRows.size(), actualRows.size());
        for (int i = 0; i < expectedRows.size(); i++) {
            Map<String, String> expected = expectedRows.get(i);
            Map<String, String> actual = actualRows.get(i);
            for (String column : expected.keySet()) {
                Assert.assertTrue(
                        "Column '" + column + "' value mismatch at row " + i,
                        actual.get(column).contains(expected.get(column)));
            }
        }
    }

    @And("Verify the {string} tab metadata matches the metadata shown for the same deals in the {string} tab")
    public void verifyMetadataMatchesBetweenTabs(String sourceTab, String targetTab) {
        logger.info("Verifying '{}' tab metadata matches '{}' tab metadata",
                sourceTab, targetTab);
        List<Map<String, String>> appliedOnlyData = dealGroups.getDealListData();
        dealGroups.clickAllDealsTab();
        List<Map<String, String>> allDealsData = dealGroups.getDealListData();
        for (Map<String, String> appliedDeal : appliedOnlyData) {
            boolean found = allDealsData.stream().anyMatch(
                    allDeal -> allDeal.get("Deal ID").equals(appliedDeal.get("Deal ID")));
            Assert.assertTrue(
                    "Deal from Applied Only not found in All Deals: "
                            + appliedDeal.get("Deal ID"),
                    found);
        }
    }

    // ==================== User Type / Feature Flag ====================

    @Given("{string} application is logged in successfully with Account {string} as an {string}")
    public void applicationIsLoggedInAsUserType(
            String application, String account, String userType) {
        logger.info("Logging into '{}' as '{}' with user type '{}'",
                application, account, userType);
        if (userType.toLowerCase().contains("external")) {
            username = ConfigReader.getExternalDemoUsername();
            password = ConfigReader.getExternalDemoPassword();
        } else {
            username = ConfigReader.getInternalDemoUsername();
            password = ConfigReader.getInternalDemoPassword();
        }
        url = ConfigReader.getProperty("demoURL");
        navigation.navigateToUrl(url);
        navigation.enterUsername(username);
        navigation.enterPassword(password);
        navigation.clickLogin();
        navigation.selectAndClickExternalUserApplicationType();
        navigation.navigateToLife();
        navigation.selectAccount(account);
    }

    @And("Applied Deals feature flag is {string} for the account")
    public void appliedDealsFeatureFlagIsSetForTheAccount(String featureState) {
        logger.info("Setting Applied Deals feature flag to '{}'", featureState);
        dealGroups.setAppliedDealsFeatureFlag(featureState);
    }

    @Then("Verify {string} tab is {string}")
    public void verifyTabVisibility(String tabName, String expectedVisibility) {
        logger.info("Verifying '{}' tab visibility is '{}'", tabName, expectedVisibility);
        String actualVisibility = dealGroups.getAppliedOnlyTabVisibilityState();
        Assert.assertEquals("Tab visibility mismatch for " + tabName,
                expectedVisibility, actualVisibility);
    }

    // ==================== Mid-Session Update ====================

    @Then("Verify the {string} summary panel count increments immediately without closing the modal")
    public void verifySummaryPanelCountIncrements(String panelName) {
        logger.info("Verifying '{}' count incremented after deal assignment", panelName);
        int countBefore = dealGroups.getCapturedSummaryCount();
        String newCountStr = dealGroups.getAppliedDealsSummaryPanelCount();
        int countAfter = Integer.parseInt(newCountStr);
        Assert.assertTrue(
                "Summary panel count did not increment. Before: "
                        + countBefore + ", After: " + countAfter,
                countAfter > countBefore);
    }

    @Then("Verify the newly applied deal appears in the {string} list immediately without closing the modal")
    public void verifyNewDealAppearsInList(String tabName) {
        logger.info("Verifying newly applied deal appears in '{}' list", tabName);
        int dealCount = dealGroups.getDealListRowCount();
        Assert.assertTrue(
                "Expected at least one deal in the Applied Only list after assignment",
                dealCount > 0);
    }

    // ==================== Regression: Loader / Empty State ====================

    @Then("Verify the tab does not display a loader indefinitely")
    public void verifyTabDoesNotDisplayLoaderIndefinitely() {
        logger.info("Verifying no indefinite loader on Applied Only tab");
        boolean loaderStuck = dealGroups.isLoaderDisplayedIndefinitely(10000);
        Assert.assertFalse("Loader is displayed indefinitely on the tab", loaderStuck);
    }

    @And("Verify the deal list is not empty when the {string} summary panel shows deals are associated")
    public void verifyDealListNotEmptyWhenSummaryShowsDeals(String panelName) {
        logger.info("Verifying deal list is not empty when '{}' shows associated deals",
                panelName);
        String summaryCount = dealGroups.getAppliedDealsSummaryPanelCount();
        int count = Integer.parseInt(summaryCount);
        if (count > 0) {
            Assert.assertFalse(
                    "Deal list is empty but summary panel shows " + count
                            + " associated deals",
                    dealGroups.isDealListEmpty());
        }
    }

    // ==================== Archived Deals ====================

    @Then("Verify the archived deal still appears in the {string} list")
    public void verifyArchivedDealStillAppearsInList(String tabName) {
        logger.info("Verifying archived deal still appears in '{}' list", tabName);
        Assert.assertTrue(
                "No deals visible in Applied Only after archiving",
                dealGroups.isAnyDealVisibleInAppliedOnlyList());
    }

    // ==================== Rapid Tab Switching ====================

    @When("User rapidly switches between {string} and {string} tabs multiple times")
    public void userRapidlySwitchesBetweenTabs(String tab1, String tab2) {
        logger.info("Rapidly switching between '{}' and '{}' tabs", tab1, tab2);
        dealGroups.rapidlyToggleTabs(5);
    }

    @Then("Verify the deal list does not break or show stale data on either tab")
    public void verifyDealListDoesNotBreakOrShowStaleData() {
        logger.info("Verifying deal list integrity after rapid tab switching");
        Assert.assertTrue(
                "Deal list appears broken or in error state after rapid switching",
                dealGroups.isDealListIntact());
        String tabCount = dealGroups.getAppliedOnlyTabCount();
        String summaryCount = dealGroups.getAppliedDealsSummaryPanelCount();
        Assert.assertEquals("Counts diverged after rapid switching",
                tabCount, summaryCount);
    }
}
