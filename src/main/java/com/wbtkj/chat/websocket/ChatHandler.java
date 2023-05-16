package com.wbtkj.chat.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.Message;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.listener.OpenAIWebSocketEventSourceListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {
    private static OpenAiStreamClient openAiStreamClient;

    @Autowired
    public void setOrderService(OpenAiStreamClient openAiStreamClient) {
        this.openAiStreamClient = openAiStreamClient;
    }

    private Long id; // 用户id
    private String email; // 用户email

    /**
     * socket 建立成功事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 用户连接成功，放入在线用户缓存
        this.id = ThreadLocalConfig.getUser().getId();
        this.email = ThreadLocalConfig.getUser().getEmail();
        WsSessionManager.add(this.email, session);
        WsSessionManager.addOnlineCount();
    }

    /**
     * 接收消息事件
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("[连接email:{}] 收到消息:{}", this.email, message.getPayload());
        //接受参数
        OpenAIWebSocketEventSourceListener eventSourceListener = new OpenAIWebSocketEventSourceListener(session);
//        String messageContext = (String) LocalCache.CACHE.get(uid);
        String messageContext = "";
        List<Message> messages = new ArrayList<>();
        if (StrUtil.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, Message.class);
            if (messages.size() >= 10) {
                messages = messages.subList(1, 10);
            }
            Message currentMessage = Message.builder().content(message.getPayload()).role(Message.Role.USER).build();
            messages.add(currentMessage);
        } else {
            Message currentMessage = Message.builder().content(message.getPayload()).role(Message.Role.USER).build();
            messages.add(currentMessage);
        }
        openAiStreamClient.streamChatCompletion(messages, eventSourceListener);
//        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 用户退出，移除缓存
        WsSessionManager.remove(this.email);

        ThreadLocalConfig.remove();

        WsSessionManager.subOnlineCount();
    }






}