package com.wbtkj.chat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.dto.role.WSChatSession;
import com.wbtkj.chat.pojo.model.ChatSession;
import com.wbtkj.chat.pojo.vo.role.RoleHistoryVO;
import com.wbtkj.chat.pojo.vo.role.RoleInfoVO;
import com.wbtkj.chat.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    public List<JSONObject> getRole() {
        return null;
    }

    @Override
    public boolean addRole(RoleInfoVO roleInfo) {
        return false;
    }

    @Override
    public boolean updateRole(RoleInfoVO roleInfo) {
        return false;
    }

    @Override
    public boolean addRoleById(long roleId) {
        return false;
    }

    @Override
    public List<RoleHistoryVO> getRoleHistory(long roleId) {
        return null;
    }

    @Override
    public ChatSession addChatSession(long userId, long roleId) {
        ChatSession messages = ChatSession.builder()
                .roleId(roleId).userId(userId).createDate(new Date())
                .messages(new ArrayList<>())
                .build();
        return mongoTemplate.save(messages);
    }

    @Override
    public ChatSession getChatSessionById(String chatSessionId) {
        return mongoTemplate.findById(chatSessionId, ChatSession.class);
    }

    @Override
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
}
