package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.SendVerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/utils")
public class UtilsController {

    @Resource
    private SendVerifyCodeService sendVerifyCodeService;

    @GetMapping("/sendMail/{email}")
    public Result sendMail(@PathVariable String email) {
        sendVerifyCodeService.sendMail(email);
        return Result.success();
    }
}
