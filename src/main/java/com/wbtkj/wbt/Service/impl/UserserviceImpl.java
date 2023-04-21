package com.wbtkj.wbt.Service.impl;

import com.wbtkj.wbt.Exception.MyException;
import com.wbtkj.wbt.Mapper.UserMapper;
import com.wbtkj.wbt.Pojo.UserInfo;
import com.wbtkj.wbt.Service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wbtkj.wbt.Utils.MD5Utils.code;

@Service
public class UserserviceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    public int UserNumber(){
        return userMapper.UserNumber();
    }
    @Override
    public void save(UserInfo userInfo) throws Exception{
        int cnt = userMapper.Count(userInfo.getEmail());
        if(cnt > 0)
            throw new MyException("注册失败:账号已被注册!");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = System.currentTimeMillis();
        Date date = new Date(time);
        userInfo.setRegDate(date);
        time %= 100000;
        userInfo.setSalt(time.toString());
        String str = userInfo.getPwd() + time.toString();
        userInfo.setPwd(code(str));
        userMapper.InsertUser(userInfo.getEmail(), userInfo.getPwd(),userInfo.getSalt(),userInfo.getRegDate());
    }

    @Override
    public void ChangePwd(String email, String pwd) throws Exception {

    }
}
