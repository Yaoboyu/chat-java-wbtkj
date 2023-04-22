package com.wbtkj.chat.controller;

import com.wbtkj.chat.pojo.Model.Cdkey;
import com.wbtkj.chat.pojo.VO.Result;
import com.wbtkj.chat.service.CDKEYService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/cdkey")
@ResponseBody
public class CDKEYController {
    @Resource
    CDKEYService cdkeyService;
    @GetMapping("/{num}/{value}")
    Result getCDKEYS(@PathVariable int num, @PathVariable int value){
        log.info("准备发放{}张卡密,价值{}",num,value);
        return Result.success(cdkeyService.publish(num,value));
    }
    @PostMapping("")
    Result verification(@RequestBody Cdkey CDKEY) throws Exception{
        log.info("用户{}激活卡密{}",CDKEY.getUserId(),CDKEY.getCode());
        int value = cdkeyService.activate(CDKEY);
        return Result.success("激活成功,额度增加" + value);
    }
}
