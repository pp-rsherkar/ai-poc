package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.*;
import pages.Studio_PublishNPIList;

public class Studio_PublishNPIList_Steps {
Studio_PublishNPIList sp=new Studio_PublishNPIList(DriverFactory.getPage());

    @When("Studio platform is available")
    public void studio_platform_is_available()
    {
        sp.menuclick();
        sp.studio();
    }

    @And("User searches the {string} and selects it")
    public void userSearchesTheAndSelectsIt(String workspace)
    {
        sp.searchworksp(workspace);
        //sp.clickws();
    }

    @When("Download button is enabled to the user")
    public void download_button_is_enabled_to_the_user()
    {
        sp.clickdwnbtn();
    }
    @When("User clicks on Publish NPI List")
    public void user_clicks_on_publish_npi_list()
    {
        sp.clickpubNpi();
    }
    @And("User selects publish {string}")
    public void userSelectsPublish(String listType)
    {
            sp.publish(listType);
            System.out.println("list type is: "+listType);
    }

    @When("User select the system to publish the list")
    public void user_select_the_system_to_publish_the_list() {
        sp.hcp();
        sp.life();

    }
    @Then("The list is available in NPI Lists page")
    public void the_list_is_available_in_npi_lists_page() {
        //sp.clickpubBtn();

    }



}
