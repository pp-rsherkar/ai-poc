package pages.healPageObjects;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MCPSelfHealingService {
    /**
     * Sends the DOM context to an MCP server and asks for a valid selector.
     */
    public static String getHealedSelector(String axTree, String intent) {
        try {
            ProcessBuilder pb = new ProcessBuilder("node", "src/test/java/mcpbridge/mcp-bridge.js", intent);
            pb.directory(new File(System.getProperty("user.dir")));
            Process process = pb.start();

            // 1. Write HTML to Node.js
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(axTree);
                writer.flush();
            }

            // 2. Read standard output (The Selector)
            String result;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                result = reader.lines().collect(Collectors.joining("")).trim();
            }

            // 3. IMPORTANT: Check for Errors if result is blank
            if (result.isEmpty()) {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                    String error = errorReader.lines().collect(Collectors.joining("\n"));
                    if (error.contains("429") || error.contains("Quota exceeded")) {
                        System.err.println("ALERT: AI Quota Exceeded. Automatic healing is paused for today.");
                        // You could return a hardcoded fallback here if you know the UI
                        return null;
                    }
                    System.err.println("Node.js Error Log:\n" + error);
                }
            }
            return (result.isEmpty() || result.toLowerCase().contains("error")) ? null : result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}