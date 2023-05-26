package com.wbtkj.chat.pojo.vo.role;

import lombok.Data;

import java.util.Date;

@Data
public class RoleHistoryVO {
    private String sessionId;
    private String title;
    private Date createDate;
}
