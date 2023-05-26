package com.wbtkj.chat.pojo.vo.user;

import com.wbtkj.chat.pojo.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    private Long id;

    private String email;

    private Integer status;

    private Date vipStartTime;

    private Date vipEndTime;

    private Integer balance;

    private Double cash;

    private String remark;

    private String myInvCode;

    private String useInvCode;

    private Date createTime;

    private Date updateTime;

    public UserInfo(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.vipStartTime = user.getVipStartTime();
        this.vipEndTime = user.getVipEndTime();
        this.balance = user.getBalance();
        this.cash = user.getCash();
        this.remark = user.getRemark();
        this.myInvCode = user.getMyInvCode();
        this.useInvCode = user.getUseInvCode();
        this.createTime = user.getCreateTime();
        this.updateTime = user.getUpdateTime();
    }
}
