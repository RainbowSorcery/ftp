package com.lyra.config;

import com.lyra.strategy.command.CheckUsernameStrategy;
import com.lyra.strategy.command.CommandStrategy;
import com.lyra.strategy.command.WelcomeCommandStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommandHandleContextConfig {
    @Autowired
    private WelcomeCommandStrategy welcomeCommandStrategy;

    @Autowired
    private CheckUsernameStrategy usernameStrategy;

    @Bean
    public Map<String, CommandStrategy> commandHandleContext() {
        Map<String, CommandStrategy> cacheMap = new HashMap<>();
        cacheMap.put("welcome", welcomeCommandStrategy);
        cacheMap.put("USER", usernameStrategy);

        return cacheMap;
    }
}
