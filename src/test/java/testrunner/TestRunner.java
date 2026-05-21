package testrunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/life/Life_NPI_Lists.feature"}
        , glue = {"stepdefinitions", "hooks"}
        , tags = "@e2e"
        , plugin = {"pretty"}
)

public class TestRunner {
}
