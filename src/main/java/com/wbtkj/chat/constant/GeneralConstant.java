package com.wbtkj.chat.constant;

import java.util.HashMap;
import java.util.Map;

// 通用常量
public class GeneralConstant {

    /**
     * 用户初始余额
     */
    public static final int USER_INIT_BALANCE = 66;

    /**
     * 用户每天赠送余额
     */
    public static final int USER_GIFT_BALANCE = 10;

    /**
     * 注册赠送被邀请用户余额
     */
    public static final int INVITE_GIFT_BALANCE = 100;

    /**
     * 充值点数兑换人民币比例
     */
    public static final double POINT_RATE = 0.01;

    /**
     * 充值返现比例
     */
    public static final double CDKEY_CASH_RATE = 0.2;

    /**
     * 角色对话返现比例
     */
    public static final double ROLE_CASH_RATE = 0.2;

    /**
     * 普通用户最多角色数
     */
    public static final int USER_NORMAL_MAX_ROLE = 20;

    /**
     * 第三方模型价格
     */
    public static Map<String, Integer> THIRD_MODEL_VALUE = new HashMap<>();

    /**
     * 处理文件价格
     */
    public static final int PARSE_FILE_VALUE = 5;

    static {
        THIRD_MODEL_VALUE.put("GPT3.5", 1);
        THIRD_MODEL_VALUE.put("GPT4", 20);
    }
}
