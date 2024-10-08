package org.example.lap2;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DES {
    private SecretKey key;

    public SecretKey generateKey() throws NoSuchAlgorithmException {
       KeyGenerator genKey =  KeyGenerator.getInstance("DES");
       genKey.init(56);
       key = genKey.generateKey();
        return key;
    }
    public void loadKey(SecretKey key){
        this.key = key;
    }
    public byte[] encrypt(String plainText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        byte[] data = plainText.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE,key);

        return  cipher.doFinal(data);
    }
    public String decrypt(byte[] cipherText) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE,key);

        return new String(cipher.doFinal(cipherText),StandardCharsets.UTF_8);
    }
    public byte[] encryptBase64(String plainText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        return Base64.getEncoder().encode(encrypt(plainText));
    }
    public String decryptBase64(byte[] cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        return decrypt(Base64.getDecoder().decode(cipherText));
    }
    public boolean encryptFile(String fileIn,String fileOut) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE,key);

        BufferedInputStream ins = new BufferedInputStream(new FileInputStream(fileIn));
        BufferedOutputStream ous = new BufferedOutputStream(new FileOutputStream(fileOut));

        CipherInputStream en = new CipherInputStream(ins,cipher);

        int i;
        byte[] read = new byte[1024];
        while((i = en.read(read))!= -1){
            ous.write(read,0,i);
        }
        read = cipher.doFinal();
        if(read != null){
            ous.write(read);
        }
        ous.flush();
        ous.close();
        ins.close();
        en.close();
        return true;
    }
    public boolean decryptFile(String fileIn,String fileOut) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE,key);

        BufferedInputStream ins = new BufferedInputStream(new FileInputStream(fileIn));
        BufferedOutputStream ous = new BufferedOutputStream(new FileOutputStream(fileOut));

        CipherOutputStream de = new CipherOutputStream(ous,cipher);

        int i;
        byte[] read = new byte[1024];
        while((i = ins.read(read))!= -1){
            de.write(read,0,i);
        }
        read = cipher.doFinal();
        if(read != null){
            de.write(read);
        }
        ous.flush();
        ous.close();
        ins.close();
        de.close();
        return true;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, IOException {
        DES des = new DES();
        SecretKey key = des.generateKey();
        des.loadKey(key);

        String plainText = "hello world";
        byte[] cipherText = des.encrypt(plainText);
        System.out.println(Base64.getEncoder().encodeToString(cipherText));
        System.out.println(new String(des.decrypt(cipherText)));

        byte[] cipherTextBase64 = des.encryptBase64(plainText);
        System.out.println(Base64.getEncoder().encodeToString(cipherTextBase64));
        System.out.println(new String(des.decryptBase64(cipherTextBase64)));

        String fileIn = "D:\\storage\\taiLieu/1.png";
        String fileOut = "D:\\storage\\taiLieu/2.png";
        String fileDe = "D:\\storage\\taiLieu/3.png";
        des.encryptFile(fileIn,fileOut);
        des.decryptFile(fileOut,fileDe);

    }
}
