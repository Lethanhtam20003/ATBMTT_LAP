package org.example.RSA;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class Lap {

    public void GenRSAKey(String publickeyFile,String privateKeyFile) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        writeFile(publickeyFile,publicKey.getEncoded());
        writeFile(privateKeyFile,privateKey.getEncoded());

//        try(FileOutputStream fos = new FileOutputStream(publickeyFile)){
//            fos.write(publicKey.getEncoded());
//        }
//        try(FileOutputStream fos = new FileOutputStream(privateKeyFile)){
//            fos.write(privateKey.getEncoded());
//        }
    }
    public SecretKey GenSecretKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        return generator.generateKey();
    }
    public IvParameterSpec GenIV(){
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    private byte[] readFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        }
    }
    private void writeFile(String path,byte[] data){
        try(FileOutputStream fos = new FileOutputStream(path)){
            fos.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private byte[] encryptRSA(byte[] data,PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    private byte[] decryptRSA(byte[] data,PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }
    public void encryteAESKey(SecretKey key, String keyFile,IvParameterSpec iv,String ivFile,String publicKeyFile) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyFile);
    }

    private PublicKey getPublicKey(String publicKeyFile) throws IOException {
        PublicKey publicKey = null;
        byte[] res = readFile(publicKeyFile);
        return publicKey;
    }

    public boolean encryptFile(PublicKey pKey,SecretKey sKey,String pathIn,String pathOut) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sKey);

        byte[] encryptKey = encryptRSA(sKey.getEncoded(),pKey);
        try(
            FileInputStream fis = new FileInputStream(pathIn);
            FileOutputStream fos = new FileOutputStream(pathOut);
            DataOutputStream dos = new DataOutputStream(fos)
        ){
            dos.writeUTF(encryptKey.toString());
            byte[] buffer = new byte[1024];
            int byteRead;
            while((byteRead = fis.read(buffer))>0){
                byte[] output = cipher.update(buffer,0,byteRead);
                if(output!=null){
                    dos.write(output);
                }
            }
            byte[] output = cipher.doFinal();
            if(output!=null){
                dos.write(output);
            }
        }
        return true;
    }

    public boolean decryptFile(PrivateKey key,String pathIn,String pathOut) throws Exception {

        return false;
    }

}
