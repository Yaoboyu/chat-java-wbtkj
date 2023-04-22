package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.VO.Result;
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
    @PutMapping("/pwd/{email}/{password}")
    public Result ChangePwd(@PathVariable String email,@PathVariable String password) throws Exception{
        log.info("用户{}尝试修改密码",email);
        userService.ChangePwd(email,password);
        return Result.success();
    }
}
