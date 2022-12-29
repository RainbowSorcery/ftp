package com.lyra;

import com.lyra.config.BeanScanConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FtpServerMain {
    private static final Logger log = LoggerFactory.getLogger(FtpServerMain.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanScanConfig.class);
        log.info("applicationContext:{}", applicationContext);
    }
}