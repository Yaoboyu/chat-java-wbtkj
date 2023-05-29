package com.wbtkj.chat;

import com.wbtkj.chat.pojo.vo.user.UserRegisterVO;
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
        UserRegisterVO userRegisterVO = new UserRegisterVO();
        userRegisterVO.setEmail("12345@qq.com");
        userRegisterVO.setPwd("12345678");
        userRegisterVO.setCode("123567");
        userService.register(userRegisterVO);
    }
}
