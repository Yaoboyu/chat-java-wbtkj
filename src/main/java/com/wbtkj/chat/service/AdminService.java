package com.wbtkj.chat.service;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.model.Admin;

public interface AdminService {

    String login(String username, String pwd);




    /***
     * 检查管理员用户名密码
     * @param username
     * @return
     * @throws MyServiceException
     */
    Admin getCheckedAdmin(String username) throws MyServiceException;

    /**
     * 解析token并将用户信息存入ThreadLocal
     * @param token
     * @return
     */
    boolean checkToken(String token) throws MyServiceException;
}
