package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream("./src/test/resources/config/config.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDbURL() throws Exception {
        String encryptedDbUrl = properties.getProperty("dbURL");
        return EncryptionDecryption.decrypt(encryptedDbUrl);
    }

    public static String getDbUsername() throws Exception {
        String encryptedDbUsername = properties.getProperty("dbUsername");
        return EncryptionDecryption.decrypt(encryptedDbUsername);
    }

    public static String getDbPassword() throws Exception {
        String encryptedDbPassword = properties.getProperty("dbPassword");
        return EncryptionDecryption.decrypt(encryptedDbPassword);
    }
    public static String getPassword() throws Exception {
        String encryptedPassword = properties.getProperty("preReleasePassword");

        return EncryptionDecryption.decrypt(encryptedPassword);


    }
}