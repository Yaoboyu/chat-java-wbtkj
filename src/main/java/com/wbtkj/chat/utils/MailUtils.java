package com.wbtkj.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConfigurationProperties(prefix = "email")
public class MailUtils {
    private static String emailFrom; // 邮箱账号
    private static String Au; // 令牌
    private static String hostname; // 邮箱服务器
    private static int exp; // 有效期

    public void setEmailFrom(String emailFrom) {
        MailUtils.emailFrom = emailFrom;
    }

    public void setAu(String au) {
        Au = au;
    }

    public void setHostname(String hostname) {
        MailUtils.hostname = hostname;
    }

    public void setExp(int exp) {
        MailUtils.exp = exp;
    }

    /**
     * 发送邮箱验证码
     * @param emailTo 目标邮箱账号
     * @param code 验证码
     * @param exp 过期时间（分钟）
     * @return 是否发送成功
     */
    public static boolean SendCodeMail(String emailTo, String code, int exp){
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(hostname);
            email.setCharset("utf-8");
            email.addTo(emailTo);
            email.setFrom(emailFrom);
            email.setAuthentication(emailFrom,Au);
            email.setSubject("乌邦图科技:您好,请查收您的注册验证码");//设置发送主题
            email.setMsg("您的验证码是：" + code + "，" + exp + "分钟有效，请注意查收。");//设置发送内容
            email.send();//进行发送
        } catch (Exception e) {
            log.error("邮件发送失败。from: {}, to: {}, code: {}", emailFrom, emailTo, code);
            return false;
        }
        return true;
    }
}
