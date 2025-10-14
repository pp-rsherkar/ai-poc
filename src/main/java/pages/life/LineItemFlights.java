package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.WaitUtility;

import java.util.ArrayList;
import java.util.List;

public class LineItemFlights {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator FLIGHT_TAB;
    private final Locator FLIGHT_TABLE;
    private final Locator FLIGHTS_DATES;
    private final Locator FLIGHTS_DETAILS;

    public LineItemFlights(Page page) {
        this.page = page;
        this.FLIGHT_TAB = page.locator("//a[contains(@class,'gaTabFlights')]");
        this.FLIGHT_TABLE = page.locator("//div[contains(@id,'parentTable')]");
        this.FLIGHTS_DATES = page.locator("//tr[contains(@class,'highlighted no-hover-effect')]/td[contains(@class,'active-flight') or contains(@class, '')][position() <= 2]/div");
        this.FLIGHTS_DETAILS = page.locator("//tr[contains(@class,'highlighted no-hover-effect')]/td[contains(@class,'active-flight') or contains(@class, '')][position() <= 4]/div");
    }

    public void clickFlightTab(){
        FLIGHT_TAB.click();
        waitUtility.waitUntilPreLoaderHidden();
    }

    public boolean isFlightTableDisplayed(){
        waitUtility.waitForLocatorVisible(FLIGHT_TABLE);
        return FLIGHT_TABLE.isVisible();
    }

    public List<String> fetchFlightDates(){
       return FLIGHTS_DATES.allInnerTexts();
    }

    public List<String> fetchFlightDetailsFromFlightTab(){
        return FLIGHTS_DETAILS.allInnerTexts();
    }

}