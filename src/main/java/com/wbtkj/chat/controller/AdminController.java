package com.wbtkj.chat.controller;

import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.pojo.dto.user.UserDTO;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.user.LoginVO;
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

    @PutMapping("/user")
    Result updateStatus(@RequestBody User user){
        userService.updateUser(user);
        return Result.success();
    }
    @GetMapping("cdkey/{num}/{value}")
    Result getCDKEYS(@PathVariable int num, @PathVariable long value){
        log.info("准备发放{}张卡密,价值{}",num,value);
        return Result.success(cdkeyService.publish(num, value));
    }
    @GetMapping("/user")
    Result getUsers(@RequestParam Integer page,@RequestParam Integer pagesize,@RequestParam String email){
        log.info("模糊分页查询");
        return Result.success(adminService.getUsers(page,pagesize,email));
    }
    @PostMapping("/{key}")
    public Result addOpenAiKey(@PathVariable String key){
        log.info("添加openaikey");
        return Result.success(adminService.addOpenAiKey(key));
    }
    @DeleteMapping("/{key}")
    public Result delOpenAiKey(@PathVariable String key){
        log.info(" 删除openaikey");
        return Result.success(adminService.delOpenAiKey(key));
    }
    @PutMapping("/{key}/{status}")
    public Result changeStatus(String key,int status){
        log.info("修改key状态");
        return Result.success(adminService.changeStatus(key,status));
    }
    @GetMapping("openaikey")
    public Result getKey(){
        log.info("获取所有openaikey");
        return Result.success(adminService.getKeys());
    }
    @PostMapping("/login")
    public Result adminLogin(@RequestBody LoginVO loginVO){
        log.info("管理员登录");
        return Result.success(userService.login(loginVO.getEmail(),loginVO.getPwd()));
    }
}
