package com.wbtkj.chat;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class UtilTests {
    @Test
    public void randomTest() throws UnknownHostException {
        System.out.println(ReUtil.isMatch("(https?://)?([^/]+)(/.*)?", "htt://www.com"));
    }
}
