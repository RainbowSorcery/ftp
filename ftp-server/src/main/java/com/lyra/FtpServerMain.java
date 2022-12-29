package com.lyra;

import com.lyra.config.BeanScanConfig;
import com.lyra.handle.CommandHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

@Component
public class FtpServerMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanScanConfig.class);
        CommandHandle bean = applicationContext.getBean(CommandHandle.class);

        System.out.println(bean);
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
                            }
                        }
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}