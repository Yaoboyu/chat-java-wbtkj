package com.wbtkj.chat.pojo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Role implements Serializable {
    private Long id;

    private Long userId;

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

    private String stop;

    private Boolean isMarket;

    private String marketStatus;

    private Integer marketType;

    private Long originRoleId;

    private List<String> fileNames;

    private Integer likes;

    private Integer hot;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting == null ? null : greeting.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system == null ? null : system.trim();
    }

    public Integer getContextN() {
        return contextN;
    }

    public void setContextN(Integer contextN) {
        this.contextN = contextN;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public Double getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public String getLogitBias() {
        return logitBias;
    }

    public void setLogitBias(String logitBias) {
        this.logitBias = logitBias == null ? null : logitBias.trim();
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop == null ? null : stop.trim();
    }

    public Boolean getIsMarket() {
        return isMarket;
    }

    public void setIsMarket(Boolean isMarket) {
        this.isMarket = isMarket;
    }

    public String getMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus) {
        this.marketStatus = marketStatus == null ? null : marketStatus.trim();
    }

    public Integer getMarketType() {
        return marketType;
    }

    public void setMarketType(Integer marketType) {
        this.marketType = marketType;
    }

    public Long getOriginRoleId() {
        return originRoleId;
    }

    public void setOriginRoleId(Long originRoleId) {
        this.originRoleId = originRoleId;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", avatar=").append(avatar);
        sb.append(", nickname=").append(nickname);
        sb.append(", greeting=").append(greeting);
        sb.append(", model=").append(model);
        sb.append(", system=").append(system);
        sb.append(", contextN=").append(contextN);
        sb.append(", maxTokens=").append(maxTokens);
        sb.append(", temperature=").append(temperature);
        sb.append(", topP=").append(topP);
        sb.append(", frequencyPenalty=").append(frequencyPenalty);
        sb.append(", presencePenalty=").append(presencePenalty);
        sb.append(", logitBias=").append(logitBias);
        sb.append(", stop=").append(stop);
        sb.append(", isMarket=").append(isMarket);
        sb.append(", marketStatus=").append(marketStatus);
        sb.append(", marketType=").append(marketType);
        sb.append(", originRoleId=").append(originRoleId);
        sb.append(", fileNames=").append(fileNames);
        sb.append(", likes=").append(likes);
        sb.append(", hot=").append(hot);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}