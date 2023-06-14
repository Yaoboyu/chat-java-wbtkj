package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.exception.MyException;
import com.wbtkj.chat.mapper.RechargeRecordMapper;
import com.wbtkj.chat.mapper.UserInfoMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.rechargeRecord.RechargeRecordType;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.model.RechargeRecord;
import com.wbtkj.chat.pojo.model.RechargeRecordExample;
import com.wbtkj.chat.pojo.model.UserInfo;
import com.wbtkj.chat.service.CDKEYService;
import com.wbtkj.chat.service.UserService;
import com.wbtkj.chat.utils.CardGenerator;
import com.wbtkj.chat.utils.TimeUtils;
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
    UserService userService;
    @Resource
    RechargeRecordMapper rechargeRecordMapper;
    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public List<String> publish(int type, int num, int value) {
        if (type != RechargeRecordType.BALANCE.getType() && type != RechargeRecordType.VIP.getType()) {
            throw new MyServiceException("类型错误");
        }
        try {
            List<String> res = CardGenerator.generateCards(type, num, value);
            return res;
        } catch (Exception e) {
            throw new MyException();
        }
    }

    @Override
    @Transactional
    public long activate(String cdkey) {
        RechargeRecordExample rechargeRecordExample = new RechargeRecordExample();
        rechargeRecordExample.createCriteria().andCdkeyEqualTo(cdkey);
        List<RechargeRecord> rechargeRecords = rechargeRecordMapper.selectByExample(rechargeRecordExample);

        if(!CollectionUtils.isEmpty(rechargeRecords)){
            throw new MyServiceException("该卡密已被使用!");
        }

        Long userId = ThreadLocalConfig.getUser().getId();
        Map<String, Integer> cdkeyInfo = CardGenerator.getInfo(cdkey);

        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUserId(userId);
        rechargeRecord.setType(cdkeyInfo.get("type"));
        rechargeRecord.setCdkey(cdkey);
        rechargeRecord.setValue(cdkeyInfo.get("value"));
        rechargeRecord.setUseTime(TimeUtils.getTimeGMT8());
        rechargeRecordMapper.insert(rechargeRecord);

        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(ThreadLocalConfig.getUser().getId());

        if (cdkeyInfo.get("type").equals(RechargeRecordType.BALANCE.getType())) {
            userInfo.setBalance(userInfo.getBalance() + cdkeyInfo.get("value"));
            userInfoMapper.updateByPrimaryKey(userInfo);

            // 返现
            if (userInfo.getUseInvCode() != null) {
                userService.cashBack(userInfo.getUseInvCode(), rechargeRecord.getValue(), GeneralConstant.CDKEY_CASH_RATE);
            }

        } else if (cdkeyInfo.get("type").equals(RechargeRecordType.VIP.getType())) {
            //TODO：vip
        }


        return cdkeyInfo.get("value");
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
            Map<String, Integer> cdkeyInfo = CardGenerator.getInfo(cdkey);
            rechargeRecord.setType(cdkeyInfo.get("type"));
            rechargeRecord.setValue(cdkeyInfo.get("value"));
            rechargeRecord.setCdkey(cdkey);
        }

        return rechargeRecord;
    }

    @Override
    @Transactional
    public List<RechargeRecord> getRechargeRecord() {
        RechargeRecordExample rechargeRecordExample = new RechargeRecordExample();
        rechargeRecordExample.createCriteria().andUserIdEqualTo(ThreadLocalConfig.getUser().getId());
        List<RechargeRecord> cdkeys = rechargeRecordMapper.selectByExample(rechargeRecordExample);
        return cdkeys;
    }

}
