package com.lyra.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelUtils {
    public static void writeData(SocketChannel socketChannel, String responseBody) {
        byte[] responseBodyByte = responseBody.getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(responseBodyByte.length);
        buffer.put(responseBodyByte);
        buffer.flip();
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
