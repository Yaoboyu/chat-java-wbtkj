package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;
import com.wbtkj.chat.pojo.model.UserInfo;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.admin.AdminLoginVO;
import com.wbtkj.chat.pojo.vo.admin.RoleCheckVO;
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
    RoleService roleService;

    @PostMapping("/login")
    public Result adminLogin(@RequestBody AdminLoginVO adminLoginVO){
        return Result.success(adminService.login(adminLoginVO.getUsername(), adminLoginVO.getPassword()));
    }

    @GetMapping("/user/page")
    Result getUsersPage(@RequestParam int pageSize,
                    @RequestParam(required = false) String email){
        return Result.success(userService.getUsersPage(pageSize, email));
    }

    @GetMapping("/user")
    Result getUsers(@RequestParam int page,
                    @RequestParam int pageSize,
                    @RequestParam(required = false) String email){
        return Result.success(userService.getUsersByPage(page, pageSize, email));
    }

    @PutMapping("/user")
    Result updateStatus(@RequestBody UserInfo userInfo){
        userService.updateUser(userInfo);
        return Result.success();
    }

    @PostMapping("/cdkey/{type}/{num}/{value}")
    Result getCDKEYS(@PathVariable int type, @PathVariable int num, @PathVariable int value){
        return Result.success(cdkeyService.publish(type, num, value));
    }

    @GetMapping("/cdkey/info/{cdkey}")
    Result getCDKEYS(@PathVariable String cdkey){
        return Result.success(cdkeyService.getRechargeRecord(cdkey));
    }

    @PostMapping("/thirdPartyModelKey")
    public Result addOpenAiKey(@RequestBody ThirdPartyModelKey thirdPartyModelKeyVO){
        return Result.success(thirdPartyModelKeyService.addKey(thirdPartyModelKeyVO));
    }

    @DeleteMapping("/thirdPartyModelKey/{id}")
    public Result delOpenAiKey(@PathVariable Long id){
        return Result.success(thirdPartyModelKeyService.delKey(id));
    }

    @PutMapping("/thirdPartyModelKey/{id}/{status}")
    public Result changeStatus(@PathVariable long id, @PathVariable int status){
        return Result.success(thirdPartyModelKeyService.changeStatus(id, status));
    }

    @GetMapping("/thirdPartyModelKey")
    public Result getKey(){
        return Result.success(thirdPartyModelKeyService.getAllKey());
    }

    @GetMapping("/role/check")
    public Result getRoleCheck(){
        return Result.success(roleService.getRoleCheck());
    }

    @PostMapping("/role/check")
    public Result updateRoleCheck(@RequestBody RoleCheckVO roleCheckVO){
        return Result.success(roleService.updateRoleCheck(roleCheckVO));
    }
}
