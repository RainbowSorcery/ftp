package com.lyra.strategy.command;

import com.lyra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckUsernameStrategy implements CommandStrategy {
    @Autowired
    private UserService userService;

    @Override
    public String commandHandle(String requestBody) {
        String username = requestBody.split(" ")[1].replaceAll("\r\n", "");
        if (userService.checkUserIsExits(username)) {
            return "331 user is found, success.\r\n";
        } else {
            return "530 username is not found.\r\n";
        }
    }
}
