package com.sergiomartinrubio.javasocketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * http://tutorials.jenkov.com/java-nio/scatter-gather.html
 */
public class SocketChannelServer {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel != null) {
                int read = socketChannel.read(buffer); // pos = n & lim = 1024

                while (read != -1) {

                    buffer.flip(); // set buffer in read mode - pos = 0 & lim = n

                    while(buffer.hasRemaining()){
                        System.out.print((char) buffer.get()); // read 1 byte at a time
                    }

                    buffer.clear(); // make buffer ready for writing - pos = 0 & lim = 1024
                    read = socketChannel.read(buffer); // set to -1
                }
            }
        }
    }
}
