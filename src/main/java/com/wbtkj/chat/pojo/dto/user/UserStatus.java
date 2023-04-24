package com.wbtkj.chat.pojo.dto.user;

import lombok.Getter;

@Getter
public enum UserStatus {
    ENABLED(0),
    DISABLED(1),
    VIP(2);

    private int status;

    UserStatus(int status) {
        this.status = status;
    }
}
