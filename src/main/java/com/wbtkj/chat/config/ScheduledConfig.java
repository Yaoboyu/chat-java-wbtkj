package com.wbtkj.chat.config;

import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.mapper.UserInfoMapper;
import com.wbtkj.chat.pojo.model.Role;
import com.wbtkj.chat.pojo.model.RoleExample;
import com.wbtkj.chat.pojo.model.UserInfo;
import com.wbtkj.chat.pojo.model.UserInfoExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling   // 开启定时任务
@Slf4j
public class ScheduledConfig {
    @Resource
    RoleMapper roleMapper;
    @Resource
    UserInfoMapper userInfoMapper;
    @Resource
    RedisTemplate redisTemplate;

    // 刷新role
    @Scheduled(fixedRate=30, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void setRole() {
        log.info("开始刷新role...");
        Set<String> keys = redisTemplate.keys(RedisKeyConstant.role.getKey() + "*");
        List<Role> roles = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(roles)) {
            return;
        }
        log.info("正在刷新role ids: {}", roles.stream().map(Role::getId).collect(Collectors.toList()).toString());
        for(Role r : roles){
            roleMapper.updateByPrimaryKey(r);
        }
        log.info("结束刷新role");
    }

    // 每天凌晨赠送余额
    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT+8")
    @Transactional
    public void giftBalance() {
        log.info("开始赠送余额...");
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andBalanceLessThan(GeneralConstant.USER_GIFT_BALANCE);
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        log.info("正在赠送余额：一共{}个账号", userInfos.size());

        for (UserInfo userInfo : userInfos) {
            userInfo.setBalance(GeneralConstant.USER_GIFT_BALANCE);
            userInfoMapper.updateByPrimaryKey(userInfo);
        }
        log.info("赠送余额结束 ...");
    }
}