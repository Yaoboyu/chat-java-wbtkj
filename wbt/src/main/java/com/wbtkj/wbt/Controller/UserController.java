package com.wbtkj.wbt.Controller;

import com.wbtkj.wbt.Pojo.Result;
import com.wbtkj.wbt.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PutMapping("/pwd/{email}/{password}")
    public Result ChangePwd(@PathVariable String email,@PathVariable String password) throws Exception{
        log.info("用户{}尝试修改密码",email);
        userService.ChangePwd(email,password);
        return Result.success();
    }
}
