package com.wbtkj.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.RechargeRecordMapper;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.rechargeRecord.RechargeRecordType;
import com.wbtkj.chat.pojo.model.RechargeRecord;
import com.wbtkj.chat.pojo.model.RechargeRecordExample;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.model.UserExample;
import com.wbtkj.chat.service.CDKEYService;
import com.wbtkj.chat.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class CDKEYServiceImpl implements CDKEYService {
    @Resource
    RechargeRecordMapper rechargeRecordMapper;
    @Resource
    UserMapper userMapper;


    private int value(String cdkey) {
        //TODO:解析失败处理
        Map<String,String> mp = AesUtil.decode(cdkey);
        return Integer.parseInt(mp.get("value"));
    }

    @Override
    public List<String> publish(int num, int value) {
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
    @Transactional
    public long activate(String cdkey) {
        RechargeRecordExample rechargeRecordExample = new RechargeRecordExample();
        rechargeRecordExample.createCriteria().andCdkeyEqualTo(cdkey);
        List<RechargeRecord> rechargeRecords = rechargeRecordMapper.selectByExample(rechargeRecordExample);

        if(!CollectionUtils.isEmpty(rechargeRecords)){
            throw new MyServiceException("激活失败:该卡密已被激活!");
        }

        Long userId = ThreadLocalConfig.getUser().getId();
        int value = value(cdkey);

        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUserId(userId);
        rechargeRecord.setType(RechargeRecordType.BALANCE.getType());
        rechargeRecord.setCdkey(cdkey);
        rechargeRecord.setValue(value);
        rechargeRecord.setUseTime(new Date());
        rechargeRecordMapper.insert(rechargeRecord);

        User user = userMapper.selectByPrimaryKey(ThreadLocalConfig.getUser().getId());
        user.setBalance(user.getBalance() + value);
        userMapper.updateByPrimaryKey(user);

        return value;
    }

    @Override
    @Transactional
    public RechargeRecord getRechargeRecord(String cdkey) {
        RechargeRecordExample rechargeRecordExample = new RechargeRecordExample();
        rechargeRecordExample.createCriteria().andCdkeyEqualTo(cdkey);
        List<RechargeRecord> cdkeyActivates = rechargeRecordMapper.selectByExample(rechargeRecordExample);

        RechargeRecord rechargeRecord;

        if(!CollectionUtils.isEmpty(cdkeyActivates)){
            rechargeRecord = cdkeyActivates.get(0);
        } else {
            rechargeRecord = new RechargeRecord();
            rechargeRecord.setValue(value(cdkey));
            rechargeRecord.setCdkey(cdkey);
        }

        return rechargeRecord;
    }

}
