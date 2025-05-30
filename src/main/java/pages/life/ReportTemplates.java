package pages.life;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
    private final Locator SELECT_VALUE;
    private final Locator RUN_REPORT;
    private final Locator REPORT_DOWNLOAD_OPTION;
    private final Locator TEMPLATE_COLUMNS;
    private final Locator SEARCH_ICON;
    private final Locator REPORT_PANEL;
    private String reportName;

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
        this.SELECT_TEMPLATE = page.locator("//input[@placeholder='Select Template']");
        this.SELECT_TACTIC = page.locator("//input[@placeholder='All Tactics']");
        this.SELECT_LIFETIME = page.locator("//button[normalize-space()='Lifetime']");
        this.TEMPLATE_COLUMNS = page.locator("//tr[@class='highlighted loadedall']//td[1]");
        this.SEARCH_ICON = page.locator(".search-field > .ui");
        this.SELECT_VALUE = getTacticName("tacticLookup", 1);
        this.RUN_REPORT = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Run").setExact(true));
        this.REPORT_DOWNLOAD_OPTION = page.locator(".inlineDiv > .ui > .pointer").first();
        this.REPORT_PANEL = page.locator(".reports-body > div").first();
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
        String xpath = String.format("//label[text()='%s']", dimension);
        Locator labelElement = page.locator(xpath);
        labelElement.click();
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

    public void enterDetailsToRunReport(String reportTemplateName, String tactic) {
        SELECT_TEMPLATE.fill(reportTemplateName);
        Locator optionLocator = page.getByTitle(reportTemplateName).first();
        optionLocator.click();
        SELECT_TACTIC.fill(tactic);
        page.waitForTimeout(500);
        SELECT_VALUE.click();
        REPORT_PANEL.click();
        SELECT_LIFETIME.click();
    }

    public String verifyAutopopulatedCampaign(String createdCampaign) {
        String campaignNameXpath = String.format("//a[contains(normalize-space(), '%s')]", createdCampaign);
        return page.locator(campaignNameXpath).innerText();
    }

    public String verifyAutopopulatedLineitem(String createdLineitem) {
        String lineitemNameXpath = String.format("//a[contains(normalize-space(), '%s')]", createdLineitem);
        return page.locator(lineitemNameXpath).innerText();
    }

    public void runReport() {
        RUN_REPORT.click();
    }

    public void downloadGeneratedReport() throws IOException {
        page.waitForTimeout(13000);
        page.reload();
        page.waitForTimeout(10000);
        page.reload();
        REPORT_DOWNLOAD_OPTION.click();

        Download download = page.waitForDownload(() -> {
            page.locator("(//span[contains(text(),'Download')])[1]").click();
        });
        reportName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String downloadPath = Paths.get(System.getProperty("user.home"), "Downloads").toString();
        String filePath = Paths.get(downloadPath, "report_" + reportName + ".csv").toString();
        download.saveAs(Paths.get(filePath));
    }

    public void verifyColumnsOfReport(String templateNameRandom) throws IOException {
        SEARCH_TEMPLATE.fill(templateNameRandom);
        SEARCH_ICON.click();
        page.waitForTimeout(1500);

        String data = TEMPLATE_COLUMNS.innerText();

        String[] expectedHeaders = data.split(",");
        for (int i = 0; i < expectedHeaders.length; i++) {
            expectedHeaders[i] = expectedHeaders[i].trim();
            expectedHeaders[i] = expectedHeaders[i].toLowerCase();
            expectedHeaders[i] = expectedHeaders[i].replace("campaign name", "campaign");
            expectedHeaders[i] = expectedHeaders[i].replace("advertiser name", "advertisername");
        }

        String downloadPath = Paths.get(System.getProperty("user.home"), "Downloads").toString();
        String filePath = Paths.get(downloadPath, "report_" + reportName + ".csv").toString();
        BufferedReader reader = null;
        String line = "";
        reader = new BufferedReader(new FileReader(filePath));
        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            String[] newArray = Arrays.copyOfRange(row, 1, row.length);
            for (int j = 0; j < newArray.length; j++) {
                newArray[j] = newArray[j].trim();
                if (newArray[j].startsWith("\"") && newArray[j].endsWith("\"")) {
                    newArray[j] = newArray[j].substring(1, newArray[j].length() - 1);
                }
                newArray[j] = newArray[j].toLowerCase();
            }

            assert Arrays.equals(expectedHeaders, newArray) : "❌ Arrays do not match!\nArray 1: " + Arrays.toString(expectedHeaders) + "\nArray 2: " + Arrays.toString(newArray);
        }
    }

    public Locator getTacticName(String id, int index) {
        return page.locator(String.format("//*[@id='%s']/div/div[%d]", id, index));
    }
}
