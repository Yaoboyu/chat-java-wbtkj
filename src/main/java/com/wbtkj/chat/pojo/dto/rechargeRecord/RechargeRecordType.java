package com.wbtkj.chat.pojo.dto.rechargeRecord;

import lombok.Getter;

@Getter
public enum RechargeRecordType {
    BALANCE(0),
    VIP(1),
    ;

    private int type;

    RechargeRecordType(int type) {
        this.type = type;
    }
}
