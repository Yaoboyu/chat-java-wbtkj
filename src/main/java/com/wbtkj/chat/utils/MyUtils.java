package com.wbtkj.chat.utils;

import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.ThirdPartyModelKeyType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MyUtils {

    public static Date getTimeGMT8() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.of("GMT+8")).toInstant());
    }

    public static Date getTimeGMT8(long delta) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newLocalDateTime = now.plus(Duration.ofMillis(delta));
        return Date.from(newLocalDateTime.atZone(ZoneId.of("GMT+8")).toInstant());
    }

    public static boolean checkModel(String model) {
        return model.equals(ThirdPartyModelKeyType.GPT3_5.getName()) || model.equals(ThirdPartyModelKeyType.GPT4.getName());
    }

    public static String getRealModel(String model) {
        if (model.equals(ThirdPartyModelKeyType.GPT3_5.getName())) {
            return ChatCompletion.Model.DEFAULT_3_5.getName();
        } else if (model.equals(ThirdPartyModelKeyType.GPT4.getName())) {
            return ChatCompletion.Model.DEFAULT_4.getName();
        } else {
            return null;
        }
    }
}
