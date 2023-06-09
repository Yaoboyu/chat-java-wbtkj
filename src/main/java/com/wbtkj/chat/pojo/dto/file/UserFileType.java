package com.wbtkj.chat.pojo.dto.file;

import lombok.Getter;

@Getter
public enum UserFileType {
    FILE(0),
    URL(1),
    ;

    private int type;

    UserFileType(int type) {
        this.type = type;
    }
}
