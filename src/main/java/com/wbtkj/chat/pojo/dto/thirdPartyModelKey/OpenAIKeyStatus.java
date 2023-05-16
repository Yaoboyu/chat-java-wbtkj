package com.wbtkj.chat.pojo.dto.thirdPartyModelKey;

import lombok.Getter;

@Getter
public enum OpenAIKeyStatus {
    ENABLED(0),
    DISABLED(-1),
    INVALID(1);

    private int status;

    OpenAIKeyStatus(int status) {
        this.status = status;
    }
}

