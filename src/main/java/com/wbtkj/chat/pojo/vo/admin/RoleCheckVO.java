package com.wbtkj.chat.pojo.vo.admin;

import lombok.Data;

@Data
public class RoleCheckVO {
    private Long roleId;
    private Boolean canShelf;
    private String remark;
}
