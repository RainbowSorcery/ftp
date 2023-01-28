package com.lyra;

import com.lyra.server.FTPServer;

//@SpringBootApplication
public class FtpServerMain {
    public static void main(String[] args) {
        // 设置初始文件目录
        FTPServer ftpServer = new FTPServer();
        ftpServer.setFileSystemDir("c://");
        ftpServer.start(21);
    }
}