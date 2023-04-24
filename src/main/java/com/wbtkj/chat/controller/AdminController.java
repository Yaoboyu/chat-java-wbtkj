package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.AdminService;
import com.wbtkj.chat.service.CDKEYService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin")
@ResponseBody
public class AdminController {
    @Resource
    AdminService adminService;
    @Resource
    CDKEYService cdkeyService;

    @PutMapping("/user")
    Result updateStatus(@RequestBody User user){
        adminService.updateUser(user);
        return Result.success();
    }

    @GetMapping("cdkey/{num}/{value}")
    Result getCDKEYS(@PathVariable int num, @PathVariable long value){
        log.info("准备发放{}张卡密,价值{}",num,value);
        return Result.success(cdkeyService.publish(num, value));
    }
}
