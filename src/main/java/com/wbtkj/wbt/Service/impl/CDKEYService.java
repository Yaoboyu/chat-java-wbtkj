package com.wbtkj.wbt.Service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wbtkj.wbt.Exception.MyException;
import com.wbtkj.wbt.Mapper.CDKEYMapper;
import com.wbtkj.wbt.Mapper.UserMapper;
import com.wbtkj.wbt.Pojo.Cdkey;
import com.wbtkj.wbt.Utils.AesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CDKEYService implements com.wbtkj.wbt.Service.CDKEYService {
    @Resource
    CDKEYMapper cdkeyMapper;
    @Resource
    UserMapper userMapper;
    //参数是原码返回加密后密文
    String code(String rawCode) {
        return AesUtil.encryptAES(AesUtil.SECRET_KEY,rawCode);
    }
    //参数是密文,返回原码
    String decode(String code) throws Exception{
        return AesUtil.decryptAES(AesUtil.SECRET_KEY,code);
    }

    Map<String, String> JsonMap(String json) {
        return JSON.parseObject(json, new TypeReference<Map<String, String>>() {});
    }

    int value(String CDKEY) throws Exception{
        Map<String,String> mp = JsonMap(decode(CDKEY));
        return Integer.parseInt(mp.get("value"));
    }

    @Override
    public List<String> publish(Integer num, Integer value) {
        List<String> list = new ArrayList<String>();
        for(Integer i = 0;i < num;i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", value.toString());
            map.put("num",i.toString());
            list.add(JSON.toJSONString(map));
        }
        List<String> publish = new ArrayList<String>();
        for(String s : list)
            publish.add(code(s));
        return publish;
    }

    @Override
    public int activate(Cdkey cdkey) throws Exception{
        int value = value(cdkey.getCode());
        Long id = cdkey.getUserId();
        DateTime dateTime = new DateTime(System.currentTimeMillis());
        String code = cdkey.getCode();
        if(cdkeyMapper.count(code) > 0)
            throw new MyException("激活失败:该卡密已被激活!");
        cdkeyMapper.activate(id,value,dateTime,code);
        userMapper.addQuota(value, Math.toIntExact(id));
        return value;
    }

}
