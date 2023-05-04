package com.wbtkj.chat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.user.UserDTO;
import com.wbtkj.chat.pojo.dto.user.UserStatus;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.model.UserExample;
import com.wbtkj.chat.pojo.vo.user.RegisterVO;
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

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", email);
        pwd = MD5Utils.code(pwd + user.getSalt());

        if(!pwd.equals(user.getPwd())) {
            throw new MyServiceException("登录信息有误,请核对后输入!");
        }

        return JwtUtils.generateJwt(claims);
    }

    @Override
    public void register(RegisterVO registerVO) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(registerVO.getEmail());

        if(userMapper.countByExample(userExample) > 0) {
            throw new MyServiceException("注册失败:账号已被注册!");
        }

        Long time = System.currentTimeMillis();
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);

        User newUser = new User();
        newUser.setEmail(registerVO.getEmail());
        newUser.setSalt(String.valueOf((time % 100000)));
        newUser.setPwd(MD5Utils.code(registerVO.getPwd() + newUser.getSalt()));
        newUser.setStatus(UserStatus.ENABLED.getStatus());
        newUser.setQuota(GeneralConstant.USER_INIT_QUOTA);
        newUser.setCost(0L);
        newUser.setRemark("");
        newUser.setMy_inv_code("");
        newUser.setUse_inv_code("");
        newUser.setCreate_time(date);
        newUser.setUpdate_time(date);

        userMapper.insert(newUser);
    }

    @Override
    public void changePwd(String pwd, String code) {
        //TODO :发邮箱校验验证码


    }

    @Override
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
            if(user == null) {
                throw new MyServiceException("用户不存在或已被禁用");
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            ThreadLocalConfig.setgetUser(userDTO);
        } catch (MyServiceException e) {//jwt解析失败
            log.info("解析令牌失败, 返回未登录错误信息");
            throw e;
        }

        return true;
    }

}
