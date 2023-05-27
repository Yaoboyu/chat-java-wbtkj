package com.wbtkj.chat.utils;

import cn.hutool.core.lang.hash.Hash;
import com.wbtkj.chat.exception.MyServiceException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CardGenerator {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String HASH_ALGORITHM = "MD5";
    private static final String SECRET_KEY = "j3jfu3hjxd92i";

    private static SecretKeySpec generateSecretKey() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] keyBytes = md.digest(SECRET_KEY.getBytes());
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static byte[] encrypt(String plainText, SecretKeySpec secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plainText.getBytes(CHARSET_NAME));
    }

    private static String decrypt(byte[] cipherText, SecretKeySpec secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainBytes = cipher.doFinal(cipherText);
        return new String(plainBytes, CHARSET_NAME);
    }

    public static List<String> generateCards(int type, int num, int value) throws Exception {
        SecretKeySpec secretKey = generateSecretKey();
        List<String> cards = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String plainText = type + "," + value + "," + uuid;
            byte[] cipherText = encrypt(plainText, secretKey);
            String card = Base64.getUrlEncoder().withoutPadding().encodeToString(cipherText);
            cards.add(card);
        }
        return cards;
    }

    public static String[] decryptCard(String card) throws Exception {
        SecretKeySpec secretKey = generateSecretKey();
        byte[] cipherText = Base64.getUrlDecoder().decode(card);
        if (cipherText.length % 4 != 0) {
            int padding = 4 - cipherText.length % 4;
            byte[] padded = new byte[cipherText.length + padding];
            System.arraycopy(cipherText, 0, padded, 0, cipherText.length); cipherText = padded;
        }
        String plainText = decrypt(cipherText, secretKey);
        return plainText.split(",");
    }

    public static Map<String, Integer> getInfo(String card) {
        try {
            Map<String, Integer> res = new HashMap<>();
            String[] decrypted = decryptCard(card);
            res.put("type", Integer.valueOf(decrypted[0]));
            res.put("value", Integer.valueOf(decrypted[1]));
            return res;
        } catch (Exception e) {
            throw new MyServiceException("卡密错误！");
        }
    }

    public static void main(String[] args) throws Exception {
        int type = 2;
        int num = 10;
        int value = 1000;
        List<String> cards = generateCards(type, num, value);
        for (String card : cards) {
            System.out.println(card);
        }
        String cardToDecrypt = cards.get(5);
        String[] decrypted = decryptCard(cardToDecrypt);
        System.out.println(getInfo(cardToDecrypt));
    }
}