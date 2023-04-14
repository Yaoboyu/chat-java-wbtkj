package com.wbtkj.wbt.Service;

import com.wbtkj.wbt.Pojo.Cdkey;

import java.util.List;

public interface CDKEYService {

    List<String> publish(Integer num, Integer value);
    int activate(Cdkey cdkey) throws Exception;
}
