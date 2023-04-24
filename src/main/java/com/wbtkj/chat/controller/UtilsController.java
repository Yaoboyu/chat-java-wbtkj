package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.SendVerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@ResponseBody
public class UtilsController {

    @Resource
    private SendVerifyCodeService sendVerifyCodeService;

    @GetMapping("/sendMail/{email}")
    public Result sendMail(@PathVariable String email) {
        sendVerifyCodeService.sendMail(email);
        return Result.success();
    }
}
