package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.pojo.Model.User;
import com.wbtkj.chat.pojo.VO.RegisterVO;
import com.wbtkj.chat.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wbtkj.chat.utils.MD5Utils.code;

@Service
public class UserserviceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    public int UserNumber(){
        return userMapper.userCount();
    }
    @Override
    public void save(RegisterVO registerVO){
        int cnt = userMapper.count(registerVO.getEmail());
        if(cnt > 0) {
            throw new MyServiceException("注册失败:账号已被注册!");
        }

        User user = new User();
        user.setEmail(registerVO.getEmail());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = System.currentTimeMillis();
        Date date = new Date(time);
        user.setRegDate(date);
        time %= 100000;
        user.setSalt(time.toString());
        String str = registerVO.getPwd() + time.toString();
        user.setPwd(code(str));
        userMapper.insertUser(user.getEmail(), user.getPwd(), user.getSalt(), date, date);
    }

    @Override
    public void ChangePwd(String email, String pwd) throws Exception {

    }
}
