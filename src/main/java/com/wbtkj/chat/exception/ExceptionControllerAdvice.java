package com.wbtkj.chat.exception;

import com.wbtkj.chat.pojo.VO.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//ExceptionControllerAdvice
//这个类是专门处理控制器发生的异常的
//@RestControllerAdvice表示当前类是处理控制器通知功能的
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    //编写处理异常的方法
    //每个方法可以处理一种异常,可以编写多个方法
    //@ExceptionHandler表示下面方式专门处理异常
    // 如果控制器中发生的异常类型和这个方法的参数匹配,就运行这个方法
    @ExceptionHandler
    @ResponseBody
    public Result handlerMyServiceException(MyServiceException e){
        log.error("业务异常：{}",e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    public Result handlerMyException(MyException e){
        log.error("程序异常：{}",e.getMessage());
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    // 这里处理异常的父类Exception,表示出现任何异常,当前类都处理
    @ExceptionHandler
    @ResponseBody
    public Result handlerException(Exception e){
        log.error("其它异常",e);
        return Result.error(e.getMessage());
    }



}
