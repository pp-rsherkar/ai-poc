package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.WaitUtility;

public class LineItemDetails {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator VERIFY_LINE_ITEM_PAGE;
    private final Locator LINE_ITEM_NAME;
    private final Locator LINE_ITEM_BUDGET;
    private final Locator ENABLE_LINE_ITEM;
    private final Locator SAVE_LINE_ITEM;
    private final Locator LINE_ITEM_SUCCESS;
    private final Locator LINE_ITEM_TYPE_DROPDOWN;
    private final Locator LINE_ITEM_TYPE_VALUE;
    private final Locator ADD_FLIGHT_BUTTON;

    public LineItemDetails(Page page) {
        this.page = page;
        this.VERIFY_LINE_ITEM_PAGE = page.locator("//div[text()='New Line Item']");
        this.LINE_ITEM_NAME = page.locator("//input[@placeholder='Line Item Name']");
        this.LINE_ITEM_BUDGET = page.locator("//input[@id='budget0']");
        this.ENABLE_LINE_ITEM = page.locator("//sui-checkbox[@class='toggle ui checkbox ng-untouched ng-pristine ng-valid']");
        this.SAVE_LINE_ITEM = page.locator("//span[text()='Save']");
        this.LINE_ITEM_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.LINE_ITEM_TYPE_DROPDOWN = page.locator("//div[contains(@class,'lineItemType')]");
        this.LINE_ITEM_TYPE_VALUE = page.locator("//div[contains(@class,'gaCostType')]/div");
        this.ADD_FLIGHT_BUTTON = page.locator("//app-icon-lable-link[contains(@text,'Add Flight')]");
    }

    public String verifyLineItemText() {
        return VERIFY_LINE_ITEM_PAGE.innerText();
    }

    public void enterLineItemName(String lineItemName) {
        LINE_ITEM_NAME.fill(lineItemName);
    }

    public void enterLineItemBudget(String lineBudget) {
        LINE_ITEM_BUDGET.fill(lineBudget);
    }

    public void enableLineItem() {
        ENABLE_LINE_ITEM.click();
    }

    public void saveLineItem() {
        SAVE_LINE_ITEM.click();
    }

    public String lineItemSuccess() {
        String successMessage = LINE_ITEM_SUCCESS.innerText().trim();
        waitUtility.waitUntilSpinnerHidden();
        return successMessage;
    }

    public void selectLineItemType(String lineItemType) {
        LINE_ITEM_TYPE_DROPDOWN.click();
        int count = LINE_ITEM_TYPE_VALUE.count();
        for(int i=0; i< count; i++) {
            if(LINE_ITEM_TYPE_VALUE.nth(i).innerText().equals(lineItemType)) {
                LINE_ITEM_TYPE_VALUE.nth(i).scrollIntoViewIfNeeded();
                LINE_ITEM_TYPE_VALUE.nth(i).click();
                break;
            }
        }
    }
    public void clickAddFlightButton() {
        ADD_FLIGHT_BUTTON.click();
    }
}