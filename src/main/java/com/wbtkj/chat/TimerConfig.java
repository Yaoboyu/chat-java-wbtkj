package com.wbtkj.chat;

import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.pojo.model.Role;
import com.wbtkj.chat.pojo.model.RoleExample;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class TimerConfig {
    @Resource
    RoleMapper roleMapper;
    @Resource
    RedisTemplate redisTemplate;
    //3.添加定时任务
    @Scheduled(fixedRate=1000*60*60)
    private void configureTasks() {
        Set<String> keys = redisTemplate.keys("Role:*");
        List<Role> values = redisTemplate.opsForValue().multiGet(keys);
        for(Role r : values){
            roleMapper.updateByPrimaryKey(r);
        }
    }
}