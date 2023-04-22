package com.wbtkj.chat.utils;

import com.wbtkj.chat.exception.MyServiceException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AesUtil {
    //秘钥设置16位
    public static String SECRET_KEY;
    //加密方式
    public static final String AES_TYPE = "AES";
    //秘钥最大长度16位
    public static final int BYTE_LENGTH = 16;

    /**
     * 加密AES
     *
     * @param value 字符串
     * @param key   秘钥
     * @return String
     */
    public static String encryptAES(String key, String value) {
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.US_ASCII), BYTE_LENGTH);
            SecretKey keyStr = new SecretKeySpec(keyBytes, AES_TYPE);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, keyStr);
            byte[] cleartext = value.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(Hex.encodeHex(ciphertextBytes)).toUpperCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES
     *
     * @param encrypted 字符串
     * @param key       秘钥
     * @return String
     */
    public static String decryptAES(String key, String encrypted) throws Exception{
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.US_ASCII), BYTE_LENGTH);
            SecretKey keyStr = new SecretKeySpec(keyBytes, AES_TYPE);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, keyStr);
            byte[] content = Hex.decodeHex(encrypted.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(content);
            return new String(ciphertextBytes);
        } catch (Exception ex) {
            throw new MyServiceException("CDKEY校验失败!请联系管理员!");
        }
    }
}