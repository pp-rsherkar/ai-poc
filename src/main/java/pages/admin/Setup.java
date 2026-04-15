package pages.admin;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.CommonUtils;
import utils.WaitUtility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Setup {
    private final Page page;
    private final Locator SETUP_TAB;
    private final Locator CURATED_MARKET_SUBTAB;
    private final Locator CREATE_CURATED_MARKET_BUTTON;
    private final Locator MARKET_NAME;
    private final Locator SELECT_ACCOUNT_DROPDOWN;
    private final Locator ACCOUNT_DROPDOWN_OPTIONS;
    private final Locator DESCRIPTION;
    private final Locator MARKET_KPI_AND_BENCHMARK;
    private final Locator SAVE_BUTTON;
    private final Locator ALERT;
    private final Locator IMPORT_DEALS_BUTTON;
    private final Locator MARKET_ID;
    private final Locator IMPORT_DEALS_PANEL;
    private final Locator DOWNLOAD_TEMPLATE_LINK;
    private final Locator BROWSE_LINK;
    private final Locator UPLOADED_TEXT;
    private final Locator PREVIEW_BUTTON;
    private final Locator DEAL_ADDED_SUCCESS_TEXT;
    private final Locator UPLOAD_BUTTON;
    private final Locator DEAL_NAME_FROM_DEALS_TAB;
    private final Locator DEAL_ID_FROM_DEALS_TAB;
    private final Locator EXCHANGE_FROM_DEALS_TAB;
    private final Locator DEAL_PRICE_FROM_DEALS_TAB;
    private final Locator PRICING_TYPE_FROM_DEALS_TAB;
    private final Locator MEDIA_TYPE_FROM_DEALS_TAB;
    private final Locator CURATOR_FROM_DEALS_TAB;
    private final Locator MPC_DEAL_TYPE_FROM_DEALS_TAB;
    private final Locator FLOOR_PRICE_FROM_DEALS_TAB;
    private final Locator TOGGLE_BUTTON;
    WaitUtility waitUtility;

    public Setup(Page page) {
        this.page = page;
        this.waitUtility = new WaitUtility(page);
        this.SETUP_TAB = page.locator("//a[@routerlink='/setupManagement']");
        this.CURATED_MARKET_SUBTAB = page.locator("//a[@routerlink='/setupManagement/curated-markets']");
        this.CREATE_CURATED_MARKET_BUTTON = page.locator("//span[text()='Create Curated Market']");
        this.MARKET_NAME = page.locator("//input[@placeholder='Name']");
        this.SELECT_ACCOUNT_DROPDOWN = page.locator("//app-multi-select[@placeholder='Select Accounts']");
        this.ACCOUNT_DROPDOWN_OPTIONS = page.locator("//app-multi-select[@placeholder='Select Accounts']//div[@class='item']//span");
        this.DESCRIPTION = page.locator("//textarea[@formcontrolname='description']");
        this.MARKET_KPI_AND_BENCHMARK = page.locator("//input[@formcontrolname='kpiAndBenchmark']");
        this.SAVE_BUTTON = page.locator("//button[contains(@class,'okButton')]");
        this.ALERT = page.locator("//div[@role='alert']");
        this.IMPORT_DEALS_BUTTON = page.locator("//span[contains(text(),'Import Deals')]");
        this.MARKET_ID = page.locator("//span[contains(text(),'Market Name')]//following-sibling::span");
        this.IMPORT_DEALS_PANEL = page.locator("//div[contains(@class,'rightPanelHeader2') and contains(text(),'Import Deals')]");
        this.DOWNLOAD_TEMPLATE_LINK = page.locator("//span[contains(text(),'Curated Market Template')]");
        this.BROWSE_LINK = page.locator("//a[@id='upload_link']");
        this.UPLOADED_TEXT = page.locator("//span[@class='uploaded-result-text']");
        this.PREVIEW_BUTTON = page.locator("//button[contains(text(),'Preview')]");
        this.DEAL_ADDED_SUCCESS_TEXT = page.locator("//span[contains(@class,'deal-add-count success')]");
        this.UPLOAD_BUTTON = page.locator("//button[contains(text(),'Upload')]");
        this.DEAL_NAME_FROM_DEALS_TAB = page.locator("//input[@placeholder='Deal Name']");
        this.DEAL_ID_FROM_DEALS_TAB = page.locator("//div[contains(@class,'id-col')]/span[@class='deal-name-text']");
        this.EXCHANGE_FROM_DEALS_TAB = page.locator("//div[contains(@class,'exchange-col')]/span[@class='deal-name-text']");
        this.DEAL_PRICE_FROM_DEALS_TAB = page.locator("//input[@placeholder='Price']");
        this.PRICING_TYPE_FROM_DEALS_TAB = page.locator("//div[contains(@class,'pricing-type')]//app-native-dropdown[@labelfield='name']//select");
        this.MEDIA_TYPE_FROM_DEALS_TAB = page.locator("//div[contains(@class,'mediatype')]//div[contains(@class,'transition')]//span");
        this.CURATOR_FROM_DEALS_TAB = page.locator("//div[contains(@class,'curator')]//app-native-dropdown[@labelfield='name']//select");
        this.MPC_DEAL_TYPE_FROM_DEALS_TAB = page.locator("//div[contains(@class,'mpc-deal-type')]//app-native-dropdown[@labelfield='name']//select");
        this.FLOOR_PRICE_FROM_DEALS_TAB = page.locator("//div[contains(@class,'pricing-type')]/following-sibling::div[1]//span[@class='deal-name-text']"); //since we do not have unique identifier for floor price column hence passed the index as 1 to fetch the floor price value from deals tab in curated market details page
        this.TOGGLE_BUTTON = page.locator("//div[contains(@class,'toggle-check')]//sui-checkbox");
    }

    public void clickSetupTab() {
        waitUtility.waitForLocatorVisible(SETUP_TAB);
        SETUP_TAB.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void curatedMarketSubtab() {
        waitUtility.waitForLocatorVisible(CURATED_MARKET_SUBTAB);
        CURATED_MARKET_SUBTAB.click();
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorVisible(CREATE_CURATED_MARKET_BUTTON);
    }

    public void clickCuratedMarketLink() {
        CREATE_CURATED_MARKET_BUTTON.click();
        waitUtility.waitForLocatorVisible(MARKET_NAME);
    }

    public void enterCuratedMarketDetails(String marketNameUnique, String accountName, String description, String marginKPIAndBenchmark) {
        MARKET_NAME.fill(marketNameUnique);
        SELECT_ACCOUNT_DROPDOWN.click();
        waitUtility.waitForLocatorVisible(ACCOUNT_DROPDOWN_OPTIONS.last());
        ACCOUNT_DROPDOWN_OPTIONS.locator("text='" + accountName + "'").click();
        page.keyboard().press("Escape");
        DESCRIPTION.fill(description);
        MARKET_KPI_AND_BENCHMARK.fill(marginKPIAndBenchmark);
    }

    public void clickSaveButton() {
        SAVE_BUTTON.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String getAlertMessage() {
        String alertText = ALERT.textContent().trim();
        waitUtility.waitForLocatorHidden(ALERT);
        return alertText;
    }

    public void clickTabName(String tabName) {
        Locator tabLocator = page.locator(String.format("//span[contains(text(),'%s')]", tabName));
        tabLocator.click();
    }

    public void clickImportDealsButton() {
        IMPORT_DEALS_BUTTON.click();
    }

    public String fetchCuratedMarketId() {
        String[] marketIdParts = MARKET_ID.textContent().trim().split(":");
        return marketIdParts.length > 1 ? marketIdParts[1].trim() : "";
    }

    public boolean isImportDealsPanelOpened() {
        return IMPORT_DEALS_PANEL.isVisible();
    }

    public Path downloadCuratedMarketTemplate() throws IOException {
        Download download = page.waitForDownload(DOWNLOAD_TEMPLATE_LINK::click);
        waitUtility.waitUntilSpinnerHidden();
        return CommonUtils.downloadFileAndMoveToSystemFolder(download);
    }

    public void fillCuratedMarketTemplate(Path filePath, String marketID, String dealNameUnique, String exchange, String mediaType, String curator, String dealPrice, String pricingType, String mpcDealType) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("Market ID", Integer.parseInt(marketID));
        data.put("Deal ID", dealNameUnique);
        data.put("Deal Name", dealNameUnique);
        data.put("Exchange", exchange);
        data.put("Media Type", mediaType);
        data.put("Curator", curator);
        data.put("Publisher Deal Price", Integer.parseInt(dealPrice));
        data.put("Pricing Type", pricingType);
        data.put("MPC Deal Type", mpcDealType);

        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            int rowIndex = sheet.getLastRowNum() + 1;
            Row dataRow = sheet.createRow(rowIndex);

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                String columnName = headerRow.getCell(i).getStringCellValue().trim();
                Cell cell = dataRow.createCell(i);

                if (data.containsKey(columnName)) {
                    Object value = data.get(columnName);
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                workbook.write(fos);
            }
        }
    }

    public void browseCuratedMarketTemplate(Path path) {
        FileChooser fileChooser = page.waitForFileChooser(BROWSE_LINK::click);
        fileChooser.setFiles(path);
        waitUtility.waitForLocatorVisible(UPLOADED_TEXT);
    }

    public void clickPreviewButton() {
        PREVIEW_BUTTON.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public boolean isDealAddedSuccessTextVisible() {
        waitUtility.waitForLocatorVisible(DEAL_ADDED_SUCCESS_TEXT);
        return DEAL_ADDED_SUCCESS_TEXT.isVisible();
    }

    public void clickUploadButton() {
        UPLOAD_BUTTON.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public List<String> fetchDealNameFromDealTab() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < DEAL_NAME_FROM_DEALS_TAB.count(); i++) {
            values.add(DEAL_NAME_FROM_DEALS_TAB.nth(i).inputValue().trim());
        }
        return values;
    }

    public List<String> fetchDealIdFromDealTab() {
        return fetchTextValues(DEAL_ID_FROM_DEALS_TAB);
    }

    public List<String> fetchExchangeTypeFromDealTab() {
        return fetchTextValues(EXCHANGE_FROM_DEALS_TAB);
    }

    public List<String> fetchDealPriceFromDealTab() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < DEAL_PRICE_FROM_DEALS_TAB.count(); i++) {
            String value = DEAL_PRICE_FROM_DEALS_TAB.nth(i).inputValue();
            values.add(value.split("\\.")[0].trim());
        }
        return values;
    }

    public List<String> fetchPricingTypeFromDealTab() {
        return fetchTextValues(PRICING_TYPE_FROM_DEALS_TAB.locator("option:checked"));
    }

    public List<String> fetchMediaTypeFromDealTab() {
        return fetchTextValues(MEDIA_TYPE_FROM_DEALS_TAB);
    }

    public List<String> fetchCuratorFromDealTab() {
        return fetchTextValues(CURATOR_FROM_DEALS_TAB.locator("option:checked"));
    }

    public List<String> fetchMPCDealTypeFromDealTab() {
        return fetchTextValues(MPC_DEAL_TYPE_FROM_DEALS_TAB.locator("option:checked"));
    }

    public List<String> fetchFloorPriceForImportedDeal() {
        return fetchTextValues(FLOOR_PRICE_FROM_DEALS_TAB);
    }

    private List<String> fetchTextValues(Locator locator) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < locator.count(); i++) {
            values.add(locator.nth(i).textContent().trim());
        }
        return values;
    }

    public void enableCuratedMarket() {
        if (!TOGGLE_BUTTON.getAttribute("class").contains("checked")) {
            TOGGLE_BUTTON.click();
            waitUtility.waitUntilSpinnerHidden();
        }
    }
}