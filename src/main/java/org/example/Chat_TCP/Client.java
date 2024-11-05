package org.example.Chat_TCP;

import java.net.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private static SecretKey aesKey;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Kết nối đến server...");

            // Lấy khóa AES từ Server (ở đây giả định khóa đã biết trước)
            aesKey = new SecretKeySpec("1234567890123456".getBytes(), "AES"); // Demo

            // Thread đọc tin nhắn từ server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Server: " + decryptMessage(message));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Gửi tin nhắn lên server
            String message;
            while ((message = consoleInput.readLine()) != null) {
                out.println(encryptMessage(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encryptMessage(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static String decryptMessage(String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decoded = Base64.getDecoder().decode(encryptedMessage);
        return new String(cipher.doFinal(decoded));
    }
}
