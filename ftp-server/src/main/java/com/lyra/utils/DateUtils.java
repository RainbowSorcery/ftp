package com.lyra.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);
    }
}
