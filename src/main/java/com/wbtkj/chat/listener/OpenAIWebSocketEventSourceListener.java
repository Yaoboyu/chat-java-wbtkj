package com.wbtkj.chat.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbtkj.chat.config.StaticContextAccessor;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletionResponse;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.service.RoleService;
import com.wbtkj.chat.service.impl.RoleServiceImpl;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

/**
 * 描述：OpenAI流式输出Socket接收
 *
 * @author https:www.unfbx.com
 * @date 2023-03-23
 */
@Slf4j
@NoArgsConstructor
public class OpenAIWebSocketEventSourceListener extends EventSourceListener {

    private WebSocketSession session;

    private StringBuilder message;


    public OpenAIWebSocketEventSourceListener(WebSocketSession session) {
        this.session = session;
        this.message = new StringBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(EventSource eventSource, Response response) {

    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (data.equals("[DONE]")) {
            session.sendMessage(new TextMessage("[DONE]"));
            RoleService roleService = StaticContextAccessor.getBean(RoleService.class);
            roleService.addReturnToWSChatSessionMessageList(session.getId(), message.toString());
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ChatCompletionResponse completionResponse = mapper.readValue(data, ChatCompletionResponse.class); // 读取Json
        String delta = completionResponse.getChoices().get(0).getDelta().getContent();
        if (StringUtils.isBlank(delta)) {
            return;
        }
        message.append(delta);
        session.sendMessage(new TextMessage(delta));
    }


    @Override
    public void onClosed(EventSource eventSource) {
    }


    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }
}
