package com.wbtkj.chat.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    /**
     * 结果，成功为1,失败为0
     */
    private Integer code;
    /**
     * 返回结果，返回结果
     */
    private Object data;
    /**
     * 备注信息，默认成功为"success"
     */
    private String msg;
    public static Result success(){
        return new Result(1,null,"success");
    }
    public static Result success(Object obj){
        return new Result(1,obj,"success");
    }
    public static Result error(){
        return new Result(0,null,"error");
    }
    public static Result error(String s){
        return new Result(0,null,s);
    }
}