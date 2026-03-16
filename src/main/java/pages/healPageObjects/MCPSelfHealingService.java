package pages.healPageObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MCPSelfHealingService {
    private static final Logger logger = LoggerFactory.getLogger(MCPSelfHealingService.class);

    /**
     * Sends the DOM context to an MCP server and asks for a valid selector.
     */
    public static List<String> getHealedSelectors(String axTree, String intent) {
        try {
            // 1. Start Node.js process with the MCP script and intent as an argument
            ProcessBuilder pb = new ProcessBuilder("node", "src/test/java/mcpbridge/mcp-bridge.js", intent);
            pb.directory(new File(System.getProperty("user.dir")));
            Process process = pb.start();

            // 2. Write HTML to Node.js
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(axTree);
                writer.flush();
            }

            // 3. Read standard output (The Selector)
            String result;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                result = reader.lines().collect(Collectors.joining("")).trim();
            }

            // 4. IMPORTANT: Check for Errors if result is blank
            if (result.isEmpty()) {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                    String error = errorReader.lines().collect(Collectors.joining("\n"));
                    if (error.contains("429") || error.contains("Quota exceeded")) {
                        logger.info("ALERT: AI Quota Exceeded. Automatic healing is paused for today.");
                        return null;
                    }
                    logger.info("Node.js Error Log:\n{}", error);
                }
            }

            if (result.isEmpty() || result.toLowerCase().contains("error")) {
                return Collections.emptyList();
            }

            // Expect selectors separated by newline or comma
            List<String> selectors = Arrays.stream(result.split("\\R")) // split on any line break
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            logger.info("MCP returned {} selector candidates", selectors.size());
            return selectors;
        } catch (Exception e) {
            logger.info("Error{} :", e.getMessage());
            return null;
        }
    }
}