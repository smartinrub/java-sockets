package com.sergiomartinrubio.rawjavasocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RawSocket {

    private static final int PORT_NUMBER = 8081;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

        while (true) {
            Socket server = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            OutputStream out = server.getOutputStream();
            in.lines().forEach(line -> {
                try {
                    out.write(("Echo: " + line.toUpperCase() + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            server.close();
        }
    }
}
