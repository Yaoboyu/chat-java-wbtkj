package com.wbtkj.chat.pojo.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Long id;

    private String email;

    private String pwd;

    private String salt;

    private Integer status;

    private Date vipStartTime;

    private Date vipEndTime;

    private Integer balance;

    private String remark;

    private String myInvCode;

    private String useInvCode;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getVipStartTime() {
        return vipStartTime;
    }

    public void setVipStartTime(Date vipStartTime) {
        this.vipStartTime = vipStartTime;
    }

    public Date getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(Date vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getMyInvCode() {
        return myInvCode;
    }

    public void setMyInvCode(String myInvCode) {
        this.myInvCode = myInvCode == null ? null : myInvCode.trim();
    }

    public String getUseInvCode() {
        return useInvCode;
    }

    public void setUseInvCode(String useInvCode) {
        this.useInvCode = useInvCode == null ? null : useInvCode.trim();
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
        sb.append(", email=").append(email);
        sb.append(", pwd=").append(pwd);
        sb.append(", salt=").append(salt);
        sb.append(", status=").append(status);
        sb.append(", vipStartTime=").append(vipStartTime);
        sb.append(", vipEndTime=").append(vipEndTime);
        sb.append(", balance=").append(balance);
        sb.append(", remark=").append(remark);
        sb.append(", myInvCode=").append(myInvCode);
        sb.append(", useInvCode=").append(useInvCode);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}