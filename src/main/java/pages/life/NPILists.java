package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class NPILists {
    private final Page page;
    private final Locator NPI_LISTS;
    private final Locator ADD_LIST;
    private final Locator CREATE_NPI_LIST;
    private final Locator STATIC_LIST;

    public NPILists(Page page) {
        this.page = page;
        this.NPI_LISTS = page.locator("#megamenu").getByText("NPI Lists");
        this.ADD_LIST = page.getByText("Add List");
        this.CREATE_NPI_LIST = page.getByText("Create New NPI List");
        this.STATIC_LIST = page.getByText("Plain static list of NPI");
    }

    public void clickNPILists() {
        NPI_LISTS.click();
    }

    public void clickAddList() {
        ADD_LIST.click();
    }

    public String verifyNPIListText() {
        return CREATE_NPI_LIST.innerText();
    }

    public void clickStaticList() {
        STATIC_LIST.click();
    }
}

