package com.wbtkj.chat.pojo.vo.role;

import lombok.Data;

@Data
public class RoleInfoVO {
    private Long id;

    private String avatar;

    private String nickname;

    private String greeting;

    private Integer model;

    private String system;

    private Integer contextN;

    private Integer maxTokens;

    private Double temperature;

    private Double topP;

    private Double frequencyPenalty;

    private Double presencePenalty;

    private String logitBias;

    private Boolean isMarket;

    private Integer marketType;
}
