package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.model.RechargeRecord;

import java.util.List;

public interface CDKEYService {

    List<String> publish(int num, int value);

    long activate(String cdkey);

    RechargeRecord getRechargeRecord(String cdkey);
}
