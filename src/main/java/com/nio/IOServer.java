package com.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {

    public static void main(String[] args) {
        ServerSocket server = null;
        InputStream input = null;

        try {
            server = new ServerSocket(8080);
            int recvMsgSize = 0;
            byte[] bytes = new byte[1024];
            System.out.println("start server in port " + server.getLocalPort());
            Socket clientSocket = server.accept();
            System.out.println("Handler clinet is " + clientSocket.getRemoteSocketAddress());

            while (true) {
                input = clientSocket.getInputStream();

                if ((recvMsgSize = input.read(bytes)) != -1) {
                    byte[] temp = new byte[recvMsgSize];
                    System.arraycopy(bytes, 0, temp, 0, recvMsgSize);
                    System.out.println(new String(temp));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
