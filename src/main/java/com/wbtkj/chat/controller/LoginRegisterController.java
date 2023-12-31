package com.wbtkj.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.vo.user.UserLoginVO;
import com.wbtkj.chat.pojo.vo.user.UserRegisterVO;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.SendVerifyCodeService;
import com.wbtkj.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@ResponseBody
public class LoginRegisterController {
    @Resource
    private UserService userService;
    @Resource
    SendVerifyCodeService sendVerifyCodeService;


    @PostMapping("/login")
    public Result Login(@RequestBody UserLoginVO userLoginVO) {
        log.info("用户 {} 登录", userLoginVO.getEmail());
        return Result.success(userService.login(userLoginVO.getEmail(), userLoginVO.getPwd()));
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterVO userRegisterVO) {
        String codeByEmail = sendVerifyCodeService.getCodeByEmail(userRegisterVO.getEmail());
        if(codeByEmail == null || StringUtils.isBlank(codeByEmail)) {
            throw new MyServiceException("验证码过期！");
        }
        if(!codeByEmail.equals(userRegisterVO.getCode())) {
            throw new MyServiceException("验证码错误！");
        }

        log.info("用户注册：{}", userRegisterVO.getEmail());
        userService.register(userRegisterVO);
        String token = userService.login(userRegisterVO.getEmail(), userRegisterVO.getPwd());
        return Result.success(new JSONObject().put("token", token));
    }

    @PostMapping("/changePwd")
    public Result changePwd(@RequestBody UserRegisterVO userRegisterVO) {
        String codeByEmail = sendVerifyCodeService.getCodeByEmail(userRegisterVO.getEmail());
        if(codeByEmail == null || StringUtils.isBlank(codeByEmail)) {
            throw new MyServiceException("验证码过期！");
        }
        if(!codeByEmail.equals(userRegisterVO.getCode())) {
            throw new MyServiceException("验证码错误！");
        }

        userService.changePwd(userRegisterVO.getEmail(), userRegisterVO.getPwd());

        log.info("用户修改密码：{}", userRegisterVO.getEmail());
        return Result.success();
    }
}
