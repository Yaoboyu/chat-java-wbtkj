package com.wbtkj.wbt.Service.impl;

import com.wbtkj.wbt.Exception.MyException;
import com.wbtkj.wbt.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.wbtkj.wbt.Utils.JwtUtils.generateJwt;
import static com.wbtkj.wbt.Utils.MD5Utils.code;

@Service
public class LoginService implements com.wbtkj.wbt.Service.LoginService{
    @Autowired
    UserMapper userMapper;

    /**
     *
     * @param email 用户邮箱
     * @param pwd   用户密码
     * @return      返回jwt令牌
     * @throws Exception 密码账户不匹配会抛出异常不返回token
     */
    @Override
    public String jwt(String email,String pwd) throws Exception{
        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("email",email);
        String salt = userMapper.Salt(email);
        pwd = code(pwd + salt);
        if(pwd != userMapper.Pwd(email))
            throw new MyException("登录信息有误,请核对后输入!");
        claims.put("pwd",pwd);
        String jwt = generateJwt(claims);
        return jwt;
    }
}
