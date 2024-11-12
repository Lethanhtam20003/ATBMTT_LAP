package org.example.lap4;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//MP5
public class MD5 {

    public String hash(String data) throws NoSuchAlgorithmException {
//        khoi tao MessageDigiest voi thuat toan MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
//        chuyen chuoi dua vao thang mang byte
        byte[] bytes = data.getBytes();
//        bam
        byte[] digest= md.digest(bytes);
        BigInteger number = new BigInteger(1, digest);
//        chuyen ket qua sang hexa
        return number.toString(16);
    }

    public String hashFile(String src) throws Exception {
//        khoi tao MessageDigiest voi thuat toan MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
//        Kiem tra co ton tai file khong
        File file = new File(src);
        if(!file.exists()) return null;
//        doc file va tinh tan mang bam
        DigestInputStream di = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), md);
        byte[] buff = new byte[1024];
        int read;
        do{
            read = di.read(buff);
        }while (read != -1);
//        nhan kiet qua sau khi bam
        byte[] digest = di.getMessageDigest().digest();
        BigInteger number =  new BigInteger(1, digest);
//        chuyen ket qua sang chuoi hexa
        return number.toString(16);
    }
    public static void main(String[] args) throws Exception {
        MD5 hash = new MD5();
        System.out.println(hash.hash("heloo").equals("948fab37aeaa563cfa7f009d836ead8d"));
        System.out.println(hash.hashFile("D:\\storage\\taiLieu\\logo trường\\logo.png").equals("a32f183353abc91feeb9cdd95bf5bd48"));

    }

}
