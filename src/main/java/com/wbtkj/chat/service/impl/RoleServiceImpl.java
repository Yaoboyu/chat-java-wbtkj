package com.wbtkj.chat.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.mapper.UserRoleMapper;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.dto.role.UserRoleStatus;
import com.wbtkj.chat.pojo.dto.role.WSChatSession;
import com.wbtkj.chat.pojo.model.*;
import com.wbtkj.chat.pojo.vo.role.RoleBriefVO;
import com.wbtkj.chat.pojo.vo.role.RoleHistoryVO;
import com.wbtkj.chat.pojo.vo.role.RoleInfoVO;
import com.wbtkj.chat.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    RoleMapper roleMapper;

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    @Transactional
    public List<Object> getUserRole() {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(ThreadLocalConfig.getUser().getId())
                .andStatusEqualTo(UserRoleStatus.ENABLED.getStatus());
        userRoleExample.setOrderByClause("top desc");

        List<UserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);
        // 用户拥有的角色ids
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        List<Role> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            roles.add(getRole(roleId));
        }

        Map<Long, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, role -> role));

        List<Object> res = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            Role role = roleMap.get(userRole.getRoleId());
            if(role.getUserId().equals(ThreadLocalConfig.getUser().getId())) {
                res.add(role);
            } else {
                res.add(new RoleBriefVO(role));
            }
        }

        return res;
    }

    @Override
    @Transactional
    public List<RoleBriefVO> getShopRole(Integer page, Integer pageSize, Integer type, String name) {
        if (page < 1 || pageSize < 1) {
            throw new MyServiceException("参数错误");
        }

        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIsMarketEqualTo(true);
        if (type != null) {
            criteria.andMarketTypeEqualTo(type);
        }
        if (name != null) {
            criteria.andNicknameLike("%" + name + "%");
        }
        roleExample.setOrderByClause("hot desc, update_time desc");

        RowBounds rowBounds = new RowBounds((page-1)*pageSize, pageSize);
        List<Role> roles = roleMapper.selectByExampleWithRowbounds(roleExample, rowBounds);
        List<RoleBriefVO> res = roles.stream().map(RoleBriefVO::new).collect(Collectors.toList());

        return res;
    }

    @Override
    @Transactional
    public boolean addRole(RoleInfoVO roleInfo) {
        checkRoleNum();

        Role role = roleInfo.toRole(ThreadLocalConfig.getUser().getId());

        // 如果上架，不能同名
        if (role.getIsMarket()) {
            RoleExample roleExample = new RoleExample();
            roleExample.createCriteria().andIsMarketEqualTo(true).andNicknameEqualTo(role.getNickname());
            if (roleMapper.countByExample(roleExample) > 0) {
                throw new MyServiceException("已存在同名角色，请修改角色昵称或选择不上架市场");
            }
        }

        roleMapper.insert(role);

        addRoleById(role.getId(), true);

        return true;
    }

    @Override
    @Transactional
    public boolean updateRole(RoleInfoVO roleInfo) {
        Role newRole = roleInfo.toRole();
        if (newRole.getId() == null) {
            throw new MyServiceException("id不能为空");
        }

        Role oldRole = roleMapper.selectByPrimaryKey(newRole.getId());

        if (!oldRole.getUserId().equals(ThreadLocalConfig.getUser().getId())) {
            throw new MyServiceException("角色的拥有者才能修改角色");
        }

        if (oldRole.getIsMarket() == true && newRole.getIsMarket() == false) {
            throw new MyServiceException("已上架角色不能下架");
        } else if (newRole.getIsMarket() == true) { // 如果上架，不能同名
            if (newRole.getNickname() == null) {
                newRole.setNickname(oldRole.getNickname());
            }
            RoleExample roleExample = new RoleExample();
            roleExample.createCriteria()
                    .andIsMarketEqualTo(true)
                    .andNicknameEqualTo(newRole.getNickname())
                    .andIdNotEqualTo(newRole.getId());
            if (roleMapper.countByExample(roleExample) > 0) {
                throw new MyServiceException("已存在同名角色，请修改角色昵称或选择不上架市场");
            }
        }

        roleMapper.updateByPrimaryKeySelective(newRole);

        newRole = roleMapper.selectByPrimaryKey(newRole.getId());
        setRole(newRole);
        return true;
    }

    @Override
    @Transactional
    public boolean addRoleById(long roleId, boolean isNew) {
        Role role = roleMapper.selectByPrimaryKey(roleId);

        if (role == null) {
            throw new MyServiceException("该角色不存在");
        }

        if (!role.getIsMarket() && !isNew) {
            throw new MyServiceException("该角色未上架");
        }

        List<UserRole> userRoles = checkRoleNum();

        if (userRoles.stream().anyMatch(userRole -> userRole.getRoleId().equals(roleId))) {
            throw new MyServiceException("已拥有该角色");
        }

        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(ThreadLocalConfig.getUser().getId());
        userRole.setTop(false);
        userRole.setStatus(UserRoleStatus.ENABLED.getStatus());
        userRole.setUsed(0);
        userRoleMapper.insert(userRole);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteRole(long roleId) {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andRoleIdEqualTo(roleId).andUserIdEqualTo(ThreadLocalConfig.getUser().getId());
        List<UserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);

        if (CollectionUtils.isEmpty(userRoles)) {
            throw new MyServiceException("该角色不存在");
        }

        userRoleMapper.deleteByPrimaryKey(userRoles.get(0).getId());

        return true;
    }

    @Override
    @Transactional
    public List<RoleHistoryVO> getRoleHistory(long roleId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Query query = new Query(Criteria.where("userId").is(ThreadLocalConfig.getUser().getId()).and("roleId").is(roleId));
        List<ChatSession> chatSessions = mongoTemplate.find(query.with(sort), ChatSession.class);

        List<RoleHistoryVO> res = chatSessions.stream().filter(chatSession ->
            !CollectionUtils.isEmpty(chatSession.getMessages())
        ).map(RoleHistoryVO::new).collect(Collectors.toList());

        return res;
    }

    @Override
    @Transactional
    public ChatSession addChatSession(long userId, long roleId) {
        ChatSession messages = ChatSession.builder()
                .roleId(roleId).userId(userId).createDate(new Date())
                .messages(new ArrayList<>())
                .build();
        return mongoTemplate.save(messages);
    }

    @Override
    @Transactional
    public ChatSession getChatSessionById(String chatSessionId, long userId) {
        ChatSession res = mongoTemplate.findById(chatSessionId, ChatSession.class);
        if (res != null && !res.getUserId().equals(userId)) {
            return null;
//            throw new MyServiceException("会话不存在");
        }
        return res;
    }

    @Override
    @Transactional
    public boolean updateChatSession(String chatSessionId, List<Message> messageList) {
        if (StringUtils.isBlank(chatSessionId) || CollectionUtils.isEmpty(messageList)) {
            return false;
        }
        Query query = new Query(Criteria.where("chatSessionId").is(chatSessionId));
        Update update = new Update();
        update.set("messages", messageList);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ChatSession.class);
        return updateResult.getModifiedCount() == 1;
    }

    @Override
    @Transactional
    public boolean addReturnToWSChatSessionMessageList(String wsSessionId, String message) {
        try {
            String key = RedisKeyConstant.ws_chat_session.getKey() + wsSessionId;
            WSChatSession wsChatSession = (WSChatSession) redisTemplate.opsForValue().get(key);
            wsChatSession.getMessageList().add(new Message(Message.Role.ASSISTANT.getName(), message, null));
            redisTemplate.opsForValue().set(key, wsChatSession);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public int addUserRoleUsed(long roleId, long userId, int point) {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andRoleIdEqualTo(roleId).andUserIdEqualTo(userId);
        List<UserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);
        if (CollectionUtils.isEmpty(userRoles)) {
            throw new MyServiceException("未添加该角色");
        }
        UserRole userRole = userRoles.get(0);
        userRole.setUsed(userRole.getUsed() + point);
        userRoleMapper.updateByPrimaryKey(userRole);
        return userRole.getUsed();
    }

    @Override
    public boolean checkUserRole(long roleId, long userId) {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andRoleIdEqualTo(roleId).andUserIdEqualTo(userId);
        List<UserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);

        return !CollectionUtils.isEmpty(userRoles);
    }

    @Override
    public Role getRole(long roleId) {
        try {
            Role role = (Role) redisTemplate.opsForValue().get("Role:" + roleId);
            if(role == null){
                role = roleMapper.selectByPrimaryKey(roleId);
                setRole(role);
            }
            return role;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setRole(Role role){
        if (role == null) {
            return;
        }
        redisTemplate.opsForValue().set(RedisKeyConstant.role.getKey() + role.getId(), role,
                RedisKeyConstant.role.getExp(), TimeUnit.MINUTES);
    }

    private List<UserRole> checkRoleNum() {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(ThreadLocalConfig.getUser().getId());
        List<UserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);

        if (userRoles.size() >= GeneralConstant.USER_NORMAL_MAX_ROLE) {
            throw new MyServiceException("拥有的角色数量已达上限");
        }
        // TODO : vip

        return userRoles;
    }
}
