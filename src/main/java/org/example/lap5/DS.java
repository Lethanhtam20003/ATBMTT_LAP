package org.example.lap5;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class DS {
    KeyPair keyPair;
    SecureRandom secureRandom;
    Signature signature;
    PublicKey publicKey;
    PrivateKey privateKey;

    public DS() {
    }
    public DS(String alg,String algRamdom,String prov) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(alg,prov);
        secureRandom = SecureRandom.getInstance(alg,prov);
        keyGen.initialize(1024,secureRandom);
        keyPair = keyGen.genKeyPair();
        signature  = Signature.getInstance(alg,prov);
    }
    public boolean genKey(){
        if(keyPair == null){
            return false;
        }
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        return true;
    }
    public void loadPublic(String key){}
    public String sign(String mes) throws InvalidKeyException, SignatureException {
        byte[] data = mes.getBytes();
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }
    public  String signFile(String src) throws IOException, InvalidKeyException, SignatureException {
        byte[] data = src.getBytes();
        signature.initSign(privateKey);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));

        byte[] buff = new byte[1024];
        int read;
        while ((read = bis.read(buff)) != -1){
            bis.read(buff);
        }
        signature.update(buff);

        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public boolean verify(String mes) throws InvalidKeyException, SignatureException{

        signature.initVerify(publicKey);
        byte[] data = mes.getBytes();
        byte[] signValue = Base64.getDecoder().decode(mes);
        signature.update(data);
        return signature.verify(signValue);
    }
    public boolean verifyFile(String src) throws IOException, InvalidKeyException, SignatureException{
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        byte[] buff = new byte[1024];
        int read;
        while ((read = bis.read(buff)) != -1){
            bis.read(buff);
        }
        return bis.read() == -1;
    }

    public static void main(String[] args) {

    }
}

