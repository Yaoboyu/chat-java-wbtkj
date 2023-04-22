package com.wbtkj.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.VO.RegisterVO;
import com.wbtkj.chat.pojo.VO.Result;
import com.wbtkj.chat.service.LoginRegisterSerevice;
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
    LoginRegisterSerevice loginRegisterSerevice;
    @Resource
    SendVerifyCodeService sendVerifyCodeService;


    @GetMapping("/login/{email}/{pwd}")
    public Result Login(@PathVariable String email,@PathVariable String pwd) throws Exception{
        log.info("用户{}登录,密码{}",email,pwd);
        String jwt = loginRegisterSerevice.login(email,pwd);
        return Result.success(new JSONObject().put("token", jwt));
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterVO registerVO) throws Exception{
        String codeByEmail = sendVerifyCodeService.getCodeByEmail(registerVO.getEmail());
        if(codeByEmail == null || StringUtils.isBlank(codeByEmail)) {
            throw new MyServiceException("验证码过期！");
        }
        if(!codeByEmail.equals(registerVO.getCode())) {
            throw new MyServiceException("验证码错误！");
        }

        log.info("用户注册：{}", registerVO.getEmail());
        userService.save(registerVO);
        String jwt = loginRegisterSerevice.login(registerVO.getEmail(), registerVO.getPwd());
        return Result.success(new JSONObject().put("token", jwt));
    }
}
