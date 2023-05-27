package com.wbtkj.chat.websocket;

import cn.hutool.json.JSONUtil;
import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.listener.OpenAIWebSocketEventSourceListener;
import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.mapper.UserMapper;
import com.wbtkj.chat.mapper.UserRoleMapper;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.dto.role.WSChatSession;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.ThirdPartyModelKeyValue;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.model.ChatSession;
import com.wbtkj.chat.pojo.model.Role;
import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.vo.role.WSChatMessage;
import com.wbtkj.chat.service.OpenAiStreamService;
import com.wbtkj.chat.service.RoleService;
import com.wbtkj.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {


    @Resource
    private OpenAiStreamService openAiStreamService;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
//    @Resource
//    private MongoTemplate mongoTemplate;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * socket 建立成功事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    @Transactional
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String token = session.getHandshakeHeaders().get("authorization").get(0);
            UserLocalDTO userLocalDTO = userService.checkToken(token);
            WSChatSession wsChatSession = WSChatSession.builder()
                    .userId(userLocalDTO.getId())
                    .email(userLocalDTO.getEmail())
                    .role(null)
                    .chatSessionId(null)
                    .messageList(null).build();
            String key = RedisKeyConstant.ws_chat_session.getKey() + session.getId();
            redisTemplate.opsForValue().set(key, wsChatSession);

            WsSessionManager.add(session.getId(), session);
            WsSessionManager.addOnlineCount();
        } catch (MyServiceException e) {
            session.close();
            throw e;
        } catch (Exception e) {
            session.close();
            throw new MyServiceException("会话连接失败");
        }

    }

    /**
     * 接收消息事件
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    @Transactional
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 取wsChatSession
        String key = RedisKeyConstant.ws_chat_session.getKey() + session.getId();
        WSChatSession wsChatSession = (WSChatSession) redisTemplate.opsForValue().get(key);
        log.info("[连接:{}] 收到消息:{}", wsChatSession.getUserId(), message.getPayload());
        WSChatMessage wsChatMessage = JSONUtil.toBean(message.getPayload(), WSChatMessage.class);

        if (StringUtils.isBlank(wsChatMessage.getMessage())) {
            return;
        }

        if (wsChatMessage.getRoleId() == null) {
            session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "缺少roleId"));
            return;
        }

        Role role = roleMapper.selectByPrimaryKey(wsChatMessage.getRoleId());

        // 扣除用户余额
        int point = ThirdPartyModelKeyValue.getValue(role.getModel());
        int newBalance = userService.deductBalance(wsChatSession.getUserId(), point);
        session.sendMessage(new TextMessage("{{wbtkj_newBalance}}:" + newBalance));
        // 返现
        if (role.getUserId() != 0 && !role.getUserId().equals(wsChatSession.getUserId())) { //不是官方角色并且不是角色主人
            userService.cashBack(role.getUserId(), point);
        }
        // 增加UserRoleUsed
        roleService.addUserRoleUsed(role.getId(), wsChatSession.getUserId(), point);


        // 更新wsChatSession
        if (wsChatSession.getRole() == null
                || !wsChatSession.getRole().getId().equals(wsChatMessage.getRoleId())) { // 新角色
            roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());

            wsChatSession.setRole(role);
            if (wsChatMessage.getChatSessionId() == null) { // 新对话
                ChatSession newChatSession = roleService.addChatSession(wsChatSession.getUserId(), role.getId());
                wsChatSession.setChatSessionId(newChatSession.getChatSessionId());
                wsChatSession.setMessageList(new ArrayList<>());
                session.sendMessage(new TextMessage("{{wbtkj_chatSessionId}}:" + newChatSession.getChatSessionId()));

            } else { // 加载历史对话
                ChatSession chatSession = roleService.getChatSessionById(wsChatMessage.getChatSessionId());
                if (chatSession == null) {
                    session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "chatSessionId错误"));
                    return;
                }
                wsChatSession.setChatSessionId(wsChatMessage.getChatSessionId());
                wsChatSession.setMessageList(chatSession.getMessages());
            }
        } else {
            if (wsChatMessage.getChatSessionId() == null) { // 新对话
                roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());
                ChatSession newChatSession = roleService.addChatSession(wsChatSession.getUserId(), wsChatSession.getRole().getId());
                wsChatSession.setChatSessionId(newChatSession.getChatSessionId());
                wsChatSession.setMessageList(new ArrayList<>());
                session.sendMessage(new TextMessage("{{wbtkj_chatSessionId}}:" + newChatSession.getChatSessionId()));

            } else if (!wsChatMessage.getChatSessionId().equals(wsChatSession.getChatSessionId())) { // 加载历史对话
                roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());
                ChatSession chatSession = roleService.getChatSessionById(wsChatMessage.getChatSessionId());
                wsChatSession.setChatSessionId(wsChatMessage.getChatSessionId());
                wsChatSession.setMessageList(chatSession.getMessages());
            }
        }
        List<Message> messages = new ArrayList<>(wsChatSession.getMessageList());

        wsChatSession.getMessageList().add(Message.builder().content(wsChatMessage.getMessage()).role(Message.Role.USER).build());
        redisTemplate.opsForValue().set(key, wsChatSession);

        messages = messages.subList(Math.max(0, messages.size() - role.getContextN()*2), messages.size());
        messages.add(0, Message.builder().content(role.getSystem()).role(Message.Role.SYSTEM).build());
        messages.add(Message.builder().content(wsChatMessage.getMessage()).role(Message.Role.USER).build());

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(role.getModel())
                .messages(messages)
                .temperature(role.getTemperature())
                .topP(role.getTopP())
//                .stop(Arrays.asList(role.getStop()))
                .maxTokens(role.getMaxTokens())
                .presencePenalty(role.getPresencePenalty())
                .frequencyPenalty(role.getFrequencyPenalty())
                .logitBias(JSONUtil.toBean(role.getLogitBias(), HashMap.class))
                .build();

        OpenAIWebSocketEventSourceListener eventSourceListener = new OpenAIWebSocketEventSourceListener(session);
        openAiStreamService.streamChatCompletion(chatCompletion, eventSourceListener);


    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    @Transactional
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 用户退出，移除缓存
        WsSessionManager.remove(session.getId());


        String key = RedisKeyConstant.ws_chat_session.getKey() + session.getId();
        WSChatSession wsChatSession = (WSChatSession) redisTemplate.opsForValue().getAndDelete(key);
        roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());

        WsSessionManager.subOnlineCount();
    }






}