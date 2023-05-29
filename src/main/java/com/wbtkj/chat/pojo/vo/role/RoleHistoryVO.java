package com.wbtkj.chat.pojo.vo.role;

import com.wbtkj.chat.pojo.model.ChatSession;
import lombok.Data;

import java.util.Date;

@Data
public class RoleHistoryVO {
    private String chatSessionId;
    private String title;
    private Date createDate;

    public RoleHistoryVO(ChatSession chatSession) {
        this.chatSessionId = chatSession.getChatSessionId();
        this.title = chatSession.getMessages().get(0).getContent();
        this.createDate = chatSession.getCreateDate();
    }
}
