package com.wbtkj.wbt.Service;

import com.wbtkj.wbt.Pojo.UserInfo;

public interface LoginService {
    String jwt(String email,String pwd) throws Exception;
}
