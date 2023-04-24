package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.user.ChangePwdVO;
import com.wbtkj.chat.service.CDKEYService;
import com.wbtkj.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

    @PutMapping("/pwd")
    public Result changePwd(@RequestBody ChangePwdVO changePwdVO) throws Exception{
        userService.changePwd(changePwdVO.getPwd(), changePwdVO.getCode());
        return Result.success();
    }

    @PostMapping("/recharge/{cdkey}")
    Result verification(@PathVariable String cdkey) {
        // TODO：用户余额修改
        long value = cdkeyService.activate(cdkey);
        return Result.success(value);
    }
}
