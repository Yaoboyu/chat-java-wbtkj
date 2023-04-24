package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.vo.user.RegisterVO;

public interface UserService {
    void save(RegisterVO registerVO) throws Exception;
    void changePwd(String pwd,String code) throws Exception;
}
