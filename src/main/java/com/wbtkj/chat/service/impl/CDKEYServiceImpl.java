package com.wbtkj.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.CdkeyActivateMapper;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.model.CdkeyActivate;
import com.wbtkj.chat.pojo.model.CdkeyActivateExample;
import com.wbtkj.chat.service.CDKEYService;
import com.wbtkj.chat.utils.AesUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CDKEYServiceImpl implements CDKEYService {
    @Resource
    CdkeyActivateMapper cdkeyActivateMapper;
    @Resource
    UserMapper userMapper;


    private long value(String cdkey) {
        Map<String,String> mp = AesUtil.decode(cdkey);
        return Long.parseLong(mp.get("value"));
    }

    @Override
    public List<String> publish(int num, long value) {
        List<String> list = new ArrayList<String>();
        for(Integer i = 0;i < num;i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", String.valueOf(value));
            map.put("num",i.toString());
            list.add(JSON.toJSONString(map));
        }
        List<String> publish = new ArrayList<String>();
        for(String s : list) {
            publish.add(AesUtil.code(s));
        }
        return publish;
    }

    @Override
    public long activate(String cdkey) {
        CdkeyActivateExample cdkeyActivateExample = new CdkeyActivateExample();
        cdkeyActivateExample.createCriteria().andCdkeyEqualTo(cdkey);
        List<CdkeyActivate> cdkeyActivates = cdkeyActivateMapper.selectByExample(cdkeyActivateExample);

        if(!CollectionUtils.isEmpty(cdkeyActivates)){
            throw new MyServiceException("激活失败:该卡密已被激活!");
        }

        Long userId = ThreadLocalConfig.getUser().getId();
        long value = value(cdkey);

        CdkeyActivate cdkeyActivate = new CdkeyActivate();
        cdkeyActivate.setUserId(userId);
        cdkeyActivate.setCdkey(cdkey);
        cdkeyActivate.setValue(value);
        cdkeyActivate.setUseTime(new Date());

        cdkeyActivateMapper.insert(cdkeyActivate);
        return value;
    }

}
