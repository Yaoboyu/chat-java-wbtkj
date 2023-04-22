package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.Model.Cdkey;

import java.util.List;

public interface CDKEYService {

    List<String> publish(Integer num, Integer value);
    int activate(Cdkey cdkey) throws Exception;
}
