package com.wbtkj.chat.constant;

import lombok.Getter;


@Getter
public enum RedisKeyConstant {

    verify_code("verify_code:", "验证码", 2),
    openaiKey("openai_key:", "thirdPartyModelKey", -1);

    private String key;
    private String desc;
    private int exp; // 过期时间（分钟）

    RedisKeyConstant(String key, String desc, int exp) {
        this.key = key;
        this.desc = desc;
        this.exp = exp;
    }
}