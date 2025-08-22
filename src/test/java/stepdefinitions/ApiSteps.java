package stepdefinitions;

import com.microsoft.playwright.APIResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.ApiActions;

import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ApiSteps {

    ApiActions apiActions = new ApiActions();
    APIResponse response;

    @Given("I call {string} with parameters {string} & {string}")
    public void i_call_with_parameters(String string, String string2, String string3) {
        HashMap<String, String> map = new HashMap<>();
//      Add headers & body as per need
        response = apiActions.getRequestWithoutBody(map);
    }

    @Then("Verify response have {string} & {string} & {string}")
    public void verify_response_have(String string, String string2, String string3) {
        assertThat(response).not().isOK();
    }
}
