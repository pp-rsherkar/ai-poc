package pages.healPageObjects;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigReader;

import java.util.List;

public class HealingActions {
    private final Page page;
    double timeout = Double.parseDouble(ConfigReader.getProperty("timeout"));
    private static final Logger logger = LoggerFactory.getLogger(HealingActions.class);

    public HealingActions(Page page) {
        this.page = page;
    }

    public void safeClick(Locator locator, String humanIntent) {
        executeWithHealing(
                locator::click,
                Locator::click, humanIntent);
    }

    public void safeFill(Locator locator, String value, String humanIntent) {
        executeWithHealing(
                () -> locator.fill(value),
                healedLocator -> healedLocator.fill(value), humanIntent);
    }

    public Locator safeLocate(Locator locator, String humanIntent) {
        final Locator[] capturedLocator = new Locator[1];
        executeWithHealing(
                () -> {
                    locator.elementHandle(new Locator.ElementHandleOptions().setTimeout(timeout));
                    capturedLocator[0] = locator;
                },
                healedLocator -> {
                    capturedLocator[0] = healedLocator;
                },
                humanIntent
        );
        return capturedLocator[0];
    }

    public void safeWaitUntilLocatorVisible(Locator locator, String humanIntent) {
        executeWithHealing(
                () -> locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeout)),
                healedLocator -> healedLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeout)), humanIntent);
    }

    public void safeWaitUntilSpinnerHidden(Locator locator, String humanIntent) {
        executeWithHealing(
                () -> locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(timeout)),
                healedLocator -> healedLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(timeout)), humanIntent);
    }

    private String capturePageHtml() {
        try {
            // 1. Wait for network to be quiet so dynamic data is present
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // 2. Use JavaScript to extract a clean version of the body
            return (String) page.evaluate("document.body.innerHTML");
        } catch (Exception ex) {
            logger.warn("Cleaned HTML snapshot failed, falling back to raw content: {}", ex.getMessage());
            try {
                return page.content(); // Fallback to standard content
            } catch (Exception e) {
                return null;
            }
        }
    }

    private void executeWithHealing(Runnable originalAction, java.util.function.Consumer<Locator> healedAction, String humanIntent) {
        try {
            originalAction.run();
            return;
        } catch (PlaywrightException originalException) {
            logger.info("Locator failed for '{}'. Attempting MCP Healing...", humanIntent);
            String pageHtml = capturePageHtml();
            if (pageHtml == null || pageHtml.isBlank()) {
                throw originalException;
            }
            List<String> healedSelectors = MCPSelfHealingService.getHealedSelectors(pageHtml, humanIntent);
            if (healedSelectors == null || healedSelectors.isEmpty()) {
                throw originalException;
            }
            for (String selector : healedSelectors) {
                logger.info("Trying healed selector: {}", selector);
                try {
                    Locator locator = page.locator(selector);
                    int count = locator.count();
                    for (int i = 0; i < count; i++) {
                        try {
                            Locator candidate = locator.nth(i);
                            candidate.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));
                            if (!candidate.isVisible()) {
                                logger.info("Skipping hidden element index {}", i);
                                continue;
                            }
                            healedAction.accept(candidate);
                            logger.info("Healing successful using {} (index {})", selector, i);
                            return;
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {}
            }
            throw originalException;
        }
    }
}

