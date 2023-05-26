package com.wbtkj.chat.pojo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("messages")
@Data
@Builder
public class Messages {
    @Id
    private String id;
    private Long roleId;
    private Long userId;
    private Date createDate;
    private String messages;
}
