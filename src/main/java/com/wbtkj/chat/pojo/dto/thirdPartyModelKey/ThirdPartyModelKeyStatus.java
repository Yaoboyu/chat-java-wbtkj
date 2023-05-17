package com.wbtkj.chat.pojo.dto.thirdPartyModelKey;

import lombok.Getter;

@Getter
public enum ThirdPartyModelKeyStatus {
    ENABLED(0),
    DISABLED(-1),
    INVALID(1);

    private int status;

    ThirdPartyModelKeyStatus(int status) {
        this.status = status;
    }
}

