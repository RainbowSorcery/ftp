package com.lyra.socker;

import com.lyra.handle.CommandHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Component
public class FTPServer {
    private static final Logger log = LoggerFactory.getLogger(FTPServer.class);

    @Autowired
    private CommandHandle commandHandle;

    public void start() {
        Selector serverSelector = null;
        ServerSocketChannel serverSocketChannel = null;
        try {
             serverSelector = Selector.open();
             serverSocketChannel = ServerSocketChannel.open();
             serverSocketChannel.configureBlocking(false);
             serverSocketChannel.socket().bind(new InetSocketAddress(20));
             serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

             while (true) {
                 int select = serverSelector.select(1000);
                 Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                 Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

                 if (selectionKeyIterator.hasNext()) {
                     SelectionKey currentSelectKey = selectionKeyIterator.next();
                     if (currentSelectKey.isAcceptable()) {
                         ServerSocketChannel clientSocketChannel = (ServerSocketChannel) currentSelectKey.channel();
                         SocketChannel accept = clientSocketChannel.accept();
                         accept.configureBlocking(false);
                         accept.register(serverSelector, SelectionKey.OP_READ);

                         log.info("有新连接，客户端ip:{}", accept.getRemoteAddress());
                         log.info("开始向客户端响应....");
                         commandHandle.welcome(accept);
                     }

                     if (currentSelectKey.isReadable()) {
                         SocketChannel socketChannel = (SocketChannel) currentSelectKey.channel();
                         ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                         int read = socketChannel.read(byteBuffer);
                         if (read > 0) {
                             byteBuffer.flip();

                             byte[] bytes = new byte[byteBuffer.remaining()];

                             byteBuffer.get(bytes);

                             log.info("客户端请求内容:{}", new String(bytes));
                         }


                     }

                     selectionKeyIterator.remove();
                 }
             }

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
}