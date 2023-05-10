package com.wbtkj.chat.service.impl;

import com.github.pagehelper.PageHelper;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.pojo.model.Openaikey;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdminSerciveImpl implements AdminService {
    @Resource
    UserMapper userMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public List<User> getUsers(Integer page, Integer pagesize, String email) {
        PageHelper.startPage(page, pagesize);
        List<User> lists = userMapper.getUsers(email);
        return lists;
    }

    @Override
    public Object addOpenAiKey(String key) {
        redisTemplate.opsForHash().put("openaikey",key,1);
        return null;
    }

    @Override
    public Object delOpenAiKey(String key) {
        redisTemplate.opsForHash().delete("openaikey",key);
        return null;
    }

    @Override
    public Object changeStatus(String key, int status) {
        redisTemplate.opsForHash().put("openaikey",key,status);
        return null;
    }

    @Override
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        Map<String, Integer> openaikey = (Map<String, Integer>) (Object)redisTemplate.opsForHash().entries("openaikey");
        for (String i: openaikey.keySet()
        ) {
            keys.add(i);
        }
        return keys;
    }

}
