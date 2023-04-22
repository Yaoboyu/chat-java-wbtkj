package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.VO.RegisterVO;

public interface UserService {
    void save(RegisterVO registerVO) throws Exception;
    void ChangePwd(String email,String pwd) throws Exception;
}
