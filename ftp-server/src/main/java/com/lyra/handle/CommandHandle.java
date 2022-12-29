package com.lyra.handle;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Component
public class CommandHandle {
    public void welcome(SocketChannel socketChannel) throws IOException {
        String responseBody = "220 (lyraFTP v1.0.0)\r\n";
        ByteBuffer buffer = ByteBuffer.allocate(responseBody.getBytes().length);
        buffer.put(responseBody.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    }
}
