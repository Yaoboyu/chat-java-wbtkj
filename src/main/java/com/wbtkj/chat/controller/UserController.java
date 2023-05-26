package com.wbtkj.chat.controller;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.user.ChangePwdVO;
import com.wbtkj.chat.service.CDKEYService;
import com.wbtkj.chat.service.SendVerifyCodeService;
import com.wbtkj.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/user")
@ResponseBody
public class UserController {
    @Resource
    UserService userService;
    @Resource
    CDKEYService cdkeyService;
    @Resource
    SendVerifyCodeService sendVerifyCodeService;

    @PutMapping("/pwd")
    public Result changePwd(@RequestBody ChangePwdVO changePwdVO) {
        String codeByEmail = sendVerifyCodeService.getCodeByEmail(ThreadLocalConfig.getUser().getEmail());
        if(codeByEmail == null || StringUtils.isBlank(codeByEmail)) {
            throw new MyServiceException("验证码过期！");
        }
        if(!codeByEmail.equals(changePwdVO.getCode())) {
            throw new MyServiceException("验证码错误！");
        }

        userService.changePwd(changePwdVO.getPwd());
        return Result.success();
    }

    @PostMapping("/recharge/{cdkey}")
    Result verification(@PathVariable String cdkey) {
        long value = cdkeyService.activate(cdkey);
        return Result.success(value);
    }

    @GetMapping("/recharge/history")
    Result getHistory() {
        return Result.success(cdkeyService.getRechargeRecord());
    }

    @GetMapping("/info")
    Result info() {
        return Result.success(userService.getUserInfo());
    }
}
