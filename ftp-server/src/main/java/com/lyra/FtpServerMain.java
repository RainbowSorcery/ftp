package com.lyra;

import com.lyra.server.FTPServer;

//@SpringBootApplication
public class FtpServerMain {
    public static void main(String[] args) {
        FTPServer ftpServer = new FTPServer();
        ftpServer.start(21);
    }
}