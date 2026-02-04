package utils;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class CucumberConfigWriter {
    static class Config {
        String automation = "cucumber-java-bdd-playwright-maven";
        String squash_server = "prod";
        String test_id_type = "iteration";
        String IDS = "10568";
        List<Map<String, String>> tests;
    }

    public static List<String> getScenarioNames(String cucumberJsonPath) throws IOException {
        List<String> scenarioNames = new ArrayList<>();
        Gson gson = new Gson();
        JsonArray features;
        try (FileReader reader = new FileReader(cucumberJsonPath)) {
            features = gson.fromJson(reader, JsonArray.class);
        }
        for (JsonElement featureElement : features) {
            JsonObject feature = featureElement.getAsJsonObject();
            if (feature.has("elements")) {
                JsonArray elements = feature.getAsJsonArray("elements");
                for (JsonElement element : elements) {
                    JsonObject scenario = element.getAsJsonObject();
                    if (scenario.has("name")) {
                        String name = scenario.get("name").getAsString();
                        if (name != null && !name.trim().isEmpty()) {
                            scenarioNames.add(name);
                        }
                    }
                }
            }
        }
        return scenarioNames;
    }

    public static void writeConfig(String cucumberJsonPath, String configPath, String automation, String squashServer, String testIdType, String ids) throws IOException {
        Config config = new Config();
        config.automation = automation;
        config.squash_server = squashServer;
        config.test_id_type = testIdType;
        config.IDS = ids;

        List<String> scenarioNames = getScenarioNames(cucumberJsonPath);
        config.tests = new ArrayList<>();
        for (String name : scenarioNames) {
            Map<String, String> test = new HashMap<>();
            test.put("name", name);
            test.put("command", "");
            config.tests.add(test);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(configPath)) {
            gson.toJson(config, writer);
        }
    }
}
