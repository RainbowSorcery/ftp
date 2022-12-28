package com.lyra;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Main {
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
                        if(next.isAcceptable()) {
                            System.out.println("接收到了连接");
                            ServerSocketChannel clientServerSocketChannel = (ServerSocketChannel) next.channel();
                            SocketChannel accept = clientServerSocketChannel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector, SelectionKey.OP_READ);

                            String response = "220 (vsFTPd 2.2.2)";
                            ByteBuffer buffer = ByteBuffer.allocate(response.getBytes().length);
                            buffer.put(response.getBytes());
                            buffer.flip();
                            accept.write(buffer);
                        }

                        if (next.isReadable()) {
                            SocketChannel clientSocketChannel = (SocketChannel) next.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int read = clientSocketChannel.read(buffer);

                            if (read > 0) {
                                byte[] bytes = new byte[buffer.remaining()];
                                buffer.get(bytes);
                                System.out.println("Message:" + new String(bytes));
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