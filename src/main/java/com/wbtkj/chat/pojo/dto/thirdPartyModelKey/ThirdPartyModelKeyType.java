package com.wbtkj.chat.pojo.dto.thirdPartyModelKey;

import lombok.Getter;

@Getter
public enum ThirdPartyModelKeyType {
    GPT3_5("GPT3.5"),
    GPT4("GPT4");

    private String name;

    ThirdPartyModelKeyType(String name) {
        this.name = name;
    }
}
