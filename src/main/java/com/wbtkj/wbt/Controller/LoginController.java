package com.wbtkj.wbt.Controller;

import com.wbtkj.wbt.Pojo.Result;
import com.wbtkj.wbt.Service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    LoginService loginService;
    @GetMapping("/{email}/{pwd}")
    public Result Login(@PathVariable String email,@PathVariable String pwd)throws Exception{
        log.info("用户{}登录,密码{}",email,pwd);
        String jwt = loginService.jwt(email,pwd);
        return Result.success(jwt);
    }
}
