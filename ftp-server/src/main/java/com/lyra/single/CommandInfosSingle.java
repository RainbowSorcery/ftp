package com.lyra.single;

import com.lyra.entity.CommandInfo;
import com.lyra.entity.Connection;
import com.lyra.server.DataChannelThread;
import com.lyra.system.NativeFileSystem;
import com.lyra.utils.DateUtils;
import com.lyra.utils.SocketChannelUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandInfosSingle {
    private static Map<String, CommandInfo> commandInfoMap;

    static {
        commandInfoMap = new HashMap<>();
        commandInfoMap.put("USER", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (pram instanceof Connection) {
                    String responseBodyBytes = null;

                    if (value.equals("lyra") || value.equals("anonymous")) {
                        responseBodyBytes = "331 username is right, Please input password.\r\n";
                    } else {
                        responseBodyBytes = "530 username is not found.\r\n";
                    }
                    Connection connection = (Connection) pram;
                    connection.setUsername(value);
                    SocketChannelUtils.writeData(socketChannel, responseBodyBytes);
                } else {
                    throw new RuntimeException("参数对象类型错误");
                }
            }
        });
        commandInfoMap.put("SYST", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                String responseBody = "215 UNIX Type: L8\r\n";

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });
        commandInfoMap.put("FEAT", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }
                String responseBody = "500 no feature.\r\n";

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });
        commandInfoMap.put("OPTS", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }

                String responseBody = "200 no feature.\r\n";

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });

        commandInfoMap.put("PASS", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                String responseBody = "";
                Connection connection = (Connection) pram;
                connection.setPassword(value);
                if (connection.getUsername().equals("lyra") && connection.getPassword().equals("365373011")) {
                    responseBody = "230 login successful.\r\n";
                    connection.setAuth(true);
                } else {
                    responseBody = "530 User name password do not match.\r\n";
                }

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });

        commandInfoMap.put("PWD", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                Connection connection = (Connection) pram;
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }


                NativeFileSystem nativeFileSystem = connection.getNativeFileSystem();

                String currentPath = "";

                if (nativeFileSystem.getCurrentFile() == null) {
                    currentPath = nativeFileSystem.getRootPath();
                } else {
                    currentPath =  nativeFileSystem.getCurrentFilePath();
                }


                //先获取最后一个  \ 所在的位置
                int index1 = currentPath.lastIndexOf(nativeFileSystem.getRootPath());
                //然后获取从最后一个\所在索引+1开始 至 字符串末尾的字符
                String path = currentPath.substring(index1 + nativeFileSystem.getRootPath().length());


                String responseBody = "257 " + "\"" + path + "\\" + "\"" + "\r\n";

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });

        commandInfoMap.put("CWD", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }
                Connection connection = (Connection) pram;
                if (value.startsWith("\\")) {
                    connection.getNativeFileSystem().setCurrentFile(new File(connection.getNativeFileSystem().getRoot() + "/" + value));
                } else {
                    connection.getNativeFileSystem().setCurrentFile(new File(connection.getNativeFileSystem().getCurrentFile() + "/" + value));
                }


                String responseBody = "250 cwd " + value + " directory done.\r\n";

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });

        commandInfoMap.put("TYPE", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }

                String responseBody = "200 Set to complete, type is " + value + ".\r\n";

                Connection connection = (Connection) pram;
                if (value.equalsIgnoreCase("I")) {
                    connection.setAscii(false);
                } else if (value.equalsIgnoreCase("A")) {
                    connection.setAscii(true);
                } else {
                    responseBody = "500 TYPE not found.";
                }

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });

        commandInfoMap.put("PASV", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }
                Connection connection = (Connection) pram;

                // 阻塞 知道channel建立完毕 也就是Connect给i哦那中dataTransferSocket成员变量赋值完成
                // todo 使用线程池进行优化
                DataChannelThread dataChannelThread = new DataChannelThread(connection, socketChannel);
                new Thread(dataChannelThread).start();
            }
        });


        commandInfoMap.put("LIST", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }

                Connection connection = (Connection) pram;
                // 有两种情况 分别是传参和未传参，如果传参了则root节点路径加传参参数，如果未传参则 currentFile目录
                String path = connection.getNativeFileSystem().getCurrentFilePath();

                File file = new File(path);
                StringBuilder stringBuffer = new StringBuilder();
                if (file.listFiles() != null) {
                    for (File listFile : file.listFiles()) {
                        stringBuffer.append(DateUtils.format(new Date(listFile.lastModified()), "yyyy/MM/dd  hh:mm:ss"));

                        if (listFile.isDirectory()) {
                            stringBuffer.append("    <DIR>     ");
                        } else {
                            stringBuffer.append(" ").append(listFile.length()).append(" ");
                        }
                        stringBuffer.append(listFile.getName    ())
                                .append("\r\n");
                    }
                }


                while (true) {
                    SocketChannel dataTransferSocketChannel = connection.getDataTransferSocketChannel();
                    if (dataTransferSocketChannel != null) {
                        String responseBody = "";
                        // 请求发送完毕套接字得关闭
                        try {
                            SocketChannelUtils.writeData(dataTransferSocketChannel, stringBuffer.toString());
                            dataTransferSocketChannel.close();
                            connection.setDataTransferSocketChannel(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                }
                SocketChannelUtils.writeData(socketChannel, "250 The list was sent\r\n");
            }
        });

        commandInfoMap.put("CDUP", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }

                Connection connection = (Connection) pram;
                NativeFileSystem nativeFileSystem = connection.getNativeFileSystem();
                nativeFileSystem.setCurrentFile(nativeFileSystem.getCurrentFile().getParentFile());

                SocketChannelUtils.writeData(socketChannel, "200 cwd parent done.\r\n");
            }
        });

        commandInfoMap.put("STOR", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }

                Connection connection = (Connection) pram;
                SocketChannel dataTransferSocketChannel = connection.getDataTransferSocketChannel();


                ByteBuffer allocate = ByteBuffer.allocate(1024);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(connection.getNativeFileSystem().getCurrentFile() + "/" + value);

                    int size = dataTransferSocketChannel.read(allocate);
                    while (size > 0) {
                        size = dataTransferSocketChannel.read(allocate);
                    }
                    fileOutputStream.write(allocate.get());
                    dataTransferSocketChannel.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        commandInfoMap.put("RETR", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }

                Connection connection = (Connection) pram;

                BufferedInputStream bufferedInputStream = null;
                while (true) {
                    SocketChannel dataTransferSocketChannel = connection.getDataTransferSocketChannel();
                    try {
                        if (dataTransferSocketChannel != null) {
                            bufferedInputStream = new BufferedInputStream(new FileInputStream(connection.getNativeFileSystem().getCurrentFile() + "\\" + value));

                            byte[] bytes = bufferedInputStream.readAllBytes();

                            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                            buffer.put(bytes);
                            buffer.flip();
                            dataTransferSocketChannel.write(buffer);
                            SocketChannelUtils.writeData(socketChannel, "226 write successful.\r\n");

                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (dataTransferSocketChannel != null) {
                            try {
                                dataTransferSocketChannel.close();
                                connection.setDataTransferSocketChannel(null);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (bufferedInputStream != null) {
                            try {
                                bufferedInputStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                }
            }
        });

    }


    public static CommandInfo getCommandInfoMap(String command) {

        return commandInfoMap.get(command);
    }

    public static void responseAccessDenied(SocketChannel socketChannel) {
        String responseBody = "530 no p permission.\r\n";
        SocketChannelUtils.writeData(socketChannel, responseBody);
    }

    private static Boolean checkConnectionPermission(Object pram) {
        Connection connection = (Connection) pram;
        return connection.getAuth();
    }
}
