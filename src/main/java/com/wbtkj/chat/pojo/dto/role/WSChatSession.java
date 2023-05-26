package com.wbtkj.chat.pojo.dto.role;

import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WSChatSession {
    private Long userId;
    private String email;
    private Role role;
    private String chatSessionId;
    private List<Message> messageList;
}
