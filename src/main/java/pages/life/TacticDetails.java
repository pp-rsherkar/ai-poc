package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TacticDetails {
    private final Page page;
    private final Locator VERIFY_TACTIC_DETAILS_PAGE;
    private final Locator TACTIC_NAME;
    private final Locator SAVE_TACTIC_DETAILS;
    private final Locator TACTIC_DETAILS_SUCCESS;

    public TacticDetails(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_DETAILS_PAGE = page.locator("//div[text()='New Tactic']");
        this.TACTIC_NAME = page.locator("//input[@placeholder='Tactic Name']");
        this.SAVE_TACTIC_DETAILS = page.locator("//span[text()='Save']");
        this.TACTIC_DETAILS_SUCCESS = page.locator("//div[@aria-label='Success!']");
    }

    public String verifyTacticDetailsText() {
        return VERIFY_TACTIC_DETAILS_PAGE.innerText();
    }

    public void enterTacticName(String tacticName) {
        TACTIC_NAME.fill(tacticName);
    }

    public void saveTacticDetails() {
        SAVE_TACTIC_DETAILS.click();
    }

    public String tacticDetailsSuccess() {
        return TACTIC_DETAILS_SUCCESS.innerText();
    }
}