package com.sergiomartinrubio.rawjavasocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RawSocket {

    private static final int PORT_NUMBER = 8082;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

        ExecutorService threadPool = Executors.newFixedThreadPool(3);


        while (true) {

            Socket server = serverSocket.accept();
            threadPool.execute(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    OutputStream out = server.getOutputStream();
                    in.lines().forEach(line -> {
                        try {
                            System.out.println(line);
                            out.write(("Echo: " + line + " - on THREAD " + Thread.currentThread().getId() +"\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
