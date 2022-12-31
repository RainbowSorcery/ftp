package com.lyra.server;

import com.lyra.entity.CommandInfo;
import com.lyra.entity.Connection;
import com.lyra.single.CommandInfosSingle;
import com.lyra.system.NativeFileSystem;
import com.lyra.system.impl.NativeFileSystemImpl;
import com.lyra.utils.CommandUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FTPServer {
    private Map<String, Connection> connectionMap = new HashMap<>();

    public void start(Integer port) {
        if (port == null) {
            throw new RuntimeException("端口号为空");
        }

        Selector serverSelector = null;
        ServerSocketChannel serverSocketChannel = null;

        try {
            serverSelector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

            selectorParse(serverSelector);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (serverSelector != null) {
                try {
                    serverSelector.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void selectorParse(Selector selector) {
        while (true) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey currentSelect = keyIterator.next();

                    if (currentSelect.isValid()) {
                        if (currentSelect.isAcceptable()) {
                            ServerSocketChannel clientServerSocketChannel = (ServerSocketChannel) currentSelect.channel();
                            System.out.println("服务端连接到客户端，服务端IP:" + clientServerSocketChannel.getLocalAddress());

                            SocketChannel accept = clientServerSocketChannel.accept();
                            Connection connection = new Connection();
                            // 设置初始文件目录
                            NativeFileSystem nativeFileSystem = new NativeFileSystemImpl(new File("c://"));
                            connection.setNativeFileSystem(nativeFileSystem);
                            connection.setAuth(false);

                            connectionMap.put(accept.getRemoteAddress().toString(), connection);
                            connection.connectionSuccessful(accept);

                            accept.configureBlocking(false);
                            accept.register(selector, SelectionKey.OP_READ);

                        }

                        if (currentSelect.isReadable()) {
                            SocketChannel channel = (SocketChannel) currentSelect.channel();

                            ByteBuffer allocate = ByteBuffer.allocate(1024);
                            int read = channel.read(allocate);
                            if (read > 0) {
                                allocate.flip();
                                byte[] bytes = new byte[allocate.remaining()];

                                allocate.get(bytes);


                                Connection connection = connectionMap.get(channel.getRemoteAddress().toString());

                                String readLine = new String(bytes);

                                Map<String, String> commandProcessMap = CommandUtils.processCommand(readLine);
                                String key = commandProcessMap.get("key");
                                String value = commandProcessMap.get("value");

                                CommandInfo commandInfo = CommandInfosSingle.getCommandInfoMap(key);
                                if (commandInfo != null) {
                                    commandInfo.run(channel, value, connection);
                                }
                            }

                        }
                    }
                    keyIterator.remove();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}