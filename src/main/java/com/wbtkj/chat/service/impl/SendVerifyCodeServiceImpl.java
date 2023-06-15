package com.wbtkj.chat.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.service.SendVerifyCodeService;
import com.wbtkj.chat.utils.MailUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class SendVerifyCodeServiceImpl implements SendVerifyCodeService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendMail(String email){
        if(!checkCanSendCode(email)) {
            throw new MyServiceException("请勿重复获取验证码，1分钟之后再试");
        }

        String code = RandomUtil.randomNumbers(6);
        // 发送成功，将 code 保存至 Redis
        String key = RedisKeyConstant.verify_code.getKey() + email;
        redisTemplate.opsForValue().set(key, code, RedisKeyConstant.verify_code.getExp(), TimeUnit.MINUTES);
        boolean res = MailUtils.SendCodeMail(email, code, RedisKeyConstant.verify_code.getExp());
        if(!res) {
            throw new MyServiceException("邮件发送失败，请稍后再试");
        }
    }

    @Override
    public boolean checkCanSendCode(String email) {
        String key = RedisKeyConstant.verify_code.getKey() + email;
        Long expire = redisTemplate.getExpire(key, TimeUnit.MINUTES);
        return expire == null || expire <= 3;
    }

    @Override
    public String getCodeByEmail(String email) {
        String key = RedisKeyConstant.verify_code.getKey() + email;
        return redisTemplate.opsForValue().get(key);
    }
}
