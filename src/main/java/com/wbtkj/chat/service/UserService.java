package com.wbtkj.chat.service;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.vo.user.UserInfoVO;
import com.wbtkj.chat.pojo.vo.user.UserRegisterVO;

import java.util.List;

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
     * @return
     */
    boolean register(UserRegisterVO userRegisterVO);

    boolean changePwd(String pwd);

    /**
     * 可以修改密码，状态，会员有效期，余额，备注
     * @param user
     * @return
     */
    boolean updateUser(User user);

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
    UserLocalDTO checkToken(String token) throws MyServiceException;

    /**
     * 分页并模糊查询email
     * @param page
     * @param pageSize
     * @param email
     * @return
     */
    List<User> getUsersByPage(int page, int pageSize, String email);

    UserInfoVO getUserInfo();

    /**
     * 返现
     * @param userId 增加返现的账户
     * @param point 返现点数
     * @return
     */
    boolean cashBack(Long userId, int point);

    /**
     * 扣除用户余额
     * @param userId
     * @param point
     * @return
     */
    int deductBalance(long userId, int point);
}
