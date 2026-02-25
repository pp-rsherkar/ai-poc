package pages.healPageObjects;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;

public class HealingActions {
    private final Page page;
    private static final int TIMEOUT = 3000;

    public HealingActions(Page page) {
        this.page = page;
    }

    public void safeClick(Locator locator, String humanIntent) {
        executeWithHealing(
                () -> locator.click(new Locator.ClickOptions().setTimeout(TIMEOUT)),
                healedSelector -> page.click(healedSelector), humanIntent);
    }

    public void safeFill(Locator locator, String value, String humanIntent) {
        executeWithHealing(
                () -> locator.fill(value, new Locator.FillOptions().setTimeout(TIMEOUT)),
                healedSelector -> page.fill(healedSelector, value), humanIntent);
    }

    private void executeWithHealing(Runnable originalAction, java.util.function.Consumer<String> healedAction, String humanIntent) {
        try {
            originalAction.run();
        } catch (PlaywrightException originalException) {

            System.err.println("Locator failed for '" + humanIntent + "'. Attempting MCP Healing...");

            String pageHtml = capturePageHtml();
            if (pageHtml == null || pageHtml.isBlank()) {
                throw originalException;
            }

            String healedSelector = MCPSelfHealingService.getHealedSelector(pageHtml, humanIntent);

            if (healedSelector == null || healedSelector.isBlank()) {
                throw originalException;
            }

            System.out.println("MCP suggested alternative selector: " + healedSelector);

            try {
                healedAction.accept(healedSelector);
            } catch (Exception healingFailure) {
                throw originalException; // preserve original cause
            }
        }
    }

    private String capturePageHtml() {
        try {
            page.waitForLoadState(LoadState.LOAD);
            return page.content();
        } catch (Exception ex) {
            System.err.println("Page HTML snapshot failed: " + ex.getMessage());
            return null;
        }
    }
}

