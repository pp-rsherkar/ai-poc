package pages.life;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.ExcelActions;
import utils.WaitUtility;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReportTemplates {
    private final Page page;
    private final Locator REPORT_TEMPLATE_LINK;
    private final Locator VERIFY_TEMPLATES_TAB;
    private final Locator VERIFY_GENERATED_REPORTS_TAB;
    private final Locator VERIFY_SCHEDULING_TAB;
    private final Locator NEW_TEMPLATE;
    private final Locator REPORT_DIMENSIONS;
    private final Locator REPORT_METRICS;
    private final Locator TEMPLATE_NAME;
    private final Locator SEARCH_DIMENSION;
    private final Locator SELECT_DIMENSION;
    private final Locator SEARCH_METRIC;
    private final Locator SELECT_METRIC;
    private final Locator VERIFY_DIMENSION;
    private final Locator VERIFY_METRIC;
    private final Locator SAVE_TEMPLATE;
    private final Locator TEMPLATE_SUCCESS;
    private final Locator SEARCH_TEMPLATE;
    private final Locator CLICK_TEMPLATE_SEARCH;
    private final Locator SELECT_TEMPLATE;
    private final Locator SELECT_TACTIC;
    private final Locator SELECT_LIFETIME;
    private final Locator RUN_REPORT;
    private final Locator REPORT_DOWNLOAD_OPTION;
    private final Locator TEMPLATE_COLUMNS;
    private final Locator SEARCH_ICON;
    private final Locator REPORT_PANEL;
    private final Locator SEARCH_REPORT;
    private final Locator SEARCH_BUTTON;
    private final Locator DOWNLOAD_REPORT;
    private final Locator REPORT_PROGRESS_ICON;
    private final Locator TEMPLATE_PAGINATION;
    private final Locator TREE_COLLAPSED_ICON;
    private final Locator DIMENSION_AND_METRICS_LABELS;
    private final Locator CANCEL_BUTTON;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public ReportTemplates(Page page) {
        this.page = page;
        this.REPORT_TEMPLATE_LINK = page.locator("//div[normalize-space(text())='Report Templates']");
        this.VERIFY_TEMPLATES_TAB = page.locator("//a[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'templates')]");
        this.VERIFY_GENERATED_REPORTS_TAB = page.locator("//a[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'generated reports')]");
        this.VERIFY_SCHEDULING_TAB = page.locator("//a[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'scheduling')]");
        this.NEW_TEMPLATE = page.locator("//button[normalize-space(text())='New Template']");
        this.REPORT_DIMENSIONS = page.locator("//div[normalize-space(text())='Dimensions']");
        this.REPORT_METRICS = page.locator("//div[normalize-space(text())='Metrics']");
        this.TEMPLATE_NAME = page.locator("//input[contains(@class,'template-name')]");
        this.SEARCH_DIMENSION = page.locator("//input[contains(@class,'search group_list') and @placeholder='Search']");
        this.SELECT_DIMENSION = page.locator("//label[text()='Advertiser Name']");
        this.SEARCH_METRIC = page.locator("//input[contains(@class,'search group_list') and @placeholder='Search']");
        this.SELECT_METRIC = page.locator("//label[text()='Impressions']");
        this.VERIFY_DIMENSION = page.locator("//sortable-item[contains(@class,'diemension')]");
        this.VERIFY_METRIC = page.locator("//sortable-item[contains(@class,'metric')]");
        this.SAVE_TEMPLATE = page.locator("//button[normalize-space(text())='Ok']");
        this.TEMPLATE_SUCCESS = page.locator("//div[@role='alert' and contains(text(),'Template created successfully')]");
        this.SEARCH_TEMPLATE = page.locator("//input[contains(@class,'gaTableSearch') and @placeholder='Search']");
        this.CLICK_TEMPLATE_SEARCH = page.locator("//div[contains(@class,'gaTableSearchBtn')]");
        this.SELECT_TEMPLATE = page.locator("//input[@placeholder='Select Template']");
        this.SELECT_TACTIC = page.locator("//input[@placeholder='All Tactics']");
        this.SELECT_LIFETIME = page.locator("//button[normalize-space()='Lifetime']");
        this.TEMPLATE_COLUMNS = page.locator("//tr[contains(@class, 'highlighted') and contains(@class, 'loadedall')]//td[1]/div");
        this.SEARCH_ICON = page.locator(".search-field > .ui");
        this.REPORT_PROGRESS_ICON = page.locator("div.icon.report-progress");
        this.RUN_REPORT = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Run").setExact(true));
        this.REPORT_DOWNLOAD_OPTION = page.locator("//img[@title='options']");
        this.REPORT_PANEL = page.locator(".reports-body > div").first();
        this.SEARCH_REPORT = page.locator("input.form-control.ng-untouched.ng-pristine.ng-valid");
        this.SEARCH_BUTTON = page.locator("div.iconSprite.search1");
        this.DOWNLOAD_REPORT = page.locator("//span[text()='Download']");
        this.TEMPLATE_PAGINATION = page.locator("div.pagination-wrapper");
        this.TREE_COLLAPSED_ICON = page.locator("//i[@class='icon_custom tree-collapsed']");
        this.DIMENSION_AND_METRICS_LABELS = page.locator("//div[contains(@class,'checkbox-group-item')]//sui-checkbox//label");
        this.CANCEL_BUTTON = page.locator("//div[@class='targetingFooter']//button[contains(text(),'Cancel')]");
    }

    public void clickReportTemplatesLink() {
        REPORT_TEMPLATE_LINK.click();
    }

    public String verifyTemplatesTab() {
        return VERIFY_TEMPLATES_TAB.innerText();
    }

    public String verifyGeneratedReportsTab() {
        return VERIFY_GENERATED_REPORTS_TAB.innerText();
    }

    public String verifySchedulingTab() {
        return VERIFY_SCHEDULING_TAB.innerText();
    }

    public void createNewTemplate() {
        NEW_TEMPLATE.click();
    }

    public String verifyDimensionsTab() {
        return REPORT_DIMENSIONS.innerText();
    }

    public String verifyMetricsTab() {
        return REPORT_METRICS.innerText();
    }

    public void clickMetricsTab() {
        REPORT_METRICS.click();
    }

    public void enterTemplateName(String templateName) {
        TEMPLATE_NAME.fill(templateName);
    }

    public void selectDimension(String dimension) {
        SEARCH_DIMENSION.fill(dimension);
        SELECT_DIMENSION.click();
        SEARCH_DIMENSION.clear();
    }

    public void selectDimensione2e(String dimension) {
        SEARCH_DIMENSION.fill(dimension);
        page.locator(String.format("//label[text()='%s']", dimension)).click();
        SEARCH_DIMENSION.clear();
    }

    public void selectMetric(String metric) {
        SEARCH_METRIC.fill(metric);
        SELECT_METRIC.click();
        SEARCH_METRIC.clear();
    }

    public String verifySelectedDimensions() {
        return VERIFY_DIMENSION.innerText();
    }

    public String verifySelectedMetrics() {
        return VERIFY_METRIC.innerText();
    }

    public void saveReportTemplate() {
        SAVE_TEMPLATE.click();
    }

    public String reportTemplateSuccess() {
        String message = TEMPLATE_SUCCESS.innerText();
        TEMPLATE_SUCCESS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        return message;
    }

    public void searchCreatedReportTemplate(String createdReportTemplate) {
        SEARCH_TEMPLATE.fill(createdReportTemplate);
        waitUtility.waitForLocatorVisible(CLICK_TEMPLATE_SEARCH, 5000);
        CLICK_TEMPLATE_SEARCH.click(new Locator.ClickOptions().setForce(true));
    }

    public String verifyCreatedReportTemplate(String createdReportTemplate) {
        String templateNameXpath = String.format("//div[contains(text(),'%s')]", createdReportTemplate);
        waitUtility.waitForElementVisible(templateNameXpath);
        return page.locator(templateNameXpath).innerText();
    }

    public int searchResultRowCount() {
        return page.locator("//tbody[@popuptrigger='manual']//tr[contains(@class,'fixedrow')]").count();
    }

    public void enterDetailsToRunReport(String reportTemplateName, String tactic) {
        SELECT_TEMPLATE.fill(reportTemplateName);
        Locator optionLocator = page.getByTitle(reportTemplateName).first();
        while (!optionLocator.isVisible()) {
            SELECT_TEMPLATE.fill(reportTemplateName);
            page.waitForTimeout(2000);
        }
        optionLocator.click();
        SELECT_TACTIC.fill(tactic);
        page.locator(String.format("//div[contains(text(),'%s')]", tactic)).click();
        REPORT_PANEL.click();
        SELECT_LIFETIME.click();
    }

    public String verifyAutopopulatedCampaign(String createdCampaign) {
        return page.locator(String.format("//a[contains(normalize-space(), '%s')]", createdCampaign)).innerText();
    }

    public String verifyAutopopulatedLineitem(String createdLineitem) {
        return page.locator(String.format("//a[contains(normalize-space(), '%s')]", createdLineitem)).innerText();
    }

    public void runReport() {
        RUN_REPORT.click();
    }

    public String downloadGeneratedReport(String templateNameRandom) throws IOException {
        waitUtility.waitForElementVisible("div.ui.dropdown.selection.sort-option-dropdown", 60000);
        SEARCH_REPORT.fill(templateNameRandom);
        SEARCH_BUTTON.click();
        waitUtility.waitForElementVisible(String.format("//div[contains(text(), '%s')]", templateNameRandom));
        while (REPORT_PROGRESS_ICON.isVisible()) {
            SEARCH_BUTTON.click();
            page.waitForTimeout(5000); // wait for 1 second
        }
        waitUtility.waitForLocatorDetached(REPORT_PROGRESS_ICON);
        REPORT_DOWNLOAD_OPTION.click();
        Download download = page.waitForDownload(DOWNLOAD_REPORT::click);
        String REPORT_NAME = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String downloadPath = Paths.get(System.getProperty("user.home"), "Downloads").toString();
        String filePath = Paths.get(downloadPath, "report_" + REPORT_NAME + ".csv").toString();
        download.saveAs(Paths.get(filePath));
        return filePath;
    }

    public boolean verifyColumnsOfReport(String templateNameRandom, String filePath) throws Exception {
        waitUtility.waitForLocatorVisible(TEMPLATE_PAGINATION);
        waitUtility.waitUntilPreLoaderHidden();
        SEARCH_TEMPLATE.fill(templateNameRandom);
        SEARCH_ICON.click(new Locator.ClickOptions().setForce(true));
        waitUtility.waitForElementVisible(String.format("//div[contains(text(), '%s')]", templateNameRandom), 5000);
        List<String> expectedHeaders = Arrays.stream(TEMPLATE_COLUMNS.innerText().split("\\s*,\\s*")).map(h -> h.toLowerCase().replaceAll("\\s+", ""))  // Normalize expected
                .toList();

        List<String> rawActualHeaders = ExcelActions.readCsvExcludingFirstColumn(filePath);
        List<String> actualHeaders = rawActualHeaders.stream().map(h -> h.toLowerCase().replaceAll("\\s+", ""))  // Normalize actual
                .toList();

        return expectedHeaders.stream().allMatch(expected -> actualHeaders.stream().anyMatch(actual -> actual.contains(expected) || expected.contains(actual)));
    }

    public List<String> expandGroupsAndFetchDimensionsAndMetrics() {
        while (TREE_COLLAPSED_ICON.count() > 0) {
            TREE_COLLAPSED_ICON.first().scrollIntoViewIfNeeded();
            TREE_COLLAPSED_ICON.first().click();
        }
        return DIMENSION_AND_METRICS_LABELS.allInnerTexts();
    }

    public void clickCancelButton() {
        CANCEL_BUTTON.click();
    }

    public boolean verifyReportGeneratedFromLineItemPage(String reportName){
        Locator xpath = page.locator(String.format("//div[contains(@class,'scopelist') and contains(., '%s')]",reportName));
        return xpath.isVisible();
    }
}