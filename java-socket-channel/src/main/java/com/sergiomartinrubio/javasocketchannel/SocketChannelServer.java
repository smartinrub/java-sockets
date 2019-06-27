package com.sergiomartinrubio.javasocketchannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * http://tutorials.jenkov.com/java-nio/scatter-gather.html
 */
public class SocketChannelServer {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("nio-data.txt");
        FileChannel inChannel = FileChannel.open(path);

//create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(1024);

        int bytesRead = inChannel.read(buf); //read into buffer.
        while (bytesRead != -1) {

            buf.flip();  //make buffer ready for read

            while(buf.hasRemaining()){
                System.out.print((char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
        }
        inChannel.close();
    }
}
