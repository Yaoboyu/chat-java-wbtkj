package com.wbtkj.chat.pojo.vo.user;

import lombok.Data;

@Data
public class UserRegisterVO {
    private String email;
    private String pwd;
    private String code;
    private String invCode;
}
