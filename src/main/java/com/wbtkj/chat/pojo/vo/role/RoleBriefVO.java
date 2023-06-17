package com.wbtkj.chat.pojo.vo.role;

import com.wbtkj.chat.pojo.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class RoleBriefVO {
    private Long id;

//    private String email;

    private String avatar;

    private String nickname;

    private String greeting;

    private String model;

    private Integer marketType;

    private Integer likes;

    private Integer hot;

    private Boolean official;

    private Date updateTime;

    public RoleBriefVO(Role role) {
        this.id = role.getId();
        this.avatar = role.getAvatar();
        this.nickname = role.getNickname();
        this.greeting = role.getGreeting();
        this.model = role.getModel();
        this.marketType = role.getMarketType();
        this.likes = role.getLikes();
        this.hot = role.getHot();
        this.official = role.getUserId() == 0;
        this.updateTime = role.getUpdateTime();
    }
}
