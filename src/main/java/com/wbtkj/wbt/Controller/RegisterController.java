package com.wbtkj.wbt.Controller;

import com.wbtkj.wbt.Pojo.Result;
import com.wbtkj.wbt.Pojo.UserInfo;
import com.wbtkj.wbt.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/register")
public class RegisterController {
    @Resource
    private UserService userService;
    @PostMapping()
    public Result register(@RequestBody UserInfo userInfo) throws Exception{
        log.info("保存用户{}",userInfo.getEmail());
        userService.save(userInfo);
        return Result.success();
    }
}
