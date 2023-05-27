package com.wbtkj.chat;

import com.wbtkj.chat.service.CDKEYService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@EnableConfigurationProperties
public class CDKEYServiceTest {
    @Resource
    CDKEYService cdkeyService;

    @Test
    public void publishTest() {
        List<String> publish = cdkeyService.publish(0, 1, 1000);
        System.out.println(publish);
    }
}
