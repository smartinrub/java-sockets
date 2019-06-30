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

    private static int counter = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

        ExecutorService threadPool = Executors.newFixedThreadPool(40);


        while (true) {

            Socket client = serverSocket.accept();
            threadPool.execute(() -> {
                System.out.println("Socket: " + counter++ + " on thread: " + Thread.currentThread().getId());
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    OutputStream out = client.getOutputStream();
                    in.lines().forEach(line -> {
                        try {
                            out.write(("Echo: " + line + " - on THREAD " + Thread.currentThread().getId() +"\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
