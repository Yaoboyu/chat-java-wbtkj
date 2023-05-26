package com.wbtkj.chat.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.dto.user.UserStatus;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.model.UserExample;
import com.wbtkj.chat.pojo.vo.user.UserInfo;
import com.wbtkj.chat.pojo.vo.user.UserRegisterVO;
import com.wbtkj.chat.service.UserService;
import com.wbtkj.chat.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
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
    private UserMapper userMapper;


    @Override
    public String login(String email, String pwd) {
        User user = getCheckedUser(email);
        if(!user.getPwd().equals(MD5Utils.code(pwd + user.getSalt()))) {
            throw new MyServiceException("密码错误！");
        }

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", email);

        return JwtUtils.generateJwt(claims);
    }

    @Override
    @Transactional
    public void register(UserRegisterVO userRegisterVO) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(userRegisterVO.getEmail());

        if(userMapper.countByExample(userExample) > 0) {
            throw new MyServiceException("账号已被注册!");
        }

        if(userRegisterVO.getPwd().length() < 8) {
            throw new MyServiceException("密码至少8位！");
        }

        userExample.clear();
        userExample.createCriteria().andMyInvCodeEqualTo(userRegisterVO.getInvCode());

        if(userMapper.countByExample(userExample) == 0) {
            throw new MyServiceException("邀请码不存在");
        }

        Long time = System.currentTimeMillis();
        Date date = new Date(time);

        User newUser = new User();
        newUser.setEmail(userRegisterVO.getEmail());
        newUser.setSalt(String.valueOf((time % 100000)));
        newUser.setPwd(MD5Utils.code(userRegisterVO.getPwd() + newUser.getSalt()));
        newUser.setStatus(UserStatus.ENABLED.getStatus());
        newUser.setBalance(GeneralConstant.USER_INIT_BALANCE);
        newUser.setCash(0.);
        newUser.setRemark("");
        newUser.setMyInvCode("");
        newUser.setUseInvCode(userRegisterVO.getInvCode());
        newUser.setCreateTime(date);
        newUser.setUpdateTime(date);

        userMapper.insert(newUser);

        // 设置全局唯一邀请码
        newUser = userMapper.selectByExample(userExample).get(0);
        newUser.setMyInvCode(RandomUtil.randomStringUpper(3) + newUser.getId() + RandomUtil.randomStringUpper(3));

        userMapper.updateByPrimaryKey(newUser);
    }

    @Override
    public void changePwd(String pwd) {
        if(pwd.length() < 8) {
            throw new MyServiceException("密码至少8位！");
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(ThreadLocalConfig.getUser().getId());

        User newUser = userMapper.selectByPrimaryKey(ThreadLocalConfig.getUser().getId());

        if(newUser.getPwd().equals(MD5Utils.code(pwd + newUser.getSalt()))) {
            throw new MyServiceException("新密码与原密码不能相同！");
        }

        newUser.setPwd(MD5Utils.code(pwd + newUser.getSalt()));
        newUser.setUpdateTime(new Date());

        userMapper.updateByPrimaryKey(newUser);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        Long id = user.getId();
        if(id == null) {
            throw new MyServiceException("缺少user id");
        }

        User oldUser = userMapper.selectByPrimaryKey(id);

        if(user.getPwd() != null) {
            oldUser.setPwd(MD5Utils.code(user.getPwd() + oldUser.getSalt()));
        }

        if(user.getStatus() != null) {
            oldUser.setStatus(user.getStatus());
        }

        if(user.getVipStartTime() != null) {
            oldUser.setVipStartTime(user.getVipStartTime());
        }

        if(user.getVipEndTime() != null) {
            oldUser.setVipEndTime(user.getVipEndTime());
        }

        if(user.getBalance() != null) {
            oldUser.setBalance(user.getBalance());
        }

        if(user.getRemark() != null) {
            oldUser.setRemark(user.getRemark());
        }

        oldUser.setUpdateTime(new Date());

        userMapper.updateByPrimaryKey(oldUser);

    }

    @Override
    @Transactional
    public User getCheckedUser(String email) throws MyServiceException {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(users)) {
            throw new MyServiceException("用户邮箱不存在");
        }
        User user = users.get(0);
        if(user.getStatus() == UserStatus.DISABLED.getStatus()) {
            throw new MyServiceException("用户已禁用！请联系管理员");
        }

        return user;
    }

    @Override
    public boolean checkToken(String token) throws MyServiceException{
        //判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(token)){
            log.info("请求头token为空,返回未登录的信息");
            throw new MyServiceException("未登录");
        }

        //解析token，如果解析失败，返回错误结果（未登录）。
        try {
            Claims claims = JwtUtils.parseJWT(token);
            User user = getCheckedUser((String) claims.get("email"));
            UserLocalDTO userLocalDTO = new UserLocalDTO();
            userLocalDTO.setId(user.getId());
            userLocalDTO.setEmail(user.getEmail());
            ThreadLocalConfig.setUser(userLocalDTO);
        } catch (MyServiceException e) {//jwt解析失败
            log.info("解析令牌失败, 返回未登录错误信息");
            throw e;
        }

        return true;
    }

    @Override
    @Transactional
    public PageInfo<User> getUsersByPage(Integer page, Integer pageSize, String email) {
        PageHelper.startPage(page, pageSize);
        UserExample userExample = new UserExample();
        if(StringUtils.hasLength(email)) {
            userExample.createCriteria().andEmailLike("%" + email + "%");
        }

        List<User> users = userMapper.selectByExample(userExample);

        //把查询的结果封装到PageInfo类中。
        PageInfo<User> pageInfo = new PageInfo<User>(users);

        return pageInfo;
    }

    @Override
    public UserInfo getUserInfo() {
        User user = userMapper.selectByPrimaryKey(ThreadLocalConfig.getUser().getId());
        UserInfo userInfo = new UserInfo(user);
        return userInfo;
    }

}
