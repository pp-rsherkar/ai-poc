package utils;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class CucumberConfigWriter {
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

    public static void writeConfig(String cucumberJsonPath, String configPath, String automation, String squashServer, String testIdType, String ids, String results_path) throws IOException {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("automation", automation);
        config.put("squash_server", squashServer);
        config.put("results_path", results_path);
        if ("test-suite".equals(testIdType)) {
            config.put("test_suites", Arrays.asList(ids.split(",")));
        } else {
            config.put("iterations", Arrays.asList(ids.split(",")));
        }

        List<String> scenarioNames = getScenarioNames(cucumberJsonPath);
        List<Map<String, String>> tests = new ArrayList<>();
        for (String name : scenarioNames) {
            Map<String, String> test = new HashMap<>();
            test.put("name", name);
            test.put("command", "");
            tests.add(test);
        }
        config.put("tests", tests);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(configPath)) {
            gson.toJson(config, writer);
        }
    }
}
