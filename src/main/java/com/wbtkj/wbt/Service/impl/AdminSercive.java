package com.wbtkj.wbt.Service.impl;

import com.wbtkj.wbt.Mapper.UserMapper;
import com.wbtkj.wbt.Service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminSercive implements AdminService {
    @Resource
    UserMapper userMapper;
    @Override
    public void updateStatus(String email,Boolean s) {
        int status = s == true ? 1 : 0;
        userMapper.updateStatus(email,status);
    }
}
