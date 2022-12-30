package com.lyra.test;

import com.lyra.mapper.UserRepository;
import com.lyra.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {
    private static final Logger log = LoggerFactory.getLogger(UserMapperTest.class);

    @Autowired
    private UserService userService;


    @Test
    public void test() {
        log.info("userExits:{}", userService.checkUserIsExits("Lyra"));
    }
}
