package com.wbtkj.chat;

import com.wbtkj.chat.pojo.vo.user.RegisterVO;
import com.wbtkj.chat.service.SendVerifyCodeService;
import com.wbtkj.chat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

@SpringBootTest
@EnableConfigurationProperties
@ActiveProfiles("dev")
public class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Test
    public void registerTest(){
        RegisterVO registerVO = new RegisterVO();
        registerVO.setEmail("12345@qq.com");
        registerVO.setPwd("123456");
        registerVO.setCode("123567");
        userService.register(registerVO);
    }
}
