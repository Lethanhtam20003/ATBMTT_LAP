package org.example.Chat_TCP;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Server {
    private static final int PORT = 12345;
    private static List<PrintWriter> clientWriters = new ArrayList<>();
    private static SecretKey aesKey;

    public static void main(String[] args) {
        try {
            // Tạo khóa AES
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            aesKey = keyGen.generateKey();

            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server đang chạy...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client kết nối: " + socket);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                clientWriters.add(out);

                // Tạo luồng mới để xử lý tin nhắn từ client
                new Thread(() -> handleClient(socket, out)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, PrintWriter out) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            while ((message = in.readLine()) != null) {
                String decryptedMessage = decryptMessage(message);
                System.out.println("Nhận từ client: " + decryptedMessage);
                broadcast(encryptMessage("Server: " + decryptedMessage));
            }
        } catch (Exception e) {
            System.out.println("Client ngắt kết nối: " + socket);
        } finally {
            clientWriters.remove(out);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Gửi tin nhắn đã mã hóa tới tất cả các client
    private static void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
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
