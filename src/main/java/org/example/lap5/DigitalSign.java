package org.example.lap5;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class DigitalSign {
    KeyPair keyPair;
    SecureRandom random;
    Signature signature;
    PublicKey publicKey;
    PrivateKey privateKey;
    public DigitalSign() {}
    public DigitalSign(String alg,String algRamdom,String prov) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen= KeyPairGenerator.getInstance(alg,prov);
        random = SecureRandom.getInstance(algRamdom,prov);
        keyGen.initialize(1024,random);
        keyPair = keyGen.genKeyPair();
        signature = Signature.getInstance(alg,prov);
    }
    public void genKey(){
        if(keyPair!=null){
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        }
    }
    public String sign(String mes) throws SignatureException, InvalidKeyException {
        byte[] data = mes.getBytes();
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }
    public String signFile(String src) throws InvalidKeyException, IOException, SignatureException {
        signature.initSign(privateKey);
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src))){
            byte[] buf = new byte[1024];
            int len;
            while((len = bis.read(buf))!= -1){
                signature.update(buf,0,len);
            }
        }
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }
    public boolean verify(String mes,String sign) throws InvalidKeyException, SignatureException {
        signature.initVerify(publicKey);
        byte[] data = mes.getBytes();
        byte[] signValue = Base64.getDecoder().decode(sign);
        signature.update(data);
        return signature.verify(signValue);
    }
    public boolean verifyFile(String src,String sign) throws InvalidKeyException, IOException, SignatureException {
        signature.initVerify(publicKey);
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src))){
            byte[] buf = new byte[1024];
            int len;
            while((len = bis.read(buf))!= -1){
                signature.update(buf,0,len);
            }
        }
        byte[] signValue = Base64.getDecoder().decode(sign);
        return signature.verify(signValue);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException, IOException {
        DigitalSign ds = new DigitalSign("DSA","SHA1PRNG","SUN");
        ds.genKey();
//        String sign = ds.sign("heloo");
//        System.out.println(sign);
//        boolean verify = ds.verify("heloo",sign);
//        System.out.println(verify);
        String src = "D:\\storage\\resource\\local-1.txt";
        String src2 = "D:\\storage\\resource\\local-2.txt";
        String sign = ds.signFile(src);
        System.out.println(sign);
        boolean verify = ds.verifyFile(src2,sign);
        System.out.println(verify);
    }

}
