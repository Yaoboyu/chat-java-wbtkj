package com.wbtkj.chat.pojo.vo.role;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.model.Role;
import com.wbtkj.chat.utils.MyUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private String marketStatus;

    private Integer marketType;

    private List<String> fileNames;

    private Long originRoleId;



    /**
     * 修改
     * @return
     */
    private Role toRole() {
        if (!MyUtils.checkModel(this.model)) {
            throw new MyServiceException("模型不可用");
        }

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
        role.setMarketStatus(this.marketStatus);
        role.setFileNames(this.fileNames);
        role.setMarketType(this.marketType);
        role.setUpdateTime(MyUtils.getTimeGMT8());
        return role;
    }

    /**
     * 新增
     * @param userId
     * @return
     */
    public Role newRole(Long userId) {
        if (avatar == null || nickname == null || greeting == null
                || model == null || system == null || contextN == null || maxTokens == null
                || temperature == null || topP == null || frequencyPenalty == null
                || presencePenalty == null || isMarket == null) {
            throw new MyServiceException("缺少参数");
        }

        Role role = toRole();
        role.setId(null);
        role.setUserId(userId);
        role.setStop(null);
        role.setIsMarket(false);
        role.setMarketType(null);
        role.setMarketStatus(null);
        role.setLikes(0);
        role.setHot(0);
        role.setCreateTime(MyUtils.getTimeGMT8());
        return role;
    }

    /**
     * 修改
     * @return
     */
    public Role updateRole() {
        if (this.id == null) {
            throw new MyServiceException("id不能为空");
        }

        Role role = toRole();
        // 不能修改的
        role.setIsMarket(null);
        role.setMarketType(null);
        role.setMarketStatus(null);
        return role;
    }

    /**
     * 上架的角色
     * @return
     */
    public Role shelfRole(Long userId) {
        if (avatar == null || nickname == null || greeting == null
                || model == null || system == null || contextN == null || maxTokens == null
                || temperature == null || topP == null || frequencyPenalty == null
                || presencePenalty == null || isMarket == null || marketType == null || originRoleId == null) {
            throw new MyServiceException("缺少参数");
        }

        Role role = toRole();
        role.setId(null);
        role.setUserId(userId);
        role.setStop(null);
        role.setIsMarket(true);
        role.setMarketStatus(null);
        role.setOriginRoleId(this.originRoleId);
        role.setLikes(0);
        role.setHot(0);
        role.setCreateTime(MyUtils.getTimeGMT8());
        return role;
    }
}
