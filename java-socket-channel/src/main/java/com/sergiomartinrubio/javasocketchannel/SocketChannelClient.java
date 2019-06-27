package com.sergiomartinrubio.javasocketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8080));


        String data = "Hello World\n";

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(data.getBytes());

        buffer.flip();

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
