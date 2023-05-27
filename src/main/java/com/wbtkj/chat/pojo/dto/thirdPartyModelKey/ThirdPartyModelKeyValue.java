package com.wbtkj.chat.pojo.dto.thirdPartyModelKey;

import java.util.HashMap;
import java.util.Map;

public class ThirdPartyModelKeyValue {
    private static Map<String, Integer> map;

    static {
        map = new HashMap<>();
        map.put("gpt-3.5-turbo", 1);
        map.put("gpt-4", 10);
    }

    public static int getValue(String model) {
        return map.get(model);
    }
}
