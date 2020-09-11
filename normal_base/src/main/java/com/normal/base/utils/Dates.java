package com.normal.base.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class Dates {

    private static final   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String format(long timestamp, String... pattern) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        if (pattern.length == 0) {
            return localDateTime.format(formatter);
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern[0]));
    }


}
