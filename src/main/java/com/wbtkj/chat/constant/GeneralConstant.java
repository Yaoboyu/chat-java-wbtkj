package com.wbtkj.chat.constant;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.Map;

// 通用常量
public class GeneralConstant {

    public static final int USER_INIT_BALANCE = 50; // 用户初始余额

    public static final double POINT_RATE = 0.01; // 充值比例

    public static final double CASH_RATE = 0.2; // 返现比例

    public static final int USER_NORMAL_MAX_ROLE = 20; // 普通用户最多角色数

    public static Map<String, Integer> THIRD_MODEL_VALUE = new HashMap<>(); // 第三方模型价格

    static {
        THIRD_MODEL_VALUE.put("gpt-3.5-turbo", 1);
        THIRD_MODEL_VALUE.put("gpt-4", 20);
    }
}
