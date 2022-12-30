package com.lyra.strategy.command;

import com.lyra.handle.CommandHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.SocketChannel;

@Component
public class WelcomeCommandStrategy implements CommandStrategy {
    @Autowired
    private CommandHandle commandHandle;

    @Override
    public String commandHandle(String requestBody) {
           return commandHandle.welcome();
    }
}
