package pages;

import com.microsoft.playwright.Page;

public class ExplorerWorkspace {
    private final Page page;

    public ExplorerWorkspace(Page page) {
        this.page = page;
    }
}