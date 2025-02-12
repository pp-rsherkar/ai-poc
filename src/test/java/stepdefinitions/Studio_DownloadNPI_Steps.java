package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.*;
import org.junit.Assert;
import pages.Navigation;
import pages.Studio_DownloadNPIList;

public class Studio_DownloadNPI_Steps {

    Navigation navigation = new Navigation(DriverFactory.getPage());
    Studio_DownloadNPIList stud=new Studio_DownloadNPIList(DriverFactory.getPage());

    @Given("the admin user is logged into the system as {string}")
    public void theAdminUserIsLoggedIntoTheSystemAs(String string) {
        navigation.navigateToUrl();
        navigation.enterUsername(string);
        navigation.enterPassword();
        navigation.clickLogin();
        Assert.assertEquals("", "Admin Dashboard", navigation.verifyProfilePage());
        navigation.clickLifeMenu();
        System.out.println(navigation.verifyProfilePage());
    }

    @When("user navigates to studio platform")
    public void user_navigates_to_studio_platform() {
        stud.menuclick();
        stud.studio();
    }
    @When("search for workspace")
    public void search_for_workspace() {
        stud.searchworksp();
    }
    @Then("user clicks on the searched workspace")
    public void user_clicks_on_the_searched_workspace() {
        stud.clickWS();
        stud.clickdwnbtn();
        stud.clicknpidwn();
        stud.clickcsvfile();
        stud.clickdwnldbtn();
        Assert.assertEquals("NPI List file is ready for download","NPI List file is ready for download",stud.verifytoast());
        stud.clickdwnbtn();
        stud.clicknpidwn();
        stud.clickXSLXFile();
        stud.clickdwnldbtn();
        Assert.assertEquals("NPI List file is ready for download","NPI List file is ready for download",stud.verifytoast());


    }



}
