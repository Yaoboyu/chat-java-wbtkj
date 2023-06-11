package com.wbtkj.chat.config;

import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.pojo.model.Role;
import com.wbtkj.chat.pojo.model.RoleExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
    RedisTemplate redisTemplate;

    // 添加定时任务
    @Scheduled(fixedRate=10, timeUnit = TimeUnit.MINUTES)
    public void configureTasks() {
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
}