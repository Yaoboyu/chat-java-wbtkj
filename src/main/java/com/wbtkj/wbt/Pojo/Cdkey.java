package com.wbtkj.wbt.Pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CDKEY
 */
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Cdkey {
    /**
     * 卡密（20位随机大小写数字组合）
     */
    private String code;
    /**
     * 自增主键
     */
    private long userId;
}