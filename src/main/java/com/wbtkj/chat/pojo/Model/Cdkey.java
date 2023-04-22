package com.wbtkj.chat.pojo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CDKEY
 */
@Data
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