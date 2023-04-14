package com.wbtkj.wbt.Controller;

import com.wbtkj.wbt.Pojo.Cdkey;
import com.wbtkj.wbt.Pojo.Result;
import com.wbtkj.wbt.Service.CDKEYService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cdkey")
public class CDKEYController {
    @Autowired
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
