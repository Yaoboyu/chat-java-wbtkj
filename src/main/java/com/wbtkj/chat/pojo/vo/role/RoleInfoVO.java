package com.wbtkj.chat.pojo.vo.role;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleInfoVO {
    private Long id;

    private String avatar;

    private String nickname;

    private String greeting;

    private String model;

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

    private List<String> fileNames;

    /**
     * 新增
     * @param userId
     * @return
     */
    public Role toRole(Long userId) {
        if (avatar == null || nickname == null || greeting == null
                || model == null || system == null || contextN == null || maxTokens == null
                || temperature == null || topP == null || frequencyPenalty == null
                || presencePenalty == null || isMarket == null || ( isMarket != null && marketType == null)) {
            throw new MyServiceException("缺少参数");
        }

        Role role = toRole();
        role.setId(null);
        role.setUserId(userId);
        role.setStop("#####");
        role.setLikes(0);
        role.setHot(0);
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        return role;
    }

    /**
     * 修改
     * @return
     */
    public Role toRole() {
        Role role = new Role();
        role.setId(this.id);
        role.setAvatar(this.avatar);
        role.setNickname(this.nickname);
        role.setGreeting(this.greeting);
        role.setModel(this.model);
        role.setSystem(this.system);
        role.setContextN(this.contextN);
        role.setMaxTokens(this.maxTokens);
        role.setTemperature(this.temperature);
        role.setTopP(this.topP);
        role.setFrequencyPenalty(this.frequencyPenalty);
        role.setPresencePenalty(this.presencePenalty);
        role.setLogitBias(this.logitBias);
        role.setIsMarket(this.isMarket);
        role.setFileNames(this.fileNames);
        role.setMarketType(this.marketType);
        role.setUpdateTime(new Date());
        return role;
    }
}
