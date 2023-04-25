package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.user.UserStatus;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.model.UserExample;
import com.wbtkj.chat.pojo.vo.user.RegisterVO;
import com.wbtkj.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.utils.MD5Utils;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;


    @Override
    public void save(RegisterVO registerVO){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(registerVO.getEmail());

        if(userMapper.countByExample(userExample) > 0) {
            throw new MyServiceException("注册失败:账号已被注册!");
        }

        Long time = System.currentTimeMillis();
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);

        User newUser = new User();
        newUser.setEmail(registerVO.getEmail());
        newUser.setSalt(String.valueOf((time % 100000)));
        newUser.setPwd(MD5Utils.code(registerVO.getPwd() + newUser.getSalt()));
        newUser.setStatus(UserStatus.ENABLED.getStatus());
        newUser.setQuota(GeneralConstant.USER_INIT_QUOTA);
        newUser.setCost(0L);
        newUser.setRemark("");
        newUser.setMy_inv_code("");
        newUser.setUse_inv_code("");
        newUser.setCreate_time(date);
        newUser.setUpdate_time(date);

        userMapper.insert(newUser);
    }

    @Override
    public void changePwd(String pwd, String code) throws Exception {
        //TODO :发邮箱校验验证码


    }
}
