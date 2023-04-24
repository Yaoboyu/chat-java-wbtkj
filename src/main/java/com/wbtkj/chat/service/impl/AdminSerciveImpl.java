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

    @Override
    public void updateUser(User user) {
        Long id = ThreadLocalConfig.getUser().getId();

        User oldUser = userMapper.selectByPrimaryKey(id);

        oldUser.setStatus(user.getStatus());
        oldUser.setQuota(user.getQuota());
        oldUser.setCost(user.getCost());
        oldUser.setRemark(user.getRemark());
        oldUser.setMy_inv_code(user.getMy_inv_code());
        oldUser.setUse_inv_code(user.getUse_inv_code());
        oldUser.setUpdate_time(new Date());

        userMapper.updateByPrimaryKey(oldUser);
    }
}
