package com.wbtkj.chat.pojo.vo.admin;

import cn.hutool.core.bean.BeanUtil;
import com.wbtkj.chat.pojo.model.RechargeRecord;
import com.wbtkj.chat.pojo.model.UserInfo;
import lombok.Data;

import java.util.List;

@Data
public class AdminUserInfoVO extends UserInfo {
    private List<RechargeRecord> rechargeHistorys;
}
