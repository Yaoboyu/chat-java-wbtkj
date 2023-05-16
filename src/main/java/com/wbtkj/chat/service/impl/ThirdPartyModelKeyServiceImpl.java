package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.mapper.ThirdPartyModelKeyMapper;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThirdPartyModelKeyServiceImpl implements ThirdPartyModelKeyService {
    @Resource
    ThirdPartyModelKeyMapper thirdPartyModelKeyMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public List<ThirdPartyModelKey> getAllKey() {

        return null;
    }

    @Override
    public boolean addKey(String key, String model) {
        Openaikey openaikey = new Openaikey();
        openaikey.setKey(key);


        return null;
    }

    @Override
    public boolean delKey(long id) {

        return null;
    }

    @Override
    public boolean changeStatus(String key, int status) {

        return null;
    }

    @Override
    public List<String> getEnableGpt3Keys() {
        return null;
    }

    @Override
    public List<String> getEnableGpt4Keys() {
        return null;
    }
}
