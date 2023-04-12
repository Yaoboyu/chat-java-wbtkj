package com.wbtkj.wbt.Service.impl;

import com.wbtkj.wbt.Exception.MyException;
import com.wbtkj.wbt.Mapper.LoginMapper;
import com.wbtkj.wbt.Pojo.UserInfo;
import com.wbtkj.wbt.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import static com.wbtkj.wbt.Utils.JwtUtils.generateJwt;
import static com.wbtkj.wbt.Utils.MD5Utils.code;

@Service
public class LoginService implements com.wbtkj.wbt.Service.LoginService{
    @Autowired
    LoginMapper loginMapper;
    @Override
    public String jwt(String email,String pwd) throws Exception{
        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("email",email);
        String salt = loginMapper.Salt(email);
        pwd = code(pwd + salt);
        if(pwd != loginMapper.Pwd(email))
            throw new MyException("登录信息有误,请核对后输入!");
        claims.put("pwd",pwd);
        String jwt = generateJwt(claims);
        return jwt;
    }
}
