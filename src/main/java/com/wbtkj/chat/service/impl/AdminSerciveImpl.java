package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AdminSerciveImpl implements AdminService {
    @Resource
    UserMapper userMapper;


}
