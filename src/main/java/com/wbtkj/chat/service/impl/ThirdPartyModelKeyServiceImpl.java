package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.filter.OpenAiAuthInterceptor;
import com.wbtkj.chat.mapper.ThirdPartyModelKeyMapper;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.ThirdPartyModelKeyStatus;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKeyExample;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThirdPartyModelKeyServiceImpl implements ThirdPartyModelKeyService {
    @Resource
    ThirdPartyModelKeyMapper thirdPartyModelKeyMapper;
    @Resource
    @Lazy
    OpenAiAuthInterceptor openAiAuthInterceptor;

    @Override
    @Transactional
    public List<ThirdPartyModelKey> getAllKey() {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria();
        List<ThirdPartyModelKey> keys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        return keys;
    }

    @Override
    @Transactional
    public boolean addKey(String key, String model) {
        ThirdPartyModelKey thirdPartyModelKey = new ThirdPartyModelKey();
        thirdPartyModelKey.setModel(model);
        thirdPartyModelKey.setKey(key);
        thirdPartyModelKey.setStatus(ThirdPartyModelKeyStatus.ENABLED.getStatus());
        Date date = new Date();
        thirdPartyModelKey.setCreateTime(date);
        thirdPartyModelKey.setUpdateTime(date);
        int insert = thirdPartyModelKeyMapper.insert(thirdPartyModelKey);

        openAiAuthInterceptor.addKey(thirdPartyModelKey.getKey(), thirdPartyModelKey.getModel());
        return insert != 0;
    }

    @Override
    @Transactional
    public boolean delKey(long id) {
        ThirdPartyModelKey thirdPartyModelKey = thirdPartyModelKeyMapper.selectByPrimaryKey(id);
        int deleteByPrimaryKey = thirdPartyModelKeyMapper.deleteByPrimaryKey(id);
        openAiAuthInterceptor.delKey(thirdPartyModelKey.getKey(), thirdPartyModelKey.getModel());
        return deleteByPrimaryKey != 0;
    }

    @Override
    @Transactional
    public boolean changeStatus(long id, int status) {
        ThirdPartyModelKey thirdPartyModelKey = thirdPartyModelKeyMapper.selectByPrimaryKey(id);
        thirdPartyModelKey.setStatus(status);
        thirdPartyModelKeyMapper.updateByPrimaryKey(thirdPartyModelKey);
        if (status == ThirdPartyModelKeyStatus.ENABLED.getStatus()) {
            openAiAuthInterceptor.addKey(thirdPartyModelKey.getKey(), thirdPartyModelKey.getModel());
        } else {
            openAiAuthInterceptor.delKey(thirdPartyModelKey.getKey(), thirdPartyModelKey.getModel());
        }

        return true;
    }

    @Override
    @Transactional
    public boolean changeStatus(String key, int status) {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria().andKeyEqualTo(key);
        List<ThirdPartyModelKey> thirdPartyModelKeys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        if (CollectionUtils.isEmpty(thirdPartyModelKeys)) {
            return false;
        }

        return changeStatus(thirdPartyModelKeys.get(0).getId(), status);
    }

    @Override
    @Transactional
    public List<String> getEnableGpt3Keys() {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria()
                .andModelEqualTo(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .andStatusEqualTo(ThirdPartyModelKeyStatus.ENABLED.getStatus());
        List<ThirdPartyModelKey> thirdPartyModelKeys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        List<String> res = thirdPartyModelKeys.stream().map(ThirdPartyModelKey::getKey).collect(Collectors.toList());
        return res;
    }

    @Override
    @Transactional
    public List<String> getEnableGpt4Keys() {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria()
                .andModelEqualTo(ChatCompletion.Model.GPT_4.getName())
                .andStatusEqualTo(ThirdPartyModelKeyStatus.ENABLED.getStatus());
        List<ThirdPartyModelKey> thirdPartyModelKeys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        List<String> res = thirdPartyModelKeys.stream().map(ThirdPartyModelKey::getKey).collect(Collectors.toList());
        return res;
    }

}
