package org.example.demo;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int PORT = 12345;
    public static void main(String[] args) {
        try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                System.out.println("Waiting for connection...");
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
