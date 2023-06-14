package com.wbtkj.chat.service;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.model.UserInfo;
import com.wbtkj.chat.pojo.vo.PageInfoVO;
import com.wbtkj.chat.pojo.vo.admin.AdminUserInfoVO;
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

    /**
     * 修改密码
     * @param pwd
     * @return
     */
    boolean changePwd(String pwd);

    /**
     * 可以修改密码，状态，会员有效期，余额，备注
     * @param userInfo
     * @return
     */
    boolean updateUser(UserInfo userInfo);

    /**
     * 检查用户是否存在，是否已被禁用
     * @param email
     * @return
     */
    UserInfo getCheckedUser(String email) throws MyServiceException;

    /**
     * 解析token并将用户信息存入ThreadLocal
     * @param token
     * @return
     */
    UserLocalDTO checkToken(String token) throws MyServiceException;

    /**
     * 管理员分页并模糊查询email
     * @param page
     * @param pageSize
     * @param email
     * @return
     */
    List<AdminUserInfoVO> getUsersByPage(int page, int pageSize, String email);

    /**
     * 管理员获取用户分页信息
     * @param pageSize
     * @param email
     * @return
     */
    PageInfoVO getUsersPage(int pageSize, String email);

    /**
     * 用户获取用户信息
     * @return
     */
    UserInfoVO getUserInfo();

    /**
     * 返现
     * @param userId 增加返现的账户
     * @param point 返现点数
     * @param rate 返现比例
     * @return
     */
    boolean cashBack(long userId, int point, double rate);

    /**
     * 增加用户余额
     * @param userId
     * @param point
     * @return
     */
    int addBalance(long userId, int point);

    /**
     * 扣除用户余额
     * @param userId
     * @param point
     * @return
     */
    int deductBalance(long userId, int point);

    /**
     * 检查用户余额
     * @param userId
     * @param point
     * @return
     */
    boolean checkBalance(long userId, int point);
}
