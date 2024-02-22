package com.shoeStore.Kha.shoeStore.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACSHA256Util {
    public static String calculateSignature(String data, String secretKey) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);

            byte[] hashBytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexStringBuilder.append('0');
                }
                hexStringBuilder.append(hex);
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
