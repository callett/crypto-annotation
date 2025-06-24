package org.example.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {
    //    private static PublicKey publicKey;
//    private static PrivateKey privateKey;
    private static final KeyPair keyPair;

    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            keyPair = generator.generateKeyPair(); // 随机生成新的密钥对 每次项目重启更新

            /*
               可根据实际项目进行替换密钥对
             */
//            String pubKeyBase64 = "...你的公钥Base64...";
//            String priKeyBase64 = "...你的私钥Base64...";
//
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            byte[] pubKeyBytes = Base64.getDecoder().decode(pubKeyBase64);
//            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
//            publicKey = keyFactory.generatePublic(pubKeySpec);
//
//            byte[] priKeyBytes = Base64.getDecoder().decode(priKeyBase64);
//            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
//            privateKey = keyFactory.generatePrivate(priKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());    // 可替换上面具体公钥
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());   // 可替换上面具体私钥
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)), StandardCharsets.UTF_8);
    }
}
