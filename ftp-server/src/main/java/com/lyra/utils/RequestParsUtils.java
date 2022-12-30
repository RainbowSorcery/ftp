package com.lyra.utils;

import org.springframework.stereotype.Component;

@Component
public class RequestParsUtils {
    public String parsKeyAndValue(String requestBody) {
        return requestBody.split(" ")[1];
    }
}
