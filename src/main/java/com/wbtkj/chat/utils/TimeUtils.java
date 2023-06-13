package com.wbtkj.chat.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeUtils {

    public static Date getTimeGMT8() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.of("GMT+8")).toInstant());
    }

    public static Date getTimeGMT8(long delta) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newLocalDateTime = now.plus(Duration.ofMillis(delta));
        return Date.from(newLocalDateTime.atZone(ZoneId.of("GMT+8")).toInstant());
    }
}
