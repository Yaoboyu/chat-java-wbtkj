package com.wbtkj.chat.pojo.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Long id;

    private String email;

    private String pwd;

    private String salt;

    private Integer status;

    private Long quota;

    private Long cost;

    private String remark;

    private String my_inv_code;

    private String use_inv_code;

    private Date create_time;

    private Date update_time;

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

    public Long getQuota() {
        return quota;
    }

    public void setQuota(Long quota) {
        this.quota = quota;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getMy_inv_code() {
        return my_inv_code;
    }

    public void setMy_inv_code(String my_inv_code) {
        this.my_inv_code = my_inv_code == null ? null : my_inv_code.trim();
    }

    public String getUse_inv_code() {
        return use_inv_code;
    }

    public void setUse_inv_code(String use_inv_code) {
        this.use_inv_code = use_inv_code == null ? null : use_inv_code.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
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
        sb.append(", quota=").append(quota);
        sb.append(", cost=").append(cost);
        sb.append(", remark=").append(remark);
        sb.append(", my_inv_code=").append(my_inv_code);
        sb.append(", use_inv_code=").append(use_inv_code);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}