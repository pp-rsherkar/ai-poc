package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import java.util.ArrayList;
import java.util.List;
import utils.WaitUtility;

public class LineItemFlights {
    private final Page page;
    private final Locator FLIGHT_TAB;
    private final Locator FLIGHT_TABLE;
    private final Locator FLIGHTS_DATES;
    private final Locator FLIGHTS_DETAILS;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public LineItemFlights(Page page) {
        this.page = page;
        this.FLIGHT_TAB = page.locator("//a[contains(@class,'gaTabFlights')]");
        this.FLIGHT_TABLE = page.locator("//div[contains(@id,'parentTable')]");
        this.FLIGHTS_DATES = page.locator(
                "//tr[contains(@class,'highlighted no-hover-effect')]/td[contains(@class,'active-flight') or contains(@class, '')][position() <= 2]/div");
        this.FLIGHTS_DETAILS = page.locator(
                "//tr[contains(@class,'highlighted no-hover-effect')]/td[contains(@class,'active-flight') or contains(@class, '')][position() <= 4]/div");
    }

    public void clickFlightTab() {
        FLIGHT_TAB.click();
        waitUtility.waitUntilPreLoaderHidden();
    }

    public boolean isFlightTableDisplayed() {
        waitUtility.waitForLocatorVisible(FLIGHT_TABLE);
        return FLIGHT_TABLE.isVisible();
    }

    public List<String> fetchFlightDates() {
        List<String> flightDates = new ArrayList<>();
        waitUtility.waitForLocatorVisible(FLIGHTS_DATES.first());
        for (int i = 0; i < FLIGHTS_DATES.count(); i++) {
            flightDates.add(FLIGHTS_DATES.nth(i).textContent());
        }
        return flightDates;
    }

    public List<String> fetchFlightDetailsFromFlightTab() {
        return FLIGHTS_DETAILS.allInnerTexts();
    }
}
