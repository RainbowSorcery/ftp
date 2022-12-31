package com.lyra.single;

import com.lyra.entity.CommandInfo;
import com.lyra.entity.Connection;
import com.lyra.utils.SocketChannelUtils;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

public class CommandInfosSingle {
    private static Map<String, CommandInfo> commandInfoMap;

    static {
        commandInfoMap = new HashMap<>();
        commandInfoMap.put("USER", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (pram instanceof Connection) {
                    String responseBodyBytes = null;

                    if (value.equals("lyra")) {
                        responseBodyBytes = "331 用户名正确，需要密码\r\n";
                    } else {
                        responseBodyBytes = "530 用户名不存在\r\n";
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
                String responseBody = "215 UNIX Type: L8\r\t";

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });
        commandInfoMap.put("FEAT", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }
                String responseBody = "500 no feature.\r\t";

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });
        commandInfoMap.put("OPTS", new CommandInfo() {
            @Override
            public void run(SocketChannel socketChannel, String value, Object pram) {
                if (!checkConnectionPermission(pram)) {
                    responseAccessDenied(socketChannel);
                }

                String responseBody = "500 no feature.\r\t";

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
                    responseBody = "230 登录成功\r\t";
                    connection.setAuth(true);
                } else {
                    responseBody = "530 用户名与密码不匹配\r\t";
                }

                SocketChannelUtils.writeData(socketChannel, responseBody);
            }
        });
    }

    public static CommandInfo getCommandInfoMap(String command) {

        return commandInfoMap.get(command);
    }

    public static void responseAccessDenied(SocketChannel socketChannel) {
        String responseBody = "530 无权限\r\t";
        SocketChannelUtils.writeData(socketChannel, responseBody);
    }

    private static Boolean checkConnectionPermission(Object pram) {
        Connection connection = (Connection) pram;
        return connection.getAuth();
    }
}
