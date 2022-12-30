package com.lyra.socket;

import com.lyra.handle.CommandHandle;
import com.lyra.strategy.command.CommandStrategy;
import com.lyra.utils.RequestParsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component
public class FTPServer {
    private static final Logger log = LoggerFactory.getLogger(FTPServer.class);

    @Autowired
    private CommandHandle commandHandle;

    @Autowired
    private RequestParsUtils requestParsUtils;

    @Autowired
    @Qualifier("commandHandleContext")
    private Map<String, CommandStrategy> commandStrategyContext;

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

                        log.info("客户端连接成功，客户端ip:{}", accept.getRemoteAddress());
                        log.info("开始向客户端响应....");
                        String responseBody = commandStrategyContext.get("welcome").commandHandle(null);
                        writeContent(responseBody, accept);
                    }

                    if (currentSelectKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) currentSelectKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                        int read = socketChannel.read(byteBuffer);
                        if (read > 0) {
                            byteBuffer.flip();

                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);
                            String requestParm = new String(bytes);
                            log.info("客户端请求内容:{}", requestParm);
                            CommandStrategy commandStrategy = commandStrategyContext.get(requestParm.split(" ")[0]);
                            if (commandStrategy != null) {
                                String responseBody = commandStrategy.commandHandle(requestParm);
                                writeContent(responseBody, socketChannel);
                            }
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

    private void writeContent(String responseBody, SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(responseBody.getBytes().length);
        buffer.put(responseBody.getBytes());
        buffer.flip();
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
