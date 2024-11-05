package org.example.RSA;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class RSA {
    KeyPair keyPair;
    PublicKey publicKey;
    PrivateKey privateKey;
    public void GenKey() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        keyPair = keyGen.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }
    public String encryptBase64(String data) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(data));
    }
    public byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] in = data.getBytes();

        return cipher.doFinal(in);
    }

    public String decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        RSA lap = new RSA();
        lap.GenKey();
        byte[] test = "test".getBytes("UTF-8");
        System.out.println(Base64.getEncoder().encodeToString(lap.encrypt("test")));
        System.out.println(lap.decrypt(lap.encrypt("test")));
    }
}
