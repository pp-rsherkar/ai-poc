package utils;

import java.util.Properties;

public abstract class WebActions {

    public static String getProperty(String key) {
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.initProp();    // Reading from config properties file
        return properties.getProperty(key);
    }

    //base64 encoding: This is used to encrypt the password and save the encrypted value in config.properties file
//    public static void encrypt() {
//        try {
//            byte[] encodedBytes = Base64.getEncoder().encode("yourPassword".getBytes("UTF-8"));
//            String encodedValue = new String(encodedBytes);
//        } catch (Exception e) {
//            System.out.println("Password was not Encrypted.");
//        }
//    }

    //base64 decoding: This is used to decrypt the password from the encrypted value in config.properties file while passing to app
//    public static String decrypt(String passwordField) {
//        String encodedBytes = WebActions.getProperty(passwordField);
//        String decodedString = "";
//        try {
//            byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
//            decodedString = new String(decodedBytes);
//        } catch (Exception e) {
//            System.out.println("Password was nor Decrypted.");
//        }
//        return decodedString;
//    }
}