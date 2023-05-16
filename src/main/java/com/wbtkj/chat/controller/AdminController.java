package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.user.UserLoginVO;
import com.wbtkj.chat.service.AdminService;
import com.wbtkj.chat.service.CDKEYService;
import com.wbtkj.chat.service.UserService;
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

    @PostMapping("/login")
    public Result adminLogin(@RequestBody UserLoginVO userLoginVO){
        log.info("管理员登录");
        return Result.success(userService.login(userLoginVO.getEmail(), userLoginVO.getPwd()));
    }

    @GetMapping("/user")
    Result getUsers(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam String email){
        log.info("模糊分页查询");
        return Result.success(userService.getUsersByPage(page, pageSize, email));
    }

    @PutMapping("/user")
    Result updateStatus(@RequestBody User user){
        log.info("修改用户信息");
        userService.updateUser(user);
        return Result.success();
    }

    @GetMapping("/cdkey/{num}/{value}")
    Result getCDKEYS(@PathVariable int num, @PathVariable long value){
        log.info("准备发放{}张卡密,价值{}",num,value);
        return Result.success(cdkeyService.publish(num, value));
    }

    @GetMapping("/cdkey/info/{cdkey}")
    Result getCDKEYS(@PathVariable String cdkey){
        return Result.success(cdkeyService.getCdkeyInfo(cdkey));
    }

    @PostMapping("/openaikey/{key}")
    public Result addOpenAiKey(@PathVariable String key){
        log.info("添加openaikey");
        return Result.success(adminService.addOpenAiKey(key));
    }

    @DeleteMapping("/openaikey/{key}")
    public Result delOpenAiKey(@PathVariable String key){
        log.info(" 删除openaikey");
        return Result.success(adminService.delOpenAiKey(key));
    }

    @PutMapping("/openaikey/{key}/{status}")
    public Result changeStatus(String key,int status){
        log.info("修改key状态");
        return Result.success(adminService.changeStatus(key,status));
    }

    @GetMapping("/openaikey")
    public Result getKey(){
        log.info("获取所有openaikey");
        return Result.success(adminService.getKeys());
    }

}
