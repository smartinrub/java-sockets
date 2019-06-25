package com.sergiomartinrubio.rawjavasocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketTestClient {

    private static int counter = 0;

    public static void main(String[] args) throws IOException {
        Socket client = new Socket("localhost", 8081);

        ExecutorService threadPool = Executors.newFixedThreadPool(3000);

        threadPool.execute(() -> {
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println(counter++);
                System.out.println(in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }
}
