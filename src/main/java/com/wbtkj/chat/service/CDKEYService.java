package com.wbtkj.chat.service;

import java.util.List;

public interface CDKEYService {

    List<String> publish(int num, long value);
    long activate(String cdkey);
}
