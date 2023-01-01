package com.lyra.server;

import com.lyra.entity.Connection;
import com.lyra.utils.SocketChannelUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class DataChannelThread implements Runnable {
    private final Connection connection;
    private final SocketChannel socketChannel;

    public DataChannelThread(Connection connection, SocketChannel socketChannel) {
        this.connection = connection;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        // 异步操作 应该新建个线程执行
        Selector dataChannelSelector = null;
        ServerSocketChannel dataChannelServerSocket;

        try {
            dataChannelSelector = Selector.open();
            dataChannelServerSocket = ServerSocketChannel.open();
            dataChannelServerSocket.configureBlocking(false);
            dataChannelServerSocket.bind(new InetSocketAddress(0));
            dataChannelServerSocket.register(dataChannelSelector, SelectionKey.OP_ACCEPT);
            connection.setDataTransferServerSocketChannel(dataChannelServerSocket);
            ServerSocketChannel dataTransferServerSocketChannel = connection.getDataTransferServerSocketChannel();
            // 响应
            if (dataTransferServerSocketChannel != null) {
                try {
                    InetSocketAddress localAddress = (InetSocketAddress) dataTransferServerSocketChannel.getLocalAddress();
                    int port = localAddress.getPort();
                    String address = InetAddress.getLocalHost().getHostAddress();
                    StringBuilder h = new StringBuilder();
                    for (String s : address.split("\\.")) {
                        h.append(s).append(",");
                    }
                    int highPort = port >> 8;
                    // 左移取反获取低8为
                    int lowPort = port ^ (highPort << 8);

                    String responseBody = "227 Enabled Passive Mode(" + h + highPort + "," + lowPort + ").\r\n";
                    SocketChannelUtils.writeData(socketChannel, responseBody);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            while (true) {
                dataChannelSelector.select(1000);
                Iterator<SelectionKey> iterator = dataChannelSelector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey currentSelector = iterator.next();
                    if (currentSelector.isValid()) {
                        if (currentSelector.isAcceptable()) {
                            ServerSocketChannel clientChannel = null;
                            try {
                                clientChannel = (ServerSocketChannel) currentSelector.channel();
                                SocketChannel accept = clientChannel.accept();
                                accept.configureBlocking(false);
                                connection.setDataTransferSocketChannel(accept);
                                System.out.println("已建立数据传输链接,ip:" + accept.getLocalAddress().toString());
                                accept.configureBlocking(false);
                                accept.register(dataChannelSelector, SelectionKey.OP_READ);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    iterator.remove();
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (dataChannelSelector != null) {
                try {
                    dataChannelSelector.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
