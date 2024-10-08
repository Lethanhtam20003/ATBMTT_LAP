package org.example.MaHoaDoiXung.Lap2;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DES {
    SecretKey key;
    public SecretKey genKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        key = keyGen.generateKey();
        return key;
    }

    public void loadKey(SecretKey key){
    this.key = key;
    }
    public byte[] encrypt(String text) throws Exception{
        byte[] data = text.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        byte[] res = cipher.doFinal(data);

        return res;
    }

    public String decrypt(byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);

        return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
    }

    public String encryptBase64(String text) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(text));
    }

    public String decryptBase64(String tetx){
        return null;
    }

    public boolean encryptfile(String fileIn,String fileOut) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileIn));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileOut));

        CipherInputStream en = new CipherInputStream(inputStream,cipher);

        int i;
        byte[] read = new byte[1024];
        while((i = en.read(read))!= -1){
                outputStream.write(read, 0, i);
            }
        read = cipher.doFinal();
        if(read!=null){
            outputStream.write(read);
        }
        outputStream.flush();
        outputStream.close();
        en.close();


        return  true;

    }
    public boolean decryptfile(String fileIn,String fileOut) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileIn));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileOut));

        CipherOutputStream de = new CipherOutputStream(outputStream,cipher);

        int i;
        byte[] read = new byte[1024];
        while((i = inputStream.read(read))!= -1){
            de.write(read, 0, i);
        }

        read = cipher.doFinal();
        if(read!=null){
            de.write(read);
        }

        outputStream.flush();
        outputStream.close();
        de.close();

        return  true;
    }

    public static void main(String[] args) throws Exception {
        DES des = new DES();
        SecretKey key = des.genKey();
//        String plainText = "khos noi vai";
//        byte[] data = des.encrypt(plainText);
//
//        System.out.println(Base64.getEncoder().encodeToString(data));
//
//        String cipherText = des.decrypt(data);
//        System.out.println(cipherText);
        String fileIn= "";
        String fileOut = "";
    }
}
