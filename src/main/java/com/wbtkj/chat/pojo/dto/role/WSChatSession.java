package com.wbtkj.chat.pojo.dto.role;

import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.model.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class WSChatSession implements Serializable {
    private Long userId;
    private String email;
    private Long roleId;
    private String chatSessionId;
    private List<Message> messageList;
}
