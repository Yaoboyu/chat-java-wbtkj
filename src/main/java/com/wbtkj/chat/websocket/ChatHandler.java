package com.wbtkj.chat.websocket;

import cn.hutool.json.JSONUtil;
import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.constant.RedisKeyConstant;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.filter.OpenAiAuthInterceptor;
import com.wbtkj.chat.listener.OpenAIWebSocketEventSourceListener;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.dto.role.WSChatSession;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.model.ChatSession;
import com.wbtkj.chat.pojo.model.Role;
import com.wbtkj.chat.pojo.vo.role.WSChatMessage;
import com.wbtkj.chat.service.OpenAIService;
import com.wbtkj.chat.service.RoleService;
import com.wbtkj.chat.service.UserService;
import com.wbtkj.chat.utils.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {


    @Resource
    private OpenAIService openAIService;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private OpenAiAuthInterceptor openAiAuthInterceptor;
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
            String token = session.getHandshakeHeaders().get("sec-websocket-protocol").get(0);
            UserLocalDTO userLocalDTO = userService.checkToken(token);
            WSChatSession wsChatSession = WSChatSession.builder()
                    .userId(userLocalDTO.getId())
                    .email(userLocalDTO.getEmail())
                    .roleId(null)
                    .chatSessionId(null)
                    .messageList(null).build();
            String key = RedisKeyConstant.ws_chat_session.getKey() + session.getId();
            redisTemplate.opsForValue().set(key, wsChatSession, 30, TimeUnit.MINUTES);

            WsSessionManager.add(session.getId(), session);
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
        // 心跳检测
        if (message.getPayload().equals("{{wbtkj_heartbeat}}")) {
            session.sendMessage(new TextMessage("{{wbtkj_heartbeat_ack}}"));
            return;
        }
        // 取wsChatSession
        String key = RedisKeyConstant.ws_chat_session.getKey() + session.getId();
        WSChatSession wsChatSession = (WSChatSession) redisTemplate.opsForValue().get(key);
        if (wsChatSession == null) {
            session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "会话过期，请刷新网页"));
            session.close();
            return;
        }
        log.debug("[链接:{}, 用户:{}] 收到消息:{}", session.getId(), wsChatSession.getUserId(), message.getPayload());
        WSChatMessage wsChatMessage = JSONUtil.toBean(message.getPayload(), WSChatMessage.class);

        if (StringUtils.isBlank(wsChatMessage.getMessage())) {
            return;
        }

        if (wsChatMessage.getRoleId() == null) {
            session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "缺少roleId"));
            return;
        }

        Role role = roleService.getRole(wsChatMessage.getRoleId());
        if (role == null) {
            session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "角色不存在"));
            return;
        }

        if (!roleService.checkUserRole(wsChatMessage.getRoleId(), wsChatSession.getUserId())) {
            session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "未拥有该角色"));
            return;
        }

        if (!openAiAuthInterceptor.hasKey(MyUtils.getRealModel(role.getModel()))) {
            session.sendMessage(new TextMessage("{{wbtkj_error}}:" + role.getModel() + "暂不可用"));
            return;
        }


        // 更新wsChatSession
        if (wsChatSession.getRoleId() == null
                || !wsChatSession.getRoleId().equals(wsChatMessage.getRoleId())) { // 新角色
            roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());

            wsChatSession.setRoleId(role.getId());
            if (wsChatMessage.getChatSessionId() == null) { // 新对话
                ChatSession newChatSession = roleService.addChatSession(wsChatSession.getUserId(), role.getId());
                wsChatSession.setChatSessionId(newChatSession.getChatSessionId());
                wsChatSession.setMessageList(new ArrayList<>());
                session.sendMessage(new TextMessage("{{wbtkj_chatSessionId}}:" + newChatSession.getChatSessionId()));

            } else { // 加载历史对话
                ChatSession chatSession = roleService.getChatSessionById(wsChatMessage.getChatSessionId(), wsChatSession.getUserId());
                if (chatSession == null) {
                    session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "chatSessionId错误或不属于该用户"));
                    return;
                }
                wsChatSession.setChatSessionId(wsChatMessage.getChatSessionId());
                wsChatSession.setMessageList(chatSession.getMessages());
            }
        } else { // 老角色
            if (wsChatMessage.getChatSessionId() == null) { // 新对话
                roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());
                ChatSession newChatSession = roleService.addChatSession(wsChatSession.getUserId(), wsChatSession.getRoleId());
                wsChatSession.setChatSessionId(newChatSession.getChatSessionId());
                wsChatSession.setMessageList(new ArrayList<>());
                session.sendMessage(new TextMessage("{{wbtkj_chatSessionId}}:" + newChatSession.getChatSessionId()));

            } else if (!wsChatMessage.getChatSessionId().equals(wsChatSession.getChatSessionId())) { // 加载历史对话
                roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());
                ChatSession chatSession = roleService.getChatSessionById(wsChatMessage.getChatSessionId(), wsChatSession.getUserId());
                if (chatSession == null) {
                    session.sendMessage(new TextMessage("{{wbtkj_error}}:" + "chatSessionId错误或不属于该用户"));
                    return;
                }
                wsChatSession.setChatSessionId(wsChatMessage.getChatSessionId());
                wsChatSession.setMessageList(chatSession.getMessages());
            }
        }

        // 拼接message
        List<Message> messages = new ArrayList<>(wsChatSession.getMessageList());

        wsChatSession.getMessageList().add(Message.builder().content(wsChatMessage.getMessage()).role(Message.Role.USER).build());
        redisTemplate.opsForValue().set(key, wsChatSession);

        messages = messages.subList(Math.max(0, messages.size() - role.getContextN()), messages.size());
        messages.add(0, Message.builder().content(role.getSystem()).role(Message.Role.SYSTEM).build());
        messages.add(Message.builder().content(wsChatMessage.getMessage()).role(Message.Role.USER).build());

        // 构造chatCompletion
        //TODO: logitBias修改
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(MyUtils.getRealModel(role.getModel()))
                .messages(messages)
                .temperature(role.getTemperature())
                .topP(role.getTopP())
//                .stop(Arrays.asList(role.getStop()))
                .maxTokens(role.getMaxTokens())
                .presencePenalty(role.getPresencePenalty())
                .frequencyPenalty(role.getFrequencyPenalty())
//                .logitBias(JSONUtil.toBean(role.getLogitBias(), HashMap.class))
                .stream(true)
                .build();

        OpenAIWebSocketEventSourceListener eventSourceListener = new OpenAIWebSocketEventSourceListener(session, role, wsChatSession.getUserId());

        try {
            if (!CollectionUtils.isEmpty(role.getFileNames())) {
                openAIService.streamChatCompletionWithFile(role.getFileNames(), chatCompletion, eventSourceListener);
            } else {
                openAIService.streamChatCompletion(chatCompletion, eventSourceListener);

            }
        } catch (MyServiceException e) {
            session.sendMessage(new TextMessage("{{wbtkj_error}}:" + e.getMessage()));
            return;
        }
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
        if (wsChatSession != null) {
            roleService.updateChatSession(wsChatSession.getChatSessionId(), wsChatSession.getMessageList());
        }

    }






}
