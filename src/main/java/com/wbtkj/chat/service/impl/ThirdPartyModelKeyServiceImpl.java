package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.filter.OpenAiAuthInterceptor;
import com.wbtkj.chat.mapper.ThirdPartyModelKeyMapper;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.KeyAndHost;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.ThirdPartyModelKeyStatus;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.ThirdPartyModelKeyType;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKeyExample;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
import com.wbtkj.chat.utils.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
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
        thirdPartyModelKeyExample.setOrderByClause("id");
        List<ThirdPartyModelKey> keys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        return keys;
    }

    @Override
    @Transactional
    public boolean addKey(ThirdPartyModelKey thirdPartyModelKeyVO) {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria().andKeyEqualTo(thirdPartyModelKeyVO.getKey()).andModelEqualTo(thirdPartyModelKeyVO.getModel());
        if(thirdPartyModelKeyMapper.countByExample(thirdPartyModelKeyExample) > 0) {
            throw new MyServiceException("第三方key已存在");
        }

        if (!MyUtils.checkModel(thirdPartyModelKeyVO.getModel())) {
            throw new MyServiceException("模型不可用");
        }


        thirdPartyModelKeyVO.setStatus(ThirdPartyModelKeyStatus.ENABLED.getStatus());
        Date date = MyUtils.getTimeGMT8();
        thirdPartyModelKeyVO.setCreateTime(date);
        thirdPartyModelKeyVO.setUpdateTime(date);
        int insert = thirdPartyModelKeyMapper.insert(thirdPartyModelKeyVO);

        openAiAuthInterceptor.addKey(thirdPartyModelKeyVO.getKey(), thirdPartyModelKeyVO.getHost(), thirdPartyModelKeyVO.getModel());
        return insert != 0;
    }

    @Override
    @Transactional
    public boolean delKey(long id) {
        ThirdPartyModelKey thirdPartyModelKey = thirdPartyModelKeyMapper.selectByPrimaryKey(id);
        if(thirdPartyModelKey == null) {
            throw new MyServiceException("未找到第三方key");
        }

        int deleteByPrimaryKey = thirdPartyModelKeyMapper.deleteByPrimaryKey(id);
        openAiAuthInterceptor.delKey(thirdPartyModelKey.getKey(), thirdPartyModelKey.getModel());
        return deleteByPrimaryKey != 0;
    }

    @Override
    @Transactional
    public boolean changeStatus(long id, int status) {
        ThirdPartyModelKey thirdPartyModelKey = thirdPartyModelKeyMapper.selectByPrimaryKey(id);
        if(thirdPartyModelKey == null) {
            throw new MyServiceException("未找到第三方key");
        }

        thirdPartyModelKey.setStatus(status);
        thirdPartyModelKey.setUpdateTime(MyUtils.getTimeGMT8());
        thirdPartyModelKeyMapper.updateByPrimaryKey(thirdPartyModelKey);
        if (status == ThirdPartyModelKeyStatus.ENABLED.getStatus()) {
            openAiAuthInterceptor.addKey(thirdPartyModelKey.getKey(), thirdPartyModelKey.getHost(), thirdPartyModelKey.getModel());
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
//            throw new MyServiceException("未找到第三方key");
        }

        return changeStatus(thirdPartyModelKeys.get(0).getId(), status);
    }

    @Override
    @Transactional
    public List<KeyAndHost> getEnableGpt3Keys() {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria()
                .andModelEqualTo(ThirdPartyModelKeyType.GPT3_5.getName())
                .andStatusEqualTo(ThirdPartyModelKeyStatus.ENABLED.getStatus());
        List<ThirdPartyModelKey> thirdPartyModelKeys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        List<KeyAndHost> res = new ArrayList<>();
        for (ThirdPartyModelKey thirdPartyModelKey : thirdPartyModelKeys) {
            res.add(KeyAndHost.builder().key(thirdPartyModelKey.getKey()).host(thirdPartyModelKey.getHost()).build());
        }
        log.info("内存中GPT3.5 key个数: {}", res.size());
        return res;
    }

    @Override
    @Transactional
    public List<KeyAndHost> getEnableGpt4Keys() {
        ThirdPartyModelKeyExample thirdPartyModelKeyExample = new ThirdPartyModelKeyExample();
        thirdPartyModelKeyExample.createCriteria()
                .andModelEqualTo(ThirdPartyModelKeyType.GPT4.getName())
                .andStatusEqualTo(ThirdPartyModelKeyStatus.ENABLED.getStatus());
        List<ThirdPartyModelKey> thirdPartyModelKeys = thirdPartyModelKeyMapper.selectByExample(thirdPartyModelKeyExample);
        List<KeyAndHost> res = new ArrayList<>();
        for (ThirdPartyModelKey thirdPartyModelKey : thirdPartyModelKeys) {
            res.add(KeyAndHost.builder().key(thirdPartyModelKey.getKey()).host(thirdPartyModelKey.getHost()).build());
        }
        log.info("内存中GPT4 key个数: {}", res.size());
        return res;
    }
}
