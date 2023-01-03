package com.lyra;

import com.lyra.server.FTPServer;
import com.lyra.system.NativeFileSystem;
import com.lyra.system.impl.NativeFileSystemImpl;

import java.io.File;

//@SpringBootApplication
public class FtpServerMain {
    public static void main(String[] args) {
        // 设置初始文件目录
        FTPServer ftpServer = new FTPServer();
        ftpServer.setFileSystemDir("d://");
        ftpServer.start(21);
    }
}