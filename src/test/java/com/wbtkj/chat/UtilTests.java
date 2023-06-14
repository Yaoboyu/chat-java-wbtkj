package com.wbtkj.chat;

import com.wbtkj.chat.utils.MD5Utils;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

public class UtilTests {
    @Test
    public void randomTest() throws UnknownHostException {
        System.out.println(MD5Utils.code("wbtkj@0317"+"68723"));
    }
}
