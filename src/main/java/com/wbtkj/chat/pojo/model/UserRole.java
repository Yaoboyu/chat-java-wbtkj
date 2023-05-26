package com.wbtkj.chat.pojo.model;

import java.io.Serializable;

public class UserRole implements Serializable {
    private Long id;

    private Long userId;

    private Long roleId;

    private Integer freeBalance;

    private Integer used;

    private Integer status;

    private Boolean top;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance(Integer freeBalance) {
        this.freeBalance = freeBalance;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", roleId=").append(roleId);
        sb.append(", freeBalance=").append(freeBalance);
        sb.append(", used=").append(used);
        sb.append(", status=").append(status);
        sb.append(", top=").append(top);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}