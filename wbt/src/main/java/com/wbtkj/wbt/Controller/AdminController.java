package com.wbtkj.wbt.Controller;

import com.wbtkj.wbt.Pojo.Result;
import com.wbtkj.wbt.Pojo.UserInfo;
import com.wbtkj.wbt.Service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @PutMapping()
    Result updateStatus(@RequestBody UserInfo userInfo){
        log.info("更新用户{}状态为{}",userInfo.getEmail(),userInfo.isStatus());
        adminService.updateStatus(userInfo.getEmail(),userInfo.isStatus());
        return Result.success();
    }
}
