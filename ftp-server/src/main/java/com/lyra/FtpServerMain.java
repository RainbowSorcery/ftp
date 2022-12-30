package com.lyra;

import com.lyra.socket.FTPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FtpServerMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FtpServerMain.class, args);
        FTPServer ftpServer = applicationContext.getBean(FTPServer.class);
        ftpServer.start();
    }
}