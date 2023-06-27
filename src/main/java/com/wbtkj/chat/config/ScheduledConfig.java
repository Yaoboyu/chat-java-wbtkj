package com.wbtkj.chat.config;

import com.mongodb.client.result.DeleteResult;
import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.mapper.UserInfoMapper;
import com.wbtkj.chat.pojo.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
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
    @Resource
    private MongoTemplate mongoTemplate;

    // 刷新role
    @Scheduled(fixedRate=30, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void setRole() {
        log.info("start refresh role...");
        Set<String> keys = redisTemplate.keys(RedisKeyConstant.role.getKey() + "*");
        List<Role> roles = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(roles)) {
            return;
        }
        log.info("refresh role ids: {}", roles.stream().map(Role::getId).collect(Collectors.toList()).toString());
        for(Role r : roles){
            roleMapper.updateByPrimaryKey(r);
        }
        log.info("end refresh role");
    }

    // 每天凌晨赠送余额
    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT+8")
    @Transactional
    public void giftBalance() {
        log.info("start gift balance...");
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andBalanceLessThan(GeneralConstant.USER_GIFT_BALANCE);
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        log.info("gift balance sum: {}", userInfos.size());

        for (UserInfo userInfo : userInfos) {
            userInfo.setBalance(GeneralConstant.USER_GIFT_BALANCE);
            userInfoMapper.updateByPrimaryKey(userInfo);
        }
        log.info("end gift balance...");
    }

    // 每天凌晨删除30天前历史记录
    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT+8")
    @Transactional
    public void delHistory() {
        log.info("start del history...");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date thirtyDaysAgo = calendar.getTime();

        Query query = new Query();
        query.addCriteria(Criteria.where("createDate").lt(thirtyDaysAgo));

        DeleteResult result = mongoTemplate.remove(query, ChatSession.class);
        log.info("del history sum：{}", result.getDeletedCount());
        log.info("end del history...");
    }
}