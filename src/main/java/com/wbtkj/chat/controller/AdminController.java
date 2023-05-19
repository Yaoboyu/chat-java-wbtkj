package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.admin.AdminLoginVO;
import com.wbtkj.chat.service.AdminService;
import com.wbtkj.chat.service.CDKEYService;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
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
    @Resource
    ThirdPartyModelKeyService thirdPartyModelKeyService;

    @PostMapping("/login")
    public Result adminLogin(@RequestBody AdminLoginVO adminLoginVO){
        log.info("管理员登录");
        //TODO：几乎所有的请求失败的msg都会出现乱码，看下是什么原因
        return Result.success(adminService.login(adminLoginVO.getUsername(), adminLoginVO.getPassword()));
        //return Result.error("测试");
    }

    @GetMapping("/user")
    Result getUsers(@RequestParam int page, @RequestParam int pageSize, @RequestParam String email){
        log.info("模糊分页查询");
        // TODO：给每个用户添加充值历史，详见apifox
        return Result.success(userService.getUsersByPage(page, pageSize, email));
    }

    @PutMapping("/user")
    Result updateStatus(@RequestBody User user){
        log.info("修改用户信息");
        userService.updateUser(user);
        return Result.success();
    }

    @GetMapping("/cdkey/{num}/{value}")
    Result getCDKEYS(@PathVariable int num, @PathVariable int value){
        log.info("准备发放{}张卡密,价值{}",num,value);
        return Result.success(cdkeyService.publish(num, value));
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
