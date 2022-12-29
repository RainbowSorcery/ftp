package com.lyra;

import com.lyra.config.BeanScanConfig;
import com.lyra.socker.FTPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FtpServerMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanScanConfig.class);
        FTPServer bean = applicationContext.getBean(FTPServer.class);
        bean.start();
    }
}