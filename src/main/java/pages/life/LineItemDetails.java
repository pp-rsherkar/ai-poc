package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LineItemDetails {
    private final Page page;
    private final Locator VERIFY_LINE_ITEM_PAGE;
    private final Locator LINE_ITEM_NAME;
    private final Locator LINE_ITEM_BUDGET;
    private final Locator ENABLE_LINE_ITEM;
    private final Locator SAVE_LINE_ITEM;
    private final Locator LINE_ITEM_SUCCESS;

    public LineItemDetails(Page page) {
        this.page = page;
        this.VERIFY_LINE_ITEM_PAGE = page.locator("//div[text()='New Line Item']");
        this.LINE_ITEM_NAME = page.locator("//input[@placeholder='Line Item Name']");
        this.LINE_ITEM_BUDGET = page.locator("//input[@id='budget0']");
        this.ENABLE_LINE_ITEM = page.locator("//sui-checkbox[@class='toggle ui checkbox ng-untouched ng-pristine ng-valid']");
        this.SAVE_LINE_ITEM = page.locator("//span[text()='Save']");
        this.LINE_ITEM_SUCCESS = page.locator("//div[@aria-label='Success!']");
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
        return LINE_ITEM_SUCCESS.innerText();
    }
}