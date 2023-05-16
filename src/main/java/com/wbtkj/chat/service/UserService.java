package com.wbtkj.chat.service;

import com.github.pagehelper.PageInfo;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.vo.user.UserRegisterVO;

public interface UserService {
    /**
     * 登录验证
     * @param email 用户邮箱
     * @param pwd   用户密码
     * @return      返回jwt令牌
     */
    String login(String email, String pwd);

    /**
     * 注册
     * @param userRegisterVO
     */
    void register(UserRegisterVO userRegisterVO);

    void changePwd(String pwd,String code);

    /**
     * 可以修改密码，状态，会员有效期，余额，备注
     * @param user
     */
    void updateUser(User user);

    /**
     * 检查用户是否存在，是否已被禁用
     * @param email
     * @return
     */
    User getCheckedUser(String email) throws MyServiceException;

    /**
     * 解析token并将用户信息存入ThreadLocal
     * @param token
     * @return
     */
    boolean checkToken(String token) throws MyServiceException;

    /**
     * 分页并模糊查询email
     * @param page
     * @param pageSize
     * @param email
     * @return
     */
    PageInfo<User> getUsersByPage(Integer page, Integer pageSize, String email);
}
