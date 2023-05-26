package com.wbtkj.chat.pojo.model;

import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("chat_session")
@Data
@Builder
public class ChatSession {
    @Id
    private String chatSessionId;
    private Long roleId;
    private Long userId;
    private Date createDate;
    private List<Message> messages;
}
