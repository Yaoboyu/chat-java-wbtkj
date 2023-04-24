package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.model.UserExample;
import com.wbtkj.chat.service.LoginRegisterSerevice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wbtkj.chat.utils.JwtUtils;
import com.wbtkj.chat.utils.MD5Utils;
import org.springframework.util.CollectionUtils;

@Service
public class LoginRegisterSereviceImpl implements LoginRegisterSerevice {
    @Resource
    UserMapper userMapper;

    /**
     * 登录验证
     * @param email 用户邮箱
     * @param pwd   用户密码
     * @return      返回jwt令牌
     */
    @Override
    public String login(String email, String pwd){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(users)) {
            throw new MyServiceException("用户名不存在");
        }
        User user = users.get(0);

        Map<String,Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("id", user.getId());
        pwd = MD5Utils.code(pwd + user.getSalt());

        if(!pwd.equals(user.getPwd())) {
            throw new MyServiceException("登录信息有误,请核对后输入!");
        }

        return JwtUtils.generateJwt(claims);
    }
}
