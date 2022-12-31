package com.lyra.entity;

import java.nio.channels.SocketChannel;

public abstract class CommandInfo {



    public abstract void run(SocketChannel socketChannel, String value, Object pram);
}
