package com.wbtkj.chat.pojo.dto.thirdPartyModelKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyAndHost {
    private String key;
    private String host;
}
