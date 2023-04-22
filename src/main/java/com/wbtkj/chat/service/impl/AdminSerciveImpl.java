package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminSerciveImpl implements AdminService {
    @Resource
    UserMapper userMapper;
    @Override
    public void updateStatus(String email, int status) {
        userMapper.updateStatus(email,status);
    }
}
