package testrunner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        // Rerun failed tests from rerun.txt file
        features = {"src/test/resources/features/"}
        , glue = {"stepdefinitions", "hooks"}
        , tags = "@regression"
        , plugin = {"pretty", "html:target/cucumber-reports/report.html", "rerun:target/rerun.txt"  // Save Failed test scenarios in rerun.txt file
})

public class TestRunner {
}