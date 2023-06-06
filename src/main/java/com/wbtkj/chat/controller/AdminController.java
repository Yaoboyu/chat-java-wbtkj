package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.model.UserInfo;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.admin.AdminLoginVO;
import com.wbtkj.chat.service.*;
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
    @Resource
    UserService userService;
    @Resource
    ThirdPartyModelKeyService thirdPartyModelKeyService;
    @Resource
    SendVerifyCodeService sendVerifyCodeService;

    @PostMapping("/login")
    public Result adminLogin(@RequestBody AdminLoginVO adminLoginVO){
        log.info("管理员登录");
        return Result.success(adminService.login(adminLoginVO.getUsername(), adminLoginVO.getPassword()));
        //return Result.error("测试");
    }

    @GetMapping("/user")
    Result getUsers(@RequestParam int page,
                    @RequestParam int pageSize,
                    @RequestParam(required = false) String email){
        log.info("模糊分页查询");
        return Result.success(userService.getUsersByPage(page, pageSize, email));
    }

    @PutMapping("/user")
    Result updateStatus(@RequestBody UserInfo userInfo){
        log.info("修改用户信息");
        userService.updateUser(userInfo);
        return Result.success();
    }

    @PostMapping("/cdkey/{type}/{num}/{value}")
    Result getCDKEYS(@PathVariable int type, @PathVariable int num, @PathVariable int value){
        log.info("准备发放{}张卡密,价值{}",num,value);
        return Result.success(cdkeyService.publish(type, num, value));
    }

    @GetMapping("/cdkey/info/{cdkey}")
    Result getCDKEYS(@PathVariable String cdkey){
        return Result.success(cdkeyService.getRechargeRecord(cdkey));
    }

    @PostMapping("/thirdPartyModelKey/{key}/{model}")
    public Result addOpenAiKey(@PathVariable String key, @PathVariable String model){
        log.info("添加key");
        return Result.success(thirdPartyModelKeyService.addKey(key, model));
    }

    @DeleteMapping("/thirdPartyModelKey/{id}")
    public Result delOpenAiKey(@PathVariable Long id){
        log.info(" 删除key");
        return Result.success(thirdPartyModelKeyService.delKey(id));
    }

    @PutMapping("/thirdPartyModelKey/{id}/{status}")
    public Result changeStatus(@PathVariable long id, @PathVariable int status){
        log.info("修改key状态");
        return Result.success(thirdPartyModelKeyService.changeStatus(id, status));
    }

    @GetMapping("/thirdPartyModelKey")
    public Result getKey(){
        log.info("获取所有key");
        return Result.success(thirdPartyModelKeyService.getAllKey());
    }

}
