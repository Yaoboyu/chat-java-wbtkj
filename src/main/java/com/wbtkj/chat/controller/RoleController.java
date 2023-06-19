package com.wbtkj.chat.controller;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.pojo.vo.role.RoleInfoVO;
import com.wbtkj.chat.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/role")
@ResponseBody
public class RoleController {
    @Resource
    RoleService roleService;


    @GetMapping("")
    Result info() {
        return Result.success(roleService.getUserRole());
    }

    @GetMapping("/history/{roleId}")
    Result roleHistory(@PathVariable Long roleId) {
        return Result.success(roleService.getRoleHistory(roleId));
    }

    @GetMapping("/session/{chatSessionId}")
    Result roleSession(@PathVariable String chatSessionId) {
        return Result.success(roleService.getChatSessionById(chatSessionId, ThreadLocalConfig.getUser().getId()));
    }

    @PostMapping("")
    Result addRole(@RequestBody RoleInfoVO roleInfoVO) {
        return Result.success(roleService.addRole(roleInfoVO));
    }

    @PostMapping("/{roleId}")
    Result addRoleById(@PathVariable Long roleId) {
        return Result.success(roleService.addRoleById(roleId));
    }

    @PutMapping("")
    Result updateRole(@RequestBody RoleInfoVO roleInfoVO) {
        return Result.success(roleService.updateRole(roleInfoVO));
    }

    @DeleteMapping("/{roleId}")
    Result deleteRole(@PathVariable Long roleId) {
        return Result.success(roleService.deleteRole(roleId));
    }

    @GetMapping("/market")
    Result getShopRole(@RequestParam Integer page,
                       @RequestParam Integer pageSize,
                       @RequestParam(required = false) Integer type,
                       @RequestParam(required = false) String name) {
        return Result.success(roleService.getShopRole(page, pageSize, type, name));
    }

    @GetMapping("/market/page")
    Result getShopPage(@RequestParam Integer pageSize,
                       @RequestParam(required = false) Integer type,
                       @RequestParam(required = false) String name) {
        return Result.success(roleService.getShopPage(pageSize, type, name));
    }

    @PostMapping("/shelf")
    Result shelfRole(@RequestBody RoleInfoVO roleInfoVO) {
        return Result.success(roleService.shelfRole(roleInfoVO));
    }
}
