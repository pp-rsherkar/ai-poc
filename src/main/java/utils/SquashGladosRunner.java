package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class SquashGladosRunner {
    /**
     * Runs the SquashGlados Python module with the given config file.
     *
     * @param configPath Path to config.json (absolute or relative to project root)
     */
    public static void runSquashGladosWithConfig(String configPath) {
        try {
            // 1. Get the Absolute Path of your project
            String projectRoot = System.getProperty("user.dir");

            // 2. Use a specific Python interpreter (customize as needed)
            String pythonExecutable =  projectRoot + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator +
                    "squashglados" + File.separator + ".venv" + File.separator +
                    "Scripts" + File.separator + "python.exe";

            // 3. Set PYTHONPATH to include the src directory containing SquashGlados package
            String squashGladosSrcPath = projectRoot + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "squashglados" + File.separator + "src";

            ProcessBuilder pb = new ProcessBuilder(
                    pythonExecutable, "-m", "SquashGlados", configPath);
            pb.directory(new File(projectRoot));
            pb.redirectErrorStream(true);
            pb.environment().put("PYTHONPATH", squashGladosSrcPath);

            // Example: pass Squash token from Java code
            String squashToken = ConfigReader.getProperty("PROD_SQUASH_SECRET_API_TOKEN");
            if (squashToken != null && !squashToken.isEmpty()) {
                pb.environment().put("PROD_SQUASH_SECRET_API_TOKEN", squashToken);
            } else {
                // Optionally, read from a config or fail gracefully
                System.err.println("Warning: PROD_SQUASH_SECRET_API_TOKEN is not set in environment variables.");
            }

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[SquashGlados]: " + line);
            }
            int exitCode = process.waitFor();
            System.out.println("SquashGlados finished with exit code: " + exitCode);
            if (exitCode != 0) {
                throw new RuntimeException("SquashGlados execution failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Error executing SquashGlados with config!");
            e.printStackTrace();
        }
    }
}
