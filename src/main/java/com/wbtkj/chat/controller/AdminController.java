package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.Model.User;
import com.wbtkj.chat.pojo.VO.Result;
import com.wbtkj.chat.service.AdminService;
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
    @PutMapping()
    Result updateStatus(@RequestBody User user){
        log.info("更新用户{}状态为{}", user.getEmail(), user.getStatus());
        adminService.updateStatus(user.getEmail(), user.getStatus());
        return Result.success();
    }
}
