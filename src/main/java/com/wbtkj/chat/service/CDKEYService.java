package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.model.CdkeyActivate;

import java.util.List;

public interface CDKEYService {

    List<String> publish(int num, long value);

    long activate(String cdkey);

    CdkeyActivate getCdkeyInfo(String cdkey);
}
