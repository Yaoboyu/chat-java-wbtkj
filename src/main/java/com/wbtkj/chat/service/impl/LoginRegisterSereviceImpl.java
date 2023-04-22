package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.service.LoginRegisterSerevice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.wbtkj.chat.utils.JwtUtils.generateJwt;
import static com.wbtkj.chat.utils.MD5Utils.code;

@Service
public class LoginRegisterSereviceImpl implements LoginRegisterSerevice {
    @Resource
    UserMapper userMapper;

    /**
     * 登录验证
     * @param email 用户邮箱
     * @param pwd   用户密码
     * @return      返回jwt令牌
     * @throws Exception 密码账户不匹配会抛出异常不返回token
     */
    @Override
    public String login(String email, String pwd) throws Exception{
        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("email", email);
        Long id = userMapper.id(email);
        if(id == null) {
            throw new MyServiceException("用户名不存在");
        }

        claims.put("id", id);
        String salt = userMapper.salt(email);
        pwd = code(pwd + salt);

        if(!pwd.equals(userMapper.pwd(email))) {
            throw new MyServiceException("登录信息有误,请核对后输入!");
        }

        return generateJwt(claims);
    }
}
