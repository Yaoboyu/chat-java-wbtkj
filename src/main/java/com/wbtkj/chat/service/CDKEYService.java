package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.model.RechargeRecord;

import java.util.List;

public interface CDKEYService {

    List<String> publish(int type, int num, int value);

    long activate(String cdkey);

    RechargeRecord getRechargeRecord(String cdkey);

    List<RechargeRecord> getRechargeRecord();
}
