package com.rev.password_service.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {

    private static final String SECRET_KEY = "1234567890123456"; // 16 characters for AES

    // Encrypt password
    public String encrypt(String data) {

        try {

            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting", e);
        }
    }

    // Decrypt password
    public String decrypt(String encryptedData) {

        try {

            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);

            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting", e);
        }
    }
}