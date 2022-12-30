package com.lyra.handle;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Component
public class CommandHandle {
    public String welcome() {
        return "220 welcome to LyraFTP Server\r\n";
    }
}
