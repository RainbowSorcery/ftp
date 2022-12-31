package com.lyra.utils;

import java.util.HashMap;
import java.util.Map;

public class CommandUtils {
    public static Map<String, String> processCommand(String command) {
        Map<String, String> resultMap = new HashMap<>();

        command = command.replaceAll("\r\n", "");

        String[] s = command.split(" ");
        if (s.length == 2) {
            resultMap.put("key", s[0]);
            resultMap.put("value", s[1]);
        } else {
            resultMap.put("key", s[0]);
        }

        return resultMap;
    }
}
