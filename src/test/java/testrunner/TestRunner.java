package testrunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import utils.ConfigReader;

import java.util.logging.Logger;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/life/Create_Campaign.feature"}
        , glue = {"stepdefinitions", "hooks"}
        , tags = "@regression"
        , plugin = {"pretty", "html:target/cucumber-reports/report.html", "json:target/cucumber-reports/cucumber.json", "junit:target/cucumber-reports/Cucumber.xml", "rerun:target/failed_scenarios.txt"
})

public class TestRunner {
    @AfterClass
    public static void generateConfigFile() {
        try {
            String cucumberJsonPath = "target/cucumber-reports/cucumber.json";
            String configPath = "target/config.json";

            String automation = ConfigReader.getProperty("automation");
            String squashServer = ConfigReader.getProperty("squash_server");
            String testIdType = ConfigReader.getProperty("test_id_type");
            String ids = ConfigReader.getProperty("IDS");

            utils.CucumberConfigWriter.writeConfig(
                    cucumberJsonPath, configPath,
                    automation, squashServer, testIdType, ids, cucumberJsonPath);

            // Call SquashGlados after config file is generated
            utils.SquashGladosRunner.runSquashGladosWithConfig(configPath);

            Logger.getLogger(TestRunner.class.getName())
                    .info("Config file generated and SquashGlados executed after all scenarios.");
        } catch (Exception e) {
            Logger.getLogger(TestRunner.class.getName())
                    .severe("Failed to generate config file: " + e.getMessage());
        }
    }
}
