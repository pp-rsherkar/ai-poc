package utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionDecryption {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1234567890abcdef";

    public static String encrypt(String value) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedValue = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decryptedValue = cipher.doFinal(decodedValue);
        byte[] unpadded = stripPKCS7Padding(decryptedValue);

        return new String(unpadded, StandardCharsets.UTF_8);
//        return new String(decryptedValue);
    }
    private static byte[] stripPKCS7Padding(byte[] decrypted) throws Exception {
        int pad = decrypted[decrypted.length - 1] & 0xFF;
        if (pad < 1 || pad > 16) {
            throw new IllegalArgumentException("Invalid padding value: " + pad);
        }
        for (int i = decrypted.length - pad; i < decrypted.length; i++) {
            if (decrypted[i] != (byte) pad) {
                throw new IllegalArgumentException("Invalid padding byte at position " + i);
            }
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }
}