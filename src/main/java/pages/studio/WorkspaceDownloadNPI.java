package pages.studio;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;


public class WorkspaceDownloadNPI {
    private final Page page;
    private final Locator searchWorkspace;
    private final Locator clickWorkspace;
    private final Locator clickDownloadbutton;
    private final Locator clickNpiListDownload;
    private final Locator clickCSV;
    private final Locator clickDownload;
    private final Locator verifyPopupMessage;
    private final Locator clickXLSX;

    public WorkspaceDownloadNPI(Page page){
        this.page=page;
        this.searchWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.clickWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("PB_Test_Verify1");
        this.clickDownloadbutton= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator(".styles__StyledScheduleStatus-sc-u6f0o3-5 > svg").first();
        this.clickNpiListDownload=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Download NPIs"));
        this.clickCSV=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("CSV");
        this.clickXLSX=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("XLSX"));
        this.clickDownload=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Download"));
        this.verifyPopupMessage=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("NPI List file is ready for");
    }

    public void searchWorkspace(){
        searchWorkspace.fill("verify");
    }
    public void clickWorkspace(){
        clickWorkspace.click();
    }
    public void clickDownloadButton(){
        clickDownloadbutton.click();
    }
    public void clickNPIDownload(){
        clickNpiListDownload.click();
    }
    public void clickCSVFile(){
        clickCSV.click();
    }
    public void clickDownloadNPIButton(){
        clickDownload.click();
    }
    public String verifyToast(){
        String msg=verifyPopupMessage.textContent();
        System.out.println("Toast msg is : "+msg);

        return msg;
    }
    public void clickXSLXFile(){
        clickXLSX.click();
    }


}
