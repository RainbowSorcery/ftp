package com.lyra.strategy.command;

import java.nio.channels.SocketChannel;

public interface CommandStrategy {
    String commandHandle(String requestBody);
}
