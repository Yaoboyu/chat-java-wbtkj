package com.wbtkj.chat.service;


public interface SendVerifyCodeService {
    /**
     * 发送验证码
     * @param email
     * @return
     */
    boolean sendMail(String email);

    /**
     * 根据手机号查询是否已生成验证码
     * @param email
     * @return
     */
    boolean checkCodeIsExist(String email);

    /**
     * 根据手机号获取验证码
     * @param email
     * @return 失效则返回null
     */
    String getCodeByEmail(String email);
}
