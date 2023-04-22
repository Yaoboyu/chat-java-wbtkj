package com.wbtkj.chat;


import com.wbtkj.chat.service.SendVerifyCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@EnableConfigurationProperties
public class SendVerifyCodeServiceImplTest {

    @Resource
    private SendVerifyCodeService sendVerifyCodeService;

    @Test
    public void sendMailTest(){
        sendVerifyCodeService.sendMail("soficesi@163.com");
    }

    @Test
    public void checkCodeIsExpiredTest(){
        System.out.println(sendVerifyCodeService.checkCodeIsExist("soficesi@163.com"));
    }

    @Test
    public void getCodeByEmailTest(){
        System.out.println(sendVerifyCodeService.getCodeByEmail("soficesi@163.com"));
    }
}
