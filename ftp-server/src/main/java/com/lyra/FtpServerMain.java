package com.lyra;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class FtpServerMain {
    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.bind(new InetSocketAddress(20));
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int select = selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();

                    if (next.isValid()) {
                        if (next.isAcceptable()) {
                            System.out.println("接收到了连接");
                            ServerSocketChannel clientServerSocketChannel = (ServerSocketChannel) next.channel();
                            SocketChannel accept = clientServerSocketChannel.accept();
                            if (accept != null) {
                                accept.configureBlocking(false);
                                accept.register(selector, SelectionKey.OP_READ);

                                String response = "220 (lyraFTP v1.0.0)\r\n";
                                ByteBuffer buffer = ByteBuffer.allocate(response.getBytes().length);
                                buffer.put(response.getBytes());
                                buffer.flip();
                                accept.write(buffer);
                            }
                        }

                        if (next.isReadable()) {
                            SocketChannel clientSocketChannel = (SocketChannel) next.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            // 将读到的数据写到buffer中
                            int read = clientSocketChannel.read(buffer);
                            if (read > 0) {
                                buffer.flip();
                                byte[] bytes = new byte[buffer.remaining()];
                                buffer.get(bytes);
                                String requestBody = new String(bytes);
                                System.out.println("Message:" + requestBody);

                                if (requestBody.contains("SYST")) {
                                    String response = "200 (windows 10)\r\n";
                                    byte[] bytes1 = response.getBytes(StandardCharsets.UTF_8);
                                    // 创建buffer
                                    ByteBuffer allocate = ByteBuffer.allocate(bytes1.length);
                                    // 在buffer中写数据
                                    allocate.put(bytes1);
                                    // 反转buffer 由写模式切换为读模式
                                    allocate.flip();
                                    // 往客户端写入数据
                                    clientSocketChannel.write(allocate);
                                }
                                if (requestBody.contains("AUTH TLS")) {
                                    String response = "332 (lyraFTP v1.0.0)\r\n";
                                    byte[] bytes1 = response.getBytes(StandardCharsets.UTF_8);
                                    // 创建buffer
                                    ByteBuffer allocate = ByteBuffer.allocate(bytes1.length);
                                    // 在buffer中写数据
                                    allocate.put(bytes1);
                                    // 反转buffer 由写模式切换为读模式
                                    allocate.flip();
                                    // 往客户端写入数据
                                    clientSocketChannel.write(allocate);
                                }

                                if (requestBody.contains("lyra")) {
                                    String response = "331 (lyraFTP v1.0.0)\r\n";
                                    byte[] bytes1 = response.getBytes(StandardCharsets.UTF_8);
                                    // 创建buffer
                                    ByteBuffer allocate = ByteBuffer.allocate(bytes1.length);
                                    // 在buffer中写数据
                                    allocate.put(bytes1);
                                    // 反转buffer 由写模式切换为读模式
                                    allocate.flip();
                                    // 往客户端写入数据
                                    clientSocketChannel.write(allocate);
                                }

                                if (requestBody.contains("365373011")) {
                                    String response = "230 (lyraFTP v1.0.0)\r\n";
                                    byte[] bytes1 = response.getBytes(StandardCharsets.UTF_8);
                                    // 创建buffer
                                    ByteBuffer allocate = ByteBuffer.allocate(bytes1.length);
                                    // 在buffer中写数据
                                    allocate.put(bytes1);
                                    // 反转buffer 由写模式切换为读模式
                                    allocate.flip();
                                    // 往客户端写入数据
                                    clientSocketChannel.write(allocate);
                                }

                                if (requestBody.contains("PWD")) {
                                    String response = "257 \"/\" is current directory.\r\n";
                                    byte[] bytes1 = response.getBytes(StandardCharsets.UTF_8);
                                    // 创建buffer
                                    ByteBuffer allocate = ByteBuffer.allocate(bytes1.length);
                                    // 在buffer中写数据
                                    allocate.put(bytes1);
                                    // 反转buffer 由写模式切换为读模式
                                    allocate.flip();
                                    // 往客户端写入数据
                                    clientSocketChannel.write(allocate);
                                }

                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}