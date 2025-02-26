package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class NPILists {
    private final Page page;
    private final Locator CLICK_NPILISTS;
    private final Locator CLICK_ADDLIST;
    private final Locator VERIFY_NPILIST;
    private final Locator CLICK_STATIC_LIST;


    public NPILists (Page page) {
        this.page = page;
        this.CLICK_NPILISTS = page.locator("#megamenu").getByText("NPI Lists");
        this.CLICK_ADDLIST = page.getByText("Add List");
        this.VERIFY_NPILIST = page.getByText("Create New NPI List");
        this.CLICK_STATIC_LIST = page.getByText("Plain static list of NPI");
    }

    public void clickNPILists() {
        CLICK_NPILISTS.click();
    }
    public void clickAddList() {
        CLICK_ADDLIST.click();
    }
    public String verifyNPIListText() {
        return VERIFY_NPILIST.innerText();
    }
    public void clickStaticList() {
        CLICK_STATIC_LIST.click();
    }

}

