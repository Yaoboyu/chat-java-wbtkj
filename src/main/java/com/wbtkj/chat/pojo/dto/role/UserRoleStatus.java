package com.wbtkj.chat.pojo.dto.role;

import lombok.Getter;

@Getter
public enum UserRoleStatus {
    ENABLED(0),
    DISABLED(-1),
    ;

    private int status;

    UserRoleStatus(int status) {
        this.status = status;
    }
}
