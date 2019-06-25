package com.sergiomartinrubio.rawjavasocket;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketTestClient {


    public static void main(String[] args) throws IOException {

        Socket[] sockets = new Socket[10000];
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        for (int i = 0; i < sockets.length; i++) {
            sockets[i] = new Socket("localhost", 8081);
            sockets[i].setKeepAlive(true);
            int counter = i;
            System.out.println(i);
            executorService.execute(() -> {
                try {
                    sockets[counter].getOutputStream().write(counter);
                    System.out.println(sockets[counter].getInputStream().read());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
