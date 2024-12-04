package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.LoginPage;

public class LifeSteps {

    LoginPage loginPage = new LoginPage(DriverFactory.getPage());

    @Given("Life application is logged in as {string}")
    public void life_application_is_looged_in_as(String string) {
        loginPage.navigateToUrl();
        loginPage.enterUsername(string);
        loginPage.enterPassword();
        loginPage.clickLogin();
        Assert.assertEquals("", "Admin Dashboard", loginPage.verifyProfilePage());
        System.out.println(loginPage.verifyProfilePage());
    }

    @Given("I click create campaign")
    public void i_click_create_campaign() {
        System.out.println("TO DO");
    }

    @When("I fill {string} {string} \"\"campaign details")
    public void i_fill_campaign_details(String string, String string2) {
        System.out.println("TO DO");
    }

    @When("I click on save")
    public void i_click_on_save() {
        System.out.println("TO DO");
    }

    @Then("Verify campaign is created successfully")
    public void verify_campaign_is_created_successfully() {
        System.out.println("TO DO");
    }
}