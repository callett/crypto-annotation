package org.example.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESUtil {

    private static final String KEY = "1234567890123456"; // 16 字节密钥（128 位） 可根据实际项目进行替换

    // 加密
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 明确指定模式和填充
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 解密
    public static String decrypt(String encryptedBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 模式必须和加密一致
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    // String 示例测试
//    public static void main(String[] args) throws Exception {
//        String plaintext = "test";
//        String encrypted = encrypt(plaintext);
//        String decrypted = decrypt(encrypted);
//
//        System.out.println("原文: " + plaintext);
//        System.out.println("加密后: " + encrypted);
//        System.out.println("解密后: " + decrypted);
//    }

    // json 示例测试
    public static void main(String[] args) throws Exception {
        String json = "{\"name\":\"Callett\",\"age\":30}";
        String encrypted = AESUtil.encrypt(json);
        System.out.println(encrypted);
    }
}
