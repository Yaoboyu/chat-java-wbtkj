package com.wbtkj.chat;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import com.wbtkj.chat.utils.MD5Utils;
import com.wbtkj.chat.utils.TimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

public class UtilTests {
    @Test
    public void randomTest() throws UnknownHostException {
        System.out.println(MD5Utils.code("wbtkj@0317"+"68723"));
    }
}
