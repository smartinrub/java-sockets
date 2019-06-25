package com.sergiomartinrubio.rawjavasocket;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketTestClient {


    public static void main(String[] args) throws IOException {

        Socket[] sockets = new Socket[1];
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        for (int i = 0; i < sockets.length; i++) {
            sockets[i] = new Socket("localhost", 8082);

            int counter = i;
            executorService.execute(() -> {
                try {
                    DataOutputStream out = new DataOutputStream(sockets[counter].getOutputStream());
                    DataInputStream in = new DataInputStream(sockets[counter].getInputStream());
                    out.writeUTF(Integer.toString(counter));
                    out.flush();
                    System.out.println(in.readUTF());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
