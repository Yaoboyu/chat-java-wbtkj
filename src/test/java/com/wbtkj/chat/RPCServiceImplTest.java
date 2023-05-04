package com.wbtkj.chat;

import com.wbtkj.chat.service.RPCService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@EnableConfigurationProperties
public class RPCServiceImplTest {

    @Resource
    private RPCService rpcService;

    @Test
    public void Test(){
        rpcService.test();
    }
}
