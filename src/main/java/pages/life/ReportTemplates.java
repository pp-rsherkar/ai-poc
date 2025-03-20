package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

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
    private final Locator VERIFY_CREATED_TEMPLATE;

    public ReportTemplates(Page page) {
        this.page = page;
        this.REPORT_TEMPLATE_LINK = page.locator("//div[normalize-space(text())='Report Templates']");
        this.VERIFY_TEMPLATES_TAB = page.locator("//a[normalize-space(text())='TEMPLATES']");
        this.VERIFY_GENERATED_REPORTS_TAB = page.locator("//a[normalize-space(text())='GENERATED REPORTS']");
        this.VERIFY_SCHEDULING_TAB = page.locator("//a[normalize-space(text())='SCHEDULING']");
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
        this.TEMPLATE_SUCCESS = page.locator("//*[text()='Template created successfully']");
        this.SEARCH_TEMPLATE = page.locator("//input[contains(@class,'gaTableSearch') and @placeholder='Search']");
        this.CLICK_TEMPLATE_SEARCH = page.locator("//div[contains(@class,'search-icon')]");
        this.VERIFY_CREATED_TEMPLATE = page.locator("//div[contains(@class,'favortile-label ellipsediv ng-star-inserted')]");
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

    public void clickDimensionsTab() {
        REPORT_DIMENSIONS.click();
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
        return TEMPLATE_SUCCESS.innerText();
    }

    public void searchCreatedReportTemplate(String createdReportTemplate) {
        SEARCH_TEMPLATE.fill(createdReportTemplate);
        CLICK_TEMPLATE_SEARCH.click();
    }

    public String verifyCreatedReportTemplate(String createdReportTemplate) {
        String templateNameXpath = String.format("//div[contains(text(),'%s')]", createdReportTemplate);
        page.waitForSelector(templateNameXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(templateNameXpath).innerText();
    }

    public int searchResultRowCount() {
        String searchResultXpath = "//tbody[@popuptrigger='manual']//tr[contains(@class,'fixedrow')]";
        return page.locator(searchResultXpath).count();
    }
}