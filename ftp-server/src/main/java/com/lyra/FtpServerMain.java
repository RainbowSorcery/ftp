package com.lyra;

import com.lyra.handle.CommandHandle;
import com.lyra.socker.FTPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class FtpServerMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FtpServerMain.class, args);
        FTPServer ftpServer = applicationContext.getBean(FTPServer.class);
        ftpServer.start();
    }
}