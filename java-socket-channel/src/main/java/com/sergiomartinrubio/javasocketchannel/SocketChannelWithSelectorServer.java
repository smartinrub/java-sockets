package com.sergiomartinrubio.javasocketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SocketChannelWithSelectorServer {

    public static void main(String[] args) throws IOException {
        Map<SocketChannel, Queue<ByteBuffer>> dataMap = new HashMap<>();;

        var selector = Selector.open();

        // Create Server Socket Channel
        var serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));

        // Register channel to selector to accept requests
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // selector pointing to ACCEPT operation

        while (true) {
            selector.select();
            var keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                var selectionKey = (SelectionKey) keys.next();

                if (selectionKey.isAcceptable()) {
                    var socketChannel = serverSocketChannel.accept();
                    System.out.println("Accepted connection from " + socketChannel);
                    socketChannel.configureBlocking(false);

                    socketChannel.write(ByteBuffer.wrap(("Welcome: " + socketChannel.getRemoteAddress() +
                            "\nThe thread assigned to you is: " + Thread.currentThread().getId() + "\n").getBytes()));

                    dataMap.put(socketChannel, new LinkedList<>()); // store socket connection
                    System.out.println("Total clients connected: " + dataMap.size());
                    socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ); // selector pointing to READ operation
                } else if (selectionKey.isReadable()) {
                    System.out.println("Reading...");
                    var socketChannel = (SocketChannel) selectionKey.channel();
                    var byteBuffer = ByteBuffer.allocate(1024);
                    int read = socketChannel.read(byteBuffer);
                    if (read == -1) {
                        dataMap.remove(socketChannel);
                        var socket = socketChannel.socket();
                        var remoteSocketAddress = socket.getRemoteSocketAddress();
                        System.out.println("Connection closed by client: " + remoteSocketAddress);
                        socketChannel.close();
                        selectionKey.cancel();
                    }
                    byteBuffer.flip();
                    dataMap.get(socketChannel).add(byteBuffer); // find socket channel to retrieve pending data if any
                    selectionKey.interestOps(SelectionKey.OP_WRITE); // set mode to WRITE to send data
                } else if (selectionKey.isWritable()) {
                    System.out.println("Writing...");
                    var socketChannel = (SocketChannel) selectionKey.channel();
                    var pendingData = dataMap.get(socketChannel);
                    while (!pendingData.isEmpty()) {
                        var buf = pendingData.poll();
                        socketChannel.write(buf);
                    }
                    selectionKey.interestOps(SelectionKey.OP_READ); // change the key to READ
                }
                keys.remove();
            }
        }
    }
}
