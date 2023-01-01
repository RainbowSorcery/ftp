package com.lyra.entity;

import com.lyra.system.NativeFileSystem;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Connection {
    private String username;
    private String password;
    private Boolean isAuth;
    private NativeFileSystem nativeFileSystem;
    private ServerSocketChannel dataTransferServerSocketChannel;

    private SocketChannel dataTransferSocketChannel;

    public void connectionSuccessful(SocketChannel socketChannel) {
        byte[] responseBodyByte = "200 与服务端连接成功.\r\n".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(responseBodyByte.length);
        buffer.put(responseBodyByte);
        buffer.flip();
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public NativeFileSystem getNativeFileSystem() {
        return nativeFileSystem;
    }

    public void setNativeFileSystem(NativeFileSystem nativeFileSystem) {
        this.nativeFileSystem = nativeFileSystem;
    }


    public void setAscii(Boolean ascii) {
    }

    public SocketChannel getDataTransferSocketChannel() {
        return dataTransferSocketChannel;
    }

    public void setDataTransferSocketChannel(SocketChannel dataTransferSocketChannel) {
        this.dataTransferSocketChannel = dataTransferSocketChannel;
    }

    public ServerSocketChannel getDataTransferServerSocketChannel() {
        return dataTransferServerSocketChannel;
    }

    public void setDataTransferServerSocketChannel(ServerSocketChannel dataTransferServerSocketChannel) {
        this.dataTransferServerSocketChannel = dataTransferServerSocketChannel;
    }
}
