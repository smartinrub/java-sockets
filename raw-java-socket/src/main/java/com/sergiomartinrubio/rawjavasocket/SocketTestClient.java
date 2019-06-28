package com.sergiomartinrubio.rawjavasocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketTestClient {

    public static void main(String[] args) throws IOException {

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        Socket[] sockets = new Socket[1];

        for (int i = 0; i < sockets.length; i++) {

            int index = i;
            threadPool.execute(() -> {
                try {
                    sockets[index] = new Socket("localhost", 8080);
                    PrintWriter out = new PrintWriter(sockets[index].getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(sockets[index].getInputStream()));
                    String fromServer;
                    out.println("hello world");
                    while ((fromServer = in.readLine()) != null) {
                        System.out.println(fromServer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
