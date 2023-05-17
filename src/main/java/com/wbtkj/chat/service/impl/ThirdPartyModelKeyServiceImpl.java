package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.mapper.ThirdPartyModelKeyMapper;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.OpenAIKeyStatus;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKeyExample;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

public class ThirdPartyModelKeyServiceImpl implements ThirdPartyModelKeyService {
    @Resource
    ThirdPartyModelKeyMapper thirdPartyModelKeyMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public List<ThirdPartyModelKey> getAllKey() {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria();
        List<ThirdPartyModelKey> keys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        return keys;
    }

    @Override
    public boolean addKey(String key, String model) {
        ThirdPartyModelKey thirdPartyModelKey = new ThirdPartyModelKey();
        thirdPartyModelKey.setModel(model);
        thirdPartyModelKey.setKey(key);
        thirdPartyModelKeyMapper.insert(thirdPartyModelKey);
        return true;
    }

    @Override
    public boolean delKey(long id) {
        thirdPartyModelKeyMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Override
    public boolean changeStatus(String key, int status) {
        ThirdPartyModelKey thirdPartyModelKey = new ThirdPartyModelKey();
        thirdPartyModelKey.setStatus(status);
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria().andKeyEqualTo(key);
        thirdPartyModelKeyMapper.updateByExample(thirdPartyModelKey,thirdPartyModelKeyExample);
        return true;
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
