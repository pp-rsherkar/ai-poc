package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;


public class Studio_DownloadNPIList {
    private final Page page;
    private final Locator megamenu;
    private final Locator studioClick;
    private final Locator searchWorkspace;
    //private final Locator enterWorkspaceName;
    private final Locator clickWorkspace;
    private final Locator clickDownloadbutton;
    private final Locator clickNpiListDownload;
    private final Locator clickCSV;
    private final Locator clickdwn;
    private final Locator verifyPopupMsg;
    private final Locator clickXLSX;

    public Studio_DownloadNPIList(Page page){
        this.page=page;
        this.megamenu= page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("menu"));
        this.studioClick=page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("Studio")).nth(3);
        this.searchWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
       // this.enterWorkspaceName=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.clickWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("PB_Test_Verify1");
        this.clickDownloadbutton= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator(".styles__StyledScheduleStatus-sc-u6f0o3-5 > svg").first();
        this.clickNpiListDownload=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Download NPIs"));
        this.clickCSV=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("CSV");
        this.clickXLSX=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("XLSX"));
        this.clickdwn=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Download"));
        this.verifyPopupMsg=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("NPI List file is ready for");

    }

    public void menuclick(){
        megamenu.click();
    }
    public void studio(){
        studioClick.click();
        Response reload = page.reload();
    }
    public void searchworksp(){
        searchWorkspace.fill("verify");

    }
    public void clickWS(){
        clickWorkspace.click();
    }
    public void clickdwnbtn(){
        clickDownloadbutton.click();
    }
    public void clicknpidwn(){
        clickNpiListDownload.click();
    }
    public void clickcsvfile(){
        clickCSV.click();
    }
    public void clickdwnldbtn(){
        clickdwn.click();
    }
    public String verifytoast(){
        String msg=verifyPopupMsg.textContent();
        System.out.println("Toast msg is : "+msg);

        return msg;
    }
    public void clickXSLXFile(){
        clickXLSX.click();
    }


}
