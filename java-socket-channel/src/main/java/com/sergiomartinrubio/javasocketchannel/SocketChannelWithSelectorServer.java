package com.sergiomartinrubio.javasocketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class SocketChannelWithSelectorServer {

    public static void main(String[] args) throws IOException {
        Map<SocketChannel, ByteBuffer> dataMap = new HashMap<>();;

        var selector = Selector.open();

        // Create Server Socket Channel
        var serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));

        // Register channel to selector to accept requests
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            var keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                var selectionKey = (SelectionKey) keys.next();

                if (selectionKey.isAcceptable()) {
                    var buffer = ByteBuffer.allocate(10000);
                    var socketChannel = serverSocketChannel.accept();
                    System.out.println("Accepted connection from " + socketChannel);
                    socketChannel.configureBlocking(false);

                    socketChannel.write(ByteBuffer.wrap("You are hitting Socket Channel Echo Server!\n".getBytes()));

                    dataMap.put(socketChannel, buffer);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    System.out.println("Reading...");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer pendingData = dataMap.get(socketChannel); // find socket channel to retrieve pending data if any
                    int read = socketChannel.read(pendingData);

                    if (read == -1) {
                        dataMap.remove(socketChannel);
                        Socket socket = socketChannel.socket();
                        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                        System.out.println("Connection closed by client: " + remoteAddr);
                        socketChannel.close();
                        selectionKey.cancel();
                    }

                    pendingData.flip();
                    dataMap.put(socketChannel, pendingData);
                    socketChannel.register(selector, SelectionKey.OP_WRITE); // set mode to WRITE to send data
                } else if (selectionKey.isWritable()) {
                    System.out.println("Writing...");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer pendingData = dataMap.get(socketChannel);
                    while (pendingData.hasRemaining()) {
                        socketChannel.write(pendingData); // sends all data at once
                    }
                    pendingData.clear();
                    socketChannel.read(pendingData);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                keys.remove();
            }
        }
    }
}
