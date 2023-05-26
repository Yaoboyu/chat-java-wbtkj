package com.wbtkj.chat.pojo.vo.role;

import lombok.Data;

@Data
public class WSChatMessage {
    private Long roleId;
    private String chatSessionId;
    private String message;
}
