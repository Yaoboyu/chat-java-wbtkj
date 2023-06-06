package com.wbtkj.chat.pojo.vo.user;

import com.wbtkj.chat.pojo.model.UserInfo;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVO {
    private Long id;

    private String email;

    private Integer status;

    private Date vipStartTime;

    private Date vipEndTime;

    private Integer balance;

    private Double cash;

    private String remark;

    private String myInvCode;

    private Long useInvCode;

    private Date createTime;

    private Date updateTime;

    public UserInfoVO(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.email = userInfo.getEmail();
        this.status = userInfo.getStatus();
        this.vipStartTime = userInfo.getVipStartTime();
        this.vipEndTime = userInfo.getVipEndTime();
        this.balance = userInfo.getBalance();
        this.cash = userInfo.getCash();
        this.remark = userInfo.getRemark();
        this.myInvCode = userInfo.getMyInvCode();
        this.useInvCode = userInfo.getUseInvCode();
        this.createTime = userInfo.getCreateTime();
        this.updateTime = userInfo.getUpdateTime();
    }
}
