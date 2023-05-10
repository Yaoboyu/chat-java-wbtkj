package com.wbtkj.chat;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class UtilTests {
    @Test
    public void randomTest(){
        for(int i=0;i<10;i++) {
            System.out.println(RandomUtil.randomStringUpper(8));
        }
    }
}
