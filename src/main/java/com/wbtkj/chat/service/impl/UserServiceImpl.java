package com.wbtkj.chat.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.UserInfoMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.mapper.UserRoleMapper;
import com.wbtkj.chat.pojo.dto.role.UserRoleStatus;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.dto.user.UserStatus;
import com.wbtkj.chat.pojo.model.UserInfo;
import com.wbtkj.chat.pojo.model.UserInfoExample;
import com.wbtkj.chat.pojo.model.UserRole;
import com.wbtkj.chat.pojo.vo.user.UserInfoVO;
import com.wbtkj.chat.pojo.vo.user.UserRegisterVO;
import com.wbtkj.chat.service.UserService;
import com.wbtkj.chat.utils.JwtUtils;
import com.wbtkj.chat.utils.TimeUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.utils.MD5Utils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public String login(String email, String pwd) {
        UserInfo userInfo = getCheckedUser(email);
        if(!userInfo.getPwd().equals(MD5Utils.code(pwd + userInfo.getSalt()))) {
            throw new MyServiceException("密码错误！");
        }

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("email", email);

        return JwtUtils.generateJwt(claims);
    }

    @Override
    @Transactional
    public boolean register(UserRegisterVO userRegisterVO) {
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andEmailEqualTo(userRegisterVO.getEmail());

        if(userInfoMapper.countByExample(userInfoExample) > 0) {
            throw new MyServiceException("账号已被注册!");
        }

        if(userRegisterVO.getPwd() == null || userRegisterVO.getPwd().length() < 8) {
            throw new MyServiceException("密码至少8位！");
        }

        Long useInvCode = null;
        if (userRegisterVO.getInvCode() != null) {
            userInfoExample.clear();
            userInfoExample.createCriteria().andMyInvCodeEqualTo(userRegisterVO.getInvCode());
            List<UserInfo> userInfoList = userInfoMapper.selectByExample(userInfoExample);
            if(CollectionUtils.isEmpty(userInfoList)) {
                throw new MyServiceException("邀请码错误！");
            }
            UserInfo userInfo = userInfoList.get(0);
            useInvCode = userInfo.getId();
            addBalance(useInvCode, GeneralConstant.INVITE_GIFT_BALANCE);
        }

        Date date = TimeUtils.getTimeGMT8();

        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setEmail(userRegisterVO.getEmail());
        newUserInfo.setSalt(RandomUtil.randomStringUpper(5));
        newUserInfo.setPwd(MD5Utils.code(userRegisterVO.getPwd() + newUserInfo.getSalt()));
        newUserInfo.setStatus(UserStatus.ENABLED.getStatus());
        newUserInfo.setBalance(GeneralConstant.USER_INIT_BALANCE);
        newUserInfo.setCash(0.);
        newUserInfo.setRemark("");
        newUserInfo.setMyInvCode("");
        newUserInfo.setUseInvCode(useInvCode);
        newUserInfo.setCreateTime(date);
        newUserInfo.setUpdateTime(date);

        userInfoMapper.insert(newUserInfo);

        // 设置全局唯一邀请码
        newUserInfo.setMyInvCode(RandomUtil.randomStringUpper(3) + newUserInfo.getId() + RandomUtil.randomStringUpper(3));
        userInfoMapper.updateByPrimaryKey(newUserInfo);

        // 默认角色
        UserRole userRole = new UserRole();
        userRole.setRoleId(1L);
        userRole.setUserId(newUserInfo.getId());
        userRole.setUsed(0);
        userRole.setStatus(UserRoleStatus.ENABLED.getStatus());
        userRole.setTop(false);
        userRoleMapper.insert(userRole);

        return true;
    }

    @Override
    public boolean changePwd(String pwd) {
        if(pwd.length() < 8) {
            throw new MyServiceException("密码至少8位！");
        }

        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andIdEqualTo(ThreadLocalConfig.getUser().getId());

        UserInfo newUserInfo = userInfoMapper.selectByPrimaryKey(ThreadLocalConfig.getUser().getId());

        if(newUserInfo.getPwd().equals(MD5Utils.code(pwd + newUserInfo.getSalt()))) {
            throw new MyServiceException("新密码与原密码不能相同！");
        }

        newUserInfo.setPwd(MD5Utils.code(pwd + newUserInfo.getSalt()));
        newUserInfo.setUpdateTime(TimeUtils.getTimeGMT8());

        userInfoMapper.updateByPrimaryKey(newUserInfo);

        return true;
    }

    @Override
    @Transactional
    public boolean updateUser(UserInfo userInfo) {
        Long id = userInfo.getId();
        if(id == null) {
            throw new MyServiceException("缺少user id");
        }

        UserInfo oldUserInfo = userInfoMapper.selectByPrimaryKey(id);

        if(userInfo.getPwd() != null) {
            oldUserInfo.setPwd(MD5Utils.code(userInfo.getPwd() + oldUserInfo.getSalt()));
        }

        if(userInfo.getStatus() != null) {
            oldUserInfo.setStatus(userInfo.getStatus());
        }

        if(userInfo.getVipStartTime() != null) {
            oldUserInfo.setVipStartTime(userInfo.getVipStartTime());
        }

        if(userInfo.getVipEndTime() != null) {
            oldUserInfo.setVipEndTime(userInfo.getVipEndTime());
        }

        if(userInfo.getBalance() != null) {
            oldUserInfo.setBalance(userInfo.getBalance());
        }

        if(userInfo.getCash() != null) {
            oldUserInfo.setCash(userInfo.getCash());
        }

        if(userInfo.getRemark() != null) {
            oldUserInfo.setRemark(userInfo.getRemark());
        }

        oldUserInfo.setUpdateTime(TimeUtils.getTimeGMT8());

        userInfoMapper.updateByPrimaryKey(oldUserInfo);

        return true;
    }

    @Override
    @Transactional
    public UserInfo getCheckedUser(String email) throws MyServiceException {
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andEmailEqualTo(email);
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        if(CollectionUtils.isEmpty(userInfos)) {
            throw new MyServiceException("用户邮箱不存在");
        }
        UserInfo userInfo = userInfos.get(0);
        if(userInfo.getStatus() == UserStatus.DISABLED.getStatus()) {
            throw new MyServiceException("用户已禁用！请联系管理员");
        }

        return userInfo;
    }

    @Override
    public UserLocalDTO checkToken(String token) throws MyServiceException{
        //判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(token)){
            log.info("请求头token为空,返回未登录的信息");
            throw new MyServiceException("token过期");
        }

        //解析token，如果解析失败，返回错误结果（未登录）。
        try {
            Claims claims = JwtUtils.parseJWT(token);
            UserInfo userInfo = getCheckedUser((String) claims.get("email"));
            UserLocalDTO userLocalDTO = UserLocalDTO.builder().id(userInfo.getId()).email(userInfo.getEmail()).build();
            ThreadLocalConfig.setUser(userLocalDTO);
            return userLocalDTO;
        } catch (Exception e) {//jwt解析失败
            log.info("解析令牌失败, 返回未登录错误信息");
            throw new MyServiceException("token过期");
        }

    }

    @Override
    @Transactional
    public List<UserInfo> getUsersByPage(int page, int pageSize, String email) {
        if (page < 1 || pageSize < 1) {
            throw new MyServiceException("参数错误");
        }

        UserInfoExample userInfoExample = new UserInfoExample();
        if(StringUtils.hasLength(email)) {
            userInfoExample.createCriteria().andEmailLike("%" + email + "%");
        }

        RowBounds rowBounds = new RowBounds((page-1)*pageSize, pageSize);
        List<UserInfo> userInfos = userInfoMapper.selectByExampleWithRowbounds(userInfoExample, rowBounds);

        return userInfos;
    }

    @Override
    @Transactional
    public UserInfoVO getUserInfo() {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(ThreadLocalConfig.getUser().getId());
        UserInfoVO userInfoVO = new UserInfoVO(userInfo);
        return userInfoVO;
    }

    @Override
    @Transactional
    public boolean cashBack(long userId, int point, double rate) {
        UserInfo roleOwner = userInfoMapper.selectByPrimaryKey(userId);
        roleOwner.setCash(roleOwner.getCash() + point * GeneralConstant.POINT_RATE * rate);
        userInfoMapper.updateByPrimaryKey(roleOwner);
        return true;
    }

    @Override
    @Transactional
    public int addBalance(long userId, int point) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        userInfo.setBalance(userInfo.getBalance() + point);
        userInfoMapper.updateByPrimaryKey(userInfo);
        return userInfo.getBalance();
    }


    @Override
    @Transactional
    public int deductBalance(long userId, int point) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if (userInfo.getBalance() - point < 0) {
            throw new MyServiceException("余额不足");
        }
        userInfo.setBalance(userInfo.getBalance() - point);
        userInfoMapper.updateByPrimaryKey(userInfo);
        return userInfo.getBalance();
    }

    @Override
    @Transactional
    public boolean checkBalance(long userId, int point) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if (userInfo.getBalance() - point < 0) {
            return false;
        }
        return true;
    }
}
