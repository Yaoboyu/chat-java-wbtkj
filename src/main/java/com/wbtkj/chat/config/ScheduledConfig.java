package com.wbtkj.chat.config;

import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.pojo.model.Role;
import com.wbtkj.chat.pojo.model.RoleExample;
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

@Configuration
@EnableScheduling   // 开启定时任务
public class ScheduledConfig {
    @Resource
    RoleMapper roleMapper;
    @Resource
    RedisTemplate redisTemplate;

    // 添加定时任务
    @Scheduled(fixedRate=2, timeUnit = TimeUnit.HOURS)
    public void configureTasks() {
        Set<String> keys = redisTemplate.keys(RedisKeyConstant.role.getKey() + "*");
        List<Role> values = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(values)) {
            return;
        }
        for(Role r : values){
            roleMapper.updateByPrimaryKey(r);
        }
    }
}