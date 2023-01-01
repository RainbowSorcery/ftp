package com.lyra.utils;

import java.util.HashMap;
import java.util.Map;

public class CommandUtils {
    public static Map<String, String> processCommand(String command) {
        Map<String, String> resultMap = new HashMap<>();

        command = command.replaceAll("\r\n", "");
        StringBuilder valueBuffer = new StringBuilder();

        String[] s = command.split(" ");
        boolean flag = true;
        for (int i = 0; i < s.length; i++) {
            if (i == 0) {
                resultMap.put("key", s[i]);
            } else if (i == s.length - 1) {
                valueBuffer.append(s[i]);
            } else {
                valueBuffer.append(s[i]).append(" ");
            }
        }

        resultMap.put("value", valueBuffer.toString());

        return resultMap;
    }
}
