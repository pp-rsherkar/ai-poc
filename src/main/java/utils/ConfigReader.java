package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream("./src/test/resources/config/config.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("Unable to load properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getDbURL() throws Exception {
        return EncryptionDecryption.decrypt(getProperty("dbURL"));
    }

    public static String getDbUsername() throws Exception {
        return EncryptionDecryption.decrypt(getProperty("dbUsername"));
    }

    public static String getDbPassword() throws Exception {
        return EncryptionDecryption.decrypt(getProperty("dbPassword"));
    }
}


