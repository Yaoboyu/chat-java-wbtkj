package com.wbtkj.chat.service.impl;

import com.github.pagehelper.PageHelper;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.mapper.AdminMapper;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.pojo.dto.admin.AdminLocalDTO;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.model.*;
import com.wbtkj.chat.service.AdminService;
import com.wbtkj.chat.utils.JwtUtils;
import com.wbtkj.chat.utils.MD5Utils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class AdminSerciveImpl implements AdminService {
    @Resource
    UserMapper userMapper;
    @Resource
    AdminMapper adminMapper;

    @Override
    public String login(String username, String pwd) {
        Admin admin = getCheckedAdmin(username);
        if(!admin.getPwd().equals(MD5Utils.code(pwd + admin.getSalt()))) {
            throw new MyServiceException("密码错误！");
        }

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", admin.getId());
        claims.put("username", username);

        return JwtUtils.generateJwt(claims);
    }

    @Override
    public Admin getCheckedAdmin(String username) throws MyServiceException {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andUsernameEqualTo(username);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        if(CollectionUtils.isEmpty(admins)) {
            throw new MyServiceException("用户名不存在");
        }
        Admin admin = admins.get(0);

        return admin;
    }

    @Override
    public boolean checkToken(String token) throws MyServiceException {
        //判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(token)){
            log.info("请求头token为空,返回未登录的信息");
            throw new MyServiceException("未登录");
        }

        //解析token，如果解析失败，返回错误结果（未登录）。
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Admin admin = getCheckedAdmin((String) claims.get("username"));
            AdminLocalDTO adminLocalDTO = new AdminLocalDTO();
            adminLocalDTO.setId(admin.getId());
            adminLocalDTO.setUsername(admin.getUsername());
            ThreadLocalConfig.setAdmin(adminLocalDTO);
        } catch (MyServiceException e) {//jwt解析失败
            log.info("解析令牌失败, 返回未登录错误信息");
            throw e;
        }

        return true;
    }

}
