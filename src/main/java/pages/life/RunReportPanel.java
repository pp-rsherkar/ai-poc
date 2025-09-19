package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RunReportPanel {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator RUN_REPORT_PANEL_HEADER;
    private final Locator TEMPLATE_DROPDOWN;
    private final Locator DIMENSION_DROPDOWN;
    private final Locator METRICS_DROPDOWN;
    private final Locator DATA_GRANULARITY_SELECTED_VALUE;
    private final Locator DATA_GRANULARITY_DROPDOWN;
    private final Locator DATA_GRANULARITY_DROPDOWN_VALUES;
    private final Locator ADVERTISER_DROPDOWN;
    private final Locator ADVERTISER_DROPDOWN_VALUES;
    private final Locator ADVERTISER_SELECTED;
    private final Locator DROPDOWN_LIST;
    private final Locator SELECTED_VALUES_FROM_DROPDOWN;
    private final Locator DROPDOWN_LOADER;
    private final Locator FILTER_REPORT_CHECKBOX_LABEL;
    private final Locator ADVANCED_SETTING_LINK;
    private final Locator REPORT_PERIOD_BUTTONS;
    private final Locator RUN_BUTTON;
    private final Locator SUCCESS_ALERT;
    private final Locator REPORT_MODIFY_OPTION;
    private final Locator FETCHED_TEMPLATE_NAME;
    private final Locator FETCHED_ADVERTISER_NAME;
    private final Locator FETCHED_CAMPAIGN_NAME;
    private final Locator FETCHED_LINEITEM_NAME;
    private final Locator FETCHED_TACTIC_NAME;
    private final Locator FETCHED_CREATIVE_NAME;
    private final Locator SEARCH_REPORT;
    private final Locator SEARCH_BUTTON;
    private final Locator START_DATE;
    private final Locator END_DATE;
    private final Locator START_TIME;
    private final Locator END_TIME;
    private final Locator TIME_ZONE;
    private final Locator REPORT_FORMAT_SELECTED_VALUE;
    private final Locator CALENDAR_VIEW;
    private final Locator TEXT_QUALIFIER_CHECKBOX;
    private final Locator FETCHED_DIMENSIONS_AND_METRICS;
    private final Locator DIMENSION_LABEL;
    private final Locator FILE_NAME_TEXTAREA;
    private final Locator FILE_NAME_ERROR;
    private final Locator DEFAULT_FLIGHT_DETAILS;
    private final Locator FLIGHT_DETAILS_DROPDOWN;
    private final Locator FLIGHT_DETAILS_DROPDOWN_VALUE;
    private final Locator FILE_BREAKDOWN_TYPE;

    public RunReportPanel(Page page) {
        this.page = page;
        this.RUN_REPORT_PANEL_HEADER = page.locator("//div[contains(text(),'Run Report')]");
        this.TEMPLATE_DROPDOWN = page.locator("//input[@placeholder='Select Template']");
        this.DIMENSION_DROPDOWN = page.locator("//input[@placeholder='All Dimensions']");
        this.METRICS_DROPDOWN = page.locator("//input[@placeholder='All Metrics']");
        this.DATA_GRANULARITY_DROPDOWN = page.locator("//sui-select[@placeholder='Data Granularity']");
        this.DATA_GRANULARITY_SELECTED_VALUE = page.locator("//sui-select[@placeholder='Data Granularity']//div[@class='text']/span[normalize-space()]");
        this.DATA_GRANULARITY_DROPDOWN_VALUES = page.locator("//sui-select[@placeholder='Data Granularity']//div[@class='menu transition visible']//span[normalize-space()]");
        this.ADVERTISER_DROPDOWN = page.locator("//sui-multi-select[@placeholder='Select Advertiser']");
        this.ADVERTISER_DROPDOWN_VALUES = page.locator("//sui-multi-select[@placeholder='Select Advertiser']//div[@class='menu transition visible']//span[normalize-space()]");
        this.ADVERTISER_SELECTED = page.locator("//sui-multi-select[@placeholder='Select Advertiser']//sui-multi-select-label");
        this.DROPDOWN_LIST = page.locator("//div[contains(@class,'menu transition visible')]");
        this.SELECTED_VALUES_FROM_DROPDOWN = page.locator("//div[contains(@class,'menu transition visible')]//div[contains(@class,'item active filtered')]");
        this.DROPDOWN_LOADER = page.locator("//div[contains(@class,'loading')]");
        this.FILTER_REPORT_CHECKBOX_LABEL = page.locator("//sui-checkbox[contains(@class,'advancedSettingsCheck')]//label");
        this.ADVANCED_SETTING_LINK = page.locator("//label[text()='Advanced Settings']");
        this.REPORT_PERIOD_BUTTONS = page.locator("//label[contains(text(),'Report Period')]/following-sibling::div//button");
        this.RUN_BUTTON = page.locator("//button[contains(@class, 'okButton') and contains(text(),'Run')]");
        this.SUCCESS_ALERT = page.locator("//div[@aria-label='Success!']");
        this.REPORT_MODIFY_OPTION = page.locator("//div[@class='icon report-progress']/ancestor::div[@class='left icon-section']/following-sibling::div//img/following-sibling::div//span[contains(text(),'Modify and Re-run')]");
        this.FETCHED_TEMPLATE_NAME = page.locator("//label[text()='Template']//following-sibling::app-single-select-dropdown//input/following-sibling::span");
        this.FETCHED_ADVERTISER_NAME = page.locator("//sui-multi-select[@placeholder='Select Advertiser']//sui-multi-select-label//span[2]");
        this.FETCHED_CAMPAIGN_NAME = page.locator("//input[@name='campaignLookupInp']/following-sibling::a");
        this.FETCHED_LINEITEM_NAME = page.locator("//input[@name='lineItemLookupInp']/following-sibling::a");
        this.FETCHED_TACTIC_NAME = page.locator("//input[@name='tacticLookupInp']/following-sibling::a");
        this.FETCHED_CREATIVE_NAME = page.locator("//input[@name='creativesLookupInp']/following-sibling::a");
        this.SEARCH_REPORT = page.locator("input.form-control.ng-untouched.ng-pristine.ng-valid");
        this.SEARCH_BUTTON = page.locator("div.iconSprite.search1");
        this.START_DATE = page.locator("//input[@formcontrolname='startDate']");
        this.END_DATE = page.locator("//input[@formcontrolname='endDate']");
        this.START_TIME = page.locator("input[formcontrolname='startTime']");
        this.END_TIME = page.locator("input[formcontrolname='endTime']");
        this.TIME_ZONE = page.locator("//select[@formcontrolname='stimeZone']");
        this.REPORT_FORMAT_SELECTED_VALUE = page.locator("//div[text()='Report Format']/following-sibling::div//div[@class='text']");
        this.CALENDAR_VIEW = page.locator("sui-calendar-date-view");
        this.TEXT_QUALIFIER_CHECKBOX = page.locator("//div[text()='Text Qualifier']/following-sibling::div/sui-checkbox");
        this.FETCHED_DIMENSIONS_AND_METRICS = page.locator("//div[@class='field customTemplate']//span");
        this.DIMENSION_LABEL = page.locator("//label[contains(text(),'Dimensions')]");
        this.FILE_NAME_TEXTAREA = page.locator("//textarea[@placeholder='File Name']");
        this.FILE_NAME_ERROR = page.locator("//div[contains(text(),'Invalid file Name')]");
        this.DEFAULT_FLIGHT_DETAILS = page.locator("//div[@id='date-option-dropdown']/div[contains(@class, 'text')]");
        this.FLIGHT_DETAILS_DROPDOWN = page.locator("//div[@id='date-option-dropdown']");
        this.FLIGHT_DETAILS_DROPDOWN_VALUE = page.locator("//div[@id='date-option-dropdown']/div[@class='menu transition visible']/div");
        this.FILE_BREAKDOWN_TYPE = page.locator("//label[contains(text(),'File Breakdown')]/following-sibling::div/button");
    }

    public boolean isRunReportPanelOpened() {
        waitUtility.waitForLocatorVisible(RUN_REPORT_PANEL_HEADER);
        return RUN_REPORT_PANEL_HEADER.isVisible();
    }

    public void clickLink(String linkName) {
        Locator locator = page.locator(String.format("//span[text()='%s']", linkName));
        locator.click();
    }

    public boolean isTemplateDropdownAvailable() {
        return TEMPLATE_DROPDOWN.isVisible();
    }

    public void selectTemplateFromDropdown(String templateName) {
        TEMPLATE_DROPDOWN.fill(templateName);
        Locator optionLocator = page.locator(String.format("[title*='%s']:visible", templateName));
        int count = optionLocator.count();
        while (count == 0) {
            TEMPLATE_DROPDOWN.fill(templateName);
            page.waitForTimeout(2000);
            count = optionLocator.count();
        }
        int randomIndex = (int) (Math.random() * count);
        optionLocator.nth(randomIndex).click();
    }

    public boolean isDimensionsAndMetricsDisplayed() {
        return DIMENSION_DROPDOWN.isVisible() && METRICS_DROPDOWN.isVisible();
    }

    public String getDefaultDataGranularity() {
        return DATA_GRANULARITY_SELECTED_VALUE.innerText().trim();
    }

    public void showDataGranularityOptions() {
        DATA_GRANULARITY_DROPDOWN.click();
    }

    public List<String> fetchDataGranularityOptions() {
        assertThat(DATA_GRANULARITY_DROPDOWN).hasAttribute("class", Pattern.compile(".*active visible.*"));
        return DATA_GRANULARITY_DROPDOWN_VALUES.allInnerTexts();
    }

    public boolean setDataGranularity(String dropdownValue) {
        if (!DATA_GRANULARITY_DROPDOWN.getAttribute("class").contains("active visible"))
            showDataGranularityOptions();
        CommonUtils.selectAndClickElement(DATA_GRANULARITY_DROPDOWN_VALUES, Collections.singletonList(dropdownValue));
        return DATA_GRANULARITY_SELECTED_VALUE.innerText().equalsIgnoreCase(dropdownValue);
    }

    public boolean isAdvertiserDropdownAvailable() {
        return ADVERTISER_DROPDOWN.isVisible();
    }

    public void clickAdvertiserDropdown() {
        ADVERTISER_DROPDOWN.click();
        waitUtility.waitForLocatorVisible(ADVERTISER_DROPDOWN_VALUES.first());
    }

    public List<String> fetchAdvertisers(){
        return ADVERTISER_DROPDOWN_VALUES.allInnerTexts();
    }

    public boolean selectMultipleAdvertisersFromDropdown() {
        assertThat(ADVERTISER_DROPDOWN).hasAttribute("class", Pattern.compile(".*active visible.*"));
        for (int i = 1; i <= ADVERTISER_DROPDOWN_VALUES.count(); i++) {
            ADVERTISER_DROPDOWN_VALUES.nth(i).click();
        }
        return ADVERTISER_SELECTED.count() > 0;
    }

    public String selectAdvertiser(String advertiser) {
        assertThat(ADVERTISER_DROPDOWN).hasAttribute("class", Pattern.compile(".*active visible.*"));
        CommonUtils.selectAndClickElement(ADVERTISER_DROPDOWN_VALUES, Collections.singletonList(advertiser));
        page.keyboard().press("Escape");
        return ADVERTISER_SELECTED.locator("xpath=.//span[normalize-space()]").innerText();
    }

    public boolean isDropdownValueLoadedForInitials(String initials, String fieldName) {
        Locator locator = page.locator(String.format("//label[text()='%s']/following-sibling::div//input[contains(@placeholder,'%s')]", fieldName,fieldName));
        locator.click();
        locator.type(initials);
        try {
            waitUtility.waitForLocatorVisible(DROPDOWN_LOADER, 1000);
        } catch (Exception ignored) {
        }
        waitUtility.waitForLocatorHidden(DROPDOWN_LOADER);
        assertThat(locator.locator("xpath=./following-sibling::div")).hasAttribute("class", Pattern.compile(".*visible.*"));
        return locator.locator("xpath=./following-sibling::div/div").count() > 0;
    }

    public List<String> selectMultipleValueFromDropdown() {
        int count = DROPDOWN_LIST.count();
        int randomIndex = (int) (Math.random() * count);
        for (int i = 0; i < 2; i++) {
            DROPDOWN_LIST.nth(randomIndex).click();
            waitUtility.waitUntilSpinnerHidden();
        }
        page.waitForTimeout(500);
        page.keyboard().press("Escape");
        return SELECTED_VALUES_FROM_DROPDOWN.allInnerTexts();
    }

    public void clickAdvanceSettings() {
        ADVANCED_SETTING_LINK.click();
    }

    public boolean isFilterReportSectionAvailable(String checkboxName) {
        Locator checkboxLocator = page.locator(String.format("//label[contains(text(),'%s')]/following-sibling::div", checkboxName));
        return checkboxLocator.isVisible();
    }

    public String fetchFilterReportCheckboxLabel(String checkboxLabel) {
        for (int i = 0; i < FILTER_REPORT_CHECKBOX_LABEL.count(); i++) {
            String labelText = FILTER_REPORT_CHECKBOX_LABEL.nth(i).innerText().trim();
            if (labelText.contains(checkboxLabel)) {
                FILTER_REPORT_CHECKBOX_LABEL.nth(i).click();
                return labelText;
            }
        }
        return " ";
    }

    public boolean isFilterReportCheckboxAvailable(String checkboxLabel) {
        for (int i = 0; i < FILTER_REPORT_CHECKBOX_LABEL.count(); i++) {
            String labelText = FILTER_REPORT_CHECKBOX_LABEL.nth(i).innerText().trim();
            if (labelText.contains(checkboxLabel)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRunNowAndScheduleTabsAvailable(String runNowTab, String scheduleTab) {
        Locator runNowTabLocator = page.locator(String.format("//a[contains(text(),'%s')]", runNowTab));
        Locator scheduleTabLocator = page.locator(String.format("//a[contains(text(),'%s')]", scheduleTab));
        return runNowTabLocator.isVisible() && scheduleTabLocator.isVisible();
    }

    public List<String> fetchReportPeriodOptions() {
        return REPORT_PERIOD_BUTTONS.allInnerTexts();
    }

    public void clickFilterReportCheckbox() {
        FILTER_REPORT_CHECKBOX_LABEL.click();
    }

    public void clickRunButton(String fileName) {
        RUN_BUTTON.click();
        if(FILE_NAME_ERROR.isVisible()){
            FILE_NAME_TEXTAREA.fill(fileName);
            RUN_BUTTON.click();
        }
    }

    public String fetchSuccessAlert() {
        String text = SUCCESS_ALERT.innerText().trim();
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
        return text;
    }

    public void clickModifyOption(String templateName) {
        waitUtility.waitUntilPreLoaderHidden();
        waitUtility.waitForElementVisible("div.ui.dropdown.selection.sort-option-dropdown");
        if(!templateName.contains("Custom Template")) {
            SEARCH_REPORT.fill(templateName);
            SEARCH_BUTTON.click();
        }
        Locator templateElements = page.locator(
                String.format("//div[contains(@class, 'content-section')][.//div[contains(@class, 'report-progress')] and .//div[contains(@class, 'name-section') and contains(text(), '%s')]]//img[contains(@class, 'icon-image')]", templateName));
        templateElements.first().click();
        REPORT_MODIFY_OPTION.first().click();
        waitUtility.waitUntilSpinnerHidden();
        page.waitForCondition(() -> FETCHED_TEMPLATE_NAME.isVisible() || DIMENSION_LABEL.isVisible());
    }

    public List<String> extractInnerTextFromLocator(Locator locator) {
        List<String> texts = new ArrayList<>();
        int count = locator.count();
        for (int i = 0; i < count; i++) {
            texts.add(locator.nth(i).innerText());
        }
        return texts;
    }

    public List<String> fetchTemplateValue() {
        if(!FETCHED_TEMPLATE_NAME.isVisible())
            return Collections.singletonList(" ");
        return extractInnerTextFromLocator(FETCHED_TEMPLATE_NAME);
    }

    public List<String> fetchDimensionAndMetricValues() {
        if(!FETCHED_DIMENSIONS_AND_METRICS.first().isVisible())
            return Collections.singletonList(" ");
        return extractInnerTextFromLocator(FETCHED_DIMENSIONS_AND_METRICS);
    }

    public List<String> fetchAdvertiserName() {
        return extractInnerTextFromLocator(FETCHED_ADVERTISER_NAME);
    }

    public List<String> fetchCampaignName() {
        return extractInnerTextFromLocator(FETCHED_CAMPAIGN_NAME);
    }

    public List<String> fetchLineItemName() {
        return extractInnerTextFromLocator(FETCHED_LINEITEM_NAME);
    }

    public List<String> fetchTacticName() {
        return extractInnerTextFromLocator(FETCHED_TACTIC_NAME);
    }

    public List<String> fetchCreativeName() {
        return extractInnerTextFromLocator(FETCHED_CREATIVE_NAME);
    }

    public boolean isReportPeriodSelected(String buttonName) {
        for (int i = 0; i < REPORT_PERIOD_BUTTONS.count(); i++) {
            if (REPORT_PERIOD_BUTTONS.nth(i).innerText().contains(buttonName) && REPORT_PERIOD_BUTTONS.nth(i).getAttribute("class").contains("active")) {
                return true;
            }
        }
        return false;
    }

    public boolean selectStartAndEndDate() {
        YearMonth currentMonth = YearMonth.now();
        int maxDay = currentMonth.lengthOfMonth();
        int today = LocalDate.now().getDayOfMonth();
        int startDay = ThreadLocalRandom.current().nextInt(today, maxDay);
        int endDay = ThreadLocalRandom.current().nextInt(startDay + 1, maxDay + 1);
        boolean startSelected = selectDate(START_DATE, startDay);
        boolean endSelected = selectDate(END_DATE, endDay);
        return startSelected && endSelected;
    }

    private boolean selectDate(Locator input, int day) {
        String dayToSelect = String.valueOf(day);
        try {
            input.click();
            CALENDAR_VIEW.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            Locator dayCells = CALENDAR_VIEW.locator("//td[normalize-space()='" + dayToSelect + "']");
            Locator correctDay = dayCells.nth(0);
            int maxDay = YearMonth.now().lengthOfMonth();
            if (day >= 1 && day <= maxDay) {
                correctDay = dayCells.last();
            }
            correctDay.click();
            waitUtility.waitForLocatorDetached(CALENDAR_VIEW);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean enterStartAndEndTime(String startTime, String endTime) {
        try {
            START_TIME.fill(startTime);
            END_TIME.fill(endTime);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String fetchStartTime(){
        if(START_TIME.isVisible())
            return START_TIME.inputValue();
        return " ";
    }

    public String fetchEndTime(){
        if(END_TIME.isVisible())
            return END_TIME.inputValue();
        return " ";
    }

    public boolean selectTimeZone(String timeZone) {
        try {
            TIME_ZONE.selectOption(new SelectOption().setLabel(timeZone));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String fetchTimeZone(){
        if(TIME_ZONE.isVisible())
            return TIME_ZONE.locator("option:checked").textContent().trim();
        return " ";
    }

    public String fetchDefaultReportFormat(String fileFormat) {
        return REPORT_FORMAT_SELECTED_VALUE.innerText().trim();
    }

    public List<String> fetchReportFormatList() {
        List<String> reportFormatValues = new ArrayList<>();
        REPORT_FORMAT_SELECTED_VALUE.click();
        Locator visibleMenu = page.locator("div.menu.transition:not(.hidden)");
        waitUtility.waitForLocatorVisible(visibleMenu, 2000);
        Locator visibleItems = visibleMenu.locator("div.item");
        List<String> allTexts = visibleItems.allInnerTexts();
        for (String text : allTexts) {
            reportFormatValues.add(text.trim());
        }
        page.keyboard().press("Escape");
        return reportFormatValues;
    }


    public boolean isTestQualifierCheckboxChecked() {
        return TEXT_QUALIFIER_CHECKBOX.getAttribute("class").contains("checked");
    }

    public List<String> verifyButtonsDisabledBeforeLineItemSelection() {
        List<String> disabledButtons = new ArrayList<>();
        for(int i = 0; i < REPORT_PERIOD_BUTTONS.count(); i++){
            if(REPORT_PERIOD_BUTTONS.nth(i).isDisabled())
                disabledButtons.add(REPORT_PERIOD_BUTTONS.nth(i).innerText());
        }
        return disabledButtons;
    }

    public void selectDimension(List<String> dimensionList) {
        DIMENSION_DROPDOWN.click();
        for (String dimension : dimensionList) {
            page.getByText(dimension).click();
        }
        page.keyboard().press("Escape");
    }

    public void selectMetrics(List<String> metricsList) {
        METRICS_DROPDOWN.click();
        for (String metric : metricsList) {
            page.getByText(metric).click();
        }
        page.keyboard().press("Escape");
    }

    public List<String> verifyButtonsEnabledAfterLineItemSelection() {
        List<String> enabledButtons = new ArrayList<>();
        for(int i = 0; i < REPORT_PERIOD_BUTTONS.count(); i++){
            String disabledAttribute = REPORT_PERIOD_BUTTONS.nth(i).getAttribute("disabled");
            boolean isEnabled = (disabledAttribute == null || disabledAttribute.isEmpty());
            if(isEnabled)
                enabledButtons.add(REPORT_PERIOD_BUTTONS.nth(i).innerText());
        }
        return enabledButtons;
    }

    public void selectReportPeriodButton(String buttonName) {
        CommonUtils.selectAndClickElement(REPORT_PERIOD_BUTTONS, Collections.singletonList(buttonName));
    }

    public String selectValueFromDropdown() {
        int count = DROPDOWN_LIST.count();
        int randomIndex = (int) (Math.random() * count);
        DROPDOWN_LIST.nth(randomIndex).click();
        waitUtility.waitUntilSpinnerHidden();
        page.waitForTimeout(500);
        page.keyboard().press("Escape");
        return SELECTED_VALUES_FROM_DROPDOWN.innerText();
    }

    public String isFlightDetailsDisplayed() {
        try {
            waitUtility.waitForLocatorVisible(DEFAULT_FLIGHT_DETAILS);
            return DEFAULT_FLIGHT_DETAILS.innerText();
        }catch (Exception e){
            return " ";
        }
    }

    public List<String> clickDimensionDropdownAndFetchValues() {
        DIMENSION_DROPDOWN.click();
        List<String> values = DIMENSION_DROPDOWN.locator("xpath=/following-sibling::div/div[@class='item']").allInnerTexts();
        page.keyboard().press("Escape");
        return values;
    }

    public List<String> clickMetricDropdownAndFetchValues() {
        METRICS_DROPDOWN.click();
        List<String> values = METRICS_DROPDOWN.locator("xpath=/following-sibling::div/div[@class='item']").allInnerTexts();
        page.keyboard().press("Escape");
        return values;
    }

    public void clickFileBreakdownType(String buttonType) {
        for (int i = 0; i < FILE_BREAKDOWN_TYPE.count(); i++) {
            if (FILE_BREAKDOWN_TYPE.nth(i).innerText().trim().contains(buttonType)) {
                FILE_BREAKDOWN_TYPE.nth(i).click();
                break;
            }
        }
    }

    public List<String> fetchAndSelectFlightDetails() {
        FLIGHT_DETAILS_DROPDOWN.click();
        waitUtility.waitForLocatorVisible(FLIGHT_DETAILS_DROPDOWN_VALUE.last());
        List<String> flightDetails = FLIGHT_DETAILS_DROPDOWN_VALUE.allInnerTexts();
        if (!FLIGHT_DETAILS_DROPDOWN_VALUE.all().isEmpty()) {
            FLIGHT_DETAILS_DROPDOWN_VALUE.first().click();
        }
        return flightDetails;
    }

    public void downloadScheduledReport() {
        waitUtility.waitForElementVisible("div.ui.dropdown.selection.sort-option-dropdown", 60000);
    }

    public String fetchFileName(){
        return FILE_NAME_TEXTAREA.inputValue().trim();
    }
}

