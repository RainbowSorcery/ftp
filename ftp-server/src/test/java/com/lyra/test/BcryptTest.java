package com.lyra.test;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BcryptTest {
    @Test
    public void test() {
        String password = BCrypt.hashpw("365373011", BCrypt.gensalt(12));
        System.out.println(password);
        boolean checkpw = BCrypt.checkpw("365373011", "$2a$10$FTdkhk5xY1F9loSezDvyXO6lfKjayfjm7aVt6WXqMM0mABchpCs/6");
        System.out.println(checkpw);
    }
}
