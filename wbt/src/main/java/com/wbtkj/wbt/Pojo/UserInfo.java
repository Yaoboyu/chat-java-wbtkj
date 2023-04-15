package com.wbtkj.wbt.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * UserInfo
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo {
    /**
     * 已消耗额度，默认0
     */
    private double cost = 0;
    /**
     * 邮箱
     */
    private String email;
    /**
     * id，自增主键
     */
    private long id;
    /**
     * 密码，加盐后并md5加密后的密码
     */
    private String pwd;
    /**
     * 额度，订阅套餐额度,默认10000
     */
    private long quota = 10000;
    /**
     * 注册时间，yyyyMMddHHmmss格式
     */
    private Date regDate;
    /**
     * 备注信息，默认null
     */
    private String remark = null;
    /**
     * 预设ai模型参数，默认null
     */
    private Map<String, Object>[] roles = null;
    /**
     * 密码盐，固定5位字符串
     */
    private String salt;
    /**
     * 账户状态，0有效,1封禁,默认0
     */
    private boolean status = false;
}